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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatturaSoggettoDaIndiceForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class CatturaSoggettoDaIndiceAction extends
		SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(CatturaSoggettoDaIndiceAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.cattura", "cattura");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "indietro");
		map.put("button.analitica", "analiticaIndice");

		return map;
	}

	private void loadSoggettario(String ticket, ActionForm form)
			throws Exception {
		CatturaSoggettoDaIndiceForm currentForm = (CatturaSoggettoDaIndiceForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatturaSoggettoDaIndiceForm currentForm = (CatturaSoggettoDaIndiceForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		CatSemSoggettoVO ricSoggLista = null;
		RicercaComuneVO ricSoggRicerca = null;
		String chiamante = null;
		FolderType folder = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {
			log.info("CatturaSoggettoDaIndiceAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			ricSoggLista = (CatSemSoggettoVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			ricSoggRicerca = (RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			folder = (FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();

			if (ricSoggLista == null || ricSoggRicerca == null) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.Faild"));

				return mapping.getInputForward();
			}
			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));

				return mapping.getInputForward();
			}
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			currentForm.setSessione(true);

			List<ElementoSinteticaSoggettoVO> listaSoggetti = ricSoggLista.getListaSoggetti();
			if (listaSoggetti.size() == 1)
				currentForm.setCodSelezionato(listaSoggetti.get(0).getCid());

			currentForm.setOutput(ricSoggLista);
			currentForm.setRicercaComune(ricSoggRicerca);

			AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (datiGB != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
				currentForm.getCatalogazioneSemanticaComune().setBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getDescPartenza());
				DettaglioTitoloParteFissaVO detTitoloPFissaVO =
					currentForm.getAreaDatiPassBiblioSemanticaVO()
						.getTreeElement()
						.getAreaDatiDettaglioOggettiVO()
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
				currentForm.setFolder(folder);
			}
		} else {
			currentForm.setFolder((FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			currentForm.setRicercaComune((RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			currentForm.setOutput((CatSemSoggettoVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
		}

		try {
			this.loadSoggettario(Navigation.getInstance(request)
					.getUserTicket(), currentForm);
		} catch (Exception ex) {
			LinkableTagUtils.resetErrors(request);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
		}

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setBlocchiCaricati(appoggio);
		if (ricSoggLista != null) {
			currentForm.setIdLista(ricSoggLista.getIdLista());
			currentForm.setMaxRighe(ricSoggLista.getMaxRighe());
			currentForm.setNumBlocco(ricSoggLista.getNumBlocco());
			currentForm.setTotBlocchi(ricSoggLista.getTotBlocchi());
			currentForm.setTotRighe(ricSoggLista.getTotRighe());
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		currentForm.setRicercaComune((RicercaComuneVO) request.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
		currentForm.getRicercaComune().setPolo(isPolo);
		return mapping.getInputForward();

	}

	public ActionForward cattura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatturaSoggettoDaIndiceForm currentForm = (CatturaSoggettoDaIndiceForm) form;


		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		String cid = currentForm.getCodSelezionato();
		CatalogazioneSemanticaComuneVO comuneVO = currentForm.getCatalogazioneSemanticaComune();
		String bid = comuneVO.getBid();

		if (ValidazioneDati.strIsNull(cid) || ValidazioneDati.strIsNull(bid)) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));

			// nessun codice selezionato

			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	false);
		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);

		AnaliticaSoggettoVO soggettoDaCatturare = delegate.caricaReticoloSoggetto(false, cid);
		if (soggettoDaCatturare == null)
			return mapping.getInputForward();

		DettaglioSoggettoVO soggettoIndice = soggettoDaCatturare.getDettaglio();

		AnaliticaSoggettoVO analitica = delegate.importaSoggettoDaIndice(soggettoIndice);
		if (analitica == null)
			return mapping.getInputForward();

		if (!analitica.isEsitoOk() ) {

			// errore SBNMarc
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", analitica.getTestoEsito()));

			//nessun codice selezionato
			return mapping.getInputForward();
		}

		AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();

		DettaglioSoggettoVO soggetto = analitica.getDettaglio();

		DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();
		legame.setBid(comuneVO.getBid());
		legame.setBidNatura(comuneVO.getNaturaBid());
		legame.setT005(detTitoloPFissaVO.getVersione() );
		legame.setBidLivelloAut(comuneVO.getLivAutBid());
		legame.setBidTipoMateriale(comuneVO.getTipoMaterialeBid());
		legame.setLivelloPolo(true);
		legame.setImporta(true);
		legame.getLegami().add(legame.new LegameTitoloSoggettoVO(soggetto.getCid(), "") );
		DatiLegameTitoloSoggettoVO risposta = delegate.creaLegameTitoloSoggetto(legame);
		if (risposta == null)
			return mapping.getInputForward();

		//aggiorno timestamp del titolo soggettato
		datiGB.getTreeElement().setT005(risposta.getT005());
		detTitoloPFissaVO.setVersione(risposta.getT005() );

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
		return mapping.findForward("catalogazioneSemantica");

	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatturaSoggettoDaIndiceForm currentForm = (CatturaSoggettoDaIndiceForm) form;

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.sogNotFound"));

			resetToken(request);
			return mapping.getInputForward();
		}

		Set<Integer> appoggio = currentForm.getBlocchiCaricati();
		int i = currentForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		CatSemSoggettoVO areaDatiPass = new CatSemSoggettoVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(currentForm.getRicercaComune().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		CatSemSoggettoVO areaDatiPassReturn = (CatSemSoggettoVO) factory
				.getGestioneSemantica().getNextBloccoSoggetti(areaDatiPass,	utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.sogNotFound"));

			resetToken(request);
			return mapping.getInputForward();
		}

		currentForm.setIdLista(areaDatiPassReturn.getIdLista());
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setBlocchiCaricati(appoggio);

		currentForm.setNumBlocco(numBlocco);

		currentForm.getOutput().getListaSoggetti().addAll(areaDatiPassReturn.getListaSoggetti());
		Collections.sort(currentForm.getOutput().getListaSoggetti(), ElementoSinteticaSoggettoVO.ORDINA_PER_PROGRESSIVO);

		return mapping.getInputForward();

	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward analiticaIndice(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CatturaSoggettoDaIndiceForm currentForm = (CatturaSoggettoDaIndiceForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		Integer progressivo = currentForm.getCatalogazioneSemanticaComune()
				.getLinkProgressivo();
		List<ElementoSinteticaSoggettoVO> listaSoggetti = currentForm.getOutput()
				.getListaSoggetti();
		if (progressivo != null) {
			for (int i = 0; i < listaSoggetti.size(); i++) {
				ElementoSinteticaSoggettoVO elem = listaSoggetti
						.get(i);
				if (elem.getProgr() == progressivo) {
					currentForm.getCatalogazioneSemanticaComune()
							.getCatalogazioneSoggetto().setCodSelezionato(
									elem.getCid());
					break;
				}
			}
		}


		String codSelezionato = currentForm.getCodSelezionato();

		String xid = null;
		if (!ValidazioneDati.strIsNull(codSelezionato)) {
			xid = codSelezionato;
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					xid);
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_CID);
			RicercaComuneVO reqRicercaComune = (RicercaComuneVO) currentForm
					.getRicercaComune().clone();
			reqRicercaComune.setPolo(false);
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					reqRicercaComune);
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
					reqRicercaComune.isPolo());

		} else {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));

			// nessun codice selezionato

			return mapping.getInputForward();
		}

		ElementoSinteticaSoggettoVO elem = null;
		for (int i = 0; i < listaSoggetti.size(); i++) {
			elem = listaSoggetti.get(i);
			if (elem.getCid().equals(xid))
				break;

		}
		// AreaDatiPassBiblioSemanticaVO clone =
		// (AreaDatiPassBiblioSemanticaVO)
		// currentForm.getAreaDatiPassBiblioSemanticaVO().clone();
		// request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
		// clone);
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, elem
				.getCodiceSoggettario());

		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
		AnaliticaSoggettoVO analitica = delegate.caricaReticoloSoggetto(false, codSelezionato);
		if (analitica == null)
			return mapping.getInputForward();
		request.setAttribute(NavigazioneSemantica.ANALITICA, analitica.getReticolo());

		NavigationForward forward = Navigation.getInstance(request).goForward(
				mapping.findForward("analiticaSoggetto"));
		forward.addParameter("FROM_CATTURA", "Y");
		return forward;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		try {
			if (idCheck.equals("CATTURA")) {
				SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
				delegate.getEJBUtente().isAbilitatoLegameTitoloAuthority("SO");
				//return delegate.isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
				delegate.getEJBUtente().checkAttivita(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003);
				return true;
			}
			return false;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

}
