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
package it.iccu.sbn.web.actions.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.PosizioneDescrittore;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoParoleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.RicercaSoggettoForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;


public class RicercaSoggettoAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(RicercaSoggettoAction.class);

	private static final String trovatolista = "trovatolista";
	private static final String nontrovato = "nontrovato";
	private static final String trovatolistadescrittorisoggetto = "trovatolistadescrittorisoggetto";
	private static final String trovatolistadescrittori = "trovatolistadescrittori";
	private static final String trovatolistadescrittoriparole = "trovatolistadescrittoriparole";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gestionesemantica.soggetto.bottone.cerca", "cerca");
		map.put("button.crea", "crea");
		return map;
	}

	private boolean initCombo(String ticket, ActionForm form, HttpServletRequest request) throws Exception {
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;

		List<ComboCodDescVO> listaSoggettari = CaricamentoComboSemantica.loadComboSoggettario(ticket, true);
		if (ValidazioneDati.size(listaSoggettari) == 2)	// solo un soggettario
			listaSoggettari = CaricamentoCombo.cutFirst(listaSoggettari);
		currentForm.setListaSoggettari(listaSoggettari);

		currentForm.setListaOrdinamentoSoggetto(CaricamentoComboSemantica.loadComboOrdSogg());
		currentForm.setListaOrdinamentoDescrittore(CaricamentoComboSemantica.loadComboOrdDesc());
		currentForm.setListaRicercaTipo(CaricamentoComboSemantica.loadComboRicercaTipo());
		currentForm.setListaRicercaPerUnDescrittore(CaricamentoComboSemantica.loadComboRicercaPerUnDescrittore());
		currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));

		//almaviva5_20111125 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));

		return true;

	}

	private ActionForward validaDati(ActionMapping mapping,
			HttpServletRequest request, ActionForm form) {

		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;
		ActionMessages errors = currentForm.validate(mapping, request);
		if (!errors.isEmpty()) {
			this.saveToken(request);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return null;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;

		if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				RicercaComuneVO ricercaComune = currentForm.getRicercaComune();

				Boolean def = Boolean.valueOf((String)utenteEjb.getDefault(ConstantDefault.RIC_SOG_TESTO_INIZIALE));
				if (def) ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_INIZIALE);
				def = Boolean.valueOf((String)utenteEjb.getDefault(ConstantDefault.RIC_SOG_TESTO_INTERO));
				if (def) ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_ESATTA);
				def = Boolean.valueOf((String)utenteEjb.getDefault(ConstantDefault.RIC_SOG_TESTO_PAROLA));
				if (def) ricercaComune.setRicercaTipo(TipoRicerca.PAROLE);

				String posDescrittore = (String) utenteEjb.getDefault(ConstantDefault.RIC_SOG_POS_DESCR);
				ricercaComune.setRicercaPerUnDescrittore(PosizioneDescrittore.fromSbnMarcValue(posDescrittore));
				ricercaComune.setElemBlocco((String) utenteEjb.getDefault(ConstantDefault.RIC_SOG_ELEMENTI_BLOCCHI));
				String ordinamento = (String) utenteEjb.getDefault(ConstantDefault.RIC_SOG_ORDINAMENTO);
				ricercaComune.setOrdinamentoSoggetto(TipoOrdinamento.fromSbnMarcValue(ordinamento));
				ricercaComune.setOrdinamentoDescrittore(TipoOrdinamento.fromSbnMarcValue(ordinamento));
				Boolean livelloRicerca = Boolean.valueOf((String)utenteEjb.getDefault(ConstantDefault.RIC_SOG_LIVELLO_POLO));
				ricercaComune.setPolo(livelloRicerca);
				if (!livelloRicerca)
					trasformaMappaRicercaPerIndice(form);

				// ricerca descrittore
				def = Boolean.valueOf((String)utenteEjb.getDefault(ConstantDefault.RIC_DES_TESTO_INIZIALE));
				if (def) ricercaComune.setRicercaTipoD(TipoRicerca.STRINGA_INIZIALE);
				def = Boolean.valueOf((String)utenteEjb.getDefault(ConstantDefault.RIC_DES_TESTO_INTERO));
				if (def) ricercaComune.setRicercaTipoD(TipoRicerca.STRINGA_ESATTA);


				//ricercaComune.setCodSoggettario();(String) utenteEjb.getDefault(ConstantDefault.ric_sogSEMANTICA_SOGGETTARIO_RICERCA));

			} catch (Exception e) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.default"));
				this.saveErrors(request, errors);
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		String chiamante = null;
		FolderType folder = null;

		this.saveToken(request);

		if (!currentForm.isSessione()) {
			try {
				this.initCombo(navi.getUserTicket(), currentForm, request);
				this.loadDefault(request, mapping, form);

				ParametriSoggetti parametri = ParametriSoggetti.retrieve(request);
				if (parametri != null)
					currentForm.setParametriSogg(parametri);

			} catch (Exception ex) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			}

			currentForm.getRicercaComune().setLivelloAutoritaA("97");

			OggettoRiferimentoVO oggettoRiferimento = (OggettoRiferimentoVO) request
				.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
			if (oggettoRiferimento != null)
				currentForm.setOggettoRiferimento(oggettoRiferimento);
		}

		if (!currentForm.isSessione() && !navi.isFirst()) {
			log.info("RicercaSoggettoAction::unspecified");
			// prima volta
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (datiGB != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
				currentForm.getCatalogazioneSemanticaComune().setBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getDescPartenza().trim());
				currentForm.setFolder(folder);
			}
			String bid = currentForm.getCatalogazioneSemanticaComune().getBid();
			if (ValidazioneDati.strIsNull(bid) )
				currentForm.getRicercaComune().setPolo(true);

		} else
			currentForm.setEnableCrea(false);

		currentForm.setSessione(true);
		currentForm.setAction(chiamante);

		if (request.getAttribute(NavigazioneSemantica.RICERCA_SOGGETTI_PER_PAROLE) == null) {
			if (currentForm.getRicercaComune() == null) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
			}
		} else {
			RicercaSoggettoParoleVO parole = (RicercaSoggettoParoleVO) request
					.getAttribute(NavigazioneSemantica.RICERCA_SOGGETTI_PER_PAROLE);
			if (parole != null) {
				currentForm.getRicercaComune().setRicercaSoggetto(parole);
				currentForm.getRicercaComune().setPolo(false);
				currentForm.setEnableIndice(true);

				return mapping.getInputForward();
			}
		}

		//currentForm.getRicercaComune().setPolo(true);

		Boolean isTrascina = (Boolean) request.getAttribute(NavigazioneSemantica.ABILITA_TRASCINAMENTO);
		if (isTrascina != null) {
			currentForm.setTitoliBiblio((List<?>) request.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
			currentForm.setCidTrascinaDa((String) request.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
			currentForm.setTestoTrascinaDa((String) request.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI);
			currentForm.setDatiBibliografici(datiBibliografici);
		}

		return mapping.getInputForward();
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;
		ActionMessages errors = currentForm.validate(mapping, request);
		RicercaComuneVO ricerca = currentForm.getRicercaComune();

		//almaviva5_20091016 check soggettario gestito
		String codSoggettario = ricerca.getCodSoggettario();
		if (ValidazioneDati.isFilled(codSoggettario)
				&& !SoggettiDelegate.getInstance(request).isSoggettarioGestito(codSoggettario)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.soggettarioNonGestito"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ricerca.setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, ricerca.isPolo());
		if (!errors.isEmpty()) {
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, codSoggettario);
		request.setAttribute(NavigazioneSemantica.EDIZIONE_SOGGETTARIO, ricerca.getEdizioneSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, ricerca.getDescSoggettario());

		if (ricerca.getRicercaSoggetto().getTestoSogg() != null
				|| ricerca.getRicercaSoggetto()
						.getTestoSogg().length() > 0) {
			request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, ricerca
					.getRicercaSoggetto().getTestoSogg().trim());
		}

		if (ricerca.getRicercaSoggetto() instanceof RicercaSoggettoDescrittoriVO) {
			RicercaSoggettoDescrittoriVO ricSogDesc = (RicercaSoggettoDescrittoriVO) ricerca.getRicercaSoggetto();

			if (ricSogDesc.getDescrittoriSogg() != null
					&& ricSogDesc.getDescrittoriSogg().length() > 0) {
				request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, ricSogDesc.getDescrittoriSogg()
						.trim());
			} else if (ricSogDesc.getDescrittoriSogg1() != null
					&& ricSogDesc.getDescrittoriSogg1().length() > 0) {
				request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, ricSogDesc.getDescrittoriSogg1()
						.trim());
			} else if (ricSogDesc.getDescrittoriSogg2() != null
					&& ricSogDesc.getDescrittoriSogg2().length() > 0) {
				request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, ricSogDesc.getDescrittoriSogg2()
						.trim());
			} else if (ricSogDesc.getDescrittoriSogg3() != null
					&& ricSogDesc.getDescrittoriSogg3().length() > 0) {
				request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, ricSogDesc.getDescrittoriSogg3()
						.trim());
			}
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		if (currentForm.getCidTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm
					.getTestoTrascinaDa().trim());
			request
					.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
					.getDatiBibliografici());
		}
		return mapping.findForward(RicercaSoggettoAction.nontrovato);
	}

	// METODO PER OTTENERE DESCRIZIONE DEL SOGGETTARIO DATO UN CODICE
	// SOGGETTARIO
	private String getDescrizione(String codice, ActionForm form) {
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;
		for (int index = 0; index < currentForm.getListaSoggettari().size(); index++) {
			ComboCodDescVO sog = currentForm
					.getListaSoggettari().get(index);
			if (sog.getCodice().equals(codice))
				return sog.getDescrizione();
		}
		return "Non trovato";
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;
		ActionForward action;
		if ((action = this.validaDati(mapping, request, currentForm)) != null)
			return action;
		currentForm.setAppoggioCodiceSoggettario(currentForm.getRicercaComune()
				.getCodSoggettario());

		String soggUnimarc = null;
		soggUnimarc = CodiciProvider.SBNToUnimarc(
				CodiciType.CODICE_SOGGETTARIO,
				currentForm.getAppoggioCodiceSoggettario());
		currentForm.setAppoggioCodiceSoggettario(soggUnimarc);
		if (currentForm.getRicercaComune().isPolo()) {
			if (currentForm.getRicercaComune().getRicercaSoggetto() instanceof RicercaSoggettoParoleVO) {

				if (currentForm.getRicercaComune().getRicercaSoggetto().count() > 0) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.polo"));
					this.saveErrors(request, errors);
					this.trasformaMappaRicercaPerPolo(currentForm);
					return this.loadDefault(request, mapping, currentForm);
				}

				this.trasformaMappaRicercaPerPolo(currentForm);
				return eseguiRicerca(mapping, request, errors, currentForm);
			}

		} else if (!currentForm.getRicercaComune().isPolo()) {

			if (currentForm.getRicercaComune().getRicercaSoggetto() instanceof RicercaSoggettoDescrittoriVO) {

				// ricerca per testo di descrittore non supportata in Indice
				if (currentForm.getRicercaComune().getRicercaSoggetto()
						.count() > 0) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.indice"));
					this.saveErrors(request, errors);
					this.trasformaMappaRicercaPerIndice(currentForm);
					return this.loadDefault(request, mapping, currentForm);
				}

				this.trasformaMappaRicercaPerIndice(currentForm);
				return eseguiRicerca(mapping, request, errors, currentForm);

			}
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noselezione"));
			currentForm.getRicercaComune().setCodSoggettario(
					currentForm.getRicercaComune().getCodSoggettario());
			this.saveErrors(request, errors);
			return this.loadDefault(request, mapping, currentForm);
		}
		return eseguiRicerca(mapping, request, errors, currentForm);
	}

	private void trasformaMappaRicercaPerIndice(ActionForm form) {
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;
		RicercaComuneVO ric = currentForm.getRicercaComune();
		RicercaSoggettoDescrittoriVO descr = (RicercaSoggettoDescrittoriVO) ric
				.getRicercaSoggetto();
		RicercaSoggettoParoleVO parole = new RicercaSoggettoParoleVO(descr
				.getTestoSogg().trim(), descr.getCid(), descr
				.getDescrittoriSogg(), descr.getDescrittoriSogg1(), descr
				.getDescrittoriSogg2(), descr.getDescrittoriSogg3(), "", "");
		ric.setRicercaSoggetto(parole);
		currentForm.setEnableIndice(true);
		currentForm.setEnableCrea(false);
	}

	private void trasformaMappaRicercaPerPolo(ActionForm form) {
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;
		RicercaComuneVO ric = currentForm.getRicercaComune();
		RicercaSoggettoParoleVO parole = (RicercaSoggettoParoleVO) ric
				.getRicercaSoggetto();
		RicercaSoggettoDescrittoriVO descr = new RicercaSoggettoDescrittoriVO(
				parole.getTestoSogg().trim(), parole.getCid(), parole
						.getParola0(), parole.getParola1(),
				parole.getParola2(), parole.getParola3(), false);
		ric.setRicercaSoggetto(descr);
		currentForm.setEnableIndice(false);
		currentForm.setEnableCrea(false);
	}

	/**
	 * it.iccu.sbn.web.actions.gestionesemantica.soggetto
	 * RicercaSoggettoAction.java eseguiRicerca ActionForward
	 *
	 * @param mapping
	 * @param request
	 * @param errors
	 * @return
	 *
	 *
	 */
	private ActionForward eseguiRicerca(ActionMapping mapping,
			HttpServletRequest request, ActionMessages errors, ActionForm form) {
		RicercaSoggettoForm currentForm = (RicercaSoggettoForm) form;

		try {

			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			// Imposto la descrizione del Soggettario
			String descr = this.getDescrizione(currentForm.getRicercaComune()
					.getCodSoggettario(), currentForm);
			currentForm.getRicercaComune().setDescSoggettario(descr);

			delegate.eseguiRicerca(currentForm.getRicercaComune(), mapping);
			RicercaSoggettoResult op = delegate.getOperazione();

			String testoSogg = currentForm.getRicercaComune().getRicercaSoggetto().getTestoSogg();
			if (!ValidazioneDati.strIsNull(testoSogg) )
				request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, testoSogg.trim());

			request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune()
					.getCodSoggettario());
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(currentForm
					.getRicercaComune().isPolo()));

			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
					.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());

			request.setAttribute(NavigazioneSemantica.ELEMENTI_PER_BLOCCO, currentForm.getRicercaComune()
					.getElemBlocco());
			RicercaSoggettoListaVO lista = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

			ActionForward forward = null;
			Navigation navi = Navigation.getInstance(request);

			ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
			parametri.put(SoggettiParamType.PARAMETRI_RICERCA, currentForm.getRicercaComune().copy() );
			ParametriSoggetti.send(request, parametri);

			switch (op) {
			case analitica_1:// SoggettiDelegate.analitica:
				if (currentForm.getRicercaComune().getOperazione() instanceof RicercaDescrittoreVO) {
					// return mapping.findForward(this.trovatounodescr);
					forward = mapping.findForward(RicercaSoggettoAction.trovatolistadescrittori);
					return navi.goForward(forward);
				} else

				if (currentForm.getCidTrascinaDa() != null) {
					request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm
							.getCidTrascinaDa());
					request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm
							.getTestoTrascinaDa().trim());
					request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
					request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
							.getDatiBibliografici());
				}
				forward = mapping.findForward(RicercaSoggettoAction.trovatolista);
				return navi.goForward(forward);
			case crea_4:// SoggettiDelegate.crea:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				if (currentForm.getRicercaComune().getOperazione() instanceof RicercaSoggettoDescrittoriVO
						|| currentForm.getRicercaComune().getOperazione() instanceof RicercaSoggettoParoleVO) {
					currentForm.setEnableCrea(SoggettiDelegate.getInstance(request).isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017));
				}
				currentForm.getRicercaComune().setPolo(true);
				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune()
						.isPolo());

				if (!ValidazioneDati.strIsNull(testoSogg) ) {
					request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, testoSogg.trim());
				}

				if (currentForm.getCidTrascinaDa() != null) {
					request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm
							.getCidTrascinaDa());
					request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm
							.getTestoTrascinaDa().trim());
					request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
					request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
							.getDatiBibliografici());
				}
				return this.loadDefault(request, mapping, currentForm);
			case sintetica_3:// SoggettiDelegate.sintetica:
				if (currentForm.getRicercaComune().getOperazione() instanceof RicercaDescrittoreVO) {
					String testoDescr = currentForm.getRicercaComune()
							.getRicercaDescrittore().getTestoDescr();
					if ((!ValidazioneDati.strIsNull(testoDescr) )
							|| ((currentForm.getRicercaComune()
									.getRicercaDescrittore().countParole() > 0) && !currentForm
									.getRicercaComune().isPolo())
							|| (currentForm.getRicercaComune()
									.getRicercaDescrittore().countParole() == 1)
							&& currentForm.getRicercaComune().isPolo()) {
						forward = mapping
								.findForward(RicercaSoggettoAction.trovatolistadescrittori);
						return navi.goForward(forward);
					} else
						forward = mapping
								.findForward(RicercaSoggettoAction.trovatolistadescrittoriparole);
					return navi.goForward(forward);
				} else
					forward = mapping
							.findForward(RicercaSoggettoAction.trovatolistadescrittorisoggetto);
				return navi.goForward(forward);
			case lista_2:// SoggettiDelegate.lista:
				if (!currentForm.getRicercaComune().isPolo())
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.livelloRicercaIndice"));
				else
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.livelloRicercaPolo"));

				this.saveMessages(request, errors);
				if (currentForm.getCidTrascinaDa() != null) {
					request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm
							.getCidTrascinaDa());
					request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm
							.getTestoTrascinaDa().trim());
					request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
					request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
							.getDatiBibliografici());
				}
				forward = mapping.findForward(RicercaSoggettoAction.trovatolista);
				return navi.goForward(forward);

			case diagnostico_0:// SoggettiDelegate.diagnostico:
				if (!lista.getEsito().equals("3001")) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", lista
									.getTestoEsito()));
					this.saveErrors(request, errors);
					return this.loadDefault(request, mapping, currentForm);
				} else {
					if (currentForm.getRicercaComune().getOperazione() instanceof RicercaSoggettoDescrittoriVO
							|| currentForm.getRicercaComune().getOperazione() instanceof RicercaSoggettoParoleVO
							|| currentForm.getRicercaComune().getOperazione() instanceof RicercaDescrittoreVO) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.nontrovato"));
						this.saveErrors(request, errors);
						currentForm.setEnableCrea(SoggettiDelegate.getInstance(request).isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017));
					}
				}
				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune()
						.isPolo());

				return this.loadDefault(request, mapping, currentForm);
			default:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				this.saveErrors(request, errors);
				return this.loadDefault(request, mapping, currentForm);
			}

		} catch (ValidationException e) {
			// errori indice
			if (e.getErrorCode() == SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			else
				LinkableTagUtils.addError(request, e);

			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			SbnErrorTypes errorCode = e.getErrorCode();
			if (errorCode != SbnErrorTypes.ERROR_GENERIC)
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorCode.getErrorMessage()) );
			else
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}
	}
}
