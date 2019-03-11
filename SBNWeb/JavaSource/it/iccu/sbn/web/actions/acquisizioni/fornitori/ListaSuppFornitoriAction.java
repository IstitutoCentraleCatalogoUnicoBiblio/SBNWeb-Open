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
package it.iccu.sbn.web.actions.acquisizioni.fornitori;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.web.actionforms.acquisizioni.fornitori.ListaSuppFornitoriForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

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

public class ListaSuppFornitoriAction extends LookupDispatchAction {
	//private ListaSuppFornitoriForm listSuppFornitori;

/*	private void loadFornitori() throws Exception {
		List lista = new ArrayList();

		FornitoreVO forn=new FornitoreVO("X10", "01", "88", "UPIE", "", "Via Cavour, 110","","","","","","","","","","","F","");
		lista.add(forn);
		forn=new FornitoreVO("X10", "01", "13", "Libreria Feltrinelli", "", "Via del Babuino, 25","","","","","","","","","","","F","");
		lista.add(forn);
		forn=new FornitoreVO("X10", "01", "26", "L.S. S.r.L.", "", "Via Badini, 17","","","","","","","","","","","F","");
		lista.add(forn);

		this.sintFornitori.setListaFornitori(lista);
	}*/

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaSuppFornitoriForm listSuppFornitori = (ListaSuppFornitoriForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			if(!listSuppFornitori.isSessione())
			{
				listSuppFornitori.setSessione(true);
			}
			//			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr==null)
			{
			// Modifica del 07.05.2013 BUG Mantis 5305 Collaudo
			// Dopo la ricerca di un fornitore solo in biblioteca che non trovo creo;
			// con il Salva il sistema non innesca il controllo di duplicazione, ma crea il duplicato
			// Trovato problema di impostazione del parametro request: risolto
//				ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				ricArr=(ListaSuppFornitoreVO) request.getAttribute("attributeListaSuppFornitoreVO");
			}

			List<FornitoreVO> listaFornitori;
			listaFornitori=this.getListaFornitoriVO(ricArr );
			//this.loadFornitori();
			listSuppFornitori.setListaFornitori(listaFornitori);
			listSuppFornitori.setNumFornitori(listSuppFornitori.getListaFornitori().size());
			// non trovati
			if (listSuppFornitori.getNumFornitori()==0)
			{
				return mapping.getInputForward();

				//return mapping.findForward("indietro");

			}
			else {
				return mapping.getInputForward();
			}
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
			// assenzaRisultati = 4001;
			if (ve.getError()==4001){
				listSuppFornitori.setRisultatiPresenti(false);
				return Navigation.getInstance(request).goBack(true);
			}
			return mapping.getInputForward();
		}catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaSuppFornitoriForm listSuppFornitori = (ListaSuppFornitoriForm) form;
		try {
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr==null)
			{
				ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");

			}
			if (ricArr!=null )
			{
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.findForward("indietro");
				}

			}
			else
			{
				return mapping.findForward("indietro");
			}


			//return mapping.findForward("indietro");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaSuppFornitoriForm listSuppFornitori = (ListaSuppFornitoriForm) form;
		try {
			request.setAttribute("fornScelto", listSuppFornitori.getSelectedFornitori());
			//return mapping.findForward("indietro");
			// selezione della denominazione
			if (listSuppFornitori.getListaFornitori().size()>0)
			{
				for (int z=0; z <listSuppFornitori.getListaFornitori().size(); z++)
				{
					if (listSuppFornitori.getListaFornitori().get(z).getCodFornitore().equals(listSuppFornitori.getSelectedFornitori()))
					{
						request.setAttribute("fornSceltoDeno", listSuppFornitori.getListaFornitori().get(z).getNomeFornitore());
					}
				}
			}

			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr==null)
			{
				ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			}
			if (ricArr!=null )
			{
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.findForward("indietro");
				}

			}
			else
			{
				return mapping.findForward("indietro");
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List<FornitoreVO> getListaFornitoriVO(ListaSuppFornitoreVO criRicerca) throws Exception
	{
	List<FornitoreVO> listaFornitori;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaFornitori = factory.getGestioneAcquisizioni().getRicercaListaFornitori(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	return listaFornitori;
	}


}
