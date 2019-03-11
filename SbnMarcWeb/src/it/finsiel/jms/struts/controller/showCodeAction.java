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
package it.finsiel.jms.struts.controller;

import it.finsiel.jms.JmsFactoryBean.JmsBrowserBean;
import it.finsiel.jms.struts.model.showCodeModel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class showCodeAction extends LookupDispatchAction{

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.indietro", "indietro");
		map.put("button.esamina", "esamina");
		map.put("button.elimina", "elimina");
		map.put("button.next", "next");
		return map;
	}
	private void init(showCodeModel form)
	{
		form.setID(null);
		form.setDetailsMessage(null);
		form.removeMessage();

	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.init((showCodeModel)form);
		this.loadAllMessage((showCodeModel)form);
		return mapping.getInputForward();
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.init((showCodeModel)form);
		this.loadAllMessage((showCodeModel)form);
		return mapping.getInputForward();
	}
	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.findMessage((showCodeModel)form,((showCodeModel)form).getID());
		return mapping.findForward("esamina");
	}
	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(((showCodeModel)form).getID()!=null)
		{
			this.removeMessage((showCodeModel)form);
		}else
			this.removeAll((showCodeModel)form);

		this.init((showCodeModel)form);
		this.loadAllMessage((showCodeModel)form);
		return mapping.findForward("elimina");
//		return mapping.getInputForward();
	}
	public ActionForward next(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	private Context createContext() throws NamingException
	{
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.factory.url.pkgs",
				"org.jboss.naming:org.jnp.interfaces");
		env.put(javax.naming.Context.PROVIDER_URL, "127.0.0.1");
		return new InitialContext(env);
	}

	private void removeMessage(showCodeModel form) throws NamingException, JMSException
	{
		String selector = "JMSMessageID = '"
			+ form.getID() + "'";
		form.setDetailsMessage(null);
		Context context = this.createContext();
		JmsBrowserBean jms = new JmsBrowserBean(context);
		jms.receiveQueue((Queue)context.lookup("queue/sbnMarcBlocchi"),selector);
	}


	private void removeAll(showCodeModel form) throws NamingException, JMSException
	{
		Context context = this.createContext();
		JmsBrowserBean jms = new JmsBrowserBean(context);
		jms.receiveQueue((Queue)context.lookup("queue/sbnMarcBlocchi"),null);
	}

	private void findMessage(showCodeModel form,String ID) throws NamingException, JMSException
	{
		Context context = this.createContext();
		JmsBrowserBean jms = new JmsBrowserBean(context);

		String selector = "JMSMessageID = '"
			+ ID + "'";

		Enumeration<?> enumeration = jms.browseQueue((Queue)context.lookup("queue/sbnMarcBlocchi"),selector);
		while (enumeration.hasMoreElements()) {
			TextMessage message = (TextMessage) enumeration.nextElement();
			form.setDetailsMessage(message);

		}
	}
	private void loadAllMessage(showCodeModel form) throws NamingException, JMSException
	{
		Context context = this.createContext();
		JmsBrowserBean jms = new JmsBrowserBean(context);
		Enumeration<?> enumeration = jms.browseQueue((Queue)context.lookup("queue/sbnMarcBlocchi"));
		while (enumeration.hasMoreElements()) {
			TextMessage message = (TextMessage) enumeration.nextElement();
			form.addMessage(message);
		}
	}

}
