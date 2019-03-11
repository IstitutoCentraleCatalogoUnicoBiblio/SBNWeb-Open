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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.suggerimenti.EsaminaSugBiblForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EsaminaSugBiblAction extends SugBiblBaseAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.accetta", "accetta");
		map.put("ricerca.button.rifiuta", "rifiuta");
		map.put("ricerca.button.salva", "salva");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("ricerca.button.cancella", "cancella");
		map.put("ricerca.button.indietro", "indietro");
		map.put("servizi.bottone.si", "Si");
		map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti", "scorriAvanti");
		map.put("ricerca.button.scorriIndietro", "scorriIndietro");
		map.put("ricerca.button.invia", "invia");
		map.put("ricerca.label.fattura", "fatturaCerca");
		map.put("ordine.label.fornitore", "fornitoreCerca");
		map.put("ricerca.button.ordine", "ordineCerca");
		map.put("ricerca.button.stampa", "stampaOnLine");

		//almaviva5_20180323 #6553
		map.put("ricerca.button.in_attesa", "attesa");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			EsaminaSugBiblForm currentForm = (EsaminaSugBiblForm) form;
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
	            return mapping.getInputForward();
			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppSuggerimentoVO ricArr=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				currentForm.setEsaminaInibito(true);
				currentForm.setDisabilitaTutto(true);
			}

			currentForm.setListaDaScorrere((List<ListaSuppSuggerimentoVO>) request.getSession().getAttribute("criteriRicercaSuggerimento"));
			if(currentForm.getListaDaScorrere() != null && currentForm.getListaDaScorrere().size()!=0)
			{
				if(currentForm.getListaDaScorrere().size()>1 )
				{
					currentForm.setEnableScorrimento(true);
					//esaCambio.setPosizioneScorrimento(0);
				}
				else
				{
					currentForm.setEnableScorrimento(false);
				}
			}
			// || strIsAlfabetic(String.valueOf(this.esaSezione.getPosizioneScorrimento()))
			if (String.valueOf(currentForm.getPosizioneScorrimento()).length()==0 )
			{
				currentForm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			if (!currentForm.isCaricamentoIniziale())
			{
				this.loadDatiSuggerimentoPassato(currentForm, currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
				currentForm.setCaricamentoIniziale(true);
			}
			//almaviva5_20130513 #4861
			controlloSezione(mapping, request, currentForm);

			//this.loadDatiSuggerimentoPassato(esaSugg.getListaDaScorrere().get(esaSugg.getPosizioneScorrimento()));
			this.loadStatoSuggerimento(currentForm);
			// temporaneamente
			//esaSugg.getDatiSuggerimento().setBibliotecario(new StrutturaCombo("",""));
			//esaSugg.getDatiSuggerimento().getBibliotecario().setCodice(Navigation.getInstance(request).getUtente().getFirmaUtente());

			if (currentForm.getDatiSuggerimento() != null
					&& ValidazioneDati.in(currentForm.getDatiSuggerimento().getStatoSuggerimento(), "O") ) {
				currentForm.setDisabilitaTutto(true);
			} else 	{
				currentForm.setDisabilitaTutto(false);
			}

			//almaviva5_20180323 #6553
			if (navi.bookmarkExists(Bookmark.Acquisizioni.Ordini.IMPORTA_DATI_ORDINE))
				currentForm.setDisabilitaTutto(true);

			return mapping.getInputForward();

			}	catch (ValidationException ve) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {

				//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

				return mapping.getInputForward();
			}
	}



	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		try {
			// validazione
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaSuggerimentoVO(esaSugg.getDatiSuggerimento());
			// fine validazione


			esaSugg.setConferma(true);
			esaSugg.setPressioneBottone("salva");
    		esaSugg.setDisabilitaTutto(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			esaSugg.setConferma(false);
			esaSugg.setPressioneBottone("");
    		esaSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();

		} catch (Exception e) {
			esaSugg.setConferma(false);
			esaSugg.setPressioneBottone("");
    		esaSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward stampaOnLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
			List<SuggerimentoVO> listaSugg = new ArrayList<SuggerimentoVO> ();
			SuggerimentoVO eleSuggerimento=esaSugg.getDatiSuggerimento();
			listaSugg.add(eleSuggerimento);
			if (listaSugg!=null && listaSugg.size()>0)
			{
					// DA RIPRISTINARE PER LA STAMPA (almaviva)
					request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_SUGGERIMENTI_BIBLIOTECARIO);
					//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
					request.setAttribute("DATI_STAMPE_ON_LINE", listaSugg);
					return mapping.findForward("stampaOL");

			}
			return mapping.getInputForward();

		} catch (Exception e) {
				resetToken(request);
				e.printStackTrace();
				return mapping.getInputForward();
		}
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm currentForm = (EsaminaSugBiblForm) form;
		try {
			this.loadDatiSuggerimentoPassato(currentForm, currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
			if (currentForm.getDatiSuggerimento() != null
					&& ValidazioneDati.in(currentForm.getDatiSuggerimento().getStatoSuggerimento(), "O") ) {
				currentForm.setDisabilitaTutto(true);
			} else 	{
				currentForm.setDisabilitaTutto(false);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		try {
			esaSugg.setConferma(false);
    		esaSugg.setDisabilitaTutto(false);
    		esaSugg.getDatiSuggerimento().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			if (esaSugg.getPressioneBottone().equals("salva")) {
				esaSugg.setPressioneBottone("");

				SuggerimentoVO eleSuggerimento=esaSugg.getDatiSuggerimento();

				ListaSuppSuggerimentoVO attrLS=(ListaSuppSuggerimentoVO) request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
				ListaSuppSuggerimentoVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleSuggerimento, attrLS);
				request.getSession().setAttribute("attributeListaSuppSuggerimentoVO",attrLSagg );
				// imposta a ricevuto se la direzione è "da fornitore"
/*				if (esaSugg.getDatiSuggerimento().getDirezioneComunicazione().equals("D"))
				{
					esaSugg.getDatiSuggerimento().setStatoComunicazione("3");
				}*/
				if (!this.modificaSuggerimento(eleSuggerimento)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreModifica"));

					//return mapping.getInputForward();
				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.modificaOK"));

					return ripristina( mapping,  form,  request,  response);

				}
			}
			if (esaSugg.getPressioneBottone().equals("cancella")) {
				esaSugg.setPressioneBottone("");
				SuggerimentoVO eleSuggerimento=esaSugg.getDatiSuggerimento();
				if (!this.cancellaSuggerimento(eleSuggerimento)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreCancella"));

				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.cancellaOK"));

		    		esaSugg.setDisabilitaTutto(true);
				}

			}


			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			esaSugg.setConferma(false);
			esaSugg.setPressioneBottone("");
    		esaSugg.setDisabilitaTutto(false);

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			esaSugg.setConferma(false);
			esaSugg.setPressioneBottone("");
    		esaSugg.setDisabilitaTutto(false);

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaSugg.setConferma(false);
			esaSugg.setPressioneBottone("");
    		esaSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm currentForm = (EsaminaSugBiblForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));


				}
			else
				{
					try
					{
						this.loadDatiSuggerimentoPassato(currentForm, currentForm.getListaDaScorrere().get(attualePosizione));
					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							currentForm.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}

					currentForm.setPosizioneScorrimento(attualePosizione);
					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
		    		currentForm.setDisabilitaTutto(false);
		    		if (currentForm.getDatiSuggerimento() != null
							&& ValidazioneDati.in(currentForm.getDatiSuggerimento().getStatoSuggerimento(), "O") ) {
						currentForm.setDisabilitaTutto(true);
					} else 	{
						currentForm.setDisabilitaTutto(false);
					}

		    		if (currentForm.isEsaminaInibito())
					{
						currentForm.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm currentForm = (EsaminaSugBiblForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;

			if (attualePosizione < 0)
				{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));


				}
			else
				{
					try
					{
						this.loadDatiSuggerimentoPassato( currentForm, currentForm.getListaDaScorrere().get(attualePosizione));
					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							currentForm.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}

					currentForm.setPosizioneScorrimento(attualePosizione);
		    		currentForm.setDisabilitaTutto(false);
		    		if (currentForm.getDatiSuggerimento() != null
							&& ValidazioneDati.in(currentForm.getDatiSuggerimento().getStatoSuggerimento(), "O") ) {
						currentForm.setDisabilitaTutto(true);
					} else 	{
						currentForm.setDisabilitaTutto(false);
					}
		    		if (currentForm.isEsaminaInibito())
					{
						currentForm.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		try {

			esaSugg.setConferma(true);
			esaSugg.setPressioneBottone("cancella");
    		esaSugg.setDisabilitaTutto(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			esaSugg.setConferma(false);
			esaSugg.setPressioneBottone("");
    		esaSugg.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	private void loadDatiSuggerimentoPassato(EsaminaSugBiblForm esaSugg, ListaSuppSuggerimentoVO criteriRicercaSuggerimento) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SuggerimentoVO> suggerimentoTrovato = new ArrayList<SuggerimentoVO>();
		suggerimentoTrovato = factory.getGestioneAcquisizioni().getRicercaListaSuggerimenti(criteriRicercaSuggerimento);
		//gestire l'esistenza del risultato e che sia univoco
		esaSugg.setDatiSuggerimento(suggerimentoTrovato.get(0));
	}

	private boolean modificaSuggerimento(SuggerimentoVO suggerimento) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaSuggerimento(suggerimento);
		return valRitorno;
	}

	private boolean cancellaSuggerimento(SuggerimentoVO suggerimento)throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaSuggerimento(suggerimento);
		return valRitorno;
	}
	private ListaSuppSuggerimentoVO AggiornaTipoVarRisultatiListaSupporto (SuggerimentoVO eleSuggerimento, ListaSuppSuggerimentoVO attributo)
	{
		try {
			if (eleSuggerimento !=null)
			{
			List<SuggerimentoVO> risultati=new ArrayList<SuggerimentoVO>();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getChiave().trim();
					if (eleRis.equals(eleSuggerimento.getChiave().trim()))
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
		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward accetta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {

			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

			return mapping.getInputForward();

		}

		String statoOLD="W";
		if (esaSugg.getDatiSuggerimento()!=null && esaSugg.getDatiSuggerimento().getStatoSuggerimento()!=null && esaSugg.getDatiSuggerimento().getStatoSuggerimento().trim().length()>0 )
		{
			statoOLD=esaSugg.getDatiSuggerimento().getStatoSuggerimento().trim();
		}
		try {
//			return mapping.findForward("accetta");
			// cambia stato al messaggio e disabilita tutto
			esaSugg.getDatiSuggerimento().setStatoSuggerimento("A");
    		//esaSugg.setDisabilitaTutto(true);
			SuggerimentoVO eleSuggerimento=esaSugg.getDatiSuggerimento();
			if (!this.modificaSuggerimento(eleSuggerimento)) {
				esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreModifica"));

				//return mapping.getInputForward();
			}
			else
			{

				LinkableTagUtils.addError(request, new ActionMessage(
				"errors.acquisizioni.modificaOK"));

				return ripristina( mapping,  form,  request,  response);
			}

			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) {
			esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
			return mapping.getInputForward();
		}
	}

	public ActionForward rifiuta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {

			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

			return mapping.getInputForward();

		}
		String statoOLD="W";
		if (esaSugg.getDatiSuggerimento()!=null && esaSugg.getDatiSuggerimento().getStatoSuggerimento()!=null && esaSugg.getDatiSuggerimento().getStatoSuggerimento().trim().length()>0 )
		{
			statoOLD=esaSugg.getDatiSuggerimento().getStatoSuggerimento().trim();
		}

		try {
//			return mapping.findForward("rifiuta");
			// cambia stato al messaggio e disabilita tutto
			esaSugg.getDatiSuggerimento().setStatoSuggerimento("R");
    		//esaSugg.setDisabilitaTutto(true);
			SuggerimentoVO eleSuggerimento=esaSugg.getDatiSuggerimento();
			if (!this.modificaSuggerimento(eleSuggerimento)) {
				esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreModifica"));

				//return mapping.getInputForward();
			}
			else
			{

				LinkableTagUtils.addError(request, new ActionMessage(
				"errors.acquisizioni.modificaOK"));

				return ripristina( mapping,  form,  request,  response);
			}

			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) {
			esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
			return mapping.getInputForward();
		}
	}

	public ActionForward attesa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSugBiblForm esaSugg = (EsaminaSugBiblForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {

			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

			return mapping.getInputForward();

		}
		String statoOLD="W";
		if (esaSugg.getDatiSuggerimento()!=null && esaSugg.getDatiSuggerimento().getStatoSuggerimento()!=null && esaSugg.getDatiSuggerimento().getStatoSuggerimento().trim().length()>0 )
		{
			statoOLD=esaSugg.getDatiSuggerimento().getStatoSuggerimento().trim();
		}

		try {
//			return mapping.findForward("rifiuta");
			// cambia stato al messaggio e disabilita tutto
			esaSugg.getDatiSuggerimento().setStatoSuggerimento("W");
    		//esaSugg.setDisabilitaTutto(true);
			SuggerimentoVO eleSuggerimento=esaSugg.getDatiSuggerimento();
			if (!this.modificaSuggerimento(eleSuggerimento)) {
				esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreModifica"));

				//return mapping.getInputForward();
			}
			else
			{

				LinkableTagUtils.addError(request, new ActionMessage(
				"errors.acquisizioni.modificaOK"));

				return ripristina( mapping,  form,  request,  response);
			}

			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) {
			esaSugg.getDatiSuggerimento().setStatoSuggerimento(statoOLD);
			return mapping.getInputForward();
		}
	}

/*	private void loadDatiSuggerimento() throws Exception {

//		(String codP, String codB, String codSugg, String statoSugg,String dataSugg, StrutturaCombo titSugg,StrutturaCombo biblSugg, StrutturaCombo sezSugg, String noteSugg,String noteForn, String noteBibl )
		SuggerimentoVO sugg=new SuggerimentoVO("X10", "01", "12", "A","08/02/2006", new StrutturaCombo("RAV0131567","Storia Greca / Hermann Bengston - Bologna"),new StrutturaCombo("5","bibliotecario cinque"),new StrutturaCombo("","SEZIONE NON TROVATA"),  "", "", "");
		esaSugg.setDatiSuggerimento(sugg);
	}*/

	private void loadStatoSuggerimento(EsaminaSugBiblForm esaSugg) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		esaSugg.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento()));
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "GESTIONE") ) {
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				//almaviva5_20150723 fix cod. attività
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {

				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		//almaviva5_20180323 #6553
		EsaminaSugBiblForm currentForm = (EsaminaSugBiblForm) form;
		SuggerimentoVO sugg = currentForm.getDatiSuggerimento();
		if (sugg != null) {
			if (ValidazioneDati.equals(idCheck, "ATTESA")) {
				return ValidazioneDati.in(sugg.getStatoSuggerimento(), "A", "R");
			}

			if (ValidazioneDati.equals(idCheck, "RIFIUTA")) {
				return ValidazioneDati.in(sugg.getStatoSuggerimento(), "A", "W");
			}

			if (ValidazioneDati.equals(idCheck, "ACCETTA")) {
				return ValidazioneDati.in(sugg.getStatoSuggerimento(), "W", "R");
			}
		}
		return false;
	}

}
