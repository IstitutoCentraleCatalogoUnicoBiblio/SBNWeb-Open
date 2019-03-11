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
package it.iccu.sbn.web.actions.gestionestampe.semantica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.semantica.StampaThesauroPoloForm;
import it.iccu.sbn.web.actions.servizi.utenti.RichiestaConfermaException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.struts.actions.LookupDispatchAction;

public class StampaThesauroPoloAction extends LookupDispatchAction {


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}


	private void initCombo(ActionForm form) throws Exception {

		StampaThesauroPoloForm currentForm = (StampaThesauroPoloForm) form;
		List lista = new ArrayList();

		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_THESAURO);
		for (TB_CODICI codice : codici) {
			StrutturaCombo elem = new StrutturaCombo();
			elem.setCodice(codice.getCd_tabella());
			elem.setDescrizione(codice.getDs_tabella());
			lista.add(elem);
		}

		currentForm.setListaCodThe(lista);

		lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzNoteThe(lista);

		lista = new ArrayList();
		elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzTerminiBiblio(lista);

		lista = new ArrayList();
		elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzNoteTerminiColl(lista);

		lista = new ArrayList();
		elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzLegamiTitoloDiBiblio(lista);

		lista = new ArrayList();
		elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzTerminiColl(lista);

		lista = new ArrayList();
		elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzTitoli(lista);

		lista = new ArrayList();
		elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		currentForm.setListaOpzFormeRinvio(lista);
	}


	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaThesauroPoloForm currentForm = (StampaThesauroPoloForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				loadDefault(request, form);
				currentForm.setSessione(true);
			}
			currentForm.setElencoModelli(getElencoModelli());
			currentForm.setTipoFormato(TipoStampa.HTML.name());

			return mapping.getInputForward();
		} catch (Exception ex) {
			ex.printStackTrace();
			this.setErroreGenerico(request);
			return mapping.getInputForward();
		}

	}


	private void loadDefault(HttpServletRequest request, ActionForm form)
	throws Exception {
		log.info("loadDefault()");
		this.initCombo(form);
		StampaThesauroPoloForm currentForm = (StampaThesauroPoloForm) form;
		currentForm.setElemBlocco(null);
		currentForm.setCodThe("F");
		currentForm.setInsDal("");
		currentForm.setInsAl("");
		currentForm.setAggDal("");
		currentForm.setAggAl("");
		currentForm.setOpzNoteThe("S");
		currentForm.setOpzTerminiBiblio("N");
		currentForm.setOpzNoteTerminiColl("S");
		currentForm.setOpzLegamiTitoloDiBiblio("N");
		currentForm.setOpzTerminiColl("S");
		currentForm.setOpzTitoli("S");
		currentForm.setOpzFormeRinvio("N");
		currentForm.setTipoRicerca("");
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaThesauroPoloForm currentForm = (StampaThesauroPoloForm) form;
		try {
			request.setAttribute("parametroPassato", currentForm.getElemBlocco());
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	private void checkForm(HttpServletRequest request, ActionForm form, ActionMapping mapping)
	throws RichiestaConfermaException, ValidationException, Exception {
		StampaThesauroPoloForm currentForm = (StampaThesauroPoloForm) form;
		ActionMessages errors = new ActionMessages();
		boolean dateInsConfrontab= true;
		boolean dateAggConfrontab= true;
		boolean missingComboValue = false;
		boolean wrongDate = false;
		boolean dateNull = false;

		if (!ValidazioneDati.strIsNull(currentForm.getCodThe())) {
			missingComboValue = true;
		}
		if (!ValidazioneDati.strIsNull(currentForm.getOpzNoteTerminiColl())) {
			missingComboValue = true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getOpzNoteThe())) {
			missingComboValue = true;
		}
		if (!ValidazioneDati.strIsNull(currentForm.getOpzTerminiBiblio())) {
			missingComboValue = true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getOpzLegamiTitoloDiBiblio())) {
			missingComboValue = true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getOpzTerminiColl())) {
			missingComboValue = true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getOpzTitoli())) {
			missingComboValue = true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getOpzFormeRinvio())) {
			missingComboValue = true;
		}

		//validazione
		if (!ValidazioneDati.strIsNull(currentForm.getInsDal())) {
			String campo = currentForm.getInsDal();
			int codRitorno = -1;
			try {
				codRitorno = ValidazioneDati.validaDataPassata(campo);
				if (codRitorno != ValidazioneDati.DATA_OK) 	throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataFormatoErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica..dataVuotaErrore"));
					this.saveErrors(request, errors);
					break;
				default:break;
				}
				dateInsConfrontab = false;
				wrongDate = true;
			}
		}else{
			dateNull = true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getInsAl())) {
			String campo = currentForm.getInsAl();
			int codRitorno = -1;
			try {
				codRitorno = ValidazioneDati.validaDataPassata(campo);
				if (codRitorno != ValidazioneDati.DATA_OK) 	throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataFormatoErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataVuotaErrore"));
					this.saveErrors(request, errors);
					break;
				default:break;
				}
				dateInsConfrontab = false;
				wrongDate = true;
			}
		}else{
			dateNull = true;
		}

		if(wrongDate){
			errors.add("generico", new ActionMessage("errors.stampaThesauroPolo.inserimento"));
			this.saveErrors(request, errors);
			throw new ValidationException("");
		}
		Date data1;
		Date data2;
		if(!dateNull){
			if(dateInsConfrontab){
				data1 = DateUtil.toDate(currentForm.getInsDal());
				data2 = DateUtil.toDate(currentForm.getInsAl());
				//restituisce 0 se le due date sono uguali
				//restituisce <0 se la prima data è inferiore alla seconda
				//restituisce >0 se la prima data é superiore alla seconda
				if((data1.compareTo(data2)) > 0){
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataInsDalMinoreDataInsAl"));
					this.saveErrors(request, errors);
					throw new ValidationException("");
				}
			}
		}
		dateNull = false;


		if (!ValidazioneDati.strIsNull(currentForm.getAggDal())) {
			String campo = currentForm.getAggDal();
			int codRitorno = -1;
			try {
				codRitorno = ValidazioneDati.validaDataPassata(campo);
				if (codRitorno != ValidazioneDati.DATA_OK) 	throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataFormatoErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataVuotaErrore"));
					this.saveErrors(request, errors);
					break;
				default:break;
				}
				dateAggConfrontab = false;
				wrongDate = true;
			}
		}else{
			dateNull= true;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getAggAl())) {
			String campo = currentForm.getAggAl();
			int codRitorno = -1;
			try {
				codRitorno = ValidazioneDati.validaDataPassata(campo);
				if (codRitorno != ValidazioneDati.DATA_OK) 	throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataFormatoErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataVuotaErrore"));
					this.saveErrors(request, errors);
					break;
				default:break;
				}
				dateAggConfrontab = false;
				wrongDate = true;
//				throw new ValidationException("");
			}
		}else{
			dateNull= true;
		}

		if(wrongDate){
			errors.add("generico", new ActionMessage("errors.stampaThesauroPolo.inserimento"));
			this.saveErrors(request, errors);
			throw new ValidationException("");
		}
		if(!dateNull){
			if(dateAggConfrontab){
				data1 = DateUtil.toDate(currentForm.getAggDal());
				data2 = DateUtil.toDate(currentForm.getAggAl());
				//restituisce 0 se le due date sono uguali
				//restituisce <0 se la prima data è inferiore alla seconda
				//restituisce >0 se la prima data é superiore alla seconda
				if((data1.compareTo(data2)) > 0){
					errors.add("generico", new ActionMessage("errors.gestioneSemantica.dataAggDalMinoreDataAggAl"));
					this.saveErrors(request, errors);
					throw new ValidationException("");
				}
			}
		}


	}


	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaThesauroPoloForm currentForm = (StampaThesauroPoloForm) form;
		try {

			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				loadDefault(request, form);
				currentForm.setSessione(true);
			}
			request.setAttribute("parametroPassato", currentForm.getElemBlocco());
			this.checkForm(request, form, mapping);

			Navigation navi = Navigation.getInstance(request);
			UserVO utente = navi.getUtente();
			String ticket = utente.getTicket();

			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

			String fileJrxml = currentForm.getTipoModello();
			StampaTerminiThesauroVO stampa = new StampaTerminiThesauroVO();
			//String denominazione della biblioteca
			stampa.setCodeThesauro(currentForm.getCodThe());
			stampa.setDenBib(utente.getBiblioteca());
			stampa.setCodBib(utente.getCodPolo());
			stampa.setCodPolo(utente.getCodBib());

			String descrizioneThesauro = delegate.getDescrizioneCodice(CodiciType.CODICE_THESAURO, currentForm.getCodThe());
			stampa.setDescThesauro(descrizioneThesauro);

			java.util.Date parsedDate = null;
			java.sql.Timestamp timestamp = null;
			if (!ValidazioneDati.strIsNull(currentForm.getInsDal())) {
				parsedDate = DateUtil.toDate(currentForm.getInsDal());//dateFormat.parse("2006-05-22 14:04:59:612");
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
				stampa.setDateInsDa(timestamp);
			}

			if (!ValidazioneDati.strIsNull(currentForm.getInsAl())) {
				parsedDate = DateUtil.toDate(currentForm.getInsAl());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
				stampa.setDateInsA(timestamp);
			}

			if (!ValidazioneDati.strIsNull(currentForm.getAggDal())) {
				parsedDate = DateUtil.toDate(currentForm.getAggDal());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
				stampa.setDateAggDa(timestamp);
			}

			if (!ValidazioneDati.strIsNull(currentForm.getAggAl())) {
				parsedDate = DateUtil.toDate(currentForm.getAggAl());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
				stampa.setDateAggA(timestamp);
			}

			stampa.setStampaTermBiblio(currentForm.getOpzTerminiBiblio());
			stampa.setStampaTitoli(currentForm.getOpzTitoli());

			stampa.setStampaNoteThes(currentForm.getOpzNoteThe());
			stampa.setStampaNoteTitle(currentForm.getOpzNoteTerminiColl());

			String idUtente= utente.getUserId();

			List inputForStampeService=new ArrayList();
			inputForStampeService.add(stampa);

			StampaDiffVO richiesta = new StampaDiffVO();
			String tipoFormato=currentForm.getTipoFormato();

			String pathDownload = StampeUtil.getBatchFilesPath();

			richiesta.setCodAttivita(CodiciAttivita.getIstance().STAMPA_THESAURO_POLO);
			richiesta.setTipoStampa(tipoFormato);
			richiesta.setUser(idUtente);
			richiesta.setCodPolo(utente.getCodPolo());
			richiesta.setCodBib(utente.getCodBib());

			List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(richiesta.getCodAttivita());
			if (!ValidazioneDati.isFilled(modelli)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.stampe.modelli.conf"));
				resetToken(request);
				return mapping.getInputForward();
			}

			String jrxml = modelli.get(0).getJrxml();

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml = basePath + File.separator + "jrxml" + File.separator + jrxml;
			//codice standard inserimento messaggio di richiesta stampa differita
			richiesta.setTemplate(pathJrxml);
			richiesta.setTipoStampa(currentForm.getTipoFormato());

			richiesta.setTipoOrdinamento("");
			richiesta.setParametri(inputForStampeService);
			richiesta.setTemplate(pathJrxml);
			richiesta.setDownload(pathDownload);
			richiesta.setDownloadLinkPath("/");
			richiesta.setTipoOperazione("STAMPA_TERMINI_THESAURO");
			richiesta.setTicket(ticket);
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			richiesta.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, richiesta, null);

			if (idBatch == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
				resetToken(request);
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));

		} catch (ValidationException e)  {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			e.printStackTrace();
			this.setErroreGenerico(request);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().STAMPA_THESAURO_POLO);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	protected void setErroreGenerico(HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneSemantica.generico"));
		this.saveErrors(request, errors);
	}
}


