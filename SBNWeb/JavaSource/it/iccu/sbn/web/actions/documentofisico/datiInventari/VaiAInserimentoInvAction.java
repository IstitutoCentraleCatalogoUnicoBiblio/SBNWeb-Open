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
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.VaiAInserimentoInvForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
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

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;

public class VaiAInserimentoInvAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(VaiAInserimentoInvAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.bottone.ordini", "ordini");
		map.put("documentofisico.bottone.creaecolloca", "collocazione");
		map.put("documentofisico.bottone.collocazioneVeloce", "collocazioneVeloce");
		map.put("documentofisico.aggiorna", "aggiorna");
		map.put("documentofisico.bottone.crea", "ok");
//		map.put("documentofisico.bottone.ok", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.lsBib", "listaSupporto");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {


		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm) form;
		String provenienza = (String) request.getAttribute("prov");
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Navigation navi = Navigation.getInstance(request);
		if (navi.getUtente().getCodBib().equals(currentForm.getCodBib())){
			try {
				currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				String cdSerieDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_SERIE);
				if (!ValidazioneDati.strIsEmpty(cdSerieDefault) ) {
					SerieVO serieDettaglio = this.getSerieDettaglio(
							currentForm.getCodPolo(), currentForm.getCodBib(), cdSerieDefault.toUpperCase(), currentForm.getTicket());

					if (serieDettaglio == null) {
						LinkableTagUtils.resetErrors(request);
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.serieDefaultNonEsiste"));

						return navi.goBack(true);
					} else {
						currentForm.setCodSerieDefault(cdSerieDefault);
						currentForm.getRecInv().setCodSerie(cdSerieDefault);
					}

				} else {
					if (provenienza != null && provenienza.equals("CV")) {
						LinkableTagUtils.resetErrors(request);
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.serieDefaultNonImpostata"));

						return navi.goBack(true);
					}
				}
				String tipoPrgDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_TP_PRG_INV);
				if (!ValidazioneDati.strIsEmpty(tipoPrgDefault)) {
					currentForm.setTipoPrgDefault(tipoPrgDefault);
					currentForm.setTipoOperazione(tipoPrgDefault);
				} else {
					// sezione di default non impostata
					LinkableTagUtils.resetErrors(request);
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.tipoPrgDefaultNonImpostata"));

					return null;
				}
			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

				return navi.goBack(true);
			}
			return null;
		}else{
			return null;
		}
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//setto il token per le transazioni successive
		this.saveToken(request);
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		Navigation navi = Navigation.getInstance(request);
		try {
			if (Navigation.getInstance(request).isFromBar() ){
				if (request.getAttribute("prov") != null &&
						(request.getAttribute("prov").equals("modInvColl"))
						|| request.getAttribute("prov") != null &&
						(request.getAttribute("prov").equals("insColl"))){
					request.setAttribute("prov", request.getAttribute("prov"));
					return this.chiudi(mapping, form, request, response);
				}
				return mapping.getInputForward();
			}


			boolean bibChanged = false;
			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				//almaviva5_20111110 #4729
				bibChanged = !ValidazioneDati.equals(currentForm.getCodBib(), bib.getCod_bib());
				//
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("titolo", currentForm.getTitolo());
				request.setAttribute("codBibDaLista",null);
				return mapping.findForward("ok");
			}
			if(request.getAttribute("codBib") != null && request.getAttribute("descrBib")!= null) {
				// provengo dalla lista biblioteche
				currentForm.setCodBib((String)request.getAttribute("codBib"));
				currentForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			if (request.getAttribute("prov") != null &&
					(request.getAttribute("prov").equals("provChiudi"))){
				request.setAttribute("prov", request.getAttribute("prov"));
				InventarioVO rec = new InventarioVO();
				currentForm.setRecInv(rec);
				currentForm.getRecInv().setNumInv(1);
			}
			if (!currentForm.isSessione()) {
				currentForm.setTicket(navi.getUserTicket());
				currentForm.setCodPolo(navi.getUtente().getCodPolo());
				currentForm.setCodBib(coalesce((String)request.getAttribute("codBib"), navi.getUtente().getCodBib()));
				currentForm.setDescrBib(coalesce((String)request.getAttribute("descrBib"), navi.getUtente().getBiblioteca()));
				if (!this.checkAttivita(request, currentForm, "df")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.utenteNonAbilitato"));

					return navi.goBack(true);
				}
				//almaviva5_20111110 #4729
				List listaSerie = this.getListaSerieDate(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getTicket(), form);
				currentForm.setListaSerie(listaSerie);
				//
				ActionForward loadDefault = loadDefault(request, mapping, form);
				if (loadDefault != null){
					return loadDefault;
				}
//				//momentaneo
//				currentForm.setDisableDataIngresso(true);
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaInventariOrdine")){
				request.setAttribute("codBib", request.getAttribute("codBib"));
				request.setAttribute("descrBib", request.getAttribute("descrBib"));
				request.setAttribute("bidInv", request.getAttribute("bidInv"));
				request.setAttribute("titoloInv", request.getAttribute("titoloInv"));
				request.setAttribute("bidOrd", request.getAttribute("bidOrd"));
				request.setAttribute("titoloOrd", request.getAttribute("titoloOrd"));
				request.setAttribute("inventario", request.getAttribute("inventario"));
				request.setAttribute("prov", request.getAttribute("prov"));
//				return mapping.getInputForward();
			}
			if (request.getAttribute("passaggioListaSuppOrdiniVO") != null){
				ListaSuppOrdiniVO ordine = ((ListaSuppOrdiniVO)request.getAttribute("passaggioListaSuppOrdiniVO"));
				if (ordine.getSelezioniChiamato() != null && ordine.getSelezioniChiamato().size()==1){
					OrdiniVO ordineSel = ordine.getSelezioniChiamato().get(0);
					request.setAttribute("prov", "listaSuppInvOrd");
					//dati ordine
					request.setAttribute("codTipoOrd", ordineSel.getTipoOrdine());
					request.setAttribute("annoOrd", ordineSel.getAnnoOrdine());
					request.setAttribute("codOrd", ordineSel.getCodOrdine());
					request.setAttribute("codBibO", ordineSel.getCodBibl());
					request.setAttribute("titOrd", ordineSel.getTitolo());
					request.setAttribute("codBibF", "");
					//dati notizia corrente
					request.setAttribute("descrBib", currentForm.getDescrBib());
					request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("titolo", currentForm.getTitolo());

					return mapping.findForward("listaInventariOrdine");
				}else{
					//provengo da indietro di lista supporto ordini
					return mapping.getInputForward();
				}
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}else{
				//cambio serie da combo
				if (currentForm.getTest() != null && currentForm.getTest().equals("serie")){
					if (currentForm.getListaSerie() != null && currentForm.getListaSerie().size() > 0){
						assegnaDateIngresso(currentForm, request);
					}
				}else {
					this.controllaTipoOperazione(request, form);
					assegnaDateIngresso(currentForm, request);
				}
//				if (request.getAttribute("prov") != null ){
//					request.setAttribute("bid", currentForm.getBid());
//					request.setAttribute("titolo", currentForm.getTitolo());
//				}else{
//					return mapping.getInputForward();
//				}
			}
			if (request.getAttribute("prov") != null ){
				if (currentForm.getBid() != null){
					request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("titolo", currentForm.getTitolo());
				}
				if (request.getAttribute("prov").equals("ordine") || request.getAttribute("prov").equals("ordineIns")){
					if (request.getAttribute("codTipoOrd") != null  &&
							//						request.getAttribute("codPoloO") != null &&
							request.getAttribute("codBibO") != null &&
							request.getAttribute("annoOrd") != null &&
							request.getAttribute("bid") != null &&
							request.getAttribute("codFornitore") != null &&
							request.getAttribute("codOrd") != null ){
						currentForm.setCodTipoOrd((String)request.getAttribute("codTipoOrd"));
						//					currentForm.setCodPoloO((String)request.getAttribute("codPoloO"));
						if (!request.getAttribute("codBibO").equals(currentForm.getCodBib())){
							currentForm.setCodBib((String)request.getAttribute("codBibO"));
						}
						currentForm.setCodBibO((String)request.getAttribute("codBibO"));
						currentForm.setAnnoOrd(Integer.valueOf((String)request.getAttribute("annoOrd")));
						currentForm.setBid((String)request.getAttribute("bid"));
						StrutturaCombo fornitore = ((StrutturaCombo)request.getAttribute("codFornitore"));
						if (fornitore.getDescrizione() != null && fornitore.getDescrizione().equals("fornitore non presente su base dati")){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.fornitoreNonPresente"));

							return navi.goBack(true);
						}
						currentForm.setCodFornitore(Integer.valueOf(fornitore.getCodice()));
						currentForm.setDescrFornitore((fornitore.getDescrizione()));
						currentForm.setCodOrd(Integer.valueOf((String)request.getAttribute("codOrd")));
						currentForm.getRecInv().setNumInv(1);
						navi.setDescrizioneX("Inserimento inventari da Ordine");
						navi.setTesto("Inserimento inventari da Ordine");
						currentForm.setProv((String)request.getAttribute("prov"));
						List lista = this.getTitolo(currentForm.getBid(), currentForm.getTicket());
						if (lista != null){
							//if (lista.size() == 1){
							currentForm.setRecTitolo((TitoloVO)lista.get(0));
							currentForm.setTitolo(currentForm.getRecTitolo().getIsbd());
							//}
						}else{

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreGetTitolo"));

							return navi.goBack(true);
						}
					}
				}
			}else{
					if (request.getAttribute("codBib") != null  &&
							request.getAttribute("descrBib") != null &&
							request.getAttribute("bid") != null &&
							request.getAttribute("titolo") != null ){
						currentForm.setCodBib((String)request.getAttribute("codBib"));
						currentForm.setDescrBib((String)request.getAttribute("descrBib"));
						currentForm.setBid((String)request.getAttribute("bid"));
						List lista = this.getTitolo(currentForm.getBid(), currentForm.getTicket());
						if (lista != null){
							//if (lista.size() == 1){
							currentForm.setRecTitolo((TitoloVO)lista.get(0));
							currentForm.setTitolo(currentForm.getRecTitolo().getIsbd());
							//}
						}else{

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreGetTitolo"));

							return navi.goBack(true);
						}
					}
				}

			// leggo serie inventariale per caricare combo lista serie
			List listaSerie = null;
			//almaviva5_20111110 #4729
			if (bibChanged)
				listaSerie = this.getListaSerieDate(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getTicket(), form);
			else
				listaSerie = currentForm.getListaSerie();

			if (listaSerie == null || listaSerie.size() <= 0) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuotaSerie"));

				request.setAttribute("noListaSerie","noListaSerie");
				return navi.goBack(true);
			} else {
//				if (currentForm.getReticolo() == null){
//					currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBid(), currentForm.getTicket(), form));
//				}

				currentForm.setListaSerie(listaSerie);
				// carico la lista delle serie nella combo
				CaricamentoCombo caricaCombo = new CaricamentoCombo();
				currentForm.setListaComboSerie(caricaCombo.loadCodice(listaSerie));
				currentForm.getRecInv().setCodPolo(currentForm.getCodPolo());
				currentForm.getRecInv().setCodBib(currentForm.getCodBib());
				currentForm.getRecInv().setBid(currentForm.getBid());
				/*almaviva5_20111110 #4729
				if (currentForm.getCodSerieDefault() == null){
					currentForm.getRecInv().setCodSerie(((CodiceVO)currentForm.getListaComboSerie().get(0)).getCodice());
				}else{
					currentForm.getRecInv().setCodSerie(currentForm.getCodSerieDefault());
				}
				*/
				currentForm.getRecInv().setCodInvent(0);
				if (currentForm.getTipoOperazione() != null && currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE)){
					//almaviva2 bug 0004950 collaudo
					currentForm.getRecInv().setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
					currentForm.setDisable(true);
				}
				currentForm.setDisableDataIngresso(true);
				currentForm.getRecInv().setPrecInv(null);
				currentForm.setTipoOperazione(currentForm.getTipoOperazione());
				if (request.getAttribute("prov") != null &&
						(request.getAttribute("prov").equals("listaInventariOrdine"))){
					if (request.getAttribute("inventario") != null){
						InventarioVO inv = ((InventarioVO)request.getAttribute("inventario"));
						currentForm.setProv((String)request.getAttribute("prov"));
						currentForm.setTipoOperazione(DocumentoFisicoCostant.S_INVENTARIO_PRESENTE);
						currentForm.getRecInv().setCodSerie(inv.getCodSerie());
						currentForm.getRecInv().setCodInvent(inv.getCodInvent());
						currentForm.setDisable(true);
						this.ok(mapping, currentForm, request, response);
						if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("msgS")){
							return mapping.getInputForward();
						}
						return navi.goBack(true);
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariOrdineNonTrovato"));

					}
				}
			}
		} catch (ValidationException e) {

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

	private static final boolean checkIntervalloEsterni(int numInv, String inizioMan, String fineMan) {
		int inizio = Integer.parseInt(inizioMan);
		int fine = Integer.parseInt(fineMan);
		if (inizio == fine)
			return true;
		return (numInv < inizio) || (numInv > fine);
	}

	private static final boolean checkIntervalloInterni(int numInv, String inizioMan, String fineMan) {
		int inizio = Integer.parseInt(inizioMan);
		int fine = Integer.parseInt(fineMan);
		if (inizio <= numInv  && numInv <= fine){
			return true;
		}else{
			return false;
		}
//		return (numInv >= inizio) || (numInv <= fine);
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		try {
			if (currentForm.getProv() != null && !currentForm.getProv().equals("listaInventariOrdine")){
				currentForm.setNInv(currentForm.getRecInv().getNumInv());
//				currentForm.getRecInv().setCodPoloO(currentForm.getCodPoloO());
				currentForm.getRecInv().setCodBibO(currentForm.getCodBibO());
				currentForm.getRecInv().setCodTipoOrd(currentForm.getCodTipoOrd());
				if (currentForm.getCodTipoOrd().equals("V")){
					currentForm.getRecInv().setTipoAcquisizione("A");
				}else{
					currentForm.getRecInv().setTipoAcquisizione(currentForm.getCodTipoOrd());
				}
				currentForm.getRecInv().setAnnoOrd(String.valueOf(currentForm.getAnnoOrd()));
				currentForm.getRecInv().setCodOrd(String.valueOf(currentForm.getCodOrd()));
				currentForm.getRecInv().setCodFornitore(String.valueOf(currentForm.getCodFornitore()));
				currentForm.getRecInv().setDescrFornitore(currentForm.getDescrFornitore());
				currentForm.getRecInv().setPrecInv("");
				currentForm.setTipoOperazione(DocumentoFisicoCostant.O_ATTIVATO_DA_GEST_ORDINI);
			}else{
				ActionForward forward = controlloTipoInventario(mapping, request, currentForm);
				// se forward é valorizzato c'é stato un errore nei controlli
				if (forward != null)
					return forward;
				//continuo con l'inserimento
				currentForm.setNInv(1);
			}
			boolean conferma = false;
			try{
				List invCreati = null;
				//
				if(currentForm.getCodOrd() != 0) {
					currentForm.getRecInv().setCodBibO(currentForm.getCodBibO());
					currentForm.getRecInv().setCodTipoOrd(currentForm.getCodTipoOrd());
					currentForm.getRecInv().setAnnoOrd(String.valueOf(currentForm.getAnnoOrd()));
					currentForm.getRecInv().setCodOrd(String.valueOf(currentForm.getCodOrd()));
					currentForm.getRecInv().setCodFornitore(String.valueOf(currentForm.getCodFornitore()));
					//
				}

				currentForm.getRecInv().setUteIns(Navigation.getInstance(request).getUtente().getFirmaUtente());
				invCreati = this.insertInventario(currentForm.getRecInv(), currentForm.getTipoOperazione(),
						currentForm.getNInv(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), form);
				if (invCreati == null ||  invCreati.size() <1)  {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erratoInserimento"));

					this.saveToken(request);
					//					currentForm.setConferma(true);
					return mapping.getInputForward();
				}else{
					// Intervento interno ALMAVIVA2 settembre 2018
			   		// La localizzazione per Gestione deve essere fatta sia per il bid oggetto di inventariazione
			   		// che per gli spogli agganciati che altrimenti rimarrebbero gialli alla successiva fase di
			   		// collocazione che inserirebbe la localizzazione per possesso ma senza quella per Gestione.
					//localizzaGestione(request, currentForm);//
					if (currentForm.getReticolo() == null){
						currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBid(), currentForm.getTicket(), form));
					}
			   		localizzaGestioneAncheSpogli(request, currentForm);
				}

				//
				if (currentForm.getProv() != null &&
						(currentForm.getProv().equals("ordine") || currentForm.getProv().equals("ordineIns"))){
					if (currentForm.getRecInv().getNumInv() == invCreati.size()){
						request.setAttribute("prov", currentForm.getProv());
						request.setAttribute("invCreati", invCreati);
						request.setAttribute("codBibO", currentForm.getCodBibO());
						request.setAttribute("bid", currentForm.getBid());
						request.setAttribute("descrFornitore", currentForm.getDescrFornitore());
						Navigation.getInstance(request).purgeThis();
						return mapping.findForward("modInvColl");
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.NumeroInvCreatiDiversoDaNumeroInvDaCreare"));

						this.saveToken(request);
						return Navigation.getInstance(request).goBack(true);
					}
				}else{
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));
					this.saveToken(request);
					return Navigation.getInstance(request).goBack();
				}
			} catch (ValidationException e) {
				if (e.getMsg()!=null){
					if (e.getMsg().equals("msgS")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage(), e.getBid(), e.getIsbd()));

						this.saveToken(request);
						currentForm.setConferma(true);
						request.setAttribute("prov", "msgS");
						currentForm.getRecInv().setPrecInv(e.getPrecInv());
						return mapping.getInputForward();
					}else if ((e.getMsg().equals("msgC"))){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						this.saveToken(request);
						currentForm.setConferma(true);
						return mapping.getInputForward();
					}else if ((e.getMsg().equals("msgGiaPresente"))){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						this.saveToken(request);
//						currentForm.setConferma(true);
						return mapping.findForward("chiudi");
					}else if ((e.getMsg().equals("msgDismesso"))){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						this.saveToken(request);
//						currentForm.setConferma(true);
						return mapping.findForward("chiudi");
					}
				}

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			} catch (Exception e) { // altri tipi di errore

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			}

		} catch (Exception e) {

			return mapping.getInputForward();
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * VaiAInserimentoInvAction.java
	  * controlloTipoInventario
	  * ActionForward
	  * @param mapping
	  * @param request
	  * @param myForm
	  * @return
	  *
	  *
	 */
	private ActionForward controlloTipoInventario(ActionMapping mapping,
			HttpServletRequest request, VaiAInserimentoInvForm currentForm) {
		if (!currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE) ||
				!currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.P_PROGRESSIVO_PREGRESSO)){
			//controlla che i numeri inseriti non ricadano negli intervalli riservati
			int codInvent = currentForm.getRecInv().getCodInvent();
			if (codInvent > 0){
				for (int i = 0; i < currentForm.getListaSerie().size(); i++) {
					SerieVO rec = (SerieVO) currentForm.getListaSerie().get(i);
					if ((currentForm.getRecInv().getCodSerie().trim()).equals(rec.getCodSerie().trim())){
						if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.S_INVENTARIO_PRESENTE)){
							//gli stessi controlli vengono effettuati in assegnaDateIngresso
							if (codInvent < Integer.parseInt(rec.getProgAssInv())){
								//nessun altro controllo
							}else if (codInvent > Integer.parseInt(rec.getProgAssInv())){
								if (  codInvent > 900000000 && codInvent <= Integer.valueOf(rec.getPregrAssInv())){
								}else if (codInvent < 900000000){
									this.controllaEstremiIntervalli(rec);
									if (checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan1(), rec.getFineMan1() )
											|| checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan2(), rec.getFineMan2())
											|| checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan3(), rec.getFineMan3())
											|| checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan4(), rec.getFineMan4()) ) {
									}else{
										//non appartiene ad alcun intervallo

										LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ilProgressivoIndicatoDeveAppartenereAdUnInventarioRiservato"));

										return mapping.getInputForward();
									}
								}
							}
						}else if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.N_INVENTARIO_NON_PRESENTE)){
							//gli stessi controlli vengono effettuati in assegnaDateIngresso
							if (codInvent < Integer.parseInt(rec.getProgAssInv())){
								this.impostaDataIngresso(currentForm, codInvent,	rec);
							}else if (codInvent > Integer.parseInt(rec.getProgAssInv())){
								if (  codInvent > 900000000 && codInvent <= Integer.valueOf(rec.getPregrAssInv())){
									//va bene
								}else if (codInvent < 900000000){
									if (checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan1(), rec.getFineMan1() )
											|| checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan2(), rec.getFineMan2())
											|| checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan3(), rec.getFineMan3())
											|| checkIntervalloInterni(currentForm.getRecInv().getCodInvent(), rec.getInizioMan4(), rec.getFineMan4()) ) {
										this.impostaDataIngresso(currentForm, codInvent,	rec);//almaviva2 24/11/2009
									}else{
									//non appartiene ad alcun intervallo

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ilProgressivoIndicatoDeveAppartenereAdUnInventarioRiservato"));

									return mapping.getInputForward();
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private void controllaEstremiIntervalli(SerieVO rec) {
		if (rec.getInizioMan1() != null){
			if (Integer.valueOf(rec.getInizioMan1()) == 0){
				rec.setInizioMan1(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getFineMan1() != null){
			if (Integer.valueOf(rec.getFineMan1()) == 0){
				rec.setFineMan1(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getInizioMan2() != null) {
			if (Integer.valueOf(rec.getInizioMan2()) == 0){
				rec.setInizioMan2(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getFineMan2() != null){
			if (Integer.valueOf(rec.getFineMan2()) == 0){
				rec.setFineMan2(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getInizioMan3() != null){
			if (Integer.valueOf(rec.getInizioMan3()) == 0){
				rec.setInizioMan3(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getFineMan3() != null){
			if (Integer.valueOf(rec.getFineMan3()) == 0){
				rec.setFineMan3(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getInizioMan4() != null){
			if (Integer.valueOf(rec.getInizioMan4()) == 0){
				rec.setInizioMan4(String.valueOf(Integer.MAX_VALUE));
			}
		}
		if (rec.getFineMan4() != null){
			if (Integer.valueOf(rec.getFineMan4()) == 0){
				rec.setFineMan4(String.valueOf(Integer.MAX_VALUE));
			}
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		try {

			if (request.getAttribute("prov") != null &&
					(request.getAttribute("prov").equals("modInvColl"))){
				request.setAttribute("ordineCompletato", "ordineCompletato");
			}
	   		request.setAttribute("codBib", currentForm.getRecInv().getCodBib());
	   		request.setAttribute("descrBib", currentForm.getDescrBib());
			return Navigation.getInstance(request).goBack(true);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ordini(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

				if (currentForm.getReticolo() == null){
					currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBid(), currentForm.getTicket(), form));
				}
				CollocazioneTitoloVO[] listaTitoliReticolo = currentForm.getReticolo().getListaTitoliCollocazione();
				CollocazioneTitoloVO titoloReticolo = null;
				String collTit = null;
				List<String> listaBidReticolo = new ArrayList<String>();
				for (int i = 0; i < listaTitoliReticolo.length; i++) {
//				for (int i = 0; i < currentForm.getReticolo().getListaTitoliCollocazione().length; i++) {
					titoloReticolo = listaTitoliReticolo[i];
					collTit = new String();
					collTit = (titoloReticolo.getBid());
					listaBidReticolo.add(collTit);
				}
				if (listaBidReticolo == null || listaBidReticolo.size() <1)  {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noLista"));

					return mapping.getInputForward();
				}
				ListaSuppOrdiniVO ricArr=new ListaSuppOrdiniVO();
				ricArr.setCodPolo(currentForm.getCodPolo());
				ricArr.setCodBibl(currentForm.getCodBib());
				ricArr.setTicket(currentForm.getTicket());
				ricArr.setChiamante(mapping.getPath());
				ricArr.setBidList(listaBidReticolo);
				ricArr.setOrdinamento("2"); //chiave ordine decrescente
				//almaviva5_20110922 #4630
				ricArr.setStatoOrdineArr(new String[] {"A", "C"} );

				request.setAttribute("passaggioListaSuppOrdiniVO",ricArr);
				return mapping.findForward("ordini");

		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}


	public ActionForward collocazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		try {
			//inserisce inventario
			currentForm.setNInv(1);

			try{
				ActionForward forward = controlloTipoInventario(mapping, request, currentForm);
				// se forward é valorizzato c'é stato un errore nei controlli
				if (forward != null)
					return forward;
				//continuo con l'inserimento
				currentForm.getRecInv().setUteIns(Navigation.getInstance(request).getUtente().getFirmaUtente());
				List invCreati = this.insertInventario(currentForm.getRecInv(), currentForm.getTipoOperazione(),
						currentForm.getNInv(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), form);
				if (invCreati == null ||  invCreati.size() <1)  {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erratoInserimento"));

					this.saveToken(request);
					return mapping.getInputForward();
				}else{
					//imposta i dati per passare a inserimento collocazione
			   		InventarioVO recInv = (InventarioVO)invCreati.get(0);
					//

			   		// Intervento interno ALMAVIVA2 settembre 2018
			   		// La localizzazione per Gestione deve essere fatta sia per il bid oggetto di inventariazione
			   		// che per gli spogli agganciati che altrimenti rimarrebbero gialli alla successiva fase di
			   		// collocazione che inserirebbe la localizzazione per possesso ma senza quella per Gestione.
					//localizzaGestione(request, currentForm);//
					if (currentForm.getReticolo() == null){
						currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBid(), currentForm.getTicket(), form));
					}
			   		localizzaGestioneAncheSpogli(request, currentForm);



					request.setAttribute("codBib",recInv.getCodBib());
					request.setAttribute("codSerie",recInv.getCodSerie());
					request.setAttribute("codInvent",recInv.getCodInvent());

			   		request.setAttribute("bid", currentForm.getBid());
//					request.setAttribute("listaDocReticolo", currentForm.getReticolo());
					request.setAttribute("titolo", currentForm.getTitolo());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					return mapping.findForward("insCollocazione");
				}
			} catch (ValidationException e) {
				if (e.getMsg()!=null){
					if (e.getMsg().equals("msgS")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage(), e.getBid(), e.getIsbd()));

						this.saveToken(request);
						currentForm.setConferma(true);
						currentForm.setProv("collocazione");
						return mapping.getInputForward();
					}else if ((e.getMsg().equals("msgC"))){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

						this.saveToken(request);
						currentForm.setConferma(true);
						return mapping.getInputForward();
					}else if ((e.getMsg().equals("msgGiaPresente"))){
						this.saveToken(request);
						return mapping.findForward("chiudi");
					}
				}

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			} catch (Exception e) { // altri tipi di errore

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward collocazioneVeloce(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		try {
			   		request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("titolo", currentForm.getTitolo());
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					request.setAttribute("prov", "CV");
					return mapping.findForward("insCollocazione");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if (!currentForm.isSessione()) {
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			this.controllaTipoOperazione(request, form);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void controllaTipoOperazione(HttpServletRequest request, ActionForm form) throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		final String tipoOperazione = coalesce(currentForm.getTipoOperazione(), DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE);
		if (ValidazioneDati.in(tipoOperazione, 
				DocumentoFisicoCostant.N_INVENTARIO_NON_PRESENTE,
				DocumentoFisicoCostant.S_INVENTARIO_PRESENTE)) {

			currentForm.getRecInv().setCodInvent(0);
			currentForm.setTipoOperazione(tipoOperazione);
			currentForm.setDisable(false);
			currentForm.setDisableDataIngresso(true);
			currentForm.getRecInv().setDataIngresso("");
		} else if (ValidazioneDati.in(tipoOperazione,
						DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE,
						DocumentoFisicoCostant.P_PROGRESSIVO_PREGRESSO)) {

			currentForm.getRecInv().setCodInvent(0);
			currentForm.setTipoOperazione(tipoOperazione);
			currentForm.setDisable(true);
			currentForm.setDisableDataIngresso(true);
			if ((tipoOperazione.equals(DocumentoFisicoCostant.P_PROGRESSIVO_PREGRESSO))) {
				currentForm.setDisableDataIngresso(false);
			}else{
				currentForm.getRecInv().setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
			}
		}

	}

	private void assegnaDateIngresso(VaiAInserimentoInvForm currentForm, HttpServletRequest request) {
		//per la serie selezionata prendo in considerazione i dati per i controlli
		for (int i = 0; i < currentForm.getListaSerie().size(); i++) {
			final SerieVO rec = (SerieVO) currentForm.getListaSerie().get(i);
			if ((currentForm.getRecInv().getCodSerie().trim()).equals(rec.getCodSerie().trim())){
				if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE)){
					currentForm.getRecInv().setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
					currentForm.setDisableDataIngresso(true);
				}else if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.P_PROGRESSIVO_PREGRESSO)){
					currentForm.setDisableDataIngresso(false);
					if (rec.getDataIngrPregr() == null){
						currentForm.getRecInv().setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
					}else{
						currentForm.getRecInv().setDataIngresso((rec.getDataIngrPregr()));
					}
				} else {
					int codInvent = currentForm.getRecInv().getCodInvent();
					if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.N_INVENTARIO_NON_PRESENTE)){
						//gli stessi controlli vengono effettuati in ok
						if (codInvent > 0){
							if (codInvent < Integer.parseInt(rec.getProgAssInv())){
								this.impostaDataIngresso(currentForm, codInvent, rec);
							}else if (codInvent > Integer.parseInt(rec.getProgAssInv())){
								if (  codInvent > 900000000 && codInvent < Integer.valueOf(rec.getPregrAssInv())){
									//non fa niente
								}else if (codInvent < 900000000){
									this.controllaEstremiIntervalli(rec);
									if (checkIntervalloInterni(codInvent, rec.getInizioMan1(), rec.getFineMan1() )
											|| checkIntervalloInterni(codInvent, rec.getInizioMan2(), rec.getFineMan2())
											|| checkIntervalloInterni(codInvent, rec.getInizioMan3(), rec.getFineMan3())
											|| checkIntervalloInterni(codInvent, rec.getInizioMan4(), rec.getFineMan4()) ) {
									}else{
										//non appartiene ad alcun intervallo

										LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ilProgressivoIndicatoDeveAppartenereAdUnInventarioRiservato"));

										this.saveToken(request);
									}
									this.impostaDataIngresso(currentForm, codInvent,	rec);
								}
							}
						}else{
//							currentForm.getRecInv().setDataIngresso("");
							currentForm.getRecInv().setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
						}
						currentForm.setDisableDataIngresso(true);

					}else if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.S_INVENTARIO_PRESENTE)){
						//gli stessi controlli vengono effettuati in ok
						if (codInvent > 0){
							if (codInvent < Integer.parseInt(rec.getProgAssInv())){
								//
							}else if (codInvent > Integer.parseInt(rec.getProgAssInv())){
								if (  codInvent > 900000000 && codInvent < Integer.valueOf(rec.getPregrAssInv())){
									//non fa niente
								}else if (codInvent < 900000000){
									this.controllaEstremiIntervalli(rec);
									if (checkIntervalloInterni(codInvent, rec.getInizioMan1(), rec.getFineMan1() )
											|| checkIntervalloInterni(codInvent, rec.getInizioMan2(), rec.getFineMan2())
											|| checkIntervalloInterni(codInvent, rec.getInizioMan3(), rec.getFineMan3())
											|| checkIntervalloInterni(codInvent, rec.getInizioMan4(), rec.getFineMan4()) ) {
									}else{
										//non appartiene ad alcun intervallo

										LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ilProgressivoIndicatoDeveAppartenereAdUnInventarioRiservato"));

										this.saveToken(request);
									}
								}
							}
						}else{
//							currentForm.getRecInv().setDataIngresso("");
							currentForm.getRecInv().setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
						}
						currentForm.setDisableDataIngresso(true);
					}
				}
			}
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * VaiAInserimentoInvAction.java
	  * impostaDataIngresso
	  * void
	  * @param myForm
	  * @param codInvent
	  * @param rec
	  *
	  *
	 */
	private void impostaDataIngresso(VaiAInserimentoInvForm currentForm,
			int codInvent, SerieVO rec) {
		this.controllaEstremiIntervalli(rec);
		//imposta data numero manuale //almaviva2 30112009
		if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.S_INVENTARIO_PRESENTE) ||
				(currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.N_INVENTARIO_NON_PRESENTE))){
			if (checkIntervalloInterni(codInvent, String.valueOf(1), rec.getNumMan() )){
				currentForm.getRecInv().setDataIngresso(rec.getDataIngrMan());
			}
		}
		//appartiene a qualche intervallo da individuare per recuperare al data convenzionale ad esso associato
		if (checkIntervalloInterni(codInvent, rec.getInizioMan1(), rec.getFineMan1() )){
			currentForm.getRecInv().setDataIngresso(rec.getDataIngrRis1());
		}else if (checkIntervalloInterni(codInvent, rec.getInizioMan2(), rec.getFineMan2() )){
			currentForm.getRecInv().setDataIngresso(rec.getDataIngrRis2());
		}else if (checkIntervalloInterni(codInvent, rec.getInizioMan3(), rec.getFineMan3() )){
			currentForm.getRecInv().setDataIngresso(rec.getDataIngrRis3());
		}else if (checkIntervalloInterni(codInvent, rec.getInizioMan4(), rec.getFineMan4() )){
			currentForm.getRecInv().setDataIngresso(rec.getDataIngrRis4());
		}
	}
	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		try {
//			if (!isTokenValid(request)) {
			saveToken(request);
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			try{
				int codInv = 0;
				currentForm.setTipoOperazione(DocumentoFisicoCostant.T_CAMBIO_TIT_REC_INV_CANC);
				if (currentForm.getTipoOperazione().equals(DocumentoFisicoCostant.T_CAMBIO_TIT_REC_INV_CANC)) {
					currentForm.setNInv(1);
					currentForm.getRecInv().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
					currentForm.getRecInv().setBid(currentForm.getBid());
					List invCreati = this.insertInventario(currentForm.getRecInv(), currentForm.getTipoOperazione(),
							currentForm.getNInv(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), form);
					if (invCreati == null ||  invCreati.size() <1)  {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erratoInserimento"));

						this.saveToken(request);
						currentForm.setConferma(false);
						return mapping.getInputForward();
					}else{
						//
						AreaDatiVariazioneReturnVO advr = null;
//						if (currentForm.getReticolo() != null){
							localizzaGestione(request, currentForm);//
//						}
					}

					//
					if (currentForm.getProv() != null && currentForm.getProv().equals("collocazione")){
						// se bottone Collocazione
						//imposta i dati per passare a inserimento collocazione
						InventarioVO recInv = (InventarioVO)invCreati.get(0);
						request.setAttribute("codBib",recInv.getCodBib());
						request.setAttribute("codSerie",recInv.getCodSerie());
						request.setAttribute("codInvent",recInv.getCodInvent());

						request.setAttribute("bid", currentForm.getBid());
						//							request.setAttribute("listaDocReticolo", currentForm.getReticolo());
						request.setAttribute("titolo", currentForm.getTitolo());
						request.setAttribute("descrBib", currentForm.getDescrBib());

						return mapping.findForward("insCollocazione");
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.recuperoInventarioEffettuato"));

						this.saveToken(request);
						return mapping.findForward("chiudi");
					}
				}
			} catch (ValidationException e) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			} catch (Exception e) { // altri tipi di errore

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			}

		} catch (Exception e) {

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * VaiAInserimentoInvAction.java
	  * localizzaGestione
	  * void
	  * @param request
	  * @param myForm
	  * @throws Exception
	  *
	  *
	 */
	private void localizzaGestione(HttpServletRequest request,
			VaiAInserimentoInvForm currentForm) throws Exception {

		if (currentForm.getRecTitolo() == null)
			throw new ValidationException("titNotFoundPolo");

		AreaDatiVariazioneReturnVO advr = null;
		AreaDatiLocalizzazioniAuthorityVO al = null;
		AreaDatiLocalizzazioniAuthorityMultiplaVO adlam = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
		if (currentForm.getRecTitolo() != null){
			al = new AreaDatiLocalizzazioniAuthorityVO();
			if (currentForm.getRecTitolo().isFlagCondiviso()){
				al.setIndice(true);
				al.setPolo(false);
				al.setTipoLoc("Gestione");
				al.setIdLoc(currentForm.getBid());
				al.setNatura(currentForm.getRecTitolo().getNatura());
				al.setAuthority("");
				al.setTipoMat(currentForm.getRecTitolo().getTipoMateriale());
				al.setTipoOpe("Localizza");
//				al.setCodiceSbn(Navigation.getInstance(request).getUtente().getCodPolo()+
//						Navigation.getInstance(request).getUtente().getCodBib());
				al.setCodiceSbn(currentForm.getCodPolo()+ currentForm.getCodBib());

				adlam.addListaAreaLocalizVO(al);
			}
			advr = this.localizzaAuthorityMultipla(adlam, currentForm.getTicket());

			request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
			request.setAttribute("bid", currentForm.getBid());
			request.setAttribute("desc", currentForm.getTitolo());
			//
			if (advr != null && (advr.getCodErr().equals("9999") || ValidazioneDati.isFilled(advr.getTestoProtocollo())) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo", advr.getTestoProtocollo()));
			} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
				if (!advr.getCodErr().equals("7017")){
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + advr.getCodErr()));
				}
			}
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		// Viene settato il token per le transazioni successive
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private List getListaSerie(String codPolo, String codBib, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		final List serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		if (serie == null ||  serie.size() <= 0)  {
			currentForm.setNoSerie(true);
		}
		return serie;
	}
	private List getListaSerieDate(String codPolo, String codBib, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		final List serie = factory.getGestioneDocumentoFisico().getListaSerieDate(codPolo, codBib, ticket);
		if (!ValidazioneDati.isFilled(serie)) {
			currentForm.setNoSerie(true);
		}
		return serie;
	}
	private List insertInventario(InventarioVO inventario, String tipoOperazione, int nInv, Locale locale, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm)form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		final List inv = factory.getGestioneDocumentoFisico().insertInventario(inventario, tipoOperazione, nInv, locale, ticket);
		if (!ValidazioneDati.isFilled(inv))  {
			currentForm.setNoSerie(true);
		}
		return inv;
	}
	private List<TitoloVO> getTitolo(String bid, String ticket) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<TitoloVO> titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}
	private DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm) form;
		DatiBibliograficiCollocazioneVO reticolo;
		String bibliotecaOperante = currentForm.getCodPolo() + currentForm.getCodBib();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		reticolo = factory.getGestioneDocumentoFisico().getAnaliticaPerCollocazione(bid, bibliotecaOperante, ticket);
		if (reticolo == null)  {
			currentForm.setNoReticolo(true);
		}else{
			currentForm.setNoReticolo(false);
		}
		return reticolo;
	}
	private InventarioVO getInventario(String codPolo, String codBib, String codSerie, int codInv, Locale locale, String ticket) throws Exception {
		InventarioVO recInv = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		recInv = factory.getGestioneDocumentoFisico().getInventario(codPolo, codBib, codSerie, codInv, locale, ticket);
		return recInv;
	}

	private SerieVO getSerieDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SerieVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSerieDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (idCheck.equals("ordini") ){
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI, currentForm.getCodPolo(), currentForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					log.error(e);
					return false;
				}
			}
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, currentForm.getCodPolo(), currentForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			log.error(e);
			return false;
		}
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

	public ActionForward listaSupporto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoInvForm currentForm = (VaiAInserimentoInvForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, currentForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setDisable(false);
			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}


	private void localizzaGestioneAncheSpogli(HttpServletRequest request,
			VaiAInserimentoInvForm currentForm) throws Exception {

		if (currentForm.getRecTitolo() == null)
			throw new ValidationException("titNotFoundPolo");

		AreaDatiVariazioneReturnVO advr = null;
		AreaDatiLocalizzazioniAuthorityVO al = null;
		AreaDatiLocalizzazioniAuthorityMultiplaVO adlam = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
		DatiBibliograficiCollocazioneVO reticolo = currentForm.getReticolo();
		CollocazioneTitoloVO[] titoliDaLocalolGestioneReticolo = reticolo.getListaTitoliLocalizzazione();
		TreeElementViewTitoli titoloNotiziaCorrente = (TreeElementViewTitoli)reticolo.getAnalitica().findElement(currentForm.getBid());

		if (currentForm.getRecTitolo() != null){
			al = new AreaDatiLocalizzazioniAuthorityVO();
			if (currentForm.getRecTitolo().isFlagCondiviso()){
				al.setIndice(true);
				al.setPolo(false);
				al.setTipoLoc("Gestione");
				al.setIdLoc(currentForm.getBid());
				al.setNatura(currentForm.getRecTitolo().getNatura());
				al.setAuthority("");
				al.setTipoMat(currentForm.getRecTitolo().getTipoMateriale());
				al.setTipoOpe("Localizza");
				al.setCodiceSbn(currentForm.getCodPolo()+ currentForm.getCodBib());

				adlam.addListaAreaLocalizVO(al);
			}
		}

		for (int i = 0; i < titoliDaLocalolGestioneReticolo.length; i++) {
			CollocazioneTitoloVO titoloCollocabile = titoliDaLocalolGestioneReticolo[i];
			TreeElementViewTitoli tit = (TreeElementViewTitoli)reticolo.getAnalitica().findElement(titoloCollocabile.getBid());

			if (tit.getNatura().equals("N")) {
				al = new AreaDatiLocalizzazioniAuthorityVO();
				// ALMAVIVA2 Correttiva per individuare il flagCondivisione dalla giusta classe (tit e non titoloNotiziaCorrente che
				// corrisponde al titolo madre)
				// al.setIndice(titoloNotiziaCorrente.isFlagCondiviso());
				al.setIndice(tit.isFlagCondiviso());

				al.setPolo(false);
				al.setTipoLoc("Gestione");
				al.setIdLoc(titoloCollocabile.getBid());
				al.setNatura(titoloCollocabile.getNatura());
				al.setAuthority("");
				al.setTipoMat(tit.getTipoMateriale());
				al.setTipoOpe("Localizza");
				al.setCodiceSbn(currentForm.getCodPolo()+ currentForm.getCodBib());

				adlam.addListaAreaLocalizVO(al);
			}
		}

		advr = this.localizzaAuthorityMultipla(adlam, currentForm.getTicket());

		request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
		request.setAttribute("descrBib",currentForm.getDescrBib());
		request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
		request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
		request.setAttribute("bid", currentForm.getBid());
		request.setAttribute("desc", currentForm.getTitolo());
		//
		if (advr != null && (advr.getCodErr().equals("9999") || ValidazioneDati.isFilled(advr.getTestoProtocollo())) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo", advr.getTestoProtocollo()));
		} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
			if (!advr.getCodErr().equals("7017")){
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + advr.getCodErr()));
			}
		}

	}


}
