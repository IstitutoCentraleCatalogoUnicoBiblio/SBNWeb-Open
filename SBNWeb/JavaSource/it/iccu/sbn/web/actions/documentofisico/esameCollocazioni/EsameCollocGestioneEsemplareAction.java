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
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareListaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.ListeInventariForm;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocGestioneEsemplareForm;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocRicercaForm;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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

public class EsameCollocGestioneEsemplareAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsameCollocGestioneEsemplareAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.indice", "indice");
		map.put("documentofisico.bottone.fascicoli", "fascicoli");
		map.put("documentofisico.bottone.cambiaEsempl", "cambiaEsemplare");
		map.put("documentofisico.bottone.cancEsempl", "cancella");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		// controllo se ho già i dati in sessione;
		if(!myForm.isSessione())		{
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			myForm.setSessione(true);
		}
		try{
			if ( request.getAttribute("codBib") != null &&
					request.getAttribute("descrBib") != null &&
					request.getAttribute("bid") != null &&
					request.getAttribute("titolo") != null &&
					request.getAttribute("reticolo") != null &&
					request.getAttribute("recColl") != null &&
					request.getAttribute("codSerie") != null &&
					request.getAttribute("codInv") != null){
				myForm.setCodBib((String)request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
				myForm.setBid((String)request.getAttribute("bid"));
				List lista = this.getTitolo(myForm.getBid(), myForm.getTicket());
				TitoloVO titolo = null;
				if (lista != null){
					if (lista.size() == 1){
						titolo = (TitoloVO)lista.get(0);
					}
				}else{
					throw new ValidationException("titNotFoundPolo", ValidationException.errore);
				}
//				myForm.setTitoloBid((String)request.getAttribute("titolo"));
				myForm.setTitoloBid(titolo.getIsbd());
				myForm.setCodSerie((String)request.getAttribute("codSerie"));
				myForm.setCodInv((String)request.getAttribute("codInv"));
				if (request.getAttribute("reticolo").equals("esamina")){
					myForm.setReticolo(null);
				}else{
					myForm.setReticolo((DatiBibliograficiCollocazioneVO)request.getAttribute("reticolo"));
				}
				myForm.setRecColl((CollocazioneDettaglioVO)request.getAttribute("recColl"));
				List lista1 = this.getTitolo(myForm.getRecColl().getBid(), myForm.getTicket());
				TitoloVO titolo1 = null;
				if (lista1 != null){
					if (lista1.size() == 1){
						titolo1 = (TitoloVO)lista1.get(0);
					}
				}else{
					throw new ValidationException("titNotFoundPolo", ValidationException.errore);
				}
				myForm.getRecColl().setBidDescr(titolo1.getIsbd());

				if (myForm.getRecColl().getCodPoloDoc() != null &&
						myForm.getRecColl().getCodBibDoc() != null ){
					myForm.setBidDoc(myForm.getRecColl().getBidDoc());
					myForm.setBidDocDescr(myForm.getRecColl().getBidDocDescr());
					myForm.setCodDoc(myForm.getRecColl().getCodDoc());
				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.errorePassaggioParametri"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}

			if (request.getAttribute("modifica") != null){
				if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("ricercaEsemplare")){
					if (request.getAttribute("recEsemplareReticolo") != null){
						EsemplareListaVO recEsemplareLista = ((EsemplareListaVO)request.getAttribute("recEsemplareReticolo"));
						if (recEsemplareLista != null){
							myForm.setCodPolo(recEsemplareLista.getCodPolo());
							myForm.setCodBib(recEsemplareLista.getCodBib());
							myForm.setBidDoc(recEsemplareLista.getBid());
							myForm.setBidDocDescr(recEsemplareLista.getBidDescr());
							myForm.setCodDoc(recEsemplareLista.getCodDoc());
						}
					}else{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.errorePassaggioParametri"));
						this.saveErrors(request, errors);
						return Navigation.getInstance(request).goBack(true);
					}
				}
			}
			if (request.getAttribute("nuovo") != null){
				if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("ricercaEsemplare")){
					if (request.getAttribute("recCollocazionetitolo") != null){
						CollocazioneTitoloVO recCollTit = ((CollocazioneTitoloVO)request.getAttribute("recCollocazionetitolo"));
						if (recCollTit != null){
							myForm.setCodPolo(myForm.getCodPolo());
							myForm.setCodBib(myForm.getCodBib());
							myForm.setBidDoc(recCollTit.getBid());

							List lista = this.getTitolo(myForm.getBidDoc(), myForm.getTicket());
							TitoloVO titolo = null;
							if (lista != null){
								if (lista.size() == 1){
									titolo = (TitoloVO)lista.get(0);
								}
							}else{
								throw new ValidationException("titNotFoundPolo", ValidationException.errore);
							}
							myForm.setBidDocDescr(titolo.getIsbd());
//							myForm.setBidDocDescr(recCollTit.getIsbd());
							myForm.setCodDoc(recCollTit.getCodDoc());
						}
					}else{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.errorePassaggioParametri"));
						this.saveErrors(request, errors);
						return Navigation.getInstance(request).goBack(true);
					}
				}
			}
			titolo(myForm.getBid(), myForm);
			if (request.getAttribute("nuovo") != null && myForm.getCodDoc() == 0){
				myForm.setTipoOperazione((String) request.getAttribute("nuovo"));
				myForm.setNuovo(true);
				//				myForm.setCodPolo(myForm.getRecColl().getCodPoloSez());
				//				myForm.setCodBib(myForm.getRecColl().getCodBibSez());
				//				myForm.setBidDoc(myForm.getRecColl().getBid());
				//				myForm.setBidDocDescr(myForm.getRecColl().getBidDescr());
				return mapping.getInputForward();
			}
			EsemplareDettaglioVO recEsemplare = this.getEsemplareDettaglio(myForm.getCodPolo(),
					myForm.getCodBib(),
					myForm.getBidDoc(),
					myForm.getCodDoc(),
					myForm.getTicket());
			if (recEsemplare != null){
				myForm.setRecEsempl(recEsemplare);
				myForm.setBidDocDescr(myForm.getRecEsempl().getBidDescr());
				myForm.setConsistenzaEsemplare(myForm.getRecEsempl().getConsDoc());
				myForm.setDataIns(myForm.getRecEsempl().getDataIns());
				myForm.setDataAgg(myForm.getRecEsempl().getDataAgg());
				titolo(myForm.getBidDoc(), myForm);
				if (request.getAttribute("esamina") != null && request.getAttribute("esamina").equals("esamina")){
					myForm.setEsamina(true);
					myForm.setDisablConsist(true);
					myForm.setDisable(true);
					return mapping.getInputForward();
				}else if (request.getAttribute("modifica") != null &&  request.getAttribute("modifica").equals("modifica")){
					myForm.setTipoOperazione("modifica");
					myForm.setDisablConsist(false);
					return mapping.getInputForward();
				}
			}else{
				//inserisce esemplare
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.esameCollocazioni
	  * EsameCollocGestioneEsemplareAction.java
	  * titolo
	  * void
	  * @param myForm
	  * @throws Exception
	  *
	  *
	 */
	private void titolo(String bid, ActionForm form)
			throws Exception {
		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		List listaTitInv = this.getTitolo(bid, myForm.getTicket());
		if (listaTitInv != null){
			if (listaTitInv.size() == 1){
				TitoloVO titolo = (TitoloVO)listaTitInv.get(0);
				if (!titolo.isFlagCondiviso()){
//					myForm.setBidDocDescr(" " + "[Loc] "+titolo.getIsbd());
					myForm.setTitoloBid(" " + "[Loc] "+titolo.getIsbd());
				}else{
//					myForm.setBidDocDescr(" "+titolo.getIsbd());
					myForm.setTitoloBid(" "+titolo.getIsbd());
					if (titolo.getNatura() != null && (titolo.getNatura().equals("M")
							|| titolo.getNatura().equals("W") || titolo.getNatura().equals("S"))){
						myForm.setAbilitaBottoneInviaInIndice(true);
					}else{
						myForm.setAbilitaBottoneInviaInIndice(false);
					}
				}
			}
		}
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		try {
			if (myForm.getTipoOperazione() != null && myForm.getTipoOperazione().equals("modifica")){
				String tipoOper = DocumentoFisicoCostant.M_MODIFICA_ESEMPL;
				myForm.getRecEsempl().setConsDoc(myForm.getConsistenzaEsemplare());
				myForm.getRecEsempl().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				boolean ret = updateEsemplare(myForm.getRecEsempl(), myForm.getRecColl().getKeyColloc(), tipoOper, myForm.getTicket());
				EsemplareDettaglioVO recEsemplare = this.getEsemplareDettaglio(myForm.getCodPolo(),
						myForm.getCodBib(),
						myForm.getBidDoc(),
						myForm.getCodDoc(),
						myForm.getTicket());
				if (recEsemplare != null){
					myForm.getRecEsempl().setDataAgg(recEsemplare.getDataAgg());
					myForm.getRecEsempl().setUteAgg(recEsemplare.getUteAgg());
				}else{
					throw new ValidationException("ricercaNoInv", ValidationException.errore);
				}
				if (ret){
					if (myForm.getProv() != null && myForm.getProv().equals("consIndiceEsemplare")){
						myForm.setProv(null);
						return inviaInIndice(mapping, request, myForm);
					}
					request.setAttribute("prov", "esemplare");
					ActionForward bookmark = Navigation.getInstance(request).goToBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL, true);
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
					this.saveErrors(request, errors);
					this.saveToken(request);
					if (bookmark != null){
						String path = bookmark.getPath();
						if (path != null && path.toUpperCase().indexOf("MODIFICACOLL") > -1){
							if (Navigation.getInstance(request).getCache().getElementAt(0).getForm() instanceof EsameCollocRicercaForm){
								return bookmark;
							}else{
								return mapping.findForward("listaInventariDelTitolo");
							}
						}
						return bookmark;
					}else{
						return Navigation.getInstance(request).goBack();
					}
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.updateEsemplareErrore"));
					this.saveErrors(request, errors);
					this.saveToken(request);
					return mapping.getInputForward();
				}
			}else{
				EsemplareVO recEsempl = new EsemplareVO();
				recEsempl.setCodPolo(myForm.getCodPolo());
				recEsempl.setCodBib(myForm.getCodBib());
				recEsempl.setBid(myForm.getBidDoc());
				recEsempl.setCodDoc(0);
				recEsempl.setConsDoc(myForm.getConsistenzaEsemplare());
				recEsempl.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				boolean ret = insertEsemplare(recEsempl, myForm.getRecColl().getKeyColloc(), myForm.getTicket());
				if (ret){

					if (myForm.getProv() != null && myForm.getProv().equals("consIndiceEsemplare")){
						myForm.setProv(null);
						return inviaInIndice(mapping, request, myForm);
					}
					request.setAttribute("prov", "esemplare");
					ActionForward bookmark = Navigation.getInstance(request).goToBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL, true);
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
					this.saveErrors(request, errors);
					this.saveToken(request);
					if (bookmark != null){
						if (bookmark.getPath() != null && bookmark.getPath().toUpperCase().indexOf("MODIFICACOLL") > -1){
							return mapping.findForward("listaInventariDelTitolo");
						}
						return bookmark;
					}else{
						return Navigation.getInstance(request).goBack();
					}
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.insertEsemplareErrore"));
					this.saveErrors(request, errors);
					this.saveToken(request);
					return mapping.getInputForward();
				}
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		try {
			ActionForward bookmark = Navigation.getInstance(request).goToBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL, true);
			if (bookmark != null){
				return bookmark;
			}else{
//				return mapping.getInputForward();
				NavigationCache navi = Navigation.getInstance(request).getCache();
				NavigationElement prev = navi.getElementAt(navi.getCurrentPosition() - 1);
				if (prev != null && prev.getForm() instanceof ListeInventariForm) {
					ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
					request.setAttribute("tipoLista", listaInvForm.getTipoLista());
					request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
					request.setAttribute("codBib", listaInvForm.getCodBib());
					request.setAttribute("descrBib", listaInvForm.getDescrBib());
					return Navigation.getInstance(request).goBack();
				}
				return Navigation.getInstance(request).goBack(true);
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		try {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico.confermaSalvaEsemplare"));
			this.saveErrors(request, errors);
			this.saveToken(request);
			myForm.setConferma(true);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm) form;

		// Inizio modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010
		if (request.getAttribute("diagnostico") != null) {
			ActionMessages errors = new ActionMessages();
//			String msg = (String) request.getAttribute("diagnostico");
			errors.add("generico", new ActionMessage(/*"error.gestioneBibliografica.testoDiagnostico", */(String) request.getAttribute("diagnostico")));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		// Fine modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010

		try{
			myForm.setProv("consIndiceEsemplare");
			myForm.setConferma(false);
			return this.ok(mapping, myForm, request, response);
		}	catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private ActionForward inviaInIndice(ActionMapping mapping,
			HttpServletRequest request, EsameCollocGestioneEsemplareForm myForm) {
		try{
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();

			AreePassaggioSifVO areePassaggioSifVO = new AreePassaggioSifVO();
			//findForward a gestione bibliografica
			areePassaggioSifVO.setOggettoDaRicercare(myForm.getBidDoc());
			areePassaggioSifVO.setDescOggettoDaRicercare(myForm.getBidDocDescr());
			areePassaggioSifVO.setNaturaOggetto("M");
			areePassaggioSifVO.setTipMatOggetto("M");
			areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
			areePassaggioSifVO.setVisualCall(false);

			UserVO utente = Navigation.getInstance(request).getUtente();
			areePassaggioSifVO.setCodBiblioteca(utente.getCodPolo().toString() + myForm.getCodBib().toString());

			if (myForm.isNuovo()){
				areePassaggioSifVO.setConsistenzaPolo(myForm.getConsistenzaEsemplare());
			}else{
				areePassaggioSifVO.setConsistenzaPolo(myForm.getRecEsempl().getConsDoc());
			}
			TreeElementViewTitoli titoloNotiziaCorrente = (TreeElementViewTitoli)myForm.getReticolo().getAnalitica().findElement(myForm.getBidDoc());
			//
			if (titoloNotiziaCorrente.isFlagCondiviso()){
				//polo = true, indice = true
				areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
			}else{
				//polo = true, indice = false
				areePassaggioSifVO.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			}
			request.setAttribute("consIndice", "consIndiceEsemplare");
			request.setAttribute("areePassaggioSifVO", areePassaggioSifVO);
			return mapping.findForward("sinteticaLocalizzazioni");

		}	catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm) form;
		// Viene settato il token per le transazioni successive
		try {
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward fascicoli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		EsameCollocGestioneEsemplareForm currentForm = (EsameCollocGestioneEsemplareForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			EsemplareDettaglioVO esemplare = currentForm.getRecEsempl();

			BibliotecaVO bib = new BibliotecaVO();
	        bib.setCod_polo(currentForm.getCodPolo());
	        bib.setCod_bib(currentForm.getCodBib());
	        bib.setNom_biblioteca(currentForm.getDescrBib());

		    return PeriodiciDelegate.getInstance(request).sifKardexPeriodicoDaEsemplare(bib, esemplare);

		} catch (Exception e) { // altri tipi di errore
			if (e instanceof SbnBaseException)
				LinkableTagUtils.addError(request, (SbnBaseException)e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setDisable(false);
			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		try {
			String tipoOper = DocumentoFisicoCostant.C_CANCELLA_ESEMPL;
			myForm.getRecEsempl().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
			boolean ret = updateEsemplare(myForm.getRecEsempl(), myForm.getRecColl().getKeyColloc(), tipoOper, myForm.getTicket());
			if (ret){
				request.setAttribute("prov", "esemplare");
				ActionForward bookmark = Navigation.getInstance(request).goToBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL, true);
				if (bookmark != null){
					if (bookmark.getPath() != null && bookmark.getPath().toUpperCase().indexOf("MODIFICACOLL") > -1){
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
						this.saveErrors(request, errors);
						this.saveToken(request);
						NavigationCache navi = Navigation.getInstance(request).getCache();
						NavigationElement prev = navi.getElementAt(0);
						if (prev != null && prev.getForm() instanceof EsameCollocRicercaForm) {
							return Navigation.getInstance(request).goBack(true);
						}else{
							return mapping.findForward("listaInventariDelTitolo");
						}

					}
					return bookmark;
				}else{
					return Navigation.getInstance(request).goBack();
				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.cancellaEsemplareErrore"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}
	public ActionForward cambiaEsemplare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocGestioneEsemplareForm myForm = (EsameCollocGestioneEsemplareForm)form;
		try {
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("bid", myForm.getBid());
			request.setAttribute("titolo", myForm.getTitoloBid());
			request.setAttribute("recColl", myForm.getRecColl());
			request.setAttribute("reticolo", myForm.getReticolo());
			request.setAttribute("codSerie", myForm.getCodSerie());
			request.setAttribute("codInv", myForm.getCodInv());
			Navigation.getInstance(request).purgeThis();
			return mapping.findForward("cambiaEsempl");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}
	private boolean insertEsemplare(EsemplareVO recEsempl, int keyLoc, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertEsemplare(recEsempl, keyLoc, ticket);
		return ret;
	}

	private EsemplareDettaglioVO getEsemplareDettaglio(String codPolo, String codBib, String bidDdoc, int codDoc, String ticket) throws Exception {
		EsemplareDettaglioVO recEsemplare = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		recEsemplare = factory.getGestioneDocumentoFisico().getEsemplareDettaglio(codPolo, codBib,	bidDdoc, codDoc, ticket);
		return recEsemplare;
	}
	private boolean updateEsemplare(EsemplareVO recEsempl, int keyLoc, String tipoOper, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateEsemplare(recEsempl, keyLoc, tipoOper, ticket);
		return ret;
	}
	private CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws Exception {
		CollocazioneDettaglioVO coll;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		coll = factory.getGestioneDocumentoFisico().getCollocazioneDettaglio(keyLoc, ticket);
		return coll;
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

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		EsameCollocGestioneEsemplareForm currentForm = (EsameCollocGestioneEsemplareForm) form;
		//almaviva5_20111104 #4717
		if (ValidazioneDati.equals(idCheck, PeriodiciConstants.FASCICOLI))
			try {
				if (currentForm.isNuovo())
					return false;

				//abilitato?
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().PERIODICI);

				//reticolo della notizia corrente
				TreeElementViewTitoli analitica = currentForm.getReticolo().getAnalitica();
				TreeElementViewTitoli node = (TreeElementViewTitoli) analitica.findElement(currentForm.getBid());
				//solo periodici
				return ValidazioneDati.equals(node.getNatura(), "S");

			} catch (Exception e) {
				log.error("", e);
				return false;
			}

		return true;
	}


}
