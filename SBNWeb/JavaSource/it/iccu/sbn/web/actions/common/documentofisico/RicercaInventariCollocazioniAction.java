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
package it.iccu.sbn.web.actions.common.documentofisico;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioniUltCollSpecVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.ScaricoInventarialeForm;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.SpostamentoCollocazioniForm;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocRicercaForm;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EsportaForm;
import it.iccu.sbn.web.actionforms.gestionestampe.buoni.StampaBuoniCaricoForm;
import it.iccu.sbn.web.actionforms.gestionestampe.cataloghi.StampaCataloghiForm;
import it.iccu.sbn.web.actionforms.gestionestampe.etichette.StampaEtichetteForm;
import it.iccu.sbn.web.actionforms.gestionestampe.ingresso.StampaRegistroIngressoForm;
import it.iccu.sbn.web.actionforms.gestionestampe.periodici.StampaListaFascicoliForm;
import it.iccu.sbn.web.actionforms.gestionestampe.schede.StampaSchedeForm;
import it.iccu.sbn.web.actionforms.gestionestampe.strumentiPatrimonio.StampaStrumentiPatrimonioForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.constant.NavigazioneDocFisico;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public class RicercaInventariCollocazioniAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(RicercaInventariCollocazioniAction.class);
	private CaricamentoCombo caricaCombo = new CaricamentoCombo();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.lsSez", "listaSupportoSez");
		map.put("documentofisico.lsColl", "listaSupportoColl");
		map.put("documentofisico.lsSpec", "listaSupportoSpec");
		map.put("documentofisico.lsCollA", "listaSupportoCollAlla");
		map.put("documentofisico.lsSpecA", "listaSupportoSpecAlla");
		map.put("documentofisico.caricaFile", "caricaFile");
		map.put("ricerca.button.ordine", "ordiniCerca");
		map.put("ricerca.btn.fornitore.sif", "fornitoreCerca");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		RicercaInventariCollocazioniForm currentForm = (RicercaInventariCollocazioniForm) form;
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {

				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					if (form instanceof SpostamentoCollocazioniForm) {
						SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
						int numCopie = Integer.valueOf(utenteEjb.getDefault(ConstantDefault.GDF_NRO_COPIE_ETICH).toString());
						if (!ValidazioneDati.strIsEmpty(String.valueOf(numCopie))){
							myForm1.setNumCopie(numCopie);
						}else{
							myForm1.setNumCopie(2);
						}
					}

				} catch (Exception e) {
					LinkableTagUtils.resetErrors(request);
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

					return mapping.getInputForward();
				}
			}
		}
		return mapping.getInputForward();
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		// setto il token per le transazioni successive
		this.saveToken(request);

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		request.setAttribute("currentForm", form);

		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		if (request.getAttribute("indietro") != null && request.getAttribute("indietro").equals("indietro")){
			inizializzaFolderCollocazioni(request, myForm);
			return mapping.getInputForward();
		}
		// controllo se ho già i dati in sessione;
		try{
			if (!myForm.isSessione()) {
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				log.info("RicercaInventariCollocazioniAction::unspecified");
				loadDefault(request, mapping, form);

				if (form instanceof EsameCollocRicercaForm) {
					inizializzaFolderCollocazioni(request, myForm);
				}else if (form instanceof StampaSchedeForm) {
				}else if (form instanceof StampaCataloghiForm) {
				}else{
					if (form instanceof StampaCataloghiForm || form instanceof EsportaForm) {
						myForm.setDisableSez(true);
					}
					myForm.setNomeFileAppoggioInv(null);
					myForm.setNomeFileAppoggioBid(null);
					myForm.setSerie("");
					myForm.setStartInventario("0");
					myForm.setEndInventario("0");

					myForm.setFolder("RangeInv");
					myForm.setDisableDallaColl(true);
					myForm.setDisableDallaSpec(true);
					myForm.setDisableAllaColl(true);
					myForm.setDisableAllaSpec(true);
					myForm.setDisableTastoDallaColl(true);
					myForm.setDisableTastoAllaColl(true);
					myForm.setDisableTastoDallaSpecif(true);
					myForm.setDisableTastoAllaSpecif(true);
					if (form instanceof ScaricoInventarialeForm) {
						myForm.setFolder("Inventari");
						myForm.setTipoOperazione("N");
						myForm.setSelezione("N");
					}
					if (form instanceof StampaEtichetteForm) {
						myForm.setFolder("Inventari");
						myForm.setTipoOperazione("A");
						myForm.setSelezione("A");
					}
				}
				myForm.setSessione(true);
			}


			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				//
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}

			if (myForm.getListaSerie() == null || myForm.getListaSerie().size() == 0 || myForm.getListaSerie().get(0)==null
					|| !Navigation.getInstance(request).getUtente().getCodBib().equals(myForm.getCodBib())){
				List listaSerie = this.getListaSerie(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket(), form);
				if (listaSerie == null || listaSerie.size() < 1){
					//				if (ricEtichette.getListaSerie() == null || ricEtichette.getListaSerie().size() <= 0) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuotaSerie"));

					request.setAttribute("noListaSerie","noListaSerie");
					myForm.setNoSerie(true);
					myForm.setSerie("");
					return mapping.getInputForward();
				} else {
					myForm.setListaSerie(listaSerie);
					myForm.setListaComboSerie(this.caricaCombo.loadCodice(myForm.getListaSerie()));
				}
			}
			if (request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA)!= null){
				SezioneCollocazioneVO sezione = (SezioneCollocazioneVO)request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA);
				myForm.setTipoColloc(sezione.getTipoColloc());
				if (form instanceof SpostamentoCollocazioniForm) {
					SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
					if (!myForm1.isAllaSez()){
						myForm.setCodPoloSez(sezione.getCodPolo());
						myForm.setCodBibSez(sezione.getCodBib());
						myForm.setSezione(sezione.getCodSezione().trim());
						myForm1.setRecSezioneP(sezione);
					}else{
						myForm1.setCodPoloSez(myForm1.getCodPolo());
						myForm1.setCodBibSez(myForm1.getCodBib());

						myForm1.setRecSezioneA(sezione);
						myForm1.setCodPoloSez(sezione.getCodPolo());
						myForm1.setCodBibSezArrivo(sezione.getCodBib());
						myForm1.setCodSezArrivo(sezione.getCodSezione());
					}
				} else if (form instanceof EsportaForm) {
					EsportaForm myForm1 = (EsportaForm)form;
					myForm.setCodPoloSez(sezione.getCodPolo());
					myForm.setCodBibSez(sezione.getCodBib());
					myForm.setSezione(sezione.getCodSezione().trim());
					myForm1.setTipoColloc(sezione.getTipoColloc());
				} else{
					myForm.setCodPoloSez(sezione.getCodPolo());
					myForm.setCodBibSez(sezione.getCodBib());
					myForm.setSezione(sezione.getCodSezione().trim());
				}
				if (form instanceof EsameCollocRicercaForm) {
					myForm.setDallaCollocazione("");
					myForm.setDallaSpecificazione("");
					myForm.setDisableDallaColl(false);
					myForm.setDisableDallaSpec(false);
//					myForm.setEsattoColl(true);
					myForm.setEsattoColl(false);
					myForm.setDisableEsattoColl(false);
					myForm.setEsattoSpec(false);
					myForm.setDisableEsattoSpec(false);
					myForm.setDisableSez(false);
				}else{
					myForm.setDisableTastoDallaColl(false);
					myForm.setDisableTastoAllaColl(false);
					myForm.setDisableTastoDallaSpecif(false);
					myForm.setDisableTastoAllaSpecif(false);
					//
					myForm.setDisableDallaColl(false);
					myForm.setDisableDallaSpec(false);
					myForm.setDisableAllaColl(false);
					myForm.setDisableAllaSpec(false);

					if (form instanceof SpostamentoCollocazioniForm) {
						SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
						if (!myForm1.isAllaSez()){
							myForm.setDallaCollocazione("");
							myForm.setDallaSpecificazione("");
							myForm.setAllaCollocazione("");
							myForm.setAllaSpecificazione("");
						}
					}
				}
				return mapping.getInputForward();
			}

			//		ritorno da lente dallaCollocazione
			if (request.getAttribute("scelColl")!= null && request.getAttribute("listaSuppColl") != null){
				CollocazioniUltCollSpecVO coll = (CollocazioniUltCollSpecVO)request.getAttribute("scelColl");
				if (myForm.getQualeColl() != null){
					myForm.setAllaCollocazione(coll.getCodColloc().trim());
					//					myForm.setDallaSpecificazione("");
					//					myForm.setAllaCollocazione("");
					myForm.setAllaSpecificazione("");
					myForm.setQualeColl(null);
				}else{
					if (form instanceof EsameCollocRicercaForm) {
						myForm.setDallaCollocazione(coll.getCodColloc().trim());
						myForm.setDallaSpecificazione("");
						myForm.setEsattoColl(true);
						myForm.setDisableDallaSpec(false);
					}else{
						myForm.setDallaCollocazione(coll.getCodColloc().trim());
						myForm.setAllaCollocazione("");
						myForm.setDallaSpecificazione("");
						myForm.setAllaSpecificazione("");
					}
				}
				return mapping.getInputForward();
			}
			//		ritorno da lente Specificazione
			if (request.getAttribute("scelColl")!= null && request.getAttribute("listaSuppSpec") != null){
				CollocazioniUltCollSpecVO spec = (CollocazioniUltCollSpecVO)request.getAttribute("scelColl");
				if (form instanceof EsameCollocRicercaForm) {
					EsameCollocRicercaForm myForm1 = (EsameCollocRicercaForm)form;
					myForm1.setDallaSpecificazione(spec.getSpecColloc().trim());
					myForm1.setEsattoColl(true);
					myForm1.setEsattoSpec(true);
				}else{
					if (myForm.getQualeSpec() != null){
						myForm.setAllaSpecificazione(spec.getSpecColloc().trim());
						myForm.setQualeSpec(null);
					}else{
						myForm.setDallaSpecificazione(spec.getSpecColloc().trim());
					}
				}
				return mapping.getInputForward();
			}
			if (myForm.getFolder() != null && myForm.getFolder().equals("Inventari")){
				if (myForm.getSelezione() != null && (myForm.getSelezione().equals("N")
						|| (myForm.getSelezione().equals("A")))){
					myForm.setNomeFileAppoggioInv(null);
				}
			}
			if (myForm.getFolder() != null && myForm.getFolder().equals("IdentificativiTitoli")){
				if (myForm.getSelezione() != null && (myForm.getSelezione().equals("N"))){
					myForm.setNomeFileAppoggioBid(null);
				}
			}


		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage()));
			if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage()));
			if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.common.documentofisico
	  * RicercaInventariCollocazioniAction.java
	  * inizializzaFolderCollocazioni
	  * void
	  * @param myForm
	  *
	  *
	 */
	private void inizializzaFolderCollocazioni(HttpServletRequest request,
			RicercaInventariCollocazioniForm form) {
		RicercaInventariCollocazioniForm myForm = form;
		request.setAttribute("currentForm", form);
		if (form instanceof EsameCollocRicercaForm) {
			myForm.setSezione("");
			myForm.setDallaCollocazione("");
			myForm.setDallaSpecificazione("");
			myForm.setEsattoColl(false);
			myForm.setEsattoSpec(false);
			myForm.setDisableDallaColl(true);
			myForm.setDisableDallaSpec(true);
			myForm.setDisableTastoDallaColl(true);
			myForm.setDisableTastoDallaSpecif(true);
			myForm.setDisableEsattoColl(true);
			myForm.setDisableEsattoSpec(true);
			myForm.setSezione("");
			myForm.setDallaCollocazione("");
			myForm.setDallaSpecificazione("");
		}else if (form instanceof SpostamentoCollocazioniForm) {
			SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
			myForm1.setAllaSez(false);
			myForm1.setCodBibSezArrivo(null);
			myForm1.setCodPoloSezArrivo(null);
			myForm1.setCodSezArrivo(null);
		}else{
			myForm.setSezione("");
			myForm.setDallaCollocazione("");
			myForm.setDallaSpecificazione("");
			myForm.setEsattoColl(false);
			myForm.setEsattoSpec(false);
			myForm.setDisableDallaColl(true);
			myForm.setDisableDallaSpec(true);
			myForm.setDisableAllaColl(true);
			myForm.setDisableAllaSpec(true);
			myForm.setDisableTastoDallaColl(true);
			myForm.setDisableTastoDallaSpecif(true);
			myForm.setDisableTastoAllaColl(true);
			myForm.setDisableTastoAllaSpecif(true);
//			myForm.setDisableEsattoColl(true);
//			myForm.setDisableEsattoSpec(true);
			myForm.setSezione("");
			myForm.setDallaCollocazione("");
			myForm.setDallaSpecificazione("");
		}
		if (form instanceof SpostamentoCollocazioniForm) {
			SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
			myForm1.setAllaSez(false);
			myForm1.setCodBibSezArrivo(null);
			myForm1.setCodPoloSezArrivo(null);
			myForm1.setCodSezArrivo(null);
			myForm1.setCollocazioneProvvisoria(null);
			myForm1.setSpecificazioneProvvisoria(null);
		}
	}

	protected ActionForward validaInputCollocazioni(ActionMapping mapping, HttpServletRequest request, ActionForm form)  throws Exception {
		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		CodiciNormalizzatiVO collSpec = null;
//		try{
			// S ... selezione intervallo di collocazione
			if (myForm.getCodBibSez() != null){
				if (form instanceof EsameCollocRicercaForm) {
					collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(myForm.getTipoColloc(), myForm.getDallaCollocazione(), "", false,
							myForm.getDallaSpecificazione(), "", false, "");
				}else{
//					collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(myForm.getTipoColloc(), myForm.getDallaCollocazione(), myForm.getAllaCollocazione(), false,
//							myForm.getDallaSpecificazione(), myForm.getAllaSpecificazione(), false, "");
					collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(myForm.getTipoColloc(), myForm.getDallaCollocazione(), myForm.getAllaCollocazione(),
							myForm.getDallaSpecificazione(), myForm.getAllaSpecificazione(), "");
				}
			}else{
				throw new ValidationException("premereTastoSezionePartenza");
			}
			if ( (collSpec.getDaColl() + collSpec.getDaSpec()).compareTo( (collSpec.getAColl() + collSpec.getASpec()) ) <= 0) {
				// la prima minore uguale della seconda
			} else {
				throw new ValidationException(
				"dallaCollocazioneDeveEssereMinoreOUgualeAllaCollocazione");
			}

		return mapping.getInputForward();
	}

	protected ActionForward validaInputInventari(ActionMapping mapping,
			HttpServletRequest request,	ActionForm form)  throws Exception {
		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		if (myForm.getSelezione() != null){
			if (myForm.getSelezione().equals("F")){
				myForm.setTipoOperazione("N");
			}
			if (myForm.getSelezione().equals("N")){
				myForm.setTipoOperazione("N");
			}
			if (myForm.getTipoOperazione()!= null && myForm.getTipoOperazione().equals("A")){
				if (form instanceof StampaEtichetteForm) {
					StampaEtichetteForm myForm1 = (StampaEtichetteForm)form;
					if (ValidazioneDati.strIsNull(myForm1.getCodBibliotec())){
						throw new ValidationException("codiceBibliotecarioObbligatorio");
					}
					if (ValidazioneDati.strIsNull(myForm1.getDataInizio())){
						throw new ValidationException("dataInizioObbligatoria");
					}
					if (ValidazioneDati.strIsNull(myForm1.getDataFine())){
						throw new ValidationException("dataFineObbligatoria");
					}
					if (ValidazioneDati.isFilled(myForm1.getDataInizio().trim()) && ValidazioneDati.validaData(myForm1.getDataInizio().trim()) != ValidazioneDati.DATA_OK){
						throw new ValidationException("dataInizioErrata");
					}
					if (ValidazioneDati.isFilled(myForm1.getDataFine().trim()) && ValidazioneDati.validaData(myForm1.getDataFine().trim()) != ValidazioneDati.DATA_OK){
						throw new ValidationException("datafineErrata");
					}
					if ((ValidazioneDati.strIsNull((String.valueOf(myForm1.getNumCopie()))))){
						throw new ValidationException("controllareNumeroCopie");
					}
					if (!(ValidazioneDati.strIsNumeric(String.valueOf(myForm1.getNumCopie()))) ||
							!(ValidazioneDati.isFilled(String.valueOf(myForm1.getNumCopie())))){
						throw new ValidationException("controllareNumeroCopie");
					}
				}
			}

			controlloSelezione(myForm, form);
		}
		return mapping.getInputForward();
	}

	protected ActionForward validaInputIdentificativiTitoli(ActionMapping mapping,
			HttpServletRequest request,	ActionForm form)  throws Exception {
		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		if (myForm.getSelezione() != null){
			if (myForm.getSelezione().equals("F")){
				myForm.setTipoOperazione("N");
			}
			if (myForm.getSelezione().equals("N")){
				myForm.setTipoOperazione("N");
			}
			controlloSelezione(myForm, form);
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.common.documentofisico
	  * RicercaInventariCollocazioniAction.java
	  * controlloSelezione
	  * void
	  * @param myForm
	  * @throws Exception
	  * @throws ValidationException
	  *
	  *
	 */
	private void controlloSelezione(RicercaInventariCollocazioniForm myForm, ActionForm form)
	throws Exception, ValidationException {
		//
		if (myForm instanceof StampaSchedeForm) {
			StampaSchedeForm myForm1 = (StampaSchedeForm)form;
			if (myForm.getFolder() != null && myForm.getFolder().equals("Inventari")){
				trattamentoFolderInventariBid(myForm);
			}else if (myForm.getFolder() != null && myForm.getFolder().equals("IdentificativiTitoli")){

				if (myForm.getSelezione().equals("N")){
					if (myForm1.getListaIdentificativi() != null && myForm1.getListaIdentificativi().size() > 0){
						myForm1.setListaBid(myForm1.getListaIdentificativi());
					}else{
						throw new ValidationException("listaBidVuota");
					}
				}else if (myForm.getSelezione().equals("F")){
					if (myForm.getNomeFileAppoggioBid() != null){
						if (myForm1.getListaBid() == null){
							throw new ValidationException("caricareIlFile");
						}else{
							if (myForm1.getListaBid().size() < 1){
								throw new ValidationException("listaBidVuota");
							}
						}
					}else{
						throw new ValidationException("caricareIlFile");
					}
				}
			}
		} else if (myForm instanceof StampaCataloghiForm) {
			StampaCataloghiForm myForm1 = (StampaCataloghiForm)form;
			if (myForm.getFolder() != null && myForm.getFolder().equals("Inventari")){
				trattamentoFolderInventariBid(myForm);
			}else if (myForm.getFolder() != null && myForm.getFolder().equals("IdentificativiTitoli")){

				if (myForm.getSelezione().equals("N")){
					if (myForm1.getListaIdentificativi() != null && myForm1.getListaIdentificativi().size() > 0){
						myForm1.setListaBid(myForm1.getListaIdentificativi());
					}else{
						throw new ValidationException("listaBidVuota");
					}
				}else if (myForm.getSelezione().equals("F")){
					if (myForm.getNomeFileAppoggioBid() != null){
						if (myForm1.getListaBid() == null){
							throw new ValidationException("caricareIlFile");
						}else{
							if (myForm1.getListaBid().size() < 1){
								throw new ValidationException("listaBidVuota");
							}
						}
					}else{
						throw new ValidationException("caricareIlFile");
					}
				}
			}
		} else if (myForm instanceof StampaStrumentiPatrimonioForm) {
			// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
			// da file o dalla valorizzazione dei campi bid sulla mappa
			//  trattamentoFolderInventariBid(myForm);
			StampaStrumentiPatrimonioForm myForm1 = (StampaStrumentiPatrimonioForm)form;
			if (myForm.getFolder() != null && myForm.getFolder().equals("IdentificativiTitoli")){

				if (myForm.getSelezione().equals("N")){
					if (myForm1.getListaIdentificativi() != null && myForm1.getListaIdentificativi().size() > 0){
						myForm1.setListaBid(myForm1.getListaIdentificativi());
					}else{
						throw new ValidationException("listaBidVuota");
					}
				}else if (myForm.getSelezione().equals("F")){
					if (myForm.getNomeFileAppoggioBid() != null){
						if (myForm1.getListaBid() == null){
							throw new ValidationException("caricareIlFile");
						}else{
							if (myForm1.getListaBid().size() < 1){
								throw new ValidationException("listaBidVuota");
							}
						}
					}else{
						throw new ValidationException("caricareIlFile");
					}
				}
			}
			if (myForm.getFolder() != null && myForm.getFolder().equals("Inventari")) {
				trattamentoFolderInventariBid(myForm);
			}
		} else {
			trattamentoFolderInventariBid(myForm);
		}
	}
		//

	/**
	  * it.iccu.sbn.web.actions.common.documentofisico
	  * RicercaInventariCollocazioniAction.java
	  * trattamentoFolderInventariBid
	  * void
	  * @param myForm
	  * @throws Exception
	  * @throws ValidationException
	  *
	  *
	 */
	private void trattamentoFolderInventariBid(
			RicercaInventariCollocazioniForm myForm) throws Exception,
			ValidationException {
		if (myForm.getSelezione().equals("N")){
			if (ValidazioneDati.isFilled(myForm.getListaInventariInput()) ){
				myForm.setListaInventari(myForm.getListaInventariInput());
			}else{
				throw new ValidationException("listaInventariVuota");
			}
		}else if (myForm.getSelezione().equals("F")){
			if (myForm.getNomeFileAppoggioInv() != null){
				if (myForm.getListaInventari() == null){
					throw new ValidationException("caricareIlFile");
				}else{
					if (myForm.getListaInventari().size() < 1){
						throw new ValidationException("listaInventariVuota");
					}
				}
			}else{
				throw new ValidationException("caricareIlFile");
			}
		}
	}

	protected ActionForward validaInputRangeInventari(ActionMapping mapping,
			HttpServletRequest request, ActionForm form)  throws Exception {
		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		myForm.setTipoOperazione("R");
		//		try{
		//validazione startInv e endInv
		if (myForm.getSerie() !=null) {
			if (myForm.getSerie().length() > 3) {
				throw new ValidationException("codSerieEccedente");
			}
		}else{
			throw new ValidationException("ERRORE: codSerie = null");
		}
		if (!ValidazioneDati.strIsNull(myForm.getStartInventario())) {
			if (ValidazioneDati.strIsNumeric(myForm.getStartInventario())){
				if ((myForm.getStartInventario()).length()>9) {
					throw new ValidationException("codInventDaEccedente");
				}
			}else{
				throw new ValidationException("codInventDaDeveEssereNumerico");
			}
		}else{
			throw new ValidationException("errInvObbl");
		}
		if (!ValidazioneDati.strIsNull(myForm.getEndInventario())) {
			if (ValidazioneDati.strIsNumeric(myForm.getEndInventario())){
				if ((myForm.getEndInventario()).length()>9) {
					throw new ValidationException("codInventAEccedente");
				}
			}else{
				throw new ValidationException("codInventADeveEssereNumerico");
			}
		}else{
			throw new ValidationException("errInvObbl");
		}
		if (form instanceof StampaRegistroIngressoForm) {
		}else{
			if (myForm.getEndInventario() != null && myForm.getStartInventario() != null){
				if (Integer.valueOf(myForm.getEndInventario()) < Integer.valueOf(myForm.getStartInventario()) ) {
					throw new ValidationException("codInventDaDeveEssereMinoreDiCodInventA");
				}
			}else{
				throw new ValidationException("ERRORE: codInvent = null");
			}
		}
		if (!ValidazioneDati.strIsNull(myForm.getStartInventario()) && !myForm.getStartInventario().trim().equals("0")
				&& ValidazioneDati.strIsNumeric(myForm.getStartInventario())
				&& Integer.valueOf(myForm.getStartInventario()) >= 0){
		}else{
			if (form instanceof StampaRegistroIngressoForm) {
				StampaRegistroIngressoForm myForm1 = (StampaRegistroIngressoForm)form;
				if (myForm1.getTipoDiStampa() != null && myForm1.getTipoDiStampa().equals("StatisticheRegistro")){

				}else{
					//errore end
					throw new ValidationException("indicareIDueEstremiDellIntervallo");
				}
			}else{
				throw new ValidationException("indicareIDueEstremiDellIntervallo");
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward validaInputDataDaA(ActionMapping mapping,
			HttpServletRequest request, ActionForm form)  throws Exception {
		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		myForm.setTipoOperazione("D");

		int codRitorno = -1;
		if (myForm.getDataDa().trim().equals("")){
			if (myForm.getDataA().trim().equals("")
					|| myForm.getDataA().equals("00/00/0000")
					|| !myForm.getDataA().equals("00/00/0000")){
				throw new ValidationException("dataDaObbligatoria");
			}
		}else{
			if (!myForm.getDataDa().trim().equals("") && myForm.getDataDa().equals("00/00/0000")){
				throw new ValidationException("formatoDataDaNonValido");
			}else{
				codRitorno = ValidazioneDati.validaDataPassata(myForm.getDataDa());
				if (codRitorno != ValidazioneDati.DATA_OK){
					throw new ValidationException("dataDaErrata");
				}else{
					if (myForm.getDataA() != null && !myForm.getDataA().trim().equals("")) {
						if (myForm.getDataA().equals("00/00/0000")){
							throw new ValidationException("formatoDataANonValido");
						}else{
							codRitorno = ValidazioneDati.validaDataPassata(myForm.getDataA());
							if (codRitorno != ValidazioneDati.DATA_OK){
								throw new ValidationException("dataAErrata");
							}else{
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
								  long longData1 = 0;
								  long longData2 = 0;

								try {
									  Date data1 = simpleDateFormat.parse(myForm.getDataDa().trim());
									  Date data2 = simpleDateFormat.parse(myForm.getDataA().trim());
									  longData1 = data1.getTime();
									  longData2 = data2.getTime();
								  } catch (ParseException e) {
									  throw new ValidationException("erroreParse");
								  }

								  if ((longData2 - longData1) < 0) {
									  throw new ValidationException("dataDaAErrata");
								  }
							}
						}
					}
				}
			}
		}
		return mapping.getInputForward();
	}
	public ActionForward selRangeInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("RangeInv");

		myForm.setSerie("");
		myForm.setStartInventario("0");
		myForm.setEndInventario("0");
		if (form instanceof EsameCollocRicercaForm) {
			EsameCollocRicercaForm myForm1 = (EsameCollocRicercaForm)form;
			myForm.setSerie("");
			myForm1.setCodInvent(0);
			myForm1.setCodRfid("");
		}


		if (form instanceof SpostamentoCollocazioniForm) {
			SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
			myForm1.setAllaSez(false);
			myForm1.setCodBibSezArrivo(null);
			myForm1.setCodPoloSezArrivo(null);
			myForm1.setCodSezArrivo(null);
		}
		return mapping.getInputForward();
	}

	public ActionForward selColloc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("Collocazioni");
		myForm.setTipoOperazione("S");
		this.inizializzaFolderCollocazioni(request, myForm);
		return mapping.getInputForward();
	}

	public ActionForward selInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("Inventari");
		myForm.setTipoOperazione("N");
		myForm.setSelezione("N");
		return mapping.getInputForward();
	}

	public ActionForward selIdentificativiTitoli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("IdentificativiTitoli");
		myForm.setTipoOperazione("N");
		myForm.setSelezione("N");
		return mapping.getInputForward();
	}

	public ActionForward selFattura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("Fattura");
		myForm.setTipoOperazione("F");
		return mapping.getInputForward();
	}

	public ActionForward selFornitore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("Fornitore");
		myForm.setTipoOperazione("F");
		return mapping.getInputForward();
	}

	public ActionForward selOrdine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("Ordine");
		myForm.setTipoOperazione("O");
		return mapping.getInputForward();
	}

	public ActionForward selPerNumBuonoCarico(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (form instanceof StampaBuoniCaricoForm) {
			StampaBuoniCaricoForm myForm1 = (StampaBuoniCaricoForm)form;
			myForm1.setRistampaDaNumBuono(true);
			myForm1.setDisableRistampaNumBuono(true);
		}
		myForm.setFolder("NumeroBuono");
		myForm.setTipoOperazione("B");
		return mapping.getInputForward();
	}

	public ActionForward selData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		myForm.setFolder("Data");
		myForm.setTipoOperazione("D");
		myForm.setDataDa("");
		myForm.setDataA("");
		myForm.setTipoFormato(TipoStampa.XLS.name());
		myForm.setCheck("nuoviEsemplari");
		return mapping.getInputForward();
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (form instanceof StampaSchedeForm){
			if (form instanceof StampaSchedeForm) {
				StampaSchedeForm myForm1 = (StampaSchedeForm)form;
				if (myForm.getFolder() != null &&
						(myForm.getFolder().equals("Inventari") || myForm.getFolder().equals("IdentificativiTitoli"))){
					myForm.setFolder(myForm1.getFolder());
				}else{
					throw new ValidationException("erroreFolder");
				}
			}
		} else if (form instanceof StampaCataloghiForm){
			if (form instanceof StampaCataloghiForm) {
				StampaCataloghiForm myForm1 = (StampaCataloghiForm)form;
				if (myForm.getFolder() != null &&
						(myForm.getFolder().equals("Inventari") || myForm.getFolder().equals("IdentificativiTitoli"))){
					myForm.setFolder(myForm1.getFolder());
				}else{
					throw new ValidationException("erroreFolder");
				}
			}
		}

		myForm.setTipoOperazione("N");
		myForm.setSelezione("F");
		// gestione upload immagine marca
		FormFile fileEsterno = myForm.getFileEsterno();
		if (fileEsterno == null || fileEsterno.getFileSize() == 0) {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));
			return mapping.getInputForward();
		}

		byte[] invbuf;
		try {
			invbuf = fileEsterno.getFileData();

			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(invbuf)));
			if (myForm.getFolder() != null && myForm.getFolder().equals("Inventari")) {

				boolean eliminaDuplicati = true;
				if (myForm instanceof StampaEtichetteForm)
					eliminaDuplicati = false;

				List<CodiceVO> listaInventari = caricaInventariDaFile(invbuf, eliminaDuplicati);

				if (!ValidazioneDati.isFilled(listaInventari) ) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));

					return mapping.getInputForward();
				}

				myForm.setListaInventari(listaInventari);

			}if (myForm.getFolder() != null && myForm.getFolder().equals("IdentificativiTitoli")){
				if (form instanceof StampaSchedeForm) {
					StampaSchedeForm myForm1 = (StampaSchedeForm)form;
					List<CodiceVO> listaBid = new ArrayList<CodiceVO>();
					String bid = null;
					while ( (bid = reader.readLine() ) != null ) {
						if (!ValidazioneDati.leggiXID(bid)){
							continue;
						}else{
							listaBid.add(new CodiceVO(bid));
						}
					}
					if (!ValidazioneDati.isFilled(listaBid) ) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));

						return mapping.getInputForward();
					}else{
						myForm1.setListaBid(listaBid);
					}
				} else if (form instanceof StampaCataloghiForm) {
					StampaCataloghiForm myForm1 = (StampaCataloghiForm)form;
					List<CodiceVO> listaBid = new ArrayList<CodiceVO>();
					String bid = null;
					while ( (bid = reader.readLine() ) != null ) {
						if (!ValidazioneDati.leggiXID(bid)){
							continue;
						}else{
							listaBid.add(new CodiceVO(bid));
						}
					}
					if (!ValidazioneDati.isFilled(listaBid) ) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));

						return mapping.getInputForward();
					}else{
						myForm1.setListaBid(listaBid);
					}


				}  else if (form instanceof StampaStrumentiPatrimonioForm) {
					// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
					// da file o dalla valorizzazione dei campindi bid sulla mappa
					//Ricerca per Titoli(lista bid o bid in in griglia)
					StampaStrumentiPatrimonioForm myForm1 = (StampaStrumentiPatrimonioForm)form;
					List<CodiceVO> listaBid = new ArrayList<CodiceVO>();
					String bid = null;
					while ( (bid = reader.readLine() ) != null ) {
						if (!ValidazioneDati.leggiXID(bid)){
							continue;
						}else{
							listaBid.add(new CodiceVO(bid));
						}
					}
					if (!ValidazioneDati.isFilled(listaBid) ) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));

						return mapping.getInputForward();
					}else{
						myForm1.setListaBid(listaBid);
					}
				}
			}
			if (myForm.getFolder() != null && myForm.getFolder().equals("Inventari")){
				myForm.setNomeFileAppoggioInv(fileEsterno.toString());
			}else{
				myForm.setNomeFileAppoggioBid(fileEsterno.toString());
			}
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	private List<CodiceVO> caricaInventariDaFile(byte[] invbuf, boolean eliminaDuplicati) throws Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(invbuf)));

		String inv = null;
		List<CodiceVO> listaInventari = new ArrayList<CodiceVO>();
		HashSet<CodiceVO> hm = new HashSet<CodiceVO>();

		while ( (inv = reader.readLine() ) != null ) {
			CodiceVO serieInv = null;
			try {
				InventarioVO i = InventarioRFIDParser.parse(inv);
				serieInv = new CodiceVO(i.getCodSerie(), i.getCodInvent() + "");
				String codBib = i.getCodBib();
				if (ValidazioneDati.isFilled(codBib))
					serieInv.setTerzo(codBib);
				else
					serieInv.setTerzo("");

			} catch (ValidationException e) {
				serieInv = null;
			}

			//CodiceVO serieInv = (ValidazioneDati.leggiInventario(inv));
			if (serieInv != null) {
				if (eliminaDuplicati)
					//aggiungo inv. a un set che elimina i duplicati
					hm.add(serieInv);
				else
					listaInventari.add(serieInv);
			} else
				//annullo caricamento
				return null;
		}

		if (eliminaDuplicati)
			listaInventari.addAll(hm);

		return listaInventari;
	}

	public ActionForward listaSupportoSez(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = ((RicercaInventariCollocazioniForm) form);
		this.resetToken(request);
		request.setAttribute("currentForm", form);
//		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		SezioneCollocazioneVO sezione = null;
		try {
			if (form instanceof SpostamentoCollocazioniForm) {
				SpostamentoCollocazioniForm myForm1 = (SpostamentoCollocazioniForm)form;
				myForm1.setAllaSez(false);
				myForm1.setCodBibSezArrivo(null);
				myForm1.setCodPoloSezArrivo(null);
				myForm1.setCodSezArrivo(null);
			}
			if (myForm.isSezPNonEsiste()){
				myForm.setSezPNonEsiste(false);
				request.setAttribute("chiamante",mapping.getPath());
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				return mapping.findForward("lenteSez");
			}else{
				if (!myForm.isDisableSez()){
					if (myForm.getCodPoloSez() == null && myForm.getCodBibSez() == null && myForm.getSezione().trim().length()>0
							|| myForm.getCodPoloSez() != null && myForm.getCodBibSez() != null && myForm.getSezione().trim().length()>0){
						//ho digitato la sezione senza passare dalla lista
						//imposto i parametri e faccio get della sezione
						sezione = this.getSezioneDettaglio(myForm.getCodPolo(), myForm.getCodBib(), myForm.getSezione().trim(), myForm.getTicket());
						myForm.setSezPNonEsiste(true);
						//se la sezione esiste imposto NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA e faccio return "this.unspecified(mapping, form,	request, response)"
						if (sezione != null && myForm.isSezPNonEsiste()){
							request.setAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA, sezione);
							myForm.setSezPNonEsiste(false);
							return this.unspecified(mapping, form, request, response);
						}else{
							request.setAttribute("chiamante",mapping.getPath());
							request.setAttribute("codBib",myForm.getCodBib());
							request.setAttribute("descrBib",myForm.getDescrBib());
							return mapping.findForward("lenteSez");
						}

					}else{
						myForm.setSezPNonEsiste(false);
						request.setAttribute("chiamante",mapping.getPath());
						request.setAttribute("codBib",myForm.getCodBib());
						request.setAttribute("descrBib",myForm.getDescrBib());
						return mapping.findForward("lenteSez");
					}
				}else{
					//
					return mapping.getInputForward();
				}
			}

		} catch (DataException e) {
			if (sezione == null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezNonEsistente"));

				myForm.setSezPNonEsiste(true);
				return this.listaSupportoSez(mapping, form,	request, response);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoColl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (Navigation.getInstance(request).isFromBar()){
			return mapping.getInputForward();
		}
		try {
			EsameCollocRicercaVO ricerca =  new EsameCollocRicercaVO();
			ricerca.setCodPolo(myForm.getCodPolo());
			ricerca.setCodBib(myForm.getCodBib());
			ricerca.setCodPoloSez(myForm.getCodPolo());
			ricerca.setCodBibSez(myForm.getCodBib());
			if (!myForm.isDisableSez()){
				ricerca.setCodSez(myForm.getSezione());
				if (form instanceof EsameCollocRicercaForm) {
					EsameCollocRicercaForm myForm1 = (EsameCollocRicercaForm)form;
					if (myForm1.isEsattoColl()){
						ricerca.setEsattoColl(myForm1.isEsattoColl());
					}
					ricerca.setALoc(myForm1.getDallaCollocazione().trim());
				}else{
					if (myForm.getQualeColl() != null && myForm.getQualeColl().equals("alla")){
						ricerca.setALoc("");
					}else{
						ricerca.setALoc(myForm.getDallaCollocazione().trim());
					}
				}
				ricerca.setCodLoc(myForm.getDallaCollocazione().trim());
				ricerca.setCodSpec("");
				ricerca.setASpec("");
				request.setAttribute("chiamante", mapping.getPath());
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				ricerca.setOrdLst("CA");
				request.setAttribute("paramRicerca", ricerca);
				request.setAttribute("listaCollSupp","listaCollSupp");
				return mapping.findForward("lenteColloc");
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));

				request.setAttribute("currentForm", myForm);
				return mapping.getInputForward();
			}

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);
		}
	}
	public ActionForward listaSupportoCollAlla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (Navigation.getInstance(request).isFromBar()){
			return mapping.getInputForward();
		}
		try {
			if (!myForm.isDisableSez()){
				myForm.setQualeColl("alla");
				return this.listaSupportoColl(mapping, form, request, response);
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));

				request.setAttribute("currentForm", myForm);
				return mapping.getInputForward();
			}

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}
	private List getListaSerie(String codPolo, String codBib, String ticket, ActionForm form)
	throws Exception {
		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		if (serie == null ||  serie.size() <= 0)  {
			myForm.setNoSerie(true);
		}
		return serie;
	}
	public ActionForward listaSupportoSpec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try {
			if (!myForm.isDisableSez()){
				EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
				ricerca.setCodPolo(myForm.getCodPolo());
				ricerca.setCodBib(myForm.getCodBib());
				ricerca.setCodPoloSez(myForm.getCodPolo());
				ricerca.setCodBibSez(myForm.getCodBib());
				ricerca.setCodSez(myForm.getSezione());
				ricerca.setCodLoc(myForm.getDallaCollocazione().trim());
				ricerca.setCodSpec(myForm.getDallaSpecificazione().trim());
				if (form instanceof EsameCollocRicercaForm) {
					EsameCollocRicercaForm myForm1 = (EsameCollocRicercaForm)form;
					ricerca.setEsattoColl(true);
					ricerca.setEsattoSpec(myForm1.isEsattoSpec());
					ricerca.setASpec(myForm.getDallaSpecificazione().trim());
				}
				if (ValidazioneDati.equals(myForm.getQualeSpec(), "alla") ){
					ricerca.setCodLoc(myForm.getAllaCollocazione().trim());
					ricerca.setCodSpec(myForm.getAllaSpecificazione().trim());
				}
				ricerca.setCodPolo(myForm.getCodPolo());
				ricerca.setCodBib(myForm.getCodBib());
				request.setAttribute("chiamante",mapping.getPath());
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("listaSpecSupp","listaSpecSupp");
				ricerca.setOrdLst("CA");
				request.setAttribute("paramRicerca", ricerca);
				return mapping.findForward("lenteSpecif");
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));

				request.setAttribute("currentForm", myForm);
				return mapping.getInputForward();
			}

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}
	public ActionForward listaSupportoSpecAlla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RicercaInventariCollocazioniForm myForm = (RicercaInventariCollocazioniForm) form;
		request.setAttribute("currentForm", form);
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try {
			if (!myForm.isDisableSez()){
				myForm.setQualeSpec("alla");
				return this.listaSupportoSpec(mapping, form, request, response);
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));

				request.setAttribute("currentForm", myForm);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward ordiniCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			if (form instanceof StampaListaFascicoliForm) {
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				StampaListaFascicoliForm myForm1 = (StampaListaFascicoliForm)form;
				this.impostaOrdineCerca( myForm1,request,mapping);
			}
			return mapping.findForward("ordiniCerca");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaOrdineCerca( StampaListaFascicoliForm currentForm, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getCodBib();
		String codBAff = null;
		String codOrd=currentForm.getCodice();
		String annoOrd=currentForm.getAnno();
		String tipoOrd=currentForm.getTipo();
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo ("","");
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=null;
		String chiama=mapping.getPath();
		String[] statoOrdArr=new String[0];
		Boolean stamp=false;
		Boolean rinn=false;

		eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
		String ticket=Navigation.getInstance(request).getUserTicket();
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);

//		request.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, (ListaSuppOrdiniVO) eleRicerca);
//		ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);

	}catch (Exception e) {	}
	}



	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			ServletRequest request, ServletResponse response) throws Exception {
		((HttpServletRequest)request).getSession().setAttribute("currentForm", form);
		return super.execute(mapping, form, request, response);
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RicercaInventariCollocazioniForm currentForm = (RicercaInventariCollocazioniForm) form;
		try {
			/*
			ActionForward forward = fornitoreCercaVeloce(mapping, request, currentForm);
			if (forward != null) {
				return forward;
			}
			*/
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

			this.impostaFornitoreCerca( currentForm, request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	private void impostaFornitoreCerca( RicercaInventariCollocazioniForm currentForm, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getCodBib();
		String codForn=currentForm.getCodFornitore();
		String nomeForn=currentForm.getFornitore();
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0"; // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}
}
