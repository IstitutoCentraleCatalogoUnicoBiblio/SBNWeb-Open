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
package it.iccu.sbn.web.actions.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.RicercaClasseForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate.RicercaClasseResult;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RicercaClasseAction extends NavigationBaseAction {

	private static Log log = LogFactory.getLog(RicercaClasseAction.class);

	private String trovatolista = "trovatolista";
	private String nontrovato = "nontrovato";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.cerca", "cerca");
		map.put("button.crea", "crea");
		map.put("button.annulla", "annulla");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			RicercaClasseForm currentForm = (RicercaClasseForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica
				.loadComboSistemaClassificazione(ticket, true)); //!navi.isFirst() )); // se sono in soggettazione o trascina voglio solo i gestiti
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDewey());
			currentForm.setListaStatoControllo(CaricamentoComboSemantica
					.loadComboStato(null));
			currentForm.setListaOrdinamentoClasse(CaricamentoComboSemantica
					.loadComboOrdClasse());

			return true;
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return false;
		}
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ActionMessages errors = new ActionMessages();
		Navigation navi = Navigation.getInstance(request);

		if (!this.initCombo(request, form, navi.getUserTicket()))
			return mapping.getInputForward();

		RicercaClasseForm currentForm = (RicercaClasseForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse();

		OggettoRiferimentoVO oggRif =
			(OggettoRiferimentoVO) request.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
		if (oggRif != null)
			currentForm.setOggettoRiferimento(oggRif);

		try {
			String puntuale = (String) utenteEjb.getDefault(ConstantDefault.RIC_CLA_SIMBOLO_PUNTUALE);
			ricercaClasse.setPuntuale(Boolean.valueOf(puntuale));

			String elemBlocco = (String) utenteEjb.getDefault(ConstantDefault.RIC_CLA_ELEMENTI_BLOCCHI);
			ricercaClasse.setElemBlocco(elemBlocco);

			String ordinamento = (String) utenteEjb.getDefault(ConstantDefault.RIC_CLA_SIMBOLO_ORDINAMENTO);
			ricercaClasse.setOrdinamentoClasse(TipoOrdinamentoClasse.fromSbnMarcValue(ordinamento));

			String livello = (String) utenteEjb.getDefault(ConstantDefault.RIC_CLA_LIVELLO_POLO);
			ricercaClasse.setPolo(Boolean.valueOf(livello));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.default"));
			this.saveErrors(request, errors);
		}

		ricercaClasse.setLivelloAutoritaA("97");
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		RicercaClasseForm currentForm = (RicercaClasseForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		String chiamante = null;
		FolderType folder = null;
		boolean isPolo = false;

		this.saveToken(request);

		if (!currentForm.isSessione())
			this.loadDefault(request, mapping, form);

		if (!currentForm.isSessione() && !navi.isFirst()) {
			//almaviva5_20111021 evolutive CFI
			this.init(request, currentForm);
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (datiGB != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
				currentForm.getCatalogazioneSemanticaComune().setBid(datiGB.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(datiGB.getDescPartenza());
				currentForm.setFolder(folder);
			}

			if (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid()) ) {
				Boolean flgPolo = (Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO);
				isPolo = flgPolo != null ? flgPolo.booleanValue() : true;
				currentForm.getRicercaClasse().setPolo(isPolo);
			}

		} else
			// Posso creare solo la prima volta
			currentForm.setEnableCrea(false);

		currentForm.setSessione(true);
		currentForm.setAction(chiamante);
		currentForm.getRicercaClasse().setCodSistemaClassificazione("D");

		Boolean abilitaTrascina = (Boolean) request
				.getAttribute(NavigazioneSemantica.ABILITA_TRASCINAMENTO);
		if (abilitaTrascina != null)
			currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));

		currentForm.setNotazioneTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA));
		currentForm.setTestoTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
		currentForm.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
				.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		log.info("RicercaClasseAction::init");
		super.init(request, form);
		RicercaClasseForm currentForm = (RicercaClasseForm) form;
		ParametriThesauro paramThes = ParametriThesauro.retrieve(request);
		if (paramThes != null) {
			currentForm.setParametriThes(paramThes);
			OggettoRiferimentoVO rif = (OggettoRiferimentoVO) paramThes.get(ParamType.OGGETTO_RIFERIMENTO);
			if (rif == null)
				return;

			currentForm.setOggettoRiferimento(rif);
		}
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaClasseForm currentForm = (RicercaClasseForm) form;
		RicercaClassiVO ricercaClasse = (RicercaClassiVO) currentForm.getRicercaClasse().clone();
		ricercaClasse.setPolo(true);
		String deweyEd = null;

		String idClasse = ricercaClasse.getIdentificativoClasse();

		if (ValidazioneDati.isFilled(idClasse) ) {
			SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
			ricercaClasse.setCodSistemaClassificazione(sd.getSistema());
			ricercaClasse.setCodEdizioneDewey(sd.getEdizione());
			ricercaClasse.setSimbolo(sd.getSimbolo());

			deweyEd = CodiciProvider.unimarcToSBN(CodiciType.CODICE_EDIZIONE_CLASSE,
					ricercaClasse.getCodEdizioneDewey());
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY, deweyEd);

		}

		request.setAttribute(
				NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
				ricercaClasse.getCodSistemaClassificazione());
		if (ValidazioneDati.isFilled(ricercaClasse.getParole()) ) {
			request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
					ricercaClasse.getParole().trim());
		}

		if (deweyEd != null)
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY, deweyEd);
		else
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
					ricercaClasse.getCodEdizioneDewey());

		String descrEdizione = this.getDescrizioneEdizione(ricercaClasse
				.getCodEdizioneDewey().toUpperCase(), form);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
				descrEdizione);
		request.setAttribute(
				NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
				ricercaClasse.getDescSistemaClassificazione());
		if (ricercaClasse.getSimbolo() != null) {
			request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
					ricercaClasse.getSimbolo());
		} else {
			request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
					ricercaClasse.getSimbolo());
		}
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				ricercaClasse.isPolo());

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getFolder());
		if (currentForm.getNotazioneTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
					currentForm.getNotazioneTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
					currentForm.getTestoTrascinaDa().trim());
			request.setAttribute(
					NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
					currentForm.getDatiBibliografici());
		}

		OggettoRiferimentoVO oggRif = currentForm.getOggettoRiferimento();
		if (oggRif.isEnabled())
			request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO, oggRif);

		ParametriThesauro.send(request, currentForm.getParametriThes());
		return mapping.findForward(this.nontrovato);
	}

	// METODO PER OTTENERE DESCRIZIONE DEL SISTEMA CLASSIFICAZIONE DATO UN
	// CODICE
	// SISTEMA
	private String getDescrizione(String codice, ActionForm form) {
		RicercaClasseForm currentForm = (RicercaClasseForm) form;
		for (int index = 0; index < currentForm
				.getListaSistemiClassificazione().size(); index++) {
			ComboCodDescVO sist = (ComboCodDescVO) currentForm
					.getListaSistemiClassificazione().get(index);
			if (sist.getCodice().equals(codice))
				return sist.getDescrizione();
		}
		return "Non trovato";
	}

	// METODO PER OTTENERE DESCRIZIONE DEL SISTEMA CLASSIFICAZIONE DATO UN
	// CODICE
	// SISTEMA
	private String getDescrizioneEdizione(String codice, ActionForm form) {
		RicercaClasseForm currentForm = (RicercaClasseForm) form;
		for (int index = 0; index < currentForm.getListaEdizioni().size(); index++) {
			ComboCodDescVO edi = currentForm
					.getListaEdizioni().get(index);
			if (edi.getCodice().equals(codice))
				return edi.getDescrizione();
		}
		return "Non trovato";
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		RicercaClasseForm currentForm = (RicercaClasseForm) form;

		// Verifico che per sistemi di classificazione diversi da "D" l'edizione
		// sia uguale a space
		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse().copy();

		if (!ricercaClasse.getCodSistemaClassificazione().equals("D")) {
			if (ValidazioneDati.isFilled(ricercaClasse.getCodEdizioneDewey())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noIdentificativoValido"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (ricercaClasse.isPuntuale()) {
			if (ValidazioneDati.isFilled(ricercaClasse.getCodEdizioneDewey())
					&& ValidazioneDati.isFilled(ricercaClasse.getSimbolo())) {
				// OK
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noPuntuale"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (ValidazioneDati.isFilled(ricercaClasse.getCodEdizioneDewey())) {
			if (ValidazioneDati.isFilled(ricercaClasse.getSimbolo()) && ricercaClasse.isPuntuale())
				ricercaClasse.setIdentificativoClasse(ricercaClasse
						.getCodSistemaClassificazione()
						+ ricercaClasse.getCodEdizioneDewey() + ricercaClasse.getSimbolo());
		}

		// Verifico che per sistemi di classificazione diversi da "D" l'edizione
		// sia uguale a space all'interno del campo identificativo
		String idClasse = ricercaClasse.getIdentificativoClasse();

		if (ValidazioneDati.isFilled(idClasse)
				&& (!ricercaClasse.getCodSistemaClassificazione().equals("D"))) {
			if (!idClasse.matches(".\\s{1}.+")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noIdentificativoValido"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (ValidazioneDati.isFilled(idClasse)) {
			SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
			if (sd.getSistema().equals(ricercaClasse.getCodSistemaClassificazione())) {
				// OK
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noSistemaValido"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (ValidazioneDati.isFilled(idClasse)) {
			SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
			ricercaClasse.setCodSistemaClassificazione(sd.getSistema());
			ricercaClasse.setCodEdizioneDewey(sd.getEdizione());
		}

		try {
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			// Imposto la descrizione del sistema di classificazione e
			// dell'edizione

			String descrSistema = this.getDescrizione(ricercaClasse
					.getCodSistemaClassificazione().toUpperCase(), form);
			ricercaClasse.setDescSistemaClassificazione(descrSistema);

			delegate.eseguiRicerca(ricercaClasse, mapping);

			RicercaClasseResult op = delegate.getOperazione();
			String deweyEd = null;
			deweyEd = CodiciProvider.unimarcToSBN(
					CodiciType.CODICE_EDIZIONE_CLASSE,
					ricercaClasse.getCodEdizioneDewey());

			request.setAttribute(
					NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
					ricercaClasse.getCodSistemaClassificazione());
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
					deweyEd);
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
					new Boolean(ricercaClasse.isPolo()));

			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
					.getPath());
			request.setAttribute(
					NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
					currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
					currentForm.getFolder());
			request.setAttribute(
					NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
					ricercaClasse.getDescSistemaClassificazione());
			request.setAttribute(
					NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
					ricercaClasse.getDescEdizioneDewey());
			request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
					idClasse);
			request.setAttribute(NavigazioneSemantica.PAROLE, ricercaClasse
					.getParole());

			switch (op) {
			case analitica_1:// RicercaClasseDelegate.analitica:
				if (currentForm.getNotazioneTrascinaDa() != null) {
					request.setAttribute(
							NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
							currentForm.getNotazioneTrascinaDa());
					request.setAttribute(
							NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
							currentForm.getTestoTrascinaDa().trim());
					request.setAttribute(
							NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
							currentForm.getTitoliBiblio());
					request.setAttribute(
							NavigazioneSemantica.DATI_BIBLIOGRAFICI,
							currentForm.getDatiBibliografici());
				}

				OggettoRiferimentoVO oggRif = currentForm.getOggettoRiferimento();
				if (oggRif.isEnabled())
					request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO, oggRif);

				ParametriThesauro.send(request, currentForm.getParametriThes());
				return Navigation.getInstance(request).goForward(mapping.findForward(this.trovatolista));

			case crea_3:// RicercaClasseDelegate.crea:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);

				ricercaClasse.setCodEdizioneDewey(deweyEd);
				currentForm.setEnableCrea(ClassiDelegate.getInstance(request).isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017));

				if (ricercaClasse.getParole() != null
						|| ricercaClasse.getParole().length() > 0) {
					request.setAttribute(
							NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
							ricercaClasse.getParole().trim());
				}

				return this.loadDefault(request, mapping, form);
			case lista_2:// RicercaClasseDelegate.lista:
				if (currentForm.getNotazioneTrascinaDa() != null) {
					request.setAttribute(
							NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
							currentForm.getNotazioneTrascinaDa());
					request.setAttribute(
							NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
							currentForm.getTestoTrascinaDa().trim());
					request.setAttribute(
							NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
							currentForm.getTitoliBiblio());
					request.setAttribute(
							NavigazioneSemantica.DATI_BIBLIOGRAFICI,
							currentForm.getDatiBibliografici());
				}

				OggettoRiferimentoVO oggRif2 = currentForm.getOggettoRiferimento();
				if (oggRif2.isEnabled())
					request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO, oggRif2);

				ParametriThesauro.send(request, currentForm.getParametriThes());
				return Navigation.getInstance(request).goForward(mapping.findForward(this.trovatolista));

			case diagnostico_0:// RicercaClasseDelegate.diagnostico:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", delegate
								.getOutput().getTestoEsito()));
				this.saveErrors(request, errors);
				return this.loadDefault(request, mapping, form);
			default:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				this.saveErrors(request, errors);
				return this.loadDefault(request, mapping, form);
			}
		} catch (ValidationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
			}
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

}
