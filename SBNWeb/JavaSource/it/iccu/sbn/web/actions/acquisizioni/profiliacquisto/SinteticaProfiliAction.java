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
package it.iccu.sbn.web.actions.acquisizioni.profiliacquisto;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.profiliacquisto.SinteticaProfiliForm;
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

public class SinteticaProfiliAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	//private SinteticaProfiliForm sinProfilo;
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.crea","crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "Ok");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar()  )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("profiliSelected")!= null && !request.getSession().getAttribute("profiliSelected").equals(""))
				{
					sinProfilo.setSelectedProfili((String[]) request.getSession().getAttribute("profiliSelected"));
				}

				return mapping.getInputForward();
			}
			if(!sinProfilo.isSessione())
			{
				sinProfilo.setSessione(true);
			}
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			if (ricArr!=null)
			{
				sinProfilo.setOrdinamentoScelto(ricArr.getOrdinamento());
			}

			if (ricArr!=null &&  ricArr.getChiamante()!=null && (ricArr.getSelezioniChiamato()==null || ricArr.getChiamante().equals("/acquisizioni/fornitori/esaminaFornitore")) )
			{
				// per il layout

				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					sinProfilo.setLSRicerca(true); // fai rox 4
				//}

				// imposto solo per chiamate da esamina fornitore le selezioni dei profili
				if (ricArr.getChiamante().equals("/acquisizioni/fornitori/esaminaFornitore") && ricArr.getSelezioniChiamato().size()>0 )
				{
					String[] selezRicArr=new String[ricArr.getSelezioniChiamato().size()];
					for (int x=0;  x < ricArr.getSelezioniChiamato().size(); x++)
					{
						StrutturaProfiloVO profi= ricArr.getSelezioniChiamato().get(x);
						String cod=profi.getChiave().trim();
						selezRicArr[x]=cod;
					}
					request.getSession().setAttribute("profiliSelected",selezRicArr);
					ricArr.setSelezioniChiamato(null);
					request.getSession().setAttribute("attributeListaSuppProfiloVO",ricArr);

				}
				// imposta visibilità bottone scegli
				sinProfilo.setVisibilitaIndietroLS(true);
			}
			sinProfilo.setRisultatiPresenti(true);
			if (ricArr==null)
			{
				// l'attributo di sessione deve essere valorizzato
				sinProfilo.setRisultatiPresenti(false);
			}
			List<StrutturaProfiloVO> listaProfili=new ArrayList();
			// deve essere escluso il caso di richiamo di lista supporto cambi
