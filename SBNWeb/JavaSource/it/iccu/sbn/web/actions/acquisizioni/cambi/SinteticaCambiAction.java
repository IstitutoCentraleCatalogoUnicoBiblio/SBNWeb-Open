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
package it.iccu.sbn.web.actions.acquisizioni.cambi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.cambi.SinteticaCambiForm;
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

public class SinteticaCambiAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	//private SinteticaCambiForm sinCambio;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.selTutti","selTutti");
		map.put("ricerca.button.deselTutti","deselTutti");
		map.put("ricerca.button.esamina","esamina");
		map.put("ricerca.button.indietro","indietro");
		//map.put("ricerca.button.carica","carica");
		map.put("ordine.label.valuta","valuta");
		//map.put("ricerca.label.dataVariazione","dataVariazione");

		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "ok");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.scegli","scegli");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
	throws Exception {

	//setto il token per le transazioni successive
	//this.saveToken(request);
	SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
	Navigation navi = Navigation.getInstance(request);
	if (navi.isFromBar())
	{
		// gestione selezione check da  menu bar
		if 	(request.getSession().getAttribute("cambiSelected")!= null && !request.getSession().getAttribute("cambiSelected").equals(""))
		{
			sinCambio.setSelectedCambi((String[]) request.getSession().getAttribute("cambiSelected"));
		}

		return mapping.getInputForward();
	}
	if(!sinCambio.isSessione())
	{
		sinCambio.setSessione(true);
	}
	try {
		ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
		if (ricArr!=null)
		{
			sinCambio.setOrdinamentoScelto(ricArr.getOrdinamento());
		}

		if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
		{
			// imposta visibilità bottone scegli
			sinCambio.setVisibilitaIndietroLS(true);
			// per il layout
			// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

			//if (ricArr.getChiamante().endsWith("RicercaParziale"))
			//{
				sinCambio.setLSRicerca(true); // fai rox 4
			//}

		}
		if (ricArr==null)
		{
			// l'attributo di sessione deve essere valorizzato
			sinCambio.setRisultatiPresenti(false);
		}

		//ListaSuppCambioVO eleRicArr=ricArr.get(0);
/*		// gestione del chiamante
		if (eleRicArr!=null && eleRicArr.getChiamante()!=null)
		{
			ActionForward action = new ActionForward();
			action.setName("RITORNA");
			action.setPath(sinCambio.getAction()+".do");
			return action;
		}
		// fine gestione del chiamante
*/
		List<CambioVO> listaCambi=new ArrayList();
		// deve essere escluso il caso di richiamo di lista supporto cambi
/*		if 	(request.getSession().getAttribute("listaCambiEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
		{
			listaCambi=(List<CambioVO>)request.getSession().getAttribute("listaCambiEmessa");
		}
		else
		{
			listaCambi=this.getListaCambiVO(ricArr); // va in errore se non ha risultati
		}
*/
		if (ricArr!=null)
		{
			listaCambi=this.getListaCambiVO(ricArr); // va in errore se non ha risultati
		}

		sinCambio.setListaCambi(listaCambi);
		sinCambio.setNumCambi(sinCambio.getListaCambi().size());

		// gestione automatismo check su unico elemento lista
		if (sinCambio.getListaCambi().size()==1)
		{
			String [] appoSelProva= new String [1];
			appoSelProva[0]=sinCambio.getListaCambi().get(0).getCodValuta();
			//	"FI|2007|3";
			sinCambio.setSelectedCambi(appoSelProva);
		}




		// gestione blocchi

		DescrittoreBloccoVO blocco1= null;
		String ticket = navi.getUserTicket();

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
		if 	(request.getSession().getAttribute("ultimoBloccoCambi")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
		{
			blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoCambi");
			//n.b la lista è quella memorizzata nella variabile di sessione
		}
		else
		{

			blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaCambi,maxElementiBlocco);
			sinCambio.setListaCambi(blocco1.getLista());
		}

		if (blocco1 != null)
		{
		//if (blocco1 == null)
		//abilito i tasti per il blocco se necessario
		//memorizzo le informazioni per la gestione blocchi
		sinCambio.setIdLista(blocco1.getIdLista()); //si
		sinCambio.setTotRighe(blocco1.getTotRighe()); //no
		sinCambio.setTotBlocchi(blocco1.getTotBlocchi()); //no
		sinCambio.setMaxRighe(blocco1.getMaxRighe()); //no
		//sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
		sinCambio.setBloccoSelezionato(blocco1.getNumBlocco()); //si
		//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
		//sinOfferte.setNumRighe(2);
		//sinOfferte.setNumBlocco(1);
		sinCambio.setUltimoBlocco(blocco1);
		sinCambio.setLivelloRicerca(navi.getUtente().getCodBib());
		}
		// fine gestione blocchi

		// non trovati
		if (sinCambio.getNumCambi()==0)
		{
				//request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI,null);
				//ActionMessages errors = new ActionMessages();
				//errors.add("generico", new ActionMessage("errors.acquisizioni.errorePagina"));
				//this.saveErrors(request, errors);
			sinCambio.setRisultatiPresenti(false);
			return mapping.getInputForward();

			//return mapping.findForward("indietro");

		}
		else {
			// int totPagine=(int) Math.round((sinCambio.getNumCambi()/sinCambio.getNumRighe())+0.5);
			// sinCambio.setTotPagine(totPagine);
			if 	(request.getSession().getAttribute("cambiSelected")!= null)
			{
				sinCambio.setSelectedCambi((String[]) request.getSession().getAttribute("cambiSelected"));
			}
			//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista

			if (ricArr.getSelezioniChiamato()!=null)
			{
				for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
				{
					String eleSele=ricArr.getSelezioniChiamato().get(t).getCodValuta();
					for (int v=0;  v < sinCambio.getListaCambi().size(); v++)
					{
						String eleList= sinCambio.getListaCambi().get(v).getCodValuta();
						if (eleList.equals(eleSele))
						{
							String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
							if (variato!=null && variato.length()!=0)
							{
								sinCambio.getListaCambi().get(v).setTipoVariazione(variato);
							}
							break;
						}
					}
				}
			}
/*			String tipoOrdEle="val";
			if (sinCambio.getOrdinaLista()!=null)
				{
					tipoOrdEle=sinCambio.getOrdinaLista().getDescrizione();
				}
			else
			{
				sinCambio.setOrdinaLista(new StrutturaCombo("A","val"));
				tipoOrdEle=sinCambio.getOrdinaLista().getDescrizione();
			}
			List <CambioVO> risLista=this.ElencaPer(tipoOrdEle);
			sinCambio.setListaCambi(risLista);
	*/
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
				sinCambio.setRisultatiPresenti(false);
			}

			return mapping.getInputForward();
	}
	// altri tipi di errore
	catch (Exception e) {
		ActionMessages errors = new ActionMessages();
		//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
		errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}
}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
			if (sinCambio.getProgrForm() > 0) {
			try {
					this.PreparaRicercaCambioSingle(  sinCambio,request,sinCambio.getProgrForm()-1);
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

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);

			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
						String[] appoSelezione=new String[0];
						appoSelezione=sinCambio.getSelectedCambi();
						request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI, this.AggiornaRisultatiListaSupporto( sinCambio,ricArr));
						request.getSession().setAttribute("cambiSelected", appoSelezione);

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
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
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

