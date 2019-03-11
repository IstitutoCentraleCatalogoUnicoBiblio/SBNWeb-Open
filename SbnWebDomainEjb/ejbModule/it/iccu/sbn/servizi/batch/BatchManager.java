/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.servizi.batch;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.BatchCleanerParamsVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.batch.job.BatchCheckIndiceConnectionJob;
import it.iccu.sbn.servizi.batch.job.BatchCleanerControllerJob;
import it.iccu.sbn.servizi.batch.job.BatchQueueControllerJob;
import it.iccu.sbn.servizi.batch.job.BatchScheduledControllerJob;
import it.iccu.sbn.servizi.batch.job.BatchStallControllerJob;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.ConfigChangeInterceptor;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.CreateException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.system.ServiceMBeanSupport;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

public class BatchManager extends ServiceMBeanSupport implements BatchManagerMBean, ConfigChangeInterceptor {

	private static final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	private static final int TRACE_DEPTH = Integer.MAX_VALUE;

	private static final Logger log = Logger.getLogger(BatchManager.class);
	private static final String STALL_MONITOR = "batch.stall.monitor";
	private static final String CLEANER_MONITOR = "batch.cleaner.monitor";
	private static final String INDICE_CHECK = "batch.indice.checker";
	private static final String SCHEDULED_GROUP = "batch.scheduled.group";
	private static final long REPEAT_INTERVAL = 60L * 10L * 1000L;	// 10 minuti

	private static final String CLEANER_CRON = "0 05 0 ? * *";	//ogni giorno alle 00:05

	private static final String FILE_LOCK = "/export/Trasf/flag.txt";

	private static final String[] OBSERVED_PROPS = new String[] {
		Configuration.BATCH_CLEANING_AGE_THRESHOLD,
		Configuration.BATCH_CLEANING_DELETE_OUTPUTS,
		Configuration.BATCH_USER_DELETE_OUTPUTS,
		Configuration.INDICE_CONNECTION_CHECKER_TIMEOUT
	};

	private final AtomicBoolean _started = new AtomicBoolean(false);
	private final List<String> tmpFilesDeletionQueue = Collections.synchronizedList(new ArrayList<String>());

	private Scheduler scheduler;
	private Map<Integer, Integer> runningJobs = new ConcurrentHashMap<Integer, Integer>();
	private BatchCleanerParamsVO cleanerParams = new BatchCleanerParamsVO(false, false, 0);
	private AtomicInteger deletedCounter = new AtomicInteger(0);
	private AtomicBoolean userDeletionDeleteOutputs = new AtomicBoolean(false);
	private RandomAccessFile lockFile = null;

	public static final BatchManagerMBean getBatchManagerInstance()
			throws Exception {

		MBeanServer mbserver = MBeanServerLocator.locateJBoss();
		ObjectName name = new ObjectName("sbn:service=BatchManager");
		BatchManagerMBean bm = (BatchManagerMBean) MBeanProxyExt.create(
				BatchManagerMBean.class, name, mbserver);

		return bm;
	}

	private List<CodaJMSVO> getBatchServizi() throws NamingException,
			RemoteException, DaoManagerException, CreateException {
		List<CodaJMSVO> list = new ArrayList<CodaJMSVO>();

		AmministrazionePolo ejbPolo = getAmministrazionePolo();
		list = ejbPolo.getListaCodeBatch();
		return list;

	}

	private AmministrazionePolo getAmministrazionePolo() throws NamingException,
			CreateException, RemoteException {

		return DomainEJBFactory.getInstance().getPolo();
	}

	private void addJobs(List<CodaJMSVO> jobs) throws NamingException, RemoteException, DaoManagerException, CreateException, Exception
	{
		if (ValidazioneDati.isFilled(jobs)) {
			log.info("=== BatchManager.addJobs() : caricamento configurazione Jobs");
			for (CodaJMSVO job : jobs)
				addJob(job);
		}

	}

	public void startService() throws Exception {
		log.info("=== BatchManager.startService()");
		initScheduler();
		reload();
		_started.set(true);
		CommonConfiguration.addInterceptor(this, OBSERVED_PROPS);
	}

