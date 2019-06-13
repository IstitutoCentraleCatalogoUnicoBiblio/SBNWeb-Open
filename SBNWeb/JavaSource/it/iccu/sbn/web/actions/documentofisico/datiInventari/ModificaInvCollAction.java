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
package it.iccu.sbn.web.actions.documentofisico.datiInventari;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceNotaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.NotaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.documentofisico.DocumentoFisicoFormTypes;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.ListeInventariForm;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.ModificaInvCollForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class ModificaInvCollAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(ModificaInvCollAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.modInvCollTab1", "tab1");
		map.put("documentofisico.modInvCollTab11", "tab11");
		map.put("documentofisico.modInvCollTab2", "tab2");
		map.put("documentofisico.modInvCollTab3", "tab3");
		map.put("documentofisico.bottone.cancInv", "cancInv");
		map.put("documentofisico.bottone.esemplare", "esEsempl");
		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.lsProven", "listaSupportoProven");
		map.put("documentofisico.bottone.SIFbibl", "SIFbibl");
		map.put("documentofisico.bottone.cancNota", "notaCanc");
		map.put("documentofisico.bottone.insNota", "notaIns");
		map.put("documentofisico.bottone.indice", "indice");
		return map;
	}

	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String parameter)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(ModificaInvCollForm.SUBMIT_CANCELLA_NOTA) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				currentForm.setSelezRadioNota(tokens[2]);
				return "notaCanc";
			}

		}
		return super.getMethodName(mapping, form, request, response, parameter);

	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {


		log.debug("loadDefault()");
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Navigation navi = Navigation.getInstance(request);
		if (navi.getUtente().getCodBib().equals(currentForm.getCodBib())){
			try {
				String catFruiDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CAT_FRUIZIONE);
				if (!ValidazioneDati.strIsEmpty(catFruiDefault)) {
					currentForm.setCatFruiDefault(catFruiDefault);
				}
				String tipoAcqDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_TP_ACQUISIZIONE);
				if (!ValidazioneDati.strIsEmpty(tipoAcqDefault)) {
					currentForm.setTipoAcqDefault(tipoAcqDefault);
				}
				String codNoDispDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_NO_DISPONIBILITA);
				if (!ValidazioneDati.strIsEmpty(codNoDispDefault)) {
					currentForm.setCodNoDispDefault(codNoDispDefault);
				}
				String codStatoConsDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_STATO_CONSERVAZIONE);
				if (!ValidazioneDati.strIsEmpty(codStatoConsDefault)) {
					currentForm.setCodStatoConsDefault(codStatoConsDefault);
				}
				String codProvDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_PROVENIENZA);
				if (!ValidazioneDati.strIsEmpty(codProvDefault)) {
					ProvenienzaInventarioVO provenienza = this.getProvenienza(currentForm.getCodPolo(), currentForm.getCodBib(),
							codProvDefault,	currentForm.getTicket());

					if (provenienza == null) {
						if (!(request.getAttribute("prov") != null && (request.getAttribute("prov").equals("ordine") || request
								.getAttribute("prov").equals("ordineIns")))) {
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.provenienzaDefaultNonEsiste"));

						}

					}else{
						currentForm.setCodPoloProvDefault(provenienza.getCodPolo());
						currentForm.setCodBibProvDefault(provenienza.getCodBib());
						currentForm.setCodProvDefault(provenienza.getCodProvenienza());
						currentForm.setDescrProvDefault(provenienza.getDescrProvenienza());
					}
				} else {
					// sezione di default non impostata
					//				errors.clear();
					//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codProvDefaultNonImpostata"));
					//
					//				return null;
				}
			} catch (ValidationException e) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				this.saveToken(request);
				return mapping.getInputForward();
			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				this.saveToken(request);
				return navi.goBack(true);
			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

				return navi.goBack(true);
			}
			return null;
		}else{
			//il diagnostico non è necessario
			return null;
		}
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		ModificaInvCollForm currentForm = (ModificaInvCollForm)form;
		currentForm = (ModificaInvCollForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			String chiamante = navi.getActionCaller();
			if (chiamante.toUpperCase().indexOf("ORDINE") > -1) {
				//linea ordini, stampa on line inventario da ordine, Indietro con ordine completato (si)
				if (request.getAttribute("findForward") != null && request.getAttribute("findForward").equals("back")){
					if (request.getAttribute("ordineCompletato") != null){
						request.setAttribute("ordineCompletato", "ordineCompletato");
					}
					//linea ordini, stampa on line inventario da ordine, Indietro con ordine non completato (no)
					return navi.goBack(true);
				}else{
					//linea ordini, stampa on line inventario da ordine, Navigation
					currentForm.setConferma(true);
					return mapping.getInputForward();
				}
			}
			InventarioVO recInv =null;
			recInv = this.getInventario(currentForm.getCodPolo(), currentForm.getCodBib(),
					currentForm.getCodSerie(), currentForm.getCodInvent(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket());
			if (recInv != null) {
				if (recInv.getKeyLoc() > 0){
					currentForm.setRecInv(recInv);
					CollocazioneDettaglioVO recColl = this.getCollocazioneDettaglio(recInv.getKeyLoc(), currentForm.getTicket());
					if (recColl != null){
						currentForm.setRecColl(recColl);
						currentForm.setCodColloc(currentForm.getRecColl().getCodColloc().trim());
						currentForm.setSpecColloc(currentForm.getRecColl().getSpecColloc().trim());
					}
				}else{
					if (currentForm.getCodCollocFormato() != null && currentForm.getCodSpecifFormato() != null){
						if (currentForm.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
								|| currentForm.getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
								|| currentForm.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
							currentForm.getRecColl().getRecFS().setProgSerie(Integer.valueOf(currentForm.getCodCollocFormato()));
							currentForm.getRecColl().getRecFS().setProgNum(Integer.valueOf(currentForm.getCodSpecifFormato()));
						}
					}
				}
				NavigationElement fogliaNavigazione = navi.getCache().getCurrentElement();
				if (!fogliaNavigazione.getTesto().equals("Inserimento Collocazione Veloce") ) {
					return mapping.getInputForward();
					//					this.loadTab1(form);
				}else{
					currentForm.setProv("visualizzazionePerCV");
				}
			}else{
				throw new ValidationException("ricercaNoInv", ValidationException.errore);
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("esemplare")){
				currentForm.setDisableTastoAggiornaInIndice(false);
				currentForm.setDisableTastoSalva(false);
				currentForm.setDisableTastoEsemplare(false);
				currentForm.setDisableTastoCancInv(false);
				currentForm.setDisableTastoProven(false);
				currentForm.setDisablePerModInvDaNav(false);
				//								}
			}
			if (currentForm.isStampaEtich() && !currentForm.isDisablePerModInvDaNav()){
				request.setAttribute("prov", "insColl");
				List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
				EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
				datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
				datiStampaEtichette.setCollocazione(currentForm.getRecColl().getCodColloc().trim());
				datiStampaEtichette.setInventario(new Integer(currentForm.getCodInvent()).toString());
				datiStampaEtichette.setSequenza(currentForm.getRecInv().getSeqColl().trim());
				datiStampaEtichette.setSerie(currentForm.getCodSerie());
				datiStampaEtichette.setSezione(currentForm.getRecColl().getCodSez().trim());
				datiStampaEtichette.setSpecificazione(currentForm.getRecColl().getSpecColloc().trim());
				//
				listaEtichetteBarcode.add(datiStampaEtichette);
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
				request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
				request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
				//
				currentForm.setDisableTastoAggiornaInIndice(true);
				currentForm.setDisableTastoSalva(true);
				currentForm.setDisableTastoEsemplare(true);
				currentForm.setDisableTastoCancInv(true);
				currentForm.setDisableTastoProven(true);
				currentForm.setDisablePerModInvDaNav(true);
				return  mapping.findForward("stampaSintetica");
			}
			return mapping.getInputForward();
		}
		if (request.getAttribute("findForward") != null && request.getAttribute("findForward").equals("back")){
			request.setAttribute("keyLoc", currentForm.getKeyLoc());
			request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
			String chiamante = navi.getActionCaller();
			if (chiamante.toUpperCase().indexOf("ESAMINAINV") > -1 || chiamante.toUpperCase().indexOf("MODIFICACOLL") > -1) {
				if (chiamante.toUpperCase().indexOf("ORDINE") > -1 ){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ordineCompletamenteRicevuto"));

					currentForm.setConferma(true);

					return mapping.getInputForward();
				}

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

				this.saveToken(request);
				currentForm.setDisableTastoAggiornaInIndice(false);
				currentForm.setDisableTastoSalva(false);
				currentForm.setDisableTastoEsemplare(false);
				currentForm.setDisableTastoCancInv(false);
				currentForm.setDisableTastoProven(false);
				currentForm.setDisablePerModInvDaNav(false);
				return mapping.getInputForward();
			}
			currentForm.setDisableTastoAggiornaInIndice(false);
			currentForm.setDisableTastoSalva(false);
			currentForm.setDisableTastoEsemplare(false);
			currentForm.setDisableTastoCancInv(false);
			currentForm.setDisableTastoProven(false);
			currentForm.setDisablePerModInvDaNav(false);
			request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
			request.setAttribute("bid", currentForm.getBid());
			request.setAttribute("desc", currentForm.getIsbdCollocazione());
			navi.purgeThis();
			request.setAttribute("prov", "modificaInv");
			return mapping.findForward("listaInventariDelTitolo");
		}
		// controllo se ho già i dati in sessione;
		if(!currentForm.isSessione())		{
			currentForm.setTicket(navi.getUserTicket());
			currentForm.setCodPolo(navi.getUtente().getCodPolo());
			currentForm.setCodBib(navi.getUtente().getCodBib());
			currentForm.setDescrBib(navi.getUtente().getBiblioteca());
			currentForm.setSessione(true);
			navi.addBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL);
		}
		//		gestione richiamo
		if(request.getAttribute("chiamante")!=null) {
			currentForm.setAction((String)request.getAttribute("chiamante"));
		}
		if (request.getAttribute("codCollocFormato") != null ){
			currentForm.setCodCollocFormato((String)request.getAttribute("codCollocFormato"));
		}
		if (request.getAttribute("codSpecifFormato") != null ){
			currentForm.setCodSpecifFormato((String)request.getAttribute("codSpecifFormato"));
		}
		if (request.getAttribute("tipoColloc") != null ){
			currentForm.setTipoColloc((String)request.getAttribute("tipoColloc"));
		}
		if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("CV")
				|| request.getAttribute("prov").equals("listaProvenienze"))){
		}else{
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("esemplare")){
				currentForm.setDisableTastoAggiornaInIndice(false);
				currentForm.setDisableTastoSalva(false);
				currentForm.setDisableTastoEsemplare(false);
				currentForm.setDisableTastoCancInv(false);
				currentForm.setDisableTastoProven(false);
				currentForm.setDisablePerModInvDaNav(false);
				//								}
			}
			if (currentForm.isStampaEtich() && !currentForm.isDisablePerModInvDaNav()){
				request.setAttribute("prov", "insColl");
				List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
				EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
				datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
				datiStampaEtichette.setCollocazione(currentForm.getRecColl().getCodColloc().trim());
				datiStampaEtichette.setInventario(new Integer(currentForm.getCodInvent()).toString());
				datiStampaEtichette.setSequenza(currentForm.getRecInv().getSeqColl().trim());
				datiStampaEtichette.setSerie(currentForm.getCodSerie());
				datiStampaEtichette.setSezione(currentForm.getRecColl().getCodSez().trim());
				datiStampaEtichette.setSpecificazione(currentForm.getRecColl().getSpecColloc().trim());
				//
				listaEtichetteBarcode.add(datiStampaEtichette);
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
				request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
				request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
				//
				currentForm.setDisableTastoAggiornaInIndice(true);
				currentForm.setDisableTastoSalva(true);
				currentForm.setDisableTastoEsemplare(true);
				currentForm.setDisableTastoCancInv(true);
				currentForm.setDisableTastoProven(true);
				currentForm.setDisablePerModInvDaNav(true);
				return  mapping.findForward("stampaSintetica");
			}
		}

		if (request.getAttribute("prov") != null &&
				(request.getAttribute("prov").equals("CV"))){
			navi.setDescrizioneX("Inserimento Collocazione Veloce");
			navi.setTesto("Inserimento Collocazione Veloce");

			if (request.getAttribute("recInv") != null){
				currentForm.setRecInv((InventarioVO)request.getAttribute("recInv"));
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioDatiDaCreazioneCollVeloce"));

				return navi.goBack(true);
			}
		}
		try{
			if (request.getAttribute("prov") != null &&
					(request.getAttribute("prov").equals("ordine") || request.getAttribute("prov").equals("ordineIns"))){
				currentForm.setProv((String)request.getAttribute("prov"));
				if (request.getAttribute("invCreati") != null
						&& request.getAttribute("bid") != null && request.getAttribute("descrFornitore") != null){
					currentForm.setListaInvCreati((List)request.getAttribute("invCreati"));
					//					if (!request.getAttribute("codBibO").equals(Navigation.getInstance(request).getUtente().getCodBib())){
					//						currentForm.setCodBib((String)request.getAttribute("codBibO"));
					if (!request.getAttribute("codBibO").equals(currentForm.getCodBib())){
						currentForm.setCodBib((String)request.getAttribute("codBibO"));
					}
					currentForm.setBid((String)request.getAttribute("bid"));
					currentForm.setDescrFornitore((String)request.getAttribute("descrFornitore"));
					navi.setDescrizioneX("Modifica inventari da Ordine");
					navi.setTesto("Modifica inventari da Ordine");
				}
				if (request.getAttribute("prgFattura") != null){
					if (request.getAttribute("codPoloFattura") != null && request.getAttribute("codBibFattura") != null
							&& request.getAttribute("dataFattura") != null && request.getAttribute("descrFornitore") != null) {
						//						if (!request.getAttribute("codBibFattura").equals(Navigation.getInstance(request).getUtente().getCodBib())){
						//							currentForm.setCodBib((String)request.getAttribute("codBibFattura"));
						//						}
						if (!request.getAttribute("codBibFattura").equals(currentForm.getCodBib())){
							currentForm.setCodBib((String)request.getAttribute("codBibFattura"));
						}
						currentForm.setCodPoloFattura((String)request.getAttribute("codPoloFattura"));
						currentForm.setCodBibFattura((String)request.getAttribute("codBibFattura"));
						currentForm.setPrgFattura((Integer)request.getAttribute("prgFattura"));
						//manca riga fattura?
						currentForm.setDataFattura((String)request.getAttribute("dataFattura"));
						currentForm.setDescrFornitore((String)request.getAttribute("descrFornitore"));
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreParametriFattura"));

						return navi.goBack(true);
					}
				}else{
					currentForm.setCodBibFattura("");
					currentForm.setPrgFattura(0);
					currentForm.setRigaFattura(0);
					currentForm.setDataFattura("");
				}
			}else{
				if (request.getAttribute("tipoLista") != null && ((request.getAttribute("tipoLista").equals("listaInvDiColloc")) ||
						request.getAttribute("tipoLista").equals("listaInvDiCollocDaEsempl"))) {
					if (request.getAttribute("tipoLista").equals("listaInvDiColloc")){
						currentForm.setListaInv(true);
					}
					currentForm.setTipoLista((String)request.getAttribute("tipoLista"));
				}
				if (request.getAttribute("codSerie")!=null
						&& request.getAttribute("codInvent")!= null
						&& request.getAttribute("bid")!= null){
					currentForm.setCodSerie((String)request.getAttribute("codSerie"));
					currentForm.setCodInvent((Integer)(request.getAttribute("codInvent")));
					currentForm.setBid((String)request.getAttribute("bid"));
					currentForm.setCodBib((String)request.getAttribute("codBib"));
					currentForm.setDescrBib((String)request.getAttribute("descrBib"));
					if (request.getAttribute("reticolo") != null && request.getAttribute("recColl") != null){
						currentForm.setReticolo((DatiBibliograficiCollocazioneVO)request.getAttribute("reticolo"));
						currentForm.setRecColl((CollocazioneVO)request.getAttribute("recColl"));
						if (!currentForm.getRecColl().getCodBibSez().equals(currentForm.getCodBib())){
							currentForm.setCodBib(currentForm.getRecColl().getCodBibSez());
						}
						currentForm.setCodPoloSez(currentForm.getRecColl().getCodPoloSez());
						currentForm.setCodBibSez(currentForm.getRecColl().getCodBibSez());
						currentForm.setCodSez(currentForm.getRecColl().getCodSez());
						currentForm.setLoc(currentForm.getRecColl().getCodColloc());
						currentForm.setSpecLoc(currentForm.getRecColl().getSpecColloc());
						currentForm.setInvColl(true);
						currentForm.setBidColloc(currentForm.getRecColl().getBid());
						if (currentForm.getRecColl().getKeyColloc()==0){
							//non è ancora collocato (sta per ...)
							List lista = this.getTitolo(currentForm.getRecColl().getBid(), currentForm.getTicket());
							if (lista != null){
								if (lista.size() == 1){
									TitoloVO titolo = (TitoloVO)lista.get(0);
									currentForm.setIsbdCollocazione(titolo.getIsbd());
								}
							}
							currentForm.setDisableConsist(false);
							currentForm.setConsistenza("");
							currentForm.getRecColl().setConsistenza("");
						}else if (currentForm.getRecColl().getKeyColloc()!=0){
							if (request.getAttribute("recColl") != null){
								currentForm.setDisableConsist(false);
							}else{
								currentForm.setDisableConsist(true);
							}
							List lista = this.getTitolo(currentForm.getRecColl().getBid(), currentForm.getTicket());
							if (lista != null){
								if (lista.size() == 1){
									TitoloVO titolo = (TitoloVO)lista.get(0);
									currentForm.setIsbdCollocazione(titolo.getIsbd());
								}
							}
							currentForm.setConsistenza(currentForm.getRecColl().getConsistenza());
						}
						if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
							Navigation navigation =navi;
							navigation.setDescrizioneX("Inserimento Collocazione Veloce");
							navigation.setTesto("Inserimento Collocazione Veloce");
							return this.ok(mapping, currentForm, request, response);
						}
						//per la non visualizzazione dei progressivi calcolati
						if (request.getAttribute("tipoColloc") != null && request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
								|| request.getAttribute("tipoColloc") != null && request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
								|| request.getAttribute("tipoColloc") != null && request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
							if (request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
								currentForm.setTipoCollocN((String)request.getAttribute("tipoColloc"));
							}
								if (currentForm.getRecColl().getRecFS() != null){
								if (currentForm.getRecColl().getRecFS().getProgSerie() != -1 && currentForm.getRecColl().getRecFS().getProgNum() != -1){
									if (request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
											|| request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
										currentForm.setTipoColloc((String)request.getAttribute("tipoColloc"));
										currentForm.setCodColloc(currentForm.getRecColl().getRecFS().getCodFormato().substring(0,2)
												+ (currentForm.getRecColl().getCodColloc().substring(2)));
										currentForm.setSpecColloc(currentForm.getRecColl().getSpecColloc());
										//setto anche su recColl
									}else if (request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
										currentForm.setTipoCollocN((String)request.getAttribute("tipoColloc"));
										currentForm.setCodColloc(currentForm.getRecColl().getCodColloc());
										currentForm.setSpecColloc("");
									}
								}else{
									if (request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
											|| request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
										currentForm.setTipoColloc((String)request.getAttribute("tipoColloc"));
										currentForm.setCodColloc(currentForm.getRecColl().getRecFS().getCodFormato().substring(0,2)
												//												+ (" ")
												+ ("..."));
										currentForm.setSpecColloc("...");
									}else if (request.getAttribute("tipoColloc").equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
										currentForm.setTipoCollocN((String)request.getAttribute("tipoColloc"));
										currentForm.setCodColloc("...");
										currentForm.setSpecColloc("");
									}
								}
							}else{
								//provengo da inserimento coll, collocazioni presenti nel reticolo, scelta coll
								currentForm.setCodColloc(currentForm.getRecColl().getCodColloc());
								currentForm.setSpecColloc(currentForm.getRecColl().getSpecColloc());
							}
						}else{
							currentForm.setCodColloc(currentForm.getRecColl().getCodColloc());
							currentForm.setSpecColloc(currentForm.getRecColl().getSpecColloc());
						}
					}else{
						currentForm.setBidColloc("");
						currentForm.setIsbdCollocazione("");
						currentForm.setRecColl(null);
					}
				}
			}
			List listaTitInv = this.getTitolo(currentForm.getBid(), currentForm.getTicket());
			if (listaTitInv != null){
				if (listaTitInv.size() == 1){
					TitoloVO titolo = (TitoloVO)listaTitInv.get(0);
					if (!titolo.isFlagCondiviso()){
						currentForm.setIsbd("[Loc] "+titolo.getIsbd());
					}else{
						currentForm.setIsbd(titolo.getIsbd());
						String chiamante = navi.getActionCaller();
						if (chiamante != null && (chiamante.toUpperCase().indexOf("VAIAINSERIMENTOCOLL") > -1
								|| chiamante.toUpperCase().indexOf("VAIAMODIFICACOLL") > -1)){
							currentForm.setAbilitaBottoneInviaInIndice(true);
						}
					}
					if (titolo.getNatura().equals("S")){
						currentForm.setPeriodico(true);
					}
				}
			}
			//			}
			if (request.getAttribute("codProven")!=null){
				currentForm.setDescrProven((String)request.getAttribute("descrProven"));
				currentForm.getRecInv().setCodProven((String)request.getAttribute("codProven"));
				currentForm.getRecInv().setCodPoloProven((String)request.getAttribute("codPoloProven"));
				currentForm.getRecInv().setCodBibProven((String)request.getAttribute("codBibProven"));
				return mapping.getInputForward();
			}
			if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
				if (currentForm.getDescrFornitore() != null){
					currentForm.setDescrFornitore(currentForm.getDescrFornitore().trim());
				}else{
					ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
					ricercaFornitori.setCodFornitore(currentForm.getRecInv().getCodFornitore());
					ricercaFornitori.setTicket(currentForm.getTicket());
					List listaFornitori = null;
					try{
						listaFornitori = this.getRicercaListaFornitori(ricercaFornitori);
					} catch (ValidationException e) {

						if (e.getMessage() != null && e.getMessage().equals("assenzaRisultati")){
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.fornitoreNonTrovato"));

							this.saveToken(request);

							request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
							request.setAttribute("descrBib",currentForm.getDescrBib());
							request.setAttribute("bid", currentForm.getBid());
							request.setAttribute("desc", currentForm.getIsbd());
							return mapping.findForward("listaInventariDelTitolo");

						}
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						this.saveToken(request);
						return mapping.getInputForward();
					} catch (DataException e) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						this.saveToken(request);
						return navi.goBack(true);
					} catch (Exception e) { // altri tipi di errore

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						//						return mapping.getInputForward();
						return navi.goBack(true);
					}
					if (listaFornitori != null && listaFornitori.size() > 0){
						FornitoreVO fornitore = (FornitoreVO)listaFornitori.get(0);
						currentForm.setDescrFornitore(fornitore.getNomeFornitore().trim());
					}
				}
			}
			if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
				InventarioVO recInv =null;
				if (currentForm.getListaInvCreati().size()>0){
					currentForm.setNumInv(currentForm.getListaInvCreati().size());

					if (currentForm.getIndex() == 0){
						currentForm.setIndex(0);
					}
					if (currentForm.getIndex() < currentForm.getListaInvCreati().size()){
						//					currentForm.setIndex(currentForm.getIndex()+1);
						recInv = (InventarioVO)currentForm.getListaInvCreati().get(currentForm.getIndex());
						if (recInv.getListaNote() != null && recInv.getListaNote().size() >= 1){
							scorriNote(currentForm, recInv);
						}else{
							recInv.setListaNote(null);
						}
						currentForm.setRecInv(recInv);

						//
						ActionForward loadDefault = loadDefault(request, mapping, form);
						if (loadDefault != null)
							return loadDefault;

						//setto nel recInv i valori di default
						controllaDefaultInv(currentForm, recInv);

						//
						currentForm.setDisableCodTipOrd(true);
						currentForm.setDisableDataIns(true);
						if (recInv.getCodOrd() != null){
							currentForm.setDisableTastoProven(true);
							currentForm.setDisableProven(true);
						}

						if (currentForm.getFolder() != null && currentForm.getFolder().equals("tab3")){
							if (currentForm.getRecInv().getCodScarico() != null && currentForm.getRecInv().getCodScarico().equals("T")){
								currentForm.setTrasferimento(true);
								if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("sinteticaBiblioteche")){
									BibliotecaVO biblioteca = (BibliotecaVO)request.getAttribute("biblioteca");
									currentForm.getRecInv().setCodPoloScar(biblioteca.getCod_polo());
									currentForm.getRecInv().setVersoBibDescr(biblioteca.getNom_biblioteca());
									currentForm.getRecInv().setIdBibScar(biblioteca.getIdBiblioteca());
								}
							}

							if (currentForm.isPeriodico()){
								if (currentForm.getRecInv().getCodMatInv().trim().equals("")){
									currentForm.getRecInv().setCodMatInv("VP");
									currentForm.getRecInv().setAnnoAbb("");
									currentForm.getRecInv().setNumVol("");
								}
							}
							return mapping.getInputForward();
						}else{
							currentForm.setFolder("tab1");
							//							this.loadTab1(form);
							this.loadTab1(form, currentForm.getRecInv());
						}
						currentForm.setDisable(true);
						currentForm.setSessione(true);
						if (request.getAttribute("tipoOperazione")!=null){
							currentForm.setTipoOperazione((String)request.getAttribute("tipoOperazione"));
						}
						currentForm.setIndex(currentForm.getIndex()+1);
						if (currentForm.isPeriodico()){
							controlloValorePerPeriodico(currentForm, currentForm.getRecInv());
							if (currentForm.getRecInv().getCodMatInv().trim().equals("")){
								currentForm.getRecInv().setCodMatInv("VP");
							}
						}
						return mapping.getInputForward();
					}
				}
			}else{
				InventarioVO recInv =null;
				if (currentForm.getRecInv().getCodInvent() == 0){
					recInv = this.getInventario(currentForm.getCodPolo(), currentForm.getCodBib(),
							currentForm.getCodSerie(), currentForm.getCodInvent(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket());
					if (recInv != null) {
						if (currentForm.isPeriodico()){
							controlloValorePerPeriodico(currentForm, recInv);
						}
						//
						//if (!ValidazioneDati.isFilled(recInv.getCodFrui())){ //è più giusto controllare il codSit
						if (recInv.isPrecisato() ) {
							ActionForward loadDefault = loadDefault(request, mapping, form);
							if (loadDefault != null){
								return loadDefault;
							}
							//setto nel recInv i valori di default
							controllaDefaultInv(currentForm, recInv);
						}
						if (ValidazioneDati.isFilled(recInv.getCodOrd())){
							currentForm.setDisableTastoProven(true);
							currentForm.setDisableProven(true);
						}
						if (recInv.getListaNote() != null && recInv.getListaNote().size() >= 1){
							scorriNote(currentForm, recInv);
						}

						DocumentoFisicoFormTypes formType = DocumentoFisicoFormTypes.getFormType(navi.getCallerForm());
						if (formType != DocumentoFisicoFormTypes.ESAME_INVENTARIO)
							if (!ValidazioneDati.isFilled(recInv.getDataInsPrimaColl()))
								recInv.setDataInsPrimaColl(DateUtil.formattaData(new Date(System.currentTimeMillis())));

						if (currentForm.isListaInv()){
							CollocazioneDettaglioVO recColl = this.getCollocazioneDettaglio(recInv.getKeyLoc(), currentForm.getTicket());
							if (recColl != null){
								currentForm.setRecColl(recColl);
								currentForm.setCodSez(currentForm.getRecColl().getCodSez());
								currentForm.setCodColloc(currentForm.getRecColl().getCodColloc());
								currentForm.setSpecColloc(currentForm.getRecColl().getSpecColloc());
								currentForm.setBidColloc(currentForm.getRecColl().getBid());
								if (recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
									currentForm.setTipoCollocN(recColl.getTipoColloc());
								}
								List listaTitColl = this.getTitolo(currentForm.getRecColl().getBid(), currentForm.getTicket());
								if (listaTitColl != null){
									if (listaTitColl.size() == 1){
										TitoloVO titolo = (TitoloVO)listaTitColl.get(0);
										currentForm.setIsbdCollocazione(titolo.getIsbd());
									}
								}
							}else{
								throw new ValidationException("noColl", ValidationException.errore);
							}
						}
						if (recInv.getCodProven() != null){
							currentForm.setDescrProven(recInv.getDescrProven());
						}
						if (!recInv.getCodBibF().trim().equals("")){
							ListaSuppFatturaVO ricercaFatture=new ListaSuppFatturaVO();
							ricercaFatture.setCodBibl(recInv.getCodBibF());
							ricercaFatture.setProgrFattura(recInv.getProgrFattura());
							ricercaFatture.setAnnoFattura(recInv.getAnnoFattura());
							ricercaFatture.setTipoFattura("F");
							ricercaFatture.setTicket(currentForm.getTicket());
							List listaFattura = null;
							try{
								listaFattura = this.getRicercaListaFatture(ricercaFatture);
							} catch (ValidationException e) {

								if (e.getMessage() != null && e.getMessage().equals("assenzaRisultati")){
									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.fatturaNonTrovata"));

									this.saveToken(request);

									request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
									request.setAttribute("descrBib",currentForm.getDescrBib());
									request.setAttribute("bid", currentForm.getBid());
									request.setAttribute("desc", currentForm.getIsbd());
									return mapping.findForward("listaInventariDelTitolo");

								}
								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

								this.saveToken(request);
								return mapping.getInputForward();
							} catch (DataException e) {

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

								this.saveToken(request);
								return navi.goBack(true);
							} catch (Exception e) { // altri tipi di errore

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

								//								return mapping.getInputForward();
								return navi.goBack(true);
							}
							if (listaFattura != null && listaFattura.size() > 0){
								FatturaVO fattura = (FatturaVO)listaFattura.get(0);
								currentForm.setAnnoFattura(String.valueOf(fattura.getDataFattura()));//almaviva2 AnnoFatt
								currentForm.setDataFattura(String.valueOf(fattura.getDataFattura().trim()));
								currentForm.setNumFattura(String.valueOf(fattura.getNumFattura()).trim());
								currentForm.setDescrFornitore(fattura.getFornitoreFattura().getDescrizione().trim());
							}else{
								throw new DataException("fatturaNonTrovata");
							}
						}
						currentForm.setRecInv(recInv);

					}else{
						throw new ValidationException("ricercaNoInv", ValidationException.errore);
					}
				}
				//				if (currentForm.getRecInv().getTitNatura().equals("S")){
				//					currentForm.setPeriodico(true);
				//				}
			}
			currentForm.setNumCarico(currentForm.getRecInv().getNumCarico());//numCarico
			if (currentForm.isPeriodico()){
				if (currentForm.getRecInv().getCodMatInv().trim().equals("")){
					currentForm.getRecInv().setCodMatInv("VP");
				}
			}
			if (currentForm.getFolder() != null && currentForm.getFolder().equals("tab3")){
				if (currentForm.getRecInv().getCodScarico() != null && currentForm.getRecInv().getCodScarico().equals("T")){
					currentForm.setTrasferimento(true);
					if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("sinteticaBiblioteche")){
						BibliotecaVO biblioteca = (BibliotecaVO)request.getAttribute("biblioteca");
						currentForm.getRecInv().setCodPoloScar(biblioteca.getCod_polo());
						currentForm.getRecInv().setVersoBibDescr(biblioteca.getNom_biblioteca());
						currentForm.getRecInv().setIdBibScar(biblioteca.getIdBiblioteca());
					}
				}

				return mapping.getInputForward();
			}
			currentForm.setFolder("tab1");
			this.loadTab1(form, currentForm.getRecInv());
			//			this.loadTab1(form);
			currentForm.setDisable(true);
			currentForm.setSessione(true);
			if (request.getAttribute("tipoOperazione")!=null){
				currentForm.setTipoOperazione((String)request.getAttribute("tipoOperazione"));
			}
			if (request.getAttribute("codProven")!=null){
				currentForm.setDescrProven((String)request.getAttribute("descrProven"));
				currentForm.getRecInv().setCodProven((String)request.getAttribute("codProven"));
			}


		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return mapping.getInputForward();
		} catch (DataException e) {
			if (currentForm.getAction() != null){
				currentForm.setFolder("tab1");
				this.loadTab1(form, currentForm.getRecInv());
				//				this.loadTab1(form);
				currentForm.setDisable(true);
				currentForm.setSessione(true);
				if (request.getAttribute("tipoOperazione")!=null){
					currentForm.setTipoOperazione((String)request.getAttribute("tipoOperazione"));
				}
				if (request.getAttribute("codProven")!=null){
					currentForm.setDescrProven((String)request.getAttribute("descrProven"));
					currentForm.getRecInv().setCodProven((String)request.getAttribute("codProven"));
				}
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return navi.goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			//			return mapping.getInputForward();
			return navi.goBack(true);
		}

		return mapping.getInputForward();
	}

	private void scorriNote(ModificaInvCollForm currentForm, InventarioVO recInv) {
		for (int i=0; i<recInv.getListaNote().size();i++){
			NotaInventarioVO nota = recInv.getListaNote().get(i);
			CodiceNotaVO codiceNota = new CodiceNotaVO();
			codiceNota.setCodice1(nota.getCodNota().trim());
			codiceNota.setDescrizione1(nota.getDescrNota().trim());
			currentForm.addListaNoteDinamica(codiceNota);
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * ModificaInvCollAction.java
	 * controlloValorePerPeriodico
	 * void
	 * @param myForm
	 * @param recInv
	 *
	 *
	 */
	private void controlloValorePerPeriodico(ModificaInvCollForm currentForm,
			InventarioVO recInv) {
		if (recInv.getValore().equals("0,00")){
			//sono in inserimento dei valori obbligatori
			currentForm.setAppoggioCampoObb(recInv.getValore());
			recInv.setAnnoAbb("");
			recInv.setNumVol("");
		}else{
			//sono in modifica inventario
			if (recInv.getAnnoAbb().trim().equals("0")){
				recInv.setAnnoAbb("");
				recInv.setNumVol("");
				currentForm.setAppoggioCampoObb("0");
			}else{
				currentForm.setAppoggioCampoObb("annoDiversoDaZero");
			}
		}
	}

	private void controllaDefaultInv(ModificaInvCollForm currentForm, InventarioVO recInv) {
		if (!ValidazioneDati.isFilled(recInv.getCodFrui()))
			recInv.setCodFrui(currentForm.getCatFruiDefault() != null ? currentForm.getCatFruiDefault(): " ");

		if (!ValidazioneDati.isFilled(recInv.getTipoAcquisizione()) )
			if (!ValidazioneDati.isFilled(recInv.getCodTipoOrd()))
				recInv.setTipoAcquisizione(currentForm.getTipoAcqDefault() != null ? currentForm.getTipoAcqDefault() : " ");
			else {
				if (recInv.getCodTipoOrd().equals("V"))
					recInv.setTipoAcquisizione("A");
				else
					recInv.setTipoAcquisizione(recInv.getCodTipoOrd());
			}

		recInv.setCodTipoOrd(recInv.getCodTipoOrd() != null ? recInv.getCodTipoOrd() : " ");
		if (!ValidazioneDati.isFilled(recInv.getCodNoDisp()) )
			recInv.setCodNoDisp(currentForm.getCodNoDispDefault() != null ? currentForm.getCodNoDispDefault() : " ");
		if (!ValidazioneDati.isFilled(recInv.getStatoConser()) )
			recInv.setStatoConser(currentForm.getCodStatoConsDefault() != null ? currentForm.getCodStatoConsDefault() : " ");
		if (!ValidazioneDati.isFilled(recInv.getCodProven()) && ValidazioneDati.strIsNull(recInv.getDescrProven()) ){
			if (!ValidazioneDati.isFilled(recInv.getCodTipoOrd()) ){
				recInv.setCodPoloProven(currentForm.getCodPoloProvDefault() != null ? currentForm.getCodPoloProvDefault() : null);
				recInv.setCodBibProven(currentForm.getCodBibProvDefault() != null ? currentForm.getCodBibProvDefault() : null);
				recInv.setCodProven(currentForm.getCodProvDefault() != null ? currentForm.getCodProvDefault() : "");
				recInv.setDescrProven(currentForm.getDescrProvDefault() != null ? currentForm.getDescrProvDefault() : "");
			}
		}

		if (!ValidazioneDati.isFilled(recInv.getCodMatInv()) )
			if(currentForm.isPeriodico()){
				if (recInv.getCodMatInv().trim().equals("")){
					recInv.setCodMatInv("VP");
				}else{
					recInv.setCodMatInv("VM");
				}
			}else{
				recInv.setCodMatInv("VM");
			}
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();
		try {
			InventarioVO appoInv = null;

			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
				//non fa niente ma deve essere così per coprire tutti i casi in cui è diverso da collocazione veloce
				navi.setDescrizioneX("Inserimento Collocazione Veloce");
				navi.setTesto("Inserimento Collocazione Veloce");
			} else {
				this.checkForm(request, form, mapping);
			}
			currentForm.setTipoOperazione(DocumentoFisicoCostant.M_MODIFICA_INV);
			//controllo data ingresso 05/07/2011 rp
			if (currentForm.getRecInv().getDataIngresso() == null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataIngressoObbligatoria"));

				this.saveToken(request);
				return mapping.getInputForward();
			}else{
				if (currentForm.getRecInv().getDataIngresso() != null  && currentForm.getRecInv().getDataIngresso().trim().equals("")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataIngressoObbligatoria"));

					this.saveToken(request);
					return mapping.getInputForward();
				}else{
					//rp bug 0004856 esercizio
					int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataIngresso());
					if (codRitorno != ValidazioneDati.DATA_OK){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataIngressoErrata"));

						resetToken(request);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
				}
			}
			//Fattura e data carico sono sullo stesso tab
			if (currentForm.getNumFattura() != null && !currentForm.getNumFattura().trim().equals("")){
				//tab fattura
				currentForm.getRecInv().setCodBibF(currentForm.getRecInv().getCodBibO());
				//
				int codRitorno = -1;
				if (currentForm.getDataFattura().trim().equals("") || currentForm.getDataFattura().equals("00/00/0000")){
					currentForm.getRecInv().setDataFattura("00/00/0000");
				}else{
					codRitorno = ValidazioneDati.validaData_1(currentForm.getDataFattura().trim());
					if (codRitorno != ValidazioneDati.DATA_OK){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataFatturaErrata"));

						this.saveToken(request);
						return mapping.getInputForward();
					}
					currentForm.getRecInv().setAnnoFattura(currentForm.getDataFattura().substring(6));//almaviva2 AnnoFatt
					currentForm.getRecInv().setDataFattura(currentForm.getDataFattura());
				}
				//
				currentForm.getRecInv().setNumFattura(String.valueOf(currentForm.getNumFattura()));
				if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
					currentForm.getRecInv().setProgrFattura(String.valueOf(0));
					currentForm.getRecInv().setRigaFattura(String.valueOf(0));
				}else{
					if (currentForm.getRecInv().getProgrFattura().equals("") && currentForm.getRecInv().getRigaFattura().equals("")){
						currentForm.getRecInv().setProgrFattura(String.valueOf(0));
						currentForm.getRecInv().setRigaFattura(String.valueOf(0));
					}
				}
			}else{//fattura = null
				if (currentForm.getDataFattura() != null && !currentForm.getDataFattura().trim().equals("")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inPresenzaDellaDataFattNumFatturaObbligatorio"));

					this.saveToken(request);
					return mapping.getInputForward();
				}else{
					currentForm.getRecInv().setDataFattura("00/00/0000");
				}
				currentForm.getRecInv().setNumFattura("");
				currentForm.getRecInv().setProgrFattura(String.valueOf(0));
				currentForm.getRecInv().setRigaFattura(String.valueOf(0));
			}
			//trattamento carico
			if (currentForm.getRecInv().getDataCarico() != null ){
				if ((currentForm.getRecInv().getDataCarico().trim().equals("") ||
						currentForm.getRecInv().getDataCarico().trim().equals("31/12/9999") || currentForm.getRecInv().getDataCarico().trim().equals("01/01/0001"))){
					currentForm.getRecInv().setDataCarico("00/00/0000");
				}
			}else{
				currentForm.getRecInv().setDataCarico("00/00/0000");//gestione campo nullable
			}
			if (currentForm.getRecInv().getCodCarico()!=null && !currentForm.getRecInv().getCodCarico().trim().equals("")) {
				if (currentForm.getRecInv().getDataCarico()!=null ) {
					if (currentForm.getRecInv().getDataCarico().equals("00/00/0000") ||
							currentForm.getRecInv().getDataCarico().trim().equals("31/12/9999") || currentForm.getRecInv().getDataCarico().trim().equals("01/01/0001")){
					//richiesta Rossana/: se il motivo di carico è presente
					//e la data di carico è null o 01/01/0001 o 31/12/9999, questa si imposta uguale alla data di ingresso
						currentForm.getRecInv().setDataCarico(currentForm.getRecInv().getDataIngresso());
//
//						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataCaricoObbligatoria"));
//
//						resetToken(request);
//						request.setAttribute("currentForm", currentForm);
//						return mapping.getInputForward();
					}

					int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataCarico());
					if (codRitorno != ValidazioneDati.DATA_OK){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataCaricoErrata"));

						resetToken(request);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
					if (currentForm.getRecInv().getNumCarico()!=null && (currentForm.getRecInv().getNumCarico().trim().equals("0")
							|| currentForm.getRecInv().getNumCarico().trim().equals(""))) {
						currentForm.getRecInv().setNumCarico("0");
					}
					if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecInv().getNumCarico()))) {
						if (ValidazioneDati.strIsNumeric(String.valueOf(currentForm.getRecInv().getNumCarico()))){
							if (String.valueOf(currentForm.getRecInv().getNumCarico()).length()>9) {

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroBuonoCaricoEccedente"));

								resetToken(request);
								request.setAttribute("currentForm", currentForm);
								return mapping.getInputForward();
							}else{
								//trattamento validazione numCarico deve essere eseguito quando:
								//- da DB arriva non impostato
								//- su mappa viene variato rispetto a quello presente su DB
								if (currentForm.getNumCarico() != null && !currentForm.getNumCarico().equals("")){
									if (!currentForm.getNumCarico().equals(currentForm.getRecInv().getNumCarico())){
										if (currentForm.getRecInv().getNumCarico().equals("0")){
											throw new ValidationException("numeroBuonoDeveEssereMaggioreDi0 ", ValidationException.errore);
										}
										if (!this.getSerieNumeroCarico(currentForm.getRecInv().getCodPolo(), currentForm.getRecInv().getCodBib(),
												currentForm.getRecInv().getCodSerie(), currentForm.getRecInv().getNumCarico(), currentForm.getTicket())) {
											currentForm.getRecInv().setNumCarico(currentForm.getNumCarico());

											LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroBuonoNonPresente"));

											resetToken(request);
											request.setAttribute("currentForm", currentForm);
											return mapping.getInputForward();
										}
									}//se è uguale ok
								}
							}
						}else{
							throw new ValidationException("numeroBuonoCaricoNumerico", ValidationException.errore);
						}
					}
					//motivo di carico = trasferimento non gestito perchè nell'inventario non
					//sono presenti campi codPolo e codBib di provenienza di carico - eventualmente si toglie
					//dalla lista dei motivi di carico la voce Trasferimento...
					// 	 					if (currentForm.getRecInv().getCodCarico().trim().equals("T")){
					// 	 						if (currentForm.getRecInv().getIdBibCar() == 0){
					//
					// 	 							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoScaricoTScegliereBiblioDallaLista"));
					//
					// 	 							resetToken(request);
					// 	 							request.setAttribute("currentForm", currentForm);
					// 	 							return mapping.getInputForward();
					// 	 						}
					// 	 					}
				}//la data non è mai null perchè impostata sul db a 31/12/9999
			}else{
				if (currentForm.getRecInv().getDataCarico()!=null && (currentForm.getRecInv().getDataCarico().trim().equals("")
						|| currentForm.getRecInv().getDataCarico().trim().equals("00/00/0000"))) {
					if (currentForm.getRecInv().getNumCarico()!=null && (currentForm.getRecInv().getNumCarico().trim().equals("0")
							|| currentForm.getRecInv().getNumCarico().trim().equals(""))) {
						currentForm.getRecInv().setNumScarico("0");
					}else{
//						if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecInv().getNumCarico()))) {
//							if (ValidazioneDati.strIsNumeric(String.valueOf(currentForm.getRecInv().getNumCarico()))){
//								if (String.valueOf(currentForm.getRecInv().getNumCarico()).length()>9) {
//
//									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroBuonoCaricoEccedente"));
//
//									resetToken(request);
//									request.setAttribute("currentForm", currentForm);
//									return mapping.getInputForward();
//								}else{
//									//trattamento validazione numCarico deve essere eseguito quando:
//									//- da DB arriva non impostato
//									//- su mappa viene variato rispetto a quello presente su DB
//									if (currentForm.getNumCarico() != null && !currentForm.getNumCarico().equals("")){
//										if (!currentForm.getNumCarico().equals(currentForm.getRecInv().getNumCarico())){
//											if (currentForm.getRecInv().getNumCarico().equals("0")){
//												throw new ValidationException("numeroBuonoDeveEssereMaggioreDi0 ", ValidationException.errore);
//											}
//											if (!this.getSerieNumeroCarico(currentForm.getRecInv().getCodPolo(), currentForm.getRecInv().getCodBib(),
//													currentForm.getRecInv().getCodSerie(), currentForm.getRecInv().getNumCarico(), currentForm.getTicket())) {
//												currentForm.getRecInv().setNumCarico(currentForm.getNumCarico());
//
//												LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroBuonoNonPresente"));
//
//												resetToken(request);
//												request.setAttribute("currentForm", currentForm);
//												return mapping.getInputForward();
//											}
//										}//se è uguale ok
//									}
//								}
//							}else{
//								throw new ValidationException("numeroBuonoCaricoNumerico", ValidationException.errore);
//							}
//						}

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoCaricoObbligatorio"));

						resetToken(request);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
				}else{
//					int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataCarico());
//					if (codRitorno != ValidazioneDati.DATA_OK){
//
//						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataCaricoErrata"));
//
//						resetToken(request);
//						request.setAttribute("currentForm", currentForm);
//						return mapping.getInputForward();
//					}

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoCaricoObbligatorio"));

					resetToken(request);
					request.setAttribute("currentForm", currentForm);
					return mapping.getInputForward();
				}
			}
			//trattamento scarico
			if (currentForm.getRecInv().getDataScarico() != null){
				if ((currentForm.getRecInv().getDataScarico().trim().equals("") ||
						currentForm.getRecInv().getDataScarico().trim().equals("31/12/9999") || currentForm.getRecInv().getDataScarico().trim().equals("01/01/0001"))){
					currentForm.getRecInv().setDataScarico("00/00/0000");
				}
			}else{
				currentForm.getRecInv().setDataScarico("00/00/0000");//gestione campo nullable
			}

			if (currentForm.getRecInv().getDataDelibScar() != null){
				if ((currentForm.getRecInv().getDataDelibScar().trim().equals("") ||
						currentForm.getRecInv().getDataDelibScar().trim().equals("31/12/9999") || currentForm.getRecInv().getDataDelibScar().trim().equals("01/01/0001"))){
					currentForm.getRecInv().setDataDelibScar("00/00/0000");
				}
			}else{
				currentForm.getRecInv().setDataScarico("00/00/0000");//gestione campo nullable
			}
			if (currentForm.getRecInv() != null){
				if (currentForm.getRecInv().getCodScarico()!=null && !currentForm.getRecInv().getCodScarico().trim().equals("")) {
					if (currentForm.getRecInv().getDataScarico()!=null ) {
						if (currentForm.getRecInv().getDataScarico().equals("00/00/0000")){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataScaricoObbligatoria"));

							resetToken(request);
							request.setAttribute("currentForm", currentForm);
							return mapping.getInputForward();
						}

						int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataScarico());
						if (codRitorno != ValidazioneDati.DATA_OK){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataScaricoErrata"));

							resetToken(request);
							request.setAttribute("currentForm", currentForm);
							return mapping.getInputForward();
						}

						if ((!ValidazioneDati.equals(currentForm.getRecInv().getDataDelibScar().trim(), "00/00/0000"))) {
							int codRitorno1 = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataDelibScar());
							if (codRitorno1 != ValidazioneDati.DATA_OK){

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataDeliberaErrata"));

								resetToken(request);
								request.setAttribute("currentForm", currentForm);
								return mapping.getInputForward();
							}
						}
						if (currentForm.getRecInv().getNumScarico()!=null && (currentForm.getRecInv().getNumScarico().trim().equals("0")
								|| currentForm.getRecInv().getNumScarico().trim().equals(""))) {
							currentForm.getRecInv().setNumScarico("0");
						}
						if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecInv().getNumScarico()))) {
							if (ValidazioneDati.strIsNumeric(String.valueOf(currentForm.getRecInv().getNumScarico()))){
								if (String.valueOf(currentForm.getRecInv().getNumScarico()).length()>9) {

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroBuonoScaricoEccedente"));

									resetToken(request);
									request.setAttribute("currentForm", currentForm);
									return mapping.getInputForward();
								}
							}else{
								throw new ValidationException("numeroBuonoScaricoNumerico", ValidationException.errore);
							}
						}
						if (currentForm.getRecInv().getCodScarico().trim().equals("T")){
							if (currentForm.getRecInv().getIdBibScar() == 0){

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoScaricoTScegliereBiblioDallaLista"));

								resetToken(request);
								request.setAttribute("currentForm", currentForm);
								return mapping.getInputForward();
							}
						}
					}
				}else{
					if (currentForm.getRecInv().getDataScarico()!=null && (currentForm.getRecInv().getDataScarico().trim().equals("")
							|| currentForm.getRecInv().getDataScarico().trim().equals("00/00/0000"))) {
						if (currentForm.getRecInv().getNumScarico()!=null && (currentForm.getRecInv().getNumScarico().trim().equals("0")
								|| currentForm.getRecInv().getNumScarico().trim().equals(""))) {
							currentForm.getRecInv().setNumScarico("0");
							if (currentForm.getRecInv().getDataDelibScar()!=null && (currentForm.getRecInv().getDataDelibScar().trim().equals("")
									|| currentForm.getRecInv().getDataDelibScar().trim().equals("00/00/0000"))) {
								if (currentForm.getRecInv().getDeliberaScarico()!=null && (!currentForm.getRecInv().getDeliberaScarico().trim().equals(""))) {

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoScaricoObbligatorio"));

									resetToken(request);
									request.setAttribute("currentForm", currentForm);
									return mapping.getInputForward();
								}
							}else{
								int codRitorno1 = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataDelibScar());
								if (codRitorno1 != ValidazioneDati.DATA_OK){

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataDeliberaErrata"));

									resetToken(request);
									request.setAttribute("currentForm", currentForm);
									return mapping.getInputForward();
								}

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoScaricoObbligatorio"));

								resetToken(request);
								request.setAttribute("currentForm", currentForm);
								return mapping.getInputForward();
							}
						}else{
							if (!ValidazioneDati.strIsNull(String.valueOf(currentForm.getRecInv().getNumScarico()))) {
								if (ValidazioneDati.strIsNumeric(String.valueOf(currentForm.getRecInv().getNumScarico()))){
									if (String.valueOf(currentForm.getRecInv().getNumScarico()).length()>9) {

										LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroBuonoScaricoEccedente"));

										resetToken(request);
										request.setAttribute("currentForm", currentForm);
										return mapping.getInputForward();
									}
								}else{
									throw new ValidationException("numeroBuonoScaricoNumerico", ValidationException.errore);
								}
							}

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoScaricoObbligatorio"));

							resetToken(request);
							request.setAttribute("currentForm", currentForm);
							return mapping.getInputForward();
						}
					}else{
						int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataScarico());
						if (codRitorno != ValidazioneDati.DATA_OK){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataScaricoErrata"));

							resetToken(request);
							request.setAttribute("currentForm", currentForm);
							return mapping.getInputForward();
						}

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoScaricoObbligatorio"));

						resetToken(request);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
				}

			}
			if (currentForm.getRecColl()!=null){
				if (currentForm.getRecColl().getKeyColloc()==0){
					currentForm.getRecInv().setUteInsPrimaColl("");
				}
			}else{
				currentForm.getRecInv().setUteInsPrimaColl("");
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
				navi.setDescrizioneX("Inserimento Collocazione Veloce");
				navi.setTesto("Inserimento Collocazione Veloce");
			}else{
				this.loadTab3(currentForm.getRecInv(), form);
			}
			//
			if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
				appoInv = new InventarioVO();
				appoInv.setSeqColl(currentForm.getRecInv().getSeqColl());
				appoInv.setCodMatInv(currentForm.getRecInv().getCodMatInv());
				appoInv.setCodTipoOrd(currentForm.getRecInv().getCodTipoOrd());
				//				appoInv.setCodTipoOrd(currentForm.getRecInv().getTipoAcquisizione());
				appoInv.setTipoAcquisizione(currentForm.getRecInv().getTipoAcquisizione());
				appoInv.setDescrProven(currentForm.getRecInv().getDescrProven());
				appoInv.setPrecInv(currentForm.getRecInv().getPrecInv());
				appoInv.setCodFrui(currentForm.getRecInv().getCodFrui());
				appoInv.setStatoConser(currentForm.getRecInv().getStatoConser());
				appoInv.setCodNoDisp(currentForm.getRecInv().getCodNoDisp());
				appoInv.setValore(currentForm.getRecInv().getValore());
				appoInv.setImporto(currentForm.getRecInv().getImporto());
				appoInv.setCodRiproducibilita(currentForm.getRecInv().getCodRiproducibilita());
				if (currentForm.getRecInv().getCodFrui().trim().equals("")){
					//imposto un valore fittizio per superare la validazione
					currentForm.getRecInv().setCodFrui("$");
				}

				if (currentForm.isStampaEtich()){
					if (currentForm.getIndex() <= currentForm.getListaInvCreati().size()){
						EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
						datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
						datiStampaEtichette.setCollocazione("");
						datiStampaEtichette.setInventario(new Integer(currentForm.getRecInv().getCodInvent()).toString());
						datiStampaEtichette.setSequenza("");
						datiStampaEtichette.setSerie(currentForm.getRecInv().getCodSerie());
						datiStampaEtichette.setSezione("");
						datiStampaEtichette.setSpecificazione("");
						currentForm.getListaEtichetteBarcode().add(datiStampaEtichette);
					}
				}
			}
			//
			if (currentForm.getListaNoteDinamica() != null && currentForm.getListaNoteDinamica().size() > 0){
				CodiceNotaVO notaDin = null;
				NotaInventarioVO nota = null;
				List<NotaInventarioVO> listaNote = new ArrayList<NotaInventarioVO>();
				HashSet<String> noteDupl = new HashSet<String>();
				for (int i = 0; i < currentForm.getListaNoteDinamica().size(); i++) {
					notaDin = currentForm.getListaNoteDinamica().get(i);
					if ((notaDin.getCodice1() != null && !notaDin.getCodice1().trim().equals("")) &&
							(notaDin.getDescrizione1() != null && !notaDin.getDescrizione1().trim().equals(""))){
						nota = new NotaInventarioVO();
						String[] splitRec = notaDin.getCodice1().split(" ");
						nota.setCodNota(splitRec[0]);
						nota.setDescrNota(notaDin.getDescrizione1());
						nota.setUteIns(navi.getUtente().getFirmaUtente());
						nota.setUteAgg(navi.getUtente().getFirmaUtente());
						if (noteDupl.contains(nota.getCodNota())){
							throw new ValidationException("nonEpossibileInserirePiuNoteAventiLoStessoCodice", ValidationException.errore);
						}else{
							noteDupl.add(nota.getCodNota());
						}
						listaNote.add(nota);
					}else{
						if (i == 0){
							throw new ValidationException("notaNonValorizzata", ValidationException.errore);
						}
					}
				}
				currentForm.getRecInv().setListaNote(listaNote);
			}else{
				currentForm.getRecInv().setListaNote(null);
				currentForm.setListaNoteDinamica(null);
			}
			//controllo dati periodico
			if (currentForm.isPeriodico()){
				this.controlloDatiPeriodico(currentForm);
				if (currentForm.getRecInv().getAnnoAbb().trim().equals("")){
					currentForm.getRecInv().setAnnoAbb("0");
					currentForm.getRecInv().setNumVol("0");
				}
			}
			//almaviva5_20100924 controllo modifica provenienza
			String descrProven = ValidazioneDati.trimOrEmpty(currentForm.getDescrProven()).toUpperCase();
			if (!ValidazioneDati.isFilled(descrProven)) {	//pulizia campi
				currentForm.getRecInv().setCodPoloProven("");
				currentForm.getRecInv().setCodBibProven("");
				currentForm.getRecInv().setDescrProven("");
				currentForm.getRecInv().setCodProven("");
			}
			else {	//validazione provenienza
				OrdinamentoUnicode u = new OrdinamentoUnicode();
				ProvenienzaInventarioVO prov = this.getProvenienza(currentForm.getCodPolo(), currentForm.getCodBib(),
						currentForm.getRecInv().getCodProven(), currentForm.getTicket());
				if (prov == null || !u.convert(descrProven).equals(u.convert(prov.getDescrProvenienza()))) {
					//se non ho trovato la prov. per codice oppure la descrizione sulla maschera differisce
					//da quella in base dati si assume che la descrizione contenga un codice prov. da caricare da DB
					prov = this.getProvenienza(currentForm.getCodPolo(), currentForm.getCodBib(),
							descrProven, currentForm.getTicket());
				}

				if (prov == null) {
					throw new ValidationException("provenienzaNonEsiste", ValidationException.errore);
				}else{
					currentForm.getRecInv().setCodPoloProven((prov.getCodPolo()));
					currentForm.getRecInv().setCodBibProven((prov.getCodBib()));
					currentForm.getRecInv().setDescrProven(prov.getDescrProvenienza());
					currentForm.getRecInv().setCodProven(prov.getCodProvenienza());
				}
			}
			if (currentForm.getRecInv().getIdAccessoRemoto() != null && !currentForm.getRecInv().getIdAccessoRemoto().equals("")){
				String uri = currentForm.getRecInv().getIdAccessoRemoto().trim().replaceAll("\\s+", "");//tolgo tutti gli spazi anche quelli intermedi
				if (uri.length() > Constants.URI_MAX_LENGTH) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.urlNonMaggioreDiXXX", Constants.URI_MAX_LENGTH));

					this.saveToken(request);
					return mapping.getInputForward();
				}
				currentForm.getRecInv().setIdAccessoRemoto(uri);
			}


			//validazione data redisp
			if (currentForm.getRecInv().getCodNoDisp()!=null && !currentForm.getRecInv().getCodNoDisp().trim().equals("")) {
				//se indico il motivo di non disponibilità, la data non è obbligatoria
				if (currentForm.getRecInv().getDataRedisp()!=null && !currentForm.getRecInv().getDataRedisp().trim().equals("")) {
					int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataRedisp());
					if (codRitorno != ValidazioneDati.DATA_OK){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataRedispErrata"));

						resetToken(request);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
				}
			}else{
				//motivo non indicato
				//se non indico il motivo ed indico la data, è richiesto di indicare il motivo
				if (currentForm.getRecInv().getDataRedisp()!=null && !(currentForm.getRecInv().getDataRedisp().trim().equals(""))) {
					int codRitorno = ValidazioneDati.validaData_1(currentForm.getRecInv().getDataRedisp());
					if (codRitorno != ValidazioneDati.DATA_OK){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataRedispErrata"));

						resetToken(request);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.motivoNoDispObbligatorio"));

					resetToken(request);
					request.setAttribute("currentForm", currentForm);
					return mapping.getInputForward();
				}
			}

			//update
			currentForm.getRecInv().setUteAgg(navi.getUtente().getFirmaUtente());
			int keyLoc = this.updateInvColl(currentForm.getRecInv(), currentForm.getRecColl(), currentForm.getTipoOperazione(),
					currentForm.getTicket());
			currentForm.setKeyLoc(keyLoc);

			if (currentForm.getRecColl() != null){
				if (currentForm.getRecColl().getMsg() != null){
					if (!currentForm.getRecColl().getMsg().equals("")){
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.error.documentofisico.numPezziDisponibiliMinoreDiNumeroPezziDaRiservare", currentForm.getRecColl().getSerieIncompleta(), currentForm.getRecColl().getNumMancanti()));

						this.saveToken(request);
					}
				}
			}
			if (keyLoc < 0) {

				if (keyLoc == -1) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreCreazioneCollocazione"));

					this.saveToken(request);
				}else if (keyLoc == -2) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreCreazioneCollocazione"));

					this.saveToken(request);
				}
				return mapping.getInputForward();
			} else {
				//almaviva5_20150109 #5692 al rientro in mappa la coll. non è impostata
				if (currentForm.getRecColl() != null)
					currentForm.getRecColl().setKeyColloc(keyLoc);
				//
				if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {//key_loc = 0
					if (currentForm.getIndex() < ValidazioneDati.size(currentForm.getListaInvCreati()) ) {
						InventarioVO recInv1 = (InventarioVO)currentForm.getListaInvCreati().get(currentForm.getIndex());
						InventarioVO recInv = this.getInventario(recInv1.getCodPolo(), recInv1.getCodBib(),
								recInv1.getCodSerie(), recInv1.getCodInvent(), this.getLocale(request, Constants.SBN_LOCALE),
								currentForm.getTicket());
						if (recInv != null) {
							//???
							if (currentForm.isListaInv()){
								CollocazioneDettaglioVO recColl = this.getCollocazioneDettaglio(recInv.getKeyLoc(), currentForm.getTicket());
								if (recColl != null){
									currentForm.setRecColl(recColl);
								}else{
									throw new ValidationException("noColl", ValidationException.errore);
								}
							}
							//
							if (recInv.getCodProven() != null){
								currentForm.setDescrProven(recInv.getDescrProven());
							}
							currentForm.setRecInv(recInv);
						}else{
							throw new ValidationException("ricercaNoInv", ValidationException.errore);
						}
						currentForm.setRecInv(recInv);
						currentForm.setDisableCodTipOrd(true);
						currentForm.setDisableDataIns(true);
						//
						recInv.setSeqColl(appoInv.getSeqColl());
						recInv.setCodMatInv(appoInv.getCodMatInv());
						recInv.setCodTipoOrd(appoInv.getCodTipoOrd());

						recInv.setTipoAcquisizione(appoInv.getTipoAcquisizione());
						recInv.setDescrProven(appoInv.getDescrProven());
						recInv.setPrecInv(appoInv.getPrecInv());
						recInv.setCodFrui(appoInv.getCodFrui());
						recInv.setStatoConser(appoInv.getStatoConser());
						recInv.setCodNoDisp(appoInv.getCodNoDisp());
						recInv.setValore(appoInv.getValore());
						recInv.setImporto(appoInv.getImporto());
						recInv.setCodRiproducibilita(appoInv.getCodRiproducibilita());
						//
						currentForm.setListaNoteDinamica(new ArrayList<CodiceNotaVO>());
						//
						currentForm.setFolder("tab1");
						this.loadTab1(form, recInv);
						// 						this.loadTab1(form);
						currentForm.setRecInv(recInv);
						currentForm.setDisable(true);
						currentForm.setSessione(true);
						// 						//inserire quì la richiesta di stampa etichette on line
						// 						if (currentForm.isStampaEtich()){
						// 							//	 						if (currentForm.getIndex() > 0){
						// 							EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
						// 							datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
						// 							datiStampaEtichette.setCollocazione("");
						// 							datiStampaEtichette.setInventario(new Integer(currentForm.getRecInv().getCodInvent()).toString());
						// 							datiStampaEtichette.setSequenza("");
						// 							datiStampaEtichette.setSerie(currentForm.getRecInv().getCodSerie());
						// 							datiStampaEtichette.setSezione("");
						// 							datiStampaEtichette.setSpecificazione("");
						// 							currentForm.getListaEtichetteBarcode().add(datiStampaEtichette);
						// 						}
						//	 					}
						currentForm.setIndex(currentForm.getIndex()+1);
						//
						return mapping.getInputForward();
					}else{
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ordineCompletamenteRicevuto"));

						currentForm.setConferma(true);

						return mapping.getInputForward();
					}
				}else if (ValidazioneDati.in(currentForm.getProv(), "insColl", "inviaInIndice") ) {//key_loc > 0
					//rileggo i'inventario salvato per prendere tsVar e uteVar
					InventarioVO recInv =null;
					recInv = this.getInventario(currentForm.getCodPolo(), currentForm.getCodBib(),
							currentForm.getCodSerie(), currentForm.getCodInvent(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket());
					if (recInv != null) {
						currentForm.getRecInv().setDataAgg(recInv.getDataAgg());
						currentForm.getRecInv().setUteAgg(recInv.getUteAgg());
					}else{
						throw new ValidationException("ricercaNoInv", ValidationException.errore);
					}

					//
					AreaDatiVariazioneReturnVO advr = null;
					if (!navi.getUtente().getCodBib().equals(currentForm.getCodBib())){
						currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBid(), currentForm.getTicket(), form));
					}
					if (currentForm.getReticolo() != null) {
						advr = localizzaReticolo(currentForm, request);
					}
					if (advr != null && (advr.getCodErr().equals("9999") || ValidazioneDati.isFilled(advr.getTestoProtocollo()) )) {
						LinkableTagUtils.addError(request,  new ActionMessage("errors.gestioneBibliografica.testoProtocollo", advr.getTestoProtocollo()));
						//						return mapping.findForward("listaInventariDelTitolo");
					} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
						if (!advr.getCodErr().equals("7017")){
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + advr.getCodErr()));
						}
					}
					//
					request.setAttribute("codSerie", currentForm.getCodSerie());
					request.setAttribute("codInv", String.valueOf(currentForm.getCodInvent()));
					//per parare casi in cui, ritornando da SinteticaLocalizzazioni, non esistono localizzazioni
					if (request.getAttribute("diagnostico") != null){
						LinkableTagUtils.addError(request, new ActionMessage("" + request.getAttribute("diagnostico") + " nella biblioteca " + currentForm.getCodPolo() + " " + currentForm.getCodBib()));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
					//fine per parare casi
					//provengo da bottone Aggiorna...in indice e da Si a conferma
					if (currentForm.isAbilitaBottoneInviaInIndice()){
						if (currentForm.getTasto() != null && currentForm.getTasto().equals("aggiornaInIndice")){
							try{
								currentForm.setTasto(null);
								currentForm.setProv(null);
								AreePassaggioSifVO areePassaggioSifVO = new AreePassaggioSifVO();
								String uri = null;
								String codDigitalizzazione = null;
								String descrDigitalizzazione = null;
								//findForward a gestione bibliografica
								areePassaggioSifVO.setOggettoDaRicercare(currentForm.getBid());
								areePassaggioSifVO.setDescOggettoDaRicercare(currentForm.getIsbd());
								areePassaggioSifVO.setNaturaOggetto("M");
								areePassaggioSifVO.setTipMatOggetto("M");
								areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
								areePassaggioSifVO.setVisualCall(false);

								// 								UserVO utente = Navigation.getInstance(request).getUtente();
								// 								areePassaggioSifVO.setCodBiblioteca(utente.getCodPolo().toString() + utente.getCodBib().toString());
								areePassaggioSifVO.setCodBiblioteca(currentForm.getCodPolo().toString() + currentForm.getCodBib().toString());

								InventarioVO inventario = this.getInventario(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getCodSerie(),
										currentForm.getCodInvent(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket());
								if (inventario != null){
									uri = inventario.getIdAccessoRemoto().trim().replaceAll("\\s+", "");//tolgo tutti gli spazi anche quelli intermedi
									//
									descrDigitalizzazione = CodiciProvider.cercaDescrizioneCodice(String.valueOf(inventario.getDigitalizzazione().trim()),
											CodiciType.CODICE_TIPO_DIGITALIZZAZIONE,
											CodiciRicercaType.RICERCA_CODICE_SBN);
									//
									if (descrDigitalizzazione == null){
										codDigitalizzazione = "";
										descrDigitalizzazione = "";
									}else{
										codDigitalizzazione = inventario.getDigitalizzazione().trim();
										descrDigitalizzazione = descrDigitalizzazione.trim().replaceAll("\\s+", "");
									}
									areePassaggioSifVO.setUriPolo(uri);
									areePassaggioSifVO.setCodTipoDigitPolo(codDigitalizzazione);
									areePassaggioSifVO.setDescTipoDigitPolo(descrDigitalizzazione);

								}else{
									throw new ValidationException("erroreInventarioNull", ValidationException.errore);
								}
								//
								TreeElementViewTitoli titoloNotiziaCorrente = (TreeElementViewTitoli)currentForm.getReticolo().getAnalitica().findElement(inventario.getBid());
								//
								if (titoloNotiziaCorrente.isFlagCondiviso()){
									//polo = true, indice = true
									areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
								}else{
									//polo = true, indice = false
									areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								}
								request.setAttribute("consIndice", "modificaInventario");
								request.setAttribute("areePassaggioSifVO", areePassaggioSifVO);
								return mapping.findForward("sinteticaLocalizzazioni");

							}	catch (ValidationException e) { // altri tipi di errore
								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

								currentForm.setConferma(false);
								return mapping.getInputForward();
							}	catch (DataException e) { // altri tipi di errore
								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

								currentForm.setConferma(false);
								return mapping.getInputForward();
							}	catch (Exception e) { // altri tipi di errore
								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

								currentForm.setConferma(false);
								return mapping.getInputForward();
							}

						}else{
							currentForm.setTasto("");
						}
					}
					//provengo dal bottone Esemplare e da Si a conferma
					currentForm.getRecColl().setKeyColloc(keyLoc);
					//per parare casi in cui, ritornando da SinteticaLocalizzazioni, non esistono localizzazioni
					if (request.getAttribute("diagnostico") != null){
						LinkableTagUtils.addError(request, new ActionMessage("" + request.getAttribute("diagnostico") + " nella biblioteca " + currentForm.getCodPolo() + " " + currentForm.getCodBib()));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
					//fine per parare casi
					if ((currentForm.getTasto() != null && (!currentForm.getTasto().equals("aggiornaInIndice"))
							||( !currentForm.getProv().equals(null)))){
						currentForm.setTasto(null);
						if (currentForm.getRecColl().getKeyColloc() != 0){
							request.setAttribute("codBib", currentForm.getCodBib());
							request.setAttribute("descrBib", currentForm.getDescrBib());
							request.setAttribute("bid", currentForm.getBid());
							request.setAttribute("titolo", currentForm.getIsbd());
							CollocazioneDettaglioVO recColl = this.getCollocazioneDettaglio(currentForm.getKeyLoc(), currentForm.getTicket());
							request.setAttribute("recColl", recColl);
							request.setAttribute("reticolo", currentForm.getReticolo());

							currentForm.setProv(null);
							if (recColl.getCodBibDoc() != null){
								request.setAttribute("modifica", "modifica");
								recColl.setBidDocDescr("");
								return mapping.findForward("modificaEsemplare");
							}else{
								return mapping.findForward("ricercaEsemplare");
							}
						}else{
							request.setAttribute("codBib", currentForm.getCodBib());
							request.setAttribute("descrBib", currentForm.getDescrBib());
							request.setAttribute("bid", currentForm.getBid());
							request.setAttribute("titolo", currentForm.getIsbd());
							CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(currentForm.getRecColl().getKeyColloc(), currentForm.getTicket());
							if (coll != null){
								request.setAttribute("reticolo", currentForm.getReticolo());
								request.setAttribute("recColl", coll);
								if (coll.getCodBibDoc() != null){
									request.setAttribute("modifica", "modifica");
									coll.setBidDocDescr("");
									return mapping.findForward("modificaEsemplare");
								}else{
									return mapping.findForward("ricercaEsemplare");
								}
							}else{
								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCollocazioneInesistente"));

								return mapping.getInputForward();
							}
						}
					}else{
						currentForm.setTasto(null);
					}
				}
				AreaDatiVariazioneReturnVO advr = null;
				if (keyLoc > 0){
					if (currentForm.getReticolo() != null) {
						//almaviva5_20140703 #5592, #5598
						advr = localizzaReticolo(currentForm, request);
					}
				}


				request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
				request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
				String chiamante = navi.getActionCaller();
				if (chiamante.toUpperCase().indexOf("INSERIMENTOCOLL") > -1 || chiamante.toUpperCase().indexOf("INSERIMENTOINV") > -1) {
					if (advr != null && (advr.getCodErr().equals("9999") || ValidazioneDati.isFilled(advr.getTestoProtocollo()) )) {
						LinkableTagUtils.addError(request,  new ActionMessage("errors.gestioneBibliografica.testoProtocollo", advr.getTestoProtocollo()));
						return mapping.findForward("listaInventariDelTitolo");
					} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
						if (!advr.getCodErr().equals("7017")){
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + advr.getCodErr()));
							return mapping.findForward("listaInventariDelTitolo");
						}
					}
					if (currentForm.isStampaEtich()){
						request.setAttribute("prov", "insColl");
						request.setAttribute("findForward", "listaInventariDelTitolo");
						//
						List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
						EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
						datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
						datiStampaEtichette.setCollocazione(currentForm.getRecColl().getCodColloc().trim());
						datiStampaEtichette.setInventario(new Integer(currentForm.getCodInvent()).toString());
						datiStampaEtichette.setSequenza(currentForm.getRecInv().getSeqColl().trim());
						datiStampaEtichette.setSerie(currentForm.getCodSerie());
						datiStampaEtichette.setSezione(currentForm.getRecColl().getCodSez().trim());
						datiStampaEtichette.setSpecificazione(currentForm.getRecColl().getSpecColloc().trim());
						//
						listaEtichetteBarcode.add(datiStampaEtichette);
						request.setAttribute("codBib", currentForm.getCodBib());
						request.setAttribute("descrBib",currentForm.getDescrBib());
						request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
						request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
						request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
						//
						return  mapping.findForward("stampaSintetica");
					}
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

					this.saveToken(request);
					if (ValidazioneDati.in(request.getAttribute("prov"), "CV") ) {
						navi.setDescrizioneX("Inserimento Collocazione Veloce");
						navi.setTesto("Inserimento Collocazione Veloce");
						request.setAttribute("prov", "fineCV");
						request.setAttribute("bid", currentForm.getBid());
						request.setAttribute("desc", currentForm.getIsbdCollocazione());
						navi.purgeThis();
						return mapping.findForward("listaInventariDelTitolo");
					}
					return mapping.findForward("listaInventariDelTitolo");
				}
			}
			request.setAttribute("keyLoc", keyLoc);
			request.setAttribute("codBib", currentForm.getRecInv().getCodBib());
			request.setAttribute("descrBib", currentForm.getDescrBib());
			request.setAttribute("codSerie", currentForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent", currentForm.getRecInv().getCodInvent());
			String chiamante = navi.getActionCaller();
			if (chiamante.toUpperCase().indexOf("ESAMINAINV") > -1 || chiamante.toUpperCase().indexOf("MODIFICACOLL") > -1) {
				if (currentForm.isStampaEtich()){
					if (chiamante.toUpperCase().indexOf("ESAMINAINV") > -1 || chiamante.toUpperCase().indexOf("MODIFICACOLL") > -1) {
						request.setAttribute("prov", chiamante);
						request.setAttribute("findForward", "back");
					}
					//
					List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
					EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
					datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
					datiStampaEtichette.setCollocazione(currentForm.getRecColl().getCodColloc().trim());
					datiStampaEtichette.setInventario(new Integer(currentForm.getCodInvent()).toString());
					datiStampaEtichette.setSequenza(currentForm.getRecInv().getSeqColl().trim());
					datiStampaEtichette.setSerie(currentForm.getCodSerie());
					datiStampaEtichette.setSezione(currentForm.getRecColl().getCodSez().trim());
					datiStampaEtichette.setSpecificazione(currentForm.getRecColl().getSpecColloc().trim());
					//
					listaEtichetteBarcode.add(datiStampaEtichette);
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("descrBib",currentForm.getDescrBib());
					request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
					request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
					request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
					//
					return  mapping.findForward("stampaSintetica");
				}
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

				this.saveToken(request);
				if (currentForm.getRecInv().getCodScarico() != null && !currentForm.getRecInv().getCodScarico().trim().equals("")){
					request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
					request.setAttribute("descrBib",currentForm.getDescrBib());
					request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("desc", currentForm.getIsbd());
					return mapping.findForward("listaInventariDelTitolo");
				}else{
					return navi.goBack();
				}
			}

			if (currentForm.isStampaEtich()){
				if (currentForm.isStampaEtich() && !currentForm.isDisablePerModInvDaNav()){
					request.setAttribute("prov", chiamante);
					request.setAttribute("findForward", "back");
					List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
					EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
					datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
					datiStampaEtichette.setCollocazione(currentForm.getRecColl().getCodColloc().trim());
					datiStampaEtichette.setInventario(new Integer(currentForm.getCodInvent()).toString());
					datiStampaEtichette.setSequenza(currentForm.getRecInv().getSeqColl().trim());
					datiStampaEtichette.setSerie(currentForm.getCodSerie());
					datiStampaEtichette.setSezione(currentForm.getRecColl().getCodSez().trim());
					datiStampaEtichette.setSpecificazione(currentForm.getRecColl().getSpecColloc().trim());
					currentForm.setDisableTastoAggiornaInIndice(true);
					currentForm.setDisableTastoSalva(true);
					currentForm.setDisableTastoEsemplare(true);
					currentForm.setDisableTastoCancInv(true);
					currentForm.setDisableTastoProven(true);
					currentForm.setDisablePerModInvDaNav(true);
					if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
						navi.purgeThis();
					}
					//
					listaEtichetteBarcode.add(datiStampaEtichette);
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("descrBib",currentForm.getDescrBib());
					request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
					request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
					request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
					//
					return  mapping.findForward("stampaSintetica");
				}
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
				navi.setDescrizioneX("Inserimento Collocazione Veloce");
				navi.setTesto("Inserimento Collocazione Veloce");
				request.setAttribute("prov", "fineCV");
				navi.purgeThis();
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("desc", currentForm.getIsbdCollocazione());
				return mapping.findForward("listaInventariDelTitolo");
			}
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
			return navi.goBack();

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			if (e.getMessage().equals("numeroRiservatoAIntervalloMiscellanea")){
				return navi.goBack(true);
			}else{
				return mapping.getInputForward();
			}
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			if (request.getAttribute("prov") != null &&
					(request.getAttribute("prov").equals("CV"))){
				Navigation navigation =navi;
				navigation.setDescrizioneX("Inserimento Collocazione Veloce");
				navigation.setTesto("Inserimento Collocazione Veloce");
				return navi.goBack(true);
			}else{
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * ModificaInvCollAction.java
	 * controlloDatiPeriodico
	 * void
	 * @param myForm
	 * @throws ValidationException
	 *
	 *
	 */
	private void controlloDatiPeriodico(ModificaInvCollForm currentForm)
	throws Exception {
		//	throws ValidationException {
		//validazione campo valore
		if (!ValidazioneDati.strIsNull((currentForm.getRecInv().getValore()))){
			if (String.valueOf(currentForm.getRecInv().getValore()).trim().equals("0.000") ||
					String.valueOf(currentForm.getRecInv().getValore()).trim().equals("0") ) {
				throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
			}
			if (!ValidazioneDati.strIsNull((currentForm.getRecInv().getValore()))) {
				if ((currentForm.getRecInv().getValore()).length()>14 ) {
					throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
				}
				if (currentForm.getRecInv().getValoreDouble() > ServiziConstant.MAX_VALORE){
					throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
				}
			}
			if (currentForm.getRecInv().getValoreDouble()==0) {
				throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
			}
		}else{
			throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
		}
		//fine validazione campo valore
		if (currentForm.isPeriodico()){
			if (currentForm.getAppoggioCampoObb() != null && currentForm.getAppoggioCampoObb().equals("0,00")){
				//fase di inserimento: il valore è arrivato a 0,00 e poi è stato valorizzato
				if (!currentForm.getRecInv().getValore().equals("0,00") && currentForm.getRecInv().getAnnoAbb().trim().equals("")){
					currentForm.setAppoggioCampoObb(currentForm.getRecInv().getValore());
					throw new ValidationException("avvisoInInserimentoAnnoPeriodicoNonIndicato", ValidationException.errore);
				}
			}else{
				if (!currentForm.getAppoggioCampoObb().equals("0,00")){
					//sono in modifica
					if (currentForm.getAppoggioCampoObb().equals("annoDiversoDaZero") && currentForm.getRecInv().getAnnoAbb().trim().equals("")){
						//il campo anno abb è arrivato dal db valorizzato
						//ed è stato tolto
						currentForm.setAppoggioCampoObb("");
						throw new ValidationException("avvisoInInserimentoAnnoPeriodicoNonIndicato", ValidationException.errore);
					}
				}
			}
			if (currentForm.getRecInv().getAnnoAbb() != null && !currentForm.getRecInv().getAnnoAbb().trim().equals("")){
				if (!ValidazioneDati.strIsNumeric(currentForm.getRecInv().getAnnoAbb().trim())){
					throw new ValidationException("annoAbbNumerico", ValidationException.errore);
				}
				if (!currentForm.getRecInv().getAnnoAbb().equals("0") &&
						(currentForm.getRecInv().getAnnoAbb().trim().length() < 4 || currentForm.getRecInv().getAnnoAbb().equals("0000"))){
					throw new ValidationException("annoAbbNonValido", ValidationException.errore);
				}
			}else{
				if (currentForm.getRecInv().getAnnoAbb() == null){
					throw new ValidationException("annoAbbNull", ValidationException.errore);
				}else if (currentForm.getRecInv().getAnnoAbb().trim().equals("")){
					currentForm.getRecInv().setAnnoAbb("0");
					currentForm.getRecInv().setNumVol("0");
				}
			}
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		try {
			Navigation navi = Navigation.getInstance(request);
			if (ValidazioneDati.in(currentForm.getProv(), "visualizzazionePerCV")
					&& currentForm.isDisablePerModInvDaNav()){
				request.setAttribute("prov", "fineCV");
				//				return Navigation.getInstance(request).goBack(true);
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("desc", currentForm.getIsbdCollocazione());
				navi.purgeThis();

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
				return mapping.findForward("listaInventariDelTitolo");
			}
			//			if (currentForm.isDisablePerModInvDaNav()){
			//				Navigation.getInstance(request).purgeThis();
			//
			//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
			//				return Navigation.getInstance(request).goBack();
			//			}
			request.setAttribute("prov","provChiudi");

			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
			//			return mapping.findForward("cancInv");
			String chiamante = navi.getActionCaller();
			if (currentForm.getCodColloc() != null && currentForm.getCodColloc().endsWith("...") ||
					currentForm.getSpecColloc()  != null && currentForm.getSpecColloc().endsWith("...")){
				return mapping.findForward("listaInventariDelTitolo");
			}

			if (chiamante.toUpperCase().indexOf("INSERIMENTOCOLL") > -1) {
				//
				//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.aggEff"));
				//
				//				this.saveToken(request);
				return mapping.findForward("listaInventariDelTitolo");
			}
			if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
				return navi.goBack();
			}
			NavigationCache cache = navi.getCache();
			NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
			if (prev != null && prev.getForm() instanceof ListeInventariForm) {
				ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
				request.setAttribute("tipoLista", listaInvForm.getTipoLista());
				request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
				request.setAttribute("codBib", listaInvForm.getCodBib());
				request.setAttribute("descrBib", listaInvForm.getDescrBib());
				return navi.goBack();
			}
			return navi.goBack(true);
			//			return Navigation.getInstance(request).goBack(true);

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoProven(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!currentForm.isSessione())
				{
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//			this.checkForm(request);
			resetToken(request);

			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			// evolutiva Mail Carla del 16.06.2017 - nuova gestione del cartiglio relativo alla Provenienza:
			// se nel campo Provenienza si inserisce una parola/stringa la pressione del cartiglio attiva una ricerca filtrata
			// per stringa inserita altrimenti se il campo rimane vuoto la ricerca rimane uguale a quella attuale.
			request.setAttribute("filtroProvenienza",currentForm.getDescrProven());

			return mapping.findForward("lenteProven");

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}


	public ActionForward tab1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			return mapping.getInputForward();
		}
		currentForm.setFolder("tab1");
		this.loadTab1(form, currentForm.getRecInv());
		//		this.loadTab1(form);

		return mapping.getInputForward();
	}

	public ActionForward tab11(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		currentForm.setFolder("tab11");
		this.loadTab11(currentForm.getRecInv(), form);

		return mapping.getInputForward();
	}
	public ActionForward tab2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		currentForm.setFolder("tab2");
		Navigation navi = Navigation.getInstance(request);
		try{
			this.loadTab2(currentForm.getRecInv(), form, request, mapping);
		} catch (ValidationException e) {

			if (e.getMessage() != null && e.getMessage().equals("assenzaRisultati")){
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.FornitoreNonTrovato"));

				this.saveToken(request);

				request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("desc", currentForm.getIsbd());

				String chiamante = navi.getActionCaller();
				if (chiamante.toUpperCase().indexOf("ORDINE") > -1) {
					return mapping.getInputForward();
				}

				return mapping.findForward("listaInventariDelTitolo");

			}
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return navi.goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			//			return mapping.getInputForward();
			return navi.goBack(true);
		}

		return mapping.getInputForward();
	}

	public ActionForward tab3(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		currentForm.setFolder("tab3");
		this.loadTab3(currentForm.getRecInv(), form);

		return mapping.getInputForward();
	}

	public ActionForward cancInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		try {



			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaCancellazione"));

			this.saveToken(request);
			currentForm.setConferma(true);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward esEsempl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		try{
			//ci passo solo in inserimento collocazione

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaSalva"));

			this.saveToken(request);
			currentForm.setConferma(true);
			if (currentForm.isAbilitaBottoneInviaInIndice()){
				currentForm.setProv("inviaInIndice");
			}else{
				currentForm.setProv("insColl");
			}
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	private void loadTab1(ActionForm form, InventarioVO inv) throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		if (!currentForm.isSessione())
			return;

		CaricamentoCombo caricaCombo = new CaricamentoCombo();
		//queste stesse combo vengono richiamate anche nella Action VaiAInserimentoColl per la costruzione della pagina in collocazione veloce
		currentForm.setListaCodiciNote(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_NOTE_CODIFICATE_INVENTARIO,true)));
		currentForm.setListaTipoFruizione(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE,true)));
		currentForm.setListaMatCons(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,true)));
		currentForm.setListaStatoCons(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));
		currentForm.setListaRiproducibilita(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM,true)));//CRIP
		currentForm.setListaSupportoCopia(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_COPIA,true)));//CSUP
		//			currentForm.setListaSupportoCopia(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE)));
		currentForm.setListaTipoOrdine(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,true)));
		currentForm.setListaCodNoDispo(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_NON_DISPONIBILITA,true)));

		String catFruiDefault = currentForm.getCatFruiDefault();
		if (ValidazioneDati.isFilled(catFruiDefault) )
			if (!ValidazioneDati.isFilled(inv.getCodFrui()) )
				inv.setCodFrui(catFruiDefault != null ? catFruiDefault: " ");


		if (!ValidazioneDati.isFilled(inv.getCodMatInv()) )
			inv.setCodMatInv("VM");
		else
			inv.setCodMatInv(inv.getCodMatInv().trim());

