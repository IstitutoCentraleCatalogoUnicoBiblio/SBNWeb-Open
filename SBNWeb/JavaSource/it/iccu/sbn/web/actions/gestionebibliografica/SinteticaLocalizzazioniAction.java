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

package it.iccu.sbn.web.actions.gestionebibliografica;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaLocalizzazioniView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.SinteticaLocalizzazioniForm;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.apache.struts.action.ActionRedirect;

public class SinteticaLocalizzazioniAction extends SinteticaLookupDispatchAction {

	public static final String SINTETICA_LOC_RETURN = "SINTETICA_LOC_RETURN";

	private static Logger log = Logger.getLogger(SinteticaLocalizzazioniAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.annulla", "annullaOper");
		map.put("button.dettaglio", "visualDettaglio");
		map.put("button.insLocalizzazione", "insLocalizzazione");
		map.put("button.gestLocal.confermaAgg", "confermaAggior");
		map.put("button.gestLocal.confermaAggDett", "confermaAggiorDettaglio");
		map.put("button.gestLocal.ricarica", "ricarica");
		map.put("button.gestLocal.filtra", "filtra");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		log.debug("unspecified()");
		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;

		if (sinteticaLocalForm.getListaSintetica() != null)
			return mapping.getInputForward();

		sinteticaLocalForm.setCodaPath("");


		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
		if (request.getAttribute("tipoRichiesta") == null) {
			sinteticaLocalForm.setTipoRichiesta("TUTTO");
		} else {
			sinteticaLocalForm.setTipoRichiesta((String) request.getAttribute("tipoRichiesta"));
			if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
				navi.setTesto("Aggiorna dati di Possesso");
			} else {
				navi.setTesto("Modifica localizzazioni per gestione");
			}
		}

		sinteticaLocalForm.setBidLocalizz((List<String>)request.getAttribute("listaBidLocaliz"));

		// Inizio modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010
		if (request.getAttribute("consIndice") != null) {
			String attributo = (String) request.getAttribute("consIndice");
			if (ValidazioneDati.in(attributo,
					"consIndiceEsemplare",
					"listaInventariTitolo",
					"modificaInventario",
					"modificaCollocazione")) {
				sinteticaLocalForm.setTipoProspettazione(attributo);
				gestioneConsistenzaIndice(request, sinteticaLocalForm);
				//almaviva5_20141010
				if (sinteticaLocalForm.getTotRighe() < 0)
					return navi.goBack(true);

				if (sinteticaLocalForm.getTotRighe() == 0) {
					request.setAttribute("diagnostico",
							"Attenzione: in Indice non esistono localizzazioni per Possesso per il titolo "
							+ sinteticaLocalForm.getAreePassSifVo().getOggettoDaRicercare());
					return	navi.goBack(true);
				}
				navi.purgeThis();
				return navi.goForward(mapping.findForward("dettaglioLocalizzazione"));
			}
		}
		// Fine modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010


		if (request.getAttribute("tipoProspettazione") != null) {
			if (request.getAttribute("tipoProspettazione").equals("INS")) {
				request.setAttribute("tipoProspettazione", "VAR");
			}
		}


		if (request.getAttribute("tipoProspettazione") != null) {
			if (request.getAttribute("tipoProspettazione").equals("VAR")) {
				sinteticaLocalForm.setTipoProspettazione("VAR");
				sinteticaLocalForm.setUtilizzoComeSif("NO");
				gestioneModalitaNoSif(request, sinteticaLocalForm);
				return mapping.getInputForward();
			}
		}

		ActionForward forward = this.load(mapping, form, request, "NO");
		if (forward == null) {
			aggiornaForm(request, (SinteticaLocalizzazioniForm) form);
			return mapping.getInputForward();
		}

		String path = forward.getPath();
		if (!path.endsWith(".do"))
			forward.setPath(path.replace(".", "/"));

