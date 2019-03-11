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
package it.iccu.sbn.web2.util;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

public class LinkableTagUtils {

	static Logger log = Logger.getLogger(LinkableTagUtils.class);

	private static final String STRUTS_TOKEN = "org.apache.struts.action.TOKEN";
	private static final String STRUTS_TOKEN_PARAM = "org.apache.struts.taglib.html.TOKEN";

	public static final String SINTETICA_LINK_HREF = "sbnlnk";
	public static final String SINTETICA_LINK_SUBMIT = "sbnbtn";
	public static final String SINTETICA_LINK_NULL = "sbn.null";

	public static final String ANCHOR_PREFIX = "anchor";
	public static final String FORM_TARGET_PARAM = "ftarget";
	public static final String VKBD_BUTTON = "vkbd.button.";

	public static final String SEPARATORE = ":::";
	public static final String BUTTON_BLOCCO = "button.blocco";
	public static final String BLOCCO_BUTTON_TOP = "nav.block.button.top";
	public static final String BLOCCO_VALUE_TOP  = "nav.block.value.top";
	public static final String BLOCCO_BUTTON_BOTTOM = "nav.block.button.bottom";
	public static final String BLOCCO_VALUE_BOTTOM  = "nav.block.value.bottom";

	private static final String[] STRING_ARRAY = new String[] { SEPARATORE };


	public static final String getUniqueButtonName(String prefix, String suffix) {
		return prefix + SEPARATORE + IdGenerator.getId() + SEPARATORE + suffix;
	}

	public static final String getTokenUrl(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return null;
		String token = (String) session.getAttribute(STRUTS_TOKEN);
		if (token == null)
			return null;

		return "&" + STRUTS_TOKEN_PARAM + "=" + token;
	}

	public static final String getUniqueButtonName(NavigationElement element, String value, String property,
			String message) throws JspException {

		String linkValue = getUniqueLinkableValue(element, value, property, message);
		return SINTETICA_LINK_HREF + "=" + ValidazioneDati.dumpBytes(linkValue.getBytes() );
	}

	public static String getUniqueLinkableValue(NavigationElement element, String value, String property, String message) {
		StringBuilder buf = new StringBuilder();
		buf.append(element.getPosition());
		buf.append(SEPARATORE);
		buf.append(value);
		buf.append(SEPARATORE);
		buf.append(property);
		buf.append(SEPARATORE);
		buf.append(message);
		return buf.toString();
	}

	public static final String createButtonAttribute(HttpServletRequest request, String paramValue, ActionForm form) {
		try {
			//Riporto la stringa esadecimale in una forma leggibile da splittare
			// 0 = posizione sulla navigazione
			// 1 = valore chiave
			// 2 = property target sulla form
			// 3 = key del bottone
			String[] tokens = ValidazioneDati.unmaskString(paramValue).split(SEPARATORE);

			// 0. la posizione della form nella navigazione
			int position  = Integer.parseInt(tokens[0]);
			NavigationElement element = Navigation.getInstance(request).getCache().getCurrentElement();
			if (element.getPosition() != position)
				return null;

			// 1. valore del progressivo o chiave
			String value = tokens[1];
			element.setAnchorId(ANCHOR_PREFIX + value);

			// 2. imposto nella form il valore della property indicata nel tag
			Class<?> propertyType = PropertyUtils.getPropertyType(form, tokens[2]);
			if (propertyType.isInstance(SEPARATORE) )// verifico se di tipo String
				PropertyUtils.setProperty(form, tokens[2], value);
			else if (propertyType.isArray()) {
				if (propertyType.isInstance(STRING_ARRAY) )
					PropertyUtils.setProperty(form, tokens[2], new String[]{value});
				else
					if (propertyType.isInstance(new Long[0]) )
						PropertyUtils.setProperty(form, tokens[2], new Long[]{new Long(value)});
					else
						//SSì20101013 riconoscimento array interi
						PropertyUtils.setProperty(form, tokens[2], new Integer[]{new Integer(value)});
			} else
				PropertyUtils.setProperty(form, tokens[2], new Integer(value));

			// 3. ritorno il testo della message key per la ricerca nei metodi mappati nella action
			return tokens[3];//.replaceAll(SEPARATORE, BLANK);

		} catch (Exception e) {
			return null;
		}

	}

