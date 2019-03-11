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
import it.iccu.sbn.ejb.remote.GestioneSemantica;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.EdDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ParametriClassi;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ParametriClassi.ClassiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ParametriClassi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SistemaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.sif.AttivazioneSIFListaClassiCollegateVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class ClassiDelegate {

	private static Logger log = Logger.getLogger(ClassiDelegate.class);

	private static final int SBNMARC_MAX_RIGHE = 4000;

	private static final String CLASSI_DELEGATE = "req.classi.delegate";

	private final FactoryEJBDelegate factory;
	private final UserVO utente;
	private final HttpServletRequest request;
	private final Utente utenteEjb;

	public enum RicercaClasseResult {
		diagnostico_0,
		analitica_1, // anche per una sola occorrenza si è
		lista_2,
		crea_3;
	}

	private RicercaClasseResult operazione;
	private RicercaClassiVO parametri;
	private RicercaClasseListaVO output;


	public RicercaClassiVO getParametri() {
		return parametri;
	}

	public void setParametri(RicercaClassiVO parametri) {
		this.parametri = parametri;
	}

	public RicercaClasseListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaClasseListaVO output) {
		this.output = output;
	}

	public void eseguiRicerca(RicercaClassiVO ricerca, ActionMapping mapping) throws Exception {
		RicercaClasseListaVO lista = null;
		this.parametri = ricerca;
		if (ricerca.isPolo())
			lista = this.ricercaInPolo(ricerca, mapping);
		 else
			lista = this.ricercaInIndice(ricerca, mapping);

		if (lista != null) {
			this.output = lista;
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, ricerca); //"ricercaLista"
			request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, lista); //"outputlista"
			return;
		}

		// lista == null
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
	private RicercaClasseListaVO ricercaInIndice(RicercaClassiVO ricerca,
			ActionMapping mapping) throws Exception {
		RicercaClasseListaVO lista = null;
		lista = this.ricercaClassiIndice(ricerca, mapping);
		return lista;
	}

	/**
	 *
	 * Metodo implementato da Anna Maria questo metodo esegue la ricerca in
	 * INDICE per Classi
	 *
	 * @param ricerca
	 * @param request
	 * @param mapping
	 * @return RicercaClasseListaVO
	 * @throws Exception
	 */
	private RicercaClasseListaVO ricercaClassiIndice(RicercaClassiVO ricerca,
			ActionMapping mapping) throws Exception {
		RicercaClasseListaVO lista = null;

		lista = factory.getGestioneSemantica().cercaClassi(ricerca, utente.getTicket());
		SistemaClassificazioneVO sistemaClassificazione = new SistemaClassificazioneVO();
		sistemaClassificazione.setCodice(ricerca.getCodSistemaClassificazione());
		sistemaClassificazione.setDescrizione(ricerca.getDescSistemaClassificazione());
		lista.setSistema(sistemaClassificazione);
		EdDeweyVO edizioneDewey = new EdDeweyVO();
		edizioneDewey.setCodice(ricerca.getCodEdizioneDewey());
		edizioneDewey.setDescrizione(ricerca.getDescEdizioneDewey());
		lista.setEdizione(edizioneDewey);

		if (!ValidazioneDati.in(lista.getEsitoType(),
				SbnMarcEsitoType.OK,
				SbnMarcEsitoType.NON_TROVATO)) {
			this.operazione = RicercaClasseResult.diagnostico_0;
			return lista;
		}

		if (!ValidazioneDati.isFilled(lista.getRisultati()) ) {
			// diagnostico

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaClasseResult.crea_3;
		} else if (lista.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaClasseResult.analitica_1;
		} else {
			this.operazione = RicercaClasseResult.lista_2;
		}
		return lista;

	}

	private RicercaClasseListaVO ricercaInPolo(RicercaClassiVO ricerca,
			ActionMapping mapping) throws Exception {
		RicercaClasseListaVO lista = null;
		lista = this.ricercaClassiPolo(ricerca, mapping, request);
		return lista;
	}

	private RicercaClasseListaVO ricercaClassiPolo(RicercaClassiVO ricerca,
			ActionMapping mapping, HttpServletRequest request) throws Exception {

		RicercaClasseListaVO lista = null;

		lista = factory.getGestioneSemantica().cercaClassi(ricerca, utente.getTicket());

		SistemaClassificazioneVO sistemaClassificazione = new SistemaClassificazioneVO();
		sistemaClassificazione
				.setCodice(ricerca.getCodSistemaClassificazione());
		sistemaClassificazione.setDescrizione(ricerca
				.getDescSistemaClassificazione());
		lista.setSistema(sistemaClassificazione);
		EdDeweyVO edizioneDewey = new EdDeweyVO();
		edizioneDewey.setCodice(ricerca.getCodEdizioneDewey());
		edizioneDewey.setDescrizione(ricerca.getDescEdizioneDewey());
		lista.setEdizione(edizioneDewey);

		if (!ValidazioneDati.in(lista.getEsitoType(),
				SbnMarcEsitoType.OK,
				SbnMarcEsitoType.NON_TROVATO)) {
			this.operazione = RicercaClasseResult.diagnostico_0;
			return lista;
		}

		if (!ValidazioneDati.isFilled(lista.getRisultati()) ) {
			// diagnostico

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.nontrovato"));
			this.operazione = RicercaClasseResult.crea_3;
		} else if (lista.getRisultati().size() == 1) {
			// Analitica
			this.operazione = RicercaClasseResult.analitica_1;
		} else {
			this.operazione = RicercaClasseResult.lista_2;
		}
		return lista;
	}

	public RicercaClasseResult getOperazione() {
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

	public static final ClassiDelegate getInstance(HttpServletRequest request) throws Exception {
		ClassiDelegate delegate = (ClassiDelegate) request.getAttribute(CLASSI_DELEGATE);
		if (delegate == null) {
			delegate = new ClassiDelegate(FactoryEJBDelegate.getInstance(), Navigation.getInstance(request).getUtente(), request);
			request.setAttribute(CLASSI_DELEGATE, delegate);
		}
		return delegate;
	}

	private ClassiDelegate(FactoryEJBDelegate factory, UserVO utenteCollegato,
			HttpServletRequest request) {
		super();
		this.factory = factory;
		this.utente = utenteCollegato;
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	}

	public RicercaClasseListaVO nextBlocco(String idLista, int numBlocco,
			int maxRighe) {


		RicercaClasseListaVO areaDatiPass = new RicercaClasseListaVO();
		areaDatiPass.setNumPrimo(numBlocco);
		areaDatiPass.setMaxRighe(maxRighe);
		areaDatiPass.setIdLista(idLista);
		areaDatiPass.setLivelloPolo(true);

		try {
			RicercaClasseListaVO areaDatiPassReturn = (RicercaClasseListaVO) factory
					.getGestioneSemantica().getNextBloccoClassi(areaDatiPass,
							utente.getTicket());

			if (areaDatiPassReturn == null
					|| areaDatiPassReturn.getRisultati().size() == 0) {

				// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO
				// SELEZIONATO"
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.noElementi"));

				return null;

			}

			return areaDatiPassReturn;
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
			return null;// gestione errori java
		} catch (InfrastructureException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;// gestione errori java
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;// gestione errori java
		}
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


	public ActionForward getSIFListaClassiCollegate(ActionMapping mapping, AttivazioneSIFListaClassiCollegateVO richiesta)	 {

		if (ValidazioneDati.strIsNull(richiesta.getBid())) {
			// thesauro non valorizzato
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noThesauro"));
			return mapping.getInputForward();
		}

		if (ValidazioneDati.strIsNull(richiesta.getTitolo())) {
			// testo non valorizzato
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noTesto"));
			return mapping.getInputForward();
		}

		try {
			//almaviva5_20111114 #4694 identificazione sist. classe
			String[] key = preparaKeySistema(richiesta.getCodSistema());
			String sistema = key[0];
			String edizione = key[1];

			CatSemClassificazioneVO risposta = factory.getGestioneSemantica()
					.ricercaClassiPerBidCollegato(true, richiesta.getBid(), sistema, edizione,
							richiesta.getElementiPerBlocco(), utente.getTicket());

			if (risposta.isEsitoOk()) {

				ParametriClassi parametri = new ParametriClassi();
				parametri.put(ClassiParamType.MODALITA_CERCA_CLASSE, ModalitaCercaType.CERCA_PER_COLLOCAZIONE);
				parametri.put(ClassiParamType.OUTPUT_SINTETICA,	new RicercaClasseListaVO(risposta));
				parametri.put(ClassiParamType.SIF_ATTIVAZIONE, richiesta);
				ParametriClassi.send(request, parametri);
				return Navigation.getInstance(request).goForward(mapping.findForward("sifClassiLegateTitolo"));
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", risposta.getTestoEsito()));

			return mapping.getInputForward();

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			return mapping.getInputForward();

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));

			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			return mapping.getInputForward();
		}
	}

	private String[] preparaKeySistema(String codSistema) throws Exception {

		String sistema = null;
		String edizione = null;

		if (ValidazioneDati.isFilled(codSistema)) {
			codSistema = codSistema.trim();
			//almaviva5_20141114 edizione ridotta
			if (codSistema.matches("[dD]\\d{2}[rR]{0,1}") ) {
			//if (codSistema.matches("[dD]\\d{2}") ) {
				sistema = "D";
				edizione = codSistema.substring(1);
			} else
				if (codSistema.matches("[dD]") )
					sistema = "D";	//senza edizione
				else
					if (CodiciProvider.exists(CodiciType.CODICE_SISTEMA_CLASSE, codSistema) )
						sistema = codSistema;	//non dewey
		}

		return new String[] {sistema, edizione};
	}

	public boolean isAbilitato(String attivita) throws Exception {

		try {
			utenteEjb.isAbilitatoAuthority("CL");
			utenteEjb.checkAttivitaAut(attivita, "CL");
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}
	}

	public boolean isLivAutOk(String livAut, boolean addMessage)
			throws Exception {



		try {
			utenteEjb.checkLivAutAuthority("CL", Integer.valueOf(livAut));
			return true;
		} catch (UtenteNotAuthorizedException ute) {

			if (addMessage) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"error.authentication.livello_aut"));

			}
			return false;
		}
	}

	public String getMaxLivelloAutorita() throws Exception {

		try {
			return utenteEjb.getLivAutAuthority("CL");

		} catch (UtenteNotAuthorizedException ute) {
			return "05";
		}
	}

	public Utente getEJBUtente() {
		return (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	}

	public boolean isSistemaGestito(String codSistemaClassificazione, String codEdizione) throws Exception {

		List<TB_CODICI> sc = factory.getGestioneSemantica()
				.getSistemiClassificazione(utente.getTicket());
		for (TB_CODICI sistema : sc) {
			if (sistema.getCd_tabella().trim().equals(codSistemaClassificazione.trim() ))
				return true;
		}

		return false;
	}

	public boolean attivaCondivisioneClasse(String id) throws Exception {



		try {
			CreaVariaClasseVO classe = factory.getGestioneSemantica()
					.analiticaClasse(true, id, utente.getTicket());

			if (!classe.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", classe
								.getTestoEsito()));

				return false;
			}

			if (!classe.isCondiviso()) {
				// Imposto la classe come condivisa
				classe.setCondiviso(true);
				CreaVariaClasseVO classeVariata = factory
						.getGestioneSemantica().variaClasse(classe,
								utente.getTicket());
				if (!classeVariata.isEsitoOk()) {
					LinkableTagUtils.addError(request,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									classeVariata.getTestoEsito()));

					return false;
				}
			}

			return true;

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", e
							.getMessage()));

			log.error("", e);
			return false;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return false;
		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return false;
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return false;
		}
	}

	public DatiLegameTitoloClasseVO invioInIndiceLegamiTitoloClasse(TreeElementViewTitoli reticoloIndice, List<SinteticaClasseVO> classiDaInviare) {


		DatiLegameTitoloClasseVO risposta = null;

		if (reticoloIndice == null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema"));

			return null;
		}

		DettaglioTitoloParteFissaVO detTitoloPFissaVO = reticoloIndice
			.getAreaDatiDettaglioOggettiVO()
			.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO();
		String T005 = detTitoloPFissaVO.getVersione(); // timestamp del titolo

		if (!ValidazioneDati.isFilled(classiDaInviare) ) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));

			return null;
		}

		//almaviva5_20140630 check lunghezza descr. classe
		for (SinteticaClasseVO sc : classiDaInviare)
			if (!ClassiUtil.checkClassePerIndice(sc.getParole())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.classe.indiceMaxLen",
						ClassiUtil.MAX_LEN_CLASSE_INDICE));
				return null;
			}

		try {
			String bid = reticoloIndice.getKey(); // titolo
			// 1. carico situazione polo
			CatSemClassificazioneVO listaClassiPolo = factory
				.getGestioneSemantica().ricercaClassiPerBidCollegato(
					true, bid, null, null, SBNMARC_MAX_RIGHE, utente.getTicket());

			if (!listaClassiPolo.isEsitoOk()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", listaClassiPolo.getTestoEsito()));

				return null;
			}

			// 2. carico situazione indice
			CatSemClassificazioneVO listaClassiIndice = factory
				.getGestioneSemantica().ricercaClassiPerBidCollegato(
						false, bid, null, null, SBNMARC_MAX_RIGHE, utente.getTicket());

			if (!listaClassiIndice.isEsitoOk() && !listaClassiIndice.isEsitoNonTrovato() ) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", listaClassiIndice.getTestoEsito()));

				return null;
			}

			// 3. cancello tutti i legami in indice (se posso)
			if (listaClassiIndice.getListaClassi().size() > 0) {

				DatiLegameTitoloClasseVO legame = new DatiLegameTitoloClasseVO();

				for (SinteticaClasseVO classeLegata : listaClassiIndice.getListaClassi())
					legame.getLegami().add(
							legame.new LegameTitoloClasseVO(classeLegata
									.getSistema(), classeLegata
									.getIdentificativoClasse(), ""));

				T005 = detTitoloPFissaVO.getVersione();
				legame.setBid(detTitoloPFissaVO.getBid() );
				legame.setBidNatura(detTitoloPFissaVO.getNatura());
				legame.setT005(T005);
				legame.setBidLivelloAut(detTitoloPFissaVO.getLivAut());
				legame.setBidTipoMateriale(detTitoloPFissaVO.getTipoMat());
				legame.setLivelloPolo(false);

				risposta = factory.getGestioneSemantica().cancellaLegameTitoloClasse(legame, utente.getTicket());

				if (!risposta.isEsitoOk()) {
					LinkableTagUtils.addError(request,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									risposta.getTestoEsito()));

					return null;
				}

				//aggiorno timestamp titolo
				T005 = risposta.getT005();

			} // fase 3

			// 4. merge polo/indice tutte le classi in indice e non in polo vanno eliminate dalla lista
			List<SinteticaClasseVO> listaClassiLegateDaReinviare = generaListaClassiLegateDaReinviare(
					listaClassiPolo.getListaClassi(), listaClassiIndice.getListaClassi());

			// 5. invio in indice le classi mancanti
			for (SinteticaClasseVO classeDaInviare : classiDaInviare) {
				CreaVariaClasseVO analitica = caricaAnaliticaClasse(true, classeDaInviare.getIdentificativoClasse() );
				if (analitica == null)
					return null;

				// provo l'invio in indice della classe soggetto
				CreaVariaClasseVO classeInIndice = inviaClasseInIndice(analitica);
				if (classeInIndice == null)
					return null;

				listaClassiLegateDaReinviare.add(new SinteticaClasseVO(classeInIndice));
			} // fase 5

			// 6. reinvio di tutti i legami in indice

			//tolgo i duplicati
			Map<String, SinteticaClasseVO> classiUnique =
				new HashMap<String, SinteticaClasseVO>();
			for (SinteticaClasseVO classeLegata: listaClassiLegateDaReinviare)
				classiUnique.put(classeLegata.getIdentificativoClasse(), classeLegata);

			if (classiUnique.size() > 0) {

				DatiLegameTitoloClasseVO legame = new DatiLegameTitoloClasseVO();

				for (SinteticaClasseVO classeLegata : classiUnique.values() )
					legame.getLegami().add(
							legame.new LegameTitoloClasseVO(classeLegata
									.getSistema(), classeLegata
									.getIdentificativoClasse(), ""));

				// CREO IL LEGAME TRA TITOLO E SOGGETTO SULLA BASE DATI DI
				// INDICE
				legame.setBid(detTitoloPFissaVO.getBid() );
				legame.setBidNatura(detTitoloPFissaVO.getNatura());
				legame.setT005(T005);
				legame.setBidLivelloAut(detTitoloPFissaVO.getLivAut());
				legame.setBidTipoMateriale(detTitoloPFissaVO.getTipoMat());
				legame.setLivelloPolo(false);

				risposta = factory.getGestioneSemantica().invioInIndiceLegameTitoloClasse(legame, utente.getTicket());

				if (!risposta.isEsitoOk()) {
					LinkableTagUtils.addError(request,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									risposta.getTestoEsito()));

					return null;
				}

			} // fase 6

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.operOk"));


			return risposta;

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

	private List<SinteticaClasseVO> generaListaClassiLegateDaReinviare(
			List<SinteticaClasseVO> listaClassiPolo,
			List<SinteticaClasseVO> listaClassiIndice) {

		List<SinteticaClasseVO> output = new ArrayList<SinteticaClasseVO>();
		if (listaClassiIndice.size() < 1)
			return output;

		for (SinteticaClasseVO classePolo : listaClassiPolo ) {

			for (SinteticaClasseVO classeIndice : listaClassiIndice ) {
				String idPolo = classePolo.getIdentificativoClasse();
				if (classeIndice.getIdentificativoClasse().equals(idPolo) ) {
					output.add(classeIndice);
					break;
				}
			}	// listaClassiIndice
		}	// listaClassiPolo
		return output;
	}

	public CreaVariaClasseVO caricaAnaliticaClasse(boolean livelloPolo,
			String idClasse) throws Exception {


		CreaVariaClasseVO analitica = null;
		try {
			analitica = factory.getGestioneSemantica()
					.analiticaClasse(livelloPolo, idClasse,	utente.getTicket());

			if (analitica.isEsitoOk() || analitica.isEsitoNonTrovato() )
				return analitica;

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", analitica
							.getTestoEsito()));

			return null;

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

	public CreaVariaClasseVO analiticaClasse(boolean livelloPolo, String idClasse) throws Exception {

		try {
			CreaVariaClasseVO analitica = factory.getGestioneSemantica().analiticaClasse(livelloPolo,
					idClasse, utente.getTicket());

			if (!analitica.isEsitoOk() ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", analitica.getTestoEsito()));
				return null;
			}

			return analitica;

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			// nessun codice selezionato
			return null;

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			return null;

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			return null;

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			return null;
		}

	}

	public CreaVariaClasseVO inviaClasseInIndice(CreaVariaClasseVO classe) {



		try {
			CreaVariaClasseVO richiesta = classe.copy();
			richiesta.setForzaCreazione(false);
			richiesta.setLivelloPolo(false);

			CreaVariaClasseVO classeCreata = factory.getGestioneSemantica()
					.creaClasse(richiesta, utente.getTicket());

			// la classe non esiste, lo creo
			if (classeCreata.isEsitoOk()) {
				attivaCondivisioneClasse(classeCreata.getT001());
				return caricaAnaliticaClasse(false, classeCreata.getT001());
			}

			// esiste una classe con lo stesso id, ritorno questa
			if (classeCreata.isEsitoTrovatiSimili()) {
				SinteticaClasseVO simile = classeCreata.getListaSimili().get(0);

				attivaCondivisioneClasse(richiesta.getT001());
				return caricaAnaliticaClasse(false, simile
						.getIdentificativoClasse());
			}

			// esiste un soggetto con lo stesso CID, ritorno questo
			if (classeCreata.isEsitoIDEsistente()) {
				attivaCondivisioneClasse(richiesta.getT001());
				return caricaAnaliticaClasse(false, richiesta.getT001());
			}

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", classeCreata
							.getTestoEsito()));

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

	public ClassiCollegateTermineVO ricercaClassiPerTermineCollegato(RicercaClassiTermineVO richiesta) throws Exception {

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.SEM_LISTA_CLASSI_COLLEGATE_TERMINE, richiesta);
		try {
			CommandResultVO result = factory.getGestioneSemantica().invoke(command);
			result.throwError();

			return (ClassiCollegateTermineVO) result.getResult();

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

	public CreaVariaClasseVO variaClasse(CreaVariaClasseVO classe) throws Exception {

		GestioneSemantica ejb = factory.getGestioneSemantica();
		CreaVariaClasseVO classeVariata = ejb.variaClasse(classe, utente.getTicket());

		SbnMarcEsitoType esito = classeVariata.getEsitoType();
		if (esito == SbnMarcEsitoType.OK)
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.polo.operOk"));

		//almaviva5_20120521 #4216 si prova ad aggiornare la classe in indice
		if (!classe.isLivelloPolo()
			|| !classe.isCondiviso()
			|| esito != SbnMarcEsitoType.OK)
			return classeVariata;

		try {
			CreaVariaClasseVO classeIndice = ejb.analiticaClasse(false, classe.getT001(), utente.getTicket());
			if (classeIndice.getEsitoType() != SbnMarcEsitoType.OK)
				return classeVariata;

			CreaVariaClasseVO tmp = classeVariata.copy();
			tmp.setLivelloPolo(false);
			tmp.setT005(classeIndice.getT005());
			CreaVariaClasseVO classeIndiceVariata = ejb.variaClasse(tmp, utente.getTicket());

			SbnMarcEsitoType esitoIndice = classeIndiceVariata.getEsitoType();
			if (esitoIndice == SbnMarcEsitoType.OK)
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.indice.operOk"));
			else
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo",
						classeIndiceVariata.getTestoEsito()));

		} catch (Exception e) {
			log.error("", e);
		}

		return classeVariata;
	}


	public boolean verificaSistemaEdizionePerBiblioteca(String codPolo, String codBib,
			String codSistema) throws Exception {

		String[] key = preparaKeySistema(codSistema);
		String sistema = key[0];
		String edizione = key[1];

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.SEM_VERIFICA_SISTEMA_EDIZIONE_BIBLIOTECA, codPolo, codBib, sistema, edizione);
		try {
			CommandResultVO result = factory.getGestioneSemantica().invoke(command);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()));
		}

		return false;
	}

	public List<String> getListaSistemaEdizionePerBiblioteca(String codPolo, String codBib,
			String codSistema) throws Exception {

		String[] key = preparaKeySistema(codSistema);
		String sistema = key[0];
		String edizione = key[1];

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.SEM_LISTA_SISTEMA_EDIZIONE_BIBLIOTECA, codPolo, codBib, sistema, edizione);
		try {
			CommandResultVO result = factory.getGestioneSemantica().invoke(command);
			result.throwError();

			return (List<String>) result.getResultAsCollection(String.class);

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
