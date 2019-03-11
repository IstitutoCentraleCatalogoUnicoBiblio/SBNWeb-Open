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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AnaliticaSoggettoForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.CreaSoggettoForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

public class CreaSoggettoAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(CreaSoggettoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.salvaSog", "crea");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private void initCombo(HttpServletRequest request, ActionForm form) throws Exception {
		CreaSoggettoForm currentForm = (CreaSoggettoForm) form;
		String ticket = Navigation.getInstance(request).getUserTicket();
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		//almaviva5_20111125 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));

		currentForm.setListaStatoControllo(CaricamentoComboSemantica
				.loadComboStato(SoggettiDelegate.getInstance(request).getMaxLivelloAutoritaSO()));

		currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		CreaSoggettoForm currentForm = (CreaSoggettoForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		currentForm.setNumNotiziePolo(0);
		currentForm.setNumNotizieBiblio(0);
		String currentFormCodice = null;
		String currentFormDescrizione = null;
		String currentFormTesto = null;
		String chiamante = null;
		boolean isPolo = false;
		RicercaSoggettoListaVO currentFormLista = null;

		String dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
		String dataVariazione = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
		FolderType folder = null;

		if (!currentForm.isSessione()) {
			log.info("CreaSoggettoAction::unspecified");
			currentFormCodice = (String) request.getAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO);
			currentFormDescrizione = (String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO);
			currentFormTesto = (String) request.getAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE);
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			currentFormLista = (RicercaSoggettoListaVO) request.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();
			folder = (FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);

			if (currentFormCodice == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
				return mapping.getInputForward();
			}

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}

			loadDefault(request, form);
			currentForm.setSessione(true);
			AreaDatiPassBiblioSemanticaVO test = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (test != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(test);
				currentForm.getCatalogazioneSemanticaComune().setBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getDescPartenza());
				currentForm.setFolder(folder);
			}

			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
				currentForm.setEnableTit(true);
			}
			currentForm.setOutputLista(currentFormLista);
			currentForm.setAction(chiamante);
			if (ValidazioneDati.isFilled(currentFormTesto) )
				currentForm.setTesto(currentFormTesto.trim());

			currentForm.getRicercaComune().setCodSoggettario(currentFormCodice);
			String edizione = (String) request.getAttribute(NavigazioneSemantica.EDIZIONE_SOGGETTARIO);
			currentForm.setEdizione(edizione);
			currentForm.getRicercaComune().setEdizioneSoggettario(edizione);
			currentForm.getRicercaComune().setPolo(isPolo);
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		try {
			this.initCombo(request, currentForm);

		} catch (Exception ex) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
		}

		currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
						.getAttribute(NavigazioneSemantica.ANALITICA));
		currentForm.setCategoriaSoggetto((String) request
				.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO));
		currentForm.setLivelloAutorita((String) request
				.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA));
		currentForm.setCodiceA(currentFormCodice);
		currentForm.setDescrizioneA(currentFormDescrizione);
		currentForm.setDataInserimentoA(dataInserimento);
		currentForm.setDataModificaA(dataVariazione);
		currentForm
				.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
						.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
		currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
		currentForm.setCidTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
		currentForm.setTestoTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));

		return mapping.getInputForward();
	}

	private void loadDefault(HttpServletRequest request, ActionForm form) {

		CreaSoggettoForm currentForm = (CreaSoggettoForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try {
			String tipoSogg = (String)utenteEjb.getDefault(ConstantDefault.CREA_SOG_TIPO_SOGG);
			currentForm.setCodTipoSoggetto(tipoSogg);
			String livAut = (String)utenteEjb.getDefault(ConstantDefault.CREA_SOG_LIVELLO_AUTORITA);
			currentForm.setCodStatoControllo(livAut);

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.default"));
		}
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CreaVariaSoggettoVO soggetto = null;
		CreaVariaSoggettoVO richiesta = new CreaVariaSoggettoVO();

		CreaSoggettoForm currentForm = (CreaSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		try {
			richiesta.setCodiceSoggettario(currentForm.getRicercaComune().getCodSoggettario());
			richiesta.setTesto(currentForm.getTesto().trim());
			richiesta.setLivello(currentForm.getCodStatoControllo());
			richiesta.setTipoSoggetto(currentForm.getCodTipoSoggetto());
			richiesta.setNote(currentForm.getNote());
			richiesta.setLivelloPolo(true);

			//almaviva5_20111125 evolutive CFI
			richiesta.setEdizioneSoggettario(currentForm.getEdizione());

			UserVO utenteCollegato = navi.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			soggetto = factory.getGestioneSemantica().creaSoggetto(richiesta, utenteCollegato.getTicket());

			if (soggetto.isEsitoOk()
					&& (ValidazioneDati.strIsNull(currentForm
							.getCatalogazioneSemanticaComune().getBid()) && ValidazioneDati
							.strIsNull(currentForm.getCidTrascinaDa()))) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
			}

			if (!soggetto.isEsitoOk()) {
				switch (soggetto.getEsitoType()) {
				case TROVATI_SIMILI:
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.duplicato", soggetto.getCid()));
					return mapping.getInputForward();

				case TROVATO_SIMILE_EDIZIONE_DIVERSA:
					ElementoSinteticaSoggettoVO simile = ValidazioneDati.first(soggetto.getListaSimili());
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.duplicato.edizione", simile.getCid(),
						CodiciProvider.cercaDescrizioneCodice(simile.getEdizioneSoggettario(), CodiciType.CODICE_EDIZIONE_SOGGETTARIO,
								CodiciRicercaType.RICERCA_CODICE_SBN)));
					return mapping.getInputForward();

				//almaviva5_20130612
				case ID_ESISTENTE:
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", soggetto.getTestoEsito()));
					return mapping.getInputForward();

				//almaviva5_20140103 #5463
				case DESCRITTORE_ESISTE_COME_RINVIO:
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", soggetto.getTestoEsito()));
					return mapping.getInputForward();

				//almaviva5_20160404 #6144
				case DESCRITTORE_CON_LEGAMI_STORICI:
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", soggetto.getTestoEsito()));
					return mapping.getInputForward();

				default:
					//almaviva5_20090915 #3154
					boolean testBid = ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())
							&& ValidazioneDati.strIsNull(currentForm.getCidTrascinaDa());
					if (soggetto.isEsitoEsisteComeRinvio() || testBid
							|| soggetto.getEsitoType() == SbnMarcEsitoType.SOGGETTARIO_NON_ABILITATO) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", soggetto.getTestoEsito()));
						return mapping.getInputForward();
					}
				}
			}

		} catch (ValidationException e) {
			// errori indice
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}

		currentForm.setNumNotiziePolo(0);
		currentForm.setNumNotizieBiblio(0);
		currentForm.setDescrizione(currentForm.getDescrizione());
		currentForm.setCodStatoControllo(currentForm.getCodStatoControllo());
		currentForm.setCodTipoSoggetto(currentForm.getCodTipoSoggetto());
		currentForm.setTesto(currentForm.getTesto());
		currentForm.setNote(currentForm.getNote());
		currentForm.setCid(soggetto.getCid());
		currentForm.setSogInserimento(currentForm.getBiblioteca());
		currentForm.setSogModifica(currentForm.getBiblioteca());
		currentForm.setDataInserimento(soggetto.getDataInserimento());
		currentForm.setDataModifica(soggetto.getDataVariazione());
		currentForm.getRicercaComune().setPolo(true);

		currentForm.setT005(soggetto.getT005());

		if (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())) {
			String xid = currentForm.getCid();
			currentForm.getRicercaComune().setPolo(true);
			String tipo = NavigazioneSemantica.TIPO_OGGETTO_CID;
			this.ricaricaReticolo(xid, tipo, currentForm, request);
		}

		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, currentForm.getCid());
		currentForm.setEnableNumPolo(false);
		request.setAttribute("isNumPolo", currentForm.isEnableNumPolo());
		currentForm.setEnableNumBiblio(false);
		request.setAttribute("isNumBiblio", currentForm.isEnableNumBiblio());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm
				.getOutputLista());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
				currentForm.getTesto());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm
				.getCodTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataModifica());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getCodStatoControllo());

		if (currentForm.getCidTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA,
					currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
					currentForm.getTestoTrascinaDa().trim());
			request.setAttribute(
					NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
					currentForm.getDatiBibliografici());
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO,
					soggetto.getCid());
			if (soggetto.getTesto() != null)
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, soggetto
								.getTesto().trim());
			else
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, currentForm
								.getTesto().trim());

			navi.purgeThis();
			return navi.goForward(mapping.findForward("ok"));
		}

		if (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())) {
			ActionForward forward = mapping.findForward("analiticaSoggetto");
			navi.purgeThis();
			// se esiste già un'analitica recupero quella
			if (navi.getCallerForm() instanceof AnaliticaSoggettoForm)
				return forward;
			else
				return navi.goForward(forward);

		} else {
			request.setAttribute(
					NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
					currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
					currentForm.getFolder());

			AnaliticaSoggettoVO reticolo = SoggettiDelegate.getInstance(request).caricaReticoloSoggetto(true, soggetto.getCid() );
			if (reticolo == null)
				return mapping.getInputForward();
			DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) reticolo.getReticolo().getDettaglio();
			request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, dettaglio.copy() );
			navi.purgeThis();
			return navi.goForward(mapping.findForward("creaLegameSoggettoTitolo"));
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

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	private void ricaricaReticolo(String xid, String tipo, ActionForm form,
			HttpServletRequest request) throws Exception {

		if (tipo.equals(NavigazioneSemantica.TIPO_OGGETTO_CID)) {
			String cid = xid;
			// chiamata a EJB con tipo oggetto padre a CID
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			AnaliticaSoggettoVO analitica = null;
			try {
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerCid(
								((CreaSoggettoForm) form).getRicercaComune()
										.isPolo(), cid,
								utenteCollegato.getTicket());

			} catch (ValidationException e) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return;

			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return;

			} catch (InfrastructureException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return;

			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
				log.error("", e);
				return;
			}

			if (!analitica.isEsitoOk())
				// gestione errori SBNMarc
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", analitica.getTestoEsito()));

			((CreaSoggettoForm) form).setTreeElementViewSoggetti(analitica.getReticolo());
			request.setAttribute(NavigazioneSemantica.ANALITICA,
					((CreaSoggettoForm) form).getTreeElementViewSoggetti());
			String livelloAut = analitica.getReticolo().getLivelloAutorita();
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
					livelloAut);
			String dataInserimento = analitica.getDataInserimento();
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
					dataInserimento);
			String dataVariazione = analitica.getDataVariazione();
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					dataVariazione);
			String tipoSoggetto = analitica.getReticolo()
					.getCategoriaSoggetto();
			request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
					tipoSoggetto);

		}
	}
}
