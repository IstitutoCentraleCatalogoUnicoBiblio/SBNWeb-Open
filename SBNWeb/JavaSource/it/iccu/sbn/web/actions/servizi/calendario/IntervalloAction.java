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
package it.iccu.sbn.web.actions.servizi.calendario;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.FasciaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.calendario.FasciaDecorator;
import it.iccu.sbn.vo.custom.servizi.calendario.IntervalloDecorator;
import it.iccu.sbn.vo.validators.calendario.ModelloCalendarioValidator;
import it.iccu.sbn.web.actionforms.servizi.calendario.IntervalloForm;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class IntervalloAction extends CalendarioBaseAction implements SbnAttivitaChecker {

	private static final String DELETE_ALL = "all";

	static Logger log = Logger.getLogger(IntervalloAction.class);

	private static final String[] BOTTONIERA = new String[] {
			"servizi.bottone.conferma",
			"servizi.bottone.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.conferma", "conferma");
		map.put("servizi.bottone.annulla", "annulla");

		map.put("servizi.bottone.aggiungi", "add");
		map.put("servizi.bottone.rimuovi", "delete");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		IntervalloForm currentForm = (IntervalloForm) form;
		if (!currentForm.isInitialized() )
			init(request, form);

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);

		IntervalloForm currentForm = (IntervalloForm) form;
		ParametriServizi params = ParametriServizi.retrieve(request);
		currentForm.setParametri(params);
		BibliotecaVO bib = (BibliotecaVO) params.get(ParamType.BIBLIOTECA);
		if (bib != null)
			currentForm.setBiblioteca(bib.getCod_bib());

		ModelloCalendarioVO modello = ((CalendarioVO) params.get(ParamType.MODELLO_CALENDARIO)).copy();
		currentForm.setModello(modello);
		IntervalloVO intervallo = new IntervalloDecorator((IntervalloVO) params.get(ParamType.INTERVALLO_CALENDARIO));
		currentForm.setIntervallo(intervallo);

		currentForm.setListaCategorieMediazione(CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE));

		currentForm.setPulsanti(BOTTONIERA);

		currentForm.setInitialized(true);
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IntervalloForm currentForm = (IntervalloForm) form;
		String action_index = currentForm.getAction_index();
		log.debug("action_index: " + action_index);

		String[] tokens = action_index.split(":");

		List<FasciaVO> fasce = (List<FasciaVO>) PropertyUtils.getSimpleProperty(currentForm, tokens[0]);
		FasciaDecorator f = new FasciaDecorator();
		fasce.add(f);

		//anchor
		request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + f.getUniqueId() );

		return mapping.getInputForward();
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IntervalloForm currentForm = (IntervalloForm) form;
		String action_index = currentForm.getAction_index();
		log.debug("action_index: " + action_index);

		String[] tokens = action_index.split(":");
		String day = tokens[0];
		//check cancellazione totale intervallo
		if (ValidazioneDati.equals(day, DELETE_ALL)) {
			currentForm.getIntervallo().clear();
			return mapping.getInputForward();
		}

		int idx = Integer.valueOf(tokens[1]).intValue();

		List<FasciaVO> fasce = (List<FasciaVO>) PropertyUtils.getSimpleProperty(currentForm, day);
		fasce.remove(idx);

		//anchor
		request.setAttribute("anchor", day);

		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IntervalloForm currentForm = (IntervalloForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();
		IntervalloVO intervallo = currentForm.getIntervallo();

		try {
			List<IntervalloVO> intervalli = modello.getIntervalli();
			if (intervallo.isNuovo()) {
				modello.addIntervallo(intervallo);

			} else {
				//sostituisce intervallo
				int idx = intervallo.getId() - 1;
				intervalli.remove(idx);
				modello.addIntervallo(intervallo);
			}

			modello.validate(new ModelloCalendarioValidator() );

			checkCongruenzaCalendarioBib(request, modello);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		ParametriServizi parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODELLO_CALENDARIO, modello);
		ParametriServizi.send(request, parametri);

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack(true);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		return true;
	}

}
