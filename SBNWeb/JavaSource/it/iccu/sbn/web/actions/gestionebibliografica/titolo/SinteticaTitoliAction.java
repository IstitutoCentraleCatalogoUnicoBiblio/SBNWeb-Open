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

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.batch.unimarc.ExportUnimarcBatch;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RicercaTitCollEditoriVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoEstrazioneUnimarc;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.SinteticaTitoliForm;
import it.iccu.sbn.web.actions.common.SbnDownloadAction;
import it.iccu.sbn.web.actions.gestionebibliografica.SinteticaLocalizzazioniAction;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.MyLabelValueBean;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaTitoliAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(SinteticaTitoliAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		// tasti per la prospettazione della lista sintetica da interrogazione
		map.put("button.blocco", "carBlocco");
		map.put("button.analiticaTit", "analiticaTit");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.creaTit", "creaTit");
		map.put("button.creaTitLoc", "creaTitLoc");
		map.put("button.esamina", "esamina");
		map.put("button.confermaGestisci", "confermaGestisci");
//		map.put("button.exportUnimarc", "exportUnimarc");
//		map.put("button.stampaSchede", "stampaSchede");
		// tasti per la selezione/deseleziona di tutti gli elementi della lista
		map.put("button.selAllTitoli", "selezionaTutti");
		map.put("button.deSelAllTitoli", "deSelezionaTutti");
		map.put("button.salvaSuFile", "salvaSuFile");
		// tasti per la prospettazione dei simili
		map.put("button.gestSimili.riAggiorna", "tornaAdAggiornamento");
		map.put("button.gestSimili.confermaAgg", "confermaAggiornamento");
		map.put("button.gestSimili.fusione", "fondiOggetti");
		map.put("button.gestSimili.cattura", "catturaOggetto");
		map.put("button.rinunciaFusione", "tornaAnalitica");
		// tasti per la prospettazione dei titoli per creazione nuovo legame
		map.put("button.gestLegami.lega", "prospettaPerLegame");
		// tasto per tornare ai servizi riportando il bid selezionato
		map.put("button.gestPerServizi.scegli", "tornaAServizi");
		map.put("button.gestPerMovimentiUtente.scegli", "tornaAMovimentiUtente");
		// tasto per tornare ai servizi riportando il bid selezionato
		map.put("button.gestPerAcquisizioni.scegli", "tornaAAcquisizioni");
		// tasti per la prospettazione dei simili a seguito di condividi notizia locale
		map.put("button.gestSimiliCondividi.catturaEFondi", "catturaEFondi");
		map.put("button.gestSimiliCondividi.catturaESpostaLegame", "catturaESpostaLegame");
		map.put("button.gestSimiliCondividi.variaNotiziaPerCatalog", "variaNotiziaPerCatalog");
		map.put("button.gestSimiliCondividi.tornaAnaliticaPerCondividi", "tornaAnaliticaPerCondividi");
		map.put("button.annulla", "indietro");

		map.put("button.gestLegamiEditTitoli.scegli", "gestioneLegamiEditTitoli");
		map.put("button.gestLegamiEditTitoli.cancella", "cancellaLegamiEditTitoli");
		map.put("button.gestLegamiEditTitoli.cercaEditore", "creaLegamiEditTitoli");

		// Evolutiva Copia reticolo Spoglio da legare a M nuova: Luglio 2015: almaviva2
		map.put("button.gestSimili.copiaSpoglio", "copiaReticoloSpoglioCreaLegame");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		// INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE
		// le verifiche possono essere effettuate solo dopo in quanto si potrebbe impostare la creazione di un Tit Uniforme o di
		// un documento nel passo successivo
		sinteticaTitoliForm.setCreaDoc("SI");
		sinteticaTitoliForm.setCreaDocLoc("SI");
		// FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */


		ActionForward forward = this.load(mapping, form, request, "NO");

		List<SinteticaTitoliView> listaSintetica = sinteticaTitoliForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			//selezione primo elemento
			SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
			if (!ValidazioneDati.isFilled(sinteticaTitoliForm.getSelezRadio())) {
				sinteticaTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
			} else {
				// Inizio Modifica almaviva2 2010.11.04 BUG MANTIS 3926
				// ricerca in Polo di un bid per titolo; trovo un bid Loc; si passa all'Analitica del bid trovato;
				// tasto Cerca in Indice; prospetta la sintetica di Indice di un altra notizia con la stessa chiave titolo;
				// seleziono Analitica e ricevo msg:
				//"L'oggetto XXXXX non è stato trovato sulla Base Dati. la ricerca effettuata non ha prodotto risultati"
				// L'errore si verifica perchè rimane in memoria la selezione del primo bid locale (infatti nella sintetica di Indice
				// non c'è nessuna selezione effettuata) che nella nuova sintetica non esiste;
				SinteticaTitoliView eleSinteticaTitoliView = null;
				boolean trovato = false;
				for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
					eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
					if (eleSinteticaTitoliView.getBid() == sinteticaTitoliForm.getSelezRadio()) {
						trovato = true;
						sinteticaTitoliForm.setSelezRadio(eleSinteticaTitoliView.getBid());
						break;
					}
				}
				if (!trovato) {
					sinteticaTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
				}
				// Fine Modifica almaviva2 2010.11.04 BUG MANTIS 3926
			}

			// Modifica almaviva2 10.03.2010 BUG 3577
			// (Nell'Esamnina Ordini si perde il puntamento alla notizia d'interesse nella lista sintetica (di Polo).)
			if (request.getAttribute("passaggioListaSuppOrdiniVO") != null) {
				String bidAppo = ((ListaSuppOrdiniVO)request.getAttribute("passaggioListaSuppOrdiniVO")).getTitolo().getCodice();
				sinteticaTitoliForm.setSelezRadio(bidAppo);
			}

		} else {

			// Inizio manutenzione 19.07.2011 BUG MANTIS esercizio 4575 (nel caso di troppi titoli trovati si deve inviare il diagnostico
			// corretto e non il generico non trovato)
//			ActionMessages errors = new ActionMessages();
//			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
//			saveErrors(request, errors);
			if (this.getErrors(request).isEmpty()) {

				// Giugno 2013: La richiesta è di avere nel menu una sola voce (Gestione legami) che in presenza di editori
				// già legati sulla sintetica mostri anche il tasto Cerca e in caso invece di assenza di legami si posizioni
				// direttamente sulla mappa di Ricerca editore
				if (sinteticaTitoliForm.getTipologiaTastiera().equals("UtilizzoPerGestioneLegamiEditTitoli")) {
					return forward;
				}

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
				saveErrors(request, errors);
			}
			return navi.goBack(true);
			// Fine manutenzione 19.07.2011 BUG MANTIS esercizio 4575
		}

		if (forward == null) {
			sinteticaTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, sinteticaTitoliForm);
			return mapping.getInputForward();
		}

		String path = forward.getPath();
		if (!path.endsWith(".do"))
			forward.setPath(path.replace(".", "/"));
		if (path.endsWith("analiticaTitolo.do")) {
			forward.setPath(path + "?RETICOLO=TRUE");
		}

		return forward;
	}

	private ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, String internCall) throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		sinteticaTitoliForm.setTipologiaTastiera("");
		sinteticaTitoliForm.setUtilizzoComeSif("NO");
		sinteticaTitoliForm.setProspettazioneSimili("NO");
		sinteticaTitoliForm.setProspettazionePerLegami("NO");
		sinteticaTitoliForm.setProspettaDatiOggColl("NO");
		sinteticaTitoliForm.setProspettazionePerServizi("NO");
		sinteticaTitoliForm.setProspettazionePerMovimentiUtente("NO");
		sinteticaTitoliForm.setProspettaSimiliPerCondividi("NO");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;

		if (request.getParameter("SERVIZI") != null) {
			sinteticaTitoliForm.setProspettazionePerServizi("SI");
			sinteticaTitoliForm.setTipologiaTastiera("ProspettazionePerServizi");
			// Giuseppe - inizio
			if (request.getAttribute("elencoBiblSelezionate") != null){
				sinteticaTitoliForm.setElencoBibliotecheSelezionate((String)request.getAttribute("elencoBiblSelezionate"));
			}
			// Giuseppe - fine
			//almaviva5_20101103 #3958
			List<BibliotecaVO> filtroBib = (List<BibliotecaVO>) request.getAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI);
			if (filtroBib != null)
				sinteticaTitoliForm.setFiltroBib(filtroBib);

			// INIZIO Evolutiva Novembre 2015 - almaviva2 gestione interrogazione da SERVIZI ILL
			if (request.getAttribute("areaDatiPassReturnSintetica") != null) {
				areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request.getAttribute("areaDatiPassReturnSintetica");
				if (areaDatiPassReturn.getNumNotizie() == 1) {
					SinteticaTitoliView sinteticaTitoliViewElem = new SinteticaTitoliView();
					sinteticaTitoliViewElem = (SinteticaTitoliView) areaDatiPassReturn.getListaSintetica().get(0);
				}
			}
			// FINE Evolutiva Novembre 2015 - almaviva2 gestione interrogazione da SERVIZI ILL
		}

		if (request.getParameter("MOVIMENTI_UTENTE") != null) {
			sinteticaTitoliForm.setProspettazionePerMovimentiUtente("SI");
			sinteticaTitoliForm.setTipologiaTastiera("ProspettazionePerMovimentiUtente");
			//almaviva5_20101103 #3958
			List<BibliotecaVO> filtroBib = (List<BibliotecaVO>) request.getAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI);
			if (filtroBib != null)
				sinteticaTitoliForm.setFiltroBib(filtroBib);
		}

		if (request.getParameter("ACQUISIZIONI") != null) {
			sinteticaTitoliForm.setTipologiaTastiera("ProspettazionePerAcquisizioni");
			sinteticaTitoliForm.setRitornoDaEsterna((String) request.getAttribute("ritornoDaEsterna"));
		}

		 // Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
		if (request.getParameter("TITCOLLEDITORI") != null) {
			sinteticaTitoliForm.setTipologiaTastiera("UtilizzoComeSif");
			Navigation.getInstance(request).setTesto("Sintetica Titoli Collegati ad Editore (Produzione editoriale)");
		}
		if (request.getParameter("EDITCOLLTITOLI") != null) {
			sinteticaTitoliForm.setTipologiaTastiera("UtilizzoPerGestioneLegamiEditTitoli");
			sinteticaTitoliForm.setProspettaDatiOggColl("SI");
			// OTTOBRE 2013 Bug mantis collaudo 5402: Da una analitica di Polo scelgo il menu altre funzioni --> gestione
			// legami titolo editore e, non essendoci editori legati, il sistema propone la mappa di ricerca editore;
			// sulla barra del percorso però è presente anche "sintetica editori collegati a titolo" e cliccando su questa
			// il sistema va in errore
//			Navigation.getInstance(request).setTesto("Sintetica Editori(Produzione editoriale) Collegati a Titolo ");
			if (request.getAttribute("bid") != null) {
				sinteticaTitoliForm.setIdOggColl((String) request.getAttribute("bid"));
			}
			if (request.getAttribute("desc") != null) {
				sinteticaTitoliForm.setDescOggColl((String) request.getAttribute("desc"));
			}

			AreaDatiPassaggioInterrogazioneTitoloReturnVO titoloReturnVO = creaListaEditCollTit(request, sinteticaTitoliForm);

			// Giugno 2013: La richiesta è di avere nel menu una sola voce (Gestione legami) che in presenza di editori
			// già legati sulla sintetica mostri anche il tasto Cerca e in caso invece di assenza di legami si posizioni
			// direttamente sulla mappa di Ricerca editore
			if (request.getAttribute("gestLegameTitEdit") != null &&  request.getAttribute("gestLegameTitEdit").equals("SI")) {
				if (titoloReturnVO.getCodErr().equals("3001")) {
					request.setAttribute("creazLegameTitEdit", "SI");
					request.setAttribute("bid", sinteticaTitoliForm.getIdOggColl());
					request.setAttribute("desc", sinteticaTitoliForm.getDescOggColl());
					// OTTOBRE 2013 Bug mantis collaudo 5402: Per legame titolo/editore non trovato si richiama la funzione di ricerca
					// per creazione e si deve cancellare dalla riga di Navigazione la voce SinteticaTitoli
					Navigation.getInstance(request).purgeThis();
					return mapping.findForward("creazLegameTitEdit");
				} else {
					Navigation.getInstance(request).setTesto("Sintetica Editori(Produzione editoriale) Collegati a Titolo ");
				}

			}
		}

		if (request.getParameter("SINTSIMILICONDIVIDI") != null) {
			sinteticaTitoliForm.setProspettaSimiliPerCondividi("SI");
			if (request.getAttribute("bidRoot") != null && request.getAttribute("bidRoot").equals("NO")) {
				sinteticaTitoliForm.setTipologiaTastiera("ProspettaSimiliPerCondividiNoRadice");
			} else {
				sinteticaTitoliForm.setTipologiaTastiera("ProspettaSimiliPerCondividi");
			}

			if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
				sinteticaTitoliForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			}

			sinteticaTitoliForm.setLivRicerca("I");

			if (request.getAttribute("areaDatiPassPerConfVariazione") != null){
				sinteticaTitoliForm.setAreaDatiPassPerConfVariazione((AreaDatiVariazioneTitoloVO) request.getAttribute("areaDatiPassPerConfVariazione"));
				sinteticaTitoliForm.setProspettaDatiOggColl("SI");
				sinteticaTitoliForm.setIdOggColl(sinteticaTitoliForm
						.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO().getBid());
				sinteticaTitoliForm.setDescOggColl(sinteticaTitoliForm
						.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO().getAreaTitTitolo());
				if (request.getAttribute("areaDatiPassReturnSintetica") != null) {
					areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request.getAttribute("areaDatiPassReturnSintetica");
					if (areaDatiPassReturn.getNumNotizie() == 1) {
						SinteticaTitoliView sinteticaTitoliViewElem = new SinteticaTitoliView();
						sinteticaTitoliViewElem = (SinteticaTitoliView) areaDatiPassReturn.getListaSintetica().get(0);
						if (sinteticaTitoliForm.getIdOggColl().equals(sinteticaTitoliViewElem.getBid())) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricNotiziaGiaPresenteInIndice", sinteticaTitoliForm.getIdOggColl()));
							this.saveErrors(request, errors);
						}
					}
				}
			}
		}


		if (request.getParameter("SIFSINTETICA") != null || internCall.equals("SI")) {
			sinteticaTitoliForm.setProspettaDatiOggColl("SI");
			sinteticaTitoliForm.setUtilizzoComeSif("SI");
			sinteticaTitoliForm.setTipologiaTastiera("UtilizzoComeSif");
			ActionForward forward = this.gestioneModalitaSif(request, sinteticaTitoliForm, internCall);
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
			sinteticaTitoliForm.setProspettaDatiOggColl("SI");
			sinteticaTitoliForm.setProspettazioneSimili("SI");
			sinteticaTitoliForm.setTipologiaTastiera("ProspettazioneSimili");
			sinteticaTitoliForm
					.setAreaDatiPassPerConfVariazione((AreaDatiVariazioneTitoloVO) request.getAttribute("areaDatiPassPerConfVariazione"));

			if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
				sinteticaTitoliForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));


				// Inizio modifica almaviva2 BUG MANTIS 4131 (Collaudo) si aggiunge nell'if il controllo sulle W perchè negli
				// altri casi la cattura è possibile

				if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("51")) {
					if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("W")) {
						sinteticaTitoliForm.setTipologiaTastiera("ProspettazioneSimiliSenzaCattura");
					} else if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N")){
						sinteticaTitoliForm.setTipologiaTastiera("ProspettazioneSimiliSenzaCattura");
					} else {
						sinteticaTitoliForm.setTipologiaTastiera("ProspettazioneSimiliPerCattura51");
					}
				}
				// Fine modifica almaviva2 BUG MANTIS 4131 (Collaudo)
			}
			sinteticaTitoliForm.setIdOggColl(sinteticaTitoliForm
					.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO().getBid());
			sinteticaTitoliForm.setDescOggColl(sinteticaTitoliForm
					.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO().getAreaTitTitolo());
			mapping.getInputForward();
		}

		if (request.getParameter("SINTNEWLEGAME") != null) {
			sinteticaTitoliForm.setProspettazionePerLegami("SI");
			sinteticaTitoliForm.setTipologiaTastiera("ProspettazionePerLegami");

			sinteticaTitoliForm
					.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request
							.getAttribute("AreaDatiLegameTitoloVO"));
			sinteticaTitoliForm.setProspettaDatiOggColl("SI");
			sinteticaTitoliForm.setIdOggColl(sinteticaTitoliForm
					.getAreaDatiLegameTitoloVO().getBidPartenza());
			sinteticaTitoliForm.setDescOggColl(sinteticaTitoliForm
					.getAreaDatiLegameTitoloVO().getDescPartenza());

			//(Inizio 5Agosto2009)
			if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
				sinteticaTitoliForm.setTipologiaTastiera("ProspettazionePerFusioneOnLine");
			}

			// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
			// Modifica Settembre 2015 si deve controllare il null sul campo getNaturaBidArrivo()
			if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M")
					&& (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo() != null &&
							sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N"))) {
				sinteticaTitoliForm.setTipologiaTastiera("ProspettazioneSimiliPerCopiaSpoglio");
			}


		}

		// Modifica almaviva2 del 07.12.2010 BUG MANTIS 4045: intervento per evitare che compaia
		// il tasto Cerca in Indice e crea in locale quando siamo in sintetica arrivando da ID Gestionali
		// (SinteticaTitoliAction - load e inserito nuovo controllo su sinteticaTitoli.jsp)
		if (request.getAttribute("areaDatiPassPerInterrogazione") != null) {
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassDaIdGestionali = (AreaDatiPassaggioInterrogazioneTitoloVO) request.getAttribute("areaDatiPassPerInterrogazione");
			if (areaDatiPassDaIdGestionali.getRicercaPerGest() != null
					&& areaDatiPassDaIdGestionali.getRicercaPerGest().equals("SI")) {
				sinteticaTitoliForm.setTipologiaTastiera("ProspettazionePerIdGestionali");
			}
		}

		if (request.getAttribute("areaDatiPassReturnSintetica") == null) {
//			 QUESTO INTERVENTO DOVREBBE NON FAR PROSPETTARE IL TASTO CERCA IN INDICE QUANDO NON SONO
			// DISPONIBILI I DATI PER EFFETTUARLA (SI PRESUME PERCHE' E' IL RIENTRO DA SIF)
			if (sinteticaTitoliForm.getDatiInterrTitolo() != null) {
				if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen() != null) {
					if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoOrdinamSelez() == null) {
						sinteticaTitoliForm.setTipologiaTastiera("UtilizzoComeSif");
// 						Inizio intervento almaviva2 23.11.2009 - BUG Mantis 3342 -
						sinteticaTitoliForm.setUtilizzoComeSif("SI");
//						Fine intervento almaviva2 23.11.2009 - BUG Mantis 3342 -
					}
				}
			}

			//almaviva5_20100625 #3577
			if (request.getParameter(SinteticaLocalizzazioniAction.SINTETICA_LOC_RETURN) != null)
				return null;

			if (request.getAttribute("passaggioListaSuppOrdiniVO") == null) {
				ActionMessages errors = new ActionMessages();
				String testoErr = request.getParameter("TESTOERR");
				if (testoErr != null) {
					//errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo", testoErr));
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo", testoErr));
				} else {
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
				}
				this.saveErrors(request, errors);
				return null;
			} else {
				return null;
			}
		}

		areaDatiPassReturn = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
				.getAttribute("areaDatiPassReturnSintetica");
		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaTitoliForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaTitoliForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaTitoliForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaTitoliForm.setNumNotizie(areaDatiPassReturn.getNumNotizie());
		sinteticaTitoliForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaTitoliForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaTitoliForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());
		sinteticaTitoliForm.setListaSintetica(areaDatiPassReturn
				.getListaSintetica());

		sinteticaTitoliForm
				.setDatiInterrTitolo((AreaDatiPassaggioInterrogazioneTitoloVO) request
						.getAttribute("areaDatiPassPerInterrogazione"));

		// Inizio modifica - almaviva2 16.11.2009 BUG MANTIS 3333 . I dati impostati in interrogazione devono essere riportati nella Sintetica
		// per poter essere utilizati al momento della pressione del tasto "caricamento schermate successive";
		if (sinteticaTitoliForm.getDatiInterrTitolo() != null && sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen() != null) {
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoOrdinamSelez() != null) {
				sinteticaTitoliForm.setTipoOrd(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoOrdinamSelez());
			}
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getFormatoListaSelez() != null) {
				sinteticaTitoliForm.setTipoOutput(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getFormatoListaSelez());
			}
		}

		// Fine modifica - almaviva2 16.11.2009 BUG MANTIS 3333


		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaTitoliForm.setAppoggio(appoggio);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaTitoliForm);

		return null;
	}

	private ActionForward gestioneModalitaSif(HttpServletRequest request,
			SinteticaTitoliForm sinteticaTitoliForm, String internCall)
			throws Exception {

		AreePassaggioSifVO areePassSifVo = sinteticaTitoliForm.getAreePassSifVo();
		if (!internCall.equals("SI")) {
			if (request.getAttribute("AreePassaggioSifVO") != null) {
				sinteticaTitoliForm.setAreePassSifVo((AreePassaggioSifVO) request.getAttribute("AreePassaggioSifVO"));
			}
			areePassSifVo.setLivelloRicerca(((Integer) (request.getAttribute(TitoliCollegatiInvoke.livDiRicerca))).intValue());
			areePassSifVo.setOggettoDaRicercare((String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			areePassSifVo.setDescOggettoDaRicercare((String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));
			areePassSifVo.setOggettoRicerca(((Integer) (request.getAttribute(TitoliCollegatiInvoke.oggDiRicerca))).intValue());
			areePassSifVo.setOggettoChiamante((String) request.getAttribute(TitoliCollegatiInvoke.oggChiamante));
			areePassSifVo.setCodBiblioteca((String) request.getAttribute(TitoliCollegatiInvoke.codBiblio));
			// almaviva2 28 ottobre 2009 BUG 3287 inizio intervento
			if (request.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca) != null) {
				areePassSifVo.setNaturaOggetto((String) request.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));
			}
			if (request.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca) != null) {
				areePassSifVo.setTipMatOggetto((String) request.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));
			}
			// almaviva2 28 ottobre 2009 BUG 3287 fine intervento
			if (((String) request.getAttribute(TitoliCollegatiInvoke.visualCall)).equals("SI")) {
				areePassSifVo.setVisualCall(true);
			} else {
				areePassSifVo.setVisualCall(false);
			}
		}

		sinteticaTitoliForm.setIdOggColl(areePassSifVo.getOggettoDaRicercare());
		sinteticaTitoliForm.setDescOggColl(areePassSifVo.getDescOggettoDaRicercare());

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
		areaDatiPass.setOggChiamante(TitoliCollegatiInvoke.ANALITICA_DETTAGLIO);
		areaDatiPass.setTipoOggetto(areePassSifVo.getOggettoRicerca());
		areaDatiPass.setTipoOggettoFiltrato(99);
		if (areePassSifVo.getNaturaOggetto() == null) {
			areaDatiPass.setNaturaTitBase("");
		} else {
			areaDatiPass.setNaturaTitBase(areePassSifVo.getNaturaOggetto());
		}

		if (areePassSifVo.getTipMatOggetto() == null) {
			areaDatiPass.setTipMatTitBase("");
		} else {
			if (areePassSifVo.getTipMatOggetto()
					.equals("musica")) {
				areaDatiPass.setTipMatTitBase("U");
			} else {
				areaDatiPass.setTipMatTitBase("");
			}
		}
		areaDatiPass.setCodiceLegame("");
		areaDatiPass.setCodiceSici("");

		areaDatiPass.setOggDiRicerca(areePassSifVo.getOggettoDaRicercare());
		// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		areaDatiPass.clear();

		areaDatiPass.setRicercaIndice(areePassSifVo.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areaDatiPass.setRicercaPolo(areePassSifVo.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

		//almaviva5_20091007 Inserimento filtro per Biblioteca
		if (areePassSifVo.getLivelloRicerca() == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO) {
			areaDatiPass.setRicercaIndice(false);
			areaDatiPass.setRicercaPolo(true);
			BibliotecaVO bib = new BibliotecaVO();
			UserVO utente = Navigation.getInstance(request).getUtente();
			bib.setCod_polo(utente.getCodPolo());
			bib.setCod_bib((String) request.getAttribute(TitoliCollegatiInvoke.codBiblio) );
			areaDatiPass.getFiltroLocBib().add(bib);
		}

		if (sinteticaTitoliForm.getDatiInterrTitolo() != null) {
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen() != null) {
				Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getElemXBlocchi() == 0) {
					try {
						areaDatiPass.setElemXBlocchi(Integer.parseInt((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_ELEMENTI_BLOCCHI)));
					} catch (DefaultNotFoundException e) {
						areaDatiPass.setElemXBlocchi(10);
					}
				} else {
					areaDatiPass.setElemXBlocchi(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getElemXBlocchi());
				}

				if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getFormatoListaSelez() == null) {
					try {
						areaDatiPass.setFormatoListaSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_FORMATO_LISTA));
					} catch (DefaultNotFoundException e) {
						areaDatiPass.setFormatoListaSelez(null);
					}
				} else {
					areaDatiPass.setFormatoListaSelez(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getFormatoListaSelez());
				}

				if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoOrdinamSelez() == null) {
					try {
						areaDatiPass.setTipoOrdinamSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_ORDINAMENTO));
					} catch (DefaultNotFoundException e) {
						areaDatiPass.setTipoOrdinamSelez(null);
					}
				} else {
					areaDatiPass.setTipoOrdinamSelez(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoOrdinamSelez());
				}
			}
		}


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().ricercaTitoli(areaDatiPass, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			ActionForward actionForward = new ActionForward();
			String temp = areePassSifVo.getOggettoChiamante();
			if (!temp.substring(temp.length() - 3).equals(".do")) {
				actionForward.setPath(areePassSifVo.getOggettoChiamante() + ".do");
			} else {
				actionForward.setPath(areePassSifVo.getOggettoChiamante());
			}

			String path = actionForward.getPath();
			if (!path.endsWith(".do")) {
				actionForward.setPath(path.replace(".", "/"));
			}
			actionForward.setRedirect(true);
			return new NavigationForward(actionForward, true);
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			ActionForward actionForward = new ActionForward();
			String temp = areePassSifVo.getOggettoChiamante();
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
			return new NavigationForward(actionForward, true);

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			ActionForward actionForward = new ActionForward();
			String temp = areePassSifVo
					.getOggettoChiamante();
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
			return new NavigationForward(actionForward, true);
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			ActionForward actionForward = new ActionForward();
			String temp = areePassSifVo
					.getOggettoChiamante();
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
			return new NavigationForward(actionForward, true);
		}

		if (!areePassSifVo.isVisualCall()) {
			request.setAttribute(TitoliCollegatiInvoke.arrayListSintetica,
					areaDatiPassReturn.getListaSintetica());
			ActionForward actionForward = new ActionForward();
			actionForward.setPath(areePassSifVo
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
		sinteticaTitoliForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaTitoliForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaTitoliForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaTitoliForm.setNumNotizie(areaDatiPassReturn.getNumNotizie());
		sinteticaTitoliForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaTitoliForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaTitoliForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());
		sinteticaTitoliForm.setListaSintetica(areaDatiPassReturn
				.getListaSintetica());
		return null;
	}

	public ActionForward carBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getNumBlocco() == 0
			|| sinteticaTitoliForm.getNumBlocco() > sinteticaTitoliForm.getTotBlocchi()) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		HashSet<Integer> appoggio = sinteticaTitoliForm.getAppoggio();
		int i = sinteticaTitoliForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(sinteticaTitoliForm.getNumBlocco());
		areaDatiPass.setMaxRighe(sinteticaTitoliForm.getMaxRighe());
		areaDatiPass.setIdLista(sinteticaTitoliForm.getIdLista());
		areaDatiPass.setTipoOrdinam(sinteticaTitoliForm.getTipoOrd());
		areaDatiPass.setTipoOutput(sinteticaTitoliForm.getTipoOutput());
		// Modifica - almaviva2 18.01.2010 BUG MANTIS 3494 . Si Imposta a true  il valore per la prospettazione del num.Sequenza
		// anche nel caso di richiesta di sintetica per collegamento
		if (sinteticaTitoliForm.getProspettaDatiOggColl().equals("SI")) {
			areaDatiPass.setPresenzaSequenzaLegame(true);
		}

		if (sinteticaTitoliForm.getLivRicerca().equals("I")) {
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(true);
		} else {
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setRicercaIndice(false);
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().getNextBloccoTitoli(areaDatiPass, Navigation.getInstance(request).getUserTicket());

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
					"errors.gestioneBibliografica.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		String idLista = areaDatiPassReturn.getIdLista();
		sinteticaTitoliForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaTitoliForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = sinteticaTitoliForm.getNumBlocco();
		appoggio = sinteticaTitoliForm.getAppoggio();
		appoggio.add(numBlocco);
		sinteticaTitoliForm.setAppoggio(appoggio);

		List<SinteticaTitoliView> listaSinteticaOriginale = sinteticaTitoliForm
				.getListaSintetica();
		listaSinteticaOriginale.addAll(areaDatiPassReturn.getListaSintetica());
		Collections.sort(listaSinteticaOriginale,
				SinteticaTitoliView.ORDINA_PER_PROGRESSIVO);

		sinteticaTitoliForm.setListaSintetica(listaSinteticaOriginale);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaTitoliForm);
		return mapping.getInputForward();
	}

	public ActionForward analiticaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getTipologiaTastiera().equals("UtilizzoPerGestioneLegamiEditTitoli")) {
			return gestioneLegamiEditTitoli(mapping,sinteticaTitoliForm,request, response);
		}

		if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO() == null) {
			request.setAttribute("presenzaTastoVaiA", "SI");
		} else {
			if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null
						|| sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals("")) {
				request.setAttribute("presenzaTastoVaiA", "SI");
			} else {
				request.setAttribute("presenzaTastoVaiA", "NO");
			}
		}

		Integer progressivo = 0;
		if (sinteticaTitoliForm.getLinkProgressivo() != null) {
			progressivo = sinteticaTitoliForm.getLinkProgressivo();
			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getProgressivo() == progressivo) {
					sinteticaTitoliForm.setSelezRadio(eleSinteticaTitoliView.getBid());
					break;
				}
			}
		}

		// Mofifica almaviva2 BUG MANTIS 3383 - inserito controllo per selezione di un solo check si invia solo il bid
		// e non la tabella;
		// Inizio Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione
		// così da inviarla correttamente
		String[] listaBidSelezOld;
		int cont = -1;
		if (sinteticaTitoliForm.getSelezCheck() != null	&& sinteticaTitoliForm.getSelezCheck().length > 0) {
			int length = sinteticaTitoliForm.getSelezCheck().length;
			listaBidSelezOld = new String[length];
			for (int i = 0; i < length; i++) {
				if (sinteticaTitoliForm.getSelezCheck()[i] != null && sinteticaTitoliForm.getSelezCheck()[i].length()> 0) {
					listaBidSelezOld[++cont] = sinteticaTitoliForm.getSelezCheck()[i];
				}
			}

			if (cont <0) {
				if (sinteticaTitoliForm.getSelezRadio() != null
						&& !sinteticaTitoliForm.getSelezRadio().equals("")) {
					// GIUGNO 2018 - Manutenzione interna perchè mancava questo controllo se la richiesta di Analitica
					// viene fatta dal tasto Analitica e non (come sotto) dal link del bid
					// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
	                // con tipo legame 431 (A08V)
					// Se il bid selezionato in Sintetica è un Rinvio di un Titolo Uniforme (natura V) si deve
					// interrogare il titoloUniforme di base
					if (!getBidDaInterrogareCorretto(sinteticaTitoliForm)) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selNaturaAnonV"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}

					String keyDesc = sinteticaTitoliForm.getSelezRadio();
					request.setAttribute("bid", keyDesc);
					request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaTitoliForm.getDatiInterrTitolo());
					resetToken(request);
					if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
					} else if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
							request.setAttribute("livRicerca", "P");
							request.setAttribute("presenzaTastoVaiA", "NO");
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
							request.setAttribute("utilizzaComeSIF", "SERVIZI");
					} else if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettaSimiliPerCondividi")) {
						request.setAttribute("presenzaTastoVaiA", "NO");
					}else if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerIdGestionali")) {

						// Modifica almaviva2 del 01.03.2011 BUG MANTIS 4045: intervento per evitare che compaia
						// il tasto Cerca in Indice quando si richiede l'analitica arrivando da ID Gestionali
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}

					if (sinteticaTitoliForm.getUtilizzoComeSif() != null && sinteticaTitoliForm.getLivRicerca() != null) {
						if (sinteticaTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaTitoliForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			} else if (cont == 0) {
				request.setAttribute("bid", listaBidSelezOld[0]);
				request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaTitoliForm.getDatiInterrTitolo());
				resetToken(request);
				if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
					request.setAttribute("livRicerca", "P");
					request.setAttribute("presenzaTastoVaiA", "NO");
					request.setAttribute("presenzaTastoCercaInIndice", "NO");
					request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
				}
				if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
					request.setAttribute("livRicerca", "P");
					request.setAttribute("presenzaTastoVaiA", "NO");
					request.setAttribute("presenzaTastoCercaInIndice", "NO");
					request.setAttribute("utilizzaComeSIF", "SERVIZI");
					//almaviva5_20110107
					request.setAttribute("elencoBiblSelezionate", sinteticaTitoliForm.getElencoBibliotecheSelezionate());
					request.setAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI, sinteticaTitoliForm.getFiltroBib());
				}

				if (sinteticaTitoliForm.getUtilizzoComeSif() != null && sinteticaTitoliForm.getLivRicerca() != null) {
					if (sinteticaTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaTitoliForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));

			} else {
//				String[] listaBidSelez = sinteticaTitoliForm.getSelezCheck();
				String[] listaBidSelez = listaBidSelezOld;
				if (listaBidSelez[0] != null) {
					request.setAttribute("bid", listaBidSelez[0]);
					request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaTitoliForm.getDatiInterrTitolo());
					request.setAttribute("listaBidSelez", listaBidSelez);
					resetToken(request);
					if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
					}
					if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "SERVIZI");
					}

					if (sinteticaTitoliForm.getUtilizzoComeSif() != null && sinteticaTitoliForm.getLivRicerca() != null) {
						if (sinteticaTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaTitoliForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			}
		} else {
			// Modifica almaviva2 29.07.2010 - BUG MANTIS 3856 inserito controllo per voce "ProspettazionePerMovimentiUtente"
			if (sinteticaTitoliForm.getSelezRadio() != null
					&& !sinteticaTitoliForm.getSelezRadio().equals("")) {

				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
                // con tipo legame 431 (A08V)
				// Se il bid selezionato in Sintetica è un Rinvio di un Titolo Uniforme (natura V) si deve
				// interrogare il titoloUniforme di base
				if (!getBidDaInterrogareCorretto(sinteticaTitoliForm)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selNaturaAnonV"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}


				String keyDesc = sinteticaTitoliForm.getSelezRadio();
				request.setAttribute("bid", keyDesc);
				request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaTitoliForm.getDatiInterrTitolo());
				resetToken(request);
				if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
					request.setAttribute("livRicerca", "P");
					request.setAttribute("presenzaTastoVaiA", "NO");
					request.setAttribute("presenzaTastoCercaInIndice", "NO");
					request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
				} else if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")
						|| sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazionePerMovimentiUtente")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "SERVIZI");
						//almaviva5_20110107
						request.setAttribute("elencoBiblSelezionate", sinteticaTitoliForm.getElencoBibliotecheSelezionate());
						request.setAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI, sinteticaTitoliForm.getFiltroBib());

				} else if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettaSimiliPerCondividi")) {
					request.setAttribute("presenzaTastoVaiA", "NO");
				}

				if (sinteticaTitoliForm.getUtilizzoComeSif() != null && sinteticaTitoliForm.getLivRicerca() != null) {
					if (sinteticaTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaTitoliForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
			}
		}
		// Fine Seconda Modifica almaviva2 BUG MANTIS 3383 - inserito l'Hidden nelle mappe; ora dobbiamo compattare la selezione

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}

	public ActionForward stampaSchede(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.funzSoloViaBatch"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}


	public ActionForward salvaSuFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getSelezCheck() != null	&& sinteticaTitoliForm.getSelezCheck().length > 0) {
			String[] listaBidSelez = sinteticaTitoliForm.getSelezCheck();

			//almaviva5_20101109 #3977
			List<String> listaBid = new ArrayList<String>(listaBidSelez.length);

			for (String bid : listaBidSelez) {
				bid = ValidazioneDati.trimOrEmpty(bid);
				if (!ValidazioneDati.leggiXID(bid))
					continue;
				else
					listaBid.add(bid);
			}

			if (!ValidazioneDati.isFilled(listaBid)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
				return mapping.getInputForward();
			}

			StringBuilder buf = new StringBuilder(11 * listaBid.size());
			for (String bid : listaBid)
				buf.append(bid).append('\n');

			byte[] bytes = buf.toString().getBytes();
			return SbnDownloadAction.downloadFile(request, "ElencoIdentif_Data" + ".txt", bytes);

		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}


	public ActionForward exportUnimarc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		Integer progressivo = 0;

		request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());

		if (sinteticaTitoliForm.getLinkProgressivo() != null) {
			progressivo = sinteticaTitoliForm.getLinkProgressivo();
			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getProgressivo() == progressivo) {
					sinteticaTitoliForm.setSelezRadio(eleSinteticaTitoliView.getBid());
					break;
				}
			}
		}

		if (sinteticaTitoliForm.getSelezCheck() != null	&& sinteticaTitoliForm.getSelezCheck().length > 0) {
			String[] listaBidSelez = sinteticaTitoliForm.getSelezCheck();
			if (listaBidSelez[0] != null) {
				for (int i = 0; i < listaBidSelez.length; i++) {
					request.setAttribute("bid", listaBidSelez[0]);
					request.setAttribute("listaBidSelez", listaBidSelez);
				}
				resetToken(request);

				//almaviva5_20090928 #3201 chiamata export
				return prenotaExportUnimarc(request, mapping, listaBidSelez);
			}
		}

		if (sinteticaTitoliForm.getSelezRadio() != null
				&& !sinteticaTitoliForm.getSelezRadio().equals("")) {

			String[] listaBidSelez = new String[1];
			request.setAttribute("bid", sinteticaTitoliForm.getSelezRadio());

			listaBidSelez[0] = sinteticaTitoliForm.getSelezRadio();
			request.setAttribute("listaBidSelez", listaBidSelez);

			resetToken(request);

			//almaviva5_20090928 #3201 chiamata export
			return prenotaExportUnimarc(request, mapping, listaBidSelez);
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	//almaviva5_20090928 #3201
	private ActionForward prenotaExportUnimarc(HttpServletRequest request, ActionMapping mapping, String[] listaBidSelez)
		throws Exception {

		EsportaVO esportaVO = new EsportaVO();

		//almaviva5_20101109 #3977
		List<String> listaBid = new ArrayList<String>(listaBidSelez.length);

		for (String bid : listaBidSelez) {
			bid = ValidazioneDati.trimOrEmpty(bid);
			if (!ValidazioneDati.leggiXID(bid))
				continue;
			else
				listaBid.add(bid);
		}

		if (!ValidazioneDati.isFilled(listaBid)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			return mapping.getInputForward();
		}

		esportaVO.setListaBID(listaBid);
		//

		esportaVO.setTipoEstrazione(TipoEstrazioneUnimarc.FILE);

		// Prova export
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		esportaVO.setCodPolo(utenteCollegato.getCodPolo());
		esportaVO.setCodBib(utenteCollegato.getCodBib());
		esportaVO.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040);
		esportaVO.setUser(utenteCollegato.getUserId());

		String ticket = utenteCollegato.getTicket();
		esportaVO.setTicket(ticket);

		//almaviva5_20130506
		esportaVO.setCodScaricoSelez("ALL");
		ExportUnimarcBatch.impostaEtichetteDaEsportare(esportaVO);

		String idBatch = null;
		try {
			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, esportaVO, null);
			if (idBatch != null)
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreGenerico"));

			return mapping.getInputForward();

		} catch (ValidationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreGenerico"));
			return mapping.getInputForward();
		}

	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati
		// nell'Interrogazione
		sinteticaTitoliForm.getDatiInterrTitolo().setRicercaPolo(false);
		sinteticaTitoliForm.getDatiInterrTitolo().setRicercaIndice(true);
		// INIZIO MODIFICA RELATIVA AL CERCA IN INDICE QUANDO SI ARRIVA ALLA SINTETICA TITOLI DA ALTRA SINTETICA;
		//***************************************************************
		//***************************************************************
		// **************************ATTENZIONE *************************
		// QUESTA MANUTENZIONE VIENE ELIMINATA PERCHE' ALTRIMENTI IL CERCA IN INDICE A SEGUITO DI UN ESAMINA NON FUNZIONA MAI
