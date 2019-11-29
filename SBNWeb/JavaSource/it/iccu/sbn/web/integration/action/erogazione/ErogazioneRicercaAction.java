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
package it.iccu.sbn.web.integration.action.erogazione;

import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.FiltroCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneDocFisico;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ErogazioneRicercaFolder;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ErogazioneRicercaForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.docfisico.SezioneDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ErogazioneRicercaAction extends ErogazioneAction {

	private static Logger log = Logger.getLogger(ErogazioneRicercaAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca",              	"cerca");
		map.put("servizi.bottone.avanti",             	"cerca");
		map.put("servizi.bottone.annulla",            	"annulla");
		map.put("servizi.erogazione.tag.liste",       	"liste");
		map.put("servizi.erogazione.tag.ricerca",     	"ricerca");
		map.put("servizi.bottone.prenotazioni",       	"prenotazioni");

		map.put("servizi.bottone.hlputente",          	"sif_utente");
		map.put("servizi.bottone.hlpinventario",      	"sif_inventario");
		map.put("servizi.bottone.hlpsegnatura",       	"sif_segnatura");
		map.put("servizi.bottone.ripulisciSegnatura", 	"ripulisciSegnatura");
		map.put("servizi.bottone.cambiaContesto",     	"cambiacontesto");
		map.put("servizi.bottone.cambiaTipoServizio", 	"cambiatiposervizio");
		map.put("servizi.bottone.servizi_attivita",   	"servizi_attivita");
		map.put("servizi.bottone.indietro",           	"indietro");
		map.put("servizi.bottone.cambioBiblioteca",   	"cambioBiblioteca");

		map.put("servizi.bottone.si",                 	"si");
		map.put("servizi.bottone.no",                 	"no");

		map.put("servizi.bottone.respingi",            	"rifiuta");

		map.put("servizi.bottone.aggiorna", 			"aggiorna");

		map.put("servizi.bottone.esamina",            	"esamina");
		map.put("servizi.bottone.ordina",             	"ordina");
		map.put("servizi.bottone.dettaglioMovimento", 	"dettaglioMovimentoDiPrenotazione");

		map.put("servizi.bottone.selTutti",           	"selezionaTutti");
		map.put("servizi.bottone.deselTutti",         	"deselezionaTutti");

		map.put("servizi.bottone.solleciti",          	"solleciti");
		map.put("servizi.bottone.giacenze",           	"giacenze");
		map.put("servizi.bottone.proroghe",           	"proroghe");

		map.put("servizi.bottone.invioSolleciti",		"invioSolleciti");
		map.put("servizi.bottone.stampa", "stampa");

		map.put("button.blocco",                   		"blocco");

		//almaviva5_20150701
		map.put("servizi.bottone.prenotazioniMov", "prenotazioniMov");

		//almaviva5_20170605 evolutiva NAP
		map.put("documentofisico.lsSez", "sif_sezione");

		return map;
	}



	private void validateForm(HttpServletRequest request, ActionForm form, MovimentoRicercaVO ricerca) throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		request.setAttribute("currentForm", form);

		RicercaRichiesteType tipoRicerca = currentForm.getTipoRicerca();

		if (tipoRicerca.equals(RicercaRichiesteType.RICERCA_LISTE)) {
			if ( ValidazioneDati.strIsNull(ricerca.getCodTipoServ())
					&& ValidazioneDati.strIsNull(ricerca.getCodStatoRic())
					&& ValidazioneDati.strIsNull(ricerca.getCodStatoMov())
					&& ValidazioneDati.strIsNull(ricerca.getCodAttivita())
					&& ValidazioneDati.strIsNull(ricerca.getCod_erog())
			   ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazionericerca.nessunFiltro"));
				throw new ValidationException("Validazione dati fallita");
			}

			//almaviva5_20100309
			ricerca.validate();
		}

		else {

			//RICERCA_PER_UTENTE  RICERCA_PER_INVENTARIO  RICERCA_PER_SEGNATURA:

			if (ValidazioneDati.strIsNull(ricerca.getCodUte())
					&& ValidazioneDati.strIsNull(ricerca.getCodBibInv())
					&& ValidazioneDati.strIsNull(ricerca.getCodSerieInv())
					&& ValidazioneDati.strIsNull(ricerca.getCodInvenInv())
					&& ValidazioneDati.strIsNull(ricerca.getCodBibDocLett())
					&& ValidazioneDati.strIsNull(currentForm.getSegnaturaRicerca())
					//almaviva5_20120220 rfid
					&& ValidazioneDati.strIsNull(ricerca.getRfidChiaveInventario()) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.IndicareAlmenoUnCampo"));
				throw new ValidationException("Validazione dati fallita");
			}

			boolean keyInv = !ValidazioneDati.strIsNull(ricerca.getCodBibInv())
				|| ricerca.getCodSerieInv() == null
				|| !ValidazioneDati.strIsNull(ricerca.getCodInvenInv());

			boolean keyDoc = !ValidazioneDati.strIsNull(ricerca.getCodBibDocLett())
				|| !ValidazioneDati.strIsNull(currentForm.getSegnaturaRicerca());

			if (keyInv && keyDoc) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.IndicareInventarioOppureCollocazione"));
				throw new ValidationException("Validazione dati fallita");
			}

			if ((!ValidazioneDati.strIsNull(ricerca.getCodBibInv()) || !ValidazioneDati.strIsNull(ricerca.getCodSerieInv()))
					&& ValidazioneDati.strIsNull(ricerca.getCodInvenInv())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.IndicareCodiceInventario"));
				throw new ValidationException("Validazione dati fallita");
			}

			if ((!ValidazioneDati.strIsNull(ricerca.getCodSerieInv()) || !ValidazioneDati.strIsNull(ricerca.getCodInvenInv()))
					&& ValidazioneDati.strIsNull(ricerca.getCodBibInv())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezionareBibliotecaDiInventario"));
				throw new ValidationException("Validazione dati fallita");
			}

			if (!ValidazioneDati.strIsNull(ricerca.getCodBibDocLett())
					&& ValidazioneDati.strIsNull(currentForm.getSegnaturaRicerca())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezionareCollocazione"));
				throw new ValidationException("Validazione dati fallita");
			}

			if (!ValidazioneDati.strIsNull(ricerca.getCodInvenInv()) && !ValidazioneDati.strIsNumeric(ricerca.getCodInvenInv())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.InventarioDiBibliotecaNonNumerico"));
				throw new ValidationException("Validazione dati fallita");
			}

			if (!ricerca.isRichiesteInCorso() &&
				!ricerca.isRichiesteRespinte() &&
				!ricerca.isRichiesteEvase() ) {
				// invio un messaggio di errore se non presente almeno un check sulla lista movimenti
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ImpostareCheckMovimenti"));
				throw new ValidationException("Validazione dati fallita");
			}


			if (!ValidazioneDati.strIsNull(ricerca.getCodUte()))
				currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);

			if (ValidazioneDati.strIsNull(ricerca.getCodUte()) &&
				     (!ValidazioneDati.strIsNull(ricerca.getCodBibInv())
				   || !ValidazioneDati.strIsNull(ricerca.getCodSerieInv())
				   || !ValidazioneDati.strIsNull(ricerca.getCodInvenInv()))) {
				currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_INVENTARIO);

				if (ValidazioneDati.strIsNull(ricerca.getCodSerieInv()))
					ricerca.setCodSerieInv(NavigazioneServizi.FAKE_SERIE);

				if (ricerca.getCodSerieInv().equals(NavigazioneServizi.FAKE_SERIE))
					ricerca.setCodSerieInv("   ");
			}

			if (ValidazioneDati.strIsNull(ricerca.getCodUte()) &&
					!ValidazioneDati.strIsNull(currentForm.getSegnaturaRicerca()))
				currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_SEGNATURA);

		}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		request.getSession().setAttribute("parametroSubmit", mapping.getParameter());

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, currentForm);

		if (request.getAttribute(NavigazioneServizi.PRENOTAZIONE_RIFIUTATA) != null)
			return this.prenotazioni(mapping, form, request, response);

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_LISTA_MOVIMENTI)) {
				// se provengo dal tasto chiudi
				// della Lista Movimenti
				// ripulisco tutti i campi della form
				currentForm.getAnaMov().setCodUte("");
				currentForm.getAnaMov().setCodBibInv("");
				currentForm.getAnaMov().setCodSerieInv("");
				currentForm.getAnaMov().setCodInvenInv("");
				currentForm.getAnaMov().setCodBibDocLett("");
				((MovimentoRicercaVO) currentForm.getAnaMov()).setRichiesteInCorso(true);
				((MovimentoRicercaVO) currentForm.getAnaMov()).setRichiesteRespinte(false);
				((MovimentoRicercaVO) currentForm.getAnaMov()).setRichiesteEvase(false);
				currentForm.setSegnaturaRicerca("");
			}

			UserVO utenteCollegato = navi.getUtente();
			MovimentoVO filtroMov = currentForm.getAnaMov();
			MovimentoVO filtroPrenot = currentForm.getFiltroPrenot();

			if (!currentForm.isSessione()) {
				filtroMov.clearMovimento();
				filtroMov.setNuovoMov(false);
				filtroMov.setCodPolo(utenteCollegato.getCodPolo());
				filtroMov.setCodBibOperante(utenteCollegato.getCodBib());

				//almaviva5_20180504 #6584 errore visualizzazione prenotazioni di altra biblioteca
				filtroPrenot.clearMovimento();
				filtroPrenot.setNuovoMov(false);
				filtroPrenot.setCodPolo(utenteCollegato.getCodPolo());
				filtroPrenot.setCodBibOperante(utenteCollegato.getCodBib());

				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setRichiesta("");
				currentForm.setPathForm(mapping.getPath());

				currentForm.setBiblioteca(utenteCollegato.getCodBib());

				filtroMov.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
				currentForm.setProvengoDa("Menu");
				/////A
				currentForm.setFolder("S");
				currentForm.setEnable(true);
				loadTipiOrdinamento(form);
				filtroMov.setElemPerBlocchi(10);

				List<String> codiciTipiServizio = getCodiciTipiServizioConIter(form, request);
				currentForm.setLstCodiciTipiServizio(codiciTipiServizio);

				currentForm.getAnaMov().setFlSvolg(ErogazioneRicercaForm.TUTTI);
				this.loadTipiServizio(form, currentForm.getLstCodiciTipiServizio(), request);
				this.loadDefaultListe(request, form);

				setDefaultTipoServizio(currentForm, request);

				//almaviva5_20091218
				loadDefault(mapping, currentForm, request, response);
			}

			//almaviva5_20101217 #4074
			if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_DETTAGLIO_MOVIMENTO)
					&& NavigazioneServizi.isOggettoModificato(request) ) {
				switch (ErogazioneRicercaFolder.fromString(currentForm.getFolder())) {
				case PRENOTAZIONI:
					return prenotazioni(mapping, currentForm, request, response);
				case SOLLECITI:
					return solleciti(mapping, currentForm, request, response);
				case GIACENZE:
					return giacenze(mapping, currentForm, request, response);
				case PROROGHE:
					return proroghe(mapping, currentForm, request, response);
				default:
					break;
				}
			}

			BibliotecaVO altraBib = (BibliotecaVO) request.getAttribute(NavigazioneServizi.BIBLIOTECA_OPERANTE);
			if (altraBib != null) {
				//vengo dal cambio biblioteca
				currentForm.setBiblioteca(altraBib.getCod_bib() );
				filtroMov.setCodBibOperante(altraBib.getCod_bib() );
				//almaviva5_20180504 #6584 errore visualizzazione prenotazioni di altra biblioteca
				filtroPrenot.setCodBibOperante(altraBib.getCod_bib() );

				List<String>codiciTipiServizio = getCodiciTipiServizio(form, request);
				currentForm.setLstCodiciTipiServizio(codiciTipiServizio);

				currentForm.getAnaMov().setFlSvolg(ErogazioneRicercaForm.TUTTI);
				this.loadTipiServizio(form, currentForm.getLstCodiciTipiServizio(), request);
				this.loadDefaultListe(request, form);

				setDefaultTipoServizio(currentForm, request);
			}

			/////////// INIZIO caricamento combo Biblioteche del Sistema Metropolitano  			 ///////////
			/////////// Tale dato sarà utilizzato come primo campo dell'inventario e della segnatura ///////////
			/////////// Parte copiata e riadattata da unspecified di RicercaBibliotecarioAction.java ///////////

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String bibCorrente = currentForm.getBiblioteca();

			List<ComboVO> elencoBib = new ArrayList<ComboVO>();

			String poloCorrente = utenteCollegato.getCodPolo();
			String ticket = navi.getUserTicket();

			List<ComboVO> listaBibSistemaMetropolitano =
				factory.getSistema().getListaComboBibliotecheSistemaMetropolitano(
					ticket,
					poloCorrente,
					bibCorrente);

			ComboVO appoSpazio = new ComboVO();
			appoSpazio.setCodice("");
			appoSpazio.setDescrizione("");
			elencoBib.add(appoSpazio);
			elencoBib.addAll(listaBibSistemaMetropolitano);

			for (int i = 0; i < elencoBib.size(); i++) {
				ComboVO comboEl = elencoBib.get(i);
				comboEl.setDescrizione(comboEl.getCodice() + " " + comboEl.getDescrizione());
			}

			if (request.getAttribute("elencoBiblSelezionate") != null){
				String elencoBibSelString = (String)request.getAttribute("elencoBiblSelezionate");
				String elencoBibSelArray []  = elencoBibSelString.split(", ");
				if (elencoBibSelArray.length == 1 && elencoBibSelArray[0].equals("")) {
					currentForm.setElencoBib(elencoBib);
				} else {
					List<ComboVO> elencoBibAppo = new ArrayList<ComboVO>();
					elencoBibAppo.add(appoSpazio);

					for (int i = 0; i < elencoBib.size(); i++) {
						ComboVO comboEl = elencoBib.get(i);
						for (int j = 0; j < elencoBibSelArray.length; j++) {
							if (comboEl.getCodice().equals(elencoBibSelArray[j])) {
								elencoBibAppo.add(elencoBib.get(i));
								break;
							}
						}
					}
					currentForm.setElencoBib(elencoBibAppo);
				}

			} else {

				currentForm.setElencoBib(elencoBib);

			}


			/////////// FINE caricamento combo Codice Biblioteca relativo all'inventario ///////////

			//filtro sezione
			SezioneCollocazioneVO sez = (SezioneCollocazioneVO) request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA);
			if (sez != null) {
				FiltroCollocazioneVO filtroColl = ((MovimentoRicercaVO) currentForm.getAnaMov()).getFiltroColl();
				String codSezione = sez.getCodSezione();
				filtroColl.setCodSezione(codSezione);
				filtroColl.setSezioneSpazio(!ValidazioneDati.isFilled(codSezione));
			}


			String serv = request.getParameter("SERVIZI");
			if ( serv != null) {
				if (request.getAttribute("bid") == null) {
					return mapping.getInputForward();
				} else {
					currentForm.setProvengoDa("HlpUte");
					//currentForm.setTipoRicerca("");
					request.setAttribute("desc", request.getAttribute("titolo"));
					request.setAttribute("bid", request.getAttribute("bid"));

					currentForm.setInfoDocumentoVO(new InfoDocumentoVO());
					//currentForm.getInfoDocumentoVO().setBid((String)request.getAttribute("bid"));

					List<String> listaBiblio = new ArrayList<String>();

                    if (currentForm.getAnaMov().getCodBibInv().equals("")) {
            			List<ComboVO> appoBib = new ArrayList<ComboVO>();
            			appoBib = currentForm.getElencoBib();
            			for (int i = 1; i < appoBib.size(); i++) {
            				listaBiblio.add(appoBib.get(i).getCodice());
            			}
                    }
                    else {
                        listaBiblio.add(currentForm.getAnaMov().getCodBibInv());
                    }

                    request.setAttribute("listaBiblio", listaBiblio);


					currentForm.getInfoDocumentoVO().setTitolo((String)request.getAttribute("titolo"));

					return mapping.findForward("sifinventario");
				}
			}

			if (request.getAttribute("STAMPASERVIZI") != null){
				if (request.getAttribute("bid") == null) {
					return mapping.getInputForward();
				} else {
					currentForm.setProvengoDa("HlpUte");
					//currentForm.setTipoRicerca("");
					request.setAttribute("desc", request.getAttribute("titolo"));
					request.setAttribute("bid", request.getAttribute("bid"));

					currentForm.setInfoDocumentoVO(new InfoDocumentoVO());
					//currentForm.getInfoDocumentoVO().setBid((String)request.getAttribute("bid"));

					List<String> listaBiblio = new ArrayList<String>();

					if (currentForm.getAnaMov().getCodBibInv().equals("")) {
						List<ComboVO> appoBib = new ArrayList<ComboVO>();
						appoBib = currentForm.getElencoBib();
						for (int i = 1; i < appoBib.size(); i++) {
							listaBiblio.add(appoBib.get(i).getCodice());
						}
					}
					else {
						listaBiblio.add(currentForm.getAnaMov().getCodBibInv());
					}

					request.setAttribute("listaBiblio", listaBiblio);


					currentForm.getInfoDocumentoVO().setTitolo((String)request.getAttribute("titolo"));

					return mapping.findForward("sifinventario");
				}
			}

			String ute = request.getParameter("UTERICERCA");
			if ( ute != null) {
				if (request.getAttribute("IdUte") == null || request.getAttribute("IdUte").equals("false")) {
					return mapping.getInputForward();
				} else {
					currentForm.setProvengoDa("HlpUte");
					filtroMov.setCodBibUte((String) request.getAttribute(NavigazioneServizi.COD_BIB_UTENTE));
					filtroMov.setCodUte((String) request.getAttribute(NavigazioneServizi.COD_UTENTE));

					// salvo la provenienza della ricerca se non già precedentemente salvata
					if (currentForm.getTipoRicerca_old() == null) {
						currentForm.setTipoRicerca_old(currentForm.getTipoRicerca());
					}
					request.setAttribute("Provenienza_ricerca", currentForm.getTipoRicerca_old());
					currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
					//return mapping.getInputForward();
					return this.cerca(mapping, form, request, response);
				}
			}

			if (request.getParameter("INVRICERCA") != null && !request.getParameter("INVRICERCA").equals("")){
				String inventario = request.getParameter("INVRICERCA");
				if ( inventario != null) {
					InventarioTitoloVO inv = (InventarioTitoloVO)request.getAttribute("scelInv");
					if (inv == null) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceParametroErrato"));
						saveToken(request);
						return mapping.getInputForward();
					}
					else {
						filtroMov.setCodPolo(utenteCollegato.getCodPolo());
						filtroMov.setCodBibInv(inv.getCodBib());
						filtroMov.setCodSerieInv(inv.getCodSerie());
						filtroMov.setCodInvenInv(String.valueOf(inv.getCodInvent()));
						currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_INVENTARIO);
						currentForm.setProvengoDa("HlpInv");

						currentForm.setInfoDocumentoVO(new InfoDocumentoVO());
						currentForm.getInfoDocumentoVO().setInventarioTitoloVO(inv);

						//almaviva5_20120220 rfid
						MovimentoRicercaVO ricerca = (MovimentoRicercaVO) currentForm.getAnaMov();
						ricerca.setRfidChiaveInventario(null);

						return this.cerca(mapping, form, request, response);
					}
				}
			}


			SIFListaDocumentiNonSbnVO segn = (SIFListaDocumentiNonSbnVO) request.getAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN);
			if ( segn != null) {
				DocumentoNonSbnVO documento = segn.getDocumento();

				filtroMov.setCodBibDocLett(documento.getCodBib() );
				filtroMov.setTipoDocLett(String.valueOf(documento.getTipo_doc_lett()) );
				filtroMov.setCodDocLet(String.valueOf(documento.getCod_doc_lett()) );
				//almaviva5_20130319 #5286
				filtroMov.setProgrEsempDocLet(ServiziUtil.selezionaEsemplareAttivo(documento).getPrg_esemplare() + "");

				currentForm.setSegnaturaRicerca(documento.getSegnatura() );
				currentForm.setProvengoDa("HlpSegn");
				currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_SEGNATURA);

				return this.cerca(mapping, form, request, response);
			}

			//parametro passato se attivo JavaScript
			String daChiamare=request.getParameter("daChiamare");
			if (daChiamare != null && !daChiamare.equalsIgnoreCase("")) {
				if (daChiamare.equalsIgnoreCase("CAMBIA_CONTESTO"))
					this.cambiaContesto(mapping, form, request, response);
				else if (daChiamare.equalsIgnoreCase("CAMBIA_TIPO_SERVIZIO"))
					this.cambiaTipoServizio(mapping, form, request, response);
			}


			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	private ActionForward loadDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		//carico default per folder Richieste/Movimenti
		MovimentoVO anaMov = currentForm.getAnaMov();
		anaMov.setCodStatoMov((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_STATO_MOV));
		anaMov.setCodStatoRic((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_STATO_RICH));

		String elementiBlocco = (String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_ELEM_BLOCCHI);
		anaMov.setElemPerBlocchi(Integer.parseInt(elementiBlocco));

		// il default su contesto causa il ricaricamento della combo tipo servizio
		String svolg = (String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_SVOLGIMENTO);
		currentForm.getAnaMov().setFlSvolg(svolg);
		if (ValidazioneDati.isFilled(svolg))
			this.cambiaContesto(mapping, currentForm, request, response);

		// il default su tipo servizio causa il ricaricamento delle combo attivita e mod. erogazione
		String codTipoServ = (String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_TIPO_SERV);
		anaMov.setCodTipoServ(codTipoServ);
		if (ValidazioneDati.isFilled(codTipoServ))
			this.cambiaTipoServizio(mapping, form, request, response);

		anaMov.setCodAttivita((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_ATTIVITA));
		anaMov.setCod_erog((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_MOD_EROG));

		caricaOrdinamentiMovimenti(currentForm, request);
		anaMov.setTipoOrdinamento((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_ORD_LISTA));

		caricaOrdinamentiPrenotazioni(currentForm, request);
		currentForm.setOrdPrenotazioni((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_ORD_LISTA_PRENOTAZIONI));

		caricaOrdinamentiProroghe(currentForm, request);
		currentForm.setOrdProroghe((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_ORD_LISTA_PROROGHE));

		caricaOrdinamentiGiacenze(currentForm, request);
		currentForm.setOrdGiacenze((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_ORD_LISTA_GIACENZE));

		caricaOrdinamentiSolleciti(currentForm, request);
		currentForm.setOrdSolleciti((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_ORD_LISTA_SOLLECITI));

		//almaviva5_20120220 rfid
		currentForm.setRfidEnabled(Boolean.valueOf(CommonConfiguration.getProperty(Configuration.RFID_ENABLE, "false")));

		//almaviva5_20170609 filtro sezione
		String codSez = (String) utenteEjb.getDefault(ConstantDefault.SER_RIC_MOV_SEZIONE_COLL);
		if (ValidazioneDati.length(codSez) > 0) {
			FiltroCollocazioneVO filtroColl = ((MovimentoRicercaVO) currentForm.getAnaMov()).getFiltroColl();
			filtroColl.setCodSezione(codSez);
			filtroColl.setSezioneSpazio(!ValidazioneDati.isFilled(codSez));
		}

		//folder di default
		String folder = (String) utenteEjb.getDefault(ConstantDefault.SER_FOLDER_EROGAZIONE);
		if (folder.equals(ErogazioneRicercaForm.FOLDER_UTENTE_DOC))
			return ricerca(mapping, currentForm, request, response);
		if (folder.equals(ErogazioneRicercaForm.FOLDER_LISTE))
			return liste(mapping, currentForm, request, response);
		if (folder.equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI))
			return prenotazioni(mapping, currentForm, request, response);
		if (folder.equals(ErogazioneRicercaForm.FOLDER_PROROGHE))
			return proroghe(mapping, currentForm, request, response);
		if (folder.equals(ErogazioneRicercaForm.FOLDER_GIACENZE))
			return giacenze(mapping, currentForm, request, response);
		if (folder.equals(ErogazioneRicercaForm.FOLDER_SOLLECITI))
			return solleciti(mapping, currentForm, request, response);
		return null;
	}



	public ActionForward liste(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setRichiesta("");
				currentForm.setPathForm(mapping.getPath());
				currentForm.setProvengoDa(null);
				currentForm.setEnable(true);
			}
			currentForm.setFolder(ErogazioneRicercaForm.FOLDER_LISTE);
			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_LISTE);

			navi.setDescrizioneX("Erogazione Richieste/movimenti");
			navi.setTesto("Erogazione Richieste/movimenti");

			currentForm.getAnaMov().setAttivitaAttuale(true);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward solleciti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		try {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			MovimentoRicercaVO ricerca = currentForm.getAnaMov().copy();
			ricerca.setDataFinePrev(DaoManager.now());

			ricerca.setTipoOrdinamento(currentForm.getOrdSolleciti());

			CommandResultVO result = delegate.invoke(CommandType.LISTA_RICHIESTE_SCADUTE, ricerca);
			result.throwError();

			DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) result.getResult();
			Navigation navi = Navigation.getInstance(request);
			if (DescrittoreBloccoVO.isFilled(blocco1) ) {
				// abilito i tasti per il blocco se necessario
				currentForm.setAbilitaBlocchi   ((blocco1.getTotBlocchi() > 1));
				// memorizzo le informazioni per la gestione blocchi
				currentForm.setIdLista          (blocco1.getIdLista());
				currentForm.setTotRighe         (blocco1.getTotRighe());
				currentForm.setTotBlocchi       (blocco1.getTotBlocchi());
				currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
				currentForm.setMaxRighe         (blocco1.getMaxRighe());
				currentForm.setLivelloRicerca   (navi.getUtente().getCodBib());
				List<MovimentoListaVO> listaSolleciti = new ArrayList<MovimentoListaVO>();
				listaSolleciti.addAll(blocco1.getLista());
				currentForm.setListaSolleciti(listaSolleciti);

				if (blocco1.getTotRighe() == 1)
					currentForm.setCodSolSing(listaSolleciti.get(0).getCodRichServ() );

			} else {
				currentForm.setListaSolleciti(new ArrayList<MovimentoListaVO>());
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota.solleciti"));
			}

			navi.getCache().getCurrentElement().setInfoBlocchi(null);
			currentForm.setFolder(ErogazioneRicercaForm.FOLDER_SOLLECITI);

			navi.setDescrizioneX("Erogazione Solleciti");
			navi.setTesto("Erogazione Solleciti");

			return mapping.getInputForward();


		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward giacenze(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setRichiesta("");
				currentForm.setPathForm(mapping.getPath());
				currentForm.setProvengoDa(null);
				currentForm.setEnable(true);
			}

			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_GIACENZE);

			if (!caricaGiacenze(request, currentForm) );
				//return mapping.getInputForward();

			navi.getCache().getCurrentElement().setInfoBlocchi(null);
			currentForm.setFolder(ErogazioneRicercaForm.FOLDER_GIACENZE);

			navi.setDescrizioneX("Erogazione Giacenze");
			navi.setTesto("Erogazione Giacenze");

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward proroghe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setRichiesta("");
				currentForm.setPathForm(mapping.getPath());
				currentForm.setProvengoDa(null);
				currentForm.setEnable(true);
			}

			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_PROROGHE);
			if (!caricaProroghe(request, currentForm) );
				//return mapping.getInputForward();

			navi.getCache().getCurrentElement().setInfoBlocchi(null);
			currentForm.setFolder(ErogazioneRicercaForm.FOLDER_PROROGHE);

			navi.setDescrizioneX("Erogazione Proroghe");
			navi.setTesto("Erogazione Proroghe");

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	public ActionForward invioSolleciti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try{
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (currentForm.getCodSolMul()!=null && currentForm.getCodSolMul().length>0) {
				currentForm.setConferma(true);
				currentForm.setRichiesta("Invio Solleciti");

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerCancRif"));

				return mapping.getInputForward();
			}
		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		String check = null;
		MovimentoListaVO movimento = null;

		String folder = currentForm.getFolder();
		if (folder != null && folder.equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI)){
			request.setAttribute("checkS", currentForm.getCodProSing());
			check = currentForm.getCodPreSing();
			if (ValidazioneDati.isFilled(check) ) {
				movimento =  this.passaSelezionato(currentForm.getListaPrenotazioni(), currentForm.getCodPreSing());
			}
		}else if (folder != null && folder.equals(ErogazioneRicercaForm.FOLDER_PROROGHE)){
			request.setAttribute("checkS", currentForm.getCodProSing());
			check = currentForm.getCodProSing();
			if (ValidazioneDati.isFilled(check) ) {
				movimento =  this.passaSelezionato(currentForm.getListaProroghe(), currentForm.getCodProSing());
			}
		}else if (folder != null && folder.equals(ErogazioneRicercaForm.FOLDER_GIACENZE)){
			request.setAttribute("checkS", currentForm.getCodGiaSing());
			check = currentForm.getCodGiaSing();
			if (ValidazioneDati.isFilled(check) ) {
				movimento =  this.passaSelezionato(currentForm.getListaGiacenze(), currentForm.getCodGiaSing());
			}
		}else if (folder != null && folder.equals(ErogazioneRicercaForm.FOLDER_SOLLECITI)){
			request.setAttribute("checkS", currentForm.getCodSolSing());
			check = currentForm.getCodSolSing();
			if (ValidazioneDati.isFilled(check) ) {
				movimento =  this.passaSelezionato(currentForm.getListaSolleciti(), currentForm.getCodSolSing());
			}
		}

		if (movimento == null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaUtenti.nessunaSelezionePerStampa"));

			return mapping.getInputForward();
		}

		// acquisisco il numero copie e il testo della stampa modulo
		// dalla configurazione dell'attività

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		List<IterServizioVO> listaIterServizio = this.getIterServizio(movimento.getCodPolo(),
				movimento.getCodBibOperante(), movimento.getCodTipoServ(), request);

		Iterator<IterServizioVO> iterator = listaIterServizio.iterator();
		IterServizioVO iterServizioVO = null;

		while (iterator.hasNext()) {
			iterServizioVO = iterator.next();
			if (iterServizioVO.getCodAttivita().equals(movimento.getCodAttivita()))
				break;
		}

		ControlloAttivitaServizio attivita = delegate.getAttivita(movimento.getCodPolo(), iterServizioVO);

		// Imposto il numero copie della stampa modulo richiesta
		if (String.valueOf(attivita.getPassoIter().getNumPag()).equals("0")) {
			movimento.setNumeroCopieStampaModulo("1");
		} else {
			movimento.setNumeroCopieStampaModulo(String.valueOf(attivita.getPassoIter().getNumPag()));
		}
		// Imposto il testo della stampa modulo richiesta
		movimento.setTestoStampaModulo(attivita.getPassoIter().getTesto());

		request.setAttribute("codBib", currentForm.getBiblioteca());
		request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_RICHIESTA);
		request.setAttribute("DATI_STAMPE_ON_LINE", movimento);
		return  mapping.findForward("stampaRichiesta");
	}


	public ActionForward prenotazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setRichiesta("");
				currentForm.setPathForm(mapping.getPath());
				currentForm.setProvengoDa(null);
				currentForm.setEnable(true);
			}

			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_PRENOTAZIONI);
			if (!caricaPrenotazioni(request, currentForm) )  {
				//return mapping.getInputForward();
			}

			navi.getCache().getCurrentElement().setInfoBlocchi(null);
			currentForm.setFolder(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI);

			navi.setDescrizioneX("Erogazione Prenotazioni");
			navi.setTesto("Erogazione Prenotazioni");

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward cambiaTipoServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			loadFiltriServiziAttivita(form, request);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward cambiaContesto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (ValidazioneDati.equals(currentForm.getAnaMov().getFlSvolg(), ErogazioneRicercaForm.LOCALE) )
				this.loadTipiServizio(form, currentForm.getLstCodiciTipiServizio(), new String[] {"T", "L"} );
			else
				if (ValidazioneDati.equals(currentForm.getAnaMov().getFlSvolg(), ErogazioneRicercaForm.ILL) )
					this.loadTipiServizio(form, currentForm.getLstCodiciTipiServizio(), new String[] {"T", "I"} );
				else
					this.loadTipiServizio(form, currentForm.getLstCodiciTipiServizio(), request);

			setDefaultTipoServizio(form, request);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward ricerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setRichiesta("");
				currentForm.setPathForm(mapping.getPath());
				currentForm.setProvengoDa(null);
				currentForm.setEnable(true);
			}

			currentForm.setFolder(ErogazioneRicercaForm.FOLDER_UTENTE_DOC);
			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);

			navi.setDescrizioneX("Erogazione Utente/documento");
			navi.setTesto("Erogazione Utente/documento");

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			request.setAttribute(NavigazioneServizi.PROVENIENZA, "");
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			MovimentoRicercaVO ricerca = currentForm.getAnaMov().copy();

			checkInventarioRfid(request, currentForm, ricerca);

			this.validateForm(request, form, ricerca);

			currentForm.setRichiesta(null);
			currentForm.setConferma(false);

			ricerca.setNuovoMov(false);

			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, ricerca);
			RicercaRichiesteType tipoRicerca = currentForm.getTipoRicerca();

			request.setAttribute("TipoRicerca", tipoRicerca);

			InfoDocumentoVO infoDocumento = new InfoDocumentoVO();

			//almaviva5_20100301 ricerca per segnatura puntuale
			//da attivare solo se segnatura é valorizzato e
			//non sto tornando dal sif di selezione documento
			String segnRicerca = currentForm.getSegnaturaRicerca();
			boolean ritornoDaSif = (request.getAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN) != null);
			if (ValidazioneDati.isFilled(segnRicerca) && !ritornoDaSif) {
				ActionForward forward = preparaRicercaPuntualeSegnatura(request, mapping, ricerca, infoDocumento, segnRicerca, null);
				if (forward != null)
					return forward;
			}

			request.setAttribute("SegnaturaRicerca", currentForm.getSegnaturaRicerca());

			switch (tipoRicerca) {

			case RICERCA_PER_UTENTE:
			case RICERCA_PER_INVENTARIO:
			case RICERCA_PER_SEGNATURA:

				String codUte = ricerca.getCodUte();
				if (ValidazioneDati.isFilled(codUte) ) {
					// se è stato impostato il codice utente
					String codUtente = ServiziUtil.espandiCodUtente(codUte);
					ricerca.setCodUte(codUtente);
					UtenteBaseVO utente = this.getUtente(codUtente, request);
					if (utente == null) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));
						return mapping.getInputForward();
					} else {
						codUtente = utente.getCodUtente();
						ricerca.setCodUte(codUtente);
						UtenteBaseVO utenteBib = this.getUtente(ricerca.getCodPolo(), utente.getCodBib(), codUtente, ricerca.getCodBibOperante(), request);
						if (utenteBib == null) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteBibNonIscritto", ricerca.getCodBibOperante()));
							return mapping.getInputForward();
						}
						ricerca.setCodBibUte(utente.getCodBib());
						request.setAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE, utenteBib);
					}
				}

				if (ricerca != null) {
					if (ricerca.isRichiestaSuInventario()) {
						infoDocumento = this.getInfoInventario(
								ricerca.getCodPolo(), ricerca.getCodBibInv(),
								ricerca.getCodSerieInv(),
								new Integer(ricerca.getCodInvenInv()),
								navi.getUserTicket(), this.getLocale(request, Constants.SBN_LOCALE),
								request);

						if (infoDocumento == null) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));
							return mapping.getInputForward();
						}

						String codNoDisp = infoDocumento.getInventarioTitoloVO().getCodNoDisp();
						String dataRedisp = infoDocumento.getInventarioTitoloVO().getDataRedisp();
						if (infoDocumento != null && ValidazioneDati.isFilled(codNoDisp)) {
							// se il documento non è disponibile
							String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
							LinkableTagUtils.addError(request, new ActionMessage(ValidazioneDati.isFilled(dataRedisp) ?
									ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL.getMessage() :
									ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE.getMessage(), descr, dataRedisp));
							return mapping.getInputForward();
						}

						currentForm.setInfoDocumentoVO(infoDocumento);

						request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, currentForm.getInfoDocumentoVO().copy() );
						request.setAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA, currentForm.getInfoDocumentoVO().getSegnatura());

					} else if (ricerca.isRichiestaSuSegnatura()) {
						infoDocumento = this.getInfoSegnatura(request,
								ricerca.getCodPolo(), ricerca.getCodBibDocLett(),
								ricerca.getTipoDocLett(),
								new Integer(ricerca.getCodDocLet() ),
								new Integer(ricerca.getProgrEsempDocLet()) );

						if (infoDocumento == null) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));
							return mapping.getInputForward();
						}

						String codNoDisp = infoDocumento.getDocumentoNonSbnVO().getCodNoDisp();
						if (infoDocumento != null && ValidazioneDati.isFilled(codNoDisp)) {
							// se il documento non è disponibile
							String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
							LinkableTagUtils.addError(request, new ActionMessage(ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE.getMessage(), descr));
							return mapping.getInputForward();
						}

						currentForm.setInfoDocumentoVO(infoDocumento);

						request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, currentForm.getInfoDocumentoVO().copy() );
						request.setAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA, currentForm.getInfoDocumentoVO().getSegnatura());
					}
				}
				break;

			case RICERCA_LISTE:
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				//almaviva5_20130301 #5268 fix range date non acquisito
				DescrittoreBloccoVO blocco1 = delegate.caricaListeTematiche(ricerca,
						ricerca.isAttivitaAttuale(), ricerca.getElemPerBlocchi() );
				if (blocco1 == null)
					return mapping.getInputForward();

				request.setAttribute(ServiziDelegate.LISTA_TEMATICHE, blocco1);
				break;

			default:
				return mapping.getInputForward();
			}

			request.setAttribute("elencoBiblioteche", currentForm.getElencoBib());

			if (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
				currentForm.setAnaMov(ricerca);
				return mapping.getInputForward();
			} else {
				return mapping.findForward("ok");
			}

		} catch (ApplicationException e) {
			resetToken(request);
			log.info(e.getMessage());
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC) {
				LinkableTagUtils.addError(request, e);

			} else
				this.setErroreGenerico(request, e);

			return mapping.getInputForward();

		} catch (ValidationException e) {
			resetToken(request);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC) {
				LinkableTagUtils.addError(request, e);

			} else if (ValidazioneDati.equals(e.getMessage(), "invNonEsistente")) {
				log.info(e.getMessage());
				if ((Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm"))) {
					throw new ValidationException("invNonEsistente");
				}

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

			} else if (ValidazioneDati.equals(e.getMessage(), "invCancellato")) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.inventarioCancellato"));

			} else {
				if (request.getAttribute("STAMPASERVIZI") != null) {
					throw new Exception(e.getMessage());
				} else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazionericerca.nessunFiltro"));
				}
			}
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}



	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		return mapping.getInputForward();
	}

	private void loadTipiOrdinamento(ActionForm form) throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		CaricamentoCombo carCombo = new CaricamentoCombo();
		List<TB_CODICI> listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_MOVIMENTI);
		currentForm.getAnaMov().setLstTipiOrdinamento(carCombo.loadComboCodiciDesc(listaCodice));
	}

	private void loadDefaultListe(HttpServletRequest request, ActionForm form) throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		CaricamentoCombo carCombo = new CaricamentoCombo();

		//Stati richiesta
		List<TB_CODICI> listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_STATO_RICHIESTA, "A");
		currentForm.setLstStatiRichiesta(carCombo.loadComboCodiciDesc(listaCodice));

		//almaviva5_20091215 Stati movimento
		listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_STATO_MOVIMENTO, "C", "P", "E");
		currentForm.setLstStatiMovimento(carCombo.loadComboCodiciDesc(listaCodice));

		listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SVOLGIMENTO_DEL_SERVIZIO);
		currentForm.setSvolgimentiSelezionati(carCombo.loadComboCodiciDesc(listaCodice));

		//almaviva5_20170511 gestione sale
		UserVO utente = Navigation.getInstance(request).getUtente();
		List<TipoServizioVO> listaTipiServizio = getListaTipiServizio(utente.getCodPolo(), utente.getCodBib(), request);
		listaTipiServizio.add(0, new TipoServizioVO());
		currentForm.setListaTipiServizio(listaTipiServizio);
	}


	private void setDefaultTipoServizio(ActionForm form, HttpServletRequest request) throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		String codice=((ComboCodDescVO)currentForm.getLstTipiServizio().get(0)).getCodice();
		if ( codice!=null && !codice.equalsIgnoreCase("")) {
			currentForm.getAnaMov().setCodTipoServ(codice);
		}
		else {
			currentForm.getAnaMov().setCodTipoServ("");
		}

		loadFiltriServiziAttivita(currentForm, request);
	}

	/**
	  * Carica la combo di tipo servizio con le informazioni
	  * relative ai codici tipo servizio contenuti in codici
	  * loadTipiServizio
	  * void
	  * @param form
	  * @param svolg
	  * @throws Exception
	  *
	  *
	 */
	private void loadTipiServizio(ActionForm form, List<String> serviziAttivi, String[] svolg) throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		CaricamentoCombo carCombo = new CaricamentoCombo();

		Set<String> tipoSvolg = new HashSet<String>(Arrays.asList(svolg));
		List<TB_CODICI> listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SERVIZIO);
		List<TB_CODICI> tmp = new ArrayList<TB_CODICI>();
		tmp.add(new TB_CODICI("", "")); // riga vuota
		for (TB_CODICI cod : listaCodice) {
			String codServ = ValidazioneDati.trimOrEmpty(cod.getCd_tabella());	// serv. attivo per la bib
			String flagSvolg = ValidazioneDati.trimOrEmpty(cod.getCd_flg4());	// il flsvolg é nel flag 4
			//almaviva5_20100422 #3556 solo servizi con iter
			boolean richiedeIter = ValidazioneDati.equals(cod.getCd_flg7(), "S");	// il flag richiede iter é nel flag 7

			if (tipoSvolg.contains(flagSvolg) && serviziAttivi.contains(codServ) && richiedeIter)
				tmp.add(cod);
		}
		currentForm.setLstTipiServizio(carCombo.loadComboCodiciDesc(tmp));
	}

	/**
	  * Carica la combo di tipo servizio con le informazioni
	  * relative ai codici tipo servizio contenuti in codici
	  * it.iccu.sbn.web.actions.servizi.erogazione
	  * ErogazioneRicercaAction.java
	  * loadTipiServizio
	  * void
	  * @param form
	 * @param request
	  * @throws Exception
	  *
	  *
	 */
	private void loadTipiServizio(ActionForm form, List<String> codici, HttpServletRequest request) throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		currentForm.setLstTipiServizio(super.loadTipiServizio(codici, request));
	}

	private List<String> getCodiciTipiServizio(ActionForm form, HttpServletRequest request) throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		return super.getCodiciTipiServizio(currentForm.getAnaMov().getCodPolo(), currentForm.getAnaMov().getCodBibOperante(), request);
	}

	private List<String> getCodiciTipiServizioConIter(ActionForm form, HttpServletRequest request) throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		List<String> tipiServizio = super.getCodiciTipiServizio(currentForm
				.getAnaMov().getCodPolo(), currentForm.getAnaMov()
				.getCodBibOperante(), request);

		Iterator<String> i = tipiServizio.iterator();
		while (i.hasNext()) {
			TB_CODICI codice = CodiciProvider.cercaCodice(i.next(),
					CodiciType.CODICE_TIPO_SERVIZIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			if (codice == null)
				continue;

			// se il flag relativo a richiede_iter sulla tabella codici non è uguale
			// a "S" devo togliere il servizio dalla lista finale.
			if (!ValidazioneDati.equals(codice.getCd_flg7(), "S"))
				i.remove();
		}

		return tipiServizio;
	}

	public ActionForward sif_inventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		request.setAttribute("currentForm", form);
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		//almaviva5_20101103 #3958
		Navigation navi = Navigation.getInstance(request);
		String codPolo = navi.getUtente().getCodPolo();
		String codBib = currentForm.getAnaMov().getCodBibInv();
		if (!ValidazioneDati.isFilled(codBib))
			codBib = currentForm.getBiblioteca();

		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(codPolo);
		bib.setCod_bib(codBib);
		request.setAttribute(BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO, ValidazioneDati.asSingletonList(bib));

		currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_INVENTARIO);
		return navi.goForward(mapping.findForward("sifbid"));
	}

	public ActionForward sif_utente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
		request.setAttribute(BIBLIOTECA_ATTR, currentForm.getBiblioteca());
		request.setAttribute(PATH_CHIAMANTE_ATTR, "ErogazioneRicerca");

		RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
		String codUte = currentForm.getAnaMov().getCodUte();
		if (ValidazioneDati.length(codUte) == 16)
			ricerca.setCodFiscale(codUte.toUpperCase());
		else
			ricerca.setCodUte(codUte);
		request.setAttribute(NavigazioneServizi.RICERCA_UTENTE, ricerca);

		return mapping.findForward("sifutente");
	}

	public ActionForward ripulisciSegnatura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		request.setAttribute("currentForm", form);
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		currentForm.setSegnaturaRicerca(null);
		request.setAttribute("SegnaturaRicerca",   currentForm.getSegnaturaRicerca());

		MovimentoVO filtroMov = currentForm.getAnaMov();
		filtroMov.setCodBibDocLett("");
		filtroMov.setCodDocLet("");

		((MovimentoRicercaVO) filtroMov).setFiltroColl(new FiltroCollocazioneVO());

		return mapping.getInputForward();

	}

	public ActionForward sif_segnatura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		request.setAttribute("currentForm", form);
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_SEGNATURA);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		MovimentoVO filtroMov = currentForm.getAnaMov();
		SIFListaDocumentiNonSbnVO richiesta = new SIFListaDocumentiNonSbnVO();
		richiesta.setCodPolo(filtroMov.getCodPolo() );
		richiesta.setCodBib(filtroMov.getCodBibOperante() );
		richiesta.setSegnatura(currentForm.getSegnaturaRicerca());
		richiesta.setRequestAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN);

        List<String> listaBiblio = new ArrayList<String>();

        if (filtroMov.getCodBibDocLett().equals("")) {
			List<ComboVO> appoBib = new ArrayList<ComboVO>();
			appoBib = currentForm.getElencoBib();
			for (int i = 1; i < appoBib.size(); i++) {
				listaBiblio.add(appoBib.get(i).getCodice());
			}
        }
        else {
            listaBiblio.add(filtroMov.getCodBibDocLett());
        }

        richiesta.setListaBib(listaBiblio);

		return delegate.getSIFListaDocumentiNonSbn(richiesta);
	}

	public ActionForward sif_sezione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		MovimentoRicercaVO ricerca = (MovimentoRicercaVO) currentForm.getAnaMov();
		FiltroCollocazioneVO filtroColl = ricerca.getFiltroColl();
		filtroColl.setCodPolo(Navigation.getInstance(request).getPolo());
		filtroColl.setCodBib(ricerca.getCodBibOperante());
		return SezioneDelegate.getInstance(request).getSIFListaSezioni(filtroColl, null);
	}

	private void loadFiltriServiziAttivita(ActionForm form, HttpServletRequest request) throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		List<TariffeModalitaErogazioneVO> tariffeErogazioneVO = null;
		boolean ult_sup = false;

		MovimentoVO mov = currentForm.getAnaMov();
		//almaviva5_20091215 check ult_sup su tipoServ
		String codTipoServ = mov.getCodTipoServ();
		if (ValidazioneDati.isFilled(codTipoServ)) {
			TB_CODICI cod = CodiciProvider.cercaCodice(codTipoServ, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN);
			ult_sup = ValidazioneDati.trimOrEmpty(cod.getCd_flg6()).equals("S");   //richiede supporto
		}

		//Se il servizio ammette supporti ed é impostato carico le mod. erogazione
		//dai supporti definiti in biblioteca
		if (ValidazioneDati.isFilled(codTipoServ) && ult_sup) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			tariffeErogazioneVO = delegate.getListaModErogLegateASupporti(mov);
		}

		// Se il tipo serv. non é impostato oppure non ammette supporti filtro
		// le mod. erogazione a partire dal cod. tipo servizio
		if (!ValidazioneDati.isFilled(codTipoServ) || !ult_sup) {
			//Caricamento tariffe Modalità Erogazione
			tariffeErogazioneVO = this.getTariffeModalitaErogazione(mov.getCodPolo(), mov.getCodBibOperante(), codTipoServ, request);

			//Modifica descrizioni modalità di erogazione caricate
			Iterator<TariffeModalitaErogazioneVO> iterator = tariffeErogazioneVO.iterator();
			TariffeModalitaErogazioneVO tariffaModalitaErog = null;

			while (iterator.hasNext()) {
				tariffaModalitaErog = iterator.next();
				//tariffaModalitaErog.setDesModErog(tariffaModalitaErog.getDesModErog()+" - "+tariffaModalitaErog.getTarBase().toString());
				tariffaModalitaErog.setDesModErog(tariffaModalitaErog.getDesModErog());
			}
		}

		if (ValidazioneDati.isFilled(tariffeErogazioneVO) ) {
			//Inserisco in testa elemento vuoto
			TariffeModalitaErogazioneVO tariffaModalitaErog = new TariffeModalitaErogazioneVO();
			tariffaModalitaErog.setCodErog("");
			tariffaModalitaErog.setDesModErog("");
			tariffeErogazioneVO.add(0, tariffaModalitaErog);
		}

		currentForm.setTariffeErogazioneVO(tariffeErogazioneVO);

		//Caricamento attività
		List<IterServizioVO> listaAttivita = this.getIterServizio(mov.getCodPolo(),	mov.getCodBibOperante(), codTipoServ, request);
		if (ValidazioneDati.isFilled(listaAttivita) ) {
			//Inserisco in testa elemento vuoto
			IterServizioVO iterServizioVO = new IterServizioVO();
			iterServizioVO.setCodAttivita("");
			iterServizioVO.setDescrizione("");
			listaAttivita.add(0, iterServizioVO);
		}
		currentForm.setIterServizioVO(listaAttivita);
	}


	public ActionForward servizi_attivita(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response) {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			currentForm.setTipoRicerca(RicercaRichiesteType.SERVIZI_ATTIVITA_SCELTA);
			this.validateForm(request, form, (MovimentoRicercaVO) currentForm.getAnaMov());
			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_SERVIZI_ATTIVITA);

			currentForm.setFolder("L2");
			MovimentoVO mov = currentForm.getAnaMov();
			mov.setCod_erog("");
			mov.setCodAttivitaSucc("");

			request.getSession().setAttribute(
					"descrTipoServizio",
					CodiciProvider.cercaDescrizioneCodice(
							mov.getCodTipoServ(), CodiciType.CODICE_TIPO_SERVIZIO,
							CodiciRicercaType.RICERCA_CODICE_SBN));

			loadFiltriServiziAttivita(form, request);
		} catch (ValidationException e) {
			resetToken(request);
			log.info(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setFolder("L");
		currentForm.setTipoRicerca(null);
		request.setAttribute("descrTipoServizio", "");

		return mapping.getInputForward();
	}


	public ActionForward cambioBiblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utente = Navigation.getInstance(request).getUtente();
		BibliotecaDelegate dao = new BibliotecaDelegate(factory, request);

		SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
			new SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(), utente.getCodBib(),
				CodiciAttivita.getIstance().SERVIZI, 10, NavigazioneServizi.BIBLIOTECA_OPERANTE);
		return dao.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);
	}


	public ActionForward selezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			String folder = currentForm.getFolder();

			if (folder.equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI)) {
				Long[] elemSel = new Long[currentForm.getListaPrenotazioni().size()];

				if (currentForm.getListaPrenotazioni().size() > 0) {
					int i = 0;
					for (MovimentoListaVO movimento : currentForm.getListaPrenotazioni()) {
						elemSel[i] = new Long(movimento.getCodRichServ());
						i++;
					}
					currentForm.setCodPreMul(elemSel);
				}
			}

			if (folder.equals(ErogazioneRicercaForm.FOLDER_SOLLECITI)) {
				List<MovimentoListaVO> listaSolleciti = currentForm.getListaSolleciti();
				if (!ValidazioneDati.isFilled(listaSolleciti))
					return mapping.getInputForward();

				Long[] elemSel = new Long[listaSolleciti.size()];
				int i = 0;
				for (MovimentoListaVO sol : listaSolleciti )
					elemSel[i++] = sol.getIdRichiesta();

				currentForm.setCodSolMul(elemSel);
			}

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward deselezionaTutti(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			String folder = currentForm.getFolder();
			if (folder.equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI))
				currentForm.setCodPreMul(null);
			if (folder.equals(ErogazioneRicercaForm.FOLDER_SOLLECITI))
				currentForm.setCodSolMul(null);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}



	public ActionForward rifiuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try{
			if (!isTokenValid(request))
				saveToken(request);

			if (ValidazioneDati.isFilled(currentForm.getCodPreMul())) {
				currentForm.setConferma(true);
				currentForm.setRichiesta("Rifiuta");

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerCancRif"));

				return mapping.getInputForward();
			}
		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		if (request.getAttribute(NavigazioneServizi.PRENOTAZIONE_RIFIUTATA)!=null)
			return this.unspecified(mapping, form, request, response);

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try{
			if (!isTokenValid(request))
				saveToken(request);

			MovimentoListaVO movimentoSelezionato = null;
			MovimentoListaVO movimentoDB = null;

			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI)) {
				if (ValidazioneDati.isFilled(currentForm.getCodPreSing()) ) {
					movimentoSelezionato = this.passaSelezionato(currentForm.getListaPrenotazioni(), currentForm.getCodPreSing());
					request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazionePrenotazioni");
				}
			}
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_PROROGHE)) {
				if (ValidazioneDati.isFilled(currentForm.getCodProSing()) ) {
					movimentoSelezionato = this.passaSelezionato(currentForm.getListaProroghe(), currentForm.getCodProSing());
					request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazioneProroghe");
				}
			}
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_GIACENZE)) {
				if (ValidazioneDati.isFilled(currentForm.getCodGiaSing()) ) {
					movimentoSelezionato = this.passaSelezionato(currentForm.getListaGiacenze(), currentForm.getCodGiaSing());
					request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazioneGiacenze");
				}
			}
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_SOLLECITI)) {
				if (ValidazioneDati.isFilled(currentForm.getCodSolSing()) ) {
					movimentoSelezionato = this.passaSelezionato(currentForm.getListaSolleciti(), currentForm.getCodSolSing());
					request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazioneSolleciti");
				}
			}

			if (movimentoSelezionato != null) {

				// leggo dal db e carico il vo con i dati selezionati
				Locale locale = this.getLocale(request, Constants.SBN_LOCALE);
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				movimentoDB = delegate.getDettaglioMovimento(movimentoSelezionato, locale);

				if (movimentoDB == null) {
					// messaggio di errore.

					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.selezioneMovimentoCancellato"));

					saveToken(request);
					return mapping.getInputForward();
				}

				resetToken(request);
				currentForm.getAnaMov().setNuovoMov(false);

				//request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, this.passaSelezionato(currentForm.getListaPrenotazioni(), codPreSing).clone());
				request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimentoDB.clone());

				return mapping.findForward("dettaglio");
			}

			// messaggio di errore.

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));

			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!isTokenValid(request))
			saveToken(request);

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try{

			//effettua l'aggiornamento di tutte le liste
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI)) {
				return this.prenotazioni(mapping, form, request, response);
			}
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_PROROGHE)) {
				return this.proroghe(mapping, form, request, response);
			}
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_GIACENZE)) {
				return this.giacenze(mapping, form, request, response);
			}
			if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_SOLLECITI)) {
				return this.solleciti(mapping, form, request, response);
			}

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward dettaglioMovimentoDiPrenotazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		try{
			if (!isTokenValid(request))
				saveToken(request);



			if (!ValidazioneDati.isFilled(currentForm.getCodPreSing())) {
				// messaggio di errore.
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));

				// nessun codice selezionato
				saveToken(request);
				return mapping.getInputForward();
			}
			resetToken(request);

			MovimentoListaVO prenotazioneSelezionata = this.passaSelezionato(currentForm.getListaPrenotazioni(), currentForm.getCodPreSing());

			//almaviva5_20100219
			InfoDocumentoVO infoDocumento = null;
			if (prenotazioneSelezionata.isRichiestaSuInventario())
				infoDocumento = this.getInfoInventario(
						prenotazioneSelezionata.getCodPolo(),
						prenotazioneSelezionata.getCodBibInv(),
						prenotazioneSelezionata.getCodSerieInv(),
						new Integer(prenotazioneSelezionata.getCodInvenInv()),
						Navigation.getInstance(request).getUserTicket(),
						this.getLocale(request, Constants.SBN_LOCALE),
						request);
			else
				infoDocumento = this.getInfoSegnatura(
						request,
						prenotazioneSelezionata.getCodPolo(),
						prenotazioneSelezionata.getCodBibDocLett(),
						prenotazioneSelezionata.getTipoDocLett(),
						new Integer(prenotazioneSelezionata.getCodDocLet()),
						new Integer(prenotazioneSelezionata.getProgrEsempDocLet()));

			if (infoDocumento != null) {
				currentForm.setInfoDocumentoVO(infoDocumento);
			} else {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

				throw new ValidationException("Inventario non trovato in base dati");
			}

			MovimentoVO movimento =
				delegate.getDettaglioMovimentoDiPrenotazione(
						prenotazioneSelezionata,
						this.getLocale(request, Constants.SBN_LOCALE));
			if (movimento == null) {
				//Movimento non trovato
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.prenotazioni.movimentoAssociato.nonTrovato"));
			}else {
				request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, infoDocumento);
				request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimento.clone());
				request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazionePrenotazioni");

				return mapping.findForward("dettaglio");
			}

			/*
				Map mappaMov = delegate.getListaMovimenti(
						prenotazioneSelezionata,
						RicercaRichiesteType.RICERCA_PER_INVENTARIO, Navigation
								.getInstance(request).getUserTicket(), this
								.getLocale(request, Constants.SBN_LOCALE) );
				List listaMov = (List)mappaMov.get(ServiziConstant.RICHIESTE);

				// rimuovo tra i movimenti estratti quello relativo alla prenotazione
				listaMov.remove(prenotazioneSelezionata);

				if (listaMov == null || listaMov.size() == 0) {
					//Movimento non trovato
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.prenotazioni.movimentoAssociato.nonTrovato"));
				} else if (listaMov.size() > 1) {
					//Errore. Risulta piu di un movimento
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.prenotazioni.movimentoAssociato.piuDiUnMovimento"));
				} else {
					//movimento trovato
					MovimentoListaVO movimento = (MovimentoListaVO)listaMov.get(0);

					request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, infoDocumento);
					request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimento.clone());
//					request.setAttribute(ServiziBaseAction.LISTA_SERVIZI_ATTR, elemov.getLstCodiciServizio());
					request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazionePrenotazioni");

					return mapping.findForward("dettaglio");
				}
				*/
			saveToken(request);

			return mapping.getInputForward();

		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}



	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);



			if (currentForm.getRichiesta().equals("Rifiuta")) {
				if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI)) {
					this.rifiutaMultipla(Arrays.asList(currentForm.getCodPreMul()), Navigation.getInstance(request).getUtente().getFirmaUtente(), request);
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceRifiutiEffettuati"));

					currentForm.setConferma(false);
					return this.prenotazioni(mapping, form, request, response);
				}
			}
			if (currentForm.getRichiesta().equals("Invio Solleciti")) {
				if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_SOLLECITI)) {
					currentForm.setConferma(false);
					return this.prenotaSolleciti(mapping, currentForm.getCodSolMul(), request, response);
				}
			}


			currentForm.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);

		return mapping.getInputForward();
	}


	public ActionForward ordina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			String folder = currentForm.getFolder();
			if (folder.equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI))
				return prenotazioni(mapping, currentForm, request, response);

			if (folder.equals(ErogazioneRicercaForm.FOLDER_SOLLECITI))
				return solleciti(mapping, currentForm, request, response);

			if (folder.equals(ErogazioneRicercaForm.FOLDER_PROROGHE))
				return proroghe(mapping, currentForm, request, response);

			if (folder.equals(ErogazioneRicercaForm.FOLDER_GIACENZE))
				return giacenze(mapping, currentForm, request, response);

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	private boolean caricaPrenotazioni(HttpServletRequest request, ErogazioneRicercaForm currentForm)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);


		MovimentoVO filtroPren = currentForm.getFiltroPrenot().copy();
		filtroPren.setTipoOrdinamento(currentForm.getOrdPrenotazioni());

		DescrittoreBloccoVO blocco1 =
			delegate.getPrenotazioni(filtroPren, filtroPren.getCodBibOperante(), this.getLocale(request, Constants.SBN_LOCALE));

		if (DescrittoreBloccoVO.isFilled(blocco1) ) {
			// abilito i tasti per il blocco se necessario
			currentForm.setAbilitaBlocchi   ((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista          (blocco1.getIdLista());
			currentForm.setTotRighe         (blocco1.getTotRighe());
			currentForm.setTotBlocchi       (blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setMaxRighe         (blocco1.getMaxRighe());
			currentForm.setLivelloRicerca   (Navigation.getInstance(request).getUtente().getCodBib());
			currentForm.setListaPrenotazioni((blocco1.getLista()));
			if (blocco1.getTotRighe() == 1)
				currentForm.setCodPreSing(((MovimentoListaVO)blocco1.getLista().get(0)).getCodRichServ());

		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota.prenotazioni"));
			currentForm.setListaPrenotazioni(new ArrayList<MovimentoListaVO>());
			currentForm.setAbilitaBlocchi(false);
			currentForm.setTotRighe(0);
			return false;
		}

		return true;
	}

	private boolean caricaProroghe(HttpServletRequest request, ErogazioneRicercaForm currentForm)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		MovimentoVO filtroMov = currentForm.getAnaMov().copy();
		filtroMov.setTipoOrdinamento(currentForm.getOrdProroghe());

		DescrittoreBloccoVO blocco1 =
			delegate.getProroghe(filtroMov.getCodBibOperante(),
						filtroMov.getElemPerBlocchi(), navi.getUserTicket(),
						this.getLocale(request, Constants.SBN_LOCALE), filtroMov.getTipoOrdinamento() );


		if (DescrittoreBloccoVO.isFilled(blocco1) ) {
			// abilito i tasti per il blocco se necessario
			currentForm.setAbilitaBlocchi   ((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista          (blocco1.getIdLista());
			currentForm.setTotRighe         (blocco1.getTotRighe());
			currentForm.setTotBlocchi       (blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setMaxRighe         (blocco1.getMaxRighe());
			currentForm.setLivelloRicerca   (Navigation.getInstance(request).getUtente().getCodBib());
			currentForm.setListaProroghe((blocco1.getLista()));
			if (blocco1.getTotRighe() == 1)
				currentForm.setCodProSing(((MovimentoListaVO)blocco1.getLista().get(0)).getCodRichServ());

		} else {
			currentForm.setListaProroghe(new ArrayList<MovimentoListaVO>());
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota.proroghe"));
			return false;
		}

		return true;
	}

	private boolean caricaGiacenze(HttpServletRequest request, ErogazioneRicercaForm currentForm)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		MovimentoVO filtroMov = currentForm.getAnaMov().copy();
		filtroMov.setTipoOrdinamento(currentForm.getOrdGiacenze());

		DescrittoreBloccoVO blocco1 =
			delegate.getGiacenze(filtroMov.getCodBibOperante(),
						filtroMov.getElemPerBlocchi(), navi.getUserTicket(),
						this.getLocale(request, Constants.SBN_LOCALE), filtroMov.getTipoOrdinamento() );


		if (DescrittoreBloccoVO.isFilled(blocco1) ) {
			// abilito i tasti per il blocco se necessario
			currentForm.setAbilitaBlocchi   ((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista          (blocco1.getIdLista());
			currentForm.setTotRighe         (blocco1.getTotRighe());
			currentForm.setTotBlocchi       (blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setMaxRighe         (blocco1.getMaxRighe());
			currentForm.setLivelloRicerca   (Navigation.getInstance(request).getUtente().getCodBib());
			currentForm.setListaGiacenze((blocco1.getLista()));
			if (blocco1.getTotRighe() == 1)
				currentForm.setCodGiaSing(((MovimentoListaVO)blocco1.getLista().get(0)).getCodRichServ());

		} else {
			currentForm.setListaGiacenze(new ArrayList<MovimentoListaVO>());
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota.giacenze"));
			return false;
		}

		return true;
	}


    private void caricaOrdinamentiPrenotazioni(ErogazioneRicercaForm form, HttpServletRequest request)
    {
    	ServiziDelegate delegate = ServiziDelegate.getInstance(request);
    	List<ComboCodDescVO> ordinamentiPrenotazioni;
		try {
			ordinamentiPrenotazioni = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_PRENOTAZIONI);
			ordinamentiPrenotazioni = CaricamentoCombo.cutFirst(ordinamentiPrenotazioni);
		} catch (RemoteException e) {
			ordinamentiPrenotazioni = new ArrayList<ComboCodDescVO>();
		} catch (CreateException e) {
			ordinamentiPrenotazioni = new ArrayList<ComboCodDescVO>();
		}

		form.setOrdinamentiPrenotazioni(ordinamentiPrenotazioni);
    }

    private void caricaOrdinamentiProroghe(ErogazioneRicercaForm form, HttpServletRequest request)
    {
    	ServiziDelegate delegate = ServiziDelegate.getInstance(request);
    	List<ComboCodDescVO> ordinamentiProroghe;
		try {
			ordinamentiProroghe = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_PROROGHE);
			ordinamentiProroghe = CaricamentoCombo.cutFirst(ordinamentiProroghe);
		} catch (RemoteException e) {
			ordinamentiProroghe = new ArrayList<ComboCodDescVO>();
		} catch (CreateException e) {
			ordinamentiProroghe = new ArrayList<ComboCodDescVO>();
		}

		form.setOrdinamentiProroghe(ordinamentiProroghe);
    }

    private void caricaOrdinamentiGiacenze(ErogazioneRicercaForm form, HttpServletRequest request)
    {
    	ServiziDelegate delegate = ServiziDelegate.getInstance(request);
    	List<ComboCodDescVO> ordinamentiGiacenze;
		try {
			ordinamentiGiacenze = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_GIACENZE);
			ordinamentiGiacenze = CaricamentoCombo.cutFirst(ordinamentiGiacenze);
		} catch (RemoteException e) {
			ordinamentiGiacenze = new ArrayList<ComboCodDescVO>();
		} catch (CreateException e) {
			ordinamentiGiacenze = new ArrayList<ComboCodDescVO>();
		}

		form.setOrdinamentiGiacenze(ordinamentiGiacenze);
    }

    private void caricaOrdinamentiSolleciti(ErogazioneRicercaForm form, HttpServletRequest request)
    {
    	ServiziDelegate delegate = ServiziDelegate.getInstance(request);
    	List<ComboCodDescVO> ordinamentiSolleciti;
		try {
			ordinamentiSolleciti = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_SOLLECITI);
			ordinamentiSolleciti = CaricamentoCombo.cutFirst(ordinamentiSolleciti);
		} catch (RemoteException e) {
			ordinamentiSolleciti = new ArrayList<ComboCodDescVO>();
		} catch (CreateException e) {
			ordinamentiSolleciti = new ArrayList<ComboCodDescVO>();
		}

		form.setOrdinamentiSolleciti(ordinamentiSolleciti);
    }

    private void caricaOrdinamentiMovimenti(ErogazioneRicercaForm form, HttpServletRequest request)
    {
    	ServiziDelegate delegate = ServiziDelegate.getInstance(request);
    	List<ComboCodDescVO> ordinamentiMov;
		try {
			ordinamentiMov = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_MOVIMENTI);
			ordinamentiMov = CaricamentoCombo.cutFirst(ordinamentiMov);
		} catch (RemoteException e) {
			ordinamentiMov = new ArrayList<ComboCodDescVO>();
		} catch (CreateException e) {
			ordinamentiMov = new ArrayList<ComboCodDescVO>();
		}

		form.setOrdinamentiMov(ordinamentiMov);
    }

    private MovimentoListaVO passaSelezionato(List<MovimentoListaVO> listaRichieste, String idRichiesta) {
		for (MovimentoListaVO eleMov : listaRichieste) {
			if (eleMov.getIdRichiesta() == Integer.parseInt(idRichiesta) )
				return eleMov;
		}
		return null;
	}

    public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = navi.getUserTicket();

		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null && bloccoVO.getLista().size() > 0) {

				List<MovimentoListaVO> listaDaAggiornare = null;
				String folder = currentForm.getFolder();
				if (folder.equals(ErogazioneRicercaForm.FOLDER_PRENOTAZIONI))
					listaDaAggiornare = currentForm.getListaPrenotazioni();

				if (folder.equals(ErogazioneRicercaForm.FOLDER_SOLLECITI))
					listaDaAggiornare = currentForm.getListaSolleciti();

				if (folder.equals(ErogazioneRicercaForm.FOLDER_PROROGHE))
					listaDaAggiornare = currentForm.getListaProroghe();

				if (folder.equals(ErogazioneRicercaForm.FOLDER_GIACENZE))
					listaDaAggiornare = currentForm.getListaGiacenze();

				listaDaAggiornare.addAll(bloccoVO.getLista());
				Collections.sort(listaDaAggiornare, MovimentoVO.ORDINAMENTO_PER_PROGRESSIVO);

				currentForm.setBloccoSelezionato(bloccoVO.getNumBlocco());
			}
		}
		return mapping.getInputForward();
	}



	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			if (!super.checkAttivita(request, form, idCheck))	//autorizzazione gestione
				return false;

			//almaviva5_20120220 rfid
			ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.RFID))
				return currentForm.isRfidEnabled();

			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.SOLLECITI))
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);

			//almaviva5_20170510 gestione sale
			if (ValidazioneDati.equals(idCheck, "PRENOTAZIONI")) {
				return ValidazioneDati.isFilled(currentForm.getListaPrenotazioni());
			}

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		navi.addBookmark(NavigazioneServizi.BOOKMARK_EROGAZIONE);
		super.init(request, form);

		return;
	}

	public ActionForward prenotazioniMov(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ErogazioneRicercaForm currentForm = (ErogazioneRicercaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		MovimentoListaVO movimentoSelezionato = null;
		if (currentForm.getFolder().equals(ErogazioneRicercaForm.FOLDER_GIACENZE)) {
			if (ValidazioneDati.isFilled(currentForm.getCodGiaSing()) ) {
				movimentoSelezionato = this.passaSelezionato(currentForm.getListaGiacenze(), currentForm.getCodGiaSing());
				request.setAttribute(NavigazioneServizi.PROVENIENZA, "ErogazioneGiacenze");
				request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimentoSelezionato.copy() );
				return navi.goForward(mapping.findForward("prenotazioniMov"));
			}
		}

		return mapping.getInputForward();
	}

}
