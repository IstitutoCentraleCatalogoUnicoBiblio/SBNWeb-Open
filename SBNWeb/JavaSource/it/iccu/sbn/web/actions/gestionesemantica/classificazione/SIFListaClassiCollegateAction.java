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
package it.iccu.sbn.web.actions.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ParametriClassi;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ParametriClassi.ClassiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.sif.AttivazioneSIFListaClassiCollegateVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.SIFListaClassiCollegateForm;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

public class SIFListaClassiCollegateAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(SIFListaClassiCollegateAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.annulla", "annulla");
		map.put("button.scegli", "scegli");
		map.put("button.blocco", "blocco");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {

			return true;
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.saveErrors(request, errors);
			log.error("", e);

			return false;
		}
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SIFListaClassiCollegateForm currentForm = (SIFListaClassiCollegateForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();



		if (!currentForm.isInitialized() ) {

			log.info("SIFListaClassiCollegateAction::unspecified");
			if (!this.initCombo(request, form, navi.getUserTicket()))
				return mapping.getInputForward();

			ParametriClassi parametri = ParametriClassi.retrieve(request);
			currentForm.setParametri(parametri);
			RicercaClasseListaVO output = (RicercaClasseListaVO) parametri.get(ClassiParamType.OUTPUT_SINTETICA);
			currentForm.setOutput(output);
			String idLista = output.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(output.getMaxRighe());
			currentForm.setNumBlocco(output.getNumBlocco());
			currentForm.setTotBlocchi(output.getTotBlocchi());
			currentForm.setTotRighe(output.getTotRighe());
			currentForm.getAppoggio().add(1);

			List<SinteticaClasseVO> risultati = output.getRisultati();
			if (ValidazioneDati.size(risultati) == 1 )
				currentForm.setCodSelezionato(risultati.get(0).getSimbolo() );

			currentForm.setInitialized(true);
			AttivazioneSIFListaClassiCollegateVO sif = (AttivazioneSIFListaClassiCollegateVO) parametri.get(ClassiParamType.SIF_ATTIVAZIONE);
			currentForm.setDatiSIF(sif);
		}

		return mapping.getInputForward();

	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SIFListaClassiCollegateForm currentForm = (SIFListaClassiCollegateForm) form;
		ActionMessages errors = new ActionMessages();

		Navigation navi = Navigation.getInstance(request);
		String classeSelezionata = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(classeSelezionata)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		String attribute = currentForm.getDatiSIF().getRequestAttribute();
		request.setAttribute(attribute, classeSelezionata);
		return navi.goBack();
	}


	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SIFListaClassiCollegateForm currentForm = (SIFListaClassiCollegateForm) form;
		ActionMessages errors = new ActionMessages();
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		HashSet<Integer> appoggio = currentForm.getAppoggio();
		int i = currentForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		RicercaClasseListaVO areaDatiPassReturn =
			delegate.nextBlocco(currentForm.getIdLista(),
				currentForm.getNumBlocco(),
				currentForm.getMaxRighe());

	if (areaDatiPassReturn == null)
		return mapping.getInputForward();

		int numBlocco = currentForm.getNumBlocco();
		currentForm.getAppoggio().add(numBlocco);

		List<SinteticaClasseVO> risultati = currentForm.getOutput().getRisultati();
		risultati.addAll(areaDatiPassReturn.getRisultati());
		Collections.sort(risultati,	SinteticaClasseVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();
	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

}


