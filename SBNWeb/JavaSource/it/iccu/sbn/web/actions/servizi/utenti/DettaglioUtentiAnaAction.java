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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.SinteticaUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.BiblioPoloVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.DocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.IndirizzoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ProfessioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ResidenzaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Constants.Servizi;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.actionforms.servizi.utenti.DettaglioUtentiAnaForm;
import it.iccu.sbn.web.actionforms.servizi.utenti.DettaglioUtentiAnaForm.RichiestaType;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.servizi.util.FileConstants;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


public class DettaglioUtentiAnaAction extends UtenteBaseAction {

	public static final String UTENTI_POLO_CONTROLLATO = "utenti.polo.checked";

	private static Logger log = Logger.getLogger(DettaglioUtentiAnaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok",                        "ok");
		map.put("servizi.bottone.si",                        "si");
		map.put("servizi.bottone.no",                        "no");
		map.put("servizi.bottone.nuovo",                     "nuovo");
		map.put("servizi.bottone.cancella",                  "cancella");
//		map.put("servizi.bottone.annulla",                   "annulla");
		map.put("servizi.bottone.indietro",                  "annulla");
		map.put("servizi.bottone.tesserino",                 "tesserino");
		map.put("servizi.bottone.impBiblio",                 "impBiblio");
		map.put("servizi.utenti.tag.anagrafica",             "anagrafe");
		map.put("servizi.utenti.tag.autorizzazioni",         "autorizzazioni");
		map.put("servizi.utenti.tag.bibliotecaPolo",         "bibliotecapolo");
		map.put("servizi.bottone.inserisciServizio2",        "insServizio");
		map.put("servizi.bottone.cancellaServizio2",         "cancellaServizio");
		map.put("servizi.bottone.aggiornaMappaPerParametri", "aggParametri");
		map.put("servizi.bottone.aggiornaServiziXAuto",      "aggiornaServiziPerAutorizzazione");
		map.put("servizi.bottone.altriDatiUtente",           "altriDati");
		map.put("servizi.bottone.chiudiAltriDatiUtente",     "chiudiAltriDati");
		map.put("servizi.bottone.materie",                   "materie");
		map.put("servizi.bottone.resetPwd",                  "resetPassword");
		map.put("servizi.bottone.scorriAvanti",				 "scorriAvanti");
		map.put("servizi.bottone.scorriIndietro",			 "scorriIndietro");
		map.put("servizi.utenti.persona",   			     "imponiTipoUtenteP");
		map.put("servizi.utenti.ente",     					 "imponiTipoUtenteE");
		map.put("servizi.utenti.confermaTipoUtente",   		 "imponiTipoUtente");
		map.put("servizi.bottone.importaBib",     			 "importaBibl");

		map.put("servizi.bottone.scegli",                    "scegli");

		return map;
	}

