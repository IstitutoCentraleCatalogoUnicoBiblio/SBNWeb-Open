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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		Alessandro Segnalini - Inizio Codifica Marzo 2008

package it.iccu.sbn.web.actions.documentofisico.possessori;


import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriVariaLegameForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriNotaLegameAction extends LookupDispatchAction  implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(PossessoriNotaLegameAction.class);

	@Override
	protected Map<String,String> getKeyMethodMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("dettaglio.button.annulla" , "annullaNotaLegame");
		map.put("dettaglio.button.salva" , "salvaNotaLegame");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		PossessoriVariaLegameForm possDett =  (PossessoriVariaLegameForm)form;

		String pid=(String)request.getAttribute("bid");
		String desc=(String)request.getAttribute("desc");
		TreeElementViewTitoli reticolo = (TreeElementViewTitoli)request.getAttribute("reticolo");

		possDett.getPossDettVO().setPid(reticolo.getParent().getKey());
		possDett.getPossDettVO().setNome(desc);
		possDett.setPidLegame(reticolo.getKey());
		possDett.setTipoCollegamento(reticolo.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().getTipoLegame());
		possDett.setNotaAlLegame(reticolo.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().getNotaAlLegame());



		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		return mapping.getInputForward();
	}

	public ActionForward annullaNotaLegame(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		PossessoriVariaLegameForm possDett = (PossessoriVariaLegameForm)form;
		request.setAttribute("bid", possDett.getPossDettVO().getPid());
		request.setAttribute("livRicerca", "P");
		return Navigation.getInstance(request).goBack(mapping.findForward("annullaNotaLegame"), true);
	}
	public ActionForward salvaNotaLegame(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
 		PossessoriVariaLegameForm possDett = (PossessoriVariaLegameForm)form;

		String ret = null ;
		try {
			ret = modificaNotaLegame (possDett,Navigation.getInstance(request).getUtente().getFirmaUtente());
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(e.getMessage()));
			this.saveErrors(request, errors);
		    return mapping.getInputForward();
		}
		// qui impostare i dati per la chiamata all'analitica.Per ora fittizi
		request.setAttribute("bid", possDett.getPossDettVO().getPid());
		request.setAttribute("tipoAuthority", "PP");
		request.setAttribute("livRicerca", "P");
		return Navigation.getInstance(request).goForward(mapping.findForward("salvaNotaLegame"));
	}
	private String modificaNotaLegame(PossessoriVariaLegameForm possDettLegame ,String uteFirma ) throws Exception {

		String pid_padre  = possDettLegame.getPossDettVO().getPid();
		String pid_figlio = possDettLegame.getPidLegame();
		String ret="";
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().modificaNotaLegame(pid_padre, pid_figlio, possDettLegame.getNotaAlLegame(), uteFirma);
		return ret;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		PossessoriVariaLegameForm myForm = (PossessoriVariaLegameForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
