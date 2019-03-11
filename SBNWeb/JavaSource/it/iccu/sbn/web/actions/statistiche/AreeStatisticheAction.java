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
package it.iccu.sbn.web.actions.statistiche;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.statistiche.AreaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.statistiche.AreeStatisticheForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.statistiche.StatisticheDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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

public class AreeStatisticheAction extends LookupDispatchAction implements SbnAttivitaChecker {


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.indietro", 	"indietro");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	AreeStatisticheForm myForm = (AreeStatisticheForm) form;

		Navigation navigation = Navigation.getInstance(request);
		navigation.setDescrizioneX("Invio richieste Statistiche");
		navigation.setTesto("Invio richieste Statistiche");

		if (!isTokenValid(request))
			saveToken(request);

		StatisticheDelegate delegate = getDelegate(request);
		Utente user = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Map<?, ?> attivita = user.getListaAttivita();
		List<AreaVO> listaAreeUtente = delegate.caricaAreeUtente(request, attivita);
		myForm.setElencoAree(listaAreeUtente);

    	return mapping.getInputForward();
    }

	public ActionForward SinteticaStatistiche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		AreeStatisticheForm statForm = (AreeStatisticheForm) form;
		String areaSelezionata = statForm.getSelezione();
		AreaVO area = statForm.getElencoAree().get(Integer.parseInt(areaSelezionata));
		request.setAttribute("area", area.getIdAreaSezione());
		request.setAttribute("descrArea", area.getDescrizione());
//		request.setAttribute("user", "utente");
		return mapping.findForward("sinteticaStatistiche");
	}


	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String parameter)
			throws Exception {

		AreeStatisticheForm currentForm = (AreeStatisticheForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(AreeStatisticheForm.SUBMIT_TESTO_PULSANTE) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				currentForm.setSelezione(tokens[2]);
				return "SinteticaStatistiche";
			}

		}
		return super.getMethodName(mapping, form, request, response, parameter);

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		AreeStatisticheForm currentForm = (AreeStatisticheForm) form;
		// gestione bottoni
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().STATISTICHE, currentForm.getCodPolo(), currentForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}

	}

	private StatisticheDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new StatisticheDelegate(FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

}
