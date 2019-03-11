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
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoPerDescrittoriVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.SinteticaDescrittoriParoleForm;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.Enumeration;
import java.util.HashMap;
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
import org.apache.struts.actions.LookupDispatchAction;

public class SinteticaDescrittoriParoleAction extends LookupDispatchAction {

	private static Log log = LogFactory
			.getLog(SinteticaDescrittoriParoleAction.class);

	private SoggettiDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new SoggettiDelegate(
				FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	private ActionForward descrittoriCollegati(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String parola) throws Exception {

		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// chiamo l'ejb che mi restituisce i descrittori legati a quella parola
		SoggettiDelegate ricerca = getDelegate(request);
		RicercaComuneVO ricercaComune = new RicercaComuneVO();
		ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
				.getCodSoggettario());
		ricercaComune.setPolo(true);

		RicercaDescrittoreVO ricDid = new RicercaDescrittoreVO();
		ricDid.setParole(parola);
		ricercaComune.setOrdinamentoDescrittore(TipoOrdinamento.PER_TESTO);
		ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_INIZIALE);
		ricercaComune.setRicercaSoggetto(null);
		ricercaComune.setRicercaDescrittore(ricDid);
		ricerca.eseguiRicerca(ricercaComune, mapping);
		request
				.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune()
						.isPolo());

		request.setAttribute("outputlistadescrpar", currentForm.getOutput());
		currentForm.setEnableCercaIndice(false);
		request.setAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE, currentForm.isEnableCercaIndice());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		if (currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()!=null){
			currentForm.setFolder(FolderType.FOLDER_SOGGETTI);
		}else {
			currentForm.setFolder(null);
		}
		return mapping.findForward("listasintetica");
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.parola0", "methodSintButtonParole0");
		map.put("button.parola1", "methodSintButtonParole1");
		map.put("button.parola2", "methodSintButtonParole2");
		map.put("button.parola3", "methodSintButtonParole3");
		map.put("button.paroleTutte", "methodSintButtonParole4");
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
			if (param.indexOf("methodSintButtonParole") > -1)
				return param; // arg2.getParameter(param);
		}
		return super.getMethodName(arg0, arg1, arg2, arg3, arg4);

	}

	public ActionForward methodSintButtonParole0(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;
		String parola = currentForm.getPrimaParola();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, parola);

		return forward;
	}

	public ActionForward methodSintButtonParole1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;
		String parola = currentForm.getSecondaParola();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, parola);

		return forward;
	}

	public ActionForward methodSintButtonParole2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;

		String parola = currentForm.getTerzaParola();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, parola);

		return forward;
	}

	public ActionForward methodSintButtonParole3(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;

		String parola = currentForm.getQuartaParola();
		ActionForward forward = this.descrittoriCollegati(mapping, form,
				request, response, parola);

		return forward;
	}

	public ActionForward methodSintButtonParole4(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;
		SoggettiDelegate ricerca = getDelegate(request);
		RicercaComuneVO ricercaComune = new RicercaComuneVO();
		RicercaDescrittoreVO paroleVO = new RicercaDescrittoreVO();
		paroleVO.setDid1(currentForm.getPrimaParola());
		paroleVO.setDid2(currentForm.getSecondaParola());
		paroleVO.setDid3(currentForm.getTerzaParola());
		paroleVO.setDid4(currentForm.getQuartaParola());
		ricercaComune.setRicercaSoggetto(null);
		ricercaComune.setRicercaDescrittore(paroleVO);
		ricercaComune.setOrdinamentoDescrittore(TipoOrdinamento.PER_TESTO);
		ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_INIZIALE);
		ricercaComune.setPolo(true);

		ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
				.getCodSoggettario());
		ricerca.eseguiRicerca(ricercaComune, mapping);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		request.setAttribute("outputlistadescrpar", currentForm.getOutput());
		currentForm.setEnableCercaIndice(false);
		request.setAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE, currentForm.isEnableCercaIndice());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		if (currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()!=null){
			currentForm.setFolder(FolderType.FOLDER_SOGGETTI);
		}else {
			currentForm.setFolder(null);
		}
		return mapping.findForward("listasintetica");
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;

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
			log.info("SinteticaDescrittoriParoleAction::unspecified");
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
			currentForm.setOutput(currentFormLista);
			currentForm.setRicercaComune(currentFormRicerca);
			currentForm.getRicercaComune().setPolo(isPolo);

			currentForm.setAction((String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER));

		} else {
			currentForm.setRicercaComune((RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			currentForm.setOutput((RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescrpar"));
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

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	private void preparaPulsanti(ActionForm form, HttpServletRequest request) {
		SinteticaDescrittoriParoleForm currentForm = (SinteticaDescrittoriParoleForm) form;
		ActionMessages messages = new ActionMessages();
		if (currentForm.getOutput() != null) {
			if (currentForm.getOutput().getRisultati().size() >= 1) {
				RicercaSoggettoPerDescrittoriVO parole = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutput().getRisultati().get(0);
				currentForm.setParole(parole.getDesDidAccettato());
				currentForm.setPrimaParola(parole.getDesDidAccettato());
				ActionMessage msg1 = new ActionMessage("sintetica.contaparole",
						parole.getCountDid());
				messages.add("button.parola0", msg1);
				if (parole.getCountDid() == 0) {
					currentForm.setNoTutti(true);
				}

			}

			if (currentForm.getOutput().getRisultati().size() >= 2) {
				RicercaSoggettoPerDescrittoriVO parole = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutput().getRisultati().get(1);
				if (currentForm.getOutput().getRisultati().size() > 2) {
					currentForm.setParole1(parole.getDesDidAccettato());
					currentForm.setSecondaParola(parole.getDesDidAccettato());
					if (parole.getCountDid() == 0) {
						currentForm.setNoTutti(true);
					}
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contaparole", parole.getCountDid());
					messages.add("button.parola1", msg1);
				} else {
					if (currentForm.isNoTutti()) {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contaparole", 0);
						messages.add("button.paroleTutte", msg1);
					} else {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contaparole", parole.getCountDid());
						messages.add("button.paroleTutte", msg1);
					}
				}
			}

			if (currentForm.getOutput().getRisultati().size() >= 3) {
				RicercaSoggettoPerDescrittoriVO parole = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutput().getRisultati().get(2);
				if (currentForm.getOutput().getRisultati().size() > 3) {
					currentForm.setParole2(parole.getDesDidAccettato());
					currentForm.setTerzaParola(parole.getDesDidAccettato());
					if (parole.getCountDid() == 0) {
						currentForm.setNoTutti(true);
					}
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contaparole", parole.getCountDid());
					messages.add("button.parola2", msg1);
				} else {
					if (currentForm.isNoTutti()) {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contaparole", 0);
						messages.add("button.paroleTutte", msg1);
					} else {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contaparole", parole.getCountDid());
						messages.add("button.paroleTutte", msg1);
					}
				}
			}

			if (currentForm.getOutput().getRisultati().size() >= 4) {
				RicercaSoggettoPerDescrittoriVO parole = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutput().getRisultati().get(3);
				if (currentForm.getOutput().getRisultati().size() > 4) {
					currentForm.setParole3(parole.getDesDidAccettato());
					currentForm.setQuartaParola(parole.getDesDidAccettato());
					if (parole.getCountDid() == 0) {
						currentForm.setNoTutti(true);
					}
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contaparole", parole.getCountDid());
					messages.add("button.parola3", msg1);
				} else {
					if (currentForm.isNoTutti()) {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contaparole", 0);
						messages.add("button.paroleTutte", msg1);
					} else {
						ActionMessage msg1 = new ActionMessage(
								"sintetica.contaparole", parole.getCountDid());
						messages.add("button.paroleTutte", msg1);
					}
				}
			}

			if (currentForm.getOutput().getRisultati().size() >= 5) {
				RicercaSoggettoPerDescrittoriVO descrittori = (RicercaSoggettoPerDescrittoriVO) currentForm
						.getOutput().getRisultati().get(4);
				// //MI VIENE FORNITA LA LISTA DI TUTTI I DESCRITTORI ACCETTATI
				// INDIVIDUATI
				// currentForm.setQuintoDid(descrittori)
				if (currentForm.isNoTutti()) {
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contaparole", 0);
					messages.add("button.paroleTutte", msg1);
				} else {
					ActionMessage msg1 = new ActionMessage(
							"sintetica.contaparole", descrittori.getCountSogg());
					messages.add("button.paroleTutte", msg1);
				}
			}
			this.saveMessages(request, messages);

		}

		if (currentForm.getParole() != null && currentForm.getParole().length() > 0)
			currentForm.setEnableParole(true);

		if (currentForm.getParole1() != null && currentForm.getParole1().length() > 0)
			currentForm.setEnableParole1(true);

		if (currentForm.getParole2() != null && currentForm.getParole2().length() > 0)
			currentForm.setEnableParole2(true);

		if (currentForm.getParole3() != null && currentForm.getParole3().length() > 0)
			currentForm.setEnableParole3(true);

	}

}
