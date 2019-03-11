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
package it.iccu.sbn.web.integration.action.sale;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.util.ThreeState;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.actionforms.servizi.sale.RicercaSaleForm;
import it.iccu.sbn.web.integration.bd.gestioneservizi.CalendarioDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RicercaSaleAction extends ServiziBaseAction implements SbnAttivitaChecker {

	private static final String[] BOTTONIERA_RICERCA = new String[] {
			"servizi.bottone.cerca",
			"servizi.bottone.nuovo" };

	private static final String[] BOTTONIERA_PRENOTAZIONI = new String[] {
			"servizi.bottone.aggiorna",
			"servizi.bottone.esamina",
			"servizi.bottone.nuovo" };

	private static final String[] BOTTONIERA_MEDIAZIONI = new String[] {
			"servizi.bottone.calendario",
			"servizi.bottone.cancella.calendario" };

	protected static final String[] BOTTONIERA_CONFERMA = new String[] {
			"servizi.bottone.si",
			"servizi.bottone.no",
			"servizi.bottone.annulla" };

	protected static final int FOLDER_PRENOTAZIONI = 0;
	protected static final int FOLDER_RICERCA = 1;
	protected static final int FOLDER_MEDIAZIONI = 2;

	private static final String[] FOLDERS = new String[3];

	static {
		//ordine dei folder (da replicare nella JSP)
		FOLDERS[FOLDER_PRENOTAZIONI] 	= "servizi.bottone.sale.prenotazioni";
		FOLDERS[FOLDER_RICERCA] 		= "servizi.bottone.sale";
		FOLDERS[FOLDER_MEDIAZIONI] 		= "servizi.bottone.sale.categorieMediazione";
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca", "cerca");
		map.put("servizi.bottone.nuovo", "nuovo");

		map.put("servizi.bottone.cambioBiblioteca", "biblio");

		map.put("servizi.bottone.sale", "folder_ricerca");
		map.put("servizi.bottone.sale.prenotazioni", "folder_prenotazioni");
		map.put("servizi.bottone.sale.categorieMediazione", "folder_mediazioni");

		map.put("servizi.bottone.calendario", "calendario");
		map.put("servizi.bottone.cancella.calendario", "cancellaCalendario");

		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");

		map.put("button.blocco", "blocco");

		map.put("servizi.bottone.esaminaMov", "esaminaMov");

		map.put("servizi.bottone.rifiuta", "annullaPrenotazione");
		map.put("servizi.bottone.hlputente", "sif_utente");

		map.put("servizi.bottone.esamina", "esamina");

		map.put("servizi.bottone.annullaOperazione", "annullaPrenotazione");
		map.put("servizi.bottone.annulla", "chiudi");

		map.put("servizi.bottone.aggiorna", "aggiorna");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		if (!currentForm.isInitialized() )
			init(request, form);

		loadForm(request, currentForm);

		Integer folder = currentForm.getCurrentFolder();
		switch (folder) {
		case FOLDER_PRENOTAZIONI:
			return folder_prenotazioni(mapping, currentForm, request, response);
		case FOLDER_MEDIAZIONI:
			return folder_mediazioni(mapping, currentForm, request, response);
		case FOLDER_RICERCA:
		default:
			break;
		}

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		Navigation navi = Navigation.getInstance(request);
		UserVO u = navi.getUtente();
		String codBib = ValidazioneDati.coalesce(currentForm.getBiblioteca(), u.getCodBib() );

		currentForm.setBiblioteca(codBib);
		RicercaSalaVO ricerca = currentForm.getRicerca();
		ricerca.setCodPolo(u.getCodPolo());
		ricerca.setCodBib(codBib);

		RicercaPrenotazionePostoVO ricercaPren = currentForm.getRicercaPrenotazioni();
		ricercaPren.setCodPolo(u.getCodPolo());
		ricercaPren.setCodBib(codBib);

		BibliotecaVO bib = DomainEJBFactory.getInstance().getBiblioteca().getBiblioteca(u.getCodPolo(), currentForm.getBiblioteca() );
		ParametriServizi params = currentForm.getParametri();
		params.put(ParamType.BIBLIOTECA, bib);

		currentForm.setPulsanti(BOTTONIERA_RICERCA);

		//TODO da sostituire con tabella codici
		List<TB_CODICI> ordinamenti = new ArrayList<TB_CODICI>();
		ordinamenti.add(new TB_CODICI("1", "Data"));
		ordinamenti.add(new TB_CODICI("2", "Utente"));
		ordinamenti.add(new TB_CODICI("3", "Sala"));
		currentForm.setListaTipoOrdinamento(ordinamenti);

		currentForm.setFolders(FOLDERS);
		currentForm.setCurrentFolder(FOLDER_PRENOTAZIONI);

		navi.addBookmark(Bookmark.Servizi.RICERCA_SALE);

		List<Mediazione> lista = SaleDelegate.getInstance(request)
				.getListaCategorieMediazione(bib.getCod_polo(), bib.getCod_bib(), true, ThreeState.UNSET, 0).getLista();
		currentForm.setListaCatMediazione(ValidazioneDati.coalesce(lista, ValidazioneDati.emptyList(Mediazione.class)) );

		currentForm.setInitialized(true);
	}


	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		String codUtente = (String) request.getAttribute(NavigazioneServizi.COD_UTENTE);
		if (ValidazioneDati.isFilled(codUtente))
			currentForm.getGrigliaCalendario().getUtente().setCodUtente(codUtente);
		super.loadForm(request, form);
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		SaleDelegate delegate = SaleDelegate.getInstance(request);
		RicercaSalaVO ricerca = currentForm.getRicerca();

		try {
			DescrittoreBloccoVO blocco1 = delegate.getListaSale(ricerca);
			if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
				return mapping.getInputForward();
			}

			ParametriServizi parametri = currentForm.getParametri().copy();

			parametri.put(ParamType.PARAMETRI_RICERCA, ricerca.copy() );
			parametri.put(ParamType.LISTA_SALE, blocco1);
			ParametriServizi.send(request, parametri);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return Navigation.getInstance(request).goForward(mapping.findForward("listaSale"));
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;

		if (!isTokenValid(request))
			saveToken(request);

		switch (currentForm.getCurrentFolder()) {
		case FOLDER_RICERCA: {
			ParametriServizi parametri = currentForm.getParametri().copy();
			SalaVO sala = new SalaVO(currentForm.getRicerca());
			parametri.put(ParamType.DETTAGLIO_SALA, sala );

			ParametriServizi.send(request, parametri);

			return Navigation.getInstance(request).goForward(mapping.findForward("nuovaSala"));
		}

		case FOLDER_PRENOTAZIONI: {
			ParametriServizi parametri = currentForm.getParametri().copy();
			RicercaGrigliaCalendarioVO grigliaCalendario = currentForm.getGrigliaCalendario().copy();
			String codUtente = ServiziUtil.espandiCodUtente(grigliaCalendario.getUtente().getCodUtente());
			UtenteBaseVO utente = ServiziDelegate.getInstance(request).getUtente(codUtente);
			if (utente == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));
				return mapping.getInputForward();
			}
			grigliaCalendario.setUtente(utente);

			grigliaCalendario.getCd_cat_mediazione().add(currentForm.getCd_cat_mediazione());
			parametri.put(ParamType.GRIGLIA_CALENDARIO, grigliaCalendario);
			parametri.put(ParamType.MODALITA_GESTIONE_PRENOT_POSTO, ModalitaGestioneType.CREA);
			ParametriServizi.send(request, parametri);

			return Navigation.getInstance(request).goForward(mapping.findForward("prenotazione"));
		}

		case FOLDER_MEDIAZIONI:
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward folder_ricerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		currentForm.setCurrentFolder(FOLDER_RICERCA);
		currentForm.setPulsanti(BOTTONIERA_RICERCA);
		return mapping.getInputForward();
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//ricarica lista prenotazioni
		return folder_prenotazioni(mapping, form, request, response);
	}

	public ActionForward folder_prenotazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		currentForm.setCurrentFolder(FOLDER_PRENOTAZIONI);
		currentForm.setPulsanti(BOTTONIERA_PRENOTAZIONI);

		BibliotecaVO bib = (BibliotecaVO) currentForm.getParametri().get(ParamType.BIBLIOTECA);
		RicercaPrenotazionePostoVO ricerca = currentForm.getRicercaPrenotazioni();
		ricerca.setCodBib(bib.getCod_bib());

		try {
			List<Mediazione> lista = SaleDelegate.getInstance(request)
					.getListaCategorieMediazione(bib.getCod_polo(), bib.getCod_bib(), true, ThreeState.FALSE, 0).getLista();
			currentForm.setListaCatMediazione(ValidazioneDati.coalesce(lista, ValidazioneDati.emptyList(Mediazione.class)) );

			currentForm.getPrenotazioni().clear();
			DescrittoreBloccoVO blocco1 = SaleDelegate.getInstance(request).getListaPrenotazioniPosto(ricerca);
			currentForm.setBlocco(blocco1);
			if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
				if (!LinkableTagUtils.hasErrors(request) )
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
				return mapping.getInputForward();
			}

			Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
			if (blocco1.getTotRighe() == 1) {
				PrenotazionePostoVO pp = (PrenotazionePostoVO) ValidazioneDati.first(blocco1.getLista());
				currentForm.setSelectedPren(pp.getId_prenotazione());
			}

			currentForm.getPrenotazioni().addAll(blocco1.getLista());
			currentForm.setBloccoSelezionato(1);
			currentForm.getBlocchiCaricati().clear();

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward folder_mediazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		currentForm.setCurrentFolder(FOLDER_MEDIAZIONI);
		currentForm.setPulsanti(BOTTONIERA_MEDIAZIONI);

		BibliotecaVO bib = (BibliotecaVO) currentForm.getParametri().get(ParamType.BIBLIOTECA);

		try {
			DescrittoreBloccoVO blocco1 = SaleDelegate.getInstance(request).getListaCategorieMediazione(bib.getCod_polo(),
					bib.getCod_bib(), false, ThreeState.UNSET, currentForm.getRicerca().getElementiPerBlocco());
			currentForm.setBlocco(blocco1);
			if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
				return mapping.getInputForward();
			}

			Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
			if (blocco1.getTotRighe() == 1) {
				Mediazione med = (Mediazione) ValidazioneDati.first(blocco1.getLista());
				currentForm.setSelectedCat(med.getRepeatableId());
			}

			currentForm.setCategorieMediazione(blocco1.getLista());
			currentForm.setBloccoSelezionato(1);
			currentForm.getBlocchiCaricati().clear();

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		DescrittoreBloccoVO blocco = currentForm.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2 || blocco.getIdLista() == null
				|| currentForm.getBlocchiCaricati().contains(bloccoSelezionato))
			return mapping.getInputForward();

		DescrittoreBloccoVO nextBlocco = DescrittoreBlocchiUtil.browseBlocco(
				Navigation.getInstance(request).getUserTicket(), blocco.getIdLista(),
				bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(nextBlocco.getNumBlocco());
		List<PrenotazionePostoVO> prenotazioni = currentForm.getPrenotazioni();
		prenotazioni.addAll(nextBlocco.getLista());
		Collections.sort(prenotazioni, BaseVO.ORDINAMENTO_PER_PROGRESSIVO);

		return mapping.getInputForward();
	}

	public ActionForward calendario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;

		List<Integer> selected = getMultiBoxSelectedItems(currentForm.getSelectedCat());
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		Mediazione med = UniqueIdentifiableVO.searchRepeatableId(selected.get(0), currentForm.getCategorieMediazione());
		ModelloCalendarioVO modello = med.getCalendario();
		try {
			if (modello == null) {
				//nuovo calendario da modello biblioteca
				BibliotecaVO bib = (BibliotecaVO) currentForm.getParametri().get(ParamType.BIBLIOTECA);
				modello = CalendarioDelegate.getInstance(request).getCalendarioBiblioteca(bib.getCod_polo(), bib.getCod_bib());
				if (modello == null)
					throw new ApplicationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_BIB_NON_TROVATO);

				modello.setId(0); //nuovo calendario
				modello.setDescrizione(String.format("calendario '%s' (%s)", med.getDescr(), med.getCd_cat_mediazione() ));
				modello.setCd_cat_mediazione(med.getCd_cat_mediazione());
			}

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		modello.setTipo(TipoCalendario.MEDIAZIONE);
		ParametriServizi params = currentForm.getParametri().copy();
		params.put(ParamType.MODELLO_CALENDARIO, modello.copy() );
		ParametriServizi.send(request, params);

		return Navigation.getInstance(request).goForward(mapping.findForward("calendario"));

	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		TipoOperazione op = currentForm.getOperazione();
		switch (op) {
		case CANCELLA:
			return eseguiCancellazione(mapping, form, request, response);
		default:
			break;
		}

		return no(mapping, currentForm, request, response);
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	private ActionForward eseguiCancellazione(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		try {
			List<Integer> selected = getMultiBoxSelectedItems(currentForm.getSelectedCat());
			Mediazione med = UniqueIdentifiableVO.searchRepeatableId(selected.get(0), currentForm.getCategorieMediazione());
			ModelloCalendarioVO calendario = med.getCalendario();
			CalendarioDelegate.getInstance(request).cancellaModelloCalendario(calendario);

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

			return folder_mediazioni(mapping, currentForm, request, response);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();

		} finally {
			no(mapping, form, request, response);
		}

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		currentForm.setConferma(false);
		currentForm.setPulsanti(BOTTONIERA_MEDIAZIONI);

		return mapping.getInputForward();
	}

	public ActionForward cancellaCalendario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		List<Integer> selected = getMultiBoxSelectedItems(currentForm.getSelectedCat());
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		Mediazione med = UniqueIdentifiableVO.searchRepeatableId(selected.get(0), currentForm.getCategorieMediazione());
		CalendarioVO modello = med.getCalendario();

			if (modello == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
				saveToken(request);
				return mapping.getInputForward();
			}

			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.CANCELLA);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneCan"));
			return mapping.getInputForward();
	}

	public ActionForward esaminaMov(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;

		Integer selected = currentForm.getSelectedMov();
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		try {
			BibliotecaVO bib = (BibliotecaVO) currentForm.getParametri().get(ParamType.BIBLIOTECA);
			MovimentoVO mov = new MovimentoVO();
			mov.setCodPolo(bib.getCod_polo());
			mov.setCodBibOperante(bib.getCod_bib());
			mov.setIdRichiesta(selected);
			mov = ServiziDelegate.getInstance(request).getDettaglioMovimento(mov, Locale.getDefault());
			if (mov == null) {
				return mapping.getInputForward();
			}

			request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, mov);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "ListaMov");

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		return Navigation.getInstance(request).goForward(mapping.findForward("movimento"));

	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;

		Integer selected = currentForm.getSelectedPren();
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		try {
			ParametriServizi params = currentForm.getParametri().copy();
			PrenotazionePostoVO pp = new PrenotazionePostoVO();
			pp.setId_prenotazione(selected);
			pp = SaleDelegate.getInstance(request).getDettaglioPrenotazionePosto(pp);
			if (pp == null)
				return mapping.getInputForward();

			params.put(ParamType.PRENOTAZIONE_POSTO, pp);
			params.put(ParamType.MODALITA_GESTIONE_PRENOT_POSTO, ModalitaGestioneType.ESAMINA);
			ParametriServizi.send(request, params);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		return Navigation.getInstance(request).goForward(mapping.findForward("prenotazione"));
	}

	public ActionForward annullaPrenotazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		Integer selected = currentForm.getSelectedPren();
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		try {
			PrenotazionePostoVO pp = new PrenotazionePostoVO();
			pp.setId_prenotazione(selected);
			pp = SaleDelegate.getInstance(request).getDettaglioPrenotazionePosto(pp);
			if (pp == null)
				return mapping.getInputForward();

			SaleDelegate.getInstance(request).rifiutaPrenotazionePosto(pp, getOperatore(request) == OperatoreType.BIBLIOTECARIO);
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

			//ricarica prenotazioni
			return folder_prenotazioni(mapping, currentForm, request, response);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
		}

		return mapping.getInputForward();
	}

	public ActionForward sif_utente(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RicercaSaleForm currentForm = (RicercaSaleForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		request.setAttribute(BIBLIOTECA_ATTR, currentForm.getBiblioteca());
		request.setAttribute(PATH_CHIAMANTE_ATTR, "ErogazioneRicerca");

		return mapping.findForward("sif_utente");
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		RicercaSaleForm currentForm = (RicercaSaleForm) form;

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.rifiuta")) {
			List<PrenotazionePostoVO> prenotazioni = currentForm.getPrenotazioni();
			return ValidazioneDati.isFilled(prenotazioni);
		}

		if (ValidazioneDati.equals(idCheck, "CREA_PRENOTAZIONE")) {
			List<Mediazione> listaCatMediazione = currentForm.getListaCatMediazione();
			return ValidazioneDati.isFilled(listaCatMediazione);
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.nuovo")) {
			if (currentForm.getCurrentFolder() == FOLDER_PRENOTAZIONI)
				return checkAttivita(request, currentForm, "CREA_PRENOTAZIONE");
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.esamina")) {
			List<PrenotazionePostoVO> prenotazioni = currentForm.getPrenotazioni();
			return ValidazioneDati.isFilled(prenotazioni);
		}

		if (currentForm.getCurrentFolder() == FOLDER_MEDIAZIONI) {
			if (ValidazioneDati.in(idCheck,
					"servizi.bottone.calendario",
					"servizi.bottone.cancella.calendario")) {
				List<Mediazione> mediazioni = currentForm.getCategorieMediazione();
				return ValidazioneDati.isFilled(mediazioni);
			}
		}

		return true;
	}

}