//		if (sinteticaTitoliForm.getAreePassSifVo().getOggettoDaRicercare() != null) {
//			sinteticaTitoliForm.getAreePassSifVo().setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
//			ActionForward forward = this.gestioneModalitaSif(request, sinteticaTitoliForm, "SI");
//			if (forward == null)
//				return mapping.getInputForward();
//			return
//				forward;
//		}
		//***************************************************************
		//***************************************************************
		// FINE MODIFICA RELATIVA AL CERCA IN INDICE QUANDO SI ARRIVA ALLA SINTETICA TITOLI DA ALTRA SINTETICA;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaTitoli(
				sinteticaTitoliForm.getDatiInterrTitolo(), Navigation.getInstance(request).getUserTicket());

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
		sinteticaTitoliForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		sinteticaTitoliForm.setMaxRighe(areaDatiPassReturn.getMaxRighe());
		sinteticaTitoliForm.setNumBlocco(areaDatiPassReturn.getNumBlocco());
		sinteticaTitoliForm.setNumNotizie(areaDatiPassReturn.getNumNotizie());
		sinteticaTitoliForm.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
		sinteticaTitoliForm.setTotRighe(areaDatiPassReturn.getTotRighe());
		sinteticaTitoliForm.setLivRicerca(areaDatiPassReturn
				.getLivelloTrovato());

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		sinteticaTitoliForm.setAppoggio(appoggio);

		List<SinteticaTitoliView> listaSinteticaOriginale = areaDatiPassReturn
				.getListaSintetica();
		sinteticaTitoliForm.setListaSintetica(listaSinteticaOriginale);

		// almaviva2 01.12.2009 BUG MANTIS 3385  inserito puntamento fisso al primo elemento
		if (ValidazioneDati.isFilled(listaSinteticaOriginale)) {
			//selezione primo elemento
			SinteticaTitoliView sinteticaTitoliView = listaSinteticaOriginale.get(0);
			sinteticaTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
		}



		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaTitoliForm);
		return mapping.getInputForward();

	}

	public ActionForward creaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getProspettazionePerLegami().equals("NO")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("") ||
					sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez());
			}
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da());
			}

			// inserita la preimpostazione anche della natura come in Interrogazione
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691


			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			resetToken(request);
			return mapping.findForward("creaTitoloPartecipato");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setDescArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaTitoliForm.getAreaDatiLegameTitoloVO());

			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("") ||
					sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez());
			}
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da());
			}
			// inserita la preimpostazione anche della natura come in Interrogazione
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			resetToken(request);
			return mapping.findForward("creaTitoloPartecipato");
		}
	}

	public ActionForward creaTitLoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getProspettazionePerLegami().equals("NO")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("") ||
					sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez());
			}
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da());
			}
			// inserita la preimpostazione anche della natura come in Interrogazione
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			resetToken(request);
			return mapping.findForward("creaTitoloLocale");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setDescArrivo("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaTitoliForm.getAreaDatiLegameTitoloVO());

			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			if (sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("") ||
					sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoMateriale());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTipoDataSelez());
			}
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getData1Da());
			}
			// inserita la preimpostazione anche della natura come in Interrogazione
			if (!sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getNaturaSelez1());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", sinteticaTitoliForm.getDatiInterrTitolo().getInterTitGen().getTitolo());
			resetToken(request);
			return mapping.findForward("creaTitoloLocale");
		}
	}


	public ActionForward confermaGestisci (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		// Mappatura dei codici della combo di Gestisci:
		//		"01"-->"Export Unimarc"
		//		"02"-->"Stampa schede catalografiche"
		//		"03"-->"Salva identificativi su file"

		if (sinteticaTitoliForm.getGestisciTitSelez().equals("00")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblLisEsamina"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (sinteticaTitoliForm.getGestisciTitSelez().equals("01")) {
			return this.exportUnimarc( mapping, sinteticaTitoliForm, request,  response);
		} else if (sinteticaTitoliForm.getGestisciTitSelez().equals("02")) {
			return this.stampaSchede( mapping, sinteticaTitoliForm, request,  response);
		} else if (sinteticaTitoliForm.getGestisciTitSelez().equals("03")) {
			return this.salvaSuFile( mapping, sinteticaTitoliForm, request,  response);
		}

		return mapping.getInputForward();

	}




	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;
		if (sinteticaTitoliForm.isClone())
			sinteticaTitoliForm.setNumBlocco(1);

		if (Navigation.getInstance(request).isFromBar()	|| request.getParameter("SIFSINTETICA") != null) {
			if (sinteticaTitoliForm.getAreePassSifVo().getOggettoDaRicercare() != null) {
				if (!sinteticaTitoliForm.getAreePassSifVo().getOggettoDaRicercare().equals("")) {
					if (!sinteticaTitoliForm.getAreePassSifVo().getOggettoDaRicercare().equals("0")) {
						sinteticaTitoliForm.setIdOggColl(sinteticaTitoliForm.getAreePassSifVo().getOggettoDaRicercare());
						sinteticaTitoliForm.setDescOggColl(sinteticaTitoliForm.getAreePassSifVo().getDescOggettoDaRicercare());
					}
				}
			}
			// inizio Dicembre 2015 almaviva2 - Manutenzione Interna per selezionare il primo elemento quando si prospetta
			// una Sintetica titoli collegati
			List<SinteticaTitoliView> listaSintetica = sinteticaTitoliForm.getListaSintetica();
			if (ValidazioneDati.isFilled(listaSintetica)) {
				SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
				sinteticaTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
			}
			// fine Dicembre 2015 almaviva2 - Manutenzione Interna

			return mapping.getInputForward();
		}

		if (sinteticaTitoliForm.getSelezRadio() != null) {
			if (sinteticaTitoliForm.getEsaminaTitSelez().length() > 0) {

				String keyDesc = sinteticaTitoliForm.getSelezRadio();

				SinteticaTitoliView eleSinteticaTitoliView = null;
				String bid = keyDesc;
				String desDaRic = "";
				for (int i = 0; i < sinteticaTitoliForm.getListaSintetica()
						.size(); i++) {
					eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
							.getListaSintetica().get(i);
					if (eleSinteticaTitoliView.getBid().equals(bid)) {
						sinteticaTitoliForm.getAreePassSifVo()
								.setNaturaOggetto(eleSinteticaTitoliView.getNatura());
						if (eleSinteticaTitoliView.getTipoMateriale().equals("musica")) {
							sinteticaTitoliForm.getAreePassSifVo()
									.setTipMatOggetto("U");
						} else {
							sinteticaTitoliForm.getAreePassSifVo()
									.setTipMatOggetto("");
						}
						desDaRic = eleSinteticaTitoliView.getTitolo();
						break;
					}
				}

				String myForwardName = sinteticaTitoliForm.getTabellaEsaminaVO().getMyForwardName(
						sinteticaTitoliForm.getMyPath(), sinteticaTitoliForm
								.getEsaminaTitSelez());
				String myForwardPath = sinteticaTitoliForm.getTabellaEsaminaVO().getMyForwardPath(
						sinteticaTitoliForm.getMyPath(), sinteticaTitoliForm
								.getEsaminaTitSelez());

				if (myForwardPath.equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				String myModeCall = sinteticaTitoliForm.getTabellaEsaminaVO().getMyModeCall(sinteticaTitoliForm
						.getMyPath(), sinteticaTitoliForm.getEsaminaTitSelez());
				String myAction = sinteticaTitoliForm.getTabellaEsaminaVO().getMyAction(sinteticaTitoliForm
						.getMyPath(), sinteticaTitoliForm.getEsaminaTitSelez());

				if (sinteticaTitoliForm.getLivRicerca().equals("B")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					sinteticaTitoliForm
							.getAreePassSifVo()
							.setLivelloRicerca(
									TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					request
							.setAttribute(TitoliCollegatiInvoke.codBiblio,
									"011");
				}
				if (sinteticaTitoliForm.getLivRicerca().equals("P")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					sinteticaTitoliForm.getAreePassSifVo()
							.setLivelloRicerca(
									TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
				}
				if (sinteticaTitoliForm.getLivRicerca().equals("I")) {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
					sinteticaTitoliForm
							.getAreePassSifVo()
							.setLivelloRicerca(
									TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
				}

				Class<TitoliCollegatiInvoke> genericClass = TitoliCollegatiInvoke.class;
				int appoggio = 0;
				try {
					Field genericField = genericClass.getField(myForwardName);
					appoggio = genericField.getInt(null);
				} catch (NoSuchFieldException e) {
				}

				sinteticaTitoliForm.getAreePassSifVo().setOggettoRicerca(
						appoggio);
				sinteticaTitoliForm.getAreePassSifVo().setOggettoDaRicercare(bid);
				sinteticaTitoliForm.getAreePassSifVo().setDescOggettoDaRicercare(desDaRic);

				sinteticaTitoliForm.getAreePassSifVo().setOggettoChiamante(mapping.getPath());
				sinteticaTitoliForm.getAreePassSifVo().setVisualCall(true);


				if (myAction.replace(".", "/").equals(
						myForwardPath.substring(0, myForwardPath.length() - 3))) {

					// almaviva5_20070822
					// Se la sintetica richiesta con il pulsante esamina è la stessa
					// di quella attuale allora devo salvare una copia completa della
					// form corrente e reindirizzare la ricerca sulla nuova form
					SinteticaTitoliForm newForm = (SinteticaTitoliForm) sinteticaTitoliForm.cloneForm(false);
					ActionForward forward = this.load(mapping, newForm, request, "SI");
					if (forward == null) {

						// sinteticaTitoliForm.setTipologiaTastiera("UtilizzoComeSif");
						newForm.setTipologiaTastiera("UtilizzoComeSif");
						// almaviva5_20070822
						// sostituisco alla form originale la nuova form. Trovando una nuova
						// form in sessione che non è presente nella sua lista di elementi la
						// navigazione userà questa form per il forward, senza crearne una nuova.
						//request.getSession().setAttribute(mapping.getName(), newForm);
						ActionForward actionForward = new ActionForward();
						actionForward.setName(myForwardName);
						actionForward.setPath(myForwardPath + "?SIFSINTETICA=TRUE");
						return
							Navigation.getInstance(request).goForward(actionForward, false, newForm);
					}
				} else {
					request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
							sinteticaTitoliForm.getAreePassSifVo()
									.getLivelloRicerca());
					request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
							sinteticaTitoliForm.getAreePassSifVo()
									.getOggettoDaRicercare());
					request.setAttribute(
							TitoliCollegatiInvoke.xidDiRicercaDesc,
							sinteticaTitoliForm.getAreePassSifVo()
									.getDescOggettoDaRicercare());

					request.setAttribute(
							TitoliCollegatiInvoke.tipMatDiRicerca,
							sinteticaTitoliForm.getAreePassSifVo()
									.getTipMatOggetto());

					request.setAttribute(
							TitoliCollegatiInvoke.naturaDiRicerca,
							sinteticaTitoliForm.getAreePassSifVo()
									.getNaturaOggetto());

					request.setAttribute(TitoliCollegatiInvoke.codBiblio,
							sinteticaTitoliForm.getAreePassSifVo()
									.getCodBiblioteca());
					if (sinteticaTitoliForm.getAreePassSifVo()
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


					request.setAttribute("AreePassaggioSifVO", sinteticaTitoliForm.getAreePassSifVo());

					if (myForwardName.equals("SOGGETTI_COLLEGATI_A_TITOLO")) {
						AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();

						if (sinteticaTitoliForm.getLivRicerca().equals("P")) {
							areaDatiPassBiblioSemanticaVO.setInserimentoPolo(true);
						} else {
							areaDatiPassBiblioSemanticaVO.setInserimentoIndice(true);
						}
						areaDatiPassBiblioSemanticaVO.setBidPartenza(sinteticaTitoliForm.getAreePassSifVo()
								.getOggettoDaRicercare());
						areaDatiPassBiblioSemanticaVO.setDescPartenza(sinteticaTitoliForm.getAreePassSifVo()
								.getDescOggettoDaRicercare());
						areaDatiPassBiblioSemanticaVO.setTreeElement(null);
						request.setAttribute("AreaDatiPassBiblioSemanticaVO",
								areaDatiPassBiblioSemanticaVO);
					}
					if (myForwardName.equals("ORDINI")) {
						ListaSuppOrdiniVO ricArr=new ListaSuppOrdiniVO();
						UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
						ricArr.setCodPolo(utenteCollegato.getCodPolo());
						//ricArr.setCodBibl(utenteCollegato.getCodBib()); TCK 3701
						ricArr.setChiamante(mapping.getPath());
						ricArr.setTitolo(new StrutturaCombo(sinteticaTitoliForm.getAreePassSifVo().getOggettoDaRicercare(),""));
						ricArr.setOrdinamento("8");
						request.setAttribute("passaggioListaSuppOrdiniVO",ricArr);
					}
					ActionForward actionForward = new ActionForward();
					actionForward.setName(myForwardName);
					actionForward.setPath(myForwardPath + "?SIFSINTETICA=TRUE");
					return Navigation.getInstance(request).goForward(actionForward);

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

	private void aggiornaForm(HttpServletRequest request,
			SinteticaTitoliForm sinteticaTitoliForm) {

		sinteticaTitoliForm.setTabellaEsaminaVO((TabellaEsaminaVO) this.getServlet().getServletContext().getAttribute("serverTypes"));
		List<?> listaCaricamento = sinteticaTitoliForm.getTabellaEsaminaVO().getLista(sinteticaTitoliForm.getMyPath());

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
					|| eleListaCar.getMyLivelloBaseDati().equals(sinteticaTitoliForm.getLivRicerca())) {
				listaEsamina.setDescrizione(eleListaCar.getMyLabel());
				lista.add(listaEsamina);
			}
		}
		sinteticaTitoliForm.setListaEsaminaTit(lista);

		ComboCodDescVO listaGestisci = new ComboCodDescVO();
		List<ComboCodDescVO> listaNew = new ArrayList<ComboCodDescVO>();

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("00");
		listaGestisci.setDescrizione("");
		listaNew.add(listaGestisci);

		if (sinteticaTitoliForm.getLivRicerca() != null) {
			if (!sinteticaTitoliForm.getLivRicerca().equals("I")) {
				listaGestisci = new ComboCodDescVO();
				listaGestisci.setCodice("01");
				listaGestisci.setDescrizione("Export Unimarc");
				listaNew.add(listaGestisci);
			}
		}


		// Intervento del 11.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
		// Eliminata la Voce di passaggio diretto a Stampa Schede perchè inutile
		// e sostituita dal Salvataggio Identificativi su File da impostare poi sulla Stampa;
//		listaGestisci = new ComboCodDescVO();
//		listaGestisci.setCodice("02");
//		listaGestisci.setDescrizione("Stampa schede catalografiche");
//		listaNew.add(listaGestisci);

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("03");
		listaGestisci.setDescrizione("Salva identificativi su file");
		listaNew.add(listaGestisci);
		sinteticaTitoliForm.setListaGestisciTit(listaNew);
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

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		// Imposto il flag di conferma a TRUE affinchè sia effettuata la
		// variazione senza ricerca simili
		AreaDatiVariazioneTitoloVO areaDatiVariazioneTit = sinteticaTitoliForm.getAreaDatiPassPerConfVariazione();
		areaDatiVariazioneTit.setConferma(true);

//
//
//
//
//		sinteticaTitoliForm.getAreaDatiPassPerConfVariazione().setInserimentoIndice(true);
//
//
//
//


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPassReturn = factory.getGestioneBibliografica()
					.inserisciTitolo(areaDatiVariazioneTit, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", areaDatiVariazioneTit.getDetTitoloPFissaVO().getBid());
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			if (sinteticaTitoliForm.getIdOggColl().equals("")) {
				// ======================================================================================================
				// Caso di conferma su prospettazione simili per legame Gestione 51 (contestuale creazione di oggetto e legame)
				if (areaDatiVariazioneTit.isLegameInf()) {
					request.setAttribute("bid", areaDatiVariazioneTit.getBidArrivo());
					request.setAttribute("livRicerca", "I");
					request.setAttribute("vaiA", "SI");
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica.operOk"));
					this.saveErrors(request, errors);
					return mapping.findForward("analitica");
				}
				// ======================================================================================================


				request.setAttribute("bid", areaDatiPassReturn.getBid());
				if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza() != null) {
					//======================================================================
					// Caso di Conferma creazione di un titolo simile in fase di nuovo legame
					String bidDaLegare = areaDatiPassReturn.getBid();
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setIdArrivo(bidDaLegare);

					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo(
							areaDatiVariazioneTit.getDetTitoloPFissaVO().getNatura());
					if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getNatura().equals("A")) {
						if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getTipoMat() == null
								|| areaDatiVariazioneTit.getDetTitoloPFissaVO().getTipoMat().equals("")
								|| areaDatiVariazioneTit.getDetTitoloPFissaVO().getTipoMat().equals("M")) {
							sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("TU");
						} else if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
							sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("UM");
						}
					}
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setDescArrivo(
							areaDatiVariazioneTit.getDetTitoloPFissaVO().getAreaTitTitolo());
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(
							areaDatiVariazioneTit.isFlagCondiviso());

					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSiciNew("");
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

					request.setAttribute("AreaDatiLegameTitoloVO", sinteticaTitoliForm
							.getAreaDatiLegameTitoloVO());
					return mapping.findForward("gestioneLegameTitolo");
					//======================================================================
				}
			} else {
				request.setAttribute("bid", sinteticaTitoliForm.getIdOggColl());
			}

			request.setAttribute("livRicerca", "I");
			if (areaDatiVariazioneTit != null
					&& areaDatiVariazioneTit.isFlagCondiviso() == false) {
				request.setAttribute("livRicerca", "P");
			}
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			return mapping.findForward("analitica");
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(areaDatiPassReturn.getTestoProtocollo()));
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

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

//		BUG MANTIS 3251 almaviva2 prima si chiede la onferma poi si decide se effettuare la variazione con forzatura
		// della data (in caso di conferma fusione) e poi fusione  o solo la variazione della notizia con i soli dati forniti dal
		// bibliotecario (rinuncia a fusione).
		AreaDatiVariazioneTitoloVO areaDatiVariazioneTit = sinteticaTitoliForm.getAreaDatiPassPerConfVariazione();

		if (!sinteticaTitoliForm.getTipologiaTastiera().equals("ConfermaFusione")) {
			// Invio del diagnostico con richiesta di conferma della fusione e non fusione automatica
			sinteticaTitoliForm.setTipologiaTastiera("ConfermaFusione");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricConfermaFusione",
					areaDatiVariazioneTit.getDetTitoloPFissaVO().getBid(),
					sinteticaTitoliForm.getSelezRadio()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}



		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			// Inizio Modifica almaviva2 02.08.2010 - intervento interno - corretta la chiamata per verifica abilitazione
			// utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().FONDE_AUTORE_1270, "LU");
			if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getNatura().equals("A")) {
				if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().FONDE_TITOLO_UNIFORME_MUSICA_1269);
				} else {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().FONDE_TITOLO_UNIFORME_1268);
				}
			} else if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getNatura().equals("B")) {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200);
			} else {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().FONDE_DOCUMENTI_1024);
			}
			// Fine Modifica almaviva2 02.08.2010 - intervento interno
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


		if (sinteticaTitoliForm.getSelezRadio() == null	|| sinteticaTitoliForm.getSelezRadio().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		int size = sinteticaTitoliForm.getListaSintetica().size();
//		if (!sinteticaTitoliForm.getTipologiaTastiera().equals("ConfermaFusione")) {
			// ===================================================================================================
			// MODIFICA ALLA PROCEDURA DI FUSIONE IN FASE DI VARIAZIONE
			// PRIMA SI EFFETTUA L'AGGIORNAMENTO POI LA FUSIONE ALTRIMENTI RISONDE CHE LA CARTA D'IDENTITA'
			// E' DIVERSA E NON PERMETTE LA FUSIONE STESSA

			if (!isTokenValid(request)) {
				saveToken(request);
				return mapping.getInputForward();
			}

			AreaDatiVariazioneReturnVO areaDatiPassVariazioneReturn = null;

			// Imposto il flag di conferma a TRUE affinchè sia effettuata la
			// variazione senza ricerca simili
			areaDatiVariazioneTit.setConferma(true);

			areaDatiVariazioneTit.setInserimentoIndice(true);

			// Inizio Modifica almaviva2 30.11.2009 BUG MANTIS 3251 - si forza la variazione della data di pubblicazione
			// per rendere effettivamente uguale la carta di identita

			if (areaDatiVariazioneTit.getDetTitoloPFissaVO().getDataPubbl1() == null
					|| areaDatiVariazioneTit.getDetTitoloPFissaVO().getDataPubbl1().equals("")) {
				String bidArrivo = "";
				SinteticaTitoliView eleSinteticaTitoliView = null;
				if (sinteticaTitoliForm.getSelezRadio() != null) {
					bidArrivo = sinteticaTitoliForm.getSelezRadio();
					for (int i = 0; i < size; i++) {
						eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
						if (eleSinteticaTitoliView.getBid().equals(bidArrivo)) {
							areaDatiVariazioneTit.getDetTitoloPFissaVO().setDataPubbl1(eleSinteticaTitoliView.getDataPubbl1Da());
							break;
						}
					}
				}
			}


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			areaDatiPassVariazioneReturn = factory.getGestioneBibliografica()
						.inserisciTitolo(areaDatiVariazioneTit, Navigation.getInstance(request).getUserTicket());

			if (areaDatiPassVariazioneReturn == null) {
				request.setAttribute("bid", areaDatiVariazioneTit.getDetTitoloPFissaVO().getBid());
				request.setAttribute("livRicerca", "I");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}


			// Modifica almaviva2 BUG MANTIS 3918 del 04.10.2010
			// Dopo le modifiche per la gestione della messaggistica per i livelli sulle variazioni per i materiali speciali
			// è stato necessario aggiungere l'if stretto sul CodErr = 0000;
			if (!areaDatiPassVariazioneReturn.getCodErr().equals("0000")) {
				if (areaDatiPassVariazioneReturn.getCodErr().equals("9999") || areaDatiPassVariazioneReturn.getTestoProtocollo() != null ) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(areaDatiPassVariazioneReturn.getTestoProtocollo()));
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
			}


