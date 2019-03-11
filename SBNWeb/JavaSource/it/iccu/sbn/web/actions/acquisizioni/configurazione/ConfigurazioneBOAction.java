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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.FormulaIntroOrdineRVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.configurazione.ConfigurazioneBOForm;
import it.iccu.sbn.web.actions.acquisizioni.util.OrdiniWebUtil;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class ConfigurazioneBOAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static final String DATI_CONFIG_BO = "datiConfigBO";
	private static final String SELECTED_FORMULA_INTRO_ORDINE_R = "selectedFormulaIntroOrdineR";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.aggiungiInt", "aggiungiInt");
		map.put("ricerca.button.cancellaInt", "cancellaInt");

		map.put("ricerca.button.aggiungiFormIntr", "aggiungiFormIntr");
		map.put("ricerca.button.cancellaFormIntr", "cancellaFormIntr");

		map.put("ricerca.button.aggiungiTestoOgg", "aggiungiTestoOgg");
		map.put("ricerca.button.cancellaTestoOgg", "cancellaTestoOgg");

		map.put("ricerca.button.aggiungiFine", "aggiungiFine");
		map.put("ricerca.button.cancellaFine", "cancellaFine");
		map.put("ricerca.button.salva", "salva");
		map.put("ricerca.button.indietro", "indietro");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		map.put("button.caricaImmagineFirma", "caricaFileFirma");

		// almaviva5_20121117 evolutive google
		map.put("ricerca.button.aggiungiFormIntrR", "aggiungiFormIntrR");
		map.put("ricerca.button.cancellaFormIntrR", "cancellaFormIntrR");

		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
	        FactoryEJBDelegate factory =FactoryEJBDelegate.getInstance();
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
		try {
			//setto il token per le transazioni successive
			this.saveToken(request);
			//ConfigurazioneBOForm ricConfigurazione = (ConfigurazioneBOForm) form;
			Navigation navi = Navigation.getInstance(request);
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.configurazione") && navi.getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);
				}
			DynaActionForm currentForm = (DynaActionForm) form;
			if (navi.isFromBar())
	            return mapping.getInputForward();

			List<TB_CODICI> codiciLavorazioneOrdine = OrdiniWebUtil.estraiCodiciLavorazioneOrdine();
			codiciLavorazioneOrdine = CaricamentoCombo.cutFirst(codiciLavorazioneOrdine);
			if(!((ConfigurazioneBOForm) currentForm).isSessione()) {
				((ConfigurazioneBOForm) currentForm).setListaTipoLavorazione(codiciLavorazioneOrdine);
				((ConfigurazioneBOForm) currentForm).setSessione(true);
			}

			//this.loadPagina(request);

			//this.loadDatiConfigurazioneBO();
			// PRECARICAMENTO INIZIALE
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=navi.getUtente().getCodBib();
			String polo=navi.getUtente().getCodPolo();

			ConfigurazioneBOVO configBOCaricato=loadConfigBO();

			if (biblio!=null && (configBOCaricato.getCodBibl()==null || (configBOCaricato.getCodBibl()!=null && configBOCaricato.getCodBibl().trim().length()==0)))
			{
				configBOCaricato.setCodBibl(biblio);
			}
			configBOCaricato.setCodPolo(polo);

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				configBOCaricato.setCodBibl(bibScelta.getCod_bib());
			}
			// precaricamento dei dati di intestazione se non presenti
			if (configBOCaricato.getCodBibl()!=null && configBOCaricato.getCodBibl().length()==3 && configBOCaricato.getCodPolo()!=null && configBOCaricato.getCodPolo().length()==3 )
			{
				configBOCaricato=this.caricaIntestBibl(configBOCaricato, configBOCaricato.getCodPolo(), configBOCaricato.getCodBibl());
			}
			// fine precaricamento dei dati di intestazione se non presenti

			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			// LETTURA DA DB CON PASSAGGIO DEL COD BIBL
			ConfigurazioneBOVO confLetto=this.loadConfigurazione(configBOCaricato);

			// assenza di configurazione ordine
			if (confLetto==null || ( confLetto!=null && (confLetto.getCodBibl()==null || (confLetto.getCodBibl()!=null && confLetto.getCodBibl().trim().length()==0 ))))
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.configurazioneAssente"));
				this.saveErrors(request, errors);
			}

			if (confLetto!=null)
			{
				configBOCaricato=confLetto;
			}
			// gestione assenza dati di intestazione
			if (configBOCaricato!=null && configBOCaricato.getListaDatiIntestazione()!=null  && (configBOCaricato.getListaDatiIntestazione().length ==0 || (configBOCaricato.getListaDatiIntestazione().length ==1 && configBOCaricato.getListaDatiIntestazione()[0].getCodice2().equals(""))))
			{
				configBOCaricato=this.caricaIntestBibl(configBOCaricato, configBOCaricato.getCodPolo(), configBOCaricato.getCodBibl());
			}

			configBOCaricato.setUtente(navi.getUtente().getFirmaUtente());
			//caricamento dati statici

			if(configBOCaricato.isNumAutomatica())
			{
				currentForm.set("numerazBuono","automatico");
			}
			else
			{
				currentForm.set("numerazBuono","manuale");
			}


	 		boolean appoNumAutomatica=false;
	 		if (configBOCaricato.isNumAutomatica())
			{
				appoNumAutomatica=true;
			}
			currentForm.set("numAutomatica",appoNumAutomatica);

			currentForm.set(DATI_CONFIG_BO,configBOCaricato);
			//ricConfigurazione.set("datiIntest",loadIntest());
			//ricConfigurazione.set("datiFineStampa",loadFineStp());
			currentForm.set("datiIntest",configBOCaricato.getListaDatiIntestazione());
			currentForm.set("datiFineStampa",configBOCaricato.getListaDatiFineStampa());
			//ricConfigurazione.set("formulaIntroduttiva",(StrutturaQuater[]) this.loadFormula() );
			//ricConfigurazione.set("testoOggetto",(StrutturaQuater[]) this.loadOggetto() );
			StrutturaQuater[] formInt=configBOCaricato.assemblaFormulaIntro(configBOCaricato.getTestoIntroduttivo(),configBOCaricato.getTestoIntroduttivoEng());
			currentForm.set("formulaIntroduttiva",formInt );
			StrutturaQuater[] ogge=configBOCaricato.assemblaFormulaIntro(configBOCaricato.getTestoOggetto(),configBOCaricato.getTestoOggettoEng());
			currentForm.set("testoOggetto",ogge );
			currentForm.set("listaTipoLavorazione", codiciLavorazioneOrdine);
			currentForm.set("item", configBOCaricato.getFormulaIntroOrdineR() );
			if(configBOCaricato.leggiAree("T"))
			{
				currentForm.set("areaTit","si");
			}
			else
			{
				currentForm.set("areaTit","no");
			}

			if(configBOCaricato.leggiAree("E"))
			{
				currentForm.set("areaEdi","si");
			}
			else
			{
				currentForm.set("areaEdi","no");
			}

			if(configBOCaricato.leggiAree("N"))
			{
				currentForm.set("areaNum","si");
			}
			else
			{
				currentForm.set("areaNum","no");
			}

			if(configBOCaricato.leggiAree("P"))
			{
				currentForm.set("areaPub","si");
			}
			else
			{
				currentForm.set("areaPub","no");
			}

			if(configBOCaricato.isPresenzaLogoImg())
			{
				currentForm.set("logo","si");
			}
			else
			{
				currentForm.set("logo","no");
			}
			if(configBOCaricato.isPresenzaFirmaImg())
			{
				currentForm.set("firmaDigit","si");
			}
			else
			{
				currentForm.set("firmaDigit","no");
			}
			if (configBOCaricato.getNomeLogoImg()!=null && configBOCaricato.getNomeLogoImg().trim().length()>0)
			{
				currentForm.set("imgLogo",configBOCaricato.getNomeLogoImg().trim() );
			}
			if (configBOCaricato.getNomeFirmaImg()!=null && configBOCaricato.getNomeFirmaImg().trim().length()>0)
			{
				currentForm.set("imgFirma",configBOCaricato.getNomeFirmaImg().trim() );
			}
			if(configBOCaricato.isPresenzaPrezzo())
			{
				currentForm.set("prezzo","si");
			}
			else
			{
				currentForm.set("prezzo","no");
			}
			if(configBOCaricato.isEtichettaProtocollo())
			{
				currentForm.set("numProt","si");
			}
			else
			{
				currentForm.set("numProt","no");
			}
			if(configBOCaricato.isEtichettaDataProtocollo())
			{
				currentForm.set("dataProt","si");
			}
			else
			{
				currentForm.set("dataProt","no");
			}
			if(configBOCaricato.isIndicaRistampa())
			{
				currentForm.set("ristampa","si");
			}
			else
			{
				currentForm.set("ristampa","no");
			}


			if(configBOCaricato.getTipoRinnovo()!=null && configBOCaricato.getTipoRinnovo().trim().length()==1 )
			{
				if (configBOCaricato.getTipoRinnovo().trim().equals("O"))
				{
					currentForm.set("indicazioneRinnovo","originario" );
				} else if (configBOCaricato.getTipoRinnovo().trim().equals("P"))
				{
					currentForm.set("indicazioneRinnovo","precedente" );
				} else if (configBOCaricato.getTipoRinnovo().trim().equals("N"))
				{
					currentForm.set("indicazioneRinnovo","nessuno" );
				}
			}
	//		else
	//		{
	//			ricConfigurazione.set("indicazioneRinnovo",null);
	//		}



	/*



			private String ristampa; // check
			private String tipoOrdine; // COMBO
	*/
			//this.ricConfigurazione.setDatiIntest(this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione());
			//httpSession.setAttribute("datiIntest", this.ricConfigurazione.getDatiIntest());
			//httpSession.setAttribute("pippo",  this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione());
			//this.ricConfigurazione.setEnableIntest(true);
			//this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione().getElementData()
		 	} catch (Exception e) {
				return mapping.getInputForward();
			}
		return mapping.getInputForward();
	}

	//CANCELLA RIGHE DATI DI STAMPA

	public ActionForward cancellaFine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		try {
            //session.setAttribute("datoAgg",this.ricConfigurazione.getDatoAgg());
			//request.getSession().setAttribute("listaInv", this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione() );
			DynaActionForm ricConfigurazione = (DynaActionForm) form;
			ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
			// acquisizione dati aggiornati
			StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
			StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
			String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
			String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");


			if (datiFineStampa.length!=0 )
			{

				List lista =new ArrayList();
				for (int t=0;  t < datiFineStampa.length; t++)
				{
					StrutturaTerna elem = datiFineStampa[t];
					lista.add(elem);
				}

				String[] appo=  selectedDatiFineStampa;
				int i= (appo.length) -1;
				// ciclo dall'ultimo codice selezionato
				while (i>=0)
				{
					int elem = Integer.valueOf(appo[i]);
					// il valore del num riga è superiore di una unità rispetto all'indice dell'array
					int elemento= elem - 1;
					lista.remove(elemento);
					i=i-1;
				}
				this.renumera(lista);
				StrutturaTerna[] lista_fin =new StrutturaTerna [lista.size()];

				for (int r=0;  r < lista.size(); r++)
				{
					lista_fin [r] = (StrutturaTerna)lista.get(r);
				}
				datiFineStampa=lista_fin;
				ricConfigurazione.set("datiFineStampa",datiFineStampa);
				//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);
				datiConfigBO.setListaDatiFineStampa(datiFineStampa);
				ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);
				ricConfigurazione.set("selectedDatiFineStampa", null) ;

/*		if ( this.ricConfigurazione.getSelectedDatiFineStampa() != null && this.ricConfigurazione.getDatiConfigBO().getListaDatiFineStampa().size() !=0 )
		{
			List lista_new =  this.ricConfigurazione.getDatiConfigBO().getListaDatiFineStampa();
			String [] appo= this.ricConfigurazione.getSelectedDatiFineStampa();
			int i= (appo.length) -1;
			// ciclo dall'ultimo codice selezionato
			while (i>=0)
			{
				int elem = Integer.valueOf(appo[i]);
				// il valore del num riga è superiore di una unità rispetto all'indiece dell'array
				int elemento= elem - 1;
				lista_new.remove(elemento);
				i=i-1;
			}
			ricConfigurazione.getDatiConfigBO().setListaDatiFineStampa(this.renumera(lista_new));
			// tolgo i check
			this.ricConfigurazione.setSelectedDatiFineStampa(null);
			//return mapping.getInputForward();
		}
		else
		{
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.buoniOrdine.crea"));
			this.saveErrors(request, errors);
		}
			//return mapping.findForward("cancellaFine");
*/
			}
			return mapping.getInputForward();
	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	// AGGIUNGI RIGHE INTEST
	public ActionForward aggiungiInt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//List provaarray= (List) request.getSession().getAttribute("listaInv");

		try {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaActionForm ricConfigurazione = (DynaActionForm) form;
		ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
		// acquisizione dati aggiornati
		StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
		StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
		String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
		String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");


		//if (datiIntest.length!=0 )
		//{
			// si deve aumentare la dimensione di datiIntest  di una unità
			StrutturaTerna[]  selezioneComposta=new StrutturaTerna[datiIntest.length +1];
			selezioneComposta [selezioneComposta.length-1]=new StrutturaTerna();
			// deve essereimpostato il campo codice1 per far funzionare anche selectedDatiIntest
			selezioneComposta [selezioneComposta.length-1].setCodice1(String.valueOf(selezioneComposta.length));
			System.arraycopy(datiIntest,0,selezioneComposta,0,datiIntest.length);
			datiIntest=new StrutturaTerna[selezioneComposta.length];
			System.arraycopy(selezioneComposta,0,datiIntest,0,selezioneComposta.length);

			ricConfigurazione.set("datiIntest",datiIntest);
			//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);
			//ricConfigurazione.set("datiConfigBO",datiConfigBOAppo);
			datiConfigBO.setListaDatiIntestazione(datiIntest);
			ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);

		//}

		return mapping.getInputForward();
		//return mapping.findForward("aggiungiInt");
		// fine elaborazione

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	//CANCELLA RIGHE INTESTAZIONE
	public ActionForward cancellaInt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaActionForm ricConfigurazione = (DynaActionForm) form;

		try {
            //session.setAttribute("datoAgg",this.ricConfigurazione.getDatoAgg());
			//request.getSession().setAttribute("listaInv", this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione() );
			ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
			// acquisizione dati aggiornati
			StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
			StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
			String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
			String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");


			if (datiIntest.length!=0 )
			{

				List lista =new ArrayList();
				for (int t=0;  t < datiIntest.length; t++)
				{
					StrutturaTerna elem = datiIntest[t];
					lista.add(elem);
				}

				String[] appo=  selectedDatiIntest;
				int i= (appo.length) -1;
				// ciclo dall'ultimo codice selezionato
				while (i>=0)
				{
					int elem = Integer.valueOf(appo[i]);
					// il valore del num riga è superiore di una unità rispetto all'indice dell'array
					int elemento= elem - 1;
					lista.remove(elemento);
					i=i-1;
				}
				this.renumera(lista);
				StrutturaTerna[] lista_fin =new StrutturaTerna [lista.size()];

				for (int r=0;  r < lista.size(); r++)
				{
					lista_fin [r] = (StrutturaTerna)lista.get(r);
				}
				datiIntest=lista_fin;
				ricConfigurazione.set("datiIntest",datiIntest);
				//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);
				//ricConfigurazione.set("datiConfigBO",datiConfigBOAppo);
				datiConfigBO.setListaDatiIntestazione(datiIntest);
				ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);
				ricConfigurazione.set("selectedDatiIntest", null) ;

		// elaborazione
/*		if ( this.ricConfigurazione.getSelectedDatiIntest() != null && this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione().size() !=0 )
		{
			List lista_new =  this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione();
			String [] appo= this.ricConfigurazione.getSelectedDatiIntest();
			int i= (appo.length) -1;
			// ciclo dall'ultimo codice selezionato
			while (i>=0)
			{
				int elem = Integer.valueOf(appo[i]);
				// il valore del num riga è superiore di una unità rispetto all'indice dell'array
				int elemento= elem - 1;
				lista_new.remove(elemento);
				i=i-1;
			}
			ricConfigurazione.getDatiConfigBO().setListaDatiIntestazione(this.renumera(lista_new));
			// tolgo i check
			this.ricConfigurazione.setSelectedDatiIntest(null);
			//return mapping.getInputForward();
		}
		else
		{
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.buoniOrdine.crea"));
			this.saveErrors(request, errors);
		}*/
		//return mapping.findForward("cancellaInt");
			}
			return mapping.getInputForward();

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	// AGGIUNGI RIGHE DATI STAMPA
	public ActionForward aggiungiFine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			DynaActionForm ricConfigurazione = (DynaActionForm) form;
			ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
			// acquisizione dati aggiornati
			StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
			StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
			String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
			String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");

			//if (datiFineStampa.length!=0 )
			//{
				// si deve aumentare la dimensione di datiIntest  di una unità
				StrutturaTerna[]  selezioneComposta=new StrutturaTerna[datiFineStampa.length +1];
				selezioneComposta [selezioneComposta.length-1]=new StrutturaTerna();
				// deve essereimpostato il campo codice1 per far funzionare anche selectedDatiIntest
				selezioneComposta [selezioneComposta.length-1].setCodice1(String.valueOf(selezioneComposta.length));
				System.arraycopy(datiFineStampa,0,selezioneComposta,0,datiFineStampa.length);
				datiFineStampa=new StrutturaTerna[selezioneComposta.length];
				System.arraycopy(selezioneComposta,0,datiFineStampa,0,selezioneComposta.length);

				ricConfigurazione.set("datiFineStampa",datiFineStampa);
				//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);
				//ricConfigurazione.set("datiConfigBO",datiConfigBOAppo);
				datiConfigBO.setListaDatiFineStampa(datiFineStampa);
				ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);

			//}

