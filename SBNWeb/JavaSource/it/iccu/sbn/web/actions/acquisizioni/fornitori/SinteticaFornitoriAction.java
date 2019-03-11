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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.DatiFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RicercaTitCollEditoriVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fornitori.SinteticaFornitoriForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
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

public class SinteticaFornitoriAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		map.put("ricerca.label.nomeForn","criterioNome");
		map.put("ricerca.label.cod","criterioCod");
		map.put("ricerca.label.tipoOrdine","criterioTipo");
		map.put("ricerca.label.unitaorg","criterioUO");
		map.put("ricerca.label.indirizzo","criterioInd");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.crea","crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "Ok");
		map.put("ricerca.button.cercaInPolo", "cercaPolo");
		map.put("ricerca.button.stampa","stampaOnLine");

		map.put("ricerca.button.titCollEditore","titoliCollegatiEditore");
		map.put("ricerca.button.creazLegameTitEdit","creazLegameTitEdit");
		return map;
	}

	public ActionForward stampaOnLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;

			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA, utente.getCodPolo(), utente.getCodBib(), null);

			}   catch (UtenteNotAuthorizedException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


			if (sintFornitori.getSelectedFornitori()==null || sintFornitori.getSelectedFornitori().length==0) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else {

				List<ListaSuppFornitoreVO> ricerca=new ArrayList();
				this.PreparaRicercaFornitore( sintFornitori, request);
				ricerca=(List<ListaSuppFornitoreVO>) request.getSession().getAttribute("criteriRicercaFornitore");
				List<FornitoreVO> listaFornitoriSel = null;

				if (ricerca!=null &&  ricerca.size()>0)
				{
					listaFornitoriSel = new ArrayList<FornitoreVO> ();
					for (int j=0;  j < ricerca.size(); j++)
					{
						List<FornitoreVO> fornitoreSel=new ArrayList<FornitoreVO>();
						fornitoreSel=getListaFornitoriVO(ricerca.get(j));
						listaFornitoriSel.add(fornitoreSel.get(0));
					}
				}

				request.setAttribute("FUNZIONE_STAMPA",     StampaType.STAMPA_LISTA_FORNITORI);
				request.setAttribute("DATI_STAMPE_ON_LINE", listaFornitoriSel);

				return mapping.findForward("stampaOL");
			}
		} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			return mapping.getInputForward();
		}

	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("fornitoriSelected")!= null
						&& !request.getSession().getAttribute("fornitoriSelected").equals(""))
				{
					sintFornitori.setSelectedFornitori((String[]) request.getSession().getAttribute("fornitoriSelected"));
				}
				return mapping.getInputForward();
			}

			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
			if (request.getAttribute("editore") != null) {
				sintFornitori.setEditore((String)request.getAttribute("editore"));
				if (request.getAttribute("creazLegameTitEdit") != null && ((String)request.getAttribute("creazLegameTitEdit")).equals("SI")) {
					sintFornitori.setCreazLegameTitEdit("SI");
					if (request.getAttribute("bid") != null) {
						sintFornitori.setBid((String) request.getAttribute("bid"));
					}
					if (request.getAttribute("descr") != null) {
						sintFornitori.setDescr((String) request.getAttribute("descr"));
					}
				} else {
					sintFornitori.setCreazLegameTitEdit("NO");
				}
				if (request.getAttribute("cartiglioEditore") != null && ((String)request.getAttribute("cartiglioEditore")).equals("SI")) {
					sintFornitori.setCartiglioEditore("SI");
				} else {
					sintFornitori.setCartiglioEditore("NO");
				}
			} else {
				sintFornitori.setEditore("NO");
			}



			if(!sintFornitori.isSessione())
			{
				sintFornitori.setSessione(true);
			}

			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr!=null)
			{
				sintFornitori.setOrdinamentoScelto(ricArr.getOrdinamento());
			}

			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null) {
				// imposta visibilità bottone scegli
				sintFornitori.setVisibilitaIndietroLS(true);
				// per il layout
				if (ricArr.getChiamante().endsWith("RicercaParziale")
					|| ricArr.getChiamante().equals("/acquisizioni/buoniordine/inserisciBuonoOrdine")
					|| ricArr.getChiamante().equals("/acquisizioni/buoniordine/esaminaBuonoOrdine")
					|| ricArr.getChiamante().equals("/acquisizioni/fatture/inserisciFattura")
					|| ricArr.getChiamante().equals("/acquisizioni/fatture/esaminaFattura")
					|| ricArr.getChiamante().equals("/acquisizioni/comunicazioni/inserisciCom")
					|| ricArr.getChiamante().equals("/acquisizioni/comunicazioni/esaminaCom")) {
					sintFornitori.setLSRicerca(true); // fai rox 4
				}
			}
			if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().trim().length()!=0 &&  ricArr.getLocale().equals("1"))	{
				// imposta visibilità bottone cerca in polo
				sintFornitori.setCercaInPolo(true);
			}

			List<FornitoreVO> listaFornitori=new ArrayList();

			if (ricArr!=null) {
				listaFornitori=this.getListaFornitoriVO(ricArr ); // va in errore se non ha risultati
			}

			//this.loadFornitori();
			sintFornitori.setListaFornitori(listaFornitori);
			sintFornitori.setNumFornitori(sintFornitori.getListaFornitori().size());

			// gestione automatismo check su unico elemento lista
			if (sintFornitori.getListaFornitori().size()==1) {
				String [] appoSelProva= new String [1];
				appoSelProva[0]=sintFornitori.getListaFornitori().get(0).getCodFornitore();
				//	"FI|2007|3";
				sintFornitori.setSelectedFornitori(appoSelProva);
			}


			// gestione blocchi

			DescrittoreBloccoVO blocco1= null;
			//String ticket=Navigation.getInstance(request).getUserTicket();
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String ticket=utenteCollegato.getTicket();

			int maxElementiBlocco=10;
			int maxRighe=10;

			if (ricArr.getElemXBlocchi()>0)
			{
				maxElementiBlocco=ricArr.getElemXBlocchi();
				maxRighe=ricArr.getElemXBlocchi();
			}

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// deve essere escluso il caso di richiamo di lista supporto offerte fornitore
			if 	(request.getSession().getAttribute("ultimoBloccoFornitori")!=null
					&& ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null))) {
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoFornitori");
				//n.b la lista è quella memorizzata nella variabile di sessione
			} else {
				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaFornitori,maxElementiBlocco);
				sintFornitori.setListaFornitori(blocco1.getLista());
			}

			if (blocco1 != null) {
				//if (blocco1 == null)
				//abilito i tasti per il blocco se necessario
				//memorizzo le informazioni per la gestione blocchi
				sintFornitori.setIdLista(blocco1.getIdLista()); //si
				sintFornitori.setTotRighe(blocco1.getTotRighe()); //no
				sintFornitori.setTotBlocchi(blocco1.getTotBlocchi()); //no
				sintFornitori.setMaxRighe(blocco1.getMaxRighe()); //no
				sintFornitori.setBloccoSelezionato(blocco1.getNumBlocco()); //si
				sintFornitori.setUltimoBlocco(blocco1);
				sintFornitori.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi


			// non trovati
			if (sintFornitori.getNumFornitori()==0)	{
				return mapping.getInputForward();
			}
			else {
				if 	(request.getSession().getAttribute("fornitoriSelected")!= null)
				{
					sintFornitori.setSelectedFornitori((String[]) request.getSession().getAttribute("fornitoriSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricArr.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getCodFornitore();
						for (int v=0;  v < sintFornitori.getListaFornitori().size(); v++)
						{
							String eleList= sintFornitori.getListaFornitori().get(v).getCodFornitore();
							if (eleList.equals(eleSele))
							{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									sintFornitori.getListaFornitori().get(v).setTipoVariazione(variato);
								}
								break;
							}
						}
					}
				}
				//ordinamento

				// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
				// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
				if (sintFornitori.getEditore().equals("SI")) {
					Navigation.getInstance(request).setTesto("Sintetica Editori (Produzione editoriale)");
				}
				if (sintFornitori.getCartiglioEditore().equals("SI")) {
					Navigation.getInstance(request).setTesto("Sintetica Editori (Produzione editoriale)");
					ricArr.setChiamante("/gestionestampe/editori/stampaTitoliEditore");
					sintFornitori.setVisibilitaIndietroLS(true);
					sintFornitori.setLSRicerca(true);
				}



				return mapping.getInputForward();
			}
			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
					// assenzaRisultati = 4001;

					if (ve.getError()==4001) {
						sintFornitori.setRisultatiPresenti(false);
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

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;

		// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
		request.setAttribute("editore", sintFornitori.getEditore());

		// OTTOBRE 2013: si aggiorna l'attributo con i dati dell'interrogazione così da passarli all'oggetto creazione
		request.getSession().setAttribute("ATTRIBUTEListaSuppFornitoreVO",
						request.getSession().getAttribute("attributeListaSuppFornitoreVO"));

		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cercaPolo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		try {

			sintFornitori.setCercaInPolo(false);

			List<FornitoreVO> listaFornitori;
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().trim().length()!=0 &&  ricArr.getLocale().equals("1"))
			{
				ricArr.setLocale("0");
				// riscateno la ricerca
				listaFornitori=this.getListaFornitoriVO(ricArr ); // va in errore se non ha risultati
				//this.loadFornitori();
				sintFornitori.setListaFornitori(listaFornitori);
				sintFornitori.setNumFornitori(sintFornitori.getListaFornitori().size());

				// gestione blocchi
				DescrittoreBloccoVO blocco1= null;
				//String ticket=Navigation.getInstance(request).getUserTicket();
				UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
				String ticket=utenteCollegato.getTicket();

				int maxElementiBlocco=10;
				int maxRighe=5;

				if (ricArr.getElemXBlocchi()>0)
				{
					maxElementiBlocco=ricArr.getElemXBlocchi();
					maxRighe=ricArr.getElemXBlocchi();
				}

				// ok blocco1=GestioneAcquisizioniBean.class.newInstance().gestBlock(ticket,listaOfferte,prova);
				//blocco1=SbnBusinessSessionBean.class.newInstance().saveBlocchi(ticket,listaOfferte,listaOfferte.size());
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaFornitori,maxElementiBlocco);
				sintFornitori.setListaFornitori(blocco1.getLista());

				if (blocco1 != null)
				{
				//if (blocco1 == null)
				//abilito i tasti per il blocco se necessario
				//memorizzo le informazioni per la gestione blocchi
				sintFornitori.setIdLista(blocco1.getIdLista()); //si
				sintFornitori.setTotRighe(blocco1.getTotRighe()); //no
				sintFornitori.setTotBlocchi(blocco1.getTotBlocchi()); //no
				sintFornitori.setMaxRighe(blocco1.getMaxRighe()); //no
				sintFornitori.setBloccoSelezionato(blocco1.getNumBlocco()); //si
				sintFornitori.setUltimoBlocco(blocco1);
				sintFornitori.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
				}
				// fine gestione blocchi

			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
				if (sintFornitori.getProgrForm() > 0) {
				try {
						this.PreparaRicercaFornitoreSingle( sintFornitori, request,sintFornitori.getProgrForm()-1);

						// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
						// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
						request.setAttribute("editore", sintFornitori.getEditore());

						return mapping.findForward("esamina");

					} catch (Exception e) {
						return mapping.getInputForward();
					}
				}
				else
				{
					return mapping.getInputForward();
				}
		}


	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sintFornitori.isSessione()) {
			sintFornitori.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sintFornitori.getBloccoSelezionato();
		String idLista = sintFornitori.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sintFornitori.getListaFornitori().addAll(bloccoSucc.getLista());
				request.getSession().setAttribute("ultimoBloccoFornitori",bloccoSucc);
				sintFornitori.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoFornitori",sintFornitori.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		try {
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			if (ricArr!=null )
			{
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



	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		try {
			String[] appoParametro=new String[0];
			String[] appoSelezione=new String[0];
			appoSelezione=sintFornitori.getSelectedFornitori();
			appoParametro=(String[])request.getSession().getAttribute("fornitoriSelected");

			//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			if (appoSelezione!=null && appoSelezione.length!=0)
			{
				//this.AggiornaParametriSintetica(request);
				this.PreparaRicercaFornitore( sintFornitori, request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute("attributeListaSuppFornitoreVO", this.AggiornaRisultatiListaSupporto(  sintFornitori,
						(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO")));
				request.getSession().setAttribute("fornitoriSelected", appoSelezione);
				request.getSession().setAttribute("listaFornitoriEmessa", sintFornitori.getListaFornitori());

				// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
				// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
				request.setAttribute("editore", sintFornitori.getEditore());

				return mapping.findForward("esamina");

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

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Inserito nuovo metodo per la ricerca dei titoli collegati in modo esplicito o implicito all'editore selezionato
	// viene richiamata dalla sintetica fornitori/editori con il tasto Titoli Collegati
	public ActionForward titoliCollegatiEditore(ActionMapping mapping, ActionForm form, HttpServletRequest request,
																HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		try {
			String[] appoSelezione=new String[0];
			appoSelezione=sintFornitori.getSelectedFornitori();
			String[] appoEditSelezione=new String[appoSelezione.length];
			int indIsbd=0;

			if (appoSelezione!=null && appoSelezione.length!=0)	{

				String [] listaSelezionati=sintFornitori.getSelectedFornitori();

				// carica i criteri di ricerca da passare alla esamina
				for (int i=0;  i < listaSelezionati.length; i++) {
					String eleSel= listaSelezionati[i];

					for (int j=0;  j < sintFornitori.getListaFornitori().size(); j++) {
						FornitoreVO eleElenco=sintFornitori.getListaFornitori().get(j);
						if (eleElenco.getCodFornitore().equals(eleSel)){
							appoEditSelezione[indIsbd]= eleElenco.getCodFornitore();
							indIsbd++;
						}
					}
				}

				RicercaTitCollEditoriVO ricercaTitCollEditoriVO = new RicercaTitCollEditoriVO();
				ricercaTitCollEditoriVO.setEditSelez(appoEditSelezione);

				AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
				InterrogazioneTitoloGeneraleVO interrGener = new InterrogazioneTitoloGeneraleVO();
				interrGener.setTitolo("");
				interrGener.setNumStandardSelez("");
				interrGener.setNumStandard1("");
				interrGener.setImpronta1("");
				interrGener.setImpronta2("");
				interrGener.setImpronta3("");
				interrGener.setData1A("");
				interrGener.setData1Da("");
				interrGener.setData2A("");
				interrGener.setData2Da("");
				interrGener.setNomeCollegato("");
				interrGener.setLuogoPubbl("");
				interrGener.setTipoMateriale("");
				interrGener.setTipiRecordSelez("");
				interrGener.setNaturaSelez1("");
				interrGener.setNaturaSelez2("");
				interrGener.setNaturaSelez3("");
				interrGener.setNaturaSelez4("");
				interrGener.setSottoNaturaDSelez("");
				interrGener.setPaeseSelez("");
				interrGener.setLinguaSelez("");
				interrGener.setTipoDataSelez("");
				interrGener.setResponsabilitaSelez("");
				interrGener.setRelazioniSelez("");
				areaDatiPass.setInterTitGen(interrGener);
				areaDatiPass.setRicercaPolo(true);
				areaDatiPass.setRicercaIndice(false);
				areaDatiPass.setOggChiamante(99);
				areaDatiPass.setTipoOggetto(99);
				areaDatiPass.setTipoOggettoFiltrato(99);
				areaDatiPass.setOggDiRicerca("");

				AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = this.getListaTitCollEditore(ricercaTitCollEditoriVO, request);

				if (areaDatiPassReturn == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.ricerca"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				if (areaDatiPassReturn.getCodErr() != "0000") {
					if (areaDatiPassReturn.getCodErr().equals("3001")) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultati"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					} else {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.database.load",areaDatiPassReturn.getCodErr()));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
				request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);
				request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassReturn);
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliCollEditore"));
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private AreaDatiPassaggioInterrogazioneTitoloReturnVO getListaTitCollEditore(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, HttpServletRequest request) throws Exception {

		UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		String ticket=utenteCollegato.getTicket();

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneAcquisizioni().getRicercaTitCollEditori(ricercaTitCollEditoriVO, ticket);
		return areaDatiPassReturn;
	}

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Inserito nuovo metodo per la creazione del legame titolo-Editorein modo esplicito (tabella apposita)
	public ActionForward creazLegameTitEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;

		if (sintFornitori.getSelectedFornitori().length > 1) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.ricercaOchecksingolo"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (sintFornitori.getSelectedFornitori().length < 1) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.ricerca"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
		areaDatiLegameTitoloVO.setBidPartenza(sintFornitori.getBid());
		areaDatiLegameTitoloVO.setDescPartenza(sintFornitori.getDescr());
		areaDatiLegameTitoloVO.setFlagCondivisoPartenza(false);
		areaDatiLegameTitoloVO.setFlagCondivisoLegame(false);

		areaDatiLegameTitoloVO.setTipoLegameNew("EDIT_TIT");
		areaDatiLegameTitoloVO.setTipoOperazione("Crea");

		String [] listaSelezionati=sintFornitori.getSelectedFornitori();
		for (int i=0;  i < listaSelezionati.length; i++) {
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sintFornitori.getListaFornitori().size(); j++) {
				FornitoreVO eleElenco=sintFornitori.getListaFornitori().get(j);
				if (eleElenco.getCodFornitore().equals(eleSel)){
					// Intervento Luglio 2013: prima di effettuare il lagame fra Titolo e Editore/Fornitore si deve verificare
					// che sia Editore (presenza del codice Regione)
					if (ValidazioneDati.notEmpty(eleElenco.getRegione())) {
						areaDatiLegameTitoloVO.setIdArrivo(eleElenco.getCodFornitore());
						areaDatiLegameTitoloVO.setDescArrivo(eleElenco.getNomeFornitore());
						break;
					} else {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.erroreCreazLegameTitoloForn"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
			}
		}

		request.setAttribute("AreaDatiLegameTitoloVO", areaDatiLegameTitoloVO);
		return mapping.findForward("gestioneLegameTitolo");
	}







	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sintFornitori.isSessione())
			{
				sintFornitori.setSessione(true);
			}
		}
		String [] appoLista= new String [sintFornitori.getListaFornitori().size()];
		int i;
		for (i=0;  i < sintFornitori.getListaFornitori().size(); i++)
		{
			FornitoreVO forn= sintFornitori.getListaFornitori().get(i);
			String cod=forn.getCodFornitore();
			appoLista[i]=cod;
		}
		sintFornitori.setSelectedFornitori(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sintFornitori.isSessione())
			{
				sintFornitori.setSessione(true);
			}
		}
		try {
			sintFornitori.setSelectedFornitori(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	/**
	 * SinteticaFornitoriAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppFornitoreVO AggiornaRisultatiListaSupporto (SinteticaFornitoriForm sintFornitori, ListaSuppFornitoreVO eleRicArr)
	{
		try {
			List<FornitoreVO> risultati=new ArrayList();
			FornitoreVO eleFornitore=new FornitoreVO();
			String [] listaSelezionati=sintFornitori.getSelectedFornitori();

			String codP;
			String codB;
			String codForn;
			String nomeForn;
			String uOrg;
			String indForn;
			String cPostForn;
			String cittaForn;
			String capForn;
			String telForn;
			String faxForn;
			String noteForn;
			String pIvaForn;
			String codFiscForn;
			String eMailForn;
			String paeseForn;
			String tipoPForn;
			String provForn;
			String tipoVar;
			String biblFornitore;
			DatiFornitoreVO fornBibl;

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sintFornitori.getListaFornitori().size(); j++)
				{
					FornitoreVO eleElenco=sintFornitori.getListaFornitori().get(j);
					if (eleSel.equals(eleElenco.getCodFornitore()))
					{
						codP=eleElenco.getCodPolo();
						codB=eleElenco.getCodBibl();
						codForn=eleElenco.getCodFornitore();
						nomeForn=eleElenco.getNomeFornitore();
						uOrg=eleElenco.getUnitaOrg();
						indForn=eleElenco.getIndirizzo();
						cPostForn=eleElenco.getCasellaPostale();
						cittaForn=eleElenco.getCitta();
						capForn=eleElenco.getCap();
						telForn=eleElenco.getTelefono();
						faxForn=eleElenco.getFax();
						noteForn=eleElenco.getNote();
						pIvaForn=eleElenco.getPartitaIva();
						codFiscForn=eleElenco.getCodiceFiscale();
						eMailForn=eleElenco.getEmail();
						paeseForn=eleElenco.getPaese();
						tipoPForn =eleElenco.getTipoPartner();
						provForn=eleElenco.getProvincia();
						tipoVar="";
						biblFornitore=""; // codice della biblioteca importata fra i fornitori
						fornBibl=null;

						eleFornitore=new FornitoreVO( codP,  codB,  codForn,  nomeForn ,  uOrg,  indForn, cPostForn, cittaForn, capForn, telForn, faxForn, noteForn, pIvaForn, codFiscForn, eMailForn, paeseForn,  tipoPForn, provForn, tipoVar, fornBibl, biblFornitore);
						risultati.add(eleFornitore);
					}
				}
			}
			//ricArrAppo=new ArrayList();
			eleRicArr.setSelezioniChiamato(risultati);
			//ListaSuppCambioVO eleRicArrAppo=eleRicArr;
			//ricArrAppo.add(eleRicArrAppo);

		} catch (Exception e) {

		}
		return eleRicArr;
	}

	private List<FornitoreVO> getListaFornitoriVO(ListaSuppFornitoreVO criRicerca) throws Exception
	{
	List<FornitoreVO> listaFornitori;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaFornitori = factory.getGestioneAcquisizioni().getRicercaListaFornitori(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	return listaFornitori;
	}

	/**
	 * SinteticaFornitoriAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaFornitore(SinteticaFornitoriForm sintFornitori,HttpServletRequest request)
	{
		try {
			List<ListaSuppFornitoreVO> ricerca=new ArrayList();
			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			String [] listaSelezionati=sintFornitori.getSelectedFornitori();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sintFornitori.getListaFornitori().size(); j++)
				{
					FornitoreVO eleElenco=sintFornitori.getListaFornitori().get(j);
					String chiaveComposta=eleElenco.getCodFornitore();
					//chiaveComposta[3] codOrdine
					//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{

						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						//String codB=eleElenco.getCodBibl();
						String ticket=Navigation.getInstance(request).getUserTicket();
						// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
						String biblio=Navigation.getInstance(request).getUtente().getCodBib();
						String codB=biblio; // da sostituire con acquisizione di biblioteca dell'utente connesso
						ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
						if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().trim().length()>0)
						{
							codB= ricArr.getCodBibl();
						}
						if (eleElenco.getFornitoreBibl()!=null && eleElenco.getFornitoreBibl().getCodBibl()!=null && eleElenco.getFornitoreBibl().getCodBibl().trim().length()>0)
						{
							codB=eleElenco.getFornitoreBibl().getCodBibl();
						}

						String loc="";
						String codForn=eleElenco.getCodFornitore();
						//String nomeForn=eleElenco.getNomeFornitore();
						String nomeForn="";
						String codProfAcq="";
						String paeseForn="";
						String tipoPForn="";
						String provForn="";
						String chiama="";
						String ordina="";
						eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama,loc);
						eleRicerca.setOrdinamento(ordina);
						if (sintFornitori.getOrdinamentoScelto()!=null && sintFornitori.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sintFornitori.getOrdinamentoScelto());
						}

						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaFornitore", ricerca);
		} catch (Exception e) {
		}
	}

	private void  PreparaRicercaFornitoreSingle(SinteticaFornitoriForm sintFornitori,HttpServletRequest request, int j)
	{
		try {
			List<ListaSuppFornitoreVO> ricerca=new ArrayList();
			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			FornitoreVO eleElenco=sintFornitori.getListaFornitori().get(j);
				// carica i criteri di ricerca da passare alla esamina
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				//String codB=eleElenco.getCodBibl();
				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();

				String codB=biblio; // da sostituire con acquisizione di biblioteca dell'utente connesso
				ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().trim().length()>0)
				{
					codB= ricArr.getCodBibl();
				}
				if (eleElenco.getFornitoreBibl()!=null && eleElenco.getFornitoreBibl().getCodBibl()!=null && eleElenco.getFornitoreBibl().getCodBibl().trim().length()>0)
				{
					codB=eleElenco.getFornitoreBibl().getCodBibl();
				}
				String loc="";
				String codForn=eleElenco.getCodFornitore();
				String nomeForn=eleElenco.getNomeFornitore();
				String codProfAcq="";
				String paeseForn="";
				String tipoPForn="";
				String provForn="";
				String chiama="";
				String ordina="";
				eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama,loc);
				eleRicerca.setOrdinamento(ordina);
				if (sintFornitori.getOrdinamentoScelto()!=null && sintFornitori.getOrdinamentoScelto().trim().length()>0 )
				{
					eleRicerca.setOrdinamento(sintFornitori.getOrdinamentoScelto());
				}

				ricerca.add(eleRicerca);
			request.getSession().setAttribute("criteriRicercaFornitore", ricerca);
		} catch (Exception e) {
		}
	}


	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFornitoriForm sintFornitori = (SinteticaFornitoriForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");

			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=sintFornitori.getSelectedFornitori();
						appoParametro=(String[])request.getSession().getAttribute("fornitoriSelected");
						request.getSession().setAttribute("attributeListaSuppFornitoreVO", this.AggiornaRisultatiListaSupporto( sintFornitori, ricArr));

						if (sintFornitori.getCartiglioEditore() != null && sintFornitori.getCartiglioEditore().equals("SI")) {
							request.setAttribute("codForn", (sintFornitori.getSelectedFornitori()[0]));
							request.setAttribute("descrForn", this.AggiornaRisultatiListaSupporto( sintFornitori, ricArr).getSelezioniChiamato().get(0).getNomeFornitore());
							request.setAttribute("cartiglioEditore", "SI");
						}
					}
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
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

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_FORNITORI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		return false;
	}

}
