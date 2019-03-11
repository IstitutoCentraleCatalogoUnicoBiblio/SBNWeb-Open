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
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.cambi.InserisciCambioForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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


public class InserisciCambioAction extends LookupDispatchAction {

	private void loadDatiCambio(InserisciCambioForm insCambio) throws Exception {
//		 (String codP, String codB, String codVal, String desVal , double tasso, String dataVar)
		// ASSEGNAZIONE DELLA data di sistema
		Date dataodierna=new Date();
		String dataOdiernaStr=DateFormat.getDateInstance(DateFormat.SHORT).format(dataodierna);
		CambioVO camb=new CambioVO("", "", "", "",0, dataOdiernaStr,"I");
		insCambio.setDatiCambio(camb);
		insCambio.getDatiCambio().setTassoCambioStr("0,00");

	}

	private void loadValuta(InserisciCambioForm currentForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();

		currentForm.setListaValuta(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta()));

	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "si");
        map.put("servizi.bottone.no", "no");
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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_CAMBI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		UserVO utente = navi.getUtente();
		String biblio=utente.getCodBib();
		String polo=utente.getCodPolo();

		if(!currentForm.isSessione()) {
			currentForm.setSessione(true);
			this.loadDatiCambio(currentForm);
			//almaviva5_20140612 #5078
			AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
			CambioVO valRif = delegate.getValutaRiferimento(polo, biblio);
			boolean forzaValutaRif = (valRif == null);
			currentForm.setForzaValutaRif(forzaValutaRif);
			if (forzaValutaRif) {
				currentForm.setValRiferChk("on");
				CambioVO cambio = currentForm.getDatiCambio();
				cambio.setDataVariazione("31/12/9999");
				cambio.setValRifer(true);
				cambio.setTassoCambioStr("1,00");
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.cambi.inserireValutaRiferimento"));
			}
		}

		int passaggioDiCaricamentoDati=0;
		if (!currentForm.isSubmitDinamico())
		{
			passaggioDiCaricamentoDati=passaggioDiCaricamentoDati+1;
			currentForm.setSubmitDinamico(true);
		}

			if (biblio!=null && currentForm.getDatiCambio()!=null && (currentForm.getDatiCambio().getCodBibl()==null || (currentForm.getDatiCambio().getCodBibl()!=null && currentForm.getDatiCambio().getCodBibl().trim().length()==0)))
		{
			currentForm.getDatiCambio().setCodBibl(biblio);
		}

		BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
		if (bibScelta!=null && bibScelta.getCod_bib()!=null)
		{
			currentForm.getDatiCambio().setCodBibl(bibScelta.getCod_bib());
		}

		currentForm.getDatiCambio().setCodPolo(polo);

		// preimpostazione della schermata di inserimento con i valori ricercati
		if (request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI)!=null)
		{
			ListaSuppCambioVO ele=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
			if (ele.getCodValuta()!=null && ele.getCodValuta().trim().length()>0 )
			{
				currentForm.getDatiCambio().setCodValuta(ele.getCodValuta());
			}
			if (ele.getDesValuta()!=null && ele.getDesValuta().trim().length()>0 )
			{
				currentForm.getDatiCambio().setDesValuta(ele.getDesValuta());;
			}
//			 MANCA GESTIONE DI RIGA ORDINE
		}


		this.loadValuta( currentForm);
		// configurazione dei check
		if ( currentForm.getDatiCambio().isValRifer())
		{
			currentForm.setValRiferChk("on");
			currentForm.setValRifer(true);
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
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		try {
			// validazione
		    String amount=currentForm.getDatiCambio().getTassoCambioStr().trim();
		    Double risult=Pulisci.ControllaImportoCambio(amount);
		    currentForm.getDatiCambio().setTassoCambio(risult);
		    // refresh del campo (da spostare nella form????? validate)
		    // serve per troncare eventuali cifre decimali immesse oltre la seconda
		    currentForm.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio(currentForm.getDatiCambio().getTassoCambio()));

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaCambioVO(currentForm.getDatiCambio());
			// fine validazione


			currentForm.setConferma(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
			// altri tipi di errore
		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			currentForm.setConferma(false);
			return mapping.getInputForward();
		}

	}

	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		try {
			CambioVO cambio = currentForm.getDatiCambio();
			cambio.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
		    String amount=cambio.getTassoCambioStr().trim();
		    Double risult=Pulisci.ControllaImportoCambio(amount);
		    cambio.setTassoCambio(risult);
		    // refresh del campo (da spostare nella form????? validate)
		    // serve per troncare eventuali cifre decimali immesse oltre la seconda
		    cambio.setTassoCambioStr(Pulisci.VisualizzaImportoCambio(cambio.getTassoCambio()));
			// se il codice ordine è già valorizzato si deve procedere alla modifica
/*		    if (insCambio.isCambiaValuta()) // è nell'onchange della combo delle valute
		    {
		    	insCambio.getDatiCambio().setIDCambio(0);
		    }
*/

			if (currentForm.getValRiferChk()!=null &&  currentForm.getValRiferChk().equals("on"))
			{
				cambio.setValRifer(true);
			}
			else
			{
				cambio.setValRifer(false);
			}


		    // SI ATTIVA LA MODIFICA SOLO SE SI TRATTA LO STESSO OGGETTO
			currentForm.setConferma(false);

		    if (cambio.getIDCambio()!=0)
			{
				if (!this.modificaCambio(cambio)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreModifica"));

					return mapping.getInputForward();
				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.modificaOK"));

					return ripristina( mapping,  form,  request,  response);
				}

			}
			else
			{
				if (this.inserisciCambio(cambio))

				{
					// rilettura dell'oggetto per acquisire l'ID
					//this.loadDatiCambioINS( request, insCambio);

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.inserimentoOK"));

					// memorizzazione dell'ultimo cambio inserito per consentire ulteriori inserimenti e/o modifiche
					//insCambio.setValutaInserita(insCambio.getDatiCambio().getCodValuta());
					return ripristina( mapping,  form,  request,  response);

				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.erroreinserimento"));

				}
			}



			return mapping.getInputForward();
			// gestione errore ValidationException invocati da ebj
			}	catch (ValidationException ve) {

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

						currentForm.setConferma(false);
						if (ve.getError()==4259)
						{
						  currentForm.setValRifer(false); //
						}
						return mapping.getInputForward();
				}
				// altri tipi di errore
				catch (Exception e) {

					//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

					currentForm.setConferma(false);
					return mapping.getInputForward();
				}

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		try {

			CambioVO eleCambio=currentForm.getDatiCambio();
			if ( eleCambio!=null && eleCambio.getCodValuta()!=null && eleCambio.getCodValuta().trim().length()>0)
			{
				// il record è già esistente
				// lettura
				CambioVO eleCambioLetto=this.loadDatiINS(eleCambio);

				if (eleCambioLetto!=null )
				{
					currentForm.setDatiCambio(eleCambioLetto);
				}
				//insSezione.setSezioneInserita(insSezione.getSezione().getCodiceSezione());
				currentForm.setValRifer(false);
				if (currentForm.isValRifer()) // è nell'onchange della combo delle valute
			    {
			    	currentForm.getDatiCambio().setValRifer(true);
			    }

			}
			else
			{

				this.loadDatiCambio( currentForm);
				if (eleCambio!=null )
				{
					if (eleCambio.getCodBibl()!=null &&  eleCambio.getCodBibl().trim().length()>0)
					{
						currentForm.getDatiCambio().setCodBibl(eleCambio.getCodBibl());
					}
					if (eleCambio.getCodPolo()!=null &&  eleCambio.getCodPolo().trim().length()>0)
					{
						currentForm.getDatiCambio().setCodPolo(eleCambio.getCodPolo());
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
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!currentForm.isSessione())
				{
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//	this.checkForm(request);
		resetToken(request);
		return mapping.findForward("cancella");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciCambioForm currentForm = (InserisciCambioForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!currentForm.isSessione())
				{
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//	this.checkForm(request);
		resetToken(request);
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private boolean inserisciCambio(CambioVO cambio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciCambioHib(cambio);
		return valRitorno;
	}

	private boolean modificaCambio(CambioVO cambio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaCambioHib(cambio);
		return valRitorno;
	}

	private void loadDatiCambioINS(HttpServletRequest request,InserisciCambioForm insCambio) throws Exception {

		ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
		String chiama=null;
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		//String codB=Navigation.getInstance(request).getUtente().getCodBib();
		String codB=insCambio.getDatiCambio().getCodBibl();
		String codVal=insCambio.getDatiCambio().getCodValuta();
		String desVal=""; // insCambio.getDatiCambio().getDesValuta();
		eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
		eleRicerca.setElemXBlocchi(999);
		eleRicerca.setOrdinamento("");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<CambioVO> cambioTrovato  = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(eleRicerca);

		//gestire l'esistenza del risultato e che sia univoco
		insCambio.setDatiCambio(cambioTrovato.get(0));
		try {
			insCambio.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio( insCambio.getDatiCambio().getTassoCambio()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			insCambio.getDatiCambio().setTassoCambioStr("0,00");
		}
	}

	private CambioVO loadDatiINS(CambioVO ele) throws Exception {

		CambioVO eleLetto =null;

		ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		String codVal=ele.getCodValuta();
		String desVal=""; // insCambio.getDatiCambio().getDesValuta();
		String chiama=null;
		eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
		eleRicerca.setOrdinamento("");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<CambioVO> cambioTrovato  = new ArrayList();

		//gestire l'esistenza del risultato e che sia univoco
		try {
			cambioTrovato = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(eleRicerca);;
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (cambioTrovato.size()>0)
		{
			eleLetto=cambioTrovato.get(0);

		}
		try {
			eleLetto.setTassoCambioStr(Pulisci.VisualizzaImportoCambio( eleLetto.getTassoCambio()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			eleLetto.setTassoCambioStr("0,00");
		}

		return eleLetto;

	}

}
