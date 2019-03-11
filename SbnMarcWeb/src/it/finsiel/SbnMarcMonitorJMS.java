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
import java.util.Enumeration;
import java.util.Hashtable;

import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
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
public class SbnMarcMonitorJMS extends HttpServlet implements
		Servlet {

	private static final long serialVersionUID = 6571378338705756266L;

	protected static Log log = LogFactory.getLog("sbnmarcPolo");
	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public SbnMarcMonitorJMS() {
		super();

	}

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response
				.getOutputStream()));

		String remove = request.getParameter("REMOVE");

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.factory.url.pkgs",
				"org.jboss.naming:org.jnp.interfaces");
		env.put(javax.naming.Context.PROVIDER_URL, "127.0.0.1");

		try {
			Context context = new InitialContext(env);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory) context
					.lookup("ConnectionFactory");
			QueueConnection queueConnection = queueFactory
					.createQueueConnection();

			QueueSession queueSession = queueConnection.createQueueSession(
					false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) context.lookup("queue/sbnMarcBlocchi");

			QueueBrowser browser = queueSession.createBrowser(queue);

			Enumeration<?> enumeration = browser.getEnumeration();
			int cnt = 0;
			if(enumeration.hasMoreElements())
				out.print("Nessun Elemento contenuto nella coda \r\n");
			while (enumeration.hasMoreElements()) {
				TextMessage message = (TextMessage) enumeration.nextElement();
				if (remove != null) {
					String selector = "JMSCorrelationID = '"
							+ message.getJMSCorrelationID() + "'";
					QueueReceiver queueReceiver = queueSession.createReceiver(
							queue, selector);
					TextMessage msg = (TextMessage) queueReceiver
							.receiveNoWait();
					if (msg != null)
						throw new RuntimeException(
								"Should not get a message if the connection is not started!");
					queueConnection.start();
					msg = (TextMessage) queueReceiver.receiveNoWait();
					out.print("Msg N°" + (++cnt) + " RIMOSSO # ");
				} else {
					out.print("Msg N°" + (++cnt) + " #");
				}
				out.print(message.toString() + ") \r\n");
			}

			browser.close();

			out.close();
			context.close();
			queueConnection.close();
		} catch (Exception exc) {
			log.info(exc.toString());
		}
		// log.debug(result);
	}

}
