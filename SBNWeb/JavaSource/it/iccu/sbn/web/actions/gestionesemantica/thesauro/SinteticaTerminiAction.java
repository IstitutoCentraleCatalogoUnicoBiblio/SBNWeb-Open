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
package it.iccu.sbn.web.actions.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.SinteticaTerminiForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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

public class SinteticaTerminiAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(SinteticaTerminiAction.class);

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		SinteticaTerminiForm currentForm = (SinteticaTerminiForm) form;
		if (!currentForm.isInitialized()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				ThRicercaComuneVO ricercaComune = currentForm.getRicercaComune();
				ricercaComune.setElemBlocco((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI));

			} catch (Exception e) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.default"));
				this.setErrors(request, errors, e);
			}
		}
		return mapping.getInputForward();
	}

	protected void setErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.annulla", "annulla");
		map.put("button.analitica", "analitica");

		return map;
	}


	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String parameter)
			throws Exception {

		SinteticaTerminiForm currentForm = (SinteticaTerminiForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(SinteticaTerminiForm.SUBMIT_TERMINI_COLLEGATI) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				currentForm.setDidScelto(tokens[2]);
				return "buttonTerminiCollegati";
			}

			if (param.startsWith(SinteticaTerminiForm.SUBMIT_TITOLI_COLLEGATI) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				currentForm.setDidScelto(tokens[2]);
				return "buttonTitoliCollegati";
			}
		}
		return super.getMethodName(mapping, form, request, response, parameter);

	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTerminiForm currentForm = (SinteticaTerminiForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		RicercaThesauroListaVO ricTheLista = null;
		ThRicercaComuneVO ricSoggRicerca = null;

		if (!currentForm.isInitialized()) {

			log.info("SinteticaTerminiAction::unspecified");
			loadDefault(request, mapping, form);
			ParametriThesauro parametri = ParametriThesauro.retrieve(request);
			currentForm.setParametri(parametri);

			// devo inizializzare tramite le request.getAttribute(......)
			ricTheLista = (RicercaThesauroListaVO) parametri
					.get(ParamType.OUTPUT_SINTETICA);
			ricSoggRicerca = (ThRicercaComuneVO) parametri
					.get(ParamType.PARAMETRI_RICERCA);

			if (ricTheLista == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setListaTermini(ricTheLista);
			currentForm.setRicercaComune(ricSoggRicerca);

			currentForm.setInitialized(true);
		}
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward buttonTitoliCollegati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTerminiForm currentForm = (SinteticaTerminiForm) form;
		ElementoSinteticaThesauroVO elemento = null;
		String didScelto = currentForm.getDidScelto();
		for (Object o: currentForm.getListaTermini().getRisultati()) {
			elemento = (ElementoSinteticaThesauroVO) o;
			if (elemento.getDid().equals(didScelto)) break;
		}

		return
			ThesauroDelegate.getInstance(request).titoliCollegatiPolo(didScelto, elemento.getTermine(), mapping, false);
	}

	public ActionForward buttonTerminiCollegati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTerminiForm currentForm = (SinteticaTerminiForm) form;
		String didScelto = currentForm.getDidScelto();
		ElementoSinteticaThesauroVO elemento = null;
		for (Object o: currentForm.getListaTermini().getRisultati()) {
			elemento = (ElementoSinteticaThesauroVO) o;
			if (elemento.getDid().equals(didScelto)) break;
		}

		int elemBlocco = Integer.valueOf(currentForm.getRicercaComune().getElemBlocco());
		RicercaThesauroListaVO output =
			ThesauroDelegate.getInstance(request).ricercaTerminiPerDidCollegato(true, didScelto, elemBlocco);
		if (output == null)
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.OUTPUT_SINTETICA, output);
		parametri.put(ParamType.OGGETTO_RIFERIMENTO,
			new OggettoRiferimentoVO(true, elemento.getDid(), elemento.getTermine()) );
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("listaTermini"));
	}

	public ActionForward analitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTerminiForm currentForm = (SinteticaTerminiForm) form;
		ActionMessages errors = new ActionMessages();

		if (!ValidazioneDati.strIsNull(currentForm.getDidScelto())) {

			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			String xid = currentForm.getDidScelto();
			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,	xid);
			if (analitica == null)
				return mapping.getInputForward();

			resetToken(request);
			ParametriThesauro parametri = currentForm.getParametri().copy();
			parametri.put(ParamType.ANALITICA, analitica
					.getReticolo());
			ParametriThesauro.send(request, parametri);
			request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, xid);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaThesauro"));

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato

			return mapping.getInputForward();
		}
	}
}
