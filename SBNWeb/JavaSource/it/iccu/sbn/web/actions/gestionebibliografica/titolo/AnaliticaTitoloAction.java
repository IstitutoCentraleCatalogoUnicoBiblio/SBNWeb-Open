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
package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.home.MenuHome;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSpecLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.AnaliticaCollocazione;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioCancAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioClasseGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioDescrittoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioSoggettoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioTermineThesauroGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiScambiaResponsLegameTitAutVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.AnaliticaTitoloForm;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.GestioneVaiA;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.MyLabelValueBean;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
import org.apache.struts.actions.LookupDispatchAction;

public class AnaliticaTitoloAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(AnaliticaTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.analiticaIndice", "analiticaIndice");
		map.put("button.dettaglio", "visualDettaglio");
		map.put("button.vaia", "impostazioniVaiA");
		map.put("ricerca.button.aggiornaCanali", "impostazioniVaiA");
		map.put("button.creaTit", "creaTit");
		map.put("button.esamina", "esamina");
		map.put("button.confermaVaiA", "confermaVaiA");
		map.put("button.annullaVaiA", "annullaOperazioneComplessa");
		// tasti per l'avanzamento su elemento precedente/successivo
		map.put("button.elemPrec", "elementoPrecedente");
		map.put("button.elemSucc", "elementoSuccessivo");
		// tasti per la selezione/deseleziona di tutti gli elementi della lista
		map.put("button.selAllInfer", "selezionaInferiori");
		map.put("button.deSelAllInfer", "deSelezionaInferiori");
		// tasti per conferma/annulla di operazioni particolarmente complesse
		map.put("button.confermaScambioForma", "confermaScambioForma");
		map.put("button.annullaScambioForma", "annullaOperazioneComplessa");
		// tasti per conferma/annulla di operazione di Condivisione con Indice dell'elemento del reticolo
		map.put("button.confermaInvioIndice", "confermaInvioIndice");
		map.put("button.annullaInvioIndice", "annullaOperazioneComplessa");

		//almaviva5_20110107
		map.put("button.gestPerServizi.scegli", "tornaAServizi");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		analiticaTitoloForm.setGestioneInferiori("NO");
		analiticaTitoloForm.setTipoOperazioneConferma("");
		analiticaTitoloForm.setAnaliticaAttiva(true);

		// Modifica almaviva2 2010.11.12 BUG MANTIS 3969
		// nuiovo flag per gestire il tasto analitica Di Indice dalla mappa di notizia corrente con Vai A di polo
		analiticaTitoloForm.setPresenzaTastoAnaliticaDiIndice("SI");

		analiticaTitoloForm.setMyPath(mapping.getPath().replaceAll("/", "."));

		if (analiticaTitoloForm.getVisualVaiA() != null && analiticaTitoloForm.getVisualVaiA().equals("SI")) {
			Navigation.getInstance(request).setTesto("Notizia corrente");
		} else {
			if (request.getAttribute("vaiA") != null) {
				analiticaTitoloForm.setVisualVaiA((String) request.getAttribute("vaiA"));
			} else {
				analiticaTitoloForm.setVisualVaiA("NO");
			}
			if (analiticaTitoloForm.getVisualVaiA().equals("SI")) {
				Navigation.getInstance(request).setTesto("Notizia corrente");
			}
		}


		if (Navigation.getInstance(request).bookmarkExists(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA)) {
			analiticaTitoloForm.setPresenzaTastoVaiA("NO");
		} else {
			if (analiticaTitoloForm.getPresenzaTastoVaiA() == null) {
				if (request.getAttribute("presenzaTastoVaiA") == null) {
					analiticaTitoloForm.setPresenzaTastoVaiA("SI");
				} else {
					analiticaTitoloForm.setPresenzaTastoVaiA((String) request.getAttribute("presenzaTastoVaiA"));
				}
			}
		}

		if (analiticaTitoloForm.getPresenzaTastoCercaInIndice() == null) {
			if (request.getAttribute("presenzaTastoCercaInIndice") == null) {
				analiticaTitoloForm.setPresenzaTastoCercaInIndice("SI");
			} else {
				analiticaTitoloForm.setPresenzaTastoCercaInIndice((String) request.getAttribute("presenzaTastoCercaInIndice"));
			}
		}

		if (analiticaTitoloForm.getProvenienzaChiamatainSIF() == null) {
			if (request.getAttribute("utilizzaComeSIF") == null) {
				analiticaTitoloForm.setProvenienzaChiamatainSIF("");
			} else {
				analiticaTitoloForm.setProvenienzaChiamatainSIF((String) request.getAttribute("utilizzaComeSIF"));
				//almaviva5_20110107
				if (ValidazioneDati.equals(analiticaTitoloForm.getProvenienzaChiamatainSIF(), "SERVIZI")) {
					if (request.getAttribute("elencoBiblSelezionate") != null)
						analiticaTitoloForm.setElencoBibliotecheSelezionate((String)request.getAttribute("elencoBiblSelezionate"));

					List<BibliotecaVO> filtroBib = (List<BibliotecaVO>) request.getAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI);
					if (filtroBib != null)
						analiticaTitoloForm.setFiltroBib(filtroBib);
				}
			}
		}

		if (Navigation.getInstance(request).isFromBar() ) {
			if (request.getParameter("RICARICA") != null) {
				int codRit = impostaReticolo(request, analiticaTitoloForm);
				if (codRit < 0) {
					NavigationForward forward = Navigation.getInstance(request).goBack();
					forward.setRedirect(true);
					return forward;
				}
				if (analiticaTitoloForm.getVisualVaiA().equals("SI")) {
					return impostazioniVaiA( mapping, form,  request, response);
				}
			}
			this.caricaListaEsamina(request, analiticaTitoloForm);

			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

			if (request.getAttribute("confermaInvioIndice") != null) {
				if ("CONFERMATO".equals(request.getAttribute("confermaInvioIndice"))) {
					if (request.getAttribute("livRicerca") != null) {
						analiticaTitoloForm.setLivRicerca((String) request.getAttribute("livRicerca"));
					}
					// Si richiama il metodo di "confermaInvioIndice" per completare la condivisione
					ActionForward forward = new ActionForward();
					forward = confermaInvioIndice(mapping, analiticaTitoloForm, request, response);
					if (forward != null) {
						return forward;
					}
					return mapping.getInputForward();
				}
			}

			// Intervento interno su segnalazione RMR 28.02.2013 per aggiornare le tendine del "Vai a":
			// quando si torna dal dettaglio (richiamato la link sull'oggetto non vengono aggiornate e quindi si
			// possono chiedere funzioni incongruenti
			if (analiticaTitoloForm.getVisualVaiA().equals("SI")) {
				return impostazioniVaiA( mapping, form,  request, response);
			}
			return mapping.findForward("input");
		}

		String urlParam = request.getParameter(TreeElementView.TREEVIEW_URL_PARAM);
		if (urlParam != null) {
			analiticaTitoloForm.setRadioItemSelez(urlParam);
			return visualDettaglio( mapping, form,  request, response);
		}

		String keyParam = request.getParameter(TreeElementView.TREEVIEW_KEY_PARAM);
		if (keyParam != null) {
			TreeElementViewTitoli root = analiticaTitoloForm
					.getTreeElementViewTitoli();

			TreeElementView element = root.findElementUnique(Integer.valueOf(keyParam));
			element.invert(); //apro/chudo nodo
			Navigation.getInstance(request).getCache().getCurrentElement()
				.setAnchorId(LinkableTagUtils.ANCHOR_PREFIX + keyParam);

			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", root);
			analiticaTitoloForm.setTreeElementViewTitoli(root);

		} else if (request.getParameter("RETICOLO") != null) {
			if (request.getAttribute("livRicerca") != null) {
				analiticaTitoloForm.setLivRicerca((String) request
						.getAttribute("livRicerca"));
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica.nonTrovato"));
				this.saveErrors(request, errors);
			}

			if (request.getAttribute("areaDatiPassPerInterrogazione") != null) {
				if (request.getAttribute("areaDatiPassPerInterrogazione") instanceof AreaDatiPassaggioInterrogazioneTitoloVO) {
					analiticaTitoloForm
					.setDatiInterrTitolo((AreaDatiPassaggioInterrogazioneTitoloVO) request
							.getAttribute("areaDatiPassPerInterrogazione"));
				} else if (request.getAttribute("areaDatiPassPerInterrogazione") instanceof AreaDatiPassaggioInterrogazioneAutoreVO) {
					analiticaTitoloForm
					.setDatiInterrAutore((AreaDatiPassaggioInterrogazioneAutoreVO) request
							.getAttribute("areaDatiPassPerInterrogazione"));
				} else if (request.getAttribute("areaDatiPassPerInterrogazione") instanceof AreaDatiPassaggioInterrogazioneMarcaVO) {
					analiticaTitoloForm
					.setDatiInterrMarca((AreaDatiPassaggioInterrogazioneMarcaVO) request
							.getAttribute("areaDatiPassPerInterrogazione"));
				} else if (request.getAttribute("areaDatiPassPerInterrogazione") instanceof AreaDatiPassaggioInterrogazioneLuogoVO) {
					analiticaTitoloForm
					.setDatiInterrLuogo((AreaDatiPassaggioInterrogazioneLuogoVO) request
							.getAttribute("areaDatiPassPerInterrogazione"));
				}
			}

			if (request.getAttribute("listaBidSelez") != null) {
				analiticaTitoloForm.setListaBidSelez((String[]) request.getAttribute("listaBidSelez"));
			}

			if (request.getAttribute("listaBidDaFile") != null) {
				analiticaTitoloForm.setListaBidDaFile((List<String>) request.getAttribute("listaBidDaFile"));
				analiticaTitoloForm.setListaBidDaFilePresent("SI");
			} else if (analiticaTitoloForm.getListaBidDaFilePresent().equals("SI")) {
				int size = analiticaTitoloForm.getListaBidDaFile().size();
				for (int i = 0; i < size; i++) {
					if (analiticaTitoloForm.getBidRoot().equals(analiticaTitoloForm.getListaBidDaFile().get(i))) {
						analiticaTitoloForm.getListaBidDaFile().set(i, "----------");
						if (i < (size - 1)) {
							analiticaTitoloForm.setBidRoot((String) analiticaTitoloForm.getListaBidDaFile().get(++i));
							request.setAttribute("bid", analiticaTitoloForm.getBidRoot());
							request.setAttribute("livRicerca", "P");
							request.setAttribute("vaiA", "NO");
							break;
						}
					}
				}
			}

			analiticaTitoloForm.setVisualCheckCattura(false);
			analiticaTitoloForm.setConfermaCanc("NO");
			analiticaTitoloForm.setTipoOperazioneConferma("");

			if (request.getAttribute("tipoAuthority") == null) {
				analiticaTitoloForm.setTipoAuthority("");
			} else {
				analiticaTitoloForm.setTipoAuthority((String) request.getAttribute("tipoAuthority"));
			}

			int codRit = impostaReticolo(request, analiticaTitoloForm);
			if (codRit < 0) {
				NavigationForward forward = Navigation.getInstance(request).goBack();
				forward.setRedirect(true);

				if (analiticaTitoloForm.getErroreRichAnalitica() != null) {
					if (!analiticaTitoloForm.getErroreRichAnalitica().equals("")) {
						forward.addParameter("TESTOERR", analiticaTitoloForm.getErroreRichAnalitica());
					}
				}

				return forward;
			}

			analiticaTitoloForm.setParamId((String) request.getAttribute("bid"));
		}

		caricaListaEsamina(request, analiticaTitoloForm);


		// almaviva2 Aprile 2015: in fase di ritorno ad Analitica dopo una richiesta di variazione descrizione/legami si differenzia
		// il messaggio che può essere OK oppure OK con untesto esplicativo delle operazioni effettuate nel dettaglio
		if (request.getAttribute("messaggio") != null) {
			if (request.getAttribute("testoMessaggio") != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.operOkConParametro", request.getAttribute("testoMessaggio")));
				this.saveErrors(request, errors);
			} else  {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + request.getAttribute("messaggio")));
				this.saveErrors(request, errors);
			}
		}
		log.info("AnaliticaTitoloAction::unspecified");

		if (analiticaTitoloForm.getVisualVaiA().equals("SI")) {
			return impostazioniVaiA( mapping, form,  request, response);
		}
		return mapping.findForward("input");
	}

	private int impostaReticolo(HttpServletRequest request,
			AnaliticaTitoloForm analiticaTitoloForm) throws Exception {

		String codBid = "";

		if (analiticaTitoloForm.getBidRoot() == null) {
			if (request.getAttribute("bid") != null) {
				codBid = (String) request.getAttribute("bid");
				analiticaTitoloForm.setBidRoot(codBid);
			}
		} else {
			if (request.getAttribute("bid") != null) {
				codBid = (String) request.getAttribute("bid");
				analiticaTitoloForm.setBidRoot(codBid);
			} else {
				codBid = analiticaTitoloForm.getBidRoot();
			}
		}

		if (codBid == null)
			return -1;
		analiticaTitoloForm.setBidRoot(codBid);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn = null;

		// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
		// operante che nel caso di centro Sistema non coincidono
		String codBiblioSbn = Navigation.getInstance(request).getUtente().getCodPolo() + Navigation.getInstance(request).getUtente().getCodBib();
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(codBiblioSbn);
		if (areaDatiPass.getCodiceBiblioSbn().equals(codBiblioSbn)) {
			analiticaTitoloForm.setCodiceBiblioSbn("");
		} else {
			analiticaTitoloForm.setCodiceBiblioSbn("per la biblioteca: " + codBiblioSbn);
		}

//		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
		// Fine Modifica almaviva2 16.07.2010

		areaDatiPass.setBidRicerca(codBid);

		if (request.getAttribute("livRicerca") != null) {
			analiticaTitoloForm.setLivRicerca((String) request.getAttribute("livRicerca"));
		}

		if (analiticaTitoloForm.getLivRicerca() == null) {
			analiticaTitoloForm.setLivRicerca("I");
		}

		if (analiticaTitoloForm.getLivRicerca().equals("I")) {
			areaDatiPass.setRicercaIndice(true);
			areaDatiPass.setRicercaPolo(false);
		} else {
			areaDatiPass.setRicercaIndice(false);
			areaDatiPass.setRicercaPolo(true);
		}

		if (analiticaTitoloForm.getTipoAuthority() == null || analiticaTitoloForm.getTipoAuthority().equals("")) {
			if (codBid.substring(3, 4).equals("V")) {
				analiticaTitoloForm.setTipoAuthority("AU");
			} else if (codBid.substring(3, 4).equals("P") || codBid.substring(3, 4).equals("T")) {
				analiticaTitoloForm.setTipoAuthority("PP");
			} else if (codBid.substring(3, 4).equals("M")) {
				analiticaTitoloForm.setTipoAuthority("MA");
			} else if (codBid.substring(3, 4).equals("L")) {
				analiticaTitoloForm.setTipoAuthority("LU");
			} else {
				analiticaTitoloForm.setTipoAuthority("");
			}
		}

		try {
			if (analiticaTitoloForm.getTipoAuthority().equals("AU") || codBid.substring(3, 4).equals("V")) {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.creaRichiestaAnaliticoAutorePerVid(areaDatiPass,
								Navigation.getInstance(request).getUserTicket());


			} else if (analiticaTitoloForm.getTipoAuthority().equals("PP") || codBid.substring(3, 4).equals("P") || codBid.substring(3, 4).equals("T")) {
				areaDatiPassReturn = factory.getGestioneDocumentoFisico().creaRichiestaAnaliticoPossessoriPid(areaDatiPass, Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(),  Navigation.getInstance(request).getUserTicket());
			} else if (analiticaTitoloForm.getTipoAuthority().equals("MA")	|| codBid.substring(3, 4).equals("M")) {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.creaRichiestaAnaliticoMarchePerMid(areaDatiPass,
								Navigation.getInstance(request).getUserTicket());
			} else if (analiticaTitoloForm.getTipoAuthority().equals("LU")	|| codBid.substring(3, 4).equals("L")) {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.creaRichiestaAnaliticoLuoghiPerLid(areaDatiPass,
								Navigation.getInstance(request).getUserTicket());
			} else {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass,
								Navigation.getInstance(request).getUserTicket());
			}
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo", "ERROR >>"	+ e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return -1;
		}
		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return -1;
		}
		if (areaDatiPassReturn.getCodErr() != null) {
			if (areaDatiPassReturn.getCodErr().equals("3001")) {
				if (analiticaTitoloForm.getListaBidDaFilePresent().equals("SI")) {
					int size = analiticaTitoloForm.getListaBidDaFile().size();
					for (int i = 0; i < size; i++) {
						if (codBid.equals(analiticaTitoloForm.getListaBidDaFile().get(i))) {
							analiticaTitoloForm.getListaBidDaFile().set(i, "----------");
							if (i < (size - 1)) {
								analiticaTitoloForm.setBidRoot((String) analiticaTitoloForm.getListaBidDaFile().get(++i));
								request.setAttribute("bid", analiticaTitoloForm.getBidRoot());
								return impostaReticolo(request, analiticaTitoloForm);
							} else {
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
								this.saveErrors(request, errors);
								return -1;
							}
						}
					}

				}
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.oggettoNotFound", codBid));
				this.saveErrors(request, errors);
				return -1;
			}
			if (areaDatiPassReturn.getCodErr().equals("9999")) {
				analiticaTitoloForm.setErroreRichAnalitica("ERROR >>" + areaDatiPassReturn.getTestoProtocollo());
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo", "ERROR >>"	+ areaDatiPassReturn.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return -1;
			}
		}

  		if (areaDatiPassReturn.getTreeElementViewTitoli().getKey() != null) {
			TreeElementViewTitoli root = areaDatiPassReturn.getTreeElementViewTitoli();
			analiticaTitoloForm.setTreeElementViewTitoli(root);
			analiticaTitoloForm.setRadioItemSelez(String.valueOf(root.getRepeatableId()));
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return 0;
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.oggettoNotFound", codBid));
			this.saveErrors(request, errors);
			return -1;
		}
	}

	public ActionForward analiticaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		TreeElementViewTitoli treeElementView = analiticaTitoloForm.getTreeElementViewTitoli();
		analiticaTitoloForm.setRadioItemSelez(String.valueOf(treeElementView.getRepeatableId())); //seleziono root
		TreeElementViewTitoli elementoTree =
			(TreeElementViewTitoli) treeElementView.findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));

		if (!elementoTree.isFlagCondiviso()) {
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.oggettoSoloLocale"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		analiticaTitoloForm.setLivRicerca("I");
		analiticaTitoloForm.setVisualVaiA("SI");

		int codRit = impostaReticolo(request, analiticaTitoloForm);
		if (codRit < 0) {
			NavigationForward forward = Navigation.getInstance(request).goBack();
			forward.setRedirect(true);
			return forward;
		}

		elementoTree = new TreeElementViewTitoli();
		elementoTree = (TreeElementViewTitoli) analiticaTitoloForm.getTreeElementViewTitoli().findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));
		request.setAttribute("vaiA", "SI");
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		boolean bidRoot = false;
		if (elementoTree.getKey().equals(analiticaTitoloForm.getBidRoot())) {
			bidRoot = true;
			analiticaTitoloForm.setGestioneInferiori("SI");
		}

		SbnAuthority elemAuth = elementoTree.getTipoAuthority();
		if (elemAuth == null) {
			if (bidRoot) {
				analiticaTitoloForm.setGestioneInferiori("SI");
			}
			if (analiticaTitoloForm.getTreeElementViewTitoli().getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
				caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else {
				caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			}
		} else {
			analiticaTitoloForm.setGestioneInferiori("NO");
			if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.PP)) {
				caricaComboPossessoreVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU)
					|| SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.UM)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			}
		}


		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAAcquis() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiACatalSemant() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiACatalUnimarc() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestBibliog() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestDocFis() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestPossessori() == null) {
			analiticaTitoloForm.setVisualVaiA("NO");
			request.setAttribute("vaiA", "NO");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.noVaiADisponibili"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		// Inizio modifica almaviva2 del 11.10.2010 BUG MANTIS 3926
		// Il controllo non deve essere effettuato perchè la ricerca deve riattivare tutti i criteri inseriti sulla mappa
		// di interrogazione e non è una secca sul bid di Analitica.
//		if (!analiticaTitoloForm.getTreeElementViewTitoli().getRoot().isFlagCondiviso()) {
//			ActionMessages errors = new ActionMessages();
//			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.oggettoSoloLocale"));
//			this.saveErrors(request, errors);
//			return mapping.getInputForward();
//		}
		// Fine modifica almaviva2 del 11.10.2010 BUG MANTIS 3926

		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati nell'Interrogazione
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		if (analiticaTitoloForm.getTipoAuthority().equals("AU")	|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("V")) {

			analiticaTitoloForm.getDatiInterrAutore().setRicercaPolo(false);
			analiticaTitoloForm.getDatiInterrAutore().setRicercaIndice(true);

			areaDatiPassReturn = factory.getGestioneBibliografica().ricercaAutori(
					analiticaTitoloForm.getDatiInterrAutore(),
					Navigation.getInstance(request).getUserTicket());

		} else if (analiticaTitoloForm.getTipoAuthority().equals("PP") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("P")
				|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("T")) {

			// ................................................................

		} else if (analiticaTitoloForm.getTipoAuthority().equals("MA") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("M")) {
			analiticaTitoloForm.getDatiInterrMarca().setRicercaPolo(false);
			analiticaTitoloForm.getDatiInterrMarca().setRicercaIndice(true);

			areaDatiPassReturn = factory.getGestioneBibliografica().ricercaMarche(
					analiticaTitoloForm.getDatiInterrMarca(),
					Navigation.getInstance(request).getUserTicket());

		} else if (analiticaTitoloForm.getTipoAuthority().equals("LU") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("L")) {
			analiticaTitoloForm.getDatiInterrLuogo().setRicercaPolo(false);
			analiticaTitoloForm.getDatiInterrLuogo().setRicercaIndice(true);

			areaDatiPassReturn = factory.getGestioneBibliografica().ricercaLuoghi(
					analiticaTitoloForm.getDatiInterrLuogo(),
					Navigation.getInstance(request).getUserTicket());
		} else {
			analiticaTitoloForm.getDatiInterrTitolo().setRicercaPolo(false);
			analiticaTitoloForm.getDatiInterrTitolo().setRicercaIndice(true);

			areaDatiPassReturn = factory.getGestioneBibliografica().ricercaTitoli(
					analiticaTitoloForm.getDatiInterrTitolo(),
					Navigation.getInstance(request).getUserTicket());
		}


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
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo", areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.oggettoNotFoundInIndice"));
			this.saveErrors(request, errors);
			resetToken(request);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassReturn);

		if (analiticaTitoloForm.getTipoAuthority().equals("AU")	|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("V")) {
			request.setAttribute("areaDatiPassPerInterrogazione", analiticaTitoloForm.getDatiInterrAutore());
			return mapping.findForward("ritornaSinteticaAutore");
		} else if (analiticaTitoloForm.getTipoAuthority().equals("PP")
				|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("P")
				|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("T")) {
			//....................
			return mapping.getInputForward();
		} else if (analiticaTitoloForm.getTipoAuthority().equals("MA") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("M")) {
			request.setAttribute("areaDatiPassPerInterrogazione", analiticaTitoloForm.getDatiInterrMarca());
			return mapping.findForward("ritornaSinteticaMarca");
		} else if (analiticaTitoloForm.getTipoAuthority().equals("LU") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("L")) {
			request.setAttribute("areaDatiPassPerInterrogazione", analiticaTitoloForm.getDatiInterrLuogo());
			return mapping.findForward("ritornaSinteticaLuogo");
		} else {
			request.setAttribute("areaDatiPassPerInterrogazione", analiticaTitoloForm.getDatiInterrTitolo());
			return mapping.findForward("ritornaSinteticaTitolo");

		}
	}

	public ActionForward elementoPrecedente(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati
		// nell'Interrogazione
		String[] listaBidSelez = analiticaTitoloForm.getListaBidSelez();
		for (int i = 0; i < listaBidSelez.length; i++) {
			if (analiticaTitoloForm.getBidRoot().equals(listaBidSelez[i])) {
				if (i > 0) {
					analiticaTitoloForm.setBidRoot(listaBidSelez[--i]);
				} else {
					this.caricaListaEsamina(request, analiticaTitoloForm);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}

			}
		}

		int codRit = impostaReticolo(request, analiticaTitoloForm);
		if (codRit < 0) {
			NavigationForward forward = Navigation.getInstance(request).goBack();
			forward.setRedirect(true);
			return forward;
		}
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		saveToken(request);
		analiticaTitoloForm.setRadioItemSelez(String
				.valueOf(analiticaTitoloForm.getTreeElementViewTitoli()
						.getRepeatableId()));
		return mapping.getInputForward();
	}

	public ActionForward elementoSuccessivo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		// Mofifica almaviva2 BUG MANTIS 3383 gestito il null nella listaBidSelez

		// Si replica la chiamata all'Indice con gli stessi parametri utilizzati nell'Interrogazione
		if (analiticaTitoloForm.getListaBidDaFilePresent() != null
				&& analiticaTitoloForm.getListaBidDaFilePresent().equals("SI")) {
			int size = analiticaTitoloForm.getListaBidDaFile().size();
			for (int i = 0; i < size; i++) {
				if (analiticaTitoloForm.getBidRoot().equals(analiticaTitoloForm.getListaBidDaFile().get(i))) {
					if (i < (size - 1)) {
						analiticaTitoloForm.setBidRoot((String) analiticaTitoloForm.getListaBidDaFile().get(++i));
					} else {
						this.caricaListaEsamina(request, analiticaTitoloForm);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
			}
		} else {
			String[] listaBidSelez = analiticaTitoloForm.getListaBidSelez();
			int size = listaBidSelez.length;
			for (int i = 0; i < size; i++) {
				if (analiticaTitoloForm.getBidRoot().equals(listaBidSelez[i])) {
					if (i < (size - 1)) {
						i = i + 1;
						if (listaBidSelez[i] == null) {
							this.caricaListaEsamina(request, analiticaTitoloForm);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
							this.saveErrors(request, errors);
							resetToken(request);
							return mapping.getInputForward();
						}
						analiticaTitoloForm.setBidRoot(listaBidSelez[i]);
						break;
					} else {
						this.caricaListaEsamina(request, analiticaTitoloForm);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
			}
		}

		int codRit = impostaReticolo(request, analiticaTitoloForm);
		if (codRit < 0) {
			NavigationForward forward = Navigation.getInstance(request).goBack();
			forward.setRedirect(true);
			return forward;
		}

		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		saveToken(request);
		analiticaTitoloForm.setRadioItemSelez(String.valueOf(analiticaTitoloForm.getTreeElementViewTitoli().getRepeatableId()));
		return mapping.getInputForward();
	}

	public ActionForward selezionaInferiori(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		TreeElementViewTitoli root = analiticaTitoloForm.getTreeElementViewTitoli();

		List<String> nodes = new ArrayList<String>();

		for (TreeElementView child : root.getChildren()) {

			if (child.isCheckVisible())
				nodes.add(String.valueOf(child.getRepeatableId()));
		}

		String[] listaInferiori = null;
		int numElem = nodes.size();
		if (numElem > 0 ) {
			listaInferiori = new String[numElem];
			nodes.toArray(listaInferiori);
		}

		analiticaTitoloForm.setCheckItemSelez(listaInferiori);
		request.setAttribute("testTree", root);
		return mapping.getInputForward();
	}

	public ActionForward deSelezionaInferiori(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		analiticaTitoloForm.setCheckItemSelez(null);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
		return mapping.getInputForward();
	}


	public ActionForward creaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		request.setAttribute("tipoProspettazione", "INS");
		resetToken(request);
		return mapping.findForward("dettaglioTitolo");
	}

	public ActionForward impostazioniVaiA(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		// INIZIO Modifica almaviva2 da richiesta interna:
		// inserimento segnalibro (ANALITICA_PROGRESS) per verificare il passaggio dall'Analitica in Vai a
		Navigation.getInstance(request).addBookmark(TitoliCollegatiInvoke.ANALITICA_PROGRESS);
		// FINE inserimento segnalibro


		TreeElementViewTitoli analiticaRoot = analiticaTitoloForm.getTreeElementViewTitoli();

		if (analiticaTitoloForm.getRadioItemSelez() == null) {
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli) analiticaRoot
				.findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));

		if (elementoTree == null) {
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		resetToken(request);

		if (analiticaTitoloForm.getInterrogazioneVaiAForm() != null) {
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAAcquisSelez("");
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiACatalSemantSelez("");
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiACatalUnimarcSelez("");
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAGestBibliogSelez("");
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAGestDocFisSelez("");
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAGestPossessoriSelez("");
			// Inizio modifica almaviva2 04.08.2010 - Nuova voce per Gestione Periodici
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAGestPeriodiciSelez("");
			// Fine modifica almaviva2 04.08.2010 - Nuova voce per Gestione Periodici
		}


		boolean bidRoot = false;
		if (elementoTree.getKey().equals(analiticaRoot.getKey())) {
			bidRoot = true;
			analiticaTitoloForm.setGestioneInferiori("SI");
		}

		SbnAuthority elemAuth = elementoTree.getTipoAuthority();
		if (elemAuth == null) {
			if (bidRoot) {
				analiticaTitoloForm.setGestioneInferiori("SI");
			}
			caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);

		} else {
			if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.PP)) {
				caricaComboPossessoreVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth,
					SbnAuthority.AB,
					SbnAuthority.CL,
					SbnAuthority.DE,
					SbnAuthority.SO,
					SbnAuthority.TH)) {
				analiticaTitoloForm.setVisualVaiA("NO");
				request.setAttribute("vaiA", "NO");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.noVaiADisponibili"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		}

		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAAcquis() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiACatalSemant() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiACatalUnimarc() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestBibliog() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestDocFis() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestPossessori() == null
				// Inizio modifica almaviva2 04.08.2010 - Nuova voce per Gestione Periodici
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestPeriodici() == null) {
				// Fine modifica almaviva2 04.08.2010 - Nuova voce per Gestione Periodici


			analiticaTitoloForm.setVisualVaiA("NO");
			request.setAttribute("vaiA", "NO");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.noVaiADisponibili"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		analiticaTitoloForm.setVisualVaiA("SI");
		analiticaTitoloForm.setVisualCheckCattura(true);
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		// almaviva2: dicembre 2017 inserito gestione tasti "seleziona inferiori" e "deseleziona inferiori" su richiesta carla
		if (elemAuth == null && (elementoTree.getNatura().equals("M") || elementoTree.getNatura().equals("W"))) {
			analiticaTitoloForm.setGestioneInferiori("SI");
		} else {
			analiticaTitoloForm.setGestioneInferiori("NO");
		}

		return mapping.findForward("input");
	}



	public ActionForward visualDettaglio(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;
		TreeElementViewTitoli treeElementView = analiticaTitoloForm.getTreeElementViewTitoli();

		if (analiticaTitoloForm.getRadioItemSelez() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli) treeElementView
				.findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));

		request.setAttribute("tipoProspettazione", "DET");

		// Richiesta dettaglio
		SbnAuthority elemAuth = elementoTree.getTipoAuthority();
		if (elemAuth == null
				|| SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {
			DettaglioTitoloCompletoVO dettTitComVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO();
			request.setAttribute("dettaglioTit", dettTitComVO);
			resetToken(request);

//			Inizio Modifica almaviva2 del 13.01.2009 BUG Mantis 2525
//			in creazione si trova il simile; se ne richiede l'analitica e poi il dettaglio; la mappa prospettata torna quella di creazione.
//			Devo salvare una copia completa della form corrente e reindirizzare la ricerca sulla nuova form
			if (Navigation.getInstance(request).bookmarkExists(Bookmark.Bibliografica.Titolo.CREA_TITOLO)) {
				AnaliticaTitoloForm newForm = analiticaTitoloForm.cloneForm(false);
				return Navigation.getInstance(request).goForward(mapping.findForward("dettaglioTitolo"), false, newForm);
			} else {
				return Navigation.getInstance(request).goForward(mapping.findForward("dettaglioTitolo"));
			}
//			return mapping.findForward("dettaglioTitolo");
//		 	Fine Modifica almaviva2 del 13.01.2009 BUG Mantis 2525
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
			DettaglioAutoreGeneraleVO dettAutGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO();
			request.setAttribute("dettaglioAut", dettAutGenVO);
			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("dettaglioAutore"));
		}


		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.PP)) {
				PossessoriDettaglioVO possDettVO = elementoTree.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO() ;
				request.setAttribute("dettaglioPossDaAnalitica", possDettVO);
				request.setAttribute("dettaglioPossDaAnaliticaTipoForma", elementoTree.isPossessoreFormaRinvio()?"FORMA_R":"FORMA_A");
				resetToken(request);
				return mapping.findForward("dettaglioPossessore");
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
			DettaglioMarcaGeneraleVO dettMarGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioMarcaGeneraleVO();
			request.setAttribute("dettaglioMar", dettMarGenVO);
			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("dettaglioMarca"));
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.SO)) {
			DettaglioSoggettoGeneraleVO dettSogGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO();
			request.setAttribute("dettaglioSog", dettSogGenVO);
			resetToken(request);
			return mapping.findForward("dettaglioSemantici");
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.CL)) {
			DettaglioClasseGeneraleVO dettClaGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioClasseGeneraleVO();
			request.setAttribute("dettaglioCla", dettClaGenVO);
			resetToken(request);
			return mapping.findForward("dettaglioSemantici");
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.DE)) {
			DettaglioDescrittoreGeneraleVO dettDesGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO();
			request.setAttribute("dettaglioDes", dettDesGenVO);
			resetToken(request);
			return mapping.findForward("dettaglioSemantici");
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TH)) {
			DettaglioTermineThesauroGeneraleVO dettTerThesGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTermineThesauroGeneraleVO();
			request.setAttribute("dettaglioTerThes", dettTerThesGenVO);
			resetToken(request);
			return mapping.findForward("dettaglioSemantici");
		}

		if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {
			DettaglioLuogoGeneraleVO dettLuoGenVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioLuogoGeneraleVO();
			request.setAttribute("dettaglioLuo", dettLuoGenVO);
			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("dettaglioLuogo"));
		}

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		if(Navigation.getInstance(request).isFromBar() ) {
			if (request.getAttribute("messaggio") != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage((String) request.getAttribute("messaggio")));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//almaviva5_20091005
			ActionMessages errors = LinkableTagUtils.getErrors(request);
			if (!errors.isEmpty() ) {
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		TreeElementViewTitoli treeElementView = analiticaTitoloForm.getTreeElementViewTitoli();

		if (analiticaTitoloForm.getRadioItemSelez() == null) {
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli) treeElementView
				.findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));

		if (analiticaTitoloForm.getEsaminaTitSelez().length() > 0) {
			String myForwardName = analiticaTitoloForm.getTabellaEsaminaVO().getMyForwardName(analiticaTitoloForm
					.getMyPath(), analiticaTitoloForm.getEsaminaTitSelez());
			String myForwardPath = analiticaTitoloForm.getTabellaEsaminaVO().getMyForwardPath(analiticaTitoloForm
					.getMyPath(), analiticaTitoloForm.getEsaminaTitSelez());


			// Modifica almaviva2 del 27.07.2010 - BUG MANTIS 3718 (se non valido rrestituisce "FALLITO" e non "")

//			if (myForwardPath.equals("")) {
			SbnAuthority elemAuth = elementoTree.getTipoAuthority();
			if (myForwardPath.equals("") || myForwardPath.equals("FALLITO")) {
				if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.PP)) {
					// Inizio gestione fuori standard per i Possessori che si agganciano all0'area del Documento Fisico;
					request.setAttribute("prov", "listaSuppInvPoss");
					request.setAttribute("codPolo", Navigation.getInstance(request).getUtente().getCodPolo());
					request.setAttribute("descr", elementoTree.getDescription());
					request.setAttribute("pid", elementoTree.getKey());
					request.setAttribute("codBib", Navigation.getInstance(request).getUtente().getCodBib());
					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("inventariPossessori"));
					// Fine gestione fuori standard per i Possessori che si agganciano all0'area del Documento Fisico;
				}
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			if (analiticaTitoloForm.getLivRicerca().equals("B")) {
				request.setAttribute(TitoliCollegatiInvoke.livDiRicerca, TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
				request.setAttribute(TitoliCollegatiInvoke.codBiblio, "011");
			}

			if (analiticaTitoloForm.getLivRicerca().equals("P")) {
				request.setAttribute(TitoliCollegatiInvoke.livDiRicerca, TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			}

			if (analiticaTitoloForm.getLivRicerca().equals("I")) {
				request.setAttribute(TitoliCollegatiInvoke.livDiRicerca, TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
			}

			int appoggio = 0;
			if (elemAuth == null) {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_TITOLO;
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.DE, SbnAuthority.RE)) {
				// Non sono gestiti gli esamina di titoli legati a descrittore/repertorio
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_AUTORE;
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.PP)) {
				// Inizio gestione fuori standard per i Possessori che si agganciano all0'area del Documento Fisico;
				request.setAttribute("prov", "listaSuppInvPoss");
				request.setAttribute("codPolo", Navigation.getInstance(request).getUtente().getCodPolo());
				request.setAttribute("descr", elementoTree.getDescription());
				request.setAttribute("pid", elementoTree.getKey());
				request.setAttribute("codBib", Navigation.getInstance(request).getUtente().getCodBib());
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("inventariPossessori"));
				// Fine gestione fuori standard per i Possessori che si agganciano all0'area del Documento Fisico;
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_MARCA;
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_LUOGO;
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.SO)) {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO;
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.CL)) {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE;
			} else {
				appoggio = TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_TITOLO;
			}

			request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, appoggio);
			request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, elementoTree.getKey());

			if (appoggio == TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO) {
				request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, elementoTree.getText());
			} else {
				request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, elementoTree.getDescription());
			}

			if (myForwardName.equals("ORDINI")) {
				ListaSuppOrdiniVO ricArr=new ListaSuppOrdiniVO();
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ricArr.setCodPolo(utenteCollegato.getCodPolo());
//				ricArr.setCodBibl(utenteCollegato.getCodBib()); TCK 3701
				ricArr.setChiamante(mapping.getPath());
				ricArr.setTitolo(new StrutturaCombo(elementoTree.getKey(),""));
				ricArr.setOrdinamento("8");
				request.setAttribute("passaggioListaSuppOrdiniVO",ricArr);
			}

			request.setAttribute(TitoliCollegatiInvoke.oggChiamante, analiticaTitoloForm.getMyPath());
			request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");

			request.setAttribute(TitoliCollegatiInvoke.naturaDiRicerca, elementoTree.getNatura());
			request.setAttribute(TitoliCollegatiInvoke.tipMatDiRicerca, elementoTree.getTipoMateriale());


			AreePassaggioSifVO areePassSifVo = new AreePassaggioSifVO();
			areePassSifVo.setOggettoRicerca(appoggio);
			areePassSifVo.setOggettoDaRicercare(elementoTree.getKey());
			areePassSifVo.setDescOggettoDaRicercare(elementoTree.getDescription());
			areePassSifVo.setTipMatOggetto(elementoTree.getTipoMateriale());
			areePassSifVo.setNaturaOggetto(elementoTree.getNatura());

			areePassSifVo.setOggettoChiamante(mapping.getPath());
			areePassSifVo.setVisualCall(true);

			request.setAttribute("AreePassaggioSifVO", areePassSifVo);
			resetToken(request);

			ActionForward actionForward = new ActionForward();
			actionForward.setName(myForwardName);
			actionForward.setPath(myForwardPath + "?SIFSINTETICA=TRUE");
			return Navigation.getInstance(request).goForward(actionForward);
		} else {
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblLisEsamina"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward confermaVaiA(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

 		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		analiticaTitoloForm.setVisualVaiA("SI");

 		if (Navigation.getInstance(request).isFromBar()) {
 			if (request.getParameter("RICARICA") != null) {
 				return unspecified(mapping, form, request, response);
 			} else {
 				return mapping.getInputForward();
 			}
 		}

		TreeElementViewTitoli treeElementView = analiticaTitoloForm.getTreeElementViewTitoli();

		if (request.getAttribute("messaggio") != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + request.getAttribute("messaggio")));
			this.saveErrors(request, errors);

			analiticaTitoloForm.setLivRicerca("I");
			request.setAttribute("livRicerca", "I");
			request.setAttribute("bid", analiticaTitoloForm.getBidRoot());
			analiticaTitoloForm.setVisualVaiA("SI");
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

			int codRit = impostaReticolo(request, analiticaTitoloForm);
			if (codRit < 0) {
				NavigationForward forward = Navigation.getInstance(request).goBack();
				forward.setRedirect(true);
				return forward;
			}
			boolean bidRoot = true;

			if (treeElementView.getTipoAuthority() == null) {
				analiticaTitoloForm.setGestioneInferiori("SI");
				caricaComboTitoloVaiA(request,analiticaTitoloForm, treeElementView,	bidRoot);
			}
			return impostazioniVaiA( mapping, form,  request, response);
		}

		if (analiticaTitoloForm.getRadioItemSelez() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		int combinazioni = 0;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAAcquisSelez() != null &&
				!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAAcquisSelez().equals(""))
			combinazioni = combinazioni + 1;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalSemantSelez() != null &&
				!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalSemantSelez().equals(""))
			combinazioni = combinazioni + 1;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalUnimarcSelez() != null &&
				!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalUnimarcSelez().equals(""))
			combinazioni = combinazioni + 1;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez() != null &&
				!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(""))
			combinazioni = combinazioni + 1;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestDocFisSelez() != null &&
				!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestDocFisSelez().equals(""))
			combinazioni = combinazioni + 1;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez() != null &&
                !analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez().equals(""))
			combinazioni = combinazioni + 1;
		// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPeriodiciSelez() != null &&
                !analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPeriodiciSelez().equals(""))
			combinazioni = combinazioni + 1;
		// Fine Modifica almaviva2 04.08.2010 - Gestione periodici



		if (combinazioni == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		} else if (combinazioni > 1) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selSoloUnaVoce"));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli) treeElementView
				.findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));


		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);


		// gestione delle aree diverse da Gestione Bibliografica

		// Solo per VOCE di Acquisizioni FUNZ_ACQUISIZIONE_CATTURA_A_SUGG_E_ORDINE il
		// controllo rimane all'area di Gestione Bibliografica che effettua la cattura a
		// Suggerimento d'acquisto e solo dopo passa il controllo all'Area della acquisizioni
		// con il codice del bid creato.

		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAAcquisSelez().equals(MenuHome.FUNZ_ACQUISIZIONE_CATTURA_A_SUGG_E_ORDINE)) {
			return catturaSuggerimentoAcquisto(mapping, request, analiticaTitoloForm, elementoTree, response);
		}

		GestioneVaiA gest = new GestioneVaiA();
		try {
			return gest.getAcquisizioneAction(request, mapping,	analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAAcquisSelez(), elementoTree);
		} catch (Exception ex) {
		}

		// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
		try {
			return gest.getPeriodiciAction(request, mapping, analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPeriodiciSelez(), elementoTree);
		} catch (Exception ex) {
		}
		// Fine Modifica almaviva2 04.08.2010 - Gestione periodici


		try {
			return gest.getSemanticaAction(request, mapping,
					analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalSemantSelez(), elementoTree, analiticaTitoloForm.getLivRicerca());
		} catch (Exception ex) {
		}


		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalUnimarcSelez() != null) {
			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalUnimarcSelez().equals(MenuHome.FUNZ_STAMPE_CATALOGRAFICHE)) {
				request.setAttribute("bid", elementoTree.getKey());
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("stampaSchedeCatalografiche"));
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalUnimarcSelez().equals(MenuHome.FUNZ_GEST_LEGAMI_TIT_EDI)) {
				List<TabellaNumSTDImpronteVO> listaNumStandard = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getListaNumStandard();
				TabellaNumSTDImpronteVO eleNumStandard = new TabellaNumSTDImpronteVO();
				String[] elencoISBN = new String[listaNumStandard.size()];
				int indIsbn=0;
				for (int index = 0; index < listaNumStandard.size(); index++) {
					eleNumStandard = (listaNumStandard.get(index));
					elencoISBN[indIsbn]= eleNumStandard.getCampoUno();
					indIsbn++;
				}
				request.setAttribute("bid", elementoTree.getKey());
				request.setAttribute("desc", elementoTree.getDescription());
				request.setAttribute("elencoISBN", elencoISBN);
				request.setAttribute("gestLegameTitEdit", "SI");
				request.setAttribute("editore", "SI");
				return Navigation.getInstance(request).goForward(mapping.findForward("gestionelegamiTitoloEditore"));
			} else {
				try {
					return gest.getStampeAction(request, mapping, analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiACatalUnimarcSelez(), elementoTree);
				} catch (Exception ex) {
				}
			}
		}

		try {
			return gest.getDocumentoFisicoAction(request, mapping, analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestDocFisSelez(), elementoTree);
		} catch (Exception ex) {
		}

		try {
			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez() != null) {
				if (!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez().equals(MenuHome.FUNZ_POSSESSORI_CANCELLAZIONE)
						&& !analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez().equals(MenuHome.FUNZ_POSSESSORI_SCAMBIO_FORMA) ) {
					 return gest.getPossessoriAction(request, mapping, analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez(), elementoTree);
				}
			}
	    } catch (Exception ex) {
	    }


		// Gestione delle aree di Gestione Bibliografica
		// Richiesta dettaglio radice del reticolo

		// =============================================================================================
		// CATTURA RETICOLO
		// =============================================================================================
		SbnAuthority elemAuth = elementoTree.getTipoAuthority();
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_RETICOLO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_AUTORE)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_MARCA)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_TITOLOUNIFORME)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_LUOGO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_INFERIORI)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO)) {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
			AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();

			areaDatiPassCattura.setIdPadre(elementoTree.getKey());
			if (elemAuth == null) {
				areaDatiPassCattura.setTipoAuthority("");
			} else {
				areaDatiPassCattura.setTipoAuthority(elemAuth.toString());
			}


			if (elementoTree.getTipoMateriale() != null) {
				areaDatiPassCattura.setTipoMateriale(elementoTree.getTipoMateriale().toString());
			}
			// almaviva2 - 14.04.2010 - MANTIS 3686 inserito nell'if controllo per copia reticolo
			if (!analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_RETICOLO)
					&& !analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO)) {
				if (analiticaTitoloForm.getCheckItemSelez() != null) {
					if (analiticaTitoloForm.getCheckItemSelez().length == 4000) {
						String[] appo = new String[0];
						areaDatiPassCattura.setInferioriDaCatturare(appo);
					} else {
						// almaviva2 Aprile 2015: Inserito if di controllo su presenza di nature diverse da M,W,N nei casi di
						// cattura inferiori (dato che ora per la nuova funzione di trascina legami titolo-autore i check sono
						// stati inseriti anche su legami diversi dai 463, 464)
						if (!verificaPresenzaSoloDocumenti(analiticaTitoloForm)) {
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selezNoDocPerCattAllin"));
							this.saveErrors(request, errors);
							Navigation.getInstance(request).setTesto("Notizia corrente");
							return mapping.getInputForward();
						}
						areaDatiPassCattura.setInferioriDaCatturare(creaListaBidPerCatturaInferiori(analiticaTitoloForm));
					}
				}
			}

			// Inizio Evolutive Google3 30.01.2014
			// Intervento finalizzato a consentire l'allineamento di un reticolo senza reinviare la localizzazione della biblioteca che
			// richiede l'allinea (quindi l'allinea on-line si deve comportare come quello batch)
			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals
																			(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_RETICOLO)) {
				areaDatiPassCattura.setProvenienzaAllineamento(true);
			}
			// Fine Evolutive Google3 30.01.2014


			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO)) {
				// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione nel caso di richiesta
				// di copia reticolo che necessita comunque della creazione in Polo
				try {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().CREA_1015);
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}
				// Fine Intervento per Google3:
				areaDatiPassCattura.setSoloCopia(true);
				areaDatiPassCattura.setCopiaReticolo(true);
				areaDatiPassCattura.setTipoMateriale(elementoTree.getTipoMateriale().toString());
			}

			try {
				if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_AUTORE)
						&& areaDatiPassCattura.getTipoAuthority().equals("AU")) {
					areaDatiPassReturnCattura = factory.getGestioneBibliografica().catturaAutore(areaDatiPassCattura,
							Navigation.getInstance(request).getUserTicket());
				} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_MARCA)
						&& areaDatiPassCattura.getTipoAuthority().equals("MA")) {
					areaDatiPassReturnCattura = factory.getGestioneBibliografica().catturaMarca(areaDatiPassCattura,
							Navigation.getInstance(request).getUserTicket());
				} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_LUOGO)
						&& areaDatiPassCattura.getTipoAuthority().equals("LU")) {
					areaDatiPassReturnCattura = factory.getGestioneBibliografica().catturaLuogo(areaDatiPassCattura,
							Navigation.getInstance(request).getUserTicket());
				} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_INFERIORI)) {
					if (areaDatiPassCattura.getInferioriDaCatturare() == null) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selezAlmenoUnInfer"));
						this.saveErrors(request, errors);
						Navigation.getInstance(request).setTesto("Notizia corrente");
						return mapping.getInputForward();
					} else {
						areaDatiPassReturnCattura = factory.getGestioneBibliografica().catturaReticolo(areaDatiPassCattura,
								Navigation.getInstance(request).getUserTicket());
					}
				} else {
					areaDatiPassReturnCattura = factory.getGestioneBibliografica().catturaReticolo(areaDatiPassCattura,
							Navigation.getInstance(request).getUserTicket());
				}
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo","ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			}

			if (areaDatiPassReturnCattura == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.findForward("interrogazioneTitolo");
			}

			if (areaDatiPassReturnCattura.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).setTesto("Notizia corrente");
			} else {
				// INIZIO DA PROVARE
				int codRit = impostaReticolo(request, analiticaTitoloForm);
				if (codRit < 0) {
					NavigationForward forward = Navigation.getInstance(request).goBack();
					forward.setRedirect(true);
					return forward;
				}

				if (areaDatiPassReturnCattura.getCodErr().equals("9999")
						|| areaDatiPassReturnCattura.getTestoProtocollo() != null) {
					ActionMessages errors = new ActionMessages();
//					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
//							areaDatiPassReturnCattura.getTestoProtocollo()));
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocolloNotCenter",
							areaDatiPassReturnCattura.getTestoProtocollo()));

					this.saveErrors(request, errors);
				} else if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica."
									+ areaDatiPassReturnCattura.getCodErr()));
					this.saveErrors(request, errors);
				}

				resetToken(request);
				return mapping.getInputForward();

			}
			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO)) {
				analiticaTitoloForm.setLivRicerca("P");
				request.setAttribute("livRicerca", "P");
			} else {
				analiticaTitoloForm.setLivRicerca("I");
				request.setAttribute("livRicerca", "I");
			}
			request.setAttribute("bid", areaDatiPassReturnCattura.getBid());
			analiticaTitoloForm.setVisualVaiA("SI");
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

			int codRit = impostaReticolo(request, analiticaTitoloForm);
			if (codRit < 0) {
				NavigationForward forward = Navigation.getInstance(request).goBack();
				forward.setRedirect(true);
				return forward;
			}
			boolean bidRoot = true;
			elementoTree = (TreeElementViewTitoli) treeElementView.getRoot();


			if (elemAuth == null) {
				analiticaTitoloForm.setGestioneInferiori("SI");
				caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree,	bidRoot);
			}
			return impostazioniVaiA( mapping, form,  request, response);
		}



		// =============================================================================================
		// almaviva2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
		// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
		// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
		// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals
						(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO_TIT_ANALITICI)) {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
			AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();

			areaDatiPassCattura.setIdPadre(elementoTree.getKey());
			areaDatiPassCattura.setTipoAuthority("");

			if (elementoTree.getTipoMateriale() != null) {
				areaDatiPassCattura.setTipoMateriale(elementoTree.getTipoMateriale().toString());
			}


			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione nel caso di richiesta
			// di copia reticolo che necessita comunque della creazione in Polo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().CREA_1015);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:
			areaDatiPassCattura.setSoloCopia(false);
			areaDatiPassCattura.setCopiaReticoloConSpoglioCondivisione(true);
			areaDatiPassCattura.setTipoMateriale(elementoTree.getTipoMateriale().toString());

			areaDatiPassCattura.setInferioriDaCatturare(creaListaBidPerSelezioneSpogli(analiticaTitoloForm));

			// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
			// Intervento almaviva2 gennaio 2017
			if (areaDatiPassCattura.getInferioriDaCatturare() == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noSpoglioDaCopiare"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}


			try {
				areaDatiPassReturnCattura = factory.getGestioneBibliografica().catturaReticolo(areaDatiPassCattura,
							Navigation.getInstance(request).getUserTicket());

			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo","ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			}

			if (areaDatiPassReturnCattura == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.findForward("interrogazioneTitolo");
			}

			if (areaDatiPassReturnCattura.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).setTesto("Notizia corrente");
			} else {

				int codRit = impostaReticolo(request, analiticaTitoloForm);
				if (codRit < 0) {
					NavigationForward forward = Navigation.getInstance(request).goBack();
					forward.setRedirect(true);
					return forward;
				}

				if (areaDatiPassReturnCattura.getCodErr().equals("9999")
						|| areaDatiPassReturnCattura.getTestoProtocollo() != null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocolloNotCenter",
							areaDatiPassReturnCattura.getTestoProtocollo()));

					this.saveErrors(request, errors);
				} else if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica."
									+ areaDatiPassReturnCattura.getCodErr()));
					this.saveErrors(request, errors);
				}

				resetToken(request);
				return mapping.getInputForward();

			}

			analiticaTitoloForm.setLivRicerca("I");
			request.setAttribute("livRicerca", "I");

			request.setAttribute("bid", areaDatiPassReturnCattura.getBid());
			analiticaTitoloForm.setVisualVaiA("SI");
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

			int codRit = impostaReticolo(request, analiticaTitoloForm);
			if (codRit < 0) {
				NavigationForward forward = Navigation.getInstance(request).goBack();
				forward.setRedirect(true);
				return forward;
			}
			boolean bidRoot = true;
			elementoTree = (TreeElementViewTitoli) treeElementView.getRoot();


			if (elemAuth == null) {
				analiticaTitoloForm.setGestioneInferiori("SI");
				caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree,	bidRoot);
			}
			return impostazioniVaiA( mapping, form,  request, response);
		}
		// =============================================================================================

		// =============================================================================================
		// CATTURA_A_SUGGERIMENTO DEL TITOLO
		// Viene aperta la funzionalità di variazione preimpostando tutti i dati dall'interrogazione
		// e viene poi catalogata
		// Solo la radice con la creazione di un nuovo identificativo
		// flag condiviso valorizzato a N
		// livello di Autorità = 01
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_A_SUGGERIMENTO)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione nel caso di richiesta
			// di copia notizia che necessita comunque della creazione in Polo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().CREA_1015);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:

			request.setAttribute("tipoProspettazione", "SUG");
			request.setAttribute("flagCondiviso", false);

			DettaglioTitoloCompletoVO dettTitComVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO();
			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", elementoTree.getDescription());
			resetToken(request);
			return mapping.findForward("dettaglioTitolo");
		}




