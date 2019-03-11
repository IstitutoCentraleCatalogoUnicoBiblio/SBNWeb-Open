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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.sif.AttivazioneSIFListaClassiCollegateVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.ListeInventariForm;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.VaiAModificaCollForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.NavigazioneDocFisico;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.action.ActionMessages;


public class VaiAModificaCollAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(VaiAModificaCollAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.lsSez", "listaSupportoSez");
		map.put("documentofisico.lsCl", "listaSupportoClassi");
		map.put("documentofisico.lsUC", "listaSupportoUltColl");
		map.put("documentofisico.lsUS", "listaSupportoUltSpec");
		map.put("documentofisico.bottone.esemplare", "esemplare");
		map.put("documentofisico.bottone.modificaInv", "modificaInv");
		map.put("documentofisico.bottone.scolloca", "scolloca");
		map.put("documentofisico.bottone.cancInv", "cancellaInv");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.indice", "indice");

		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		VaiAModificaCollForm currentForm = (VaiAModificaCollForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.getUtente().getCodBib().equals(currentForm.getCodBib())){
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

		this.saveToken(request);
		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		if (myForm.isConferma()){
			myForm.setConferma(false);
		}
		if(request.getAttribute("prov") != null && (request.getAttribute("prov").equals("ricercaEsemplare") ||
				request.getAttribute("prov").equals("provChiudi"))){
			myForm.setProv("");
//			if (navi.isFromBar()){
//
//			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
//
//			this.saveToken(request);
//			request.setAttribute("codPolo",myForm.getRecInv().getCodPolo());
//			request.setAttribute("codBib",myForm.getRecInv().getCodBib());
//			request.setAttribute("descrBib",myForm.getDescrBib());
//			request.setAttribute("codSerie",myForm.getRecInv().getCodSerie());
//			NavigationCache navi = Navigation.getInstance(request).getCache();
//			NavigationElement prev = navi.getElementAt(navi.getCurrentPosition() - 1);
//			if (prev != null && prev.getForm() instanceof ListeInventariForm) {
//				ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
//				request.setAttribute("tipoLista", listaInvForm.getTipoLista());
//				request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
//				request.setAttribute("codBib", listaInvForm.getCodBib());
//				request.setAttribute("descrBib", listaInvForm.getDescrBib());
//				return Navigation.getInstance(request).goBack();
//			}
//			return Navigation.getInstance(request).goBack();
//			}
		}
		Navigation navi = Navigation.getInstance(request);
		if(!myForm.isSessione())	{
			myForm.setTicket(navi.getUserTicket());
			myForm.setCodPolo(navi.getUtente().getCodPolo());
			myForm.setCodBib(navi.getUtente().getCodBib());
			myForm.setDescrBib(navi.getUtente().getBiblioteca());
			myForm.setSessione(true);
			loadDefault(request, mapping, form);
		}
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (navi.isFromBar()){
				NavigationCache cache = navi.getCache();
				NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
				NavigationElement post = cache.getElementAt(cache.getCurrentPosition() + 1);
				if (prev != null && prev.getName().equals("vaiAListaInventariTitoloForm")
						&& post == null){
					return mapping.getInputForward();
			}
			int keyLoc = myForm.getRecCollDett().getKeyColloc();
			CollocazioneDettaglioVO recColl = null;
			recColl = this.getCollocazioneDettaglio(keyLoc, myForm.getTicket());
			if (recColl != null) {
				myForm.setRecCollDett(recColl);
				this.loadDatiCollocazione1(request, form);

			}else{
				throw new ValidationException("ricercaNoColl", ValidationException.errore);
			}
			return mapping.getInputForward();
		}
		if (request.getAttribute("invCanc") != null ){
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
		}
		if (request.getAttribute("classe") != null){
			myForm.setCodCollocazione((String)request.getAttribute("classe"));
			return mapping.getInputForward();
		}
		if (request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA)!= null) {
			SezioneCollocazioneVO sez = (SezioneCollocazioneVO)(request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA));

			myForm.getRecCollDett().setCodPoloSez(sez.getCodPolo());
			myForm.getRecCollDett().setCodBibSez(sez.getCodBib());
			myForm.getRecCollDett().setCodSez(sez.getCodSezione());
			//almaviva5_20140414 #5551
			SezioneCollocazioneVO sezioneDettaglio = this.getSezioneDettaglio(sez.getCodPolo(),
					sez.getCodBib(), sez.getCodSezione(),
					myForm.getTicket());
			if (sezioneDettaglio != null)
				myForm.setRecSezione(sezioneDettaglio);
			//
			myForm.getRecCollDett().setTipoColloc(sez.getTipoColloc());
			myForm.getRecCollDett().setTipoSez(sez.getTipoSezione());
			myForm.getRecCollDett().setDescrTipoColl(sez.getDescrTipoColl());
			myForm.getRecCollDett().setDescr(sez.getDescrSezione());
			myForm.setRecFormatiSezioni(null);
			//			this.loadPagina(request);
			myForm.setLenteSez("lenteSez");
			myForm.setNoSezione("siSezione");
			myForm.getRecCollDett().setCodColloc("");
			myForm.getRecCollDett().setSpecColloc("");
			this.loadDatiCollocazione2(request, form);
		}
		// controllo se ho già i dati in sessione;
		if(!myForm.isSessione())		{
			myForm.setSessione(true);
			loadDefault(request, mapping, form);
			navi.addBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL);
		}
		navi.addBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL);
		try{
			if (request.getAttribute("keyLoc") != null) {
				if (request.getAttribute("codBib") != null &&
						request.getAttribute("descrBib") != null &&
						request.getAttribute("bid") != null &&
						request.getAttribute("titolo") != null) {
					myForm.setCodBib((String)request.getAttribute("codBib"));
					myForm.setDescrBib((String)request.getAttribute("descrBib"));
					//					myForm.setBid((String)request.getAttribute("bid"));
					//					myForm.setTitolo((String)request.getAttribute("titolo"));
					List lista = this.getTitolo((String)request.getAttribute("bid"), myForm.getTicket());
					TitoloVO titolo = null;
					if (lista != null){
						if (lista.size() == 1){
							titolo = (TitoloVO)lista.get(0);
						}
					}else{
						throw new ValidationException("titNotFoundPolo", ValidationException.errore);
					}

					myForm.setBid(titolo.getBid());
					myForm.setTitolo(titolo.getIsbd());
					myForm.setTerzaParte(titolo.getTerzaParte());
				}
				//				inv
				if (request.getAttribute("codSerie") != null &&
						request.getAttribute("codInvent") != null ) {
					myForm.setCodSerie((String)request.getAttribute("codSerie"));
					myForm.setCodInvent((Integer)request.getAttribute("codInvent"));
				}
				myForm.setKeyLoc((Integer)(request.getAttribute("keyLoc")));
				//				}
				//				if (myForm.getKeyLoc() != 0 	|| request.getAttribute("provChiudi").equals("provChiudi")){
				CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(myForm.getKeyLoc(), myForm.getTicket());
				if (coll != null){
					myForm.setCollocazioneOriginale((CollocazioneDettaglioVO) coll.clone());
					if (coll.getCodSez() != null){
						SezioneCollocazioneVO sez = this.getSezioneDettaglio(myForm.getCodPolo(), coll.getCodBibSez(), coll.getCodSez().trim(), myForm.getTicket());
						if (sez != null){
							myForm.setRecSezione(sez);
						}
					}
					if (coll.getCancDb2i().equals("S")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCancellata"));

						return navi.goBack(true);
					}else{
						List lista = this.getTitolo(coll.getBid(), myForm.getTicket());
						TitoloVO titolo = null;
						if (lista != null){
							if (lista.size() == 1){
								titolo = (TitoloVO)lista.get(0);
							}
						}else{
							throw new ValidationException("titNotFoundPolo", ValidationException.errore);
						}
						coll.setBid(titolo.getBid());
//						coll.setBidDescr(titolo.getIsbd());
						myForm.setRecCollDett(coll);
						if (!titolo.isFlagCondiviso()){
							coll.setBidDescr(" " + "[Loc] "+titolo.getIsbd());
						}else{
							coll.setBidDescr(" "+titolo.getIsbd());
							if (titolo.getNatura() != null && (titolo.getNatura().equals("M")
									|| titolo.getNatura().equals("W") || titolo.getNatura().equals("S"))){
								myForm.setAbilitaBottoneInviaInIndice(true);
							}
						}
					}
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCollocazioneInesistente"));

					return mapping.getInputForward();
				}
				myForm.setReticolo(this.getAnaliticaPerCollocazione(myForm.getBid(), myForm.getTicket(), form));
				myForm.setCodSez(coll.getCodSez());
				this.loadDatiCollocazione1(request, form);
			}
			String chiave = null;
			if (myForm.getChiave() != null && myForm.getChiave().equals("chiaveTitolo")){
				chiave = this.getChiaviTitoloAutore("T", myForm.getRecCollDett().getBid(), myForm.getTicket());
				if (chiave != null){
					myForm.setCodSpecificazione(chiave.trim());
//					myForm.setCodSpecificazioneDisable(true);//bug 4687 collaudo
				}else{
					myForm.setCodSpecificazione("");
					myForm.setCodSpecificazioneDisable(false);

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.chiaveTitoloNonEsistente"));

				}
			}else if (myForm.getChiave() != null && myForm.getChiave().equals("chiaveAutore")){
				String vid = null;
				CollocazioneTitoloVO[] reticolo = myForm.getReticolo().getListaTitoliCollocazione();
				CollocazioneTitoloVO recRet = null;
				for (int i = 0; i < myForm.getReticolo().getListaTitoliCollocazione().length; i++) {
					recRet = reticolo[i];
					if (myForm.getRecCollDett().getBid().equals(recRet.getBid())){
						if (recRet.getVidAutore() != null){
							vid = recRet.getVidAutore();
						}else{
							myForm.setCodSpecificazione("");
							myForm.setCodSpecificazioneDisable(false);

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.chiaveAutoreNonEsistente"));

						}
					}
				}
				//				}
				chiave = this.getChiaviTitoloAutore("A", vid, myForm.getTicket());
				if (chiave != null){
					myForm.setCodSpecificazione(chiave);
//					myForm.setCodSpecificazioneDisable(true);//bug 4687 collaudo
				}else{
					myForm.setCodSpecificazione("");
					myForm.setCodSpecificazioneDisable(false);

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.chiaveAutoreNonEsistente"));

				}

			}else if (myForm.getChiave()!=null && myForm.getChiave().equals("altro")){
				myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc().trim());
				myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
				myForm.setCodSpecificazioneDisable(false);
			}
			//gestione tipoCollocazione = chiave Titolo
			if (myForm.getRecCollDett().getTipoColloc() != null &&
					myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CHIAVE_TITOLO)) {
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CHIAVE_TITOLO);
				String cles = null;
				myForm.setCodCollocazione("");
				String isbd = null;
				String titoloSenzaAstrisco = null;
				TreeElementViewTitoli bid = (TreeElementViewTitoli)myForm.getReticolo().getAnalitica().findElement(myForm.getBid());
				if (bid.getNatura().equals("W")){
					isbd = myForm.getReticolo().getTitoloGenerale().getIsbd();
				}else{
					isbd = myForm.getTitolo();
				}

				int asterisco = isbd.indexOf("*");// si cerca carattere "*" all'interno di desc(descrizione )
				if (asterisco > 0){
					titoloSenzaAstrisco = isbd.substring(asterisco);
				}else{
					titoloSenzaAstrisco = isbd;
				}

				GeneraChiave keyDuoble = new GeneraChiave(titoloSenzaAstrisco,"");
				keyDuoble.estraiChiavi("", titoloSenzaAstrisco);
				cles = (keyDuoble.getKy_cles1_A());

				if (cles != null){
					if (cles.length() >= 24){
						myForm.setCodCollocazione(cles.substring(0, 24).trim());
					}else{
						myForm.setCodCollocazione(cles.trim());
					}
				}else{
					myForm.setCodCollocazione("");
					myForm.setCodSpecificazione("");
					myForm.setChiave("Altro");
				}
				myForm.setCodCollocazioneDisable(true);
			}

			////////////////////

			//			if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())
			//					&& (ValidazioneDati.strIsNull(myForm.getRecCollDett().getCodColloc()))){
			if (myForm.getCodFormato() != null && !myForm.getCodFormato().equals("")){
				if (myForm.getRecFormatiSezioni() != null){
					if ((!myForm.getCodFormato().equals(myForm.getRecFormatiSezioni().getCodFormato()))){

						FormatiSezioniVO rec = this.trovaFormatoSezione(myForm.getCodFormato(), form);
						//27/8/2008
						//rec.setProgNum(0);
						//rec.setProgSerie(0);
						if (rec != null){
							String descrFor [] = rec.getDescrFor().split(";");
							if (descrFor.length == 3){
								rec.setDescrFor(descrFor[2]);
							}else if (descrFor.length == 1){
								rec.setDescrFor(descrFor[0]);
							}else if (descrFor.length == 2){
								rec.setDescrFor("");
							}
							rec.setProgNum(0);
							rec.setProgSerie(0);
							myForm.setRecFormatiSezioni(rec);
						}else{
							rec = new FormatiSezioniVO();
							rec = formatoNonTrovato(request, myForm, new ActionMessages());
							myForm.setRecFormatiSezioni(rec);
							myForm.setCodFormato("");
						}
					}
				}
			}
			//			}
			/*
			 * if (myForm.getCodFormato() != null && !myForm.getCodFormato().equals("")){
				if (myForm.getRecFormatiSezioni() != null){
					if ((!myForm.getCodFormato().equals(myForm.getRecFormatiSezioni().getCodFormato()))){

						FormatiSezioniVO rec = this.trovaFormatoSezione(myForm.getCodFormato(), form);
//						27/8/2008
						rec.setProgNum(0);
						rec.setProgSerie(0);

						myForm.setRecFormatiSezioni(rec);
					}
				}
			}
			 */
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm currentForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);

		boolean datiMappaModificati = false;//viene trattato in prepareUpdate()
		boolean collocazioneEsistente = false;
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			if (currentForm.getCodSez().equals(null)){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codSezObbl"));

				currentForm.setConferma(false);
				return mapping.getInputForward();
			}else{
				currentForm.setCodSez(currentForm.getCodSez().trim());
			}
			if (currentForm.getCodSez().trim().length() > 0){
				SezioneCollocazioneVO sez = this.getSezioneDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getCodSez(), currentForm.getTicket());
				if (sez != null){
					currentForm.setRecSezione(sez);
				}
			}

			currentForm.getRecCollDett().setUteAgg(navi.getUtente().getFirmaUtente());
			currentForm.getRecCollDett().setCodPolo(currentForm.getCodPolo());
			currentForm.getRecCollDett().setCodBib(currentForm.getCodBib());
			//
			currentForm.getRecCollDett().setCodColloc(currentForm.getCodCollocazione());
			currentForm.getRecCollDett().setSpecColloc(currentForm.getCodSpecificazione());
			currentForm.getRecCollDett().setConsistenza(currentForm.getConsistenza());
			//
			if (currentForm.getProv() == null){
				if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
						|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					if (currentForm.getCodFormato() == null || currentForm.getCodFormato().trim().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.validaFormatiSezioniCodFormatoObbligatorio"));

						return mapping.getInputForward();
					}
				}
			}
			this.prepareUpdate(mapping, request, form);
			if (currentForm.getCollocazioneOriginale().getCodSez()!= null && !currentForm.getCollocazioneOriginale().getCodSez().trim().equals
					(currentForm.getRecCollDett().getCodSez().trim())){
				datiMappaModificati = true;
			}
			if (currentForm.getCollocazioneOriginale().getCodColloc()!= null && !currentForm.getCollocazioneOriginale().getCodColloc().trim().equals
					(currentForm.getRecCollDett().getCodColloc().trim())){
				datiMappaModificati = true;
			}
			if (currentForm.getCollocazioneOriginale().getSpecColloc()!= null && !currentForm.getCollocazioneOriginale().getSpecColloc().trim().equals
					(currentForm.getRecCollDett().getSpecColloc().trim())){
				datiMappaModificati = true;
			}

			if (currentForm.getCodSez() != null && currentForm.getCodCollocazione() != null && currentForm.getCodSpecificazione() != null){
				if (currentForm.getCodSez().trim().equals("")
						&& currentForm.getCodCollocazione().trim().equals("")
						&& currentForm.getCodSpecificazione().trim().equals("")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneEStringaDiCollocazioneASpazio"));

					currentForm.setConferma(false);
					return mapping.getInputForward();
				}
			}
			if (currentForm.isConferma()){ //??????????? controllare e se non serve togliere
				currentForm.setConferma(false);
				mapping.getInputForward();
			}
			if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
					|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
					|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
				currentForm.getRecCollDett().setRecFS(currentForm.getRecFormatiSezioni());
			}
			//				//
			if (currentForm.getCodSez() != null && currentForm.getCodCollocazione() != null && currentForm.getCodSpecificazione() != null){
				if (currentForm.getCodSez().trim().equals("")
						&& currentForm.getCodCollocazione().trim().equals("")
						&& currentForm.getCodSpecificazione().trim().equals("")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneEStringaDiCollocazioneASpazio"));

					currentForm.setConferma(false);
					return mapping.getInputForward();
				}
			}
			currentForm.getRecCollDett().setUteAgg(navi.getUtente().getFirmaUtente());
			//				//
			if (request.getAttribute("collocazioneEsistente") != null){
				collocazioneEsistente = true;
			}

			if (!this.updateCollocazione(currentForm.getRecCollDett(), datiMappaModificati, collocazioneEsistente, currentForm.getTicket())){
//				if (!this.updateCollocazione(myForm.getRecCollDett(), collocazioneEsistente, myForm.getTicket())){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.invCollUpdErrore"));

				this.saveToken(request);
				return mapping.getInputForward();
			}else{
				CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(currentForm.getRecCollDett().getKeyColloc(), currentForm.getTicket());
//				myForm.getRecCollDett().setDataAgg(coll.getDataAgg());
//				myForm.getRecCollDett().setUteAgg(coll.getUteAgg());
				currentForm.setRecCollDett(coll);
				this.loadDatiCollocazione1(request, currentForm);
				if (currentForm.getProv() != null && currentForm.getProv().equals("tastoEsemplare")){
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("titolo", currentForm.getTitolo());
					currentForm.getRecCollDett().setCodSez(currentForm.getCodSez());
					currentForm.getRecCollDett().setConsistenza(currentForm.getConsistenza());
					request.setAttribute("recColl", currentForm.getRecCollDett());
					request.setAttribute("reticolo", currentForm.getReticolo());
					request.setAttribute("codSerie", currentForm.getCodSerie());
					request.setAttribute("codInv", String.valueOf(currentForm.getCodInvent()));
					if (currentForm.getRecCollDett().getCodBibDoc() != null){
						request.setAttribute("modifica", "modifica");
						return mapping.findForward("modificaEsemplare");
					}else{
						return mapping.findForward("ricercaEsemplare");
					}
				}else{
					if (currentForm.getProv() != null && !currentForm.getProv().equals("ok")){
						request.setAttribute("codBib",currentForm.getCodBib());
						request.setAttribute("descrBib",currentForm.getDescrBib());
						request.setAttribute("codSerie",currentForm.getCodSerie());
						request.setAttribute("codInvent",currentForm.getCodInvent());
						//se devo andare a listaInvTit non serve il true perche devo ricaricare la listaInvTit
						//se devo andare a listaInvDiColl serve il true perche devo ricaricare la listaInvDiColl

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

						this.saveToken(request);
						return navi.goBack();
					}
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("titolo", currentForm.getTitolo());
					currentForm.getRecCollDett().setCodSez(currentForm.getCodSez());
					currentForm.getRecCollDett().setConsistenza(currentForm.getConsistenza());
					request.setAttribute("recColl", currentForm.getRecCollDett());
					request.setAttribute("reticolo", currentForm.getReticolo());
					request.setAttribute("codSerie", currentForm.getCodSerie());
					request.setAttribute("codInv", String.valueOf(currentForm.getCodInvent()));
					if (currentForm.isProvTastoEsemplare()){
						if (currentForm.getRecCollDett().getCodBibDoc() != null){
							request.setAttribute("modifica", "modifica");
							return mapping.findForward("modificaEsemplare");
						}else{
							return mapping.findForward("ricercaEsemplare");
						}
					}else{
						return navi.goBack();
					}
				}
			}
		} catch (ValidationException e) {
			if (e.getMsg()!=null){
				if (e.getMsg().equals("msgCodLoc")){

					if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
							|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneGiaEsistente"));
						currentForm.setConferma(false);
					}else{
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage()/*+" "+ve.getBid()+" " +ve.getIsbd()+" ?"*/));
						currentForm.setConferma(true);
					}

					this.saveToken(request);
					currentForm.setProv("ok");
					return mapping.getInputForward();
				}
				if (e.getMsg().equals("msgCodLocEsistente")){

					if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							/*segnalazione firenze con mail del 1/10/2012*/
//							|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
							|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneGiaEsistente"));
						currentForm.setConferma(false);
					}else{
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage()/*+" "+ve.getBid()+" " +ve.getIsbd()+" ?"*/));
						currentForm.setConferma(true);
					}

					this.saveToken(request);
					currentForm.setProv("ok");
					return mapping.getInputForward();
				}
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			request.setAttribute("errore", true);
			return mapping.getInputForward();
		} catch (DataException e) {
			if (e.getMessage().equals("collCancellata")){

				currentForm.setConferma(false);
				if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
					if (currentForm.getRecFormatiSezioni().getProgSerie() == -1){
						currentForm.getRecFormatiSezioni().setProgSerie(0);
					}
					if (currentForm.getRecFormatiSezioni().getProgNum() == -1){
						currentForm.getRecFormatiSezioni().setProgNum(0);
					}
				}else if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					if (currentForm.getRecFormatiSezioni().getProgNum() == -1){
						currentForm.getRecFormatiSezioni().setProgNum(0);
					}
				}
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCancellata"));

				this.saveToken(request);
				request.setAttribute("errore", true);
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			request.setAttribute("errore", true);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			request.setAttribute("errore", true);
			return mapping.getInputForward();
		}
	}

	private void prepareUpdate(ActionMapping mapping, HttpServletRequest request, ActionForm form)
	throws Exception {
		VaiAModificaCollForm currentForm = (VaiAModificaCollForm) form;
		currentForm.getRecCollDett().setCodPolo(currentForm.getCodPolo());
		currentForm.getRecCollDett().setCodBib(currentForm.getCodBib());
		currentForm.getRecCollDett().setCodPoloSez(currentForm.getCodPolo());
		currentForm.getRecCollDett().setCodBibSez(currentForm.getCodBib());
		currentForm.getRecCollDett().setCodSez(currentForm.getCodSez());
		if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_STRUTTURATA)){
			currentForm.setLivello1(this.trattamentoLivello(currentForm.getLivello1()));
			currentForm.setLivello2(this.trattamentoLivello(currentForm.getLivello2()));
			currentForm.setLivello3(this.trattamentoLivello(currentForm.getLivello3()));
			//almaviva5_20121108
			//myForm.setCodCollocazione(myForm.getLivello1().trim()+"/"+myForm.getLivello2().trim()+"/"+myForm.getLivello3().trim());
			StringBuilder buf = new StringBuilder(24);
			buf.append(currentForm.getLivello1()).append('/');
			buf.append(currentForm.getLivello2()).append('/');
			buf.append(currentForm.getLivello3());
			String coll = buf.toString();
			currentForm.setCodCollocazione(coll);
			log.debug("COD_ESPLICITA_STRUTTURATA: " + coll);
			//
		}
		//inizio gestione senza buchi
		if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
				|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
				|| currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
			int progressivi[]={-1,-1};

			if ((currentForm.getRecFormatiSezioni().getProgSerie() == 0) && (currentForm.getRecFormatiSezioni().getProgNum() == 0 )){
				//contestualmente all'ins. della collocazione calcolo i progressivi da inserire in forSez
				currentForm.setCodCollocazione("-1");
				currentForm.setCodSpecificazione("-1");
			}else{
				//l'utente ha indicato i valori di serie/progr
				//controllo la validità dei valori inseriti con getFormatoSezioni
				FormatiSezioniVO forSez = this.getFormatiSezioniDettaglio(currentForm.getRecCollDett().getCodPoloSez(), currentForm.getRecCollDett().getCodBibSez(),
						currentForm.getCodSez(), currentForm.getCodFormato(), currentForm.getTicket());
				if (forSez != null){
					//controllo che il numero digitato dal bibliotecario sia valido
					if (currentForm.getRecFormatiSezioni().getProgSerie() > forSez.getProgSerie()){
						//serie non valida
						throw new DataException("serieNonValida");
					}
					if (currentForm.getRecFormatiSezioni().getProgSerie() == forSez.getProgSerie()){
						if (currentForm.getRecFormatiSezioni().getProgNum() > forSez.getProgNum()){
							//numero non valido
							throw new DataException("numeroNonValido");
						}
					}
					progressivi[0] = currentForm.getRecFormatiSezioni().getProgSerie();
					progressivi[1] = currentForm.getRecFormatiSezioni().getProgNum();
					if (currentForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
						currentForm.setCodCollocazione(String.valueOf(currentForm.getRecFormatiSezioni().getProgNum()).trim());
						currentForm.setCodSpecificazione("");
					}else{
						currentForm.setCodCollocazione(String.valueOf(currentForm.getCodFormato() + currentForm.getRecFormatiSezioni().getProgSerie()).trim());
						currentForm.setCodSpecificazione(String.valueOf(currentForm.getRecFormatiSezioni().getProgNum()).trim());
					}
				}
			}
			currentForm.setRecFormatiSezioni(new FormatiSezioniVO());
			currentForm.getRecFormatiSezioni().setCodPolo(currentForm.getRecCollDett().getCodPoloSez());
			currentForm.getRecFormatiSezioni().setCodBib(currentForm.getRecCollDett().getCodBibSez());
			currentForm.getRecFormatiSezioni().setCodSez(currentForm.getRecCollDett().getCodSez());
			currentForm.getRecFormatiSezioni().setCodFormato(currentForm.getCodFormato());
			currentForm.getRecFormatiSezioni().setProgSerie(progressivi[0]);
			currentForm.getRecFormatiSezioni().setProgNum(progressivi[1]);
			Navigation navi = Navigation.getInstance(request);
			currentForm.getRecFormatiSezioni().setUteAgg(navi.getUtente().getFirmaUtente());
		}
		//fine gestione senza buchi
		currentForm.getRecCollDett().setCodColloc(currentForm.getCodCollocazione());
		currentForm.getRecCollDett().setSpecColloc(currentForm.getCodSpecificazione());
		currentForm.getRecCollDett().setConsistenza(currentForm.getConsistenza());
	}

	public ActionForward esemplare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaSalva"));

			this.saveToken(request);
			myForm.setConferma(true);
			myForm.setProv("esemplare");
			myForm.setProvTastoEsemplare(true);
			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward esEsemplare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		boolean collocazioneEsistente = false;
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			myForm.setProv("tastoEsemplare");
//			request.setAttribute("tastoEsemplare", "tastoEsemplare");
			ActionForward forward = this.ok(mapping, myForm, request, response);
			if (forward != null){
				return forward;
			}

