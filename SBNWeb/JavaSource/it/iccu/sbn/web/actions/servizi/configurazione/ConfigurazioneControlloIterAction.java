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
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.web.actionforms.servizi.configurazione.ConfigurazioneControlloIterForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ConfigurazioneControlloIterAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esamina",    "esamina");
		map.put("servizi.bottone.cancella",   "cancella");
		map.put("servizi.bottone.annulla",    "Chiudi");
		map.put("servizi.bottone.indietro",   "Chiudi");
		map.put("servizi.bottone.inserisci",  "inserisci");
		map.put("servizi.bottone.aggiungiNuovo",   "aggiungi");
		map.put("servizi.bottone.frecciaSu",  "spostaSu");
		map.put("servizi.bottone.frecciaGiu", "spostaGiu");

		map.put("servizi.bottone.si",        "Si");
		map.put("servizi.bottone.no",        "No");

		return map;
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

		ConfigurazioneControlloIterForm currentForm = (ConfigurazioneControlloIterForm)form;

		try {
			if (!currentForm.isSessione()) {
				checkUnspecified(currentForm, request);
				configuraControlloIterMap(currentForm, request);

				currentForm.setConferma(false);
				currentForm.setSessione(true);
			}
			if(request.getParameter("ricaricaLista")!=null && request.getParameter("ricaricaLista").equals("true")) {
				configuraControlloIterMap(currentForm, request);
				currentForm.setConferma(false);
			}

			if (currentForm.getControlloIterMap() != null) {
				if (currentForm.getControlloIterMap().size() == 1){
					currentForm.setCodControlloScelto("1");
				} else {
					currentForm.setCodControlloScelto(null);
				}
			}

		} catch (ValidationException e) {
			return backForward(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}


	public ActionForward Chiudi(ActionMapping mapping, ActionForm form,
			  HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (request.getSession().getAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE)!=null) {
			request.getSession().setAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA, "true");
		}

		return this.backForward(request);
	}


	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm dettaglio = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkInserisci(dettaglio, request);
		}  catch (ValidationException e) {
			resetToken(request);
			log.info(e.getMessage());
			return mapping.getInputForward();
		}

		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR,      dettaglio.getTipoServizio());
		request.setAttribute(ServiziBaseAction.VO_ITER_SERVIZIO,           dettaglio.getIterServizio());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,            dettaglio.getBiblioteca());
		request.setAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI, dettaglio.getLstCodControlloGiaAssociati());
		request.setAttribute(ServiziBaseAction.PROGRESSIVO_CONTROLLO_SCELTO,
							dettaglio.getControlloIterMap().get(dettaglio.getCodControlloScelto()).getProgrFase());

		return mapping.findForward("inserisciControllo");
	}


	public ActionForward aggiungi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm dettaglio = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR,      dettaglio.getTipoServizio());
		request.setAttribute(ServiziBaseAction.VO_ITER_SERVIZIO,           dettaglio.getIterServizio());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,            dettaglio.getBiblioteca());
		request.setAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI, dettaglio.getLstCodControlloGiaAssociati());

		return mapping.findForward("aggiungiControllo");
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm dettaglio = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkOK(dettaglio, request);

			request.setAttribute(ServiziBaseAction.VO_DETTAGLIO_CONTROLLO_ATTR, dettaglio.getControlloIterMap().get(dettaglio.getCodControlloScelto()).clone());
			request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, dettaglio.getTipoServizio());
			request.setAttribute(ServiziBaseAction.VO_ITER_SERVIZIO, dettaglio.getIterServizio());
			request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, dettaglio.getBiblioteca());
			request.setAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI, dettaglio.getLstCodControlloGiaAssociati());

		} catch (ValidationException e) {
			resetToken(request);
			log.info(e.getMessage());
			return mapping.getInputForward();
		}

		return mapping.findForward("esaminaControllo");
	}


	public ActionForward spostaSu(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm currentForm = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkOK(currentForm, request);
			this.checkScambia(currentForm, request, TipoAggiornamentoIter.SU);

			ActionMessages errors = new ActionMessages();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneServizi().scambioControlliIter(Navigation.getInstance(request).getUserTicket(), currentForm.getTipoServizio().getIdTipoServizio(),
												currentForm.getIterServizio().getCodAttivita(),
												currentForm.getControlloIterMap().get(currentForm.getCodControlloScelto()).getProgrFase(),
												TipoAggiornamentoIter.SU);

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
			this.saveErrors(request, errors);

			configuraControlloIterMap(currentForm, request);
		}  catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}


	public ActionForward spostaGiu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm currentForm = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkOK(currentForm, request);
			this.checkScambia(currentForm, request, TipoAggiornamentoIter.GIU);

			ActionMessages errors = new ActionMessages();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneServizi().scambioControlliIter(Navigation.getInstance(request).getUserTicket(), currentForm.getTipoServizio().getIdTipoServizio(),
										currentForm.getIterServizio().getCodAttivita(),
										currentForm.getControlloIterMap().get(currentForm.getCodControlloScelto()).getProgrFase(),
										TipoAggiornamentoIter.GIU);

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
			this.saveErrors(request, errors);

			configuraControlloIterMap(currentForm, request);
		}  catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}


	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm currentForm = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {

			this.checkCancella(currentForm, request);

			currentForm.setConferma(true);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		}catch (ValidationException e) {
		resetToken(request);
		log.info(e.getMessage());
		return mapping.getInputForward();
	    }

	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneControlloIterForm currentForm = (ConfigurazioneControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ActionMessages errors = new ActionMessages();

			Short codControllo = currentForm.getControlloIterMap().get(currentForm.getCodControlloScelto()).getCodControllo();

			boolean operazioneOk=factory.getGestioneServizi().cancellaControlloIter(Navigation.getInstance(request).getUserTicket(), new String[]{codControllo.toString()}, currentForm.getTipoServizio().getIdTipoServizio(), currentForm.getIterServizio().getCodAttivita(), Navigation.getInstance(request).getUtente().getFirmaUtente());
			if (operazioneOk) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);
				configuraControlloIterMap(currentForm, request);
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
				this.saveErrors(request, errors);
			}
		}  catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		currentForm.setConferma(false);
		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneControlloIterForm currentForm = (ConfigurazioneControlloIterForm)form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}

		currentForm.setConferma(false);
		resetToken(request);
		return mapping.getInputForward();
	}


	private void configuraControlloIterMap(ConfigurazioneControlloIterForm currentForm, HttpServletRequest request)
	throws NumberFormatException, RemoteException, CreateException {
		List<FaseControlloIterVO> listaControlloIter =
				this.getControlloIter(currentForm.getTipoServizio().getIdTipoServizio(),
									currentForm.getIterServizio().getCodAttivita(), request);

		TreeMap<String, FaseControlloIterVO> controlloIterMap = new TreeMap<String, FaseControlloIterVO>();
		List<String> lstCodiciControlloGiaAssociati = new ArrayList<String>();
		FaseControlloIterVO controlloIter=null;
		Iterator<FaseControlloIterVO> iterator = listaControlloIter.iterator();
		while (iterator.hasNext()) {
			controlloIter = iterator.next();

			controlloIterMap.put(controlloIter.getProgrFase().toString()/*controlloIter.getCodControllo().toString()*/,
								 controlloIter);
			lstCodiciControlloGiaAssociati.add(controlloIter.getCodControllo().toString());
		}
		currentForm.setControlloIterMap(controlloIterMap);
		currentForm.setLstCodControlloGiaAssociati(lstCodiciControlloGiaAssociati);

		if (currentForm.getControlloIterMap() != null) {
			if (currentForm.getControlloIterMap().size() == 1){
				currentForm.setCodControlloScelto("1");
			} else {
				currentForm.setCodControlloScelto(null);
			}
		}

	}


	private void checkUnspecified(ConfigurazioneControlloIterForm currentForm, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		TipoServizioVO tipoServizio = (TipoServizioVO)request.getAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR);
		if (tipoServizio==null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.tipoServizioRichiesto"));
			checkOK=false;
		}
		currentForm.setTipoServizio(tipoServizio);

		IterServizioVO iterServizio = (IterServizioVO)request.getAttribute(ServiziBaseAction.VO_ITER_SERVIZIO);
		if (iterServizio==null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.dettaglioControllo.richiestoIterServizio"));
			checkOK=false;
		}
		currentForm.setIterServizio(iterServizio);

		currentForm.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));
		if (currentForm.getBiblioteca()==null || currentForm.getBiblioteca().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.richiestaBiblioteca"));
			checkOK=false;
		}

		if(!checkOK) {
			this.saveErrors(request, errors);
			throw new ValidationException("Validazione dati fallita");
		}
	}


	private void checkInserisci(ConfigurazioneControlloIterForm form, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		if (form.getCodControlloScelto()==null || form.getCodControlloScelto().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.posizioneAssente"));
			this.saveErrors(request, errors);
			checkOK=false;
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	private void checkOK(ConfigurazioneControlloIterForm form, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		if (form.getCodControlloScelto()==null || form.getCodControlloScelto().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.controlloAssente"));
			this.saveErrors(request, errors);
			checkOK=false;
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	private void checkCancella(ConfigurazioneControlloIterForm currentForm, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		if (currentForm.getCodControlloScelto()==null || currentForm.getCodControlloScelto().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.controlloAssente"));
			this.saveErrors(request, errors);
			checkOK=false;
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	private void checkScambia(ConfigurazioneControlloIterForm currentForm, HttpServletRequest request, TipoAggiornamentoIter tipoOp)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		//codControlloScelto in realtà contiene il progressivo fase
		if ( (new Integer(currentForm.getCodControlloScelto())==1 && tipoOp.equals(TipoAggiornamentoIter.SU))
				||
			 (new Integer(currentForm.getCodControlloScelto())==currentForm.getControlloIterMap().size() && tipoOp.equals(TipoAggiornamentoIter.GIU))) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
			this.saveErrors(request, errors);
			checkOK=false;
		}
		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}

}
