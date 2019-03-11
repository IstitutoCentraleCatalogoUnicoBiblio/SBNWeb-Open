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
package it.iccu.sbn.web.actions.acquisizioni.cambi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.cambi.EsaminaCambioForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

public class EsaminaCambioAction extends LookupDispatchAction implements SbnAttivitaChecker{

	private boolean modificaCambio(CambioVO cambio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaCambioHib(cambio);
		return valRitorno;
	}

	private boolean cancellaCambio(CambioVO cambio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaCambioHib(cambio);
		return valRitorno;
	}

	private void loadDatiCambioPassato(EsaminaCambioForm currentForm,ListaSuppCambioVO criteriRicercaCambi) throws Exception {
//		 (String codP, String codB, String codVal, String desVal , double tasso, String tasso)
		//CambioVO camb=new CambioVO("X10", "01", "EUR", "euro",1.000000, "20/10/2003");

		//List<CambioVO> cambioTrovato=null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//List<CambioVO> cambioTrovato = (List<CambioVO>) factory.getGestioneAcquisizioni().getRicercaListaCambi(criteriRicercaCambi);
		// prova hibernate
		List<CambioVO> cambioTrovato  = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(criteriRicercaCambi);

		//gestire l'esistenza del risultato e che sia univoco
		currentForm.setDatiCambio(cambioTrovato.get(0));
		try {
			currentForm.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio( currentForm.getDatiCambio().getTassoCambio()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			currentForm.getDatiCambio().setTassoCambioStr("0,00");
		}
		currentForm.setValRifer(false);
//		 configurazione dei check
		if (currentForm.getDatiCambio()!=null &&  currentForm.getDatiCambio().isValRifer())
		{
			currentForm.setValRifer(true);
			currentForm.setValRiferChk("on");
		}
		else
		{
			currentForm.setValRifer(false);
			currentForm.setValRiferChk(null);
		}

	}


	private void loadValuta(EsaminaCambioForm currentForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
		currentForm.setListaValuta(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta()));

/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("AFA","AFA - afghani");
		lista.add(elem);
		elem = new StrutturaCombo("EUR","EUR - euro");
		lista.add(elem);
		elem = new StrutturaCombo("NZD","NZD - dollaro neozelandese");
		lista.add(elem);
		currentForm.setListaValuta(lista);
*/	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
        map.put("servizi.bottone.si", "si");
        map.put("servizi.bottone.no", "no");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//HttpSession httpSession = request.getSession();
		//setto il token per le transazioni successive
		//this.saveToken(request);
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		if (Navigation.getInstance(request).isFromBar() )
            return mapping.getInputForward();


		//CambioVO prova= (CambioVO)request.getAttribute("cambSel");

		//(String[]) appoParametroComposto=new String[]
		//request.getSession().getAttribute("cambiSelected");
		//if
		//currentForm.setSelectedCambi((String[]) appoParametroComposto );
		//request.getAttribute("listaCambi");

		if(!currentForm.isSessione())
		{
			currentForm.setSessione(true);
		}
		// preleva il size per vedere se mettere i bottoni di scorrimento
		// utilizza la variabile posizione con elemento dell'arraylist in esamina
		// esamina vede un elemento alla volta


//		ListaSuppCambioVO listaRicercati =(ListaSuppCambioVO)request.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
//		listaRicercati.setSelezioniChiamato(currentForm.getListaDaScorrere());


		try {

			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				currentForm.setEsaminaInibito(true);
				currentForm.setDisabilitaTutto(true);
			}


			currentForm.setListaDaScorrere((List<ListaSuppCambioVO>) request.getSession().getAttribute("criteriRicercaCambio"));
			if(currentForm.getListaDaScorrere() != null && currentForm.getListaDaScorrere().size()!=0)
			{
				if(currentForm.getListaDaScorrere().size()>1 )
				{
					currentForm.setEnableScorrimento(true);
					//currentForm.setPosizioneScorrimento(0);
				}
				else
				{
					currentForm.setEnableScorrimento(false);
				}

				//this.loadAppo(resultckeck);
			}
			currentForm.setPosizioneScorrimento(0);
			// richiamo ricerca su db con elemento 1 di ricerca
			Boolean caricamentoIniziale=false;
			int passaggioDiCaricamentoDati=0;
			if (!currentForm.isSubmitDinamico())
			{
				this.loadDatiCambioPassato( currentForm,currentForm.getListaDaScorrere().get(0));
				passaggioDiCaricamentoDati=passaggioDiCaricamentoDati+1;
				currentForm.setSubmitDinamico(true);
			}
			this.loadValuta( currentForm);
			// ????
			currentForm.setValuta(currentForm.getDatiCambio().getCodValuta());

/*			currentForm.setValRifer(false);
			if (currentForm.getDatiCambio()!=null && currentForm.getDatiCambio().isValRifer())
			{
				currentForm.setValRifer(true);
			}
*/			// configurazione dei check
			if (currentForm.getDatiCambio()!=null && currentForm.getDatiCambio().isValRifer())
			{
				currentForm.setValRifer(true);
				currentForm.setValRiferChk("on");
			}
			else
			{
				currentForm.setValRifer(false);
				currentForm.setValRiferChk(null);
			}
			if (passaggioDiCaricamentoDati==0)
			{
				Boolean chk=false;
				if (request.getParameter("valRiferChk") != null  )
				{
					chk = true;
				}

				if (chk)
				{
					currentForm.setValRifer(true);
					currentForm.setValRiferChk("on");
					currentForm.getDatiCambio().setValRifer(true);
				}
				else
				{
					currentForm.setValRifer(false);
					currentForm.setValRiferChk(null);
					currentForm.getDatiCambio().setValRifer(false);
				}
			}


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

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.erroreScorriAvanti"));


			}
		else
			{
				try
				{
				  this.loadDatiCambioPassato( currentForm,currentForm.getListaDaScorrere().get(attualePosizione));
				} catch (ValidationException ve) {
					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
					// assenzaRisultati = 4001;
					if (ve.getError()==4001)
					{
						//
						//LinkableTagUtils.addError(request, new ActionMessage(
						//		"errors.acquisizioni.erroreScorriCancellato"));
						//

						// passa avanti perchè l'elemento è stato cancellato
						currentForm.setPosizioneScorrimento(attualePosizione);
						return scorriAvanti( mapping,  form,  request,  response);
					}
					return mapping.getInputForward();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		// richiamo ricerca su db con elemento 1 di ricerca
		//return mapping.findForward("scorriAvanti");

		//this.loadDatiCambioPassato( esaCambio,currentForm.getListaDaScorrere().get(attualePosizione));
		currentForm.setPosizioneScorrimento(attualePosizione);
		currentForm.setDisabilitaTutto(false);
		if (currentForm.isEsaminaInibito())
		{
			currentForm.setDisabilitaTutto(true);
		}
		currentForm.setValRifer(false);
		if (currentForm.getDatiCambio()!=null && currentForm.getDatiCambio().isValRifer())
		{
			currentForm.setValRifer(true);
		}

		return mapping.getInputForward();


		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));


				}
			else
				{
				try
				{
				  this.loadDatiCambioPassato( currentForm,currentForm.getListaDaScorrere().get(attualePosizione));
				} catch (ValidationException ve) {
					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
					// assenzaRisultati = 4001;
					if (ve.getError()==4001)
					{
						//
						//LinkableTagUtils.addError(request, new ActionMessage(
						//		"errors.acquisizioni.erroreScorriCancellato"));
						//
						// passa indietro perchè l'elemento è stato cancellato
						currentForm.setPosizioneScorrimento(attualePosizione);
						return scorriIndietro( mapping,  form,  request,  response);
					}
					return mapping.getInputForward();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				//this.loadDatiCambioPassato( esaCambio,currentForm.getListaDaScorrere().get(attualePosizione));
				currentForm.setPosizioneScorrimento(attualePosizione);
				currentForm.setDisabilitaTutto(false);
				if (currentForm.isEsaminaInibito())
				{
					currentForm.setDisabilitaTutto(true);
				}
				currentForm.setValRifer(false);
				if (currentForm.getDatiCambio()!=null && currentForm.getDatiCambio().isValRifer())
				{
					currentForm.setValRifer(true);
				}

				}
		return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		try {
/*			if (!isTokenValid(request)) {
				saveToken(request);
				if(!currentForm.isSessione())
				{
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//	this.checkForm(request);
		resetToken(request);	*/
			String amount=currentForm.getDatiCambio().getTassoCambioStr().trim();
		    Double risult=Pulisci.ControllaImportoCambio(amount);
		    currentForm.getDatiCambio().setTassoCambio(risult);
		    // refresh del campo (da spostare nella form????? validate)
		    // serve per troncare eventuali cifre decimali immesse oltre la seconda
		    currentForm.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio(currentForm.getDatiCambio().getTassoCambio()));

			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaCambioVO(currentForm.getDatiCambio());
			// fine validazione


			currentForm.setConferma(true);
			currentForm.setPressioneBottone("salva");
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		//return mapping.findForward("salva");
		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			return mapping.getInputForward();

		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		try {
			currentForm.setDisabilitaTutto(false);
			currentForm.setConferma(false);
			if (currentForm.getPressioneBottone().equals("salva")) {
				currentForm.setPressioneBottone("");

				//meglio sostituire le istruzioni di sopra con
				CambioVO eleCambio=currentForm.getDatiCambio();

				String amount=currentForm.getDatiCambio().getTassoCambioStr().trim();
			    Double risult=Pulisci.ControllaImportoCambio(amount);
			    currentForm.getDatiCambio().setTassoCambio(risult);
			    // refresh del campo (da spostare nella form????? validate)
			    // serve per troncare eventuali cifre decimali immesse oltre la seconda
			    currentForm.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio(currentForm.getDatiCambio().getTassoCambio()));


				ListaSuppCambioVO attrLS=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
				ListaSuppCambioVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleCambio, attrLS);
				request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI,attrLSagg );

				//request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI, (ListaSuppCambioVO) this.AggiornaTipoVarRisultatiListaSupporto(eleCambio, (ListaSuppCambioVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI)));
				eleCambio.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				if (currentForm.getValRiferChk()!=null &&  currentForm.getValRiferChk().equals("on"))
				{
					currentForm.getDatiCambio().setValRifer(true);
				}
				else
				{
					currentForm.getDatiCambio().setValRifer(false);
				}


				if (!this.modificaCambio(eleCambio)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreModifica"));

					//return mapping.getInputForward();
				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.modificaOK"));

					// aggiornamento data di modifica in layout
					Date dataodierna=new Date();
					String dataOdiernaStr=DateFormat.getDateInstance(DateFormat.SHORT).format(dataodierna);
					currentForm.getDatiCambio().setDataVariazione(dataOdiernaStr);

					//currentForm.setConferma(false);
					return ripristina( mapping,  form,  request,  response);

				}
			}
			if (currentForm.getPressioneBottone().equals("cancella")) {
				currentForm.setPressioneBottone("");

				//meglio sostituire le istruzioni di sopra con
				CambioVO eleCambio=currentForm.getDatiCambio();
				eleCambio.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
				String amount=currentForm.getDatiCambio().getTassoCambioStr().trim();
			    Double risult=Pulisci.ControllaImportoCambio(amount);
			    currentForm.getDatiCambio().setTassoCambio(risult);
			    // refresh del campo (da spostare nella form????? validate)
			    // serve per troncare eventuali cifre decimali immesse oltre la seconda
			    currentForm.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio(currentForm.getDatiCambio().getTassoCambio()));

				if (!this.cancellaCambio(eleCambio)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreCancella"));

					//return mapping.getInputForward();
				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.cancellaOK"));

					currentForm.setDisabilitaTutto(true);
					return Navigation.getInstance(request).goBack();
				}

			}
			currentForm.setConferma(false);
			return mapping.getInputForward();

		//return mapping.findForward("salva");
		}	catch (ValidationException e) {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setPressioneBottone("");
			//almaviva5_20140617 #5078
			if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));

			return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setPressioneBottone("");

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		currentForm.setPressioneBottone("");
		return mapping.getInputForward();
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		try {
/*			if (!isTokenValid(request)) {
				saveToken(request);
				if(!currentForm.isSessione())
				{
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//	this.checkForm(request);
		resetToken(request);
*/
		// ripristino del valore iniziale
		this.loadDatiCambioPassato( currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
		return mapping.getInputForward();
		//return mapping.findForward("ripristina");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaCambioForm currentForm = (EsaminaCambioForm) form;
		try {
			currentForm.setConferma(true);
			currentForm.setPressioneBottone("cancella");
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		//return mapping.findForward("cancella");
		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private ListaSuppCambioVO AggiornaTipoVarRisultatiListaSupporto (CambioVO eleCambio, ListaSuppCambioVO attributo)
	{
		try {
			if (eleCambio !=null)
			{
			List<CambioVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getCodValuta();
					if (eleRis.equals(eleCambio.getCodValuta()))
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
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_CAMBI, utente.getCodPolo(), utente.getCodBib(), null);
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
