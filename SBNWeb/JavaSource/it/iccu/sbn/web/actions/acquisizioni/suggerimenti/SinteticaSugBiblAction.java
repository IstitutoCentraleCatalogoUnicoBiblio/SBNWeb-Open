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
package it.iccu.sbn.web.actions.acquisizioni.suggerimenti;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AccettaSuggerimentiDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.suggerimenti.SinteticaSugBiblForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class SinteticaSugBiblAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.indietro", "indietro");
		map.put("ricerca.button.esamina", "esamina");
		map.put("ricerca.button.selTutti", "selTutti");
		map.put("ricerca.button.deselTutti", "deselTutti");
		map.put("ricerca.button.scegli", "scegli");
		map.put("ricerca.button.crea", "crea");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.ok", "ok");
		map.put("ricerca.button.stampa", "stampaOnLine");
		map.put("ricerca.button.accetta", "accetta");
		map.put("ricerca.button.rifiuta", "rifiuta");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar()  )
			{
				// gestione selezione check da  menu bar
				if 	(request.getSession().getAttribute("suggerimentiSelected")!= null && !request.getSession().getAttribute("suggerimentiSelected").equals(""))
				{
					currentForm.setSelectedSuggerimenti((String[]) request.getSession().getAttribute("suggerimentiSelected"));
				}

				return mapping.getInputForward();
			}
			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
			if (ricArr!=null)
			{
				currentForm.setOrdinamentoScelto(ricArr.getOrdinamento());
			}

			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				if (!ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
				{
					currentForm.setVisibilitaIndietroLS(true);
				}

				// per il layout

				// il bottone crea su sintetica non deve essere visibile in caso di lista di supporto e non solo quando si proviene da ricerca

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					currentForm.setLSRicerca(true); // fai rox 4
				//}
			}
			if (ricArr==null)
			{
				// l'attributo di sessione deve essere valorizzato
				currentForm.setRisultatiPresenti(false);
			}
			List<SuggerimentoVO> listaSuggerimenti=new ArrayList<SuggerimentoVO>();
			// deve essere escluso il caso di richiamo di lista supporto cambi
