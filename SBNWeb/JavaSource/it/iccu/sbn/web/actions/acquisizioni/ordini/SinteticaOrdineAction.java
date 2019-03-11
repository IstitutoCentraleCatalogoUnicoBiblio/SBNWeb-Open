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
 package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.SinteticaOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
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
import org.hamcrest.Matchers;

import ch.lambdaj.Lambda;

public class SinteticaOrdineAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.operazionesuordine","operazionesuordine");
		map.put("ricerca.button.controllaordine","controllaordine");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		map.put("ordine.label.tabTipo","criterioTipo");
		map.put("ordine.label.tabNum","criterioNum");
		map.put("buono.label.dataBuono","criterioData");
		map.put("buono.label.tabStato","criterioStato");
		map.put("ordine.label.fornitore","criterioForn");
		map.put("buono.label.tabBilancio","criterioBil");
		//map.put("ricerca.button.carica","carica");
		map.put("ricerca.button.scegli","scegli");
		map.put("servizi.bottone.blocco", "blocco");
		map.put("ricerca.button.crea","crea");

		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "ok");
		map.put("ricerca.button.stampa","stampaOnLine");
		map.put("ricerca.button.listaInventariOrdine","listaInventariOrdine");
		map.put("ricerca.button.stampaBollettario","stampaBollettario");

		return map;
	}

		public ActionForward stampaOnLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			try {
				SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
				Navigation navi = Navigation.getInstance(request);
				if (navi.isFromBar() )
			        return mapping.getInputForward();

				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = navi.getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);

				}   catch (UtenteNotAuthorizedException e) {

					LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

					return mapping.getInputForward();
				}

				if (currentForm.getSelectedOrdini()==null || currentForm.getSelectedOrdini().length==0) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

					return mapping.getInputForward();
				} else {

/*					List<ListaSuppOrdiniVO> ricerca=new ArrayList();
					this.PreparaRicercaOrdine( sintordini, request);
					ricerca=(List<ListaSuppOrdiniVO>) request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
					List<OrdiniVO> listaOrdiniSel = null;

					if (ricerca!=null &&  ricerca.size()>0)
					{
						listaOrdiniSel = new ArrayList<OrdiniVO> ();
						for (int j=0;  j < ricerca.size(); j++)
						{
							List<OrdiniVO> ordineSel=new ArrayList<OrdiniVO>();
							ListaSuppOrdiniVO crit=ricerca.get(j);
							crit.setOrdinamento("7"); // ordinamento per cod forn e tipo ordi
							ordineSel=getListaOrdiniVO(crit);
							listaOrdiniSel.add(ordineSel.get(0));
						}
					}*/


					List<Integer> listaIDOrdini = new ArrayList<Integer> ();

					for (int v=0; v< currentForm.getSelectedOrdini().length; v++)
					{
						for (int u=0; u<currentForm.getListaOrdini().size(); u++)
						{
							if (currentForm.getSelectedOrdini()[v].equals(currentForm.getListaOrdini().get(u).getChiave()))
							  {
								listaIDOrdini.add(currentForm.getListaOrdini().get(u).getIDOrd());
								break;
							  }
						}
					}
					ListaSuppOrdiniVO crit2=new ListaSuppOrdiniVO();
					crit2.setIdOrdList(listaIDOrdini);
					crit2.setOrdinamento("7"); // ordinamento per cod forn e tipo ordi
					List<OrdiniVO> provaElencoOrdinato=getListaOrdiniVO(crit2);

					ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);

					// List<OrdiniVO> lst
					//List<OrdiniVO> provaElencoOrdinato=(List <OrdiniVO>)this.ElencaPer( listaOrdiniSel, sintordini,"forn");
/*					ConfigurazioneBOVO confBO=new ConfigurazioneBOVO("", "", true,loadIntest(), loadFineStp());
					String biblio=Navigation.getInstance(request).getUtente().getCodBib();
					String cBiblio=ricArr.getCodBibl();
					confBO.setCodBibl(cBiblio);
					String polo=Navigation.getInstance(request).getUtente().getCodPolo();
					confBO.setCodPolo(polo);
					ConfigurazioneBOVO confLetto=this.loadConfigurazione(confBO);

					if (confLetto!=null)
					{
						confBO=confLetto;
					}
					confBO.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
					String[] strAreeIsbd=new String[7];
					strAreeIsbd[0]="1";
					strAreeIsbd[1]="0";
					strAreeIsbd[2]="0";
					strAreeIsbd[3]="0";
					strAreeIsbd[4]="0";
					strAreeIsbd[5]="0";
					strAreeIsbd[6]="0";
					confBO.setAreeIsbd(strAreeIsbd);

					confBO.setLinguaIT(true);
					confBO.setListaDatiFineStampaEng(null);
					confBO.setListaDatiIntestazioneEng(null);

					StrutturaCombo[] strTestoOggetto=new StrutturaCombo[6];
                    strTestoOggetto[0]=new StrutturaCombo("A", "Buono ordine (acquisto)");
                    strTestoOggetto[1]=new StrutturaCombo("V", "Buono ordine (visione trattenuta)");
                    strTestoOggetto[2]=new StrutturaCombo("L", "Buono ordine (deposito legale)");
                    strTestoOggetto[3]=new StrutturaCombo("D", "Buono ordine (dono)");
                    strTestoOggetto[4]=new StrutturaCombo("C", "Buono ordine (cambio)");
                    strTestoOggetto[5]=new StrutturaCombo("R", "Buono ordine (rilegatura)");

                    confBO.setTestoOggetto(strTestoOggetto);
                    confBO.setTestoOggettoEng(null);

					StrutturaCombo[] strTestoIntroduttivo=new StrutturaCombo[6];
					strTestoIntroduttivo[0]=new StrutturaCombo("A", "Vi preghiamo di fornire il materiale di seguito elencato: (acquisto)");
					strTestoIntroduttivo[1]=new StrutturaCombo("V", "testo introduttivo (visione trattenuta)");
					strTestoIntroduttivo[2]=new StrutturaCombo("L", "testo introduttivo (deposito legale)");
					strTestoIntroduttivo[3]=new StrutturaCombo("D", "Vi ringraziamo del materiale ricevuto di seguito elencato: (dono)");
					strTestoIntroduttivo[4]=new StrutturaCombo("C", "testo introduttivo (cambio)");
					strTestoIntroduttivo[5]=new StrutturaCombo("R", "testo introduttivo (rilegatura)");


                    confBO.setTestoIntroduttivo(strTestoIntroduttivo);
                    confBO.setTestoIntroduttivoEng(null);

                    confBO.setPresenzaLogoImg(true);
                    confBO.setNomeLogoImg("logo.jpg");

                    confBO.setPresenzaFirmaImg(false);
                    confBO.setNomeFirmaImg(null);

                    confBO.setPresenzaPrezzo(true);

                    confBO.setIndicaRistampa(true);

                    confBO.setRinnovoOrigine(null); //  StrutturaTerna[]
                    confBO.setTipoRinnovo("N");   // O=originario, P=precedente, N=nessuno
*/
                    // aggiunta per la lettura dinamica integrale della configurazione senza impostazioni statiche

					ConfigurazioneBOVO confBOnew=new ConfigurazioneBOVO();
					String biblio=utente.getCodBib();
					String polo=utente.getCodPolo();
					String cBiblionew=ricArr.getCodBibl();
					if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().length()>0)
					{
						biblio=ricArr.getCodBibl();
					}
					confBOnew.setCodBibl(biblio);
					confBOnew.setCodPolo(polo);
					ConfigurazioneBOVO confLettonew=this.loadConfigurazione(confBOnew);

