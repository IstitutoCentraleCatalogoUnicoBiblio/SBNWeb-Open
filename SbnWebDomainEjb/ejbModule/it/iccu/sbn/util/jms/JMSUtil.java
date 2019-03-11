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
package it.iccu.sbn.util.jms;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.util.ProgressivoDAO;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

@SuppressWarnings("unchecked")
public class JMSUtil {

	private static Logger log = Logger.getLogger(JMSUtil.class);
	private static final int MSG_PRIORITY = 1;
	private static final long MSG_TTL = 0L;

	private transient QueueConnectionFactory queueConnectionFactory;
	private ProgressivoDAO dao;
	private Context ctx;

	public JMSUtil(Context jndiContext) throws NamingException {
		super();
		this.init(jndiContext);
	}


	private void init(Context jndiContext) throws NamingException {
		ctx = jndiContext;
		queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
		dao = new ProgressivoDAO();
	}

	protected QueueConnection getConnection() throws JMSException {
		QueueConnection connection = queueConnectionFactory.createQueueConnection();
		connection.setExceptionListener(new JMSExceptionListener(connection));
		return connection;
	}

	public Queue getQueue(String jndiName) throws NamingException {
		Queue queue = (Queue) ctx.lookup(jndiName);
		return queue;
	}

	/**
	 * Returns an <code>Enumeration</code> that is used to scan the queue's
	 * messages.
	 *
	 * @param queue
	 *            the queue to browse
	 * @return Enumeration containing the messages on the queue.
	 * @throws JMSException
	 *             if an error occurs while establishing the connection with the
	 *             JMS provider.
	 *
	 * @ejb.interface-method
	 */
	public List<Message> browseQueue(Queue queue) throws JMSException {
		return browseQueue(queue, null);
	}

	/**
	 * Returns an <code>Enumeration</code> that is used to scan the queue's
	 * messages.
	 *
	 * @param queue
	 *            the queue to browse
	 * @param messageSelector
	 *            the message selector to use
	 * @return Enumeration containing the messages on the queue.
	 * @throws JMSException
	 *             if an error occurs while establishing the connection with the
	 *             JMS provider.
	 *
	 * @ejb.interface-method
	 */
	public List<Message> browseQueue(Queue queue, String messageSelector)
			throws JMSException {
		List<Message> output = ValidazioneDati.emptyList();

		QueueConnection connection = null;
		QueueSession session = null;
		QueueBrowser browser = null;
		Enumeration<Message> e = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			if (messageSelector != null)
				browser = session.createBrowser(queue, messageSelector);
			else
				browser = session.createBrowser(queue);

			e = browser.getEnumeration();
			while (e.hasMoreElements())
				output.add(e.nextElement());

		} finally {
			closeQueueBrowser(browser);
			closeSession(session);
			closeConnection(connection);
		}

