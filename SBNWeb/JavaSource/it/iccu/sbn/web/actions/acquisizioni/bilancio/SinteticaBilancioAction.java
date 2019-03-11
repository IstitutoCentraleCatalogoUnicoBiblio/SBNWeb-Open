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
package it.iccu.sbn.web.actions.acquisizioni.bilancio;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.bilancio.SinteticaBilancioForm;
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

public class SinteticaBilancioAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{
	//private SinteticaBilancioForm sintBilanci;
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
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
			{

				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("bilanciSelected")!= null && !request.getSession().getAttribute("bilanciSelected").equals(""))
				{
					sintBilanci.setSelectedBilanci((String[]) request.getSession().getAttribute("bilanciSelected"));

				}

				return mapping.getInputForward();
			}
			if(!sintBilanci.isSessione())
			{
				sintBilanci.setSessione(true);
			}
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
			if (ricArr!=null)
			{
				sintBilanci.setOrdinamentoScelto(ricArr.getOrdinamento());
			}

			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta ordinamento
				// imposta visibilità bottone scegli
				sintBilanci.setVisibilitaIndietroLS(true);
				// per il layout

				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					sintBilanci.setLSRicerca(true); // fai rox 4
				//}
				// per il layout fine

			}
			if (ricArr==null)
			{
				// l'attributo di sessione deve essere valorizzato
				sintBilanci.setRisultatiPresenti(false);
			}

			List<BilancioVO> listaBilanci=new ArrayList();
			// deve essere escluso il caso di richiamo di lista supporto cambi

/*			if 	(request.getSession().getAttribute("listaBilanciEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaBilanci=(List<BilancioVO>)request.getSession().getAttribute("listaBilanciEmessa");
			}
			else
			{
				listaBilanci=this.getListaBilancioVO(ricArr ); // va in errore se non ha risultati
			}
*/
			if (ricArr!=null)
			{
				listaBilanci=this.getListaBilancioVO(ricArr, request ); // va in errore se non ha risultati
			}
			//this.loadFornitori();
			sintBilanci.setListaBilanci(listaBilanci);
			sintBilanci.setNumBilanci(sintBilanci.getListaBilanci().size());
