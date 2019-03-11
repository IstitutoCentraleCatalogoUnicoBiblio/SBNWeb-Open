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
package it.iccu.sbn.web.actions.gestionestampe.fornitori;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBean;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaFornitoriDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaFornitoriVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaTerna;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.fornitori.StampaFornitoriForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class StampaFornitoriAction extends ReportAction{ // LookupDispatchAction {
	private StampaFornitoriForm stampaFornitoriForm;
	private CaricamentoCombo carCombo = new CaricamentoCombo();
	private AcquisizioniBean acqBean;


	private void loadTpForn() throws Exception {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			stampaFornitoriForm.setListaTpForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoPartner()));
	}
//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom("B","Biblioteca");
//		lista.add(elem);
//		elem = new StrutturaCom("D","Dipartimento");
//		lista.add(elem);
//		elem = new StrutturaCom("C","Donatore");
//		lista.add(elem);
//		elem = new StrutturaCom("E","Editore commerciale");
//		lista.add(elem);
//		elem = new StrutturaCom("G","Editore non commerciale");
//		lista.add(elem);
//		elem = new StrutturaCom("F","Fornitore");
//		lista.add(elem);
//		this.stampaFornitoriForm.setListaTpForn(lista);

    private void loadProv() throws Exception {
    		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    		stampaFornitoriForm.setListaProv(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceProvincia()));
//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom("RM","Roma");
//		lista.add(elem);
//		elem = new StrutturaCom("MI","Milano");
//		lista.add(elem);
//		elem = new StrutturaCom("NA","Napoli");
//		lista.add(elem);
//		this.stampaFornitoriForm.setListaProv(lista);
	}

	private void loadPaese() throws Exception {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			stampaFornitoriForm.setListaPaese(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));

//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom("D","DATABASE");
//		lista.add(elem);
//		elem = new StrutturaCom("C","C - Chiuso");
//		lista.add(elem);
//		elem = new StrutturaCom("I","I - Spedito");
//		lista.add(elem);
//		elem = new StrutturaCom("N","N - Annullato");
//		lista.add(elem);
//		elem = new StrutturaCom("R","R - Rinnovato");
//		lista.add(elem);
//		this.stampaFornitoriForm.setListaPaese(lista);
	}

	private void loadProfAcq(HttpServletRequest request) throws Exception {
			String polo = this.stampaFornitoriForm.getCodPolo();
			String bibl = this.stampaFornitoriForm.getCodBib();
			StrutturaCombo strutNull = new StrutturaCombo("", "");
			ListaSuppProfiloVO lpVO= new  ListaSuppProfiloVO (polo, bibl, strutNull, strutNull, strutNull, strutNull,  null, "");
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List listaProfili=null;
			try {
				listaProfili = factory.getGestioneAcquisizioni().getRicercaListaProfili(lpVO);//acqBean.getRicercaListaProfili(lpVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (listaProfili!=null && listaProfili.size() > 0)
			{
				StrutturaProfiloVO struttDefault = new StrutturaProfiloVO();
				struttDefault.setCodBibl(bibl);
				struttDefault.setCodPolo(polo);
				List lisForn = new ArrayList();
				StrutturaTerna struttTerna  = new StrutturaTerna();
				struttTerna.setCodice1("");
				struttTerna.setCodice2("");
				struttTerna.setCodice3("");
				lisForn.add(struttTerna);
				struttDefault.setListaFornitori(lisForn);
				it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo strutCombo = new it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo();
				strutCombo.setCodice("");
				strutCombo.setDescrizione("");
				struttDefault.setProfilo(strutCombo);
				listaProfili.add(0, struttDefault);
				this.stampaFornitoriForm.setListaProfAcq(listaProfili);
			}
			else{
				List arr = new ArrayList();
				StrutturaProfiloVO prof = new StrutturaProfiloVO(polo, bibl, strutNull, strutNull, strutNull, strutNull, arr, "");
				//String polo, String bibl, StrutturaCombo prof, StrutturaCombo sez, StrutturaCombo lin, StrutturaCombo pae, List listProf , String tipoVar
//				String prof = "";
				arr.add(prof);
				this.stampaFornitoriForm.setListaProfAcq(arr);
			}
	}
//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Letteratura italiana");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Periodici italiani");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Letteratura inglese");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Periodici inglesi");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Storia");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Storia dell'Umbria");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Letteratura francese");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Fantasy");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Letteratura locale");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Profilo");
//		lista.add(elem);
//		elem = new StrutturaCom(" ","Periodici francesi");
//		lista.add(elem);
//		this.stampaFornitoriForm.setListaProfAcq(lista);


	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");
//		map.put("button.stampa","stampa");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		stampaFornitoriForm = (StampaFornitoriForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA, 10, "codBib");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession httpSession = request.getSession();
		stampaFornitoriForm = (StampaFornitoriForm) form;
		//Settaggio biblioteche
		stampaFornitoriForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		stampaFornitoriForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
		stampaFornitoriForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());

		//Settaggio biblioteche
