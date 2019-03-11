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
package it.iccu.sbn.ejb.utils.jms;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.IdListaMetaInfoVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSExceptionListener;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class JMSBlocchiMessagePump {

	private static Logger log = Logger.getLogger(JMSBlocchiMessagePump.class);

	private static final String SEPARATORE = ":::";
	private AtomicBoolean initialized;
	private String queueName;
	private long ttl;
	private QueueConnectionFactory queueFactory;
	private Queue queue;


	private static JMSBlocchiMessagePump instance = null;

	public static synchronized JMSBlocchiMessagePump getInstance()
			throws NamingException {
		if (instance == null)
			instance = new JMSBlocchiMessagePump();
		return instance;
	}

	private JMSBlocchiMessagePump() throws NamingException {
		this.initialized = new AtomicBoolean(false);
		this.queueName = null;
		this.ttl = -1;
		this.queueFactory = null;
		this.queue = null;
	}

	private String getKey(String ticket, String idLista, int numBlocco) {
		if (ValidazioneDati.strIsNull(ticket))
			return null;
		if (ValidazioneDati.strIsNull(idLista))
			return null;
		if (numBlocco < 1)
			return null;

		StringBuilder buf = new StringBuilder();
		buf.append(ticket);
		buf.append(SEPARATORE);
		buf.append(idLista);
		buf.append(SEPARATORE);
		buf.append(String.valueOf(numBlocco));

		return buf.toString();
	}

	private String getMetaInfoKey(String ticket, String idLista) {
		if (ValidazioneDati.strIsNull(ticket))
			return null;
		if (ValidazioneDati.strIsNull(idLista))
			return null;

		StringBuilder buf = new StringBuilder();
		buf.append(ticket);
		buf.append(SEPARATORE);
		buf.append(idLista);
		buf.append(SEPARATORE);
		buf.append("METAINFO");

		return buf.toString();
	}

	private synchronized void init() throws NamingException {

		try {
			this.queueName = CommonConfiguration.getProperty("QUEUE_NAME");
			this.ttl = Long.valueOf(CommonConfiguration.getProperty("MESSAGE_TTL")); // 21600000
		} catch (Exception e) {
			throw new NamingException("Errore sbnweb.conf");
		}

		InitialContext ic = JNDIUtil.getContext();
		this.queueFactory = (QueueConnectionFactory) ic.lookup("ConnectionFactory");
		this.queue = (Queue) ic.lookup(this.queueName);

		this.initialized.set(true);
	}

	public DescrittoreBloccoVO receive(String ticket, String idLista,
			int numBlocco) throws JMSException, NamingException {

		String key = this.getKey(ticket, idLista, numBlocco);
		if (key == null)
			return null;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		MessageConsumer receiver = null;

		ObjectMessage msg;
		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			String selector = ConstantsJMS.ID_BLOCCO + "='" + key + "'";
			receiver = queueSession.createConsumer(queue, selector);

			queueConnection.start();
			msg = (ObjectMessage) receiver.receive(ConstantsJMS.RECEIVE_TIMEOUT);
			if (msg == null)
				return null;
		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;

		} finally {

			closeJMSReceiver(receiver);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}

		return (DescrittoreBloccoVO) msg.getObject();
	}

	public DescrittoreBloccoVO browse(String ticket, String idLista,
			int numBlocco) throws JMSException, NamingException {

		String key = this.getKey(ticket, idLista, numBlocco);
		if (key == null)
			return null;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueBrowser browser = null;

		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			String selector = ConstantsJMS.ID_BLOCCO + " = '" + key + "'";
			browser = queueSession.createBrowser(queue, selector);
		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;
		}

		ObjectMessage msg = null;
		try {
			queueConnection.start();
			Enumeration<Message> e = browser.getEnumeration();
			if (!e.hasMoreElements())
				return null;
			msg = (ObjectMessage)e.nextElement();
			if (msg == null)
				return null;
		} finally {

			closeJMSBrowser(browser);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}

		return (DescrittoreBloccoVO) msg.getObject();
	}

	public void clearIdLista(String ticket, String idLista)
			throws JMSException, NamingException {

		if (ValidazioneDati.strIsNull(idLista))
			return;
		if (ValidazioneDati.strIsNull(ticket))
			return;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		MessageConsumer receiver = null;

		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			StringBuilder selector = new StringBuilder();
			selector.append(ConstantsJMS.ID_BLOCCO).append(" LIKE= '");
			selector.append(ticket);
			selector.append(SEPARATORE);
			selector.append(idLista);
			selector.append(SEPARATORE);
			selector.append("%'");

			receiver = queueSession.createConsumer(queue, selector.toString());

		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;
		}

		Message msg = null;
		try {
			queueConnection.start();
			do {
				//almaviva5_20110908 fix per migrazione a Jboss5 (JBossMessaging)
				msg = receiver.receive(1);
			} while (msg != null);
		} finally {

			closeJMSReceiver(receiver);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}
	}

	public void clearAll(String ticket) throws JMSException, NamingException {

		if (ValidazioneDati.strIsNull(ticket))
			return;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		MessageConsumer receiver = null;

		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			StringBuilder selector = new StringBuilder();
			selector.append(ConstantsJMS.ID_BLOCCO).append(" LIKE '");
			selector.append(ticket);
			selector.append(SEPARATORE);
			selector.append("%'");

			receiver = queueSession.createConsumer(queue, selector.toString());

		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;
		}

		Message msg = null;
		try {
			queueConnection.start();
			do {
				//almaviva5_20110908 fix per migrazione a Jboss5 (JBossMessaging)
				msg = receiver.receive(1);
			} while (msg != null);

		} finally {

			closeJMSReceiver(receiver);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}
	}

	public void pump(String ticket, List<DescrittoreBloccoVO> blocchi) throws NamingException, JMSException {

		if (ValidazioneDati.size(blocchi) < 1)
			return;
		if (ValidazioneDati.strIsNull(ticket))
			return;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		MessageProducer sender = null;
		QueueSession queueSession = null;

		try {
			queueConnection = getConnection();
			queueConnection.start();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			sender = queueSession.createProducer(queue);

			for (DescrittoreBloccoVO blocco : blocchi) {

				String key = this.getKey(ticket, blocco .getIdLista(), blocco.getNumBlocco());
				ObjectMessage msg = queueSession.createObjectMessage();
				msg.setStringProperty(ConstantsJMS.ID_BLOCCO, key);
				//msg.setJMSCorrelationID(key);
				msg.setJMSReplyTo(queue);
				msg.setObject(blocco);

				sender.send(queue, msg, DeliveryMode.NON_PERSISTENT, 4,	this.ttl);
			}

		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;

		} finally {

			closeJMSSender(sender);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}

	}

	private QueueConnection getConnection() throws JMSException {
		QueueConnection c = queueFactory.createQueueConnection();
		c.setExceptionListener(new JMSExceptionListener(c));
		return c;
	}

	public void sendMetaInfo(String ticket, IdListaMetaInfoVO meta)
			throws NamingException, JMSException {

		if (meta == null)
			return;
		if (ValidazioneDati.strIsNull(ticket))
			return;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		MessageProducer sender = null;
		QueueSession queueSession = null;

		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			sender = queueSession.createProducer(queue);

			String metaKey = this.getMetaInfoKey(ticket, meta.getIdLista());
			ObjectMessage msg = queueSession.createObjectMessage();
			//msg.setJMSCorrelationID(metaKey);
			msg.setStringProperty(ConstantsJMS.ID_BLOCCO, metaKey);
			msg.setJMSReplyTo(queue);
			msg.setObject(meta);

			sender.send(queue, msg, DeliveryMode.NON_PERSISTENT, 4, this.ttl);

		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;

		} finally {

			closeJMSSender(sender);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}

	}

	public IdListaMetaInfoVO receiveMetaInfo(String ticket, String idLista)
		throws JMSException, NamingException {

		String metaKey = this.getMetaInfoKey(ticket, idLista);
		if (metaKey == null)
			return null;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		MessageConsumer receiver = null;

		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			String selector = ConstantsJMS.ID_BLOCCO + " = '" + metaKey + "'";
			receiver = queueSession.createConsumer(queue, selector);
		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;
		}

		ObjectMessage msg = null;
		try {
			queueConnection.start();
			msg = (ObjectMessage) receiver.receive(ConstantsJMS.RECEIVE_TIMEOUT);
			if (msg == null)
				return null;
		} finally {

			closeJMSReceiver(receiver);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}

		return (IdListaMetaInfoVO) msg.getObject();
	}

	public IdListaMetaInfoVO browseMetaInfo(String ticket, String idLista)
			throws JMSException, NamingException {

		String metaKey = this.getMetaInfoKey(ticket, idLista);
		if (metaKey == null)
			return null;

		if (!this.initialized.get())
			this.init();

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueBrowser browser;

		try {
			queueConnection = getConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			String selector = ConstantsJMS.ID_BLOCCO + " = '" + metaKey + "'";
			browser = queueSession.createBrowser(queue, selector);
		} catch (JMSException e) {
			this.initialized.set(false);
			throw e;
		}

		ObjectMessage msg = null;
		try {
			queueConnection.start();
			Enumeration<Message> e = browser.getEnumeration();
			if (!e.hasMoreElements())
				return null;
			msg = (ObjectMessage)e.nextElement();
			if (msg == null)
				return null;
		} finally {

			closeJMSBrowser(browser);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}

		return (IdListaMetaInfoVO) msg.getObject();
}

	 /**
     * Chiude la connessione JMS.
     */
    private static final void closeJMSConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (JMSException e) {
            log.warn("errore chiusura JMS connection", e);
        }
    }

    /**
     *  Chiude la sessione JMS.
     */
    private static final void closeJMSSession(Session session) {
        try {
            if (session != null)
                session.close();
        } catch (JMSException e) {
            log.warn("errore chiusura JMS session", e);
        }
    }

    private static final void closeJMSSender(MessageProducer MessageProducer) {
        try {
			if (MessageProducer != null)
            	MessageProducer.close();
        } catch (JMSException e) {
            log.warn("errore chiusura JMS sender", e);
        }
    }

    private static final void closeJMSReceiver(MessageConsumer MessageConsumer) {
        try {
			if (MessageConsumer != null)
            	MessageConsumer.close();
        } catch (JMSException e) {
            log.warn("errore chiusura JMS receiver", e);
        }
    }

    private static final void closeJMSBrowser(QueueBrowser browser) {
        try {
			if (browser != null)
            	browser.close();
        } catch (JMSException e) {
            log.warn("errore chiusura JMS browser", e);
        }
    }

}
