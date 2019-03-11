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
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.gare.SinteticaGaraForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaGaraAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{
	//private SinteticaGaraForm sinGare;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
/*		map.put("ricerca.button.caricaBlocco","carica");
*/		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.crea","crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "Ok");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaGaraForm sinGare = (SinteticaGaraForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar()  )
			{

				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("gareSelected")!= null && !request.getSession().getAttribute("gareSelected").equals(""))
				{
					sinGare.setSelectedRichieste((String[]) request.getSession().getAttribute("gareSelected"));
				}

				return mapping.getInputForward();
			}
				if(!sinGare.isSessione())
				{
					sinGare.setSessione(true);
				}
				ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
				if (ricArr!=null)
				{
					sinGare.setOrdinamentoScelto(ricArr.getOrdinamento());
				}

				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					if (!ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
					{
						sinGare.setVisibilitaIndietroLS(true);
					}

					// per il layout
					// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

					//if (ricArr.getChiamante().endsWith("RicercaParziale"))
					//{
					sinGare.setLSRicerca(true); // fai rox 4


					//}

				}
				if (ricArr==null)
				{
					// l'attributo di sessione deve essere valorizzato
					sinGare.setRisultatiPresenti(false);
				}
				List<GaraVO> listaGare=new ArrayList();
				// deve essere escluso il caso di richiamo di lista supporto cambi
