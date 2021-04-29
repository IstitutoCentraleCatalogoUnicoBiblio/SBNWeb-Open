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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.IndirizzoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ResidenzaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.mail.MailBodyBuilder;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.UtenteWebLock;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.InserimentoUtenteWebForm;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.Ticket;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.ServiziWebDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;
// almaviva
public final class InserimentoUtenteWebAction extends LookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.avanti", "avanti");
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.aggiorna", "ok");
		map.put("servizi.bottone.indietro", "riciclo");
		map.put("servizi.indietro.esci", "esci");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		String codPolo = Navigation.getInstance(request).getPolo();
		CaricamentoCombo carCombo = new CaricamentoCombo();

		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;

		//valorizzo i campi della biblioteca impostati in
		//precedenza nella jsp listaBiblio
		currentForm.setNome_biblio((String) request.getAttribute("codbib") + " - " + request.getAttribute("nomebib"));
		currentForm.setCod_biblio(((String) request.getAttribute("codbib")));
		//currentForm.getUteA//luogo di nascita
		currentForm.setNazCitta(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
		currentForm.setProvinciaResidenza(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceProvince()));

		if (!currentForm.isSessione()) {
			HttpSession session = request.getSession();
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
			if (utente != null) {
				ServiziWebDelegate delegate = ServiziWebDelegate.getInstance(request);
				String bib = (String) session.getAttribute(Constants.COD_BIBLIO);
				UtenteBibliotecaVO dettaglio = delegate.getDettaglioUtente(bib, utente);
				currentForm.setUteAna(dettaglio);
				currentForm.setLock(new UtenteWebLock(dettaglio));
				currentForm.setPassword(utente.getPassword());
				currentForm.setCod_biblio(bib);
				currentForm.setModifica(true);
			}

			currentForm.setSessione(true);
		}
		//
		try {
			//se ha selezionato una biblioteca
			if (ValidazioneDati.isFilled(codPolo) ) {
				//inizializzazione di tutti i campi e combo relativi
				//ai dati anagrafici
				return mapping.getInputForward();
			} else { //altrimenti se non ha selezionato una biblioteche
				//invio msg:"selezionare almeno una biblioteca"

				LinkableTagUtils.addError(request, new ActionMessage("servizi.biblioteche.autoregistrazione"));
				//
				return mapping.findForward("listaBiblio");
			}

		}catch (Exception e) {

		}
		return (mapping.getInputForward());
	}

	public ActionForward avanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		//
		//autoregistrazione ad una biblioteca del polo.
		//se risulta iscritto invio un msg:"Attenzione e già iscritto ad
		//una bilioteca del polo" altrimenti si procede con l'autoregistrazione
		//

		//inizio controlli di obbligatorieta
		try {
			ActionMessages  error = this.validateAnag(request, currentForm);
			if (error.size() > 0) {
				this.saveErrors(request, error);
				return mapping.getInputForward();
			}
			//fine controlli di obbligatorieta
			//******************************
			//
			//inizio controlli formali
			ActionMessages  err = this.controlliFormali(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}
			//fine controlli formali
			//******************************
			//prima di procedere con l'iscrizione,
			//controllo se l'utente è già iscritto.
			//inizio controllo esistenza utente

			//almaviva5_20150505 evolutiva mail2
			UtenteBibliotecaVO utente = currentForm.getUteAna();

			// controllo duplicazione codice fiscale // tolto dettaglioUtente.isNuovoUte()
			if (utente != null && utente.getAnagrafe() != null
					&& ValidazioneDati.isFilled(utente.getAnagrafe().getCodFiscale()) ) {
				String codfiscale = utente.getAnagrafe().getCodFiscale();
				String idUtente = utente.getIdUtente();
				boolean duplicato = false;
				duplicato = factory.getGestioneServizi().checkEsistenzaUtente(codfiscale, null, null, idUtente);
				if (duplicato) {
					UtenteWeb uw = factory.getGestioneServiziWeb().esistenzaUtenteWeb(codfiscale, null);
					if (!uw.isCancellato() ) {
						//l'utente risulta già iscritto.
						//Non è consentita l'autoregistrazione perchè i dati forniti risultano associati ad una precedente registrazione.
						//Accedi tramite "login" quindi scegli la biblioteca di interesse.
						//Se hai dimenticato la password indica il tuo codice utente e chiedi di recuperarla, riceverai una e-mail con la nuova password
						//Se hai dimenticato anche il codice utente rivolgiti alla biblioteca.
						LinkableTagUtils.addError(request, new ActionMessage("error.authentication.utenterisultaiscritto" ));
						return mapping.getInputForward();
					}
				}
			}
			try {
				// controllo duplicazione email // tolto dettaglioUtente.isNuovoUte()
				if (utente != null && utente.getAnagrafe() != null) {
					AnagrafeVO anag = utente.getAnagrafe();
					//almaviva5_20150430
					ServiziDelegate delegate = ServiziDelegate.getInstance(request);
					delegate.checkMailUtenteBiblioteca(utente.getIdUtente(), anag.getPostaElettronica(), anag.getPostaElettronica2());
				}
			} catch (SbnBaseException e) {
				LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();
			}

			currentForm.setPassword("Avanti");
			return (mapping.getInputForward());

		} catch (Exception e) {
			//errore di autoregistrazione
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.biblioteche.autoregFallita"));
		}

		return (mapping.getInputForward());

	}
	//
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		String polo = navi.getPolo();
		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		//
		//autoregistrazione ad una biblioteca del polo.
		//se risulta iscritto invio un msg:"Attenzione e già iscritto ad
		//una bilioteca del polo" altrimenti si procede con l'autoregistrazione
		//
		//inizio controlli di obbligatorieta
		if (!ValidazioneDati.isFilled(currentForm.getPassword())) {
			//Prima validare i dati anagrafici” .
			LinkableTagUtils.addError(request, new ActionMessage ("Prima validare i dati anagrafici" ));

			currentForm.setPassword("Avanti");
			return (mapping.getInputForward());

		}
		ActionMessages error = this.validateForm(request, currentForm);
		if (error.size() > 0) {
			this.saveErrors(request, error);
			return mapping.getInputForward();
		}
		//fine controlli di obbligatorieta
		//******************************
		//
		//inizio controlli formali
		ActionMessages  err = this.controlliFormali(request, currentForm);
		if (err.size() > 0) {
			this.saveErrors(request, err);
			return mapping.getInputForward();
		}
		//fine controlli formali
		//******************************
		//prima di procedere con l'iscrizione,
		//controllo se l'utente è già iscritto
		//inizio controllo esistenza utente
		boolean recuperoUtenteCancellato = false;
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		AnagrafeVO anag = utente.getAnagrafe();
		//controllo per cod.fiscale
		String codfiscale = ValidazioneDati.trimOrEmpty(anag.getCodFiscale());
		boolean duplicato = factory.getGestioneServizi().checkEsistenzaUtente(codfiscale, null, null, utente.getIdUtente());
		UtenteWeb uw = factory.getGestioneServiziWeb().esistenzaUtenteWeb(codfiscale, null);
		if (duplicato) {
			//controllo se utente cancellato
			if (uw == null || !uw.isCancellato() ) {
				if (!currentForm.isModifica())
					LinkableTagUtils.addError(request, new ActionMessage("error.authentication.utenterisultaiscritto"));
				else
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreCodFiscaleDuplicato"));

				return mapping.getInputForward();
			}
		}

		String firmaUte = polo + currentForm.getCod_biblio() + Constants.UTENTE_WEB_TICKET;
		utente.setUteVar(firmaUte);
		utente.setUteIns(firmaUte);
		//almaviva5_20120924 #5117 recupero utente cancellato
		//almaviva5_20150723 #5946
		if (uw != null && ValidazioneDati.isFilled(uw.getIdUtente()) ) {
			recuperoUtenteCancellato = true;
			utente.setIdUtente(uw.getIdUtente().toString());
		}

		//controllo per mail
		// controllo duplicazione email // tolto dettaglioUtente.isNuovoUte()
		try {
			//almaviva5_20150430
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.checkMailUtenteBiblioteca(utente.getIdUtente(), anag.getPostaElettronica(), anag.getPostaElettronica2());

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		if (uw != null && !uw.isCancellato() )
			if (!currentForm.isModifica() || !String.valueOf(uw.getIdUtente()).equals(utente.getIdUtente()) ) {
				//"Esiste un'altro utente con questa E-mail"
				LinkableTagUtils.addError(request, new ActionMessage("message.authentication.utenteEmailEsistente" ));
				return mapping.getInputForward();
			}
		//
		//fine controllo esistenza utente
		//**************************************************************
		//Valorizzo domicilio = residenza, se domicilio non specificato:
		//
		ResidenzaVO residenza = anag.getResidenza();
		IndirizzoVO domicilio = anag.getDomicilio();

		//domicilio Indirizzo
		if (!ValidazioneDati.isFilled(domicilio.getVia()) ) {
			domicilio.setVia( residenza.getVia());
		}
		//domicilio Cap
		if (!ValidazioneDati.isFilled(domicilio.getCap()) ) {
			domicilio.setCap( residenza.getCap());
		}
		//domicilio Città
		if (!ValidazioneDati.isFilled(domicilio.getCitta()) ) {
			domicilio.setCitta( residenza.getCitta());
		}
		//domicilio Provincia
		if (!ValidazioneDati.isFilled(domicilio.getProvincia()) ) {
			domicilio.setProvincia( residenza.getProvincia());
		}
		//fine valorizzazione domicilio = residenza.

		//almaviva5_20110118 #4152
		if (currentForm.isModifica()) {
			boolean updateUtente = factory.getGestioneServizi().updateUtente(navi.getUserTicket(), utente);
			if (!updateUtente)
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.biblioteche.autoregFallita"));
			else
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.aggiornaOk"));

			return mapping.getInputForward();
		}

		//*********************************************************************
		//inizio inserimento utente
		//
		// 17.11.09 correzione di almaviva4 per la gestione dell'utente di tipo Biblioteca
		//currentForm.getUteAna().getBibliopolo().setCodPoloXUteBib(polo);
		//currentForm.getUteAna().getBibliopolo().setCodBibXUteBib(currentForm.getCod_biblio());
		utente.getBibliopolo().setCodPoloXUteBib("");
		utente.getBibliopolo().setCodBibXUteBib("");
		utente.setCodiceBiblioteca(currentForm.getCod_biblio());
		utente.setCodPolo(polo);
		utente.setCodPoloSer(polo);
		String ticket = Ticket.getUtenteWebTicket(polo, currentForm.getCod_biblio(), navi.getUserAddress());

		utente.setCodBibSer(currentForm.getCod_biblio());
		utente.setFlCanc("N");
		utente.setFlCancAna("N");
		//
		try {
			//valorizzo i diritti
			List<ServizioVO> listDir = factory.getGestioneServiziWeb().getListaServiziAutorizzazione(ticket, utente);
			utente.getAutorizzazioni().setServizi(listDir);
			//String dataFine = DateUtil.formattaData(System.currentTimeMillis());
			long ts = System.currentTimeMillis();
			String dataFine = "31/12/" + DateUtil.getYear(new Date(ts));
			//valorizzo Codice Tipo Autorizzazione Modifica del 27/01/2010 almaviva
			if (ValidazioneDati.isFilled(listDir)) {
				ServizioVO diritto = listDir.get(0);
				//currentForm.getUteAna().getAutorizzazioni().getServizi().get.setAutorizzazione(diritto.getAutorizzazione());
				utente.getAutorizzazioni().setCodTipoAutor(diritto.getAutorizzazione());
				utente.getBibliopolo().setInizioAuto(DateUtil.formattaData(ts));
				utente.getBibliopolo().setFineAuto(dataFine);

				for (int i = 0; i < utente.getAutorizzazioni().getServizi().size(); i++) {
					ServizioVO ele = utente.getAutorizzazioni().getServizi().get(i);
					ele.setFlag_aut_ereditato(diritto.getAutorizzazione());
					ele.setDataFine(dataFine);
				}
			}
			//valorizzo Chiave Utente Modifica del 27/01/2010 almaviva
			String nome = utente.getCognomeNome();
	        GeneraChiave key = new GeneraChiave();
	        key.estraiChiavi("", nome);
	        utente.setChiaveUte(key.getKy_cles1_A());

			boolean ok;
			if (!recuperoUtenteCancellato)
				ok = factory.getGestioneServizi().insertUtente(ticket, utente);
			else
				ok = factory.getGestioneServizi().importaUtente(navi.getUserTicket(), utente);
			//
			if (ok) {//inserimento utente ok
				//
				int ritEmail = 0;
				/*
				utente = null;
				codfiscale = null;
				//
				mail = anag.getPostaElettronica();
				utente = factory.getGestioneServiziWeb().esistenzaUtenteWeb(codfiscale, mail);
				*/
				//
				UtenteWeb uteWeb = factory.getGestioneServiziWeb().recuperoPassword(utente.getCodiceUtente().trim());
				if (uteWeb == null) {
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.biblioteche.autoregFallita"));
					return (mapping.getInputForward());
				}
				String mail = ServiziUtil.getEmailUtente(uteWeb);

				//almaviva5_20110405 #4341
				String body = MailBodyBuilder.ServiziWeb.autoregistrazioneUtente(uteWeb);

				AmministrazioneMail mailBean = factory.getGestioneAcquisizioni().getAmministrazioneMailBean();

				InternetAddress to = new InternetAddress(mail, utente.getCognomeNome());
				AddressPair pair = mailBean.getMailBiblioteca(polo, currentForm.getCod_biblio() );

				String subject = "Autoregistrazione " + utente.getCodPolo() + utente.getCodBibSer();
				ritEmail = mailBean.inviaMail(pair.getFrom(), to, subject, body.toString());
				if (ritEmail > 0)
					LinkableTagUtils.addError(request, new ActionMessage("error.email.autoreg.failed", utente.getCodiceUtente() ));

				if (ritEmail < 1)
					LinkableTagUtils.addError(request, new ActionMessage("error.authentication.emailpresente"));

				return mapping.findForward("login");

			} else {//errore nell'inserimento utente
				//invio msg: Registrazione fallita riprovare.
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.biblioteche.autoregFallita"));
				return (mapping.getInputForward());
			}

		} catch (Exception e) {
			//errore di autoregistrazione
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.biblioteche.autoregFallita"));
		}

		return (mapping.getInputForward());
	}

	public ActionForward esci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			return mapping.findForward("listaBiblio");

		}catch (Exception e) {

		}

		return (mapping.getInputForward());

	}
	public ActionForward riciclo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;
		currentForm.setPassword("");
		return (mapping.getInputForward());

	}

	private ActionMessages validateAnag(HttpServletRequest request, ActionForm form) {

		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;
		ActionMessages errors = new ActionMessages();

		if (!ValidazioneDati.isFilled(currentForm.getCod_biblio())) {
			errors.add("Biblioteca", new ActionMessage("campo.obbligatorio", "Biblioteca"));
			return errors;
		}
		//
		// inizio controllo dati anagrafici
		//
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		if (!ValidazioneDati.isFilled(utente.getCognome())) {
			errors.add("Cognome", new ActionMessage("campo.obbligatorio", "Cognome"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(utente.getNome())) {
			errors.add("Nome", new ActionMessage("campo.obbligatorio", "Nome"));
			return errors;
		}
		//
		AnagrafeVO anag = utente.getAnagrafe();
		if (!ValidazioneDati.isFilled(anag.getLuogoNascita())) {
			errors.add("Luogo di nascita", new ActionMessage("campo.obbligatorio", "Luogo di nascita"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getSesso())) {
			errors.add("Sesso", new ActionMessage("campo.obbligatorio", "Sesso"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getDataNascita())) {
			errors.add("Data di nascita", new ActionMessage("campo.obbligatorio", "Data di nascita"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getNazionalita())) {
			errors.add("Nazione di nascita", new ActionMessage("campo.obbligatorio", "Nazione di nascita"));
			return errors;
		}
		//
		anag.setCodFiscale(ValidazioneDati.trimOrEmpty(anag.getCodFiscale()).toUpperCase());
		if (!ValidazioneDati.isFilled(anag.getCodFiscale())) {
			errors.add("Codice fiscale", new ActionMessage("campo.obbligatorio", "Codice fiscale"));
			return errors;
		}
		//
		String mail = ServiziUtil.getEmailUtente(utente);
		if (!ValidazioneDati.isFilled(mail)) {
			errors.add("E-mail", new ActionMessage("campo.obbligatorio", "E-mail"));
			return errors;
		}
		// fine controllo dati di residenza
		//
		return errors;
	}
	//

	private ActionMessages validateForm(HttpServletRequest request,
			ActionForm form) throws Exception {

		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;
		ActionMessages errors = new ActionMessages();

		if (!ValidazioneDati.isFilled(currentForm.getCod_biblio())) {
			errors.add("Biblioteca", new ActionMessage("campo.obbligatorio", "Biblioteca"));
			return errors;
		}
		//
		// inizio controllo dati anagrafici
		// LFV manutenzione 20/07/18
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		if (!ValidazioneDati.isFilled(utente.getCognome())) {
			errors.add("Cognome", new ActionMessage("campo.obbligatorio", "Cognome"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(utente.getNome())) {
			errors.add("Nome", new ActionMessage("campo.obbligatorio", "Nome"));
			return errors;
		}
		//
		AnagrafeVO anag = utente.getAnagrafe();
		if (!ValidazioneDati.isFilled(anag.getLuogoNascita())) {
			errors.add("Luogo di nascita", new ActionMessage("campo.obbligatorio", "Luogo di nascita"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getSesso())) {
			errors.add("Sesso", new ActionMessage("campo.obbligatorio", "Sesso"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getDataNascita())) {
			errors.add("Data di nascita", new ActionMessage("campo.obbligatorio", "Data di nascita"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getNazionalita())) {
			errors.add("Nazione di nascita", new ActionMessage("campo.obbligatorio", "Nazione di nascita"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(anag.getCodFiscale())) {
			errors.add("Codice fiscale", new ActionMessage("campo.obbligatorio", "Codice fiscale"));
			return errors;
		}
		//
		// fine controllo dati anagrafici
		//
		// **************************************************************************
		//
		// inizio controllo dati di residenza
		//
		ResidenzaVO residenza = anag.getResidenza();
		String nazionalita = residenza.getNazionalita();
		if (!ValidazioneDati.isFilled(nazionalita)) {
			errors.add("Nazionalita di residenza", new ActionMessage("campo.obbligatorio", "Nazionalita di residenza"));
			return errors;
		}
		// almaviva5_20151120 dati opzionali:
		// se utente estero cap e provincia non sono obbligatori
		boolean isEstero = !nazionalita.equals(CommonConfiguration.getProperty(Configuration.SRV_UTENTE_NAZIONE));

		if (!ValidazioneDati.isFilled(residenza.getVia())) {
			errors.add("Indirizzo di residenza", new ActionMessage("campo.obbligatorio", "Indirizzo di residenza"));
			return errors;
		}
		//
		if (!isEstero && !ValidazioneDati.isFilled(residenza.getCap())) {
			errors.add("Cap di residenza", new ActionMessage("campo.obbligatorio", "Cap di residenza"));
			return errors;
		}
		//
		if (!ValidazioneDati.isFilled(residenza.getCitta())) {
			errors.add("Citta di residenza", new ActionMessage("campo.obbligatorio", "Citta di residenza"));
			return errors;
		}
		//
		if (!isEstero && !ValidazioneDati.isFilled(residenza.getProvincia())) {
			errors.add("Provincia di residenza", new ActionMessage("campo.obbligatorio", "Provincia di residenza"));
			return errors;
		}
		//
		String mail = ServiziUtil.getEmailUtente(utente);
		if (!ValidazioneDati.isFilled(mail)) {
			errors.add("E-mail", new ActionMessage("campo.obbligatorio", "E-mail"));
			return errors;
		}
		// fine controllo dati di residenza
		return errors;
	}
	//
	private ActionMessages controlliFormali(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		//
		InserimentoUtenteWebForm currentForm = (InserimentoUtenteWebForm) form;
		ActionMessages errors = new ActionMessages();
		//
		UtenteBibliotecaVO utente = currentForm.getUteAna();
		AnagrafeVO anag = utente.getAnagrafe();
		if (anag.getCodFiscale().trim().length() >16) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.utenti.campo.codfiscale.errato"));
			return errors;
		}
		//
		if (anag.getCodFiscale().trim().length() <16) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.utenti.campo.codfiscale.errato"));
			return errors;
		}
		//
		String apData = anag.getDataNascita().trim();
		//
		if (apData.length()<10) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.utenti.campo.formato.data.errato"));
			return errors;
		}
		//
		if (apData.length()>10) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.utenti.campo.formato.data.errato"));
			return errors;
		}
		//
		if (!DateUtil.isData(apData)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.utenti.campo.formato.data.errato"));
			return errors;
		}
		//
		return errors;
	}
}
