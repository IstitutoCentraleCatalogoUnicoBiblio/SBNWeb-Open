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
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.GestioneLegameTitoloAutoreForm;
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

public class GestioneLegameTitoloAutoreAction extends LookupDispatchAction {

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
		GestioneLegameTitoloAutoreForm gestLegTAForm = (GestioneLegameTitoloAutoreForm) form;

		if (gestLegTAForm.getListaTipoRespons() == null) {
			gestLegTAForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));
		}

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		gestLegTAForm.setListaRelatorCode(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLegameTitoloAutore()));
		gestLegTAForm.setListaTipoRespons(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceResponsabilita()));

		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
		// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
		//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew() == null) {
			gestLegTAForm.setPresenzaSpecStrumVoci("NO");
		} else if (gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("590")
				|| gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("906")) {
			gestLegTAForm.setPresenzaSpecStrumVoci("SI");
			gestLegTAForm.setListaSpecStrumVoci(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceStrumentiVociOrganico()));
		} else  {
			gestLegTAForm.setPresenzaSpecStrumVoci("NO");
		}

		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Cancella")) {
			return confermaOper(mapping, form, request, response);
		}

		// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: INIZIO trascinamento legami autore ma M a N (esempio disco M con tracce N)
		String appoggio="";
		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("TrascinaLegameAutore")) {
			int tappo = 0;
			if (gestLegTAForm.getAreaDatiLegameTitoloVO().getInferioriDaCatturare() != null) {
				tappo = gestLegTAForm.getAreaDatiLegameTitoloVO().getInferioriDaCatturare().length;
			}
			for (int iCat = 0; iCat < tappo; iCat++) {
				if (iCat == 0) {
					appoggio = gestLegTAForm.getAreaDatiLegameTitoloVO().getInferioriDaCatturare()[iCat];
				} else {
					appoggio = appoggio + ", " + gestLegTAForm.getAreaDatiLegameTitoloVO().getInferioriDaCatturare()[iCat];
				}
			}
		}
		gestLegTAForm.setElencoBidArrivoLegamiAutore(appoggio);
		// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: FINE trascinamento legami autore ma M a N (esempio disco M con tracce N)


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloAutoreForm gestLegTAForm = (GestioneLegameTitoloAutoreForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute("bid", gestLegTAForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		request.setAttribute("livRicerca", "I");

		return
		Navigation.getInstance(request).goBack( mapping.findForward("annulla"), true);
	}

	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloAutoreForm gestLegTAForm = (GestioneLegameTitoloAutoreForm) form;

		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoResponsNew().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.obblResponsRelator"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		 //=================
		// Con Codice di Responsabilità 4, gli unici
		// Relator Code ammessi sono i seguenti:
		// 340 - Editor
		// 650 - Editore
		// 700 - Scrittore
		// 750 - Tipografo
        if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoResponsNew().equals("4")) {
        	// L.almaviva2 30/10/2009 inserito controllo su presenza del valore altrimenti nullPointerException
        	if (gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew() != null
        			&& !gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("")) {
        		if ((!gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("650")) &&
        					(!gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("340")) &&
        	                (!gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("700")) &&
        	                (!gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew().equals("750"))) {
        	    			ActionMessages errors = new ActionMessages();
        	    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.RelatorCodeNonValido"));
        	    			this.saveErrors(request, errors);
        	    			return mapping.getInputForward();
        	    }
        	} else {
        		ActionMessages errors = new ActionMessages();
    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.RelatorCodeNonValido"));
    			this.saveErrors(request, errors);
    			return mapping.getInputForward();
        	}

        } else if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoResponsNew().equals("0")) {

            // almaviva2 Bug esercizio Liguria 46 - Riportato intervento su Software irchiesto da De Caro nel 2007
            // non si effettuano controlli su relator code per responsabilità 0
//            List<String>  v = new ArrayList<String>();
//            v.add("200");
//            v.add("820");
//            v.add("790");
//            v.add("280");
//            v.add("290");
//            v.add("390");
//            v.add("590");
//            v.add("040");
//            v.add("005");
//            v.add("275");
//            v.add("580");
//            v.add("190");
//            v.add("300");
//            v.add("250");
//            v.add("310");
//            v.add("320");
//            v.add("400");
//            v.add("110");
//            v.add("160");
//            v.add("500");
//            v.add("490");
//            v.add("420");
//            if (!v.contains(gestLegTAForm.getAreaDatiLegameTitoloVO().getRelatorCodeNew())) {
//    			ActionMessages errors = new ActionMessages();
//    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.RelatorCodeNonValido"));
//    			this.saveErrors(request, errors);
//    			return mapping.getInputForward();
//            }
        }

		 //=================

		if (request.getParameter("areaDatiLegameTitoloVO.superfluoNew") != null) {
			gestLegTAForm.getAreaDatiLegameTitoloVO().setSuperfluoNew(true);
		} else {
			gestLegTAForm.getAreaDatiLegameTitoloVO().setSuperfluoNew(false);
		}
		if (request.getParameter("areaDatiLegameTitoloVO.incertoNew") != null) {
			gestLegTAForm.getAreaDatiLegameTitoloVO().setIncertoNew(true);
		} else {
			gestLegTAForm.getAreaDatiLegameTitoloVO().setIncertoNew(false);
		}



		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (!gestLegTAForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()
				|| !gestLegTAForm.getAreaDatiLegameTitoloVO().isFlagCondivisoArrivo()) {
			gestLegTAForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
			gestLegTAForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
			gestLegTAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
		} else {
			gestLegTAForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(true);
			gestLegTAForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(true);
			gestLegTAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(false);
		}


		// Inizio Modifica almaviva2 01.12.2010 BUG MANTIS 4023 verifica che la nota al legame non superi gli 80 caratteri
		// (GestioneLegameTitoloAutoreAction - confermaOper)
		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
			if (gestLegTAForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
    			ActionMessages errors = new ActionMessages();
    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
    			this.saveErrors(request, errors);
    			return mapping.getInputForward();
			}
		}
		// Fine Modifica almaviva2 01.12.2010 BUG MANTIS 4023


		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;


		// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: INIZIO trascinamento legami autore ma M a N (esempio disco M con tracce N)
		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("TrascinaLegameAutore")) {
			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.trascinaLegameAutore(gestLegTAForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		} // Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: FINE trascinamento legami autore ma M a N (esempio disco M con tracce N)
		else if (gestLegTAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null) {
			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.inserisciLegameTitolo(gestLegTAForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		} else if (gestLegTAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("TU")
			|| gestLegTAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("UM")) {
			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.collegaElementoAuthority(gestLegTAForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		}


		if (gestLegTAForm.getAreaDatiLegameTitoloVO().isInserimentoIndice()) {
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

		if (gestLegTAForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("TrascinaLegameAutore")) {
			if (areaDatiPassReturn.getCodErr().equals("0000") && areaDatiPassReturn.getTestoProtocollo() != null) {
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.operOkConParametro",
						areaDatiPassReturn.getTestoProtocollo()));
				this.saveErrors(request, errors);
				request.setAttribute("messaggio", "operOk");
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			}
		}
		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
		}


		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
		}
		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
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
