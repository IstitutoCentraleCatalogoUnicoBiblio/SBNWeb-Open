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
package it.iccu.sbn.web.actions.servizi.configurazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioSupportoForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class DettaglioSupportoAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.indietro", "chiudi");
		map.put("servizi.bottone.ok",      "ok");
		map.put("servizi.bottone.si",      "si");
		map.put("servizi.bottone.no",      "no");
		map.put("servizi.bottone.nuova",   "nuovo");
		map.put("servizi.bottone.cancella", "cancella");

		return map;
	}



	private void checkUnspecified(DettaglioSupportoForm form, HttpServletRequest request)
	throws Exception {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		form.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));
		if (form.getBiblioteca()==null || form.getBiblioteca().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.richiestaBiblioteca"));
			checkOK=false;
		}

		if(!checkOK) {
			this.saveErrors(request, errors);
			throw new ValidationException("Validazione dati fallita");
		}

		List<TB_CODICI> svolgimento = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SVOLGIMENTO_DEL_SERVIZIO);
		svolgimento = CaricamentoCombo.cutFirst(svolgimento);
		form.setLstSvolgimento(svolgimento);
		form.setTipoSvolgimento("L");
	}


	private void checkOk(DettaglioSupportoForm form, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		String codiceSupporto = form.getCodiceSupporto();
		if (codiceSupporto==null || codiceSupporto.equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.supporti.scegliSupporto"));
			checkOK=false;
		}

		if(!checkOK) {
			this.saveErrors(request, errors);
			throw new ValidationException("Validazione dati fallita");
		}
	}



	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioSupportoForm dettaglio = (DettaglioSupportoForm)form;

		try {
			if (!dettaglio.isSessione()) {
				checkUnspecified(dettaglio, request);

				List<String> lstSupportiGiaAssegnati = (List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI);
				if (lstSupportiGiaAssegnati!=null) dettaglio.setLstSupportiGiaAssegnati(lstSupportiGiaAssegnati);
				else dettaglio.setLstSupportiGiaAssegnati(new ArrayList<String>());

				SupportoBibliotecaVO supportoVO = (SupportoBibliotecaVO)request.getAttribute(ServiziBaseAction.VO_SUPPORTO_ATTR);
				if (supportoVO == null) {
					dettaglio.setNuovo(true);
					supportoVO = new SupportoBibliotecaVO();
					supportoVO.setCd_bib(dettaglio.getBiblioteca());
					supportoVO.setCd_polo(Navigation.getInstance(request).getUtente().getCodPolo());
					supportoVO.setFlSvolg("L");
					caricaSupporti(dettaglio, request);
					dettaglio.setUltimoSalvato(null);
					dettaglio.setSupporto(supportoVO);

					if (dettaglio.getLstSupporti().size()==1){
						ActionMessages errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.associatiTuttiSupporti"));
						this.saveErrors(request, errors);
						return Navigation.getInstance(request).goBack(true);
					}

				} else {
					dettaglio.setNuovo(false);
					dettaglio.setUltimoSalvato((SupportoBibliotecaVO)supportoVO.clone());
					dettaglio.setSupporto(supportoVO);
					dettaglio.setTipoSvolgimento(supportoVO.getFlSvolg());
					this.configuraSupportiModalitaErogazioneMap(request, (DettaglioSupportoForm)form);

				}

				dettaglio.setSessione(true);
			}

			if 	(request.getAttribute("prov_dettModSupp") != null && request.getAttribute("prov_dettModSupp").equals("moderogaz")){
				this.configuraSupportiModalitaErogazioneMap(request, (DettaglioSupportoForm)form);
			}

			if (ValidazioneDati.equals(dettaglio.getUpdateCombo(), "svolgimento") ) {
				caricaSupporti(dettaglio, request);
				dettaglio.setUpdateCombo(null);
			}

		} catch (ValidationException e) {
			log.error("", e);
			return backForward(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return backForward(request, true);
		}



		return mapping.getInputForward();
	}


	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			  					HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		return this.backForward(request);
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioSupportoForm dettaglio = (DettaglioSupportoForm)form;

		try {
			Navigation navi = Navigation.getInstance(request);
			String utente = navi.getUtente().getFirmaUtente();

			SupportoBibliotecaVO supportoVO = dettaglio.getSupporto();
			Timestamp now = DaoManager.now();
			if (dettaglio.isNuovo()) {
				//Nuovo inserimento
				this.checkOk(dettaglio, request);

				supportoVO.setFlCanc("N");
				supportoVO.setUteIns(utente);
				supportoVO.setUteVar(utente);
				supportoVO.setTsIns(now);
				supportoVO.setTsVar(now);
				//supportoVO.setDescrizione(dettaglio.getCodiceSupporto().substring(dettaglio.getCodiceSupporto().indexOf("_")+1));
				//supportoVO.setCodSupporto(dettaglio.getCodiceSupporto().substring(0, dettaglio.getCodiceSupporto().indexOf("_")));
				supportoVO.setCodSupporto(dettaglio.getCodiceSupporto());
				supportoVO.setFlSvolg(dettaglio.getTipoSvolgimento());

				dettaglio.setConferma(true);
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();

			} else {
				//Aggiornamento
				supportoVO.setUteIns(utente);
				supportoVO.setTsVar(now);
				if (!supportoVO.equals(dettaglio.getUltimoSalvato())) {
					//Ci sono modifiche da salvare
					//Nel caso di aggiornamento chiedo conferma all'utente
					dettaglio.setConferma(true);
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
					this.saveErrors(request, errors);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();
				}else {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}
			}
		} catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioSupportoForm dettaglio = (DettaglioSupportoForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		SupportoBibliotecaVO supportoVO = dettaglio.getSupporto();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			if (dettaglio.getProv_dett_supp() != null && dettaglio.getProv_dett_supp().equals("cancella")){
				dettaglio.setProv_dett_supp(null);

				ActionMessages errors = new ActionMessages();
				SupportiModalitaErogazioneVO modErog = (SupportiModalitaErogazioneVO) dettaglio
						.getLstSupportiModalitaErogazione().get(
								Integer.valueOf(dettaglio
										.getSelectedModalitaErogazione())).clone();
				boolean operazioneOk=factory.getGestioneServizi().cancellaSupportiModalitaErogazione(Navigation.getInstance(request).getUserTicket(), modErog,
						dettaglio.getSupporto().getId());
				if (operazioneOk) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
					this.saveErrors(request, errors);
					this.configuraSupportiModalitaErogazioneMap(request, dettaglio);
					dettaglio.setConferma(false);
					return mapping.getInputForward();
				} else {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreCancellazione"));
					this.saveErrors(request, errors);
					dettaglio.setConferma(false);
					return mapping.getInputForward();
				}

			}else{
				//aggiornamento

				boolean operazioneOK = factory.getGestioneServizi().aggiornaSupportoBiblioteca(Navigation.getInstance(request).getUserTicket(), supportoVO);
				if (operazioneOK) {
					dettaglio.setUltimoSalvato((SupportoBibliotecaVO)supportoVO.clone());
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
					this.saveErrors(request, errors);
					resetToken(request);
					caricaSupporti(dettaglio, request);
					dettaglio.setConferma(false);
					return mapping.findForward("indietro");
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
					this.saveErrors(request, errors);
					dettaglio.setConferma(false);
					resetToken(request);
					return mapping.getInputForward();
				}
			}
		} catch (NumberFormatException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		} catch (RemoteException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioSupportoForm dettaglio = (DettaglioSupportoForm)form;

		request.setAttribute(ServiziBaseAction.VO_SUPPORTO_ATTR, dettaglio.getSupporto());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, dettaglio.getBiblioteca());
		request.setAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR, dettaglio.getLstCodiciErogazione());

		return mapping.findForward("nuovaModalita");

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioSupportoForm dettaglio = (DettaglioSupportoForm)form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettaglio.isSessione()) {
			dettaglio.setSessione(true);
		}

		dettaglio.setConferma(false);
		resetToken(request);
		return mapping.getInputForward();
	}


	private void caricaSupporti(DettaglioSupportoForm dettaglio, HttpServletRequest request) throws Exception
	{
		//dettaglio.setLstSupporti(this.loadCodiciSupportiBibliotecaDiversiDa(dettaglio.getLstSupportiGiaAssegnati(), request));
		if (dettaglio.getTipoSvolgimento().equals("L"))
			dettaglio.setLstSupporti(CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_COPIA));
		else
			dettaglio.setLstSupporti(CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_COPIA_ILL));
	}

	private void configuraSupportiModalitaErogazioneMap(HttpServletRequest request, DettaglioSupportoForm form)
	throws RemoteException, CreateException, ParseException {
		Navigation navi = Navigation.getInstance(request);

		List<SupportiModalitaErogazioneVO> listaSupportiModalitaErogazione = this.getSupportiModalitaErogazione(navi.getUtente().getCodPolo(),
				form.getBiblioteca(), form.getSupporto().getCodSupporto(), request);
		form.setLstSupportiModalitaErogazione(listaSupportiModalitaErogazione);

		Map<String, SupportiModalitaErogazioneVO> supportiModalitaErogazioneMap = new HashMap<String, SupportiModalitaErogazioneVO>();
		Iterator<SupportiModalitaErogazioneVO> iterator = listaSupportiModalitaErogazione.iterator();
		SupportiModalitaErogazioneVO tariffaModalitaVO;
		Locale locale = this.getLocale(request, Constants.SBN_LOCALE);//request.getLocale();
		double tariffa;
		double costoUnitario;
		List<String> lstCodiciErogazione = new ArrayList<String>();
		while (iterator.hasNext()) {
			tariffaModalitaVO = iterator.next();
			if (!locale.equals(tariffaModalitaVO.getLocale())) {
				//il locale dell'istanza di SupportiModalitaErogazioneVO non corrisponde a quello della request
				//lo sovrascrivo e sovrascrivo anche la stringa relativa alla tariffa convertendola al formato
				//relativo al nuovo locale

				tariffaModalitaVO.setLocale(locale);

				tariffa = tariffaModalitaVO.getTarBaseDouble();
				tariffaModalitaVO.setTarBaseDouble(tariffa);

				costoUnitario = tariffaModalitaVO.getCostoUnitarioDouble();
				tariffaModalitaVO.setCostoUnitarioDouble(costoUnitario);
			}
			supportiModalitaErogazioneMap.put(tariffaModalitaVO.getCodErog(), tariffaModalitaVO);
			lstCodiciErogazione.add(tariffaModalitaVO.getCodErog());
		}
		form.setSupportiModalitaErogazioneMap(supportiModalitaErogazioneMap);
		form.setLstCodiciErogazione(lstCodiciErogazione);

		if (form.getLstSupportiModalitaErogazione() != null && form.getLstSupportiModalitaErogazione().size() == 1) {
			form.setSelectedModalitaErogazione("0");
		} else {
			form.setSelectedModalitaErogazione(null);
		}

		if (form.getLstSupportiModalitaErogazione() != null && form.getLstSupportiModalitaErogazione().size() == 0) {
			// se non presenti modalità di erogazione associate al supporto
			// imposto il relativo messaggio
			form.setStringaMessaggioSupportiModalita("ATTENZIONE: è obbligatorio indicare almeno una modalità di erogazione");
		}
		else {
			form.setStringaMessaggioSupportiModalita("");
		}

	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioSupportoForm currentForm = (DettaglioSupportoForm)form;

		try {


			int codSel;
			String check = "";

			check = currentForm.getSelectedModalitaErogazione();

			if (check != null && check.length() != 0) {
				codSel = Integer.parseInt(currentForm.getSelectedModalitaErogazione());
				currentForm.setScelRecModErog(currentForm.getLstSupportiModalitaErogazione().get(codSel));
				currentForm.getScelRecModErog().setUteVar(Navigation.getInstance(request).getUtente().getFirmaUtente());
			}else{
				//Non è stata scelta nessuna modalita
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliModalitaCancellare"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setProv_dett_supp("cancella");
			currentForm.setConferma(true);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "SERVIZI_ILL")) {
			try {
				DettaglioSupportoForm currentForm = (DettaglioSupportoForm)form;
				SupportoBibliotecaVO supporto = currentForm.getSupporto();
				ParametriBibliotecaVO parametriBiblioteca = ServiziDelegate.getInstance(request).getParametriBiblioteca(supporto.getCd_polo(), supporto.getCd_bib() );
				return parametriBiblioteca != null && parametriBiblioteca.isServiziILLAttivi();

			} catch (Exception e) {
				log.error("",  e);
				return false;
			}
		}
		return super.checkAttivita(request, form, idCheck);
	}

}
