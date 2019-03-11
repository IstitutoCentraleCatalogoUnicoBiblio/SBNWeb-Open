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
package it.iccu.sbn.web.actions.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni.SinteticaComForm;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
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

public class SinteticaComAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker  {
	//private SinteticaComForm sinCom;

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
			SinteticaComForm sinCom = (SinteticaComForm) form;
			try {
				if (Navigation.getInstance(request).isFromBar() )
				{
					// gestione selezione check da  menu bar
					if 	(request.getSession().getAttribute("comunicazioniSelected")!= null && !request.getSession().getAttribute("comunicazioniSelected").equals(""))
					{
						sinCom.setSelectedComunicazioni((String[]) request.getSession().getAttribute("comunicazioniSelected"));
					}

					return mapping.getInputForward();
				}
				if(!sinCom.isSessione())
				{
					sinCom.setSessione(true);
				}
				ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
				if (ricArr!=null)
				{
					sinCom.setOrdinamentoScelto(ricArr.getOrdinamento());
				}
				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					sinCom.setVisibilitaIndietroLS(true);
					// per il layout
					// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

					//if (ricArr.getChiamante().endsWith("RicercaParziale"))
					//{
						sinCom.setLSRicerca(true); // fai rox 4
					//}

				}
				if (ricArr==null)
				{
					// l'attributo di sessione deve essere valorizzato
					sinCom.setRisultatiPresenti(false);
				}
				List<ComunicazioneVO> listaComunicazioni=new ArrayList();
				// deve essere escluso il caso di richiamo di lista supporto cambi
/*				if 	(request.getSession().getAttribute("listaComEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
				{
					listaComunicazioni=(List<ComunicazioneVO>)request.getSession().getAttribute("listaComEmessa");
				}
				else
				{
					listaComunicazioni=this.getListaComunicazioniVO(ricArr ); // va in errore se non ha risultati
				}
*/
				if (ricArr!=null)
				{
					listaComunicazioni=this.getListaComunicazioniVO(ricArr ); // va in errore se non ha risultati
				}

				sinCom.setListaComunicazioni(listaComunicazioni);
				sinCom.setNumComunicazioni(sinCom.getListaComunicazioni().size());

