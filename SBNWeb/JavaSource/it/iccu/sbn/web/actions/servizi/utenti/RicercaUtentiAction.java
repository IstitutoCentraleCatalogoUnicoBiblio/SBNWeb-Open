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
package it.iccu.sbn.web.actions.servizi.utenti;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.utenti.RicercaUtentiForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class RicercaUtentiAction extends UtenteBaseAction {

	private static Logger log = Logger.getLogger(RicercaUtentiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca",   "cerca");
		map.put("servizi.bottone.nuovo",   "nuovo");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.si",      "si");
		map.put("servizi.bottone.no",      "no");
		map.put("servizi.bottone.importaDaBiblioteca", "importaBiblioteca");

		//almaviva5_20111109 gestione bib. affiliate
		map.put("servizi.bottone.cambioBiblioteca", "sifBibAffiliata");

		return map;
	}


	private void checkForm(HttpServletRequest request, ActionForm form, ActionMapping mapping)
	throws RichiestaConfermaException, ValidationException, Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		ActionMessages errors = new ActionMessages();

		RicercaUtenteBibliotecaVO ricerca = currentForm.getRicerca();
		if (ricerca.impostatoCodiceFiscale()) {
			if (ricerca.getCodFiscale().length() != 16) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceFiscaleErrore"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}

			if (ricerca.impostatoCodiceMatricola()
					|| ricerca.impostatoCodiceUtente()
					|| ricerca.impostatiCriteriNonUnivoci()
					|| ricerca.impostatoNomeCognomeEsatto()
				)
			{
				//Se é presente il codice fiscale, utilizzo solo questo per effettuare la ricerca
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.utenti.ricercaSoloConCodiceFiscale"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				currentForm.setConferma(true);
				currentForm.setRichiesta("RicercaConCodiceFiscale");

				throw new RichiestaConfermaException();
			} else return;
		}

		if (ricerca.impostatoCodiceUtente()) {
			String codiceUtente = ricerca.getCodUte();

			if (codiceUtente.length()<3) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceUtenteInizialiErrore"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}

			ricerca.setCodUte(ServiziUtil.espandiCodUtente(codiceUtente));
			//LFV 08/10/2018 Adeguato a utenze Esse3
			if (ricerca.getCodUte().length() > 128) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceUtenteErrore"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}

			if (ricerca.impostatoCodiceMatricola()
				|| ricerca.impostatiCriteriNonUnivoci()
				|| ricerca.impostatoNomeCognomeEsatto())
			{
				//Se é presente il codice utente, utilizzo solo questo per effettuare la ricerca
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.utenti.ricercaSoloConCodiceUtente"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				currentForm.setConferma(true);
				currentForm.setRichiesta("RicercaConCodiceUtente");

				throw new RichiestaConfermaException();
			} else return;
		}

		if (ricerca.impostatoCodiceMatricola()) {
			if (ricerca.impostatiCriteriNonUnivoci()
					||
				ricerca.impostatoNomeCognomeEsatto())
			{
					//Se é presente la matricola, utilizzo solo questa per effettuare la ricerca
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.utenti.ricercaSoloConCodiceMatricola"));
					this.saveErrors(request, errors);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					currentForm.setConferma(true);
					currentForm.setRichiesta("RicercaConCodiceMatricola");

					throw new RichiestaConfermaException();
			} else return;
		}

		if (!ValidazioneDati.strIsNull(ricerca.getDataNascita())) {
			String campo = ricerca.getDataNascita();
			int codRitorno = -1;
			try {
				codRitorno = ValidazioneDati.validaDataPassata(campo);
				if (codRitorno != ValidazioneDati.DATA_OK) 	throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
					case ValidazioneDati.DATA_ERRATA:
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataFormatoErrore"));
						this.saveErrors(request, errors);
						break;
					case ValidazioneDati.DATA_MAGGIORE:
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMaggioreErrore"));
						this.saveErrors(request, errors);
						break;
					case ValidazioneDati.DATA_PASSATA_ERRATA:
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataVuotaErrore"));
						this.saveErrors(request, errors);
						break;
					default:break;
				}
				throw new ValidationException("");
			}
		}

		if (!ValidazioneDati.strIsNull(ricerca.getCognome())) {
			if (!ValidazioneDati.strIsAlfabetic(ricerca.getCognome())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.cognomeNumericoError"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}
		}
		if (!ValidazioneDati.strIsNull(ricerca.getNome())) {
			if (!ValidazioneDati.strIsAlfabetic(ricerca.getNome())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.nomeNumericoErrore"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}
		}

		if (!ValidazioneDati.strIsNull(ricerca.getOccupazione())) {
			if (ValidazioneDati.strIsNull(ricerca.getProfessione())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codOccupazioneSenzaProfessione"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}
		}

		if (!ValidazioneDati.strIsNull(ricerca.getSpecificita())) {
			if (ValidazioneDati.strIsNull(ricerca.getTitStudio())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codSpecTitoloStudioSenzaTitStudio"));
				this.saveErrors(request, errors);
				throw new ValidationException("");
			}
		}

		if (!ricerca.impostatoCodiceMatricola() && ((ricerca.getMatricola()!=null && ricerca.getMatricola().trim().length()>0 ) || (ricerca.getCodiceAteneo()!=null && ricerca.getCodiceAteneo().trim().length()>0 ))) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.utenti.ricercaPresenzaMatricolaAteneo"));
			this.saveErrors(request, errors);
			throw new ValidationException("");
		}
		if (!ricerca.impostatiCriteriUnivoci() && !ricerca.impostatiCriteriNonUnivoci()) {
			//Non è stato utilizzato nessuno dei canali univoci per la ricerca. Si da un messaggio di avviso
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noCanali"));
			throw new ValidationException("");
		}

	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			init(request, currentForm);

			//almaviva5_20111109 gestione bib. affiliate
			BibliotecaVO bib = (BibliotecaVO) request.getAttribute(BibliotecaDelegate.LISTA_BIBLIOTECHE_AFFILIATE);
			if (bib != null) {
				currentForm.clear();
				currentForm.setBiblioteca(bib.getCod_bib());
				currentForm.setCurrentBib(bib);
				currentForm.getRicerca().setCodBibSer(bib.getCod_bib());
				initCombo(request, form);
			}

			if (currentForm.getChgProf()!=null && !currentForm.getChgProf().equals(currentForm.getRicerca().getProfessione()))
				this.reloadOccupazioni(request,form);

			if (currentForm.getChgTit()!=null &&  !currentForm.getChgTit().equals(currentForm.getRicerca().getTitStudio()))
				this.reloadTitSpecif(request,form);


			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward importaBiblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RicercaUtentiForm ricercaForm = (RicercaUtentiForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();

			if (!isTokenValid(request)) {
				saveToken(request);
			}

			ricercaForm.setNonTrovato(false);

			return mapping.findForward("importaDaBiblioteca");
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RicercaUtentiForm ricercaForm = (RicercaUtentiForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!ricercaForm.isSessione()) {
				ricercaForm.setSessione(true);
			}

			ricercaForm.setConferma(false);
			ricercaForm.setRichiesta("");

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaUtentiForm ricercaForm = (RicercaUtentiForm) form;

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (!ricercaForm.isSessione()) {
				ricercaForm.setSessione(true);
			}

			String appoggio;
			if (ricercaForm.getRichiesta().equals("RicercaConCodiceFiscale")) {
				appoggio=ricercaForm.getRicerca().getCodFiscale();
				ricercaForm.getRicerca().clear();
				ricercaForm.getRicerca().setCodFiscale(appoggio);
			}

			if (ricercaForm.getRichiesta().equals("RicercaConCodiceUtente")) {
				appoggio=ricercaForm.getRicerca().getCodUte();
				ricercaForm.getRicerca().clear();
				ricercaForm.getRicerca().setCodUte(appoggio);
			}

			if (ricercaForm.getRichiesta().equals("RicercaConCodiceMatricola")) {
				appoggio=ricercaForm.getRicerca().getMatricola();
				ricercaForm.getRicerca().clear();
				ricercaForm.getRicerca().setMatricola(appoggio);
			}

			if (ricercaForm.getRichiesta().equals("RicercaSenzaCriteriUnivoci")) {

			}

			ricercaForm.setConferma(false);
			ricercaForm.setRichiesta("");

			return this.eseguiRicerca(mapping, form, request, response);
		} catch (Exception e) {
			ricercaForm.setConferma(false);
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		currentForm.setNonTrovato(false);

		request.setAttribute("Nuovo", "N");
		request.setAttribute("PathForm", mapping.getPath());
		this.resetToken(request);
		request.setAttribute("RicercaUtenti", currentForm.getRicerca());
		return mapping.findForward("nuovo");
	}


	private ActionForward eseguiRicerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		DescrittoreBloccoVO blocco1 = null;

		String cognomeNome = currentForm.getRicerca().getCognomeNome();
		if (!ValidazioneDati.strIsNull(cognomeNome)) {
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi("", cognomeNome);
			currentForm.getRicerca().setChiave_ute(key.getKy_cles1_A());
		} else
			currentForm.getRicerca().setChiave_ute(null);

		try {
			 blocco1 = delegate.caricaListaUtenti(request,
					navi.getUserTicket(), currentForm.getRicerca(),
					currentForm.getRicerca().getNumeroElementiBlocco());

		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);

		}

		if (DescrittoreBloccoVO.isFilled(blocco1) ) {
			request.setAttribute(ServiziDelegate.LISTA_UTENTI, blocco1);
			request.setAttribute("RicercaUtenti", currentForm.getRicerca());
			request.setAttribute("PathForm", mapping.getPath());

			request.setAttribute(BIBLIOTECA_ATTR, currentForm.getRicerca().getCodBibSer());
			request.setAttribute(PATH_CHIAMANTE_ATTR, "RicercaUtenti");

			return mapping.findForward("ok");
		} else {

			if (currentForm.getRicerca().impostatiCriteriUnivoci())	{
				currentForm.getRicerca().setRicercaUtentePolo(true); // imposto per la ricerca
				try {
					blocco1 = delegate.caricaListaUtenti(request,
							navi.getUserTicket(), currentForm.getRicerca(),
							currentForm.getRicerca().getNumeroElementiBlocco());

				} catch (Exception e) {
					log.error("", e);
				}
				currentForm.getRicerca().setRicercaUtentePolo(false); // reset dopo ricerca
			}
			if (DescrittoreBloccoVO.isFilled(blocco1) ) {
				request.setAttribute(ServiziDelegate.LISTA_UTENTI, blocco1);
				request.setAttribute("RicercaUtenti", currentForm.getRicerca());
				request.setAttribute("PathForm", mapping.getPath());

				//request.setAttribute(BIBLIOTECA_ATTR, listaelementi.getRicerca().getCodBibSer());
				request.setAttribute(BIBLIOTECA_ATTR, "di Polo");
				request.setAttribute(BIBLIOTECA_ATTR, "X");
				request.setAttribute(PATH_CHIAMANTE_ATTR, "RicercaUtenti");
				request.setAttribute("UTENTIPOLO","si");
				return mapping.findForward("ok");
			}
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
			this.saveErrors(request, errors);
			resetToken(request);
			currentForm.setNonTrovato(true);
			return mapping.getInputForward();
		}
	}


	public ActionForward cerca(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RicercaUtentiForm listaelementi = (RicercaUtentiForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!listaelementi.isSessione()) {
				loadDefault(request, form);
				listaelementi.setSessione(true);
			}

			this.checkForm(request, form, mapping);


			return this.eseguiRicerca(mapping, form, request, response);
		}  catch (RichiestaConfermaException e) {
			return mapping.getInputForward();
		} catch (ValidationException e)  {
			resetToken(request);
			if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			this.loadDefault(request, form);
			currentForm.setSessione(true);
		}
	    if (currentForm.getRicerca().getParametro() != null) {
			request.setAttribute("IdUte", "false");
			return mapping.findForward("utilityUte");
	    } else {
			currentForm.clear();
			this.loadDefault(request, form);
			currentForm.setSessione(true);
			return mapping.getInputForward();
	    }
	}

	public ActionForward sifBibAffiliata(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			UserVO utente = Navigation.getInstance(request).getUtente();
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(
					utente.getCodPolo(), utente.getCodBib(),
					CodiciAttivita.getIstance().SRV_GESTIONE_ANAGRAFE_UTENTE, 0,
					BibliotecaDelegate.LISTA_BIBLIOTECHE_AFFILIATE);
			return BibliotecaDelegate.getInstance(request).getSIFListaBibliotecheAffiliatePerAttivita(richiesta);
	}

	protected void initCombo(HttpServletRequest request, ActionForm form) throws Exception {
		log.debug("initCombo()");
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		currentForm.setTitoloStudio(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTitoloStudio()));
		currentForm.setProfessione(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceProfessioni()));
		currentForm.setNazCitta(carCombo.loadComboCodiciDesc(factory.getCodici().getCodicePaese()));
		currentForm.setProvinciaResidenza(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceProvincia()));
		currentForm.setAtenei(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceAteneo()));
		currentForm.setTipoPersonalita(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoPersonaGiuridica()));

		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		lista.add(new StrutturaCombo("",""));
		lista.add(new StrutturaCombo("S","Si"));
		lista.add(new StrutturaCombo("N","No"));
		currentForm.setListaPersonaGiurid(lista);

		RicercaUtenteBibliotecaVO ricerca = currentForm.getRicerca();
		String codPolo = ricerca.getCodPoloSer();
		String codBib = ricerca.getCodBibSer();
		int maxRows = CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, Integer.MAX_VALUE);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		RicercaAutorizzazioneVO ricercaAut = new RicercaAutorizzazioneVO();

		ricercaAut.setCodPolo(codPolo);
		ricercaAut.setCodBib(codBib);
		ricercaAut.setNumeroElementiBlocco(maxRows);
		ricercaAut.setTicket(Navigation.getInstance(request).getUserTicket());
		DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(ricercaAut);

		List<AutorizzazioneVO> listaAut = new ArrayList<AutorizzazioneVO>();
		listaAut.add(new AutorizzazioneVO("", ""));
		if (DescrittoreBloccoVO.isFilled(blocco1) )
			listaAut.addAll(blocco1.getLista());

		currentForm.setTipoAutor(listaAut);

		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(codPolo);
		ricercaOccup.setCodBib(codBib);
		ricercaOccup.setNumeroElementiBlocco(maxRows);
		if (ValidazioneDati.isFilled(ricerca.getProfessione()) )
			ricercaOccup.setProfessione(ricerca.getProfessione());

		blocco1 = delegate.caricaListaOccupazioni(ricercaOccup);
		List<OccupazioneVO> listaOccup = new ArrayList<OccupazioneVO>();
		listaOccup.add( new OccupazioneVO("", "", "", ""));
		if (DescrittoreBloccoVO.isFilled(blocco1) )
			listaOccup.addAll(blocco1.getLista());
		currentForm.setOccupazioni(listaOccup);


		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(codPolo);
		ricercaTDS.setCodBib(codBib);
		ricercaTDS.setNumeroElementiBlocco(maxRows);
		blocco1 = delegate.caricaListaSpecialita(ricercaTDS);

		List<SpecTitoloStudioVO> appoggioSpecTdS = new ArrayList<SpecTitoloStudioVO>();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (DescrittoreBloccoVO.isFilled(blocco1) )
			appoggioSpecTdS.addAll(blocco1.getLista());
		currentForm.setSpecTitoloStudio(appoggioSpecTdS);

	}


	private void reloadOccupazioni(HttpServletRequest request, ActionForm form)
	throws Exception {
		RicercaUtentiForm listaelementi = (RicercaUtentiForm) form;
		if (listaelementi.getRicerca()!=null && listaelementi.getRicerca().getProfessione()!=null)
		{
			listaelementi.getRicerca().setOccupazione("");
			listaelementi.setChgProf(listaelementi.getRicerca().getProfessione());

		}
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(navi.getUtente().getCodPolo());
		ricercaOccup.setCodBib(navi.getUtente().getCodBib());
		ricercaOccup.setTicket(navi.getUserTicket());
		ricercaOccup.setNumeroElementiBlocco(4000);
		if (listaelementi.getRicerca()!=null && listaelementi.getRicerca().getProfessione()!=null && listaelementi.getRicerca().getProfessione().trim().length()>0 )
		{
			ricercaOccup.setProfessione(listaelementi.getRicerca().getProfessione());
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
		RicercaUtentiForm listaelementi = (RicercaUtentiForm) form;
		if (listaelementi.getRicerca()!=null && listaelementi.getRicerca().getTitStudio()!=null)
		{
			listaelementi.getRicerca().setSpecificita("");
			listaelementi.setChgTit(listaelementi.getRicerca().getTitStudio());

		}
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(navi.getUtente().getCodPolo());
		ricercaTDS.setCodBib(navi.getUtente().getCodBib());
		ricercaTDS.setTicket(navi.getUserTicket());
		ricercaTDS.setNumeroElementiBlocco(4000);
		if (listaelementi.getRicerca()!=null && listaelementi.getRicerca().getTitStudio()!=null && listaelementi.getRicerca().getTitStudio().trim().length()>0 )
		{
			ricercaTDS.setTitoloStudio(listaelementi.getRicerca().getTitStudio());
		}
		DescrittoreBloccoVO blocco1 = delegate.caricaListaSpecialita(ricercaTDS);

		List<SpecTitoloStudioVO> appoggioSpecTdS = new ArrayList<SpecTitoloStudioVO>();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (blocco1.getTotRighe() > 0)
			appoggioSpecTdS.addAll(blocco1.getLista());
		listaelementi.setSpecTitoloStudio(appoggioSpecTdS);
	}


	protected void loadDefault(HttpServletRequest request, ActionForm form) {

		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try {
			RicercaUtenteBibliotecaVO ricerca = currentForm.getRicerca();
			ricerca.setNumeroElementiBlocco(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_UTE_ELEM_BLOCCHI)));
			ricerca.setOrdinamento((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_UTE_ORD_LISTA));

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.erroreDefault"));
		}

	}


	private void loadTipiOrdinamento(HttpServletRequest request, ActionForm form) throws Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		List<TB_CODICI> listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_UTENTI);
		CaricamentoCombo carCombo = new CaricamentoCombo();
		List<?> appoOrdinamento = carCombo.loadComboCodiciDesc(listaCodice);
		// rimuovo il primo elemento che non contiene valori
		appoOrdinamento = CaricamentoCombo.cutFirst(appoOrdinamento);
		currentForm.setLstTipiOrdinamento(appoOrdinamento);
	}

	private void loadMaterie(HttpServletRequest request, ActionForm form)
	throws Exception {
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
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
		currentForm.setMaterie(ris);

	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		if (!super.checkAttivita(request, form, idCheck))
			return false;

		Navigation navi = Navigation.getInstance(request);
		if (ValidazioneDati.equals(idCheck, "GESTIONE"))
			return !navi.bookmarksExist(NavigazioneServizi.BOOKMARK_EROGAZIONE, Bookmark.Servizi.RICERCA_SALE);

		if (ValidazioneDati.equals(idCheck, "CAMBIO_BIB")) {
			RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
			return !currentForm.isSIF() && super.checkAttivita(request, form, NavigazioneServizi.GESTIONE);
		}

		return true;
	}


	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);
		RicercaUtentiForm currentForm = (RicercaUtentiForm) form;
		if (currentForm.isSessione())
			return;

		Navigation navi = Navigation.getInstance(request);
		RicercaUtenteBibliotecaVO ricerca = (RicercaUtenteBibliotecaVO) request.getAttribute(NavigazioneServizi.RICERCA_UTENTE);
		ricerca = ricerca != null ? ricerca : currentForm.getRicerca();
		currentForm.setRicerca(ricerca);
		UserVO utente = navi.getUtente();

		currentForm.setSIF(ValidazioneDati.isFilled(request.getParameter("UTERICERCA")) );

		if (ValidazioneDati.equals(request.getAttribute(PATH_CHIAMANTE_ATTR), "ErogazioneRicerca"))
			ricerca.setCodBibSer((String)request.getAttribute(BIBLIOTECA_ATTR));
		else
			ricerca.setCodBibSer(utente.getCodBib());

		List<Integer> idUtentiIgnorati = (List<Integer>) request.getAttribute(LISTA_ID_IGNORATI);
		if (ValidazioneDati.isFilled(idUtentiIgnorati))
			ricerca.setIdUtentiIgnorati(idUtentiIgnorati);

		ricerca.setParametro(request.getParameter("UTERICERCA"));
		ricerca.setCodPoloSer(utente.getCodPolo());
		initCombo(request, form);

		loadDefault(request, form);

		//currentForm.clear();
		loadTipiOrdinamento(request, form);
		ricerca.setTipoRicerca("ini");
		ricerca.setRicercaUtentePolo(false);
		currentForm.setNonTrovato(false);

		//almaviva5_20111109 gestione bib. affiliate
		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(utente.getCodPolo());
		bib.setCod_bib(utente.getCodBib());
		bib.setNom_biblioteca(utente.getBiblioteca());
		currentForm.setBiblioteca(utente.getCodBib());
		currentForm.setCurrentBib(bib);
		ricerca.setCodBibSer(utente.getCodBib());

		loadMaterie(request, form);

		currentForm.setSessione(true);
	}


}

