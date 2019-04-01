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
package it.iccu.sbn.web.actions.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoPerDescrittoriVO;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.SinteticaDescrittoriSoggettoForm;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class SinteticaDescrittoriSoggettoAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(SinteticaDescrittoriSoggettoAction.class);

	private SoggettiDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new SoggettiDelegate(
				FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	private ActionForward soggettiCollegati(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String did, String testo) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// chiamo l'ejb che mi restituisce i soggetti legati a quel did
		SoggettiDelegate ricerca = getDelegate(request);
		RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();
		ricercaComune.setPolo(true);

		RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();

		ricDid.setDid(did);
		ricercaComune.setRicercaSoggetto(ricDid);
		ricercaComune.setRicercaDescrittore(null);

		ricerca.eseguiRicerca(ricercaComune, mapping);
		request.setAttribute("outputlistadescrsog", currentForm
				.getOutputDescrittori());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		currentForm.setEnableCercaIndice(false);
		request.setAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE, currentForm.isEnableCercaIndice());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		if (currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()!=null){
			currentForm.setFolder(FolderType.FOLDER_SOGGETTI);
		}else {
			currentForm.setFolder(null);
		}
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());

		request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
				new OggettoRiferimentoVO(true, did, testo));

		return mapping.findForward("listasintetica");
	}

	private ActionForward descrittoriCollegati(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String did, String testo) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		SoggettiDelegate delegate = getDelegate(request);
		RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();

		RicercaDescrittoreVO ricDid = new RicercaDescrittoreVO();

		ricDid.setDid(did);
		//almaviva5_20101221 #4068
		ricDid.setCercaDidCollegati(true);
		//
		ricercaComune.setRicercaSoggetto(null);
		ricercaComune.setRicercaDescrittore(ricDid);
		ricercaComune.setOrdinamentoDescrittore(TipoOrdinamento.PER_ID);

		delegate.eseguiRicerca(ricercaComune, mapping);
		request.setAttribute("outputlistadescrsog", currentForm.getOutputDescrittori());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		if (currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()!=null){
			currentForm.setFolder(FolderType.FOLDER_SOGGETTI);
		}else {
			currentForm.setFolder(null);
		}
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());

		request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
				new OggettoRiferimentoVO(true, did, testo));

		return mapping.findForward("listadescrittori");
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.soggetti0", "methodSintButtonDes0");
		map.put("button.soggetti1", "methodSintButtonDes1");
		map.put("button.soggetti2", "methodSintButtonDes2");
		map.put("button.soggetti3", "methodSintButtonDes3");
		map.put("button.descrittori0", "methodSintButtonDes4");
		map.put("button.descrittori1", "methodSintButtonDes5");
		map.put("button.descrittori2", "methodSintButtonDes6");
		map.put("button.descrittori3", "methodSintButtonDes7");
		map.put("button.soggettiTutti", "methodSintButtonDes8");
		map.put("button.annulla", "annulla");

		return map;
	}

	@Override
	protected String getMethodName(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3, String arg4)
			throws Exception {

		Enumeration<?> e = arg2.getParameterNames();
		while (e.hasMoreElements()) {
			String param = (String) e.nextElement();
			if (param.indexOf("methodSintButtonDes") > -1)
				return param; // arg2.getParameter(param);
		}
		return super.getMethodName(arg0, arg1, arg2, arg3, arg4);

	}

	public ActionForward methodSintButtonDes0(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;
		String descrittore = currentForm.getPrimoDid();
		ActionForward forward = this.soggettiCollegati(mapping, form, request,
				response, descrittore, currentForm.getDescrittoriSogg() );

		return forward;
	}

	public ActionForward methodSintButtonDes4(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getPrimoDid();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, descrittore, currentForm.getDescrittoriSogg() );

		return forward;
	}

	public ActionForward methodSintButtonDes1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getSecondoDid();
		ActionForward forward = this.soggettiCollegati(mapping, form, request,
				response, descrittore, currentForm.getDescrittoriSogg1() );

		return forward;
	}

	public ActionForward methodSintButtonDes5(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getSecondoDid();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, descrittore, currentForm.getDescrittoriSogg1() );

		return forward;
	}

	public ActionForward methodSintButtonDes2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getTerzoDid();
		ActionForward forward = this.soggettiCollegati(mapping, form, request,
				response, descrittore, currentForm.getDescrittoriSogg2());

		return forward;
	}

	public ActionForward methodSintButtonDes6(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getTerzoDid();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, descrittore, currentForm.getDescrittoriSogg2());

		return forward;
	}

	public ActionForward methodSintButtonDes3(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getQuartoDid();
		ActionForward forward = this.soggettiCollegati(mapping, form, request,
				response, descrittore, currentForm.getDescrittoriSogg3() );

		return forward;
	}

	public ActionForward methodSintButtonDes7(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		String descrittore = currentForm.getQuartoDid();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, descrittore, currentForm.getDescrittoriSogg3() );

		return forward;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		if (Navigation.getInstance(request).isFromBar() ) {
			this.preparaPulsanti(currentForm, request);
			return mapping.getInputForward();
		}
		RicercaSoggettoListaVO currentFormLista = null;
		RicercaComuneVO currentFormRicerca = null;
		String chiamante = null;
		boolean isPolo = false;
		FolderType folder = null;

		if (!currentForm.isSessione()) {
			log.info("SinteticaDescrittoriSoggettoAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			currentFormLista = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			currentFormRicerca = (RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);

			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();
			folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);

			if (currentFormLista == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setOutputDescrittori(currentFormLista);
			currentForm.setRicercaComune(currentFormRicerca);
			currentForm.getRicercaComune().setPolo(isPolo);

			currentForm.setAction((String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER));

		} else {
			currentForm.setRicercaComune((RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			currentForm.setOutputDescrittori((RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescrsog"));
		}

		this.preparaPulsanti(currentForm, request);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		AreaDatiPassBiblioSemanticaVO test = (AreaDatiPassBiblioSemanticaVO) request
				.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
		if (test != null) {
			currentForm.setAreaDatiPassBiblioSemanticaVO(test);
			currentForm.setFolder(folder);
		}
		return mapping.getInputForward();

	}

	private void preparaPulsanti(ActionForm form, HttpServletRequest request) {
		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;
		ActionMessages messages = new ActionMessages();
		if (currentForm.getOutputDescrittori() != null) {
			if (currentForm.getOutputDescrittori().getRisultati().size() >= 1) {
				RicercaSoggettoPerDescrittoriVO descrittori = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutputDescrittori().getRisultati().get(0);
				currentForm.setDescrittoriSogg(descrittori.getDesDidAccettato());
				currentForm.setPrimoDid(descrittori.getDidAccettato());
				ActionMessage msg1 = new ActionMessage(
						"sintetica.contasoggetto", descrittori.getCountSogg());
				messages.add("button.soggetti0", msg1);
				ActionMessage msg2 = new ActionMessage(
						"sintetica.contasoggetto", descrittori.getCountDid());
				messages.add("button.descrittori0", msg2);
				if (currentForm.getPrimoDid().equals(SBNMarcUtil.SBNMARC_DEFAULT_ID)) {
					currentForm.setNoTutti(true);
				}

			}
			if (currentForm.getOutputDescrittori().getRisultati().size() >= 2) {
				RicercaSoggettoPerDescrittoriVO descrittori = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutputDescrittori().getRisultati().get(1);
				if (currentForm.getOutputDescrittori().getRisultati().size() > 2) {
					currentForm.setDescrittoriSogg1(descrittori
							.getDesDidAccettato());
					currentForm.setSecondoDid(descrittori.getDidAccettato());
					if (currentForm.getSecondoDid().equals(SBNMarcUtil.SBNMARC_DEFAULT_ID)) {
						currentForm.setNoTutti(true);
					}
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountSogg());
					messages.add("button.soggetti1", msg1);
					ActionMessage msg2 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountDid());
					messages.add("button.descrittori1", msg2);
				} else {
					if (currentForm.isNoTutti()) {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contasoggetto", 0);
						messages.add("button.soggettiTutti", msg1);
					} else {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contasoggetto", descrittori
										.getCountSogg());
						messages.add("button.soggettiTutti", msg1);
					}
				}

			}
			if (currentForm.getOutputDescrittori().getRisultati().size() >= 3) {
				RicercaSoggettoPerDescrittoriVO descrittori = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutputDescrittori().getRisultati().get(2);
				if (currentForm.getOutputDescrittori().getRisultati().size() > 3) {
					currentForm.setDescrittoriSogg2(descrittori
							.getDesDidAccettato());
					currentForm.setTerzoDid(descrittori.getDidAccettato());
					if (currentForm.getTerzoDid().equals(SBNMarcUtil.SBNMARC_DEFAULT_ID)) {
						currentForm.setNoTutti(true);
					}
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountSogg());
					messages.add("button.soggetti2", msg1);
					ActionMessage msg2 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountDid());
					messages.add("button.descrittori2", msg2);
				} else {
					if (currentForm.isNoTutti()) {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contasoggetto", 0);
						messages.add("button.soggettiTutti", msg1);
					} else {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contasoggetto", descrittori
										.getCountSogg());
						messages.add("button.soggettiTutti", msg1);
					}
				}

			}
			if (currentForm.getOutputDescrittori().getRisultati().size() >= 4) {
				RicercaSoggettoPerDescrittoriVO descrittori = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutputDescrittori().getRisultati().get(3);
				if (currentForm.getOutputDescrittori().getRisultati().size() > 4) {
					currentForm.setDescrittoriSogg3(descrittori
							.getDesDidAccettato());
					currentForm.setQuartoDid(descrittori.getDidAccettato());
					if (currentForm.getQuartoDid().equals(SBNMarcUtil.SBNMARC_DEFAULT_ID)) {
						currentForm.setNoTutti(true);
					}
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountSogg());
					messages.add("button.soggetti3", msg1);
					ActionMessage msg2 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountDid());
					messages.add("button.descrittori3", msg2);
				} else {
					if (currentForm.isNoTutti()) {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contasoggetto", 0);
						messages.add("button.soggettiTutti", msg1);
					} else {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contasoggetto", descrittori
										.getCountSogg());
						messages.add("button.soggettiTutti", msg1);
					}
				}
			}
			if (currentForm.getOutputDescrittori().getRisultati().size() >= 5) {
				RicercaSoggettoPerDescrittoriVO descrittori = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutputDescrittori().getRisultati().get(4);
				// //MI VIENE FORNITA LA LISTA DI TUTTI I DESCRITTORI ACCETTATI
				// INDIVIDUATI
				// currentForm.setQuintoDid(descrittori)
				if (currentForm.isNoTutti()) {
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contasoggetto", 0);
					messages.add("button.soggettiTutti", msg1);
				} else {
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contasoggetto", descrittori
									.getCountSogg());
					messages.add("button.soggettiTutti", msg1);
				}
			}
			this.saveMessages(request, messages);

		}

		if (currentForm.getDescrittoriSogg() != null
				&& currentForm.getDescrittoriSogg().length() > 0)
			currentForm.setEnableDescrittori(true);

		if (currentForm.getDescrittoriSogg1() != null
				&& currentForm.getDescrittoriSogg1().length() > 0)
			currentForm.setEnableDescrittori1(true);

		if (currentForm.getDescrittoriSogg2() != null
				&& currentForm.getDescrittoriSogg2().length() > 0)
			currentForm.setEnableDescrittori2(true);

		if (currentForm.getDescrittoriSogg3() != null
				&& currentForm.getDescrittoriSogg3().length() > 0)
			currentForm.setEnableDescrittori3(true);

	}

	public ActionForward methodSintButtonDes8(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriSoggettoForm currentForm = (SinteticaDescrittoriSoggettoForm) form;

		SoggettiDelegate ricerca = getDelegate(request);
		RicercaComuneVO ricercaComune = new RicercaComuneVO();
		RicercaSoggettoDescrittoriVO descrittoriVO = new RicercaSoggettoDescrittoriVO();
		descrittoriVO.setDid1(currentForm.getPrimoDid());
		descrittoriVO.setDid2(currentForm.getSecondoDid());
		descrittoriVO.setDid3(currentForm.getTerzoDid());
		descrittoriVO.setDid4(currentForm.getQuartoDid());
		ricercaComune.setRicercaSoggetto(descrittoriVO);
		ricercaComune.setRicercaDescrittore(null);
		ricercaComune.setOrdinamentoSoggetto(TipoOrdinamento.PER_TESTO);
		ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_INIZIALE);
		ricercaComune.setPolo(true);

		ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
				.getCodSoggettario());
		ricerca.eseguiRicerca(ricercaComune, mapping);

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute("outputlistadescrsog", currentForm
				.getOutputDescrittori());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		currentForm.setEnableCercaIndice(false);
		request.setAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE, currentForm.isEnableCercaIndice());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		if (currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()!=null){
			currentForm.setFolder(FolderType.FOLDER_SOGGETTI);
		}else {
			currentForm.setFolder(null);
		}
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		return mapping.findForward("listasintetica");
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

}