		forward.setPath(forward.getPath() + sinteticaLocalForm.getCodaPath());
		return forward;
	}


	private void gestioneConsistenzaIndice(HttpServletRequest request,
			SinteticaLocalizzazioniForm sinteticaLocalForm) throws RemoteException, NamingException, CreateException {

		sinteticaLocalForm.setAreePassSifVo((AreePassaggioSifVO) request.getAttribute("areePassaggioSifVO"));

		sinteticaLocalForm.setCentroSistema("NO");
		sinteticaLocalForm.setIdOggColl(sinteticaLocalForm.getAreePassSifVo().getOggettoDaRicercare());
		sinteticaLocalForm.setDescOggColl(sinteticaLocalForm.getAreePassSifVo().getDescOggettoDaRicercare());

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiLocalizzazioniAuthorityVO areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();

		areaDatiPass.setIdLoc(sinteticaLocalForm.getAreePassSifVo().getOggettoDaRicercare());
		areaDatiPass.setCodiceSbn(sinteticaLocalForm.getAreePassSifVo().getCodBiblioteca());

		areaDatiPass.setTipoMat(sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto());
		areaDatiPass.setNatura(sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto());

		if (sinteticaLocalForm.getAreePassSifVo().getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
			sinteticaLocalForm.setLivRic("I");
		} else {
			sinteticaLocalForm.setLivRic("P");
		}
		areaDatiPass.setIndice(sinteticaLocalForm.getAreePassSifVo().getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areaDatiPass.setPolo(sinteticaLocalForm.getAreePassSifVo().getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().cercaLocalizzazioni(areaDatiPass, false, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noConnessione"));

		}

		//almaviva5_20141010
		if (SbnMarcEsitoType.of(areaDatiPassReturn.getCodErr()) == SbnMarcEsitoType.SERVER_NON_DISPONIBILE)
			sinteticaLocalForm.setTotRighe(-1);

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo", areaDatiPassReturn.getTestoProtocollo()));

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.titNotFound", areaDatiPassReturn.getCodErr()));

		}
		// Modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010
		if (areaDatiPassReturn.getTotRighe() < 1)
			return;

		sinteticaLocalForm.setIdLista(areaDatiPassReturn.getIdLista());
		sinteticaLocalForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaLocalForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaLocalForm.setNumLoc(areaDatiPassReturn.getNumNotizie());
		sinteticaLocalForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaLocalForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaLocalForm.setListaSintetica(areaDatiPassReturn	.getListaSintetica());

		SinteticaLocalizzazioniView eleSinteticaLocalizzazioniView = new SinteticaLocalizzazioniView();
		if (sinteticaLocalForm.getListaSintetica().size() > 0) {
			eleSinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(0);

			// Ulteriore Modifica almaviva2 19.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010
			// oltre alla presenza la localizzazione deve essere per Possesso

			SinteticaLocalizzazioniView eleSinteticaLocalizzazioniViewBis;
			for (int i = 0; i < sinteticaLocalForm.getListaSintetica().size(); i++) {
				eleSinteticaLocalizzazioniViewBis = new SinteticaLocalizzazioniView();
				eleSinteticaLocalizzazioniViewBis = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i);
				if (eleSinteticaLocalizzazioniViewBis.getTipoLoc().contains("Possesso")) {
					break;
				} else {
					sinteticaLocalForm.setTotRighe(0);
					return;
				}
			}



		} else {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.titNotFound"));

		}

		request.setAttribute("elementoLocalizzazione", eleSinteticaLocalizzazioniView);
		request.setAttribute("AreePassSifVo", sinteticaLocalForm.getAreePassSifVo());
		request.setAttribute("tipoProspettazione", sinteticaLocalForm.getTipoProspettazione());
		request.setAttribute("idOggColl", sinteticaLocalForm.getIdOggColl());
		request.setAttribute("descOggColl", sinteticaLocalForm.getDescOggColl());
		request.setAttribute("centroSistema", sinteticaLocalForm.getCentroSistema());
		request.setAttribute("livRicerca", sinteticaLocalForm.getLivRic());

		return;

	}



	private ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, String internCall) throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;
		sinteticaLocalForm.setUtilizzoComeSif("NO");
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;

		if (request.getParameter("SIFSINTETICA") != null || internCall.equals("SI")) {
			sinteticaLocalForm.setTipoProspettazione("DET");
			sinteticaLocalForm.setUtilizzoComeSif("SI");
			ActionForward forward = this.gestioneModalitaSif(request, sinteticaLocalForm, internCall);
			if (sinteticaLocalForm.getListaSintetica() != null) {
				sinteticaLocalForm.getListaSinteticaCompleta().addAll(sinteticaLocalForm.getListaSintetica());
				this.caricaPoli(sinteticaLocalForm);
			}
			if (forward == null) {
				return null;
			} else {
				String path = forward.getPath();
				if (!path.endsWith(".do"))
					forward.setPath(path.replace(".", "/"));
				return forward;
			}
		}

		if (request.getAttribute("areaDatiPassReturnSintetica") == null) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));

			return null;
		}
		areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
				.getAttribute("areaDatiPassReturnSintetica");
		sinteticaLocalForm.setIdLista(areaDatiPassReturn.getIdLista());
		sinteticaLocalForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaLocalForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaLocalForm.setNumLoc(areaDatiPassReturn.getNumNotizie());
		sinteticaLocalForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaLocalForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaLocalForm.setLivRic(areaDatiPassReturn.getLivelloTrovato());
		sinteticaLocalForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		aggiornaForm(request, sinteticaLocalForm);

		return null;
	}

	private ActionForward gestioneModalitaSif(HttpServletRequest request,
			SinteticaLocalizzazioniForm sinteticaLocalForm, String internCall)
			throws RemoteException, NamingException, CreateException {

		if (!internCall.equals("SI")) {
			sinteticaLocalForm.getAreePassSifVo().setLivelloRicerca(
					((Integer) (request.getAttribute(TitoliCollegatiInvoke.livDiRicerca))).intValue());
			sinteticaLocalForm.getAreePassSifVo().setOggettoDaRicercare(
					(String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			sinteticaLocalForm.getAreePassSifVo().setDescOggettoDaRicercare(
							(String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));

			sinteticaLocalForm.getAreePassSifVo().setTipMatOggetto(
							(String) request.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));

			sinteticaLocalForm.getAreePassSifVo().setNaturaOggetto(
							(String) request.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));

			sinteticaLocalForm.getAreePassSifVo().setOggettoRicerca(
					((Integer) (request.getAttribute(TitoliCollegatiInvoke.oggDiRicerca))).intValue());
			sinteticaLocalForm.getAreePassSifVo().setOggettoChiamante(
					(String) request.getAttribute(TitoliCollegatiInvoke.oggChiamante));
			sinteticaLocalForm.getAreePassSifVo().setCodBiblioteca(
					(String) request.getAttribute(TitoliCollegatiInvoke.codBiblio));
			if (((String) request.getAttribute(TitoliCollegatiInvoke.visualCall)).equals("SI")) {
				sinteticaLocalForm.getAreePassSifVo().setVisualCall(true);
			} else {
				sinteticaLocalForm.getAreePassSifVo().setVisualCall(false);
			}
		}

		sinteticaLocalForm.setIdOggColl(sinteticaLocalForm.getAreePassSifVo().getOggettoDaRicercare());
		sinteticaLocalForm.setDescOggColl(sinteticaLocalForm.getAreePassSifVo().getDescOggettoDaRicercare());

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiLocalizzazioniAuthorityVO areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();

		areaDatiPass.setIdLoc(sinteticaLocalForm.getAreePassSifVo().getOggettoDaRicercare());

		areaDatiPass.setTipoMat(sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto());
		areaDatiPass.setNatura(sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto());
		if (sinteticaLocalForm.getIdOggColl().substring(3, 4).equals("M")) {
			areaDatiPass.setAuthority("MA");
		} else if (sinteticaLocalForm.getIdOggColl().substring(3, 4).equals("V")) {
			areaDatiPass.setAuthority("AU");
		}

		if (sinteticaLocalForm.getAreePassSifVo().getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
			sinteticaLocalForm.setLivRic("I");
		} else {
			sinteticaLocalForm.setLivRic("P");
		}
		areaDatiPass
				.setIndice(sinteticaLocalForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areaDatiPass
				.setPolo(sinteticaLocalForm.getAreePassSifVo()
						.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().cercaLocalizzazioni(areaDatiPass, false,
						Navigation.getInstance(request).getUserTicket());

		if (sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante().endsWith("analiticaTitolo")) {
			sinteticaLocalForm.setCodaPath("?RETICOLO=TRUE");
			request.setAttribute("livRicerca", sinteticaLocalForm.getAreePassSifVo().getLivelloRicerca());
		}
		if (areaDatiPassReturn == null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noConnessione"));

			ActionForward actionForward = new ActionForward();
			String temp = sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante();

			if (!temp.substring(temp.length() - 3).equals(".do")) {
				actionForward.setPath(temp.replace(".", "/") + ".do");
			} else {
				actionForward.setPath(temp);
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}

			actionForward.setRedirect(true);
			return actionForward;
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));

			ActionForward actionForward = new ActionForward();
			String temp = sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do")) {
				actionForward.setPath(temp.replace(".", "/") + ".do");
			} else {
				actionForward.setPath(temp);
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}

			actionForward.setRedirect(true);
			return actionForward;
		} else if (!areaDatiPassReturn.getCodErr().equals("0000") && !areaDatiPassReturn.getCodErr().equals("3001")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));

			ActionForward actionForward = new ActionForward();
			String temp = sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do")) {
				actionForward.setPath(temp.replace(".", "/") + ".do");
			} else {
				actionForward.setPath(temp);
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}

			actionForward.setRedirect(true);
			return actionForward;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.titNotFound"));

			ActionForward actionForward = new ActionForward();
			String temp = sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do")) {
				actionForward.setPath(temp.replace(".", "/") + ".do");
			} else {
				actionForward.setPath(temp);
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}

			actionForward.setRedirect(true);
			return actionForward;
		}

		if (!sinteticaLocalForm.getAreePassSifVo().isVisualCall()) {
			request.setAttribute(TitoliCollegatiInvoke.arrayListSintetica,	areaDatiPassReturn.getListaSintetica());
			ActionForward actionForward = new ActionForward();
			actionForward.setPath(sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante()	+ ".do");
			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}
			return actionForward;
		}

		// Impostazione della form con i valori tornati dal Server
		sinteticaLocalForm.setIdLista(areaDatiPassReturn.getIdLista());
		sinteticaLocalForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaLocalForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaLocalForm.setNumLoc(areaDatiPassReturn.getNumNotizie());
		sinteticaLocalForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaLocalForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaLocalForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());
		return null;

	}

	private void gestioneModalitaNoSif(HttpServletRequest request,
			SinteticaLocalizzazioniForm sinteticaLocalForm) throws RemoteException, NamingException, CreateException {


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		// INSERIRE CONTROLLI PER VALORIZZAZIONE IN CASO DI CENTRO DI SITEMA;
		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
		if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
			sinteticaLocalForm.setCentroSistema("NO");
		} else {
			sinteticaLocalForm.setCentroSistema("SI");
		}

		// CHIAMATA ALLA FUNZIONE CODICI PER CARICARE COMBO TIPI-LOCALIZZAZIONI

		CaricamentoCombo carCombo = new CaricamentoCombo();

		try {
			sinteticaLocalForm.setListaMutiloM(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSiNo()));
		} catch (RemoteException e2) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e2.getMessage()));

		} catch (CreateException e2) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e2.getMessage()));

		}


		// CHIAMATA ALL'EJB DI AMMINISTRAZIONE PER LISTA BIBLIOTECHE DEL POLO
		try {
			sinteticaLocalForm.setListaBiblio(factory.getGestioneBibliografica()
					.getComboBibliotechePolo(Navigation.getInstance(request).getUtente().getCodPolo(),
							Navigation.getInstance(request).getUserTicket()));
		} catch (RemoteException e1) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e1.getMessage()));

		}
		ComboCodDescVO comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("");
		comboCodDescVO.setDescrizione("");
		sinteticaLocalForm.addListaBiblio(comboCodDescVO);
		Collections.sort(sinteticaLocalForm.getListaBiblio(), ComboCodDescVO.ORDINA_PER_CODICE);

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiLocalizzazioniAuthorityVO areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();

		sinteticaLocalForm.setIdOggColl((String) request.getAttribute("bid"));
		sinteticaLocalForm.setDescOggColl((String) request.getAttribute("desc"));

		areaDatiPass.setIdLoc(sinteticaLocalForm.getIdOggColl());
		areaDatiPass.setAuthority((String) request.getAttribute("tipoAuth"));

		if (areaDatiPass.getAuthority() == null
				|| areaDatiPass.getAuthority().equals("UM")
				|| areaDatiPass.getAuthority().equals("TU")) {
			areaDatiPass.setNatura((String) request.getAttribute("natura"));
			sinteticaLocalForm.getAreePassSifVo().setNaturaOggetto((String) request.getAttribute("natura"));
			areaDatiPass.setTipoMat((String) request.getAttribute("tipoMat"));
			sinteticaLocalForm.getAreePassSifVo().setTipMatOggetto((String) request.getAttribute("tipoMat"));
		}

		if (sinteticaLocalForm.getCentroSistema().equals("SI")) {
			areaDatiPass.setCodiceSbn(Navigation.getInstance(request).getUtente().getCodPolo() + "   ");
		} else {
			areaDatiPass.setCodiceSbn(Navigation.getInstance(request).getUtente().getCodPolo()
					+ Navigation.getInstance(request).getUtente().getCodBib());
		}

		if (((String) request.getAttribute("livRic")).equals("I")) {
			sinteticaLocalForm.setLivRic("I");
			areaDatiPass.setIndice(true);
			areaDatiPass.setPolo(false);
		} else {
			sinteticaLocalForm.setLivRic("P");
			areaDatiPass.setIndice(false);
			areaDatiPass.setPolo(true);
		}

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		try {
			areaDatiPassReturn = factory
					.getGestioneBibliografica().cercaLocalizzazioni(areaDatiPass, false,
							Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		request.setAttribute("bid", sinteticaLocalForm.getIdOggColl());
		request.setAttribute("livRicerca", sinteticaLocalForm.getLivRic());

		if (areaDatiPassReturn == null) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));

		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));

		} else if (!areaDatiPassReturn.getCodErr().equals("0000") && !areaDatiPassReturn.getCodErr().equals("3001")) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));

		}

		try {
			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			if (sinteticaLocalForm.getLivRic().equals("P")) {
				if (areaDatiPass.getNatura() != null) {
					if (areaDatiPass.getNatura().equals("M") || areaDatiPass.getNatura().equals("W") ||
						areaDatiPass.getNatura().equals("S") || areaDatiPass.getNatura().equals("C") ||
						areaDatiPass.getNatura().equals("N") || areaDatiPass.getNatura().equals("R")) {
							sinteticaLocalForm.setListaTipoLocalizzazione(carCombo.loadComboCodiciDesc
												(factory.getCodici().getCodiceTipoLocalizzazione("POLO")));
					} else {
							sinteticaLocalForm.setListaTipoLocalizzazione(carCombo.loadComboCodiciDesc
												(factory.getCodici().getCodiceTipoLocalizzazione("NESSUNA")));
					}
				} else {
						sinteticaLocalForm.setListaTipoLocalizzazione(carCombo.loadComboCodiciDesc
											(factory.getCodici().getCodiceTipoLocalizzazione("NESSUNA")));
				}
			} else {
				if (areaDatiPass.getNatura() != null) {
					// Intervento del 27.7.2012 almaviva2 intervento richiesto da almaviva1/almaviva
					// per chiusura collaudo collana ha localizzazione solo x gestione.
//					if (areaDatiPass.getNatura().equals("M") || areaDatiPass.getNatura().equals("W") ||
//						areaDatiPass.getNatura().equals("S") || areaDatiPass.getNatura().equals("C") ||
//						areaDatiPass.getNatura().equals("N")) {
					if (areaDatiPass.getNatura().equals("M") || areaDatiPass.getNatura().equals("W") ||
						areaDatiPass.getNatura().equals("S") || areaDatiPass.getNatura().equals("N")) {
							sinteticaLocalForm.setListaTipoLocalizzazione(carCombo.loadComboCodiciDesc
									(factory.getCodici().getCodiceTipoLocalizzazione("ALL")));
					} else {
							sinteticaLocalForm.setListaTipoLocalizzazione(carCombo.loadComboCodiciDesc
												(factory.getCodici().getCodiceTipoLocalizzazione("POS")));
					}
				} else {
						sinteticaLocalForm.setListaTipoLocalizzazione(carCombo.loadComboCodiciDesc
											(factory.getCodici().getCodiceTipoLocalizzazione("POS")));
				}
			}
		} catch (RemoteException e2) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e2.getMessage()));

		} catch (CreateException e2) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e2.getMessage()));

		}

		try {
			if (areaDatiPassReturn.getNumNotizie() == 0 || areaDatiPassReturn.getCodErr().equals("3001")) {
				sinteticaLocalForm.setTipoProspettazione("VAR");

				SinteticaLocalizzazioniView sinteticaLocalizzazioniView;
				List<SinteticaLocalizzazioniView> listaSintentica = new ArrayList<SinteticaLocalizzazioniView>();

				sinteticaLocalizzazioniView = new SinteticaLocalizzazioniView();
				sinteticaLocalizzazioniView.setProgressivo(1);
				sinteticaLocalizzazioniView.setIDSbn("");
				sinteticaLocalizzazioniView.setIDAnagrafe("");
				sinteticaLocalizzazioniView.setCodBiblioteca(Navigation.getInstance(request).getUtente().getCodBib());
				sinteticaLocalizzazioniView.setDescrBiblioteca("");
				sinteticaLocalizzazioniView.setTipoLoc("Nessuna");

				// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
				// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
				// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
				if (sinteticaLocalForm.getTipoRichiesta().equals("GESTIONE")) {
					sinteticaLocalizzazioniView.setListaTipoLoc(
							carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("NESGES")));
				} else if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
					sinteticaLocalizzazioniView.setListaTipoLoc(
							carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("NESPOS")));
				} else {
					sinteticaLocalizzazioniView.setListaTipoLoc(
							carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("ALL")));
				}

				sinteticaLocalizzazioniView.setFondo("");
				sinteticaLocalizzazioniView.setSegnatura("");
				sinteticaLocalizzazioniView.setSegnaturaAntica("");
				sinteticaLocalizzazioniView.setConsistenza("");
				sinteticaLocalizzazioniView.setNote("");
				sinteticaLocalizzazioniView.setFormatoElettronico("N");
				sinteticaLocalizzazioniView.setValoreM("N");
				sinteticaLocalizzazioniView.setUriCopiaElettr("");
				sinteticaLocalizzazioniView.setTipoDigitalizzazione("0");
				listaSintentica.add(sinteticaLocalizzazioniView);
				sinteticaLocalForm.setIdLista("1");
				sinteticaLocalForm.setMaxRighe(1);
				sinteticaLocalForm.setNumBlocco(1);
				sinteticaLocalForm.setNumLoc(1);
				sinteticaLocalForm.setTotBlocchi(1);
				sinteticaLocalForm.setTotRighe(1);
				sinteticaLocalForm.setListaSintetica(listaSintentica);

			} else {
				sinteticaLocalForm.setIdLista(areaDatiPassReturn.getIdLista());
				sinteticaLocalForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
				sinteticaLocalForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
				sinteticaLocalForm.setNumLoc(areaDatiPassReturn.getNumNotizie());
				sinteticaLocalForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
				sinteticaLocalForm.setTotRighe(areaDatiPassReturn.getTotRighe());
				sinteticaLocalForm.setListaSintetica(areaDatiPassReturn.getListaSintetica());

				SinteticaLocalizzazioniView eleSinteticaLocalizzazioniView;

				// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
				// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
				// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
				for (int i = 0; i < sinteticaLocalForm.getListaSintetica().size(); i++) {
					eleSinteticaLocalizzazioniView = new SinteticaLocalizzazioniView();
					eleSinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i);
					sinteticaLocalForm.addTabBibTipoLocOld(eleSinteticaLocalizzazioniView.getTipoLoc().toString());
					if (sinteticaLocalForm.getTipoRichiesta().equals("GESTIONE")) {
						if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Gestione")
								|| eleSinteticaLocalizzazioniView.getTipoLoc().equals("Nessuna")) {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
											(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("NESGES")));
						} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso")
								|| eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso/Gestione")) {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
											(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("POSTUTTI")));
						} else {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
											(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("ALL")));
						}
					} else if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
						if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Nessuna")) {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
											(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("NESPOS")));
						} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Gestione")) {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
											(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("GESTUTTI")));
						} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso")) {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
											(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("SOLOPOS")));
						} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso/Gestione")) {

							if (((String) request.getAttribute("livRic")).equals("I")) {
								((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
										(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("SOLOTUTTI")));
							} else {
								((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
										(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("SOLOPOS")));
							}


						} else {
							((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
							(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("ALL")));
						}
					} else {
						((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i)).setListaTipoLoc
						(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("ALL")));
					}
				}
			}
		} catch (RemoteException e2) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e2.getMessage()));

		} catch (CreateException e2) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + e2.getMessage()));

		}
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;
		request.setAttribute("bid", sinteticaLocalForm.getIdOggColl());
		request.setAttribute("livRicerca", sinteticaLocalForm.getLivRic());

		if (sinteticaLocalForm.getTipoProspettazione().equals("VAR")) {
			return Navigation.getInstance(request).goBack(
					mapping.findForward("annulla"), true);
		} else {
			if (sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante().endsWith("analiticaTitolo")) {
				sinteticaLocalForm.setCodaPath("?RETICOLO=TRUE");
				request.setAttribute("livRicerca", sinteticaLocalForm
						.getAreePassSifVo().getLivelloRicerca());
			}
			ActionRedirect forward = new ActionRedirect();
			String temp = sinteticaLocalForm.getAreePassSifVo().getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do"))
				forward.setPath(temp.replace(".", "/") + ".do" + sinteticaLocalForm.getCodaPath());
			else
				forward.setPath(temp + sinteticaLocalForm.getCodaPath());


			forward.setRedirect(true);
			forward.addParameter(SINTETICA_LOC_RETURN, "true");
			return forward;
		}
	}

	// metodo per il caricamento della JSP Sintetica marche
	private void aggiornaForm(HttpServletRequest request,
			SinteticaLocalizzazioniForm sinteticaLocalizzazioniForm) {
		sinteticaLocalizzazioniForm.setIdOggColl(sinteticaLocalizzazioniForm.getAreePassSifVo().getOggettoDaRicercare());
		sinteticaLocalizzazioniForm.setDescOggColl(sinteticaLocalizzazioniForm.getAreePassSifVo().getDescOggettoDaRicercare());
	}


	public ActionForward insLocalizzazione(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;
		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		SinteticaLocalizzazioniView sintLocWiew = new SinteticaLocalizzazioniView();
		sintLocWiew.setProgressivo(sinteticaLocalForm.getTotRighe() +1);
		sinteticaLocalForm.setTotRighe(sinteticaLocalForm.getTotRighe() +1);
		sintLocWiew.setCodBiblioteca("");
		sintLocWiew.setIDSbn("");
		sintLocWiew.setDescrBiblioteca("");
		sintLocWiew.setIDAnagrafe("");
		sintLocWiew.setListaTipoLoc	(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoLocalizzazione("NESGES")));
		sinteticaLocalForm.addListaSintetica(sintLocWiew);
		return mapping.getInputForward();
	}

	public ActionForward confermaAggior(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {



		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;

		// Evolutiva Google3: Modifica per controllo abilitazioni
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			if (sinteticaLocalForm.getTipoRichiesta().equals("GESTIONE")) {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_GESTIONE);
			} else if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
				//almaviva5_20140521 evolutive google3
				//utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_1008);
			}
		}
		catch(UtenteNotAuthorizedException ute)	{

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));

			resetToken(request);
			return mapping.getInputForward();
		}
		catch (RemoteException e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));

			resetToken(request);
			return mapping.getInputForward();
		}

		AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla = caricaAreaLocalizzazione(request, sinteticaLocalForm);
		if (areaLocalizzaMultipla.getCodErr().equals("9999")) {

			LinkableTagUtils.addError(request, new ActionMessage(areaLocalizzaMultipla.getTestoErr()));

			resetToken(request);
			return mapping.getInputForward();
		}
		if (areaLocalizzaMultipla.getListaAreaLocalizVO().isEmpty()) {
			request.setAttribute("livRicerca", sinteticaLocalForm.getLivRic());
			request.setAttribute("vaiA", "SI");

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.operOkConParametro", "Nessuna Operazione effettuata"));

			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

//		AreaDatiVariazioneReturnVO areaDatiPassReturn = factory.getGestioneBibliografica().
//							localizzaAuthorityMultipla(areaLocalizzaMultipla, Navigation.getInstance(request).getUserTicket());

		AreaDatiVariazioneReturnVO areaDatiPassReturn = factory.getGestioneBibliografica().
		localizzaUnicoXML(areaLocalizzaMultipla, Navigation.getInstance(request).getUserTicket());


		if (areaDatiPassReturn == null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noConnessione"));

			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			request.setAttribute("livRicerca", sinteticaLocalForm.getLivRic());
			request.setAttribute("vaiA", "SI");

			if (areaDatiPassReturn.getTestoProtocolloInformational() != null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.operOkConParametro", areaDatiPassReturn.getTestoProtocolloInformational()));
			} else {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.operOk"));
			}

			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
		} else if (areaDatiPassReturn.getCodErr().equals("9999") && areaDatiPassReturn.getTestoProtocollo() != null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo", areaDatiPassReturn.getTestoProtocollo()));

			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


	public ActionForward confermaAggiorDettaglio(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;

		// Evolutiva Google3:Modifica per controllo abilitazioni
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			if (sinteticaLocalForm.getTipoRichiesta().equals("GESTIONE")) {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_GESTIONE);
			} else if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
				//almaviva5_20140521 evolutive google3
				//utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_1008);
			}
		}
		catch(UtenteNotAuthorizedException ute)	{

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));

			resetToken(request);
			return mapping.getInputForward();
		}
		catch (RemoteException e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));

			resetToken(request);
			return mapping.getInputForward();
		}

		AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla = caricaAreaLocalizzazione(request, sinteticaLocalForm);
		if (areaLocalizzaMultipla.getCodErr().equals("9999")) {

			LinkableTagUtils.addError(request, new ActionMessage(areaLocalizzaMultipla.getTestoErr()));

			resetToken(request);
			return mapping.getInputForward();
		}

		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
		// Nel caso di POSSESSO dopo l'aggiornamento si prospettano direttamente i dati di dettaglio;
		sinteticaLocalForm.setSelezRadio("1");
		sinteticaLocalForm.setAreaLocalizzaMultipla(areaLocalizzaMultipla);
		return visualDettaglio(mapping, sinteticaLocalForm, request, response);
	}

	public ActionForward visualDettaglio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;

		if (sinteticaLocalForm.getSelezRadio() != null
				&& !sinteticaLocalForm.getSelezRadio().equals("")) {
			String progressivo = sinteticaLocalForm.getSelezRadio();

			SinteticaLocalizzazioniView eleSinteticaLocalizzazioniView = null;
			for (int i = 0; i < sinteticaLocalForm.getListaSintetica().size(); i++) {
				eleSinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i);

				// Inizio modifica almaviva2 BUG MANTIS 4158 (collaudo) 18.01.2011
				// viene valorizzata la descrizione del campo tipoDigitalizzazione per la mappa del dettaglio
				if (eleSinteticaLocalizzazioniView.getTipoDigitalizzazione() != null) {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					sinteticaLocalForm.getAreePassSifVo().setDescTipoDigitPolo(
							factory.getCodici().cercaDescrizioneCodice(	eleSinteticaLocalizzazioniView.getTipoDigitalizzazione(),
							CodiciType.CODICE_TIPO_DIGITALIZZAZIONE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
				}
				// Fine modifica almaviva2 BUG MANTIS 4158 (collaudo) 18.01.2011

				if (eleSinteticaLocalizzazioniView.getProgressivo() == Integer.valueOf(progressivo)) {

					if (!eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso")
							&& !eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso/Gestione")) {

						LinkableTagUtils.addError(request, new ActionMessage(
								"errors.gestioneBibliografica.tipoLocalInsufficiente"));

						return mapping.getInputForward();
					}

					request.setAttribute("elementoLocalizzazione", eleSinteticaLocalizzazioniView);
					request.setAttribute("AreePassSifVo", sinteticaLocalForm.getAreePassSifVo());

					// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
					// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
					// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
					if (sinteticaLocalForm.getTipoRichiesta().equals("GESTIONE")) {
						request.setAttribute("tipoProspettazione", "DET");
					} else {
						request.setAttribute("tipoProspettazione", sinteticaLocalForm.getTipoProspettazione());
					}
					request.setAttribute("idOggColl", sinteticaLocalForm.getIdOggColl());
					request.setAttribute("descOggColl", sinteticaLocalForm.getDescOggColl());
					request.setAttribute("centroSistema", sinteticaLocalForm.getCentroSistema());
					request.setAttribute("livRicerca", sinteticaLocalForm.getLivRic());
					request.setAttribute("areaLocalizzaMultipla", sinteticaLocalForm.getAreaLocalizzaMultipla());
					break;
				}
			}
			return Navigation.getInstance(request).goForward(mapping.findForward("dettaglioLocalizzazione"));
		}


		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneBibliografica.selObblOggSint"));

		return mapping.getInputForward();
	}

	public ActionForward ricarica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;
		sinteticaLocalForm.getListaSintetica().clear();
		sinteticaLocalForm.getListaSintetica().addAll(sinteticaLocalForm.getListaSinteticaCompleta());
		return mapping.getInputForward();
	}

	public ActionForward filtra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaLocalizzazioniForm sinteticaLocalForm = (SinteticaLocalizzazioniForm) form;
		sinteticaLocalForm.getListaSintetica().clear();

		if (sinteticaLocalForm.getCodPoloRicercaSelez() == null || sinteticaLocalForm.getCodPoloRicercaSelez().equals("")) {
			sinteticaLocalForm.getListaSintetica().addAll(sinteticaLocalForm.getListaSinteticaCompleta());
			return mapping.getInputForward();
		}

		SinteticaLocalizzazioniView localizzazioniView = null;
		for (int i = 0; i < sinteticaLocalForm.getListaSinteticaCompleta().size(); i++) {
			localizzazioniView = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSinteticaCompleta().get(i);
			if (localizzazioniView.getIDSbn().toUpperCase().indexOf(sinteticaLocalForm.getCodPoloRicercaSelez()) > -1) {
				sinteticaLocalForm.getListaSintetica().add(sinteticaLocalForm.getListaSinteticaCompleta().get(i));
			}
		}
		return mapping.getInputForward();
	}

	private void caricaPoli(SinteticaLocalizzazioniForm sinteticaLocalForm) {

		List<ComboSoloDescVO> lista = new ArrayList<ComboSoloDescVO>();
		ComboSoloDescVO soloDescVO;

		soloDescVO = new ComboSoloDescVO();
		soloDescVO.setDescrizione("");
		lista.add(soloDescVO);

		if (sinteticaLocalForm.getListaSinteticaCompleta().size() > 0) {
			SinteticaLocalizzazioniView localizzazioniView = null;
			String oldsoloDescVO = ((SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSinteticaCompleta().get(0)).getIDSbn().substring(0,3);
			soloDescVO = new ComboSoloDescVO();
			soloDescVO.setDescrizione(oldsoloDescVO);
			lista.add(soloDescVO);

			for (int i = 0; i < sinteticaLocalForm.getListaSinteticaCompleta().size(); i++) {
				localizzazioniView = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSinteticaCompleta().get(i);
				if (!localizzazioniView.getIDSbn().substring(0,3).equals(oldsoloDescVO)) {
					soloDescVO = new ComboSoloDescVO();
					soloDescVO.setDescrizione(localizzazioniView.getIDSbn().substring(0,3));
					lista.add(soloDescVO);
					oldsoloDescVO = localizzazioniView.getIDSbn().substring(0,3);
				}
			}
			sinteticaLocalForm.setListaCodPolo(lista);
		}

		return;
	}

	private AreaDatiLocalizzazioniAuthorityMultiplaVO caricaAreaLocalizzazione(
								HttpServletRequest request, SinteticaLocalizzazioniForm sinteticaLocalForm) {

		AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
		areaLocalizzaMultipla.setCodErr("0000");

		AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
		areaLocalizza.setIdLoc(sinteticaLocalForm.getIdOggColl());

		String oldTipoLoc;
		String appoggioNatura;
		String appoggioTipoMat;
		String appoggioAuthority;
		if (sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto() == null
				&& sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto() == null) {
			areaLocalizza.setNatura("");
			areaLocalizza.setTipoMat("");
			if (sinteticaLocalForm.getIdOggColl().substring(3, 4).equals("V")) {
				areaLocalizza.setAuthority("AU");
			} else if (sinteticaLocalForm.getIdOggColl().substring(3, 4)
					.equals("M")) {
				areaLocalizza.setAuthority("MA");
			}
		} else if (sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto() != null
				&& sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto() != null) {
			if (sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto().equals("A")) {
				if (sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto().equals("U")) {
					areaLocalizza.setAuthority("UM");
					areaLocalizza.setNatura(sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto());
					areaLocalizza.setTipoMat(sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto());
				} else {
					areaLocalizza.setAuthority("TU");
					areaLocalizza.setNatura(sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto());
					areaLocalizza.setTipoMat(sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto());
				}
			} else {
				areaLocalizza.setAuthority("");
				areaLocalizza.setNatura(sinteticaLocalForm.getAreePassSifVo().getNaturaOggetto());
				areaLocalizza.setTipoMat(sinteticaLocalForm.getAreePassSifVo().getTipMatOggetto());
			}
		}

		appoggioNatura = areaLocalizza.getNatura();
		appoggioTipoMat = areaLocalizza.getTipoMat();
		appoggioAuthority = areaLocalizza.getAuthority();


		for (int i = 0; i < sinteticaLocalForm.getListaSintetica().size(); i++) {

			oldTipoLoc = "";
			SinteticaLocalizzazioniView eleSinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) sinteticaLocalForm.getListaSintetica().get(i);
			if  (eleSinteticaLocalizzazioniView.getCodBiblioteca().equals("")) {
				continue;
			}
			if  (eleSinteticaLocalizzazioniView.getIDAnagrafe() != null) {
				if  (!eleSinteticaLocalizzazioniView.getIDAnagrafe().equals("")) {
					if  (eleSinteticaLocalizzazioniView.getTipoLoc().toString().equals(sinteticaLocalForm.getTabBibTipoLocOld().get(i))) {
						continue;
					}
				}
			}
			if  (eleSinteticaLocalizzazioniView.getIDAnagrafe() == null ||
					eleSinteticaLocalizzazioniView.getIDAnagrafe().equals("")) {
					oldTipoLoc = "Nessuna";
			} else {
				if  (eleSinteticaLocalizzazioniView.getTipoLoc().toString().equals(sinteticaLocalForm.getTabBibTipoLocOld().get(i))) {
					continue;
				} else {
					oldTipoLoc = sinteticaLocalForm.getTabBibTipoLocOld().get(i).toString();
				}
			}

			areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
			areaLocalizza.setIdLoc(sinteticaLocalForm.getIdOggColl());
			areaLocalizza.setAuthority(appoggioAuthority);
			areaLocalizza.setNatura(appoggioNatura);
			areaLocalizza.setTipoMat(appoggioTipoMat);

			// Interventi Marzo 2014 su Gestione localizzazioni: quando su una loc di "Gestione" si richiede "Possesso" si deve inviare
			// prima una delocalizzazione per "Gestione" e poi una localizzazione pere "Possesso" altrimenti si ottiene "Possesso/Gestione"
			// stessa cosa per quando su una loc di "Possesso" si richiede "Gestione" si deve inviare
			// prima una delocalizzazione per "Possesso" e poi una localizzazione pere "Gestione" altrimenti si ottiene "Possesso/Gestione"
			// Inserimento controlli preventivi su Abilitazione a localizzare o Delocalizzare per evitare che l'operazione
			// venga effettuata in Indice ma non in Polo generando così squadrature
			String tipoOpeBis = "";
			String tipoLocBis = "";
			if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Nessuna")) {
				areaLocalizza.setTipoOpe("Delocalizza");
				areaLocalizza.setTipoLoc("Tutti");
			} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso/Gestione")) {
				areaLocalizza.setTipoOpe("Localizza");

				if (sinteticaLocalForm.getLivRic().equals("I")) {
					if (oldTipoLoc.equals("Gestione")) {
						areaLocalizza.setTipoLoc("Possesso");
					} else if (oldTipoLoc.equals("Possesso")) {
						areaLocalizza.setTipoLoc("Gestione");
					}else if (oldTipoLoc.equals("Nessuna")) {
						areaLocalizza.setTipoLoc("Possesso/Gestione");
					}
				} else {
					areaLocalizza.setTipoLoc("Possesso/Gestione");
				}

			} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Possesso")) {
				if (oldTipoLoc.equals("Nessuna")) {
					areaLocalizza.setTipoOpe("Localizza");
					// La localizzazione per POSSESSO se prima non ce ne era nessuna diveta AUTOMATICAMENTE POSSESSO/GESTIONE
//					areaLocalizza.setTipoLoc("Possesso");
					areaLocalizza.setTipoLoc("Possesso/Gestione");
				} else if (oldTipoLoc.equals("Gestione")) {
					// Inserita qui la seconda operazione da effettuare per ottenere la situazione di localizzazione selezionata
					areaLocalizza.setTipoOpe("Delocalizza");
					areaLocalizza.setTipoLoc("Gestione");
					tipoOpeBis = "Localizza";
					tipoLocBis = "Possesso";
				} else if (oldTipoLoc.equals("Possesso/Gestione")) {
					areaLocalizza.setTipoOpe("Delocalizza");
					areaLocalizza.setTipoLoc("Gestione");
				}
			} else if (eleSinteticaLocalizzazioniView.getTipoLoc().equals("Gestione")) {
				if (oldTipoLoc.equals("Nessuna")) {
					areaLocalizza.setTipoOpe("Localizza");
					areaLocalizza.setTipoLoc("Gestione");
				} else if (oldTipoLoc.equals("Possesso")) {
					// Inserita qui la seconda operazione da effettuare per ottenere la situazione di localizzazione selezionata
					areaLocalizza.setTipoOpe("Delocalizza");
					areaLocalizza.setTipoLoc("Possesso");
					tipoOpeBis = "Localizza";
					tipoLocBis = "Gestione";
				} else if (oldTipoLoc.equals("Possesso/Gestione")) {
					areaLocalizza.setTipoOpe("Delocalizza");
					areaLocalizza.setTipoLoc("Possesso");
				}
			}

			// Modifica per controllo abilitazioni
			Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				if ((areaLocalizza.getTipoOpe().equals("Delocalizza") && areaLocalizza.getTipoLoc().equals("Gestione"))
						|| (tipoOpeBis.equals("Delocalizza") && tipoLocBis.equals("Gestione"))) {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().DELOCALIZZA_PER_GESTIONE_1014);
				} else if ((areaLocalizza.getTipoOpe().equals("Delocalizza") && areaLocalizza.getTipoLoc().equals("Possesso"))
						|| (tipoOpeBis.equals("Delocalizza") && tipoLocBis.equals("Possesso"))) {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().DELOCALIZZA_PER_POSSEDUTO_1012);
				} else if ((areaLocalizza.getTipoOpe().equals("Localizza") && areaLocalizza.getTipoLoc().equals("Gestione"))
						|| (tipoOpeBis.equals("Localizza") && tipoLocBis.equals("Gestione"))) {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009);
				} else if ((areaLocalizza.getTipoOpe().equals("Localizza") && areaLocalizza.getTipoLoc().equals("Possesso"))
						|| (tipoOpeBis.equals("Localizza") && tipoLocBis.equals("Possesso"))) {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_1008);
				}
			}
			catch(UtenteNotAuthorizedException ute)	{
				areaLocalizzaMultipla.setCodErr("9999");
				areaLocalizzaMultipla.setTestoErr("errors.gestioneBibliografica.autNotAuthorized");
			}
			catch (RemoteException e) {
				areaLocalizzaMultipla.setCodErr("9999");
				areaLocalizzaMultipla.setTestoErr("errors.gestioneBibliografica.autNotAuthorized");
			}

			if (eleSinteticaLocalizzazioniView.getIDSbn().equals("")) {
				eleSinteticaLocalizzazioniView.setIDSbn(Navigation.getInstance(request).getUtente().getCodPolo() +
						eleSinteticaLocalizzazioniView.getCodBiblioteca());
			}

			areaLocalizza.setConsistenza(eleSinteticaLocalizzazioniView.getConsistenza());
			areaLocalizza.setIndicatoreMutilo(eleSinteticaLocalizzazioniView.getValoreM());
			if (sinteticaLocalForm.getLivRic().equals("I")) {
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(true);
				areaLocalizza.setMantieniAllineamento(true);
			} else if (sinteticaLocalForm.getLivRic().equals("P")) {
				if (!areaLocalizza.getAuthority().equals("")) {
					areaLocalizzaMultipla.setCodErr("9999");
					areaLocalizzaMultipla.setTestoErr("errors.gestioneBibliografica.LocalNonPoss");
				}
				areaLocalizza.setIndice(false);
				areaLocalizza.setPolo(true);
			}

			areaLocalizza.setCodiceSbn(eleSinteticaLocalizzazioniView.getIDSbn());
			areaLocalizzaMultipla.addListaAreaLocalizVO(areaLocalizza);

			if (tipoOpeBis != "" && tipoLocBis != "") {
				AreaDatiLocalizzazioniAuthorityVO areaLocalizzaBis = (AreaDatiLocalizzazioniAuthorityVO) areaLocalizza.clone();
				areaLocalizzaBis.setTipoOpe(tipoOpeBis);
				areaLocalizzaBis.setTipoLoc(tipoLocBis);
				areaLocalizzaMultipla.addListaAreaLocalizVO(areaLocalizzaBis);
				tipoOpeBis = "";
				tipoLocBis = "";
			}

			// Solo nel caso di POSSESSO viene effettuata l'estenzione a tutti gli elementi del reticolo delle caratteristiche
			// del bid Radice a tutti gli elementi di tipo DOC del reticolo
			if (sinteticaLocalForm.getTipoRichiesta().equals("POSSESSO")) {
				List<String> bidLocalizz = sinteticaLocalForm.getBidLocalizz();
				if (ValidazioneDati.isFilled(bidLocalizz) ) {
					for (int j = 0; j < bidLocalizz.size(); j++) {
						if (!bidLocalizz.get(j).equals(sinteticaLocalForm.getIdOggColl())) {
							AreaDatiLocalizzazioniAuthorityVO areaLocalizzaTer = (AreaDatiLocalizzazioniAuthorityVO) areaLocalizza.clone();
							areaLocalizzaTer.setIdLoc(bidLocalizz.get(j));
							areaLocalizzaMultipla.addListaAreaLocalizVO(areaLocalizzaTer);
						}
					}
				}
			}

		}

		return areaLocalizzaMultipla;
	}


}
