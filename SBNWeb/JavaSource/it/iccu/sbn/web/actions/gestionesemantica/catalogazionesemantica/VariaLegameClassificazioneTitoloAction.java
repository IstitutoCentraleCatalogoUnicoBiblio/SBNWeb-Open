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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO.LegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.VariaLegameClassificazioneTitoloForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate.RicercaClasseResult;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
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

public class VariaLegameClassificazioneTitoloAction extends LookupDispatchAction {

	private static Log log = LogFactory
			.getLog(VariaLegameClassificazioneTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.ok", "ok");
		map.put("button.stampa", "stampa");
		map.put("button.invioIndice", "invioInIndice");
		map.put("button.chiudi", "chiudi");

		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			VariaLegameClassificazioneTitoloForm currentForm = (VariaLegameClassificazioneTitoloForm) form;
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

		VariaLegameClassificazioneTitoloForm currentForm = (VariaLegameClassificazioneTitoloForm) form;
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
			log.info("VariaLegameClassificazioneTitoloAction::unspecified");
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
			currentForm.setIdentificativoClasse(currentForm.getDettClaGen().getIdentificativo());

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
			//almaviva5_20181022 #6749
			TreeElementViewTitoli classe = (TreeElementViewTitoli) currentForm.getAreaDatiPassBiblioSemanticaVO()
			 	.getTreeElement().findElement(currentForm.getIdentificativoClasse());
			if (classe != null)
				currentForm.setNotaAlLegame(classe.getAreaDatiDettaglioOggettiVO().getDettaglioClasseGeneraleVO().getNotaAlLegame());
			if (!this.initCombo(request, form, navi.getUserTicket()))
				return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VariaLegameClassificazioneTitoloForm currentForm = (VariaLegameClassificazioneTitoloForm) form;
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
			legame.setLivelloPolo(currentForm.getRicercaClasse().isPolo());
			LegameTitoloClasseVO legameTitCla = legame.new LegameTitoloClasseVO();
			legame.getLegami().add(legameTitCla);
			legameTitCla.setIdentificativoClasse(currentForm
					.getIdentificativoClasse());
			legameTitCla.setCodSistemaClassificazione(currentForm.getRicercaClasse().getCodSistemaClassificazione());
			legameTitCla.setNotaLegame(currentForm.getNotaAlLegame());
			DatiLegameTitoloClasseVO risposta = factory
					.getGestioneSemantica()
					.modificaLegameTitoloClasse(legame, utenteCollegato.getTicket());

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

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB );
		//variaCatalSemCla.setFolder(FolderType.FOLDER_CLASSI);
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

	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VariaLegameClassificazioneTitoloForm currentForm = (VariaLegameClassificazioneTitoloForm) form;

		ActionMessages errors = new ActionMessages();