	private void checkFormPerCancellazione(HttpServletRequest request,
			ActionForm form) throws Exception {

		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		ActionMessages errors = new ActionMessages();
		int error = 0;
		// controllo Richieste
		error = this.verificaMovimenti(currentForm.getUteAna().getIdUtenteBiblioteca(), Navigation.getInstance(request).getUtente().getTicket());
		if (error == -1) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.erroreLetturaMovimenti"));
			this.saveErrors(request, errors);
			throw new Exception();
		}
		if (error > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.cancellazioneImpossibileMovimentiPresenti"));
			this.saveErrors(request, errors);
			throw new Exception();
		}
	}

	private void checkDatiAnagrafici(HttpServletRequest request, ActionForm form)	throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		ActionMessages errors = new ActionMessages();
		boolean error = false;
		DocumentoVO doc;
		String data = "";
		// String dataOLD = "";
		int codRitorno = -1;

		UtenteBibliotecaVO dettaglioUtente = currentForm.getUteAna();
		data = dettaglioUtente.getCodPoloSer();
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.errorePoloServizioCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new Exception();
		}
		data = dettaglioUtente.getCodBibSer();
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreBibServizioCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new Exception();
		}
		data = dettaglioUtente.getCodPolo();
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.errorePoloCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new Exception();
		}
		data = dettaglioUtente.getCodiceBiblioteca();
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreBibUtenteCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new Exception();
		}
		data = dettaglioUtente.getCodiceUtente();
		if (!dettaglioUtente.isNuovoUte()) {
			if (ValidazioneDati.strIsNull(data)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreUtenteCampoObbligtorio"));
				this.saveErrors(request, errors);
				// error = true;
				throw new Exception();
			}
		}

		data = dettaglioUtente.getCognome();
		if (ValidazioneDati.isFilled(data)) {
			if (data.trim().length() > 80) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCognomeTroppoLungo"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
			dettaglioUtente.setCognome(data.trim());
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCognomeObbligatorio"));
			this.saveErrors(request, errors);
			throw new Exception();
		}

		ProfessioniVO professione = dettaglioUtente.getProfessione();
		boolean isPersonaFisica = professione.isPersonaFisica();
		if (isPersonaFisica) {
			data = dettaglioUtente.getNome();
			if (ValidazioneDati.isFilled(data)) {
				try {
					if (data.trim().length() > 50) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoNomeTroppoLungo"));
						this.saveErrors(request, errors);
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception();
				}
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoNomeObbligatorio"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
		}

		AnagrafeVO anagrafe = dettaglioUtente.getAnagrafe();
		// RIPRISTINATO PER PROBLEMI CON INDICE  && dettaglioUtente.getProfessione().isPersonaFisica()
		if (ValidazioneDati.strIsNull(anagrafe.getCodFiscale()) && isPersonaFisica ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreCodiceFiscaleCampoObbligatorio"));
			this.saveErrors(request, errors);
			error = true;
		}
		else if (ValidazioneDati.isFilled(anagrafe.getCodFiscale())) {
			//dettaglioUtente.getAnagrafe().setCodFiscale(anagrafe.getCodFiscale().trim().toUpperCase());
			if (isPersonaFisica) {
				if (anagrafe.getCodFiscale().length() != 16) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.lunghezzaCodiceFiscaleErrata"));
					this.saveErrors(request, errors);
					error = true;
				}
			} else {
				//almaviva5_20110228 #4207 aggiunto, per enti, codice anagrafe di 6 car.
				if (!ValidazioneDati.in(anagrafe.getCodFiscale().length(), 16, 11, 6) ) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.lunghezzaCodiceFiscaleErrata.ente"));
					this.saveErrors(request, errors);
					error = true;
				}
			}
		}
		if (error)
			throw new Exception();


		// controllo esistenza utente con lo stesso cod fisc
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		DescrittoreBloccoVO blocco = null;
		try {
			blocco = delegate.verificaEsistenzaUtentePolo(currentForm.getUteAna(), Navigation.getInstance(request).getUserTicket());
		} catch (Exception e) {
			log.error("", e);
		}

		if (DescrittoreBloccoVO.isFilled(blocco) ) {
			String var = "N";
			// E=erogante A=altra biblioteca T=tutte
			for (int index = 0; index < blocco.getLista().size(); index++) {
				SinteticaUtenteVO utePolo = (SinteticaUtenteVO) blocco.getLista().get(index);
				// correzione rox 10.11.09 misunderstanding biblio e polo di utebiblio ovvero per utente-biblioteca
				//if (utePolo.getBiblioDiUteBiblio().equals(Navigation.getInstance(request).getUtente().getCodBib())) {
				//if (utePolo.getBibErogante().equals(Navigation.getInstance(request).getUtente().getCodBib())) {
				if (utePolo.getBibErogante().equals(currentForm.getBiblioteca())) {
					if (var != null && var.equals("A")) {
						var = "T";
					} else {
						var = "E";
					}
					// l'utente risulta già iscritto in questa biblioteca. Si
					// vuole aggiornare i dati ?
				} else {
					if (var != null && var.equals("E")) {
						var = "T";
					} else {
						var = "A";
					}
					// "utente già isceritto presso altra biblioteca del polo .
					// Si per importarne i dati anagrafici no per annullare
					// l'operazione"?
				}
			}

			if (dettaglioUtente.isImportato() && (var.equals("T") || var.equals("E"))) {
				dettaglioUtente.setTipoIscrizione(var);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoErogante"));
				this.saveErrors(request, errors);
				error = true;
			}

			if (var.equals("E")) {
				// iscritto solo a bib Erogante
				dettaglioUtente.setTipoIscrizione("E");
				if (currentForm.getUteAna().isNuovoUte())
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoErogante"));
					this.saveErrors(request, errors);
					error = true;
				}
				//return;
			}
			if (var.equals("T")) {
				// iscritto a bib Erogante ed ad Altre = Tutte
				dettaglioUtente.setTipoIscrizione("T");
				if (currentForm.getUteAna().isNuovoUte())
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoInTutte"));
					this.saveErrors(request, errors);
					error = true;
				}

				//return;
			}
			if (var.equals("A")) {
				// iscritto solo ad Altre bib
				dettaglioUtente.setTipoIscrizione("A");
				if (currentForm.getUteAna().isNuovoUte() && !dettaglioUtente.isImportato() )
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoAltraBiblio"));
					// lo faccio uscire senza exception per proseguire con la conferma
					this.saveErrors(request, errors);
					error = true;
				}
				//return;
			}

			//this.saveErrors(request, errors);
			if (error) {
				throw new Exception();
			}
			//throw new Exception();

		}

		//Controllo se dati residenza sono impostati
		ResidenzaVO residenza = anagrafe.getResidenza();
		if (!residenza.impostato()) {
			//almaviva5_20130115 #5223
			if (isPersonaFisica)
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.datiResidenzaObbligatori"));
			else
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.datiSedeObbligatori"));
			this.saveErrors(request, errors);
			throw new Exception();
		}

		data = residenza.getVia();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 105) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoIndResTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = residenza.getCitta();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCittaResTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = residenza.getCap();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 10) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCapResTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		//telefono residenza e' in realta' il telefono fisso
		data = residenza.getTelefono();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 20) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoTelResTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		} else {
			if (dettaglioUtente.getTipoSMS()==UtenteBibliotecaVO.SMS_SU_FISSO) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.richiestoNumeroTelefonoFisso"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
		}
		data = residenza.getFax();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 20) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoFaxResTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}

		//almaviva5_20151120 dati opzionali:
		//se utente estero cap e provincia non sono obbligatori
		String nazionalita = residenza.getNazionalita();
		boolean isEstero = !nazionalita.equals(CommonConfiguration.getProperty(Configuration.SRV_UTENTE_NAZIONE));

		IndirizzoVO domicilio = anagrafe.getDomicilio();
		if (domicilio.almenoUnaProprietaImpostata()
				&& (isEstero ? !domicilio.impostatoXResidenzaEstera() : !domicilio.impostato() )
				&& isPersonaFisica ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.datiDomicilioIncompleti"));
			this.saveErrors(request, errors);
			throw new Exception();
		} else {
			if (isPersonaFisica
					&& (isEstero ? !domicilio.impostatoXResidenzaEstera() : !domicilio.impostato() ) ) {
				//Non è impostato il domicilio. I dati saranno gli stessi della residenza
				currentForm.setFlagDomUgualeRes(true);
				domicilio.setVia(residenza.getVia());
				domicilio.setCap(residenza.getCap());
				domicilio.setCitta(residenza.getCitta());
				domicilio.setFax(residenza.getFax());
				domicilio.setProvincia(residenza.getProvincia());
				//almaviva5_20101207 #4028 il numero di tel. domicilio é usato per il cellulare
				//domicilio.setTelefono(residenza.getTelefono());
			}
		}

		data = domicilio.getVia();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 105) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoIndDomTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = domicilio.getCitta();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCittaDomTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = domicilio.getCap();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 10) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCapDomTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		//telefono domicilio e' in realta' il telefono cellulare
		data = domicilio.getTelefono();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 20) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoTelDomTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		} else {
			if (dettaglioUtente.getTipoSMS()==UtenteBibliotecaVO.SMS_SU_MOBILE) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.richiestoNumeroTelefonoMobile"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
		}
		data = domicilio.getFax();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 20) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoFaxDomTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}

		if (isPersonaFisica) {
			data = anagrafe.getLuogoNascita();
			if (ValidazioneDati.isFilled(data)) {
				try {
					if (data.trim().length() > 30) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoLuogoNascitaTroppoLungo"));
						this.saveErrors(request, errors);
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception();
				}
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoLuogoNascitaObbligatorio"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
		}

		String ateneo = professione.getAteneo();
		if (ValidazioneDati.isFilled(ateneo)) {
			try {
				if (ateneo.length() > 3) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCodAteneoTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		String matricola = professione.getMatricola();
		if (ValidazioneDati.isFilled(matricola)) {
			try {
				if (matricola.length() > 25) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoMatricolaTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}

		//almaviva5_20110428 #4400
		if ((ValidazioneDati.isFilled(ateneo) && !ValidazioneDati.isFilled(matricola)) ||
			(!ValidazioneDati.isFilled(ateneo) && ValidazioneDati.isFilled(matricola))) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.utenti.ricercaPresenzaMatricolaAteneo"));
			throw new Exception();
		}

		data = professione.getCorsoLaurea();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 26) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCorsoLaureaTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = anagrafe.getPostaElettronica();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 80) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoEMailTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = professione.getReferente();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoReferenteTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = dettaglioUtente.getBibliopolo().getPoloNote();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoPoloNoteTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = dettaglioUtente.getBibliopolo().getPoloInfrazioni();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.trim().length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoInfrazioniTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		for (int idx = 0; idx < 4; idx++) {
			doc = dettaglioUtente.getDocumento().get(idx);
			if (ValidazioneDati.isFilled(doc.getNumero())) {
				try {
					if (doc.getNumero().length() > 20) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoDocnNumTroppoLungo", idx));
						this.saveErrors(request, errors);
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception();
				}
			}

			if (ValidazioneDati.isFilled(doc.getAutRilascio())) {
				try {
					if (doc.getAutRilascio().length() > 40) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoDocAutRilTroppoLungo", idx));
						this.saveErrors(request, errors);
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception();
				}
			}

			//almaviva5_20110225 #4108 controllo validità doc.
			try {
				doc.validate();
			} catch (ValidationException e) {
				LinkableTagUtils.addError(request, e);
				throw new Exception();
			}


		}
		data = domicilio.getProvincia();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 2) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoProvDomTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = residenza.getProvincia();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 2) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoProvResTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}

		dettaglioUtente.getBibliopolo().setLocale(this.getLocale(request, Constants.SBN_LOCALE));
		dettaglioUtente.getBibliopolo().setNumberFormat(ServiziConstant.NUMBER_FORMAT_CONVERTER);
		data = dettaglioUtente.getBibliopolo().getPoloCredito();
		if (ValidazioneDati.isFilled(data)) {
			try {
				double poloCredito=dettaglioUtente.getBibliopolo().getPoloCreditoDouble();
				log.debug("credito polo: " + poloCredito);
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCreditoPoloErrato"));
				this.saveErrors(request, errors);
			}
		}

		data = dettaglioUtente.getBibliopolo().getBiblioCredito();
		if (ValidazioneDati.isFilled(data)) {
			try {
				double biblioCredito=dettaglioUtente.getBibliopolo().getBiblioCreditoDouble();
				log.debug("credito biblio: " + biblioCredito);
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoCreditoBiblioErrato"));
				this.saveErrors(request, errors);
			}
		}
		/*	almaviva5_20130827 segnalazione TO0: eliminato limite lunghezza note
		data = dettaglioUtente.getBibliopolo().getBiblioNote();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoBiblioNoteTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		*/
		if (isPersonaFisica) {
			data = anagrafe.getSesso();
			if (ValidazioneDati.isFilled(data)) {
				try {
					if (data.length() > 1) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoSessoTroppoLungo"));
						this.saveErrors(request, errors);
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception();
				}
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoSessoObbligatorio"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
		}

		data = anagrafe.getProvenienza();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 1) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoProvenienzaTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		data = professione
				.getTipoPersona();
		if (ValidazioneDati.isFilled(data)) {
			try {
				if (data.length() > 1) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.campoTipoPersonaTroppoLungo"));
					this.saveErrors(request, errors);
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}

		data = anagrafe.getDataNascita();
		if (isPersonaFisica)
		{
			if (ValidazioneDati.strIsNull(data)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreDataCampoObbligtorio"));
				this.saveErrors(request, errors);
				throw new Exception();
			}
			try {
					codRitorno = ValidazioneDati.validaDataPassata(data);
					if (codRitorno != ValidazioneDati.DATA_OK)
						if (codRitorno == -1) {
							codRitorno = ValidazioneDati.DATA_ERRATA;
						}
						throw new Exception();
			} catch (Exception e) {
					switch (codRitorno) {
					case ValidazioneDati.DATA_ERRATA:
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataFormatoErrore"));
						this.saveErrors(request, errors);
						error = true;
						break;
					case ValidazioneDati.DATA_MAGGIORE:
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMaggioreErrore"));
						this.saveErrors(request, errors);
						error = true;
						break;
					case ValidazioneDati.DATA_PASSATA_ERRATA:
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataVuotaErrore"));
						this.saveErrors(request, errors);
						error = true;
						break;
					}
			}
			if (error) {
				throw new Exception();
			}
		}

//			data = currentForm.getUteAna().getAnagrafe().getCodFiscale();
//			Calendar dataNascita = Calendar.getInstance();
//			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//			format.setLenient(false);
//			Date date = format.parse(currentForm.getUteAna().getAnagrafe().getDataNascita());
//			dataNascita.setTime(date);
//			String cognome = currentForm.getUteAna().getCognome();
//			String nome = currentForm.getUteAna().getNome();
//			char sesso = currentForm.getUteAna().getAnagrafe().getSesso().charAt(0);
//			String luogo = currentForm.getUteAna().getAnagrafe().getLuogoNascita();
//			if (!error) {
//				try {
//					CodiceFiscale codfis = new CodiceFiscale(cognome, nome, dataNascita, luogo, sesso);
//					data = currentForm.getUteAna().getAnagrafe().getCodFiscale();
//					if (ValidazioneDati.isFilled(data)) {
//						if (!data.equals(codfis.getCodiceFiscale())) {
//							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreCodFiscaleErrato"));
//							this.saveErrors(request, errors);
//							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreConfermaCodFisErrato"));
//							this.saveErrors(request, errors);
//							currentForm.setConferma(true);
//							currentForm.setRichiesta("CodFis");
//							error = false;
//						}
//					} else {
//						currentForm.getUteAna().getAnagrafe().setCodFiscale(codfis.getCodiceFiscale());
//					}
//				} catch (Exception e) {
//					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
//							"errors.servizi.erroreCalcoloCodFiscale"));
//					this.saveErrors(request, errors);
//					error = true;
//				}
//			}
//		if (error) {
//			throw new Exception();
//		}

		data = dettaglioUtente.getBibliopolo().getInizioAuto();
		if (ValidazioneDati.isFilled(data)) {
			try {
				codRitorno = ValidazioneDati.validaData(data);
				if (codRitorno != ValidazioneDati.DATA_OK)
					if (codRitorno != -1) {
						codRitorno = ValidazioneDati.DATA_ERRATA;
					}
				throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataFormatoErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataVuotaErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				}
			}
		}
		if (error) {
			throw new Exception();
		}
		data = dettaglioUtente.getBibliopolo().getInizioSosp();
		if (ValidazioneDati.isFilled(data)) {
			try {
				codRitorno = ValidazioneDati.validaData(data);
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataFormatoErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataVuotaErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				}
			}
		}
		if (error) {
			throw new Exception();
		}
		data = dettaglioUtente.getBibliopolo().getFineAuto();
		if (ValidazioneDati.isFilled(data)) {
			try {
				codRitorno = ValidazioneDati.validaData(data);
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataFormatoErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataVuotaErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				}
			}
		}
		if (error) {
			throw new Exception();
		}
		data = dettaglioUtente.getBibliopolo().getFineSosp();
		if (ValidazioneDati.isFilled(data)) {
			try {
				codRitorno = ValidazioneDati.validaData(data);
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new Exception();
			} catch (Exception e) {
				switch (codRitorno) {
				case ValidazioneDati.DATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataFormatoErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_MAGGIORE:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMaggioreErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				case ValidazioneDati.DATA_PASSATA_ERRATA:
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataVuotaErrore"));
					this.saveErrors(request, errors);
					error = true;
					break;
				}
			}
		}

		if (error) {
			throw new Exception();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		Navigation navi = Navigation.getInstance(request);

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		if (request.getAttribute(UTENTI_POLO_CONTROLLATO) != null)
			currentForm.setUtentePoloControllato(true);

		try {
			if (!isTokenValid(request))
				saveToken(request);

			List<?> serviziSel = null;
			currentForm.setConferma(false);
			currentForm.setRichiesta(null);
			currentForm.setProvengoDa((String) request.getAttribute("VengoDa"));
			currentForm.setPathForm((String) request.getAttribute("PathForm"));

			if (request.getAttribute("RicercaUtenti") != null) {
				RicercaUtenteBibliotecaVO ricerca = (RicercaUtenteBibliotecaVO) request.getAttribute("RicercaUtenti");
				currentForm.setUteRic(ricerca);
			}
			UtenteBibliotecaVO selected = (UtenteBibliotecaVO) request.getAttribute("UtenteScelto");
			List<UtenteBibliotecaVO> listaUtenti = (List<UtenteBibliotecaVO>) request.getAttribute("UtentiSel");
			if (ValidazioneDati.isFilled(listaUtenti) ) {
				selected = ValidazioneDati.first(listaUtenti);
				//almaviva5_20111109 gestione bib. affiliate
				RicercaUtenteBibliotecaVO ricerca = currentForm.getUteRic();
				ricerca.setCodBibSer(selected.getCodBibSer());

				currentForm.setSelectedUtenti(listaUtenti);
				currentForm.setNumUtenti(currentForm.getSelectedUtenti().size());
				currentForm.setUteAna(selected);
				if (currentForm.getPosizioneScorrimento() > 0)
					currentForm.setUteAna(currentForm.getSelectedUtenti().get(currentForm.getPosizioneScorrimento()));
			}
			else if (selected != null) {
				currentForm.setUteAna(selected);
				//almaviva5_20111109 gestione bib. affiliate
				RicercaUtenteBibliotecaVO ricerca = currentForm.getUteRic();
				ricerca.setCodBibSer(selected.getCodBibSer());
			}


			UserVO utente = navi.getUtente();
			if (!currentForm.isSessione()) {
				//almaviva5_20111109 gestione bib. affiliate
				RicercaUtenteBibliotecaVO ricerca = currentForm.getUteRic();
				currentForm.setBiblioteca(ricerca != null ? ricerca.getCodBibSer() : utente.getCodBib());

				log.info("DettaglioUtentiAnaAction::unspecified");

				// controllo se sono in nuovo o da selezione
				if ("N".equals(request.getAttribute("Nuovo"))) {
					currentForm.getUteAna().setNuovoUte(UtenteBibliotecaVO.NEW);
					// trl_utente_biblioteca
					currentForm.getUteAna().setCodBibSer(currentForm.getBiblioteca() );
					currentForm.getUteAna().setCodPoloSer(utente.getCodPolo());
					// tbl_utenti
					currentForm.getUteAna().setCodiceBiblioteca(currentForm.getBiblioteca());
					currentForm.getUteAna().setCodPolo(utente.getCodPolo());
					currentForm.getUteAna().setUteIns(utente.getFirmaUtente());
					currentForm.getUteAna().setUteVar(utente.getFirmaUtente());
					currentForm.getUteAna().setNome(ricerca.getNome());
					currentForm.getUteAna().setCognome(ricerca.getCognome());
					currentForm.getUteAna().getAnagrafe().setCodFiscale(ricerca.getCodFiscale());
					currentForm.getUteAna().getAnagrafe().setDataNascita(ricerca.getDataNascita());
					currentForm.getUteAna().getAnagrafe().setPostaElettronica(ricerca.getEmail());
					currentForm.getUteAna().getAnagrafe().setNazionalita(ricerca.getNazCitta());
					currentForm.getUteAna().getAnagrafe().getResidenza().setProvincia(ricerca.getProvResidenza());
					this.loadDefaultStandard(request, mapping, form);
				} else {
					currentForm.getUteAna().setNuovoUte(UtenteBibliotecaVO.OLD);
					if (!ValidazioneDati.isFilled(currentForm.getTipoUtente()) ) {
						if (ValidazioneDati.isFilled(currentForm.getUteAna().getAnagrafe().getSesso())) {
							currentForm.setTipoUtente("P");
							currentForm.getUteAna().getProfessione().setPersonaGiuridica('N');
						} else {
							currentForm.setTipoUtente("E");
							currentForm.getUteAna().getProfessione().setPersonaGiuridica('S');
						}
					}
				}
				currentForm.setSessione(true);
			}

			// controllo che non sia stata attivata la lista supporto delle biblioteche

			BibliotecaVO bib = (BibliotecaVO) request.getAttribute("biblioteca");
			if (bib != null && bib.getIdBiblioteca() > 0) {
				// l'importazione della biblioteca è consentita solo da inserimento per cui si è già in utente nuovo
				currentForm.getUteAna().setCodiceBiblioteca(bib.getCod_bib());
				currentForm.getUteAna().setCodPolo(utente.getCodPolo());
				currentForm.getUteAna().setUteIns(utente.getFirmaUtente());
				currentForm.getUteAna().setUteVar(utente.getFirmaUtente());
				currentForm.setTipoUtente("E");
				currentForm.getUteAna().getProfessione().setPersonaGiuridica('S');
				currentForm.getUteAna().getProfessione().setTipoPersona("5");

				currentForm.getUteAna().setCodBibSer(currentForm.getBiblioteca() );
				currentForm.getUteAna().setCodPoloSer(utente.getCodPolo());
				currentForm.getUteAna().getAnagrafe().setCodFiscale("");
				//almaviva5_20110228 #4207
				//almaviva5_20110428 #4401
				if (ValidazioneDati.isFilled(bib.getCd_ana_biblioteca()))
					currentForm.getUteAna().getBibliopolo().setCodiceAnagrafe(bib.getCd_ana_biblioteca());

				if (ValidazioneDati.isFilled(bib.getCod_fiscale()) )
					currentForm.getUteAna().getAnagrafe().setCodFiscale(bib.getCod_fiscale());
				else
					if (ValidazioneDati.isFilled(bib.getP_iva()) )
						currentForm.getUteAna().getAnagrafe().setCodFiscale(bib.getP_iva());

				currentForm.getUteAna().setCognome("");
				if (bib.getNom_biblioteca() != null)
					currentForm.getUteAna().setCognome(bib.getNom_biblioteca());

				currentForm.getUteAna().getAnagrafe().getResidenza().setProvincia("");
				if (bib.getProvincia() != null)
					currentForm.getUteAna().getAnagrafe().getResidenza().setProvincia(bib.getProvincia());

				currentForm.getUteAna().getAnagrafe().getResidenza().setCap("");
				if (bib.getCap()!=null)
				{
					currentForm.getUteAna().getAnagrafe().getResidenza().setCap(bib.getCap());
				}
				currentForm.getUteAna().getAnagrafe().getResidenza().setVia("");
				if (bib.getIndirizzo()!=null)
				{
					currentForm.getUteAna().getAnagrafe().getResidenza().setVia(bib.getIndirizzo());
				}
				currentForm.getUteAna().getAnagrafe().getResidenza().setCitta("");
				if (bib.getLocalita()!=null)
				{
					currentForm.getUteAna().getAnagrafe().getResidenza().setCitta(bib.getLocalita());
				}
				currentForm.getUteAna().getAnagrafe().setNazionalita("");
				if (bib.getPaese()!=null)
				{
					currentForm.getUteAna().getAnagrafe().getResidenza().setNazionalita(bib.getPaese());
				}
				currentForm.getUteAna().getAnagrafe().setPostaElettronica("");
				if (bib.getE_mail()!=null)
				{
					currentForm.getUteAna().getAnagrafe().setPostaElettronica(bib.getE_mail());
				}
				currentForm.getUteAna().getProfessione().setTipoPersona("5");

				currentForm.getUteAna().getAnagrafe().getResidenza().setTelefono("");
				if (bib.getTelefono()!=null)
				{
					currentForm.getUteAna().getAnagrafe().getResidenza().setTelefono(bib.getTelefono());
				}
				currentForm.getUteAna().getAnagrafe().getResidenza().setFax("");
				if (bib.getFax()!=null)
				{
					currentForm.getUteAna().getAnagrafe().getResidenza().setFax(bib.getFax());
				}
				// impostazione del polo e della biblioteca
				currentForm.getUteAna().getBibliopolo().setCodBibXUteBib(bib.getCod_bib());
				currentForm.getUteAna().getBibliopolo().setCodPoloXUteBib(bib.getCod_polo());
			}


			if ("ListaServizi".equals(currentForm.getProvengoDa())) {
				currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_AUTORIZZAZIONI);
				serviziSel = (List<?>) request.getAttribute("ServSelezionati");
				if (serviziSel != null && serviziSel.size() != 0) {
					currentForm.getUteAna().getAutorizzazioni().setServizi(this.loadServiziUtente((List<ServizioVO>) serviziSel));
				}
				if (currentForm.getUteAna()!=null && currentForm.getUteAna().getAutorizzazioni()!=null &&  currentForm.getUteAna().getAutorizzazioni().getListaServizi()!= null && currentForm.getUteAna().getAutorizzazioni().getListaServizi().size() > 0)
				{
					Date now = new Date(System.currentTimeMillis());
					for (int index = 0; index <currentForm.getUteAna().getAutorizzazioni().getListaServizi().size(); index++) {
						ServizioVO ele=currentForm.getUteAna().getAutorizzazioni().getListaServizi().get(index);
						ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getCodice()));
						// solo per i servizi inseriti impongo data inizio aut= data prima iscri e data fine aut=dta fine aut gen
						if (ele.getStato()==1 && currentForm.getUteAna()!=null && currentForm.getUteAna().getBibliopolo()!=null &&  currentForm.getUteAna().getBibliopolo().getFineAuto()!=null && currentForm.getUteAna().getBibliopolo().getFineAuto().trim().length()>0)
						{
							ele.setDataInizio(dateToString(now));
							ele.setDataFine(currentForm.getUteAna().getBibliopolo().getFineAuto());
						}
					}
				}
				return mapping.getInputForward();
			}
			if ("MaterieInteresse".equals(currentForm.getProvengoDa())) {
				if (request.getAttribute("UtenteScelto")!=null) {
					// Vengo da scelta materie interesse
					currentForm.setUteAna(selected);
				}
				return mapping.getInputForward();
			}

			if ("amministrazione_biblioteche".equals(currentForm.getProvengoDa())) {
				//c'é una biblioteca da importare come utente
				BibliotecaVO bibVO = (BibliotecaVO)request.getAttribute("BibliotecaVO");

				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				BaseVO infoUtente = new BaseVO();
				infoUtente.setFlCanc("N");
				infoUtente.setTsIns(DaoManager.now());
				infoUtente.setTsVar(DaoManager.now());
				infoUtente.setUteIns(utente.getFirmaUtente());
				infoUtente.setUteVar(utente.getFirmaUtente());

				ActionMessages errors = new ActionMessages();

				DescrittoreBloccoVO blocco = delegate.verificaEsistenzaUtentePolo(bibVO,
					utente.getCodPolo(), currentForm.getBiblioteca(), infoUtente, navi.getUserTicket());
				String stato = this.getStatoUtentePolo(blocco, mapping, request, currentForm.getBiblioteca(), errors);
				if (stato.equals("A"))
					return navi.goForward(mapping.findForward("listaUtentiPolo"));

				if (stato.equals("T") || stato.equals("E")) {
					resetToken(request);
					request.setAttribute("VengoDa", "");
					request.setAttribute("RicercaUtenti", currentForm.getUteAna());
					return mapping.findForward("annulla");
				}

				UtenteBibliotecaVO utenteVO = delegate.importaBibliotecaComeUtente(utente.getCodPolo(), currentForm.getBiblioteca(), bibVO, infoUtente, this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/);
				filtraDatiUtente(utenteVO);

				currentForm.setUteAna(utenteVO);

				if (!currentForm.getUteAna().isNuovoUte()) {
					//la biblioteca è già stata importata nella tabella utenti
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.bibliotecaGiaImportata"));
					this.saveErrors(request, errors);
				} else {
					currentForm.getUteAna().setNuovoUte(UtenteBibliotecaVO.OLD);
					//la biblioteca è stata importata corettamente
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.bibliotecaImportata"));
					this.saveErrors(request, errors);
				}
			}

			this.loadCombo(form);
			this.loadProfessioni(request,currentForm);
			this.loadDocumenti(currentForm);
