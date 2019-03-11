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
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.periodici.KardexPeriodicoDecorator;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.PosizioneFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexIntestazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerAssociaInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.vo.custom.periodici.InventarioOrdineDecorator;
import it.iccu.sbn.web.actionforms.periodici.KardexPeriodiciForm;
import it.iccu.sbn.web.actionforms.periodici.PeriodiciListaForm;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class KardexPeriodiciAction extends PeriodiciListaAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(KardexPeriodiciAction.class);

	private static String[] BOTTONIERA = new String[] {
		//"button.periodici.note",
		"button.periodici.esamina",
		"button.periodici.ricevi",
		"button.periodici.annulla.ricezione",
		"button.periodici.sollecito",
		"button.periodici.associa.inv",
		"button.periodici.annulla.associa",
		"button.periodici.volumi",
		"button.periodici.inserisci",
		"button.periodici.elimina",
		"button.periodici.chiudi" };

	private static String[] BOTTONIERA_SCEGLI = new String[] {
		"button.periodici.scegli",
		"button.periodici.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("button.periodici.note", "note");
		map.put("button.periodici.associa.inv", "associaInventario");
		//map.put("button.periodici.ricevi", "associaInventario");
		map.put("button.periodici.ricevi", "ricevi");
		map.put("button.periodici.inserisci", "inserisci");
		map.put("button.periodici.esamina", "esamina");
		map.put("button.periodici.elimina", "elimina");
		map.put("button.periodici.annulla.ricezione", "annullaRicezione");
		map.put("button.periodici.annulla.associa", "annullaRicezione");
		//map.put("button.periodici.aggiorna", "aggiorna");
		map.put("button.periodici.esegui", "aggiorna");

		map.put("button.periodici.sollecito", "sollecito");
		map.put("button.periodici.esamina.sollecito", "esameSollecito");
		map.put("button.periodici.scegli", "scegli");

		map.put("button.periodici.si", "si");
		map.put("button.periodici.no", "no");

		map.put("button.periodici.annulla", "annulla");
		map.put("button.periodici.chiudi", "annulla");

		map.put("button.periodici.posiziona", "posiziona");

		//almaviva5_20110221 #4160
		map.put("button.periodici.volumi", "volumi");

		return map;
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)  throws Exception {

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		currentForm.setConferma(false);

		switch (currentForm.getOperazione()) {
		case ASSOCIA_MULTIPLA_INVENTARIO:
			return eseguiAssociazioneMultipla(mapping, form, request, response);
		case ANNULLA_RICEZIONE:
			return eseguiAnnullamentoRicezioneMultipla(mapping, form, request, response);
		case RICEZIONE_MULTIPLA:
			return eseguiRicezioneMultiplaOrdine(mapping, currentForm, request, response);
		case CANCELLA:
			return eseguiCancellazioneMultipla(mapping, currentForm, request, response);
		case ASSOCIA_MULTIPLA_GRUPPO:
			return eseguiAssociazioneMultiplaGruppo(mapping, currentForm, request, response);

		default:
			return no(mapping, currentForm, request, response);
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return annullaConferma(mapping, form);
	}

	private ActionForward annullaConferma(ActionMapping mapping, ActionForm form) {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		currentForm.setConferma(false);
		currentForm.setInventario(null);
		currentForm.setOperazione(currentForm.getOperazioneInit());
		switch (currentForm.getOperazione()) {
		case SCEGLI_FASCICOLI_PER_CREA_ORDINE:
		case SCEGLI_FASCICOLI_PER_COMUNICAZIONE:
		case SCEGLI_FASCICOLI_PER_ASSOCIA_INVENTARI_ORDINE:
			currentForm.setPulsanti(BOTTONIERA_SCEGLI);
			break;
		default:
			currentForm.setPulsanti(BOTTONIERA);
			break;
		}

		return mapping.getInputForward();
	}

	private ActionForward eseguiAssociazioneMultiplaGruppo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		boolean ok = delegate.associaFascicoliGruppoInventario(currentForm.getInventario().getGruppoFascicolo(),
				currentForm.getKardex().getTipo(), getSelectedItems(request, currentForm));
		if (!ok)
			return annullaConferma(mapping, form);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		//aggiorna kardex
		return aggiorna(mapping, currentForm, request, response);
	}

	private ActionForward eseguiCancellazioneMultipla(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		boolean ok = delegate.cancellazioneMultiplaFascicoli(getSelectedItems(request, currentForm));
		if (!ok)
			return annullaConferma(mapping, form);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		//aggiorna kardex
		return aggiorna(mapping, currentForm, request, response);
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,	HttpServletResponse response) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		ParametriPeriodici parametri = currentForm.getParametri();
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = currentForm.getRicercaKardex();
		KardexPeriodicoVO kardex = delegate.getKardexPeriodico(currentForm.getBiblioteca(), ricerca);
		if (kardex == null)
			return annullaConferma(mapping, form);

		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		currentForm.setKardex(kardex);

		resetBlocchi(request, currentForm);

		return annullaConferma(mapping, form);
	}

	private ActionForward eseguiAnnullamentoRicezioneMultipla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,	HttpServletResponse response) throws Exception {

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		boolean ok = delegate.annullaRicezioneFascicoli(currentForm.getKardex().getTipo(),
				getSelectedItems(request, currentForm));
		if (!ok)
			return annullaConferma(mapping, form);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		//aggiorna kardex
		return aggiorna(mapping, currentForm, request, response);
	}


	private ActionForward eseguiAssociazioneMultipla(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		boolean ok = delegate.associaFascicoliInventario(currentForm.getInventario(),
				currentForm.getKardex().getTipo(), getSelectedItems(request, currentForm));
		if (!ok)
			return annullaConferma(mapping, form);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		//aggiorna kardex
		return aggiorna(mapping, currentForm, request, response);
	}

	private ActionForward eseguiRicezioneMultiplaOrdine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		boolean ok = delegate.ricezioneMultiplaOrdine(currentForm.getKardex().getIntestazione(), getSelectedItems(request, currentForm));
		if (!ok)
			return annullaConferma(mapping, form);

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));
		//aggiorna kardex
		return aggiorna(mapping, currentForm, request, response);
	}

	private FascicoloVO getSelectedItem(HttpServletRequest request, ActionForm form) {

		List<FascicoloVO> fascicoli = getSelectedItems(request, form);
		if (fascicoli == null)
			return null;

		if (ValidazioneDati.size(fascicoli) != 1) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.selezione.singola"));
			return null;
		}

		return fascicoli.get(0);
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		try {
			String cod_bib = currentForm.getBiblioteca().getCod_bib();
			List<FascicoloVO> fascicoli = PeriodiciDelegate.getInstance(request).getDettaglioFascicolo(ff, true);
			if (fascicoli == null)
				return mapping.getInputForward();

			PeriodiciUtil.checkFascicoliPerCancellazione(cod_bib, fascicoli);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		currentForm.setOperazione(TipoOperazione.CANCELLA);
		return preparaConferma(request, mapping, currentForm, new ActionMessage("errors.periodici.cancella.conferma"), null);
	}

	public ActionForward ricevi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		KardexIntestazioneVO intestazione = kardex.getIntestazione();

		try {
			PeriodiciUtil.checkFascicoliPerRicezioneSuOrdine(ff);
		} catch (ValidationException e)  {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		InventarioVO inv = delegate.getUnicoInventario(kardex.getIntestazione().getSerie());
		if (inv != null) {
			InventarioOrdineDecorator iod = (InventarioOrdineDecorator) inv;
			int cnt = iod.getCount();
			if (cnt == 1)
				return associaInventario(mapping, currentForm, request, response);

			if (cnt > 1)
				return delegate.sifListaInventariDiOrdine(null, intestazione);
		}

		currentForm.setOperazione(TipoOperazione.RICEZIONE_MULTIPLA);
		return preparaConferma(request, mapping, currentForm,
				new ActionMessage("errors.periodici.associa.ord.conferma",
						intestazione.getOrdine().getDescrizione()), null);
	}

	public ActionForward sollecito(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		if (!PeriodiciUtil.checkFascicoliPerSollecito(ff) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
			return mapping.getInputForward();
		}

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		//return delegate.sifListaComunicazioniOrdine(currentForm.getKardex().getIntestazione().getOrdine(), ff);

		return delegate.sifInserisciComunicazioneOrdine(currentForm.getKardex().getIntestazione().getOrdine(), ff);
	}

	public ActionForward esameSollecito(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		FascicoloVO f = getSelectedItem(request, form);
		if (f == null)
			return mapping.getInputForward();

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		return delegate.sifListaComunicazioniOrdine(currentForm.getKardex().getIntestazione().getOrdine(), f);
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		String attribute = (String) currentForm.getParametri().get(ParamType.SIF_KARDEX_RETURN_ATTRIBUTE);

		switch (currentForm.getOperazione()) {
		case SCEGLI_FASCICOLI_PER_COMUNICAZIONE:
			if (!PeriodiciUtil.checkFascicoliPerSollecito(ff) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
				return mapping.getInputForward();
			}

			request.setAttribute(attribute, ff);
			return navi.goBack();

		case SCEGLI_FASCICOLI_PER_CREA_ORDINE:
			//almaviva5_20101202 #4004 da crea ordine posso scegliere un solo fascicolo
			if (ValidazioneDati.size(ff) != 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.selezione.singola"));
				return mapping.getInputForward();
			}

			request.setAttribute(attribute, ff);
			return navi.goBack(true);

		case SCEGLI_FASCICOLI_PER_ASSOCIA_INVENTARI_ORDINE:
			try {
				PeriodiciUtil.checkFascicoliPerAssociazione(ff);
			} catch (ValidationException e) {
				LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();
			}
			InventarioVO inv = ((RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO>) currentForm.getRicercaKardex()).getInventario();
			currentForm.setInventario(inv);
			return associaInventario(mapping, currentForm, request, response);

		default:
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
			return mapping.getInputForward();
		}
	}

	public ActionForward associaInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		PeriodiciListaForm currentForm = (PeriodiciListaForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		KardexIntestazioneVO intestazione = kardex.getIntestazione();
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

		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		try {
			PeriodiciUtil.checkFascicoliPerAssociazione(ff);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		return internalAssociaInventarioFascicolo(mapping, currentForm, request, response, ff);
	}

	public ActionForward annullaRicezione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		if (!PeriodiciUtil.checkFascicoliPerAnnullamentoRicezione(ff) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
			return mapping.getInputForward();
		}

		currentForm.setOperazione(TipoOperazione.ANNULLA_RICEZIONE);
		return preparaConferma(request, mapping, currentForm, null, null);
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;

		Navigation navi = Navigation.getInstance(request);
		navi.addBookmark(PeriodiciDelegate.BOOKMARK_KARDEX);

		if (currentForm.isInitialized() )
			return;

		ParametriPeriodici parametri = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(parametri);
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) parametri.get(ParamType.PARAMETRI_RICERCA);
		currentForm.setBiblioteca(ricerca.getBiblioteca());
		currentForm.setRicercaKardex(ricerca);
		KardexPeriodicoVO kardex = (KardexPeriodicoVO) currentForm.getParametri().get(ParamType.KARDEX_PERIODICO);
		currentForm.setKardex(kardex);
		resetBlocchi(request, currentForm);

		TipoOperazione tipo = (TipoOperazione) parametri.get(ParamType.TIPO_OPERAZIONE);
		currentForm.setOperazione(tipo);
		currentForm.setOperazioneInit(tipo);
		switch (tipo) {
		case SCEGLI_FASCICOLI_PER_CREA_ORDINE:
			currentForm.setPulsanti(BOTTONIERA_SCEGLI);
			navi.setTesto(".periodici.kardexPeriodici.CREA_ORDINE.testo");
			break;
		case SCEGLI_FASCICOLI_PER_COMUNICAZIONE:
			currentForm.setPulsanti(BOTTONIERA_SCEGLI);
			navi.setTesto(".periodici.kardexPeriodici.COMUNICAZIONE.testo");
			break;
		case SCEGLI_FASCICOLI_PER_ASSOCIA_INVENTARI_ORDINE:
			currentForm.setPulsanti(BOTTONIERA_SCEGLI);
			navi.setTesto(".periodici.kardexPeriodici.INV_ORDINE.testo");

			InventarioVO inv = ((RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO>) ricerca).getInventario();
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.lega.fascicoli.inventario.ordine",
					inv.getChiaveOrdine(), inv.getChiaveInventario()) );
			break;
		default:
			currentForm.setPulsanti(BOTTONIERA);
			navi.setTesto(".periodici.kardexPeriodici." + currentForm.getKardex().getTipo() + ".testo");
			break;
		}

		currentForm.setInitialized(true);
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

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;

		InventarioVO inv = currentForm.getInventario();
		if (inv != null) {
			currentForm.setOperazione(TipoOperazione.ASSOCIA_MULTIPLA_INVENTARIO);
			return preparaConferma(request, mapping, currentForm,
				new ActionMessage("errors.periodici.associa.inv.conferma",
				inv.getChiaveInventario()), null);
		}

		ComunicazioneVO com = currentForm.getComunicazione();
		if (com != null) {
			currentForm.setComunicazione(null);
			return aggiorna(mapping, currentForm, request, response);
		}

		return mapping.getInputForward();
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		ParametriPeriodici parametri = currentForm.getParametri();
		KardexPeriodicoVO newKardex = (KardexPeriodicoVO) parametri.get(ParamType.KARDEX_PERIODICO);

		//controllo se il kardex registrato nella form é stato aggiornato
		//in caso affermativo devo ricaricare le info sui blocchi
		KardexPeriodicoDecorator oldKardex = (KardexPeriodicoDecorator) currentForm.getKardex();
		currentForm.setKardex(newKardex);
		if (oldKardex != null && oldKardex.older(newKardex)) {
			resetBlocchi(request, currentForm);
			//imposto la selezione sul fascicolo
			Integer id = (Integer) parametri.getAndRemove(ParamType.POSIZIONA_ID_FASCICOLO);
			if (id != null)	{
				PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
				PosizioneFascicoloVO pos = delegate.getPosizioneFascicoloPerId(id, (KardexPeriodicoDecorator) newKardex);
				if (pos != null)
					posizionaSelezione(request, form, pos);
			}

		}

		List<FascicoloVO> fascicoli = currentForm.getFascicoli();
		if (ValidazioneDati.size(fascicoli) == 1 )
			currentForm.setSelected(new Integer[] {fascicoli.get(0).getRepeatableId() });

		//sto ricevendo su inventario
		InventarioVO inv = (InventarioVO) request.getAttribute(PeriodiciDelegate.SIF_INVENTARIO);
		if (inv != null) {
			log.debug("inv selezionato: " + inv);
			currentForm.setInventario(inv);
		}

		//sto tornando da comunicazioni
		ComunicazioneVO com = (ComunicazioneVO) request.getAttribute(PeriodiciDelegate.SIF_COMUNICAZIONI);
		currentForm.setComunicazione(com);

	}

	public ActionForward volumi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<FascicoloVO> ff = getSelectedItems(request, form);
		if (ff == null)
			return mapping.getInputForward();

		try {
			PeriodiciUtil.checkFascicoliPerAssociaGruppo(ff);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		currentForm.setOperazione(TipoOperazione.ASSOCIA_MULTIPLA_GRUPPO);
		currentForm.setInventario(new InventarioVO());
		return preparaConferma(request, mapping, currentForm, null, null);
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.internalEsamina(mapping, form, request, response, TipoOperazione.GESTIONE_FASCICOLO, true);
	}

	public ActionForward posiziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = currentForm.getRicercaKardex();
		KardexPeriodicoDecorator kardex = (KardexPeriodicoDecorator) currentForm.getKardex();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		PosizioneFascicoloVO pos = delegate.getPosizioneFascicoloPerData(ricerca.getDataScroll(), kardex);
		if (pos == null)
			return mapping.getInputForward();

		posizionaSelezione(request, currentForm, pos);

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		KardexPeriodicoVO kardex = currentForm.getKardex();
		SeriePeriodicoType type = kardex.getTipo();
		try {
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

			//almaviva5_20110221 #4160
			if (ValidazioneDati.equals(idCheck, "button.periodici.volumi"))
				return !kardex.isEmpty() && delegate.isAbilitatoAmministrazioneFascicoli()
					&& type != SeriePeriodicoType.ORDINE;

			if (ValidazioneDati.equals(idCheck, "button.periodici.inserisci"))
				return delegate.isAbilitatoDescrizioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.elimina"))
				return !kardex.isEmpty() && delegate.isAbilitatoDescrizioneFascicoli();

			if (ValidazioneDati.equals(idCheck, "button.periodici.ricevi"))
				return delegate.isAbilitatoAmministrazioneFascicoli() &&
					!kardex.isEmpty() && type == SeriePeriodicoType.ORDINE;

			if (ValidazioneDati.equals(idCheck, "button.periodici.esamina"))
				return !kardex.isEmpty() &&
					(delegate.isAbilitatoAmministrazioneFascicoli() || delegate.isAbilitatoDescrizioneFascicoli());

			if (ValidazioneDati.equals(idCheck, "button.periodici.annulla.ricezione"))
				return delegate.isAbilitatoAmministrazioneFascicoli() &&
					!kardex.isEmpty() && type == SeriePeriodicoType.ORDINE;

			if (ValidazioneDati.equals(idCheck, "button.periodici.annulla.associa"))
				return delegate.isAbilitatoAmministrazioneFascicoli() &&
					!kardex.isEmpty() && type != SeriePeriodicoType.ORDINE;

			if (ValidazioneDati.equals(idCheck, "button.periodici.associa.inv"))
				return delegate.isAbilitatoAmministrazioneFascicoli() &&
					!kardex.isEmpty() && type != SeriePeriodicoType.ORDINE;

			if (ValidazioneDati.equals(idCheck, "button.periodici.sollecito"))
				return delegate.isAbilitatoAmministrazioneFascicoli() &&
					!kardex.isEmpty() && type == SeriePeriodicoType.ORDINE;

			if (ValidazioneDati.in(idCheck, "LINK_NUM_FASCICOLO", "LINK_STATO_FASCICOLO")) {
				ParametriPeriodici parametri = currentForm.getParametri();
				TipoOperazione op = (TipoOperazione) parametri.get(ParamType.TIPO_OPERAZIONE);
				return !ValidazioneDati.in(op,
					TipoOperazione.SCEGLI_FASCICOLI_PER_COMUNICAZIONE,
					TipoOperazione.SCEGLI_FASCICOLI_PER_CREA_ORDINE,
					TipoOperazione.SCEGLI_FASCICOLI_PER_ASSOCIA_INVENTARI_ORDINE);
			}

			if (ValidazioneDati.equals(idCheck, "POSIZIONA"))
				return !kardex.isEmpty();

			if (ValidazioneDati.equals(idCheck, "POSIZIONA_BOTTOM")) {
				KardexPeriodicoDecorator kpd = (KardexPeriodicoDecorator) kardex;
				DescrittoreBloccoVO blocco1 = kpd.getBlocco();
				return (blocco1.getTotBlocchi() > 1);
			}

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;
	}

	@Override
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		KardexPeriodiciForm currentForm = (KardexPeriodiciForm) form;
		return internalBlocco(mapping, form, request, response, currentForm.getRicercaKardex().getComparator());
	}

	@Override
	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return internalInserisci(mapping, form, request, response, TipoOperazione.NUOVO_FASCICOLO);
	}

}