				// gestione automatismo check su unico elemento lista
				if (sinCom.getListaComunicazioni().size()==1)
				{
					String [] appoSelProva= new String [1];
					appoSelProva[0]=sinCom.getListaComunicazioni().get(0).getChiave();
					//	"FI|2007|3";
					sinCom.setSelectedComunicazioni(appoSelProva);
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
				if 	(request.getSession().getAttribute("ultimoBloccoCom")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
				{
					blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoCom");
					//n.b la lista è quella memorizzata nella variabile di sessione
				}
				else
				{

					blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaComunicazioni,maxElementiBlocco);
					sinCom.setListaComunicazioni(blocco1.getLista());
				}

				if (blocco1 != null)
				{
				//if (blocco1 == null)
				//abilito i tasti per il blocco se necessario
				//memorizzo le informazioni per la gestione blocchi
				sinCom.setIdLista(blocco1.getIdLista()); //si
				sinCom.setTotRighe(blocco1.getTotRighe()); //no
				sinCom.setTotBlocchi(blocco1.getTotBlocchi()); //no
				sinCom.setMaxRighe(blocco1.getMaxRighe()); //no
				//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
				sinCom.setBloccoSelezionato(blocco1.getNumBlocco()); //si
				//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
				//sinOfferte.setNumRighe(2);
				//sinOfferte.setNumBlocco(1);
				sinCom.setUltimoBlocco(blocco1);
				sinCom.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
				}
				// fine gestione blocchi


				// non trovati
				if (sinCom.getNumComunicazioni()==0)
				{
					sinCom.setRisultatiPresenti(false);
					return mapping.getInputForward();
				}
				else {
					// impostazione attributo sessione dei selezionati
					if 	(request.getSession().getAttribute("comunicazioniSelected")!= null)
					{
						sinCom.setSelectedComunicazioni((String[]) request.getSession().getAttribute("comunicazioniSelected"));
					}
					//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
					if (ricArr.getSelezioniChiamato()!=null)
					{
						for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
						{
							String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave().trim();
							for (int v=0;  v < sinCom.getListaComunicazioni().size(); v++)
							{
								String eleList= sinCom.getListaComunicazioni().get(v).getChiave().trim();
								if (eleList.equals(eleSele))
								{
									String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
									if (variato!=null && variato.length()!=0)
									{
										sinCom.getListaComunicazioni().get(v).setTipoVariazione(variato);
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
						sinCom.setRisultatiPresenti(false);
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

	private List<ComunicazioneVO> getListaComunicazioniVO(ListaSuppComunicazioneVO criRicerca) throws Exception
	{
	List<ComunicazioneVO> listaComunicazioni=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaComunicazioni = factory.getGestioneAcquisizioni().getRicercaListaComunicazioni(criRicerca);
	return listaComunicazioni;
	}



	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaComForm sinCom = (SinteticaComForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SinteticaComForm sinCom = (SinteticaComForm) form;
			if (sinCom.getProgrForm() > 0) {
				try {
						this.PreparaRicercaComunicazioneSingle( sinCom,request,sinCom.getProgrForm()-1);
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
		SinteticaComForm sinCom = (SinteticaComForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sinCom.isSessione()) {
			sinCom.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinCom.getBloccoSelezionato();
		String idLista = sinCom.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinCom.getListaComunicazioni().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoCom",bloccoSucc);
				sinCom.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoCom",sinCom.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}


public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaComForm sinCom = (SinteticaComForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
		if (ricArr!=null )
		{
			//almaviva5_201011223 periodici
			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_FASCICOLO))
				return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_FASCICOLO, true);

			if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_KARDEX))
				return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_KARDEX, true);

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
			//almaviva5_201011223 periodici
			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_FASCICOLO))
				return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_FASCICOLO, true);
			if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_KARDEX))
				return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_KARDEX, true);

			return mapping.findForward("indietro");
		}
	} catch (Exception e) {
		return mapping.getInputForward();
	}
}

