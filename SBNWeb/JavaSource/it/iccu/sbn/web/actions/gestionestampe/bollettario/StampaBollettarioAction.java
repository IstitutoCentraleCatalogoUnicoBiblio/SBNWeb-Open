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
package it.iccu.sbn.web.actions.gestionestampe.bollettario;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBean;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaBollettarioDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.bollettario.StampaBollettarioForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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


public class StampaBollettarioAction extends ReportAction{ // LookupDispatchAction {
	private StampaBollettarioForm stampaBollettarioForm;
	private CaricamentoCombo carCombo = new CaricamentoCombo();
	private AcquisizioniBean acqBean;




	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");
		map.put("ricerca.label.bibliolist", "biblioCerca");
//		map.put("button.stampa","stampa");
		return map;
	}



	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession httpSession = request.getSession();
		stampaBollettarioForm = (StampaBollettarioForm) form;
		stampaBollettarioForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		stampaBollettarioForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());

		//Settaggio biblioteche
/*		if(request.getAttribute("codBib") != null ) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			stampaBollettarioForm.setCodBib((String)request.getAttribute("codBib"));
		} */
		BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
		if (bibScelta!=null && bibScelta.getCod_bib()!=null && stampaBollettarioForm.getCodBib()!=null )
		{
			stampaBollettarioForm.setCodBib(bibScelta.getCod_bib());
			stampaBollettarioForm.setDescrBib(bibScelta.getNom_biblioteca());
		}
		List lista = new ArrayList();
		lista = this.loadTipiOrdinamento();
		stampaBollettarioForm.setListaTipiOrdinamento(lista);