//			// Invio del diagnostico con richiesta di conferma della fusione e non fusione automatica
//			sinteticaTitoliForm.setTipologiaTastiera("ConfermaFusione");
//			ActionMessages errors = new ActionMessages();
//			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricConfermaFusione",
//					areaDatiVariazioneTit.getDetTitoloPFissaVO().getBid(),
//					sinteticaTitoliForm.getSelezRadio()));
//			this.saveErrors(request, errors);
//			return mapping.getInputForward();
//			// ===================================================================================================
//		}

		if (sinteticaTitoliForm.getSelezRadio() != null) {
			String bidAccorpante = sinteticaTitoliForm.getSelezRadio();
			String bidEliminato = areaDatiVariazioneTit.getDetTitoloPFissaVO()
					.getBid();
			String tipoMatEliminato = areaDatiVariazioneTit.getDetTitoloPFissaVO()
					.getTipoMat();
			String tipoMatAccorpante = "";

			String tipoAutEliminato = "";
			String tipoAutAccorpante = "";

			if (areaDatiVariazioneTit
					.getDetTitoloPFissaVO().getNatura().equals("A")) {
				if (areaDatiVariazioneTit
						.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
					tipoAutEliminato = (SbnAuthority.UM).toString();
				} else {
					tipoAutEliminato = (SbnAuthority.TU).toString();
				}
			}

			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < size; i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
						.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(bidAccorpante)) {
					if (eleSinteticaTitoliView.getTipoMateriale().equals(
							"bianco")) {
						tipoMatAccorpante = "";
					} else {
						tipoMatAccorpante = eleSinteticaTitoliView
								.getAlfaMateriale();
					}
					tipoAutAccorpante = eleSinteticaTitoliView
							.getTipoAutority();
					break;
				}
			}

			// Settembre 2018 almaviva2 - problema:in fase di variazione descrizione titolo, se si varia la data in modo che
			// cambi automaticamente anche il tipo materiale (da Moderno ad Antico e viceversa) la fusione dei titoli resi
			// uguali non funziona perchè, mancando una rilettura del documento variato, nell'area memorizzata è presente
			// il vecchio tipoMateriale e non quello aggiornato. Viene eliminato questo controllo dalla parte di Sbnweb
			// devolvendolo a quello effettuato in indice dal protocollo.
			// if (tipoMatAccorpante.equals(tipoMatEliminato)
			//	&&  tipoAutAccorpante.equals(tipoAutEliminato)) {

			if ( tipoAutAccorpante.equals(tipoAutEliminato)) {
				AreaDatiAccorpamentoVO areaDatiPass = new AreaDatiAccorpamentoVO();
				AreaDatiAccorpamentoReturnVO areaDatiPassReturn = new AreaDatiAccorpamentoReturnVO();
				areaDatiPass.setIdElementoEliminato(bidEliminato);
				areaDatiPass.setIdElementoAccorpante(bidAccorpante);
				if (!tipoAutAccorpante.equals("")) {
					areaDatiPass.setTipoAuthority(SbnAuthority
							.valueOf(tipoAutAccorpante));
				}
				areaDatiPass.setTipoMateriale(tipoMatAccorpante);
				areaDatiPass.setLivelloBaseDati("I");


//				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				areaDatiPassReturn = factory.getGestioneBibliografica().richiestaAccorpamento(areaDatiPass, Navigation.getInstance(request).getUserTicket());

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
			} else {
				// Inizio Modifica almaviva2 02.08.2010 - intervento interno - inserita diagnostica di fusione non effettuata
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.avvisoFusioneNonPoss", bidEliminato, bidAccorpante));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
				// Fine Modifica almaviva2 02.08.2010 - intervento interno
			}
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward catturaOggetto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getSelezRadio() != null) {

			String bidDaCatturare = sinteticaTitoliForm.getSelezRadio();
			String tipoAutDaCatturare = "";
			String[] appo = new String[0];

			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
						.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(bidDaCatturare)) {
					tipoAutDaCatturare = eleSinteticaTitoliView.getTipoAutority();
					break;
				}
			}

			//Inizio modifica almaviva2 BUG MANTIS 4131 (Collaudo) si controlla se l'oggetto che si vuole catturare in
			// caso di oggetto che dovrà diventare errivo di un legami 51 faccia parte del reticolo in oggetto (esempio
			// cerco di catturare una N questa deve essere legata alla S in oggetto: non posso catturarne una gia legata ad altra S)
			if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazioneSimiliPerCattura51")) {
				if (!eleSinteticaTitoliView.getTipoRecDescrizioneLegami().contains(sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza())) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.catt02", sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
			//Fine modifica almaviva2 BUG MANTIS 4131 (Collaudo)




			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
			AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
			areaDatiPassCattura.setIdPadre(bidDaCatturare);
			areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
			areaDatiPassCattura.setInferioriDaCatturare(appo);

			try {
				areaDatiPassReturnCattura = factory.getGestioneBibliografica()
						.catturaReticolo(areaDatiPassCattura, Navigation.getInstance(request).getUserTicket());
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

			if (areaDatiPassReturnCattura.getCodErr().equals("0000")) {

				//Inizio modifica almaviva2 BUG MANTIS 4131 (Collaudo) dopo la cattura si torna al reticolo originale senza dover creare il legame
				// perchè è stato catturato automaticamente;
				if (sinteticaTitoliForm.getTipologiaTastiera().equals("ProspettazioneSimiliPerCattura51")) {
					request.setAttribute("bid", sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza());
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
					this.saveErrors(request, errors);
					request.setAttribute("bid", sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza());
					request.setAttribute("livRicerca", "I");
					request.setAttribute("vaiA", "SI");
					return mapping.findForward("analitica");
				}
				//Fine modifica almaviva2 BUG MANTIS 4131 (Collaudo)

				if (sinteticaTitoliForm.getIdOggColl().equals("")) {
					request.setAttribute("bid", areaDatiPassReturnCattura.getBid());

					if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza() != null) {
//						======================================================================
//						Caso di Cattura di titolo simile in fase di nuovo legame
						String bidDaLegare = areaDatiPassReturnCattura.getBid();
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setIdArrivo(bidDaLegare);

						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo(eleSinteticaTitoliView.getNatura());
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo(eleSinteticaTitoliView.getTipoAutority());
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setDescArrivo(eleSinteticaTitoliView.getDescrizioneLegami());
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(eleSinteticaTitoliView.isFlagCondiviso());

						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSiciNew("");
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

						request.setAttribute("AreaDatiLegameTitoloVO", sinteticaTitoliForm
								.getAreaDatiLegameTitoloVO());
						return mapping.findForward("gestioneLegameTitolo");
//						======================================================================
					}
				} else {
					request.setAttribute("bid", sinteticaTitoliForm.getIdOggColl());
				}

				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				request.setAttribute("bid", bidDaCatturare);
				request.setAttribute("livRicerca", "I");
				request.setAttribute("vaiA", "NO");
				return mapping.findForward("analitica");
			} else {
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
		}
		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	public ActionForward prospettaPerLegame(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setIdArrivo(
				sinteticaTitoliForm.getSelezRadio());

		if (sinteticaTitoliForm.getSelezRadio() != null) {
			SinteticaTitoliView eleSinteticaTitoliView = null;
			String bidDaLegare = sinteticaTitoliForm.getSelezRadio();
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
						.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(bidDaLegare)) {

//					 un oggetto condiviso può legarsi solo a elementi condivisi;
					if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
						// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
						if (!eleSinteticaTitoliView.getNatura().equals("R")) {
							if (!eleSinteticaTitoliView.isFlagCondiviso()) {
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage("errors.gestioneBibliografica.elemCondivisoConElemLocale"));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						}
					}
					//=====================================================================================
					// intervento almaviva2 11.01.2012 BUG MANTIS 4805 (collaudo): Nel caso in cui il titolo
					// di partenza sia di natura N è possibile effettuare legami solo di tipo 6B, 8P, 8D, 9A

					if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("N")) {
						if (!eleSinteticaTitoliView.getNatura().equals("B")
								&& !eleSinteticaTitoliView.getNatura().equals("P")
								&& !eleSinteticaTitoliView.getNatura().equals("D")
								&& !eleSinteticaTitoliView.getNatura().equals("A")) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.gestioneBibliografica.LegamiNatNObbligati"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
					}
					//=====================================================================================


					// Febbraio 2018 - gestione del blocco alla creazione del legami fra titoli Uniformi e titoli di composizione
					if (!ValidazioneDati.strIsNull(sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza())) {
						if (!ValidazioneDati.strIsNull(eleSinteticaTitoliView.getTipoAutority())) {
							if ((sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("TU") &&
									eleSinteticaTitoliView.getTipoAutority().equals("UM")) ||
									(sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("UM") &&
											eleSinteticaTitoliView.getTipoAutority().equals("TU"))) {
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.LegameImpossibileUMTU"));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						}
					}


					//=====================================================================================
					// Controllo di esistenza di un tipo legame valido fra le nature
					// prima di richiamare la mappa di legame
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					if ((factory.getCodici().getLICR(
									sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza(),
									eleSinteticaTitoliView.getNatura())).size() < 1) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.LegameNonPrevisto"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					//=====================================================================================

					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo(eleSinteticaTitoliView.getNatura());
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo(eleSinteticaTitoliView.getTipoAutority());
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setDescArrivo(eleSinteticaTitoliView.getDescrizioneLegami());
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(eleSinteticaTitoliView.isFlagCondiviso());
					break;
				}
			}
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

			request.setAttribute("AreaDatiLegameTitoloVO", sinteticaTitoliForm.getAreaDatiLegameTitoloVO());
			return mapping.findForward("gestioneLegameTitolo");

		}
		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}


	public ActionForward selezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception{

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		SinteticaTitoliView eleSinteticaTitoliView = null;
		int numElem = sinteticaTitoliForm.getListaSintetica().size();
		String[] listaBidSelez = new String[numElem];
		for (int i = 0; i < numElem; i++) {
			eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
				.getListaSintetica().get(i);
			listaBidSelez[i] =  eleSinteticaTitoliView.getBid();
		}
		sinteticaTitoliForm.setSelezCheck(listaBidSelez);
		return mapping.getInputForward();
	}

	public ActionForward deSelezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;
		sinteticaTitoliForm.setSelezCheck(null);
		return mapping.getInputForward();
	}

	public ActionForward tornaAServizi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		SinteticaTitoliForm currentForm = (SinteticaTitoliForm) form;

		String bid = currentForm.getSelezRadio();
		if (ValidazioneDati.isFilled(bid)) {

			String titolo = null;
			log.info("SERVIZI bid selezionato: " + bid);
			for (int i = 0; i < currentForm.getListaSintetica().size(); i++) {
				SinteticaTitoliView titoliView = (SinteticaTitoliView) currentForm.getListaSintetica().get(i);
				if (titoliView.getBid().equals(bid)) {
					titolo = titoliView.getTitolo();
					break;
				}
			}
			// se titolo è null abbiamo un problema
			if (titolo != null) {

				//almaviva5_20101103 #3958
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				if (!delegate.esisteInventarioBibliotecaSistemaMetro(currentForm.getFiltroBib(), bid) ) {
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noInv"));
					return mapping.getInputForward();
				}

				request.setAttribute("bid", bid);
				request.setAttribute("titolo", titolo);
				request.setAttribute("livRicerca", currentForm.getLivRicerca());
				resetToken(request);
				if (currentForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
					if (navi.getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
						request.setAttribute("elencoBiblSelezionate", currentForm.getElencoBibliotecheSelezionate());
						return navi.goBack(mapping.findForward("stampaServizi"));
					} else {
						// Giuseppe - inizio
						request.setAttribute("elencoBiblSelezionate", currentForm.getElencoBibliotecheSelezionate());
						// Giuseppe - fine

						//almaviva5_20101108 #3958
						request.setAttribute("desc", titolo);
						request.setAttribute("bid", bid);

						List<String> listaBiblio = new ArrayList<String>();
						for (BibliotecaVO b : currentForm.getFiltroBib())
							listaBiblio.add(b.getCod_bib());

	                    request.setAttribute("listaBiblio", listaBiblio);

						return navi.goForward(mapping.findForward("sifinventario"));

					}
				} else if (currentForm.getTipologiaTastiera().equals("ProspettazionePerMovimentiUtente")) {
					return navi.goBack(mapping.findForward("movimentiUtente"));
				}
			}
		}

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
		return mapping.getInputForward();
	}


	public ActionForward tornaAAcquisizioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getLivRicerca().equals("I")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.effettuareRicercaInPolo"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		}

		if (sinteticaTitoliForm.getSelezRadio() != null
				&& !sinteticaTitoliForm.getSelezRadio().equals("")) {

			String titolo = null;
			String bid = sinteticaTitoliForm.getSelezRadio();
			log.info("ACQUISIZIONI bid selezionato: " + bid);
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				SinteticaTitoliView titoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
				if (titoliView.getBid().equals(bid)) {
					titolo = titoliView.getTitolo();
					break;
				}
			}

			// se titolo è null abbiamo un problema
			if (titolo != null) {
				request.setAttribute("bid", bid);
				request.setAttribute("titolo", titolo);
				request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());
				resetToken(request);
				ActionForward actionForward = new ActionForward();
				actionForward.setName("acquisizioni");
				actionForward.setPath(sinteticaTitoliForm.getRitornoDaEsterna());
				return Navigation.getInstance(request).goBack(actionForward);
			}
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}


	public ActionForward catturaEFondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String bidDaCatturare = sinteticaTitoliForm.getSelezRadio();
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaTitoliView eleSinteticaTitoliView = null;
		for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
			eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
					.getListaSintetica().get(i);
			if (eleSinteticaTitoliView.getBid().equals(bidDaCatturare)) {
				tipoAutDaCatturare = eleSinteticaTitoliView.getTipoAutority();
				break;
			}
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
		areaDatiPassCattura.setIdPadre(bidDaCatturare);
		areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
		areaDatiPassCattura.setInferioriDaCatturare(appo);

		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaReticolo(areaDatiPassCattura, Navigation.getInstance(request).getUserTicket());
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

		// In questo caso (l'oggetto è già presente in Indice) è stato già aggiornato il flag di condivisione in Polo dalla cattura
		// la transazione può concludersi qui.

		// Modifica almaviva2 2010.11.17 BUG MANTIS 3990 - il bid da confrontare pnel caso di fusione di un oggetto
		// proveniente da Indice con se stesso è sia quello presente in sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza()
		// che in sinteticaTitoliForm.getIdOggColl() metodo catturaEFondi di SinteticaTitoliAction

		if (sinteticaTitoliForm.getSelezRadio().equals(sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza())
				|| sinteticaTitoliForm.getSelezRadio().equals(sinteticaTitoliForm.getIdOggColl())) {
			request.setAttribute("bid", sinteticaTitoliForm.getSelezRadio());
			request.setAttribute("livRicerca", "I");
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza() != null) {
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030",
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza(),
						sinteticaTitoliForm.getSelezRadio()));
			} else {
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030",
						sinteticaTitoliForm.getIdOggColl(),
						sinteticaTitoliForm.getSelezRadio()));
			}
			this.saveErrors(request, errors);
			return mapping.findForward("analitica");
		}


		String bidAccorpante = "";
		String bidEliminato = "";
		String tipoMatEliminato = "";
		String tipoMatAccorpante = "";
		String tipoAutEliminato = "";
		String tipoAutAccorpante = "";

		if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			bidAccorpante = sinteticaTitoliForm.getSelezRadio();
			bidEliminato = sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza();
			tipoMatEliminato = sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipMatBidPartenza();
			tipoMatAccorpante = "";

			if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("A")) {
				if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipMatBidPartenza().equals("U")) {
					tipoAutEliminato = (SbnAuthority.UM).toString();
				} else {
					tipoAutEliminato = (SbnAuthority.TU).toString();
				}
			}

			if (eleSinteticaTitoliView.getTipoMateriale().equals("bianco")) {
				tipoMatAccorpante = "";
			} else {
				tipoMatAccorpante = eleSinteticaTitoliView.getAlfaMateriale();
			}
			tipoAutAccorpante = eleSinteticaTitoliView.getTipoAutority();

		} else {
			bidAccorpante = sinteticaTitoliForm.getSelezRadio();
			bidEliminato = sinteticaTitoliForm
					.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO()
					.getBid();
			tipoMatEliminato = sinteticaTitoliForm
					.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO()
					.getTipoMat();
			tipoMatAccorpante = "";

			if (sinteticaTitoliForm.getAreaDatiPassPerConfVariazione()
					.getDetTitoloPFissaVO().getNatura().equals("A")) {
				if (sinteticaTitoliForm.getAreaDatiPassPerConfVariazione()
						.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
					tipoAutEliminato = (SbnAuthority.UM).toString();
				} else {
					tipoAutEliminato = (SbnAuthority.TU).toString();
				}
			}

			if (eleSinteticaTitoliView.getTipoMateriale().equals("bianco")) {
				tipoMatAccorpante = "";
			} else {
				tipoMatAccorpante = eleSinteticaTitoliView.getAlfaMateriale();
			}
			tipoAutAccorpante = eleSinteticaTitoliView.getTipoAutority();

		}
