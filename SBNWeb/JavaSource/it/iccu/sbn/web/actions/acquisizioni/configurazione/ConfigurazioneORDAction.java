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
package it.iccu.sbn.web.actions.acquisizioni.configurazione;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.configurazione.ConfigurazioneORDForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
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

public class ConfigurazioneORDAction extends AcquisizioniBaseAction implements SbnAttivitaChecker{

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("ricerca.label.sezione", "sezioneCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ordine.label.fornitore0", "fornitoreCerca0");
		map.put("ordine.label.fornitore1", "fornitoreCerca1");
		map.put("ordine.label.fornitore2", "fornitoreCerca2");
		map.put("ordine.label.fornitore3", "fornitoreCerca3");
		map.put("ordine.label.fornitore4", "fornitoreCerca4");
		map.put("ordine.label.fornitore5", "fornitoreCerca5");
		map.put("ordine.label.bilancio", "bilancioCerca");
		// map.put("button.crea.profiliAcquisto","profiloCerca");
		map.put("ricerca.button.salva", "salva");
		map.put("ricerca.button.indietro", "indietro");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("servizi.bottone.si", "Si");
		map.put("servizi.bottone.no", "No");
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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_PARAMETRI_BIBLIOTECA, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	// UNSPECIFIED
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;
		if (Navigation.getInstance(request).isFromBar() )
		{
			// configurazione dei check
			if (currentForm.getDatiConfigORD().isGestioneBilancio())
			{
				currentForm.setGestBil("on");
				currentForm.setEnableBil(true);
			}
			else
			{
				currentForm.setGestBil(null);
				currentForm.setEnableBil(false);
				if (currentForm.getDatiConfigORD()!=null && currentForm.getDatiConfigORD().getChiaveBilancio()!=null && currentForm.getDatiConfigORD().getChiaveBilancio().getCodice1()!=null && currentForm.getDatiConfigORD().getChiaveBilancio().getCodice2()!=null  )
				{
					currentForm.getDatiConfigORD().getChiaveBilancio().setCodice1("");
					currentForm.getDatiConfigORD().getChiaveBilancio().setCodice2("");
				}
			}

			if (currentForm.getDatiConfigORD().isGestioneProfilo())
			{
				currentForm.setGestProf("on");
				currentForm.setEnableProf(true);

			}
			else
			{
				currentForm.setGestProf(null);
				currentForm.setEnableProf(false);

			}

			if (currentForm.getDatiConfigORD().isGestioneSezione())
			{
				currentForm.setGestSez("on");
				currentForm.setEnableSez(true);
				if (currentForm.getDatiConfigORD()!=null && currentForm.getDatiConfigORD().getCodiceSezione()!=null  )
				{
					currentForm.getDatiConfigORD().setCodiceSezione("");
				}
			}
			else
			{
				currentForm.setGestSez(null);
				currentForm.setEnableSez(false);

			}

			if (currentForm.getDatiConfigORD().isOrdiniAperti())
			{
				currentForm.setOrdAperti("on");
			}
			else
			{
				currentForm.setOrdAperti(null);
			}

			if (currentForm.getDatiConfigORD().isOrdiniChiusi())
			{
				currentForm.setOrdChiusi("on");
			}
			else
			{
				currentForm.setOrdChiusi(null);
			}

			if (currentForm.getDatiConfigORD().isOrdiniAnnullati())
			{
				currentForm.setOrdAnnullati("on");
			}
			else
			{
				currentForm.setOrdAnnullati(null);
			}

			return mapping.getInputForward();
		}
		if(!currentForm.isSessione())
		{
			currentForm.setSessione(true);
		}

		//this.loadPagina(request);

