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
package it.iccu.sbn.web.actions.documentofisico.esameCollocazioni;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoNumCollVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneUltKeyLocVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioniUltCollSpecVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocListeCollocazioniForm;
import it.iccu.sbn.web.actionforms.periodici.GestioneFascicoloForm;
import it.iccu.sbn.web.actionforms.periodici.KardexPeriodiciForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class EsameCollocListeCollocazioniAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(EsameCollocListeCollocazioniAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "blocco");
		map.put("documentofisico.bottone.esemplare", "esemplare");
		map.put("documentofisico.bottone.inventari", "inventari");
		map.put("documentofisico.bottone.scegli", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.lsUS", "listaUltSpec");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

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

		//setto il token per le transazioni successive
	this.saveToken(request);

		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
				return mapping.getInputForward();
		// controllo se ho già i dati in sessione;
		if(!currentForm.isSessione()){
			currentForm.setTicket(navi.getUserTicket());
			currentForm.setCodPolo(navi.getUtente().getCodPolo());
			currentForm.setCodBib(navi.getUtente().getCodBib());
			currentForm.setDescrBib(navi.getUtente().getBiblioteca());
			log.info("EsameCollocListeCollocazioniAction::unspecified");
			navi.addBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL);
			loadDefault(request, mapping, form);
			currentForm.setSessione(true);
		}
		String tipoRichiesta = null;
		try{
			if(request.getAttribute("codBib") != null &&
					request.getAttribute("descrBib") != null &&
					request.getAttribute("paramRicerca") != null) {
				currentForm.setCodBib((String)request.getAttribute("codBib"));
				currentForm.setDescrBib((String)request.getAttribute("descrBib"));
				if (currentForm.getParamRicerca() == null){
					currentForm.setParamRicerca((EsameCollocRicercaVO)request.getAttribute("paramRicerca"));
				}
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.errorePassaggioParametri"));

				return navi.goBack(true);
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("posseduto")){
				currentForm.setProv("posseduto");
			}
			//				gestione richiamo lista collocazioni da lente
			if (request.getAttribute("listaCollSupp") != null &&
					request.getAttribute("listaCollSupp").equals("listaCollSupp")){
				currentForm.setRichiamo("lista");
				currentForm.setListaSupportoColl("listaSuppColl");
				currentForm.setAction((String)request.getAttribute("chiamante"));
				tipoRichiesta = "6";
			}
			if (request.getAttribute("listaSpecSupp") != null
					&& request.getAttribute("listaSpecSupp").equals("listaSpecSupp")){
				//					gestione richiamo lista specificazioni da lente
				currentForm.setRichiamo("lista");
				currentForm.setListaSupportoColl("listaSuppSpec");
				currentForm.setAction((String)request.getAttribute("chiamante"));
				tipoRichiesta = "8";
				currentForm.getParamRicerca().setTipoRicerca("8");
			}else if (request.getAttribute("listaUltColl") != null &&
					request.getAttribute("listaUltColl").equals("listaUltColl")){
				//					lista ult collocazioni
				currentForm.setListaSupportoColl("listaUltColl");
				tipoRichiesta = "10";
			}else if (request.getAttribute("listaUltSpec") != null &&
					request.getAttribute("listaUltSpec").equals("listaUltSpec")){
				//					lista ult specificazioni
				currentForm.setListaSupportoColl("listaUltSpec");
//				currentForm.getParamRicerca().setEsattoColl(true);
				tipoRichiesta = "11";
			//almaviva5_20101001 tipo lista per periodici
			} else if (ValidazioneDati.in(request.getAttribute("listaCollEsemplare"), "listaCollEsemplare",
					PeriodiciDelegate.LISTA_COLL_ESEMPL_KARDEX,
					PeriodiciDelegate.LISTA_COLL_ESEMPL_PERIODICI) ) {
				currentForm.getParamRicerca().setCodLoc("");
				currentForm.getParamRicerca().setCodSpec("");
				currentForm.getParamRicerca().setEsattoColl(false);
				currentForm.getParamRicerca().setUltLoc("");
				currentForm.getParamRicerca().setUltSpec("");
				currentForm.getParamRicerca().setOrdLst("CA");
				currentForm.setListaSupportoColl("listaCollEsemplare");
				tipoRichiesta = "12";
				//almaviva5_20101215 periodici
				currentForm.setPeriodici(ValidazioneDati.equals(request.getAttribute("listaCollEsemplare"), PeriodiciDelegate.LISTA_COLL_ESEMPL_PERIODICI));
				currentForm.setKardex(ValidazioneDati.equals(request.getAttribute("listaCollEsemplare"), PeriodiciDelegate.LISTA_COLL_ESEMPL_KARDEX));
			}else if (request.getAttribute("listaColl") != null &&
					request.getAttribute("listaColl").equals("listaColl")){
				//					lista collocazioni
				currentForm.setListaSupportoColl("listaColl");
				if (currentForm.getParamRicerca().isEsattoColl()){
					if (currentForm.getParamRicerca().isEsattoSpec()){
						currentForm.getParamRicerca().setTipoRicerca("4");
						tipoRichiesta = "4";
					}else{
						currentForm.getParamRicerca().setTipoRicerca("2");
						tipoRichiesta = "2";
					}
				}else{
					currentForm.getParamRicerca().setTipoRicerca("0");
					tipoRichiesta = "0";
				}
				if (currentForm.getParamRicerca().getElemPerBlocchi() != 0){
					currentForm.setNRec(currentForm.getParamRicerca().getElemPerBlocchi());
				}
			}
			//calcolo limite per l'estrazione dei records
			//almaviva5_20140408
			int elementiBlocco = currentForm.getNRec();
			int maxRows = CommonConfiguration.getPropertyAsInteger(Configuration.ESAME_COLLOCAZIONI_MAX_RESULT_ROWS, DocumentoFisicoCostant.NUM_REC_LISTA);
			maxRows = ValidazioneDati.max(maxRows, elementiBlocco);
			int limite = maxRows / elementiBlocco;
			if (limite < 1){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreCalcoloLimite"));

				return mapping.getInputForward();
			}

			String tokens[] = String.valueOf(limite).split(",");
			//prendo la pare intera del numero e la moltiplico per currentForm.getNRec()
			//così stabilisco il nuovo limite e lo passo al servizio con paramtri di ricerca
			int intero = Integer.valueOf(tokens[0]);
			int nuovoLimite = intero * elementiBlocco;

			currentForm.setTipoRichiesta(tipoRichiesta);
			currentForm.getParamRicerca().setTipoRicerca(tipoRichiesta);
			currentForm.getParamRicerca().setNuovoLimiteRicerca(nuovoLimite);

			currentForm.getParamRicerca().setCodPolo(currentForm.getCodPolo());
//			//almaviva5_20090420
//			currentForm.getParamRicerca().setOrdLst("CA");
			List<?> listaColl = this.getListaCollocazioniSezione(currentForm.getParamRicerca(),
					tipoRichiesta, currentForm.getTicket(), elementiBlocco, currentForm);

			gestioneListaCollocazioni(mapping, form, request, currentForm, tipoRichiesta, listaColl);

			//almaviva5_20101001 gest. periodici: se ho una sola collocazione di esemplare
			//passo direttamente a lista inventari
			if (navi.bookmarksExist(PeriodiciDelegate.BOOKMARK_KARDEX, PeriodiciDelegate.BOOKMARK_FASCICOLO)
					&& ValidazioneDati.size(listaColl) == 1) {
				navi.purgeThis();
				return inventari(mapping, currentForm, request, response);
			}

		} catch (ValidationException e) {
			if (e.getMsg()!=null){
				if (e.getMsg().equals("msgS")){
					this.saveToken(request);
					request.setAttribute("prov", "msgS");
					return gestioneListaCollocazioni(mapping, form, request, currentForm, tipoRichiesta, e.getLista());
				}
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return navi.goBack(true);
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return navi.goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return navi.goBack(true);
		}
		return mapping.getInputForward();

	}

	public ActionForward gestioneListaCollocazioni(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			EsameCollocListeCollocazioniForm currentForm, String tipoRichiesta,
			List<?> listaColl) throws Exception {
		try{
			if (listaColl == null ||  listaColl.size() <1)  {
				currentForm.setNoColl(true);
				currentForm.setCodBib(currentForm.getCodBib());
				currentForm.setDescrBib(currentForm.getDescrBib());
				currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());


				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collNonTrovate"));

				return Navigation.getInstance(request).goBack(true);
			}else{
				if (listaColl.size() == 1){
					currentForm.setSelectedColl("0");
					currentForm.setSelectedCollSpec("0");
				}
				if (tipoRichiesta.equals("6") || tipoRichiesta.equals("7")){
					currentForm.setListaSupportoColl("listaSuppColl");
					currentForm.setNoColl(false);
					currentForm.setCodBib(currentForm.getCodBib());
					currentForm.setDescrBib(currentForm.getDescrBib());
					currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());
					currentForm.setCodColloc(currentForm.getParamRicerca().getCodLoc().toUpperCase());
					currentForm.setListaCollocSpec(listaColl);
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Collocazioni");
					navigation.setTesto("Lista Collocazioni");
					return mapping.getInputForward();
					//					return mapping.findForward("listaCollSupp");
				}else if (tipoRichiesta.equals("8") || tipoRichiesta.equals("9")){
					currentForm.setListaSupportoSpec("listaSuppSpec");
					currentForm.setNoColl(false);
					currentForm.setCodBib(currentForm.getCodBib());
					currentForm.setDescrBib(currentForm.getDescrBib());
					currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());
					currentForm.setCodColloc(currentForm.getParamRicerca().getCodLoc().toUpperCase());
					currentForm.setListaCollocSpec(listaColl);
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Specificazioni");
					navigation.setTesto("Lista Specificazioni");
					return mapping.getInputForward();
				}else if (tipoRichiesta.equals("10")){
					currentForm.setListaSupportoColl("listaUltColl");
					currentForm.setNoColl(false);
					currentForm.setCodBib(currentForm.getCodBib());
					currentForm.setDescrBib(currentForm.getDescrBib());
					currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());
					currentForm.setCodColloc(currentForm.getParamRicerca().getCodLoc().toUpperCase());
					currentForm.setListaCollocSpec(listaColl);
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Ultime Collocazioni");
					navigation.setTesto("Lista Ultime Collocazioni");
					return mapping.getInputForward();
				}else if (tipoRichiesta.equals("11")){
					currentForm.setListaSupportoColl("listaUltSpec");
					currentForm.setNoColl(false);
					currentForm.setCodBib(currentForm.getCodBib());
					currentForm.setDescrBib(currentForm.getDescrBib());
					currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());
					currentForm.setCodColloc(currentForm.getParamRicerca().getCodLoc().toUpperCase());
					currentForm.setListaCollocSpec(listaColl);
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Ultime Specificazioni");
					navigation.setTesto("Lista Ultime Specificazioni");
					return mapping.getInputForward();
				}else if (tipoRichiesta.equals("12")){
					currentForm.setListaSupportoColl("listaCollEsemplare");
					currentForm.setNoColl(false);
					currentForm.setCodBib(currentForm.getCodBib());
					currentForm.setDescrBib(currentForm.getDescrBib());
					if (currentForm.getProv() != null && currentForm.getProv().equals("posseduto")){
					}else{
						currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());
					}
					currentForm.setCodColloc(currentForm.getParamRicerca().getCodLoc().toUpperCase());
					currentForm.setListaColloc(listaColl);
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Collocazioni di Esemplare");
					navigation.setTesto("Lista Collocazioni di Esemplare");
				}
				currentForm.setNoColl(false);
				currentForm.setCodBib(currentForm.getCodBib());
				currentForm.setDescrBib(currentForm.getDescrBib());
				if (currentForm.getProv() != null && currentForm.getProv().equals("posseduto")){
					currentForm.setTipoOrdinam("I");
				}else{
					currentForm.setCodSezione(currentForm.getParamRicerca().getCodSez().toUpperCase());
					this.loadTipiOrdinamentoInv(request, form);
					//					currentForm.setTipoOrdinamento(((CodiceVO) currentForm.getListaTipiOrdinamento().get(0)).getDescrizione());
					currentForm.setTipoOrdinam(currentForm.getTipoOrdinam());
				}
				currentForm.setListaColloc(listaColl);
			}
		} catch (ValidationException e) {
			if (e.getMsg()!=null){
				if (e.getMsg().equals("msgS")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

					this.saveToken(request);
					request.setAttribute("prov", "msgS");
					return mapping.getInputForward();
				}
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		}
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		try{
		int numBlocco = 0;
		if (currentForm.isCaricoAltriBlocchi()){
			currentForm.setBloccoSelezionato(currentForm.getBloccoSelezionato());
		}
		numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				if (currentForm.getListaSupportoColl() != null && (currentForm.getListaSupportoColl().equals("listaSuppColl") ||
						currentForm.getListaSupportoColl().equals("listaSuppSpec") )){
					if (currentForm.getListaCollocSpec() != null){
						if (numBlocco == currentForm.getTotBlocchi()){
							currentForm.setBloccoSelezionato(numBlocco + 1);
						}
						int ultProg = currentForm.getListaCollocSpec().size();
						CollocazioniUltCollSpecVO ultRec = (CollocazioniUltCollSpecVO)currentForm.getListaCollocSpec().get(ultProg - 1);
						int nuovoPrg =  (bloccoVO.getMaxRighe() * (bloccoVO.getNumBlocco() - 1)) + 1;//ultRec.getPrg() + 1;
						int sizeBlocco = bloccoVO.getLista().size();
						if (sizeBlocco > 0){
							for (int y = 0; y < sizeBlocco; y++) {
								CollocazioniUltCollSpecVO rec = (CollocazioniUltCollSpecVO) bloccoVO.getLista().get(y);
								rec.setPrg(nuovoPrg + y);
							}
						}
						currentForm.getListaCollocSpec().addAll(bloccoVO.getLista());
					}else{
						currentForm.getListaColloc().addAll(bloccoVO.getLista());
					}
				}else if (currentForm.getListaSupportoColl() != null && (currentForm.getListaSupportoColl().equals("listaUltColl"))){

					if (currentForm.getListaCollocSpec() != null ){
						currentForm.getListaCollocSpec().addAll(bloccoVO.getLista());
					}else{
						currentForm.getListaCollocSpec().addAll(bloccoVO.getLista());
					}
				}else if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaUltSpec") ){
					if (currentForm.getListaCollocSpec() != null){
						currentForm.getListaCollocSpec().addAll(bloccoVO.getLista());
					}else{
						currentForm.getListaCollocSpec().addAll(bloccoVO.getLista());
					}
				}else if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaCollEsemplare")){
					currentForm.getListaColloc().addAll(bloccoVO.getLista());
				}else if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaColl")){
					if (numBlocco == currentForm.getTotBlocchi()){
						currentForm.setBloccoSelezionato(numBlocco + 1);
					}
					ricalcolaProgressiviBlocco(currentForm, bloccoVO);
					currentForm.getListaColloc().addAll(bloccoVO.getLista());
				}
			}else{
				if (currentForm.isCaricoAltriBlocchi()){
						currentForm.getListaColloc().addAll(currentForm.getNewLista());
						currentForm.setCaricoAltriBlocchi(false);
						currentForm.setBloccoSelezionato(numBlocco + 1);
						return mapping.getInputForward();
				}else{
					return mapping.getInputForward();
				}
			}
			//
			//quando si chiede di caricare l'ultimo blocco controllo se ci sono altre collocazioni non ancora estratte dal db
			//su un calcolo: currentForm.getNumBlocchi * currentForm.getNumRighe, se il prodotto è < di currentForm.getTotRighe parte la ricerca
			//delle righe rimanenti che verranno appese all'ultimo blocco trattato
			if (numBlocco == currentForm.getTotBlocchi()){
				int totElementi = currentForm.getElemPerBlocchi() * currentForm.getBloccoSelezionato();
				if ((totElementi >= currentForm.getTotRighe())){
					currentForm.setBloccoSelezionato(numBlocco);
					currentForm.setAbilitaBottoneCarBlocchi(false);
					return mapping.getInputForward();
				}
				currentForm.setCaricoAltriBlocchi(true);
				currentForm.getParamRicerca().setUltLoc(currentForm.getUltLoc());
				currentForm.getParamRicerca().setUltOrdLoc(currentForm.getUltOrdLoc());
				currentForm.getParamRicerca().setUltSpec(currentForm.getUltSpec());
				currentForm.getParamRicerca().setUltKeyLoc(Integer.parseInt(currentForm.getUltKeyLoc()));
				if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaColl")){
					if (currentForm.getParamRicerca().isEsattoColl()){
						currentForm.getParamRicerca().setTipoRicerca("3");
						if (currentForm.getParamRicerca().isEsattoSpec()){
							currentForm.getParamRicerca().setTipoRicerca("5");
						}
					}else{
						currentForm.getParamRicerca().setTipoRicerca("1");
					}
				}else if (currentForm.getListaSupportoColl() != null && (currentForm.getListaSupportoColl().equals("listaSuppColl") ||
						currentForm.getListaSupportoColl().equals("listaSuppSpec") )){
					if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaSuppColl")){
						currentForm.getParamRicerca().setTipoRicerca("7");
					}else if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaSuppSpec")){
						currentForm.getParamRicerca().setTipoRicerca("9");
					}

				}
			//
				currentForm.getParamRicerca().setOldIdLista(idLista);
				currentForm.getParamRicerca().setUltimoBlocco(numBlocco);
