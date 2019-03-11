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
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareListaVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocRicercaEsemplareForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
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

public class EsameCollocRicercaEsemplareAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsameCollocRicercaEsemplareAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.esameEsemplRicercaTab1", "tab1");
		map.put("documentofisico.esameEsemplRicercaTab2", "tab2");
		map.put("documentofisico.bottone.sceltaEsempl", "sceltaEsempl");
		map.put("documentofisico.bottone.esColl", "esColl");
		map.put("documentofisico.bottone.sceltaTit", "sceltaTit");
//		map.put("documentofisico.bottone.ok", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		//almaviva5_20130419 segnalazione RMR
		map.put("botton.blocco", "blocco");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		EsameCollocRicercaEsemplareForm currentForm = (EsameCollocRicercaEsemplareForm) form;
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				} catch (Exception e) {
					errors.clear();
					errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
					this.saveErrors(request, errors);
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
		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		// controllo se ho già i dati in sessione;
		if(!myForm.isSessione())	{
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			loadDefault(request, mapping, form);
			myForm.setSessione(true);
			log.debug("EsameCollocRicercaEsemplareAction::unspecified");
		}
		if (myForm.getReticolo() != null){
		}else{
			if((request.getAttribute("codBib") != null
					&& request.getAttribute("descrBib") != null
					&& request.getAttribute("bid") != null
					&& request.getAttribute("titolo") != null
					&& request.getAttribute("recColl") != null
					&& request.getAttribute("reticolo") != null
					&& request.getAttribute("codSerie") != null
					&& request.getAttribute("codInv") != null)){
				//per visualizzare la pagina mi servono i dati dell'inventario, i dati del reticolo
				myForm.setCodBib((String)request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
				myForm.setBid((String)request.getAttribute("bid"));
				myForm.setTitolo((String)request.getAttribute("titolo"));
				myForm.setRecCollDett((CollocazioneDettaglioVO)request.getAttribute("recColl"));
				myForm.setCodSerie((String)request.getAttribute("codSerie"));
				myForm.setCodInv(request.getAttribute("codInv").toString());
				if (myForm.getRecCollDett().getBidDescr() != null){
					List lista = this.getTitolo(myForm.getRecCollDett().getBid(), myForm.getTicket());
					if (lista != null){
						if (lista.size() == 1){
							TitoloVO titolo = (TitoloVO)lista.get(0);
							myForm.getRecCollDett().setBidDescr(titolo.getIsbd());
						}
					}
				}
				myForm.setReticolo((DatiBibliograficiCollocazioneVO)request.getAttribute("reticolo"));
				myForm.setReticoloTitoli(myForm.getReticolo().getListaTitoliCollocazione());
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.errorePassaggioParametri"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}
		}
		try{
			List listaEsemplariReticolo = this.getListaEsemplariReticolo(myForm.getCodPolo(), myForm.getCodBib(),
					myForm.getReticoloTitoli(), myForm.getTicket(), myForm.getNRec(), form);
			myForm.setCodSez(myForm.getRecCollDett().getCodSez());
			myForm.setCodCollocazione(myForm.getRecCollDett().getCodColloc());
			myForm.setCodSpecificazione(myForm.getRecCollDett().getSpecColloc());

			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getCodPolo(), myForm.getCodBib(), null);

			if (listaEsemplariReticolo!=null && listaEsemplariReticolo.size()>0){
				if (listaEsemplariReticolo.size() == 1){
					myForm.setSelectedEsemRetic("0");
				}
				myForm.setListaEsemplReticolo(listaEsemplariReticolo);
				myForm.setFolder("tab1");
				myForm.setTab1(true);
			}else{
				myForm.setFolder("tab2");
				CollocazioneTitoloVO[] reticolo = myForm.getReticoloTitoli();
				CollocazioneTitoloVO recRet = null;
				CollocazioneTitoloVO collTitoli = null;
				List lista = new ArrayList();
				if (reticolo.length == 1){
					myForm.setSelectedTit("0");
				}
				for (int i = 0; i < reticolo.length; i++) {
					recRet = reticolo[i];
					collTitoli = new CollocazioneTitoloVO();
					collTitoli.setCodPolo(myForm.getCodPolo());
					collTitoli.setCodBib(myForm.getCodBib());
					collTitoli.setBid(recRet.getBid());
					collTitoli.setNatura(recRet.getNatura());
					collTitoli.setIsbd(recRet.getIsbd());
					lista.add(collTitoli);
				}

				myForm.setNoColl(false);
				myForm.setCodBib(myForm.getCodBib());
				myForm.setDescrBib(myForm.getDescrBib());
				myForm.setTitoliEsempl(lista);
			}
		}	catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
//			return Navigation.getInstance(request).goBack();
			return mapping.getInputForward();
		}	catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
//			return Navigation.getInstance(request).goBack();
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
//			return Navigation.getInstance(request).goBack();
			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);
		try {

			return mapping.findForward("ok");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);
		try {
			request.setAttribute("prov", "ricercaEsemplare");
			return Navigation.getInstance(request).goBack(true);
			//return mapping.findForward("chiudi");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sceltaEsempl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);
		try {
			String check = myForm.getSelectedEsemRetic();
			int esemsel;
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			if (check != null && check.length() != 0) {
				esemsel = Integer.parseInt(myForm.getSelectedEsemRetic());
				EsemplareListaVO scelEsempl = (EsemplareListaVO) myForm.getListaEsemplReticolo().get(esemsel);
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("bid", myForm.getBid());
				request.setAttribute("titolo", myForm.getTitolo());
				request.setAttribute("recEsemplareReticolo", scelEsempl);
				request.setAttribute("recColl", myForm.getRecCollDett());
				request.setAttribute("reticolo", myForm.getReticolo());
				request.setAttribute("codSerie", myForm.getCodSerie());
				request.setAttribute("codInv", myForm.getCodInv());
				request.setAttribute("prov", "ricercaEsemplare");
				request.setAttribute("modifica", "modifica");
				return mapping.findForward("sceltaEsempl");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward esColl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			String check = myForm.getSelectedEsemRetic();
			int esemsel;
			if (check != null && check.length() != 0) {
				esemsel = Integer.parseInt(myForm.getSelectedEsemRetic());
				EsemplareListaVO scelEsempl = (EsemplareListaVO) myForm.getListaEsemplReticolo().get(esemsel);

				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("bid", myForm.getBid());
				request.setAttribute("titolo", myForm.getTitolo());
				EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
				paramRicerca.setCodPolo(scelEsempl.getCodPolo());
				paramRicerca.setCodBib(scelEsempl.getCodBib());
				paramRicerca.setCodPoloSez(myForm.getCodPolo());
				paramRicerca.setCodBibSez(myForm.getCodBib());
				paramRicerca.setCodSez(myForm.getCodSez());
				paramRicerca.setCodPoloDoc(scelEsempl.getCodPolo());
				paramRicerca.setCodBibDoc(scelEsempl.getCodBib());
				paramRicerca.setBidDoc(scelEsempl.getBid());
				paramRicerca.setCodDoc(scelEsempl.getCodDoc());

				request.setAttribute("listaCollEsemplare", "listaCollEsemplare");
				request.setAttribute("paramRicerca", paramRicerca);
				request.setAttribute("recColl", myForm.getRecCollDett());

				return Navigation.getInstance(request).goForward(mapping.findForward("esameColloc"));
//				return mapping.findForward("esameColloc");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward sceltaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);
		try {
			String check = myForm.getSelectedTit();
			int esemsel;
			if (check != null && check.length() != 0) {
				esemsel = Integer.parseInt(myForm.getSelectedTit());
				CollocazioneTitoloVO scelEsempl = (CollocazioneTitoloVO) myForm.getTitoliEsempl().get(esemsel);

				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("bid", myForm.getBid());
				request.setAttribute("titolo", myForm.getTitolo());
				scelEsempl.setCodDoc(0);
				request.setAttribute("recCollocazionetitolo", scelEsempl);
				request.setAttribute("reticolo", myForm.getReticolo());
				request.setAttribute("recColl", myForm.getRecCollDett());
				request.setAttribute("codSerie", myForm.getCodSerie());
				request.setAttribute("codInv", myForm.getCodInv());
				request.setAttribute("prov", "ricercaEsemplare");
				request.setAttribute("nuovo", "nuovo");
				return mapping.findForward("sceltaTitolo");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		try{
			if (myForm.getFolder().equals("tab1")){
				int numBlocco = myForm.getBloccoSelezionato();
				String idLista = myForm.getIdLista();
				String ticket = Navigation.getInstance(request).getUserTicket();
				if (numBlocco>1 && idLista != null) {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
					if (bloccoVO != null) {
						myForm.getListaEsemplReticolo().addAll(bloccoVO.getLista());
					}
				}
			}
			return mapping.getInputForward();

		}catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward tab1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);
		try{
			if (myForm.getListaEsemplReticolo()!=null){
				myForm.setFolder("tab1");
//				CollocazioneTitoloVO[] reticolo = myForm.getReticolo().getListaTitoliCollocazione();
//				CollocazioneTitoloVO recRet = null;
//				CollocazioneTitoloVO collTit = null;
//				List lista = new ArrayList();
////				String tipoOperazione = DocumentoFisicoCostant.LISTA_DA_RETICOLO_4;
//				for (int i = 0; i < reticolo.length; i++) {
//				recRet = (CollocazioneTitoloVO)reticolo[i];
//				collTit = new CollocazioneTitoloVO();
//				collTit.setBid(recRet.getBid());
//				collTit.setNatura(recRet.getNatura());
//				collTit.setIsbd(recRet.getIsbd());
//				lista.add(collTit);
//				}
				if (myForm.getListaEsemplReticolo().size() == 1){
					myForm.setSelectedEsemRetic("0");
				}
				myForm.setNoColl(false);
				myForm.setCodBib(myForm.getCodBib());
				myForm.setDescrBib(myForm.getDescrBib());
//				myForm.setTitoliEsempl(lista);
			}else{
				myForm.setNoColl(true);
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("bid", myForm.getBid());
				request.setAttribute("titolo", myForm.getTitolo());
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.noEsemplariReticolo"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward tab2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		request.setAttribute("currentForm", form);

		try{
			myForm.setFolder("tab2");
			CollocazioneTitoloVO[] reticolo = myForm.getReticoloTitoli();
			CollocazioneTitoloVO recRet = null;
			CollocazioneTitoloVO collTitoli = null;
			List lista = new ArrayList();
			for (int i = 0; i < reticolo.length; i++) {
				recRet = reticolo[i];
				collTitoli = new CollocazioneTitoloVO();
				collTitoli.setBid(recRet.getBid());
				collTitoli.setNatura(recRet.getNatura());
				collTitoli.setIsbd(recRet.getIsbd());
				lista.add(collTitoli);
			}
			myForm.setNoColl(false);
			if (lista.size() == 1){
				myForm.setSelectedTit("0");
			}
			myForm.setCodBib(myForm.getCodBib());
			myForm.setDescrBib(myForm.getDescrBib());
			myForm.setTitoliEsempl(lista);
			myForm.setTab1(true);

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	private List getListaEsemplariReticolo(String codPolo, String codBib,  CollocazioneTitoloVO[] reticolo, String ticket, int nRec, ActionForm form)
	throws Exception {
		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		DescrittoreBloccoVO blocco1;
		List titoli;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaEsemplariReticolo(codPolo, codBib, reticolo, nRec, ticket);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			myForm.setNoReticolo(true);
			return null;
		}else{
			myForm.setNoReticolo(false);
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
	private List getTitolo(String bid, String ticket) throws Exception {
		List titolo = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		EsameCollocRicercaEsemplareForm myForm = (EsameCollocRicercaEsemplareForm) form;
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
