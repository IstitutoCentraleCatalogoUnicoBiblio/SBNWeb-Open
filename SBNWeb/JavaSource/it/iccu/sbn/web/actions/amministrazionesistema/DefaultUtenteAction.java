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
package it.iccu.sbn.web.actions.amministrazionesistema;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.AreaVO;
import it.iccu.sbn.web.actionforms.amministrazionesistema.gestioneDefault.DefaultUtenteForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.DefaultDelegate;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class DefaultUtenteAction extends LookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
//		map.put("profilo.button.pulsante",           "GestioneDefault");
		return map;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		DefaultUtenteForm myForm = (DefaultUtenteForm) form;

    	if (!isTokenValid(request))
			saveToken(request);

		DefaultDelegate delegate = DefaultDelegate.getInstance(request);;
		Utente user = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Map<?, ?> attivita = user.getListaAttivita();
		List<AreaVO> listaAreeUtente = delegate.caricaAreeUtente(request, attivita);
		myForm.setelencoAree(listaAreeUtente);

    	return mapping.getInputForward();
    }

	public ActionForward GestioneDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DefaultUtenteForm defForm = (DefaultUtenteForm) form;
		String areaSelezionata = defForm.getSelezione();
		request.setAttribute("area", areaSelezionata);
		request.setAttribute("user", NavigazioneProfilazione.ID_UTENTE);
		return mapping.findForward("gestioneDefault");
	}


	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String parameter)
			throws Exception {

		DefaultUtenteForm currentForm = (DefaultUtenteForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(DefaultUtenteForm.SUBMIT_TESTO_PULSANTE) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				currentForm.setSelezione(tokens[2]);
				return "GestioneDefault";
			}

		}
		return super.getMethodName(mapping, form, request, response, parameter);

	}

}
