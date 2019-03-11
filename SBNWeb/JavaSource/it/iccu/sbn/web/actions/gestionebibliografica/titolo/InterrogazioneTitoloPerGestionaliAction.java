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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.InterrogazioneTitoloPerGestionaliForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
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

public class InterrogazioneTitoloPerGestionaliAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(InterrogazioneTitoloPerGestionaliAction.class);

	private static final String LISTA_BIBLIOTECHE_AFFILIATE_ORD = "LISTA_BIBLIOTECHE_AFFILIATE_ORD";
	private static final String LISTA_BIBLIOTECHE_AFFILIATE_INV = "LISTA_BIBLIOTECHE_AFFILIATE_INV";


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("ricerca.button.cerca", "cercaTit");

		map.put("ricerca.gestionali.tag.inventari", "tabInventario");
		map.put("ricerca.gestionali.tag.ordini", "tabOrdine");
		map.put("ricerca.gestionali.tag.locFondoSegn", "tabLocFondoSegn");

		map.put("ricerca.label.bibliolist", "sifBibOrdine");
		map.put("documentofisico.lsBib", "sifBibInventario");

		return map;
	}


	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equals("POST"))
			((InterrogazioneTitoloPerGestionaliForm)form).getInterrGener().save();
		return super.execute(mapping, form, request, response);
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ActionMessages errors = new ActionMessages();
		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			String cdSerieDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_SERIE);
			if (!ValidazioneDati.strIsEmpty(cdSerieDefault) ) {
				SerieVO serieDettaglio = this.getSerieDettaglio(
						utente.getCodPolo(), utente.getCodBib(), cdSerieDefault.toUpperCase(), utente.getTicket());

				if (serieDettaglio == null) {
					errors.clear();
					errors.add("noSelection", new ActionMessage("error.documentofisico.serieDefaultNonEsiste"));
					this.saveErrors(request, errors);
					return Navigation.getInstance(request).goBack(true);
				} else {
					currentForm.setCodSerieDefault(cdSerieDefault);
				}

			}

			// Modifica almaviva2 Aprile 2015: viene impostato il valore di default del TipoFormatoListaSintetica
			// nella ricerca per Gestionali nella casistiche di ricerca per Inventari e per Ordini dato che nelle Mappe
			// non è presente la tendina di selezione
			currentForm.getInterrGener().setFormatoListaSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_FORMATO_LISTA));

		} catch (Exception e) {
			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			errors.clear();
			errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
			this.saveErrors(request, errors);
			//almaviva2 09/10/2009
			try{
				List listaSerie = null;
				listaSerie = this.getListaSerieDate(utente.getCodPolo(),
						utente.getCodBib(), utente.getTicket(), form);
				if (listaSerie == null || listaSerie.size() <= 0) {
					errors.add("generico", new ActionMessage("error.documentofisico.listaVuotaSerie"));
					this.saveErrors(request, errors);
					request.setAttribute("noListaSerie","noListaSerie");
					return Navigation.getInstance(request).goBack(true);
				} else {
					currentForm.setListaSerie(listaSerie);
					// carico la lista delle serie nella combo
					currentForm.setListaComboSerie(caricaCombo.loadCodice(listaSerie));

					if (currentForm.getCodSerieDefault() == null){
						currentForm.setSerieInventario(((CodiceVO)currentForm.getListaComboSerie().get(0)).getCodice());
					}else{
						currentForm.setSerieInventario(currentForm.getCodSerieDefault());
					}
				}
			} catch (Exception e1) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.documentofisico.listaVuotaSerie"));
				this.saveErrors(request, errors);
			}
			return mapping.getInputForward();
			//almaviva2 09/10/2009
			}
		return null;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			currentForm.getInterrGener().restore();
			return mapping.getInputForward();
		}

		if (currentForm.getTipoProspettazione() == null) {
			log.debug("InterrogazioneTitoloPerGestionaliAction::unspecified");
			currentForm.setTipoProspettazione("Inventario");
		}

		currentForm.setProvenienza("");
		if (request.getParameter("NEWLEGAME") != null) {
			currentForm.setProvenienza("NEWLEGAME");
			currentForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
		}


		if (request.getAttribute(TitoliCollegatiInvoke.xidDiRicerca) != null) {
			currentForm.setProvenienza("INTERFILTRATA");
			currentForm.setXidDiRicerca((String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			currentForm.setXidDiRicercaDesc((String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));
			currentForm.setTipoMatDiRic((String) request.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));
			currentForm.setNaturaDiRic((String) request.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));
			currentForm.setOggettoDiRicerca((Integer) request.getAttribute(TitoliCollegatiInvoke.oggDiRicerca));
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);


		// Impostazione di inizializzazione jsp
		caricaComboGenerTitolo(currentForm.getInterrGener());

		if (currentForm.getTipoProspettazione().equals("LocFonSeg"))
			return tabLocFondoSegn(mapping, currentForm, request, response);

		if (currentForm.getTipoProspettazione().equals("Inventario"))
			return tabInventario(mapping, currentForm, request, response);

		if (currentForm.getTipoProspettazione().equals("Ordine"))
			return tabOrdine(mapping, currentForm, request, response);


		return mapping.getInputForward();
	}


	public ActionForward tabLocFondoSegn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;

		currentForm.setTipoProspettazione("LocFonSeg");
		currentForm.getInterrGener().setTipoMateriale("U");
		currentForm.getInterrGener().setRicLocale(false);
		currentForm.getInterrGener().setRicIndice(true);
		currentForm.getInterrGener().setElemXBlocchi(10);
		return mapping.getInputForward();
	}


	public ActionForward tabInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;
		UserVO utente = Navigation.getInstance(request).getUtente();

		//almaviva5_20111109 fix biblioteca
		if (!ValidazioneDati.isFilled(currentForm.getCodBib())) {
			currentForm.setCodBib(utente.getCodBib());
			currentForm.setDescrBib(utente.getBiblioteca());
		}

		boolean bibChanged = false;
		if (request.getAttribute(LISTA_BIBLIOTECHE_AFFILIATE_INV) != null) {
			BibliotecaVO bib = (BibliotecaVO)request.getAttribute(LISTA_BIBLIOTECHE_AFFILIATE_INV);

			//biblioteca cambiata?
			bibChanged = !ValidazioneDati.equals(currentForm.getCodBib(), bib.getCod_bib());

			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			currentForm.setCodBib(bib.getCod_bib());
			currentForm.setDescrBib(bib.getNom_biblioteca());
		}

		CaricamentoCombo caricaCombo = new CaricamentoCombo();

		ActionForward loadDefault = loadDefault(request, mapping, form);
		if (loadDefault != null)
			return loadDefault;

		//
		if (bibChanged || currentForm.getListaSerie() == null) {
			List listaSerie = this.getListaSerie(utente.getCodPolo(), currentForm.getCodBib(), utente.getTicket(), form);
			if (!ValidazioneDati.isFilled(listaSerie) ) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuotaSerie"));
				request.setAttribute("noListaSerie","noListaSerie");
				currentForm.setNoSerie(true);
				currentForm.setSerieInventario("");
				return mapping.getInputForward();
			} else {
				currentForm.setListaSerie(listaSerie);
				currentForm.setListaComboSerie(caricaCombo.loadCodice(currentForm.getListaSerie()));
			}
		}
		//

		if (currentForm.getCodSerieDefault() == null){
			currentForm.setSerieInventario(((CodiceVO)currentForm.getListaComboSerie().get(0)).getCodice());
		}else{
			currentForm.setSerieInventario(currentForm.getCodSerieDefault());
		}
		currentForm.setNumeroInventario(0);

		currentForm.setTipoProspettazione("Inventario");
		currentForm.getInterrGener().setRicLocale(true);
		currentForm.getInterrGener().setRicIndice(false);
		currentForm.getInterrGener().setElemXBlocchi(10);
		return mapping.getInputForward();
	}


	public ActionForward tabOrdine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;

		UserVO utente = Navigation.getInstance(request).getUtente();

		if (!ValidazioneDati.isFilled(currentForm.getCodBibOrd())) {
			currentForm.setCodBibOrd(utente.getCodBib());
			currentForm.setDescrBibOrd(utente.getBiblioteca());
		}

		BibliotecaVO bib = (BibliotecaVO) request.getAttribute(LISTA_BIBLIOTECHE_AFFILIATE_ORD);
		if (bib != null) {
			currentForm.setCodBibOrd(bib.getCod_bib());
			currentForm.setDescrBibOrd(bib.getNom_biblioteca());
		}

		CaricamentoCombo carCombo = new CaricamentoCombo();

		currentForm.setListaTipoOrdine(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ORDINE)));
		currentForm.setAnnoOrdine("");
		currentForm.setNumeroOrdine("");

		currentForm.setTipoProspettazione("Ordine");
		currentForm.getInterrGener().setRicLocale(true);
		currentForm.getInterrGener().setRicIndice(false);
		currentForm.getInterrGener().setElemXBlocchi(10);
		return mapping.getInputForward();
	}


	public ActionForward cercaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;

		AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
		areaDatiPass.setRicercaPerGest("SI");
		areaDatiPass.setTipoProspettazioneGestionali(currentForm.getTipoProspettazione());

		if (currentForm.getTipoProspettazione().equals("LocFonSeg")) {
			try {
				currentForm.getInterrGener().validaParametriGener();
			} catch (ValidationException e) {
				e.printStackTrace();
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica." + e.getMessage()));
				this.saveErrors(request, errors);
				caricaComboGenerTitolo(currentForm.getInterrGener());
				return mapping.getInputForward();
			}


			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);

			if (currentForm.getInterrGener().isRicLocale()) {
				request.setAttribute("livRicerca", "P");
				currentForm.setLivRicerca("P");
				areaDatiPass.setRicercaPolo(true);
				if (currentForm.getInterrGener().getBibliotecaSelez() != null
						&& currentForm.getInterrGener().getBibliotecaSelez().length() > 0) {
					request.setAttribute("livRicerca", "B");
					request.setAttribute("codBib", currentForm.getInterrGener()
							.getBibliotecaSelez().toString());
				}
			}
			if (currentForm.getInterrGener().isRicIndice()) {
				request.setAttribute("livRicerca", "I");
				currentForm.setLivRicerca("I");
				areaDatiPass.setRicercaIndice(true);
			}

			areaDatiPass.setInterTitGen(currentForm.getInterrGener());
			areaDatiPass.setInterTitCar(currentForm.getInterrCartog());
			areaDatiPass.setInterTitGra(currentForm.getInterrGrafic());
			areaDatiPass.setInterTitMus(currentForm.getInterrMusic());
			areaDatiPass.setOggChiamante(99);
			areaDatiPass.setTipoOggetto(99);
			areaDatiPass.setTipoOggettoFiltrato(99);
			areaDatiPass.setOggDiRicerca("");

			if (currentForm.getProvenienza().equals("INTERFILTRATA")) {
				areaDatiPass.setOggDiRicerca(currentForm.getXidDiRicerca());
				areaDatiPass.setTipMatTitBase(currentForm.getTipoMatDiRic());
				areaDatiPass.setNaturaTitBase(currentForm.getNaturaDiRic());
				areaDatiPass.setCodiceLegame("");
				areaDatiPass.setCodiceSici("");
				areaDatiPass.setTipoOggettoFiltrato(currentForm.getOggettoDiRicerca());
			}

		} else if (currentForm.getTipoProspettazione().equals("Inventario")) {
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			areaDatiPass.setCodBiblio(currentForm.getCodBib());
			if (String.valueOf(currentForm.getNumeroInventario()).trim() != null  && (!ValidazioneDati.strIsAlfabetic(String.valueOf(currentForm.getNumeroInventario()).trim()))){
				if (Integer.parseInt(String.valueOf(currentForm.getNumeroInventario()).trim()) > 0){
				areaDatiPass.setNumeroInventario((Integer.parseInt(String.valueOf(currentForm.getNumeroInventario()).trim())));
				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.indicareUnNumeroInventarioMaggioreDiZero"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			areaDatiPass.setSerieInventario(currentForm.getSerieInventario());
		} else if (currentForm.getTipoProspettazione().equals("Ordine")) {
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			if (currentForm.getCodTipoOrdine().equals("") || currentForm.getAnnoOrdine().equals("") || currentForm.getNumeroOrdine().equals("") ) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.valorTuttiICampi"));
				areaDatiPass.setRicercaPolo(false);
				areaDatiPass.setRicercaIndice(false);
				this.saveErrors(request, errors);
				return mapping.getInputForward();

			}
			areaDatiPass.setCodBiblio(currentForm.getCodBibOrd());
			areaDatiPass.setAnnoOrdine(currentForm.getAnnoOrdine());
			areaDatiPass.setNumeroOrdine(currentForm.getNumeroOrdine());
			areaDatiPass.setCodTipoOrdine(currentForm.getCodTipoOrdine());
		}

		// Modifica almaviva2 Aprile 2015: viene impostato il valore di default del TipoFormatoListaSintetica
		// nella ricerca per Gestionali nella casistiche di ricerca per Inventari e per Ordini dato che nelle Mappe
		// non è presente la tendina di selezione
		if (areaDatiPass.getInterTitGen().getFormatoListaSelez() == null) {
			areaDatiPass.getInterTitGen().setFormatoListaSelez(currentForm.getInterrGener().getFormatoListaSelez());
		}
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaTitoliPerGestionali(areaDatiPass, request.getLocale(), Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getNumNotizie() > 0) {
			request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);
			request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassReturn);
			if (currentForm.getProvenienza().equals("NEWLEGAME")) {
				request.setAttribute("AreaDatiLegameTitoloVO", currentForm.getAreaDatiLegameTitoloVO());
				return mapping.findForward("sinteticaTitoliPerLegame");
			} else {
				return mapping.findForward("sinteticaTitoli");
			}
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}
	}

