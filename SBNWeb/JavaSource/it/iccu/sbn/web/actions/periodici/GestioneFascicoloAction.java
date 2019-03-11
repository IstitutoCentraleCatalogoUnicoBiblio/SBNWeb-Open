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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexIntestazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.custom.periodici.InventarioOrdineDecorator;
import it.iccu.sbn.web.actionforms.periodici.GestioneFascicoloForm;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class GestioneFascicoloAction extends PeriodiciListaAction implements SbnAttivitaChecker {

	private static String[] BOTTONIERA = new String[] {
		"button.periodici.back",
		"button.periodici.forward",
		"button.periodici.salva",
		"button.periodici.ins.ulteriori",
		"button.periodici.associa.inv",
		"button.periodici.cambia.inv",
		"button.periodici.associa.ord",
		"button.periodici.cambia.ord",
		"button.periodici.ricevi",
		"button.periodici.annulla.ricezione",
		"button.periodici.annulla.associa",
		"button.periodici.elimina",
		"button.periodici.chiudi" };

	private static String[] BOTTONIERA_NUOVO = new String[] {
		"button.periodici.salva",
		"button.periodici.annulla" };

	private static String[] BOTTONIERA_ESAME = new String[] {
		"button.periodici.back",
		"button.periodici.forward",
		"button.periodici.chiudi" };

	private static String[] BOTTONIERA_PREVISIONALE = new String[] {
		"button.periodici.back",
		"button.periodici.forward",
		"button.periodici.salva",
		"button.periodici.previsionale.acquisisci",
		"button.periodici.esemplari",
		"button.periodici.elimina",
		"button.periodici.chiudi" };

	private static String[] BOTTONIERA_RICEVI = new String[] {
		"button.periodici.ricevi.conferma",
		"button.periodici.associa.inv",
		"button.periodici.cambia.inv",
		"button.periodici.associa.ord",
		"button.periodici.cambia.ord",
		"button.periodici.annulla" };

	private static String[] BOTTONIERA_CONFERMA = new String[] {
		"button.periodici.si",
		"button.periodici.no",
		"button.periodici.annulla" };

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		try {
			GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
			FascicoloVO f = currentForm.getFascicolo();
			EsemplareFascicoloVO ef = f.getEsemplare();
			TipoOperazione op = currentForm.getOperazione();

			//recupero l'eventuale catena rinnovi per la serie in esame
			ParametriPeriodici parametri = currentForm.getParametri();
			RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) parametri.get(ParamType.PARAMETRI_RICERCA);
			KardexPeriodicoVO kardex = currentForm.getKardex();
			SeriePeriodicoType type = kardex.getTipo();
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

			//almaviva5_20111007 #4653
			if (ValidazioneDati.equals(idCheck, PeriodiciConstants.KARDEX_RANGE) )
				return false;

			//almaviva5_20110216 #3986 fascicoli multipli
			if (ValidazioneDati.in(idCheck, "button.periodici.forward", "button.periodici.back"))
				return (ValidazioneDati.size(currentForm.getFascicoli()) > 1);

			if (ValidazioneDati.equals(idCheck, PeriodiciConstants.DESCRIZIONE_FASCICOLO)) {
				FascicoloDecorator fd = (FascicoloDecorator) f;
				return op != TipoOperazione.ESAME
					&& delegate.isAbilitatoDescrizioneFascicoli()
					&& PeriodiciUtil.isFascicoloModificabile(kardex.getBiblioteca().getCod_bib(), fd);
			}

			if (ValidazioneDati.equals(idCheck, PeriodiciConstants.DATA_CONVENZIONALE)) {
				//la data conv. é modificabile se non esiste altra biblioteca che ha
				//ricevuto il fid (stessa condizione della cancellazione)
				FascicoloDecorator fd = (FascicoloDecorator) f;
				return op != TipoOperazione.ESAME
					&& delegate.isAbilitatoDescrizioneFascicoli()
					//almaviva5_20110509 #4435
					//&& PeriodiciUtil.isFascicoloCancellabile(kardex.getBiblioteca().getCod_bib(), fd);
					&& PeriodiciUtil.isFascicoloModificabile(kardex.getBiblioteca().getCod_bib(), fd);
			}

			//almaviva5_20121106 #58 LIG
			if (ValidazioneDati.equals(idCheck, PeriodiciConstants.BID_COLLOCAZIONE) )
				return !ValidazioneDati.equals(f.getBid(), kardex.getBid() );

			if (ValidazioneDati.in(op, TipoOperazione.PREVISIONALE,
					TipoOperazione.GESTIONE_FASCICOLO_NO_AMM,
					TipoOperazione.NUOVO_FASCICOLO_NO_AMM,
					TipoOperazione.NUOVO_FASCICOLO_PREVISTO) )
				return checkAttivitaPrevisionale(request, currentForm, idCheck);

			if (ValidazioneDati.equals(idCheck, "AMMINISTRAZIONE"))
				return  op != TipoOperazione.PREVISIONALE
					&& delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.salva"))
				return delegate.isAbilitatoAmministrazioneFascicoli()
					|| delegate.isAbilitatoDescrizioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.ricevi"))
				return op == TipoOperazione.GESTIONE_FASCICOLO &&
					type == SeriePeriodicoType.ORDINE &&
					!f.isNuovo() && !StatoFascicolo.isPosseduto(f.getStato()) &&
					delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.ricevi.conferma"))
				return op == TipoOperazione.RICEVI &&
					!f.isNuovo() && StatoFascicolo.isPosseduto(f.getStato()) &&
					delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.annulla.ricezione"))
				return op == TipoOperazione.GESTIONE_FASCICOLO &&
					(!f.isNuovo() && StatoFascicolo.isPosseduto(f.getStato()))
					//&& type == SeriePeriodicoType.ORDINE
					&& ef.isLegatoOrdine() && !ef.isLegatoInventario()
					&& delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.annulla.associa"))
				return op == TipoOperazione.GESTIONE_FASCICOLO &&
					(!f.isNuovo() && StatoFascicolo.isPosseduto(f.getStato()))
					//&& type != SeriePeriodicoType.ORDINE
					&& ef.isLegatoInventario()
					&& delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.elimina")) {
				FascicoloDecorator fd = (FascicoloDecorator) f;
				return op == TipoOperazione.GESTIONE_FASCICOLO && !f.isNuovo()
					&& delegate.isAbilitatoDescrizioneFascicoli()
					&& PeriodiciUtil.isFascicoloCancellabile(kardex.getBiblioteca().getCod_bib(), fd);
			}

			if (ValidazioneDati.equals(idCheck, "button.periodici.associa.inv")) {
				switch (type) {
				case ESEMPLARE:
				case COLLOCAZIONE:
					return op != TipoOperazione.NUOVO_FASCICOLO
						//&& StatoFascicolo.isPosseduto(f.getStato())
						&& (ef == null || (!ef.isLegatoInventario() || ef.isCancellato()))
						&& delegate.isAbilitatoAmministrazioneFascicoli();

				case ORDINE:
					return op != TipoOperazione.NUOVO_FASCICOLO
						&& StatoFascicolo.isPosseduto(f.getStato())
						&& !ef.isLegatoInventario()
						&& delegate.isAbilitatoAmministrazioneFascicoli();
				}
			}

			if (ValidazioneDati.equals(idCheck, "button.periodici.cambia.inv")) {
				switch (type) {
				case ESEMPLARE:
				case COLLOCAZIONE:
					return op != TipoOperazione.NUOVO_FASCICOLO
						&& StatoFascicolo.isPosseduto(f.getStato())
						&& ef.isLegatoInventario()
						&& !ef.isLegatoOrdine()
						&& delegate.isAbilitatoAmministrazioneFascicoli();
				case ORDINE:
					return op != TipoOperazione.NUOVO_FASCICOLO
						&& StatoFascicolo.isPosseduto(f.getStato())
						&& ef.isLegatoInventario()
						&& ef.isLegatoOrdine()
						&& delegate.isAbilitatoAmministrazioneFascicoli();
				}

			}

			ElementoSeriePeriodicoVO esp = (ElementoSeriePeriodicoVO) ricerca.getOggettoRicerca().getParent();
			SerieOrdineVO ordine = esp != null ? esp.getOrdine() : null; //ordine più recente su kardex esempl.\coll.

			if (ValidazioneDati.equals(idCheck, "button.periodici.associa.ord"))
				return !ValidazioneDati.in(op, TipoOperazione.NUOVO_FASCICOLO, TipoOperazione.PREVISIONALE)
					&& StatoFascicolo.isPosseduto(f.getStato())
					&& !ef.isLegatoOrdine()
					&& type == SeriePeriodicoType.ORDINE
					&& ordine != null
					&& delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.cambia.ord"))
				return !ValidazioneDati.in(op, TipoOperazione.NUOVO_FASCICOLO, TipoOperazione.PREVISIONALE)
					&& StatoFascicolo.isPosseduto(f.getStato())
					&& ef.isLegatoOrdine()
					&& !ef.isLegatoInventario()
					&& type == SeriePeriodicoType.ORDINE
					&& ordine != null
					&& delegate.isAbilitatoAmministrazioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.ins.ulteriori")) {
				/*
				return currentForm.getOperazione() == TipoOperazione.GESTIONE_FASCICOLO
					&& !f.isNuovo()
					&& !ValidazioneDati.isFilled(f.getNum_alter())
					&& !FascicoloValidator.isMultiplo(f);
				*/
				return false;
			}


		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;
	}

	private boolean checkAttivitaPrevisionale(HttpServletRequest request, ActionForm form, String idCheck) {

		try {
			GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
			KardexPeriodicoVO kardex = currentForm.getKardex();
			FascicoloDecorator f = (FascicoloDecorator) currentForm.getFascicolo();
			TipoOperazione op = currentForm.getOperazione();

			//recupero l'eventuale catena rinnovi per la serie in esame
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

			if (ValidazioneDati.equals(idCheck, "button.periodici.salva"))
				return (!f.isNuovo() || op == TipoOperazione.NUOVO_FASCICOLO_NO_AMM)
					&& delegate.isAbilitatoDescrizioneFascicoli()
					&& PeriodiciUtil.isFascicoloModificabile(kardex.getBiblioteca().getCod_bib(), f);

			if (ValidazioneDati.equals(idCheck, "button.periodici.previsionale.acquisisci"))
				return f.isNuovo() && delegate.isAbilitatoDescrizioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.esemplari")) {
				FascicoloDecorator fd = new FascicoloDecorator(f);
				return !f.isNuovo() && ValidazioneDati.isFilled(fd.getListaBibRicezione());
			}

			if (ValidazioneDati.equals(idCheck, "button.periodici.elimina")) {
				FascicoloDecorator fd = f;
				return delegate.isAbilitatoDescrizioneFascicoli()
					&& (f.isNuovo() ||
							(!f.isNuovo() && PeriodiciUtil.isFascicoloCancellabile(kardex.getBiblioteca().getCod_bib(), fd)));
			}

			if (ValidazioneDati.in(idCheck, "AMMINISTRAZIONE",
					"button.periodici.ricevi",
					"button.periodici.ricevi.conferma",
					"button.periodici.annulla.ricezione",
					"button.periodici.annulla.associa"))
				return false;



		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;

	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.periodici.annulla", "annulla");
		map.put("button.periodici.chiudi", "annulla");
		map.put("button.periodici.salva", "salva");
		map.put("button.periodici.elimina", "elimina");
		map.put("button.periodici.associa.inv", "associaInventario");
		map.put("button.periodici.cambia.inv", "associaInventario");
		map.put("button.periodici.associa.ord", "associaOrdine");
		map.put("button.periodici.cambia.ord", "associaOrdine");

		map.put("button.periodici.ricevi", "preparaRicevi");
		map.put("button.periodici.ricevi.conferma", "conferma");
		map.put("button.periodici.annulla.ricezione", "annullaRicezione");
		map.put("button.periodici.annulla.associa", "annullaRicezione");
		map.put("button.periodici.ins.ulteriori", "ulterioriFascicoli");

		map.put("button.periodici.si", "si");
		map.put("button.periodici.no", "no");

		//almaviva5_20110216 #3986 fascicoli multipli
		map.put("button.periodici.back", "back");
		map.put("button.periodici.forward", "forward");

		map.put("button.periodici.previsionale.acquisisci", "acquisisci");
		map.put("button.periodici.esemplari", "esemplari");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {

		//bookmark per ritorno da sif inventari
		Navigation.getInstance(request).addBookmark(PeriodiciDelegate.BOOKMARK_FASCICOLO);

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		if (currentForm.isInitialized())
			return;

		currentForm.setListaTipoFascicolo(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_FASCICOLI));
		currentForm.setListaPeriodicita(CodiciProvider.getCodici(CodiciType.CODICE_PERIODICITA));

		ParametriPeriodici parametri = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(parametri);

		KardexPeriodicoVO kardex = (KardexPeriodicoVO) parametri.get(ParamType.KARDEX_PERIODICO);
		currentForm.setKardex(kardex);
		currentForm.setBiblioteca(kardex.getBiblioteca());
		FascicoloVO f = (FascicoloVO) parametri.get(ParamType.DETTAGLIO_FASCICOLO);

		//almaviva5_20110216 #3986 fascicoli multipli
		if (f == null) {
			List<FascicoloVO> fascicoli = (List<FascicoloVO>) parametri.get(ParamType.LISTA_FASCICOLI);
			currentForm.setFascicoli(fascicoli);
			f = ValidazioneDati.first(fascicoli).copy();
		} else
			currentForm.setFascicoli(ValidazioneDati.asSingletonList(f));

		currentForm.setFascicolo(f);

		TipoOperazione op = (TipoOperazione) parametri.get(ParamType.TIPO_OPERAZIONE);
		currentForm.setOperazioneInit(op);
		currentForm.setOperazione(op);

		switch(op) {
		case ESAME:
			currentForm.setOggettoRiferimento((OggettoRiferimentoVO) parametri.get(ParamType.OGGETTO_RIFERIMENTO));
			break;

		case RICEVI:
		case ANNULLA_RICEZIONE:
		case GESTIONE_FASCICOLO:
			currentForm.setOldFascicolo((FascicoloVO) f.clone());
			break;

		case PREVISIONALE:
		case GESTIONE_FASCICOLO_NO_AMM:
		case NUOVO_FASCICOLO_PREVISTO:
			currentForm.setOggettoRiferimento((OggettoRiferimentoVO) parametri.get(ParamType.OGGETTO_RIFERIMENTO));
			currentForm.setOldFascicolo((FascicoloVO) f.clone());
			break;
		}

		impostaPulsanti(form, op);

		currentForm.setInitialized(true);
	}

	private void impostaPulsanti(ActionForm form, TipoOperazione op) {
		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		switch(op) {
		case NUOVO_FASCICOLO:
		case NUOVO_FASCICOLO_NO_AMM:
			currentForm.setPulsanti(BOTTONIERA_NUOVO);
			break;
		case ESAME:
			currentForm.setPulsanti(BOTTONIERA_ESAME);
			break;
		case RICEVI:
		case ANNULLA_RICEZIONE:
		case GESTIONE_FASCICOLO:
			currentForm.setPulsanti(BOTTONIERA);
			break;
		case PREVISIONALE:
		case GESTIONE_FASCICOLO_NO_AMM:
		case NUOVO_FASCICOLO_PREVISTO:
			currentForm.setPulsanti(BOTTONIERA_PREVISIONALE);
			break;
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		TipoOperazione op = currentForm.getOperazione();

		switch (op) {
		case RICEVI:
			InventarioVO inv = (InventarioVO) request.getAttribute(PeriodiciDelegate.SIF_INVENTARIO);
			if (inv == null)
				inv = currentForm.getInventario();
			if (inv == null) {	//solo se non sto tornando da sif inventari
				currentForm.setOperazione(TipoOperazione.GESTIONE_FASCICOLO);
				return preparaRicevi(mapping, currentForm, request, response);
			}
			break;
		case ANNULLA_RICEZIONE:
			currentForm.setOperazione(TipoOperazione.GESTIONE_FASCICOLO);
			return annullaRicezione(mapping, currentForm, request, response);
		}

		return mapping.getInputForward();
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) {
		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;

		FascicoloVO f = currentForm.getFascicolo();
		if (f.isNuovo() ) {
			if (!ValidazioneDati.isFilled(f.getCd_per())) {
				KardexPeriodicoVO kardex = currentForm.getKardex();
				f.setCd_per(kardex.getTipoPeriodicita());	//periodicità presunta
			}
			//almaviva5_20101110 #3980 default a semplice
			if (!ValidazioneDati.isFilled(f.getCd_tipo_fasc()))
				f.setCd_tipo_fasc("S");
		}

		//sto ricevendo su inventario
		InventarioVO inv = (InventarioVO) request.getAttribute(PeriodiciDelegate.SIF_INVENTARIO);
		if (inv == null)
			inv = currentForm.getInventario();

		PeriodiciUtil.associaInventario(f, inv);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		currentForm.setConferma(false);

		switch (currentForm.getOperazione()) {
		case ANNULLA_RICEZIONE:
		case CANCELLA:
			FascicoloVO f = currentForm.getFascicolo();
			if (f.isNuovo()) { //previsionale?
				FascicoloDecorator fd = new FascicoloDecorator(f);
				fd.setFlCanc("S");
				currentForm.setFascicolo(fd);
				currentForm.getFascicoli().set(currentForm.getPosizione(), fd);
				return goBackWithReloadPrevisionale(request, mapping, currentForm);
			}

			FascicoloVO newFascicolo = eseguiAggiornamento(mapping, form, request, response);
			if (newFascicolo == null)
				return no(mapping, currentForm, request, response);

			currentForm.setFascicolo(newFascicolo);
			//ok ricarico lista
			return goBackWithReload(request, mapping, form);

		case ULTERIORI_FASCICOLI:
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
			return mapping.getInputForward();

		default:
			return no(mapping, currentForm, request, response);
		}

	}

	private ActionForward goBackWithReload(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) throws Exception, ValidationException {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		ParametriPeriodici parametri = currentForm.getParametri();
		//controllo che il fascicolo sia cambiato da quando é stato caricato nella form
		//almaviva5_20110216 #3986 fascicoli multipli
		boolean modified = false;
		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		if (ValidazioneDati.size(fascicoli) > 1)
			for (FascicoloVO f : fascicoli) {
				modified = (!f.isNuovo() && f.getTsVar().after(currentForm.getCreationTime()));
				if (modified)
					break;
			}
		else {	//fascicolo singolo
			FascicoloVO f = currentForm.getFascicolo();
			modified = (!f.isNuovo() && f.getTsVar().after(currentForm.getCreationTime()));
			parametri.put(ParamType.POSIZIONA_ID_FASCICOLO, f.getRepeatableId());
		}

		if (!modified)
			return Navigation.getInstance(request).goBack(true);

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) parametri.get(ParamType.PARAMETRI_RICERCA);

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		KardexPeriodicoVO kardex = delegate.getKardexPeriodico(currentForm.getBiblioteca(), ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		ParametriPeriodici.send(request, parametri);

		return Navigation.getInstance(request).goBack();
	}

	private ActionForward goBackWithReloadPrevisionale(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) throws Exception, ValidationException {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		ParametriPeriodici parametri = currentForm.getParametri();
		TipoOperazione op = currentForm.getOperazione();
		Map<Integer, Integer> posizioni = (Map<Integer, Integer>) parametri.get(ParamType.POSIZIONE_LISTA_FASCICOLI);
		//controllo che il fascicolo sia cambiato da quando é stato caricato nella form
		//almaviva5_20110216 #3986 fascicoli multipli
		boolean modified = false;

		KardexPeriodicoVO kardex = currentForm.getKardex();
		//nel caso del previsionale la lista fascicoli ha tutti i fascicoli calcolati
		List<FascicoloVO> fascicoliCalcolati = kardex.getFascicoli();
		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		int size = ValidazioneDati.size(fascicoli);

		if (op == TipoOperazione.NUOVO_FASCICOLO_PREVISTO) {
			modified = true;
			fascicoliCalcolati.add(currentForm.getFascicolo());	//nuovo fascicolo in coda
		} else
			for (int pos = 0; pos < size; pos++) {
				FascicoloVO f = fascicoli.get(pos);
				if (f.getCreationTime().after(currentForm.getCreationTime())) {
					modified = true;
					Integer originalPos = posizioni != null ? posizioni.get(pos) : 0;
					//cancello vecchio fascicolo
					if (originalPos != null && originalPos > -1) {
						fascicoliCalcolati.remove(originalPos.intValue());
						if (!f.isCancellato())
							fascicoliCalcolati.add(originalPos.intValue(), f);
					}
				}
			}

		if (size == 1) {	//fascicolo singolo
			FascicoloVO f = currentForm.getFascicolo();
			modified = modified || (!f.isNuovo() && f.getCreationTime().after(currentForm.getCreationTime()));
			parametri.put(ParamType.POSIZIONA_ID_FASCICOLO, f.getRepeatableId());
		}

		if (!modified)
			return Navigation.getInstance(request).goBack(true);

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) parametri.get(ParamType.PARAMETRI_RICERCA);
		BaseVO.sortAndEnumerate(fascicoliCalcolati, ricerca.getComparator());

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		KardexPeriodicoVO newKardex = delegate.duplicaKardexPrevisionale(ricerca, kardex);
		if (newKardex == null)
			return mapping.getInputForward();

		parametri.put(ParamType.KARDEX_PERIODICO, newKardex);
		ParametriPeriodici.send(request, parametri);

		return Navigation.getInstance(request).goBack();
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		FascicoloVO f = currentForm.getFascicolo();
		currentForm.setFascicoloConferma((FascicoloVO) f.clone());
		//imposto i flag di cancellazione su fascicolo ed esemplare (se presente)
		f.setFlCanc("S");
		EsemplareFascicoloVO ef = f.getEsemplare();
		if (ef != null) {
			ef.setData_arrivo(null);
			ef.setFlCanc("S");
		}

		currentForm.setOperazione(TipoOperazione.CANCELLA);

		return preparaConferma(request, mapping, currentForm);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		currentForm.setConferma(false);
		currentForm.setFascicolo((FascicoloVO) currentForm.getFascicoloConferma().clone() );
		currentForm.setOperazione(currentForm.getOperazioneInit());
		impostaPulsanti(currentForm, currentForm.getOperazioneInit());

		return mapping.getInputForward();
	}

	public ActionForward annullaRicezione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		FascicoloVO f = currentForm.getFascicolo();
		currentForm.setFascicoloConferma((FascicoloVO) f.clone());

		EsemplareFascicoloVO ef = f.getEsemplare();
		//ef.setId_ordine(0);
		//ef.setCd_inven(0);
		ef.setData_arrivo(null);
		ef.setFlCanc("S");

		currentForm.setOperazione(TipoOperazione.ANNULLA_RICEZIONE);

		return preparaConferma(request, mapping, form);
	}

	private ActionForward preparaConferma(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		currentForm.setConferma(true);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);
		//currentForm.setFascicoloConferma((FascicoloVO) currentForm.getFascicolo().clone());
		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));
		return mapping.getInputForward();
	}

	public ActionForward preparaRicevi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		KardexIntestazioneVO intestazione = currentForm.getKardex().getIntestazione();
		SeriePeriodicoType tipo = intestazione.getTipo();
		FascicoloVO f = currentForm.getFascicolo();

		EsemplareFascicoloVO ef;
		switch (currentForm.getOperazione()) {
		case GESTIONE_FASCICOLO:	// devo impostare i dati di ricezione
			f = PeriodiciUtil.preparaFascicoloPerRicezione(f);
			ef = f.getEsemplare();

			currentForm.setPulsanti(BOTTONIERA_RICEVI);
			currentForm.setOperazione(TipoOperazione.RICEVI);
			if (tipo == SeriePeriodicoType.ORDINE) {
				SerieOrdineVO o = intestazione.getOrdine();
				ef.setId_ordine(o.getId_ordine());
				ef.setCod_bib_ord(o.getCod_bib_ord());
				ef.setAnno_ord(o.getAnno_ord());
				ef.setCod_tip_ord(o.getCod_tip_ord());
				ef.setCod_ord(o.getCod_ord());

				//controllo se devo invocare il sif di scelta inventario
				return super.internalAssociaInventarioFascicolo(mapping, currentForm, request, response,
						ValidazioneDati.asSingletonList(f));
			}

			ParametriPeriodici parametri = currentForm.getParametri();
			InventarioVO inv = (InventarioVO) parametri.get(ParamType.INVENTARIO);
			PeriodiciUtil.associaInventario(f, inv);
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		FascicoloVO f = currentForm.getFascicolo();

		KardexPeriodicoVO kardex = currentForm.getKardex();
		switch (currentForm.getOperazione()) {
		case RICEVI:
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			//check su inventario ordine
			if (kardex.getTipo() == SeriePeriodicoType.ORDINE) {
				EsemplareFascicoloVO ef = f.getEsemplare();
				if (!ef.isLegatoInventario()) {
					InventarioVO inv = delegate.getUnicoInventario(kardex.getIntestazione().getSerie());
					InventarioOrdineDecorator iod = (InventarioOrdineDecorator) inv;
					if (iod.getCount() != 0) {
						LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.PER_ASSOCIARE_INVENTARIO.getErrorMessage()));
						return mapping.getInputForward();
					}
				}
			}

			f = delegate.riceviFascicolo(kardex, f);
			if (f != null) {
				//tutto ok
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
				currentForm.setFascicolo(f);
				currentForm.setOldFascicolo((FascicoloVO) f.clone());
				currentForm.setPulsanti(BOTTONIERA);
				currentForm.setOperazione(TipoOperazione.GESTIONE_FASCICOLO);
				//almaviva5_20110216 #3986 sostituzione fascicolo in lista fascicoli multipli
				if (ValidazioneDati.isFilled(currentForm.getFascicoli()))
					currentForm.getFascicoli().set(currentForm.getPosizione(), (FascicoloVO) f.clone());
			}
		}

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		switch (currentForm.getOperazione()) {
		case NUOVO_FASCICOLO:
		case GESTIONE_FASCICOLO:
		case GESTIONE_FASCICOLO_NO_AMM:
		case CANCELLA:
			return goBackWithReload(request, mapping, form);

		case PREVISIONALE:
			return goBackWithReloadPrevisionale(request, mapping, form);

		case RICEVI:
			currentForm.setFascicolo((FascicoloVO) currentForm.getOldFascicolo().clone());
			currentForm.setOperazione(TipoOperazione.GESTIONE_FASCICOLO);
			currentForm.setPulsanti(BOTTONIERA);
			return mapping.getInputForward();
		}

		return Navigation.getInstance(request).goBack(true);
	}

	private FascicoloVO eseguiAggiornamento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		FascicoloVO f = currentForm.getFascicolo();
		if (delegate.checkEsistenzaFascicolo(f, currentForm.getKardex().getFascicoli()) )
			return null;

		List<FascicoloVO> newFascicolo = delegate.aggiornaFascicolo(currentForm.getKardex().getTipo(), ValidazioneDati.asSingletonList(f));
		if (!ValidazioneDati.isFilled(newFascicolo))
			return null;

		//tutto ok
		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		return newFascicolo.get(0);

	}

	public ActionForward ulterioriFascicoli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		currentForm.setUlterioriFascicoli(1);
		currentForm.setOperazione(TipoOperazione.ULTERIORI_FASCICOLI);
		FascicoloVO f = currentForm.getFascicolo();
		currentForm.setFascicoloConferma((FascicoloVO) f.clone());

		return preparaConferma(request, mapping, currentForm);
	}

	public ActionForward associaInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		KardexIntestazioneVO intestazione = kardex.getIntestazione();
		FascicoloVO f = currentForm.getFascicolo();

		SeriePeriodicoType tipo = intestazione.getTipo();

		if (navi.isFromBack())
			switch (tipo) {
			case ESEMPLARE:
			case COLLOCAZIONE:
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariOrdineNonTrovato"));
				return mapping.getInputForward();
			case ORDINE:
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));
				return mapping.getInputForward();
			}

		//almaviva5_20110126 imposto i campi ricezione se non vengo da abbonamento (il tasto ricevi é invisibile)
		if (tipo != SeriePeriodicoType.ORDINE && currentForm.getOperazione() != TipoOperazione.RICEVI)
			preparaRicevi(mapping, currentForm, request, response);

		return super.internalAssociaInventarioFascicolo(mapping, currentForm, request, response,
			ValidazioneDati.asSingletonList(f));

		/*
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		EsemplareFascicoloVO ef = f.getEsemplare();
		switch (tipo) {
		case ESEMPLARE:
			if (ef != null && ValidazioneDati.isFilled(ef.getCod_bib_ord()))
				return delegate.sifListaInventariDiOrdine(f, intestazione);
			else
				return delegate.sifListaInventariDiEsemplare(kardex);
		case COLLOCAZIONE:
			if (ef != null && ValidazioneDati.isFilled(ef.getCod_bib_ord()))
				return delegate.sifListaInventariDiOrdine(f, intestazione);
			else
				return delegate.sifListaInventariDiCollocazione(kardex, f.getData_conv_pub());
		case ORDINE:
			return delegate.sifListaInventariDiOrdine(f, intestazione);
		}

		switch (tipo) {
		case ESEMPLARE:
			return delegate.sifListaInventariDiEsemplare(kardex);
		case COLLOCAZIONE:
			return delegate.sifListaInventariDiCollocazione(kardex, f.getData_conv_pub());
		case ORDINE:
			return delegate.sifListaInventariDiOrdine(f, intestazione);
		}
		return mapping.getInputForward();
		 */
	}

	public ActionForward associaOrdine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		//recupero l'eventuale catena rinnovi per la serie in esame
		ParametriPeriodici parametri = currentForm.getParametri();
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) parametri.get(ParamType.PARAMETRI_RICERCA);
		ElementoSeriePeriodicoVO esp = (ElementoSeriePeriodicoVO) ricerca.getOggettoRicerca().getParent();
		SerieOrdineVO ordine = esp.getOrdine();

		return PeriodiciDelegate.getInstance(request).sifListaCatenaRinnoviOrdine(ordine);
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;

		FascicoloVO newFascicolo = eseguiAggiornamento(mapping, form, request, response);
		if (newFascicolo == null)
			return mapping.getInputForward();

		currentForm.setFascicolo(newFascicolo);

		switch (currentForm.getOperazione() ) {
		case GESTIONE_FASCICOLO_NO_AMM:
		case NUOVO_FASCICOLO_NO_AMM:
			currentForm.setOperazione(TipoOperazione.GESTIONE_FASCICOLO_NO_AMM);
			break;
		case PREVISIONALE:
		case NUOVO_FASCICOLO_PREVISTO:
			currentForm.setOperazione(TipoOperazione.PREVISIONALE);
			break;
		default:
			currentForm.setOperazione(TipoOperazione.GESTIONE_FASCICOLO);
		}

		impostaPulsanti(currentForm, currentForm.getOperazione());

		currentForm.setOldFascicolo((FascicoloVO) newFascicolo.clone());
		//almaviva5_20110216 #3986 sostituzione fascicolo in lista fascicoli multipli
		if (ValidazioneDati.isFilled(currentForm.getFascicoli()))
			currentForm.getFascicoli().set(currentForm.getPosizione(), (FascicoloVO) newFascicolo.clone());

		return mapping.getInputForward();
	}

	public ActionForward acquisisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		FascicoloVO f = currentForm.getFascicolo();
		//le chiavi del fascicolo sono cambiate
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		if (delegate.checkEsistenzaFascicolo(f, currentForm.getKardex().getFascicoli()) )
			return mapping.getInputForward();

		FascicoloDecorator fd = new FascicoloDecorator(f);
		currentForm.setFascicolo(fd);
		currentForm.getFascicoli().set(currentForm.getPosizione(), fd);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		return goBackWithReloadPrevisionale(request, mapping, currentForm);
	}

	public ActionForward esemplari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		currentForm.setSelected(new Integer[] { currentForm.getFascicolo().getRepeatableId() } );
		return super.esemplari(mapping, form, request, true);
	}

	private ActionForward scorri(ActionMapping mapping, ActionForm form, HttpServletRequest request, int incr) throws Exception {
		GestioneFascicoloForm currentForm = (GestioneFascicoloForm) form;
		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		int pos = currentForm.getPosizione();
		if ( (pos == (ValidazioneDati.size(fascicoli) - 1) && incr > 0) || (pos == 0 && incr < 0) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
			return mapping.getInputForward();
		}

		pos += incr;
		currentForm.setPosizione(pos);
		if (ValidazioneDati.isFilled(fascicoli) ) {
			//recupero il fascicolo alla posizione selezionata per scorrimento
			FascicoloVO f = fascicoli.get(pos).copy();
			currentForm.setFascicolo(f);
			currentForm.setOldFascicolo((FascicoloVO) f.clone());
		}

		return mapping.getInputForward();
	}

	public ActionForward forward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return scorri(mapping, form, request, +1);
	}

	public ActionForward back(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return scorri(mapping, form, request, -1);
	}

	@Override
	public ActionForward posiziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	@Override
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	@Override
	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

}
