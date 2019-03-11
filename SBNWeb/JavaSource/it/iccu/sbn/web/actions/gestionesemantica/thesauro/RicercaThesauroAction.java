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
package it.iccu.sbn.web.actions.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaDescrittoreVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.RicercaThesauroForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate.RicercaThesauroResult;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.HashMap;
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
import org.apache.struts.actions.LookupDispatchAction;

public class RicercaThesauroAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(RicercaThesauroAction.class);
	private String trovatolista = "trovatolista";
	private String nontrovato = "nontrovato";
	private String trovatolistaterminithesauro = "trovatolistaterminithesauro";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gestionesemantica.thesauro.bottone.cerca", "cerca");
		map.put("gestionesemantica.thesauro.bottone.crea", "crea");
		map.put("button.annulla", "indietro");
		return map;
	}

	protected void setErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form) throws Exception {

		try {
			RicercaThesauroForm currentForm = (RicercaThesauroForm) form;
			currentForm.setListaThesauri(CaricamentoComboSemantica
					.loadComboThesauro(Navigation.getInstance(request).getUserTicket(), true)); //(currentForm.getModalita() != ModalitaCercaType.CERCA) ));

			currentForm.setListaOrdinamentoDescrittore(CaricamentoComboSemantica
					.loadComboOrdDesc());

			currentForm.setListaRicercaTipo(CaricamentoComboSemantica
					.loadComboRicercaTipo());
			return true;

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, ex);
			return false;
		}
	}


	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ActionMessages errors = new ActionMessages();
		RicercaThesauroForm currentForm = (RicercaThesauroForm) form;
		if (!currentForm.isInitialized()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				ThRicercaComuneVO ricercaComune = currentForm.getRicercaComune();
				String blocchi = (String) utenteEjb.getDefault(ConstantDefault.RIC_THE_ELEMENTI_BLOCCHI);
				ricercaComune.setElemBlocco(blocchi);

				String puntuale = (String) utenteEjb.getDefault(ConstantDefault.RIC_THE_TERMINE_PUNTUALE);
				if (Boolean.valueOf(puntuale))
					ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_ESATTA);
				else
					ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_INIZIALE);

				String ordinamento = (String) utenteEjb.getDefault(ConstantDefault.RIC_THE_ORDINAMENTO);
				ricercaComune.setOrdinamentoDescrittore(TipoOrdinamento.fromSbnMarcValue(ordinamento));

			} catch (Exception e) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.default"));
				this.setErrors(request, errors, e);
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RicercaThesauroForm currentForm = (RicercaThesauroForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		String termineThesauro = null;
		String chiamante = null;
		FolderType folder = null;

		this.saveToken(request);

		if (!currentForm.isInitialized() && !Navigation.getInstance(request).isFirst()) {
			// prima volta
			termineThesauro = (String) request.getAttribute("termineThesauro");
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);

			currentForm.setTermineThesauro(termineThesauro);
			AreaDatiPassBiblioSemanticaVO datiBibliografici = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (datiBibliografici != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(datiBibliografici);
				currentForm.getCatalogazioneSemanticaComune().setBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getDescPartenza());
				currentForm.setFolder(folder);
			}

		} else {
			// Posso creare solo la prima volta
			currentForm.setEnableCrea(false);

		}

		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		if (parametri == null) {
			// parametri di ricerca a default
			parametri = new ParametriThesauro();
			parametri.put(ParamType.MODALITA_CERCA_TERMINE, ModalitaCercaType.CERCA);
			currentForm.setParametri(parametri);
			currentForm.setModalita(ModalitaCercaType.CERCA);
			//return mapping.getInputForward();
		}

		currentForm.setParametri(parametri);
		ModalitaCercaType modalita = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_TERMINE);
		currentForm.setModalita(modalita);

		if (!currentForm.isInitialized() ) {
			log.info("RicercaThesauroAction:unspecified");
			currentForm.setAction(chiamante);
			this.loadDefault(request, mapping, form);
			currentForm.getRicercaComune().setPolo(true);
			currentForm.setInitialized(true);
			if (!initCombo(request, form))
				return mapping.getInputForward();
		}

		switch (modalita) {
		case CREA_LEGAME_TERMINI:
			DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) parametri.get(ParamType.DATI_LEGAME_TERMINI);
			currentForm.setDatiLegame(datiLegame);
			//posso cercare solo nel thesauro del did di partenza
			currentForm.getRicercaComune().setCodThesauro(datiLegame.getDid1().getCodThesauro());
			currentForm.getRicercaComune().setRicercaTitoliCollegati(null);
			break;
		case CREA_LEGAME_TITOLO:
			AreaDatiPassBiblioSemanticaVO datiBibliografici = (AreaDatiPassBiblioSemanticaVO) parametri
					.get(ParamType.DATI_BIBLIOGRAFICI);
			currentForm.setAreaDatiPassBiblioSemanticaVO(datiBibliografici);
			currentForm.getRicercaComune().setRicercaTitoliCollegati(null);
			break;
		case TRASCINA_TITOLI:
			DatiFusioneTerminiVO datiFusione = (DatiFusioneTerminiVO) parametri.get(ParamType.DATI_FUSIONE_TERMINI);
			currentForm.setDatiLegame(datiFusione);
			//posso cercare solo nel thesauro del did di partenza
			currentForm.getRicercaComune().setCodThesauro(datiFusione.getDid1().getCodThesauro());
			currentForm.getRicercaComune().setRicercaTitoliCollegati(null);
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaThesauroForm currentForm = (RicercaThesauroForm) form;
		ActionMessages errors = currentForm.validate(mapping, request);
		if (!errors.isEmpty()) {
			this.setErrors(request, errors, null);
			this.saveToken(request);
			currentForm.getRicercaComune().setPolo(true);
			return mapping.getInputForward();
		}

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.CODICE_THESAURO, currentForm.getRicercaComune().getCodThesauro());
		parametri.put(ParamType.LIVELLO_POLO, currentForm.getRicercaComune().isPolo());
		parametri.put(ParamType.DATI_BIBLIOGRAFICI, currentForm.getAreaDatiPassBiblioSemanticaVO());
		switch (currentForm.getModalita()) {
		case CERCA:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA);
			break;
		case TRASCINA_TITOLI:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_TRASCINA_TITOLI);
			break;
		case CREA_LEGAME_TERMINI:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_LEGAME_TERMINI);
			break;
		case CREA_LEGAME_TITOLO:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_LEGAME_TITOLO);
			break;
		}

		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());

		if (!ValidazioneDati.strIsNull(currentForm.getRicercaComune().getRicercaThesauroDescrittore()
				.getTestoDescr())) {
			parametri.put(ParamType.TESTO_THESAURO, currentForm.getRicercaComune()
					.getRicercaThesauroDescrittore().getTestoDescr().trim());
		}

		ParametriThesauro.send(request, parametri);
		return Navigation.getInstance(request).goForward(mapping.findForward(this.nontrovato) );
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		RicercaThesauroForm currentForm = (RicercaThesauroForm) form;
		Navigation navi = Navigation.getInstance(request);
		ThRicercaComuneVO ricerca = currentForm.getRicercaComune();

		try {
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			// Imposto la descrizione del Thesaurus

			delegate.eseguiRicerca(ricerca);
			RicercaThesauroResult op = delegate.getOperazione();

			ParametriThesauro parametri = currentForm.getParametri().copy();
			parametri.put(ParamType.PARAMETRI_RICERCA, delegate.getParametri());
			parametri.put(ParamType.CODICE_THESAURO, currentForm.getRicercaComune()
					.getCodThesauro());
			parametri.put(ParamType.LIVELLO_POLO, new Boolean(currentForm
					.getRicercaComune().isPolo()));

			parametri.put(ParamType.DATI_BIBLIOGRAFICI, currentForm
					.getAreaDatiPassBiblioSemanticaVO());
			RicercaThesauroListaVO lista = delegate.getOutput();
			parametri.put(ParamType.OUTPUT_SINTETICA, lista);

			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
			ParametriThesauro.send(request, parametri);

			ActionForward forward = null;
			String testoTermine = currentForm.getRicercaComune()
					.getRicercaThesauroDescrittore().getTestoDescr();
			switch (op) {
			case analitica_1:// RicercaThesauroDelegate.analitica:
				if (currentForm.getRicercaComune().getOperazione() instanceof ThRicercaDescrittoreVO) {
					forward = mapping.findForward(this.trovatolista);
					return navi.goForward(forward);
				} else {
					int termini = (ricerca.getRicercaTitoliCollegati()).count();
					if (termini == 1) {}

					forward = mapping.findForward(this.trovatolistaterminithesauro);
					return navi.goForward(forward);
				}
			case crea_4:// RicercaThesauroDelegate.crea:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				if (currentForm.getRicercaComune().getOperazione() instanceof ThRicercaDescrittoreVO) {
					currentForm.setEnableCrea(delegate.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017));
				}

				if (!ValidazioneDati.strIsNull(testoTermine)) {
					parametri.put(ParamType.TESTO_THESAURO, testoTermine);
				}

				return mapping.getInputForward();

			case sintetica_3:// RicercaThesauroDelegate.sintetica:
				request.setAttribute("termini", currentForm.getRicercaComune()
						.getRicercaTitoliCollegati());
				forward = mapping.findForward(this.trovatolistaterminithesauro);
				return navi.goForward(forward);

			case lista_2:// RicercaThesauroDelegate.lista:
				request.setAttribute(NavigazioneSemantica.PAROLE, currentForm.getRicercaComune()
						.getRicercaThesauroDescrittore());
				forward = mapping.findForward(this.trovatolista);
				return navi.goForward(forward);

			case diagnostico_0:// RicercaThesauroDelegate.diagnostico:
				if (!lista.getEsito().equals("3001")) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", lista.getTestoEsito()));
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				} else {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.gestioneSemantica.nontrovato"));
					this.setErrors(request, errors, null);
					currentForm.setEnableCrea(delegate.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017));
					return mapping.getInputForward();
				}

			default:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();

			}
		} catch (ValidationException e) {

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
			return mapping.getInputForward();
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

}