//		 =============================================================================================
		// SCONDIVIDI OGGETTI
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_SCONDIVIDI)) {

			try{
				utenteEjb.isAbilitatoAuthority("AU");
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_AUTORE_1263);
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			DettaglioAutoreGeneraleVO dettAutVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO();

			try{
				utenteEjb.checkLivAutAuthority(elemAuth.toString(), Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			request.setAttribute("bidPerRientroAnalitica", analiticaTitoloForm.getBidRoot());
			request.setAttribute("tipoProspettazione", "AGGCOND");
			request.setAttribute("flagCondiviso", false);

			request.setAttribute("dettaglioAut", dettAutVO.clone());
			resetToken(request);
			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());
			return mapping.findForward("dettaglioAutore");

		}


		// =============================================================================================
		// CONDIVIDI OGGETTI - NUOVA VERSIONE REQUISITI SU DOCUMENTO RES. RIUNIONE 28.07.2009
		// Inizio BUG MANTIS 3344 - almaviva2 09.12.2009 - inserita in OR anche la voce di REINVIO_IN_INDICE
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_REINVIO_IN_INDICE)) {

			AreaTabellaOggettiDaCondividereVO areaTabellaOggettiDaCondividereVO = ricercaRadicePerCondivisione(elementoTree, request, analiticaTitoloForm);

			if (!areaTabellaOggettiDaCondividereVO.getCodErr().equals("0000")) {
				analiticaTitoloForm.setLivRicerca("P");
				analiticaTitoloForm.setVisualVaiA("SI");
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm
						.getTreeElementViewTitoli());

				boolean bidRoot = true;
				if (elemAuth == null) {
					analiticaTitoloForm.setGestioneInferiori("SI");
					caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree,	bidRoot);
				}
				resetToken(request);
				return mapping.getInputForward();
			}

			if (areaTabellaOggettiDaCondividereVO.getNumNotizie() > 0) {
				AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassaggioInterrogazioneTitoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSimili(true);
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setLivelloTrovato("I");
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setIdLista(areaTabellaOggettiDaCondividereVO.getIdLista());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSintetica(areaTabellaOggettiDaCondividereVO.getListaSintetica());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setMaxRighe(areaTabellaOggettiDaCondividereVO.getMaxRighe());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumBlocco(areaTabellaOggettiDaCondividereVO.getNumBlocco());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumNotizie(areaTabellaOggettiDaCondividereVO.getNumNotizie());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumPrimo(areaTabellaOggettiDaCondividereVO.getNumPrimo());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotBlocchi(areaTabellaOggettiDaCondividereVO.getTotBlocchi());
				areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotRighe(areaTabellaOggettiDaCondividereVO.getTotRighe());

				request.setAttribute("areaDatiPassReturnSintetica",	areaDatiPassaggioInterrogazioneTitoloReturnVO);

				if (elemAuth != null
						&& elemAuth.toString().equals("AU")) {
					AreaDatiVariazioneAutoreVO areaDatiVariazioneAutoreVO = new AreaDatiVariazioneAutoreVO();
					areaDatiVariazioneAutoreVO.setDettAutoreVO(elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioAutoreGeneraleVO());
					request.setAttribute("areaDatiPassPerConfVariazione", areaDatiVariazioneAutoreVO);

					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaAutoriPerCondividi"));
				} else {
					AreaDatiVariazioneTitoloVO areaDatiVariazioneTitoloVO = new AreaDatiVariazioneTitoloVO();
					areaDatiVariazioneTitoloVO.setDetTitoloPFissaVO(elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO());
					areaDatiVariazioneTitoloVO.setDetTitoloGraVO(elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloGraVO());
					areaDatiVariazioneTitoloVO.setDetTitoloMusVO(elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloMusVO());
					areaDatiVariazioneTitoloVO.setDetTitoloCarVO(elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloCarVO());
					request.setAttribute("areaDatiPassPerConfVariazione", areaDatiVariazioneTitoloVO);

					resetToken(request);
					return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerCondividi"));
				}

			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.cond002"));
				this.saveErrors(request, errors);
				analiticaTitoloForm.setVisualVaiA("NO");
				analiticaTitoloForm.setTipoOperazioneConferma("INVIOINDICE");
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}
		}

		// =============================================================================================
		// CONDIVIDI OGGETTI NON RADICE - NUOVA VERSIONE REQUISITI SU DOCUMENTO RES. RIUNIONE 28.07.2009
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION)) {

			// Maggio 2013-Bug Mantis (Collaudo) 5329: Se creo una M [loc] e uno spoglio N [loc] il sistema non deve consentire
			// di inviare in Indice lo spoglio come elemento del reticolo e, quindi, in Indice di registrare la N senza il legame
			// alla M sup. Il sistema si deve comportare come per le nature W
			// inserito or su tipo legame 464 (M su N)
			if (elementoTree.getDatiLegame().getTipoLegame().equals("463")
					|| elementoTree.getDatiLegame().getTipoLegame().equals("464")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.catalWImpossibile", elementoTree.getKey()));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			analiticaTitoloForm.setVisualVaiA("NO");
			analiticaTitoloForm.setAnaliticaAttiva(false);
			analiticaTitoloForm.setTipoOperazioneConferma("INVIOINDICE");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.ricConfermaInvioIndiceNoRoot"));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			resetToken(request);
			return mapping.getInputForward();

		}

//		// =============================================================================================
//		// CONDIVIDI ELEMENTI RETICOLO DI UN OGGETTO CATALOGATO ANCHE INDICE
//		// =============================================================================================
//		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
//				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO)) {
//			analiticaTitoloForm.setVisualVaiA("NO");
//			analiticaTitoloForm.setTipoOperazioneConferma("INVIOINDICE");
//			analiticaTitoloForm.setRinviaInIndice(true);
//			this.caricaListaEsamina(request, analiticaTitoloForm);
//			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
//			return mapping.getInputForward();
//		}

		// =============================================================================================
		// VARIAZIONE DESCRIZIONE
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_DESCRIZIONE)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CAMBIA_NATURA_BA_AB)) {

			request.setAttribute("bidPerRientroAnalitica", analiticaTitoloForm.getBidRoot());

			request.setAttribute("tipoProspettazione", "AGG");
			if (analiticaTitoloForm.getInterrogazioneVaiAForm()
					.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CAMBIA_NATURA_BA_AB)) {
				request.setAttribute("tipoProspettazione", "VARIANAT");
			}

			request.setAttribute("flagCondiviso", elementoTree.isFlagCondiviso());
			if (elemAuth == null
					|| elemAuth.toString().equals("TU")
					|| elemAuth.toString().equals("UM")) {

				/** VERIFICA ABILITAZIONE E LIVELLO AUTORITA'  */
				if (elemAuth == null) {
					try{
						utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							this.caricaListaEsamina(request, analiticaTitoloForm);
							resetToken(request);
							return mapping.getInputForward();
						}
						if (elementoTree.getTipoMateriale() != null
								&& !elementoTree.getTipoMateriale().equals("")
								&& !elementoTree.getTipoMateriale().equals(" ")) {


							// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo serve per gestire le abilitazioni alla sola parte
							// generale e bloccare la variazione dell'area delle specificita viene asteriscato e portato in dettaglio
//							try{
//								utenteEjb.isAbilitatoTipoMateriale(elementoTree.getTipoMateriale());
//								}catch(UtenteNotAuthorizedException ute)
//								{
//									ActionMessages errors = new ActionMessages();
//									errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.tipoMaterNotAuthorized", elementoTree.getTipoMateriale().toString()));
//									this.saveErrors(request, errors);
//									request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
//									this.caricaListaEsamina(request, analiticaTitoloForm);
//									resetToken(request);
//									return mapping.getInputForward();
//								}
								// Fine modifica BUG MANTIS 3551



							try{
								utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),
															Integer.valueOf(elementoTree.getLivelloAutorita()));
								}catch(UtenteNotAuthorizedException ute)
								{
									ActionMessages errors = new ActionMessages();
									errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
									this.saveErrors(request, errors);
									request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
									this.caricaListaEsamina(request, analiticaTitoloForm);
									resetToken(request);
									return mapping.getInputForward();
								}
						} else {
								try{
									utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),
																Integer.valueOf(elementoTree.getLivelloAutorita()));
									}catch(UtenteNotAuthorizedException ute)
									{
										ActionMessages errors = new ActionMessages();
										errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
										this.saveErrors(request, errors);
										request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
										this.caricaListaEsamina(request, analiticaTitoloForm);
										resetToken(request);
										return mapping.getInputForward();
									}

						}

				} else 	if (elemAuth.toString().equals("TU")) {
					try{
						utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_TITOLO_UNIFORME_1261);
						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							this.caricaListaEsamina(request, analiticaTitoloForm);
							resetToken(request);
							return mapping.getInputForward();
						}
						try{
							utenteEjb.checkLivAutAuthority(elemAuth.toString(),
														Integer.valueOf(elementoTree.getLivelloAutorita()));
							}catch(UtenteNotAuthorizedException ute)
							{
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
								this.saveErrors(request, errors);
								request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
								this.caricaListaEsamina(request, analiticaTitoloForm);
								resetToken(request);
								return mapping.getInputForward();
							}

				} else if (elemAuth.toString().equals("UM")) {
					try{
						utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_TITOLO_UNIFORME_MUSICA_1262);
						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							this.caricaListaEsamina(request, analiticaTitoloForm);
							resetToken(request);
							return mapping.getInputForward();
						}
						try{
							utenteEjb.checkLivAutAuthority(elemAuth.toString(),
														Integer.valueOf(elementoTree.getLivelloAutorita()));
							}catch(UtenteNotAuthorizedException ute)
							{
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
								this.saveErrors(request, errors);
								request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
								this.caricaListaEsamina(request, analiticaTitoloForm);
								resetToken(request);
								return mapping.getInputForward();
							}
				}


				DettaglioTitoloCompletoVO dettTitComVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO();
				request.setAttribute("dettaglioTit", dettTitComVO.clone());
				resetToken(request);
				request.setAttribute("bid", elementoTree.getKey());
				request.setAttribute("desc", elementoTree.getDescription());
				return mapping.findForward("dettaglioTitolo");

			} else if (elemAuth.toString().equals("AU")) {
				try{
					utenteEjb.isAbilitatoAuthority("AU");
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_AUTORE_1263);
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

				DettaglioAutoreGeneraleVO dettAutVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO();

				try{
					utenteEjb.checkLivAutAuthority(elemAuth.toString(), Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

				request.setAttribute("dettaglioAut", dettAutVO.clone());
				resetToken(request);
				request.setAttribute("bid", elementoTree.getKey());
				request.setAttribute("desc", elementoTree.getDescription());
				return mapping.findForward("dettaglioAutore");

			} else if (elemAuth.toString().equals("MA")) {

				try{
					utenteEjb.isAbilitatoAuthority("MA");
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_MARCA_1264);
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

				try{
					utenteEjb.checkLivAutAuthority(elemAuth.toString(),
							Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

				DettaglioMarcaGeneraleVO dettMarVO = elementoTree
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioMarcaGeneraleVO();
				request.setAttribute("dettaglioMar", dettMarVO.clone());
				resetToken(request);
				request.setAttribute("bid", elementoTree.getKey());
				request.setAttribute("desc", elementoTree.getDescription());
				return mapping.findForward("dettaglioMarca");
			} else if (elemAuth.toString().equals("LU")) {

				try{
					utenteEjb.isAbilitatoAuthority("LU");
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_LUOGO_1267);
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

				try{
					utenteEjb.checkLivAutAuthority(elemAuth.toString(),
							Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

				DettaglioLuogoGeneraleVO dettLuoVO = elementoTree
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioLuogoGeneraleVO();
				request.setAttribute("dettaglioLuo", dettLuoVO.clone());
				resetToken(request);
				request.setAttribute("bid", elementoTree.getKey());
				request.setAttribute("desc", elementoTree.getDescription());
				return mapping.findForward("dettaglioLuogo");
			} else {
				// POSIZIONAMENTO SU UN OGGETTO NON GESTITO DA GESTIONE
				// BIBLIOGRAFICA
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.gestSemantica", elementoTree.getKey()));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}
		}

		// =============================================================================================
		// VARIAZIONE Correzione nota ISBD
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CORREZIONE_NOTA_ISBD)) {

			try{
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			request.setAttribute("tipoProspettazione", "AGGNOTA");
			request.setAttribute("flagCondiviso", elementoTree.isFlagCondiviso());
			if (elemAuth == null) {
				DettaglioTitoloCompletoVO dettTitComVO = elementoTree
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO();
				request.setAttribute("dettaglioTit", dettTitComVO.clone());
				resetToken(request);
				request.setAttribute("bid", elementoTree.getKey());
				request.setAttribute("desc", elementoTree.getDescription());
				return mapping.findForward("dettaglioTitolo");
			} else {
				// POSIZIONAMENTO SU UN OGGETTO NON GESTITO DA GESTIONE
				// BIBLIOGRAFICA
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.gestSemantica",
						elementoTree.getKey()));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}

		}

		// =============================================================================================
		// VARIAZIONE VARIANTE MUTILO
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_VARIANTE_MUTILO)) {
			// FUNZIONE MOMENTANEAMENTE NON DISPONIBILE
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.funzNoDisp"));
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		// =============================================================================================
		//(Inizio 5Agosto2009)
		// FONDI OGGETTO LOCALE SU ELEMENTO CONDIVISO
		// =============================================================================================

		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
				MenuHome.FUNZ_BIBLIOGRAFICA_FONDI_ON_LINE)) {
			AreaDatiLegameTitoloVO areaDatiFusioneTitoloVO = new AreaDatiLegameTitoloVO();
			if (elemAuth == null
					|| (elemAuth.toString().equals("TU") ||
							elemAuth.toString().equals("UM"))) {
				// Fusione titolo/Authority TU-UM
				if (elemAuth != null) {
					areaDatiFusioneTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
				}
				areaDatiFusioneTitoloVO.setBidPartenza(elementoTree.getKey());
				// Modifica almaviva2 del 23.10.2009 tagliata area descrizione altrimenti non validazione protocollo
				if (elementoTree.getDescription() != null) {
					if (elementoTree.getDescription().length() > 80) {
						areaDatiFusioneTitoloVO.setDescPartenza(elementoTree.getDescription().substring(0,79));
					} else {
						areaDatiFusioneTitoloVO.setDescPartenza(elementoTree.getDescription());
					}
					// Intervento interno richiesto da  almaviva Aprile 2015
					// nel caso in cui si effettui la ricerca in Indice di un elemento per fusione per una natura W
					// si deve utilizzare il titolo della M superiore altrimenti la ricerca non andra' a buon fine;
					if (elementoTree.getNatura().equals("W")) {
						String descMsup="";
						int sizeTree = elementoTree.getChildren().size();
						for (int i=0; i < sizeTree; i++) {
							TreeElementViewTitoli elementoCorrente = (TreeElementViewTitoli) elementoTree.getChildren().get(i);
							if (elementoCorrente.getDatiLegame().getTipoLegame().equals("461")) {
								descMsup = elementoCorrente.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getAreaTitTitolo();
								if (descMsup.length() > 80) {
									areaDatiFusioneTitoloVO.setDescPartenzaMsuperiore(descMsup.substring(0,79));
								} else {
									areaDatiFusioneTitoloVO.setDescPartenzaMsuperiore(descMsup);
								}
								break;
							}
						}
					} else {
						areaDatiFusioneTitoloVO.setDescPartenzaMsuperiore("");
					}
				}

				areaDatiFusioneTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
				areaDatiFusioneTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());

				// Inizio Modifica per fusione di TU che non hanno esplicitato il tipoMateriale adeguativa all'Indice per coprire
				// casistica di tp_record = null - 07 gennaio 2016 almaviva2
				// areaDatiFusioneTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
				if (elementoTree.getTipoMateriale() != null) {
					areaDatiFusioneTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
				} else {
					if (elemAuth != null && elemAuth.toString().equals("UM")) {
						areaDatiFusioneTitoloVO.setTipMatBidPartenza("U");
					} else {
						areaDatiFusioneTitoloVO.setTipMatBidPartenza("");
					}
				}
				// Fine Modifica per fusione di TU

				areaDatiFusioneTitoloVO.setTipoDataPubblPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getTipoData());
				areaDatiFusioneTitoloVO.setData1DaPubblPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getDataPubbl1());

				if (elemAuth != null) {
					areaDatiFusioneTitoloVO.setTimeStampBidPartenza(elementoTree.getT005());
				} else {
					if (elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getListaNumStandard().size() == 1) {
						TabellaNumSTDImpronteVO tabellaNumSTDImpronteVO = new TabellaNumSTDImpronteVO();
						tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getListaNumStandard().get(0);
						areaDatiFusioneTitoloVO.setValNumStandardPartenza(tabellaNumSTDImpronteVO.getCampoUno());
						areaDatiFusioneTitoloVO.setTipNumStandardPartenza(tabellaNumSTDImpronteVO.getCampoDue());
					}
					if (elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getListaImpronte().size() == 1) {
						TabellaNumSTDImpronteVO tabellaNumSTDImpronteVO = new TabellaNumSTDImpronteVO();
						tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getListaImpronte().get(0);
						areaDatiFusioneTitoloVO.setImprontaPartenza(tabellaNumSTDImpronteVO.getCampoDue());
					}

					areaDatiFusioneTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
				}

				areaDatiFusioneTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());

				areaDatiFusioneTitoloVO.setTipoOperazione("Fondi");
				request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiFusioneTitoloVO);
				return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloTitolo"));
			} else if (elemAuth.toString() != null
					&& elemAuth.toString().equals("AU")) {
				areaDatiFusioneTitoloVO.setAuthorityOggettoPartenza("AU");
				areaDatiFusioneTitoloVO.setBidPartenza(elementoTree.getKey());
				areaDatiFusioneTitoloVO.setDescPartenza(elementoTree.getDescription());
				areaDatiFusioneTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioAutoreGeneraleVO().getLivAut());
				areaDatiFusioneTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioAutoreGeneraleVO().getVersione());
				areaDatiFusioneTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
				areaDatiFusioneTitoloVO.setTipoOperazione("Fondi");
				request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiFusioneTitoloVO);
				return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloAutore"));
			} else if (elemAuth.toString() != null
					&& elemAuth.toString().equals("MA")) {
				areaDatiFusioneTitoloVO.setAuthorityOggettoPartenza("MA");
				areaDatiFusioneTitoloVO.setBidPartenza(elementoTree.getKey());
				areaDatiFusioneTitoloVO.setDescPartenza(elementoTree.getDescription());
				areaDatiFusioneTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioMarcaGeneraleVO().getLivAut());
				areaDatiFusioneTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioMarcaGeneraleVO().getVersione());
				areaDatiFusioneTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
				areaDatiFusioneTitoloVO.setTipoOperazione("Fondi");
				request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiFusioneTitoloVO);
				return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloMarca"));
			} else if (elemAuth.toString() != null
					&& elemAuth.toString().equals("LU")) {
				areaDatiFusioneTitoloVO.setAuthorityOggettoPartenza("LU");
				areaDatiFusioneTitoloVO.setBidPartenza(elementoTree.getKey());
				areaDatiFusioneTitoloVO.setDescPartenza(elementoTree.getDescription());
				areaDatiFusioneTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioLuogoGeneraleVO().getLivAut());
				areaDatiFusioneTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioLuogoGeneraleVO().getVersione());
				areaDatiFusioneTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
				areaDatiFusioneTitoloVO.setTipoOperazione("Fondi");
				request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiFusioneTitoloVO);
				return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloLuogo"));
			}
		}
		//(Fine 5Agosto2009)

		// =============================================================================================
		// GESTIONE LEGAMI
		// =============================================================================================
		AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();


		// =============================================================================================
		// INSERIMENTO LEGAMI TITOLO-TITOLO o TITOLOUNIFORME-TITOLO
		// =============================================================================================

		// Inizio intervento almaviva2 - mail Scognamiglio 12.06.2012
		//.Inserita nuova voce nel vai a per gestire la creazione del legame di una Monografia condivisa con una raccolta locale

		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
						MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_TITOLO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
						MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_RACCOLTA)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
						MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
								MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLOUNIFORME)) {

			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
					MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_TITOLO)
					|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
						MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_RACCOLTA)) {

				// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
				// di creazione nuovo legame che modifica oggetto di partenza del reticolo
				try {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}
				// Fine Intervento per Google3:

				try{
					utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),	Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
					MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLO)
					|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
							MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLOUNIFORME)) {
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
				// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
				// di creazione nuovo legame che modifica oggetto di partenza del reticolo
				try {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_TITOLO_UNIFORME_1261);
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}
				// Fine Intervento per Google3:

				try{
					utenteEjb.checkLivAutAuthority(elemAuth.toString(),
								Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			}



			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getT005());
			} else {
				areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			}
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());

			areaDatiLegameTitoloVO.setTipoOperazione("Crea");

			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
						MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_RACCOLTA)) {
				areaDatiLegameTitoloVO.setNaturaBidArrivo("R");
			}

			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloTitolo"));

		}


		//
		// =============================================================================================
		// INSERIMENTO LEGAMI 51 TITOLO con W o M (volumi)
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_VOLUMI_INFERIORI)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
			// di creazione nuovo legame che modifica oggetto di partenza del reticolo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:

			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());

			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("tipoProspettazione", "INS");
			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			if (elementoTree.isFlagCondiviso()) {
				return mapping.findForward("creaTitoloPartecipato51");
			} else {
				return mapping.findForward("creaTitoloLocale51");
			}
		}

		// =============================================================================================
		// INSERIMENTO LEGAMI 51 TITOLO con N (spogli)
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO_LOCALE)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
			// di creazione nuovo legame che modifica oggetto di partenza del reticolo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:

			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());

			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("tipoProspettazione", "INS");
			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);

			// Inizio Dicembre 2015 almaviva2 : in fase di creazione di uno spoglio sotto una M si porta tutto il dettaglio
			// del titolo madre così da preimpostare tutti i campi della N che salvo alcune rare eccezioni saranno gli stessi
			request.setAttribute("dettaglioTitMadre", elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO());
			// Fine Dicembre 2015 almaviva2

			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());



			// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
			// qui va inserita le ricerca titolo come nella normale strada del lega titolo ed asteriscata la chiamata diretta al crea.
			// viene inoltre preimpostata la natura N per facilitare l'operato del bibliotecario
			// vengono impostati i flag di condivisione per sapere se la richietsa era spoglio locale o spoglio condiviso
			// Manutenzione Fuori Mantis: almaviva2 ottobre 2015 la nuova funzionalità del copia spoglio vale solo per
			// i documenti di natura "M" e non di natura "S"
			if (elementoTree.getNatura().equals("M")) {
				areaDatiLegameTitoloVO.setNaturaBidArrivo("N");
				if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO_LOCALE)) {
					areaDatiLegameTitoloVO.setFlagCondivisoArrivo(false);
					areaDatiLegameTitoloVO.setFlagCondivisoLegame(false);
				} else {
					areaDatiLegameTitoloVO.setFlagCondivisoArrivo(true);
					areaDatiLegameTitoloVO.setFlagCondivisoLegame(true);
				}
				request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
				return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloTitolo"));
			} else {
				if (elementoTree.isFlagCondiviso()) {
					if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO_LOCALE)) {
						return mapping.findForward("creaSpoglioLocale51");
					} else {
						return mapping.findForward("creaSpoglioPartecipato51");
					}
				} else {
					return mapping.findForward("creaSpoglioLocale51");
				}
			}
			// Fine Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
		}


		//


		// =============================================================================================
		// INSERIMENTO LEGAMI TITOLO-INFERIORE
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_INFERIORE)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
			// di creazione nuovo legame che modifica oggetto di partenza del reticolo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:


			try{
				utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),	Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",
					areaDatiLegameTitoloVO);
			return mapping.findForward("gestionePerLegamiTitoloInferiore");
		}

		// =============================================================================================
		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		// INSERIMENTO LEGAMI TITOLOUNIFORME-RIVIO (A438V)
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_RINVIO)) {
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_TITOLO_UNIFORME_1261);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}

			try{

				utenteEjb.checkLivAutAuthority(elemAuth.toString(),	Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setNaturaBidArrivo("V");
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("tipoProspettazione", "INS");
			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
//				if (elementoTree.isFlagCondiviso()) {
//					return mapping.findForward("creaTitoloPartecipato51");
//				} else {
//					return mapping.findForward("creaTitoloLocale51");
//				}
			return mapping.findForward("gestionePerLegamiTitoloUniRinvio");
		}

		// =============================================================================================
		// INSERIMENTO LEGAMI TITOLO-AUTORE
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_AUTORE)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_AUTORE)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
			// di creazione nuovo legame che modifica oggetto di partenza del reticolo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:

			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
					MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_AUTORE)) {
				try{
					utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),	Integer.valueOf(elementoTree.getLivelloAutorita()));
					utenteEjb.isAbilitatoLegameTitoloAuthority("AU");
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
					MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_AUTORE)) {
				try{
					utenteEjb.checkLivAutAuthority(elemAuth.toString(),	Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			}


			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getT005());
			} else {
				areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			}
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloAutore"));
		}


		// =============================================================================================
		// SCAMBIO LEGAME FRA AUTORE PRINCIPALE E AUTORE ALTERNATIVO CON IL TITOLO
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIA_LEGAME_AUTORE)) {

		try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			try{
				utenteEjb.checkLivAutAuthority(elemAuth.toString(),Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


				if (analiticaTitoloForm.getConfermaCanc() == null) {
					analiticaTitoloForm.setConfermaCanc("NO");
				}

				if (analiticaTitoloForm.getConfermaCanc().equals("NO")) {
					analiticaTitoloForm.setConfermaCanc("SI");
					analiticaTitoloForm.setAnaliticaAttiva(false);
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricConfermaScambioLegameTitAut",
							elementoTree.getKey()));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				} else {
					analiticaTitoloForm.setConfermaCanc("NO");
					analiticaTitoloForm.setAnaliticaAttiva(true);
				}




				// POSIZIONAMENTO SU UN OGGETTO DI TIPO AUTORE
				// E' NECASSARIO INSERIRE LA PARTE DEI DATI OLD PERCHE' LA MODIFICA DI QUESTO TIPO DI LEGAME CONSISTE IN CANCELLA-INSERISCI
				// L'areaDatiLegameTitolo21VO contiene il cambio di tipo-legame dell'autore alternativo in quello principale ( da 2 a 1)
				// L'areaDatiLegameTitolo12VO contiene il cambio di tipo-legame dell'autore principale in quello alternativo ( da 1 a 2)

				AreaDatiScambiaResponsLegameTitAutVO areaDatiPass = new AreaDatiScambiaResponsLegameTitAutVO();
				areaDatiPass.setTipoOperazione("Scambio");
				AreaDatiLegameTitoloVO areaDatiLegameTitolo21VO = new AreaDatiLegameTitoloVO();
				AreaDatiLegameTitoloVO areaDatiLegameTitolo12VO = new AreaDatiLegameTitoloVO();

				areaDatiLegameTitolo21VO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitolo21VO.setDescArrivo(elementoTree.getDescription());
				areaDatiLegameTitolo21VO.setAuthorityOggettoArrivo("AU");
				areaDatiLegameTitolo21VO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());

				areaDatiLegameTitolo21VO.setRelatorCodeOld(elementoTree.getDatiLegame().getRelatorCode());
				areaDatiLegameTitolo21VO.setTipoResponsOld(elementoTree.getDatiLegame().getResponsabilita());
				areaDatiLegameTitolo21VO.setSuperfluoOld(elementoTree.getDatiLegame().isSuperfluo());
				areaDatiLegameTitolo21VO.setIncertoOld(elementoTree.getDatiLegame().isIncerto());
				areaDatiLegameTitolo21VO.setTipoLegameOld(elementoTree.getDatiLegame().getTipoLegame());
				areaDatiLegameTitolo21VO.setNoteLegameOld(elementoTree.getDatiLegame().getNotaLegame());

				areaDatiLegameTitolo21VO.setRelatorCodeNew(elementoTree.getDatiLegame().getRelatorCode());
				areaDatiLegameTitolo21VO.setTipoResponsNew("1");
				areaDatiLegameTitolo21VO.setSuperfluoNew(elementoTree.getDatiLegame().isSuperfluo());
				areaDatiLegameTitolo21VO.setIncertoNew(elementoTree.getDatiLegame().isIncerto());
				areaDatiLegameTitolo21VO.setTipoLegameNew(elementoTree.getDatiLegame().getTipoLegame());
				areaDatiLegameTitolo21VO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());

				areaDatiLegameTitolo21VO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
				if (areaDatiLegameTitolo21VO.isFlagCondivisoLegame()) {
					areaDatiPass.setInserimentoIndice(true);
					areaDatiPass.setInserimentoPolo(true);
				} else {
					areaDatiPass.setInserimentoIndice(false);
					areaDatiPass.setInserimentoPolo(true);
				}


				// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-autore
				areaDatiLegameTitolo21VO.setBidPartenza(elementoTree.getParent().getKey());
				areaDatiLegameTitolo21VO.setDescPartenza(elementoTree.getParent().getDescription());
				areaDatiLegameTitolo21VO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
								.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
				areaDatiLegameTitolo21VO.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getNatura());
				areaDatiLegameTitolo21VO.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getTipoMateriale());
				areaDatiLegameTitolo21VO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
								.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
				areaDatiLegameTitolo21VO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
				areaDatiLegameTitolo21VO.setTipoOperazione("Modifica");

				// Ricerca dell'autore principale del titolo a cui è agganciato l'eutore selezionato
				TreeElementViewTitoli elementViewTitoliPadre = new TreeElementViewTitoli();
				elementViewTitoliPadre = (TreeElementViewTitoli) elementoTree.getParent();


				for (int i=0; i<elementViewTitoliPadre.getChildren().size(); i++) {

					TreeElementViewTitoli treeElementViewFiglio = (TreeElementViewTitoli) elementViewTitoliPadre.getChildren().get(i);
					if (treeElementViewFiglio.getTipoAuthority() != null) {
						if (treeElementViewFiglio.getTipoAuthority().toString().equals("AU")
								&& treeElementViewFiglio.getDatiLegame().getResponsabilita().equals("1")) {

							areaDatiLegameTitolo12VO.setIdArrivo(treeElementViewFiglio.getKey());
							areaDatiLegameTitolo12VO.setDescArrivo(treeElementViewFiglio.getDescription());
							areaDatiLegameTitolo12VO.setAuthorityOggettoArrivo("AU");
							areaDatiLegameTitolo12VO.setFlagCondivisoArrivo(treeElementViewFiglio.isFlagCondiviso());

							areaDatiLegameTitolo12VO.setRelatorCodeOld(treeElementViewFiglio.getDatiLegame().getRelatorCode());
							areaDatiLegameTitolo12VO.setTipoResponsOld(treeElementViewFiglio.getDatiLegame().getResponsabilita());
							areaDatiLegameTitolo12VO.setSuperfluoOld(treeElementViewFiglio.getDatiLegame().isSuperfluo());
							areaDatiLegameTitolo12VO.setIncertoOld(treeElementViewFiglio.getDatiLegame().isIncerto());
							areaDatiLegameTitolo12VO.setTipoLegameOld(treeElementViewFiglio.getDatiLegame().getTipoLegame());
							areaDatiLegameTitolo12VO.setNoteLegameOld(treeElementViewFiglio.getDatiLegame().getNotaLegame());

							areaDatiLegameTitolo12VO.setRelatorCodeNew(treeElementViewFiglio.getDatiLegame().getRelatorCode());
							areaDatiLegameTitolo12VO.setTipoResponsNew("2");
							areaDatiLegameTitolo12VO.setSuperfluoNew(treeElementViewFiglio.getDatiLegame().isSuperfluo());
							areaDatiLegameTitolo12VO.setIncertoNew(treeElementViewFiglio.getDatiLegame().isIncerto());
							areaDatiLegameTitolo12VO.setTipoLegameNew(treeElementViewFiglio.getDatiLegame().getTipoLegame());
							areaDatiLegameTitolo12VO.setNoteLegameNew(treeElementViewFiglio.getDatiLegame().getNotaLegame());

							areaDatiLegameTitolo12VO.setFlagCondivisoLegame(treeElementViewFiglio.getDatiLegame().isFlagCondiviso());

							// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-autore
							areaDatiLegameTitolo12VO.setBidPartenza(treeElementViewFiglio.getParent().getKey());
							areaDatiLegameTitolo12VO.setDescPartenza(treeElementViewFiglio.getParent().getDescription());
							areaDatiLegameTitolo12VO.setLivAutBidPartenza(((TreeElementViewTitoli) treeElementViewFiglio.getParent())
											.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
							areaDatiLegameTitolo12VO.setNaturaBidPartenza(((TreeElementViewTitoli) treeElementViewFiglio.getParent()).getNatura());
							areaDatiLegameTitolo12VO.setTipMatBidPartenza(((TreeElementViewTitoli) treeElementViewFiglio.getParent()).getTipoMateriale());
							areaDatiLegameTitolo12VO.setTimeStampBidPartenza(((TreeElementViewTitoli) treeElementViewFiglio.getParent())
											.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
							areaDatiLegameTitolo12VO.setFlagCondivisoPartenza(treeElementViewFiglio.getParent().isFlagCondiviso());
							areaDatiLegameTitolo12VO.setTipoOperazione("Modifica");
						}
					}
				}

				if (areaDatiLegameTitolo12VO.getIdArrivo() == null || areaDatiLegameTitolo12VO.getIdArrivo().equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


				areaDatiPass.setAreaDatiLegameTitolo12VO(areaDatiLegameTitolo12VO);
				areaDatiPass.setAreaDatiLegameTitolo21VO(areaDatiLegameTitolo21VO);

				AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				try {
					areaDatiPassReturn = factory.getGestioneBibliografica().scambiaResponsabilitaLegameTitoloAutore(areaDatiPass,
							Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>"	+ e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					this.caricaListaEsamina(request, analiticaTitoloForm);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				}
				if (areaDatiPassReturn == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
					this.saveErrors(request, errors);
				}

				if (areaDatiPassReturn.getCodErr().equals("0000")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
					this.saveErrors(request, errors);
				}

				if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",	areaDatiPassReturn.getTestoProtocollo()));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

		}



		// =============================================================================================
		// INSERIMENTO LEGAMI TITOLO-MARCA
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_MARCA)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
			// di creazione nuovo legame che modifica oggetto di partenza del reticolo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:

			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("MA");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			try{
				utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),
						Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloMarca"));
		}

		// =============================================================================================
		// INSERIMENTO LEGAMI TITOLO-LUOGO
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_LUOGO)) {

			// Inizio Intervento per Google3: controllo sull'abilitazione alla creazione/modifica nel caso di richiesta
			// di creazione nuovo legame che modifica oggetto di partenza del reticolo
			try {
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
			// Fine Intervento per Google3:

			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("LU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(),
						Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}



			if (elemAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(elemAuth.toString());
			}
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(elementoTree.getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(elementoTree.getTipoMateriale());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiTitoloLuogo"));
		}

		// =============================================================================================
		// INSERIMENTO LEGAMI AUTORE(ente)-AUTORE(ente)
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORI)) {

			try{
				utenteEjb.isAbilitatoAuthority("AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			try{
				utenteEjb.checkLivAutAuthority(elemAuth.toString(),Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioAutoreGeneraleVO().getLivAut());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioAutoreGeneraleVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiAutoreAutore"));

		}

		// =============================================================================================
		// INSERIMENTO LEGAMI LUOGO-LUOGO
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_LUOGHI)) {

			try{
				utenteEjb.isAbilitatoAuthority("LU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("LU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.checkLivAutAuthority(elemAuth.toString(),
							Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("LU");
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioLuogoGeneraleVO().getLivAut());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioLuogoGeneraleVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiLuogoLuogo"));

		}




		// =============================================================================================
		// INSERIMENTO LEGAMI AUTORE(ente)-MARCA
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORE_MARCA)) {

			try{
				utenteEjb.isAbilitatoAuthority("AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("MA");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.checkLivAutAuthority(elemAuth.toString(),
							Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioAutoreGeneraleVO().getLivAut());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioAutoreGeneraleVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiAutoreMarca"));

		}

		// =============================================================================================
		// INSERIMENTO LEGAMI MARCA - AUTORE(ente
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_MARCA_AUTORE)) {

			try{
				utenteEjb.isAbilitatoAuthority("MA");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.isAbilitatoLegameTitoloAuthority("AU");
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noAbilitaLegamiDoc"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}


			try{
				utenteEjb.checkLivAutAuthority(elemAuth.toString(),
							Integer.valueOf(elementoTree.getLivelloAutorita()));
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.livAutInsuff"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}

			areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("MA");
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioMarcaGeneraleVO().getLivAut());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
					.getDettaglioMarcaGeneraleVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return Navigation.getInstance(request).goForward(mapping.findForward("interrogazionePerLegamiMarcaAutore"));

		}


		// =============================================================================================
		// GESTIONE FORMA DI RINVIO AUTORE E LUOGO
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_FORMA_RINVIO)) {


			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Crea");
			request.setAttribute("AreaDatiLegameTitoloVO",areaDatiLegameTitoloVO);

			if (elemAuth.toString().equals("AU")) {

				try{
					utenteEjb.isAbilitatoAuthority("AU");
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
				areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioAutoreGeneraleVO().getLivAut());
				areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioAutoreGeneraleVO().getVersione());
				return mapping.findForward("gestionePerLegamiAutoreRinvio");
			}
			if (elemAuth.toString().equals("LU")) {

				try{
					utenteEjb.isAbilitatoAuthority("LU");
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}


				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("LU");
				areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioLuogoGeneraleVO().getLivAut());
				areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioLuogoGeneraleVO().getVersione());
				return mapping.findForward("gestionePerLegamiLuogoRinvio");
			}
		}

		// =============================================================================================
		// VARIAZIONE LEGAMI GIA' PRESENTE
		// =============================================================================================
		SbnAuthority parentAuth = elementoTree.getParent() != null ? elementoTree.getParent().getTipoAuthority() : null;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_LEGAME)) {

			if (!verificaLivelloAutorita()) {
			}

			if (elemAuth == null
					|| SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {

				// POSIZIONAMENTO SU UN OGGETTO DI TIPO TITOLO
				ActionForward actionForward = impostaAreaPerVariaLegameTitolo(mapping, request, elementoTree, analiticaTitoloForm.getBidRoot(), areaDatiLegameTitoloVO);
				if (actionForward != null) {
					return actionForward;
				}

			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				// POSIZIONAMENTO SU UN OGGETTO DI TIPO AUTORE
				// E' NECASSARIO INSERIRE LA PARTE DEI DATI OLD PERCHE' LA MODIFICA DI QUESTO
				// TIPO DI LEGAME CONSISTE IN CANCELLA-INSERISCI
				ActionForward actionForward = impostaAreaPerVariaLegameAutore(mapping, request, elementoTree, analiticaTitoloForm.getBidRoot(), areaDatiLegameTitoloVO);
				if (actionForward != null) {
					return actionForward;
				}

			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
				// POSIZIONAMENTO SU UN OGGETTO DI TIPO MARCA
				areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitoloVO.setDescArrivo(elementoTree.getDescription());
				areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("MA");
				areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
				areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());
				areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
				if (parentAuth == null
						|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
					// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-marca
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree	.getParent().getDescription());
					areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
					areaDatiLegameTitoloVO.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getNatura());
					areaDatiLegameTitoloVO.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getTipoMateriale());
					areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
					request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiTitoloMarca");
				} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.AU)) {
					// OGGETTO PADRE E' UN AUTORE - variazione legame editore-marca
					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
					request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiFraAutority");
				}
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {

				//================================================================

				// POSIZIONAMENTO SU UN OGGETTO DI TIPO LUOGO
				areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitoloVO.setDescArrivo(elementoTree.getDescription());
				areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("LU");
				areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
				areaDatiLegameTitoloVO.setRelatorCodeNew(elementoTree.getDatiLegame().getRelatorCode());
				areaDatiLegameTitoloVO.setTipoResponsNew(elementoTree.getDatiLegame().getResponsabilita());

				areaDatiLegameTitoloVO.setSuperfluoNew(elementoTree.getDatiLegame().isSuperfluo());
				areaDatiLegameTitoloVO.setIncertoNew(elementoTree.getDatiLegame().isIncerto());

				areaDatiLegameTitoloVO.setTipoLegameNew(elementoTree.getDatiLegame().getTipoLegame());
				areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());
				areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
				if (parentAuth == null
						|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
					// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-luogo
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
									.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
					areaDatiLegameTitoloVO.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getNatura());
					areaDatiLegameTitoloVO.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getTipoMateriale());
					areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
									.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
					request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiTitoloLuogo");
				} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.LU)) {
					// OGGETTO PADRE E' UNA LUOGO - variazione legameluogo-luogo
					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("LU");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
									.getAreaDatiDettaglioOggettiVO().getDettaglioLuogoGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
									.getAreaDatiDettaglioOggettiVO().getDettaglioLuogoGeneraleVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
					request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiFraAutority");
				}

				// POSIZIONAMENTO SU UN OGGETTO DI TIPO LUOGO
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo",
						"FUNZIONE ATTUALMENTE NON DISPONIBILE"));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			} else {
				// POSIZIONAMENTO SU UN OGGETTO NON GESTITO DA GESTIONE
				// BIBLIOGRAFICA
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.gestSemantica",
						elementoTree.getKey()));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}
		}
		// =============================================================================================
		// FINE VARIAZIONE LEGAMI GIA' PRESENTE
		// =============================================================================================


		// =============================================================================================
		// INIZIO FUNZIONE DI TRASCINAMENTO DI LEGAMI TITOLO-AUTORE
		// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: trascinamento legami autore ma M a N (esempio disco M con tracce N)
		// =============================================================================================
		parentAuth = elementoTree.getParent() != null ? elementoTree.getParent().getTipoAuthority() : null;
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_TRASCINA_LEGAME_AUTORE)) {

			if (!verificaLivelloAutorita()) {
			}

			// Passo 1: verifica che siano state effettuate le selezioni degli spogli su cui trascinare i legami
			if (analiticaTitoloForm.getCheckItemSelez() != null) {
				if (analiticaTitoloForm.getCheckItemSelez().length >= 4000) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selezTroppiInferPerTrasc"));
					this.saveErrors(request, errors);
					Navigation.getInstance(request).setTesto("Notizia corrente");
					return mapping.getInputForward();
				} else {
					if (verificaPresenzaDocUniformi(analiticaTitoloForm)) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selezDocUnifPerTrasc"));
						this.saveErrors(request, errors);
						Navigation.getInstance(request).setTesto("Notizia corrente");
						return mapping.getInputForward();
					}
					areaDatiLegameTitoloVO.setInferioriDaCatturare(creaListaBidPerCatturaInferiori(analiticaTitoloForm));
				}
			}
			if (areaDatiLegameTitoloVO.getInferioriDaCatturare() == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selezAlmenoUnInferPerTrasc"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).setTesto("Notizia corrente");
				return mapping.getInputForward();
			}


			// Passo 2: passaggio dei dati per impostare la maschera successiva dell'Autore e degli attributi del legame
			areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
			areaDatiLegameTitoloVO.setDescArrivo(elementoTree.getDescription());
			areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("AU");
			areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoResponsNew(elementoTree.getDatiLegame().getResponsabilita());
			areaDatiLegameTitoloVO.setRelatorCodeNew(elementoTree.getDatiLegame().getRelatorCode());
			areaDatiLegameTitoloVO.setSpecStrumVociNew(elementoTree.getDatiLegame().getSpecStrumVoci());
			areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());
			areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());

			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
			if (analiticaTitoloForm.getBidRoot() != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree	.getParent().getDescription());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("TrascinaLegameAutore");
			request.setAttribute("AreaDatiLegameTitoloVO",	areaDatiLegameTitoloVO);
			return mapping.findForward("variazionePerLegamiTitoloAutore");

		}

		// =============================================================================================
		// FINE FUNZIONE DI TRASCINAMENTO DI LEGAMI TITOLO-AUTORE
		// =============================================================================================


		// =============================================================================================
		// CANCELLAZIONE ELEMENTO DI AUTHORITY
		// =============================================================================================

		// Modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione cpme vecchia Gestione 51
		// inserito if e gestione per la voce di FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI

		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_AUTORE)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_MARCA)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LUOGO)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestPossessoriSelez().equals(MenuHome.FUNZ_POSSESSORI_CANCELLAZIONE)) {


			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_AUTORE)) {
				try{
					utenteEjb.isAbilitatoAuthority("AU");
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "AU");
					utenteEjb.checkLivAutAuthority("AU", Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LUOGO)) {
				try{
					utenteEjb.isAbilitatoAuthority("LU");
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "LU");
					utenteEjb.checkLivAutAuthority("LU", Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO)
					&& elemAuth.toString().equals("AU")) {
				try{
					utenteEjb.isAbilitatoAuthority("AU");
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "AU");
					utenteEjb.checkLivAutAuthority("AU", Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO)
					&& elemAuth.toString().equals("LU")) {
				try{
					utenteEjb.isAbilitatoAuthority("LU");
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "LU");
					utenteEjb.checkLivAutAuthority("LU", Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
					// febbraio 2018 - almaviva2 - Piccola manutenzione per cancellazione forma rinvio del TU
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO)
					&& elemAuth.toString().equals("TU")) {
				try{
					utenteEjb.isAbilitatoAuthority("TU");
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "TU");
					utenteEjb.checkLivAutAuthority("TU", Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}

			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_MARCA)) {
				try{
					utenteEjb.isAbilitatoAuthority("MA");
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "MA");
					utenteEjb.checkLivAutAuthority("MA", Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO)
					|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA)
					|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI)) {
				// Inizio modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione come vecchia Gestione 51
				if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI)) {
					if (!elementoTree.isCheckVisible()) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric028"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
				// Fine modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione come vecchia Gestione 51
				if (elemAuth == null) {
					try{
						if (elementoTree.getTipoMateriale() == null	|| elementoTree.getTipoMateriale().trim().equals("")) {
							utenteEjb.checkAttivita(CodiciAttivita.getIstance().CANCELLA_DOCUMENTO_1025);
						} else {
							utenteEjb.isAbilitatoTipoMateriale(elementoTree.getTipoMateriale());
							utenteEjb.checkAttivitaMat(CodiciAttivita.getIstance().CANCELLA_DOCUMENTO_1025, elementoTree.getTipoMateriale());
						}


						// Inizio Modifica almaviva2 08.07.2010 BUG MANTIS 3839: delocalizzazione non richiede controllo sul liv. Autorità
						if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO)) {
							utenteEjb.checkLivAutDocumenti(elementoTree.getTipoMateriale(), Integer.valueOf(elementoTree.getLivelloAutorita()));
						}
						// Fine   Modifica almaviva2 08.07.2010 BUG MANTIS 3839: delocalizzazione non richiede controllo sul liv. Autorità


						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							this.caricaListaEsamina(request, analiticaTitoloForm);
							resetToken(request);
							return mapping.getInputForward();
						}

				} else if (elemAuth.toString().equals("TU")) {
					try{
						utenteEjb.isAbilitatoAuthority("TU");
						utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "TU");
						utenteEjb.checkLivAutAuthority("TU", Integer.valueOf(elementoTree.getLivelloAutorita()));
						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							this.caricaListaEsamina(request, analiticaTitoloForm);
							resetToken(request);
							return mapping.getInputForward();
						}
				} else if (elemAuth.toString().equals("UM")) {
					try{
						utenteEjb.isAbilitatoAuthority("UM");
						utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028, "UM");
						utenteEjb.checkLivAutAuthority("UM", Integer.valueOf(elementoTree.getLivelloAutorita()));
						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
							this.caricaListaEsamina(request, analiticaTitoloForm);
							resetToken(request);
							return mapping.getInputForward();
						}
				}
			}

			if (analiticaTitoloForm.getConfermaCanc() == null) {
				analiticaTitoloForm.setConfermaCanc("NO");
			}
			if (analiticaTitoloForm.getConfermaCanc().equals("NO")) {
				analiticaTitoloForm.setConfermaCanc("SI");
				analiticaTitoloForm.setAnaliticaAttiva(false);
				ActionMessages errors = new ActionMessages();
				if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA)) {
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricConfermaScattura", elementoTree.getKey()));
				} else {
					errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricConfermaCanc", elementoTree.getKey()));
				}
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm
						.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			} else {
				analiticaTitoloForm.setConfermaCanc("NO");
				analiticaTitoloForm.setAnaliticaAttiva(true);
			}

			AreaDatiPassaggioCancAuthorityVO areaDatiPass = new AreaDatiPassaggioCancAuthorityVO();

			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA)
					|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI)) {
				areaDatiPass.setTipoOperazione("Scattura");
			} else {
				areaDatiPass.setTipoOperazione("Cancella");
			}

			areaDatiPass.setRicercaPolo(true);
			if (elementoTree.isFlagCondiviso()) {
				areaDatiPass.setRicercaIndice(true);
			} else {
				areaDatiPass.setRicercaIndice(false);
			}

			if (elemAuth != null) {
				areaDatiPass.setTipoAut(elemAuth.toString());
			}
			areaDatiPass.setBid(elementoTree.getKey());

			if (elementoTree.getTipoMateriale() == null || elementoTree.getTipoMateriale().equals("")) {
				areaDatiPass.setTipoMat(" ");
			} else {
				areaDatiPass.setTipoMat(elementoTree.getTipoMateriale());
			}

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			try {
				// COMMENTO PER ALESSANDRO COSI' SA' DO' DEVE CAMBIA' !!!!!!!!!!!!!!!!!!!
				if (elemAuth != null && elemAuth.toString().equals("PP")) {
					AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn;
					UserVO utente = Navigation.getInstance(request).getUtente();
					if (!elementoTree.isPossessoreFormaRinvio()){
						// sto effettuando la cancellazione a partire da una radice
						areaDatiPassReturn = factory.getGestioneDocumentoFisico().cancellaLegamePossessore(
								elementoTree.getKey(),
								"",
								utente.getCodPolo(),
								utente.getCodBib(),
								utente.getFirmaUtente());
					} else {
						// sto cancellando la singola voce di rinvio
						areaDatiPassReturn = factory.getGestioneDocumentoFisico().cancellaLegamePossessore(
								elementoTree.getParent().getKey() ,
								elementoTree.getKey(),
								utente.getCodPolo(),
								utente.getCodBib(),
								utente.getFirmaUtente());
					}
					areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
					if (areaDatiPass.getCodErr().equals("0000")) {
						areaDatiPass.setTestoProtocollo(null);
					} else {
						areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
					}
				} else {

					// INSERIRE controllo si natura N legata ad M per attivare la nuova funzione con apposito TipoOperazione
					// bug mantis 0006173 - almaviva2 Aprile 2016
					// Il controllo sulla natura dell'ogetto bibliografico da cancellare deve essere subordinato al controllo
					// che tale campo sia valorizzato e non null: è stato inserito tale controllo
					if (elementoTree.getNatura() != null) {
						if (elementoTree.getNatura().equals("N")) {
							int sizeTree = elementoTree.getChildren().size();
							for (int i=0; i < sizeTree; i++) {
								TreeElementViewTitoli elementoCorrente = (TreeElementViewTitoli) elementoTree.getChildren().get(i);
								if (elementoCorrente.getDatiLegame().getTipoLegame().equals("461") && elementoCorrente.getNatura().equals("M")) {
									areaDatiPass.setTipoOperazione("CancellaSpoglioDiMonografia");
									break;
								}
							}
						}
					}


					areaDatiPass = factory.getGestioneBibliografica().cancellaAuthority(areaDatiPass,
							Navigation.getInstance(request).getUserTicket());
				}
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>"	+ e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			}

			analiticaTitoloForm.setVisualVaiA("SI");

			if (areaDatiPass == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors);
			}

			if (areaDatiPass.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				if (areaDatiPass.getTestoProtocollo() == null || areaDatiPass.getTestoProtocollo().equals("")) {
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
				} else {
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOkConParametro" ,areaDatiPass.getTestoProtocollo()));
				}
				this.saveErrors(request, errors);

				if (analiticaTitoloForm.getBidRoot().equals(areaDatiPass.getBid())) {
					if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_AUTORE)) {
						return mapping.findForward("interrogazioneAutore");
					} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_MARCA)) {
						return mapping.findForward("interrogazioneMarca");
					} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LUOGO)) {
						return mapping.findForward("interrogazioneLuogo");
					} else {
						return mapping.findForward("interrogazioneTitolo");
					}
				} else {
					int codRit = impostaReticolo(request, analiticaTitoloForm);
					if (codRit < 0) {
						NavigationForward forward = Navigation.getInstance(request).goBack();
						forward.setRedirect(true);
						return forward;
					}
					if (analiticaTitoloForm.getVisualVaiA().equals("SI")) {
						return impostazioniVaiA( mapping, form,  request, response);
					}
				}
			}

			if (areaDatiPass.getCodErr().equals("9999")
					|| areaDatiPass.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",	areaDatiPass.getTestoProtocollo()));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				//almaviva2 13/11/2009
				if (areaDatiPass.getTipoAut() != null && areaDatiPass.getTipoAut().equals("PP")
						&& areaDatiPass.getTipoOperazione() != null && areaDatiPass.getTipoOperazione().equals("Cancella")
						&& areaDatiPass.getTestoProtocollo().equals("")){
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
					this.saveErrors(request, errors);
					request.setAttribute("cancellazione", "cancellazione");
					return mapping.findForward("interrogazionePossessore");
//					request.setAttribute("prov", "analitica");
//					return Navigation.getInstance(request).goBack();
				}
//				else if (areaDatiPass.getTipoAut() != null && areaDatiPass.getTipoAut().equals("PP")
//						&& areaDatiPass.getTipoOperazione() != null && areaDatiPass.getTipoOperazione().equals("Cancella")){
//					this.saveErrors(request, errors);
//					request.setAttribute("prov", "analitica");
//					return Navigation.getInstance(request).goBack();
//				}
				resetToken(request);
				return mapping.getInputForward();
			} else if (!areaDatiPass.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPass.getCodErr()));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			}
		}

		// =============================================================================================
		// CANCELLAZIONE LEGAMI FRA AUTHORITY
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LEGAME)) {

			if (elemAuth != null && elemAuth.toString().equals("AU")) {
				if (elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO() != null &&
						elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getForma().toString().equals("R")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.usareCanRinvio"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}
			} else if (elemAuth != null && elemAuth.toString().equals("LU")) {
				if (elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioLuogoGeneraleVO() != null &&
						elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioLuogoGeneraleVO().getForma().toString().equals("R")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.usareCanRinvio"));
					this.saveErrors(request, errors);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					this.caricaListaEsamina(request, analiticaTitoloForm);
					resetToken(request);
					return mapping.getInputForward();
				}
			}


			if (!verificaLivelloAutorita()) {
			}

			if (analiticaTitoloForm.getConfermaCanc() == null) {
				analiticaTitoloForm.setConfermaCanc("NO");
			}

			if (analiticaTitoloForm.getConfermaCanc().equals("NO")) {
				analiticaTitoloForm.setConfermaCanc("SI");
				analiticaTitoloForm.setAnaliticaAttiva(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ricConfermaCancLegame",
						elementoTree.getParent().getKey(), elementoTree.getKey()));
				this.saveErrors(request, errors);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				this.caricaListaEsamina(request, analiticaTitoloForm);
				resetToken(request);
				return mapping.getInputForward();
			} else {
				analiticaTitoloForm.setConfermaCanc("NO");
				analiticaTitoloForm.setAnaliticaAttiva(true);
			}

			//===========================================================================


			if (elemAuth == null
					|| SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {
				// POSIZIONAMENTO SU UN OGGETTO DI TIPO TITOLO
				areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitoloVO.setDescArrivo(elementoTree
						.getDescription());
				areaDatiLegameTitoloVO.setNaturaBidArrivo(elementoTree
						.getNatura());
				areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
				if (elemAuth != null) {
					areaDatiLegameTitoloVO.setAuthorityOggettoArrivo(elemAuth.toString());
				}
				areaDatiLegameTitoloVO.setTipoLegameNew(elementoTree.getDatiLegame().getTipoLegame());
				areaDatiLegameTitoloVO.setSequenzaNew(elementoTree
						.getDatiLegame().getSequenza());
				areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree
						.getDatiLegame().getNotaLegame());
				areaDatiLegameTitoloVO.setSiciNew(elementoTree.getDatiLegame()
						.getSici());
				areaDatiLegameTitoloVO.setSottoTipoLegameNew(elementoTree
						.getDatiLegame().getSottoTipoLegame());
				areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
				if (parentAuth == null
						|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
					// OGGETTO PADRE E' UN TITOLO - variazione legame
					// titolo-titolo
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getLivAut());
					areaDatiLegameTitoloVO
							.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getNatura());
					areaDatiLegameTitoloVO
							.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getTipoMateriale());
					if ( parentAuth != null) {
						areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(parentAuth.toString());
					}
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO",
							areaDatiLegameTitoloVO);
					return mapping
							.findForward("variazionePerLegamiTitoloTitolo");
				}
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				// POSIZIONAMENTO SU UN OGGETTO DI TIPO AUTORE
				areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitoloVO.setDescArrivo(elementoTree
						.getDescription());
				areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("AU");
				areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
				areaDatiLegameTitoloVO.setRelatorCodeNew(elementoTree
						.getDatiLegame().getRelatorCode());
				areaDatiLegameTitoloVO.setTipoResponsNew(elementoTree
						.getDatiLegame().getResponsabilita());

				areaDatiLegameTitoloVO.setSuperfluoNew(elementoTree
						.getDatiLegame().isSuperfluo());
				areaDatiLegameTitoloVO.setIncertoNew(elementoTree
						.getDatiLegame().isIncerto());

				areaDatiLegameTitoloVO.setTipoLegameNew(elementoTree
						.getDatiLegame().getTipoLegame());
				areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree
						.getDatiLegame().getNotaLegame());
				areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
				if (parentAuth == null
						|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
					// OGGETTO PADRE E' UN TITOLO - variazione legame
					// titolo-autore
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getLivAut());
					areaDatiLegameTitoloVO
							.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getNatura());
					areaDatiLegameTitoloVO
							.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getTipoMateriale());
					if ( parentAuth != null) {
						areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(parentAuth.toString());
					}
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");

					// RICORDARSI DI INSERIRE LA PARTE DEI DATI OLD PERCHE' LA
					// MODIFICA DI QUESTO
					// TIPO DI LEGAME CONSISTE IN CANCELLA-INSERISCI
					// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

					request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiTitoloAutore");
				} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.AU)) {
					// OGGETTO PADRE E' UN AUTORE - variazione legame fra
					// editori
					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioAutoreGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioAutoreGeneraleVO()
									.getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO",
							areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiFraAutority");
				} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.MA)) {
					// OGGETTO PADRE E' UNA MARCA - variazione legame
					// marca-autore
					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("MA");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioMarcaGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioMarcaGeneraleVO()
									.getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO",
							areaDatiLegameTitoloVO);
					return mapping
							.findForward("variazionePerLegamiFraAutority");
				}
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
				// POSIZIONAMENTO SU UN OGGETTO DI TIPO MARCA
				areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitoloVO.setDescArrivo(elementoTree
						.getDescription());
				areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("MA");
				areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
				areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree
						.getDatiLegame().getNotaLegame());
				areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
				if (parentAuth == null
						|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
					// OGGETTO PADRE E' UN TITOLO - variazione legame
					// titolo-marca
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getLivAut());
					areaDatiLegameTitoloVO
							.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getNatura());
					areaDatiLegameTitoloVO
							.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getTipoMateriale());
					if ( parentAuth != null) {
						areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(parentAuth.toString());
					}
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO",
							areaDatiLegameTitoloVO);
					return mapping
							.findForward("variazionePerLegamiTitoloMarca");
				} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.AU)) {
					// OGGETTO PADRE E' UN AUTORE - variazione legame
					// editore-marca
					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioAutoreGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioAutoreGeneraleVO()
									.getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO",
							areaDatiLegameTitoloVO);
					return mapping
							.findForward("variazionePerLegamiFraAutority");
				}
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {
				// POSIZIONAMENTO SU UN OGGETTO DI TIPO LUOGO
				areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
				areaDatiLegameTitoloVO.setDescArrivo(elementoTree.getDescription());
				areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("LU");
				areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
				areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());
				areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());

				// Inizio modifica almaviva2 10.01.2011 BUG MANTIS 4123 valorizzato correttamente il tipo relazione
				// fra titolo e luogo così da consentirne la cancellazione
				areaDatiLegameTitoloVO.setRelatorCodeNew(elementoTree.getDatiLegame().getRelatorCode());
				// Fine modifica almaviva2 10.01.2011 BUG MANTIS 4123


				if (parentAuth == null
						|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
					// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-marca
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
					areaDatiLegameTitoloVO.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getNatura());
					areaDatiLegameTitoloVO.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getTipoMateriale());
					if ( parentAuth != null) {
						areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(parentAuth.toString());
					}
					areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
					return mapping.findForward("variazionePerLegamiTitoloLuogo");
				} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.LU)) {
					// OGGETTO PADRE E' UN LUOGO - variazione legame luogo-luogo
					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("LU");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
					if (analiticaTitoloForm.getBidRoot() != null) {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(analiticaTitoloForm.getBidRoot());
					} else {
						areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
					}

					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
					areaDatiLegameTitoloVO
							.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioAutoreGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO
							.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
									.getParent())
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioAutoreGeneraleVO()
									.getVersione());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Cancella");
					request.setAttribute("AreaDatiLegameTitoloVO",
							areaDatiLegameTitoloVO);
					return mapping
							.findForward("variazionePerLegamiFraAutority");
				}
			} else {
				// POSIZIONAMENTO SU UN OGGETTO NON GESTITO DA GESTIONE
				// BIBLIOGRAFICA
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.gestSemantica",
						elementoTree.getKey()));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm
						.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}

			//===========================================================================

		}



		// =============================================================================================
		// SCAMBIO FORMA FRA FORMA DI RINVIO E FORMA ACCETTATA
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIO_FORMA)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestPossessoriSelez().equals(MenuHome.FUNZ_POSSESSORI_SCAMBIO_FORMA)) {

			if (elemAuth.toString().equals("AU")
					|| elemAuth.toString().equals("LU")) {
				try{
					utenteEjb.isAbilitatoAuthority(elemAuth.toString());
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().SCAMBIO_FORMA_1029);
					utenteEjb.checkLivAutAuthority(elemAuth.toString() , Integer.valueOf(elementoTree.getLivelloAutorita()));
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						this.caricaListaEsamina(request, analiticaTitoloForm);
						resetToken(request);
						return mapping.getInputForward();
					}
			} else if (elemAuth.toString().equals("PP")) {

			}
			if (analiticaTitoloForm.getTipoOperazioneConferma().equals("")) {
				analiticaTitoloForm.setVisualVaiA("NO");
				analiticaTitoloForm.setAnaliticaAttiva(false);
				analiticaTitoloForm.setTipoOperazioneConferma("SCAMBIOFORMA");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica.ricConfermaScambio",
						elementoTree.getKey(), elementoTree.getParent()
								.getKey()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm
						.getTreeElementViewTitoli());
				resetToken(request);
				return mapping.getInputForward();

			}
		}

		// =============================================================================================
		// GESTIONE LOCALIZZAZIONI
		// =============================================================================================
		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza

		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez()
															.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez()
															.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_GESTIONE)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez()
															.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_POSSESSO)) {

			request.setAttribute("tipoProspettazione", "VAR");

			if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez()
													.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI)) {
				request.setAttribute("tipoRichiesta", "TUTTO");
			} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez()
													.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_GESTIONE)) {
				request.setAttribute("tipoRichiesta", "GESTIONE");
			}  else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez()
													.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_POSSESSO)) {
				request.setAttribute("tipoRichiesta", "POSSESSO");


				// INIZIO almaviva2 marzo 2017 quando si chaiam la funzionalità di aggiornamento localizzazioni secca su un bid
				// NON si deve creare una lista di oggetti da localizzare ma inviare solo il bid selezionato nella analitica
				// viene asteriscata tutta la chimata all'oggetto datiBibliograficiCollocazioneVO e sostituito
//				// Chiamata alla funzione per ricevere la lista degli elementi del reticolo (DOC) da localizzare
				DatiBibliograficiCollocazioneVO datiBibliograficiCollocazioneVO = AnaliticaCollocazione.getDatiBibliografici(treeElementView);
				List<String> bidLocalizz = new ArrayList<String>();
				if (ValidazioneDati.isFilled(datiBibliograficiCollocazioneVO.getListaTitoliLocalizzazioneNonPosseduti())) {
					CollocazioneTitoloVO[] collTitVo = datiBibliograficiCollocazioneVO.getListaTitoliLocalizzazioneNonPosseduti();
					for (int i = 0; i < collTitVo.length; i++) {
						bidLocalizz.add(collTitVo[i].getBid());
					}
				}
//				List<String> bidLocalizz = new ArrayList<String>();
//				bidLocalizz.add(elementoTree.getKey());
				// FINE almaviva2 marzo 2017

				request.setAttribute("listaBidLocaliz", bidLocalizz);
			}

			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());
			if (elemAuth != null) {
				request.setAttribute("tipoAuth", elemAuth.toString());
			}
			if (elemAuth == null
					|| elemAuth.toString().equals("TU")
					|| elemAuth.toString().equals("UM")) {
				DettaglioTitoloCompletoVO dettTitComVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO();
				request.setAttribute("natura", dettTitComVO.getDetTitoloPFissaVO().getNatura());
				request.setAttribute("tipoMat", dettTitComVO.getDetTitoloPFissaVO().getTipoMat());
			} else if (elemAuth.toString().equals("AU")
					|| elemAuth.toString().equals("MA")
					|| elemAuth.toString().equals("LU")) {
				request.setAttribute("natura", "");
			} else {
				// POSIZIONAMENTO SU UN OGGETTO NON GESTITO DA GESTIONE BIBLIOGRAFICA
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.gestSemantica", elementoTree.getKey()));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}
			request.setAttribute("livRic", analiticaTitoloForm.getLivRicerca());
			return mapping.findForward("sinteticaLocalizzazioni");

		}

		// =============================================================================================
		// CHIAMATA AL MODULO DI PROPOSTE DI CORREZIONE
		// =============================================================================================
		if (analiticaTitoloForm.getInterrogazioneVaiAForm()
				.getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_PROPOSTA_CORREZIONE)) {

			request.setAttribute("tipoProspettazione", "VAR");
			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());

			DettaglioTitoloCompletoVO dettTitComVO = elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO();
			AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVO = new  AreaDatiPropostaDiCorrezioneVO();

			areaDatiPropostaDiCorrezioneVO.setIdOggettoProposta(elementoTree.getKey());
			areaDatiPropostaDiCorrezioneVO.setMittenteProposta("");
			if (elemAuth == null) {
				areaDatiPropostaDiCorrezioneVO.setTipoMaterialeProposta(dettTitComVO.getDetTitoloPFissaVO().getTipoMat());
				areaDatiPropostaDiCorrezioneVO.setNaturaProposta(dettTitComVO.getDetTitoloPFissaVO().getNatura());
			} else if (elemAuth.toString().equals("AU")
					|| elemAuth.toString().equals("MA")
					|| elemAuth.toString().equals("LU")
					|| elemAuth.toString().equals("TU")
					|| elemAuth.toString().equals("UM")) {
						areaDatiPropostaDiCorrezioneVO.setTipoAuthorityProposta(elemAuth.toString());
			} else {
				// POSIZIONAMENTO SU UN OGGETTO NON GESTITO DA GESTIONE BIBLIOGRAFICA
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.gestSemantica", elementoTree.getKey()));
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return mapping.getInputForward();
			}

			request.setAttribute("areaDatiPropostaDiCorrezioneVO", areaDatiPropostaDiCorrezioneVO);
			request.setAttribute("livRic", analiticaTitoloForm.getLivRicerca());
			return mapping.findForward("propostaDiCorrezione");

		}
		// =============================================================================================
		// FINE SELEZIONI POSSIBILI SUL VAI A
		// =============================================================================================

		if (elementoTree.getKey().equals(analiticaTitoloForm.getBidRoot())) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			resetToken(request);
			if (analiticaTitoloForm.getTipoAuthority().equals("AU")	|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("V")) {
				return mapping.findForward("interrogazioneAutore");
			} else if (analiticaTitoloForm.getTipoAuthority().equals("PP")
					|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("P")
					|| analiticaTitoloForm.getBidRoot().substring(3, 4).equals("T")) {
				return mapping.findForward("interrogazionePossessore");
			} else if (analiticaTitoloForm.getTipoAuthority().equals("MA") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("M")) {
				return mapping.findForward("interrogazioneMarca");
			} else if (analiticaTitoloForm.getTipoAuthority().equals("LU") || analiticaTitoloForm.getBidRoot().substring(3, 4).equals("L")) {
				return mapping.findForward("interrogazioneLuogo");
			} else {
				return mapping.findForward("interrogazioneTitolo");
			}
		}

		analiticaTitoloForm.setLivRicerca("I");
		request.setAttribute("livRicerca", "I");
		request.setAttribute("bid", analiticaTitoloForm.getBidRoot());
		request.setAttribute("vaiA", "SI");
		analiticaTitoloForm.setAnaliticaAttiva(true);
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		int codRit = impostaReticolo(request, analiticaTitoloForm);
		if (codRit < 0) {
			NavigationForward forward = Navigation.getInstance(request).goBack();
			forward.setRedirect(true);
			return forward;
		}

		elementoTree = (TreeElementViewTitoli) treeElementView.findElement(analiticaTitoloForm.getBidRoot());

		boolean bidRoot = false;
		if (elementoTree.getKey().equals(analiticaTitoloForm.getBidRoot())) {
			bidRoot = true;
			analiticaTitoloForm.setGestioneInferiori("SI");
		}

		if (elemAuth == null) {
			if (bidRoot) {
				analiticaTitoloForm.setGestioneInferiori("SI");
			}
			caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree,	bidRoot);
		} else {
			if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.PP)) {
				caricaComboPossessoreVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.MA)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.LU)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {
				caricaComboOggettiBibliogVaiA(request,analiticaTitoloForm, elementoTree, bidRoot);
			}
		}

		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAAcquis() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiACatalSemant() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiACatalUnimarc() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestBibliog() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestDocFis() == null
				&& analiticaTitoloForm.getInterrogazioneVaiAForm().getListaVaiAGestPossessori() == null) {
			analiticaTitoloForm.setVisualVaiA("NO");
			request.setAttribute("vaiA", "NO");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.noVaiADisponibili"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		resetToken(request);
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
		return mapping.getInputForward();
	}

	private String[] creaListaBidPerCatturaInferiori(AnaliticaTitoloForm form) {

		TreeElementViewTitoli root = form.getTreeElementViewTitoli();
		HashSet<String> tmp = new HashSet<String>();
		for (String id : form.getCheckItemSelez()) {
			TreeElementView child = root.findElementUnique(Integer.valueOf(id));
			if (child != null)
				tmp.add(child.getKey());
		}

		int size = tmp.size();
		if (size > 0) {
			String[] listaInferiori = new String[size];
			return tmp.toArray(listaInferiori);
		}
		return null;
	}

	// almaviva2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
	// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
	// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
	// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
	private String[] creaListaBidPerSelezioneSpogli(AnaliticaTitoloForm form) {

		TreeElementViewTitoli root = form.getTreeElementViewTitoli();
		HashSet<String> tmp = new HashSet<String>();
		for (TreeElementView child : root.getChildren()) {
			TreeElementViewTitoli childCastato = (TreeElementViewTitoli) child;
			if (childCastato.getNatura().equals("N"))
				tmp.add(child.getKey());
		}

		int size = tmp.size();
		if (size > 0) {
			String[] listaInferiori = new String[size];
			return tmp.toArray(listaInferiori);
		}
		return null;
	}


	// Aprile 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: trascinamento legami autore ma M a N (esempio disco M con tracce N)
	// creato nuovo metodo per controllare l'elenco degli oggetti di arrivo del trascinamento legami tit-aut
	// con controllo che non si siano selezionati sia documenti che titoliUniformi
	private boolean verificaPresenzaDocUniformi(AnaliticaTitoloForm form) {

		TreeElementViewTitoli root = form.getTreeElementViewTitoli();
		boolean uniformi=false;
		boolean documenti=false;

		for (String id : form.getCheckItemSelez()) {
			TreeElementView child = root.findElementUnique(Integer.valueOf(id));
			if (child != null) {
				if (((TreeElementViewTitoli)child).getNatura().equals("A")) {
					uniformi=true;
				} else if (((TreeElementViewTitoli)child).getNatura().equals("M") ||
						((TreeElementViewTitoli)child).getNatura().equals("W") ||
						((TreeElementViewTitoli)child).getNatura().equals("N") ||
						((TreeElementViewTitoli)child).getNatura().equals("T") ||
						((TreeElementViewTitoli)child).getNatura().equals("B")) {
					documenti=true;
				}
			}
		}

		if (documenti && uniformi) {
			return true;
		}
		return false;
	}

	private boolean verificaPresenzaSoloDocumenti(AnaliticaTitoloForm form) {

		TreeElementViewTitoli root = form.getTreeElementViewTitoli();
		String natura = "";

//		for (String id : form.getCheckItemSelez()) {
//			TreeElementView child = root.findElementUnique(Integer.valueOf(id));
//			if (child != null) {
//				natura = ((TreeElementViewTitoli)child).getNatura();
//				if (!ValidazioneDati.in(natura, "M", "W", "N")) {
//					return false;
//				}
//			}
//		}
		SbnAuthority authority;

		for (String id : form.getCheckItemSelez()) {
			TreeElementView child = root.findElementUnique(Integer.valueOf(id));
			if (child != null) {
				natura = ((TreeElementViewTitoli)child).getNatura();
				authority = ((TreeElementViewTitoli)child).getTipoAuthority();

				if (authority != null) {
					if (authority.getType() != SbnAuthority.SO_TYPE && authority.getType() != SbnAuthority.CL_TYPE) {
						return false;
					}
				} else if (!ValidazioneDati.in(natura, "M", "W", "N")) {
					return false;
				}
			}
		}

		return true;
	}


	public ActionForward annullaVaiA(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		analiticaTitoloForm.setVisualVaiA("NO");
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
		resetToken(request);
		return mapping.getInputForward();
	}

	public void caricaComboTitoloVaiA(HttpServletRequest request,AnaliticaTitoloForm analiticaTitoloForm,
			TreeElementViewTitoli elementoTree,	boolean bidRoot) {

		GestioneVaiA gest = new GestioneVaiA();

		// modifica evolutiva almaviva2 18.05.2011 - per Vai A attivato dalla sinteticaSchede per la catalogazione derivata
		// in questo caso si deve mostrare solo il menù di Gestione Bibliografica
		if (Navigation.getInstance(request).bookmarkExists(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE)) {
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPossessori(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalSemant(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalUnimarc(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPeriodici(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestBibliog(gest.getBibliograficaTitoloLista
					(request, analiticaTitoloForm.getLivRicerca(), elementoTree, bidRoot));
			return;
		}


		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPossessori(null);
		if (elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
//				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {

			if (elementoTree.getNatura().equals("A")
					|| elementoTree.getNatura().equals("B")
					|| elementoTree.getNatura().equals("P")
					|| elementoTree.getNatura().equals("T")
					|| elementoTree.getNatura().equals("D")
					|| elementoTree.getNatura().equals("N")) {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(null);
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
			} else if (elementoTree.getNatura().equals("C")) {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(gest.getAcquisizioneLista
						(request, analiticaTitoloForm.getLivRicerca(),  elementoTree));
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
			} else if (elementoTree.getNatura().equals("S")) {
				// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
				//almaviva5_20100906 controllo profilo
				try {
					Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().PERIODICI);
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPeriodici(gest.getPeriodiciLista
							(request, analiticaTitoloForm.getLivRicerca(),  elementoTree));
				} catch (Exception e) {
					//non autorizzato
				}
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(gest.getAcquisizioneLista
						(request, analiticaTitoloForm.getLivRicerca(),  elementoTree));
				// Ottobre 2017 - almaviva2 - per i documenti con livello autorità 01 non è possibile effettuare alcuna
				// operazione di Documento Fisico
				if (elementoTree.getLivelloAutorita().equals("01")) {
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
				} else {
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(gest.getDocumentoFisicoLista(request));
				}
				// Fine Modifica almaviva2 04.08.2010 - Gestione periodiciù
				// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			} else if (elementoTree.getNatura().equals("R")) {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(null);
				// Ottobre 2017 - almaviva2 - per i documenti con livello autorità 01 non è possibile effettuare alcuna
				// operazione di Documento Fisico
				if (elementoTree.getLivelloAutorita().equals("01")) {
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
				} else {
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(gest.getDocumentoFisicoLista(request));
				}
			} else {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(gest.getAcquisizioneLista
						(request, analiticaTitoloForm.getLivRicerca(),  elementoTree));
				// Ottobre 2017 - almaviva2 - per i documenti con livello autorità 01 non è possibile effettuare alcuna
				// operazione di Documento Fisico
				if (elementoTree.getLivelloAutorita().equals("01")) {
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
				} else {
					analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(gest.getDocumentoFisicoLista(request));
				}

			}


			// BUG mantis 5316  collaudo - Maggio 2013 - Anche le Raccolte Fittizie sono soggette a catalogazione sematica
			if (elementoTree.getNatura().equals("M")
					|| elementoTree.getNatura().equals("S")
					|| elementoTree.getNatura().equals("N")
					|| elementoTree.getNatura().equals("W")
					|| elementoTree.getNatura().equals("R")) {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalSemant(gest.getSemanticaLista
						(request, analiticaTitoloForm.getLivRicerca(), elementoTree));
			} else {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalSemant(null);
			}
			if (elementoTree.getNatura().equals("M")
					|| elementoTree.getNatura().equals("S")
					|| elementoTree.getNatura().equals("N")
					|| elementoTree.getNatura().equals("W")
					|| elementoTree.getNatura().equals("C")) {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalUnimarc(gest.getStampeLista(request,
						analiticaTitoloForm.getLivRicerca(), elementoTree));
			} else {
				analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalUnimarc(null);
			}

		} else {
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalSemant(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalUnimarc(null);
			// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
			analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPeriodici(null);
			// Fine Modifica almaviva2 04.08.2010 - Gestione periodici
		}

		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestBibliog(gest.getBibliograficaTitoloLista
						(request, analiticaTitoloForm.getLivRicerca(), elementoTree, bidRoot));

	}


	public void caricaComboPossessoreVaiA(HttpServletRequest request,AnaliticaTitoloForm analiticaTitoloForm,
			TreeElementViewTitoli elementoTree, boolean bidRoot) {

		GestioneVaiA gest = new GestioneVaiA();

		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPossessori(gest.getPossessoreLista
				(request, analiticaTitoloForm.getLivRicerca(),elementoTree));
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(null);
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalSemant(null);
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
		// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPeriodici(null);
		// Fine Modifica almaviva2 04.08.2010 - Gestione periodici

		// Inizio modifica almaviva2 2010.10.27 intervento interno;
		// nel caso dei Possessori la tendina di gestione Bibliografica non si deve vedere;
//		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestBibliog(null);
		analiticaTitoloForm.getInterrogazioneVaiAForm().initNullListaVaiAGestBibliog();
		// Fine modifica almaviva2 2010.10.27 intervento interno;

		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalUnimarc(null);
	}

	public void caricaComboOggettiBibliogVaiA(HttpServletRequest request,AnaliticaTitoloForm analiticaTitoloForm,
			TreeElementViewTitoli elementoTree, boolean bidRoot) {

		GestioneVaiA gest = new GestioneVaiA();

		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPossessori(null);
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAAcquis(null);
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalSemant(null);
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestDocFis(null);
		// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestPeriodici(null);
		// Fine Modifica almaviva2 04.08.2010 - Gestione periodici
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiAGestBibliog
			(gest.getBibliograficaTitoloLista(request, analiticaTitoloForm.getLivRicerca(),elementoTree, bidRoot));
		analiticaTitoloForm.getInterrogazioneVaiAForm().setListaVaiACatalUnimarc
			(gest.getStampeLista(request, analiticaTitoloForm.getLivRicerca(), elementoTree));
	}



	private void caricaListaEsamina(HttpServletRequest request,
			AnaliticaTitoloForm analiticaTitoloForm) {


		TreeElementViewTitoli elementoTree =
				(TreeElementViewTitoli) analiticaTitoloForm.getTreeElementViewTitoli()
						.findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));

		String pathDiAnalitica = analiticaTitoloForm.getMyPath();



		if (analiticaTitoloForm.getProvenienzaChiamatainSIF().equals("ACQUISIZIONI")) {
			if (elementoTree.getTipoAuthority() == null) {
				pathDiAnalitica = ".gestionebibliografica.titolo.analiticaTitoloDaAcquisizioni";
			} else {
				pathDiAnalitica ="";
			}
		} else {
			// Modifica almaviva2 29.07.2010 BUG MANTIS 3859 - inserita gestione (i nuovi else if) per le Authority di Soggetti e Classi
			// nel caricamento dell'XML per la lista degli esamina disponibili
			if (elementoTree.getTipoAuthority() == null) {
				pathDiAnalitica = analiticaTitoloForm.getMyPath();
			} else if (elementoTree.getTipoAuthority().toString().equals("AU")) {
				pathDiAnalitica = ".gestionebibliografica.titolo.analiticaAutore";
			} else if (elementoTree.getTipoAuthority().toString().equals("MA")) {
				pathDiAnalitica = ".gestionebibliografica.titolo.analiticaMarca";
			} else if (elementoTree.getTipoAuthority().toString().equals("LU")) {
				pathDiAnalitica = ".gestionebibliografica.titolo.analiticaLuogo";
			} else if (elementoTree.getTipoAuthority().toString().equals("PP")) {
				pathDiAnalitica =".gestionebibliografica.titolo.analiticaPossessore";
			} else if (elementoTree.getTipoAuthority().toString().equals("SO")) {
				pathDiAnalitica = ".gestionebibliografica.titolo.analiticaSoggetto";
			} else if (elementoTree.getTipoAuthority().toString().equals("CL")) {
				pathDiAnalitica =".gestionebibliografica.titolo.analiticaClasse";
			}
		}

		analiticaTitoloForm.setTabellaEsaminaVO((TabellaEsaminaVO) this.getServlet().getServletContext().getAttribute("serverTypes"));
		List<?> listaCaricamento = analiticaTitoloForm.getTabellaEsaminaVO().getLista(pathDiAnalitica);

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
					|| eleListaCar.getMyLivelloBaseDati().equals(analiticaTitoloForm.getLivRicerca())) {
				listaEsamina.setDescrizione(eleListaCar.getMyLabel());
				lista.add(listaEsamina);
			}
		}
		analiticaTitoloForm.setListaEsaminaTit(lista);
	}

	public ActionForward confermaScambioForma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;
		analiticaTitoloForm.setAnaliticaAttiva(true);

		TreeElementViewTitoli treeElementView = analiticaTitoloForm.getTreeElementViewTitoli();

		analiticaTitoloForm.setBidRoot(treeElementView.getKey());

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli) treeElementView
		.findElementUnique(Integer.valueOf(analiticaTitoloForm
				.getRadioItemSelez()));

		AreaDatiVariazioneReturnVO areaDatiPassReturnScambio = new AreaDatiVariazioneReturnVO();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (elementoTree.getTipoAuthority().toString().equals("AU")
				|| elementoTree.getTipoAuthority().toString().equals("LU")) {

			AreaDatiLegameTitoloVO areaDatiPass = new AreaDatiLegameTitoloVO();

			areaDatiPass.setIdArrivo(elementoTree.getKey());

			if (elementoTree.getTipoAuthority().toString().equals("AU")) {
				areaDatiPass.setAuthorityOggettoPartenza("AU");
				areaDatiPass.setTipoNomeArrivo(elementoTree
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioAutoreGeneraleVO().getTipoNome());
				areaDatiPass.setLivAutIdArrivo(elementoTree
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioAutoreGeneraleVO().getLivAut());
			}

			if (elementoTree.getTipoAuthority().toString().equals("LU")) {
				areaDatiPass.setAuthorityOggettoPartenza("LU");
				areaDatiPass.setLivAutIdArrivo(elementoTree
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioLuogoGeneraleVO().getLivAut());
			}

			areaDatiPass.setBidPartenza(elementoTree.getParent().getKey());
			areaDatiPass.setTimeStampBidPartenza(elementoTree.getParent().getT005());

			try {
				areaDatiPassReturnScambio = factory.getGestioneBibliografica()
						.scambiaForma(areaDatiPass, Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>"	+ e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			}
		} else if (elementoTree.getTipoAuthority().toString().equals("PP")) {

			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn;
			areaDatiPassReturn = factory.getGestioneDocumentoFisico().scambioLegame(
						elementoTree.getParent().getKey(), elementoTree.getKey(), Navigation.getInstance(request).getUtente().getFirmaUtente());

			areaDatiPassReturnScambio.setCodErr(areaDatiPassReturn.getCodErr());
			if (areaDatiPassReturnScambio.getCodErr().equals("0000")) {
				areaDatiPassReturnScambio.setTestoProtocollo(null);
			} else {
				areaDatiPassReturnScambio.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
			}
		}


		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
		analiticaTitoloForm.setVisualVaiA("SI");
		analiticaTitoloForm.setTipoOperazioneConferma("");


		if (areaDatiPassReturnScambio == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
		}

		if (areaDatiPassReturnScambio.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			int codRit = impostaReticolo(request, analiticaTitoloForm);
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAGestBibliogSelez("");
			analiticaTitoloForm.getInterrogazioneVaiAForm().setVaiAGestPossessoriSelez("");
			if (codRit < 0) {
				NavigationForward forward = Navigation.getInstance(request).goBack();
				forward.setRedirect(true);
				return forward;
			}
		}

		if (areaDatiPassReturnScambio.getCodErr().equals("9999")
				|| areaDatiPassReturnScambio.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturnScambio.getTestoProtocollo()));
			this.saveErrors(request, errors);
		} else if (!areaDatiPassReturnScambio.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturnScambio.getCodErr()));
			this.saveErrors(request, errors);
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward annullaOperazioneComplessa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;
//		Richiesta esplicita Contardi
//		analiticaTitoloForm.setVisualVaiA("NO");

		// inizio modifica almaviva2 19.04.2010 BUG MANTIS 3692 rispristinato Annulla da Vai a che non funzionava più
		// dopo la modifica sopra identificata dal commento "Richiesta esplicita Contardi"
		if (analiticaTitoloForm.getConfermaCanc() != null && analiticaTitoloForm.getConfermaCanc().equals("NO")) {
			analiticaTitoloForm.setVisualVaiA("NO");
		} else {
			analiticaTitoloForm.setVisualVaiA("SI");
		}
		analiticaTitoloForm.setConfermaCanc("NO");

