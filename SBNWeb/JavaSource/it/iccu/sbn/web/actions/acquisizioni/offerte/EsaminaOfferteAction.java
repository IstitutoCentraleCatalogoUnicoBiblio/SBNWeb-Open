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
import it.iccu.sbn.web.actionforms.acquisizioni.offerte.EsaminaOfferteForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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

public class EsaminaOfferteAction extends  LookupDispatchAction {

	//private EsaminaOfferteForm esaOfferte;



	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
		map.put("ricerca.button.accetta","accetta");
		map.put("ordine.bottone.searchTit", "sifbid");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!esaOfferte.isSessione())
				{
					esaOfferte.setSessione(true);
				}
				// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
				// DISABILITAZIONE DELL'INPUT
				ListaSuppOffertaFornitoreVO ricArr=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");
				// controllo che non riceva l'attributo di sessione di una lista supporto
				if (ricArr!=null  && ricArr.getChiamante()!=null)
				{
					esaOfferte.setDisabilitaTutto(true);
				}

				esaOfferte.setListaDaScorrere((List<ListaSuppOffertaFornitoreVO>) request.getSession().getAttribute("criteriRicercaOfferta"));
				if(esaOfferte.getListaDaScorrere() != null && esaOfferte.getListaDaScorrere().size()!=0)
				{
					if(esaOfferte.getListaDaScorrere().size()>1 )
					{
						esaOfferte.setEnableScorrimento(true);
						//esaCambio.setPosizioneScorrimento(0);
					}
					else
					{
						esaOfferte.setEnableScorrimento(false);
					}
				}
				// || strIsAlfabetic(String.valueOf(this.esaSezione.getPosizioneScorrimento()))
				if (String.valueOf(esaOfferte.getPosizioneScorrimento()).length()==0 )
				{
					esaOfferte.setPosizioneScorrimento(0);
				}

				//VALUTAZIONE SCELTA DEL TAB

				// richiamo ricerca su db con elemento 1 di ricerca
				if (!esaOfferte.isCaricamentoIniziale())
				{
					this.loadDatiOffertaPassata( esaOfferte, esaOfferte.getListaDaScorrere().get(esaOfferte.getPosizioneScorrimento()));
					esaOfferte.setCaricamentoIniziale(true);
				}

				if (request.getParameter("paramLink") != null) {
					esaOfferte.setScegliTAB(request.getParameter("paramLink"));
				}

				//this.loadDatiComunicazionePassata(esaCom.getListaDaScorrere().get(this.esaCom.getPosizioneScorrimento()));
				this.loadLingue( esaOfferte);
				this.loadPaesi( esaOfferte);
				this.loadTipoDataOfferta( esaOfferte);
				this.loadTipoPrezzoOfferta( esaOfferte);
				this.loadTipoProvenienza( esaOfferte);
				this.loadTipoResponsabilita( esaOfferte);
				this.loadStatoSuggOfferta( esaOfferte);

				// controllo se ho un risultato da interrogazione ricerca
				String bid=(String) request.getAttribute("bid");
				if (bid!=null && bid.length()!=0 )
				{
					String titolo=(String) request.getAttribute("titolo");
					// controllo se ho un risultato da interrogazione ricerca
					//String acq = request.getParameter("ACQUISIZIONI");
					//if ( acq != null) {
					esaOfferte.getDatiOffertaForn().getBid().setCodice(bid);
					if ( titolo != null) {
						esaOfferte.getDatiOffertaForn().getBid().setDescrizione(titolo);
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


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {

			ActionMessages errors = new ActionMessages();
			esaOfferte.setConferma(true);
			esaOfferte.setPressioneBottone("salva");
    		esaOfferte.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			esaOfferte.setConferma(false);
			esaOfferte.setPressioneBottone("");
    		esaOfferte.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
			this.loadDatiOffertaPassata( esaOfferte, esaOfferte.getListaDaScorrere().get(esaOfferte.getPosizioneScorrimento()));
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
			esaOfferte.setConferma(false);
    		esaOfferte.setDisabilitaTutto(false);

			if (esaOfferte.getPressioneBottone().equals("salva")) {
				esaOfferte.setPressioneBottone("");

				OffertaFornitoreVO eleOfferta=esaOfferte.getDatiOffertaForn();
				eleOfferta.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				ListaSuppOffertaFornitoreVO attrLS=(ListaSuppOffertaFornitoreVO) request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO");
				ListaSuppOffertaFornitoreVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleOfferta, attrLS);
				request.getSession().setAttribute("attributeListaSuppOffertaFornitoreVO",attrLSagg );

				if (!this.modificaOfferta(eleOfferta)) {
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
				}
			}
/*			if (this.esaCom.getPressioneBottone().equal("cancella")) {
				this.esaCom.setPressioneBottone("");
				ComunicazioneVO eleComunicazione=esaCom.getDatiComunicazione();
				if (!this.cancellaComunicazione(eleComunicazione)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreCancella"));
					this.saveErrors(request, errors);
				}
				else
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.cancellaOK"));
					this.saveErrors(request, errors);
					esaOfferte.setDisabilitaTutto(true);

				}

			}			*/


			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			esaOfferte.setConferma(false);
			esaOfferte.setPressioneBottone("");
    		esaOfferte.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			esaOfferte.setConferma(false);
			esaOfferte.setPressioneBottone("");
    		esaOfferte.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaOfferte.setConferma(false);
			esaOfferte.setPressioneBottone("");
    		esaOfferte.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
			int attualePosizione=esaOfferte.getPosizioneScorrimento()+1;
			int dimensione=esaOfferte.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					this.loadDatiOffertaPassata( esaOfferte, esaOfferte.getListaDaScorrere().get(attualePosizione));
					esaOfferte.setPosizioneScorrimento(attualePosizione);
					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
			int attualePosizione=esaOfferte.getPosizioneScorrimento()-1;
			int dimensione=esaOfferte.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					this.loadDatiOffertaPassata( esaOfferte, esaOfferte.getListaDaScorrere().get(attualePosizione));
					esaOfferte.setPosizioneScorrimento(attualePosizione);
				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void loadDatiOffertaPassata(EsaminaOfferteForm esaOfferte, ListaSuppOffertaFornitoreVO criteriRicercaOfferta) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<OffertaFornitoreVO> offertaTrovata = new ArrayList();
		offertaTrovata = factory.getGestioneAcquisizioni().getRicercaListaOfferte(criteriRicercaOfferta);
		//gestire l'esistenza del risultato e che sia univoco
		esaOfferte.setDatiOffertaForn(offertaTrovata.get(0));

		esaOfferte.setNumRigheAut(esaOfferte.getDatiOffertaForn().getOffFornAut().size());
		esaOfferte.setNumRigheSer(esaOfferte.getDatiOffertaForn().getOffFornSerie().size());
		esaOfferte.setNumRigheSog(esaOfferte.getDatiOffertaForn().getOffFornSogg().size());
		esaOfferte.setNumRigheCla(esaOfferte.getDatiOffertaForn().getOffFornClass().size());
		esaOfferte.setNumRigheNot(esaOfferte.getDatiOffertaForn().getOffFornNote().size());
		esaOfferte.setNumRigheIsb(esaOfferte.getDatiOffertaForn().getOffFornIsbd().size());

		// caricamento righe aut
		esaOfferte.setListaAutori(esaOfferte.getDatiOffertaForn().getOffFornAut());

		// caricamento righe serie
		esaOfferte.setListaSerie(esaOfferte.getDatiOffertaForn().getOffFornSerie());

		// caricamento righe soggetto
		esaOfferte.setListaSoggetto(esaOfferte.getDatiOffertaForn().getOffFornSogg());

		// caricamento righe classificazione
		esaOfferte.setListaClassificazione(esaOfferte.getDatiOffertaForn().getOffFornClass());

		// caricamento righe note edi
		esaOfferte.setListaNoteEDI(esaOfferte.getDatiOffertaForn().getOffFornNote());

		// caricamento isbd
		esaOfferte.setListaISBD(esaOfferte.getDatiOffertaForn().getOffFornIsbd());


	}

	private boolean modificaOfferta(OffertaFornitoreVO offerta) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//valRitorno = factory.getGestioneAcquisizioni().modificaOfferta(offerta);
		valRitorno=true; // da rimuovere
		return valRitorno;
	}


	private ListaSuppOffertaFornitoreVO AggiornaTipoVarRisultatiListaSupporto (OffertaFornitoreVO eleOfferta, ListaSuppOffertaFornitoreVO attributo)
	{
		try {
			if (eleOfferta !=null)
			{
			List<OffertaFornitoreVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getChiave().trim();
					if (eleRis.equals(eleOfferta.getChiave().trim()))
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


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward accetta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
//			return mapping.findForward("accetta");
			// cambia stato al messaggio e disabilita tutto (DEVE ESISTERE BID)
			esaOfferte.getDatiOffertaForn().setStatoOfferta("A");
    		esaOfferte.setDisabilitaTutto(true);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;
		try {
			if (esaOfferte.getDatiOffertaForn().getBid()!=null && esaOfferte.getDatiOffertaForn().getBid().getCodice()!=null)
			{
				request.setAttribute("bidFromRicOrd", esaOfferte.getDatiOffertaForn().getBid().getCodice());
			}
			return mapping.findForward("sifbid");
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}



/*	private void loadOffertaFornitore() throws Exception {
		// caricamento offerta
		// 	(String codP, String codB, String tipoProv, String idOff, String dataOff, StrutturaCombo forn, String statoOff, String valOff, String tipoPre, String pre, String infPre, String tipData, String data, String pae , String ling, String codStand, String numStand, StrutturaCombo bidOff, String KTitIsdb)

		OffertaFornitoreVO off= new OffertaFornitoreVO("X10", "01", new StrutturaCombo("L", ""), "009","", new StrutturaCombo("33", "Libreria Grande"),"W", "", "CA", "", "", "D","1999", new StrutturaCombo("IT", ""),new StrutturaCombo("ITA", ""), "", "", new StrutturaCombo("00000008", "Naturalis historia"),new StrutturaCombo("98-7556-258-2", "")  );
		esaOfferte.setDatiOffertaForn(off);

		// caricamento righe autori
		// 	(String kAut, String aut, String tipoAut, String respAut)

		List listaAut = new ArrayList();
		OffertaFornitoreAutoreVO offFornAut= new OffertaFornitoreAutoreVO ("001", "Airone", "1", "1");
		listaAut.add(offFornAut);
		offFornAut= new OffertaFornitoreAutoreVO ("001", "a cura di Andre Giardina", "1", "1");
		listaAut.add(offFornAut);

		esaOfferte.setListaAutori(listaAut);

		// caricamento righe serie
		// 	public OffertaFornitoreSerie (String numColl, String coll, String KColl) throws Exception {

		List listaSer = new ArrayList();
		OffertaFornitoreSerieVO offFornSer= new OffertaFornitoreSerieVO ("022", "Rosa", "0125");
		listaSer.add(offFornSer);
		offFornSer= new OffertaFornitoreSerieVO ("075", "Gialli", "0236");
		listaSer.add(offFornSer);

		esaOfferte.setListaSerie(listaSer);

		// caricamento righe soggetto
		// 	public OffertaFornitoreSoggetto (String sogg, String kSogg) throws Exception {

		List listaSog = new ArrayList();
		OffertaFornitoreSoggettoVO offFornSog= new OffertaFornitoreSoggettoVO ("001", "Natura");
		listaSog.add(offFornSog);
		offFornSog= new OffertaFornitoreSoggettoVO ("002", "Storia");
		listaSog.add(offFornSog);

		esaOfferte.setListaSoggetto(listaSog);

		// caricamento righe classificazione
		// 	public OffertaFornitoreClassificazione (String codSistClass, String idClass) throws Exception {

		List listaCla = new ArrayList();
		OffertaFornitoreClassificazioneVO offFornCla= new OffertaFornitoreClassificazioneVO ("A1", "PT012355");
		listaCla.add(offFornCla);
		offFornCla= new OffertaFornitoreClassificazioneVO ("A3", "BG059874");
		listaCla.add(offFornCla);

		esaOfferte.setListaClassificazione(listaCla);

		// caricamento righe note edi
		// 	public OffertaFornitoreNoteEdi (String noteEd, String coordEdi, String altriRif) throws Exception {

		List listaNot = new ArrayList();
		OffertaFornitoreNoteEdiVO offFornNot= new OffertaFornitoreNoteEdiVO("edito recentemente", "prot. edi", "");
		listaNot.add(offFornNot);

		esaOfferte.setListaNoteEDI(listaNot);

		// caricamento isbd
		// 	public OffertaFornitoreIsbd (String chiaveIsbd, String desIsbd) throws Exception {

		List listaIsb = new ArrayList();
		OffertaFornitoreIsbdVO offFornIsb= new OffertaFornitoreIsbdVO ("896-25", "collocazione Z");
		listaIsb.add(offFornIsb);
		offFornIsb= new OffertaFornitoreIsbdVO ("896-25", "collocazione F");
		listaIsb.add(offFornIsb);

		esaOfferte.setListaISBD(listaIsb);

	}
*/
	private void loadLingue(EsaminaOfferteForm esaOfferte) throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceLingua());
		esaOfferte.setListaLingue(lista);

	}

	private void loadPaesi(EsaminaOfferteForm esaOfferte) throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese());
		esaOfferte.setListaPaesi(lista);

	}
	private void loadStatoSuggOfferta(EsaminaOfferteForm esaOfferte) throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento());
		esaOfferte.setListaStatoSuggOfferta(lista);

	}

	private void loadTipoProvenienza(EsaminaOfferteForm esaOfferte) throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceProvenienzeOfferte());
		esaOfferte.setListaTipoProvenienza(lista);

	}

	private void loadTipoDataOfferta(EsaminaOfferteForm esaOfferte) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - PERIODICO O COLLEZIONE CORRENTE");
		lista.add(elem);
		elem = new StrutturaCombo("B","B - PERIODICO O COLLEZIONE SPENTO");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - SERIALE IN CORSO");
		lista.add(elem);
		elem = new StrutturaCombo("D","D - MONOGRAFIA");
		lista.add(elem);
		elem = new StrutturaCombo("E","E - RIPRODUZIONE");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - DATA INCERTA");
		lista.add(elem);
		elem = new StrutturaCombo("G","G - PUBBLICAZIONE IN PIÙ ANNI");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - RISTAMPA");
		lista.add(elem);

		esaOfferte.setListaTipoDataOfferta(lista);
	}



	private void loadTipoPrezzoOfferta(EsaminaOfferteForm esaOfferte) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("CA","CA - CATALOGO");
		lista.add(elem);
		elem = new StrutturaCombo("CT","CT - CONTRATTO");
		lista.add(elem);
		elem = new StrutturaCombo("DI","DI - PREZZO DEL DISTRIBUTORE");
		lista.add(elem);

		esaOfferte.setListaTipoPrezzoOfferta(lista);
	}






	private void loadTipoResponsabilita(EsaminaOfferteForm esaOfferte) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1 - RESPONSABILITÀ PRINCIPALE");
		lista.add(elem);
		elem = new StrutturaCombo("2","2	- RESPONSABILITÀ ALTERNATIVA");
		lista.add(elem);
		elem = new StrutturaCombo("3","3	- RESPONSABILITÀ SECONDARIA");
		lista.add(elem);
		elem = new StrutturaCombo("4","4	- RESPONSABILITÀ MATERIALE");
		lista.add(elem);

		esaOfferte.setListaTipoResponsabilita(lista);
	}