/*
		//almaviva5_20150909 il default viene trattato nel metodo controllaDefaultInv()
		String tipoAcqDefault = currentForm.getTipoAcqDefault();
		if (ValidazioneDati.isFilled(tipoAcqDefault) ) {
			//default valorizzato
			if (!ValidazioneDati.isFilled(inv.getTipoAcquisizione()) )
				inv.setCodTipoOrd(tipoAcqDefault != null ? tipoAcqDefault : " ");
			if (!ValidazioneDati.isFilled(inv.getCodTipoOrd()) )
				inv.setTipoAcquisizione(tipoAcqDefault != null ? tipoAcqDefault : " ");
		}
*/
		if (!ValidazioneDati.isFilled(currentForm.getCodNoDispDefault()) )
			inv.setCodNoDisp(inv.getCodNoDisp().trim());
		else
			if (!ValidazioneDati.isFilled(inv.getCodNoDisp()) )
				inv.setCodNoDisp(currentForm.getCodNoDispDefault() != null ? currentForm.getCodNoDispDefault() : " ");

		if (!ValidazioneDati.isFilled(currentForm.getCodStatoConsDefault()) )
			inv.setStatoConser(inv.getStatoConser().trim());
		else
			if (!ValidazioneDati.isFilled(inv.getStatoConser()) )
				inv.setStatoConser(currentForm.getCodStatoConsDefault() != null ? currentForm.getCodStatoConsDefault() : " ");


		if (!ValidazioneDati.isFilled(inv.getCodProven()) && !ValidazioneDati.isFilled(inv.getDescrProven()) )
			if (!ValidazioneDati.isFilled(inv.getCodTipoOrd()) )
				inv.setDescrProven(currentForm.getDescrProvDefault() != null ? currentForm.getDescrProvDefault() : "");

		//almaviva5_20081126
		String codRiproducibilita = inv.getCodRiproducibilita();
		inv.setCodRiproducibilita(ValidazioneDati.isFilled(codRiproducibilita) ? codRiproducibilita.trim() : " ");
		String supportoCopia = inv.getSupportoCopia();
		inv.setSupportoCopia(ValidazioneDati.isFilled(supportoCopia) ? supportoCopia.trim() : " ");
		if (!inv.getCodOrd().equals("")){
			currentForm.setDisableCodTipOrd(true);
			if (inv.getTipoAcquisizione().trim().equals("")){
				inv.setTipoAcquisizione(inv.getCodTipoOrd());
			}
		}

	}

	private void loadTab11(InventarioVO recInv, ActionForm form) throws Exception {
		//		scarico inventariale
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		CaricamentoCombo caricaCombo = new CaricamentoCombo();
		currentForm.setListaDispDaRemoto(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_DISP_ACCESSO_REMOTO,true)));
		currentForm.setListaTecaDigitale(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TECHE_DIGITALI,true)));
		currentForm.setListaTipoDigit(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE,true)));

		String codTipoDigital = currentForm.getRecInv().getDigitalizzazione();
		currentForm.getRecInv().setDigitalizzazione(ValidazioneDati.isFilled(codTipoDigital) ? codTipoDigital : " ");
		String codDispDaRemoto = currentForm.getRecInv().getDispDaRemoto();
		currentForm.getRecInv().setDispDaRemoto(ValidazioneDati.isFilled(codDispDaRemoto) ? codDispDaRemoto.trim() : " ");
		String codTecaDigit = currentForm.getRecInv().getRifTecaDigitale();
		currentForm.getRecInv().setRifTecaDigitale(ValidazioneDati.isFilled(codTecaDigit) ? codTecaDigit.trim() : " ");
		String idAccessoRemoto = currentForm.getRecInv().getIdAccessoRemoto();
		currentForm.getRecInv().setIdAccessoRemoto(ValidazioneDati.isFilled(idAccessoRemoto) ? idAccessoRemoto.trim() : "");

	}
	private void loadTab2(InventarioVO recInv, ActionForm form, HttpServletRequest request, ActionMapping mapping) throws Exception {
		//		carico inventariale / fattura
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;

		currentForm.setOrdine(false);
		if (!recInv.getCodOrd().equals("")){
			currentForm.getRecInv().setCodBibO(recInv.getCodBibO());
			//			currentForm.getRecInv().setCodTipoOrd(recInv.getCodTipoOrd());
			currentForm.getRecInv().setTipoAcquisizione(recInv.getTipoAcquisizione());
			currentForm.getRecInv().setCodTipoOrd(recInv.getCodTipoOrd());
			currentForm.getRecInv().setAnnoOrd(recInv.getAnnoOrd());
			currentForm.getRecInv().setCodOrd(recInv.getCodOrd());
			currentForm.setDisableDataFattura(false);
			currentForm.setDisableNumFattura(false);
			if (!recInv.getCodBibF().trim().equals("")){
				currentForm.getRecInv().setNumFattura(currentForm.getNumFattura().trim());
				currentForm.getRecInv().setAnnoFattura(currentForm.getDataFattura().substring(6));//almaviva2 AnnoFatt
				currentForm.getRecInv().setDataFattura(currentForm.getDataFattura().trim());
				currentForm.setDisableDataFattura(true);
				currentForm.setDisableNumFattura(true);
			}
			ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
			ricercaFornitori.setCodFornitore(currentForm.getRecInv().getCodFornitore());
			ricercaFornitori.setTicket(currentForm.getTicket());
			List listaFornitori = null;
			try{
				listaFornitori = this.getRicercaListaFornitori(ricercaFornitori);
			} catch (ValidationException e) {
				throw new ValidationException(e.getMessage());
			}
			if (listaFornitori != null && listaFornitori.size() > 0){
				FornitoreVO fornitore = (FornitoreVO)listaFornitori.get(0);
				currentForm.setDescrFornitore(fornitore.getNomeFornitore().trim());
			}
			currentForm.setOrdine(true);
		}


		//		this.loadMotivoCarico();
		CaricamentoCombo caricaCombo = new CaricamentoCombo();
		currentForm.setListaMotivoCarico(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_MOTIVI_DI_CARICO_INVENTARIALE)));
		if (!recInv.getDataCarico().trim().equals("")){
			if (recInv.getDataCarico().substring(0, 10).equals("31/12/9999") || recInv.getDataCarico().substring(0, 10).equals("01/01/0001")){
				currentForm.getRecInv().setDataCarico("");
			}else{
				currentForm.getRecInv().setDataCarico(recInv.getDataCarico());
			}
		}
		currentForm.getRecInv().setCodCarico(recInv.getCodCarico().trim());
		currentForm.getRecInv().setNumCarico(recInv.getNumCarico());
	}

	private void loadTab3(InventarioVO recInvColl, ActionForm form) throws Exception {
		//		scarico inventariale
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		currentForm.getRecInv().setCodBibS(recInvColl.getCodBibS());
		currentForm.getRecInv().setCodPoloScar(recInvColl.getCodPoloScar());
		//		this.loadMotivoScarico();
		CaricamentoCombo caricaCombo = new CaricamentoCombo();
		currentForm.setListaMotivoScarico(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_MOTIVI_DI_SCARICO_INVENTARIALE)));
		currentForm.getRecInv().setCodScarico(recInvColl.getCodScarico().trim());
		if (recInvColl.getDataScarico().equals("31/12/9999") || recInvColl.getDataScarico().equals("01/01/0001")){
			currentForm.getRecInv().setDataScarico("00/00/0000");
		}
		if (recInvColl.getDataDelibScar().equals("31/12/9999") || recInvColl.getDataDelibScar().equals("01/01/0001")){
			currentForm.getRecInv().setDataDelibScar("00/00/0000");
		}
		currentForm.getRecInv().setDeliberaScarico(recInvColl.getDeliberaScarico().trim());
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		try{
			Navigation navi = Navigation.getInstance(request);
			if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
				//quando finisce il ciclo ed è stampa etichette, va in stampa
				if (currentForm.isStampaEtich()){
					if (currentForm.getListaEtichetteBarcode() != null && currentForm.getListaEtichetteBarcode().size() > 0){
						request.setAttribute("codBib", currentForm.getCodBib());
						request.setAttribute("descrBib",currentForm.getDescrBib());
						request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
						request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getListaEtichetteBarcode());
						request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
						request.setAttribute("prov", "modInvColl");
						request.setAttribute("ordineCompletato", "ordineCompletato");
						return  mapping.findForward("stampaSintetica");
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaInventariDaStampareVuota" + "error.documentofisico.ordineCompletamenteRicevuto"));

						currentForm.setConferma(true);
						return mapping.getInputForward();
					}
				}else{
					request.setAttribute("prov", "modInvColl");
					request.setAttribute("ordineCompletato", "ordineCompletato");
					return navi.goBack(true);
				}
			}else if(ValidazioneDati.in(currentForm.getProv(), "insColl", "modificaInv", "inviaInIndice") ) {
				currentForm.setConferma(false);
				return this.ok(mapping, form, request, response);
			}else{
				//provengo da modificaColl
			}
			//ritorno indietro da SinteticaLocalizzazioni con diagnostico
			if (request.getAttribute("diagnostico") != null){

				LinkableTagUtils.addError(request, new ActionMessage("" + request.getAttribute("diagnostico") + " nella biblioteca " + currentForm.getCodPolo() + " " + currentForm.getCodBib()));

				currentForm.setConferma(false);
				return mapping.getInputForward();
			}
			String tipoOperazione = DocumentoFisicoCostant.C_CANCELLA_INV;
			//cancellazione

			if (!this.deleteInventario(currentForm.getRecInv(), currentForm.getRecColl(),
					tipoOperazione, currentForm.getTicket())){
			}else{
				request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
				request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
				request.setAttribute("invCanc", "invCanc");
				String chiamante = navi.getActionCaller();

				if ((chiamante.toUpperCase().indexOf("INSERIMENTOCOLL") > -1 || chiamante.toUpperCase().indexOf("MODIFICACOLL") > -1)) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

					this.saveToken(request);
					return mapping.findForward("listaInventariDelTitolo");
				}
				NavigationCache cache = navi.getCache();
				NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
				if (prev != null && prev.getForm() instanceof ListeInventariForm) {
					ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
					request.setAttribute("tipoLista", listaInvForm.getTipoLista());
					request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
					request.setAttribute("codBib", listaInvForm.getCodBib());
					request.setAttribute("descrBib", listaInvForm.getDescrBib());
					return navi.goBack();
				}
				return navi.goBack();
				//				return mapping.findForward("listaInventariNonColl");
			}
			//deve ritornare al chiamante:
			//- dal vaiA lista inventari del titolo
			//- lista inventari non collocati
			return mapping.getInputForward();
			//			}
		}
		catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		// Viene settato il token per le transazioni successive
		try {
			currentForm.setConferma(false);
			Navigation navi = Navigation.getInstance(request);
			String chiamante = navi.getActionCaller();
			if (chiamante.toUpperCase().indexOf("INSERIMENTOINV") > -1) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));

				this.saveToken(request);
				if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
					//quando finisce il ciclo ed è stampa etichette, va in stampa
					return navi.goBack(true);

				}
			}else if (chiamante.toUpperCase().indexOf("ORDINE") > -1) {
				if (currentForm.isStampaEtich()){
					if (currentForm.getListaEtichetteBarcode() != null && currentForm.getListaEtichetteBarcode().size() > 0){
						request.setAttribute("codBib", currentForm.getCodBib());
						request.setAttribute("descrBib",currentForm.getDescrBib());
						request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
						request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getListaEtichetteBarcode());
						request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
						request.setAttribute("prov", "modInvColl");
						//						request.setAttribute("ordineCompletato", "ordineCompletato");
						return  mapping.findForward("stampaSintetica");
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaInventariDaStampareVuota" + "error.documentofisico.ordineCompletamenteRicevuto"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
			}

			//			return mapping.findForward("chiudi");
			return navi.goBack();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward etichette(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;

		try {
			List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
			EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
			datiStampaEtichette.setBiblioteca(currentForm.getDescrBib());
			datiStampaEtichette.setSerie(currentForm.getRecInv().getCodSerie());
			datiStampaEtichette.setInventario(new Integer(currentForm.getRecInv().getCodInvent()).toString());
			datiStampaEtichette.setSequenza(currentForm.getRecInv().getSeqColl().trim());
			datiStampaEtichette.setPrecisazione(currentForm.getRecInv().getPrecInv().trim());
			if (currentForm.getRecInv().getKeyLoc() > 0){
				datiStampaEtichette.setCollocazione(currentForm.getCodColloc().trim());
				datiStampaEtichette.setSezione(currentForm.getCodSez().trim());
				datiStampaEtichette.setSpecificazione(currentForm.getSpecLoc().trim());
			}else{
				datiStampaEtichette.setCollocazione("");
				datiStampaEtichette.setSezione("");
				datiStampaEtichette.setSpecificazione("");
			}
			//
			listaEtichetteBarcode.add(datiStampaEtichette);
			request.setAttribute("codBib", currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
			request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
			request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
			//
			return  mapping.findForward("stampaSintetica");

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}


	private int updateInvColl(InventarioVO recInvColl, CollocazioneVO recColl, String tipoOperazione,
			String ticket) throws Exception {
		int keyLoc = 0;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		keyLoc = factory.getGestioneDocumentoFisico().updateInvColl(recInvColl, recColl, tipoOperazione, ticket);
		return keyLoc;
	}

	private boolean deleteInventario(InventarioVO recInvColl, CollocazioneVO recColl, String tipoOperazione, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().deleteInventario(recInvColl, recColl, tipoOperazione, ticket);
		return ret;
	}

	private InventarioVO getInventario(String codPolo, String codBib, String codSerie, int codInv, Locale locale, String ticket) throws Exception {
		InventarioVO recInv = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		recInv = factory.getGestioneDocumentoFisico().getInventario(codPolo, codBib, codSerie, codInv, locale, ticket);
		return recInv;
	}

	private List getTitolo(String bid, String ticket) throws Exception {
		List titolo = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}

	private CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws Exception {
		CollocazioneDettaglioVO coll;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		coll = factory.getGestioneDocumentoFisico().getCollocazioneDettaglio(keyLoc, ticket);
		return coll;
	}

	private AreaDatiVariazioneReturnVO localizzaAuthorityMultipla(
			AreaDatiLocalizzazioniAuthorityMultiplaVO area, String ticket)
			throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		// Evolutiva Google3: Viene creato il nuovo metodo localizzaUnicoXML che effettua la chiamata per localizzazione tramite
		// un unica chiamata ai protocollo di Indice/Polo utilizzando sempre l'area di passaggio AreaDatiLocalizzazioniAuthorityMultiplaVO
		// questo metodo sostituirà localizzaAuthorityMultipla e localizzaAuthority
//		AreaDatiVariazioneReturnVO adv = factory.getGestioneBibliografica().localizzaAuthorityMultipla(area, ticket);
		AreaDatiVariazioneReturnVO adv = factory.getGestioneBibliografica().localizzaUnicoXML(area, ticket);
		return adv;
	}

	private List getRicercaListaFatture(ListaSuppFatturaVO fattura)
	throws Exception   {

		List risultato;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		risultato = factory.getGestioneAcquisizioni().getRicercaListaFatture(fattura);
		return risultato;

	}

	private List getRicercaListaFornitori(ListaSuppFornitoreVO listaFornitori)
	throws Exception   {

		List risultato;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		risultato = factory.getGestioneAcquisizioni().getRicercaListaFornitori(listaFornitori);
		return risultato;

	}

	private ProvenienzaInventarioVO getProvenienza(String codPolo, String codBib, String codProven, String ticket) throws Exception {
		try {
			if (!ValidazioneDati.isFilled(codProven))
				return null;

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ProvenienzaInventarioVO provenienza = factory.getGestioneDocumentoFisico().getProvenienza(codPolo, codBib, codProven, ticket);
			return provenienza;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ActionForward SIFbibl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			request.setAttribute("modificaInvColl", "modificaInvColl");
			return mapping.findForward("SIFbibl");
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	private void checkForm(HttpServletRequest request, ActionForm form, ActionMapping mapping)
	throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		String data = " ";

		InventarioVO recInv = currentForm.getRecInv();
		recInv.setLocale(this.getLocale(request, Constants.SBN_LOCALE));
		recInv.setNumberFormat(ServiziConstant.VALORE_IMPORTO_NUMBER_FORMAT_CONVERTER);
		data = recInv.getValore();
		if (!ValidazioneDati.strIsNull(data) || !ValidazioneDati.strIsAlfabetic(data)) {
			try {
				double datoValore=recInv.getValoreDouble();
				if (datoValore > ServiziConstant.MAX_VALORE){
					throw new ValidationException("validaInvValoreEccedente");
				}
			} catch (ValidationException e) { // altri tipi di errore
				throw new Exception("validaInvValoreEccedente");
			} catch (ParseException e) { // altri tipi di errore
				throw new Exception("erroreCampoNumerico");
			} catch (Exception e) { // altri tipi di errore
				throw new Exception("validaInvValoreObbligatorio");
			}
		}

		//
		data = recInv.getImporto();
		if (data != null ) {
			try {
				double datoImporto=recInv.getImportoDouble();
				if (datoImporto > ServiziConstant.MAX_VALORE){
					throw new ValidationException("validaInvImportoEccedente");
				}
			} catch (ValidationException e) { // altri tipi di errore
				throw new Exception("validaInvImportoEccedente");
			} catch (ParseException e) { // altri tipi di errore
				throw new Exception("erroreCampoNumerico");
			} catch (Exception e) { // altri tipi di errore
				throw new Exception("validaInvImportoObbligatorio");
			}
		}

		if (ValidazioneDati.in(currentForm.getProv(), "ordine", "ordineIns") ) {
			//nessun controllo da ordine
		} else {
			DocumentoFisicoFormTypes formType = DocumentoFisicoFormTypes.getFormType(Navigation.getInstance(request).getCallerForm());
			String dataInsPrimaColl = recInv.getDataInsPrimaColl();
			if (!ValidazioneDati.isFilled(dataInsPrimaColl) && formType != DocumentoFisicoFormTypes.ESAME_INVENTARIO)
				throw new ValidationException("validaInvDataInsPrimaCollObbl");
			if (ValidazioneDati.isFilled(dataInsPrimaColl) && ValidazioneDati.validaData(dataInsPrimaColl) != ValidazioneDati.DATA_OK)
				throw new ValidationException("validaInvDataInsPrimaCollErrata");
		}
	}

	public ActionForward notaIns(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;

		List<NotaInventarioVO> listaNote = currentForm.getRecInv().getListaNote();
		if (listaNote == null){
			listaNote = new ArrayList<NotaInventarioVO>();
			currentForm.getRecInv().setListaNote(listaNote);
		}
		if (currentForm.getListaNoteDinamica() == null){
			currentForm.setListaNoteDinamica(new ArrayList<CodiceNotaVO>());
		}
		currentForm.addListaNoteDinamica(new CodiceNotaVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward notaCanc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;

		if (currentForm.getSelezRadioNota() == null
				|| currentForm.getSelezRadioNota().equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selObblNumStandard"));

			return mapping.getInputForward();
		}

		int index = Integer.parseInt(currentForm.getSelezRadioNota());
		currentForm.getListaNoteDinamica().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		currentForm.setTasto("aggiornaInIndice");
		return this.esEsempl(mapping, currentForm, request, response);

	}

	private DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String ticket, ActionForm form) throws Exception {
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		DatiBibliograficiCollocazioneVO reticolo;
		String bibliotecaOperante = currentForm.getCodPolo() + currentForm.getCodBib();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		reticolo = factory.getGestioneDocumentoFisico().getAnaliticaPerCollocazione(bid, bibliotecaOperante, ticket);
		//		if (reticolo == null)  {
		//			currentForm.setrNoReticolo(true);
		//		}else{
		//			currentForm.setNoReticolo(false);
		//		}
		return reticolo;
	}

	private boolean getSerieNumeroCarico(String codPolo, String codBib, String codSerie, String numCarico, String ticket)
	throws Exception   {
		boolean num;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		num = factory.getGestioneDocumentoFisico().getSerieNumeroCarico(codPolo, codBib, codSerie, numCarico, ticket);
		return num;

	}

	private AreaDatiVariazioneReturnVO localizzaReticolo(ActionForm form, HttpServletRequest request) throws Exception {
		//almaviva5_20140703 #5592, #5598
		ModificaInvCollForm currentForm = (ModificaInvCollForm) form;
		DatiBibliograficiCollocazioneVO reticolo = currentForm.getReticolo();
		AreaDatiLocalizzazioniAuthorityMultiplaVO adlam = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
		TreeElementViewTitoli titoloNotiziaCorrente = (TreeElementViewTitoli)reticolo.getAnalitica().findElement(currentForm.getBid());

		AreaDatiVariazioneReturnVO advr = null;

		CollocazioneTitoloVO[] titoliCollocabiliReticolo = reticolo.getListaTitoliLocalizzazione();
		for (int i = 0; i < titoliCollocabiliReticolo.length; i++) {
			CollocazioneTitoloVO titoloCollocabile = titoliCollocabiliReticolo[i];
			TreeElementViewTitoli tit = (TreeElementViewTitoli)reticolo.getAnalitica().findElement(titoloCollocabile.getBid());

			AreaDatiLocalizzazioniAuthorityVO al = new AreaDatiLocalizzazioniAuthorityVO();
			al.setIndice(titoloNotiziaCorrente.isFlagCondiviso());
			al.setPolo(true);

			if (!ValidazioneDati.in(tit.getLocalizzazione(),
					TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA,
					TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO)) {

				if (tit.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE) {
					al.setTipoLoc("Possesso");
				} else if (ValidazioneDati.in(tit.getLocalizzazione(),
						TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO,
						TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO)) {//aggiunto
					al.setTipoLoc("Possesso");
				}
				al.setIdLoc(titoloCollocabile.getBid());
				al.setNatura(titoloCollocabile.getNatura());
				al.setAuthority("");
				al.setTipoMat(tit.getTipoMateriale());
				al.setTipoOpe("Localizza");
				al.setCodiceSbn(currentForm.getCodPolo()+ currentForm.getCodBib());
				adlam.addListaAreaLocalizVO(al);
			}
		}
		//almaviva5_20140626 with LV #5592, #5598
		//la chiamata a localizzazione multipla va effettuata UNA SOLA VOLTA alla fine del ciclo.
		if (ValidazioneDati.isFilled(adlam.getListaAreaLocalizVO()) )
			advr = this.localizzaAuthorityMultipla(adlam, currentForm.getTicket());

		return advr;

	}
}
