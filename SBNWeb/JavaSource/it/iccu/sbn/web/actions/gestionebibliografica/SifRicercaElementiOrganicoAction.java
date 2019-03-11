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

package it.iccu.sbn.web.actions.gestionebibliografica;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.SifRicercaElementiOrganicoForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
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

public class SifRicercaElementiOrganicoAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(SifRicercaRepertoriAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sifElOrganico.button.cerca", "cerca");
		map.put("sifElOrganico.button.ricarica", "ricarica");
		map.put("sifElOrganico.button.selez", "seleziona");
		map.put("sifElOrganico.button.annul", "annulla");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");
		SifRicercaElementiOrganicoForm sifRicElOrgForm = (SifRicercaElementiOrganicoForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		sifRicElOrgForm.setTipoOrganico((String) request.getAttribute("tipoOrganico"));
		this.caricaListe(sifRicElOrgForm);
		return mapping.getInputForward();
	}

	public ActionForward seleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SifRicercaElementiOrganicoForm sifRicElOrgForm = (SifRicercaElementiOrganicoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sifRicElOrgForm.getSelezRadio() != null) {
			request.setAttribute("codOrg", sifRicElOrgForm.getSelezRadio());
			request.setAttribute("tipoOrganico", sifRicElOrgForm.getTipoOrganico());
			resetToken(request);
			//return	Navigation.getInstance(request).goBack();
			return Navigation.getInstance(request).goBack(true);
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}


	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SifRicercaElementiOrganicoForm sifRicElOrgForm = (SifRicercaElementiOrganicoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sifRicElOrgForm.getTestoRicerca() == null || sifRicElOrgForm.getTestoRicerca().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		List<ComboCodDescVO> listaElOrganicoNew = new ArrayList<ComboCodDescVO>();
		ComboCodDescVO codDesc;
		List<ComboCodDescVO> listaElementiOrg = sifRicElOrgForm.getListaElementiOrg();
		for (int i = 0; i < listaElementiOrg.size(); i++) {
			codDesc = new ComboCodDescVO();
			codDesc = listaElementiOrg.get(i);
			if (codDesc.getDescrizione().toUpperCase().contains(sifRicElOrgForm.getTestoRicerca().toUpperCase())) {
				listaElOrganicoNew.add(listaElementiOrg.get(i));
			}
		}

		sifRicElOrgForm.setListaElementiOrg(listaElOrganicoNew);
		return mapping.getInputForward();
	}

	public ActionForward ricarica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SifRicercaElementiOrganicoForm sifRicElOrgForm = (SifRicercaElementiOrganicoForm) form;
		this.caricaListe(sifRicElOrgForm);
		return mapping.getInputForward();
	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return	Navigation.getInstance(request).goBack(true);
	}

	private void caricaListe(SifRicercaElementiOrganicoForm sifRicercaElementiOrganicoForm)
		throws RemoteException, CreateException, NamingException {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


//		almaviva2 28.01.2010 - BUG 3004 - il controllo dell'organico va fatto a partire dalla tabella ORGA, sia per sintetico che analitico
		if (sifRicercaElementiOrganicoForm.getTipoOrganico().equals("SINTETICO")
				|| sifRicercaElementiOrganicoForm.getTipoOrganico().equals("ANALITICO")) {
			sifRicercaElementiOrganicoForm.setListaElementiOrg(carCombo.loadElencoOrganico(factory.getCodici().getCodiceStrumentiVociOrganico()));
		} else {
			sifRicercaElementiOrganicoForm.setListaElementiOrg(carCombo.loadElencoOrganico(factory.getCodici().getCodici(CodiciType.CODICE_STRUMENTI_VOCI_ORGANICO_PLURALI)));
		}
//		if (sifRicercaElementiOrganicoForm.getTipoOrganico().equals("SINTETICO")
//				|| sifRicercaElementiOrganicoForm.getTipoOrganico().equals("SINTETICO-COMP")) {
//			sifRicercaElementiOrganicoForm.setListaElementiOrg(carCombo.loadElencoOrganico(factory.getCodici().getCodici(CodiciType.CODICE_STRUMENTI_VOCI_ORGANICO_PLURALI)));
//		} else {
//			sifRicercaElementiOrganicoForm.setListaElementiOrg(carCombo.loadElencoOrganico(factory.getCodici().getCodiceStrumentiVociOrganico()));
//		}

		return;
	}



}
