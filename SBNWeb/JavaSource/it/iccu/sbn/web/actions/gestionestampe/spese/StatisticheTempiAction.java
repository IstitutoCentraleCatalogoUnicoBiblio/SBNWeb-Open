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
package it.iccu.sbn.web.actions.gestionestampe.spese;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBean;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSpeseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaStatisticheDiffVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.spese.StatisticheTempiForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
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


public class StatisticheTempiAction extends ReportAction  implements SbnAttivitaChecker  { // LookupDispatchAction {
	private StatisticheTempiForm StatisticheTempiForm;
	private CaricamentoCombo carCombo = new CaricamentoCombo();
	private AcquisizioniBean acqBean;




	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");
//		map.put("button.stampa","stampa");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}



	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession httpSession = request.getSession();
		StatisticheTempiForm = (StatisticheTempiForm) form;
		StatisticheTempiForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		StatisticheTempiForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
		StatisticheTempiForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());

		//Settaggio biblioteche
/*		if(request.getAttribute("codBib") != null ) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			StatisticheTempiForm.setCodBib((String)request.getAttribute("codBib"));
		}
*/		BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
		if (bibScelta!=null && bibScelta.getCod_bib()!=null && StatisticheTempiForm.getCodBib()!=null )
		{
			StatisticheTempiForm.setCodBib(bibScelta.getCod_bib());
			StatisticheTempiForm.setDescrBib(bibScelta.getNom_biblioteca());
		}
		this.loadTipoOrdine(StatisticheTempiForm);
		List lista = new ArrayList();
		lista = this.loadTipiOrdinamento();
		StatisticheTempiForm.setListaTipiOrdinamento(lista);
		StatisticheTempiForm.setTipoRicerca("accessionamento");

