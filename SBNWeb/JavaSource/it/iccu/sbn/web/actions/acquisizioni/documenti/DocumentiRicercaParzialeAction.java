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
package it.iccu.sbn.web.actions.acquisizioni.documenti;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.documenti.DocumentiRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
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
import org.apache.struts.actions.LookupDispatchAction;

public class DocumentiRicercaParzialeAction extends LookupDispatchAction implements SbnAttivitaChecker{


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca", "cerca");
		map.put("ricerca.button.indietro", "indietro");
		map.put("servizi.bottone.hlputente", "sifutente");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_SUGGERIMENTO_LETTORE, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sifutente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DocumentiRicercaParzialeForm currentForm = (DocumentiRicercaParzialeForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);


		//erogForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
		return mapping.findForward("sifutente");
	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DocumentiRicercaParzialeForm currentForm = (DocumentiRicercaParzialeForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();
			String sifUtente = request.getParameter("UTERICERCA");
			if (sifUtente != null) {
				String codUtente = (String) request.getAttribute(NavigazioneServizi.COD_UTENTE);
				if (codUtente != null && !codUtente.equals("false"))
					currentForm.setCodUtenteProg(codUtente);

				return mapping.getInputForward();
			}


			if (request.getSession().getAttribute(Constants.CURRENT_MENU)
					.equals("menu.acquisizioni.documenti")
					&& (currentForm.getCodBibl() == null || (currentForm
							.getCodBibl() != null && currentForm.getCodBibl()
							.trim().length() == 0))
					&& navi.getActionCaller() == null)

			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

				/*
				 * request.getSession().removeAttribute(
				 * "attributeListaSuppDocumentoVO");
				 * request.getSession().removeAttribute("documentiSelected");
				 * request
				 * .getSession().removeAttribute("criteriRicercaDocumento");
				 */}

			// ricDocumenti.setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
			// Navigation.getInstance(request).getUtente().getCodPolo();
			// impostazione del bibliotecario
			// this.ricSuggerimenti.setCodBibliotec(Navigation.getInstance(request).getUtente().getUserId());
			String ticket = navi.getUserTicket();
			// cod bibl da caricare
			// (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio = navi.getUtente().getCodBib();
			if (biblio != null
					&& (currentForm.getCodBibl() == null || (currentForm
							.getCodBibl() != null && currentForm.getCodBibl()
							.trim().length() == 0))) {
				currentForm.setCodBibl(biblio);
			}

			BibliotecaVO bibScelta = (BibliotecaVO) request
					.getAttribute("codBibDalista");
			if (bibScelta != null && bibScelta.getCod_bib() != null) {
				currentForm.setCodBibl(bibScelta.getCod_bib());
			}

			this.loadStatoSuggerimento(currentForm);
			// ricSuggerimenti.setStatoSuggerimento("A");
			currentForm.setTitoloDoc("");
			this.loadTipoOrdinamento(currentForm);

			/*
			 * if (bid!=null && bid.length()!=0 && desc!=null &&
			 * desc.length()!=0 && natura!=null && natura.length()!=0) {
			 * this.ricOrdini.setProvenienzaVAIA(true);
			 * this.ricOrdini.getTitolo().setCodice(bid);
			 * this.ricOrdini.getTitolo().setDescrizione(desc);
			 * this.ricOrdini.setNatura(natura); ListaSuppOrdiniVO
			 * eleRicerca=this.caricaParametriVAIA (bid,desc,natura);
			 * request.getSession
			 * ().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE,
			 * (ListaSuppOrdiniVO) eleRicerca); }
			 */

			ListaSuppDocumentoVO ricArr = (ListaSuppDocumentoVO) request
					.getSession().getAttribute("attributeListaSuppDocumentoVO");

			// controllo che non riceva l'attributo di sessione di una lista
			// supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null &&
			// this.ricOrdini.isProvenienzaVAIA())

