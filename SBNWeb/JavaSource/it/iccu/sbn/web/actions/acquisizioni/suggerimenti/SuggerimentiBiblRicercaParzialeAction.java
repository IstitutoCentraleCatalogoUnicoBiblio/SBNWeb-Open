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
package it.iccu.sbn.web.actions.acquisizioni.suggerimenti;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.suggerimenti.SuggerimentiBiblRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.actions.LookupDispatchAction;

public class SuggerimentiBiblRicercaParzialeAction extends LookupDispatchAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca", "cerca");
		map.put("ricerca.button.crea", "crea");
		map.put("ricerca.button.indietro", "indietro");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("ricerca.label.sezione", "ricercaBibliot");
		map.put("ordine.bottone.searchTit", "sifbid");

		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_SUGGERIMENTO_BIBLIOTECARIO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SuggerimentiBiblRicercaParzialeForm ricSuggerimenti = (SuggerimentiBiblRicercaParzialeForm) form;

		try {

			if (Navigation.getInstance(request).isFromBar() )
			        return mapping.getInputForward();

			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.suggerimenti") && (ricSuggerimenti.getCodBibl()==null || (ricSuggerimenti.getCodBibl()!=null && ricSuggerimenti.getCodBibl().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute("attributeListaSuppSuggerimentoVO");
				request.getSession().removeAttribute("suggerimentiSelected");
				request.getSession().removeAttribute("criteriRicercaSuggerimento");
*/			}
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			if (biblio!=null && (ricSuggerimenti.getCodBibl()==null || (ricSuggerimenti.getCodBibl()!=null && ricSuggerimenti.getCodBibl().trim().length()==0)))
			{
				ricSuggerimenti.setCodBibl(biblio);
			}


			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				ricSuggerimenti.setCodBibl(bibScelta.getCod_bib());
			}

			// temporaneo
			//ricSuggerimenti.setCodBibliotec(Navigation.getInstance(request).getUtente().getFirmaUtente());

			//Navigation.getInstance(request).getUtente().getCodPolo();
			// impostazione del bibliotecario
			//ricSuggerimenti.setCodBibliotec(Navigation.getInstance(request).getUtente().getUserId());

			//ricSuggerimenti.setCodBibl("");

			this.loadStatoSuggerimento(ricSuggerimenti);
			//ricSuggerimenti.setStatoSuggerimento("A");
			ricSuggerimenti.setTitolo(new StrutturaCombo("",""));
			this.loadTipoOrdinamento(ricSuggerimenti);

			// controllo se la ricerca è stata richiamata dal VAI A
			String bid=(String) request.getAttribute("bid");
			String desc=(String) request.getAttribute("desc");
			String natura=(String) request.getAttribute("natura");


			if (bid!=null && bid.length()!=0 && desc!=null && desc.length()!=0 && natura!=null && natura.length()!=0)
			{
				// controllo se richiamo da VAI A o da interrogazione titoli come lista supp
				if ( Navigation.getInstance(request)!=null  && Navigation.getInstance(request).getActionCaller()!=null && Navigation.getInstance(request).getActionCaller().equals("/gestionebibliografica/titolo/analiticaTitolo"))
//
				{
					//ricSuggerimenti.setProvenienzaVAIA(true);
					// imposta maschera di ricerca
					ricSuggerimenti.getTitolo().setCodice(bid);
					ricSuggerimenti.getTitolo().setDescrizione(desc);
					// imposta parametri di ricerca
					ListaSuppSuggerimentoVO eleRicerca=this.caricaParametriVAIA (ricSuggerimenti, bid,desc,natura);
					//eleRicerca.setCodBibl(biblio);
					request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", eleRicerca);
				}
			}


			// controllo se ho un risultato da interrogazione ricerca
			if (bid!=null && bid.length()!=0 )
			{
				String titolo=(String) request.getAttribute("titolo");
				// controllo se ho un risultato da interrogazione ricerca
				//String acq = request.getParameter("ACQUISIZIONI");
				//if ( acq != null) {
				ricSuggerimenti.getTitolo().setCodice(bid);
				if ( titolo != null) {
					ricSuggerimenti.getTitolo().setDescrizione(titolo);
				}
			}

			// controllo che non sia stata attivata la lista supporto dei bibliotecari

			UtenteVO bibliote=(UtenteVO) request.getAttribute("bibliotecario");

			if (bibliote!=null && bibliote.getUsername()!=null && bibliote.getUsername().length()>0)
			{
				ricSuggerimenti.setCodBibliotec(bibliote.getUsername());
				ricSuggerimenti.setNomeBibliotec(bibliote.getNome()+" " + bibliote.getCognome() );
				ricSuggerimenti.setIdBibliotec(String.valueOf(bibliote.getId()));
			}

			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");

			// controllo che non riceva l'attributo di sessione di una lista supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null && this.ricOrdini.isProvenienzaVAIA())

			if ((ricArr!=null &&  ricArr.getChiamante()!=null && ricArr.getSelezioniChiamato()==null) )
			{
				// per il layout
				ricSuggerimenti.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					ricSuggerimenti.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine
				if (ricArr.isModalitaSif())
				{
					this.getListaSuggerimentiVO(ricArr); // va in errore se non ha risultati
					this.caricaAttributeListaSupp(ricSuggerimenti, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.findForward("cerca");
				}
				else
				{
					if (!ricSuggerimenti.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
					{
						this.caricaAttributeListaSupp(ricSuggerimenti, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					}
					else
					{
						ricSuggerimenti.setRientroDaSif(false);
					}
					return mapping.getInputForward();
				}

			}
			else
			{
				return mapping.getInputForward();
			}


		}	catch (ValidationException ve) {
/*
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

*/				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppSuggerimentoVO ricArrRec=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
					this.caricaAttributeListaSupp(ricSuggerimenti, ricArrRec);
					//ricSuggerimenti.setVisibilitaIndietroLS(true);
					//return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && !ricArrRec.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))

					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

						return mapping.getInputForward();
					}
					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea
						this.passaCriteri( ricSuggerimenti, request); // imposta il crea con i valori cercati
						if (ricArrRec.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
						{
							Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
							UserVO utente = Navigation.getInstance(request).getUtente();
							try {
								utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);
								return mapping.findForward("crea");

							}   catch (UtenteNotAuthorizedException e) {
								//e.printStackTrace();
								//
								//LinkableTagUtils.addError(request, new ActionMessage("error.authentication.non_abilitato"));
								//

								LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));

								return mapping.getInputForward();
							}

						}
						else
						{
							return mapping.getInputForward();
						}
					}

				}
				else
				{
					return mapping.getInputForward();
				}

		}
		// altri tipi di errore
		catch (Exception e) {

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SuggerimentiBiblRicercaParzialeForm currentForm = (SuggerimentiBiblRicercaParzialeForm) form;
		try {
			String chiama=null;
			//if (ricSuggerimenti.isLSRicerca())
			if (currentForm.isVisibilitaIndietroLS())
			{
				ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppSuggerimentoVO eleRicerca = new ListaSuppSuggerimentoVO();
			request.getSession().removeAttribute("ultimoBloccoSugg");
			String ticket = Navigation.getInstance(request).getUserTicket();
			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String codP = polo;
			String bibl = currentForm.getCodBibl();
			String codSugg = currentForm.getCodSuggerim();
			String statoSugg = currentForm.getStatoSuggerimento();
			String dataSuggDa = currentForm.getDataInizio();
			String dataSuggA = currentForm.getDataFine();
			StrutturaCombo titSugg = new StrutturaCombo("", "");
			titSugg.setCodice(currentForm.getTitolo().getCodice());
			titSugg.setDescrizione(currentForm.getTitolo().getDescrizione());
			StrutturaCombo biblSugg = new StrutturaCombo("", "");
			biblSugg.setCodice(currentForm.getCodBibliotec().trim());
			if (biblSugg.getCodice() != null && biblSugg.getCodice().trim().length() > 0)
			{
				biblSugg.setDescrizione(currentForm.getIdBibliotec());
			}
			else
			{
				currentForm.setNomeBibliotec("");
			}

			StrutturaCombo sezSugg=new StrutturaCombo("","");
			//String chiama=null;
			String ordina="";

			eleRicerca=new ListaSuppSuggerimentoVO( codP,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(currentForm.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (currentForm.getTipoOrdinamSelez()!=null && !currentForm.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(currentForm.getTipoOrdinamSelez());
			}
			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", eleRicerca);
			// controllo di esistenza risultati su db se ci sono eccezioni
			this.getListaSuggerimentiVO(eleRicerca);
			return mapping.findForward("cerca");
			}	catch (ValidationException ve) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));

			return mapping.getInputForward();
		}

	}


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SuggerimentiBiblRicercaParzialeForm ricSuggerimenti = (SuggerimentiBiblRicercaParzialeForm) form;
		try {
			this.passaCriteri( ricSuggerimenti, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ricercaBibliot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private ListaSuppSuggerimentoVO caricaParametriVAIA(SuggerimentiBiblRicercaParzialeForm ricSuggerimenti, String bid, String desc, String natura)
	{
	try {

		// carica i criteri di ricerca da passare alla esamina

		String codP="";
		String bibl="";
		String codSugg=null;
		String statoSugg=null;
		String dataSuggDa=null;
		String dataSuggA=null;
		StrutturaCombo titSugg=new StrutturaCombo(bid,desc);
		StrutturaCombo biblSugg=new StrutturaCombo("","");
		StrutturaCombo sezSugg=new StrutturaCombo("","");
		String chiama="/gestionebibliografica/titolo/analiticaTitolo";
		String ordina="";
		ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO( codP,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );

		return	eleRicerca;


	}catch (Exception e) {return null;	}
	}




	private void passaCriteri(SuggerimentiBiblRicercaParzialeForm ricSuggerimenti, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {

			String chiama=null;
			if (ricSuggerimenti.isLSRicerca())
			{
				ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String bibl=ricSuggerimenti.getCodBibl();
			String codSugg="";
			String statoSugg="";
			String dataSuggDa="";
			String dataSuggA="";
			StrutturaCombo titSugg=new StrutturaCombo("","");
			titSugg.setCodice(ricSuggerimenti.getTitolo().getCodice());
			titSugg.setDescrizione(ricSuggerimenti.getTitolo().getDescrizione());
			StrutturaCombo biblSugg=new StrutturaCombo("","");
			biblSugg.setCodice(ricSuggerimenti.getCodBibliotec());
			StrutturaCombo sezSugg=new StrutturaCombo("","");
			//String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppSuggerimentoVO( polo,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(ricSuggerimenti.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("ATTRIBUTEListaSuppSuggerimentoVO", eleRicerca);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
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

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SuggerimentiBiblRicercaParzialeForm ricSuggerimenti = (SuggerimentiBiblRicercaParzialeForm) form;
		try {
			if (ricSuggerimenti.getTitolo()!=null && ricSuggerimenti.getTitolo().getCodice()!=null)
			{
				request.setAttribute("bidFromRicOrd", ricSuggerimenti.getTitolo().getCodice());
			}

			return mapping.findForward("sifbid");
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}

	private List<SuggerimentoVO> getListaSuggerimentiVO(ListaSuppSuggerimentoVO criRicerca) throws Exception
	{
	List<SuggerimentoVO> listaSuggerimenti=new ArrayList<SuggerimentoVO>();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaSuggerimenti = factory.getGestioneAcquisizioni().getRicercaListaSuggerimenti(criRicerca);
	return listaSuggerimenti;
	}

	private void caricaAttributeListaSupp(SuggerimentiBiblRicercaParzialeForm ricSuggerimenti, ListaSuppSuggerimentoVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricSuggerimenti.setCodBibl(ricArr.getCodBibl());
		if (ricSuggerimenti.getCodBibliotec()==null ||(ricSuggerimenti.getCodBibliotec()!=null && ricSuggerimenti.getCodBibliotec().trim().length()==0))
		{
			ricSuggerimenti.setCodBibliotec(ricArr.getBibliotecario().getCodice());
		}
		ricSuggerimenti.setCodSuggerim(ricArr.getCodiceSuggerimento());
		ricSuggerimenti.setDataFine(ricArr.getDataSuggerimentoA());
		ricSuggerimenti.setDataInizio(ricArr.getDataSuggerimentoDa());
		ricSuggerimenti.setStatoSuggerimento(ricArr.getStatoSuggerimento());
		//String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
	}catch (Exception e) {	}
	}

	private void loadTipoOrdinamento(SuggerimentiBiblRicercaParzialeForm ricSuggerimenti) throws Exception {
		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Codice sugg.");
		lista.add(elem);
		elem = new StrutturaCombo("2","Stato");
		lista.add(elem);
		elem = new StrutturaCombo("3","Data (disc)");
		lista.add(elem);
		elem = new StrutturaCombo("4","Cod. bibliotecario");
		lista.add(elem);
		elem = new StrutturaCombo("5","Cod. sezione");
		lista.add(elem);

		ricSuggerimenti.setListaTipiOrdinam(lista);
	}
	private void loadStatoSuggerimento(SuggerimentiBiblRicercaParzialeForm ricSuggerimenti) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricSuggerimenti.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento()));
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
*/		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {

				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;
	}

/*	private void loadStatoSuggerimento() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - Accettato");
		lista.add(elem);
		elem = new StrutturaCombo("W","W - Attesa di risposta");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - Ordinato");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - Rifiutato");
		lista.add(elem);

		ricSuggerimenti.setListaStatoSuggerimento(lista);
	}
*/
/*	private ListaSuppOrdiniVO caricaParametriVAIA( String bid, String desc, String natura)
	{
	try {

		// carica i criteri di ricerca da passare alla esamina
					String polo=Navigation.getInstance(request).getUtente().getCodPolo();
					String codP=polo;
		String codB="";
		//List<String> codBAff=ricOrdini.getBiblAffil();
		String codBAff=null;
		String codOrd=null;
		//String annoOrd=ricOrdini.;
		String tipoOrd=null; // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo (bid,desc);
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=natura;
		//String chiama="";
		String chiama="/gestionebibliografica/titolo/analiticaTitolo";
		// prova
		String annoOrd="";
		String[] statoOrdArr=ricOrdini.getStatoArr();
		Boolean stamp=false;
		Boolean rinn=false;
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr,rinn,stamp);
		return	eleRicerca;

	}catch (Exception e) {return null;	}
	}*/

}