//				ultProg = currentForm.getListaColloc().size();
				List<?> listaColl = this.getListaCollocazioniSezione(currentForm.getParamRicerca(),
					currentForm.getTipoRichiesta(), currentForm.getTicket(), currentForm.getNRec(), currentForm);
				if (listaColl != null && listaColl.size() > 0){
					currentForm.setCaricoAltriBlocchi(false);
					currentForm.setOldLista(listaColl);
				}
			}
		}
		Collections.sort(currentForm.getListaColloc());
		return mapping.getInputForward();

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.esameCollocazioni
	  * EsameCollocListeCollocazioniAction.java
	  * ricalcolaProgressiviBlocco
	  * void
	  * @param currentForm
	  * @param bloccoVO
	  *
	  *
	 */
	private void ricalcolaProgressiviBlocco(
			EsameCollocListeCollocazioniForm currentForm,
			DescrittoreBloccoVO bloccoVO) {
		//ricalcola i progressivi
		int ultProg = currentForm.getListaColloc().size();
		CollocazioneUltKeyLocVO ultRec = (CollocazioneUltKeyLocVO)currentForm.getListaColloc().get(ultProg - 1);
		int nuovoPrg =  (bloccoVO.getMaxRighe() * (bloccoVO.getNumBlocco() - 1)) + 1;//ultRec.getPrg() + 1;
		int sizeBlocco = bloccoVO.getLista().size();
		if (sizeBlocco > 0){
			for (int y = 0; y < sizeBlocco; y++) {
				CollocazioneUltKeyLocVO rec = (CollocazioneUltKeyLocVO) bloccoVO.getLista().get(y);
				rec.setPrg(nuovoPrg + y);
			}
		}
	}

	public ActionForward inventari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (request.getAttribute("listaColl") != null && request.getAttribute("listaColl").equals("listaColl")){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));

				return mapping.getInputForward();
			}
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSez",currentForm.getCodSezione());
			int collSel;
			request.setAttribute("checkS", currentForm.getSelectedCollSpec());
			String check = currentForm.getSelectedCollSpec();
			if (check != null && check.length() != 0) {
				collSel = Integer.parseInt(currentForm.getSelectedCollSpec());
				CollocazioneUltKeyLocVO scelColl = (CollocazioneUltKeyLocVO) currentForm.getListaColloc().get(collSel);
				EsameCollocRicercaVO paramRicerca = (EsameCollocRicercaVO) currentForm.getParamRicerca().clone();
				paramRicerca.setCodPolo(scelColl.getCodPolo());
				paramRicerca.setCodBib(scelColl.getCodBib());
				paramRicerca.setKeyLoc(scelColl.getKeyColloc());
				paramRicerca.setCodLoc(scelColl.getCodColloc());
				paramRicerca.setCodSpec(scelColl.getSpecColloc());
				paramRicerca.setCodSez(scelColl.getCodSez());
				paramRicerca.setOrdLst(currentForm.getTipoOrdinam());
				request.setAttribute("paramRicerca", paramRicerca);
				String chiamante = Navigation.getInstance(request).getActionCaller();

				//almaviva5_20101001 tipo lista per gest. periodici
				ActionForm prevForm = Navigation.getInstance(request).getCallerForm();
				if (ValidazioneDati.in(prevForm.getClass(), KardexPeriodiciForm.class, GestioneFascicoloForm.class) || currentForm.isPeriodici() ) {
					request.setAttribute("tipoLista", "listaInvDiColloc");
			        request.setAttribute("prov", currentForm.isPeriodici() ? "esamePeriodici" : "fascicolo");
			   	} else
					if (chiamante.toUpperCase().indexOf("ESAMECOLLOCRICERCAESEMPLARE") > -1) {
						request.setAttribute("tipoLista","listaInvDiCollocDaEsempl");
					}else{
						if (currentForm.getProv() != null &&  currentForm.getProv().equals("posseduto")){
							request.setAttribute("prov", "esaminaPosseduto");
						}
						request.setAttribute("tipoLista","listaInvDiColloc");
					}
				return Navigation.getInstance(request).goForward(mapping.findForward("inventari"));
//				return mapping.findForward("inventari");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward esemplare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			String check = currentForm.getSelectedCollSpec();
			if (check != null && check.length() != 0) {
				int collsel = Integer.parseInt(currentForm.getSelectedCollSpec());

				CollocazioneUltKeyLocVO coll = (CollocazioneUltKeyLocVO) currentForm.getListaColloc().get(collsel);
				CollocazioneDettaglioVO recColl = this.getCollocazioneDettaglio(coll.getKeyColloc(), currentForm.getTicket());
				if (recColl != null){
					request.setAttribute("recColl", recColl);
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneInesistente"));

					return mapping.getInputForward();
				}
				if (recColl.getCodBibDoc() != null){
					request.setAttribute("codBib", coll.getCodBib());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					request.setAttribute("bid", coll.getBid());
					request.setAttribute("titolo", coll.getTitolo());
					request.setAttribute("codSerie", "");//è fittizio, ma ci deve stare
					request.setAttribute("codInv", String.valueOf("0"));//è fittizio, ma ci deve stare
					request.setAttribute("reticolo", "esamina");
					request.setAttribute("esamina", "esamina");
					return mapping.findForward("esemplare");
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.esemplareNonEsistente"));

					return mapping.getInputForward();
				}

			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}



		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}

	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			String chiamante = Navigation.getInstance(request).getActionCaller();

			if (chiamante.toUpperCase().indexOf("INSERIMENTOCOLL") > -1) {
				return Navigation.getInstance(request).goBack();
			}else if (currentForm.getListaSupportoColl() != null &&
					currentForm.getListaSupportoColl().equals("listaUltSpec")){
				currentForm.setListaSupportoColl("listaUltColl");
				request.setAttribute("listaUltColl", currentForm.getListaSupportoColl());
				return Navigation.getInstance(request).goBack(true);
			}else if (currentForm.getListaSupportoColl() != null &&
					(currentForm.getListaSupportoColl().equals("listaSuppSpec")
							|| currentForm.getListaSupportoColl().equals("listaSuppColl"))){
				if (currentForm.getListaSupportoColl().equals("listaSuppColl")){
					request.setAttribute("listaSuppColl", currentForm.getListaSupportoColl());
				}else{
					request.setAttribute("listaSuppSpec", currentForm.getListaSupportoColl());
				}
				return Navigation.getInstance(request).goBack(true);
			}
			NavigationCache navi = Navigation.getInstance(request).getCache();
			NavigationElement prev = navi.getElementAt(navi.getCurrentPosition() - 1);
			NavigationElement post = navi.getElementAt(navi.getCurrentPosition() + 1);
			if (prev != null && prev.getName().equals("esameCollocRicercaForm") ){
				return Navigation.getInstance(request).goBack(true);
			}else{
				request.setAttribute("indietro", "indietro");
				currentForm.setParamRicerca(null);
				return Navigation.getInstance(request).goBack();
			}

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		try {

			int collSel;
			request.setAttribute("checkS", currentForm.getSelectedCollSpec());
			String checkC = currentForm.getSelectedCollSpec();
			if (ValidazioneDati.isFilled(checkC)) {
				collSel = Integer.parseInt(currentForm.getSelectedCollSpec());
				//almaviva5_20101215 periodici
				if (currentForm.isPeriodici()) {
					CollocazioneUltKeyLocVO scelColl = (CollocazioneUltKeyLocVO) currentForm.getListaColloc().get(collSel);
					BibliotecaVO bib = new BibliotecaVO();
			        bib.setCod_polo(currentForm.getCodPolo());
			        bib.setCod_bib(currentForm.getCodBib());
			        bib.setNom_biblioteca(currentForm.getDescrBib());
			        SerieCollocazioneVO coll = new SerieCollocazioneVO();
			        coll.setKey_loc(scelColl.getKeyColloc());
			        coll.setBid(scelColl.getBid());
			        coll.setCodSez(scelColl.getCodSez());
			        coll.setCd_loc(scelColl.getCodColloc());
			        coll.setSpec_loc(scelColl.getSpecColloc());
				    return PeriodiciDelegate.getInstance(request).sifKardexPeriodico(bib, coll);
				}

				CollocazioniUltCollSpecVO scelColl = (CollocazioniUltCollSpecVO) currentForm.getListaCollocSpec().get(collSel);
				request.setAttribute("scelColl", scelColl);
				if (currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaSuppColl")){
					request.setAttribute("listaSuppColl", currentForm.getListaSupportoColl());
				}else if(currentForm.getListaSupportoSpec() != null && currentForm.getListaSupportoSpec().equals("listaSuppSpec")){
					request.setAttribute("listaSuppSpec", currentForm.getListaSupportoSpec());
				}else if(currentForm.getListaSupportoColl() != null && currentForm.getListaSupportoColl().equals("listaUltColl")){
					request.setAttribute("listaUltColl", currentForm.getListaSupportoColl());
				}
				return Navigation.getInstance(request).goBack();  //true
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward listaUltSpec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		try {
			if (request.getAttribute("listaUltimeSPecificazioni") != null){
				currentForm.setListaCollocSpec((List<?>)request.getAttribute("listaUltimeSPecificazioni"));
				return mapping.getInputForward();
			}
			int collSel;
			request.setAttribute("checkS", currentForm.getSelectedCollSpec());
			String checkC = currentForm.getSelectedCollSpec();
			if (checkC != null && checkC.length() != 0) {
				collSel = Integer.parseInt(currentForm.getSelectedCollSpec());
				CollocazioniUltCollSpecVO scelColl = (CollocazioniUltCollSpecVO) currentForm.getListaCollocSpec().get(collSel);
				request.setAttribute("scelColl", scelColl);
				//currentForm.setListaSupportoColl("listaUltSpec");

				request.setAttribute("codBib",currentForm.getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());

				EsameCollocListeCollocazioniForm listaUltSpecForm = currentForm.cloneForm();

				listaUltSpecForm.getParamRicerca().setCodPolo(currentForm.getCodPolo());
				listaUltSpecForm.getParamRicerca().setCodBib(currentForm.getCodBib());
				listaUltSpecForm.getParamRicerca().setCodPoloSez(currentForm.getCodPolo());
				listaUltSpecForm.getParamRicerca().setCodBibSez(currentForm.getCodBib());
				listaUltSpecForm.getParamRicerca().setCodSez(currentForm.getCodSezione());
				listaUltSpecForm.getParamRicerca().setCodLoc(scelColl.getCodColloc().trim());
				listaUltSpecForm.getParamRicerca().setCodSpec("");
				listaUltSpecForm.getParamRicerca().setEsattoColl(false);
				listaUltSpecForm.getParamRicerca().setEsattoSpec(false);
				listaUltSpecForm.getParamRicerca().setOrdLst("");
				String tipoRichiesta = "11";
				listaUltSpecForm.getParamRicerca().setTipoRicerca(tipoRichiesta);
				List<?> listaColl = this.getListaCollocazioniSezione(listaUltSpecForm.getParamRicerca(),
						tipoRichiesta, listaUltSpecForm.getTicket(), listaUltSpecForm.getNRec(), listaUltSpecForm);
				Navigation navi = Navigation.getInstance(request);
				if (listaColl == null ||  listaColl.size() <1)  {
					currentForm.setNoColl(true);

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaCollocazioniVuota"));

					return navi.goBack();
				}else{
					listaUltSpecForm.setListaSupportoColl("listaUltSpec");
					listaUltSpecForm.setNoColl(false);
					listaUltSpecForm.setCodBib(currentForm.getCodBib());
					listaUltSpecForm.setDescrBib(currentForm.getDescrBib());
					listaUltSpecForm.setCodSezione(listaUltSpecForm.getParamRicerca().getCodSez().toUpperCase());
					listaUltSpecForm.setCodColloc(listaUltSpecForm.getParamRicerca().getCodLoc().toUpperCase());
					listaUltSpecForm.setListaCollocSpec(listaColl);
					request.setAttribute("listaUltimeSPecificazioni", listaUltSpecForm.getListaCollocSpec());
					return navi.goForward(mapping.findForward("listaUltSpec"), false, listaUltSpecForm);
				}
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);
		}
	}

	private List<?> getListaCollocazioniSezione(EsameCollocRicercaVO paramRicerca, String tipoRichiesta, String ticket, int nRec,
			ActionForm form) throws Exception {
		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		DescrittoreBloccoVO blocco1;
		Object tracciato = null;
		//		int totRighe = 0;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaCollocazioniSezione(paramRicerca, tipoRichiesta, ticket, nRec);
		tracciato = blocco1;
		if (tracciato instanceof DescrittoreBloccoNumCollVO){
			DescrittoreBloccoNumCollVO descrBloccoNum = (DescrittoreBloccoNumCollVO)blocco1;
			if (currentForm.getTotRighe() == 0){
				//				totRighe = descrBloccoNum.getCountColl();
				currentForm.setTotRighe(descrBloccoNum.getCountColl());
			}
			//			currentForm.setBloccoAltraLista(blocco1.getNumBlocco());
			currentForm.setUltLoc(descrBloccoNum.getUltLoc());
			currentForm.setUltOrdLoc(descrBloccoNum.getUltOrdLoc());
			currentForm.setUltSpec(descrBloccoNum.getUltSpec());
			currentForm.setUltKeyLoc(descrBloccoNum.getUltKeyLoc());
		}
		if (currentForm.getTotRighe() == 0){
			currentForm.setTotRighe(blocco1.getTotRighe());
		}
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			if (currentForm.isCaricoAltriBlocchi()){
				currentForm.setNoColl(true);
				return null;
			}else{
				return impostaCampiBlocco(currentForm, blocco1, currentForm.getTotRighe());
			}
		}else{
			if (currentForm.isCaricoAltriBlocchi()){
				currentForm.getParamRicerca().setUltLoc(null);
				currentForm.getParamRicerca().setUltSpec(null);
				currentForm.getParamRicerca().setUltKeyLoc(0);
				currentForm.setNoColl(false);
				currentForm.setIdLista(blocco1.getIdLista());
				currentForm.setBloccoSelezionato(blocco1.getNumBlocco() - 1);
				currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
				//
				currentForm.setBlocchiTotali((int) (Math.ceil((double) currentForm.getTotRighe() / (double) currentForm.getNRec())));
				if (currentForm.getTotBlocchi() > 0){
					if (currentForm.getTotBlocchi() != currentForm.getBlocchiTotali()){
						currentForm.setTotBlocchi(blocco1.getTotBlocchi() + currentForm.getTotBlocchi());
						currentForm.setAbilitaBottoneCarBlocchi(true);
					}else{
						currentForm.setTotBlocchi(currentForm.getTotBlocchi());
						currentForm.setAbilitaBottoneCarBlocchi(false);
					}
				}else{
					currentForm.setTotBlocchi(blocco1.getTotBlocchi() + currentForm.getTotBlocchi());
					currentForm.setAbilitaBottoneCarBlocchi(true);
				}
				return blocco1.getLista();
			}else{
				return impostaCampiBlocco(currentForm, blocco1, currentForm.getTotRighe());
			}
		}
	}

	private List<?> impostaCampiBlocco(
			EsameCollocListeCollocazioniForm currentForm,
			DescrittoreBloccoVO blocco1, int totRighe) {
		currentForm.setNoColl(false);
		currentForm.setIdLista(blocco1.getIdLista());
		currentForm.setTotBlocchi(blocco1.getTotBlocchi());
		currentForm.setTotRighe(totRighe);
		currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
		currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
//			if ((blocco1.getTotBlocchi() > 1)){
		if (blocco1.getTotBlocchi() > 1){
			currentForm.setAbilitaBottoneCarBlocchi(false);
		}else{
			currentForm.setAbilitaBottoneCarBlocchi(true);
		}
		return blocco1.getLista();
	}
	private CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws Exception {
		CollocazioneDettaglioVO coll;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		coll = factory.getGestioneDocumentoFisico().getCollocazioneDettaglio(keyLoc, ticket);
		return coll;
	}
	private void loadTipiOrdinamento(HttpServletRequest request, ActionForm form) throws Exception {
		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		List<CodiceVO> lista = new ArrayList<CodiceVO>();
		if (currentForm.getListaSupportoSpec() != null){
			CodiceVO rec = new CodiceVO("SA","Specificazione Asc");
			lista.add(rec);
			rec = new CodiceVO("SD","Specificazione Desc");
			lista.add(rec);
		}else{
			CodiceVO rec = new CodiceVO("CA","Collocazione Asc");
			lista.add(rec);
			rec = new CodiceVO("CD","Collocazione Desc");
			lista.add(rec);
			rec = new CodiceVO("SA","Specificazione Asc");
			lista.add(rec);
			rec = new CodiceVO("SD","Specificazione Desc");
			lista.add(rec);
		}
		currentForm.setListaTipiOrdinamento(lista);
	}
	private void loadTipiOrdinamentoInv(HttpServletRequest request, ActionForm form) throws Exception {
		EsameCollocListeCollocazioniForm currentForm = (EsameCollocListeCollocazioniForm) form;
		List<CodiceVO> lista = new ArrayList<CodiceVO>();
		if (currentForm.getListaSupportoSpec() != null){
		}else{
			CodiceVO rec = new CodiceVO("I","Inventario Asc");
			lista.add(rec);
			rec = new CodiceVO("S","Sequenza Asc");
			lista.add(rec);
		}
		currentForm.setListaTipiOrdinamento(lista);
	}
}
