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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioIncipitVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.GestioneIncipitForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
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

public class GestioneIncipitAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(GestioneIncipitAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gestIncipit.button.salva", "salva");
		map.put("gestIncipit.button.annulla", "annulla");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");

		GestioneIncipitForm gestioneIncipitForm = (GestioneIncipitForm) form;
		this.saveToken(request);

		if (request.getAttribute("tipoProspettazione") == null) {
			gestioneIncipitForm.setTipoProspettazione("INS");
		} else {
			gestioneIncipitForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));
		}

		if (request.getAttribute("dettaglioIncipit") != null ) {
			gestioneIncipitForm.setDettaglioIncipitVO((DettaglioIncipitVO)request.getAttribute("dettaglioIncipit"));
		}

		if (request.getAttribute("progrDettaglioIncipit") != null ) {
			gestioneIncipitForm.setProgrDettIncipit((Integer) request.getAttribute("progrDettaglioIncipit"));
		}

		if (request.getAttribute("idOggColl") != null) {
			gestioneIncipitForm.setIdOggColl((String) request.getAttribute("idOggColl"));
		} else {
			gestioneIncipitForm.setIdOggColl("");
		}
		if (request.getAttribute("descOggColl") != null) {
			gestioneIncipitForm.setDescOggColl((String) request.getAttribute("descOggColl"));
		} else {
			gestioneIncipitForm.setDescOggColl("");
		}

		// Inizio Intervento Febbraio 2014: il campo Voce/Strumento presente nell'incipit puo contenere anche il numero
        // di Voci/Strumento quindi deve essere gestita anche la parte numerica da inserire negli appositi campi di Db;
		if (gestioneIncipitForm.getDettaglioIncipitVO().getVoceStrumentoSelez() != null &&
				!gestioneIncipitForm.getDettaglioIncipitVO().getVoceStrumentoSelez().equals("")) {
        	String strumento = gestioneIncipitForm.getDettaglioIncipitVO().getVoceStrumentoSelez();
        	String numero = "";
        	int posiz = 0;
        	for (int y = 0; y < strumento.length(); y++) {
				if (Character.isDigit(strumento.charAt(y))) {
					posiz = y;
					break;
				}
            }
        	if (posiz > 0) {
        		gestioneIncipitForm.setVoceStrumento(strumento.substring(0, posiz));
        		gestioneIncipitForm.setVoceStrumentoNum(strumento.substring(posiz));
        	} else {
        		// Manutenzione Settembre 2016 Bug esercizio 0006270 almaviva2: Mancato salvataggio strumento per incipit
        		// Viene inserita la valorizzazione del campo Strumento anche nel caso in cui il numero delgli strumenti
        		// non sia valorizzato (prima si faceva solo per maggiore di 0)
        		gestioneIncipitForm.setVoceStrumento(strumento);
        	}
		}
		// Fine Intervento Febbraio 2014

		if (gestioneIncipitForm.getTipoProspettazione().equals("INS")) {
			this.caricaComboMusica(gestioneIncipitForm);
		} else if (gestioneIncipitForm.getTipoProspettazione().equals("AGG")) {
			this.caricaComboMusica(gestioneIncipitForm);
		} else if (gestioneIncipitForm.getTipoProspettazione().equals("DET")) {
			this.caricaComboMusica(gestioneIncipitForm);
		}

		return mapping.getInputForward();
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneIncipitForm gestioneIncipitForm = (GestioneIncipitForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		// Inizio modifica almaviva2 03.05.2010 - BUG MANTIS 3729 inseriti controlli di lunghezza dei campi prima mancanti
		if (gestioneIncipitForm.getDettaglioIncipitVO().getNumComposizione() != null) {
			if (gestioneIncipitForm.getDettaglioIncipitVO().getNumComposizione().length() > 2) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo", "Attenzione: il N. Composizione deve essere al massimo di due caratteri"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		if (gestioneIncipitForm.getDettaglioIncipitVO().getNumMovimento() != null) {
			if (gestioneIncipitForm.getDettaglioIncipitVO().getNumMovimento().length() > 2) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo", "Attenzione: il N. Movimento deve essere al massimo di due caratteri"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		if (gestioneIncipitForm.getDettaglioIncipitVO().getNumProgDocumento() != null) {
			if (gestioneIncipitForm.getDettaglioIncipitVO().getNumProgDocumento().length() > 2) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo", "Attenzione: il N. Progressivo deve essere al massimo di due caratteri"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		// Manutenzione Settembre 2016 Bug esercizio 0006270 almaviva2: Mancato salvataggio strumento per incipit
		// Viene inserita la valorizzazione del campo Strumento anche nel caso in cui il numero delgli strumenti
		// non sia valorizzato (prima si faceva solo per maggiore di 0)
		// NUOVO CONTROLLO SU LUNGHEZZA CAMPO CHIAVE
		if (gestioneIncipitForm.getDettaglioIncipitVO().getChiave() != null) {
			if (gestioneIncipitForm.getDettaglioIncipitVO().getChiave().length() > 3) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo", "Attenzione: la Chiave deve essere al massimo di tre caratteri"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}


		// Inizio Intervento Febbraio 2014: il campo Voce/Strumento presente nell'incipit puo contenere anche il numero
        // di Voci/Strumento quindi deve essere gestita anche la parte numerica da inserire negli appositi campi di Db;
		if (gestioneIncipitForm.getVoceStrumentoNum() != null &&
				!gestioneIncipitForm.getVoceStrumentoNum().equals("")) {
			gestioneIncipitForm.getDettaglioIncipitVO().setVoceStrumentoSelez(
						gestioneIncipitForm.getVoceStrumento() + gestioneIncipitForm.getVoceStrumentoNum());
		} else {
    		// Manutenzione Settembre 2016 Bug esercizio 0006270 almaviva2: Mancato salvataggio strumento per incipit
    		// Viene inserita la valorizzazione del campo Strumento anche nel caso in cui il numero delgli strumenti
    		// non sia valorizzato (prima si faceva solo per maggiore di 0)
			gestioneIncipitForm.getDettaglioIncipitVO().setVoceStrumentoSelez(gestioneIncipitForm.getVoceStrumento());
	}
		// Fine Intervento Febbraio 2014

		// Fine modifica almaviva2 03.05.2010 - BUG MANTIS 3729 inseriti controlli di lunghezza dei campi prima mancanti
		request.setAttribute("dettaglioIncipit", gestioneIncipitForm.getDettaglioIncipitVO());
		request.setAttribute("progrDettaglioIncipit", gestioneIncipitForm.getProgrDettIncipit());

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	private void caricaComboMusica(GestioneIncipitForm gestioneIncipitForm)
			throws RemoteException, CreateException, NamingException {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		gestioneIncipitForm.setListaFormaMusicale(carCombo
				.loadComboCodiciDesc(factory.getCodici().getCodiceFormaMusicale()));
		gestioneIncipitForm.setListaTonalita(carCombo
				.loadComboCodiciDesc(factory.getCodici().getCodiceTonalita()));
		gestioneIncipitForm.setListaVoceStrumento(carCombo
				.loadComboCodiciDesc(factory.getCodici().getCodiceStrumentiVociOrganico()));

	}


}
