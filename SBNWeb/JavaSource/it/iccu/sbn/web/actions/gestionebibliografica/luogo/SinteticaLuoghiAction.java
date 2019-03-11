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

package it.iccu.sbn.web.actions.gestionebibliografica.luogo;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiVariazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.SinteticaLuoghiView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.luogo.SinteticaLuoghiForm;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.MyLabelValueBean;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaLuoghiAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(SinteticaLuoghiAction.class);

	//private TabellaEsaminaVO tabella = null;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "carBlocco");
		map.put("button.analiticaLuo", "analiticaLuo");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.creaLuo", "creaLuo");
		map.put("button.creaLuoLoc", "creaLuoLoc");
		map.put("button.esamina", "esamina");

		map.put("button.selAllLuoghi", "selezionaTutti");
		map.put("button.deSelAllLuoghi", "deSelezionaTutti");

		// tasti per la prospettazione dei simili
		map.put("button.gestSimili.riAggiorna", "tornaAdAggiornamento");
		map.put("button.gestSimili.confermaAgg", "confermaAggiornamento");
		map.put("button.gestSimili.fusione", "fondiOggetti");
		// tasti per la prospettazione dei titoli per creazione nuovo legame
		map.put("button.gestLegami.lega", "prospettaPerLegame");

		map.put("button.gestSimiliCondividi.catturaEFondi", "catturaEFondi");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;
		log.debug("SinteticaLuoghiAction");
		/** INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE */
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,"LU");
			utenteEjb.isAbilitatoAuthority("LU");
			sinteticaLuoghiForm.setCreaLuo("SI");
			sinteticaLuoghiForm.setCreaLuoLoc("SI");
		}catch(UtenteNotAuthorizedException ute)
		{
			sinteticaLuoghiForm.setCreaLuo("NO");
			sinteticaLuoghiForm.setCreaLuoLoc("NO");
		}
		/** FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */



		ActionForward forward = this.load(mapping, form, request, "NO");

