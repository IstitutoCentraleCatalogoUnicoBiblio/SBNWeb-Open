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
package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBiblioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.ListaBibliotecheAffiliateAcqForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

public class ListaBibliotecheAffiliateAcqAction extends SinteticaLookupDispatchAction  {

	//private ListaBibliotecheAffiliateForm listbibl;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
/*		map.put("ricerca.button.salva","salva");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");*/

		map.put("ricerca.button.scegli","scegli");
		map.put("button.blocco", "caricaBlocco");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			ListaBibliotecheAffiliateAcqForm listbibl = (ListaBibliotecheAffiliateAcqForm) form;
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			if(!listbibl.isSessione())
			{
				loadDefault(request, mapping, form);
				listbibl.setSessione(true);
			}
			if (request.getAttribute("biblInOgg") != null)
			{
				listbibl.setBiblioteca((String)request.getAttribute("biblInOgg"));
			}

			//this.loadBiblAff( listbibl);
			//String [] pippo={"01","03","CO"};
			//listbibl.setSelectedBiblAff (pippo);

			List<ComboVO> listaBiblAff=new ArrayList<ComboVO> ();

			this.loadBiblAff(listbibl);

			if (listbibl.getListaBiblAff()!=null && listbibl.getListaBiblAff().size()>0)
			{
				//listbibl.setListaBiblAff(elencoBib);
				listbibl.setNumBibl(listbibl.getListaBiblAff().size());
				// gestione blocchi
				DescrittoreBloccoVO blocco1= null;
				UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
				String ticket=utenteCollegato.getTicket();

				int maxElementiBlocco=listbibl.getNRec();
				int maxRighe=listbibl.getNRec();

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listbibl.getListaBiblAff(),maxElementiBlocco);
				listbibl.setListaBiblAff(blocco1.getLista());

				if (blocco1 != null)
				{
					listbibl.setIdLista(blocco1.getIdLista()); //si
					listbibl.setTotRighe(blocco1.getTotRighe()); //no
					listbibl.setTotBlocchi(blocco1.getTotBlocchi()); //no
					listbibl.setMaxRighe(blocco1.getMaxRighe()); //no
					listbibl.setBloccoSelezionato(blocco1.getNumBlocco()); //si
					//listbibl.setUltimoBlocco(blocco1);
				}
				// fine gestione blocchi
			}




			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaBibliotecheAffiliateAcqForm listbibl = (ListaBibliotecheAffiliateAcqForm) form;
		try {

			ListaSuppBiblioVO ricArr= (ListaSuppBiblioVO)request.getSession().getAttribute("attributeListaSuppBiblioVO") ;
			if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().trim().length()>0)
			{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante().trim()+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
			}
			else
			{
				if (request.getSession().getAttribute("chiamante") != null)
				{
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(request.getSession().getAttribute("chiamante")+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
				}
				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaBibliotecheAffiliateForm listbibl = (ListaBibliotecheAffiliateForm ) form;
		try {
    		ActionMessages errors = new ActionMessages();
    		listbibl.setConferma(true);
    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		ListaBibliotecheAffiliateForm listbibl = (ListaBibliotecheAffiliateForm ) form;
		try {
			listbibl.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaBibliotecheAffiliateForm listbibl = (ListaBibliotecheAffiliateForm ) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		listbibl.setConferma(false);
		return mapping.getInputForward();
	}
	*/
    private void loadBiblAff(ListaBibliotecheAffiliateAcqForm listbibl) throws Exception {
/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("01","Biblioteca principale dell'ICCU");
		lista.add(elem);
		elem = new StrutturaCombo("02","Storia moderna e contemporanea");
		lista.add(elem);
		elem = new StrutturaCombo("03","Biblioteca Nazionale di Napoli");
		lista.add(elem);
		elem = new StrutturaCombo("CO","Oriani");
		lista.add(elem);
		elem = new StrutturaCombo("CS","Biblioteca di Cosenza");
		lista.add(elem);*/

		List<StrutturaCombo> listaBiblAff = new ArrayList<StrutturaCombo>();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaBiblAff = factory.getGestioneAcquisizioni().getRicercaBiblAffiliate(listbibl.getBiblioteca(), CodiciAttivita.getIstance().GA_GESTIONE_ORDINI);
		listbibl.setListaBiblAff(listaBiblAff);
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaBibliotecheAffiliateAcqForm listbibl = (ListaBibliotecheAffiliateAcqForm ) form;
		try {

/*			ListaSuppOrdiniVO ricSez=listaOrd.getSifOrdini();


			if (listaOrd!=null && listaOrd.getRadioSel()!=null &&  listaOrd.getRadioSel().trim().length() >0)
			{
				String [] appoSez=new String [1];
				appoSez[0]=listaOrd.getRadioSel().trim();
				listaOrd.setSelectedOrdini(appoSez);
				List<OrdiniVO> arrSezioStrut=new ArrayList<OrdiniVO>();
				for (int i=0;  i < listaOrd.getSelectedOrdini().length; i++)
				{
					String codProfil=listaOrd.getSelectedOrdini()[i].trim();
					for (int j=0;  j < listaOrd.getListaOrdini().size(); j++)
					{

						OrdiniVO profilStrut=listaOrd.getListaOrdini().get(j);
						if (profilStrut.getChiave().trim().equals(codProfil))
						{
							arrSezioStrut.add(profilStrut);
						}
					}
				}
				// aggiornamento della variabile di sessione
				ricSez.setSelezioniChiamato(arrSezioStrut);
			}


			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().trim().length()>0)
			{
				 	request.setAttribute("passaggioListaSuppOrdiniVO", (ListaSuppOrdiniVO) ricSez);
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricSez.getChiamante().trim()+".do");
					return action;
			}
			else
			{
				return mapping.getInputForward();
			}
*/

			ListaSuppBiblioVO ricArr= (ListaSuppBiblioVO)request.getSession().getAttribute("attributeListaSuppBiblioVO") ;
			// aggiornamento della variabile di sessione
			String[] selectedBiblAff=listbibl.getSelectedBiblAff();

			List<ComboVO> listaBibl=new ArrayList<ComboVO>();
			for (int i=0;  i < listbibl.getSelectedBiblAff().length; i++)
			{
					ComboVO ele=new ComboVO();
					ele.setCodice(selectedBiblAff[i]);
					ele.setDescrizione("");
					listaBibl.add(ele);
			}



			ricArr.setSelezioniChiamato(listaBibl);


			if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().trim().length()>0)
			{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante().trim()+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
			}
			else
			{
				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaBibliotecheAffiliateAcqForm listbibl = (ListaBibliotecheAffiliateAcqForm ) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listbibl.isSessione()) {
			listbibl.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = listbibl.getBloccoSelezionato();
		String idLista = listbibl.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				listbibl.getListaBiblAff().addAll(bloccoSucc.getLista());
			}
		}
		return mapping.getInputForward();
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		ListaBibliotecheAffiliateAcqForm listbibl = (ListaBibliotecheAffiliateAcqForm ) form;
		if (!listbibl.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				listbibl.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.acquisizioni.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}

}