//			this.loadMaterie(request, currentForm);
//			this.setMaterieUtente(currentForm);

			// indico che ho caricato il forder Anagrafe
			currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_ANAGRAFICA);
			currentForm.setEnable(true);
			if (currentForm.getUteAna() == null) {
				currentForm.setUteAna(new UtenteBibliotecaVO());
			}

			// testo se sono in modifica vecchio utente
			if (!currentForm.getUteAna().isNuovoUte()) {
				currentForm.setEnable(false);
			}

			// clono l'old utente appena caricato per constatare eventuali
			// modifiche
			if (currentForm.getUteAnaOLD() == null) {
				currentForm.setUteAnaOLD((UtenteBibliotecaVO)currentForm.getUteAna().clone());
			}
    		if (request.getParameter("tagNote")!=null )
    	   	{
				currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_AUTORIZZAZIONI);
    			if (currentForm.getClicNotaPrg()!=null && currentForm.getClicNotaPrg().equals(Integer.valueOf(request.getParameter("tagNote"))))
        	   	{
        			currentForm.setClicNotaPrg(-1);
        	   	}
	    		else
	    	   	{
	    			currentForm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
	    	   	}
    	   	}


			if (currentForm.getUteAna()!=null && currentForm.getUteAna().getAutorizzazioni()!=null )
			{
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				currentForm.setLstServiziAutor(factory.getGestioneServizi().getListaServiziAutorizzazione(navi.getUserTicket(), currentForm.getUteAna()));
				RicercaAutorizzazioneVO ricAut=new RicercaAutorizzazioneVO();
				if (currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor()!=null && currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor().trim().length()>0)
				{
					ricAut.setCodice(currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor());
					ricAut.setCodBib(currentForm.getUteAna().getCodBibSer());
					ricAut.setCodPolo(currentForm.getUteAna().getCodPoloSer());
					DescrittoreBloccoVO bloccoAut=factory.getGestioneServizi().getListaAutorizzazioni(navi.getUserTicket(),ricAut );
					if (bloccoAut!=null && bloccoAut.getLista()!=null && bloccoAut.getLista().size()==1)
					{
						AutorizzazioneVO eleBlocco=new AutorizzazioneVO();
						eleBlocco= (AutorizzazioneVO) bloccoAut.getLista().get(0);
						currentForm.getUteAna().getAutorizzazioni().setCodTipoAutor(eleBlocco.getCodAutorizzazione());
						currentForm.getUteAna().getAutorizzazioni().setAutorizzazione(eleBlocco.getDesAutorizzazione());
						currentForm.getUteAna().getAutorizzazioni().setIdAutor(eleBlocco.getIdAutorizzazione());
						currentForm.getUteAna().getAutorizzazioni().setCodBibAutor(eleBlocco.getCodBiblioteca());
						currentForm.getUteAna().getAutorizzazioni().setCodPoloAutor(eleBlocco.getCodPolo());
						// reimposto l'old ma solo per le autorizzazioni
						currentForm.setUteAnaOLD((UtenteBibliotecaVO)currentForm.getUteAna().clone());

					}
				}
				if (currentForm.getUteAna().getAutorizzazioni().getListaServizi()!= null && currentForm.getUteAna().getAutorizzazioni().getListaServizi().size() > 0)
				{
					for (int index = 0; index <currentForm.getUteAna().getAutorizzazioni().getListaServizi().size(); index++) {
						ServizioVO ele=currentForm.getUteAna().getAutorizzazioni().getListaServizi().get(index);
						ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getCodice()));
						//ele.setAutorizzazione(String.valueOf(currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor()));
						// reimposto l'old ma solo per le autorizzazioni (NON NECESSARIO) 03/09/2010
						//currentForm.setUteAnaOLD((UtenteBibliotecaVO)currentForm.getUteAna().clone());
					}
				}
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);
			// scorro i servizi utente tolgo gli eliminati e ripulisco cancella
			// nei record selezionati per cui non è stata richiesta la cancellazione
			UtenteBibliotecaVO utente = currentForm.getUteAna();
			Date now = new Date();

			//almaviva5_20101206 aggiorna chiave utente
			String nome = utente.getCognomeNome();
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi("", nome);
			utente.setChiaveUte(key.getKy_cles1_A());

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// controllo duplicazione codice fiscale // tolto dettaglioUtente.isNuovoUte()
			if (utente != null && utente.getAnagrafe() != null
					&& ValidazioneDati.isFilled(utente.getAnagrafe().getCodFiscale()) ) {
				String codfiscale = utente.getAnagrafe().getCodFiscale();
				String idAna = utente.getIdUtente();
				boolean duplicato = false;
				duplicato = factory.getGestioneServizi().checkEsistenzaUtente(codfiscale, null, null, idAna);
				if (duplicato) {
					LinkableTagUtils.addError(request, new ActionMessage ("errors.servizi.utenti.codFiscEsistente" ));
					return mapping.getInputForward();
				}
			}
			// controllo duplicazione email // tolto dettaglioUtente.isNuovoUte()
			if (utente != null
					&& utente.getAnagrafe() != null) {
				String idUtente = utente.getIdUtente();
				AnagrafeVO anag = utente.getAnagrafe();
				//almaviva5_20150430
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				List<UtenteBaseVO> utentiMail2 = delegate
						.checkMailUtenteBiblioteca(idUtente, anag.getPostaElettronica(), anag.getPostaElettronica2());

				//lista di utenti che hanno la mail principale di questo utente impostata come mail secondaria
				//verrà notificato al bibliotecario l'aggiornamento in cascata.
				if (ValidazioneDati.isFilled(utentiMail2)) {
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utente.modifica.mail.primaria.update.cascade", anag.getPostaElettronica()));
				}

				/*
					&& ValidazioneDati.isFilled(utente.getAnagrafe().getPostaElettronica()) ) {
				String mail = utente.getAnagrafe().getPostaElettronica().trim().toUpperCase();
				String idAna = utente.getIdUtente();
				boolean duplicato = false;
				duplicato = factory.getGestioneServizi().checkEsistenzaUtente(null, mail, null, idAna);
				if (duplicato) {
					LinkableTagUtils.addError(request, new ActionMessage ("errors.servizi.utenti.emailEsistente" ));
					return mapping.getInputForward();
				*/
			}

			//almaviva5_20110428 #4400
			ProfessioniVO professione = utente.getProfessione();
			String ateneo = professione.getAteneo();
			String matricola = professione.getMatricola();
			if (ValidazioneDati.isFilled(ateneo) && ValidazioneDati.isFilled(matricola) )  {
				String idAna = utente.getIdUtente();
				boolean duplicato = false;
				duplicato = factory.getGestioneServizi().checkEsistenzaUtente(null, null, new String[]{ateneo, matricola}, idAna);
				if (duplicato) {
					LinkableTagUtils.addError(request, new ActionMessage ("errors.servizi.utenti.ateneoMatEsistente" ));
					return mapping.getInputForward();
				}
			}

			try {
				this.checkDatiAnagrafici(request, form);
				//almaviva5_20110324 #4319
				if (checkAttivita(request, currentForm, NavigazioneServizi.DIRITTI))
					checkDiritti(request, currentForm, now);
			} catch (Exception e) {
				// recupero gli errori della validazione
				log.error("", e);
				return mapping.getInputForward();
			}

			if (currentForm.getUteAnaOLD().equals(utente)) {
				// escludo il caso di importa & reset password
				if (!utente.isImportato() && currentForm.getRichiesta() != RichiestaType.RESET_PASSWORD) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
					resetToken(request);
					return mapping.getInputForward();
				}
			}

			// incongruenza professione-occupazione
			if (utente != null
					&& utente.getProfessione() != null
					&& ValidazioneDati.isFilled(utente.getProfessione().getProfessione())
					&& ValidazioneDati.isFilled(utente.getProfessione().getIdOccupazione())
					&& !utente.getProfessione().getIdOccupazione().equals("0")) {
				for (int index = 0; index <currentForm.getOccupazioni().size(); index++) {
					OccupazioneVO occ = (OccupazioneVO) currentForm.getOccupazioni().get(index);
					if (utente.getProfessione().getIdOccupazione().equals(String.valueOf(occ.getIdOccupazioni()))) {
						if (!utente.getProfessione().getProfessione().equals(occ.getProfessione())) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.incongruenzaOccupazioneProfessione"));
							return mapping.getInputForward();
						}
					}
				}
			}

			// incongruenza titolo-spec titolo
			if (utente!=null && utente.getProfessione()!=null &&  ValidazioneDati.isFilled(utente.getProfessione().getTitoloStudio()) &&  ValidazioneDati.isFilled(utente.getProfessione().getIdSpecTitoloStudio()) && !utente.getProfessione().getIdSpecTitoloStudio().equals("0") ) {
				for (int index = 0; index <currentForm.getSpecTitoloStudio().size(); index++) {
					SpecTitoloStudioVO ele= (SpecTitoloStudioVO) currentForm.getSpecTitoloStudio().get(index);
					if (utente.getProfessione().getIdSpecTitoloStudio().equals(String.valueOf(ele.getIdTitoloStudio())))
					{
						if (!utente.getProfessione().getTitoloStudio().equals(ele.getTitoloStudio()))
						{
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.incongruenzaTitoloSpecTit"));
							return mapping.getInputForward();
						}
						//break;
					}
				}
			}

			// se occupazione impostata e professione vuota
			if (utente!=null && utente.getProfessione()!=null &&  ValidazioneDati.strIsNull(utente.getProfessione().getProfessione()) &&  ValidazioneDati.isFilled(utente.getProfessione().getIdOccupazione())) {
				for (int index = 0; index <currentForm.getOccupazioni().size(); index++) {
					OccupazioneVO ele= (OccupazioneVO) currentForm.getOccupazioni().get(index);
					if (utente.getProfessione().getIdOccupazione().equals(String.valueOf(ele.getIdOccupazioni()))) {
						utente.getProfessione().setProfessione(ele.getProfessione());
						break;
					}
				}
			}

			// se spec titolo  impostata e titolo di studio vuoto
			if (utente!=null && utente.getProfessione()!=null &&  ValidazioneDati.strIsNull(utente.getProfessione().getTitoloStudio()) &&  ValidazioneDati.isFilled(utente.getProfessione().getIdSpecTitoloStudio())) {
				for (int index = 0; index <currentForm.getSpecTitoloStudio().size(); index++) {
					SpecTitoloStudioVO ele= (SpecTitoloStudioVO) currentForm.getSpecTitoloStudio().get(index);
					if (utente.getProfessione().getIdSpecTitoloStudio().equals(String.valueOf(ele.getIdTitoloStudio())))
					{
						utente.getProfessione().setTitoloStudio(ele.getTitoloStudio());
						break;
					}
				}
			}

			//almaviva5_20110427 #4398
			utente.setFlCanc("N");
			utente.setFlCancAna("N");

			currentForm.setDettaglioConferma((UtenteBibliotecaVO)utente.clone() );

			if (currentForm.getRichiesta() == RichiestaType.CODICE_FISCALE ) {
				currentForm.setRichiesta(RichiestaType.AGGIORNA);
				currentForm.setConferma(true);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();
			}

			if (currentForm.getRichiesta() == RichiestaType.RESET_PASSWORD ) {
				//imposto la password di default
				String codFiscale = utente.getAnagrafe().getCodFiscale();
				PasswordEncrypter crypt = new PasswordEncrypter(codFiscale);
				String password = crypt.encrypt(codFiscale);
				utente.setPassword(password);
				utente.setChangePassword("S");
				utente.setLastAccess(DaoManager.now());

			}

			// CONTROLLO SE UTENTE GIA' DELOCALIZZATO
			currentForm.setRichiesta(RichiestaType.AGGIORNA);
			currentForm.setConferma(true);

			if (currentForm.isFlagDomUgualeRes()) {
				//informo che i dati di domicilio saranno uguali a quelli di residenza
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.domicilioUgualeResidenza"));
				currentForm.setFlagDomUgualeRes(false);
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error("", e);
			return mapping.getInputForward();
		}

	}

	private void checkDiritti(HttpServletRequest request,
			DettaglioUtentiAnaForm currentForm, Date now) throws Exception {
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		AutorizzazioniVO autorizzazioni = utente.getAutorizzazioni();
		List<ServizioVO> servizi = autorizzazioni.getServizi();
		if (ValidazioneDati.isFilled(servizi))
			this.resetCampoCancella(servizi);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		// adeguamento descr autorizzazione al codice prescelto
		RicercaAutorizzazioneVO ricAut = new RicercaAutorizzazioneVO();
		Navigation navi = Navigation.getInstance(request);
		String codTipoAut = autorizzazioni.getCodTipoAutor();
		if (utente != null && autorizzazioni != null && ValidazioneDati.isFilled(codTipoAut)) {
			ricAut.setCodice(codTipoAut);
			ricAut.setCodBib(utente.getCodBibSer());
			ricAut.setCodPolo(utente.getCodPoloSer());
			DescrittoreBloccoVO bloccoAut = factory.getGestioneServizi().getListaAutorizzazioni(navi.getUserTicket(), ricAut);
			if (bloccoAut != null && bloccoAut.getLista() != null && bloccoAut.getLista().size() == 1) {
				AutorizzazioneVO eleBlocco = new AutorizzazioneVO();
				eleBlocco = (AutorizzazioneVO) bloccoAut.getLista().get(0);
				autorizzazioni.setCodTipoAutor(eleBlocco.getCodAutorizzazione());
				autorizzazioni.setAutorizzazione(eleBlocco.getDesAutorizzazione());
				autorizzazioni.setIdAutor(eleBlocco.getIdAutorizzazione());
				autorizzazioni.setCodBibAutor(eleBlocco.getCodBiblioteca());
				autorizzazioni.setCodPoloAutor(eleBlocco.getCodPolo());
			}
		}

		// gestione automatismo autorizzazione "automatico per"
		BiblioPoloVO bp = utente.getBibliopolo();
		if (utente != null
				&& utente.getProfessione() != null
				&& utente.getProfessione().getIdOccupazione() != null
				&& utente.getProfessione().getIdOccupazione().trim().length() > 0
				&& new Integer(utente.getProfessione().getIdOccupazione()).intValue() > 0) {
			if (autorizzazioni == null
					|| (!ValidazioneDati.isFilled(autorizzazioni.getAutorizzazione()) )
					|| (autorizzazioni.getListaServizi() == null || !ValidazioneDati.isFilled(autorizzazioni.getListaServizi())) ) {

				AutorizzazioniVO autorizzazioneVO = factory.getGestioneServizi().getAutorizzazioneByProfessione(
								navi.getUserTicket(),
								utente.getCodPoloSer(),
								utente.getCodBibSer(),
								new Integer(utente.getProfessione().getIdOccupazione()).intValue());
				if (autorizzazioneVO != null
						&& ValidazioneDati.isFilled(autorizzazioneVO.getAutorizzazione()) )
					utente.setAutorizzazioni(autorizzazioneVO);

				if (ValidazioneDati.isFilled(autorizzazioni.getListaServizi())) {
					for (int index = 0; index < autorizzazioni.getListaServizi().size(); index++) {
						ServizioVO srv = autorizzazioni.getListaServizi().get(index);
						srv.setDescrizioneTipoServizio(this.cercaDescrServizio(srv.getCodice()));
						srv.setDataInizio(ValidazioneDati.coalesce(bp.getInizioAuto(), dateToString(now)) );
						srv.setDataFine(bp.getFineAuto());
						srv.setSospDataInizio(bp.getInizioSosp());
						srv.setSospDataFine(bp.getFineSosp());
					}
					currentForm.setLstServiziAutor(factory.getGestioneServizi().getListaServiziAutorizzazione(navi.getUserTicket(), utente));
					List<ServizioVO> listaServiziAut = currentForm.getLstServiziAutor();
					// elimina tutti i vecchi servizi dall'array
					// inserisce i servizi della nuuova autor. settando lo stato
					// giusto
					if (ValidazioneDati.isFilled(listaServiziAut) )
						inserisciServiziAutorizzazione(listaServiziAut,	autorizzazioni.getServizi(), codTipoAut);

				}
			}
		}

		// tck3214
		// se ci sono le date dell'autorizzazione e ci sono servizi privi
		// dell'indicazione occorre dare segnalazione del riempimento automatico
		// boolean
		// adeguato=this.aggiornaListaServizi(dettaglioUtente.getAutorizzazioni().getListaServizi(),currentForm);
		boolean adeguato = this.aggiornaListaServizi(autorizzazioni.getListaServizi(), currentForm, request);
		// se ci sono le date dell'autorizzazione e ci sono servizi privi dell'indicazione occorre dare segnalazione del riempimento automatico
		if (adeguato)
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.adeguamentoDateServizi"));

		// controlli su date autorizzazione
		boolean ret = true;

		ret = this.verificaPerAggiornaDatiLista(request, bp
				.getInizioAuto(), bp.getFineAuto(), bp.getInizioSosp(), bp
				.getFineSosp(), "AUT");
		if (!ret)
			throw new ValidationException("diritti");

		// controlli su date servizi
		boolean ritorno = true;
		ritorno = this.controllaDateServizi(request, autorizzazioni.getListaServizi(), bp);
		if (!ritorno)
			throw new ValidationException("diritti");

		// obbligatorietà della data di inizio autorizzazione in caso di
		// esistenza di un codice autorizzazione
		if (ValidazioneDati.strIsNull(bp.getInizioAuto())
				&& autorizzazioni != null && codTipoAut != null
				&& codTipoAut.trim().length() > 0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.dataGenInizioAutAssentePerAut"));
			throw new ValidationException("diritti");
		}

		// se data inizio autorizzazione non è stata impostata si attribuisce la
		// data di prima iscrizione
		if (utente.isNuovoUte()
				|| (currentForm.getUteAnaOLD() != null
						&& currentForm.getUteAnaOLD().getBibliopolo() != null
						&& ValidazioneDati.strIsNull(currentForm.getUteAnaOLD().getBibliopolo().getInizioAuto()))) {
			if (utente != null
					&& bp != null
					&& ValidazioneDati.strIsNull(bp.getInizioAuto())) {

				bp.setInizioAuto(dateToString(now));
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.dataGenAutAssente"));
				throw new ValidationException("diritti");

			} else {
				String Dai = bp.getInizioAuto();
				String dataReg = bp.getPoloDataRegistrazione();
				String Dis = ValidazioneDati.isFilled(dataReg) ? dataReg : dateToString(now);
				if (ValidazioneDati.isFilled(Dai)
						&& ValidazioneDati.isFilled(Dis)) {
					int test = ValidazioneDati.confrontaLeDateBest(Dai, Dis);
					if (test == ValidazioneDati.DATA1_LT_DATA2) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.dataGenInizioAutMinoreDataIscr"));
						throw new ValidationException("diritti");
					}
				}
			}
		}

		// incongruenza autorizzazione-diritti di lista
		if (autorizzazioni!=null && ValidazioneDati.isFilled(codTipoAut) ) {
			if (ValidazioneDati.isFilled(autorizzazioni.getListaServizi())) {
				for (ServizioVO srv : autorizzazioni.getListaServizi()) {
					//almaviva5_20110211 #4184 escludo cancellati
					if (ValidazioneDati.in(srv.getStato(), ServizioVO.ELI,
							ServizioVO.DELELI, ServizioVO.DELMOD,
							ServizioVO.DELDELMOD, ServizioVO.DELOLD,
							ServizioVO.DELDELOLD))
						continue;
					String fae = srv.getFlag_aut_ereditato();
					if (ValidazioneDati.isFilled(fae) && !fae.trim().toUpperCase().equals(codTipoAut.trim().toUpperCase())) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.incongruenzaAutServiziAut"));
						throw new ValidationException("diritti");
					}
					// break;
				}
			}
		}

	}

	private DescrittoreBloccoVO verificaEsistenzaUtentePolo(
			HttpServletRequest request, ActionForm form) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		DescrittoreBloccoVO blocco1 = delegate.verificaEsistenzaUtentePolo(
				currentForm.getUteAna(), Navigation
						.getInstance(request).getUserTicket());

		return blocco1;
	}

	public boolean controllaDateServizi(HttpServletRequest request,
			List<ServizioVO> servizi, BiblioPoloVO infBibl) throws Exception {
		boolean ret = true;
		for (int index = 0; index < servizi.size(); index++) {
			ServizioVO ser = servizi.get(index);
			ret = this.verificaPerAggiornaDatiLista(request, ser
					.getDataInizio(), ser.getDataFine(), ser
					.getSospDataInizio(), ser.getSospDataFine(), "DIR");
			if (!ret) {
				break;
			}
			// controlli integrati su date autorizzazioni e servizi
			ret = this.verificheUterioriSuDate(request, ser
					.getDataInizio(), ser.getDataFine(), ser
					.getSospDataInizio(), ser.getSospDataFine(), infBibl );
			if (!ret) {
				break;
			}

		}
		return ret;
	}

	private static final String dateToString(java.util.Date data) {
		if (data == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String value = format.format(data);

		return value;
	}


	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);
			this.checkFormPerCancellazione(request, form);
			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaType.CANCELLA);
			currentForm.setDettaglioConferma((UtenteBibliotecaVO)currentForm.getUteAna().clone() );
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			currentForm.setConferma(false);
			boolean ret = true;
			Navigation navi = Navigation.getInstance(request);
			RichiestaType richiesta = currentForm.getRichiesta();
			UtenteBibliotecaVO dettaglioUtente = currentForm.getUteAna();

			switch (richiesta) {
			case CANCELLA:
				this.checkFormPerCancellazione(request, form);
				ret = this.cancellaDb(dettaglioUtente,Navigation.getInstance(request).getUserTicket());
				if (!ret) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceErroreCancellazione"));
					currentForm.setRichiesta(null);
					currentForm.setConferma(false);
					currentForm.setDettaglioConferma(null);
					return mapping.getInputForward();
				} else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
					this.resetToken(request);
					dettaglioUtente.clearUtenteBiblioteca();
					return mapping.findForward("cancella");
				}

			case AGGIORNA:
				String trovato = "";
				if (dettaglioUtente.isNuovoUte() && !currentForm.isUtentePoloControllato()) {

					trovato = controllaUtentePolo(mapping, form, request, currentForm, LinkableTagUtils.getErrors(request));
					if (trovato.equals("A"))
						return navi.goForward(mapping.findForward("listaUtentiPolo"));

					if (trovato.equals("T") || trovato.equals("E")) {
						resetToken(request);
						request.setAttribute("VengoDa", "");
						request.setAttribute("RicercaUtenti", dettaglioUtente);
						return mapping.findForward("annulla");
					}
				}

				// (trovato.equals("N")) non trovato
