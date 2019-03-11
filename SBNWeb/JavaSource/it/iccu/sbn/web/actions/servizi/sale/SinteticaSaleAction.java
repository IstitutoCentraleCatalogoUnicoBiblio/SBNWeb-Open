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
package it.iccu.sbn.web.actions.servizi.sale;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.web.actionforms.servizi.sale.SinteticaSaleForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class SinteticaSaleAction extends ServiziBaseAction {

	private static final String[] BOTTONIERA = new String[] {
			"servizi.bottone.esamina",
			"servizi.bottone.nuovo",
			"servizi.bottone.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esamina", "esamina");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.annulla", "annulla");

		map.put("button.blocco", "blocco");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		SinteticaSaleForm currentForm = (SinteticaSaleForm) form;
		if (currentForm.isInitialized())
			return;

		ParametriServizi params = ParametriServizi.retrieve(request);
		currentForm.setParametri(params);
		currentForm.setPulsanti(BOTTONIERA);

		currentForm.setInitialized(true);
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		SinteticaSaleForm currentForm = (SinteticaSaleForm) form;
		ParametriServizi params = currentForm.getParametri();

		List<SalaVO> sale = currentForm.getSale();
		sale.clear();

		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) params.get(ParamType.LISTA_SALE);
		currentForm.setBlocco(blocco1);
		currentForm.setBloccoSelezionato(1);
		sale.addAll(blocco1.getLista());
		if (sale.size() == 1)
			currentForm.setSelectedSala(sale.get(0).getRepeatableId());

	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSaleForm currentForm = (SinteticaSaleForm) form;

		if (!isTokenValid(request))
			saveToken(request);

		List<Integer> selected = getMultiBoxSelectedItems(currentForm.getSelectedSala());
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		SalaVO sala = BaseVO.searchRepeatableId(selected.get(0), currentForm.getSale() );
		sala = SaleDelegate.getInstance(request).getDettaglioSala(sala);
		if (sala == null) {
			return mapping.getInputForward();
		}

		ParametriServizi parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.DETTAGLIO_SALA, sala);

		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("dettaglio"));
	}


	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaSaleForm currentForm = (SinteticaSaleForm) form;

		if (!isTokenValid(request))
			saveToken(request);

		ParametriServizi parametri = currentForm.getParametri().copy();
		SalaVO ricerca = (SalaVO) parametri.get(ParamType.PARAMETRI_RICERCA);
		SalaVO sala = new SalaVO(ricerca);
		parametri.put(ParamType.DETTAGLIO_SALA, sala );

		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("nuovaSala"));

	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaSaleForm currentForm = (SinteticaSaleForm) form;
		DescrittoreBloccoVO blocco = currentForm.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2 || blocco.getIdLista() == null
				|| currentForm.getBlocchiCaricati().contains(bloccoSelezionato))
			return mapping.getInputForward();

		DescrittoreBloccoVO nextBlocco = DescrittoreBlocchiUtil.browseBlocco(
				Navigation.getInstance(request).getUserTicket(), blocco.getIdLista(),
				bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(nextBlocco.getNumBlocco());
		List<SalaVO> sale = currentForm.getSale();
		sale.addAll(nextBlocco.getLista());
		Collections.sort(sale, BaseVO.ORDINAMENTO_PER_PROGRESSIVO);

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack(true);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		SinteticaSaleForm currentForm = (SinteticaSaleForm) form;
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.esamina")) {
			List<SalaVO> sale = currentForm.getSale();
			return ValidazioneDati.isFilled(sale);
		}
		return true;
	}

}
