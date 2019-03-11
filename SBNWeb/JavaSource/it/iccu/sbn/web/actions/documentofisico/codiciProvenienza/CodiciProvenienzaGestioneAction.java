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
package it.iccu.sbn.web.actions.documentofisico.codiciProvenienza;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.web.actionforms.documentofisico.codiciProvenienza.CodiciProvenienzaGestioneForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class CodiciProvenienzaGestioneAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(CodiciProvenienzaGestioneAction.class);

//	private CodiciProvenienzaGestioneForm form = new CodiciProvenienzaGestioneForm();

	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();

		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);

		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		// controllo se ho già i dati in sessione;
		if(!myForm.isSessione()){
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			myForm.setSessione(true);
		}
		if (request.getAttribute("Nuova") != null) {
			myForm.setDisableBib(false);
			myForm.setDisableDescr(false);
			myForm.setTastoOk("nuova");
			//carico pagina vuota
			this.loadPaginaVuota(request, form);
		} else {
			//carico pagina con dati selezionati
		} if (request.getAttribute("Modifica") != null) {
			if(request.getAttribute("scelProv") != null) {
				myForm.setDisableBib(true);
				myForm.setDisableDescr(false);
				myForm.setTastoOk("modifica");
				this.loadRigaSel(request, form);
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.campoNonImp"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
		}
		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if (!myForm.isSessione()) {
					myForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//insert provenienze
			if (myForm.getTastoOk().equals("nuova")){
				try {
					if (!this.insertProvenienze(myForm.getRecProvInv(), myForm.getTicket())){
						request.setAttribute("erratoInserimento","erratoInserimento");
					}else{
						request.setAttribute("insertOk","insertOk");
						request.setAttribute("codBib",myForm.getRecProvInv().getCodBib());
					}
					return mapping.findForward("ok");
				}	catch (ValidationException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}	catch (Exception e) { // altri tipi di errore
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}else if (myForm.getTastoOk().equals("modifica")){
				//richiesta conferma update provenienze
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaModifica"));
				this.saveErrors(request, errors);
				this.saveToken(request);

				myForm.setConferma(true);
				return mapping.getInputForward();
			}else{
				return mapping.findForward("ok");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		try {
//			if (myForm.getTastoOk().equals("nuova")) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaModifica"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setDisableBib(true);
				myForm.setDisableDescr(true);
				myForm.setConferma(true);
				return mapping.getInputForward();
//			} else {
//				return mapping.findForward("chiudi");
//			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadPaginaVuota(HttpServletRequest request, ActionForm form) throws Exception {
		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		myForm.setCodBib((String)(request.getAttribute("codBib")));
		myForm.getRecProvInv().setCodBib(myForm.getCodBib());
		myForm.setDescrBib((String)(request.getAttribute("descrBib")));

		ProvenienzaInventarioVO provenienze = new ProvenienzaInventarioVO();
		provenienze.setCodPolo(myForm.getCodPolo());
		provenienze.setCodBib(myForm.getCodBib());
		provenienze.setCodProvenienza(null);
		provenienze.setDescrProvenienza(null);
		provenienze.setUteIns(Navigation.getInstance(request).getUtente().getFirmaUtente());
		myForm.setRecProvInv(provenienze);
	}

	private void loadRigaSel(HttpServletRequest request, ActionForm form) throws Exception {
		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		ProvenienzaInventarioVO provenienze = (ProvenienzaInventarioVO)request.getAttribute("scelProv");
		myForm.getRecProvInv().setCodPolo(myForm.getCodPolo());
		myForm.getRecProvInv().setCodBib(provenienze.getCodBib());
		myForm.getRecProvInv().setCodProvenienza(provenienze.getCodProvenienza());
		myForm.getRecProvInv().setDescrProvenienza(provenienze.getDescrProvenienza());
		myForm.getRecProvInv().setDataIns(provenienze.getDataIns().substring(0,10));
		myForm.getRecProvInv().setDataAgg(provenienze.getDataAgg().substring(0,10));
		myForm.getRecProvInv().setUteAgg(provenienze.getUteAgg());
		myForm.setDate(true);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if (!myForm.isSessione()) {
					myForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			if (myForm.getTastoOk().equals("modifica")){
				try{
					if (!this.updateProvenienze(myForm.getRecProvInv(), myForm.getTicket())){
						request.setAttribute("errataModifica","errataModifica");
					}else{
						request.setAttribute("updateOk","updateOk");
					}
					return mapping.findForward("ok");
				}
				catch (ValidationException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}	catch (Exception e) { // altri tipi di errore
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			} else if (myForm.isConferma()) {
				// insert provenienze
				try {
					if (!this.insertProvenienze(myForm.getRecProvInv(), myForm.getTicket())){
						request.setAttribute("erratoInserimento","erratoInserimento");
					}else{
						request.setAttribute("insertOk","insertOk");
						request.setAttribute("codBib",myForm.getRecProvInv().getCodBib());
					}
					return mapping.findForward("ok");
				}	catch (ValidationException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
					this.saveErrors(request, errors);
					myForm.setConferma(false);
					return mapping.getInputForward();
				}	catch (Exception e) { // altri tipi di errore
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
					this.saveErrors(request, errors);
					myForm.setConferma(false);
					return mapping.getInputForward();
				}
			} else{
				myForm.setConferma(false);
				return mapping.findForward("ok");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		CodiciProvenienzaGestioneForm myForm = (CodiciProvenienzaGestioneForm) form;
		// Viene settato il token per le transazioni successive
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private boolean insertProvenienze(ProvenienzaInventarioVO provenienze, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertProvenienza(provenienze, ticket);
		return ret;
	}

	private boolean updateProvenienze(ProvenienzaInventarioVO provenienze, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateProvenienza(provenienze, ticket);
		return ret;
	}

}