/*				if (dettaglioUtente.getTipoIscrizione().equals("A"))
				{
					// è stata data conferma di importazione di un utente di un'altra biblioteca occorre controllare se esiste una delocalizzazione
					FactoryEJBDelegate factory = (FactoryEJBDelegate) this.servlet.getServletContext().getAttribute(FactoryEJBPlugin.FACTORY_EJBXxxx);
					UtenteBibliotecaVO dettaglioUtenteBis = currentForm.getUteAna();
					dettaglioUtenteBis.setFlCanc("S");
					DescrittoreBloccoVO blocco1=factory.getGestioneServizi().verificaEsistenzaUtentePolo(Navigation.getInstance(request).getUserTicket(),dettaglioUtente);
					if (blocco1!=null && blocco1.getTotRighe()>0)
					{
						boolean delocalizza=true;
					}
				}
*/

				ret = this.aggiornaDb(dettaglioUtente, request);
				if (currentForm.getUteAnaOLD() == null) {
					currentForm.setUteAnaOLD((UtenteBibliotecaVO)currentForm.getUteAna().clone());
				}

				if (!ret) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
					currentForm.setRichiesta(null);
					currentForm.setConferma(false);
					currentForm.setDettaglioConferma(null);
					return mapping.getInputForward();
				} else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
					// resetto campi
					this.resetToken(request);
					currentForm.setRichiesta(null);
					currentForm.setConferma(false);
					dettaglioUtente.setNuovoUte(false);
					currentForm.setUteAnaOLD((UtenteBibliotecaVO)dettaglioUtente.clone());
					if (currentForm.getUteAnaOLD().getAutorizzazioni().getServizi() != null	) // && currentForm.getUteAnaOLD().getAutorizzazioni().getServizi().size() > 0
					{
						currentForm.getUteAnaOLD().getAutorizzazioni().setServizi(this.resetListaDiritti(currentForm.getUteAnaOLD().getAutorizzazioni().getServizi()));
						currentForm.getUteAna().getAutorizzazioni().setServizi(this.resetListaDiritti(currentForm.getUteAna().getAutorizzazioni().getServizi()));
					}
					currentForm.setDettaglioConferma(null);

					return mapping.getInputForward();
				}

			case RESET_PASSWORD:
				//imposto la password di default
				PasswordEncrypter crypt = new PasswordEncrypter(dettaglioUtente.getCodiceUtente());
				String password = crypt.encrypt(dettaglioUtente.getAnagrafe().getCodFiscale());
				dettaglioUtente.setPassword(password);
				currentForm.setRichiesta(RichiestaType.AGGIORNA);
				//ripeto l'aggiornamento
				return si(mapping, currentForm, request, response);

			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


	public ActionForward materie(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Navigation navi = Navigation.getInstance(request);
		try {
			if (navi.isFromBar())
				return mapping.getInputForward();

			DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			request.setAttribute("UtenteScelto", currentForm.getUteAna());

			//almaviva5_20120216 gestione bib. affiliate
			request.setAttribute(BIBLIOTECA_ATTR, currentForm.getBiblioteca() );

			return mapping.findForward("materie");

		} catch (Exception e) {
			log.error("", e);
			return navi.goBack(true);
		}
	}

	public ActionForward resetPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		currentForm.setRichiesta(RichiestaType.RESET_PASSWORD);
		return ok(mapping, currentForm, request, response);

	}


	private String getStatoUtentePolo(DescrittoreBloccoVO blocco1, ActionMapping mapping,
									HttpServletRequest request, String codBibSer,
									ActionMessages errors)
	{
		String var = "N";
		if (blocco1.getTotRighe() > 0) {
			List<SinteticaUtenteVO> utentiPolo = blocco1.getLista();
			// E=erogante A=altra biblioteca T=tutte
			for (int index = 0; index < utentiPolo.size(); index++) {
				SinteticaUtenteVO utePolo = utentiPolo.get(index);
				if (utePolo.getBiblioDiUteBiblio().equals(codBibSer)) {
					if (var != null && var.equals("A")) {
						var = "T";
					} else {
						var = "E";
					}
					// l'utente risulta già iscritto in questa biblioteca. Si
					// vuole aggiornare i dati ?
				} else {
					if (var != null && var.equals("E")) {
						var = "T";
					} else {
						var = "A";
					}
					// "utente già isceritto presso altra biblioteca del polo .
					// Si per importarne i dati anagrafici no per annullare
					// l'operazione"?
				}
			}
			if (var.equals("E")) {
				// iscritto solo a bib Erogante
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoInTutte"));
			}
			if (var.equals("T")) {
				// iscritto a bib Erogante ed ad Altre = Tutte
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoErogante"));
			}
			if (var.equals("A")) {
				// iscritto solo ad Altre bib
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreUtenteGiaIscrittoAltraBiblio"));
			}
			this.saveErrors(request, errors);
			request.setAttribute(ServiziDelegate.LISTA_UTENTI, blocco1);
			request.setAttribute("PathForm", mapping.getPath());
		}
		return var;
	}


	private String controllaUtentePolo(ActionMapping mapping, ActionForm form,
										HttpServletRequest request,
										DettaglioUtentiAnaForm currentForm,
										ActionMessages errors)
	throws Exception {
		DescrittoreBloccoVO blocco1 = this.verificaEsistenzaUtentePolo(request,	form);

		return this.getStatoUtentePolo(blocco1, mapping, request, currentForm.getUteAna().getCodBibSer(), errors);
	}


	public boolean aggiornaDb(UtenteBibliotecaVO utente, HttpServletRequest request) throws Exception
	{
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		// vedo : se è nuovo inserire autorizzazione
		// se è vecchio update di autorizzazione
		boolean ret = true;

		utente.setFlCanc("N");
		utente.setUteVar(Navigation.getInstance(request).getUtente().getFirmaUtente());
		utente.setTsVar(DaoManager.now());
		try {
			if (utente.isNuovoUte()) {
				utente.setUteIns(Navigation.getInstance(request).getUtente().getFirmaUtente());
				utente.setTsIns(DaoManager.now());
				utente.setPassword(UtenteBibliotecaVO.DEFAULT_PASSWORD);
				ret = factory.getGestioneServizi().insertUtente(Navigation.getInstance(request).getUserTicket(), utente);
			} else {
				if (utente.getIdUtenteBiblioteca() != null)
				{
					ret = factory.getGestioneServizi().updateUtente(Navigation.getInstance(request).getUserTicket(), utente);
				}
				else
				{
					ret = factory.getGestioneServizi().importaUtente(Navigation.getInstance(request).getUserTicket(), utente);
					//tck 3880
					if (ret)
					{
						utente.setImportato(false);
					}
				}
			}
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			ret=false;
		} catch (RemoteException e) {
			log.error("", e);
			ret=false;
		}

		return ret;
	}

	public int verificaMovimenti(String idUte, String ticket)
			throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		int numRec = 0;
		numRec = factory.getGestioneServizi().verificaMovimentiUtente(ticket,idUte);
		return numRec;
	}

	public boolean cancellaDb(UtenteBibliotecaVO utente, String ticket) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		boolean ret = factory.getGestioneServizi().cancelUtenteBiblioteca(ticket, utente);
		return ret;
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		currentForm = (DettaglioUtentiAnaForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		currentForm.setRichiesta(null);
		currentForm.setUteAna(currentForm.getDettaglioConferma());
		currentForm.setDettaglioConferma(null);
		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		// dai servizi utente tolgo gli eliminati e ripulisco il flag cancella
		// nei record selezionati per cui non è stata richiesta la cancellazione
		if (currentForm.getUteAna().getAutorizzazioni().getServizi() != null
				&& currentForm.getUteAna().getAutorizzazioni().getServizi().size() > 0) {
			this.resetCampoCancella(currentForm.getUteAna().getAutorizzazioni().getServizi());
		}
		this.resetToken(request);

		currentForm.setUteAna(new UtenteBibliotecaVO());

		currentForm.getUteAna().setCodBibSer(currentForm.getBiblioteca());
		currentForm.getUteAna().setCodPoloSer(navi.getUtente().getCodPolo());
		currentForm.getUteAna().setCodiceBiblioteca(currentForm.getBiblioteca());
		currentForm.getUteAna().setCodPolo(navi.getUtente().getCodPolo());
		currentForm.getUteAna().setUteIns(navi.getUtente().getFirmaUtente());
		currentForm.getUteAna().setUteVar(navi.getUtente().getFirmaUtente());

		currentForm.setRichiesta(null);
		currentForm.setConferma(false);
		currentForm.getUteAna().setNuovoUte(true);

		//this.setMaterieUtente(currentForm);

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		// dai servizi utente tolgo gli eliminati e ripulisco il flag cancella
		// nei record selezionati per cui non è stata richiesta la cancellazione
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		if (ValidazioneDati.isFilled(utente.getAutorizzazioni().getServizi()) )
			this.resetCampoCancella(utente.getAutorizzazioni().getServizi());

		resetToken(request);
		request.setAttribute("VengoDa", "");
		request.setAttribute("RicercaUtenti", utente);
		/**
		 * ActionForward pagina = new ActionForward(); pagina.setName("pagRit");
		 * pagina.setPath(this.dettaglioAnaForm.getPathForm() + ".do"); return
		 * pagina;
		 */
		if (utente != null && utente.isNuovoUte())
			return mapping.findForward("cancella");

		else {
			//almaviva5_20110322 #4118
			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarkExists(NavigazioneServizi.BOOKMARK_EROGAZIONE)) {
				if (utente.getTsVar().after(currentForm.getCreationTime())) { //modificato
					request.setAttribute(NavigazioneServizi.CHIUDI, NavigazioneServizi.CHIUDI_DETTAGLIO_UTENTE);
					request.setAttribute(NavigazioneServizi.OGGETTO_MODIFICATO, true);
					return navi.goBack();
				}

				return navi.goBack(true);
			}

			//almaviva5_20151124 servizi ill
			if (navi.bookmarkExists(Bookmark.Servizi.DATI_RICHIESTA_ILL))
				return navi.goBack(true);

			return mapping.findForward("annulla");
		}

	}

	public ActionForward tesserino(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		// dai servizi utente tolgo gli eliminati e ripulisco il flag cancella
		// nei record selezionati per cui non è stata richiesta la cancellazione
		if (currentForm.getUteAna().getAutorizzazioni().getServizi() != null
				&& currentForm.getUteAna().getAutorizzazioni()
						.getServizi().size() > 0) {
			this.resetCampoCancella(currentForm.getUteAna()
					.getAutorizzazioni().getServizi());
		}
		resetToken(request);

		// implementazione dati per il tesserino tck 3355

		UtenteBibliotecaVO utenteXtesserino = new UtenteBibliotecaVO();
		utenteXtesserino = (UtenteBibliotecaVO) currentForm.getUteAna().clone();
		utenteXtesserino.setDescrBiblioteca(Navigation.getInstance(request).getUtente().getBiblioteca());
		request.getSession().setAttribute("UtenteBibliotecaVO",	utenteXtesserino);

		return mapping.findForward("tesserino");
	}

	public ActionForward impBiblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		// dai servizi utente tolgo gli eliminati e ripulisco il flag cancella
		// nei record selezionati per cui non è stata richiesta la cancellazione
		if (currentForm.getUteAna().getAutorizzazioni().getServizi() != null
				&& currentForm.getUteAna().getAutorizzazioni()
						.getServizi().size() > 0) {
			this.resetCampoCancella(currentForm.getUteAna()
					.getAutorizzazioni().getServizi());
		}
		resetToken(request);
		return mapping.findForward("impbiblio");
	}

	public ActionForward anagrafe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		this.resetCampoCancella(currentForm.getUteAna()
				.getAutorizzazioni().getServizi());
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_ANAGRAFICA);
		this.loadCombo(form);
		return mapping.getInputForward();
	}

	public ActionForward autorizzazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_AUTORIZZAZIONI);
		this.loadAutorizzazioni(request, form);
		return mapping.getInputForward();
	}

	public ActionForward bibliotecapolo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_BIBLIOPOLO);
		return mapping.getInputForward();
	}

	public ActionForward cancellaServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);


		resetToken(request);
		// dai servizi utente tolgo gli eliminati e pulisco il flag cancella nei
		// record selezionati per cui non è stata richiesta la cancellazione
		List<ServizioVO> servizi = currentForm.getUteAna().getAutorizzazioni().getServizi();
		if (ValidazioneDati.isFilled(servizi) )
			this.eliminaRecord(servizi);

		return mapping.getInputForward();
	}

	public ActionForward insServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);
		// scorro i servizi utente tolgo gli eliminati e ripulisco cancella nei
		// record ceccati per i quali non è stata richiesta la cancellazione
		if (currentForm.getUteAna().getAutorizzazioni().getServizi() != null
				&& currentForm.getUteAna().getAutorizzazioni()
						.getServizi().size() > 0) {
			this.resetCampoCancella(currentForm.getUteAna()
					.getAutorizzazioni().getServizi());
		}
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		// DescrittoreBloccoVO blocco1 = delegate.caricaListaAnagServiziUte(
		// request, navi.getUserTicket(), currentForm.getUteAna()
		// .getAutorizzazioni().getServizi(),
		// navi.getUtente().getCodPolo(),
		// currentForm.getUteAna().getCodBibSer(), currentForm
		// .getUteAna().getCodiceUtente(), currentForm
		// .getUteAna().getCodiceBiblioteca(), 10);
		DescrittoreBloccoVO blocco1 = delegate.caricaListaAnagServiziUte(navi
				.getUserTicket(), currentForm.getUteAna(), 10);
		if (blocco1.getTotRighe() > 0) {
			request.setAttribute("CodiceUtente", currentForm
					.getUteAna().getCodiceUtente());
			request.setAttribute("CodiceBibUtente", currentForm
					.getUteAna().getCodiceBiblioteca());
			request.setAttribute("CodicePolo", currentForm.getUteAna()
					.getCodPolo());
			request.setAttribute("CodiceBibServizio", currentForm
					.getUteAna().getCodBibSer());
			request.setAttribute("ServAssociati", currentForm
					.getUteAna().getAutorizzazioni().getServizi());
			request.setAttribute("PathForm", mapping.getPath());
			request.setAttribute(
					ServiziDelegate.LISTA_ANAGRAFICA_SERVIZI_BIBLIOTECA,
					blocco1);
			return mapping.findForward("insServizio");
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.ListaServiziVuota"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}
	}

	public ActionForward altriDati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);
		// request.setAttribute("UtenteOld",this.dettaglioAnaForm.getUteAnaOLD());