//	public ActionForward creaTit(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;
//
//		request.setAttribute("tipoProspettazione", "INS");
//		DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
//		dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(currentForm.getInterrGener().getTitolo());
//		if (currentForm.getInterrGener().getTipoMateriale().equals("") ||
//				currentForm.getInterrGener().getTipoMateriale().equals("*")) {
//			dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
//		} else  {
//			dettTitComVO.getDetTitoloPFissaVO().setTipoMat(currentForm.getInterrGener().getTipoMateriale());
//		}
//
//		request.setAttribute("dettaglioTit", dettTitComVO);
//		request.setAttribute("bid", "");
//		request.setAttribute("desc", currentForm.getInterrGener().getTitolo());
//
//		resetToken(request);
//		return mapping.findForward("creaTitoloPartecipato");
//	}

//	public ActionForward creaTitLocale(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//
//		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;
//
//		request.setAttribute("tipoProspettazione", "INS");
//		DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
//		dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(currentForm.getInterrGener().getTitolo());
//		if (currentForm.getInterrGener().getTipoMateriale().equals("") ||
//				currentForm.getInterrGener().getTipoMateriale().equals("*")) {
//			dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
//		} else  {
//			dettTitComVO.getDetTitoloPFissaVO().setTipoMat(currentForm.getInterrGener().getTipoMateriale());
//		}
//
//		request.setAttribute("dettaglioTit", dettTitComVO);
//		request.setAttribute("bid", "");
//		request.setAttribute("desc", currentForm.getInterrGener().getTitolo());
//
//		resetToken(request);
//		return mapping.findForward("creaTitoloLocale");
//
//	}

	public void caricaComboGenerTitolo(InterrogazioneTitoloGeneraleVO titGenVO) throws Exception {
		// Impostazione di inizializzazione jsp area generalizzata per tutti i
		// tipi materiale

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titGenVO.setListaTipiOrdinam(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceOrdinamentoTitoli()));
		titGenVO.setListaFormatoLista(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceMinMax()));
		titGenVO.setListaResponsabilita(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceResponsabilita()));
		titGenVO.setListaBiblioteche(carCombo.loadBiblioteche());

		List<String> arrayListVuoto = new ArrayList<String>();
		arrayListVuoto.add("");
		titGenVO.setListaLingue(arrayListVuoto);
		titGenVO.setListaNature(arrayListVuoto);
		titGenVO.setListaPaese(arrayListVuoto);
		titGenVO.setListaRelazioni(arrayListVuoto);
		titGenVO.setListaSottoNatureD(arrayListVuoto);
		titGenVO.setListaSottonatureD(arrayListVuoto);
		titGenVO.setListaSpecificita(arrayListVuoto);
		titGenVO.setListaTipiNumStandard(arrayListVuoto);
		titGenVO.setListaTipiRecord(arrayListVuoto);
		titGenVO.setListaTipoData(arrayListVuoto);

	}

	private List getListaSerie(String codPolo, String codBib, String ticket, ActionForm form) throws Exception {
		InterrogazioneTitoloPerGestionaliForm currentForm = (InterrogazioneTitoloPerGestionaliForm) form;
		List serie;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		if (serie == null ||  serie.size() <= 0)  {
			currentForm.setNoSerie(true);
		}
		return serie;
	}
	private SerieVO getSerieDettaglio(String codPolo, String codBib, String codSerie, String ticket) throws Exception {
		SerieVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSerieDettaglio(codPolo, codBib, codSerie, ticket);
		return rec;
	}


	public ActionForward sifBibOrdine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			UserVO utente = Navigation.getInstance(request).getUtente();
			BibliotecaDelegate delegate = new BibliotecaDelegate(request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(
					utente.getCodPolo(), utente.getCodBib(),
					CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI, 10,
					LISTA_BIBLIOTECHE_AFFILIATE_ORD);

			return delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sifBibInventario(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			BibliotecaDelegate biblio = new BibliotecaDelegate(request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(
					Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					//CodiciAttivita.getIstance().GDF_ESAME_POSSEDUTO,
					CodiciAttivita.getIstance().GDF_ESAME_COLLOCAZIONI,
					0, LISTA_BIBLIOTECHE_AFFILIATE_INV);
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."
					+ e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	private List getListaSerieDate(String codPolo, String codBib, String ticket, ActionForm form) throws Exception {
		InterrogazioneTitoloPerGestionaliForm myForm = (InterrogazioneTitoloPerGestionaliForm)form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List serie = factory.getGestioneDocumentoFisico().getListaSerieDate(codPolo, codBib, ticket);
		if (serie == null ||  serie.size() <= 0)  {
			myForm.setNoSerie(true);
		}
		return serie;
	}
}
