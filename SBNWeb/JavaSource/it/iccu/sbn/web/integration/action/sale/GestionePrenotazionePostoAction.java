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
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO.SlotSala;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.SlotVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.servizi.SaleUtil;
import it.iccu.sbn.vo.custom.servizi.sale.StatoPrenotazionePosto2;
import it.iccu.sbn.vo.validators.servizi.PrenotazionePostoValidator;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.actionforms.servizi.sale.GestionePrenotazionePostoForm;
import it.iccu.sbn.web.integration.actionforms.servizi.sale.GestionePrenotazionePostoForm.TipoGriglia;
import it.iccu.sbn.web.integration.actionforms.servizi.sale.GestionePrenotazionePostoForm.TipoOperazionePrenotazione;
import it.iccu.sbn.web.integration.bd.gestioneservizi.CalendarioDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class GestionePrenotazionePostoAction extends ServiziBaseAction implements SbnAttivitaChecker {

	private static final String[] BOTTONIERA_GRIGLIA = new String[] {
			"servizi.bottone.torna.sala",
			"servizi.bottone.torna.mese",
			"servizi.bottone.conferma",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_PRENOTAZIONE = new String[] {
			"servizi.bottone.ok",
			"servizi.bottone.prenotazionePosto.sposta",
			"servizi.bottone.fruito",
			"servizi.bottone.conclusa",
			"servizi.bottone.respingi",
			"servizi.bottone.prenotazionePosto.disdici",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_CONFERMA = new String[] {
			"servizi.bottone.si",
			"servizi.bottone.no",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_PRENOTAZIONI_UTENTE = new String[] {
			"servizi.bottone.scegli",
			"servizi.bottone.nuovo",
			"servizi.bottone.annulla" };

	//private static final int DAYS = 30;

	static SimpleDateFormat _formatterMonth = new SimpleDateFormat("MMMM yyyy");
	static SimpleDateFormat _formatterDay = new SimpleDateFormat("EEEE dd/MM/yyyy");

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.conferma", "prenota");
		map.put("servizi.bottone.annulla", "annulla");

		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");

		map.put("button.elemPrec", "prev");
		map.put("button.elemSucc", "next");

		map.put("servizi.bottone.fruito", "fruito");
		map.put("servizi.bottone.conclusa", "conclusa");

		map.put("servizi.bottone.prenotazionePosto.disdici", "rifiuta");
		map.put("servizi.bottone.respingi", "rifiuta");

		map.put("servizi.bottone.scegli", "scegli");
		map.put("servizi.bottone.nuovo", "nuovo");

		map.put("servizi.bottone.torna.sala", "indietro");
		map.put("servizi.bottone.torna.mese", "indietro");

		map.put("servizi.bottone.ok", "salva");
		map.put("servizi.bottone.prenotazionePosto.aggiungi.utente", "sif_utente");
		map.put("servizi.bottone.rimuovi", "rimuoviUtente");

		map.put("servizi.bottone.prenotazionePosto.sposta", "sposta");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			init(request, form);
			loadForm(request, form);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return navi.goBack(true);

		} catch (Exception e) {
			log.error(e);
			setErroreGenerico(request, e);
			return navi.goBack(true);
		}

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		if (currentForm.isInitialized())
			return;

		super.init(request, form);
		ParametriServizi parametri = ParametriServizi.retrieve(request);
		currentForm.setParametri(parametri);
		ModalitaGestioneType modalita = (ModalitaGestioneType) parametri.get(ParamType.MODALITA_GESTIONE_PRENOT_POSTO);
		currentForm.setModalita(modalita);

		RicercaGrigliaCalendarioVO griglia = currentForm.getRicerca();
		switch (modalita) {
		case ESAMINA: {
			PrenotazionePostoVO pp = (PrenotazionePostoVO) parametri.get(ParamType.PRENOTAZIONE_POSTO);
			currentForm.setPrenotazione(pp);
			currentForm.setPrenotazioneOld((PrenotazionePostoVO) pp.clone());

			List<MovimentoVO> movimenti = currentForm.getMovimenti();
			for (MovimentoVO mov : pp.getMovimenti()) {
				movimenti.add(ServiziDelegate.getInstance(request).getDettaglioMovimento(mov, Locale.getDefault()));
			}

			griglia.setUtente(pp.getUtente());
			currentForm.setPulsanti(BOTTONIERA_PRENOTAZIONE);
			currentForm.setTipo(TipoGriglia.DETTAGLIO);
			Navigation.getInstance(request).setTesto(".servizi.sale.gestionePrenotazionePosto.ESAMINA.testo");
			break;
		}

		case SPOSTA:
		case CREA: {
			preparaCreaPrenotazione(request, currentForm, parametri, griglia, DaoManager.now());
			break;
		}

		default:
			break;
		}

		Navigation.getInstance(request).addBookmark(Bookmark.Servizi.PRENOTAZIONE_POSTO);
		currentForm.setInitialized(true);
	}

	private void preparaCreaPrenotazione(HttpServletRequest request, GestionePrenotazionePostoForm currentForm,
			ParametriServizi parametri, RicercaGrigliaCalendarioVO griglia, Date when) throws Exception {
		//crea prenotazione
		currentForm.setPulsanti(BOTTONIERA_GRIGLIA);
		currentForm.setTipo(TipoGriglia.MESE);
		MovimentoVO mov = (MovimentoVO) parametri.get(ParamType.DETTAGLIO_MOVIMENTO);
		if (mov != null) {
			//creazione da erogazione, con movimento e inventario associato
			currentForm.setMovimenti(ValidazioneDati.asList(mov));

			//ricerca delle prenotazioni per l'utente
			RicercaPrenotazionePostoVO ricercaPren = new RicercaPrenotazionePostoVO();
			BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);
			InventarioDettaglioVO inv = getInventario(request, bib, mov);

			UtenteBaseVO utente = ServiziDelegate.getInstance(request).getUtente(mov.getCodUte());
			griglia.setUtente(utente);

			ricercaPren.setCodPolo(bib.getCod_polo());
			ricercaPren.setCodBib(bib.getCod_bib());
			ricercaPren.setUtente(utente);
			ricercaPren.setListaCatMediazione(SaleUtil.tipoMatInv2CatMediazione(inv.getCodMatInv()));
			ricercaPren.setMaxRichiesteAssociate(3);
			ricercaPren.setElementiPerBlocco(0);
			ricercaPren.setDettaglioCompleto(true);

			DescrittoreBloccoVO blocco1 = SaleDelegate.getInstance(request).getListaPrenotazioniPosto(ricercaPren);
			if (DescrittoreBloccoVO.isFilled(blocco1)) {
				//ci sono prenotazioni attive per lo stesso utente, si propone lista per scelta
				List<PrenotazionePostoVO> prenotazioni = blocco1.getLista();
				currentForm.setPrenotazioniUtente(prenotazioni);
				currentForm.setTipo(TipoGriglia.PRENOTAZIONI_UTENTE);
				currentForm.setPulsanti(BOTTONIERA_PRENOTAZIONI_UTENTE);
				if (prenotazioni.size() == 1)
					currentForm.setSelectedPren(prenotazioni.get(0).getId_prenotazione());
			}

		} else {
			//creazione senza movimento associato
			griglia = (RicercaGrigliaCalendarioVO) parametri.get(ParamType.GRIGLIA_CALENDARIO);
			if (griglia != null) {
				currentForm.setRicerca(griglia);
				String catMed = ValidazioneDati.first(griglia.getCd_cat_mediazione());
				currentForm.setDescrCatMediazione(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE, catMed));
			}
		}

		//se la richiesta è inserita dall'utente si verificano i tempi di preparazione del supporto (inventario)
		griglia.setRemoto(getOperatore(request) == OperatoreType.UTENTE);

		griglia.setInizio(when);
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		ParametriServizi parametri = ValidazioneDati.coalesce(ParametriServizi.retrieve(request), currentForm.getParametri());

		if (currentForm.getTipo() == TipoGriglia.MESE) {
			String dataSelezionata = currentForm.getSelectedDay();
			if (ValidazioneDati.isFilled(dataSelezionata)) {
				preparaSala(request, currentForm, dataSelezionata);
				return;
			}

			preparaGrigliaMese(request, currentForm, parametri, currentForm.getRicerca().getInizio());
		}

		Integer salaSelezionata = currentForm.getSelectedSala();
		if (ValidazioneDati.isFilled(salaSelezionata)) {
			preparaGiorno(request, currentForm, salaSelezionata);
			return;
		}

		//aggiungi altro utente a prenotazione
		try {
			String codUtente = (String) request.getAttribute(NavigazioneServizi.COD_UTENTE);
			if (ValidazioneDati.isFilled(codUtente)) {
				List<UtenteBaseVO> altriUtenti = currentForm.getPrenotazione().getAltriUtenti();
				altriUtenti.add(ServiziDelegate.getInstance(request).getUtente(codUtente));
			}
		} catch (Exception e) {
			setErroreGenerico(request, e);
		}
	}

	private void preparaSala(HttpServletRequest request, GestionePrenotazionePostoForm currentForm, String dataSelezionata) {
		GiornoVO giorno = currentForm.getGiorni().get(Integer.parseInt(dataSelezionata));
		currentForm.setGiorno(giorno);
		List<SlotSala> slotSala = giorno.getSlotSala();

		//se ho un'unica sala passo direttamente alla selezione delle fasce
		if (ValidazioneDati.size(slotSala) == 1) {
			preparaGiorno(request, currentForm, ValidazioneDati.first(slotSala).getSala().getIdSala());
			return;
		}

		currentForm.setSelectedSala(null);
//		currentForm.setSelectedDay(null);
//		currentForm.setSelectedFascia(null);
		currentForm.setTipo(TipoGriglia.SALA);
		setIntestazione(currentForm);
	}

	private void preparaGiorno(HttpServletRequest request, GestionePrenotazionePostoForm currentForm, Integer salaSelezionata) {
		GiornoVO giorno = currentForm.getGiorno();
		for (SlotSala ss : giorno.getSlotSala()) {
			if (ss.getSala().getIdSala() == salaSelezionata) {
				currentForm.setSala(ss);
				currentForm.setMaxSlots(ss.getSala().getMaxFascePrenotazione());
				break;
			}
		}

		currentForm.setSelectedDay(null);
		currentForm.setSelectedSala(null);
		currentForm.setSelectedFascia(null);
		currentForm.setTipo(TipoGriglia.GIORNO);
		setIntestazione(currentForm);
	}

	private void preparaGrigliaMese(HttpServletRequest request, GestionePrenotazionePostoForm currentForm,
			ParametriServizi parametri, Date inizio) throws Exception {
		LocalDate start = LocalDate.fromDateFields(inizio);
		LocalDate first = DateUtil.of(start.getYear(), start.getMonthOfYear(), 1);
		LocalDate last = first.plusMonths(1).minusDays(1);

		_preparaGriglia(request, currentForm, parametri, first.toDate(), Days.daysBetween(first,  last).getDays());
	}

	private void _preparaGriglia(HttpServletRequest request, GestionePrenotazionePostoForm currentForm,
			ParametriServizi parametri, Date inizio, int days) throws Exception {
		try {
			BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);
			RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();
			ricerca.setCodPolo(bib.getCod_polo());
			ricerca.setCodBib(bib.getCod_bib());
			LocalDate start = LocalDate.fromDateFields(inizio);
			ricerca.setInizio(start.toDate());
			LocalDate end = start.plusDays(days);//Months(1).dayOfMonth().withMaximumValue();
			ricerca.setFine(end.toDate());

			//ricavo cat. mediazione da inventario
			MovimentoVO mov = ValidazioneDati.first(currentForm.getMovimenti());
			if (mov != null) {
				ricerca.setUtente(ServiziDelegate.getInstance(request).getUtente(mov.getCodUte()));
				InventarioDettaglioVO inv = getInventario(request, bib, mov);
				ricerca.setInventario(inv);

				String codMatInv = inv.getCodMatInv();

				currentForm.setTipoMateriale(CodiciProvider.cercaDescrizioneCodice(codMatInv,
						CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE, CodiciRicercaType.RICERCA_CODICE_SBN));
				ricerca.setCd_cat_mediazione(SaleUtil.tipoMatInv2CatMediazione(codMatInv));
				ricerca.setServizio(ServiziDelegate.getInstance(request).getServizioBiblioteca(bib.getCod_polo(), bib.getCod_bib(),
						mov.getCodTipoServ(), mov.getCodServ()) );
			}

			List<GiornoVO> grigliaCalendario = CalendarioDelegate.getInstance(request).getGrigliaCalendario(ricerca);
			currentForm.setGiorni(grigliaCalendario);

			setIntestazione(currentForm);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
		}
	}

	private InventarioDettaglioVO getInventario(HttpServletRequest request, BibliotecaVO bib, MovimentoVO mov)
			throws ResourceNotFoundException, ApplicationException, ValidationException, DataException, RemoteException,
			NamingException, CreateException {
		return DomainEJBFactory.getInstance().getInventario().getInventarioDettaglio(
				bib.getCod_polo(), bib.getCod_bib(),
				mov.getCodSerieInv(),
				Integer.parseInt(mov.getCodInvenInv()),
				Locale.getDefault(), Navigation.getInstance(request).getUserTicket());
	}

	private void setIntestazione(GestionePrenotazionePostoForm currentForm) {
		switch (currentForm.getTipo()) {
		case MESE:
			RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();
			//String intestazione = _formatterDay.format(ricerca.getInizio()) + " - " + _formatterDay.format(ricerca.getFine());
			String intestazione = _formatterMonth.format(ricerca.getInizio());
			currentForm.setIntestazione(intestazione);
			break;

		case GIORNO:
		case SALA:
			GiornoVO giorno = currentForm.getGiorno();
			currentForm.setIntestazione(_formatterDay.format(giorno.getDate()));
			break;
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		switch (currentForm.getTipo()) {
		case MESE:
			return Navigation.getInstance(request).goBack(true);

		case SALA:
			currentForm.setSelectedFascia(null);
			currentForm.setGiorno(null);
			currentForm.setTipo(TipoGriglia.MESE);
			setIntestazione(currentForm);
			return no(mapping, currentForm, request, response);

		case GIORNO:
			currentForm.setSelectedFascia(null);
			currentForm.setSala(null);
			List<SlotSala> sale = currentForm.getGiorno().getSlotSala();
			if (ValidazioneDati.size(sale) > 1)
				currentForm.setTipo(TipoGriglia.SALA);
			else {
				currentForm.setTipo(TipoGriglia.MESE);
				currentForm.setGiorno(null);
			}
			setIntestazione(currentForm);
			return no(mapping, currentForm, request, response);

		default:
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward prenota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		String fasciaSelezionata = currentForm.getSelectedFascia();
		if (!ValidazioneDati.isFilled(fasciaSelezionata))
			return mapping.getInputForward();

		//lista delle fasce selezionate
		Integer[] slots = ClonePool.fromJson(fasciaSelezionata, Integer[].class );
		if (!ValidazioneDati.isFilled(slots))
			return mapping.getInputForward();

		SlotSala sala = currentForm.getSala();

		List<SlotVO> mergeable = new ArrayList<SlotVO>();
		for (int idx : slots)
			mergeable.add(sala.getSlots().get(idx));
		SlotVO merged = SaleUtil.mergeSlots(mergeable);
		currentForm.setFascia(merged);

		PrenotazionePostoVO pp = preparaPrenotazione(request, form);
		try {
			pp.validate(new PrenotazionePostoValidator());
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		currentForm.setPrenotazione(pp);
		Navigation navi = Navigation.getInstance(request);
		if (navi.bookmarkExists(Bookmark.Servizi.LISTA_MOVIMENTI)) {
			//ritorno a erogazione, senza inserimento reale in bd
			request.setAttribute(NavigazioneServizi.PRENOTAZIONE_POSTO, pp);
			return navi.goToBookmark(Bookmark.Servizi.LISTA_MOVIMENTI, false);
		}

		//check modulo web: inserimento prenotazione senza movimento
		boolean moduloWeb = (getOperatore(request) == OperatoreType.UTENTE) && !ValidazioneDati.isFilled(currentForm.getMovimenti());
		if (navi.bookmarkExists(Bookmark.Servizi.RICERCA_SALE) || moduloWeb) {
			//inserimento da gestione sale, chiedo conferma
			SimpleDateFormat _formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");

			String key_msg;
			if (currentForm.getParametri().get(ParamType.MODALITA_GESTIONE_PRENOT_POSTO) == ModalitaGestioneType.SPOSTA) {
				key_msg = "message.servizi.erogazione.sale.spostaPrenotazionePosto";
				currentForm.setTipoOperazione(TipoOperazionePrenotazione.SPOSTA);
			} else {
				key_msg = "message.servizi.erogazione.sale.richiestaPrenotazionePosto";
				currentForm.setTipoOperazione(TipoOperazionePrenotazione.SALVA);
			}

			LinkableTagUtils.addError(request,
					new ActionMessage(key_msg,
							pp.getPosto().getSala().getDescrizione(), merged.getLabel(),
							_formatter.format(currentForm.getGiorno().getDate())));

			currentForm.setConferma(true);
			this.saveToken(request);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
		}

		if (navi.bookmarkExists(Bookmark.Servizi.ModuloWeb.INSERIMENTO_RICHIESTA)) {
			//ritorno a erogazione, senza inserimento reale in bd
			request.setAttribute(NavigazioneServizi.PRENOTAZIONE_POSTO, pp);
			return navi.goToBookmark(Bookmark.Servizi.ModuloWeb.INSERIMENTO_RICHIESTA, false);
		}

		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		if (!this.isTokenValid(request)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.navigation.locked") );
			return no(mapping, currentForm, request, response);
		}
		this.resetToken(request);
		TipoOperazionePrenotazione op = currentForm.getTipoOperazione();
		switch (op) {
		case SPOSTA:
			return eseguiSpostamentoPrenotazione(mapping, form, request, response);
		case SALVA:
			return eseguiAggiornamentoPrenotazione(mapping, form, request, response);
		case RIFIUTA:
			return eseguiRifiutoPrenotazione(mapping, form, request, response);

		default:
			break;
		}

		return no(mapping, currentForm, request, response);
	}

	private ActionForward eseguiAggiornamentoPrenotazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		try {
			PrenotazionePostoVO pp = currentForm.getPrenotazione();
			pp = SaleDelegate.getInstance(request).aggiornaPrenotazionePosto(pp);
			currentForm.setPrenotazione(pp);
			currentForm.setPrenotazioneOld((PrenotazionePostoVO) pp.copy());
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return no(mapping, currentForm, request, response);
		}

		return navi.goToBookmark(Bookmark.Servizi.RICERCA_SALE, false);
	}

	private ActionForward eseguiSpostamentoPrenotazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		try {
			PrenotazionePostoVO pp = currentForm.getPrenotazione();

			//check spostamento prenotazione
			ParametriServizi parametri = currentForm.getParametri();
			PrenotazionePostoVO prenDaSpostare = (PrenotazionePostoVO) parametri.get(ParamType.PRENOTAZIONE_POSTO);
			if (pp.isNuovo() && (prenDaSpostare != null && !prenDaSpostare.isNuovo()) ) {
				prenDaSpostare.setPosto(pp.getPosto());
				prenDaSpostare.setTs_inizio(pp.getTs_inizio());
				prenDaSpostare.setTs_fine(pp.getTs_fine());
				pp = prenDaSpostare;
			}

			pp = SaleDelegate.getInstance(request).aggiornaPrenotazionePosto(pp);
			currentForm.setPrenotazione(pp);
			currentForm.setPrenotazioneOld((PrenotazionePostoVO) pp.copy());
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return no(mapping, currentForm, request, response);
		}

		return navi.goToBookmark(Bookmark.Servizi.RICERCA_SALE, false);
	}

	private ActionForward eseguiRifiutoPrenotazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		try {
			PrenotazionePostoVO pp = currentForm.getPrenotazione();
			pp = SaleDelegate.getInstance(request).rifiutaPrenotazionePosto(pp, getOperatore(request) == OperatoreType.BIBLIOTECARIO);
			currentForm.setPrenotazioneOld(pp);
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return no(mapping, currentForm, request, response);
		}

		return navi.goToBookmark(Bookmark.Servizi.RICERCA_SALE, false);
	}

	private PrenotazionePostoVO preparaPrenotazione(HttpServletRequest request, ActionForm form) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		MovimentoVO mov = ValidazioneDati.first(currentForm.getMovimenti());
		GiornoVO giorno = currentForm.getGiorno();
		SlotVO fascia = currentForm.getFascia();
		RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();

		PrenotazionePostoVO pp = ValidazioneDati.coalesce(currentForm.getPrenotazione(), new PrenotazionePostoVO());
		pp.setCodPolo(ricerca.getCodPolo());
		pp.setCodBib(ricerca.getCodBib());
		if (mov != null)
			pp.getMovimenti().add(mov);
		pp.setUtente(ricerca.getUtente());

		pp.setStato(StatoPrenotazionePosto.IMMESSA);
		pp.setTs_inizio(DateUtil.componiData(giorno.getDate(), fascia.getInizio(), 0));
		pp.setTs_fine(DateUtil.componiData(giorno.getDate(), fascia.getFine(), 0));

		pp.setTsIns(DaoManager.now());
		String firmaUtente = DaoManager.getFirmaUtente(navi.getUserTicket());
		pp.setUteIns(firmaUtente);
		pp.setUteVar(firmaUtente);
		pp.setFlCanc("N");

		pp.setPosto(ValidazioneDati.first(fascia.getPosti()));

		pp.setCatMediazione(ValidazioneDati.first(ricerca.getCd_cat_mediazione()) );

		return pp;
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		currentForm.setSelectedFascia(null);
		currentForm.setFascia(null);

		currentForm.setConferma(false);

		switch (currentForm.getTipo()) {
		case DETTAGLIO:
			currentForm.setPulsanti(BOTTONIERA_PRENOTAZIONE);
			PrenotazionePostoVO old = currentForm.getPrenotazioneOld();
			currentForm.setPrenotazione(old);
			break;

		case GIORNO:
		case MESE:
			currentForm.setPulsanti(BOTTONIERA_GRIGLIA);
			break;

		case PRENOTAZIONI_UTENTE:
			currentForm.setPulsanti(BOTTONIERA_PRENOTAZIONI_UTENTE);
			break;

		default:
			break;
		}


		return mapping.getInputForward();
	}

	public ActionForward prev(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();
		switch (currentForm.getTipo()) {
		case MESE:
			//LocalDate inizio = LocalDate.fromDateFields(ricerca.getInizio()).minusDays(1).minusDays(DAYS);
			LocalDate inizio = LocalDate.fromDateFields(ricerca.getInizio()).minusMonths(1);
			//if (inizio.isBefore(LocalDate.now()))
			//	return mapping.getInputForward();

			preparaGrigliaMese(request, currentForm, currentForm.getParametri(), inizio.toDate());
			return mapping.getInputForward();

		case GIORNO:
			GiornoVO giorno = currentForm.getGiorno();
			int idx = currentForm.getGiorni().indexOf(giorno);
			if (idx == 0)
				mapping.getInputForward();

			//preparaGiorno(request, currentForm, String.valueOf(idx - 1) );
			return mapping.getInputForward();

		default:
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward next(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();
		switch (currentForm.getTipo()) {
		case MESE:
			//Date inizio = LocalDate.fromDateFields(ricerca.getFine()).plusDays(1).toDate();
			Date inizio = LocalDate.fromDateFields(ricerca.getInizio()).plusMonths(1).toDate();
			preparaGrigliaMese(request, currentForm, currentForm.getParametri(), inizio);
			return mapping.getInputForward();

		case SALA:
			String dataSelezionata = currentForm.getSelectedDay();
			preparaSala(request, currentForm, dataSelezionata);
			return mapping.getInputForward();

		case GIORNO:
			//GiornoVO giorno = currentForm.getGiorno();
			//preparaGiorno(request, currentForm, String.valueOf(currentForm.getGiorni().indexOf(giorno) + 1) );
			return mapping.getInputForward();

		default:
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward rifiuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		PrenotazionePostoVO pp = currentForm.getPrenotazione();
		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.sale.rifiutoPrenotazionePosto",
				pp.getId_prenotazione(), pp.getPosto().getSala().getDescrizione()));

		currentForm.setConferma(true);
		this.saveToken(request);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);
		currentForm.setTipoOperazione(TipoOperazionePrenotazione.RIFIUTA);

		// se il rifiuto proviene dal modulo utente è una disdetta, altrimenti è un annullamento
		StatoPrenotazionePosto nuovoStato = StatoPrenotazionePosto.DISDETTA;
		if (getOperatore(request) == OperatoreType.BIBLIOTECARIO) {
			nuovoStato = StatoPrenotazionePosto.ANNULLATA;
		}

		pp.setStato(nuovoStato);

		return mapping.getInputForward();
	}

	public ActionForward fruito(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

		currentForm.setConferma(true);
		this.saveToken(request);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);
		currentForm.setTipoOperazione(TipoOperazionePrenotazione.SALVA);

		PrenotazionePostoVO pp = currentForm.getPrenotazione();
		pp.setStato(StatoPrenotazionePosto.FRUITA);

		return mapping.getInputForward();
	}

	public ActionForward conclusa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

		currentForm.setConferma(true);
		this.saveToken(request);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);
		currentForm.setTipoOperazione(TipoOperazionePrenotazione.SALVA);

		PrenotazionePostoVO pp = currentForm.getPrenotazione();
		pp.setStato(StatoPrenotazionePosto.CONCLUSA);

		return mapping.getInputForward();
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;

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

			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarkExists(Bookmark.Servizi.LISTA_MOVIMENTI)) {
				//ritorno a erogazione
				request.setAttribute(NavigazioneServizi.PRENOTAZIONE_POSTO, pp);
				return navi.goToBookmark(Bookmark.Servizi.LISTA_MOVIMENTI, false);
			}

			if (navi.bookmarkExists(Bookmark.Servizi.ModuloWeb.INSERIMENTO_RICHIESTA)) {
				//ritorno a erogazione, senza inserimento reale in bd
				request.setAttribute(NavigazioneServizi.PRENOTAZIONE_POSTO, pp);
				return navi.goToBookmark(Bookmark.Servizi.ModuloWeb.INSERIMENTO_RICHIESTA, false);
			}

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		currentForm.setTipo(TipoGriglia.MESE);
		currentForm.setPulsanti(BOTTONIERA_GRIGLIA);
		preparaGrigliaMese(request, currentForm, currentForm.getParametri(), currentForm.getRicerca().getInizio());
		return mapping.getInputForward();
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		eseguiAggiornamentoPrenotazione(mapping, form, request, response);
		return mapping.getInputForward();
	}

	public ActionForward sif_utente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		ParametriServizi parametri = currentForm.getParametri();
		BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);

		request.setAttribute(BIBLIOTECA_ATTR, bib.getCod_bib());
		request.setAttribute(PATH_CHIAMANTE_ATTR, "ErogazioneRicerca");

		List<UtenteBaseVO> altriUtenti = currentForm.getPrenotazione().getAltriUtenti();
		List<Integer> idUtenti = new ArrayList<Integer>();
		for (UtenteBaseVO u : altriUtenti)
			idUtenti.add(u.getIdUtente());

		request.setAttribute(LISTA_ID_IGNORATI, idUtenti);

		return mapping.findForward("sif_utente");
	}

	public ActionForward rimuoviUtente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		Integer selected = currentForm.getSelectedUtente();
		if (!ValidazioneDati.isFilled(selected))
			return mapping.getInputForward();

		PrenotazionePostoVO pp = currentForm.getPrenotazione();
		Iterator<UtenteBaseVO> i = pp.getAltriUtenti().iterator();
		while (i.hasNext())
			if (i.next().getIdUtente() == selected.intValue())
				i.remove();

		return mapping.getInputForward();
	}

	public ActionForward sposta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		ParametriServizi parametri = currentForm.getParametri();
		//parametri.remove(ParamType.PRENOTAZIONE_POSTO);

		RicercaGrigliaCalendarioVO griglia = new RicercaGrigliaCalendarioVO();
		PrenotazionePostoVO pp = currentForm.getPrenotazione();
		griglia.setUtente(pp.getUtente());
		griglia.getCd_cat_mediazione().add(pp.getCatMediazione());
		griglia.getPrenotazioniEscluse().add(pp);

		parametri.put(ParamType.GRIGLIA_CALENDARIO, griglia);
		parametri.put(ParamType.MODALITA_GESTIONE_PRENOT_POSTO, ModalitaGestioneType.SPOSTA);

		preparaCreaPrenotazione(request, currentForm, parametri, griglia, pp.getTs_inizio() );

		ParametriServizi.send(request, parametri);

		return unspecified(mapping, currentForm, request, response);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		GestionePrenotazionePostoForm currentForm = (GestionePrenotazionePostoForm) form;
		RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();
		TipoGriglia tipo = currentForm.getTipo();
		if (ValidazioneDati.equals(idCheck, "MESE")) {
			return tipo == TipoGriglia.MESE;
		}

		if (ValidazioneDati.equals(idCheck, "SALA")) {
			return tipo == TipoGriglia.SALA;
		}

		if (ValidazioneDati.equals(idCheck, "GIORNO")) {
			return tipo == TipoGriglia.GIORNO;
		}

		boolean dettaglio = (tipo == TipoGriglia.DETTAGLIO);

		if (ValidazioneDati.equals(idCheck, "DETTAGLIO")) {
			return dettaglio;
		}

		if (ValidazioneDati.equals(idCheck, "PRENOTAZIONI_UTENTE")) {
			return tipo == TipoGriglia.PRENOTAZIONI_UTENTE;
		}

		if (ValidazioneDati.equals(idCheck, "PREV_NEXT")) {
			return !ValidazioneDati.in(tipo, TipoGriglia.PRENOTAZIONI_UTENTE, TipoGriglia.DETTAGLIO);
		}

		if (ValidazioneDati.equals(idCheck, "PREV")) {
			switch (tipo) {
			case MESE:
				return LocalDate.fromDateFields(ricerca.getInizio()).isAfter(LocalDate.now());
			case SALA:
			case GIORNO:
			default:
				return false;//LocalDate.fromDateFields(currentForm.getGiorno().getDate()).isAfter(LocalDate.now());
			}
		}

		if (ValidazioneDati.equals(idCheck, "NEXT")) {
			switch (tipo) {
			case MESE:
				return true;//LocalDate.fromDateFields(currentForm.getDataInizioGriglia()).isAfter(LocalDate.now());
			case SALA:
			case GIORNO:
			default:
				return false;//!LocalDate.fromDateFields(currentForm.getGiorno().getDate()).isEqual(LocalDate.fromDateFields(ricerca.getFine()));
			}
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.conferma")) {
			return tipo == TipoGriglia.GIORNO;
		}

		if (ValidazioneDati.equals(idCheck, "DATI_DOCUMENTO")) {
			MovimentoVO mov = ValidazioneDati.first(currentForm.getMovimenti());
			return mov != null && mov.isRichiestaSuInventario();
		}

 		final Timestamp now = DaoManager.now();
		final StatoPrenotazionePosto2 stato = currentForm.getPrenotazione() != null ? SaleUtil.getStatoDinamico(currentForm.getPrenotazione().getStato(), 
				currentForm.getPrenotazione().getTs_fine(), now) : StatoPrenotazionePosto2.IMMESSA;
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.respingi")) {
			return dettaglio
					&& getOperatore(request) == OperatoreType.BIBLIOTECARIO
					&& ValidazioneDati.in(stato,
							StatoPrenotazionePosto2.IMMESSA);
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.prenotazionePosto.disdici")) {
			return dettaglio
					&& getOperatore(request) == OperatoreType.UTENTE
					&& ValidazioneDati.in(stato,
							StatoPrenotazionePosto2.IMMESSA);
		}

		//attivo solo nel giorno stesso della prenotazione
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.fruito")) {
			return dettaglio
					&& getOperatore(request) == OperatoreType.BIBLIOTECARIO
					&& DateUtil.isSameDay(currentForm.getPrenotazione().getTs_inizio(), now)
					&& ValidazioneDati.in(stato,
							StatoPrenotazionePosto2.IMMESSA);
		}
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.conclusa")) {
			return dettaglio
					&& getOperatore(request) == OperatoreType.BIBLIOTECARIO
					//&& DateUtil.isSameDay(currentForm.getPrenotazione().getTs_inizio(), now)
					&& ValidazioneDati.in(stato,
							StatoPrenotazionePosto2.NON_FRUITA,
							StatoPrenotazionePosto2.IN_CORSO);
		}

		if (ValidazioneDati.equals(idCheck, "ALTRI_UTENTI")) {
			try {
				return dettaglio &&
						(currentForm.getPrenotazione().getPosto().getSala().getMaxUtentiPerPrenotazione() > 1 || ValidazioneDati.size(currentForm.getPrenotazione().getAltriUtenti()) > 1)
						// richiesta DDS: l'utente che prenota un posto può essere accompagnato da uno o più utenti
						&& Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SRV_PRENOTAZIONE_POSTO_UTENTI_MULTIPLI,
									Boolean.FALSE.toString()));
			} catch (Exception e) {
				return false;
			}
		}

		if (ValidazioneDati.equals(idCheck, "AGGIUNGI_UTENTE")) {
			if (!ValidazioneDati.in(stato,
					StatoPrenotazionePosto2.IMMESSA,
					StatoPrenotazionePosto2.IN_CORSO))
				return false;

			return dettaglio &&
					(currentForm.getPrenotazione().getPosto().getSala().getMaxUtentiPerPrenotazione() > ValidazioneDati.size(currentForm.getPrenotazione().getAltriUtenti()) );
		}

		if (ValidazioneDati.equals(idCheck, "RIMUOVI_UTENTE")) {
			if (!ValidazioneDati.in(stato,
					StatoPrenotazionePosto2.IMMESSA,
					StatoPrenotazionePosto2.IN_CORSO))
				return false;

			return dettaglio;// && (sala.getMaxUtentiPerPrenotazione() > ValidazioneDati.size(altriUtenti) );
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.torna.sala")) {
			return (tipo == TipoGriglia.GIORNO && ValidazioneDati.size(currentForm.getGiorno().getSlotSala()) > 1);
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.torna.mese")) {
			return (tipo == TipoGriglia.SALA)
					|| (tipo == TipoGriglia.GIORNO && ValidazioneDati.size(currentForm.getGiorno().getSlotSala()) == 1);
		}

		if (ValidazioneDati.equals(idCheck,  "servizi.bottone.prenotazionePosto.sposta")) {
			return dettaglio
					&& getOperatore(request) == OperatoreType.BIBLIOTECARIO
					&& stato == StatoPrenotazionePosto2.IMMESSA;
		}

		if (ValidazioneDati.equals(idCheck,  "SPOSTA")) {
			return currentForm.getParametri().get(ParamType.MODALITA_GESTIONE_PRENOT_POSTO) == ModalitaGestioneType.SPOSTA;
		}

		return true;
	}

}
