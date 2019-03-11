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
package it.iccu.sbn.ejb.domain.stampe;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public abstract class SBNStampeBase implements ExceptionListener {

	protected static final String TMP_PATH = FileUtil.getTempFilesDir();
	protected static final String JASPER_DIR = "jasperDir";

	protected static Logger log = Logger.getLogger(SBNStampeBase.class);

	private QueueConnection conn;
	private QueueSession session;
	private List<String> downloadFileName = new ArrayList<String>();
	private JMSUtil jms;
	private boolean jmsInitialized = false;

	/**
	 * Chiude la connessione JMS.
	 */
	private static final void closeConnection(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (JMSException e) {
			log.warn("Could not close JMS connection", e);
		}
	}

	/**
	 * Chiude la sessione JMS.
	 */
	private static final void closeSession(Session session) {
		try {
			if (session != null)
				session.close();
		} catch (JMSException e) {
			log.warn("Could not close JMS session", e);
		}
	}

	private  static final void closeQueueSender(QueueSender queueSender) {
		try {
			if (queueSender != null)
				queueSender.close();
		} catch (JMSException e) {
			log.warn("Could not close queue browser", e);
		}
	}

	public SBNStampeBase() {
		super();
	}

	protected final boolean setupJMS() {
		try {
			if (jmsInitialized)
				return true;
			log.debug("Creazione connessione JMS");
			InitialContext ctx = new InitialContext();
			QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("java:comp/env/jms/QCF");
			this.conn = qcf.createQueueConnection();
			this.conn.setExceptionListener(this);

			this.session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			this.jms = new JMSUtil(ctx);

			conn.start();

			log.debug("Connessione JMS creata: " + conn);

			jmsInitialized = true;
			return true;

		} catch (Exception e) {
			log.error(e);
			jmsInitialized = false;
			return false;
		}
	}

	protected final void closeJMS() {
		closeSession(session);
		closeConnection(conn);
	}

	protected final void replyExec(Message message, Queue dest,
			ElaborazioniDifferiteOutputVo output)
			throws JMSException {

		setupJMS();
		QueueSender sender = session.createSender(dest);

		if (message instanceof ObjectMessage) {
			ObjectMessage replyMsg = (ObjectMessage) jms.duplicateMessage(message);
			replyMsg.setStringProperty(ConstantsJMS.STATO, output.getStato());
			replyMsg.setStringProperty(ConstantsJMS.LOG, "true");
			replyMsg.setJMSCorrelationID(message.getJMSMessageID());
			replyMsg.setObject(output);
			replyMsg.setJMSMessageID(message.getJMSMessageID());
			sender.send(replyMsg);
			closeQueueSender(sender);

		} else if (message instanceof TextMessage) {
			TextMessage replyMsg = (TextMessage) jms.duplicateMessage(message);
			replyMsg.setStringProperty("STATO", output.getStato());
			replyMsg.setStringProperty("DATA_ORA_ESECUZIONE_PROGRAMMATA", "ZERO");
			sender.send(replyMsg);
			closeQueueSender(sender);
		}

	}

	protected final void reply(Message message, Queue dest,
			ElaborazioniDifferiteOutputVo output)
			throws JMSException {

		setupJMS();
		QueueSender sender = session.createSender(dest);
		message.clearBody();

		if (message instanceof ObjectMessage) {

			ObjectMessage replyMsg = (ObjectMessage) jms.duplicateMessage(message);

			replyMsg.setStringProperty(ConstantsJMS.STATO, output.getStato());
			replyMsg.setStringProperty(ConstantsJMS.DATA_ORA_ESECUZIONE_PROGRAMMATA, "cronExpr");
			replyMsg.setStringProperty(ConstantsJMS.DATA_ELABORAZIONE, output.getDataDiElaborazione());
			replyMsg.setObject(output);
			sender.send(replyMsg);

		} else if (message instanceof TextMessage) {
			TextMessage replyMsg = (TextMessage) jms.duplicateMessage(message);
			replyMsg.setStringProperty("STATO", output.getStato());
			replyMsg.setJMSCorrelationID(message.getJMSMessageID());
			replyMsg.setJMSMessageID(message.getJMSMessageID());
			sender.send(replyMsg);
		}
		log.info("messaggio inoltrato in differita: " + message);
		closeQueueSender(sender);
	}

	public void scriviFile(String user, String formatoStampa,
			byte[] streamByte, String pathDownload, String filename)
			throws Exception {
		String dirSave = pathDownload;
		File newFile = null;
		try {
			newFile = creaFile(dirSave, filename);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
		if (newFile.exists())
			newFile.delete();

		try {
			newFile.createNewFile();
		} catch (IOException exError) {
			exError.printStackTrace();
		}
		downloadFileName.add(filename);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile, false));
		bos.write(streamByte, 0, streamByte.length);
		bos.flush();
		bos.close();
		log.info("Terminato lo storage del file indicato nella directory indicata: " + dirSave);
	}

	public void scriviFile(String user, String formatoStampa,
			InputStream streamByte, String pathDownload, String filename)
			throws Exception {
		String dirSave = pathDownload;
		File newFile = null;
		try {
			newFile = creaFile(dirSave, filename);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
		if (newFile.exists())
			newFile.delete();

		try {
			newFile.createNewFile();
		} catch (IOException exError) {
			exError.printStackTrace();
		}
		downloadFileName.add(filename);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile, false));
		SbnStampe.transferData(streamByte, bos);//bos.write(streamByte, 0, streamByte.length);
		streamByte.close();
		bos.flush();
		bos.close();
		log.info("Terminato lo storage del file indicato nella directory indicata: " + dirSave);
	}

	private File creaFile(String dirSave, String filename) throws IOException {
		File newFile = null;
		try {
			newFile = new File(dirSave, filename);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
		if (newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		return newFile;
	}

	public List<String> getDownloadFileName() {
		return downloadFileName;
	}

	public void onException(JMSException jmsEx) {
		log.error("Errore connessione JMS: " + conn);
		log.error(jmsEx);
		try {
			jmsInitialized = false;
			closeJMS();
			while (!jmsInitialized) {
				Thread.sleep(10000);
				jmsInitialized = setupJMS();
			}
		} catch (InterruptedException e) {
			log.error(e);
			return;
		} catch (Exception e) {
			log.error(e);
		}
	}

	public abstract Object elabora(StampaVo stampaVO, BatchLogWriter log) throws Exception;

	protected InputStream effettuaStampa(String pathJrxml, String tipoStampa, Object output) throws Exception {
		return effettuaStampa(pathJrxml, tipoStampa, output, null);
	}

	protected InputStream effettuaStampa(String pathJrxml, String tipoStampa, Object output, Map<String, Object> params) throws Exception {
		InputStream streamRichiestaStampa = null;

		SbnStampe sbn = new SbnStampe(pathJrxml);
		sbn.setFormato(TipoStampa.toTipoStampa(tipoStampa));
		Map<String, Object> parametri = new HashMap<String, Object>();
		// se il Design richiede dei subreport passo
		// a jasper il parametro jasperDir (path completo della directory
		// dove si trovano i jasper files, senza lo slash finale)
		parametri.put(JASPER_DIR, TMP_PATH + File.separator);

		//almaviva5_20121219
		if (ValidazioneDati.isFilled(params))
			parametri.putAll(params);

		streamRichiestaStampa = sbn.stampa(output, parametri);

		return streamRichiestaStampa;
	}

}
