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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.EsaminaSezioneForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
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

public class EsaminaSezioneAction extends  SinteticaLookupDispatchAction implements SbnAttivitaChecker {
	//private EsaminaSezioneForm esaSezione;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
		map.put("button.crea.profiliAcquisto","profili");
		map.put("button.crea.importaBib","importaBibl");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
		map.put("button.esaminaSpesa","esaminaSpesaSezione");
		map.put("button.esaminaStoria","esaminaStoria");
		return map;
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!esaSezione.isSessione())
				{
					esaSezione.setSessione(true);
				}

				// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
				// DISABILITAZIONE DELL'INPUT
				ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				// controllo che non riceva l'attributo di sessione di una lista supporto
				if (ricArr!=null  && ricArr.getChiamante()!=null)
				{
					esaSezione.setEsaminaInibito(true);
					esaSezione.setDisabilitaTutto(true);
				}

				esaSezione.setListaDaScorrere((List<ListaSuppSezioneVO>) request.getSession().getAttribute("criteriRicercaSezione"));
				if(esaSezione.getListaDaScorrere() != null && esaSezione.getListaDaScorrere().size()!=0)
				{
					if(esaSezione.getListaDaScorrere().size()>1 )
					{
						esaSezione.setEnableScorrimento(true);
						//esaCambio.setPosizioneScorrimento(0);
					}
					else
					{
						esaSezione.setEnableScorrimento(false);
					}
				}
				// || strIsAlfabetic(String.valueOf(this.esaSezione.getPosizioneScorrimento()))
				if (String.valueOf(esaSezione.getPosizioneScorrimento()).length()==0 )
				{
					esaSezione.setPosizioneScorrimento(0);
				}
				// richiamo ricerca su db con elemento 1 di ricerca
				this.loadDatiSezionePassata( esaSezione, esaSezione.getListaDaScorrere().get(esaSezione.getPosizioneScorrimento()), request);
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

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward esaminaSpesaSezione(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
		// variabile di sessione
		request.getSession().setAttribute("sezioneEsaminata",esaSezione.getSezione().getIdSezione());
		//request.getSession().setAttribute("sezioneEsaminata2",(SezioneVO) esaSezione.getSezione());

		return mapping.findForward("esamSpesa");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward esaminaStoria(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
		// variabile di sessione
		request.getSession().setAttribute("sezioneEsaminata",esaSezione.getSezione().getIdSezione());
		//request.getSession().setAttribute("sezioneEsaminata2",(SezioneVO) esaSezione.getSezione());

		return mapping.findForward("esameStoria");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaSezioneVO(esaSezione.getSezione());
			// fine validazione
			ActionMessages errors = new ActionMessages();
			esaSezione.setConferma(true);
			esaSezione.setPressioneBottone("salva");
    		esaSezione.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			esaSezione.setConferma(false);
			esaSezione.setPressioneBottone("");
    		esaSezione.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			esaSezione.setConferma(false);
			esaSezione.setPressioneBottone("");
    		esaSezione.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			this.loadDatiSezionePassata( esaSezione, esaSezione.getListaDaScorrere().get(esaSezione.getPosizioneScorrimento()), request);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			esaSezione.setConferma(false);
    		esaSezione.setDisabilitaTutto(false);
    		esaSezione.getSezione().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			if (esaSezione.getPressioneBottone().equals("salva")) {
				esaSezione.setPressioneBottone("");

				SezioneVO eleSezione=esaSezione.getSezione();
			    String amount=esaSezione.getSezione().getBudgetSezioneStr().trim();
			    Double risult=Pulisci.ControllaImporto(amount);
			    esaSezione.getSezione().setBudgetSezione(risult);
			    // serve per troncare eventuali cifre decimali immesse oltre la seconda
			    esaSezione.getSezione().setBudgetSezioneStr(Pulisci.VisualizzaImporto(esaSezione.getSezione().getBudgetSezione()));


				ListaSuppSezioneVO attrLS=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				ListaSuppSezioneVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleSezione, attrLS);
				request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE,attrLSagg );

				if (!this.modificaSezione(eleSezione)) {
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
			if (esaSezione.getPressioneBottone().equals("cancella")) {
				esaSezione.setPressioneBottone("");
				SezioneVO eleSezione=esaSezione.getSezione();
				if (!this.cancellaSezione(eleSezione)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreCancellaSez"));
					this.saveErrors(request, errors);
				}
				else
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.cancellaOK"));
					this.saveErrors(request, errors);
		    		esaSezione.setDisabilitaTutto(true);
				}

			}


			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			esaSezione.setConferma(false);
			esaSezione.setPressioneBottone("");
    		esaSezione.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			esaSezione.setConferma(false);
			esaSezione.setPressioneBottone("");
    		esaSezione.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaSezione.setConferma(false);
			esaSezione.setPressioneBottone("");
    		esaSezione.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			int attualePosizione=esaSezione.getPosizioneScorrimento()+1;
			int dimensione=esaSezione.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						this.loadDatiSezionePassata( esaSezione, esaSezione.getListaDaScorrere().get(attualePosizione), request);
					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							esaSezione.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}

					esaSezione.setPosizioneScorrimento(attualePosizione);
					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
		    		esaSezione.setDisabilitaTutto(false);
					if (esaSezione.isEsaminaInibito())
					{
						esaSezione.setDisabilitaTutto(true);
					}


				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			int attualePosizione=esaSezione.getPosizioneScorrimento()-1;
			int dimensione=esaSezione.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						this.loadDatiSezionePassata( esaSezione, esaSezione.getListaDaScorrere().get(attualePosizione), request);
					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							esaSezione.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					esaSezione.setPosizioneScorrimento(attualePosizione);
		    		esaSezione.setDisabilitaTutto(false);
					if (esaSezione.isEsaminaInibito())
					{
						esaSezione.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSezioneForm esaSezione = (EsaminaSezioneForm) form;
		try {
			ActionMessages errors = new ActionMessages();
			esaSezione.setConferma(true);
			esaSezione.setPressioneBottone("cancella");
    		esaSezione.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			esaSezione.setConferma(false);
			esaSezione.setPressioneBottone("");
    		esaSezione.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	private void loadSezione(EsaminaSezioneForm esaSezione) throws Exception {
		// caricamento sezione

		SezioneVO sez=new SezioneVO("X10", "01", "CONS", "CONSULTAZIONE", +3000.00, "", "2006",+4000.00,"" );
		esaSezione.setSezione(sez);
	}

	private void loadDatiSezionePassata(EsaminaSezioneForm esaSezione, ListaSuppSezioneVO criteriRicercaSezione,  HttpServletRequest request) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SezioneVO> sezioneTrovata = new ArrayList();
		criteriRicercaSezione.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Documento Fisico 09/05/08
/*		if(esaSezione.getSezione()!=null &&  esaSezione.getSezione().getIdSezione()>0)
		{
			// imponendo l'id si salta nella getricercalistasezioni il controllo sulla condizione di chiusura che nel frattempo potrebbe essere avvenuta in tale istanza di modifica e quindi il ripristina potrebbe andare  male
			criteriRicercaSezione.setIdSezione(esaSezione.getSezione().getIdSezione());
		}
*/		sezioneTrovata = factory.getGestioneAcquisizioni().getRicercaListaSezioni(criteriRicercaSezione);
		//gestire l'esistenza del risultato e che sia univoco
		esaSezione.setSezione(sezioneTrovata.get(0));
		try {
		    esaSezione.getSezione().setBudgetSezioneStr(Pulisci.VisualizzaImporto( esaSezione.getSezione().getBudgetSezione()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			esaSezione.getSezione().setBudgetSezioneStr("0,00");
		}

	}
	private boolean modificaSezione(SezioneVO sezione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//valRitorno = factory.getGestioneAcquisizioni().modificaSezione(sezione);
		// prova hibernate
		valRitorno = factory.getGestioneAcquisizioni().modificaSezioneHib(sezione);
		return valRitorno;
	}

	private boolean cancellaSezione(SezioneVO sezione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//valRitorno = factory.getGestioneAcquisizioni().cancellaSezione(sezione);
		// prova hibernate
		valRitorno = factory.getGestioneAcquisizioni().cancellaSezioneHib(sezione);

		return valRitorno;
	}
	private ListaSuppSezioneVO AggiornaTipoVarRisultatiListaSupporto (SezioneVO eleSezione, ListaSuppSezioneVO attributo)
	{
		try {
			if (eleSezione !=null)
			{
			List<SezioneVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getChiave().trim();
					if (eleRis.equals(eleSezione.getChiave().trim()))
					{
						//risultati.get(i).setTipoVariazione(eleCambio.getTipoVariazione());
						risultati.get(i).setTipoVariazione("M");

						break;
					}
			}
			attributo.setSelezioniChiamato(risultati);
			}
		} catch (Exception e) {

		}
		return attributo;
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("GESTIONE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_SEZIONE_ACQUISIZIONI, utente.getCodPolo(), utente.getCodBib(), null);
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
