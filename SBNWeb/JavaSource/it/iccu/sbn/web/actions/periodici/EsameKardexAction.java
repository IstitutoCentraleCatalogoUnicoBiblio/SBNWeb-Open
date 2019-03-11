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
package it.iccu.sbn.web.actions.periodici;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.KardexPeriodicoDecorator;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.PosizioneFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.DummySerieImplVO;
import it.iccu.sbn.ejb.vo.periodici.esame.RicercaKardexEsameBiblioPoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.util.periodici.PrevisionaleUtil;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.web.actionforms.periodici.EsameKardexForm;
import it.iccu.sbn.web.actionforms.periodici.PeriodiciBaseForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EsameKardexAction extends PeriodiciListaAction implements SbnAttivitaChecker {

	private static String[] BOTTONIERA = new String[] {
		"button.periodici.esamina",
		"button.periodici.polo",
		"button.periodici.biblio",
		"button.periodici.chiudi"
	};

	private static String[] BOTTONIERA_PREVISIONALE = new String[] {
		"button.periodici.previsionale.salva",
		"button.periodici.esamina",
		"button.periodici.inserisci",
		"button.periodici.elimina",
		"button.periodici.chiudi"
	};

	private static String[] BOTTONIERA_DESCRIZIONE_FASCICOLI = new String[] {
		"button.periodici.esamina",
		"button.periodici.inserisci",
		"button.periodici.elimina",
		"button.periodici.previsionale",
		"button.periodici.chiudi"
	};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("button.blocco", "blocco");
		map.put("button.periodici.chiudi", "annulla");
		map.put("button.periodici.polo", "polo");
		map.put("button.periodici.biblio", "biblio");

		//almaviva5_20110520 previsionali
		map.put("button.periodici.inserisci", "inserisci");
		map.put("button.periodici.elimina", "elimina");
		map.put("button.periodici.previsionale.salva", "salva");
		map.put("button.periodici.previsionale", "previsionale");

		map.put("button.periodici.si", "si");
		map.put("button.periodici.no", "no");

		//almaviva5_20110624 #4521
		map.put("button.periodici.esegui", "aggiorna");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		if (currentForm.isInitialized())
			return;

		//la biblioteca é impostata a quella di login
		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();
		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(utente.getCodPolo());
		bib.setCod_bib(utente.getCodBib());
		bib.setNom_biblioteca(utente.getBiblioteca());
		currentForm.setBiblioteca(bib);

		loadDefault(request, currentForm);

		//se vengo da combo esamina gestione bib. non ho in input i parametri e il tipo operazione
		ParametriPeriodici params = ParametriPeriodici.retrieve(request);
		params = params != null ? params : new ParametriPeriodici();
		currentForm.setParametri(params);

		TipoOperazione op = (TipoOperazione) params.get(ParamType.TIPO_OPERAZIONE);
		op = op != null ? op : TipoOperazione.ESAME;
		currentForm.setOperazioneInit(op);
		currentForm.setOperazione(op);

		navi.setTesto(".periodici.esameKardex." + op.name() + ".testo");
		navi.setDescrizioneX(".periodici.esameKardex." + op.name() + ".descrizione");

		switch (op) {
		case ESAME:
			AreePassaggioSifVO sif = (AreePassaggioSifVO) request.getAttribute(TitoliCollegatiInvoke.AreePassaggioSifVO);
			OggettoRiferimentoVO rif = new OggettoRiferimentoVO(true, sif.getOggettoDaRicercare(), sif.getDescOggettoDaRicercare());
			currentForm.setOggettoRiferimento(rif);
			params.put(ParamType.OGGETTO_RIFERIMENTO, rif);

			DatiBibliograficiPeriodicoVO dbp = new DatiBibliograficiPeriodicoVO(
					sif.getOggettoDaRicercare(),
					sif.getDescOggettoDaRicercare(), null);
			params.put(ParamType.DATI_BIBLIOGRAFICI_PER_PERIODICI, dbp);
			break;

		case PREVISIONALE:
			RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) currentForm.getParametri().get(ParamType.PARAMETRI_RICERCA);
			currentForm.setRicercaKardex(ricerca);
			KardexPeriodicoVO kardex = (KardexPeriodicoVO) currentForm.getParametri().get(ParamType.KARDEX_PERIODICO);
			//almaviva5_20101126 #4014 ricavo periodicità da esame periodico
			EsameSeriePeriodicoVO esame = (EsameSeriePeriodicoVO) currentForm.getParametri().get(ParamType.ESAME_SERIE_PERIODICO);
			kardex.setTipoPeriodicita(esame.getCd_per());
			currentForm.setKardex(kardex);
			resetBlocchi(request, currentForm);
			break;

		case DESCRIZIONE_FASCICOLI:
			navi.addBookmark(PeriodiciDelegate.BOOKMARK_DESCRIZIONE_FASCICOLI);
			ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) currentForm.getParametri().get(ParamType.PARAMETRI_RICERCA);
			currentForm.setRicercaKardex(ricerca);
			kardex = (KardexPeriodicoVO) currentForm.getParametri().get(ParamType.KARDEX_PERIODICO);
			//almaviva5_20101126 #4014 ricavo periodicità da esame periodico
			esame = (EsameSeriePeriodicoVO) currentForm.getParametri().get(ParamType.ESAME_SERIE_PERIODICO);
			kardex.setTipoPeriodicita(esame.getCd_per());
			currentForm.setKardex(kardex);
			resetBlocchi(request, currentForm);
			break;
		}

		impostaPulsanti(form, op);

		currentForm.setInitialized(true);
	}

	private void impostaPulsanti(ActionForm form, TipoOperazione op) {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		switch (op) {
		case ESAME:
			currentForm.setPulsanti(BOTTONIERA);
			break;

		case PREVISIONALE:
			currentForm.setPulsanti(BOTTONIERA_PREVISIONALE);
			break;

		case DESCRIZIONE_FASCICOLI:
			currentForm.setPulsanti(BOTTONIERA_DESCRIZIONE_FASCICOLI);
			break;
		}
	}

	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)	throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;

		try {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			String def = (String) utenteEjb.getDefault(ConstantDefault.PER_KARDEX_ELEM_BLOCCHI);
			currentForm.getParamRicerca().setNumeroElementiBlocco(Integer.valueOf(def));
		} catch (Exception e) {
			log.error("Errore caricamento default: ", e);
		}
	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;

		ParametriPeriodici params = currentForm.getParametri();
		TipoOperazione op = currentForm.getOperazione();
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		switch (op) {
		case ESAME:
			DatiBibliograficiPeriodicoVO dbp = (DatiBibliograficiPeriodicoVO) params.get(ParamType.DATI_BIBLIOGRAFICI_PER_PERIODICI);
			RicercaKardexEsameBiblioPoloVO richiesta = currentForm.getParamRicerca().copy();
			richiesta.setBiblioteca(currentForm.getBiblioteca());
			richiesta.setBid(dbp.getBid());
			richiesta.setOggettoRicerca(new DummySerieImplVO());
			richiesta.setComparator(ValidazioneDati.invertiComparatore(PrevisionaleUtil.ORDINAMENTO_PREVISIONALE));
			KardexPeriodicoVO kardex = delegate.getEsameKardexBiblioPolo(currentForm.getBiblioteca(), richiesta);
			if (kardex == null)
				return;

			params.put(ParamType.KARDEX_PERIODICO, kardex);
			params.put(ParamType.PARAMETRI_RICERCA, richiesta);
			currentForm.setRicercaKardex(richiesta);
			currentForm.setKardex(kardex);
			resetBlocchi(request, currentForm);
			break;

		case DESCRIZIONE_FASCICOLI:
		case PREVISIONALE:
			//controllo se il kardex registrato nella form é stato aggiornato
			//in caso affermativo devo ricaricare le info sui blocchi
			KardexPeriodicoVO newKardex = (KardexPeriodicoVO) params.get(ParamType.KARDEX_PERIODICO);
			KardexPeriodicoDecorator oldKardex = (KardexPeriodicoDecorator) currentForm.getKardex();
			currentForm.setKardex(newKardex);
			if (oldKardex != null && oldKardex.older(newKardex)) {
				resetBlocchi(request, currentForm);
				//imposto la selezione sul fascicolo
				Integer id = (Integer) params.getAndRemove(ParamType.POSIZIONA_ID_FASCICOLO);
				if (id != null)	{
					PosizioneFascicoloVO pos = delegate.getPosizioneFascicoloPerId(id, (KardexPeriodicoDecorator) newKardex);
					if (pos != null)
						posizionaSelezione(request, form, pos);
				}
			}
			break;
		}

		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		if (ValidazioneDati.size(fascicoli) == 1 )
			currentForm.setSelected(new Integer[] {fascicoli.get(0).getRepeatableId() });
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();

		try {
			if (ValidazioneDati.in(idCheck,
				"button.periodici.esamina",
				"button.periodici.polo",
				"button.periodici.biblio"))
				return !kardex.isEmpty();

			if (ValidazioneDati.equals(idCheck, "POSIZIONA"))
				return !kardex.isEmpty();

			if (ValidazioneDati.equals(idCheck, "POSIZIONA_BOTTOM")) {
				KardexPeriodicoDecorator kpd = (KardexPeriodicoDecorator) kardex;
				DescrittoreBloccoVO blocco1 = kpd.getBlocco();
				return (blocco1.getTotBlocchi() > 1);
			}

			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

			if (ValidazioneDati.equals(idCheck, "button.periodici.inserisci"))
				return delegate.isAbilitatoDescrizioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.elimina"))
				return !kardex.isEmpty() && delegate.isAbilitatoDescrizioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.previsionale"))
				return delegate.isAbilitatoDescrizioneFascicoli();

			return true;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return esemplari(mapping, form, request, false);
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return esemplari(mapping, form, request, true);
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;

		List<FascicoloVO> nuoviFascicoli = PrevisionaleUtil.selezionaNuoviFascicoli(currentForm.getKardex().getFascicoli());
		if (!ValidazioneDati.isFilled(nuoviFascicoli)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.previsionale.salva"));
			return mapping.getInputForward();
		}

		currentForm.setOperazione(TipoOperazione.SALVA_PREVISIONALE);
		return preparaConferma(request, mapping, currentForm, new ActionMessage("errors.periodici.previsionale.salva.conferma"), null);
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameKardexForm currentForm = (EsameKardexForm) form;
		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		//almaviva5_20111028 #4705
		String cod_bib = currentForm.getKardex().getBiblioteca().getCod_bib();
		if (!delegate.checkFascicoliPrevistiPerCancellazione(cod_bib, ff) )
			return mapping.getInputForward();

		currentForm.setOperazione(TipoOperazione.CANCELLA);
		return preparaConferma(request, mapping, currentForm, new ActionMessage("errors.periodici.cancella.conferma"), null);
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		currentForm.getParametri().put(ParamType.OGGETTO_RIFERIMENTO, currentForm.getOggettoRiferimento());
		TipoOperazione op = currentForm.getOperazione();
		//almaviva5_20110523 il previsionale gestisce anche fascicoli non salvati in base dati.
		switch (op) {
		case ESAME:
			return super.internalEsamina(mapping, form, request, response, TipoOperazione.ESAME, true);
		case DESCRIZIONE_FASCICOLI:
			return super.internalEsamina(mapping, form, request, response, TipoOperazione.GESTIONE_FASCICOLO_NO_AMM, true);
		case PREVISIONALE:
			return super.internalEsamina(mapping, form, request, response, TipoOperazione.PREVISIONALE, false);
		}

		return mapping.getInputForward();
	}

	public ActionForward posiziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		EsameKardexForm currentForm = (EsameKardexForm) form;
