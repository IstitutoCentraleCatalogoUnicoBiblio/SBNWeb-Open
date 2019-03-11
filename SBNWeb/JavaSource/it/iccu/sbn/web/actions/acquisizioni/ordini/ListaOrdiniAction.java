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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.ListaOrdiniForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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


public class ListaOrdiniAction extends SinteticaLookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegliOrd"); // 11.11.2008
		map.put("ricerca.button.scegliInv","scegli"); // 11.11.2008
		map.put("button.blocco", "caricaBlocco");
		map.put("ricerca.button.listaInventariOrdine","listaInventariOrdine");
		map.put("button.gestLocal.filtra", "filtra");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			ListaOrdiniForm currentForm = (ListaOrdiniForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
		        return mapping.getInputForward();

			if(!currentForm.isSessione())
			{
				loadDefault(request, mapping, form);
				currentForm.setSessione(true);
			}

			ListaSuppOrdiniVO ricerca=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			if (ricerca!=null &&  (ricerca.getChiamante().equals("/gestionebibliografica/titolo/sinteticaTitoli") || ricerca.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo")))
			{
				currentForm.setProvInterroga(true);
			}
			if (ricerca!=null &&  (ricerca.getChiamante().equals("/acquisizioni/buoniordine/inserisciBuonoOrdine")))
			{
				currentForm.setProvBo(true);
			}

			List<OrdiniVO> listaOrdini=null;
			if (ricerca == null) {
				// ricerca ampia ma va in eccezione per not
				// esistenzacriteriminimi
				ricerca = new ListaSuppOrdiniVO();
				ricerca.setCodPolo(navi.getUtente().getCodPolo());
				ricerca.setCodBibl(navi.getUtente().getCodBib());
				ricerca.setTicket(navi.getUserTicket());
				// impostazione dei criteri di default di configurazione
				ricerca.setElemXBlocchi(currentForm.getElemXBlocchi());
				ricerca.setStatoOrdineArr(currentForm.getStatoArr());
				ricerca.setOrdinamento(currentForm.getTipoOrdinamSelez());

				if (navi.getBackAction() != null) {
					ricerca.setChiamante(navi.getBackAction());
				} else {
					ricerca.setChiamante("");
				}

			} else {
				// impostazione dei criteri di default di configurazione
				if (ricerca.getElemXBlocchi() == 0)
					ricerca.setElemXBlocchi(currentForm.getElemXBlocchi());
				if (!ValidazioneDati.isFilled(ricerca.getStatoOrdineArr()))
					ricerca.setStatoOrdineArr(currentForm.getStatoArr());
				if (!ValidazioneDati.isFilled(ricerca.getOrdinamento()))
					ricerca.setOrdinamento(currentForm.getTipoOrdinamSelez());
			}
			currentForm.setSifOrdini(ricerca);

			try {
				listaOrdini = this.getListaOrdiniVO(currentForm.getSifOrdini());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (listaOrdini!=null && listaOrdini.size()>0)
			{
				currentForm.setListaOrdini(listaOrdini);
				currentForm.setNumOrdini(listaOrdini.size());
				// gestione blocchi
				DescrittoreBloccoVO blocco1= null;
				UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
				String ticket=utenteCollegato.getTicket();




				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaOrdini,currentForm.getElemXBlocchi());
				currentForm.setListaOrdini(blocco1.getLista());

				if (blocco1 != null)
				{
				currentForm.setIdLista(blocco1.getIdLista()); //si
				currentForm.setTotRighe(blocco1.getTotRighe()); //no
				currentForm.setTotBlocchi(blocco1.getTotBlocchi()); //no
				currentForm.setMaxRighe(blocco1.getMaxRighe()); //no
				currentForm.setBloccoSelezionato(blocco1.getNumBlocco()); //si
				currentForm.setUltimoBlocco(blocco1);
				}
				// fine gestione blocchi
			}
			else
			{
				currentForm.setListaOrdini(new ArrayList<OrdiniVO>());
				currentForm.setNumOrdini(0);
				currentForm.setRisultatiPresenti(false);

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));

			}

			// gestione automatismo check su unico elemento lista con radio
			if (currentForm.getListaOrdini()!=null &&  currentForm.getListaOrdini().size()==1)
			{
				currentForm.setRadioSel(currentForm.getListaOrdini().get(0).getChiave()); //.trim()
			}

			currentForm.setListaSinteticaCompleta(listaOrdini);
			this.caricaBibl(currentForm);
			if (ricerca!=null) // per escludere il primo accesso
			{
				this.impostaBib(currentForm,request);
			}
			return mapping.getInputForward();

			}	catch (ValidationException ve) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));


					if (ve.getError()==4001)
					{
						currentForm.setRisultatiPresenti(false);
					}

					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

				return mapping.getInputForward();
			}
	}


	public ActionForward filtra(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaOrdiniForm currentForm = (ListaOrdiniForm) form;

		int ordini = ValidazioneDati.size(currentForm.getListaSinteticaCompleta());
		if (ordini < 1) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));
			return mapping.getInputForward();
		}

		currentForm.getListaSintetica().clear();

		if (currentForm.getCodBiblioSelez() == null || currentForm.getCodBiblioSelez().equals("")) {
			currentForm.getListaSintetica().addAll(currentForm.getListaSinteticaCompleta());
		}
		else
		{
			for (int i = 0; i < ordini; i++) {
				OrdiniVO ordine = currentForm.getListaSinteticaCompleta().get(i);
				if (ordine.getCodBibl().equals(currentForm.getCodBiblioSelez()) ) {
					currentForm.getListaSintetica().add(ordine);
				}
			}
		}
		currentForm.setListaOrdini(currentForm.getListaSintetica());

		if (currentForm.getListaOrdini()!=null && currentForm.getListaOrdini().size()>0)
		{
			currentForm.setListaOrdini(currentForm.getListaOrdini());
			currentForm.setNumOrdini(currentForm.getListaOrdini().size());
			// gestione blocchi
			DescrittoreBloccoVO blocco1= null;
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String ticket=utenteCollegato.getTicket();



			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,
					new ArrayList<OrdiniVO>(currentForm.getListaOrdini()), currentForm.getElemXBlocchi());
			currentForm.setListaOrdini(blocco1.getLista());

			if (blocco1 != null)
			{
			currentForm.setIdLista(blocco1.getIdLista()); //si
			currentForm.setTotRighe(blocco1.getTotRighe()); //no
			currentForm.setTotBlocchi(blocco1.getTotBlocchi()); //no
			currentForm.setMaxRighe(blocco1.getMaxRighe()); //no
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			currentForm.setUltimoBlocco(blocco1);
			}
			// fine gestione blocchi
		}
		else
		{
			currentForm.setListaOrdini(new ArrayList<OrdiniVO>());
			currentForm.setNumOrdini(0);
			currentForm.setRisultatiPresenti(false);
			currentForm.setTotRighe(0); //no
			currentForm.setTotBlocchi(0); //no
			currentForm.setMaxRighe(0); //no


			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));

		}


		return mapping.getInputForward();
	}

	private void impostaBib(ListaOrdiniForm currentForm, HttpServletRequest request) {
		try {
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			currentForm.setCodBiblioSelez(biblio);
			currentForm.getListaSintetica().clear();
			if (currentForm.getCodBiblioSelez() == null || currentForm.getCodBiblioSelez().equals("")) {
				currentForm.getListaSintetica().addAll(currentForm.getListaSinteticaCompleta());
			}
			else
			{
				for (int i = 0; i < currentForm.getListaSinteticaCompleta().size(); i++) {
					OrdiniVO ordine = currentForm.getListaSinteticaCompleta().get(i);
					if (ordine.getCodBibl().equals(currentForm.getCodBiblioSelez()) ) {
						currentForm.getListaSintetica().add(ordine);
					}
				}
			}
			currentForm.setListaOrdini(currentForm.getListaSintetica());
		} catch (Exception e) {
		}
		return;
	}
	private void caricaBibl(ListaOrdiniForm listaOrd) {
		try {
			List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
			//String ele ="";
			StrutturaCombo ele = new StrutturaCombo();
			lista.add(ele);

			if (listaOrd.getListaOrdini().size() > 0) {
				for (int i = 0; i < listaOrd.getListaSinteticaCompleta().size(); i++) {
					OrdiniVO eleOrd= listaOrd.getListaSinteticaCompleta().get(i);

					if (eleOrd.getCodBibl()!=null && eleOrd.getCodBibl().trim().length()>0)
					{
						if (i==listaOrd.getListaSinteticaCompleta().size()-1)
						{
							ele = new StrutturaCombo();
							ele.setCodice(eleOrd.getCodBibl());
						}
						ele = new StrutturaCombo();
						ele.setCodice(eleOrd.getCodBibl());

						if (!lista.contains(ele))
						{
							lista.add(ele);
						}
					}
				}
			}
			listaOrd.setListaCodBib(lista);

		} catch (Exception e) {
		}
		return;
	}




	public ActionForward listaInventariOrdine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		try {
            if (Navigation.getInstance(request).isFromBar()){

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.inventariAssenti"));


            	return mapping.getInputForward();
            }

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
				//ricSez.setSelezioniChiamato(arrSezioStrut);
				if (arrSezioStrut!=null && arrSezioStrut.size()==1)
				{
					OrdiniVO ordineSel=arrSezioStrut.get(0);
					request.setAttribute("codBibF", ordineSel.getCodPolo());
			        request.setAttribute("codBibO", ordineSel.getCodBibl());
			        request.setAttribute("codTipoOrd", ordineSel.getTipoOrdine());
			        request.setAttribute("annoOrd", ordineSel.getAnnoOrdine());
			        request.setAttribute("codOrd", ordineSel.getCodOrdine());
			        request.setAttribute("prov", "ordine");
			        request.setAttribute("titOrd", ordineSel.getTitolo());
			        return mapping.findForward("sifListeInventari");
				}
				else
				{
					return mapping.getInputForward();
				}
			}
			else
			{
				// messaggio di non nessuna selezione

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.ricerca"));

				return mapping.getInputForward();

			}
		}
		 catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		try {
			//ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			ListaSuppOrdiniVO ricArr=listaOrd.getSifOrdini();

			// solo per test
/*			ricArr=new ListaSuppOrdiniVO();
			ricArr.setIDOrd(1);
			//ricArr.setStatoOrdine("A");
			ricArr.setChiamante("/acquisizioni/fatture/esaminaFattura");
*/			//fine solo x test

			if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().trim().length()>0)
			{
					request.setAttribute("passaggioListaSuppOrdiniVO", ricArr);
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

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//COPIA ESATTA DI  ActionForward scegliInv MA PER BOTTONE CON LABEL DIVERSA PER INTERROGAZIONE

		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		try {

			//ListaSuppOrdiniVO ricSez=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			ListaSuppOrdiniVO ricSez=listaOrd.getSifOrdini();

			// solo per test
/*			ricSez=new ListaSuppOrdiniVO();
			ricSez.setIDOrd(1);
			//ricSez.setStatoOrdine("A");
			ricSez.setChiamante("/acquisizioni/fatture/esaminaFattura");
			//fine solo x test
*/

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
				 	request.setAttribute("passaggioListaSuppOrdiniVO", ricSez);
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricSez.getChiamante().trim()+".do");
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

	public ActionForward scegliOrd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//COPIA ESATTA DI  ActionForward scegliInv MA PER BOTTONE CON LABEL DIVERSA PER INTERROGAZIONE

		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		try {

			//ListaSuppOrdiniVO ricSez=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			ListaSuppOrdiniVO ricSez=listaOrd.getSifOrdini();

			// solo per test
/*			ricSez=new ListaSuppOrdiniVO();
			ricSez.setIDOrd(1);
			//ricSez.setStatoOrdine("A");
			ricSez.setChiamante("/acquisizioni/fatture/esaminaFattura");
			//fine solo x test
*/

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
				 	request.setAttribute("passaggioListaSuppOrdiniVO", ricSez);
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricSez.getChiamante().trim()+".do");
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


	public ActionForward scegliInv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		try {
			//COPIA ESATTA DI  ActionForward scegli MA PER BOTTONE CON LABEL DIVERSA PER DOC FISICO
			//ListaSuppOrdiniVO ricSez=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			ListaSuppOrdiniVO ricSez=listaOrd.getSifOrdini();

			// solo per test
/*			ricSez=new ListaSuppOrdiniVO();
			ricSez.setIDOrd(1);
			//ricSez.setStatoOrdine("A");
			ricSez.setChiamante("/acquisizioni/fatture/esaminaFattura");
			//fine solo x test
*/

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
				 	request.setAttribute("passaggioListaSuppOrdiniVO", ricSez);
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricSez.getChiamante().trim()+".do");
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
		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaOrd.isSessione()) {
			listaOrd.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = listaOrd.getBloccoSelezionato();
		String idLista = listaOrd.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				listaOrd.getListaOrdini().addAll(bloccoSucc.getLista());
			}
		}
		return mapping.getInputForward();
	}

	private List<OrdiniVO> getListaOrdiniVO(ListaSuppOrdiniVO criRicerca) throws Exception
	{
		List<OrdiniVO> listaOrdini;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaOrdini = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);
		return listaOrdini;
	}

	ListaSuppFatturaVO gestioneFatturaDaDocFisicoMtd(ListaSuppFatturaVO ricercaFatture) throws Exception
	{
		ListaSuppFatturaVO risultato;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		risultato = factory.getGestioneAcquisizioni().gestioneFatturaDaDocFisico(ricercaFatture);
		return risultato;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ListaOrdiniForm listaOrd = (ListaOrdiniForm) form;
		if (!listaOrd.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				//ricOrdini.setTipoOrdine((String) utenteEjb.getDefault(ConstantDefault.ACQ_TIPO_ORDINE));
				listaOrd.setElemXBlocchi(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_ELEM_BLOCCHI)));
				listaOrd.setTipoOrdinamSelez( (String) utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_ORDINAMENTO));
				// solo se non è già stata impostato lo stato
				String[] statoArr= null;
				// A,C,N
				int dim=0;
				if (listaOrd.getStatoArr()==null || (listaOrd.getStatoArr()!=null &&  listaOrd.getStatoArr().length==0))
				{
					if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_STATO_APERTO)))
					{
						dim=dim+1;
					}
					if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_STATO_CHIUSO)))
					{
						dim=dim+1;
					}
					if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_STATO_ANNULLATO)))
					{
						dim=dim+1;
					}
				}

				if (dim>0)
				{
					statoArr= new String[dim];
					int v=0;
					if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_STATO_APERTO)))
					{
						statoArr[v]="A";
						v=v+1;
					}
					if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_STATO_CHIUSO)))
					{
						statoArr[v]="C";
						v=v+1;
					}
					if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.GA_RIC_ORD_STATO_ANNULLATO)))
					{
						statoArr[v]="N";
						v=v+1;
					}
					listaOrd.setStatoArr(statoArr);
				}

			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.erroreDefault"));

				return mapping.getInputForward();
			}
		}

		return mapping.getInputForward();
	}


}
