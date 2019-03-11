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
package it.iccu.sbn.web.actions.acquisizioni.documenti;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RifiutoSuggerimentiDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.documenti.SinteticaDocForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
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

public class SinteticaDocAction extends SinteticaLookupDispatchAction {

	//private SinteticaDocForm sinDoc;


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		map.put("ricerca.button.esamina","esamina");
//		map.put("ricerca.button.stampa","stampa");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.crea","crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "Ok");
		map.put("ricerca.button.rifiuta","rifiuta");
		map.put("ricerca.button.stampa","stampaOnLine");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;

		try {
/*			if (request.getParameter("parametroPassato") != null  )
			{
				boolean vero=true;
			}
*/
			if (Navigation.getInstance(request).isFromBar() )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("documentiSelected")!= null && !request.getSession().getAttribute("documentiSelected").equals(""))
				{
					sinDoc.setSelectedDocumenti((String[]) request.getSession().getAttribute("documentiSelected"));
				}

				return mapping.getInputForward();
			}

				if(!sinDoc.isSessione())
				{
					sinDoc.setSessione(true);
				}
				ListaSuppDocumentoVO ricArr=(ListaSuppDocumentoVO) request.getSession().getAttribute("attributeListaSuppDocumentoVO");
				if (ricArr!=null)
				{
					sinDoc.setOrdinamentoScelto(ricArr.getOrdinamento());
				}

				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					sinDoc.setVisibilitaIndietroLS(true);
					// per il layout
					if (ricArr.getChiamante().endsWith("RicercaParziale"))
					{
						sinDoc.setLSRicerca(true); // fai rox 4
					}
				}
				if (ricArr==null)
				{
					// l'attributo di sessione deve essere valorizzato
					sinDoc.setRisultatiPresenti(false);
				}
				List<DocumentoVO> listaDocumenti=new ArrayList();
				// deve essere escluso il caso di richiamo di lista supporto cambi
