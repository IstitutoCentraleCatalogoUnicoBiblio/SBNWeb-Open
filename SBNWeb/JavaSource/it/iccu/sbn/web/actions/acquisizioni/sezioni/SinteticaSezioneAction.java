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
package it.iccu.sbn.web.actions.acquisizioni.sezioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.SinteticaSezioneForm;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
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

public class SinteticaSezioneAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {
	//private SinteticaSezioneForm sinSezione;
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		//map.put("ricerca.button.caricaBlocco","carica");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.crea","crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "Ok");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar()  )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("sezioniSelected")!= null && !request.getSession().getAttribute("sezioniSelected").equals(""))
				{
					sinSezione.setSelectedSezioni((String[]) request.getSession().getAttribute("sezioniSelected"));
				}

				return mapping.getInputForward();
			}
			if(!sinSezione.isSessione())
			{
				sinSezione.setSessione(true);
			}

			// ricevo i criteri di ricerca
			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricArr!=null)
			{
				sinSezione.setOrdinamentoScelto(ricArr.getOrdinamento());
			}

			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				sinSezione.setVisibilitaIndietroLS(true);
				// per il layout
				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					sinSezione.setLSRicerca(true); // fai rox 4
				//}
			}
			if (ricArr==null)
			{
				// l'attributo di sessione deve essere valorizzato
				sinSezione.setRisultatiPresenti(false);
			}
			List<SezioneVO> listaSezioni=new ArrayList();
			// deve essere escluso il caso di richiamo di lista supporto sezioni
