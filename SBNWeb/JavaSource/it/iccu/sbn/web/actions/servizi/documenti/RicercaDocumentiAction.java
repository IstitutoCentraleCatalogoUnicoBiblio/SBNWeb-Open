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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO.TipoSIF;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.z3950.Z3950ClientFactory;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.documenti.RicercaDocumentoForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RicercaDocumentiAction extends ServiziBaseAction implements SbnAttivitaChecker {

	private static final String CAMBIO_BIBLIOTECA = "ric.doc.non.sbn.cambio.bib";
	private static final String[] BOTTONIERA = new String[] {
			"servizi.bottone.cerca",
			"servizi.bottone.nuovo" };
	private static final String[] BOTTONIERASTAMPA = new String[] {
		"servizi.bottone.cerca",
	    "servizi.bottone.annulla" };
	private static final String[] BOTTONIERA_SIF = new String[] {
			"servizi.bottone.cerca",
			"servizi.bottone.cerca.opac",
			"servizi.bottone.nuovo",
			"servizi.bottone.annulla" };

	private static Logger log = Logger.getLogger(RicercaDocumentiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca", "cerca");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.cambioBiblioteca", "biblio");
		map.put("servizi.bottone.annulla", "annulla");

		//almaviva5_20160317 servizi ILL
		map.put("servizi.bottone.cerca.opac", "cercaOpac");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		if (!currentForm.isInitialized() )
			init(request, form);

		//cambio biblioteca
		BibliotecaVO bib = (BibliotecaVO) request.getAttribute(CAMBIO_BIBLIOTECA);
		if (bib != null) {
			DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
			filtro.setCodBib(bib.getCod_bib() );
			currentForm.setBiblioteca(bib.getCod_bib() );
		}

		return mapping.getInputForward();
	}

	protected void init(HttpServletRequest request, ActionForm form) throws Exception {

		log.debug("RicercaDocumentiAction::init");
		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;

		ParametriServizi parametri = ParametriServizi.retrieve(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")) {
			parametri.put(ParamType.MODALITA_CERCA_DOCUMENTO, ModalitaCercaType.CERCA_PER_STAMPA_SERVIZI_CORRENTI);
		}
		if (parametri == null) {
			parametri = new ParametriServizi();
			parametri.put(ParamType.MODALITA_CERCA_DOCUMENTO, ModalitaCercaType.CERCA);
		}
		currentForm.setParametri(parametri);

		DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
		UserVO utente = navi.getUtente();
		filtro.setCodPolo(utente.getCodPolo());
		filtro.setCodBib(utente.getCodBib());
		filtro.setTipo_doc_lett('P');	//imposto tipo P
		filtro.setOrdinamento("1");	// per titolo

		currentForm.setBiblioteca(utente.getCodBib() );

		Utente utenteEJB = ServiziDelegate.getUtenteEJB(request);
		try {
			int elementiPerBlocco = Integer.valueOf((String) utenteEJB.getDefault(ConstantDefault.ELEMENTI_BLOCCHI) );
			filtro.setElementiPerBlocco(elementiPerBlocco);
		} catch (Exception e) {
			log.error(e);
		}

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		CaricamentoCombo combo = new CaricamentoCombo();

		//almaviva5_20110406 #4348
		List<ComboCodDescVO> listaOrd = delegate.getComboCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_DOCUMENTO_LETTORE);
		listaOrd = CaricamentoCombo.cutFirst(listaOrd);
		currentForm.setListaTipoOrdinamento(listaOrd);
		currentForm.setListaTipoFonte(delegate.getComboCodici(CodiciType.CODICE_FONTE_DOCUMENTO_LETTORE));
		List<TB_CODICI> codiciTipoDoc = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DOCUMENTO_LETTORE, true, "S");
		currentForm.setListaTipoDocumento(combo.loadCodiceDesc(codiciTipoDoc));

		//almaviva5_20160331 servizi ill
		currentForm.setListaNature(CodiciProvider.getCodici(CodiciType.CODICE_NATURA_ORDINE, true));
		currentForm.setListaTipoNumeroStd(Z3950ClientFactory.getCodiciNumeroStandardZ3950());
		currentForm.setListaTipoRecord(CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC, true));

		ModalitaCercaType modalita = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_DOCUMENTO);
		currentForm.setModalitaCerca(modalita);

		SIFListaDocumentiNonSbnVO sif = null;
		switch (modalita) {
		case CERCA:
			currentForm.setListaPulsanti(BOTTONIERA);
			break;
		case CERCA_PER_STAMPA_SERVIZI_CORRENTI:
			currentForm.setListaPulsanti(BOTTONIERASTAMPA);
			break;
		case CERCA_PER_EROGAZIONE:
			currentForm.setListaPulsanti(BOTTONIERA_SIF);
			sif = (SIFListaDocumentiNonSbnVO) parametri.get(ParamType.PARAMETRI_SIF_DOCNONSBN);
			currentForm.setAttivazioneSIF(sif);
			filtro.setCodPolo(sif.getCodPolo());
			filtro.setCodBib(sif.getCodBib());
			if (sif.getTipoSIF() == TipoSIF.DOCUMENTO_POSSEDUTO) {
				filtro.setTipo_doc_lett('P');
				filtro.setSegnatura(sif.getSegnatura());
			} else {
				//almaviva5_20160216 servizi ILL
				filtro.setTipo_doc_lett('D');
				filtro.setTitolo(sif.getTitolo());
			}

			//almaviva5_20091027 filtro bib metropolitane
			filtro.setListaBib(sif.getListaBib());
			break;
		}

		currentForm.setInitialized(true);
	}

	private ActionForward eseguiCerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();

		try {
			checkFiltroRicerca(filtro);
			DescrittoreBloccoVO blocco1 = delegate.getListaDocumentiNonSbn(filtro);
			if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
				return mapping.getInputForward();
			}

			ParametriServizi parametri = currentForm.getParametri().copy();

			parametri.put(ParamType.PARAMETRI_RICERCA, filtro.copy() );
			parametri.put(ParamType.LISTA_DOCUMENTI, blocco1);
			ParametriServizi.send(request, parametri);

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage()));
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return Navigation.getInstance(request).goForward(mapping.findForward("listaDocumenti"));
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
		filtro.setRicercaOpac(false);

		return eseguiCerca(mapping, currentForm, request, response);
	}

	public ActionForward cercaOpac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		DocumentoNonSbnRicercaVO filtro = currentForm.getFiltro();
		filtro.setRicercaOpac(true);
		//almaviva5_20160617 filtro bib opac su tipo servizio
		filtro.setTipoServizio(currentForm.getAttivazioneSIF().getTipoServizio());

		return eseguiCerca(mapping, currentForm, request, response);
	}

	private void checkFiltroRicerca(DocumentoNonSbnRicercaVO filtro) throws ValidationException {

		String segnatura = filtro.getSegnatura();
		if (ValidazioneDati.strIsNull(segnatura))
			filtro.setOrd_segnatura(null);
		else
			filtro.setOrd_segnatura(OrdinamentoCollocazione2.normalizza(segnatura) );

		String utente = filtro.getUtente();
		if (ValidazioneDati.isFilled(utente))
			filtro.setUtente(ServiziUtil.espandiCodUtente(utente));

		try {
			Timestamp tsIns = null;
			Timestamp tsVar = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String dataInizio = filtro.getDataInizio();
			if (ValidazioneDati.strIsNull(dataInizio))
				filtro.setTsIns(null);
			else {
				if (ValidazioneDati.validaData(dataInizio) != ValidazioneDati.DATA_OK)
					throw new ValidationException(SbnErrorTypes.SRV_DATA_INIZIO_INTERVALLO_ERRATA);

				tsIns = new Timestamp(dateFormat.parse(dataInizio).getTime());
				filtro.setTsIns(tsIns);
			}

			String dataFine = filtro.getDataFine();
			if (ValidazioneDati.strIsNull(dataFine))
				filtro.setTsVar(null);
			else {
				if (ValidazioneDati.validaData(dataFine) != ValidazioneDati.DATA_OK)
					throw new ValidationException(SbnErrorTypes.SRV_DATA_FINE_INTERVALLO_ERRATA);

				tsVar = DateUtil.toTimestampA(dataFine);
				filtro.setTsVar(tsVar);
			}

			if (ValidazioneDati.isFilled(dataInizio) && ValidazioneDati.isFilled(dataFine)
				&& 	tsIns.after(tsVar))
				throw new ValidationException(SbnErrorTypes.SRV_INTERVALLO_DATE_ERRATO);

		} catch (ParseException e) {
			log.error(e);
		}


	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		ParametriServizi parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.CREA);
		DocumentoNonSbnRicercaVO documento = currentForm.getFiltro().copy();
		documento.setFonte('B');
		documento.setTipoRecord('a');
		parametri.put(ParamType.DETTAGLIO_DOCUMENTO, documento );
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("nuovoDocumento"));
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		BibliotecaDelegate delegate = new BibliotecaDelegate(request);
		UserVO utente = Navigation.getInstance(request).getUtente();
		SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
			new SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(), utente.getCodBib(),
				CodiciAttivita.getIstance().SERVIZI, currentForm.getFiltro().getElementiPerBlocco(), CAMBIO_BIBLIOTECA );
		ActionForward forward = delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		return forward;
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	private enum TipoControllo {
		CAMBIO_BIBLIOTECA,
		UTENTE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		RicercaDocumentoForm currentForm = (RicercaDocumentoForm) form;
		ModalitaCercaType modalita = currentForm.getModalitaCerca();

		try {

			if (ValidazioneDati.equals(idCheck, "servizi.bottone.cerca.opac")) {
				//attiva ricerca su opac per titoli di altra bib.
				SIFListaDocumentiNonSbnVO sif = currentForm.getAttivazioneSIF();
				if (sif.getTipoSIF() != TipoSIF.DOCUMENTO_ALTRA_BIB)
					return false;

				//check ricerca opac su z39.50
				boolean opacIsEnabled = Boolean.parseBoolean(CommonConfiguration.getProperty(
						Configuration.OPAC_Z3950_SEARCH_ENABLE, "false"));

				return opacIsEnabled;
			}

			if (ValidazioneDati.equals(idCheck, "servizi.bottone.cerca")) {
				//inibisce ricerca locale per titoli di altra bib.
				SIFListaDocumentiNonSbnVO sif = currentForm.getAttivazioneSIF();
				if (sif != null && sif.getTipoSIF() == TipoSIF.DOCUMENTO_ALTRA_BIB)
					return false;
			}

			if (ValidazioneDati.equals(idCheck, "POSSEDUTO")) {
				//campi di ricerca attivi solo per titoli posseduti
				SIFListaDocumentiNonSbnVO sif = currentForm.getAttivazioneSIF();
				return (sif == null || sif.getTipoSIF() == TipoSIF.DOCUMENTO_POSSEDUTO);
			}


			TipoControllo controllo = TipoControllo.valueOf(idCheck);
			switch (controllo) {
			case CAMBIO_BIBLIOTECA:
				return (modalita == ModalitaCercaType.CERCA);
			case UTENTE:
				return (modalita != ModalitaCercaType.CERCA_PER_EROGAZIONE);
			}

			return true;

		} catch (IllegalArgumentException e) {
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

}
