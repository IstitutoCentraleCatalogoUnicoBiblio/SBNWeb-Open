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
package it.iccu.sbn.web.actions.amministrazionesistema.biblioteca.sif;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheBaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheSistemaMetropolitanoVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.sif.ListaBibliotecheAffiliateForm;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
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

public class ListaBibliotecheAffiliateAction extends SinteticaLookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("documentofisico.bottone.scegli", "scegli");
		map.put("documentofisico.bottone.indietro", "annulla");
		map.put("button.blocco", "blocco");

		//almaviva5_20111007
		map.put("button.selAllTitoli", "tutti");
		map.put("button.deSelAllTitoli", "nessuno");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaBibliotecheAffiliateForm currentForm = (ListaBibliotecheAffiliateForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		if (!currentForm.isSessione())
			init(request, currentForm);

		return mapping.getInputForward();
	}

	protected void init(HttpServletRequest request, ActionForm form) {

		Navigation navi = Navigation.getInstance(request);
		ListaBibliotecheAffiliateForm currentForm = (ListaBibliotecheAffiliateForm) form;

		SIFListaBibliotecheBaseVO attivazioneSIF =
			(SIFListaBibliotecheBaseVO) navi.getAttribute(BibliotecaDelegate.ATTIVAZIONE_SIF);
		currentForm.setAttivazioneSIF(attivazioneSIF);
		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) navi
				.getAttribute(BibliotecaDelegate.LISTA_BIBLIOTECHE_AFFILIATE);
		currentForm.setIdLista(blocco1.getIdLista());
		currentForm.setBloccoCorrente(1);
		currentForm.setTotBlocchi(blocco1.getTotBlocchi());
		currentForm.setMaxRighe(blocco1.getMaxRighe());
		currentForm.setTotRighe(blocco1.getTotRighe());
		currentForm.setListaBiblioteche(blocco1.getLista());

		List<BibliotecaVO> biblioteche = currentForm.getListaBiblioteche();

		//almaviva5_20091126 descrizione su navigazione
		String descrizione = attivazioneSIF.getTestoNavigazione();
		if (ValidazioneDati.isFilled(descrizione))
			navi.setTesto(descrizione);

		if (blocco1.getTotRighe() == 1) {
			currentForm.setSelectedBiblio(biblioteche.get(0).getRepeatableId() );
			currentForm.setMultiBiblio(new Integer[] { biblioteche.get(0).getRepeatableId() });
		}

		//almaviva5_20091127 mod per biblioteche sistema metropolitano
		if (attivazioneSIF instanceof SIFListaBibliotecheSistemaMetropolitanoVO) {
			SIFListaBibliotecheSistemaMetropolitanoVO sif = (SIFListaBibliotecheSistemaMetropolitanoVO) attivazioneSIF;
			if (sif.isSelezionaTutti() ) {
				Integer[] selez = new Integer[biblioteche.size()];
				for (int i = 0; i < selez.length; i++)
					selez[i] = biblioteche.get(i).getRepeatableId();
				currentForm.setMultiBiblio(selez);
			}
		}

		//almaviva5_20110728 #4588
		Integer[] selected = attivazioneSIF.getSelected();
		if (ValidazioneDati.isFilled(selected))
			currentForm.setMultiBiblio(selected);

		currentForm.setSessione(true);
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaBibliotecheAffiliateForm currentForm = (ListaBibliotecheAffiliateForm) form;

		Integer selected = currentForm.getSelectedBiblio();
		List<Integer> items = getMultiBoxSelectedItems(currentForm.getMultiBiblio());
		SIFListaBibliotecheBaseVO attivazioneSIF = currentForm.getAttivazioneSIF();

		if ((!ValidazioneDati.isFilled(selected) && !attivazioneSIF.isMultiSelezione())
				|| (!ValidazioneDati.isFilled(items) && attivazioneSIF.isMultiSelezione())) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		String requestAttribute = attivazioneSIF.getRequestAttribute();
		List<BibliotecaVO> listaBiblioteche = currentForm.getListaBiblioteche();

		if (attivazioneSIF.isMultiSelezione()) {
			List<BibliotecaVO> output = new ArrayList<BibliotecaVO>();
			for (Integer id : items)
				output.add(BibliotecaVO.searchRepeatableId(id, listaBiblioteche));

			request.setAttribute(requestAttribute, output);

		} else
			request.setAttribute(requestAttribute, BibliotecaVO.searchRepeatableId(selected, listaBiblioteche));

		return Navigation.getInstance(request).goBack();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaBibliotecheAffiliateForm currentForm = (ListaBibliotecheAffiliateForm) form;
		BibliotecaDelegate delegate = new BibliotecaDelegate(request);
		DescrittoreBloccoVO blocco = delegate.caricaBlocco(currentForm.getIdLista(), currentForm.getBloccoCorrente() );
		if (blocco == null || blocco.getRowCount() < 1)
			return mapping.getInputForward();

		List<BibliotecaVO> listaBib = currentForm.getListaBiblioteche();
		listaBib.addAll(blocco.getLista());
		Collections.sort(listaBib, BibliotecaVO.ORDINAMENTO_PER_PROGRESSIVO);

		return mapping.getInputForward();
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaBibliotecheAffiliateForm currentForm = (ListaBibliotecheAffiliateForm) form;
		List<BibliotecaVO> biblioteche = currentForm.getListaBiblioteche();
		int size = ValidazioneDati.size(biblioteche);
		if (size > 0) {
			Integer[] selected = new Integer[size];
			for (int idx = 0; idx < size; idx++)
				selected[idx] = biblioteche.get(idx).getRepeatableId();

			currentForm.setMultiBiblio(selected);
		}

		return mapping.getInputForward();
	}

	public ActionForward nessuno(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaBibliotecheAffiliateForm currentForm = (ListaBibliotecheAffiliateForm) form;
		currentForm.setMultiBiblio(null);
		return mapping.getInputForward();
	}

}