/*		List lista_newFine = this.ricConfigurazione.getDatiConfigBO().getListaDatiFineStampa();
		int numeroRiga=ricConfigurazione.getDatiConfigBO().getListaDatiFineStampa().size() + 1;
		StrutturaTerna elemFS = new StrutturaTerna(String.valueOf(numeroRiga),"","");
		lista_newFine.add(elemFS);
		//ricConfigurazione.getDatiConfigBO().setListaDatiFineStampa(lista_new);
		ricConfigurazione.getDatiConfigBO().setListaDatiFineStampa(this.renumera(lista_newFine));*/
		//return mapping.findForward("aggiungiFine");
		return mapping.getInputForward();

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward aggiungiFormIntr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//List provaarray= (List) request.getSession().getAttribute("listaInv");

		try {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaActionForm ricConfigurazione = (DynaActionForm) form;
		ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
		// acquisizione dati aggiornati
		StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
		StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
		String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
		String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");

		StrutturaQuater[]  datiTestoOggetto=(StrutturaQuater[]) ricConfigurazione.get("testoOggetto");
		String[] selectedTestoOggetto=(String[])ricConfigurazione.get("selectedTestoOggetto");


		StrutturaQuater[]  datiFormulaIntroduttiva=(StrutturaQuater[]) ricConfigurazione.get("formulaIntroduttiva");
		String[] selectedFormulaIntroduttiva=(String[])ricConfigurazione.get("selectedFormulaIntroduttiva");



//		if (datiFormulaIntroduttiva.length!=0 )
//		{
			// si deve aumentare la dimensione di datiIntest  di una unità
			StrutturaQuater[]  selezioneComposta=new StrutturaQuater[datiFormulaIntroduttiva.length +1];
			selezioneComposta [selezioneComposta.length-1]=new StrutturaQuater();
			// deve essereimpostato il campo codice1 per far funzionare anche selectedDatiIntest
			selezioneComposta [selezioneComposta.length-1].setCodice1(String.valueOf(selezioneComposta.length));
			System.arraycopy(datiFormulaIntroduttiva,0,selezioneComposta,0,datiFormulaIntroduttiva.length);
			datiFormulaIntroduttiva=new StrutturaQuater[selezioneComposta.length];

			System.arraycopy(selezioneComposta,0,datiFormulaIntroduttiva,0,selezioneComposta.length);

			ricConfigurazione.set("formulaIntroduttiva",datiFormulaIntroduttiva);
			//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);

			List<StrutturaCombo> testoIntroduttivo=new ArrayList<StrutturaCombo>();
			List<StrutturaCombo> testoIntroduttivoEng=new ArrayList<StrutturaCombo>();

			StrutturaCombo[] testoIntroduttivo_lst=new StrutturaCombo[6];  // testi in italiano
			StrutturaCombo[] testoIntroduttivoEng_lst=new StrutturaCombo[6];  // testi in inglese


			for (int r=0;  r < datiFormulaIntroduttiva.length; r++)
			{
				if (datiFormulaIntroduttiva[r].getCodice3().equals("ITA") )
				{
					testoIntroduttivo.add(new StrutturaCombo(datiFormulaIntroduttiva [r].getCodice2(), datiFormulaIntroduttiva [r].getCodice4()));
				}
				else
				{
					testoIntroduttivoEng.add(new StrutturaCombo(datiFormulaIntroduttiva [r].getCodice2(), datiFormulaIntroduttiva [r].getCodice4()));
				}
			}


			for (int r=0;  r < testoIntroduttivo.size() && r<6; r++)
			{
				testoIntroduttivo_lst [r] = testoIntroduttivo.get(r);
			}
			for (int r=0;  r < testoIntroduttivoEng.size() && r<6; r++)
			{
				testoIntroduttivoEng_lst [r] = testoIntroduttivoEng.get(r);
			}

			datiConfigBO.setTestoIntroduttivo(testoIntroduttivo_lst);
			datiConfigBO.setTestoIntroduttivoEng(testoIntroduttivoEng_lst);
			ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);

