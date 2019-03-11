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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTitoloTermineType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ThesauroFolder extends Action implements SemanticaFolder {

	private static Logger log = Logger.getLogger(ThesauroFolder.class);

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private CatSemThesauroVO getThesauro(ActionForm form) {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneThesauro();
	}

	private ElementoSinteticaThesauroVO getElementoSelezionato(String did, ActionForm form) {
		CatSemThesauroVO thesauro = getThesauro(form);

		for (Object o : thesauro.getListaTermini() ) {
			ElementoSinteticaThesauroVO termine = (ElementoSinteticaThesauroVO) o;
			if (termine.getDid().equals(did))
				return termine;
		}

		return null;
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("no impl");
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		CatSemThesauroVO thesauro = getThesauro(form);

		if (thesauro.getNumBlocco() > thesauro.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (thesauro.getBlocchiCaricati().contains(thesauro.getNumBlocco()))
			return mapping.getInputForward();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

		RicercaThesauroListaVO areaDatiPassReturn = delegate.nextBlocco(
				thesauro.getIdLista(), thesauro.getNumBlocco(), thesauro
						.getMaxRighe());

		if (areaDatiPassReturn == null)
			return mapping.getInputForward();

		thesauro.getBlocchiCaricati().add(thesauro.getNumBlocco());

		List<ElementoSinteticaThesauroVO> sintetica = thesauro
				.getListaTermini();
		sintetica.addAll(areaDatiPassReturn.getRisultati());
		Collections.sort(sintetica,
				ElementoSinteticaThesauroVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA, "P");
		request.setAttribute(NavigazioneSemantica.BID_RIFERIMENTO, currentForm.getCatalogazioneSemanticaComune()
				.getBid());
		return mapping.findForward("analiticaTitolo");
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CatSemThesauroVO thesauro = getThesauro(form);

		ParametriThesauro parametri = thesauro.getParametri().copy();
		parametri.put(ParamType.DATI_BIBLIOGRAFICI,
				currentForm.getAreaDatiPassBiblioSemanticaVO());

		parametri.put(ParamType.MODALITA_CERCA_TERMINE,
				ModalitaCercaType.CREA_LEGAME_TITOLO);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(
				mapping.findForward("inserisciLegameTitoloTermine"));
	}


	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CatSemThesauroVO thesauro = getThesauro(form);

		String codSelezionato = thesauro.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
				codSelezionato);
		if (analitica == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio =
			(DettaglioTermineThesauroVO) analitica.getReticolo().getDettaglio();

		if (dettaglio.getNumTitoliBiblio() < 1) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.cancella.legame.noloc"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		}

		if (!delegate.isThesauroGestito(dettaglio.getCodThesauro()) ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		}

		ParametriThesauro parametri = thesauro.getParametri().copy();
		parametri.put(ParamType.DATI_BIBLIOGRAFICI,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		parametri.put(ParamType.MODALITA_LEGAME_TITOLO_TERMINE,
				ModalitaLegameTitoloTermineType.CANCELLA);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(
				mapping.findForward("gestioneLegameTitoloTermine"));
	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatSemThesauroVO thesauro = getThesauro(form);
		ActionMessages errors = new ActionMessages();
		if (thesauro.getDidMultiSelez() == null
				&& thesauro.getCodSelezionato() == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.selObblOggSint"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

		if (thesauro.getDidMultiSelez() != null
				&& thesauro.getDidMultiSelez().length > 0) {
			String[] listaDidSelez = thesauro.getDidMultiSelez();
			String xid = listaDidSelez[0];

			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
					xid);
			if (analitica == null)
				return mapping.getInputForward();

			resetToken(request);
			ParametriThesauro parametri = thesauro.getParametri().copy();
			parametri.put(ParamType.ANALITICA, analitica
					.getReticolo());
			parametri.put(ParamType.LISTA_DID_SELEZIONATI, listaDidSelez);
			ParametriThesauro.send(request, parametri);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaThesauro"));
		}

		if (!ValidazioneDati.strIsNull(thesauro.getCodSelezionato())) {
			String xid = thesauro.getCodSelezionato();
			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
					xid);
			if (analitica == null)
				return mapping.getInputForward();

			resetToken(request);
			ParametriThesauro parametri = thesauro.getParametri().copy();
			parametri.put(ParamType.ANALITICA, analitica
					.getReticolo());
			ParametriThesauro.send(request, parametri);
			request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, xid);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaThesauro"));

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato

			return mapping.getInputForward();
		}
	}

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		//almaviva5_20100127 #2952
		navi.setSuffissoTesto("/" + LinkableTagUtils.findMessage(request, request.getLocale(), "button.tag.thesauro"));

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		currentForm.setSessione(true);

		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		if (parametri == null)
			parametri = new ParametriThesauro();

		AreaDatiPassBiblioSemanticaVO datiBibliografici = currentForm.getAreaDatiPassBiblioSemanticaVO();
		// dopo la creazione o modifica di un legame devo aggiornare il T005 del titolo
		// in soggettazione
		String T005 = (String) parametri.get(ParamType.CATALOGAZIONE_TITOLO_T005);
		if (T005 != null) {
			TreeElementViewTitoli reticolo = datiBibliografici.getTreeElement();
			reticolo.setT005(T005);
			DettaglioTitoloParteFissaVO detTitoloPFissaVO =
					reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO()
					.getDetTitoloPFissaVO();
			detTitoloPFissaVO.setVersione(T005);
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		String bid = datiBibliografici.getBidPartenza();
		CatSemThesauroVO thesauro = delegate.ricercaTerminiPerBidCollegato(true, bid, null, 10, false);

		if (thesauro == null)
			return mapping.getInputForward();

		thesauro.setParametri(parametri);
		currentForm.getCatalogazioneSemanticaComune().setCatalogazioneThesauro(thesauro);
		List<ElementoSinteticaThesauroVO> termini = thesauro.getListaTermini();

		currentForm.setComboGestioneLegame(LabelGestioneSemantica
				.getComboGestioneSematicaPerLegame(servlet
						.getServletContext(), request, form,
						new String[] { "TH" }, this));
		currentForm.setIdFunzioneLegame("");

		// se ho un solo elemento lo seleziono
		if (termini.size() == 1)
			thesauro.setCodSelezionato(termini.get(0).getDid());

		thesauro.getBlocchiCaricati().add(1);
		currentForm.setIdLista(thesauro.getIdLista());
		currentForm.setMaxRighe(thesauro.getMaxRighe());
		currentForm.setNumBlocco(thesauro.getNumBlocco());
		currentForm.setTotBlocchi(thesauro.getTotBlocchi());
		currentForm.setTotRighe(thesauro.getTotRighe());

		return mapping.getInputForward();
	}

	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CatSemThesauroVO thesauro = getThesauro(form);

		String codSelezionato = thesauro.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
				codSelezionato);
		if (analitica == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio = analitica.getReticolo()
				.getAreaDatiDettaglioOggettiVO()
				.getDettaglioTermineThesauroVO();

		ParametriThesauro parametri = thesauro.getParametri().copy();
		parametri.put(ParamType.DATI_BIBLIOGRAFICI,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		parametri.put(ParamType.MODALITA_LEGAME_TITOLO_TERMINE,
				ModalitaLegameTitoloTermineType.MODIFICA);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(
				mapping.findForward("gestioneLegameTitoloTermine"));
	}


	public ActionForward recupera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CatSemThesauroVO thesauro = getThesauro(form);

		String codSelezionato = thesauro.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ElementoSinteticaThesauroVO termine = getElementoSelezionato(codSelezionato, form);
		if (termine.getNumTitoliBiblio() > 0) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
				codSelezionato);
		if (analitica == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio = analitica.getReticolo()
				.getAreaDatiDettaglioOggettiVO()
				.getDettaglioTermineThesauroVO();

		ParametriThesauro parametri = thesauro.getParametri().copy();
		parametri.put(ParamType.DATI_BIBLIOGRAFICI,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		parametri.put(ParamType.MODALITA_LEGAME_TITOLO_TERMINE,
				ModalitaLegameTitoloTermineType.CREA);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(
				mapping.findForward("gestioneLegameTitoloTermine"));

	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneLegame();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		if (!checkAttivita(request, form, idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		GESTIONE,
		RECUPERA_LEGAME_TITOLO,
		CREA_LEGAME_TITOLO,
		MODIFICA_LEGAME_TITOLO,
		ELIMINA_LEGAME_TITOLO,
		INVIA_LEGAME_TITOLO;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
			//TreeElementViewTitoli rootTitolo = currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement();
			//String tipoMateriale = rootTitolo.getTipoMateriale();
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);

			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			Utente utente = delegate.getEJBUtente();

			switch (attivita) {
			case INVIA_LEGAME_TITOLO:
				return false;

			case GESTIONE:
				if (getThesauro(currentForm).getTotRighe() < 1)	return false;

			case ELIMINA_LEGAME_TITOLO:
			case MODIFICA_LEGAME_TITOLO:
			case RECUPERA_LEGAME_TITOLO:
				if (currentForm.isEnableSoloEsamina())
					return false;

				if (getThesauro(currentForm).getTotRighe() < 1)	return false;

				//almaviva5_20130520 #5316
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				utente.isAbilitatoLegameTitoloAuthority("TH");
				break;

			case CREA_LEGAME_TITOLO:
				if (currentForm.isEnableSoloEsamina())
					return false;

				//almaviva5_20130520 #5316
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				utente.isAbilitatoLegameTitoloAuthority("TH");
				break;

			}
		} catch (Exception e) {
			// log.error("", e);
			return false;
		}

		return true;
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

}