/*					if (confLettonew.getTestoIntroduttivo().length>0 && confLettonew.getTestoIntroduttivo().length<7 )
					{
						for (int i=0;  i<6; i++)
						{
							StrutturaCombo ele=confLettonew.getTestoIntroduttivo()[i];
							if (ele==null)
							{
								confLettonew.getTestoIntroduttivo()[i]=new StrutturaCombo("","");
							}

						}
					}

					if (confLettonew.getTestoIntroduttivoEng().length>0 && confLettonew.getTestoIntroduttivoEng().length<7 )
					{
						for (int i=0;  i<6; i++)
						{
							StrutturaCombo ele=confLettonew.getTestoIntroduttivo()[i];
							if (ele==null)
							{
								confLettonew.getTestoIntroduttivoEng()[i]=new StrutturaCombo("","");
							}

						}
					}
					if (confLettonew.getTestoOggetto().length>0 && confLettonew.getTestoOggetto().length<7 )
					{
						for (int i=0;  i<6; i++)
						{
							StrutturaCombo ele=confLettonew.getTestoOggetto()[i];
							if (ele==null)
							{
								confLettonew.getTestoOggetto()[i]=new StrutturaCombo("","");
							}

						}
					}
					if (confLettonew.getTestoOggettoEng().length>0 && confLettonew.getTestoOggettoEng().length<7 )
					{
						for (int i=0;  i<6; i++)
						{
							StrutturaCombo ele=confLettonew.getTestoOggettoEng()[i];
							if (ele==null)
							{
								confLettonew.getTestoOggettoEng()[i]=new StrutturaCombo("","");
							}

						}
					}
*/


					if (confLettonew!=null)
					{
						confBOnew=confLettonew;
					}
					confBOnew.setUtente(navi.getUtente().getFirmaUtente());
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
					stampaOL.setListaOrdiniDaStampare(provaElencoOrdinato);
					stampaOL.setListaBuoniOrdineDaStampare(null);
					stampaOL.setUtente(navi.getUtente().getFirmaUtente());
					stampaOL.setTicket(navi.getUserTicket());

					//List<StampaBuoniVO> stampaOL_new=new ArrayList<StampaBuoniVO> ();


					//gestione del cambio di stato del flag stampato
 					List listaFiltrata=this.Stampato(provaElencoOrdinato,"ORD","");
 					//List<StampaBuoniVO> risultatoPerStampa=impostaBuonoOrdineDaStampare(confBOnew, provaElencoOrdinato,"ORD", sintordini,  request);
 					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
 					List risultatoPerStampa=factory.getGestioneAcquisizioni().impostaBuoniOrdineDaStampare( confBOnew, listaFiltrata, "ORD", false,  navi.getUserTicket(), navi.getUtente().getFirmaUtente(),  currentForm.getDenoBibl());

 					listaFiltrata=risultatoPerStampa;
 					if (listaFiltrata!=null && listaFiltrata.size()>0)
 					{
 						//((StampaBuoniVO)risultatoPerStampa.get(0)).setListaIDDaStampare(listaFiltrata); // per memorizzare gli id di tutti gli ordini da stampare
 						stampaOL.setListaOrdiniDaStampare(listaFiltrata);
 	 					// test su memorizzazione flag stampato	SPOSTATO IN STAMPAONLINEACTION
 	 					//boolean risultato=this.StampaOrdini(provaElencoOrdinato,null,"ORD",confBOnew.getUtente(),"");
 						boolean risultato=true;
 						if (!risultato)
 						{

 							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

 							return mapping.getInputForward();
 						}
 						else
 						{
 							//TODO GVCANCE
 							request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_BUONI_ORDINE);
 							//request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_LISTA_ORDINI); // aggiungere stampa_lista_ordini
 							//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
 							request.setAttribute("DATI_STAMPE_ON_LINE", listaFiltrata);
 							return mapping.findForward("stampaOL");
 						}
 					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

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


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
		if (request.getAttribute("bidNotiziaCorrente") != null){
			currentForm.setBidNotiziaCorrente((String)request.getAttribute("bidNotiziaCorrente"));
		}
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()  ){
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("IndietroDiEsaminaOrdine")){
				currentForm.setProv("IndietroDiEsaminaOrdine");
				if (!navi.bookmarkExists(TitoliCollegatiInvoke.ANALITICA_PROGRESS) &&
						(currentForm.getListaOrdini() != null && currentForm.getListaOrdini().size() > 0 )) {
					return mapping.getInputForward();
				}
				return this.indietro(mapping, form, request, response);
			}
			// gestione selezione check da  menu bar
			if 	(request.getSession().getAttribute("ordiniSelected")!= null
					&& !request.getSession().getAttribute("ordiniSelected").equals(""))	{
				currentForm.setSelectedOrdini((String[]) request.getSession().getAttribute("ordiniSelected"));
			}

			return mapping.getInputForward();
		}
		if(!currentForm.isSessione())
		{
			currentForm.setSessione(true);
		}
		try {
			//this.loadOrdini();
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			if (ricArr!=null)
			{
				currentForm.setOrdinamentoScelto(ricArr.getOrdinamento());

			}
			if (ricArr!=null && ricArr.getDenoBiblStampe()!=null && ricArr.getDenoBiblStampe().trim().length()>0)
			{
				currentForm.setDenoBibl(ricArr.getDenoBiblStampe().trim());
			}

			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null){

				// imposta visibilità bottone scegli escludendo il caso VAI A
				if (ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
				{
					currentForm.setProvenienzaVAIA(true); // solo bottone crea
					currentForm.setLSRicerca(false); // fai rox 4

				}
				else
				{
					currentForm.setVisibilitaIndietroLS(true); // bottone scegli
					currentForm.setProvenienzaVAIA(true); // bottone crea
					currentForm.setLSRicerca(true); // fai rox 4

				}
				// per il layout
				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
				//}

			}
			if (ricArr==null){
				// l'attributo di sessione deve essere valorizzato
				currentForm.setRisultatiPresenti(false);
			}

			List<OrdiniVO> listaOrdini=new ArrayList();

			//2 righe aggiunte per gestire i blocchi
			//ricArr.setElemXBlocchi(2); // test su + blocchi
			ricArr.setTicket(navi.getUserTicket());
			if (request.getAttribute("numElexBlocchi")!=null){
				ricArr.setElemXBlocchi((Integer)request.getAttribute("numElexBlocchi"));
			}else{
				if (currentForm.getMaxRighe() > 0){
					ricArr.setElemXBlocchi(currentForm.getMaxRighe());
				}
			}
			//ricArr.setElemXBlocchi(currentForm.getNumRighe());

/*			if 	(request.getSession().getAttribute("listaOrdiniEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaOrdini=(List<OrdiniVO>)request.getSession().getAttribute("listaOrdiniEmessa");
			}
			else
			{
				listaOrdini=this.getListaOrdiniVO(ricArr ); // va in errore se non ha risultati
			}
*/
			if (ricArr!=null)
			{
				listaOrdini=this.getListaOrdiniVO(ricArr ); // va in errore se non ha risultati
			}

			currentForm.setListaOrdini(listaOrdini);
			//this.getListaOrdini(); // solo per test
			currentForm.setNumOrdini(currentForm.getListaOrdini().size());

			// gestione automatismo check su unico elemento lista
			if (currentForm.getListaOrdini().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=currentForm.getListaOrdini().get(0).getChiave();
				//	"FI|2007|3";
				currentForm.setSelectedOrdini(appoSelProva);
			}

			if (currentForm.getListaOrdini()!=null &&  currentForm.getListaOrdini().size()>0)
			{
				if (currentForm.getListaOrdini().get(0)!=null &&  !currentForm.getListaOrdini().get(0).isGestBil())
				{
					currentForm.setGestBil(false);
				}
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
			if 	(request.getSession().getAttribute("ultimoBloccoOrdini")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoOrdini");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{

				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaOrdini,maxElementiBlocco);
				currentForm.setListaOrdini(blocco1.getLista());
			}

			if (blocco1 != null) {
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
			currentForm.setLivelloRicerca(navi.getUtente().getCodBib());
			}
			// fine gestione blocchi

			// non trovati

			if (currentForm.getNumOrdini()==0)	{

			}	 else {

				//int totPagine=(int) Math.round((currentForm.getNumOrdini()/currentForm.getNumRighe())+0.5);
				//currentForm.setTotPagine(totPagine);
				if 	(request.getSession().getAttribute("ordiniSelected")!= null){
					currentForm.setSelectedOrdini((String[]) request.getSession().getAttribute("ordiniSelected"));
				}
				// controllo esistenza di precendenti operazioni di modifica
				// ed aggiornamento dello stato della lista con contrassegni di modifica

				if (ricArr.getSelezioniChiamato()!=null)	{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++) 	{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave();
						for (int v=0;  v < currentForm.getListaOrdini().size(); v++)	{
							String eleList= currentForm.getListaOrdini().get(v).getChiave();
							if (eleList.equals(eleSele))	{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)	{
									currentForm.getListaOrdini().get(v).setTipoVariazione(variato);
								}
								break;
							}
						}
					}
				}
