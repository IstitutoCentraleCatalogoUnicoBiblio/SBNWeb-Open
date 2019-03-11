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
package it.iccu.sbn.web.actions.acquisizioni.buoniordine;


import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO.TipoOperazione;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.SinteticaBuonoOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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

public class SinteticaBuonoOrdineAction extends AcquisizioniBaseAction implements SbnAttivitaChecker{

	//private SinteticaBuonoOrdineForm sintBuoniOrd;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
	//	map.put("ricerca.button.caricaBlocco","carica");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.crea","crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "ok");
		map.put("ricerca.button.stampa","stampaOnLine");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBuonoOrdineForm currentForm = (SinteticaBuonoOrdineForm ) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("buoniSelected")!= null && !request.getSession().getAttribute("buoniSelected").equals(""))
				{
					currentForm.setSelectedBuoni((String[]) request.getSession().getAttribute("buoniSelected"));
				}

				return mapping.getInputForward();
			}
			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			ListaSuppBuoniOrdineVO ricerca =(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			if (ricerca!=null)
			{
				currentForm.setParametri(ricerca);
				currentForm.setOrdinamentoScelto(ricerca.getOrdinamento());
			}

			if (ricerca!=null &&  ricerca.getSelezioniChiamato()==null && ricerca.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				currentForm.setVisibilitaIndietroLS(true);
				// per il layout
				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					currentForm.setLSRicerca(true); // fai rox 4
				//}

			}
			if (ricerca==null)
			{
				// l'attributo di sessione deve essere valorizzato
				currentForm.setRisultatiPresenti(false);
			}

			List<BuoniOrdineVO> listaBuoni=new ArrayList();
			// deve essere escluso il caso di richiamo di lista supporto cambi
	/*		if 	(request.getSession().getAttribute("listaBOEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaBuoni=(List<BuoniOrdineVO>)request.getSession().getAttribute("listaBOEmessa");
			}
			else
			{
				listaBuoni=this.getListaBuoniOrdineVO(ricArr ); // va in errore se non ha risultati
			}
	*/
			if (ricerca!=null)
			{
				listaBuoni=this.getListaBuoniOrdineVO(ricerca ); // va in errore se non ha risultati
			}

			//this.loadFornitori();
			currentForm.setListaBuoniOrdine(listaBuoni);
			currentForm.setNumBuoni(currentForm.getListaBuoniOrdine().size());

			// gestione automatismo check su unico elemento lista
			if (currentForm.getListaBuoniOrdine().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=currentForm.getListaBuoniOrdine().get(0).getChiave();
				//	"FI|2007|3";
				currentForm.setSelectedBuoni(appoSelProva);
			}


			// gestione blocchi

			DescrittoreBloccoVO blocco1= null;
			//String ticket=Navigation.getInstance(request).getUserTicket();
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String ticket=utenteCollegato.getTicket();

			int maxElementiBlocco=10;
			int maxRighe=10;

			if (ricerca.getElemXBlocchi()>0)
			{
				maxElementiBlocco=ricerca.getElemXBlocchi();
				maxRighe=ricerca.getElemXBlocchi();
			}

			// ok blocco1=GestioneAcquisizioniBean.class.newInstance().gestBlock(ticket,listaOfferte,prova);
			//blocco1=SbnBusinessSessionBean.class.newInstance().saveBlocchi(ticket,listaOfferte,listaOfferte.size());
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// deve essere escluso il caso di richiamo di lista supporto offerte fornitore
			if 	(request.getSession().getAttribute("ultimoBloccoBO")!=null && ((ricerca==null) || (ricerca!=null && ricerca.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoBO");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{

				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaBuoni,maxElementiBlocco);
				currentForm.setListaBuoniOrdine(blocco1.getLista());
			}

			if (blocco1 != null)
			{
			//if (blocco1 == null)
			//abilito i tasti per il blocco se necessario
			//memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista(blocco1.getIdLista()); //si
			currentForm.setTotRighe(blocco1.getTotRighe()); //no
			currentForm.setTotBlocchi(blocco1.getTotBlocchi()); //no
			currentForm.setMaxRighe(blocco1.getMaxRighe()); //no
			//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
			//sinOfferte.setNumRighe(2);
			//sinOfferte.setNumBlocco(1);
			currentForm.setUltimoBlocco(blocco1);
			currentForm.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi

			// non trovati
			if (currentForm.getNumBuoni()==0)
			{

				return mapping.getInputForward();

				//return mapping.findForward("indietro");

			}
			else {
				// imposto con la var impostata su ricerca
/*				if (request.getAttribute("numElexBlocchi")!=null)
				{
					sintBuoniOrd.setNumRighe((Integer)request.getAttribute("numElexBlocchi"));
				}
				// controllare che numrighe non sia superiore alla dimensione dell'arraylist listabilanci
				int totPagine=(int) Math.round((sintBuoniOrd.getNumBuoni()/sintBuoniOrd.getNumRighe()));
				int resto=(int) sintBuoniOrd.getNumBuoni()%sintBuoniOrd.getNumRighe();
				if (resto > 0)
				{
					totPagine=totPagine+1;
				}
				sintBuoniOrd.setTotPagine(totPagine);

				if (sintBuoniOrd.getNumRighe()>sintBuoniOrd.getListaBuoniOrdine().size())
				{
					sintBuoniOrd.setNumRighe(sintBuoniOrd.getListaBuoniOrdine().size());
				}
				//this.sintBilanci.setBilanciVisualizzati(this.sintBilanci.getListaBilanci().subList(0,sintBilanci.getNumRighe()));
				if (sintBuoniOrd.getBuoniVisualizzati()==null )
				{
					sintBuoniOrd.setBuoniVisualizzati(new ArrayList<BuoniOrdineVO>());
					int start=0;
					int end=sintBuoniOrd.getNumRighe();
					for (int j=0;  j < end; j++)
					{
						BuoniOrdineVO ele=sintBuoniOrd.getListaBuoniOrdine().get(j);
						sintBuoniOrd.getBuoniVisualizzati().add(ele);
					}
					// inizializzazione blocchi
					int totblck=sintBuoniOrd.getTotPagine();
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
		            	if (seqBlock[i].getInizio()+sintBuoniOrd.getNumRighe()<=sintBuoniOrd.getListaBuoniOrdine().size())
		            	{
			            	seqBlock[i].setFine(seqBlock[i].getInizio()+sintBuoniOrd.getNumRighe());
		            	}
		            	else
		            	{
			            	seqBlock[i].setFine(sintBuoniOrd.getListaBuoniOrdine().size());
		            	}

		            }
		            sintBuoniOrd.setSequenzablocchi(seqBlock);
					// fine inizializzazione blocchi

				}

				// posizionamento blocco successivo
				int blckSucc=sintBuoniOrd.getNumPagina();
				if (sintBuoniOrd.getSequenzablocchi().length>0)
				{
					for (int x=blckSucc; x<sintBuoniOrd.getSequenzablocchi().length; x++)
					{
						if (!sintBuoniOrd.getSequenzablocchi()[x].isCaricato())
						{
							blckSucc=x+1;
							break;
						}
					}

				}
				sintBuoniOrd.setNumPagina(blckSucc);*/
/*				if (blckSucc<= sintBilanci.getTotPagine() && blckSucc>0)
				{
					sintBilanci.setNumPagina(blckSucc);
				}
*/
				if 	(request.getSession().getAttribute("buoniSelected")!= null)
				{
					currentForm.setSelectedBuoni((String[]) request.getSession().getAttribute("buoniSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricerca.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricerca.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricerca.getSelezioniChiamato().get(t).getChiave();
						for (int v=0;  v < currentForm.getListaBuoniOrdine().size(); v++)
						{
							String eleList= currentForm.getListaBuoniOrdine().get(v).getChiave();
							if (eleList.equals(eleSele))
							{
								String variato=ricerca.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									currentForm.getListaBuoniOrdine().get(v).setTipoVariazione(variato);
								}
								break;
							}
						}
					}
				}

			}
			return mapping.getInputForward();

		}	catch (ValidationException ve) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;

				if (ve.getError()==4001)
				{
					currentForm.setRisultatiPresenti(false);
				}

				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			e.printStackTrace();


			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward stampaOnLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			SinteticaBuonoOrdineForm sintBuoniOrd = (SinteticaBuonoOrdineForm ) form;

			if (sintBuoniOrd.getSelectedBuoni()==null || sintBuoniOrd.getSelectedBuoni().length==0) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

				return mapping.getInputForward();
			} else {



				List<Integer> listaIDBuoniOrdine = new ArrayList<Integer> ();

				for (int v=0; v< sintBuoniOrd.getSelectedBuoni().length; v++)
				{
					for (int u=0; u<sintBuoniOrd.getListaBuoniOrdine().size(); u++)
					{
						if ( sintBuoniOrd.getSelectedBuoni()[v].equals(sintBuoniOrd.getListaBuoniOrdine().get(u).getChiave()))
						  {
							listaIDBuoniOrdine.add(sintBuoniOrd.getListaBuoniOrdine().get(u).getIDBuonoOrd());
							break;
						  }
					}
				}

				ListaSuppBuoniOrdineVO crit2=new ListaSuppBuoniOrdineVO();
				crit2.setIdBuoOrdList(listaIDBuoniOrdine);
				crit2.setOrdinamento(""); // ordinamento di default
				List<BuoniOrdineVO> provaElencoOrdinato=getListaBuoniOrdineVO(crit2);



                // aggiunta per la lettura dinamica integrale della configurazione senza impostazioni statiche
				ConfigurazioneBOVO confBOnew=new ConfigurazioneBOVO();
				ListaSuppBuoniOrdineVO ricArr=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String cBiblionew=ricArr.getCodBibl();
				if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().length()>0)
				{
					biblio=ricArr.getCodBibl();
				}
				confBOnew.setCodBibl(biblio);
				confBOnew.setCodPolo(polo);


				ConfigurazioneBOVO confLettonew=this.loadConfigurazione(confBOnew);

				if (confLettonew!=null)
				{
					confBOnew=confLettonew;
				}
				confBOnew.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				// fine aggiunta

				// le due istruzioni seguenti servono al sw di stampa per non andare in errore per l'assenza di voci per tipo ordine/lingua
				//confBOnew.setTestoOggetto(strTestoOggetto);
                //confBOnew.setTestoIntroduttivo(strTestoIntroduttivo);


                StampaBuonoOrdineVO stampaOL=new StampaBuonoOrdineVO ();
				stampaOL.setCodBibl(biblio);
				stampaOL.setCodPolo(polo);

                stampaOL.setConfigurazione(confBOnew);
				//stampaOL.setConfigurazione(confBO);
				//stampaOL.setListaOrdiniDaStampare(listaOrdiniSel);
				stampaOL.setListaOrdiniDaStampare(null);
				stampaOL.setListaBuoniOrdineDaStampare(provaElencoOrdinato);
				stampaOL.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
				stampaOL.setTicket(Navigation.getInstance(request).getUserTicket());



					//gestione del cambio di stato del flag stampato
					List listaFiltrata=this.stampato(provaElencoOrdinato,"BUO","");

 					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
 					List risultatoPerStampa=factory.getGestioneAcquisizioni().impostaBuoniOrdineDaStampare( confBOnew, listaFiltrata, "BUO", false,  Navigation.getInstance(request).getUserTicket(), Navigation.getInstance(request).getUtente().getFirmaUtente(),  confBOnew.getDenoBibl());

 					listaFiltrata=risultatoPerStampa;

					if (listaFiltrata!=null && listaFiltrata.size()>0)
					{
						stampaOL.setListaBuoniOrdineDaStampare(listaFiltrata);
 	 					// test su memorizzazione flag stampato	SPOSTATO IN STAMPAONLINEACTION
	 					// boolean risultato=this.StampaOrdini(provaElencoOrdinato,null,"BUO",confBOnew.getUtente(),"");
 						boolean risultato=true;

						if (!risultato)
						{

							// cambiare messaggio
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoBuoniOrdineNonStampati"));

							return mapping.getInputForward();
						}
						else
						{
							// DA RIPRISTINARE PER LA STAMPA (almaviva)
 							//TODO GVCANCE
 							request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_BUONI_ORDINE);
 							//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
 							request.setAttribute("DATI_STAMPE_ON_LINE", listaFiltrata);
 							return mapping.findForward("stampaOL");

							//request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_BUONI_ORDINE); // aggiungere stampa_lista_ordini
							//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
							//return mapping.findForward("stampaOL");
							//return mapping.getInputForward();
						}
					}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoBuoniOrdineNonStampati"));

					return mapping.getInputForward();
				}
		}

		//return mapping.getInputForward();

	} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			return mapping.getInputForward();
		}

	}



	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBuonoOrdineForm currentForm = (SinteticaBuonoOrdineForm) form;
		int idx = currentForm.getProgrForm();
		if (idx > 0) {
			try {
				this.preparaRicercaBuonoSingle(currentForm.getListaBuoniOrdine(), request, idx - 1);
				//almaviva5_20140624 #4631
				ListaSuppBuoniOrdineVO ricerca = ((List<ListaSuppBuoniOrdineVO>) request
						.getSession().getAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE)).get(0);
				if (currentForm.getParametri().getTipoOperazione() == TipoOperazione.SCEGLI_DA_ORDINE)
					ricerca.setTipoOperazione(TipoOperazione.ESAMINA_DA_ORDINE);
				return mapping.findForward("esamina");

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBuonoOrdineForm sintBuoniOrd = (SinteticaBuonoOrdineForm ) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sintBuoniOrd.isSessione()) {
			sintBuoniOrd.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sintBuoniOrd.getBloccoSelezionato();
		String idLista = sintBuoniOrd.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sintBuoniOrd.getListaBuoniOrdine().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoBO",bloccoSucc);
				sintBuoniOrd.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoBO",sintBuoniOrd.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}



	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBuonoOrdineForm sintBuoniOrd = (SinteticaBuonoOrdineForm ) form;
		try {

			sintBuoniOrd.setNumBuoni(sintBuoniOrd.getListaBuoniOrdine().size());
			// aggiungo 0.5 per arrotondarlo al intero superiore
			// controllare che il numero delle righe non sia il size perchè totpag viene =2
			int totPagine= (int)Math.round((sintBuoniOrd.getNumBuoni()/sintBuoniOrd.getNumRighe()));
			// resto della divisione
			int resto=(int) sintBuoniOrd.getNumBuoni()%sintBuoniOrd.getNumRighe();
			if (resto > 0)
			{
				totPagine=totPagine+1;
			}

			sintBuoniOrd.setTotPagine(totPagine);
			// la posizione dell'elemento nella lista si conteggia da zero per cui non aggiungo una unità
			sintBuoniOrd.setPosElemento((sintBuoniOrd.getNumRighe())*(sintBuoniOrd.getNumPagina()-1));

			// occorre controllare che non sia già stato caricato

			if (sintBuoniOrd.getNumPagina()>0 && sintBuoniOrd.getNumPagina()<=sintBuoniOrd.getTotPagine())
			{
				if (!sintBuoniOrd.getSequenzablocchi()[sintBuoniOrd.getNumPagina()-1].isCaricato())
				{
					int start=sintBuoniOrd.getSequenzablocchi()[sintBuoniOrd.getNumPagina()-1].getInizio();
					int end=sintBuoniOrd.getSequenzablocchi()[sintBuoniOrd.getNumPagina()-1].getFine();
					//this.sintBilanci.getBilanciVisualizzati().addAll(this.sintBilanci.getBilanciVisualizzati().size(),this.sintBilanci.getListaBilanci().subList(start,end));
					for (int j=start;  j < end; j++)
					{
						BuoniOrdineVO ele=sintBuoniOrd.getListaBuoniOrdine().get(j);
						sintBuoniOrd.getBuoniVisualizzati().add(ele);
					}
					sintBuoniOrd.getSequenzablocchi()[sintBuoniOrd.getNumPagina()-1].setCaricato(true);
					// posizionamento blocco successivo

					int blckSucc=sintBilanci.getNumPagina()+1;
					if (blckSucc<= sintBilanci.getTotPagine() && blckSucc>0)
					{
						sintBilanci.setNumPagina(blckSucc);
					}
					int blckSucc=sintBuoniOrd.getNumPagina();
					if (sintBuoniOrd.getSequenzablocchi().length>0)
					{
						for (int x=blckSucc; x<sintBuoniOrd.getSequenzablocchi().length; x++)
						{
							if (!sintBuoniOrd.getSequenzablocchi()[x].isCaricato())
							{
								blckSucc=x+1;
								break;
							}
						}

					}
					sintBuoniOrd.setNumPagina(blckSucc);

				}
				else
				{
					// blocco già caricato
				}

			}
			this.AggiornaParametriSintetica(request);
			// errore
			if (Integer.valueOf(sintBuoniOrd.getNumPagina())>totPagine)
			{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.errorePagina"));

				sintBuoniOrd.setBuoniVisualizzatiOld(sintBuoniOrd.getBuoniVisualizzati());
				// reimposto la paginazione a quella precedente all'errore
				//sintBuoniOrd.setNumRighe(sintBuoniOrd.getNumRigheOld());
				//sintBuoniOrd.setPosElemento(sintBuoniOrd.getPosElementoOld());
				//sintBuoniOrd.setNumPagina(sintBuoniOrd.getNumPaginaOld());
				return mapping.getInputForward();

			}

			//sintBuoniOrd.setNumRigheOld(sintBuoniOrd.getNumRighe());
			//sintBuoniOrd.setPosElementoOld(sintBuoniOrd.getPosElemento());
			sintBuoniOrd.setBuoniVisualizzatiOld(sintBuoniOrd.getBuoniVisualizzati());

			return mapping.getInputForward();
		} catch (Exception e) {
			e.printStackTrace();

			return mapping.getInputForward();
		}
	}
	*/

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBuoniOrdineVO ricArr=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
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
		SinteticaBuonoOrdineForm currentForm = (SinteticaBuonoOrdineForm ) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBuoniOrdineVO ricerca = (ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			if (ricerca != null) {
					// gestione del chiamante
				if (ricerca.getChiamante() != null)	{
					// carico i risultati della selezione nella variabile da restituire
					String[] appoSelezione = currentForm.getSelectedBuoni();
					String[] appoParametro = (String[])request.getSession().getAttribute("buoniSelected");

					//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
					//{
					//	this.AggiornaParametriSintetica(request);
						ricerca = this.aggiornaRisultatiListaSupporto(currentForm, ricerca);
						request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE, ricerca);
					//}
				}

				request.setAttribute(NavigazioneAcquisizioni.BUONO_ORDINE_SELEZIONATO, ricerca.getSelezioniChiamato().get(0));
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(ricerca.getChiamante()+".do");
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
	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBuonoOrdineForm sintBuoniOrd = (SinteticaBuonoOrdineForm ) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sintBuoniOrd.isSessione())
			{
				sintBuoniOrd.setSessione(true);
			}
		}
		try {
			sintBuoniOrd.setSelectedBuoni(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBuonoOrdineForm sintBuoniOrd = (SinteticaBuonoOrdineForm ) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			if(!sintBuoniOrd.isSessione())
			{
				sintBuoniOrd.setSessione(true);
			}
		}
		String [] appoLista= new String [sintBuoniOrd.getListaBuoniOrdine().size()];
		int i;
		for (i=0;  i < sintBuoniOrd.getListaBuoniOrdine().size(); i++)
		{
			BuoniOrdineVO buo= sintBuoniOrd.getListaBuoniOrdine().get(i);
			String cod=buo.getChiave().trim();
			appoLista[i]=cod;
		}
		sintBuoniOrd.setSelectedBuoni(appoLista);
		return mapping.getInputForward();
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaBuonoOrdineForm currentForm = (SinteticaBuonoOrdineForm ) form;
		try {
			String[] selected = currentForm.getSelectedBuoni();
			if (ValidazioneDati.isFilled(selected) ) {
				//this.AggiornaParametriSintetica(request);
				this.preparaRicercaBuono( currentForm,request);
				// si aggiorna l'attributo con l'elenco dei cambi trovati
				request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE, this.aggiornaRisultatiListaSupporto( currentForm, (ListaSuppBuoniOrdineVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE)));
				request.getSession().setAttribute("buoniSelected", selected);
				request.getSession().setAttribute("listaBOEmessa", currentForm.getListaBuoniOrdine());
				return mapping.findForward("esamina");
			}
			else
			{

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private List<BuoniOrdineVO> getListaBuoniOrdineVO(ListaSuppBuoniOrdineVO criRicerca) throws Exception
	{
	List<BuoniOrdineVO> listaBuoniOrdine;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaBuoniOrdine = factory.getGestioneAcquisizioni().getRicercaListaBuoniOrd(criRicerca);
	return listaBuoniOrdine;
	}

	/**
	 * SinteticaBuonoOrdineAction.java
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

		appoSelezione=(String[]) sintBuoniOrd.getSelectedBuoni();
		appoParametro=(String[])request.getSession().getAttribute("buoniSelected");
		//int nRighe=this.sintBilanci.getNumRigheOld();
		//int nPos=this.sintBilanci.getPosElementoOld();
			//List <BuoniOrdineVO> listaOld=sintBuoniOrd.getBuoniVisualizzatiOld();
		List <BuoniOrdineVO> listaOld=new ArrayList();
		if (sintBuoniOrd.getBuoniVisualizzatiOld()!=null && sintBuoniOrd.getBuoniVisualizzatiOld().size()!=0 )
		{
			listaOld=sintBuoniOrd.getBuoniVisualizzatiOld();
		}
		else
		{
			listaOld=sintBuoniOrd.getBuoniVisualizzati();
		}

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			//this.sintBilanci.getListaBilanci()
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione,listaOld );
			request.getSession().setAttribute("buoniSelected", (String[])nuovoParametro);
			sintBuoniOrd.setSelectedBuoni((String[]) nuovoParametro );
		}

	}
*/
	/**
	 * SinteticaBuonoOrdineAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void preparaRicercaBuono(SinteticaBuonoOrdineForm currentForm, HttpServletRequest request)
	{
		try {
			List<ListaSuppBuoniOrdineVO> ricerca=new ArrayList();
			ListaSuppBuoniOrdineVO eleRicerca=new ListaSuppBuoniOrdineVO();
			String [] listaSelezionati=currentForm.getSelectedBuoni();
			String ticket=Navigation.getInstance(request).getUserTicket();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < currentForm.getListaBuoniOrdine().size(); j++)
				{
					BuoniOrdineVO eleElenco=currentForm.getListaBuoniOrdine().get(j);
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
						String numDa=eleElenco.getNumBuonoOrdine();
						String numA=eleElenco.getNumBuonoOrdine();
						String dataDa=eleElenco.getDataBuonoOrdine();
						String dataA=eleElenco.getDataBuonoOrdine();
						String stato="";
						//StrutturaTerna ord=eleElenco.getListaOrdiniBuono();
						//StrutturaCombo forn=eleElenco.getFornitore();
						//StrutturaTerna bil=eleElenco.getBilancio();
						StrutturaTerna ord=new StrutturaTerna("","","");
						StrutturaCombo forn=new StrutturaCombo("","");
						StrutturaTerna bil=new StrutturaTerna("","","");
						String chiama=null;
						String ordina="";

						eleRicerca=new ListaSuppBuoniOrdineVO(codP, codB, numDa, numA, dataDa, dataA, stato, ord, forn, bil, chiama, ordina );
						if (currentForm.getOrdinamentoScelto()!=null && currentForm.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(currentForm.getOrdinamentoScelto());
						}
						// aggiorna ticket
						eleRicerca.setTicket(ticket);
						ricerca.add(eleRicerca);

						//almaviva5_20140624 #4631
						if (currentForm.getParametri().getTipoOperazione() == TipoOperazione.SCEGLI_DA_ORDINE)
							eleRicerca.setTipoOperazione(TipoOperazione.ESAMINA_DA_ORDINE);
					}
				}
			}
			request.getSession().setAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE, ricerca);
		} catch (Exception e) {
		}
	}

	/**
	 * SinteticaBuonoOrdineAction.java
	 * @param ricerca
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppBuoniOrdineVO aggiornaRisultatiListaSupporto(SinteticaBuonoOrdineForm currentForm, ListaSuppBuoniOrdineVO ricerca)
	{
		try {
			List<BuoniOrdineVO> risultati = new ArrayList<BuoniOrdineVO>();

			String[] keys = currentForm.getSelectedBuoni();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < keys.length; i++) {
				String key = keys[i];
				List<BuoniOrdineVO> buoni = currentForm.getListaBuoniOrdine();
				int size = buoni.size();
				for (int j=0;  j < size; j++) {
					BuoniOrdineVO selected = buoni.get(j);
					if (key.equals(selected.getChiave().trim()))
						risultati.add(selected);
				}
			}

			ricerca.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return ricerca;
	}
	/**
	 * SinteticaBuonoOrdineAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<BuoniOrdineVO> lista)
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
				BuoniOrdineVO buo= (BuoniOrdineVO) lista.get(x);
				cod=buo.getChiave().trim();
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
/*	private void loadBuoniOrdine() throws Exception {
		List lista = new ArrayList();
		BuoniOrdineVO buoni = new BuoniOrdineVO("X10", "01","00000001","16/01/2006","S",  new StrutturaCombo("13", "Libreria Feltrinelli"), new StrutturaTerna("2006", "1", ""),null,null );
		lista.add(buoni);
		buoni = new BuoniOrdineVO("X10", "01","00000003","07/02/2006","S",  new StrutturaCombo("13", "Libreria Feltrinelli"), new StrutturaTerna("2006", "1", ""),null,null );
		lista.add(buoni);
		buoni = new BuoniOrdineVO("X10", "01","00000007","06/02/2006","S",  new StrutturaCombo("33", "Libreria Grande"), new StrutturaTerna("2006", "1", ""),null,null );
		lista.add(buoni);
		buoni = new BuoniOrdineVO("X10", "01","00000008","06/02/2006","S",  new StrutturaCombo("36", "Libreria Feltrinelli di Palermo"), new StrutturaTerna("2006", "1", ""),null,null );
		lista.add(buoni);
		sintBuoniOrd.setListaBuoniOrdine(lista);
	}
*/
/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {


		SinteticaBuonoOrdineForm sintBuoniOrd = (SinteticaBuonoOrdineForm ) form;
		this.loadBuoniOrdine();
		sintBuoniOrd.setNumBuoni(sintBuoniOrd.getListaBuoniOrdine().size());


		if (request.getParameter("indietro0") != null) {
			return mapping.findForward("indietro");
		}

		if (request.getParameter("PROVA").equals(1))
		{

		}


		if (request.getParameter("esamina0") != null) {
			if (sintBuoniOrd.getSelectedBuoni().length!=0)
			{
				return mapping.findForward("esamina");
			}
			else
			{

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.ricerca"));


			}
		}
		if (request.getParameter("selTutti0") != null)
		{
			String [] appoLista= new String [sintBuoniOrd.getListaBuoniOrdine().size()];
			int i;
			for (i=0;  i < sintBuoniOrd.getListaBuoniOrdine().size(); i++)
			{
				BuoniOrdineVO buoni= (BuoniOrdineVO) sintBuoniOrd.getListaBuoniOrdine().get(i);
				String cod=buoni.getNumBuonoOrdine();
				appoLista[i]=cod;
			}
			sintBuoniOrd.setSelectedBuoni(appoLista);
			return mapping.getInputForward();
		}
		if (request.getParameter("deselTutti0") != null) {
			//return mapping.findForward("controllaordine");
			sintBuoniOrd.setSelectedBuoni(null);
			return mapping.getInputForward();
		}

        return mapping.getInputForward();
    }
*/
	private ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneBOVO configurazioneTrovata = new ConfigurazioneBOVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazione(configurazione);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		return configurazioneTrovata;
	}

	private List stampato(List listaOggetti, String tipoOggetti,  String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List ris = factory.getGestioneAcquisizioni().gestioneStampato( listaOggetti,  tipoOggetti, bo);
		return ris;
	}

	private Boolean stampaOrdini(List listaOggetti, List idList, String tipoOggetti, String utente, String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		Boolean ris = factory.getGestioneAcquisizioni().gestioneStampaOrdini( listaOggetti,  idList,  tipoOggetti,  utente,  bo);
		return ris;
	}



	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		SinteticaBuonoOrdineForm currentForm = (SinteticaBuonoOrdineForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();

		try {
			if (idCheck.equals("CREA") ) {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BUONI_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			}

			if (idCheck.equals("SCEGLI") ) {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BUONI_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);
				return currentForm.getParametri().getTipoOperazione() == TipoOperazione.SCEGLI_DA_ORDINE
						&& ValidazioneDati.isFilled(currentForm.getListaBuoniOrdine());
			}
		} catch (Exception e) {	}

		return false;

	}
}
