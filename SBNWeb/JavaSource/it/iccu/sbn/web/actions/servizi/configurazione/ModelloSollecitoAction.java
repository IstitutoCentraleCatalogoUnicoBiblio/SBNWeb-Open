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
package it.iccu.sbn.web.actions.servizi.configurazione;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.CampoSollecito;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.FormattazioneCampo;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.util.servizi.SollecitiUtil;
import it.iccu.sbn.web.actionforms.servizi.configurazione.ModelloSollecitoForm;
import it.iccu.sbn.web.actions.common.SbnDownloadAction;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.FileUtil;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ModelloSollecitoAction extends NavigationBaseAction {

	private static final String[] BOTTONIERA = new String[] {
		"servizi.bottone.ok", "servizi.bottone.stampa",
		"servizi.bottone.annulla"};

	private static final String[] BOTTONIERA_CONFERMA = new String[] {
		"servizi.bottone.si", "servizi.bottone.no" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.stampa", "stampa");
		map.put("servizi.bottone.annulla", "annulla");

		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");

		//folders
		map.put("servizi.configurazione.sollecito.lettera", "lettera");
		map.put("servizi.utenti.email", "email");
		map.put("servizi.configurazione.sollecito.sms", "sms");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form)
			throws Exception {
		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		if (currentForm.isInitialized())
			return;

		currentForm.setPulsanti(BOTTONIERA);
		ParametriBibliotecaVO parametri = (ParametriBibliotecaVO) request.getAttribute(NavigazioneServizi.PARAMETRI_BIBLIOTECA);
		currentForm.setParametri(parametri);
		ModelloSollecitoVO modello = parametri.getModelloSollecito();
		modello = (ModelloSollecitoVO) ((modello != null) ? modello.copy() : SollecitiUtil.getModelloSollecitoBase());
		currentForm.setModello(modello);

		initCombo(request, currentForm);

		currentForm.setInitialized(true);
	}

	private void initCombo(HttpServletRequest request, ActionForm form)
			throws Exception {
		Locale locale = getLocale(request);
		List<ComboVO> listaCampiModello = new ArrayList<ComboVO>();
		for (CampoSollecito cs : ModelloSollecitoVO.CampoSollecito.values()) {
			ComboVO field = new ComboVO('[' + cs.getField() + ']', LinkableTagUtils.findMessage(request, locale, cs.getDescr()));
			listaCampiModello.add(field);
		}

		List<ComboVO> listaFormattazione = new ArrayList<ComboVO>();
		for (FormattazioneCampo fc : ModelloSollecitoVO.FormattazioneCampo.values()) {
			ComboVO opt = new ComboVO(fc.getTag(), LinkableTagUtils.findMessage(request, locale, fc.getDescr()));
			listaFormattazione.add(opt);
		}

		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		currentForm.setListaCampiModello(listaCampiModello);
		currentForm.setListaFormattazione(listaFormattazione);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		ModelloSollecitoVO modello = currentForm.getModello();
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		try {
			delegate.validaModelloSollecito(modello, TipoModello.LETTERA);
			delegate.validaModelloSollecito(modello, TipoModello.EMAIL);
			delegate.validaModelloSollecito(modello, TipoModello.SMS);
		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));
		currentForm.setConferma(true);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);

		return mapping.getInputForward();
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		ModelloSollecitoVO modello = currentForm.getModello();
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		String fileName = null;
		try {
			fileName = delegate.validaModelloSollecito(modello, TipoModello.LETTERA);
		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(fileName));
			return SbnDownloadAction.downloadFile(request, "modelloSollecito.pdf",	FileUtil.getData(in));
		} finally {
			FileUtil.close(in);
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		ModelloSollecitoVO modello = currentForm.getModello();
		ParametriBibliotecaVO parametri = currentForm.getParametri();
		parametri.setModelloSollecito(modello);

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ParametriBibliotecaVO newParams = factory.getGestioneServizi().aggiornaParametriBiblioteca(navi.getUserTicket(), parametri);
			if (newParams != null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				request.setAttribute(NavigazioneServizi.PARAMETRI_BIBLIOTECA, newParams);
				return navi.goBack();
			} else {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
				return no(mapping, currentForm, request, response);
			}
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
		} finally {
			no(mapping, currentForm, request, response);
		}

		return no(mapping, currentForm, request, response);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		currentForm.setConferma(false);
		currentForm.setPulsanti(BOTTONIERA);

		return mapping.getInputForward();
	}

	public ActionForward lettera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		currentForm.setFolder(TipoModello.LETTERA);

		return mapping.getInputForward();
	}

	public ActionForward email(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		currentForm.setFolder(TipoModello.EMAIL);

		return mapping.getInputForward();
	}

	public ActionForward sms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelloSollecitoForm currentForm = (ModelloSollecitoForm) form;
		currentForm.setFolder(TipoModello.SMS);

		return mapping.getInputForward();
	}
}