//		request.setAttribute("UtenteScelto", currentForm.getUteAna());
//		return mapping.findForward("altriDati");

		//this.setMaterieUtente(currentForm);
		currentForm.setMostraAltriDati(true);
		return mapping.getInputForward();
	}


	public ActionForward chiudiAltriDati(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}

		resetToken(request);

		currentForm.setMostraAltriDati(false);
		return mapping.getInputForward();
	}


	public ActionForward imponiTipoUtente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);

		if (currentForm.getTipoUtente().equals("E"))
		{
			currentForm.setTipoUtente("E");
			currentForm.getUteAna().getProfessione().setPersonaGiuridica('S');
		}
		else
		{
			currentForm.setTipoUtente("P");
			currentForm.getUteAna().getProfessione().setPersonaGiuridica('N');
		}


		return mapping.getInputForward();
	}


	public ActionForward imponiTipoUtenteP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);
		currentForm.setTipoUtente("P");
		currentForm.getUteAna().getProfessione().setPersonaGiuridica('N');

		return mapping.getInputForward();
	}


	public ActionForward aggiornaServiziPerAutorizzazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		resetToken(request);

		Navigation navi = Navigation.getInstance(request);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		//almaviva5_20110201 #4184
		List<ServizioVO> servizi = currentForm.getUteAna().getAutorizzazioni().getServizi();
		if (ValidazioneDati.isFilled(servizi))
			for (ServizioVO srv : servizi)
				srv.setCancella("C");
		this.eliminaRecord(servizi);
		//

		// legge servizi per Autorizzazione e carica la lista da anagrafica
		// servizi già in formato servizioVO
		currentForm.setLstServiziAutor(factory.getGestioneServizi()	.getListaServiziAutorizzazione(navi.getUserTicket(), currentForm.getUteAna()));
		List<ServizioVO> listaServiziAut = currentForm.getLstServiziAutor();
		// elimina tutti i vecchi servizi dall'array
		// inserisce i servizi della nuova autor. settando lo stato giusto
		if (ValidazioneDati.isFilled(listaServiziAut))
			inserisciServiziAutorizzazione(listaServiziAut, servizi, currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor());

		if (currentForm.getUteAna().getAutorizzazioni().getListaServizi() != null
				&& currentForm.getUteAna().getAutorizzazioni().getListaServizi().size() > 0) {

			//almaviva5_20110427 #4402
			BiblioPoloVO bp = currentForm.getUteAna().getBibliopolo();
			Date dtInizioAut = ValidazioneDati.coalesce(DateUtil.toDate(bp.getInizioAuto()), new Date() );
			for (int index = 0; index <currentForm.getUteAna().getAutorizzazioni().getListaServizi().size(); index++) {
				ServizioVO ele=currentForm.getUteAna().getAutorizzazioni().getListaServizi().get(index);
				ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getCodice()));
				ele.setDataInizio(dateToString(dtInizioAut));
				if (currentForm.getUteAna()!=null && currentForm.getUteAna().getBibliopolo()!=null && currentForm.getUteAna().getBibliopolo().getFineAuto()!=null )
				{
					ele.setDataFine(currentForm.getUteAna().getBibliopolo().getFineAuto());
				}
				if (currentForm.getUteAna()!=null && currentForm.getUteAna().getBibliopolo()!=null && currentForm.getUteAna().getBibliopolo().getInizioSosp()!=null )
				{
					ele.setSospDataInizio(currentForm.getUteAna().getBibliopolo().getInizioSosp());
				}
				if (currentForm.getUteAna()!=null && currentForm.getUteAna().getBibliopolo()!=null && currentForm.getUteAna().getBibliopolo().getFineSosp()!=null )
				{
					ele.setSospDataFine(currentForm.getUteAna().getBibliopolo().getFineSosp());
				}
			}
		}

		return mapping.getInputForward();

	}

	private void inserisciServiziAutorizzazione(List<ServizioVO> listaServiziAut,
			List<ServizioVO> serviziUtente, String codTipoAut) {
		// RIPULISCO L'ARRaY DEI VECCHI SERVIZI
		//serviziUtente.clear();
		if (listaServiziAut == null || listaServiziAut.size() == 0)
			return;
		int index = 0;
		while (index < listaServiziAut.size()) {
			ServizioVO serBib = listaServiziAut.get(index);
			try {
				serBib.setDescrizioneTipoServizio(this.cercaDescrServizio(serBib.getCodice()));
			} catch (Exception e)
			{
				log.error("", e);
			}
			serBib.setCancella("");
			serBib.setAutorizzazione(String.valueOf(codTipoAut).trim().toUpperCase());
			serBib.setFlag_aut_ereditato(String.valueOf(codTipoAut).trim().toUpperCase());
			serBib.setStato(ServizioVO.NEW);
			serviziUtente.add(serBib);
			index++;
		}
	}


	private List<ServizioVO> resetListaDiritti(List<ServizioVO> listaDiritti) {
		// RIPULISCO L'ARRaY DEI VECCHI SERVIZI

		List <ServizioVO> listaDirittiOrigin=new ArrayList<ServizioVO> ();
		if (listaDiritti == null || listaDiritti.size() == 0)
			return listaDirittiOrigin;
		int index = 0;
		int indNew=0;
		while (index < listaDiritti.size()) {
			ServizioVO serBib = listaDiritti.get(index);
			if (serBib.getStato()==ServizioVO.NEW || serBib.getStato()==ServizioVO.MOD || serBib.getStato()==ServizioVO.OLD)
			{
				serBib.setStato(ServizioVO.OLD);
				listaDirittiOrigin.add(indNew, serBib);
				indNew++;
			}
			index++;
		}
		return listaDirittiOrigin;
	}

	public void eliminaRecord(List<ServizioVO> servizi) throws Exception {
		if (!ValidazioneDati.isFilled(servizi) )
			return;

		for (ServizioVO srv : servizi) {
			if (srv.getStato() == ServizioVO.ELI) {
				srv.setCancella("");
				srv.setStato(ServizioVO.DELELI);
			}
			if (srv.getStato() == ServizioVO.DELMOD) {
				srv.setCancella("");
				srv.setStato(ServizioVO.DELDELMOD);
			}
			if (srv.getStato() == ServizioVO.DELOLD) {
				srv.setCancella("");
				srv.setStato(ServizioVO.DELDELOLD);
			}
		}
	}

	public ActionForward aggParametri(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		resetToken(request);
		UtenteBibliotecaVO dettaglioUtente = currentForm.getUteAna();
		AutorizzazioniVO autorizzazioni = dettaglioUtente.getAutorizzazioni();

		if (ciSonoSelezionati(autorizzazioni.getListaServizi())) {
			BiblioPoloVO biblioPoloVO = dettaglioUtente.getBibliopolo();
			if (ValidazioneDati.isFilled(biblioPoloVO.getInizioAuto())
					|| ValidazioneDati.isFilled(biblioPoloVO.getFineAuto())
					|| ValidazioneDati.isFilled(biblioPoloVO.getInizioSosp())
					|| ValidazioneDati.isFilled(biblioPoloVO.getFineSosp())) {
				boolean ret = false;
				ret = this.verificaPerAggiornaDatiLista(request,
						biblioPoloVO.getInizioAuto(), biblioPoloVO.getFineAuto(),
						biblioPoloVO.getInizioSosp(), biblioPoloVO.getFineSosp(), "AUT");
				if (ret)
					if (ValidazioneDati.isFilled(autorizzazioni.getListaServizi()) )
						autorizzazioni.setServizi(this.aggiornaDatiLista(autorizzazioni.getListaServizi(), form));


			}
		}
		else
		{
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
		}


		return mapping.getInputForward();
	}

	private boolean ciSonoSelezionati(List<ServizioVO> serviziSel) throws Exception {
		boolean ret = false;
		for (int index = 0; index < serviziSel.size(); index++) {
			ServizioVO serBib = serviziSel.get(index);
			if (serBib.getCancella().equals("C")) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	private boolean verificaPerAggiornaDatiLista(HttpServletRequest request,
			String dataIA, String dataFA, String dataIS, String dataFS, String livello)
			throws Exception {
		// parametrizzare per controllo per ogni riga di lista servizi
		if (livello==null || (livello!=null && livello.trim().length()==0) || (livello!=null && !livello.equals("AUT") && !livello.equals("DIR")))
		{
			livello="AUT";
		}
		ActionMessages errors = new ActionMessages();
		boolean ret = true;
		int ritorno = 0;
		if (ValidazioneDati.isFilled(dataIA)
				&& ValidazioneDati.strIsNull(dataFA)) {
			if (livello.equals("AUT"))
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dataInizioSenzaDataFine"));
			}
			else
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dir.dataInizioSenzaDataFine"));
			}
			this.saveErrors(request, errors);
			ret = false;
		}
		if (ValidazioneDati.strIsNull(dataIA)
				&& ValidazioneDati.isFilled(dataFA)) {
			if (livello.equals("AUT"))
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dataFineAutSenzaDataInizioAut"));
			}
			else
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dir.dataFineAutSenzaDataInizioAut"));
			}

			this.saveErrors(request, errors);
			ret = false;
		}
		if (ValidazioneDati.isFilled(dataIS)
				&& ValidazioneDati.strIsNull(dataFS)) {
			if (livello.equals("AUT"))
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dataInizioSospSenzaDataFineSosp"));
			}
			else
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dir.dataInizioSospSenzaDataFineSosp"));
			}

			this.saveErrors(request, errors);
			ret = false;
		}
		if (ValidazioneDati.isFilled(dataFS)
				&& ValidazioneDati.strIsNull(dataIS)) {
			if (livello.equals("AUT"))
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dataFineSospSenzaDataInizioSosp"));
			}
			else
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.dir.dataFineSospSenzaDataInizioSosp"));
			}

			this.saveErrors(request, errors);
			ret = false;
		}

		if (ValidazioneDati.strIsNull(dataIA)
				&& (ValidazioneDati.isFilled(dataIS) || !ValidazioneDati
						.strIsNull(dataFS))) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataSospOFineSospSenzaDataInizio"));
			this.saveErrors(request, errors);
			ret = false;
		}
		// ************************************************************************

