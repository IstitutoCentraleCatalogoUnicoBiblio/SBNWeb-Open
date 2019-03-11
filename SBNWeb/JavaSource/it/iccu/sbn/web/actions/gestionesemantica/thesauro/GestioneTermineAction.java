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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneOggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTerminiType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTitoloTermineType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.AnaliticaThesauroForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.GestioneTermineForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.actions.LookupDispatchAction;

public class GestioneTermineAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(GestioneTermineAction.class);

	private static String[] BOTTONIERA = new String[] {
		"button.ok", "button.trascina", "button.elimina", "button.stampa", "button.annulla" };
	private static String[] BOTTONIERA_ESAMINA = new String[] {
		"button.stampa", "button.annulla" };
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
		"button.no", "button.annulla" };
	private static String[] BOTTONIERA_FONDI = new String[] { "button.fondi",
		"button.no", "button.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.ok", "ok");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		map.put("button.trascina", "trascina");
		map.put("button.elimina", "elimina");
		map.put("button.biblio", "biblio");
		map.put("button.polo", "polo");

		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.fondi", "fondi");

		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");
		return map;
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form) {

		try {
			GestioneTermineForm currentForm = (GestioneTermineForm) form;
			currentForm.setListaThesauri(CaricamentoComboSemantica
					.loadComboThesauro(Navigation.getInstance(request).getUserTicket(),
							(currentForm.getModalita() != ModalitaGestioneType.ESAMINA)));

				currentForm
					.setListaLivelloAutorita(CaricamentoComboSemantica
							.loadComboStato((currentForm.getModalita() == ModalitaGestioneType.ESAMINA) ? null
										: ThesauroDelegate.getInstance(request).getMaxLivelloAutorita()));


			currentForm.setListaFormaNome(CaricamentoComboSemantica
					.loadComboFormaNome() );

			return true;

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, ex);
			return false;
		}
	}

	private void init(ActionMapping mapping, HttpServletRequest request,
			ActionForm form) {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		log.info("GestioneTermineAction::init");

		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		currentForm.setParametri(parametri);

		ModalitaGestioneType modalita = (ModalitaGestioneType) parametri.get(ParamType.MODALITA_GESTIONE_TERMINE);
		currentForm.setModalita(modalita);
		currentForm.setListaPulsanti(BOTTONIERA);

		initCombo(request, form);

		DettaglioTermineThesauroVO dettaglio = null;
		Navigation navi = Navigation.getInstance(request);

		switch (modalita) {
		case CREA_PER_LEGAME_TITOLO:
			navi.setTesto(".gestionesemantica.thesauro.GestioneTermine.CREA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneTermine.CREA.descrizione");

			AreaDatiPassBiblioSemanticaVO datiBibliografici =
				(AreaDatiPassBiblioSemanticaVO) parametri.get(ParamType.DATI_BIBLIOGRAFICI);
			currentForm.getCatalogazioneSemanticaComune().setBid(datiBibliografici.getBidPartenza());
			currentForm.getCatalogazioneSemanticaComune().setTestoBid(datiBibliografici.getDescPartenza());
			dettaglio = this.preparaDettaglioPerCreazione(parametri);
			break;

		case CREA_PER_TRASCINA_TITOLI:
			navi.setTesto(".gestionesemantica.thesauro.GestioneTermine.CREA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneTermine.CREA.descrizione");

			DatiFusioneTerminiVO datiFusione = (DatiFusioneTerminiVO) parametri.get(ParamType.DATI_FUSIONE_TERMINI);
			currentForm.setDatiLegame(datiFusione);
			dettaglio = this.preparaDettaglioPerCreazione(parametri);
			break;

		case CREA_PER_LEGAME_TERMINI:
			navi.setTesto(".gestionesemantica.thesauro.GestioneTermine.CREA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneTermine.CREA.descrizione");

			DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) parametri.get(ParamType.DATI_LEGAME_TERMINI);
			currentForm.setDatiLegame(datiLegame);
			dettaglio = this.preparaDettaglioPerCreazione(parametri);
			break;

		case CREA:
			navi.setTesto(".gestionesemantica.thesauro.GestioneTermine.CREA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneTermine.CREA.descrizione");
			dettaglio = this.preparaDettaglioPerCreazione(parametri);
			break;

		case GESTIONE:
			dettaglio = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);
			//conservo una copia del dettaglio originale
			currentForm.setDettaglioOld((DettaglioTermineThesauroVO) dettaglio.clone() );
			break;

		case ESAMINA:
			navi.setTesto(".gestionesemantica.thesauro.GestioneTermine.ESAMINA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneTermine.ESAMINA.descrizione");
			dettaglio = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);
			currentForm.setListaPulsanti(BOTTONIERA_ESAMINA);
			break;

		case CANCELLA:
			navi.setTesto(".gestionesemantica.thesauro.GestioneTermine.CANCELLA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneTermine.CANCELLA.descrizione");
			dettaglio = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);
			currentForm.setEnableConferma(true);
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.thesauro.cancThes"));
			this.setErrors(request, errors, null);
			break;

		default:
			break;
		}

		currentForm.setDettaglio(dettaglio);
		loadDefault(request, form);

		currentForm.setInitialized(true);
	}

	private void loadDefault(HttpServletRequest request, ActionForm form) {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		ModalitaGestioneType modalita = currentForm.getModalita();
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();

		try {
			switch (modalita) {
			case CREA_PER_LEGAME_TERMINI:
				String livAut = (String)utenteEjb.getDefault(ConstantDefault.CREA_THE_LIVELLO_AUTORITA);
				int maxLivAut = Integer.parseInt(ThesauroDelegate.getInstance(request).getMaxLivelloAutorita() );
				if (Integer.parseInt(livAut) <= maxLivAut )
					dettaglio.setLivAut(livAut);

				String forma = (String)utenteEjb.getDefault(ConstantDefault.CREA_THE_FORMA_TERMINE);
				dettaglio.setFormaNome(forma);

			default:
				break;
			}

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.default"));
			this.saveErrors(request, errors);
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;

		if (!currentForm.isInitialized())
			init(mapping, request, form);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri();

		currentForm.setComboGestioneEsamina(LabelGestioneSemantica
				.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
						request, form, new String[]{"TH"}, this));
		currentForm.setIdFunzioneEsamina("");

		//check trascina diretto da analitica
		Boolean forzaTrascina = (Boolean) parametri.get(ParamType.FORZA_TRASCINAMENTO);
		if (forzaTrascina != null && forzaTrascina) {
			navi.purgeThis();
			parametri.remove(ParamType.FORZA_TRASCINAMENTO);
			return trascina(mapping, form, request, response);
		}

		return mapping.getInputForward();
	}

	private DettaglioTermineThesauroVO preparaDettaglioPerCreazione(
			ParametriThesauro parametri) {

		DettaglioTermineThesauroVO dettaglio = new DettaglioTermineThesauroVO();
		dettaglio.setCodThesauro((String) parametri
				.get(ParamType.CODICE_THESAURO));
		dettaglio.setTesto((String) parametri
				.get(ParamType.TESTO_THESAURO));
		return dettaglio;
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();

		switch (currentForm.getModalita()) {
		case CREA:
			if (!eseguiCreazioneTermine(mapping, form, request,	response, parametri) )
				return mapping.getInputForward();

			ParametriThesauro.send(request, parametri);
			ActionForm callerForm = navi.getCallerForm();
			ActionForward forward = null;

			// se ho fatto creazione da analitica devo tornare indietro e non avanti
			if (callerForm instanceof AnaliticaThesauroForm )
				forward = navi.goBack(mapping.findForward("analiticaThesauro"));
			else
				forward = navi.goForward(mapping.findForward("analiticaThesauro"));

			navi.purgeThis();
			return forward;

		case CREA_PER_TRASCINA_TITOLI:
			if (!eseguiCreazioneTermine(mapping, form, request,	response, parametri) )
				return mapping.getInputForward();

			// imposto i dati del did arrivo
			DatiFusioneTerminiVO datiFusione = (DatiFusioneTerminiVO) parametri.get(ParamType.DATI_FUSIONE_TERMINI);
			DettaglioTermineThesauroVO dettaglioNuovoTermine = ((TreeElementViewSoggetti) parametri
					.get(ParamType.ANALITICA))
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTermineThesauroVO();
			datiFusione.setDid2(dettaglioNuovoTermine);

			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			AreaDatiPassaggioInterrogazioneTitoloReturnVO listaTitoliCollegatiBiblio =
				delegate.listaTitoliCollegatiBiblio(datiFusione.getDid1().getDid(), currentForm.getMaxRighe());

			if (listaTitoliCollegatiBiblio == null)
				return mapping.getInputForward();

			datiFusione.setTitoliCollegati(listaTitoliCollegatiBiblio);

			ParametriThesauro.send(request, parametri);
			navi.purgeThis();
			return navi.goForward(mapping.findForward("sinteticaTitoliFusione"));

		case CREA_PER_LEGAME_TERMINI:
			if (!eseguiCreazioneTermine(mapping, form, request,	response, parametri) )
				return mapping.getInputForward();

			// imposto i dati del did arrivo
			DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) parametri
					.get(ParamType.DATI_LEGAME_TERMINI);
			dettaglioNuovoTermine = ((TreeElementViewSoggetti) parametri
					.get(ParamType.ANALITICA))
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTermineThesauroVO();
			datiLegame.setDid2(dettaglioNuovoTermine);
			parametri.put(ParamType.MODALITA_LEGAME_TERMINI, ModalitaLegameTerminiType.CREA);
			ParametriThesauro.send(request, parametri);
			navi.purgeThis();
			return navi.goForward(mapping.findForward("creaLegameTermini"));

		case CREA_PER_LEGAME_TITOLO:
			if (!eseguiCreazioneTermine(mapping, form, request,	response, parametri) )
				return mapping.getInputForward();

			// imposto i dati del did arrivo
			DettaglioTermineThesauroVO dettaglioTermine = ((TreeElementViewSoggetti) parametri
					.get(ParamType.ANALITICA))
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTermineThesauroVO();
			parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglioTermine);
			parametri.put(ParamType.MODALITA_LEGAME_TITOLO_TERMINE, ModalitaLegameTitoloTermineType.CREA);
			ParametriThesauro.send(request, parametri);
			navi.purgeThis();
			return navi.goForward(mapping.findForward("inserisciLegameTitoloTermine"));

		case GESTIONE:
			if (!eseguiModificaTermine(mapping, form, request,	response, parametri) )
				return mapping.getInputForward();

			ParametriThesauro.send(request, parametri);
			return mapping.findForward("analiticaThesauro");

		case ESAMINA:
		default:
			return mapping.getInputForward();
		}

	}

	private boolean eseguiCreazioneTermine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, ParametriThesauro parametri) throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();

		ActionMessages errors = new ActionMessages();
		CreaVariaTermineVO nuovoTermine = null;

		try {
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

			nuovoTermine = delegate.gestioneTermine(dettaglio, TipoOperazioneOggetto.CREA);
			if (nuovoTermine == null)
				return false;

			boolean bidIsNull = ValidazioneDati.strIsNull(currentForm
					.getCatalogazioneSemanticaComune().getBid());
			if (nuovoTermine.isEsitoOk() && bidIsNull) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.operOk"));
				this.setErrors(request, errors, null);
			}

			if (!nuovoTermine.isEsitoOk()) {
				if (nuovoTermine.isEsitoTrovatiSimili() ) {
					if (bidIsNull) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.thesauro.duplicato",
								nuovoTermine.getDid()));
						this.setErrors(request, errors, null);
						return false;
					}
				} else {
					if (bidIsNull) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.incongruo",
								nuovoTermine.getTestoEsito()));
						this.setErrors(request, errors, null);
						return false;
					}
				}
			}

			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true, nuovoTermine.getDid() );
			if (analitica == null)
				return false;

			resetToken(request);
			parametri.put(ParamType.ANALITICA, analitica.getReticolo());
			request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, nuovoTermine.getDid());
			return true;

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato
			return false;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return false;

		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return false;

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return false;
		}

	}

	private boolean eseguiModificaTermine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, ParametriThesauro parametri) throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();

		ActionMessages errors = new ActionMessages();
		CreaVariaTermineVO termineModificato = null;

		try {
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

			termineModificato = delegate.gestioneTermine(dettaglio, TipoOperazioneOggetto.MODIFICA);
			if (termineModificato == null)
				return false;

			if (!termineModificato.isEsitoOk()) {
				if (termineModificato.isEsitoTrovatiSimili() ) {

					List<ElementoSinteticaThesauroVO> listaSimili = termineModificato.getListaSimili();
					if (listaSimili.size() > 1) {
						// errore troppi soggetti
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.incongruenzaThes"));
						this.saveErrors(request, errors);
						// nessun codice selezionato
						return false;
					}

					ElementoSinteticaThesauroVO termineTrovato = listaSimili.get(0);

					// il termine é legato a titoli?
					if (dettaglio.getNumTitoliBiblio() > 0 || dettaglio.getNumTitoliPolo() > 0) {
						//CONTROLLO FONDI
						if (!delegate.isAbilitato(CodiciAttivita.getIstance().FONDE_THESAURO_1300) ) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"error.authentication.non_abilitato"));
							this.setErrors(request, errors, null);
							return false;
						}

						// conferma a fusione
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.thesauro.fondi",
								termineTrovato.getDid()));
						this.setErrors(request, errors, null);
						currentForm.setListaPulsanti(BOTTONIERA_FONDI);
						currentForm.setEnableConferma(true);
						parametri.put(ParamType.DID_TARGET_FUSIONE, termineTrovato.getDid() );
						currentForm.setParametri(parametri);
						return false;

					}  else {	// non legato a titoli

						//CONTROLLO MODIFICA E CANCELLAZIONE
						if (!delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301) ||
								!delegate.isAbilitato(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028) ) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"error.authentication.non_abilitato"));
							this.saveErrors(request, errors);
							return false;
						}

						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.thesauro.trovatoECancella",
								termineTrovato.getDid()) );
						this.setErrors(request, errors, null);
						currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
						return false;
					}

				}

				//altro errore SBNMarc
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.SBNMarc", termineModificato.getTestoEsito() ));
				this.setErrors(request, errors, null);
				return false;

			}

			//tutto ok
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.operOk"));
			this.setErrors(request, errors, null);

			TreeElementViewSoggetti nodoSelezionato =
				(TreeElementViewSoggetti) parametri.get(ParamType.NODO_PARTENZA_LEGAME);
			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true, nodoSelezionato.getKey() );
			if (analitica == null)
				return false;

			// sostituisco il sotto reticolo aggiornato al nodo partenza del legame in modo
			// da poter aggiornare l'analitica senza ricaricare tutti i sotto-reticoli
			nodoSelezionato.adoptChildren(analitica.getReticolo() );
			nodoSelezionato.open();
			parametri.put(ParamType.ID_NODO_SELEZIONATO, nodoSelezionato.getRepeatableId() );
			// la radice dell'analitica è comunque quella originale
			parametri.put(ParamType.ANALITICA, nodoSelezionato.getRoot() );

			resetToken(request);
			//parametri.put(ThesauroParamType.ANALITICA, analitica.getReticolo());
			request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, termineModificato.getDid());
			return true;


		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato
			return false;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return false;

		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return false;

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return false;
		}

	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.setErrors(request, errors, null);

		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();

		switch (currentForm.getModalita()) {
		case CANCELLA:
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			CreaVariaTermineVO termine = delegate.gestioneTermine(dettaglio, TipoOperazioneOggetto.CANCELLA);
			if (termine == null)
				return mapping.getInputForward();

			ParametriThesauro parametri = currentForm.getParametri().copy();
			Navigation navi = Navigation.getInstance(request);

			TreeElementViewSoggetti nodoCancellato =
				(TreeElementViewSoggetti) parametri.get(ParamType.NODO_PARTENZA_LEGAME);

			if (nodoCancellato.isRoot()) {
				// ho cancellato la root, l'analitica non è più valida
				navi.getCache().getPreviousElement().setPurge(true);
				return navi.goBack(true);
			}

			// cancello il figlio dal padre
			TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoCancellato.getParent();
			padre.removeChild(nodoCancellato);
			parametri.put(ParamType.ANALITICA, padre.getRoot() );

			ParametriThesauro.send(request, parametri);
			return mapping.findForward("analiticaThesauro");
		}


		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.setErrors(request, errors, null);

		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		currentForm.setListaPulsanti(BOTTONIERA);
		currentForm.setEnableConferma(false);
		currentForm.setModalita(ModalitaGestioneType.GESTIONE);
		currentForm.getParametri().remove(ParamType.DID_TARGET_FUSIONE);
		return mapping.getInputForward();
	}

	public ActionForward fondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();

		//imposto i dati di partenza del legame
		DatiFusioneTerminiVO datiFusione = new DatiFusioneTerminiVO();
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglioOld();
		datiFusione.setDid1(dettaglio);

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AnaliticaThesauroVO analitica =
			delegate.ricaricaReticolo(true,	(String) parametri.get(ParamType.DID_TARGET_FUSIONE));

		if (analitica == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglioTermineScelto =
			(DettaglioTermineThesauroVO) analitica.getReticolo().getDettaglio();
		datiFusione.setDid2(dettaglioTermineScelto);

		AreaDatiPassaggioInterrogazioneTitoloReturnVO listaTitoliCollegatiBiblio =
			delegate.listaTitoliCollegatiBiblio(datiFusione.getDid1().getDid(), currentForm.getMaxRighe());

		if (listaTitoliCollegatiBiblio == null)
			return mapping.getInputForward();

		datiFusione.setTitoliCollegati(listaTitoliCollegatiBiblio);
		parametri.put(ParamType.DATI_FUSIONE_TERMINI, datiFusione);
		ParametriThesauro.send(request, parametri);

		return navi.goForward(mapping.findForward("sinteticaTitoliFusione"));
	}

	public ActionForward trascina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;

		ParametriThesauro parametri = currentForm.getParametri().copy();

		//imposto i dati di partenza del legame
		DatiFusioneTerminiVO datiFusione = new DatiFusioneTerminiVO();
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();
		datiFusione.setDid1(dettaglio);
		parametri.put(ParamType.DATI_FUSIONE_TERMINI, datiFusione);
		//parametri.put(ThesauroParamType.NODO_PARTENZA_LEGAME, dettaglio.getDid() );

		//modalità di ricerca per creazione legame
		parametri.put(ParamType.MODALITA_CERCA_TERMINE, ModalitaCercaType.TRASCINA_TITOLI);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("cercaTerminePerFusione"));
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		currentForm.setModalita(ModalitaGestioneType.CANCELLA);
		currentForm.setEnableConferma(true);
		currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.thesauro.cancThes"));
		this.setErrors(request, errors, null);

		return mapping.getInputForward();
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();
		UserVO utente = Navigation.getInstance(request).getUtente();
		ActionForward titoliCollegati =
			delegate.titoliCollegatiBiblio(utente.getCodBib(), dettaglio.getDid(), dettaglio.getTesto(), mapping, false);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();
		ActionForward titoliCollegati = delegate.titoliCollegatiPolo(dettaglio.getDid(), dettaglio.getTesto(), mapping, false);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();
		ActionForward titoliCollegati = delegate.titoliCollegatiPolo(dettaglio.getDid(), dettaglio.getTesto(), mapping, true);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
		}

		if (!checkAttivita(request, form, idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}


	private static enum TipoAttivita {

		TITOLI_COLL_BIBLIO,
		TITOLI_COLL_BIBLIO_FILTRO,
		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO;
	}



	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		boolean abilitato;
		GestioneTermineForm currentForm = (GestioneTermineForm) form;
		DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();
		ModalitaGestioneType modalita = currentForm.getModalita();

		try {
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			if (idCheck.equals("button.elimina") ) {
				if (modalita != ModalitaGestioneType.GESTIONE)
					return false;
				abilitato = delegate.isAbilitato(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028);
				if (!abilitato)
					return false;
				if (dettaglio.getNumTitoliBiblio() > 0 || dettaglio.getNumTitoliPolo() > 0)
					return false;
				return delegate.isLivAutOk(currentForm.getDettaglio().getLivAut(), false);
			}

			if (idCheck.equals("button.trascina") ) {
				if (modalita != ModalitaGestioneType.GESTIONE)
					return false;
				abilitato = delegate.isAbilitato(CodiciAttivita.getIstance().FONDE_THESAURO_1300);
				if (!abilitato)
					return false;
				if (dettaglio.getNumTitoliBiblio() == 0 && dettaglio.getNumTitoliPolo() == 0)
					return false;
				return delegate.isLivAutOk(currentForm.getDettaglio().getLivAut(), false);
			}

			//check altri tasti
			Map<String, String> mappedButtons = getKeyMethodMap();
			for (String button : mappedButtons.keySet() )
				if (button.equals(idCheck))
					return true;

			switch (modalita) {
			case CREA:
			case CREA_PER_LEGAME_TERMINI:
			case CREA_PER_LEGAME_TITOLO:
			case CREA_PER_TRASCINA_TITOLI:
				return false;
			}

			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			switch (attivita) {
			case TITOLI_COLL_BIBLIO:
			case TITOLI_COLL_BIBLIO_FILTRO:
				return (dettaglio.getNumTitoliBiblio() > 0);
			case TITOLI_COLL_POLO:
			case TITOLI_COLL_POLO_FILTRO:
				return (dettaglio.getNumTitoliPolo() > 0);
			}

			return false;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

}