public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaComForm sinCom = (SinteticaComForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");

		if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					// carico i risultati della selezione nella variabile da restituire
					String[] appoParametro=new String[0];
					String[] appoSelezione=new String[0];
					appoSelezione=sinCom.getSelectedComunicazioni();
					appoParametro=(String[])request.getSession().getAttribute("comunicazioniSelected");

					//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
					//{
						//this.AggiornaParametriSintetica(request);
						request.getSession().setAttribute("attributeListaSuppComunicazioneVO", this.AggiornaRisultatiListaSupporto( sinCom, ricArr));
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
	SinteticaComForm sinCom = (SinteticaComForm) form;
	try {
		String[] appoParametro=new String[0];
		String[] appoSelezione=new String[0];
		appoSelezione=sinCom.getSelectedComunicazioni();
/*		appoParametro=(String[])request.getSession().getAttribute("comunicazioniSelected");
		// vedere se appoparametro è null
		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			this.AggiornaParametriSintetica(request);
		}
		appoSelezione=(String[]) sinCom.getSelectedComunicazioni();
		appoParametro=(String[])request.getSession().getAttribute("comunicazioniSelected");*/

		//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		if (appoSelezione!=null && appoSelezione.length!=0)
		{
			//this.AggiornaParametriSintetica(request);
			this.PreparaRicercaComunicazione( sinCom,request);
			// si aggiorna l'attributo con l'elenco dei cambi trovati
			request.getSession().setAttribute("attributeListaSuppComunicazioneVO", this.AggiornaRisultatiListaSupporto( sinCom, (ListaSuppComunicazioneVO)request.getSession().getAttribute("attributeListaSuppComunicazioneVO")));
			request.getSession().setAttribute("comunicazioniSelected", appoSelezione);
			request.getSession().setAttribute("listaComEmessa", sinCom.getListaComunicazioni());
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

/*public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaComForm sinCom = (SinteticaComForm) form;
	try {
		sinCom.setNumSezioni(sinCom.getListaSezioni().size());
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
 * SinteticaComAction.java
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

	appoSelezione=(String[]) sinCom.getSelectedComunicazioni();
	appoParametro=(String[])request.getSession().getAttribute("comunicazioniSelected");
	//int nRighe=this.sinSezione.getNumRigheOld();
	//int nPos=this.sinSezione.getPosElementoOld();
	//List <SezioneVO> listaOld=this.sinSezione.getSezioniVisualizzateOld();
	List <ComunicazioneVO>  listaOld=new ArrayList();

	if (sinCom.getComunicazioniVisualizzateOld()!=null && sinCom.getComunicazioniVisualizzateOld().size()!=0 )
	{
		listaOld=sinCom.getComunicazioniVisualizzateOld();
	}
	else
	{
		listaOld=sinCom.getComunicazioniVisualizzate();
	}

	if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
	{
		//this.sinSezione.getListaSezioni()
		nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione, listaOld);
		request.getSession().setAttribute("comunicazioniSelected", (String[])nuovoParametro);
		sinCom.setSelectedComunicazioni((String[]) nuovoParametro );
	}

}*/
/**
 * SinteticaComAction.java
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
/*private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<ComunicazioneVO> lista)
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
			ComunicazioneVO com= (ComunicazioneVO) lista.get(x);
			cod=com.getChiave().trim();
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
	SinteticaComForm sinCom = (SinteticaComForm) form;
	String [] appoLista= new String [sinCom.getListaComunicazioni().size()];
	int i;
	for (i=0;  i < sinCom.getListaComunicazioni().size(); i++)
	{
		ComunicazioneVO sez= sinCom.getListaComunicazioni().get(i);
		String cod=sez.getChiave().trim();
		appoLista[i]=cod;
	}
	sinCom.setSelectedComunicazioni(appoLista);
	return mapping.getInputForward();
}

public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaComForm sinCom = (SinteticaComForm) form;
	try {
		sinCom.setSelectedComunicazioni(null);
		return mapping.getInputForward();
	} catch (Exception e) {
		return mapping.getInputForward();
	}
}

/**
 * SinteticaComAction.java
 * @param eleRicArr
 * @return
 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
 */
private ListaSuppComunicazioneVO AggiornaRisultatiListaSupporto (SinteticaComForm sinCom,ListaSuppComunicazioneVO eleRicArr)
{
	try {

		List<ComunicazioneVO> risultati=new ArrayList();
		ComunicazioneVO eleCom=new ComunicazioneVO();
		String [] listaSelezionati=sinCom.getSelectedComunicazioni();
		String codP;
		String codB;
		String codMsg;
		String tipoDoc;
		String tipoMsg;
		StrutturaCombo forn;
		StrutturaTerna idDoc;
		String statoCom;
		String dataCom;
		String dirCom;
		String tipoInvioCom;
		String noteCom;
		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sinCom.getListaComunicazioni().size(); j++)
			{
				ComunicazioneVO eleElenco=sinCom.getListaComunicazioni().get(j);
				if (eleSel.equals(eleElenco.getChiave().trim()))
				{
					codP="";
					codB=eleElenco.getCodBibl();
					codMsg=eleElenco.getCodiceMessaggio();
					tipoDoc=eleElenco.getTipoDocumento();
					tipoMsg=eleElenco.getTipoMessaggio();
					forn=eleElenco.getFornitore();
					idDoc=eleElenco.getIdDocumento();
					statoCom=eleElenco.getStatoComunicazione();
					dataCom=eleElenco.getDataComunicazione();
					dirCom=eleElenco.getDirezioneComunicazione();
					tipoInvioCom=eleElenco.getTipoInvioComunicazione();
					noteCom=eleElenco.getNoteComunicazione();

					eleCom=new ComunicazioneVO(codP,  codB,  codMsg,  tipoDoc, tipoMsg,  forn,  idDoc,  statoCom, dataCom,  dirCom,  tipoInvioCom,  noteCom);
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
 * SinteticaComAction.java
 * @param request
 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
 */
private void  PreparaRicercaComunicazione(SinteticaComForm sinCom, HttpServletRequest request)
{
	try {
		List<ListaSuppComunicazioneVO> ricerca=new ArrayList();
		ListaSuppComunicazioneVO eleRicerca=new ListaSuppComunicazioneVO();
		String [] listaSelezionati=sinCom.getSelectedComunicazioni();

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sinCom.getListaComunicazioni().size(); j++)
			{
				ComunicazioneVO eleElenco=sinCom.getListaComunicazioni().get(j);
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
					String codMsg=eleElenco.getCodiceMessaggio();
					String tipoDoc="";
					String tipoMsg="";
					StrutturaCombo forn=new StrutturaCombo("","");
					StrutturaTerna idDoc=new StrutturaTerna("","","");
					String statoCom="";
					String dataComDa="";
					String dataComA="";
					String dirCom="";
					String tipoInvioCom="";

/*					String tipoMsg=eleElenco.getTipoMessaggio();
					StrutturaCombo forn=new StrutturaCombo("","");
					forn.setCodice(eleElenco.getFornitore().getCodice());
					forn.setDescrizione(eleElenco.getFornitore().getDescrizione());
					StrutturaTerna idDoc=new StrutturaTerna("","","");
					//if (eleElenco.getTipoDocumento().equals("O"))
						idDoc.setCodice1(eleElenco.getIdDocumento().getCodice1());
						idDoc.setCodice2(eleElenco.getIdDocumento().getCodice2());
						idDoc.setCodice3(eleElenco.getIdDocumento().getCodice3());
					String statoCom=eleElenco.getStatoComunicazione();
					String dataComDa=eleElenco.getDataComunicazione();
					String dataComA=eleElenco.getDataComunicazione();
					String dirCom=eleElenco.getDirezioneComunicazione();
					String tipoInvioCom=eleElenco.getTipoInvioComunicazione();
*/
					String chiama=null;
					String ordina="";
					eleRicerca=new ListaSuppComunicazioneVO(codP, codB, codMsg,  tipoDoc, tipoMsg,  forn, idDoc, statoCom, dataComDa, dataComA, dirCom, tipoInvioCom, chiama, ordina );
					if (sinCom.getOrdinamentoScelto()!=null && sinCom.getOrdinamentoScelto().trim().length()>0 )
					{
						eleRicerca.setOrdinamento(sinCom.getOrdinamentoScelto());
					}
					ricerca.add(eleRicerca);
				}
			}
		}
		request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_COMUNICAZIONE, ricerca);
	} catch (Exception e) {
	}
}


private void  PreparaRicercaComunicazioneSingle(SinteticaComForm sinCom, HttpServletRequest request, int j)
{
	try {
		List<ListaSuppComunicazioneVO> ricerca=new ArrayList();
		ListaSuppComunicazioneVO eleRicerca=new ListaSuppComunicazioneVO();
			ComunicazioneVO eleElenco=sinCom.getListaComunicazioni().get(j);
				// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
				String codB=eleElenco.getCodBibl();
				String codMsg=eleElenco.getCodiceMessaggio();
				String tipoDoc="";
				String tipoMsg="";
				StrutturaCombo forn=new StrutturaCombo("","");
				StrutturaTerna idDoc=new StrutturaTerna("","","");
				String statoCom="";
				String dataComDa="";
				String dataComA="";
				String dirCom="";
				String tipoInvioCom="";

/*					String tipoMsg=eleElenco.getTipoMessaggio();
					StrutturaCombo forn=new StrutturaCombo("","");
					forn.setCodice(eleElenco.getFornitore().getCodice());
					forn.setDescrizione(eleElenco.getFornitore().getDescrizione());
					StrutturaTerna idDoc=new StrutturaTerna("","","");
					//if (eleElenco.getTipoDocumento().equals("O"))
						idDoc.setCodice1(eleElenco.getIdDocumento().getCodice1());
						idDoc.setCodice2(eleElenco.getIdDocumento().getCodice2());
						idDoc.setCodice3(eleElenco.getIdDocumento().getCodice3());
					String statoCom=eleElenco.getStatoComunicazione();
					String dataComDa=eleElenco.getDataComunicazione();
					String dataComA=eleElenco.getDataComunicazione();
					String dirCom=eleElenco.getDirezioneComunicazione();
					String tipoInvioCom=eleElenco.getTipoInvioComunicazione();
*/
				String chiama=null;
				String ordina="";

				eleRicerca=new ListaSuppComunicazioneVO(codP, codB, codMsg,  tipoDoc, tipoMsg,  forn, idDoc, statoCom, dataComDa, dataComA, dirCom, tipoInvioCom, chiama, ordina );
				if (sinCom.getOrdinamentoScelto()!=null && sinCom.getOrdinamentoScelto().trim().length()>0 )
				{
					eleRicerca.setOrdinamento(sinCom.getOrdinamentoScelto());
				}

				ricerca.add(eleRicerca);
				request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_COMUNICAZIONE, ricerca);
	} catch (Exception e) {
	}
}

public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
	if (idCheck.equals("CREA") ){
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_COMUNICAZIONI, utente.getCodPolo(), utente.getCodBib(), null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			//return true; // temporaneamente per superare l'abilitazione negata a monte
		}
	}

	//almaviva5_201011223 periodici
	if (idCheck.equals("PERIODICI") ) {
		Navigation navi = Navigation.getInstance(request);
		return !navi.bookmarksExist(PeriodiciDelegate.BOOKMARK_KARDEX, PeriodiciDelegate.BOOKMARK_FASCICOLO);
	}

	return false;
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

		sinCom.setListaComunicazioni(lista);
	}*/

