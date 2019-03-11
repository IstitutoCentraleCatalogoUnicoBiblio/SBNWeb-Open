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

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.codiciProvenienza.CodiciProvenienzaListaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
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

public class CodiciProvenienzaListaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	private static Log log = LogFactory.getLog(CodiciProvenienzaListaAction.class);

//	private CodiciProvenienzaListaForm form;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.bottone.nuova", "nuova");
		map.put("documentofisico.bottone.modifica", "modifica");
		map.put("documentofisico.bottone.scegli", "okP");
		map.put("documentofisico.bottone.chiudiP", "chiudiP");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("button.blocco", "blocco");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		CodiciProvenienzaListaForm currentForm = (CodiciProvenienzaListaForm) form;
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				} catch (Exception e) {
					errors.clear();
					errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm)form;
		this.saveToken(request);
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
//		controllo se ho già i dati in sessione;
		if(!myForm.isSessione())
		{
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			loadDefault(request, mapping, form);
			myForm.setSessione(true);
		}
//		gestione ritorno da Nuova
		if(request.getAttribute("insertOk")!=null) {
			ActionMessages errors = new ActionMessages();
			errors.add("insertOk", new ActionMessage("error.documentofisico.insertOk"));
			this.saveErrors(request, errors);
		}
		if(request.getAttribute("erratoInserimento")!=null) {
			ActionMessages errors = new ActionMessages();
			errors.add("erratoInserimento", new ActionMessage("error.documentofisico.erratoInserimento"));
			this.saveErrors(request, errors);
		}
//		gestione ritorno da Modifica
		if(request.getAttribute("updateOk")!=null) {
			ActionMessages errors = new ActionMessages();
			errors.add("updateOk", new ActionMessage("error.documentofisico.updateOk"));
			this.saveErrors(request, errors);
		}
		if(request.getAttribute("errataModifica")!=null) {
			ActionMessages errors = new ActionMessages();
			errors.add("errataModifica", new ActionMessage("error.documentofisico.errataModifica"));
			this.saveErrors(request, errors);
		}