//		analiticaTitoloForm.setVisualVaiA("SI");
//		Fine modifica almaviva2 19.04.2010 BUG MANTIS 3692

		analiticaTitoloForm.setAnaliticaAttiva(true);
		analiticaTitoloForm.setTipoOperazioneConferma("");
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}


	private AreaTabellaOggettiDaCondividereVO ricercaRadicePerCondivisione(TreeElementViewTitoli elementoTreeCondividiNew,
			HttpServletRequest request, AnaliticaTitoloForm analiticaTitoloForm) throws Exception {

		// Impostazioni aree per ricerca in Indice della RADICE (:
		// se l'oggetto viene trovato si prospetta l'elenco dei simili trovati;
		// si l'oggetto non viene trovato si invia diagnostio per evere le opportune conferme;

		AreaTabellaOggettiDaCondividereVO areaTabellaOggettiDaCondividereVO = new AreaTabellaOggettiDaCondividereVO();

		if (elementoTreeCondividiNew.getTipoAuthority() != null) {
			areaTabellaOggettiDaCondividereVO.setTipoAuthority(elementoTreeCondividiNew.getTipoAuthority().toString());
		}

		if (elementoTreeCondividiNew.getTipoMateriale() != null) {
			areaTabellaOggettiDaCondividereVO.setTipoMateriale(elementoTreeCondividiNew.getTipoMateriale());
		}

		if (elementoTreeCondividiNew.getNatura() != null) {
			areaTabellaOggettiDaCondividereVO.setNatura(elementoTreeCondividiNew.getNatura());
			if (areaTabellaOggettiDaCondividereVO.getNatura().equals("W")) {
				// Nel caso di catalogazione in indice della W la ricerca deve essere fatta con la descrizione della
				// madre e non con quella della W che non è significativa
				int sizeTree = elementoTreeCondividiNew.getChildren().size();
				for (int i=0; i < sizeTree; i++) {
					TreeElementViewTitoli elementoCorrente = (TreeElementViewTitoli) elementoTreeCondividiNew.getChildren().get(i);
					if (elementoCorrente.getDatiLegame().getTipoLegame().equals("461")) {
						areaTabellaOggettiDaCondividereVO.setDescrizionePerRicercaMadre51(
								elementoCorrente.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getAreaTitTitolo());
						break;
					}
				}
			}
		}

		areaTabellaOggettiDaCondividereVO.setIdPadre(elementoTreeCondividiNew.getKey());
		areaTabellaOggettiDaCondividereVO.setDescrizionePerRicerca(elementoTreeCondividiNew.getDescription());
		areaTabellaOggettiDaCondividereVO.setBidRoot(true);
		areaTabellaOggettiDaCondividereVO.setDettTitComVO(elementoTreeCondividiNew.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO());
		areaTabellaOggettiDaCondividereVO.setDettAutGenVO(elementoTreeCondividiNew.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO());
		areaTabellaOggettiDaCondividereVO.setTipoRicerca("PRIMARIC");

		// Inizio Modifica almaviva2 BUG MANTIS 4293 (Collaudo) la prospettazione dei simili per catalogazione In Indice di
		// elemento locale deve essere fatta con la tipologia esatta di ordinamento e non con quella di default
		if (analiticaTitoloForm.getDatiInterrTitolo() != null) {
			if (analiticaTitoloForm.getDatiInterrTitolo().getInterTitGen() != null) {
				areaTabellaOggettiDaCondividereVO.setTipoOrdinamSelez(analiticaTitoloForm.getDatiInterrTitolo().getInterTitGen().getTipoOrdinamSelez());
			}
		}
		// Fine Modifica almaviva2 BUG MANTIS 4293 (Collaudo)

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		try {
			if (areaTabellaOggettiDaCondividereVO.getTipoAuthority() != null && areaTabellaOggettiDaCondividereVO.getTipoAuthority().equals("AU")) {
				areaTabellaOggettiDaCondividereVO = factory.getGestioneBibliografica().ricercaAutorePerCondividi(
						areaTabellaOggettiDaCondividereVO, Navigation.getInstance(request).getUserTicket());
			} else if (areaTabellaOggettiDaCondividereVO.getNatura() != null) {
				areaTabellaOggettiDaCondividereVO = factory.getGestioneBibliografica().ricercaDocumentoPerCondividi(
						areaTabellaOggettiDaCondividereVO, Navigation.getInstance(request).getUserTicket());
			}
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",	"ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return areaTabellaOggettiDaCondividereVO;
		}

		if (areaTabellaOggettiDaCondividereVO == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return areaTabellaOggettiDaCondividereVO;
		}

		if (!areaTabellaOggettiDaCondividereVO.getCodErr().equals("0000")) {
			if (areaTabellaOggettiDaCondividereVO.getCodErr().equals("9999")
					|| areaTabellaOggettiDaCondividereVO.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
						areaTabellaOggettiDaCondividereVO.getTestoProtocollo()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return areaTabellaOggettiDaCondividereVO;
			} else if (!areaTabellaOggettiDaCondividereVO.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica."	+ areaTabellaOggettiDaCondividereVO.getCodErr()));
				this.saveErrors(request, errors);
				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				return areaTabellaOggettiDaCondividereVO;
			}
		}

		return areaTabellaOggettiDaCondividereVO;

	}




	public ActionForward confermaInvioIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		// Inizio BUG MANTIS 3344 - almaviva2 09.12.2009 - inserita in OR anche la voce di REINVIO_IN_INDICE
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION)
				|| analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_REINVIO_IN_INDICE)) {
			// controllo sulla presenza all'interno del reticolo di elementi arrivo di legami NON CONDIVISI !!!

			// Intervento interno del 05.03.2013 anche i legami di tipo 464 (spogli) non impediscono l'invio in Indice
			// di un oggetto solo locale: inserito l'or su tipoLegame 464
			int sizeTree = analiticaTitoloForm.getTreeElementViewTitoli().getChildren().size();
			for (int i=0; i < sizeTree; i++) {
				TreeElementViewTitoli elementoCorrente = (TreeElementViewTitoli) analiticaTitoloForm.getTreeElementViewTitoli().getChildren().get(i);
				if (!elementoCorrente.isFlagCondiviso()) {
					if (elementoCorrente.isAutoreFormaRinvio()
							|| elementoCorrente.isLuogoFormaRinvio()
							|| elementoCorrente.getDatiLegame().getTipoLegame().equals("463")
							|| elementoCorrente.getDatiLegame().getTipoLegame().equals("464")
							|| (elementoCorrente.getTipoAuthority() != null
									&& (elementoCorrente.getTipoAuthority().toString().equals("AB")
											|| elementoCorrente.getTipoAuthority().toString().equals("CL")
											|| elementoCorrente.getTipoAuthority().toString().equals("DE")
											|| elementoCorrente.getTipoAuthority().toString().equals("SO")
											|| elementoCorrente.getTipoAuthority().toString().equals("TH")))) {
					} else {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.cond001"));
						this.saveErrors(request, errors);
						this.caricaListaEsamina(request, analiticaTitoloForm);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						return mapping.getInputForward();
					}
				}
			}
			ActionForward actionForward = this.invioIndiceOggetto(mapping, request, analiticaTitoloForm, analiticaTitoloForm.getTreeElementViewTitoli().getKey());
			if (actionForward == null) {

				// Se l'operazione è andata bene si prospetta il successivo bid di Polo da catalogare
				if (analiticaTitoloForm.getListaBidDaFilePresent() != null
						&& analiticaTitoloForm.getListaBidDaFilePresent().equals("SI")) {
					int size = analiticaTitoloForm.getListaBidDaFile().size();
					for (int i = 0; i < size; i++) {
						if (analiticaTitoloForm.getBidRoot().equals(analiticaTitoloForm.getListaBidDaFile().get(i))) {
							analiticaTitoloForm.getListaBidDaFile().set(i, "----------");
							if (i < (size - 1)) {
								analiticaTitoloForm.setBidRoot((String) analiticaTitoloForm.getListaBidDaFile().get(++i));
								int codRit = impostaReticolo(request, analiticaTitoloForm);
								if (codRit < 0) {
									NavigationForward forward = Navigation.getInstance(request).goBack();
									forward.setRedirect(true);
									return forward;
								}

								this.caricaListaEsamina(request, analiticaTitoloForm);
								request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
								this.saveErrors(request, errors);

								saveToken(request);
								analiticaTitoloForm.setRadioItemSelez(String.valueOf(analiticaTitoloForm.getTreeElementViewTitoli().getRepeatableId()));
								return mapping.getInputForward();
							} else {
								int codRit = impostaReticolo(request, analiticaTitoloForm);
								if (codRit < 0) {
									NavigationForward forward = Navigation.getInstance(request).goBack();
									forward.setRedirect(true);
									return forward;
								}

								this.caricaListaEsamina(request, analiticaTitoloForm);
								request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
								this.saveErrors(request, errors);
								resetToken(request);
								return mapping.getInputForward();
							}
						}
					}
				} else {
					request.setAttribute("livRicerca", "I");
					int codRit = impostaReticolo(request, analiticaTitoloForm);
					if (codRit < 0) {
						NavigationForward forward = Navigation.getInstance(request).goBack();
						forward.setRedirect(true);
						return forward;
					}

					this.caricaListaEsamina(request, analiticaTitoloForm);
					request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}
			} else {
				return actionForward;
			}
		} else if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(
				MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION)) {
			// controllo sulla presenza all'interno del reticolo dell'elemento selezionato di elementi arrivo di legami NON CONDIVISI !!!

			// Intervento interno del 05.03.2013 anche i legami di tipo 464 (spogli) non impediscono l'invio in Indice
			// di un oggetto solo locale: inserito l'or su tipoLegame 464
			TreeElementViewTitoli elementoTree =
				(TreeElementViewTitoli) analiticaTitoloForm.getTreeElementViewTitoli().findElementUnique(Integer.valueOf(analiticaTitoloForm.getRadioItemSelez()));
			int sizeTree = elementoTree.getChildren().size();
			for (int i=0; i < sizeTree; i++) {
				TreeElementViewTitoli elementoCorrente = (TreeElementViewTitoli) elementoTree.getChildren().get(i);
				if (!elementoCorrente.isFlagCondiviso()) {
					if (elementoCorrente.isAutoreFormaRinvio()
							|| elementoCorrente.isLuogoFormaRinvio()
							|| elementoCorrente.getDatiLegame().getTipoLegame().equals("463")
							|| elementoCorrente.getDatiLegame().getTipoLegame().equals("464")
							|| (elementoCorrente.getTipoAuthority() != null
									&& (elementoCorrente.getTipoAuthority().toString().equals("AB")
											|| elementoCorrente.getTipoAuthority().toString().equals("CL")
											|| elementoCorrente.getTipoAuthority().toString().equals("DE")
											|| elementoCorrente.getTipoAuthority().toString().equals("SO")
											|| elementoCorrente.getTipoAuthority().toString().equals("TH")))) {
					} else {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.cond001"));
						this.saveErrors(request, errors);
						this.caricaListaEsamina(request, analiticaTitoloForm);
						request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
						return mapping.getInputForward();
					}
				}
			}


			// Inizio modifica almaviva2 del 2010.11.08 per BUG MANTIS 3971
			// si deve controllare se siamo al ritorno dalla Sintetica nella quale si è già controllato i simili
			// e scelto di Confermare la Catalogazione in indice (request.setAttribute("confermaInvioIndice", "CONFERMATO"));

			if (!"CONFERMATO".equals(request.getAttribute("confermaInvioIndice"))) {
				//  Inizio RICERCA di eventiuali Simili in Indice

				AreaTabellaOggettiDaCondividereVO areaTabellaOggettiDaCondividereVO = ricercaRadicePerCondivisione(elementoTree, request, analiticaTitoloForm);

				if (!areaTabellaOggettiDaCondividereVO.getCodErr().equals("0000")) {
					analiticaTitoloForm.setLivRicerca("P");
					analiticaTitoloForm.setVisualVaiA("SI");
					this.caricaListaEsamina(request, analiticaTitoloForm);
					request.setAttribute("testTree", analiticaTitoloForm
							.getTreeElementViewTitoli());

					boolean bidRoot = true;
					if (elementoTree.getTipoAuthority() == null) {
						analiticaTitoloForm.setGestioneInferiori("SI");
						caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree,	bidRoot);
					}
					resetToken(request);
					return mapping.getInputForward();
				}

				if (areaTabellaOggettiDaCondividereVO.getNumNotizie() > 0) {
					AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassaggioInterrogazioneTitoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSimili(true);
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setLivelloTrovato("I");
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setIdLista(areaTabellaOggettiDaCondividereVO.getIdLista());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSintetica(areaTabellaOggettiDaCondividereVO.getListaSintetica());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setMaxRighe(areaTabellaOggettiDaCondividereVO.getMaxRighe());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumBlocco(areaTabellaOggettiDaCondividereVO.getNumBlocco());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumNotizie(areaTabellaOggettiDaCondividereVO.getNumNotizie());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumPrimo(areaTabellaOggettiDaCondividereVO.getNumPrimo());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotBlocchi(areaTabellaOggettiDaCondividereVO.getTotBlocchi());
					areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotRighe(areaTabellaOggettiDaCondividereVO.getTotRighe());

					request.setAttribute("bidRoot",	"NO");
					request.setAttribute("areaDatiPassReturnSintetica",	areaDatiPassaggioInterrogazioneTitoloReturnVO);

					if (elementoTree.getTipoAuthority() != null
							&& elementoTree.getTipoAuthority().toString().equals("AU")) {
						AreaDatiVariazioneAutoreVO areaDatiVariazioneAutoreVO = new AreaDatiVariazioneAutoreVO();
						areaDatiVariazioneAutoreVO.setDettAutoreVO(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioAutoreGeneraleVO());
						request.setAttribute("areaDatiPassPerConfVariazione", areaDatiVariazioneAutoreVO);

						AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
						impostaAreaPerVariaLegameAutore(mapping, request, elementoTree, analiticaTitoloForm.getBidRoot(), areaDatiLegameTitoloVO);
						request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);

						resetToken(request);
						return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaAutoriPerCondividi"));
					} else {
						AreaDatiVariazioneTitoloVO areaDatiVariazioneTitoloVO = new AreaDatiVariazioneTitoloVO();
						areaDatiVariazioneTitoloVO.setDetTitoloPFissaVO(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO());
						areaDatiVariazioneTitoloVO.setDetTitoloGraVO(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO().getDetTitoloGraVO());
						areaDatiVariazioneTitoloVO.setDetTitoloMusVO(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO().getDetTitoloMusVO());
						areaDatiVariazioneTitoloVO.setDetTitoloCarVO(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO().getDetTitoloCarVO());
						request.setAttribute("areaDatiPassPerConfVariazione", areaDatiVariazioneTitoloVO);

						AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
						impostaAreaPerVariaLegameTitolo(mapping, request, elementoTree, analiticaTitoloForm.getBidRoot(), areaDatiLegameTitoloVO);
						request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);

						resetToken(request);
						return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerCondividi"));
					}

				}
			}
			// Fine modifica almaviva2 del 2010.11.08 per BUG MANTIS 3971



			ActionForward actionForward = this.invioIndiceOggetto(mapping, request, analiticaTitoloForm, elementoTree.getKey());
			if (actionForward == null) {
				request.setAttribute("livRicerca", "P");
				int codRit = impostaReticolo(request, analiticaTitoloForm);
				if (codRit < 0) {
					NavigationForward forward = Navigation.getInstance(request).goBack();
					forward.setRedirect(true);
					return forward;
				}

				this.caricaListaEsamina(request, analiticaTitoloForm);
				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			} else {
				return actionForward;
			}
		}
		return null;
	}

	private ActionForward invioIndiceOggetto(ActionMapping mapping, HttpServletRequest request,
			AnaliticaTitoloForm analiticaTitoloForm, String keyElemento ) throws Exception {


		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli) analiticaTitoloForm.getTreeElementViewTitoli().findElement(keyElemento);

		Boolean elaborRadice = false;
		if (elementoTree.getKey().equals(analiticaTitoloForm.getBidRoot())) {
			elaborRadice = true;
		}

		String newLivAut = elementoTree.getLivelloAutorita();

		SbnAuthority elemAuth = elementoTree.getTipoAuthority();
		if (Integer.valueOf(elementoTree.getLivelloAutorita()) < 5) {

			// Inizio Intervento interno 12.11.2012 Se livello Autorità di un documento SOLO LOCALE è inferiore a 05 e quindi
			// non catalogabile in Indice, tale livello viene di default impostato a 51;
			newLivAut = "51";
			elementoTree.setLivelloAutorita(newLivAut);
//			try{
//				if (elemAuth == null) {
//					newLivAut = utenteEjb.getLivAutDocumenti(elementoTree.getTipoMateriale());
//				} else {
//					newLivAut = utenteEjb.getLivAutAuthority(elemAuth.toString());
//				}
//
//			}catch(UtenteNotAuthorizedException ute)
//			{
//				ActionMessages errors = new ActionMessages();
//				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
//				this.saveErrors(request, errors);
//				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
//				this.caricaListaEsamina(request, analiticaTitoloForm);
//				resetToken(request);
//				return mapping.getInputForward();
//			} catch (RemoteException e) {
//				ActionMessages errors = new ActionMessages();
//				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
//				this.saveErrors(request, errors);
//				request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
//				this.caricaListaEsamina(request, analiticaTitoloForm);
//				resetToken(request);
//				return mapping.getInputForward();
//			}
			// Fine Intervento interno 12.11.2012
		}

		// Svolgimento punto 1.
		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (elemAuth == null
				|| SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {

			AreaDatiVariazioneTitoloVO areaDatiPass = new AreaDatiVariazioneTitoloVO();
			areaDatiPass.setDetTitoloPFissaVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO());
			areaDatiPass.setDetTitoloCarVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloCarVO());
			areaDatiPass.setDetTitoloGraVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloGraVO());
			areaDatiPass.setDetTitoloMusVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloMusVO());

			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			areaDatiPass.setDetTitoloAudVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloAudVO());
			areaDatiPass.setDetTitoloEleVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloEleVO());

			// almaviva2 Mantis 5620: Intervento Interno per intervenire nel caso in cui il LivAutSpec non sia null ma vuoto
			//almaviva5_20140908 #5635 il metodo String.isEmpty() non è supportato da java5
			areaDatiPass.getDetTitoloPFissaVO().setLivAut(newLivAut);
			if (areaDatiPass.getDetTitoloCarVO() != null)  {
				if (!ValidazioneDati.isFilled(areaDatiPass.getDetTitoloCarVO().getLivAutSpec())) {
					areaDatiPass.getDetTitoloCarVO().setLivAutSpec(newLivAut);
				} else if (Integer.valueOf(areaDatiPass.getDetTitoloCarVO().getLivAutSpec()) < 5)  {
					areaDatiPass.getDetTitoloCarVO().setLivAutSpec(newLivAut);
				}
			}
			if (areaDatiPass.getDetTitoloGraVO() != null)  {
				if (!ValidazioneDati.isFilled(areaDatiPass.getDetTitoloGraVO().getLivAutSpec())) {
					areaDatiPass.getDetTitoloGraVO().setLivAutSpec(newLivAut);
				} else
				if (Integer.valueOf(areaDatiPass.getDetTitoloGraVO().getLivAutSpec()) < 5)  {
					areaDatiPass.getDetTitoloGraVO().setLivAutSpec(newLivAut);
				}
			}
			if (areaDatiPass.getDetTitoloMusVO() != null)  {
				if (!ValidazioneDati.isFilled(areaDatiPass.getDetTitoloMusVO().getLivAutSpec())) {
					areaDatiPass.getDetTitoloMusVO().setLivAutSpec(newLivAut);
				} else
				if (Integer.valueOf(areaDatiPass.getDetTitoloMusVO().getLivAutSpec()) < 5)  {
					areaDatiPass.getDetTitoloMusVO().setLivAutSpec(newLivAut);
				}
			}

			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			if (areaDatiPass.getDetTitoloAudVO() != null)  {
				if (!ValidazioneDati.isFilled(areaDatiPass.getDetTitoloAudVO().getLivAutSpec())) {
					areaDatiPass.getDetTitoloAudVO().setLivAutSpec(newLivAut);
				} else
				if (Integer.valueOf(areaDatiPass.getDetTitoloAudVO().getLivAutSpec()) < 5)  {
					areaDatiPass.getDetTitoloAudVO().setLivAutSpec(newLivAut);
				}
			}
			if (areaDatiPass.getDetTitoloEleVO() != null)  {
				if (!ValidazioneDati.isFilled(areaDatiPass.getDetTitoloEleVO().getLivAutSpec())) {
					areaDatiPass.getDetTitoloEleVO().setLivAutSpec(newLivAut);
				} else
				if (Integer.valueOf(areaDatiPass.getDetTitoloEleVO().getLivAutSpec()) < 5)  {
					areaDatiPass.getDetTitoloEleVO().setLivAutSpec(newLivAut);
				}
			}


			// il flag conferma viene impostato a true per non ripetere la gestione di ricerca simili
			// MODIFICATO IL FLAG DI CONFERMA: LA RICERCA DEI SIMILI DEVE ESSRE FATTA PER TUTTI I FIGLI ?????????????????

			// Modifica Intervento interno 05.03.2010 almaviva2 - dopo la prima ricerca dei simili con esito negativo,
			// nel caso di radice si forza la catalogazione in Indice senza ulteriore ricerca (inserito else if)
			areaDatiPass.setConferma(false);
			if (request.getAttribute("confermaInvioIndice") != null) {
				if ("CONFERMATO".equals(request.getAttribute("confermaInvioIndice"))) {
					areaDatiPass.setConferma(true);
				}
			} else if (elaborRadice) {
				areaDatiPass.setConferma(true);
			}

			areaDatiPass.setModifica(false);

			areaDatiPass.setNaturaTitoloDaVariare(areaDatiPass.getDetTitoloPFissaVO().getNatura());
			areaDatiPass.setVariazioneCompAntico(true);
			areaDatiPass.setVariazioneTuttiComp(true);

			areaDatiPass.setFlagCondiviso(false);
			areaDatiPass.setInserimentoIndice(true);
			areaDatiPass.setInserimentoPolo(false);

			if (analiticaTitoloForm.getTabellaTimeStampPolo().get(elementoTree.getKey()) != null) {
				areaDatiPass.setTimeStampPolo((String) analiticaTitoloForm.getTabellaTimeStampPolo().get(elementoTree.getKey()));
			} else {
				areaDatiPass.setTimeStampPolo(elementoTree.getT005());
			}


			// Inizio Viene impostata tutta l'area SbnMarc per l'inserimento dei legami contestuale all'inserimento della notizia
			LegamiType legamiType = new LegamiType();
			legamiType.setIdPartenza(elementoTree.getKey());
			legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

			ArrivoLegame arrivoLegame;
			String keyFiglio = "";
			int size = elementoTree.getChildren().size();
			for (int i=0; i < size; i++) {

				arrivoLegame = new ArrivoLegame();

				TreeElementViewTitoli treeElementViewFiglio = (TreeElementViewTitoli) elementoTree.getChildren().get(i);

				// Intervento interno del 05.03.2013 anche i legami di tipo 464 (spogli) non impediscono l'invio in Indice
				// di un oggetto solo locale: inserito l'or su tipoLegame 464
				if (treeElementViewFiglio.getDatiLegame().getTipoLegame().equals("463")
						|| (treeElementViewFiglio.getDatiLegame().getTipoLegame().equals("464") &&
											treeElementViewFiglio.getDatiLegame().isFlagCondiviso() == false)) {
					continue;
				}

				keyFiglio = treeElementViewFiglio.getKey();

				SbnAuthority figlioAuth = treeElementViewFiglio.getTipoAuthority();
				if (figlioAuth != null) {
					if (!(SBNMarcUtil.eqAuthority(figlioAuth,
							SbnAuthority.AB,
							SbnAuthority.CL,
							SbnAuthority.PP,
							SbnAuthority.DE,
							SbnAuthority.RE,
							SbnAuthority.SO))) {

						LegameElementoAutType legameElementoAutType = new LegameElementoAutType();
						legameElementoAutType.setIdArrivo(keyFiglio);
						legameElementoAutType.setCondiviso(LegameElementoAutTypeCondivisoType.S);
						if (treeElementViewFiglio.getDatiLegame().isIncerto()) {
							legameElementoAutType.setIncerto(SbnIndicatore.S);
						} else {
							legameElementoAutType.setIncerto(SbnIndicatore.N);
						}
						legameElementoAutType.setNoteLegame(treeElementViewFiglio.getDatiLegame().getNotaLegame());
						legameElementoAutType.setRelatorCode(treeElementViewFiglio.getDatiLegame().getRelatorCode());
						if (treeElementViewFiglio.getDatiLegame().isSuperfluo()) {
							legameElementoAutType.setSuperfluo(SbnIndicatore.S);
						} else {
							legameElementoAutType.setIncerto(SbnIndicatore.N);
						}
						legameElementoAutType.setTipoAuthority(figlioAuth);

						// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
						// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
						//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
						legameElementoAutType.setStrumento(treeElementViewFiglio.getDatiLegame().getSpecStrumVoci());

						legameElementoAutType.setTipoLegame(SbnLegameAut.valueOf(treeElementViewFiglio.getDatiLegame().getTipoLegame()));
						if (treeElementViewFiglio.getDatiLegame().getResponsabilita() != null) {
							legameElementoAutType.setTipoRespons(SbnRespons.valueOf(treeElementViewFiglio.getDatiLegame().getResponsabilita()));
						}
						arrivoLegame.setLegameElementoAut(legameElementoAutType);
						legamiType.addArrivoLegame(arrivoLegame);
					}
				} else {
					if (treeElementViewFiglio.getNatura() != null
							&& (treeElementViewFiglio.getNatura().equals("B")
									|| treeElementViewFiglio.getNatura().equals("D")
									|| treeElementViewFiglio.getNatura().equals("P")
									|| treeElementViewFiglio.getNatura().equals("T"))) {

						LegameTitAccessoType legameTitAccessoType = new LegameTitAccessoType();
						legameTitAccessoType.setIdArrivo(keyFiglio);
						legameTitAccessoType.setCondiviso(LegameTitAccessoTypeCondivisoType.S);
						legameTitAccessoType.setNoteLegame(treeElementViewFiglio.getDatiLegame().getNotaLegame());
						legameTitAccessoType.setSequenzaMusica(treeElementViewFiglio.getDatiLegame().getSequenzaMusica());
						if (treeElementViewFiglio.getDatiLegame().getSottoTipoLegame() != null) {
							legameTitAccessoType.setSottoTipoLegame(SbnSpecLegameDoc.valueOf(treeElementViewFiglio.getDatiLegame().getSottoTipoLegame()));
						}
						legameTitAccessoType.setTipoLegame(SbnLegameTitAccesso.valueOf(treeElementViewFiglio.getDatiLegame().getTipoLegame()));
						arrivoLegame.setLegameTitAccesso(legameTitAccessoType);
						legamiType.addArrivoLegame(arrivoLegame);
					} else {
						LegameDocType legameDocType = new LegameDocType();
						legameDocType.setIdArrivo(keyFiglio);
						legameDocType.setCondiviso(LegameDocTypeCondivisoType.S);
						legameDocType.setNoteLegame(treeElementViewFiglio.getDatiLegame().getNotaLegame());
						legameDocType.setSequenza(treeElementViewFiglio.getDatiLegame().getSequenza());
						legameDocType.setSici(treeElementViewFiglio.getDatiLegame().getSici());
						legameDocType.setTipoLegame(SbnLegameDoc.valueOf(treeElementViewFiglio.getDatiLegame().getTipoLegame()));
						arrivoLegame.setLegameDoc(legameDocType);
						legamiType.addArrivoLegame(arrivoLegame);
					}
				}
			}

			if (legamiType.getArrivoLegameCount() > 0) {
				areaDatiPass.setLegameDaCondividere(true);
				areaDatiPass.setLegamiTypeDaCondividere(legamiType);
			}
			// Fine Viene impostata tutta l'area SbnMarc per l'inserimento dei legami contestuale all'inserimento della notizia

			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.inserisciTitolo(areaDatiPass, Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {

			AreaDatiVariazioneAutoreVO areaDatiPass = new AreaDatiVariazioneAutoreVO();
			areaDatiPass.setDettAutoreVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO());
			areaDatiPass.getDettAutoreVO().setLivAut(newLivAut);

			// il flag conferma viene impostato a true per non ripetere la gestione di ricerca simili
			// // Intervento interno almaviva2 30.03.2011 - inserito valore true sulla richeidat di inserimento in seconda battuta MAIL almaviva
//			areaDatiPass.setConferma(false);
			areaDatiPass.setConferma(true);

			areaDatiPass.setModifica(false);

			areaDatiPass.setFlagCondiviso(false);
			areaDatiPass.setInserimentoIndice(true);
			areaDatiPass.setInserimentoPolo(false);
			if (analiticaTitoloForm.getTabellaTimeStampPolo().get(elementoTree.getKey()) != null) {
				areaDatiPass.getDettAutoreVO().setVersione((String) analiticaTitoloForm.getTabellaTimeStampPolo().get(elementoTree.getKey()));
			}


			// Inizio Viene impostata tutta l'area SbnMarc per l'inserimento dei legami contestuale all'inserimento della notizia

			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO;
			String keyFiglio = "";

			int size = elementoTree.getChildren().size();
			for (int i=0; i < size; i++) {

				TreeElementViewTitoli treeElementViewFiglio = (TreeElementViewTitoli) elementoTree.getChildren().get(i);
				keyFiglio = treeElementViewFiglio.getKey();

				SbnAuthority figlioAuth = treeElementViewFiglio.getTipoAuthority();
				if (figlioAuth != null
						&& SBNMarcUtil.eqAuthority(figlioAuth, SbnAuthority.AU)
						&& treeElementViewFiglio.isAutoreFormaRinvio()) {

					areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();

					areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
					areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getKey());
					areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getDescription());
					areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.isFlagCondiviso());
					areaDatiLegameTitoloVO.setTipoOperazione("Crea");
					areaDatiLegameTitoloVO.setLivAutBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioAutoreGeneraleVO().getLivAut());
					areaDatiLegameTitoloVO.setTimeStampBidPartenza(elementoTree.getAreaDatiDettaglioOggettiVO()
								.getDettaglioAutoreGeneraleVO().getVersione());

					areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("AU");
					areaDatiLegameTitoloVO.setIdArrivo(keyFiglio);
					areaDatiLegameTitoloVO.setDescArrivo(treeElementViewFiglio.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getNome());
					areaDatiLegameTitoloVO.setTipoNomeArrivo(treeElementViewFiglio.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getTipoNome());
					areaDatiLegameTitoloVO.setFormaIdArrivo(treeElementViewFiglio.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getForma());
					areaDatiLegameTitoloVO.setNotaInformativaIdArrivo(treeElementViewFiglio.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getNotaInformativa());
					areaDatiLegameTitoloVO.setLivAutIdArrivo(treeElementViewFiglio.getLivelloAutorita());
					areaDatiLegameTitoloVO.setTimeStampBidArrivo(treeElementViewFiglio.getT005());

					areaDatiLegameTitoloVO.setTipoLegameNew("4XX");
					areaDatiLegameTitoloVO.setNoteLegameNew(treeElementViewFiglio.getDatiLegame().getNotaLegame());

					areaDatiLegameTitoloVO.setFlagCondivisoLegame(true);
					areaDatiLegameTitoloVO.setInserimentoIndice(true);
					areaDatiLegameTitoloVO.setInserimentoPolo(false);

					areaDatiPass.setLegameDaCondividere(true);
					areaDatiPass.addListaAreaDatiLegameTitoloVO(areaDatiLegameTitoloVO);
				}
			}


			// Fine Viene impostata tutta l'area SbnMarc per l'inserimento dei legami contestuale all'inserimento della notizia

			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.inserisciAutore(areaDatiPass, Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>"	+ e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.findForward("annulla");
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			analiticaTitoloForm.setAreaLogIncrementale(analiticaTitoloForm.getAreaLogIncrementale()
					+ "<br />" + elementoTree.getKey() + " : " + areaDatiPassReturn.getTestoProtocollo());
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("0000")
				&& areaDatiPassReturn.getTestoProtocolloInformational() != null) {
			analiticaTitoloForm.setAreaLogIncrementale(analiticaTitoloForm.getAreaLogIncrementale()
					+ "<br />" + elementoTree.getKey() + " : " + areaDatiPassReturn.getTestoProtocolloInformational());


			if (Navigation.getInstance(request).bookmarkExists(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE)
					&& Navigation.getInstance(request).bookmarkExists(keyElemento)) {
				Navigation.getInstance(request).removeBookmark(keyElemento);

				request.setAttribute("esitoAnalitica", "CATALOGATO");
				return Navigation.getInstance(request).goToBookmark(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE, false);
			}

		}

		if (areaDatiPassReturn.getNumNotizie() > 0) {
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassaggioInterrogazioneTitoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSimili(true);
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setLivelloTrovato("I");
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setIdLista(areaDatiPassReturn.getIdLista());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSintetica(areaDatiPassReturn.getListaSintetica());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setMaxRighe(areaDatiPassReturn.getMaxRighe());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumBlocco(areaDatiPassReturn.getNumBlocco());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumNotizie(areaDatiPassReturn.getNumNotizie());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumPrimo(areaDatiPassReturn.getNumPrimo());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotRighe(areaDatiPassReturn.getTotRighe());
			request.setAttribute("areaDatiPassReturnSintetica",	areaDatiPassaggioInterrogazioneTitoloReturnVO);


			if (elemAuth == null) {
				AreaDatiVariazioneTitoloVO areaDatiVariazioneTitoloVO = new AreaDatiVariazioneTitoloVO();
				areaDatiVariazioneTitoloVO.setDetTitoloPFissaVO(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO());
				areaDatiVariazioneTitoloVO.setDetTitoloGraVO(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloGraVO());
				areaDatiVariazioneTitoloVO.setDetTitoloMusVO(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloMusVO());
				areaDatiVariazioneTitoloVO.setDetTitoloCarVO(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloCarVO());
				areaDatiVariazioneTitoloVO.setDetTitoloCarVO(elementoTree.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloCarVO());
				request.setAttribute("areaDatiPassPerConfVariazione", areaDatiVariazioneTitoloVO);
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerCondividi"));
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.AU)) {
				AreaDatiVariazioneAutoreVO areaDatiVariazioneAutoreVO = new AreaDatiVariazioneAutoreVO();
				areaDatiVariazioneAutoreVO.setDettAutoreVO(elementoTree.getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO());
				request.setAttribute("areaDatiPassPerConfVariazione", areaDatiVariazioneAutoreVO);
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaAutoriPerCondividi"));
			} else if (SBNMarcUtil.eqAuthority(elemAuth, SbnAuthority.TU, SbnAuthority.UM)) {
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerCondividi"));
			}
		}

		if (elaborRadice) {
			if (areaDatiPassReturn.getBid() != null
				&& !areaDatiPassReturn.getBid().equals("")) {
				analiticaTitoloForm.setBidRoot(areaDatiPassReturn.getBid());
			}
		}

		elementoTree.setKey(areaDatiPassReturn.getBid());
		elementoTree.setLivelloAutorita(newLivAut);
		analiticaTitoloForm.getTabellaTimeStampIndice().put(elementoTree.getKey(), areaDatiPassReturn.getVersioneIndice());
		analiticaTitoloForm.getTabellaTimeStampPolo().put(elementoTree.getKey(), areaDatiPassReturn.getVersionePolo());

		if (Navigation.getInstance(request).bookmarkExists(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE)
				&& Navigation.getInstance(request).bookmarkExists(keyElemento)) {
			Navigation.getInstance(request).removeBookmark(keyElemento);

			request.setAttribute("esitoAnalitica", "CATALOGATO");
			return Navigation.getInstance(request).goToBookmark(TitoliCollegatiInvoke.GESTIONE_DA_SINT_SCHEDE, false);

		}

		return null;
	}


	public ActionForward catturaSuggerimentoAcquisto(ActionMapping mapping, HttpServletRequest request,
			AnaliticaTitoloForm analiticaTitoloForm, TreeElementViewTitoli elementoTree, HttpServletResponse response)
			throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
		AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();

		areaDatiPassCattura.setIdPadre(elementoTree.getKey());
		if (elementoTree.getTipoAuthority() == null) {areaDatiPassCattura.setTipoAuthority("");
		} else {
			areaDatiPassCattura.setTipoAuthority(elementoTree.getTipoAuthority().toString());
		}
		String[] appo = new String[0];
		areaDatiPassCattura.setInferioriDaCatturare(appo);
		areaDatiPassCattura.setSoloCopia(true);
		areaDatiPassCattura.setSoloRadice(true);
		areaDatiPassCattura.setCreaNuovoId(true);


		try {
			areaDatiPassReturnCattura = factory.getGestioneBibliografica()
					.catturaReticolo(areaDatiPassCattura, Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	"ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			this.caricaListaEsamina(request, analiticaTitoloForm);
			request.setAttribute("testTree", analiticaTitoloForm
					.getTreeElementViewTitoli());
		}

		if (areaDatiPassReturnCattura == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.findForward("interrogazioneTitolo");
		}

		if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
			if (areaDatiPassReturnCattura.getCodErr().equals("9999")
					|| areaDatiPassReturnCattura.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",	areaDatiPassReturnCattura.getTestoProtocollo()));
				this.saveErrors(request, errors);
			} else if (!areaDatiPassReturnCattura.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica."	+ areaDatiPassReturnCattura.getCodErr()));
				this.saveErrors(request, errors);
			}
			resetToken(request);
			return mapping.getInputForward();
		}

		analiticaTitoloForm.setLivRicerca("P");
		request.setAttribute("livRicerca", "P");
		request.setAttribute("bid", areaDatiPassReturnCattura.getBid());

		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAAcquisSelez().equals(MenuHome.FUNZ_ACQUISIZIONE_CATTURA_A_SUGG_E_ORDINE)) {
			request.setAttribute("bid", areaDatiPassReturnCattura.getBid());
			request.setAttribute("desc", elementoTree.getDescription());
			request.setAttribute("natura", elementoTree.getNatura());

			int codRit = impostaReticolo(request, analiticaTitoloForm);
			if (codRit < 0) {
				NavigationForward forward = Navigation.getInstance(request).goBack();
				forward.setRedirect(true);
				return forward;
			}

			impostazioniVaiA( mapping, analiticaTitoloForm,  request, response);

			ActionMessages errors = new ActionMessages();
			errors.add("", new ActionMessage("errors.acquisizioni.gestBiblOperazioneOk", elementoTree.getKey(), areaDatiPassReturnCattura.getBid()));
			this.saveErrors(request, errors);
			return mapping.findForward("ordineDopoCatturaASuggerimento");
		}
		if (analiticaTitoloForm.getInterrogazioneVaiAForm().getVaiAGestBibliogSelez().equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_A_SUGGERIMENTO)) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			Navigation.getInstance(request).setTesto("Notizia corrente");
		}

		analiticaTitoloForm.setVisualVaiA("SI");
		this.caricaListaEsamina(request, analiticaTitoloForm);
		request.setAttribute("testTree", analiticaTitoloForm.getTreeElementViewTitoli());

		int codRit = impostaReticolo(request, analiticaTitoloForm);
		if (codRit < 0) {
			NavigationForward forward = Navigation.getInstance(request).goBack();
			forward.setRedirect(true);
			return forward;
		}

		boolean bidRoot = true;

		if (elementoTree.getTipoAuthority() == null) {
			analiticaTitoloForm.setGestioneInferiori("SI");
			caricaComboTitoloVaiA(request,analiticaTitoloForm, elementoTree,	bidRoot);
		}
		return impostazioniVaiA( mapping, analiticaTitoloForm,  request, response);
	}


	public boolean verificaLivelloAutorita() {

		// VERIFICARE LA PRESENZA DEI METODI OPPORRUNI
		return true;
	}



	public void creaTabellaTimeStamp(TreeElementViewTitoli elementoReticolo, Map<String, String> tabellaTimeStamp)
		throws Exception {

		tabellaTimeStamp.put(elementoReticolo.getKey(), elementoReticolo.getT005());
		for (int i=0; i<elementoReticolo.getChildren().size(); i++) {
			creaTabellaTimeStamp( (TreeElementViewTitoli) elementoReticolo.getChildren().get(i), tabellaTimeStamp);
		}
		return;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		AnaliticaTitoloForm analiticaTitoloForm = (AnaliticaTitoloForm) form;

		// almaviva5_20090722 #3098
		if (idCheck.equalsIgnoreCase("INFERIORI")) {
			TreeElementViewTitoli reticolo = analiticaTitoloForm.getTreeElementViewTitoli();
			if (reticolo == null)
				return true;

			List<TreeElementView> nodes = reticolo.traverse();
			for (TreeElementView t : nodes) {
				TreeElementViewTitoli tit = (TreeElementViewTitoli) t;
				DatiLegame datiLegame = tit.getDatiLegame();
				if (datiLegame == null)
					continue;
				if (datiLegame.getTipoLegame().equals("463") || datiLegame.getTipoLegame().equals("464"))
					return true;
			}
			return false;
		}

		return true;
	}


	public ActionForward impostaAreaPerVariaLegameTitolo(ActionMapping mapping, HttpServletRequest request,
			TreeElementViewTitoli elementoTree,	String bidRoot, AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) throws Exception {

		// POSIZIONAMENTO SU UN OGGETTO DI TIPO TITOLO
		areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
		areaDatiLegameTitoloVO.setDescArrivo(elementoTree.getDescription());
		areaDatiLegameTitoloVO.setNaturaBidArrivo(elementoTree.getNatura());
		// Modifica almaviva2 23.10.2009 BUG 3268
		if (elementoTree.getTipoAuthority() != null) {
			areaDatiLegameTitoloVO.setAuthorityOggettoArrivo(elementoTree.getTipoAuthority().toString());
		}
		areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());
		areaDatiLegameTitoloVO.setTipoLegameNew(elementoTree.getDatiLegame().getTipoLegame());
		areaDatiLegameTitoloVO.setSequenzaNew(elementoTree.getDatiLegame().getSequenza());
		areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());
		areaDatiLegameTitoloVO.setSiciNew(elementoTree.getDatiLegame().getSici());
		areaDatiLegameTitoloVO.setSottoTipoLegameNew(elementoTree.getDatiLegame().getSottoTipoLegame());
		areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
		SbnAuthority parentAuth = elementoTree.getParent() != null ? elementoTree.getParent().getTipoAuthority() : null;
		if (parentAuth == null
				|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
			// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-titolo
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
			if (bidRoot != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(bidRoot);
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
							.getParent()).getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getTipoMateriale());