			if ((ricArr != null && ricArr.getChiamante() != null && ricArr
					.getSelezioniChiamato() == null)) {
				// per il layout
				currentForm.setVisibilitaIndietroLS(true);
				// non c'è creazione
				if (ricArr.isModalitaSif()) {
					List<DocumentoVO> listaDocumenti;
					listaDocumenti = this.getListaDocumentiVO(ricArr); // va in
																		// errore
																		// se
																		// non
																		// ha
																		// risultati
					this.caricaAttributeListaSupp(currentForm, ricArr); // IMPOSTA
																		// CRITERI
																		// DI
																		// RICERCA
																		// SULLA
																		// PAGINA
					return mapping.findForward("cerca");
				} else {
					if (!currentForm.isRientroDaSif()) // per escludere il reset
														// dal ritorno dei
														// richiami di liste
														// supporto effettuati
														// da questa pagina
					{
						this.caricaAttributeListaSupp(currentForm, ricArr); // IMPOSTA
																			// CRITERI
																			// DI
																			// RICERCA
																			// SULLA
																			// PAGINA
					} else {
						currentForm.setRientroDaSif(false);
					}

					return mapping.getInputForward();
				}
			} else {
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
						// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
						this.caricaAttributeListaSupp( currentForm, (ListaSuppDocumentoVO) request.getSession().getAttribute("attributeListaSuppDocumentoVO"));
						currentForm.setVisibilitaIndietroLS(true);
						//return mapping.getInputForward();
						//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
						//return mapping.findForward("crea"); // NON ESISTE LA CREAZIONE DI SUGG LETT
						return mapping.getInputForward();
					}
					else
					{
						return mapping.getInputForward();
					}

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

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DocumentiRicercaParzialeForm ricDocumenti = (DocumentiRicercaParzialeForm) form;
		try {
			String chiama=null;
			//if (ricDocumenti.isLSRicerca())
			if (ricDocumenti.isVisibilitaIndietroLS())
			{
				ListaSuppDocumentoVO ricArr=(ListaSuppDocumentoVO) request.getSession().getAttribute("attributeListaSuppDocumentoVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppDocumentoVO eleRicerca=new ListaSuppDocumentoVO();
			request.getSession().removeAttribute("ultimoBloccoDoc");
			String ticket=Navigation.getInstance(request).getUserTicket();

			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricDocumenti.getCodBibl();
			String codDoc=ricDocumenti.getCodDoc();
			String statoSuggDoc=ricDocumenti.getStatoSuggerimento();
			StrutturaTerna ute=new StrutturaTerna("","","");
//			ute.setCodice2(ricDocumenti.getCodUtenteProg());
			ute.setCodice1(ricDocumenti.getCodUtenteBibl());//almaviva2 bug 0004033 collaudo
			ute.setCodice2(ServiziUtil.espandiCodUtente(ricDocumenti.getCodUtenteProg()));

			StrutturaCombo tit=new StrutturaCombo("","");
			tit.setDescrizione(ricDocumenti.getTitoloDoc());
			String dataDa=ricDocumenti.getDataInizio();
			String dataA=ricDocumenti.getDataFine();
			String edi="";
			String luogo="";
			StrutturaCombo pae=new StrutturaCombo("","");
			StrutturaCombo lin=new StrutturaCombo("","");
			String annoEdi="";
			//String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppDocumentoVO( codP,  codB,  codDoc,  statoSuggDoc,  ute,  tit,  dataDa,  dataA,   edi,  luogo,  pae,  lin,  annoEdi,    chiama,  ordina );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(ricDocumenti.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (ricDocumenti.getTipoOrdinamSelez()!=null && !ricDocumenti.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricDocumenti.getTipoOrdinamSelez());
			}

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppDocumentoVO", eleRicerca);
			// controllo di esistenza risultati su db se ci sono eccezioni
			List<DocumentoVO> listaDocumenti;
			listaDocumenti=this.getListaDocumentiVO(eleRicerca);
			return mapping.findForward("cerca");
			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DocumentiRicercaParzialeForm ricDocumenti = (DocumentiRicercaParzialeForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppDocumentoVO ricArr=(ListaSuppDocumentoVO) request.getSession().getAttribute("attributeListaSuppDocumentoVO");
			if (ricArr!=null )
			{
				//ListaSuppCambioVO eleRicArr=ricArr.get(0);
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
					return mapping.getInputForward();
				}
			}
			else
			{
				return mapping.getInputForward();
			}
			//return Navigation.getInstance(request)..goBack();
			//return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List<DocumentoVO> getListaDocumentiVO(ListaSuppDocumentoVO criRicerca) throws Exception
	{
	List<DocumentoVO> listaDocumenti=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaDocumenti = factory.getGestioneAcquisizioni().getRicercaListaDocumenti(criRicerca);
	return listaDocumenti;
	}

	private void caricaAttributeListaSupp(DocumentiRicercaParzialeForm ricDocumenti, ListaSuppDocumentoVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricDocumenti.setCodBibl(ricArr.getCodBibl());
		ricDocumenti.setCodDoc(ricArr.getCodDocumento());
		ricDocumenti.setCodUtenteBibl(ricArr.getUtente().getCodice1());
		ricDocumenti.setCodUtenteProg(ricArr.getUtente().getCodice2());
		ricDocumenti.setStatoSuggerimento(ricArr.getStatoSuggerimentoDocumento());
		ricDocumenti.setTitoloDoc(ricArr.getTitolo().getCodice());
		ricDocumenti.setDataInizio(ricArr.getDataSuggerimentoDocDa());
		ricDocumenti.setDataFine(ricArr.getDataSuggerimentoDocA());
		//String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
	}catch (Exception e) {	}
	}


	private void loadTipoOrdinamento(DocumentiRicercaParzialeForm ricDocumenti) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Codice Sugg.");
		lista.add(elem);
		elem = new StrutturaCombo("2","Stato");
		lista.add(elem);
		elem = new StrutturaCombo("3","Data (disc.)");
		lista.add(elem);

		ricDocumenti.setListaTipiOrdinam(lista);
	}
	private void loadStatoSuggerimento(DocumentiRicercaParzialeForm ricDocumenti) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricDocumenti.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento()));
	}


/*	private void loadStatoSuggerimento() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - Accettato");
		lista.add(elem);
		elem = new StrutturaCombo("W","W - Attesa di risposta");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - Ordinato");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - Rifiutato");
		lista.add(elem);

		ricDocumenti.setListaStatoSuggerimento(lista);
	}*/

	private void loadCodBiblioteche(DocumentiRicercaParzialeForm ricDocumenti) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","01");
		lista.add(elem);
		elem = new StrutturaCombo("02","02");
		lista.add(elem);
		elem = new StrutturaCombo("03","03");
		lista.add(elem);

		ricDocumenti.setListaCodBibl(lista);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_SUGGERIMENTO_LETTORE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
*/		}

		return false;	}

}
