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
package it.iccu.sbn.web.actions.amministrazionesistema.bibliotecario;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario.NuovoBibliotecarioForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.struts.actions.LookupDispatchAction;

public final class NuovoBibliotecarioAction extends LookupDispatchAction implements SbnAttivitaChecker {

    private static Logger log = Logger.getLogger(NuovoBibliotecarioAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("nuovo.bibliotecario.salva", 			"salva");
		map.put("nuovo.bibliotecario.annulla", 			"annulla");
		map.put("nuovo.bibliotecario.abilitazioni",		"profilo");
		map.put("nuovo.bibliotecario.conferma.si",		"salva");
		map.put("nuovo.bibliotecario.conferma.no",		"noSalva");
		map.put("servizi.bottone.resetPwd",				"password");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		NuovoBibliotecarioForm currentForm = (NuovoBibliotecarioForm) form;
        Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_UTENTE);
            currentForm.setAbilitatoNuovo(false);
        }
        catch (UtenteNotAuthorizedException e) {
            LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));
            return mapping.findForward("blank");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_UTENTE);
            currentForm.setAbilitatoNuovo(true);
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoNuovo(false);
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_UTENTE);
            currentForm.setAbilitatoProfilo(true);
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoProfilo(false);
        }

		String idUtente = (String)request.getAttribute(NavigazioneProfilazione.ID_UTENTE);
		currentForm.setProvenienza((String)request.getAttribute("provenienza"));

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		String bibCorrente = navi.getUtente().getCodBib();
		String poloCorrente = navi.getUtente().getCodPolo();
		String ticket = navi.getUserTicket();
		boolean isCentroSistema = factory.getSistema().isCentroSistema(poloCorrente, bibCorrente);
		log.debug("isCentroSistema: " + isCentroSistema);
		List<ComboVO> elencoBib = new ArrayList<ComboVO>();
		elencoBib.addAll(factory.getSistema()
						.getListaComboBibliotecheAffiliatePerAttivita(
								ticket,
								poloCorrente,
								bibCorrente,
								CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_UTENTE));
		for (int i = 0; i < elencoBib.size(); i++) {
			ComboVO comboEl = elencoBib.get(i);
			comboEl.setDescrizione(comboEl.getCodice() + " " + comboEl.getDescrizione());
		}
		currentForm.setElencoBiblio(elencoBib);
		//almaviva5_20151204 #6053
		String codBib = (String) request.getAttribute(NavigazioneProfilazione.BIBLIOTECA);
		if (ValidazioneDati.isFilled(codBib))
			currentForm.setSelezioneBiblio(codBib);

		List<TB_CODICI> elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_RUOLO_BIBLIOTECARI, false);

		List<ComboVO> elencoRuoli = new ArrayList<ComboVO>();
		for (int i = 1; i < elencoCodici.size(); i++) {
			TB_CODICI codice = elencoCodici.get(i);
			ComboVO comboEl = new ComboVO();
			comboEl.setCodice(codice.getCd_tabella().trim());
			comboEl.setDescrizione(codice.getDs_tabella().trim());
			elencoRuoli.add(comboEl);
		}
		currentForm.setElencoRuoli(elencoRuoli);

		if (idUtente == null || idUtente.equals("")) {
			currentForm.setNuovo(true);
			currentForm.setCheckReset(true);
		}

		else {
			UtenteVO ute = factory.getSistema().caricaBibliotecario(Integer.parseInt(idUtente));
			currentForm.setCognome(ute.getCognome());
			currentForm.setNome(ute.getNome());
			currentForm.setUfficio(ute.getUfficio());
			currentForm.setSelezioneRuolo(ute.getRuolo());
			currentForm.setNote(ute.getNote());
			currentForm.setSelezioneBiblio(ute.getBiblioteca());
			currentForm.setUsername(ute.getUsername());
			currentForm.setUsernameBackup(ute.getUsername());
			currentForm.setId(ute.getId());

			char reset = ute.getChange_password();
			currentForm.setCheckReset((reset == 'S'));

			currentForm.setDataAccesso(DateUtil.formattaDataOra(ute.getTempoAccesso()));
			currentForm.setDataVariazione(DateUtil.formattaDataOra(ute.getTempoVariazione()));

			if (ute.getTempoVariazione() != null) {

				Calendar tsVar = Calendar.getInstance();
				tsVar.setTime(ute.getTempoVariazione());
				int days = factory.getSistema().getDurataPassword();
				tsVar.add(Calendar.DATE, days);
				Calendar now = Calendar.getInstance();

				//almaviva5_20100719 #3751
				currentForm.setScaduto( (reset == 'S') || (days > 0 && tsVar.before(now)) );
			}
			currentForm.setNuovo(false);
			currentForm.setSalvato(true);

			navi.setTesto(".amministrazionesistema.modificaBibliotecario.testo");
			navi.setDescrizioneX(".amministrazionesistema.modificaBibliotecario.descrizione");
		}

		return mapping.getInputForward();
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovoBibliotecarioForm currentForm = (NuovoBibliotecarioForm) form;
		String nome = currentForm.getNome().trim().toLowerCase();

		if (nome.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.nome"));
			return mapping.getInputForward();
		}
		for (int y=0; y < nome.length(); y++) {
			char carattere = nome.charAt(y);
			if (y == 0)
				nome = nome.substring(0, 1).toUpperCase() + nome.substring(y+1, nome.length());
			else if (carattere == ' ') {
				nome = nome.substring(0, y + 1) + nome.substring(y+1, y+2).toUpperCase() + nome.substring(y+2, nome.length());
			}
		}
		String cognome = currentForm.getCognome().trim().toLowerCase();
		if (cognome.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.cognome"));
			return mapping.getInputForward();
		}
		for (int y=0; y < cognome.length(); y++) {
			char carattere = cognome.charAt(y);
			if (y == 0)
				cognome = cognome.substring(0, 1).toUpperCase() + cognome.substring(y+1, cognome.length());
			else if (carattere == ' ') {
				cognome = cognome.substring(0, y + 1) + cognome.substring(y+1, y+2).toUpperCase() + cognome.substring(y+2, cognome.length());
			}
		}

		String username = currentForm.getUsername().toLowerCase();
		boolean abilita = true;
		boolean salvato = currentForm.isSalvato();
		boolean nuovo = currentForm.isNuovo();
		boolean conferma = currentForm.isConferma();

		if (username.trim().equals("")) {
			abilita = false;
			if (salvato && !conferma && !nuovo && !currentForm.getUsernameBackup().equals("")) {
				LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.username.conferma"));
				currentForm.setConferma(true);
				return mapping.getInputForward();
			}
			username = currentForm.getUsernameBackup();
		}
		if (username.contains(" ")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.username.spazio"));
			return mapping.getInputForward();
		}
		if (salvato && !nuovo && !currentForm.getUsernameBackup().equals("") && !username.equals(currentForm.getUsernameBackup())) {
			LinkableTagUtils.addError(request, new ActionMessage("modifica.bibliotecario.username.diversa"));
			currentForm.setUsername(currentForm.getUsernameBackup());
			return mapping.getInputForward();
		}
		String biblioteca = currentForm.getSelezioneBiblio();
		if (biblioteca == null || biblioteca.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.nobiblioteca"));
			currentForm.setUsername(currentForm.getUsernameBackup());
			return mapping.getInputForward();
		}

		//Imposto la password criptata identica allo username:
		boolean checkReset = currentForm.isCheckReset();
		String ruolo = currentForm.getSelezioneRuolo();
		String ufficio = currentForm.getUfficio().trim().toUpperCase();
		String note = currentForm.getNote().trim();
		if (note.length() > 160) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.note"));
			return mapping.getInputForward();
		}
		if (nuovo)
			checkReset = true;
		char reset = 'N';
		if (checkReset)
			reset = 'S';

		int utenteInseritore = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		UtenteVO bibliotecario = new UtenteVO();

		PasswordEncrypter enc = new PasswordEncrypter(username);
		String password = enc.encrypt(username);
		bibliotecario.setPassword(password);

		bibliotecario.setNome(nome);
		bibliotecario.setCognome(cognome);
		bibliotecario.setUsername(username);
		bibliotecario.setBiblioteca(biblioteca);
		bibliotecario.setRuolo(ruolo);
		bibliotecario.setNote(note);
		bibliotecario.setUfficio(ufficio);
		bibliotecario.setChange_password(reset);
		UtenteVO ute = new UtenteVO();
		if (!salvato)
			ute = factory.getSistema().creaBibliotecario(bibliotecario, utenteInseritore, false, abilita);
		else {
			bibliotecario.setId(currentForm.getId());
			ute = factory.getSistema().creaBibliotecario(bibliotecario, utenteInseritore, true, abilita);
		}
		if (ute.getInserito() == 1) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.bibliotecario"));
			return mapping.getInputForward();
		}
		if (ute.getInserito() == 2) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.bibliotecario.esiste"));
			return mapping.getInputForward();
		}
		if (ute.getInserito() == 3) {
			LinkableTagUtils.addError(request, new ActionMessage("modifica.bibliotecario.ok"));
		}
		else {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.ok"));
		}
		currentForm.setDataAccesso(DateUtil.formattaDataOra(ute.getTempoAccesso()));
		currentForm.setDataVariazione(DateUtil.formattaDataOra(ute.getTempoVariazione()));

		if (ute.getTempoVariazione() != null) {

			Calendar tsVar = Calendar.getInstance();
			tsVar.setTime(bibliotecario.getTempoVariazione());
			int days = factory.getSistema().getDurataPassword();
			tsVar.add(Calendar.DATE, days);
			Calendar now = Calendar.getInstance();
			//almaviva5_20100719 #3751
			currentForm.setScaduto( (ute.getChange_password() == 'S') || (days > 0 && tsVar.before(now)) );
		}
		currentForm.setCheckReset((ute.getChange_password() == 'S'));
		currentForm.setSalvato(true);
		currentForm.setNuovo(false);
		currentForm.setConferma(false);
		currentForm.setUsernameBackup(ute.getUsername());
		currentForm.setId(ute.getId());

		//almaviva5_20111116 pulizia profilo
		SbnWebProfileCache.getInstance().clear(ValidazioneDati.asSingletonList(username));

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovoBibliotecarioForm myForm = (NuovoBibliotecarioForm) form;
		String provenienza = myForm.getProvenienza();
		if (provenienza == null || provenienza.equals("ricerca"))
			return mapping.findForward("torna");
		if (provenienza.equals("sintetica"))
			return mapping.findForward("sintetica");

		return mapping.getInputForward();
	}

	public ActionForward profilo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovoBibliotecarioForm currentForm = (NuovoBibliotecarioForm) form;

		String nome = currentForm.getNome().trim().toLowerCase();
		if (nome.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.nome"));
			return mapping.getInputForward();
		}
		for (int y=0; y < nome.length(); y++) {
			char carattere = nome.charAt(y);
			if (y == 0)
				nome = nome.substring(0, 1).toUpperCase() + nome.substring(y+1, nome.length());
			else if (carattere == ' ') {
				nome = nome.substring(0, y + 1) + nome.substring(y+1, y+2).toUpperCase() + nome.substring(y+2, nome.length());
			}
		}
		String cognome = currentForm.getCognome().trim().toLowerCase();
		if (cognome.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.cognome"));
			return mapping.getInputForward();
		}
		for (int y=0; y < cognome.length(); y++) {
			char carattere = cognome.charAt(y);
			if (y == 0)
				cognome = cognome.substring(0, 1).toUpperCase() + cognome.substring(y+1, cognome.length());
			else if (carattere == ' ') {
				cognome = cognome.substring(0, y + 1) + cognome.substring(y+1, y+2).toUpperCase() + cognome.substring(y+2, cognome.length());
			}
		}
		String username = currentForm.getUsername().toLowerCase();
		boolean abilita = true;
		if (username.trim().equals("")) {
			abilita = false;
			if (currentForm.isSalvato() && !currentForm.isConferma() && !currentForm.isNuovo() && !currentForm.getUsernameBackup().equals("")) {
				LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.username.conferma"));
				currentForm.setConferma(true);
				return mapping.getInputForward();
			}
			username = currentForm.getUsernameBackup();
		}
		if (username.contains(" ")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.username.spazio"));
			return mapping.getInputForward();
		}
		if (currentForm.isSalvato() && !currentForm.isNuovo() && !currentForm.getUsernameBackup().equals("") && !username.equals(currentForm.getUsernameBackup())) {
			LinkableTagUtils.addError(request, new ActionMessage("modifica.bibliotecario.username.diversa"));
			currentForm.setUsername(currentForm.getUsernameBackup());
			return mapping.getInputForward();
		}
		String biblioteca = currentForm.getSelezioneBiblio();
		if (biblioteca == null || biblioteca.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.nobiblioteca"));
			currentForm.setUsername(currentForm.getUsernameBackup());
			return mapping.getInputForward();
		}

		//Imposto la password criptata identica allo username:
		PasswordEncrypter enc = new PasswordEncrypter(username);
		String password = enc.encrypt(username);
		String ruolo = currentForm.getSelezioneRuolo();
		String ufficio = currentForm.getUfficio().trim().toUpperCase();
		String note = currentForm.getNote().trim();
		if (note.length() > 160) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.note"));
			return mapping.getInputForward();
		}
		boolean checkReset = currentForm.isCheckReset();
		if (currentForm.isNuovo())
			checkReset = true;
		char reset = 'N';
		if (checkReset)
			reset = 'S';

		int utenteInseritore = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		UtenteVO bibliotecario = new UtenteVO();
		bibliotecario.setNome(nome);
		bibliotecario.setCognome(cognome);
		bibliotecario.setUsername(username);
		bibliotecario.setBiblioteca(biblioteca);
		bibliotecario.setPassword(password);
		bibliotecario.setRuolo(ruolo);
		bibliotecario.setNote(note);
		bibliotecario.setUfficio(ufficio);
		bibliotecario.setChange_password(reset);
		UtenteVO bibliotecarioReturn = new UtenteVO();
		if (!currentForm.isSalvato())
			bibliotecarioReturn = factory.getSistema().creaBibliotecario(bibliotecario, utenteInseritore, false, abilita);
		else {
			bibliotecario.setId(currentForm.getId());
			bibliotecarioReturn = factory.getSistema().creaBibliotecario(bibliotecario, utenteInseritore, true, abilita);
		}
		if (bibliotecarioReturn.getInserito() == 1) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.bibliotecario"));
			return mapping.getInputForward();
		}
		if (bibliotecarioReturn.getInserito() == 2) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.bibliotecario.esiste"));
			return mapping.getInputForward();
		}
		if (bibliotecarioReturn.getInserito() == 3 && abilita) {
			LinkableTagUtils.addError(request, new ActionMessage("modifica.bibliotecario.ok"));
		}
		else {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.ok"));
		}

		if (bibliotecarioReturn.getChange_password() == 'S')
			checkReset = true;
		else
			checkReset = false;
		currentForm.setCheckReset(checkReset);
		currentForm.setSalvato(true);
		currentForm.setNuovo(false);
		currentForm.setConferma(false);
		currentForm.setId(bibliotecarioReturn.getId());

		if (abilita) {
			request.setAttribute(NavigazioneProfilazione.ID_UTENTE, currentForm.getId() + "");
			request.setAttribute("provenienza", "nuovo");
			return mapping.findForward("profilo");
		}

		LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.abilita.ok"));
		return mapping.getInputForward();
	}

	public ActionForward noSalva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovoBibliotecarioForm currentForm = (NuovoBibliotecarioForm) form;
		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward password(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovoBibliotecarioForm currentForm = (NuovoBibliotecarioForm) form;
		currentForm.setCheckReset(true);
		return salva(mapping, currentForm, request, response);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		NuovoBibliotecarioForm currentForm = (NuovoBibliotecarioForm) form;
		//almaviva5_20100719 #3751
		if (idCheck.equals("PASSWORD")) {
			if (currentForm.isNuovo())
				return false;
			return ValidazioneDati.isFilled(currentForm.getDataVariazione());
		}
		return true;
	}

}
