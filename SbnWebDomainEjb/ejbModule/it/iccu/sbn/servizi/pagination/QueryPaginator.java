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
package it.iccu.sbn.servizi.pagination;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.NamingException;

public class QueryPaginator {

	private final Class<? extends QueryExecutionLogic> logic;
	private final QueryExecutionParams params;
	private JMSUtil jms;
	private int pageSize;

	public static final QueryPage paginate(Class<? extends QueryExecutionLogic> logic,
			QueryExecutionParams params, int pageSize) throws ApplicationException, ValidationException {
		try {
			QueryPaginator qp = new QueryPaginator(logic, params, pageSize);
			return qp.first();
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	private QueryPage first() throws Exception {
		Queue q = jms.getQueue(CommonConfiguration.getProperty(Configuration.QUEUE_NAME));

		String idLista = UUID.randomUUID().toString();

		Map<String, Object> props = new HashMap<String, Object>();

		props.put(Pagination.ID_LISTA, idLista);
		props.put(Pagination.LOGIC_CLASS, logic.getCanonicalName());
		props.put(Pagination.ACTION, Pagination.FIRST_PAGE);
		props.put(Pagination.PAGE_SIZE, pageSize);

		jms.sendQueue(q, params, props);

		StringBuilder selector = new StringBuilder();
		selector.append(Pagination.ACTION).append("='")
				.append(Pagination.RETURN_PAGE).append("'")
				.append(" AND ")
				.append(Pagination.ID_LISTA).append("='")
				.append(idLista).append("'");

		Message msg = jms.waitAndReceive(q, selector.toString() );
		msg.acknowledge();
		return (QueryPage) ((ObjectMessage)msg).getObject();
	}

	private QueryPaginator(Class<? extends QueryExecutionLogic> logic,
			QueryExecutionParams params, int pageSize) throws ApplicationException, ValidationException {
		this.logic = logic;
		this.params = params;
		this.pageSize = pageSize;
		try {
			this.jms = new JMSUtil(JNDIUtil.getContext());
		} catch (NamingException e) {
			throw new ApplicationException(e);
		}

	}

	public Class<? extends QueryExecutionLogic> getLogic() {
		return logic;
	}

	public QueryExecutionParams getParams() {
		return params;
	}

}
