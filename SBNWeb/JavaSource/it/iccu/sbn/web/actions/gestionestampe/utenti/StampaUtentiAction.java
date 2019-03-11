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
package it.iccu.sbn.web.actions.gestionestampe.utenti;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaUtentiDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.utenti.StampaUtentiForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StampaUtentiAction extends ReportAction {

    private static Logger log = Logger.getLogger(StampaUtentiAction.class);

	private CaricamentoCombo carCombo = new CaricamentoCombo();

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaUtentiForm stampForm = (StampaUtentiForm) form;

		if (!stampForm.isSessione()) {
			stampForm.setSessione(true);
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			try{
				loadDefault(request, form);
				loadMaterie(request, form);

				stampForm.setElencoModelli(getElencoModelli());
			} catch (Exception ex) {
//				 ### MESSAGGIO DI ERRORE
				return mapping.getInputForward();
			}

			stampForm.setTipoRicerca("ini");
			stampForm.setTipoFormato(TipoStampa.PDF.name());
			stampForm.setTipoModello(((ModelloStampaVO)stampForm.getElencoModelli().get(0)).getJrxml());
			// se tipo modello tabellare impostare excel
			if (stampForm.getTipoModello().equals("default_utenti1"))
			{
				stampForm.setTipoFormato(TipoStampa.XLS.name());
			}
		}

		if (stampForm.getChgProf()!=null &&  !stampForm.getChgProf().equals(stampForm.getTipoProfessione()))
		{
			this.reloadOccupazioni(request,form);
		}
//		if (listaelementi.getRicerca()!=null && listaelementi.getRicerca().getTitStudio()!=null && listaelementi.getRicerca().getTitStudio().trim().length()>0 )
		if (stampForm.getChgTit()!=null &&  !stampForm.getChgTit().equals(stampForm.getTipoTitStudio()))
		{
			this.reloadTitSpecif(request,form);
		}


		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		StampaUtentiForm stampForm = (StampaUtentiForm) form;

		try {
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaUtentiForm currentForm = ((StampaUtentiForm) form);

		UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);

		String utente= user.getUserId();

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_STAMPA_UTENTE, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);
		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.servizi.erogazione.utente.noAut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();//return null;
		}

		try{
				this.checkForm(request, form);
				String biblUtente = Navigation.getInstance(request).getUtente().getCodBib();
				String poloUtente = Navigation.getInstance(request).getUtente().getCodPolo();
				RicercaUtenteBibliotecaVO ricerca =new RicercaUtenteBibliotecaVO();

				ricerca.setCognome(currentForm.getCognome());
				ricerca.setNome(currentForm.getNome());
				if (!ValidazioneDati.strIsNull(currentForm.getCognomeNome())) {
					GeneraChiave key = new GeneraChiave();
					key.estraiChiavi("", currentForm.getCognomeNome());
					ricerca.setChiave_ute(key.getKy_cles1_A());
					ricerca.setCognome("");
					ricerca.setNome("");
				} else
					ricerca.setChiave_ute(null);

				ricerca.setCodiceAteneo(currentForm.getCodiceAteneo());
				ricerca.setMatricola(currentForm.getMatricola());
				ricerca.setCodUte(currentForm.getCodUte());

				//vo.setDataNascita(stampaUtentiForm.getDataNascita());
				ricerca.setDataNascita(currentForm.getDataNascitaDa());
				ricerca.setDataNascitaA(currentForm.getDataNascitaA());
				ricerca.setDataFineAut(currentForm.getDataFineAutDa());
				ricerca.setDataFineAutA(currentForm.getDataFineAutA());

				ricerca.setTipoAutorizzazione(currentForm.getTipoAutorizzazione());
				if (ValidazioneDati.isFilled(ricerca.getTipoAutorizzazione()) ) {
					for (int t = 0; t < currentForm.getElencoAutorizzazioni().size(); t++) {
						if (currentForm.getElencoAutorizzazioni().get(t).getCodice().trim().equals(ricerca.getTipoAutorizzazione().trim())) {
						   	ricerca.setTipoAutorizzazioneDescr(currentForm.getElencoAutorizzazioni().get(t).getDescrizione().trim());
						   	break;
						}
					}
				}

				ricerca.setCodFiscale(currentForm.getCodiceFiscale());
			    ricerca.setEmail(currentForm.getEmail());
			    ricerca.setTitStudio(currentForm.getTipoTitStudio());
			    ricerca.setProvResidenza(currentForm.getTipoProvResid());
			    ricerca.setNazCitta(currentForm.getTipoNazCitta());
			    ricerca.setProfessione(currentForm.getTipoProfessione());
			    ricerca.setTitStudio(currentForm.getTipoTitStudio());
			    ricerca.setOccupazione(currentForm.getOccupazione());
			    ricerca.setSpecificita(currentForm.getSpecificita());
			    ricerca.setTipoPersona(currentForm.getTipoPersona());

			    //vo.setProfessione("");

				if (ricerca.getTitStudio()!=null && ricerca.getTitStudio().trim().length()>0)
				{
				   	String descr=CodiciProvider.cercaDescrizioneCodice(ricerca.getTitStudio().trim(), CodiciType.CODICE_TITOLO_STUDIO,CodiciRicercaType.RICERCA_CODICE_SBN );
				   	ricerca.setTitStudioDescr(descr);
				}

				if (ricerca.getSpecificita()!=null && ricerca.getSpecificita().trim().length()>0)
				{
					String descr="";
					for (int i=0;  i <  currentForm.getSpecTitoloStudio().size(); i++)
					{
						SpecTitoloStudioVO elem= (SpecTitoloStudioVO) currentForm.getSpecTitoloStudio().get(i);
						if (elem.getCodSpecialita().equals(ricerca.getSpecificita()))
						{
							descr=elem.getDesSpecialita();
						   	ricerca.setSpecificitaDescr(descr);
						   	break;
						}
					}
				}


				if (ricerca.getProfessione()!=null && ricerca.getProfessione().trim().length()>0)
				{
				   	String descr=CodiciProvider.cercaDescrizioneCodice(ricerca.getProfessione().trim(), CodiciType.CODICE_PROFESSIONI,CodiciRicercaType.RICERCA_CODICE_SBN );
				   	ricerca.setProfessioneDescr(descr);
				}

				if (ricerca.getOccupazione()!=null && ricerca.getOccupazione().trim().length()>0)
				{
					String descr="";
					for (int i=0;  i <  currentForm.getOccupazioni().size(); i++)
					{
						OccupazioneVO elem= (OccupazioneVO) currentForm.getOccupazioni().get(i);
						if (elem.getCodOccupazione().equals(ricerca.getOccupazione()))
						{
							descr=elem.getDesOccupazione();
						   	ricerca.setOccupazioneDescr(descr);
						   	break;
						}
					}
				}

				//almaviva5_20110201 #4185
				String materia = currentForm.getMateria();
				if (ValidazioneDati.isFilled(materia))
					ricerca.setMateria(materia);


				if (ricerca.getNazCitta()!=null && ricerca.getNazCitta().trim().length()>0)
				{
				   	String descr=CodiciProvider.cercaDescrizioneCodice(ricerca.getNazCitta().trim(), CodiciType.CODICE_PAESE,CodiciRicercaType.RICERCA_CODICE_SBN );
				   	ricerca.setNazDescr(descr);
				}

			    //attenzione, prima di FI c'è uno spazio!!!!
			    ricerca.setCodBibSer(biblUtente);

			    ricerca.setCodPoloSer(poloUtente);
			    //FI
	//		    vo.setCodBibSer(" FI");
	     	     //"SBR" o "CSW"
	//		    vo.setCodPoloSer("CSW");
			    ricerca.setIdUte(0);
			    ricerca.setNumeroElementiBlocco(4000);
			    ricerca.setTipoRicerca(currentForm.getTipoRicerca());//("ini");
			    ricerca.setOrdinamento("");

				List inputForStampeService=new ArrayList();
				inputForStampeService.add(ricerca);

				String tipoFormato=currentForm.getTipoFormato();

				request.setAttribute("DatiVo", inputForStampeService);
				request.setAttribute("TipoFormato", tipoFormato);
				String fileJrxml = currentForm.getTipoModello()+".jrxml";
				String basePath=this.servlet.getServletContext().getRealPath(File.separator);


		//		percorso dei file template: webroot/jrxml/
				String pathJrxml=basePath+File.separator+"jrxml"+File.separator+fileJrxml;
//				String pathDownload = basePath+File.separator+"download";
				String pathDownload = StampeUtil.getBatchFilesPath();

				log.debug("PATH DEL SORGENTE " +pathJrxml);

				//codice standard inserimento messaggio di richiesta stampa differita
				StampaDiffVO stam = new StampaDiffVO();
				stam.setTipoStampa(tipoFormato);
				stam.setUser(utente);
				//stam.setCodPolo
				stam.setCodBib(biblUtente);
				//stam.setTipoOrdinamento
				stam.setParametri(inputForStampeService);
				stam.setTemplate(pathJrxml);
				stam.setDownload(pathDownload);
				stam.setDownloadLinkPath("/");
				stam.setTipoOperazione("STAMPA_UTENTE");
				//stam.setTicket
				UtilityCastor util= new UtilityCastor();
				String dataCorr = util.getCurrentDate();
				stam.setData(dataCorr);
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				//String idMessaggio = factory.getStampeOnline().stampaUtente(stam);

/*				ActionMessages errors = new ActionMessages();
				idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
				errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
				this.saveErrors(request, errors);
*/
				// nuova gestione  batch

				StampaUtentiDiffVO richiesta = new StampaUtentiDiffVO();
				richiesta.setCodPolo(poloUtente);
				richiesta.setCodBib(biblUtente);
				richiesta.setUser(utente);
				richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_STAMPA_UTENTE);
				richiesta.setParametri(inputForStampeService);
				String ticket = Navigation.getInstance(request).getUserTicket();
				richiesta.setTicket(ticket);
				//richiesta.setTipoOrdinamento(ordinamFile);
				richiesta.setStampavo(stam);


				String s =  null;
				try {
					s = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, richiesta, null);;
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
					errors.add("Attenzione", new ActionMessage("errors.servizi.prenotFallita"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.servizi.prenotOk", s.toString()));
				this.saveErrors(request, errors);






//				}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	return mapping.getInputForward();
	}

//	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
//		try{
//			request.setAttribute("stampa", "stampaOnLine");
//		} catch (Exception e) {
//			return mapping.getInputForward();
//		}
//		return unspecified(mapping, form, request, response);
//	}


	private void checkForm(HttpServletRequest request, ActionForm form) throws Exception {
		StampaUtentiForm listaelementi = (StampaUtentiForm) form;
		ActionMessages errors = new ActionMessages();
		boolean error = false;
		if(!(listaelementi.getTipoRicerca() != null)){//errors.finestampa
			errors.add("generico", new ActionMessage("errors.stampaUtenti.tipoRicerca"));
			this.saveErrors(request, errors);
			error = true;
			if (error) {
				request.setAttribute("errore", "erroreInserimentoDati");//request.setAttribute("stampa", "stampaOnLine");
				throw new Exception();
			}
		}
		if (listaelementi.getCodiceFiscale() != null
				&& listaelementi.getCodiceFiscale().length() != 0) {
			if (listaelementi.getCodiceFiscale().length() != 16) {
				errors.add("generico", new ActionMessage("errors.stampaUtenti.codiceFiscaleErrore"));
				this.saveErrors(request, errors);
				error = true;
			}
		}

		if (!ValidazioneDati.strIsNull(listaelementi.getDataNascita())) {
			String campo = listaelementi.getDataNascita();
			int codRitorno = -1;
			try {
				codRitorno = ValidazioneDati.validaDataPassata(campo);
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.stampaUtenti.dataFormatoErrore"));
					this.saveErrors(request, errors);
					error = true;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add("generico", new ActionMessage("errors.stampaUtenti.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					error = true;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add("generico", new ActionMessage("errors.stampaUtenti.dataVuotaErrore"));
					this.saveErrors(request, errors);
					error = true;
				}
			}
		}
		if (!ValidazioneDati.strIsNull(listaelementi.getCognome())) {
			if (!ValidazioneDati.strIsAlfabetic(listaelementi.getCognome())) {
				errors.add("generico", new ActionMessage("errors.stampaUtenti.cognomeNumericoError"));
				this.saveErrors(request, errors);
				error = true;
			}
			if (error) {
				request.setAttribute("errore", "erroreInserimentoDati");//request.setAttribute("stampa", "stampaOnLine");
				throw new Exception();
			}
		}
		if (!ValidazioneDati.strIsNull(listaelementi.getNome())) {
			if (!ValidazioneDati.strIsAlfabetic(listaelementi.getNome())) {
				errors.add("generico", new ActionMessage("errors.stampaUtenti.nomeNumericoErrore"));
				this.saveErrors(request, errors);
				error = true;
			}
			if (error) {
				request.setAttribute("errore", "erroreInserimentoDati");//request.setAttribute("stampa", "stampaOnLine");
				throw new Exception();
			}
		}

		if (!ValidazioneDati.strIsNull(listaelementi.getOccupazione())) {
			if (ValidazioneDati.strIsNull(listaelementi.getTipoProfessione())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codOccupazioneSenzaProfessione"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}
		}

		if (!ValidazioneDati.strIsNull(listaelementi.getSpecificita())) {
			if (ValidazioneDati.strIsNull(listaelementi.getTipoTitStudio())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codSpecTitoloStudioSenzaTitStudio"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}
		}


	}

	protected void loadDefault(HttpServletRequest request, ActionForm form) {
	try{
		log.info("loadDefault()");
		StampaUtentiForm stampaUtentiForm = (StampaUtentiForm) form;
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		stampaUtentiForm.setElencoTitStudio(this.carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTitoloStudio()));
		stampaUtentiForm.setElencoProfessioni(this.carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceProfessioni()));
		stampaUtentiForm.setElencoNazCitta(this.carCombo
				.loadComboCodiciDesc(factory.getCodici().getCodicePaese()));
		stampaUtentiForm.setElencoProvResid(this.carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceProvincia()));
		stampaUtentiForm.setAtenei(this.carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceAteneo()));
		stampaUtentiForm.setTipoPersonalita(this.carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoPersonaGiuridica()));


		String codPoloServ = Navigation.getInstance(request).getUtente().getCodPolo();
		String codBibSer = Navigation.getInstance(request).getUtente().getCodBib();

		RicercaAutorizzazioneVO ricercaAut = new RicercaAutorizzazioneVO();
		ricercaAut.setCodPolo(codPoloServ);
		ricercaAut.setCodBib(codBibSer);
		ricercaAut.setNumeroElementiBlocco(4000);
		ricercaAut.setTicket(Navigation.getInstance(request).getUserTicket());
		DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(ricercaAut);

//		List listaAut = new ArrayList<AnagAutorizzazioniVO>();
//		listaAut.add(new AnagAutorizzazioniVO("", ""));
		List listaAut = new ArrayList();
		ComboCodDescVO comboListaAut = new ComboCodDescVO();

		comboListaAut.setCodice("");
		comboListaAut.setDescrizione("");
		listaAut.add(comboListaAut);
		List<AutorizzazioneVO> listaAnagAutorizzVO = new ArrayList<AutorizzazioneVO>();
//		listaAut.add(new ComboCodDescVO("", ""));
		if (blocco1.getTotRighe() > 0){
//			listaAut.addAll(blocco1.getLista());
			listaAnagAutorizzVO.addAll(blocco1.getLista());
		}
		AutorizzazioneVO anagAutor = new AutorizzazioneVO();
		int dimensLista = listaAnagAutorizzVO.size();
		while(dimensLista >0){
			anagAutor= listaAnagAutorizzVO.get(dimensLista-1);
			comboListaAut = new ComboCodDescVO();
			comboListaAut.setCodice(anagAutor.getCodAutorizzazione());
			comboListaAut.setDescrizione(anagAutor.getDesAutorizzazione());
			listaAut.add(comboListaAut);
			dimensLista--;
		}
		stampaUtentiForm.setElencoAutorizzazioni(listaAut);

		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(navi.getUtente().getCodPolo());
		ricercaOccup.setCodBib(navi.getUtente().getCodBib());
		ricercaOccup.setTicket(navi.getUserTicket());
		ricercaOccup.setNumeroElementiBlocco(4000);
		if (stampaUtentiForm.getTipoProfessione()!=null && stampaUtentiForm.getTipoProfessione().trim().length()>0 )
		{
			ricercaOccup.setProfessione(stampaUtentiForm.getTipoProfessione());
		}

		DescrittoreBloccoVO blocco2 = delegate.caricaListaOccupazioni(ricercaOccup);
		List<OccupazioneVO> listaOccup = new ArrayList<OccupazioneVO>();
		listaOccup.add( new OccupazioneVO("", "", "", ""));
		if (blocco2!=null &&  blocco2.getTotRighe() > 0)
			listaOccup.addAll(blocco2.getLista());
		stampaUtentiForm.setOccupazioni(listaOccup);

		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(navi.getUtente().getCodPolo());
		ricercaTDS.setCodBib(navi.getUtente().getCodBib());
		ricercaTDS.setTicket(navi.getUserTicket());
		ricercaTDS.setNumeroElementiBlocco(4000);
		blocco2 = delegate.caricaListaSpecialita(ricercaTDS);

		List<SpecTitoloStudioVO> appoggioSpecTdS = new ArrayList<SpecTitoloStudioVO>();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (blocco2.getTotRighe() > 0)
			appoggioSpecTdS.addAll(blocco2.getLista());
		stampaUtentiForm.setSpecTitoloStudio(appoggioSpecTdS);


		//Navigation navi = Navigation.getInstance(request);
		//ServiziDelegate delegate = new ServiziDelegate(request);


/*		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(navi.getUtente().getCodPolo());
		ricercaOccup.setCodBib(navi.getUtente().getCodBib());
		ricercaOccup.setTicket(navi.getUserTicket());
		ricercaOccup.setNumeroElementiBlocco(4000);


		if (stampaUtentiForm.getTipoProfessione()!=null && stampaUtentiForm.getTipoProfessione().trim().length()>0 )
		{
			ricercaOccup.setProfessione(stampaUtentiForm.getTipoProfessione());
		}

		DescrittoreBloccoVO blocco2 = delegate.caricaListaOccupazioni(ricercaOccup);
		List<OccupazioneVO> listaOccup = new ArrayList<OccupazioneVO>();
		listaOccup.add( new OccupazioneVO("", "", "", ""));
		if (blocco2!=null &&  blocco2.getTotRighe() > 0)
			listaOccup.addAll(blocco2.getLista());
		stampaUtentiForm.setOccupazioni(listaOccup);


		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(navi.getUtente().getCodPolo());
		ricercaTDS.setCodBib(navi.getUtente().getCodBib());
		ricercaTDS.setTicket(navi.getUserTicket());
		ricercaTDS.setNumeroElementiBlocco(4000);
		blocco2 = delegate.caricaListaSpecialita(ricercaTDS);

		List<SpecTitoloStudioVO> appoggioSpecTdS = new ArrayList<SpecTitoloStudioVO>();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (blocco2.getTotRighe() > 0)
			appoggioSpecTdS.addAll(blocco2.getLista());
		stampaUtentiForm.setSpecTitoloStudio(appoggioSpecTdS);
*/

	}catch(Exception execption){//RemoteException
		  execption.printStackTrace();
		}

	}


	private void loadMaterie(HttpServletRequest request, ActionForm form)
	throws Exception {
		StampaUtentiForm listaelementi = (StampaUtentiForm) form;
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		RicercaMateriaVO ricMat = new RicercaMateriaVO();
		ricMat.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
		ricMat.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		ricMat.setCodice("");
		ricMat.setDescrizione("");
		ricMat.setNumeroElementiBlocco(4000);
		ricMat.setOrdinamento("1");
		ricMat.setTicket(Navigation.getInstance(request).getUserTicket());
		List<MateriaVO> ris = new ArrayList<MateriaVO> ();
		ris.add(new MateriaVO("","",""));
		ris.addAll(delegate.getListaMaterie(ricMat));
		listaelementi.setMaterie(ris);

	}



	private void reloadOccupazioni(HttpServletRequest request, ActionForm form)
	throws Exception {
		StampaUtentiForm listaelementi = (StampaUtentiForm) form;
		if (listaelementi.getTipoProfessione()!=null)
		{
			listaelementi.setOccupazione("");
			listaelementi.setChgProf(listaelementi.getTipoProfessione());
		}
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(navi.getUtente().getCodPolo());
		ricercaOccup.setCodBib(navi.getUtente().getCodBib());
		ricercaOccup.setTicket(navi.getUserTicket());
		ricercaOccup.setNumeroElementiBlocco(4000);
		if (listaelementi.getTipoProfessione()!=null && listaelementi.getTipoProfessione().trim().length()>0 )
		{
			ricercaOccup.setProfessione(listaelementi.getTipoProfessione());
		}


		DescrittoreBloccoVO blocco1 = delegate.caricaListaOccupazioni(ricercaOccup);
		List<OccupazioneVO> listaOccup = new ArrayList<OccupazioneVO>();
		listaOccup.add( new OccupazioneVO("", "", "", ""));
		if (blocco1!=null &&  blocco1.getTotRighe() > 0)
			listaOccup.addAll(blocco1.getLista());
		listaelementi.setOccupazioni(listaOccup);
	}

	private void reloadTitSpecif(HttpServletRequest request, ActionForm form)
	throws Exception {
		StampaUtentiForm listaelementi = (StampaUtentiForm) form;
		if (listaelementi.getTipoTitStudio()!=null)
		{
			listaelementi.setSpecificita("");
			listaelementi.setChgTit(listaelementi.getTipoTitStudio());

		}
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(navi.getUtente().getCodPolo());
		ricercaTDS.setCodBib(navi.getUtente().getCodBib());
		ricercaTDS.setTicket(navi.getUserTicket());
		ricercaTDS.setNumeroElementiBlocco(4000);
		if (listaelementi.getTipoTitStudio()!=null && listaelementi.getTipoTitStudio().trim().length()>0 )
		{
			ricercaTDS.setTitoloStudio(listaelementi.getTipoTitStudio());
		}
		DescrittoreBloccoVO blocco1 = delegate.caricaListaSpecialita(ricercaTDS);

		List<SpecTitoloStudioVO> appoggioSpecTdS = new ArrayList<SpecTitoloStudioVO>();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (blocco1.getTotRighe() > 0)
			appoggioSpecTdS.addAll(blocco1.getLista());
		listaelementi.setSpecTitoloStudio(appoggioSpecTdS);
	}




	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI_STAMPA_UTENTE);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
}
