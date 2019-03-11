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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioClasseGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioSoggettoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioTermineThesauroGeneraleVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.DettaglioLegameTitoloSemanticiForm;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class DettaglioLegameTitoloSemanticiAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(DettaglioLegameTitoloSemanticiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.annulla", "annulla");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();


		DettaglioLegameTitoloSemanticiForm dettTitSemForm = (DettaglioLegameTitoloSemanticiForm) form;
		log.debug("DettaglioLegameTitoloSemanticiAction::unspecified");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		dettTitSemForm.setDettSogGenVO((DettaglioSoggettoGeneraleVO)request.getAttribute("dettaglioSog"));
		dettTitSemForm.setDettClaGenVO((DettaglioClasseGeneraleVO)request.getAttribute("dettaglioCla"));
		dettTitSemForm.setDettTerThesGenVO((DettaglioTermineThesauroGeneraleVO)request.getAttribute("dettaglioTerThes"));

		if (dettTitSemForm.getDettClaGenVO() != null)
			dettTitSemForm.setTipoLegame(dettTitSemForm.getDettClaGenVO().getTipoLegame().trim());
		else if (dettTitSemForm.getDettSogGenVO() != null)
			dettTitSemForm.setTipoLegame(dettTitSemForm.getDettSogGenVO().getTipoLegame().trim());
		else if (dettTitSemForm.getDettTerThesGenVO() != null)
			dettTitSemForm.setTipoLegame(dettTitSemForm.getDettTerThesGenVO().getTipoLegame().trim());


		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		return Navigation.getInstance(request).goBack( mapping.findForward("annulla"), true);
	}

}
