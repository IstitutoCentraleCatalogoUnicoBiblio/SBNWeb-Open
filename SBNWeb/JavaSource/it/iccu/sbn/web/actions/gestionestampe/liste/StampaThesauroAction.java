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
import it.iccu.sbn.web.actionforms.gestionestampe.liste.StampaThesauroForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




public class StampaThesauroAction extends Action {
	private StampaThesauroForm ricOrdini;
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




		private void loadOpzTit() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("S","SI");
			lista.add(elem);
			elem = new StrutturaCombo("N","NO");
			lista.add(elem);
			this.ricOrdini.setListaOpzTit(lista);
		}


			private void loadOpzNoteThe() throws Exception {
				List lista = new ArrayList();
				StrutturaCombo elem = new StrutturaCombo("","");
				lista.add(elem);
				elem = new StrutturaCombo("S","SI");
				lista.add(elem);
				elem = new StrutturaCombo("N","NO");
				lista.add(elem);
				this.ricOrdini.setListaOpzNoteThe(lista);
			}



				private void loadOpzStringa() throws Exception {
					List lista = new ArrayList();
					StrutturaCombo elem = new StrutturaCombo("","");
					lista.add(elem);
					elem = new StrutturaCombo("S","SI");
					lista.add(elem);
					elem = new StrutturaCombo("N","NO");
					lista.add(elem);
					this.ricOrdini.setListaOpzStringa(lista);
				}


					private void loadOpzNote() throws Exception {
						List lista = new ArrayList();
						StrutturaCombo elem = new StrutturaCombo("","");
						lista.add(elem);
						elem = new StrutturaCombo("S","SI");
						lista.add(elem);
						elem = new StrutturaCombo("N","NO");
						lista.add(elem);
						this.ricOrdini.setListaOpzNote(lista);
					}




						private void loadOpzBibl() throws Exception {
							List lista = new ArrayList();
							StrutturaCombo elem = new StrutturaCombo("","");
							lista.add(elem);
							elem = new StrutturaCombo("S","SI");
							lista.add(elem);
							elem = new StrutturaCombo("N","NO");
							lista.add(elem);
							this.ricOrdini.setListaOpzBibl(lista);
						}

						private void loadOpzRlzn() throws Exception {
							List lista = new ArrayList();
							StrutturaCombo elem = new StrutturaCombo("","");
							lista.add(elem);
							elem = new StrutturaCombo("S","SI");
							lista.add(elem);
							elem = new StrutturaCombo("N","NO");
							lista.add(elem);
							this.ricOrdini.setListaOpzRlzn(lista);
						}



						private void loadOpzForme() throws Exception {
							List lista = new ArrayList();
							StrutturaCombo elem = new StrutturaCombo("","");
							lista.add(elem);
							elem = new StrutturaCombo("S","SI");
							lista.add(elem);
							elem = new StrutturaCombo("N","NO");
							lista.add(elem);
							this.ricOrdini.setListaOpzForme(lista);
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







	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


			HttpSession httpSession = request.getSession();
			//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());
			ricOrdini = (StampaThesauroForm) form;







		if (request.getParameter("cerca0") != null) {
			request.setAttribute("parametroPassato", ricOrdini.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("cerca");
		}



		this.loadCodThe();
		this.loadOpzTit();
		this.loadOpzNoteThe();
		this.loadOpzStringa();
		this.loadOpzNote();
		this.loadOpzBibl();
		this.loadOpzRlzn();
		this.loadOpzForme();
		this.loadOpzColl();
		ricOrdini.setElemBlocco(null);
		ricOrdini.setCodThe("F");
		ricOrdini.setInsDal("gg/mm/aaaa");
		ricOrdini.setInsAl("gg/mm/aaaa");
		ricOrdini.setAggDal("gg/mm/aaaa");
		ricOrdini.setAggAl("gg/mm/aaaa");
		ricOrdini.setOpzTit("S");
		ricOrdini.setOpzNoteThe("S");
		ricOrdini.setOpzStringa("N");
		ricOrdini.setOpzNote("S");
		ricOrdini.setOpzBibl("S");
		ricOrdini.setOpzRlzn("S");
		ricOrdini.setOpzForme("S");
		ricOrdini.setOpzColl("S");
		ricOrdini.setTipoRicerca("");




//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();

	}

					}


