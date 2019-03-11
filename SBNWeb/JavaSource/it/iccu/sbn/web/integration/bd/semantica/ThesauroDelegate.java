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
package it.iccu.sbn.web.integration.bd.semantica;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneOggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaTitoliCollegatiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThesauroVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.apache.log4j.Logger;

public class ThesauroDelegate {

	private static Logger log = Logger.getLogger(ThesauroDelegate.class);

	private static final String DID_REGEX = "\\w{3}D\\d{6}";

	private final FactoryEJBDelegate factory;
	private final UserVO utente;
	private final HttpServletRequest request;
	private final Utente utenteEjb;

	public enum RicercaThesauroResult {
		diagnostico_0, analitica_1, // anche per una sola occorrenza si è
		lista_2, sintetica_3, crea_4;
	}

	private RicercaThesauroResult operazione;
	private RicercaThesauroListaVO output;
	private ThRicercaComuneVO parametri;

	protected void saveErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);

		if (errors == null || errors.isEmpty()) {
			request.removeAttribute("org.apache.struts.action.ERROR");
			return;
		} else {
			request.setAttribute("org.apache.struts.action.ERROR", errors);
			return;
		}
	}

	public void eseguiRicerca(ThRicercaComuneVO ricerca) throws Exception {
		RicercaThesauroListaVO lista = null;

		this.output = null;
		this.parametri = null;
		ricerca.validate();
		lista = this.ricercaInPolo(ricerca);

		if (lista != null) {
			this.output = lista;
			this.parametri = (ThRicercaComuneVO) ricerca.clone();
			return;
		}

		throw new Exception();
	}

	private RicercaThesauroListaVO ricercaInPolo(ThRicercaComuneVO ricerca)
			throws Exception {

		RicercaThesauroListaVO lista = null;
		if (ricerca.getOperazione() instanceof ThRicercaDescrittoreVO)
			lista = this.ricercaDescrittoreThesauro(ricerca);
		else if (ricerca.getOperazione() instanceof ThRicercaTitoliCollegatiVO) {
			lista = this.ricercaTitoliCollegati(ricerca);
		}
		return lista;
	}

	private RicercaThesauroListaVO ricercaDescrittoreThesauro(
			ThRicercaComuneVO ricerca) throws Exception {

		RicercaThesauroListaVO risposta = factory.getGestioneSemantica()
				.ricercaThesauro(ricerca, utente.getTicket());

		ThesauroVO thesauro = new ThesauroVO();
		thesauro.setCodice(ricerca.getCodThesauro());
		thesauro.setDescrizione(ricerca.getDescThesauro());
		risposta.setThesauro(thesauro);

		if (risposta.getRisultati().size() == 0) {
			// diagnostico
			// ActionMessages errors = new ActionMessages();
			// errors.add("generico", new ActionMessage(
			// "errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaThesauroResult.diagnostico_0;
			// this.operazione = RicercaThesauroResult.crea_4;
		} else if (risposta.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaThesauroResult.analitica_1;
		} else {
			// Lista
			this.operazione = RicercaThesauroResult.lista_2;
		}

		return risposta;
	}

	private RicercaThesauroListaVO ricercaTitoliCollegati(
			ThRicercaComuneVO ricerca) throws Exception {
		RicercaThesauroListaVO lista = null;

		lista = factory.getGestioneSemantica().ricercaThesauro(ricerca,
				utente.getTicket());

		ThesauroVO thesauro = new ThesauroVO();
		thesauro.setCodice(ricerca.getCodThesauro());
		thesauro.setDescrizione(ricerca.getDescThesauro());
		lista.setThesauro(thesauro);
		if (lista.getTotRighe() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaThesauroResult.diagnostico_0;
		} else if (lista.getTotRighe() == 1) {
			// Analitica
			this.operazione = RicercaThesauroResult.analitica_1;
		} else {
			if ((ricerca.getRicercaTitoliCollegati().count() == 1))
				// Lista termini di thesauro
				this.operazione = RicercaThesauroResult.lista_2;
			else
				// Sintetica termini di thesauro pre lista termini
				this.operazione = RicercaThesauroResult.sintetica_3;
		}
		return lista;
	}

	public RicercaThesauroResult getOperazione() {
		return operazione;
	}

	public RicercaThesauroListaVO getOutput() {
		return output;
	}

	public ThRicercaComuneVO getParametri() {
		return parametri;
	}

	public static final ThesauroDelegate getInstance(HttpServletRequest request) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		return new ThesauroDelegate(FactoryEJBDelegate.getInstance(), navi.getUtente(), request);
	}

	private ThesauroDelegate(FactoryEJBDelegate factory, UserVO utenteCollegato,
			HttpServletRequest request) {
		super();
		this.factory = factory;
		this.utente = utenteCollegato;
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	}

	public AnaliticaThesauroVO ricaricaReticolo(boolean livelloPolo, String did)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaThesauroVO analitica = null;
		try {
			analitica = factory.getGestioneSemantica()
					.creaAnaliticaThesauroPerDid(livelloPolo, did, 0,
							utente.getTicket());

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;
		}
		if (!analitica.isEsitoOk()) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.SBNMarc", analitica
							.getTestoEsito()));
			this.saveErrors(request, errors, null);
			return null;
		}

		return analitica;
	}

	public AnaliticaThesauroVO caricaSottoReticolo(boolean livelloPolo,
			String did, int startIndex) throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaThesauroVO analitica = null;
		try {
			analitica = factory.getGestioneSemantica()
					.creaAnaliticaThesauroPerDid(livelloPolo, did, startIndex,
							utente.getTicket());

			if (!analitica.isEsitoOk()) {
				return null;
			}

			return analitica;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;
		}
	}

	public String cercaTerminePerTestoEsatto(String codThesauro, String termine) {
		ActionMessages errors = new ActionMessages();

		if (ValidazioneDati.strIsNull(codThesauro)) {
			// thesauro non valorizzato
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noThesauro"));
			this.saveErrors(request, errors, null);
			return null;
		}

		if (ValidazioneDati.strIsNull(termine)) {
			// testo non valorizzato
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noTesto"));
			this.saveErrors(request, errors, null);
			return null;
		}

		ThRicercaComuneVO parametriRicerca = new ThRicercaComuneVO();
		parametriRicerca.setCodThesauro(codThesauro);
		parametriRicerca.getRicercaThesauroDescrittore().setTestoDescr(termine);
		parametriRicerca.setRicercaTipo(TipoRicerca.STRINGA_ESATTA);
		parametriRicerca.setRicercaTitoliCollegati(null);
		parametriRicerca.setPolo(true);

		try {
			RicercaThesauroListaVO risultatoRicerca = ricercaInPolo(parametriRicerca);
			if (risultatoRicerca.isEsitoOk()) {
				String didTrovato = ((ElementoSinteticaThesauroVO) risultatoRicerca
						.getRisultati().get(0)).getDid();
				return didTrovato;
			}

			if (risultatoRicerca.isEsitoNonTrovato())
				return "";

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.SBNMarc", risultatoRicerca
							.getTestoEsito()));
			this.saveErrors(request, errors, null);
			return null;

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, null);
			return null;
		}

	}

	public CreaVariaTermineVO gestioneTermine(
			DettaglioTermineThesauroVO dettaglio,
			TipoOperazioneOggetto operazione) throws Exception {

		ActionMessages errors = new ActionMessages();

		if (ValidazioneDati.strIsNull(dettaglio.getCodThesauro())) {
			// thesauro non valorizzato
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noThesauro"));
			this.saveErrors(request, errors, null);
			return null;
		}

		if (ValidazioneDati.strIsNull(dettaglio.getTesto())) {
			// testo non valorizzato
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noTesto"));
			this.saveErrors(request, errors, null);
			return null;
		}

		if (ValidazioneDati.strIsNull(dettaglio.getLivAut())) {
			// liv aut non valorizzato
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noLivello"));
			this.saveErrors(request, errors, null);
			return null;
		}

		CreaVariaTermineVO termine = null;

		try {

			CreaVariaTermineVO richiesta = new CreaVariaTermineVO(dettaglio);
			richiesta.setOperazione(operazione);
			richiesta.setLivelloPolo(true);
			termine = factory.getGestioneSemantica().gestioneTermineThesauro(
					richiesta, utente.getTicket());

			if (termine.isEsitoOk()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.operOk"));
				this.saveErrors(request, errors, null);
				return termine;
			}

			if (termine.getEsitoType() == SbnMarcEsitoType.SIMILE_ESISTENTE ) {
				// il messaggio d'errore contiene il did del simile
				Matcher matcher = Pattern.compile(DID_REGEX).matcher(termine.getTestoEsito() );
				if (matcher.find() ) {
					// cerco i dati del simile
					String didSimile = matcher.group();
					ThRicercaComuneVO paramRicerca = new ThRicercaComuneVO();
					paramRicerca.setPolo(true);
					paramRicerca.getRicercaThesauroDescrittore().setDid(didSimile);
					RicercaThesauroListaVO ricercaDescrittoreThesauro =
						ricercaDescrittoreThesauro(paramRicerca);

					if (ricercaDescrittoreThesauro.isEsitoOk()) {
						termine.setEsito(SbnMarcEsitoType.TROVATI_SIMILI.getEsito());	//sto barando
						termine.setListaSimili(ricercaDescrittoreThesauro.getRisultati());
						return termine;
					}
				}
			}

			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.gestioneSemantica.SBNMarc",
							termine.getTestoEsito()));
			this.saveErrors(request, errors, null);

			return null;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}

	}

	public CreaVariaTermineVO gestioneLegameTermini(DatiLegameTerminiVO termine) {

		ActionMessages errors = new ActionMessages();

		try {
			CreaVariaTermineVO nuovoTermine = factory.getGestioneSemantica()
					.gestioneLegameTerminiThesauro(termine,
							utente.getTicket());

			if (nuovoTermine.isEsitoOk()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.operOk"));
				this.saveErrors(request, errors, null);
				return nuovoTermine;
			}

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.SBNMarc", nuovoTermine
							.getTestoEsito()));
			this.saveErrors(request, errors, null);
			return null;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}

	}

	public RicercaThesauroListaVO nextBlocco(String idLista, int numBlocco,
			int maxRighe) {

		ActionMessages errors = new ActionMessages();
		RicercaThesauroListaVO areaDatiPass = new RicercaThesauroListaVO();
		areaDatiPass.setNumPrimo(numBlocco);
		areaDatiPass.setMaxRighe(maxRighe);
		areaDatiPass.setIdLista(idLista);
		areaDatiPass.setLivelloPolo(true);

		try {
			RicercaThesauroListaVO areaDatiPassReturn = factory
					.getGestioneSemantica().getNextBloccoTermini(areaDatiPass,
							utente.getTicket());

			if (areaDatiPassReturn == null
					|| areaDatiPassReturn.getRisultati().size() == 0) {

				// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO
				// SELEZIONATO"
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noElementi"));
				this.saveErrors(request, errors, null);
				return null;

			}

			return areaDatiPassReturn;
		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}
	}

	public CatSemThesauroVO ricercaTerminiPerBidCollegato(
			boolean livelloPolo, String bid, String codThesauro,
			int elementiBlocco, boolean addMessage) {
		ActionMessages errors = new ActionMessages();

		try {
			CatSemThesauroVO risultatoRicerca = factory.getGestioneSemantica()
					.ricercaTerminiPerBidCollegato(livelloPolo, bid,
							codThesauro, elementiBlocco,
							utente.getTicket());

			if (!risultatoRicerca.isEsitoOk() && addMessage) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", risultatoRicerca
								.getTestoEsito()));
				this.saveErrors(request, errors, null);
				return null;
			}

			return risultatoRicerca;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}

	}

	public ActionForward titoliCollegatiBiblio(String codBib, String did, String testo,
			ActionMapping mapping, boolean filtro) {

		request.setAttribute(TitoliCollegatiInvoke.codBiblio, codBib);
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, did);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, testo);
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, Navigation
				.getInstance(request).getCache().getCurrentElement().getUri());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_THESAURO));

		ActionForward forward = null;
		if (filtro)
			forward = mapping.findForward("delegate_titoliCollegatiFiltro");
		else
			forward = mapping.findForward("delegate_titoliCollegati");
		return Navigation.getInstance(request).goForward(forward);

	}

	public ActionForward titoliCollegatiPolo(String did, String testo,
			ActionMapping mapping, boolean filtro) {

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, did);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, testo);
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, Navigation
				.getInstance(request).getCache().getCurrentElement().getUri());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_THESAURO));

		ActionForward forward = null;
		if (filtro)
			forward = mapping.findForward("delegate_titoliCollegatiFiltro");
		else
			forward = mapping.findForward("delegate_titoliCollegati");
		return Navigation.getInstance(request).goForward(forward);
	}

	public SBNMarcCommonVO gestioneLegameTitoloTermini(
			DatiLegameTitoloTermineVO datiLegame) {
		ActionMessages errors = new ActionMessages();

		try {
			SBNMarcCommonVO risultato = factory.getGestioneSemantica()
					.gestioneLegameTitoloTermini(datiLegame,
							utente.getTicket());

			if (!risultato.isEsitoOk()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", risultato
								.getTestoEsito()));
				this.saveErrors(request, errors, null);
				return null;
			}

			// tutto ok
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.operOk"));
			this.saveErrors(request, errors, null);

			return risultato;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}

	}

	public RicercaThesauroListaVO ricercaTerminiPerDidCollegato(
			boolean livelloPolo, String did, int elementiBlocco) {

		ActionMessages errors = new ActionMessages();

		try {
			RicercaThesauroListaVO risultato = factory.getGestioneSemantica()
					.ricercaTerminiPerDidCollegato(livelloPolo,
							utente.getTicket(), did, elementiBlocco);

			if (!risultato.isEsitoOk()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", risultato
								.getTestoEsito()));
				this.saveErrors(request, errors, null);
				return null;
			}

			return risultato;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}

	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO listaTitoliCollegatiBiblio(
			String did, int maxRighe) {

		ActionMessages errors = new ActionMessages();

		try {

			// CHIAMATA ALL'EJB DI INTERROGAZIONE
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
			areaDatiPass
					.setOggChiamante(TitoliCollegatiInvoke.ANALITICA_DETTAGLIO);
			areaDatiPass
					.setTipoOggetto(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_THESAURO);
			areaDatiPass.setNaturaTitBase("");
			areaDatiPass.setTipMatTitBase("");
			areaDatiPass.setCodiceLegame("");
			areaDatiPass.setCodiceSici("");
			areaDatiPass.setOggDiRicerca(did);
			areaDatiPass.clear();
			areaDatiPass.setRicercaIndice(false);
			areaDatiPass.setRicercaPolo(true);
			UserVO utenteCollegato = Navigation.getInstance(request)
					.getUtente();
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
					.getGestioneBibliografica().ricercaTitoli(areaDatiPass,
							utenteCollegato.getTicket());

			if (areaDatiPassReturn == null) {

				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.noConnessione"));
				this.saveErrors(request, errors, null);
				return null;
			}

			if (areaDatiPassReturn.getCodErr().equals("9999")
					|| areaDatiPassReturn.getTestoProtocollo() != null) {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo",
						areaDatiPassReturn.getTestoProtocollo()));
				this.saveErrors(request, errors, null);
				return null;

			} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica."
								+ areaDatiPassReturn.getCodErr()));
				this.saveErrors(request, errors, null);
				return null;
			}

			if (areaDatiPassReturn.getNumNotizie() < 1) {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.testoProtocollo",
						areaDatiPassReturn.getTestoProtocollo()));
				this.saveErrors(request, errors, null);
				return null;
			}

			return areaDatiPassReturn;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}
	}

	public AreaDatiAccorpamentoReturnVO fondiTerminiThesauro(
			boolean livelloPolo, DatiFusioneTerminiVO datiFusione) {
		ActionMessages errors = new ActionMessages();

		try {
			AreaDatiAccorpamentoReturnVO risultato = factory
					.getGestioneSemantica().fondiTerminiThesauro(livelloPolo,
							datiFusione, utente.getTicket());

			if (risultato.isEsitoOk()) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.operOk"));
				this.saveErrors(request, errors, null);

				return risultato;

			}

			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.gestioneSemantica.SBNMarc",
							risultato.getIdErrore()));
			this.saveErrors(request, errors, null);
			return null;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}

	}

	public String getDescrizioneCodice(CodiciType tipoCodice, String codice) {
		ActionMessages errors = new ActionMessages();

		try {
			String risultato = factory.getCodici().getDescrizioneCodiceSBN(
					tipoCodice, codice);
			return risultato;

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			e.printStackTrace();
			return null;// gestione errori java
		}
	}

	public boolean isAbilitato(String attivita) throws Exception {

		try {
			utenteEjb.checkAttivitaAut(attivita, "TH");
			utenteEjb.isAbilitatoAuthority("TH");
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}
	}

	public boolean isLivAutOk(String livAut, boolean addMessage)
			throws Exception {

		ActionMessages errors = new ActionMessages();

		try {
			utenteEjb.checkLivAutAuthority("TH", Integer.valueOf(livAut));
			return true;
		} catch (UtenteNotAuthorizedException ute) {

			if (addMessage) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.authentication.livello_aut"));
				this.saveErrors(request, errors, null);
			}
			return false;
		}
	}

	public String getMaxLivelloAutorita() throws Exception {

		try {
			return utenteEjb.getLivAutAuthority("TH");

		} catch (UtenteNotAuthorizedException ute) {
			return "05";
		}
	}

	public Utente getEJBUtente() {
		return (Utente) request.getSession().getAttribute("UTENTE_BEAN");
	}

	public boolean isThesauroGestito(String codThesauro) throws Exception {

		List<TB_CODICI> thesauri = factory.getGestioneSemantica()
				.getCodiciThesauro(utente.getTicket());
		for (TB_CODICI thes : thesauri) {
			if (thes.getCd_tabella().trim().equals(codThesauro))
				return true;
		}

		return false;
	}

	public SBNMarcCommonVO gestioneLegameTermineClasse(DatiLegameTermineClasseVO datiLegame) {

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.SEM_GESTIONE_LEGAME_TERMINE_CLASSE, datiLegame);
		try {
			CommandResultVO result = factory.getGestioneSemantica().invoke(command);
			result.throwError();

			SBNMarcCommonVO response = (SBNMarcCommonVO) result.getResult();
			if (!response.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", response.getTestoEsito()));
				return null;
			}

			return response;

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()));
		}

		return null;
	}

}
