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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.InsDescrPerLegameDescrForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.actions.LookupDispatchAction;

public class InsDescrPerLegameDescrAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(InsDescrPerLegameDescrAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.ok", "ok");
		map.put("button.crea", "crea");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private void initCombo(String ticket, ActionForm form) throws Exception {
		InsDescrPerLegameDescrForm currentForm = (InsDescrPerLegameDescrForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		currentForm.setListaFormaNome(CaricamentoComboSemantica.loadComboFormaNome());
		//almaviva5_20111125 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}


	private void loadDefault(HttpServletRequest request, ActionForm form) {

		InsDescrPerLegameDescrForm currentForm = (InsDescrPerLegameDescrForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try {
			String formaNome = (String)utenteEjb.getDefault(ConstantDefault.CREA_DES_DES_FORMA_TERMINE);
			currentForm.getRicercaComune().setFormaNome(formaNome);

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.default"));
		}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InsDescrPerLegameDescrForm currentForm = (InsDescrPerLegameDescrForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		String dataInserimento = null;
		String dataModifica = null;
		DettaglioDescrittoreVO dettaglio = null;

		boolean isPolo = false;
		String chiamante = null;
		String tipoSoggetto = null;
		String descrizione = null;
		String cid = null;

		currentForm.setRicercaComune((RicercaComuneVO) request
				.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));

		if (!currentForm.isSessione()) {
			log.info("InsDescrPerLegameDescrAction::unspecfied");
			// devo inizializzare tramite le request.getAttribute(......)
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			cid = (String) request.getAttribute(NavigazioneSemantica.CID_RIFERIMENTO);
			dettaglio = (DettaglioDescrittoreVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_DESCRITTORE);

			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			descrizione = (String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO);
			currentForm.setDidPadre((String) request.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
			currentForm.setCondiviso(dettaglio.isCondiviso());
			//almaviva5_20090720
			currentForm.setLivAut(dettaglio.getLivAut());

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			try {
				this.initCombo(Navigation.getInstance(request).getUserTicket(), currentForm);
				currentForm.setListaLivelloAutorita(CaricamentoComboSemantica
						.loadComboStato(SoggettiDelegate.getInstance(request).getMaxLivelloAutoritaDE()));
			} catch (Exception ex) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			}

			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDettDesGenVO(dettaglio);

			currentForm.getRicercaComune().setPolo(isPolo);
			currentForm.getRicercaComune().setCodSoggettario(currentForm.getDettDesGenVO().getCampoSoggettario());
			currentForm.getRicercaComune().setDescSoggettario(descrizione);

			currentForm.setPrimodid(currentForm.getDettDesGenVO().getDid());
			currentForm.setPrimotesto(currentForm.getDettDesGenVO().getTesto().trim());
			currentForm.setPrimaForma(currentForm.getDettDesGenVO().getFormaNome());
			currentForm.setLivelloAutorita(currentForm.getDettDesGenVO().getLivAut());
			currentForm.setTipoSoggetto(tipoSoggetto);
			currentForm.setCid(cid);
			currentForm.setT005(currentForm.getDettDesGenVO().getT005());
			currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request.getAttribute(NavigazioneSemantica.ANALITICA));
			if (currentForm.getT005() == null)
				currentForm.setT005(currentForm.getTreeElementViewSoggetti().getT005());

			currentForm.setAction(chiamante);

			OggettoRiferimentoVO oggettoRiferimento = (OggettoRiferimentoVO) request
					.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
			if (oggettoRiferimento != null)
				currentForm.setOggettoRiferimento(oggettoRiferimento);
			loadDefault(request, form);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InsDescrPerLegameDescrForm currentForm = (InsDescrPerLegameDescrForm) form;
		Navigation navi = Navigation.getInstance(request);
		RicercaComuneVO ricerca = currentForm.getRicercaComune();
		boolean polo = ricerca.isPolo();
		AnaliticaSoggettoVO analitica = null;

		try {
			UserVO utenteCollegato = navi.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			CreaVariaDescrittoreVO descrittore = new CreaVariaDescrittoreVO();
			descrittore.setFormaNome(ricerca.getFormaNome());
			descrittore.setTesto(currentForm.getDescrittore().trim());
			descrittore.setLivelloAutorita(currentForm.getLivAut());
			DettaglioDescrittoreVO dettaglio = currentForm.getDettDesGenVO();
			descrittore.setCodiceSoggettario(dettaglio.getCampoSoggettario());
			descrittore.setNote(currentForm.getNote());
			descrittore.setT005(currentForm.getT005());
			descrittore.setLivelloPolo(polo);
			//almaviva5_20120420 evolutive CFI
			descrittore.setCategoriaTermine(dettaglio.getCategoriaTermine());
			descrittore.setEdizioneSoggettario(ricerca.getEdizioneSoggettario());
			CreaVariaDescrittoreVO risposta = factory.getGestioneSemantica().creaDescrittoreManuale(descrittore, utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));
				return mapping.getInputForward();
			}

			analitica = SoggettiDelegate.getInstance(request).caricaReticoloDescrittore(polo, risposta.getDid());
			if (analitica == null)
				return mapping.getInputForward();

			currentForm.setDid(risposta.getDid());
			currentForm.setSecondoLivelloAut(risposta.getLivelloAutorita());
			currentForm.getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO().setDataIns(risposta.getDataInserimento());
			currentForm.setDataInserimento(risposta.getDataInserimento());
			currentForm.getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO().setDataAgg(risposta.getDataVariazione());
			currentForm.setDataModifica(risposta.getDataVariazione());

		} catch (ValidationException e) {
			if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME, currentForm.getDid());
		request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_TESTO, currentForm.getDescrittore().trim());
		request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_FORMA_NOME, ricerca.getFormaNome());
		request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_LIVELLO_AUTORITA, currentForm.getSecondoLivelloAut());
		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, currentForm.getCid());
		request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME, currentForm.getPrimodid());
		request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_TESTO, currentForm.getPrimotesto().trim());
		request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_FORMA_NOME, currentForm.getPrimaForma());
		request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_LIVELLO_AUTORITA, currentForm.getLivelloAutorita());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005, currentForm.getT005());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, polo);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, ricerca.getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getLivelloAutorita());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, ricerca.getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, ricerca.clone() );
		request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI, currentForm.getDidPadre());
		request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE, currentForm.isCondiviso());

		//almaviva5_20120507 evolutive CFI
		ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
		parametri.put(SoggettiParamType.DETTAGLIO_ID_PARTENZA, currentForm.getDettDesGenVO() );
		parametri.put(SoggettiParamType.DETTAGLIO_ID_ARRIVO, analitica.getReticolo().getDettaglio() );
		ParametriSoggetti.send(request, parametri);

		navi.purgeThis();
		return navi.goForward(mapping.findForward("ok"));
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		InsDescrPerLegameDescrForm currentForm = (InsDescrPerLegameDescrForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		try {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();

			ricercaComune.setRicercaSoggetto(null);
			RicercaDescrittoreVO ricercaDescrittore = new RicercaDescrittoreVO();
			ricercaDescrittore.setTestoDescr(currentForm.getDescrittore().trim());
			ricercaComune.setRicercaDescrittore(ricercaDescrittore );
			ricercaComune.setOrdinamentoDescrittore(TipoOrdinamento.PER_TESTO);
			ricercaComune.setRicercaTipoD(TipoRicerca.STRINGA_ESATTA);

			delegate.eseguiRicerca(ricercaComune, mapping);
			RicercaSoggettoResult op = delegate.getOperazione();
			RicercaSoggettoListaVO output =
				(RicercaSoggettoListaVO) request.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

			switch (op) {

			case analitica_1:// SoggettiDelegate.analitica:
				currentForm.setOutputDescr(output);

				ElementoSinteticaDescrittoreVO descrittoreTrovato = (ElementoSinteticaDescrittoreVO) currentForm
						.getOutputDescr().getRisultati().get(0);


				AnaliticaSoggettoVO analitica = delegate.caricaReticoloDescrittore(ricercaComune.isPolo(), descrittoreTrovato.getDid() );
				if (analitica == null)
					return mapping.getInputForward();

				DettaglioDescrittoreVO dettaglioArrivo = analitica
						.getReticolo().getAreaDatiDettaglioOggettiVO()
						.getDettaglioDescrittoreGeneraleVO();

				request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME, dettaglioArrivo.getDid());
				request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_TESTO, dettaglioArrivo.getTesto());

				request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_FORMA_NOME, dettaglioArrivo.getFormaNome() );
				request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_LIVELLO_AUTORITA, dettaglioArrivo.getLivAut() );
				request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, currentForm.getCid());
				request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME, currentForm.getPrimodid());
				request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_TESTO, currentForm.getPrimotesto().trim());
				request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_FORMA_NOME, currentForm.getPrimaForma());
				request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_LIVELLO_AUTORITA, currentForm.getLivelloAutorita());
				request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, ricercaComune.getCodSoggettario());

				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, ricercaComune.isPolo());
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005, currentForm.getT005());
				request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, dettaglioArrivo.getDataIns() );
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dettaglioArrivo.getDataAgg() );
				request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTipoSoggetto());
				request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getLivelloAutorita());
				request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, ricercaComune.getDescSoggettario());
				request	.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, ricercaComune);
				request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI, currentForm.getDidPadre());
				request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE, currentForm.isCondiviso());

				//almaviva5_20120507 evolutive CFI
				ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
				parametri.put(SoggettiParamType.DETTAGLIO_ID_PARTENZA, currentForm.getDettDesGenVO() );
				parametri.put(SoggettiParamType.DETTAGLIO_ID_ARRIVO, analitica.getReticolo().getDettaglio() );
				ParametriSoggetti.send(request, parametri);

				navi.purgeThis();
				return navi.goForward(mapping.findForward("ok"));

			case crea_4:// SoggettiDelegate.crea:
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.nontrovato"));
				currentForm.setEnableCrea(true);
				return mapping.getInputForward();

			case sintetica_3:// SoggettiDelegate.sintetica:
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruenza"));
				return mapping.getInputForward();

			case lista_2:// SoggettiDelegate.lista:
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruenza"));
				return mapping.getInputForward();

			case diagnostico_0:// SoggettiDelegate.diagnostico:
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.nontrovato"));
				currentForm.setEnableCrea(true);
				return mapping.getInputForward();

			default:
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noselezione"));
				return mapping.getInputForward();
			}

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();
		}
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		return mapping.findForward("stampa");
	}

	private enum TipoAttivita {
		CREA
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			InsDescrPerLegameDescrForm currentForm = (InsDescrPerLegameDescrForm) form;
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			switch (attivita) {

			case CREA:
				if (!delegate.isSoggettarioGestito(currentForm.getRicercaComune().getCodSoggettario()) )
					return false;
				if (!currentForm.isEnableCrea())
					return false;

				return delegate.isAbilitatoDE(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
			}
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}

	}


}
