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
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO.TipoSIF;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm.RichiestaListaMovimentiType;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class ListaMovimentiAction extends ErogazioneAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.aggiorna",          "aggiorna");
		map.put("servizi.bottone.esamina",           "esamina");
		map.put("servizi.bottone.nuovarichiesta",    "nuovaRichiesta");
		map.put("servizi.bottone.avanti",    		 "nuovaRichiesta");
		map.put("servizi.bottone.rifiuta",           "rifiuta");
		map.put("servizi.bottone.cancella",          "cancella");
		map.put("servizi.bottone.annulla",           "annulla");
		map.put("servizi.bottone.si",                "si");
		map.put("servizi.bottone.no",                "no");
		map.put("button.blocco",                     "blocco");
		map.put("servizi.bottone.deselTutti",        "deselTutti");
		map.put("servizi.bottone.selTutti",          "selTutti");

		map.put("servizi.bottone.ripulisciSegnatura", "ripulisciSegnatura");
		map.put("servizi.bottone.dettaglioMovimento", "dettaglioMovimentoDiPrenotazione");  // ROX 14.04.10  TCK 3676

		//
		map.put("servizi.bottone.hlpinventario",     "sif_inventario");
		map.put("servizi.bottone.hlpsegnatura",      "sif_segnatura");
		map.put("servizi.bottone.hlputente",         "sif_utente");

		map.put("servizi.bottone.mostraSolleciti",   "mostraSolleciti");
		map.put("servizi.bottone.nascondiSolleciti", "nascondiSolleciti");

		map.put("servizi.bottone.stampa", "stampa");

		//almaviva5_20110322 #4118
		map.put("servizi.bottone.esame.utente",  "utente");

		map.put("servizi.bottone.cambioBiblioteca",  "sif_bib_fornitrice");

		//almaviva5_20160216 servizi ILL
		map.put("servizi.bottone.hlpdocaltrabib", "sif_titoloAltraBib");

		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);


			if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_DETTAGLIO_MOVIMENTO)) {
				// se provengo dal tasto chiudi
				// del Dettaglio Movimento
				// ripulisco tutti i campi della form
				// relativamente alla nuova richiesta

				//currentForm.getInfoDocumentoVO().clear();

				RicercaRichiesteType tipoRicerca = currentForm.getTipoRicerca();

				if (tipoRicerca != RicercaRichiesteType.RICERCA_PER_INVENTARIO)
					// questo campo lo ripulisco solo se non provengo
					// da RICERCA_PER_INVENTARIO
					currentForm.getMovRicerca().setCodBibInv("");

				currentForm.setCodSerieInv("");
				currentForm.setCodInvenInv("");
				if (tipoRicerca != RicercaRichiesteType.RICERCA_PER_SEGNATURA)
					// questo campo lo ripulisco solo se non provengo
					// da RICERCA_PER_SEGNATURA
					currentForm.getMovRicerca().setCodBibDocLett("");

				currentForm.setDesXRicSeg("");
				currentForm.setSegnaturaRicerca("");
				currentForm.setCodUtente("");
			}

			//attiva/disattiva riga doc. altra biblioteca
			if (ValidazioneDati.equals(currentForm.getUpdateCombo(), "tipoSrv")) {
				onChangeComboTipoServizio(currentForm);
				return mapping.getInputForward();
			}

			//almaviva5_20170403 prenotazione posto
			PrenotazionePostoVO pp = (PrenotazionePostoVO) request.getAttribute(NavigazioneServizi.PRENOTAZIONE_POSTO);
			if (pp != null) {
				currentForm.getNuovaRichiesta().setPrenotazionePosto(new PrenotazionePostoDecorator(pp));
			}

			if (request.getParameter("cambiaSupportoListaMov") != null)
				return this.cambiaSupportoListaMov(mapping, form, request, response);

			//almaviva5_20151216 servizi ILL
			BibliotecaVO bibForn = (BibliotecaVO) request.getAttribute(NavigazioneProfilazione.BIBLIOTECA);
			if (bibForn != null) {
				if (!bibForn.isILLFornitrice()) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.bib.non.fornitrice"));
					return mapping.getInputForward();
				}

				//fix serialize loop
				bibForn.setBibliotecaILL(null);

				//TODO gestire bib multiple
				currentForm.setBibliotecaFornitrice(ValidazioneDati.asSingletonList(bibForn));
				MovimentoVO nuovaRichiesta = currentForm.getNuovaRichiesta();
				if (nuovaRichiesta != null) {
					DatiRichiestaILLVO datiILL = nuovaRichiesta.getDatiILL();
					datiILL.setResponderId(bibForn.getIsil());
					datiILL.setBibliotecheFornitrici(ValidazioneDati.asSingletonList(bibForn));
				}

				return mapping.getInputForward();
			}


			currentForm.setConfermaNuovaRichiesta(false);

			currentForm.setTipiSupporto(new ArrayList<SupportoBibliotecaVO>());
			currentForm.setModoErogazione(new ArrayList<TariffeModalitaErogazioneVO>());
			//currentForm.setCodTipoServNuovaRich("");

			if (!currentForm.isSessione()) {

				List<ComboVO> listaBib = (List<ComboVO>) request.getAttribute("elencoBiblioteche");
				if (listaBib != null)
					currentForm.setElencoBib(listaBib);

				currentForm.setChiamante(request.getParameter("CHIAMANTE"));
				currentForm.setSegnaturaRicerca((String)request.getAttribute("SegnaturaRicerca"));
				currentForm.setTipoRicerca((RicercaRichiesteType)request.getAttribute("TipoRicerca"));
				currentForm.setMovRicerca((MovimentoVO) request.getAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE));
				MovimentoVO movRicerca = currentForm.getMovRicerca();
				if (movRicerca.getElemPerBlocchi() == 0)
					movRicerca.setElemPerBlocchi(currentForm.getMaxRighe());

				currentForm.setUtenteVO((UtenteBaseVO)request.getAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE));
				currentForm.setInfoDocumentoVO((InfoDocumentoVO)request.getAttribute(NavigazioneServizi.DATI_DOCUMENTO));

				currentForm.setCodBibInv(movRicerca.getCodBibOperante());
				currentForm.setBiblioteca(movRicerca.getCodBibOperante());

				currentForm.setCodSerieInv(movRicerca.getCodSerieInv());
				currentForm.setCodInvenInv(String.valueOf(movRicerca.getCodInvenInv()));

				currentForm.setCodBibUtente(movRicerca.getCodBibUte());

				this.loadDefault(currentForm, request);

				//almaviva5_20120221 rfid
				currentForm.setRfidEnabled(Boolean.valueOf(CommonConfiguration.getProperty(Configuration.RFID_ENABLE, "false")));

				//almaviva5_20151216 servizi ILL
				currentForm.getBibliotecaFornitrice().add(new BibliotecaVO());
				Navigation.getInstance(request).addBookmark(Bookmark.Servizi.LISTA_MOVIMENTI);

				currentForm.setSessione(true);
			}

			if (request.getAttribute("SCEGLI") == null || !request.getAttribute("SCEGLI").equals("SI")) {

				if ( request.getParameter("UTERICERCA") != null) {
					if (request.getAttribute("IdUte") != null || !(request.getAttribute("IdUte")).equals("false")) {
						currentForm.setCodBibUtente((String) request.getAttribute(NavigazioneServizi.COD_BIB_UTENTE));
						currentForm.setCodUtente   ((String) request.getAttribute(NavigazioneServizi.COD_UTENTE));
					}

					return mapping.getInputForward();
				}

				if ( request.getParameter("SERVIZI") != null) {
					if (request.getAttribute("bid") == null) {
						return mapping.getInputForward();
					} else {
						request.setAttribute("desc", request.getAttribute("titolo"));
						request.setAttribute("bid",  request.getAttribute("bid"));

	                    List<String> listaBiblio = new ArrayList<String>();

	                    if (currentForm.getMovRicerca().getCodBibInv().equals("")) {
	            			List<ComboVO> appoBib = new ArrayList<ComboVO>();
	            			appoBib = currentForm.getElencoBib();
	            			for (int i = 1; i < appoBib.size(); i++) {
	            				listaBiblio.add(appoBib.get(i).getCodice());
	            			}
	                    }
	                    else {
	                        listaBiblio.add(currentForm.getMovRicerca().getCodBibInv());
	                    }

	                    request.setAttribute("listaBiblio", listaBiblio);

						return mapping.findForward("interrogaInventario");
					}
				}

				if ( request.getParameter("MOVIMENTI_UTENTE") != null) {
					InventarioTitoloVO inv = (InventarioTitoloVO)request.getAttribute("scelInv");
					if (inv == null) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceParametroErrato"));

						saveToken(request);
					}
					else {
						currentForm.setCodBibInv(inv.getCodBib());
						currentForm.setCodSerieInv(inv.getCodSerie());
						currentForm.setCodInvenInv(String.valueOf(inv.getCodInvent()));
					}
					return mapping.getInputForward();
				}

				currentForm.setDesXRicSeg(currentForm.getSegnaturaRicerca());

				SIFListaDocumentiNonSbnVO segn = (SIFListaDocumentiNonSbnVO) request.getAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN);
				if ( segn != null) {
					DocumentoNonSbnVO doc = segn.getDocumento();

					MovimentoVO filtroMov = currentForm.getMovRicerca();
					filtroMov.setCodBibDocLett(doc.getCodBib() );
					filtroMov.setTipoDocLett(String.valueOf(doc.getTipo_doc_lett()) );
					filtroMov.setCodDocLet(String.valueOf(doc.getCod_doc_lett()) );
					//almaviva5_20130319 #5286
					filtroMov.setProgrEsempDocLet(ServiziUtil.selezionaEsemplareAttivo(doc).getPrg_esemplare() + "");

					currentForm.setDesXRicSeg(doc.getSegnatura() );

					InfoDocumentoVO info = new InfoDocumentoVO();
					info.setDocumentoNonSbnVO(doc);
					info.setTitolo(doc.getTitolo());
					currentForm.setInfoDocumentoVO(info);

					this.loadDefault(currentForm, request);
				}

				//almaviva5_20160217 sif per non posseduto (tipo=D)
				segn = (SIFListaDocumentiNonSbnVO) request.getAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN_ALTRA_BIB);
				if (segn != null) {
					DocumentoNonSbnVO doc = segn.getDocumento();

					MovimentoVO filtroMov = currentForm.getMovRicerca();
					filtroMov.setCodBibDocLett(doc.getCodBib() );
					filtroMov.setTipoDocLett(String.valueOf(doc.getTipo_doc_lett()) );
					filtroMov.setCodDocLet(String.valueOf(doc.getCod_doc_lett()) );
					//almaviva5_20130319 #5286
					filtroMov.setProgrEsempDocLet(ServiziUtil.selezionaEsemplareAttivo(doc).getPrg_esemplare() + "");

					currentForm.setTitoloDocAltraBib(doc.getTitolo());

					InfoDocumentoVO info = new InfoDocumentoVO();
					info.setDocumentoNonSbnVO(doc);
					info.setTitolo(doc.getTitolo());
					currentForm.setInfoDocumentoVO(info);

					//gestione bib fornitrice da doc tipo D
					bibForn = ValidazioneDati.first(doc.getBiblioteche());
					if (bibForn != null) {
						currentForm.setBibliotecaFornitrice(ValidazioneDati.asSingletonList(bibForn));
					}

					//this.loadDefault(currentForm, request);

					return nuovaRichiesta(mapping, currentForm, request, response);
				} else {
					if (currentForm.getNuovaRichiesta() == null || !currentForm.getNuovaRichiesta().isWithPrenotazionePosto())
						currentForm.setCodTipoServNuovaRich("");
				}

			}
			else {
				request.setAttribute("SCEGLI", "NO");
			}

			//ricerca movimenti
			aggiornaListaMovimenti(request, currentForm);

			//se ho trovato dei movimenti per la coppia utente+documento
			//cancello i dati del doc dalla maschera
			if (currentForm.getTipoRicerca() == RicercaRichiesteType.RICERCA_PER_UTENTE) {
				MovimentoVO movRicerca = currentForm.getMovRicerca();
				if (ValidazioneDati.isFilled(movRicerca.getDatiInventario())
					&& ValidazioneDati.isFilled(currentForm.getListaMovRicUte()) ) {
					movRicerca.setCodBibInv("");
					currentForm.setCodSerieInv("");
					currentForm.setCodInvenInv("");
					movRicerca.setCodBibDocLett("");
					currentForm.setDesXRicSeg("");
					//currentForm.setInfoDocumentoVO(null);
					loadDefault(currentForm, request);
				}
			}

			//Controllo se sono stato chiamato da un'altra linea funzionale diversa dai servizi
			//Se non esistono movimenti torno subito al chiamante
			if (currentForm.getChiamante() != null) {
				if (!ValidazioneDati.isFilled(currentForm.getListaMovRicUte()) ) {
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.movimentiNonPresentiPerInventario"));

					return this.backForward(request, true);
				}
			}

			if ( request.getParameter("UTERICERCA") != null) {
				if (request.getAttribute("IdUte") != null || !(request.getAttribute("IdUte")).equals("false")) {
					currentForm.setTipoRicerca((RicercaRichiesteType)request.getAttribute("Provenienza_ricerca"));
					currentForm.setCodBibUtente((String) request.getAttribute(NavigazioneServizi.COD_BIB_UTENTE));
					currentForm.setCodUtente   ((String) request.getAttribute(NavigazioneServizi.COD_UTENTE));
				}
			}

			return mapping.getInputForward();

		} catch (SbnBaseException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			return this.backForward(request, true);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}


	private void onChangeComboTipoServizio(ListaMovimentiForm currentForm) {
		currentForm.setUpdateCombo(null);
		currentForm.setTitoloDocAltraBib(null);
	}


	private void aggiornaListaMovimenti(HttpServletRequest request,	ListaMovimentiForm currentForm) throws Exception {
		if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_DETTAGLIO_MOVIMENTO)) {
			if (NavigazioneServizi.isOggettoModificato(request))
				this.caricaMovimenti(request, currentForm, true);
		} else
			if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_DETTAGLIO_UTENTE)) {
				this.loadDefault(currentForm, request);
				this.caricaMovimenti(request, currentForm, true);
			}
			else
				this.caricaMovimenti(request, currentForm, true);
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			//almaviva5_20100308 esamina su selezione multipla
			List<Long> items = getMultiBoxSelectedItems(currentForm.getCodSelMov());
			if (ValidazioneDati.isFilled(items)) {
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				List<MovimentoListaVO> listaMovimenti = new ArrayList<MovimentoListaVO>();
				for (long idRichiesta : items) {
					MovimentoListaVO selected = passaSelezionato(currentForm.getListaMovRicUte(), idRichiesta + "");
					listaMovimenti.add(delegate.getDettaglioMovimento(selected, Locale.getDefault()));
				}
				request.setAttribute(NavigazioneServizi.LISTA_MOVIMENTI_SELEZIONATI, listaMovimenti);
				request.setAttribute(NavigazioneServizi.PROVENIENZA, "ListaMov");
				return mapping.findForward("ok");
			}

			String idRichiesta = currentForm.getCodSelMovSing();
			if (ValidazioneDati.isFilled(idRichiesta) ) {
				resetToken(request);

				// leggo dal db e carico il vo con i dati selezionati
				Locale locale = this.getLocale(request, Constants.SBN_LOCALE);
				MovimentoListaVO movimentoSelezionato = this.passaSelezionato(currentForm.getListaMovRicUte(), idRichiesta);
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				movimentoSelezionato =  delegate.getDettaglioMovimento(movimentoSelezionato, locale);
				currentForm.setMov(movimentoSelezionato);

				if (movimentoSelezionato == null) {
					// messaggio di errore.
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.selezioneMovimentoCancellato"));
					saveToken(request);
					return mapping.getInputForward();
				}

				movimentoSelezionato = preparaMovimentoPerEsame(request, movimentoSelezionato, currentForm.getMovRicerca(),
					currentForm.getUtenteVO(), currentForm.getInfoDocumentoVO());

				return mapping.findForward("ok");

			} else {
				// messaggio di errore.
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
				saveToken(request);
				return mapping.getInputForward();
			}

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

		// ROX 14.04.10 TCK 3676

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		try {
			if (!isTokenValid(request))
				saveToken(request);


			MovimentoListaVO prenotazioneSelezionata = null;

			List<Long> items = getMultiBoxSelectedItems(currentForm.getCodSelMov());
			if (ValidazioneDati.isFilled(items)) {
				if (items.size() > 1) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricercaOchecksingolo"));

					return mapping.getInputForward();
				}

				currentForm.setCodSelMovSing(String.valueOf(items.get(0)));
				prenotazioneSelezionata = this.passaSelezionato(currentForm.getListaMovRicUte(), currentForm.getCodSelMovSing());
				// documento di tipo prenotazione
				if (prenotazioneSelezionata == null || !prenotazioneSelezionata.isPrenotazione()) {
					prenotazioneSelezionata = null;
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.richiestaNonPrenotazione"));

					return mapping.getInputForward();
				}
			}

			InfoDocumentoVO infoDocumento = null;

			if (prenotazioneSelezionata != null) {
				if (prenotazioneSelezionata.isRichiestaSuInventario())
					infoDocumento = this.getInfoInventario(
							prenotazioneSelezionata.getCodPolo(),
							prenotazioneSelezionata.getCodBibInv(),
							prenotazioneSelezionata.getCodSerieInv(),
							new Integer(prenotazioneSelezionata.getCodInvenInv()),
							navi.getUserTicket(),
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

				if (infoDocumento == null) {
					LinkableTagUtils.addError(request,
							new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

					throw new ValidationException("Inventario non trovato in base dati");
				}

				currentForm.setInfoDocumentoVO(infoDocumento);

				MovimentoVO movimento = delegate.getDettaglioMovimentoDiPrenotazione(
						prenotazioneSelezionata,
						this.getLocale(request, Constants.SBN_LOCALE));
				if (movimento == null) {
					// Movimento non trovato
					LinkableTagUtils.addError(request,
							new ActionMessage("errors.servizi.erogazione.prenotazioni.movimentoAssociato.nonTrovato"));

					return mapping.getInputForward();
				}

				request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO,	infoDocumento);
				request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimento.clone());
				request.setAttribute(NavigazioneServizi.PROVENIENZA, "ListaMovimenti");
				return navi.goForward(mapping.findForward("dettaglio"));
			}

			LinkableTagUtils.addError(request,
				new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));

			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = navi.getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null && bloccoVO.getLista().size() > 0) {
				int posizioneInserimentoBlocco=1;
				int progressivoPrimoElementoBloccoCaricato=((MovimentoListaVO)bloccoVO.getLista().get(0)).getProgr();
				List<MovimentoListaVO> listaDaAggiornare = currentForm.getListaMovRicUte();
				Iterator<MovimentoListaVO> iterator = listaDaAggiornare.iterator();
				while (iterator.hasNext()) {
					if (progressivoPrimoElementoBloccoCaricato < iterator.next().getProgr())
						break;

					posizioneInserimentoBlocco++;
				}
				if (posizioneInserimentoBlocco > listaDaAggiornare.size())
					currentForm.getListaMovRicUte().addAll(bloccoVO.getLista());
				else
					currentForm.getListaMovRicUte().addAll(posizioneInserimentoBlocco-1, bloccoVO.getLista());

//				currentForm.getListaMovRicUte().addAll(bloccoVO.getLista());
//				if (bloccoVO.getNumBlocco() <= bloccoVO.getTotBlocchi())
					currentForm.setBloccoSelezionato(bloccoVO.getNumBlocco());

				// ho caricato tutte le righe sulla form
//				if (currentForm.getListaMovRicUte().size() == bloccoVO.getTotRighe())
//					currentForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}


	public ActionForward sif_inventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;

		//almaviva5_20101103 #3958
		String codPolo = navi.getUtente().getCodPolo();
		String codBib = currentForm.getMovRicerca().getCodBibInv();
		if (!ValidazioneDati.isFilled(codBib))
			codBib = currentForm.getMovRicerca().getCodBibOperante();

		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(codPolo);
		bib.setCod_bib(codBib);
		request.setAttribute(BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO, ValidazioneDati.asSingletonList(bib));

		return navi.goForward(mapping.findForward("interrogaTitolo"));
	}


	public ActionForward sif_segnatura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;

		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		MovimentoVO filtroMov = currentForm.getMovRicerca();
		SIFListaDocumentiNonSbnVO richiesta = new SIFListaDocumentiNonSbnVO();
		richiesta.setCodPolo(filtroMov.getCodPolo());
		richiesta.setCodBib(filtroMov.getCodBibOperante());
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


	public ActionForward sif_utente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
		String codUte = currentForm.getCodUtente();
		if (ValidazioneDati.length(codUte) == 16)
			ricerca.setCodFiscale(codUte.toUpperCase());
		else
			ricerca.setCodUte(codUte);
		request.setAttribute(NavigazioneServizi.RICERCA_UTENTE, ricerca);

		return mapping.findForward("interrogaUtente");
	}


	public ActionForward nuovaRichiesta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		MovimentoVO movimento = null;
		try {
			if (!currentForm.isConfermaNuovaRichiesta() ) {

				// se è la prima volta che effettuo l'elaborazione della nuova richiesta

				//almaviva5_20100317 ricerca per segnatura puntuale
				//da attivare solo se segnatura é valorizzato e:
				// - i dati attuali non sono relativi a una segnatura (info.isRichiestaSuSegnatura() == false)
				// - i dati sono relativi a una segnatura diversa (ord_segnatura != segnRicerca normalizzata)
				String segnRicerca = currentForm.getDesXRicSeg();
				boolean perSegnatura = ValidazioneDati.isFilled(segnRicerca);
				String titoloDocAltraBib = currentForm.getTitoloDocAltraBib();
				boolean perTitoloAltraBib = ValidazioneDati.isFilled(titoloDocAltraBib);

				if (perSegnatura && perTitoloAltraBib) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.impostareSoloUnCanale"));
					throw new ValidationException("Validazione dati fallita");
				}

				if (perSegnatura) {

					InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
					if (info == null
						|| !info.isRichiestaSuSegnatura()
						|| !ValidazioneDati.equals(info.getDocumentoNonSbnVO().getOrd_segnatura(),
								OrdinamentoCollocazione2.normalizza(segnRicerca))) {

						ActionForward forward = preparaRicercaPuntualeSegnatura(
								request, mapping, currentForm.getMovRicerca(),
								info, segnRicerca, null);
						if (forward != null)
							return forward;
					}
				}

				//almaviva5_20160217 servizi ILL
				if (perTitoloAltraBib) {

					InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
					if (info == null
						|| !info.isRichiestaSuSegnatura()
						|| !ValidazioneDati.equals(
								OrdinamentoCollocazione2.normalizza(info.getDocumentoNonSbnVO().getTitolo()),
								OrdinamentoCollocazione2.normalizza(titoloDocAltraBib))) {

						ActionForward forward = preparaRicercaPuntualeSegnatura(
								request, mapping, currentForm.getMovRicerca(),
								info, null, titoloDocAltraBib);
						if (forward != null)
							return forward;
					}
				}

				//controllo dati inseriti per la definizione della nuova richiesta
				this.checkNuovaRichiesta(request, currentForm);

				UtenteBaseVO utente = currentForm.getUtenteVO();

				MovimentoVO movRicerca = currentForm.getMovRicerca();
				movRicerca.setCodBibUte(utente.getCodBib());
				movRicerca.setCodUte(utente.getCodUtente());

				// effettuo il calcolo sui servizi-diritti dell'utente incrociati
				// con i servizi del documento
				// mi salvo la ricerca
				RicercaRichiesteType salvaRicerca = currentForm.getTipoRicerca();
				currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);

				this.loadDefault(currentForm, request);

				currentForm.setTipoRicerca(salvaRicerca);


				if (currentForm.getLstCodiciServizio() != null) {
					if (ValidazioneDati.strIsNull(currentForm.getCodTipoServNuovaRich())) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.servizioAssente"));
						throw new ValidationException("Validazione dati fallita");
					}
				}


				boolean servizioPresente = false;
				if (ValidazioneDati.isFilled(currentForm.getLstCodiciServizio())) {
					// imposto in currentForm.getcodTipoServNuovaRich
					// il Servizio associato al Diritto selezionato
					Iterator<ServizioBibliotecaVO> iterator = null;
					iterator = currentForm.getLstCodiciServizio().iterator();
					while (iterator.hasNext()) {
						ServizioBibliotecaVO servizioVO = iterator.next();
						if (servizioVO.getCodTipoServ() != null && servizioVO.getCodTipoServ().equals(currentForm.getCodTipoServNuovaRich())) {
							currentForm.setCodServNuovaRich(servizioVO.getCodServ());
							servizioPresente = true;
							break;
						}
					}
				}
				else {
					return mapping.getInputForward();
				}
				if (!servizioPresente) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.servizioNonPresente"));
					throw new ValidationException("Il servizio selezionato non è tra quelli disponibili");
				}

			}

			// effettuo la preparazione del movimento e i controlli base
			DatiControlloVO controlliBase = this.controlliNuovaRichiesta(request, mapping, currentForm, getOperatore(request) );

			ControlloAttivitaServizioResult checkControlliBase = controlliBase.getResult();

			movimento = controlliBase.getMovimento();


			// verifico i controlli base
			switch (checkControlliBase) {
			case OK:
			case OK_NON_ANCORA_DISPONIBILE:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
				// successivamente si differenzieranno tali casi
				break;

			case ERRORE_DOCUMENTO_NON_DISPONIBILE:
			case ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL:
				String codNoDisp = controlliBase.getCodNoDisp();
				String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(), descr, controlliBase.getDataRedisp()));
				return mapping.getInputForward();

			case ERRORE_DOCUMENTO_NON_DISPONIBILE_NO_PRENOT:
			default:
				// in caso di errore sui controlli base
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
				throw new ValidationException("errore");
			}


			//controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio
			ControlloAttivitaServizio primoIter = null;
			ServizioBibliotecaVO servizioSelezionato = this.getServizio(currentForm.getLstCodiciServizio(),
					currentForm.getCodTipoServNuovaRich(), currentForm.getCodServNuovaRich());
			List<ControlloAttivitaServizio> listaAttivita = this.getListaAttivitaSuccessive(servizioSelezionato.getCodPolo(),
					servizioSelezionato.getCodBib(), servizioSelezionato.getCodTipoServ(), 0, null, request);

			primoIter = this.primoPassoIter(listaAttivita);

			//almaviva5_20121017 #5146
			int numPezzi = ServiziUtil.getNumeroPaginePerRiproduzione(movimento.getIntervalloCopia());
			if (numPezzi != ServiziConstant.NUM_PAGINE_ERROR)
				movimento.setNumPezzi(String.valueOf(numPezzi));
			else
				LinkableTagUtils.addError(request, new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE) );

			DatiControlloVO controlliPrimoIter = super.eseguiControlli(request, movimento, primoIter, getOperatore(request), false, checkControlliBase);
			if (controlliPrimoIter.getResult() != ControlloAttivitaServizioResult.OK) {
				if (controlliPrimoIter.getResult() == ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE)
					if (currentForm.getNuovaRichiesta() != null)
						return prenotazionePosto(mapping, currentForm, request, response);

				throw new ValidationException("Errore durante creazione nuova richiesta");
			}


			// dopo i controlli sul primo passo dell'iter
			// verifico il risultato dei precedenti controlli base
			// per efffettuare l'eventuale prenotazione
			Timestamp now = DaoManager.now();
			switch (checkControlliBase) {
			case OK:
				break;

			case OK_NON_ANCORA_DISPONIBILE:
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) controlliBase.getCheckData();
				Timestamp tsRitiro = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(), tsRitiro));
				Timestamp tsInizioPrev = movimento.getDataInizioPrev();
				movimento.setDataInizioPrev(tsRitiro.after(tsInizioPrev) ? tsRitiro : tsInizioPrev);
				break;

			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
				disponibilitaVO = (ControlloDisponibilitaVO) controlliBase.getCheckData();
				Timestamp dataPrenotazione = disponibilitaVO.getDataPrenotazione();

				//almaviva5_20151012 nuova gestione prenotazioni
				int prenotazioniPendenti = disponibilitaVO.getPrenotazioniPendenti();
				//indice del messaggio per 0, una o più prenotazioni pendenti.
				int idMsg = prenotazioniPendenti == 0 ? 0 : prenotazioniPendenti > 1 ? 2 : 1;
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(idMsg),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO,
						disponibilitaVO.getMovimentoAttivo().getCodTipoServ()),
						dataPrenotazione,
						//almaviva5_20151012 nuova gestione prenotazioni
						prenotazioniPendenti));

				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				if (dataPrenotazione.before(now) )
					dataPrenotazione = now;

				movimento.setDataInizioPrev(dataPrenotazione);
				currentForm.setNuovaRichiesta(movimento);
				//throw new ValidationException(SbnErrorTypes.SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO);
				currentForm.setConferma(true);
				saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

				return mapping.getInputForward();

			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
				disponibilitaVO = (ControlloDisponibilitaVO) controlliBase.getCheckData();
				Timestamp dataPrenotazione2 = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				if (dataPrenotazione2.before(now) )
					dataPrenotazione2 = now;

				movimento.setDataInizioPrev(dataPrenotazione2);
				currentForm.setNuovaRichiesta(movimento);
				currentForm.setConferma(true);
				saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

				return mapping.getInputForward();

			default:
				// se errore nei controlli base il successivo codice è stato già elaborato in precedenza
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
				throw new ValidationException("errore");
			}

			/*
			// gestire eventuale doppio ingresso:
			// solo controlli oppure controlli + inserimento richiesta
			if (!(request.getAttribute("InserisciRichiesta") != null && request.getAttribute("InserisciRichiesta").equals("SI"))) {
				// se, dopo i controlli, non si deve ancora inserire la richiesta
				// ma richiedere ulteriori campi
				request.setAttribute("InserisciRichiesta", null);
				request.setAttribute(ServiziDelegate.MOV_DA_INSERIRE, movimento);
				request.setAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE,    currentForm.getUtenteVO());
				request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, currentForm.getInfoDocumentoVO());

				currentForm.setCampiModuloRichiesta(true);
				return mapping.findForward("avanti");
			}
			*/


			currentForm.setSalvaIdServizio(servizioSelezionato.getIdServizio());

			if (!currentForm.isConfermaNuovaRichiesta() ) {

				// se è la prima volta che effettuo l'elaborazione della nuova richiesta

				currentForm.setNuovaRichiesta(movimento);

				// i valori successivi sono stati impostati e salvati precedentemente in
				// preparaMovimento di ErogazioneAction

				List<SupportoBibliotecaVO> listaSupporti = (List<SupportoBibliotecaVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_SUPPORTI);
				List<TariffeModalitaErogazioneVO> listaModErog = (List<TariffeModalitaErogazioneVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE);
				if (ValidazioneDati.isFilled(listaSupporti) ) {
					// e siamo in presenza di ulteriori supporti
					// imposto la drop con la lista dei supporti
					currentForm.setTipiSupporto(listaSupporti);
					// nella combo è già impostato il primo elemento della lista dei supporti
					// imposto la drop con la lista delle modalità di erogazione associate al primo supporto
					currentForm.setModoErogazione(listaModErog);
					// nella combo è già impostato il primo elemento della lista delle modalità di erogazione
				}
				else {
					// in caso contrario (non siamo in presenza di ulteriori supporti)
					// la lista dei supporti la creo vuota
					currentForm.setTipiSupporto(new ArrayList<SupportoBibliotecaVO>());
					// imposto la drop con la lista delle modalità di erogazione associate al servizio
					currentForm.setModoErogazione(listaModErog);
					// nella combo è già impostato il primo elemento della lista delle modalità di erogazione
				}

				currentForm.setConfermaNuovaRichiesta(true);
				return mapping.getInputForward();

			}

			// inserisce la nuova richiesta
			movimento = currentForm.getNuovaRichiesta();

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.aggiornaRichiesta(movimento, currentForm.getSalvaIdServizio());

			if (movimento == null)
				return mapping.getInputForward();

			//almaviva5_20141020 #5659
			//movimento = this.creaDettaglioMovimento(request, currentForm, movimento);
			movimento = delegate.getDettaglioMovimento(movimento, this.getLocale(request));

			// carico il vo con i dati selezionati
			request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimento );
			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, currentForm.getMovRicerca());
			request.setAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE,    currentForm.getUtenteVO());
			request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, currentForm.getInfoDocumentoVO());

			request.setAttribute(NavigazioneServizi.PROVENIENZA, "ListaMov");
			request.setAttribute(NavigazioneServizi.NUOVA_RICHIESTA, NavigazioneServizi.NUOVA_RICHIESTA);

			currentForm.setNuovaRichiesta(new MovimentoVO());

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceRichiestaEffettuata"));

			Navigation navi = Navigation.getInstance(request);
			//navi.purgeThis();
			return navi.goForward(mapping.findForward("ok"));

		} catch (ValidationException e) {

			resetToken(request);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);

			if (ValidazioneDati.equals(e.getMessage(), "invNonEsistente"))
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

			currentForm.setNuovaRichiesta(movimento);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		//return this.unspecified(mapping, form, request, response);
	}


	protected void checkDirittiUtente(HttpServletRequest request,
			ListaMovimentiForm currentForm) throws ValidationException, RemoteException {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		UtenteBaseVO utente = currentForm.getUtenteVO();
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		ServizioBibliotecaVO servizioSelezionato = this.getServizio(currentForm.getLstCodiciServizio(),
				currentForm.getCodTipoServNuovaRich(), currentForm.getCodServNuovaRich());

		if (!delegate.isUtenteAutorizzato(utenteCollegato.getCodPolo(), utente.getCodBib(),
				utente.getCodUtente(), currentForm.getMovRicerca().getCodBibOperante(),
				servizioSelezionato.getCodTipoServ(),
				servizioSelezionato.getCodServ())) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.utente.noAut"));

			throw new ValidationException("Utente non autorizzato");
		}

	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		request.setAttribute(NavigazioneServizi.CHIUDI, NavigazioneServizi.CHIUDI_LISTA_MOVIMENTI);

		String chiamante = ((ListaMovimentiForm)form).getChiamante();
		if (ValidazioneDati.equals(chiamante, "DOCUMENTO_FISICO") ||
			ValidazioneDati.equals(chiamante, "DOCUMENTO_NO_SBN"))
			return this.backForward(request, true);

		return this.backForward(request, false);
	}


	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			List<Long> items = getMultiBoxSelectedItems(currentForm.getCodSelMov());

			if (ValidazioneDati.isFilled(items) ) {
				currentForm.setConferma(true);
				currentForm.setRichiesta(RichiestaListaMovimentiType.CANCELLA);

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerCancRif"));

				return mapping.getInputForward();
			}

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
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			List<Long> items = getMultiBoxSelectedItems(currentForm.getCodSelMov());

			if (ValidazioneDati.isFilled(items) ) {
				currentForm.setConferma(true);
				currentForm.setRichiesta(RichiestaListaMovimentiType.RIFIUTA);

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerCancRif"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()) return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);


			UserVO utenteCollegato = navi.getUtente();
			RichiestaListaMovimentiType richiesta = currentForm.getRichiesta();

			switch (richiesta) {
			case CANCELLA:
				this.cancellaMultipla(getMultiBoxSelectedItems(currentForm.getCodSelMov()), utenteCollegato.getFirmaUtente(), request);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
				break;

			case RIFIUTA:
				this.rifiutaMultipla(getMultiBoxSelectedItems(currentForm.getCodSelMov()), utenteCollegato.getFirmaUtente(), request);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceRifiutiEffettuati"));
				break;

			case PRENOTAZIONE:
				MovimentoListaVO dettaglioPrenotazione = this.creaPrenotazione(request, mapping, currentForm);

				if (dettaglioPrenotazione != null) {
					currentForm.setConferma(false);
					request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, dettaglioPrenotazione);
					request.setAttribute(NavigazioneServizi.PROVENIENZA,        "ListaMov");
					request.setAttribute(ServiziBaseAction.LISTA_SERVIZI_ATTR, currentForm.getLstCodiciServizio());
					request.setAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE,    currentForm.getUtenteVO());
					request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, currentForm.getInfoDocumentoVO());
					request.setAttribute(NavigazioneServizi.NUOVA_RICHIESTA, NavigazioneServizi.NUOVA_RICHIESTA);

					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.prenotazioneInserita"));

					return mapping.findForward("nuovo");

				} else {
					currentForm.setConferma(false);
					return mapping.getInputForward();
				}
			}


			currentForm.setConferma(false);

			return this.unspecified(mapping, form, request, response);//mapping.findForward("cancella");

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

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);
		return mapping.getInputForward();
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);


			Long[] elemSel = new Long[currentForm.getListaMovRicUte().size()];
			if (currentForm.getListaMovRicUte().size() > 0) {
				for (int m = 0; m < currentForm.getListaMovRicUte().size(); m++) {
					MovimentoVO singMov = currentForm.getListaMovRicUte().get(m);
					elemSel[m] = new Long(singMov.getCodRichServ());
				}
				currentForm.setCodSelMov(elemSel);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			currentForm.setCodSelMov(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward mostraSolleciti(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			currentForm.setMostraSolleciti(true);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward nascondiSolleciti(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			currentForm.setMostraSolleciti(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripulisciSegnatura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

		currentForm.setDesXRicSeg(null);
		currentForm.setTitoloDocAltraBib(null);
		request.setAttribute("SegnaturaRicerca",   currentForm.getDesXRicSeg());

		MovimentoVO filtroMov = currentForm.getMovRicerca();
		filtroMov.setCodBibDocLett("");
		filtroMov.setCodDocLet("");

		//almaviva5_20100317 ripulisco le info su questo doc.
		InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
		if (info != null)// && info.isRichiestaSuSegnatura())
			info.clear();

		if (currentForm.getNuovaRichiesta() != null)
			currentForm.getNuovaRichiesta().setPrenotazionePosto(null);

		return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	private void loadDefault(ListaMovimentiForm currentForm,
			HttpServletRequest request) throws Exception {

		switch (currentForm.getTipoRicerca()) {
		case RICERCA_PER_UTENTE: {
			loadDefaultRicercaPerUtente(request, currentForm);
			break;
		}

		case RICERCA_PER_INVENTARIO: {
			loadDefaultRicercaPerInventario(request, currentForm);
			break;
		}

		case RICERCA_PER_SEGNATURA: {
			loadDefaultRicercaPerSegnatura(request, currentForm);
			break;
		}

		default:
			currentForm.setLstCodiciServizio(new ArrayList<ServizioBibliotecaVO>());
		}

		if (currentForm.getLstCodiciServizio().size() == 0 ||
		    (currentForm.getLstCodiciServizio().size() == 1 &&
			 currentForm.getLstCodiciServizio().get(0).getCodTipoServ()==null &&
			 currentForm.getLstCodiciServizio().get(0).getCodServ()==null)) {
			// se è presente solo un elemento e questo è
			// quello che contiene i valori a null
			// imposto tutto l'array a null
			currentForm.setLstCodiciServizio(null);
		}
/*
		if (currentForm.getInfoDocumentoVO() != null &&
		    (currentForm.getInfoDocumentoVO().getSegnatura() == null ||
			 currentForm.getInfoDocumentoVO().getSegnatura().trim().equals(""))) {
			// se non è presente la collocazione
			// imposto tutto l'array a null
			// perchè non ci possono essere servizi disponibili
			// per un documento che non ha collocazione
			currentForm.setLstCodiciServizio(null);
		}
*/
		/*
		//almaviva5_20101222 eliminazione dei tipi serv. con codici scaduti
		List<ServizioBibliotecaVO> servizi = currentForm.getLstCodiciServizio();
		if (ValidazioneDati.isFilled(servizi)) {
			Date now = new Date();
			Iterator<ServizioBibliotecaVO> i = servizi.iterator();
			while (i.hasNext()) {
				ServizioBibliotecaVO srv = i.next();
				TB_CODICI cod = CodiciProvider.cercaCodice(srv.getCodTipoServ(), CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null && !cod.getDt_fine_validita().after(now))
					i.remove();
			}
		}
		*/

		//almaviva5_20160927 servizi ill
		TipoServizioVO tipoServizio = getTipoServizio(Navigation.getInstance(request).getPolo(),
				currentForm.getBiblioteca(), currentForm.getCodTipoServNuovaRich(), request);
		if (!tipoServizio.isNuovo() ) {
			List<TB_CODICI> tipiSupportoILL = ILLConfiguration2.getInstance().getListaSupportiILL(tipoServizio);
			tipiSupportoILL = CaricamentoCombo.cutFirst(tipiSupportoILL);
			currentForm.setTipiSupportoILL(tipiSupportoILL);
			List<TB_CODICI> modoErogazioneILL = ILLConfiguration2.getInstance().getListaModErogazioneILL(tipoServizio);
			modoErogazioneILL = CaricamentoCombo.cutFirst(modoErogazioneILL);
			currentForm.setModoErogazioneILL(modoErogazioneILL);
		}
	}

	private void loadDefaultRicercaPerSegnatura(HttpServletRequest request, ListaMovimentiForm currentForm) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		UserVO utente = Navigation.getInstance(request).getUtente();
		InfoDocumentoVO infoDocumentoVO = currentForm.getInfoDocumentoVO();

		DocumentoNonSbnVO documento = infoDocumentoVO.getDocumentoNonSbnVO();
		String codFruiDoc = documento.getCodFruizione();
		if (ValidazioneDati.strIsNull(codFruiDoc)) {
			CommandResultVO result = delegate.invoke(CommandType.CATEGORIA_FRUIZIONE_DOCUMENTO_NON_SBN, documento);
			result.throwError();

			DocumentoNonSbnVO doc = (DocumentoNonSbnVO) result.getResult();
			//check non disponibilità
			codFruiDoc = ValidazioneDati.isFilled(doc.getCodNoDisp()) ? "" : doc.getCodFruizione();
		}

		List<ServizioBibliotecaVO> lstDirittiDocFrui = delegate.getServiziAttiviPerCatFruizione(utente
				.getCodPolo(), documento.getCodBib(), codFruiDoc.trim());

		if (ValidazioneDati.isFilled(lstDirittiDocFrui) ) {

			// riporto lstDirittiDocFrui in lstServiziSegnatura
			// relativamente solo agli elementi che hanno Servizi diversi
			// (in tal modo nella lista dei servizi disponibili per la nuova richiesta
			// ci saranno solo servizi diversi)
			List<ServizioBibliotecaVO> lstServiziSegnatura = new ArrayList<ServizioBibliotecaVO>();
			Iterator<ServizioBibliotecaVO> iteratorDirittiCodFrui = lstDirittiDocFrui.iterator();

			boolean first = true;

			// loop su lstDirittiDocFrui
			while (iteratorDirittiCodFrui.hasNext()) {
				ServizioBibliotecaVO srvSegn = iteratorDirittiCodFrui.next();
				if (first) {
					// il primo elemento viene copiato
					// perchè contiene valori a null
					// (nella drop il primo elemento è vuoto)
					lstServiziSegnatura.add(srvSegn);
					first = false;
					continue;
				}
				Iterator<ServizioBibliotecaVO> iteratorDirittiDoc = lstServiziSegnatura.iterator();
				boolean trovato = false;

				// loop su lstServiziSegnatura
				while (iteratorDirittiDoc.hasNext()) {
					ServizioBibliotecaVO servizioVO_1 = iteratorDirittiDoc.next();
					if (servizioVO_1.getCodTipoServ() != null &&
					    servizioVO_1.getCodTipoServ().equals(srvSegn.getCodTipoServ())){
						trovato = true;
					}
				}
				if (!trovato) {
					// copio solo gli elementi che hanno il servizio diverso
					lstServiziSegnatura.add(srvSegn);
				}
			}

			lstDirittiDocFrui = lstServiziSegnatura;

			// imposto in lstDirittiSegnatura la descrizione del Codice Tipo Servizio
			// prendendola dalla tabella tb_codici
			iteratorDirittiCodFrui = lstDirittiDocFrui.iterator();
			while (iteratorDirittiCodFrui.hasNext()) {
				ServizioBibliotecaVO servizioVO = iteratorDirittiCodFrui.next();
				if (servizioVO.getCodTipoServ() != null) {
					CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(servizioVO.getCodTipoServ(),
							CodiciType.CODICE_TIPO_SERVIZIO,
							CodiciRicercaType.RICERCA_CODICE_SBN));
					if (!servizioVO.isILL() && documento.isDocumentoILL())
						iteratorDirittiCodFrui.remove();
					else
						servizioVO.setDescrTipoServ(ts.getDs_tabella());
				}
			}

		}

		currentForm.setLstCodiciServizio(lstDirittiDocFrui);
	}

	private void loadDefaultRicercaPerInventario(HttpServletRequest request, ListaMovimentiForm currentForm) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		UserVO utente = Navigation.getInstance(request).getUtente();
		InfoDocumentoVO infoDocumentoVO = currentForm.getInfoDocumentoVO();

		InventarioTitoloVO inventario = infoDocumentoVO.getInventarioTitoloVO();
		String codFrui = inventario.getCodFrui() != null ? inventario.getCodFrui() : "";

		List<ServizioBibliotecaVO> lstDirittiInventario = delegate.getServiziAttiviPerCatFruizione(utente
				.getCodPolo(), inventario.getCodBib(), codFrui.trim());

		if (ValidazioneDati.isFilled(lstDirittiInventario) ) {

			// riporto lstDirittiInventario in lstServiziInventario
			// relativamente solo agli elementi che hanno Servizi diversi
			// (in tal modo nella lista dei servizi disponibili per la nuova richiesta
			// ci saranno solo servizi diversi)
			List<ServizioBibliotecaVO> lstServiziInventario = new ArrayList<ServizioBibliotecaVO>();
			Iterator<ServizioBibliotecaVO> iteratorDirittiUtente = lstDirittiInventario.iterator();

			boolean first = true;

			// loop su lstDirittiInventario
			while (iteratorDirittiUtente.hasNext()) {
				ServizioBibliotecaVO srvInv = iteratorDirittiUtente.next();
				if (first) {
					// il primo elemento viene copiato
					// perchè contiene valori a null
					// (nella drop il primo elemento è vuoto)
					lstServiziInventario.add(srvInv);
					first = false;
					continue;
				}
				Iterator<ServizioBibliotecaVO> iteratorDirittiInv = lstServiziInventario.iterator();
				boolean trovato = false;

				// loop su lstServiziInventario
				while (iteratorDirittiInv.hasNext()) {
					ServizioBibliotecaVO servizioVO_1 = iteratorDirittiInv.next();
					if (servizioVO_1.getCodTipoServ() != null &&
					    servizioVO_1.getCodTipoServ().equals(srvInv.getCodTipoServ())){
						trovato = true;
					}
				}
				if (!trovato)
					// copio solo gli elementi che hanno il servizio diverso
					lstServiziInventario.add(srvInv);

			}

			lstDirittiInventario = lstServiziInventario ;

			// imposto in lstDirittiInventario la descrizione del Codice Tipo Servizio
			// prendendola dalla tabella tb_codici
			iteratorDirittiUtente = lstDirittiInventario.iterator();
			while (iteratorDirittiUtente.hasNext()) {
				ServizioBibliotecaVO servizioVO = iteratorDirittiUtente.next();
				if (servizioVO.getCodTipoServ() != null) {
					servizioVO.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO.getCodTipoServ(),
							CodiciType.CODICE_TIPO_SERVIZIO,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}
			}

		}

		currentForm.setLstCodiciServizio(lstDirittiInventario);
	}

	private void loadDefaultRicercaPerUtente(HttpServletRequest request, ListaMovimentiForm currentForm) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		UserVO utente = Navigation.getInstance(request).getUtente();
		MovimentoVO movRicerca = currentForm.getMovRicerca();
		InfoDocumentoVO infoDocumentoVO = currentForm.getInfoDocumentoVO();

		List<ServizioBibliotecaVO> lstDirittiUtente = delegate.getServiziAttivi(utente.getCodPolo(),
			movRicerca.getCodBibUte(), movRicerca.getCodUte(),
			movRicerca.getCodBibOperante(),
			DaoManager.now());

		if (ValidazioneDati.isFilled(lstDirittiUtente) ) {
			// imposto in lstDirittiUtente la descrizione del Codice Tipo Servizio
			// prendendola dalla tabella tb_codici
			Iterator<ServizioBibliotecaVO> iteratorDirittiUtente = lstDirittiUtente.iterator();
			while (iteratorDirittiUtente.hasNext()) {
				ServizioBibliotecaVO servizioVO = iteratorDirittiUtente.next();
				if (servizioVO.getCodTipoServ() != null) {
					servizioVO.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO.getCodTipoServ(),
							CodiciType.CODICE_TIPO_SERVIZIO,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}
			}
		}

		if (ValidazioneDati.size(lstDirittiUtente) == 0) {
			// se la lista dei diritti utente è vuota non effettuo le successive elaborazioni
			// cioè la selezione dei soli diritti comuni tra quelli dell'utente
			// e quelli dell'inventario o segnatura
			currentForm.setLstCodiciServizio(lstDirittiUtente);
		}
		else {

			if (infoDocumentoVO == null) {
				// oltre l'utente non c'è nè inventario nè segnatura
				currentForm.setLstCodiciServizio(lstDirittiUtente);
			}
			else {
				// selezione dei soli diritti comuni tra quelli dell'utente e quelli dell'inventario o segnatura
				if (infoDocumentoVO.isRichiestaSuInventario() ) {

					// oltre l'utente c'è solo l'inventario
					// elaboro lstDirittiInventario
					InventarioTitoloVO inventario = infoDocumentoVO.getInventarioTitoloVO();
					String codFrui = inventario.getCodFrui() != null ? inventario.getCodFrui() : "";

					List<ServizioBibliotecaVO> lstDirittiInventario = delegate.getServiziAttiviPerCatFruizione(utente
							.getCodPolo(), inventario.getCodBib(), codFrui.trim());

					if (ValidazioneDati.isFilled(lstDirittiInventario) ) {

						// confronto lstDirittiUtente e lstDirittiInventario
						// riportando su lstServizi solo gli elementi di
						// lstDirittiUtente che sono presenti anche in lstDirittiInventario

						List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
						Iterator<ServizioBibliotecaVO> iteratorDirittiUtente = lstDirittiUtente.iterator();

						boolean first = true;

						// loop su lstDirittiUtente
						while (iteratorDirittiUtente.hasNext()) {
							ServizioBibliotecaVO srvUtente = iteratorDirittiUtente.next();
							if (first) {
								// il primo elemento viene copiato
								// perchè contiene valori a null
								// (nella drop il primo elemento è vuoto)
								lstServizi.add(srvUtente);
								first = false;
								continue;
							}
							Iterator<ServizioBibliotecaVO> iteratorDirittiInv = lstDirittiInventario.iterator();

							// loop su lstServiziInventario
							while (iteratorDirittiInv.hasNext()) {
								ServizioBibliotecaVO srvInv = iteratorDirittiInv.next();
								if (srvInv.getCodTipoServ() != null &&
									srvInv.getCodServ() != null &&
								    srvInv.getCodTipoServ().equals(srvUtente.getCodTipoServ()) &&
								    srvInv.getCodServ().equals(srvUtente.getCodServ())){
									// copio l'elemento di lstDirittiUtente perchè trovato uguale in lstDirittiInventario
									lstServizi.add(srvUtente);
									break;
								}
							}
						}

						currentForm.setLstCodiciServizio(lstServizi);

					}
					else {
						// imposto setLstCodiciServizio alla lista vuota
						currentForm.setLstCodiciServizio(lstDirittiInventario);
					}

				}
				//i servizi vengono filtrati solo sui doc. posseduti (che hanno una cat. friuzione propria)
				else if (infoDocumentoVO.getDocumentoNonSbnVO().isPosseduto() ) {

					// oltre l'utente c'è solo la segnatura
					// elaboro lstDirittiSegnatura
					DocumentoNonSbnVO documento = infoDocumentoVO.getDocumentoNonSbnVO();
					String codFruiDoc = documento.getCodFruizione();
					if (ValidazioneDati.strIsNull(codFruiDoc)) {
						CommandResultVO result = delegate.invoke(CommandType.CATEGORIA_FRUIZIONE_DOCUMENTO_NON_SBN, documento);
						result.throwError();

						DocumentoNonSbnVO doc = (DocumentoNonSbnVO) result.getResult();
						//cehck disponibilità
						codFruiDoc = ValidazioneDati.isFilled(doc.getCodNoDisp()) ? "" : doc.getCodFruizione();
					}

					List<ServizioBibliotecaVO> lstDirittiSegnatura = delegate.getServiziAttiviPerCatFruizione(utente
							.getCodPolo(), documento.getCodBib(), codFruiDoc.trim());

					if (ValidazioneDati.isFilled(lstDirittiSegnatura) ) {

						// confronto lstDirittiUtente e lstDirittiSegnatura
						// riportando su lstServizi solo gli elementi di
						// lstDirittiUtente che sono presenti anche in lstDirittiSegnatura

						List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
						Iterator<ServizioBibliotecaVO> iteratorDirittiUtente = lstDirittiUtente.iterator();

						boolean first = true;

						// loop su lstDirittiUtente
						while (iteratorDirittiUtente.hasNext()) {
							ServizioBibliotecaVO srvUtente = iteratorDirittiUtente.next();
							if (first) {
								// il primo elemento viene copiato
								// perchè contiene valori a null
								// (nella drop il primo elemento è vuoto)
								lstServizi.add(srvUtente);
								first = false;
								continue;
							}
							Iterator<ServizioBibliotecaVO> iteratorDirittiDoc = lstDirittiSegnatura.iterator();

							// loop su lstDirittiSegnatura
							while (iteratorDirittiDoc.hasNext()) {
								ServizioBibliotecaVO srvSegn = iteratorDirittiDoc.next();

								//almaviva5_20160608 filtro servizio e doc. ILL
								String tipoSrv = srvSegn.getCodTipoServ();
								CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(tipoSrv, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
								if (ts == null)
									continue;
								//almaviva5_20180508 #6585 solo servizi locali
								if (srvSegn.isILL() )
									continue;

								if (tipoSrv != null &&
									srvSegn.getCodServ() != null &&
									tipoSrv.equals(srvUtente.getCodTipoServ()) &&
									srvSegn.getCodServ().equals(srvUtente.getCodServ())) {
									// copio l'elemento di lstDirittiUtente perchè trovato uguale in lstDirittiSegnatura
									lstServizi.add(srvUtente);
									break;
								}
							}
						}

						currentForm.setLstCodiciServizio(lstServizi);

					}
					else {
						// imposto setLstCodiciServizio alla lista vuota
						currentForm.setLstCodiciServizio(lstDirittiSegnatura);
					}
				}
			}

		}
	}

	private void caricaMovimenti(HttpServletRequest request, ListaMovimentiForm currentForm, boolean cambioTipoRicerca)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		DescrittoreBloccoVO blocco1 = null;
		Map<?, ?> mappaRichieste = null;

		//Chiamata funzione per ricerca
		RicercaRichiesteType tipoRicerca = currentForm.getTipoRicerca();
		MovimentoVO movRicerca = currentForm.getMovRicerca();
		switch (tipoRicerca) {
		case RICERCA_PER_UTENTE:
			mappaRichieste = delegate.getListaMovimentiPerErogazione(
					request, movRicerca, tipoRicerca,
					this.getLocale(request, Constants.SBN_LOCALE) );
			blocco1 = (DescrittoreBloccoVO)mappaRichieste.get(ServiziConstant.BLOCCO_RICHIESTE);
			//Controllo presenza solleciti
			currentForm.setListaSollecitiUte((List<SollecitiVO>)mappaRichieste.get(ServiziConstant.SOLLECITI));

			//almaviva5_20100527 se ho trovato con filtro utente ho finito
			//altrimenti riprovo con filtro documento (se impostato)
			if (!cambioTipoRicerca || DescrittoreBloccoVO.isFilled(blocco1) )
				break;

			if (movRicerca.isRichiestaSuInventario())
				tipoRicerca = RicercaRichiesteType.RICERCA_PER_INVENTARIO;
			else if (movRicerca.isRichiestaSuSegnatura())
				tipoRicerca = RicercaRichiesteType.RICERCA_PER_SEGNATURA;
			else
				break;

			currentForm.setCodUtente(movRicerca.getCodUte());
			//continuo con il case su documento

		case RICERCA_PER_INVENTARIO:
		case RICERCA_PER_SEGNATURA:
			mappaRichieste = delegate.getListaMovimentiPerErogazione(
					request, movRicerca, tipoRicerca,
					this.getLocale(request, Constants.SBN_LOCALE) );
			blocco1 = (DescrittoreBloccoVO)mappaRichieste.get(ServiziConstant.BLOCCO_RICHIESTE);

			//se ho trovato qualcosa aggiorno la rappresentazione jsp
			if (DescrittoreBloccoVO.isFilled(blocco1))
				currentForm.setTipoRicerca(tipoRicerca);

			//Controllo presenza solleciti
			currentForm.setListaSollecitiUte((List<SollecitiVO>)mappaRichieste.get(ServiziConstant.SOLLECITI));
			break;

		case RICERCA_LISTE:
			blocco1 = (DescrittoreBloccoVO) request.getAttribute(ServiziDelegate.LISTA_TEMATICHE);
			if (blocco1 == null)
				blocco1 = delegate.caricaListeTematiche(movRicerca, movRicerca.isAttivitaAttuale(), movRicerca.getElemPerBlocchi());

			break;

		default:
			break;
		}

		if (DescrittoreBloccoVO.isFilled(blocco1) ) {

			// inizializza nuovamente i blocchi
			navi.getCache().getCurrentElement().setInfoBlocchi(null);

			// abilito i tasti per il blocco se necessario
			currentForm.setAbilitaBlocchi   ((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista          (blocco1.getIdLista());
			currentForm.setTotRighe         (blocco1.getTotRighe());
			currentForm.setTotBlocchi       (blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setMaxRighe         (blocco1.getMaxRighe());
			currentForm.setLivelloRicerca   (Navigation.getInstance(request).getUtente().getCodBib());
			currentForm.setListaMovRicUte(blocco1.getLista());

		} else {
			currentForm.setListaMovRicUte(new ArrayList<MovimentoListaVO>());
			currentForm.setMov(new MovimentoListaVO());
		}

		if (ValidazioneDati.isFilled(currentForm.getListaSollecitiUte())) {
			currentForm.setSolleciti("S");
			currentForm.setMostraSolleciti(false);
		} else {
			currentForm.setSolleciti("N");
			currentForm.setListaSollecitiUte(new ArrayList<SollecitiVO>());
		}
	}

	protected void caricaSolleciti(HttpServletRequest request, ListaMovimentiForm currentForm)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		if (currentForm.getTipoRicerca() == RicercaRichiesteType.RICERCA_PER_UTENTE ) {
			currentForm.setListaSollecitiUte(delegate.caricaListaSollecitiUte(currentForm.getMovRicerca(), currentForm.getTipoRicerca(), navi.getUserTicket()));
		}
		if (currentForm.getListaSollecitiUte() != null && currentForm.getListaSollecitiUte().size() > 0) {
			currentForm.setSolleciti("S");
			currentForm.setMostraSolleciti(false);
		} else {
			currentForm.setSolleciti("N");
		}
	}

	private void checkNuovaRichiesta(HttpServletRequest request, ListaMovimentiForm currentForm)
	throws NumberFormatException, Exception {
		boolean checkOK = true;


		MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) currentForm.getMovRicerca();

		if (checkInventarioRfid(request, currentForm, movRicerca) ) {
			//impostazione chiavi inventario
			currentForm.setCodBibInv(movRicerca.getCodBibInv());
			currentForm.setCodSerieInv(movRicerca.getCodSerieInv());
			currentForm.setCodInvenInv(movRicerca.getCodInvenInv());
		}

		RicercaRichiesteType tipoRicerca = currentForm.getTipoRicerca();

		switch (tipoRicerca) {
		case RICERCA_PER_UTENTE:
			boolean ricPerInv = false;
			boolean ricPerSegnatura = false;
			boolean ricPerTitoloAltraBib = ValidazioneDati.isFilled(currentForm.getTitoloDocAltraBib());
			if (ValidazioneDati.strIsNull(currentForm.getCodSerieInv()))
				currentForm.setCodSerieInv(NavigazioneServizi.FAKE_SERIE);

			if (ValidazioneDati.strIsNull(movRicerca.getCodBibInv()) && ValidazioneDati.isFilled(currentForm.getCodInvenInv())) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezionareBibliotecaDiInventario"));
				break;
			}

			if (ValidazioneDati.isFilled(movRicerca.getCodBibInv()) && ValidazioneDati.strIsNull(currentForm.getCodInvenInv())	) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.IndicareCodiceInventario"));
					break;
			}

			if (ValidazioneDati.strIsNull(movRicerca.getCodBibDocLett()) && ValidazioneDati.isFilled(currentForm.getDesXRicSeg())) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreCodBibliotecaCampoObbligtorio"));
				break;
			}

			if (ValidazioneDati.isFilled(movRicerca.getCodBibDocLett())
					&& ValidazioneDati.strIsNull(currentForm.getDesXRicSeg())
					&& !ricPerTitoloAltraBib) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezionareCollocazione"));
					break;
			}

			ricPerInv = (ValidazioneDati.isFilled(movRicerca.getCodBibInv()) && ValidazioneDati.isFilled(currentForm.getCodInvenInv()));
			ricPerSegnatura = (ValidazioneDati.isFilled(movRicerca.getCodBibDocLett()) && ValidazioneDati.isFilled(currentForm.getDesXRicSeg()));

			if (ricPerInv && ricPerSegnatura) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.soloInventarioOSegnatura"));
				break;
			}

			if (ricPerInv && ValidazioneDati.isFilled(movRicerca.getRfidChiaveInventario())) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.soloInventarioOSegnatura"));
				break;
			}

			if (!ricPerInv && !ricPerSegnatura && !ricPerTitoloAltraBib) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.unoTraInventarioESegnatura"));
				break;
			}

			if (!ValidazioneDati.strIsNull(currentForm.getCodInvenInv()) && !ValidazioneDati.strIsNumeric(currentForm.getCodInvenInv())) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.InventarioDiBibliotecaNonNumerico"));
				break;
			}

			if (ricPerInv) {
				InfoDocumentoVO infoDocumento = this.getInfoInventario(movRicerca.getCodPolo(), movRicerca.getCodBibInv(), currentForm.getCodSerieInv(), new Integer(currentForm.getCodInvenInv()), Navigation.getInstance(request).getUserTicket(), this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/, request);
				if (infoDocumento != null)
					currentForm.setInfoDocumentoVO(infoDocumento);
				else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

					throw new ValidationException("Inventario non trovato in base dati");
				}
			}

			//almaviva5_20100317
			if (ricPerSegnatura) {
				InfoDocumentoVO infoDocumento =
					this.getInfoSegnatura(request, movRicerca.getCodPolo(),
							movRicerca.getCodBibDocLett(),
							movRicerca.getTipoDocLett(),
							new Integer(movRicerca.getCodDocLet()),
							new Integer(movRicerca.getProgrEsempDocLet()));
				if (infoDocumento != null)
					currentForm.setInfoDocumentoVO(infoDocumento);
				else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

					throw new ValidationException("Inventario non trovato in base dati");
				}
			}
			break;

		case RICERCA_PER_INVENTARIO:
		case RICERCA_PER_SEGNATURA:

			if (!ValidazioneDati.isFilled(currentForm.getCodUtente()) ) {
				checkOK = false;
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.indicareTuttiDatiUtente"));
			}else {
				currentForm.setCodUtente(ServiziUtil.espandiCodUtente(currentForm.getCodUtente()) );
				UtenteBaseVO utente = this.getUtente(currentForm.getCodUtente(), request);

				if (utente == null) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));

					throw new ValidationException("Utente non trovato in base dati");
				} else {
					UtenteBaseVO utenteBib = this.getUtente(movRicerca.getCodPolo(), utente.getCodBib(), utente.getCodUtente(), movRicerca.getCodBibOperante(), request);
					if (utenteBib == null) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteBibNonIscritto", " " + movRicerca.getCodBibOperante()));

						throw new ValidationException("Utente non iscritto nella biblioteca operante");
					}
					currentForm.setUtenteVO(utente);
				}
			}

		default:
			break;
		}

		/*
		if (ValidazioneDati.strIsNull(currentForm.getCodTipoServNuovaRich())) {
			checkOK = false;
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.servizioAssente"));
		}
		*/
		if (!checkOK) {

			throw new ValidationException("Validazione dati fallita");
		}

	}

	protected DatiControlloVO controlliNuovaRichiesta(HttpServletRequest request,
			ActionMapping mapping, ListaMovimentiForm currentForm, OperatoreType richiedente)
			throws ValidationException, Exception {


		boolean ok = true;
		MovimentoVO movimento = null;

		ControlloAttivitaServizio primoIter = null;
		ServizioBibliotecaVO servizioSelezionato = this.getServizio(currentForm.getLstCodiciServizio(),
				currentForm.getCodTipoServNuovaRich(), currentForm.getCodServNuovaRich());
		List<ControlloAttivitaServizio> listaAttivita =
			this.getListaAttivitaSuccessive(servizioSelezionato.getCodPolo(),
				servizioSelezionato.getCodBib(),
				servizioSelezionato.getCodTipoServ(), 0, null,
				request);

		DatiControlloVO controlli = null;

		if (ValidazioneDati.isFilled(listaAttivita) ) {

			primoIter = this.primoPassoIter(listaAttivita);
			if (primoIter != null) {

				MovimentoVO movRicerca = currentForm.getMovRicerca();
				movimento = this.preparaMovimento(request,
								currentForm.getNuovaRichiesta(),
								movRicerca.getCodPolo(),
								movRicerca.getCodBibOperante(),
								currentForm.getUtenteVO(),
								currentForm.getInfoDocumentoVO(),
								servizioSelezionato,
								primoIter,
								richiedente);

				try {
					ControlloAttivitaServizio controlliBase = ControlloAttivitaServizio.getControlliBase();
					controlli = super.eseguiControlli(request, movimento, controlliBase, getOperatore(request), false, null);

					return controlli;

				} catch (ValidationException e) {
					currentForm.setNuovaRichiesta(movimento);
					throw e;
				}

			} else {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
				ok=false;
			}
		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.iterNonAssociatoAlServizio"));
			ok=false;
		}

		if (!ok) {

			throw new ValidationException("Errore durante creazione nuova richiesta");
		} else {
			return controlli;
		}
	}


	protected MovimentoListaVO creaPrenotazione(HttpServletRequest request,
			ActionMapping mapping, ListaMovimentiForm currentForm)
			throws Exception {

		MovimentoVO mov = (MovimentoVO) currentForm.getNuovaRichiesta().clone();
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		ServizioBibliotecaVO servizio =
			this.getServizio(currentForm.getLstCodiciServizio(), mov.getCodTipoServ(), mov.getCodServ());

		// la data fine prevista è così impostata uguale a quella di inizio prevista
		mov.setDataFinePrev(mov.getDataInizioPrev());

		mov.setCodStatoRic(MovimentoVO.STATO_RICHIESTA_IMMESSA);
		mov.setCodStatoMov(MovimentoVO.STATO_PRENOTAZIONE);
		mov.setFlTipoRec(RichiestaRecordType.FLAG_PRENOTAZIONE);
		mov.setCodBibDest(mov.getCodBibOperante());

		List<ControlloAttivitaServizio> listaAttivita =
			super.getListaAttivitaSuccessive(mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(), 0, null, request);

		if (!ValidazioneDati.isFilled(listaAttivita) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
			return null;
		}

		ControlloAttivitaServizio primoIter = super.primoPassoIter(listaAttivita);
		if (primoIter == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
			return null;
		}

		/*
		//almaviva5_20100427 la data inizio prevista all'atto della pronotazione
		//deve essere conservata se maggiore della data di inoltro (now)
		Timestamp now = DaoManager.now();
		Timestamp dataInizioPrev = movimento.getDataInizioPrev();
		movimento.setDataInizioPrev(dataInizioPrev.after(now) ? dataInizioPrev : now);
		*/

		try {
			// controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio
			DatiControlloVO datiControllo = super.eseguiControlli(request, mov, primoIter, getOperatore(request), false, null);
			if (datiControllo.getResult() != ControlloAttivitaServizioResult.OK) {
				LinkableTagUtils.addError(request, new ActionMessage(datiControllo.getResult().getMessage()) );
				return null;
			}
		} catch (ValidationException e) {
			resetToken(request);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);

			currentForm.setNuovaRichiesta(null);
			return null;
		}

		delegate.aggiornaRichiesta(mov, servizio.getIdServizio());
		currentForm.setNuovaRichiesta(null);

		return this.creaDettaglioMovimento(request, currentForm, mov);

	}


	private MovimentoListaVO passaSelezionato(List<MovimentoListaVO> listaMov, String codRicSer) {
		for (int i = 0; i < listaMov.size(); i++) {
			MovimentoListaVO currentForm = listaMov.get(i);
			if (currentForm.getCodRichServ().equals(codRicSer)) {
				return currentForm;
			}
		}
		return null;
	}





	private MovimentoListaVO creaDettaglioMovimento(HttpServletRequest request, ListaMovimentiForm currentForm, MovimentoVO movimentoVO)
	throws Exception {
		return this.creaDettaglioMovimento(request, currentForm.getUtenteVO(), currentForm.getInfoDocumentoVO(), movimentoVO);
	}


	protected enum TipoOperazione {
		PRENOTAZIONE_POSTO,
		GESTIONE,
		NUOVA_RICHIESTA,
		PERIODICO,
		TARIFFA,
		PASSWORD,
		INTERVALLO_COPIE,
		ILL_BIB_FORNITRICE,
		SUPPORTO_MODALITA_ILL,
		TIPO_SERVIZIO_ILL,
		DOC_ALTRA_BIBLIOTECA,
		DOC_LOCALE;
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			if (!super.checkAttivita(request, form, idCheck))	//autorizzazione gestione
				return false;

			//almaviva5_20120220 rfid
			ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.RFID))
				return currentForm.isRfidEnabled();

			TipoOperazione attivita = TipoOperazione.valueOf(idCheck);
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			InfoDocumentoVO infoVO = currentForm.getInfoDocumentoVO();

			switch (attivita) {
			case NUOVA_RICHIESTA:
				if (ValidazioneDati.size(currentForm.getLstCodiciServizio()) < 1) // lista servizi vuota
					return false;

				return delegate.isAbilitatoErogazione() && (!currentForm.getTipoRicerca().equals(RicercaRichiesteType.RICERCA_LISTE) );

			case PRENOTAZIONE_POSTO: {
				MovimentoVO mov = currentForm.getNuovaRichiesta();
				return (mov != null && mov.getPrenotazionePosto() != null);
			}

			case PERIODICO:
				boolean periodico = false;
				if (infoVO != null && currentForm.getNuovaRichiesta() != null)
					if (infoVO.isRichiestaSuInventario())
						periodico = ValidazioneDati.equals(infoVO.getInventarioTitoloVO().getNatura(), "S");
					else
						periodico = ValidazioneDati.equals(infoVO.getDocumentoNonSbnVO().getNatura(), 'S');

				return periodico;

			case INTERVALLO_COPIE:
				if (infoVO == null || currentForm.getNuovaRichiesta() == null
						|| !ValidazioneDati.isFilled(currentForm.getTipiSupporto()))
					return false;

			case ILL_BIB_FORNITRICE: {
				//almaviva5_20151216 servizi ILL
				MovimentoVO nuovaRichiesta = currentForm.getNuovaRichiesta();
				if (nuovaRichiesta == null)
					return false;

				String tipoSrv = currentForm.getCodTipoServNuovaRich();
				CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(tipoSrv, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
				//tipo servizio ill/locale su flag4.
				if (ts != null && ts.isILL()) {
					if (nuovaRichiesta.isRichiestaILL() && infoVO != null && infoVO.isRichiestaSuSegnatura() )
						return true;
				}

				return false;
			}

			case SUPPORTO_MODALITA_ILL: {
				MovimentoVO nuovaRichiesta = currentForm.getNuovaRichiesta();
				if (nuovaRichiesta != null) {
					DatiRichiestaILLVO datiILL = nuovaRichiesta.getDatiILL();
					if (datiILL != null && datiILL.isRichiedente() && ValidazioneDati.isFilled(datiILL.getResponderId()))
						return true;
				}
				return false;
			}

			case TIPO_SERVIZIO_ILL: {
				MovimentoVO nuovaRichiesta = currentForm.getNuovaRichiesta();
				if (nuovaRichiesta != null) {
					TipoServizioVO tipoServizio = getTipoServizio(nuovaRichiesta.getCodPolo(),
							nuovaRichiesta.getCodBibOperante(), nuovaRichiesta.getCodTipoServ(), request);
					if (!tipoServizio.isNuovo() ) {
						if (tipoServizio.isILL() && ValidazioneDati.in(ILLServiceType.valueOf(tipoServizio.getCodServizioILL()),
								ILLServiceType.PR, ILLServiceType.PI))
							return false;
					}
				}
				return true;
			}

			case DOC_ALTRA_BIBLIOTECA: {
				TipoServizioVO tipoServizio = getTipoServizio(Navigation.getInstance(request).getPolo(),
						currentForm.getBiblioteca(), currentForm.getCodTipoServNuovaRich(), request);
				ILLServiceType servizioILL = checkConfigurazioneServizioILL(tipoServizio);
				return (servizioILL != null);
			}

			case DOC_LOCALE: {
				TipoServizioVO tipoServizio = getTipoServizio(Navigation.getInstance(request).getPolo(),
						currentForm.getBiblioteca(), currentForm.getCodTipoServNuovaRich(), request);
				ILLServiceType servizioILL = checkConfigurazioneServizioILL(tipoServizio);
				return (servizioILL == null);	//serv locale o ill NON configurato
			}

			default:
				break;
			}
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);


			MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) currentForm.getMovRicerca();//.copy();
			if (!movRicerca.isRichiesteInCorso() &&
				!movRicerca.isRichiesteRespinte() &&
				!movRicerca.isRichiesteEvase() ) {
				// invio un messaggio di errore se non presente almeno un check sulla lista movimenti
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ImpostareCheckMovimenti"));
				return mapping.getInputForward();
			}

			if (checkInventarioRfid(request, currentForm, movRicerca)) {
				//impostazione chiavi inventario
				currentForm.setCodBibInv(movRicerca.getCodBibInv());
				currentForm.setCodSerieInv(movRicerca.getCodSerieInv());
				currentForm.setCodInvenInv(movRicerca.getCodInvenInv());
			}

			//controllo dati
			boolean checkOK = true;

			// salvo tipoRicerca il tipo della ricerca
			RicercaRichiesteType tipoRicerca = currentForm.getTipoRicerca();

			switch (tipoRicerca) {
			case RICERCA_PER_UTENTE:
				boolean ricPerInv = false;
				boolean ricPerSegnatura = false;
				if (ValidazioneDati.strIsNull(currentForm.getCodSerieInv()))
					currentForm.setCodSerieInv(NavigazioneServizi.FAKE_SERIE);

				if (ValidazioneDati.strIsNull(movRicerca.getCodBibInv()) && ValidazioneDati.isFilled(currentForm.getCodInvenInv())) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezionareBibliotecaDiInventario"));
					break;
				}
				if (ValidazioneDati.isFilled(movRicerca.getCodBibInv()) && ValidazioneDati.strIsNull(currentForm.getCodInvenInv())	) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.IndicareCodiceInventario"));
					break;
				}

				if (ValidazioneDati.strIsNull(movRicerca.getCodBibDocLett()) && ValidazioneDati.isFilled(currentForm.getDesXRicSeg())) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreCodBibliotecaCampoObbligtorio"));
					break;
				}
				if (ValidazioneDati.isFilled(movRicerca.getCodBibDocLett()) && ValidazioneDati.strIsNull(currentForm.getDesXRicSeg())	) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezionareCollocazione"));
					break;
				}


				ricPerInv = (ValidazioneDati.isFilled(movRicerca.getCodBibInv()) && ValidazioneDati.isFilled(currentForm.getCodInvenInv()));
				ricPerSegnatura = (ValidazioneDati.isFilled(movRicerca.getCodBibDocLett()) && ValidazioneDati.isFilled(currentForm.getDesXRicSeg()));

				if (ricPerInv && ricPerSegnatura) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.soloInventarioOSegnatura"));
					break;
				}

				if (!ValidazioneDati.strIsNull(currentForm.getCodInvenInv()) && !ValidazioneDati.strIsNumeric(currentForm.getCodInvenInv())) {
					checkOK = false;
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.InventarioDiBibliotecaNonNumerico"));
					break;
				}

				if (ricPerInv) {
					InfoDocumentoVO infoDocumento = this.getInfoInventario(movRicerca.getCodPolo(), movRicerca.getCodBibInv(), currentForm.getCodSerieInv(), new Integer(currentForm.getCodInvenInv()), Navigation.getInstance(request).getUserTicket(), this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/, request);
					if (infoDocumento != null) {
						currentForm.setInfoDocumentoVO(infoDocumento);
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));

						throw new ValidationException("Inventario non trovato in base dati");
					}
				}
				break;
			case RICERCA_PER_INVENTARIO:
			case RICERCA_PER_SEGNATURA:

				if (ValidazioneDati.strIsNull(currentForm.getCodUtente()) ) {
					currentForm.setUtenteVO(null);
					movRicerca.setCodUte("");
					movRicerca.setCodBibUte("");
				} else {

					currentForm.setCodUtente(ServiziUtil.espandiCodUtente(currentForm.getCodUtente()) );
					UtenteBaseVO utente = this.getUtente(currentForm.getCodUtente(), request);

					if (utente == null) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));

						throw new ValidationException("Utente non trovato in base dati");
					} else {
						UtenteBaseVO utenteBib = this.getUtente(movRicerca.getCodPolo(), utente.getCodBib(), currentForm.getCodUtente(), movRicerca.getCodBibOperante(), request);
						if (utenteBib == null) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteBibNonIscritto", " " + movRicerca.getCodBibOperante()));

							throw new ValidationException("Utente non iscritto nella biblioteca operante");
						}
						currentForm.setUtenteVO(utente);
					}
				}

			default:
				checkOK = false;
				break;

			}

			if (!checkOK) {

				throw new ValidationException("Validazione dati fallita");
			}

			if (currentForm.getUtenteVO() != null) {
				UtenteBaseVO utente = currentForm.getUtenteVO();
				movRicerca.setCodUte(utente.getCodUtente());
				movRicerca.setCodBibUte(utente.getCodBib());
			} else {
				movRicerca.setCodUte("");
				movRicerca.setCodBibUte("");
			}


			// riporto nel movimento i campi dell'inventario se impostati
			if (ValidazioneDati.isFilled(currentForm.getCodBibInv()) &&
				ValidazioneDati.isFilled(currentForm.getCodInvenInv())) {
				movRicerca.setCodBibInv(currentForm.getCodBibInv());
				movRicerca.setCodSerieInv(currentForm.getCodSerieInv());
				movRicerca.setCodInvenInv(currentForm.getCodInvenInv());
			} else {
				// se non impostato l'inventario ripulisco in InfoDocumentoVO
				// la parte dell'inventario
				InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
				if (info != null && info.isRichiestaSuInventario())
					currentForm.getInfoDocumentoVO().setInventarioTitoloVO(null);
			}


			// riporto nel movimento la segnatura se impostata

			//almaviva5_20100317 ricerca per segnatura puntuale
			//da attivare solo se segnatura é valorizzato e:
			// - i dati attuali non sono relativi a una segnatura (info.isRichiestaSuSegnatura() == false)
			// - i dati sono relativi a una segnatura diversa (ord_segnatura != segnRicerca normalizzata)
			String segnRicerca = currentForm.getDesXRicSeg();
			if (ValidazioneDati.isFilled(segnRicerca) ) {

				InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
				if (info == null
					|| !info.isRichiestaSuSegnatura()
					|| !ValidazioneDati.equals(info.getDocumentoNonSbnVO().getOrd_segnatura(),
							OrdinamentoCollocazione2.normalizza(segnRicerca))) {

					if (info == null) {
						info = new InfoDocumentoVO();
						currentForm.setInfoDocumentoVO(info);
					}

					ActionForward forward = preparaRicercaPuntualeSegnatura(
							request, mapping, movRicerca,
							info, segnRicerca, null);
					if (forward != null)
						return forward;
				}
			} else {
				// se non impostata la segnatura ripulisco in InfoDocumentoVO
				// la parte della segnatura
				InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
				if (info != null && info.isRichiestaSuSegnatura())
					currentForm.getInfoDocumentoVO().setDocumentoNonSbnVO(null);
			}


			// se non impostato nè l'inventario nè la segnatura ripulisco tutto InfoDocumentoVO
			InfoDocumentoVO info = currentForm.getInfoDocumentoVO();
			if (info != null && !info.isRichiestaSuInventario() &&
					            !info.isRichiestaSuSegnatura()) {
				currentForm.setInfoDocumentoVO(null);
			}


			if (ValidazioneDati.isFilled(movRicerca.getCodUte())) {
				if (ValidazioneDati.in(tipoRicerca,
						RicercaRichiesteType.RICERCA_PER_INVENTARIO,
						RicercaRichiesteType.RICERCA_PER_SEGNATURA) ) {
					// se presente un utente (impostato successivamente nella seconda schermata)
					// imposto il corretto tipoRicerca per il successivo caricaMovimenti
					currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
				}
			}


			//carica i movimenti
			this.caricaMovimenti(request, currentForm, false);

			// effettuo il calcolo sui servizi disponibili
			this.loadDefault(currentForm, request);

			// ripristino il tipo della ricerca al suo valore iniziale
			currentForm.setTipoRicerca(tipoRicerca);

			return mapping.getInputForward();

		} catch (ValidationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC) {
				//LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaUtenti.nessunaSelezionePerStampa"));
				//
				LinkableTagUtils.addError(request, e);
			}
			resetToken(request);
			log.info(e.getMessage());
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;

		// aggiunta rox ROX 14.04.10  TCK 3388
		List<Long> items = getMultiBoxSelectedItems(currentForm.getCodSelMov());
		if (ValidazioneDati.isFilled(items)) {

			if (items.size() > 1) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricercaOchecksingolo"));

				return mapping.getInputForward();
			} else
				 currentForm.setCodSelMovSing(String.valueOf(items.get(0)));
		}
		// fine aggiunta rox

		request.setAttribute("checkS", currentForm.getCodSelMovSing());
		String check = currentForm.getCodSelMovSing();
		if (check != null && check.length() != 0) {
			Locale locale = this.getLocale(request, Constants.SBN_LOCALE);
			MovimentoListaVO movimento =  this.passaSelezionato(currentForm.getListaMovRicUte(), currentForm.getCodSelMovSing());
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			currentForm.setMov(delegate.getDettaglioMovimento(movimento, locale));

			// acquisisco il numero copie e il testo della stampa modulo
			// dalla configurazione dell'attività

			List<IterServizioVO> listaIterServizio = this.getIterServizio(currentForm.getMov().getCodPolo(),
					currentForm.getMov().getCodBibOperante(), currentForm.getMov().getCodTipoServ(), request);

			Iterator<IterServizioVO> iterator = listaIterServizio.iterator();
			IterServizioVO iterServizioVO = null;

			while (iterator.hasNext()) {
				iterServizioVO = iterator.next();
				if (iterServizioVO.getCodAttivita().equals(currentForm.getMov().getCodAttivita()))
					break;
			}

			ControlloAttivitaServizio attivita = delegate.getAttivita(currentForm.getMov().getCodPolo(), iterServizioVO);

			// Imposto il numero copie della stampa modulo richiesta
			if (String.valueOf(attivita.getPassoIter().getNumPag()).equals("0")) {
				currentForm.getMov().setNumeroCopieStampaModulo("1");
			} else {
				currentForm.getMov().setNumeroCopieStampaModulo(String.valueOf(attivita.getPassoIter().getNumPag()));
			}
			// Imposto il testo della stampa modulo richiesta
			currentForm.getMov().setTestoStampaModulo(attivita.getPassoIter().getTesto());

			request.setAttribute("codBib", currentForm.getBiblioteca());
			request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_RICHIESTA);
			request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getMov());
			return  mapping.findForward("stampaRichiesta");
		} else {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaUtenti.nessunaSelezionePerStampa"));

			return mapping.getInputForward();
		}
	}

	public ActionForward prenotazionePosto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			ParametriServizi params = new ParametriServizi();
			BibliotecaVO bib = new BibliotecaVO();
			bib.setCod_polo(currentForm.getMovRicerca().getCodPolo());
			bib.setCod_bib(currentForm.getMovRicerca().getCodBibOperante());
			params.put(ParamType.BIBLIOTECA, bib);
			params.put(ParamType.DETTAGLIO_MOVIMENTO, this.creaDettaglioMovimento(
					request, currentForm.getUtenteVO(), currentForm
					.getInfoDocumentoVO(), currentForm.getNuovaRichiesta() ));
			params.put(ParamType.MODALITA_GESTIONE_PRENOT_POSTO, ModalitaGestioneType.CREA);
			ParametriServizi.send(request, params);

			return navi.goForward(mapping.findForward("prenotaPosto"));

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	public ActionForward cambiaSupportoListaMov(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			currentForm.setConferma(false);

			currentForm.setFlgErrNuovaRichiesta(false);

			MovimentoVO detMov = currentForm.getNuovaRichiesta();

			// ult_supp è "S"

			// La lista dei supporti, estratta a partire dalla Categoria di Riproduzione,
			// è stata precedentemente salvata su TipiSupporto presente sul Form
			// Il supporto selezionato è presente su currentForm.getDetMov().getCodSupporto()

			// estraggo le modalità di erogazione associate al supporto selezionato
			List<SupportiModalitaErogazioneVO> listaSupportiModalitaErogazione = this.getSupportiModalitaErogazione(detMov.getCodPolo(),
					detMov.getCodBibOperante(), detMov.getCodSupporto(), request);
			if (ValidazioneDati.isFilled(listaSupportiModalitaErogazione) ) {
				List<TariffeModalitaErogazioneVO> listaTariffe = new ArrayList<TariffeModalitaErogazioneVO>();
				Iterator<SupportiModalitaErogazioneVO> iterator_1 = listaSupportiModalitaErogazione.iterator();
				while (iterator_1.hasNext()) {
					TariffeModalitaErogazioneVO tariffeModalitaErogazioneVO = new TariffeModalitaErogazioneVO();
					ClonePool.copyCommonProperties(tariffeModalitaErogazioneVO,iterator_1.next());
					listaTariffe.add(tariffeModalitaErogazioneVO);
				}
				currentForm.setModoErogazione(listaTariffe);
				// imposto la prima modalità di erogazione
				detMov.setCod_erog(listaTariffe.get(0).getCodErog());
			} else {

				// ERRORE !!! Non ci sono modalità di erogazione
				// associate al supporto selezionato
				currentForm.setModoErogazione(new ArrayList<TariffeModalitaErogazioneVO>());
				currentForm.setFlgErrNuovaRichiesta(true);

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonAssociateAlSupporto"));

				saveToken(request);
				return mapping.getInputForward();

			}

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	public ActionForward utente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//almaviva5_20110322 #4118
		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		UtenteBaseVO utente = currentForm.getUtenteVO();
		return ServiziDelegate.getInstance(request).sifDettaglioUtente(currentForm.getMovRicerca().getCodBibOperante(),
			utente.getCodBib(), utente.getCodUtente(), 0);
	}

	public ActionForward sif_bib_fornitrice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//almaviva5_20110322 #4118
		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
		richiesta.setIsilBibRichiedente(currentForm.getNuovaRichiesta().getDatiILL().getRequesterId());
		return BibliotecaDelegate.getInstance(request).getSIFListaBibliotecheILLFornitrici(richiesta);
	}

	public ActionForward sif_titoloAltraBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaMovimentiForm currentForm = (ListaMovimentiForm) form;
		request.setAttribute("currentForm", form);
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		//almaviva5_20160610 check tipo servizio selezionato (solo ILL)
		String tipoSrv = currentForm.getCodTipoServNuovaRich();
		CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(tipoSrv, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
		if (ts == null || !ts.isILL()) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.servizio.errato.ricerca.opac"));
			return mapping.getInputForward();
		}

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		MovimentoVO filtroMov = currentForm.getMovRicerca();
		SIFListaDocumentiNonSbnVO richiesta = new SIFListaDocumentiNonSbnVO();
		richiesta.setCodPolo(filtroMov.getCodPolo());
		richiesta.setCodBib(filtroMov.getCodBibOperante());
		richiesta.setTitolo(currentForm.getTitoloDocAltraBib());
		richiesta.setTipoSIF(TipoSIF.DOCUMENTO_ALTRA_BIB);
		richiesta.setRequestAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN_ALTRA_BIB);
		//almaviva5_20160609 servizi ILL
		richiesta.setTipoServizio(currentForm.getCodTipoServNuovaRich());

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

}
