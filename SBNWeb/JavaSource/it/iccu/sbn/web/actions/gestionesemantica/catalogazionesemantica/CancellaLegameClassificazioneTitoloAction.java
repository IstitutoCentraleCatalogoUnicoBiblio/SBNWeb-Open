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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO.LegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CancellaLegameClassificazioneTitoloForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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

public class CancellaLegameClassificazioneTitoloAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(CancellaLegameClassificazioneTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.elimina", "elimina");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.chiudi", "chiudi");

		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form, String ticket) {

		try {
			CancellaLegameClassificazioneTitoloForm currentForm = (CancellaLegameClassificazioneTitoloForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica
							.loadComboSistemaClassificazione(ticket, false));
			currentForm.setListaEdizioni(CaricamentoComboSemantica
					.loadComboEdizioneDewey());
			currentForm.setListaStatoControllo(CaricamentoComboSemantica
					.loadComboStato(null));

			return true;
		} catch (Exception e) {

			LinkableTagUtils.resetErrors(request);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.Faild"));

			log.error("", e);
			// nessun codice selezionato
			return false;
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CancellaLegameClassificazioneTitoloForm currentForm = (CancellaLegameClassificazioneTitoloForm) form;
//		 setto il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()) {
			return mapping.getInputForward();
		}

		DettaglioClasseVO dettaglio = null;
		boolean isPolo = false;
		String chiamante = null;

		if (!currentForm.isSessione()) {
			log.info("CancellaLegameClassificazioneTitoloAction::unspecified");
			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();
			dettaglio = (DettaglioClasseVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE);
			chiamante = navi.getActionCaller();
			if (chiamante == null) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));

				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setDettClaGen(dettaglio);
			currentForm.setDataInserimento(currentForm.getDettClaGen()
					.getDataIns());
			currentForm.setDataModifica(currentForm.getDettClaGen()
					.getDataAgg());
			currentForm.getRicercaClasse().setPolo(isPolo);
			currentForm.getRicercaClasse().setCodSistemaClassificazione(
					currentForm.getDettClaGen().getCampoSistema());
			currentForm.getRicercaClasse().setCodEdizioneDewey(
					currentForm.getDettClaGen().getCampoEdizione());
			currentForm.setStatoControllo(currentForm.getDettClaGen()
					.getLivAut());
			currentForm.setIdentificativoClasse(currentForm
					.getDettClaGen().getIdentificativo());

			SimboloDeweyVO sd = SimboloDeweyVO.parse(currentForm.getDettClaGen().getIdentificativo());
			currentForm.setSimbolo(sd.getSimbolo());

			currentForm.setDescrizione(currentForm.getDettClaGen()
					.getDescrizione());
			currentForm.setUlterioreTermine(currentForm.getDettClaGen()
					.getUlterioreTermine());

			currentForm.setAction(chiamante);
			currentForm
					.setAreaDatiPassBiblioSemanticaVO((AreaDatiPassBiblioSemanticaVO) request
							.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA));
			currentForm.getCatalogazioneSemanticaComune().setBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getBidPartenza());
			currentForm.getCatalogazioneSemanticaComune().setTestoBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getDescPartenza());
			DettaglioTitoloParteFissaVO detTitoloPFissaVO = currentForm.getAreaDatiPassBiblioSemanticaVO()
					.getTreeElement().getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO()
					.getDetTitoloPFissaVO();
			currentForm.getCatalogazioneSemanticaComune().setLivAutBid(
					detTitoloPFissaVO.getLivAut());
			currentForm.getCatalogazioneSemanticaComune().setNaturaBid(
					detTitoloPFissaVO.getNatura());
			currentForm.getCatalogazioneSemanticaComune()
					.setTipoMaterialeBid(
							detTitoloPFissaVO.getTipoMat());
			currentForm.getCatalogazioneSemanticaComune().setT005(
					detTitoloPFissaVO.getVersione());
			//almaviva5_20181022 #6749
			TreeElementViewTitoli classe = (TreeElementViewTitoli) currentForm.getAreaDatiPassBiblioSemanticaVO()
			 	.getTreeElement().findElement(currentForm.getIdentificativoClasse());
			if (classe != null)
				currentForm.setNotaAlLegame(classe.getAreaDatiDettaglioOggettiVO().getDettaglioClasseGeneraleVO().getNotaAlLegame());

			if (!this.initCombo(request, form, navi.getUserTicket()));
				return mapping.getInputForward();

		}

		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CancellaLegameClassificazioneTitoloForm currentForm = (CancellaLegameClassificazioneTitoloForm) form;

		try {
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();




				// cancello la classificazione
				factory.getGestioneSemantica().cancellaClasse(
						currentForm.getRicercaClasse().isPolo(),
								currentForm.getIdentificativoClasse(),
						utenteCollegato.getTicket());



		} catch (ValidationException e) {
			// errori indice

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();

		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		}

		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneSemantica.operOk"));

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		return mapping.findForward("catalogazioneSemantica");
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CancellaLegameClassificazioneTitoloForm currentForm = (CancellaLegameClassificazioneTitoloForm) form;
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		currentForm.setFolder(FolderType.FOLDER_CLASSI);
		return mapping.findForward("catalogazioneSemantica");

	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CancellaLegameClassificazioneTitoloForm currentForm = (CancellaLegameClassificazioneTitoloForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		AreaDatiPassBiblioSemanticaVO datiGB =
			currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();

		try {
			DatiLegameTitoloClasseVO legame = new DatiLegameTitoloClasseVO();
			legame.setBid(currentForm.getCatalogazioneSemanticaComune()
					.getBid());
			legame.setBidNatura(currentForm
					.getCatalogazioneSemanticaComune().getNaturaBid());
			legame.setT005(detTitoloPFissaVO.getVersione() );
			legame.setBidLivelloAut(currentForm
					.getCatalogazioneSemanticaComune().getLivAutBid());
			legame.setBidTipoMateriale(currentForm
					.getCatalogazioneSemanticaComune().getTipoMaterialeBid());
			legame.setLivelloPolo(currentForm.getRicercaClasse().isPolo());
			LegameTitoloClasseVO legameTitCla = legame.new LegameTitoloClasseVO();
			legameTitCla.setIdentificativoClasse(currentForm.getIdentificativoClasse());
			legameTitCla.setCodSistemaClassificazione(currentForm.getRicercaClasse().getCodSistemaClassificazione());
			legameTitCla.setNotaLegame(currentForm.getNotaAlLegame());
			legame.getLegami().add(legameTitCla);
			DatiLegameTitoloClasseVO risposta = factory.getGestioneSemantica()
					.cancellaLegameTitoloClasse(legame,
							utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));

				return mapping.getInputForward();
			}

			//aggiorno timestamp del titolo soggettato
			datiGB.getTreeElement().setT005(risposta.getT005());
			detTitoloPFissaVO.setVersione(risposta.getT005());

			//almaviva5_20181022 #6749 elimino nota
			TreeElementViewTitoli classe = (TreeElementViewTitoli) datiGB.getTreeElement().findElement(currentForm.getIdentificativoClasse());
			if (classe != null)
				classe.getAreaDatiDettaglioOggettiVO().getDettaglioClasseGeneraleVO().setNotaAlLegame("");

			// titoli collegati al soggetto
			int titoli = currentForm.getDettClaGen().getNumTitoliPolo();
			if (titoli == 1) {
				// vuol dire che non c'è più alcun titolo legato alla classificazione
				// imposto la richiesta di cancellazione della classificazione

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.cancClaTit"));

				this.saveToken(request);
				currentForm.setEnableConferma(true);
				this.preparaConferma(mapping, request);
				return mapping.getInputForward();
			}

			// se la classificazione è legato ad altri titoli posso cancellare solo il
			// legame
			if (titoli > 0) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.operOk"));

				request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
				return mapping.findForward("catalogazioneSemantica");
			}

		} catch (ValidationException e) {
			// errori indice

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		currentForm.setFolder(FolderType.FOLDER_CLASSI);
		return mapping.findForward("catalogazioneSemantica");

	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	private void preparaConferma(ActionMapping mapping,
			HttpServletRequest request) {
		ActionMessages messages = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("button.parameter", mapping
				.getParameter());
		messages.add("gestionesemantica.parameter.conferma", msg1);
		this.saveMessages(request, messages);
	}

}