//		if (sinteticaLuoghiForm.getListaSintetica() != null) {
//			if (sinteticaLuoghiForm.getListaSintetica().size() == 1) {
//				SinteticaLuoghiView sinteticaLuoghiView = new SinteticaLuoghiView();
//				sinteticaLuoghiView = (SinteticaLuoghiView) sinteticaLuoghiForm.getListaSintetica().get(0);
//				sinteticaLuoghiForm.setSelezRadio(sinteticaLuoghiView.getLid());
//			}
//		}

		List<SinteticaLuoghiView> listaSintetica = sinteticaLuoghiForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			SinteticaLuoghiView sinteticaLuoghiView = listaSintetica.get(0);
			sinteticaLuoghiForm.setSelezRadio(sinteticaLuoghiView.getLid());
		}



		if (forward == null)  {
			sinteticaLuoghiForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, (SinteticaLuoghiForm) form);
			return mapping.getInputForward();
		}

		String path = forward.getPath();
		if (!path.endsWith(".do"))
			forward.setPath(path.replace(".", "/"));
		return forward;
	}


	private ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, String internCall) throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		sinteticaLuoghiForm.setTipologiaTastiera("");
		sinteticaLuoghiForm.setUtilizzoComeSif("NO");
		sinteticaLuoghiForm.setProspettazioneSimili("NO");
		sinteticaLuoghiForm.setProspettazionePerLegami("NO");
		sinteticaLuoghiForm.setProspettaDatiOggColl("NO");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;

		if (request.getParameter("SIFSINTETICA") != null || internCall.equals("SI")) {
			sinteticaLuoghiForm.setProspettaDatiOggColl("SI");
			sinteticaLuoghiForm.setUtilizzoComeSif("SI");
			sinteticaLuoghiForm.setTipologiaTastiera("UtilizzoComeSif");
			ActionForward forward = this.gestioneModalitaSif(request, sinteticaLuoghiForm, internCall);
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
			sinteticaLuoghiForm.setProspettaDatiOggColl("SI");
			sinteticaLuoghiForm.setProspettazioneSimili("SI");

			sinteticaLuoghiForm.setTipologiaTastiera("ProspettazioneSimili");
			sinteticaLuoghiForm.setAreaDatiPassPerConfVariazione((AreaDatiVariazioneLuogoVO) request
							.getAttribute("areaDatiPassPerConfVariazione"));
			sinteticaLuoghiForm.setIdOggColl(sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().getDettLuogoVO().getLid());
			sinteticaLuoghiForm.setDescOggColl(sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().getDettLuogoVO().getDenomLuogo());

			if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
				sinteticaLuoghiForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
				sinteticaLuoghiForm.setProspettazionePerLegami("SI");
			}
			mapping.getInputForward();
		}

		if (request.getParameter("SINTNEWLEGAME") != null) {
			sinteticaLuoghiForm.setProspettazionePerLegami("SI");
			sinteticaLuoghiForm.setTipologiaTastiera("ProspettazionePerLegami");
			sinteticaLuoghiForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			sinteticaLuoghiForm.setProspettaDatiOggColl("SI");
			sinteticaLuoghiForm.setIdOggColl(sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			sinteticaLuoghiForm.setDescOggColl(sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getDescPartenza());
//			(Inizio 5Agosto2009)
			if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
				sinteticaLuoghiForm.setTipologiaTastiera("ProspettazionePerFusioneOnLine");
			}
		}

		if (request.getAttribute("areaDatiPassReturnSintetica") == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			return null;
		}
		areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
			.getAttribute("areaDatiPassReturnSintetica");
		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaLuoghiForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaLuoghiForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaLuoghiForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaLuoghiForm.setNumLuoghi(areaDatiPassReturn.getNumNotizie());
		sinteticaLuoghiForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaLuoghiForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaLuoghiForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());
		sinteticaLuoghiForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());

		sinteticaLuoghiForm.setDatiInterrLuogo((AreaDatiPassaggioInterrogazioneLuogoVO) request
						.getAttribute("areaDatiPassPerInterrogazione"));

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaLuoghiForm.setAppoggio(appoggio);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaLuoghiForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaLuoghiForm);

		return null;
	}

	private ActionForward gestioneModalitaSif(HttpServletRequest request,
			SinteticaLuoghiForm sinteticaLuoghiForm, String internCall) throws RemoteException, NamingException, CreateException {

		if (!internCall.equals("SI")) {
			sinteticaLuoghiForm.getAreePassSifVo().setLivelloRicerca(
					((Integer) (request
							.getAttribute(TitoliCollegatiInvoke.livDiRicerca)))
							.intValue());
			sinteticaLuoghiForm.getAreePassSifVo().setOggettoDaRicercare(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			sinteticaLuoghiForm
					.getAreePassSifVo()
					.setDescOggettoDaRicercare(
							(String) request
									.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));

			sinteticaLuoghiForm
			.getAreePassSifVo()
			.setTipMatOggetto(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));

			sinteticaLuoghiForm
			.getAreePassSifVo()
			.setNaturaOggetto(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));

			sinteticaLuoghiForm.getAreePassSifVo().setOggettoRicerca(
					((Integer) (request
							.getAttribute(TitoliCollegatiInvoke.oggDiRicerca)))
							.intValue());
			sinteticaLuoghiForm.getAreePassSifVo().setOggettoChiamante(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.oggChiamante));
			sinteticaLuoghiForm.getAreePassSifVo().setCodBiblioteca(
				(String) request
							.getAttribute(TitoliCollegatiInvoke.codBiblio));
			if (((String) request
				.getAttribute(TitoliCollegatiInvoke.visualCall))
				.equals("SI")) {
				sinteticaLuoghiForm.getAreePassSifVo().setVisualCall(true);
			} else {
				sinteticaLuoghiForm.getAreePassSifVo()
						.setVisualCall(false);
			}
		}

		sinteticaLuoghiForm.setIdOggColl(sinteticaLuoghiForm.getAreePassSifVo().getOggettoDaRicercare());
		sinteticaLuoghiForm.setDescOggColl(sinteticaLuoghiForm.getAreePassSifVo().getDescOggettoDaRicercare());

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass = new AreaDatiPassaggioInterrogazioneAutoreVO();
		areaDatiPass
				.setOggChiamante(TitoliCollegatiInvoke.ANALITICA_DETTAGLIO);
		areaDatiPass.setTipoOggetto(sinteticaLuoghiForm.getAreePassSifVo()
				.getOggettoRicerca());

		areaDatiPass.setOggDiRicerca(sinteticaLuoghiForm
				.getAreePassSifVo().getOggettoDaRicercare());

		areaDatiPass.setTipMatTitBase(sinteticaLuoghiForm
				.getAreePassSifVo().getTipMatOggetto());
		areaDatiPass.setNaturaTitBase(sinteticaLuoghiForm
				.getAreePassSifVo().getNaturaOggetto());

		areaDatiPass.setInterrGener(null);

		areaDatiPass
				.setRicercaIndice(sinteticaLuoghiForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areaDatiPass
				.setRicercaPolo(sinteticaLuoghiForm.getAreePassSifVo()
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
			String temp = sinteticaLuoghiForm.getAreePassSifVo().getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do"))  {
				actionForward.setPath(sinteticaLuoghiForm.getAreePassSifVo().getOggettoChiamante() + ".do");
			} else  {
				actionForward.setPath(sinteticaLuoghiForm.getAreePassSifVo().getOggettoChiamante());
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")){
				actionForward.setPath(path.replace(".", "/"));
			}
			actionForward.setRedirect(true);
			return actionForward;
		}

		if (!sinteticaLuoghiForm.getAreePassSifVo().isVisualCall()) {
			request.setAttribute(TitoliCollegatiInvoke.arrayListSintetica,
					areaDatiPassReturn.getListaSintetica());
			ActionForward actionForward = new ActionForward();
			actionForward.setPath(sinteticaLuoghiForm.getAreePassSifVo().getOggettoChiamante() + ".do");
			String path = actionForward.getPath();
			if (!path.endsWith(".do")){
				actionForward.setPath(path.replace(".", "/"));
			}
			return actionForward;
		}

		// Impostazione della form con i valori tornati dal Server
		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaLuoghiForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaLuoghiForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaLuoghiForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaLuoghiForm.setNumLuoghi(areaDatiPassReturn.getNumNotizie());
		sinteticaLuoghiForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaLuoghiForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaLuoghiForm.setLivRicerca(areaDatiPassReturn.getLivelloTrovato());
		sinteticaLuoghiForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());
		return null;
	}



	public ActionForward carBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaLuoghiForm.getNumBlocco() == 0
				|| sinteticaLuoghiForm.getNumBlocco() > sinteticaLuoghiForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		HashSet<Integer> appoggio = sinteticaLuoghiForm.getAppoggio();
		int i = sinteticaLuoghiForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(sinteticaLuoghiForm.getNumBlocco());
		areaDatiPass.setMaxRighe(sinteticaLuoghiForm.getMaxRighe());
		areaDatiPass.setIdLista(sinteticaLuoghiForm.getIdLista());
		areaDatiPass.setTipoOrdinam(sinteticaLuoghiForm.getTipoOrd());
		areaDatiPass.setTipoOutput(sinteticaLuoghiForm.getTipoOutput());

		if (sinteticaLuoghiForm.getLivRicerca().equals("I")) {
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(true);
		} else {
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setRicercaIndice(false);
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory.getGestioneBibliografica().getNextBloccoLuoghi(areaDatiPass, Navigation.getInstance(request).getUserTicket());

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

		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaLuoghiForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaLuoghiForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco =sinteticaLuoghiForm.getNumBlocco();
		appoggio = sinteticaLuoghiForm.getAppoggio();
		appoggio.add(numBlocco);
		sinteticaLuoghiForm.setAppoggio(appoggio);

		//sinteticaLuoghiForm.setNumBlocco(++numBlocco);

        List<SinteticaLuoghiView> listaSinteticaOriginale = sinteticaLuoghiForm.getListaSintetica();
        listaSinteticaOriginale.addAll(areaDatiPassReturn.getListaSintetica());
        Collections.sort(listaSinteticaOriginale, SinteticaLuoghiView.sortListaSinteticaLuo);

        sinteticaLuoghiForm.setListaSintetica(listaSinteticaOriginale);

		// Si prospetta la mappa Sintetica autore con la lista dei autori trovati
		sinteticaLuoghiForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaLuoghiForm);
		return mapping.getInputForward();
	}

	public ActionForward analiticaLuo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO() == null) {
			request.setAttribute("presenzaTastoVaiA", "SI");
		} else {
			if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null
						|| sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals("")) {
				request.setAttribute("presenzaTastoVaiA", "SI");
			} else {
				request.setAttribute("presenzaTastoVaiA", "NO");
			}
		}

		Integer progressivo = sinteticaLuoghiForm.getLinkProgressivo();
		if (progressivo != null) {
			SinteticaLuoghiView eleSinteticaLuoghiView = null;
			for (int i = 0; i < sinteticaLuoghiForm.getListaSintetica().size(); i++) {
				eleSinteticaLuoghiView = (SinteticaLuoghiView) sinteticaLuoghiForm.getListaSintetica().get(i);
				if (eleSinteticaLuoghiView.getProgressivo() == progressivo) {
					sinteticaLuoghiForm.setSelezRadio(eleSinteticaLuoghiView.getLid());
					break;
				}
			}
		}

		// Mofifica almaviva2 BUG MANTIS 3383 - inserito controllo per selezione di un solo check si invia solo il bid
		// e non la tabella;
		// Inizio Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione
		// così da inviarla correttamente
		String[] listaLidSelezOld;
		int cont = -1;
		if (sinteticaLuoghiForm.getSelezCheck() != null	&& sinteticaLuoghiForm.getSelezCheck().length > 0) {
			int length = sinteticaLuoghiForm.getSelezCheck().length;
			listaLidSelezOld = new String[length];
			for (int i = 0; i < length; i++) {
				if (sinteticaLuoghiForm.getSelezCheck()[i] != null && sinteticaLuoghiForm.getSelezCheck()[i].length()> 0) {
					listaLidSelezOld[++cont] = sinteticaLuoghiForm.getSelezCheck()[i];
				}
			}

			if (cont <0) {
				if (sinteticaLuoghiForm.getSelezRadio() != null
						&& !sinteticaLuoghiForm.getSelezRadio().equals("")) {
					String vidNome = sinteticaLuoghiForm.getSelezRadio();
					request.setAttribute("bid", vidNome.substring(0, 10));
					request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaLuoghiForm.getDatiInterrLuogo());
					request.setAttribute("tipoAuthority", "LU");

					if (sinteticaLuoghiForm.getUtilizzoComeSif() != null && sinteticaLuoghiForm.getLivRicerca() != null) {
						if (sinteticaLuoghiForm.getUtilizzoComeSif().equals("SI") && sinteticaLuoghiForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			} else if (cont == 0) {
				request.setAttribute("bid", listaLidSelezOld[0]);
				request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaLuoghiForm.getDatiInterrLuogo());
				request.setAttribute("tipoAuthority", "LU");

				if (sinteticaLuoghiForm.getUtilizzoComeSif() != null && sinteticaLuoghiForm.getLivRicerca() != null) {
					if (sinteticaLuoghiForm.getUtilizzoComeSif().equals("SI") && sinteticaLuoghiForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));

			} else {
//				String[] listaBidSelez = sinteticaTitoliForm.getSelezCheck();
				String[] listaLidSelez = listaLidSelezOld;
				if (listaLidSelez[0] != null) {
					request.setAttribute("bid", listaLidSelez[0]);
					request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaLuoghiForm.getDatiInterrLuogo());
					request.setAttribute("listaBidSelez", listaLidSelez);
					request.setAttribute("tipoAuthority", "LU");
					resetToken(request);

					if (sinteticaLuoghiForm.getUtilizzoComeSif() != null && sinteticaLuoghiForm.getLivRicerca() != null) {
						if (sinteticaLuoghiForm.getUtilizzoComeSif().equals("SI") && sinteticaLuoghiForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			}
		} else {
			if (sinteticaLuoghiForm.getSelezRadio() != null
					&& !sinteticaLuoghiForm.getSelezRadio().equals("")) {
				String vidNome = sinteticaLuoghiForm.getSelezRadio();
				request.setAttribute("bid", vidNome.substring(0, 10));
				request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaLuoghiForm.getDatiInterrLuogo());
				request.setAttribute("tipoAuthority", "LU");

				if (sinteticaLuoghiForm.getUtilizzoComeSif() != null && sinteticaLuoghiForm.getLivRicerca() != null) {
					if (sinteticaLuoghiForm.getUtilizzoComeSif().equals("SI") && sinteticaLuoghiForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
			}
		}
		// Fine Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione

//		if (sinteticaLuoghiForm.getSelezCheck() != null
//				&& sinteticaLuoghiForm.getSelezCheck().length > 0) {
//			String[] listaLidSelez = sinteticaLuoghiForm.getSelezCheck();
//			if (listaLidSelez[0] != null) {
//				for (int i = 0; i < listaLidSelez.length; i++) {
//					request.setAttribute("bid", listaLidSelez[0]);
//					request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
//					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaLuoghiForm.getDatiInterrLuogo());
//					request.setAttribute("listaBidSelez", listaLidSelez);
//				}
//
//				if (sinteticaLuoghiForm.getUtilizzoComeSif() != null && sinteticaLuoghiForm.getLivRicerca() != null) {
//					if (sinteticaLuoghiForm.getUtilizzoComeSif().equals("SI") && sinteticaLuoghiForm.getLivRicerca().equals("P")) {
//						request.setAttribute("presenzaTastoCercaInIndice", "NO");
//					}
//				}
//				resetToken(request);
//				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
//			}
//		}
//
//
//
//		if (sinteticaLuoghiForm.getSelezRadio() != null) {
//			String vidNome = sinteticaLuoghiForm.getSelezRadio();
//			request.setAttribute("bid", vidNome.substring(0, 10));
//			request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
//			request.setAttribute("areaDatiPassPerInterrogazione", sinteticaLuoghiForm.getDatiInterrLuogo());
//			request.setAttribute("tipoAuthority", "LU");
//
//			if (sinteticaLuoghiForm.getUtilizzoComeSif() != null && sinteticaLuoghiForm.getLivRicerca() != null) {
//				if (sinteticaLuoghiForm.getUtilizzoComeSif().equals("SI") && sinteticaLuoghiForm.getLivRicerca().equals("P")) {
//					request.setAttribute("presenzaTastoCercaInIndice", "NO");
//				}
//			}
//			resetToken(request);
//			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
//		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
//		}
		return mapping.getInputForward();
	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati nell'Interrogazione
		sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().setRicLocale(false);
		sinteticaLuoghiForm.getDatiInterrLuogo().setRicercaPolo(false);
		sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().setRicIndice(true);
		sinteticaLuoghiForm.getDatiInterrLuogo().setRicercaIndice(true);

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaLuoghi(sinteticaLuoghiForm.getDatiInterrLuogo(), Navigation.getInstance(request).getUserTicket());

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

		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaLuoghiForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaLuoghiForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaLuoghiForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaLuoghiForm.setNumLuoghi(areaDatiPassReturn.getNumNotizie());
		sinteticaLuoghiForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaLuoghiForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaLuoghiForm.setLivRicerca(areaDatiPassReturn.getLivelloTrovato());

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaLuoghiForm.setAppoggio(appoggio);

		List<SinteticaLuoghiView> listaSinteticaOriginale = areaDatiPassReturn
				.getListaSintetica();
		sinteticaLuoghiForm.setListaSintetica(listaSinteticaOriginale);

		// almaviva2 Luglio 2018 riportata modifica del 01.12.2009 BUG MANTIS 3385  inserito puntamento fisso al primo elemento
		// fatta sulla sintetica titoli (altrimenti passando da analitica in locale ad analitica in Indice se l'elemento selezionato
		// sulla prima è assente dalla seconda nella lista non è selezionato alcun elemento)
		if (ValidazioneDati.isFilled(listaSinteticaOriginale)) {
			//selezione primo elemento
			SinteticaLuoghiView sinteticaLuoghiView = listaSinteticaOriginale.get(0);
			sinteticaLuoghiForm.setSelezRadio(sinteticaLuoghiView.getLid());
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli trovati
		sinteticaLuoghiForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaLuoghiForm);
		return mapping.getInputForward();

	}

	public ActionForward creaLuoLoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}


		if (sinteticaLuoghiForm.getProspettazionePerLegami().equals("NO")) {

			request.setAttribute("tipoProspettazione", "INS");
			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());

			resetToken(request);
			return mapping.findForward("creaLuogoLocale");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("LU");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setDescArrivo("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaLuoghiForm.getAreaDatiLegameTitoloVO());

			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());
			resetToken(request);

			return mapping.findForward("creaLuogoLocale");
		}
	}

	public ActionForward creaLuo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaLuoghiForm.getProspettazionePerLegami().equals("NO")) {

			request.setAttribute("tipoProspettazione", "INS");
			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());

			resetToken(request);
			return mapping.findForward("creaLuogo");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO()
					.setAuthorityOggettoArrivo("LU");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setDescArrivo("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO()
					.setTipoLegameNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO()
					.setSottoTipoLegameNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO()
					.setNoteLegameNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaLuoghiForm.getAreaDatiLegameTitoloVO()
					.setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaLuoghiForm
					.getAreaDatiLegameTitoloVO());

			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaLuoghiForm.getDatiInterrLuogo().getInterrGener().getDenominazione());
			resetToken(request);

			return mapping.findForward("creaLuogo");
		}
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}


		// Inizio Modifica almaviva2 11.01.2011 BUG MANTIS 4132 (esercizio) - Ricerca titolo collegati a luogo senza esito dava errore; intervento per
		// restituire il diagnostico 'la ricerca effettuata non ha prodotto risultati' (SinteticaLuoghiAction - esamina)
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || request.getParameter("SIFSINTETICA") != null) {
			if (sinteticaLuoghiForm.getAreePassSifVo().getOggettoDaRicercare() != null) {
				if (!sinteticaLuoghiForm.getAreePassSifVo().getOggettoDaRicercare().equals("")) {
					if (!sinteticaLuoghiForm.getAreePassSifVo().getOggettoDaRicercare().equals("0")) {
						sinteticaLuoghiForm.setIdOggColl(sinteticaLuoghiForm.getAreePassSifVo().getOggettoDaRicercare());
						sinteticaLuoghiForm.setDescOggColl(sinteticaLuoghiForm.getAreePassSifVo().getDescOggettoDaRicercare());
					}
				}
			}
			return mapping.getInputForward();
		}
		// Fine Modifica almaviva2 11.01.2011 BUG MANTIS 4132 (esercizio)


		if (sinteticaLuoghiForm.getSelezRadio() != null) {
			if (sinteticaLuoghiForm.getEsaminaLuoSelez().length() > 0) {

				SinteticaLuoghiView eleSinteticaLuoghiView = null;
				String keyDesc = sinteticaLuoghiForm.getSelezRadio();
				String lid = keyDesc.substring(0, 10);
				String desDaRic = "";
				// Modifica almaviva2 BUG MANTIS 4137 (Collaudo)- l'esamine su forme di rinvio deve essere fatto con la sua forma accettata
				String lidDaRic = "";


				for (int i = 0; i < sinteticaLuoghiForm.getListaSintetica().size(); i++) {
					eleSinteticaLuoghiView = (SinteticaLuoghiView) sinteticaLuoghiForm.getListaSintetica().get(i);
					if (eleSinteticaLuoghiView.getLid().equals(lid)) {
						// Inizio Modifica almaviva2 BUG MANTIS 4137 (Collaudo)
						//l'esamina su forme di rinvio deve essere fatto con la sua forma accettata
//						desDaRic = eleSinteticaLuoghiView.getDenominazione();
						desDaRic = eleSinteticaLuoghiView.isRinvio() ? eleSinteticaLuoghiView.getLuogoAccettata() : eleSinteticaLuoghiView.getDenominazione();
						desDaRic = desDaRic.replace("&lt;", "<");
						desDaRic = desDaRic.replace("&gt;", ">");
						lidDaRic = eleSinteticaLuoghiView.getLidAccettata();
						break;
					}
				}

				String myForwardName = sinteticaLuoghiForm.getTabellaEsaminaVO().getMyForwardName(
						sinteticaLuoghiForm.getMyPath(), sinteticaLuoghiForm
								.getEsaminaLuoSelez());
				String myForwardPath = sinteticaLuoghiForm.getTabellaEsaminaVO().getMyForwardPath(
						sinteticaLuoghiForm.getMyPath(), sinteticaLuoghiForm
								.getEsaminaLuoSelez());

				if (myForwardPath.equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				String myModeCall = sinteticaLuoghiForm.getTabellaEsaminaVO().getMyModeCall(sinteticaLuoghiForm
						.getMyPath(), sinteticaLuoghiForm.getEsaminaLuoSelez());
				String myAction = sinteticaLuoghiForm.getTabellaEsaminaVO().getMyAction(sinteticaLuoghiForm
						.getMyPath(), sinteticaLuoghiForm.getEsaminaLuoSelez());

				if (sinteticaLuoghiForm.getLivRicerca().equals("B")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					sinteticaLuoghiForm.getAreePassSifVo()
						.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					request.setAttribute(TitoliCollegatiInvoke.codBiblio,
									"011");
				}
				if (sinteticaLuoghiForm.getLivRicerca().equals("P")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					sinteticaLuoghiForm.getAreePassSifVo()
					.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
				}
				if (sinteticaLuoghiForm.getLivRicerca().equals("I")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
					sinteticaLuoghiForm.getAreePassSifVo()
					.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
				}

				Class<TitoliCollegatiInvoke> genericClass = TitoliCollegatiInvoke.class;
				int appoggio = 0;
				try {
					Field genericField = genericClass.getField(myForwardName);
					appoggio = genericField.getInt(null);
				} catch (NoSuchFieldException e) {
				}

				sinteticaLuoghiForm.getAreePassSifVo().setOggettoRicerca(appoggio);

				// Modifica almaviva2 BUG MANTIS 4137 (Collaudo)
				//l'esamina su forme di rinvio deve essere fatto con la sua forma accettata
//				sinteticaLuoghiForm.getAreePassSifVo().setOggettoDaRicercare(lid);
				sinteticaLuoghiForm.getAreePassSifVo().setOggettoDaRicercare(lidDaRic);

				sinteticaLuoghiForm.getAreePassSifVo().setDescOggettoDaRicercare(desDaRic);
				sinteticaLuoghiForm.getAreePassSifVo().setOggettoChiamante(mapping.getPath());
				sinteticaLuoghiForm.getAreePassSifVo().setVisualCall(true);

				if (myAction.replace(".", "/").equals(
						myForwardPath.substring(0, myForwardPath.length() - 3))) {
					ActionForward forward = this.load(mapping, form, request, "SI");
					if (forward == null) {
						mapping.getInputForward();
					}
				} else  {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							sinteticaLuoghiForm.getAreePassSifVo()
									.getLivelloRicerca());
					request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
							sinteticaLuoghiForm.getAreePassSifVo()
									.getOggettoDaRicercare());
					request.setAttribute(
							TitoliCollegatiInvoke.xidDiRicercaDesc,
							sinteticaLuoghiForm.getAreePassSifVo()
									.getDescOggettoDaRicercare());
					request.setAttribute(TitoliCollegatiInvoke.codBiblio,
							sinteticaLuoghiForm.getAreePassSifVo()
									.getCodBiblioteca());
					if (sinteticaLuoghiForm.getAreePassSifVo()
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
					return Navigation.getInstance(request).goForward(actionForward);
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
			SinteticaLuoghiForm sinteticaLuoghiForm) {


		sinteticaLuoghiForm.setTabellaEsaminaVO((TabellaEsaminaVO) this.getServlet().getServletContext()
				.getAttribute("serverTypes"));
		List<MyLabelValueBean> listaCaricamento = sinteticaLuoghiForm.getTabellaEsaminaVO().getLista(sinteticaLuoghiForm.getMyPath());

		MyLabelValueBean eleListaCar;
		List<ComboSoloDescVO> lista = new ArrayList<ComboSoloDescVO>();

		ComboSoloDescVO listaEsamina = new ComboSoloDescVO();
		listaEsamina.setDescrizione("");
		lista.add(listaEsamina);

		int i;
		for (i = 0; i < listaCaricamento.size(); i++) {
			eleListaCar = listaCaricamento.get(i);
			listaEsamina = new ComboSoloDescVO();
			if (eleListaCar.getMyLivelloBaseDati().equals("A")
					|| eleListaCar.getMyLivelloBaseDati().equals(sinteticaLuoghiForm.getLivRicerca())) {
				listaEsamina.setDescrizione(eleListaCar.getMyLabel());
				lista.add(listaEsamina);
			}
		}
		sinteticaLuoghiForm.setListaEsaminaLuo(lista);

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

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		// Imposto il flag di conferma a TRUE affinchè sia effettuata la
		// variazione senza ricerca simili
		sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().setConferma(true);

		sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().setInserimentoIndice(true);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPassReturn = factory.getGestioneBibliografica().inserisciLuogo(
				sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione(), Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
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
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
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

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward fondiOggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;


		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().FONDE_AUTORE_1270, "LU");
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}





		// ===================================================================================================
		// MODIFICA ALLA PROCEDURA DI FUSIONE IN FASE DI VARIAZIONE
		// PRIMA SI EFFETTUA L'AGGIORNAMENTO POI LA FUSIONE ALTRIMENTI RISONDE CHE LA CARTA D'IDENTITA'
		// E' DIVERSA E NON PERMETTE LA FUSIONE STESSA

		AreaDatiVariazioneReturnVO areaDatiPassVariazioneReturn = null;

		// Imposto il flag di conferma a TRUE affinchè sia effettuata la
		// variazione senza ricerca simili
		sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().setConferma(true);

		sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().setInserimentoIndice(true);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPassVariazioneReturn = factory.getGestioneBibliografica().inserisciLuogo(
				sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione(), Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassVariazioneReturn == null) {
			request.setAttribute("bid", null);
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}
		if (areaDatiPassVariazioneReturn.getCodErr().equals("9999") || areaDatiPassVariazioneReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassVariazioneReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassVariazioneReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassVariazioneReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		// ===================================================================================================

		if (sinteticaLuoghiForm.getSelezRadio() != null) {
			String vidAccorpante = sinteticaLuoghiForm.getSelezRadio().substring(0,10);
			String vidEliminato = sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().getDettLuogoVO().getLid();
			String tipoMatEliminato = "";
			String tipoMatAccorpante = "";

			String tipoAutEliminato = (SbnAuthority.LU).toString();
			String tipoAutAccorpante = (SbnAuthority.LU).toString();

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

				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.richiestaAccorpamento(areaDatiPass, Navigation.getInstance(request).getUserTicket());
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


	//	(Inizio 5Agosto2009)
	public ActionForward catturaEFondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (sinteticaLuoghiForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String lidDaCatturare = sinteticaLuoghiForm.getSelezRadio().substring(0,10);
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaLuoghiView eleSinteticaLuoghiView = null;
		int size = sinteticaLuoghiForm.getListaSintetica().size();
		for (int i = 0; i < size; i++) {
			eleSinteticaLuoghiView = (SinteticaLuoghiView) sinteticaLuoghiForm
					.getListaSintetica().get(i);
			if (eleSinteticaLuoghiView.getLid().equals(lidDaCatturare)) {
				tipoAutDaCatturare = eleSinteticaLuoghiView.getTipoAutority();
				break;
			}
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
		areaDatiPassCattura.setIdPadre(lidDaCatturare);
		areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
		areaDatiPassCattura.setInferioriDaCatturare(appo);

		UserVO utenteCollegato = (UserVO) request.getSession()
		.getAttribute(Constants.UTENTE_KEY);

		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaLuogo(areaDatiPassCattura,
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

		if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			bidAccorpante = sinteticaLuoghiForm.getSelezRadio().substring(0,10);
			bidEliminato = sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getBidPartenza();
			tipoAutAccorpante = eleSinteticaLuoghiView.getTipoAutority();
			tipoAutEliminato = eleSinteticaLuoghiView.getTipoAutority();
		} else {
			bidAccorpante = sinteticaLuoghiForm.getSelezRadio().substring(0,10);
			bidEliminato = sinteticaLuoghiForm.getAreaDatiPassPerConfVariazione().getDettLuogoVO().getLid();
			tipoAutAccorpante = eleSinteticaLuoghiView.getTipoAutority();
			tipoAutEliminato = eleSinteticaLuoghiView.getTipoAutority();

		}


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
				request.setAttribute("confermaInvioIndice", "CONFERMATO");
				request.setAttribute("livRicerca", "P");
				Navigation navi = Navigation.getInstance(request);
				if (navi.isFromBar())
					return mapping.getInputForward();
				return navi.goBack(true);

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

	public ActionForward tornaAnalitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;
		if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			request.setAttribute("bid", sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		} else {
			request.setAttribute("bid", sinteticaLuoghiForm.getSelezRadio());
		}

		request.setAttribute("livRicerca", sinteticaLuoghiForm.getLivRicerca());
		resetToken(request);
		return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
	}
//	(Fine 5Agosto2009)


	public ActionForward prospettaPerLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		if (sinteticaLuoghiForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setIdArrivo(sinteticaLuoghiForm.getSelezRadio().substring(0,10));
		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("LU");

		SinteticaLuoghiView eleSinteticaLuoghiView = null;
		String lidDaLegare = sinteticaLuoghiForm.getSelezRadio().substring(0,10);
		for (int i = 0; i < sinteticaLuoghiForm.getListaSintetica().size(); i++) {
			eleSinteticaLuoghiView = (SinteticaLuoghiView) sinteticaLuoghiForm.getListaSintetica().get(i);
			if (eleSinteticaLuoghiView.getLid().equals(lidDaLegare)) {

				// Inizio Modifica almaviva2 BUG MANTIS 4137 (Collaudo)- il legane su forme di rinvio deve essere fatto con la sua forma accettata
				// inoltre si inserisce il controllo per cui  un oggetto condiviso può legarsi solo a elementi condivisi;
//				sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setDescArrivo(eleSinteticaLuoghiView.getDenominazione());
				if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
					if (!eleSinteticaLuoghiView.isFlagCondiviso()) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.elemCondivisoConElemLocale"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}

				if (eleSinteticaLuoghiView.isAccettata() ) {
					sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setIdArrivo(eleSinteticaLuoghiView.getLid());
					String appoNome = eleSinteticaLuoghiView.getDenominazione();
					appoNome = appoNome.replace("&lt;", "<");
					appoNome = appoNome.replace("&gt;", ">");
					sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setDescArrivo(appoNome);
					sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo(eleSinteticaLuoghiView.getTipoLuogoAccettata());
				} else {
					sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setIdArrivo(eleSinteticaLuoghiView.getLidAccettata());
					String appoNome = eleSinteticaLuoghiView.getLuogoAccettata();
					appoNome = appoNome.replace("&lt;", "<");
					appoNome = appoNome.replace("&gt;", ">");
					sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setDescArrivo(appoNome);
					sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo(eleSinteticaLuoghiView.getTipoLuogoAccettata());
				}
				// Fine Modifica almaviva2 BUG MANTIS 4137 (Collaudo)-

				sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(eleSinteticaLuoghiView.isFlagCondiviso());
				break;
			}
		}

		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSiciNew("");
		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
		sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

		request.setAttribute("AreaDatiLegameTitoloVO", sinteticaLuoghiForm.getAreaDatiLegameTitoloVO());

		if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null
				|| sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("TU")
				|| sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("UM")) {
			return mapping.findForward("gestioneLegameTitoloLuogo");
		}
		if (sinteticaLuoghiForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("LU")) {
			return mapping.findForward("gestioneLegameFraAutority");
		}
		return mapping.getInputForward();
	}

	public ActionForward selezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception{

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;

		SinteticaLuoghiView eleSinteticaLuoghiView = null;
		int numElem = sinteticaLuoghiForm.getListaSintetica().size();
		String[] listaBidSelez = new String[numElem];
		for (int i = 0; i < numElem; i++) {
			eleSinteticaLuoghiView = (SinteticaLuoghiView) sinteticaLuoghiForm.getListaSintetica().get(i);
			listaBidSelez[i] =  eleSinteticaLuoghiView.getLid();
		}
		sinteticaLuoghiForm.setSelezCheck(listaBidSelez);
		return mapping.getInputForward();
	}

	public ActionForward deSelezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SinteticaLuoghiForm sinteticaLuoghiForm = (SinteticaLuoghiForm) form;
		sinteticaLuoghiForm.setSelezCheck(null);
		return mapping.getInputForward();
	}


}
