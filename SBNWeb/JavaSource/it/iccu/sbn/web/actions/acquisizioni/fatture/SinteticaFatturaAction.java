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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fatture.SinteticaFatturaForm;
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

public class SinteticaFatturaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {
	//private SinteticaFatturaForm sinFatture;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		map.put("ricerca.button.controllaordine","controlla");
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
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;

		try {


			if (Navigation.getInstance(request).isFromBar()  )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("fattureSelected")!= null && !request.getSession().getAttribute("fattureSelected").equals(""))
				{
					sinFatture.setSelectedFatture((String[]) request.getSession().getAttribute("fattureSelected"));
				}

				return mapping.getInputForward();
			}
			if(!sinFatture.isSessione())
			{
				sinFatture.setSessione(true);
			}

			ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
			if (ricArr!=null)
			{
				sinFatture.setOrdinamentoScelto(ricArr.getOrdinamento());
			}

			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				sinFatture.setVisibilitaIndietroLS(true);
				// per il layout
				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca
				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					sinFatture.setLSRicerca(true); // fai rox 4
				//}
			}
			// disabilitazione bottone esamina solo per richiami liste supporto ricorsiva da esamina fattura E NON DA INS FATTURA
			if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().equals("/acquisizioni/fatture/esaminaFattura"))
			{
				// imposta visibilità bottone scegli
				sinFatture.setAbilitaEsamina(false);
			}

			sinFatture.setRisultatiPresenti(true);
			if (ricArr==null)
			{
				// l'attributo di sessione deve essere valorizzato
				sinFatture.setRisultatiPresenti(false);
			}
			List<FatturaVO> listaFatture=new ArrayList();
			// deve essere escluso il caso di richiamo di lista supporto cambi