/*			if 	(request.getSession().getAttribute("listaSezioniEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaSezioni=(List<SezioneVO>)request.getSession().getAttribute("listaSezioniEmessa");
			}
			else
			{
				listaSezioni=this.getListaSezioniVO(ricArr ); // va in errore se non ha risultati
			}
*/

			if (ricArr!=null)
			{
				listaSezioni=this.getListaSezioniVO(ricArr, request ); // va in errore se non ha risultati
			}

			sinSezione.setListaSezioni(listaSezioni);
			sinSezione.setNumSezioni(sinSezione.getListaSezioni().size());

			// gestione automatismo check su unico elemento lista
			if (sinSezione.getListaSezioni().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=sinSezione.getListaSezioni().get(0).getChiave();
				//	"FI|2007|3";
				sinSezione.setSelectedSezioni(appoSelProva);
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
			if 	(request.getSession().getAttribute("ultimoBloccoSezioni")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoSezioni");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{
				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaSezioni,maxElementiBlocco);
				sinSezione.setListaSezioni(blocco1.getLista());
			}

			if (blocco1 != null)
			{
			//if (blocco1 == null)
			//abilito i tasti per il blocco se necessario
			//memorizzo le informazioni per la gestione blocchi
			sinSezione.setIdLista(blocco1.getIdLista()); //si
			sinSezione.setTotRighe(blocco1.getTotRighe()); //no
			sinSezione.setTotBlocchi(blocco1.getTotBlocchi()); //no
			sinSezione.setMaxRighe(blocco1.getMaxRighe()); //no
			//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
			sinSezione.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
			//sinOfferte.setNumRighe(2);
			//sinOfferte.setNumBlocco(1);
			sinSezione.setUltimoBlocco(blocco1);
			sinSezione.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi

			// non trovati
			if (sinSezione.getNumSezioni()==0)
			{
				sinSezione.setRisultatiPresenti(false);
				return mapping.getInputForward();
			}
			else {
				// imposto con la var impostata su ricerca
/*				if (request.getAttribute("numElexBlocchi")!=null)
				{
					sinSezione.setNumRighe((Integer)request.getAttribute("numElexBlocchi"));
				}
				// controllare che numrighe non sia superiore alla dimensione dell'arraylist listabilanci
				int totPagine=(int) Math.round((sinSezione.getNumSezioni()/sinSezione.getNumRighe()));
				int resto=(int) sinSezione.getNumSezioni()%sinSezione.getNumRighe();
				if (resto > 0)
				{
					totPagine=totPagine+1;
				}
				sinSezione.setTotPagine(totPagine);

				if (sinSezione.getNumRighe()>sinSezione.getListaSezioni().size())
				{
					sinSezione.setNumRighe(sinSezione.getListaSezioni().size());
				}
				// inizializzazione dei blocchi di visualizzazione e caricamento array di visualizzazione
				if (sinSezione.getSezioniVisualizzate()==null )
				{
					sinSezione.setSezioniVisualizzate(new ArrayList<SezioneVO>());
					int start=0;
					int end=sinSezione.getNumRighe();
					for (int j=0;  j < end; j++)
					{
						SezioneVO ele=sinSezione.getListaSezioni().get(j);
						sinSezione.getSezioniVisualizzate().add(ele);
					}
					// inizializzazione blocchi
					int totblck=sinSezione.getTotPagine();
					GestioneBlocchiVO[] seqBlock=new GestioneBlocchiVO[totblck];
		            for (int i=0; i<seqBlock.length; i++) {
		            	seqBlock[i]=new GestioneBlocchiVO();
		            	if (i==0)
		            	{
			            	seqBlock[i].setInizio(0);
			            	seqBlock[i].setCaricato(true);
		            	}
		            	else
		            	{
			            	seqBlock[i].setInizio(seqBlock[i-1].getFine());
		            	}
		            	if (seqBlock[i].getInizio()+sinSezione.getNumRighe()<=sinSezione.getListaSezioni().size())
		            	{
			            	seqBlock[i].setFine(seqBlock[i].getInizio()+sinSezione.getNumRighe());
		            	}
		            	else
		            	{
			            	seqBlock[i].setFine(sinSezione.getListaSezioni().size());
		            	}

		            }
		            sinSezione.setSequenzablocchi(seqBlock);
					// fine inizializzazione blocchi
				}
				// posizionamento blocco successivo
				int blckSucc=sinSezione.getNumPagina();
				if (sinSezione.getSequenzablocchi().length>0)
				{
					for (int x=blckSucc; x<sinSezione.getSequenzablocchi().length; x++)
					{
						if (!sinSezione.getSequenzablocchi()[x].isCaricato())
						{
							blckSucc=x+1;
							break;
						}
					}

				}
				sinSezione.setNumPagina(blckSucc);
*/				// impostazione attributo sessione dei selezionati
				if 	(request.getSession().getAttribute("sezioniSelected")!= null)
				{
					sinSezione.setSelectedSezioni((String[]) request.getSession().getAttribute("sezioniSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricArr.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave().trim();
						for (int v=0;  v < sinSezione.getListaSezioni().size(); v++)
						{
							String eleList= sinSezione.getListaSezioni().get(v).getChiave().trim();
							if (eleList.equals(eleSele))
							{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									sinSezione.getListaSezioni().get(v).setTipoVariazione(variato);
								}
								break;
							}
						}
					}
				}
				//ordinamento
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
					sinSezione.setRisultatiPresenti(false);
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

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sinSezione.isSessione()) {
			sinSezione.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinSezione.getBloccoSelezionato();
		String idLista = sinSezione.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinSezione.getListaSezioni().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoSezioni",bloccoSucc);
				sinSezione.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoSezioni",sinSezione.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}


	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
			if (sinSezione.getProgrForm() > 0) {
			try {
					this.PreparaRicercaSezioneSingle( sinSezione, request,sinSezione.getProgrForm()-1);
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


	private List<SezioneVO> getListaSezioniVO(ListaSuppSezioneVO criRicerca, HttpServletRequest request) throws Exception
	{
	List<SezioneVO> listaSezioni;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	criRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Documento Fisico 09/05/08
	listaSezioni = factory.getGestioneAcquisizioni().getRicercaListaSezioni(criRicerca);
	// prova hibernate
	//listaSezioni = (List<SezioneVO>) factory.getGestioneAcquisizioni().getRicercaListaSezioniHib(criRicerca);

	return listaSezioni;
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
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
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);

			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=sinSezione.getSelectedSezioni();
						appoParametro=(String[])request.getSession().getAttribute("sezioniSelected");

						//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
						//{
						//	this.AggiornaParametriSintetica(request);
							request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, this.AggiornaRisultatiListaSupporto( sinSezione, ricArr));
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
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		try {
			String[] appoParametro=new String[0];
			String[] appoSelezione=new String[0];
			appoSelezione=sinSezione.getSelectedSezioni();
/*			appoParametro=(String[])request.getSession().getAttribute("sezioniSelected");
			// vedere se appoparametro è null
			if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			{
				this.AggiornaParametriSintetica(request);
			}
			appoSelezione=(String[]) sinSezione.getSelectedSezioni();
			appoParametro=(String[])request.getSession().getAttribute("sezioniSelected");
*/
			//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			if (appoSelezione!=null && appoSelezione.length!=0)
			{
				//this.AggiornaParametriSintetica(request);
				this.PreparaRicercaSezione( sinSezione, request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, this.AggiornaRisultatiListaSupporto( sinSezione,  (ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)));
				request.getSession().setAttribute("sezioniSelected", appoSelezione);
				request.getSession().setAttribute("listaSezioniEmessa", sinSezione.getListaSezioni());
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

/*	public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		try {
			sinSezione.setNumSezioni(sinSezione.getListaSezioni().size());
			int totPagine= (int)Math.round((sinSezione.getNumSezioni()/sinSezione.getNumRighe()));
			// resto della divisione
			int resto=(int) sinSezione.getNumSezioni()%sinSezione.getNumRighe();
			if (resto > 0)
			{
				totPagine=totPagine+1;
			}
			sinSezione.setTotPagine(totPagine);
			// la posizione dell'elemento nella lista si conteggia da zero per cui non aggiungo una unità
			sinSezione.setPosElemento((sinSezione.getNumRighe())*(sinSezione.getNumPagina()-1));

			// occorre controllare che non sia già stato caricato
			if (sinSezione.getNumPagina()>0 && sinSezione.getNumPagina()<=sinSezione.getTotPagine())
			{
				if (!sinSezione.getSequenzablocchi()[sinSezione.getNumPagina()-1].isCaricato())
				{
					int start=sinSezione.getSequenzablocchi()[sinSezione.getNumPagina()-1].getInizio();
					int end=sinSezione.getSequenzablocchi()[sinSezione.getNumPagina()-1].getFine();
					for (int j=start;  j < end; j++)
					{
						SezioneVO ele=sinSezione.getListaSezioni().get(j);
						sinSezione.getSezioniVisualizzate().add(ele);
					}
					sinSezione.getSequenzablocchi()[sinSezione.getNumPagina()-1].setCaricato(true);
					int blckSucc=sinSezione.getNumPagina();
					if (sinSezione.getSequenzablocchi().length>0)
					{
						for (int x=blckSucc; x<sinSezione.getSequenzablocchi().length; x++)
						{
							if (!sinSezione.getSequenzablocchi()[x].isCaricato())
							{
								blckSucc=x+1;
								break;
							}
						}
					}
					sinSezione.setNumPagina(blckSucc);
				}
				else
				{
					// blocco già caricato
				}

			}
			this.AggiornaParametriSintetica(request);
			// errore
			if (Integer.valueOf(sinSezione.getNumPagina())>totPagine)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.errorePagina"));
				this.saveErrors(request, errors);
				// reimposto la paginazione a quella precedente all'errore
				//sinSezione.setNumRighe(sinSezione.getNumRigheOld());
				//sinSezione.setPosElemento(sinSezione.getPosElementoOld());
				//sinSezione.setNumPagina(sinSezione.getNumPaginaOld());
				//return mapping.getInputForward();

			}

			//sinSezione.setNumRigheOld(sinSezione.getNumRighe());
			//sinSezione.setPosElementoOld(sinSezione.getPosElemento());
			sinSezione.setSezioniVisualizzateOld(sinSezione.getSezioniVisualizzate());

			return mapping.getInputForward();
		} catch (Exception e) {
			e.printStackTrace();

			return mapping.getInputForward();
		}
	}	*/

	/**
	 * SinteticaSezioneAction.java
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

		appoSelezione=(String[]) sinSezione.getSelectedSezioni();
		appoParametro=(String[])request.getSession().getAttribute("sezioniSelected");
		//int nRighe=sinSezione.getNumRigheOld();
		//int nPos=sinSezione.getPosElementoOld();
		//List <SezioneVO> listaOld=sinSezione.getSezioniVisualizzateOld();
		List <SezioneVO>  listaOld=new ArrayList();
		if (sinSezione.getSezioniVisualizzateOld()!=null && sinSezione.getSezioniVisualizzateOld().size()!=0 )
		{
			listaOld=sinSezione.getSezioniVisualizzateOld();
		}
		else
		{
			listaOld=sinSezione.getSezioniVisualizzate();
		}

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			//sinSezione.getListaSezioni()
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione, listaOld);
			request.getSession().setAttribute("sezioniSelected", (String[])nuovoParametro);
			sinSezione.setSelectedSezioni((String[]) nuovoParametro );
		}

	}*/
	/**
	 * SinteticaSezioneAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<SezioneVO> lista)
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
				SezioneVO sez= (SezioneVO) lista.get(x);
				cod=sez.getChiave().trim();
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
	}	*/


	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		String [] appoLista= new String [sinSezione.getListaSezioni().size()];
		int i;
		for (i=0;  i < sinSezione.getListaSezioni().size(); i++)
		{
			SezioneVO sez= sinSezione.getListaSezioni().get(i);
			String cod=sez.getChiave().trim();
			appoLista[i]=cod;
		}
		sinSezione.setSelectedSezioni(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSezioneForm sinSezione = (SinteticaSezioneForm) form;
		try {
			sinSezione.setSelectedSezioni(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	/**
	 * SinteticaSezioneAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppSezioneVO AggiornaRisultatiListaSupporto (SinteticaSezioneForm sinSezione, ListaSuppSezioneVO eleRicArr)
	{
		try {

			List<SezioneVO> risultati=new ArrayList();
			SezioneVO eleSezione=new SezioneVO();
			String [] listaSelezionati=sinSezione.getSelectedSezioni();
			String codP;
			String codB;
			String codSez;
			String desSez;
			double sommaSez;
			String noteSez;
			String annoValSez;
			double bdgSez;
			String tipoVar;
			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinSezione.getListaSezioni().size(); j++)
				{
					SezioneVO eleElenco=sinSezione.getListaSezioni().get(j);
					if (eleSel.equals(eleElenco.getChiave().trim()))
					{
						codP="";
						codB=eleElenco.getCodBibl();
						codSez=eleElenco.getCodiceSezione();
						desSez=eleElenco.getDescrizioneSezione();
						sommaSez=eleElenco.getSommaDispSezione();
						noteSez=eleElenco.getNoteSezione();
						annoValSez=eleElenco.getAnnoValiditaSezione();
						bdgSez=eleElenco.getBudgetSezione();
						tipoVar="";

						eleSezione=new SezioneVO(codP,  codB,  codSez,  desSez ,sommaSez, noteSez, annoValSez, bdgSez,tipoVar );
						eleSezione.setChiusa(eleElenco.isChiusa());
						risultati.add(eleSezione);
					}
				}
			}
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}

	/**
	 * SinteticaSezioneAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaSezione(SinteticaSezioneForm sinSezione, HttpServletRequest request)
	{
		try {
			List<ListaSuppSezioneVO> ricerca=new ArrayList();
			ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
			String [] listaSelezionati=sinSezione.getSelectedSezioni();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinSezione.getListaSezioni().size(); j++)
				{
					SezioneVO eleElenco=sinSezione.getListaSezioni().get(j);
					String chiaveComposta=eleElenco.getChiave().trim();
					//chiaveComposta[3] codOrdine
					//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{
						// carica i criteri di ricerca da passare alla esamina

						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=eleElenco.getCodBibl();
						String codSez=eleElenco.getCodiceSezione();
						String desSez=eleElenco.getDescrizioneSezione();
						String chiama=null;
						String ordina="";
						eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
						if (eleElenco.isChiusa())
						{
							eleRicerca.setChiusura(true);
						}
						if (sinSezione.getOrdinamentoScelto()!=null && sinSezione.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sinSezione.getOrdinamentoScelto());
						}

						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaSezione", ricerca);
		} catch (Exception e) {
		}
	}

	private void  PreparaRicercaSezioneSingle(SinteticaSezioneForm sinSezione, HttpServletRequest request, int j)
	{
		try {
			List<ListaSuppSezioneVO> ricerca=new ArrayList();
			ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
			SezioneVO eleElenco=sinSezione.getListaSezioni().get(j);
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleElenco.getCodBibl();
			String codSez=eleElenco.getCodiceSezione();
			String desSez=eleElenco.getDescrizioneSezione();
			String chiama=null;
			String ordina="";

			eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
			if (sinSezione.getOrdinamentoScelto()!=null && sinSezione.getOrdinamentoScelto().trim().length()>0 )
			{
				eleRicerca.setOrdinamento(sinSezione.getOrdinamentoScelto());
			}
			if (eleElenco.isChiusa())
			{
				eleRicerca.setChiusura(true);
			}
			ricerca.add(eleRicerca);
			request.getSession().setAttribute("criteriRicercaSezione", ricerca);
		} catch (Exception e) {
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_SEZIONE_ACQUISIZIONI, utente.getCodPolo(), utente.getCodBib(), null);
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
