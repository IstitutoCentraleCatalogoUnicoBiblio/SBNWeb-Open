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
package it.iccu.sbn.web.actions.acquisizioni.fornitori;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DatiFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fornitori.InserisciFornitoreForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

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

public class InserisciFornitoreAction extends LookupDispatchAction {

	//private InserisciFornitoreForm insFornitori;



	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
		map.put("button.crea.profiliAcquisto","profili");
		map.put("button.crea.importaBib","importaBibl");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.controllaEsistenza","controllaEsistenza");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		map.put("button.canNumStandard", "canNumStandard");
		map.put("button.insNumStandard", "insNumStandard");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm insFornitori  = (InserisciFornitoreForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_FORNITORI, 10, "codBib");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		try {
			InserisciFornitoreForm insFornitori  = (InserisciFornitoreForm) form;

			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)ù
			// BUG MANTIS 5306 Collaudo - Inserito controllo sul campo Editore affinche la dicitura  (Produzione editoriale) compaia
			// solo nel caso di provenienza dalla Voce di Menu' corretta

			if ((request.getAttribute("editore") != null && request.getAttribute("editore").equals("SI"))
					|| (request.getSession().getAttribute("editore") != null && request.getSession().getAttribute("editore").equals("SI"))) {
				insFornitori.setEditore("SI");
				Navigation.getInstance(request).setTesto("Crea Editore (Produzione editoriale)");
			} else {
				insFornitori.setEditore("NO");
			}

			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!insFornitori.isSessione()) {
				insFornitori.setSessione(true);
			}

    		if (!insFornitori.isCaricamentoIniziale()) {
    			this.loadFornitore( insFornitori);
				insFornitori.setCaricamentoIniziale(true);
			}

			String ticket=Navigation.getInstance(request).getUserTicket();
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			if (biblio!=null && insFornitori.getDatiFornitore()!=null
					&&  (insFornitori.getDatiFornitore().getCodBibl()==null
							|| (insFornitori.getDatiFornitore().getCodBibl()!=null
									&& insFornitori.getDatiFornitore().getCodBibl().trim().length()==0))) {
				insFornitori.getDatiFornitore().setCodBibl(biblio);
				insFornitori.getFornitore().setCodBibl(biblio);
			}

			BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null && insFornitori.getDatiFornitore()!=null )	{
				insFornitori.getDatiFornitore().setCodBibl(bibScelta.getCod_bib());
				insFornitori.getFornitore().setCodBibl(bibScelta.getCod_bib());
			}
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			insFornitori.getFornitore().setCodPolo(polo);
			if (biblio!=null && insFornitori.getDatiFornitore()!=null
					&&  insFornitori.getDatiFornitore().getCodPolo()!=null
					&& insFornitori.getDatiFornitore().getCodPolo().trim().length()==0)	{
				insFornitori.getDatiFornitore().setCodPolo(polo);
			}

			// preimpostazione della schermata di inserimento con i valori ricercati

			if (request.getSession().getAttribute("ATTRIBUTEListaSuppFornitoreVO")!=null) {
				ListaSuppFornitoreVO ele=(ListaSuppFornitoreVO) request.getSession().getAttribute("ATTRIBUTEListaSuppFornitoreVO");
				request.getSession().removeAttribute("ATTRIBUTEListaSuppFornitoreVO");
				if (ele.getTipoPartner()!=null && ele.getTipoPartner().trim().length()>0 ) {
					insFornitori.getFornitore().setTipoPartner(ele.getTipoPartner());
				}
				if (ele.getNomeFornitore()!=null && ele.getNomeFornitore().trim().length()>0 )	{
					insFornitori.getFornitore().setNomeFornitore(ele.getNomeFornitore());;
				}
				if (ele.getPaese()!=null && ele.getPaese().trim().length()>0 )	{
					insFornitori.getFornitore().setPaese(ele.getPaese());;
				}
				if (ele.getProvincia()!=null && ele.getProvincia().trim().length()>0 )	{
					insFornitori.getFornitore().setProvincia(ele.getProvincia());;
				}
			}

			this.loadTipologieFornitore( insFornitori);
			this.loadPaese( insFornitori);
			this.loadProvincia( insFornitori);
			this.loadValuta( insFornitori, request, insFornitori.getDatiFornitore().getCodBibl());

			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// gestione del campo isbnEditore che contiene i 3 possibile isbn che saranno visualizzati splittati su 3 campi
			if (insFornitori.getFornitore().getIsbnEditore() != null &&  insFornitori.getFornitore().getIsbnEditore().length() > 0) {
				String isbnFornTotale = insFornitori.getFornitore().getIsbnEditore();
				if (isbnFornTotale.contains(",")) {
					insFornitori.setIsbnEditore1(isbnFornTotale.substring(0, isbnFornTotale.indexOf(",")));
					isbnFornTotale = isbnFornTotale.substring(isbnFornTotale.indexOf(",")+1, isbnFornTotale.length());
					if (isbnFornTotale.contains(",")) {
						insFornitori.setIsbnEditore2(isbnFornTotale.substring(0, isbnFornTotale.indexOf(",")));
						insFornitori.setIsbnEditore3(isbnFornTotale.substring(isbnFornTotale.indexOf(",")+1, isbnFornTotale.length()));
					} else {
						insFornitori.setIsbnEditore2(isbnFornTotale.substring(0, isbnFornTotale.length()));
					}
				} else {
					insFornitori.setIsbnEditore1(isbnFornTotale);
				}
			}
			this.loadRegione(insFornitori);
			insFornitori.setRegioneForn(insFornitori.getFornitore().getRegione());

			//controllo se ho un risultato di una lista di supporto PROFILI richiamata da questa pagina (risultato della simulazione)
			ListaSuppProfiloVO ricProf=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			if (ricProf!=null && ricProf.getChiamante()!=null && ricProf.getChiamante().equals(mapping.getPath())) {
				if (ricProf!=null && ricProf.getSelezioniChiamato()!=null && ricProf.getSelezioniChiamato().size()!=0 )	{
					if (ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice()!=null
							&& ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice().length()!=0 )	{
						insFornitori.getDatiFornitore().setProfiliAcq(new ArrayList());
						for (int i=0; i<ricProf.getSelezioniChiamato().size(); i++)	{
							insFornitori.getDatiFornitore().getProfiliAcq().add(ricProf.getSelezioniChiamato().get(i));
						}
					}
				} else {
					insFornitori.getDatiFornitore().setProfiliAcq(new ArrayList());
				}
				request.getSession().removeAttribute("attributeListaSuppProfiloVO");
				request.getSession().removeAttribute("profiliSelected");
				request.getSession().removeAttribute("criteriRicercaProfilo");

 			}

			// controllo che non sia stata attivata la lista supporto delle biblioteche

			BibliotecaVO biblioteca=(BibliotecaVO) request.getAttribute("biblioteca");
			if (biblioteca!=null && biblioteca.getIdBiblioteca()>0)	{
				insFornitori.getFornitore().setTipoPartner("B");
				insFornitori.getFornitore().setCap(biblioteca.getCap());
				insFornitori.getFornitore().setCasellaPostale(biblioteca.getCpostale());
				insFornitori.getFornitore().setIndirizzo(biblioteca.getIndirizzo());
				insFornitori.getFornitore().setCitta(biblioteca.getLocalita());
				insFornitori.getFornitore().setCodiceFiscale(biblioteca.getCod_fiscale());
				insFornitori.getFornitore().setPaese(biblioteca.getPaese());
				insFornitori.getFornitore().setNomeFornitore(biblioteca.getNom_biblioteca());
				insFornitori.getFornitore().setBibliotecaFornitore(biblioteca.getCod_bib());
				insFornitori.getFornitore().setBibliotecaFornitoreCodPolo(biblioteca.getCod_polo());
			}

			// CONTROLLO
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null) {
				insFornitori.setVisibilitaIndietroLS(true);
			}
			// è stato scelto un fornitore dalla lista e si devono caricare tutti i dati
			String fornScelto= (String)request.getAttribute("fornScelto");
			if (fornScelto!=null &&  fornScelto.length()!=0) {
				insFornitori.getFornitore().setCodFornitore(fornScelto);
				this.impostaFornitoreCerca( insFornitori, request,mapping);
			}

			// controllo la configurazione
			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
			if (insFornitori.getDatiFornitore()!=null
					&& insFornitori.getDatiFornitore().getCodBibl()!=null
					&& insFornitori.getDatiFornitore().getCodBibl().length()>0 ) {
				configurazioneCriteri.setCodBibl(insFornitori.getDatiFornitore().getCodBibl());
			}
			configurazioneCriteri.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestProf =true;
			if (configurazioneLetta !=null  && !configurazioneLetta.isGestioneProfilo()) {
				gestProf =false;
			}
			if (!gestProf) {
				insFornitori.setGestProf(false);
			}

			// Intervento Maggio 2013 - il tasto Profili d'acquisto non deve essere presente nel trattamento di
			// creazione x produzione editoriale (è corretto invece nel caso di creazione fornitori)
			if (insFornitori.getEditore().equals("SI")) {
				insFornitori.setGestProf(false);
			}


			return mapping.getInputForward();
	}	catch (ValidationException ve) {
			return mapping.getInputForward();
	}
	// altri tipi di errore
	catch (Exception e) {
		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
			}


	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm  insFornitori = (InserisciFornitoreForm) form;
		try {
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr!=null  && ricArr.getChiamante()!=null)	{
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(ricArr.getChiamante()+".do");
				return action;
			} else {
				return mapping.findForward("indietro");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm  insFornitori = (InserisciFornitoreForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricArr!=null ) {
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null) {
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				} else {
					return mapping.getInputForward();
				}
			} else {
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm  insFornitori = (InserisciFornitoreForm) form;

		// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// gestione del campo isbnEditore che contyiene i 3 possibile isbn che saranno visualizzati splittati su 3 campi
		String isbnEditoreTot="";
		if (insFornitori.getIsbnEditore1() != null && insFornitori.getIsbnEditore1().length() > 0) {
			isbnEditoreTot = insFornitori.getIsbnEditore1();
		}
		if (insFornitori.getIsbnEditore2() != null && insFornitori.getIsbnEditore2().length() > 0) {
			isbnEditoreTot = isbnEditoreTot + "," + insFornitori.getIsbnEditore2();
		}
		if (insFornitori.getIsbnEditore3() != null && insFornitori.getIsbnEditore3().length() > 0) {
			isbnEditoreTot = isbnEditoreTot + "," + insFornitori.getIsbnEditore3();
		}
		insFornitori.getFornitore().setIsbnEditore(isbnEditoreTot);

		try {
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaFornitoreVO(insFornitori.getFornitore());
			// fine validazione

			// Inizio modifica del 07.05.2013 BUG Mantis 5305 Collaudo
			// Dopo la ricerca di un fornitore solo in biblioteca che non trovo creo;
			// con il Salva il sistema non innesca il controllo di duplicazione, ma crea il duplicato
			// Si forza il controllo di esistenza in Polo per verificare che non esistano duplicati
			this.impostaFornitoreCerca( insFornitori, request,mapping);
			if 	(request.getAttribute("attributeListaSuppFornitoreVO") != null) {
				// esistono più fornitori quindi è presenta una lista
				ActionMessages errors = new ActionMessages();
	    		insFornitori.setConferma(true);
	    		insFornitori.setDisabilitaTutto(true);
	    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazioneDuplicazFornitori"));
	    		this.saveErrors(request, errors);
	    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
	    		return mapping.getInputForward();
			}

			if 	(insFornitori.getFornitore().getCodFornitore() != null) {
				// esiste solo un fornitore quindi i dati sono nel VOfornitore
				ActionMessages errors = new ActionMessages();
	    		insFornitori.setConferma(true);
	    		insFornitori.setDisabilitaTutto(true);
	    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazioneDuplicazFornitori"));
	    		this.saveErrors(request, errors);
	    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
	    		return mapping.getInputForward();
			}

			// Fine modifica del 07.05.2013 BUG Mantis 5305 Collaudo

			ActionMessages errors = new ActionMessages();
    		insFornitori.setConferma(true);
    		insFornitori.setDisabilitaTutto(true);
    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();
		}	catch (ValidationException ve) {
			if (ve.getError() == 4001) {
				// BUG mantis collaudo 5323 - MAggio 2013: viene corretta la gestione del "4001: assenza risultati" che, nel caso
				// di inserimento nuovo fornitore significa che non siamo in presenza di simili e si puo procedere con il Salvataggio
				ActionMessages errors = new ActionMessages();
	    		insFornitori.setConferma(true);
	    		insFornitori.setDisabilitaTutto(true);
	    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
	    		this.saveErrors(request, errors);
	    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
	    		return mapping.getInputForward();
			}

			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			insFornitori.setConferma(false);
			insFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			insFornitori.setConferma(false);
			insFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {
			insFornitori.setConferma(false);
    		//insFornitori.setDisabilitaTutto(false);
    		ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
    		if (ricArr!=null ) {
    				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null) {
					// carico i risultati della selezione nella variabile da restituire
					request.getSession().setAttribute("attributeListaSuppFornitoreVO", this.AggiornaRisultatiListaSupportoDaIns( insFornitori, ricArr));
				}
			}

    		insFornitori.getFornitore().setFornitoreBibl(insFornitori.getDatiFornitore());
    		insFornitori.getFornitore().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			GeneraChiave keyDuoble = new GeneraChiave(insFornitori.getFornitore().getNomeFornitore().trim(),"");
			keyDuoble.estraiChiavi("",insFornitori.getFornitore().getNomeFornitore().trim());
			insFornitori.getFornitore().setChiaveFor(keyDuoble.getKy_cles1_A());

			// se il codice ordine è già valorzzato si deve procedere alla modifica
			insFornitori.setDisabilitaTutto(false);
    		if (insFornitori.getFornitore().getCodFornitore()!=null
    				&& insFornitori.getFornitore().getCodFornitore().length()>0) {
				if (!this.modificaFornitore(insFornitori.getFornitore())) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreModifica"));
					this.saveErrors(request, errors);
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.modificaOK"));
					this.saveErrors(request, errors);
					return ripristina( mapping,  form,  request,  response);
				}
			} else {
	    		if (this.inserisciFornitore(insFornitori.getFornitore())) {
	    			ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.inserimentoOK"));
					this.saveErrors(request, errors);
					return ripristina( mapping,  form,  request,  response);
				} else {
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
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}



	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			insFornitori.setConferma(false);
			insFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {

			FornitoreVO eleForn=insFornitori.getFornitore();
			if ( eleForn!=null &&   eleForn.getCodFornitore()!=null
					&& eleForn.getCodFornitore().trim().length()>0)	{
				// il record è già esistente lettura
				FornitoreVO eleFornLetto=this.loadDatiINS(eleForn, insFornitori);

				if (eleFornLetto!=null ) {
					insFornitori.setFornitore(eleFornLetto);
					if (eleFornLetto.getFornitoreBibl()!=null ) {
						insFornitori.setDatiFornitore(eleFornLetto.getFornitoreBibl());
					}
				}
			} else {
				// come era, tale deve restare
				this.loadFornitore( insFornitori);
				if (eleForn!=null )	{
					if (eleForn.getCodBibl()!=null
							&& eleForn.getFornitoreBibl()!=null
							&&  eleForn.getFornitoreBibl().getCodBibl()!=null
							&&  eleForn.getFornitoreBibl().getCodBibl().trim().length()>0) {
						insFornitori.getFornitore().setCodBibl(eleForn.getCodBibl());
					}
					if (eleForn.getCodPolo()!=null &&  eleForn.getCodPolo().trim().length()>0) {
						insFornitori.getFornitore().setCodPolo(eleForn.getCodPolo());
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
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!insFornitori.isSessione()) {
					insFornitori.setSessione(true);
				}
				return mapping.getInputForward();
			}
		resetToken(request);
		return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward profili(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {
			request.setAttribute("fornOggetto", insFornitori.getFornitore());
			request.getSession().removeAttribute("attributeListaSuppProfiloVO");
			this.impostaProfiloCerca( insFornitori,request,mapping);
			request.getSession().setAttribute("chiamante",  mapping.getPath());
			return mapping.findForward("profili");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward importaBibl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {
			return mapping.findForward("importaBibl");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadFornitore(InserisciFornitoreForm insFornitori) throws Exception {
		// salvo alcuni dati preesistenti
		String poloSuForm="";
		String biblSuForm="";
		String valSuForm="";

		if(insFornitori.getDatiFornitore()!=null
				&&  insFornitori.getDatiFornitore().getCodPolo()!=null
				&& insFornitori.getDatiFornitore().getCodPolo().trim().length()>0 ) {
			poloSuForm=insFornitori.getDatiFornitore().getCodPolo();
		}

		if(insFornitori.getDatiFornitore()!=null
				&&  insFornitori.getDatiFornitore().getCodBibl()!=null
				&& insFornitori.getDatiFornitore().getCodBibl().trim().length()>0 ) {
			biblSuForm=insFornitori.getDatiFornitore().getCodBibl();
		}
		if(insFornitori.getDatiFornitore()!=null
				&&  insFornitori.getDatiFornitore().getValuta()!=null
				&& insFornitori.getDatiFornitore().getValuta().trim().length()>0 ) {
			valSuForm=insFornitori.getDatiFornitore().getValuta();
		}

		// caricamento fattura

		FornitoreVO forn=new FornitoreVO(poloSuForm, "", "", "", "", "","","","","","","","","","","","","","",null,"" );
		insFornitori.setFornitore(forn);
		DatiFornitoreVO datiForn=new DatiFornitoreVO(poloSuForm, biblSuForm, "", "", "", "","","",valSuForm);
		insFornitori.setDatiFornitore(datiForn);
	}

	private FornitoreVO loadDatiINS(FornitoreVO ele, InserisciFornitoreForm insFornitori ) throws Exception
	{

		FornitoreVO eleLetto =null;

		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP=insFornitori.getDatiFornitore().getCodPolo();
		String codB=insFornitori.getDatiFornitore().getCodBibl();
		String loc="0";
		String codForn=ele.getCodFornitore();
		String nomeForn="";
		String codProfAcq="";
		String paeseForn=ele.getPaese();
		String tipoPForn=ele.getTipoPartner() ;
		String provForn=ele.getProvincia();
		String chiama=null;
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		eleRicerca.setOrdinamento("");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<FornitoreVO> recordTrovati = new ArrayList();
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaFornitori(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0) {
			eleLetto=recordTrovati.get(0);
		}

		return eleLetto;
	}



	private void loadTipologieFornitore(InserisciFornitoreForm insFornitori) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		insFornitori.setListaTipoForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoPartner()));
	}

	private void loadProvincia(InserisciFornitoreForm insFornitori) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	insFornitori.setListaProvinciaForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceProvince()));

	}

	private void loadRegione(InserisciFornitoreForm insFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		insFornitori.setListaRegioneForn(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_REGIONE)));
	}

	private void loadPaese(InserisciFornitoreForm insFornitori) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	insFornitori.setListaPaeseForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
	}

	private void loadValuta(InserisciFornitoreForm insFornitori, HttpServletRequest request, String biblSel) throws Exception {

    	// esegui query su cambi di biblioteca
    	List<CambioVO> listaCambi=null;
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

    	ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=biblSel;
		String codVal="";
		String desVal="";
		String chiama=null;
		eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
		eleRicerca.setOrdinamento("");
		try {
	    	listaCambi = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(eleRicerca);
			}
		catch (Exception e)
		{
			e.printStackTrace();
		}


    	List lista = new ArrayList();
    	boolean esisteEuro= false;

    	if (listaCambi==null || listaCambi.size()==0) {
    		StrutturaTerna elem = new StrutturaTerna("EUR",  "EURO", "1");
			lista.add(elem);
		} else {
        	for (int w=0;  w < listaCambi.size(); w++) {
        		StrutturaTerna elem = new StrutturaTerna(listaCambi.get(w).getCodValuta(),
        						listaCambi.get(w).getDesValuta().trim(), String.valueOf(listaCambi.get(w).getTassoCambio()));
    			if (listaCambi.get(w).getCodValuta().equals("EUR")) {
    				esisteEuro=true;
    			}
        		lista.add(elem);
    		}
    	}
    	// assegnazione della valuta di default (eur ) nel caso in cui non sia valorizzata
    	if (insFornitori.getDatiFornitore().getValuta()==null || (insFornitori.getDatiFornitore().getValuta()!=null && insFornitori.getDatiFornitore().getValuta().trim().length()==0));
		{
	    	if (esisteEuro)	{
	    		insFornitori.getDatiFornitore().setValuta("EUR");
	    	}
	    	else {
	        	StrutturaTerna primoEle= (StrutturaTerna) lista.get(0); // il primo della lista
	        	insFornitori.getDatiFornitore().setValuta(primoEle.getCodice1());
	    	}
		}

    	insFornitori.setListaValuta(lista);

	}

	/**
	 * SinteticaFornitoriAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppFornitoreVO AggiornaRisultatiListaSupportoDaIns (InserisciFornitoreForm insFornitori, ListaSuppFornitoreVO eleRicArr)
	{
		try {
			List<FornitoreVO> risultati=new ArrayList();
			FornitoreVO eleFornitore=insFornitori.getFornitore();
			risultati.add(eleFornitore);
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}

	public ActionForward controllaEsistenza(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	 throws Exception
	{
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {

			this.impostaFornitoreCerca( insFornitori, request,mapping);
			if 	(request.getAttribute("attributeListaSuppFornitoreVO")!=null) {
				return mapping.findForward("listaForn");
			}

			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca(InserisciFornitoreForm insFornitori, HttpServletRequest request, ActionMapping mapping ) throws Exception
	{
	//impostazione di una lista di supporto
	 try {
		if ((insFornitori.getFornitore().getNomeFornitore()!=null
				&& insFornitori.getFornitore().getNomeFornitore().trim().length()!=0)
				|| (insFornitori.getFornitore().getCodFornitore()!=null
						&& insFornitori.getFornitore().getCodFornitore().trim().length()!=0)) {
			ListaSuppFornitoreVO eleRicercaForn=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String codP="";
			String codB="";
			String codForn="";
			if (insFornitori.getFornitore().getCodFornitore()!=null
					&& insFornitori.getFornitore().getCodFornitore().trim().length()!=0) {
				codForn=insFornitori.getFornitore().getCodFornitore();
			}
			String nomeForn="";
			if (insFornitori.getFornitore().getNomeFornitore()!=null
					&& insFornitori.getFornitore().getNomeFornitore().trim().length()!=0) {
				nomeForn=insFornitori.getFornitore().getNomeFornitore();
			}
			String codProfAcq="";
			String paeseForn="";
			String tipoPForn="";
			String provForn="";
			String chiama="";
			String loc="0";

			eleRicercaForn=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama,loc);

			if (insFornitori.getFornitore().getNomeFornitore()!=null
					&& insFornitori.getFornitore().getNomeFornitore().trim().length()!=0) {
				GeneraChiave keyDuoble = new GeneraChiave( insFornitori.getFornitore().getNomeFornitore().trim(),"");
				keyDuoble.estraiChiavi("",insFornitori.getFornitore().getNomeFornitore().trim());
				eleRicercaForn.setChiaveFor(keyDuoble.getKy_cles1_A());
			}

			eleRicercaForn.setTipoRicerca("parole");


	// dopo aver impostato la lista di supporto
	// eseguo la ricerca dell'anagrafica dei fornitori (cod bibl="") se esiste un elemento importo le informazioni sulla pagina
			List<FornitoreVO> listaFornitori;
			List<FornitoreVO> listaFornitoriBibl;

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			listaFornitori = factory.getGestioneAcquisizioni().getRicercaListaFornitori(eleRicercaForn);
			// Ottobre 2013: gestione dei simili in creazione editore unificata nel caso di esistenza di UNO o PIU simili
			if (insFornitori.getEditore().equals("SI") && listaFornitori.size()>0) {
				request.setAttribute("attributeListaSuppFornitoreVO", eleRicercaForn);
			} else 	if (listaFornitori.size()==1) {
				// inibisco l'input della parte comune del fornitore
				insFornitori.setDisabilitaComune(true);

				// carico i valori nella pagina
				insFornitori.getFornitore().setCodFornitore(listaFornitori.get(0).getCodFornitore());
				insFornitori.getFornitore().setNomeFornitore(listaFornitori.get(0).getNomeFornitore());
				insFornitori.getFornitore().setUnitaOrg(listaFornitori.get(0).getUnitaOrg());
				insFornitori.getFornitore().setIndirizzo(listaFornitori.get(0).getIndirizzo());
				insFornitori.getFornitore().setCasellaPostale(listaFornitori.get(0).getCasellaPostale());
				insFornitori.getFornitore().setCitta(listaFornitori.get(0).getCitta());
				insFornitori.getFornitore().setCap(listaFornitori.get(0).getCap());
				insFornitori.getFornitore().setTelefono(listaFornitori.get(0).getTelefono());
				insFornitori.getFornitore().setFax(listaFornitori.get(0).getFax());
				insFornitori.getFornitore().setCodiceFiscale(listaFornitori.get(0).getCodiceFiscale());
				insFornitori.getFornitore().setPartitaIva(listaFornitori.get(0).getPartitaIva());
				insFornitori.getFornitore().setEmail(listaFornitori.get(0).getEmail());
				insFornitori.getFornitore().setPaese(listaFornitori.get(0).getPaese());
				insFornitori.getFornitore().setTipoPartner(listaFornitori.get(0).getTipoPartner());
				insFornitori.getFornitore().setProvincia(listaFornitori.get(0).getProvincia());
				insFornitori.getFornitore().setNote(listaFornitori.get(0).getNote());
				// solo se è andata a buon esisto la ricerca del fornitore

				if (insFornitori.getFornitore().getCodFornitore()!=null
						&& insFornitori.getFornitore().getCodFornitore().trim().length()!=0) {
					// eseguo la ricerca dell'anagrafica dei fornitori (cod bibl=ARG) se esiste importo le informazioni NEI CONTATTI
					codP=insFornitori.getDatiFornitore().getCodPolo();
					codB=insFornitori.getDatiFornitore().getCodBibl();
					codForn=insFornitori.getFornitore().getCodFornitore();
					nomeForn=insFornitori.getFornitore().getNomeFornitore();
					codProfAcq="";
					paeseForn="";
					tipoPForn="";
					provForn="";
					chiama="";
					loc="1";
					ListaSuppFornitoreVO eleRicercaFornBibl=new ListaSuppFornitoreVO();

					eleRicercaFornBibl=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn,
							tipoPForn, provForn, chiama,loc);
					if (insFornitori.getFornitore().getNomeFornitore()!=null
							&& insFornitori.getFornitore().getNomeFornitore().trim().length()!=0) {
						GeneraChiave keyDuoble = new GeneraChiave( insFornitori.getFornitore().getNomeFornitore().trim(),"");
						keyDuoble.estraiChiavi("",insFornitori.getFornitore().getNomeFornitore().trim());
						eleRicercaFornBibl.setChiaveFor(keyDuoble.getKy_cles1_A());
					}

					eleRicercaFornBibl.setTipoRicerca("parole");
					listaFornitoriBibl = factory.getGestioneAcquisizioni().getRicercaListaFornitori(eleRicercaFornBibl);
					if (listaFornitoriBibl.size()==1) {
						//istanziazione
						DatiFornitoreVO datiForn=new DatiFornitoreVO();
						insFornitori.setDatiFornitore(datiForn);
						// carico i valori di contatto nella pagina
						if (listaFornitoriBibl.get(0).getFornitoreBibl()!=null)	{
							insFornitori.getDatiFornitore().setCodPolo(listaFornitoriBibl.get(0).getFornitoreBibl().getCodPolo());
							insFornitori.getDatiFornitore().setCodBibl(listaFornitoriBibl.get(0).getFornitoreBibl().getCodBibl());
							insFornitori.getDatiFornitore().setTipoPagamento(listaFornitoriBibl.get(0).getFornitoreBibl().getTipoPagamento());
							insFornitori.getDatiFornitore().setCodCliente(listaFornitoriBibl.get(0).getFornitoreBibl().getCodCliente());
							insFornitori.getDatiFornitore().setNomContatto(listaFornitoriBibl.get(0).getFornitoreBibl().getNomContatto());
							insFornitori.getDatiFornitore().setTelContatto(listaFornitoriBibl.get(0).getFornitoreBibl().getTelContatto());
							insFornitori.getDatiFornitore().setFaxContatto(listaFornitoriBibl.get(0).getFornitoreBibl().getFaxContatto());
							insFornitori.getDatiFornitore().setValuta(listaFornitoriBibl.get(0).getFornitoreBibl().getValuta());
						}
					}	else	{
						// esistono tanti record di dati di contatto della stessa biblioteca (errore)
						// se non esistono i dati di contatto biblioteca di  fornitore va in catch
						// è possibile l'inserimento
					}
				}
			} else {
				// ci sono tanti fornitori perchè la ricerca nulla va in catch
				if (listaFornitori.size()>1) {
					//aprire lista di supporto con sintetica dei risultati
					//eleRicercaForn.setChiamante(mapping.getPath());
					// 	CHIAMATA LISTA SUPPORTO
					// non utilizzare sessione
					request.setAttribute("attributeListaSuppFornitoreVO", eleRicercaForn);

				}
			}
		}
// se esistono più elementi allora devo scatenare la sintetica per la scelta tramite parametro di sessione (oppure ne creo un'altro
//oppure gestisco alla grande il valore del chiamante nella sintetica fornitori) salvando il vecchio se esistente
// eseguo la ricerca dell'anagrafica dei fornitori (cod bibl=ARG) se esiste importo le informazioni NEI CONTATTI
// EMETTENDO IL MESSAGGIO DI ERRORE DI INSERIMENTO PER DatI già PRESENTI ED INIBENDO I BOTTONI (ANDARE IN MODIFICA MESSAGGE)


	}catch (Exception e) {
		e.printStackTrace();
   	  throw e;

	}
	}

	private boolean inserisciFornitore(FornitoreVO fornitore) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciFornitore(fornitore);
		return valRitorno;
	}

	private boolean modificaFornitore(FornitoreVO fornitore) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaFornitore(fornitore);
		return valRitorno;
	}
	private ConfigurazioneORDVO loadConfigurazioneORD(ConfigurazioneORDVO configurazioneORD) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
		return configurazioneTrovata;
	}


	public ActionForward profiloCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciFornitoreForm insFornitori = (InserisciFornitoreForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			request.getSession().removeAttribute("attributeListaSuppProfiloVO");
			request.getSession().removeAttribute("profiliSelected");
			request.getSession().removeAttribute("criteriRicercaProfilo");

			this.impostaProfiloCerca( insFornitori,request,mapping);
			return mapping.findForward("profiloCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaProfiloCerca( InserisciFornitoreForm insFornitori, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=Navigation.getInstance(request).getUtente().getCodBib();
		ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
		if (ricArr!=null && ricArr.getCodBibl()!=null && ricArr.getCodBibl().trim().length()>0)	{
			codB= ricArr.getCodBibl();
		}
		if (insFornitori.getDatiFornitore()!=null
				&& insFornitori.getDatiFornitore().getCodBibl()!=null
				&& insFornitori.getDatiFornitore().getCodBibl().trim().length()>0)	{
			codB=insFornitori.getDatiFornitore().getCodBibl();
		}

		StrutturaCombo prof=new StrutturaCombo("","");
		StrutturaCombo sez=new StrutturaCombo("","");
		StrutturaCombo lin=new StrutturaCombo("","");
		StrutturaCombo pae=new StrutturaCombo(insFornitori.getFornitore().getPaese(),"");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,   chiama, ordina );
		StrutturaCombo forn=new StrutturaCombo("","");
		// impostare il fornitore

		eleRicerca.setFornitore(forn);
		if (insFornitori.getDatiFornitore()!=null
				&& insFornitori.getDatiFornitore().getProfiliAcq()!=null
				&& insFornitori.getDatiFornitore().getProfiliAcq().size()>0) {
			eleRicerca.setSelezioniChiamato(insFornitori.getDatiFornitore().getProfiliAcq());
		}

		request.getSession().setAttribute("attributeListaSuppProfiloVO", eleRicerca);

	}catch (Exception e) {	}
	}


	public ActionForward insNumStandard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InserisciFornitoreForm insFornitori  = (InserisciFornitoreForm) form;
		insFornitori.getFornitore().addListaNumStandard(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canNumStandard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InserisciFornitoreForm insFornitori  = (InserisciFornitoreForm) form;

		if (insFornitori.getSelezRadioNumStandard() == null	|| insFornitori.getSelezRadioNumStandard().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblNumStandard"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(insFornitori.getSelezRadioNumStandard());
		insFornitori.getFornitore().getListaNumStandard().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}
}
