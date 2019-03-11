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
package it.iccu.sbn.web.actions.periodici;

import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.web.actionforms.periodici.ListaEsemplariForm;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaEsemplariAction extends PeriodiciListaAction {

	private static String[] BOTTONIERA = new String[] {
		"button.periodici.chiudi" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("button.periodici.chiudi", "chiudi");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return Navigation.getInstance(request).goBack(true);
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		ListaEsemplariForm currentForm = (ListaEsemplariForm) form;
		if (currentForm.isInitialized())
			return;

		ParametriPeriodici parametri = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(parametri);
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoVO<FascicoloVO>) parametri.get(ParamType.PARAMETRI_RICERCA);
		currentForm.setBiblioteca(ricerca.getBiblioteca());
		currentForm.setOggettoRiferimento((OggettoRiferimentoVO) parametri.get(ParamType.OGGETTO_RIFERIMENTO));
		KardexPeriodicoVO kardex = (KardexPeriodicoVO) parametri.get(ParamType.LISTA_ESEMPLARI_FASCICOLO);
		currentForm.setKardex(kardex);
		resetBlocchi(request, currentForm);

		Navigation navi = Navigation.getInstance(request);
		SerieFascicoloVO sf = (SerieFascicoloVO) ricerca.getOggettoRicerca();
		if (sf.isEsemplariInPolo())
			navi.setTesto(".periodici.listaEsemplari.POLO.testo");
		else
			navi.setTesto(".periodici.listaEsemplari.BIB.testo");

		currentForm.setPulsanti(BOTTONIERA);
		super.init(request, form);

		currentForm.setInitialized(true);
	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		super.loadForm(request, form);
	}

	@Override
	public ActionForward posiziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	@Override
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaEsemplariForm currentForm = (ListaEsemplariForm) form;
		return internalBlocco(mapping, form, request, response, currentForm.getRicercaKardex().getComparator());
	}

	@Override
	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

}