/*				if 	(request.getSession().getAttribute("listaDocEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
				{
					listaDocumenti=(List<DocumentoVO>)request.getSession().getAttribute("listaDocEmessa");
				}
				else
				{
					listaDocumenti=this.getListaDocumentiVO(ricArr ); // va in errore se non ha risultati
				}
*/
				if (ricArr!=null)
				{
					listaDocumenti=this.getListaDocumentiVO(ricArr ); // va in errore se non ha risultati
				}

				//this.loadStatoSuggerimento();
				sinDoc.setListaDocumenti(listaDocumenti);
				sinDoc.setNumDocumenti(sinDoc.getListaDocumenti().size());

				// gestione automatismo check su unico elemento lista
				if (sinDoc.getListaDocumenti().size()==1)
				{
					String [] appoSelProva= new String [1];
					appoSelProva[0]=sinDoc.getListaDocumenti().get(0).getChiave();
					//	"FI|2007|3";
					sinDoc.setSelectedDocumenti(appoSelProva);
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

				// ok blocco1=GestioneAcquisizioniBean.class.newInstance().gestBlock(ticket,listaOfferte,prova);
				//blocco1=SbnBusinessSessionBean.class.newInstance().saveBlocchi(ticket,listaOfferte,listaOfferte.size());
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				// deve essere escluso il caso di richiamo di lista supporto offerte fornitore
				if 	(request.getSession().getAttribute("ultimoBloccoDoc")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
				{
					blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoDoc");
					//n.b la lista è quella memorizzata nella variabile di sessione
				}
				else
				{

					blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaDocumenti,maxElementiBlocco);
					sinDoc.setListaDocumenti(blocco1.getLista());
				}

				if (blocco1 != null)
				{
				//if (blocco1 == null)
				//abilito i tasti per il blocco se necessario
				//memorizzo le informazioni per la gestione blocchi
				sinDoc.setIdLista(blocco1.getIdLista()); //si
				sinDoc.setTotRighe(blocco1.getTotRighe()); //no
				sinDoc.setTotBlocchi(blocco1.getTotBlocchi()); //no
				sinDoc.setMaxRighe(blocco1.getMaxRighe()); //no
				//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
				sinDoc.setBloccoSelezionato(blocco1.getNumBlocco()); //si
				//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
				//sinOfferte.setNumRighe(2);
				//sinOfferte.setNumBlocco(1);
				sinDoc.setUltimoBlocco(blocco1);
				sinDoc.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
				}
				// fine gestione blocchi

				// non trovati
				if (sinDoc.getNumDocumenti()==0)
				{
					sinDoc.setRisultatiPresenti(false);
					return mapping.getInputForward();
				}
				else {
					// impostazione attributo sessione dei selezionati
					if 	(request.getSession().getAttribute("documentiSelected")!= null)
					{
						sinDoc.setSelectedDocumenti((String[]) request.getSession().getAttribute("documentiSelected"));
					}
					//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
					if (ricArr.getSelezioniChiamato()!=null)
					{
						for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
						{
							String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave().trim();
							for (int v=0;  v < sinDoc.getListaDocumenti().size(); v++)
							{
								String eleList= sinDoc.getListaDocumenti().get(v).getChiave().trim();
								if (eleList.equals(eleSele))
								{
									String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
									if (variato!=null && variato.length()!=0)
									{
										sinDoc.getListaDocumenti().get(v).setTipoVariazione(variato);
									}
									break;
								}
							}
						}
					}
					//ordinamento
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
						sinDoc.setRisultatiPresenti(false);
					}
					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {
				e.printStackTrace();
				ActionMessages errors = new ActionMessages();
				//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
	}

/*	public ActionForward rifiuta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sinDoc.isSessione())
			{
				sinDoc.setSessione(true);
			}
		}
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}*/

	public ActionForward rifiuta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_SUGGERIMENTO_LETTORE, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		}

		try {
			String[] appoSelezione=new String[0];
			appoSelezione=sinDoc.getSelectedDocumenti();

			if (appoSelezione==null || appoSelezione.length==0) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			else
			{
				List<Integer> listaIDOrdini = new ArrayList<Integer> ();

				for (int v=0; v< sinDoc.getSelectedDocumenti().length; v++)
				{
					for (int u=0; u<sinDoc.getListaDocumenti().size(); u++)
					{
						if (sinDoc.getSelectedDocumenti()[v].equals(sinDoc.getListaDocumenti().get(u).getChiave()))
						  {
							listaIDOrdini.add(sinDoc.getListaDocumenti().get(u).getIDDoc());
							break;
						  }
					}
				}
				OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO = new OperazioneSuOrdiniMassivaVO();

				operazioneSuOrdiniVO.setTipoOperazione("S");

				// impostare gli id degli ordini selezionati come array e passarlo

				operazioneSuOrdiniVO.setDatiInput(listaIDOrdini);

				operazioneSuOrdiniVO.setCodPolo(sinDoc.getListaDocumenti().get(0).getCodPolo()); // assumo il codice del primo della lista
				operazioneSuOrdiniVO.setCodBib(sinDoc.getListaDocumenti().get(0).getCodBibl()); // assumo il codice del primo della lista

				String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				operazioneSuOrdiniVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				String downloadLinkPath = "/";
				operazioneSuOrdiniVO.setDownloadPath(downloadPath);
				operazioneSuOrdiniVO.setDownloadLinkPath(downloadLinkPath);
				operazioneSuOrdiniVO.setTicket(Navigation.getInstance(request).getUserTicket());
				operazioneSuOrdiniVO.setUser(Navigation.getInstance(request).getUtente().getFirmaUtente());
                FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				// new gestione batch inizio
                RifiutoSuggerimentiDiffVO richiesta = new RifiutoSuggerimentiDiffVO();
				richiesta.setCodPolo(operazioneSuOrdiniVO.getCodPolo());
				richiesta.setCodBib(operazioneSuOrdiniVO.getCodBib());
				richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
				richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_GESTIONE_SUGGERIMENTO_LETTORE);
				//richiesta.setParametri(inputForStampeService);
				//richiesta.setTipoOrdinamento(ordinamFile);
				richiesta.setOperazioneSuOrdini(operazioneSuOrdiniVO);
				richiesta.setTicket(Navigation.getInstance(request).getUserTicket());

				//AmministrazionePolo  anministrazionePolo;
				String s =  null;
				try {
					s = DomainEJBFactory.getInstance().getPolo().prenotaElaborazioneDifferita(richiesta, null);
				} catch (ApplicationException e) {
					if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (s == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotBatchKO"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}

				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();

			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward stampaOnLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			SinteticaDocForm sinDoc = (SinteticaDocForm) form;

			if (sinDoc.getSelectedDocumenti()==null || sinDoc.getSelectedDocumenti().length==0) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else {



				List<DocumentoVO> listaSugg = new ArrayList<DocumentoVO> ();

				for (int v=0; v< sinDoc.getSelectedDocumenti().length; v++)
				{
					for (int u=0; u<sinDoc.getListaDocumenti().size(); u++)
					{
						if (sinDoc.getSelectedDocumenti()[v].trim().equals(sinDoc.getListaDocumenti().get(u).getChiave().trim()))
						  {
							listaSugg.add(sinDoc.getListaDocumenti().get(u));
							break;
						  }
					}
				}


				if (listaSugg!=null && listaSugg.size()>0)
				{
						// DA RIPRISTINARE PER LA STAMPA (almaviva)
						//TODO GVCANCE
						request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_SUGGERIMENTI_LETTORE);
						//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
						request.setAttribute("DATI_STAMPE_ON_LINE", listaSugg);
						return mapping.findForward("stampaOL");

				}
		}

		return mapping.getInputForward();

	} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			return mapping.getInputForward();
		}

	}



	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SinteticaDocForm sinDoc = (SinteticaDocForm) form;
			if (sinDoc.getProgrForm() > 0) {
					try {
							this.PreparaRicercaDocumentoSuggerimentoSingle( sinDoc, request,sinDoc.getProgrForm()-1);
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
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sinDoc.isSessione()) {
			sinDoc.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinDoc.getBloccoSelezionato();
		String idLista = sinDoc.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinDoc.getListaDocumenti().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoDoc",bloccoSucc);
				sinDoc.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoDoc",sinDoc.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppDocumentoVO ricArr=(ListaSuppDocumentoVO) request.getSession().getAttribute("attributeListaSuppDocumentoVO");
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

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppDocumentoVO ricArr=(ListaSuppDocumentoVO) request.getSession().getAttribute("attributeListaSuppDocumentoVO");

			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=sinDoc.getSelectedDocumenti();
						appoParametro=(String[])request.getSession().getAttribute("documentiSelected");

						//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
						//{
							//this.AggiornaParametriSintetica(request);
							request.getSession().setAttribute("attributeListaSuppDocumentoVO", this.AggiornaRisultatiListaSupporto( sinDoc, ricArr));
						//}
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

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		try {
			String[] appoParametro=new String[0];
			String[] appoSelezione=new String[0];
			appoSelezione=sinDoc.getSelectedDocumenti();
/*			appoParametro=(String[])request.getSession().getAttribute("documentiSelected");
			// vedere se appoparametro è null
			if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			{
				this.AggiornaParametriSintetica(request);
			}
			appoSelezione=(String[]) sinDoc.getSelectedDocumenti();
			appoParametro=(String[])request.getSession().getAttribute("documentiSelected");
*/
			//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))


			if (appoSelezione!=null && appoSelezione.length!=0)

			{
				//this.AggiornaParametriSintetica(request);
				this.PreparaRicercaDocumentoSuggerimento( sinDoc, request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute("attributeListaSuppDocumentoVO", this.AggiornaRisultatiListaSupporto( sinDoc,  (ListaSuppDocumentoVO)request.getSession().getAttribute("attributeListaSuppDocumentoVO")));
				request.getSession().setAttribute("documentiSelected", appoSelezione);
				//request.getSession().setAttribute("listaSuggEmessa", (List<SuggerimentoVO>)sinSugg.getListaSuggerimenti());

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
	/**
	 * SinteticaDocAction.java
	 * @param request
	 * Questo metodo provvede a seguito di un submit al recupero dell'oggetto di form che contiene i codici degli elementi di lista selezionati nella pagina attiva
	 * e dell'oggetto di sessione che riporta la sintesi di tutti i codici degli oggetti precedentemente selezionati nelle diverse pagine
	 * Inoltre memorizza il range di righe e la posizione dell'ultima pagina visitata per consentire la paginazione
	 * Dopo avere effettuato il merge delle informazioni delle ultime selezioni/deselezioni e delle precedenti memorizzate
	 * ripulisce ed aggiorna l'oggetto di sessione con tutti i codici degli oggetti selezionati
	 */
/*	private void AggiornaParametriSintetica(HttpServletRequest request)
	{

		String[] appoParametro=new String[0];
		String[] appoSelezione=new String[0];
		String[] nuovoParametro=new String[0];

		appoSelezione=(String[]) sinDoc.getSelectedDocumenti();
		appoParametro=(String[])request.getSession().getAttribute("documentiSelected");
		//int nRighe=this.sinSezione.getNumRigheOld();
		//int nPos=this.sinSezione.getPosElementoOld();
		//List <SezioneVO> listaOld=this.sinSezione.getSezioniVisualizzateOld();
		List <DocumentoVO>  listaOld=new ArrayList();

		if (this.sinCom.getComunicazioniVisualizzateOld()!=null && this.sinCom.getComunicazioniVisualizzateOld().size()!=0 )
		{
			listaOld=this.sinCom.getComunicazioniVisualizzateOld();
		}
		else
		{
			listaOld=this.sinCom.getComunicazioniVisualizzate();
		}

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			//this.sinSezione.getListaSezioni()
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione, listaOld);
			request.getSession().setAttribute("documentiSelected", (String[])nuovoParametro);
			sinDoc.setSelectedDocumenti((String[]) nuovoParametro );
		}

	}
*/	/**
	 * SinteticaDocAction.java
	 * @param parametro
	 * @param selezionati
	 * @param nRighe
	 * @param nPos
	 * @param lista
	 * @return
	 * Questo metodo viene chiamato dopo un submit della pagina di sintetica
	 * ed effettua il merge delle informazioni dei codici delle ultime selezioni/deselezioni con i codici precedentemente memorizzati
	 * restituendo un array con tutti i codici degli oggetti selezionati ripulito dai doppioni e dalle deselezioni ed aggiornato
	 */
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<DocumentoVO> lista)
	{
		String[] appoParametro=new String[0];
		String[] appoParametroFinale=new String[0];
		String[] selezioneComposta=new String[0];

		String cod="";
		String elePar="";
		String eleSel="";
		Boolean memorizzato=false;
		Boolean selezionato=false;
		Boolean ristruttura=false;
		int w=0;
		int j=0;

		// anche se selected è vuoto è possibile che ci siano stati solo dechek
		if (parametro!=null && parametro.length >0)
		{
			//ciclo sulla lista getElencaCambi (postata dal submit) per individuare i selezionati e i decheck
			for (int x=0;  x < lista.size(); x++)
			{
				DocumentoVO doc= (DocumentoVO) lista.get(x);
				cod=doc.getChiave().trim();
				// è stato precedentemente selezionato e memorizzato?

					memorizzato=false;
					for (w=0;  w < parametro.length; w++)
					{
						elePar= (String) parametro[w];
						if (elePar.equals(cod)) {
							memorizzato=true;
							break;
						}
					}
					if (memorizzato)
					{
						ristruttura=true;
						// se codice memorizzato ma non più selezionato: status dechek eliminare da parametro
						// se codice selezionato e già memorizzato: eliminare da parametro perchè viene aggiunto con selected
						// eliminazione da parametro del codice in oggetto
						if (parametro.length > 1) {
							appoParametro=new String[parametro.length-1];
							int y=0;
							for (j=0;  j < parametro.length-1; j++)
							{
								if (j==w)
								{
									// salto l'elemento da eliminare
									y=y+1;
								}
								appoParametro[j]= parametro[y];
								y=y+1;
							}
						}

						else {
							appoParametro=new String[0];
						}
						parametro=new String[appoParametro.length];
						System.arraycopy(appoParametro,0,parametro,0,appoParametro.length);
					}
			}
		}

		if (parametro!=null && parametro.length >0)
		{
			if (selezionati!=null && selezionati.length >0)
			{
				// parametri + selezioni
				selezioneComposta=new String[parametro.length +selezionati.length];
				System.arraycopy(parametro,0,selezioneComposta,0,parametro.length);
				System.arraycopy(selezionati,0,selezioneComposta,parametro.length,selezionati.length);
			}
			else
			{
				// solo parametri
				selezioneComposta=new String[parametro.length];
				System.arraycopy(parametro,0,selezioneComposta,0,parametro.length);
			}
		}
		else
		{
			if ( selezionati!=null && selezionati.length >0)
			{
				// solo selezioni
				selezioneComposta=new String[selezionati.length];
				System.arraycopy(selezionati,0,selezioneComposta,0,selezionati.length);
			}

		}
		parametro=new String[selezioneComposta.length];
		System.arraycopy(selezioneComposta,0,parametro,0,selezioneComposta.length);
		return parametro;
	}
*/

	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		String [] appoLista= new String [sinDoc.getListaDocumenti().size()];
		int i;
		for (i=0;  i < sinDoc.getListaDocumenti().size(); i++)
		{
			DocumentoVO doc= sinDoc.getListaDocumenti().get(i);
			String cod=doc.getChiave().trim();
			appoLista[i]=cod;
		}
		sinDoc.setSelectedDocumenti(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaDocForm sinDoc = (SinteticaDocForm) form;
		try {
			sinDoc.setSelectedDocumenti(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	/**
	 * SinteticaDocAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppDocumentoVO AggiornaRisultatiListaSupporto (SinteticaDocForm sinDoc , ListaSuppDocumentoVO eleRicArr)
	{
		try {

			List<DocumentoVO> risultati=new ArrayList();
			DocumentoVO eleCom=new DocumentoVO();
			String [] listaSelezionati=sinDoc.getSelectedDocumenti();

			String codP;
			String codB;
			String codDoc;
			String statoSuggDoc;
			StrutturaTerna ute;
			StrutturaCombo tit;
			String dataI;
			String dataA;
			String aut;
			String edi;
			String luogo;
			StrutturaCombo pae;
			StrutturaCombo lin;
			String annoEdi;
			String noteDoc;
			String msgXLet;

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinDoc.getListaDocumenti().size(); j++)
				{
					DocumentoVO eleElenco=sinDoc.getListaDocumenti().get(j);
					if (eleSel.equals(eleElenco.getChiave().trim()))
					{
						codP="";
						codB=eleElenco.getCodBibl();
						codDoc=eleElenco.getCodDocumento();
						statoSuggDoc=eleElenco.getStatoSuggerimentoDocumento();
						ute=eleElenco.getUtente();
						tit=eleElenco.getTitolo();
						dataI=eleElenco.getDataIns();
						dataA=eleElenco.getDataAgg();
						aut=eleElenco.getPrimoAutore();
						edi=eleElenco.getEditore();
						luogo=eleElenco.getLuogoEdizione();
						pae=eleElenco.getPaese();
						lin=eleElenco.getLingua();
						annoEdi=eleElenco.getAnnoEdizione();
						noteDoc=eleElenco.getNoteDocumento();
						msgXLet=eleElenco.getMsgPerLettore();

						//
						eleCom=new DocumentoVO( codP,  codB,  codDoc,  statoSuggDoc,  ute,  tit,  dataI, dataA,  aut,  edi,  luogo,  pae,  lin,  annoEdi,  noteDoc,  msgXLet);
						eleCom.setNaturaBid(eleElenco.getNaturaBid());
						eleCom.setIDDoc(eleElenco.getIDDoc());
						risultati.add(eleCom);
					}
				}
			}
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}

	/**
	 * SinteticaDocAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaDocumentoSuggerimento(SinteticaDocForm sinDoc, HttpServletRequest request)
	{
		try {
			List<ListaSuppDocumentoVO> ricerca=new ArrayList();
			ListaSuppDocumentoVO eleRicerca=new ListaSuppDocumentoVO();
			String [] listaSelezionati=sinDoc.getSelectedDocumenti();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinDoc.getListaDocumenti().size(); j++)
				{
					DocumentoVO eleElenco=sinDoc.getListaDocumenti().get(j);
					String chiaveComposta=eleElenco.getChiave().trim();
					//chiaveComposta[3] codOrdine
					//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{
						// carica i criteri di ricerca da passare alla esamina
						String ticket=Navigation.getInstance(request).getUserTicket();
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=eleElenco.getCodBibl();
						String codDoc=eleElenco.getCodDocumento();
						//String statoSuggDoc=eleElenco.getStatoSuggerimentoDocumento();
						String statoSuggDoc="";
						StrutturaTerna ute=new StrutturaTerna("","","");
						StrutturaCombo tit=new StrutturaCombo("","");
						String dataDa="";
						String dataA="";
						String edi="";
						String luogo="";
						StrutturaCombo pae=new StrutturaCombo("","");
						StrutturaCombo lin=new StrutturaCombo("","");
						String annoEdi="";
						String chiama=null;
						String ordina="";

						eleRicerca=new ListaSuppDocumentoVO( codP,  codB,  codDoc,  statoSuggDoc,  ute,  tit,  dataDa,  dataA,   edi,  luogo,  pae,  lin,  annoEdi,    chiama,  ordina );
						if (sinDoc.getOrdinamentoScelto()!=null && sinDoc.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sinDoc.getOrdinamentoScelto());
						}
						eleRicerca.setTicket(ticket);

						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaDocumento", ricerca);
		} catch (Exception e) {
		}
	}

	private void  PreparaRicercaDocumentoSuggerimentoSingle(SinteticaDocForm sinDoc, HttpServletRequest request, int j)
	{
		try {
			List<ListaSuppDocumentoVO> ricerca=new ArrayList();
			ListaSuppDocumentoVO eleRicerca=new ListaSuppDocumentoVO();
			DocumentoVO eleElenco=sinDoc.getListaDocumenti().get(j);
			// carica i criteri di ricerca da passare alla esamina
			String ticket=Navigation.getInstance(request).getUserTicket();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleElenco.getCodBibl();
			String codDoc=eleElenco.getCodDocumento();
			String statoSuggDoc=eleElenco.getStatoSuggerimentoDocumento();
			StrutturaTerna ute=new StrutturaTerna("","","");
			StrutturaCombo tit=new StrutturaCombo("","");
			String dataDa="";
			String dataA="";
			String edi="";
			String luogo="";
			StrutturaCombo pae=new StrutturaCombo("","");
			StrutturaCombo lin=new StrutturaCombo("","");
			String annoEdi="";
			String chiama=null;
			String ordina="";

			eleRicerca=new ListaSuppDocumentoVO( codP,  codB,  codDoc,  statoSuggDoc,  ute,  tit,  dataDa,  dataA,   edi,  luogo,  pae,  lin,  annoEdi,    chiama,  ordina );
			if (sinDoc.getOrdinamentoScelto()!=null && sinDoc.getOrdinamentoScelto().trim().length()>0 )
			{
				eleRicerca.setOrdinamento(sinDoc.getOrdinamentoScelto());
			}

			eleRicerca.setTicket(ticket);

			ricerca.add(eleRicerca);
			request.getSession().setAttribute("criteriRicercaDocumento", ricerca);
		} catch (Exception e) {
		}
	}

	private List<DocumentoVO> getListaDocumentiVO(ListaSuppDocumentoVO criRicerca) throws Exception
	{
	List<DocumentoVO> listaDocumenti=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaDocumenti = factory.getGestioneAcquisizioni().getRicercaListaDocumenti(criRicerca);
	return listaDocumenti;
	}

	private void loadStatoSuggerimento(SinteticaDocForm sinDoc) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		sinDoc.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento()));
	}


	/*	private void loadDocumenti() throws Exception {
	List lista = new ArrayList();

//String codP, String codB, String codDoc, String statoSuggDoc, StrutturaTerna ute, StrutturaCombo tit, String dataI, String dataA, String aut, String edi, String luogo, String pae, String lin, String annoEdi, String noteDoc, String msgXLet)
	DocumentoVO doc=new DocumentoVO("X10", "01", "4", "A", new StrutturaTerna("01","05","Vincenzo Bianchi"),new StrutturaCombo("LO10423457","Da Cartesio a Kant / E. Paolo Lamanna. - Firenze : Le Monnier, c1961. - 565 p. ; 23 cm."),  "03/05/2004", "", "","","","","","","","");
	lista.add(doc);
	doc=new DocumentoVO("X10", "01", "62", "A", new StrutturaTerna("02","07","Mario Rossi"),new StrutturaCombo("X110006911","Novecento : un monologo / Alessandro Baricco. - 18. ed. - Milano : Feltrinelli,"),  "23/02/2006", "", "","","","","","","","");
	lista.add(doc);
	doc=new DocumentoVO("X10", "01", "63", "A", new StrutturaTerna("03","01","Aldo Verdi"),new StrutturaCombo("X110007166","La vita infinita"),  "23/02/2006", "", "","","","","","","","");
	lista.add(doc);
	sinDoc.setListaDocumenti(lista);
}


private void loadStatoSuggerimento() throws Exception {
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

	sinDoc.setListaStatoSuggerimento(lista);
}
*/


}
