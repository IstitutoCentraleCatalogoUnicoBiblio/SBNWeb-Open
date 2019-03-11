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
package it.iccu.sbn.web.actions.acquisizioni.offerte;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.actionforms.acquisizioni.offerte.SinteticaOfferteForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

public class SinteticaOfferteAction extends SinteticaLookupDispatchAction   {
	//private SinteticaOfferteForm sinOfferte;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
/*		map.put("ricerca.button.caricaBlocco","carica");
*/		map.put("ricerca.button.scegli","scegli");
		map.put("button.blocco", "caricaBlocco");
		map.put("ricerca.button.crea","crea");
		map.put("button.ok", "Ok");

		return map;
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
			if (sinOfferte.getProgrForm() > 0) {
					try {
							this.PreparaRicercaOffertaSingle( sinOfferte, request,sinOfferte.getProgrForm()-1);
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

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar()  )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("offerteSelected")!= null && !request.getSession().getAttribute("offerteSelected").equals(""))
				{
					sinOfferte.setSelectedOfferte((String[]) request.getSession().getAttribute("offerteSelected"));
				}

				return mapping.getInputForward();
			}
				if(!sinOfferte.isSessione())
				{
					sinOfferte.setSessione(true);
				}
				ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");
				if (ricArr!=null)
				{
					sinOfferte.setOrdinamentoScelto(ricArr.getOrdinamento());
				}

				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					sinOfferte.setVisibilitaIndietroLS(true);
					// per il layout
					if (ricArr.getChiamante().endsWith("RicercaParziale"))
					{
						sinOfferte.setLSRicerca(true); // fai rox 4
					}
				}
				if (ricArr==null)
				{
					// l'attributo di sessione deve essere valorizzato
					sinOfferte.setRisultatiPresenti(false);
				}
				List<OffertaFornitoreVO> listaOfferte=new ArrayList();

				// deve essere escluso il caso di richiamo di lista supporto offerte fornitore
