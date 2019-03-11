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
import it.iccu.sbn.ejb.remote.GestioneServizi;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.validators.servizi.ServizioBibliotecaValidator;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioServizioForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class DettaglioServizioAction extends ConfigurazioneBaseAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.annulla",  "Chiudi");
		map.put("servizi.bottone.ok",        "ok");
		map.put("servizi.bottone.si",        "Si");
		map.put("servizi.bottone.no",        "No");
		return map;
	}



	private void checkOk(ActionForm form, HttpServletRequest request)
	throws Exception {
		DettaglioServizioForm dettaglio = (DettaglioServizioForm)form;
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		if (dettaglio.isNuovo()) {
			if (dettaglio.getLstCodiciServiziAssociati().contains( dettaglio.getServizio().getCodServ().trim())) {
				//il codice servizio inserito è già presente. Occorre sceglierne uno nuovo
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.dettaglioServizio.codiceServizioNonValido"));
				this.saveErrors(request, errors);
				checkOK=false;
			}
		}
		if (dettaglio.isPenalita()) {
			if (dettaglio.getServizio().getPenalita()==null ||
				(dettaglio.getServizio().getPenalita().getGgSosp()==null || dettaglio.getServizio().getPenalita().getGgSosp()<0)) {
				//occorre inserire il dato relativo alla penalità: giorni di sospensione
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.dettaglioServizio.penalitaNonValida.ggSospensione"));
				this.saveErrors(request, errors);
				checkOK=false;
			}
			if (dettaglio.getServizio().getPenalita()==null ||
				(dettaglio.getServizio().getPenalita().getCoeffSosp()==null || dettaglio.getServizio().getPenalita().getCoeffSosp()<0)) {
				//occorre inserire il dato relativo alla penalità: coefficiente di sospensione
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.dettaglioServizio.penalitaNonValida.coeffSospensione"));
				this.saveErrors(request, errors);
				checkOK=false;
			}
		}

		dettaglio.getServizio().validate(new ServizioBibliotecaValidator());

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}



	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioServizioForm dettaglio = (DettaglioServizioForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		dettaglio.setChiamante((String)request.getAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR));

		try {
			if (!dettaglio.isSessione()) {
				//I seguenti 2 attributi devono sempre essere impostati
				String codTipoServizio = (String)request.getAttribute(ServiziBaseAction.COD_TIPO_SERVIZIO_ATTR);
				String desTipoServizio = (String)request.getAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR);
				if (!ValidazioneDati.isFilled(codTipoServizio)
						|| !ValidazioneDati.isFilled(desTipoServizio) ) {
					throw new ValidationException("Non è stato impostato il tipo servizio");
				}
				Integer idTipoServizio = (Integer)request.getAttribute(ServiziBaseAction.ID_TIPO_SERVIZIO_ATTR);
				if (idTipoServizio==null)
					throw new ValidationException("Non è stato impostato l'ID del tipo servizio");
				Boolean penalita = (Boolean)request.getAttribute(ServiziBaseAction.PENALITA_ATTR);
				if (penalita!=null) dettaglio.setPenalita(penalita.booleanValue());

				dettaglio.setCodiceTipoServizio(codTipoServizio);
				dettaglio.setDesTipoServizio(desTipoServizio);
				dettaglio.setIdTipoServizio(idTipoServizio.intValue());
				dettaglio.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));

				ServizioBibliotecaVO servizio = (ServizioBibliotecaVO)request.getAttribute(ServiziBaseAction.VO_SERVIZIO_ATTR);

				if (servizio == null) {
					dettaglio.setUltimoSalvato(null);
					//Nuovo servizio
					dettaglio.setNuovo(true);
					servizio = new ServizioBibliotecaVO();
					servizio.setCodPolo(navi.getUtente().getCodPolo());
					servizio.setCodBib(dettaglio.getBiblioteca());
					servizio.setCodTipoServ(codTipoServizio);
					dettaglio.setServizio(servizio);
					List<String> lstCodiciServiziAssociati = (List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_SERVIZI_ATTR);
					if (lstCodiciServiziAssociati!=null) {
						dettaglio.setLstCodiciServiziAssociati(lstCodiciServiziAssociati);
					} else {
						dettaglio.setLstCodiciServiziAssociati(new ArrayList<String>());
					}
				} else {
					dettaglio.setNuovo(false);
					//Visualizzazione Dettaglio Servizio
					dettaglio.setServizio(servizio);
					dettaglio.setUltimoSalvato((ServizioBibliotecaVO) servizio.clone());
				}

				//check prenotazione posto
				List<IterServizioVO> iterServizio = getIterServizio(navi.getUtente().getCodPolo(), dettaglio.getBiblioteca(), codTipoServizio, request);
				if (ValidazioneDati.isFilled(iterServizio)) {
					boolean check = ValidazioneDati.listToMap(iterServizio, String.class, "codAttivita")
							.containsKey(StatoIterRichiesta.PRENOTAZIONE_POSTO.getISOCode());
					dettaglio.setRichiedePrenotPosto(check);
				}

				dettaglio.setSessione(true);
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.tipoServizioRichiesto"));
			this.saveErrors(request, errors);
			return backForward(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return backForward(request);
		}

		return mapping.getInputForward();
	}



	public ActionForward Chiudi(ActionMapping mapping, ActionForm form,
									HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		String pathChiamante = ((DettaglioServizioForm)form).getChiamante();
		return backForward(mapping, pathChiamante);
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioServizioForm dettaglio = (DettaglioServizioForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			checkOk(form, request);
			ServizioBibliotecaVO servizioVO = dettaglio.getServizio();

			if (!servizioVO.equals(dettaglio.getUltimoSalvato())) {
				//Ci sono delle modifiche da apportare
				Navigation navi = Navigation.getInstance(request);
				String utente = navi.getUtente().getFirmaUtente();
				Timestamp now = DaoManager.now();

				if (servizioVO.getCodServ().equals("")){
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.impostaCodiceDiritto"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}

				if (servizioVO.getDescr().equals("")){
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.impostaDescrizioneDiritto"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}

				if (dettaglio.isNuovo()) {
					servizioVO.setUteIns(utente);
					servizioVO.setUteVar(utente);
					servizioVO.setTsIns(now);
					servizioVO.setTsVar(now);
					servizioVO.setFlCanc("N");

					//if (servizioVO.impostataPenalita()) {
						PenalitaServizioVO penalita = servizioVO.getPenalita();
						penalita.setUteIns(utente);
						penalita.setUteVar(utente);
						penalita.setTsIns(now);
						penalita.setTsVar(now);
						penalita.setFlCanc("N");
						penalita.setCodBib(servizioVO.getCodBib());
						penalita.setCodServ(servizioVO.getCodServ());
						penalita.setCodTipoServ(servizioVO.getCodTipoServ());

						Short appggSosp = 0;
						penalita.setGgSosp(appggSosp);

						Long appcoeffSosp = new Long(0);
						penalita.setCoeffSosp(appcoeffSosp);
					//}

					//Chiedo conferma all'utente
					dettaglio.setConferma(true);
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
					this.saveErrors(request, errors);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();

				} else {
					//Aggiornamento
					servizioVO.setUteVar(utente);
					servizioVO.setTsVar(now);

					//if (servizioVO.impostataPenalita()) {
						PenalitaServizioVO penalita = servizioVO.getPenalita();
						penalita.setUteVar(utente);
						penalita.setTsVar(now);
						if (penalita.getUteIns()==null || penalita.getUteIns().equals(""))
							penalita.setUteIns(utente);
						if (penalita.getTsIns()==null)
							penalita.setTsIns(now);
						if (penalita.getFlCanc()==null || penalita.getFlCanc().equals(""))
							penalita.setFlCanc("N");
						if (penalita.getCodBib()==null)
							penalita.setCodBib(servizioVO.getCodBib());
						if (penalita.getCodServ()==null)
							penalita.setCodServ(servizioVO.getCodServ());
						if (penalita.getCodTipoServ()==null)
							penalita.setCodTipoServ(servizioVO.getCodTipoServ());
					//}

					if (dettaglio.isPenalita()) {
						if (penalita.getGgSosp() == null) {
							Short appggSosp = 0;
							penalita.setGgSosp(appggSosp);
						}
						if (penalita.getCoeffSosp() == null) {
							Long appcoeffSosp = new Long(0);
							penalita.setCoeffSosp(appcoeffSosp);
						}
					}
					else {
						Short appggSosp = 0;
						penalita.setGgSosp(appggSosp);

						Long appcoeffSosp = new Long(0);
						penalita.setCoeffSosp(appcoeffSosp);
					}

					//Chiedo conferma all'utente
					dettaglio.setConferma(true);
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
					this.saveErrors(request, errors);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();
				}
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			resetToken(request);
			if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward Si(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioServizioForm dettaglio = (DettaglioServizioForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ServizioBibliotecaVO servizioVO = dettaglio.getServizio();
		try {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			GestioneServizi servizi = factory.getGestioneServizi();
			boolean operazioneOK = servizi.aggiornaServizio(Navigation.getInstance(request).getUserTicket(), servizioVO, dettaglio.getIdTipoServizio());
			if (operazioneOK) {
				dettaglio.setUltimoSalvato((ServizioBibliotecaVO)servizioVO.clone());
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
		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return No(mapping, form, request, response);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return No(mapping, form, request, response);
		}
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioServizioForm dettaglio = (DettaglioServizioForm)form;
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



	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {

			if (ValidazioneDati.equals(idCheck, "PRENOTAZIONE_POSTO")) {
				DettaglioServizioForm currentForm = (DettaglioServizioForm) form;
				return currentForm.isRichiedePrenotPosto();
			}

			if (ValidazioneDati.equals(idCheck, "SERVIZI_ILL")) {
				DettaglioServizioForm currentForm = (DettaglioServizioForm) form;
				String tipoSrv = currentForm.getCodiceTipoServizio();
				CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(tipoSrv, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));

				return ts != null && ts.isILL();

			}
		} catch (Exception e) {
			return false;
		}

		return super.checkAttivita(request, form, idCheck);
	}
}
