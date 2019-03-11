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
package it.finsiel.sbn.polo.factoring.util;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class JMSObjectLocator {

	private static Logger log = Logger.getLogger(JMSObjectLocator.class);
	private static JMSObjectLocator _instance = null;

	private QueueConnectionFactory queueFactory;
	private Queue queue;

	private static final String ID_BLOCCO = "ID_BLOCCO";
	private static final int TTL = 216000000;
	private static final int RECEIVE_TIMEOUT = 5000;


	private static class JMSExceptionListener implements ExceptionListener {

		private final Connection connection;

		private JMSExceptionListener(Connection connection) {
			super();
			this.connection = connection;
		}

		public void onException(JMSException jmsEx) {

			log.error("Errore connessione JMS: " + connection + ": ", jmsEx);
			closeJMSConnection(connection);
		}
	}


	private JMSObjectLocator() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

		InitialContext ctx = new InitialContext(env);
		this.queueFactory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");

		String jms_name = ResourceLoader.getPropertyString("JMS_CODA_BLOCCHI");
		this.queue = (Queue) ctx.lookup(jms_name);
	}

	public static synchronized JMSObjectLocator getInstance()
			throws NamingException {
		if (_instance == null)
			_instance = new JMSObjectLocator();
		return _instance;
	}

	public Serializable lookup(String identify) throws JMSException, NamingException {

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		MessageConsumer MessageConsumer = null;

		try {
			queueConnection = getConnection();

			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			String selector = ID_BLOCCO + "= '" + identify + "'";

			MessageConsumer = queueSession.createReceiver(queue, selector);
			ObjectMessage msg = (ObjectMessage) MessageConsumer.receiveNoWait();

			if (msg != null)
				throw new RuntimeException("Should not get a message if the connection is not started!");

			queueConnection.start();
			msg = (ObjectMessage) MessageConsumer.receive(RECEIVE_TIMEOUT);

			return msg.getObject();

		} finally {
			closeJMSReceiver(MessageConsumer);
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}
	}

	private QueueConnection getConnection() throws JMSException {
		QueueConnection c = queueFactory.createQueueConnection();
		c.setExceptionListener(new JMSExceptionListener(c));
		return c;
	}

	public void bind(String identify, Serializable message) throws NamingException, JMSException {

		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		try {
			queueConnection = getConnection();

			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			this.sendMessage(queueSession, queue, identify, message);
		} finally {
			closeJMSSession(queueSession);
			closeJMSConnection(queueConnection);
		}
	}

	private void sendMessage(QueueSession queueSession, Queue queue,
			String identify, Serializable message) throws JMSException {

		MessageProducer sender = null;
		try {
			sender = queueSession.createSender(queue);
			ObjectMessage msg = this.prepareMessage(queueSession, queue, identify, message);
			sender.send(queue, msg, DeliveryMode.NON_PERSISTENT, 4, TTL);
		} finally {
			closeJMSSender(sender);
		}
	}

	private ObjectMessage prepareMessage(QueueSession queueSession, Queue queue,
			String identify, Serializable message) throws JMSException {
		ObjectMessage msg = queueSession.createObjectMessage();
		msg.setObject(message);
		msg.setJMSReplyTo(queue);
		msg.setStringProperty(ID_BLOCCO, identify);
		return msg;
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
	 * Chiude la sessione JMS.
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

}