//			 Modifica almaviva2 23.10.2009 BUG 3268
			if (elementoTree.getParent().getTipoAuthority() != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(parentAuth.toString());
			}
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
							.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			return mapping.findForward("variazionePerLegamiTitoloTitolo");
		}
		return null;
	}


	public ActionForward impostaAreaPerVariaLegameAutore(ActionMapping mapping, HttpServletRequest request,
			TreeElementViewTitoli elementoTree,	String bidRoot, AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) throws Exception {

		// POSIZIONAMENTO SU UN OGGETTO DI TIPO AUTORE
		// E' NECASSARIO INSERIRE LA PARTE DEI DATI OLD PERCHE' LA MODIFICA DI QUESTO
		// TIPO DI LEGAME CONSISTE IN CANCELLA-INSERISCI

		areaDatiLegameTitoloVO.setIdArrivo(elementoTree.getKey());
		areaDatiLegameTitoloVO.setDescArrivo(elementoTree.getDescription());
		areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("AU");
		areaDatiLegameTitoloVO.setFlagCondivisoArrivo(elementoTree.isFlagCondiviso());

		areaDatiLegameTitoloVO.setRelatorCodeOld(elementoTree.getDatiLegame().getRelatorCode());
		areaDatiLegameTitoloVO.setTipoResponsOld(elementoTree.getDatiLegame().getResponsabilita());
		areaDatiLegameTitoloVO.setSuperfluoOld(elementoTree.getDatiLegame().isSuperfluo());
		areaDatiLegameTitoloVO.setIncertoOld(elementoTree.getDatiLegame().isIncerto());
		areaDatiLegameTitoloVO.setTipoLegameOld(elementoTree.getDatiLegame().getTipoLegame());
		areaDatiLegameTitoloVO.setNoteLegameOld(elementoTree.getDatiLegame().getNotaLegame());

		areaDatiLegameTitoloVO.setRelatorCodeNew(elementoTree.getDatiLegame().getRelatorCode());
		areaDatiLegameTitoloVO.setTipoResponsNew(elementoTree.getDatiLegame().getResponsabilita());
		areaDatiLegameTitoloVO.setSuperfluoNew(elementoTree.getDatiLegame().isSuperfluo());
		areaDatiLegameTitoloVO.setIncertoNew(elementoTree.getDatiLegame().isIncerto());
		areaDatiLegameTitoloVO.setTipoLegameNew(elementoTree.getDatiLegame().getTipoLegame());
		areaDatiLegameTitoloVO.setNoteLegameNew(elementoTree.getDatiLegame().getNotaLegame());

		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
		// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
		//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
		areaDatiLegameTitoloVO.setSpecStrumVociOld(elementoTree.getDatiLegame().getSpecStrumVoci());
		areaDatiLegameTitoloVO.setSpecStrumVociNew(elementoTree.getDatiLegame().getSpecStrumVoci());

		areaDatiLegameTitoloVO.setFlagCondivisoLegame(elementoTree.getDatiLegame().isFlagCondiviso());
		SbnAuthority parentAuth = elementoTree.getParent() != null ? elementoTree.getParent().getTipoAuthority() : null;
		if (parentAuth == null
				|| SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.TU, SbnAuthority.UM)) {
			// OGGETTO PADRE E' UN TITOLO - variazione legame titolo-autore
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
			if (bidRoot != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(bidRoot);
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
							.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getLivAut());
			areaDatiLegameTitoloVO.setNaturaBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getNatura());
			areaDatiLegameTitoloVO.setTipMatBidPartenza(((TreeElementViewTitoli) elementoTree.getParent()).getTipoMateriale());
			// Inizio Modifica 19.11.2009 - Richiesta interna manca impostazione dell'Authority di partenza
			if (parentAuth != null) {
				areaDatiLegameTitoloVO.setAuthorityOggettoPartenza(parentAuth.toString());
			}
			// Fine Modifica 19.11.2009 - Richiesta interna manca impostazione dell'Authority di partenza
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree.getParent())
							.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Modifica");

			request.setAttribute("AreaDatiLegameTitoloVO",areaDatiLegameTitoloVO);
			return mapping.findForward("variazionePerLegamiTitoloAutore");
		} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.AU)) {
			// OGGETTO PADRE E' UN AUTORE - variazione legame fra editori
			areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("AU");
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
			if (bidRoot != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(bidRoot);
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
							.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getLivAut());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
							.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
			request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
			return mapping.findForward("variazionePerLegamiFraAutority");
		} else if (SBNMarcUtil.eqAuthority(parentAuth, SbnAuthority.MA)) {
			// OGGETTO PADRE E' UNA MARCA - variazione legame marca-autore
			areaDatiLegameTitoloVO.setAuthorityOggettoPartenza("MA");
			areaDatiLegameTitoloVO.setBidPartenza(elementoTree.getParent().getKey());
			if (bidRoot != null) {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(bidRoot);
			} else {
				areaDatiLegameTitoloVO.setBidRientroAnalitica(elementoTree.getParent().getKey());
			}

			areaDatiLegameTitoloVO.setDescPartenza(elementoTree.getParent().getDescription());
			areaDatiLegameTitoloVO.setLivAutBidPartenza(((TreeElementViewTitoli) elementoTree
							.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioMarcaGeneraleVO().getLivAut());
			areaDatiLegameTitoloVO.setTimeStampBidPartenza(((TreeElementViewTitoli) elementoTree
							.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioMarcaGeneraleVO().getVersione());
			areaDatiLegameTitoloVO.setFlagCondivisoPartenza(elementoTree.getParent().isFlagCondiviso());
			areaDatiLegameTitoloVO.setTipoOperazione("Modifica");
			request.setAttribute("AreaDatiLegameTitoloVO",areaDatiLegameTitoloVO);
			return mapping.findForward("variazionePerLegamiFraAutority");
		}
		return null;
	}

	public ActionForward tornaAServizi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

 		Navigation navi = Navigation.getInstance(request);
 		if (navi.isFromBar())
			return mapping.getInputForward();

 		AnaliticaTitoloForm currentForm = (AnaliticaTitoloForm) form;

		String idx = currentForm.getRadioItemSelez();
		if (!ValidazioneDati.isFilled(idx)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selObblOggSint"));
			return mapping.getInputForward();
		}

		TreeElementViewTitoli root = currentForm.getTreeElementViewTitoli();
		TreeElementViewTitoli node = (TreeElementViewTitoli) root.findElementUnique(Integer.valueOf(idx));

		if (node.getTipoAuthority() != null) {//authority?
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selOggAuthNoGest"));
			return mapping.getInputForward();
		}
		DettaglioTitoloParteFissaVO dettaglio = node.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO();
		String bid = dettaglio.getBid();
		log.info("SERVIZI bid selezionato: " + bid);

		//almaviva5_20101103 #3958
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		if (!delegate.esisteInventarioBibliotecaSistemaMetro(currentForm.getFiltroBib(), bid) ) {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noInv"));
			return mapping.getInputForward();
		}

		request.setAttribute("bid", bid);
		request.setAttribute("titolo", dettaglio.getAreaTitTitolo());
		request.setAttribute("desc", dettaglio.getAreaTitTitolo());
		request.setAttribute("livRicerca", currentForm.getLivRicerca());

		request.setAttribute("elencoBiblSelezionate", currentForm.getElencoBibliotecheSelezionate());

		List<String> listaBiblio = new ArrayList<String>();
		for (BibliotecaVO b : currentForm.getFiltroBib())
			listaBiblio.add(b.getCod_bib());

        request.setAttribute("listaBiblio", listaBiblio);

		return navi.goForward(mapping.findForward("sifinventario"));
	}

	public void updateListaSchede(String bidDaAggiornare)	throws Exception {

		AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPassCiclicaVO = new AreaDatiPassaggioSchedaDocCiclicaVO();
		areaDatiPassCiclicaVO.setAggiornamento(true);
		areaDatiPassCiclicaVO.setBidDocLocale(bidDaAggiornare);
		areaDatiPassCiclicaVO.setStatoLavorRecordNew("4");
		areaDatiPassCiclicaVO.setIdArrivoFusione("");
		areaDatiPassCiclicaVO.setTipoTrattamento("3");




	}

}