/*	private void loadTipoDocumento() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - Fattura");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - Ordine");
		lista.add(elem);
		sinCom.setListaTipoDocumento(lista);
	}


	private void loadStatoComunicazione() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1	- RICEVUTO");
		lista.add(elem);
		elem = new StrutturaCombo("2","2	- SPEDITO");
		lista.add(elem);
		elem = new StrutturaCombo("3","3	- NON SPEDITO");
		lista.add(elem);
		sinCom.setListaStatoComunicazione(lista);
	}

	private void loadTipoMessaggio() throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","01 - Note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","02 - Reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","04 - Notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","07 - note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","08 - reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("10","10 - Già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("15","15 - Aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("17","17 - Fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","18 - Esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","19 - Non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("22","22 - Esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","23 - In attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("25","25 - reclamo e sollecito ordine");
		lista.add(elem);

		sinCom.setListaTipoMessaggio(lista);
	}*/

/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			SinteticaComForm sinCom = (SinteticaComForm) form;
			this.loadComunicazioni();
			sinCom.setNumComunicazioni(sinCom.getListaComunicazioni().size());


			if (request.getParameter("indietro0") != null) {
				return mapping.findForward("indietro");
			}

			if (request.getParameter("esamina0") != null) {
				if (sinCom.getSelectedComunicazioni().length!=0)
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
				String [] appoLista= new String [sinCom.getListaComunicazioni().size()];
				int i;
				for (i=0;  i < sinCom.getListaComunicazioni().size(); i++)
				{
					ComunicazioneVO com= (ComunicazioneVO) sinCom.getListaComunicazioni().get(i);
					String cod=com.getCodiceMessaggio();
					appoLista[i]=cod;
				}
				sinCom.setSelectedComunicazioni(appoLista);
				return mapping.getInputForward();
			}

			if (request.getParameter("deselTutti0") != null) {
				sinCom.setSelectedComunicazioni(null);
				return mapping.getInputForward();
			}


			this.loadStatoComunicazione();
			this.loadTipoDocumento();
			this.loadTipoMessaggio();

			return mapping.getInputForward();
	}
*/
}