/*		if (ValidazioneDati.isFilled(dataIA)
				&& ValidazioneDati.isFilled(dataFA)) {
			ritorno = ValidazioneDati.confrontaLeDate(dataFA, dataIA,
					FileConstants.VERIFICA_DATA1_GT_DATA2);
			if (ritorno != ValidazioneDati.DATA1_GT_DATA2) {  // n.b. dataFA <= dataIA
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineMinoreDataInizio"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineMinoreDataInizio"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataIS)
				&& ValidazioneDati.isFilled(dataFS)) {
			ritorno = ValidazioneDati.confrontaLeDate(dataFS, dataIS,
					FileConstants.VERIFICA_DATA1_GT_DATA2);
			if (ritorno != ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineSospMinoreDataInizioSosp"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineSospMinoreDataInizioSosp"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		// *********** ulteriori cantrolli
		if (ValidazioneDati.isFilled(dataIA)
				&& ValidazioneDati.isFilled(dataIS)) {
			ritorno = ValidazioneDati.confrontaLeDate(dataIS, dataIA,
					FileConstants.VERIFICA_DATA1_GT_DATA2);
			if (ritorno != ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataInizioSospMinoreDataInizio"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataInizioSospMinoreDataInizio"));
				}
				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataFA)
				&& ValidazioneDati.isFilled(dataIS)) {
			ritorno = ValidazioneDati.confrontaLeDate(dataFA, dataIS,
					FileConstants.VERIFICA_DATA1_GT_DATA2);
			if (ritorno != ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataInizioSospMaggioreDataFine"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataInizioSospMaggioreDataFine"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataFS)
				&& ValidazioneDati.isFilled(dataIA)) {
			ritorno = ValidazioneDati.confrontaLeDate(dataFS, dataIA,
					FileConstants.VERIFICA_DATA1_GT_DATA2);
			if (ritorno != ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineSospMinoreDataInizio"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineSospMinoreDataInizio"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataFA)
				&& ValidazioneDati.isFilled(dataFS)) {
			ritorno = ValidazioneDati.confrontaLeDate(dataFA, dataFS,
					FileConstants.VERIFICA_DATA1_GT_DATA2);
			if (ritorno != ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineSospMaggioreDataFine"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineSospMaggioreDataFine"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
*/
		// da testare

		if (ValidazioneDati.isFilled(dataIA)
				&& ValidazioneDati.isFilled(dataFA)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dataFA, dataIA);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {  // n.b. dataFA <= dataIA
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineMinoreDataInizio"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineMinoreDataInizio"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataIS)
				&& ValidazioneDati.isFilled(dataFS)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dataFS, dataIS);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineSospMinoreDataInizioSosp"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineSospMinoreDataInizioSosp"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		// *********** ulteriori cantrolli
		if (ValidazioneDati.isFilled(dataIA)
				&& ValidazioneDati.isFilled(dataIS)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dataIS, dataIA);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataInizioSospMinoreDataInizio"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataInizioSospMinoreDataInizio"));
				}
				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataFA)
				&& ValidazioneDati.isFilled(dataIS)) {
			ritorno = ValidazioneDati.confrontaLeDateBest( dataIS,dataFA);
			if (ritorno == ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataInizioSospMaggioreDataFine"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataInizioSospMaggioreDataFine"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataFS)
				&& ValidazioneDati.isFilled(dataIA)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dataFS, dataIA);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineSospMinoreDataInizio"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineSospMinoreDataInizio"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		if (ValidazioneDati.isFilled(dataFA)
				&& ValidazioneDati.isFilled(dataFS)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dataFS,dataFA);
			if (ritorno == ValidazioneDati.DATA1_GT_DATA2) {
				if (livello.equals("AUT"))
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dataFineSospMaggioreDataFine"));
				}
				else
				{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.dir.dataFineSospMaggioreDataFine"));
				}

				this.saveErrors(request, errors);
				ret = false;
			}
		}
		return ret;
	}

	private boolean verificheUterioriSuDate(HttpServletRequest request,
			String dtInizioAut, String dtFineAut, String dtInizioSosp, String dtFineSosp, BiblioPoloVO bp)
			throws Exception {
		// parametrizzare per controllo per ogni riga di lista servizi

		//conforme al documento sui controlli dati inviato con mail del 30.11.09 da contardi

		String dtDataReg = ""; // data di prima iscrizione data_reg
		String dtInizioGenAut = ""; // data autorizzazione inizio
		String dtFineGenAut = ""; // data autorizzazione fine
		String dtInizioGenSosp = ""; // data inizio sospensione autorizzazione
		String dtFineGenSosp = ""; // data fine sospensione autorizzazione

		if (bp != null && bp.getPoloDataRegistrazione() != null
				&& bp.getPoloDataRegistrazione().trim().length() > 0) {
			dtDataReg = bp.getPoloDataRegistrazione();
		} else
			dtDataReg = dateToString(new Date());// data odierna

		if (bp != null && bp.getInizioAuto() != null
				&& bp.getInizioAuto().trim().length() > 0) {
			dtInizioGenAut = bp.getInizioAuto();
		}
		if (bp != null && bp.getFineAuto() != null
				&& bp.getFineAuto().trim().length() > 0) {
			dtFineGenAut = bp.getFineAuto();
		}
		if (bp != null && bp.getInizioSosp() != null
				&& bp.getInizioSosp().trim().length() > 0) {
			dtInizioGenSosp = bp.getInizioSosp();
		}
		if (bp != null && bp.getFineSosp() != null
				&& bp.getFineSosp().trim().length() > 0) {
			dtFineGenSosp = bp.getFineSosp();
		}

		ActionMessages errors = new ActionMessages();
		boolean ret = true;
		int ritorno = 0;


		//Dai < Dis
		if (ValidazioneDati.isFilled(dtInizioGenAut)
				&& ValidazioneDati.isFilled(dtDataReg)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dtInizioGenAut, dtDataReg);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.dataGenInizioAutMinoreDataIscr"));
				this.saveErrors(request, errors);
				ret = false;
			}
		}

		//almaviva5_20110428 #4402
		if (ValidazioneDati.isFilled(dtInizioAut)
				&& ValidazioneDati.isFilled(dtInizioGenAut)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dtInizioAut, dtInizioGenAut);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.dataInizioAutDirMinoreDataGenInizioAut"));
				this.saveErrors(request, errors);
				ret = false;
			}
		}

		if (ValidazioneDati.isFilled(dtFineGenAut)
				&& ValidazioneDati.isFilled(dtFineAut)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dtFineAut, dtFineGenAut);
			if (ritorno == ValidazioneDati.DATA1_GT_DATA2) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.dataFineAutDirMaggioreDataGenFineAut"));
				this.saveErrors(request, errors);
				ret = false;
			}
		}

		//Dasi<dai
		if (ValidazioneDati.isFilled(dtInizioGenSosp)
				&& ValidazioneDati.isFilled(dtInizioGenAut)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dtInizioGenSosp, dtInizioGenAut);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.dataGenInizioSospMinoreDataGenInizioAut"));
				this.saveErrors(request, errors);
				ret = false;
			}
		}

		//dasi<ddsi
		if (ValidazioneDati.isFilled(dtInizioGenSosp)
				&& ValidazioneDati.isFilled(dtInizioSosp)) {
			ritorno = ValidazioneDati.confrontaLeDateBest( dtInizioSosp, dtInizioGenSosp);
			if (ritorno == ValidazioneDati.DATA1_GT_DATA2) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.dataInizioSospMaggioreDataGenInizioSosp"));
				this.saveErrors(request, errors);
				ret = false;
			}
		}

		//ddsf<dasf
		if (ValidazioneDati.isFilled(dtFineSosp)
				&& ValidazioneDati.isFilled(dtFineGenSosp)) {
			ritorno = ValidazioneDati.confrontaLeDateBest(dtFineSosp, dtFineGenSosp);
			if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.dataFineSospMinoreDataGenFineSosp"));
				this.saveErrors(request, errors);
				ret = false;
			}
		}

		return ret;
	}


	private List<ServizioVO> aggiornaDatiLista(List<ServizioVO> serviziSel, ActionForm form)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		List<ServizioVO> listamat = new ArrayList<ServizioVO>();
		for (int index = 0; index < serviziSel.size(); index++) {
			ServizioVO serBib = serviziSel.get(index);
			if ((serBib.getCancella().equals("C"))
					&& (serBib.getStato() != ServizioVO.DELDELMOD
							&& serBib.getStato() != ServizioVO.DELDELOLD && serBib
							.getStato() != ServizioVO.DELELI)) {
				serBib.setCancella("");
				serBib.setDataInizio(currentForm.getUteAna()
						.getBibliopolo().getInizioAuto());
				serBib.setDataFine(currentForm.getUteAna()
						.getBibliopolo().getFineAuto());
				serBib.setSospDataInizio(currentForm.getUteAna()
						.getBibliopolo().getInizioSosp());
				serBib.setSospDataFine(currentForm.getUteAna()
						.getBibliopolo().getFineSosp());
				listamat.add(serBib);
			} else {
				listamat.add(serBib);
			}
		}
		return listamat;
	}

	private boolean  aggiornaListaServizi(List<ServizioVO> serviziSel, ActionForm form, HttpServletRequest request)
	throws Exception {
		boolean adeguato=false;
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;

		List<ServizioVO> listamat = new ArrayList<ServizioVO>();
		for (int index = 0; index < serviziSel.size(); index++) {
			ServizioVO serBib = serviziSel.get(index);
			if (ValidazioneDati.strIsNull(serBib.getDataInizio()) && ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getInizioAuto()))
				{

					//int ritorno = 0;
					// escludo le impostazioni con date pregresse
					//if (ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getInizioAuto())
					//		&& ValidazioneDati.isFilled(this.dateToString(new Date()))) {
					//	ritorno = ValidazioneDati.confrontaLeDate(currentForm.getUteAna().getBibliopolo().getInizioAuto(), this.dateToString(new Date()),
					//			FileConstants.VERIFICA_DATA1_LT_DATA2);
					//	if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
							serBib.setDataInizio(currentForm.getUteAna().getBibliopolo().getInizioAuto());
							adeguato=true;
					//	}
					//}

				}
				if (ValidazioneDati.strIsNull(serBib.getDataFine()) && ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getFineAuto()))
				{
					serBib.setDataFine(currentForm.getUteAna().getBibliopolo().getFineAuto());
					adeguato=true;
				}

				Date now = new Date();
				if (ValidazioneDati.strIsNull(serBib.getSospDataInizio()) && ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getInizioSosp()))
				{
					int ritorno = 0;
					if (ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getInizioSosp())
							&& ValidazioneDati.isFilled(dateToString(now))) {
						ritorno = ValidazioneDati.confrontaLeDate(currentForm.getUteAna().getBibliopolo().getInizioSosp(), dateToString(now),
								FileConstants.VERIFICA_DATA1_LT_DATA2);
						if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
							serBib.setSospDataInizio(currentForm.getUteAna().getBibliopolo().getInizioSosp());
							adeguato=true;
						}
