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
package it.iccu.sbn.web.actions.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.GestioneDescrittoreForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
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

public class GestioneDescrittoreAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(GestioneDescrittoreAction.class);

	public static final String CANCELLA_IMMEDIATE = "gs.elimina.descr";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.ok", "ok");
		map.put("button.soggetti", "soggetti");
		map.put("button.elimina", "elimina");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.utilizzati", "utilizzati");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private void initCombo(HttpServletRequest request, ActionForm form) 	throws Exception {
		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		String ticket = Navigation.getInstance(request).getUserTicket();
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		//almaviva5_20111125 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
		currentForm.setListaCategoriaTermine(CaricamentoComboSemantica.loadComboCategoriaSogDes(SbnAuthority.DE));
		currentForm.setListaFormaNome(CaricamentoComboSemantica.loadComboFormaNome());
		currentForm.setListaLivelloAutorita(CaricamentoComboSemantica
				.loadComboStato(SoggettiDelegate.getInstance(request)
						.getMaxLivelloAutoritaDE()));
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private String getDescrizione(String codice, ActionForm form) {
		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		for (int index = 0; index < currentForm.getListaSoggettari().size(); index++) {
			ComboCodDescVO sog = (ComboCodDescVO) currentForm
					.getListaSoggettari().get(index);
			if (sog.getCodice().equals(codice))
				return sog.getDescrizione();
		}
		return "Non trovato";
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		String dataInserimento = null;
		String dataModifica = null;
		String dataInsAnalitica = null;
		String dataModAnalitica = null;
		String chiamante = null;
		String cid = null;
		DettaglioDescrittoreVO dettaglio = null;

		boolean isPolo = false;
		boolean isManuale = false;
		boolean condiviso = false;
		String tipoSoggetto = null;
		String T005 = null;
		String testo = null;
		String note = null;

		if (!currentForm.isSessione()) {
			log.info("GestioneDescrittoreAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			cid = (String) request
					.getAttribute(NavigazioneSemantica.CID_RIFERIMENTO);
			dettaglio = (DettaglioDescrittoreVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_DESCRITTORE);
			dataInserimento = (String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			dataInsAnalitica = (String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModAnalitica = (String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();
			isManuale = !SoggettiDelegate.getInstance(request).isDescrittoreAutomaticoPerAltriSoggetti(dettaglio.getDid());
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			T005 = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA_T005);
			testo = (String) request.getAttribute(NavigazioneSemantica.TESTO_SOGGETTO);
			note = (String) request.getAttribute(NavigazioneSemantica.NOTE_OGGETTO);
			condiviso = (Boolean) request.getAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE);

			if (cid != null) {
				// Vengo da analitica soggetto
				currentForm.setCid(cid);
				currentForm.setTestoSoggetto(testo.trim());
			} else {
				// Vengo da analitica descrittore
				currentForm.setDidPadre((String) request
						.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
			}

			if (chiamante == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}
			currentForm.setSessione(true);
			currentForm.setAction(chiamante);

			try {
				this.initCombo(request, currentForm);
			} catch (Exception ex) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
			}
			this.setErrors(request, errors, null);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDataIAna(dataInsAnalitica);
			currentForm.setDataMAna(dataModAnalitica);
			currentForm.setT005Ana(T005);
			currentForm.setDettDesGenVO(dettaglio);
			currentForm.getRicercaComune().setPolo(isPolo);
			currentForm.setTipoSoggetto(tipoSoggetto);
			currentForm.setEnableManuale(isManuale);
			currentForm.setNote(note);
			currentForm
					.setEnableAnaSog(((Boolean) request
							.getAttribute(NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO))
							.booleanValue());
			currentForm
					.setProgr((String) request
							.getAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO));
			currentForm.setCondiviso(condiviso);
		}

		currentForm.setNumUtilizzati(0);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		DettaglioDescrittoreVO dettDesGenVO = currentForm.getDettDesGenVO();
		currentForm.getRicercaComune().setCodSoggettario(
				dettDesGenVO.getCampoSoggettario());
		currentForm.getRicercaComune().setPolo(
				currentForm.getRicercaComune().isPolo());
		String descr = this.getDescrizione(currentForm.getRicercaComune()
				.getCodSoggettario(), currentForm);
		currentForm.getRicercaComune().setDescSoggettario(descr);
		currentForm.setLivContr(dettDesGenVO.getLivAut());
		currentForm.setDid(dettDesGenVO.getDid());
		currentForm.setDidPadre((String) request
				.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
		currentForm.setFormaNome(dettDesGenVO.getFormaNome());

		currentForm.setNumSoggetti(currentForm.getNumSoggetti());
		currentForm.setEnableSoggetti((currentForm.getNumSoggetti() > 0));
		currentForm.setEnableUtilizzati((currentForm.getNumUtilizzati() > 0));
		currentForm
				.setEnableSoggettiIndice((currentForm.getNumSoggettiIndice() > 0));
		currentForm.setModificato(false);
		currentForm.setDescrittore(dettDesGenVO.getTesto());
		currentForm.setNote(dettDesGenVO.getNote());

		try {
			// ABILITO IL TASTO ELIMINA SOLO SE NON HO SOGGETTI COLLEGATI E NON
			// HO LEGAMI CON ALTRI
			// DESCRITTORI
			boolean cancella = (dettDesGenVO.isLivelloPolo()
					&& dettDesGenVO.getSoggettiCollegati() == 0 && dettDesGenVO
					.getSoggettiUtilizzati() == 0);
			AnaliticaSoggettoVO analitica = null;
			if (cancella) {
				// non avendo soggetti collegati posso variare anche il testo
				currentForm.setEnableManuale(true);
				analitica = SoggettiDelegate.getInstance(request).caricaReticoloDescrittore(
						dettDesGenVO.isLivelloPolo(), dettDesGenVO.getDid());
				if (analitica == null)
					return mapping.getInputForward();

				if (analitica.isEsitoOk()) {
					if (analitica.getReticolo().hasChildren()) {
						currentForm.setEnableElimina(false);
					} else {
						currentForm.setEnableElimina(true);
					}
				}
			}

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato

			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);

			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);

			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);

			return mapping.getInputForward();// gestione errori java
		}

		if (request.getAttribute(CANCELLA_IMMEDIATE) != null)
			return elimina(mapping, form, request, response);

		return mapping.getInputForward();

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		CreaVariaDescrittoreVO descrittore = new CreaVariaDescrittoreVO();
		try {

			descrittore.setDid(currentForm.getDid());
			DettaglioDescrittoreVO dettDesGenVO = currentForm.getDettDesGenVO();
			descrittore.setCodiceSoggettario(dettDesGenVO.getCampoSoggettario());
			descrittore.setTesto(currentForm.getDescrittore().trim());
			descrittore.setNote(currentForm.getNote());
			descrittore.setDataInserimento(currentForm.getDataInserimento());
			descrittore.setDataVariazione(currentForm.getDataModifica());
			descrittore.setLivelloAutorita(dettDesGenVO.getLivAut());
			descrittore.setFormaNome(currentForm.getFormaNome());
			descrittore.setT005(currentForm.getT005Ana());
			descrittore.setLivelloPolo(true);
			descrittore.setCondiviso(currentForm.isCondiviso());
			descrittore.setEdizioneSoggettario(dettDesGenVO.getEdizioneSoggettario());
			descrittore.setCategoriaTermine(dettDesGenVO.getCategoriaTermine());

			UserVO utenteCollegato = Navigation.getInstance(request)
					.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			descrittore = factory.getGestioneSemantica().variaDescrittore(
					descrittore, utenteCollegato.getTicket());
			ActionMessages errors = new ActionMessages();
			if (!descrittore.isEsitoOk()) {
				if (descrittore.isEsitoTrovatiSimili()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.duplicatoDe",
									descrittore.getDid()));
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				} else {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									descrittore.getTestoEsito()));
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				}
			}

		} catch (ValidationException e) {
			// errori indice
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getLivContr());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm
				.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataModifica());
		currentForm.getAreaDatiDettaglioOggettiVO()
				.getDettaglioDescrittoreGeneraleVO().setT005(
						descrittore.getT005());
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
				currentForm.isEnableAnaSog());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getProgr());
		if (currentForm.getCid() != null) {
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					currentForm.getCid());
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_CID);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.operOk"));
			this.setErrors(request, errors, null);
			return mapping.findForward("analiticasoggetto");
		} else {
			// analitica descrittore
			request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI,
					currentForm.getDidPadre());
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					currentForm.getDidPadre());
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_DID);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.operOk"));
			this.setErrors(request, errors, null);
			return mapping.findForward("analiticadescrittore");
		}

	}

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		try {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			RicercaComuneVO ricercaComune = new RicercaComuneVO();
			ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
					.getCodSoggettario());
			ricercaComune.setDescSoggettario(currentForm.getRicercaComune()
					.getDescSoggettario());
			ricercaComune.setPolo(currentForm.getRicercaComune().isPolo());
			RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
			ricDid.setDid(currentForm.getDid());
			ricercaComune.setRicercaSoggetto(ricDid);
			ricercaComune.setRicercaDescrittore(null);
			ricercaComune.setElemBlocco("10");
			delegate.eseguiRicerca(ricercaComune, mapping);
			RicercaSoggettoListaVO lista = delegate.getOutput();
			if (lista.isEsitoNonTrovato()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			if (!lista.isEsitoOk()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", lista
								.getTestoEsito()));
				this.saveErrors(request, errors);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

		} catch (Exception ex) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.NoServer"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		}

		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getLivContr());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm
				.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataModifica());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
				new OggettoRiferimentoVO(true, currentForm.getDettDesGenVO()
						.getDid(), currentForm.getDettDesGenVO().getTesto()));

		return Navigation.getInstance(request).goForward(
				mapping.findForward("listasintetica"));
	}

	public ActionForward utilizzati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		request.setAttribute("bibDiRicerca", currentForm.getBiblioteca());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		try {
			SoggettiDelegate ricerca = SoggettiDelegate.getInstance(request);
			RicercaComuneVO ricercaComune = new RicercaComuneVO();
			ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
					.getCodSoggettario());
			ricercaComune.setDescSoggettario(currentForm.getRicercaComune()
					.getDescSoggettario());
			ricercaComune.setPolo(currentForm.getRicercaComune().isPolo());
			// DEVO GESTIRE L'ACCESSO DIVERSIFICATO PER LIVELLO DI RICERCA
			// IMPOSTATO IN INPUT

			RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
			ricDid.setDid(currentForm.getDid());
			ricercaComune.setRicercaSoggetto(ricDid);
			ricercaComune.setRicercaDescrittore(null);
			ricercaComune.setElemBlocco("10");
			ricerca.eseguiRicerca(ricercaComune, mapping);
		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.NoServer"));

		}

		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getLivContr());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm
				.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataModifica());
		return mapping.findForward("listasintetica");
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.setErrors(request, errors, null);
		// nessun codice selezionato
		return mapping.getInputForward();
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		// Viene settato il token per le transazioni successive
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.cancDes"));
		this.setErrors(request, errors, null);
		this.saveToken(request);
		currentForm.setEnableConferma(true);
		this.preparaConferma(mapping, request);
		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();

		try {
			UserVO utenteCollegato = Navigation.getInstance(request)
					.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			factory.getGestioneSemantica().cancellaSoggettoDescrittore(
					currentForm.getRicercaComune().isPolo(),
					currentForm.getDid(), utenteCollegato.getTicket(),
					SbnAuthority.DE);

		} catch (ValidationException e) {
			// errori indice

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());

		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		this.setErrors(request, errors, null);
		return mapping.findForward("ricerca");

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
				currentForm.isEnableAnaSog());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getProgr());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, currentForm
				.getDid());
		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, currentForm
				.getCid());
		request.setAttribute(NavigazioneSemantica.DID_PADRE, currentForm
				.getDidPadre());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
				currentForm.getDettDesGenVO());
		request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
				currentForm.getDescrittore().trim());
		request.setAttribute(NavigazioneSemantica.NOTE_OGGETTO, currentForm
				.getNote());

		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getLivContr());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm
				.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());

		if (currentForm.getCid() != null) {
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					currentForm.getCid());
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_CID);
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
					currentForm.getDataIAna());
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					currentForm.getDataMAna());
			return mapping.findForward("analiticasoggetto");
		} else {
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					currentForm.getDidPadre());
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_DID);
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
					currentForm.getDataInserimento());
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					currentForm.getDataModifica());
			return mapping.findForward("analiticadescrittore");
		}

	}

	private void preparaConferma(ActionMapping mapping,
			HttpServletRequest request) {
		ActionMessages messages = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("button.parameter", mapping
				.getParameter());
		messages.add("gestionesemantica.parameter.conferma", msg1);
		this.saveMessages(request, messages);
	}

	private enum TipoAttivita {
		CANCELLA, SOGGETTI;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			GestioneDescrittoreForm currentForm = (GestioneDescrittoreForm) form;
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			DettaglioDescrittoreVO dettaglio = currentForm.getDettDesGenVO();
			switch (attivita) {

			case CANCELLA:
				if (!delegate.isSoggettarioGestito(dettaglio.getCampoSoggettario()))
					return false;
				if (!delegate.isLivAutOkDE(dettaglio.getLivAut(), false))
					return false;
				if (dettaglio.getSoggettiCollegati() > 0)
					return false;
				return delegate.isAbilitatoDE(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028);

			case SOGGETTI:
				return (!dettaglio.isRinvio());
			}
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}

	}

}
