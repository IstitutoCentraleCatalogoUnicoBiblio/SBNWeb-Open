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
package it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.sezioniCollocazioni.SezioniCollocazioniFormatiGestioneForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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

public class SezioniCollocazioniFormatiGestioneAction extends LookupDispatchAction  implements SbnAttivitaChecker{

	private static Log log = LogFactory.getLog(SezioniCollocazioniGestioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.aggiorna", "aggiorna");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.lsFormatiSezioni", "lsFormatiSezioni");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();
		// controllo se ho già i dati in sessione;
		if (!currentForm.isSessione()) {
			currentForm.setTicket(navi.getUserTicket());
			UserVO utente = navi.getUtente();
			currentForm.setCodPolo(utente.getCodPolo());
			currentForm.setCodBib(utente.getCodBib());
			currentForm.setDescrBib(utente.getBiblioteca());
			currentForm.setSessione(true);
		}
		try{
			if (request.getAttribute("codPolo") != null &&
					request.getAttribute("codBib") != null &&
					request.getAttribute("codSez") != null &&
					request.getAttribute("descrBib") != null){
				currentForm.setCodPolo((String)request.getAttribute("codPolo"));
				currentForm.setCodBib((String)request.getAttribute("codBib"));
				currentForm.setCodSez((String)request.getAttribute("codSez"));
				currentForm.setDescrBib((String)request.getAttribute("descrBib"));
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.campoNonImp"));

				return navi.goBack(true);
			}
			SezioneCollocazioneVO sez = this.getSezioneDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(),
					currentForm.getCodSez(), currentForm.getTicket());
			currentForm.setSezione(sez);

			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("nuova")) {
				currentForm.setProv("nuova");
				this.loadPaginaVuota(form);
				currentForm.setDisableNSerieNum1Misc(true);
				currentForm.setDisableDalProgrMisc(true);
				currentForm.setDisableNSerieNum2Misc(true);
				currentForm.setDisableAlProgrMisc(true);
				currentForm.setModifica(false);
				return mapping.getInputForward();
			}else if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("modifica")) {
				currentForm.setDisableFormato(true);
				currentForm.setDisableDescrFormato(false);
				currentForm.setDisableNPezzi(true);
//				if (currentForm.getSezione().getTipoSezione() != null && currentForm.getSezione().getTipoSezione().equals("L")){
					currentForm.setDisableNPezziMisc(false);
//				}
				currentForm.setDisableDimDa(false);
				currentForm.setDisableDimA(false);
				currentForm.setDisableNSerieNum1Misc(true);
				currentForm.setDisableDalProgrMisc(true);
				currentForm.setDisableNSerieNum2Misc(true);
				currentForm.setDisableAlProgrMisc(true);
				currentForm.setProv("modifica");
				currentForm.setModifica(true);
			}else if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("esamina")) {
				currentForm.setDisable(true);
				currentForm.setDisableFormato(true);
				currentForm.setDisableDescrFormato(true);
				currentForm.setDisableNPezzi(true);
				currentForm.setDisableNSerie(true);
				currentForm.setDisablePrgAss(true);
				currentForm.setDisableDimDa(true);
				currentForm.setDisableDimA(true);
//				if (currentForm.getSezione().getTipoSezione() != null && currentForm.getSezione().getTipoSezione().equals("L")){
					currentForm.setDisableNPezziMisc(true);
					currentForm.setDisableNSerieNum1Misc(true);
					currentForm.setDisableDalProgrMisc(true);
					currentForm.setDisableNSerieNum2Misc(true);
					currentForm.setDisableAlProgrMisc(true);
//				}
				currentForm.setProv("esamina");
				currentForm.setModifica(false);
			}
			FormatiSezioniVO formato = (FormatiSezioniVO)request.getAttribute("scelForSez");
			if (formato != null) {
				currentForm.getRecFormatiSezioni().setCodPolo(formato.getCodPolo());
				currentForm.getRecFormatiSezioni().setCodBib((formato.getCodBib()));
				currentForm.getRecFormatiSezioni().setCodSez((formato.getCodSez()));
				currentForm.setCodFormato((formato.getCodFormato()));
				FormatiSezioniVO rec = this.getFormatiSezioniDettaglio(
						currentForm.getCodPolo(), currentForm.getCodBib(),
						currentForm.getCodSez(), currentForm.getCodFormato(),
						currentForm.getTicket());
				if (rec != null){
					currentForm.setRecFormatiSezioni(rec);
					currentForm.setDataIns(rec.getDataIns());
					currentForm.setDataAgg(rec.getDataAgg());
					currentForm.setUteIns(rec.getUteIns());
					currentForm.setUteAgg(rec.getUteAgg());
					currentForm.setNumAss(currentForm.getRecFormatiSezioni().getProgNum());
					currentForm.setNumSerie(currentForm.getRecFormatiSezioni().getProgSerie());
					//almaviva5_20140911 #5638
					formattaDescrizione(currentForm);
					currentForm.setDisableFormato(true);
				}
				if (currentForm.isModifica() && rec.getNumeroPezziMisc() ==  0){
					currentForm.setDisableNPezziMisc(false);
				}
				return mapping.getInputForward();
			}
			if (request.getAttribute("scelForBib") != null) {
				currentForm.setRecFormatiSezioni((FormatiSezioniVO)request.getAttribute("scelForBib"));
				currentForm.getRecFormatiSezioni().setCodPolo(currentForm.getCodPolo());
				currentForm.getRecFormatiSezioni().setCodBib(currentForm.getCodBib());
				currentForm.getRecFormatiSezioni().setCodSez(currentForm.getCodSez());
				if (currentForm.getProv() != null && !currentForm.getProv().equals("nuova")){
					currentForm.getRecFormatiSezioni().setCodFormato(currentForm.getCodFormato().trim());
					currentForm.getRecFormatiSezioni().setDataIns(currentForm.getDataIns());
					currentForm.getRecFormatiSezioni().setDataAgg(currentForm.getDataAgg());
					currentForm.getRecFormatiSezioni().setUteIns(currentForm.getUteIns());
					currentForm.getRecFormatiSezioni().setUteAgg(currentForm.getUteAgg());
				}
				//
//				if (currentForm.getProv() != null && !currentForm.getProv().equals("nuova")) {
//					currentForm.setNumAss(currentForm.getRecFormatiSezioni().getProgNum());
//					currentForm.setNumSerie(currentForm.getRecFormatiSezioni().getProgSerie());
//				}else{
//					currentForm.getRecFormatiSezioni().setProgSerie(0);
//					currentForm.getRecFormatiSezioni().setProgNum(0);
//				}
				currentForm.getRecFormatiSezioni().setProgSerie(0); //era già così, poi è stato modificato con le righe asteriscate sopra, ora di nuovo così
				currentForm.getRecFormatiSezioni().setProgNum(0);
				currentForm.getRecFormatiSezioni().setProgSerieNum1Misc(0);
				currentForm.getRecFormatiSezioni().setDalProgrMisc(0);
				currentForm.getRecFormatiSezioni().setProgSerieNum2Misc(0);
				currentForm.getRecFormatiSezioni().setAlProgrMisc(0);
				currentForm.setDescrFormato(currentForm.getRecFormatiSezioni().getDescrFor().trim());
				if(currentForm.getRecFormatiSezioni().getDescrFor() != null && !currentForm.getRecFormatiSezioni().getDescrFor().trim().equals("")){
					String descrFor [] = currentForm.getRecFormatiSezioni().getDescrFor().trim().split(";");
					if (descrFor.length == 1){
						currentForm.getRecFormatiSezioni().setDescrFor(descrFor[0]);
					}else if (descrFor.length == 3){
						currentForm.getRecFormatiSezioni().setDimensioneDa(Integer.parseInt(descrFor[0]));
						currentForm.getRecFormatiSezioni().setDimensioneA(Integer.parseInt(descrFor[1]));
						currentForm.getRecFormatiSezioni().setDescrFor(descrFor[2]);
						//					currentForm.setCodFormato(descrFor[2]);
					}else if (descrFor.length == 2){
						currentForm.getRecFormatiSezioni().setDimensioneDa(Integer.parseInt(descrFor[0]));
						currentForm.getRecFormatiSezioni().setDimensioneA(Integer.parseInt(descrFor[1]));
						currentForm.getRecFormatiSezioni().setDescrFor("");
						//					currentForm.setCodFormato(descrFor[2]);
					}
				}
				currentForm.setDisableFormato(true);
				if (currentForm.isModifica() && currentForm.getRecFormatiSezioni().getNumeroPezziMisc() ==  0){
					currentForm.setDisableNPezziMisc(false);
					}
				return mapping.getInputForward();
			}
			FormatiSezioniVO rec = this.getFormatiSezioniDettaglio(currentForm.getRecFormatiSezioni().getCodPolo(),
					currentForm.getRecFormatiSezioni().getCodBib(), currentForm.getRecFormatiSezioni().getCodSez(),
					currentForm.getRecFormatiSezioni().getCodFormato(),
					currentForm.getTicket());
			if (rec != null){
				currentForm.setNumSerie(rec.getProgSerie());
				currentForm.setNumAss(rec.getProgNum());
				rec.setCodSez(currentForm.getCodSez());
				currentForm.setRecFormatiSezioni(rec);
				currentForm.setDate(true);
				if (currentForm.isModifica() && rec.getNumeroPezziMisc() ==  0){
					currentForm.setDisableNPezziMisc(false);
				}
			}
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	private void formattaDescrizione(ActionForm form) {
		//almaviva5_20140911 #5638
		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		FormatiSezioniVO formato = currentForm.getRecFormatiSezioni();
		String descrFor[] = formato.getDescrFor().split(";");
		if (descrFor.length == 1) {
			formato.setDescrFor(descrFor[0]);
		} else if (descrFor.length == 3) {
			formato.setDimensioneDa(Integer.parseInt(descrFor[0]));
			formato.setDimensioneA(Integer.parseInt(descrFor[1]));
			formato.setDescrFor(descrFor[2]);
		} else if (descrFor.length == 2) {
			formato.setDimensioneDa(Integer.parseInt(descrFor[0]));
			formato.setDimensioneA(Integer.parseInt(descrFor[1]));
			formato.setDescrFor("");
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	  * SezioniCollocazioniFormatiGestioneAction.java
	  * splitDiemnsioniDescr
	  * void
	  * @param currentForm
	  *
	  *
	 */
//	private void splitDimensioniDescr(
//			SezioniCollocazioniFormatiGestioneForm currentForm) {
//		String token[] = currentForm.getRecFormatiSezioni().getDescrFor().split(FormatiSezioniVO.SEPARATOREFORSEZ);
//		currentForm.getRecFormatiSezioni().setDimensioneDa(Integer.valueOf(token[0]));
//		currentForm.getRecFormatiSezioni().setDimensioneA(Integer.valueOf(token[1]));
//		if (token.length > 2) {
//			currentForm.getRecFormatiSezioni().setDescrFor(token[2]);
//		}else{
//			currentForm.getRecFormatiSezioni().setDescrFor("");
//		}
//	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		FormatiSezioniVO formato = currentForm.getRecFormatiSezioni();
		try {
			validaInput(currentForm);
			// insert sezione
			String op = currentForm.getProv();
			if (ValidazioneDati.equals(op, "nuova")) {
				String dimensioneDa = new String(gruppoNumerico(String.valueOf(formato.getDimensioneDa())) + ";");
				String dimensioneA = new String(gruppoNumerico(String.valueOf(formato.getDimensioneA())) + ";");
				currentForm.setDescrFormato(new String (formato.getDescrFor().trim()));//bug 0004839 esercizio

				formato.setDescrFor(dimensioneDa + dimensioneA + formato.getDescrFor());
				if (ValidazioneDati.length(formato.getDescrFor()) > 30) {//10+20(dimensione formato + descrizione)
					throw new ValidationException("validaFormatiSezioniDescrFormatoEccedente", ValidationException.errore);
				}
				Navigation navi = Navigation.getInstance(request);
				UserVO utente = navi.getUtente();
				formato.setUteAgg(utente.getFirmaUtente());
				if (formato.getNumeroPezziMisc() > 0){
					if (String.valueOf(formato.getNumeroPezziMisc()).length() > 2){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.lunghezzaNumPezziMiscErrata"));

						return mapping.getInputForward();
					}
				}
				if (this.insertFormatiSezioni(formato, currentForm.getTicket())) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));

					this.saveToken(request);
					request.setAttribute("codPolo", currentForm.getCodPolo());
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("codSez", currentForm.getCodSez());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					//					request.setAttribute("prov", "sezForGest");
					return navi.goBack();
				}
				//				return mapping.findForward("ok");
			}
			else if (ValidazioneDati.equals(op, "modifica")) {
//				String dimensioneDa = new String(gruppoNumerico(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneDa())) + ";");
//				String dimensioneA = new String(gruppoNumerico(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneA())) + ";");
//				currentForm.setDescrFormato(new String (currentForm.getRecFormatiSezioni().getDescrFor().trim()));//bug 0004839 esercizio
//
//				currentForm.getRecFormatiSezioni().setDescrFor(dimensioneDa + dimensioneA + currentForm.getRecFormatiSezioni().getDescrFor());
//				if (currentForm.getRecFormatiSezioni().getDescrFor() != null && currentForm.getRecFormatiSezioni().getDescrFor().length() > 30){//10+20(dimensione formato + descrizione)
//					throw new ValidationException("validaFormatiSezioniDescrFormatoEccedente", ValidationException.errore);
//				}
				if (ValidazioneDati.length(formato.getDescrFor()) > 20)
					throw new ValidationException("validaFormatiSezioniDescrFormatoEccedente", ValidationException.errore);
				//almaviva5_20140911 #5638
				boolean changePrgSerie = (formato.getProgSerie() != currentForm.getNumSerie() );
				if (changePrgSerie && formato.getProgSerie() < currentForm.getNumSerie()){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prgSerieNonPuoEssereMinoreDelValorePrec"));
					return mapping.getInputForward();
				}
				if (!changePrgSerie && formato.getProgNum() < currentForm.getNumAss()){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prgAssNonPuoEssereMinoreDelValorePrec"));
					return mapping.getInputForward();
				}
				// richiesta conferma update sezione

				//almaviva5_20140911 #5638
				if (!changePrgSerie)
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaModifica"));
				else {
					//incrementando la serie il numero assegnato viene impostato a 0
					formato.setProgNum(0);
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoSezione.incr.serie.confermaModifica"));
				}

				currentForm.setDisableDescrFormato(true);
				currentForm.setDisableNSerie(true);
				currentForm.setDisablePrgAss(true);
				currentForm.setDisableDimA(true);
				currentForm.setDisableDimDa(true);
				currentForm.setDisableNPezziMisc(true);
				this.saveToken(request);

				currentForm.setConferma(true);
				return mapping.getInputForward();
			} else {
				return mapping.findForward("ok");
			}
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			if (currentForm.getDescrFormato() != null){
				formato.setDescrFor(currentForm.getDescrFormato());//bug 0004839 esercizio
			}
			return mapping.getInputForward();
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			if (currentForm.getDescrFormato() != null){
				formato.setDescrFor(currentForm.getDescrFormato());//bug 0004839 esercizio
			}
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			if (currentForm.getDescrFormato() != null){
				formato.setDescrFor(currentForm.getDescrFormato());//bug 0004839 esercizio
			}
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	  * SezioniCollocazioniFormatiGestioneAction.java
	  * validaInput
	  * void
	  * @param currentForm
	  * @throws ValidationException
	  *
	  *
	 */
	private void validaInput(SezioniCollocazioniFormatiGestioneForm currentForm)
			throws ValidationException {
		if (currentForm.getRecFormatiSezioni().getCodFormato()!=null && currentForm.getRecFormatiSezioni().getCodFormato().trim().length()==0) {
			throw new ValidationException("validaFormatiSezioniCodFormatoObbligatorio", ValidationException.errore);
		}
		if (currentForm.getRecFormatiSezioni().getCodFormato()!=null && currentForm.getRecFormatiSezioni().getCodFormato().trim().length()!=0) {
			if (currentForm.getRecFormatiSezioni().getCodFormato().length()>2) {
				throw new ValidationException("validaFormatiSezioniCodFormatoEccedente", ValidationException.errore);
			}
		}
		if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecFormatiSezioni().getNumeroPezzi()))) {
			if  (!ValidazioneDati.strIsNumeric(String.valueOf((currentForm.getRecFormatiSezioni().getNumeroPezzi())))) {
				throw new ValidationException("validaFormatiSezioniNumericoNumeroPezzi", ValidationException.errore);
			}
			if ((String.valueOf(currentForm.getRecFormatiSezioni().getNumeroPezzi()).length() > 6)) {
				throw new ValidationException("validaFormatiSezioniNumeroPezziEccedente", ValidationException.errore);
			}
		}
		if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecFormatiSezioni().getProgNum()))) {
			if (!ValidazioneDati.strIsNumeric(String.valueOf((currentForm.getRecFormatiSezioni().getProgNum())))) {
				throw new ValidationException("validaFormatiSezioniNumericoProgNum", ValidationException.errore);
			}
			if ((String.valueOf(currentForm.getRecFormatiSezioni().getProgNum()).length() > 6)) {
				throw new ValidationException("validaFormatiSezioniProgNumEccedente", ValidationException.errore);
			}
		}
		if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecFormatiSezioni().getProgSerie()))) {
			if (!ValidazioneDati.strIsNumeric(String.valueOf((currentForm.getRecFormatiSezioni().getProgSerie())))) {
				throw new ValidationException("validaFormatiSezioniNumericoProgSerie", ValidationException.errore);
			}
			if ((String.valueOf(currentForm.getRecFormatiSezioni().getProgSerie()).length() > 6)) {
				throw new ValidationException("validaFormatiSezioniProgSerieEccedente", ValidationException.errore);
			}
		}
		if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneDa()))) {
			if  (!ValidazioneDati.strIsNumeric(String.valueOf((currentForm.getRecFormatiSezioni().getDimensioneDa())))) {
				throw new ValidationException("validaFormatiSezioniDimensioneDa", ValidationException.errore);
			}
			if ((String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneDa()).length() > 4)) {
				throw new ValidationException("validaFormatiSezioniDimensioneDaEccedente", ValidationException.errore);
			}
		}
		if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneA()))) {
			if  (!ValidazioneDati.strIsNumeric(String.valueOf((currentForm.getRecFormatiSezioni().getDimensioneA())))) {
				throw new ValidationException("validaFormatiSezioniDimensioneA", ValidationException.errore);
			}
			if ((String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneA()).length() > 4)) {
				throw new ValidationException("validaFormatiSezioniDimensioneAEccedente", ValidationException.errore);
			}
		}
		if (currentForm.getRecFormatiSezioni().getDimensioneDa() > currentForm.getRecFormatiSezioni().getDimensioneA()){
			throw new ValidationException("validaFormatiSezioniDimensioneANonValida", ValidationException.errore);
		}
		if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecFormatiSezioni().getNumeroPezziMisc()))) {
			if  (!ValidazioneDati.strIsNumeric(String.valueOf((currentForm.getRecFormatiSezioni().getNumeroPezziMisc())))) {
				throw new ValidationException("validaFormatiSezioniNumericoNumeroPezziMisc", ValidationException.errore);
			}
			if ((currentForm.getRecFormatiSezioni().getNumeroPezziMisc()) < 1) {
				throw new ValidationException("validaFormatiSezioniNumeroPezziMiscDeveEssereMaggioreDi0", ValidationException.errore);
			}
			if ((String.valueOf(currentForm.getRecFormatiSezioni().getNumeroPezziMisc()).length() > 6)) {
				throw new ValidationException("validaFormatiSezioniNumeroPezziMiscEccedente", ValidationException.errore);
			}
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		try {
//			if (currentForm.getProv().equals("nuova")) {
//
//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaModifica"));
//
//				this.saveToken(request);
//				currentForm.setDisableDescrFormato(true);
//				currentForm.setDisableNSerie(true);
//				currentForm.setDisablePrgAss(true);
//				currentForm.setConferma(true);
//				return mapping.getInputForward();
//			} else {
				request.setAttribute("codPolo", currentForm.getCodPolo());
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("codSez", currentForm.getCodSez());
				request.setAttribute("descrBib", currentForm.getDescrBib());
				return mapping.findForward("chiudi");
//			}
			// request.setAttribute("codBib",currentForm.getCodBib());
			// return mapping.findForward("chiudi");
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if (!currentForm.isSessione()) {
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}

			String token[] = currentForm.getRecFormatiSezioni().getCodFormato().split(FormatiSezioniVO.SEPARATORE);
			if (token.length != 5) {
				// errore
			}

			currentForm.getRecFormatiSezioni().setCodPolo(token[0]);
			currentForm.getRecFormatiSezioni().setCodBib(token[1]);
			currentForm.getRecFormatiSezioni().setCodFormato(token[2]);
			currentForm.getRecFormatiSezioni().setDescrFor(token[3]);
			currentForm.getRecFormatiSezioni().setNumeroPezzi(Integer.valueOf(token[4]));

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadPaginaVuota(ActionForm form) throws Exception {
		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		FormatiSezioniVO rec = new FormatiSezioniVO();
		rec.setCodPolo(currentForm.getCodPolo());
		rec.setCodBib(currentForm.getCodBib());
		rec.setCodSez(currentForm.getCodSez());
		rec.setCodFormato(null);
		rec.setDescrFor(null);
		rec.setDescrFor(null);
		rec.setProgSerie(0);
		rec.setProgNum(0);
//		if (currentForm.getSezione().getTipoSezione() != null && currentForm.getSezione().getTipoSezione().equals("L")){
			rec.setNumeroPezziMisc(20);//defualt proposto
			rec.setDalProgrMisc(0);
			rec.setAlProgrMisc(0);
//		}
		rec.setDimensioneDa(0);
		rec.setDimensioneA(0);
		rec.setNumeroPezzi(999999);

		currentForm.setRecFormatiSezioni(rec);
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		try {
			if (currentForm.getProv().equals("modifica")) {
				String dimensioneDa = (gruppoNumerico(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneDa())) + ";");
				String dimensioneA = (gruppoNumerico(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneA())) + ";");

				currentForm.getRecFormatiSezioni().setDescrFor(dimensioneDa + dimensioneA + currentForm.getRecFormatiSezioni().getDescrFor());
				currentForm.getRecFormatiSezioni().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				if (this.updateFormatiSezioni(currentForm.getRecFormatiSezioni(), currentForm.getTicket())) {
					request.setAttribute("codPolo",currentForm.getCodPolo());
					request.setAttribute("codBib",currentForm.getCodBib());
					request.setAttribute("codSez",currentForm.getCodSez());
					request.setAttribute("descrBib",currentForm.getDescrBib());
					currentForm.setConferma(false);


					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

					return Navigation.getInstance(request).goBack();
//					return mapping.findForward("ok");
				}
			} else if (currentForm.isConferma()) {
				// insert formato sezione
				String dimensioneDa = (gruppoNumerico(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneDa())) + ";");
				String dimensioneA = (gruppoNumerico(String.valueOf(currentForm.getRecFormatiSezioni().getDimensioneA())) + ";");

				currentForm.getRecFormatiSezioni().setDescrFor(dimensioneDa + dimensioneA + currentForm.getRecFormatiSezioni().getDescrFor());
				if (this.insertFormatiSezioni(currentForm.getRecFormatiSezioni(), currentForm.getTicket())) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOK"));

					request.setAttribute("codPolo",currentForm.getCodPolo());
					request.setAttribute("codBib",currentForm.getCodBib());
					request.setAttribute("codSez",currentForm.getCodSez());
					request.setAttribute("descrBib",currentForm.getDescrBib());
					currentForm.setConferma(false);
					return mapping.findForward("ok");
				}
			} else {
				currentForm.setConferma(false);
				return mapping.findForward("ok");
			}
		} catch (DataException e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		} catch (ValidationException e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();

		} finally {
			//almaviva5_20140911 #5638
			formattaDescrizione(currentForm);
		}
		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		// Viene settato il token per le transazioni successive
		try {
			request.setAttribute("codPolo",currentForm.getCodPolo());
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("codSez",currentForm.getCodSez());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("prov","sezForGest");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward lsFormatiSezioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		try {
			request.setAttribute("codPolo",currentForm.getCodPolo());
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSez",currentForm.getCodSez());
			request.setAttribute("esamina",currentForm.getProv());
			request.setAttribute("prov", "listaFB");
			return Navigation.getInstance(request).goForward(mapping.findForward("lenteForBib"));
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private boolean insertFormatiSezioni(FormatiSezioniVO formatiSezioni, String ticket)
			throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertFormatiSezioni(
				formatiSezioni, ticket);
		return ret;
	}

	private boolean updateFormatiSezioni(FormatiSezioniVO formatiSezioni, String ticket)
			throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateFormatiSezioni(formatiSezioni, ticket);
		return ret;
	}

	private FormatiSezioniVO getFormatiSezioniDettaglio(String codPolo, String codBib, String codSez,
			String codFormato, String ticket)
	throws Exception {
		FormatiSezioniVO rec;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getFormatiSezioniDettaglio(codPolo, codBib, codSez, codFormato, ticket);
		return rec;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		SezioniCollocazioniFormatiGestioneForm currentForm = (SezioniCollocazioniFormatiGestioneForm) form;
		// gestione bottoni
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_SEZIONI_DI_COLLOCAZIONE, currentForm.getCodPolo(), currentForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}

	}
	private String gruppoNumerico(String gruppo) {

		// Calcolo la lunghezza del gruppo numerico e del carattere
		// di controllo
		int limit;
		int quartine = 0;

		//tolgo gli zeri non significativi
		gruppo = Integer.valueOf(gruppo).toString();

		int len = gruppo.length();
		if ( (len % 6) == 0)
			return gruppo;

		for (limit = 3; limit < len; limit += 4)
			quartine++;

		// Formatta il numero a 6 (...12....18..24 Etc) cifre
		// mettendo davanti tutti '0'
		if (quartine > 0)
			return String.valueOf(quartine) + ValidazioneDati.fillLeft(gruppo, '0', limit);
		else
			return ValidazioneDati.fillLeft(gruppo, '0', limit + 1);
	}
	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}


}