//		}

		return mapping.getInputForward();
		//return mapping.findForward("aggiungiInt");
		// fine elaborazione

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	//CANCELLA RIGHE INTESTAZIONE
	public ActionForward cancellaFormIntr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaActionForm ricConfigurazione = (DynaActionForm) form;

		try {
            //session.setAttribute("datoAgg",this.ricConfigurazione.getDatoAgg());
			//request.getSession().setAttribute("listaInv", this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione() );
			ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
			// acquisizione dati aggiornati
			StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
			StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
			String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
			String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");

			StrutturaQuater[]  datiFormulaIntroduttiva=(StrutturaQuater[]) ricConfigurazione.get("formulaIntroduttiva");
			String[] selectedFormulaIntroduttiva=(String[])ricConfigurazione.get("selectedFormulaIntroduttiva");



			if (datiFormulaIntroduttiva.length!=0 )
			{

				List lista =new ArrayList();
				for (int t=0;  t < datiFormulaIntroduttiva.length; t++)
				{
					StrutturaQuater elem = datiFormulaIntroduttiva[t];
					lista.add(elem);
				}

				String[] appo=  selectedFormulaIntroduttiva;
				int i= (appo.length) -1;
				// ciclo dall'ultimo codice selezionato
				while (i>=0 )
				{
					int elem = Integer.valueOf(appo[i]);
					// il valore del num riga è superiore di una unità rispetto all'indice dell'array
					int elemento= elem - 1;
					lista.remove(elemento);
					i=i-1;
				}
				this.renumeraBis(lista);
				StrutturaQuater[] lista_fin =new StrutturaQuater [lista.size()];


				List<StrutturaCombo> testoIntroduttivo=new ArrayList<StrutturaCombo>();
				List<StrutturaCombo> testoIntroduttivoEng=new ArrayList<StrutturaCombo>();

				StrutturaCombo[] testoIntroduttivo_lst=new StrutturaCombo[6];  // testi in italiano
				StrutturaCombo[] testoIntroduttivoEng_lst=new StrutturaCombo[6];  // testi in inglese


				for (int r=0;  r < lista.size(); r++)
				{
					lista_fin [r] = (StrutturaQuater)lista.get(r);

					if (lista_fin [r].getCodice3().equals("ITA"))
					{
						testoIntroduttivo.add(new StrutturaCombo(lista_fin [r].getCodice2(), lista_fin [r].getCodice4()));
					}
					else
					{
						testoIntroduttivoEng.add(new StrutturaCombo(lista_fin [r].getCodice2(), lista_fin [r].getCodice4()));
					}

				}
				datiFormulaIntroduttiva=lista_fin;
				ricConfigurazione.set("formulaIntroduttiva",datiFormulaIntroduttiva);

				//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);
				// ciclo per caricare a seconda della lingua il campo  testoIntroduttivo o testoIntroduttivoEng di tipo StrutturaCombo[] di 6


				for (int r=0;  r < testoIntroduttivo.size() && r<6; r++)
				{
					testoIntroduttivo_lst [r] = testoIntroduttivo.get(r);
				}
				for (int r=0;  r < testoIntroduttivoEng.size() && r<6; r++)
				{
					testoIntroduttivoEng_lst [r] = testoIntroduttivoEng.get(r);
				}

				datiConfigBO.setTestoIntroduttivo(testoIntroduttivo_lst);
				datiConfigBO.setTestoIntroduttivoEng(testoIntroduttivoEng_lst);

				ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);
				ricConfigurazione.set("selectedFormulaIntroduttiva", null) ;

			}
			return mapping.getInputForward();

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward aggiungiTestoOgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//List provaarray= (List) request.getSession().getAttribute("listaInv");

		try {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaActionForm ricConfigurazione = (DynaActionForm) form;
		ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
		// acquisizione dati aggiornati
		StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
		StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
		String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
		String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");

		StrutturaQuater[]  datiFormulaIntroduttiva=(StrutturaQuater[]) ricConfigurazione.get("formulaIntroduttiva");
		String[] selectedFormulaIntroduttiva=(String[])ricConfigurazione.get("selectedFormulaIntroduttiva");

		StrutturaQuater[]  datiTestoOggetto=(StrutturaQuater[]) ricConfigurazione.get("testoOggetto");
		String[] selectedTestoOggetto=(String[])ricConfigurazione.get("selectedTestoOggetto");


//		if (datiFormulaIntroduttiva.length!=0 )
//		{
			// si deve aumentare la dimensione di datiIntest  di una unità
			StrutturaQuater[]  selezioneComposta=new StrutturaQuater[datiTestoOggetto.length +1];
			selezioneComposta [selezioneComposta.length-1]=new StrutturaQuater();
			// deve essereimpostato il campo codice1 per far funzionare anche selectedDatiIntest
			selezioneComposta [selezioneComposta.length-1].setCodice1(String.valueOf(selezioneComposta.length));
			System.arraycopy(datiTestoOggetto,0,selezioneComposta,0,datiTestoOggetto.length);
			datiTestoOggetto=new StrutturaQuater[selezioneComposta.length];
			System.arraycopy(selezioneComposta,0,datiTestoOggetto,0,selezioneComposta.length);

			ricConfigurazione.set("testoOggetto",datiTestoOggetto);
			//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);

			List<StrutturaCombo> testoOggetto=new ArrayList<StrutturaCombo>();
			List<StrutturaCombo> testoOggettoEng=new ArrayList<StrutturaCombo>();

			StrutturaCombo[] testoOggetto_lst=new StrutturaCombo[6];  // testi in italiano
			StrutturaCombo[] testoOggettoEng_lst=new StrutturaCombo[6];  // testi in inglese


			for (int r=0;  r < datiTestoOggetto.length; r++)
			{
				if (datiTestoOggetto[r].getCodice3().equals("ITA") )
				{
					testoOggetto.add(new StrutturaCombo(datiTestoOggetto [r].getCodice2(), datiTestoOggetto [r].getCodice4()));
				}
				else
				{
					testoOggettoEng.add(new StrutturaCombo(datiTestoOggetto [r].getCodice2(), datiTestoOggetto [r].getCodice4()));
				}
			}

			//ricConfigurazione.set("datiConfigBO",datiConfigBOAppo);

			for (int r=0;  r < testoOggetto.size() && r<6; r++)
			{
				testoOggetto_lst [r] = testoOggetto.get(r);
			}
			for (int r=0;  r < testoOggettoEng.size() && r<6; r++)
			{
				testoOggettoEng_lst [r] = testoOggettoEng.get(r);
			}

			datiConfigBO.setTestoOggetto(testoOggetto_lst);
			datiConfigBO.setTestoOggettoEng(testoOggettoEng_lst);
			ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);

