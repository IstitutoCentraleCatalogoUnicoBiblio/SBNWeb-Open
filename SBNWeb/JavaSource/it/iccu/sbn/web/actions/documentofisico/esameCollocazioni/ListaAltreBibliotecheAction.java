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
package it.iccu.sbn.web.actions.documentofisico.esameCollocazioni;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.ListaAltreBibliotecheForm;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
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

public class ListaAltreBibliotecheAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(ListaAltreBibliotecheAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.bottone.scegli", "scegli");
		map.put("documentofisico.bottone.indietro", "indietro");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		this.saveToken(request);

		ListaAltreBibliotecheForm myForm = null;
		try{
			myForm = (ListaAltreBibliotecheForm) form;
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			if (request.getAttribute("altreBib") != null){
				List listaAltreBib = (List)request.getAttribute("altreBib");
				if (listaAltreBib.size() > 0){
					if (listaAltreBib.size() == 1){
						myForm.setSelectedBiblio("0");
					}
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Altre Biblioteche");
					navigation.setTesto("Lista Altre Biblioteche");
					myForm.setListaBiblioteche(listaAltreBib);
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("noSelection", new ActionMessage("error.documentofisico.listaVuota"));
					this.saveErrors(request, errors);
					return Navigation.getInstance(request).goBack(true);
				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.listaVuota"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaAltreBibliotecheForm myForm = (ListaAltreBibliotecheForm) form;
			try {
				request.setAttribute("check", myForm.getSelectedBiblio());
				String check = myForm.getSelectedBiblio();
				int bibsel;
				if (check != null && check.length() != 0) {
					bibsel=Integer.parseInt(myForm.getSelectedBiblio());
					CodiceVO biblio = (CodiceVO) myForm.getListaBiblioteche().get(bibsel);
					request.setAttribute("codBib", biblio.getCodice());
					request.setAttribute("descrBib", biblio.getDescrizione());
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				return Navigation.getInstance(request).goBack();
			}	catch (Exception e) { // altri tipi di errore
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaAltreBibliotecheForm myForm = (ListaAltreBibliotecheForm) form;
			try {

				return Navigation.getInstance(request).goBack(true);
			}	catch (Exception e) { // altri tipi di errore
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

	}
}