/*				String tipoOrdEle="num";
				if (currentForm.getOrdinaLista()!=null)
					{
						tipoOrdEle=currentForm.getOrdinaLista().getDescrizione();
					}
				else
				{
					currentForm.setOrdinaLista(new StrutturaCombo("A","num"));
					tipoOrdEle=currentForm.getOrdinaLista().getDescrizione();
				}*/
				// gestione boolean continuativo
				//List <OrdiniVO> risLista=this.ElencaPer(tipoOrdEle);



				//currentForm.setListaOrdini(risLista);


			}

			return mapping.getInputForward();
		}	catch (ValidationException ve) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;

				if (ve.getError()==4001)
				{
					currentForm.setRisultatiPresenti(false);
					return mapping.findForward("indietro");

				}
				else
				{
					return mapping.getInputForward();
				}
		}
		// altri tipi di errore
		catch (Exception e) {

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
		try {
			ListaSuppOrdiniVO passaACrea=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			// controllo se PROVIENE dal VAI A
			//currentForm.isProvenienzaVAIA() &&
			if ( passaACrea!=null)
			{
				if (passaACrea.getChiamante()!=null && passaACrea.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
				{
					if (passaACrea.getTitolo().getCodice()!=null && passaACrea.getTitolo().getCodice().length()!=0 && passaACrea.getTitolo().getDescrizione()!=null && passaACrea.getTitolo().getDescrizione().length()!=0 && passaACrea.getNaturaOrdine()!=null && passaACrea.getNaturaOrdine().length()!=0 )
					{
						request.getSession().setAttribute("bid",passaACrea.getTitolo().getCodice());
						request.getSession().setAttribute("desc",passaACrea.getTitolo().getDescrizione());
						request.getSession().setAttribute("natura",passaACrea.getNaturaOrdine());
						// pulizia dei criteri da ricercare solo per il VAI A dal crea di sintetica
						request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
					}
				}
			}
			if (currentForm.getBidNotiziaCorrente() != null){
				request.setAttribute("bidNotiziaCorrente", currentForm.getBidNotiziaCorrente());
			}
		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
/*		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			GestioneAcquisizioniDelegate delegate = new GestioneAcquisizioniDelegate(this.servlet.getServletContext());
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				currentForm.getListaOrdini().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					currentForm.setBloccoSelezionato(bloccoVO.getNumBlocco() + 1);
				// ho caricato tutte le righe sulla form
				if (currentForm.getListaOrdini().size() == bloccoVO.getTotRighe())
					currentForm.setAbilitaBlocchi(false);
			}
		}
*/		return mapping.getInputForward();
	}
		public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {

				// l'azione di indietro della sintetica torna al chiamante se è stata invocata la lista di supporto, altrimenti torna alla ricerca
				// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
				ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (ricArr!=null )
				{
					//ListaSuppCambioVO eleRicArr=ricArr.get(0);
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null && !ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
					{
						// carico i risultati della selezione nella variabile da restituire

						//String[] appoParametro=new String[0];
						String[] appoSelezione=new String[0];
						appoSelezione=currentForm.getSelectedOrdini();
						//appoParametro=(String[])request.getSession().getAttribute("ordiniSelected");

						//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
						//{
							//this.AggiornaParametriSintetica(request);
							request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, this.AggiornaRisultatiListaSupporto(currentForm,ricArr));
							request.getSession().setAttribute("ordiniSelected", appoSelezione);

							//this.AggiornaRisultatiListaSupporto(eleRicArr);
						//}

						ActionForward action = new ActionForward();
						action.setName("RITORNA");
						action.setPath(ricArr.getChiamante()+".do");
						return action;
					}
					else
					{
						// solo nel caso di provenienza dal vai A

						if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
						{
							ricArr.setChiamante(null);
						}
						return mapping.findForward("indietro");
					}
				}
				else
				{
					return mapping.findForward("indietro");
				}

				//return mapping.findForward("indietro");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				// l'azione di indietro della sintetica torna al chiamante se è stata invocata la lista di supporto, altrimenti torna alla ricerca
				// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
				ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (ricArr!=null )	{
					//ListaSuppCambioVO eleRicArr=ricArr.get(0);
					// gestione del chiamante
					if (ricArr!=null){
						Navigation navi = Navigation.getInstance(request);
						if (ricArr.getChiamante()!=null){
							if (!ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))	{
								// solo nel caso di provenienza dal vai A
								navi.purgeThis();
								ActionForward action = new ActionForward();
								action.setName("RITORNA");
								action.setPath(ricArr.getChiamante()+".do");
								return action;
							}else{
								if (currentForm.getProv()!= null){
									navi.purgeThis();
									return navi.goBack(true);
//									 if (request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE) == null){
//											request.getSession().setAttribute("ATTRIBUTEListaSuppOrdiniVO", (ListaSuppOrdiniVO) ricArr);
//									 }
//									return this.unspecified(mapping, form, request, response);
								}
							}
						}	else	{
							if (ricArr!=null && ricArr.getChiamante()!=null
									&& ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo")){
								ricArr.setChiamante(null);
							}
							if (currentForm.getProv()!= null){
								navi.purgeThis();
								return navi.goBack(true);
//								request.setAttribute("prov", null);
//								 if (request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE) == null){
//										request.getSession().setAttribute("ATTRIBUTEListaSuppOrdiniVO", (ListaSuppOrdiniVO) ricArr);
//								 }
//								return this.unspecified(mapping, form, request, response);
							}

							return mapping.findForward("indietro");
						}
					}
				}	else	{
					if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("IndietroDiEsaminaOrdine")){
//						if (Navigation.getInstance(request).bookmarkExists(TitoliCollegatiInvoke.ANALITICA_PROGRESS)) {
//							Navigation.getInstance(request).purgeThis();
//							return Navigation.getInstance(request).goBack(true);
//						}else{
							return mapping.getInputForward();
//						}
					}else{
						return mapping.findForward("indietro");
					}
				}
				return mapping.getInputForward();

				//return mapping.findForward("indietro");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			int numBlocco =0;
			numBlocco = currentForm.getBloccoSelezionato();
			String idLista = currentForm.getIdLista();
			String ticket = Navigation.getInstance(request).getUserTicket();
			// && numBlocco <=sinOfferte.getTotBlocchi()
			if ( numBlocco > 1  && idLista != null) {
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
				//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
				// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

				//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
				if (bloccoSucc != null) {
					currentForm.getListaOrdini().addAll(bloccoSucc.getLista());
//					if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//						this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//					// ho caricato tutte le righe sulla form
//					if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//						eleutenti.setAbilitaBlocchi(false);
					request.getSession().setAttribute("ultimoBloccoOrdini",bloccoSucc);
					currentForm.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
				}
				else
				{
					request.getSession().setAttribute("ultimoBloccoOrdini",currentForm.getUltimoBlocco());
				}
			}
			return mapping.getInputForward();
		}

		public ActionForward ok(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
				if (currentForm.getProgrForm() > 0) {
					try {
							this.preparaRicercaOrdineSingle(currentForm, request,currentForm.getProgrForm() );
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


		public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				String[] appoParametro=new String[0];
				String[] appoSelezione=new String[0];
				appoSelezione=currentForm.getSelectedOrdini();
				//appoParametro=(String[])request.getSession().getAttribute("ordiniSelected");

				if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
				{
					//this.AggiornaParametriSintetica(request);
					this.preparaRicercaOrdine(currentForm,request);
					// si aggiorna l'attributo con l'elenco dei cambi trovati
					request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, this.AggiornaRisultatiListaSupporto( currentForm, (ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE)));
					request.getSession().setAttribute("ordiniSelected", appoSelezione);
					request.getSession().setAttribute("listaOrdiniEmessa", currentForm.getListaOrdini());

					return mapping.findForward("esamina");

				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.ricerca"));

					return mapping.getInputForward();
				}
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward operazionesuordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_OPERAZIONI_SU_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);

				}   catch (UtenteNotAuthorizedException e) {

					LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

					return mapping.getInputForward();
				}

				if (currentForm.getSelectedOrdini()==null || currentForm.getSelectedOrdini().length==0) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

					return mapping.getInputForward();
				}
				else
				{
					List<Integer> listaIDOrdini = new ArrayList<Integer> ();

					for (int v=0; v< currentForm.getSelectedOrdini().length; v++)
					{
						for (int u=0; u<currentForm.getListaOrdini().size(); u++)
						{
							if (currentForm.getSelectedOrdini()[v].equals(currentForm.getListaOrdini().get(u).getChiave()))
							  {
								listaIDOrdini.add(currentForm.getListaOrdini().get(u).getIDOrd());
								break;
							  }
						}
					}

					// imposto variabile di sessione per la pagina chiamante
					request.getSession().setAttribute("chiamante",  mapping.getPath());
					request.setAttribute("listaIDOrdini", listaIDOrdini);
					OperazOrdVO passaAOperazOrdini=new OperazOrdVO();
					OrdiniVO ordine = currentForm.getListaOrdini().get(0);
					passaAOperazOrdini.setCodBibl(ordine.getCodBibl());
					passaAOperazOrdini.setCodPolo(ordine.getCodPolo());
					passaAOperazOrdini.setListaIDOrdini(listaIDOrdini);

					request.setAttribute("passaAOperazOrdini", passaAOperazOrdini);

					return mapping.findForward("operazionesuordine");
				}

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward stampaBollettario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {

				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO, utente.getCodPolo(), utente.getCodBib(), null);

				}   catch (UtenteNotAuthorizedException e) {

					LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

					return mapping.getInputForward();

				}

				if (currentForm.getSelectedOrdini()==null || currentForm.getSelectedOrdini().length==0) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

					return mapping.getInputForward();
				}
				else
				{
					List<Integer> listaIDOrdini = new ArrayList<Integer> ();
					List<OrdiniVO> listaOrdini = new ArrayList<OrdiniVO> ();

					Boolean scarto=false;
					for (int v=0; v< currentForm.getSelectedOrdini().length; v++)
					{
						for (int u=0; u<currentForm.getListaOrdini().size()  ; u++)
						{
							// solo ordini di rilegatura
/*							if (!currentForm.getListaOrdini().get(u).getTipoOrdine().equals("R"))
							  {
								break;
							  }
*/							if (currentForm.getSelectedOrdini()[v].equals(currentForm.getListaOrdini().get(u).getChiave()))
							  {
/*								if (currentForm.getListaOrdini().get(u).getTipoOrdine()!=null &&  currentForm.getListaOrdini().get(u).getTipoOrdine().equals("R"))
								  {
									listaIDOrdini.add(currentForm.getListaOrdini().get(u).getIDOrd());
								  }
								else
								  {
									scarto=true;
								  }
*/
								// solo ordini di rilegatura
								//if (currentForm.getListaOrdini().get(u).getTipoOrdine()!=null &&  currentForm.getListaOrdini().get(u).getTipoOrdine().equals("R"))
								//  {
								//listaOrdini.add(currentForm.getListaOrdini().get(u));
									// recupero del titolo degli inventari legati all'ordine
/*									OrdiniVO eleOrd=currentForm.getListaOrdini().get(u);
									if (eleOrd.getRigheInventariRilegatura()!=null && eleOrd.getRigheInventariRilegatura().size()>0  )
									{
										for (int i=0;  i < eleOrd.getRigheInventariRilegatura().size(); i++)
										{
											StrutturaInventariOrdVO ele=eleOrd.getRigheInventariRilegatura().get(i);
											if (ele.getTitolo()==null)
											{
												ele.setTitolo("");
											}
											String	titoInv="";
											titoInv=this.controllaTitolo(ele.getBid());

											if (titoInv!=null)
											{
												ele.setTitolo(titoInv);
											}
											else
											{
												ele.setTitolo("");
											}
										}
									}
*/								 // }
								listaIDOrdini.add(currentForm.getListaOrdini().get(u).getIDOrd());
								break;
							  }
						}
					}

					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					ListaSuppOrdiniVO crit2=new ListaSuppOrdiniVO();
					// basta considerare il polo-biblioteca del primo della lista prospettata
					if (currentForm.getListaOrdini()!=null && currentForm.getListaOrdini().get(0)!=null && currentForm.getListaOrdini().get(0).getCodPolo()!=null && currentForm.getListaOrdini().get(0).getCodPolo().length()>0)
					{
						crit2.setCodPolo(currentForm.getListaOrdini().get(0).getCodPolo());
					}
					if (currentForm.getListaOrdini()!=null && currentForm.getListaOrdini().get(0)!=null && currentForm.getListaOrdini().get(0).getCodBibl()!=null && currentForm.getListaOrdini().get(0).getCodBibl().length()>0)
					{
						crit2.setCodBibl(currentForm.getListaOrdini().get(0).getCodBibl());
					}

					crit2.setIdOrdList(listaIDOrdini);

					crit2.setIdOrdList(listaIDOrdini);
					crit2.setBollettarioSTP(true);
					List<StrutturaInventariOrdVO> elencoOrdinato = factory.getGestioneAcquisizioni().getInventariOrdineRilegatura(crit2);

					if (currentForm.getSelectedOrdini().length >  elencoOrdinato.size())
					{
						// messaggio di ordini scartati perche non di rilegatura

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonDiRilegatura"));

						return mapping.getInputForward();

					}

					// elenco dei soli ordini di rilegatura selezionati
					if (elencoOrdinato!=null && elencoOrdinato.size()>0)
					  {
						// va poi passato al batch di stampa del bollettario
							request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_BOLLETTARIO); // aggiungere stampa_lista_ordini
 							request.setAttribute("DATI_STAMPE_ON_LINE", elencoOrdinato);
 							return mapping.findForward("stampaOL");
					  }


