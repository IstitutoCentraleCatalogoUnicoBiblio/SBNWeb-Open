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
package it.iccu.sbn.web.actions.servizi.documenti;

import static it.iccu.sbn.web.vo.SbnErrorTypes.ERROR_GENERIC_FIELD_TOO_LONG;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO.TipoSIF;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.z3950.Z3950ClientFactory;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator.Sezioni;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.actionforms.servizi.documenti.GestioneDocumentoForm;
import it.iccu.sbn.web.actionforms.servizi.documenti.RicercaDocumentoForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class GestioneDocumentoAction extends ServiziBaseAction implements
		SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(GestioneDocumentoAction.class);

	private static final String[] BOTTONIERA_CREA = new String[] {
			"servizi.bottone.ok", "servizi.bottone.annulla" };
	private static final String[] BOTTONIERA_ESAMINA = new String[] { "servizi.bottone.annulla" };
	private static final String[] BOTTONIERA_GESTIONE = new String[] {
			"servizi.bottone.ok", "servizi.bottone.cancella",
			"servizi.bottone.attiva",
			"servizi.bottone.disattiva", "documentofisico.bottone.disponibilita",
			"servizi.bottone.annulla"};

	private static final String[] BOTTONIERA_SIF1 = new String[] { "servizi.bottone.scegli",
	"servizi.bottone.annulla"};

	private static final String[] BOTTONIERA_CONFERMA = new String[] {
			"servizi.bottone.si", "servizi.bottone.no",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_SIF = new String[] {
			"servizi.bottone.scegli",
			"servizi.bottone.ok",
			"documentofisico.bottone.disponibilita",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_CREA_OPAC = new String[] {
			"servizi.bottone.scegli", "servizi.bottone.annulla" };

	private static final Comparator<BibliotecaVO> ORDINA_PER_PRIORITA_BIB = new Comparator<BibliotecaVO>() {
		public int compare(BibliotecaVO b1, BibliotecaVO b2) {
			//i valori a zero vanno in fondo
			int p1 = b1.getPriorita() == 0 ? Integer.MAX_VALUE : b1.getPriorita();
			int p2 = b2.getPriorita() == 0 ? Integer.MAX_VALUE : b2.getPriorita();
			return p1 - p2;
		}
	};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.cancella", "cancella");
		map.put("servizi.bottone.aggiungi", "aggiungi");
		map.put("servizi.bottone.attiva", "attiva");
		map.put("servizi.bottone.disattiva", "disattiva");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("servizi.bottone.annulla", "annulla");

		map.put("servizi.bottone.scegli", "scegli");
		map.put("servizi.bottone.scorriAvanti","scorriAvanti");
		map.put("servizi.bottone.scorriIndietro","scorriIndietro");

		map.put("documentofisico.bottone.disponibilita", "disponibilita");

		return map;
	}

	private EsemplareDocumentoNonSbnVO getCodSelezionato(ActionForm form) {

		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		String codSelezionato = currentForm.getCodSelezionato();

		if (ValidazioneDati.strIsNull(codSelezionato))
			return null;

		List<EsemplareDocumentoNonSbnVO> esemplari = currentForm.getDettaglio().getEsemplari();
		for (EsemplareDocumentoNonSbnVO esempl : esemplari)
			if (esempl.getProgr() == Integer.parseInt(codSelezionato))
				return esempl;

		return null;
	}

	protected void init(HttpServletRequest request, ActionForm form) throws Exception {

		log.debug("GestioneDocumentoAction::init");
		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;

		ParametriServizi parametri = ParametriServizi.retrieve(request);
		currentForm.setParametri(parametri);

		ModalitaGestioneType modalita =
			(ModalitaGestioneType) parametri.get(ParamType.MODALITA_GESTIONE_DOCUMENTO);
		currentForm.setModalita(modalita);

		switch (modalita) {
		case CANCELLA:
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
			break;
		case CREA:
		case CREA_SIF:
			currentForm.setListaPulsanti(BOTTONIERA_CREA);
			break;
		case CREA_OPAC:
			currentForm.setListaPulsanti(BOTTONIERA_CREA_OPAC);
			break;
		case ESAMINA:
			currentForm.setListaPulsanti(BOTTONIERA_ESAMINA);
			break;
		case GESTIONE:
			currentForm.setListaPulsanti(BOTTONIERA_GESTIONE);
			break;
		case GESTIONE_SIF:
			currentForm.setListaPulsanti(BOTTONIERA_SIF);
			break;
		case GESTIONE_SIF_BATCH:
			currentForm.setListaPulsanti(BOTTONIERA_SIF1);
			break;
		}
		DocumentoNonSbnVO dettaglio = null;
		List<DocumentoNonSbnVO> docSelezionati = (List<DocumentoNonSbnVO>) request.getAttribute(ServiziDelegate.DOCUMENTI_SELEZIONATI);
		if (ValidazioneDati.isFilled(docSelezionati) ) {
			currentForm.setSelectedDoc(docSelezionati);
			currentForm.setNumDoc(currentForm.getSelectedDoc().size());
			dettaglio = currentForm.getSelectedDoc().get(0);
			if (currentForm.getPosizioneScorrimento() > 0)
				dettaglio = currentForm.getSelectedDoc().get(currentForm.getPosizioneScorrimento());
		}
		else
			dettaglio = (DocumentoNonSbnVO) parametri.get(ParamType.DETTAGLIO_DOCUMENTO);

		currentForm.setDettaglio(dettaglio);
		currentForm.setDettaglioOld((DocumentoNonSbnVO) dettaglio.clone());

		List<EsemplareDocumentoNonSbnVO> esemplari = dettaglio.getEsemplari();
		if (ValidazioneDati.size(esemplari) == 1)
			currentForm.setCodSelezionato("1");

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		List<TB_CODICI> codiciTipoDoc = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DOCUMENTO_LETTORE, true, "S");
		codiciTipoDoc = CaricamentoCombo.cutFirst(codiciTipoDoc);
		CaricamentoCombo combo = new CaricamentoCombo();
		currentForm.setListaTipoDocumento(combo.loadCodiceDesc(codiciTipoDoc));

		currentForm.setListaTipoFonte(delegate.getComboCodici(CodiciType.CODICE_FONTE_DOCUMENTO_LETTORE));
		currentForm.setListaTipoCodFruizione(delegate.getComboCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE));
		currentForm.setListaTipoCodNoDisp(delegate.getComboCodici(CodiciType.CODICE_NON_DISPONIBILITA));
		currentForm.setListaPaesi(delegate.getComboCodici(CodiciType.CODICE_PAESE));
		currentForm.setListaLingue(delegate.getComboCodici(CodiciType.CODICE_LINGUA));
		currentForm.setListaNature(CodiciProvider.getCodici(CodiciType.CODICE_NATURA_ORDINE, true, "C"));	//escludi collane

		//almaviva5_20141125 servizi ill
		currentForm.setListaTipoNumeroStd(Z3950ClientFactory.getCodiciNumeroStandardZ3950());

		SIFListaDocumentiNonSbnVO sif = (SIFListaDocumentiNonSbnVO) parametri.get(ParamType.PARAMETRI_SIF_DOCNONSBN);
		if (sif != null && sif.getTipoSIF() == TipoSIF.DOCUMENTO_ALTRA_BIB) {
			currentForm.setTipoServizio(sif.getTipoServizio());
		}

		List<TB_CODICI> listaTipoRecord = CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC, true);
		currentForm.setListaTipoRecord(listaTipoRecord);
		//listaTipoRecord = CaricamentoCombo.cutFirst(listaTipoRecord);

		currentForm.setInitialized(true);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		if (!currentForm.isInitialized())
			init(request, form);

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//almaviva5_20110415 #4347
		request.setAttribute(NavigazioneServizi.CHIUDI, NavigazioneServizi.CHIUDI_DETTAGLIO_DOCUMENTO);

		Navigation navi = Navigation.getInstance(request);
		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		Timestamp tsVar = currentForm.getDettaglio().getTsVar();
		if (tsVar != null && tsVar.after(currentForm.getCreationTime())) {//modificato
			request.setAttribute(NavigazioneServizi.OGGETTO_MODIFICATO, true);
			return navi.goBack();
		}

		return navi.goBack(true);
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;

		try {
			int attualePosizione = currentForm.getPosizioneScorrimento() + 1;
			int dimensione = currentForm.getSelectedDoc().size();
			if (attualePosizione > dimensione - 1) {


				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreScorriAvanti"));


			} else {
				currentForm.setDettaglio(currentForm.getSelectedDoc().get(attualePosizione));
				currentForm.setPosizioneScorrimento(attualePosizione);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;

		try {
			int attualePosizione = currentForm.getPosizioneScorrimento() - 1;
			if (attualePosizione < 0) {


				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreScorriIndietro"));


			} else {
				currentForm.setDettaglio(currentForm.getSelectedDoc().get(attualePosizione));
				currentForm.setPosizioneScorrimento(attualePosizione);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward aggiungi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		List<EsemplareDocumentoNonSbnVO> esemplari = currentForm.getDettaglio()
				.getEsemplari();

		EsemplareDocumentoNonSbnVO esemplare = new EsemplareDocumentoNonSbnVO(currentForm.getDettaglio());
		esemplare.setFonte('B');
		esemplari.add(esemplare);

		int progr = esemplari.size();
		esemplare.setProgr(progr);
		currentForm.setCodSelezionato(progr + "");

		return mapping.getInputForward();

	}

	public ActionForward attiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsemplareDocumentoNonSbnVO esemplare = getCodSelezionato(form);

		if (esemplare == null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));

			return mapping.getInputForward();
		}

		if (!esemplare.isCancellato()) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.noFunz"));

			return mapping.getInputForward();
		}

		esemplare.setFlCanc("N");

		return mapping.getInputForward();
	}

	public ActionForward disattiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			EsemplareDocumentoNonSbnVO esemplare = getCodSelezionato(form);

			if (esemplare == null) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.codiceNessunaSelezione"));

				return mapping.getInputForward();
			}

			if (esemplare.isCancellato()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.noFunz"));

				return mapping.getInputForward();
			}

			GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
			List<EsemplareDocumentoNonSbnVO> esemplari = currentForm
					.getDettaglio().getEsemplari();

			if (esemplare.isNuovo())
				esemplari.remove(esemplare);
			else {
				checkMovimentoAttivoEsemplare(request, esemplare);
				esemplare.setFlCanc("S");
			}

			return mapping.getInputForward();

		} catch (ValidationException e) {
			return mapping.getInputForward();

		}
	}

	private void checkMovimentoAttivoEsemplare(HttpServletRequest request,
			EsemplareDocumentoNonSbnVO esemplare) throws ValidationException {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		MovimentoListaVO movimento = new MovimentoListaVO();
		movimento.setCodPolo(esemplare.getCodPolo());
		movimento.setCodBibDocLett(esemplare.getCodBib());
		movimento.setTipoDocLett(String.valueOf(esemplare.getTipo_doc_lett()));
		movimento.setCodDocLet(esemplare.getCod_doc_lett() + "");
		movimento.setProgrEsempDocLet(esemplare.getPrg_esemplare() + "");

		try {
			if (!delegate.esisteMovimentoAttivo(movimento))
				return;


			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.documenti.mov.attivo.esemplare", esemplare
							.getProgr()));

			throw new ValidationException("errore");

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new ValidationException((Exception) e.detail);
		}

	}

	private void checkMovimentoAttivoDocumento(HttpServletRequest request,
			DocumentoNonSbnVO documento) throws Exception {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		try {
			if (delegate.documentoCancellabile(documento))
				return;

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.documenti.mov.attivo.documento", documento
							.getProgr()));

			throw new ValidationException("errore");

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new ValidationException((Exception) e.detail);
		}
	}

	public ActionForward disponibilita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();


		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;

		EsemplareDocumentoNonSbnVO esemplare = getCodSelezionato(form);
		if (esemplare == null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));

			return mapping.getInputForward();
		}

		if (esemplare.isNuovo()) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.documenti.esemplare.non.salvato"));

			return mapping.getInputForward();
		}

		DocumentoNonSbnVO documento = currentForm.getDettaglio();

		MovimentoVO movimento = new MovimentoRicercaVO();
		movimento.setCodPolo(documento.getCodPolo());
		movimento.setCodBibDocLett(documento.getCodBib());
		movimento.setTipoDocLett(String.valueOf(documento.getTipo_doc_lett()));
		movimento.setCodDocLet(documento.getCod_doc_lett() + "");
		movimento.setCodBibOperante(currentForm.getBiblioteca());
		movimento.setProgrEsempDocLet(esemplare.getPrg_esemplare() + "");
		request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, movimento);
		InfoDocumentoVO infoDoc = new InfoDocumentoVO();
		infoDoc.setDocumentoNonSbnVO(documento);
		request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, infoDoc);
		request.setAttribute("TipoRicerca", RicercaRichiesteType.RICERCA_PER_SEGNATURA);

		return Navigation.getInstance(request).goForward(mapping.findForward("SIFServizi"));
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;

		try {
			DocumentoNonSbnVO dettaglio = currentForm.getDettaglio();

			checkMovimentoAttivoDocumento(request, dettaglio);

			currentForm.setDettaglioOld((DocumentoNonSbnVO) dettaglio.copy());
			Iterator<EsemplareDocumentoNonSbnVO> iterator = dettaglio
					.getEsemplari().iterator();
			while (iterator.hasNext()) {
				EsemplareDocumentoNonSbnVO esemplare = iterator.next();
				if (esemplare.isNuovo())
					iterator.remove();
				esemplare.setFlCanc("S");
			}

			dettaglio.setFlCanc("S");

			currentForm.setConferma(true);
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.CANCELLA);

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.confermaOperazioneCan"));

			return mapping.getInputForward();

		} catch (ValidationException e) {
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();

	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		TipoOperazione op = currentForm.getOperazione();
		switch (op) {
		case SALVA:
		case SALVA_E_SCEGLI:
			return eseguiAggiornamento(mapping, form, request, response);

		case CANCELLA:
			return eseguiCancellazione(mapping, form, request, response);
		}

		return no(mapping, currentForm, request, response);
	}

	private ActionForward eseguiAggiornamento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {


		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		DocumentoNonSbnVO dettaglio = currentForm.getDettaglio();

		try {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			ParametriServizi parametri = currentForm.getParametri();
			ModalitaCercaType modalitaCerca = (ModalitaCercaType) parametri
					.get(ParamType.MODALITA_CERCA_DOCUMENTO);
			ModalitaGestioneType modalita = (ModalitaGestioneType) parametri
					.get(ParamType.MODALITA_GESTIONE_DOCUMENTO);

			//almaviva5_20100322 #3647 check categoria fruizione su doc modificato
			//da erogazione servizi (solo se non impostata)
			if (!ValidazioneDati.isFilled(dettaglio.getCodFruizione())) {
				switch (modalita) {
				case CREA_SIF:
				case GESTIONE_SIF:
					String catFrui = delegate.getCategoriaFruizioneSegnatura(dettaglio);
					if (!ValidazioneDati.isFilled(catFrui)) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.segnature.richiestoCodiceFruizione"));

						return mapping.getInputForward();
					}
				}
			}

			DocumentoNonSbnVO doc = delegate.aggiornaDocumentoNonSbn(ValidazioneDati.asSingletonList(dettaglio)).get(0);

			List<EsemplareDocumentoNonSbnVO> esemplari = doc.getEsemplari();
			if (ValidazioneDati.size(esemplari) == 1)
				currentForm.setCodSelezionato("1");

			doc = new DocumentoNonSbnDecorator(doc);
			currentForm.setDettaglio(doc);
			currentForm.setDettaglioOld((DocumentoNonSbnVO) doc.clone());

			currentForm.setConferma(false);

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));


			// sono in modalità sif?
			if ((modalita == ModalitaGestioneType.CREA ||
				modalita == ModalitaGestioneType.CREA_SIF ||
				modalita == ModalitaGestioneType.GESTIONE_SIF) &&
				modalitaCerca == ModalitaCercaType.CERCA_PER_EROGAZIONE) {

				// modalita sif
				currentForm.setModalita(ModalitaGestioneType.GESTIONE_SIF);
				currentForm.setListaPulsanti(BOTTONIERA_SIF);
			} else {
				// modalità gestione
				currentForm.setModalita(ModalitaGestioneType.GESTIONE);
				currentForm.setListaPulsanti(BOTTONIERA_GESTIONE);
			}

			//almaviva5_20100322 #3647
			if (currentForm.getOperazione() == TipoOperazione.SALVA_E_SCEGLI)
				return scegli(mapping, currentForm, request, response);
			else
				return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e
					.getErrorCode().getErrorMessage()));
			//return mapping.getInputForward();
			return no(mapping, currentForm, request, response);

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e
					.getErrorCode().getErrorMessage()));

			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);
		}

		return mapping.getInputForward();
	}


	private ActionForward eseguiCancellazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {



		try {
			GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DocumentoNonSbnVO dettaglio = currentForm.getDettaglio();
			delegate.aggiornaDocumentoNonSbn(ValidazioneDati.asSingletonList(dettaglio));

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));


			return Navigation.getInstance(request).goBack(true);

		} catch (ValidationException e) {
			return mapping.getInputForward();

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e
					.getErrorCode().getErrorMessage()));


		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);
		}

		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		currentForm.setConferma(false);

		//currentForm.setDettaglio((DocumentoNonSbnVO) currentForm.getDettaglioOld().clone());
		//currentForm.setDettaglioOld((DocumentoNonSbnVO) currentForm.getDettaglio().clone());

		ModalitaGestioneType modalita = currentForm.getModalita();
		switch (modalita) {
		case CANCELLA:
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
			break;
		case CREA:
		case CREA_SIF:
			currentForm.setListaPulsanti(BOTTONIERA_CREA);
			break;
		case CREA_OPAC:
			currentForm.setListaPulsanti(BOTTONIERA_CREA_OPAC);
			break;
		case ESAMINA:
			currentForm.setListaPulsanti(BOTTONIERA_ESAMINA);
			break;
		case GESTIONE:
			currentForm.setListaPulsanti(BOTTONIERA_GESTIONE);
			break;
		case GESTIONE_SIF:
			currentForm.setListaPulsanti(BOTTONIERA_SIF);
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		DocumentoNonSbnVO dettaglio = currentForm.getDettaglio();

		if (dettaglio.isPosseduto()) {
			EsemplareDocumentoNonSbnVO esemplare = getCodSelezionato(form);

			if (esemplare == null) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.codiceNessunaSelezione"));

				return mapping.getInputForward();
			}

			if (esemplare.isNuovo()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.documenti.esemplare.non.salvato"));

				return mapping.getInputForward();
			}

			if (esemplare.isCancellato()
					|| ValidazioneDati.isFilled(esemplare.getCodNoDisp())) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.documenti.esemplare.nodisp", esemplare.getProgr()));

				return mapping.getInputForward();
			}

			List<EsemplareDocumentoNonSbnVO> esemplareSif = new ArrayList<EsemplareDocumentoNonSbnVO>();
			esemplareSif.add(esemplare);
			dettaglio.setEsemplari(esemplareSif);
		}

		//check modifica dati documento
		if (!currentForm.getDettaglioOld().equals(dettaglio)) {

			try {
				checkDettaglio(request, dettaglio);
			} catch (ValidationException e) {
				if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
					LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();
			}

			currentForm.setConferma(true);
			if (dettaglio.isDocumentoILL() && !ValidazioneDati.isFilled(dettaglio.getBiblioteche()))
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.conferma.doc.opac.senza.bib.ill"));
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.SALVA_E_SCEGLI);
			return mapping.getInputForward();
		}

		//almaviva5_20160524 servizi ILL
		List<BibliotecaVO> biblioteche = dettaglio.getBiblioteche();
		if (dettaglio.isDocumentoILL() && ValidazioneDati.isFilled(biblioteche)) {
			int size = ValidazioneDati.size(ServiziUtil.controllaPrioritaBibliotecheILL(biblioteche, Constants.MAX_BIBLIOTECHE_RICHIESTA_ILL));
			if (size == 0) {
				LinkableTagUtils.addError(request, new ValidationException(SbnErrorTypes.SRV_ILL_BIBLIOTECA_FORNITRICE_NON_IMPOSTATA));
				return mapping.getInputForward();
			}
		}

		ParametriServizi parametri = currentForm.getParametri();
		ModalitaGestioneType modalita = (ModalitaGestioneType) parametri
			.get(ParamType.MODALITA_GESTIONE_DOCUMENTO);

		try {
			//almaviva5_20100322 #3647 check categoria fruizione su doc modificato
			//da erogazione servizi (solo se non impostata)
			if (!ValidazioneDati.isFilled(dettaglio.getCodFruizione())) {
				switch (modalita) {
				case CREA_SIF:
				case GESTIONE_SIF:
					ServiziDelegate delegate = ServiziDelegate.getInstance(request);
					String catFrui = delegate.getCategoriaFruizioneSegnatura(dettaglio);
					if (!ValidazioneDati.isFilled(catFrui)) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.segnature.richiestoCodiceFruizione"));

						return mapping.getInputForward();
					}
				case GESTIONE_SIF_BATCH:
					ServiziDelegate delegate1 = ServiziDelegate.getInstance(request);
					String catFrui1 = delegate1.getCategoriaFruizioneSegnatura(dettaglio);
					if (!ValidazioneDati.isFilled(catFrui1)) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.segnature.richiestoCodiceFruizione"));

						return mapping.getInputForward();
					}
				}
			}
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage()));

			return mapping.getInputForward();
		}

		// recupero i parametri per questo sif
		SIFListaDocumentiNonSbnVO sif = (SIFListaDocumentiNonSbnVO) parametri.get(ParamType.PARAMETRI_SIF_DOCNONSBN);
		if (sif == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.generico"));

			return mapping.getInputForward();
		}

		sif.setDocumento(dettaglio);

		// porto l'esemplare scelto alla maschera di erogazione servizi
		String requestAttribute = sif.getRequestAttribute();
		request.setAttribute(requestAttribute, sif);
		if (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
			return mapping.findForward("stampaServizi");
		}

		// torno al bookmark impostato dalla mappa di erogazione
		return Navigation.getInstance(request).goToBookmark(requestAttribute, false);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		DocumentoNonSbnVO dettaglio = currentForm.getDettaglio();

		try {
			checkDettaglio(request, dettaglio);
			checkEsemplari(request, dettaglio.getEsemplari());

			if (currentForm.getDettaglioOld().equals(dettaglio)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noAggiornaNoVariazioni"));

				resetToken(request);
				return mapping.getInputForward();
			}

			currentForm.setConferma(true);
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);

			TipoOperazione operazione = TipoOperazione.SALVA;
			//almaviva5_20171002 scelta automatica nuovo doc. per ILL
			SIFListaDocumentiNonSbnVO sif = (SIFListaDocumentiNonSbnVO) currentForm.getParametri().get(ParamType.PARAMETRI_SIF_DOCNONSBN);
			if (sif != null && sif.getTipoSIF() == TipoSIF.DOCUMENTO_ALTRA_BIB) {
				operazione = TipoOperazione.SALVA_E_SCEGLI;
			}

			currentForm.setOperazione(operazione);

			// se posseduto, imposto come fonte il Bibliotecario
			if (dettaglio.getTipo_doc_lett() != 'D')
				dettaglio.setFonte('B');

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (ValidationException e) {
			if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	private void checkEsemplari(HttpServletRequest request,
			List<EsemplareDocumentoNonSbnVO> esemplari)
			throws ValidationException {

		if (!ValidazioneDati.isFilled(esemplari))
			return;

		for (EsemplareDocumentoNonSbnVO esemplare : esemplari) {

			if (ValidazioneDati.strIsNull(esemplare.getInventario()))
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.documenti.inventarioObbligatorio"));

/*			String annata = esemplare.getAnnata();
			if (ValidazioneDati.isFilled(annata)
					&& !ValidazioneDati.strIsNumeric(annata) )
			{
				LinkableTagUtils.addError(request, new ActionMessage(
				"errors.servizi.documenti.annataDataErrata"));
			}
			else
			{
				// tck 3944
				annata="01/01/"+ annata;
				if (ValidazioneDati.isFilled(annata)
						&& ValidazioneDati.validaData(annata) != ValidazioneDati.DATA_OK )
				{
					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.documenti.annataDataErrata"));
				}

			}*/

			if (ValidazioneDati.isFilled(esemplare.getCodNoDisp())
					&& !esemplare.isNuovo())
				checkMovimentoAttivoEsemplare(request, esemplare);

			if (LinkableTagUtils.getErrors(request).size() > 0) {

				throw new ValidationException("errore");
			}
		}

	}

	private void checkDettaglio(HttpServletRequest request,
			DocumentoNonSbnVO dettaglio) throws ValidationException {

		//almaviva5_20160201 servizi ILL
		if (dettaglio.getTipo_doc_lett() == 'P')
			if (ValidazioneDati.strIsNull(dettaglio.getSegnatura()))
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.documenti.segnaturaObbligatorio"));

		if (ValidazioneDati.strIsNull(dettaglio.getTitolo()))
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.documenti.titoloObbligatorio"));

		if (!ValidazioneDati.isFilled(dettaglio.getNatura()))
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.documenti.naturaObbligatorio"));

		if (!ValidazioneDati.isFilled(dettaglio.getFonte()))
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.documenti.fonteObbligatorio"));

		//almaviva5_20160405
		if (ValidazioneDati.length(dettaglio.getTitolo()) > 240)
			LinkableTagUtils.addError(request, new ValidationException(ERROR_GENERIC_FIELD_TOO_LONG, "servizi.documenti.titolo", "240"));
		if (ValidazioneDati.length(dettaglio.getEditore()) > 50)
			LinkableTagUtils.addError(request, new ValidationException(ERROR_GENERIC_FIELD_TOO_LONG, "servizi.documenti.editore", "50"));
		if (ValidazioneDati.length(dettaglio.getAutore()) > 160)
			LinkableTagUtils.addError(request, new ValidationException(ERROR_GENERIC_FIELD_TOO_LONG, "servizi.documenti.autore", "160"));
		if (ValidazioneDati.length(dettaglio.getLuogoEdizione()) > 30)
			LinkableTagUtils.addError(request, new ValidationException(ERROR_GENERIC_FIELD_TOO_LONG, "servizi.documenti.luogo", "30"));
		if (ValidazioneDati.length(dettaglio.getSegnatura()) > 40)
			LinkableTagUtils.addError(request, new ValidationException(ERROR_GENERIC_FIELD_TOO_LONG, "servizi.documenti.segnatura", "40"));

		//almaviva5_20141125 servizi ill
		boolean tipNumStd = ValidazioneDati.isFilled(dettaglio.getTipoNumStd());
		boolean numeroStd = ValidazioneDati.isFilled(dettaglio.getNumeroStd());
		if ( (tipNumStd && !numeroStd) || (!tipNumStd && numeroStd) )
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.documenti.numeroStdIncompleto"));

		//check priorità biblioteche
		List<BibliotecaVO> biblioteche = dettaglio.getBiblioteche();
		if (ValidazioneDati.isFilled(biblioteche)) {
			ServiziUtil.controllaPrioritaBibliotecheILL(biblioteche, Constants.MAX_BIBLIOTECHE_RICHIESTA_ILL);
			Collections.sort(biblioteche, ORDINA_PER_PRIORITA_BIB);
		}

		if (dettaglio.getTipo_doc_lett() == 'D') {
			if (!ValidazioneDati.isFilled(dettaglio.getTipoRecord()))
				LinkableTagUtils.addError(request, new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.documenti.tipoRecord"));
		}

		if (LinkableTagUtils.getErrors(request).size() > 0) {
			throw new ValidationException("errore");
		}

		String annata = dettaglio.getAnnata();
		if (ValidazioneDati.isFilled(annata) && !ValidazioneDati.strIsNumeric(annata)) {
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_NUMERIC_NOT_MANDATORY_FIELD, "Annata");
		}

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		GestioneDocumentoForm currentForm = (GestioneDocumentoForm) form;
		try {
			//almaviva5_20110415 #4347
			Navigation navi = Navigation.getInstance(request);
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.DETTAGLIO_MOVIMENTO))
				return navi.bookmarksExist(Bookmark.Servizi.DETTAGLIO_MOVIMENTO);

			if (ValidazioneDati.equals(idCheck, "servizi.bottone.scegli"))
				return !navi.bookmarksExist(Bookmark.Servizi.DETTAGLIO_MOVIMENTO);

			if (ValidazioneDati.equals(idCheck, "POSSEDUTO")) {
				return currentForm.getDettaglio().isPosseduto();
			}

			if (ValidazioneDati.in(idCheck, Sezioni.DOC_DATI_SPOGLIO))
				return navi.bookmarksExist(
						Bookmark.Servizi.LISTA_MOVIMENTI,
						Bookmark.Servizi.DETTAGLIO_MOVIMENTO);

			if (ValidazioneDati.in(idCheck, Sezioni.DOC_BIBLIOTECHE)) {
				return ValidazioneDati.isFilled(currentForm.getDettaglio().getBiblioteche()) &&
					!navi.bookmarksExist(Bookmark.Servizi.DETTAGLIO_MOVIMENTO);
			}

			//almaviva5_20160208 servizi ILL
			if (ValidazioneDati.in(idCheck,
					"servizi.bottone.attiva",
					"servizi.bottone.disattiva",
					"documentofisico.bottone.disponibilita")) {
				DocumentoNonSbnVO doc = currentForm.getDettaglio();
				return doc.isPosseduto();
			}
			//

			if (ValidazioneDati.equals(idCheck, "SEGNATURA")) {
				DocumentoNonSbnVO doc = currentForm.getDettaglio();
				return (doc.isDocumentoILL() || !ValidazioneDati.in(currentForm.getModalita(),
								ModalitaGestioneType.GESTIONE_SIF,
								ModalitaGestioneType.CREA_SIF));

			}

			ModalitaGestioneType modalita = ModalitaGestioneType.valueOf(idCheck);
			switch (modalita) {
			case CREA:
				return !ValidazioneDati.in(currentForm.getModalita(),
					ModalitaGestioneType.CREA,
					ModalitaGestioneType.CREA_SIF);
			}

		} catch (IllegalArgumentException e) {
			return true;
		} catch (Exception e) {
			log.error(e);
		}

		return true;
	}

}
