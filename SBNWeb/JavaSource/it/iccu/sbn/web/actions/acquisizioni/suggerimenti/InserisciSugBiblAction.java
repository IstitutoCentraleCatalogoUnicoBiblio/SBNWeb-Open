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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.suggerimenti.InserisciSugBiblForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class InserisciSugBiblAction extends SugBiblBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.accetta","accetta");
		map.put("ricerca.button.rifiuta","rifiuta");
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ordine.bottone.searchTit", "sifbid");
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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;

				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!insSugg.isSessione())
				{
					insSugg.setSessione(true);
				}
				if (!insSugg.isCaricamentoIniziale())
				{
					this.loadDatiSuggerimento(insSugg, request);
					insSugg.setCaricamentoIniziale(true);
				}
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				if (biblio!=null && insSugg.getDatiSuggerimento()!=null  && (insSugg.getDatiSuggerimento().getCodBibl()==null || (insSugg.getDatiSuggerimento().getCodBibl()!=null && insSugg.getDatiSuggerimento().getCodBibl().trim().length()==0)))
				{
					insSugg.getDatiSuggerimento().setCodBibl(biblio);
				}

				BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null)
				{
					insSugg.getDatiSuggerimento().setCodBibl(bibScelta.getCod_bib());
				}

				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				insSugg.getDatiSuggerimento().setCodPolo(polo);

				// preimpostazione della schermata di inserimento con i valori ricercati

				if (request.getSession().getAttribute("ATTRIBUTEListaSuppSuggerimentoVO")!=null)
				{
					ListaSuppSuggerimentoVO ele=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("ATTRIBUTEListaSuppSuggerimentoVO");
					request.getSession().removeAttribute("ATTRIBUTEListaSuppSuggerimentoVO");
					if (ele.getBibliotecario()!=null )
					{
						insSugg.getDatiSuggerimento().setBibliotecario(ele.getBibliotecario());
					}
					if (ele.getTitolo()!=null )
					{
						insSugg.getDatiSuggerimento().setTitolo(ele.getTitolo());;
					}
//					 MANCA GESTIONE DI RIGA ORDINE
				}


				//insSugg.getDatiSuggerimento().setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
				// impostazione del bibliotecario
				//insSugg.getDatiSuggerimento().getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getUserId());
				this.loadStatoSuggerimento(insSugg);


				ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					if (!ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
					{
						insSugg.setVisibilitaIndietroLS(true);
					}
				}
				//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
				//almaviva5_20130513 #4861
				controlloSezione(mapping, request, insSugg);

				// controllo se ho un risultato da interrogazione ricerca
				String bid=(String) request.getAttribute("bid");
				if (bid!=null && bid.length()!=0 )
				{
					String titolo=(String) request.getAttribute("titolo");
					// controllo se ho un risultato da interrogazione ricerca
					//String acq = request.getParameter("ACQUISIZIONI");
					//if ( acq != null) {
					insSugg.getDatiSuggerimento().getTitolo().setCodice(bid);
					if ( titolo != null) {
						insSugg.getDatiSuggerimento().getTitolo().setDescrizione(titolo);
					}
				}
				// temporaneamente
				insSugg.getDatiSuggerimento().setBibliotecario(new StrutturaCombo("",""));
				//insSugg.getDatiSuggerimento().getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getFirmaUtente());
				//insSugg.getDatiSuggerimento().getBibliotecario().setCodice(String.valueOf(Navigation.getInstance(request).getUtente().getIdUtenteProfessionale()));
				insSugg.getDatiSuggerimento().getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getUserId());
				insSugg.getDatiSuggerimento().getBibliotecario().setDescrizione(String.valueOf(Navigation.getInstance(request).getUtente().getIdUtenteProfessionale()));
				insSugg.getDatiSuggerimento().setNominativoBibliotecario(String.valueOf(Navigation.getInstance(request).getUtente().getNome_cognome()));

				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				/*				ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
							this.saveErrors(request, errors);
				*/
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

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(ricArr.getChiamante()+".do");
				return action;
			}
			else
			{
				return mapping.findForward("indietro");
			}					} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta

			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
			if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					// carico i risultati della selezione nella variabile da restituire
					request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", this.AggiornaRisultatiListaSupportoDaIns(insSugg, ricArr));
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



	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		try {
			if(!insSugg.isSessione()) {
				insSugg.setSessione(true);
			}
			// validazione
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaSuggerimentoVO(insSugg.getDatiSuggerimento());
			// fine validazione

			ActionMessages errors = new ActionMessages();
    		insSugg.setConferma(true);
    		insSugg.setDisabilitaTutto(true);
    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
    		insSugg.setConferma(false);
    		insSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
    		insSugg.setConferma(false);
    		insSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		try {
			insSugg.setConferma(false);
    		insSugg.setDisabilitaTutto(false);
			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
			insSugg.getDatiSuggerimento().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

    		if (ricArr!=null )
    			{
    				// gestione del chiamante
    				if (ricArr!=null && ricArr.getChiamante()!=null)
    				{
    					// carico i risultati della selezione nella variabile da restituire
    					request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", this.AggiornaRisultatiListaSupportoDaIns(insSugg,ricArr));
    				}
    			}

			// se il codice ordine è già valorzzato si deve procedere alla modifica
			if (insSugg.getDatiSuggerimento().getCodiceSuggerimento()!=null  && insSugg.getDatiSuggerimento().getCodiceSuggerimento().length()>0)
			{
				if (!this.modificaSuggerimento(insSugg.getDatiSuggerimento())) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreModifica"));
					this.saveErrors(request, errors);
					//return mapping.getInputForward();
				}
				else
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.modificaOK"));
					this.saveErrors(request, errors);
					return ripristina( mapping,  form,  request,  response);
				}

			}
			else
			{
	    		if (this.inserisciSuggerimento(insSugg.getDatiSuggerimento()))
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.inserimentoOK"));
					this.saveErrors(request, errors);
					return ripristina( mapping,  form,  request,  response);

				}
				else
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.erroreinserimento"));
					this.saveErrors(request, errors);

				}
			}
			return mapping.getInputForward();

			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
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



	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			insSugg.setConferma(false);
			insSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		try {

			SuggerimentoVO eleSugg=insSugg.getDatiSuggerimento();
			if ( eleSugg!=null && eleSugg.getCodiceSuggerimento()!=null && eleSugg.getCodiceSuggerimento().trim().length()>0)
			{
				// il record è già esistente
				// lettura
				SuggerimentoVO eleSuggLetto=this.loadDatiINS(eleSugg, request);

				if (eleSuggLetto!=null )
				{
					insSugg.setDatiSuggerimento(eleSuggLetto);
				}

			}
			else
			{
				this.loadDatiSuggerimento(insSugg, request);
				if (eleSugg!=null )
				{
					if (eleSugg.getCodBibl()!=null &&  eleSugg.getCodBibl().trim().length()>0)
					{
						insSugg.getDatiSuggerimento().setCodBibl(eleSugg.getCodBibl());
					}
					if (eleSugg.getCodPolo()!=null &&  eleSugg.getCodPolo().trim().length()>0)
					{
						insSugg.getDatiSuggerimento().setCodPolo(eleSugg.getCodPolo());
					}
				}

			}



			//insSugg.getDatiSuggerimento().setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
			// impostazione del bibliotecario
			//insSugg.getDatiSuggerimento().getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getUserId());

			if (insSugg.getDatiSuggerimento()!=null && insSugg.getDatiSuggerimento().getStatoSuggerimento()!=null &&  (insSugg.getDatiSuggerimento().getStatoSuggerimento().equals("A") || insSugg.getDatiSuggerimento().getStatoSuggerimento().equals("O") || insSugg.getDatiSuggerimento().getStatoSuggerimento().equals("R") )) {
				insSugg.setDisabilitaTutto(true);
			} else 	{
				insSugg.setDisabilitaTutto(false);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		try {
			if ( insSugg.getDatiSuggerimento().getTitolo()!=null && insSugg.getDatiSuggerimento().getTitolo().getCodice()!=null)
			{
				request.setAttribute("bidFromRicOrd", insSugg.getDatiSuggerimento().getTitolo().getCodice());
			}

			return mapping.findForward("sifbid");
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}


/*	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		insCom = (InserisciComForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!this.insCom.isSessione())
				{
					this.insCom.setSessione(true);
				}
				return mapping.getInputForward();
			}
		resetToken(request);
//		return mapping.findForward("cancella");
		return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	*/
	private void loadDatiSuggerimento(InserisciSugBiblForm insSugg, HttpServletRequest request) throws Exception {
		// ASSEGNAZIONE DELLA data di sistema
		Date dataodierna=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdiernaStr =formato.format(dataodierna);

		//Navigation.getInstance(request).getUtente().getCodPolo();

		SuggerimentoVO sugg=new SuggerimentoVO("", "", "", "W",dataOdiernaStr, new StrutturaCombo("",""),new StrutturaCombo("0",""),new StrutturaCombo("",""),  "", "", "");
		insSugg.setDatiSuggerimento(sugg);
		insSugg.getDatiSuggerimento().setBibliotecario(new StrutturaCombo("",""));
		insSugg.getDatiSuggerimento().getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getUserId());
		insSugg.getDatiSuggerimento().getBibliotecario().setDescrizione(String.valueOf(Navigation.getInstance(request).getUtente().getIdUtenteProfessionale()));
		insSugg.getDatiSuggerimento().setNominativoBibliotecario(String.valueOf(Navigation.getInstance(request).getUtente().getNome_cognome()));

	}

	private SuggerimentoVO loadDatiINS(SuggerimentoVO ele,  HttpServletRequest request ) throws Exception
	{

		SuggerimentoVO eleLetto =null;

		ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
		String codP=ele.getCodPolo();
		String bibl=ele.getCodBibl();
		String codSugg=ele.getCodiceSuggerimento();
		String statoSugg="";
		String dataSuggDa="";
		String dataSuggA="";
		StrutturaCombo titSugg=new StrutturaCombo("","");
		StrutturaCombo biblSugg=new StrutturaCombo("","");
		StrutturaCombo sezSugg=new StrutturaCombo("","");
		String chiama=null;
		String ordina="";

		eleRicerca=new ListaSuppSuggerimentoVO( codP,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SuggerimentoVO> recordTrovati = new ArrayList<SuggerimentoVO>();
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaSuggerimenti(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0)
		{
			eleLetto=recordTrovati.get(0);
			//insSugg.getDatiSuggerimento().setBibliotecario(new StrutturaCombo("",""));
/*			eleLetto.getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getUserId());
			eleLetto.getBibliotecario().setDescrizione(String.valueOf(Navigation.getInstance(request).getUtente().getIdUtenteProfessionale()));
			eleLetto.setNominativoBibliotecario(String.valueOf(Navigation.getInstance(request).getUtente().getNome_cognome()));
*/		}
		return eleLetto;
	}



	private boolean inserisciSuggerimento(SuggerimentoVO suggerimento) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciSuggerimento(suggerimento);
		return valRitorno;
	}

	private boolean modificaSuggerimento(SuggerimentoVO suggerimento) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaSuggerimento(suggerimento);
		return valRitorno;
	}


	/**
	 * InserisciSugBibkAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */

	private ListaSuppSuggerimentoVO AggiornaRisultatiListaSupportoDaIns (InserisciSugBiblForm insSugg, ListaSuppSuggerimentoVO eleRicArr)
	{
		try {
			List<SuggerimentoVO> risultati=new ArrayList<SuggerimentoVO>();
			SuggerimentoVO eleSuggerimento=insSugg.getDatiSuggerimento();
			risultati.add(eleSuggerimento);
			eleRicArr.setSelezioniChiamato(risultati);
		} catch (Exception e) {

		}
		return eleRicArr;
	}

	private void loadStatoSuggerimento(InserisciSugBiblForm insSugg) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		insSugg.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_STATO_SUGGERIMENTO)));
	}

	public ActionForward accetta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		String statoOLD="W";
		if (insSugg.getDatiSuggerimento()!=null && insSugg.getDatiSuggerimento().getStatoSuggerimento()!=null && insSugg.getDatiSuggerimento().getStatoSuggerimento().trim().length()>0 )
		{
			statoOLD=insSugg.getDatiSuggerimento().getStatoSuggerimento().trim();
		}
		try {
//			return mapping.findForward("accetta");
			// cambia stato al messaggio e disabilita tutto
			insSugg.getDatiSuggerimento().setStatoSuggerimento("A");
    		//esaSugg.setDisabilitaTutto(true);
			SuggerimentoVO eleSuggerimento=insSugg.getDatiSuggerimento();
			if (!this.modificaSuggerimento(eleSuggerimento)) {
				insSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreModifica"));
				this.saveErrors(request, errors);
				//return mapping.getInputForward();
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
				"errors.acquisizioni.modificaOK"));
				this.saveErrors(request, errors);
				return ripristina( mapping,  form,  request,  response);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			insSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
			return mapping.getInputForward();
		}
	}
	public ActionForward rifiuta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSugBiblForm insSugg = (InserisciSugBiblForm) form;
		String statoOLD="W";
		if (insSugg.getDatiSuggerimento()!=null && insSugg.getDatiSuggerimento().getStatoSuggerimento()!=null && insSugg.getDatiSuggerimento().getStatoSuggerimento().trim().length()>0 )
		{
			statoOLD=insSugg.getDatiSuggerimento().getStatoSuggerimento().trim();
		}
		try {
//			return mapping.findForward("rifiuta");
			// cambia stato al messaggio e disabilita tutto
			insSugg.getDatiSuggerimento().setStatoSuggerimento("R");
    		//esaSugg.setDisabilitaTutto(true);
			SuggerimentoVO eleSuggerimento=insSugg.getDatiSuggerimento();
			if (!this.modificaSuggerimento(eleSuggerimento)) {
				insSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreModifica"));
				this.saveErrors(request, errors);
				//return mapping.getInputForward();
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
				"errors.acquisizioni.modificaOK"));
				this.saveErrors(request, errors);
				return ripristina( mapping,  form,  request,  response);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			insSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
			return mapping.getInputForward();
		}
	}


/*	private void loadDatiSuggerimento() throws Exception {

//		(String codP, String codB, String codSugg, String statoSugg,String dataSugg, StrutturaCombo titSugg,StrutturaCombo biblSugg, StrutturaCombo sezSugg, String noteSugg,String noteForn, String noteBibl )
		SuggerimentoVO sugg=new SuggerimentoVO("X10", "01", "", "W","24/10/2006", new StrutturaCombo("",""),new StrutturaCombo("5","bibliotecario cinque"),new StrutturaCombo("",""),  "", "", "");
		insSugg.setDatiSuggerimento(sugg);
	}

	private void loadStatoSuggerimento() throws Exception {
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

		insSugg.setListaStatoSuggerimento(lista);
	}*/
}