/*		stampaBollettarioForm.setCodForn("");;
		stampaBollettarioForm.setNomForn("");
		stampaBollettarioForm.setTpForn("");
		stampaBollettarioForm.setPaese("");
		stampaBollettarioForm.setProv("");
		stampaBollettarioForm.setProfAcq("");
*/
//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		stampaBollettarioForm.setElencoModelli(getElencoModelli());
		stampaBollettarioForm.setTipoFormato(TipoStampa.HTML.name());

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO, utente.getCodPolo(), utente.getCodBib(), null);

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
		stampaBollettarioForm = (StampaBollettarioForm) form;
		try {
			request.setAttribute("parametroPassato", stampaBollettarioForm.getElemBlocco());
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
		stampaBollettarioForm = (StampaBollettarioForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		try {
//			// validazione

			if (stampaBollettarioForm.getCodBib()!=null && stampaBollettarioForm.getCodBib().length()!=0)
			{
				if (stampaBollettarioForm.getCodBib().length()>3)
				{
					throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
				}
			}

			if (stampaBollettarioForm.getDataUscitaDa()!=null && stampaBollettarioForm.getDataUscitaDa().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(stampaBollettarioForm.getDataUscitaDa()) && stampaBollettarioForm.getDataUscitaDa().length()==4)
				{
					String strAnnata=stampaBollettarioForm.getDataUscitaDa();
					stampaBollettarioForm.setDataUscitaDa("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(stampaBollettarioForm.getDataUscitaDa())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}
			}
			if (stampaBollettarioForm.getDataUscitaA()!=null && stampaBollettarioForm.getDataUscitaA().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(stampaBollettarioForm.getDataUscitaA()) && stampaBollettarioForm.getDataUscitaA().length()==4)
				{
					String strAnnata=stampaBollettarioForm.getDataUscitaA();
					stampaBollettarioForm.setDataUscitaA("31/12/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(stampaBollettarioForm.getDataUscitaA())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}
			if (stampaBollettarioForm.getDataRientroDa()!=null && stampaBollettarioForm.getDataRientroDa().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(stampaBollettarioForm.getDataRientroDa()) && stampaBollettarioForm.getDataRientroDa().length()==4)
				{
					String strAnnata=stampaBollettarioForm.getDataRientroDa();
					stampaBollettarioForm.setDataRientroDa("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(stampaBollettarioForm.getDataRientroDa())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}

			if (stampaBollettarioForm.getDataRientroA()!=null && stampaBollettarioForm.getDataRientroA().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(stampaBollettarioForm.getDataRientroA()) && stampaBollettarioForm.getDataRientroA().length()==4)
				{
					String strAnnata=stampaBollettarioForm.getDataRientroA();
					stampaBollettarioForm.setDataRientroA("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(stampaBollettarioForm.getDataRientroA())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}

			if (stampaBollettarioForm.getDataRientroPresuntaDa()!=null && stampaBollettarioForm.getDataRientroPresuntaDa().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(stampaBollettarioForm.getDataRientroPresuntaDa()) && stampaBollettarioForm.getDataRientroPresuntaDa().length()==4)
				{
					String strAnnata=stampaBollettarioForm.getDataRientroPresuntaDa();
					stampaBollettarioForm.setDataRientroPresuntaDa("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(stampaBollettarioForm.getDataRientroPresuntaDa())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}
			if (stampaBollettarioForm.getDataRientroPresuntaA()!=null && stampaBollettarioForm.getDataRientroPresuntaA().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(stampaBollettarioForm.getDataRientroPresuntaA()) && stampaBollettarioForm.getDataRientroPresuntaA().length()==4)
				{
					String strAnnata=stampaBollettarioForm.getDataRientroPresuntaA();
					stampaBollettarioForm.setDataRientroPresuntaA("31/12/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(stampaBollettarioForm.getDataRientroPresuntaA())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}

			ListaSuppOrdiniVO stampaBollettarioVO = new ListaSuppOrdiniVO();
			stampaBollettarioVO.setBollettarioSTP(true);
			if (stampaBollettarioForm.getCodBib() != null)
				stampaBollettarioVO.setCodBibl(stampaBollettarioForm.getCodBib());
			if (stampaBollettarioForm.getDataUscitaDa() != null)
				stampaBollettarioVO.setDataOrdineDa(stampaBollettarioForm.getDataUscitaDa());
			if (stampaBollettarioForm.getDataUscitaA() != null)
				stampaBollettarioVO.setDataOrdineA(stampaBollettarioForm.getDataUscitaA());
			if (stampaBollettarioForm.getDataRientroDa() != null)
				stampaBollettarioVO.setDataStampaOrdineDa(stampaBollettarioForm.getDataRientroDa());
			if (stampaBollettarioForm.getDataRientroA() != null)
				stampaBollettarioVO.setDataStampaOrdineA(stampaBollettarioForm.getDataRientroA());
			if (stampaBollettarioForm.getDataRientroPresuntaDa() != null)
				stampaBollettarioVO.setDataFineAbbOrdineDa(stampaBollettarioForm.getDataRientroPresuntaDa());
			if (stampaBollettarioForm.getDataRientroPresuntaA() != null)
				stampaBollettarioVO.setDataFineAbbOrdineA(stampaBollettarioForm.getDataRientroPresuntaA());

			if (stampaBollettarioForm.isRicercaLocale())
			{
				stampaBollettarioVO.setSoloInRilegatura(true);
			}
			else
			{
				stampaBollettarioVO.setSoloInRilegatura(false);
			}
			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
			if (stampaBollettarioForm.getCodBib()!=null && stampaBollettarioForm.getCodBib().length()>0)
			{
				stampaBollettarioVO.setCodBibl(stampaBollettarioForm.getCodBib());
			}

			stampaBollettarioVO.setCodPolo(polo);
			stampaBollettarioVO.setCodBibl(bibl);
			String ordinamFile = stampaBollettarioForm.getTipoOrdinamSelez();
			stampaBollettarioVO.setOrdinamento(ordinamFile);


			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();

			List inputForStampeService=new ArrayList();
			inputForStampeService.add(stampaBollettarioVO);

			String tipoFormato=stampaBollettarioForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);//  ListaBuoniOrdine
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = stampaBollettarioForm.getTipoModello()+".jrxml";
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
			stam.setTipoOperazione("STAMPA_BOLLETTARIO");
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//String idMessaggio = factory.getStampeOnline().stampaBollettario(stam);
			//ActionMessages errors = new ActionMessages();
			//idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			//errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
			//this.saveErrors(request, errors);


			// nuova gestione  batch

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			StampaBollettarioDiffVO richiesta = new StampaBollettarioDiffVO();
			richiesta.setCodPolo(polo);
			richiesta.setCodBib(bibl);
			richiesta.setUser(utente);
			richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO);
			richiesta.setParametri(inputForStampeService);
			richiesta.setTipoOrdinamento(ordinamFile);
			richiesta.setStampavo(stam);
			String ticket=Navigation.getInstance(request).getUserTicket();

			String downloadPath = StampeUtil.getBatchFilesPath();
			// ristampa etichette
			if (stampaBollettarioForm.isRistampaEtichette())
			{
				SpostamentoCollocazioniVO spostaColl = new SpostamentoCollocazioniVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				spostaColl.setEtichette(DocumentoFisicoCostant.ETICHETTE);
				ModelloDefaultVO modello = this.getModelloDefault(Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(), Navigation.getInstance(request).getUtente().getTicket());
				if (modello == null){
					spostaColl.setCodModello("");
					spostaColl.setTipoFormato(TipoStampa.HTML.name());
					spostaColl.setDescrBibEtichetta("");

				}else{
					spostaColl.setCodModello(modello.getCodModello());
					spostaColl.setTipoFormato(modello.getFormatoStampa());
					if (modello.getDescrBibModello() != null && !modello.getDescrBibModello().trim().equals("")){
						spostaColl.setDescrBibEtichetta(modello.getDescrBibModello());
					}else{
						spostaColl.setDescrBibEtichetta(stampaBollettarioForm.getDescrBib());
					}
				}
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				int numCopie = Integer.valueOf(utenteEjb.getDefault(ConstantDefault.GDF_NRO_COPIE_ETICH).toString());
				if (!ValidazioneDati.strIsEmpty(String.valueOf(numCopie))){
					spostaColl.setNumCopie(numCopie);
				}else{
					spostaColl.setNumCopie(2);
				}
				//ho finito di preparare il VO con i dati della richiesta utente, ora lo metto nell'arraylist che passerò alla coda.
				List parametri=new ArrayList();
				parametri.add(spostaColl);
				request.setAttribute("DatiVo", parametri);
				//codice standard inserimento messaggio di richiesta stampa differita
				StampaDiffVO stamEtic = new StampaDiffVO();
				stamEtic.setTipoStampa(spostaColl.getTipoFormato());
				stamEtic.setUser(Navigation.getInstance(request).getUtente().getUserId());
				stamEtic.setCodPolo(richiesta.getCodPolo());
				stamEtic.setCodBib(richiesta.getCodBib());
				stamEtic.setTipoOrdinamento("");
				stamEtic.setParametri(parametri);
				stamEtic.setTemplate(spostaColl.getCodModello());
				stamEtic.setNumCopie(spostaColl.getNumCopie());
				stamEtic.setDownload(downloadPath);
				stamEtic.setDownloadLinkPath("/");
				stamEtic.setTipoOperazione("STAMPA_ETICHETTE");
				stamEtic.setTicket(Navigation.getInstance(request).getUserTicket());
				stamEtic.setData(dataCorr);
				spostaColl.setStampaDiffEtichette(stamEtic);
				richiesta.setRistampaEtichette(spostaColl);
				//
//			}else{
//				spostaColl.setStampaDiffEtichette(null);
//				spostaColl.setEtichette(null);
			}

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
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO);
			return listaModelli;
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
/*		rec = new CodiceVO("FA","Fornitore  Ascendente");
		lista.add(rec);
		rec = new CodiceVO("FD","Fornitore  Discendente");
		lista.add(rec);
*/		return lista;
	}


	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		stampaBollettarioForm = (StampaBollettarioForm) form;
		boolean searchOtherBib = stampaBollettarioForm.isRicercaLocale();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!stampaBollettarioForm.isSessione())
				{
					stampaBollettarioForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			resetToken(request);
			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("biblio",stampaBollettarioForm.getCodBib());
			request.setAttribute("descr",stampaBollettarioForm.getDescrBib());
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
			stampaBollettarioForm.setCodBib(((CodiceVO)ret.get(0)).getCodice());
			stampaBollettarioForm.setDescrBib("Biblioteca principale dell'ICCU");
		} else if (ret.size() > 1) {
			//mi chiedo se il codBib della biblio operante è nella lista delle biblioteche
			//prospetto la mappa con codBib, lente e descr  disable=false e carico la lista delle sezioni
			//della prima biblioteca della lista
			stampaBollettarioForm.setCodBib((this.searchBibliotecaOperante(ret,httpSession).getCodice()));
		}else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.stampaFornitori.campoNonImp"));
			this.saveErrors(request, errors);
		}
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		stampaBollettarioForm = (StampaBollettarioForm) form;
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
	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("STAMPA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO, utente.getCodPolo(), utente.getCodBib(), null);
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