/*				if 	(request.getSession().getAttribute("listaOfferteEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
				{
					listaOfferte=(List<OffertaFornitoreVO>)request.getSession().getAttribute("listaOfferteEmessa");
				}
				else
				{
					listaOfferte=this.getListaOfferteVO(ricArr);
				}
*/
				if (ricArr!=null)
				{
					listaOfferte=this.getListaOfferteVO(ricArr);
				}

				sinOfferte.setListaOfferte(listaOfferte);
				sinOfferte.setNumOfferte(sinOfferte.getListaOfferte().size());

				//sinOfferte.setNumNotizie(sinOfferte.getListaOfferte().size());
				//sinOfferte.setOffset(5);
				// gestione blocchi

				DescrittoreBloccoVO blocco1= null;
				//String ticket=Navigation.getInstance(request).getUserTicket();
				UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
				String ticket=utenteCollegato.getTicket();

				//listaOfferte.size()

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
				if 	(request.getSession().getAttribute("ultimoBloccoOfferte")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
				{
					blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoOfferte");
					//n.b la lista è quella memorizzata nella variabile di sessione
				}
				else
				{

					blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaOfferte,maxElementiBlocco);
					sinOfferte.setListaOfferte(blocco1.getLista());
				}

				if (blocco1 != null)
				{
				//if (blocco1 == null)
				//abilito i tasti per il blocco se necessario
				//memorizzo le informazioni per la gestione blocchi
				sinOfferte.setIdLista(blocco1.getIdLista());
				sinOfferte.setTotRighe(blocco1.getTotRighe());
				sinOfferte.setTotBlocchi(blocco1.getTotBlocchi());
				sinOfferte.setMaxRighe(blocco1.getMaxRighe());
				sinOfferte.setNumBlocco(blocco1.getNumBlocco());
				sinOfferte.setBloccoSelezionato(blocco1.getNumBlocco());
				//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
				//sinOfferte.setNumRighe(2);
				//sinOfferte.setNumBlocco(1);
				sinOfferte.setUltimoBlocco(blocco1);
				sinOfferte.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
				}

				// non trovati
				if (sinOfferte.getNumOfferte()==0)
				{
					sinOfferte.setRisultatiPresenti(false);
					return mapping.getInputForward();
				}
				else {
					// impostazione attributo sessione dei selezionati
					if 	(request.getSession().getAttribute("offerteSelected")!= null)
					{
						sinOfferte.setSelectedOfferte((String[]) request.getSession().getAttribute("offerteSelected"));
					}
					//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
					if (ricArr.getSelezioniChiamato()!=null)
					{
						for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
						{
							String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave().trim();
							for (int v=0;  v < sinOfferte.getListaOfferte().size(); v++)
							{
								String eleList= sinOfferte.getListaOfferte().get(v).getChiave().trim();
								if (eleList.equals(eleSele))
								{
									String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
									if (variato!=null && variato.length()!=0)
									{
										sinOfferte.getListaOfferte().get(v).setTipoVariazione(variato);
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
						sinOfferte.setRisultatiPresenti(false);
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
		SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sinOfferte.isSessione()) {
			sinOfferte.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinOfferte.getBloccoSelezionato();
		String idLista = sinOfferte.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinOfferte.getListaOfferte().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoOfferte",bloccoSucc);
				sinOfferte.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoOfferte",sinOfferte.getUltimoBlocco());
			}

		}

		return mapping.getInputForward();
	}


	private List<OffertaFornitoreVO> getListaOfferteVO(ListaSuppOffertaFornitoreVO criRicerca) throws Exception
	{
	List<OffertaFornitoreVO> listaOfferte=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaOfferte = factory.getGestioneAcquisizioni().getRicercaListaOfferte(criRicerca);
	return listaOfferte;
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");
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
	SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");

		if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					// carico i risultati della selezione nella variabile da restituire
					String[] appoSelezione=new String[0];
					appoSelezione=sinOfferte.getSelectedOfferte();
					request.getSession().setAttribute("attributeListaSuppOffertaFornitoreVO", this.AggiornaRisultatiListaSupporto( sinOfferte, ricArr));
					request.getSession().setAttribute("offerteSelected", appoSelezione);

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
	SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
	try {
		String[] appoParametro=new String[0];
		String[] appoSelezione=new String[0];
		appoSelezione=sinOfferte.getSelectedOfferte();
/*
		appoParametro=(String[])request.getSession().getAttribute("offerteSelected");
		// vedere se appoparametro è null

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			this.AggiornaParametriSintetica(request);
		}
		appoSelezione=(String[]) sinOfferte.getSelectedOfferte();
		appoParametro=(String[])request.getSession().getAttribute("offerteSelected");

		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
*/
		if (appoSelezione!=null && appoSelezione.length!=0)

		{
			//this.AggiornaParametriSintetica(request);
			this.PreparaRicercaOfferta( sinOfferte, request);
			// si aggiorna l'attributo con l'elenco dei cambi trovati
			request.getSession().setAttribute("attributeListaSuppOffertaFornitoreVO", this.AggiornaRisultatiListaSupporto(  sinOfferte, (ListaSuppOffertaFornitoreVO)request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO")));
			request.getSession().setAttribute("offerteSelected", appoSelezione);
			request.getSession().setAttribute("listaOfferteEmessa", sinOfferte.getListaOfferte());
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



public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
	String [] appoLista= new String [sinOfferte.getListaOfferte().size()];
	int i;
	for (i=0;  i < sinOfferte.getListaOfferte().size(); i++)
	{
		OffertaFornitoreVO off= sinOfferte.getListaOfferte().get(i);
		String cod=off.getChiave().trim();
		appoLista[i]=cod;
	}
	sinOfferte.setSelectedOfferte(appoLista);
	return mapping.getInputForward();
}

public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaOfferteForm sinOfferte = (SinteticaOfferteForm) form;
	try {
		sinOfferte.setSelectedOfferte(null);
		return mapping.getInputForward();
	} catch (Exception e) {
		return mapping.getInputForward();
	}
}

/**
 * SinteticaOfferteAction.java
 * @param eleRicArr
 * @return
 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
 */
private ListaSuppOffertaFornitoreVO AggiornaRisultatiListaSupporto (SinteticaOfferteForm sinOfferte, ListaSuppOffertaFornitoreVO eleRicArr)
{
	try {

		List<OffertaFornitoreVO> risultati=new ArrayList();
		OffertaFornitoreVO eleOff=new OffertaFornitoreVO();
		String [] listaSelezionati=sinOfferte.getSelectedOfferte();

		String codP;
		String codB;
		StrutturaCombo tipoProv;
		String idOff;
		String dataOff;
		StrutturaCombo forn;
		String statoOff;
		String valOff;
		String tipoPre;
		String pre;
		String infPre;
		String tipData;
		String data;
		StrutturaCombo pae;
		StrutturaCombo ling;
		String codStand;
		String numStand;
		StrutturaCombo bidOff;
		StrutturaCombo KTitIsdb;

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sinOfferte.getListaOfferte().size(); j++)
			{
				OffertaFornitoreVO eleElenco=sinOfferte.getListaOfferte().get(j);
				if (eleSel.equals(eleElenco.getChiave().trim()))
				{
					 codP="";
					 codB=eleElenco.getCodBibl();
					 tipoProv=eleElenco.getTipoProvenienza();
					 idOff=eleElenco.getIdentificativoOfferta();
					 dataOff=eleElenco.getDataOfferta();
					 forn=eleElenco.getFornitore();
					 statoOff=eleElenco.getStatoOfferta();
					 valOff=eleElenco.getValutaOfferta();
					 tipoPre=eleElenco.getTipoPrezzo();
					 pre=eleElenco.getPrezzo();
					 infPre=eleElenco.getInformazioniPrezzo();
					 tipData=eleElenco.getTipoData();
					 data=eleElenco.getData();
					 pae =eleElenco.getPaese();
					 ling=eleElenco.getLingua();
					 codStand=eleElenco.getCodiceStandard();
					 numStand=eleElenco.getNumeroStandard();
					 bidOff=eleElenco.getBid();
					 KTitIsdb=eleElenco.getChiaveTitoloIsbd();

					eleOff=new OffertaFornitoreVO( codP,  codB,  tipoProv,  idOff,  dataOff,  forn,  statoOff,  valOff,  tipoPre,  pre,  infPre,  tipData,  data,  pae ,  ling,  codStand,  numStand,  bidOff,  KTitIsdb );
					eleOff.setNaturaBid(eleElenco.getNaturaBid());
					eleOff.setIDOff(eleElenco.getIDOff());

					risultati.add(eleOff);
				}
			}
		}
		eleRicArr.setSelezioniChiamato(risultati);

	} catch (Exception e) {

	}
	return eleRicArr;
}

/**
 * SinteticaOfferteAction.java
 * @param request
 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
 */
private void  PreparaRicercaOfferta(SinteticaOfferteForm sinOfferte, HttpServletRequest request)
{

	try {
		List<ListaSuppOffertaFornitoreVO> ricerca=new ArrayList();
		ListaSuppOffertaFornitoreVO eleRicerca=new ListaSuppOffertaFornitoreVO();
		String [] listaSelezionati=sinOfferte.getSelectedOfferte();
		request.getSession().removeAttribute("criteriRicercaOfferta");

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < sinOfferte.getListaOfferte().size(); j++)
			{
				OffertaFornitoreVO eleElenco=sinOfferte.getListaOfferte().get(j);
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
					String idOff=eleElenco.getIdentificativoOfferta();
					StrutturaCombo forn=new StrutturaCombo("","");
					//forn.setCodice(ricOfferte.getFornitore().getCodice());
					//forn.setDescrizione(ricOfferte.getFornitore().getDescrizione());
					String statoOff="";
					StrutturaCombo pae=new StrutturaCombo("","");
					StrutturaCombo ling=new StrutturaCombo("","");
					StrutturaCombo bidOff=new StrutturaCombo("","");

					StrutturaCombo KTitIsdb=new StrutturaCombo("","");
					//KTitIsdb.setDescrizione(ricOfferte.getTitolo());

					StrutturaCombo aut=new StrutturaCombo("","");
					//aut.setCodice(ricOfferte.getAutore());

					StrutturaCombo classif=new StrutturaCombo("","");
					//classif.setCodice(ricOfferte.getClassificazione());

					String chiama=null;
					String ordina="";

					eleRicerca=new ListaSuppOffertaFornitoreVO( codP,  codB,   idOff,   forn,  statoOff,  pae ,  ling,   bidOff,  KTitIsdb, aut, classif,  chiama,  ordina );
					if (sinOfferte.getOrdinamentoScelto()!=null && sinOfferte.getOrdinamentoScelto().trim().length()>0 )
					{
						eleRicerca.setOrdinamento(sinOfferte.getOrdinamentoScelto());
					}

					eleRicerca.setTicket(ticket);

					ricerca.add(eleRicerca);
				}
			}
		}
		request.getSession().setAttribute("criteriRicercaOfferta", ricerca);
	} catch (Exception e) {
	}
}

private void  PreparaRicercaOffertaSingle(SinteticaOfferteForm sinOfferte, HttpServletRequest request, int j)
{

	try {
		List<ListaSuppOffertaFornitoreVO> ricerca=new ArrayList();
		ListaSuppOffertaFornitoreVO eleRicerca=new ListaSuppOffertaFornitoreVO();
		OffertaFornitoreVO eleElenco=sinOfferte.getListaOfferte().get(j);
			// carica i criteri di ricerca da passare alla esamina
			String ticket=Navigation.getInstance(request).getUserTicket();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleElenco.getCodBibl();
			String idOff=eleElenco.getIdentificativoOfferta();
			StrutturaCombo forn=new StrutturaCombo("","");
			//forn.setCodice(ricOfferte.getFornitore().getCodice());
			//forn.setDescrizione(ricOfferte.getFornitore().getDescrizione());
			String statoOff="";
			StrutturaCombo pae=new StrutturaCombo("","");
			StrutturaCombo ling=new StrutturaCombo("","");
			StrutturaCombo bidOff=new StrutturaCombo("","");

			StrutturaCombo KTitIsdb=new StrutturaCombo("","");
			//KTitIsdb.setDescrizione(ricOfferte.getTitolo());

			StrutturaCombo aut=new StrutturaCombo("","");
			//aut.setCodice(ricOfferte.getAutore());

			StrutturaCombo classif=new StrutturaCombo("","");
			//classif.setCodice(ricOfferte.getClassificazione());

			String chiama=null;
			String ordina="";

			eleRicerca=new ListaSuppOffertaFornitoreVO( codP,  codB,   idOff,   forn,  statoOff,  pae ,  ling,   bidOff,  KTitIsdb, aut, classif,  chiama,  ordina );
			if (sinOfferte.getOrdinamentoScelto()!=null && sinOfferte.getOrdinamentoScelto().trim().length()>0 )
			{
				eleRicerca.setOrdinamento(sinOfferte.getOrdinamentoScelto());
			}

			eleRicerca.setTicket(ticket);

			ricerca.add(eleRicerca);
		request.getSession().setAttribute("criteriRicercaOfferta", ricerca);
	} catch (Exception e) {
	}
}




}
