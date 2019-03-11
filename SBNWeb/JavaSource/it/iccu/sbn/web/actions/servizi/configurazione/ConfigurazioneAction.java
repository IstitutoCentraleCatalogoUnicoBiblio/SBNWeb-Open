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
package it.iccu.sbn.web.actions.servizi.configurazione;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.ThreeState;
import it.iccu.sbn.util.servizi.CalendarioUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.configurazione.ConfigurazioneForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.LocalDate;

public class ConfigurazioneAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		//gestione submit folder
		map.put("servizi.bottone.tipiServizio",        "tipiServizio");
		map.put("servizi.bottone.modalitaErogazione",  "modalitaErogazione");
		map.put("servizi.bottone.supporti",            "supporti");
		map.put("servizi.bottone.parametriBiblioteca", "parametriBiblioteca");
		map.put("servizi.bottone.modalitaPagamento",   "modalitaPagamento");

		//gestione submit footer
		map.put("servizi.bottone.esamina",             "esamina");
//		map.put("servizi.bottone.esamina",             "configura");
		map.put("servizi.bottone.nuovo",               "nuovo");
		map.put("servizi.bottone.nuova",    		   "nuovo");
//		map.put("servizi.bottone.configura",           "configura");
//      map.put("servizi.bottone.aggiorna",            "aggiorna");
		map.put("servizi.bottone.aggiungi",            "aggiungi");
		map.put("servizi.bottone.cancella",            "cancella");

		map.put("servizi.bottone.ok",                  "ok");
		map.put("servizi.bottone.annulla",             "chiudi");

		map.put("servizi.bottone.si",        		   "si");
		map.put("servizi.bottone.no",        		   "no");

		//cambio biblioteca
		map.put("servizi.bottone.cambioBiblioteca",    "cambioBiblioteca");

		map.put("servizi.bottone.modello.sollecito", "modelloSollecito");

		//almaviva5_20160906
		map.put("servizi.bottone.calendario", "calendario");

		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		request.getSession().setAttribute("parametroSubmit", mapping.getParameter());

		Navigation navi = Navigation.getInstance(request);

		try {
			if (!currentForm.isSessione()) {
				currentForm.setBiblioteca(navi.getUtente().getCodBib());
				currentForm.setPolo(navi.getUtente().getCodPolo());

				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setFolder(ConfigurazioneForm.TIPI_SERVIZIO);

				navi.addBookmark(NavigazioneServizi.BOOKMARK_CONFIGURAZIONE);
			}

			BibliotecaVO bib = (BibliotecaVO) request.getAttribute("codBib");
			if (bib != null) {
				//vengo dal cambio biblioteca
				currentForm.setBiblioteca(bib.getCod_bib());
			}

			String folder = currentForm.getFolder();
			if (!folder.equals(ConfigurazioneForm.TIPI_SERVIZIO)) {
				if (navi.isFromBar()) {
					return mapping.getInputForward();
				}
			}

			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (folder.equals(ConfigurazioneForm.TIPI_SERVIZIO)) {
				return this.tipiServizio(mapping, form, request, response);
			}
			if (folder.equals(ConfigurazioneForm.MODALITA_EROGAZIONE)) {
				return this.modalitaErogazione(mapping, form, request, response);
			}
			if (folder.equals(ConfigurazioneForm.PARAMETRI_BIBLIOTECA)) {
				return this.parametriBiblioteca(mapping, form, request, response);
			}
			if (folder.equals(ConfigurazioneForm.SUPPORTI)) {
				return this.supporti(mapping, form, request, response);
			}
			if (folder.equals(ConfigurazioneForm.MODALITA_PAGAMENTO)) {
				return this.modalitaPagamento(mapping, form, request, response);
			}

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


	////////////////////////////////////////////////////////////////////////////////
	///////////////////////////   GESTIONE FOLDER    ///////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	public ActionForward tipiServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		/*
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		*/

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneForm.TIPI_SERVIZIO);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Configurazione Servizi");
		navigation.setTesto("Configurazione Servizi");

		//Carico lista tipi servizio associati alla biblioteca operante
		List<TipoServizioVO> lstCodTipiServizio = this.getListaTipiServizio(currentForm.getPolo(), currentForm.getBiblioteca(), request);

		int i;
		String codTipoServElab;
		boolean almenoUn_UltMod = false;
		boolean almenoUn_UltSupp = false;
		boolean nessuna_ModalitaErogazione = true;
		boolean nessun_Supporto = true;

		// elaboro tutti i Servizi per verificare sulla tabella tb_codici
		for (i = 0; i < lstCodTipiServizio.size(); i++) {
			codTipoServElab = lstCodTipiServizio.get(i).getCodiceTipoServizio();
			CodTipoServizio cod = CodTipoServizio.of(getCodice(codTipoServElab, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN, request));
			if (cod!=null){
				// il flag ult_mod
				almenoUn_UltMod = cod.richiedeModalitaErogazione();
				almenoUn_UltSupp = cod.richiedeSupporto();
			}

		}

		// verifico se non è presente alcuna modalità di erogazione
		Navigation navi = Navigation.getInstance(request);
		List<ModalitaErogazioneVO> listaTariffeModalitaErogazione = this.getTariffeModalitaErogaz(navi.getUtente().getCodPolo(),
				currentForm.getBiblioteca(), request);
		if (!listaTariffeModalitaErogazione.isEmpty()){
			//se presente almeno una modalità di erogazione imposto il flag a false
			nessuna_ModalitaErogazione = false;
		}

		// verifico se non è presente alcun supporto
		List<SupportoBibliotecaVO> listaSupportiVO = this.getSupportiBiblioteca(currentForm.getPolo(), currentForm.getBiblioteca(), request);

		if (!listaSupportiVO.isEmpty()){
			//se presente almeno un supporto imposto il flag a false
			nessun_Supporto=false;
		}

		currentForm.setStringaMessaggioServizioModalitaUltMod("");
		if (almenoUn_UltMod == true && nessuna_ModalitaErogazione == true){
			currentForm.setStringaMessaggioServizioModalitaUltMod("ATTENZIONE: non sono state configurate modalità di erogazione per la biblioteca");
		}

		currentForm.setStringaMessaggioServizioSupportiUltSupp("");
		if (almenoUn_UltSupp == true && nessun_Supporto == true){
			currentForm.setStringaMessaggioServizioSupportiUltSupp("ATTENZIONE: non sono stati configurati supporti per la biblioteca");
		}

		if (lstCodTipiServizio.size() == 1){
			currentForm.setSelectedTipoServizio("0");
		}

		currentForm.setLstTipiServizio(lstCodTipiServizio);

		return mapping.getInputForward();
	}

	public ActionForward parametriBiblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneForm.PARAMETRI_BIBLIOTECA);

		// carico le Categorie di Fruizione
		currentForm.setLstCatFruizione(loadCategorieFruizione(request));

		// carico le Categorie di Riproduzione
		currentForm.setLstCatRiproduzione(loadCategorieRiproduzione(request));
		// rimuovo il primo elemento per avere una lista
		// solo di valori validi (senza il primo a spazio)
		currentForm.getLstCatRiproduzione().remove(0);

		navi.setDescrizioneX("Configurazione Parametri biblioteca");
		navi.setTesto("Configurazione Parametri biblioteca");

		//almaviva5_20150511 recupero da action modello sollecito
		ParametriBibliotecaVO parametri = (ParametriBibliotecaVO) request.getAttribute(NavigazioneServizi.PARAMETRI_BIBLIOTECA);
		if (parametri != null) {
			currentForm.setParametriBib(parametri);
			return mapping.getInputForward();
		}

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// verifico se per quel polo-biblioteca è ammessa l'autoregistrazione
			// se sì, nella jsp successiva sarà presente anche la check box "E' ammessa l'autoregistrazione"
			// in caso contrario la check box non sarà presente
			boolean checkAutoreg = factory.getGestioneServizi().verificaAutoregistrazione(navi.getUserTicket(), currentForm.getPolo(), currentForm.getBiblioteca());
			currentForm.setVerificaAutoregistrazione(checkAutoreg);

			String verCambiato = null;
			String verInserimentoUtente = null;

			if (currentForm.getCambiato() != null)
				verCambiato = currentForm.getCambiato();


			if (currentForm.getInserimentoUtente() != null)
				verInserimentoUtente = currentForm.getInserimentoUtente();


			if (verCambiato != null && verCambiato.equals("SI"))
				currentForm.setCambiato(null);

			if (verInserimentoUtente != null && verInserimentoUtente.equals("SI"))
				currentForm.setInserimentoUtente(null);

			if (verCambiato != null && verCambiato.equals("SI") || verInserimentoUtente != null && verInserimentoUtente.equals("SI")) {
			}
			else {

				inizializzaListeParametriBiblioteca(currentForm, request);

				parametri = this.getParametriBiblioteca(currentForm.getPolo(), currentForm.getBiblioteca(), request);
				if (parametri != null) {
					currentForm.setNuovo(false);

				} else {
					parametri = new ParametriBibliotecaVO();
					currentForm.setNuovo(true);
				}

				// imposto codFruiDaIntervallo sempre a "SI"
				// Questo significa: utilizzare la categoria di fruizione di default solo per sezioni non definite
				parametri.setCodFruiDaIntervallo(true);

				currentForm.setParametriBib(parametri);
				currentForm.setUltimiParametriSalvati((ParametriBibliotecaVO)parametri.clone());

				//calendario
				ParametriServizi params = ParametriServizi.retrieve(request);
				if (params != null) {
					ModelloCalendarioVO modello = (ModelloCalendarioVO) params.get(ParamType.MODELLO_CALENDARIO);
					if (modello != null)
						parametri.setCalendario(modello);
				}
			}

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}


		return mapping.getInputForward();
	}

	public ActionForward supporti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneForm.SUPPORTI);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Configurazione Supporti");
		navigation.setTesto("Configurazione Supporti");

		try {
			this.configuraSupporti(currentForm, request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}

	public ActionForward modalitaErogazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneForm.MODALITA_EROGAZIONE);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Configurazione Modalità erogazione");
		navigation.setTesto("Configurazione Modalità erogazione");

		try {
			this.configuraTariffeModalitaErogazioneMap(request, (ConfigurazioneForm)form);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}

	public ActionForward modalitaPagamento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneForm.MODALITA_PAGAMENTO);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Configurazione Modalità pagamento");
		navigation.setTesto("Configurazione Modalità pagamento");

		try {
			this.configuraModalitaPagamento(currentForm, request);
			currentForm.setAggiungiModalita(false);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}
	//////////////////////////////////////////////////////////////////////////////////
	////////////////  FINE GESTIONE FOLDER  //////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////