	private void configureMonitorJob() throws Exception {
		log.info("=== BatchManager.configureMonitorJob()");
		scheduler.unscheduleJob(STALL_MONITOR, null);
		scheduler.deleteJob(STALL_MONITOR, null);
		JobDetail job = new JobDetail(STALL_MONITOR, null, BatchStallControllerJob.class);
		List<CodaJMSVO> confQueues = this.getBatchServizi();
		job.getJobDataMap().put(BatchConstants.QUEUE_PARAMS, confQueues);
		Trigger trigger = new SimpleTrigger(STALL_MONITOR,
                null,
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                REPEAT_INTERVAL);
		trigger.setMisfireInstruction(Trigger.INSTRUCTION_NOOP);

		scheduler.scheduleJob(job, trigger);
	}

	private void configureCleanerJob() throws Exception {
		log.info("=== BatchManager.configureCleanerJob()");
		scheduler.unscheduleJob(CLEANER_MONITOR, null);
		scheduler.deleteJob(CLEANER_MONITOR, null);

		int age = CommonConfiguration.getPropertyAsInteger(Configuration.BATCH_CLEANING_AGE_THRESHOLD, 30);
		boolean delete = Boolean.valueOf(CommonConfiguration.getProperty(Configuration.BATCH_CLEANING_DELETE_OUTPUTS, "TRUE"));
		cleanerParams = new BatchCleanerParamsVO((age != 0), delete, age);

		if (!cleanerParams.isActive())
			return;

		JobDetail job = new JobDetail(CLEANER_MONITOR, null, BatchCleanerControllerJob.class);
		List<String> confQueues = getAmministrazionePolo().getListaCodeBatchOutput();
		job.getJobDataMap().put(BatchConstants.QUEUE_PARAMS, confQueues);
		job.getJobDataMap().put(BatchConstants.BATCH_CLEANER_PARAMS, cleanerParams);
		job.getJobDataMap().put(BatchConstants.BATCH_TMPFILE_DELETION_QUEUE, tmpFilesDeletionQueue);
		/*
		Trigger trigger = new SimpleTrigger(CLEANER_MONITOR,
                null,
                new Date(System.currentTimeMillis() + REPEAT_INTERVAL), // dopo 10 min
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                REPEAT_INTERVAL);
        */
		Trigger trigger = new CronTrigger(CLEANER_MONITOR, null, CLEANER_CRON);
		trigger.setMisfireInstruction(Trigger.INSTRUCTION_NOOP);
		log.debug("prossimo controllo batch cleaner: " + trigger.getFireTimeAfter(DaoManager.now()) );

		scheduler.scheduleJob(job, trigger);
	}

	private void configureIndiceConnectionCheckerJob() throws Exception {
		log.info("=== BatchManager.configureIndiceConnectionCheckerJob()");
		scheduler.unscheduleJob(INDICE_CHECK, null);
		scheduler.deleteJob(INDICE_CHECK, null);

		int timeout = CommonConfiguration.getPropertyAsInteger(Configuration.INDICE_CONNECTION_CHECKER_TIMEOUT, (int) REPEAT_INTERVAL);

		if (timeout < 1)	//check disattivato
			return;

		JobDetail job = new JobDetail(INDICE_CHECK, null, BatchCheckIndiceConnectionJob.class);
		Trigger trigger = new SimpleTrigger(INDICE_CHECK,
                null,
                new Date(System.currentTimeMillis() + timeout),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                timeout);
		trigger.setMisfireInstruction(Trigger.INSTRUCTION_NOOP);

		scheduler.scheduleJob(job, trigger);
	}

