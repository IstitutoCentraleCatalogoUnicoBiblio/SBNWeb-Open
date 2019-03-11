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
package it.iccu.sbn.web.actions.acquisizioni.offerte;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.web.actionforms.acquisizioni.offerte.OfferteRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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
import org.apache.struts.actions.LookupDispatchAction;

public class OfferteRicercaParzialeAction extends    LookupDispatchAction  {
	//private OfferteRicercaParzialeForm ricOfferte;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.indietro","indietro");
		map.put("ordine.label.fornitore","fornitoreCerca");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OfferteRicercaParzialeForm ricOfferte = (OfferteRicercaParzialeForm) form;

		try {

			if (Navigation.getInstance(request).isFromBar() )
			        return mapping.getInputForward();

			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.offerte") &&  (ricOfferte.getCodBibl()==null || (ricOfferte.getCodBibl()!=null && ricOfferte.getCodBibl().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute("attributeListaSuppOffertaFornitoreVO");
				request.getSession().removeAttribute("offerteSelected");
				request.getSession().removeAttribute("criteriRicercaOfferta");
*/			}


			ricOfferte.setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
			//Navigation.getInstance(request).getUtente().getCodPolo();
			// impostazione del bibliotecario
			//this.ricSuggerimenti.setCodBibliotec(Navigation.getInstance(request).getUtente().getUserId());

			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();

			ricOfferte.setCodBibl(biblio);
			this.loadTipoOrdinamento( ricOfferte);
			ricOfferte.setFornitore(new StrutturaCombo("",""));