//		(Fine 5Agosto2009)




		// Inizio modifica almaviva2 BUG MANTIS 4486 esercizio : si eliminano i controlli sul tipo materiale in Fusione in quanto
		// è possibile richiedere la fusione anche tra materiali diversi fatto salvo l'Antico che viene controllato
		// a livello di protocollo;

		// Modificato L.almaviva2 08.03.2011 si aggiunge la trim dei tipi Materiale in controlli pre-fusione per eguagliare i casi di vuoto e blank
//		if (tipoMatAccorpante.trim().equals(tipoMatEliminato.trim())
//				&& tipoAutAccorpante.equals(tipoAutEliminato)) {
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
			factory = FactoryEJBDelegate.getInstance();

			areaDatiPassReturn = factory.getGestioneBibliografica().richiestaAccorpamento(areaDatiPass, Navigation.getInstance(request).getUserTicket());

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
//				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
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
//		} else {
//			// Modifica almaviva2 28.02.2011 - invio diagnostica nel caso di natura/tipo materiale diverso fra bidPartenza e bidArrivo
//			ActionMessages errors = new ActionMessages();
//			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.avvisoFusioneNonPoss", bidEliminato, bidAccorpante));
//			this.saveErrors(request, errors);
//			return mapping.getInputForward();
//		}
		// Fine modifica almaviva2 BUG MANTIS 4486 esercizio




		return mapping.getInputForward();
	}

	public ActionForward catturaESpostaLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		if (sinteticaTitoliForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String bidDaCatturare = sinteticaTitoliForm.getSelezRadio();
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaTitoliView eleSinteticaTitoliView = null;
		for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
			eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
					.getListaSintetica().get(i);
			if (eleSinteticaTitoliView.getBid().equals(bidDaCatturare)) {
				tipoAutDaCatturare = eleSinteticaTitoliView.getTipoAutority();
				break;
			}
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
		areaDatiPassCattura.setIdPadre(bidDaCatturare);
		areaDatiPassCattura.setTipoAuthority(tipoAutDaCatturare);
		areaDatiPassCattura.setInferioriDaCatturare(appo);

		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaReticolo(areaDatiPassCattura, Navigation.getInstance(request).getUserTicket());
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
		if (sinteticaTitoliForm.getSelezRadio().equals(sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza())) {
			request.setAttribute("livRicerca", "P");
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			return mapping.findForward("analitica");
		}

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null) {

			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setIdArrivoNew(bidDaCatturare);
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoOperazione("Sposta");

			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
			sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);

			if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null) {

				// Inizio modifica almaviva2 2010.11.05 BUG Mantis 3971
				// per la casistica della catalogazione in Indice di un legame solo locale il tipo legame arriva in formato Unimarc
				// quindi non ha bisogno di decodifica ulteriore
				String codiceLegame = sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
											sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoLegameNew() +
											sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo();

				String tipoLegameUnimarc = "";
				try {
				tipoLegameUnimarc = (factory.getCodici().cercaDescrizioneCodice(
					codiceLegame,
					CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
					CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (!tipoLegameUnimarc.equals("")) {
					sinteticaTitoliForm.getAreaDatiLegameTitoloVO().setTipoLegameNew(
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
						tipoLegameUnimarc +
						sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo());
				}
				// Fine modifica almaviva2 2010.11.05 BUG Mantis 3971

				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.inserisciLegameTitolo(sinteticaTitoliForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					return mapping.findForward("annulla");
				}
			} else if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("TU")
				|| sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("UM")) {
				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.collegaElementoAuthority(sinteticaTitoliForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					return mapping.findForward("annulla");
				}
			}

			if (areaDatiPassReturn == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.noConnessione"));
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


	public ActionForward variaNotiziaPerCatalog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

		request.setAttribute("tipoProspettazione", "AGG");
		request.setAttribute("flagCondiviso", false);

		DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
		dettTitComVO.setDetTitoloPFissaVO(sinteticaTitoliForm
				.getAreaDatiPassPerConfVariazione().getDetTitoloPFissaVO());
		dettTitComVO.setDetTitoloCarVO(sinteticaTitoliForm
				.getAreaDatiPassPerConfVariazione().getDetTitoloCarVO());
		dettTitComVO.setDetTitoloGraVO(sinteticaTitoliForm
				.getAreaDatiPassPerConfVariazione().getDetTitoloGraVO());
		dettTitComVO.setDetTitoloMusVO(sinteticaTitoliForm
				.getAreaDatiPassPerConfVariazione().getDetTitoloMusVO());
		request.setAttribute("dettaglioTit", dettTitComVO);
		resetToken(request);
		request.setAttribute("bid", sinteticaTitoliForm.getIdOggColl());
		request.setAttribute("desc", sinteticaTitoliForm.getDescOggColl());
		return mapping.findForward("dettaglioTitolo");
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();
		return navi.goBack(true);
	}


	public ActionForward tornaAnalitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;


		if (sinteticaTitoliForm.getTipologiaTastiera().equals("ConfermaFusione")) {
			// ===================================================================================================
//			BUG MANTIS 3251 almaviva2 se si richiede rinuncia si deve effettuare il solo aggiornamento della notizia
			// MODIFICA ALLA PROCEDURA DI FUSIONE IN FASE DI VARIAZIONE
			// PRIMA SI EFFETTUA L'AGGIORNAMENTO POI LA FUSIONE ALTRIMENTI RISONDE CHE LA CARTA D'IDENTITA'
			// E' DIVERSA E NON PERMETTE LA FUSIONE STESSA

			AreaDatiVariazioneTitoloVO areaDatiVariazioneTit = sinteticaTitoliForm.getAreaDatiPassPerConfVariazione();
			AreaDatiVariazioneReturnVO areaDatiPassVariazioneReturn = null;

			// Imposto il flag di conferma a TRUE affinchè sia effettuata la
			// variazione senza ricerca simili
			areaDatiVariazioneTit.setConferma(true);

			areaDatiVariazioneTit.setInserimentoIndice(true);

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			areaDatiPassVariazioneReturn = factory.getGestioneBibliografica()
						.inserisciTitolo(areaDatiVariazioneTit, Navigation.getInstance(request).getUserTicket());

			if (areaDatiPassVariazioneReturn == null) {
				request.setAttribute("bid", areaDatiVariazioneTit.getDetTitoloPFissaVO().getBid());
				request.setAttribute("livRicerca", "I");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}

			if (areaDatiPassVariazioneReturn.getCodErr().equals("9999") || areaDatiPassVariazioneReturn.getTestoProtocollo() != null ) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(areaDatiPassVariazioneReturn.getTestoProtocollo()));
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
		}

		if (sinteticaTitoliForm.getAreaDatiLegameTitoloVO() != null
				&& sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
			request.setAttribute("bid", sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			request.setAttribute("livRicerca", "P");
		} else {
			if (sinteticaTitoliForm.getIdOggColl() != null) {
				request.setAttribute("bid", sinteticaTitoliForm.getIdOggColl());
			} else {
				request.setAttribute("bid", sinteticaTitoliForm.getSelezRadio());
			}
			request.setAttribute("livRicerca", sinteticaTitoliForm.getLivRicerca());
		}

		resetToken(request);
		return Navigation.getInstance(request).goBack(mapping.findForward("analitica"));

	}

	public ActionForward tornaAnaliticaPerCondividi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		request.setAttribute("confermaInvioIndice", "CONFERMATO");
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();
		return navi.goBack(true);

	}


	public AreaDatiPassaggioInterrogazioneTitoloReturnVO creaListaEditCollTit
								(HttpServletRequest request, SinteticaTitoliForm sinteticaTitoliForm) {

		AreaDatiPassaggioInterrogazioneTitoloReturnVO titoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		try {

				RicercaTitCollEditoriVO ricercaTitCollEditoriVO = new RicercaTitCollEditoriVO();

				ricercaTitCollEditoriVO.setIsbnSelez((String[]) request.getAttribute("elencoISBN"));
				ricercaTitCollEditoriVO.setBidSelez(sinteticaTitoliForm.getIdOggColl());

				AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();

				InterrogazioneTitoloGeneraleVO interrGener = new InterrogazioneTitoloGeneraleVO();
				interrGener.setTitolo("");
				interrGener.setNumStandardSelez("");
				interrGener.setNumStandard1("");
				interrGener.setImpronta1("");
				interrGener.setImpronta2("");
				interrGener.setImpronta3("");
				interrGener.setData1A("");
				interrGener.setData1Da("");
				interrGener.setData2A("");
				interrGener.setData2Da("");
				interrGener.setNomeCollegato("");
				interrGener.setLuogoPubbl("");
				interrGener.setTipoMateriale("");
				interrGener.setTipiRecordSelez("");
				interrGener.setNaturaSelez1("");
				interrGener.setNaturaSelez2("");
				interrGener.setNaturaSelez3("");
				interrGener.setNaturaSelez4("");
				interrGener.setSottoNaturaDSelez("");
				interrGener.setPaeseSelez("");
				interrGener.setLinguaSelez("");
				interrGener.setTipoDataSelez("");
				interrGener.setResponsabilitaSelez("");
				interrGener.setRelazioniSelez("");
				areaDatiPass.setInterTitGen(interrGener);
				areaDatiPass.setRicercaPolo(true);
				areaDatiPass.setRicercaIndice(false);
				areaDatiPass.setOggChiamante(99);
				areaDatiPass.setTipoOggetto(99);
				areaDatiPass.setTipoOggettoFiltrato(99);
				areaDatiPass.setOggDiRicerca("");

				titoloReturnVO = getListaEditCollTitolo(ricercaTitCollEditoriVO, request);
				request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);
				request.setAttribute("areaDatiPassReturnSintetica", titoloReturnVO);
				return titoloReturnVO;
		} catch (Exception e) {
			titoloReturnVO.setCodErr("9999");
			titoloReturnVO.setTestoProtocollo(e.getMessage());
			return titoloReturnVO;
		}
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getListaEditCollTitolo
							(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, HttpServletRequest request) throws Exception {

			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String ticket=utenteCollegato.getTicket();

			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			areaDatiPassReturn = factory.getGestioneAcquisizioni().getRicercaEditCollTitolo(ricercaTitCollEditoriVO, ticket);
			return areaDatiPassReturn;
	}


		public ActionForward gestioneLegamiEditTitoli(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {

			SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

			if (sinteticaTitoliForm.getSelezRadio() == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica.selObblOggSint"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			String idFornitore = sinteticaTitoliForm.getSelezRadio();
			String descFornitore = sinteticaTitoliForm.getSelezRadio();
			Boolean fisso=true;

			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(idFornitore)) {
					descFornitore = eleSinteticaTitoliView.getDescrizioneLegami();
					if (eleSinteticaTitoliView.getDescrizioneLegami().contains("Legame implicito non modificabile")) {
						fisso=true;
					} else {
						fisso=false;
					}
					break;
				}
			}

			if (fisso) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
						"Attenzione il legame deriva dall'assegnazione del Numero Standard e non puo' essere modificato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
			areaDatiLegameTitoloVO.setBidPartenza(sinteticaTitoliForm.getIdOggColl());
			areaDatiLegameTitoloVO.setDescPartenza(sinteticaTitoliForm.getDescOggColl());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(false);
			areaDatiLegameTitoloVO.setFlagCondivisoLegame(false);

			areaDatiLegameTitoloVO.setTipoLegameNew("EDIT_TIT");
			areaDatiLegameTitoloVO.setTipoOperazione("Gestisci");
			areaDatiLegameTitoloVO.setIdArrivo(idFornitore);
			int idx = descFornitore.indexOf(" (");
			String nota="";
			if (idx > 0) {
				areaDatiLegameTitoloVO.setDescArrivo(descFornitore.substring(0,idx));
				nota = descFornitore.substring(idx+1,descFornitore.length());
				nota = nota.replace("(", "");
				nota = nota.replace(")", "");
				areaDatiLegameTitoloVO.setNoteLegameNew(nota);
			} else {
				areaDatiLegameTitoloVO.setDescArrivo(descFornitore);
			}


			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			return mapping.findForward("gestioneLegamiEditTitoli");
		}


		public ActionForward cancellaLegamiEditTitoli(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {


			SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

			if (sinteticaTitoliForm.getSelezRadio() == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica.selObblOggSint"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			String idFornitore = sinteticaTitoliForm.getSelezRadio();
			String descFornitore = sinteticaTitoliForm.getSelezRadio();
			Boolean fisso=true;

			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(idFornitore)) {
					descFornitore = eleSinteticaTitoliView.getDescrizioneLegami();
					if (eleSinteticaTitoliView.getDescrizioneLegami().contains("Legame implicito non modificabile")) {
						fisso=true;
					} else {
						fisso=false;
					}
					break;
				}
			}
			if (fisso) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
						"Attenzione il legame deriva dall'assegnazione del Numero Standard e puo' essere cancellato solo modificandolo o " +
						"eliminandolo dalla mappa di variazione titolo"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
			areaDatiLegameTitoloVO.setBidPartenza(sinteticaTitoliForm.getIdOggColl());
			areaDatiLegameTitoloVO.setDescPartenza(sinteticaTitoliForm.getDescOggColl());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(false);
			areaDatiLegameTitoloVO.setFlagCondivisoLegame(false);

			areaDatiLegameTitoloVO.setTipoLegameNew("EDIT_TIT");
			areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
			areaDatiLegameTitoloVO.setIdArrivo(idFornitore);
			int idx = descFornitore.indexOf(" (");
			String nota="";
			if (idx > 0) {
				areaDatiLegameTitoloVO.setDescArrivo(descFornitore.substring(0,idx));
				nota = descFornitore.substring(idx+1,descFornitore.length());
				nota = nota.replace("(", "");
				nota = nota.replace(")", "");
				areaDatiLegameTitoloVO.setNoteLegameNew(nota);
			} else {
				areaDatiLegameTitoloVO.setDescArrivo(descFornitore);
			}


			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			return mapping.findForward("gestioneLegamiEditTitoli");

		}


		// Giugno 2013: La richiesta è di avere nel menu una sola voce (Gestione legami) che in presenza di editori
		// già legati sulla sintetica mostri anche il tasto Cerca e in caso invece di assenza di legami si posizioni
		// direttamente sulla mappa di Ricerca editore
		public ActionForward creaLegamiEditTitoli(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {

			SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;
			request.setAttribute("creazLegameTitEdit", "SI");
			request.setAttribute("bid", sinteticaTitoliForm.getIdOggColl());
			request.setAttribute("desc", sinteticaTitoliForm.getDescOggColl());

			return mapping.findForward("creazLegameTitEdit");

		}

		public ActionForward copiaReticoloSpoglioCreaLegame(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {


			SinteticaTitoliForm sinteticaTitoliForm = (SinteticaTitoliForm) form;

			if (sinteticaTitoliForm.getSelezRadio() == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			String bidDaCatturare = sinteticaTitoliForm.getSelezRadio();
			String tipoMaterialeDaCatturare= "";

			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm
						.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(bidDaCatturare)) {
					tipoMaterialeDaCatturare = eleSinteticaTitoliView.getAlfaMateriale();
					break;
				}
			}

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
			AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
			areaDatiPassCattura.setIdPadre(bidDaCatturare);
			areaDatiPassCattura.setTipoMateriale(tipoMaterialeDaCatturare);
			areaDatiPassCattura.setCopiaSpoglioSenzaMadre(true);
			areaDatiPassCattura.setIdNewMadre(sinteticaTitoliForm.getIdOggColl());

			if (sinteticaTitoliForm.getLivRicerca().equals("I")) {
				areaDatiPassCattura.setSpoglioCondiviso(true);
			} else {
				areaDatiPassCattura.setSpoglioCondiviso(false);
			}



			try {
				areaDatiPassReturnCattura = factory.getGestioneBibliografica()
						.catturaReticolo(areaDatiPassCattura, Navigation.getInstance(request).getUserTicket());
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

			request.setAttribute("bid", sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("bid", sinteticaTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			if (sinteticaTitoliForm.getLivRicerca().equals("I")) {
				request.setAttribute("livRicerca", "I");
			} else  {
				request.setAttribute("livRicerca", "P");
			}
			request.setAttribute("vaiA", "SI");
			return mapping.findForward("analitica");

		}

		public boolean getBidDaInterrogareCorretto(SinteticaTitoliForm sinteticaTitoliForm) {

			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaTitoliForm.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getBid().equals(sinteticaTitoliForm.getSelezRadio())) {
					if (eleSinteticaTitoliView.getNatura().equals("V")) {
						return false;
					}
					break;
				}
			}


			return true;
			}

	}
