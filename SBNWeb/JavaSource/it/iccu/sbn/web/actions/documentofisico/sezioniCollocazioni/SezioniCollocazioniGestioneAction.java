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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.documentofisico.sezioniCollocazioni.SezioniCollocazioniGestioneForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
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

/**
 * SezioniCollocazioniGestioneAction.java
 * dic-2006
 *
 */
public class SezioniCollocazioniGestioneAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(SezioniCollocazioniGestioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.bottone.formati", "formati");
//		map.put("documentofisico.bottone.ok", "ok");
		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.aggiorna", "aggiorna");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try{
			SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
				return mapping.getInputForward();
			// controllo se ho già i dati in sessione;
			if (!myForm.isSessione()) {
				log.debug("SezioniCollocazioniGestioneAction::unspecified()");
				myForm.setTicket(navi.getUserTicket());
				myForm.setCodPolo(navi.getUtente().getCodPolo());
				myForm.setCodBib(navi.getUtente().getCodBib());
				myForm.setDescrBib(navi.getUtente().getBiblioteca());
				myForm.setSessione(true);
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaForSez")) {
				request.setAttribute("codBib",myForm.getRecSezione().getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				return navi.goBack();
			}
			if (request.getAttribute("codBib") != null
					&& request.getAttribute("descrBib") != null) {
				myForm.setCodBib((String)request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
				//quando è modifica o esamina
				if (request.getAttribute("codSez") != null){
					myForm.getRecSezione().setCodSezione((String)request.getAttribute("codSez"));
				}
			}
			//caricamento combo
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			myForm.setListaTipoSezione(caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceTipoSezione()));
			myForm.setListaTipoCollocazione(caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceTipoCollocazione()));
			if ((myForm.getRecSezione().getTipoSezione() != null && !myForm.getRecSezione().getTipoSezione().equals("")) &&
					myForm.getRecSezione().getTipoSezione().equals("L")){
				//con onClick nella lista tipo collocazione carico soltanto a formato e continuazione
				if (myForm.getListaTipoCollocazione() != null){
					if (myForm.getListaTipoCollocazione().size() > 0){
						List listaRidotta = new ArrayList<CodiceVO>();
						for (int i = 0; i < myForm.getListaTipoCollocazione().size(); i++ ){
							CodiceVO rec = (CodiceVO)myForm.getListaTipoCollocazione().get(i);
							if (rec.getCodice().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO) ||
									rec.getCodice().equals("C")){
								listaRidotta.add(rec);
							}
						}
						myForm.setListaTipoCollocazione(listaRidotta);
					}
				}
			}

			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("nuova")) {
				myForm.setDisable(false);
				myForm.setProv("nuova");
				this.loadPaginaVuota(request, form);
				myForm.setTastoAggiorna(true);
			} else {
				// carico i dati
				if (request.getAttribute("prov") != null && myForm.getRecSezione().getTipoColloc() == null) {
					SezioneCollocazioneVO sezDettaglio = this.getSezioneDettaglio(myForm.getCodPolo(), myForm.getCodBib(),
							myForm.getRecSezione().getCodSezione(), myForm.getTicket());
					if (sezDettaglio != null){
						myForm.setRecSezione(sezDettaglio);
						if (request.getAttribute("prov").equals("esamina")){
							myForm.setProv("esamina");
							this.loadRigaSel(form);
							myForm.setDisableSez(true);
							myForm.setDisableInvColl(true);
							myForm.setDisableNote(true);
							myForm.setDisableDescr(true);
							myForm.setDisableInvPrev(true);
							myForm.setDisableSistClass(true);
							myForm.setDisableNote(true);
							myForm.setDisableDescr(true);
							myForm.setDisableTipoSez(true);
							myForm.setDisableTipoColl(true);
							myForm.setTastoAggiorna(false);
							myForm.setTastoFormati(true);
							myForm.setDisable(true);
							myForm.setEsamina(true);
						}else if (request.getAttribute("prov").equals("modifica")) {
							myForm.setProv("modifica");
							myForm.setDisable(false);
							this.loadRigaSel(form);
						}
					}
				}
					//le successive istruzioni vengono eseguite quando sono abilitati javaScripts
					myForm.setUltPrgAss(false);
					if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)) {
						myForm.setUltPrgAss(true);
					}
					myForm.setSistCla(false);
					if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
						if (myForm.getListaClassificazioni() == null){
								//almaviva5_20130307 #4677 lista sistemi classificazioni da profilo biblioteca e non utente
								myForm.setListaClassificazioni(CaricamentoComboSemantica.loadComboSistemaClassificazioneBiblioteca(
										myForm.getCodPolo(), myForm.getCodBib(), true));
								if (myForm.getRecSezione().getClassific() == null ||
										myForm.getRecSezione().getClassific() != null && myForm.getRecSezione().getClassific().trim().equals("")){
									myForm.getRecSezione().setClassific("D");
								}
						}
						myForm.setSistCla(true);
					}
					myForm.setTastoFormati(false);
					if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO) ||
							myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE) ||
							myForm.getRecSezione().getTipoSezione().equals(DocumentoFisicoCostant.MISCELLANEA) ){
						myForm.setTastoFormati(true);
					}
				}

			return mapping.getInputForward();

		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	  * SezioniCollocazioniGestioneAction.java
	  * ok
	  * ActionForward
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  *
	  * scelta di creazione di una sezione di collocazione
	 */
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		try {
			// insert sezione
			if (myForm.getProv().equals("nuova")) {
//				if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)){
//
//					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.evolutivaFormatiSezioniInCorsoScelta"));
//
//					return mapping.getInputForward();
//				}else{
					if (this.insertSezione(myForm.getRecSezione(), myForm.getTicket())) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));

						this.saveToken(request);
						return Navigation.getInstance(request).goBack();
					}