//			this.ok(mapping, myForm, request, response);
			if (request.getAttribute("errore") != null){
				return mapping.getInputForward();
			}
			if (request.getAttribute("collocazioneEsistente") != null){
				collocazioneEsistente = true;
			}

			myForm.setProv("");
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("bid", myForm.getBid());
			request.setAttribute("titolo", myForm.getTitolo());
			myForm.getRecCollDett().setCodSez(myForm.getCodSez());
			myForm.getRecCollDett().setConsistenza(myForm.getConsistenza());
			request.setAttribute("recColl", myForm.getRecCollDett());
			request.setAttribute("reticolo", myForm.getReticolo());
			request.setAttribute("codSerie", myForm.getCodSerie());
			request.setAttribute("codInv", String.valueOf(myForm.getCodInvent()));
			if (myForm.getRecCollDett().getCodBibDoc() != null){
				request.setAttribute("modifica", "modifica");
				return mapping.findForward("modificaEsemplare");
			}else{
				return mapping.findForward("ricercaEsemplare");
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}
	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAModificaCollForm currentForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			currentForm.setConferma(false);
			if (request.getAttribute("diagnostico") != null){

				LinkableTagUtils.addError(request, new ActionMessage("" + request.getAttribute("diagnostico") + " nella biblioteca " + currentForm.getCodPolo() + " " + currentForm.getCodBib()));

				currentForm.setConferma(false);
				return mapping.getInputForward();
			}
			if (currentForm.isAbilitaBottoneInviaInIndice()){
				if (currentForm.getTasto() != null && currentForm.getTasto().equals("aggiornaInIndice")){
					try{
						currentForm.setTasto(null);
						AreePassaggioSifVO areePassaggioSifVO = new AreePassaggioSifVO();
						//findForward a gestione bibliografica
						areePassaggioSifVO.setOggettoDaRicercare(currentForm.getBid());
						areePassaggioSifVO.setDescOggettoDaRicercare(currentForm.getRecCollDett().getBidDescr());
						areePassaggioSifVO.setNaturaOggetto("M");
						areePassaggioSifVO.setTipMatOggetto("M");
						areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
						areePassaggioSifVO.setVisualCall(false);

						UserVO utente = navi.getUtente();
						areePassaggioSifVO.setCodBiblioteca(utente.getCodPolo().toString() + currentForm.getCodBib().toString());

						areePassaggioSifVO.setConsistenzaPolo(currentForm.getConsistenza().trim());

						TreeElementViewTitoli titoloNotiziaCorrente = (TreeElementViewTitoli)currentForm.getReticolo().getAnalitica().findElement(currentForm.getBid());
						//
						if (titoloNotiziaCorrente.isFlagCondiviso()){
							//polo = true, indice = true
							areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
						}else{
							//polo = true, indice = false
							areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
						}
						request.setAttribute("consIndice", "modificaCollocazione");
						request.setAttribute("areePassaggioSifVO", areePassaggioSifVO);
						return mapping.findForward("sinteticaLocalizzazioni");

					}	catch (Exception e) { // altri tipi di errore

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
			}

			if (currentForm.getProv() != null
					&& (currentForm.getProv().equals("ok")
							|| currentForm.getProv().equals("modificaInv")
							|| currentForm.getProv().equals("esemplare"))){
				if (currentForm.getProv().equals("ok")){
					request.setAttribute("collocazioneEsistente", "collocazioneEsistente");
					currentForm.setConferma(false);
					return this.ok(mapping, form, request, response);
				}else{
					if (currentForm.getMsg() != null && currentForm.getMsg().equals("confermaSalva")){
						currentForm.setConferma(true);
						if (currentForm.getProv().equals("modificaInv")){
							return this.modificaInventario(mapping, form, request, response);
						}else if (currentForm.getProv().equals("esemplare")){
							return this.esEsemplare(mapping, currentForm, request, response);
						}
					}else{
						if (currentForm.getMsg() != null && currentForm.getMsg().equals("msgCodLocEsistente")){
							request.setAttribute("collocazioneEsistente", "collocazioneEsistente");
							currentForm.setConferma(false);
						}
						if (currentForm.getProv().equals("modificaInv")){
							return this.modificaInventario(mapping, form, request, response);
						}else if (currentForm.getProv().equals("esemplare")){
							return this.esEsemplare(mapping, currentForm, request, response);
						}
					}
				}
			}
			if (currentForm.getProv() != null
					&& (currentForm.getProv().equals("cancella") )){
			String tipoOperazione = DocumentoFisicoCostant.C_CANCELLA_INV;
			//cancellazione
			InventarioVO keyInv =  new InventarioVO();
			keyInv.setCodPolo(currentForm.getCodPolo());
			keyInv.setCodBib(currentForm.getCodBib());
			keyInv.setCodSerie(currentForm.getCodSerie());
			keyInv.setCodInvent(currentForm.getCodInvent());
			keyInv.setUteAgg(navi.getUtente().getFirmaUtente());
			currentForm.setRecInv(keyInv);
			if (!this.deleteInventario(currentForm.getRecInv(), currentForm.getRecCollDett(),
					tipoOperazione, currentForm.getTicket())){
			}else{
				request.setAttribute("codPolo",currentForm.getRecInv().getCodPolo());
				request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

				this.saveToken(request);
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
				//				return mapping.findForward("cancellaInv");
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreCancella"));

			this.saveToken(request);

			}
			return mapping.getInputForward();
		}
		catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}catch (ValidationException e) {

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
		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		// Viene settato il token per le transazioni successive
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			myForm.setConferma(false);
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
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			request.setAttribute("codBib",myForm.getRecInv().getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());
			request.setAttribute("codSerie",myForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent",myForm.getRecInv().getCodInvent());
			//			return mapping.findForward("cancInv");
			String chiamante = navi.getActionCaller();

			if (chiamante.toUpperCase().indexOf("INSERIMENTOCOLL") > -1) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

				this.saveToken(request);
				return mapping.findForward("listaInventariDelTitolo");
			}
			NavigationCache cache = navi.getCache();
			NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
			if (prev != null && prev.getForm() instanceof ListeInventariForm) {
				ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
				if (!listaInvForm.getParamRicerca().getCodSez().trim().equals(myForm.getRecCollDett().getCodSez().trim())){
					request.setAttribute("sezioneCambiata", "sezioneCambiata");
				}
				if (myForm.getCollocazioneOriginale().getCodSez()!= null && !myForm.getCollocazioneOriginale().getCodSez().trim().equals
						(myForm.getRecCollDett().getCodSez().trim())){
					request.setAttribute("datiMappaModificati", "datiMappaModificati");
					}
				if (myForm.getCollocazioneOriginale().getCodColloc()!= null && !myForm.getCollocazioneOriginale().getCodColloc().trim().equals
						(myForm.getRecCollDett().getCodColloc().trim())){
					request.setAttribute("datiMappaModificati", "datiMappaModificati");
				}
				if (myForm.getCollocazioneOriginale().getSpecColloc()!= null && !myForm.getCollocazioneOriginale().getSpecColloc().trim().equals
						(myForm.getRecCollDett().getSpecColloc().trim())){
					request.setAttribute("datiMappaModificati", "datiMappaModificati");
				}


				request.setAttribute("tipoLista", listaInvForm.getTipoLista());
				request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
				request.setAttribute("codBib", listaInvForm.getCodBib());
				request.setAttribute("descrBib", listaInvForm.getDescrBib());
				return navi.goBack();
			}
			return navi.goBack();
			//			return Navigation.getInstance(request).goBack(true);
			//			return mapping.findForward("chiudi");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoUltColl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()){
			return mapping.getInputForward();
		}
		try {
			request.setAttribute("codBib",myForm.getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());

			EsameCollocRicercaVO paramRicercaCollSpec = new EsameCollocRicercaVO();
			paramRicercaCollSpec.setCodPolo(myForm.getRecCollDett().getCodPoloSez());
			paramRicercaCollSpec.setCodBib(myForm.getRecCollDett().getCodBibSez());
			paramRicercaCollSpec.setCodPoloSez(myForm.getRecCollDett().getCodPoloSez());
			paramRicercaCollSpec.setCodBibSez(myForm.getRecCollDett().getCodBibSez());
			paramRicercaCollSpec.setCodSez(myForm.getRecCollDett().getCodSez());
			paramRicercaCollSpec.setCodLoc("");
			paramRicercaCollSpec.setCodSpec("");
			paramRicercaCollSpec.setEsattoColl(false);
			paramRicercaCollSpec.setEsattoSpec(false);
			paramRicercaCollSpec.setOrdLst("");

			request.setAttribute("paramRicerca",paramRicercaCollSpec);
			request.setAttribute("listaUltColl", "listaUltColl");
			request.setAttribute("chiamante", mapping.getPath());
			return navi.goForward(mapping.findForward("listaUltColl"));

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoUltSpec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			request.setAttribute("codBib",myForm.getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());
			EsameCollocRicercaVO paramRicercaCollSpec = new EsameCollocRicercaVO();
			paramRicercaCollSpec.setCodPolo(myForm.getRecCollDett().getCodPoloSez());
			paramRicercaCollSpec.setCodBib(myForm.getRecCollDett().getCodBibSez());
			paramRicercaCollSpec.setCodPoloSez(myForm.getRecCollDett().getCodPoloSez());
			paramRicercaCollSpec.setCodBibSez(myForm.getRecCollDett().getCodBibSez());
			paramRicercaCollSpec.setCodSez(myForm.getRecCollDett().getCodSez());
			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
					|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
				myForm.setCodCollocazione(myForm.getCodFormato().trim());
			}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO) ){
				myForm.setCodCollocazione("");
			}
			paramRicercaCollSpec.setCodLoc(myForm.getCodCollocazione());
			paramRicercaCollSpec.setCodSpec("");
			paramRicercaCollSpec.setEsattoColl(false);
			paramRicercaCollSpec.setEsattoSpec(false);
			paramRicercaCollSpec.setOrdLst("");

			request.setAttribute("paramRicerca",paramRicercaCollSpec);
			request.setAttribute("listaUltSpec", "listaUltSpec");

			return navi.goForward(mapping.findForward("listaUltSpec"));

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoClassi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}

			if (!navi.getUtente().getCodBib().equals(myForm.getCodBib())){
				navi.getUtente().setCodBib(myForm.getCodBib());
			}
			ClassiDelegate classi = ClassiDelegate.getInstance(request);
			AttivazioneSIFListaClassiCollegateVO richiesta = new AttivazioneSIFListaClassiCollegateVO(myForm.getBid(), myForm.getTitolo(), myForm.getRecSezione().getClassific(), myForm.getNRec(), "classe");
			return classi.getSIFListaClassiCollegate(mapping, richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward modificaInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaSalva"));
			myForm.setMsg("confermaSalva");

			this.saveToken(request);
			myForm.setConferma(true);
			myForm.setProv("modificaInv");
			return mapping.getInputForward();


		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward modificaInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		boolean datiMappaModificati = false;
//		if (myForm.getCodSez()!= null && !myForm.getCodSez().trim().equals(myForm.getRecCollDett().getCodSez().trim())){
//			datiMappaModificati = true;
//		}
//		if (myForm.getCodCollocazione()!= null && !myForm.getCodCollocazione().trim().equals(myForm.getRecCollDett().getCodColloc().trim())){
//			datiMappaModificati = true;
//		}
//		if (myForm.getCodSpecificazione()!= null && !myForm.getCodSpecificazione().trim().equals(myForm.getRecCollDett().getSpecColloc().trim())){
//			datiMappaModificati = true;
//		}
		boolean collocazioneEsistente = false;
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			if (myForm.getProv() == null){
				if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
						|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					if (myForm.getCodFormato() == null || myForm.getCodFormato().trim().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.validaFormatiSezioniCodFormatoObbligatorio"));

						return mapping.getInputForward();
					}
				}
			}
			this.prepareUpdate(mapping, request, form);
			if (myForm.getCollocazioneOriginale().getCodSez()!= null && !myForm.getCollocazioneOriginale().getCodSez().trim().equals
					(myForm.getRecCollDett().getCodSez().trim())){
				datiMappaModificati = true;
			}
			if (myForm.getCollocazioneOriginale().getCodColloc()!= null && !myForm.getCollocazioneOriginale().getCodColloc().trim().equals
					(myForm.getRecCollDett().getCodColloc().trim())){
				datiMappaModificati = true;
			}
			if (myForm.getCollocazioneOriginale().getSpecColloc()!= null && !myForm.getCollocazioneOriginale().getSpecColloc().trim().equals
					(myForm.getRecCollDett().getSpecColloc().trim())){
				datiMappaModificati = true;
			}


			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
					|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
					|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
				myForm.getRecCollDett().setRecFS(myForm.getRecFormatiSezioni());
			}
			//			}
		//			else{
			//				myForm.setProv(null);
			//			}
			if (myForm.getCodSez() != null && myForm.getCodCollocazione() != null && myForm.getCodSpecificazione() != null){
				if (myForm.getCodSez().trim().equals("")
						&& myForm.getCodCollocazione().trim().equals("")
						&& myForm.getCodSpecificazione().trim().equals("")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneEStringaDiCollocazioneASpazio"));

					myForm.setConferma(false);
					return mapping.getInputForward();
				}
			}
			myForm.getRecCollDett().setUteAgg(navi.getUtente().getFirmaUtente());
			myForm.getRecCollDett().setCodPolo(myForm.getCodPolo());
			myForm.getRecCollDett().setCodBib(myForm.getCodBib());
			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
					|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
			}else{
				myForm.getRecCollDett().setCodColloc(myForm.getCodCollocazione());
				myForm.getRecCollDett().setSpecColloc(myForm.getCodSpecificazione());
			}
			myForm.getRecCollDett().setConsistenza(myForm.getConsistenza());
			if (request.getAttribute("collocazioneEsistente") != null){
				collocazioneEsistente = true;
			}
			if (!this.updateCollocazione(myForm.getRecCollDett(), datiMappaModificati, collocazioneEsistente, myForm.getTicket())){
				//				if (!this.updateCollocazione(myForm.getRecCollDett(), collocazioneEsistente, myForm.getTicket())){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.invCollUpdErrore"));

				this.saveToken(request);
				myForm.setConferma(false);
				return mapping.getInputForward();
			}else{
				CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(myForm.getRecCollDett().getKeyColloc(), myForm.getTicket());
				myForm.getRecCollDett().setDataAgg(coll.getDataAgg());
				myForm.getRecCollDett().setUteAgg(coll.getUteAgg());
				myForm.setRecCollDett(coll);
				loadDatiCollocazione1(request, myForm);
				return impostaDatiPerModificaInv(mapping, request, myForm);

			}
		} catch (ValidationException e) {
			if (e.getMsg()!=null){
				if (e.getMsg().equals("msgCodLoc")){

					if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
							|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneGiaEsistente"));
						myForm.setConferma(false);
					}else{
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
						myForm.setConferma(true);
					}

					this.saveToken(request);
					myForm.setProv("modificaInv");
					return mapping.getInputForward();
				}
				if (e.getMsg().equals("msgCodLocEsistente")){

					if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
							|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
						//
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneGiaEsistente"));
						myForm.setConferma(false);

						this.saveToken(request);
						myForm.setProv("ok");
						return mapping.getInputForward();
					}else{
						if (myForm.getMsg() != null && myForm.getMsg().equals("confermaSalva")){
							myForm.setMsg("msgCodLocEsistente");
						}
					}
				}
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return mapping.getInputForward();
		} catch (DataException e) {//23/06/2010
			if (e.getMessage().equals("collCancellata")){

				myForm.setConferma(false);
				if (myForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| myForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
					if (myForm.getRecFormatiSezioni().getProgSerie() == -1){
						myForm.getRecFormatiSezioni().setProgSerie(0);
					}
					if (myForm.getRecFormatiSezioni().getProgNum() == -1){
						myForm.getRecFormatiSezioni().setProgNum(0);
					}
				}else if (myForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					//							if (myForm.getRecFormatiSezioni().getProgSerie() == -1){
					//								myForm.getRecFormatiSezioni().setProgSerie(0);
					//							}
					if (myForm.getRecFormatiSezioni().getProgNum() == -1){
						myForm.getRecFormatiSezioni().setProgNum(0);
					}
				}
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCancellata"));

				this.saveToken(request);
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	private ActionForward impostaDatiPerModificaInv(ActionMapping mapping,
			HttpServletRequest request, VaiAModificaCollForm myForm) {
		myForm.getRecCollDett().setCodColloc(myForm.getRecCollDett().getCodColloc());
		myForm.getRecCollDett().setSpecColloc(myForm.getRecCollDett().getSpecColloc());
		request.setAttribute("codPolo",myForm.getCodPolo());
		request.setAttribute("codBib",myForm.getCodBib());
		request.setAttribute("descrBib",myForm.getDescrBib());
		request.setAttribute("codSerie",myForm.getCodSerie());
		request.setAttribute("codInvent",myForm.getCodInvent());
		request.setAttribute("titolo",myForm.getTitolo());
		request.setAttribute("reticolo",myForm.getReticolo());
		request.setAttribute("recColl",myForm.getRecCollDett());
		request.setAttribute("tipoColloc", myForm.getRecCollDett().getTipoColloc());
		request.setAttribute("bid",myForm.getBid());
		Navigation navi = Navigation.getInstance(request);
		NavigationForward goForward = navi.goForward(mapping.findForward("modificaInv"));
		return goForward;
	}

	private String trattamentoLivello(String livello) {
		if (!ValidazioneDati.isFilled(livello))
			return ValidazioneDati.fillRight("", ' ', 7);

		livello = ValidazioneDati.fillRight(livello.trim(), ' ', 7);

		return livello.substring(0, 7);
	}

	public ActionForward scolloca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			InventarioVO keyInv = new InventarioVO();
			keyInv.setCodPolo(myForm.getCodPolo());
			keyInv.setCodBib(myForm.getCodBib());
			keyInv.setCodSerie(myForm.getCodSerie());
			keyInv.setCodInvent(myForm.getCodInvent());
			myForm.setRecInv(keyInv);
			String tipoOperazione = DocumentoFisicoCostant.S_SCOLLOCAZIONE_INV;
			Navigation navi = Navigation.getInstance(request);
			myForm.getRecCollDett().setUteAgg(navi.getUtente().getFirmaUtente());
			if (!this.updateCollocazioneScolloca(myForm.getRecInv(), myForm.getRecCollDett(),
					tipoOperazione,	myForm.getTicket())){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreScolloca"));

				this.saveToken(request);
			}
			CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(myForm.getRecCollDett().getKeyColloc(), myForm.getTicket());
			myForm.getRecCollDett().setDataAgg(coll.getDataAgg());
			myForm.getRecCollDett().setUteAgg(coll.getUteAgg());
			//
			request.setAttribute("codPolo",myForm.getRecInv().getCodPolo());
			request.setAttribute("codBib",myForm.getRecInv().getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());
			request.setAttribute("codSerie",myForm.getRecInv().getCodSerie());

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.scollocazioneAvvenuta"));

			this.saveToken(request);
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
			//se sono nella linea di esameColloc devo tornare alla pagina di esameColloc altrimenti back()
			return navi.goBack();
			//			return mapping.findForward("listaInventariDelTitolo");

			//			request.setAttribute("codSerie", myForm.getCodSerie());
			//			request.setAttribute("codInvent", myForm.getCodInvent());
			//			request.setAttribute("bid", myForm.getBid());
			//			request.setAttribute("reticolo", myForm.getReticolo());
			//			request.setAttribute("recColl", myForm.getRecCollDett());
			//			request.setAttribute("tipoOperazione", DocumentoFisicoCostant.S_SCOLLOCAZIONE_INV);
			//			return mapping.findForward("scolloca");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward cancellaInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {

			myForm.setProv("cancella");
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaCancellazione"));

			this.saveToken(request);
			myForm.setConferma(true);

			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward listaSupportoSez(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		SezioneCollocazioneVO sezione = null;
		try {
			if (myForm.isSezNonEsiste()){
				myForm.setSezNonEsiste(false);
				request.setAttribute("chiamante",mapping.getPath());
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				return mapping.findForward("lenteSez");
			}else{
				if (myForm.getCodSez() != null && (!myForm.getRecCollDett().getCodSez().trim().equals(myForm.getCodSez().trim()))){
					//ho digitato la sezione senza passare dalla lista
					//imposto i parametri e faccio get della sezione
					sezione = this.getSezioneDettaglio(myForm.getCodPolo(), myForm.getCodBib(), myForm.getCodSez().trim(), myForm.getTicket());
					if (sezione != null){
						myForm.setRecSezione(sezione);
					}
					//se la sezione esiste imposto NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA e faccio return "this.unspecified(mapping, form,	request, response)"
					if (sezione != null && myForm.getNoSezione().equals("siSezione")){
						request.setAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA, sezione);
						return this.unspecified(mapping, form, request, response);
					}else{
						//						return this.listaSupportoSez(mapping, form,	request, response);
						request.setAttribute("chiamante",mapping.getPath());
						request.setAttribute("codBib",myForm.getCodBib());
						request.setAttribute("descrBib",myForm.getDescrBib());
						return mapping.findForward("lenteSez");
					}
				}else{
					myForm.setSezNonEsiste(false);
					request.setAttribute("chiamante",mapping.getPath());
					request.setAttribute("codBib",myForm.getCodBib());
					request.setAttribute("descrBib",myForm.getDescrBib());
					return mapping.findForward("lenteSez");
				}
			}

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException e) {
			if (sezione == null){
				myForm.setSezNonEsiste(true);

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezNonEsistente"));

				return this.listaSupportoSez(mapping, form,	request, response);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private void loadDatiCollocazione1(HttpServletRequest request, ActionForm form) throws Exception {
		//viene chiamata quando si prospettano i dati della collocazione esistente

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		if (myForm.getRecCollDett().getCodBibSez()!= null){
			myForm.setLenteSez("lenteSez");
			myForm.setNoSezione("siSezione");
		}
		myForm.setCodSez(myForm.getRecCollDett().getCodSez().trim());
		myForm.setDescrSez(myForm.getRecCollDett().getDescr().trim());
		if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.SISTEMA_DI_CLASSIFICAZIONE);
			myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc().trim());
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
			myForm.setChiave("altro");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_NON_STRUTTURATA)) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_NON_STRUTTURATA);
			myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc().trim());
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
			myForm.setChiave("altro");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CHIAVE_TITOLO)) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CHIAVE_TITOLO);
			myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc().trim());
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
			myForm.setChiave("altro");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_NON_A_FORMATO);
			FormatiSezioniVO rec = new FormatiSezioniVO();
			rec.setCodFormato("00");
			rec.setProgSerie(0);
			if (myForm.getRecCollDett().getCodColloc().trim().equals("")){
				rec.setProgNum((0));
			}else{
				rec.setProgNum(Integer.parseInt(myForm.getRecCollDett().getCodColloc().trim()));
			}
			myForm.setRecFormatiSezioni(rec);
			myForm.setCodFormato("00");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
				|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)) {

			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)){
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_A_FORMATO);
			}
			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CONTINUAZIONE);
			}
			//			//carico i formati
			List listaForSez = null;
			listaForSez = this.getListaFormatiSezioni(myForm.getRecCollDett().getCodPoloSez(),
					myForm.getRecCollDett().getCodBibSez(), myForm.getCodSez(), myForm.getTicket(), form);
			if (listaForSez == null || listaForSez.size() <= 0) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuota"));

			} else {
				//quando si accede la prima volta si spresenta la collocazione così com'è
				// carico la lista dei formati nella combo
				myForm.setListaFormatiSezioni(listaForSez);
				//myForm.setListaCodiciFormati(this.caricaCombo.loadCodDescPezzi(myForm.getListaFormatiSezioni()));
				List listaCodiciFormati = new ArrayList(listaForSez);
				listaCodiciFormati.add(0, new FormatiSezioniVO());
				myForm.setListaCodiciFormati(listaCodiciFormati);
				if (myForm.getRecCollDett() != null && !myForm.getRecCollDett().getCodColloc().trim().equals("")){
					FormatiSezioniVO rec = null;
					rec = this.trovaFormatoSezione(myForm.getRecCollDett().getCodColloc().trim(), form);
					if (rec == null) {
						rec = new FormatiSezioniVO();
						rec.setCodFormato("");
						rec.setDescrFor("");
						myForm.setCodFormato("");
						myForm.setRecFormatiSezioni(rec);
						throw new DataException("controllareCodiceCollocazioneAFormato");
					}
					myForm.setCodFormato(rec.getCodFormato());
					rec.setProgSerie(Integer.parseInt(myForm.getRecCollDett().getCodColloc().substring(2).trim()));
					rec.setProgNum(Integer.parseInt(myForm.getRecCollDett().getSpecColloc().trim()));
					myForm.setRecFormatiSezioni(rec);
				}else{
					FormatiSezioniVO rec = new FormatiSezioniVO();
					rec.setCodFormato(((FormatiSezioniVO) myForm.getListaCodiciFormati().get(0)).getCodFormato());
					rec.setDescrFor(null);
					rec.setProgSerie(0);
					rec.setProgNum(0);
					myForm.setRecFormatiSezioni(rec);
					myForm.setCodFormato("");
				}
			}

			//
			//			String[] finoAlPuntoEVirgola = null;
			//			String dimensioneFormato = null;
			//			String codFor = null;
			//			if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())
			//					&& (!ValidazioneDati.strIsNull(myForm.getRecCollDett().getCodColloc()))){
			//				finoAlPuntoEVirgola = myForm.getTerzaParte().split(";");
			//				if (finoAlPuntoEVirgola.length > 1){
			//					if (finoAlPuntoEVirgola[1].indexOf("cm") < 0 ){
			//						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));
			//
			//					}else{
			//						dimensioneFormato = (finoAlPuntoEVirgola[1].substring(0, finoAlPuntoEVirgola[1].indexOf("cm"))).trim();
			//					}
			//				}else{
			//					if (finoAlPuntoEVirgola[0].indexOf("cm") < 0 ){
			//						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));
			//
			//					}else{
			//						dimensioneFormato = (finoAlPuntoEVirgola[0].substring(0, finoAlPuntoEVirgola[0].indexOf("cm"))).trim();
			//					}
			//				}//				finoAlPuntoEVirgola = myForm.getTerzaParte().split(";");
			//				//				dimensioneFormato = (finoAlPuntoEVirgola[1].substring(0, finoAlPuntoEVirgola[1].indexOf("cm"))).trim();
			//			}
			//
			//			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)){
			//				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_A_FORMATO);
			//			}
			//			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
			//				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CONTINUAZIONE);
			//			}
			//			//			//carico i formati
			//			List listaForSez = null;
			//			listaForSez = this.getListaFormatiSezioni(myForm.getRecCollDett().getCodPoloSez(),
			//					myForm.getRecCollDett().getCodBibSez(), myForm.getCodSez(), myForm.getTicket(), form);
			//			if (listaForSez == null || listaForSez.size() <= 0) {
			//				errors = new ActionMessages();
			//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuota"));
			//
			//			} else {
			//				if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())
			//						&& (!ValidazioneDati.strIsNull(myForm.getRecCollDett().getCodColloc()))
			//				){
			//					if (!ValidazioneDati.strIsNull(dimensioneFormato) && ValidazioneDati.strIsNumeric(dimensioneFormato)){
			//						for (int i = 0; i < listaForSez.size(); i++) {
			//							FormatiSezioniVO recForSez = (FormatiSezioniVO) listaForSez.get(i);
			//							String dimFor[] = recForSez.getDescrFor().split(FormatiSezioniVO.SEPARATOREFORSEZ);
			//							String dimDa = null;
			//							String dimA = null;
			//							if (dimFor.length > 1){
			//								dimDa = Integer.valueOf(dimFor[0]).toString();
			//								dimA = Integer.valueOf(dimFor[1]).toString();
			//								if (Integer.valueOf(dimensioneFormato) >= Integer.valueOf(dimDa)
			//										&& Integer.valueOf(dimensioneFormato) <= Integer.valueOf(dimA)){
			//									codFor = recForSez.getCodFormato();
			//									break;
			//								}
			//							}
			//						}
			//					}
			//				}
			//				// carico la lista dei formati nella combo
			//				for (int i = 0; i < listaForSez.size(); i ++){
			//					FormatiSezioniVO rec = (FormatiSezioniVO) listaForSez.get(i);
			//					String descrFor [] = rec.getDescrFor().split(";");
			//					if (descrFor.length == 3){
			//						rec.setDescrFor(descrFor[2]);
			//					}else if (descrFor.length == 1){
			//						rec.setDescrFor(descrFor[0]);
			//					}else if (descrFor.length == 2){
			//						rec.setDescrFor("");
			//					}
			//				}
			//				myForm.setListaFormatiSezioni(listaForSez);
			//				//myForm.setListaCodiciFormati(this.caricaCombo.loadCodDescPezzi(myForm.getListaFormatiSezioni()));
			//				List listaCodiciFormati = new ArrayList(listaForSez);
			//				listaCodiciFormati.add(0, new FormatiSezioniVO());
			//				myForm.setListaCodiciFormati(listaCodiciFormati);
			//				assegnaFormato(request, form, myForm, errors, codFor);
			//			}
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_STRUTTURATA)
				&& myForm.getNoSezione().equals("siSezione")) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_STRUTTURATA);
			if (myForm.getRecCollDett().getCodColloc().equals(null) || myForm.getRecCollDett().getCodColloc().trim().equals("")){
				myForm.setLivello1("");
				myForm.setLivello2("");
				myForm.setLivello3("");
				myForm.setChiave("altro");
			}else{
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_STRUTTURATA);
				myForm.setLivello1(myForm.getRecCollDett().getCodColloc().substring(0,7).trim());
				myForm.setLivello2(myForm.getRecCollDett().getCodColloc().substring(8,15).trim());
				myForm.setLivello3(myForm.getRecCollDett().getCodColloc().substring(16).trim());
				myForm.setChiave("altro");
			}
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
		}

		myForm.setConsistenza(myForm.getRecCollDett().getConsistenza().trim());
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * VaiAModificaCollAction.java
	 * assegnaFormato
	 * void
	 * @param request
	 * @param form
	 * @param myForm
	 * @param errors
	 * @param codFor
	 *
	 *
	 */
	private void assegnaFormato(HttpServletRequest request, ActionForm form,
			VaiAModificaCollForm myForm, ActionMessages errors, String codFor) {
		//metodo usato
		boolean c2 = (request.getAttribute("collocazione2") != null && request
				.getAttribute("collocazione2").equals("collocazione2"));
		if (codFor != null) {
			if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())
					&& (c2 || !ValidazioneDati.strIsNull(myForm.getRecCollDett().getCodColloc()))) {
				FormatiSezioniVO rec = null;
				rec = this.trovaFormatoSezione(codFor, form);
				if (rec == null ) {
					rec = formatoNonTrovato(request, myForm, errors);
				}else{
					if (c2 && (ValidazioneDati.strIsNull(myForm.getRecCollDett().getCodColloc()))){
						rec = new FormatiSezioniVO();
						rec.setCodFormato(((FormatiSezioniVO) myForm.getListaCodiciFormati().get(0)).getCodFormato());
						rec.setDescrFor(null);
						rec.setProgSerie(0);
						rec.setProgNum(0);
						myForm.setRecFormatiSezioni(rec);
						myForm.setCodFormato("");
					}else{
						myForm.setCodFormato(rec.getCodFormato());
					}
				}
				if (rec.getDescrFor() != null){
					String descrFor [] = rec.getDescrFor().split(";");
					if (descrFor.length == 3){
						rec.setDescrFor(descrFor[2]);
					}else if (descrFor.length == 1){
						rec.setDescrFor(descrFor[0]);
					}else if (descrFor.length == 2){
						rec.setDescrFor("");
					}
				}else{
					rec.setDescrFor(null);
				}
				myForm.setRecFormatiSezioni(rec);
			}
		}else{
			if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())
					&& (c2 || !ValidazioneDati.strIsNull(myForm.getRecCollDett().getCodColloc()))) {
				FormatiSezioniVO rec = null;
				rec = this.trovaFormatoSezione(myForm.getRecCollDett().getCodColloc().trim(), form);
				if (rec == null) {
					rec = formatoNonTrovato(request, myForm, errors);
				}else{
					myForm.setCodFormato(rec.getCodFormato());
					rec.setProgSerie(0);
					rec.setProgNum(0);
					//					rec.setProgSerie(Integer.parseInt(myForm.getRecCollDett().getCodColloc().substring(2).trim()));
					//					rec.setProgNum(Integer.parseInt(myForm.getRecCollDett().getSpecColloc().trim()));
				}
				if (rec.getDescrFor() != null){
					String descrFor [] = rec.getDescrFor().split(";");
					if (descrFor.length == 3){
						rec.setDescrFor(descrFor[2]);
					}else if (descrFor.length == 1){
						rec.setDescrFor(descrFor[0]);
					}else if (descrFor.length == 2){
						rec.setDescrFor("");
					}
				}else{
					rec.setDescrFor(null);
				}
				myForm.setRecFormatiSezioni(rec);
			}else{
				FormatiSezioniVO rec = new FormatiSezioniVO();
				rec.setCodFormato(((FormatiSezioniVO) myForm.getListaCodiciFormati().get(0)).getCodFormato());
				rec.setDescrFor(null);
				rec.setProgSerie(0);
				rec.setProgNum(0);
				myForm.setRecFormatiSezioni(rec);
				myForm.setCodFormato("");
				if (errors.isEmpty()){
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoNonDefinito"));

				}
			}
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * VaiAModificaCollAction.java
	 * formatoNonTrovato
	 * FormatiSezioniVO
	 * @param request
	 * @param myForm
	 * @param errors
	 * @return
	 *
	 *
	 */
	private FormatiSezioniVO formatoNonTrovato(HttpServletRequest request,
			VaiAModificaCollForm myForm, ActionMessages errors) {
		FormatiSezioniVO rec;
		rec = new FormatiSezioniVO();
		if (errors.isEmpty()){
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoNonDefinito"));

		}
		rec.setCodFormato(((FormatiSezioniVO) myForm.getListaCodiciFormati().get(0)).getCodFormato());
		rec.setDescrFor(null);
		rec.setProgSerie(0);
		rec.setProgNum(0);
		myForm.setRecFormatiSezioni(rec);
		myForm.setCodFormato("");
		return rec;
	}

	private void loadDatiCollocazione2(HttpServletRequest request, ActionForm form) throws Exception {
		//viene chiamata quando si prospettano i dati della collocazione modificata
		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		if (myForm.getRecCollDett().getCodBibSez()!= null){
			myForm.setLenteSez("lenteSez");
			myForm.setNoSezione("siSezione");
		}
		myForm.setCodSez(myForm.getRecCollDett().getCodSez().trim());
		myForm.setDescrSez(myForm.getRecCollDett().getDescr().trim());
		if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.SISTEMA_DI_CLASSIFICAZIONE);
			myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc().trim());
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
			myForm.setChiave("altro");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_NON_STRUTTURATA)) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_NON_STRUTTURATA);
			myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc().trim());
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
			myForm.setChiave("altro");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_NON_A_FORMATO);
			FormatiSezioniVO rec = new FormatiSezioniVO();
			rec.setCodFormato("00");
			rec.setProgSerie(0);
			if (myForm.getRecCollDett().getCodColloc().trim().equals("")){
				rec.setProgNum((0));
			}else{
				rec.setProgNum(Integer.parseInt(myForm.getRecCollDett().getCodColloc().trim()));
			}
			myForm.setRecFormatiSezioni(rec);
			myForm.setCodFormato("00");
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
				|| myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)) {
			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)){
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_A_FORMATO);
			}
			if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CONTINUAZIONE);
			}

			List listaForSez = null;
			listaForSez = this.getListaFormatiSezioni(myForm.getRecCollDett().getCodPoloSez(),
					myForm.getRecCollDett().getCodBibSez(), myForm.getCodSez(), myForm.getTicket(), form);
			if (listaForSez == null || listaForSez.size() <= 0) {


				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuota"));

			} else {

				String[] finoAlPuntoEVirgola = myForm.getTerzaParte().split(";");
				String dimensioneFormato = null;
				String codFor = null;
				if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())){
					if (finoAlPuntoEVirgola.length > 1){
						finoAlPuntoEVirgola = myForm.getTerzaParte().split(";");
						if (finoAlPuntoEVirgola[1].indexOf("cm") < 0 ){
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));

						}else{
							dimensioneFormato = (finoAlPuntoEVirgola[1].substring(0, finoAlPuntoEVirgola[1].indexOf("cm"))).trim();
						}
					}else{
						if (finoAlPuntoEVirgola[0].indexOf("cm") < 0 ){
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));

						}else{
							dimensioneFormato = (finoAlPuntoEVirgola[0].substring(0, finoAlPuntoEVirgola[0].indexOf("cm"))).trim();
						}
					}
				}
				//			//carico i formati
				if (!ValidazioneDati.strIsNull(myForm.getTerzaParte())){
					if (!ValidazioneDati.strIsNull(dimensioneFormato) && ValidazioneDati.strIsNumeric(dimensioneFormato)){
						for (int i = 0; i < listaForSez.size(); i++) {
							FormatiSezioniVO recForSez = (FormatiSezioniVO) listaForSez.get(i);
							String dimFor[] = recForSez.getDescrFor().split(FormatiSezioniVO.SEPARATOREFORSEZ);
							String dimDa = null;
							String dimA = null;
							if (dimFor.length > 1){
								dimDa = Integer.valueOf(dimFor[0]).toString();
								dimA = Integer.valueOf(dimFor[1]).toString();
								if (Integer.valueOf(dimensioneFormato) >= Integer.valueOf(dimDa)
										&& Integer.valueOf(dimensioneFormato) <= Integer.valueOf(dimA)){
									codFor = recForSez.getCodFormato();
									break;
								}
							}
						}

					}
				}
				if (codFor == null){
					if (LinkableTagUtils.getErrors(request).isEmpty()){
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoNonDefinito"));

					}
				}
				// carico la lista dei formati nella combo
				for (int i = 0; i < listaForSez.size(); i ++){
					FormatiSezioniVO rec = (FormatiSezioniVO) listaForSez.get(i);
					String descrFor [] = rec.getDescrFor().split(";");
					if (descrFor.length == 3){
						rec.setDescrFor(descrFor[2]);
					}else if (descrFor.length == 1){
						rec.setDescrFor(descrFor[0]);
					}else if (descrFor.length == 2){
						rec.setDescrFor("");
					}
				}
				myForm.setListaFormatiSezioni(listaForSez);
				List listaCodiciFormati = new ArrayList(listaForSez);
				listaCodiciFormati.add(0, new FormatiSezioniVO());
				myForm.setListaCodiciFormati(listaCodiciFormati);
				request.setAttribute("collocazione2", "collocazione2");
				assegnaFormato(request, form, myForm, LinkableTagUtils.getErrors(request), codFor);
			}
		}else if (myForm.getRecCollDett().getTipoColloc().equals(DocumentoFisicoCostant.COD_ESPLICITA_STRUTTURATA)
				&& myForm.getNoSezione().equals("siSezione")) {
			myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_STRUTTURATA);
			if (myForm.getRecCollDett().getCodColloc().equals(null) || myForm.getRecCollDett().getCodColloc().trim().equals("")){
				myForm.setLivello1("");
				myForm.setLivello2("");
				myForm.setLivello3("");
				myForm.setChiave("altro");
			}else{
				myForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_STRUTTURATA);
				myForm.setLivello1(myForm.getRecCollDett().getCodColloc().substring(0,7).trim());
				myForm.setLivello2(myForm.getRecCollDett().getCodColloc().substring(8,15).trim());
				myForm.setLivello3(myForm.getRecCollDett().getCodColloc().substring(16).trim());
				myForm.setChiave("altro");
			}
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc().trim());
		}

		myForm.setConsistenza(myForm.getRecCollDett().getConsistenza().trim());
	}

	private FormatiSezioniVO trovaFormatoSezione(String codFormato, ActionForm form) {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		for (int i = 0; i < myForm.getListaFormatiSezioni().size(); i++) {
			FormatiSezioniVO rec = (FormatiSezioniVO) myForm.getListaFormatiSezioni().get(i);
			codFormato = ValidazioneDati.fillRight(codFormato, ' ', 2);
			if (rec.getCodFormato().equals(codFormato.substring(0,2)))
				return rec;
		}

		return null;
	}


	private boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean datiMappaModificati, boolean collocazioneEsistente, String ticket) throws Exception {
//		private boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean collocazioneEsistente, String ticket) throws Exception {
		boolean ret;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateCollocazione(recColl, datiMappaModificati, collocazioneEsistente, ticket);
//		ret = factory.getGestioneDocumentoFisico().updateCollocazione(recColl, collocazioneEsistente, ticket);
		return ret;
	}
	private CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws Exception {
		CollocazioneDettaglioVO coll;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		coll = factory.getGestioneDocumentoFisico().getCollocazioneDettaglio(keyLoc, ticket);
		return coll;
	}
	private DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String ticket, ActionForm form)
	throws Exception {
		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		DatiBibliograficiCollocazioneVO reticolo;
		String bibliotecaOperante = myForm.getCodPolo() + myForm.getCodBib();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		reticolo = factory.getGestioneDocumentoFisico().getAnaliticaPerCollocazione(bid, bibliotecaOperante, ticket);
		if (reticolo == null)  {
			myForm.setNoReticolo(true);
		}else{
			myForm.setNoReticolo(false);
		}
		return reticolo;
	}
	private boolean deleteInventario(InventarioVO recInv, CollocazioneVO recColl, String tipoOperazione, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().deleteInventario(recInv, recColl, tipoOperazione, ticket);
		return ret;
	}
	private boolean updateCollocazioneScolloca(InventarioVO recInv, CollocazioneVO recColl, String tipoOperazione, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateCollocazioneScolloca(recInv, recColl, tipoOperazione, ticket);
		return ret;
	}
	private List getListaFormatiSezioni(String codPolo, String codBib, String codSez, String ticket, ActionForm form) throws Exception {
		VaiAModificaCollForm currentForm = (VaiAModificaCollForm) form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<FormatiSezioniVO> formati = factory.getGestioneDocumentoFisico().getListaFormatiSezioni(codPolo, codBib,
			codSez, currentForm.getTicket());
		if (!ValidazioneDati.isFilled(formati) ) {
			currentForm.setNoFormati(true);
		} else
			formattaDescrizione(formati);

		return formati;
	}
	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
	private String getChiaviTitoloAutore(String tipo, String bidVid, String ticket) throws Exception {
		String chiave = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		chiave = factory.getGestioneDocumentoFisico().getChiaviTitoloAutore(tipo, bidVid, ticket);
		return chiave;
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
		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (idCheck.equalsIgnoreCase("soggetti") ){
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_RICERCA_CLASSI_SOGGETTI_PER_COLLOCAZIONE, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	private List getTitolo(String bid, String ticket) throws Exception {
		List titolo = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAModificaCollForm myForm = (VaiAModificaCollForm) form;
		request.setAttribute("currentForm", form);
		try {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaSalva"));

			this.saveToken(request);
			myForm.setConferma(true);
			myForm.setTasto("aggiornaInIndice");

			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}

	}

	private void formattaDescrizione(List<FormatiSezioniVO> formati) {
		//almaviva5_20141216
		for (FormatiSezioniVO formato : formati) {
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

	}

}
