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

package it.iccu.sbn.web.actions.gestionebibliografica.marca;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.SinteticaImmaginiMarcheView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.SinteticaMarcheView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.marca.SinteticaMarcheForm;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.MyLabelValueBean;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.MarcaImageCache;
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
import java.util.Set;

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

public class SinteticaMarcheAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(SinteticaMarcheAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "carBlocco");
		map.put("button.analiticaMar", "analiticaMar");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.creaMar", "creaMar");
		// map.put("button.creaMarLoc", "creaMarLoc");
		map.put("button.esamina", "esamina");
		map.put("button.immaginiMarche", "visualizzaImmagini");

		map.put("button.selAllMarche", "selezionaTutti");
		map.put("button.deSelAllMarche", "deSelezionaTutti");
		map.put("button.selAllMarcheImmagini", "selezionaTuttiImg");

		// tasti per la prospettazione dei titoli per creazione nuovo legame
		map.put("button.gestLegami.lega", "prospettaPerLegame");

		map.put("button.gestSimiliCondividi.catturaEFondi", "catturaEFondi");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		log.debug("SinteticaMarcheAction");
		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		/** INIZIO VERIFICA ABILITAZIONE */
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,"MA");
			utenteEjb.isAbilitatoAuthority("MA");
			sinteticaMarcheForm.setCreaMar("SI");
			sinteticaMarcheForm.setCreaMarLoc("SI");
		}catch(UtenteNotAuthorizedException ute)
		{
			sinteticaMarcheForm.setCreaMar("NO");
			sinteticaMarcheForm.setCreaMarLoc("NO");
		}
		/** FINE VERIFICA ABILITAZIONE */


		ActionForward forward = this.load(mapping, form, request, "NO");

//		if (sinteticaMarcheForm.getListaSintetica() != null) {
//			if (sinteticaMarcheForm.getListaSintetica().size() == 1) {
//				SinteticaMarcheView sinteticaMarcheView = new SinteticaMarcheView();
//				sinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm.getListaSintetica().get(0);
//				sinteticaMarcheForm.setSelezRadio(sinteticaMarcheView.getKeyMidNome());
//			}
//		}

		List<SinteticaMarcheView> listaSintetica = sinteticaMarcheForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			SinteticaMarcheView sinteticaMarcheView = listaSintetica.get(0);
			sinteticaMarcheForm.setSelezRadio(sinteticaMarcheView.getKeyMidNome());
		}

		if (forward == null) {
			sinteticaMarcheForm.setMyPath(mapping.getPath()
					.replaceAll("/", "."));
			aggiornaForm(request, sinteticaMarcheForm);
			return mapping.getInputForward();
		}

		String path = forward.getPath();
		if (!path.endsWith(".do"))
			forward.setPath(path.replace(".", "/"));
		return forward;
	}

	private ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, String internCall) throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		sinteticaMarcheForm.setTipologiaTastiera("");
		sinteticaMarcheForm.setUtilizzoComeSif("NO");
		sinteticaMarcheForm.setProspettazioneSimili("NO");
		sinteticaMarcheForm.setProspettazionePerLegami("NO");
		sinteticaMarcheForm.setProspettaDatiOggColl("NO");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;

		if (request.getParameter("SIFSINTETICA") != null
				|| internCall.equals("SI")) {
			sinteticaMarcheForm.setProspettaDatiOggColl("SI");
			sinteticaMarcheForm.setUtilizzoComeSif("SI");
			sinteticaMarcheForm.setTipologiaTastiera("UtilizzoComeSif");

			ActionForward forward = this.gestioneModalitaSif(request,
					sinteticaMarcheForm, internCall);
			if (forward == null) {
				return null;
			} else {
				String path = forward.getPath();
				if (!path.endsWith(".do"))
					forward.setPath(path.replace(".", "/"));
				return forward;
			}
		}

		// // Nella Interfaccia diretta non viene gestito l'invio dei simili
		// quindi non sembrerebbe
		// // necesario farlo da Noi. CHIEDERE A ROSSANA !!
		// if (request.getParameter("SINTSIMILI") != null) {
		// sinteticaMarcheForm.setProspettaDatiOggColl("SI");
		// sinteticaMarcheForm.setProspettazioneSimili("SI");
		// sinteticaMarcheForm
		// .setAreaDatiPassPerConfVariazione((AreaDatiVariazioneMarcaVO) request
		// .getAttribute("areaDatiPassPerConfVariazione"));
		// mapping.getInputForward();
		// }

		if (request.getParameter("SINTNEWLEGAME") != null) {
			sinteticaMarcheForm.setProspettazionePerLegami("SI");
			sinteticaMarcheForm
					.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request
							.getAttribute("AreaDatiLegameTitoloVO"));
			sinteticaMarcheForm.setProspettaDatiOggColl("SI");
			sinteticaMarcheForm.setIdOggColl(sinteticaMarcheForm
					.getAreaDatiLegameTitoloVO().getBidPartenza());
			sinteticaMarcheForm.setDescOggColl(sinteticaMarcheForm
					.getAreaDatiLegameTitoloVO().getDescPartenza());
