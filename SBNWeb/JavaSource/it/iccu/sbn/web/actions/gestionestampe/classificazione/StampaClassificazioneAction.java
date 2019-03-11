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
package it.iccu.sbn.web.actions.gestionestampe.classificazione;

//import it.iccu.sbn.web.integration.bo.NavigationPathBO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.classificazione.StampaClassificazioneForm;

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

public class StampaClassificazioneAction extends LookupDispatchAction {
	private StampaClassificazioneForm ricOrdini;
	private void loadCodCla() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("B","Classificazione Biblioteche");
		lista.add(elem);
		elem = new StrutturaCombo("C","Classificazione Colon");
		lista.add(elem);
		elem = new StrutturaCombo("12","Dewey 12 ed. ridotta");
		lista.add(elem);
		elem = new StrutturaCombo("13","Dewey 13 ed. ridotta");
		lista.add(elem);
		elem = new StrutturaCombo("16","Dewey 16");
		lista.add(elem);
		elem = new StrutturaCombo("19","Dewey 19 ed.");
		lista.add(elem);
		elem = new StrutturaCombo("20","Dewey 20 ed.");
		lista.add(elem);
		elem = new StrutturaCombo("21","Dewey 21 ed.");
		lista.add(elem);
		this.ricOrdini.setListaCodCla(lista);
	}


		private void loadOpzColl() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("S","SI");
			lista.add(elem);
			elem = new StrutturaCombo("N","NO");
			lista.add(elem);
			this.ricOrdini.setListaOpzColl(lista);
		}



		private void loadOpzNotzn() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("S","SI");
			lista.add(elem);
			elem = new StrutturaCombo("N","NO");
			lista.add(elem);
			this.ricOrdini.setListaOpzNotzn(lista);
		}


		private void loadOpzNtznPrpr() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("S","SI");
			lista.add(elem);
			elem = new StrutturaCombo("N","NO");
			lista.add(elem);
			this.ricOrdini.setListaOpzNtznPrpr(lista);
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
		ricOrdini = (StampaClassificazioneForm) form;

		this.loadCodCla();
		this.loadOpzColl();
		this.loadOpzNotzn();
		this.loadOpzNtznPrpr();
		ricOrdini.setElemBlocco(null);
		ricOrdini.setCodCla("21");
		ricOrdini.setInsDal("gg/mm/aaaa");
		ricOrdini.setInsAl("gg/mm/aaaa");
		ricOrdini.setAggDal("gg/mm/aaaa");
		ricOrdini.setAggAl("gg/mm/aaaa");
		ricOrdini.setOpzColl("S");
		ricOrdini.setOpzNotzn("N");
		ricOrdini.setOpzNtznPrpr("S");
		ricOrdini.setTipoRicerca("");

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricOrdini = (StampaClassificazioneForm) form;
		try {
			request.setAttribute("parametroPassato", ricOrdini.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricOrdini = (StampaClassificazioneForm) form;
		try {
			return mapping.getInputForward();
//			return mapping.findForward("conferma");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
}