/*					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

					OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO = new OperazioneSuOrdiniMassivaVO();

					operazioneSuOrdiniVO.setTipoOperazione("H");

					// impostare gli id degli ordini selezionati come array e passarlo

					operazioneSuOrdiniVO.setDatiInput(listaIDOrdini);
					// sarebbe meglio passare i codici bibl e polo del primo della lista di sintetica
					operazioneSuOrdiniVO.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
					operazioneSuOrdiniVO.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());

					String basePath=this.servlet.getServletContext().getRealPath(File.separator);
					operazioneSuOrdiniVO.setBasePath(basePath);
					String downloadPath = StampeUtil.getBatchFilesPath();
					log.info("download path: " + downloadPath);
					String downloadLinkPath = "/";
					operazioneSuOrdiniVO.setDownloadPath(downloadPath);
					operazioneSuOrdiniVO.setDownloadLinkPath(downloadLinkPath);
					operazioneSuOrdiniVO.setTicket(Navigation.getInstance(request).getUserTicket());
					operazioneSuOrdiniVO.setUser(Navigation.getInstance(request).getUtente().getFirmaUtente());
					String s = factory.getElaborazioniDifferite().operazioneSuOrdine(operazioneSuOrdiniVO);

					if (s == null) {

						errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotBatchKO"));

						resetToken(request);
						return mapping.getInputForward();
					}


					errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));

					resetToken(request);
*/
					return mapping.getInputForward();

				}

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward listaInventariOrdine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
	            if (Navigation.getInstance(request).isFromBar()){

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.inventariAssenti"));

	            	return mapping.getInputForward();
	            }

				if (currentForm.getSelectedOrdini()==null || currentForm.getSelectedOrdini().length==0 || currentForm.getSelectedOrdini().length>1) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricercaOchecksingolo"));

					return mapping.getInputForward();
				}
				else
				{
					List<OrdiniVO> arrSezioStrut=new ArrayList<OrdiniVO>();
					for (int i=0;  i < currentForm.getSelectedOrdini().length; i++)
					{
						String codProfil=currentForm.getSelectedOrdini()[i].trim();
						for (int j=0;  j < currentForm.getListaOrdini().size(); j++)
						{
							OrdiniVO profilStrut=currentForm.getListaOrdini().get(j);
							if (profilStrut.getChiave().trim().equals(codProfil))
							{
								arrSezioStrut.add(profilStrut);
							}
						}
					}
					if (arrSezioStrut!=null && arrSezioStrut.size()==1)
					{
						OrdiniVO ordineSel=arrSezioStrut.get(0);
						if (ordineSel.getTipoOrdine().equals("R"))
						{

							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.noListaInvOrdiniRil"));

							return mapping.getInputForward();

						}
						request.setAttribute("codBibF", ordineSel.getCodPolo());
				        request.setAttribute("codBibO", ordineSel.getCodBibl());
				        request.setAttribute("codTipoOrd", ordineSel.getTipoOrdine());
				        request.setAttribute("annoOrd", ordineSel.getAnnoOrdine());
				        request.setAttribute("codOrd", ordineSel.getCodOrdine());
				        request.setAttribute("prov", "ordine");
				        request.setAttribute("titOrd", ordineSel.getTitolo());
				        return mapping.findForward("sifListeInventari");
					}
					else
					{
						return mapping.getInputForward();
					}
				}

			}catch (Exception e) {
				return mapping.getInputForward();
			}
		}



		public ActionForward controllaordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				return mapping.findForward("controllaordine");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				String [] appoLista= new String [currentForm.getListaOrdini().size()];
				int i;
				for (i=0;  i < currentForm.getListaOrdini().size(); i++)
				{
					OrdiniVO ordi= currentForm.getListaOrdini().get(i);
					//String cod=ordi.getCodOrdine();
					String cod=ordi.getChiave();
					appoLista[i]=cod;
				}
				currentForm.setSelectedOrdini(appoLista);
				request.getSession().setAttribute("ordiniSelected", appoLista);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				String[] appoSel=new String[0];
				currentForm.setSelectedOrdini(appoSel);
				request.getSession().setAttribute("ordiniSelected", appoSel);

				//currentForm.setSelectedOrdini(null);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

