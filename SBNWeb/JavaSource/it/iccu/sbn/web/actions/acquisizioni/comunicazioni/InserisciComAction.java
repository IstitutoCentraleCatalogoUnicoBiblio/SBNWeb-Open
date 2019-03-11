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
package it.iccu.sbn.web.actions.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni.InserisciComForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

public class InserisciComAction extends ComunicazioneBaseAction {
	//private InserisciComForm insCom;


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.label.fattura","fatturaCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.ordine","ordineCerca");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_COMUNICAZIONI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				InserisciComForm insCom = (InserisciComForm) form;

				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!insCom.isSessione())
				{
					insCom.setSessione(true);
				}
				if (!insCom.isCaricamentoIniziale())
				{
					this.loadDatiComunicazione( insCom);
					insCom.setCaricamentoIniziale(true);
				}
				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				if (biblio!=null && (insCom.getDatiComunicazione().getCodBibl()==null || (insCom.getDatiComunicazione().getCodBibl()!=null && insCom.getDatiComunicazione().getCodBibl().trim().length()==0)))
				{
					insCom.getDatiComunicazione().setCodBibl(biblio);
				}
				insCom.getDatiComunicazione().setCodPolo(polo);
				BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null)
				{
					insCom.getDatiComunicazione().setCodBibl(bibScelta.getCod_bib());
				}

				// preimpostazione della schermata di inserimento con i valori ricercati

				if (request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_COMUNICAZIONE)!=null)
				{
					ListaSuppComunicazioneVO ele=(ListaSuppComunicazioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_COMUNICAZIONE);
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_COMUNICAZIONE);
					if (ele.getTipoMessaggio()!=null && ele.getTipoMessaggio().trim().length()>0)
					{
						insCom.getDatiComunicazione().setTipoMessaggio(ele.getTipoMessaggio());
					}
					if (ele.getDirezioneComunicazione()!=null  && ele.getDirezioneComunicazione().trim().length()>0 )
					{
						insCom.getDatiComunicazione().setDirezioneComunicazione(ele.getDirezioneComunicazione());;
					}
					if (ele.getFornitore()!=null )
					{
						insCom.getDatiComunicazione().setFornitore(ele.getFornitore());;
					}
					if (ele.getIdDocumento()!=null )
					{
						insCom.getDatiComunicazione().setIdDocumento(ele.getIdDocumento());;
					}

					//almaviva5_20101123 periodici
					if (ValidazioneDati.isFilled(ele.getTipoDocumento()))
						insCom.getDatiComunicazione().setTipoDocumento(ele.getTipoDocumento());
					if (ValidazioneDati.isFilled(ele.getNote()))
						insCom.getDatiComunicazione().setNoteComunicazione(ele.getNote());
					if (ValidazioneDati.isFilled(ele.getFascicoli()))
						insCom.getDatiComunicazione().setFascicoli(ele.getFascicoli());
				}


				//this.loadDatiComunicazione();
				this.loadStatoComunicazione( insCom);
				this.loadDirezioneComunicazione( insCom);
				this.loadTipoDocumento( insCom);
				// elenchi differenziati per direzione e tipo doc/ogg
				this.loadTipoMessaggio( insCom);

				if (insCom.getDatiComunicazione().getDirezioneComunicazione()!=null && insCom.getDatiComunicazione().getDirezioneComunicazione().equals(""))
				{
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals("F"))
					{
						this.loadTipoMessaggioFatt( insCom);
					}
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals("O"))
					{
						this.loadTipoMessaggioOrd( insCom);
					}
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals(""))
					{
						this.loadTipoMessaggio( insCom);
					}
				}
				if (insCom.getDatiComunicazione().getDirezioneComunicazione()!=null && insCom.getDatiComunicazione().getDirezioneComunicazione().equals("A"))
				{
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals("F"))
					{
						this.loadTipoMessaggioPerFornFatt( insCom);
					}
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals("O"))
					{
						this.loadTipoMessaggioPerFornOrd( insCom);
					}
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals(""))
					{
						this.loadTipoMessaggioPerForn( insCom);
					}
				}
				if (insCom.getDatiComunicazione().getDirezioneComunicazione()!=null && insCom.getDatiComunicazione().getDirezioneComunicazione().equals("D"))
				{
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals("F"))
					{
						this.loadTipoMessaggioDaFornFatt( insCom);
					}
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals("O"))
					{
						this.loadTipoMessaggioDaFornOrd( insCom);
					}
					if (insCom.getDatiComunicazione().getTipoDocumento()!=null && insCom.getDatiComunicazione().getTipoDocumento().equals(""))
					{
						this.loadTipoMessaggioDaForn( insCom);
					}
				}
				// fine elenchi differenziati
				this.loadTipoInvio( insCom);
				this.loadTipoOrdine( insCom);

				ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					insCom.setVisibilitaIndietroLS(true);
				}
				//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
				ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
					{
						if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
						{
							insCom.getDatiComunicazione().getFornitore().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
							insCom.getDatiComunicazione().getFornitore().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						insCom.getDatiComunicazione().getFornitore().setCodice("");
						insCom.getDatiComunicazione().getFornitore().setDescrizione("");
					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
					request.getSession().removeAttribute("fornitoriSelected");
					request.getSession().removeAttribute("criteriRicercaFornitore");

	 			}
				//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
				ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
					{
						if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
						{
							insCom.getDatiComunicazione().getIdDocumento().setCodice1(ricOrd.getSelezioniChiamato().get(0).getTipoOrdine());
							insCom.getDatiComunicazione().getIdDocumento().setCodice2(ricOrd.getSelezioniChiamato().get(0).getAnnoOrdine());
							insCom.getDatiComunicazione().getIdDocumento().setCodice3(ricOrd.getSelezioniChiamato().get(0).getCodOrdine());

							StrutturaCombo fornit=new StrutturaCombo("","");
							if (ricOrd.getSelezioniChiamato().get(0).getFornitore()!=null)
							{
								fornit=ricOrd.getSelezioniChiamato().get(0).getFornitore();	// il primo fornitore
							}
							if (insCom.getDatiComunicazione().getFornitore()==null || (insCom.getDatiComunicazione().getFornitore()!=null && insCom.getDatiComunicazione().getFornitore().getCodice()!=null && insCom.getDatiComunicazione().getFornitore().getCodice().trim().length()==0 ))
							{
								insCom.getDatiComunicazione().setFornitore(fornit);
							}

						}
					}

					else
					{
						// pulizia della maschera di ricerca
						insCom.getDatiComunicazione().getIdDocumento().setCodice1("");
						insCom.getDatiComunicazione().getIdDocumento().setCodice2("");
						insCom.getDatiComunicazione().getIdDocumento().setCodice3("");

					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
					request.getSession().removeAttribute("ordiniSelected");
					request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

	 			}
				//controllo se ho un risultato di una lista di supporto FATTURA richiamata da questa pagina (risultato della simulazione)
				ListaSuppFatturaVO ricFatt=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
				if (ricFatt!=null && ricFatt.getChiamante()!=null && ricFatt.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricFatt!=null && ricFatt.getSelezioniChiamato()!=null && ricFatt.getSelezioniChiamato().size()!=0 )
					{
						if (ricFatt.getSelezioniChiamato().get(0).getNumFattura()!=null && ricFatt.getSelezioniChiamato().get(0).getNumFattura().length()!=0 )
						{
							insCom.getDatiComunicazione().getIdDocumento().setCodice1("");
							insCom.getDatiComunicazione().getIdDocumento().setCodice2(ricFatt.getSelezioniChiamato().get(0).getAnnoFattura());
							insCom.getDatiComunicazione().getIdDocumento().setCodice3(ricFatt.getSelezioniChiamato().get(0).getProgrFattura());
							StrutturaCombo fornit=new StrutturaCombo("","");
							if (ricFatt.getSelezioniChiamato().get(0).getFornitoreFattura()!=null)
							{
								fornit=ricFatt.getSelezioniChiamato().get(0).getFornitoreFattura();	// il primo fornitore
							}
							if (insCom.getDatiComunicazione().getFornitore()==null || (insCom.getDatiComunicazione().getFornitore()!=null && insCom.getDatiComunicazione().getFornitore().getCodice()!=null && insCom.getDatiComunicazione().getFornitore().getCodice().trim().length()==0 ))
							{
								insCom.getDatiComunicazione().setFornitore(fornit);
							}

						}
					}
					else
					{
						// pulizia della maschera di ricerca
						insCom.getDatiComunicazione().getIdDocumento().setCodice1("");
						insCom.getDatiComunicazione().getIdDocumento().setCodice2("");
						insCom.getDatiComunicazione().getIdDocumento().setCodice3("");
					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppFatturaVO");
					request.getSession().removeAttribute("fattureSelected");
					request.getSession().removeAttribute("criteriRicercaFattura");

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
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(ricArr.getChiamante()+".do");
				return action;
			}
			else
			{
				//almaviva5_201011223 periodici
				Navigation navi = Navigation.getInstance(request);
				if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_FASCICOLO)) {
					request.setAttribute(PeriodiciDelegate.SIF_COMUNICAZIONI, insCom.getDatiComunicazione());
					return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_FASCICOLO, false);
				}

				if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_KARDEX)) {
					request.setAttribute(PeriodiciDelegate.SIF_COMUNICAZIONI, insCom.getDatiComunicazione());
					return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_KARDEX, false);
				}

				return mapping.findForward("indietro");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta

			ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
			if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					// carico i risultati della selezione nella variabile da restituire
					request.getSession().setAttribute("attributeListaSuppComunicazioneVO", this.AggiornaRisultatiListaSupportoDaIns( insCom,ricArr));
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
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			if(!insCom.isSessione()) {
				insCom.setSessione(true);
			}
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaComunicazioneVO(insCom.getDatiComunicazione());
			// fine validazione

			ActionMessages errors = new ActionMessages();
    		insCom.setConferma(true);
    		insCom.setDisabilitaTutto(true);
    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			insCom.setConferma(false);
			insCom.setPressioneBottone("");
			insCom.setDisabilitaTutto(false);
			return mapping.getInputForward();

		} catch (Exception e) {
			insCom.setConferma(false);
			insCom.setPressioneBottone("");
			insCom.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		InserisciComForm currentForm = (InserisciComForm) form;
		try {
			currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);
			ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");

    		if (ricArr!=null )
    			{
    				// gestione del chiamante
    				if (ricArr!=null && ricArr.getChiamante()!=null)
    				{
    					// carico i risultati della selezione nella variabile da restituire
    					request.getSession().setAttribute("attributeListaSuppComunicazioneVO", this.AggiornaRisultatiListaSupportoDaIns( currentForm,ricArr));
    				}
    			}
    		currentForm.getDatiComunicazione().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
			// imposta a ricevuto se la direzione è "da fornitore"
			if (currentForm.getDatiComunicazione().getDirezioneComunicazione().equals("D"))
			{
				currentForm.getDatiComunicazione().setStatoComunicazione("1");
			}

    		// se il codice ordine è già valorzzato si deve procedere alla modifica
			if (currentForm.getDatiComunicazione().getCodiceMessaggio()!=null  && currentForm.getDatiComunicazione().getCodiceMessaggio().length()>0)
			{
				if (!this.modificaComunicazione(currentForm.getDatiComunicazione())) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreModifica"));
					this.saveErrors(request, errors);
					//return mapping.getInputForward();
				}
				else
				{
/*					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.modificaOK"));
					this.saveErrors(request, errors);
*/
					// esame del tipo di  messaggio se notifica annullamento ordine (03) oppure  notifica chiusura ordine (04)
					//si deve procedere all'annullamento e/o chiusura ordine
					boolean buonfine=true;
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("O") && currentForm.getDatiComunicazione().getTipoMessaggio()!=null &&  (currentForm.getDatiComunicazione().getTipoMessaggio().equals("03") || currentForm.getDatiComunicazione().getTipoMessaggio().equals("04"))) // annullamento o chiusura
					{
						// lettura ordine
						OrdiniVO eleOrd=null;
						eleOrd=this.loadDatiOrdinePassato( currentForm.getDatiComunicazione(), request);
						// controllo lo stato che deve essere aperto
						if (eleOrd!=null && eleOrd.getStatoOrdine()!=null &&  eleOrd.getStatoOrdine().equals("A"))
						{
							if (eleOrd!=null &&  currentForm.getDatiComunicazione().getTipoMessaggio().equals("04") ) // chiusura
							{
								eleOrd.setStatoOrdine("C");
							}
							if (eleOrd!=null && currentForm.getDatiComunicazione().getTipoMessaggio().equals("03")) // annullamento
							{
								eleOrd.setStatoOrdine("N");
							}
							// controllo esistenza inventari
							if (eleOrd!=null && eleOrd.getStatoOrdine().length()>0 && (eleOrd.getStatoOrdine().equals("C") || eleOrd.getStatoOrdine().equals("N") ) )
							{
								List listaInv=null;
								try
								{

									  String codPolo=eleOrd.getCodPolo();
							          String codBib=eleOrd.getCodBibl();
							          String codBibO=eleOrd.getCodBibl();
							          String codTipOrd=eleOrd.getTipoOrdine();
							          String codBibF="";
							          //String ticket=ordine.getTicket();
							          String ticket=eleOrd.getTicket();
							          int   annoOrd=Integer.valueOf(eleOrd.getAnnoOrdine().trim());
							          int   codOrd=Integer.valueOf(eleOrd.getCodOrdine());
							          int   nRec=999;

							          FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
							          listaInv = factory.getGestioneDocumentoFisico().getListaInventariOrdine( codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, this.getLocale(request, Constants.SBN_LOCALE), ticket,  nRec).getLista();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									// l'errore capita in questo punto
									e.printStackTrace();
								}
								//CASO: ORDINE DA CHIUDERE SOLO SE ESISTONO INVENTARI
								if (!eleOrd.getTipoOrdine().equals("R") && eleOrd.getStatoOrdine().equals("C") && (listaInv==null || (listaInv!=null && listaInv.size()==0)) )
						          {
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage(
											"errors.acquisizioni.noChiusuraInventari"));
									this.saveErrors(request, errors);
									//return mapping.getInputForward();
									buonfine=false;
						          }

								if (!eleOrd.getTipoOrdine().equals("R") &&  eleOrd.getStatoOrdine().equals("N") && (listaInv!=null && listaInv.size()>0))
						          {
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage(
											"errors.acquisizioni.noAnnullaInventari"));
									this.saveErrors(request, errors);
									//return mapping.getInputForward();
									buonfine=false;
						          }

								if (buonfine)
									{
										if (!modificaOrdine(eleOrd))
										{
											buonfine=false;
											eleOrd=null;
										}
									}
							}	// fine controllo esistenza inventari

						}
						else
						{
							eleOrd=null;
							buonfine=false;
						}

						if (eleOrd==null)
						{
							buonfine=false;
						}

					}
					if (buonfine) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));
						return forwardMappaGestione(mapping, request, currentForm);
						//return ripristina( mapping,  form,  request,  response);

					}
					else
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.modificaParzialeComOK"));
						this.saveErrors(request, errors);
						return ripristina( mapping,  form,  request,  response);

					}
				}
			}
			else
			{
	    		if (this.inserisciComunicazione(currentForm.getDatiComunicazione()))
				{
/*					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.inserimentoOK"));
					this.saveErrors(request, errors);
*/
					// esame del tipo di  messaggio se notifica annullamento ordine (03) oppure  notifica chiusura ordine (04)
					//si deve procedere all'annullamento e/o chiusura ordine
					boolean buonfine=true;
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("O") && currentForm.getDatiComunicazione().getTipoMessaggio()!=null &&  (currentForm.getDatiComunicazione().getTipoMessaggio().equals("03") || currentForm.getDatiComunicazione().getTipoMessaggio().equals("04"))) // annullamento o chiusura
					{
						// lettura ordine
						OrdiniVO eleOrd=null;
						eleOrd=this.loadDatiOrdinePassato( currentForm.getDatiComunicazione(), request);
						// controllo lo stato che deve essere aperto
						if (eleOrd!=null && eleOrd.getStatoOrdine()!=null &&  eleOrd.getStatoOrdine().equals("A"))
						{
							if (eleOrd!=null &&  currentForm.getDatiComunicazione().getTipoMessaggio().equals("04") ) // chiusura
							{
								eleOrd.setStatoOrdine("C");
							}
							if (eleOrd!=null && currentForm.getDatiComunicazione().getTipoMessaggio().equals("03")) // annullamento
							{
								eleOrd.setStatoOrdine("N");
							}
							// controllo esistenza inventari
							if (eleOrd!=null && eleOrd.getStatoOrdine().length()>0 && (eleOrd.getStatoOrdine().equals("C") || eleOrd.getStatoOrdine().equals("N") ) )
							{
								List listaInv=null;
								try
								{

									  String codPolo=eleOrd.getCodPolo();
							          String codBib=eleOrd.getCodBibl();
							          String codBibO=eleOrd.getCodBibl();
							          String codTipOrd=eleOrd.getTipoOrdine();
							          String codBibF="";
							          //String ticket=ordine.getTicket();
							          String ticket=eleOrd.getTicket();
							          int   annoOrd=Integer.valueOf(eleOrd.getAnnoOrdine().trim());
							          int   codOrd=Integer.valueOf(eleOrd.getCodOrdine());
							          int   nRec=999;

							          FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
							          listaInv = factory.getGestioneDocumentoFisico().getListaInventariOrdine( codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, this.getLocale(request, Constants.SBN_LOCALE), ticket,  nRec).getLista();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									// l'errore capita in questo punto
									e.printStackTrace();
								}
								//CASO: ORDINE DA CHIUDERE SOLO SE ESISTONO INVENTARI
								if (!eleOrd.getTipoOrdine().equals("R") && eleOrd.getStatoOrdine().equals("C") && (listaInv==null || (listaInv!=null && listaInv.size()==0)) )
						          {
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage(
											"errors.acquisizioni.noChiusuraInventari"));
									this.saveErrors(request, errors);
									//return mapping.getInputForward();
									buonfine=false;
						          }

								if (!eleOrd.getTipoOrdine().equals("R") &&  eleOrd.getStatoOrdine().equals("N") && (listaInv!=null && listaInv.size()>0))
						          {
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage(
											"errors.acquisizioni.noAnnullaInventari"));
									this.saveErrors(request, errors);
									//return mapping.getInputForward();
									buonfine=false;
						          }

								if (buonfine)
									{
										if (!modificaOrdine(eleOrd))
										{
											buonfine=false;
											eleOrd=null;
										}
									}
							}	// fine controllo esistenza inventari

						}
						else
						{
							eleOrd=null;
							buonfine=false;
						}

						if (eleOrd==null)
						{
							buonfine=false;
						}

					}
					if (buonfine) {
						//almaviva5_20110406 #4340
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));
						return forwardMappaGestione(mapping, request, currentForm);

						//return ripristina( mapping,  form,  request,  response);

					}
					else
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.inserimentoParzialeComOK"));
						this.saveErrors(request, errors);
						return ripristina( mapping,  form,  request,  response);

					}


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

	private ActionForward forwardMappaGestione(ActionMapping mapping, HttpServletRequest request, ActionForm form) {
		//almaviva5_20110406 #4340
		InserisciComForm currentForm = (InserisciComForm) form;
		ListaSuppComunicazioneVO ricerca = new ListaSuppComunicazioneVO();
		ClonePool.copyCommonProperties(ricerca, currentForm.getDatiComunicazione());
		request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_COMUNICAZIONE, ValidazioneDati.asSingletonList(ricerca));
		Navigation navi = Navigation.getInstance(request);
		navi.purgeThis();

		NavigationForward forward = navi.goForward(mapping.findForward("esamina"));
		forward.setRedirect(true);	//pulisco request per evitare errore bean populate
		return forward;
	}



	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			insCom.setConferma(false);
			insCom.setDisabilitaTutto(false);
			return mapping.getInputForward();
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {

			ComunicazioneVO eleCom=insCom.getDatiComunicazione();
			if ( eleCom!=null &&  eleCom.getCodiceMessaggio()!=null && eleCom.getCodiceMessaggio().trim().length()>0)
			{
				// il record è già esistente
				// lettura
				ComunicazioneVO eleComLetto=this.loadDatiINS(eleCom);

				if (eleComLetto!=null )
				{
					insCom.setDatiComunicazione(eleComLetto);
				}

			}
			else
			{
				this.loadDatiComunicazione( insCom);
				if (eleCom!=null )
				{
					if (eleCom.getCodBibl()!=null &&  eleCom.getCodBibl().trim().length()>0)
					{
						insCom.getDatiComunicazione().setCodBibl(eleCom.getCodBibl());
					}
					if (eleCom.getCodPolo()!=null &&  eleCom.getCodPolo().trim().length()>0)
					{
						insCom.getDatiComunicazione().setCodPolo(eleCom.getCodPolo());
					}
				}

			}




			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!insCom.isSessione())
				{
					insCom.setSessione(true);
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
	private void loadDatiComunicazione(InserisciComForm insCom) throws Exception {
		// caricamento
		Calendar c=new GregorianCalendar();
	 	int anno=c.get(Calendar.YEAR);
	 	String annoAttuale="";
	 	annoAttuale=Integer.valueOf(anno).toString();
		// ASSEGNAZIONE DELLA data di sistema
		Date dataodierna=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdiernaStr =formato.format(dataodierna);

	 	ComunicazioneVO com=new ComunicazioneVO("", "", "", "","", new StrutturaCombo("",""),new StrutturaTerna("","",""), "3", dataOdiernaStr, "", "" , "");
		insCom.setDatiComunicazione(com);

	}

	private ComunicazioneVO loadDatiINS(ComunicazioneVO ele ) throws Exception
	{

		ComunicazioneVO eleLetto =null;

		ListaSuppComunicazioneVO eleRicerca=new ListaSuppComunicazioneVO();

		// carica i criteri di ricerca da passare alla esamina
		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		String codMsg=ele.getCodiceMessaggio();
		String tipoDoc="";
		String tipoMsg="";
		StrutturaCombo forn=new StrutturaCombo("","");
		StrutturaTerna idDoc=new StrutturaTerna("","","");
		String statoCom="";
		String dataComDa="";
		String dataComA="";
		String dirCom="";
		String tipoInvioCom="";
		String chiama=null;
		String ordina="";

		eleRicerca=new ListaSuppComunicazioneVO(codP, codB, codMsg,  tipoDoc, tipoMsg,  forn, idDoc, statoCom, dataComDa, dataComA, dirCom, tipoInvioCom, chiama, ordina );

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<ComunicazioneVO> recordTrovati = new ArrayList();
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaComunicazioni(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0)
		{
			eleLetto=recordTrovati.get(0);

		}

		return eleLetto;
	}




	private boolean inserisciComunicazione(ComunicazioneVO comunicazione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciComunicazione(comunicazione);
		return valRitorno;
	}

	private boolean modificaComunicazione(ComunicazioneVO comunicazione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaComunicazione(comunicazione);
		return valRitorno;
	}


	/**
	 * InserisciComAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */

	private ListaSuppComunicazioneVO AggiornaRisultatiListaSupportoDaIns (InserisciComForm insCom, ListaSuppComunicazioneVO eleRicArr)
	{
		try {
			List<ComunicazioneVO> risultati=new ArrayList();
			ComunicazioneVO eleComunicazione=insCom.getDatiComunicazione();
			risultati.add(eleComunicazione);
			eleRicArr.setSelezioniChiamato(risultati);
		} catch (Exception e) {

		}
		return eleRicArr;
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, insCom);
			if (forward != null){
				return forward;
			}
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

/*			if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}*/

			this.impostaFornitoreCerca( insCom,request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( InserisciComForm insCom, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=insCom.getDatiComunicazione().getCodBibl();
		String codForn=insCom.getDatiComunicazione().getFornitore().getCodice();
		String nomeForn=insCom.getDatiComunicazione().getFornitore().getDescrizione();
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0"; // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward ordineCerca( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			request.getSession().removeAttribute("ordiniSelected");
			request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

/*			if (request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null )
			{
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaOrdineCerca( insCom,request,mapping);
			return mapping.findForward("ordineCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaOrdineCerca(InserisciComForm insCom, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=insCom.getDatiComunicazione().getCodBibl();
		String codBAff = null;
		String codOrd=insCom.getDatiComunicazione().getIdDocumento().getCodice3();
		String annoOrd=insCom.getDatiComunicazione().getIdDocumento().getCodice2();
		String tipoOrd=insCom.getDatiComunicazione().getIdDocumento().getCodice1(); // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		if (insCom.getDatiComunicazione()!=null && insCom.getDatiComunicazione().getFornitore()!=null && insCom.getDatiComunicazione().getFornitore().getCodice()!=null && insCom.getDatiComunicazione().getFornitore().getCodice().trim().length()>0 )
		{
			forn.setCodice(insCom.getDatiComunicazione().getFornitore().getCodice());
		}
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo ("","");
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=null;
		String chiama=mapping.getPath();
		String[] statoOrdArr=new String[0];
		Boolean stamp=false;
		Boolean rinn=false;

		eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
		String ticket=Navigation.getInstance(request).getUserTicket();
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);

		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward fatturaCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciComForm insCom = (InserisciComForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFatturaVO")==null
			request.getSession().removeAttribute("attributeListaSuppFatturaVO");
			request.getSession().removeAttribute("fattureSelected");
			request.getSession().removeAttribute("criteriRicercaFattura");

/*			if (request.getSession().getAttribute("criteriRicercaFattura")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFatturaVO");
				request.getSession().removeAttribute("fattureSelected");
				request.getSession().removeAttribute("criteriRicercaFattura");
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaFatturaCerca( insCom, request,mapping);
			return mapping.findForward("fatturaCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFatturaCerca(InserisciComForm insCom, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=insCom.getDatiComunicazione().getCodBibl();
		String annoF=insCom.getDatiComunicazione().getIdDocumento().getCodice2();
		String numF="";
		String staF="";
		String dataDa="";
		String dataA="";
		String prgF=insCom.getDatiComunicazione().getIdDocumento().getCodice3();
		String dataF="";
		String dataRegF="";
		String tipF="";
		StrutturaTerna ordFatt=new StrutturaTerna("","","");
		StrutturaCombo fornFatt=new StrutturaCombo("","");
		if (insCom.getDatiComunicazione()!=null && insCom.getDatiComunicazione().getFornitore()!=null && insCom.getDatiComunicazione().getFornitore().getCodice()!=null && insCom.getDatiComunicazione().getFornitore().getCodice().trim().length()>0 )
		{
			fornFatt.setCodice(insCom.getDatiComunicazione().getFornitore().getCodice());
		}
		StrutturaTerna bilFatt=new StrutturaTerna("","","");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFatturaVO", eleRicerca);
		//String ticket=Navigation.getInstance(request).getUserTicket();
		//eleRicerca.setTicket(ticket);

	}catch (Exception e) {	}
	}



	private void loadDirezioneComunicazione(InserisciComForm insCom) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("A","Per fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("D","Da Fornitore");
		lista.add(elem);

		insCom.setListaDirezioneComunicazione(lista);
	}

/*	private void loadTipoOrdine() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","Acquisto");
		lista.add(elem);
		elem = new StrutturaCombo("L","Deposito Legale");
		lista.add(elem);
		elem = new StrutturaCombo("D","Dono");
		lista.add(elem);
		elem = new StrutturaCombo("C","Scambio");
		lista.add(elem);
		elem = new StrutturaCombo("V","Visione Trattenuta");
		lista.add(elem);

		insCom.setListaTipoOrdine(lista);
	}*/

	private void loadTipoOrdine(InserisciComForm insCom) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		insCom.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));
	}

	private void loadTipoDocumento(InserisciComForm insCom) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - Fattura");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - Ordine");
		lista.add(elem);
		insCom.setListaTipoDocumento(lista);
	}


	private void loadStatoComunicazione(InserisciComForm insCom) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1	- RICEVUTO");
		lista.add(elem);
		elem = new StrutturaCombo("2","2	- SPEDITO");
		lista.add(elem);
		elem = new StrutturaCombo("3","3	- NON SPEDITO");
		lista.add(elem);
		insCom.setListaStatoComunicazione(lista);
	}

	private void loadTipoMessaggio(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioOrd(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioFatt(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioDaFornOrd(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioDaFornFatt(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioPerFornOrd(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioPerFornFatt(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);

		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	private void loadTipoMessaggioPerForn(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}
	private void loadTipoMessaggioDaForn(InserisciComForm insCom) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		insCom.setListaTipoMessaggio(lista);
		insCom.setListaTipoMessaggio(this.ElencaPer(lista, insCom,"tipo"));

	}

	public List<StrutturaCombo> ElencaPer(List<StrutturaCombo> lst,InserisciComForm insCom,String sortBy ) throws EJBException {
		//List<OrdiniVO> lst = sintordini.getListaOrdini();
		Comparator comp=null;
		if (sortBy==null)
		{
			comp =new TipoMsgAscending();
		}
		else if (sortBy.equals("tipo")) {
			comp =new TipoMsgAscending();
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

	private static class TipoMsgAscending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((StrutturaCombo) o1).getDescrizione();
				String e2 = ((StrutturaCombo) o2).getDescrizione();
				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private static class TipoMsgDescending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((StrutturaCombo) o1).getDescrizione();
				String e2 = ((StrutturaCombo) o2).getDescrizione();
				return - e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}



	private void loadTipoInvio(InserisciComForm insCom) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	insCom.setListaTipoInvio(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoInvio()));

/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - fax");
		lista.add(elem);
		elem = new StrutturaCombo("P","P - posta");
		lista.add(elem);
		elem = new StrutturaCombo("S","S - stampa");
		lista.add(elem);
		insCom.setListaTipoInvio(lista);
*/	}

	private boolean modificaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaOrdine(ordine);
		return valRitorno;
	}

	protected Locale getLocale(HttpServletRequest request, String locale) {
		return RequestUtils.getUserLocale(request, locale);
	}


	private OrdiniVO loadDatiOrdinePassato(ComunicazioneVO eleComunicazione, HttpServletRequest request) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		// prepara lista supporto

		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		String ticket=Navigation.getInstance(request).getUserTicket();
		OrdiniVO eleElenco=new OrdiniVO();

		String codP=eleComunicazione.getCodPolo();
		String codB=eleComunicazione.getCodBibl();
		String codBAff=null;
		String codOrd=eleComunicazione.getIdDocumento().getCodice3();
		String annoOrd=eleComunicazione.getIdDocumento().getCodice2();
		String tipoOrd=eleComunicazione.getIdDocumento().getCodice1();
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
		String[] statoOrdArr=null; // solo per scorrimenti di  dettaglio
		Boolean rinn=false;
		Boolean stamp=false;
		eleRicerca=new ListaSuppOrdiniVO( codP, codB,  codBAff,   codOrd,  annoOrd,  tipoOrd,  dataOrdDa, dataOrdA,   cont,  statoOrd,  forn,   tipoInvioOrd,  bil,    sezioneAcqOrd,  tit,   dataFineAbbOrdDa,  dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
		eleRicerca.setTicket(ticket);
		eleRicerca.setOrdinamento("");

		List<OrdiniVO> ordiniTrovato = factory.getGestioneAcquisizioni().getRicercaListaOrdini(eleRicerca);
		//gestire l'esistenza del risultato e che sia univoco
		if (ordiniTrovato!=null && ordiniTrovato.size()==1)
		{
			eleElenco=ordiniTrovato.get(0);
			eleElenco.setUtente(eleComunicazione.getUtente());
		}
		return eleElenco;
	}

}
