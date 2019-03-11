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
package it.iccu.sbn.ejb.services.pagination;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.servizi.pagination.Pagination;
import it.iccu.sbn.servizi.pagination.PaginationCacheStrategy;
import it.iccu.sbn.servizi.pagination.QueryExecutionLogic;
import it.iccu.sbn.servizi.pagination.QueryExecutionParams;
import it.iccu.sbn.servizi.pagination.QueryPage;
import it.iccu.sbn.servizi.pagination.QueryPage.State;
import it.iccu.sbn.servizi.pagination.impl.QueryPageImpl;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.vo.domain.pagination.PaginationInternalParamsVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import org.apache.log4j.Logger;

public class QueryPaginationExecutorBean implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 6814022689705787815L;
	private static Logger log = Logger.getLogger(QueryPaginationExecutorBean.class);
	private MessageDrivenContext ctx;
	private JMSUtil jms;

	private static PaginationCacheStrategy cacheMgr;


	public void ejbCreate() throws EJBException {
		try {
			jms = new JMSUtil(JNDIUtil.getContext());
			cacheMgr = getCacheInstance();

		} catch (Exception e) {
			throw new EJBException(e);
		}
		return;
	}



	public void ejbRemove() throws EJBException {
		return;
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) throws EJBException {
		this.ctx = mdc;
		log.debug(ctx);
	}

	private static PaginationCacheStrategy getCacheInstance() throws Exception {
		if (cacheMgr == null) {
			String type = CommonConfiguration.getProperty(Configuration.PAGE_CACHE_STRATEGY_CLASS);
			ClassLoader loader = Thread.currentThread().getContextClassLoader();

			Class<?> clazz = loader.loadClass(type);
			cacheMgr = (PaginationCacheStrategy) clazz.newInstance();
			cacheMgr.init();
		}

		return cacheMgr;
	}

	private QueryExecutionLogic createExecutorInstance(String className) throws Exception {

		if (ValidazioneDati.strIsNull(className))
			throw new ValidationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class<?> clazz = loader.loadClass(className);
		return (QueryExecutionLogic) clazz.newInstance();
	}

	public void onMessage(Message message) {
		String idLista = UUID.randomUUID().toString();
		try {
			message.acknowledge();
			log.debug(message);

			if (!(message instanceof ObjectMessage)) {
				log.error("Messaggio non gestito");
				return;
			}

			ObjectMessage objMsg = (ObjectMessage) message;
			Serializable obj = objMsg.getObject();
			if (!(obj instanceof QueryExecutionParams)) {
				log.error("Messaggio non gestito");
				return;
			}

			QueryExecutionParams params = (QueryExecutionParams) obj;
			int pageSize = objMsg.getIntProperty(Pagination.PAGE_SIZE);
			idLista = objMsg.getStringProperty(Pagination.ID_LISTA);
			QueryExecutionLogic logic = createExecutorInstance(objMsg.getStringProperty(Pagination.LOGIC_CLASS));

			logic.init(params);

			int cnt = logic.count(params);
			log.debug(idLista + " - Elementi trovati: " + cnt);
			if (cnt == 0) {
				sendNotification(idLista, State.NOT_FOUND);
				return;
			}

			int pageCount = (int) (Math.ceil((double) cnt / pageSize));
			log.debug(idLista + " - Numero pagine da produrre: " + pageCount);

			logic.begin(params);

			PaginationInternalParamsVO pip = new PaginationInternalParamsVO(logic, params, idLista, pageSize, pageCount, cnt);

			Serializable[] ids = prepareNextPageIds(pip);
			String key = getKey(pip, 1);
			getCacheInstance().put(key, ids);

			QueryPage page1 = preparePage(pip, 1);
			sendPage(page1);

			if (cnt > pageSize)
				asyncPreparePages(pip);

			logic.end(params);

//		} catch (JMSException e) {
//			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
			try {
				sendNotification(idLista, State.ERROR);
			} catch (Exception e1) {
				log.error("", e);
			}
		}
	}

	private String getKey(PaginationInternalParamsVO pip, int i) {
		return pip.getIdLista() + "_" + i;
	}

	private void asyncPreparePages(PaginationInternalParamsVO pip) throws Exception {

		PaginationCacheStrategy cache = getCacheInstance();
		int pageCount = pip.getPageCount();
		for (int page = 2; page <= pageCount; page++) {
			Serializable[] ids = prepareNextPageIds(pip);
			String key = getKey(pip, page);
			cache.put(key, ids);
			log.debug(pip.getIdLista() + " - memorizzata lista id blocco: " + key + ", ids: " + ids);
		}

	}

	private QueryPage preparePage(PaginationInternalParamsVO pip, int pageNumber) throws Exception {

		String key = getKey(pip, pageNumber);
		PaginationCacheStrategy cache = getCacheInstance();
		Serializable[] ids = (Serializable[])cache.get(key);
		if (ids == null)
			return null; //pagina non pronta...

		cache.remove(key);

		QueryExecutionLogic logic = pip.getLogic();
		int pageSize = pip.getPageSize();
		List<Serializable> lista = new ArrayList<Serializable>(pageSize);

		int start = pageSize * (pageNumber - 1);
		int rowsInPage = 0;

		for (int idx = 0; idx < pageSize; idx++) {
			if (ids[idx] != null) {
				lista.add(idx, logic.getData(pip.getParams(), ids[idx]));
				rowsInPage++;
			}
		}

		log.debug(pip.getIdLista() + " - preparata pagina n. " + pageNumber);

		return new QueryPageImpl(pip.getIdLista(), pageNumber,
				pip.getRowCount(), start, rowsInPage, pip.getPageCount(),
				pageSize, lista, State.OK);
	}

	private Serializable[] prepareNextPageIds(PaginationInternalParamsVO pip) throws Exception {

		QueryExecutionLogic logic = pip.getLogic();
		int pageSize = pip.getPageSize();
		Serializable[] ids = new Serializable[pageSize];
		for (int idx = 0; idx < pageSize; idx++)
			ids[idx] = logic.nextId(pip.getParams());

		return ids;
	}


	private void sendPage(QueryPage page) throws Exception {
		Queue q = jms.getQueue(CommonConfiguration.getProperty(Configuration.QUEUE_NAME));

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Pagination.ID_LISTA, page.getIdLista());
		props.put(Pagination.ACTION, Pagination.RETURN_PAGE);

		jms.sendQueue(q, page, props);

	}


	private void sendNotification(String idLista, State state) throws Exception {
		Queue q = jms.getQueue(CommonConfiguration.getProperty(Configuration.QUEUE_NAME));

		QueryPage page = new QueryPageImpl(idLista, 0, 0, 0, 0, 0, 0, null, state);

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Pagination.ID_LISTA, idLista);
		props.put(Pagination.ACTION, Pagination.RETURN_PAGE);

		jms.sendQueue(q, page, props);

	}

}