//						 rimozione temporanea della gestione degli errori dovuti alla immissione di date inferiori alla data odierna
/*						else
						{
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMinoreDataOdierna"));
							this.saveErrors(request, errors);
							throw new Exception();
						}
*/
					}

				}
				if (ValidazioneDati.strIsNull(serBib.getSospDataFine()) && ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getFineSosp()))
				{
					int ritorno = 0;
					if (ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getFineSosp())
							&& ValidazioneDati.isFilled(dateToString(now))) {
						ritorno = ValidazioneDati.confrontaLeDate(currentForm.getUteAna().getBibliopolo().getFineSosp(), dateToString(now),
								FileConstants.VERIFICA_DATA1_LT_DATA2);
						if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
							serBib.setSospDataFine(currentForm.getUteAna().getBibliopolo().getFineSosp());
							adeguato=true;
						}
//						 rimozione temporanea della gestione degli errori dovuti alla immissione di date inferiori alla data odierna
/*						else
						{
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMinoreDataOdierna"));
							this.saveErrors(request, errors);
							throw new Exception();
						}
*/

					}

				}
				// caso  data fine sosp < data fine sosp gen si deve aggiornare con quella gen
				if (ValidazioneDati.isFilled(serBib.getSospDataFine()) && ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getFineSosp()))
				{
					int ritorno = 0;
					if (ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getFineSosp())
							&& ValidazioneDati.isFilled(serBib.getSospDataFine())) {
						ritorno = ValidazioneDati.confrontaLeDateBest( serBib.getSospDataFine(), currentForm.getUteAna().getBibliopolo().getFineSosp());
						if (ritorno == ValidazioneDati.DATA1_LT_DATA2) {
							serBib.setSospDataFine(currentForm.getUteAna().getBibliopolo().getFineSosp());
							adeguato=true;
						}
//						 rimozione temporanea della gestione degli errori dovuti alla immissione di date inferiori alla data odierna
/*						else
						{
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMinoreDataOdierna"));
							this.saveErrors(request, errors);
							throw new Exception();
						}
*/

					}

				}
				// caso  data ini sosp > data ini sosp gen si deve aggiornare con quella gen
				if (ValidazioneDati.isFilled(serBib.getSospDataInizio()) && ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getInizioSosp()))
				{
					int ritorno = 0;
					if (ValidazioneDati.isFilled(currentForm.getUteAna().getBibliopolo().getInizioSosp())
							&& ValidazioneDati.isFilled(serBib.getSospDataInizio())) {
						ritorno = ValidazioneDati.confrontaLeDateBest(serBib.getSospDataInizio(), currentForm.getUteAna().getBibliopolo().getInizioSosp());
						if (ritorno == ValidazioneDati.DATA1_GT_DATA2) {
							serBib.setSospDataInizio(currentForm.getUteAna().getBibliopolo().getInizioSosp());
							adeguato=true;
						}
//						 rimozione temporanea della gestione degli errori dovuti alla immissione di date inferiori alla data odierna
/*						else
						{
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dataMinoreDataOdierna"));
							this.saveErrors(request, errors);
							throw new Exception();
						}
*/
					}

				}

				listamat.add(serBib);
		}
		if (adeguato && serviziSel!=null &&  serviziSel.size()>0  && listamat!=null && listamat.size()>0 && listamat.size()==serviziSel.size())
		{
			currentForm.setLstServiziAutor(listamat);
		}
	return adeguato;
	}




	private void loadCombo(ActionForm form) throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		CaricamentoCombo carCombo = new CaricamentoCombo();
		currentForm.setProvenienze(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROVENIENZA)));
		currentForm.setNazCitta(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PAESE)));
		List<?> province = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE));
		currentForm.setProvinciaResidenza(province);
		currentForm.setProvinciaDomicilio(province);
		currentForm.setTitoloStudioArr(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TITOLO_STUDIO)));
		currentForm.setProfessioneArr(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROFESSIONI)));

	}

	private ActionForward loadDefaultStandard(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		//if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

			try {
				if (currentForm.getUteAna().isNuovoUte())
				{
					if (currentForm.getUteAna()!=null && currentForm.getUteAna().getAutorizzazioni()!=null && utenteEjb.getDefault(ConstantDefault.SER_INS_UTE_TIPO_AUT)!=null && utenteEjb.getDefault(ConstantDefault.SER_INS_UTE_TIPO_AUT).toString().trim().length()>0)
					{
						currentForm.getUteAna().getAutorizzazioni().setCodTipoAutor((String)utenteEjb.getDefault(ConstantDefault.SER_INS_UTE_TIPO_AUT));
					}
					if (currentForm.getUteAna()!=null && currentForm.getUteAna().getAnagrafe()!=null &&  utenteEjb.getDefault(ConstantDefault.SER_INS_UTE_NAZIONE)!=null && utenteEjb.getDefault(ConstantDefault.SER_INS_UTE_NAZIONE).toString().trim().length()>0)
					{
						currentForm.getUteAna().getAnagrafe().setNazionalita((String)utenteEjb.getDefault(ConstantDefault.SER_INS_UTE_NAZIONE));
					}
				}
			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.acquisizioni.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		//}
		return mapping.getInputForward();
	}

	private void loadAutorizzazioni(HttpServletRequest request, ActionForm form)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		// autorizzazioni che vengono caricate tutte
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		RicercaAutorizzazioneVO ricerca = new RicercaAutorizzazioneVO();
		ricerca.setCodPolo(currentForm.getUteAna().getCodPoloSer());
		ricerca.setCodBib(currentForm.getUteAna().getCodBibSer());
		ricerca.setNumeroElementiBlocco(0);
		ricerca.setTicket(Navigation.getInstance(request).getUserTicket());
		DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(ricerca);

		List<AutorizzazioneVO> listaAut = new ArrayList<AutorizzazioneVO>();
		listaAut.add(new AutorizzazioneVO("", ""));
		if (blocco1.getTotRighe() > 0)
			listaAut.addAll(blocco1.getLista());

		currentForm.setElencoAutorizzazioni(listaAut);
	}


	private List<ServizioVO> loadServiziUtente(List<ServizioVO> serviziSel) throws Exception {
		List<ServizioVO> listamat = new ArrayList<ServizioVO>();
		if (serviziSel != null && serviziSel.size() != 0) {
			for (int index = 0; index < serviziSel.size(); index++) {
				ServizioVO serBib = serviziSel.get(index);
				listamat.add(serBib);
			}
		}
		return listamat;
	}

	public void resetCampoCancella(List<ServizioVO> servizi) throws Exception {

		for (ServizioVO servizio : servizi) {
			if (servizio.getStato() != ServizioVO.DELELI
					&& servizio.getStato() != ServizioVO.DELDELMOD
					&& servizio.getStato() != ServizioVO.DELDELOLD)
				servizio.setCancella("");
		}
	}



	private void loadProfessioni(HttpServletRequest request, ActionForm form) throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		// professioni
		CaricamentoCombo carCombo = new CaricamentoCombo();
		currentForm.setTitoloStudio(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TITOLO_STUDIO)));
		currentForm.setProfessioni(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROFESSIONI)));
		currentForm.setAteneo(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_ATENEI)));
		currentForm.setTipoPersonalita(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_PERSONA_GIURIDICA)));

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(navi.getUtente().getCodPolo());
		ricercaTDS.setCodBib(currentForm.getBiblioteca());
		ricercaTDS.setTicket(navi.getUserTicket());
		ricercaTDS.setNumeroElementiBlocco(0);
		ricercaTDS.setOrdinamento("DETTUTEANA");
		DescrittoreBloccoVO blocco1 = delegate.caricaListaSpecialita(ricercaTDS);

		List<SpecTitoloStudioVO> appoggioSpecTdS = new ArrayList<SpecTitoloStudioVO>();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (blocco1.getTotRighe() > 0)
			appoggioSpecTdS.addAll(blocco1.getLista());
		currentForm.setSpecTitoloStudio(appoggioSpecTdS);

		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(navi.getUtente().getCodPolo());
		ricercaOccup.setCodBib(currentForm.getBiblioteca());
		ricercaOccup.setTicket(navi.getUserTicket());
		ricercaOccup.setNumeroElementiBlocco(4000);
		ricercaOccup.setOrdinamento("DETTUTEANA");
		blocco1 = delegate.caricaListaOccupazioni(ricercaOccup);
		List<OccupazioneVO> listaOccup = new ArrayList<OccupazioneVO>();
		listaOccup.add( new OccupazioneVO("", "", "", ""));
		if (blocco1.getTotRighe() > 0)
			listaOccup.addAll(blocco1.getLista());
		currentForm.setOccupazioni(listaOccup);
	}

	private void loadDocumenti(ActionForm form) throws Exception {
		DettaglioUtentiAnaForm altriDatiUteForm = (DettaglioUtentiAnaForm) form;
		// documenti
		CaricamentoCombo carCombo = new CaricamentoCombo();
		altriDatiUteForm.setElencoDocumenti(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DOCUMENTO_DI_RICONOSCIMENTO)));
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getSelectedUtenti().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_ANAGRAFICA);
					currentForm.setUteAna(currentForm.getSelectedUtenti().get(attualePosizione));
					currentForm.setUteAnaOLD((UtenteBibliotecaVO)currentForm.getUteAna().clone());
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			if (currentForm.getUteAna().getAutorizzazioni().getListaServizi()!= null && currentForm.getUteAna().getAutorizzazioni().getListaServizi().size() > 0)
			{
				for (int index = 0; index <currentForm.getUteAna().getAutorizzazioni().getListaServizi().size(); index++) {
					ServizioVO ele=currentForm.getUteAna().getAutorizzazioni().getListaServizi().get(index);
					ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getCodice()));
					//ele.setAutorizzazione(String.valueOf(currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor()));
				}
			}
			if (currentForm.getUteAna()!=null && currentForm.getUteAna().getProfessione()!=null && currentForm.getUteAna().getProfessione().isPersonaFisica())
			{
				currentForm.setTipoUtente("P");
			}
			else
			{
				currentForm.setTipoUtente("E");
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			if (attualePosizione < 0) {

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setFolder(DettaglioUtentiAnaForm.FOLDER_ANAGRAFICA);
					currentForm.setUteAna(currentForm.getSelectedUtenti().get(attualePosizione));
					currentForm.setUteAnaOLD((UtenteBibliotecaVO)currentForm.getUteAna().clone());
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			if (currentForm.getUteAna().getAutorizzazioni().getListaServizi()!= null && currentForm.getUteAna().getAutorizzazioni().getListaServizi().size() > 0)
			{
				for (int index = 0; index <currentForm.getUteAna().getAutorizzazioni().getListaServizi().size(); index++) {
					ServizioVO ele=currentForm.getUteAna().getAutorizzazioni().getListaServizi().get(index);
					ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getCodice()));
					//ele.setAutorizzazione(String.valueOf(currentForm.getUteAna().getAutorizzazioni().getCodTipoAutor()));
				}
			}
			if (currentForm.getUteAna()!=null && currentForm.getUteAna().getProfessione()!=null && currentForm.getUteAna().getProfessione().isPersonaFisica())
			{
				currentForm.setTipoUtente("P");
			}
			else
			{
				currentForm.setTipoUtente("E");
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	private void filtraDatiUtente(UtenteBibliotecaVO utenteVO) {
		utenteVO.getAnagrafe().setSesso(utenteVO.getAnagrafe().getSesso().trim());
		utenteVO.getAnagrafe().setProvenienza(utenteVO.getAnagrafe().getProvenienza().trim());
//		utenteVO.getAnagrafe().getNazionalita().trim();
//		utenteVO.getAnagrafe().getResidenza().getNazionalita().trim();
//		utenteVO.getAnagrafe().getResidenza().getCap().trim();
//		utenteVO.getAnagrafe().getDomicilio().getCap();
	}

   private String cercaDescrServizio(String cod) throws Exception {
	   	return CodiciProvider.cercaDescrizioneCodice(cod, CodiciType.CODICE_TIPO_SERVIZIO,CodiciRicercaType.RICERCA_CODICE_SBN);
   }

	public ActionForward importaBibl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("importaBibl");

	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		request.setAttribute(NavigazioneServizi.COD_UTENTE, utente.getCodiceUtente());

		return Navigation.getInstance(request).goToBookmark(Bookmark.Servizi.PRENOTAZIONE_POSTO, false);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		if (!super.checkAttivita(request, form, idCheck))
			return false;

		DettaglioUtentiAnaForm currentForm = (DettaglioUtentiAnaForm) form;
		Navigation navi = Navigation.getInstance(request);

		if (ValidazioneDati.equals(idCheck, "RESET_PASSWORD"))
			return currentForm.getUteAna().getProfessione().isPersonaFisica()
				&& super.checkAttivita(request, form, NavigazioneServizi.GESTIONE)
				&& ValidazioneDati.equals(currentForm.getUteAna().getTipoUtente(), Servizi.Utenti.UTENTE_TIPO_SBNWEB);


		if (ValidazioneDati.equals(idCheck, "CANCELLA"))
			return !navi.bookmarkExists(NavigazioneServizi.BOOKMARK_EROGAZIONE)
				&& super.checkAttivita(request, form, NavigazioneServizi.GESTIONE);

		if (ValidazioneDati.equals(idCheck, "SCEGLI")) {
			UtenteBibliotecaVO utente = currentForm.getUteAna();
			RicercaUtenteBibliotecaVO ricerca = currentForm.getUteRic();
			return !utente.isNuovoUte() && ValidazioneDati.in(ricerca.getParametro(), "prenot_posto") &&
					navi.bookmarkExists(Bookmark.Servizi.PRENOTAZIONE_POSTO);
		}

		return true;
	}

}
