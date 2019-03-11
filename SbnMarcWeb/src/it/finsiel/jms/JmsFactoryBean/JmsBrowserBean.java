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
package it.finsiel.jms.JmsFactoryBean;


import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

public class JmsBrowserBean {
	private static org.apache.log4j.Logger logger =
        org.apache.log4j.Logger.getLogger(JmsBrowserBean.class.getName());

	private transient QueueConnectionFactory queueConnectionFactory;


	public JmsBrowserBean(Context jndiContext) throws NamingException {
		super();
		this.init(jndiContext);
	}

	private void init(Context jndiContext) throws NamingException {
		queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");

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
    public Enumeration<?> browseQueue(Queue queue) throws JMSException {
        return browseQueue(queue, null);
    }

    /**
     * Returns an <code>Enumeration</code> that is used to scan the queue's messages.
     *
     * @param queue the queue to browse
     * @param messageSelector the message selector to use
     * @return Enumeration containing the messages on the queue.
     * @throws JMSException if an error occurs while establishing the connection with the JMS provider.
     *
     * @ejb.interface-method
     */
    public Enumeration<?> browseQueue(Queue queue, String messageSelector) throws JMSException {
        QueueConnection connection = null;
        QueueSession session = null;
        QueueBrowser browser = null;
        Enumeration<?> queueContents = null;

        try {
            connection = getConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            if (messageSelector != null) {
                browser = session.createBrowser(queue, messageSelector);
            } else {
                browser = session.createBrowser(queue);
            }
            queueContents = browser.getEnumeration();
        } finally {
            closeQueueBrowser(browser);
            closeSession(session);
            closeConnection(connection);
        }

        return queueContents;
    }

    /**
     * Returns a messages using the specified start date and end date.
     * @param queue the queue to browse
     * @param startDate the beginning date. If null no constraint is set on the start date
     * @param endDate the end date. If null, no constraint is set on the end date
     * @return an <code>Enumaration</code> of messages posted between <tt>startDate</tt> and <tt>endDate</tt>
     * @throws JMSException if an error occurs while establishing the connection with the JMS provider.
     *
     * @ejb.interface-method
     */
    public Enumeration<?> browseQueue(Queue queue, Date startDate, Date endDate) throws JMSException {
        // Let's build the selector
        String selector = null;
        if (startDate != null && endDate != null) {
            selector = "JMSTimestamp >= " + startDate.getTime() + " AND JMSTimestamp <=" + endDate.getTime();
        } else if (startDate != null) {
            selector = "JMSTimestamp >= " + startDate.getTime();
        } else if (endDate != null) {
            selector = "JMSTimestamp <=" + endDate.getTime();
        } else { // no date is set
            selector = null;
        }

        return browseQueue(queue, selector);
    }



    public List<TextMessage> receiveQueue(Queue queue, String messageSelector) throws JMSException {
        QueueConnection connection = null;
        QueueSession session = null;
        QueueReceiver receiver = null;
        TextMessage received = null;
        List<TextMessage> response = new ArrayList<TextMessage>();

        try {
            connection = getConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            if (messageSelector != null) {
            	receiver = session.createReceiver(queue, messageSelector);
            } else {
            	receiver = session.createReceiver(queue);
            }
            logger.info("Try to receive message, it will not work");
            received = (TextMessage)receiver.receiveNoWait();
            if (received != null)
               throw new RuntimeException("Should not get a message if the connection is not started!");

            logger.info("You have to start the connection before receiving messages");
            connection.start();

            logger.info("This receive will work");
            do{
            received = (TextMessage)receiver.receiveNoWait();
            response.add(received);
            }while (received!=null);

        } finally {
        	closeQueueReceiver(receiver);
            closeSession(session);
            closeConnection(connection);
        }

        return response;
    }





    /**
     * Chiude la connessione JMS.
     */
    private void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (JMSException e) {
            logger.warn("Could not close JMS connection", e);
        }
    }

    /**
     *  Chiude la sessione JMS.
     */
    private void closeSession(Session session) {
        try {
            if (session != null)
                session.close();
        } catch (JMSException e) {
            logger.warn("Could not close JMS session", e);
        }
    }

    private void closeQueueBrowser(QueueBrowser queueBrowser) {
        try {
            if (queueBrowser!= null)
                queueBrowser.close();
        } catch (JMSException e) {
            logger.warn("Could not close queue browser", e);
        }
    }

    private void closeQueueReceiver(QueueReceiver queueReceiver) {
        try {
            if (queueReceiver!= null)
            	queueReceiver.close();
        } catch (JMSException e) {
            logger.warn("Could not close queue browser", e);
        }
    }


    protected QueueConnection getConnection() throws JMSException {
        return queueConnectionFactory.createQueueConnection();
    }
}