		//this.loadDatiConfigurazioneBO();
		// PRECARICAMENTO INIZIALE
		if (!currentForm.isCaricamentoIniziale()) {

			ConfigurazioneORDVO configORDCaricato;
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			String deno=Navigation.getInstance(request).getUtente().getBiblioteca();

			configORDCaricato=loadConfigORD();

			if (biblio!=null && (configORDCaricato.getCodBibl()==null || (configORDCaricato.getCodBibl()!=null && configORDCaricato.getCodBibl().trim().length()==0)))
			{

				configORDCaricato.setCodBibl(biblio);
				configORDCaricato.setDenoBibl(deno);
			}
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();

			if (polo!=null && configORDCaricato.getCodPolo()!=null && configORDCaricato.getCodPolo().trim().length()==0)
			{
				configORDCaricato.setCodPolo(polo);
			}
			configORDCaricato.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				configORDCaricato.setCodBibl(bibScelta.getCod_bib());
				configORDCaricato.setDenoBibl(bibScelta.getNom_biblioteca());

			}
			configORDCaricato.setTicket(ticket);
			ConfigurazioneORDVO confLetto=this.loadConfigurazione(configORDCaricato);

			// assenza di configurazione ordine
			if (confLetto==null || ( confLetto!=null && (confLetto.getCodBibl()==null || (confLetto.getCodBibl()!=null && confLetto.getCodBibl().trim().length()==0 ))))
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.configurazioneAssente"));
				this.saveErrors(request, errors);
			}


			if (confLetto!=null)
			{
				configORDCaricato=confLetto;
			}


			if (configORDCaricato.isGestioneBilancio())
			{
				currentForm.setGestBil("on");
				currentForm.setEnableBil(true);
			}
			else
			{
				currentForm.setGestBil(null);
				currentForm.setEnableBil(false);
				if (currentForm.getDatiConfigORD()!=null && currentForm.getDatiConfigORD().getChiaveBilancio()!=null && currentForm.getDatiConfigORD().getChiaveBilancio().getCodice1()!=null && currentForm.getDatiConfigORD().getChiaveBilancio().getCodice2()!=null  )
				{
					currentForm.getDatiConfigORD().getChiaveBilancio().setCodice1("");
					currentForm.getDatiConfigORD().getChiaveBilancio().setCodice2("");
				}

			}

			if (configORDCaricato.isGestioneProfilo())
			{
				currentForm.setGestProf("on");
				currentForm.setEnableProf(true);

			}
			else
			{
				currentForm.setGestProf(null);
				currentForm.setEnableProf(false);

			}

			if (configORDCaricato.isGestioneSezione())
			{
				currentForm.setGestSez("on");
				currentForm.setEnableSez(true);

			}
			else
			{
				currentForm.setGestSez(null);
				currentForm.setEnableSez(false);
				if (currentForm.getDatiConfigORD()!=null && currentForm.getDatiConfigORD().getCodiceSezione()!=null  )
				{
					currentForm.getDatiConfigORD().setCodiceSezione("");
				}

			}

			if (configORDCaricato.isOrdiniAperti())
			{
				currentForm.setOrdAperti("on");
			}
			else
			{
				currentForm.setOrdAperti(null);
			}

			if (configORDCaricato.isOrdiniChiusi())
			{
				currentForm.setOrdChiusi("on");
			}
			else
			{
				currentForm.setOrdChiusi(null);
			}

			if (configORDCaricato.isOrdiniAnnullati())
			{
				currentForm.setOrdAnnullati("on");
			}
			else
			{
				currentForm.setOrdAnnullati(null);
			}

			//almaviva5_20121205 evolutive google
			Integer cd_forn_google = configORDCaricato.getCd_forn_google();
			String codFornitore = ValidazioneDati.isFilled(cd_forn_google) ? String.valueOf(cd_forn_google) : "";
			currentForm.setCodFornitore(codFornitore);
			if (ValidazioneDati.isFilled(codFornitore))
				fornitoreCercaVeloce(mapping, request, currentForm);

			currentForm.setDatiConfigORD(configORDCaricato);
			currentForm.setCaricamentoIniziale(true);
		}
		else  // ricConfigurazioneORD.isCaricamentoIniziale()
		{
			// operazione da evitare quando non si è modificato alcun check
			if (!currentForm.isSubmitDinamico())
			{
				Boolean bil=false;
				if (request.getParameter("gestBil") != null  )
				{
					bil = true;
				}

				if (bil)
				{
					currentForm.setGestBil("on");
				}
				else
				{
					currentForm.setGestBil(null);
					if (currentForm.getDatiConfigORD()!=null && currentForm.getDatiConfigORD().getChiaveBilancio()!=null && currentForm.getDatiConfigORD().getChiaveBilancio().getCodice1()!=null && currentForm.getDatiConfigORD().getChiaveBilancio().getCodice2()!=null  )
					{
						currentForm.getDatiConfigORD().getChiaveBilancio().setCodice1("");
						currentForm.getDatiConfigORD().getChiaveBilancio().setCodice2("");
					}
				}
				currentForm.getDatiConfigORD().setGestioneBilancio(bil);
				currentForm.setEnableBil(bil);


				Boolean prof=false;
				if (request.getParameter("gestProf") != null ) {
					prof = true;
				}

				Boolean sez=false;
				if (request.getParameter("gestSez") != null) {
					sez = true;
				}

				currentForm.getDatiConfigORD().setGestioneProfilo(prof);
				currentForm.setEnableProf(prof);
				//ricConfigurazioneORD.setGestProf(request.getParameter("gestProf"));
				if (prof)
				{
					if (sez)
					{
						currentForm.setGestProf("on");
					}
					else
					{
						currentForm.setGestProf(null);
					}

				}
				else
				{
					currentForm.setGestProf(null);
				}

				currentForm.getDatiConfigORD().setGestioneSezione(sez);
				currentForm.setEnableSez(sez);
				//ricConfigurazioneORD.setGestSez(request.getParameter("gestSez"));
				if (sez)
				{
					currentForm.setGestSez("on");
				}
				else
				{
					currentForm.setGestSez(null);
					currentForm.setGestProf(null);
					currentForm.setEnableProf(false);
					currentForm.getDatiConfigORD().setGestioneProfilo(false);

					if (currentForm.getDatiConfigORD()!=null && currentForm.getDatiConfigORD().getCodiceSezione()!=null  )
					{
						currentForm.getDatiConfigORD().setCodiceSezione("");
					}
				}

				Boolean apert=false;
				if (request.getParameter("ordAperti") != null  ) {
					apert = true;
				}

				currentForm.getDatiConfigORD().setOrdiniAperti(apert);
				//ricConfigurazioneORD.setOrdAperti(request.getParameter("ordAperti"));
				if (apert)
				{
					currentForm.setOrdAperti("on");
				}
				else
				{
					currentForm.setOrdAperti(null);
				}

				Boolean chius=false;
				if (request.getParameter("ordChiusi") != null ) {
					chius = true;
				}

				currentForm.getDatiConfigORD().setOrdiniChiusi(chius);
				//ricConfigurazioneORD.setOrdChiusi(request.getParameter("ordChiusi"));
				if (chius)
				{
					currentForm.setOrdChiusi("on");
				}
				else
				{
					currentForm.setOrdChiusi(null);
				}

				Boolean annullat=false;
				if (request.getParameter("ordAnnullati") != null ) {
					annullat = true;
				}
				currentForm.getDatiConfigORD().setOrdiniAnnullati(annullat);
				if (annullat)
				{
					currentForm.setOrdAnnullati("on");
				}
				else
				{
					currentForm.setOrdAnnullati(null);
				}

			}

		}

		this.loadTipoImpegno( currentForm);
		this.loadAllineamento( currentForm);