/*		public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;

			try {

				currentForm.setNumOrdini(currentForm.getListaOrdini().size());
				// aggiungo 0.5 per arrotondarlo al intero superiore
				// controllare che il numero delle righe non sia il size perchè totpag viene =2
				int totPagine= (int)Math.round((currentForm.getNumOrdini()/currentForm.getNumRighe())+0.5);
				// resto della divisione
				currentForm.setTotPagine(totPagine);
				// la posizione dell'elemento nella lista si conteggia da zero per cui non aggiungo una unità
				currentForm.setPosElemento((currentForm.getNumRighe())*(currentForm.getNumPagina()-1));

				this.AggiornaParametriSintetica(request);

				// errore
				if (Integer.valueOf(currentForm.getNumPagina())>totPagine)
				{

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.errorePagina"));

					// reimposto la paginazione a quella precedente all'errore
					currentForm.setNumRighe(currentForm.getNumRigheOld());
					currentForm.setPosElemento(currentForm.getPosElementoOld());
					currentForm.setNumPagina(currentForm.getNumPaginaOld());
					return mapping.getInputForward();
				}

				currentForm.setNumRigheOld(currentForm.getNumRighe());
				currentForm.setPosElementoOld(currentForm.getPosElemento());

				return mapping.getInputForward();

			} catch (Exception e) {
				return mapping.getInputForward();
			}

		}
		*/
/*		public ActionForward criterioTipo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				this.AggiornaParametriSintetica(request);

				if (currentForm.getOrdinaLista().getDescrizione().equal("tipo"))
				{
					if (currentForm.getOrdinaLista().getCodice().equal("A") ||  currentForm.getOrdinaLista().getCodice().length()==0 ||  currentForm.getOrdinaLista().getCodice()==null)
					{
						currentForm.getOrdinaLista().setCodice("D");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaDescPer("tipo"));
					}
					else if (currentForm.getOrdinaLista().getCodice().equal("D"))
					{
						currentForm.getOrdinaLista().setCodice("A");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("tipo"));
					}
				}
				else
				{
					currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("tipo"));
					currentForm.getOrdinaLista().setCodice("A");
					currentForm.getOrdinaLista().setDescrizione("tipo");

				}

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward criterioNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				this.AggiornaParametriSintetica(request);

  				//currentForm.setOrdinaLista("var");
				//String tipoOrdEle=currentForm.getOrdinaLista();
				if (currentForm.getOrdinaLista().getDescrizione().equal("num"))
				{
					if (currentForm.getOrdinaLista().getCodice().equal("A") ||  currentForm.getOrdinaLista().getCodice().length()==0 ||  currentForm.getOrdinaLista().getCodice()==null)
					{
						currentForm.getOrdinaLista().setCodice("D");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaDescPer("num"));
					}
					else if (currentForm.getOrdinaLista().getCodice().equal("D"))
					{
						currentForm.getOrdinaLista().setCodice("A");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("num"));
					}
				}
				else
				{
					currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("num"));
					currentForm.getOrdinaLista().setCodice("A");
					currentForm.getOrdinaLista().setDescrizione("num");

				}

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward criterioData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				this.AggiornaParametriSintetica(request);

				if (currentForm.getOrdinaLista().getDescrizione().equal("data"))
				{
					if (currentForm.getOrdinaLista().getCodice().equal("A") ||  currentForm.getOrdinaLista().getCodice().length()==0 ||  currentForm.getOrdinaLista().getCodice()==null)
					{
						currentForm.getOrdinaLista().setCodice("D");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaDescPer("data"));
					}
					else if (currentForm.getOrdinaLista().getCodice().equal("D"))
					{
						currentForm.getOrdinaLista().setCodice("A");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("data"));
					}
				}
				else
				{
					currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("data"));
					currentForm.getOrdinaLista().setDescrizione("data");
					currentForm.getOrdinaLista().setCodice("A");

				}

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward criterioStato(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				this.AggiornaParametriSintetica(request);

				if (currentForm.getOrdinaLista().getDescrizione().equal("stato"))
				{
					if (currentForm.getOrdinaLista().getCodice().equal("A") ||  currentForm.getOrdinaLista().getCodice().length()==0 ||  currentForm.getOrdinaLista().getCodice()==null)
					{
						currentForm.getOrdinaLista().setCodice("D");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaDescPer("stato"));
					}
					else if (currentForm.getOrdinaLista().getCodice().equal("D"))
					{
						currentForm.getOrdinaLista().setCodice("A");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("stato"));
					}

				}
				else
				{
					currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("stato"));
					currentForm.getOrdinaLista().setCodice("A");
					currentForm.getOrdinaLista().setDescrizione("stato");

				}

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}
*/
/*		public ActionForward criterioForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				//this.AggiornaParametriSintetica(request);

				if (currentForm.getOrdinaLista().getDescrizione().equal("forn"))
				{
					if (currentForm.getOrdinaLista().getCodice().equal("A") ||  currentForm.getOrdinaLista().getCodice().length()==0 ||  currentForm.getOrdinaLista().getCodice()==null)
					{
						currentForm.getOrdinaLista().setCodice("D");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaDescPer( sintordini,"forn"));
					}
					else if (currentForm.getOrdinaLista().getCodice().equal("D"))
					{
						currentForm.getOrdinaLista().setCodice("A");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer(sintordini, "forn"));
					}
				}
				else
				{
					currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer(sintordini, "forn"));
					currentForm.getOrdinaLista().setCodice("A");
					currentForm.getOrdinaLista().setDescrizione("forn");
				}
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}			*/
/*
		public ActionForward criterioBil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SinteticaOrdineForm currentForm = (SinteticaOrdineForm ) form;
			try {
				this.AggiornaParametriSintetica(request);

				if (currentForm.getOrdinaLista().getDescrizione().equal("bil"))
				{
					if (currentForm.getOrdinaLista().getCodice().equal("A") ||  currentForm.getOrdinaLista().getCodice().length()==0 ||  currentForm.getOrdinaLista().getCodice()==null)
					{
						currentForm.getOrdinaLista().setCodice("D");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaDescPer("bil"));
					}
					else if (currentForm.getOrdinaLista().getCodice().equal("D"))
					{
						currentForm.getOrdinaLista().setCodice("A");
						currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("bil"));
					}

				}
				else
				{
					currentForm.setListaOrdini((List <OrdiniVO>)this.ElencaPer("bil"));
					currentForm.getOrdinaLista().setCodice("A");
					currentForm.getOrdinaLista().setDescrizione("bil");

				}
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}
*/
		public List<OrdiniVO> ElencaPer(List<OrdiniVO> lst,SinteticaOrdineForm currentForm,String sortBy ) throws EJBException {
			//List<OrdiniVO> lst = currentForm.getListaOrdini();
			Comparator comp=null;
			if (sortBy==null) {
				comp =new FornComparator();
				}
/*			else if (sortBy.equals("num")) {
				comp =new NumComparator();
				}
			else if (sortBy.equals("tipo")) {
				comp =new TipoComparator();
				}
			else if (sortBy.equals("data")) {
				comp =new DataComparator();
				}
			else if (sortBy.equals("stato")) {
				comp =new StatoComparator();
				}
*/			else if (sortBy.equals("forn")) {
				comp =new FornComparator();
				}
/*			else if (sortBy.equals("bil")) {
				comp =new BilComparator();
				}*/

			if (lst != null)
			{
				if (comp != null)
				{
					Collections.sort(lst, comp);
				}
			}
			return lst;
		}

		public List<OrdiniVO> ElencaDescPer(List<OrdiniVO> lst, SinteticaOrdineForm currentForm,String sortBy) throws EJBException {
			//List<OrdiniVO> lst = currentForm.getListaOrdini();
			Comparator comp=null;
			if (sortBy==null) {
				comp =new FornDescending();
				}
/*			else if (sortBy.equals("tipo")) {
				comp =new TipoDescending();
				}
			else if (sortBy.equals("data")) {
				comp =new DataDescending();
				}
			else if (sortBy.equals("stato")) {
				comp =new StatoDescending();
				}*/
			else if (sortBy.equals("forn")) {
				comp =new FornDescending();
				}
/*			else if (sortBy.equals("bil")) {
				comp =new BilDescending();
				}

*/			if (lst != null)
			{
				if (comp != null)
				{
					//Collections.sort(lst, Collections.reverseOrder());
					Collections.sort(lst, comp);
				}
			}
			return lst;
		}