/*				if 	(request.getSession().getAttribute("listaGareEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
				{
					listaGare=(List<GaraVO>)request.getSession().getAttribute("listaGareEmessa");
				}
				else
				{
					listaGare=this.getListaGareVO(ricArr); // va in errore se non ha risultati
				}
*/
				if (ricArr!=null)
				{
					listaGare=this.getListaGareVO(ricArr); // va in errore se non ha risultati
				}

				sinGare.setListaRichiesteOfferta(listaGare);
				sinGare.setNumRichieste(sinGare.getListaRichiesteOfferta().size());

				// gestione automatismo check su unico elemento lista
				if (sinGare.getListaRichiesteOfferta().size()==1)
				{
					String [] appoSelProva= new String [1];
					appoSelProva[0]=sinGare.getListaRichiesteOfferta().get(0).getChiave().trim();
					//	"FI|2007|3";
					sinGare.setSelectedRichieste(appoSelProva);
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
				if 	(request.getSession().getAttribute("ultimoBloccoGare")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
				{
					blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoGare");
					//n.b la lista è quella memorizzata nella variabile di sessione
				}
				else
				{

					blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaGare,maxElementiBlocco);
					sinGare.setListaRichiesteOfferta(blocco1.getLista());
				}

				if (blocco1 != null)
				{
				//if (blocco1 == null)
				//abilito i tasti per il blocco se necessario
				//memorizzo le informazioni per la gestione blocchi
				sinGare.setIdLista(blocco1.getIdLista()); //si
				sinGare.setTotRighe(blocco1.getTotRighe()); //no
				sinGare.setTotBlocchi(blocco1.getTotBlocchi()); //no
				sinGare.setMaxRighe(blocco1.getMaxRighe()); //no
				//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
				sinGare.setBloccoSelezionato(blocco1.getNumBlocco()); //si
				//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
				//sinOfferte.setNumRighe(2);
				//sinOfferte.setNumBlocco(1);
				sinGare.setUltimoBlocco(blocco1);
				sinGare.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
				}
				// fine gestione blocchi



				// non trovati
				if (sinGare.getNumRichieste()==0)
				{
					sinGare.setRisultatiPresenti(false);
					return mapping.getInputForward();
				}
				else {
					// impostazione attributo sessione dei selezionati
					if 	(request.getSession().getAttribute("gareSelected")!= null)
					{
						sinGare.setSelectedRichieste((String[]) request.getSession().getAttribute("gareSelected"));
					}
					//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
					if (ricArr.getSelezioniChiamato()!=null)
					{
						for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
						{
							String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave().trim();
							for (int v=0;  v < sinGare.getListaRichiesteOfferta().size(); v++)
							{
								String eleList= sinGare.getListaRichiesteOfferta().get(v).getChiave().trim();
								if (eleList.equals(eleSele))
								{
									String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
									if (variato!=null && variato.length()!=0)
									{
										sinGare.getListaRichiesteOfferta().get(v).setTipoVariazione(variato);
									}
									break;
								}
							}
						}
					}
					//ordinamento

				}
				// ordinamento per titolo
/*				if (sinGare.getOrdinamentoScelto().equals("1"))
				{
					List<GaraVO> provaElencoOrdinato=(List<GaraVO>) this.ElencaPer( sinGare.getListaRichiesteOfferta(), sinGare,"tit");
					sinGare.setListaRichiesteOfferta(provaElencoOrdinato);
				}*/
				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
					// assenzaRisultati = 4001;
					if (ve.getError()==4001)
					{
						sinGare.setRisultatiPresenti(false);
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


	public List<GaraVO> ElencaPer(List<GaraVO> lst,SinteticaGaraForm sinGare,String sortBy ) throws EJBException {
		//List<OrdiniVO> lst = sintordini.getListaOrdini();
		Comparator comp=null;
		if (sortBy==null) {
			comp =new TitComparator();
			}
			else if (sortBy.equals("tit")) {
			comp =new TitComparator();
			}
		if (lst != null)
		{
			if (comp != null)
			{
				Collections.sort(lst, comp);
			}
		}
		return lst;
	}


	private static class TitComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((GaraVO) o1).getBid().getDescrizione().toLowerCase();  //
				String e2 = ((GaraVO) o2).getBid().getDescrizione().toLowerCase();
				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}



	private List<GaraVO> getListaGareVO(ListaSuppGaraVO criRicerca) throws Exception
	{
	List<GaraVO> listaGare=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaGare = factory.getGestioneAcquisizioni().getRicercaListaGare(criRicerca);
	return listaGare;
	}


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
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
	SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");

		if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					// carico i risultati della selezione nella variabile da restituire
					String[] appoParametro=new String[0];
					String[] appoSelezione=new String[0];
					appoSelezione=sinGare.getSelectedRichieste();
					appoParametro=(String[])request.getSession().getAttribute("gareSelected");

					//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
					//{
					//	this.AggiornaParametriSintetica(request);
						request.getSession().setAttribute("attributeListaSuppGaraVO", this.AggiornaRisultatiListaSupporto( sinGare, ricArr));
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
	SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
	try {
		String[] appoParametro=new String[0];
		String[] appoSelezione=new String[0];
		appoSelezione=sinGare.getSelectedRichieste();
		appoParametro=(String[])request.getSession().getAttribute("gareSelected");
		// vedere se appoparametro è null
/*		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			this.AggiornaParametriSintetica(request);
		}
		appoSelezione=(String[]) sinGare.getSelectedRichieste();
		appoParametro=(String[])request.getSession().getAttribute("gareSelected");
*/
//		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		if (appoSelezione!=null && appoSelezione.length!=0)
		{
			//this.AggiornaParametriSintetica(request);
			this.PreparaRicercaGara( sinGare, request);
			// si aggiorna l'attributo con l'elenco dei cambi trovati
			request.getSession().setAttribute("attributeListaSuppGaraVO", this.AggiornaRisultatiListaSupporto( sinGare,  (ListaSuppGaraVO)request.getSession().getAttribute("attributeListaSuppGaraVO")));
			request.getSession().setAttribute("gareSelected", appoSelezione);
			request.getSession().setAttribute("listaGareEmessa", sinGare.getListaRichiesteOfferta());

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

public ActionForward Ok(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
		if (sinGare.getProgrForm() > 0) {
			try {
					this.PreparaRicercaGaraSingle( sinGare, request,sinGare.getProgrForm()-1);
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
	SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
	if (!isTokenValid(request)) {
		saveToken(request);
	}
	if (!sinGare.isSessione()) {
		sinGare.setSessione(true);
	}
	int numBlocco =0;
	numBlocco = sinGare.getBloccoSelezionato();
	String idLista = sinGare.getIdLista();
	String ticket = Navigation.getInstance(request).getUserTicket();
	// && numBlocco <=sinOfferte.getTotBlocchi()
	if ( numBlocco > 1  && idLista != null) {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
		//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
		// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

		//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
		if (bloccoSucc != null) {
			sinGare.getListaRichiesteOfferta().addAll(bloccoSucc.getLista());
//			if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//				this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//			// ho caricato tutte le righe sulla form
//			if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//				eleutenti.setAbilitaBlocchi(false);
			request.getSession().setAttribute("ultimoBloccoGare",bloccoSucc);
			sinGare.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
		}
		else
		{
			request.getSession().setAttribute("ultimoBloccoGare",sinGare.getUltimoBlocco());
		}
	}
	return mapping.getInputForward();
}


/*public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	sinCom = (SinteticaComForm) form;
	try {
		this.sinCom.setNumSezioni(sinCom.getListaSezioni().size());
		int totPagine= (int)Math.round((sinCom.getNumSezioni()/sinCom.getNumRighe()));
		// resto della divisione
		int resto=(int) sinCom.getNumSezioni()%sinCom.getNumRighe();
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
					SezioneVO ele=this.sinSezione.getListaSezioni().get(j);
					this.sinSezione.getSezioniVisualizzate().add(ele);
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
		if (Integer.valueOf(this.sinSezione.getNumPagina())>totPagine)
		{
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.acquisizioni.errorePagina"));
			this.saveErrors(request, errors);
			// reimposto la paginazione a quella precedente all'errore
			//this.sinSezione.setNumRighe(this.sinSezione.getNumRigheOld());
			//this.sinSezione.setPosElemento(this.sinSezione.getPosElementoOld());
			//this.sinSezione.setNumPagina(this.sinSezione.getNumPaginaOld());
			//return mapping.getInputForward();

		}

		//this.sinSezione.setNumRigheOld(sinSezione.getNumRighe());
		//this.sinSezione.setPosElementoOld(sinSezione.getPosElemento());
		this.sinSezione.setSezioniVisualizzateOld(this.sinSezione.getSezioniVisualizzate());

		return mapping.getInputForward();
	} catch (Exception e) {
		e.printStackTrace();

		return mapping.getInputForward();
	}
}	*/

/**
 * SinteticaGaraAction.java
 * @param request
 * Questo metodo provvede a seguito di un submit al recupero dell'oggetto di form che contiene i codici degli elementi di lista selezionati nella pagina attiva
 * e dell'oggetto di sessione che riporta la sintesi di tutti i codici degli oggetti precedentemente selezionati nelle diverse pagine
 * Inoltre memorizza il range di righe e la posizione dell'ultima pagina visitata per consentire la paginazione
 * Dopo avere effettuato il merge delle informazioni delle ultime selezioni/deselezioni e delle precedenti memorizzate
 * ripulisce ed aggiorna l'oggetto di sessione con tutti i codici degli oggetti selezionati
 */
/*private void AggiornaParametriSintetica(HttpServletRequest request)
{

	String[] appoParametro=new String[0];
	String[] appoSelezione=new String[0];
	String[] nuovoParametro=new String[0];

	appoSelezione=(String[]) sinGare.getSelectedRichieste();
	appoParametro=(String[])request.getSession().getAttribute("gareSelected");
	//int nRighe=this.sinSezione.getNumRigheOld();
	//int nPos=this.sinSezione.getPosElementoOld();
	//List <SezioneVO> listaOld=this.sinSezione.getSezioniVisualizzateOld();
	List <GaraVO>  listaOld=new ArrayList();

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
		request.getSession().setAttribute("gareSelected", (String[])nuovoParametro);
		sinGare.setSelectedRichieste((String[]) nuovoParametro );
	}

}*/
/**
 * SinteticaGaraAction.java
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
/*private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<GaraVO> lista)
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
			GaraVO gar= (GaraVO) lista.get(x);
			cod=gar.getChiave().trim();
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
	SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
	String [] appoLista= new String [sinGare.getListaRichiesteOfferta().size()];
	int i;
	for (i=0;  i < sinGare.getListaRichiesteOfferta().size(); i++)
	{
		GaraVO gar= sinGare.getListaRichiesteOfferta().get(i);
		String cod=gar.getChiave().trim();
		appoLista[i]=cod;
	}
	sinGare.setSelectedRichieste(appoLista);
	return mapping.getInputForward();
}

public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
	try {
		sinGare.setSelectedRichieste(null);
		return mapping.getInputForward();
	} catch (Exception e) {
		return mapping.getInputForward();
	}
}

/**
 * SinteticaGaraAction.java
 * @param eleRicArr
 * @return
 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
 */
private ListaSuppGaraVO AggiornaRisultatiListaSupporto (SinteticaGaraForm sinGare, ListaSuppGaraVO eleRicArr)
{
	try {

		List<GaraVO> risultati=new ArrayList();
		GaraVO eleCom=new GaraVO();
		String [] listaSelezionati=sinGare.getSelectedRichieste();

		String codP;
		String codB;
		StrutturaCombo idtitolo;
		String codRich;
		String dataRich;
		String statoRich;
		double prezGara;
		int copieRich;
		String noteOrd;

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sinGare.getListaRichiesteOfferta().size(); j++)
			{
				GaraVO eleElenco=sinGare.getListaRichiesteOfferta().get(j);
				if (eleSel.equals(eleElenco.getChiave().trim()))
				{
					codP="";
					codB=eleElenco.getCodBibl();
					idtitolo=eleElenco.getBid();
					codRich=eleElenco.getCodRicOfferta();
					dataRich=eleElenco.getDataRicOfferta();
					statoRich=eleElenco.getStatoRicOfferta();
					prezGara=eleElenco.getPrezzoIndGara();
					copieRich=eleElenco.getNumCopieRicAcq();
					noteOrd=eleElenco.getNoteOrdine();
					eleCom=new GaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  prezGara,  copieRich,  noteOrd );
					eleCom.setDettaglioPartecipantiGara(eleElenco.getDettaglioPartecipantiGara());
					eleCom.setNaturaBid(eleElenco.getNaturaBid());
					eleCom.setChiave();

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
 * SinteticaGaraAction.java
 * @param request
 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
 */
private void  PreparaRicercaGara(SinteticaGaraForm sinGare, HttpServletRequest request)
{

	try {
		List<ListaSuppGaraVO> ricerca=new ArrayList();
		ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO();
		String [] listaSelezionati=sinGare.getSelectedRichieste();
		request.getSession().removeAttribute("criteriRicercaGara");

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sinGare.getListaRichiesteOfferta().size(); j++)
			{
				GaraVO eleElenco=sinGare.getListaRichiesteOfferta().get(j);
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
					String ticket=Navigation.getInstance(request).getUserTicket();
					StrutturaCombo idtitolo=new StrutturaCombo("","");
					idtitolo.setCodice(eleElenco.getBid().getCodice());
					idtitolo.setDescrizione(eleElenco.getBid().getDescrizione());
					String codRich=eleElenco.getCodRicOfferta();
					String dataRich=eleElenco.getDataRicOfferta();
					String statoRich=eleElenco.getStatoRicOfferta();
					String chiama=null;
					String ordina="";
					eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina );
					eleRicerca.setTicket(ticket);
					if (sinGare.getOrdinamentoScelto()!=null && sinGare.getOrdinamentoScelto().trim().length()>0 )
					{
						eleRicerca.setOrdinamento(sinGare.getOrdinamentoScelto());
					}

					ricerca.add(eleRicerca);
				}
			}
		}
		request.getSession().setAttribute("criteriRicercaGara", ricerca);
	} catch (Exception e) {
	}
}

private void  PreparaRicercaGaraSingle(SinteticaGaraForm sinGare, HttpServletRequest request, int j)
{

	try {
		List<ListaSuppGaraVO> ricerca=new ArrayList();
		ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO();
		GaraVO eleElenco=sinGare.getListaRichiesteOfferta().get(j);
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=eleElenco.getCodBibl();
		StrutturaCombo idtitolo=new StrutturaCombo("","");
		idtitolo.setCodice(eleElenco.getBid().getCodice());
		idtitolo.setDescrizione(eleElenco.getBid().getDescrizione());
		String codRich=eleElenco.getCodRicOfferta();
		String dataRich=eleElenco.getDataRicOfferta();
		String statoRich=eleElenco.getStatoRicOfferta();
		String chiama=null;
		String ordina="";
		eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina );
		if (sinGare.getOrdinamentoScelto()!=null && sinGare.getOrdinamentoScelto().trim().length()>0 )
		{
			eleRicerca.setOrdinamento(sinGare.getOrdinamentoScelto());
		}
		ricerca.add(eleRicerca);
		request.getSession().setAttribute("criteriRicercaGara", ricerca);
	} catch (Exception e) {
	}
}



/*	private void loadComunicazioni() throws Exception {
		List lista = new ArrayList();

		//	(String codP, String codB, String codMsg, String tipoDoc,String tipoMsg, StrutturaCombo forn, StrutturaTerna idDoc, String statoCom,String dataCom, String dirCom, String tipoInvioCom, String noteCom) throws Exception {
		ComunicazioneVO com=new ComunicazioneVO("X10", "01", "1", "O","04", new StrutturaCombo("13","Libreria Feltrinelli"),new StrutturaTerna("A","2003","1"), "2", "31/03/2004", "", "" , "");
		lista.add(com);
		com=new ComunicazioneVO("X10", "01", "11", "O","25", new StrutturaCombo("22","Libreria KAPPA"),new StrutturaTerna("A","2005","31"), "2", "30/03/2005", "", "" , "");
		lista.add(com);
		com=new ComunicazioneVO("X10", "01", "18", "O","25", new StrutturaCombo("90","Libreria Moderna"),new StrutturaTerna("A","2006","43"), "3", "27/03/2006", "", "" , "");
		lista.add(com);

		this.sinCom.setListaComunicazioni(lista);
	}*/


/*	private void loadRichieste() throws Exception {
		List lista = new ArrayList();

		GaraVO richOff= new GaraVO("X10", "01", new StrutturaCombo("BVEE021819", "Naturalis historia"),"00000001", "15/12/2004", "1", 100.00, 3, "");
		lista.add(richOff);
		richOff= new GaraVO("X10", "01",  new StrutturaCombo("LO10531230", "Roma antica/M. Beard...(et.al.); a cura di Andrea Giardina - Roma. Editori Laterza 2000"), "00000003", "21/12/2004", "1", 10.00, 100, ""  );
		lista.add(richOff);
		richOff= new GaraVO("X10", "01", new StrutturaCombo("LO10338841", "4: Arbo-asse. Istituto G. Treccani - Roma, 1929 - XVI, 999 p., 182 tav.: ill.; 32 cm."), "00000011", "06/02/2006", "1", 300.00, 2, "" );
		lista.add(richOff);
		sinGare.setListaRichiesteOfferta(lista);
	}*/

	private void loadStatiRichiestaOfferta(SinteticaGaraForm sinGare) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("1","1 - Aperta");
		lista.add(elem);
		elem = new StrutturaCombo("2","2 - Chiusa");
		lista.add(elem);
		elem = new StrutturaCombo("3","3 - Annullata");
		lista.add(elem);
		elem = new StrutturaCombo("4","4 - Ordinata");
		lista.add(elem);

		sinGare.setListaStatoRichiestaOfferta(lista);
	}

public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
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

/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		SinteticaGaraForm sinGare = (SinteticaGaraForm) form;
		this.loadRichieste();
		sinGare.setNumRichieste(sinGare.getListaRichiesteOfferta().size());
		this.loadStatiRichiestaOfferta();

		if (request.getParameter("indietro0") != null) {
			return mapping.findForward("indietro");
		}

		if (request.getParameter("esamina0") != null) {
			if (sinGare.getSelectedRichieste().length!=0)
			{
				return mapping.findForward("esamina");
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);

			}
		}

		if (request.getParameter("selTutti0") != null)
		{
			String [] appoLista= new String [sinGare.getListaRichiesteOfferta().size()];
			int i;
			for (i=0;  i < sinGare.getListaRichiesteOfferta().size(); i++)
			{
				GaraVO richOff= (GaraVO) sinGare.getListaRichiesteOfferta().get(i);
				String cod=richOff.getCodRicOfferta();
				appoLista[i]=cod;
			}
			sinGare.setSelectedRichieste(appoLista);
			return mapping.getInputForward();
		}

		if (request.getParameter("deselTutti0") != null) {
			sinGare.setSelectedRichieste(null);
			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}*/

}
