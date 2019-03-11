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
package it.iccu.sbn.web.actions.acquisizioni.sezioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.InserisciSezioneForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

public class InserisciSezioneAction extends SinteticaLookupDispatchAction {
	//private InserisciSezioneForm insSezione;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.label.bibliolist", "biblioCerca");

		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_SEZIONE_ACQUISIZIONI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				InserisciSezioneForm insSezione = (InserisciSezioneForm) form;

				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!insSezione.isSessione())
				{
					insSezione.setSessione(true);
				}
				this.loadSezione( insSezione);
				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				if (biblio!=null &&   insSezione.getSezione()!=null &&   (insSezione.getSezione().getCodBibl()==null || (insSezione.getSezione().getCodBibl()!=null && insSezione.getSezione().getCodBibl().trim().length()==0)))
				{
					insSezione.getSezione().setCodBibl(biblio);
				}

				BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null)
				{
					insSezione.getSezione().setCodBibl(bibScelta.getCod_bib());
				}

				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				insSezione.getSezione().setCodPolo(polo);

				// preimpostazione della schermata di inserimento con i valori ricercati
				if (request.getSession().getAttribute("ATTRIBUTEListaSuppSezioneVO")!=null)
				{
					ListaSuppSezioneVO ele= (ListaSuppSezioneVO )request.getSession().getAttribute("ATTRIBUTEListaSuppSezioneVO");
					request.getSession().removeAttribute("ATTRIBUTEListaSuppSezioneVO");
					if (ele.getCodiceSezione()!=null && ele.getCodiceSezione().trim().length()>0)
					{
						insSezione.getSezione().setCodiceSezione(ele.getCodiceSezione());
					}
					if (ele.getDescrizioneSezione()!=null && ele.getDescrizioneSezione().trim().length()>0)
					{
						insSezione.getSezione().setDescrizioneSezione(ele.getDescrizioneSezione());
					}
				}

				ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);

				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					insSezione.setVisibilitaIndietroLS(true);
				}
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
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {
			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
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
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta

			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					// carico i risultati della selezione nella variabile da restituire
					request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, this.AggiornaRisultatiListaSupportoDaIns( insSezione, ricArr));
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
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaSezioneVO(insSezione.getSezione());
			// fine validazione
			ActionMessages errors = new ActionMessages();
    		insSezione.setConferma(true);
    		insSezione.setDisabilitaTutto(true);
    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();
		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);

				return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception {
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {
			insSezione.setConferma(false);
    		insSezione.setDisabilitaTutto(false);
			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			insSezione.getSezione().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
			//insSezione.getSezione().setBudgetSezione(insSezione.getBudgetStr());

		    String amount=insSezione.getSezione().getBudgetSezioneStr().trim();
		    Double risult=Pulisci.ControllaImporto(amount);
		    insSezione.getSezione().setBudgetSezione(risult);
		    // refresh del campo (da spostare nella form????? validate)
		    // serve per troncare eventuali cifre decimali immesse oltre la seconda
		    insSezione.getSezione().setBudgetSezioneStr(Pulisci.VisualizzaImporto(insSezione.getSezione().getBudgetSezione()));
		    if (ricArr!=null )
    			{
    				// gestione del chiamante
    				if (ricArr!=null && ricArr.getChiamante()!=null)
    				{
    					// carico i risultati della selezione nella variabile da restituire
    					request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, this.AggiornaRisultatiListaSupportoDaIns( insSezione, ricArr));
    				}
    			}
			// se il codice ordine è già valorzzato si deve procedere alla modifica
			if (insSezione.getSezione().getIdSezione()!=0 && insSezione.getSezione().getCodiceSezione().equals(insSezione.getSezioneInserita()))
			{
				if (!this.modificaSezione(insSezione.getSezione())) {
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
	    		if (this.inserisciSezione(insSezione.getSezione()))
				{
					// rilettura dell'oggetto per acquisire l'ID
					//this.loadSezioneINS( request, insSezione);
	    			ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.inserimentoOK"));
					this.saveErrors(request, errors);
					insSezione.setSezioneInserita(insSezione.getSezione().getCodiceSezione());
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
			InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			insSezione.setConferma(false);
			insSezione.setDisabilitaTutto(false);
			return mapping.getInputForward();
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {

			SezioneVO eleSez=insSezione.getSezione();
			if ( eleSez!=null && eleSez.getCodiceSezione()!=null && eleSez.getCodiceSezione().trim().length()>0)
			{
				// il record è già esistente
				// lettura
				SezioneVO eleSezLetto=this.loadDatiINS(eleSez, request);

				if (eleSezLetto!=null )
				{
					insSezione.setSezione(eleSezLetto);
				}
				//insSezione.setSezioneInserita(insSezione.getSezione().getCodiceSezione());
			}
			else
			{

				this.loadSezione( insSezione);
				if (eleSez!=null )
				{
					if (eleSez.getCodBibl()!=null &&  eleSez.getCodBibl().trim().length()>0)
					{
						insSezione.getSezione().setCodBibl(eleSez.getCodBibl());
					}
					if (eleSez.getCodPolo()!=null &&  eleSez.getCodPolo().trim().length()>0)
					{
						insSezione.getSezione().setCodPolo(eleSez.getCodPolo());
					}
				}

			}


			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciSezioneForm insSezione = (InserisciSezioneForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!insSezione.isSessione())
				{
					insSezione.setSessione(true);
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

	private void loadSezione(InserisciSezioneForm insSezione) throws Exception {
		// caricamento sezione
		Calendar c=new GregorianCalendar();
	 	int anno=c.get(Calendar.YEAR);
	 	String annoAttuale="";
	 	annoAttuale=Integer.valueOf(anno).toString();
		SezioneVO sez=new SezioneVO("", "", "", "", 0.00, "", annoAttuale,0.00,"" );
		insSezione.setSezione(sez);
		insSezione.getSezione().setBudgetSezioneStr("0,00");

	}


	private boolean inserisciSezione(SezioneVO sezione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//valRitorno = factory.getGestioneAcquisizioni().inserisciSezione(sezione);
		// prova hibernate
		valRitorno = factory.getGestioneAcquisizioni().inserisciSezioneHib(sezione);

		return valRitorno;
	}

	private boolean modificaSezione(SezioneVO sezione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//valRitorno = factory.getGestioneAcquisizioni().modificaSezione(sezione);
		// prova hibernate
		valRitorno = factory.getGestioneAcquisizioni().modificaSezioneHib(sezione);
		return valRitorno;
	}


	/**
	 * InserisciSezioneAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */

	private ListaSuppSezioneVO AggiornaRisultatiListaSupportoDaIns (InserisciSezioneForm insSezione, ListaSuppSezioneVO eleRicArr)
	{
		try {
			List<SezioneVO> risultati=new ArrayList();
			SezioneVO eleSezione=insSezione.getSezione();
			risultati.add(eleSezione);
			eleRicArr.setSelezioniChiamato(risultati);
		} catch (Exception e) {

		}
		return eleRicArr;
	}

	private void loadSezioneINS(HttpServletRequest request,InserisciSezioneForm insSezione) throws Exception {

		String chiama=null;
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		//String codB=Navigation.getInstance(request).getUtente().getCodBib();
		String codB=insSezione.getSezione().getCodBibl();
		String codSez=insSezione.getSezione().getCodiceSezione();
		String desSez="";  // insSezione.getSezione().getDescrizioneSezione();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		eleRicerca.setElemXBlocchi(999);
		eleRicerca.setOrdinamento("");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SezioneVO> sezioneTrovata = new ArrayList();
		eleRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Documento Fisico 09/05/08
		sezioneTrovata = factory.getGestioneAcquisizioni().getRicercaListaSezioni(eleRicerca);
		//gestire l'esistenza del risultato e che sia univoco
		insSezione.setSezione(sezioneTrovata.get(0));
		try {
			insSezione.getSezione().setBudgetSezioneStr(Pulisci.VisualizzaImporto( insSezione.getSezione().getBudgetSezione()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			insSezione.getSezione().setBudgetSezioneStr("0,00");
		}
	}

	private SezioneVO loadDatiINS(SezioneVO ele, HttpServletRequest request) throws Exception {

		SezioneVO eleLetto =null;

		String chiama=null;
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		String codSez=ele.getCodiceSezione();
		String desSez="";  // insSezione.getSezione().getDescrizioneSezione();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
/*		if(ele!=null &&  ele.getIdSezione()>0)
		{
			// imponendo l'id si salta nella getricercalistasezioni il controllo sulla condizione di chiusura che nel frattempo potrebbe essere avvenuta in tale istanza di modifica e quindi il ripristina potrebbe andare  male
			eleRicerca.setIdSezione(ele.getIdSezione());
		}	*/

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SezioneVO> sezioneTrovata = new ArrayList();
		eleRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Documento Fisico 09/05/08
		//gestire l'esistenza del risultato e che sia univoco
		try {
			sezioneTrovata = factory.getGestioneAcquisizioni().getRicercaListaSezioni(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (sezioneTrovata.size()>0)
		{
			eleLetto=sezioneTrovata.get(0);

		}

		try {
			eleLetto.setBudgetSezioneStr(Pulisci.VisualizzaImporto( eleLetto.getBudgetSezione()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			eleLetto.setBudgetSezioneStr("0,00");
		}
		return eleLetto;

	}



}


