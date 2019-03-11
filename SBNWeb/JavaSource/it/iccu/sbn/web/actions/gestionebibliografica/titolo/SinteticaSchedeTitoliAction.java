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

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.SinteticaSchedeTitoliForm;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.MyLabelValueBean;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
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

public class SinteticaSchedeTitoliAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(SinteticaTitoliAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		// tasti per la prospettazione d"esitoAnalitica"ella lista sintetica da interrogazione
		map.put("button.analiticaTit", "analiticaTit");
		map.put("button.esamina", "esamina");

		// Bottone per salavre la coppia di bid per fusione batch
		map.put("button.salvaSuFile", "salvaSuFile");

		// tasti per la prospettazione dei simili a seguito di condividi notizia locale
		map.put("button.gestSimiliCondividi.catturaEFondi", "catturaEFondi");

		map.put("button.listeConf.successivo", "successivo");
		map.put("button.escludiOggettoDoc", "escludiOggettoDoc");
		map.put("button.annulla", "indietro");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Appena si entra sii deve pulire un eventuale presenza di segnalibro
		Navigation.getInstance(request).removeBookmark(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE);

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;




		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {

			if (request.getAttribute("esitoAnalitica") != null
					&& request.getAttribute("esitoAnalitica").equals("CATALOGATO")) {
				// Catalogazione correttamente eseguita; si invia l'aggiornamento della tabella e si richiede il prossimo elemento;
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setAggiornamento(true);
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setStatoLavorRecordNew("4");
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setIdArrivoFusione("");
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setTipoTrattamento("3");

				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setSuccessivo(true);
				request.setAttribute("areaDatiPassCiclicaVO", sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO());
				ActionForward forward = this.load(mapping, form, request, "NO");

				List<SinteticaTitoliView> listaSintetica = sinteticaSchedeTitoliForm.getListaSintetica();
				if (ValidazioneDati.isFilled(listaSintetica)) {
					//selezione primo elemento
					SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
					if (!ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getSelezRadio())) {
						sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
					} else {
						SinteticaTitoliView eleSinteticaTitoliViewNew = null;
						boolean trovato = false;
						for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
							eleSinteticaTitoliViewNew = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
							if (eleSinteticaTitoliViewNew.getBid() == sinteticaSchedeTitoliForm.getSelezRadio()) {
								trovato = true;
								sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliViewNew.getBid());
								break;
							}
						}
						if (!trovato) {
							sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
						}
					}

				} else {
					if (!sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto().equals("N")) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
						saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}

				if (forward == null) {
					sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
					this.aggiornaForm(request, sinteticaSchedeTitoliForm);
					return mapping.getInputForward();
				}

				return mapping.getInputForward();
			}
			return mapping.getInputForward();
		}


		// INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE
		// le verifiche possono essere effettuate solo dopo in quanto si potrebbe impostare la creazione di un Tit Uniforme o di
		// un documento nel passo successivo
		sinteticaSchedeTitoliForm.setCreaDoc("SI");
		sinteticaSchedeTitoliForm.setCreaDocLoc("NO");
		// FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */


		ActionForward forward = this.load(mapping, form, request, "NO");

		List<SinteticaTitoliView> listaSintetica = sinteticaSchedeTitoliForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			//selezione primo elemento
			SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
			if (!ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getSelezRadio())) {
				sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
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
				for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
					eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
					if (eleSinteticaTitoliView.getBid() == sinteticaSchedeTitoliForm.getSelezRadio()) {
						trovato = true;
						sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliView.getBid());
						break;
					}
				}
				if (!trovato) {
					sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
				}
				// Fine Modifica almaviva2 2010.11.04 BUG MANTIS 3926
			}

		} else {

			// Inizio manutenzione 19.07.2011 BUG MANTIS esercizio 4575 (nel caso di troppi titoli trovati si deve inviare il diagnostico
			// corretto e non il generico non trovato)
			if (this.getErrors(request).isEmpty()) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
				saveErrors(request, errors);
			}
			return navi.goBack(true);
		}

		if (forward == null) {
			sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, sinteticaSchedeTitoliForm);
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

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;

		sinteticaSchedeTitoliForm.setTipologiaTastiera("");
		sinteticaSchedeTitoliForm.setUtilizzoComeSif("NO");
		sinteticaSchedeTitoliForm.setProspettazioneSimili("NO");
		sinteticaSchedeTitoliForm.setProspettazionePerLegami("NO");
		sinteticaSchedeTitoliForm.setProspettaDatiOggColl("NO");
		sinteticaSchedeTitoliForm.setProspettazionePerServizi("NO");
		sinteticaSchedeTitoliForm.setProspettazionePerMovimentiUtente("NO");
		sinteticaSchedeTitoliForm.setProspettaSimiliPerCondividi("NO");

		sinteticaSchedeTitoliForm.setAreaDatiPassCiclicaVO((AreaDatiPassaggioSchedaDocCiclicaVO) request.getAttribute("areaDatiPassCiclicaVO"));
		sinteticaSchedeTitoliForm.setProspettaDatiOggColl("SI");
		sinteticaSchedeTitoliForm.setIdOggColl(sinteticaSchedeTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		sinteticaSchedeTitoliForm.setDescOggColl(sinteticaSchedeTitoliForm.getAreaDatiLegameTitoloVO().getDescPartenza());

		sinteticaSchedeTitoliForm.setTipologiaTastiera("ProspettaListeDiConfronto");


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	"ERROR >>" + "Area sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO() invalida"));
			this.saveErrors(request, errors);
			return mapping.findForward("interrogazioneListeDiConfronto");
		}

		try {
			sinteticaSchedeTitoliForm.setAreaDatiPassCiclicaVO(factory.getGestioneBibliografica().getSchedaDocCiclica(
					sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO(), Navigation.getInstance(request).getUserTicket()));
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	"ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().isAutore()) {

			if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede() == null
					|| sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede().size() == 0) {
				sinteticaSchedeTitoliForm.setListaSintetica(null);
				sinteticaSchedeTitoliForm.setNumNotizie(0);
				sinteticaSchedeTitoliForm.setTotRighe(0);
			} else {
				sinteticaSchedeTitoliForm.setListaSintetica(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede());
				sinteticaSchedeTitoliForm.setNumNotizie(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede().size());
				sinteticaSchedeTitoliForm.setTotRighe(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede().size());
			}

		} else {
			if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede() == null) {
				sinteticaSchedeTitoliForm.setListaSintetica(null);
				sinteticaSchedeTitoliForm.setNumNotizie(0);
				sinteticaSchedeTitoliForm.setTotRighe(0);
			} else if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede().size() == 0) {
				sinteticaSchedeTitoliForm.setListaSintetica(null);
				sinteticaSchedeTitoliForm.setNumNotizie(0);
				sinteticaSchedeTitoliForm.setTotRighe(0);
			} else {
				sinteticaSchedeTitoliForm.setListaSintetica(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede());
				sinteticaSchedeTitoliForm.setNumNotizie(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede().size());
				sinteticaSchedeTitoliForm.setTotRighe(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSinteticaSchede().size());
			}

		}

		// Richieste Scognamiglio/Contardi Novembre 2011: Modifica alla messaggistica su Documenti
		if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getCodErr().equals("3001")) {
			if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getTestoProtocollo() == null
				|| sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getTestoProtocollo().length() == 0) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
					saveErrors(request, errors);
					return mapping.getInputForward();
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getTestoProtocollo()));
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

		if (!sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getCodErr().equals("0000")) {
			if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getCodErr().equals("9999")
					|| sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		String idLista = "1";
		sinteticaSchedeTitoliForm.setIdLista(idLista);
		sinteticaSchedeTitoliForm.setMaxRighe(10);
		sinteticaSchedeTitoliForm.setNumBlocco(1);
		if (ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto())) {
			if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto().equals("S")) {
				sinteticaSchedeTitoliForm.setStatoConfrontoDesc("SIMILE");
			} else if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto().equals("U")) {
				sinteticaSchedeTitoliForm.setStatoConfrontoDesc("UGUALE");
			}else if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto().equals("N")) {
				sinteticaSchedeTitoliForm.setStatoConfrontoDesc("NUOVO");
			}
		}

		if (ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoLavorRecord())) {
			if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoLavorRecord().equals("1")) {
				sinteticaSchedeTitoliForm.setStatoLavorRecordDesc("DA TRATTARE");
			} else if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoLavorRecord().equals("2")) {
				sinteticaSchedeTitoliForm.setStatoLavorRecordDesc("IN TRATTAMENTO");
			} else if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoLavorRecord().equals("3")) {
				sinteticaSchedeTitoliForm.setStatoLavorRecordDesc("ESCLUSO");
			} else if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoLavorRecord().equals("4")) {
				sinteticaSchedeTitoliForm.setStatoLavorRecordDesc("TRATTATO");
			}
		}

		sinteticaSchedeTitoliForm.setTotBlocchi(1);
		sinteticaSchedeTitoliForm.setLivRicerca("I");

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Si prospetta la mappa Sintetica titolo con la lista dei titoli
		// trovati
		sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
		aggiornaForm(request, sinteticaSchedeTitoliForm);

		return null;


	}

	public ActionForward analiticaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;

		if (sinteticaSchedeTitoliForm.getAreaDatiLegameTitoloVO() == null) {
			request.setAttribute("presenzaTastoVaiA", "SI");
		} else {
			if (sinteticaSchedeTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null
						|| sinteticaSchedeTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals("")) {
				request.setAttribute("presenzaTastoVaiA", "SI");
			} else {
				request.setAttribute("presenzaTastoVaiA", "NO");
			}
		}

		Integer progressivo = 0;
		if (sinteticaSchedeTitoliForm.getLinkProgressivo() != null) {
			progressivo = sinteticaSchedeTitoliForm.getLinkProgressivo();
			SinteticaTitoliView eleSinteticaTitoliView = null;
			for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
				eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
				if (eleSinteticaTitoliView.getProgressivo() == progressivo) {
					sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliView.getBid());
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
		if (sinteticaSchedeTitoliForm.getSelezCheck() != null	&& sinteticaSchedeTitoliForm.getSelezCheck().length > 0) {
			int length = sinteticaSchedeTitoliForm.getSelezCheck().length;
			listaBidSelezOld = new String[length];
			for (int i = 0; i < length; i++) {
				if (sinteticaSchedeTitoliForm.getSelezCheck()[i] != null && sinteticaSchedeTitoliForm.getSelezCheck()[i].length()> 0) {
					listaBidSelezOld[++cont] = sinteticaSchedeTitoliForm.getSelezCheck()[i];
				}
			}

			if (cont <0) {
				if (sinteticaSchedeTitoliForm.getSelezRadio() != null
						&& !sinteticaSchedeTitoliForm.getSelezRadio().equals("")) {
					String keyDesc = sinteticaSchedeTitoliForm.getSelezRadio();
					request.setAttribute("bid", keyDesc);
					request.setAttribute("livRicerca", sinteticaSchedeTitoliForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaSchedeTitoliForm.getDatiInterrTitolo());
					resetToken(request);
					if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
					} else if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
							request.setAttribute("livRicerca", "P");
							request.setAttribute("presenzaTastoVaiA", "NO");
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
							request.setAttribute("utilizzaComeSIF", "SERVIZI");
					} else if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettaSimiliPerCondividi")) {
						request.setAttribute("presenzaTastoVaiA", "NO");
					}else if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerIdGestionali")) {

						// Modifica almaviva2 del 01.03.2011 BUG MANTIS 4045: intervento per evitare che compaia
						// il tasto Cerca in Indice quando si richiede l'analitica arrivando da ID Gestionali
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}

					if (sinteticaSchedeTitoliForm.getUtilizzoComeSif() != null && sinteticaSchedeTitoliForm.getLivRicerca() != null) {
						if (sinteticaSchedeTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaSchedeTitoliForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			} else if (cont == 0) {
				request.setAttribute("bid", listaBidSelezOld[0]);
				request.setAttribute("livRicerca", sinteticaSchedeTitoliForm.getLivRicerca());
				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaSchedeTitoliForm.getDatiInterrTitolo());
				resetToken(request);
				if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
					request.setAttribute("livRicerca", "P");
					request.setAttribute("presenzaTastoVaiA", "NO");
					request.setAttribute("presenzaTastoCercaInIndice", "NO");
					request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
				}
				if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
					request.setAttribute("livRicerca", "P");
					request.setAttribute("presenzaTastoVaiA", "NO");
					request.setAttribute("presenzaTastoCercaInIndice", "NO");
					request.setAttribute("utilizzaComeSIF", "SERVIZI");
					//almaviva5_20110107
					request.setAttribute("elencoBiblSelezionate", sinteticaSchedeTitoliForm.getElencoBibliotecheSelezionate());
					request.setAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI, sinteticaSchedeTitoliForm.getFiltroBib());
				}

				if (sinteticaSchedeTitoliForm.getUtilizzoComeSif() != null && sinteticaSchedeTitoliForm.getLivRicerca() != null) {
					if (sinteticaSchedeTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaSchedeTitoliForm.getLivRicerca().equals("P")) {
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
					}
				}
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));

			} else {
//				String[] listaBidSelez = sinteticaSchedeTitoliForm.getSelezCheck();
				String[] listaBidSelez = listaBidSelezOld;
				if (listaBidSelez[0] != null) {
					request.setAttribute("bid", listaBidSelez[0]);
					request.setAttribute("livRicerca", sinteticaSchedeTitoliForm.getLivRicerca());
					request.setAttribute("areaDatiPassPerInterrogazione", sinteticaSchedeTitoliForm.getDatiInterrTitolo());
					request.setAttribute("listaBidSelez", listaBidSelez);
					resetToken(request);
					if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerAcquisizioni")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "ACQUISIZIONI");
					}
					if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettazionePerServizi")) {
						request.setAttribute("livRicerca", "P");
						request.setAttribute("presenzaTastoVaiA", "NO");
						request.setAttribute("presenzaTastoCercaInIndice", "NO");
						request.setAttribute("utilizzaComeSIF", "SERVIZI");
					}

					if (sinteticaSchedeTitoliForm.getUtilizzoComeSif() != null && sinteticaSchedeTitoliForm.getLivRicerca() != null) {
						if (sinteticaSchedeTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaSchedeTitoliForm.getLivRicerca().equals("P")) {
							request.setAttribute("presenzaTastoCercaInIndice", "NO");
						}
					}
					return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
				}
			}
		} else {
			// Modifica almaviva2 29.07.2010 - BUG MANTIS 3856 inserito controllo per voce "ProspettazionePerMovimentiUtente"
			if (sinteticaSchedeTitoliForm.getSelezRadio() != null
					&& !sinteticaSchedeTitoliForm.getSelezRadio().equals("")) {
				String keyDesc = sinteticaSchedeTitoliForm.getSelezRadio();
				request.setAttribute("bid", keyDesc);
				if (keyDesc.equals(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getBidDocLocale())) {
					// inserimento segnalibro (GESTIONE_DA_SINT_SCHEDE) per verificare il passaggio dalla sintetica schede ed eventualmente
					// aggiornare le tabelle con il trattamento effettuato
					Navigation.getInstance(request).addBookmark(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE);
					Navigation.getInstance(request).addBookmark(keyDesc);
					request.setAttribute("livRicerca", "P");
				} else {
					if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSchedaIdLocale().get(0)
							.getTipoRecDescrizioneLegami().contains(keyDesc)) {
						request.setAttribute("livRicerca", "P");
					} else {
						request.setAttribute("livRicerca", "I");
					}
				}

				request.setAttribute("areaDatiPassPerInterrogazione", sinteticaSchedeTitoliForm.getDatiInterrTitolo());
				resetToken(request);
				if (sinteticaSchedeTitoliForm.getTipologiaTastiera().equals("ProspettaSimiliPerCondividi")) {
					request.setAttribute("presenzaTastoVaiA", "NO");
				}

				if (sinteticaSchedeTitoliForm.getUtilizzoComeSif() != null && sinteticaSchedeTitoliForm.getLivRicerca() != null) {
					if (sinteticaSchedeTitoliForm.getUtilizzoComeSif().equals("SI") && sinteticaSchedeTitoliForm.getLivRicerca().equals("P")) {
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

	public ActionForward salvaSuFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;

		// Fusione correttamente eseguita; si invia l'aggiornamento della tabella e si richiede il prossimo elemento;
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setAggiornamento(true);
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setStatoLavorRecordNew("2");
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setIdArrivoFusione(sinteticaSchedeTitoliForm.getSelezRadio());
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setTipoTrattamento("2");

		// Modifica per passare a occorrenza successiva dopo aver salvato la coppia su file per fusione
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setSuccessivo(true);

		request.setAttribute("areaDatiPassCiclicaVO", sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO());
		ActionForward forward = this.load(mapping, form, request, "NO");

		List<SinteticaTitoliView> listaSintetica = sinteticaSchedeTitoliForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			//selezione primo elemento
			SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
			if (!ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getSelezRadio())) {
				sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
			} else {
				// Inizio Modifica almaviva2 2010.11.04 BUG MANTIS 3926
				// ricerca in Polo di un bid per titolo; trovo un bid Loc; si passa all'Analitica del bid trovato;
				// tasto Cerca in Indice; prospetta la sintetica di Indice di un altra notizia con la stessa chiave titolo;
				// seleziono Analitica e ricevo msg:
				//"L'oggetto XXXXX non è stato trovato sulla Base Dati. la ricerca effettuata non ha prodotto risultati"
				// L'errore si verifica perchè rimane in memoria la selezione del primo bid locale (infatti nella sintetica di Indice
				// non c'è nessuna selezione effettuata) che nella nuova sintetica non esiste;
				SinteticaTitoliView eleSinteticaTitoliViewNew = null;
				boolean trovato = false;
				for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
					eleSinteticaTitoliViewNew = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
					if (eleSinteticaTitoliViewNew.getBid() == sinteticaSchedeTitoliForm.getSelezRadio()) {
						trovato = true;
						sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliViewNew.getBid());
						break;
					}
				}
				if (!trovato) {
					sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
				}
				// Fine Modifica almaviva2 2010.11.04 BUG MANTIS 3926
			}

		} else {

			if (this.getErrors(request).isEmpty()) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
				saveErrors(request, errors);
			}
			Navigation navi = Navigation.getInstance(request);
			return navi.goBack(true);

