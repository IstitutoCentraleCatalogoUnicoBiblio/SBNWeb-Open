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
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CreaLegameClassificazioneTitoloForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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

public class CreaLegameClassificazioneTitoloAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(CreaLegameClassificazioneTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.lega", "lega");
		map.put("button.stampa", "stampa");
		map.put("button.invioIndice", "invioInIndice");
		map.put("button.chiudi", "chiudi");

		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			CreaLegameClassificazioneTitoloForm currentForm = (CreaLegameClassificazioneTitoloForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica.loadComboSistemaClassificazione(ticket, true));
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDewey());
			currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
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
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CreaLegameClassificazioneTitoloForm currentForm = (CreaLegameClassificazioneTitoloForm) form;
		// setto il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()) {
			return mapping.getInputForward();
		}

		DettaglioClasseVO dettaglio = null;
		boolean isPolo = false;

		String chiamante = null;

		if (!currentForm.isSessione()) {
			log.info("CreaLegameClassificazioneTitoloAction::unspecified");
			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();
			dettaglio = (DettaglioClasseVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE);
			chiamante = navi.getActionCaller();
			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
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
			currentForm.setSimbolo(
					currentForm
					.getDettClaGen().getSimbolo());
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
			currentForm.getCatalogazioneSemanticaComune().setLivAutBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getTreeElement().getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO()
							.getDetTitoloPFissaVO().getLivAut());
			currentForm.getCatalogazioneSemanticaComune().setNaturaBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getTreeElement().getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO()
							.getDetTitoloPFissaVO().getNatura());
			currentForm.getCatalogazioneSemanticaComune()
					.setTipoMaterialeBid(
							currentForm.getAreaDatiPassBiblioSemanticaVO()
									.getTreeElement()
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioTitoloCompletoVO()
									.getDetTitoloPFissaVO().getTipoMat());
			currentForm.getCatalogazioneSemanticaComune().setT005(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getTreeElement().getAreaDatiDettaglioOggettiVO()
							.getDettaglioTitoloCompletoVO()
							.getDetTitoloPFissaVO().getVersione());

			if (!this.initCombo(request, form, navi.getUserTicket()))
				return mapping.getInputForward();

		}

		return mapping.getInputForward();
	}

	public ActionForward lega(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CreaLegameClassificazioneTitoloForm currentForm = (CreaLegameClassificazioneTitoloForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		AreaDatiPassBiblioSemanticaVO datiGB =
			currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();

		ActionMessages errors = new ActionMessages();
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
			legame.setLivelloPolo(currentForm
					.getRicercaClasse().isPolo());
			LegameTitoloClasseVO legameTitCla = legame.new LegameTitoloClasseVO();
			legame.getLegami().add(legameTitCla);
			legameTitCla.setCodSistemaClassificazione(currentForm.getRicercaClasse().getCodSistemaClassificazione());
			legameTitCla.setIdentificativoClasse(currentForm
					.getIdentificativoClasse());
			legameTitCla.setNotaLegame(currentForm.getNotaAlLegame());
			DatiLegameTitoloClasseVO risposta = factory
					.getGestioneSemantica()
					.creaLegameTitoloClasse(legame, utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//aggiorno timestamp del titolo soggettato
			datiGB.getTreeElement().setT005(risposta.getT005());
			detTitoloPFissaVO.setVersione(risposta.getT005() );

			//almaviva5_20181022 #6749 aggiorno nota
			TreeElementViewTitoli classe = (TreeElementViewTitoli) datiGB.getTreeElement().findElement(currentForm.getIdentificativoClasse());
			if (classe != null)
				classe.getAreaDatiDettaglioOggettiVO().getDettaglioClasseGeneraleVO().setNotaAlLegame(currentForm.getNotaAlLegame());

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
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

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);
		return mapping.findForward("catalogazioneSemantica");
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
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
		this.saveErrors(request, errors);
		// nessun codice selezionato
		return mapping.getInputForward();
	}
}
