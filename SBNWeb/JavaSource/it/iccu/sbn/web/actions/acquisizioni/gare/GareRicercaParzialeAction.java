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
package it.iccu.sbn.web.actions.acquisizioni.gare;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.gare.GareRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class GareRicercaParzialeAction extends    LookupDispatchAction  implements SbnAttivitaChecker{
	//private GareRicercaParzialeForm ricGare;
//

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.indietro","indietro");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_GARE_ACQUISTO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;

		try {

			if (Navigation.getInstance(request).isFromBar() )
			        return mapping.getInputForward();
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.gare") && (ricGare.getCodBibl()==null || (ricGare.getCodBibl()!=null && ricGare.getCodBibl().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute("attributeListaSuppGaraVO");
				request.getSession().removeAttribute("gareSelected");
				request.getSession().removeAttribute("criteriRicercaGara");
*/			}

			//Navigation.getInstance(request).getUtente().getCodPolo();
			// impostazione del bibliotecario
			//this.ricSuggerimenti.setCodBibliotec(Navigation.getInstance(request).getUtente().getUserId());
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			if (biblio!=null &&   (ricGare.getCodBibl()==null || (ricGare.getCodBibl()!=null && ricGare.getCodBibl().trim().length()==0)))
			{
				ricGare.setCodBibl(biblio);
			}
			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				ricGare.setCodBibl(bibScelta.getCod_bib());
			}

			this.loadStatiRichiestaOfferta( ricGare);
			this.loadTipoOrdinamento( ricGare);
			//ricSuggerimenti.setStatoSuggerimento("A");

			// condizioni di ricerca univoca
			if ( ricGare.getCodRichiestaOfferta()!=null && ricGare.getCodRichiestaOfferta().trim().length()!=0 )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricGare.setStatoRichiestOfferta("");
				ricGare.setCodBid("");
				ricGare.setDesBid("");
				ricGare.setDisabilitaTutto(true);
			}
			else
			{
				ricGare.setDisabilitaTutto(false);
			}

			String bid=(String) request.getAttribute("bid");
			String desc=(String) request.getAttribute("desc");
			String natura=(String) request.getAttribute("natura");


			if (bid!=null && bid.length()!=0 && desc!=null && desc.length()!=0 && natura!=null && natura.length()!=0)
			{
				// controllo se richiamo da VAI A o da interrogazione titoli come lista supp
				if (Navigation.getInstance(request)!=null  && Navigation.getInstance(request).getActionCaller()!=null && Navigation.getInstance(request).getActionCaller().equals("/gestionebibliografica/titolo/analiticaTitolo"))
//
				{
					//ricSuggerimenti.setProvenienzaVAIA(true);
					// imposta maschera di ricerca
					ricGare.setCodBid(bid);
					ricGare.setDesBid(desc);
					// imposta parametri di ricerca
					ListaSuppGaraVO eleRicerca=this.caricaParametriVAIA (ricGare, bid,desc,natura);
					//eleRicerca.setCodBibl(biblio);
					request.getSession().setAttribute("attributeListaSuppGaraVO", eleRicerca);
				}
			}

			// controllo se ho un risultato da interrogazione ricerca
			if (bid!=null && bid.length()!=0 )
			{
				String titolo=(String) request.getAttribute("titolo");
				// controllo se ho un risultato da interrogazione ricerca
				//String acq = request.getParameter("ACQUISIZIONI");
				//if ( acq != null) {
				ricGare.setCodBid(bid);
				if ( titolo != null) {
					ricGare.setDesBid(titolo);
				}
			}

			ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");

			// controllo che non riceva l'attributo di sessione di una lista supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null && this.ricOrdini.isProvenienzaVAIA())

			if ((ricArr!=null &&  ricArr.getChiamante()!=null && ricArr.getSelezioniChiamato()==null) )
			{
				// per il layout
				ricGare.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					ricGare.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine
					if (ricArr.isModalitaSif())
					{
						List<GaraVO> listaGare;
						listaGare=this.getListaGareVO(ricArr); // va in errore se non ha risultati
						this.caricaAttributeListaSupp( ricGare,ricArr ); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						return mapping.findForward("cerca");
					}
					else
					{
						if (!ricGare.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
						{
							this.caricaAttributeListaSupp(ricGare,ricArr ); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						}
						else
						{
							ricGare.setRientroDaSif(false);
						}
						return mapping.getInputForward();
					}

			}
			else
			{
				return mapping.getInputForward();
			}


		}	catch (ValidationException ve) {
/*				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
*/				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppGaraVO ricArrRec=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
					this.caricaAttributeListaSupp( ricGare,ricArrRec );
					//ricGare.setVisibilitaIndietroLS(true);
					//return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && !ricArrRec.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea
						if (ricArrRec.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
						{
							this.passaCriteri( ricGare, request); // imposta il crea con i valori cercati
							Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
							UserVO utente = Navigation.getInstance(request).getUtente();
							try {
								utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_GARE_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
								return mapping.findForward("crea");

							}   catch (UtenteNotAuthorizedException e) {
								//e.printStackTrace();
								//ActionMessages errors = new ActionMessages();
								//errors.add("generico", new ActionMessage("error.authentication.non_abilitato"));
								//this.saveErrors(request, errors);
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage("messaggio.info.noaut"));
								this.saveErrors(request, errors);
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
			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;

		try {

			String chiama=null;
			//if (ricGare.isLSRicerca())
			if (ricGare.isVisibilitaIndietroLS())
			{
				ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
				chiama=ricArr.getChiamante();
			}

			request.getSession().removeAttribute("ultimoBloccoGare");
			ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO();
			String ticket=Navigation.getInstance(request).getUserTicket();

			// condizioni di ricerca univoca
			if ( ricGare.getCodRichiestaOfferta()!=null && ricGare.getCodRichiestaOfferta().trim().length()!=0 )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricGare.setStatoRichiestOfferta("");
				ricGare.setCodBid("");
				ricGare.setDesBid("");
			}


			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricGare.getCodBibl();
			StrutturaCombo idtitolo=new StrutturaCombo("","");
			idtitolo.setCodice(ricGare.getCodBid());
			idtitolo.setDescrizione(ricGare.getDesBid());
			String codRich=ricGare.getCodRichiestaOfferta();
			String dataRich="";
			String statoRich=ricGare.getStatoRichiestOfferta();
			//String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(ricGare.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (ricGare.getTipoOrdinamSelez()!=null && !ricGare.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricGare.getTipoOrdinamSelez());
			}

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppGaraVO", eleRicerca);
			// controllo di esistenza risultati su db se ci sono eccezioni
			List<GaraVO> listaGare;
			listaGare=this.getListaGareVO(eleRicerca);
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
		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;
		try {
			this.passaCriteri( ricGare, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private ListaSuppGaraVO caricaParametriVAIA(GareRicercaParzialeForm ricGare, String bid, String desc, String natura)
	{
	try {

		// carica i criteri di ricerca da passare alla esamina

		String codP="";
		String codB="";
		StrutturaCombo idtitolo=new StrutturaCombo(bid,desc);
		String codRich=null;
		String dataRich=null;
		String statoRich=null;
		String chiama="/gestionebibliografica/titolo/analiticaTitolo";
		String ordina="";

		ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina);
		return	eleRicerca;


	}catch (Exception e) {return null;	}
	}


	private void passaCriteri(GareRicercaParzialeForm ricGare, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {
			String chiama=null;
			if (ricGare.isLSRicerca())
			{
				ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
				chiama=ricArr.getChiamante();
			}


		ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO();
		String ticket=Navigation.getInstance(request).getUserTicket();
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricGare.getCodBibl();
		StrutturaCombo idtitolo=new StrutturaCombo("","");
		idtitolo.setCodice(ricGare.getCodBid());
		idtitolo.setDescrizione(ricGare.getDesBid());
		//String codRich=ricGare.getCodRichiestaOfferta();
		String codRich="";
		String dataRich="";
		String statoRich=ricGare.getStatoRichiestOfferta();
		//String chiama=null;
		String ordina="";
		eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina );
		eleRicerca.setTicket(ticket);
		eleRicerca.setElemXBlocchi(ricGare.getElemXBlocchi());
		eleRicerca.setOrdinamento("");
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		request.getSession().setAttribute("ATTRIBUTEListaSuppGaraVO", eleRicerca);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
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


	private List<GaraVO> getListaGareVO(ListaSuppGaraVO criRicerca) throws Exception
	{
	List<GaraVO> listaGare=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaGare = factory.getGestioneAcquisizioni().getRicercaListaGare(criRicerca);
	return listaGare;
	}

	private void caricaAttributeListaSupp(GareRicercaParzialeForm ricGare, ListaSuppGaraVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricGare.setCodBibl(ricArr.getCodBibl());
		ricGare.setCodBid(ricArr.getBid().getCodice());
		ricGare.setCodRichiestaOfferta(ricArr.getCodRicOfferta());
		ricGare.setDesBid(ricArr.getBid().getDescrizione());
		ricGare.setStatoRichiestOfferta(ricArr.getStatoRicOfferta());
		//String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
	}catch (Exception e) {	}
	}

	private void loadTipoOrdinamento(GareRicercaParzialeForm ricGare) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Titolo");
		lista.add(elem);
		elem = new StrutturaCombo("2","Data (disc.)");
		lista.add(elem);
		elem = new StrutturaCombo("3","Cod.");
		lista.add(elem);

		/*
		elem = new StrutturaCombo("3","3 - triennale");
		lista.add(elem);
*/
		ricGare.setListaTipiOrdinam(lista);
	}
/*	private void loadStatoSuggerimento() throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		this.ricSuggerimenti.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento()));
	}*/


	private void loadStatiRichiestaOfferta(GareRicercaParzialeForm ricGare) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		 elem = new StrutturaCombo("1","Aperta");
		lista.add(elem);
		elem = new StrutturaCombo("2","Chiusa");
		lista.add(elem);
//		elem = new StrutturaCombo("3","Annullata");
//		lista.add(elem);
		elem = new StrutturaCombo("4","Ordinata");
		lista.add(elem);

		ricGare.setListaStatoRichiestaOfferta(lista);
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;
		try {
			request.setAttribute("bidFromRicOrd", ricGare.getCodBid());
			return mapping.findForward("sifbid");
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_GARE_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}*/
		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_GARE_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;
	}


/*
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		GareRicercaParzialeForm ricGare = (GareRicercaParzialeForm) form;

			if (request.getParameter("cerca0") != null)
			{
				return mapping.findForward("cerca");
			}
			if (request.getParameter("crea0") != null) {
				return mapping.findForward("nuovo");
			}


			this.loadStatiRichiestaOfferta();
			ricGare.setCodBibl("01");
			ricGare.setStatoRichiestaOfferta("1");
			ricGare.setCodRichiestaOfferta("0003");

			return mapping.getInputForward();
	}*/

}
