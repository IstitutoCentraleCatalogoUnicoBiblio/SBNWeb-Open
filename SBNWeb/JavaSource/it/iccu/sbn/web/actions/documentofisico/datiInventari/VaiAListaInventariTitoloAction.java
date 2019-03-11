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
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.VaiAListaInventariTitoloForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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


/**
 * VaiAListaInventariAction.java
 * 18-gen-2007
 *
 */
public class VaiAListaInventariTitoloAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(VaiAListaInventariTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "blocco");
		map.put("documentofisico.bottone.scegliColl", "collocazione");
		map.put("documentofisico.bottone.altroInv", "altroInv");
		map.put("documentofisico.bottone.ordini", "ordini");
		map.put("documentofisico.bottone.esInv1", "esameInventario");
		map.put("documentofisico.bottone.cancInv", "cancInv");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.bottone.crea", "ok");
		map.put("documentofisico.bottone.scegli", "ok");
		map.put("documentofisico.lsBib", "listaSupporto");
		map.put("documentofisico.bottone.esameInv", "esameInv");
		map.put("documentofisico.bottone.possessori", "possessori");
		map.put("documentofisico.bottone.esemplariTitolo", "esemplariTitolo");
		map.put("documentofisico.bottone.disponibilita", "disponibilita");
		map.put("documentofisico.bottone.indice", "indice");

		//almaviva5_20101021
		map.put("documentofisico.bottone.fascicoli", "fascicoli");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		VaiAListaInventariTitoloForm currentForm = (VaiAListaInventariTitoloForm) form;
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
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm)form;
		try{
			myForm = (VaiAListaInventariTitoloForm) form;
			if (Navigation.getInstance(request).isFromBar()  ){
				if (request.getAttribute("prov") != null && !request.getAttribute("prov").equals("modificaInv")){
					if (Navigation.getInstance(request).isFromBar() ){
						if (request.getAttribute("cancellaInv") != null && request.getAttribute("cancellaInv").equals("cancellaInv")){
						}else{
							return mapping.getInputForward();
						}
					}
				}else if (myForm.getListaInventari() == null){
					ActionMessages errors = LinkableTagUtils.getErrors(request);
					if (!errors.isEmpty()){

						return Navigation.getInstance(request).goBack(true);
					}else{
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.nonSonoPresentiInventariPerIlTitolo"));

						return Navigation.getInstance(request).goBack(true);
					}
				} else{
					return mapping.getInputForward();
				}
			}
			if (request.getAttribute("indietro") != null){
				return mapping.getInputForward();
			}
			// controllo se ho già i dati in sessione;
			if(!myForm.isSessione()) {
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				loadDefault(request, mapping, form);
				myForm.setSessione(true);
				Navigation.getInstance(request).addBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL);
			}
			//test variabile "passaggio...." da ordine
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaInventariOrdine")){
				request.setAttribute("codBib", request.getAttribute("codBib"));
				request.setAttribute("descrBib", request.getAttribute("descrBib"));
				request.setAttribute("bid", request.getAttribute("bid"));
				request.setAttribute("titolo", request.getAttribute("titolo"));
				request.setAttribute("bidOrdine", request.getAttribute("bidOrdine"));
				request.setAttribute("titoloOrdine", request.getAttribute("titoloOrdine"));
				request.setAttribute("inventario", request.getAttribute("inventario"));
				request.setAttribute("prov", request.getAttribute("prov"));
				return mapping.findForward("vaiAInserimentoInv");
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
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("bid", myForm.getBidNotCorr());
					request.setAttribute("titolo", myForm.getTitoloNotCorr());
					request.setAttribute("tastoOrdiniDaListaInvTitolo", "tastoOrdiniDaListaInvTitolo");
					return mapping.findForward("listaInventariOrdine");
				}else{
					//provengo da indietro di lista supporto ordini
					return mapping.getInputForward();
				}
			}
			if (request.getParameter("INVRICERCA")!=null){
				myForm.setIdInv(request.getParameter("INVRICERCA"));
			}
			if ( myForm.getIdInv() != null) {
				myForm.setBidNotCorr((String)request.getAttribute("bid"));
				myForm.setTitoloNotCorr((String)request.getAttribute("desc"));
				List listaBiblio = (List)request.getAttribute("listaBiblio");
				if (!ValidazioneDati.isFilled(listaBiblio)) {
					listaBiblio = new ArrayList();
					listaBiblio.add(Navigation.getInstance(request).getUtente().getCodBib());
				}else{
					myForm.setIdInv("EROGAZIONE_RICERCA");
				}
				myForm.setListaBibliotecheServizi(listaBiblio);
				if (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
					myForm.setIdInv("STAMPA_SERVIZI_CORRENTI");
				}

			}else{
				if (request.getAttribute("codBibDaLista") != null) {
					BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
					request.setAttribute("codBib", bib.getCod_bib());
					request.setAttribute("descrBib", bib.getNom_biblioteca());
				}
				if(request.getAttribute("codBib") != null && request.getAttribute("descrBib")!= null) {
					// provengo dalla lista biblioteche
					myForm.setCodBib((String)request.getAttribute("codBib"));
					myForm.setDescrBib((String)request.getAttribute("descrBib"));
				}
			}
			if (myForm.getBidNotCorr()==null && myForm.getTitoloNotCorr()==null){
				if (request.getAttribute("bid") != null || request.getAttribute("desc") != null) {
					myForm.setBidNotCorr((String)request.getAttribute("bid"));
					List lista = this.getTitolo(myForm.getBidNotCorr(), myForm.getTicket());
					if (lista != null){
						if (lista.size() == 1){
							TitoloVO titolo = (TitoloVO)lista.get(0);
							myForm.setBidNotCorr(titolo.getBid());
							if (!titolo.isFlagCondiviso()){
								myForm.setTitoloNotCorr("[Loc] "+titolo.getIsbd());
							}else{
								myForm.setTitoloNotCorr(titolo.getIsbd());
								myForm.setAbilitaBottoneInviaInIndice(true);
							}
//							myForm.setTitoloNotCorr(titolo.getIsbd());
//							if (!myForm.isFlagLoc()){
//								myForm.setTitoloNotCorr("[Loc] "+myForm.getTitoloNotCorr());
//							}
						}
					}
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioDeiParametri"));

					request.setAttribute("bid",myForm.getBidNotCorr());
					request.setAttribute("livRicerca","I");
					return mapping.findForward("chiudi");
				}
			}
			List listaInv = null;
			if ( myForm.getIdInv() == null) {
				listaInv = this.getListaInventariTitolo(myForm.getCodPolo(), myForm.getCodBib(),
						myForm.getBidNotCorr(), myForm.getTicket(), myForm.getNRec(), form);
			}else{
				listaInv = this.getListaInventariTitolo(myForm.getCodPolo(), myForm.getListaBibliotecheServizi(),
						myForm.getBidNotCorr(), myForm.getTicket(), myForm.getNRec(), form);
			}
			if (listaInv == null ||  listaInv.size() <1)  {
				// errore su sif servizi
				if (myForm.getIdInv() != null){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.nonSonoPresentiInventariPerIlTitolo"));

					request.setAttribute("bid",myForm.getBidNotCorr());
					request.setAttribute("IdInv","false");
					return Navigation.getInstance(request).goBack(true);
				}

				return inventariNonTrovati(mapping, request, myForm);
			}else{
				if (listaInv.size() == 1){
					myForm.setSelectedInv("0");
				}else{
					myForm.setSelectedInv(null);
				}
				if (myForm.getIdInv() != null){
					request.setAttribute("IdInv","true");
				}
				myForm.setNoInv(false);
				myForm.setCodBib(myForm.getCodBib());
				myForm.setDescrBib(myForm.getDescrBib());
				myForm.setListaInventari(listaInv);
			}
		}	catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}	catch (DataException de) {
			if (myForm.getIdInv() != null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + de.getMessage()));

				request.setAttribute("bid",myForm.getBidNotCorr());
				request.setAttribute("IdInv","false");
				return Navigation.getInstance(request).goBack(true);
			}
			if (de.getErrorCode() == SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO) {
				if (this.checkAttivita(request, myForm, "df")) {
					myForm.setListaInventari(null);//bug 0003870 collaudo
					request.setAttribute("codBib", myForm.getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("bid", myForm.getBidNotCorr());
					request.setAttribute("titolo", myForm.getTitoloNotCorr());
					return mapping.findForward("vaiAInserimentoInv");
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.utenteNonAbilitato"));

					return Navigation.getInstance(request).goBack(true);
				}

				//				myForm.setDisable(false);
				//				myForm.setConferma(false);
				//				myForm.setListaInventari(null);
				//				myForm.setTotRighe(0);
				//				return inventariNonTrovati(mapping, request, myForm);
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + de.getMessage()));

			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + de.getMessage()));

			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * VaiAListaInventariTitoloAction.java
	 * inventariNonTrovati
	 * ActionForward
	 * @param mapping
	 * @param request
	 * @param myForm
	 * @return
	 *
	 *
	 */
	private ActionForward inventariNonTrovati(ActionMapping mapping, HttpServletRequest request, VaiAListaInventariTitoloForm myForm)
	throws Exception{
		myForm.setNoInv(true);
		request.setAttribute("codBib", myForm.getCodBib());
		request.setAttribute("descrBib", myForm.getDescrBib());
		request.setAttribute("bid", myForm.getBidNotCorr());
		request.setAttribute("titolo", myForm.getTitoloNotCorr());
		if (request.getAttribute("noListaSerie")!=null){
			if (myForm.getIdInv() != null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.nonSonoPresentiInventariPerIlTitolo"));

				request.setAttribute("bid",myForm.getBidNotCorr());
				request.setAttribute("IdInv","false");
				return Navigation.getInstance(request).goBack(true);
			}
			myForm.setNoInv(false);
			return mapping.getInputForward();
		}else{

			if (this.checkAttivita(request, myForm, "df")){
				if (myForm.getIdInv() == null){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.nonSonoPresentiInventariPerIlTitolo"));

					request.setAttribute("bid",myForm.getBidNotCorr());
					request.setAttribute("IdInv","false");
//					return Navigation.getInstance(request).goBack(true);//bug 0003995 collaudo
				}
				return mapping.findForward("vaiAInserimentoInv");
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noInv"));

				myForm.setDisable(false);
				myForm.setConferma(false);
				return mapping.getInputForward();
			}
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * VaiAListaInventariAction.java
	 * chiudi
	 * ActionForward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * Il metodo consente di abbandonare la pagina senza apportare modifiche
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {

			return Navigation.getInstance(request).goBack(true);
			//    		request.setAttribute("bid",myForm.getBidNotCorr());
			//    		request.setAttribute("livRicerca","I");
			//			return mapping.findForward("chiudi");

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward esameInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {
			request.setAttribute("bid",myForm.getBidNotCorr());
			request.setAttribute("livRicerca","I");
			return mapping.findForward("chiudi");

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {
			Navigation navi = Navigation.getInstance(request);
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String checkS = myForm.getSelectedInv();
			if (checkS != null && checkS.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioTitoloVO scelInv = (InventarioTitoloVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("scelInv",scelInv);
				if (myForm.getIdInv().equals("EROGAZIONE_RICERCA")){
					//almaviva5_20151126 servizi ill
					if (navi.bookmarkExists(Bookmark.Servizi.DATI_RICHIESTA_ILL) ) {
						if (ValidazioneDati.isFilled(scelInv.getCodNoDisp()) ) {
							LinkableTagUtils.addError(request, new ActionMessage("ERRORE_DOCUMENTO_NON_DISPONIBILE",
									scelInv.getDisponibilita()));
							return mapping.getInputForward();
						}
						return navi.goToBookmark(Bookmark.Servizi.DATI_RICHIESTA_ILL, false);
					}
					//
					return mapping.findForward("serviziErogazione");
				}else if (myForm.getIdInv().equals("MOVIMENTI_UTENTE")){
					return mapping.findForward("movimenti_utente");
				}else if (myForm.getIdInv().equals("STAMPA_SERVIZI_CORRENTI")){
					return mapping.findForward("stampaServizi");
				}else{
					return mapping.getInputForward();
				}
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * VaiAListaInventariAction.java
	 * altroInv
	 * ActionForward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * Il metodo richiama la funzione di inserimento di un nuovo inventario
	 */
	public ActionForward altroInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {

			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("bid", myForm.getBidNotCorr());
			request.setAttribute("titolo", myForm.getTitoloNotCorr());

			return mapping.findForward("vaiAInserimentoInv");

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.datiInventari
	 * VaiAListaInventariAction.java
	 * collocazione
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * ActionForward
	 *
	 * Il metodo
	 */
	public ActionForward collocazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {

			request.setAttribute("check", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			int invsel;
			if (!myForm.isNoInv()){
				if (check != null && check.length() != 0) {
					//					List listaInv = (List) myForm.getListaInventari();
					invsel = Integer.parseInt(myForm.getSelectedInv());
					InventarioTitoloVO inv = (InventarioTitoloVO) myForm.getListaInventari().get(invsel);
					if (inv.isDismesso() ) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventarioDismesso"));

						return mapping.getInputForward();
					}
					//individuato l'inventario selezionato vado sul db per effettuare controllo di esistenza
					InventarioVO recInv = this.getInventario(myForm.getCodPolo(), inv.getCodBib(), inv.getCodSerie(), inv.getCodInvent(), this.getLocale(request, Constants.SBN_LOCALE), myForm.getTicket());
					if (recInv != null) {
						//dati di inventario
						request.setAttribute("codBib",recInv.getCodBib());
						request.setAttribute("codSerie",recInv.getCodSerie());
						request.setAttribute("codInvent",recInv.getCodInvent());
						//dati di mappa
						request.setAttribute("descrBib", myForm.getDescrBib());
						request.setAttribute("bid", myForm.getBidNotCorr());
						request.setAttribute("titolo", myForm.getTitoloNotCorr());
						if (recInv.getKeyLoc()==(0)) {
							//							request.setAttribute("listaDocReticolo", myForm.getReticolo());
							return mapping.findForward("vaiAInserimentoColl");
						}else {
							request.setAttribute("keyLoc", recInv.getKeyLoc());
							return mapping.findForward("vaiAModificaColl");
						}
					}else {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));

					}
				}else {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

					return mapping.getInputForward();
				}
			}
			return mapping.getInputForward();

		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ordini(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

			if (!myForm.isNoInv()){
				if (myForm.getReticolo() == null){
					myForm.setReticolo(this.getAnaliticaPerCollocazione(myForm.getBidNotCorr(), myForm.getTicket(), form));
				}
				CollocazioneTitoloVO[] listaTitoliReticolo = myForm.getReticolo().getListaTitoliCollocazione();
				CollocazioneTitoloVO titoloReticolo = null;
				String collTit = null;
				List listaBidReticolo = new ArrayList();
				for (int i = 0; i < myForm.getReticolo().getListaTitoliCollocazione().length; i++) {
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
				ricArr.setCodPolo(myForm.getCodPolo());
				ricArr.setCodBibl(myForm.getCodBib());
				ricArr.setTicket(myForm.getTicket());
				ricArr.setChiamante(mapping.getPath());
				ricArr.setBidList(listaBidReticolo);
				ricArr.setOrdinamento("8");

				request.setAttribute("passaggioListaSuppOrdiniVO",ricArr);
				return mapping.findForward("ordini");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noInv"));

			}

			return mapping.getInputForward();

		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward possessori(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent",""+scelInv.getCodInvent());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("prov", "DOCFISGESTIONE");

				//				return mapping.getInputForward();
				return mapping.findForward("possessori");
			} else {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

			}

			return mapping.getInputForward();

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}


	public ActionForward fascicoli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//almaviva5_20101021
		VaiAListaInventariTitoloForm currentForm = (VaiAListaInventariTitoloForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {

			String selected = currentForm.getSelectedInv();
			if (!ValidazioneDati.isFilled(selected)) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));
				return mapping.getInputForward();
			}

			int invSel = Integer.parseInt(currentForm.getSelectedInv());
			InventarioTitoloVO inv = (InventarioTitoloVO) currentForm.getListaInventari().get(invSel);
			if (inv.getKeyLoc() == 0) { //solo collocati!!
				LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
				return mapping.getInputForward();
			}

			BibliotecaVO bib = new BibliotecaVO();
	        bib.setCod_polo(currentForm.getCodPolo());
	        bib.setCod_bib(currentForm.getCodBib());
	        bib.setNom_biblioteca(currentForm.getDescrBib());

		    return PeriodiciDelegate.getInstance(request).sifKardexPeriodicoDaInv(bib, inv);

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setDisable(false);
			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}



	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try{

			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!myForm.isSessione()) {
				myForm.setSessione(true);
			}
			int numBlocco = myForm.getBloccoSelezionato();
			String idLista = myForm.getIdLista();
			String ticket = Navigation.getInstance(request).getUserTicket();
			if (numBlocco>1 && idLista != null) {
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
				if (bloccoVO != null) {
					myForm.getListaInventari().addAll(bloccoVO.getLista());
				}
			}
			Collections.sort(myForm.getListaInventari());
			return mapping.getInputForward();

		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward esameInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

			if (!myForm.isNoInv()){

				int invSel;
				request.setAttribute("checkS", myForm.getSelectedInv());
				String check = myForm.getSelectedInv();
				if (check != null && check.length() != 0) {
					invSel = Integer.parseInt(myForm.getSelectedInv());
					InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
					request.setAttribute("codBib",myForm.getCodBib());
					request.setAttribute("codSerie",scelInv.getCodSerie());
					request.setAttribute("codInvent",scelInv.getCodInvent());
					request.setAttribute("descrBib",myForm.getDescrBib());
					request.setAttribute("prov", "interrogazioneEsame");
					return mapping.findForward("esameInventario");
				} else {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

					return mapping.getInputForward();
				}

			} else {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noInv"));

			}

			return mapping.getInputForward();

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward cancInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {

			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {


				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.confermaCancellazione"));

				this.saveToken(request);
				myForm.setDisable(true);
				myForm.setConferma(true);
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

			}
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward si(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try{
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				CollocazioneVO recColl = new CollocazioneVO();
				String tipoOperazione = DocumentoFisicoCostant.C_CANCELLA_INV;
				scelInv.setCodPolo(myForm.getCodPolo());
				scelInv.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				recColl.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				if (!this.deleteInventario(scelInv, recColl, tipoOperazione, myForm.getTicket())){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreInFaseDiCancellazione"));

					myForm.setDisable(false);
					myForm.setConferma(false);
					return mapping.getInputForward();
				}else{
					request.setAttribute("codBib", myForm.getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

					this.saveToken(request);
					request.setAttribute("cancellaInv", "cancellaInv");
					myForm.setDisable(false);
					myForm.setConferma(false);
					return unspecified(mapping, form, request, response);
				}
			}
			return mapping.getInputForward();
		}
		catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		// Viene settato il token per le transazioni successive
		try {
			myForm.setDisable(false);
			myForm.setConferma(false);
			String chiamante = Navigation.getInstance(request).getActionCaller();
			if (chiamante.toUpperCase().indexOf("INSERIMENTOINV") > -1) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));

				this.saveToken(request);
				return mapping.findForward("esaminaOrdineMod");
			}
			//			return mapping.findForward("chiudi");
			return mapping.getInputForward();
			//			return Navigation.getInstance(request).goBack();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward listaSupporto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}


	private List getListaInventariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec, ActionForm form)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariTitolo(codPolo, codBib, bid, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				myForm.setAbilitaBottoneCarBlocchi(false);
			}else{
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}
	private List getListaInventariTitolo(String codPolo, List<String> listaBiblio, String bid, String ticket, int nRec, ActionForm form)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariTitolo(codPolo, listaBiblio, bid, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				myForm.setAbilitaBottoneCarBlocchi(false);
			}else{
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
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

	private DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String ticket, ActionForm form) throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
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
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (idCheck.equals("possessori") ){
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					log.error(e);
					return false;
				}
			}
			if (idCheck.equals("ordini") ){
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					log.error(e);
					return false;
				}
			}
			//almaviva5_20101021
			if (idCheck.equals("fascicoli") ){
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().PERIODICI, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					log.error(e);
					return false;
				}
			}

			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	private List getTitolo(String bid, String ticket) throws Exception {
		List titolo = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}
	public ActionForward disponibilita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//		richiama un servizio di Servizi a fronte di un codSerie, codInv
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm)form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			request.setAttribute("check", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			int invsel;
			if (!myForm.isNoInv()){
				if (check != null && check.length() != 0) {
					invsel = Integer.parseInt(myForm.getSelectedInv());
					InventarioTitoloVO inv = (InventarioTitoloVO) myForm.getListaInventari().get(invsel);
					//individuato l'inventario selezionato vado sul db per effettuare controllo di esistenza
					InventarioVO recInv = this.getInventario(myForm.getCodPolo(),
							inv.getCodBib(), inv.getCodSerie(), inv.getCodInvent(),
							this.getLocale(request, Constants.SBN_LOCALE), myForm.getTicket());
					if (recInv != null){
						MovimentoVO movimento = new MovimentoVO();
						movimento.setCodPolo(myForm.getCodPolo());
						movimento.setCodBibOperante(myForm.getCodBib());
						movimento.setCodBibInv(recInv.getCodBib());
						movimento.setCodSerieInv(recInv.getCodSerie());
						movimento.setCodInvenInv(String.valueOf(recInv.getCodInvent()));
						request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, movimento);
						InfoDocumentoVO infoDoc = new InfoDocumentoVO();
						infoDoc.setInventarioTitoloVO(inv);
						request.setAttribute("InfoDocumentoVO", infoDoc);
						request.setAttribute("TipoRicerca", RicercaRichiesteType.RICERCA_PER_INVENTARIO);
						return Navigation.getInstance(request).goForward(mapping.findForward("SIFServizi"));
					}
				}else {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

					return mapping.getInputForward();
				}
			}
		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}
	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAListaInventariTitoloForm myForm = (VaiAListaInventariTitoloForm)form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try{
			if (request.getAttribute("diagnostico") != null){

				LinkableTagUtils.addError(request, new ActionMessage("" + request.getAttribute("diagnostico") + " nella biblioteca " + myForm.getCodPolo() + " " + myForm.getCodBib()));

				myForm.setConferma(false);
				return mapping.getInputForward();
			}
			if (!myForm.isNoInv()){
				if (myForm.getReticolo() == null){
					myForm.setReticolo(this.getAnaliticaPerCollocazione(myForm.getBidNotCorr(), myForm.getTicket(), form));
				}
				CollocazioneTitoloVO[] listaTitoliReticolo = myForm.getReticolo().getListaTitoliCollocazione();
				CollocazioneTitoloVO titoloReticolo = null;
				String collTit = null;
				List listaBidReticolo = new ArrayList();
				for (int i = 0; i < myForm.getReticolo().getListaTitoliCollocazione().length; i++) {
					titoloReticolo = listaTitoliReticolo[i];
					collTit = new String();
					collTit = (titoloReticolo.getBid());
					listaBidReticolo.add(collTit);
				}
				if (listaBidReticolo == null || listaBidReticolo.size() <1)  {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noLista"));

					return mapping.getInputForward();
				}
			}

			AreePassaggioSifVO areePassaggioSifVO = new AreePassaggioSifVO();
			//findForward a gestione bibliografica
			areePassaggioSifVO.setOggettoDaRicercare(myForm.getBidNotCorr());
			areePassaggioSifVO.setDescOggettoDaRicercare(myForm.getTitoloNotCorr());
			areePassaggioSifVO.setNaturaOggetto("M");
			areePassaggioSifVO.setTipMatOggetto("M");
			areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
			areePassaggioSifVO.setVisualCall(false);

			UserVO utente = Navigation.getInstance(request).getUtente();
			areePassaggioSifVO.setCodBiblioteca(utente.getCodPolo().toString() + utente.getCodBib().toString());

			TreeElementViewTitoli titoloNotiziaCorrente = (TreeElementViewTitoli)myForm.getReticolo().getAnalitica().findElement(myForm.getBidNotCorr());
			//
			if (titoloNotiziaCorrente.isFlagCondiviso()){
				//polo = true, indice = true
				areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
			}else{
				//polo = true, indice = false
				areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			}
			request.setAttribute("consIndice", "listaInventariTitolo");
			request.setAttribute("areePassaggioSifVO", areePassaggioSifVO);
			return mapping.findForward("sinteticaLocalizzazioni");

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
}
