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
package it.iccu.sbn.ejb.services.batch;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_batch_serviziDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_batch_servizi;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManagerMBean;
import it.iccu.sbn.servizi.batch.SchedulableBatchExecutor;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.batch.BatchUtil;
import it.iccu.sbn.util.jasper.BatchCollectionSerializer;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.util.logging.SbnBatchDumpStyle;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.quartz.Scheduler;

public class BatchProcessorProxyBean extends TicketChecker implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 2458705022137111021L;
	private static Logger log = Logger.getLogger(BatchProcessorProxyBean.class);

	private MessageDrivenContext ctx;
	private JMSUtil jms;
	private InitialContext jndiCtx;

	private BatchManagerMBean manager;


	public void ejbCreate() throws EJBException {
		try {
			jndiCtx = JNDIUtil.getContext();
			jms = new JMSUtil(jndiCtx);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return;
	}

	public void ejbRemove() throws EJBException {
		return;
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) throws EJBException {
		this.ctx = mdc;
		log.debug(ctx);
	}

	public void onMessage(Message message) {

		Serializable obj = null;

		try {
			message.acknowledge();

			if (!(message instanceof ObjectMessage)) {
				log.error("Messaggio non gestito");
				return;
			}

			ObjectMessage objMsg = (ObjectMessage) message;
			obj = objMsg.getObject();
			if (!(obj instanceof ParametriRichiestaElaborazioneDifferitaVO)) {
				log.error("Messaggio non gestito");
				return;
			}

		} catch (JMSException ex) {
			log.error("", ex);
			return;
		}

		UserTransaction tx = ctx.getUserTransaction();
		ParametriRichiestaElaborazioneDifferitaVO params = (ParametriRichiestaElaborazioneDifferitaVO) obj;
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		BatchLogWriter batchLog = new BatchLogWriter(params);
		BatchExecutor executor = null;

		batchLog.logWriteLine("--- Inizio DUMP Parametri di attivazione ---");
		batchLog.logWriteLine(ReflectionToStringBuilder.toString(params, new SbnBatchDumpStyle()) );
		batchLog.logWriteLine("--- Fine DUMP Parametri di attivazione ---");

		// nel caso l'output risulti invalido
		// devo garantire l'uscita del file di log
		output.getDownloadList().add(batchLog.getFilename());

		try {
			log.info("Messaggio ricevuto per batch: " + params);
			message = sendExecNotification(message, output, batchLog);

			//almaviva5_20140905
			PoloVO polo = DomainEJBFactory.getInstance().getPolo().getInfoPolo();
			batchLog.logWriteLine("versione sbnweb : " + polo.getSbnWebBuildTime() );
			batchLog.logWriteLine("versione sbnmarc: " + polo.getSbnMarcBuildTime() );

			// carico la configurazione dal DB per questo batch
			Tbf_batch_servizi config = readBatchConfig(tx, params);
			// creo l'istanza dell'oggetto delegato all'esecuzione effettiva del batch
			executor = BatchUtil.createExecutorInstance(config);

			//almaviva5_20130321 evolutive LO1
			setStart(tx, executor, params);


			if (!executor.validateInput(params, batchLog)) {
				sendErrorNotification(message, params, output, batchLog);
				return;
			}

			//almaviva5_20110530 #4480 assegnazione ticket valido
			assegnaNuovoTicket(params, batchLog);

			ElaborazioniDifferiteOutputVo bout = executor.execute(params.getFirmaBatch(), params, batchLog, tx);

			if (bout == null) {
				sendErrorNotification(message, params, output, batchLog);
				return;
			} else
				output = bout;

			DownloadVO logFilename = batchLog.getFilename();
			if (batchLog.exists())
				bout.getDownloadList().add(logFilename);

			bout.validate();

			if (ValidazioneDati.equals(bout.getStato(), ConstantsJMS.STATO_OK))
				sendOkNotification(message, params, bout, batchLog);
			else
				sendErrorNotification(message, params, bout, batchLog);


		} catch (Exception e) {
			log.error("", e);
			batchLog.logWriteException(e);
			try {
				sendErrorNotification(message, params, output, batchLog);
			} catch (Exception e1) {
				log.error("", e1);
			}
		} finally {
			try {
				//almaviva5_20130321 evolutive LO1
				setEnd(tx, executor, params, output);
			} catch (Exception e) {
				log.error("", e);
			}

			//cancello i file temporanei associati al batch
			BatchCollectionSerializer.deleteBatchTempFiles(params.getIdBatch());
			try {
				getScheduler().resumeTrigger(params.getIdCoda() + "", null);
			} catch (Exception e1)  {
				log.error("", e1);
			}
			if (batchLog != null)
				batchLog.closeLogFile();
		}

	}

	private void setEnd(UserTransaction tx, BatchExecutor executor,
			ParametriRichiestaElaborazioneDifferitaVO params,
			ElaborazioniDifferiteOutputVo output) throws Exception {

		if (executor instanceof SchedulableBatchExecutor) {
			boolean success = false;
			DaoManager.begin(tx);
			try {
				SchedulableBatchExecutor sbe = (SchedulableBatchExecutor) executor;
				sbe.setEnd(params, output);
				success = true;
			} finally {
				DaoManager.endTransaction(tx, success);
			}
		}
	}

	private void setStart(UserTransaction tx, BatchExecutor executor,
			ParametriRichiestaElaborazioneDifferitaVO params) throws Exception {

		if (executor instanceof SchedulableBatchExecutor) {
			boolean success = false;
			DaoManager.begin(tx);
			try {
				SchedulableBatchExecutor sbe = (SchedulableBatchExecutor) executor;
				sbe.setStart(params);
				success = true;
			} finally {
				DaoManager.endTransaction(tx, success);
			}
		}
	}

	private void assegnaNuovoTicket(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter batchLog) throws Exception {
		//si inserisce un nuovo ticket fittizio per gli ejb che lo richiedono
		String ticket = generateUniqueTicket(params.getCodPolo(), params.getCodBib(), params.getUser());
		Class<ParametriRichiestaElaborazioneDifferitaVO> clazz = ParametriRichiestaElaborazioneDifferitaVO.class;
		Field f = clazz.getDeclaredField("ticket");
		f.setAccessible(true);
		f.set(params, ticket);

		batchLog.logWriteLine("Assegnato nuovo ticket per elaborazione differita: " + ticket);

		addTicket(ticket, InetAddress.getLocalHost());
	}

	private Scheduler getScheduler() {
		try {
			BatchManagerMBean bm = getBatchManager();
			Scheduler scheduler = bm.getScheduler();
			return scheduler;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	private BatchManagerMBean getBatchManager() throws MalformedObjectNameException {
		if (manager != null)
			return manager;

		MBeanServer mbserver = MBeanServerLocator.locateJBoss();
		ObjectName name = new ObjectName("sbn:service=BatchManager");
		manager = (BatchManagerMBean)MBeanProxyExt.create(BatchManagerMBean.class, name, mbserver);
		return manager;
	}

	private Tbf_batch_servizi readBatchConfig(
			UserTransaction tx, ParametriRichiestaElaborazioneDifferitaVO params) throws Exception {

		boolean success = false;
		DaoManager.begin(tx);
		try {
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			Tbf_batch_servizi batch = dao.selectBatchByCodAttivita(params.getCodAttivita());
			success = true;
			return batch;

		} finally {
			DaoManager.endTransaction(tx, success);
		}
	}

	private Message sendExecNotification(Message message,
			ParametriRichiestaElaborazioneDifferitaVO params,
			BatchLogWriter batchLog) throws Exception {

		try {
			getScheduler().pauseTrigger(params.getIdCoda() + "", null);
			Queue queue = (Queue) jndiCtx.lookup(params.getNomeCodaJMS() );
			Map<String, String> otherParams = new HashMap<String, String>();
			String now = DateUtil.now();
			otherParams.put(ConstantsJMS.DATA_ELABORAZIONE, now );
			otherParams.put(ConstantsJMS.STATO, ConstantsJMS.STATO_EXEC);
			batchLog.logWriteLine(params + " posto in EXEC in data " + now);
			getBatchManager().addRunningJob(params.getIdBatch(), String.valueOf(Thread.currentThread().getId()) );

			ObjectMessage newMsg = (ObjectMessage) jms.duplicateMessage(message);
			jms.sendQueue(queue, newMsg, params, otherParams);
			return newMsg;

		} finally {
			getScheduler().resumeTrigger(params.getIdCoda() + "", null);
		}
	}

	private void sendOkNotification(Message message, ParametriRichiestaElaborazioneDifferitaVO params,
			ElaborazioniDifferiteOutputVo output, BatchLogWriter batchLog) throws Exception {

		try {
			getScheduler().pauseTrigger(params.getIdCoda() + "", null);

			Queue queue = (Queue) jndiCtx.lookup(params.getNomeCodaJMSOutput());
			Map<String, String> otherParams = new HashMap<String, String>();
			otherParams.put(ConstantsJMS.STATO, ConstantsJMS.STATO_OK);
			String now = DateUtil.now();
			//almaviva5_20130321 evolutive LO1
			output.setEndTime(DaoManager.now());
			otherParams.put(ConstantsJMS.DATA_FINE_ELABORAZIONE, now);
			ObjectMessage newMsg = (ObjectMessage) jms.duplicateMessage(message);
			jms.sendQueue(queue, newMsg, output, otherParams);
			batchLog.logWriteLine(params + " posto in OK in data " + now);
			removeExecNotification(params);

		} finally {
			getScheduler().resumeTrigger(params.getIdCoda() + "", null);
		}

	}

	private void sendErrorNotification(Message message, ParametriRichiestaElaborazioneDifferitaVO params,
			ElaborazioniDifferiteOutputVo output, BatchLogWriter batchLog) throws Exception {

		try {
			getScheduler().pauseTrigger(params.getIdCoda() + "", null);
			Queue queue = (Queue) jndiCtx.lookup(params.getNomeCodaJMSOutput());
			Map<String, String> otherParams = new HashMap<String, String>();
			otherParams.put(ConstantsJMS.STATO, ConstantsJMS.STATO_ERROR);
			String now = DateUtil.now();
			//almaviva5_20130321 evolutive LO1
			output.setEndTime(DaoManager.now());
			otherParams.put(ConstantsJMS.DATA_FINE_ELABORAZIONE, now);
			ObjectMessage newMsg = (ObjectMessage) jms.duplicateMessage(message);
			jms.sendQueue(queue, newMsg, output, otherParams);
			batchLog.logWriteLine(params + " posto in ERROR in data " + now);
			removeExecNotification(params);

		} finally {
			getScheduler().resumeTrigger(params.getIdCoda() + "", null);
		}
	}

	private void removeExecNotification(
			ParametriRichiestaElaborazioneDifferitaVO params) throws Exception {
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
