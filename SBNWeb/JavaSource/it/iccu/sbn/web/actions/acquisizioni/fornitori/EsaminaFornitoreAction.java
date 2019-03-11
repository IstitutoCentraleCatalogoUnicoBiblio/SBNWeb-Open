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
import it.iccu.sbn.ejb.remote.Utente;
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
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fornitori.EsaminaFornitoreForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
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

public class EsaminaFornitoreAction extends LookupDispatchAction implements SbnAttivitaChecker{

	//private EsaminaFornitoreForm esaFornitori;




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
		map.put("button.canNumStandard", "canNumStandard");
		map.put("button.insNumStandard", "insNumStandard");

        return map;
	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		try {
			EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;

			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
			if (request.getAttribute("editore") != null && request.getAttribute("editore").equals("SI")) {
				esaFornitori.setEditore((String)request.getAttribute("editore"));
				Navigation.getInstance(request).setTesto("Esamina Editore (Produzione editoriale)");
			} else {
				esaFornitori.setEditore("NO");
			}

			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if (!esaFornitori.isSessione())	{
				esaFornitori.setSessione(true);
			}

			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null  && ricArr.getChiamante()!=null)	{
				esaFornitori.setEsaminaInibito(true);
				esaFornitori.setDisabilitaTutto(true);
			}


			esaFornitori.setListaDaScorrere((List<ListaSuppFornitoreVO>) request.getSession().getAttribute("criteriRicercaFornitore"));
			if(esaFornitori.getListaDaScorrere() != null && esaFornitori.getListaDaScorrere().size()!=0) {
				if(esaFornitori.getListaDaScorrere().size()>1 )	{
					esaFornitori.setEnableScorrimento(true);
				} else	{
					esaFornitori.setEnableScorrimento(false);
				}
			}

			if (!esaFornitori.isSubmitDinamico()) {
				if (String.valueOf(esaFornitori.getPosizioneScorrimento()).length()==0 ) {
					esaFornitori.setPosizioneScorrimento(0);
				}
				this.loadDatiFornitorePassato( esaFornitori,esaFornitori.getListaDaScorrere().get(esaFornitori.getPosizioneScorrimento()));
			}

			this.loadTipologieFornitore( esaFornitori);
			this.loadPaese( esaFornitori);
			this.loadProvincia( esaFornitori);
			this.loadValuta( esaFornitori,  request, esaFornitori.getDatiFornitore().getCodBibl());
			esaFornitori.setValuta(esaFornitori.getDatiFornitore().getValuta());
			esaFornitori.setTipoForn(esaFornitori.getFornitore().getTipoPartner());
			esaFornitori.setPaeseForn(esaFornitori.getFornitore().getPaese());
			esaFornitori.setProvinciaForn(esaFornitori.getFornitore().getProvincia());

			this.loadRegione(esaFornitori);
			esaFornitori.setRegioneForn(esaFornitori.getFornitore().getRegione());

