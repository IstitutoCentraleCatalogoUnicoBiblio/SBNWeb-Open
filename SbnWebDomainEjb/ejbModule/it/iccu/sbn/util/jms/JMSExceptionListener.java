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

import it.iccu.sbn.util.logging.SbnBatchDumpStyle;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

public class JMSExceptionListener implements ExceptionListener {

	private static Logger log = Logger.getLogger(JMSExceptionListener.class);

	private final Connection connection;

	public JMSExceptionListener(Connection connection) {
		super();
		this.connection = connection;
	}

	public void onException(JMSException jmsEx) {
		log.error("Errore connessione JMS: ", jmsEx);
		log.error(ReflectionToStringBuilder.toString(connection, new SbnBatchDumpStyle()) );
		closeJMSConnection(connection);
	}

	private void closeJMSConnection(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (JMSException e) {
			log.warn("Could not close JMS connection", e);
		}
	}
}