/*	public ActionForward dataVariazione(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;

		try {
			this.AggiornaParametriSintetica(request);
			if (sinCambio.getOrdinaLista().getDescrizione().equal("var"))
			{
				if (sinCambio.getOrdinaLista().getCodice().equal("A") ||  sinCambio.getOrdinaLista().getCodice().length()==0 ||  sinCambio.getOrdinaLista().getCodice()==null)
				{
					sinCambio.getOrdinaLista().setCodice("D");
					sinCambio.setListaCambi((List <CambioVO>)this.ElencaDescPer("var"));
				}
				else if (sinCambio.getOrdinaLista().getCodice().equal("D"))
				{
					sinCambio.getOrdinaLista().setCodice("A");
					sinCambio.setListaCambi((List <CambioVO>)this.ElencaPer("var"));
				}
			}
			else
			{
				sinCambio.setListaCambi((List <CambioVO>)this.ElencaPer("var"));
				sinCambio.getOrdinaLista().setCodice("A");
				sinCambio.getOrdinaLista().setDescrizione("var");
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward valuta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {

			this.AggiornaParametriSintetica(request);
			if (sinCambio.getOrdinaLista().getDescrizione().equal("val"))
			{
				if (sinCambio.getOrdinaLista().getCodice().equal("A") ||  sinCambio.getOrdinaLista().getCodice().length()==0 ||  sinCambio.getOrdinaLista().getCodice()==null)
				{
					sinCambio.getOrdinaLista().setCodice("D");
					sinCambio.setListaCambi((List <CambioVO>)this.ElencaDescPer("val"));
				}
				else if (sinCambio.getOrdinaLista().getCodice().equal("D"))
				{
					sinCambio.getOrdinaLista().setCodice("A");
					sinCambio.setListaCambi((List <CambioVO>)this.ElencaPer("val"));
				}
			}
			else
			{
				sinCambio.setListaCambi((List <CambioVO>)this.ElencaPer("val"));
				sinCambio.getOrdinaLista().setCodice("A");
				sinCambio.getOrdinaLista().setDescrizione("val");
			}

			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	*/

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {
			/*if (!isTokenValid(request)) {
				saveToken(request);
				if(!sinCambio.isSessione())
				{
					sinCambio.setSessione(true);
				}
				//return mapping.getInputForward();
			}
			resetToken(request);	*/
    		//request.setAttribute("chiamante",mapping.getPath());
				String[] appoParametro=new String[0];
				String[] appoSelezione=new String[0];
				appoSelezione=sinCambio.getSelectedCambi();

				if (appoSelezione!=null && appoSelezione.length!=0)

				{
					//this.AggiornaParametriSintetica(request);
					this.PreparaRicercaCambio(  sinCambio, request);
					// si aggiorna l'attributo con l'elenco dei cambi trovati
					request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI, this.AggiornaRisultatiListaSupporto( sinCambio, (ListaSuppCambioVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI)));
					request.getSession().setAttribute("cambiSelected", appoSelezione);
					request.getSession().setAttribute("listaCambiEmessa", sinCambio.getListaCambi());
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
			//return mapping.findForward("esamina");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward selTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {
/*			if (!isTokenValid(request)) {
				saveToken(request);
				if(!sinCambio.isSessione())
				{
					sinCambio.setSessione(true);
				}
				//return mapping.getInputForward();
			}
			resetToken(request);	*/

			// sinCambio.selectedCambi[i]


			String [] appoLista= new String [sinCambio.getListaCambi().size()];
			int i;
			for (i=0;  i < sinCambio.getListaCambi().size(); i++)
			{
				CambioVO camb= sinCambio.getListaCambi().get(i);
				String cod=camb.getCodValuta();
				//appoLista[i]=cod;
				//String cod=String.valueOf(i);
				appoLista[i]=cod;

				}
			sinCambio.setSelectedCambi(appoLista);
			request.getSession().setAttribute("cambiSelected", appoLista);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		try {
			if (!isTokenValid(request)) {
				sinCambio.setSelectedCambi(null);
				saveToken(request);
				if(!sinCambio.isSessione())
				{
					sinCambio.setSessione(true);
				}
			//	return mapping.getInputForward();
			}
			//resetToken(request);

			String[] appoSel=new String[0];
			sinCambio.setSelectedCambi(appoSel);

			request.getSession().setAttribute("cambiSelected", appoSel);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!sinCambio.isSessione()) {
			sinCambio.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = sinCambio.getBloccoSelezionato();
		String idLista = sinCambio.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				sinCambio.getListaCambi().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoCambi",bloccoSucc);
				sinCambio.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoCambi",sinCambio.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}

/*
	public ActionForward carica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaCambiForm sinCambio = (SinteticaCambiForm) form;

		try {
			sinCambio.setNumCambi(sinCambio.getListaCambi().size());
			// aggiungo 0.5 per arrotondarlo al intero superiore
			// controllare che il numero delle righe non sia il size perchè totpag viene =2
			int totPagine= (int)Math.round((sinCambio.getNumCambi()/sinCambio.getNumRighe())+0.5);
			// resto della divisione
			sinCambio.setTotPagine(totPagine);
			// la posizione dell'elemento nella lista si conteggia da zero per cui non aggiungo una unità
			sinCambio.setPosElemento((sinCambio.getNumRighe())*(sinCambio.getNumPagina()-1));

			this.AggiornaParametriSintetica(request);

			// errore
			if (Integer.valueOf(sinCambio.getNumPagina())>totPagine)
			{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.errorePagina"));
				this.saveErrors(request, errors);
				// reimposto la paginazione a quella precedente all'errore
				sinCambio.setNumRighe(sinCambio.getNumRigheOld());
				sinCambio.setPosElemento(sinCambio.getPosElementoOld());
				sinCambio.setNumPagina(sinCambio.getNumPaginaOld());
				return mapping.getInputForward();

			}

			sinCambio.setNumRigheOld(sinCambio.getNumRighe());
			sinCambio.setPosElementoOld(sinCambio.getPosElemento());

			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}

}

	public List<CambioVO> ElencaPer(String sortBy) throws EJBException {
		List<CambioVO> lst = sinCambio.getListaCambi();
		Comparator comp=null;
		if (sortBy==null) {
			comp =new CodValComparator();
			}
		else if (sortBy.equals("val")) {
			comp =new CodValComparator();
			}
		else if (sortBy.equals("var")) {
			comp =new DataVarComparator();
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


	public List<CambioVO> ElencaDescPer(String sortBy) throws EJBException {
		List<CambioVO> lst = sinCambio.getListaCambi();
		Comparator comp=null;
		if (sortBy.equals("val")) {
			comp =new CodValDescending();
			}
		else if (sortBy.equals("var")) {
			comp =new DataVarDescending();
			}

		if (lst != null)
		{
			if (comp != null)
			{
				//Collections.sort(lst, Collections.reverseOrder());
				Collections.sort(lst, comp);
			}
		}
		return lst;
	}


	private static class CodValComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((CambioVO) o1).getCodValuta();
					String e2 = ((CambioVO) o2).getCodValuta();
					return e1.compareTo(e2);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}
	private static class CodValDescending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((CambioVO) o1).getCodValuta();
				String e2 = ((CambioVO) o2).getCodValuta();
				return -e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private static class DataVarComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				try {
					String e1 = ((CambioVO) o1).getDataVariazione();
					String e2 = ((CambioVO) o2).getDataVariazione();
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
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

	private static class DataVarDescending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((CambioVO) o1).getDataVariazione();
				String e2 = ((CambioVO) o2).getDataVariazione();
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
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
	}*/

	private List<CambioVO> getListaCambiVO(ListaSuppCambioVO criRicerca) throws Exception
	{
		List<CambioVO> listaCambi;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//listaCambi = (List<CambioVO>) factory.getGestioneAcquisizioni().getRicercaListaCambi(criRicerca);
		listaCambi = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(criRicerca);

		return listaCambi;
	}

	/**
	 * SinteticaCambiAction.java
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

		appoSelezione=(String[]) sinCambio.getSelectedCambi();
		appoParametro=(String[])request.getSession().getAttribute("cambiSelected");
		int nRighe=sinCambio.getNumRigheOld();
		int nPos=sinCambio.getPosElementoOld();
		if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
		{
			nuovoParametro=this.aggiornaParametro(appoParametro, appoSelezione,nRighe,nPos, sinCambio.getListaCambi());
			request.getSession().setAttribute("cambiSelected", (String[])nuovoParametro);
			sinCambio.setSelectedCambi((String[]) nuovoParametro );
		}

	}
	*/
	/**
	 * SinteticaCambiAction.java
	 * @param request
	 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
	 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
	 */
	private void  PreparaRicercaCambio(SinteticaCambiForm sinCambio,HttpServletRequest request)
	{
		try {
			List<ListaSuppCambioVO> ricerca=new ArrayList();
			ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
			String [] listaSelezionati=sinCambio.getSelectedCambi();
			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinCambio.getListaCambi().size(); j++)
				{
					CambioVO eleElenco=sinCambio.getListaCambi().get(j);
					if (eleSel.equals(eleElenco.getCodValuta()))
					{
						String codP=eleElenco.getCodPolo();
						String codB=eleElenco.getCodBibl();
						String codVal=eleElenco.getCodValuta();
						String desVal=null;
						String chiama=null;
						eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
						if (sinCambio.getOrdinamentoScelto()!=null && sinCambio.getOrdinamentoScelto().trim().length()>0 )
						{
							eleRicerca.setOrdinamento(sinCambio.getOrdinamentoScelto());
						}
						ricerca.add(eleRicerca);
					}
				}
			}
			request.getSession().setAttribute("criteriRicercaCambio", ricerca);
		} catch (Exception e) {
		}
	}



	private void  PreparaRicercaCambioSingle(SinteticaCambiForm sinCambio, HttpServletRequest request, int j)
	{
		try {
			List<ListaSuppCambioVO> ricerca=new ArrayList();
			ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
			CambioVO eleElenco=sinCambio.getListaCambi().get(j);
			String codP=eleElenco.getCodPolo();
			String codB=eleElenco.getCodBibl();
			String codVal=eleElenco.getCodValuta();
			String desVal=null;
			String chiama=null;
			eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
			if (sinCambio.getOrdinamentoScelto()!=null && sinCambio.getOrdinamentoScelto().trim().length()>0 )
			{
				eleRicerca.setOrdinamento(sinCambio.getOrdinamentoScelto());
			}

			ricerca.add(eleRicerca);
			request.getSession().setAttribute("criteriRicercaCambio", ricerca);
		} catch (Exception e) {
		}
	}

	/**
	 * SinteticaCambiAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i cambi prodotti dalla ricerca
	 */
	private ListaSuppCambioVO AggiornaRisultatiListaSupporto (SinteticaCambiForm sinCambio,ListaSuppCambioVO eleRicArr)
	{
		//List<ListaSuppCambioVO> ricArrAppo=null;
		try {
			List<CambioVO> risultati=new ArrayList();
			CambioVO eleCambio=new CambioVO();
			String [] listaSelezionati=sinCambio.getSelectedCambi();
			String codP;
			String codB;
			String codVal;
			String desVal;
			Double tasso;
			String dataVar;
			// carica i criteri di ricerca da passare alla esamina
			for (int i=0;  i < listaSelezionati.length; i++)
			{
				String eleSel= listaSelezionati[i];
				for (int j=0;  j < sinCambio.getListaCambi().size(); j++)
				{
					CambioVO eleElenco=sinCambio.getListaCambi().get(j);
					if (eleSel.equals(eleElenco.getCodValuta()))
					{
						codP=eleElenco.getCodPolo();
						codB=eleElenco.getCodBibl();
						codVal=eleElenco.getCodValuta();
						desVal=eleElenco.getDesValuta();
						tasso=eleElenco.getTassoCambio();
						dataVar=eleElenco.getDataVariazione();
						String tipoVar="";
						eleCambio=new CambioVO(codP,  codB,  codVal,  desVal , tasso, dataVar, tipoVar);
						risultati.add(eleCambio);
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

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_CAMBI, utente.getCodPolo(), utente.getCodBib(), null);
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
	 * SinteticaCambiAction.java
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
/*	private String[] aggiornaParametro(String[] parametro, String[] selezionati, int nRighe,int nPos,  List<CambioVO> lista)
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
			for (int x=nPos;  x < nPos+ nRighe && x<lista.size(); x++)
			{
				CambioVO camb= (CambioVO) lista.get(x);
				cod=camb.getCodValuta();
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
	}*/

}