//		}

		return mapping.getInputForward();
		//return mapping.findForward("aggiungiInt");
		// fine elaborazione

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	//CANCELLA RIGHE INTESTAZIONE
	public ActionForward cancellaTestoOgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaActionForm ricConfigurazione = (DynaActionForm) form;

		try {
            //session.setAttribute("datoAgg",this.ricConfigurazione.getDatoAgg());
			//request.getSession().setAttribute("listaInv", this.ricConfigurazione.getDatiConfigBO().getListaDatiIntestazione() );
			ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
			// acquisizione dati aggiornati
			StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
			StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
			String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
			String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");

			StrutturaQuater[]  datiFormulaIntroduttiva=(StrutturaQuater[]) ricConfigurazione.get("formulaIntroduttiva");
			String[] selectedFormulaIntroduttiva=(String[])ricConfigurazione.get("selectedFormulaIntroduttiva");

			StrutturaQuater[]  datiTestoOggetto=(StrutturaQuater[]) ricConfigurazione.get("testoOggetto");
			String[] selectedTestoOggetto=(String[])ricConfigurazione.get("selectedTestoOggetto");



			if (datiTestoOggetto.length!=0 )
			{

				List lista =new ArrayList();
				for (int t=0;  t < datiTestoOggetto.length; t++)
				{
					StrutturaQuater elem = datiTestoOggetto[t];
					lista.add(elem);
				}

				String[] appo=  selectedTestoOggetto;
				int i= (appo.length) -1;
				// ciclo dall'ultimo codice selezionato
				while (i>=0)
				{
					int elem = Integer.valueOf(appo[i]);
					// il valore del num riga è superiore di una unità rispetto all'indice dell'array
					int elemento= elem - 1;
					lista.remove(elemento);
					i=i-1;
				}
				this.renumeraBis(lista);
				StrutturaQuater[] lista_fin =new StrutturaQuater [lista.size()];

				List<StrutturaCombo> testoOggetto=new ArrayList<StrutturaCombo>();
				List<StrutturaCombo> testoOggettoEng=new ArrayList<StrutturaCombo>();

				StrutturaCombo[] testoOggetto_lst=new StrutturaCombo[6];  // testi in italiano
				StrutturaCombo[] testoOggettoEng_lst=new StrutturaCombo[6];  // testi in inglese


				for (int r=0;  r < lista.size(); r++)
				{
					lista_fin [r] = (StrutturaQuater)lista.get(r);

					if (lista_fin [r].getCodice3().equals("ITA"))
					{
						testoOggetto.add(new StrutturaCombo(lista_fin [r].getCodice2(), lista_fin [r].getCodice4()));
					}
					else
					{
						testoOggettoEng.add(new StrutturaCombo(lista_fin [r].getCodice2(), lista_fin [r].getCodice4()));
					}

				}
				datiTestoOggetto=lista_fin;
				ricConfigurazione.set("testoOggetto",datiTestoOggetto);

				//ConfigurazioneBOVO datiConfigBOAppo=new ConfigurazioneBOVO( datiConfigBO.getCodPolo(), datiConfigBO.getCodBibl(), datiConfigBO.isNumAutomatica(),  datiIntest, datiFineStampa);
				// ciclo per caricare a seconda della lingua il campo  testoIntroduttivo o testoIntroduttivoEng di tipo StrutturaCombo[] di 6


				for (int r=0;  r < testoOggetto.size() && r<6; r++)
				{
					testoOggetto_lst [r] = testoOggetto.get(r);
				}
				for (int r=0;  r < testoOggettoEng.size() && r<6; r++)
				{
					testoOggettoEng_lst [r] = testoOggettoEng.get(r);
				}

				datiConfigBO.setTestoOggetto(testoOggetto_lst);
				datiConfigBO.setTestoOggettoEng(testoOggettoEng_lst);

				ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);
				ricConfigurazione.set("selectedTestoOggetto", null) ;

			}
			return mapping.getInputForward();

	 	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	// SALVA
	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		DynaValidatorForm ricConfigurazione = (DynaValidatorForm) form;
		ConfigurazioneBOForm ricConfigurazioneNorm = (ConfigurazioneBOForm) form;

		try {

			ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
			String datiConfigBOAppoBibl=datiConfigBO.getCodBibl();
			String datiConfigBOAppoPolo=datiConfigBO.getCodPolo();
			Boolean numAutomatica = (Boolean)ricConfigurazione.get("numAutomatica");
			// acquisizione dati aggiornati
			StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
			StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
			String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
			String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");


			// CARICAMENTO CON I VALORI POSTATI
			//datiConfigBO=new ConfigurazioneBOVO( datiConfigBOAppoPolo, datiConfigBOAppoBibl, numAutomatica,  datiIntest, datiFineStampa);
			datiConfigBO.setNumAutomatica(numAutomatica);
			datiConfigBO.setListaDatiIntestazione(datiIntest);
			datiConfigBO.setListaDatiFineStampa(datiFineStampa);


			// AGGIORNAMENTO DEI NUOVI CAMPI



			// FORMULA INTROD
			StrutturaQuater[]  datiFormulaIntroduttiva=(StrutturaQuater[]) ricConfigurazione.get("formulaIntroduttiva");
			StrutturaCombo[] testoIntroduttivo_lst=new StrutturaCombo[6];  // testi in italiano
			StrutturaCombo[] testoIntroduttivoEng_lst=new StrutturaCombo[6];  // testi in inglese
			List<StrutturaCombo> testoIntroduttivo=new ArrayList<StrutturaCombo>();
			List<StrutturaCombo> testoIntroduttivoEng=new ArrayList<StrutturaCombo>();
			for (int r=0;  r < datiFormulaIntroduttiva.length; r++)
			{
				if (datiFormulaIntroduttiva[r].getCodice3().equals("ITA") )
				{
					testoIntroduttivo.add(new StrutturaCombo(datiFormulaIntroduttiva [r].getCodice4(), datiFormulaIntroduttiva [r].getCodice2()));
				}
				else
				{
					testoIntroduttivoEng.add(new StrutturaCombo(datiFormulaIntroduttiva [r].getCodice4(), datiFormulaIntroduttiva [r].getCodice2()));
				}
			}
			for (int r=0;  r < testoIntroduttivo.size() && r<6; r++)
			{
				testoIntroduttivo_lst [r] = testoIntroduttivo.get(r);
			}
			for (int r=0;  r < testoIntroduttivoEng.size() && r<6; r++)
			{
				testoIntroduttivoEng_lst [r] = testoIntroduttivoEng.get(r);
			}
			datiConfigBO.setTestoIntroduttivo(testoIntroduttivo_lst);
			datiConfigBO.setTestoIntroduttivoEng(testoIntroduttivoEng_lst);
			// FINE FORMULA INTROD

			// TESTO OGGETTO
			StrutturaQuater[]  datiTestoOggetto=(StrutturaQuater[]) ricConfigurazione.get("testoOggetto");

			List<StrutturaCombo> testoOggetto=new ArrayList<StrutturaCombo>();
			List<StrutturaCombo> testoOggettoEng=new ArrayList<StrutturaCombo>();

			StrutturaCombo[] testoOggetto_lst=new StrutturaCombo[6];  // testi in italiano
			StrutturaCombo[] testoOggettoEng_lst=new StrutturaCombo[6];  // testi in inglese


			for (int r=0;  r < datiTestoOggetto.length; r++)
			{
				if (datiTestoOggetto[r].getCodice3().equals("ITA") )
				{
					testoOggetto.add(new StrutturaCombo(datiTestoOggetto [r].getCodice4(),datiTestoOggetto [r].getCodice2() ));
				}
				else
				{
					testoOggettoEng.add(new StrutturaCombo(datiTestoOggetto [r].getCodice4(), datiTestoOggetto [r].getCodice2()));
				}
			}

			for (int r=0;  r < testoOggetto.size() && r<6; r++)
			{
				testoOggetto_lst [r] = testoOggetto.get(r);
			}
			for (int r=0;  r < testoOggettoEng.size() && r<6; r++)
			{
				testoOggettoEng_lst [r] = testoOggettoEng.get(r);
			}

			datiConfigBO.setTestoOggetto(testoOggetto_lst);
			datiConfigBO.setTestoOggettoEng(testoOggettoEng_lst);

			// FINE TESTO OGGETTO


			// VALIDAZIONE

			 datiConfigBO.validate();

			// AREE ISBD
			//areaTit; // check
			//areaEdi; // check
			//areaNum; // check
			//areaPub; // check

			String[] areeIsbd =new String[7];
			String  areaTit=(String) ricConfigurazione.get("areaTit");
			String  areaEdi=(String) ricConfigurazione.get("areaEdi");
			String  areaNum=(String) ricConfigurazione.get("areaNum");
			String  areaPub=(String) ricConfigurazione.get("areaPub");


			for(int i=0; i<6; i++)
			{
				areeIsbd[i]="0";
				if (areaTit!=null && i==0 && areaTit.equals("si"))
				{
					areeIsbd[i]="1";
				}
				if (areaEdi!=null && i==1 && areaEdi.equals("si"))
				{
					areeIsbd[i]="1";
				}
				if (areaNum!=null && i==2 && areaNum.equals("si"))
				{
					areeIsbd[i]="1";
				}
				if (areaPub!=null && i==3 && areaPub.equals("si"))
				{
					areeIsbd[i]="1";
				}
			}
			datiConfigBO.setAreeIsbd(areeIsbd);
			// FINE AREE ISBD

			String  logo=(String) ricConfigurazione.get("logo");
			String  imgLogo=(String) ricConfigurazione.get("imgLogo");
			datiConfigBO.setPresenzaLogoImg(false);
			datiConfigBO.setNomeLogoImg("");
			if (logo!=null && logo.equals("si"))
			{
				datiConfigBO.setPresenzaLogoImg(true);
				if (imgLogo!=null && imgLogo.trim().length()>0)
				{
					datiConfigBO.setNomeLogoImg(imgLogo);
				}
			}

			String  firmaDigit=(String) ricConfigurazione.get("firmaDigit");
			String  imgFirma=(String) ricConfigurazione.get("imgFirma");
			datiConfigBO.setPresenzaFirmaImg(false);
			datiConfigBO.setNomeFirmaImg("");
			if (firmaDigit!=null && firmaDigit.equals("si"))
			{
				datiConfigBO.setPresenzaFirmaImg(true);
				if (imgFirma!=null && imgFirma.trim().length()>0)
				{
					datiConfigBO.setNomeFirmaImg(imgFirma);
				}
			}

			String  prezzo=(String) ricConfigurazione.get("prezzo");
			datiConfigBO.setPresenzaPrezzo(false);
			if (prezzo!=null && prezzo.equals("si"))
			{
				datiConfigBO.setPresenzaPrezzo(true);
			}

			String  ristampa=(String) ricConfigurazione.get("ristampa");
			datiConfigBO.setIndicaRistampa(false);
			if (ristampa!=null && ristampa.equals("si"))
			{
				datiConfigBO.setIndicaRistampa(true);
			}


			String  numerazBuono=(String) ricConfigurazione.get("numerazBuono");
			datiConfigBO.setNumAutomatica(false);
			if (numerazBuono!=null && numerazBuono.equals("automatico"))
			{
				datiConfigBO.setNumAutomatica(true);
			}

			String  numProt=(String) ricConfigurazione.get("numProt");
			datiConfigBO.setEtichettaProtocollo(false);
			if (numProt!=null && numProt.equals("si"))
			{
				datiConfigBO.setEtichettaProtocollo(true);
			}

			String  dataProt=(String) ricConfigurazione.get("dataProt");
			datiConfigBO.setEtichettaDataProtocollo(false);
			if (dataProt!=null && dataProt.equals("si"))
			{
				datiConfigBO.setEtichettaDataProtocollo(true);
			}

			String  indicazioneRinnovo=(String) ricConfigurazione.get("indicazioneRinnovo");
			datiConfigBO.setTipoRinnovo("N");
			if (indicazioneRinnovo!=null && indicazioneRinnovo.equals("originario"))
			{
				datiConfigBO.setTipoRinnovo("O");
			} else if (indicazioneRinnovo!=null && indicazioneRinnovo.equals("precedente"))
			{
				datiConfigBO.setTipoRinnovo("P");
			} else if (indicazioneRinnovo!=null && indicazioneRinnovo.equals("nessuno"))
			{
				datiConfigBO.setTipoRinnovo("N");
			}

		// FINE AGGIORNAMENTO DEI NUOVI CAMPI




			ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);
			ricConfigurazione.set("numAutomatica",datiConfigBO.isNumAutomatica());



			ActionMessages errors = new ActionMessages();
			ricConfigurazioneNorm.setConferma(true);
    		ricConfigurazioneNorm.setDisabilitaTutto(true);

			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
 			this.saveErrors(request, errors);
 			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
 			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			ricConfigurazioneNorm.setConferma(false);
    		ricConfigurazioneNorm.setDisabilitaTutto(false);
			return mapping.getInputForward();

	 	} catch (Exception e) {
			ricConfigurazioneNorm.setConferma(false);
    		ricConfigurazioneNorm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	// INDIETRO
	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ConfigurazioneBOForm ricConfigurazione = (ConfigurazioneBOForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!ricConfigurazione.isSessione())
				{
					ricConfigurazione.setSessione(true);
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

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//ricConfigurazione = (ConfigurazioneBOForm) form;
		try {
/*			if (!isTokenValid(request)) {
				saveToken(request);
				if(!this.esaCambio.isSessione())
				{
					this.esaCambio.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//	this.checkForm(request);
		resetToken(request);
*/
		// ripristino del valore iniziale

		DynaActionForm ricConfigurazione = (DynaActionForm) form;
		ConfigurazioneBOVO configBOCaricato=loadConfigBO();
		String biblio=Navigation.getInstance(request).getUtente().getCodBib();
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		configBOCaricato.setCodBibl(biblio);
		configBOCaricato.setCodPolo(polo);

		// LETTURA DA DB CON PASSAGGIO DEL COD BIBL
		ConfigurazioneBOVO confLetto=this.loadConfigurazione(configBOCaricato);
		if (confLetto!=null ) //&& confLetto.getListaDatiIntestazione().length>0
		{
			configBOCaricato=confLetto;
		}
		ricConfigurazione.set(DATI_CONFIG_BO,configBOCaricato);

		configBOCaricato.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
		//caricamento dati statici

		if(configBOCaricato.isNumAutomatica())
		{
			ricConfigurazione.set("numerazBuono","automatico");
		}
		else
		{
			ricConfigurazione.set("numerazBuono","manuale");
		}


 		boolean appoNumAutomatica=false;
 		if (configBOCaricato.isNumAutomatica())
		{
			appoNumAutomatica=true;
		}
		ricConfigurazione.set("numAutomatica",appoNumAutomatica);

		ricConfigurazione.set(DATI_CONFIG_BO,configBOCaricato);
		//ricConfigurazione.set("datiIntest",loadIntest());
		//ricConfigurazione.set("datiFineStampa",loadFineStp());
		ricConfigurazione.set("datiIntest",configBOCaricato.getListaDatiIntestazione());
		ricConfigurazione.set("datiFineStampa",configBOCaricato.getListaDatiFineStampa());
		//ricConfigurazione.set("formulaIntroduttiva",(StrutturaQuater[]) this.loadFormula() );
		//ricConfigurazione.set("testoOggetto",(StrutturaQuater[]) this.loadOggetto() );
		StrutturaQuater[] formInt=configBOCaricato.assemblaFormulaIntro(configBOCaricato.getTestoIntroduttivo(),configBOCaricato.getTestoIntroduttivoEng());
		ricConfigurazione.set("formulaIntroduttiva",formInt );
		StrutturaQuater[] ogge=configBOCaricato.assemblaFormulaIntro(configBOCaricato.getTestoOggetto(),configBOCaricato.getTestoOggettoEng());
		ricConfigurazione.set("testoOggetto",ogge );
		if(configBOCaricato.leggiAree("T"))
		{
			ricConfigurazione.set("areaTit","si");
		}
		else
		{
			ricConfigurazione.set("areaTit","no");
		}

		if(configBOCaricato.leggiAree("E"))
		{
			ricConfigurazione.set("areaEdi","si");
		}
		else
		{
			ricConfigurazione.set("areaEdi","no");
		}

		if(configBOCaricato.leggiAree("N"))
		{
			ricConfigurazione.set("areaNum","si");
		}
		else
		{
			ricConfigurazione.set("areaNum","no");
		}

		if(configBOCaricato.leggiAree("P"))
		{
			ricConfigurazione.set("areaPub","si");
		}
		else
		{
			ricConfigurazione.set("areaPub","no");
		}

		if(configBOCaricato.isPresenzaLogoImg())
		{
			ricConfigurazione.set("logo","si");
		}
		else
		{
			ricConfigurazione.set("logo","no");
		}
		if(configBOCaricato.isPresenzaFirmaImg())
		{
			ricConfigurazione.set("firmaDigit","si");
		}
		else
		{
			ricConfigurazione.set("firmaDigit","no");
		}
		if (configBOCaricato.getNomeLogoImg()!=null && configBOCaricato.getNomeLogoImg().trim().length()>0)
		{
			ricConfigurazione.set("imgLogo",configBOCaricato.getNomeLogoImg().trim() );
		}
		if (configBOCaricato.getNomeFirmaImg()!=null && configBOCaricato.getNomeFirmaImg().trim().length()>0)
		{
			ricConfigurazione.set("imgFirma",configBOCaricato.getNomeFirmaImg().trim() );
		}
		if(configBOCaricato.isPresenzaPrezzo())
		{
			ricConfigurazione.set("prezzo","si");
		}
		else
		{
			ricConfigurazione.set("prezzo","no");
		}
		if(configBOCaricato.isEtichettaProtocollo())
		{
			ricConfigurazione.set("numProt","si");
		}
		else
		{
			ricConfigurazione.set("numProt","no");
		}
		if(configBOCaricato.isEtichettaDataProtocollo())
		{
			ricConfigurazione.set("dataProt","si");
		}
		else
		{
			ricConfigurazione.set("dataProt","no");
		}
		if(configBOCaricato.isIndicaRistampa())
		{
			ricConfigurazione.set("ristampa","si");
		}
		else
		{
			ricConfigurazione.set("ristampa","no");
		}


		if(configBOCaricato.getTipoRinnovo()!=null && configBOCaricato.getTipoRinnovo().trim().length()==1 )
		{
			if (configBOCaricato.getTipoRinnovo().trim().equals("O"))
			{
				ricConfigurazione.set("indicazioneRinnovo","originario" );
			} else if (configBOCaricato.getTipoRinnovo().trim().equals("P"))
			{
				ricConfigurazione.set("indicazioneRinnovo","precedente" );
			} else if (configBOCaricato.getTipoRinnovo().trim().equals("N"))
			{
				ricConfigurazione.set("indicazioneRinnovo","nessuno" );
			}
		}

		return mapping.getInputForward();
		//return mapping.findForward("ripristina");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm ricConfigurazione = (DynaValidatorForm) form;
		ConfigurazioneBOForm ricConfigurazioneNorm = (ConfigurazioneBOForm) form;

		try {

		// dati aggiornati
		ConfigurazioneBOVO datiConfigBO=(ConfigurazioneBOVO) ricConfigurazione.get(DATI_CONFIG_BO);
		String datiConfigBOAppoBibl=datiConfigBO.getCodBibl();
		String datiConfigBOAppoPolo=datiConfigBO.getCodPolo();
		Boolean numAutomatica = (Boolean)ricConfigurazione.get("numAutomatica");
		// acquisizione dati aggiornati
		StrutturaTerna[]  datiIntest=(StrutturaTerna[]) ricConfigurazione.get("datiIntest");
		StrutturaTerna[]  datiFineStampa=(StrutturaTerna[]) ricConfigurazione.get("datiFineStampa");
		String[] selectedDatiIntest=(String[])ricConfigurazione.get("selectedDatiIntest");
		String[] selectedDatiFineStampa=(String[])ricConfigurazione.get("selectedDatiFineStampa");


		// CARICAMENTO CON I VALORI POSTATI
		//datiConfigBO=new ConfigurazioneBOVO( datiConfigBOAppoPolo, datiConfigBOAppoBibl, numAutomatica,  datiIntest, datiFineStampa);
		datiConfigBO.setNumAutomatica(numAutomatica);
		datiConfigBO.setListaDatiIntestazione(datiIntest);
		datiConfigBO.setListaDatiFineStampa(datiFineStampa);


		datiConfigBO.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());


		// AGGIORNAMENTO DEI NUOVI CAMPI



			// FORMULA INTROD
			StrutturaQuater[]  datiFormulaIntroduttiva=(StrutturaQuater[]) ricConfigurazione.get("formulaIntroduttiva");
			StrutturaCombo[] testoIntroduttivo_lst=new StrutturaCombo[6];  // testi in italiano
			StrutturaCombo[] testoIntroduttivoEng_lst=new StrutturaCombo[6];  // testi in inglese
			List<StrutturaCombo> testoIntroduttivo=new ArrayList<StrutturaCombo>();
			List<StrutturaCombo> testoIntroduttivoEng=new ArrayList<StrutturaCombo>();
			for (int r=0;  r < datiFormulaIntroduttiva.length; r++)
			{
				if (datiFormulaIntroduttiva[r].getCodice3().equals("ITA") )
				{
					testoIntroduttivo.add(new StrutturaCombo(datiFormulaIntroduttiva [r].getCodice4(), datiFormulaIntroduttiva [r].getCodice2()));
				}
				else
				{
					testoIntroduttivoEng.add(new StrutturaCombo(datiFormulaIntroduttiva [r].getCodice4(), datiFormulaIntroduttiva [r].getCodice2()));
				}
			}
			for (int r=0;  r < testoIntroduttivo.size() && r<6; r++)
			{
				testoIntroduttivo_lst [r] = testoIntroduttivo.get(r);
			}
			for (int r=0;  r < testoIntroduttivoEng.size() && r<6; r++)
			{
				testoIntroduttivoEng_lst [r] = testoIntroduttivoEng.get(r);
			}
			datiConfigBO.setTestoIntroduttivo(testoIntroduttivo_lst);
			datiConfigBO.setTestoIntroduttivoEng(testoIntroduttivoEng_lst);
			// FINE FORMULA INTROD

			// TESTO OGGETTO
			StrutturaQuater[]  datiTestoOggetto=(StrutturaQuater[]) ricConfigurazione.get("testoOggetto");

			List<StrutturaCombo> testoOggetto=new ArrayList<StrutturaCombo>();
			List<StrutturaCombo> testoOggettoEng=new ArrayList<StrutturaCombo>();

			StrutturaCombo[] testoOggetto_lst=new StrutturaCombo[6];  // testi in italiano
			StrutturaCombo[] testoOggettoEng_lst=new StrutturaCombo[6];  // testi in inglese


			for (int r=0;  r < datiTestoOggetto.length; r++)
			{
				if (datiTestoOggetto[r].getCodice3().equals("ITA") )
				{
					testoOggetto.add(new StrutturaCombo(datiTestoOggetto [r].getCodice4(),datiTestoOggetto [r].getCodice2() ));
				}
				else
				{
					testoOggettoEng.add(new StrutturaCombo(datiTestoOggetto [r].getCodice4(), datiTestoOggetto [r].getCodice2()));
				}
			}

			for (int r=0;  r < testoOggetto.size() && r<6; r++)
			{
				testoOggetto_lst [r] = testoOggetto.get(r);
			}
			for (int r=0;  r < testoOggettoEng.size() && r<6; r++)
			{
				testoOggettoEng_lst [r] = testoOggettoEng.get(r);
			}

			datiConfigBO.setTestoOggetto(testoOggetto_lst);
			datiConfigBO.setTestoOggettoEng(testoOggettoEng_lst);

			// FINE TESTO OGGETTO


			// VALIDAZIONE

			datiConfigBO.validate();

			// AREE ISBD
			//areaTit; // check
			//areaEdi; // check
			//areaNum; // check
			//areaPub; // check

			String[] areeIsbd =new String[7];
			String  areaTit=(String) ricConfigurazione.get("areaTit");
			String  areaEdi=(String) ricConfigurazione.get("areaEdi");
			String  areaNum=(String) ricConfigurazione.get("areaNum");
			String  areaPub=(String) ricConfigurazione.get("areaPub");


			for(int i=0; i<6; i++)
			{
				areeIsbd[i]="0";
				if (areaTit!=null && i==0 && areaTit.equals("si"))
				{
					areeIsbd[i]="1";
				}
				if (areaEdi!=null && i==1 && areaEdi.equals("si"))
				{
					areeIsbd[i]="1";
				}
				if (areaNum!=null && i==2 && areaNum.equals("si"))
				{
					areeIsbd[i]="1";
				}
				if (areaPub!=null && i==3 && areaPub.equals("si"))
				{
					areeIsbd[i]="1";
				}
			}
			datiConfigBO.setAreeIsbd(areeIsbd);
			// FINE AREE ISBD

			String  logo=(String) ricConfigurazione.get("logo");
			String  imgLogo=(String) ricConfigurazione.get("imgLogo");
			datiConfigBO.setPresenzaLogoImg(false);
			datiConfigBO.setNomeLogoImg("");
			if (logo!=null && logo.equals("si"))
			{
				datiConfigBO.setPresenzaLogoImg(true);
				if (imgLogo!=null && imgLogo.trim().length()>0)
				{
					datiConfigBO.setNomeLogoImg(imgLogo);
				}
			}

			String  firmaDigit=(String) ricConfigurazione.get("firmaDigit");
			String  imgFirma=(String) ricConfigurazione.get("imgFirma");
			datiConfigBO.setPresenzaFirmaImg(false);
			datiConfigBO.setNomeFirmaImg("");
			if (firmaDigit!=null && firmaDigit.equals("si"))
			{
				datiConfigBO.setPresenzaFirmaImg(true);
				if (imgFirma!=null && imgFirma.trim().length()>0)
				{
					datiConfigBO.setNomeFirmaImg(imgFirma);
				}
			}

			String  prezzo=(String) ricConfigurazione.get("prezzo");
			datiConfigBO.setPresenzaPrezzo(false);
			if (prezzo!=null && prezzo.equals("si"))
			{
				datiConfigBO.setPresenzaPrezzo(true);
			}

			String  ristampa=(String) ricConfigurazione.get("ristampa");
			datiConfigBO.setIndicaRistampa(false);
			if (ristampa!=null && ristampa.equals("si"))
			{
				datiConfigBO.setIndicaRistampa(true);
			}

			String  numerazBuono=(String) ricConfigurazione.get("numerazBuono");
			datiConfigBO.setNumAutomatica(false);
			if (numerazBuono!=null && numerazBuono.equals("automatico"))
			{
				datiConfigBO.setNumAutomatica(true);
			}


			String  numProt=(String) ricConfigurazione.get("numProt");
			datiConfigBO.setEtichettaProtocollo(false);
			if (numProt!=null && numProt.equals("si"))
			{
				datiConfigBO.setEtichettaProtocollo(true);
			}

			String  dataProt=(String) ricConfigurazione.get("dataProt");
			datiConfigBO.setEtichettaDataProtocollo(false);
			if (dataProt!=null && dataProt.equals("si"))
			{
				datiConfigBO.setEtichettaDataProtocollo(true);
			}

			String  indicazioneRinnovo=(String) ricConfigurazione.get("indicazioneRinnovo");
			datiConfigBO.setTipoRinnovo("N");
			if (indicazioneRinnovo!=null && indicazioneRinnovo.equals("originario"))
			{
				datiConfigBO.setTipoRinnovo("O");
			} else if (indicazioneRinnovo!=null && indicazioneRinnovo.equals("precedente"))
			{
				datiConfigBO.setTipoRinnovo("P");
			} else if (indicazioneRinnovo!=null && indicazioneRinnovo.equals("nessuno"))
			{
				datiConfigBO.setTipoRinnovo("N");
			}

		// FINE AGGIORNAMENTO DEI NUOVI CAMPI

		ricConfigurazione.set("numAutomatica",datiConfigBO.isNumAutomatica());
		ricConfigurazione.set(DATI_CONFIG_BO,datiConfigBO);

		ricConfigurazioneNorm = (ConfigurazioneBOForm) form;
		ricConfigurazioneNorm.setConferma(false);
		ricConfigurazioneNorm.setDisabilitaTutto(false);
		//
		if (!this.modificaConfigurazione(datiConfigBO)) {
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

		return mapping.getInputForward();

		}	catch (ValidationException ve) {
			SbnErrorTypes error = ve.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, ve);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			ricConfigurazioneNorm.setConferma(false);
			ricConfigurazioneNorm.setDisabilitaTutto(false);
			return mapping.getInputForward();


		//return mapping.findForward("salva");
	 	} catch (Exception e) {
			ricConfigurazioneNorm.setConferma(false);
			ricConfigurazioneNorm.setDisabilitaTutto(false);
	 		return mapping.getInputForward();
	 	}

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ConfigurazioneBOForm ricConfigurazione = (ConfigurazioneBOForm) form;
		ConfigurazioneBOForm ricConfigurazioneNorm = (ConfigurazioneBOForm) form;

		try {
		// Viene settato il token per le transazioni successive


			this.saveToken(request);
			ricConfigurazione.setConferma(false);
			ricConfigurazioneNorm.setDisabilitaTutto(false);

			return mapping.getInputForward();
		 	} catch (Exception e) {
				ricConfigurazione.setConferma(false);
				ricConfigurazioneNorm.setDisabilitaTutto(false);
		 		return mapping.getInputForward();
			}
	}

	private StrutturaTerna[] loadIntest() throws Exception {
		// caricamento dati intestazione
		StrutturaTerna[] listaIntest = new StrutturaTerna[1];
		//listaIntest[0]=new StrutturaTerna("1","Biblioteca Principale dell'ICCU","");
		listaIntest[0]=new StrutturaTerna("1","","");
		return listaIntest;
	}

	private ConfigurazioneBOVO caricaIntestBibl(ConfigurazioneBOVO configurazione, String polo, String biblio) throws Exception {
		// caricamento dati intestazione
		StrutturaTerna[] listaIntest = new StrutturaTerna[3];
		//listaIntest[0]=new StrutturaTerna("1","Biblioteca Principale dell'ICCU","");

		BibliotecaVO eleBibl= this.loadBiblioteca(polo, biblio);
		boolean trovatoQualcosa=false;
		if (eleBibl!=null)
		{
			listaIntest[0]=new StrutturaTerna("1","n.s",""); // riservato alla denominazione
			listaIntest[1]=new StrutturaTerna("2","n.s",""); // riservato all'indirizzo
			listaIntest[2]=new StrutturaTerna("3","n.s",""); // riservato al cap + località

			if (eleBibl.getNom_biblioteca()!=null && eleBibl.getNom_biblioteca().trim().length()>0 )
			{
				trovatoQualcosa=true;
				listaIntest[0]=new StrutturaTerna("1",eleBibl.getNom_biblioteca().trim(),"");
			}

			if (eleBibl.getIndirizzo()!=null && eleBibl.getIndirizzo().trim().length()>0 )
			{
				trovatoQualcosa=true;
				listaIntest[1]=new StrutturaTerna("1",eleBibl.getIndirizzo().trim(),"");
			}

			if ((eleBibl.getCap()!=null && eleBibl.getCap().trim().length()>0) || (eleBibl.getLocalita()!=null && eleBibl.getLocalita().trim().length()>0) )
			{
				trovatoQualcosa=true;
				String secondaRiga="";
				if ((eleBibl.getCap()!=null && eleBibl.getCap().trim().length()>0) )
				{
					secondaRiga=eleBibl.getCap().trim() + " ";
				}
				listaIntest[2]=new StrutturaTerna("2",secondaRiga + eleBibl.getLocalita().trim(),"");
			}
			if (trovatoQualcosa)
			{
				configurazione.setListaDatiIntestazione(listaIntest);
			}
		}
		return configurazione;
	}


	private StrutturaQuater[] loadFormula() throws Exception {
		// caricamento dati formula introduttiva
		StrutturaQuater[] listaFormula = new StrutturaQuater[1];
		//listaIntest[0]=new StrutturaTerna("1","Biblioteca Principale dell'ICCU","");
		listaFormula[0]=new StrutturaQuater("1","form intro","IT", "L");
		return listaFormula;
	}

	private StrutturaQuater[] loadOggetto() throws Exception {
		// caricamento dati formula introduttiva
		StrutturaQuater[] listaOggetto = new StrutturaQuater[1];
		//listaIntest[0]=new StrutturaTerna("1","Biblioteca Principale dell'ICCU","");
		listaOggetto[0]=new StrutturaQuater("1","testo ogg","IT", "L");
		return listaOggetto;
	}


	private StrutturaTerna[] loadFineStp() throws Exception {
		StrutturaTerna[] listaFineStp = new StrutturaTerna[1];
		listaFineStp[0]=new StrutturaTerna("1","","");

		//StrutturaTerna[] listaFineStp = new StrutturaTerna[2];
		//listaFineStp[0]=new StrutturaTerna("1","IL DIRETTORE","");
		//listaFineStp[1]=new StrutturaTerna("2","(Dott. Marco Rossi)","");
		return listaFineStp;
	}

	private ConfigurazioneBOVO loadConfigBO() throws Exception {
		//ConfigurazioneBOVO confBO=new ConfigurazioneBOVO("", "IC", true,loadIntest(), loadFineStp());
		ConfigurazioneBOVO confBO=new ConfigurazioneBOVO("", "", true,loadIntest(), loadFineStp());
		//this.ricConfigurazione.setDatiConfigBO(confBO);
		return confBO;
	}

	private boolean modificaConfigurazione(ConfigurazioneBOVO configurazione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaConfigurazione(configurazione);
		return valRitorno;
	}

	private ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneBOVO configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazione(configurazione);
		return configurazioneTrovata;
	}

	private BibliotecaVO loadBiblioteca(String codicePolo, String codiceBib) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		BibliotecaVO eleBibl = factory.getSistema().getBiblioteca(codicePolo, codiceBib);

		return eleBibl;
	}

	private List renumera(List listaAlterata) throws Exception {

		int i;
		for (i=0;  i < listaAlterata.size(); i++)
		{
			StrutturaTerna elemFS = (StrutturaTerna) listaAlterata.get(i);
			// renumera lista
			elemFS.setCodice1(String.valueOf(i+1));
		}
		return listaAlterata;
	}

	private List renumeraBis(List listaAlterata) throws Exception {

		int i;
		for (i=0;  i < listaAlterata.size(); i++)
		{
			StrutturaQuater elemFS = (StrutturaQuater) listaAlterata.get(i);
			// renumera lista
			elemFS.setCodice1(String.valueOf(i+1));
		}
		return listaAlterata;
	}


	private List caricaDatiIntest(List listaNuova) throws Exception {

		int i;
		for (i=0;  i < listaNuova.size(); i++)
		{
			StrutturaTerna elemFS = (StrutturaTerna) listaNuova.get(i);
			// carica lista

			elemFS.setCodice1(String.valueOf(i+1));

		}
		return listaNuova;
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm ricConfigurazione = (DynaValidatorForm) form;
		//ConfigurazioneBOForm ricConfigurazioneNorm = (ConfigurazioneBOForm) form;
		// gestione upload immagine marca
		FormFile formFile =  (FormFile) ricConfigurazione.get("fileIdList");
		if (formFile != null && formFile.getFileSize() > 0)
			return gestioneUploadFile(mapping, ricConfigurazione, request, "logo");
		return mapping.getInputForward();
	}

	public ActionForward caricaFileFirma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm ricConfigurazione = (DynaValidatorForm) form;
		// gestione upload immagine marca

		FormFile formFile =  (FormFile) ricConfigurazione.get("fileIdList");
		if (formFile != null && formFile.getFileSize() > 0)
			return gestioneUploadFile(mapping, ricConfigurazione, request, "logo");

		FormFile formFileFirma =  (FormFile) ricConfigurazione.get("fileIdListFirma");
		if (formFileFirma != null && formFileFirma.getFileSize() > 0)
			return gestioneUploadFile(mapping, ricConfigurazione, request, "firma");

		return mapping.getInputForward();
	}

	private ActionForward gestioneUploadFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, String tipoFile) {

		DynaValidatorForm ricConfigurazione = (DynaValidatorForm) form;
		String fileName = null;	//path+nome
		//String fileName1 = null;	//path+nome
		String fileNameLocal = null;	//path+nome - file in locale
		try {
			byte[] imgbuf;
			FormFile formFileAttiva=(FormFile) ricConfigurazione.get("fileIdList");
			if (tipoFile.equals("firma"))
			{
				formFileAttiva=(FormFile) ricConfigurazione.get("fileIdListFirma");
			}
			imgbuf = formFileAttiva.getFileData();
			//InputStream imgbuf2 = ((FormFile)ricConfigurazione.get("fileIdList")).getInputStream();
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgbuf));
			//InputStream inputStream = new BufferedInputStream(((FormFile)ricConfigurazione.get("fileIdList")).getInputStream());

			if (image == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.ImgNotValid"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				fileNameLocal = Navigation.getInstance(request).getUserTicket() + formFileAttiva.getFileName();
				//fileName ="C:/Eclipse3.1/SBNWeb/WebContent/download/" + fileNameLocal;
				fileName=StampeUtil.getBatchFilesPath()+ File.separator + fileNameLocal;
				outputStream = new FileOutputStream(new File(fileName));
				inputStream = new BufferedInputStream(formFileAttiva.getInputStream());
				int count;
				byte buf[] = new byte[4096];
				while ((count = inputStream.read(buf)) > -1) {
					outputStream.write(buf, 0, count);
				}
			}
			finally {
				if (inputStream!=null) inputStream.close();
				if (outputStream!=null) outputStream.close();
			}
		} catch (IOException e) {
			log.error("", e);
		}
		if (fileName!=null){
			//Imposta il nuovo path+nome del file caricato sul VO
		//	((EsportaForm)form).getEsportaGen().setFileIdListPathName(fileName);
			// Imposta il path+nome originario del file caricato sul VO
		//	((EsportaForm)form).getEsportaGen().setFileIdListPathNameLocal(fileNameLocal);
			// Imposta messaggio con il nome originario del file caricato
			//fileName=fileName.replace("\\","\\\\");
			if (tipoFile.equals("firma"))
			{
				//((ConfigurazioneBOForm)form).setImgLogoPathCompleto(fileName);
				ricConfigurazione.set("imgFirma", fileNameLocal);
				ricConfigurazione.set("firmaDigit","si");
				//ricConfigurazione.set("imgFirma", (String) fileName);
			}
			if (tipoFile.equals("logo"))
			{
				//((ConfigurazioneBOForm)form).setImgLogoPathCompleto(fileName);
				ricConfigurazione.set("imgLogo", fileNameLocal);
				//ricConfigurazione.set("imgLogo", (String) fileName);
				ricConfigurazione.set("logo","si");
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
			"errors.acquisizioni.operOkConParametro","<br>File "+fileNameLocal+" caricato correttamente"));

			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();

	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals(NavigazioneAcquisizioni.GESTIONE) ) {
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_PARAMETRI_BIBLIOTECA, utente.getCodPolo(), utente.getCodBib(), null);
				return true;

			} catch (Exception e) {
				log.error("", e);
			}
		}

		return false;
	}


	public ActionForward aggiungiFormIntrR(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ConfigurazioneBOForm currentForm = (ConfigurazioneBOForm) form;
		ConfigurazioneBOVO config = (ConfigurazioneBOVO) currentForm.get(DATI_CONFIG_BO);
		List<FormulaIntroOrdineRVO> fio = ValidazioneDati.coalesce(config.getFormulaIntroOrdineR(),
				new ArrayList<FormulaIntroOrdineRVO>() );
		FormulaIntroOrdineRVO item = new FormulaIntroOrdineRVO();
		int pos = fio.size() + 1;
		item.setProgr(pos);
		fio.add(item);
		config.setFormulaIntroOrdineR(fio);
		currentForm.set(SELECTED_FORMULA_INTRO_ORDINE_R, item.getRepeatableId());
		currentForm.set("item", fio);

		return mapping.getInputForward();
	}

	public ActionForward cancellaFormIntrR(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ConfigurazioneBOForm currentForm = (ConfigurazioneBOForm) form;
		Integer selected = (Integer) currentForm.get(SELECTED_FORMULA_INTRO_ORDINE_R);
		if (selected == null)
			return mapping.getInputForward();

		ConfigurazioneBOVO config = (ConfigurazioneBOVO) currentForm.get(DATI_CONFIG_BO);
		List<FormulaIntroOrdineRVO> fio = ValidazioneDati.coalesce(config.getFormulaIntroOrdineR(),
				new ArrayList<FormulaIntroOrdineRVO>() );
		int idx = UniqueIdentifiableVO.indexOfRepeatableId(selected, fio);

		FormulaIntroOrdineRVO old = fio.remove(idx);
		int pos = old.getProgr();
		for (int i = idx; i < fio.size(); i++)
			fio.get(i).setProgr(pos++);

		config.setFormulaIntroOrdineR(fio);
		currentForm.set("item", fio);

		return mapping.getInputForward();
	}

}