//		gestione richiamo lista provenienze da lente
		if(request.getAttribute("chiamante")!=null) {
			myForm.setRichiamo("lista");
			myForm.setAction((String)request.getAttribute("chiamante"));
			myForm.getRecProvInv().setCodBib(myForm.getCodBib());
		}

		if (request.getAttribute("codBibDaLista") != null) {
			BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
			request.setAttribute("codBib", bib.getCod_bib());
			request.setAttribute("descrBib", bib.getNom_biblioteca());
		}
		if(request.getAttribute("codBib") != null && request.getAttribute("descrBib")!= null) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			myForm.setCodBib((String)request.getAttribute("codBib"));
			myForm.setDescrBib((String)request.getAttribute("descrBib"));
		}
		try{
			// evolutiva Mail Carla del 16.06.2017 - nuova gestione del cartiglio relativo alla Provenienza:
			// se nel campo Provenienza si inserisce una parola/stringa la pressione del cartiglio attiva una ricerca filtrata
			// per stringa inserita altrimenti se il campo rimane vuoto la ricerca rimane uguale a quella attuale.
			String filtroProvenienza = "";
			if(request.getAttribute("filtroProvenienza") != null && request.getAttribute("filtroProvenienza")!= null) {
				filtroProvenienza = (String)request.getAttribute("filtroProvenienza");
			}

			List listaProven = this.getListaProvenienze(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket(), form, myForm.getNRec(), filtroProvenienza);
			if (listaProven == null ||  listaProven.size() <= 0)  {
				myForm.setNoProvInv(true);
				if (myForm.isNoProvInv() && myForm.getRecProvInv().getCodProvenienza()!=null){
					return Navigation.getInstance(request).goBack(true);
				}
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_CODICI_DI_PROVENIENZA, myForm.getCodPolo(), myForm.getCodBib(), null);
				String bottOk = "Ok";
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("Nuova","nuova");
				request.setAttribute("bottOk",bottOk);
				if (!this.checkAttivita(request, myForm, "df")){
					return mapping.getInputForward();
				}else{
					return mapping.findForward("nuova");
				}
			}else{
				if (listaProven.size() == 1){
					myForm.setSelectedCod("0");
				}

				myForm.getRecProvInv().setCodBib(myForm.getCodBib());
				myForm.setDescrBib(myForm.getDescrBib());
				myForm.setListaProvInv(listaProven);
			}
		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (UtenteNotAuthorizedException e) {
			//??
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward nuova(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!myForm.isSessione())
				{
					myForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
//			resetToken(request);
			String bottOk = "Ok";
			request.setAttribute("codBib", myForm.getRecProvInv().getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("Nuova","nuova");
			request.setAttribute("bottOk",bottOk);
			return mapping.findForward("nuova");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!myForm.isSessione())
				{
					myForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
//			this.checkForm(request);
			resetToken(request);

			String bottOk = "Ok";
			request.setAttribute("check", myForm.getSelectedCod());
			String check = myForm.getSelectedCod();
			int provsel;
			if (check != null && check.length() != 0) {
				provsel = Integer.parseInt(myForm.getSelectedCod());
				ProvenienzaInventarioVO scelProv = (ProvenienzaInventarioVO) myForm.getListaProvInv().get(provsel);
				scelProv.setCodBib(myForm.getRecProvInv().getCodBib());
				request.setAttribute("scelProv", scelProv);
				request.setAttribute("bottOk",bottOk);
				request.setAttribute("Modifica","modifica");
				return mapping.findForward("modifica");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward okP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!myForm.isSessione())
				{
					myForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
//			this.checkForm(request);
			resetToken(request);

			int provSel;
			request.setAttribute("checkS", myForm.getSelectedCod());
			String checkS = myForm.getSelectedCod();
			if (checkS != null && checkS.length() != 0) {
				provSel = Integer.parseInt(myForm.getSelectedCod());
				ProvenienzaInventarioVO scelProv = (ProvenienzaInventarioVO) myForm.getListaProvInv().get(provSel);
				request.setAttribute("codPoloProven", myForm.getCodPolo());
				request.setAttribute("codBibProven", myForm.getCodBib());
				request.setAttribute("codProven", scelProv.getCodProvenienza().trim());
				request.setAttribute("descrProven", scelProv.getDescrProvenienza().trim());
				request.setAttribute("prov", "listaProvenienze");

				return Navigation.getInstance(request).goBack();
//				ActionForward action = new ActionForward();
//				action.setName("back");
//				action.setPath(myForm.getAction()+".do");
//				return action;
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudiP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!myForm.isSessione())
				{
					myForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
//			this.checkForm(request);
			resetToken(request);

			return Navigation.getInstance(request).goBack(true);
//			return mapping.findForward("chiudi");
//			ActionForward action = new ActionForward();
//			action.setName("back");
//			action.setPath(myForm.getAction()+".do");
//			return action;

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try {
			if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")) {
				return Navigation.getInstance(request).goBack(true);
			}
			return mapping.findForward("chiudi");
//			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {

			return mapping.getInputForward();
		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!myForm.isSessione()) {
			myForm.setSessione(true);
		}
		int numBlocco = myForm.getBloccoSelezionato();
		String idLista = myForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				myForm.getListaProvInv().addAll(bloccoVO.getLista());
				Collections.sort(myForm.getListaProvInv());
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(), CodiciAttivita.getIstance().GDF_CODICI_DI_PROVENIENZA,
					myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List getListaProvenienze(String codPolo, String codBib, String ticket, ActionForm form, int nRec, String filtroProvenienza) throws Exception {
		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaProvenienze(codPolo, codBib, ticket, nRec, filtroProvenienza);
		if (blocco1 == null || blocco1.getTotRighe() < 1) {
			myForm.setNoProvInv(true);
			return null;
		} else {
			myForm.setNoProvInv(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)) {
				myForm.setAbilitaBottoneCarBlocchi(false);
			} else {
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		CodiciProvenienzaListaForm myForm = (CodiciProvenienzaListaForm) form;
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_CODICI_DI_PROVENIENZA, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
