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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.GestioneLegameTitoloEditoreForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
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

public class GestioneLegameTitoloEditoreAction extends LookupDispatchAction {

    // Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Nuovo Oggetto


	private static Log log = LogFactory.getLog(DettaglioTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
			map.put("button.annulla", "annullaOper");
			map.put("button.ok", "confermaOper");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");
		Navigation.getInstance(request).setTesto("Gestione legame Titolo Editore");
		GestioneLegameTitoloEditoreForm gestLegTEForm = (GestioneLegameTitoloEditoreForm) form;

		gestLegTEForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));

		if (gestLegTEForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Cancella")) {
			return confermaOper(mapping, form, request, response);
		}

		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloEditoreForm gestLegTEForm = (GestioneLegameTitoloEditoreForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute("bid", gestLegTEForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		request.setAttribute("livRicerca", "I");

		return
		Navigation.getInstance(request).goBack( mapping.findForward("annulla"), true);
	}

	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloEditoreForm gestLegTEForm = (GestioneLegameTitoloEditoreForm) form;

		gestLegTEForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
		gestLegTEForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
		gestLegTEForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);

		if (gestLegTEForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
			if (gestLegTEForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
    			ActionMessages errors = new ActionMessages();
    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
    			this.saveErrors(request, errors);
    			return mapping.getInputForward();
			}
		}

		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = null;

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			areaDatiVariazioneReturnVO = factory.getGestioneAcquisizioni().gestioneLegameTitEdit(
							gestLegTEForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		request.setAttribute("livRicerca", "P");

		if (areaDatiVariazioneReturnVO == null) {
			request.setAttribute("bid", null);
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}
		if (areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
		}
		if (areaDatiVariazioneReturnVO.getCodErr().equals("9999") || areaDatiVariazioneReturnVO.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiVariazioneReturnVO.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."
							+ areaDatiVariazioneReturnVO.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward leggiDatiLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		GestioneLegameTitoloEditoreForm gestLegTEForm = (GestioneLegameTitoloEditoreForm) form;


		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = null;

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			areaDatiVariazioneReturnVO = factory.getGestioneAcquisizioni().gestioneLegameTitEdit(
							gestLegTEForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		request.setAttribute("livRicerca", "P");

		if (areaDatiVariazioneReturnVO == null) {
			request.setAttribute("bid", null);
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}
		if (areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
		}
		if (areaDatiVariazioneReturnVO.getCodErr().equals("9999") || areaDatiVariazioneReturnVO.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiVariazioneReturnVO.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."
							+ areaDatiVariazioneReturnVO.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();

	}



}