//				}
			} else if (myForm.getProv().equals("modifica")) {
				// richiesta conferma update sezione

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaModifica"));

				this.saveToken(request);
				myForm.setConferma(true);
				return mapping.getInputForward();
			} else {
				return mapping.findForward("ok");
			}
		} catch (ValidationException e) {
			if (e.getMsg()!=null){
				if (e.getMsg().equals("msgSez")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

					this.saveToken(request);
					myForm.setConferma(true);
					myForm.setDisableSez(true);
					myForm.setDisableInvColl(true);
					myForm.setDisableNote(true);
					myForm.setDisableDescr(true);
					myForm.setDisableInvPrev(true);
					myForm.setDisableSistClass(true);
					myForm.setDisableNote(true);
					myForm.setDisableDescr(true);
					myForm.setDisableUltPrg(true);
					myForm.setDisableTipoSez(true);
					myForm.setDisableTipoColl(true);
					myForm.setTastoAggiorna(false);
					myForm.setDisable(true);
					myForm.setTastoFormati(false);
					return mapping.getInputForward();
				}
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException de) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ de.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	  * SezioniCollocazioniGestioneAction.java
	  * chiudi
	  * ActionForward
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  *
	  * scelta di abbandono della pagina di inserimento sezione di collocazione
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		try {
			if (myForm.getProv() != null && (myForm.getProv().equals("nuova") || myForm.getProv().equals("modifica"))) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaModifica"));

				this.saveToken(request);
				myForm.setConferma(true);
				return mapping.getInputForward();
			} else {
				return Navigation.getInstance(request).goBack(true);
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	  * SezioniCollocazioniGestioneAction.java
	  * formati
	  * ActionForward
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  *
	  * il metodo corrisponde al tasto 'formati' che viene abilitato a fronte
	  * di tipo collocazione 'a formato' o 'continuazione'
	 */
	public ActionForward formati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		try {
/*
			if (myForm.getRecSezione().getTipoColloc()!=DocumentoFisicoCostant.MAGAZZINO_A_FORMATO
					|| myForm.getRecSezione().getTipoColloc()!=DocumentoFisicoCostant.COD_CONTINUAZIONE){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezAggiorna"));

				return mapping.getInputForward();

			}

*/
				myForm.setHoPremutoFormati(true);
			if (myForm.getProv().equals("nuova") ) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaSalvaSezione"));

				this.saveToken(request);
				myForm.setConferma(true);
				myForm.setConferma(true);
				myForm.setDisableSez(true);
				myForm.setDisableInvColl(true);
				myForm.setDisableNote(true);
				myForm.setDisableDescr(true);
				myForm.setDisableInvPrev(true);
				myForm.setDisableSistClass(true);
				myForm.setDisableNote(true);
				myForm.setDisableDescr(true);
				myForm.setDisableUltPrg(true);
				myForm.setDisableTipoSez(true);
				myForm.setDisableTipoColl(true);
				myForm.setTastoAggiorna(false);
				myForm.setDisable(true);
				return mapping.getInputForward();
			}else if (myForm.getProv().equals("esamina")){
				request.setAttribute("codPolo",myForm.getRecSezione().getCodPolo());
				request.setAttribute("codBib",myForm.getRecSezione().getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("codSez",myForm.getRecSezione().getCodSezione());
				request.setAttribute("esamina", myForm.getProv());
				request.setAttribute("prov", "sezGest");

//
//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.evolutivaFormatiSezioniInCorso"));
//
//				return mapping.getInputForward();
				return mapping.findForward("formati");
			}else {
				if ((myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE))
						&& !myForm.getRecSezione().getDataIns().equals("")){
					request.setAttribute("codPolo",myForm.getRecSezione().getCodPolo());
					request.setAttribute("codBib",myForm.getRecSezione().getCodBib());
					request.setAttribute("codSez",myForm.getRecSezione().getCodSezione());
					request.setAttribute("descrBib",myForm.getDescrBib());
//
//
//					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.evolutivaFormatiSezioniInCorso"));
//
//					return mapping.getInputForward();
					request.setAttribute("prov", "sezGest");
					return mapping.findForward("formati");
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaSalva"));

					this.saveToken(request);
					myForm.setConferma(true);
					return mapping.getInputForward();
				}
			}
//		} catch (ValidationException e) {
//
//			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
//
//			return Navigation.getInstance(request).goBack(true);
//		} catch (DataException e) {
//
//			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
//
//			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	  * SezioniCollocazioniGestioneAction.java
	  * aggiorna
	  * ActionForward
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  *
	  * il metodo corrisponde al tasto 'aggiorna' che viene abilitato a fronte
	  * di scelta di tipo collocazione impostato a
	  * "magazzino non a formato", per l'abilitazione del campo ultPrgAss, o
	  * "sistema di classificazione", per l'abilitazione del campo sistClass o
	  * "magazzino a formato" o "continuazion" per l'abilitazione del tasto "formati"
	 */
	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		try {
			myForm.setTastoFormati(false);
			if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)||
					myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)) {
				myForm.setTastoFormati(true);
			}

			myForm.setUltPrgAss(false);
			if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)) {
				myForm.setUltPrgAss(true);
			}
			myForm.setSistCla(false);
			if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
				myForm.setSistCla(true);
			}