/*

		private static class NumComparator implements Comparator {
				public int compare(Object o1, Object o2) {
					try {
						String e1 = ((OrdiniVO) o1).getCodOrdine();
						String e2 = ((OrdiniVO) o2).getCodOrdine();
						return e1.compareTo(e2);
					} catch (RuntimeException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}
		private static class NumDescending implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getCodOrdine();
					String e2 = ((OrdiniVO) o2).getCodOrdine();
					return - e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}

		private static class TipoComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getTipoOrdine();
					String e2 = ((OrdiniVO) o2).getTipoOrdine();
					return e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}
	private static class TipoDescending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((OrdiniVO) o1).getTipoOrdine();
				String e2 = ((OrdiniVO) o2).getTipoOrdine();
				return - e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}


		private static class DataComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getDataOrdine();
					String e2 = ((OrdiniVO) o2).getDataOrdine();
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					try {
						Date e1A=(Date) formato.parse(e1);
						Date e2A=(Date) formato.parse(e2);
						return e1A.compareTo(e2A);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}

			}
		}

		private static class DataDescending implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getDataOrdine();
					String e2 = ((OrdiniVO) o2).getDataOrdine();
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					try {
						Date e1A=(Date) formato.parse(e1);
						Date e2A=(Date) formato.parse(e2);
						return -e1A.compareTo(e2A);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return -e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}



		private static class StatoComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getStatoOrdine();
					String e2 = ((OrdiniVO) o2).getStatoOrdine();
					return e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}

		private static class StatoDescending implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getStatoOrdine();
					String e2 = ((OrdiniVO) o2).getStatoOrdine();
					return -e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}
*/
		private static class FornComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getCoppiaOrdinamento();  // codice forn | tipo ordine
					String e2 = ((OrdiniVO) o2).getCoppiaOrdinamento();

/*					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					try {
						Date e1A=(Date) formato.parse(e1);
						Date e2A=(Date) formato.parse(e2);
						return e1A.compareTo(e2A);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
*/

					return e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}




		private static class FornDescending implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getFornitore().getCoppia();
					String e2 = ((OrdiniVO) o2).getFornitore().getCoppia();
					return -e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}
/*
		private static class BilComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getBilancio().getTerna();
					String e2 = ((OrdiniVO) o2).getBilancio().getTerna();
					return e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}

		private static class BilDescending implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((OrdiniVO) o1).getBilancio().getTerna();
					String e2 = ((OrdiniVO) o2).getBilancio().getTerna();
					return -e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}


	private void loadOrdini(SinteticaOrdineForm currentForm) throws Exception {
		List lista = new ArrayList();

		OrdiniVO ordi = new OrdiniVO("X10", "01","40", "2006", "A", "27/03/2006", "", 0, true, "A", "", "", "", "","",  new StrutturaCombo("33", "Libreria Grande"), "", "",new  StrutturaTerna ("2006", "1", "2"), "", "", "EUR", 19.00, 19.00, "IT", "MON", "", "",new StrutturaCombo("RAV0008725", "Airone: vivere la natura conoscere il mondo"), "", "", "","", "", "", 0, "", "", "S", false,false, "");
		lista.add(ordi);

		ordi = new OrdiniVO("X10", "01","17", "2006", "A", "09/02/2006", "", 0, false, "A", "", "", "", "","",  new StrutturaCombo("33", "Libreria Grande"), "", "",new  StrutturaTerna ("2006", "1", "1"), "", "", "EUR", 19.00, 19.00, "IT", "MON", "", "",new StrutturaCombo("X100006643", "Le radici storiche dell'etica analitica"), "", "", "","", "", "", 0, "", "", "M",false, false, "");
		lista.add(ordi);

		currentForm.setListaOrdini(lista);
	}
*/
	private List<OrdiniVO> getListaOrdiniVO(ListaSuppOrdiniVO criRicerca) throws Exception
	{
		List<OrdiniVO> listaOrdini;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaOrdini = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);

/*		List listaBid;
		listaBid=new ArrayList();
		String tick=criRicerca.getTicket();

		if (listaOrdini!=null && listaOrdini.size()>0)
		{
			for (int i=0;  i <  listaOrdini.size(); i++)
			{
				String bid="";
				if (listaOrdini.get(i).getTitolo()!=null && listaOrdini.get(i).getTitolo().getCodice()!=null && listaOrdini.get(i).getTitolo().getCodice().trim().length()>0)
				{
					bid=listaOrdini.get(i).getTitolo().getCodice();
					List<OrdiniVO> pippo = (List<OrdiniVO>) factory.getGestioneAcquisizioni().getTitoloOrdine(listaOrdini.get(i));

				}
				listaBid.add(bid);
			}
			if (listaBid!=null  && listaBid.size()>0)
			{
				List listaTit=(List)factory.getGestioneAcquisizioni().getTitolo(listaBid,tick);
			}

		}

*/
/*		String tick=criRicerca.getTicket();
		if (listaOrdini!=null && listaOrdini.size()>0)
		{
			for (int i=0;  i <  listaOrdini.size(); i++)
			{
				String bid="";
				String isbd="";
				String natura="";
				String paese="";
				String[] arrCodLingua=new String[0];

				if (listaOrdini.get(i).getTitolo()!=null && listaOrdini.get(i).getTitolo().getCodice()!=null && listaOrdini.get(i).getTitolo().getCodice().trim().length()>0)
				{
					bid=listaOrdini.get(i).getTitolo().getCodice();
				}
				if (bid!=null  && bid.trim().length()!=0)
				{
					try {
					List listaTit=(List)factory.getGestioneAcquisizioni().getTitolo(bid,tick);
					TitoloACQVO recTit = (TitoloACQVO) listaTit.get(0);
					if (recTit!=null)
					{
						isbd=recTit.getIsbd();
						natura=recTit.getNatura();
						paese=recTit.getCodPaese();
						arrCodLingua=recTit.getArrCodLingua();
					}
					} catch (Exception e) {
						isbd="titolo non trovato";
					}
				}
				//List lista = (List<OrdiniVO>) factory.getGestioneAcquisizioni().getTitolo(bid ,tick);
				listaOrdini.get(i).getTitolo().setDescrizione(isbd);
				listaOrdini.get(i).setNaturaOrdine(natura);
				listaOrdini.get(i).setPaeseOrdine(paese);
				//listaOrdini.get(i).; lingua???

			}
		}
*/
		// esempio con i blocchi