			//controllo se ho un risultato di una lista di supporto PROFILI richiamata da questa pagina (risultato della simulazione)
			ListaSuppProfiloVO ricProf=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			if (ricProf!=null && ricProf.getChiamante()!=null && ricProf.getChiamante().equals(mapping.getPath()))	{
				if (ricProf!=null && ricProf.getSelezioniChiamato()!=null && ricProf.getSelezioniChiamato().size()!=0 ) {
					if (ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice()!=null
							&& ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice().length()!=0 )	{
						//dal size aggiungo le righe selezionate e importate
						esaFornitori.getDatiFornitore().setProfiliAcq(new ArrayList());
						// 08/09/08 esaFornitori.getDatiFornitore().setCodBibl(esaFornitori.getFornitore().getFornitoreBibl().getCodBibl());
						for (int i=0; i<ricProf.getSelezioniChiamato().size(); i++)	{
							esaFornitori.getDatiFornitore().getProfiliAcq().add(ricProf.getSelezioniChiamato().get(i));
						}
					}
				} else {
					esaFornitori.getDatiFornitore().setProfiliAcq(new ArrayList());
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppProfiloVO");
				request.getSession().removeAttribute("profiliSelected");
				request.getSession().removeAttribute("criteriRicercaProfilo");

 			}
			// controllo che non sia stata attivata la lista supporto delle biblioteche

			BibliotecaVO biblioteca=(BibliotecaVO) request.getAttribute("biblioteca");
			if (biblioteca!=null && biblioteca.getIdBiblioteca()>0)	{
				esaFornitori.getFornitore().setTipoPartner("B");
				esaFornitori.getFornitore().setCap(biblioteca.getCap());
				esaFornitori.getFornitore().setCasellaPostale(biblioteca.getCpostale());
				esaFornitori.getFornitore().setIndirizzo(biblioteca.getIndirizzo());
				esaFornitori.getFornitore().setCitta(biblioteca.getLocalita());
				esaFornitori.getFornitore().setCodiceFiscale(biblioteca.getCod_fiscale());
				esaFornitori.getFornitore().setPaese(biblioteca.getPaese());
				esaFornitori.getFornitore().setNomeFornitore(biblioteca.getNom_biblioteca());
				esaFornitori.getFornitore().setBibliotecaFornitore(biblioteca.getCod_bib());
				esaFornitori.getFornitore().setBibliotecaFornitoreCodPolo(biblioteca.getCod_polo());
			}

			// controllo la configurazione

			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(Navigation.getInstance(request).getUtente().getCodBib());
			if (esaFornitori.getDatiFornitore()!=null
					&& esaFornitori.getDatiFornitore().getCodBibl()!=null
					&& esaFornitori.getDatiFornitore().getCodBibl().length()>0 ) {
				configurazioneCriteri.setCodBibl(esaFornitori.getDatiFornitore().getCodBibl());
			}
			configurazioneCriteri.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);

