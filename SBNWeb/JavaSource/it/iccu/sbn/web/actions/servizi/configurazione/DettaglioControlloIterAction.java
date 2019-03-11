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
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioControlloIterForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class DettaglioControlloIterAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok",            "ok");
		map.put("servizi.bottone.annulla",       "Chiudi");
		map.put("servizi.bottone.indietro",      "Chiudi");
		map.put("servizi.bottone.si",            "si");
		map.put("servizi.bottone.no",            "no");
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

		DettaglioControlloIterForm dettaglio = (DettaglioControlloIterForm)form;


		try {
			if (!dettaglio.isSessione()) {
				checkUnspecified(dettaglio, request);

				FaseControlloIterVO controlloIterVO = (FaseControlloIterVO)request.getAttribute(ServiziBaseAction.VO_DETTAGLIO_CONTROLLO_ATTR);
				if (controlloIterVO==null) {
					dettaglio.setNuovo(true);
					dettaglio.setUltimoSalvato(null);
					dettaglio.setBloccante(false);

					Short posizione = (Short)request.getAttribute(ServiziBaseAction.PROGRESSIVO_CONTROLLO_SCELTO);
					if (posizione!=null)
						dettaglio.setPosizioneInserimento(posizione);

					String tipoOperazione = request.getParameter("tipoOperazione");
					if (tipoOperazione!=null && !tipoOperazione.equals(""))
						dettaglio.setTipoOperazione(tipoOperazione);
				} else {
					dettaglio.setNuovo(false);
					dettaglio.setBloccante(controlloIterVO.isFlagBloc());
					dettaglio.setControlloIterVO(controlloIterVO);
					dettaglio.setUltimoSalvato((FaseControlloIterVO)controlloIterVO.clone());
				}

				caricaSelectOption(dettaglio, request);

				dettaglio.setSessione(true);
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



	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioControlloIterForm dettaglio = (DettaglioControlloIterForm)form;

		try {
			Navigation navi = Navigation.getInstance(request);
			String utente = navi.getUtente().getFirmaUtente();
			long time = System.currentTimeMillis();

			FaseControlloIterVO controlloIterVO = dettaglio.getControlloIterVO();
			if (dettaglio.isNuovo()) {
				//Nuovo inserimento
				this.checkOk(dettaglio, request);

				//FaseControlloIterVO controlloIterVO = new FaseControlloIterVO();
				controlloIterVO.setFlCanc("N");
				controlloIterVO.setUteIns(utente);
				controlloIterVO.setUteVar(utente);
				controlloIterVO.setDataIns(new Date(time));
				controlloIterVO.setDataAgg(new Date(time));
				//controlloIterVO.setProgrFase(dettaglio.getIterServizioVO().getProgrIter());
				controlloIterVO.setDescControllo(dettaglio.getConrolloScelto().substring(dettaglio.getConrolloScelto().indexOf("_")+1));
				controlloIterVO.setCodControllo(new Short(dettaglio.getConrolloScelto().substring(0, dettaglio.getConrolloScelto().indexOf("_"))));
				controlloIterVO.setCodAttivita(dettaglio.getIterServizioVO().getCodAttivita());
				controlloIterVO.setCodBib(dettaglio.getTipoServizioVO().getCodBib());
				controlloIterVO.setCodTipoServ(dettaglio.getTipoServizioVO().getCodiceTipoServizio());
				controlloIterVO.setFlagBloc(dettaglio.isBloccante());

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				boolean operazioneOK = factory.getGestioneServizi()
										.aggiornaControlloIter(Navigation.getInstance(request).getUserTicket(), controlloIterVO,
															dettaglio.getTipoServizioVO().getIdTipoServizio(),
															dettaglio.getIterServizioVO().getCodAttivita(),
															true, dettaglio.getPosizioneInserimento(),
															(dettaglio.getTipoOperazione().equals("I") ? TipoAggiornamentoIter.INSERIMENTO : TipoAggiornamentoIter.AGGIUNTA) );
				if (operazioneOK) {
					dettaglio.setUltimoSalvato((FaseControlloIterVO)controlloIterVO.clone());
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.findForward("indietro");
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
					this.saveErrors(request, errors);
				}
			} else {
				//Aggiornamento

				// verifico che sia impostato il messaggio associato al controllo
				FaseControlloIterVO controlloIter = dettaglio.getControlloIterVO();
				if (ValidazioneDati.strIsNull(controlloIter.getMessaggio())) {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.testoMessaggiocontrolloAssente"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}
				controlloIterVO.setFlagBloc(dettaglio.isBloccante());
				controlloIterVO.setUteIns(utente);
				controlloIterVO.setDataAgg(new Date(time));
				if (!controlloIterVO.equals(dettaglio.getUltimoSalvato())) {
					//Ci sono modifiche da salvare
					//Nel caso di aggiornamento chiedo conferma all'utente
					dettaglio.setConferma(true);
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
					this.saveErrors(request, errors);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();
				}
				else {
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

		resetToken(request);
		return mapping.getInputForward();
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioControlloIterForm dettaglio = (DettaglioControlloIterForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		FaseControlloIterVO controlloIterVO = dettaglio.getControlloIterVO();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			boolean operazioneOK = factory.getGestioneServizi()
									.aggiornaControlloIter(Navigation.getInstance(request).getUserTicket(), controlloIterVO,
											dettaglio.getTipoServizioVO().getIdTipoServizio(),
											dettaglio.getIterServizioVO().getCodAttivita(),
											false, (short)0, TipoAggiornamentoIter.MODIFICA);
			if (operazioneOK) {
				dettaglio.setUltimoSalvato((FaseControlloIterVO)controlloIterVO.clone());
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.findForward("indietro");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward Chiudi(ActionMapping mapping, ActionForm form,
			  HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		return this.backForward(request);
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioControlloIterForm dettaglio = (DettaglioControlloIterForm)form;
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






	private void caricaSelectOption(DettaglioControlloIterForm dettaglio, HttpServletRequest request)
	throws RemoteException, CreateException {
		List<String> listaCodici=(List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI);
		dettaglio.setLstCodiciControllo(this.loadCodiciControlloIterDiversiDa(listaCodici, request));
	}


	private void checkUnspecified(DettaglioControlloIterForm form, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		TipoServizioVO tipoServizio = (TipoServizioVO)request.getAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR);
		if (tipoServizio==null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.tipoServizioRichiesto"));
			checkOK=false;
		}
		form.setTipoServizioVO(tipoServizio);

		IterServizioVO iterServizio = (IterServizioVO)request.getAttribute(ServiziBaseAction.VO_ITER_SERVIZIO);
		if (iterServizio==null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.dettaglioControllo.richiestoIterServizio"));
			checkOK=false;
		}
		form.setIterServizioVO(iterServizio);

		form.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));
		if (form.getBiblioteca()==null || form.getBiblioteca().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.richiestaBiblioteca"));
			checkOK=false;
		}


		if(!checkOK) {
			this.saveErrors(request, errors);
			throw new ValidationException("Validazione dati fallita");
		}
	}


	private void checkOk(DettaglioControlloIterForm form, HttpServletRequest request)
	throws ValidationException {
		DettaglioControlloIterForm dettaglio = form;
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		FaseControlloIterVO controlloIter = form.getControlloIterVO();
		if (form.getConrolloScelto()==null || form.getConrolloScelto().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.controlloAssente"));
			checkOK=false;
		} else {
			// solo se presente ul controllo verifico che sia impostato il messaggio associato
			if (ValidazioneDati.strIsNull(controlloIter.getMessaggio())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.testoMessaggiocontrolloAssente"));
				checkOK=false;
			}
		}

		if(!checkOK) {
			this.saveErrors(request, errors);
			throw new ValidationException("Validazione dati fallita");
		}

	}
}