	private void configureScheduledJob() throws Exception {
		log.info("=== BatchManager.configureScheduledJob()");
		deleteGroupJobs(SCHEDULED_GROUP);
		Timestamp now = DaoManager.now();

		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_BATCH_PIANIFICABILE, true);
		for (TB_CODICI cod : codici) {
			SchedulableBatchVO sb = new SchedulableBatchVO(cod);
			String jobName = sb.getJobName();
			if (!ValidazioneDati.isFilled(jobName))
				continue;

			JobDetail job = new JobDetail(jobName, SCHEDULED_GROUP, BatchScheduledControllerJob.class);
			//almaviva5_20150330 schedulazione a cron o avvio diretto
			Trigger trigger = null;
			String expr = sb.getActivationExpr();
			switch (sb.getScheduleType()) {
			case TIME:
				Date when = DateUtil.dataOra(expr);
				if (when == null)
					throw new ApplicationException(SbnErrorTypes.AMM_BATCH_SCHEDULAZIONE_ERRATA, sb.getJobName() );
				if (when.before(now))
					continue;
				trigger = new SimpleTrigger(jobName, SCHEDULED_GROUP, when );
				break;

			default:
			case CRON:
				try {
					trigger = new CronTrigger(jobName, SCHEDULED_GROUP, expr );
				} catch (ParseException e) {
					throw new ApplicationException(SbnErrorTypes.AMM_BATCH_SCHEDULAZIONE_ERRATA, sb.getJobName() );
				}
				break;
			}
			trigger.setMisfireInstruction(Trigger.INSTRUCTION_NOOP);
			log.debug(jobName + " prossima prenotazione: " + trigger.getFireTimeAfter(now) );

			scheduler.scheduleJob(job, trigger);
		}

	}

	private void deleteGroupJobs(String group) throws SchedulerException {
		for (String name : scheduler.getJobNames(group) ) {
			scheduler.unscheduleJob(name, group);
			scheduler.deleteJob(name, group);
		}
	}

	public void reload() throws Exception {
		try {
			scheduler.pauseAll();
			List<CodaJMSVO> confQueues = this.getBatchServizi();
			if (ValidazioneDati.isFilled(confQueues) ) {
				this.clearJobs(confQueues);
				this.addJobs(confQueues);
			}
			configureMonitorJob();
			configureCleanerJob();
			configureScheduledJob();
			//almaviva5_20150126
			configureIndiceConnectionCheckerJob();

			boolean delete = Boolean.valueOf(CommonConfiguration.getProperty(Configuration.BATCH_USER_DELETE_OUTPUTS));
			userDeletionDeleteOutputs.set(delete);

		} finally {
			scheduler.resumeAll();
		}

	}

	public void stopService() throws Exception {
		log.info("=== BatchManager.stopService()");
		this.clear();
		_started.set(false);
	}

	private void clear() throws Exception {
		this.clearJobs(this.getBatchServizi());

		scheduler.unscheduleJob(STALL_MONITOR, null);
		scheduler.deleteJob(STALL_MONITOR, null);

		scheduler.unscheduleJob(CLEANER_MONITOR, null);
		scheduler.deleteJob(CLEANER_MONITOR, null);
	}

	private void initScheduler() throws Exception {
		log.info("=== BatchManager.initScheduler() : Look for Quartz Scheduler ...");
		InitialContext iniCtx = JNDIUtil.getContext();
		scheduler = (Scheduler) iniCtx.lookup("Quartz");
	}

	private void clearJobs(List<CodaJMSVO> jobs) throws Exception {
		if (ValidazioneDati.isFilled(jobs)) {
			log.info("=== BatchManager.setScheduler() : pulizia Job");
			for (CodaJMSVO job : jobs) {
				String jobName = job.getId_coda() + "";
				scheduler.unscheduleJob(jobName, null);
				scheduler.deleteJob(jobName, null);
			}
		}
	}

	/**
	 * Crea il job per l'attivita
	 *
	 * @param jobName
	 * @param groupName
	 * @param triggerName
	 * @param cron
	 * @param servizi
	 * @throws Exception
	 */
	private void addJob(CodaJMSVO params) throws Exception {

		log.info("=== BatchManager.addJob(): " + params);
		try {
			String idJob = (params.getId_coda() + "");
			JobDetail job = new JobDetail(idJob, null, BatchQueueControllerJob.class);
			job.getJobDataMap().put(BatchConstants.QUEUE_PARAMS, params);
			Trigger trigger = new CronTrigger(idJob, null, params.getCron_expression() );
			trigger.setMisfireInstruction(Trigger.INSTRUCTION_NOOP);
			scheduler.scheduleJob(job, trigger);
			scheduler.pauseTrigger(idJob, null);
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}

	public Scheduler getScheduler() throws Exception {
		if (scheduler == null || !scheduler.isStarted())
			initScheduler();

		return scheduler;
	}

	public void deleteBatchId(String id) throws Exception {

		if (ValidazioneDati.strIsNull(id) || !ValidazioneDati.strIsNumeric(id))
			return;

		int deleted = getAmministrazionePolo().eliminaRichiesteElaborazioniDifferite(
				new String[] { id }, false );

		deletedCounter.addAndGet(deleted);
		removeRunningJob(id);
	}

	public void addRunningJob(String id, String threadId) throws Exception {
		if (ValidazioneDati.strIsNull(id) || !ValidazioneDati.strIsNumeric(id))
			return;

		if (ValidazioneDati.strIsNull(threadId) || !ValidazioneDati.strIsNumeric(threadId))
			return;

		runningJobs.put(Integer.valueOf(id), Integer.valueOf(threadId));

		addLockFile(id);
	}

	private void addLockFile(String id) {
		try {
			if (lockFile == null) {
				lockFile = new RandomAccessFile(new File(FILE_LOCK), "rwd");
			}

			lockFile.writeBytes(id);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void removeRunningJob(String id) throws Exception {
		if (ValidazioneDati.strIsNull(id) || !ValidazioneDati.strIsNumeric(id))
			return;
		runningJobs.remove(Integer.valueOf(id));
		removeLockFile();
	}

	private void removeLockFile() {
		try {
			try {
				if (ValidazioneDati.isFilled(runningJobs))
					return;

				if (lockFile != null) {
					lockFile.close();
					File f = new File(FILE_LOCK);
					if (f.exists())
						f.delete();
				}
			} catch (IOException e) {
				log.error("", e);
			}
		} finally {
			lockFile = null;
		}
	}

	public Set<Integer> getRunningJobs() throws Exception {
		return runningJobs.keySet();
	}

	public String getCleaningAgeThreshold() throws Exception {
		return String.valueOf(cleanerParams.getDaysInterval() );
	}

	public boolean isCleaningDeleteOutputs() throws Exception {
		return cleanerParams.isDeleteOutputs();
	}

	public int getDeletedCount() throws Exception {
		return deletedCounter.get();
	}

	public void incrementDeletedCount(int deleted) throws Exception {
		deletedCounter.addAndGet(deleted);
	}

	public String dumpRunningJobThread(String id) throws Exception {
		if (ValidazioneDati.strIsNull(id) || !ValidazioneDati.strIsNumeric(id))
			return "";

		Integer tid = runningJobs.get(Integer.valueOf(id));
		if (tid == null)
			return "";

		ThreadInfo tinfo = threadMXBean.getThreadInfo(tid, TRACE_DEPTH);
		StackTraceElement[] trace = tinfo.getStackTrace();
		StringBuilder buf = new StringBuilder();
        for (int s = 0; s < trace.length; s++) {
            buf.append("\t ");
            buf.append(trace[s]);
            buf.append("\n");
        }

        return buf.toString();

	}

	public void checkForInterruption(String id) throws BatchInterruptedException {
		if (ValidazioneDati.strIsNull(id) || !ValidazioneDati.strIsNumeric(id))
			return;
		if (runningJobs.containsKey(Integer.valueOf(id)) )
			return;

		throw new BatchInterruptedException();

	}

	public boolean isUserDeletionDeleteOutputs() throws Exception {
		return userDeletionDeleteOutputs.get();
	}

	public void onConfigPropertyChange(String key) throws Exception {
		configureCleanerJob();
	}

	public void onConfigReload(Set<String> changedProperties) throws Exception {
		//scateno il reload solo se ho cambiato una delle property dei batch
		for (String prop : OBSERVED_PROPS)
			if (changedProperties.contains(prop)) {
				reload();
				break;
			}
	}

	public void markForDeletion(File f) throws Exception {
		//aggiungo il file alla lista dei file temporanei da cancellare
		tmpFilesDeletionQueue.add(f.getAbsolutePath());
		log.debug("=== BatchManager.markForDeletion(): aggiunto file " + f.getName());
	}

	public void forceBatchStart(String id) throws Exception {
		getAmministrazionePolo().forceBatchStart(id);
	}

}
