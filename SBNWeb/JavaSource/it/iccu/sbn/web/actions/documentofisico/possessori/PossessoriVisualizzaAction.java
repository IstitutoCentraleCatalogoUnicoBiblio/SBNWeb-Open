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


import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriDettaglioForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriVisualizzaAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(PossessoriVisualizzaAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String,String> getKeyMethodMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("dettaglio.button.annulla", "indietro");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		PossessoriDettaglioVO possDettVO =  (PossessoriDettaglioVO)request.getAttribute("dettaglioPossDaAnalitica");
		String tipoForma=(String)request.getAttribute("dettaglioPossDaAnaliticaTipoForma");

		PossessoriDettaglioForm possDett = (PossessoriDettaglioForm)form;
		possDett.setPossDettVO(possDettVO);

		caricaComboGenerAutore(possDett);


		HttpSession httpSession = request.getSession();
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		return mapping.getInputForward();
	}


	public void caricaComboGenerAutore(PossessoriDettaglioForm possDettForm) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	possDettForm.getPossDettVO().setListalivAutority(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita()));
		possDettForm.getPossDettVO().setListaTipoTitolo(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoAutore()));
   }

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		PossessoriDettaglioForm possDett = (PossessoriDettaglioForm)form;
		request.setAttribute("bid", possDett.getPossDettVO().getPid());
		request.setAttribute("livRicerca", "P");

		return Navigation.getInstance(request).goBack(mapping.findForward("indietro"), true);

	}

}





