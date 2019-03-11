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
package it.iccu.sbn.web2.action;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationBlocchiInfo;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public abstract class SinteticaLookupDispatchAction extends	NavigationBaseAction {

	public static final String SBNMARC_IDLISTA = "nav.sbnmarc.idlista";

	private static final String[] MULTIBOX_OFF_VALUES = new String[] {"0", "null", "false", "off"};

	private String findButtonParameter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Enumeration<?> params = request.getParameterNames();

		// Cerco tra i parametri della request quello del pulsante
		// generato dal tag sbn:linkbutton
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			//if (paramName.startsWith(LinkableTagUtils.SINTETICA_LINK_SUBMIT))
			//almaviva5_20101009
			if (ValidazioneDati.firstIndexOf(paramName, LinkableTagUtils.SINTETICA_LINK_HREF, LinkableTagUtils.SINTETICA_LINK_SUBMIT) > -1)
				try {
					String parameter = request.getParameter(paramName);
					if (parameter == null || ValidazioneDati.equals(parameter, LinkableTagUtils.SINTETICA_LINK_NULL) )
						continue;
					return LinkableTagUtils.createButtonAttribute(request, parameter, form);

				} catch (Exception e) {
					return null;
				}
		}
		return null;
	}

	protected <T> List<T> getMultiBoxSelectedItems(T... values) {
		if (!ValidazioneDati.isFilled(values))
			return Collections.emptyList();

		List<T> out = new ArrayList<T>(values.length);
		for (T v : values) {
			String value = v != null ? ValidazioneDati.trimOrNull(v.toString()) : null;
			if (value != null && !ValidazioneDati.in(value, MULTIBOX_OFF_VALUES))
				out.add(v);
		}

		return out;
	}

	protected Locale getLocale(HttpServletRequest request, String locale) {
		return RequestUtils.getUserLocale(request, locale);
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		return new THashMap<String, String>();
	}

	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String parameter) throws Exception {

		// cerco tra i parametri della request se è stato premuto uno dei tasti
		// generati dal tag sbn:linkbutton
		String keyName = this.findButtonParameter(mapping, form, request, response);
		if (keyName != null) {
			applicationButtonClicked(request, form);
			return getLookupMapName(request, keyName, mapping);
		}

		// verifico se è stato premuto il tasto carica blocco
		keyName = this.checkBlocco(request);
		if (keyName != null) {
			applicationButtonClicked(request, form);
			return getLookupMapName(request, keyName, mapping);
		// porto a 0 le properties impostate da sbn:linkbutton
		// this.resetProperties(request, form);
		}

		// comportamento standard di struts
		String methodName = super.getMethodName(mapping, form, request, response, parameter);
		if (methodName != null)
			applicationButtonClicked(request, form);
		return methodName;
	}

	@SuppressWarnings("unchecked")
	protected void addSbnMarcIdLista(HttpServletRequest request, String idLista) {

		if (ValidazioneDati.strIsNull(idLista) )
			return;

		Navigation navi = Navigation.getInstance(request);
		Set<String> idListaSet = (Set<String>) navi.getAttribute(SBNMARC_IDLISTA);
		if (idListaSet == null) {
			idListaSet = new HashSet<String>();
			navi.setAttribute(SBNMARC_IDLISTA, (Serializable) idListaSet, true);
		}

		idListaSet.add(idLista);
	}

	protected void resetProperties(HttpServletRequest request, ActionForm form) {
		NavigationElement element = Navigation.getInstance(request).getCache().getCurrentElement();
		Integer zero = new Integer(0);
		Set<String> properties = element.getLinkButtonProperties();
		for (String property : properties)
			try {
				Class<?> propertyType = PropertyUtils.getPropertyType(form,	property);
				if (propertyType.isInstance(property)) // verifico se di tipo String
					PropertyUtils.setProperty(form, property, null);
				else
					PropertyUtils.setProperty(form, property, zero);

			} catch (Exception e) {
				return;
			}

	}

	private String checkBlocco(HttpServletRequest request) {

		NavigationElement element = Navigation.getInstance(request).getCache().getCurrentElement();
		NavigationBlocchiInfo infoBlocchi = element.getInfoBlocchi();
		if (infoBlocchi == null)
			return null;

		String bloccoValue = null;
		String bloccoRes = null;

		String buttonTop = request.getParameter(LinkableTagUtils.BLOCCO_BUTTON_TOP);
		if (buttonTop != null) {
			bloccoValue = request.getParameter(LinkableTagUtils.BLOCCO_VALUE_TOP);
			if (ValidazioneDati.strIsNull(bloccoValue))
				bloccoValue = "0";
			bloccoRes = buttonTop;
		}

		String buttonBottom = request.getParameter(LinkableTagUtils.BLOCCO_BUTTON_BOTTOM);
		if (buttonBottom != null) {
			bloccoValue = request.getParameter(LinkableTagUtils.BLOCCO_VALUE_BOTTOM);
			if (ValidazioneDati.strIsNull(bloccoValue))
				bloccoValue = "0";
			bloccoRes = buttonBottom;
		}

		if (bloccoValue == null)
			return null;

		try {
			ActionForm form = element.getForm();
			String numBloccoFormProperty = element.getInfoBlocchi().getNumBloccoFormProperty();
			Class<?> propertyType = PropertyUtils.getPropertyType(form,	numBloccoFormProperty);
			if (propertyType.isInstance(bloccoValue)) // verifico se di tipo String
				PropertyUtils.setProperty(form, numBloccoFormProperty, bloccoValue);
			else
				PropertyUtils.setProperty(form, numBloccoFormProperty, new Integer(bloccoValue));

			log.info("Imposta valore blocco a: " + bloccoValue);

			int numBloccoJsp = Integer.parseInt(bloccoValue);
			if (numBloccoJsp < 1 || numBloccoJsp > infoBlocchi.getTotBlocchi())
				return bloccoRes;

			int position = (infoBlocchi.getElementiPerBlocco() * (numBloccoJsp - 1)) + 1;
			// Se ho specificato un'ancora interna alla pagina la imposto sul
			// tag body della jsp
			// FUNZIONA SOLO CON JAVASCRIPT!!
			element.setAnchorId(LinkableTagUtils.ANCHOR_PREFIX + position);
			request.setAttribute(LinkableTagUtils.ANCHOR_PREFIX, element.getAnchorId());

		} catch (Exception e) {
			return null;
		}
		return bloccoRes;

	}

	protected void applicationButtonClicked(HttpServletRequest request, ActionForm form) {
		return;
	}

}
