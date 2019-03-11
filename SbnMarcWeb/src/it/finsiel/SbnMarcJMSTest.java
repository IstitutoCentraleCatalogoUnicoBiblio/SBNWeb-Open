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
package it.finsiel;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet implementation class for Servlet: SbnMarcTest
 *
 * @web.servlet name="SbnMarcTest" display-name="SbnMarcTest"
 *
 * @web.servlet-mapping url-pattern="/SbnMarcTest"
 *
 */
public class SbnMarcJMSTest extends HttpServlet implements Servlet {

	private static final long serialVersionUID = -5613703197733051836L;

	protected static Log log = LogFactory.getLog("sbnmarcPolo");

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public SbnMarcJMSTest() {
		super();

	}

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream()));

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		env.put(javax.naming.Context.PROVIDER_URL, "127.0.0.1");

		try {
			Context context = new InitialContext(env);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
			QueueConnection queueConnection = queueFactory.createQueueConnection();

			QueueSession queueSession = queueConnection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) context.lookup("queue/sbnMarcBlocchi");

			/**
			 * INVIO MESSAGGI
			 */

			QueueSender sender = queueSession.createSender(queue);

			TextMessage hello = queueSession.createTextMessage();
			hello.setText("Prova");
			hello.setJMSReplyTo(queue);
			hello.setJMSCorrelationID("123456789_1");
			sender.send(queue, hello);

			hello = queueSession.createTextMessage();
			hello.setText("Prova2");
			hello.setJMSReplyTo(queue);
			hello.setJMSCorrelationID("123456789_2");
			sender.send(queue, hello);

			hello = queueSession.createTextMessage();
			hello.setText("Prova3");
			hello.setJMSReplyTo(queue);
			hello.setJMSCorrelationID("123456789_3");
			sender.send(queue, hello);

			hello = queueSession.createTextMessage();
			hello.setText("Prova4");
			hello.setJMSReplyTo(queue);
			hello.setJMSCorrelationID("123456789_4");
			sender.send(queue, hello);

			//String msgID = hello.getJMSMessageID();

			// String selector = "JMSMessageID = '" + msgID + "'";
			String selector = "JMSCorrelationID = '123456789_2'";
			QueueReceiver queueReceiver = queueSession.createReceiver(queue, selector);
			// QueueReceiver queueReceiver = queueSession.createReceiver(queue);
			TextMessage msg = (TextMessage) queueReceiver.receiveNoWait();
			if (msg != null)
				throw new RuntimeException("Should not get a message if the connection is not started!");
			queueConnection.start();
			msg = (TextMessage) queueReceiver.receiveNoWait();

			// TextMessage msg = (TextMessage)queueReceiver.receive(5000);
			//
			// if(msg==null)
			// {
			// out.println("non trovato con il selector<br>");
			// queueReceiver = queueSession.createReceiver(queue);
			// msg = (TextMessage)queueReceiver.receiveNoWait();
			// if (msg != null)
			// throw new RuntimeException(
			// "Should not get a message if the connection is not started!");
			// queueConnection.start();
			// msg = (TextMessage)queueReceiver.receiveNoWait();
			// }

			out.print(msg.toString());
			out.close();
			context.close();
			msg.setJMSRedelivered(true);
			queueConnection.close();
		} catch (Exception exc) {
			log.info(exc.toString());
		}

		// log.debug(result);
	}

}