		return output;
	}

	public void sendQueue(Queue queue, String message, Map<?, ?> property)
			throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageProducer sender = null;
		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);

			TextMessage tm = session.createTextMessage(message);
			tm.setJMSReplyTo(queue);
			if (property != null) {
				Iterator<?> i = property.keySet().iterator();
				while (i.hasNext()) {
					String key = (String) i.next();
					String value = (String) property.get(key);
					tm.setStringProperty(key, value);
				}
			}

			sender = session.createSender(queue);
			sender.send(tm, DeliveryMode.PERSISTENT, MSG_PRIORITY, MSG_TTL);
		} finally {
			closeMessageProducer(sender);
			closeSession(session);
			closeConnection(connection);
		}
	}

	public String sendQueue(Queue queue, Serializable message, Map<?, ?> property)
			throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageProducer sender = null;
		String idMessaggio = "";

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);

			ObjectMessage msg = session.createObjectMessage(message);
			msg.setJMSReplyTo(queue);

			if (property != null) {
				Iterator<?> i = property.keySet().iterator();
				while (i.hasNext()) {
					String key = (String) i.next();
					Object value = property.get(key);
					msg.setObjectProperty(key, value);
				}
			}

			sender = session.createSender(queue);
			sender.send(msg, DeliveryMode.PERSISTENT, MSG_PRIORITY, MSG_TTL);
			idMessaggio = msg.getJMSMessageID();
			if (idMessaggio != null) {
				int indiceChar = idMessaggio.indexOf(":");
				idMessaggio = idMessaggio.substring(indiceChar + 1, idMessaggio
						.length());
			}
			log.info("messaggio inoltrato in differita: " + msg);
		} catch (JMSException e) {
			idMessaggio = "0";
			return idMessaggio;
			// inoltroOk = false;
			// return inoltroOk;
		} finally {
			closeMessageProducer(sender);
			closeSession(session);
			closeConnection(connection);
		}
		return idMessaggio;
	}

	public void sendQueue(Queue queue, ObjectMessage msg, Serializable msgObject, Map<String, String> properties) throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageProducer sender = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);

			msg.setObject(msgObject);
			msg.setJMSReplyTo(queue);

			if (properties != null)
				for (String key : properties.keySet() )
					if (ConstantsJMS.contains(key))
						msg.setObjectProperty(key, properties.get(key) );

			sender = session.createSender(queue);
			sender.send(msg, DeliveryMode.PERSISTENT, MSG_PRIORITY, MSG_TTL);
			log.info("messaggio inoltrato in differita: " + msg);

		} finally {
			closeMessageProducer(sender);
			closeSession(session);
			closeConnection(connection);
		}
	}

	public void sendQueue(Queue queue, Message message) throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageProducer sender = null;
		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			sender = session.createSender(queue);
			sender
					.send(message, DeliveryMode.PERSISTENT, MSG_PRIORITY,
							MSG_TTL);
		} finally {
			closeMessageProducer(sender);
			closeSession(session);
			closeConnection(connection);
		}
	}

	/**
	 * Returns a messages using the specified start date and end date.
	 *
	 * @param queue
	 *            the queue to browse
	 * @param startDate
	 *            the beginning date. If null no constraint is set on the start
	 *            date
	 * @param endDate
	 *            the end date. If null, no constraint is set on the end date
	 * @return an <code>Enumaration</code> of messages posted between
	 *         <tt>startDate</tt> and <tt>endDate</tt>
	 * @throws JMSException
	 *             if an error occurs while establishing the connection with the
	 *             JMS provider.
	 *
	 * @ejb.interface-method
	 */
	public List<Message> browseQueue(Queue queue, Date startDate, Date endDate)
			throws JMSException {
		// Let's build the selector
		String selector = null;
		if (startDate != null && endDate != null) {
			selector = "JMSTimestamp >= " + startDate.getTime()
					+ " AND JMSTimestamp <=" + endDate.getTime();
		} else if (startDate != null) {
			selector = "JMSTimestamp >= " + startDate.getTime();
		} else if (endDate != null) {
			selector = "JMSTimestamp <=" + endDate.getTime();
		} else { // no date is set
			selector = null;
		}

		return browseQueue(queue, selector);
	}

	public List<Message> receiveQueue(Queue queue, String messageSelector)
			throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageConsumer receiver = null;
		Message received = null;
		List<Message> response = new ArrayList<Message>();

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			if (messageSelector != null) {
				receiver = session.createReceiver(queue, messageSelector);
			} else {
				receiver = session.createReceiver(queue);
			}

			connection.start();
			while ((received = receiver.receive(ConstantsJMS.RECEIVE_TIMEOUT)) != null) {
				response.add(received);
			}

		} finally {
			closeMessageConsumer(receiver);
			closeSession(session);
			closeConnection(connection);
		}

		return response;
	}

	public void discardQueue(Queue queue, String messageSelector) throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageConsumer receiver = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			if (messageSelector != null)
				receiver = session.createReceiver(queue, messageSelector);
			else
				receiver = session.createReceiver(queue);

			connection.start();
			while ( receiver.receive(1) != null)
				continue;

		} finally {
			closeMessageConsumer(receiver);
			closeSession(session);
			closeConnection(connection);
		}
	}

	public Message receiveQueueMessage(Queue queue, String messageSelector)
			throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageConsumer receiver = null;
		Message received = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			if (messageSelector != null) {
				receiver = session.createReceiver(queue, messageSelector);
			} else {
				receiver = session.createReceiver(queue);
			}
			connection.start();
			received = receiver.receive(ConstantsJMS.RECEIVE_TIMEOUT);

		} finally {
			closeMessageConsumer(receiver);
			closeSession(session);
			closeConnection(connection);
		}

		return received;
	}

	public Message waitAndReceive(Queue queue, String messageSelector) throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageConsumer receiver = null;
		Message received = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			if (messageSelector != null) {
				receiver = session.createReceiver(queue, messageSelector);
			} else {
				receiver = session.createReceiver(queue);
			}
			connection.start();
			received = receiver.receive();

		} finally {
			closeMessageConsumer(receiver);
			closeSession(session);
			closeConnection(connection);
		}

		return received;
	}

	public Message receiveQueueMessage(Queue queue, String messageSelector,
			int timeout) throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageConsumer receiver = null;
		Message received = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			if (messageSelector != null) {
				receiver = session.createReceiver(queue, messageSelector);
			} else {
				receiver = session.createReceiver(queue);
			}

			connection.start();
			received = receiver.receive(timeout);

		} finally {
			closeMessageConsumer(receiver);
			closeSession(session);
			closeConnection(connection);
		}

		return received;
	}

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

	private  static final void closeMessageProducer(MessageProducer MessageProducer) {
		try {
			if (MessageProducer != null)
				MessageProducer.close();
		} catch (JMSException e) {
			log.warn("Could not close queue browser", e);
		}
	}

	private static final void closeQueueBrowser(QueueBrowser queueBrowser) {
		try {
			if (queueBrowser != null)
				queueBrowser.close();
		} catch (JMSException e) {
			log.warn("Could not close queue browser", e);
		}
	}

	private static final void closeMessageConsumer(MessageConsumer MessageConsumer) {
		try {
			if (MessageConsumer != null)
				MessageConsumer.close();
		} catch (JMSException e) {
			log.warn("Could not close queue browser", e);
		}
	}

	public final Message duplicateMessage(Message src) throws JMSException {
		if (src == null)
			return null;

		QueueConnection connection = null;
		QueueSession session = null;
		Message dest = null;

		try {

			connection = getConnection();
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

			if (src instanceof ObjectMessage)
				dest = session.createObjectMessage();
			else
				dest = session.createTextMessage();

			Enumeration<?> properties = src.getPropertyNames();
			while (properties.hasMoreElements()) {
				String key = (String) properties.nextElement();
				if (ConstantsJMS.contains(key)) {
					Object value = src.getObjectProperty(key);
					dest.setObjectProperty(key, value);
				}
			}

		} finally {
			closeSession(session);
			closeConnection(connection);
		}

		return dest;

	}


	public final String prenotaBatch(int idCoda, String queueName,
			ParametriRichiestaElaborazioneDifferitaVO richiesta, Map<String, Object> otherProps)
			throws NamingException, JMSException {

		InitialContext ctx = JNDIUtil.getContext();
		Queue queue = (Queue) ctx.lookup(queueName);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ConstantsJMS.ID_CODA, idCoda);
		params.put(ConstantsJMS.STATO, ConstantsJMS.STATO_SEND);
		params.put(ConstantsJMS.COD_ATTIVITA, richiesta.getCodAttivita());
		params.put(ConstantsJMS.BIBLIOTECA, richiesta.getCodBib());
		params.put(ConstantsJMS.BIBLIOTECARIO, richiesta.getUser());
		params.put(ConstantsJMS.DATA_ORA_RICHIESTA, DateUtil.now());
		params.put(ConstantsJMS.VISIBILITA, richiesta.getVisibilita() );

		if (ValidazioneDati.isFilled(otherProps))
			for (String key : otherProps.keySet())
				if (ConstantsJMS.contains(key))
					params.put(key, otherProps.get(key) );

		int idBatch = calcolaIdPrenotazioneBatch();
		String id = String.valueOf(idBatch);
		richiesta.setIdBatch(id);
		params.put(ConstantsJMS.ID_BATCH, idBatch);

		sendQueue(queue, richiesta, params);
		return id;
	}


	public int calcolaIdPrenotazioneBatch() {
		try {
			return dao.calcolaIdPrenotazioneBatch();
		} catch (DaoManagerException e) {
			e.printStackTrace();
			return -1;
		}
	}


	public Message resendQueue(Queue queue, Message srcMsg,
			Map<String, String> properties) throws JMSException {
		QueueConnection connection = null;
		QueueSession session = null;
		MessageProducer sender = null;

		try {
			connection = getConnection();
			session = connection.createQueueSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);

			Message newMsg = duplicateMessage(srcMsg);
			if (srcMsg instanceof ObjectMessage)
				((ObjectMessage)newMsg).setObject(((ObjectMessage) srcMsg).getObject());

			newMsg.setJMSReplyTo(queue);

			if (properties != null)
				for (String key : properties.keySet() )
					if (ConstantsJMS.contains(key))
						newMsg.setObjectProperty(key, properties.get(key) );

			sender = session.createSender(queue);
			sender.send(newMsg, DeliveryMode.PERSISTENT, MSG_PRIORITY, MSG_TTL);
			return newMsg;

		} finally {
			closeMessageProducer(sender);
			closeSession(session);
			closeConnection(connection);
		}
	}

	public void startBatch(CodaJMSVO params, Message msg) throws Exception {
		Queue coda = (Queue) ctx.lookup(params.getNome());
		Map<String, String> props = new HashMap<String, String>();
		props.put(ConstantsJMS.STATO, ConstantsJMS.STATO_HELD);
		resendQueue(coda, msg, props);
	}

}