/*			if 	(request.getSession().getAttribute("listaFattureEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaFatture=(List<FatturaVO>)request.getSession().getAttribute("listaFattureEmessa");
			}
			else
			{
				listaFatture=this.getListaFatturaVO(ricArr ); // va in errore se non ha risultati
			}
*/
			//this.loadFornitori();
			if (ricArr!=null)
			{
				listaFatture=this.getListaFatturaVO(ricArr ); // va in errore se non ha risultati
			}

			sinFatture.setListaFatture(listaFatture);
			sinFatture.setNumFatture(sinFatture.getListaFatture().size());

			// gestione automatismo check su unico elemento lista
			if (sinFatture.getListaFatture().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=sinFatture.getListaFatture().get(0).getChiave();
				//	"FI|2007|3";
				sinFatture.setSelectedFatture(appoSelProva);
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
			if 	(request.getSession().getAttribute("ultimoBloccoFatture")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoFatture");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{

				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaFatture,maxElementiBlocco);
				sinFatture.setListaFatture(blocco1.getLista());
			}

			if (blocco1 != null)
			{
			//if (blocco1 == null)
			//abilito i tasti per il blocco se necessario
			//memorizzo le informazioni per la gestione blocchi
			sinFatture.setIdLista(blocco1.getIdLista()); //si
			sinFatture.setTotRighe(blocco1.getTotRighe()); //no
			sinFatture.setTotBlocchi(blocco1.getTotBlocchi()); //no
			sinFatture.setMaxRighe(blocco1.getMaxRighe()); //no
			//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
			sinFatture.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
			//sinOfferte.setNumRighe(2);
			//sinOfferte.setNumBlocco(1);
			sinFatture.setUltimoBlocco(blocco1);
			sinFatture.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi

			// non trovati
			if (sinFatture.getNumFatture()==0)
			{
				return mapping.getInputForward();

				//return mapping.findForward("indietro");

			}
			else {
				if 	(request.getSession().getAttribute("fattureSelected")!= null)
				{
					sinFatture.setSelectedFatture((String[]) request.getSession().getAttribute("fattureSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricArr.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave();
						for (int v=0;  v < sinFatture.getListaFatture().size(); v++)
						{
							String eleList= sinFatture.getListaFatture().get(v).getChiave();
							if (eleList.equals(eleSele))
							{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									sinFatture.getListaFatture().get(v).setTipoVariazione(variato);
								}
								break;
							}
						}
					}
				}

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
					sinFatture.setRisultatiPresenti(false);
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


	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
			{
			SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
			if (sinFatture.getProgrForm() > 0) {
			try {

					this.PreparaRicercaFatturaSingle( sinFatture, request,sinFatture.getProgrForm()-1);
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
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (!sinFatture.isSessione()) {
			sinFatture.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinFatture.getBloccoSelezionato();
		String idLista = sinFatture.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinFatture.getListaFatture().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoFatture",bloccoSucc);
				sinFatture.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoFatture",sinFatture.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=sinFatture.getSelectedFatture();
						appoParametro=(String[])request.getSession().getAttribute("fattureSelected");

						//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
						//{
						//	this.AggiornaParametriSintetica(request);
							request.getSession().setAttribute("attributeListaSuppFatturaVO", this.AggiornaRisultatiListaSupporto( sinFatture, ricArr));
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

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
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

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		try {
			sinFatture.setSelectedFatture(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		String [] appoLista= new String [sinFatture.getListaFatture().size()];
		int i;
		for (i=0;  i < sinFatture.getListaFatture().size(); i++)
		{
			FatturaVO fatt= sinFatture.getListaFatture().get(i);
			String cod=fatt.getChiave().trim();
			appoLista[i]=cod;
		}
		sinFatture.setSelectedFatture(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		try {
			String[] appoParametro=new String[0];
			String[] appoSelezione=new String[0];
			appoSelezione=sinFatture.getSelectedFatture();
/*			appoParametro=(String[])request.getSession().getAttribute("fattureSelected");
			// vedere se appoparametro è null
			if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			{
				this.AggiornaParametriSintetica(request);
			}
			appoSelezione=(String[]) sinFatture.getSelectedFatture();
			appoParametro=(String[])request.getSession().getAttribute("fattureSelected");
*/
			//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			if (appoSelezione!=null && appoSelezione.length!=0)
			{
				//this.AggiornaParametriSintetica(request);
				this.PreparaRicercaFattura( sinFatture, request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute("attributeListaSuppFatturaVO", this.AggiornaRisultatiListaSupporto( sinFatture, (ListaSuppFatturaVO)request.getSession().getAttribute("attributeListaSuppFatturaVO")));
				request.getSession().setAttribute("fattureSelected", appoSelezione);

				request.getSession().setAttribute("listaFattureEmessa", sinFatture.getListaFatture());

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


	public ActionForward controlla(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaFatturaForm sinFatture = (SinteticaFatturaForm) form;
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List<FatturaVO> getListaFatturaVO(ListaSuppFatturaVO criRicerca) throws Exception
	{
	List<FatturaVO> listaFatture;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaFatture = factory.getGestioneAcquisizioni().getRicercaListaFatture(criRicerca);
	//listaFatture = new ArrayList();
	//FatturaVO fatt=new FatturaVO("X10", "01", "2004", "4", "10", "10/01/2004", "10/01/2004", 400.00,0.00, "EUR", 1, "2", "F" , new StrutturaCombo("33","Libreria Grande"),"");
	//listaFatture.add(fatt);
	return listaFatture;
	}

	/**
	 * SinteticaFatturaAction.java
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

		appoSelezione=(String[]) sinFatture.getSelectedFatture();
		appoParametro=(String[])request.getSession().getAttribute("fattureSelected");
		List <FatturaVO> listaOld=new ArrayList();
		if (this.sintBuoniOrd.getBuoniVisualizzatiOld()!=null && this.sintBuoniOrd.getBuoniVisualizzatiOld().size()!=0 )
		{
			listaOld=this.sintBuoniOrd.getBuoniVisualizzatiOld();
		}
		else
		{
			listaOld=this.sintBuoniOrd.getBuoniVisualizzati();
		}

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			//this.sintBilanci.getListaBilanci()
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione,listaOld );
			request.getSession().setAttribute("fattureSelected", (String[])nuovoParametro);
			sinFatture.setSelectedFatture((String[]) nuovoParametro );
		}

	}*/

	/**
	 * SinteticaFatturaAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaFattura(SinteticaFatturaForm sinFatture, HttpServletRequest request)
	{
		try {

			List<ListaSuppFatturaVO> ricerca=new ArrayList();
			ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
			String [] listaSelezionati=sinFatture.getSelectedFatture();
			String ticket=Navigation.getInstance(request).getUserTicket();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinFatture.getListaFatture().size(); j++)
				{
					FatturaVO eleElenco=sinFatture.getListaFatture().get(j);
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
						String annoF=eleElenco.getAnnoFattura();
						String prgF=eleElenco.getProgrFattura();
						String dataDa="";
						String dataA="";
						String numF=""; // eleElenco.getNumFattura(); può essere cambiato ad una successiva emissione della sintetica tramite barra di navigazione
						//String numF=eleElenco.getNumFattura();
						String dataF=""; //eleElenco.getDataFattura();
						String dataRegF="";
						//String  staF=eleElenco.getStatoFattura();
						String  staF=""; // lo stato può essere cambiato ad una successiva emissione della sintetica tramite barra di navigazione
						String tipF=eleElenco.getTipoFattura();
						StrutturaCombo fornFatt=new StrutturaCombo("","");
						StrutturaTerna ordFatt=new StrutturaTerna("","","");
						StrutturaTerna bilFatt=new StrutturaTerna("","","");
						String chiama=null;
						String ordina="";

						eleRicerca=new ListaSuppFatturaVO(codP, codB,  annoF,  prgF ,  dataDa,  dataA ,   numF, dataF,  dataRegF,  staF,tipF , fornFatt, ordFatt,  bilFatt,  chiama,  ordina);
						if (sinFatture.getOrdinamentoScelto()!=null && sinFatture.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sinFatture.getOrdinamentoScelto());
						}
						eleRicerca.setIDFatt(eleElenco.getIDFatt());
						// aggiorna ticket
						eleRicerca.setTicket(ticket);
						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaFattura", ricerca);
		} catch (Exception e) {
		}
	}

	private void  PreparaRicercaFatturaSingle(SinteticaFatturaForm sinFatture, HttpServletRequest request, int j)
	{
		try {

			List<ListaSuppFatturaVO> ricerca=new ArrayList();
			ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			FatturaVO eleElenco=sinFatture.getListaFatture().get(j);

			// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=eleElenco.getCodBibl();
						String annoF=eleElenco.getAnnoFattura();
						String prgF=eleElenco.getProgrFattura();
						String dataDa="";
						String dataA="";
						String numF=""; //eleElenco.getNumFattura();
						String dataF=""; //eleElenco.getDataFattura();
						String dataRegF="";
						String staF=""; //eleElenco.getStatoFattura();
						String tipF=eleElenco.getTipoFattura();
						StrutturaCombo fornFatt=new StrutturaCombo("","");
						StrutturaTerna ordFatt=new StrutturaTerna("","","");
						StrutturaTerna bilFatt=new StrutturaTerna("","","");
						String chiama=null;

						String ordina="";

						eleRicerca=new ListaSuppFatturaVO(codP, codB,  annoF,  prgF ,  dataDa,  dataA ,   numF, dataF,  dataRegF,  staF,tipF , fornFatt, ordFatt,  bilFatt,  chiama,  ordina);
						if (sinFatture.getOrdinamentoScelto()!=null && sinFatture.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sinFatture.getOrdinamentoScelto());
						}
						// aggiorna ticket
						eleRicerca.setTicket(ticket);
						eleRicerca.setIDFatt(eleElenco.getIDFatt());
						ricerca.add(eleRicerca);
						request.getSession().setAttribute("criteriRicercaFattura", ricerca);
		} catch (Exception e) {
		}
	}


	/**
	 * SinteticaFatturaAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppFatturaVO AggiornaRisultatiListaSupporto (SinteticaFatturaForm sinFatture,ListaSuppFatturaVO eleRicArr)
	{
		try {

			List<FatturaVO> risultati=new ArrayList();
			FatturaVO eleFatt=new FatturaVO();
			String [] listaSelezionati=sinFatture.getSelectedFatture();

			String codP;
			String codB;
			String annoF;
			String prgF;
			String numF;
			String dataF;
			String dataRegF;
			double impF;
			double scoF;
			String valF;
			double camF;
			String  staF;
			String tipF;
			StrutturaCombo fornFatt;
			String tipoVar;

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinFatture.getListaFatture().size(); j++)
				{
					FatturaVO eleElenco=sinFatture.getListaFatture().get(j);
					if (eleSel.equals(eleElenco.getChiave().trim()))
					{

						codP="";
						codB=eleElenco.getCodBibl();
						annoF=eleElenco.getAnnoFattura();
						prgF=eleElenco.getProgrFattura();
						numF=eleElenco.getNumFattura();
						dataF=eleElenco.getDataFattura();
						dataRegF=eleElenco.getDataRegFattura();
						impF=eleElenco.getImportoFattura();
						scoF=eleElenco.getScontoFattura();
						valF=eleElenco.getValutaFattura();
						camF=eleElenco.getCambioFattura();
						staF=eleElenco.getStatoFattura();
						tipF=eleElenco.getTipoFattura();
						fornFatt=eleElenco.getFornitoreFattura();
						tipoVar="";

						eleFatt=new FatturaVO(codP, codB, annoF, prgF , numF, dataF, dataRegF, impF, scoF, valF, camF,  staF, tipF , fornFatt, tipoVar);
						risultati.add(eleFatt);
					}
				}
			}
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}
	/**
	 * SinteticaFatturaAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<FatturaVO> lista)
	{
//		attributeListaSuppFatturaVO

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
		if (lista==null)
		{
			lista=new ArrayList();
		}

		// anche se selected è vuoto è possibile che ci siano stati solo dechek
		if (parametro!=null && parametro.length >0)
		{
			//ciclo sulla lista getElencaCambi (postata dal submit) per individuare i selezionati e i decheck
			for (int x=0;  x < lista.size(); x++)
			{
				FatturaVO fatt= (FatturaVO) lista.get(x);
				cod=fatt.getChiave().trim();
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
					// si può eliminare il controllo sul decked in blocco

					if (memorizzato)
					{
						// è stato attualmente deselezionato?
						if (selezionati!=null && selezionati.length >0)
						{
							selezionato=false;
							for (int i=0;  i < selezionati.length; i++)
							{
								eleSel= (String) selezionati[i];
								if (eleSel.equals(cod))
								{
									selezionato=true;
									break;
								}
							}
						}
					}

					// fine controllo decked in blocco
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
/*	private void loadFatture() throws Exception {
		List lista = new ArrayList();

		FatturaVO fatt=new FatturaVO("X10", "01", "2005", "8", "4", "14/06/2005", "14/06/2005", 400.00,0.00, "EUR", 1, "1", "F" , new StrutturaCombo("33","Libreria Grande"),"");
		lista.add(fatt);
		fatt=new FatturaVO("X10", "01", "2004", "7", "2", "01/09/2004", "01/09/2004", 190.00,0.00, "EUR", 1, "1", "F" , new StrutturaCombo("21","Libreria XXX"),"");
		lista.add(fatt);

		sinFatture.setListaFatture(lista);
	}
*/

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_FATTURE, utente.getCodPolo(), utente.getCodBib(), null);
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
