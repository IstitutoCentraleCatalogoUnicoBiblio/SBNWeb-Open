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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.util.semantica.SemanticaUtil;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.InsLegameTraDescrForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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
import org.apache.struts.actions.LookupDispatchAction;

public class InsLegameTraDescrAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(InsLegameTraDescrAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.ok", "ok");
		map.put("button.annulla", "annulla");
		return map;
	}


	private void loadDefault(HttpServletRequest request, ActionForm form) {

		InsLegameTraDescrForm currentForm = (InsLegameTraDescrForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try {
			String tipoLegame = (String)utenteEjb.getDefault(ConstantDefault.CREA_DES_DES_TIPO_LEGAME);
			List<TB_CODICI> tipiLegame = currentForm.getListaTipoLegame();
			for (TB_CODICI cod : tipiLegame)
				if (ValidazioneDati.equals(cod.getCd_tabellaTrim(), tipoLegame))
					currentForm.setCodTipoLegame(tipoLegame);

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.default"));
		}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InsLegameTraDescrForm currentForm = (InsLegameTraDescrForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		String codice = null;
		String primodid = null;
		String primotesto = null;
		String primaForma = null;
		String primoLivelloAut = null;
		String secondodid = null;
		String secondotesto = null;
		String secondaForma = null;
		String secondoLivelloAut = null;
		String chiamante = null;
		String cid = null;
		String T005 = null;
		boolean isPolo = false;
		boolean condiviso = false;
		String descrizione = null;
		String dataInserimento = null;
		String dataModifica = null;
		String tipoSoggetto = null;

		if (!currentForm.isSessione()) {
			log.info("InsLegameTraDescrAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			codice = (String) request.getAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO);
			primodid = (String) request.getAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME);
			primotesto = (String) request.getAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_TESTO);
			primaForma = (String) request.getAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_FORMA_NOME);
			primoLivelloAut = (String) request.getAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_LIVELLO_AUTORITA);
			secondodid = (String) request.getAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME);
			secondotesto = (String) request.getAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_TESTO);
			secondaForma = (String) request.getAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_FORMA_NOME);
			secondoLivelloAut = (String) request.getAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_LIVELLO_AUTORITA);
			cid = (String) request.getAttribute(NavigazioneSemantica.CID_RIFERIMENTO);
			T005 = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA_T005);
			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();

			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			descrizione = (String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO);
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			condiviso = (Boolean) request.getAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE);

			if (codice == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
				return mapping.getInputForward();
			}

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setDidPadre((String) request.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
			currentForm.setAction(chiamante);
			currentForm.setRicercaComune((RicercaComuneVO) request.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			currentForm.getRicercaComune().setCodSoggettario(codice);
			currentForm.getRicercaComune().setDescSoggettario(descrizione);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setTipoSoggetto(tipoSoggetto);

			currentForm.getRicercaComune().setPolo(isPolo);
			currentForm.setPrimoDid(primodid);
			currentForm.setPrimoTesto(primotesto.trim());
			currentForm.setPrimaForma(primaForma);
			currentForm.setPrimoLivelloAut(primoLivelloAut);
			currentForm.setSecondoDid(secondodid);
			currentForm.setSecondoTesto(secondotesto.trim());
			currentForm.setSecondaForma(secondaForma);
			currentForm.setSecondoLivelloAut(secondoLivelloAut);
			currentForm.setCid(cid);
			currentForm.setT005(T005);
			currentForm.setRicercaComune((RicercaComuneVO) request.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			currentForm.setCondiviso(condiviso);

			//almaviva5_20120507 evolutive CFI
			ParametriSoggetti parametri = ParametriSoggetti.retrieve(request);
			currentForm.setParametriSogg(parametri);
			DettaglioDescrittoreVO d1 = (DettaglioDescrittoreVO) parametri.get(SoggettiParamType.DETTAGLIO_ID_PARTENZA);
			DettaglioDescrittoreVO d2 = (DettaglioDescrittoreVO) parametri.get(SoggettiParamType.DETTAGLIO_ID_ARRIVO);
			currentForm.setListaTipoLegame(SemanticaUtil.getListaTipoLegameTraDescrittori(d1, d2));

			try {
				this.loadDefault(request, currentForm);
			} catch (Exception ex) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			}

		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setEnable(true);
		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InsLegameTraDescrForm currentForm = (InsLegameTraDescrForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);

		try {

			//controllo livello autorità
			if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(currentForm.getPrimoLivelloAut(), true) )
				return mapping.getInputForward();

			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			DatiLegameDescrittoreVO legame = new DatiLegameDescrittoreVO();
			legame.setDidPartenza(currentForm.getPrimoDid());
			legame.setLivelloPolo(currentForm.getRicercaComune().isPolo());
			legame.setT005(currentForm.getT005());
			legame.setDidPartenzaFormaNome(currentForm.getPrimaForma());
			legame.setDidArrivoFormaNome(currentForm.getSecondaForma());
			legame.setDidPartenzaLivelloAut(currentForm.getPrimoLivelloAut());
			legame.setCodiceSoggettario(currentForm.getRicercaComune().getCodSoggettario());
			legame.setDidArrivo(currentForm.getSecondoDid());
			legame.setNotaLegame(currentForm.getNote());
			legame.setTipoLegame(currentForm.getCodTipoLegame());
			legame.setCondiviso(currentForm.isCondiviso());

			CreaVariaDescrittoreVO risposta = factory.getGestioneSemantica()
					.creaLegameDescrittori(legame, utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ){
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));
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

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune()
				.getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO , currentForm.getRicercaComune().isPolo());

		//request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getPrimoLivelloAut());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getRicercaComune().getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO, currentForm.getPrimoDid());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		if (currentForm.getCid() != null) {
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					currentForm.getCid());
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_CID);
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
			return mapping.findForward("analiticasoggetto");
		} else {
			request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI, currentForm.getDidPadre());
			request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
					//currentForm.getPrimoDid());
					currentForm.getDidPadre());
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_DID);
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

			return mapping.findForward("analiticadescrittore");
		}

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

}