/*		if(request.getAttribute("codBib") != null ) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			stampaBollettarioForm.setCodBib((String)request.getAttribute("codBib"));
		} */
		BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
		if (bibScelta!=null && bibScelta.getCod_bib()!=null && stampaFornitoriForm.getCodBib()!=null )
		{
			stampaFornitoriForm.setCodBib(bibScelta.getCod_bib());
			stampaFornitoriForm.setDescrBib(bibScelta.getNom_biblioteca());
		}


		this.loadTpForn();
		this.loadPaese();
		this.loadProv();
		this.loadProfAcq(request);
		List lista = new ArrayList();
		lista = this.loadTipiOrdinamento();
		stampaFornitoriForm.setListaTipiOrdinamento(lista);
		stampaFornitoriForm.setCodForn("");;
		stampaFornitoriForm.setNomForn("");
		stampaFornitoriForm.setTpForn("");
		stampaFornitoriForm.setPaese("");
		stampaFornitoriForm.setProv("");
		stampaFornitoriForm.setProfAcq("");
//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		stampaFornitoriForm.setElencoModelli(getElencoModelli());
		stampaFornitoriForm.setTipoFormato(TipoStampa.HTML.name());
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		stampaFornitoriForm = (StampaFornitoriForm) form;
		try {
			request.setAttribute("parametroPassato", stampaFornitoriForm.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
//			return mapping.findForward("indietro");//rosa
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		stampaFornitoriForm = (StampaFornitoriForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();//return null;
		}

		try {
//			// validazione
			if (stampaFornitoriForm.getCodBib()!=null && stampaFornitoriForm.getCodBib().length()!=0)
			{
				if (stampaFornitoriForm.getCodBib().length()>3)
				{
					throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
				}
			}
			if (stampaFornitoriForm.getCodForn()!=null && stampaFornitoriForm.getCodForn().length()!=0)
			{
				if (!this.strIsNumeric(String.valueOf(stampaFornitoriForm.getCodForn())))
				{
					throw new ValidationException("ordineerroreCodFornNumerico",
							ValidationExceptionCodici.ordineerroreCodFornNumerico);
				}
			}
			if (stampaFornitoriForm.getNomForn()!=null && stampaFornitoriForm.getNomForn().trim().length()!=0)
			{
				if (strIsNumeric(stampaFornitoriForm.getNomForn()))
				{
					throw new ValidationException("ordinierroreCampoNomeFornAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
				}
				if (stampaFornitoriForm.getNomForn().trim().length()>50)			{
					throw new ValidationException("ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
				}
			}
			String tipoPaese=((StampaFornitoriForm)form).getPaese();
			String tipoProvincia = ((StampaFornitoriForm)form).getProv();
			String tipoMod= ((StampaFornitoriForm)form).getTipoModello();
			String ordinamFile = ((StampaFornitoriForm)form).getTipoOrdinamSelez();
//			boolean orderByCode = false;
//			boolean orderByName = false;
//			if((tipoMod.startsWith("default_fornitori_byCode")) && (ordinamFile.equals("CA")||(ordinamFile.equals("CD")))){
//				orderByCode= true;
//			}else if((tipoMod.startsWith("default_fornitori_byName")) && (ordinamFile.equals("FA")||(ordinamFile.equals("FD")))){
//				orderByName= true;
//
//			}
//			if(((!tipoPaese.equalsIgnoreCase("IT")) && (!orderByCode)) || ((!tipoPaese.equalsIgnoreCase("IT")) &&(!orderByName))) {  //tipoPaese!=null && ) || ((!orderByCode) || (!orderByName)))
//
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.fornitori.stampa"));
//				this.saveErrors(request, errors);
//				return mapping.getInputForward();
//			}else if((!orderByCode) || (!orderByName)){//errors.fornitori.scelta_ordinamento.stampa
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.fornitori.tipordinamento.stampa"));
//				this.saveErrors(request, errors);
//				return mapping.getInputForward();
//			}else if(!tipoPaese.equalsIgnoreCase("IT")){
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.fornitori.p.stampa"));
//				this.saveErrors(request, errors);
//				return mapping.getInputForward();

			// Bag MAntis collaudo 5232 - inserito corretto diagnostico e controllo x codice paese non valorizzato nella
			// mappa di richiesta stampa fornitori.
			if(tipoPaese.equals("") && tipoProvincia.length() > 0){ //(tipoProvincia!= null)
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.stampa.IndicarePaese"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
//			errors.stampa.IndicarePaese

			if(!(tipoPaese.equalsIgnoreCase("IT")) && !(tipoProvincia.equals(""))){ //(tipoProvincia!= null)
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.stampa.sceltaPaese"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			} else {
				StampaFornitoriVO stampaFornitoriVO = new StampaFornitoriVO();
				if (stampaFornitoriForm.getCodForn() != null)
					stampaFornitoriVO.setCodiceFornitore(stampaFornitoriForm.getCodForn());
				if (stampaFornitoriForm.getNomForn() != null)
					stampaFornitoriVO.setNomeFornitore(stampaFornitoriForm.getNomForn());
				if (stampaFornitoriForm.getPaese() != null)
					stampaFornitoriVO.setPaese(stampaFornitoriForm.getPaese());
				if (stampaFornitoriForm.getProfAcq() != null)
					stampaFornitoriVO.setProfAcquisti(stampaFornitoriForm.getProfAcq());
				if (stampaFornitoriForm.getProv() != null)
					stampaFornitoriVO.setProvincia(stampaFornitoriForm.getProv());
				if (stampaFornitoriForm.getTpForn() != null)
					stampaFornitoriVO.setTipoFornitore(stampaFornitoriForm.getTpForn());
				if (stampaFornitoriForm.isRicercaLocale())
					stampaFornitoriVO.setRicercaLocale("1");
				else
					stampaFornitoriVO.setRicercaLocale("0");
				String polo = Navigation.getInstance(request).getUtente().getCodPolo();
				String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
				stampaFornitoriVO.setPolo(polo);
				stampaFornitoriVO.setBiblioteca(bibl);
				UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
				String utente= user.getUserId();

				List inputForStampeService=new ArrayList();
				inputForStampeService.add(stampaFornitoriVO);

				String tipoFormato=stampaFornitoriForm.getTipoFormato();

				request.setAttribute("DatiVo", inputForStampeService);//  ListaBuoniOrdine
				request.setAttribute("TipoFormato", tipoFormato);

				String fileJrxml = stampaFornitoriForm.getTipoModello()+".jrxml";
				String basePath=this.servlet.getServletContext().getRealPath(File.separator);

//				 percorso dei file template: webroot/jrxml/\tab\tab\tab\par
				String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
				//NB: Se voglio memorizzare sul server
//				String pathDownload = basePath+File.separator+"download";
				String pathDownload = StampeUtil.getBatchFilesPath();

				//Se voglio memorizzare in locale

//				String percRel=request.getServletPath();
//				StringBuffer percUrl=request.getRequestURL();
//				String percorsoOK = percUrl.toString();
//				int indice = percorsoOK.indexOf(percRel);
//				pathDownload = percorsoOK.substring(0, indice);

				//codice standard inserimento messaggio di richiesta stampa differita
				StampaDiffVO stam = new StampaDiffVO();
				stam.setTipoStampa(tipoFormato);
				stam.setUser(utente);
				stam.setCodPolo(polo);
				stam.setCodBib(bibl);
				stam.setTipoOrdinamento(ordinamFile);
				stam.setParametri(inputForStampeService);
				stam.setTemplate(pathJrxml);
				stam.setDownload(pathDownload);
				stam.setDownloadLinkPath("/");
				stam.setTipoOperazione("STAMPA_FORNITORE");
				UtilityCastor util= new UtilityCastor();
				String dataCorr = util.getCurrentDate();
				stam.setData(dataCorr);
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
/*
				String idMessaggio = factory.getStampeOnline().stampaFornitori(stam);

				ActionMessages errors = new ActionMessages();
				idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
				errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
				this.saveErrors(request, errors);
*/
				// nuova gestione  batch

				StampaFornitoriDiffVO richiesta = new StampaFornitoriDiffVO();
				richiesta.setCodPolo(polo);
				richiesta.setCodBib(bibl);
				richiesta.setUser(utente);
				richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA);
				richiesta.setParametri(inputForStampeService);
				richiesta.setTipoOrdinamento(ordinamFile);
				richiesta.setStampavo(stam);
				String ticket=Navigation.getInstance(request).getUserTicket();

				String s =  null;
				try {
					s = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, richiesta, null);
				} catch (ApplicationException e) {
					if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


				if (s == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.acquisizioni.prenotFallita"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.acquisizioni.prenotOk", s.toString()));
				this.saveErrors(request, errors);


//				return mapping.getInputForward();
			}
		}catch (Exception e){
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.stampaFornitori.inserimento"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
	}
		return mapping.getInputForward();//return null;
	}

//	private void stampa(String fileJrxml,String tipoFormato,HttpServletResponse response, DatiFornitoreVO datiF) throws Exception
//	{
//		List inputForStampeService=new ArrayList();
//
//		for (int k = 1; k<4; k++){
//			FornitoreVO	vo	 = new FornitoreVO ("", "", "codFornitore", "nomeFornitore", "unitaOrg", "indirizzo", "cap", "citta", "casellaPostale", "telefono", "fax", "", "partitaIva", "codiceFiscale", "email", " paese", "tipoPForn", "provForn",  "tipoVar", datiF, "biblFornitore");
//			inputForStampeService.add(vo);
//		}
//
//		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
//
////		percorso dei file template: webroot/jrxml/
////		percorso dei file jasper compilati: webroot/jasper/
////		String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
////		percorso dei file template: webroot/jrxml/
//		String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
//
//		SbnStampaFornitori sbn = new SbnStampaFornitori(pathJrxml);
//		sbn.setFormato(TipoStampa.toTipoStampa(tipoFormato));
//		byte [] streamByte = sbn.stampa(inputForStampeService, pathJrxml);
//
//		ServletOutputStream servletOutputStream =response.getOutputStream();
//		response = getContentTypeResponse(TipoStampa.toTipoStampa(tipoFormato), response);
//		servletOutputStream.write(streamByte);
//		servletOutputStream.flush();
//		servletOutputStream.close();
//	}


	public HttpServletResponse getContentTypeResponse(TipoStampa tipoFormato, HttpServletResponse response){
		String tipo = "";
		switch (tipoFormato) {
		case PDF:
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=prova.pdf");

			break;
	  case RTF:
			response.setContentType("application/rtf");
			response.setHeader("Content-disposition", "inline; filename=prova.rtf");

	        break;
	  case XLS:
			response.setContentType("application/msexcel");
			response.setHeader("Content-disposition", "inline; filename=prova.xls");

			break;
	  case HTML:
			response.setContentType("text/html");

			 break;
	  case CSV:
			response.setContentType("text/csv");
			response.setHeader("Content-disposition", "inline; filename=prova.csv");

	        break;
	  default:
			response.setContentType("application/pdf");
	}
		return response;
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private List loadTipiOrdinamento() throws Exception {
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("6","Codice  Ascendente");
		lista.add(rec);
		rec = new CodiceVO("7","Codice  Discendente");
		lista.add(rec);
		rec = new CodiceVO("3","Fornitore  Ascendente");
		lista.add(rec);
		rec = new CodiceVO("8","Fornitore  Discendente");
		lista.add(rec);
		return lista;
	}


	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		stampaFornitoriForm = (StampaFornitoriForm) form;
		boolean searchOtherBib = stampaFornitoriForm.isRicercaLocale();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!stampaFornitoriForm.isSessione())
				{
					stampaFornitoriForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			resetToken(request);
			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("biblio",stampaFornitoriForm.getCodBib());
			request.setAttribute("descr",stampaFornitoriForm.getDescrBib());
			if(searchOtherBib){
				return mapping.getInputForward();
			}
			else{
				return mapping.findForward("lenteBiblio");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
	}

	private String calcolaCodBib(HttpServletRequest request)throws Exception {
		String bibl;
		String bibl1 = Navigation.getInstance(request).getUtente().getCodBib();
		UserVO ute= (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
		String bibl2 = ute.getCodBib();
		if(!(bibl2.equals(bibl1)) || (bibl2==null)){
			bibl = bibl2;
		}else{
			bibl= bibl1;
		}
		return bibl;
	}

	private void getBiblioteca(HttpSession httpSession,List ret, HttpServletRequest request) throws Exception
	{
		/// emulazione da togliere quando imp la funzione di login
		//applico algoritmo per l'identificazione della lista bib
		if (ret.size() == 1) {
			stampaFornitoriForm.setCodBib(((CodiceVO)ret.get(0)).getCodice());
			stampaFornitoriForm.setDescrBib("Biblioteca principale dell'ICCU");
		} else if (ret.size() > 1) {
			//mi chiedo se il codBib della biblio operante è nella lista delle biblioteche
			//prospetto la mappa con codBib, lente e descr  disable=false e carico la lista delle sezioni
			//della prima biblioteca della lista
			stampaFornitoriForm.setCodBib((this.searchBibliotecaOperante(ret,httpSession).getCodice()));
		}else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.stampaFornitori.campoNonImp"));
			this.saveErrors(request, errors);
		}
	}

	private CodiceVO searchBibliotecaOperante(List listaBiblioteca,HttpSession session)
	{
		CodiceVO ret = (CodiceVO)listaBiblioteca.get(0);
		String codBiblioOperante = (String) session.getAttribute("CODICE");
		for(int index = 0; index<listaBiblioteca.size(); index++)
		{
			if(((CodiceVO)listaBiblioteca.get(index)).getCodice().equals(codBiblioOperante))
			{
				ret = (CodiceVO)listaBiblioteca.get(index);
				break;
			}
		}
		return ret;
	}


	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			request.setAttribute("stampa", "stampaOnLine");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return unspecified(mapping, form, request, response);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("STAMPA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				// 04.12.08 e.printStackTrace();
				log.error(e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		return false;
	}

}