	public static final String findMessage(PageContext pageContext, String locale,
			String key, Object[] values) {

		ServletRequest request = pageContext.getRequest();
		ModuleConfig moduleConfig =  TagUtils.getInstance().getModuleConfig(pageContext);
		Locale userLocale = TagUtils.getInstance().getUserLocale(pageContext, locale);
		MessageResourcesConfig mrc[] = moduleConfig.findMessageResourcesConfigs();
		String message = null;

		for (int i = 0; i < mrc.length; i++) {
			MessageResources resources = (MessageResources) request.getAttribute(mrc[i].getKey());
			message = resources.getMessage(userLocale, key, values);
			if (!(message == null || message.startsWith("???")))
				break;

			message = null;
		}

		if (message == null)
			return key;

		return message;
	}

	public static final String findMessage(HttpServletRequest request, Locale locale,
			String key, Object... values) {

		ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(request);
		MessageResourcesConfig mrc[] = moduleConfig.findMessageResourcesConfigs();
		String message = null;

		for (int i = 0; i < mrc.length; i++) {
			MessageResources resources = (MessageResources) request.getAttribute(mrc[i].getKey());
			message = resources.getMessage(locale, key, values);
			if (!(message == null || message.startsWith("???")))
				break;

			message = null;
		}

		if (message == null)
			return key;

		return message;
	}

	public static final String getVirtualKeyboardButtonName() {
		return VKBD_BUTTON + IdGenerator.getId();
	}

	@SuppressWarnings("unchecked")
	public static final void addError(HttpServletRequest request, ActionMessage message, boolean noDuplicati) {

		ActionMessages errors = getErrors(request);
		//errori già presenti su request, controllo duplicati
		if (noDuplicati) {
			Iterator<ActionMessage> i = errors.get();
			while (i.hasNext())
				if (message.getKey().equals(i.next().getKey()))
					return;
		}

		errors.add(ActionMessages.GLOBAL_MESSAGE, message);
	}


	public static final void addError(HttpServletRequest request, ActionMessage message) {
		addError(request, message, true);
	}


	public static final void addError(HttpServletRequest request, SbnBaseException e) {
		List<SbnBaseException> exceptions = e.getExceptions();

		for (SbnBaseException sbe : exceptions) {
			String[] labels = sbe.getLabels();
			if (ValidazioneDati.isFilled(labels)) {
				List<String> tmp = new ArrayList<String>(labels.length);
				for (String l : labels)
					tmp.add(findMessage(request, Locale.getDefault(), l));
				labels = tmp.toArray(labels);
			}

			addError(request, new ActionMessage(sbe.getErrorCode().getErrorMessage(), labels));

			if (sbe.getErrorCode().isIncludeId())
				addError(request, new ActionMessage("ERROR_ID_TEMPLATE", new String[] {String.valueOf(sbe.getErrorId())} ));

			Navigation.getInstance(request).setExceptionLog(e);
		}
	}


	public static final void resetErrors(HttpServletRequest request) {
		ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
		if (errors != null)
			errors.clear();
	}

	public static final ActionMessages getErrors(HttpServletRequest request) {
		ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null) {
			errors = new ActionMessages();
			request.setAttribute(Globals.ERROR_KEY, errors);
		}

		return errors;
	}

	public static boolean checkAttivita(HttpServletRequest request, boolean inverted, String idControllo) {
		NavigationElement currentElement = Navigation.getInstance(request).getCache().getCurrentElement();
		if (currentElement == null)
			return false;
		Action action = currentElement.getActionInstance();
		if (action == null)
			return false;
		if (!(action instanceof SbnAttivitaChecker))
			return true;
		if (!ValidazioneDati.isFilled(idControllo))
			return false;

		ActionForm form = currentElement.getForm();
		boolean check = false;
		SbnAttivitaChecker checkable = (SbnAttivitaChecker) action;
		for (String test : idControllo.split("\\|"))
			try {
				check = checkable.checkAttivita(request, form, ValidazioneDati.trimOrEmpty(test));
				check = inverted ? !check : check;
				if (check)
					break;

			} catch (Exception e) {
				log.error("", e);
			};

		return check;

	}

	public static boolean hasErrors(HttpServletRequest request) {
		ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
		return (errors != null && !errors.isEmpty());
	}

}