/*			if 	(request.getSession().getAttribute("listaSuggEmessa")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()!=null)))
			{
				listaSuggerimenti=(List<SuggerimentoVO>)request.getSession().getAttribute("listaSuggEmessa");
			}
			else
			{
				listaSuggerimenti=this.getListaSuggerimentiVO(ricArr ); // va in errore se non ha risultati
			}
*/
			if (ricArr!=null)
			{
				listaSuggerimenti=this.getListaSuggerimentiVO(ricArr ); // va in errore se non ha risultati
			}

			//this.loadStatoSuggerimento();
			currentForm.setListaSuggerimenti(listaSuggerimenti);
			currentForm.setNumSuggerimenti(currentForm.getListaSuggerimenti().size());

			// gestione automatismo check su unico elemento lista
			if (currentForm.getListaSuggerimenti().size()==1)
			{
				String [] appoSelProva= new String [1];
				appoSelProva[0]=currentForm.getListaSuggerimenti().get(0).getChiave().trim();
				//	"FI|2007|3";
				currentForm.setSelectedSuggerimenti(appoSelProva);
			}

			// gestione blocchi

			DescrittoreBloccoVO blocco1= null;
			//String ticket=Navigation.getInstance(request).getUserTicket();
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String ticket=utenteCollegato.getTicket();

			int maxElementiBlocco=10;

			if (ricArr.getElemXBlocchi()>0)
			{
				maxElementiBlocco=ricArr.getElemXBlocchi();

			}

			// ok blocco1=GestioneAcquisizioniBean.class.newInstance().gestBlock(ticket,listaOfferte,prova);
			//blocco1=SbnBusinessSessionBean.class.newInstance().saveBlocchi(ticket,listaOfferte,listaOfferte.size());
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// deve essere escluso il caso di richiamo di lista supporto offerte fornitore
			if 	(request.getSession().getAttribute("ultimoBloccoSugg")!=null && ((ricArr==null) || (ricArr!=null && ricArr.getChiamante()==null)))
			{
				blocco1=(DescrittoreBloccoVO) request.getSession().getAttribute("ultimoBloccoSugg");
				//n.b la lista è quella memorizzata nella variabile di sessione
			}
			else
			{
				blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaSuggerimenti,maxElementiBlocco);
				currentForm.setListaSuggerimenti(blocco1.getLista());
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
			if (currentForm.getNumSuggerimenti()==0)
			{
				currentForm.setRisultatiPresenti(false);
				return mapping.getInputForward();
			}
			else {
				// impostazione attributo sessione dei selezionati
				if 	(request.getSession().getAttribute("suggerimentiSelected")!= null)
				{
					currentForm.setSelectedSuggerimenti((String[]) request.getSession().getAttribute("suggerimentiSelected"));
				}
				//	controllo esistenza di precendenti operazioni di modifica ed aggiornamento dello stato della lista
				if (ricArr.getSelezioniChiamato()!=null)
				{
					for (int t=0;  t < ricArr.getSelezioniChiamato().size(); t++)
					{
						String eleSele=ricArr.getSelezioniChiamato().get(t).getChiave().trim();
						for (int v=0;  v < currentForm.getListaSuggerimenti().size(); v++)
						{
							String eleList= currentForm.getListaSuggerimenti().get(v).getChiave().trim();
							if (eleList.equals(eleSele))
							{
								String variato=ricArr.getSelezioniChiamato().get(t).getTipoVariazione();
								if (variato!=null && variato.length()!=0)
								{
									currentForm.getListaSuggerimenti().get(v).setTipoVariazione(variato);
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
			SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;

			if (currentForm.getSelectedSuggerimenti()==null || currentForm.getSelectedSuggerimenti().length==0) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

				return mapping.getInputForward();
			} else {



				List<SuggerimentoVO> listaSugg = new ArrayList<SuggerimentoVO> ();

				for (int v=0; v< currentForm.getSelectedSuggerimenti().length; v++)
				{
					for (int u=0; u<currentForm.getListaSuggerimenti().size(); u++)
					{
						if (currentForm.getSelectedSuggerimenti()[v].trim().equals(currentForm.getListaSuggerimenti().get(u).getChiave().trim()))
						  {
							listaSugg.add(currentForm.getListaSuggerimenti().get(u));
							break;
						  }
					}
				}


				if (listaSugg!=null && listaSugg.size()>0)
				{
						// DA RIPRISTINARE PER LA STAMPA (almaviva)
						request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_SUGGERIMENTI_BIBLIOTECARIO);
						//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
						request.setAttribute("DATI_STAMPE_ON_LINE", listaSugg);
						return mapping.findForward("stampaOL");

				}
		}

		return mapping.getInputForward();

	} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			return mapping.getInputForward();
		}

	}

	private List<SuggerimentoVO> getListaSuggerimentiVO(ListaSuppSuggerimentoVO criRicerca) throws Exception
	{
	List<SuggerimentoVO> listaSuggerimenti=new ArrayList<SuggerimentoVO>();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaSuggerimenti = factory.getGestioneAcquisizioni().getRicercaListaSuggerimenti(criRicerca);
	return listaSuggerimenti;
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
			if (currentForm.getProgrForm() > 0) {
				try {
						this.PreparaRicercaSuggerimentoSingle(currentForm, request,currentForm.getProgrForm()-1);
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
		SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
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
				currentForm.getListaSuggerimenti().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoSugg",bloccoSucc);
				currentForm.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoSugg",currentForm.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}


	public ActionForward accetta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {

			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

			return mapping.getInputForward();
		}
		try {
			String[] appoSelezione=new String[0];
			appoSelezione=currentForm.getSelectedSuggerimenti();

			if (appoSelezione==null || appoSelezione.length==0) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

				return mapping.getInputForward();
			}
			else
			{
				List<Integer> listaIDOrdini = new ArrayList<Integer> ();

				for (int v=0; v< currentForm.getSelectedSuggerimenti().length; v++)
				{
					for (int u=0; u<currentForm.getListaSuggerimenti().size(); u++)
					{
						if (currentForm.getSelectedSuggerimenti()[v].equals(currentForm.getListaSuggerimenti().get(u).getChiave().trim()))
						  {
							listaIDOrdini.add(Integer.parseInt(currentForm.getListaSuggerimenti().get(u).getCodiceSuggerimento()));
							break;
						  }
					}
				}
				OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO = new OperazioneSuOrdiniMassivaVO();

				operazioneSuOrdiniVO.setTipoOperazione("V");

				// impostare gli id degli ordini selezionati come array e passarlo

				operazioneSuOrdiniVO.setDatiInput(listaIDOrdini);

				operazioneSuOrdiniVO.setCodPolo(currentForm.getListaSuggerimenti().get(0).getCodPolo()); // assumo il codice del primo della lista
				operazioneSuOrdiniVO.setCodBib(currentForm.getListaSuggerimenti().get(0).getCodBibl()); // assumo il codice del primo della lista

				String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				operazioneSuOrdiniVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				String downloadLinkPath = "/";
				operazioneSuOrdiniVO.setDownloadPath(downloadPath);
				operazioneSuOrdiniVO.setDownloadLinkPath(downloadLinkPath);
				operazioneSuOrdiniVO.setTicket(Navigation.getInstance(request).getUserTicket());
				operazioneSuOrdiniVO.setUser(Navigation.getInstance(request).getUtente().getFirmaUtente());

				// new gestione batch inizio
                AccettaSuggerimentiDiffVO richiesta = new AccettaSuggerimentiDiffVO();
				richiesta.setCodPolo(operazioneSuOrdiniVO.getCodPolo());
				richiesta.setCodBib(operazioneSuOrdiniVO.getCodBib());
				richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
				richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO);
				//richiesta.setParametri(inputForStampeService);
				//richiesta.setTipoOrdinamento(ordinamFile);
				richiesta.setOperazioneSuOrdini(operazioneSuOrdiniVO);
				richiesta.setTicket(Navigation.getInstance(request).getUserTicket());

				//AmministrazionePolo  anministrazionePolo;

				String s =  null;
				try {
					s = DomainEJBFactory.getInstance().getPolo().prenotaElaborazioneDifferita(richiesta, null);
				} catch (ApplicationException e) {
					if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
					{

						LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

						return mapping.getInputForward();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (s == null) {

					LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotBatchKO"));

					resetToken(request);
					return mapping.getInputForward();
				}


				LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));

				resetToken(request);
				return mapping.getInputForward();

			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward rifiuta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {

			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

			return mapping.getInputForward();

		}

		try {
			String[] appoSelezione=new String[0];
			appoSelezione=currentForm.getSelectedSuggerimenti();

			if (appoSelezione==null || appoSelezione.length==0) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));

				return mapping.getInputForward();
			}
			else
			{
				List<Integer> listaIDOrdini = new ArrayList<Integer> ();

				for (int v=0; v< currentForm.getSelectedSuggerimenti().length; v++)
				{
					for (int u=0; u<currentForm.getListaSuggerimenti().size(); u++)
					{
						if (currentForm.getSelectedSuggerimenti()[v].equals(currentForm.getListaSuggerimenti().get(u).getChiave().trim()))
						  {
							listaIDOrdini.add(Integer.parseInt(currentForm.getListaSuggerimenti().get(u).getCodiceSuggerimento()));
							break;
						  }
					}
				}
				OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO = new OperazioneSuOrdiniMassivaVO();

				operazioneSuOrdiniVO.setTipoOperazione("F");

				// impostare gli id degli ordini selezionati come array e passarlo

				operazioneSuOrdiniVO.setDatiInput(listaIDOrdini);

				operazioneSuOrdiniVO.setCodPolo(currentForm.getListaSuggerimenti().get(0).getCodPolo()); // assumo il codice del primo della lista
				operazioneSuOrdiniVO.setCodBib(currentForm.getListaSuggerimenti().get(0).getCodBibl()); // assumo il codice del primo della lista

				String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				operazioneSuOrdiniVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				String downloadLinkPath = "/";
				operazioneSuOrdiniVO.setDownloadPath(downloadPath);
				operazioneSuOrdiniVO.setDownloadLinkPath(downloadLinkPath);
				operazioneSuOrdiniVO.setTicket(Navigation.getInstance(request).getUserTicket());
				operazioneSuOrdiniVO.setUser(Navigation.getInstance(request).getUtente().getFirmaUtente());

				// new gestione batch inizio
                AccettaSuggerimentiDiffVO richiesta = new AccettaSuggerimentiDiffVO();
				richiesta.setCodPolo(operazioneSuOrdiniVO.getCodPolo());
				richiesta.setCodBib(operazioneSuOrdiniVO.getCodBib());
				richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
				richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO);
				//richiesta.setParametri(inputForStampeService);
				//richiesta.setTipoOrdinamento(ordinamFile);
				richiesta.setOperazioneSuOrdini(operazioneSuOrdiniVO);
				richiesta.setTicket(Navigation.getInstance(request).getUserTicket());

				//AmministrazionePolo  anministrazionePolo;

				String s =  null;
				try {
					s = DomainEJBFactory.getInstance().getPolo().prenotaElaborazioneDifferita(richiesta, null);
				} catch (ApplicationException e) {
					if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
					{

						LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

						return mapping.getInputForward();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// new fine


				//String s = factory.getElaborazioniDifferite().operazioneSuOrdine(operazioneSuOrdiniVO);
                // ultimo // String s = factory.getElaborazioniDifferite().rifiutoSuggerimenti(operazioneSuOrdiniVO);

				// test inizio
//                AdeguamentoCalcoliVO adegua = new AdeguamentoCalcoliVO();
//                adegua.setCodPolo(sinDoc.getListaDocumenti().get(0).getCodPolo()); // assumo il codice del primo della lista
//                adegua.setCodBib(sinDoc.getListaDocumenti().get(0).getCodBibl()); // assumo il codice del primo della lista
//
//				basePath=this.servlet.getServletContext().getRealPath(File.separator);
//				adegua.setBasePath(basePath);
//				downloadPath = StampeUtil.getBatchFilesPath();
//				log.info("download path: " + downloadPath);
//				downloadLinkPath = "/";
//				//adegua.setDatiInput(listaIDOrdini);
//				adegua.setDownloadPath(downloadPath);
//				adegua.setDownloadLinkPath(downloadLinkPath);
//				adegua.setTicket(Navigation.getInstance(request).getUserTicket());
//				adegua.setUser(Navigation.getInstance(request).getUtente().getFirmaUtente());
//
//                String s = factory.getElaborazioniDifferite().adeguaCalcoli(adegua);
                // test fine

				if (s == null) {

					LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotBatchKO"));

					resetToken(request);
					return mapping.getInputForward();
				}


				LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));

				resetToken(request);
				return mapping.getInputForward();

			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
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
	SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
	try {
		// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
		// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
		ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");

		if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null) {

					//if ((appoParametro!=null && appoParametro.length!=0 ) || (appoSelezione!=null && appoSelezione.length!=0))
					//{
						//this.AggiornaParametriSintetica(request);
						request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", this.AggiornaRisultatiListaSupporto(currentForm, ricArr));
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
	SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
	try {
		String[] appoSelezione = currentForm.getSelectedSuggerimenti();

		if (appoSelezione!=null && appoSelezione.length!=0)
		{
			//this.AggiornaParametriSintetica(request);
			this.PreparaRicercaSuggerimento( currentForm, request);
			// si aggiorna l'attributo con l'elenco dei cambi trovati
			request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", this.AggiornaRisultatiListaSupporto(currentForm, (ListaSuppSuggerimentoVO)request.getSession().getAttribute("attributeListaSuppSuggerimentoVO")));
			request.getSession().setAttribute("suggerimentiSelected", appoSelezione);
			request.getSession().setAttribute("listaSuggEmessa", currentForm.getListaSuggerimenti());
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
/**
 * SinteticaSugBiblAction.java
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

	appoSelezione=(String[]) currentForm.getSelectedSuggerimenti();
	appoParametro=(String[])request.getSession().getAttribute("suggerimentiSelected");
	//int nRighe=this.sinSezione.getNumRigheOld();
	//int nPos=this.sinSezione.getPosElementoOld();
	//List <SezioneVO> listaOld=this.sinSezione.getSezioniVisualizzateOld();
	List <SuggerimentoVO>  listaOld=new ArrayList();

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
		request.getSession().setAttribute("suggerimentiSelected", (String[])nuovoParametro);
		currentForm.setSelectedSuggerimenti((String[]) nuovoParametro );
	}

}*/
/**
 * SinteticaSugBiblAction.java
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
/*private String[] aggiornaParametro(String[] parametro, String[] selezionati,  List<SuggerimentoVO> lista)
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
			SuggerimentoVO sug= (SuggerimentoVO) lista.get(x);
			cod=sug.getChiave().trim();
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
	SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
	String [] appoLista= new String [currentForm.getListaSuggerimenti().size()];
	int i;
	for (i=0;  i < currentForm.getListaSuggerimenti().size(); i++)
	{
		SuggerimentoVO sug= currentForm.getListaSuggerimenti().get(i);
		String cod=sug.getChiave().trim();
		appoLista[i]=cod;
	}
	currentForm.setSelectedSuggerimenti(appoLista);
	return mapping.getInputForward();
}

public ActionForward deselTutti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
throws Exception {
	SinteticaSugBiblForm currentForm = (SinteticaSugBiblForm) form;
	try {
		currentForm.setSelectedSuggerimenti(null);
		return mapping.getInputForward();
	} catch (Exception e) {
		return mapping.getInputForward();
	}
}

/**
 * SinteticaSugBiblAction.java
 * @param eleRicArr
 * @return
 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
 */
private ListaSuppSuggerimentoVO AggiornaRisultatiListaSupporto (SinteticaSugBiblForm currentForm, ListaSuppSuggerimentoVO eleRicArr)
{
	try {

		List<SuggerimentoVO> risultati=new ArrayList<SuggerimentoVO>();
		SuggerimentoVO eleCom=new SuggerimentoVO();
		String [] listaSelezionati=currentForm.getSelectedSuggerimenti();

		String codP;
		String codB;
		String codSugg;
		String statoSugg;
		String dataSugg;
		StrutturaCombo titSugg;
		StrutturaCombo biblSugg;
		StrutturaCombo sezSugg;
		String noteSugg;
		String noteForn;
		String noteBibl;

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < currentForm.getListaSuggerimenti().size(); j++)
			{
				SuggerimentoVO eleElenco=currentForm.getListaSuggerimenti().get(j);
				if (eleSel.equals(eleElenco.getChiave().trim()))
				{
					codP="";
					codB=eleElenco.getCodBibl();
					codSugg=eleElenco.getCodiceSuggerimento();
					statoSugg=eleElenco.getStatoSuggerimento();
					dataSugg=eleElenco.getDataSuggerimento();
					titSugg=eleElenco.getTitolo();
					biblSugg=eleElenco.getBibliotecario();
					sezSugg=eleElenco.getSezione();
					noteSugg=eleElenco.getNoteSuggerimento();
					noteForn=eleElenco.getNoteFornitore();
					noteBibl=eleElenco.getNoteBibliotecario();

					eleCom=new SuggerimentoVO(codP,  codB,  codSugg,  statoSugg, dataSugg,  titSugg, biblSugg,  sezSugg,  noteSugg, noteForn,  noteBibl );
					eleCom.setNaturaBid(eleElenco.getNaturaBid());
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
 * SinteticaSugBiblAction.java
 * @param request
 * Questo metodo viene chiamato dal bottone esamina per impostare un oggetto di sessione che contiene i
 * criteri di ricerca per individuare gli oggetti selezionati nella sintetica e poterli scorrere
 */
private void  PreparaRicercaSuggerimento(SinteticaSugBiblForm currentForm, HttpServletRequest request)
{
	try {
		List<ListaSuppSuggerimentoVO> ricerca=new ArrayList<ListaSuppSuggerimentoVO>();
		ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
		String [] listaSelezionati=currentForm.getSelectedSuggerimenti();

		// carica i criteri di ricerca da passare alla esamina
		for (int i=0;  i < listaSelezionati.length; i++)
		{
			String eleSel= listaSelezionati[i];
			for (int j=0;  j < currentForm.getListaSuggerimenti().size(); j++)
			{
				SuggerimentoVO eleElenco=currentForm.getListaSuggerimenti().get(j);
				String chiaveComposta=eleElenco.getChiave().trim();
				//chiaveComposta[3] codOrdine
				//String[] chiaveComposta=eleElenco.getChiave().split("\\|");
				//if (eleSel.equals(eleElenco.getCodOrdine()))
				if (eleSel.equals(chiaveComposta))
				{
					// carica i criteri di ricerca da passare alla esamina
					String polo="";
					String bibl=eleElenco.getCodBibl();
					String codSugg=eleElenco.getCodiceSuggerimento();
					String statoSugg="";
					String dataSuggDa="";
					String dataSuggA="";
					StrutturaCombo titSugg=new StrutturaCombo("","");
					StrutturaCombo biblSugg=new StrutturaCombo("","");
					StrutturaCombo sezSugg=new StrutturaCombo("","");
					String chiama=null;
					String ordina="";

					eleRicerca=new ListaSuppSuggerimentoVO( polo,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );
					if (currentForm.getOrdinamentoScelto()!=null && currentForm.getOrdinamentoScelto().trim().length()>0 )
					{
						eleRicerca.setOrdinamento(currentForm.getOrdinamentoScelto());
					}

					ricerca.add(eleRicerca);
				}
			}
		}
		request.getSession().setAttribute("criteriRicercaSuggerimento", ricerca);
	} catch (Exception e) {
	}
}

private void  PreparaRicercaSuggerimentoSingle(SinteticaSugBiblForm currentForm, HttpServletRequest request, int j)
{
	try {
		List<ListaSuppSuggerimentoVO> ricerca=new ArrayList<ListaSuppSuggerimentoVO>();
		ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
		SuggerimentoVO eleElenco=currentForm.getListaSuggerimenti().get(j);
		// carica i criteri di ricerca da passare alla esamina
		String polo="";
		String bibl=eleElenco.getCodBibl();
		String codSugg=eleElenco.getCodiceSuggerimento();
		String statoSugg="";
		String dataSuggDa="";
		String dataSuggA="";
		StrutturaCombo titSugg=new StrutturaCombo("","");
		StrutturaCombo biblSugg=new StrutturaCombo("","");
		StrutturaCombo sezSugg=new StrutturaCombo("","");
		String chiama=null;
		String ordina="";

		eleRicerca=new ListaSuppSuggerimentoVO( polo,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );
		if (currentForm.getOrdinamentoScelto()!=null && currentForm.getOrdinamentoScelto().trim().length()>0 )
		{
			eleRicerca.setOrdinamento(currentForm.getOrdinamentoScelto());
		}

		ricerca.add(eleRicerca);
		request.getSession().setAttribute("criteriRicercaSuggerimento", ricerca);
	} catch (Exception e) {
	}
}


public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
	if (idCheck.equals("CREA") ){
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);
			return true;
		} catch (Exception e) {

			return false;
			//return true; // temporaneamente per superare l'abilitazione negata a monte
		}
	}

	return false;
}

}