/*
		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);
		// gestione blocchi INIZIO getListaOrdiniPaginata
		currentForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
		//memorizzo le informazioni per la gestione blocchi
		currentForm.setIdLista(blocco1.getIdLista());
		currentForm.setTotRighe(blocco1.getTotRighe());
		currentForm.setTotBlocchi(blocco1.getTotBlocchi());
		currentForm.setBloccoSelezionato(blocco1.getNumBlocco()+1);
		listaOrdini=(List<OrdiniVO>)blocco1.getLista();
*/
		return listaOrdini;
	}




	private void getListaOrdini(SinteticaOrdineForm currentForm) throws Exception
	{
	List<OrdiniVO> listaCambi;

	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaCambi = factory.getGestioneAcquisizioni().getListaOrdini();
	currentForm.setListaOrdini(listaCambi);
	//return elencoSezioni;

	}

	/**
	 * SinteticaOrdineAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  preparaRicercaOrdine(SinteticaOrdineForm currentForm,  HttpServletRequest request)
	{
		try {
			List<ListaSuppOrdiniVO> ricerca=new ArrayList();
			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			String [] listaSelezionati=currentForm.getSelectedOrdini();
			String ticket=Navigation.getInstance(request).getUserTicket();

			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < currentForm.getListaOrdini().size(); j++)
				{
					OrdiniVO eleElenco=currentForm.getListaOrdini().get(j);
					String chiaveComposta=eleElenco.getChiave();
					//chiaveComposta[3] codOrdine
					//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{
						String codP=eleElenco.getCodPolo();
						String codB=eleElenco.getCodBibl();
						String codBAff=null;
						String codOrd=eleElenco.getCodOrdine();
						String annoOrd=eleElenco.getAnnoOrdine();
						String tipoOrd=eleElenco.getTipoOrdine();
						String dataOrdDa=null;
						String dataOrdA=null;
						String cont=null;
						String statoOrd=""; // ininfluente doppione
						StrutturaCombo forn=new StrutturaCombo("","") ;
						String tipoInvioOrd="";
						StrutturaTerna bil=new StrutturaTerna("","","");
						String sezioneAcqOrd="";
						StrutturaCombo tit=new StrutturaCombo("","");
						String dataFineAbbOrdDa=null;
						String dataFineAbbOrdA=null;
						String naturaOrd="";
						String chiama=null;
						//String[] statoOrdArr= new String[0];
						String[] statoOrdArr=null; // solo per scorrimenti di  dettaglio
						Boolean rinn=eleElenco.isRinnovato();
						Boolean stamp=eleElenco.isStampato();
/*						String statoOrd=eleElenco.getStatoOrdine();
						StrutturaCombo forn=eleElenco.getFornitore();
						String tipoInvioOrd=eleElenco.getTipoInvioOrdine();
						StrutturaTerna bil=eleElenco.getBilancio();
						String sezioneAcqOrd=eleElenco.getSezioneAcqOrdine();
						StrutturaCombo tit=eleElenco.getTitolo();
						String dataFineAbbOrdDa=null;
						String dataFineAbbOrdA=null;
						String naturaOrd=eleElenco.getNaturaOrdine();
						String chiama=null;
						String[] statoOrdArr= new String[0];
						Boolean rinn=eleElenco.isRinnovato();
						Boolean stamp=eleElenco.isStampato();
*/
						eleRicerca=new ListaSuppOrdiniVO( codP, codB,  codBAff,   codOrd,  annoOrd,  tipoOrd,  dataOrdDa, dataOrdA,   cont,  statoOrd,  forn,   tipoInvioOrd,  bil,    sezioneAcqOrd,  tit,   dataFineAbbOrdDa,  dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
						eleRicerca.setTicket(ticket);
						eleRicerca.setOrdinamento("");
						if (currentForm.getOrdinamentoScelto()!=null && currentForm.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(currentForm.getOrdinamentoScelto());
						}

						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE, ricerca);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void  preparaRicercaOrdineSingle(SinteticaOrdineForm currentForm, HttpServletRequest request, int prg)
	{
		try {
			List<ListaSuppOrdiniVO> ricerca=new ArrayList();
			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			String ticket=Navigation.getInstance(request).getUserTicket();

			//almaviva5_20141230
			OrdiniVO p = Lambda.on(OrdiniVO.class);

			List<OrdiniVO> listaOrdini = currentForm.getListaOrdini();
			//OrdiniVO eleElenco=(OrdiniVO)listaOrdini.get(prg);
			OrdiniVO eleElenco = Lambda.selectUnique(listaOrdini, Lambda.having(p.getProgressivo(), Matchers.equalTo(prg)));

			String codP=eleElenco.getCodPolo();
			String codB=eleElenco.getCodBibl();
			String codBAff=null;
			String codOrd=eleElenco.getCodOrdine();
			String annoOrd=eleElenco.getAnnoOrdine();
			String tipoOrd=eleElenco.getTipoOrdine();
			String dataOrdDa=null;
			String dataOrdA=null;
			String cont=null;
			String statoOrd=""; // ininfluente doppione
			StrutturaCombo forn=new StrutturaCombo("","") ;
			String tipoInvioOrd="";
			StrutturaTerna bil=new StrutturaTerna("","","");
			String sezioneAcqOrd="";
			StrutturaCombo tit=new StrutturaCombo("","");
			String dataFineAbbOrdDa=null;
			String dataFineAbbOrdA=null;
			String naturaOrd="";
			String chiama=null;
			//String[] statoOrdArr= new String[0];
			String[] statoOrdArr=null; // solo per scorrimenti di  dettaglio
			Boolean rinn=eleElenco.isRinnovato();
			Boolean stamp=eleElenco.isStampato();
/*						String statoOrd=eleElenco.getStatoOrdine();
						StrutturaCombo forn=eleElenco.getFornitore();
						String tipoInvioOrd=eleElenco.getTipoInvioOrdine();
						StrutturaTerna bil=eleElenco.getBilancio();
						String sezioneAcqOrd=eleElenco.getSezioneAcqOrdine();
						StrutturaCombo tit=eleElenco.getTitolo();
						String dataFineAbbOrdDa=null;
						String dataFineAbbOrdA=null;
						String naturaOrd=eleElenco.getNaturaOrdine();
						String chiama=null;
						String[] statoOrdArr= new String[0];
						Boolean rinn=eleElenco.isRinnovato();
						Boolean stamp=eleElenco.isStampato();
*/
			eleRicerca=new ListaSuppOrdiniVO( codP, codB,  codBAff,   codOrd,  annoOrd,  tipoOrd,  dataOrdDa, dataOrdA,   cont,  statoOrd,  forn,   tipoInvioOrd,  bil,    sezioneAcqOrd,  tit,   dataFineAbbOrdDa,  dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
			eleRicerca.setTicket(ticket);
			eleRicerca.setOrdinamento("");
			if (currentForm.getOrdinamentoScelto()!=null && currentForm.getOrdinamentoScelto().trim().length()>0 )
			{
				eleRicerca.setOrdinamento(currentForm.getOrdinamentoScelto());
			}

			ricerca.add(eleRicerca);
			request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE, ricerca);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}



	/**
	 * SinteticaOrdineAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i cambi prodotti dalla ricerca
	 */
	private ListaSuppOrdiniVO AggiornaRisultatiListaSupporto (SinteticaOrdineForm currentForm, ListaSuppOrdiniVO eleRicArr)
	{
		try {
			List<OrdiniVO> risultati=new ArrayList();
			OrdiniVO eleOrdine=new OrdiniVO();
			String [] listaSelezionati=currentForm.getSelectedOrdini();
			String codP;
			String codB;
			String codOrd;
			String annoOrd;
			String tipoOrd;
			String dataOrd;
			String noteOrd;
			int numCopieOrd;
			boolean cont;
			String statoOrd;
			String codDocOrd;
			String codTipoDocOrd;
			String codUrgenzaOrd;
			String codRicOffertaOrd;
			String idOffertaFornOrd;
			StrutturaCombo forn;
			String noteForn;
			String tipoInvioOrd;
			StrutturaTerna bil;
			String codPrimoOrd;
			String annoPrimoOrd;
			String valutaOrd;
			double prezzoOrd;
			double prezzoEuroOrd;
			String paeseOrd;
			String sezioneAcqOrd;
			String codBibliotecaSuggOrd;
			String codSuggBiblOrd;
			StrutturaCombo tit;
			String statoAbbOrd;
			String periodoValAbbOrd;
			String annoAbbOrd;
			String numFascicoloAbbOrd;
			String dataPubblFascicoloAbbOrd;
			String annataAbbOrd;
			int  numVolAbbOrd;
			String dataFineAbbOrd;
			String regTribOrd;
			String naturaOrd;
			boolean rinn;
			boolean stamp;

			String tipoVar;
			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < currentForm.getListaOrdini().size(); j++)
				{
					OrdiniVO eleElenco=currentForm.getListaOrdini().get(j);
					String chiaveComposta=eleElenco.getChiave();
					//if (eleSel.equals(eleElenco.getCodOrdine()))
					if (eleSel.equals(chiaveComposta))
					{
						codP=eleElenco.getCodPolo();
						codB=eleElenco.getCodBibl();
						codOrd=eleElenco.getCodOrdine();
						annoOrd=eleElenco.getAnnoOrdine();
						tipoOrd=eleElenco.getTipoOrdine();
						dataOrd=eleElenco.getDataOrdine();

						//Date dataOrdDate=Date.valueOf(dataOrd);
						//String annoOrd=String.valueOf(dataOrdDate.getYear());

						noteOrd=eleElenco.getNoteOrdine();
						numCopieOrd=eleElenco.getNumCopieOrdine();
						cont=eleElenco.isContinuativo();
						statoOrd=eleElenco.getStatoOrdine();
						codDocOrd=eleElenco.getCodDocOrdine();
						codTipoDocOrd=eleElenco.getCodTipoDocOrdine();
						codUrgenzaOrd=eleElenco.getCodUrgenzaOrdine();
						codRicOffertaOrd=eleElenco.getCodRicOffertaOrdine();
						idOffertaFornOrd=eleElenco.getIdOffertaFornOrdine();
						forn=eleElenco.getFornitore();
						noteForn=eleElenco.getNoteFornitore();
						tipoInvioOrd=eleElenco.getTipoInvioOrdine();
						bil=eleElenco.getBilancio();
						codPrimoOrd=eleElenco.getCodPrimoOrdine();
						annoPrimoOrd=eleElenco.getAnnoPrimoOrdine();
						valutaOrd=eleElenco.getValutaOrdine();
						prezzoOrd=eleElenco.getPrezzoOrdine();
						prezzoEuroOrd=eleElenco.getPrezzoEuroOrdine();
						paeseOrd=eleElenco.getPaeseOrdine();
						sezioneAcqOrd=eleElenco.getSezioneAcqOrdine();
						codBibliotecaSuggOrd=eleElenco.getCodBibl();
						codSuggBiblOrd=eleElenco.getCodSuggBiblOrdine();
						tit=eleElenco.getTitolo();
						statoAbbOrd=eleElenco.getStatoAbbOrdine();
						periodoValAbbOrd=eleElenco.getPeriodoValAbbOrdine();
						annoAbbOrd=eleElenco.getAnnoAbbOrdine();
						numFascicoloAbbOrd=eleElenco.getNumFascicoloAbbOrdine();
						dataPubblFascicoloAbbOrd=eleElenco.getDataPubblFascicoloAbbOrdine();
						annataAbbOrd=eleElenco.getAnnataAbbOrdine();
						numVolAbbOrd=eleElenco.getNumVolAbbOrdine();
						dataFineAbbOrd=eleElenco.getDataFineAbbOrdine();
						regTribOrd=eleElenco.getRegTribOrdine();
						naturaOrd=eleElenco.getNaturaOrdine();
						rinn=eleElenco.isRinnovato();
						stamp=eleElenco.isStampato();
						tipoVar="";

						eleOrdine=new OrdiniVO(codP, codB,  codOrd,  annoOrd,  tipoOrd,  dataOrd,  noteOrd,  numCopieOrd,  cont, statoOrd,  codDocOrd, codTipoDocOrd,  codUrgenzaOrd,  codRicOffertaOrd,  idOffertaFornOrd,  forn,  noteForn,  tipoInvioOrd, bil,  codPrimoOrd,  annoPrimoOrd,  valutaOrd,  prezzoOrd,  prezzoEuroOrd,  paeseOrd,  sezioneAcqOrd,  codBibliotecaSuggOrd,  codSuggBiblOrd,  tit,  statoAbbOrd,  periodoValAbbOrd,  annoAbbOrd,  numFascicoloAbbOrd,  dataPubblFascicoloAbbOrd,  annataAbbOrd,   numVolAbbOrd,  dataFineAbbOrd,  regTribOrd,  naturaOrd, rinn, stamp,  tipoVar);
						eleOrdine.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(eleOrdine.getPrezzoEuroOrdine()));
						eleOrdine.setIDBil(eleElenco.getIDBil());
						risultati.add(eleOrdine);
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





	/**
	 * SinteticaOrdineAction.java
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

		appoSelezione=(String[]) currentForm.getSelectedOrdini();
		appoParametro=(String[])request.getSession().getAttribute("ordiniSelected");
		int nRighe=currentForm.getNumRigheOld();
		int nPos=currentForm.getPosElementoOld();
		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione,nRighe,nPos, currentForm.getListaOrdini());
			request.getSession().setAttribute("ordiniSelected", (String[])nuovoParametro);
			currentForm.setSelectedOrdini((String[]) nuovoParametro );
		}

	}	*/
	/**
	 * SinteticaOrdineAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati, int nRighe,int nPos,  List<OrdiniVO> lista)
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
			//ciclo sulla lista getListaOrdini (postata dal submit) per individuare i selezionati e i decheck
			for (int x=nPos;  x < nPos+ nRighe && x<lista.size(); x++)
			{
				OrdiniVO ord= (OrdiniVO) lista.get(x);
				//cod=ord.getCodOrdine();
				cod=ord.getChiave();
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

	private ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneBOVO configurazioneTrovata = new ConfigurazioneBOVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazione(configurazione);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		return configurazioneTrovata;
	}

	private String controllaTitolo(String bidPassato) throws Exception {
		String tito=null;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//List lista = null;
			TitoloACQVO tit=null;
			tit = factory.getGestioneAcquisizioni().getTitoloRox(bidPassato);
			if (tit!=null)
			{
				//TitoloACQVO titoloTrovatoElemento = (TitoloACQVO) lista.get(0);
				tito=tit.getIsbd();
			}
		} catch (Exception e) {
		    	e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
		}
		return tito;
	}


	private List  Stampato(List listaOggetti, String tipoOggetti,  String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List ris = factory.getGestioneAcquisizioni().gestioneStampato( listaOggetti,  tipoOggetti, bo);
		return ris;
	}

	private Boolean  StampaOrdini(List listaOggetti, List idList, String tipoOggetti, String utente, String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		Boolean ris = factory.getGestioneAcquisizioni().gestioneStampaOrdini( listaOggetti,  idList,  tipoOggetti,  utente,  bo);
		return ris;
	}


	private StrutturaTerna[] loadIntest() throws Exception {
		// caricamento dati intestazione
		StrutturaTerna[] listaIntest = new StrutturaTerna[1];
		//listaIntest[0]=new StrutturaTerna("1","Biblioteca Principale dell'ICCU","");
		listaIntest[0]=new StrutturaTerna("1","","");
		return listaIntest;
	}

	private StrutturaTerna[] loadFineStp() throws Exception {
		StrutturaTerna[] listaFineStp = new StrutturaTerna[1];
		listaFineStp[0]=new StrutturaTerna("1","","");

		//StrutturaTerna[] listaFineStp = new StrutturaTerna[2];
		//listaFineStp[0]=new StrutturaTerna("1","IL DIRETTORE","");
		//listaFineStp[1]=new StrutturaTerna("2","(Dott. Marco Rossi)","");
		return listaFineStp;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);
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
