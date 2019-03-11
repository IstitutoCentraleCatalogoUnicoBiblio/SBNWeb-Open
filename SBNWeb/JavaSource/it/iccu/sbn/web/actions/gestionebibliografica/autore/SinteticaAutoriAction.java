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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.autore;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.SinteticaAutoriView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.autore.SinteticaAutoriForm;
import it.iccu.sbn.web.actions.common.SbnDownloadAction;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.MyLabelValueBean;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaAutoriAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(SinteticaAutoriAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "carBlocco");
		map.put("button.analiticaAut", "analiticaAut");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.creaAut", "creaAut");
		map.put("button.creaAutLoc", "creaAutLoc");
		map.put("button.esamina", "esamina");
		// intervento interno almaviva2 per creare lista di vid (su esempio della lista di titoli) da passare ad altre applicazioni
		map.put("button.confermaGestisci", "confermaGestisci");

		map.put("button.selAllAutori", "selezionaTutti");
		map.put("button.deSelAllAutori", "deSelezionaTutti");

		// tasti per la prospettazione dei simili
		map.put("button.gestSimili.riAggiorna", "tornaAdAggiornamento");
		map.put("button.gestSimili.confermaAgg", "confermaAggiornamento");
		map.put("button.gestSimili.fusione", "fondiOggetti");
		// tasti per la prospettazione dei titoli per creazione nuovo legame
		map.put("button.gestLegami.lega", "prospettaPerLegame");
		// tasti per la prospettazione dei simili a seguito di condividi notizia locale
		map.put("button.gestSimiliCondividi.catturaEFondi", "catturaEFondi");
		map.put("button.gestSimiliCondividi.catturaESpostaLegame", "catturaESpostaLegame");
		map.put("button.gestSimiliCondividi.variaAutorePerCatalog", "variaAutorePerCatalog");
		map.put("button.gestSimiliCondividi.tornaAnaliticaPerCondividi", "tornaAnaliticaPerCondividi");

		map.put("button.rinunciaFusione", "tornaAnalitica");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("SinteticaAutoriAction");
		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		/** INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE */
		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,"AU");
			utenteEjb.isAbilitatoAuthority("AU");
			sinteticaAutoriForm.setCreaAut("SI");
			sinteticaAutoriForm.setCreaAutLoc("SI");
		}catch(UtenteNotAuthorizedException ute)
		{
			sinteticaAutoriForm.setCreaAut("NO");
			sinteticaAutoriForm.setCreaAutLoc("NO");
		}
		/** FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */


		ActionForward forward = this.load(mapping, form, request, "NO");

		List<SinteticaAutoriView> listaSintetica = sinteticaAutoriForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			SinteticaAutoriView sinteticaAutoriView = listaSintetica.get(0);
			sinteticaAutoriForm.setSelezRadio(sinteticaAutoriView.getKeyVidNome());
		}

		if (forward == null)  {
			sinteticaAutoriForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, (SinteticaAutoriForm) form);
			return mapping.getInputForward();
		}

		String path = forward.getPath();
		if (!path.endsWith(".do"))
			forward.setPath(path.replace(".", "/"));
		return forward;
	}


	private ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, String internCall) throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		sinteticaAutoriForm.setTipologiaTastiera("");
		sinteticaAutoriForm.setUtilizzoComeSif("NO");
		sinteticaAutoriForm.setProspettazioneSimili("NO");
		sinteticaAutoriForm.setProspettazionePerLegami("NO");
		sinteticaAutoriForm.setProspettaDatiOggColl("NO");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;

		if (request.getParameter("SINTSIMILICONDIVIDI") != null) {
			sinteticaAutoriForm.setProspettaSimiliPerCondividi("SI");
			if (request.getAttribute("bidRoot") != null && request.getAttribute("bidRoot").equals("NO")) {
				sinteticaAutoriForm.setTipologiaTastiera("ProspettaSimiliPerCondividiNoRadice");
			} else {
				sinteticaAutoriForm.setTipologiaTastiera("ProspettaSimiliPerCondividi");
			}

			if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
				sinteticaAutoriForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			}

			sinteticaAutoriForm.setLivRicerca("I");
			if (request.getAttribute("areaDatiPassPerConfVariazione") != null){
				sinteticaAutoriForm.setAreaDatiPassPerConfVariazione((AreaDatiVariazioneAutoreVO) request.getAttribute("areaDatiPassPerConfVariazione"));
				sinteticaAutoriForm.setProspettaDatiOggColl("SI");
				sinteticaAutoriForm.setIdOggColl(sinteticaAutoriForm
						.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getVid());
				sinteticaAutoriForm.setDescOggColl(sinteticaAutoriForm
						.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getNome());

				if (request.getAttribute("areaDatiPassReturnSintetica") != null) {
					areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request.getAttribute("areaDatiPassReturnSintetica");
					if (areaDatiPassReturn.getNumNotizie() == 1) {
						SinteticaAutoriView sinteticaAutoriViewElem = new SinteticaAutoriView();
						sinteticaAutoriViewElem = (SinteticaAutoriView) areaDatiPassReturn.getListaSintetica().get(0);
						if (sinteticaAutoriForm.getIdOggColl().equals(sinteticaAutoriViewElem.getVid())) {
							// BUG MANTIS 3344 - almaviva2 09.12.2009 - nuova tipologia tastiera per identico
							// Modificata anche la jsp sinteticaAutori
							sinteticaAutoriForm.setTipologiaTastiera("ProspettaIdenticoPerCondividi");
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricOggettoGiaPresenteInIndice", sinteticaAutoriForm.getIdOggColl()));
							this.saveErrors(request, errors);
						}
					}
				}
			}
		}


		if (request.getParameter("SIFSINTETICA") != null || internCall.equals("SI")) {
			sinteticaAutoriForm.setProspettaDatiOggColl("SI");
			sinteticaAutoriForm.setUtilizzoComeSif("SI");
			sinteticaAutoriForm.setTipologiaTastiera("UtilizzoComeSif");
			ActionForward forward = this.gestioneModalitaSif(request, sinteticaAutoriForm, internCall);
			if (forward == null) {
				return null;
			} else {
				String path = forward.getPath();
				if (!path.endsWith(".do"))
					forward.setPath(path.replace(".", "/"));
				return forward;
			}
		}

		if (request.getParameter("SINTSIMILI") != null) {
			sinteticaAutoriForm.setProspettaDatiOggColl("SI");
			sinteticaAutoriForm.setProspettazioneSimili("SI");

			sinteticaAutoriForm.setTipologiaTastiera("ProspettazioneSimili");
			sinteticaAutoriForm.setAreaDatiPassPerConfVariazione((AreaDatiVariazioneAutoreVO) request
							.getAttribute("areaDatiPassPerConfVariazione"));
			sinteticaAutoriForm.setIdOggColl(sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getVid());
			sinteticaAutoriForm.setDescOggColl(sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getNome());

			if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
				sinteticaAutoriForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
				sinteticaAutoriForm.setProspettazionePerLegami("SI");
			}

			mapping.getInputForward();
		}

		if (request.getParameter("SINTNEWLEGAME") != null) {
			sinteticaAutoriForm.setProspettazionePerLegami("SI");
			sinteticaAutoriForm.setTipologiaTastiera("ProspettazionePerLegami");
			sinteticaAutoriForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			sinteticaAutoriForm.setProspettaDatiOggColl("SI");
			sinteticaAutoriForm.setIdOggColl(sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			sinteticaAutoriForm.setDescOggColl(sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getDescPartenza());
//			(Inizio 5Agosto2009)
			if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
				sinteticaAutoriForm.setTipologiaTastiera("ProspettazionePerFusioneOnLine");
			}
		}

		if (request.getAttribute("areaDatiPassReturnSintetica") == null) {
			// QUESTO INTERVENTO DOVREBBE NON FAR PROSPETTARE IL TASTO CERCA IN INDICE QUANDO NON SONO
			// DISPONIBILI I DATI PER EFFETTUARLA (SI PRESUME PERCHE' E' IL RIENTRO DA SIF)
			if (sinteticaAutoriForm.getDatiInterrAutore() != null) {
				if (sinteticaAutoriForm.getDatiInterrAutore().getInterrGener() != null) {
					if (sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getTipoOrdinamSelez() == null) {
						sinteticaAutoriForm.setTipologiaTastiera("UtilizzoComeSif");
					}
				}
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			return null;
		}

		areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
			.getAttribute("areaDatiPassReturnSintetica");
		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaAutoriForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaAutoriForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaAutoriForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaAutoriForm.setNumAutori(areaDatiPassReturn.getNumNotizie());
		sinteticaAutoriForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaAutoriForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaAutoriForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());
		sinteticaAutoriForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());

		sinteticaAutoriForm.setDatiInterrAutore((AreaDatiPassaggioInterrogazioneAutoreVO) request
						.getAttribute("areaDatiPassPerInterrogazione"));

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaAutoriForm.setAppoggio(appoggio);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaAutoriForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaAutoriForm);

		return null;
	}

	private ActionForward gestioneModalitaSif(HttpServletRequest request,
			SinteticaAutoriForm sinteticaAutoriForm, String internCall) throws RemoteException, NamingException, CreateException {

		if (!internCall.equals("SI")) {
			sinteticaAutoriForm.getAreePassSifVo().setLivelloRicerca(
					((Integer) (request
							.getAttribute(TitoliCollegatiInvoke.livDiRicerca)))
							.intValue());
			sinteticaAutoriForm.getAreePassSifVo().setOggettoDaRicercare(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			sinteticaAutoriForm
					.getAreePassSifVo()
					.setDescOggettoDaRicercare(
							(String) request
									.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));

			sinteticaAutoriForm
			.getAreePassSifVo()
			.setTipMatOggetto(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));

			sinteticaAutoriForm
			.getAreePassSifVo()
			.setNaturaOggetto(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));

			sinteticaAutoriForm.getAreePassSifVo().setOggettoRicerca(
					((Integer) (request
							.getAttribute(TitoliCollegatiInvoke.oggDiRicerca)))
							.intValue());
			sinteticaAutoriForm.getAreePassSifVo().setOggettoChiamante(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.oggChiamante));
			sinteticaAutoriForm.getAreePassSifVo().setCodBiblioteca(
				(String) request
							.getAttribute(TitoliCollegatiInvoke.codBiblio));
			if (((String) request
				.getAttribute(TitoliCollegatiInvoke.visualCall))
				.equals("SI")) {
				sinteticaAutoriForm.getAreePassSifVo().setVisualCall(true);
			} else {
				sinteticaAutoriForm.getAreePassSifVo()
						.setVisualCall(false);
			}
		}

		sinteticaAutoriForm.setIdOggColl(sinteticaAutoriForm.getAreePassSifVo().getOggettoDaRicercare());
		sinteticaAutoriForm.setDescOggColl(sinteticaAutoriForm.getAreePassSifVo().getDescOggettoDaRicercare());

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass = new AreaDatiPassaggioInterrogazioneAutoreVO();
		areaDatiPass
				.setOggChiamante(TitoliCollegatiInvoke.ANALITICA_DETTAGLIO);
		areaDatiPass.setTipoOggetto(sinteticaAutoriForm.getAreePassSifVo()
				.getOggettoRicerca());

		areaDatiPass.setOggDiRicerca(sinteticaAutoriForm
				.getAreePassSifVo().getOggettoDaRicercare());

		areaDatiPass.setTipMatTitBase(sinteticaAutoriForm
				.getAreePassSifVo().getTipMatOggetto());
		areaDatiPass.setNaturaTitBase(sinteticaAutoriForm
				.getAreePassSifVo().getNaturaOggetto());

		areaDatiPass.setInterrGener(null);

		areaDatiPass
				.setRicercaIndice(sinteticaAutoriForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areaDatiPass
				.setRicercaPolo(sinteticaAutoriForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().ricercaAutoriCollegati(areaDatiPass, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			ActionForward actionForward = new ActionForward();
			String temp = sinteticaAutoriForm.getAreePassSifVo().getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do"))  {
				actionForward.setPath(sinteticaAutoriForm.getAreePassSifVo().getOggettoChiamante() + ".do");
			} else  {
				actionForward.setPath(sinteticaAutoriForm.getAreePassSifVo().getOggettoChiamante());
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")){
				actionForward.setPath(path.replace(".", "/"));
			}
			actionForward.setRedirect(true);
			return actionForward;
		}

		if (!sinteticaAutoriForm.getAreePassSifVo().isVisualCall()) {
			request.setAttribute(TitoliCollegatiInvoke.arrayListSintetica,
					areaDatiPassReturn.getListaSintetica());
			ActionForward actionForward = new ActionForward();
			actionForward.setPath(sinteticaAutoriForm.getAreePassSifVo().getOggettoChiamante() + ".do");
			String path = actionForward.getPath();
			if (!path.endsWith(".do")){
				actionForward.setPath(path.replace(".", "/"));
			}
			return actionForward;
		}

		// Impostazione della form con i valori tornati dal Server
		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaAutoriForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaAutoriForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaAutoriForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaAutoriForm.setNumAutori(areaDatiPassReturn.getNumNotizie());
		sinteticaAutoriForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaAutoriForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaAutoriForm.setLivRicerca(areaDatiPassReturn.getLivelloTrovato());
		sinteticaAutoriForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());
		return null;
	}



	public ActionForward carBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaAutoriForm.getNumBlocco() == 0
				|| sinteticaAutoriForm.getNumBlocco() > sinteticaAutoriForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		HashSet<Integer> appoggio = sinteticaAutoriForm.getAppoggio();
		int i = sinteticaAutoriForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(sinteticaAutoriForm.getNumBlocco());
		areaDatiPass.setMaxRighe(sinteticaAutoriForm.getMaxRighe());
		areaDatiPass.setIdLista(sinteticaAutoriForm.getIdLista());
		areaDatiPass.setTipoOrdinam(sinteticaAutoriForm.getTipoOrd());
		areaDatiPass.setTipoOutput(sinteticaAutoriForm.getTipoOutput());

		if (sinteticaAutoriForm.getLivRicerca().equals("I")) {
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(true);
		} else {
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setRicercaIndice(false);
		}
		UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory.getGestioneBibliografica().getNextBloccoAutori(areaDatiPass, utenteCollegato.getTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		sinteticaAutoriForm.setIdLista(areaDatiPassReturn.getIdLista());
		sinteticaAutoriForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco =sinteticaAutoriForm.getNumBlocco();
		appoggio = sinteticaAutoriForm.getAppoggio();
		appoggio.add(numBlocco);
		sinteticaAutoriForm.setAppoggio(appoggio);

		// Modifica per generalizzazione testata sintetica 
		// sinteticaAutoriForm.setNumBlocco(++numBlocco);

        List<SinteticaAutoriView> listaSinteticaOriginale = sinteticaAutoriForm.getListaSintetica();
        listaSinteticaOriginale.addAll(areaDatiPassReturn.getListaSintetica());
        Collections.sort(listaSinteticaOriginale, SinteticaAutoriView.sortListaSinteticaAut);

        sinteticaAutoriForm.setListaSintetica(listaSinteticaOriginale);

		// Si prospetta la mappa Sintetica autore con la lista dei autori trovati
		sinteticaAutoriForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaAutoriForm);
		return mapping.getInputForward();
	}

	public ActionForward analiticaAut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO() == null) {
			request.setAttribute("presenzaTastoVaiA", "SI");
		} else {
			if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null
						|| sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals("")) {
				request.setAttribute("presenzaTastoVaiA", "SI");
			} else {
				request.setAttribute("presenzaTastoVaiA", "NO");
			}
		}


		Integer progressivo = sinteticaAutoriForm.getLinkProgressivo();
		if (progressivo != null) {
			SinteticaAutoriView eleSinteticaAutoriView = null;
			int size = sinteticaAutoriForm.getListaSintetica().size();
			for (int i = 0; i < size; i++) {
				eleSinteticaAutoriView = (SinteticaAutoriView) sinteticaAutoriForm.getListaSintetica().get(i);
				if (eleSinteticaAutoriView.getProgressivo() == progressivo) {
					sinteticaAutoriForm.setSelezRadio(eleSinteticaAutoriView.getVid());
					break;
				}
			}
		}

		// Mofifica almaviva2 BUG MANTIS 3383 - inserito controllo per selezione di un solo check si invia solo il bid
		// e non la tabella;
		// Inizio Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione
		// così da inviarla correttamente
		String[] listaVidSelezOld;
		int cont = -1;
		if (sinteticaAutoriForm.getSelezCheck() != null	&& sinteticaAutoriForm.getSelezCheck().length > 0) {
			int length = sinteticaAutoriForm.getSelezCheck().length;
			listaVidSelezOld = new String[length];
			for (int i = 0; i < length; i++) {
				if (sinteticaAutoriForm.getSelezCheck()[i] != null && sinteticaAutoriForm.getSelezCheck()[i].length()> 0) {
					listaVidSelezOld[++cont] = sinteticaAutoriForm.getSelezCheck()[i];
				}
			}

			if (cont <0) {
				if (sinteticaAutoriForm.getSelezRadio() != null
						&& !sinteticaAutoriForm.getSelezRadio().equals("")) {
					String vidNome = sinteticaAutoriForm.getSelezRadio();
					request.setAttribute("bid", vidNome.substring(0, 10));
					request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
					request.setAttribute("tipoAuthority", "AU");

					if (sinteticaAutoriForm.getUtilizzoComeSif() != null && sinteticaAutoriForm.getLivRicerca() != null) {
						if (sinteticaAutoriForm.getUtilizzoComeSif().equals("SI") && sinteticaAutoriForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			} else if (cont == 0) {
				request.setAttribute("bid", listaVidSelezOld[0]);
				request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
				request.setAttribute("tipoAuthority", "AU");
				resetToken(request);

				if (sinteticaAutoriForm.getUtilizzoComeSif() != null && sinteticaAutoriForm.getLivRicerca() != null) {
					if (sinteticaAutoriForm.getUtilizzoComeSif().equals("SI") && sinteticaAutoriForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));

			} else {
//				String[] listaBidSelez = sinteticaTitoliForm.getSelezCheck();
				String[] listaVidSelez = listaVidSelezOld;
				if (listaVidSelez[0] != null) {
					request.setAttribute("bid", listaVidSelez[0]);
					request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
					request.setAttribute("listaBidSelez", listaVidSelez);
					resetToken(request);

					if (sinteticaAutoriForm.getUtilizzoComeSif() != null && sinteticaAutoriForm.getLivRicerca() != null) {
						if (sinteticaAutoriForm.getUtilizzoComeSif().equals("SI") && sinteticaAutoriForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			}
		} else {
			if (sinteticaAutoriForm.getSelezRadio() != null
					&& !sinteticaAutoriForm.getSelezRadio().equals("")) {
				String vidNome = sinteticaAutoriForm.getSelezRadio();
				request.setAttribute("bid", vidNome.substring(0, 10));
				request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
				request.setAttribute("tipoAuthority", "AU");

				if (sinteticaAutoriForm.getUtilizzoComeSif() != null && sinteticaAutoriForm.getLivRicerca() != null) {
					if (sinteticaAutoriForm.getUtilizzoComeSif().equals("SI") && sinteticaAutoriForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
			}
		}
		// Fine Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);

//			if (listaVidSelez[0] != null) {
//				int j = listaVidSelez.length;
//				for (int i = 0; i < j; i++) {
//					request.setAttribute("bid", listaVidSelez[0]);
//					request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
//					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
//					request.setAttribute("listaBidSelez", listaVidSelez);
//				}
//				if (sinteticaAutoriForm.getUtilizzoComeSif() != null && sinteticaAutoriForm.getLivRicerca() != null) {
//					if (sinteticaAutoriForm.getUtilizzoComeSif().equals("SI") && sinteticaAutoriForm.getLivRicerca().equals("P")) {
//						request.setAttribute("presenzaTastoCercaInIndice", "NO");
//					}
//				}
//				resetToken(request);
//				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
//			}
//		}
//
//
//		if (sinteticaAutoriForm.getSelezRadio() != null) {
//			String vidNome = sinteticaAutoriForm.getSelezRadio();
//			request.setAttribute("bid", vidNome.substring(0, 10));
//			request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
//			request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
//			request.setAttribute("tipoAuthority", "AU");
//
//			if (sinteticaAutoriForm.getUtilizzoComeSif() != null && sinteticaAutoriForm.getLivRicerca() != null) {
//				if (sinteticaAutoriForm.getUtilizzoComeSif().equals("SI") && sinteticaAutoriForm.getLivRicerca().equals("P")) {
//					request.setAttribute("presenzaTastoCercaInIndice", "NO");
//				}
//			}
//			resetToken(request);
//			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
//		} else {
//			ActionMessages errors = new ActionMessages();
//			errors.add("generico", new ActionMessage(
//					"errors.gestioneBibliografica.selObblOggSint"));
//			this.saveErrors(request, errors);
//		}
		return mapping.getInputForward();
	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati nell'Interrogazione
		sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().setRicLocale(false);
		sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().setRicIndice(true);

		sinteticaAutoriForm.getDatiInterrAutore().setRicercaIndice(true);
		sinteticaAutoriForm.getDatiInterrAutore().setRicercaPolo(false);
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;
		UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaAutori(sinteticaAutoriForm.getDatiInterrAutore(), utenteCollegato.getTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		sinteticaAutoriForm.setIdLista(areaDatiPassReturn.getIdLista());
		sinteticaAutoriForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaAutoriForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaAutoriForm.setNumAutori(areaDatiPassReturn.getNumNotizie());
		sinteticaAutoriForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaAutoriForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaAutoriForm.setLivRicerca(areaDatiPassReturn.getLivelloTrovato());

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaAutoriForm.setAppoggio(appoggio);

		List<SinteticaAutoriView> listaSinteticaOriginale = areaDatiPassReturn
				.getListaSintetica();
		sinteticaAutoriForm.setListaSintetica(listaSinteticaOriginale);

		// almaviva2 Luglio 2018 riportata modifica del 01.12.2009 BUG MANTIS 3385  inserito puntamento fisso al primo elemento
		// fatta sulla sintetica titoli (altrimenti passando da analitica in locale ad analitica in Indice se l'elemento selezionato
		// sulla prima è assente dalla seconda nella lista non è selezionato alcun elemento)
		if (ValidazioneDati.isFilled(listaSinteticaOriginale)) {
			//selezione primo elemento
			SinteticaAutoriView sinteticaAutoriView = listaSinteticaOriginale.get(0);
			sinteticaAutoriForm.setSelezRadio(sinteticaAutoriView.getKeyVidNome());
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli trovati
		sinteticaAutoriForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaAutoriForm);
		return mapping.getInputForward();

	}

	public ActionForward creaAut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaAutoriForm.getProspettazionePerLegami().equals("NO")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			dettAutVO.setNome(sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			resetToken(request);
			return mapping.findForward("creaAutore");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setDescArrivo("");

			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaAutoriForm.getAreaDatiLegameTitoloVO());

			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			dettAutVO.setNome(sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			resetToken(request);
			return mapping.findForward("creaAutore");
		}
	}

	public ActionForward creaAutLoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaAutoriForm.getProspettazionePerLegami().equals("NO")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			dettAutVO.setNome(sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			resetToken(request);
			return mapping.findForward("creaAutoreLocale");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setDescArrivo("");

			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaAutoriForm.getAreaDatiLegameTitoloVO());

			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			dettAutVO.setNome(sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaAutoriForm.getDatiInterrAutore().getInterrGener().getNome());
			resetToken(request);
			return mapping.findForward("creaAutoreLocale");
		}

	}

	// intervento interno almaviva2 per creare lista di vid (su esempio della lista di titoli) da passare ad altre applicazioni
	public ActionForward confermaGestisci (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		// Mappatura dei codici della combo di Gestisci:
		//		"03"-->"Salva identificativi su file"

		if (sinteticaAutoriForm.getGestisciAutSelez().equals("00")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblLisEsamina"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (sinteticaAutoriForm.getGestisciAutSelez().equals("03")) {
			return this.salvaSuFile( mapping, sinteticaAutoriForm, request,  response);
		}

		return mapping.getInputForward();

	}




	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm currentForm = (SinteticaAutoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

//		Inizio almaviva2 12.11.2009 BUG MANTIS 3330
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || request.getParameter("SIFSINTETICA") != null) {
			if (currentForm.getAreePassSifVo().getOggettoDaRicercare() != null) {
				if (!currentForm.getAreePassSifVo().getOggettoDaRicercare().equals("")) {
					if (!currentForm.getAreePassSifVo().getOggettoDaRicercare().equals("0")) {
						currentForm.setIdOggColl(currentForm.getAreePassSifVo().getOggettoDaRicercare());
						currentForm.setDescOggColl(currentForm.getAreePassSifVo().getDescOggettoDaRicercare());
					}
				}
			}
			return mapping.getInputForward();
		}
//		Fine almaviva2 12.11.2009 BUG MANTIS 3330


		if (currentForm.getSelezRadio() != null) {
			if (currentForm.getEsaminaAutSelez().length() > 0) {

				SinteticaAutoriView autore = null;
				String keyDesc = currentForm.getSelezRadio();
				String vid = keyDesc.substring(0, 10);
				String desDaRic = "";
				String vidDaRic = "";


				int size = ValidazioneDati.size(currentForm.getListaSintetica());
				for (int i = 0; i < size; i++) {
					autore = (SinteticaAutoriView) currentForm.getListaSintetica().get(i);
					if (autore.getVid().equals(vid)) {
						//almaviva5_20100618 #3775 descrizione forma accettata
						// Inizio Intervento almaviva2 - Febbraio 2014 Mantis esercizio 5497
						// Correzione dell'errore in visualizzazione autore partendo dalla lista autori-> esame titoli collegati
						// Nel campo della descrizione autore fa vedere l'autore in modo errato: Azeglio, Massimo : d&0000039;
						// invece di Azeglio, Massimo : d'
						// passando dall'Analitica il testo ivece è corretto
//						desDaRic = autore.isRinvio() ? autore.getNomeAccettata() : autore.getNome();
						desDaRic = autore.isRinvio() ? autore.getNomeAccettata() : autore.getOriginal_nome();
						// Fine Intervento almaviva2 - Febbraio 2014 Mantis esercizio 5497
						desDaRic = desDaRic.replace("&lt;", "<");
						desDaRic = desDaRic.replace("&gt;", ">");
						vidDaRic = autore.getVidAccettata();
						break;
					}
				}

				String myForwardName = currentForm.getTabellaEsaminaVO().getMyForwardName(
						currentForm.getMyPath(), currentForm
								.getEsaminaAutSelez());
				String myForwardPath = currentForm.getTabellaEsaminaVO().getMyForwardPath(
						currentForm.getMyPath(), currentForm
								.getEsaminaAutSelez());

				if (myForwardPath.equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}


				String myModeCall = currentForm.getTabellaEsaminaVO().getMyModeCall(currentForm
						.getMyPath(), currentForm.getEsaminaAutSelez());
				String myAction = currentForm.getTabellaEsaminaVO().getMyAction(currentForm
						.getMyPath(), currentForm.getEsaminaAutSelez());

				if (currentForm.getLivRicerca().equals("B")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					currentForm.getAreePassSifVo()
						.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					request.setAttribute(TitoliCollegatiInvoke.codBiblio,
									"011");
				}
				if (currentForm.getLivRicerca().equals("P")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					currentForm.getAreePassSifVo()
					.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
				}
				if (currentForm.getLivRicerca().equals("I")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
					currentForm.getAreePassSifVo()
					.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
				}

				Class<TitoliCollegatiInvoke> genericClass = TitoliCollegatiInvoke.class;
				int appoggio = 0;
				try {
					Field genericField = genericClass.getField(myForwardName);
					appoggio = genericField.getInt(null);
				} catch (NoSuchFieldException e) {
				}

				currentForm.getAreePassSifVo().setOggettoRicerca(appoggio);
				currentForm.getAreePassSifVo().setOggettoDaRicercare(vidDaRic);
				currentForm.getAreePassSifVo().setDescOggettoDaRicercare(desDaRic);
				currentForm.getAreePassSifVo().setOggettoChiamante(mapping.getPath());
				currentForm.getAreePassSifVo().setVisualCall(true);

				if (myAction.replace(".", "/").equals(
						myForwardPath.substring(0, myForwardPath.length() - 3))) {
					ActionForward forward = this.load(mapping, form, request, "SI");
					if (forward == null) {
						mapping.getInputForward();
					}
				} else  {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							currentForm.getAreePassSifVo()
									.getLivelloRicerca());
					request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
							currentForm.getAreePassSifVo()
									.getOggettoDaRicercare());
					request.setAttribute(
							TitoliCollegatiInvoke.xidDiRicercaDesc,
							currentForm.getAreePassSifVo()
									.getDescOggettoDaRicercare());
					request.setAttribute(TitoliCollegatiInvoke.codBiblio,
							currentForm.getAreePassSifVo()
									.getCodBiblioteca());
					if (currentForm.getAreePassSifVo()
							.isVisualCall()) {
						request.setAttribute(TitoliCollegatiInvoke.visualCall,
								"SI");
					} else {
						request.setAttribute(TitoliCollegatiInvoke.visualCall,
								"NO");
					}
					request.setAttribute(TitoliCollegatiInvoke.oggChiamante,
							mapping.getPath());


					request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca,
							appoggio);

					ActionForward actionForward = new ActionForward();
					actionForward.setName(myForwardName);
					actionForward.setPath(myForwardPath + "?SIFSINTETICA=TRUE");
					return navi.goForward(actionForward);
					//return actionForward;
				}
				ActionForward actionForward = new ActionForward();
				actionForward.setName(myForwardName);
				String nextPath;
				if (myModeCall.equals("jsp")) {
					nextPath = (mapping.getModuleConfig().findActionConfig(
							myForwardPath.substring(0,
									myForwardPath.length() - 3)).getInput());
				} else {
					nextPath = myForwardPath;
				}

				actionForward.setPath(nextPath);
				return actionForward;
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica.selObblLisEsamina"));
				this.saveErrors(request, errors);
			}
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
		}
		return mapping.getInputForward();
	}


	// metodo per il caricamento della JSP Sintetica autori
	private void aggiornaForm(HttpServletRequest request,
			SinteticaAutoriForm sinteticaAutoriForm) {

		sinteticaAutoriForm.setTabellaEsaminaVO((TabellaEsaminaVO) this.getServlet().getServletContext()
				.getAttribute("serverTypes"));
		List<MyLabelValueBean> listaCaricamento = sinteticaAutoriForm.getTabellaEsaminaVO().getLista(sinteticaAutoriForm.getMyPath());

		MyLabelValueBean eleListaCar;
		List<ComboSoloDescVO> lista = new ArrayList<ComboSoloDescVO>();

		ComboSoloDescVO listaEsamina = new ComboSoloDescVO();
		listaEsamina.setDescrizione("");
		lista.add(listaEsamina);

		int i;
		int size = listaCaricamento.size();
		for (i = 0; i < size; i++) {
			eleListaCar = listaCaricamento.get(i);
			listaEsamina = new ComboSoloDescVO();
			if (eleListaCar.getMyLivelloBaseDati().equals("A")
					|| eleListaCar.getMyLivelloBaseDati().equals(sinteticaAutoriForm.getLivRicerca())) {
				listaEsamina.setDescrizione(eleListaCar.getMyLabel());
				lista.add(listaEsamina);
			}
		}
		sinteticaAutoriForm.setListaEsaminaAut(lista);


		// intervento interno almaviva2 per creare lista di vid (su esempio della lista di titoli) da passare ad altre applicazioni
		ComboCodDescVO listaGestisci = new ComboCodDescVO();
		List<ComboCodDescVO> listaNew = new ArrayList<ComboCodDescVO>();

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("00");
		listaGestisci.setDescrizione("");
		listaNew.add(listaGestisci);

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("03");
		listaGestisci.setDescrizione("Salva identificativi su file");
		listaNew.add(listaGestisci);
		sinteticaAutoriForm.setListaGestisciAut(listaNew);
		return;
	}

	public ActionForward tornaAdAggiornamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward confermaAggiornamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		// VIENE ASTERISCATO il diagnostico e si riattiva l'inserimento in forzatura

//		// INIZIO
//		// Per gli autori non è possibile forzare un aggiornamento in presenza di simili
//		// si invia il diagnostico ma non si cancella la funzione perchè le cose
//		// potrebbero cambiare (Vedi anche il fondiOggettiNew)
//		ActionMessages errors = new ActionMessages();
//		errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.forzaturaAutoreNoDisp"));
//		this.saveErrors(request, errors);
//		return mapping.getInputForward();


		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		// Imposto il flag di conferma a TRUE affinchè sia effettuata la
		// variazione senza ricerca simili
		sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().setConferma(true);

//
//
//
//
//
//		sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().setInserimentoIndice(true);
//
//
//
//
//

		UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPassReturn = factory.getGestioneBibliografica()
					.inserisciAutore(sinteticaAutoriForm.getAreaDatiPassPerConfVariazione(), utenteCollegato.getTicket());

		if (areaDatiPassReturn == null) {
			//request.setAttribute("bid", areaDatiPassReturn.getBid());
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}
		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			request.setAttribute("bid", areaDatiPassReturn.getBid());
			request.setAttribute("livRicerca", "I");

			if (sinteticaAutoriForm.getAreaDatiPassPerConfVariazione() != null
					&& sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().isFlagCondiviso() == false) {
				request.setAttribute("livRicerca", "P");
			}

			if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null) {
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				return mapping.findForward("analitica");
			} else {
				sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setIdArrivo(areaDatiPassReturn.getBid());
				sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
				sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo(sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getTipoNome());
				sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setDescArrivo(sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getNome());
				sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().isFlagCondiviso());

				request.setAttribute("AreaDatiLegameTitoloVO", sinteticaAutoriForm.getAreaDatiLegameTitoloVO());
				if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null) {
					return mapping.findForward("gestioneLegameTitoloAutore");
				}
				if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("AU") ||
						sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("MA")) {
					return mapping.findForward("gestioneLegameFraAutority");
				}
			}


		}
		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();

		// FINE
	}


	public ActionForward fondiOggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (sinteticaAutoriForm.getSelezRadio() != null) {
			String vidAccorpante = sinteticaAutoriForm.getSelezRadio().substring(0,10);
			String vidEliminato = sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getVid();


			it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try{
				utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().FONDE_AUTORE_1270, "AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

			String tipoMatEliminato = "";
			String tipoMatAccorpante = "";

			String tipoAutEliminato = (SbnAuthority.AU).toString();
			String tipoAutAccorpante = (SbnAuthority.AU).toString();

			if (tipoMatAccorpante.equals(tipoMatEliminato)
					&& tipoAutAccorpante.equals(tipoAutEliminato)) {
				AreaDatiAccorpamentoVO areaDatiPass = new AreaDatiAccorpamentoVO();
				AreaDatiAccorpamentoReturnVO areaDatiPassReturn = new AreaDatiAccorpamentoReturnVO();
				areaDatiPass.setIdElementoEliminato(vidEliminato);
				areaDatiPass.setIdElementoAccorpante(vidAccorpante);
				if (!tipoAutAccorpante.equals("")) {
					areaDatiPass.setTipoAuthority(SbnAuthority.valueOf(tipoAutAccorpante));
				}

				areaDatiPass.setTipoMateriale(tipoMatAccorpante);
				areaDatiPass.setLivelloBaseDati("I");
				UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.richiestaAccorpamento(areaDatiPass, utenteCollegato.getTicket());
				} catch (RemoteException e) {
				}

				if (areaDatiPassReturn == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica.noConnessione"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				if (areaDatiPassReturn.getCodErr().equals("3142")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica."
									+ areaDatiPassReturn.getCodErr()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				if (areaDatiPassReturn.getCodErr().equals("0000")) {
					request.setAttribute("bid", areaDatiPass.getIdElementoAccorpante());
					request.setAttribute("livRicerca", "I");
					request.setAttribute("vaiA", "SI");
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica.operOk"));
					this.saveErrors(request, errors);
					return mapping.findForward("analitica");
				}
				if (areaDatiPassReturn.getCodErr().equals("9999")
						|| areaDatiPassReturn.getTestoProtocollo() != null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(areaDatiPassReturn
							.getTestoProtocollo()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica."
									+ areaDatiPassReturn.getCodErr()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward prospettaPerLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm currentForm = (SinteticaAutoriForm) form;

		if (currentForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");

		SinteticaAutoriView autore = null;
		String vidDaLegare = currentForm.getSelezRadio().substring(0,10);
		int size = currentForm.getListaSintetica().size();
		for (int i = 0; i < size; i++) {
			autore = (SinteticaAutoriView) currentForm.getListaSintetica().get(i);
			if (autore.getVid().equals(vidDaLegare)) {

				// un oggetto condiviso può legarsi solo a elementi condivisi;
				if (currentForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
					if (!autore.isFlagCondiviso()) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.gestioneBibliografica.elemCondivisoConElemLocale"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}


				if (autore.isAccettata() ) {
					currentForm.getAreaDatiLegameTitoloVO().setIdArrivo(autore.getVid());
					String appoNome = autore.getNome();
					appoNome = appoNome.replace("&lt;", "<");
					appoNome = appoNome.replace("&gt;", ">");
					// Modifica almaviva2 Bug Mantis esercizio 4730 corretta la visualizzazione nella creazione
					// del legame del carattere apostrofo
					appoNome = appoNome.replace("&#39;", "'");
					currentForm.getAreaDatiLegameTitoloVO().setDescArrivo(appoNome);
					currentForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo(autore.getTipoNome());
				} else {
					currentForm.getAreaDatiLegameTitoloVO().setIdArrivo(autore.getVidAccettata());
					String appoNome = autore.getNomeAccettata();
					appoNome = appoNome.replace("&lt;", "<");
					appoNome = appoNome.replace("&gt;", ">");
					// Modifica almaviva2 Bug Mantis esercizio 4730 corretta la visualizzazione nella creazione
					// del legame del carattere apostrofo
					appoNome = appoNome.replace("&#39;", "'");
					currentForm.getAreaDatiLegameTitoloVO().setDescArrivo(appoNome);
					currentForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo(autore.getTipoNomeAccettata());
				}
				currentForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(autore.isFlagCondiviso());
				break;
			}
		}

		currentForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
		currentForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
		currentForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
		currentForm.getAreaDatiLegameTitoloVO().setSiciNew("");
		currentForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
		currentForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

		request.setAttribute("AreaDatiLegameTitoloVO", currentForm.getAreaDatiLegameTitoloVO());
		if (currentForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null
				|| currentForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("TU")
				|| currentForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("UM")) {
			return mapping.findForward("gestioneLegameTitoloAutore");
		}
		if (currentForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("AU")
				|| currentForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("MA")) {
			return mapping.findForward("gestioneLegameFraAutority");
		}
		return mapping.getInputForward();
	}


	public ActionForward selezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception{

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		SinteticaAutoriView eleSinteticaAutoriView = null;
		int numElem = sinteticaAutoriForm.getListaSintetica().size();
		String[] listaBidSelez = new String[numElem];
		for (int i = 0; i < numElem; i++) {
			eleSinteticaAutoriView = (SinteticaAutoriView) sinteticaAutoriForm.getListaSintetica().get(i);
			listaBidSelez[i] =  eleSinteticaAutoriView.getVid();
		}
		sinteticaAutoriForm.setSelezCheck(listaBidSelez);
		return mapping.getInputForward();
	}

	public ActionForward deSelezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;
		sinteticaAutoriForm.setSelezCheck(null);
		return mapping.getInputForward();
	}

	public ActionForward catturaEFondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (sinteticaAutoriForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String vidDaCatturare = sinteticaAutoriForm.getSelezRadio().substring(0,10);
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaAutoriView eleSinteticaAutoriView = null;
		int size = sinteticaAutoriForm.getListaSintetica().size();
		for (int i = 0; i < size; i++) {
			eleSinteticaAutoriView = (SinteticaAutoriView) sinteticaAutoriForm
					.getListaSintetica().get(i);
			if (eleSinteticaAutoriView.getVid().equals(vidDaCatturare)) {
				tipoAutDaCatturare = eleSinteticaAutoriView.getTipoAutority();

				// Modifica almaviva2 su Segnalazione interna di almaviva 18.05.2011
				// nel caso di selezione per fusione su forma di rinvio si deve inviare la richiesta sulla forma accettata collegata;
				if (!eleSinteticaAutoriView.isAccettata() ) {
					vidDaCatturare = eleSinteticaAutoriView.getVidAccettata();
				}
				break;
			}
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
		areaDatiPassCattura.setIdPadre(vidDaCatturare);
		areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
		areaDatiPassCattura.setInferioriDaCatturare(appo);

		UserVO utenteCollegato = (UserVO) request.getSession()
		.getAttribute(Constants.UTENTE_KEY);

		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaAutore(areaDatiPassCattura,
							utenteCollegato.getTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					"ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturnCattura == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
			if (areaDatiPassReturnCattura.getCodErr().equals("9999")
					|| areaDatiPassReturnCattura.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo",
						areaDatiPassReturnCattura.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica."
								+ areaDatiPassReturnCattura.getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}


		String bidAccorpante = "";
		String bidEliminato = "";
		String tipoMatEliminato = "";
		String tipoMatAccorpante = "";
		String tipoAutEliminato = "";
		String tipoAutAccorpante = "";

		if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {

			// Modifica almaviva2 su Segnalazione interna di almaviva 18.05.2011
			// nel caso di selezione per fusione su forma di rinvio si deve inviare la richiesta sulla forma accettata collegata;
			if (eleSinteticaAutoriView.isAccettata() ) {
				bidAccorpante = sinteticaAutoriForm.getSelezRadio().substring(0,10);
			} else {
				bidAccorpante = vidDaCatturare;
			}

			bidEliminato = sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza();
			tipoAutAccorpante = eleSinteticaAutoriView.getTipoAutority();
			tipoAutEliminato = eleSinteticaAutoriView.getTipoAutority();
		} else {
			// Modifica almaviva2 su Segnalazione interna di almaviva 18.05.2011
			// nel caso di selezione per fusione su forma di rinvio si deve inviare la richiesta sulla forma accettata collegata;
			if (eleSinteticaAutoriView.isAccettata() ) {
				bidAccorpante = sinteticaAutoriForm.getSelezRadio().substring(0,10);
			} else {
				bidAccorpante = vidDaCatturare;
			}
			bidEliminato = sinteticaAutoriForm.getAreaDatiPassPerConfVariazione().getDettAutoreVO().getVid();
			tipoAutAccorpante = eleSinteticaAutoriView.getTipoAutority();
			tipoAutEliminato = eleSinteticaAutoriView.getTipoAutority();

		}

		// In questo caso (l'oggetto è già presente in Indice) è stato già aggiornato il flag di condivisione in Polo dalla cattura
		// la transazione può concludersi qui.
		if (bidAccorpante.equals(bidEliminato)) {
			request.setAttribute("bid", sinteticaAutoriForm.getSelezRadio().substring(0,10));
			request.setAttribute("livRicerca", "I");
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030", bidEliminato,	bidAccorpante));
			this.saveErrors(request, errors);
			return mapping.findForward("analitica");
		}



//		(Fine 5Agosto2009)

		if (tipoMatAccorpante.equals(tipoMatEliminato)
				&& tipoAutAccorpante.equals(tipoAutEliminato)) {
			AreaDatiAccorpamentoVO areaDatiPass = new AreaDatiAccorpamentoVO();
			AreaDatiAccorpamentoReturnVO areaDatiPassReturn = new AreaDatiAccorpamentoReturnVO();
			areaDatiPass.setIdElementoEliminato(bidEliminato);
			areaDatiPass.setIdElementoAccorpante(bidAccorpante);
			if (!tipoAutAccorpante.equals("")) {
				areaDatiPass.setTipoAuthority(SbnAuthority
						.valueOf(tipoAutAccorpante));
			}
			areaDatiPass.setTipoMateriale(tipoMatAccorpante);
			areaDatiPass.setLivelloBaseDati("P");

			areaDatiPassReturn = factory.getGestioneBibliografica().richiestaAccorpamento(areaDatiPass, utenteCollegato.getTicket());

			if (areaDatiPassReturn == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			if (areaDatiPassReturn.getCodErr().equals("0000")) {
				request.setAttribute("bid", areaDatiPass.getIdElementoAccorpante());
				request.setAttribute("livRicerca", "I");
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030", bidEliminato, bidAccorpante));
				this.saveErrors(request, errors);
				return mapping.findForward("analitica");
			}

			if (areaDatiPassReturn.getCodErr().equals("9999")
					|| areaDatiPassReturn.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(areaDatiPassReturn
						.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica."
								+ areaDatiPassReturn.getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}



	public ActionForward catturaESpostaLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (sinteticaAutoriForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String vidDaCatturare = sinteticaAutoriForm.getSelezRadio().substring(0,10);
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaAutoriView eleSinteticaAutoriView = null;
		int size = sinteticaAutoriForm.getListaSintetica().size();
		for (int i = 0; i < size; i++) {
			eleSinteticaAutoriView = (SinteticaAutoriView) sinteticaAutoriForm
					.getListaSintetica().get(i);
			if (eleSinteticaAutoriView.getVid().equals(vidDaCatturare)) {
				tipoAutDaCatturare = eleSinteticaAutoriView.getTipoAutority();
				break;
			}
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
		areaDatiPassCattura.setIdPadre(vidDaCatturare);
		areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
		areaDatiPassCattura.setInferioriDaCatturare(appo);

		UserVO utenteCollegato = (UserVO) request.getSession()
		.getAttribute(Constants.UTENTE_KEY);

		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaAutore(areaDatiPassCattura,
							utenteCollegato.getTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					"ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturnCattura == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
			if (areaDatiPassReturnCattura.getCodErr().equals("9999")
					|| areaDatiPassReturnCattura.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo",
						areaDatiPassReturnCattura.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica."
								+ areaDatiPassReturnCattura.getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

//		Cancellazione del vecchio legame e creazione del nuovo con la stessa chiamata per evitare che rimangano
//		appese delle operazioni e si lasci il reticolo inconsistente

		// In questo caso (l'oggetto è già presente in Indice) è stato già aggiornato il flag di condivisione in Polo dalla cattura
		// la transazione può concludersi qui.
		if (sinteticaAutoriForm.getSelezRadio().equals(sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza())) {
			request.setAttribute("livRicerca", "P");
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			return mapping.findForward("analitica");
		}

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null) {

			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setIdArrivoNew(vidDaCatturare);
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setTipoOperazione("Sposta");

			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
			sinteticaAutoriForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);

			if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null) {
				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.inserisciLegameTitolo(sinteticaAutoriForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					return mapping.findForward("annulla");
				}
			} else if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("TU")
				|| sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("UM")) {
				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.collegaElementoAuthority(sinteticaAutoriForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					return mapping.findForward("annulla");
				}
			}

			if (areaDatiPassReturn == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			if (areaDatiPassReturn.getCodErr().equals("0000")) {
				request.setAttribute("livRicerca", "P");
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				return mapping.findForward("analitica");
			}

			if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica."
								+ areaDatiPassReturn.getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}



	public ActionForward tornaAnalitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (sinteticaAutoriForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			request.setAttribute("bid", sinteticaAutoriForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			request.setAttribute("livRicerca", "P");
		} else  {
			request.setAttribute("bid", sinteticaAutoriForm.getSelezRadio());
			request.setAttribute("livRicerca", sinteticaAutoriForm.getLivRicerca());
		}


		resetToken(request);
		return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
	}



	public ActionForward variaAutorePerCatalog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		request.setAttribute("tipoProspettazione", "AGG");
		request.setAttribute("flagCondiviso", false);

		request.setAttribute("dettaglioAut", sinteticaAutoriForm.getAreaDatiPassPerConfVariazione());
		resetToken(request);
		request.setAttribute("vid", sinteticaAutoriForm.getIdOggColl());
		request.setAttribute("desc", sinteticaAutoriForm.getDescOggColl());
		return mapping.findForward("dettaglioAutore");
	}

	public ActionForward tornaAnaliticaPerCondividi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;


		// Intervento interno almaviva2 30.03.2011 - inserito controllo su presenza della lista sintetica MAIL almaviva
		if (sinteticaAutoriForm.getListaSintetica() != null) {
			if (sinteticaAutoriForm.getListaSintetica().size() == 1
					&& ((SinteticaAutoriView) sinteticaAutoriForm.getListaSintetica().get(0)).getVid().equals(sinteticaAutoriForm.getIdOggColl())) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.forzaturaAutoreNoDisp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

			}
		}


		request.setAttribute("confermaInvioIndice", "CONFERMATO");
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();
		return navi.goBack(true);

	}


	public ActionForward salvaSuFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaAutoriForm sinteticaAutoriForm = (SinteticaAutoriForm) form;

		if (sinteticaAutoriForm.getSelezCheck() != null	&& sinteticaAutoriForm.getSelezCheck().length > 0) {
			String[] listaVidSelez = sinteticaAutoriForm.getSelezCheck();


			List<String> listaVid = new ArrayList<String>(listaVidSelez.length);

			for (String vid : listaVidSelez) {
				vid = ValidazioneDati.trimOrEmpty(vid);
				if (!ValidazioneDati.leggiXID(vid))
					continue;
				else
					listaVid.add(vid);
			}

			if (!ValidazioneDati.isFilled(listaVid)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
				return mapping.getInputForward();
			}

			StringBuilder buf = new StringBuilder(listaVid.size() * 12);
			for (String vid : listaVid)
				buf.append(vid).append(Constants.NEW_LINE);

			byte[] bytes = buf.toString().getBytes("UTF-8");
			return SbnDownloadAction.downloadFile(request, "ElencoIdentif_Data" + ".txt", bytes);

		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}



}