/*
	private void loadLingue() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("ITA","ITA - Italiano");
		lista.add(elem);
		elem = new StrutturaCombo("ENG","ENG - Inglese");
		lista.add(elem);
		elem = new StrutturaCombo("FRE","FRE - Francese");
		lista.add(elem);
		elem = new StrutturaCombo("JPN","JPN - Giapponese");
		lista.add(elem);

		esaOfferte.setListaLingue(lista);
	}

	private void loadStatoSuggOfferta() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - ACCETTATO ");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - ORDINATO");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - RIFIUTATO");
		lista.add(elem);
		elem = new StrutturaCombo("W","W - ATTESA DI RISPOSTA");
		lista.add(elem);

		esaOfferte.setListaStatoSuggOfferta(lista);
	}

	private void loadTipoProvenienza() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("E","E - EDITORE");
		lista.add(elem);
		elem = new StrutturaCombo("L","L - LIBRAIO");
		lista.add(elem);
		elem = new StrutturaCombo("B","B - BIBLIOGRAFIA");
		lista.add(elem);


		esaOfferte.setListaTipoProvenienza(lista);
	}


  	private void loadPaesi() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("IT","IT - ITALIA");
		lista.add(elem);
		elem = new StrutturaCombo("GB","GB - Gran Bretagna");
		lista.add(elem);
		elem = new StrutturaCombo("FR","FR - Francia");
		lista.add(elem);
		elem = new StrutturaCombo("JP","JP - Giappone");
		lista.add(elem);

		esaOfferte.setListaPaesi(lista);
	}	*/



