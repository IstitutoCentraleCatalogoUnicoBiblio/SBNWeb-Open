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
package it.iccu.sbn.web.actions.servizi.serviziweb.sale;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.sale.ListaPrenotazioniPostiForm;
import it.iccu.sbn.web.integration.action.sale.RicercaSaleAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaPrenotazioniPostiAction extends RicercaSaleAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("servizi.utenti.prenotaPosto", "nuovo");	//definito nella superclasse
		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		ListaPrenotazioniPostiForm currentForm = (ListaPrenotazioniPostiForm) form;
		if (currentForm.isInitialized())
			return;

		//recupero bib scelta dall'utente
		HttpSession session = request.getSession();
		BibliotecaVO bib = (BibliotecaVO)session.getAttribute(Constants.BIBLIO);
		currentForm.setBiblioteca(bib.getCod_bib());

		//filtro utente
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		RicercaPrenotazionePostoVO ricercaPren = currentForm.getRicercaPrenotazioni();
		UtenteBaseVO datiUtente = ServiziDelegate.getInstance(request).getUtente(utente.getUserId());
		ricercaPren.setUtente(datiUtente);
		currentForm.getGrigliaCalendario().setUtente(datiUtente);

		super.init(request, form);

		currentForm.setCurrentFolder(FOLDER_PRENOTAZIONI);
	}

	@Override
	public ActionForward annullaPrenotazione(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ListaPrenotazioniPostiForm currentForm = (ListaPrenotazioniPostiForm) form;
		PrenotazionePostoVO pp = new PrenotazionePostoVO();
		pp.setId_prenotazione(currentForm.getSelectedPren());
		pp = SaleDelegate.getInstance(request).getDettaglioPrenotazionePosto(pp);
		if (pp == null)
			return mapping.getInputForward();

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.sale.rifiutoPrenotazionePosto",
				pp.getId_prenotazione(), pp.getPosto().getSala().getDescrizione()));
		currentForm.setConferma(true);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);

		return mapping.getInputForward(); //super.annullaPrenotazione(mapping, form, request, response);
	}

	@Override
	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		super.annullaPrenotazione(mapping, form, request, response);

		return super.no(mapping, form, request, response);
	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.annulla")) {
			return false;
		}
		return super.checkAttivita(request, form, idCheck);
	}

}