/*
		//controllo se ho un risultato di una lista di supporto PROFILI richiamata da questa pagina (risultato della simulazione)
		ListaSuppProfiloVO ricProf=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
		if (ricProf!=null && ricProf.getChiamante()!=null && ricProf.getChiamante().equals(mapping.getPath()))
			{
			if (ricProf!=null && ricProf.getSelezioniChiamato()!=null && ricProf.getSelezioniChiamato().size()!=0 )
			{
				if (ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice()!=null && ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice().length()!=0 )
				{
					ricConfigurazioneORD.getDatiConfigORD().setCodiceProfilo(ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice());
					ricConfigurazioneORD.setProfAcqFornDes(ricProf.getSelezioniChiamato().get(0).getProfilo().getDescrizione());
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				ricConfigurazioneORD.getDatiConfigORD().setCodiceProfilo("");
				ricConfigurazioneORD.setProfAcqFornDes("");
			}

			// il reset dell'attributo di sessione deve avvenire solo dal chiamante
			request.getSession().removeAttribute("attributeListaSuppProfiloVO");
			request.getSession().removeAttribute("profiliSelected");
			request.getSession().removeAttribute("criteriRicercaProfilo");

			}

		*/
		//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
		ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
		if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath())) {
			List<FornitoreVO> ff = ricForn.getSelezioniChiamato();
			if (ValidazioneDati.size(ff) == 1) {
				FornitoreVO f = ValidazioneDati.first(ff);
				currentForm.setCodFornitore(f.getCodFornitore());
				currentForm.setFornitore(f.getNomeFornitore());
			}
			/*
			if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
			{
				if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
				{
					// nel codice ci va A,L,V,D,R,
					currentForm.getDatiConfigORD().getFornitoriDefault()[currentForm.getRadioForn()].setDescrizione(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
					currentForm.setFornitoreDes(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				currentForm.getDatiConfigORD().getFornitoriDefault()[currentForm.getRadioForn()].setCodice("");
				currentForm.setFornitoreDes("");
			}
			*/
			// il reset dell'attributo di sessione deve avvenire solo dal chiamante
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

			}
		//controllo se ho un risultato di una lista di supporto BILANCIO richiamata da questa pagina (risultato della simulazione)
		ListaSuppBilancioVO ricBil=(ListaSuppBilancioVO)request.getSession().getAttribute("attributeListaSuppBilancioVO");
		if (ricBil!=null && ricBil.getChiamante()!=null && ricBil.getChiamante().equals(mapping.getPath()))
			{
			if (ricBil!=null && ricBil.getSelezioniChiamato()!=null && ricBil.getSelezioniChiamato().size()!=0 )
			{
				if (ricBil.getSelezioniChiamato().get(0).getChiave()!=null && ricBil.getSelezioniChiamato().get(0).getChiave().length()!=0 )
				{
					currentForm.getDatiConfigORD().getChiaveBilancio().setCodice1(ricBil.getSelezioniChiamato().get(0).getEsercizio());
					currentForm.getDatiConfigORD().getChiaveBilancio().setCodice2(ricBil.getSelezioniChiamato().get(0).getCapitolo());
					//this.ricOrdini.setTipoImpegno(ricBil.getSelezioniChiamato().get(0).getImpegno());
					// disabilito il tipo impegno
					//ricConfigurazioneORD.getDatiConfigORD().getChiaveBilancio().setCodice3(ricBil.getSelezioniChiamato().get(0).getSelezioneImp());

				}
			}

			else
			{
				// pulizia della maschera di ricerca
				currentForm.getDatiConfigORD().getChiaveBilancio().setCodice1("");
				currentForm.getDatiConfigORD().getChiaveBilancio().setCodice2("");
				// disabilito il tipo impegno
				// ricConfigurazioneORD.getDatiConfigORD().getChiaveBilancio().setCodice3("");
			}

			// il reset dell'attributo di sessione deve avvenire solo dal chiamante
			request.getSession().removeAttribute("attributeListaSuppBilancioVO");
			request.getSession().removeAttribute("bilanciSelected");
			request.getSession().removeAttribute("criteriRicercaBilancio");

			}
		//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
		ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
		if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
			{
			if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
			{
				if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
				{
					currentForm.getDatiConfigORD().setCodiceSezione(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				currentForm.getDatiConfigORD().setCodiceSezione("");
			}

			// il reset dell'attributo di sessione deve avvenire solo dal chiamante
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

			}

		// lettura dei boolean impostati lato client sulla pagina ed impostazione delle variabili dell'oggetto da passare
		// SOLO DOPO IL PRECARICAMENTO INIZIALE


		// ripristino variabile dinamica jsp
		if (currentForm.isSubmitDinamico())
		{
			currentForm.setSubmitDinamico(false);
		}

		return mapping.getInputForward();
	}



	// SALVA
	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;
		try {
			//almaviva5_20121205 evolutive google
			String codFornitore = currentForm.getCodFornitore();
			if (ValidazioneDati.isFilled(codFornitore)) {
				ActionForward fwd = fornitoreCercaVeloce(mapping, request, currentForm);
				if (fwd == null)
					return mapping.getInputForward();
			}

			if (ValidazioneDati.strIsNumeric(codFornitore))
				currentForm.getDatiConfigORD().setCd_forn_google(Integer.valueOf(codFornitore));
			else
				currentForm.getDatiConfigORD().setCd_forn_google(null);

			currentForm.setConferma(true);
			currentForm.setDisabilitaTutto(true);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

			return mapping.getInputForward();
	 	} catch (Exception e) {
			e.printStackTrace();
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	// INDIETRO
	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;
		try {
			ConfigurazioneORDVO configORDCaricato;
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			configORDCaricato=loadConfigORD();
			configORDCaricato.setCodBibl(biblio);
			configORDCaricato.setTicket(ticket);
			ConfigurazioneORDVO confLetto=this.loadConfigurazione(configORDCaricato);
			if (confLetto!=null)
			{
				configORDCaricato=confLetto;
			}
			if (configORDCaricato.isGestioneBilancio())
			{
				currentForm.setGestBil("on");
				currentForm.setEnableBil(true);
			}
			else
			{
				currentForm.setGestBil(null);
				currentForm.setEnableBil(false);

			}

			if (configORDCaricato.isGestioneProfilo())
			{
				currentForm.setGestProf("on");
				currentForm.setEnableProf(true);

			}
			else
			{
				currentForm.setGestProf(null);
				currentForm.setEnableProf(false);

			}

			if (configORDCaricato.isGestioneSezione())
			{
				currentForm.setGestSez("on");
				currentForm.setEnableSez(true);

			}
			else
			{
				currentForm.setGestSez(null);
				currentForm.setEnableSez(false);

			}

			if (configORDCaricato.isOrdiniAperti())
			{
				currentForm.setOrdAperti("on");
			}
			else
			{
				currentForm.setOrdAperti(null);
			}

			if (configORDCaricato.isOrdiniChiusi())
			{
				currentForm.setOrdChiusi("on");
			}
			else
			{
				currentForm.setOrdChiusi(null);
			}

			if (configORDCaricato.isOrdiniAnnullati())
			{
				currentForm.setOrdAnnullati("on");
			}
			else
			{
				currentForm.setOrdAnnullati(null);
			}
			currentForm.setDatiConfigORD(configORDCaricato);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;

		try {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);

			// lettura dei boolean impostati lato client sulla pagina ed impostazione delle variabili dell'oggetto da passare
			// lettura dei boolean impostati lato client sulla pagina ed impostazione delle variabili dell'oggetto da passare
/*			Boolean bil=false;
			if (request.getParameter("gestBil") != null) {
				bil = true;
			}
			ricConfigurazioneORD.getDatiConfigORD().setGestioneBilancio(bil);
			ricConfigurazioneORD.setEnableBil(bil);
			if (bil)
			{
				ricConfigurazioneORD.setGestBil("on");
			}
			else
			{
				ricConfigurazioneORD.setGestBil(null);
			}

			Boolean sez=false;
			if (request.getParameter("gestSez") != null) {
				sez = true;
			}

			ricConfigurazioneORD.getDatiConfigORD().setGestioneSezione(sez);
			ricConfigurazioneORD.setEnableSez(sez);
			//ricConfigurazioneORD.setGestSez(request.getParameter("gestSez"));
			if (sez)
			{
				ricConfigurazioneORD.setGestSez("on");
			}
			else
			{
				ricConfigurazioneORD.setGestSez(null);
			}

			Boolean prof=false;
			if (request.getParameter("gestProf") != null) {
				prof = true;
			}

			ricConfigurazioneORD.getDatiConfigORD().setGestioneProfilo(prof);
			ricConfigurazioneORD.setEnableProf(prof);
			//ricConfigurazioneORD.setGestProf(request.getParameter("gestProf"));
			if (prof)
			{
				ricConfigurazioneORD.setGestProf("on");
			}
			else
			{
				ricConfigurazioneORD.setGestProf(null);
			}


			Boolean apert=false;
			if (request.getParameter("ordAperti") != null) {
				apert = true;
			}

			ricConfigurazioneORD.getDatiConfigORD().setOrdiniAperti(apert);
			//ricConfigurazioneORD.setOrdAperti(request.getParameter("ordAperti"));
			if (apert)
			{
				ricConfigurazioneORD.setOrdAperti("on");
			}
			else
			{
				ricConfigurazioneORD.setOrdAperti(null);
			}

			Boolean chius=false;
			if (request.getParameter("ordChiusi") != null) {
				chius = true;
			}

			ricConfigurazioneORD.getDatiConfigORD().setOrdiniChiusi(chius);
			//ricConfigurazioneORD.setOrdChiusi(request.getParameter("ordChiusi"));
			if (chius)
			{
				ricConfigurazioneORD.setOrdChiusi("on");
			}
			else
			{
				ricConfigurazioneORD.setOrdChiusi(null);
			}

			Boolean annullat=false;
			if (request.getParameter("ordAnnullati") != null) {
				annullat = true;
			}
			ricConfigurazioneORD.getDatiConfigORD().setOrdiniAnnullati(annullat);
			if (annullat)
			{
				ricConfigurazioneORD.setOrdAnnullati("on");
			}
			else
			{
				ricConfigurazioneORD.setOrdAnnullati(null);
			}
*/

			ConfigurazioneORDVO datiConfigORD = currentForm.getDatiConfigORD();

			datiConfigORD.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			if (!this.modificaConfigurazione(datiConfigORD)) {
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
				// lancio servizio di amministrazione che deve rimuovere o aggiungere l'abilitazione alla gestione del bilancio e/o sezione dalle attività degli utenti della biblioteca
				return ripristina( mapping,  form,  request,  response);

			}

			return mapping.getInputForward();
		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}

	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;

		try {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private ConfigurazioneORDVO loadConfigORD() throws Exception {
		//ConfigurazioneBOVO confBO=new ConfigurazioneBOVO("", "IC", true,loadIntest(), loadFineStp());
		// (String codP, String codB, boolean gestBil,boolean gestSez,boolean gestProf,String codProf, String codSez,StrutturaTerna kBil,  String[] codForn )
		String [] forn={"","","","","",""};
		StrutturaTerna bil=new StrutturaTerna("","","");
		ConfigurazioneORDVO confORD=new ConfigurazioneORDVO("", "", true,true, true,"", "", bil,forn,true, false,false,"A");

		//this.ricConfigurazione.setDatiConfigBO(confBO);
		return confORD;
	}

    private void loadTipoImpegno(ConfigurazioneORDForm currentForm) throws Exception {

    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	currentForm.setListaTipoImpegno(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale()));
	}

	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;
		try {
			this.impostaSezioneCerca(currentForm, request,mapping);
			// per evitare l'aggiornamento dei check
			currentForm.setSubmitDinamico(true);
			return mapping.findForward("sezioneCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca( ConfigurazioneORDForm ricConfigurazioneORD, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto

		try {
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricConfigurazioneORD.getDatiConfigORD().getCodBibl();
		String codSez=ricConfigurazioneORD.getDatiConfigORD().getCodiceSezione();
		String desSez="";
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm currentForm = (ConfigurazioneORDForm) form;
		try {
			ActionForward fwd = fornitoreCercaVeloce(mapping, request, currentForm);
			if (fwd != null)
				return fwd;

			this.impostaFornitoreCerca( currentForm,request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward fornitoreCerca0(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			ricConfigurazioneORD.setRadioForn(0);
			this.impostaFornitoreCerca( ricConfigurazioneORD,request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward fornitoreCerca1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			ricConfigurazioneORD.setRadioForn(1);
			this.impostaFornitoreCerca( ricConfigurazioneORD,request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward fornitoreCerca2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			ricConfigurazioneORD.setRadioForn(2);
			this.impostaFornitoreCerca( ricConfigurazioneORD,request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("fornitoreCerca");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward fornitoreCerca3(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			ricConfigurazioneORD.setRadioForn(3);
			this.impostaFornitoreCerca( ricConfigurazioneORD,request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward fornitoreCerca4(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			ricConfigurazioneORD.setRadioForn(4);
			this.impostaFornitoreCerca( ricConfigurazioneORD,request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward fornitoreCerca5(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			ricConfigurazioneORD.setRadioForn(5);
			this.impostaFornitoreCerca( ricConfigurazioneORD,request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void impostaFornitoreCerca(ConfigurazioneORDForm ricConfigurazioneORD, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
		try {
		int radioForn=ricConfigurazioneORD.getRadioForn();

		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricConfigurazioneORD.getDatiConfigORD().getCodBibl();
		String codForn="";
		String nomeForn="";
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="R";
		String provForn="";
		String loc="0";  // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}


	public ActionForward bilancioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneORDForm ricConfigurazioneORD;
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			this.impostaBilancioCerca( ricConfigurazioneORD, request,mapping);
			// per evitare l'aggiornamento dei check
			ricConfigurazioneORD.setSubmitDinamico(true);
			return mapping.findForward("bilancioCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward profiloCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ricConfigurazioneORD = (ConfigurazioneORDForm) form;
		try {
			this.impostaProfiloCerca(request,mapping);
			return mapping.findForward("profiloCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaProfiloCerca( HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
		// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
		String codB=ricConfigurazioneORD.getDatiConfigORD().getCodBibl();
		StrutturaCombo prof=new StrutturaCombo(ricConfigurazioneORD.getDatiConfigORD().getCodiceProfilo(),ricConfigurazioneORD.getProfAcqFornDes());
		StrutturaCombo sez=new StrutturaCombo("","");
		StrutturaCombo lin=new StrutturaCombo("","");
		StrutturaCombo pae=new StrutturaCombo("","");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,   chiama, ordina );
		request.getSession().setAttribute("attributeListaSuppProfiloVO", (ListaSuppProfiloVO) eleRicerca);

	}catch (Exception e) {	}
	}
	*/

	private void impostaBilancioCerca( ConfigurazioneORDForm ricConfigurazioneORD, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
		try {
		ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricConfigurazioneORD.getDatiConfigORD().getCodBibl();
		String ese=ricConfigurazioneORD.getDatiConfigORD().getChiaveBilancio().getCodice1();
		String cap=ricConfigurazioneORD.getDatiConfigORD().getChiaveBilancio().getCodice2();
		String imp="";
		// disabilito il tipo impegno
		//String imp=ricConfigurazioneORD.getDatiConfigORD().getChiaveBilancio().getCodice3();
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
		request.getSession().setAttribute("attributeListaSuppBilancioVO", eleRicerca);

	}catch (Exception e) {	}
	}

	   private void loadAllineamento(ConfigurazioneORDForm ricConfigurazioneORD) throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("A","A - automatico");
			lista.add(elem);
			elem = new StrutturaCombo("R","R - a richiesta");
			lista.add(elem);
			elem = new StrutturaCombo("N","N - non gestito");
			lista.add(elem);
			ricConfigurazioneORD.setListaAllineamento(lista);
		}

/*	private ConfigurazioneBOVO aggiornaConfigBO(StrutturaTerna[] listInt, StrutturaTerna[] listFin) throws Exception {
		ConfigurazioneBOVO confBO=new ConfigurazioneBOVO(loadIntest(), loadFineStp());
		//this.ricConfigurazione.setDatiConfigBO(confBO);
		return confBO;
	}
*/

   private boolean modificaConfigurazione(ConfigurazioneORDVO configurazioneORD) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaConfigurazioneOrdini(configurazioneORD);
		return valRitorno;
   }

	private ConfigurazioneORDVO loadConfigurazione(ConfigurazioneORDVO configurazioneORD) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		return configurazioneTrovata;
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("GESTIONE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_PARAMETRI_BIBLIOTECA, utente.getCodPolo(), utente.getCodBib(), null);
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







