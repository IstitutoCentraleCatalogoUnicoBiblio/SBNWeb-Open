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
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.web.actionforms.acquisizioni.fornitori.ProfiliAcqForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
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

public class ProfiliAcqAction extends LookupDispatchAction {
	//private ProfiliAcqForm profiliFornitori;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			ProfiliAcqForm profiliFornitori = (ProfiliAcqForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			if(!profiliFornitori.isSessione())
			{
				profiliFornitori.setSessione(true);
			}

			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			List<StrutturaProfiloVO> listaProfili;
			//if (ricArr.getSelezioniChiamato()!=null && ricArr.getSelezioniChiamato().size()>0)
			//{
			//	listaProfili=ricArr.getSelezioniChiamato();
			//}
			//else
			//{
				listaProfili=this.getListaStrutturaProfiloVO(ricArr);
			//}

			//this.loadFornitori();
			profiliFornitori.setListaProfili(listaProfili);
			profiliFornitori.setNumProfili(listaProfili.size());

			FornitoreVO fornOggetto= (FornitoreVO) request.getAttribute("fornOggetto");
			List<StrutturaProfiloVO> listaProfOggetto=new ArrayList<StrutturaProfiloVO>();
			if (fornOggetto.getFornitoreBibl()!=null && fornOggetto.getFornitoreBibl().getProfiliAcq()!=null)
			{
				listaProfOggetto=fornOggetto.getFornitoreBibl().getProfiliAcq();
			}

			if (listaProfOggetto!=null && listaProfOggetto.size()>0)
			{
				String [] appoSelProf=new String [listaProfOggetto.size()];
				for (int i=0;  i < listaProfOggetto.size(); i++)
				{
					String codProfil=listaProfOggetto.get(i).getProfilo().getCodice();
					appoSelProf[i]=codProfil;
				}
				profiliFornitori.setSelectedProfili(appoSelProf);
			}
			return mapping.getInputForward();

		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
					// assenzaRisultati = 4001;

					if (ve.getError()==4001)
					{
						profiliFornitori.setRisultatiPresenti(false);
					}

					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ProfiliAcqForm profiliFornitori = (ProfiliAcqForm) form;
		try {
				//return mapping.findForward("indietro");
			if (request.getSession().getAttribute("chiamante")!=null )
			{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(request.getSession().getAttribute("chiamante")+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
			}
			else
			{
				return mapping.findForward("indietro");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ProfiliAcqForm profiliFornitori = (ProfiliAcqForm) form;
		try {
			request.setAttribute("profScelto", profiliFornitori.getSelectedProfili());

			ListaSuppProfiloVO ricProf=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			List<StrutturaProfiloVO> arrProfilStrut=new ArrayList<StrutturaProfiloVO>();
			if (profiliFornitori.getSelectedProfili().length >0)
			{
				// selezioni effettuate
				// ciclo sui selezionati
				for (int i=0;  i < profiliFornitori.getSelectedProfili().length; i++)
				{
					String codProfil=profiliFornitori.getSelectedProfili()[i].trim();
					// ciclo su tutti i profili
					for (int j=0;  j < profiliFornitori.getListaProfili().size(); j++)
					{

						StrutturaProfiloVO profilStrut=profiliFornitori.getListaProfili().get(j);
						if (profilStrut.getProfilo().getCodice().trim().equals(codProfil))
						{
							arrProfilStrut.add(profilStrut);
						}
					}
				}
				// aggiornamento della variabile di sessione
			}
			ricProf.setSelezioniChiamato(arrProfilStrut);
			request.getSession().setAttribute("attributeListaSuppProfiloVO", ricProf);
			//return mapping.findForward("indietro");

			if (request.getSession().getAttribute("chiamante")!=null )
			{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(request.getSession().getAttribute("chiamante")+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
			}
			else
			{
				return mapping.findForward("indietro");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	private List<StrutturaProfiloVO> getListaStrutturaProfiloVO(ListaSuppProfiloVO criRicerca) throws Exception
	{
	List<StrutturaProfiloVO> listaProfili;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaProfili = factory.getGestioneAcquisizioni().getRicercaListaProfili(criRicerca);
	return listaProfili;
	}


}