			//ricSuggerimenti.setStatoSuggerimento("A");

			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						ricOfferte.getFornitore().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						ricOfferte.getFornitore().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
						ricOfferte.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricOfferte.getFornitore().setCodice("");
					ricOfferte.getFornitore().setDescrizione("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}

			ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");

			// controllo che non riceva l'attributo di sessione di una lista supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null && this.ricOrdini.isProvenienzaVAIA())

			if ((ricArr!=null &&  ricArr.getChiamante()!=null && ricArr.getSelezioniChiamato()==null) )
			{
				// per il layout
				ricOfferte.setVisibilitaIndietroLS(true);
				// non c'è creazione
				if (ricArr.isModalitaSif())
				{
					List<OffertaFornitoreVO> listaOfferte;
					listaOfferte=this.getListaOfferteVO(ricArr); // va in errore se non ha risultati
					this.caricaAttributeListaSupp(ricOfferte,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.findForward("cerca");
				}
				else
				{
					if (!ricOfferte.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
					{
						this.caricaAttributeListaSupp(ricOfferte,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					}
					else
					{
						ricOfferte.setRientroDaSif(false);
					}

					return mapping.getInputForward();
				}
			}
			else
			{
				return mapping.getInputForward();
			}


		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					this.caricaAttributeListaSupp( ricOfferte, (ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO"));
					ricOfferte.setVisibilitaIndietroLS(true);
					return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//return mapping.findForward("crea");

				}
				else
				{
					return mapping.getInputForward();
				}

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

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OfferteRicercaParzialeForm ricOfferte = (OfferteRicercaParzialeForm) form;
		try {
			String chiama=null;
			//if (ricOfferte.isLSRicerca())
			if (ricOfferte.isVisibilitaIndietroLS())
			{
				ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppOffertaFornitoreVO eleRicerca=new ListaSuppOffertaFornitoreVO();
			// rimozione della var di sess che visualizza la precedente sintetica
			request.getSession().removeAttribute("ultimoBloccoOfferte");
			String ticket=Navigation.getInstance(request).getUserTicket();

			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricOfferte.getCodBibl();
			String idOff=ricOfferte.getOffFornitore();
			StrutturaCombo forn=new StrutturaCombo("","");
			forn.setCodice(ricOfferte.getFornitore().getCodice());
			forn.setDescrizione(ricOfferte.getFornitore().getDescrizione());
			String statoOff="";
			StrutturaCombo pae=new StrutturaCombo("","");
			StrutturaCombo ling=new StrutturaCombo("","");
			StrutturaCombo bidOff=new StrutturaCombo("","");

			StrutturaCombo KTitIsdb=new StrutturaCombo("","");
			KTitIsdb.setDescrizione(ricOfferte.getTitolo());

			StrutturaCombo aut=new StrutturaCombo("","");
			aut.setCodice(ricOfferte.getAutore());

			StrutturaCombo classif=new StrutturaCombo("","");
			classif.setCodice(ricOfferte.getClassificazione());

			//String chiama=null;
			String ordina="";

			eleRicerca=new ListaSuppOffertaFornitoreVO( codP,  codB,   idOff,   forn,  statoOff,  pae ,  ling,   bidOff,  KTitIsdb, aut, classif,  chiama,  ordina );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(ricOfferte.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (ricOfferte.getTipoOrdinamSelez()!=null && !ricOfferte.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricOfferte.getTipoOrdinamSelez());
			}

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppOffertaFornitoreVO", eleRicerca);
			// controllo di esistenza risultati su db se ci sono eccezioni
			List<OffertaFornitoreVO> listaOfferte;
			listaOfferte=this.getListaOfferteVO(eleRicerca);
			return mapping.findForward("cerca");
			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OfferteRicercaParzialeForm ricOfferte = (OfferteRicercaParzialeForm) form;
		try {
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OfferteRicercaParzialeForm ricOfferte = (OfferteRicercaParzialeForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");
			if (ricArr!=null )
			{
				//ListaSuppCambioVO eleRicArr=ricArr.get(0);
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.getInputForward();
				}
			}
			else
			{
				return mapping.getInputForward();
			}
			//return Navigation.getInstance(request)..goBack();
			//return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List<OffertaFornitoreVO> getListaOfferteVO(ListaSuppOffertaFornitoreVO criRicerca) throws Exception
	{
	List<OffertaFornitoreVO> listaOfferte=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaOfferte = factory.getGestioneAcquisizioni().getRicercaListaOfferte(criRicerca);
	return listaOfferte;
	}

	private void caricaAttributeListaSupp(OfferteRicercaParzialeForm ricOfferte, ListaSuppOffertaFornitoreVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricOfferte.setCodBibl(ricArr.getCodBibl());
		ricOfferte.setAutore(ricArr.getAutore().getCodice());
		ricOfferte.setClassificazione(ricArr.getClassificazione().getCodice());
		ricOfferte.setTitolo(ricArr.getChiaveTitoloIsbd().getCodice());
		ricOfferte.setOffFornitore(ricArr.getIdentificativoOfferta());
		ricOfferte.getFornitore().setCodice(ricArr.getFornitore().getCodice());
		ricOfferte.getFornitore().setDescrizione(ricArr.getFornitore().getDescrizione());
		//String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
	}catch (Exception e) {	}
	}

	private void loadTipoOrdinamento(OfferteRicercaParzialeForm ricOfferte) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Codice offerta");
		lista.add(elem);
		elem = new StrutturaCombo("2","Cod. fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("3","Anno (disc.)");
		lista.add(elem);

		ricOfferte.setListaTipiOrdinam(lista);
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OfferteRicercaParzialeForm ricOfferte = (OfferteRicercaParzialeForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

/*			if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaFornitoreCerca( ricOfferte, request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( OfferteRicercaParzialeForm ricOfferte, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricOfferte.getCodBibl();
		String codForn=ricOfferte.getFornitore().getCodice();
		String nomeForn=ricOfferte.getFornitore().getDescrizione();
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0"; // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}


/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


			HttpSession httpSession = request.getSession();
			//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());

			OfferteRicercaParzialeForm ricOfferte = (OfferteRicercaParzialeForm) form;

				if (request.getParameter("cerca0") != null) {
					NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
					return mapping.findForward("cerca");
				}
				if (request.getParameter("crea0") != null) {
					NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
					return mapping.findForward("nuovo");
				}

				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);

			return mapping.getInputForward();
	}
*/
}
