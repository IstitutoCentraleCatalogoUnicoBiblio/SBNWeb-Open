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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator;
import it.iccu.sbn.web.actionforms.servizi.documenti.ListaDocumentiForm;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
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

public class ListaDocumentiAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(ListaDocumentiAction.class);

	private static final String[] BOTTONIERA = new String[] { "servizi.bottone.esamina", "servizi.bottone.nuovo",
		"servizi.bottone.cancella", "servizi.bottone.annulla"};

	private static final String[] BOTTONIERA_SIF = new String[] { //"servizi.bottone.doc.importa.opac",
		"servizi.bottone.scegli",
		"servizi.bottone.esamina", "servizi.bottone.nuovo",
		"servizi.bottone.annulla"};

	private static final String[] BOTTONIERA_SIF1 = new String[] { "servizi.bottone.scegli",
		"servizi.bottone.annulla"};

	private static final String[] BOTTONIERA_CONFERMA = new String[] { "servizi.bottone.si",
		"servizi.bottone.no", "servizi.bottone.annulla"};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esaminaOne", "ok");
		map.put("servizi.bottone.esamina", "esamina2");
//		map.put("servizi.bottone.esamina", "esamina");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.cancella", "cancella");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("button.blocco", "blocco");
		map.put("servizi.bottone.deselTutti", "deselTutti");
		map.put("servizi.bottone.selTutti",   "selTutti");

		//almaviva5_20100322 #3647
		map.put("servizi.bottone.scegli", "scegli");

		map.put("servizi.bottone.doc.importa.opac", "importaOpac");

		return map;
	}

	private DocumentoNonSbnVO getCodSelezionato(ActionForm form) {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		String codSelezionato = currentForm.getCodSelezionato();

		if (ValidazioneDati.strIsNull(codSelezionato))
			return null;

		return UniqueIdentifiableVO.search(Integer.parseInt(codSelezionato),
				currentForm.getDocumenti());
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		if (!currentForm.isInitialized() )
			init(request, form);

		return mapping.getInputForward();
	}

	protected void init(HttpServletRequest request, ActionForm form) {

		log.debug("ListaDocumentiAction::init");
		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		currentForm.setListaPulsanti(BOTTONIERA);
		ParametriServizi parametri = ParametriServizi.retrieve(request);
		currentForm.setParametri(parametri);

		currentForm.setFiltro((DocumentoNonSbnRicercaVO) parametri.get(ParamType.PARAMETRI_RICERCA));
		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) parametri.get(ParamType.LISTA_DOCUMENTI);
		currentForm.setBlocco(blocco1);
		currentForm.setBloccoSelezionato(1);

		if (blocco1.getTotRighe() == 1) {
			DocumentoNonSbnVO doc = (DocumentoNonSbnVO) blocco1.getLista().get(0);
			//String idDoc = doc.getIdDocumento() + "";
			String idDoc = doc.getUniqueId() + "";
			currentForm.setIdSelezionati(new String[] {idDoc} );
			currentForm.setCodSelezionato(idDoc);
		}

		//almaviva5_20091027 filtro bib metropolitane
		ModalitaCercaType modalitaCerca = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_DOCUMENTO);
		currentForm.setModalitaCerca(modalitaCerca);

		//almaviva5_20100322 #3647
		switch (modalitaCerca) {
		case CERCA:
			currentForm.setListaPulsanti(BOTTONIERA);
			break;
		case CERCA_PER_EROGAZIONE:
			currentForm.setListaPulsanti(BOTTONIERA_SIF);
			break;
		case CERCA_PER_STAMPA_SERVIZI_CORRENTI:
			currentForm.setListaPulsanti(BOTTONIERA_SIF1);
			break;
		}

		currentForm.setDocumenti(new ArrayList<DocumentoNonSbnVO>() );
		currentForm.getDocumenti().addAll(blocco1.getLista() );

		currentForm.setInitialized(true);

	}



	public ActionForward esamina2(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;

		if (!isTokenValid(request))
			saveToken(request);

		List<String> idSelezionati = getMultiBoxSelectedItems(currentForm.getIdSelezionati());
		DocumentoNonSbnVO docSelezionato = getCodSelezionato(form);

		if (docSelezionato == null && !ValidazioneDati.isFilled(idSelezionati)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		if (!ValidazioneDati.isFilled(idSelezionati)) // selezione singola
			return ok(mapping, currentForm, request, response);


		resetToken(request);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List<DocumentoNonSbnVO> arrDocSel = new ArrayList<DocumentoNonSbnVO>();
		for (String id : idSelezionati) {
			DocumentoNonSbnVO doc = UniqueIdentifiableVO.search(Integer.parseInt(id), currentForm.getDocumenti());
			try {
				DocumentoNonSbnVO dettDoc = delegate.getDettaglioDocumentoNonSbn(doc);
				if (dettDoc != null)
					arrDocSel.add(dettDoc);

			} catch (Exception e) {
				log.error("", e);
			}
		}

		request.setAttribute(ServiziDelegate.DOCUMENTI_SELEZIONATI, arrDocSel);

		ParametriServizi parametri = currentForm.getParametri().copy();

		ModalitaCercaType modalitaCerca = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_DOCUMENTO);

		if (modalitaCerca == ModalitaCercaType.CERCA){
			parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE);
		}else if (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
			parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE_SIF_BATCH);
		}else{
			parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE_SIF);
		}

		//parametri.put(DocumentoParamType.DETTAGLIO_DOCUMENTO, dettaglio);
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneDocumento"));
	}

	private void checkMovimentoAttivoDocumento(HttpServletRequest request, DocumentoNonSbnVO documento) throws Exception {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		try {
			if (delegate.documentoCancellabile(documento))
				return;

			LinkableTagUtils.addError(request,
				new ActionMessage("errors.servizi.documenti.mov.attivo.documento.multi",
					documento.getSegnatura()));
			throw new ValidationException("errore");

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new ValidationException((Exception) e.detail);
		}
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		DocumentoNonSbnVO doc = getCodSelezionato(form);

		if (doc == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));

			return mapping.getInputForward();
		}

		//almaviva5_20160405 servizi ILL
		if (doc.isNuovo() && doc.isDocumentoILL())
			//documento da opac, si passa a importazione
			return importaOpac(mapping, currentForm, request, response);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		DocumentoNonSbnVO documento = delegate.getDettaglioDocumentoNonSbn(doc);
		if (documento == null)
			return mapping.getInputForward();

		ParametriServizi parametri = currentForm.getParametri().copy();

		ModalitaCercaType modalitaCerca = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_DOCUMENTO);
		if (modalitaCerca == ModalitaCercaType.CERCA)
			parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE);
		else
			parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE_SIF);

		parametri.put(ParamType.DETTAGLIO_DOCUMENTO, new DocumentoNonSbnDecorator(documento));
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneDocumento"));
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		ParametriServizi parametri = currentForm.getParametri().copy();

		parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.CREA);
		DocumentoNonSbnRicercaVO documento = currentForm.getFiltro().copy();
		documento.setFonte('B');
		documento.setTipoRecord('a');
		parametri.put(ParamType.DETTAGLIO_DOCUMENTO, new DocumentoNonSbnDecorator(documento));
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneDocumento"));
	}

	public ActionForward importaOpac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DocumentoNonSbnVO docSelezionato = getCodSelezionato(form);
		if (docSelezionato == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
		DocumentoNonSbnVO doc = docSelezionato.copy();
		doc.setIdDocumento(0);
		doc.setCodPolo(filtro.getCodPolo());
		doc.setCodBib(filtro.getCodBib());

		ParametriServizi parametri = currentForm.getParametri().copy();

		parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.CREA_OPAC);
		parametri.put(ParamType.DETTAGLIO_DOCUMENTO, doc );
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneDocumento"));
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		List<String> items = getMultiBoxSelectedItems(currentForm.getIdSelezionati());

		if (!ValidazioneDati.isFilled(items)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		for (String id : items) {
			//DocumentoNonSbnVO d = UniqueIdentifiableVO.searchRepeatableId(new Integer(id), currentForm.getDocumenti());
			DocumentoNonSbnVO d = UniqueIdentifiableVO.search(new Integer(id), currentForm.getDocumenti());
			try {
				checkMovimentoAttivoDocumento(request, d);
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		currentForm.setOperazione(TipoOperazione.CANCELLA);
		return preparaConferma(request, mapping, currentForm, null, null);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		switch (currentForm.getOperazione()) {
		case CANCELLA:
			return eseguiCancellazioneMultipla(mapping, currentForm, request, response);
		}

		return mapping.getInputForward();
	}


	private ActionForward eseguiCancellazioneMultipla(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		List<DocumentoNonSbnVO> documenti = new ArrayList<DocumentoNonSbnVO>();
		List<String> items = getMultiBoxSelectedItems(currentForm.getIdSelezionati());

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		for (String id : items) {
			//DocumentoNonSbnVO d = UniqueIdentifiableVO.searchRepeatableId(new Integer(id), currentForm.getDocumenti()).copy();
			DocumentoNonSbnVO d = UniqueIdentifiableVO.search(new Integer(id), currentForm.getDocumenti()).copy();
			DocumentoNonSbnVO dettaglio = delegate.getDettaglioDocumentoNonSbn(d);
			dettaglio.setFlCanc("S");
			List<EsemplareDocumentoNonSbnVO> esemplari = dettaglio.getEsemplari();
			if (ValidazioneDati.isFilled(esemplari))
				for (EsemplareDocumentoNonSbnVO e : esemplari)
					e.setFlCanc("S");

			documenti.add(dettaglio);
		}

		try {
			delegate.aggiornaDocumentoNonSbn(documenti);

			Iterator<DocumentoNonSbnVO> i = currentForm.getDocumenti().iterator();
			while (i.hasNext())
				if (items.contains(String.valueOf(i.next().getUniqueId())))
					i.remove();

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.generico"));
			log.error("", e);
		}

		return no(mapping, currentForm, request, response);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		currentForm.setOperazione(null);
		currentForm.setConferma(false);
		currentForm.setListaPulsanti(BOTTONIERA);

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		try {
			currentForm.setIdSelezionati(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;

		int numDoc=0;
		try {
			//currentForm.setCodSelAutSing("");
			numDoc = currentForm.getDocumenti().size();
			if (numDoc > 0) {
				String[] codDoc = new String[numDoc];
				int i=0;
				Iterator<DocumentoNonSbnVO> iterator=currentForm.getDocumenti().iterator();
				while (iterator.hasNext()) {
					codDoc[i] = String.valueOf(iterator.next().getUniqueId());
					i++;
				}
				currentForm.setIdSelezionati(codDoc);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		DescrittoreBloccoVO blocco = currentForm.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2 || blocco.getIdLista() == null)
			return mapping.getInputForward();

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		DescrittoreBloccoVO nextBlocco = delegate.caricaBlocco(Navigation.getInstance(request).getUserTicket(),
			blocco.getIdLista(), bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		List<DocumentoNonSbnVO> documenti = currentForm.getDocumenti();
		documenti.addAll(nextBlocco.getLista() );
		Collections.sort(documenti, BaseVO.ORDINAMENTO_PER_PROGRESSIVO);

		return mapping.getInputForward();
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		//almaviva5_20110415 #4372
		if (ValidazioneDati.equals(idCheck, "SELEZIONE_SINGOLA")) {
			//in modalità sif da erogazione o stampa
			ParametriServizi parametri = currentForm.getParametri();
			ModalitaCercaType modalitaCerca = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_DOCUMENTO);
			return ValidazioneDati.in(modalitaCerca,
					ModalitaCercaType.CERCA_PER_EROGAZIONE,
					ModalitaCercaType.CERCA_PER_STAMPA_SERVIZI_CORRENTI);
		}

		if (ValidazioneDati.in(idCheck,
				//"servizi.bottone.scegli",
				"servizi.bottone.esamina",
				"servizi.bottone.nuovo") ) {
			DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
			return !filtro.isRicercaOpac();
		}

		return true;
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DocumentoNonSbnVO doc = getCodSelezionato(form);

		if (doc == null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));

			return mapping.getInputForward();
		}

		//almaviva5_20160617 cortocircuito scegli su importa da opac (servizi ILL)
		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
		if (filtro.isRicercaOpac())
			return importaOpac(mapping, form, request, response);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		DocumentoNonSbnVO dettaglio = delegate.getDettaglioDocumentoNonSbn(doc);
		if (dettaglio == null)
			return mapping.getInputForward();

		if (ValidazioneDati.size(dettaglio.getEsemplari()) == 1 ) {
			//il doc ha un solo esemplare. Posso tornare a servizi
			ParametriServizi parametri = currentForm.getParametri();
			SIFListaDocumentiNonSbnVO sif = null;
				sif = (SIFListaDocumentiNonSbnVO) parametri.get(ParamType.PARAMETRI_SIF_DOCNONSBN);
			if (sif == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.generico"));

				return mapping.getInputForward();
			}

			sif.setDocumento(dettaglio);

			// porto l'esemplare scento alla maschera di erogazione servizi
			String requestAttribute = sif.getRequestAttribute();
			request.setAttribute(requestAttribute, sif);
			if (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
//				ParametriDocumentiNonSbn.send(request, parametri);
				return mapping.findForward("stampaServizi");
			}

			// torno al bookmark impostato dalla mappa di erogazione
			return Navigation.getInstance(request).goToBookmark(requestAttribute, false);

		}

		//il doc. ha più esemplari, devo navigare sulla mappa del dettaglio
		//almaviva5_20100322 #3647 simulo selezione multipla
		currentForm.setIdSelezionati(new String[] {dettaglio.getUniqueId() + ""} );
		return esamina2(mapping, currentForm, request, response);
	}

	protected ActionForward preparaConferma(HttpServletRequest request,
			ActionMapping mapping, ActionForm form, ActionMessage msg, String[] pulsanti)
			throws Exception {
		ListaDocumentiForm currentForm = (ListaDocumentiForm) form;
		currentForm.setConferma(true);
		currentForm.setListaPulsanti(pulsanti != null ? pulsanti : BOTTONIERA_CONFERMA);
		LinkableTagUtils.addError(request, msg != null ? msg : new ActionMessage("errors.servizi.confermaOperazioneAgg"));
		return mapping.getInputForward();
	}

}