//			ActionMessages errors = new ActionMessages();
//			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
//			saveErrors(request, errors);
//			return mapping.getInputForward();
		}

		if (forward == null) {
			sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, sinteticaSchedeTitoliForm);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030",
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getBidDocLocale(),
				sinteticaSchedeTitoliForm.getSelezRadio()));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
		}



	private void aggiornaForm(HttpServletRequest request,
			SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm) {

		sinteticaSchedeTitoliForm.setTabellaEsaminaVO((TabellaEsaminaVO) this.getServlet().getServletContext().getAttribute("serverTypes"));
		List<?> listaCaricamento = sinteticaSchedeTitoliForm.getTabellaEsaminaVO().getLista(sinteticaSchedeTitoliForm.getMyPath());

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
					|| eleListaCar.getMyLivelloBaseDati().equals(sinteticaSchedeTitoliForm.getLivRicerca())) {
				listaEsamina.setDescrizione(eleListaCar.getMyLabel());
				lista.add(listaEsamina);
			}
		}
		sinteticaSchedeTitoliForm.setListaEsaminaTit(lista);

		ComboCodDescVO listaGestisci = new ComboCodDescVO();
		List<ComboCodDescVO> listaNew = new ArrayList<ComboCodDescVO>();

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("00");
		listaGestisci.setDescrizione("");
		listaNew.add(listaGestisci);

		if (sinteticaSchedeTitoliForm.getLivRicerca() != null) {
			if (!sinteticaSchedeTitoliForm.getLivRicerca().equals("I")) {
				listaGestisci = new ComboCodDescVO();
				listaGestisci.setCodice("01");
				listaGestisci.setDescrizione("Export Unimarc");
				listaNew.add(listaGestisci);
			}
		}

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("02");
		listaGestisci.setDescrizione("Stampa schede catalografiche");
		listaNew.add(listaGestisci);

		listaGestisci = new ComboCodDescVO();
		listaGestisci.setCodice("03");
		listaGestisci.setDescrizione("Salva identificativi su file");
		listaNew.add(listaGestisci);
		sinteticaSchedeTitoliForm.setListaGestisciTit(listaNew);
		return;

	}


	public ActionForward catturaEFondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;

		if (sinteticaSchedeTitoliForm.getSelezRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String bidDaCatturare = sinteticaSchedeTitoliForm.getSelezRadio();
		String tipoAutDaCatturare = "";
		String[] appo = new String[0];

		SinteticaTitoliView eleSinteticaTitoliView = null;
		for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
			eleSinteticaTitoliView = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
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
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	"ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturnCattura == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
			if (areaDatiPassReturnCattura.getCodErr().equals("9999")
					|| areaDatiPassReturnCattura.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	areaDatiPassReturnCattura.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturnCattura.getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		// In questo caso (l'oggetto è già presente in Indice) è stato già aggiornato il flag di condivisione in Polo dalla cattura
		// la transazione può concludersi qui.

		// Modifica almaviva2 2010.11.17 BUG MANTIS 3990 - il bid da confrontare pnel caso di fusione di un oggetto
		// proveniente da Indice con se stesso è sia quello presente in sinteticaSchedeTitoliForm.getAreaDatiLegameTitoloVO().getBidPartenza()
		// che in sinteticaSchedeTitoliForm.getIdOggColl() metodo catturaEFondi di SinteticaTitoliAction

		if (sinteticaSchedeTitoliForm.getSelezRadio().equals(sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getBidDocLocale())) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030",
					sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getBidDocLocale(),
						sinteticaSchedeTitoliForm.getSelezRadio()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		String bidAccorpante = "";
		String bidEliminato = "";
		String tipoMatEliminato = "";
		String tipoMatAccorpante = "";
		String tipoAutEliminato = "";
		String tipoAutAccorpante = "";

		SinteticaTitoliView eleSinteticaTitoliViewLocale = null;
		if (sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO() != null
				&& sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSchedaIdLocale() != null
				&& sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSchedaIdLocale().size() > 0) {
			eleSinteticaTitoliViewLocale =
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getListaSchedaIdLocale().get(0);

			bidAccorpante = sinteticaSchedeTitoliForm.getSelezRadio();
			tipoMatAccorpante = "";
			bidEliminato = eleSinteticaTitoliViewLocale.getBid();
			tipoMatEliminato = eleSinteticaTitoliViewLocale.getTipoMateriale();

			if (eleSinteticaTitoliViewLocale.getNatura().equals("A")) {
				if (tipoMatEliminato.equals("U")) {
					tipoAutEliminato = (SbnAuthority.UM).toString();
				} else {
					tipoAutEliminato = (SbnAuthority.TU).toString();
				}
			}
			if (eleSinteticaTitoliView.getTipoMateriale().equals("bianco")) {
				tipoMatAccorpante = "";
			} else {
				tipoMatAccorpante = eleSinteticaTitoliView.getTipoMateriale();
			}

			if (eleSinteticaTitoliView.getTipoAutority() != null) {
				tipoAutAccorpante = eleSinteticaTitoliView.getTipoAutority();
			}
		}

		// Modificato L.almaviva2 08.03.2011 si aggiunge la trim dei tipi Materiale in controlli pre-fusione per eguagliare i casi di vuoto e blank
		if (tipoMatAccorpante.trim().equals(tipoMatEliminato.trim())
				|| tipoAutAccorpante.equals(tipoAutEliminato)) {
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

			areaDatiPassReturn = factory.getGestioneBibliografica().richiestaAccorpamento(
					areaDatiPass, Navigation.getInstance(request).getUserTicket());
			if (areaDatiPassReturn == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			if (areaDatiPassReturn.getCodErr().equals("0000")) {

				// Fusione correttamente eseguita; si invia l'aggiornamento della tabella e si richiede il prossimo elemento;
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setAggiornamento(true);
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setBidDocLocale(bidEliminato);
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setStatoLavorRecordNew("4");
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setIdArrivoFusione(bidAccorpante);
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setTipoTrattamento("1");

				// Modifica per passare a occorrenza successiva dopo aver salvato la coppia su file per fusione
				sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setSuccessivo(true);


				request.setAttribute("areaDatiPassCiclicaVO", sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO());
				ActionForward forward = this.load(mapping, form, request, "NO");

				List<SinteticaTitoliView> listaSintetica = sinteticaSchedeTitoliForm.getListaSintetica();
				if (ValidazioneDati.isFilled(listaSintetica)) {
					//selezione primo elemento
					SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
					if (!ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getSelezRadio())) {
						sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
					} else {
						// Inizio Modifica almaviva2 2010.11.04 BUG MANTIS 3926
						// ricerca in Polo di un bid per titolo; trovo un bid Loc; si passa all'Analitica del bid trovato;
						// tasto Cerca in Indice; prospetta la sintetica di Indice di un altra notizia con la stessa chiave titolo;
						// seleziono Analitica e ricevo msg:
						//"L'oggetto XXXXX non è stato trovato sulla Base Dati. la ricerca effettuata non ha prodotto risultati"
						// L'errore si verifica perchè rimane in memoria la selezione del primo bid locale (infatti nella sintetica di Indice
						// non c'è nessuna selezione effettuata) che nella nuova sintetica non esiste;
						SinteticaTitoliView eleSinteticaTitoliViewNew = null;
						boolean trovato = false;
						for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
							eleSinteticaTitoliViewNew = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
							if (eleSinteticaTitoliViewNew.getBid() == sinteticaSchedeTitoliForm.getSelezRadio()) {
								trovato = true;
								sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliViewNew.getBid());
								break;
							}
						}
						if (!trovato) {
							sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
						}
						// Fine Modifica almaviva2 2010.11.04 BUG MANTIS 3926
					}

				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
					saveErrors(request, errors);
					return mapping.getInputForward();
				}

				if (forward == null) {
					sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
					this.aggiornaForm(request, sinteticaSchedeTitoliForm);
					return mapping.getInputForward();
				}

				request.setAttribute("bid", areaDatiPass.getIdElementoAccorpante());
				request.setAttribute("livRicerca", "I");
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins030", bidEliminato, bidAccorpante));
				this.saveErrors(request, errors);

				return mapping.getInputForward();
			}

			if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {
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
		} else {
			// Modifica almaviva2 28.02.2011 - invio diagnostica nel caso di natura/tipo materiale diverso fra bidPartenza e bidArrivo
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.avvisoFusioneNonPoss", bidEliminato, bidAccorpante));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();


	}

	public ActionForward successivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;


		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setAggiornamento(false);
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setSuccessivo(true);
		request.setAttribute("areaDatiPassCiclicaVO", sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO());
		ActionForward forward = this.load(mapping, form, request, "NO");

		List<SinteticaTitoliView> listaSintetica = sinteticaSchedeTitoliForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			//selezione primo elemento
			SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
			if (!ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getSelezRadio())) {
				sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
			} else {
				// Inizio Modifica almaviva2 2010.11.04 BUG MANTIS 3926
				// ricerca in Polo di un bid per titolo; trovo un bid Loc; si passa all'Analitica del bid trovato;
				// tasto Cerca in Indice; prospetta la sintetica di Indice di un altra notizia con la stessa chiave titolo;
				// seleziono Analitica e ricevo msg:
				//"L'oggetto XXXXX non è stato trovato sulla Base Dati. la ricerca effettuata non ha prodotto risultati"
				// L'errore si verifica perchè rimane in memoria la selezione del primo bid locale (infatti nella sintetica di Indice
				// non c'è nessuna selezione effettuata) che nella nuova sintetica non esiste;
				SinteticaTitoliView eleSinteticaTitoliViewNew = null;
				boolean trovato = false;
				for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
					eleSinteticaTitoliViewNew = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
					if (eleSinteticaTitoliViewNew.getBid() == sinteticaSchedeTitoliForm.getSelezRadio()) {
						trovato = true;
						sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliViewNew.getBid());
						break;
					}
				}
				if (!trovato) {
					sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
				}
				// Fine Modifica almaviva2 2010.11.04 BUG MANTIS 3926
			}

		} else {
			// Inizio manutenzione 19.07.2011 BUG MANTIS esercizio 4575 (nel caso di troppi titoli trovati si deve inviare il diagnostico
			// corretto e non il generico non trovato)
			if (this.getErrors(request).isEmpty()) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
				saveErrors(request, errors);
			}
			Navigation navi = Navigation.getInstance(request);
			return navi.goBack(true);