/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		EsaminaOfferteForm esaOfferte = (EsaminaOfferteForm) form;

		if (request.getParameter("accetta0") != null) {
			//return mapping.findForward("accetta");
		}

		if (request.getParameter("ripristina0") != null) {
			//return mapping.findForward("ripristina");
		}


		if (request.getParameter("indietro1") != null) {
			return mapping.findForward("indietro");
		}
		//VALUTAZIONE SCELTA DEL TAB


		if (request.getParameter("paramLink") != null) {
			esaOfferte.setScegliTAB(request.getParameter("paramLink"));

		}


		this.loadOffertaFornitore();
		this.loadLingue();
		this.loadPaesi();
		this.loadTipoDataOfferta();
		this.loadTipoPrezzoOfferta();
		this.loadTipoProvenienza();
		this.loadTipoResponsabilita();
		this.loadStatoSuggOfferta();
		esaOfferte.setNumRigheAut(esaOfferte.getListaAutori().size());
		esaOfferte.setNumRigheSer(esaOfferte.getListaSerie().size());
		esaOfferte.setNumRigheSog(esaOfferte.getListaSoggetto().size());
		esaOfferte.setNumRigheCla(esaOfferte.getListaClassificazione().size());
		esaOfferte.setNumRigheNot(esaOfferte.getListaNoteEDI().size());
		esaOfferte.setNumRigheIsb(esaOfferte.getListaISBD().size());

		esaOfferte.setStatoSuggOfferta(esaOfferte.getDatiOffertaForn().getStatoOfferta());
		esaOfferte.setTipoDataOfferta(esaOfferte.getDatiOffertaForn().getTipoData());
		esaOfferte.setPaesi(esaOfferte.getDatiOffertaForn().getPaese().getCodice());
		esaOfferte.setLingue(esaOfferte.getDatiOffertaForn().getLingua().getCodice());
		esaOfferte.setTipoPrezzoOfferta(esaOfferte.getDatiOffertaForn().getTipoPrezzo());
		esaOfferte.setTipoProvenienza(esaOfferte.getDatiOffertaForn().getTipoProvenienza().getCodice());


		return mapping.getInputForward();
	}
*/
}