//			myForm.setTastoAggiorna(false);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	private void loadPaginaVuota(HttpServletRequest request, ActionForm form) throws Exception {

		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		SezioneCollocazioneVO rec = new SezioneCollocazioneVO();
		rec.setCodPolo(myForm.getCodPolo());
		rec.setCodBib(myForm.getCodBib());
		//carico le combo
		rec.setTipoSezione(((CodiceVO) myForm.getListaTipoSezione().get(2)).getCodice());
		rec.setTipoColloc(((CodiceVO) myForm.getListaTipoCollocazione().get(3)).getCodice());
		rec.setCodSezione(null);
		rec.setInventariPrevisti(999999999);
		rec.setInventariCollocati(0);
		rec.setDescrSezione("");
		rec.setClassific("");
		rec.setProgNum(0);
		rec.setNoteSezione("");
		rec.setUteIns(Navigation.getInstance(request).getUtente().getFirmaUtente());
		rec.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());

		myForm.setDisableInvColl(true);
		myForm.setRecSezione(rec);
	}

	private void loadRigaSel(ActionForm form) throws Exception {

		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
				|| myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)) {
			myForm.setTastoAggiorna(true);
			myForm.setDisableSez(true);
			myForm.setDisableInvColl(true);
			if (myForm.getRecSezione().getInventariCollocati()==0){
				myForm.setTastoAggiorna(true);
				myForm.setDisableTipoSez(false);
				myForm.setDisableTipoColl(false);
				myForm.setTastoFormati(false);
			}else{
				myForm.setTastoAggiorna(false);
				myForm.setDisableTipoSez(true);
				myForm.setDisableTipoColl(true);
				myForm.setTastoFormati(true);
			}
		} else if ((myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_NON_STRUTTURATA))
				|| (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_STRUTTURATA))
				|| (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE))) {
			myForm.setDisableTipoSez(true);
			myForm.setDisableTipoColl(true);
			myForm.setDisableSez(true);
			myForm.setDisableInvColl(true);
			if (myForm.getRecSezione().getClassific() != null && myForm.getRecSezione().getInventariCollocati() > 0){
				myForm.setDisableSistClass(true);
			}
		}
		if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)) {
			if (myForm.getRecSezione().getInventariCollocati()==0){
				myForm.setTastoAggiorna(true);
				myForm.setDisableTipoSez(false);
				myForm.setDisableTipoColl(false);
				myForm.setTastoFormati(false);
			}else{
				myForm.setTastoAggiorna(false);
				myForm.setDisableTipoSez(true);
				myForm.setDisableTipoColl(true);
				myForm.setTastoFormati(true);
			}
			myForm.setDisableSez(true);
			myForm.setDisableInvColl(true);
			myForm.setUltPrgAss(true);
			myForm.getRecSezione().setProgNum(myForm.getRecSezione().getProgNum());
			myForm.setDisableUltPrg(true);
		}
		myForm.setSistCla(false);
		if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
			myForm.setSistCla(true);
		}
		myForm.setDate(true);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (myForm.getProv().equals("modifica")) {
				SezioneCollocazioneVO sezioni = myForm.getRecSezione();
				if (!this.updateSezione(sezioni, myForm.getTicket())) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.errataModifica"));

					this.saveToken(request);
				} else {
					if (myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| myForm.getRecSezione().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
						request.setAttribute("codSez", myForm.getRecSezione().getCodSezione().toUpperCase());
						request.setAttribute("descrSez",myForm.getRecSezione().getDescrSezione());
						request.setAttribute("codBib", myForm.getRecSezione().getCodBib());
						request.setAttribute("descrBib",myForm.getDescrBib());
						myForm.setConferma(false);
						if (myForm.isHoPremutoFormati()){
							request.setAttribute("prov", "sezGest");
							return mapping.findForward("formati");
						}
					}

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.updateOk"));

					this.saveToken(request);
				}
				return mapping.findForward("ok");
			} else if (myForm.isConferma()) {
				// insert sezione
				if (myForm.getRecSezione().getFlRecSez().equals(SezioneCollocazioneVO.SEZRECUP)){
//					myForm.getRecSezione().setFlRecSez("");
				}
				if (!this.insertSezione(myForm.getRecSezione(), myForm.getTicket())) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erratoInserimento"));

					this.saveToken(request);
					return Navigation.getInstance(request).goBack();
				} else {
					if (myForm.isTastoFormati()){
						request.setAttribute("codPolo", myForm.getRecSezione().getCodPolo());
						request.setAttribute("codBib", myForm.getRecSezione().getCodBib());
					}else{
						request.setAttribute("codBib", myForm.getRecSezione().getCodBib());
						request.setAttribute("descrBib", myForm.getDescrBib());

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));

						this.saveToken(request);
						return Navigation.getInstance(request).goBack();
					}
				}
				if (myForm.getRecSezione().getCodSezione() != null && !myForm.isTastoFormati()) {
					return mapping.findForward("ok");
				} else {
					request.setAttribute("codSez", myForm.getRecSezione().getCodSezione().toUpperCase());
					request.setAttribute("descrSez", myForm.getRecSezione().getDescrSezione());
					request.setAttribute("codBib", myForm.getRecSezione().getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("prov", "sezGest");
					myForm.setConferma(false);
					return mapping.findForward("formati");
				}
			} else {
				return mapping.findForward("ok");
			}
		} catch (DataException e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			myForm.getRecSezione().setFlRecSez("");
			return mapping.getInputForward();
		} catch (ValidationException e) {
			if (myForm.isTastoFormati()){
				myForm.setConferma(true);
			}else{
				myForm.getRecSezione().setFlRecSez("");
				myForm.setConferma(false);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			myForm.getRecSezione().setFlRecSez("");
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SezioniCollocazioniGestioneForm myForm = (SezioniCollocazioniGestioneForm) form;
		// Viene settato il token per le transazioni successive
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private boolean insertSezione(SezioneCollocazioneVO sezione, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertSezione(sezione, ticket);
		return ret;
	}

	private boolean updateSezione(SezioneCollocazioneVO sezione, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//almaviva5_20140331
		sezione.setUteAgg(DaoManager.getFirmaUtente(ticket));
		ret = factory.getGestioneDocumentoFisico().updateSezione(sezione,  ticket);
		return ret;
	}

	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
}
