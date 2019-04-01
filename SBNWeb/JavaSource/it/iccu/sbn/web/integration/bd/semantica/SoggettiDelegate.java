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
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.semantica.soggetti.Soggetti;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.GestioneSemantica;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.DettaglioOggettoSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.SoggettarioVO;
import it.iccu.sbn.ejb.vo.semantica.TbCodiciSogg;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.custom.semantica.UserMessage;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SoggettiDelegate {

	private static Logger log = Logger.getLogger(SoggettiDelegate.class);

	static Reference<Soggetti> soggetti = new Reference<Soggetti>() {
		@Override
		protected Soggetti init() throws Exception {
			return DomainEJBFactory.getInstance().getSoggetti();
		}};

	private final FactoryEJBDelegate factory;
	private final UserVO utente;
	private final HttpServletRequest request;


	public enum RicercaSoggettoResult {
		diagnostico_0,
		analitica_1, // anche per una sola occorrenza si è
		lista_2,
		sintetica_3,
		crea_4;
	}

	private RicercaSoggettoResult operazione;
	private RicercaComuneVO parametri;
	private RicercaSoggettoListaVO output;
	private Utente utenteEjb;

	public RicercaComuneVO getParametri() {
		return parametri;
	}

	public void setParametri(RicercaComuneVO parametri) {
		this.parametri = parametri;
	}

	public RicercaSoggettoListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaSoggettoListaVO output) {
		this.output = output;
	}

	public void eseguiRicerca(RicercaComuneVO ricerca, ActionMapping mapping)
			throws Exception {
		RicercaSoggettoListaVO lista = null;
		this.parametri = ricerca;

		if (ricerca.isPolo())
			lista = this.ricercaInPolo(ricerca, mapping);
		else
			lista = this.ricercaInIndice(ricerca, mapping);

		this.output = lista;
		if (lista != null) {
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					ricerca.copy()); // "ricercaLista"
			request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, lista); // "outputlista"
			return;
		}

		throw new Exception();
	}

	/**
	 *
	 * Metodo implementato da Administrator questo metodo esegue Effettua una
	 * riceca in Indice sia per i Descrittori che per i Soggetti
	 *
	 * @param ricerca
	 * @param request
	 * @param mapping
	 * @return
	 * @throws Exception
	 */
	private RicercaSoggettoListaVO ricercaInIndice(RicercaComuneVO ricerca,
			ActionMapping mapping) throws Exception {
		RicercaSoggettoListaVO lista = null;
		if (ricerca.getOperazione() instanceof RicercaDescrittoreVO) {
			lista = this.ricercaDescrittoriInIndice(ricerca, mapping);
		} else {
			lista = this.ricercaSoggettiIndice(ricerca, mapping);
		}
		return lista;
	}

	/**
	 *
	 * Metodo implementato da Anna Maria questo metodo esegue la ricerca in
	 * INDICE per Descrittore
	 *
	 * @param ricerca
	 * @param request
	 * @param mapping
	 * @return RicercaSoggettoLista
	 * @throws Exception
	 */
	private RicercaSoggettoListaVO ricercaDescrittoriInIndice(
			RicercaComuneVO ricerca, ActionMapping mapping) throws Exception {

		RicercaSoggettoListaVO risposta = factory.getGestioneSemantica()
				.ricercaSoggetti(ricerca, utente.getTicket());

		SoggettarioVO soggettario = new SoggettarioVO();
		soggettario.setCodice(ricerca.getCodSoggettario());
		soggettario.setDescrizione(ricerca.getDescSoggettario());
		risposta.setSoggettario(soggettario);

		if (risposta.getRisultati().size() == 0) {
			// diagnostico
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaSoggettoResult.diagnostico_0;
		} else if (risposta.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaSoggettoResult.analitica_1;
		} else {
			// Sintetica
			this.operazione = RicercaSoggettoResult.sintetica_3;
		}

		return risposta;

	}

	/**
	 *
	 * Metodo implementato da Anna Maria questo metodo esegue la ricerca in
	 * INDICE per Soggetti
	 *
	 * @param ricerca
	 * @param request
	 * @param mapping
	 * @return RicercaSoggettoLista
	 * @throws Exception
	 */
	private RicercaSoggettoListaVO ricercaSoggettiIndice(
			RicercaComuneVO ricerca, ActionMapping mapping) throws Exception {

		RicercaSoggettoListaVO risposta = null;

		if (ricerca.getRicercaSoggetto() instanceof RicercaSoggettoDescrittoriVO) {

			String did = ((RicercaSoggettoDescrittoriVO) ricerca
					.getRicercaSoggetto()).getDid();
			if (!ValidazioneDati.strIsNull(did)) {
				risposta = factory.getGestioneSemantica()
						.ricercaSoggettiPerDidCollegato(false, did,
								Integer.valueOf(ricerca.getElemBlocco()),
								utente.getTicket(),
								ricerca.getOrdinamentoSoggetto(), null);

			} else {
				risposta = factory.getGestioneSemantica().ricercaSoggetti(
						ricerca, utente.getTicket());
			}
		} else {
			risposta = factory.getGestioneSemantica().ricercaSoggetti(ricerca,
					utente.getTicket());
		}

		SoggettarioVO soggettario = new SoggettarioVO();
		soggettario.setCodice(ricerca.getCodSoggettario());
		soggettario.setDescrizione(ricerca.getDescSoggettario());
		risposta.setSoggettario(soggettario);

		if (risposta.getRisultati().size() == 0) {
			// diagnostico
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaSoggettoResult.diagnostico_0;
		} else if (risposta.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaSoggettoResult.analitica_1;
		} else {
			// Lista
			this.operazione = RicercaSoggettoResult.lista_2;
		}
		return risposta;
	}

	private RicercaSoggettoListaVO ricercaInPolo(RicercaComuneVO ricerca,
			ActionMapping mapping) throws Exception {
		RicercaSoggettoListaVO lista = null;
		if (ricerca.getOperazione() instanceof RicercaDescrittoreVO) {
			lista = this.ricercaDescrittoriInPolo(ricerca, mapping);
		} else {
			lista = this.ricercaSoggettiPolo(ricerca, mapping);
		}
		return lista;
	}

	private RicercaSoggettoListaVO ricercaDescrittoriInPolo(RicercaComuneVO ricerca, ActionMapping mapping) throws Exception {

		RicercaSoggettoListaVO lista = null;
		GestioneSemantica ejb = factory.getGestioneSemantica();

		RicercaDescrittoreVO rd = ricerca.getRicercaDescrittore();
		lista = rd.isCercaDidCollegati() ?
				ejb.ricercaDescrittoriPerDidCollegato(true, rd.getDid(), Integer.valueOf(ricerca.getElemBlocco()), utente.getTicket()) :
				ejb.ricercaSoggetti(ricerca, utente.getTicket());

		SoggettarioVO soggettario = new SoggettarioVO();
		soggettario.setCodice(ricerca.getCodSoggettario());
		soggettario.setDescrizione(ricerca.getDescSoggettario());
		lista.setSoggettario(soggettario);

		if (lista.getRisultati() == null) {
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaSoggettoResult.diagnostico_0;
		} else if ((lista.getRisultati().size() == 0)) {
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaSoggettoResult.diagnostico_0;
		} else if (lista.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaSoggettoResult.analitica_1;
		} else {
			// Sintetica
			this.operazione = RicercaSoggettoResult.sintetica_3;
		}
		return lista;
	}

	private RicercaSoggettoListaVO ricercaSoggettiPolo(RicercaComuneVO ricerca,
			ActionMapping mapping) throws Exception {

		RicercaSoggettoListaVO lista = null;

		if (ricerca.getRicercaSoggetto() instanceof RicercaSoggettoDescrittoriVO) {
			int descrittori = ((RicercaSoggettoDescrittoriVO) ricerca
					.getRicercaSoggetto()).count();
			if (descrittori == 1) {
				ricerca.setRicercaTipoD(TipoRicerca.STRINGA_ESATTA);
				ricerca.setOrdinamentoDescrittore(TipoOrdinamento.PER_TESTO);

				RicercaSoggettoDescrittoriVO ricSogDesc = (RicercaSoggettoDescrittoriVO) ricerca
						.getRicercaSoggetto();

				if (!ValidazioneDati.strIsNull(ricSogDesc.getUnDescrittore())) {
					lista = factory.getGestioneSemantica()
							.ricercaSoggettiPerDescrittore(ricerca,
									utente.getTicket());
				}
			} // if descrittori==1

			String did = ((RicercaSoggettoDescrittoriVO) ricerca
					.getRicercaSoggetto()).getDid();
			if (!ValidazioneDati.strIsNull(did) && descrittori == 0) {
				lista = factory.getGestioneSemantica()
						.ricercaSoggettiPerDidCollegato(true, did,
								Integer.valueOf(ricerca.getElemBlocco()),
								utente.getTicket(), ricerca.getOrdinamentoSoggetto(),
								ricerca.getEdizioneSoggettario() );

			} else {
				if (descrittori != 1) // tutti i casi in cui il campo
					// descrittori non è uguale a 1
					lista = factory.getGestioneSemantica().ricercaSoggetti(
							ricerca, utente.getTicket());
			}
		}

		SoggettarioVO soggettario = new SoggettarioVO();
		soggettario.setCodice(ricerca.getCodSoggettario());
		soggettario.setDescrizione(ricerca.getDescSoggettario());
		lista.setSoggettario(soggettario);

		if (!lista.isEsitoOk() && !lista.isEsitoNonTrovato()
				&& !lista.isEsitoTrovatiSimili()) {
			this.operazione = RicercaSoggettoResult.diagnostico_0;
			return lista;
		}

		if (lista.getRisultati() == null) {
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaSoggettoResult.diagnostico_0;
		} else if ((lista.getRisultati().size() == 0)) {
			// diagnostico

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaSoggettoResult.crea_4;
		} else if (lista.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaSoggettoResult.analitica_1;
		} else {
			String testoSogg = ricerca.getRicercaSoggetto().getTestoSogg();
			if (!ValidazioneDati.strIsNull(testoSogg)
					|| (ricerca.getRicercaSoggetto().count() == 1)) {
				// Lista
				this.operazione = RicercaSoggettoResult.lista_2;
			} else {
				// Lista
				this.operazione = RicercaSoggettoResult.sintetica_3;
			}
		}
		return lista;
	}

	public RicercaSoggettoResult getOperazione() {
		return operazione;
	}

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

	public static final SoggettiDelegate getInstance(HttpServletRequest request) throws Exception {
		return new SoggettiDelegate(FactoryEJBDelegate.getInstance(),
				Navigation.getInstance(request).getUtente(), request);
	}

	public SoggettiDelegate(FactoryEJBDelegate factory, UserVO utenteCollegato,
			HttpServletRequest request) {
		super();
		this.factory = factory;
		this.utente = utenteCollegato;
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	}

	public boolean isAbilitatoDE(String attivita) throws Exception {

		try {
			utenteEjb.isAbilitatoAuthority("DE");
			utenteEjb.checkAttivitaAut(attivita, "DE");
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}
	}

	public boolean isLivAutOkDE(String livAut, boolean addMessage)
			throws Exception {



		try {
			utenteEjb.checkLivAutAuthority("DE", Integer.valueOf(livAut));
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			if (addMessage) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"error.authentication.livello_aut"));

			}
			return false;
		}
	}

	public String getMaxLivelloAutoritaDE() throws Exception {

		try {
			return utenteEjb.getLivAutAuthority("DE");

		} catch (UtenteNotAuthorizedException ute) {
			return "05";
		}
	}

	public boolean isAbilitatoSO(String attivita) throws Exception {

		try {
			utenteEjb.isAbilitatoAuthority("SO");
			utenteEjb.checkAttivitaAut(attivita, "SO");
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}
	}

	public boolean isLivAutOkSO(String livAut, boolean addMessage)
			throws Exception {
		try {
			utenteEjb.checkLivAutAuthority("SO", Integer.valueOf(livAut));
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			if (addMessage)
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.livello_aut"));

			return false;
		}
	}

	public String getMaxLivelloAutoritaSO() throws Exception {

		try {
			return utenteEjb.getLivAutAuthority("SO");

		} catch (UtenteNotAuthorizedException ute) {
			return "05";
		}
	}

	public Utente getEJBUtente() {
		return this.utenteEjb;
	}

	public boolean isSoggettarioGestito(String codSoggettario) throws Exception {

		List<TB_CODICI> soggettari = factory.getGestioneSemantica()
				.getCodiciSoggettario(true, utente.getTicket());
		for (TB_CODICI sistema : soggettari) {
			if (sistema.getCd_tabella().trim().equals(codSoggettario))
				return true;
		}

		return false;
	}

	public boolean isSoggettarioGestitoIndice(String codSoggettario) throws Exception {

		List<TB_CODICI> soggettari = factory.getGestioneSemantica().getCodiciSoggettario(false, utente.getTicket());
		for (TB_CODICI cod : soggettari) {
			TbCodiciSogg sogg = (TbCodiciSogg) cod;
			if (sogg.getCd_tabella().trim().equals(codSoggettario))
				return !sogg.isSoloLocale();
		}

		return false;
	}

	public TreeElementViewTitoli caricaReticoloTitolo(boolean livelloPolo, String bid) {



		// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
		// operante che nel caso di centro Sistema non coincidono
		String codBiblioSbn = Navigation.getInstance(request).getUtente().getCodPolo() + Navigation.getInstance(request).getUtente().getCodBib();
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(codBiblioSbn);
//		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
		// Fine Modifica almaviva2 16.07.2010

		try {

			areaDatiPass.setBidRicerca(bid);
			areaDatiPass.setRicercaPolo(livelloPolo);
			areaDatiPass.setRicercaIndice(!livelloPolo);
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn = factory
					.getGestioneBibliografica()
					.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass,
							utente.getTicket());

			if (ValidazioneDati.isFilled(areaDatiPassReturn.getCodErr()) ) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", areaDatiPassReturn.getTestoProtocollo() ));

				return null;
			}

			return areaDatiPassReturn.getTreeElementViewTitoli();

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			return null;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}

	}

	public AnaliticaSoggettoVO caricaReticoloSoggetto(boolean livelloPolo,
			String cid) throws Exception {

		try {
			AnaliticaSoggettoVO analitica = soggetti.get()
					.caricaReticoloSoggetto(utente.getTicket(), livelloPolo, cid);

			if (analitica.isEsitoOk() || analitica.isEsitoNonTrovato() )
				return analitica;

			consumeMessages(soggetti.get().consumeMessages(utente.getTicket()));

			return null;

		} catch (Exception e) {
			return null;
		}

	}

	public CreaVariaDescrittoreVO cancellaLegameConDescrittore(
			boolean livelloPolo, DettaglioOggettoSemanticaVO padre,
			DettaglioDescrittoreVO descrittore, String tipoLegame,
			boolean eliminaDescrittore) throws Exception {


		CreaVariaDescrittoreVO risposta = null;
		try {
			if (padre instanceof DettaglioSoggettoVO) {

				DettaglioSoggettoVO padreSO = (DettaglioSoggettoVO) padre;
				CreaLegameSoggettoDescrittoreVO legame = new CreaLegameSoggettoDescrittoreVO();

				legame.setCid(padreSO.getCid());
				legame.setCodSoggettario(padreSO.getCampoSoggettario());
				legame.setLivelloAutorita(padreSO.getLivAut());
				legame.setT005(padreSO.getT005());
				legame.setDid(descrittore.getDid());
				legame.setPolo(livelloPolo);
				legame.setCondiviso(padreSO.isCondiviso());
				legame.setCategoriaSoggetto(padreSO.getCategoriaSoggetto());

				risposta = factory.getGestioneSemantica()
						.cancellaLegameSoggettoDescrittore(legame,
								utente.getTicket());
			} else {

				DettaglioDescrittoreVO padreDE = (DettaglioDescrittoreVO) padre;
				DatiLegameDescrittoreVO legame = new DatiLegameDescrittoreVO();

				legame.setDidPartenza(padreDE.getDid());
				legame.setDidPartenzaFormaNome(padreDE.getFormaNome());
				legame.setT005(padreDE.getT005());
				legame.setDidPartenzaLivelloAut(padreDE.getLivAut());
				legame.setDidArrivo(descrittore.getDid());
				legame.setDidArrivoFormaNome(descrittore.getFormaNome());
				legame.setTipoLegame(tipoLegame);
				legame.setNotaLegame("");
				legame.setLivelloPolo(livelloPolo);
				legame.setCondiviso(padreDE.isCondiviso());

				risposta = factory.getGestioneSemantica()
						.cancellaLegameDescrittori(legame,
								utente.getTicket());
			}

			if (!risposta.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));

				return null;
			}

			if (eliminaDescrittore)
				factory.getGestioneSemantica().cancellaSoggettoDescrittore(
						livelloPolo, descrittore.getDid(),
						utente.getTicket(),
						SbnAuthority.DE);

			return risposta;

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			return null;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}
	}

	public AnaliticaSoggettoVO caricaReticoloDescrittore(boolean livelloPolo,
			String did) throws Exception {


		AnaliticaSoggettoVO analitica = null;
		try {
			analitica = factory.getGestioneSemantica()
					.creaAnaliticaSoggettoPerDid(livelloPolo, did, 0,
							utente.getTicket());

			if (!analitica.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", analitica
								.getTestoEsito()));

				return null;
			}

			return analitica;

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			return null;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}

	}

	public AnaliticaSoggettoVO caricaSottoReticoloDescrittore(
			boolean livelloPolo, String did, int startIndex) throws Exception {


		AnaliticaSoggettoVO analitica = null;
		try {
			analitica = factory.getGestioneSemantica()
					.creaAnaliticaSoggettoPerDid(livelloPolo, did, startIndex,
							utente.getTicket());

			if (!analitica.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", analitica
								.getTestoEsito()));

				return null;
			}

			return analitica;

		} catch (ValidationException e) {
			// errori indice

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			return null;

		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (InfrastructureException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}

	}

	public boolean attivaCondivisioneSoggetto(DettaglioSoggettoVO soggettoPolo, String idIndice, OrigineSoggetto origineSoggetto) throws Exception {
		try {
			boolean ok = soggetti.get().attivaCondivisioneSoggetto(utente.getTicket(), soggettoPolo, idIndice, origineSoggetto );
			consumeMessages(soggetti.get().consumeMessages(utente.getTicket()));
			return ok;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return false;
		}

	}


	public AnaliticaSoggettoVO inviaSoggettoInIndice(DettaglioSoggettoVO soggetto) {
		try {
			AnaliticaSoggettoVO soggInviato = soggetti.get().inviaSoggettoInIndice(utente.getTicket(), soggetto);
			consumeMessages(soggetti.get().consumeMessages(utente.getTicket()));
			return soggInviato;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;// gestione errori java
		}

	}

	public AnaliticaSoggettoVO importaSoggettoDaIndice(DettaglioSoggettoVO soggetto) {
		try {
			AnaliticaSoggettoVO analiticaSoggetto = soggetti.get().importaSoggettoDaIndice(utente.getTicket(), soggetto);
			consumeMessages(soggetti.get().consumeMessages(utente.getTicket()));
			return analiticaSoggetto;

		} catch (Exception e) {
			return null;
		}

	}


	public DatiLegameTitoloSoggettoVO creaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame) {
		try {
			// creo il legame con il bid in polo
			DatiLegameTitoloSoggettoVO risposta = factory
					.getGestioneSemantica().creaLegameTitoloSoggetto(legame,
							utente.getTicket());

			if (risposta.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.operOk", risposta.getTestoEsito()));

				return risposta;
			}

			if (risposta.getEsitoType() != SbnMarcEsitoType.LEGAME_ESISTENTE) {
					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));
			}

			return null;

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", e
							.getMessage()));

			log.error("", e);
			return null;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}

	}

	public DatiLegameTitoloSoggettoVO modificaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame) {
		try {
			// creo il legame con il bid in polo
			DatiLegameTitoloSoggettoVO risposta = factory
					.getGestioneSemantica().modificaLegameTitoloSoggetto(legame,
							utente.getTicket());

			if (risposta.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.operOk", risposta.getTestoEsito()));

				return risposta;
			}

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));

			return null;

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", e
							.getMessage()));

			log.error("", e);
			return null;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}

	}

	public DatiLegameTitoloSoggettoVO invioInIndiceLegamiTitoloSoggetto(TreeElementViewTitoli reticoloIndice, List<ElementoSinteticaSoggettoVO> soggettiDaInviare) {
		try {
			DatiLegameTitoloSoggettoVO legamiTitoloSoggetto = soggetti.get()
					.invioInIndiceLegamiTitoloSoggetto(utente.getTicket(), reticoloIndice, soggettiDaInviare);
			consumeMessages(soggetti.get().consumeMessages(utente.getTicket()));
			return legamiTitoloSoggetto;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;// gestione errori java
		}
	}
