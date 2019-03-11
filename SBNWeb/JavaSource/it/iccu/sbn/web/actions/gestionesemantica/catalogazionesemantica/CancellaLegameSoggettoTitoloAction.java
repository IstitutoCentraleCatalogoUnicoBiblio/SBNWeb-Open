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
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CancellaLegameSoggettoTitoloForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
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

public class CancellaLegameSoggettoTitoloAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(CancellaLegameSoggettoTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.elimina", "elimina");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.chiudi", "chiudi");

		return map;
	}

	private void initCombo(String ticket, ActionForm form) throws Exception {
		CancellaLegameSoggettoTitoloForm currentForm = (CancellaLegameSoggettoTitoloForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
		currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
		currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
		//almaviva5_20111123 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		CancellaLegameSoggettoTitoloForm currentForm = (CancellaLegameSoggettoTitoloForm) form;
		// setto il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		String codice = null;
		String cid = null;
		String testo = null;
		String tipoSoggetto = null;
		String statoControllo = null;
		String chiamante = null;
		String dataInserimento = null;
		String dataModifica = null;

		if (!currentForm.isSessione()) {
			log.info("CancellaLegameSoggettoTitoloAction::unspecified");
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			codice = (String) request.getAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO);
			cid = (String) request.getAttribute(NavigazioneSemantica.CID_RIFERIMENTO);
			testo = (String) request.getAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE);
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			statoControllo = (String) request.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA);
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);

			DettaglioSoggettoVO dettaglio =
				(DettaglioSoggettoVO) request.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);
			currentForm.setDettaglio(dettaglio);

			chiamante = navi.getActionCaller();
			if (chiamante == null) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));

				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.getRicercaComune().setCodSoggettario(codice);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setAction(chiamante);
			currentForm.setAreaDatiPassBiblioSemanticaVO((AreaDatiPassBiblioSemanticaVO) request
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
			currentForm.getCatalogazioneSemanticaComune()
					.setTipoMaterialeBid(
							detTitoloPFissaVO.getTipoMat());
			currentForm.getCatalogazioneSemanticaComune().setT005(
					detTitoloPFissaVO.getVersione());
			 TreeElementViewTitoli titolo = (TreeElementViewTitoli)
			 	currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement().findElement(cid);
			// ATTENZIONE : ATTUALMENTE IL PROTOCOLLO NON GESTISCE LE NOTE AL
			// LEGAME TITOLO/SOGGETTO!!!!
			currentForm.setNotaAlLegame(titolo.getDatiLegame().getNotaLegame());

			// default su sintetica soggetti collegati
			//currentForm.getCatalogazioneSemanticaComune().setFolder("S");

			try {
				this.initCombo(navi.getUserTicket(), currentForm);
				currentForm.setTipoSoggetto(tipoSoggetto);
				currentForm.setStatoControllo(statoControllo);
			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.Faild"));

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

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		CancellaLegameSoggettoTitoloForm currentForm = (CancellaLegameSoggettoTitoloForm) form;

		try {
			currentForm.setEnableConferma(false);
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

//			AnaliticaSoggettoVO analitica = null;
//
//			analitica = factory
//					.getGestioneSemantica()
//					.creaAnaliticaSoggettoPerCid(
//							((CancellaLegameSoggettoTitoloForm) form)
//									.getCatalogazioneSemanticaComune().isPolo(),
//							currentForm.getCid(),
//							utenteCollegato.getTicket());
//			if (analitica.isEsitoOk() ) {
//				if (analitica.getReticolo().hasChildren()) {
//					for (TreeElementView child : analitica.getReticolo()
//							.getChildren()) {
//						RicercaSoggettoListaVO lista = factory
//								.getGestioneSemantica()
//								.ricercaSoggettiPerDidCollegato(
//										((CancellaLegameSoggettoTitoloForm) form)
//												.getCatalogazioneSemanticaComune()
//												.isPolo(), child.getKey(), 10,
//										utenteCollegato.getTicket());
//						if (lista.isEsitoOk() ) {
//							boolean cancella = (lista.getTotRighe() == 1);
//							if (cancella) {
//								CreaLegameSoggettoDescrittoreVO legame1 = new CreaLegameSoggettoDescrittoreVO();
//								legame1
//										.setCid(analitica.getReticolo()
//												.getKey());
//								legame1
//										.setCodSoggettario(currentForm
//												.getRicercaComune()
//												.getCodSoggettario());
//								legame1.setLivelloAutorita(analitica
//										.getReticolo().getLivelloAutorita());
//								legame1.setT005(analitica.getReticolo()
//										.getT005());
//								legame1.setDid(child.getKey());
//								legame1
//										.setPolo(((CancellaLegameSoggettoTitoloForm) form)
//												.getCatalogazioneSemanticaComune()
//												.isPolo());
//								VariaDescrittoreVO descrittore = factory
//										.getGestioneSemantica()
//										.cancellaLegameSoggettoDescrittore(
//												legame1,
//												utenteCollegato.getTicket());
//								if (!descrittore.isEsitoOk() ) {
//									// errori indice
//
//									errors
//											.add(
//													ActionMessages.GLOBAL_MESSAGE,
//													new ActionMessage(
//															"errors.gestioneSemantica.incongruo",
//															descrittore
//																	.getTestoEsito()));
//
//									return mapping.getInputForward();
//								}
//
//								AnaliticaSoggettoVO analiticaDesc = factory
//										.getGestioneSemantica()
//										.creaAnaliticaSoggettoPerDid(
//												((CancellaLegameSoggettoTitoloForm) form)
//														.getCatalogazioneSemanticaComune()
//														.isPolo(),
//												child.getKey(), 0,
//												utenteCollegato.getTicket());
//								if (analiticaDesc.isEsitoOk() ) {
//									if (analiticaDesc.getReticolo()
//											.hasChildren())
//										break;
//									//cancello il descrittore inutilizzato
//									factory
//											.getGestioneSemantica()
//											.cancellaSoggettoDescrittore(
//													((CancellaLegameSoggettoTitoloForm) form)
//															.getCatalogazioneSemanticaComune()
//															.isPolo(),
//													child.getKey(),
//													utenteCollegato.getTicket());
//								}
//
//							} // if cancella
//
//						} // end if lista
//
//					} // end for
//				} // soggetto.hasChildren()

				// cancello il soggetto
			//DOVREI SAPERE SE IL SOGGETTO E' PRESENTE ANCHE IN INDICE E SE NON HA LEGAMI CON
			//TITOLI CANCELLARLO
				factory.getGestioneSemantica().cancellaSoggettoDescrittore(
						((CancellaLegameSoggettoTitoloForm) form)
								.getCatalogazioneSemanticaComune().isPolo(),
								currentForm.getCid(),
				//		analitica.getReticolo().getKey(),
						utenteCollegato.getTicket(),
						SbnAuthority.SO);

//			} // if analitica

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

		CancellaLegameSoggettoTitoloForm currentForm = (CancellaLegameSoggettoTitoloForm) form;
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		return mapping.findForward("catalogazioneSemantica");

	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CancellaLegameSoggettoTitoloForm currentForm = (CancellaLegameSoggettoTitoloForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		AreaDatiPassBiblioSemanticaVO datiGB =
			currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();
		try {
			DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();
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
					.getCatalogazioneSemanticaComune().isPolo());
			DettaglioSoggettoVO dettaglio = currentForm.getDettaglio();
			LegameTitoloSoggettoVO datiLegame = legame.new LegameTitoloSoggettoVO(dettaglio.getCid(), currentForm.getNotaAlLegame());
			datiLegame.setRank(datiGB.getRankLegame());
			legame.getLegami().add(datiLegame );
			DatiLegameTitoloSoggettoVO risposta = factory.getGestioneSemantica()
					.cancellaLegameTitoloSoggetto(legame,
							utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));

				return mapping.getInputForward();
			}

			//aggiorno timestamp del titolo soggettato
			datiGB.getTreeElement().setT005(risposta.getT005());
			detTitoloPFissaVO.setVersione(risposta.getT005() );

			int titoli = dettaglio.getNumTitoliPolo();
			// titoli collegati al soggetto

			if (titoli <= 1 ) { // un titolo é quello appena slegato
				// vuol dire che non c'è alcun titolo legato al soggetto
				// imposto la richiesta di cancellazione del soggetto

				//almaviva5_20140225 check per cancellazione soggetto
				SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
				if (delegate.isLivAutOkSO(dettaglio.getLivAut(), false) &&
						delegate.isAbilitatoSO(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028) ) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.gestioneSemantica.cancSogTit"));

					this.saveToken(request);
					currentForm.setEnableConferma(true);
					this.preparaConferma(mapping, request);
					return mapping.getInputForward();
				}
			}

			// se il soggetto è legato ad altri titoli posso cancellare solo il
			// legame
			if (titoli > 1) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.operOk"));

				request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
						datiGB );
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
