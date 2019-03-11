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
package it.iccu.sbn.web.actions.acquisizioni.fatture;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.actionforms.acquisizioni.fatture.ListaFattureForm;
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

public class ListaFattureAction extends SinteticaLookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
		map.put("button.blocco", "caricaBlocco");
		map.put("ricerca.button.esamina", "esamina");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			ListaFattureForm listaFatt = (ListaFattureForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			if(!listaFatt.isSessione())
			{
				listaFatt.setSessione(true);
			}

			ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
			listaFatt.setSifFatture(ricArr);
			if (ricArr.getTipoFattura().equals("N"))
			{
				listaFatt.setNoteCredito(true);
			}

			//solo x test
/*			ricArr=new ListaSuppOrdiniVO();
			//ricArr.setIDOrd(1);
			//ricArr.setStatoOrdine("A");
			ricArr.setCodPolo("CSW");
			ricArr.setCodBibl(" FI");
			ricArr.setTicket(Navigation.getInstance(request).getUserTicket());
			ricArr.setChiamante("/acquisizioni/fatture/esaminaFattura");
			List<String> provaListBid=new ArrayList<String>();
			String eleProvaListBid="LO10487929";
			provaListBid.add(eleProvaListBid);
			eleProvaListBid="NAP0085399";
			provaListBid.add(eleProvaListBid);
			eleProvaListBid="RAV0028330";
			provaListBid.add(eleProvaListBid);
			//ricArr.setBidList(provaListBid);
			ricArr.setBidList(null);
			ricArr.setOrdinamento("8");

			//ricArr.setCodOrdine("1");
			//ricArr.setAnnoOrdine("2008");
			//ricArr.setTipoOrdine("A");
			listaOrd.setSifOrdini(ricArr);
*/
			//fine solo x test

			List<FatturaVO> listaFatture=null;
			if (ricArr==null)
			{
				// ricerca ampia
				ricArr=new ListaSuppFatturaVO();
				ricArr.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				ricArr.setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
				ricArr.setTicket(Navigation.getInstance(request).getUserTicket());
				if (Navigation.getInstance(request).getBackAction()!=null)
				{
					ricArr.setChiamante(Navigation.getInstance(request).getBackAction());
				}
				else
				{
					ricArr.setChiamante("");
				}
				ricArr.setOrdinamento("8");
			}

			listaFatture=this.getListaOrdiniVO(ricArr);
			// test da rimuovere x documento fisico
			// chiave  ordine - bibl - tipo - anno - progr
			// num fatt, data fatt., fornitore
/*			ListaSuppFatturaVO ricercaFatture=new ListaSuppFatturaVO();
			ricercaFatture.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			ricercaFatture.setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
			ricercaFatture.setTicket(Navigation.getInstance(request).getUserTicket());
			ricercaFatture.setFornitore(new StrutturaCombo("1", "almaviva7"));
			//ricercaFatture.setBilancio(new StrutturaTerna("2007", "12", "3")); // 2007,12,3
			ricercaFatture.setOrdine(new StrutturaTerna("A", "2008", "7")); //7
			ricercaFatture.setDataFattura("01/01/2008");
			ricercaFatture.setAnnoFattura("2008");
			ricercaFatture.setNumFattura("99");
			ricercaFatture.setTipoFattura("F");
			ricercaFatture.setImportoFattura(33.00);
			ricercaFatture.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			if (Navigation.getInstance(request).getBackAction()!=null)
			{
				ricercaFatture.setChiamante(Navigation.getInstance(request).getBackAction());
			}
			else
			{
				ricercaFatture.setChiamante("");
			}
			ricercaFatture.setOrdinamento("");
			ListaSuppFatturaVO risultato=this.gestioneFatturaDaDocFisicoMtd(ricercaFatture);
*/			// fine test da rimuovere


			if (listaFatture!=null && listaFatture.size()>0)
			{
				listaFatt.setListaFatture(listaFatture);
				listaFatt.setNumFatture(listaFatture.size());
				// gestione blocchi
				DescrittoreBloccoVO blocco1= null;
				UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
				String ticket=utenteCollegato.getTicket();

				int maxElementiBlocco=10;
				int maxRighe=10;

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaFatture,maxElementiBlocco);
				listaFatt.setListaFatture(blocco1.getLista());

				if (blocco1 != null)
				{
					listaFatt.setIdLista(blocco1.getIdLista()); //si
					listaFatt.setTotRighe(blocco1.getTotRighe()); //no
					listaFatt.setTotBlocchi(blocco1.getTotBlocchi()); //no
					listaFatt.setMaxRighe(blocco1.getMaxRighe()); //no
					listaFatt.setBloccoSelezionato(blocco1.getNumBlocco()); //si
					listaFatt.setUltimoBlocco(blocco1);
				}
				// fine gestione blocchi
			}

			// gestione automatismo check su unico elemento lista con radio
			if (listaFatt.getListaFatture()!=null &&  listaFatt.getListaFatture().size()==1)
			{
				listaFatt.setRadioSel(listaFatt.getListaFatture().get(0).getChiave()); //.trim()
			}


			return mapping.getInputForward();

			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);

					if (ve.getError()==4001)
					{
						listaFatt.setRisultatiPresenti(false);
					}

					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaFattureForm listaFatt = (ListaFattureForm) form;
		try {
			if (listaFatt!=null && listaFatt.getRadioSel()!=null &&  listaFatt.getRadioSel().trim().length() >0)
			{
				String [] appoSez=new String [1];
				appoSez[0]=listaFatt.getRadioSel().trim();
				listaFatt.setSelectedFatture(appoSez);
				List<FatturaVO> arrSezioStrut=new ArrayList<FatturaVO>();
				for (int i=0;  i < listaFatt.getSelectedFatture().length; i++)
				{
					String codProfil=listaFatt.getSelectedFatture()[i].trim();
					for (int j=0;  j < listaFatt.getListaFatture().size(); j++)
					{

						FatturaVO profilStrut=listaFatt.getListaFatture().get(j);
						if (profilStrut.getChiave().trim().equals(codProfil))
						{
							arrSezioStrut.add(profilStrut);
						}
					}
				}
				if (arrSezioStrut!=null && arrSezioStrut.size()==1 )
				{
					FatturaVO fattScelta=new FatturaVO();
					fattScelta=arrSezioStrut.get(0);
					int identif= fattScelta.getIDFatt();
					ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
					// carica i criteri di ricerca da passare alla esamina
					String polo=Navigation.getInstance(request).getUtente().getCodPolo();
					String codP="";
					String codB="";
					String annoF="";
					String numF="";
					String staF="";
					String dataDa="";
					String dataA="";
					String prgF="";
					String dataF="";
					String dataRegF="";
					String tipF="F"; // DEVE CERCARE FRA LE FATTURE
					StrutturaTerna ordFatt=new StrutturaTerna("","","");
					StrutturaCombo fornFatt=new StrutturaCombo("","");
					StrutturaTerna bilFatt=new StrutturaTerna("","","");
					String chiama=mapping.getPath();
					String ordina="";
					eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
					//
					eleRicerca.setIDFatt(identif);
					request.getSession().setAttribute("passaggioEsaminaFatturaSingle", eleRicerca);
					return mapping.findForward("esamina");
				}
				else
				{
					return mapping.getInputForward();
				}
			}
			else
			{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.ricerca"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaFattureForm listaFatt = (ListaFattureForm) form;
		try {
			//ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			ListaSuppFatturaVO ricArr=listaFatt.getSifFatture();

			// solo per test
/*			ricArr=new ListaSuppOrdiniVO();
			ricArr.setIDOrd(1);
			//ricArr.setStatoOrdine("A");
			ricArr.setChiamante("/acquisizioni/fatture/esaminaFattura");
*/			//fine solo x test

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

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaFattureForm listaFatt = (ListaFattureForm) form;
		try {

			//ListaSuppOrdiniVO ricSez=(ListaSuppOrdiniVO) request.getAttribute("passaggioListaSuppOrdiniVO");
			ListaSuppFatturaVO ricSez=listaFatt.getSifFatture();

			// solo per test
/*			ricSez=new ListaSuppOrdiniVO();
			ricSez.setIDOrd(1);
			//ricSez.setStatoOrdine("A");
			ricSez.setChiamante("/acquisizioni/fatture/esaminaFattura");
			//fine solo x test
*/

			if (listaFatt!=null && listaFatt.getRadioSel()!=null &&  listaFatt.getRadioSel().trim().length() >0)
			{
				String [] appoSez=new String [1];
				appoSez[0]=listaFatt.getRadioSel().trim();
				listaFatt.setSelectedFatture(appoSez);
				List<FatturaVO> arrSezioStrut=new ArrayList<FatturaVO>();
				for (int i=0;  i < listaFatt.getSelectedFatture().length; i++)
				{
					String codProfil=listaFatt.getSelectedFatture()[i].trim();
					for (int j=0;  j < listaFatt.getListaFatture().size(); j++)
					{

						FatturaVO profilStrut=listaFatt.getListaFatture().get(j);
						if (profilStrut.getChiave().trim().equals(codProfil))
						{
							//profilStrut.getRigheDettaglioFattura().get(0).setIDFattNC(listaFatt.getListaFatture().get(j).getIDFatt());
							arrSezioStrut.add(profilStrut);
							break;
						}
					}
				}
				// aggiornamento della variabile di sessione
				ricSez.setSelezioniChiamato(arrSezioStrut);
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().trim().length()>0)
			{
/*					ListaSuppFatturaVO ricSezNuovo=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");

					if (ricSezNuovo!=null && ricSezNuovo.getSelezioniChiamato()!=null && ricSezNuovo.getSelezioniChiamato().size()>0 && ricSezNuovo.getSelezioniChiamato().get(0)!=null &&  ricSezNuovo.getSelezioniChiamato().get(0).getIDFatt().equals(ricSez.getIDFatt()) && ricSezNuovo.getSelezioniChiamato().get(0).getRigheDettaglioFattura().size()>0 && ricSezNuovo.getSelezioniChiamato().get(0).getRigheDettaglioFattura().get(0).getFattura().getCodice3()!=null && ricSezNuovo.getSelezioniChiamato().get(0).getRigheDettaglioFattura().get(0).getFattura().getCodice3().trim().length()>0)
					{
						// e' stata effettuata la scelta di una riga
						request.getSession().setAttribute("passaggioListaSuppFatturaVO", (ListaSuppFatturaVO) ricSezNuovo);
					}
					else
					{
						request.getSession().setAttribute("passaggioListaSuppFatturaVO", (ListaSuppFatturaVO) ricSez);
					}
*/
					request.getSession().setAttribute("passaggioListaSuppFatturaVO", ricSez);

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
		ListaFattureForm listaFatt = (ListaFattureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaFatt.isSessione()) {
			listaFatt.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = listaFatt.getBloccoSelezionato();
		String idLista = listaFatt.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				listaFatt.getListaFatture().addAll(bloccoSucc.getLista());
			}
		}
		return mapping.getInputForward();
	}

	private List<FatturaVO> getListaOrdiniVO(ListaSuppFatturaVO criRicerca) throws Exception
	{
		List<FatturaVO> listaOrdini;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaOrdini = factory.getGestioneAcquisizioni().getRicercaListaFatture(criRicerca);
		return listaOrdini;
	}

}