		if (currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneClassificazione().getCodNotazioneSelezionato() != null) {
			String xid = currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneClassificazione()
					.getCodNotazioneSelezionato();
			currentForm.getRicercaClasse().setIdentificativoClasse(xid);
			String deweyEd = null;
			String idClasse = currentForm.getRicercaClasse().getIdentificativoClasse();
			if (ValidazioneDati.isFilled(idClasse) ) {
				SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
				currentForm.getRicercaClasse().setCodSistemaClassificazione(sd.getSistema());
				currentForm.getRicercaClasse().setCodEdizioneDewey(sd.getEdizione());
				currentForm.getRicercaClasse().setSimbolo(sd.getSimbolo());

				deweyEd = CodiciProvider.unimarcToSBN(
						CodiciType.CODICE_EDIZIONE_CLASSE,
						currentForm.getRicercaClasse().getCodEdizioneDewey());

				request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY, deweyEd);
			}
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaClasse().clone() );
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			CreaVariaClasseVO classe = null;
			try {

				classe = factory.getGestioneSemantica().analiticaClasse(
						currentForm
						.getCatalogazioneSemanticaComune().isPolo(), xid,
						utenteCollegato.getTicket());
			} catch (EJBException e) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.indiceTestoEsito", e
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (ValidationException ve) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreValidazione", ve
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", ve);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

			currentForm.getDettClaGen().setLivAut(classe.getLivello());
			currentForm.getDettClaGen().setIdentificativo(xid);
			currentForm.getDettClaGen().setDescrizione(
					classe.getDescrizione());
			currentForm.getDettClaGen().setCampoSistema(
					classe.getCodSistemaClassificazione());
			currentForm.getDettClaGen().setCampoEdizione(
					deweyEd);
			currentForm.getDettClaGen()
					.setDataAgg(classe.getDataVariazione());
			currentForm.getDettClaGen().setDataIns(
					classe.getDataInserimento());
			currentForm.getDettClaGen().setIndicatore("NO");
			currentForm.getDettClaGen().setT005(classe.getT005());
			request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, currentForm.getDettClaGen());
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		try {
			// VERIFICO SE LA CLASSIFICAZIONE RISULTA GIA' PRESENTE IN INDICE CON QUEL
			// TESTO ESATTO
			// IN TAL CASO IL LEGAME SARA' TRA QUESTO CID TROVATO E IL BID
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			RicercaClassiVO ricercaClassi = new RicercaClassiVO();
			ricercaClassi.setCodSistemaClassificazione(currentForm.getDettClaGen().getCampoSistema());
			//ricercaClassi.setDescSistemaClassificazione(variaCatalSemCla
			//		.getRicercaClasse().getDescSistemaClassificazione());
			// ricercaClassi.setCodEdizioneDewey(catalSem.getRicercaClasse().getCodEdizioneDewey());
			// ricercaClassi.setDescEdizioneDewey(catalSem.getRicercaClasse().getDescEdizioneDewey());
			ricercaClassi.setPolo(false);
			ricercaClassi.setParole(currentForm.getDettClaGen().getDescrizione());
			ricercaClassi.setOrdinamentoClasse(TipoOrdinamentoClasse.PER_TESTO);
			ricercaClassi.setElemBlocco(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
			delegate.eseguiRicerca(ricercaClassi, mapping);
			RicercaClasseResult op = delegate.getOperazione();

			switch (op) {
			case analitica_1:// ClassiDelegate.analitica:
				RicercaClasseListaVO lista = (RicercaClasseListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
				SinteticaClasseVO classeTrovata = lista
						.getRisultati().get(0);
				currentForm.getDettClaGen().setIdentificativo(
						classeTrovata.getIdentificativoClasse());
				break;
			case crea_3:// ClassiDelegate.crea:
				CreaVariaClasseVO classe = null;
				CreaVariaClasseVO richiesta = new CreaVariaClasseVO();

				try {
					richiesta.setCodSistemaClassificazione(currentForm
							.getDettClaGen().getCampoSistema());
					richiesta.setCodEdizioneDewey(currentForm.getDettClaGen()
							.getCampoEdizione());
					richiesta.setSimbolo(currentForm.getRicercaClasse()
							.getSimbolo());
					richiesta.setDescrizione(currentForm.getDettClaGen()
							.getDescrizione().trim());
					richiesta.setLivello(currentForm.getDettClaGen()
							.getLivAut());
					richiesta.setUlterioreTermine("");
					richiesta.setLivelloPolo(true);

					classe = factory.getGestioneSemantica().creaClasse(
							richiesta, utenteCollegato.getTicket());
					if (classe.isEsitoOk() ) {
						currentForm.getDettClaGen().setIdentificativo(
								classe.getT001());
					}
					break;
				} catch (ValidationException e) {
					// errori indice
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreValidazione", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();

				} catch (DataException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (InfrastructureException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (Exception e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreSistema", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				}
//			case 4:// ClassiDelegate.sintetica:
//				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
//						"errors.gestioneSemantica.incongruenzaClaInd"));
//				this.saveErrors(request, errors);
//				return mapping.getInputForward();
			case lista_2:// RicercaSoggettoDelegate.lista:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruenzaClaInd"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

			case diagnostico_0:// ClassiDelegate.diagnostico:
				CreaVariaClasseVO classe1 = null;
				CreaVariaClasseVO parametri = new CreaVariaClasseVO();
				try {
					parametri.setCodSistemaClassificazione(currentForm
							.getDettClaGen().getCampoSistema());
					parametri.setCodEdizioneDewey(currentForm.getDettClaGen()
							.getCampoEdizione());
					parametri.setSimbolo(currentForm.getRicercaClasse()
							.getSimbolo());
					parametri.setDescrizione(currentForm.getDettClaGen()
							.getDescrizione().trim());
					parametri.setLivello(currentForm.getDettClaGen()
							.getLivAut());
					parametri.setUlterioreTermine("");
					parametri.setLivelloPolo(true);

					classe1 = factory.getGestioneSemantica().creaClasse(
							parametri, utenteCollegato.getTicket());
					if (classe1.isEsitoOk() ) {
						currentForm.getDettClaGen().setIdentificativo(
								classe1.getT001());
					}
				} catch (ValidationException e) {
					// errori indice
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreValidazione", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();

				} catch (DataException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (InfrastructureException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (Exception e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreSistema", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				}
				break;
			default:

			}

			// SE IL BID RISULTA GIA' LEGATO AD UNA CLASSIFICAZIONE IN INDICE NON CREO
			// UN NUOVO LEGAME
			CatSemClassificazioneVO listaClassi = factory
			.getGestioneSemantica()
			.ricercaClassiPerBidCollegato(
					false,
					currentForm.getCatalogazioneSemanticaComune().getBid(),
					currentForm.getDettClaGen().getCampoSistema(),
					currentForm.getDettClaGen()
					.getCampoEdizione(),
							currentForm.getMaxRighe(),
					utenteCollegato.getTicket());

			if (listaClassi.getTotRighe() > 0) {
				for (int index = 0; index < listaClassi.getListaClassi()
						.size(); index++) {
					SinteticaClasseVO classe = listaClassi.getListaClassi().get(index);
					if (classe.getIdentificativoClasse().equals(currentForm.getDettClaGen().getIdentificativo())) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.giaInIndiceCla"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
				// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				// "errors.gestioneSemantica.noInvioIndice"));
				// this.saveErrors(request, errors);
				// return mapping.getInputForward();
			}
			// RECUPERO LE INFORMAZIONI RELATIVE AL BID PRESENTI IN INDICE
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO analitica = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

			// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
			// operante che nel caso di centro Sistema non coincidono
			String codBiblioSbn = Navigation.getInstance(request).getUtente().getCodPolo() + Navigation.getInstance(request).getUtente().getCodBib();
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(codBiblioSbn);
//			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
			// Fine Modifica almaviva2 16.07.2010

			areaDatiPass.setBidRicerca(currentForm
					.getCatalogazioneSemanticaComune().getBid());
			areaDatiPass.setRicercaIndice(true);
			areaDatiPass.setRicercaPolo(false);
			analitica = factory.getGestioneBibliografica()
					.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass,
							utenteCollegato.getTicket());
			TreeElementViewTitoli reticolo = analitica
					.getTreeElementViewTitoli();
			// SE RETICOLO E' UGUALE A NULL VUOL DIRE CHE IL BID NON E' PRESENTE
			// IN INDICE
			// SQUADRATURA
			if (reticolo == null)
				// houston abbiamo un problema
				throw new Exception(
						"Documento non presente sulla base dati di Indice");
			// CREO IL LEGAME TRA TITOLO E SOGGETTO SULLA BASE DATI DI INDICE
			String T005 = reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getVersione();
			DatiLegameTitoloClasseVO legame = new DatiLegameTitoloClasseVO();
			legame.setBid(currentForm.getCatalogazioneSemanticaComune().getBid());
			legame.setBidNatura(reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getNatura());
			legame.setT005(T005);
			legame.setBidLivelloAut(reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getLivAut());
			legame.setBidTipoMateriale(reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getTipoMat());
			legame.setLivelloPolo(false);
			LegameTitoloClasseVO legameTitCla = legame.new LegameTitoloClasseVO();
			legame.getLegami().add(legameTitCla);
			legameTitCla.setCodSistemaClassificazione(currentForm.getDettClaGen().getCampoSistema());
			legameTitCla.setIdentificativoClasse(currentForm.getDettClaGen().getIdentificativo());
			legameTitCla.setNotaLegame(currentForm.getNotaAlLegame());
			DatiLegameTitoloClasseVO risposta = factory.getGestioneSemantica()
					.modificaInvioInIndiceLegameTitoloClasse(legame,
							utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", e
							.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		}

		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		currentForm.setFolder(FolderType.FOLDER_CLASSI);
		return mapping.findForward("catalogazioneSemantica");
	}

}



