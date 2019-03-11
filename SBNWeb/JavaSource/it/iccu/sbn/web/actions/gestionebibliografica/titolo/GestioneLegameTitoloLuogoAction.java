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
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.GestioneLegameTitoloLuogoForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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

public class GestioneLegameTitoloLuogoAction extends LookupDispatchAction {

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
		GestioneLegameTitoloLuogoForm gestLegTLForm = (GestioneLegameTitoloLuogoForm) form;

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		gestLegTLForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));
		gestLegTLForm.setListaRelatorCode(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLegameTitoloLuogo()));
		if (gestLegTLForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew() == null) {
			gestLegTLForm.getAreaDatiLegameTitoloVO().setRelatorCodeNew("P");
		}


		if (gestLegTLForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Cancella")) {
			return confermaOper(mapping, form, request, response);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();

	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloLuogoForm gestLegTLForm = (GestioneLegameTitoloLuogoForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute("bid", gestLegTLForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		request.setAttribute("livRicerca", "I");

		return
		Navigation.getInstance(request).goBack( mapping.findForward("annulla"), true);
	}

	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloLuogoForm gestLegTLForm = (GestioneLegameTitoloLuogoForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (!gestLegTLForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()
				|| !gestLegTLForm.getAreaDatiLegameTitoloVO().isFlagCondivisoArrivo()) {
			gestLegTLForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
			gestLegTLForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
			gestLegTLForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
		} else {
			gestLegTLForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(true);
			gestLegTLForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(true);
			gestLegTLForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(false);
		}


		// Inizio Modifica almaviva2 01.12.2010 BUG MANTIS 4023 verifica che la nota al legame non superi gli 80 caratteri
		// (GestioneLegameTitoloLuogoAction - confermaOper)
		if (gestLegTLForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
			if (gestLegTLForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
    			ActionMessages errors = new ActionMessages();
    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
    			this.saveErrors(request, errors);
    			return mapping.getInputForward();
			}
		}
		// Fine Modifica almaviva2 01.12.2010 BUG MANTIS 4023


		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		try {
			areaDatiPassReturn = factory.getGestioneBibliografica()
					.inserisciLegameTitolo(gestLegTLForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		if (gestLegTLForm.getAreaDatiLegameTitoloVO().isInserimentoIndice()) {
			request.setAttribute("livRicerca", "I");
		} else {
			request.setAttribute("livRicerca", "P");
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}
		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			//request.setAttribute("bid", areaDatiPassReturn.getBid());
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			//return mapping.findForward("analitica");
		}
		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

}