/*			if 	(request.getSession().getAttribute("listaProfiliEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaProfili=(List<StrutturaProfiloVO>)request.getSession().getAttribute("listaProfiliEmessa");
			}
			else
			{
				listaProfili=this.getListaStrutturaProfiloVO(ricArr ); // va in errore se non ha risultati
			}
*/
			if (ricArr!=null)
			{
				listaProfili=this.getListaStrutturaProfiloVO(ricArr ); // va in errore se non ha risultati
			}

			//this.loadFornitori();
			sinProfilo.setListaProfili(listaProfili);
			sinProfilo.setNumProfili(sinProfilo.getListaProfili().size());

			// gestione automatismo check su unico elemento lista
			if (sinProfilo.getListaProfili().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=sinProfilo.getListaProfili().get(0).getChiave();
				//	"FI|2007|3";
				sinProfilo.setSelectedProfili(appoSelProva);
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
			if 	(request.getSession().getAttribute("ultimoBloccoProfili")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoProfili");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{

				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaProfili,maxElementiBlocco);
				sinProfilo.setListaProfili(blocco1.getLista());
			}

			if (blocco1 != null)
			{
			//if (blocco1 == null)
			//abilito i tasti per il blocco se necessario
			//memorizzo le informazioni per la gestione blocchi
			sinProfilo.setIdLista(blocco1.getIdLista()); //si
			sinProfilo.setTotRighe(blocco1.getTotRighe()); //no
			sinProfilo.setTotBlocchi(blocco1.getTotBlocchi()); //no
			sinProfilo.setMaxRighe(blocco1.getMaxRighe()); //no
			//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
			sinProfilo.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
			//sinOfferte.setNumRighe(2);
			//sinOfferte.setNumBlocco(1);
			sinProfilo.setUltimoBlocco(blocco1);
			sinProfilo.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi

			// non trovati
			if (sinProfilo.getNumProfili()==0)
			{
				return mapping.getInputForward();

			}
			else {
				if 	(request.getSession().getAttribute("profiliSelected")!= null)
				{
					sinProfilo.setSelectedProfili((String[]) request.getSession().getAttribute("profiliSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricArr.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave();
						for (int v=0;  v < sinProfilo.getListaProfili().size(); v++)
						{
							String eleList= sinProfilo.getListaProfili().get(v).getChiave();
							if (eleList.equals(eleSele))
							{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									sinProfilo.getListaProfili().get(v).setTipoVariazione(variato);
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
					sinProfilo.setRisultatiPresenti(false);
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
			throws Exception {
				SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
				if (sinProfilo.getProgrForm() > 0) {
					try {
							this.PreparaRicercaProfiloSingle( sinProfilo, request,sinProfilo.getProgrForm()-1);
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
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sinProfilo.isSessione()) {
			sinProfilo.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinProfilo.getBloccoSelezionato();
		String idLista = sinProfilo.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinProfilo.getListaProfili().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoProfili",bloccoSucc);
				sinProfilo.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoProfili",sinProfilo.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}


	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=sinProfilo.getSelectedProfili();
						appoParametro=(String[])request.getSession().getAttribute("profiliSelected");

						//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
						//{
							//this.AggiornaParametriSintetica(request);
							request.getSession().setAttribute("attributeListaSuppProfiloVO", this.AggiornaRisultatiListaSupporto( sinProfilo, ricArr));
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


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
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
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		try {
			sinProfilo.setSelectedProfili(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		String [] appoLista= new String [sinProfilo.getListaProfili().size()];
		int i;
		for (i=0;  i < sinProfilo.getListaProfili().size(); i++)
		{
			StrutturaProfiloVO prof= sinProfilo.getListaProfili().get(i);
			String cod=prof.getChiave().trim();
			appoLista[i]=cod;
		}
		sinProfilo.setSelectedProfili(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		try {
			String[] appoParametro=new String[0];
			String[] appoSelezione=new String[0];
			appoSelezione=sinProfilo.getSelectedProfili();
/*			appoParametro=(String[])request.getSession().getAttribute("profiliSelected");
			// vedere se appoparametro è null
			if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			{
				this.AggiornaParametriSintetica(request);
			}
			appoSelezione=(String[]) sinProfilo.getSelectedProfili();
			appoParametro=(String[])request.getSession().getAttribute("profiliSelected");
*/
			//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			if (appoSelezione!=null && appoSelezione.length!=0)
			{
				//this.AggiornaParametriSintetica(request);
				this.PreparaRicercaProfilo( sinProfilo, request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute("attributeListaSuppProfiloVO", this.AggiornaRisultatiListaSupporto( sinProfilo,  (ListaSuppProfiloVO)request.getSession().getAttribute("attributeListaSuppProfiloVO")));
				request.getSession().setAttribute("profiliSelected", appoSelezione);
				request.getSession().setAttribute("listaProfiliEmessa", sinProfilo.getListaProfili());
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

/*	private void loadProfili() throws Exception {
		List lista = new ArrayList();
		StrutturaProfiloVO profilo = new StrutturaProfiloVO("X10", "01", new StrutturaCombo("9", "FANTASY"),new StrutturaCombo("LET", ""), new StrutturaCombo("ITA", ""), new StrutturaCombo("IT", ""), null);
		lista.add(profilo);
		profilo = new StrutturaProfiloVO("X10", "01", new StrutturaCombo("10", "LETTERATURA"),new StrutturaCombo("LET", ""), new StrutturaCombo("ITA", ""), new StrutturaCombo("IT", ""),null  );
		lista.add(profilo);
		profilo = new StrutturaProfiloVO("X10", "01", new StrutturaCombo("1", "LETTERATURA ITALIANA"),new StrutturaCombo("LET", ""), new StrutturaCombo("ITA", ""), new StrutturaCombo("IT", ""),null  );
		lista.add(profilo);

		sinProfilo.setListaProfili(lista);
	}
*/
	private List<StrutturaProfiloVO> getListaStrutturaProfiloVO(ListaSuppProfiloVO criRicerca) throws Exception
	{
	List<StrutturaProfiloVO> listaProfili;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaProfili = factory.getGestioneAcquisizioni().getRicercaListaProfili(criRicerca);
	return listaProfili;
	}


	/**
	 * SinteticaProfiliAction.java
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

		appoSelezione=(String[]) sinProfilo.getSelectedProfili();
		appoParametro=(String[])request.getSession().getAttribute("profiliSelected");
		List <StrutturaProfiloVO> listaOld=new ArrayList();
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
			request.getSession().setAttribute("profiliSelected", (String[])nuovoParametro);
			sinProfilo.setSelectedProfili((String[]) nuovoParametro );
		}

	}*/

	/**
	 * SinteticaProfiliAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaProfilo(SinteticaProfiliForm sinProfilo, HttpServletRequest request)
	{
		try {

			List<ListaSuppProfiloVO> ricerca=new ArrayList();
			ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
			String [] listaSelezionati=sinProfilo.getSelectedProfili();
			String ticket=Navigation.getInstance(request).getUserTicket();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinProfilo.getListaProfili().size(); j++)
				{
					StrutturaProfiloVO eleElenco=sinProfilo.getListaProfili().get(j);
					String chiaveComposta=eleElenco.getChiave().trim();
					//chiaveComposta[3] codOrdine
					//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{
						ListaSuppProfiloVO eleRicerca1=new ListaSuppProfiloVO();
						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=eleElenco.getCodBibl();
						StrutturaCombo prof=new StrutturaCombo(eleElenco.getProfilo().getCodice(),eleElenco.getProfilo().getDescrizione());
						//StrutturaCombo sez=new StrutturaCombo(eleElenco.getSezione().getCodice(),eleElenco.getSezione().getDescrizione());
						StrutturaCombo sez=new StrutturaCombo("","");
						StrutturaCombo lin=new StrutturaCombo("","");
						StrutturaCombo pae=new StrutturaCombo("","");
						String chiama=null;
						String ordina="";
						eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae, chiama, ordina );
						eleRicerca.setTicket(ticket);
						if (sinProfilo.getOrdinamentoScelto()!=null && sinProfilo.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sinProfilo.getOrdinamentoScelto());
						}
						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaProfilo", ricerca);
		} catch (Exception e) {
		}
	}

	private void  PreparaRicercaProfiloSingle(SinteticaProfiliForm sinProfilo, HttpServletRequest request, int j)
	{
		try {

			List<ListaSuppProfiloVO> ricerca=new ArrayList();
			ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			StrutturaProfiloVO eleElenco=sinProfilo.getListaProfili().get(j);
			ListaSuppProfiloVO eleRicerca1=new ListaSuppProfiloVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleElenco.getCodBibl();
			StrutturaCombo prof=new StrutturaCombo(eleElenco.getProfilo().getCodice(),eleElenco.getProfilo().getDescrizione());
			StrutturaCombo sez=new StrutturaCombo(eleElenco.getSezione().getCodice(),eleElenco.getSezione().getDescrizione());
			StrutturaCombo lin=new StrutturaCombo("","");
			StrutturaCombo pae=new StrutturaCombo("","");
			String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae, chiama, ordina );
			eleRicerca.setTicket(ticket);
			if (sinProfilo.getOrdinamentoScelto()!=null && sinProfilo.getOrdinamentoScelto().trim().length()>0 )
			{
				eleRicerca.setOrdinamento(sinProfilo.getOrdinamentoScelto());
			}

			ricerca.add(eleRicerca);
			request.getSession().setAttribute("criteriRicercaProfilo", ricerca);
		} catch (Exception e) {
		}
	}


	/**
	 * SinteticaProfiliAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppProfiloVO AggiornaRisultatiListaSupporto (SinteticaProfiliForm sinProfilo, ListaSuppProfiloVO eleRicArr)
	{
		try {

			List<StrutturaProfiloVO> risultati=new ArrayList();
			StrutturaProfiloVO eleProf=new StrutturaProfiloVO();
			String [] listaSelezionati=sinProfilo.getSelectedProfili();


			String polo;
			String bibl;
			StrutturaCombo prof;
			StrutturaCombo sez;
			StrutturaCombo lin;
			StrutturaCombo pae;
			List listProf;
			String tipoVar;

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinProfilo.getListaProfili().size(); j++)
				{
					StrutturaProfiloVO eleElenco=sinProfilo.getListaProfili().get(j);
					if (eleSel.equals(eleElenco.getChiave().trim()))
					{
						polo="";
						bibl=eleElenco.getCodBibl();;
						prof=eleElenco.getProfilo();
						sez=eleElenco.getSezione();
						lin=eleElenco.getLingua();
						pae=eleElenco.getPaese();
						listProf=eleElenco.getListaFornitori();
						tipoVar="";
						eleProf=new StrutturaProfiloVO( polo, bibl, prof,  sez,  lin,  pae,  listProf, tipoVar );
						risultati.add(eleProf);
					}
				}
			}
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}
	/**
	 * SinteticaProfiliAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<StrutturaProfiloVO> lista)
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
				StrutturaProfiloVO fatt= (StrutturaProfiloVO) lista.get(x);
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
	}		*/

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_PROFILI_DI_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
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

		HttpSession httpSession = request.getSession();
		//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());

		SinteticaProfiliForm sinProfilo = (SinteticaProfiliForm) form;
		this.loadProfili();
		sinProfilo.setNumProfili(sinProfilo.getListaProfili().size());


		if (request.getParameter("indietro0") != null) {
			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		}



		if (request.getParameter("esamina0") != null)
		{
			if (sinProfilo.getSelectedProfili().length!=0)
			{
				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
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
			String [] appoLista= new String [sinProfilo.getListaProfili().size()];
			int i;
			for (i=0;  i < sinProfilo.getListaProfili().size(); i++)
			{
				StrutturaProfiloVO profilo= (StrutturaProfiloVO) sinProfilo.getListaProfili().get(i);
				String cod=profilo.getProfilo().getCodice();
				appoLista[i]=cod;
			}
			sinProfilo.setSelectedProfili(appoLista);
			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), true);
			return mapping.getInputForward();
		}

		if (request.getParameter("deselTutti0") != null) {
			sinProfilo.setSelectedProfili(null);
			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), true);
			return mapping.getInputForward();
		}

		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);

		return mapping.getInputForward();
	}*/

}