/*		StatisticheTempiForm.setCodForn("");;
		StatisticheTempiForm.setNomForn("");
		StatisticheTempiForm.setTpForn("");
		StatisticheTempiForm.setPaese("");
		StatisticheTempiForm.setProv("");
		StatisticheTempiForm.setProfAcq("");
*///		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		StatisticheTempiForm.setElencoModelli(getElencoModelli());
		StatisticheTempiForm.setTipoFormato(TipoStampa.HTML.name());

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			//utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI, utente.getCodPolo(), utente.getCodBib(), null);
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utente.getCodPolo(), utente.getCodBib(), null);

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
		StatisticheTempiForm = (StatisticheTempiForm) form;
		try {
			request.setAttribute("parametroPassato", StatisticheTempiForm.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StatisticheTempiForm = (StatisticheTempiForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            //new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI, 10, "codBib");
	        	new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, 10, "codBib");

	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		StatisticheTempiForm = (StatisticheTempiForm) form;

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		try {
			//utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();//return null;

		}

		try {
//
			// validazione


			if (StatisticheTempiForm.getCodBib()!=null && StatisticheTempiForm.getCodBib().length()!=0)
			{
				if (StatisticheTempiForm.getCodBib().length()>3)
				{
					throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
				}
			}

			if (StatisticheTempiForm.getDataOrdineDa()!=null && StatisticheTempiForm.getDataOrdineDa().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(StatisticheTempiForm.getDataOrdineDa()) && StatisticheTempiForm.getDataOrdineDa().length()==4)
				{
					String strAnnata=StatisticheTempiForm.getDataOrdineDa();
					StatisticheTempiForm.setDataOrdineDa("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(StatisticheTempiForm.getDataOrdineDa())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}
			}
			if (StatisticheTempiForm.getDataOrdineA()!=null && StatisticheTempiForm.getDataOrdineA().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(StatisticheTempiForm.getDataOrdineA()) && StatisticheTempiForm.getDataOrdineA().length()==4)
				{
					String strAnnata=StatisticheTempiForm.getDataOrdineA();
					StatisticheTempiForm.setDataOrdineA("31/12/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(StatisticheTempiForm.getDataOrdineA())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}

			if (StatisticheTempiForm.getAnnoOrdine()!=null && StatisticheTempiForm.getAnnoOrdine().length()!=0)
			{
				// controllo congruenza
				if (!strIsNumeric(StatisticheTempiForm.getAnnoOrdine().trim()))
				{
					throw new ValidationException("sezioneerroreCampoAnnoOrdineNumerico",
							ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
				}

				if (StatisticheTempiForm.getAnnoOrdine().trim().length()!=4)
				{
					throw new ValidationException("sezioneerroreCampoAnnoOrdineEccedente",
							ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
				}

			}
			if (StatisticheTempiForm.getTipoOrdine()!=null && StatisticheTempiForm.getTipoOrdine().length()!=0)
			{
				if (strIsNumeric(StatisticheTempiForm.getTipoOrdine()))
				{
					throw new ValidationException("ordinierroreCampoTipoOrdineAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
				}
				if (StatisticheTempiForm.getTipoOrdine().length()!=1)
				{
					throw new ValidationException("ordinierroreCampoTipoOrdineEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
				}
			}

			ListaSuppSpeseVO stampaRipartoSpeseVO = new ListaSuppSpeseVO();
			if (StatisticheTempiForm.getCodBib() != null)
				stampaRipartoSpeseVO.setCodBibl(StatisticheTempiForm.getCodBib());
			if (StatisticheTempiForm.getDataOrdineDa() != null)
				stampaRipartoSpeseVO.setDataOrdineDa(StatisticheTempiForm.getDataOrdineDa());
			if (StatisticheTempiForm.getDataOrdineA() != null)
				stampaRipartoSpeseVO.setDataOrdineA(StatisticheTempiForm.getDataOrdineA());
			if (StatisticheTempiForm.getAnnoOrdine() != null)
				stampaRipartoSpeseVO.setAnno(StatisticheTempiForm.getAnnoOrdine());
			if (StatisticheTempiForm.getTipoOrdine() != null)
				stampaRipartoSpeseVO.setTipoOrdine(StatisticheTempiForm.getTipoOrdine());

			stampaRipartoSpeseVO.setTipoReport("11");
			if (StatisticheTempiForm.getTipoRicerca().equals("accessionamento"))
			{
				stampaRipartoSpeseVO.setTipoReport("10");
			}


			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
			String denoBibl = Navigation.getInstance(request).getUtente().getBiblioteca();//this.calcolaCodBib(request);

			stampaRipartoSpeseVO.setCodPolo(polo);
			stampaRipartoSpeseVO.setCodBibl(bibl);
			stampaRipartoSpeseVO.setDenoBibl(denoBibl);


			if (StatisticheTempiForm.getCodBib()!=null && StatisticheTempiForm.getCodBib().length()>0)
			{
				stampaRipartoSpeseVO.setCodBibl(StatisticheTempiForm.getCodBib());
				if (StatisticheTempiForm.getDescrBib()!=null && StatisticheTempiForm.getDescrBib().trim().length()>0 )
				{
					stampaRipartoSpeseVO.setDenoBibl(StatisticheTempiForm.getDescrBib());
				}
				else
				{
					stampaRipartoSpeseVO.setDenoBibl("");
				}
			}


			String ordinamFile = StatisticheTempiForm.getTipoOrdinamSelez();
			stampaRipartoSpeseVO.setOrdinamento(ordinamFile);


			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//List<RigheVO> risultato=factory.getGestioneAcquisizioni().ripartoSpese(stampaRipartoSpeseVO);


			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();

			List inputForStampeService=new ArrayList();
			inputForStampeService.add(stampaRipartoSpeseVO);

			String tipoFormato=StatisticheTempiForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);//  ListaBuoniOrdine
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = StatisticheTempiForm.getTipoModello()+".jrxml";
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
			// 07.09.09
			stam.setTipoOperazione("STATISTICHE_TEMPI");
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

/*			String idMessaggio = factory.getStampeOnline().stampaSpese(stam);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));

			this.saveErrors(request, errors);
*/
			// nuova gestione  batch

			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			StampaStatisticheDiffVO richiesta = new StampaStatisticheDiffVO();
			richiesta.setCodPolo(polo);
			richiesta.setCodBib(bibl);
			richiesta.setUser(utente);
			//07.09.09
			//richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI);
			richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE);
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
		}catch (Exception e){
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
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
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI);
			//List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE);

			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private List loadTipiOrdinamento() throws Exception {
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("1","Data ascendente");
		lista.add(rec);
		rec = new CodiceVO("2","Data discendente");
		lista.add(rec);
		return lista;
	}


	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StatisticheTempiForm = (StatisticheTempiForm) form;
		boolean searchOtherBib = StatisticheTempiForm.isRicercaLocale();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!StatisticheTempiForm.isSessione())
				{
					StatisticheTempiForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			resetToken(request);
			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("biblio",StatisticheTempiForm.getCodBib());
			request.setAttribute("descr",StatisticheTempiForm.getDescrBib());
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
			StatisticheTempiForm.setCodBib(((CodiceVO)ret.get(0)).getCodice());
			StatisticheTempiForm.setDescrBib("Biblioteca principale dell'ICCU");
		} else if (ret.size() > 1) {
			//mi chiedo se il codBib della biblio operante è nella lista delle biblioteche
			//prospetto la mappa con codBib, lente e descr  disable=false e carico la lista delle sezioni
			//della prima biblioteca della lista
			StatisticheTempiForm.setCodBib((this.searchBibliotecaOperante(ret,httpSession).getCodice()));
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
	private void loadTipoOrdine(StatisticheTempiForm StatisticheTempiForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//StatisticheTempiForm.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));


		List arrListaTipoOrdine=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine());

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);

		//tck 2565 esclusione di D, L,C
		for (int j=0;  j < arrListaTipoOrdine.size(); j++)
		{
			ComboCodDescVO eleTipoOrd= (ComboCodDescVO) arrListaTipoOrdine.get(j);
			if (eleTipoOrd.getCodice().equals("A") || eleTipoOrd.getCodice().equals("V") || eleTipoOrd.getCodice().equals("R"))
			{
				elem = new StrutturaCombo(eleTipoOrd.getCodice(), eleTipoOrd.getCodice() +" - " + eleTipoOrd.getDescrizione());
				lista.add(elem);
			}
		}
		StatisticheTempiForm.setListaTipoOrdine(lista);


	}





	public int validaDataPassata(String data) {
		int DATA_OK=0;
		int DATA_ERRATA=1;
		int DATA_MAGGIORE=2;
		int DATA_PASSATA_ERRATA=3;

		int codRitorno=-1;
		if (data==null) {
			codRitorno=DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			//DateParser.parseDate(data);
			// l'istruzione sottostante va in errore se non non riesce a fare il parsing del rispetto del formato
			java.util.Date date = format.parse(data);
			if (java.util.regex.Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				Date oggi = new Date(System.currentTimeMillis());
				//if (date.after(oggi)) {
				//	codRitorno=DATA_MAGGIORE;
                //    throw new Exception(); // data > data odierna
				//}
				codRitorno=DATA_OK;
				return codRitorno; // tutto OK
			} else {
                codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {
			return codRitorno;
		}
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
		if (idCheck.equals("STAMPASPESE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				// 04.12.08 e.printStackTrace();
				log.error(e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		if (idCheck.equals("STAMPASTAT") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				//utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI, utente.getCodPolo(), utente.getCodBib(), null);
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utente.getCodPolo(), utente.getCodBib(), null);
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