//			if (!sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto().equals("N")) {
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
//				saveErrors(request, errors);
//				return mapping.getInputForward();
//			}
		}

		if (forward == null) {
			sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, sinteticaSchedeTitoliForm);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}



	public ActionForward escludiOggettoDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSchedeTitoliForm sinteticaSchedeTitoliForm = (SinteticaSchedeTitoliForm) form;


		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setAggiornamento(true);
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setStatoLavorRecordNew("3");
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setIdArrivoFusione("");
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setTipoTrattamento("");
		sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().setSuccessivo(true);
		request.setAttribute("areaDatiPassCiclicaVO", sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO());
		ActionForward forward = this.load(mapping, form, request, "NO");

		List<SinteticaTitoliView> listaSintetica = sinteticaSchedeTitoliForm.getListaSintetica();
		if (ValidazioneDati.isFilled(listaSintetica)) {
			//selezione primo elemento
			SinteticaTitoliView sinteticaTitoliView = listaSintetica.get(0);
			if (!ValidazioneDati.isFilled(sinteticaSchedeTitoliForm.getSelezRadio())) {
				sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
			} else {
				SinteticaTitoliView eleSinteticaTitoliViewNew = null;
				boolean trovato = false;
				for (int i = 0; i < sinteticaSchedeTitoliForm.getListaSintetica().size(); i++) {
					eleSinteticaTitoliViewNew = (SinteticaTitoliView) sinteticaSchedeTitoliForm.getListaSintetica().get(i);
					if (eleSinteticaTitoliViewNew.getBid() == sinteticaSchedeTitoliForm.getSelezRadio()) {
						trovato = true;
						sinteticaSchedeTitoliForm.setSelezRadio(eleSinteticaTitoliViewNew.getBid());
						break;
					}
				}
				if (!trovato) {
					sinteticaSchedeTitoliForm.setSelezRadio(sinteticaTitoliView.getBid());
				}
			}

		} else {
			if (!sinteticaSchedeTitoliForm.getAreaDatiPassCiclicaVO().getStatoConfronto().equals("N")) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
				saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (forward == null) {
			sinteticaSchedeTitoliForm.setMyPath(mapping.getPath().replaceAll("/", "."));
			this.aggiornaForm(request, sinteticaSchedeTitoliForm);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}




	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();
		return navi.goBack(true);
	}


}
