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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.servizi.batch.BatchConstants;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.util.List;

import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BatchQueueControllerJob extends SerializableVO implements Job {

	private static final long serialVersionUID = 5982743704140730290L;
	private static final Logger log = Logger.getLogger(BatchQueueControllerJob.class);

	private JMSUtil jms;

	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		try {
			jms = new JMSUtil(JNDIUtil.getContext());

			CodaJMSVO params =
				(CodaJMSVO) ctx.getJobDetail().getJobDataMap().get(BatchConstants.QUEUE_PARAMS);
			if (params == null)
				throw new JobExecutionException();

			// 1. controllo presenza batch in exec/held
			if (params.isCodaSincrona() && isBatchRunning(params))
				return;

			// 2. ricerca prenotazione
			Message msg = findBatchPrenotazione(params);
			if (msg == null)
				return;

			// 3. inoltro prenotazione;
			startBatch(params, msg);

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}

	}

	private Message findBatchPrenotazione(CodaJMSVO params) throws Exception {

		String selector = makeSelector(params);
		//log.debug("findBatchPrenotazione() selector: " + selector );

		InitialContext ctx = JNDIUtil.getContext();
		Queue coda = (Queue) ctx.lookup(params.getNome());

		return jms.receiveQueueMessage(coda, selector );
	}

	private String makeSelector(CodaJMSVO params) {
		StringBuilder buf = new StringBuilder();
		buf.append(ConstantsJMS.ID_CODA);
		buf.append("=" + params.getId_coda());
		buf.append(" and ");
		buf.append(ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_SEND + "'");
		String selector = buf.toString();
		return selector;
	}

	private boolean isBatchRunning(CodaJMSVO params) throws Exception {

		StringBuilder selector = new StringBuilder();
		selector.append(ConstantsJMS.ID_CODA);
		selector.append("=" + params.getId_coda());
		selector.append(" and ");
		selector.append("(" + ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_HELD + "'");
		selector.append(" or ");
		selector.append(ConstantsJMS.STATO + "='" + ConstantsJMS.STATO_EXEC + "')");
		//log.debug("isBatchRunning() selector: " + selector.toString() );

		InitialContext ctx = JNDIUtil.getContext();
		Queue coda = (Queue) ctx.lookup(params.getNome());
		List<Message> msgs = jms.browseQueue(coda, selector.toString() );

		if (!ValidazioneDati.isFilled(msgs) )
			return false;

		log.debug("isBatchRunning() running Job: " + msgs.get(0) );
		return true;
	}

	private void startBatch(CodaJMSVO params, Message msg) throws Exception {
		jms.startBatch(params, msg);
		log.info("startBatch() msg: " + msg.toString() );
	}

}
