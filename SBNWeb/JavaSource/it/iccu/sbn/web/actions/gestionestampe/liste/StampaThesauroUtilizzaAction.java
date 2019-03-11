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
package it.iccu.sbn.web.actions.gestionestampe.liste;

//import it.iccu.sbn.web.integration.bo.NavigationPathBO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.liste.StampaThesauroUtilizzaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;





public class StampaThesauroUtilizzaAction extends Action {
	private StampaThesauroUtilizzaForm ricOrdini;
	private void loadCodThe() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("F","FASCISMO");
		lista.add(elem);
		elem = new StrutturaCombo("P","POESIA");
		lista.add(elem);
		elem = new StrutturaCombo("S","SPORT");
		lista.add(elem);

		this.ricOrdini.setListaCodThe(lista);
	}


		private void loadOpzSmpTit() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("S","SI");
			lista.add(elem);
			elem = new StrutturaCombo("N","NO");
			lista.add(elem);
			this.ricOrdini.setListaOpzSmpTit(lista);
		}


			private void loadOpzSmpNoteTit() throws Exception {
				List lista = new ArrayList();
				StrutturaCombo elem = new StrutturaCombo("","");
				lista.add(elem);
				elem = new StrutturaCombo("S","SI");
				lista.add(elem);
				elem = new StrutturaCombo("N","NO");
				lista.add(elem);
				this.ricOrdini.setListaOpzSmpNoteTit(lista);
			}


				private void loadOpzSmpStringa() throws Exception {
					List lista = new ArrayList();
					StrutturaCombo elem = new StrutturaCombo("","");
					lista.add(elem);
					elem = new StrutturaCombo("S","SI");
					lista.add(elem);
					elem = new StrutturaCombo("N","NO");
					lista.add(elem);
					this.ricOrdini.setListaOpzSmpStringa(lista);
				}


					private void loadOpzSmpNote() throws Exception {
						List lista = new ArrayList();
						StrutturaCombo elem = new StrutturaCombo("","");
						lista.add(elem);
						elem = new StrutturaCombo("S","SI");
						lista.add(elem);
						elem = new StrutturaCombo("N","NO");
						lista.add(elem);
						this.ricOrdini.setListaOpzSmpNote(lista);
					}



						private void loadOpzSmpThe() throws Exception {
							List lista = new ArrayList();
							StrutturaCombo elem = new StrutturaCombo("","");
							lista.add(elem);
							elem = new StrutturaCombo("S","SI");
							lista.add(elem);
							elem = new StrutturaCombo("N","NO");
							lista.add(elem);
							this.ricOrdini.setListaOpzSmpThe(lista);
						}









	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


			HttpSession httpSession = request.getSession();
			//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());
			ricOrdini = (StampaThesauroUtilizzaForm) form;







		if (request.getParameter("cerca0") != null) {
			request.setAttribute("parametroPassato", ricOrdini.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("cerca");
		}



		this.loadCodThe();
		this.loadOpzSmpTit();
		this.loadOpzSmpNoteTit();
		this.loadOpzSmpStringa();
		this.loadOpzSmpNote();
		this.loadOpzSmpThe();
		ricOrdini.setElemBlocco(null);
		ricOrdini.setCodThe("F");
		ricOrdini.setInsDal("gg/mm/aaaa");
		ricOrdini.setInsAl("gg/mm/aaaa");
		ricOrdini.setAggDal("gg/mm/aaaa");
		ricOrdini.setAggAl("gg/mm/aaaa");
		ricOrdini.setOpzSmpTit("S");
		ricOrdini.setOpzSmpNoteTit("S");
		ricOrdini.setOpzSmpStringa("S");
		ricOrdini.setOpzSmpNote("S");
		ricOrdini.setOpzSmpThe("S");
		ricOrdini.setTipoRicerca("");





//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();

	}

					}


