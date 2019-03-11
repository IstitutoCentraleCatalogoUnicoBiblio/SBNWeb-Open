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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		Alessandro Segnalini - Inizio Codifica Marzo 2008

package it.iccu.sbn.web.actions.documentofisico.possessori;


import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.documentofisico.TreeElementViewPossessori;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriAnaliticaForm;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriAnaliticaAction extends LookupDispatchAction   implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(PossessoriSinteticaAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("analitica.button.dettaglio", "dettaglioPossessore");
		map.put("analitica.button.inventari", "inventariPossessori");

		map.put("analitica.button.nuovolegame", "creaLegamePossessore");

		return map;

	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(Navigation.getInstance(request).isFromBar() ) {
					return mapping.getInputForward();
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		String keyTree ="";
		String descTree = "";
		List listaPossBySint = (List)request.getAttribute("listaPossessoriBySint");
		String pid = (String)request.getAttribute("pidBySint");
		if (pid != null) {
			keyTree = pid ;

		}
		TreeElementViewPossessori root=null;
		PossessoriAnaliticaForm myForm = (PossessoriAnaliticaForm)form;

		// creazione dell treeView
		if (request.getAttribute("treeElementViewPossessori")!=null){
			root =(TreeElementViewPossessori)request.getAttribute("treeElementViewPossessori");
		} else
			root = new TreeElementViewPossessori();
		root.setKey(keyTree);
		root.setText(descTree);
		root.setRadioVisible(true);
		root.open();
		root.setFlagCondiviso(false);
		myForm.setTreeElementViewPossessori(root);
		myForm.setSelezRadio(""+root.getRepeatableId());




		return mapping.getInputForward();
	}
	public ActionForward dettaglioPossessore(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PossessoriAnaliticaForm myForm = (PossessoriAnaliticaForm) form;
		TreeElementViewPossessori  treeElementView = myForm.getTreeElementViewPossessori();

		if (myForm.getSelezRadio()  == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentoFisico.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		TreeElementViewPossessori elementoTree = (TreeElementViewPossessori) treeElementView.findElementUnique(Integer.valueOf(myForm.getSelezRadio()));

		request.setAttribute("listaPossByAna", myForm.getListaPossessori());
		request.setAttribute("keyTreeByAna", elementoTree.getKey());

		return mapping.findForward("dettaglioPossessore");
}
	public ActionForward creaLegamePossessore(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PossessoriAnaliticaForm myForm = (PossessoriAnaliticaForm) form;
		TreeElementViewPossessori  treeElementView = myForm.getTreeElementViewPossessori();

		if (myForm.getSelezRadio()  != null) {
			request.setAttribute("pidIniziale",  treeElementView.getKey());
			request.setAttribute("descIniziale", treeElementView.getText());
			return mapping.findForward("legamePossessore");

		}
		return mapping.getInputForward();
}
	//


	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		PossessoriAnaliticaForm myForm = (PossessoriAnaliticaForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (idCheck.equalsIgnoreCase("inventari") ){
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

}
