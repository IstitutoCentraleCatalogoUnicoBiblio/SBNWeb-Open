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
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.InsLegameSogDescrForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
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
import org.apache.struts.actions.LookupDispatchAction;


public class InsLegameSogDescrAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(InsLegameSogDescrAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.ok", "ok");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private void loadSoggettario(String ticket, ActionForm form) throws Exception {
		InsLegameSogDescrForm currentForm = (InsLegameSogDescrForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		//almaviva5_20120326 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InsLegameSogDescrForm currentForm = (InsLegameSogDescrForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		String chiamante = null;

		String dataInserimento = null;
		String dataModifica = null;
		DettaglioSoggettoVO dettaglio = null;
		boolean isPolo = false;
		String tipoSoggetto = null;

		if (!currentForm.isSessione()) {
			log.info("InsLegameSogDescrAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)

			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			dettaglio = (DettaglioSoggettoVO) request.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);

			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}
			currentForm.setSessione(true);

			try {
				this.loadSoggettario(Navigation.getInstance(request).getUserTicket(), currentForm);
			} catch (Exception ex) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			}

			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDettSogGenVO(dettaglio);
			currentForm.getRicercaComune().setPolo(isPolo);

			OggettoRiferimentoVO oggettoRiferimento = (OggettoRiferimentoVO) request.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
			if (oggettoRiferimento != null)
				currentForm.setOggettoRiferimento(oggettoRiferimento);

		}
		currentForm.getRicercaComune().setCodSoggettario(currentForm.getDettSogGenVO().getCampoSoggettario());
		currentForm.setCid(currentForm.getDettSogGenVO().getCid());
		currentForm.setLivelloAutoritaSogg(currentForm.getDettSogGenVO().getLivAut());
		currentForm.setT005(currentForm.getDettSogGenVO().getT005());
		currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request.getAttribute(NavigazioneSemantica.ANALITICA));
		currentForm.setTipoSoggetto(tipoSoggetto);
		currentForm.setAction(chiamante);
		return mapping.getInputForward();

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InsLegameSogDescrForm currentForm = (InsLegameSogDescrForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);

		DettaglioSoggettoVO soggetto = currentForm.getDettSogGenVO();
		try {
			UserVO utente = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			CreaLegameSoggettoDescrittoreVO legame = new CreaLegameSoggettoDescrittoreVO();
			legame.setCid(currentForm.getCid());
			legame.setPolo(currentForm.getRicercaComune().isPolo());
			legame.setT005(currentForm.getT005());
			legame.setTestoDescrittore(currentForm.getDescrittore().trim());
			legame.setCodSoggettario(currentForm.getRicercaComune().getCodSoggettario());
			legame.setEdizioneSoggettario(soggetto.getEdizioneSoggettario());
			legame.setLivelloAutorita(currentForm.getLivelloAutoritaSogg());
			legame.setCondiviso(soggetto.isCondiviso() );
			legame.setCategoriaSoggetto(soggetto.getCategoriaSoggetto());

			CreaVariaDescrittoreVO risposta = factory.getGestioneSemantica().creaLegameSoggettoDescrittore(legame, utente.getTicket());

			switch (risposta.getEsitoType()) {
			case OK:
				break;

			case EDIZIONE_INCONGRUENTE:
			default:
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));
				return mapping.getInputForward();
			}

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

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, soggetto.getLivAut());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, soggetto.getCategoriaSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

		request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID, currentForm.getCid());
		request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE, NavigazioneSemantica.TIPO_OGGETTO_CID);
		return mapping.findForward("ok");
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		return mapping.findForward("stampa");
	}

}
