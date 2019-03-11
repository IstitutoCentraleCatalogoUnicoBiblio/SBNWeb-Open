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
package it.iccu.sbn.servizi.batch.job;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.BatchCleanerParamsVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ProfilerManager;
import it.iccu.sbn.servizi.ProfilerManagerMBean;
import it.iccu.sbn.servizi.batch.BatchConstants;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BatchCleanerControllerJob extends SerializableVO implements Job {

	private static final long serialVersionUID = -6060676693481577290L;

	private static final Logger log = Logger.getLogger(BatchCleanerControllerJob.class);

	private JMSUtil jms;
	private InitialContext jndiCtx;

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		try {
			jndiCtx = JNDIUtil.getContext();
			jms = new JMSUtil(jndiCtx);

			JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();

			//almaviva5_20100705 check coda cancellazione file temporanei
			checkDeletionMarkRefQueue((List<String>)jobDataMap.get(BatchConstants.BATCH_TMPFILE_DELETION_QUEUE));

			BatchCleanerParamsVO params = (BatchCleanerParamsVO) jobDataMap.get(BatchConstants.BATCH_CLEANER_PARAMS);
			if (params == null)
				throw new JobExecutionException();

			List<String> confQueues = (List<String>) jobDataMap.get(BatchConstants.QUEUE_PARAMS);
			if (confQueues == null)
				throw new JobExecutionException();

			log.debug("--- Inizio pulizia code elaborazioni differite ---");
			log.debug(params);

			int deleted = 0;
			for (String qname : confQueues)
				deleted = searchForOldestJobs(qname, params);

			if (deleted != 0)
				BatchManager.getBatchManagerInstance().incrementDeletedCount(deleted);

			log.debug("Record elaborazioni differite eliminati: " + deleted);
			log.debug("--- Fine pulizia code elaborazioni differite ---");

			//almaviva5_20150330 pulizia tabella PIED
			deleteExpiredScheduledJobs();


		} catch (Exception e) {
			log.error("", e);
			throw new JobExecutionException(e);
		}

	}

	private void checkDeletionMarkRefQueue(List<String> list) throws Exception {
		if (!ValidazioneDati.isFilled(list) )
			return;

		//solo se non ci sono batch attivi
		if (ValidazioneDati.isFilled(BatchManager.getBatchManagerInstance().getRunningJobs()))
			return;

		//ciclo per la cancellazione periodica dei file temporanei registrati
		synchronized (list) {
			Iterator<String> i = list.iterator();
			while (i.hasNext()) {
				File f = new File(i.next());
				if (f.delete()) {
					log.debug("Cancellato file temporaneo: " + f.getName());
					i.remove();
				}
			}
		}
	}

	private int searchForOldestJobs(String qName, BatchCleanerParamsVO params) throws Exception {

		int count = 0;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DAY_OF_YEAR, negation(params.getDaysInterval()) );
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		log.debug("Ricerca elaborazioni differite con data fine elaborazione precedente a: " + calendar.getTime());

		StringBuilder selector = new StringBuilder();
		selector.append(ConstantsJMS.JMSTimestamp + "<=" + calendar.getTime().getTime());
		selector.append(" AND (" + ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_OK + "'");
		selector.append(" OR " + ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_ERROR + "'");
		selector.append(")");
		//log.debug("searchForStalledJobs() selector: " + selector.toString() );

		InitialContext ctx = JNDIUtil.getContext();
		Queue coda = (Queue) ctx.lookup(qName);
		List<Message> messages = jms.receiveQueue(coda, selector.toString() );

		for (Message message : messages) {
			if (!(message instanceof ObjectMessage))
				continue;

			count++;
			ElaborazioniDifferiteOutputVo output = (ElaborazioniDifferiteOutputVo) ((ObjectMessage)message).getObject();
			log.debug("Cancellazione elaborazione differita: " + output);
			if (!params.isDeleteOutputs())
				continue;

			List<DownloadVO> downloadList = output.getDownloadList();
			if (!ValidazioneDati.isFilled(downloadList))
				continue;

			for (DownloadVO file : downloadList)
				deleteBatchDownloadFile(file);
		}
		return count;

	}

	public static final void deleteBatchDownloadFile(DownloadVO file) {

		String link = file.getLinkToDownload();
		if (!ValidazioneDati.isFilled(link))
			return;

		String pathname =
			link.substring(1).indexOf(File.separator) > -1 ? link : StampeUtil.getBatchFilesPath() + File.separator + link;

		File f = new File(pathname);
		if (!f.exists()) {
			log.warn("il file specificato non esiste: " + pathname);
			return;
		}

		if (!f.delete() )
			log.warn("Impossibile cancellare file: " + link);

	}

	private final int negation(int value) {
		return (value < 0 ? value : -value);
	}

	private void deleteExpiredScheduledJobs() throws Exception {
		boolean deleted = false;
		AmministrazioneGestioneCodici ejb = DomainEJBFactory.getInstance().getCodiciBMT();
		Timestamp now = DaoManager.now();

		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_BATCH_PIANIFICABILE, true);
		for (TB_CODICI cod : codici) {
			SchedulableBatchVO sb = new SchedulableBatchVO(cod);
			String jobName = sb.getJobName();
			if (!ValidazioneDati.isFilled(jobName))
				continue;

			//almaviva5_20150330 schedulazione a cron o avvio diretto
			switch (sb.getScheduleType()) {
			default:
			case CRON:
				continue;

			case TIME:
				String expr = sb.getActivationExpr();
				Date when = DateUtil.dataOra(expr);
				if (when.after(now))
					continue;	//ancora valido

				ejb.deleteTabellaCodici(jobName, CodiciType.CODICE_BATCH_PIANIFICABILE);
				log.debug("Cancellata schedulazione non valida: " + cod + "; " + expr);
				deleted = true;
			}
		}

		if (deleted) {
			ProfilerManagerMBean pm = ProfilerManager.getProfilerManagerInstance();
			pm.reloadCodici();
		}
	}

}
