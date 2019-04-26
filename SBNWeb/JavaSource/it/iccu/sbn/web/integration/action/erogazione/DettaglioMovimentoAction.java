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
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.matchers.Servizi;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator.Sezioni;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.ill.DatiRichiestaILLDecorator;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.actionforms.servizi.ServiziFormTypes;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.DettaglioMovimentoForm;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.DettaglioMovimentoForm.RichiestaDettaglioMovimentoType;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class DettaglioMovimentoAction extends ErogazioneAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.stampa",           "stampa");
		map.put("servizi.bottone.ok",               "ok");
		map.put("servizi.bottone.avanti",           "ok");
		map.put("servizi.bottone.annulla",          "annulla");
		map.put("servizi.bottone.cancella",         "cancella");
		map.put("servizi.bottone.respingi",         "rifiuta");
		map.put("servizi.bottone.rinnova",          "rinnova");
		map.put("servizi.bottone.avanza",           "avanza");
		map.put("servizi.bottone.prenotazioni",     "prenotazioni");
		map.put("servizi.bottone.cambiaServ",       "cambiaServizio");
		map.put("servizi.bottone.si",               "si");
		map.put("servizi.bottone.no",               "no");
		map.put("servizi.bottone.confermaServizio", "confermaServizio");
		map.put("servizi.bottone.inoltro",			"inoltro");

		//almaviva5_20100308
		map.put("button.elemPrec",					"indietro");
		map.put("button.elemSucc",					"avanti");

		//almaviva5_20100421 #3463
		map.put("servizi.bottone.rifiuta.proroga",  "rifiutaProroga");

		//almaviva5_20110322 #4118
		map.put("servizi.bottone.esame.utente",  "utente");
		//almaviva5_20110414 #4347
		map.put("servizi.bottone.esame.inv", "documento");

		//almaviva5_20150127 servizi ill
		map.put("servizi.bottone.mov.dati.ill", "datiILL");
		map.put("servizi.bottone.mov.ill.inoltro.bib.forn", "inoltroILL");
		map.put("servizi.bottone.ill.proponi.annullamento", "rifiuta");

		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setData_sosp(null);
				//almaviva5_20100308 esamina multiplo
				List<MovimentoListaVO> listaMovimenti = (List<MovimentoListaVO>) request.getAttribute(NavigazioneServizi.LISTA_MOVIMENTI_SELEZIONATI);
				if (ValidazioneDati.isFilled(listaMovimenti)) {
					currentForm.setListaMovimenti(listaMovimenti);
					request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, listaMovimenti.get(0));
				}

				//almaviva5_20110415 #4347
				navi.addBookmark(Bookmark.Servizi.DETTAGLIO_MOVIMENTO);

				currentForm.setSessione(true);
			}

			/*
			List<MovimentoListaVO> listaMovimenti = currentForm.getListaMovimenti();
			if (ValidazioneDati.isFilled(listaMovimenti) && !currentForm.isCambioServizio() ) {
				//recupero il movimento alla posizione selezionata per scorrimento
				int pos = currentForm.getPosizioneCorrente();
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				MovimentoListaVO movimentoSelezionato = listaMovimenti.get(pos);
				movimentoSelezionato = delegate.getMovimentoListaVO(navi.getUserTicket(), movimentoSelezionato, getLocale(request) );
				movimentoSelezionato = preparaMovimentoPerEsame(request, movimentoSelezionato, null, null, null);
				listaMovimenti.set(pos, movimentoSelezionato);
			}
			*/
			//currentForm.setConfermaNuovaRichiesta(false);

			currentForm.setVengoDa((String)request.getAttribute(NavigazioneServizi.PROVENIENZA));

			MovimentoListaVO movimento = (MovimentoListaVO) request.getAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO);
			if (movimento != null) {
				log.debug("Caricamento dettaglio movimento n. " + movimento.getIdRichiesta() );
				currentForm.setDetMov(movimento);
			}

			//almaviva5_20110415 #4347
			if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_DETTAGLIO_DOCUMENTO))
				if (NavigazioneServizi.isOggettoModificato(request)) {
					ServiziDelegate delegate = ServiziDelegate.getInstance(request);
					currentForm.setDetMov(delegate.getDettaglioMovimento(currentForm.getDetMov(), getLocale(request)));
				}

			// salvo il polo e
			// la biblioteca operante
			currentForm.setSalvaPolo(currentForm.getDetMov().getCodPolo());
			currentForm.setSalvaBibOperante(currentForm.getDetMov().getCodBibOperante());

			//prendo la descrBib erogante
			//almaviva5_20101206 #4027
			BibliotecaVO bib =  this.getBiblioteca(currentForm.getDetMov().getCodPolo(), currentForm.getDetMov().getCodBibOperante());
			if (bib != null)
				currentForm.getDetMov().setDescrBib(bib.getNom_biblioteca());

			//prendo la descrBib richiedente
			if (currentForm.getDetMov().getCodBibRichiedente() != null){
				BibliotecaVO bibRich =  this.getBiblioteca(currentForm.getDetMov().getCodPolo(), currentForm.getDetMov().getCodBibRichiedente());
				if (bibRich != null)
					currentForm.getDetMov().setDescrBibRichiedente(bibRich.getNom_biblioteca().trim());
			}

			if (currentForm.getDetMov().getCodBibInv() == null)
				currentForm.getDetMov().setCodBibInv("");

			if (currentForm.getDetMov().getCodSerieInv() == null)
				currentForm.getDetMov().setCodSerieInv("");

			if (currentForm.getDetMov().getCodInvenInv() == null)
				currentForm.getDetMov().setCodInvenInv("");


			//almaviva5_20100224
			String inventario = "";
			if (currentForm.getDetMov().isRichiestaSuInventario())
				inventario = currentForm.getDetMov().getCodBibInv() + " "
					+ currentForm.getDetMov().getCodSerieInv() + " "
					+ currentForm.getDetMov().getCodInvenInv();
			else
				inventario = currentForm.getDetMov().getCodBibDocLett() + " "
					+ currentForm.getDetMov().getTipoDocLett() + " "
					+ currentForm.getDetMov().getCodDocLet() + " "
					+ currentForm.getDetMov().getProgrEsempDocLet();

			currentForm.setInventario(inventario);
			//

			//prendo la descrBib fornitrice
			if (currentForm.getDetMov().getCodBibFornitrice() != null){
				BibliotecaVO bibRich =  this.getBiblioteca(currentForm.getDetMov().getCodPolo(), currentForm.getDetMov().getCodBibFornitrice());
				if (bibRich != null)
					currentForm.getDetMov().setDescrBibFornitrice(bibRich.getNom_biblioteca());
			}

			if (ValidazioneDati.isFilled(request.getParameter("cambiaSupportoDettMov")))
				return this.cambiaSupportoDettMov(mapping, form, request, response);

			if (ValidazioneDati.isFilled(request.getParameter("confermaServizio")))
				return this.confermaServizio(mapping, form, request, response);

			//almaviva5_20110207
			if (ValidazioneDati.equals(currentForm.getUpdateCombo(), "supporto")) {
				currentForm.setUpdateCombo(null);
				return this.cambiaSupportoDettMov(mapping, form, request, response);
			}

			if (ValidazioneDati.equals(currentForm.getUpdateCombo(), "cambio")) {
				currentForm.setUpdateCombo(null);
				return this.confermaServizio(mapping, form, request, response);
			}
			//

			currentForm.setConfermaNuovaRichiesta(false);

			if (currentForm.getDetMov().isPrenotazione() ) {
				navi.setTesto(".servizi.erogazione.DettaglioMovimento.PRENOT.testo");
				navi.setDescrizioneX(".servizi.erogazione.DettaglioMovimento.PRENOT.descrizione");
			}

			if (currentForm.getDetMov().getElemPerBlocchi() == 0)
				currentForm.getDetMov().setElemPerBlocchi(currentForm.getMaxRighe());

			currentForm.setMovimentoSalvato((MovimentoListaVO)currentForm.getDetMov().clone() );
			currentForm.setMovRicerca((MovimentoVO) request.getAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE));

			List<ServizioBibliotecaVO> listaServizi = (List<ServizioBibliotecaVO>)request.getAttribute(ServiziBaseAction.LISTA_SERVIZI_ATTR);
			this.impostaDatiServizi(currentForm, listaServizi, currentForm.getDetMov(), request);

			currentForm.setUtenteVO((UtenteBaseVO)request.getAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE));
			currentForm.setInfoDocumentoVO((InfoDocumentoVO)request.getAttribute(NavigazioneServizi.DATI_DOCUMENTO));


			if (currentForm.getInfoDocumentoVO() == null) {

				ActionMessages errors = new ActionMessages();
				MovimentoListaVO detMov = currentForm.getDetMov();
				InfoDocumentoVO infoDocumento = null;

				if (ValidazioneDati.strIsNull(detMov.getCodBibDocLett())) {
					// se non presente la biblioteca della segnatura
					// significa che siamo in presenza di un inventario
					infoDocumento = this.getInfoInventario(detMov
							.getCodPolo(), detMov.getCodBibInv(), detMov
							.getCodSerieInv(), new Integer(detMov.getCodInvenInv()),
							navi.getUserTicket(), this.getLocale(request, Constants.SBN_LOCALE), request);

					if (infoDocumento != null) {
						currentForm.setInfoDocumentoVO(infoDocumento);
					} else {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));
						this.saveErrors(request, errors);
						throw new ValidationException("Inventario non trovato in base dati");
					}
				} else {
					// siamo in presenza di una segnatura (collocazione)
					infoDocumento = this.getInfoSegnatura(request, detMov.getCodPolo(),
							detMov.getCodBibDocLett(),
							detMov.getTipoDocLett(),
							new Integer(detMov.getCodDocLet() ),
							new Integer(detMov.getProgrEsempDocLet()) );

					if (infoDocumento != null)
						currentForm.setInfoDocumentoVO(infoDocumento);
					else {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.datiSegnaturaNonTrovati"));
						this.saveErrors(request, errors);
						throw new ValidationException("Segnatura non trovata in base dati");
					}

				}

			}


			this.loadCollections(currentForm, request, currentForm.getDetMov());

			if (request.getAttribute(NavigazioneServizi.NUOVA_RICHIESTA) != null ) {

				//S20100603 se ho inserito una nuova richiesta devo
				//fare in modo che al ritorno sulla lista sia proposta la
				//situazione dell'utente
				NavigationElement prev = navi.getCache().getPreviousElement();
				if (prev != null && ServiziFormTypes.getFormType(prev.getForm()) == ServiziFormTypes.LISTA_MOVIMENTI) {
					ListaMovimentiForm listaMovForm = (ListaMovimentiForm) prev.getForm();
					listaMovForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
					MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) listaMovForm.getMovRicerca();
					if (movRicerca != null)
						movRicerca.clearDatiDocumento();
				}

				// nel caso di una nuova richiesta
				if (!currentForm.getUlt_supp().equals("S")) {
					// se non siamo in presenza di servizi che ammettono supporti
					// il numero pezzi è 0 e è modificabile
					currentForm.getDetMov().setNumPezzi("0");
					currentForm.setFlgNumPezzi(true);
				}
				else {
					// se siamo in presenza di servizi che ammettono supporti
					// il numero pezzi è 0 e è modificabile
					// (se però presenti gli intervalli copia
					// anche se presente, viene ricalcolato)
					currentForm.getDetMov().setNumPezzi("0");
					currentForm.setFlgNumPezzi(true);
				}

				// effettuo anche il calcolo del costo servizio
				// considerando il supporto
				// e la modalità di erogazione (associata al supporto o al servizio se non presente il supporto)
				SupportoBibliotecaVO supporto = this.getSupporto(currentForm.getTipiSupporto(), currentForm.getDetMov().getCodSupporto());
				TariffeModalitaErogazioneVO modalitaErogazione = this.getModalitaErogazione(currentForm.getModoErogazione(), currentForm.getDetMov().getCod_erog());
				this.validate(currentForm, request, supporto, modalitaErogazione);

				if (!ValidazioneDati.strIsNull(currentForm.getDetMov().getCodSupporto())) {
					// se presente il supporto
					// imposto l'intervallo copia quando non siamo nella visualizzaione
					impostaIntervalloCopia(request, currentForm.getDetMov());
				}

				// effettuo il calcolo del costo del servizio
				this.calcoloCostoServizio(currentForm, supporto, modalitaErogazione);

			} else {

				if (!ValidazioneDati.strIsNull(currentForm.getDetMov().getCodSupporto())) {
					// se presente il supporto
					// imposto l'intervallo copia quando dobbiamo visualizzare il movimento
					impostaIntervalloCopia(request, currentForm.getDetMov());
					// il numero pezzi
					// sarà modificabile
					// (se però presenti gli intervalli copia
					// anche se presente, viene ricalcolato)
					currentForm.setFlgNumPezzi(true);
				} else {
					// se non presente il supporto
					// il numero pezzi
					// sarà modificabile
					//currentForm.setFlgNumPezzi(true);

					//tck 3563 letura configurazione

					currentForm.setFlgNumPezzi(false);
					if (!ValidazioneDati.strIsNull(currentForm.getDetMov().getIntervalloCopia()))
					{
						// se l'intervallo copia è impostato
						currentForm.setFlgNumPezzi(true);
					}
				}

			}

			//almaviva5_20170928 salvataggio contestuale in caso di nuova richiesta ILL
			MovimentoListaVO mov = currentForm.getDetMov();
			if (mov != null && mov.isNuovo() && mov.isRichiestaILL())
				return ok(mapping, currentForm, request, response);

			return mapping.getInputForward();

		} catch (ValidationException e) {
			resetToken(request);
			return this.backForward(request, true);
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}

	public ActionForward inoltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			beforeUpdateChecks(mapping, request, currentForm, currentForm.getDetMov());
			MovimentoVO prenotazione = currentForm.getDetMov().copy();

			currentForm.setPrenotazione(prenotazione);
			MovimentoVO movimento = preparaInoltroPrenotazione(request, mapping, currentForm, prenotazione );
			if (movimento == null)
				return mapping.getInputForward();

			currentForm.setDetMov((MovimentoListaVO) movimento);
			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.INOLTRO_PRENOTAZIONE);

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

			saveToken(request);
			return mapping.getInputForward();

		} catch (ValidationException ve) {
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	private MovimentoVO preparaInoltroPrenotazione(HttpServletRequest request,
			ActionMapping mapping, DettaglioMovimentoForm currentForm,
			MovimentoVO prenotazione) throws ValidationException, Exception {

		MovimentoVO movimento = prenotazione.copy();

		//almaviva5_20100428
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		if (!movimento.isPeriodico() && delegate.esisteMovimentoAttivo(movimento)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.movimento.attivo.documento"));
			return null;
		}

		List<ControlloAttivitaServizio> listaAttivita =
			super.getListaAttivitaSuccessive(movimento.getCodPolo(), movimento.getCodBibOperante(), movimento.getCodTipoServ(), 0, null, request);

		if (!ValidazioneDati.isFilled(listaAttivita) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
			return null;
		}

		ControlloAttivitaServizio primoIter = super.primoPassoIter(listaAttivita);
		if (primoIter == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
			return null;
		}


		//almaviva5_20100427 la data inizio prevista all'atto della pronotazione
		//deve essere conservata se maggiore della data di inoltro (now)
		Timestamp now = DaoManager.now();
		Timestamp dataInizioPrev = movimento.getDataInizioPrev();
		movimento.setDataInizioPrev(dataInizioPrev.after(now) ? dataInizioPrev : now);

		movimento.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
		IterServizioVO passoIter = primoIter.getPassoIter();
		movimento.setCodStatoMov(passoIter.getCodStatoMov());
		movimento.setCodStatoRic(passoIter.getCodStatoRich());
		movimento.setCodAttivita(passoIter.getCodAttivita());
		movimento.setCodTipoServ(passoIter.getCodTipoServ());
		movimento.setProgrIter(passoIter.getProgrIter().toString());

		//almaviva5_20100112 decodifica stati (solo per MovimentoListaVO)
		if (movimento instanceof MovimentoListaVO) {
			MovimentoListaVO dettaglio = (MovimentoListaVO) movimento;
			dettaglio.decode();
		}

		ServizioBibliotecaVO servizio = getServizio(movimento.getCodPolo(),
				movimento.getCodBibOperante(), movimento.getCodTipoServ(),
				movimento.getCodServ(), request);

		if (movimento.isWithPrenotazionePosto())
			movimento.setDataFinePrev(movimento.getPrenotazionePosto().getTs_fine());
		else
			movimento.setDataFinePrev(ServiziUtil.calcolaDataFinePrevista(servizio, movimento.getDataInizioPrev()));

		return movimento;
	}


	private void inoltraPrenotazione(HttpServletRequest request,
			ActionMapping mapping, ActionForm form, MovimentoVO movimento)
			throws ValidationException, Exception {


		List<ControlloAttivitaServizio> listaAttivita =
			super.getListaAttivitaSuccessive(movimento.getCodPolo(), movimento.getCodBibOperante(), movimento.getCodTipoServ(), 0, null, request);

		if (!ValidazioneDati.isFilled(listaAttivita) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
			return;
		}

		ControlloAttivitaServizio primoIter = super.primoPassoIter(listaAttivita);
		if (primoIter == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
			return;
		}

		/*
		//almaviva5_20100427 la data inizio prevista all'atto della pronotazione
		//deve essere conservata se maggiore della data di inoltro (now)
		Timestamp now = DaoManager.now();
		Timestamp dataInizioPrev = movimento.getDataInizioPrev();
		movimento.setDataInizioPrev(dataInizioPrev.after(now) ? dataInizioPrev : now);
		*/

		ControlloAttivitaServizio controlliBase = ControlloAttivitaServizio.getControlliBase();
		DatiControlloVO datiControllo = super.eseguiControlli(request, movimento, controlliBase, getOperatore(request), true, null);
		ControlloAttivitaServizioResult checkControlliBase = datiControllo.getResult();

		List<String> msgSupplementari = datiControllo.getCodiciMsgSupplementari();
		if (ValidazioneDati.isFilled(msgSupplementari) ) {
			//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
			this.addErrors(request, msgSupplementari);
			msgSupplementari.clear();
		}

		switch (checkControlliBase) {
		case OK:
		case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
		case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
			break;

		case OK_NON_ANCORA_DISPONIBILE:
			ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
			Timestamp tsRitiro = disponibilitaVO.getDataPrenotazione();
			LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(), tsRitiro));
			Timestamp tsInizioPrev = movimento.getDataInizioPrev();
			movimento.setDataInizioPrev(tsRitiro.after(tsInizioPrev) ? tsRitiro : tsInizioPrev);
			break;

		case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
		case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
			checkControlliBase = ControlloAttivitaServizioResult.ERRORE_INOLTRO_PRENOTAZIONE_IMPOSSIBILE;
			LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
			throw new ValidationException("errore");

		case ERRORE_DOCUMENTO_NON_DISPONIBILE:
		case ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL:
			String codNoDisp = datiControllo.getCodNoDisp();
			String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
			LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(), descr, datiControllo.getDataRedisp()));
			return;

		default:
			LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
			throw new ValidationException("errore");
		}

		// controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio
		datiControllo = super.eseguiControlli(request, movimento, primoIter, getOperatore(request), true, checkControlliBase);
		if (datiControllo.getResult() != ControlloAttivitaServizioResult.OK) {
			LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
			throw new ValidationException("Errore durante creazione nuova richiesta");
		}

		movimento.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
		movimento.setCodStatoMov(primoIter.getPassoIter().getCodStatoMov());
		movimento.setCodStatoRic(primoIter.getPassoIter().getCodStatoRich());
		movimento.setCodAttivita(primoIter.getPassoIter().getCodAttivita());
		movimento.setCodTipoServ(primoIter.getPassoIter().getCodTipoServ());
		movimento.setProgrIter(primoIter.getPassoIter().getProgrIter().toString());

		/*
		Calendar fineMov = Calendar.getInstance();
		fineMov.setTimeInMillis(movimento.getDataInizioPrev().getTime());
		fineMov.add(Calendar.DATE, servizio.getDurMov());
		movimento.setDataFinePrev(new Timestamp(fineMov.getTimeInMillis()));
		*/

		ServizioBibliotecaVO servizio = getServizio(movimento.getCodPolo(),
				movimento.getCodBibOperante(), movimento.getCodTipoServ(),
				movimento.getCodServ(), request);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		delegate.aggiornaRichiesta(movimento, servizio.getIdServizio());

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		currentForm.setDetMov((MovimentoListaVO) movimento);
		currentForm.setMovimentoSalvato((MovimentoListaVO) movimento.copy());

		//tutto ok
		Navigation navi = Navigation.getInstance(request);
		navi.setTesto(".servizi.erogazione.DettaglioMovimento.testo");
		navi.setDescrizioneX(".servizi.erogazione.DettaglioMovimento.descrizione");
		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceRichiestaEffettuata"));

		checkStampa(currentForm, (MovimentoListaVO)movimento, primoIter.getPassoIter());
	}


	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		// acquisisco il numero copie e il testo della stampa modulo
		// dalla configurazione dell'attività

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		List<IterServizioVO> listaIterServizio = this.getIterServizio(currentForm.getDetMov().getCodPolo(),
				currentForm.getDetMov().getCodBibOperante(), currentForm.getDetMov().getCodTipoServ(), request);

		Iterator<IterServizioVO> iterator = listaIterServizio.iterator();
		IterServizioVO iterServizioVO = null;

		while (iterator.hasNext()) {
			iterServizioVO = iterator.next();
			if (iterServizioVO.getCodAttivita().equals(currentForm.getDetMov().getCodAttivita()))
				break;
		}

		ControlloAttivitaServizio attivita = delegate.getAttivita(currentForm.getDetMov().getCodPolo(), iterServizioVO);

		// Imposto il numero copie della stampa modulo richiesta
		if (String.valueOf(attivita.getPassoIter().getNumPag()).equals("0"))
			currentForm.getDetMov().setNumeroCopieStampaModulo("1");
		else
			currentForm.getDetMov().setNumeroCopieStampaModulo(String.valueOf(attivita.getPassoIter().getNumPag()));

		// Imposto il testo della stampa modulo richiesta
		currentForm.getDetMov().setTestoStampaModulo(attivita.getPassoIter().getTesto());

		request.setAttribute("codBib", currentForm.getBiblioteca());
		request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_RICHIESTA);
		request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getDetMov());
		return  mapping.findForward("stampaRichiesta");
	}


	public ActionForward prenotazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();
		request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO,
				currentForm.getDetMov().copy() );

		return mapping.findForward("prenotazioni");
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		int pos = currentForm.getPosizioneCorrente();
		if (pos == 0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
			resetToken(request);
			return mapping.getInputForward();
		}

		currentForm.setPosizioneCorrente(--pos);
		List<MovimentoListaVO> listaMovimenti = currentForm.getListaMovimenti();
		if (ValidazioneDati.isFilled(listaMovimenti) ) {
			//recupero il movimento alla posizione selezionata per scorrimento
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			MovimentoListaVO movimentoSelezionato = listaMovimenti.get(pos);
			movimentoSelezionato = delegate.getDettaglioMovimento(movimentoSelezionato, getLocale(request) );
			movimentoSelezionato = preparaMovimentoPerEsame(request, movimentoSelezionato, null, null, null);
			listaMovimenti.set(pos, movimentoSelezionato);
		}

		return unspecified(mapping, currentForm, request, response);
	}

	public ActionForward avanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		List<MovimentoListaVO> listaMovimenti = currentForm.getListaMovimenti();
		int pos = currentForm.getPosizioneCorrente();
		if (pos == (ValidazioneDati.size(listaMovimenti) - 1) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.fineScorrimento"));
			resetToken(request);
			return mapping.getInputForward();
		}

		currentForm.setPosizioneCorrente(++pos);
		if (ValidazioneDati.isFilled(listaMovimenti) ) {
			//recupero il movimento alla posizione selezionata per scorrimento
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			MovimentoListaVO movimentoSelezionato = listaMovimenti.get(pos);
			movimentoSelezionato = delegate.getDettaglioMovimento(movimentoSelezionato, getLocale(request) );
			movimentoSelezionato = preparaMovimentoPerEsame(request, movimentoSelezionato, null, null, null);
			listaMovimenti.set(pos, movimentoSelezionato);
		}

		return unspecified(mapping, currentForm, request, response);
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		ActionMessages errors = new ActionMessages();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			resetToken(request);

			//cambio servizio
			if (currentForm.isCambioServizio())
				return elaboraCambioServizio(mapping, request, currentForm, errors);

			// aggiorna DB
			MovimentoListaVO detMov = currentForm.getDetMov();
			if (!detMov.equals(currentForm.getMovimentoSalvato())) {
				beforeUpdateChecks(mapping, request, currentForm, detMov);

				currentForm.setConferma(true);
				currentForm.setDetMovConferma((MovimentoListaVO) detMov.clone());
				currentForm.setRichiesta(RichiestaDettaglioMovimentoType.OK);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();

			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.noAggiornaNoVariazioni"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

		} catch (ValidationException e) {
			resetToken(request);
			if (e.getErrorCode().equals(SbnErrorTypes.SRV_ERRORE_BLOCCANTE_CAMBIO_SERVIZIO))
				currentForm.setErroreBloccante(true);

			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.findForward("annulla");
		}
	}


	private void beforeUpdateChecks(ActionMapping mapping,
			HttpServletRequest request, DettaglioMovimentoForm currentForm, MovimentoListaVO detMov)
			throws ValidationException, ParseException, RemoteException,
			CreateException, Exception {

		//C'é stata qualche modifica, aggiorno il movimento sul DB
		SupportoBibliotecaVO supporto = getSupporto(currentForm.getTipiSupporto(), detMov.getCodSupporto());
		TariffeModalitaErogazioneVO modalitaErogazione = getModalitaErogazione(currentForm.getModoErogazione(), detMov.getCod_erog());

		boolean isCancellaRifiuta = ValidazioneDati.in(currentForm.getRichiesta(),
				RichiestaDettaglioMovimentoType.RIFIUTA,
				RichiestaDettaglioMovimentoType.CANCELLA);

		if (modalitaErogazione == null && !isCancellaRifiuta) {
			if (ValidazioneDati.equals(currentForm.getUlt_supp(), "S")) //riproduzione?
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonAssociateAlSupporto"));
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonAssociateAlServizio"));

			throw new ValidationException("Errore creazione nuova richiesta");
		}

		if (!isCancellaRifiuta)
			validate(currentForm, request, supporto, modalitaErogazione);

		impostaIntervalloCopia(request, detMov);

		// effettuo il calcolo del costo del servizio
		if (!isCancellaRifiuta)
			this.calcoloCostoServizio(currentForm, supporto, modalitaErogazione);


		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		List<IterServizioVO> listaIter =
			this.getIterServizio(detMov.getCodPolo(), detMov.getCodBibOperante(), detMov.getCodTipoServ(), request);

		Iterator<IterServizioVO> i = listaIter.iterator();
		IterServizioVO iterServizioVO = null;

		boolean found = false;
		while (i.hasNext()) {
			iterServizioVO = i.next();
			if (iterServizioVO.getCodAttivita().equals(detMov.getCodAttivita())) {
				found = true;
				break;
			}
		}

		if (!found) {
			//errore: iter non trovato
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.nuovaRichiesta.passoIterNonPresente"));
			throw new ValidationException("Messaggi bloccanti: aggiornamento non effettuato");
		}

		ControlloAttivitaServizio attivita = delegate.getAttivita(detMov.getCodPolo(), iterServizioVO);

		List<String> messaggi = new ArrayList<String>();
		DatiControlloVO dati = new DatiControlloVO(
				navi.getUserTicket(),
				detMov,
				getOperatore(request),
				false, null);


		//Esecuzione controlli associati all'iter
		boolean checkIter = false;
		checkIter = attivita.controlloIter(dati, messaggi, true, false);
		if (ValidazioneDati.isFilled(messaggi) ) {
			String[] msg = new String[messaggi.size()];
			messaggi.toArray(msg);
			this.saveErrors(request, msg);
		}

		if (!checkIter) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreBloccanteAggiornamentoOperazioneKo"));
			throw new ValidationException("Messaggi bloccanti: aggiornamento non effettuato");
		}

	}


	private ActionForward elaboraCambioServizio(ActionMapping mapping,
			HttpServletRequest request, DettaglioMovimentoForm currentForm,
			ActionMessages errors) throws ValidationException, Exception {

		if (ValidazioneDati.strIsNull(currentForm.getCodServNuovaRich()) ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.servizioAssente"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore creazione nuova richiesta");
		}

		if (ValidazioneDati.strIsNull(currentForm.getProgrIter()) ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliIter"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore creazione nuova richiesta");
		}

		IterServizioVO iter = this.getIterScelto(currentForm.getLstIter(), currentForm.getProgrIter());

		if (!currentForm.isConfermaNuovaRichiesta() ) {
			// se è la prima volta che confermo la richiesta
			if (!iter.getCodStatoMov().equals(MovimentoVO.STATO_MOVIMENTO_ATTIVO)) {
				//si sta scegliendo un passo dell'iter con stato a chiuso
				//si chiede conferma
				currentForm.setConferma(true);
				currentForm.setRichiesta(RichiestaDettaglioMovimentoType.CAMBIO_SERVIZIO);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.statoMovimentoChiuso.conferma"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			}
		}

		if (!currentForm.isConfermaNuovaRichiesta() )
			// se è la prima volta che confermo la richiesta
			currentForm.setConfermaNuovaRichiesta(true);
		else {
			// se ho già confermato la richiesta
			// ripristino a false il relativo flag
			currentForm.setConfermaNuovaRichiesta(false);
			// e ripristino il cambio servizio
			currentForm.setCambioServizio(false);
		}

		Navigation navi = Navigation.getInstance(request);
		this.nuovaRichiesta(currentForm, request, (MovimentoVO) navi.getAttribute(NavigazioneServizi.CAMBIO_SERVIZIO) );

		//almaviva5_20121217 #4057 ricarico passi iter per nuovo servizio
		MovimentoListaVO mov = currentForm.getDetMov();
		this.loadAttivitaSuccessive(currentForm, mov.getCodPolo(), mov
				.getCodBibOperante(), mov.getCodTipoServ(), new Integer(mov.getProgrIter()).intValue(), mov.getDatiILL(), request);
		this.impostaDatiServizi(currentForm, null, mov, request);

		return mapping.getInputForward();
	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;

		request.setAttribute(NavigazioneServizi.CHIUDI, NavigazioneServizi.CHIUDI_DETTAGLIO_MOVIMENTO);

		MovimentoListaVO mov = currentForm.getDetMov();
		if (currentForm.isCambioServizio()) {
			currentForm.setConfermaNuovaRichiesta(false);
			currentForm.setCambioServizio(false);
			currentForm.setErroreBloccante(false);
			currentForm.setCodServNuovaRich(null);
			currentForm.setLstIter(null);
			currentForm.setProgrIter(null);

			Navigation navi = Navigation.getInstance(request);
			currentForm.setDetMov((MovimentoListaVO) navi.getAttribute(NavigazioneServizi.CAMBIO_SERVIZIO));

			//almaviva5_20101217 #4063
			currentForm.setFlgErrNuovaRichiesta(false);
			currentForm.setMovCambioServizio(null);
			loadCollections(currentForm, request, mov);

			return mapping.getInputForward();
		}

		//almaviva5_20101217 #4074
		if (!mov.isNuovo() && mov.getTsVar().after(currentForm.getCreationTime())) //modificato
			request.setAttribute(NavigazioneServizi.OGGETTO_MODIFICATO, true);

		return this.backForward(request, false); //mapping.findForward("annulla");
	}


	public ActionForward cancella(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			MovimentoListaVO mov = currentForm.getDetMov();
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.CANCELLA);
			beforeUpdateChecks(mapping, request, currentForm, mov);

			currentForm.setDetMovConferma((MovimentoListaVO) mov.clone());
			currentForm.setConferma(true);

			//campi da visualizzare
			mov.setCodStatoMov("E");
			mov.setCodStatoRic("D");
			mov.decode();

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);

			//almaviva5_20160118 servizi ILL
			if (mov.isRichiestaILL()) {
				MessaggioVO msg = new MessaggioVO(mov.getDatiILL());
				msg.setDataMessaggio(DaoManager.now());
				msg.setStato(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode());
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward rifiuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			MovimentoListaVO mov = currentForm.getDetMov();
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.RIFIUTA);
			beforeUpdateChecks(mapping, request, currentForm, mov);
			currentForm.setDetMovConferma((MovimentoListaVO) mov.clone());

			currentForm.setConferma(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);

			//almaviva5_20160118 servizi ILL
			//se la richiesta è già stata condivisa col server ILL questo va notificato
			DatiRichiestaILLVO datiILL = mov.getDatiILL();
			if (mov.isRichiestaILL() && datiILL.getTransactionId() > 0) {
				String stato = null;
				switch (datiILL.getRuolo()) {
				case RICHIEDENTE:
					stato = StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO.getISOCode();
					mov.setCodAttivitaSucc(stato);
					break;

				case FORNITRICE:
					if (StatoIterRichiesta.of(datiILL.getCurrentState()) == StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO) {
						stato = StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO.getISOCode();
					} else
						stato = StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode();

					mov.setCodStatoMov("C");
					mov.setCodStatoRic("B");
					mov.setCodAttivitaSucc(stato);
				}

				MessaggioVO msg = new MessaggioVO(datiILL);
				msg.setDataMessaggio(DaoManager.now());
				msg.setStato(stato);
				currentForm.setMessaggio(msg);
			} else {
				// mov locale
				mov.setCodStatoMov("C");
				mov.setCodStatoRic("B");
			}

			mov.decode();

			return mapping.getInputForward();

		} catch (ValidationException e) {
			resetToken(request);
			log.error("", e);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);

			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);

			return no(mapping, currentForm, request, response);
		}
	}


	public ActionForward rinnova(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			/*
			if (!currentForm.getDetMov().equals(currentForm.getMovimentoSalvato())) {
				//C'é stata qualche modifica
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.salvaPrima"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
			*/
			MovimentoListaVO mov = currentForm.getDetMov();
			beforeUpdateChecks(mapping, request, currentForm, mov);

			Date dataProroga = mov.getDataProroga();
			if (dataProroga == null) { //|| dataProroga.equals(movimentoSalvato.getDataProroga())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.rinnovo.dataProrogaNonInserita"));

				resetToken(request);
				return mapping.getInputForward();
			}

			//almaviva5_20161205 servizi ill

			if (mov.isRichiestaILL()) {
				//il rinnovo di una richiesta ill si configura come un avanzamento allo stato F115 (se richiedente) o F129 (fornitrice)
				StatoIterRichiesta stato = mov.isRichiedenteRichiestaILL()
						? StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO
						: StatoIterRichiesta.F129_CONFERMA_RINNOVO;

				ControlloAttivitaServizio attivita = Stream.of(currentForm.getListaAttivitaSucc())
						.filter(Servizi.ControlloAttivitaServizio.codAttivitaEquals(stato.getISOCode()))
						.single();
				if (attivita != null) {
					checkDatiRinnovo(currentForm, request);
					mov.setCodAttivitaSucc(attivita.getPassoIter().getCodAttivita());
					return avanza(mapping, currentForm, request, response);
				}
				return mapping.getInputForward();
			}

			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.RINNOVA);
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);
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
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		MovimentoListaVO mov = currentForm.getDetMov();
		return ServiziDelegate.getInstance(request).sifDettaglioUtente(null, null, null, mov.getIdUtenteBib());
	}

	public ActionForward documento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		MovimentoListaVO mov = currentForm.getDetMov();
		if (mov.isRichiestaSuSegnatura())
			return ServiziDelegate.getInstance(request).sifDettaglioDocumentoNonSBN(mov.getIdDocumento());

		if (mov.isRichiestaSuInventario())
			return ServiziDelegate.getInstance(request).sifEsameInventario(mov);

		return mapping.getInputForward();
	}

	public ActionForward rifiutaProroga(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			//almaviva5_20100421 #3463
			MovimentoListaVO detMov = currentForm.getDetMov();
			beforeUpdateChecks(mapping, request, currentForm, detMov);
			currentForm.setDetMovConferma((MovimentoListaVO) detMov.clone());
			detMov.setCodStatoRic("S"); //proroga rifiutata
			detMov.decode();

			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.RIFIUTA_PROROGA);
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			Navigation navi = Navigation.getInstance(request);
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			RichiestaDettaglioMovimentoType richiesta = currentForm.getRichiesta();
			switch (richiesta) {
			case OK:
				eseguiSalvataggioMovimento(request, currentForm, true);
				break;

			case CAMBIO_SERVIZIO:
				// ho già confermato la richiesta
				// ripristino a false il relativo flag
				currentForm.setConfermaNuovaRichiesta(true);
				this.nuovaRichiesta(currentForm, request, null);
				break;

			case CANCELLA:
				eseguiSalvataggioMovimento(request, currentForm, false);
				this.cancMov(new Long[]{currentForm.getDetMov().getIdRichiesta()}, navi.getUtente().getFirmaUtente(), request);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
				//almaviva5_20151014 gestione priorità prenotazioni
				invioMailUtentiPrenotati(request, currentForm.getDetMov());
				return annulla(mapping, currentForm, request, response);

			case RIFIUTA: {
				MovimentoListaVO mov = currentForm.getDetMov();
				if (mov.isRichiestaILL()) {
					//almaviva5_20160204 servizi ILL
					DatiRichiestaILLVO dati = mov.getDatiILL();
					dati.addUltimoMessaggio(currentForm.getMessaggio());
					dati = ServiziILLDelegate.getInstance(request).rifiutaRichiestaILL(dati);

					if (StatoIterRichiesta.of(dati.getCurrentState()) == StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO)
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.propostaAnullamentoInviata"));
					else
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.richiestaRespinta"));

				} else {
					//richiesta locale
					eseguiSalvataggioMovimento(request, currentForm, false);
					rifiutaMov(new Long[] { mov.getIdRichiesta() }, navi.getUtente().getFirmaUtente(), request);
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.richiestaRespinta"));
				}
				// rileggo il movimento rifiutato (dopo le modifiche apportate)
				currentForm.setDetMov(delegate.getDettaglioMovimento(currentForm.getDetMov(), getLocale(request)));
				// e lo salvo in movimentoSalvato
				currentForm.setMovimentoSalvato((MovimentoListaVO)currentForm.getDetMov().clone());

				//almaviva5_20151014 gestione priorità prenotazioni
				invioMailUtentiPrenotati(request, currentForm.getDetMov());

				if (ValidazioneDati.equalsIgnoreCase(currentForm.getVengoDa(), "ErogazionePrenotazioni")) {
					request.setAttribute(NavigazioneServizi.PRENOTAZIONE_RIFIUTATA, new Boolean(true));
					return this.backForward(request, false);
				}

				mov = currentForm.getDetMov();
				this.loadAttivitaSuccessive(currentForm, mov.getCodPolo(), mov.getCodBibOperante(),
						mov.getCodTipoServ(), Integer.parseInt(mov.getProgrIter()),
						mov.getDatiILL(), request);
				break;
			}

			case RIFIUTA_PROROGA:
				//almaviva5_20100421 #3463
				eseguiSalvataggioMovimento(request, currentForm, false);
				// rileggo il movimento rifiutato (dopo le modifiche apportate)
				currentForm.setDetMov(delegate.getDettaglioMovimento(currentForm.getDetMov(), getLocale(request)));
				// e lo salvo in movimentoSalvato
				currentForm.setMovimentoSalvato((MovimentoListaVO)currentForm.getDetMov().clone());
				break;

			case AVANZA: {
				MovimentoListaVO movDaAvanzare = currentForm.getDetMov();
				String codAttivitaSucc = movDaAvanzare.getCodAttivitaSucc();
				boolean locale = !StatoIterRichiesta.of(codAttivitaSucc).isILL();
				if (locale) {
					//l'attivita e progr. iter su movimento locale va aggiornato solo se il passo è locale
					movDaAvanzare.setCodAttivita(codAttivitaSucc);
				}

				//almaviva5_20151208 servizi ILL
				ControlloAttivitaServizio iter = currentForm.getAttivitaPendente();
				DatiControlloVO postControlli = this.eseguiPostControlli(request, movDaAvanzare, iter, getOperatore(request), false, null, currentForm.getMessaggio());
				if (!ControlloAttivitaServizioResult.isOK(postControlli.getResult()) )
					throw new ValidationException(SbnErrorTypes.SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO);

				MovimentoListaVO mov = (MovimentoListaVO) postControlli.getMovimentoAggiornato();
				currentForm.setDetMov(mov);
				mov.decode();
				eseguiSalvataggioMovimento(request, currentForm, false);
				currentForm.setDetMovConferma(null);
				currentForm.setMessaggio(null);

				//Aggiorno la lista di attività successive alla luce della nuova attività del movimento
				this.loadAttivitaSuccessive(currentForm, currentForm.getDetMov().getCodPolo(), currentForm.getDetMov().getCodBibOperante(),
						currentForm.getDetMov().getCodTipoServ(), Integer.parseInt(currentForm.getDetMov().getProgrIter()),
						currentForm.getDetMov().getDatiILL(), request);

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAvanzamentoEffettuato"));

				//invio email a utenti prenotati in caso di restituzione
				invioMailUtentiPrenotati(request, movDaAvanzare);

				//il controllo sospensione va eseguito prima
				//dell'eventuale stampa modulo
				ActionForward forward = null;
				if ((forward = eseguiSospensione(mapping, request, currentForm)) != null)
					return forward;

				if (currentForm.isStampaModulo())
					return preparaStampaModuloAvanza(mapping, request, currentForm);

				break;
			}

			case STAMPA_MODULO_RICHIESTA:

				// se è richiesta la stampa modulo
				// richiamo il form della stampa richiesta
				// passando i campi già predisposti nel metodo
				// eseguiAvanzaMovimento

				// imposto anche tutti i campi che saranno successivamente,
				// se confermata la richiesta di avanzamento, utilizzati
				// per la stampa del modulo
				request.setAttribute("codBib", currentForm.getBiblioteca());
				request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_RICHIESTA);
				request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getDetMov());

				// ripulisco il campo checkIter
				currentForm.setStampaModulo(false);
				// ripristino il campo della conferma
				currentForm.setConferma(false);
				return  mapping.findForward("stampaRichiesta");

			//GESTIONE RINNOVO
			case RINNOVA:
				checkDatiRinnovo(currentForm, request);

				boolean esistePrenotazione = this.esistePrenotazione(currentForm.getDetMov(), request);
				if (esistePrenotazione) {
					currentForm.setConferma(true);
					currentForm.setRichiesta(RichiestaDettaglioMovimentoType.RINNOVO_CON_PRENOTAZIONE);
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.rinnovo.esistePrenotazione"));
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					saveToken(request);
					return mapping.getInputForward();
				} else
					return this.eseguiRinnovo(mapping, currentForm, request);


			case RINNOVO_CON_PRENOTAZIONE:
				currentForm.setEsistonoPrenotazioni(true);
				return this.eseguiRinnovo(mapping, currentForm, request);

			case PROROGA_SUCCESSIVA_DATA_MASSIMA:
				this.aggiornaMovimentoPerProroga(currentForm, request);
				if (currentForm.isEsistonoPrenotazioni()) {
					// se ci sono prenotazioni
					// imposto la data inizio e data fine prevista della prenotazione con la data proroga
					this.aggiornaPrenotazioniDopoRinnovo(currentForm, request);
				}
				break;

			case SOSPENDI:
				this.sospendiUtente(request, currentForm);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.sospensioneUtenteEffettuato"));
				if (currentForm.isStampaModulo())
					return preparaStampaModuloAvanza(mapping, request, currentForm);

				break;

			case INOLTRO_PRENOTAZIONE:
				MovimentoVO prenotazione = currentForm.getDetMov().copy();
				this.inoltraPrenotazione(request, mapping, currentForm, prenotazione);
				if (currentForm.isStampaModulo())
					return preparaStampaModuloAvanza(mapping, request, currentForm);
				break;

			case INOLTRO_ALTRA_BIB_FORNITRICE: {
				MovimentoListaVO detMov = currentForm.getDetMov();
				DatiRichiestaILLVO dati = detMov.getDatiILL();
				dati.addUltimoMessaggio(currentForm.getMessaggio());
				BibliotecaVO bibForn = BibliotecaVO.search(currentForm.getBibSelezionata(), dati.getBibliotecheFornitrici());

				MovimentoVO mov = ServiziILLDelegate.getInstance(request).inoltraRichiestaAdAltraBiblioteca(detMov, bibForn);
				detMov = delegate.getDettaglioMovimento(mov, Locale.getDefault());
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.mov.ill.inoltro.altra.biblioteca",
						mov.getIdRichiesta(), detMov.getDenominazioneBibFornitrice()));

				//Aggiorno la lista di attività successive alla luce della nuova attività del movimento
				this.loadAttivitaSuccessive(currentForm, detMov.getCodPolo(), detMov.getCodBibOperante(),
						detMov.getCodTipoServ(), Integer.parseInt(detMov.getProgrIter()),
						detMov.getDatiILL(), request);

				detMov.decode();
				currentForm.setDetMov(detMov);
				currentForm.setMovimentoSalvato((MovimentoListaVO)detMov.clone());
				break;
			}

			default:
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.richiestaOperazioneInvalida"));
				log.error("operazione non valida: " + richiesta);
				return mapping.getInputForward();
			}

			ActionForward forward = null;
			if ((forward = eseguiSospensione(mapping, request, currentForm)) != null)
				return forward;

			currentForm.setConferma(false);
			currentForm.setDetMovConferma(null);
			currentForm.setMessaggio(null);
			currentForm.setRichiesta(null);

			return mapping.getInputForward();

		} catch (ApplicationException e) {
			currentForm.setConferma(false);
			resetToken(request);
			log.error("", e);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				this.setErroreGenerico(request, e);

			return no(mapping, currentForm, request, response);

		} catch (ValidationException e) {
			currentForm.setConferma(false);
			resetToken(request);
			SbnErrorTypes error = e.getErrorCode();
			if (error == SbnErrorTypes.SRV_ERRORE_BLOCCANTE_CAMBIO_SERVIZIO)
				currentForm.setErroreBloccante(true);
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);

			//return mapping.getInputForward();
			return no(mapping, currentForm, request, response);
		} catch (Exception e) {
			currentForm.setConferma(false);
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			//return mapping.getInputForward();
			return no(mapping, currentForm, request, response);
		}
	}


	private ActionForward eseguiSospensione(ActionMapping mapping,
			HttpServletRequest request, ActionForm form) {

		 DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		//GESTIONE EVENTUALE SOSPENSIONE UTENTE
		if (currentForm.getData_sosp() == null)
			return null;

		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.servizi.sospendiLettore",
				currentForm.getGg_rit(),
				currentForm.getGg_sosp()));
		currentForm.setConferma(true);
		currentForm.setRichiesta(RichiestaDettaglioMovimentoType.SOSPENDI);
		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
		saveToken(request);

		return mapping.getInputForward();

	}


	private ActionForward preparaStampaModuloAvanza(ActionMapping mapping,
			HttpServletRequest request, ActionForm form) {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;

		currentForm.setRichiesta(RichiestaDettaglioMovimentoType.STAMPA_MODULO_RICHIESTA);
		currentForm.setConferma(true);
		// imposto il messaggio per confermare la stampa del modulo
		// con il numero di copie impostate in configurazione
		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneStampaModulo",
				currentForm.getDetMov().getNumeroCopieStampaModulo()));

		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
		saveToken(request);

		//return mapping.getInputForward();

		request.setAttribute("codBib", currentForm.getBiblioteca());
		request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_RICHIESTA_AVANZA);
		request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getDetMov());

		// ripulisco il campo checkIter
		currentForm.setStampaModulo(false);
		// ripristino il campo della conferma
		currentForm.setConferma(false);
		return  mapping.findForward("stampaRichiesta");
	}


	private void eseguiSalvataggioMovimento(HttpServletRequest request,
			ActionForm form, boolean msg) throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		// mi salvo le note utente
		MovimentoListaVO mov = currentForm.getDetMov().copy();

		aggiornaMov(currentForm, request, mov);

		currentForm.setDetMov(mov);
		currentForm.setMovimentoSalvato((MovimentoListaVO)mov.clone());
		if (msg)
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			RichiestaDettaglioMovimentoType richiesta = currentForm.getRichiesta();
			switch (richiesta) {
			case AVANZA:
			case INOLTRO_ALTRA_BIB_FORNITRICE:
				currentForm.setDetMov(currentForm.getDetMovConferma() );
				currentForm.setDetMovConferma(null);
				break;

			case INOLTRO_PRENOTAZIONE:
				currentForm.setDetMov((MovimentoListaVO) currentForm.getPrenotazione() );
				currentForm.setPrenotazione(null);
				break;

			case STAMPA_MODULO_RICHIESTA:
				break;

			case OK:
			case CANCELLA:
			case RIFIUTA:
			case RIFIUTA_PROROGA:
				currentForm.setDetMov(currentForm.getDetMovConferma() );
				currentForm.setDetMovConferma(null);
				break;

			case SOSPENDI:
				currentForm.setData_sosp(null);
				if (currentForm.isStampaModulo())
					return preparaStampaModuloAvanza(mapping, request, currentForm);
				break;

			default:
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.richiestaOperazioneInvalida"));
				log.error("operazione non valida: " + richiesta);
				return mapping.getInputForward();
			}

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		} finally {
			currentForm.setConferma(false);
			currentForm.setData_sosp(null);
			currentForm.setDataProrogaMassima(null);
			currentForm.setNumeroRinnovo((short)0);
			currentForm.setRichiesta(null);
			currentForm.setAttivitaPendente(null);
			currentForm.setMessaggio(null);
		}
	}


	public ActionForward cambiaServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			// carico la lista dei servizi comuni
			// tra documento e utente
			this.caricaServiziDisponibiliUtenteDocumento(currentForm, request);

			if (currentForm.getLstCodiciServizio().size() == 0 ||
			    (currentForm.getLstCodiciServizio().size() == 1 &&
				 currentForm.getLstCodiciServizio().get(0).getCodTipoServ()==null &&
				 currentForm.getLstCodiciServizio().get(0).getCodServ()==null)) {
				// se è presente solo un elemento e questo è
				// quello che contiene i valori a null
				// imposto tutto l'array a null
				currentForm.setLstCodiciServizio(null);
			}
			if (currentForm.getInfoDocumentoVO() != null &&
			    (currentForm.getInfoDocumentoVO().getSegnatura() == null ||
				 currentForm.getInfoDocumentoVO().getSegnatura().trim().equals(""))) {
				// se non è presente la collocazione
				// imposto tutto l'array a null
				// perchè non ci possono essere servizi disponibili
				// per un documento che non ha collocazione
				currentForm.setLstCodiciServizio(null);
			}

			if (currentForm.getLstCodiciServizio() != null) {
				// elimino il servizio già utilizzato nel movimento
				List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
				Iterator<ServizioBibliotecaVO> iterator = currentForm.getLstCodiciServizio().iterator();
				while (iterator.hasNext()) {
					ServizioBibliotecaVO servizioVO = iterator.next();
					if (servizioVO.getCodTipoServ() != null &&
							servizioVO.getCodTipoServ().equals(currentForm.getDetMov().getCodTipoServ())) {
						// non faccio niente
					} else {
						//riporto il servizio sul VO di appoggio
						lstServizi.add(servizioVO);
					}
				}
				currentForm.setLstCodiciServizio(lstServizi);

			}

			//TODO solo servizi locali
			List<ServizioBibliotecaVO> listaServizi = currentForm.getLstCodiciServizio();
			if (ValidazioneDati.isFilled(listaServizi)) {
				listaServizi = Stream.of(listaServizi).filter(new Predicate<ServizioBibliotecaVO>() {
					public boolean test(ServizioBibliotecaVO srv) {
						try {
							CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(srv.getCodTipoServ(),
									CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
							return ts == null || ts.isLocale();
						} catch (Exception e) {
							return false;
						}
					}
				}).toList();
				currentForm.setLstCodiciServizio(listaServizi);
			}

			if (ValidazioneDati.size(currentForm.getLstCodiciServizio()) < 2) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.noServiziCambioServizio"));
				saveToken(request);
				return mapping.getInputForward();
			}

			currentForm.setConferma(false);
			currentForm.setCambioServizio(true);
			Navigation navi = Navigation.getInstance(request);
			navi.setAttribute(NavigazioneServizi.CAMBIO_SERVIZIO, currentForm.getDetMov().copy());

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward cambiaSupportoDettMov(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			currentForm.setConferma(false);
			currentForm.setFlgErrNuovaRichiesta(false);

			MovimentoVO detMov = currentForm.isCambioServizio() ? currentForm.getMovCambioServizio() : currentForm.getDetMov();

			//almaviva5_20101217 #4063
			if (currentForm.isCambioServizio() )
				loadCollections(currentForm, request, detMov);

			if (currentForm.getUlt_supp().equals("S")) {

				// se ult_supp è "S"
				// La lista dei supporti, estratta a partire dalla Categoria di Riproduzione,
				// è stata precedentemente salvata su TipiSupporto presente sul Form
				// Il supporto selezionato è presente su currentForm.getDetMov().getCodSupporto()

				// estraggo le modalità di erogazione associate al supporto selezionato
				List<SupportiModalitaErogazioneVO> listaSupportiModalitaErogazione = this.getSupportiModalitaErogazione(detMov.getCodPolo(),
						detMov.getCodBibOperante(), currentForm.getDetMov().getCodSupporto(), request);
				if (ValidazioneDati.isFilled(listaSupportiModalitaErogazione) ) {
					List<TariffeModalitaErogazioneVO> listaTariffe = new ArrayList<TariffeModalitaErogazioneVO>();
					for (SupportiModalitaErogazioneVO sme : listaSupportiModalitaErogazione) {
						TariffeModalitaErogazioneVO tme = new TariffeModalitaErogazioneVO();
						ClonePool.copyCommonProperties(tme, sme);
						listaTariffe.add(tme);
					}
					currentForm.setModoErogazione(listaTariffe);
					// imposto la prima modalità di erogazione
					currentForm.getDetMov().setCod_erog(listaTariffe.get(0).getCodErog());
				} else {
					// ERRORE !!! Non ci sono modalità di erogazione
					// associate al supporto selezionato
					currentForm.setModoErogazione(new ArrayList<TariffeModalitaErogazioneVO>());
					currentForm.setFlgErrNuovaRichiesta(true);
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonAssociateAlSupporto"));
					saveToken(request);
					return mapping.getInputForward();

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

	public ActionForward confermaServizio(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			currentForm.setConferma(false);

			// mi salvo il codice servizio associato al codice tipo servizio selezionato
			String codServizio = "";
			Iterator<ServizioBibliotecaVO> iterator = currentForm.getLstCodiciServizio().iterator();
			while (iterator.hasNext()) {
				ServizioBibliotecaVO servizioVO = iterator.next();
				if (servizioVO.getCodTipoServ() != null &&
						servizioVO.getCodTipoServ().equals(currentForm.getCodServNuovaRich())) {
					// se sto elaborando il tipo servizio (servizio) selezionato
					// mi salvo il codice servizio (diritto)
					codServizio = servizioVO.getCodServ();
					break;
				}
			}

			if (ValidazioneDati.isFilled(codServizio) ) {
				ServizioBibliotecaVO servizioSelezionato = this.getServizio(
						currentForm.getLstCodiciServizio(), currentForm.getCodServNuovaRich(), codServizio);
				currentForm.setServizioSelezionato(servizioSelezionato);
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				currentForm.setLstIter(delegate.getIterServizio(
						servizioSelezionato.getCodPolo(), servizioSelezionato
								.getCodBib(), servizioSelezionato
								.getCodTipoServ()));
			} else {
				currentForm.setLstIter(new ArrayList<IterServizioVO>());
				currentForm.setProgrIter(null);
			}
			// ripulisco, se già presente, la selezione alle attività
			currentForm.setProgrIter(null);

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	private void cancMov(Long[] codSelMov, String uteVar, HttpServletRequest request)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		delegate.cancellaRichieste(codSelMov, uteVar);
	}


	private void rifiutaMov(Long[] codSelMov, String uteVar, HttpServletRequest request)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		delegate.rifiutaRichieste(codSelMov, uteVar, getOperatore(request) == OperatoreType.BIBLIOTECARIO);
	}


	private void aggiornaMov(DettaglioMovimentoForm currentForm, HttpServletRequest request, MovimentoVO mov)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		ServizioBibliotecaVO srv = delegate.getServizioBiblioteca(mov.getCodPolo(), mov.getCodBibOperante(),
			mov.getCodTipoServ(), mov.getCodServ());
		delegate.aggiornaRichiesta(mov, srv.getIdServizio());
	}


	private void sospendiUtente(HttpServletRequest request, DettaglioMovimentoForm currentForm)
	throws Exception {
		Navigation navi = Navigation.getInstance(request);
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		BaseVO datiModifica = new BaseVO();
		datiModifica.setUteVar(navi.getUtente().getFirmaUtente());
		datiModifica.setTsVar(new Timestamp(Calendar.getInstance().getTimeInMillis()));

		delegate.sospendiDirittoUtente(currentForm.getDetMov(), currentForm.getData_sosp(), datiModifica);
		currentForm.setData_sosp(null);
	}


	public ActionForward avanza(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response)

	throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			beforeUpdateChecks(mapping, request, currentForm, currentForm.getDetMov());
			preparaAvanzaMovimento(request, currentForm);

			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.AVANZA);
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);
			return mapping.getInputForward();

		} catch (Exception ex) {
			return mapping.getInputForward();
		}
	}


	private void nuovaRichiesta(DettaglioMovimentoForm currentForm,
			HttpServletRequest request, MovimentoVO template) throws NumberFormatException, Exception {
		ActionMessages errors = new ActionMessages();

		MovimentoListaVO mov = currentForm.getDetMov();
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);


		if (currentForm.isConfermaNuovaRichiesta() ) {
			// vengo prima della conferma dell'elaborazione della nuova richiesta

			if (ValidazioneDati.strIsNull(currentForm.getCodServNuovaRich()) ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.servizioAssente"));
				this.saveErrors(request, errors);
				throw new ValidationException("Errore creazione nuova richiesta");
			}

			if (ValidazioneDati.strIsNull(currentForm.getProgrIter()) ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliIter"));
				this.saveErrors(request, errors);
				throw new ValidationException("Errore creazione nuova richiesta");
			}

			if (currentForm.getUtenteVO() == null) {
				UtenteBaseVO utente = this.getUtente(mov.getCodPolo(),
						mov.getCodBibUte(), mov.getCodUte(), mov.getCodBibOperante(), request);
				if (utente == null) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.datiUtenteNonTrovati"));
					this.saveErrors(request, errors);
					throw new ValidationException("Utente non trovato in base dati");
				} else
					currentForm.setUtenteVO(utente);
			}

			//creazione nuova richiesta
			IterServizioVO item = this.getIterScelto(currentForm.getLstIter(), currentForm.getProgrIter());

			ControlloAttivitaServizio attivita = delegate.getAttivita(mov.getCodPolo(), item);

			if (!attivita.bibliotecarioAbilitato(navi.getUtente())) {
				//Il bibliotecario non è abilitato all'iter al quale si avanza il movimento
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.bibliotecarioNonAbilitato"));
				throw new ValidationException(SbnErrorTypes.SRV_ERRORE_BLOCCANTE_CAMBIO_SERVIZIO);
			}

			MovimentoVO nuovaRichiesta =
				this.preparaMovimento(request,
						null,
						mov.getCodPolo(),
						mov.getCodBibOperante(),
						currentForm.getUtenteVO(),
						currentForm.getInfoDocumentoVO(),
						currentForm.getServizioSelezionato(),
						attivita,
						getOperatore(request));

			//almaviva5_20110516 segnalazione ICCU: perdita data inizio eff. in cambio serv.
			if (template != null) {
				nuovaRichiesta.setDataInizioEff(template.getDataInizioEff());
			}

			IterServizioVO passoIter = attivita.getPassoIter();

			DatiControlloVO dati = new DatiControlloVO(
					navi.getUserTicket(),
					nuovaRichiesta,
					getOperatore(request),
					false, null);
			//Esecuzione operazioni di default associate all'attività
			ControlloAttivitaServizioResult controlloAttivita = eseguiControlliDefault(currentForm, attivita, dati);

			if (!ControlloAttivitaServizioResult.isOK(controlloAttivita) ) {
				List<String> msgSupplementari = dati.getCodiciMsgSupplementari();
				if (ValidazioneDati.isFilled(msgSupplementari) ) {
					//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
					this.addErrors(request, msgSupplementari);
					msgSupplementari.clear();
				} else
					//errore controlli default
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.erroreEsecuzioneControlloDiDefault"));
				throw new ValidationException(SbnErrorTypes.SRV_ERRORE_BLOCCANTE_CAMBIO_SERVIZIO);
			}

			//Esecuzione controlli associati all'iter
			List<String> messaggi = new ArrayList<String>();
			boolean checkIter = attivita.controlloIter(dati, messaggi, false, false);
			if (ValidazioneDati.isFilled(messaggi) ) {
				String[] msg = new String[messaggi.size()];
				messaggi.toArray(msg);
				this.saveErrors(request, msg);
			}

			if (!checkIter) {
				//errore controlli passo iter
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreBloccanteOperazioneKo"));
				throw new ValidationException(SbnErrorTypes.SRV_ERRORE_BLOCCANTE_CAMBIO_SERVIZIO);
			}

			checkStampa(currentForm, mov, passoIter);

			// mi salvo la nuova richiesta relativa al cambio servizio
			currentForm.setMovCambioServizio(nuovaRichiesta);

			// i valori successivi sono stati impostati e salvati precedentemente in
			// preparaMovimento di ErogazioneAction

			List<SupportoBibliotecaVO> listaSupporti = (List<SupportoBibliotecaVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_SUPPORTI);
			if (ValidazioneDati.isFilled(listaSupporti) ) {
				// e siamo in presenza di ulteriori supporti
				// imposto la drop con la lista dei supporti
				currentForm.setTipiSupporto(listaSupporti);
				// nella combo è già impostato il primo elemento della lista dei supporti
				// imposto la drop con la lista delle modalità di erogazione associate al primo supporto
				currentForm.setModoErogazione((List<TariffeModalitaErogazioneVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE));
				// nella combo è già impostato il primo elemento della lista delle modalità di erogazione
			}
			else {
				// in caso contrario (non siamo in presenza di ulteriori supporti)
				// la lista dei supporti la creo vuota
				currentForm.setTipiSupporto(new ArrayList<SupportoBibliotecaVO>());
				// imposto la drop con la lista delle modalità di erogazione associate al servizio
				currentForm.setModoErogazione((List<TariffeModalitaErogazioneVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE));
				// nella combo è già impostato il primo elemento della lista delle modalità di erogazione
			}

		} else {

			// vengo dopo la conferma dell'elaborazione della nuova richiesta
			// ho selezionato il servizio, l'attività, l'eventuale supporto e la modalità di erogazione

			//riporto la nuova richiesta precedentemente salvata
			MovimentoVO nuovaRichiesta = currentForm.getMovCambioServizio();

			//lascio invariati i seguenti attributi sulla nuova richiesta
			MovimentoListaVO vecchioDettaglio = mov.copy();

			//almaviva5_20111026 #4684
			nuovaRichiesta.setNoteBibliotecario(vecchioDettaglio.getNoteBibliotecario());
			nuovaRichiesta.setNoteUtente(vecchioDettaglio.getNoteUtente());

			// lascio le date così come vengono impostate nell'inserimento di una nuova richiesta
			//nuovaRichiesta.setDataRichiesta(vecchioDettaglio.getDataRichiesta());
			//nuovaRichiesta.setDataInizioPrev(vecchioDettaglio.getDataInizioPrev());
			//nuovaRichiesta.setDataInizioEff(vecchioDettaglio.getDataInizioEff());


			// imposto l'eventuale supporto selezionato per la nuova richiesta
			if (ValidazioneDati.isFilled(currentForm.getDetMov().getCodSupporto()))
				nuovaRichiesta.setCodSupporto(currentForm.getDetMov().getCodSupporto());

			// imposto la modalità di erogazione selezionata per la nuova richiesta
			nuovaRichiesta.setCod_erog(currentForm.getDetMov().getCod_erog());


			//La seguente funzione cancella la richiesta vecchia e ne inserisce una nuova
			nuovaRichiesta = delegate.aggiornaRichiestaPerCambioServizio(
					nuovaRichiesta,
					vecchioDettaglio.getIdRichiesta(),
					currentForm.getServizioSelezionato().getIdServizio(),
					navi.getUtente().getFirmaUtente());

			if (nuovaRichiesta == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.erroreCambioServizio"));
				this.saveErrors(request, errors);
				throw new ValidationException("Inventario durante il cambio servizio");
			}

			MovimentoListaVO dettaglioMovimento = this.creaDettaglioMovimento(
					request, currentForm.getUtenteVO(), currentForm
							.getInfoDocumentoVO(), nuovaRichiesta);

			currentForm.setDetMov(dettaglioMovimento);

			// imposto il polo e la biblioteca operante precedentemente salvati
			currentForm.getDetMov().setCodPolo(currentForm.getSalvaPolo());
			currentForm.getDetMov().setCodBibOperante(currentForm.getSalvaBibOperante());

			// salvo il movimento
			currentForm.setMovimentoSalvato((MovimentoListaVO)currentForm.getDetMov().clone());

			// ripristino gli opportuni flag
			currentForm.setCambioServizio(false);
			currentForm.setCodServNuovaRich(null);
			currentForm.setLstIter(null);
			currentForm.setProgrIter(null);

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
			this.saveErrors(request, errors);
			saveToken(request);

			//almaviva5_20100308 se ero in un dettaglio multiplo, devo sostituire nella
			//lista dei movimenti la vecchia richiesta con la nuova
			List<MovimentoListaVO> listaMovimenti = currentForm.getListaMovimenti();
			if (ValidazioneDati.isFilled(listaMovimenti)) {
				int pos = currentForm.getPosizioneCorrente();
				listaMovimenti.remove(pos);
				listaMovimenti.add(pos, dettaglioMovimento);
			}

		}

	}


	private void checkStampa(DettaglioMovimentoForm currentForm,
			MovimentoListaVO mov, IterServizioVO passoIter) {
		if (ValidazioneDati.in(passoIter.getFlagStampa(), "s", "S")	&& passoIter.getNumPag() > 0) {

			// imposto il campo checkIter della form per indicare
			// la richiesta della stampa modulo
			currentForm.setStampaModulo(true);

			// imposto nel movimento i campi che saranno successivamente,
			// se confermata la richiesta di avanzamento, utilizzati
			// per la stampa del modulo

			// Imposto il numero copie della stampa modulo richiesta
			mov.setNumeroCopieStampaModulo(String.valueOf(passoIter.getNumPag()));
			// Imposto il testo della stampa modulo richiesta
			mov.setTestoStampaModulo(passoIter.getTesto());

		}
	}


	private ActionForward eseguiRinnovo(ActionMapping mapping, ActionForm form, HttpServletRequest request)
	throws Exception {
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		currentForm.setConferma(false);

		if (currentForm.getDataProrogaMassima() != null) {
			//Si avvisa il bibliotecario che la data proroga inserita supera la massima
			//prevista dalle parametrizzazioni. Si chiederà conferma se proseguire ugualmente
			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.PROROGA_SUCCESSIVA_DATA_MASSIMA);
			//almaviva5_20151113 #6032
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.rinnovo.prorogaSuccessivaADataMassima",
					currentForm.getDetMov().getDataProrogaString(),
					DateUtil.formattaData(currentForm.getDataProrogaMassima().getTime())));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);
		} else {
			this.aggiornaMovimentoPerProroga(currentForm, request);

			if (currentForm.isEsistonoPrenotazioni()) {
				// se ci sono prenotazioni
				// imposto la data inizio e data fine prevista della prenotazione con la data proroga
				this.aggiornaPrenotazioniDopoRinnovo(currentForm, request);
			}

		}

		return mapping.getInputForward();
	}

	/**
	 * Utilizzata nel caso di rinnovo<br/><br/>
	  * it.iccu.sbn.web.actions.servizi.erogazione
	  * DettaglioMovimentoAction.java
	  * aggiornaMovimento
	  * void
	  * @param currentForm
	  * @param request
	  * @throws Exception
	  *
	  *
	 */
	private void aggiornaMovimentoPerProroga(DettaglioMovimentoForm currentForm, HttpServletRequest request)
	throws Exception {

		MovimentoListaVO mov = currentForm.getDetMov();
		mov.setNumRinnovi(currentForm.getNumeroRinnovo());
		mov.setCodStatoRic("N");	//prorogata
		mov.decode();

		//la data proroga (di tipo Date) non trasporta i secondi
		//prendo quelli della vecchia fine prevista per impostare
		//l'equivalenza a livello di orario
		Date nuovaDataFine = mov.getDataProroga();
		nuovaDataFine = DateUtil.copiaOrario(mov.getDataFinePrev(), nuovaDataFine);
		mov.setDataFinePrev(new Timestamp(nuovaDataFine.getTime()));

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		delegate.aggiornaRichiesta(mov, currentForm.getServizioMovimento().getIdServizio());

		currentForm.setDataProrogaMassima(null);
		currentForm.setNumeroRinnovo((short)0);

		currentForm.setMovimentoSalvato((MovimentoListaVO)mov.clone());

		LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.rinnovoEffettuato"));
	}

	private void aggiornaPrenotazioniDopoRinnovo(DettaglioMovimentoForm dettForm, HttpServletRequest request)
	throws Exception {

		// provengo da RINNOVO_CON_PRENOTAZIONE
		// estraggo le prenotazioni presenti per quel documento
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		MovimentoListaVO mov = dettForm.getDetMov();
		DescrittoreBloccoVO blocco1 = delegate.getPrenotazioni(mov,
			mov.getCodBibOperante(),
			this.getLocale(request,	Constants.SBN_LOCALE)/* request.getLocale() */);

		if (DescrittoreBloccoVO.isFilled(blocco1) ) {

			List<MovimentoListaVO> listaPrenotazioni = new ArrayList<MovimentoListaVO>();
			listaPrenotazioni = blocco1.getLista();

			Iterator<MovimentoListaVO> i = listaPrenotazioni.iterator();

			//la data proroga (di tipo Date) non trasporta i secondi
			//prendo quelli della vecchia fine prevista per impostare
			//l'equivalenza a livello di orario
			Date nuovaDataFine = mov.getDataProroga();
			nuovaDataFine = DateUtil.copiaOrario(mov.getDataFinePrev(), nuovaDataFine);
			Timestamp nuovaFinePrev = new Timestamp(nuovaDataFine.getTime());

			mov.setDataFinePrev(nuovaFinePrev);

			while (i.hasNext()) {
				// faccio un loop sulle prenotazioni
				MovimentoListaVO prenotazione = delegate.getDettaglioMovimento(i.next(), Locale.getDefault());
				// imposto la data inizio e data fine prevista della prenotazione con la data proroga
				prenotazione.setDataInizioPrev(nuovaFinePrev);
				prenotazione.setDataFinePrev(nuovaFinePrev);

				ServizioBibliotecaVO servizio = getServizio(prenotazione.getCodPolo(),
						prenotazione.getCodBibOperante(),
						prenotazione.getCodTipoServ(),
						prenotazione.getCodServ(),
						request);

				delegate.aggiornaRichiesta(prenotazione, servizio.getIdServizio());
			}

		}

	}


	private void validate(DettaglioMovimentoForm dettForm, HttpServletRequest request,
						 SupportoBibliotecaVO supporto, TariffeModalitaErogazioneVO modalitaErogazione)
	throws ValidationException, NumberFormatException, ParseException {
		boolean ok=true;
		ActionMessages errors = new ActionMessages();

		MovimentoListaVO mov = dettForm.getDetMov();

		//boolean numPezziImpostato = mov.getNumPezzi()!=null && !mov.getNumPezzi().trim().equals("");
		//boolean supportoImpostato = mov.getCodSupporto()!=null && !mov.getCodSupporto().trim().equals("");


		if (modalitaErogazione == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.dettaglioMovimento.noModErogazione"));
			ok=false;
		}
		else {

			if (ValidazioneDati.strIsNull(mov.getNumPezzi())) {
				// imposto a zero il numero pezzi se
				// questo non è presente
				mov.setNumPezzi("0");
			}

		}

		if (!ok) {
			this.saveErrors(request, errors);
			throw new ValidationException("Errore validazione dati");
		}
	}


	private void calcoloCostoServizio(DettaglioMovimentoForm dettForm,
			SupportoBibliotecaVO supporto, TariffeModalitaErogazioneVO modalitaErogazione)
		throws ValidationException, NumberFormatException, ParseException {

		ServiziUtil.calcolaCostoServizio(dettForm.getDetMov(), supporto, modalitaErogazione);
	}


	private void checkDatiRinnovo(DettaglioMovimentoForm currentForm,
			HttpServletRequest request) throws ValidationException {

		MovimentoListaVO detMov = currentForm.getDetMov();
		if (!detMov.isRinnovabile()) {
			//non rinnovabile
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.rinnovo.attivitaNonRinnovabile"));
			throw new ValidationException("Errore validazione dati");
		}

		short numRinnoviGiaEffettuati = detMov.getNumRinnovi();
		if (numRinnoviGiaEffettuati == MovimentoVO.MAX_RINNOVI) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.rinnovo.maxNumeroRinnoviRaggiunto"));
			throw new ValidationException("Errore validazione dati");
		}

		ServizioBibliotecaVO servizio = currentForm.getServizioMovimento();
		short durataMassimaRinnovo = 0;
		short numeroRinnovo = (short) (numRinnoviGiaEffettuati + 1);

		switch(numeroRinnovo) {
		case 1:
			durataMassimaRinnovo = servizio.getDurMaxRinn1();
			break;
		case 2:
			durataMassimaRinnovo = servizio.getDurMaxRinn2();
			break;
		case 3:
			durataMassimaRinnovo = servizio.getDurMaxRinn3();
			break;
		default:
			break;
		}

		if (durataMassimaRinnovo == 0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.rinnovo.durataRinnovoNonImpostata"));
			throw new ValidationException("Errore validazione dati");
		}

		Date dataFinePrevista = detMov.getDataFinePrev();
		Date dataProroga      = detMov.getDataProroga();

		if (dataProroga == null) { //|| dataProroga.equals(movimentoSalvato.getDataProroga())) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.rinnovo.dataProrogaNonInserita"));
			throw new ValidationException("Errore validazione dati");
		}

		//data proroga inferiore data fine prevista
		if (dataProroga.compareTo(dataFinePrevista) <= 0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.rinnovo.dataProrogaMinoreDataFinePrevista"));
			throw new ValidationException("Errore validazione dati");
		}

		Date dataProrogaMassima = DateUtil.addDay(dataFinePrevista, durataMassimaRinnovo);

		currentForm.setDataProrogaMassima(null);
		currentForm.setNumeroRinnovo(numeroRinnovo);
		if (dataProroga.compareTo(dataProrogaMassima) > 0)
			//Si avvisa il bibliotecario che la data proroga inserita supera la massima
			//prevista dalle parametrizzazioni. Si chiederà conferma se proseguire ugualmente
			currentForm.setDataProrogaMassima(dataProrogaMassima);

	}


	private boolean esistePrenotazione(MovimentoVO mov, HttpServletRequest request) throws RemoteException
	{
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.esistonoPrenotazioni(mov, new Timestamp(mov.getDataProroga().getTime()));
	}


	private void preparaAvanzaMovimento(HttpServletRequest request, DettaglioMovimentoForm currentForm)
	throws ValidationException, Exception {
		//ActionMessages errors = new ActionMessages();

		//Istanza contenente i dati del movimento modificati in seguito all'avanzamento di stato
		MovimentoListaVO movimentoCorrente = currentForm.getDetMov();
		MovimentoListaVO movDaAvanzare = (MovimentoListaVO)movimentoCorrente.clone();

		String ls_svolg = movDaAvanzare.getFlSvolg();
		boolean ok = true;

		// controllo biblioteca destinataria nel caso di ILL
		if (ls_svolg.equals('I') && ValidazioneDati.strIsNull(movDaAvanzare.getCodBibDest()) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceBibDestNonImpostata"));
			ok = false;
		} else {
			if (ValidazioneDati.isFilled(movDaAvanzare.getCodAttivitaSucc()) ) {

				ControlloAttivitaServizio attivitaSuccessiva = null;
				boolean found = false;
				List<ControlloAttivitaServizio> listaAttSucc = currentForm.getListaAttivitaSucc();
				for (ControlloAttivitaServizio cas : listaAttSucc) {
					if (cas.getPassoIter().getCodAttivita().equals(movDaAvanzare.getCodAttivitaSucc())) {
						attivitaSuccessiva = cas;
						found = true;
						break;
					}
				}

				if (!found) {
					//errore: iter non trovato
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.nuovaRichiesta.passoIterNonPresente"));
					throw new ValidationException("Errore durante avanzamento movimento");
				}

				//almaviva5_20151208 servizi ILL
				currentForm.setAttivitaPendente(attivitaSuccessiva);

				Navigation navi = Navigation.getInstance(request);
				IterServizioVO passoIter = attivitaSuccessiva.getPassoIter();
				boolean locale = !StatoIterRichiesta.of(passoIter.getCodAttivita()).isILL();
				if (attivitaSuccessiva.bibliotecarioAbilitato(navi.getUtente())) {
					if (locale) {
						//l'attivita e progr. iter su movimento locale va aggiornato solo se il passo è locale
						movDaAvanzare.setProgrIter(passoIter.getProgrIter().toString());
					}
					movDaAvanzare.setCodStatoMov(passoIter.getCodStatoMov());
					movDaAvanzare.setCodStatoRic(passoIter.getCodStatoRich());

					movDaAvanzare.setRinnovabile(ValidazioneDati.equals(passoIter.getFlgRinn(), "S") );	//rinnovabile?
					movDaAvanzare.decode();

					DatiControlloVO datiControllo = new DatiControlloVO(
							navi.getUserTicket(),
							movDaAvanzare,
							getOperatore(request),
							false, null);
					//Esecuzione operazioni di default associate all'attività
					ControlloAttivitaServizioResult controlloAttivita =
						eseguiControlliDefault(currentForm, attivitaSuccessiva, datiControllo);

					List<String> msgSupplementari = datiControllo.getCodiciMsgSupplementari();
					if (ValidazioneDati.isFilled(msgSupplementari) ) {
						//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
						this.addErrors(request, msgSupplementari);
						msgSupplementari.clear();
					}

					if (controlloAttivita == ControlloAttivitaServizioResult.OK) {
						//Esecuzione controlli associati all'iter
						List<String> messaggi = new ArrayList<String>();
						boolean checkIter = attivitaSuccessiva.controlloIter(datiControllo, messaggi, false, false);
						if (ValidazioneDati.isFilled(messaggi) ) {
							String[] msg = new String[messaggi.size()];
							messaggi.toArray(msg);
							this.saveErrors(request, msg);
						}
						if (checkIter) {
							checkStampa(currentForm, movDaAvanzare, passoIter);
							//almaviva5_20151208 servizi ILL
							MessaggioVO msg = datiControllo.getUltimoMessaggio();
							if (msg != null)
								currentForm.setMessaggio(msg);
						} else {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreBloccanteOperazioneKo"));
							ok = false;
						}

					} else {

						msgSupplementari = datiControllo.getCodiciMsgSupplementari();
						if (ValidazioneDati.isFilled(msgSupplementari) ) {
							//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
							this.addErrors(request, msgSupplementari);
							msgSupplementari.clear();
						} else
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.erroreEsecuzioneControlloDiDefault"));

						ok = false;
					}
				} else {
					//Il bibliotecario non è abilitato all'iter al quale si avanza il movimento
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.bibliotecarioNonAbilitato"));
					ok = false;
				}
			}  else {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.dettaglioMovimento.attivitaSuccessivaNonImpostata"));
				ok = false;
			}
		}

		if (!ok)
			throw new ValidationException("Errore durante avanzamento movimento");
		else {
			currentForm.setDetMovConferma((MovimentoListaVO) movimentoCorrente.clone()); //copia dello stato attuale del movimento
			currentForm.setDetMov(movDaAvanzare); // da visualizzare in conferma

			//scorrimento pagina a panel messaggio ILL
			MessaggioVO msg = currentForm.getMessaggio();
			if (msg != null) {
				int id = msg.getUniqueId();
				Navigation.getInstance(request).getCache().getCurrentElement().setAnchorId(id + "");
				request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + id);
			}
		}
	}


	private ControlloAttivitaServizioResult eseguiControlliDefault(
			DettaglioMovimentoForm mov,
			ControlloAttivitaServizio attivita, DatiControlloVO dati) throws Exception {

		ControlloAttivitaServizioResult controlloOK = attivita.controlloDefault(dati);

		if (controlloOK == ControlloAttivitaServizioResult.OK) {
			// Controllo se è stato eseguito il controllo relativo alla
			// restituzione
			// da parte del lettore per determinare eventuali penalità
			if (ValidazioneDati.equals(dati.getControlloEseguito(), StatoIterRichiesta.RESTITUZIONE_DOCUMENTO) ) {
				if (dati.getGgRitardo() > 0) {
					// se ci sono giorni di ritardo
					// imposto nel movimento i giorni di ritardo
					mov.setGg_rit(dati.getGgRitardo());
					if (dati.getGgSospensione() > 0) {
						// se ci sono giorni di sospensione
						// imposto nel movimento i giorni di sospensione
						mov.setGg_sosp(dati.getGgSospensione());
						// imposto nel movimento la data di sospensione
						mov.setData_sosp(dati.getDataSospensione());
					}
				}
			}
		}

		return controlloOK;
	}


	@SuppressWarnings("unchecked")
	private void loadCollections(DettaglioMovimentoForm currentForm,
			HttpServletRequest request, MovimentoVO mov) throws Exception  {

		try {
			boolean ill = mov.isRichiestaILL();
			DatiRichiestaILLVO datiILL = mov.getDatiILL();

			// carico i valori della tabella tb_codici relativi a quel servizio
			CodTipoServizio ts = CodTipoServizio.of(getCodice(mov.getCodTipoServ(), CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN, request));
			if (ts != null) {
				if (ts.richiedeSupporto() || (ill && datiILL.isFornitrice()) ) {
					// se il flag relativo a ult_supp sulla tabella codici è "S"
					currentForm.setUlt_supp("S");
					currentForm.setUlt_mod("N");
					//una richiesta ILL come richiedente avrà il supporto determinato dalla biblioteca fornitrice,
					//quindi non va impostato anche se richiesto dalla configurazione locale
					if (mov.isRichiedenteRichiestaILL() ) {
						currentForm.setUlt_supp("N");
						currentForm.setUlt_mod("S");
					}
				}
				else{
					// altrimenti
					if (ts.richiedeModalitaErogazione()) {
						// se il flag relativo a ult_supp sulla tabella codici non è "S"
						// e  il flag relativo a ult_mod  sulla tabella codici è "S"
						currentForm.setUlt_supp("N");
						currentForm.setUlt_mod("S");
					}
					else {
						// il flag relativo a ult_supp sulla tabella codici non è "S"
						// e  il flag relativo a ult_mod  sulla tabella codici non è "S"
						currentForm.setUlt_supp("N");
						currentForm.setUlt_mod("N");
					}
				}
			}

			if (ValidazioneDati.equals(currentForm.getUlt_supp(), "S")) {

				// se ult_supp è "S"
				List<SupportoBibliotecaVO> listaSupporti = new ArrayList<SupportoBibliotecaVO>();

				log.debug("Categoria_riproduzione documento: " + mov.getCat_riproduzione());

				if (!ValidazioneDati.isFilled(mov.getCat_riproduzione()) ) {

					// se non presente la categoria di riproduzione nel documento
					// e il richiedente è il bibliotecario (questa action viene utilizzata
					// solo per il bibliotecario)
					// in tal caso la lista dei supporti
					// viene estratta dai supporti configurati per quel polo-biblioteca
					listaSupporti = this.getSupportiBiblioteca(mov.getCodPolo(), mov.getCodBibOperante(), request);

				} else {

					// la lista dei Supporti è estratta a partire dalla Categoria di Riproduzione
					// Ricerca della Categoria di Riproduzione del documento per estrarre
					// i supporti ad essa associati
					// viene utilizzata la tabella "tb_codici" relativamente al codice LSUP
					List<TB_CODICI> listaCodici = CodiciProvider.getCodiciCross(CodiciType.CODICE_TIPI_RIPRODUZIONE_CODICE_SUPPORTO, mov.getCat_riproduzione(), true);
					if (ValidazioneDati.isFilled(listaCodici) ) {
						for (TB_CODICI cod : listaCodici) {
							SupportoBibliotecaVO supp = this.getSupportoBiblioteca(mov.getCodPolo(), mov.getCodBibOperante(), cod.getCd_tabellaTrim(), request);
							if (supp != null)
								listaSupporti.add(supp);
						}
					}

					//almaviva5_20100511 se il supporto salvato sul movimento non é compreso
					//nella lista configurata sulla tabella codici LSUP devo comunque includerlo
					boolean sup_found = false;
					for (SupportoBibliotecaVO sup : listaSupporti)
						if ((sup_found = ValidazioneDati.equals(sup.getCodSupporto(), mov.getCodSupporto())))
							break;
					if (!sup_found) {
						SupportoBibliotecaVO movSup = this.getSupportoBiblioteca(mov.getCodPolo(), mov.getCodBibOperante(),  mov.getCodSupporto(), request);
						if (movSup != null)
							listaSupporti.add(0, movSup);
						else
							listaSupporti.add(0, new SupportoBibliotecaVO());
					}
				}

				if (ValidazioneDati.isFilled(listaSupporti) ) {

					//almaviva5_20180530 filtro supporti ill
					if (ill && datiILL.isFornitrice() ) {
						final ILLServiceType servizioILL = ILLServiceType.fromValue(datiILL.getServizio());
						listaSupporti = Stream.of(listaSupporti).filter(new Predicate<SupportoBibliotecaVO>() {
							public boolean test(SupportoBibliotecaVO supp) {
								//su prestito si impone supporto originale, che invece va escluso su riproduzione
								boolean originale = supp.getCodSupporto().equals("05");
								return !supp.isLocale() && ((servizioILL == ILLServiceType.RI && !originale)
									|| (ValidazioneDati.in(servizioILL, ILLServiceType.PR, ILLServiceType.PI) && originale));
							}
						}).toList();
					}

					// imposto la drop con la lista dei supporti
					currentForm.setTipiSupporto(listaSupporti);

					// il supporto è già presente
					// (primo supporto se nuova richiesta:
					// impostato in preparaMovimento di ErogazioneAction)
					// o valore impostato già sul movimento

					// estraggo le modalità di erogazione associate al supporto impostato
					List<SupportiModalitaErogazioneVO> listaSupportiModalitaErogazione = this.getSupportiModalitaErogazione(mov.getCodPolo(),
							mov.getCodBibOperante(), mov.getCodSupporto(), request);
					if (ValidazioneDati.isFilled(listaSupportiModalitaErogazione) ) {
						List<TariffeModalitaErogazioneVO> listaTariffe = new ArrayList<TariffeModalitaErogazioneVO>();
						for (SupportiModalitaErogazioneVO sme : listaSupportiModalitaErogazione) {
							TariffeModalitaErogazioneVO tme = new TariffeModalitaErogazioneVO();
							ClonePool.copyCommonProperties(tme, sme);
							listaTariffe.add(tme);
						}

						// imposto la drop con la lista delle modalità di erogazione
						currentForm.setModoErogazione(listaTariffe);

						// il codice di erogazione è già presente
						// (primo codice di erogazione associato al primo supporto se nuova richiesta:
						// impostato in ErogazioneAction preparaMovimento)
						// o valore impostato già sul movimento

					} else {
						// qui non dovrebbe mai passare perchè controllato in preparaMovimento di ErogazioneAction
						currentForm.setModoErogazione(ValidazioneDati.asSingletonList(new TariffeModalitaErogazioneVO()));
					}

				} else {
					// qui non dovrebbe mai passare perchè controllato in preparaMovimento di ErogazioneAction
					currentForm.setTipiSupporto(new ArrayList<SupportoBibliotecaVO>());
				}
			}

			else {
				// se ult_supp è "N"

				//la lista dei supporti è vuota
				currentForm.setTipiSupporto(new ArrayList<SupportoBibliotecaVO>());
				//listaSupporti.add(0, new SupportoBibliotecaVO());

				//la lista delle Modalità di Erogazione è quella relativa al Servizio selezionato
				//se la richiesta è locale o ill come richiedente filtro le modalità locali, come fornitrice quelle ill
				String flsvolg_erog = mov.isFornitriceRichiestaILL() ? "I" : "L";
				List<TariffeModalitaErogazioneVO> listaTariffe = ServiziDelegate.getInstance(request).getTariffeModalitaErogazioneServizio(mov.getCodPolo(), mov
						.getCodBibOperante(), mov.getCodTipoServ(), flsvolg_erog);
				if (listaTariffe != null)
					currentForm.setModoErogazione(listaTariffe);
				else
					// qui non dovrebbe mai passare perchè controllato in preparaMovimento di ErogazioneAction
					currentForm.setModoErogazione(new ArrayList<TariffeModalitaErogazioneVO>());

			}
			this.loadAttivitaSuccessive(currentForm, mov.getCodPolo(), mov.getCodBibOperante(),
					mov.getCodTipoServ(), Integer.parseInt(mov.getProgrIter()),
					mov.getDatiILL(), request);

			/////////// INIZIO caricamento combo Biblioteche del Sistema Metropolitano  			 ///////////rosa
			/////////// Tale dato sarà utilizzato come primo campo dell'inventario e della segnatura ///////////
			/////////// Parte copiata e riadattata da unspecified di RicercaBibliotecarioAction.java ///////////

			Navigation navi = Navigation.getInstance(request);
			UserVO utenteCollegato = navi.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			List<ComboVO> elencoBib = new ArrayList<ComboVO>();

			String poloCorrente = utenteCollegato.getCodPolo();
			String bib = utenteCollegato.getCodBib();
			String ticket = navi.getUserTicket();

			List<ComboVO> listaBibSistemaMetropolitano =
				factory.getSistema().getListaComboBibliotecheSistemaMetropolitano(
					ticket,
					poloCorrente,
					bib);
			ComboVO appoSpazio = new ComboVO();
			appoSpazio.setCodice("");
			appoSpazio.setDescrizione("");
			elencoBib.add(appoSpazio);
			elencoBib.addAll(listaBibSistemaMetropolitano);
			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			currentForm.setBibliotechePolo(caricaCombo.loadCodiceDesc(elencoBib));

//			if (!ValidazioneDati.strIsNull(mov.getCodBibPrelievo()) )
//				mov.setCodBibPrelievo(mov.getCodBibPrelievo());
//			else
//				if (ValidazioneDati.strIsNull(mov.getCodBibPrelievo()) )
//					mov.setCodBibPrelievo(mov.getCodBibPrelievo() != null ? mov.getCodBibPrelievo(): " ");
//
//			if (ValidazioneDati.strIsNull(mov.getCodBibRestituzione()) )
//				mov.setCodBibRestituzione(mov.getCodBibRestituzione());
//			else
//				if (ValidazioneDati.strIsNull(mov.getCodBibRestituzione()) )
//					mov.setCodBibRestituzione(mov.getCodBibRestituzione() != null ? mov.getCodBibRestituzione(): " ");
//

			if (mov.getCodBibPrelievo() != null && !mov.getCodBibPrelievo().equals("")){

			}else{
				mov.setCodBibPrelievo(elencoBib.get(0).getCodice());
			}
			if (mov.getCodBibRestituzione() != null && !mov.getCodBibRestituzione().equals("")){

			}else{
				mov.setCodBibRestituzione(elencoBib.get(0).getCodice());
			}

			//almaviva5_20160927 servizi ill
			TipoServizioVO tipoServizio = getTipoServizio(mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(), request);
			List<TB_CODICI> tipiSupportoILL = ILLConfiguration2.getInstance().getListaSupportiILL(tipoServizio);
			tipiSupportoILL = CaricamentoCombo.cutFirst(tipiSupportoILL);
			currentForm.setTipiSupportoILL(tipiSupportoILL);
			List<TB_CODICI> modoErogazioneILL = ILLConfiguration2.getInstance().getListaModErogazioneILL(tipoServizio);
			modoErogazioneILL = CaricamentoCombo.cutFirst(modoErogazioneILL);
			currentForm.setModoErogazioneILL(modoErogazioneILL);

			List<TB_CODICI> listaTipoRecord = CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC, true);
			currentForm.setListaTipoRecord(listaTipoRecord);
			currentForm.setListaTipoNumeroStd(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_NUMERO_STANDARD, true));

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			currentForm.setListaTipoCodFruizione(delegate.getComboCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE));
			currentForm.setListaTipoCodNoDisp(delegate.getComboCodici(CodiciType.CODICE_NON_DISPONIBILITA));
			currentForm.setListaPaesi(delegate.getComboCodici(CodiciType.CODICE_PAESE));
			currentForm.setListaLingue(delegate.getComboCodici(CodiciType.CODICE_LINGUA));
			currentForm.setListaNature(CodiciProvider.getCodici(CodiciType.CODICE_NATURA_ORDINE));

			impostaIntervalloCopia(request, mov);

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

	}


	private void loadAttivitaSuccessive(DettaglioMovimentoForm currentForm, String codPolo, String codBibOp, String codTipoServ, int progrIter,
			DatiRichiestaILLVO datiILL, HttpServletRequest request)
	throws RemoteException {
		List<ControlloAttivitaServizio> listaAttivita = this.getListaAttivitaSuccessive(codPolo, codBibOp, codTipoServ, progrIter, datiILL, request);
		listaAttivita.add(0, new ControlloAttivitaServizio());
		currentForm.setListaAttivitaSucc(listaAttivita);

		//almaviva5_20101217 #4065
		if (ValidazioneDati.size(listaAttivita) > 1) {
			String codAttivitaSucc = listaAttivita.get(1).getPassoIter().getCodAttivita();
			currentForm.getDetMov().setCodAttivitaSucc(codAttivitaSucc);
		}
	}


	private void impostaDatiServizi(DettaglioMovimentoForm currentForm,
			List<ServizioBibliotecaVO> listaServizi, MovimentoVO mov,
			HttpServletRequest request) throws RemoteException {
		List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
		currentForm.setLstServizi(lstServizi);
		lstServizi.add(new ServizioBibliotecaVO());

		if (!ValidazioneDati.isFilled(listaServizi) ) {
			currentForm.setServizioMovimento(getServizio(mov.getCodPolo(), mov
					.getCodBibOperante(), mov.getCodTipoServ(), mov
					.getCodServ(), request));
		}

		if (ValidazioneDati.isFilled(listaServizi)) {
			for (ServizioBibliotecaVO srv : listaServizi ) {
				if (ValidazioneDati.equals(srv.getCodTipoServ(), mov.getCodTipoServ())
					&& ValidazioneDati.equals(srv.getCodServ(), mov.getCodServ()))
					currentForm.setServizioMovimento(srv);
				else
					lstServizi.add(srv);
			}
		}
	}


	private void impostaIntervalloCopia(HttpServletRequest request, MovimentoVO mov)
		throws ValidationException, NumberFormatException, ParseException {

		String intervalloCopia = mov.getIntervalloCopia();
		if (ValidazioneDati.isFilled(intervalloCopia)) {

			// se l'intervallo copia è impostato
			int numPezzi = ServiziUtil.getNumeroPaginePerRiproduzione(intervalloCopia);
			if (numPezzi != ServiziConstant.NUM_PAGINE_ERROR)
				mov.setNumPezzi(String.valueOf(numPezzi));
			else
				LinkableTagUtils.addError(request, new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE) );

		}
	}


	private IterServizioVO getIterScelto(List<IterServizioVO> listaIter,
			String progrIter) {
		Iterator<IterServizioVO> iterator = listaIter.iterator();
		IterServizioVO iterServizioVO = null;

		while (iterator.hasNext()) {
			iterServizioVO = iterator.next();
			if (iterServizioVO.getProgrIter().equals(new Short(progrIter)))
				break;
		}

		return iterServizioVO;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			if (!super.checkAttivita(request, form, idCheck))	//autorizzazione gestione
				return false;

			if (ValidazioneDati.in(idCheck,
					Sezioni.DOC_TIPO_DOCUMENTO,
					Sezioni.DOC_NATURA,
					Sezioni.DOC_BID_TITOLO))
				return false;

			if (ValidazioneDati.in(idCheck, Sezioni.DOC_DATI_SPOGLIO))
				return true;

			RichiestaDettaglioMovimentoType richiesta = RichiestaDettaglioMovimentoType.valueOf(idCheck);

			Navigation navi = Navigation.getInstance(request);
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			final DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
			MovimentoListaVO mov = currentForm.getDetMov();
			boolean nuovo = mov.isNuovo();
			boolean ill = mov.isRichiestaILL();

			switch (richiesta) {
			case INOLTRO_PRENOTAZIONE:
				// rendo visibile il bottone Inoltro
				// se non ci sono movimento attivi associati alla prenotazione
				//almaviva5_20100518 se periodico permetto al bibliotecario di forzare l'inoltro
				return !nuovo && delegate.isAbilitatoErogazione() && (ValidazioneDati.in(mov.getCodStatoRic(), "A") && (mov.isPeriodico() || !delegate.esisteMovimentoAttivo(mov)));

			case OK:
				return delegate.isAbilitatoErogazione() && isMovimentoAttivo(mov);

			case AVANZA:
				return !nuovo && delegate.isAbilitatoErogazione() && (
						(!ill && isMovimentoAttivo(mov)) || (ill && ValidazioneDati.size(currentForm.getListaAttivitaSucc()) > 1
								//isMovimentoAvanzabile(mov)
								) );

			case CAMBIO_SERVIZIO:
				// rendo visibile il bottone Salva
				// se il movimento è attivo
				return !nuovo && !ill && delegate.isAbilitatoErogazione() && isMovimentoAttivo(mov);

			case RINNOVA: {
				boolean rinnovabile = !nuovo && delegate.isAbilitatoErogazione() && isMovimentoAttivo(mov) && mov.isRinnovabile();
				if (rinnovabile && ill)
					rinnovabile = ILLConfiguration2.getInstance().isRichiestaRinnovabile(mov.getDatiILL());

				return rinnovabile;
			}

			case CANCELLA:
				return !nuovo && !ill && delegate.isAbilitatoErogazione() && isMovimentoAttivo(mov) && delegate.movimentoStatoPrecedeConsegnaDocLett(mov);

			case RIFIUTA: {
				// rendo visibile il bottone Cancella/Rifiuta
				// se il movimento è attivo e se
				// è in uno stato precedente la
				// Consegna del documento al lettore
				boolean show = !nuovo && delegate.isAbilitatoErogazione() && ((isMovimentoAttivo(mov) && delegate.movimentoStatoPrecedeConsegnaDocLett(mov))
					|| ValidazioneDati.equals(mov.getCodStatoMov(), "P"));
				DatiRichiestaILLVO datiILL = mov.getDatiILL();
				return show && (mov.isRichiestaLocale() || (datiILL.isFornitrice() || (datiILL.isRichiedente() && datiILL.getTransactionId() == 0)) );
			}

			case RIFIUTA_PROROGA:
				//almaviva5_20100421 #3463
				//movimento attivo e stato richiesta == 'P' (attesa proroga)
				return !nuovo && !ill && delegate.isAbilitatoErogazione() && isMovimentoAttivo(mov) && ValidazioneDati.equals(mov.getCodStatoRic(), "P");

			case RICHIESTA_ILL:
				//almaviva5_20150127 servizi ill
				return !nuovo && ill && !navi.bookmarkExists(Bookmark.Servizi.DATI_RICHIESTA_ILL);

			case MESSAGGIO:
				return !nuovo && ill && currentForm.isConferma() && currentForm.getMessaggio() != null;

			case CONDIZIONE: {
					final MessaggioVO msg = currentForm.getMessaggio();
					return !nuovo && ill && currentForm.isConferma() &&
							msg != null && StatoIterRichiesta.of(msg.getStato()).withCondition();
				}

			case PRENOTAZIONI: {
				return !nuovo && !mov.isPrenotazione() &&
						(!ill || mov.getDatiILL().isFornitrice() );
			}

			case INOLTRO_ALTRA_BIB_FORNITRICE: {
				return !nuovo
						&& ill
						&& !navi.bookmarkExists(Bookmark.Servizi.DATI_RICHIESTA_ILL)
						&& ILLConfiguration2.getInstance().isRichiestaInoltrabileAltraBib(mov.getDatiILL());
			}

			case LISTA_BIB_FORNITRICI: {
				DatiRichiestaILLVO dati = mov.getDatiILL();
				return currentForm.getRichiesta() == RichiestaDettaglioMovimentoType.INOLTRO_ALTRA_BIB_FORNITRICE
					//&& checkAttivita(request, currentForm, RichiestaDettaglioMovimentoType.INOLTRO_BIB_FORNITRICE.name())
					&& ValidazioneDati.isFilled(dati.getBibliotecheFornitrici());

			}

			case SUPPORTO_EROGAZIONE_ILL: {
				DatiRichiestaILLVO dati = mov.getDatiILL();
				return mov.isNuovo() || ILLConfiguration2.getInstance().isRichiestaAnnullabile(dati);
			}

			case DATA_MASSIMA_ILL: {
				DatiRichiestaILLVO dati = mov.getDatiILL();
				return !nuovo && ill && dati.isRichiedente() && ILLConfiguration2.getInstance().isRichiestaAnnullabile(dati);
			}

			case ILL_RICHIEDENTE: {
				DatiRichiestaILLVO dati = mov.getDatiILL();
				return !ill || dati.isRichiedente() &&
						ValidazioneDati.in(StatoIterRichiesta.of(dati.getCurrentState()),
								StatoIterRichiesta.F100_DEFINIZIONE_RICHIESTA_DA_UTENTE,
								StatoIterRichiesta.F111_DEFINIZIONE_RICHIESTA);
			}

			case ILL_DOWNLOAD: {
				DatiRichiestaILLDecorator dati = (DatiRichiestaILLDecorator) mov.getDatiILL();
				return !nuovo && ill && dati.isRichiedente() && ValidazioneDati.isFilled(dati.getDocDeliveryLink());
			}

			case ILL_UPLOAD: {
				DatiRichiestaILLDecorator dati = (DatiRichiestaILLDecorator) mov.getDatiILL();
				return !nuovo && ill && dati.isFornitrice() && ValidazioneDati.isFilled(dati.getDocDeliveryLink());
			}

			case CHIEDI_ANNULLAMENTO: {
				DatiRichiestaILLVO datiILL = mov.getDatiILL();
				return !nuovo && mov.isRichiedenteRichiestaILL() && ILLConfiguration2.getInstance().isRichiestaAnnullabile(datiILL)
						&& datiILL.getTransactionId() > 0;
			}

			default:
				break;

			}

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private boolean isMovimentoAttivo(MovimentoListaVO movimento) {
		return ValidazioneDati.in(movimento.getCodStatoMov(), "A", "S");
	}
/*
	private boolean isMovimentoAvanzabile(MovimentoVO mov) throws Exception {
		if (!mov.isRichiestaILL())
			return true;

		//verifica se esiste un percorso per avanzare la richiesta
		ILLConfiguration2 conf = ILLConfiguration2.getInstance();
		DatiRichiestaILLVO dati = mov.getDatiILL();
		List<AttivitaServizioVO> attivitaSeguenti = conf.getListaAttivitaSuccessive(null, null,
				dati.getServizio(), dati.getRuolo(), dati.getCurrentState(),
				mov.getCodAttivita(), null);
		return ValidazioneDati.isFilled(attivitaSeguenti);
	}
*/
	public BibliotecaVO getBiblioteca(String codPolo, String codBib) throws Exception {
		BibliotecaVO biblioteca = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		biblioteca = factory.getSistema().getBiblioteca(codPolo, codBib);
		return biblioteca;
	}


	private void caricaServiziDisponibiliUtenteDocumento(DettaglioMovimentoForm currentForm,
			HttpServletRequest request) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		UserVO utente = Navigation.getInstance(request).getUtente();

		//MovimentoVO movRicerca = currentForm.getMovRicerca();

		MovimentoVO dettMovimento = currentForm.getDetMov();

		InfoDocumentoVO infoDocumentoVO = currentForm.getInfoDocumentoVO();

		Iterator<ServizioBibliotecaVO> i = null;
		Iterator<ServizioBibliotecaVO> i2 = null;
		boolean first;
		List<ServizioBibliotecaVO> lstDirittiUtente = null;

		lstDirittiUtente = delegate.getServiziAttivi(utente
			.getCodPolo(), dettMovimento.getCodBibUte(),
			dettMovimento.getCodUte(), dettMovimento.getCodBibOperante(),
			DaoManager.now());

		if (ValidazioneDati.isFilled(lstDirittiUtente) ) {
			// imposto in lstDirittiUtente la descrizione del Codice Tipo Servizio
			// prendendola dalla tabella tb_codici
			i = lstDirittiUtente.iterator();
			while (i.hasNext()) {
				ServizioBibliotecaVO servizioVO = i.next();
				if (servizioVO.getCodTipoServ() != null) {
					servizioVO.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO.getCodTipoServ(),
							CodiciType.CODICE_TIPO_SERVIZIO,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}
			}
		}

		//almaviva5_20101217 #4091
		if (!ValidazioneDati.isFilled(lstDirittiUtente) ) {
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
				// selezione dei soli diritti comuni tra quelli dell'utente
				// e quelli dell'invenraio o segnatura
				//almaviva5_20180502 #6578
				//if (infoDocumentoVO.getDocumentoNonSbnVO() == null) {
				if (infoDocumentoVO.isRichiestaSuInventario() ) {

					// oltre l'utente c'è solo l'inventario
					// elaboro lstDirittiInventario

					List<ServizioBibliotecaVO> lstDirittiInventario = null;

					InventarioTitoloVO inventario = infoDocumentoVO.getInventarioTitoloVO();
					String codFrui = inventario.getCodFrui() != null ? inventario.getCodFrui() : "";

					lstDirittiInventario = delegate.getServiziAttiviPerCatFruizione(utente
							.getCodPolo(), inventario.getCodBib(), codFrui.trim());

					if (ValidazioneDati.isFilled(lstDirittiInventario) ) {

						// confronto lstDirittiUtente e lstDirittiInventario
						// riportando su lstServizi solo gli elementi di
						// lstDirittiUtente che sono presenti anche in lstDirittiInventario

						List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
						i = lstDirittiUtente.iterator();

						first = true;

						// loop su lstDirittiUtente
						while (i.hasNext()) {
							ServizioBibliotecaVO servizioVO = i.next();
							if (first) {
								// il primo elemento viene copiato
								// perchè contiene valori a null
								// (nella drop il primo elemento è vuoto)
								lstServizi.add(servizioVO);
								first = false;
								continue;
							}
							i2 = lstDirittiInventario.iterator();

							// loop su lstServiziInventario
							while (i2.hasNext()) {
								ServizioBibliotecaVO servizioVO_1 = i2.next();
								if (servizioVO_1.getCodTipoServ() != null &&
									servizioVO_1.getCodServ() != null &&
								    servizioVO_1.getCodTipoServ().equals(servizioVO.getCodTipoServ()) &&
								    servizioVO_1.getCodServ().equals(servizioVO.getCodServ())){
									// copio l'elemento di lstDirittiUtente perchè trovato uguale in lstDirittiInventario
									lstServizi.add(servizioVO);
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
				else {

					// oltre l'utente c'è solo la segnatura
					// elaboro lstDirittiSegnatura

					List<ServizioBibliotecaVO> lstDirittiSegnatura = null;

					DocumentoNonSbnVO documento = infoDocumentoVO.getDocumentoNonSbnVO();
					String codFruiDoc = documento.getCodFruizione();
					if (ValidazioneDati.strIsNull(codFruiDoc)) {
						CommandResultVO result = delegate.invoke(CommandType.CATEGORIA_FRUIZIONE_DOCUMENTO_NON_SBN, documento);
						result.throwError();

						DocumentoNonSbnVO doc = (DocumentoNonSbnVO) result.getResult();
						codFruiDoc = ValidazioneDati.isFilled(doc.getCodNoDisp()) ?  "" : doc.getCodFruizione();

					}

					lstDirittiSegnatura = delegate.getServiziAttiviPerCatFruizione(utente
							.getCodPolo(), documento.getCodBib(), codFruiDoc.trim());


					if (ValidazioneDati.isFilled(lstDirittiSegnatura) ) {

						// confronto lstDirittiUtente e lstDirittiSegnatura
						// riportando su lstServizi solo gli elementi di
						// lstDirittiUtente che sono presenti anche in lstDirittiSegnatura

						List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
						i = lstDirittiUtente.iterator();

						first = true;

						// loop su lstDirittiUtente
						while (i.hasNext()) {
							ServizioBibliotecaVO servizioVO = i.next();
							if (first) {
								// il primo elemento viene copiato
								// perchè contiene valori a null
								// (nella drop il primo elemento è vuoto)
								lstServizi.add(servizioVO);
								first = false;
								continue;
							}
							i2 = lstDirittiSegnatura.iterator();

							// loop su lstDirittiSegnatura
							while (i2.hasNext()) {
								ServizioBibliotecaVO servizioVO_1 = i2.next();
								if (servizioVO_1.getCodTipoServ() != null &&
									servizioVO_1.getCodServ() != null &&
								    servizioVO_1.getCodTipoServ().equals(servizioVO.getCodTipoServ()) &&
								    servizioVO_1.getCodServ().equals(servizioVO.getCodServ())){
									// copio l'elemento di lstDirittiUtente perchè trovato uguale in lstDirittiSegnatura
									lstServizi.add(servizioVO);
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

	public ActionForward datiILL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//almaviva5_20150127 servizi ill
		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		ParametriServizi parametri = new ParametriServizi();
		parametri.put(ParamType.DETTAGLIO_MOVIMENTO, currentForm.getDetMov().copy());

		ParametriServizi.send(request, parametri);
		return Navigation.getInstance(request).goForward(mapping.findForward("datiRichiestaILL"));
	}

	public ActionForward inoltroILL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DettaglioMovimentoForm currentForm = (DettaglioMovimentoForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			Timestamp now = DaoManager.now();
			MovimentoListaVO mov = currentForm.getDetMov();
			currentForm.setDetMovConferma((MovimentoListaVO) mov.copy());

			mov.setCodStatoMov("A");
			mov.setCodStatoRic("G");

			DatiRichiestaILLVO dati = mov.getDatiILL();
			dati.setCurrentState(StatoIterRichiesta.F111_DEFINIZIONE_RICHIESTA.getISOCode());

			//seleziono la seconda biblioteca tra quelle definite come fornitrici
			BibliotecaVO oldBibForn = dati.getBibliotecheFornitrici().get(0);
			oldBibForn.setFlCanc("S");
			BibliotecaVO newBibForn = dati.getBibliotecheFornitrici().get(1);
			dati.setResponderId(newBibForn.getIsil());

			dati.setDataInizio(now);
			dati.setDataFine(null);

			//prepara messaggio
			MessaggioVO msg = new MessaggioVO(dati);
			msg.setDataMessaggio(now);
			msg.setTipoInvio(TipoInvio.INVIATO);
			msg.setStato(StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA.getISOCode());
			currentForm.setMessaggio(msg);

			beforeUpdateChecks(mapping, request, currentForm, mov);

			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaDettaglioMovimentoType.INOLTRO_ALTRA_BIB_FORNITRICE);
			currentForm.setBibSelezionata(newBibForn.getUniqueId());

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.conferma.inoltro.altra.bib"));
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

			saveToken(request);
			return mapping.getInputForward();

		} catch (ValidationException ve) {
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

}
