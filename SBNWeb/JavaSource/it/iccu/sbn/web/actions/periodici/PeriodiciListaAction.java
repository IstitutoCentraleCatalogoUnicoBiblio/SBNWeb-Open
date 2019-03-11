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
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.periodici.KardexPeriodicoDecorator;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.PosizioneFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexIntestazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.custom.periodici.InventarioOrdineDecorator;
import it.iccu.sbn.web.actionforms.periodici.PeriodiciListaForm;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationBlocchiInfo;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public abstract class PeriodiciListaAction extends SinteticaLookupDispatchAction {

	protected static String[] BOTTONIERA_CONFERMA = new String[] {
		"button.periodici.si",
		"button.periodici.no",
		"button.periodici.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.periodici.esamina", "esamina");
		map.put("button.periodici.posiziona", "posiziona");
		map.put("button.blocco", "blocco");
		map.put("button.selAllTitoli", "tutti");
		map.put("button.deSelAllTitoli", "nessuno");

		map.put("button.periodici.annulla", "annulla");

		return map;
	}

	protected ActionForward internalAssociaInventarioFascicolo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response, List<FascicoloVO> ff)
			throws Exception {

		// se ho selezionato fascicoli con ordini (sempre lo stesso)
		// devo proporre gli inventari di quest'ultimo
		FascicoloVO fascicoloOrd = null;
		for (FascicoloVO f : ff) {
			EsemplareFascicoloVO ef = f.getEsemplare();
			if (ef != null && !ef.isCancellato() && ef.isLegatoOrdine()) {
				fascicoloOrd = f;
				break;
			}
		}

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		KardexIntestazioneVO intestazione = kardex.getIntestazione();
		SeriePeriodicoType tipo = intestazione.getTipo();

		// almaviva5_20101025 check inventario
		ParametriPeriodici parametri = currentForm.getParametri();
		InventarioVO inv = (InventarioVO) parametri.get(ParamType.INVENTARIO);
		if (inv != null) {
			// nel caso la chiamata sia da lista inv. del titolo non accetto
			// associazioni ad ordini diversi da quello associato all'inv. selezionato
			if (fascicoloOrd != null) {
				//almaviva5_20111102 #4710
				FascicoloDecorator fd = new FascicoloDecorator(fascicoloOrd);
				if (!ValidazioneDati.equals(fd.getChiaveOrdine(), inv.getChiaveOrdine()) ) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
					return mapping.getInputForward();
				}
			}
			// simulazione sif inventario di collocazione
			currentForm.setInventario(inv);
			return unspecified(mapping, currentForm, request, response);
		}

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		switch (tipo) {
		case ESEMPLARE:
			inv = delegate.getUnicoInventario(kardex.getIntestazione().getSerie());
			if (inv != null) {
				// simulazione sif inventario di collocazione
				currentForm.setInventario(inv);
				return unspecified(mapping, currentForm, request, response);
			}
			if (fascicoloOrd != null)
				return delegate.sifListaInventariDiOrdine(fascicoloOrd,	intestazione);
			else
				return delegate.sifListaInventariDiEsemplare(kardex);
		case COLLOCAZIONE:
			inv = delegate.getUnicoInventario(kardex.getIntestazione().getSerie());
			if (inv != null) {
				// simulazione sif inventario di collocazione
				currentForm.setInventario(inv);
				return unspecified(mapping, currentForm, request, response);
			}
			if (fascicoloOrd != null)
				return delegate.sifListaInventariDiOrdine(fascicoloOrd,	intestazione);
			else
				return delegate.sifListaInventariDiCollocazione(kardex,	ff.get(0).getData_conv_pub());
		case ORDINE:
			inv = delegate.getUnicoInventario(kardex.getIntestazione().getSerie());
			if (inv != null) {
				InventarioOrdineDecorator iod = (InventarioOrdineDecorator) inv;
				int cnt = iod.getCount();
				if (cnt == 1) {
					// simulazione sif inventario di collocazione
					currentForm.setInventario(inv);
					return unspecified(mapping, currentForm, request, response);
				}
				if (cnt > 1)
					return delegate.sifListaInventariDiOrdine(null, intestazione);
			}

		}

		return mapping.getInputForward();
	}

	protected List<FascicoloVO> getSelectedItems(HttpServletRequest request, ActionForm form) {
		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		List<Integer> items = getMultiBoxSelectedItems(currentForm.getSelected());
		if (!ValidazioneDati.isFilled(items)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
			return null;
		}

		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		List<FascicoloVO> output = new ArrayList<FascicoloVO>();
		for (int idx : items)
			output.add(FascicoloVO.searchRepeatableId(idx, fascicoli));

		return output;
	}

	protected ActionForward internalEsamina(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, TipoOperazione op, boolean errorIfNotFound) throws Exception {

		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		ParametriPeriodici parametri = currentForm.getParametri();
		parametri.put(ParamType.TIPO_OPERAZIONE, op);
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		List<FascicoloVO> fascicoli = delegate.getDettaglioFascicolo(ff, errorIfNotFound);
		int size = ValidazioneDati.size(fascicoli);
		if (size == 0)	//non trovato
			return mapping.getInputForward();

		if (size == 1) {
			// singolo fascicolo.
			parametri.put(ParamType.DETTAGLIO_FASCICOLO, fascicoli.get(0).copy());
			parametri.remove(ParamType.LISTA_FASCICOLI);
		} else {
			// almaviva5_20110216 #3986 fascicoli multipli
			parametri.put(ParamType.LISTA_FASCICOLI, (Serializable) fascicoli);
			parametri.remove(ParamType.DETTAGLIO_FASCICOLO);
		}

		//si crea una mappa che per ogni fascicolo selezionato
		//memorizza la sua posizione originale nella sintetica
		KardexPeriodicoVO kardex = currentForm.getKardex();
		Map<Integer, Integer> posizioni = new HashMap<Integer, Integer>(size);
		for (int pos = 0; pos < size; pos++) {
			FascicoloVO f = fascicoli.get(pos);
			posizioni.put(pos, UniqueIdentifiableVO.indexOfRepeatableId(f.getRepeatableId(), kardex.getFascicoli()));
		}
		parametri.put(ParamType.POSIZIONE_LISTA_FASCICOLI, (Serializable) posizioni);

		ParametriPeriodici.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("fascicolo"));
	}

	public abstract ActionForward blocco(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	protected ActionForward internalBlocco(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, Comparator<? super FascicoloVO> comparator) throws Exception {

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		KardexPeriodicoDecorator kpd = (KardexPeriodicoDecorator) currentForm.getKardex();
		DescrittoreBloccoVO blocco = kpd.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2 || blocco.getIdLista() == null
				|| currentForm.getBlocchiCaricati().contains(bloccoSelezionato))
			return mapping.getInputForward();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		DescrittoreBloccoVO nextBlocco = delegate.nextBlocco(
				blocco.getIdLista(), bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(nextBlocco.getNumBlocco());
		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		fascicoli.addAll(nextBlocco.getLista());
		Collections.sort(fascicoli, comparator);

		return mapping.getInputForward();

	}

	protected void posizionaSelezione(HttpServletRequest request, ActionForm form, PosizioneFascicoloVO pos) {
		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		DescrittoreBloccoVO blocco = pos.getBlocco();
		FascicoloDecorator fascicolo = pos.getFascicolo();

		//seleziono sulla lista il fascicolo trovato
		currentForm.setSelected(new Integer[] { fascicolo.getRepeatableId() });
		NavigationElement e = Navigation.getInstance(request).getCache().getCurrentElement();
		e.setAnchorId(fascicolo.getRepeatableId() + "");
		NavigationBlocchiInfo info = e.getInfoBlocchi();
		if (info != null)
			info.setNumBlocco(blocco.getNumBlocco());
		currentForm.setBloccoSelezionato(blocco.getNumBlocco());
		request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + fascicolo.getRepeatableId()); //scroll jsp

		//solo se non trovo il fascicolo già in lista devo caricare il blocco cui appartiene
		if (blocco != null && !currentForm.getBlocchiCaricati().contains(blocco.getNumBlocco()) ) {
			currentForm.getBlocchiCaricati().add(blocco.getNumBlocco());
			List<FascicoloVO> fascicoli = currentForm.getFascicoli();
			fascicoli.addAll(blocco.getLista() );
			BaseVO.sortAndEnumerate(fascicoli, currentForm.getRicercaKardex().getComparator() );
		}
	}

	protected void resetBlocchi(HttpServletRequest request, PeriodiciListaForm currentForm) {
		KardexPeriodicoVO kardex = currentForm.getKardex();
		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		if (!kardex.isEmpty()) {
			Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
			fascicoli.clear();
			DescrittoreBloccoVO blocco1 = ((KardexPeriodicoDecorator)kardex).getBlocco();
			fascicoli.addAll(blocco1.getLista());
			currentForm.setBloccoSelezionato(1);
			currentForm.getBlocchiCaricati().clear();
			currentForm.getBlocchiCaricati().add(1);
		} else
			currentForm.setFascicoli(ValidazioneDati.emptyList(FascicoloVO.class));
	}

	public abstract ActionForward posiziona(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	protected String getMethodName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response, String parameter) throws Exception {

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(PeriodiciListaForm.SUBMIT_BUTTON_POSIZIONA) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				boolean isBottom = Boolean.parseBoolean(tokens[2]);
				String value = isBottom ? currentForm.getPosizionaBottom() : currentForm.getPosizionaTop();
				currentForm.getRicercaKardex().setDataScroll(value);
				return "posiziona";
			}

		}
		return super.getMethodName(mapping, form, request, response, parameter);
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		int size = ValidazioneDati.size(fascicoli);
		if (size > 0) {
			Integer[] selected = new Integer[size];
			for (int idx = 0; idx < size; idx++)
				selected[idx] = fascicoli.get(idx).getRepeatableId();

			currentForm.setSelected(selected);
		}

		return mapping.getInputForward();
	}

	public ActionForward nessuno(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		currentForm.setSelected(null);
		return mapping.getInputForward();
	}

	protected ActionForward esemplari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, boolean polo) throws Exception,
			ValidationException {
		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;

		// se ho selezionato un fascicolo dalla sintetica recupero i dati da
		// quest'ultimo
		List<FascicoloVO> fascicoli = getSelectedItems(request, currentForm);
		if (fascicoli == null)
			return mapping.getInputForward();

		if (ValidazioneDati.size(fascicoli) != 1) { // il fascicolo "modello" deve essere solo uno
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.selezione.singola"));
			return mapping.getInputForward();
		}

		FascicoloVO selected = fascicoli.get(0);

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		ParametriPeriodici parametri = currentForm.getParametri().copy();
		BibliotecaVO bib = currentForm.getBiblioteca();
		RicercaKardexPeriodicoVO<FascicoloDecorator> ricerca = new RicercaKardexPeriodicoVO<FascicoloDecorator>(bib, new SerieFascicoloVO(selected, polo));
		ricerca.setComparator(FascicoloDecorator.ORDINAMENTO_ESEMPLARE);

		KardexPeriodicoVO kardex = delegate.getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		parametri.put(ParamType.LISTA_ESEMPLARI_FASCICOLO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		ParametriPeriodici.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForwardConfig("esemplari"));
	}

	protected ActionForward preparaConferma(HttpServletRequest request,
			ActionMapping mapping, ActionForm form, ActionMessage msg, String[] pulsanti)
			throws Exception {
		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		currentForm.setConferma(true);
		currentForm.setPulsanti(pulsanti != null ? pulsanti : BOTTONIERA_CONFERMA);
		LinkableTagUtils.addError(request, msg != null ? msg : new ActionMessage("errors.servizi.confermaOperazioneAgg"));
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public abstract ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	protected ActionForward internalInserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, TipoOperazione op)
			throws Exception {

		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		FascicoloVO f = null;

		// se ho selezionato un fascicolo dalla sintetica recupero i dati da quest'ultimo
		List<FascicoloVO> items = getSelectedItems(request, currentForm);
		int size = ValidazioneDati.size(items);

		if (size > 1) { // il fascicolo "modello" deve essere solo uno
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.selezione.singola"));
			return mapping.getInputForward();
		}

		try {
			FascicoloVO selected = size > 0 ? ValidazioneDati.first(items) : null;
			List<FascicoloVO> fascicoli = currentForm.getFascicoli();

			if (selected != null) {
				f = PeriodiciUtil.preparaModelloFascicolo(selected);
				int idx = UniqueIdentifiableVO.indexOfRepeatableId(selected.getRepeatableId(), fascicoli);
				f.setData_in_pubbl(PeriodiciUtil.calcolaDataProssimoFascicolo(ValidazioneDati.asSingletonList(fascicoli.get(idx))));
			} else {
				f = new FascicoloVO();
				f.setCodPolo(kardex.getBiblioteca().getCod_polo());
				f.setCodBib(kardex.getBiblioteca().getCod_bib());
				f.setBid(ValidazioneDati.isFilled(fascicoli) ? ValidazioneDati.first(fascicoli).getBid() : kardex.getBid() );
				f.setCd_per(kardex.getTipoPeriodicita());
			}

			//almaviva5_20121107 #58 LIG
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			FascicoloVO nuovoFascicolo = ValidazioneDati.first(delegate.getDettaglioFascicolo(ValidazioneDati.asSingletonList(f), false));
			//
			ParametriPeriodici parametri = currentForm.getParametri();
			parametri.put(ParamType.DETTAGLIO_FASCICOLO, nuovoFascicolo);
			parametri.put(ParamType.TIPO_OPERAZIONE, op);
			ParametriPeriodici.send(request, parametri);

			LinkableTagUtils.resetErrors(request);
			return Navigation.getInstance(request).goForward(mapping.findForwardConfig("fascicolo"));

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}
	}

}
