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
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO.LegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CreaLegameSoggettoTitoloForm;
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

public class CreaLegameSoggettoTitoloAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(CreaLegameSoggettoTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.lega", "lega");
		map.put("button.stampa", "stampa");
		map.put("button.chiudi", "chiudi");

		return map;
	}

	private void initCombo(String ticket, ActionForm form) throws Exception {
		CreaLegameSoggettoTitoloForm currentForm = (CreaLegameSoggettoTitoloForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
		currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
		//almaviva5_20111123 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CreaLegameSoggettoTitoloForm currentForm = (CreaLegameSoggettoTitoloForm) form;
		// setto il token per le transazioni successive
		this.saveToken(request);
		// if (Navigation.getInstance(request).isFromBar() ) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		String codice = null;
		String descrizione = null;
		String cid = null;
		String testo = null;
		String tipoSoggetto = null;
		String statoControllo = null;
		String chiamante = null;
		String dataInserimento = null;
		String dataModifica = null;

		if (!currentForm.isSessione()) {
			log.info("CreaLegameSoggettoTitoloAction::unspecified");
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			codice = (String) request.getAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO);
			descrizione = (String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO);
			cid = (String) request.getAttribute(NavigazioneSemantica.CID_RIFERIMENTO);
			testo = (String) request.getAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE);
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			statoControllo = (String) request.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA);
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) request.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);

			chiamante = Navigation.getInstance(request).getActionCaller();
			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setDettaglio(dettaglio);
			currentForm.getRicercaComune().setCodSoggettario(codice);
			currentForm.getRicercaComune().setDescSoggettario(descrizione);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
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
			DettaglioTitoloParteFissaVO detTitoloPFissaVO =
				currentForm.getAreaDatiPassBiblioSemanticaVO()
					.getTreeElement().getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO()
					.getDetTitoloPFissaVO();

			currentForm.getCatalogazioneSemanticaComune().setLivAutBid(
					detTitoloPFissaVO.getLivAut());
			currentForm.getCatalogazioneSemanticaComune().setNaturaBid(
					detTitoloPFissaVO.getNatura());
			currentForm.getCatalogazioneSemanticaComune().setTipoMaterialeBid(
					detTitoloPFissaVO.getTipoMat());
			currentForm.getCatalogazioneSemanticaComune().setT005(
					detTitoloPFissaVO.getVersione());

			ActionMessages errors = new ActionMessages();
			try {
				this.initCombo(Navigation.getInstance(request).getUserTicket(), currentForm);
				currentForm.setStatoControllo(statoControllo);
				currentForm.setTipoSoggetto(tipoSoggetto);

			} catch (Exception e) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.saveErrors(request, errors);
				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

		}

		currentForm.getCatalogazioneSemanticaComune().setPolo(
				((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue());
		currentForm.setCid(cid);
		currentForm.setTesto(testo);

		return mapping.getInputForward();
	}



	public ActionForward lega(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CreaLegameSoggettoTitoloForm currentForm = (CreaLegameSoggettoTitoloForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		ActionMessages errors = new ActionMessages();
		AreaDatiPassBiblioSemanticaVO datiGB =
			currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();
		try {
			String cid = currentForm.getDettaglio().getCid();

			DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();
			legame.setBid(currentForm.getCatalogazioneSemanticaComune().getBid());
			legame.setBidNatura(currentForm.getCatalogazioneSemanticaComune().getNaturaBid());
			legame.setT005(detTitoloPFissaVO.getVersione() );
			legame.setBidLivelloAut(currentForm.getCatalogazioneSemanticaComune().getLivAutBid());
			legame.setBidTipoMateriale(currentForm.getCatalogazioneSemanticaComune().getTipoMaterialeBid());
			legame.setLivelloPolo(currentForm.getCatalogazioneSemanticaComune().isPolo());

			LegameTitoloSoggettoVO datiLegame = legame.new LegameTitoloSoggettoVO(cid, currentForm.getNotaAlLegame());
			//almaviva5_20120508 evolutive CFI
			datiLegame.setRank((short) (datiGB.getCountLegamiSoggetti() + 1));

			legame.getLegami().add(datiLegame);
			DatiLegameTitoloSoggettoVO risposta = factory.getGestioneSemantica()
					.creaLegameTitoloSoggetto(legame,
							utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//aggiorno timestamp del titolo soggettato
			datiGB.getTreeElement().setT005(risposta.getT005());
			detTitoloPFissaVO.setVersione(risposta.getT005());
			//aggiorno nota
			TreeElementViewTitoli titolo = (TreeElementViewTitoli) datiGB.getTreeElement().findElement(cid);
			if (titolo != null)
				titolo.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO().setNotaAlLegame(currentForm.getNotaAlLegame());

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
					"errors.gestioneSemantica.erroreSistema", e
							.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
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
