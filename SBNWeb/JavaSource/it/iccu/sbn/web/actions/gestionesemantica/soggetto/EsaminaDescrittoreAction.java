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

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.EsaminaDescrittoreForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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

public class EsaminaDescrittoreAction extends LookupDispatchAction implements
		SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(EsaminaDescrittoreAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.gestione", "gestione");
		map.put("button.soggetti", "soggetti");
		map.put("button.utilizzati", "utilizzati");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private void initCombo(String ticket, ActionForm form) throws Exception {
		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
		//almaviva5_20111127 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
		currentForm.setListaCategoriaTermine(CaricamentoComboSemantica.loadComboCategoriaSogDes(SbnAuthority.DE));
	}

	private String getDescrizione(String codice, ActionForm form) {
		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;
		for (int index = 0; index < currentForm.getListaSoggettari().size(); index++) {
			ComboCodDescVO sog = (ComboCodDescVO) currentForm
					.getListaSoggettari().get(index);
			if (sog.getCodice().equals(codice))
				return sog.getDescrizione();
		}
		return "Non trovato";
	}

	private void loadComboFormaNome(ActionForm form) throws Exception {
		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;
		currentForm.setListaFormaNome(CaricamentoComboSemantica
				.loadComboFormaNome());
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		String dataInserimento = null;
		String dataModifica = null;
		String dataInsAnalitica = null;
		String dataModAnalitica = null;
		boolean isManuale = false;
		String chiamante = null;
		String cid = null;
		String T005 = null;
		DettaglioDescrittoreVO dettaglio = null;
		boolean isPolo = false;
		String tipoSoggetto = null;
		String testoSoggetto = null;
		String note = null;
		boolean enableAnaSog = false;
		RicercaSoggettoListaVO ricSoggListaDescr = null;

		if (!currentForm.isSessione()) {
			log.info("EsaminaDescrittoreAction::unspecified");
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
			tipoSoggetto = (String) request
					.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			testoSoggetto = (String) request
					.getAttribute(NavigazioneSemantica.TESTO_SOGGETTO);
			isManuale = ((Boolean) request
					.getAttribute(NavigazioneSemantica.DESCRITTORE_MANUALE))
					.booleanValue();
			T005 = (String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA_T005);
			enableAnaSog = ((Boolean) request
					.getAttribute(NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO))
					.booleanValue();
			ricSoggListaDescr = (RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescr");
			note = dettaglio.getNote();

			if (cid != null) {
				// Vengo da analitica soggetto
				currentForm.setCid(cid);
			} else {
				// Vengo da analitica descrittore
				currentForm.setDidPadre((String) request
						.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
			}

			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			currentForm.setSessione(true);
			currentForm.setAction(chiamante);
			ActionMessages errors = currentForm.validate(mapping, request);

			try {
				this.initCombo(Navigation.getInstance(request).getUserTicket(), currentForm);
				this.loadComboFormaNome(currentForm);
				currentForm.setListaLivelloAutorita(CaricamentoComboSemantica.loadComboStato(null));
			} catch (Exception ex) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
			}
			this.saveErrors(request, errors);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDataIns(dataInsAnalitica);
			currentForm.setDataMod(dataModAnalitica);
			currentForm.setDettDesGenVO(dettaglio);
			currentForm.getRicercaComune().setPolo(isPolo);
			currentForm.setTipoSoggetto(tipoSoggetto);
			currentForm.setTestoSoggetto(testoSoggetto);
			currentForm.setEnableAnaSog(enableAnaSog);
			currentForm.setEnableManuale(isManuale);
			currentForm.setT005(T005);
			currentForm.setNote(note);
			currentForm.setOutputDescrittori(ricSoggListaDescr);
			currentForm
					.setProgr((String) request
							.getAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO));

		}

		currentForm.setNumUtilizzati(0);
		currentForm.setNumSoggettiIndice(1);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.getRicercaComune().setCodSoggettario(
				currentForm.getDettDesGenVO().getCampoSoggettario());
		String descr = this.getDescrizione(currentForm.getRicercaComune()
				.getCodSoggettario(), currentForm);
		currentForm.getRicercaComune().setDescSoggettario(descr);
		currentForm.setLivContr(currentForm.getDettDesGenVO().getLivAut());
		currentForm.setDid(currentForm.getDettDesGenVO().getDid());
		currentForm.setDescrittore(currentForm.getDettDesGenVO().getTesto());
		// INSERISCO RICHIESTA NUMERO TOTALE SOGGETTI COLLEGATI AL DID
		// ContaSoggettiCollegatiVO contaSoggetti;
		// UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		// FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//
		// contaSoggetti = factory.getGestioneSemantica()
		// .contaSoggettiPerDidCollegato(
		// currentForm.getRicercaComune().isPolo(), currentForm.getDid(),
		// utenteCollegato.getTicket());
		//
		// if (contaSoggetti.isEsitoOk() ) {
		// currentForm.setNumSoggetti(contaSoggetti.getTotRighe());
		// }
		currentForm.setEnableSoggetti((currentForm.getNumSoggetti() > 0));
		currentForm.setEnableUtilizzati((currentForm.getNumUtilizzati() > 0));
		currentForm
				.setEnableSoggettiIndice((currentForm.getNumSoggettiIndice() > 0));
		currentForm.setEnableGestione(currentForm.getRicercaComune().isPolo()
				&& (currentForm.getNumSoggetti() == 0));
		currentForm
				.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
						.getAttribute(NavigazioneSemantica.ANALITICA));
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, currentForm
				.getDid());
		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, currentForm
				.getCid());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataIns());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataMod());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataModifica());
		request.setAttribute(NavigazioneSemantica.DID_PADRE, currentForm
				.getDidPadre());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
				currentForm.getDettDesGenVO());
		request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
				currentForm.getDescrittore());
		request.setAttribute(NavigazioneSemantica.NOTE_OGGETTO, currentForm
				.getNote());
		request.setAttribute(NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
				currentForm.isEnableAnaSog());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getProgr());
		request.setAttribute(NavigazioneSemantica.DESCRITTORE_MANUALE,
				currentForm.isEnableManuale());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005,
				currentForm.getT005());
		request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI,
				currentForm.getDidPadre());
		request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE,
				currentForm.getDettDesGenVO().isCondiviso());

		// Vengo da analitica soggetto
		if (currentForm.getAction().equals(
				"/gestionesemantica/soggetto/AnaliticaSoggetto")) {
			request.setAttribute(NavigazioneSemantica.TESTO_SOGGETTO,
					currentForm.getTestoSoggetto());
		}
		Navigation navi = Navigation.getInstance(request);
		navi.purgeThis();
		return navi.goForward(mapping.findForward("gestione"));
	}

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;
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

		EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;
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
		this.saveErrors(request, errors);
		// nessun codice selezionato
		return mapping.getInputForward();
	}

	private enum TipoAttivita {
		MODIFICA, SOGGETTI;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			EsaminaDescrittoreForm currentForm = (EsaminaDescrittoreForm) form;
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			DettaglioDescrittoreVO dettaglio = currentForm.getDettDesGenVO();
			switch (attivita) {

			case MODIFICA:
				if (!delegate.isSoggettarioGestito(dettaglio.getCampoSoggettario()))
					return false;
				if (!delegate.isLivAutOkDE(dettaglio.getLivAut(), false))
					return false;
				if (!dettaglio.isLivelloPolo()) // no in indice
					return false;
				return delegate.isAbilitatoDE(CodiciAttivita.getIstance().MODIFICA_SOGGETTO_1265);

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
