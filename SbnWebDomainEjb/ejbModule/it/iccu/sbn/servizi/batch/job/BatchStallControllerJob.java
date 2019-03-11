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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.servizi.batch.BatchConstants;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManagerMBean;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

public class BatchStallControllerJob extends SerializableVO implements Job {

	private static final long serialVersionUID = -8360842356328623841L;
	private static final Logger log = Logger.getLogger(BatchStallControllerJob.class);

	private JMSUtil jms;
	private BatchManagerMBean manager;
	private Scheduler scheduler;
	private InitialContext jndiCtx;

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		try {
			jndiCtx = JNDIUtil.getContext();
			jms = new JMSUtil(jndiCtx);

			scheduler = ctx.getScheduler();
			List<CodaJMSVO> confQueues = (List<CodaJMSVO>) ctx.getJobDetail().getJobDataMap().get(BatchConstants.QUEUE_PARAMS);
			if (confQueues == null)
				throw new JobExecutionException();

			for (CodaJMSVO coda : confQueues)
				searchForStalledJobs(coda);

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}

	}

	private BatchManagerMBean getBatchManager() throws MalformedObjectNameException {
		if (manager != null)
			return manager;

		MBeanServer mbserver = MBeanServerLocator.locateJBoss();
		ObjectName name = new ObjectName("sbn:service=BatchManager");
		manager = (BatchManagerMBean)MBeanProxyExt.create(BatchManagerMBean.class, name, mbserver);
		return manager;
	}

	private void searchForStalledJobs(CodaJMSVO params) throws Exception {

		StringBuilder selector = new StringBuilder();
		selector.append(ConstantsJMS.ID_CODA);
		selector.append("=" + params.getId_coda());
		selector.append(" and ");
		selector.append(ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_EXEC + "'");
		//log.debug("searchForStalledJobs() selector: " + selector.toString() );

		InitialContext ctx = JNDIUtil.getContext();
		Queue coda = (Queue) ctx.lookup(params.getNome());
		List<Message> msgs = jms.browseQueue(coda, selector.toString() );

		if (!ValidazioneDati.isFilled(msgs) )
			return;

		Set<Integer> runningJobs = getBatchManager().getRunningJobs();
		synchronized (runningJobs) {
			Iterator<Message> i = msgs.iterator();
			while (i.hasNext() ) {
				ObjectMessage msg = (ObjectMessage) i.next();
				Object obj = msg.getObject();
				if (!(obj instanceof ParametriRichiestaElaborazioneDifferitaVO) )
					continue;

				ParametriRichiestaElaborazioneDifferitaVO batch = (ParametriRichiestaElaborazioneDifferitaVO) obj;
				if (runningJobs.contains(Integer.valueOf(batch.getIdBatch())))
					continue;

				ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(batch);
				BatchLogWriter batchLog = new BatchLogWriter(batch);
				output.getDownloadList().add(batchLog.getFilename());
				output.validate();

				batchLog.logWriteLine("Il sistema ha individuato una condizione di stallo relativa a questo processo.");
				sendErrorNotification(msg, batch, output, batchLog);
				log.debug("searchForStalledJobs(): posto in stato ERROR batch id " + batch.getIdBatch() );
			}
		}
	}

	private void sendErrorNotification(Message message, ParametriRichiestaElaborazioneDifferitaVO params,
			ElaborazioniDifferiteOutputVo output, BatchLogWriter batchLog) throws Exception {

		try {
			scheduler.pauseTrigger(params.getIdCoda() + "", null);
			Queue queue = (Queue) jndiCtx.lookup(params.getNomeCodaJMSOutput());
			Map<String, String> otherParams = new HashMap<String, String>();
			otherParams.put(ConstantsJMS.STATO, ConstantsJMS.STATO_ERROR);
			String now = DateUtil.now();
			otherParams.put(ConstantsJMS.DATA_FINE_ELABORAZIONE, now);
			ObjectMessage newMsg = (ObjectMessage) jms.duplicateMessage(message);
			jms.sendQueue(queue, newMsg, output, otherParams);
			batchLog.logWriteLine(params + " posto in ERROR in data " + now);
			removeExecNotification(params);

		} finally {
			scheduler.resumeTrigger(params.getIdCoda() + "", null);
		}
	}

	private void removeExecNotification(ParametriRichiestaElaborazioneDifferitaVO params) throws Exception {
		StringBuilder selector = new StringBuilder();
		selector.append(ConstantsJMS.ID_BATCH);

		String idBatch = params.getIdBatch();
		selector.append("=" + idBatch );
		selector.append(" and ");
		selector.append(ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_EXEC + "'");

		Queue queue = (Queue) jndiCtx.lookup(params.getNomeCodaJMS() );
		jms.receiveQueueMessage(queue, selector.toString() );
		getBatchManager().removeRunningJob(idBatch);
	}

}