//		if (currentForm.getOperazione() == TipoOperazione.PREVISIONALE) {
//			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
//			return mapping.getInputForward();
//		}

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = currentForm.getRicercaKardex();
		KardexPeriodicoDecorator kardex = (KardexPeriodicoDecorator) currentForm.getKardex();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		PosizioneFascicoloVO pos = delegate.getPosizioneFascicoloPerData(ricerca.getDataScroll(), kardex);
		if (pos == null)
			return mapping.getInputForward();

		posizionaSelezione(request, currentForm, pos);

		return mapping.getInputForward();
	}

	@Override
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameKardexForm currentForm = (EsameKardexForm) form;
		return internalBlocco(mapping, form, request, response, currentForm.getRicercaKardex().getComparator());
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)  throws Exception {

		EsameKardexForm currentForm = (EsameKardexForm) form;
		currentForm.setConferma(false);

		switch (currentForm.getOperazione()) {
		case SALVA_PREVISIONALE:
			return eseguiSalvaPrevisionale(mapping, form, request, response);
		case CANCELLA:
			return eseguiCancellazioneMultipla(mapping, currentForm, request, response);

		default:
			return no(mapping, currentForm, request, response);
		}
	}

	private ActionForward eseguiSalvaPrevisionale(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EsameKardexForm currentForm = (EsameKardexForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		List<FascicoloVO> fascicoli = kardex.getFascicoli();
		List<FascicoloVO> nuoviFascicoli = PrevisionaleUtil.selezionaNuoviFascicoli(fascicoli);
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		List<FascicoloVO> ff = delegate.aggiornaFascicolo(null, nuoviFascicoli);
		if (ff == null)
			return annullaConferma(mapping, currentForm);

		for (FascicoloVO f : ff) {
			FascicoloDecorator fd = new FascicoloDecorator(f);
			int pos = UniqueIdentifiableVO.indexOfRepeatableId(fd.getRepeatableId(), fascicoli);
			if (pos > -1) {
				fascicoli.remove(pos);
				fascicoli.add(pos, fd);
			}
		}

		KardexPeriodicoVO newKardex = delegate.duplicaKardexPrevisionale(currentForm.getRicercaKardex(), kardex);
		currentForm.setKardex(newKardex);
		currentForm.getParametri().put(ParamType.KARDEX_PERIODICO, newKardex);
		resetBlocchi(request, currentForm);

		//tutto ok
		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		Navigation navi = Navigation.getInstance(request);
		//se il previsionale é stato attivato da una ricerca per fascicoli torno alla lista
		//dei fascicoli aggiornata.
		if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_DESCRIZIONE_FASCICOLI)) {
			NavigationElement e = navi.getCache().getElementAtBookmark(PeriodiciDelegate.BOOKMARK_DESCRIZIONE_FASCICOLI);
			if (e != null) {
				PeriodiciBaseForm prev = (PeriodiciBaseForm) e.getForm();
				prev.getParametri().put(ParamType.KARDEX_PERIODICO, newKardex);
				NavigationForward fwd = navi.goToBookmark(PeriodiciDelegate.BOOKMARK_DESCRIZIONE_FASCICOLI, false);
				fwd.setRedirect(true);
				return fwd;
			}
		}

		return annullaConferma(mapping, currentForm);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return annullaConferma(mapping, form);
	}

	private ActionForward annullaConferma(ActionMapping mapping, ActionForm form) {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		currentForm.setConferma(false);
		currentForm.setInventario(null);
		currentForm.setOperazione(currentForm.getOperazioneInit());
		impostaPulsanti(currentForm, currentForm.getOperazioneInit());

		return mapping.getInputForward();
	}

	private ActionForward eseguiCancellazioneMultipla(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EsameKardexForm currentForm = (EsameKardexForm) form;
		List<FascicoloVO> ff = getSelectedItems(request, form);

		//si eliminano immediatamente i fascicoli non salvati
		Iterator<FascicoloVO> i = ff.iterator();
		while (i.hasNext())
			if (i.next().isNuovo()) i.remove();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		boolean ok = delegate.cancellazioneMultiplaFascicoli(ff);
		if (!ok)
			return annullaConferma(mapping, form);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		switch (currentForm.getOperazioneInit()) {
		case ESAME:
		case DESCRIZIONE_FASCICOLI:
			//aggiorna kardex
			return aggiorna(mapping, currentForm, request, response);
		case PREVISIONALE:
			List<FascicoloVO> deleted = getSelectedItems(request, form);
			KardexPeriodicoVO kardex = currentForm.getKardex();
			List<FascicoloVO> fascicoli = kardex.getFascicoli();
			for (FascicoloVO f : deleted)
				fascicoli.remove(f);

			KardexPeriodicoVO newKardex = delegate.duplicaKardexPrevisionale(currentForm.getRicercaKardex(), kardex);
			currentForm.setKardex(newKardex);
			currentForm.getParametri().put(ParamType.KARDEX_PERIODICO, newKardex);
			resetBlocchi(request, currentForm);

			return annullaConferma(mapping, form);
		}

		return annullaConferma(mapping, form);
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,	HttpServletResponse response) throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		ParametriPeriodici parametri = currentForm.getParametri();
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = currentForm.getRicercaKardex();
		KardexPeriodicoVO kardex = null;
		switch (currentForm.getOperazioneInit()) {
		case ESAME:
			kardex = delegate.getEsameKardexBiblioPolo(currentForm.getBiblioteca(), (RicercaKardexEsameBiblioPoloVO) ricerca);
			break;
		case DESCRIZIONE_FASCICOLI:
		case PREVISIONALE:
			kardex = delegate.getKardexPeriodico(currentForm.getBiblioteca(), ricerca);
			break;
		}
		if (kardex == null)
			return annullaConferma(mapping, form);

		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		currentForm.setKardex(kardex);

		resetBlocchi(request, currentForm);

		return annullaConferma(mapping, form);
	}

	@Override
	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;
		switch (currentForm.getOperazione()) {
		case DESCRIZIONE_FASCICOLI:
			return internalInserisci(mapping, form, request, response, TipoOperazione.NUOVO_FASCICOLO_NO_AMM);
		case PREVISIONALE:
			return internalInserisci(mapping, form, request, response, TipoOperazione.NUOVO_FASCICOLO_PREVISTO);
		}

		return mapping.getInputForward();
	}

	public ActionForward previsionale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		EsameKardexForm currentForm = (EsameKardexForm) form;

		// se ho selezionato un fascicolo dalla sintetica recupero i dati da
		// quest'ultimo
		List<FascicoloVO> items = getSelectedItems(request, currentForm);
		int size = ValidazioneDati.size(items);

		if (size > 1) { // il fascicolo "modello" deve essere solo uno
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.selezione.singola"));
			return mapping.getInputForward();
		}

		FascicoloVO selected = size > 0 ? items.get(0) : null;
		RicercaKardexPrevisionaleVO calc = new RicercaKardexPrevisionaleVO();
		ModelloPrevisionaleVO modello = calc.getModello();

		try {
			if (selected != null) {
				List<FascicoloVO> fascicoli = currentForm.getFascicoli();
				int idx = UniqueIdentifiableVO.indexOfRepeatableId(selected.getRepeatableId(), fascicoli);
				modello.setCd_per(selected.getCd_per());
				//calc.setData_conv_pub(PeriodiciUtil.calcolaDataProssimoFascicolo(fascicoli.subList(idx, fascicoli.size())));
				calc.setData_conv_pub(PeriodiciUtil.calcolaDataProssimoFascicolo(ValidazioneDati.asSingletonList(fascicoli.get(idx))));
				modello.setNum_primo_volume(ValidazioneDati.isFilled(selected.getNum_volume()) ? selected.getNum_volume().toString() : "" );
				String annata = selected.getAnnata();
				if (ValidazioneDati.isFilled(annata) && annata.matches("\\d{4}/\\d{4}")) {
					String[] anno = annata.split("/");
					calc.setAnnataFrom(anno[0]);
					calc.setAnnataTo(anno[1]);
				}
				modello.setNum_primo_fascicolo(PeriodiciUtil.formattaNumeroFascicolo(selected));
			} else {
				calc.setData_conv_pub(new Date());
				EsameSeriePeriodicoVO esame = (EsameSeriePeriodicoVO) currentForm.getParametri().get(ParamType.ESAME_SERIE_PERIODICO);
				modello.setCd_per(esame.getCd_per());
			}

			calc.setPosizione_primo_fascicolo(1);

			ParametriPeriodici params = currentForm.getParametri().copy();
			params.put(ParamType.PARAMETRI_PREVISIONALE, calc);
			ParametriPeriodici.send(request, params);

			LinkableTagUtils.resetErrors(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("previsionale"));

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}
	}

}