//
//

			// gestione automatismo check su unico elemento lista
			if (sintBilanci.getListaBilanci().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=sintBilanci.getListaBilanci().get(0).getChiave();
				//	"FI|2007|3";
				sintBilanci.setSelectedBilanci(appoSelProva);
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
			if 	(request.getSession().getAttribute("ultimoBloccoBilanci")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoBilanci");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{

				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaBilanci,maxElementiBlocco);
				sintBilanci.setListaBilanci(blocco1.getLista());
			}

			if (blocco1 != null)
			{
			//if (blocco1 == null)
			//abilito i tasti per il blocco se necessario
			//memorizzo le informazioni per la gestione blocchi
			sintBilanci.setIdLista(blocco1.getIdLista()); //si
			sintBilanci.setTotRighe(blocco1.getTotRighe()); //no
			sintBilanci.setTotBlocchi(blocco1.getTotBlocchi()); //no
			sintBilanci.setMaxRighe(blocco1.getMaxRighe()); //no
			//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
			sintBilanci.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
			//sinOfferte.setNumRighe(2);
			//sinOfferte.setNumBlocco(1);
			sintBilanci.setUltimoBlocco(blocco1);
			sintBilanci.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi



			// non trovati
			if (sintBilanci.getNumBilanci()==0)
			{

				return mapping.getInputForward();

				//return mapping.findForward("indietro");

			}
			else {
				// imposto con la var impostata su ricerca
/*				if (request.getAttribute("numElexBlocchi")!=null)
				{
					sintBilanci.setNumRighe((Integer)request.getAttribute("numElexBlocchi"));
				}
				// controllare che numrighe non sia superiore alla dimensione dell'arraylist listabilanci
				int totPagine=(int) Math.round((sintBilanci.getNumBilanci()/sintBilanci.getNumRighe()));
				int resto=(int) sintBilanci.getNumBilanci()%sintBilanci.getNumRighe();
				if (resto > 0)
				{
					totPagine=totPagine+1;
				}
				sintBilanci.setTotPagine(totPagine);

				if (sintBilanci.getNumRighe()>sintBilanci.getListaBilanci().size())
				{
					sintBilanci.setNumRighe(sintBilanci.getListaBilanci().size());
				}
				//sintBilanci.setBilanciVisualizzati(sintBilanci.getListaBilanci().subList(0,sintBilanci.getNumRighe()));
				if (sintBilanci.getBilanciVisualizzati()==null )
				{
					sintBilanci.setBilanciVisualizzati(new ArrayList<BilancioVO>());
					int start=0;
					int end=sintBilanci.getNumRighe();
					for (int j=0;  j < end; j++)
					{
						BilancioVO ele=sintBilanci.getListaBilanci().get(j);
						sintBilanci.getBilanciVisualizzati().add(ele);
					}
					// inizializzazione blocchi
					int totblck=sintBilanci.getTotPagine();
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
		            	if (seqBlock[i].getInizio()+sintBilanci.getNumRighe()<=sintBilanci.getListaBilanci().size())
		            	{
			            	seqBlock[i].setFine(seqBlock[i].getInizio()+sintBilanci.getNumRighe());
		            	}
		            	else
		            	{
			            	seqBlock[i].setFine(sintBilanci.getListaBilanci().size());
		            	}

		            }
					sintBilanci.setSequenzablocchi(seqBlock);
					// fine inizializzazione blocchi

				}

				// posizionamento blocco successivo
				int blckSucc=sintBilanci.getNumPagina();
				if (sintBilanci.getSequenzablocchi().length>0)
				{
					for (int x=blckSucc; x<sintBilanci.getSequenzablocchi().length; x++)
					{
						if (!sintBilanci.getSequenzablocchi()[x].isCaricato())
						{
							blckSucc=x+1;
							break;
						}
					}

				}
				sintBilanci.setNumPagina(blckSucc);*/
/*				if (blckSucc<= sintBilanci.getTotPagine() && blckSucc>0)
				{
					sintBilanci.setNumPagina(blckSucc);
				}
*/
				if 	(request.getSession().getAttribute("bilanciSelected")!= null)
				{
					sintBilanci.setSelectedBilanci((String[]) request.getSession().getAttribute("bilanciSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricArr.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave();
						for (int v=0;  v < sintBilanci.getListaBilanci().size(); v++)
						{
							String eleList= sintBilanci.getListaBilanci().get(v).getChiave();
							if (eleList.equals(eleSele))
							{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									sintBilanci.getListaBilanci().get(v).setTipoVariazione(variato);
								}
								break;
							}
						}
					}
				}
				//ordinamento
				//sintBilanci.setListaBilanci();
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
						sintBilanci.setRisultatiPresenti(false);
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

			SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
			if (sintBilanci.getProgrForm() > 0) {
				try {
					this.PreparaRicercaBilancioSingle( sintBilanci, request,sintBilanci.getProgrForm()-1);
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
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sintBilanci.isSessione()) {
			sintBilanci.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sintBilanci.getBloccoSelezionato();
		String idLista = sintBilanci.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sintBilanci.getListaBilanci().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoBilanci",bloccoSucc);
				sintBilanci.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoBilanci",sintBilanci.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}



	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
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
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		try {
			if (sintBilanci.getSelectedBilanci()==null || sintBilanci.getSelectedBilanci().length==0 || sintBilanci.getSelectedBilanci().length>1) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ricercaOchecksingolo"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone

			// trasformazione in strutturaCombo
			  //Process each of the elements
			  String[] selImp=sintBilanci.getSelectedImp();
			  StrutturaCombo[] selImpStrut=new StrutturaCombo[selImp.length];

			  for(int i=0;i<selImp.length;i++){

			    //Remove everything before the 1st span
			    int spanPos = selImp[i].indexOf("*");
			    //if we find a match, take out
			    //everything before the span
		        selImpStrut[i]=new StrutturaCombo("","");
			    if(spanPos>0){
			        String  primaParteChiave=selImp[i].substring(0,spanPos);
			        String  secondaParteImpegno=selImp[i].substring(spanPos+1);
			        selImpStrut[i].setCodice(primaParteChiave);
			        selImpStrut[i].setDescrizione(secondaParteImpegno);
			    }
			  }
			  // aggiornamento selected bilanci con il valore dell'impegno selezionato
				// carica i criteri di ricerca da passare alla esamina
				for (int i=0;  i < sintBilanci.getSelectedBilanci().length; i++)
				{
					String eleSel= sintBilanci.getSelectedBilanci()[i];
					for (int j=0;  j < sintBilanci.getListaBilanci().size(); j++)
					{
						BilancioVO eleElenco=sintBilanci.getListaBilanci().get(j);
						if (eleSel.equals(eleElenco.getChiave().trim()))
						{
							for (int y=0;  y < selImpStrut.length; y++)
							{
								if (eleSel.equals(selImpStrut[y].getCodice()))
								{
									eleElenco.setSelezioneImp(selImpStrut[y].getDescrizione());
								}
							}

						}
					}
				}
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=sintBilanci.getSelectedBilanci();
						appoParametro=(String[])request.getSession().getAttribute("bilanciSelected");

						//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
						//{
						//	this.AggiornaParametriSintetica(request);
							request.getSession().setAttribute("attributeListaSuppBilancioVO", this.AggiornaRisultatiListaSupporto( sintBilanci,ricArr));
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



/*	public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		try {

			sintBilanci.setNumBilanci(sintBilanci.getListaBilanci().size());
			// aggiungo 0.5 per arrotondarlo al intero superiore
			// controllare che il numero delle righe non sia il size perchè totpag viene =2
			int totPagine= (int)Math.round((sintBilanci.getNumBilanci()/sintBilanci.getNumRighe()));
			// resto della divisione
			int resto=(int) sintBilanci.getNumBilanci()%sintBilanci.getNumRighe();
			if (resto > 0)
			{
				totPagine=totPagine+1;
			}

			sintBilanci.setTotPagine(totPagine);
			// la posizione dell'elemento nella lista si conteggia da zero per cui non aggiungo una unità
			sintBilanci.setPosElemento((sintBilanci.getNumRighe())*(sintBilanci.getNumPagina()-1));


			// carica blocco 3
			//sintBilanci.setBilanciVisualizzati((List <BilancioVO>)sintBilanci.getListaBilanci().subList(0,2));
			//sintBilanci.getListaBilanci().subList(0,2).listIterator(1);
			//int dim=sintBilanci.getBilanciVisualizzati().size();
			//sintBilanci.getBilanciVisualizzati().addAll(dim ,(List<BilancioVO>)sintBilanci.getListaBilanci().subList(2,4));
			//sintBilanci.setListaBilanci(sintBilanci.getBilanciVisualizzati());

			//sintBilanci.setBilanciVisualizzati(sintBilanci.getListaBilanci().subList(2,4));
			//int dim=sintBilanci.getBilanciVisualizzati().size();
			//sintBilanci.getListaBilanci().addAll(dim ,sintBilanci.getBilanciVisualizzati());

			// occorre controllare che non sia già stato caricato

			if (sintBilanci.getNumPagina()>0 && sintBilanci.getNumPagina()<=sintBilanci.getTotPagine())
			{
				if (!sintBilanci.getSequenzablocchi()[sintBilanci.getNumPagina()-1].isCaricato())
				{
					int start=sintBilanci.getSequenzablocchi()[sintBilanci.getNumPagina()-1].getInizio();
					int end=sintBilanci.getSequenzablocchi()[sintBilanci.getNumPagina()-1].getFine();
					//sintBilanci.getBilanciVisualizzati().addAll(sintBilanci.getBilanciVisualizzati().size(),sintBilanci.getListaBilanci().subList(start,end));
					for (int j=start;  j < end; j++)
					{
						BilancioVO ele=sintBilanci.getListaBilanci().get(j);
						sintBilanci.getBilanciVisualizzati().add(ele);
					}
					sintBilanci.getSequenzablocchi()[sintBilanci.getNumPagina()-1].setCaricato(true);
					// posizionamento blocco successivo

					int blckSucc=sintBilanci.getNumPagina()+1;
					if (blckSucc<= sintBilanci.getTotPagine() && blckSucc>0)
					{
						sintBilanci.setNumPagina(blckSucc);
					}
					int blckSucc=sintBilanci.getNumPagina();
					if (sintBilanci.getSequenzablocchi().length>0)
					{
						for (int x=blckSucc; x<sintBilanci.getSequenzablocchi().length; x++)
						{
							if (!sintBilanci.getSequenzablocchi()[x].isCaricato())
							{
								blckSucc=x+1;
								break;
							}
						}

					}
					sintBilanci.setNumPagina(blckSucc);

				}
				else
				{
					// blocco già caricato
				}

			}
			this.AggiornaParametriSintetica(request);
			// errore
			if (Integer.valueOf(sintBilanci.getNumPagina())>totPagine)
			{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.errorePagina"));
				this.saveErrors(request, errors);
				// reimposto la paginazione a quella precedente all'errore
				//sintBilanci.setNumRighe(sintBilanci.getNumRigheOld());
				//sintBilanci.setPosElemento(sintBilanci.getPosElementoOld());
				//sintBilanci.setNumPagina(sintBilanci.getNumPaginaOld());
				return mapping.getInputForward();

			}

			//sintBilanci.setNumRigheOld(sintBilanci.getNumRighe());
			//sintBilanci.setPosElementoOld(sintBilanci.getPosElemento());
			sintBilanci.setBilanciVisualizzatiOld(sintBilanci.getBilanciVisualizzati());

			return mapping.getInputForward();
		} catch (Exception e) {
			e.printStackTrace();

			return mapping.getInputForward();
		}
	}
	*/

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		try {
			String[] appoParametro=new String[0];

			String[] appoSelezione=new String[0];
			appoSelezione=sintBilanci.getSelectedBilanci();
/*			appoParametro=(String[])request.getSession().getAttribute("bilanciSelected");
			if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			{
				this.AggiornaParametriSintetica(request);
			}
			appoSelezione=(String[]) sintBilanci.getSelectedBilanci();
			appoParametro=(String[])request.getSession().getAttribute("bilanciSelected");
*/			// vedere se appoparametro è null
			//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
			if (appoSelezione!=null && appoSelezione.length!=0)

			{
				//this.AggiornaParametriSintetica(request);
				this.PreparaRicercaBilancio( sintBilanci,request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute("attributeListaSuppBilancioVO", this.AggiornaRisultatiListaSupporto(  sintBilanci,(ListaSuppBilancioVO)request.getSession().getAttribute("attributeListaSuppBilancioVO")));
				request.getSession().setAttribute("bilanciSelected", appoSelezione);
				request.getSession().setAttribute("listaBilanciEmessa", sintBilanci.getListaBilanci());
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


	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sintBilanci.isSessione())
			{
				sintBilanci.setSessione(true);
			}
		}
		String [] appoLista= new String [sintBilanci.getListaBilanci().size()];
		int i;
		for (i=0;  i < sintBilanci.getListaBilanci().size(); i++)
		{
			BilancioVO bil= sintBilanci.getListaBilanci().get(i);
			String cod=bil.getChiave();
			appoLista[i]=cod;
		}
		sintBilanci.setSelectedBilanci(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBilancioForm sintBilanci = (SinteticaBilancioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sintBilanci.isSessione())
			{
				sintBilanci.setSessione(true);
			}
		}
		try {
			sintBilanci.setSelectedBilanci(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	/**
	 * SinteticaBilanciAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppBilancioVO AggiornaRisultatiListaSupporto (SinteticaBilancioForm sintBilanci,ListaSuppBilancioVO eleRicArr)
	{

		try {
			List<BilancioVO> risultati=new ArrayList();
			BilancioVO eleBilancio=new BilancioVO();
			BilancioDettVO eleBilancioDett=new BilancioDettVO();
			String [] listaSelezionati=sintBilanci.getSelectedBilanci();
			String codP;
			String codB;
			String ese;
			String cap;
			double bdgCap;
			String imp;
			double bdg;
			double impegna;
			double fattur;
			double paga;
			String tipoVar;

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sintBilanci.getListaBilanci().size(); j++)
				{
					BilancioVO eleElenco=sintBilanci.getListaBilanci().get(j);
					if (eleSel.equals(eleElenco.getChiave()))
					{
						codP=eleElenco.getCodPolo();
						codB=eleElenco.getCodBibl();
						ese=eleElenco.getEsercizio();
						cap=eleElenco.getCapitolo();
						bdgCap=eleElenco.getBudgetDiCapitolo();
						if (eleElenco.getDettagliBilancio()!=null && eleElenco.getDettagliBilancio().size()>0 )
						{
							imp=eleElenco.getDettagliBilancio().get(0).getImpegno();
							//imp="";
							//bdg=eleElenco.getBudget();
							//impegna=eleElenco.getImpegnato();
							//fattur=eleElenco.getImpFatt();
							//paga=eleElenco.getPagato();
							tipoVar="";
							eleBilancio=new BilancioVO(codP,  codB,  ese,  cap ,bdgCap,   tipoVar );
							eleBilancioDett=new BilancioDettVO();
							eleBilancioDett.setImpegno(imp);
							eleBilancio.getDettagliBilancio().add(eleBilancioDett);
						}

						eleBilancio.setSelezioneImp(eleElenco.getSelezioneImp()); // aggiunto tipo impegno selezionato
						risultati.add(eleBilancio);

					}
				}
			}
			//ricArrAppo=new ArrayList();
			eleRicArr.setSelezioniChiamato(risultati);
			//ListaSuppCambioVO eleRicArrAppo=eleRicArr;
			//ricArrAppo.add(eleRicArrAppo);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return eleRicArr;
	}


	private List<BilancioVO> getListaBilancioVO(ListaSuppBilancioVO criRicerca,HttpServletRequest request) throws Exception
	{
	List<BilancioVO> listaBilanci;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	//criRicerca.setLoc(request.getLocale());	//	 aggiunto per Documento Fisico 09/05/08
	criRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunto per Documento Fisico 27/05/08 insieme al SinteticaLookupDispatchAction invece di LookupDispatchAction
	listaBilanci = factory.getGestioneAcquisizioni().getRicercaListaBilanci(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	return listaBilanci;
	}
	/**
	 * SinteticaBIlanciAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaBilancio(SinteticaBilancioForm sintBilanci,HttpServletRequest request)
	{

		try {
			List<ListaSuppBilancioVO> ricerca=new ArrayList();
			ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
			String [] listaSelezionati=sintBilanci.getSelectedBilanci();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sintBilanci.getListaBilanci().size(); j++)
				{
					BilancioVO eleElenco=sintBilanci.getListaBilanci().get(j);
					String chiaveComposta=eleElenco.getChiave();
					//chiaveComposta[3] codOrdine
					//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{

						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=eleElenco.getCodBibl();
						String ese=eleElenco.getEsercizio();
						String cap=eleElenco.getCapitolo();
						//String imp=eleElenco.getImpegno();
						String imp="";
						String chiama=null;
						String ordina="";

						eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
						if (sintBilanci.getOrdinamentoScelto()!=null && sintBilanci.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sintBilanci.getOrdinamentoScelto());
						}
						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaBilancio", ricerca);
		} catch (Exception e) {
		}
	}

	private void  PreparaRicercaBilancioSingle(SinteticaBilancioForm sintBilanci,HttpServletRequest request, int j)
	{
		try {
					List<ListaSuppBilancioVO> ricerca=new ArrayList();
					ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
					BilancioVO eleElenco=sintBilanci.getListaBilanci().get(j);
					// carica i criteri di ricerca da passare alla esamina
					String polo=Navigation.getInstance(request).getUtente().getCodPolo();
					String codP=polo;
					String codB=eleElenco.getCodBibl();
					String ese=eleElenco.getEsercizio();
					String cap=eleElenco.getCapitolo();
					//String imp=eleElenco.getImpegno();
					String imp="";
					String chiama=null;
					String ordina="";
					eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
					if (sintBilanci.getOrdinamentoScelto()!=null && sintBilanci.getOrdinamentoScelto().trim().length()>0 )
					{
						eleRicerca.setOrdinamento(sintBilanci.getOrdinamentoScelto());
					}
					ricerca.add(eleRicerca);
					request.getSession().setAttribute("criteriRicercaBilancio", ricerca);
		} catch (Exception e) {
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BILANCIO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return false;
	}


	/**
	 * SinteticaBilanciAction.java
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

		appoSelezione=(String[]) sintBilanci.getSelectedBilanci();
		appoParametro=(String[])request.getSession().getAttribute("bilanciSelected");
		//int nRighe=sintBilanci.getNumRigheOld();
		//int nPos=sintBilanci.getPosElementoOld();
		//List <BilancioVO> listaOld=sintBilanci.getBilanciVisualizzatiOld();
		List <BilancioVO> listaOld=new ArrayList();
		if (sintBilanci.getBilanciVisualizzatiOld()!=null && sintBilanci.getBilanciVisualizzatiOld().size()!=0 )
		{
			listaOld=sintBilanci.getBilanciVisualizzatiOld();
		}
		else
		{
			listaOld=sintBilanci.getBilanciVisualizzati();
		}

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			//sintBilanci.getListaBilanci()
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione,listaOld );
			request.getSession().setAttribute("bilanciSelected", (String[])nuovoParametro);
			sintBilanci.setSelectedBilanci((String[]) nuovoParametro );
		}

	}*/
	/**
	 * SinteticaBilanciAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<BilancioVO> lista)
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
				BilancioVO bil= (BilancioVO) lista.get(x);
				cod=bil.getChiave();
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
	}	*/

}
