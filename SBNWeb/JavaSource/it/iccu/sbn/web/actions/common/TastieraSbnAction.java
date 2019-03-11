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
package it.iccu.sbn.web.actions.common;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.SbnUnicodeMapping;
import it.iccu.sbn.web.actionforms.common.TastieraSbnForm;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.tags.LinkTastieraSbnTag;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

/**
 * TastieraSbnAction.java 21/mag/07
 *
 * @author almaviva
 */
public class TastieraSbnAction extends LookupDispatchAction {

	private final static String METHOD_FOLDER = "folder";

	private static Logger log = Logger.getLogger(TastieraSbnAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.ok", "ok");
		map.put("button.annulla", "annulla");
		return map;
	}

	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String parameter) throws Exception {

		// controllo se ho premuto un tasto non mappato staticamente
		String keyName = request.getParameter(parameter);
		if (!ValidazioneDati.isFilled(keyName) )
			return null;

		TastieraSbnForm currentForm = (TastieraSbnForm) form;
		if (!currentForm.isInitialized())
			return super.getMethodName(mapping, form, request, response, parameter);

		if (currentForm.getCategories().containsKey(keyName))
			return METHOD_FOLDER;

		return super.getMethodName(mapping, form, request, response, parameter);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TastieraSbnForm currentForm = (TastieraSbnForm) form;

		if (!currentForm.isInitialized()) {
			SbnUnicodeMapping unicodeMapping = SbnUnicodeMapping.getInstance();
			currentForm.setKeys(unicodeMapping.getKeys() );
			Map<Object, Object> categories = new TreeMap<Object, Object>(unicodeMapping.getCategories() );
			currentForm.setCategories(categories);
			currentForm.setInitialized(true);
			currentForm.setInputField("test");
			currentForm.setFolder("Combinati Maiuscoli");
			currentForm.setCurrentKeyList((List<?>) currentForm.getCategories().get("Combinati Maiuscoli"));

			this.caricaDati(mapping, form, request, response);
		}
		return mapping.getInputForward();
	}

	private void caricaDati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String data = (String) request.getAttribute(LinkTastieraSbnTag.KEYBOARD_DATA_PARAM);
		if (!ValidazioneDati.isFilled(data) )
			return;

		TastieraSbnForm currentForm = (TastieraSbnForm) form;
		String[] token = data.split(String.valueOf(LinkableTagUtils.SEPARATORE));
		currentForm.setTargetName(token[0]);
		currentForm.setTargetProperty(token[1]);
		currentForm.setLimit(Integer.valueOf(token[2]));

		String fieldValue = "";
		try {
			int id = Integer.valueOf(currentForm.getTargetName());
			Object target = Navigation.getInstance(request).getCache().getElementById(id).getForm();
			fieldValue = (String) PropertyUtils.getProperty(target, currentForm.getTargetProperty());
		} catch (Exception e) {
			log.error("", e);
		}
		currentForm.setInputField(fieldValue);
}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TastieraSbnForm currentForm = (TastieraSbnForm) form;

		if (ValidazioneDati.length(currentForm.getInputField()) > currentForm.getLimit()) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.tastiera.length"));
			return mapping.getInputForward();
		}

		String data = currentForm.getTargetName()
				+ LinkableTagUtils.SEPARATORE
				+ currentForm.getTargetProperty()
				+ LinkableTagUtils.SEPARATORE
				+ currentForm.getInputField();

		request.setAttribute(LinkTastieraSbnTag.KEYBOARD_RESPONSE, data);
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward folder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String folderName = request.getParameter(mapping.getParameter());
		TastieraSbnForm currentForm = (TastieraSbnForm) form;
		currentForm.setFolder(folderName);
		currentForm.setCurrentKeyList((List<Object>) currentForm.getCategories().get(folderName));

		return mapping.getInputForward();
	}

}
