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
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneReticoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareListaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocEsaminaPossedutoForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class EsameCollocEsaminaPossedutoAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsameCollocEsaminaPossedutoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "blocco");
		map.put("documentofisico.esaminaPossTab1", "tab1");
		map.put("documentofisico.esaminaPossTab2", "tab2");
		map.put("documentofisico.esaminaPossTab3", "tab3");
		map.put("documentofisico.bottone.esInv1", "esameInventario");
		map.put("documentofisico.bottone.esEsempl", "esameEsemplare");
		map.put("documentofisico.bottone.inventari", "inventari");
		map.put("documentofisico.bottone.collocazioni", "collocazioni");
		map.put("documentofisico.bottone.esemplari", "esemplari");
		map.put("documentofisico.bottone.altreBib", "altreBib");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.disponibilita", "disponibilita");
		map.put("documentofisico.bottone.possessori", "possessori");
		map.put("documentofisico.bottone.esamina", "esamina");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);

		try{
			EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
			Navigation navi = Navigation.getInstance(request);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
//			currentForm.setPosseduto(this.getDatiPosseduto(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getBid(), currentForm.getTicket()), form);
			if (navi.isFromBar() )
				return mapping.getInputForward();

			if (!currentForm.isSessione()) {
				log.debug("EsameCollocEsaminaPossedutoAction::unspecified");
				currentForm.setTicket(navi.getUserTicket());
				currentForm.setCodPolo(navi.getUtente().getCodPolo());
				currentForm.setCodBib(navi.getUtente().getCodBib());
				currentForm.setDescrBib(navi.getUtente().getBiblioteca());
				currentForm.setSessione(true);
			}
			if (request.getAttribute("codBib") != null){
				currentForm.setCodBib((String)request.getAttribute("codBib"));
				currentForm.setDescrBib((String)request.getAttribute("descrBib"));

				//almaviva5_20150309 reset liste al cambio biblioteca
				currentForm.setListaInventariDelTitolo(null);
				currentForm.setListaCollocazioniDelTitolo(null);
				currentForm.setListaEsemplariDelTitolo(null);
			}
			if (request.getAttribute("scelInv") != null){
				currentForm.setRecInvTit((InventarioTitoloVO)request.getAttribute("scelInv"));
			}
			if (request.getAttribute("AreePassaggioSifVO") != null){
				currentForm.setDatiGestioneBibliografica((AreePassaggioSifVO)request.getAttribute("AreePassaggioSifVO"));
				currentForm.setBid(currentForm.getDatiGestioneBibliografica().getOggettoDaRicercare());
				currentForm.setTitolo(currentForm.getDatiGestioneBibliografica().getDescOggettoDaRicercare());
			}
//			else{
//
//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.errorePassaggioParametriDaGestBibl"));
//
//				return mapping.getInputForward();
//			}
			currentForm.setBid(currentForm.getBid());
			boolean[] posseduto = this.getDatiPosseduto(currentForm.getCodPolo(),
					currentForm.getCodBib(), currentForm.getBid(), currentForm.getTicket(), currentForm);
			currentForm.setPosseduto(posseduto);
			if (posseduto != null){
				if (posseduto[0] != false){
					currentForm.setTab1(true);
					currentForm.setTastoInv(true);
					if (posseduto[1] != false){
						currentForm.setTab2(false);
						currentForm.setTastoColl(true);
						if (posseduto[2] != false){
							currentForm.setTab3(false);
							currentForm.setTastoEsempl(true);
						}
					}
				}else{
					currentForm.setTab1(false);
					currentForm.setTastoInv(false);
					if (posseduto[1] != false){
						currentForm.setTab2(true);
						currentForm.setTastoColl(true);
						if (posseduto[2] != false){
							currentForm.setTab3(false);
							currentForm.setTastoEsempl(true);
						}else{
							currentForm.setTab3(false);
							currentForm.setTastoEsempl(false);
						}
					}else{
						currentForm.setTab2(false);
						currentForm.setTastoColl(false);
						if (posseduto[2] != false){
							currentForm.setTab3(true);
							currentForm.setTastoEsempl(true);
						}else{
							currentForm.setTab3(false);
							currentForm.setTastoEsempl(false);
						}
					}
				}
				if (posseduto[0] != false){
					//tab1 attivo
					currentForm.setTab1(true);
					currentForm.setTastoInv(true);
					currentForm.getParamRicerca().setKeyLoc(currentForm.getRecInvTit().getKeyLoc());
					List listaInv = this.getListaInventariTitolo(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getBid(), currentForm.getTicket(), currentForm.getNRec(), form);
					if (!ValidazioneDati.isFilled(listaInv))  {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));

						return navi.goBack(true);
					}else{
						if (listaInv.size() == 1)
							currentForm.setSelectedInv("0");

						currentForm.setListaInventariDelTitolo(listaInv);
					}
				}//else{
				if (posseduto[1]){
					currentForm.setTastoColl(true);
					List listaColl = this.getListaCollocazioniTitolo(currentForm.getCodPolo(),
							currentForm.getCodBib(),  currentForm.getBid(), currentForm.getTicket(), currentForm.getNRec(), currentForm);
					if (!ValidazioneDati.isFilled(listaColl))  {
						currentForm.setNoColl(true);
						currentForm.setTab2(false);
						currentForm.setTastoColl(false);
					}else {
						if (listaColl.size() == 1)
							currentForm.setSelectedColl("0");

						currentForm.setNoColl(false);
						currentForm.setCodBib(currentForm.getCodBib());
						currentForm.setDescrBib(currentForm.getDescrBib());
						currentForm.setListaCollocazioniDelTitolo(listaColl);
//						return mapping.getInputForward();
					}
				}//else{
				if (posseduto[2]){
					List listaEsempl = this.getListaEsemplariTitolo(currentForm.getCodPolo(),
							currentForm.getCodBib(), currentForm.getBid(), currentForm.getTicket(), currentForm.getNRec(), currentForm);
					if (!ValidazioneDati.isFilled(listaEsempl) )  {
						currentForm.setNoEsem(true);
						currentForm.setTab3(false);
						currentForm.setTastoEsempl(false);
					}else {
						if (listaEsempl.size() == 1)
							currentForm.setSelectedEsem("0");

						currentForm.setNoEsem(false);
						currentForm.setCodBib(currentForm.getCodBib());
						currentForm.setDescrBib(currentForm.getDescrBib());
						currentForm.setListaEsemplariDelTitolo(listaEsempl);
//						return mapping.getInputForward();
					}
				}
				//abilitazione tasto altre bib
				if ((posseduto[0] || posseduto[1] || posseduto[2]) &&
						posseduto[3] || posseduto[4] || posseduto[5]) {
					currentForm.setTastoAltreBib(true);
				}else if ((!posseduto[0] && !posseduto[1] && !posseduto[2]) &&
						(posseduto[3] || posseduto[4] || posseduto[5])){
//					currentForm.setTastoAltreBib(true);
					List listaBib = this.getAltreBib(currentForm.getBid(), null, currentForm.getTicket(), currentForm);
					if (ValidazioneDati.isFilled(listaBib) ){
						request.setAttribute("altreBib", listaBib);
						return mapping.findForward("altreBib");
					}
					// e visualizza lista altre bib che possiedono il bid in esame
				}else if ((posseduto[0] || posseduto[1] || posseduto[2]) &&
						(!posseduto[3] || !posseduto[4] || !posseduto[5])){
					currentForm.setTastoAltreBib(false);
				}

				if (!posseduto[0] &&
						!posseduto[1] &&
						!posseduto[2] &&
						!posseduto[3] &&
						!posseduto[4] &&
						!posseduto[5]){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noPosseduto"));

					return navi.goBack(true);
				}
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noPosseduto"));

				return navi.goBack(true);
			}

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		request.setAttribute("currentForm", form);
		try {
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		request.setAttribute("currentForm", form);
		try {
			try {

				return Navigation.getInstance(request).goBack(true);
			}	catch (Exception e) { // altri tipi di errore

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward altreBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		request.setAttribute("currentForm", form);
		try {

			List listaBib = this.getAltreBib(currentForm.getBid(), currentForm.getCodBib(), currentForm.getTicket(), currentForm);
			if (listaBib.size() > 0){
				request.setAttribute("altreBib", listaBib);
				return mapping.findForward("altreBib");
			}
			return mapping.getInputForward();//per il momento

		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		String ticket = Navigation.getInstance(request).getUserTicket();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try{
			if (currentForm.isTab1()){
				int numBlocco1 = currentForm.getBloccoSelezionato1();
				String idLista1 = currentForm.getIdLista1();
				if (numBlocco1 > 1 && idLista1 != null) {
					DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista1, numBlocco1);
					if (bloccoVO != null) {
						List inventari = currentForm.getListaInventariDelTitolo();
						inventari.addAll(bloccoVO.getLista());
						Collections.sort(inventari);
					}
				}
			}else if (currentForm.isTab2()){
				int numBlocco2 = currentForm.getBloccoSelezionato2();
				String idLista2 = currentForm.getIdLista2();
				if (numBlocco2 > 1 && idLista2 != null) {
					DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista2, numBlocco2);
					if (bloccoVO != null) {
						List collocazioni = currentForm.getListaCollocazioniDelTitolo();
						collocazioni.addAll(bloccoVO.getLista());
						Collections.sort(collocazioni);
					}
				}
			}else if (currentForm.isTab3()){
				int numBlocco3 = currentForm.getBloccoSelezionato3();
				String idLista3 = currentForm.getIdLista3();
				if (numBlocco3 > 1 && idLista3 != null) {
					DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista3, numBlocco3);
					if (bloccoVO != null) {
						List esemplari = currentForm.getListaEsemplariDelTitolo();
						esemplari.addAll(bloccoVO.getLista());
						Collections.sort(esemplari);
					}
				}
			}
			return mapping.getInputForward();

		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	public ActionForward tab1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		request.setAttribute("currentForm", form);
		Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
		try{
			//inventari
			// sono state rilevati inventari ma non si ha ancora la lista
			List listaInv = currentForm.getListaInventariDelTitolo();
			if (currentForm.isTastoInv() && (!ValidazioneDati.isFilled(listaInv) )) {
				listaInv = this.getListaInventariTitolo(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getBid(), currentForm.getTicket(), currentForm.getNRec(), form);
				if (!ValidazioneDati.isFilled(listaInv) ) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));

					return Navigation.getInstance(request).goBack(true);
				}else{
					currentForm.setListaInventariDelTitolo(listaInv);
				}
			}
			if (!ValidazioneDati.isFilled(listaInv) )  {
				currentForm.setNoInv(true);

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noListaInvTit"));

				return mapping.getInputForward();
			}else{
				currentForm.setTab1(true);
				currentForm.setTab2(false);
				currentForm.setTab3(false);
				currentForm.setNoInv(false);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//			return this.tab1(mapping, form,	request, response);
			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward tab2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//collocazioni

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		request.setAttribute("currentForm", form);
		currentForm.setFolder("tab2");
		Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
		try{
			// sono state rilevate collocazioni ma non si ha ancora la lista
			List listaColl = currentForm.getListaCollocazioniDelTitolo();
			if (currentForm.isTastoColl() && (!ValidazioneDati.isFilled(listaColl) )) {
				listaColl = this.getListaCollocazioniTitolo(currentForm.getCodPolo(),
						currentForm.getCodBib(),  currentForm.getBid(), currentForm.getTicket(), currentForm.getNRec(), currentForm);
				if (!ValidazioneDati.isFilled(listaColl))  {
					currentForm.setNoColl(true);
					currentForm.setTab2(false);
					currentForm.setTastoColl(false);

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noLista"));

					return mapping.getInputForward();
				}else {
					if (listaColl.size() == 1)
						currentForm.setSelectedColl("0");

					currentForm.setNoColl(false);
					currentForm.setListaCollocazioniDelTitolo(listaColl);
					currentForm.setTab1(false);
					currentForm.setTab2(true);
					currentForm.setTab3(false);
					currentForm.setNoColl(false);
					return mapping.getInputForward();
				}
			}
			if (!ValidazioneDati.isFilled(listaColl) ) {
					currentForm.setNoColl(true);

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noListaCollTit"));

				}else{
					currentForm.setTab1(false);
					currentForm.setTab2(true);
					currentForm.setTab3(false);
					currentForm.setNoColl(false);
				}

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return this.tab1(mapping, form,	request, response);
//			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}

	public ActionForward tab3(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
		try{
			// sono state rilevati esemplari ma non si ha ancora la lista
			List listaEsempl = currentForm.getListaEsemplariDelTitolo();
			if (currentForm.isTastoEsempl() && (!ValidazioneDati.isFilled(listaEsempl) )) {
				listaEsempl = this.getListaEsemplariTitolo(currentForm.getCodPolo(),
						currentForm.getCodBib(), currentForm.getBid(), currentForm.getTicket(), currentForm.getNRec(), currentForm);
				if (!ValidazioneDati.isFilled(listaEsempl) )  {
					currentForm.setNoEsem(true);
					currentForm.setTab3(false);
//					currentForm.setTastoEsempl(false);

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noLista"));

					return mapping.getInputForward();
				}else {
					if (listaEsempl.size() == 1)
						currentForm.setSelectedEsem("0");

					currentForm.setTab1(false);
					currentForm.setTab2(false);
					currentForm.setTab3(true);
					currentForm.setNoEsem(false);
					currentForm.setListaEsemplariDelTitolo(listaEsempl);
					return mapping.getInputForward();
				}
			}
			if (!ValidazioneDati.isFilled(listaEsempl) )  {
				currentForm.setNoEsem(true);

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noListaEsemplTit"));

			}else{
				currentForm.setTab1(false);
				currentForm.setTab2(false);
				currentForm.setTab3(true);
				currentForm.setNoColl(false);
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}



	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.setAttribute("currentForm", form);
		// Viene settato il token per le transazioni successive
		try {
			return mapping.findForward("chiudi");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward esameInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

			if (!currentForm.isNoInv()){

				int invSel;
				request.setAttribute("checkS", currentForm.getSelectedInv());
				String check = currentForm.getSelectedInv();
				if (check != null && check.length() != 0) {
					invSel = Integer.parseInt(currentForm.getSelectedInv());
					InventarioVO scelInv = (InventarioVO) currentForm.getListaInventariDelTitolo().get(invSel);
					request.setAttribute("codBib",currentForm.getCodBib());
					request.setAttribute("codSerie",scelInv.getCodSerie());
					request.setAttribute("codInvent",scelInv.getCodInvent());
					request.setAttribute("descrBib",currentForm.getDescrBib());
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

			return mapping.getInputForward();
		}
	}
	public ActionForward esameEsemplare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {
			int collsel;
			request.setAttribute("check", currentForm.getSelectedColl());
			String check = currentForm.getSelectedColl();
			if (check != null && check.length() != 0) {
				collsel = Integer.parseInt(currentForm.getSelectedColl());
				CollocazioneReticoloVO scelColl = (CollocazioneReticoloVO) currentForm.getListaCollocazioniDelTitolo().get(collsel);
				if (scelColl.getCodDoc() == 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneNonLegataAdEsemplare"));

					return mapping.getInputForward();
				}else{
					CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(scelColl.getKeyColloc(), currentForm.getTicket());
					if (coll != null){
						request.setAttribute("codBib", currentForm.getCodBib());
						request.setAttribute("descrBib", currentForm.getDescrBib());
						request.setAttribute("bid", coll.getBid());
						request.setAttribute("titolo", coll.getBidDescr());
						request.setAttribute("recColl", coll);
						request.setAttribute("codSerie", "");//è fittizio, ma ci deve stare
						request.setAttribute("codInv", String.valueOf("0"));//è fittizio, ma ci deve stare
						request.setAttribute("reticolo", "esamina");
						request.setAttribute("esamina", "esamina");
						return mapping.findForward("esEsempl");
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCollocazioneInesistente"));

						return mapping.getInputForward();
					}


//					currentForm.setRecEsempl(this.getEsemplareDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(),
//					scelColl.getBidDoc(), scelColl.getCodDoc(), currentForm.getTicket()));
//					if (currentForm.getRecEsempl()!=null){
//					request.setAttribute("bid", scelColl.getBid());
//					request.setAttribute("bidDescr", scelColl.getDescrBid());
//					request.setAttribute("titolo", scelColl.getDescrBid());
//					request.setAttribute("codDoc", scelColl.getCodDoc());
//					request.setAttribute("bidDoc", scelColl.getBidDoc());
//					request.setAttribute("bidDocDescr", scelColl.getdBidDoc());
//					request.setAttribute("esamina", "esamina");
//					return mapping.findForward("esEsempl");

//					request.setAttribute("esamina","esamina");
//					request.setAttribute("descrBib",currentForm.getDescrBib());
//					request.setAttribute("recEsemplare",currentForm.getRecEsempl());
//					return mapping.findForward("esEsempl");
				}
			}else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}
	public ActionForward inventari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		try {
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
//			request.setAttribute("codSez",currentForm.getCodSezione());
			int collSel;
			request.setAttribute("checkS", currentForm.getSelectedColl());
			String check = currentForm.getSelectedColl();
			if (check != null && check.length() != 0) {
				collSel = Integer.parseInt(currentForm.getSelectedColl());
				CollocazioneReticoloVO scelColl = (CollocazioneReticoloVO) currentForm.getListaCollocazioniDelTitolo().get(collSel);
				EsameCollocRicercaVO paramRicerca = (EsameCollocRicercaVO) currentForm.getParamRicerca().clone();
				paramRicerca.setCodPolo(scelColl.getCodPolo());
				paramRicerca.setCodBib(scelColl.getCodBib());
				paramRicerca.setCodSez(scelColl.getCodSez());
				paramRicerca.setKeyLoc(scelColl.getKeyColloc());
				paramRicerca.setCodLoc(scelColl.getCodColloc());
				paramRicerca.setCodSpec(scelColl.getSpecColloc());
				paramRicerca.setOrdLst("I");
				request.setAttribute("paramRicerca", paramRicerca);
				String chiamante = Navigation.getInstance(request).getActionCaller();

				if (chiamante.toUpperCase().indexOf("ESAMECOLLOCRICERCAESEMPLARE") > -1) {
					request.setAttribute("tipoLista","listaInvDiCollocDaEsempl");
				}else{
					request.setAttribute("prov","esaminaPosseduto");
					request.setAttribute("tipoLista","listaInvDiColloc");
				}
				return Navigation.getInstance(request).goForward(mapping.findForward("inventari"));
//				return mapping.findForward("inventari");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward collocazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() ){
				return mapping.getInputForward();
			}
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			//			request.setAttribute("codSez",currentForm.getCodSezione());
			int collSel;
			request.setAttribute("checkS", currentForm.getSelectedEsem());
			String check = currentForm.getSelectedEsem();
			if (check != null && check.length() != 0) {
				collSel = Integer.parseInt(currentForm.getSelectedEsem());
				EsemplareListaVO scelEsempl = (EsemplareListaVO) currentForm.getListaEsemplariDelTitolo().get(collSel);
				EsameCollocRicercaVO paramRicerca = (EsameCollocRicercaVO) currentForm.getParamRicerca().clone();
				paramRicerca.setCodPolo(scelEsempl.getCodPolo());
				paramRicerca.setCodBib(scelEsempl.getCodBib());
				paramRicerca.setCodPoloDoc(scelEsempl.getCodPolo());
				paramRicerca.setCodBibDoc(scelEsempl.getCodBib());
				paramRicerca.setBidDoc(scelEsempl.getBid());
				paramRicerca.setCodDoc(scelEsempl.getCodDoc());
				paramRicerca.setCodBibSez(currentForm.getCodBib());
				request.setAttribute("paramRicerca", paramRicerca);
				request.setAttribute("prov","posseduto");
				request.setAttribute("listaCollEsemplare","listaCollEsemplare");
				return mapping.findForward("collocazioni");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward possessori(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession httpSession = request.getSession();

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", currentForm.getSelectedInv());
			String check = currentForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(currentForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) currentForm.getListaInventariDelTitolo().get(invSel);
				request.setAttribute("codBib",currentForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent",""+scelInv.getCodInvent());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				request.setAttribute("prov", "DOCFISVIS");

				//return mapping.getInputForward();
				return mapping.findForward("possessori");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward disponibilita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", currentForm.getSelectedInv());
			String check = currentForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(currentForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) currentForm.getListaInventariDelTitolo().get(invSel);
				InventarioTitoloVO invTit = new InventarioTitoloVO(scelInv);
				MovimentoVO movimento = new MovimentoVO();
				movimento.setCodPolo(currentForm.getCodPolo());
				movimento.setCodBibOperante(currentForm.getCodBib());
				movimento.setCodBibInv(scelInv.getCodBib());
				movimento.setCodSerieInv(scelInv.getCodSerie());
				movimento.setCodInvenInv(String.valueOf(scelInv.getCodInvent()));
				request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, movimento);
				InfoDocumentoVO infoDoc = new InfoDocumentoVO();
				infoDoc.setInventarioTitoloVO(invTit);
				request.setAttribute("InfoDocumentoVO", infoDoc);
				request.setAttribute("TipoRicerca", RicercaRichiesteType.RICERCA_PER_INVENTARIO);
				return mapping.findForward("SIFServizi");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (currentForm.isTab1()){
				return this.esameInventario(mapping, currentForm, request, response);
			} else if (currentForm.isTab2()){
				return this.esameEsemplare(mapping, currentForm, request, response);
			} else if (currentForm.isTab3()){
				return this.esameEsemplare(mapping, currentForm, request, response);
			}else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	private CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws Exception {
		CollocazioneDettaglioVO coll;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		coll = factory.getGestioneDocumentoFisico().getCollocazioneDettaglio(keyLoc, ticket);
		return coll;
	}
	//--------------------------------------------------------------------------------------------------
	private boolean [] getDatiPosseduto(String codPolo, String codBib, String bid, String ticket, ActionForm form)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		boolean [] poss = {false, false, false, false};
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		poss = factory.getGestioneDocumentoFisico().getDatiPosseduto(codPolo, codBib, bid, ticket);
		if (poss[0]==false && poss[1]==false && poss[2]==false)  {
			currentForm.setNoPosseduto(true);
			if (poss[3]==false)  {

			}
		}else{
			currentForm.setNoPosseduto(false);
		}
		return poss;
	}
	private List getListaInventariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec, ActionForm form)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariTitolo(codPolo, codBib, bid, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			currentForm.setNoInv(true);
			return null;
		}else{
			currentForm.setNoInv(false);
			currentForm.setIdLista1(blocco1.getIdLista());
			currentForm.setTotBlocchi1(blocco1.getTotBlocchi());
			currentForm.setTotRighe1(blocco1.getTotRighe());
			currentForm.setBloccoSelezionato1(blocco1.getNumBlocco());
			currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				currentForm.setAbilitaBottoneCarBlocchi1(false);
			}else{
				currentForm.setAbilitaBottoneCarBlocchi1(true);
			}
			return blocco1.getLista();
		}
	}
	private List getListaCollocazioniTitolo(String codPolo, String codBib, String bid, String ticket, int nRec, ActionForm form)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaCollocazioniTitolo(codPolo, codBib, bid, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			currentForm.setNoColl(true);
			return null;
		}else{
			currentForm.setNoColl(false);
			currentForm.setIdLista2(blocco1.getIdLista());
			currentForm.setTotBlocchi2(blocco1.getTotBlocchi());
			currentForm.setTotRighe2(blocco1.getTotRighe());
			currentForm.setBloccoSelezionato2(blocco1.getNumBlocco());
			currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				currentForm.setAbilitaBottoneCarBlocchi2(false);
			}else{
				currentForm.setAbilitaBottoneCarBlocchi2(true);
			}
			return blocco1.getLista();
		}
	}
	private List getListaEsemplariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec, ActionForm form)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaEsemplariTitolo(codPolo, codBib, bid, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			currentForm.setNoEsem(true);
			return null;
		}else{
			currentForm.setNoEsem(false);
			currentForm.setIdLista3(blocco1.getIdLista());
			currentForm.setTotBlocchi3(blocco1.getTotBlocchi());
			currentForm.setTotRighe3(blocco1.getTotRighe());
			currentForm.setBloccoSelezionato3(blocco1.getNumBlocco());
			currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				currentForm.setAbilitaBottoneCarBlocchi3(false);
			}else{
				currentForm.setAbilitaBottoneCarBlocchi3(true);
			}
			return blocco1.getLista();
		}
	}
	private List getAltreBib(String bid, String codBib, String ticket, ActionForm form)
	throws Exception {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		List listaBib = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaBib = factory.getGestioneDocumentoFisico().getAltreBib(bid, codBib, ticket);
		if (listaBib == null ||  listaBib.size() < 1)  {
			currentForm.setTastoAltreBib(false);
			return null;
		}else{
			return listaBib;
		}
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		EsameCollocEsaminaPossedutoForm currentForm = (EsameCollocEsaminaPossedutoForm) form;
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_ESAME_POSSEDUTO, currentForm.getCodPolo(), currentForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
