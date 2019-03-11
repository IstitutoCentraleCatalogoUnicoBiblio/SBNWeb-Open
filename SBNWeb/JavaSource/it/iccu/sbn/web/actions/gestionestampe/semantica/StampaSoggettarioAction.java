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
package it.iccu.sbn.web.actions.gestionestampe.semantica;

//import it.iccu.sbn.web.integration.bo.NavigationPathBO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.semantica.StampaSoggettarioForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class StampaSoggettarioAction extends LookupDispatchAction {
	private StampaSoggettarioForm stampaSoggettarioForm;
	private void loadCodSogg() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("F","Soggettario di Firenze");
		lista.add(elem);
		elem = new StrutturaCombo("P","Soggettario di Palermo");
		lista.add(elem);
		elem = new StrutturaCombo("L","Soggettario Locale");
		lista.add(elem);
		elem = new StrutturaCombo("N","Soggettario National Libra");
		lista.add(elem);

		this.stampaSoggettarioForm.setListaCodSogg(lista);
	}

	private void loadOpzMan() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		this.stampaSoggettarioForm.setListaOpzMan(lista);
	}

	private void loadOpzColl() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		this.stampaSoggettarioForm.setListaOpzColl(lista);
	}

	private void loadOpzLegame() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		this.stampaSoggettarioForm.setListaOpzLegame(lista);
	}

	private void loadOpzBibl() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		this.stampaSoggettarioForm.setListaOpzBibl(lista);
	}

	private void loadOpzStringa() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		this.stampaSoggettarioForm.setListaOpzStringa(lista);
	}

	private void loadOpzNote() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("S","SI");
		lista.add(elem);
		elem = new StrutturaCombo("N","NO");
		lista.add(elem);
		this.stampaSoggettarioForm.setListaOpzNote(lista);
	}

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession httpSession = request.getSession();
		//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());
		stampaSoggettarioForm = (StampaSoggettarioForm) form;

		this.loadCodSogg();
		this.loadOpzMan();
		this.loadOpzColl();
		this.loadOpzLegame();
		this.loadOpzBibl();
		this.loadOpzStringa();
		this.loadOpzNote();
		stampaSoggettarioForm.setElemBlocco(null);
		stampaSoggettarioForm.setCodSogg("F");
		stampaSoggettarioForm.setInsDal("gg/mm/aaaa");
		stampaSoggettarioForm.setInsAl("gg/mm/aaaa");
		stampaSoggettarioForm.setAggDal("gg/mm/aaaa");
		stampaSoggettarioForm.setAggAl("gg/mm/aaaa");
		stampaSoggettarioForm.setOpzMan("S");
		stampaSoggettarioForm.setOpzColl("S");
		stampaSoggettarioForm.setOpzLegame("S");
		stampaSoggettarioForm.setOpzBibl("S");
		stampaSoggettarioForm.setOpzStringa("N");
		stampaSoggettarioForm.setOpzNote("S");
		stampaSoggettarioForm.setTipoRicerca("");

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		stampaSoggettarioForm = (StampaSoggettarioForm) form;
		try {
			StampaSoggettarioVO stampaSoggettarioVO = new StampaSoggettarioVO();

			request.setAttribute("stampaSoggettarioVO", stampaSoggettarioVO);
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
/*
			request.setAttribute("parametroPassato", stampaSoggettarioForm.getElemBlocco());
			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.getInputForward();   */
			return mapping.findForward("conferma");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		stampaSoggettarioForm = (StampaSoggettarioForm) form;
		try {
			request.setAttribute("parametroPassato", stampaSoggettarioForm.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
}


