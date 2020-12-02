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
import it.iccu.sbn.ejb.remote.GestioneServizi;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.web.actionforms.servizi.configurazione.ConfigurazioneTipoServizioForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
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

public class ConfigurazioneTipoServizioAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.annulla", 			"chiudi");
		map.put("servizi.bottone.indietro", 		"indietro");
		map.put("servizi.bottone.ok", 				"aggiorna");
		map.put("servizi.bottone.nuovo",    		"nuovo");
		map.put("servizi.bottone.nuova",    		"nuovo");
		map.put("servizi.bottone.esamina",  		"esamina");
		map.put("servizi.bottone.cancella", 		"cancella");
		map.put("servizi.bottone.inserisci", 		"inserisci");
		map.put("servizi.bottone.aggiungi",  		"aggiungi");
		map.put("servizi.bottone.frecciaSu",        "spostaSu");
		map.put("servizi.bottone.frecciaGiu",       "spostaGiu");

		map.put("servizi.bottone.si",        "si");
		map.put("servizi.bottone.no",        "no");

		//Gestione folder
		map.put("servizi.bottone.diritti",  "servizi");
		map.put("servizi.bottone.iter",     "iter");
		map.put("servizi.bottone.modalitaErogazione", "modalita");
		map.put("servizi.bottone.moduloRichiesta", "moduloRichiesta");

		return map;
	}

	private void check(ActionForm form, HttpServletRequest request)
	throws Exception {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm) form;

		TipoServizioVO tipoServizio = currentForm.getTipoServizio();
		boolean checkOK=true;

		if (tipoServizio.getOreRidis()<0 || tipoServizio.getGgRidis()<0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.ore_gg_positivi"));

			checkOK=false;
		}
		if (tipoServizio.getOreRidis()!=0 && tipoServizio.getGgRidis()!=0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.ore_gg_solo_uno"));

			checkOK=false;
		}
		if (tipoServizio.getOreRidis()>23) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.ore_minori23"));

			checkOK=false;
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm) form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {

			String verInserimentoUtente = null;

			if (currentForm.getInserimentoUtente() != null) {
				verInserimentoUtente = currentForm.getInserimentoUtente();
			}

			if (verInserimentoUtente != null && verInserimentoUtente.equals("SI")) {
				currentForm.setInserimentoUtente(null);
			}

			// viene verificato se si proviene dalla modifica del campo sull'inserimento della richiesta da parte dell'utente
			if (verInserimentoUtente != null && verInserimentoUtente.equals("SI")) {
			}
			else {

				// in caso negativo proseguo l'elaborazione
				if (!currentForm.isSessione()) {
					currentForm.setSessione(true);
					currentForm.setConferma(false);
					currentForm.setFolder(ConfigurazioneTipoServizioForm.SERVIZI);

					currentForm.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));

					currentForm.setPolo(Navigation.getInstance(request).getUtente().getCodPolo());

					//almaviva5_20180914 #6685
					currentForm.setTipiServizioILL(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SERVIZIO_ILL));

					TipoServizioVO tipoServizio = (TipoServizioVO)request.getAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR);
					currentForm.setTipoServizio(tipoServizio);

					TipoServizioVO tipoServizioVO = currentForm.getTipoServizio();
					currentForm.setUltimoSalvato((TipoServizioVO)tipoServizioVO.clone());

					// carico i valori della tabella tb_codici relativi a quel servizio
					CodTipoServizio ts = CodTipoServizio.of(getCodice(tipoServizio.getCodiceTipoServizio(), CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN, request));

					if (ts != null) {
						if (ts.richiedeSupporto() && !ts.isILL()) {
							// se il flag relativo a ult_supp sulla tabella codici è "S"
							// scrivo il messaggio
							currentForm.setStringaMessaggioModalitaUltSupp("Funzione inibita. Il servizio in oggetto prevede l'indicazione di un supporto. Le modalità di erogazione dipendono dai singoli supporti configurati per la Biblioteca");
							// e imposto 'S' nel relativo campo della form sui supporti
							currentForm.setUlt_supp("S");
							// pulisco il messaggio sulle modalità (vedi dopo)
							currentForm.setStringaMessaggioNoModalita("");
							// e imposto "" nel relativo campo della form sulle modalità
							currentForm.setUlt_mod("");
						}
						else {
							// se il flag relativo a ult_supp sulla tabella codici è diverso da "S"
							if (ts.richiedeModalitaErogazione()) {
								// se il flag relativo a ult_mod  sulla tabella codici è "S"
								// pulisco il messaggio
								currentForm.setStringaMessaggioNoModalita("");
								// imposto 'S' nel relativo campo della form
								currentForm.setUlt_mod("S");
							}
							else {
								// scrivo il messaggio
								currentForm.setStringaMessaggioNoModalita("Funzione inibita. Il servizio in oggetto non prevede l'indicazione di una modalità di erogazione");
								// imposto a 'N' il relativo campo della form
								currentForm.setUlt_mod("N");
							}

							// pulisco il messaggio
							currentForm.setStringaMessaggioModalitaUltSupp("");
							// e imposto 'N' nel relativo campo della form
							currentForm.setUlt_supp("N");
						}
						if (ts.richiedeIter()){
							// se il flag relativo a richiede_iter sulla tabella codici è "S"
							// pulisco il messaggio
							currentForm.setStringaMessaggioNoIter("");
							currentForm.setRichiede_iter("S");
						}
						else {
							// imposto il messaggio relativo ad un servizio che non prevede iter
							currentForm.setStringaMessaggioNoIter("Funzione inibita. Il servizio in oggetto non prevede l'indicazione dell'iter");
							// imposto 'N nel relativo campo della form
							currentForm.setRichiede_iter("N");
						}

					}
				}
				configuraServiziMap(request, currentForm);
				if (currentForm.getLstServizi().size() == 1){
					currentForm.setSelectedServizio("0");
				} else {
					currentForm.setSelectedServizio(null);
				}
				if (currentForm.getLstTariffeModalitaErogazione() != null) {
					if (currentForm.getLstTariffeModalitaErogazione().size() == 1){
						currentForm.setSelectedModalitaErogazione("0");
					} else {
						currentForm.setSelectedModalitaErogazione(null);
					}
				}

				if (currentForm.getLstIter() != null) {
					if (currentForm.getLstIter().size() == 1){
						currentForm.setProgrIter("0");
					} else {
						currentForm.setProgrIter(null);
					}
				}

			if 	(request.getAttribute("prov") != null && request.getAttribute("prov").equals("moderogaz")){
				this.configuraTariffeModalitaErogazioneMap(request, currentForm);

			}

			if (request.getParameter("ricaricaListaIter") != null){
				return this.iter(mapping, currentForm, request, response);
			}

			// verifica se nei parametri di biblioteca è impostato il campo "E' ammesso l'inserimento della richiesta da parte dell'utente"
			// per impostarlo nel form e riutilizzarlo quando si deve o meno prospettare l'equivalente campo  tra le proprietà del Servizio
			// se impostato nei parametri di biblioteca non verranno prospettati tra le proprietà del servizio i campi sull'inserimento
			// della richiesta da parte dell'utente e quello relativo anche da WEB
			ParametriBibliotecaVO parametri = this.getParametriBiblioteca(currentForm.getPolo(), currentForm.getBiblioteca(), request);
			if (parametri == null) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfiguraServizio"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				NavigationForward goBack = Navigation.getInstance(request).goBack(true);
				return goBack;
			}
			currentForm.setAmmInsUtenteParamBiblioteca(parametri.isAmmessoInserimentoUtente());

			if (((ConfigurazioneTipoServizioForm)form).getFolder().equals(ConfigurazioneTipoServizioForm.MODULORICHIESTA)) {
				this.moduloRichiesta(mapping, currentForm, request, response);
			}

		}


		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


	public ActionForward spostaSu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkScambia(currentForm, request, TipoAggiornamentoIter.SU);


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneServizi().scambioIter(Navigation.getInstance(request).getUserTicket(), currentForm.getTipoServizio().getIdTipoServizio(), new Integer(currentForm.getProgrIter()), TipoAggiornamentoIter.SU);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

		}  catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return this.iter(mapping, form, request, response);
	}


	public ActionForward spostaGiu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkScambia(currentForm, request, TipoAggiornamentoIter.GIU);


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneServizi().scambioIter(Navigation.getInstance(request).getUserTicket(), currentForm.getTipoServizio().getIdTipoServizio(), new Integer(currentForm.getProgrIter()), TipoAggiornamentoIter.GIU);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

		} catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return this.iter(mapping, form, request, response);
	}



	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);


		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		try {
			int codSel;
			String check = "";

			String folder = currentForm.getFolder();
			UserVO utente = navi.getUtente();
			if (folder.equals(ConfigurazioneTipoServizioForm.SERVIZI)) {

				check = currentForm.getSelectedServizio();
				if (ValidazioneDati.isFilled(check)) {
					codSel = Integer.parseInt(check);
					currentForm.setScelRec(currentForm.getLstServizi().get(codSel));
					currentForm.getScelRec().setUteVar(utente.getFirmaUtente());
				}else{
					//Non è stato scelto nessun servizio
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliDirittoCancellare"));

					return mapping.getInputForward();
				}

			}

			if (folder.equals(ConfigurazioneTipoServizioForm.MODALITA)) {

				check = currentForm.getSelectedModalitaErogazione();
				if (ValidazioneDati.isFilled(check)) {
					codSel = Integer.parseInt(check);
					currentForm.setScelRecModErog(currentForm.getLstTariffeModalitaErogazione().get(codSel));
					currentForm.getScelRecModErog().setUteVar(utente.getFirmaUtente());
				}else{
					//Non è stata scelta nessuna modalita
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliModalitaCancellare"));

					return mapping.getInputForward();
				}

			}

			if (folder.equals(ConfigurazioneTipoServizioForm.ITER)) {
				check = currentForm.getProgrIter();
				if (ValidazioneDati.isFilled(check)) {
					codSel = Integer.parseInt(check) - 1;
					IterServizioVO iter = currentForm.getLstIter().get(codSel);
					//controllo mov. attivi su iter da cancellare
					ServiziDelegate delegate = ServiziDelegate.getInstance(request);
					if (delegate.esisteMovimentoAttivoPerIter(iter)) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.cancellaIterServizioAttivo"));
						return mapping.getInputForward();
					}

				}else{
					//Non è stata scelta nessun iter
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliIter"));

					return mapping.getInputForward();
				}
			}

			currentForm.setProv("cancella");
			currentForm.setConferma(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		if (currentForm.getProgrIter()==null || currentForm.getProgrIter().equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliIter"));

			return mapping.getInputForward();
		}

		request.setAttribute(ServiziBaseAction.LISTA_CODICI_ATTIVITA,    currentForm.getLstCodiciAttivita());
		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, currentForm.getTipoServizio());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,          currentForm.getBiblioteca());
		request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR,      mapping.getPath());
		request.setAttribute(ServiziBaseAction.PROGRESSIVO_ITER_SCELTO,  currentForm.getProgrIter());

		return mapping.findForward("inserisciAttivita");
	}


	public ActionForward aggiungi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		request.setAttribute(ServiziBaseAction.LISTA_CODICI_ATTIVITA,    currentForm.getLstCodiciAttivita());
		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, currentForm.getTipoServizio());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,          currentForm.getBiblioteca());
		request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR,      mapping.getPath());

		return mapping.findForward("aggiungiAttivita");
	}


	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		return mapping.findForward("indietro");
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		return mapping.findForward("indietro");
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (((ConfigurazioneTipoServizioForm)form).getFolder().equals(ConfigurazioneTipoServizioForm.SERVIZI)) {
			return this.nuovoServizio(mapping,(ConfigurazioneTipoServizioForm)form, request, response);
		}
		if (((ConfigurazioneTipoServizioForm)form).getFolder().equals(ConfigurazioneTipoServizioForm.MODALITA)) {
			return this.nuovaModalita(mapping, (ConfigurazioneTipoServizioForm)form, request, response);
		}
		if (((ConfigurazioneTipoServizioForm)form).getFolder().equals(ConfigurazioneTipoServizioForm.ITER)) {

		}

		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.folderErrato"));

		return mapping.getInputForward();
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		//Attributi comuni
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,         currentForm.getBiblioteca());
		request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR,     mapping.getPath());
		request.setAttribute(ServiziBaseAction.COD_TIPO_SERVIZIO_ATTR,  currentForm.getTipoServizio().getCodiceTipoServizio());
		request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR, currentForm.getTipoServizio().getDescrizione());
		request.setAttribute(ServiziBaseAction.ID_TIPO_SERVIZIO_ATTR,   currentForm.getTipoServizio().getIdTipoServizio());

		if (currentForm.getFolder().equals(ConfigurazioneTipoServizioForm.SERVIZI)) {
			return this.esaminaServizio(mapping, currentForm, request, response);
		}
		if (currentForm.getFolder().equals(ConfigurazioneTipoServizioForm.MODALITA)) {
			return this.esaminaModalita(mapping, currentForm, request, response);
		}
		if (currentForm.getFolder().equals(ConfigurazioneTipoServizioForm.ITER)) {
			return this.esaminaAttivita(mapping, currentForm, request, response);
		}

		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.folderErrato"));

		return mapping.getInputForward();
	}

	private ActionForward nuovoServizio(ActionMapping mapping, ConfigurazioneTipoServizioForm currentForm,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,           currentForm.getBiblioteca());
		request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR,       mapping.getPath());
		request.setAttribute(ServiziBaseAction.COD_TIPO_SERVIZIO_ATTR,    currentForm.getTipoServizio().getCodiceTipoServizio());
		request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR,   currentForm.getTipoServizio().getDescrizione());
		request.setAttribute(ServiziBaseAction.PENALITA_ATTR,             new Boolean(currentForm.getTipoServizio().isPenalita()));
		request.setAttribute(ServiziBaseAction.LISTA_CODICI_SERVIZI_ATTR, currentForm.getLstCodiciServizio());
		request.setAttribute(ServiziBaseAction.ID_TIPO_SERVIZIO_ATTR,     currentForm.getTipoServizio().getIdTipoServizio());

		return mapping.findForward("nuovoServizio");
	}

	private ActionForward esaminaServizio(ActionMapping mapping, ConfigurazioneTipoServizioForm currentForm,
			HttpServletRequest request, HttpServletResponse response) {
		if (ValidazioneDati.strIsNull(currentForm.getSelectedServizio())) {
		//if (currentForm.getCodServizio()==null || currentForm.getCodServizio().equals("")) {
			//Non è stato scelto nessun servizio

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliDiritto"));

			return mapping.getInputForward();
		}

		request.setAttribute(ServiziBaseAction.PENALITA_ATTR,           new Boolean(currentForm.getTipoServizio().isPenalita()));
		//request.setAttribute(ServiziBaseAction.VO_SERVIZIO_ATTR,        (currentForm.getServiziMap().get(currentForm.getCodServizio())).clone());
		request.setAttribute(ServiziBaseAction.VO_SERVIZIO_ATTR,        (currentForm.getLstServizi().get(Integer.valueOf(currentForm.getSelectedServizio()))).clone());

		return mapping.findForward("esaminaServizio");
	}

	private ActionForward nuovaModalita(ActionMapping mapping, ConfigurazioneTipoServizioForm currentForm,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,           currentForm.getBiblioteca());
		request.setAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR,          mapping.getPath());
		request.setAttribute(ServiziBaseAction.COD_TIPO_SERVIZIO_ATTR,       currentForm.getTipoServizio().getCodiceTipoServizio());
		request.setAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR,      currentForm.getTipoServizio().getDescrizione());
		request.setAttribute(ServiziBaseAction.ID_TIPO_SERVIZIO_ATTR,        currentForm.getTipoServizio().getIdTipoServizio());
		request.setAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR, currentForm.getLstCodiciErogazione());

		return mapping.findForward("nuovaModalita");
	}

	private ActionForward esaminaModalita(ActionMapping mapping, ConfigurazioneTipoServizioForm currentForm,
			HttpServletRequest request, HttpServletResponse response) {
		if (ValidazioneDati.strIsNull(currentForm.getSelectedModalitaErogazione())) {
		//if (currentForm.getCodErog()==null || currentForm.getCodErog().equals("")) {
			//Non è stata scelta nessuna modalita

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliModalita"));

			return mapping.getInputForward();
		}

		request.setAttribute(ServiziBaseAction.VO_TARIFFA_MODALITA_EROGAZIONE, (currentForm.getLstTariffeModalitaErogazione().get(Integer.valueOf(currentForm.getSelectedModalitaErogazione()))).clone());

		return mapping.findForward("esaminaModalita");
	}


	private ActionForward esaminaAttivita(ActionMapping mapping, ConfigurazioneTipoServizioForm currentForm,
			HttpServletRequest request, HttpServletResponse response) {
		if (currentForm.getProgrIter()==null || currentForm.getProgrIter().equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliIter"));

			return mapping.getInputForward();
		}
		request.setAttribute(ServiziBaseAction.VO_ITER_SERVIZIO, (currentForm.getIterMap().get(currentForm.getProgrIter())).clone());
		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, currentForm.getTipoServizio());

		return mapping.findForward("esaminaAttivita");
	}



	////////////////////////////////////////////////////////////////////////////
	/////////////////////        GESTIONE FOLDER           /////////////////////
	////////////////////////////////////////////////////////////////////////////
	public ActionForward servizi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;
		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneTipoServizioForm.SERVIZI);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Diritti");
		navigation.setTesto("Diritti");

		try {
			this.configuraServiziMap(request, currentForm);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward iter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;
		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneTipoServizioForm.ITER);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Iter");
		navigation.setTesto("Iter");

		try {
			this.configuraIterServizioMap(request, form);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward modalita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;
		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneTipoServizioForm.MODALITA);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Modalità erogazione");
		navigation.setTesto("Modalità erogazione");

		try {
			this.configuraTariffeModalitaErogazioneMap(request, (ConfigurazioneTipoServizioForm)form);

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward moduloRichiesta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;
		currentForm.setConferma(false);
		currentForm.setFolder(ConfigurazioneTipoServizioForm.MODULORICHIESTA);

		Navigation navigation =Navigation.getInstance(request);
		navigation.setDescrizioneX("Modulo richiesta");
		navigation.setTesto("Modulo richiesta");

		try {

			if (currentForm.getVisibileModuloRichiesta() != null && currentForm.getVisibileModuloRichiesta().equals("SI")) {
				currentForm.setVisibileModuloRichiesta(null);
			}
			else {
				this.configuraModuloRichiesta(request, (ConfigurazioneTipoServizioForm)form);

				List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO = currentForm.getLstServizioWebDatiRichiesti();
				currentForm.setLstUltimoSalvatoModuloRichiesta(ClonePool.deepCopy(lstServizioWebDatiRichiestiVO));
			}

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


	////////////////////////////////////////////////////////////////////////////
	/////////////////////////   FINE GESTIONE FOLDER    ////////////////////////
	////////////////////////////////////////////////////////////////////////////

	private void configuraServiziMap(HttpServletRequest request, ConfigurazioneTipoServizioForm form)
	throws RemoteException {
		Navigation navi = Navigation.getInstance(request);

		//Ricerca servizi associati
		List<ServizioBibliotecaVO> listaServizi = this.getListaServizi(navi.getUtente().getCodPolo(),
				form.getBiblioteca(), form.getTipoServizio().getCodiceTipoServizio(), request);
		form.setLstServizi(listaServizi);

		Map<String, ServizioBibliotecaVO> serviziMap = new HashMap<String, ServizioBibliotecaVO>();
		List<String> lstCodiciServizio = new ArrayList<String>();

		Iterator<ServizioBibliotecaVO> iterator = form.getLstServizi().iterator();
		ServizioBibliotecaVO servizioVO;
		while (iterator.hasNext()) {
			servizioVO = iterator.next();
			//
			serviziMap.put(servizioVO.getCodServ(), servizioVO);
			//
			lstCodiciServizio.add(servizioVO.getCodServ().trim());
		}
		form.setServiziMap(serviziMap);
		form.setLstCodiciServizio(lstCodiciServizio);

		if (lstCodiciServizio.size() == 1) {
			form.setSelectedServizio("0");
		} else {
			form.setSelectedServizio(null);
		}
	}


	private void configuraTariffeModalitaErogazioneMap(HttpServletRequest request, ConfigurazioneTipoServizioForm currentForm)
	throws RemoteException, CreateException, ParseException {
		Navigation navi = Navigation.getInstance(request);

		List<TariffeModalitaErogazioneVO> listaTariffeModalitaErogazione = this.getTariffeModalitaErogazione(navi.getUtente().getCodPolo(),
				currentForm.getBiblioteca(), currentForm.getTipoServizio().getCodiceTipoServizio(), request);
		currentForm.setLstTariffeModalitaErogazione(listaTariffeModalitaErogazione);

		Map<String, TariffeModalitaErogazioneVO> tariffeModalitaErogazioneMap = new HashMap<String, TariffeModalitaErogazioneVO>();
		Iterator<TariffeModalitaErogazioneVO> i = listaTariffeModalitaErogazione.iterator();
		TariffeModalitaErogazioneVO tariffaModalitaVO;
		Locale locale = this.getLocale(request, Constants.SBN_LOCALE);//request.getLocale();
		double tariffa;
		double costoUnitario;
		List<String> lstCodiciErogazione = new ArrayList<String>();
		while (i.hasNext()) {
			tariffaModalitaVO = i.next();
			if (!locale.equals(tariffaModalitaVO.getLocale())) {
				//il locale dell'istanza di TariffeModalitaErogazioneVO non corrisponde a quello della request
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
		currentForm.setTariffeModalitaErogazioneMap(tariffeModalitaErogazioneMap);
		currentForm.setLstCodiciErogazione(lstCodiciErogazione);

		if (currentForm.getLstTariffeModalitaErogazione() != null && currentForm.getLstTariffeModalitaErogazione().size() == 1) {
			currentForm.setSelectedModalitaErogazione("0");
		} else {
			currentForm.setSelectedModalitaErogazione(null);
		}

		currentForm.setStringaMessaggioModalitaUltMod("");
		currentForm.setStringaMessaggioNoModErogSeAncoraPresente("");

		if (currentForm.getUlt_supp() != null && !currentForm.getUlt_supp().equals("S")){

			// se ult_supp è diverso da "S"

			if (currentForm.getUlt_mod() != null && currentForm.getUlt_mod().equals("S")){
				// se il flag relativo a ult_mod  sulla tabella codici è "S"
				// verifico se ci sono o meno modalità di erogazione associate al servizio
				if (currentForm.getLstTariffeModalitaErogazione() != null && currentForm.getLstTariffeModalitaErogazione().size() == 0) {
					// in caso contrario impostao il relativo messaggio
					currentForm.setStringaMessaggioModalitaUltMod("ATTENZIONE: al servizio non è stata ancora associata alcuna modalità di erogazione");
				}
				else {
					currentForm.setStringaMessaggioModalitaUltMod("");
				}
			}

			if (currentForm.getUlt_mod() != null && currentForm.getUlt_mod().equals("N")) {
				// se non sono previste modalità di erogazione
				// verifico se ci sono o meno modalità di erogazione associate al servizio
				if (currentForm.getLstTariffeModalitaErogazione() != null && currentForm.getLstTariffeModalitaErogazione().size() > 0) {
					// se ci sono ancora modalità di erogazione associate a quel servizio
					// imposto l'ulteriore messaggio
					currentForm.setStringaMessaggioNoModErogSeAncoraPresente("Attenzione. Per il servizio risultano presenti delle Modalità di erogazione. " +
						"Aggiornare la tabella codici indicando Modalità 'Si' e eliminare " +
						"le modalità presenti prima di riportare Modalità a 'No'");
				} else {
					// e non c'è ancora un iter associato a quel servizio
					// pulisco il messaggio
					currentForm.setStringaMessaggioNoModErogSeAncoraPresente("");
				}
			}
		}

	}

	private void configuraModuloRichiesta(HttpServletRequest request, ConfigurazioneTipoServizioForm form)
	throws RemoteException, CreateException, ParseException {
		Navigation navi = Navigation.getInstance(request);

		List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO = this.getModuloRichiesta(navi.getUtente().getCodPolo(),
				form.getBiblioteca(), form.getTipoServizio().getCodiceTipoServizio(), request, null);
		form.setLstServizioWebDatiRichiesti(lstServizioWebDatiRichiestiVO);

	}



	private void configuraIterServizioMap(HttpServletRequest request, ActionForm form)
	throws Exception {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm) form;
		Navigation navi = Navigation.getInstance(request);

		List<IterServizioVO> listaIterServizio = this.getIterServizio(navi.getUtente().getCodPolo(),
				currentForm.getBiblioteca(), currentForm.getTipoServizio().getCodiceTipoServizio(), request);
		currentForm.setLstIter(listaIterServizio);

		TreeMap<String, IterServizioVO> iterServizioMap = new TreeMap<String, IterServizioVO>();
		List<String> lstCodiciAttivita = new ArrayList<String>();
		IterServizioVO iterServizioVO;
		String cod_stato_richiesta_appo = "";
		String cod_stato_movimento_appo = "";
		for (int i = 0; i < ValidazioneDati.size(listaIterServizio); i++) {
			iterServizioVO = listaIterServizio.get(i);
			iterServizioMap.put(iterServizioVO.getProgrIter().toString(), iterServizioVO);
			String codAttivita = iterServizioVO.getCodAttivita();
			lstCodiciAttivita.add(codAttivita);
			cod_stato_richiesta_appo = iterServizioVO.getCodStatoRich();
			cod_stato_movimento_appo = iterServizioVO.getCodStatoMov();

			//check posizione
			StatoIterRichiesta stato = StatoIterRichiesta.of(codAttivita);
			int position = stato.getPosition();
			if (position > 0 && position != (i + 1)) {
				currentForm.setStringaMessaggioIterKO(LinkableTagUtils.findMessage(request,
						Locale.getDefault(), "errors.servizi.configurazione.tipoServizio.iter.posizione.errata",
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_ATTIVITA_ITER, codAttivita), position));
				currentForm.setStringaMessaggioIterOK("");
				return;
			}
		}
		if (cod_stato_richiesta_appo.equals("H") && cod_stato_movimento_appo.equals("C")){
			// se per l'ultima attività lo stato richiesta è "concluso"
			// e lo stato movimento è "chiuso"
			// imposto il messaggio:
			// "Iter completo"
			currentForm.setStringaMessaggioIterOK("Iter completo");
			currentForm.setStringaMessaggioIterKO("");
		}
		else {
			// se per l'ultima attività lo stato richiesta non è "concluso"
			// e lo stato movimento non è "chiuso"
			// imposto il messaggio:
			// "ATTENZIONE Iter incompleto ..."
			currentForm.setStringaMessaggioIterKO(LinkableTagUtils.findMessage(request,
				Locale.getDefault(), "errors.servizi.configurazione.tipoServizio.iter.non.completo"));
			currentForm.setStringaMessaggioIterOK("");
		}
		currentForm.setIterMap(iterServizioMap);
		currentForm.setLstCodiciAttivita(lstCodiciAttivita);

		if (lstCodiciAttivita.size() == 1)
			currentForm.setProgrIter("1");

		if (currentForm.getRichiede_iter() != null && currentForm.getRichiede_iter().equals("N")) {
			// se l'iter non deve essere presente
			if (currentForm.getLstIter() != null && currentForm.getLstIter().size() > 0) {
				// e c'è ancora un iter associato a quel servizio
				// imposto l'ulteriore messaggio
				currentForm.setStringaMessaggioNoIterSeAncoraPresente("Attenzione. Per il servizio risultano presenti delle attività. " +
					"Aggiornare la tabella codici indicando Iter 'Si' e eliminare " +
					"le attività presenti prima di riportare Iter a 'No'");
			} else {
				// e non c'è ancora un iter associato a quel servizio
				// pulisco il messaggio
				currentForm.setStringaMessaggioNoIterSeAncoraPresente("");
			}
		}

	}


	private void checkScambia(ConfigurazioneTipoServizioForm currentForm, HttpServletRequest request, TipoAggiornamentoIter tipoOp)
	throws ValidationException {

		boolean checkOK=true;

		//codControlloScelto in realtà contiene il progressivo fase
		if ( (new Integer(currentForm.getProgrIter())==1 && tipoOp.equals(TipoAggiornamentoIter.SU))
				||
				(new Integer(currentForm.getProgrIter()) == ValidazioneDati.size(currentForm.getIterMap()) && tipoOp.equals(TipoAggiornamentoIter.GIU))) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));

			checkOK=false;
		}
		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}

	private ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			check(form, request);
			TipoServizioVO tipoServizioVO = currentForm.getTipoServizio();
			if (!tipoServizioVO.equals(currentForm.getUltimoSalvato())) {
				String utente = navi.getUtente().getFirmaUtente();
				Timestamp now = DaoManager.now();

				if (currentForm.isNuovo()) {
					//Nuovo tipo servizio
					tipoServizioVO.setUteIns(utente);
					tipoServizioVO.setUteVar(utente);
					tipoServizioVO.setTsIns(now);
					tipoServizioVO.setTsVar(now);
					tipoServizioVO.setFlCanc("N");

					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					GestioneServizi servizi = factory.getGestioneServizi();
					boolean operazioneOK=servizi.aggiornaTipoServizio(navi.getUserTicket(), tipoServizioVO);
					if (operazioneOK) {
						currentForm.setUltimoSalvato((TipoServizioVO)tipoServizioVO.clone());

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
						this.resetToken(request);
						return mapping.getInputForward();
						//return mapping.findForward("indietro");
					} else {

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));

					}
				} else {
					//Aggiornamento tipo servizio
					tipoServizioVO.setUteVar(utente);
					tipoServizioVO.setTsVar(now);
					//Conferma all'utente dell'aggiornamento
					currentForm.setConferma(true);
					//imposto il flag che servirà ad effettuare l'aggiornamento del Servizio successivamente alla conferma
					currentForm.setAggiornaServizio(true);
					if (currentForm.getProv() != null && currentForm.getProv().equals("aggiornaModuloRichiesta")){
						if (currentForm.isDiversoModRichiesta()) {
							currentForm.setDiversoModRichiesta(false);
							//imposto il flag che servirà ad effettuare l'aggiornamento del Modulo Richiesta successivamente alla conferma
							currentForm.setAggiornaModuloRichiesta(true);
						}
					}

					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();
				}
			}
			else {
				if (currentForm.getProv() != null && currentForm.getProv().equals("aggiornaModuloRichiesta")){
					if (currentForm.isDiversoModRichiesta()) {
						currentForm.setDiversoModRichiesta(false);
						//Conferma all'utente dell'aggiornamento
						currentForm.setConferma(true);
						//imposto il flag che servirà ad effettuare l'aggiornamento del Modulo Richiesta successivamente alla conferma
						currentForm.setAggiornaModuloRichiesta(true);

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

						this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
						return mapping.getInputForward();
					}
					else {

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
						this.resetToken(request);
						return mapping.getInputForward();
					}

				}
				else {

					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));

					resetToken(request);
					return mapping.getInputForward();
				}
			}
		} catch (ValidationException e) {
			resetToken(request);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneTipoServizioForm dettaglio = (ConfigurazioneTipoServizioForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {

			if (!((ConfigurazioneTipoServizioForm)form).getFolder().equals(ConfigurazioneTipoServizioForm.MODULORICHIESTA)) {
				// se non proveniamo dal folder relativo al Modulo richiesta
				// vado direttamente al salvataggio
				return this.salva(mapping, form, request, response);
			}

			// nel caso in cui provengo dal folder relativo al Modulo richiesta
			// effettuo prima l'aggiornamento al Modulo richiesta
			// e successivamente (vedi ultima istruzione del metodo)
			// anche il salvataggio delle eventuali variazioni al Servizio

			List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO = dettaglio.getLstServizioWebDatiRichiesti();

			dettaglio.setDiversoModRichiesta(false);
			Navigation navi = Navigation.getInstance(request);
			String utente = navi.getUtente().getFirmaUtente();

			List<ServizioWebDatiRichiestiVO> listaModificati = new ArrayList<ServizioWebDatiRichiestiVO>();

			// verifico che ci sia almeno un elemento della Lista
			// che sia diverso per poter permettere l'aggiornamento
			for (int c = 0; c < lstServizioWebDatiRichiestiVO.size(); c++)  {
				if (lstServizioWebDatiRichiestiVO.get(c).getTsIns() == null ||
				    (!lstServizioWebDatiRichiestiVO.get(c).equals(dettaglio.getLstUltimoSalvatoModuloRichiesta().get(c)))) {
					// se non ho ancora inserito sul DB oppure ho cambiato qualcosa
					dettaglio.setDiversoModRichiesta(true);
					break;
				}
			}
			if (dettaglio.isDiversoModRichiesta()) {
				listaModificati = lstServizioWebDatiRichiestiVO;
				for (int c = 0; c < lstServizioWebDatiRichiestiVO.size(); c++)  {
					// Imposto il campo timbro per il successivo aggiornamento del Modulo richiesta
					// (solo UteVar perchè TsVar vine messo dal sistema)
					lstServizioWebDatiRichiestiVO.get(c).setUteVar(utente);
				}
			}

			dettaglio.setProv("aggiornaModuloRichiesta");
			dettaglio.setLstModificati(listaModificati);

			// effettuo anche il salvataggio delle eventuali variazioni al Servizio
			return this.salva(mapping, form, request, response);

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
		ConfigurazioneTipoServizioForm dettaglio = (ConfigurazioneTipoServizioForm)form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettaglio.isSessione()) {
			dettaglio.setSessione(true);
		}

		dettaglio.setConferma(false);
		resetToken(request);
		return mapping.getInputForward();
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		try {
			if (ValidazioneDati.equals(currentForm.getProv(), "cancella")) {
				currentForm.setProv(null);
				if (currentForm.getFolder().equals("I")) {

					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					//boolean operazioneOk=factory.getGestioneServizi()
					//.cancellaIterServizio(Navigation.getInstance(request).getUserTicket(), new String[]{dettaglio.getProgrIter()},
					//		dettaglio.getTipoServizio().getIdTipoServizio(), Navigation.getInstance(request).getUtente().getFirmaUtente());
					//				.aggiornaIter(currentForm.getTipoServizio().getIdTipoServizio(),
					//							new Integer(currentForm.getProgrIter()).intValue(),
					//							null, TipoAggiornamentoIter.CANCELLAZIONE, false);

					boolean operazioneOk = factory.getGestioneServizi()
					.aggiornaIter(Navigation.getInstance(request).getUserTicket(), currentForm.getTipoServizio().getIdTipoServizio(),
							new Integer(currentForm.getProgrIter()).intValue(), null,
								  TipoAggiornamentoIter.CANCELLAZIONE, false);

					if (operazioneOk) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

						this.configuraServiziMap(request, currentForm);
						currentForm.setConferma(false);
						return this.iter(mapping, form, request, response);
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.CancellazioneIterNonEffettuata"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}else if (currentForm.getFolder().equals("S")){


					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					boolean operazioneOk=factory.getGestioneServizi().cancellaServizio(Navigation.getInstance(request).getUserTicket(), currentForm.getScelRec(), currentForm.getTipoServizio().getIdTipoServizio());
					if (operazioneOk) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));

						this.configuraServiziMap(request, currentForm);
						currentForm.setConferma(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceErroreCancellazione"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}else if (currentForm.getFolder().equals("M")){

					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					List<TariffeModalitaErogazioneVO> lstTariffeModalitaErogazione = currentForm.getLstTariffeModalitaErogazione();
					int selected = Integer.valueOf(currentForm.getSelectedModalitaErogazione());
					TariffeModalitaErogazioneVO modErog = (TariffeModalitaErogazioneVO) lstTariffeModalitaErogazione.get(selected).clone();
					boolean operazioneOk=factory.getGestioneServizi().cancellaTariffeModalitaErogazione(Navigation.getInstance(request).getUserTicket(), modErog,
							currentForm.getTipoServizio().getIdTipoServizio());
					if (operazioneOk) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));

						this.configuraTariffeModalitaErogazioneMap(request, currentForm);

						currentForm.setConferma(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceErroreCancellazione"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
			}else{
				//aggiorna il modulo richiesta
				if (currentForm.isAggiornaModuloRichiesta() == true) {
					currentForm.setAggiornaModuloRichiesta(false);
					if (currentForm.getProv() != null && currentForm.getProv().equals("aggiornaModuloRichiesta")){
						currentForm.setProv(null);
						// aggiorna il Modulo richiesta
						List<ServizioWebDatiRichiestiVO> lstModificati = currentForm.getLstModificati();
						FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
						GestioneServizi servizi = factory.getGestioneServizi();
						boolean operazioneOK=servizi.aggiornaModuloRichiesta(Navigation.getInstance(request).getUserTicket(), lstModificati);
						if (operazioneOK) {
							this.configuraModuloRichiesta(request, (ConfigurazioneTipoServizioForm)form);
							List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO = currentForm.getLstServizioWebDatiRichiesti();
							currentForm.setLstUltimoSalvatoModuloRichiesta(ClonePool.deepCopy(lstServizioWebDatiRichiestiVO));
							if (currentForm.isAggiornaServizio() == false) {

								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
								this.
								resetToken(request);
								currentForm.setConferma(false);
								return mapping.getInputForward();
								//return mapping.findForward("indietro");
							}
						} else {

							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));

							currentForm.setConferma(false);
							return mapping.getInputForward();
						}
					}
				}

				// aggiorna il Tipo Servizio
				if (currentForm.isAggiornaServizio() == true) {
					currentForm.setAggiornaServizio(false);
					TipoServizioVO tipoServizioVO = currentForm.getTipoServizio();
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					GestioneServizi servizi = factory.getGestioneServizi();
					boolean operazioneOK=servizi.aggiornaTipoServizio(Navigation.getInstance(request).getUserTicket(), tipoServizioVO);
					if (operazioneOK) {
						currentForm.setUltimoSalvato((TipoServizioVO)tipoServizioVO.clone());

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

						resetToken(request);
						currentForm.setConferma(false);
						return mapping.getInputForward();
						//return mapping.findForward("indietro");
					} else {

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
			}
		} catch (ApplicationException e) {
			resetToken(request);
			log.error("", e);

			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage()));

			return mapping.getInputForward();
		} catch (NumberFormatException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);

			return mapping.getInputForward();
		} catch (RemoteException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);

			return mapping.getInputForward();
		} finally {
			currentForm.setProv(null);
			currentForm.setConferma(false);
		}

		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "SERVIZIO_ILL")) {
			try {
				ConfigurazioneTipoServizioForm currentForm = (ConfigurazioneTipoServizioForm) form;
				TipoServizioVO tipoServizioVO = currentForm.getTipoServizio();
				CodTipoServizio ts = CodTipoServizio.of(getCodice(tipoServizioVO.getCodiceTipoServizio(), CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN, request));
				return ts != null && !ts.isLocale();

			} catch (Exception e) {
				return false;
			}
		}
		return super.checkAttivita(request, form, idCheck);
	}

}