/*
	protected List<ElementoSinteticaSoggettoVO> generaListaSoggettiLegatiDaReinviare(
			List<ElementoSinteticaSoggettoVO> listaSoggettiPolo,
			List<ElementoSinteticaSoggettoVO> listaSoggettiIndice,
			List<ElementoSinteticaSoggettoVO> soggettiDaInviare) {

		List<ElementoSinteticaSoggettoVO> output = new ArrayList<ElementoSinteticaSoggettoVO>();
		if (listaSoggettiIndice.size() < 1)
			return output;

		for (ElementoSinteticaSoggettoVO soggettoPolo : listaSoggettiPolo ) {
			List<DatiCondivisioneSoggettoVO> datiCondivisionePolo = soggettoPolo.getDatiCondivisione();
			if (datiCondivisionePolo.size() < 1)
				continue;

			String cidIndice = datiCondivisionePolo.get(0).getCidIndice();
			for (ElementoSinteticaSoggettoVO soggettoIndice : listaSoggettiIndice ) {
				if (soggettoIndice.getCid().equals(cidIndice) ) {
					if (SoggettiUtil.isSoggettoEquals(soggettoPolo.getTesto(), soggettoIndice.getTesto()) )
						// è il mio soggetto. tutto ok
						output.add(soggettoIndice);
					else
						// qualcuno ha cambiato il soggetto in indice, devo reinviare il mio testo
						soggettiDaInviare.add(soggettoPolo);
					break;
				}
			}	// listaSoggettiIndice
		}	// listaSoggettiPolo
		return output;
	}
*/
	public int countSoggettariGestiti() {
		List<?> codiciSoggettario;
		int size = 1;
		try {
			codiciSoggettario = factory.getGestioneSemantica().getCodiciSoggettario(true, utente.getTicket());
			size = codiciSoggettario.size();
		} catch (Exception e) {
			log.error("", e);
			return 0;
		}

		return --size ;

	}

	public boolean isDescrittoreAutomaticoPerAltriSoggetti(String did)
			throws InfrastructureException, DataException, ValidationException,
			RemoteException {
		return factory.getGestioneSemantica()
				.isDescrittoreAutomaticoPerAltriSoggetti(utente.getTicket(), did);
	}

	public SBNMarcCommonVO eseguiCambioEdizioneSoggetto(DettaglioSoggettoVO oldDettaglio, String newCid) {

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.SEM_CAMBIO_EDIZIONE_SOGGETTO, oldDettaglio, newCid);
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

	public CreaVariaSoggettoVO variaSoggetto(CreaVariaSoggettoVO soggetto) {
		try {
			CreaVariaSoggettoVO soggVariato = soggetti.get().modificaSoggetto(utente.getTicket(), soggetto);
			consumeMessages(soggetti.get().consumeMessages(utente.getTicket()));
			return soggVariato;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;// gestione errori java
		}

	}

	private void consumeMessages(List<UserMessage> messages) {
		for (UserMessage msg : messages) {
			SbnBaseException e = msg.getException();
			if (e != null)
				LinkableTagUtils.addError(request, e);
			else {
				ActionMessage am = new ActionMessage(msg.getKey(), msg.getValues() );
				LinkableTagUtils.addError(request, am);
			}
		}
	}

}
