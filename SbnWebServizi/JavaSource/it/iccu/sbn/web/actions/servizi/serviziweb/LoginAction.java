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
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO.TipoRichiesta;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Constants.Servizi;
import it.iccu.sbn.util.mail.MailBodyBuilder;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.LoginForm;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.Ticket;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.LogoutDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.ServiziWebDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LoginAction extends ServiziModuloWebAction implements SbnAttivitaChecker {

	private static final String AUTOREG_REGEX = "(?is)(<autoreg>)(.*)(</autoreg>)";
	private static final String AUTOREG_LINK_REGEX = "(?i)<autoreglink>";

	private static Logger log = Logger.getLogger(LoginAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.login", "login");
		map.put("button.autoregistrazione", "autoregistrazione");
		map.put("button.recuperopassword",  "recuperoPassword");
		map.put("button.cambiopassword",  "cambioPassword");
		map.put("button.home",  "home");

		//almaviva5_20110429 #4337
		map.put("servizi.bottone.opac.iscrizione", "iscrizione");

		//almaviva5_20171006 servizi ill
		map.put("servizi.bottone.scegli", "nuovaRichiestaILL");
		map.put("button.continua", "continua");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		LoginForm currentForm = (LoginForm) form;

		if (!currentForm.isInitialized()) {
			//almaviva5_20110420 segnalazione MIL: login multiplo da opac
			navi.makeFirst();
			preparaWelcomeMsg(request, response, form);
			currentForm.setInitialized(true);
		}

		if (!populateOpacRequest(request, form)) {
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.InsRichOpac.inventarioErrato"));
			LogoutDelegate.logout(request);
			return mapping.getInputForward();
		}

		if (request.getParameter(Constants.AUTOREGISTRAZIONE) != null)
			return autoregistrazione(mapping, currentForm, request, response);

		return mapping.getInputForward();
	}

	private boolean populateOpacRequest(HttpServletRequest request,	ActionForm form) {

		LoginForm currentForm = (LoginForm) form;

		request.getSession().removeAttribute(Constants.RICHIESTA_OPAC);

		String bibRichiesta = request.getParameter(Constants.BIBLIOTECA);
		if (!ValidazioneDati.isFilled(bibRichiesta))
			return true;	//senza richiesta opac

		InventarioVO inv = null;
		String chiaveInv = ValidazioneDati.rtrim(request.getParameter(Constants.INVENTARIO));
		try {
			inv = InventarioRFIDParser.parse(chiaveInv);
			if (inv != null) {
				inv.setCodBib(bibRichiesta);
				log.debug("inventario opac: " + inv);
			}
		} catch (Exception e) {
			return false;
		}

		RichiestaOpacVO richiesta = new RichiestaOpacVO();
		currentForm.setRichiesta(richiesta);

		if (inv != null) {
			richiesta.setTipo(TipoRichiesta.INVENTARIO);
			richiesta.setInventario(inv);
			richiesta.setCodBibOpac(bibRichiesta);
			richiesta.setCodSerieOpac(inv.getCodSerie());
			richiesta.setCodInventOpac(inv.getCodInvent());
			richiesta.setAnno(request.getParameter(Constants.ANNO));
			richiesta.setTitolo(request.getParameter(Constants.TITOLO));
			richiesta.setAutore(request.getParameter(Constants.AUTORE));
			richiesta.setNatura(request.getParameter(Constants.NATURA));
		}

		//prenotazione posto
		String sala = request.getParameter(Constants.SALA);
		String tipoMed = request.getParameter(Constants.CAT_MEDIAZIONE);
		if (ValidazioneDati.isFilled(sala) && ValidazioneDati.isFilled(tipoMed) ) {
			richiesta.setCodBibOpac(bibRichiesta);
			richiesta.setTipo(TipoRichiesta.SALA);
			richiesta.setTipoMediazione(tipoMed);
		}

		try {
			BibliotecaVO bibInv = BibliotecaDelegate.getInstance(request)
					.getBiblioteca(Navigation.getInstance(request).getPolo(), richiesta.getCodBibOpac());
			richiesta.setDenBibOpac(bibInv.getNom_biblioteca());
		} catch (Exception e) {
			log.error("", e);
		}

		return true;
	}


	private void preparaWelcomeMsg(HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
		//almaviva5_20110120 messaggio benvenuto personalizzato
		LoginForm currentForm = (LoginForm) form;

		//biblioteche che ammettono autoregistrazione
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		String polo = Navigation.getInstance(request).getPolo();
		List<?> listaBib = factory.getGestioneServiziWeb().getListaBibAutoregistrazione(polo);
		boolean autoreg = (ValidazioneDati.isFilled(listaBib));

		//messaggio default
		String welcome = LinkableTagUtils.findMessage(request, Locale.getDefault(), "servizi.welcome");
		currentForm.setWelcome(welcome);

		List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI);
		if (ValidazioneDati.size(modelli) == 1) // solo una descrizione gestita
			welcome = modelli.get(0).getJrxml();

		//almaviva5_20110304 #4280 la frase relativa dell'autoregistrazione viene nascosta se non esistono
		//biblioteche disponibili all'operazione
		Pattern p = Pattern.compile(AUTOREG_REGEX);
		Matcher m = p.matcher(welcome);
		//il 2° gruppo, se trovato, contiene il testo compreso tra i tag <autoreg>
		String phrase = m.find() ? m.group(2) : null;
		if (ValidazioneDati.isFilled(phrase)) {

			String autoRegMsg = LinkableTagUtils.findMessage(request, Locale.getDefault(), "button.autoregistrazione");
			String autoRegUrl = "<a href=\"" + request.getContextPath() + response.encodeURL("/login.do?AUTOREG=Y") + "\">" + autoRegMsg + "</a>";
			//inserisco url calcolato, oppure elimino tutta la frase
			phrase = autoreg ? phrase.replaceAll(AUTOREG_LINK_REGEX, autoRegUrl) : "";
			welcome = m.replaceAll(phrase);
		}

		currentForm.setWelcome(welcome);
	}

	public ActionForward iscrizione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		LoginForm currentForm = (LoginForm) form;

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

		HttpSession session = request.getSession();
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		String polo = navi.getPolo();

		try {
			try {
				//iscrizione ad una nuova bib. del polo
				//se ha selezionato una bib. procedo con l'iscrizione
				BibliotecaVO firstBib = utente.getListaBiblio().get(0);
				String ticket = Ticket.getUtenteWebTicket(polo, firstBib.getCod_bib(), navi.getUserAddress());

				RicercaUtenteBibliotecaVO recUte = new RicercaUtenteBibliotecaVO();

				recUte.setIdUte(firstBib.getId_utenti_biblioteca());

				UtenteBibliotecaVO dettaglio = factory.getGestioneServizi().getDettaglioUtente(ticket, recUte, null, null);
				//BibliotecaVO bibSel = (BibliotecaVO) currentForm.getListaBiblioAuto().get(currentForm.getBiblioAuto().getPrg());

				dettaglio.setCodiceBiblioteca(firstBib.getCod_bib());
				dettaglio.setCodBibSer(currentForm.getRichiesta().getCodBibOpac()); //bib. che possiede il doc.

				Date now = new Date();
				String dataInizio = DateUtil.formattaData(now);
				String dataFine = "31/12/" + DateUtil.getYear(now);
				dettaglio.getBibliopolo().setInizioAuto(dataInizio);
				dettaglio.getBibliopolo().setFineAuto(dataFine);

				List<ServizioVO> diritti = factory.getGestioneServiziWeb().getListaServiziAutorizzazione(ticket, dettaglio);

				//almaviva5_20110127 #4178
				if (ValidazioneDati.isFilled(diritti)) {
					ServizioVO diritto = diritti.get(0);
					AutorizzazioniVO autorizzazioni = dettaglio.getAutorizzazioni();
					autorizzazioni.setServizi(diritti);
					autorizzazioni.setCodTipoAutor(diritto.getAutorizzazione());

					for (ServizioVO srv : diritti) {
						srv.setFlag_aut_ereditato(diritto.getAutorizzazione());
						srv.setDataInizio(dataInizio);
						srv.setDataFine(dataFine);
					}
				}

				boolean insUteWeb = factory.getGestioneServizi().importaUtente(ticket, dettaglio);
				//se non è mai stato iscritto alla bib. inserisco
				if (insUteWeb) {
					//Invio msg. "Iscrizione effettuata con successo"
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.iscrizioneOk"));
					//almaviva5_20110502 fix per menu già caricato per altra biblioteca
					session.removeAttribute(Constants.USER_MENU);
					session.setAttribute(Constants.COD_BIBLIO, currentForm.getRichiesta().getCodBibOpac());
					//
					//init(currentForm);
					return preparaRichiestaOpac(request, utente, form, mapping);

				} else {
					//invio msg: Iscrizione fallita riprovare.
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.aiscrizioneNotOk"));
					return (mapping.getInputForward());
				}

			} catch (Exception e) {
				//errore di Iscrizione
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.aiscrizioneNotOk"));
				return (mapping.getInputForward());
			}
		} finally {
			init(currentForm);
		}

	}

	public ActionForward login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LoginForm currentForm = (LoginForm) form;
		HttpSession session = request.getSession();
		Navigation navi = Navigation.getInstance(request);

		//currentForm.setUserName(ServiziUtil.espandiCodUtente(currentForm.getUserName()));
		String polo = navi.getPolo();
		String username = currentForm.getUserName();
		//LFV 19/07/18 viene impostato il testo a maiuscolo. logica errata.  Es. Esse3 ha userId minuscoli
		//ServiziUtil.espandiCodUtente(currentForm.getUserName());
		String password = currentForm.getPassword();
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			log.debug("tentativo login web: " + username);

			UtenteWeb uteWeb = factory.getGestioneServiziWeb().login(navi.getUserTicket(), username, password, null, navi.getUserAddress());

			session.setAttribute(Constants.UTENTE_WEB_KEY, uteWeb);
			session.setAttribute(Constants.UTENTE_CON, uteWeb.getUserId());
			session.setAttribute(Constants.PASSWORD, password);

			//Scadenza delle credenziali

			//È previsto un controllo sulla data di ultimo utilizzo della userID e il suo annullamento
			//nel caso in cui non venga utilizzata per il n.ro di giorni solari definiti.
			//Il default di applicazione (definito nel software) è 180 giorni. Per modificare tale default
			//è necessario inserire nel DB un apposito record nella tabella tbf_contatore
			//(chiave di identificazione cd_polo, cd_biblioteca, cd_cont=’USL’) e valorizzare
			//opportunamente il campo lim_max.
			//Per evitare il controllo sulla data di ultimo utilizzo della userID e rendere quindi
			//indefinita la durata della userID, si deve inserire il record con cd_cont= ‘USL’  è impostare
			//lim_max=0.
			//private boolean remoto = false;

			//inizio controllo scadenza userid
			long ts = System.currentTimeMillis();

			Calendar now = Calendar.getInstance();
			now.setTimeInMillis(ts);
			int gg_max_ute = factory.getGestioneServiziWeb().getLimMax(polo);

			Calendar lastAccess = Calendar.getInstance();
			lastAccess.setTimeInMillis(uteWeb.getLastAccess().getTime());

			if (gg_max_ute == 0)
				lastAccess = now;
			else
				lastAccess.add(Calendar.DAY_OF_MONTH, gg_max_ute);

			if (now.after(lastAccess) ) {
				//Utente scaduto: per la riattivazione dovrà recarsi presso la biblioteca..
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.utenteScaduto"));
				LogoutDelegate.logout(request);
				return mapping.getInputForward();
			}
			//fine controllo scadenza userid

			} catch (UtenteNotFoundException ute) {
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.failed", ute.getMessage()));
				log.error("Login fallito per " + username +": " + ute.getMessage() );
				return mapping.getInputForward();

			} catch (DefaultNotFoundException ute) {
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.failed", ute.getMessage()));
				log.error("Login fallito per " + username +": " + ute.getMessage() );
				return mapping.getInputForward();

			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.failed", e.getMessage()));
				return mapping.getInputForward();
			}

		return continua(mapping, currentForm, request, response);
	}


	private ActionForward preparaRichiestaOpac(HttpServletRequest request, UtenteWeb uteWeb, ActionForm form, ActionMapping mapping) throws Exception {

		LoginForm currentForm = (LoginForm) form;
		Navigation navi = Navigation.getInstance(request);
		//ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		//CONTROLLO SE L'UTENTE è ISCRITTO ALLA BIB. EROGANTE DEL SERVIZIO(SCELTA DA OPAC)
		List<BibliotecaVO> listaBiblio = factory.getGestioneServiziWeb()
				.controlloBibRicOpac(uteWeb.getCodPolo(), uteWeb.getIdUtente(),	currentForm.getRichiesta().getCodBibOpac());

		//almaviva5_20110429 #4337 se l'utente non é iscritto alla bib. del documento
		// si verifica la possibilita di autoregistrazione.
		if (!ValidazioneDati.isFilled(listaBiblio) ) {
			//almaviva5_20171006 servizi ill
			ActionForward forward = preparaRichiestaILLRichiedente(request, mapping, uteWeb, currentForm);
			if (forward != null)
				return forward;

			if (isPossibileAutoregistrazione(request, uteWeb.getCodPolo(), currentForm.getRichiesta().getCodBibOpac(), uteWeb.getIdUtente())) {
				log.debug(currentForm.getRichiesta().getCodBibOpac() + ": autoreg possibile");
				LinkableTagUtils.addError(request, new ActionMessage("servizi.utenti.richiesta.opac.iscrizione"));
				currentForm.setIscrizione(true);
				return mapping.getInputForward();
			}
		}

		if (ValidazioneDati.isFilled(listaBiblio) ) {
			//se è iscritto alla bib. erogante, valorizzo il Vo
			RichiestaOpacVO richiesta = currentForm.getRichiesta();
			richiesta.setBibIscr(uteWeb.getCodBib());
			richiesta.setListaBiblioOpac(listaBiblio);

			//estraggo la denominazione della bib. per il documento richiesto dall'utente
			currentForm.setListaBiblio(listaBiblio);
			Integer idUteBib = null;

			if (ValidazioneDati.isFilled(currentForm.getListaBiblio()) ) {

				for (int i = 0; i < currentForm.getListaBiblio().size(); i++) {
					BibliotecaVO nombib = currentForm.getListaBiblio().get(i);
					if (ValidazioneDati.equals(currentForm.getRichiesta().getCodBibOpac(), nombib.getCod_bib())) {
						richiesta.setDenBibOpac(nombib.getNom_biblioteca());
						idUteBib = nombib.getId_utenti_biblioteca();
					}
				}
			}

			HttpSession session = request.getSession();
			session.setAttribute(Constants.ID_UTE_BIB, idUteBib);
			session.setAttribute(Constants.BIBLIO_SEL, richiesta.getDenBibOpac());
			session.setAttribute(Constants.POLO, uteWeb.getCodPolo());
			session.setAttribute(Constants.COD_BIBLIO, richiesta.getCodBibOpac());

			session.setAttribute(Constants.RICHIESTA_OPAC, richiesta);

			List<ServizioBibliotecaVO> lstDirittiUtente = null;
			//lista servizi attivi per utente
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			lstDirittiUtente = delegate.getServiziAttivi(uteWeb.getCodPolo(), uteWeb.getCodBib(), uteWeb.getUserId(), currentForm.getRichiesta().getCodBibOpac(),
				DaoManager.now() );
			//controllare
			int size = ValidazioneDati.size(lstDirittiUtente);
			if (size > 1) {
				//almaviva5_20101228 #4035
				session.setAttribute(Constants.BIBLIO, factory.getSistema().getBiblioteca(uteWeb.getCodPolo(), currentForm.getRichiesta().getCodBibOpac()));
				uteWeb.setRemoto(factory.getGestioneServiziWeb().setRemote(uteWeb, uteWeb.getCodPolo(), currentForm.getRichiesta().getCodBibOpac(), navi.getUserAddress()));

				//se il flag changed_password = S procedo al
				//cambio password prospettando la maschera opportuna
				if (!checkPasswordIsValid(request, uteWeb))
					return navi.goForward(mapping.findForward("cambioPwd"));

				//fare incrocio utente documento
				return super.selectNextForwardRichiesta(request, mapping, uteWeb, richiesta);

			} else {
				//Non sono presenti servizi per il doc. richiesto
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.serviziDispDocOpac"));
				LogoutDelegate.logout(request);
				this.init(currentForm);//modifica almaviva del 15/03/2010
				return mapping.getInputForward();
			}
		} else {
			//se non è iscritto alla bib. erogante invio msg:
			//non risulta iscrizione alla bib. erogante del servizio
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.InsRichOpac"));
			LogoutDelegate.logout(request);
			this.init(currentForm); //modifica almaviva del 15/03/2010
			return mapping.getInputForward();
		}//fine gestione inserimento rich. Opac
	}


	private boolean isPossibileAutoregistrazione(HttpServletRequest request, String codPolo,
			String codBib, Integer idUtente) throws Exception {

		ServiziWebDelegate delegate = ServiziWebDelegate.getInstance(request);

		List<BibliotecaVO> lstAutoReg = delegate.getListaAltreBibPerAutoregistrazione(codPolo, idUtente);

		if (!ValidazioneDati.isFilled(lstAutoReg))
			return false;

		//controllo che la bib. del documento sia tra quelle che ammettono
		//autoregistrazione
		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(codPolo);
		bib.setCod_bib(codBib);

		return (UniqueIdentifiableVO.searchRepeatableId(bib.getRepeatableId(), lstAutoReg) != null);

	}

	private boolean checkPasswordIsValid(HttpServletRequest request, UtenteWeb uteWeb) {
		Navigation navi = Navigation.getInstance(request);
		if (!uteWeb.isSIP2()
				&& ValidazioneDati.equals(uteWeb.getChangePassword(), 'S') ) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.passwordScaduta"));
			navi.purgeThis();
			return false;
		}
		return true;
	}


	public ActionForward recuperoPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LoginForm currentForm = (LoginForm) form;

		//leggo il campo digitato dall' utente
		//currentForm.setUserName(ServiziUtil.espandiCodUtente(currentForm.getUserName()));
		String username = currentForm.getUserName();

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

		int ritEmail = 0;

		try {
			UtenteWeb uteWeb = factory.getGestioneServiziWeb().recuperoPassword(username);

			if (uteWeb == null) {
				//non risulta iscritto a nessuna biblioteca del polo” .
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.utentenonpresente"));
				return mapping.getInputForward();
			}

			if (uteWeb.isScaduto()) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.utenteScaduto"));
				return mapping.getInputForward();
			}

			String mail = ServiziUtil.getEmailUtente(uteWeb);
			if (!ValidazioneDati.isFilled(mail) ) {
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.emailnonpresente"));
				return mapping.getInputForward();
			}

			if (!ValidazioneDati.equals(uteWeb.getTipoUtente(), Servizi.Utenti.UTENTE_TIPO_SBNWEB) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.tipoUtente.operazione.non.gestita"));
				return mapping.getInputForward();
			}

			String body = MailBodyBuilder.ServiziWeb.passwordTemporanea(uteWeb);

			String subject = uteWeb.getCodPolo() + " Password temporanea";
			AmministrazioneMail mailBean = factory.getGestioneAcquisizioni().getAmministrazioneMailBean();
			ritEmail = mailBean.inviaMail(null, new InternetAddress(mail, uteWeb.getCognomeNome()), subject, body);

			//testo "ritEmail" se=0 e-mail inviata con successo altrimenti errore
			if (ritEmail > 0) {
				//errore invio e-mail
				LinkableTagUtils.addError(request, new ActionMessage("error.email.failed"));
			}
			if (ritEmail < 1) {
				//e-mail inviata correttamente
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.emailpresente"));
			}



		} catch (Exception ute) {
			//errore di invio e-mail
			LinkableTagUtils.addError(request, new ActionMessage("error.email.failed"));
			log.error("Utente non iscritto " + username +": " + ute.getMessage() );
		}
		return (mapping.getInputForward());
	}

	public ActionForward autoregistrazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String polo = Navigation.getInstance(request).getPolo();

			//listaBiblio listbib;lista delle biblioteche che
			//ammettono l'autoregistrazione da web.
			ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
			List<BibliotecaVO> listaBib = factory.getGestioneServiziWeb().getListaBibAutoregistrazione(polo);

			if (ValidazioneDati.isFilled(listaBib) ) //se ci sono biblioteche
				return mapping.findForward("listaBiblio");

			else { //altrimenti se non trovo biblioteche
				//invio msg:"non sono presenti biblioteche che ammettono
				//autoregistrazione da web pertanto recarsi presso la biblioteca più vicina"
				//
				LinkableTagUtils.addError(request, new ActionMessage("error.biblioteche.autoregistrazione"));
				return mapping.getInputForward();
			}

		} catch (Exception e) {
			log.error("", e);
		}

		return mapping.getInputForward();
	}

	public ActionForward cambioPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LoginForm currentForm = (LoginForm) form;

		//currentForm.setUserName(ServiziUtil.espandiCodUtente(currentForm.getUserName()));
		String username = currentForm.getUserName();
		String password = currentForm.getPassword();
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

		try {
			UtenteWeb uteWeb = null;

			log.debug("tentativo cambio password web: " + username);

			Navigation navi = Navigation.getInstance(request);
			uteWeb = factory.getGestioneServiziWeb().login(navi.getUserTicket(), username, password, null, navi.getUserAddress());

			if (uteWeb == null) {
				//non risulta iscritto a nessuna biblioteca del polo” .
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.utentenonpresente"));
				return mapping.getInputForward();
			}

			if (uteWeb.isScaduto()) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.utenteScaduto"));
				return mapping.getInputForward();
			}

			if (!ValidazioneDati.equals(uteWeb.getTipoUtente(), Servizi.Utenti.UTENTE_TIPO_SBNWEB) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.tipoUtente.operazione.non.gestita"));
				return mapping.getInputForward();
			}

			HttpSession session = request.getSession();
			session.setAttribute(Constants.UTENTE_WEB_KEY, uteWeb);
			session.setAttribute(Constants.UTENTE_CON, username);
			session.setAttribute(Constants.PASSWORD, password);

			if (ValidazioneDati.isFilled(uteWeb.getListaBiblio()) ) {

				if (ValidazioneDati.isFilled(currentForm.getRichiesta().getCodBibOpac()) ) {
					uteWeb.setChangePassword('S');
					//Gestione Inserimento richiesta da opac
					ActionForward forward = preparaRichiestaOpac(request, uteWeb, currentForm, mapping);
					if (forward != null) {
						//pagina eliminata da nav solo se il forward è su altra pagina
						navi.purgeThisIf(forward);
						return forward;
					}
				}

				return mapping.findForward("cambioPwd");
			}

		} catch (UtenteNotFoundException ute) {
			LinkableTagUtils.addError(request, new ActionMessage("error.authentication.failed", ute.getMessage()));
			log.error("Login fallito per " + username +": " + ute.getMessage() );

		} catch (DefaultNotFoundException ute) {
			ute.printStackTrace();
		}
		return (mapping.getInputForward());
	}

	public ActionForward home(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm currentForm = (LoginForm) form;

		LogoutDelegate.clear(request);

		this.init(currentForm);
		log.debug("home");

		return unspecified(mapping, currentForm, request, response);
	}

	public ActionForward nuovaRichiestaILL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm currentForm = (LoginForm) form;

		HttpSession session = request.getSession();
		UtenteWeb uteWeb = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);

		BibliotecaVO bibSelezionata = BibliotecaDelegate.getInstance(request).getBiblioteca(uteWeb.getCodPolo(),
				currentForm.getBib().getCod_bib());

		//session.setAttribute(Constants.ID_UTE_BIB, idUteBib);
		session.setAttribute(Constants.BIBLIO_SEL, bibSelezionata.getNom_biblioteca());
		session.setAttribute(Constants.POLO, uteWeb.getCodPolo());
		session.setAttribute(Constants.COD_BIBLIO, bibSelezionata.getCod_bib() );

		session.setAttribute(Constants.RICHIESTA_OPAC, currentForm.getRichiesta() );

		Navigation.getInstance(request).purgeThis();
		return mapping.findForward("listaMovimentiOpac");
	}

	public ActionForward continua(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//Se all’atto del login il sistema determina che la userID è scaduta invia un msg all’utente.
		//Per la riattivazione l’utente dovrà recarsi in biblioteca; il bibliotecario potrà riattivarla
		//utilizzando la reset della password.

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		LoginForm currentForm = (LoginForm) form;
		HttpSession session = request.getSession();
		UtenteWeb uteWeb = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);

		if (ValidazioneDati.isFilled(uteWeb.getListaBiblio()) ) {
			//se la biblioteca è già impostata da url si attiva l'inserimento richiesta
			if (ValidazioneDati.isFilled(currentForm.getRichiesta().getCodBibOpac()) ) {
				//Gestione Inserimento richiesta da opac
				ActionForward forward = preparaRichiestaOpac(request, uteWeb, currentForm, mapping);
				if (forward != null) {
					//pagina eliminata da nav solo se il forward è su altra pagina
					navi.purgeThisIf(forward);
					return forward;
				}
			}

			navi.purgeThis();

			//se il flag changed_password = S procedo al
			//cambio password prospettando la maschera opportuna
			if (!checkPasswordIsValid(request, uteWeb))
				return navi.goForward(mapping.findForward("cambioPwd"));

			return navi.goForward(mapping.findForward("selezioneBiblioteca"));
		}

		return mapping.getInputForward();
	}

	private ActionForward preparaRichiestaILLRichiedente(HttpServletRequest request, ActionMapping mapping,
			UtenteWeb uteWeb, LoginForm currentForm) {
		try {
			RichiestaOpacVO richiesta = currentForm.getRichiesta();
			if (!ValidazioneDati.in(richiesta.getTipo(),
					TipoRichiesta.INVENTARIO,
					TipoRichiesta.RICHIESTA_ILL) )
				return null;

			BibliotecaVO bibInv = BibliotecaDelegate.getInstance(request).getBiblioteca(uteWeb.getCodPolo(),
					richiesta.getCodBibOpac());
			if (bibInv == null)
				return null;

			//1. verifica se la bib. che possiede l'inventario accetta autoregistrazione
			boolean hasAutoreg = isPossibileAutoregistrazione(request, bibInv.getCod_polo(), bibInv.getCod_bib(), uteWeb.getIdUtente());

			//2. verifica se la bib aderisce a ILL come fornitrice
			final List<BibliotecaVO> biblioteche = ServiziILLDelegate.getInstance(request).getBibliotechePoloILL(bibInv.getCod_polo());
			bibInv = UniqueIdentifiableVO.searchRepeatableId(bibInv.getRepeatableId(), biblioteche);
			if (bibInv == null || !bibInv.isILLFornitrice())
				return null;	//la biblioteca non aderisce a ILL

			//3. ricava lista di altre bib a cui l'utente è iscritto e aderiscono a ILL come richiedenti
			List<BibliotecaVO> richiedenti = ServiziWebDelegate.getInstance(request).getListaBibliotecheRichiedentiILL(uteWeb, biblioteche);

			if (!ValidazioneDati.isFilled(richiedenti))
				return null;	//nessuna bib dell'utente aderisce a ILL come richiedente

			currentForm.setListaBiblio(new ArrayList<BibliotecaVO>(richiedenti));

			currentForm.setIscrizione(hasAutoreg);
			currentForm.setRichiestaILL(true);

			richiesta.setTipo(TipoRichiesta.RICHIESTA_ILL);
			request.getSession().setAttribute(Constants.RICHIESTA_OPAC, richiesta);

			if (!checkPasswordIsValid(request, uteWeb))
				return Navigation.getInstance(request).goForward(mapping.findForward("cambioPwd"));

		} catch (Exception e) {
			log.error("", e);
		}
		return mapping.getInputForward();
	}

	//metodo di init Form
	private void init(LoginForm currentForm) throws Exception {
		currentForm.setRichiesta(new RichiestaOpacVO());

		currentForm.setUserName("");
		currentForm.setPassword("");

		currentForm.setIscrizione(false);
		currentForm.setInitialized(false);
		currentForm.setRichiestaILL(false);
	}

	public static void main(String[] args) {
		String test = "test messaggio benvenuto <STRONG>test modulo utente</STRONG> web.<autoreg><br/>Se non sei iscritto usa il modulo di <i><autoreg_link></i>.</autoreg>";
		Pattern p = Pattern.compile("(?i)(<autoreg>)(.*)(</autoreg>)");
		Matcher m = p.matcher(test);

		String phrase = m.find() ? m.group(2) : null;
		if (ValidazioneDati.isFilled(phrase)) {

		}

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		LoginForm currentForm = (LoginForm) form;
		if (ValidazioneDati.equals(idCheck, "ISCRIZIONE")) {
			boolean iscrizione = currentForm.isIscrizione();
			boolean richiestaILL = currentForm.isRichiestaILL();
			return iscrizione && !richiestaILL;
		}

		if (ValidazioneDati.equals(idCheck, "LOGGED")) {
			UtenteWeb utente = (UtenteWeb)request.getSession().getAttribute(Constants.UTENTE_WEB_KEY);
			return utente != null;
		}

		if (ValidazioneDati.equals(idCheck, "NOT_LOGGED")) {
			return !checkAttivita(request, currentForm, "LOGGED");
		}

		//almaviva5_20171006 servizi ill
		if (ValidazioneDati.equals(idCheck, "SELEZIONE_BIB_RICHIESTA_ILL")) {
			UtenteWeb utente = (UtenteWeb)request.getSession().getAttribute(Constants.UTENTE_WEB_KEY);
			return utente != null && currentForm.isRichiestaILL();
		}

		if (ValidazioneDati.equals(idCheck, "DATI_INVENTARIO")) {
			RichiestaOpacVO richiesta = currentForm.getRichiesta();
			return richiesta != null
					&& ValidazioneDati.in(richiesta.getTipo(),
							TipoRichiesta.INVENTARIO,
							TipoRichiesta.RICHIESTA_ILL)
					&& richiesta.isCompleta();
		}

		return true;
	}

}