/*
	public ActionForward configura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			//String tipoServizio=((ConfigurazioneForm)form).getCodTipoServizio();
			//if (tipoServizio!=null && !tipoServizio.equalsIgnoreCase("")) {
			//	String codTipoServizio  = tipoServizio.substring(0, tipoServizio.indexOf("_"));
			//	String descTipoServizio = tipoServizio.substring(tipoServizio.indexOf("_")+1);
			//
			//	Caricamento dettagli tipo servizio scelto
			//	TipoServizioVO tipoServizioVO = this.getTipoServizio(((ConfigurazioneForm)form).getPolo(), ((ConfigurazioneForm)form).getBiblioteca(), codTipoServizio, request);
			//	tipoServizioVO.setDescrizione(descTipoServizio);

			//	request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, tipoServizioVO);
			//	request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR, descTipoServizio);
			int codSel;
			String check = currentForm.getSelectedTipoServizio();
			if (check != null && check.length() != 0) {
				codSel = Integer.parseInt(currentForm.getSelectedTipoServizio());
				TipoServizioVO scelCod = (TipoServizioVO) currentForm.getLstTipiServizio().get(codSel);

				request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, scelCod);
				request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR, scelCod.getDescrizione() );
				request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, ((ConfigurazioneForm)form).getBiblioteca());

				return mapping.findForward("configura");

			}else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliTipoServizio"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

*/

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		try {

			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.TIPI_SERVIZIO)) {

				//				return this.esaminaTipoServizio(mapping, currentForm, request, response);

				int codSel;
				String check = currentForm.getSelectedTipoServizio();
				if (check != null && check.length() != 0) {
					codSel = Integer.parseInt(currentForm.getSelectedTipoServizio());
					TipoServizioVO scelCod = currentForm.getLstTipiServizio().get(codSel);

					request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, scelCod);
					request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR, scelCod.getDescrizione() );
					request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, ((ConfigurazioneForm)form).getBiblioteca());

					return mapping.findForward("configura");

				}else {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliTipoServizio"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}


			}

			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.SUPPORTI)) {
				return this.esaminaSupporto(mapping, currentForm, request, response);
			}

			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.MODALITA_EROGAZIONE)) {
				return this.esaminaModalita(mapping, currentForm, request, response);
			}

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		this.resetToken(request);
		return mapping.getInputForward();
	}

	private ActionForward esaminaModalita(ActionMapping mapping, ConfigurazioneForm currentForm,
			HttpServletRequest request, HttpServletResponse response) {
		if (ValidazioneDati.strIsNull(currentForm.getSelectedModalitaErogazione())) {
		//if (currentForm.getCodErog()==null || currentForm.getCodErog().equals("")) {
			//Non è stata scelta nessuna modalita
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliModalita"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, currentForm.getBiblioteca());
		request.setAttribute(ServiziBaseAction.VO_TARIFFA_MODALITA_EROGAZIONE, (currentForm.getLstTariffeModalitaErogazione().get(Integer.valueOf(currentForm.getSelectedModalitaErogazione()))).clone());

		return mapping.findForward("esaminaModalita");
	}


	public ActionForward cambioBiblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utente = Navigation.getInstance(request).getUtente();
		BibliotecaDelegate dao = new BibliotecaDelegate(factory, request);
		SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
			new SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(), utente.getCodBib(),
				CodiciAttivita.getIstance().SERVIZI, 10, "codBib");
		return dao.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);
	}


	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.TIPI_SERVIZIO)) {
			//Nuovo Tipo servizio
			request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, null);
			request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, currentForm.getBiblioteca());
			request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR, mapping.getPath());

		}else if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.MODALITA_EROGAZIONE)) {
			request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,           currentForm.getBiblioteca());
			request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR,          mapping.getPath());
			request.setAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR, currentForm.getLstCodiciErogazione());

			return mapping.findForward("nuovaModalita");

		}else if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.SUPPORTI)) {
			//Nuovo supporto
			request.setAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI, currentForm.getCodiciSupportiGiaAssociati());
			request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, currentForm.getBiblioteca());
			request.setAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR, currentForm.getLstCodiciErogazione());
			return mapping.findForward("aggiungiSupporto");

		}else if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.MODALITA_PAGAMENTO)) {
			//Nuova modalita pagamento
			currentForm.setAggiungiModalita(true);

			String utente = Navigation.getInstance(request).getUtente().getFirmaUtente();
			ModalitaPagamentoVO modalitaVO=new ModalitaPagamentoVO();
			modalitaVO.setCdBib(currentForm.getBiblioteca());
			modalitaVO.setCdPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			modalitaVO.setUteIns(utente);
			modalitaVO.setUteVar(utente);
			Timestamp now = DaoManager.now();
			modalitaVO.setTsIns(now);
			modalitaVO.setTsVar(now);
			currentForm.setModalitaVO(modalitaVO);

			return mapping.getInputForward();
		}

		return mapping.findForward("nuovo");
	}



	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);


		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
		try {
			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.SUPPORTI)) {
				if (ValidazioneDati.strIsNull(currentForm.getCodiceSupporto())) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.supporti.scegliSupportoCanc"));
					throw new ValidationException("Validazione dati fallita");
				}

				currentForm.setProv("cancella");
				currentForm.setConferma(true);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAttenzioneCancSupporto"));
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();

			}

			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.MODALITA_PAGAMENTO)) {
				List<String> items = getMultiBoxSelectedItems(currentForm.getModalitaSelezionate());
				if (!ValidazioneDati.isFilled(items)) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.modalitaPagamento.scegliModalitaCanc"));
					throw new ValidationException("Validazione dati fallita");
				}

				currentForm.setProv("cancella");
				currentForm.setConferma(true);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAttenzioneCancModPagamento"));
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();

			}

			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.MODALITA_EROGAZIONE)) {

				int codSel;
				String check = "";

				check = currentForm.getSelectedModalitaErogazione();

				if (check != null && check.length() != 0) {
					codSel = Integer.parseInt(currentForm.getSelectedModalitaErogazione());
					currentForm.setScelRecModErog(currentForm.getLstTariffeModalitaErogazione().get(codSel));
					currentForm.getScelRecModErog().setUteVar(Navigation.getInstance(request).getUtente().getFirmaUtente());
				}else{
					//Non è stata scelta nessuna modalita
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliModalitaCancellare"));
					return mapping.getInputForward();
				}

				currentForm.setProv("cancella");
				currentForm.setConferma(true);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAttenzioneCancModErogazione"));
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();
			}

			//almaviva5_20160622 cancellazione tipo servizio
			if (currentForm.getFolder().equalsIgnoreCase(ConfigurazioneForm.TIPI_SERVIZIO)) {
				String tipoServizio = currentForm.getSelectedTipoServizio();

				if (!ValidazioneDati.isFilled(tipoServizio) ) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliTipoServizioCancellare"));
					return mapping.getInputForward();
				}

				currentForm.setProv("cancella");
				currentForm.setConferma(true);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAttenzioneCancTipoServizio"));
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();
			}

		} catch (ValidationException e) {
			log.info(e.getMessage());
			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


/*	public ActionForward aggiungi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		request.setAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI, currentForm.getCodiciSupportiGiaAssociati());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, currentForm.getBiblioteca());
		return mapping.findForward("aggiungiSupporto");
	}
*/

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
		currentForm.setAggiungiModalita(false);

		return mapping.getInputForward();
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
		ActionMessages errors = new ActionMessages();

		try {
			if (currentForm.getFolder().equals(ConfigurazioneForm.PARAMETRI_BIBLIOTECA)) {
				checkAggiornaParametri(currentForm, request);

				ParametriBibliotecaVO parametri = currentForm.getParametriBib();
				if (!parametri.equals(currentForm.getUltimiParametriSalvati())) {
					//Ci sono dei cambiamenti. Salvo
					Navigation navi = Navigation.getInstance(request);
					String utente = navi.getUtente().getFirmaUtente();

					if (currentForm.isNuovo()) {
						parametri.setTsIns(DaoManager.now());
						parametri.setUteIns(utente);
						parametri.setCodBib(currentForm.getBiblioteca());
						parametri.setCodPolo(currentForm.getPolo());
					}

					parametri.setUteVar(utente);

					currentForm.setProv("aggiorna");
					currentForm.setConferma(true);
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.parameter.conferma"));
					this.saveErrors(request, errors);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();

				}

				else {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}

			} else {

				ModalitaPagamentoVO modalitaVO = currentForm.getModalitaVO();
				checkOkModalitaPagamento(currentForm, request);
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				ModalitaPagamentoVO newModPag =
					factory.getGestioneServizi().aggiornaModalitaPagamento(Navigation.getInstance(request).getUserTicket(),	modalitaVO);
				if (newModPag != null) {
					currentForm.setModalitaVO(newModPag);
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
					this.saveErrors(request, errors);
					resetToken(request);
					return this.modalitaPagamento(mapping, form, request, response);
				} else {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
					this.saveErrors(request, errors);
				}

			}

		} catch (ValidationException e) {
			log.info(e.getMessage());

		} catch (ApplicationException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage() ));
			this.saveErrors(request, errors, e);
			return mapping.getInputForward();

		} catch (RemoteException e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();

	}


	private void checkOkModalitaPagamento(ConfigurazioneForm currentForm, HttpServletRequest request)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean ok = true;

		ModalitaPagamentoVO modalitaVO = currentForm.getModalitaVO();
		if (modalitaVO.getCodModPagamento()<=0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.modalitaPagamento.codiceAssente"));
			this.saveErrors(request, errors);
			ok=false;
		}
		if (currentForm.getCodiciModalitaPagamentoAssociati().contains(modalitaVO.getCodModPagamento())) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.modalitaPagamento.codiceGiaPresente"));
			this.saveErrors(request, errors);
			ok=false;
		}
		if (modalitaVO.getDescrizione()==null || modalitaVO.getDescrizione().equals("")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.modalitaPagamento.descrizioneAssente"));
			this.saveErrors(request, errors);
			ok=false;
		}

		if (!ok) throw new ValidationException("Validazione dati fallita");
	}


	private void checkAggiornaParametri(ConfigurazioneForm currentForm, HttpServletRequest request) throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean ok = true;

		ConfigurazioneForm formCorrente = currentForm;

		ParametriBibliotecaVO parametri = formCorrente.getParametriBib();
		short numLettere=parametri.getNumeroLettere();

		if (numLettere>3) parametri.setNumeroLettere((short)3);
		if (numLettere<0) parametri.setNumeroLettere((short)0);

		if (numLettere<3) parametri.setGgRitardo3((short)0);
		if (numLettere<2) parametri.setGgRitardo2((short)0);
		if (numLettere<1) parametri.setGgRitardo1((short)0);

		if (parametri.getNumeroPrenotazioni()<0) parametri.setNumeroPrenotazioni((short)0);

		String codModalitaInvio1Sollecito1=parametri.getCodModalitaInvio1Sollecito1();
		String codModalitaInvio2Sollecito1=parametri.getCodModalitaInvio2Sollecito1();
		String codModalitaInvio3Sollecito1=parametri.getCodModalitaInvio3Sollecito1();

		String codModalitaInvio1Sollecito2=parametri.getCodModalitaInvio1Sollecito2();
		String codModalitaInvio2Sollecito2=parametri.getCodModalitaInvio2Sollecito2();
		String codModalitaInvio3Sollecito2=parametri.getCodModalitaInvio3Sollecito2();

		String codModalitaInvio1Sollecito3=parametri.getCodModalitaInvio1Sollecito3();
		String codModalitaInvio2Sollecito3=parametri.getCodModalitaInvio2Sollecito3();
		String codModalitaInvio3Sollecito3=parametri.getCodModalitaInvio3Sollecito3();

		if (codModalitaInvio1Sollecito1 == null) {
			codModalitaInvio1Sollecito1 = "";
		}
		if (codModalitaInvio2Sollecito1 == null) {
			codModalitaInvio2Sollecito1 = "";
		}
		if (codModalitaInvio3Sollecito1 == null) {
			codModalitaInvio3Sollecito1 = "";
		}

		if (codModalitaInvio1Sollecito2 == null) {
			codModalitaInvio1Sollecito2 = "";
		}
		if (codModalitaInvio2Sollecito2 == null) {
			codModalitaInvio2Sollecito2 = "";
		}
		if (codModalitaInvio3Sollecito2 == null) {
			codModalitaInvio3Sollecito2 = "";
		}

		if (codModalitaInvio1Sollecito3 == null) {
			codModalitaInvio1Sollecito3 = "";
		}
		if (codModalitaInvio2Sollecito3 == null) {
			codModalitaInvio2Sollecito3 = "";
		}
		if (codModalitaInvio3Sollecito3 == null) {
			codModalitaInvio3Sollecito3 = "";
		}

		if (numLettere>=1 && parametri.getGgRitardo1()<=0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.ggRitardo1Assente"));
			this.saveErrors(request, errors);
			ok=false;
		}

		if (ok != false) {
			if (codModalitaInvio1Sollecito1.equals("")  && !codModalitaInvio2Sollecito1.equals("") ||
					codModalitaInvio1Sollecito1.equals("")  && !codModalitaInvio3Sollecito1.equals("") ||
					!codModalitaInvio1Sollecito1.equals("") &&  codModalitaInvio2Sollecito1.equals("") && !codModalitaInvio3Sollecito1.equals("")){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.erroreModalitaInvio1"));
					this.saveErrors(request, errors);
					ok=false;
			}
		}

		if (ok != false) {
			if (numLettere>=1 && codModalitaInvio1Sollecito1.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.erroreModalitaInvio1a"));
				this.saveErrors(request, errors);
				ok=false;
			}
		}

		if (ok != false) {
			if (numLettere>=2 && parametri.getGgRitardo2()<=0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.ggRitardo2Assente"));
				this.saveErrors(request, errors);
				ok=false;
			}
		}

		if (ok != false) {
			if (codModalitaInvio1Sollecito2.equals("")  && !codModalitaInvio2Sollecito2.equals("") ||
					codModalitaInvio1Sollecito2.equals("")  && !codModalitaInvio3Sollecito2.equals("") ||
					!codModalitaInvio1Sollecito2.equals("") &&  codModalitaInvio2Sollecito2.equals("") && !codModalitaInvio3Sollecito2.equals("")){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.erroreModalitaInvio2"));
					this.saveErrors(request, errors);
					ok=false;
			}
		}

		if (ok != false) {
			if (numLettere>=2 && codModalitaInvio1Sollecito2.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.erroreModalitaInvio2a"));
				this.saveErrors(request, errors);
				ok=false;
			}
		}

		if (ok != false) {
			if (numLettere==3 && parametri.getGgRitardo3()<=0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.ggRitardo3Assente"));
				this.saveErrors(request, errors);
				ok=false;
			}
		}

		if (ok != false) {
			if (codModalitaInvio1Sollecito3.equals("")  && !codModalitaInvio2Sollecito3.equals("") ||
					codModalitaInvio1Sollecito3.equals("")  && !codModalitaInvio3Sollecito3.equals("") ||
					!codModalitaInvio1Sollecito3.equals("") &&  codModalitaInvio2Sollecito3.equals("") && !codModalitaInvio3Sollecito3.equals("")){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.erroreModalitaInvio3"));
					this.saveErrors(request, errors);
					ok=false;
			}
		}

		if (ok != false) {
			if (numLettere==3 && codModalitaInvio1Sollecito3.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.parametri.erroreModalitaInvio3a"));
				this.saveErrors(request, errors);
				ok=false;
			}
		}

		if (!ok) throw new ValidationException("Validazione dati fallita");
	}

	private void inizializzaListeParametriBiblioteca(ConfigurazioneForm currentForm, HttpServletRequest request) throws Exception {

		// inizializzo lstNumMaxSolleciti
		List<ComboCodDescVO> listaNumMaxSolleciti = new ArrayList<ComboCodDescVO>();

		listaNumMaxSolleciti.add(new ComboCodDescVO("0", ""));
		listaNumMaxSolleciti.add(new ComboCodDescVO("1", ""));
		listaNumMaxSolleciti.add(new ComboCodDescVO("2", ""));
		listaNumMaxSolleciti.add(new ComboCodDescVO("3", ""));

		currentForm.setLstNumMaxSolleciti(listaNumMaxSolleciti);

		// inizializzo lstModalitaInvio
		//almaviva5_20091222
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_INVIO_SOLLECITO);
		CaricamentoCombo combo = new CaricamentoCombo();
		currentForm.setLstModalitaInvio(combo.loadComboCodiciDesc(codici));

		List<Mediazione> lista = SaleDelegate.getInstance(request)
				.getListaCategorieMediazione(currentForm.getPolo(), currentForm.getBiblioteca(), false, ThreeState.FALSE, 0).getLista();
		lista = ValidazioneDati.coalesce(lista, ValidazioneDati.emptyList(Mediazione.class));
		lista.add(0, new Mediazione());
		currentForm.setListaCatMediazione(lista);

		//almaviva5_20150617
		codici = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_RINNOVO_AUTOMATICO);
		codici = CaricamentoCombo.cutFirst(codici);
		currentForm.setListaTipoRinnovo(codici);

		//almaviva5_20151125 servizi ill
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		UserVO utente = Navigation.getInstance(request).getUtente();
		RicercaAutorizzazioneVO ricerca = new RicercaAutorizzazioneVO();
		ricerca.setCodBib(currentForm.getBiblioteca());
		ricerca.setCodPolo(utente.getCodPolo());
		ricerca.setSoloILL(true);

		List<AutorizzazioneVO> listaTipoAutorizzazione = new ArrayList<AutorizzazioneVO>();
		listaTipoAutorizzazione.add(new AutorizzazioneVO("", ""));
		DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(ricerca);
		if (DescrittoreBloccoVO.isFilled(blocco1) )
			listaTipoAutorizzazione.addAll(blocco1.getLista());

		currentForm.setListaTipoAutorizzazione(listaTipoAutorizzazione);


	}


	private void configuraSupporti(ConfigurazioneForm currentForm, HttpServletRequest request) throws RemoteException, CreateException {
		//Carico lista supporti associati alla biblioteca operante
		List<SupportoBibliotecaVO> listaSupportiVO = this.getSupportiBiblioteca(currentForm.getPolo(), currentForm.getBiblioteca(), request);

		Map<String, SupportoBibliotecaVO> supportiBiblioteca = new TreeMap<String, SupportoBibliotecaVO>();
		List<String> codiciSupportiGiaAssociati = new ArrayList<String>();
		SupportoBibliotecaVO supportoVO=null;
		Iterator<SupportoBibliotecaVO> iterator = listaSupportiVO.iterator();
		while (iterator.hasNext()) {
			supportoVO=iterator.next();
			supportiBiblioteca.put(supportoVO.getCodSupporto(), supportoVO);
			codiciSupportiGiaAssociati.add(supportoVO.getCodSupporto());
		}
		currentForm.setSupportiBiblioteca(supportiBiblioteca);

		//currentForm.setLstSupporti(this.loadCodiciSupportiBibliotecaDiversiDa(codiciSupportiGiaAssociati));
		currentForm.setCodiciSupportiGiaAssociati(codiciSupportiGiaAssociati);

		if (codiciSupportiGiaAssociati != null && codiciSupportiGiaAssociati.size() == 1) {
			currentForm.setCodiceSupporto(codiciSupportiGiaAssociati.get(0));
		} else {
			currentForm.setCodiceSupporto(null);
		}

	}


	private void configuraModalitaPagamento(ConfigurazioneForm currentForm, HttpServletRequest request) throws RemoteException, CreateException {
		List<ModalitaPagamentoVO> listaModalitaVO = this.getModalitaPagamento(currentForm.getPolo(), currentForm.getBiblioteca(), request);
		currentForm.setModalitaPagamento(listaModalitaVO);
		currentForm.setCodiciModalitaPagamentoAssociati(new ArrayList<Short>());
		Iterator<ModalitaPagamentoVO> iterator = listaModalitaVO.iterator();
		while (iterator.hasNext()) {
			currentForm.getCodiciModalitaPagamentoAssociati().add( iterator.next().getCodModPagamento() );
		}
	}

	private void configuraTariffeModalitaErogazioneMap(HttpServletRequest request, ConfigurazioneForm form)
	throws RemoteException, CreateException, ParseException {
		Navigation navi = Navigation.getInstance(request);

		List<ModalitaErogazioneVO> listaTariffeModalitaErogazione = this.getTariffeModalitaErogaz(navi.getUtente().getCodPolo(),
				form.getBiblioteca(), request);
		form.setLstTariffeModalitaErogazione(listaTariffeModalitaErogazione);

		Map<String, ModalitaErogazioneVO> tariffeModalitaErogazioneMap = new HashMap<String, ModalitaErogazioneVO>();
		Iterator<ModalitaErogazioneVO> iterator = listaTariffeModalitaErogazione.iterator();
		ModalitaErogazioneVO tariffaModalitaVO;
		Locale locale = this.getLocale(request, Constants.SBN_LOCALE);//request.getLocale();
		double tariffa;
		double costoUnitario;
		List<String> lstCodiciErogazione = new ArrayList<String>();
		while (iterator.hasNext()) {
			tariffaModalitaVO = iterator.next();
			if (!locale.equals(tariffaModalitaVO.getLocale())) {
				//il locale dell'istanza di ModalitaErogazioneVO non corrisponde a quello della request
				//lo sovrascrivo e sovrascrivo anche la stringa relativa alla tariffa convertendola al formato
				//relativo al nuovo locale

				tariffaModalitaVO.setLocale(locale);

				tariffa = tariffaModalitaVO.getTarBaseDouble();
				tariffaModalitaVO.setTarBaseDouble(tariffa);

				costoUnitario = tariffaModalitaVO.getCostoUnitarioDouble();
				tariffaModalitaVO.setCostoUnitarioDouble(costoUnitario);
			}
			tariffeModalitaErogazioneMap.put(tariffaModalitaVO.getCodErog(), tariffaModalitaVO);
			lstCodiciErogazione.add(tariffaModalitaVO.getCodErog());
		}
		form.setTariffeModalitaErogazioneMap(tariffeModalitaErogazioneMap);
		form.setLstCodiciErogazione(lstCodiciErogazione);

		if (form.getLstTariffeModalitaErogazione() != null && form.getLstTariffeModalitaErogazione().size() == 1) {
			form.setSelectedModalitaErogazione("0");
		} else {
			form.setSelectedModalitaErogazione(null);
		}
	}



	protected ActionForward esaminaTipoServizio(ActionMapping mapping, ConfigurazioneForm currentForm,
								HttpServletRequest request, HttpServletResponse response) throws RemoteException
	{
		String tipoServizio = currentForm.getCodTipoServizio();
		if (tipoServizio!=null && !tipoServizio.equalsIgnoreCase("")) {
			String codTipoServizio= tipoServizio.substring(0, tipoServizio.indexOf("_"));
			String descTipoServizio = tipoServizio.substring(tipoServizio.indexOf("_")+1);

			//Caricamento dettagli tipo servizio scelto
			TipoServizioVO tipoServizioVO = this.getTipoServizio(currentForm.getPolo(), currentForm.getBiblioteca(), codTipoServizio, request);
			tipoServizioVO.setDescrizione(descTipoServizio);

			request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, tipoServizioVO.clone());
			request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR, descTipoServizio);
			request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, currentForm.getBiblioteca());
			request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR, mapping.getPath());

			return mapping.findForward("esaminaTipoServizio");
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliTipoServizio"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}


	private ActionForward esaminaSupporto(ActionMapping mapping, ConfigurazioneForm currentForm,
										  HttpServletRequest request, HttpServletResponse response)
	throws RemoteException
	{
		String codiceSupporto = currentForm.getCodiceSupporto();
		if (codiceSupporto!=null && !codiceSupporto.equals("")) {
			request.setAttribute(ServiziBaseAction.VO_SUPPORTO_ATTR, currentForm.getSupportiBiblioteca().get(currentForm.getCodiceSupporto()));
			//request.setAttribute(ServiziBaseAction.LISTA_CODICI_GIA_ASSEGNATI, currentForm.getCodiciSupportiGiaAssociati());
			request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR, currentForm.getBiblioteca());

			return mapping.findForward("esaminaSupporto");
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.supporti.scegliSupporto"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		boolean ok = true;

		Integer[] nonSelezionati = {};

		String firmaUtente = navi.getUtente().getFirmaUtente();
		Timestamp ts = DaoManager.now();

		BaseVO utenteVar = new BaseVO();
		utenteVar.setFlCanc("S");
		utenteVar.setUteIns(firmaUtente);
		utenteVar.setUteVar(firmaUtente);
		utenteVar.setTsIns(ts);
		utenteVar.setTsVar(ts);

		try {
			String folder = currentForm.getFolder();
			if (ValidazioneDati.equals(currentForm.getProv(), "cancella")){
				currentForm.setProv(null);

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				if (folder.equalsIgnoreCase(ConfigurazioneForm.MODALITA_EROGAZIONE)) {

					ModalitaErogazioneVO modErog = (ModalitaErogazioneVO) currentForm
							.getLstTariffeModalitaErogazione().get(
									Integer.valueOf(currentForm
											.getSelectedModalitaErogazione())).clone();
					ok = factory.getGestioneServizi().cancellaModalitaErogazione(navi.getUserTicket(), modErog);

					if (ok) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
						this.configuraTariffeModalitaErogazioneMap(request, currentForm);
						currentForm.setConferma(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceErroreCancellazione"));
						return mapping.getInputForward();
					}
				}

				else if (folder.equalsIgnoreCase(ConfigurazioneForm.SUPPORTI)) {

					SupportoBibliotecaVO supportoBibliotecaVO = currentForm.getSupportiBiblioteca().get(currentForm.getCodiceSupporto());

					ok = factory.getGestioneServizi().cancellaSupportiBiblioteca(navi.getUserTicket(),
							new String[] { String.valueOf(supportoBibliotecaVO.getId()) }, nonSelezionati, utenteVar);
					if (ok){
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
						this.configuraSupporti(currentForm, request);
						currentForm.setConferma(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
						return mapping.getInputForward();
					}
				}
				else if (folder.equalsIgnoreCase(ConfigurazioneForm.MODALITA_PAGAMENTO)) {

					List<String> items = getMultiBoxSelectedItems(currentForm.getModalitaSelezionate());

					ok = factory.getGestioneServizi().cancellaModalitaPagamento(navi.getUserTicket(),
									items.toArray(new String[0]),
									nonSelezionati,
									utenteVar);
					if (ok){
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
						this.configuraModalitaPagamento(currentForm, request);
						currentForm.setAggiungiModalita(false);
						currentForm.setConferma(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
						return mapping.getInputForward();
					}

				}
				else if (folder.equalsIgnoreCase(ConfigurazioneForm.TIPI_SERVIZIO)) {
					String tipoServizio = currentForm.getSelectedTipoServizio();
					TipoServizioVO tipoSrv = currentForm.getLstTipiServizio().get(Integer.valueOf(tipoServizio));
					ServiziDelegate.getInstance(request).cancellaTipoServizio(tipoSrv);
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
					return tipiServizio(mapping, currentForm, request, response);
				}

			}
			if (ValidazioneDati.equals(currentForm.getProv(), "aggiorna")){
				currentForm.setProv(null);
				if (folder.equals(ConfigurazioneForm.PARAMETRI_BIBLIOTECA)) {

					ParametriBibliotecaVO parametri = currentForm.getParametriBib();
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					ParametriBibliotecaVO newParams = factory.getGestioneServizi()
							.aggiornaParametriBiblioteca(navi
											.getUserTicket(), parametri);
					if (newParams != null) {
						currentForm.setParametriBib(newParams);
						currentForm.setUltimiParametriSalvati((ParametriBibliotecaVO) newParams.clone() );
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
						currentForm.setConferma(false);
						currentForm.setNuovo(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
						return mapping.getInputForward();
					}

				}

			}
		} catch (NumberFormatException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return no(mapping, form, request, response);
		} catch (SbnBaseException e) {
			resetToken(request);
			LinkableTagUtils.addError(request, e);
			return no(mapping, form, request, response);
		} catch (RemoteException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return no(mapping, form, request, response);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return no(mapping, form, request, response);
		}

		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneForm dettaglio = (ConfigurazioneForm)form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!dettaglio.isSessione())
			dettaglio.setSessione(true);


		dettaglio.setConferma(false);
		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward modelloSollecito(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
		ParametriBibliotecaVO parametriBib = currentForm.getParametriBib();
		request.setAttribute(NavigazioneServizi.PARAMETRI_BIBLIOTECA, parametriBib);

		return Navigation.getInstance(request).goForward(mapping.findForwardConfig("modelloSollecito"));
	}

	public ActionForward calendario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
		ParametriBibliotecaVO parametriBib = currentForm.getParametriBib();
		request.setAttribute(NavigazioneServizi.PARAMETRI_BIBLIOTECA, parametriBib);

		CalendarioVO modello = parametriBib.getCalendario();
		if (modello == null) {
			LocalDate inizio = LocalDate.now().withMonthOfYear(1).withDayOfMonth(1);
			LocalDate fine = inizio.plusYears(1).minusDays(1);
			String descr = String.format("calendario biblioteca '%s'", parametriBib.getCodPolo() + parametriBib.getCodBib() );
			modello = CalendarioUtil.getTemplate(inizio.toDate(), fine.toDate(), descr);

			modello.setCodPolo(currentForm.getPolo());
			modello.setCodBib(currentForm.getBiblioteca());
		}

		modello.setTipo(TipoCalendario.BIBLIOTECA);
		ParametriServizi params = new ParametriServizi();
		params.put(ParamType.MODELLO_CALENDARIO, modello.copy() );
		ParametriServizi.send(request, params);

		return Navigation.getInstance(request).goForward(mapping.findForward("calendario"));
	}


	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		boolean ok = super.checkAttivita(request, form, idCheck);
		if (ok) {
			if (idCheck.equals("MODELLO_SOLLECITO")) {
				ConfigurazioneForm currentForm = (ConfigurazioneForm)form;
				return !currentForm.isNuovo();
			}
		}

		return ok;
	}

}