//			(Inizio 5Agosto2009)
			if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
				sinteticaMarcheForm.setTipologiaTastiera("ProspettazionePerFusioneOnLine");
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
		sinteticaMarcheForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaMarcheForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaMarcheForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaMarcheForm.setNumMarche(areaDatiPassReturn.getNumNotizie());
		sinteticaMarcheForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaMarcheForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaMarcheForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());
		sinteticaMarcheForm.setListaSintetica(areaDatiPassReturn
				.getListaSintetica());

		sinteticaMarcheForm
				.setDatiInterrMarca((AreaDatiPassaggioInterrogazioneMarcaVO) request
						.getAttribute("areaDatiPassPerInterrogazione"));

		Set<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaMarcheForm.setAppoggio(appoggio);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaMarcheForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaMarcheForm);

		return null;
	}

	private ActionForward gestioneModalitaSif(HttpServletRequest request,
			SinteticaMarcheForm sinteticaMarcheForm, String internCall)
			throws RemoteException, NamingException, CreateException {

		if (!internCall.equals("SI")) {
			sinteticaMarcheForm.getAreePassSifVo().setLivelloRicerca(
					((Integer) (request
							.getAttribute(TitoliCollegatiInvoke.livDiRicerca)))
							.intValue());
			sinteticaMarcheForm.getAreePassSifVo().setOggettoDaRicercare(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			sinteticaMarcheForm
					.getAreePassSifVo()
					.setDescOggettoDaRicercare(
							(String) request
									.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));

			sinteticaMarcheForm
					.getAreePassSifVo()
					.setTipMatOggetto(
							(String) request
									.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));

			sinteticaMarcheForm
					.getAreePassSifVo()
					.setNaturaOggetto(
							(String) request
									.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));

			sinteticaMarcheForm.getAreePassSifVo().setOggettoRicerca(
					((Integer) (request
							.getAttribute(TitoliCollegatiInvoke.oggDiRicerca)))
							.intValue());
			sinteticaMarcheForm.getAreePassSifVo().setOggettoChiamante(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.oggChiamante));
			sinteticaMarcheForm.getAreePassSifVo().setCodBiblioteca(
					(String) request
							.getAttribute(TitoliCollegatiInvoke.codBiblio));
			if (((String) request
					.getAttribute(TitoliCollegatiInvoke.visualCall))
					.equals("SI")) {
				sinteticaMarcheForm.getAreePassSifVo().setVisualCall(true);
			} else {
				sinteticaMarcheForm.getAreePassSifVo().setVisualCall(false);
			}
		}

		sinteticaMarcheForm.setIdOggColl(sinteticaMarcheForm.getAreePassSifVo()
				.getOggettoDaRicercare());
		sinteticaMarcheForm.setDescOggColl(sinteticaMarcheForm
				.getAreePassSifVo().getDescOggettoDaRicercare());

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneMarcaVO();
		areaDatiPass.setOggChiamante(TitoliCollegatiInvoke.ANALITICA_DETTAGLIO);
		areaDatiPass.setTipoOggetto(sinteticaMarcheForm.getAreePassSifVo()
				.getOggettoRicerca());

		areaDatiPass.setOggDiRicerca(sinteticaMarcheForm.getAreePassSifVo()
				.getOggettoDaRicercare());

		areaDatiPass.setTipMatTitBase(sinteticaMarcheForm.getAreePassSifVo()
				.getTipMatOggetto());
		areaDatiPass.setNaturaTitBase(sinteticaMarcheForm.getAreePassSifVo()
				.getNaturaOggetto());

		areaDatiPass.setInterrGener(null);

		areaDatiPass
				.setRicercaIndice(sinteticaMarcheForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areaDatiPass
				.setRicercaPolo(sinteticaMarcheForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().ricercaMarcheCollegate(
						areaDatiPass, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.nonTrovato"));
			this.saveErrors(request, errors);
			ActionForward actionForward = new ActionForward();
			String temp = sinteticaMarcheForm.getAreePassSifVo()
					.getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do")) {
				actionForward.setPath(sinteticaMarcheForm.getAreePassSifVo()
						.getOggettoChiamante()
						+ ".do");
			} else {
				actionForward.setPath(sinteticaMarcheForm.getAreePassSifVo()
						.getOggettoChiamante());
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}
			actionForward.setRedirect(true);
			return actionForward;
		}

		if (!sinteticaMarcheForm.getAreePassSifVo().isVisualCall()) {
			request.setAttribute(TitoliCollegatiInvoke.arrayListSintetica,
					areaDatiPassReturn.getListaSintetica());
			ActionForward actionForward = new ActionForward();
			actionForward.setPath(sinteticaMarcheForm.getAreePassSifVo()
					.getOggettoChiamante()
					+ ".do");
			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}
			return actionForward;
		}

		// Impostazione della form con i valori tornati dal Server
		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaMarcheForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaMarcheForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaMarcheForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaMarcheForm.setNumMarche(areaDatiPassReturn.getNumNotizie());
		sinteticaMarcheForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaMarcheForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaMarcheForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());
		sinteticaMarcheForm.setListaSintetica(areaDatiPassReturn
				.getListaSintetica());
		return null;

	}

	public ActionForward carBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaMarcheForm.getNumBlocco() == 0
				|| sinteticaMarcheForm.getNumBlocco() > sinteticaMarcheForm
						.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		Set<Integer> appoggio = sinteticaMarcheForm.getAppoggio();
		int i = sinteticaMarcheForm.getNumBlocco();

		if (appoggio != null)
			if (appoggio.contains(i))
				return mapping.getInputForward();


		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(sinteticaMarcheForm.getNumBlocco());
		areaDatiPass.setMaxRighe(sinteticaMarcheForm.getMaxRighe());
		areaDatiPass.setIdLista(sinteticaMarcheForm.getIdLista());
		areaDatiPass.setTipoOrdinam(sinteticaMarcheForm.getTipoOrd());
		areaDatiPass.setTipoOutput(sinteticaMarcheForm.getTipoOutput());

		if (sinteticaMarcheForm.getLivRicerca().equals("I")) {
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(true);
		} else {
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setRicercaIndice(false);
		}
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().getNextBloccoMarche(areaDatiPass,
						Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
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
		sinteticaMarcheForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaMarcheForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = sinteticaMarcheForm.getNumBlocco();
		appoggio = sinteticaMarcheForm.getAppoggio();
		appoggio.add(numBlocco);
		sinteticaMarcheForm.setAppoggio(appoggio);

		// sinteticaMarcheForm.setNumBlocco(++numBlocco);

		List<SinteticaMarcheView> listaSinteticaOriginale = sinteticaMarcheForm.getListaSintetica();
		listaSinteticaOriginale.addAll(areaDatiPassReturn.getListaSintetica());
		Collections.sort(listaSinteticaOriginale,
				SinteticaMarcheView.sortListaSinteticaMar);

		sinteticaMarcheForm.setListaSintetica(listaSinteticaOriginale);

		sinteticaMarcheForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaMarcheForm);
		return mapping.getInputForward();
	}

	public ActionForward analiticaMar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO() == null) {
			request.setAttribute("presenzaTastoVaiA", "SI");
		} else {
			if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null
						|| sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals("")) {
				request.setAttribute("presenzaTastoVaiA", "SI");
			} else {
				request.setAttribute("presenzaTastoVaiA", "NO");
			}
		}

		Integer progressivo = sinteticaMarcheForm.getLinkProgressivo();
		if (progressivo != null) {
			SinteticaMarcheView eleSinteticaMarcheView = null;
			for (int i = 0; i < sinteticaMarcheForm.getListaSintetica().size(); i++) {
				eleSinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm
						.getListaSintetica().get(i);
				if (eleSinteticaMarcheView.getProgressivo() == progressivo) {
					sinteticaMarcheForm.setSelezRadio(eleSinteticaMarcheView
							.getMid());
					break;
				}
			}
		}

		// Mofifica almaviva2 BUG MANTIS 3383 - inserito controllo per selezione di un solo check si invia solo il bid
		// e non la tabella;
		// Inizio Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione
		// così da inviarla correttamente
		String[] listaMidSelezOld;
		int cont = -1;
		if (sinteticaMarcheForm.getSelezCheck() != null	&& sinteticaMarcheForm.getSelezCheck().length > 0) {
			int length = sinteticaMarcheForm.getSelezCheck().length;
			listaMidSelezOld = new String[length];
			for (int i = 0; i < length; i++) {
				if (sinteticaMarcheForm.getSelezCheck()[i] != null && sinteticaMarcheForm.getSelezCheck()[i].length()> 0) {
					listaMidSelezOld[++cont] = sinteticaMarcheForm.getSelezCheck()[i];
				}
			}

			if (cont <0) {
				if (sinteticaMarcheForm.getSelezRadio() != null
						&& !sinteticaMarcheForm.getSelezRadio().equals("")) {
					String midNome = sinteticaMarcheForm.getSelezRadio();
					request.setAttribute("bid", midNome.substring(0, 10));
					request.setAttribute("livRicerca", sinteticaMarcheForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaMarcheForm.getDatiInterrMarca());
					request.setAttribute("tipoAuthority", "MA");

					if (sinteticaMarcheForm.getUtilizzoComeSif() != null && sinteticaMarcheForm.getLivRicerca() != null) {
						if (sinteticaMarcheForm.getUtilizzoComeSif().equals("SI") && sinteticaMarcheForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			} else if (cont == 0) {
				request.setAttribute("bid", listaMidSelezOld[0]);
				request.setAttribute("livRicerca", sinteticaMarcheForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaMarcheForm.getDatiInterrMarca());
				request.setAttribute("tipoAuthority", "MA");

				if (sinteticaMarcheForm.getUtilizzoComeSif() != null && sinteticaMarcheForm.getLivRicerca() != null) {
					if (sinteticaMarcheForm.getUtilizzoComeSif().equals("SI") && sinteticaMarcheForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
			} else {
//				String[] listaBidSelez = sinteticaTitoliForm.getSelezCheck();
				String[] listaMidSelez = listaMidSelezOld;
				if (listaMidSelez[0] != null) {
					request.setAttribute("bid", listaMidSelez[0]);
					request.setAttribute("livRicerca", sinteticaMarcheForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaMarcheForm.getDatiInterrMarca());
					request.setAttribute("tipoAuthority", "MA");
					request.setAttribute("listaBidSelez", listaMidSelez);
					resetToken(request);

					if (sinteticaMarcheForm.getUtilizzoComeSif() != null && sinteticaMarcheForm.getLivRicerca() != null) {
						if (sinteticaMarcheForm.getUtilizzoComeSif().equals("SI") && sinteticaMarcheForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			}
		} else {
			if (sinteticaMarcheForm.getSelezRadio() != null
					&& !sinteticaMarcheForm.getSelezRadio().equals("")) {
				String midNome = sinteticaMarcheForm.getSelezRadio();
				request.setAttribute("bid", midNome.substring(0, 10));
				request.setAttribute("livRicerca", sinteticaMarcheForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaMarcheForm.getDatiInterrMarca());
				request.setAttribute("tipoAuthority", "MA");

				if (sinteticaMarcheForm.getUtilizzoComeSif() != null && sinteticaMarcheForm.getLivRicerca() != null) {
					if (sinteticaMarcheForm.getUtilizzoComeSif().equals("SI") && sinteticaMarcheForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
			}
		}
		// Fine Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione

//		if (sinteticaMarcheForm.getSelezCheck() != null
//				&& sinteticaMarcheForm.getSelezCheck().length > 0) {
//			String[] listaMidSelez = sinteticaMarcheForm.getSelezCheck();
//			if (listaMidSelez[0] != null) {
//				for (int i = 0; i < listaMidSelez.length; i++) {
//					request.setAttribute("bid", listaMidSelez[0]);
//					request.setAttribute("livRicerca", sinteticaMarcheForm
//							.getLivRicerca());
//					request.setAttribute("areaDatiPassPerInterrogazione",
//							sinteticaMarcheForm.getDatiInterrMarca());
//					request.setAttribute("listaBidSelez", listaMidSelez);
//				}
//
//				if (sinteticaMarcheForm.getUtilizzoComeSif() != null && sinteticaMarcheForm.getLivRicerca() != null) {
//					if (sinteticaMarcheForm.getUtilizzoComeSif().equals("SI") && sinteticaMarcheForm.getLivRicerca().equals("P")) {
//						request.setAttribute("presenzaTastoCercaInIndice", "NO");
//					}
//				}
//				resetToken(request);
//				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
//			}
//		}
//
//		if (sinteticaMarcheForm.getSelezRadio() != null) {
//			String midNome = sinteticaMarcheForm.getSelezRadio();
//			request.setAttribute("bid", midNome.substring(0, 10));
//			request.setAttribute("livRicerca", sinteticaMarcheForm
//					.getLivRicerca());
//			request.setAttribute("areaDatiPassPerInterrogazione",
//					sinteticaMarcheForm.getDatiInterrMarca());
//			request.setAttribute("tipoAuthority", "MA");
//
//			if (sinteticaMarcheForm.getUtilizzoComeSif() != null && sinteticaMarcheForm.getLivRicerca() != null) {
//				if (sinteticaMarcheForm.getUtilizzoComeSif().equals("SI") && sinteticaMarcheForm.getLivRicerca().equals("P")) {
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

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati
		// nell'Interrogazione
		sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().setRicLocale(false);
		sinteticaMarcheForm.getDatiInterrMarca().setRicercaPolo(false);
		sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().setRicIndice(true);
		sinteticaMarcheForm.getDatiInterrMarca().setRicercaIndice(true);

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaMarche(
				sinteticaMarcheForm.getDatiInterrMarca(),
				Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
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
		sinteticaMarcheForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaMarcheForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaMarcheForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaMarcheForm.setNumMarche(areaDatiPassReturn.getNumNotizie());
		sinteticaMarcheForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaMarcheForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaMarcheForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaMarcheForm.setAppoggio(appoggio);

		List<?> listaSinteticaOriginale = areaDatiPassReturn
				.getListaSintetica();
		sinteticaMarcheForm.setListaSintetica(listaSinteticaOriginale);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaMarcheForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaMarcheForm);
		return mapping.getInputForward();

	}

	public ActionForward creaMar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaMarcheForm.getProspettazionePerLegami().equals("NO")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioMarcaGeneraleVO dettMarVO = new DettaglioMarcaGeneraleVO();
			dettMarVO.setDesc(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getDescrizione());

			// Inizio Modifica almaviva2 per preimpostare i valori di fonte/repertorio per la creazione 12.04.2010 MANTIS 3684
			dettMarVO.setMotto(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getMotto());
			dettMarVO.setParChiave1(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getParolaChiave1());
			dettMarVO.setParChiave2(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getParolaChiave2());
			dettMarVO.setParChiave3(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getParolaChiave3());
			dettMarVO.setCampoCodiceRep1Old(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getCitazioneStandardSelez());
			dettMarVO.setCampoProgressivoRep1Old(sinteticaMarcheForm.getDatiInterrMarca().getInterrGener().getSiglaRepertorio());
			// Fine Modifica almaviva2 per preimpostare i valori di fonte/repertorio per la creazione 12.04.2010 MANTIS 3684

			request.setAttribute("dettaglioMar", dettMarVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaMarcheForm
					.getDatiInterrMarca().getInterrGener().getDescrizione());
			resetToken(request);
			return mapping.findForward("creaMarca");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
					.setAuthorityOggettoArrivo("MA");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setDescArrivo("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
					.setTipoLegameNew("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
					.setSottoTipoLegameNew("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
					.setNoteLegameNew("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
					.setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaMarcheForm
					.getAreaDatiLegameTitoloVO());

			DettaglioMarcaGeneraleVO dettMarVO = new DettaglioMarcaGeneraleVO();
			dettMarVO.setDesc(sinteticaMarcheForm.getDatiInterrMarca()
					.getInterrGener().getDescrizione());
			request.setAttribute("dettaglioMar", dettMarVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaMarcheForm
					.getDatiInterrMarca().getInterrGener().getDescrizione());
			resetToken(request);
			return mapping.findForward("creaMarca");
		}

	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (Navigation.getInstance(request).isFromBar() || !isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sinteticaMarcheForm.getSelezRadio() != null) {
			if (sinteticaMarcheForm.getEsaminaMarSelez().length() > 0) {

				//String keyDesc = sinteticaMarcheForm.getSelezRadio();

				String myForwardName = sinteticaMarcheForm
						.getTabellaEsaminaVO().getMyForwardName(
								sinteticaMarcheForm.getMyPath(),
								sinteticaMarcheForm.getEsaminaMarSelez());
				String myForwardPath = sinteticaMarcheForm
						.getTabellaEsaminaVO().getMyForwardPath(
								sinteticaMarcheForm.getMyPath(),
								sinteticaMarcheForm.getEsaminaMarSelez());

				if (myForwardPath.equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				String myModeCall = sinteticaMarcheForm.getTabellaEsaminaVO()
						.getMyModeCall(sinteticaMarcheForm.getMyPath(),
								sinteticaMarcheForm.getEsaminaMarSelez());
				String myAction = sinteticaMarcheForm.getTabellaEsaminaVO()
						.getMyAction(sinteticaMarcheForm.getMyPath(),
								sinteticaMarcheForm.getEsaminaMarSelez());

				if (sinteticaMarcheForm.getLivRicerca().equals("B")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					sinteticaMarcheForm.getAreePassSifVo().setLivelloRicerca(
							TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					request
							.setAttribute(TitoliCollegatiInvoke.codBiblio,
									"011");
				}
				if (sinteticaMarcheForm.getLivRicerca().equals("P")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					sinteticaMarcheForm.getAreePassSifVo().setLivelloRicerca(
							TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
				}
				if (sinteticaMarcheForm.getLivRicerca().equals("I")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
					sinteticaMarcheForm.getAreePassSifVo().setLivelloRicerca(
							TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
				}

				Class<TitoliCollegatiInvoke> genericClass = TitoliCollegatiInvoke.class;
				int appoggio = 0;
				try {
					Field genericField = genericClass.getField(myForwardName);
					appoggio = genericField.getInt(null);
				} catch (NoSuchFieldException e) {
				}

				SinteticaMarcheView eleSinteticaMarcheView = null;
				String midDaLegare = sinteticaMarcheForm.getSelezRadio()
						.substring(0, 10);
				String desDaRic = "";
				for (int i = 0; i < sinteticaMarcheForm.getListaSintetica()
						.size(); i++) {
					eleSinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm
							.getListaSintetica().get(i);
					if (eleSinteticaMarcheView.getMid().equals(midDaLegare)) {
						desDaRic = eleSinteticaMarcheView.getNome();
						break;
					}
				}

				sinteticaMarcheForm.getAreePassSifVo().setOggettoRicerca(
						appoggio);
				sinteticaMarcheForm.getAreePassSifVo().setOggettoDaRicercare(
						midDaLegare);
				sinteticaMarcheForm.getAreePassSifVo()
						.setDescOggettoDaRicercare(desDaRic);
				sinteticaMarcheForm.getAreePassSifVo().setOggettoChiamante(
						mapping.getPath());
				sinteticaMarcheForm.getAreePassSifVo().setVisualCall(true);

				if (myAction.replace(".", "/").equals(
						myForwardPath.substring(0, myForwardPath.length() - 3))) {
					ActionForward forward = this.load(mapping, form, request,
							"SI");
					if (forward == null) {
						mapping.getInputForward();
					}
				} else {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							sinteticaMarcheForm.getAreePassSifVo()
									.getLivelloRicerca());
					request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
							sinteticaMarcheForm.getAreePassSifVo()
									.getOggettoDaRicercare());
					request.setAttribute(
							TitoliCollegatiInvoke.xidDiRicercaDesc,
							sinteticaMarcheForm.getAreePassSifVo()
									.getDescOggettoDaRicercare());
					request.setAttribute(TitoliCollegatiInvoke.codBiblio,
							sinteticaMarcheForm.getAreePassSifVo()
									.getCodBiblioteca());
					if (sinteticaMarcheForm.getAreePassSifVo().isVisualCall()) {
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
					return Navigation.getInstance(request).goForward(
							actionForward);
					// return actionForward;
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

	// metodo per il caricamento della JSP Sintetica marche
	private void aggiornaForm(HttpServletRequest request,
			SinteticaMarcheForm sinteticaMarcheForm) {

		sinteticaMarcheForm.setTabellaEsaminaVO((TabellaEsaminaVO) this
				.getServlet().getServletContext().getAttribute("serverTypes"));
		List<?> listaCaricamento = sinteticaMarcheForm
				.getTabellaEsaminaVO()
				.getLista(sinteticaMarcheForm.getMyPath());

		MyLabelValueBean eleListaCar;
		List<ComboSoloDescVO> lista = new ArrayList<ComboSoloDescVO>();

		ComboSoloDescVO listaEsamina = new ComboSoloDescVO();
		listaEsamina.setDescrizione("");
		lista.add(listaEsamina);

		int i;
		for (i = 0; i < listaCaricamento.size(); i++) {
			eleListaCar = (MyLabelValueBean) listaCaricamento.get(i);
			listaEsamina = new ComboSoloDescVO();
			if (eleListaCar.getMyLivelloBaseDati().equals("A")
					|| eleListaCar.getMyLivelloBaseDati().equals(sinteticaMarcheForm.getLivRicerca())) {
				listaEsamina.setDescrizione(eleListaCar.getMyLabel());
				lista.add(listaEsamina);
			}
		}
		sinteticaMarcheForm.setListaEsaminaMar(lista);

	}


	//	(Inizio 5Agosto2009)

	public ActionForward catturaEFondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (sinteticaMarcheForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String midDaCatturare = sinteticaMarcheForm.getSelezRadio().substring(0,10);
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaMarcheView eleSinteticaMarcheView = null;
		int size = sinteticaMarcheForm.getListaSintetica().size();
		for (int i = 0; i < size; i++) {
			eleSinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm
					.getListaSintetica().get(i);
			if (eleSinteticaMarcheView.getMid().equals(midDaCatturare)) {
				tipoAutDaCatturare = eleSinteticaMarcheView.getTipoAutority();
				break;
			}
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
		areaDatiPassCattura.setIdPadre(midDaCatturare);
		areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
		areaDatiPassCattura.setInferioriDaCatturare(appo);

		UserVO utenteCollegato = (UserVO) request.getSession()
		.getAttribute(Constants.UTENTE_KEY);

		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaMarca(areaDatiPassCattura,
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

		if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			bidAccorpante = sinteticaMarcheForm.getSelezRadio().substring(0,10);
			bidEliminato = sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getBidPartenza();
			tipoAutAccorpante = eleSinteticaMarcheView.getTipoAutority();
			tipoAutEliminato = eleSinteticaMarcheView.getTipoAutority();
		} else {
			bidAccorpante = sinteticaMarcheForm.getSelezRadio().substring(0,10);
			bidEliminato = sinteticaMarcheForm.getAreaDatiPassPerConfVariazione().getDettMarcaVO().getMid();
			tipoAutAccorpante = eleSinteticaMarcheView.getTipoAutority();
			tipoAutEliminato = eleSinteticaMarcheView.getTipoAutority();

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

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			request.setAttribute("bid", sinteticaMarcheForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		} else  {
			request.setAttribute("bid", sinteticaMarcheForm.getSelezRadio());
		}

		request.setAttribute("livRicerca", sinteticaMarcheForm.getLivRicerca());
		resetToken(request);
		return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
	}
//	(Fine 5Agosto2009)


	public ActionForward prospettaPerLegame(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		if (sinteticaMarcheForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setIdArrivo(
				sinteticaMarcheForm.getSelezRadio().substring(0, 10));
		sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
				.setAuthorityOggettoArrivo("MA");

		SinteticaMarcheView eleSinteticaMarcheView = null;
		String midDaLegare = sinteticaMarcheForm.getSelezRadio().substring(0,
				10);
		for (int i = 0; i < sinteticaMarcheForm.getListaSintetica().size(); i++) {
			eleSinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm
					.getListaSintetica().get(i);
			if (eleSinteticaMarcheView.getMid().equals(midDaLegare)) {
				sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setDescArrivo(
						eleSinteticaMarcheView.getNome());
				sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
						.setFlagCondivisoArrivo(
								eleSinteticaMarcheView.isFlagCondiviso());
				break;
			}
		}

		sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
		sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew(
				"");
		sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
		sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setSiciNew("");
		sinteticaMarcheForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
		sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
				.setSequenzaMusicaNew("");

		request.setAttribute("AreaDatiLegameTitoloVO", sinteticaMarcheForm
				.getAreaDatiLegameTitoloVO());
		if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
				.getAuthorityOggettoPartenza() == null) {
			return mapping.findForward("gestioneLegameTitoloMarca");
		}
		if (sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
				.getAuthorityOggettoPartenza().equals("AU")
				|| sinteticaMarcheForm.getAreaDatiLegameTitoloVO()
						.getAuthorityOggettoPartenza().equals("MA")) {
			return mapping.findForward("gestioneLegameFraAutority");
		}
		return mapping.getInputForward();
	}

	public ActionForward visualizzaImmagini(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaMarcheForm currentForm = (SinteticaMarcheForm) form;

		SinteticaImmaginiMarcheView sinteticaImmaginiMarcheView;
		List<SinteticaImmaginiMarcheView> listaSintImmagini = new ArrayList<SinteticaImmaginiMarcheView>();

		Navigation navi = Navigation.getInstance(request);
		MarcaImageCache imageCache = navi.getMarcaImageCache();

		String[] listaMidSelez = currentForm.getSelezCheck();
		if (ValidazioneDati.isFilled(listaMidSelez) ) {

			for (String midSelez : listaMidSelez) {

				if (!ValidazioneDati.leggiXID(midSelez))
					continue;

				for (Object o : currentForm.getListaSintetica() ) {
					SinteticaMarcheView marca = (SinteticaMarcheView) o;
					if (marca.getMid().equals(midSelez)) {
						int count = ValidazioneDati.size(marca.getListaImmagini());
						if (count > 0) {
							// carico le immagini sulla navigazione
							String mid = marca.getMid();
							imageCache.addImages(mid, marca.getListaImmagini());

							String[] keyImage = new String[count];
							for (int k = 0; k < count; k++)
								keyImage[k] = mid + "-" + String.valueOf(k);

							sinteticaImmaginiMarcheView = new SinteticaImmaginiMarcheView(marca, keyImage);
							listaSintImmagini.add(sinteticaImmaginiMarcheView);
						}
						break;
					}
				}
			}
			//almaviva5_20100624 #3832
			if (ValidazioneDati.isFilled(listaSintImmagini)) {
				request.setAttribute("livRicerca", currentForm.getLivRicerca());
				request.setAttribute("listaSintImmagini", listaSintImmagini);
				if (currentForm.getProspettazionePerLegami().equals("SI")) {
					request.setAttribute("AreaDatiLegameTitoloVO", currentForm.getAreaDatiLegameTitoloVO());
					resetToken(request);
					return navi.goForward(mapping.findForward("sintImmaginiPerLegame"));
				} else {
					resetToken(request);
					return navi.goForward(mapping.findForward("sintImmagini"));
				}
			}

		}

		String selezRadio = currentForm.getSelezRadio();
		if (ValidazioneDati.isFilled(selezRadio) ) {
			String midSelez = selezRadio.substring(0, 10);
			for (Object o : currentForm.getListaSintetica() ) {
				SinteticaMarcheView marca = (SinteticaMarcheView) o;
				if (marca.getMid().equals(midSelez)) {
					int count = ValidazioneDati.size(marca.getListaImmagini());
					if (count > 0) {
						// carico le immagini sulla navigazione
						String mid = marca.getMid();
						imageCache.addImages(mid, marca.getListaImmagini());

						String[] keyImage = new String[count];
						for (int k = 0; k < count; k++)
							keyImage[k] = mid + "-" + String.valueOf(k);

						sinteticaImmaginiMarcheView = new SinteticaImmaginiMarcheView();
						sinteticaImmaginiMarcheView.setMid(marca.getMid());
						sinteticaImmaginiMarcheView.setNome(marca.getNome());
						sinteticaImmaginiMarcheView.setMotto(marca.getMotto());
						sinteticaImmaginiMarcheView.setCitazione(marca.getCitazione());
						sinteticaImmaginiMarcheView.setKeyImage(keyImage);
						listaSintImmagini.add(sinteticaImmaginiMarcheView);
					}
					break;
				}
			}
			//almaviva5_20100624 #3832
			if (ValidazioneDati.isFilled(listaSintImmagini)) {
				request.setAttribute("livRicerca", currentForm.getLivRicerca());
				request.setAttribute("listaSintImmagini", listaSintImmagini);
				if (currentForm.getProspettazionePerLegami().equals("SI")) {
					request.setAttribute("AreaDatiLegameTitoloVO",	currentForm.getAreaDatiLegameTitoloVO());
					resetToken(request);
					return navi.goForward(mapping.findForward("sintImmaginiPerLegame"));
				} else {
					resetToken(request);
					return navi.goForward(mapping.findForward("sintImmagini"));
				}
			}
		}

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.marca.selObblOggConImg"));
		return mapping.getInputForward();

	}

	public ActionForward selezionaTuttiImg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		SinteticaMarcheView eleSinteticaMarcheView = null;
		int numElem = sinteticaMarcheForm.getListaSintetica().size();
		List<String> tmp = new ArrayList<String>();

		for (int i = 0; i < numElem; i++) {
			eleSinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm
					.getListaSintetica().get(i);
			// listaBidSelez[i] = eleSinteticaMarcheView.getMid();
			if (eleSinteticaMarcheView.getListaImmagini().size() > 0)
				tmp.add(eleSinteticaMarcheView.getMid());
		}

		int size = tmp.size();
		if (size > 0) {
			String[] listaBidSelez = new String[size];
			listaBidSelez = tmp.toArray(listaBidSelez);
			sinteticaMarcheForm.setSelezCheck(listaBidSelez);
		}

		return mapping.getInputForward();

	}

	public ActionForward selezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;

		SinteticaMarcheView eleSinteticaMarcheView = null;
		int numElem = sinteticaMarcheForm.getListaSintetica().size();
		String[] listaBidSelez = new String[numElem];
		for (int i = 0; i < numElem; i++) {
			eleSinteticaMarcheView = (SinteticaMarcheView) sinteticaMarcheForm
					.getListaSintetica().get(i);
			listaBidSelez[i] = eleSinteticaMarcheView.getMid();
		}
		sinteticaMarcheForm.setSelezCheck(listaBidSelez);
		return mapping.getInputForward();

	}

	public ActionForward deSelezionaTutti(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaMarcheForm sinteticaMarcheForm = (SinteticaMarcheForm) form;
		sinteticaMarcheForm.setSelezCheck(null);
		return mapping.getInputForward();
	}

}