			Boolean gestProf =true;
			if (configurazioneLetta !=null  && !configurazioneLetta.isGestioneProfilo()) {
				gestProf =false;
			}
			if (!gestProf) {
				esaFornitori.setGestProf(false);
			}
			return mapping.getInputForward();
			}	catch (ValidationException ve) {
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
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;

		//Maggio 2013  - reinserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
		// per reimpostare la tastiera al ritorno sulla sintetica
		request.setAttribute("editore", esaFornitori.getEditore());

		try {
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward profiloCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			request.getSession().removeAttribute("attributeListaSuppProfiloVO");
			request.getSession().removeAttribute("profiliSelected");
			request.getSession().removeAttribute("criteriRicercaProfilo");

			this.impostaProfiloCerca( esaFornitori,request,mapping);
			return mapping.findForward("profili");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaProfiloCerca( EsaminaFornitoreForm esaFornitori,HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=esaFornitori.getFornitore().getCodBibl();
		StrutturaCombo prof=new StrutturaCombo("","");
		StrutturaCombo sez=new StrutturaCombo("","");
		StrutturaCombo lin=new StrutturaCombo("","");
		StrutturaCombo pae=new StrutturaCombo(esaFornitori.getFornitore().getPaese(),"");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,   chiama, ordina );
		StrutturaCombo forn=new StrutturaCombo("","");
		// impostare il fornitore
		eleRicerca.setFornitore(forn);
		// impostare le selezioni chiamato con i profili associati al fornitore trovati con la lettura
		if (esaFornitori.getDatiFornitore()!=null
				&& esaFornitori.getDatiFornitore().getProfiliAcq()!=null
				&& esaFornitori.getDatiFornitore().getProfiliAcq().size()>0) {
			eleRicerca.setSelezioniChiamato(esaFornitori.getDatiFornitore().getProfiliAcq());
		}
		request.getSession().setAttribute("attributeListaSuppProfiloVO", eleRicerca);

	}catch (Exception e) {	}
	}


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;

		// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// gestione del campo isbnEditore che contyiene i 3 possibile isbn che saranno visualizzati splittati su 3 campi
		String isbnEditoreTot="";
		if (esaFornitori.getIsbnEditore1() != null && esaFornitori.getIsbnEditore1().length() > 0) {
			isbnEditoreTot = esaFornitori.getIsbnEditore1();
		}
		if (esaFornitori.getIsbnEditore2() != null && esaFornitori.getIsbnEditore2().length() > 0) {
			isbnEditoreTot = isbnEditoreTot + "," + esaFornitori.getIsbnEditore2();
		}
		if (esaFornitori.getIsbnEditore3() != null && esaFornitori.getIsbnEditore3().length() > 0) {
			isbnEditoreTot = isbnEditoreTot + "," + esaFornitori.getIsbnEditore3();
		}
		esaFornitori.getFornitore().setIsbnEditore(isbnEditoreTot);

		if (esaFornitori.getEditore() != null && esaFornitori.getEditore().equals("SI")) {
			if (esaFornitori.getFornitore().getRegione() == null || esaFornitori.getFornitore().getRegione() .equals("")) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.codiceRegioneObbligatorio"));
				this.saveErrors(request, errors);
				esaFornitori.setConferma(false);
				esaFornitori.setPressioneBottone("");
	    		esaFornitori.setDisabilitaTutto(false);
				return mapping.getInputForward();
			}
		}


		try {
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaFornitoreVO(esaFornitori.getFornitore());
			// fine validazione
			ActionMessages errors = new ActionMessages();
			esaFornitori.setConferma(true);
			esaFornitori.setPressioneBottone("salva");
    		esaFornitori.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			esaFornitori.setConferma(false);
			esaFornitori.setPressioneBottone("");
    		esaFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();

		} catch (Exception e) {
			esaFornitori.setConferma(false);
			esaFornitori.setPressioneBottone("");
    		esaFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
			this.loadDatiFornitorePassato( esaFornitori,esaFornitori.getListaDaScorrere().get(esaFornitori.getPosizioneScorrimento()));
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
			esaFornitori.setConferma(false);
    		esaFornitori.setDisabilitaTutto(false);

			FornitoreVO eleFornitore=esaFornitori.getFornitore();
			eleFornitore.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			// PROPOSTA CONCORDATA TELEFONICAMENTE FRA ROSSANA E MARGHERITA:
			// in cancellazione editore: se editore/fornitore è fornitore di biblio si eliminano solo le specificità
			// (cioè i dati che lo rendono editore); se invece editore/fornitore NON è fornitore si cancella anche
			// l’anagrafica sulla tabella;
			// in cancellazione fornitore: se editore/fornitore è fornitore di biblio si eliminano le specificità
			// (cioè i dati che lo rendono fornitore) ma si lascia l’anagrafica per la linea degli editori;
			// se invece editore/fornitore NON è fornitore si cancella anche l’anagrafica sulla tabella

			if (esaFornitori.getEditore().equals("SI")) {
				// OTTOBRE 2013 questa modifica serve per non inserire i dati di FornitoreBibl quando dalla linea di editori
				// si aggiornano i dati dell'editore
				eleFornitore.setFornitoreBibl(null);
			} else {
				eleFornitore.setFornitoreBibl(esaFornitori.getDatiFornitore());
			}
			//eleFornitore.setFornitoreBibl(esaFornitori.getDatiFornitore());


			GeneraChiave keyDuoble = new GeneraChiave(eleFornitore.getNomeFornitore().trim(),"");
			keyDuoble.estraiChiavi("",esaFornitori.getFornitore().getNomeFornitore().trim());
			esaFornitori.getFornitore().setChiaveFor(keyDuoble.getKy_cles1_A());


    		if (esaFornitori.getPressioneBottone().equals("salva")) {
				esaFornitori.setPressioneBottone("");

				ListaSuppFornitoreVO attrLS=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				ListaSuppFornitoreVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleFornitore, attrLS);
				request.getSession().setAttribute("attributeListaSuppFornitoreVO",attrLSagg );

				if (!this.modificaFornitore(eleFornitore)) {
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

			if (esaFornitori.getPressioneBottone().equals("cancella")) {

				if (esaFornitori.getEditore().equals("SI")) {

					// La cancellazione viene trasformata in aggiornamento dei soli dati di regione e isbn/num.Standard associati;
					// OTTOBRE 2013: PROPOSTA CONCORDATA TELEFONICAMENTE FRA ROSSANA E MARGHERITA: in cancellazione editore:
					//-- se editore/fornitore è fornitore di biblio si eliminano solo le specificità
					//   (cioè i dati che lo rendono editore); se invece editore/fornitore NON è fornitore si cancella anche
					//   l’anagrafica sulla tabella; in cancellazione fornitore:
					//-- se editore/fornitore è fornitore di biblio si eliminano le specificità (cioè i dati che lo rendono fornitore)
					//   ma si lascia l’anagrafica per la linea degli editori; se invece editore/fornitore NON è fornitore si cancella
					//   anche l’anagrafica sulla tabella

					esaFornitori.getFornitore().setProdEditoriale("SI");

					if (esaFornitori.getFornitore().getBibliotecaFornitore().equals("   ")
							&& esaFornitori.getFornitore().getBibliotecaFornitoreCodPolo().equals("   ")) {
						// CASO EDITORE PURO: si puo effettuare la cancellazione completa
						esaFornitori.setPressioneBottone("");
						eleFornitore=esaFornitori.getFornitore();
						eleFornitore.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
						if (!this.cancellaFornitore(eleFornitore)) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
									"errors.acquisizioni.erroreCancella"));
							this.saveErrors(request, errors);
						} else {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.cancellaOK"));
							this.saveErrors(request, errors);
				    		esaFornitori.setDisabilitaTutto(true);
						}
					} else {
						// CASO EDITORE/FORNITORE: si puo effettuare SOLO l'aggiornamento per eliminare le tipicità di EDITORE
						esaFornitori.setPressioneBottone("");

						ListaSuppFornitoreVO attrLS=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
						ListaSuppFornitoreVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleFornitore, attrLS);
						request.getSession().setAttribute("attributeListaSuppFornitoreVO",attrLSagg );

						eleFornitore.setRegione("");
						eleFornitore.setIsbnEditore("");
						eleFornitore.setListaNumStandard(new ArrayList<TabellaNumSTDImpronteVO>());

						if (!this.modificaFornitore(eleFornitore)) {
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
					}

				} else {
					// SIAMO SULLA LINEA DEI FORNITORI
					esaFornitori.getFornitore().setProdEditoriale("NO");
					if (esaFornitori.getRegioneForn() == null || esaFornitori.getRegioneForn().trim().equals("")) {
						// SIAMO NEL CASO DI FORNITORE PURO: si effettua la cancellazione completa
						esaFornitori.setPressioneBottone("");
						eleFornitore=esaFornitori.getFornitore();
						eleFornitore.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
						if (!this.cancellaFornitore(eleFornitore)) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
									"errors.acquisizioni.erroreCancella"));
							this.saveErrors(request, errors);
						} else {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.cancellaOK"));
							this.saveErrors(request, errors);
				    		esaFornitori.setDisabilitaTutto(true);
						}
					} else {
						// SIAMO NEL CASO DI FORNITORE/EDITORE: si cancellano solo le tipicità del fornitore (tabella fornitore X biblioteca)
						esaFornitori.setPressioneBottone("");
						eleFornitore=esaFornitori.getFornitore();
						eleFornitore.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
						if (!this.cancellaFornitore(eleFornitore)) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
									"errors.acquisizioni.erroreCancella"));
							this.saveErrors(request, errors);
						} else {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.cancellaOK"));
							this.saveErrors(request, errors);
				    		esaFornitori.setDisabilitaTutto(true);
						}

					}

				}
			}
			return mapping.getInputForward();

			}	catch (ValidationException ve) {
			esaFornitori.setConferma(false);
			esaFornitori.setPressioneBottone("");
    		esaFornitori.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			esaFornitori.setConferma(false);
			esaFornitori.setPressioneBottone("");
    		esaFornitori.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaFornitori.setConferma(false);
			esaFornitori.setPressioneBottone("");
    		esaFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
			int attualePosizione=esaFornitori.getPosizioneScorrimento()+1;
			int dimensione=esaFornitori.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);
			} else {
				try {
					this.loadDatiFornitorePassato( esaFornitori,esaFornitori.getListaDaScorrere().get(attualePosizione));

				} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001) {
							// passa indietro perchè l'elemento è stato cancellato
							esaFornitori.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
				} catch (Exception e) {
						e.printStackTrace();
						throw e;
				}

				esaFornitori.setPosizioneScorrimento(attualePosizione);
				// aggiornamento del tab di visualizzazione dei dati per tipo ordine
	    		esaFornitori.setDisabilitaTutto(false);
				if (esaFornitori.isEsaminaInibito()) {
					esaFornitori.setDisabilitaTutto(true);
				}
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
			int attualePosizione=esaFornitori.getPosizioneScorrimento()-1;
			int dimensione=esaFornitori.getListaDaScorrere().size();
			if (attualePosizione < 0) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage( "errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);
			} else {
				try	{
						this.loadDatiFornitorePassato( esaFornitori, esaFornitori.getListaDaScorrere().get(attualePosizione));

					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001) {
							// passa indietro perchè l'elemento è stato cancellato
							esaFornitori.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}

					esaFornitori.setPosizioneScorrimento(attualePosizione);
		    		esaFornitori.setDisabilitaTutto(false);
					if (esaFornitori.isEsaminaInibito()) {
						esaFornitori.setDisabilitaTutto(true);
					}
				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;

		// Maggio 2013 Dalla linea fornitori la cancellazione non viene effettuata se il fornitore è anche editore
		// OTTOBRE: questo intervento viene eliminato: se editore/fornitore è fornitore di biblio si eliminano solo le specificità
		//   (cioè i dati che lo rendono editore); se invece editore/fornitore NON è fornitore si cancella anche
		//   l’anagrafica sulla tabella; in cancellazione fornitore:
//		if (esaFornitori.getEditore().equals("NO") && (esaFornitori.getRegioneForn() != null && esaFornitori.getRegioneForn().length() > 0)) {
//			ActionMessages errors = new ActionMessages();
//			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreCancellaFornitoreEditore"));
//			this.saveErrors(request, errors);
//			return mapping.getInputForward();
//		}


		try {
			ActionMessages errors = new ActionMessages();
			esaFornitori.setConferma(true);
			esaFornitori.setPressioneBottone("cancella");
    		esaFornitori.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			esaFornitori.setConferma(false);
			esaFornitori.setPressioneBottone("");
    		esaFornitori.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		try {
			// oppure leggere con locale=1 e cod bibl e polo specificati in listasupp
			List<FornitoreVO> listaFornitoriSel = new ArrayList<FornitoreVO>();
			listaFornitoriSel.add(esaFornitori.getFornitore());

			request.setAttribute("FUNZIONE_STAMPA",     StampaType.STAMPA_LISTA_FORNITORI);
			request.setAttribute("DATI_STAMPE_ON_LINE", listaFornitoriSel);

			return mapping.findForward("stampaOL");
		} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			return mapping.getInputForward();
		}

	}


	public ActionForward profili(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {

			// selezione del fornitore in oggetto
			// solo per fornitori di biblioteca
			if (esaFornitori.getFornitore().getFornitoreBibl()!=null &&  esaFornitori.getFornitore().getFornitoreBibl().getCodBibl()!=null && esaFornitori.getFornitore().getFornitoreBibl().getCodBibl().trim().length()>0)
			{
				request.setAttribute("fornOggetto", esaFornitori.getFornitore());
				request.getSession().removeAttribute("attributeListaSuppProfiloVO");
				this.impostaProfiloCerca( esaFornitori,request,mapping);
				request.getSession().setAttribute("chiamante",  mapping.getPath());
				return mapping.findForward("profili");
			}
			else
			{
	    		ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreNotFornDiBibl"));
	    		this.saveErrors(request, errors);
			}
			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward importaBibl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		try {
		//return mapping.getInputForward();
		// IMPORTANTE: impostare IL COD POLO E IL COD BIBL DELLA BIBLIOTECA UTILIZZATA COME FORNITORE
		return mapping.findForward("importaBibl");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadDatiFornitorePassato(EsaminaFornitoreForm esaFornitori, ListaSuppFornitoreVO criteriRicercaFornitore) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<FornitoreVO> ordiniTrovato = new ArrayList();
		ordiniTrovato = factory.getGestioneAcquisizioni().getRicercaListaFornitori(criteriRicercaFornitore);

		//gestire l'esistenza del risultato e che sia univoco
		esaFornitori.setFornitore(ordiniTrovato.get(0));
		DatiFornitoreVO datiForn=new DatiFornitoreVO();
		if (ordiniTrovato.get(0).getFornitoreBibl()!=null) {
			esaFornitori.setAssenzaFornitoreBibl(false);
			datiForn=ordiniTrovato.get(0).getFornitoreBibl();
		} else {
			esaFornitori.setAssenzaFornitoreBibl(true);
			datiForn=new DatiFornitoreVO("", "", "", "", "", "","","","");
		}
		esaFornitori.setDatiFornitore(datiForn);
	}


	private void loadTipologieFornitore(EsaminaFornitoreForm esaFornitori) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		esaFornitori.setListaTipoForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoPartner()));
	}

	private boolean modificaFornitore(FornitoreVO fornitore) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaFornitore(fornitore);
		return valRitorno;
	}

	private boolean cancellaFornitore(FornitoreVO fornitore) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaFornitore(fornitore);
		return valRitorno;
	}

	private void loadProvincia(EsaminaFornitoreForm esaFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
    	esaFornitori.setListaProvinciaForn(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE)));
	}

	private void loadRegione(EsaminaFornitoreForm esaFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
    	esaFornitori.setListaRegioneForn(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_REGIONE)));
	}

	private void loadPaese(EsaminaFornitoreForm esaFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
    	esaFornitori.setListaPaeseForn(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_PAESE)));
	}

	private void loadValuta(EsaminaFornitoreForm esaFornitori, HttpServletRequest request,  String biblSel) throws Exception {
    	// esegui query su cambi di biblioteca
    	List<CambioVO> listaCambi=null;
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

    	ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=Navigation.getInstance(request).getUtente().getCodBib();
		codB=biblSel;
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
        		StrutturaTerna elem = new StrutturaTerna(listaCambi.get(w).getCodValuta(),  listaCambi.get(w).getDesValuta().trim(), String.valueOf(listaCambi.get(w).getTassoCambio()));
    			if (listaCambi.get(w).getCodValuta().equals("EUR")) {
    				esisteEuro=true;
    			}
        		lista.add(elem);
    		}
    	}
    	// assegnazione della valuta di default (eur ) nel caso in cui non sia valorizzata
    	if (esaFornitori.getDatiFornitore().getValuta()==null || (esaFornitori.getDatiFornitore().getValuta()!=null && esaFornitori.getDatiFornitore().getValuta().trim().length()==0));
		{
	    	if (esisteEuro) {
	    		esaFornitori.getDatiFornitore().setValuta("EUR");
	    	} else {
	        	StrutturaTerna primoEle= (StrutturaTerna) lista.get(0); // il primo della lista
	        	esaFornitori.getDatiFornitore().setValuta(primoEle.getCodice1());
	    	}
		}
		esaFornitori.setListaValuta(lista);
	}
	private ConfigurazioneORDVO loadConfigurazioneORD(ConfigurazioneORDVO configurazioneORD) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
		return configurazioneTrovata;
	}


	private ListaSuppFornitoreVO AggiornaTipoVarRisultatiListaSupporto (FornitoreVO eleFornitore, ListaSuppFornitoreVO attributo)
	{
		try {
			if (eleFornitore !=null)
			{
			List<FornitoreVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getCodFornitore();
					if (eleRis.equals(eleFornitore.getCodFornitore()))
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
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_FORNITORI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		return false;
	}

	public ActionForward insNumStandard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;
		esaFornitori.getFornitore().addListaNumStandard(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canNumStandard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaFornitoreForm esaFornitori = (EsaminaFornitoreForm) form;

		if (esaFornitori.getSelezRadioNumStandard() == null
				|| esaFornitori.getSelezRadioNumStandard().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblNumStandard"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(esaFornitori.getSelezRadioNumStandard());
		esaFornitori.getFornitore().getListaNumStandard().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}


}
