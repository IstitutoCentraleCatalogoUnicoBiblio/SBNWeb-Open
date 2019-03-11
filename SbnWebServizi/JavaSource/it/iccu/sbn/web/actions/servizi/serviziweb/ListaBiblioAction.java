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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.ListaBiblioForm;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.ServiziWebDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;
// almaviva
public final class ListaBiblioAction extends LookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.avanti", "avanti");
		map.put("button.logout", "esci");
		return map;
	}
	//
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		//
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			String polo = Navigation.getInstance(request).getPolo();
			//polo = "CSW";
			//listaBiblio listbib;lista delle biblioteche che
			//ammettono l'autoregistrazione da web.
			ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
			List listaBib = factory.getGestioneServiziWeb().getListaBibAutoregistrazione(polo);

			ListaBiblioForm currentForm = (ListaBiblioForm) form;
			//se ci sono biblioteche
			if (listaBib.size()>0) {
				//caricamento della lista delle biblioteche
				//che ammettono autoregistrazione da web
				currentForm.setBiblio(listaBib);
				BibliotecaVO bib = (BibliotecaVO) listaBib.get(0);
				currentForm.setCod_biblio(bib.getCod_bib());
				currentForm.setNome_biblio(bib.getNom_biblioteca());
				//
				//rinvio la jsp listaBiblio con la lista delle biblioteche
				return (mapping.getInputForward());
			} else { //altrimenti se non trovo biblioteche
				//invio msg:"non sono presenti biblioteche che ammettono
				//autoregistrazione da web pertanto recarsi presso la biblioteca più vicina"
				//
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.biblioteche.autoregistrazione"));
				this.saveErrors(request, errors);
				//
				return (mapping.findForward("login"));
			}

		}catch (Exception e) {

		}

		return (mapping.getInputForward());
	}

//	@SuppressWarnings("unchecked")
	public ActionForward avanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//dopo aver selezionato una biblioteca
		//procedo alla prospettazione della jsp contenente tutti idati anagrafici
		//necessari all'inserimento di un nuovo utente.
		ListaBiblioForm currentForm = (ListaBiblioForm) form;

		BibliotecaVO bibVo = (BibliotecaVO) currentForm.getBiblio().get(currentForm.getRiga());
		request.setAttribute("nomebib",bibVo.getNom_biblioteca());

		request.setAttribute("codbib",bibVo.getCod_bib());

		try {
			//almaviva5_20120920 #5117 controllo configurazione server mail PRIMA
			//dell'eventuale iscrizione
			ServiziWebDelegate delegate = ServiziWebDelegate.getInstance(request);
			delegate.getMailBiblioteca(Navigation.getInstance(request).getUtente().getCodPolo(), bibVo.getCod_bib());

			if (currentForm.getBiblio() != null) {
				//prospetto la jsp contenente i dati anagrafici
				return mapping.findForward("inserimentoUtenteWeb");
			} else {
				//con msg:"Selezionare almeno una biblioteca"
				LinkableTagUtils.addError(request, new ActionMessage("servizi.biblioteche.autoregistrazione"));
				return (mapping.getInputForward());
			}
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}
	//
	public ActionForward esci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			return mapping.findForward("login");
		}catch (Exception e) {

		}
		return (mapping.getInputForward());
	}

}
