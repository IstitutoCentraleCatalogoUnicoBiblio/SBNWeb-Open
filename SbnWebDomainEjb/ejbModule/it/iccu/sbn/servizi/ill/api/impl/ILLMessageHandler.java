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
package it.iccu.sbn.servizi.ill.api.impl;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriAllineamentoBibliotecheILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.BiblioPoloVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziIllDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ill.api.ILLBaseAction;
import it.iccu.sbn.servizi.ill.api.ILLMessageHandlerAPI;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CancelReplyType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CancelType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CheckedInType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ConditionalReplyType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DamagedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ExpiredType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.LostType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.MostRecentServiceNote;
import it.iccu.sbn.vo.xml.binding.ill.apdu.OverdueType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RecallType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ReceivedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RenewAnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RenewType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RequesterNote;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ResponderNote;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ReturnedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ShippedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusOrErrorReportType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

public abstract class ILLMessageHandler extends SerializableVO implements
		ILLMessageHandlerAPI {

	private static final long serialVersionUID = 2060450779386610688L;

	private Servizi servizi;

	protected Logger log = Logger.getLogger(ILLMessageHandler.class);

	protected final ILLAPDU input;
	protected String targetIsil;

	private final String ticket;
	private final String codPolo;
	private final String codBib;

	public static ILLMessageHandler getHandler(String ticket, ILLAPDU input)
			throws ApplicationException {
		if (input == null)
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_VALIDAZIONE_APDU);

		List<ILLRequestType> illRequest = input.getILLRequest();
		if (isFilled(illRequest))
			return new ILLRequestTypeHandler(ticket, input);

		ILLAnswerType illAnswer = input.getILLAnswer();
		if (illAnswer != null)
			return new ILLAnswerTypeHandler(ticket, input);

		ConditionalReplyType conditionalReply = input.getConditionalReply();
		if (conditionalReply != null)
			return new ConditionalReplyHandler(ticket, input);

		ShippedType shipped = input.getShipped();
		if (shipped != null)
			return new ShippedTypeHandler(ticket, input);

		ReceivedType received = input.getReceived();
		if (received != null)
			return new ReceivedTypeHandler(ticket, input);

		RenewType renew = input.getRenew();
		if (renew != null)
			return new RenewTypeHandler(ticket, input);

		RenewAnswerType renewAnswer = input.getRenewAnswer();
		if (renewAnswer != null)
			return new RenewAnswerTypeHandler(ticket, input);

		ReturnedType returned = input.getReturned();
		if (returned != null)
			return new ReturnedTypeHandler(ticket, input);

		CheckedInType checkedIn = input.getCheckedIn();
		if (checkedIn != null)
			return new CheckedInTypeHandler(ticket, input);

		RecallType recall = input.getRecall();
		if (recall != null)
			return new RecallTypeHandler(ticket, input);

		LostType lost = input.getLost();
		if (lost != null)
			return new LostTypeHandler(ticket, input);

		CancelType cancel = input.getCancel();
		if (cancel != null)
			return new CancelTypeHandler(ticket, input);

		CancelReplyType cancelReply = input.getCancelReply();
		if (cancelReply != null)
			return new CancelReplyTypeHandler(ticket, input);

		ExpiredType expired = input.getExpired();
		if (expired != null)
			return new ExpiredTypeHandler(ticket, input);

		OverdueType overdue = input.getOverdue();
		if (overdue != null)
			return new OverdueTypeHandler(ticket, input);

		DamagedType damaged = input.getDamaged();
		if (damaged !=  null)
			return new DamagedTypeHandler(ticket, input);

		StatusOrErrorReportType statusOrErrorReport = input.getStatusOrErrorReport();
		if (statusOrErrorReport != null)
			return new StatusOrErrorReportTypeHandler(ticket, input);

		throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_VALIDAZIONE_APDU);
	}

	protected Servizi getServizi() throws Exception {
		if (servizi != null)
			return servizi;

		servizi = DomainEJBFactory.getInstance().getServizi();
		return servizi;
	}

	protected ILLMessageHandler(String ticket, ILLAPDU input) {
		this.ticket = ticket;
		this.codPolo = DaoManager.codPoloFromTicket(ticket);
		this.codBib = DaoManager.codBibFromTicket(ticket);

		this.input = input;
		log.debug("creazione ILL handler: " + this.getClass().getSimpleName());
	}

	protected String getTicket() {
		return ticket;
	}

	protected String getCodPolo() {
		return codPolo;
	}

	protected String getCodBib() {
		return codBib;
	}

	protected abstract void valorizzaDatiBase(DatiRichiestaILLVO dati_richiesta_ill, ILLBaseAction action);

	protected DatiRichiestaILLVO getDatiRichiestaIll(String tid, RuoloBiblioteca ruolo, String reqId, String resId) throws DaoManagerException {
		RichiesteServizioDAO dao = new RichiesteServizioDAO();
		String country = Locale.getDefault().getCountry();
		reqId = ServiziUtil.formattaIsil(reqId, country);
		resId = ServiziUtil.formattaIsil(resId, country);
		Tbl_dati_richiesta_ill dri = dao.getDatiRichiestaIll(tid, ruolo.getFl_ruolo(), reqId, resId);
		DatiRichiestaILLVO richiestaILL = dri != null ? ConversioneHibernateVO.toWeb().datiRichiestaILL(dri, dri.getRichiesta()) : new DatiRichiestaILLVO();
		String firmaUtente = DaoManager.getFirmaUtente(getTicket());
		if (richiestaILL.isNuovo() ) {
			//nuova richiesta
			richiestaILL.setTransactionId(new Long(tid));
			richiestaILL.setUteIns(firmaUtente);
			richiestaILL.setTsIns(DaoManager.now());
			richiestaILL.setFlCanc("N");
			richiestaILL.setRuolo(ruolo);
			richiestaILL.setCurrentState(StatoIterRichiesta.F111_DEFINIZIONE_RICHIESTA.getISOCode());
		}
		richiestaILL.setUteVar(firmaUtente);

		return richiestaILL;
	}

	protected DatiRichiestaILLVO getDatiRichiestaIll(ILLBaseAction action) throws DaoManagerException {
		String tid = action.getTransactionId().getValue();

		String responderId = ServiziUtil.formattaIsil(action.getResponderId(), Locale.getDefault().getCountry());
		RuoloBiblioteca ruolo = ValidazioneDati.equals(getTarget(), responderId) ? RuoloBiblioteca.FORNITRICE : RuoloBiblioteca.RICHIEDENTE;

		DatiRichiestaILLVO dati_richiesta_ill = null;
		if (ruolo == RuoloBiblioteca.FORNITRICE)
			dati_richiesta_ill = getDatiRichiestaIll(tid, ruolo, null, action.getResponderId());
		else
			dati_richiesta_ill = getDatiRichiestaIll(tid, ruolo, action.getRequesterId(), null);

		return dati_richiesta_ill;
	}

	protected BibliotecaILLVO getBibliotecaByIsil(String isil) throws Exception {

		ServiziIllDAO dao = new ServiziIllDAO();
		isil = ServiziUtil.formattaIsil(isil, Locale.getDefault().getCountry() );
		Tbl_biblioteca_ill bibIll = dao.getBibliotecaByIsil(isil);
		if (bibIll == null) {
			//biblioteca non trovata, si attiva l'allineamento delle biblioteche ill
			ParametriAllineamentoBibliotecheILLVO richiesta = new ParametriAllineamentoBibliotecheILLVO();
			richiesta.setTicket(ticket);
			richiesta.setAllineaBiblioteche(true);
			richiesta.setAllineaRichieste(false);

			DomainEJBFactory.getInstance().getServiziBMT().allineaServerILL(richiesta, null);

//			AllineamentoBibliotecheILL allinea = new AllineamentoBibliotecheILL(null, richiesta, null);
//			allinea.allinea();

			//secondo tentativo
			bibIll = dao.getBibliotecaByIsil(isil);
			if (bibIll == null)
				throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_NON_TROVATA);
		}

		BibliotecaILLVO biblioteca = ConversioneHibernateVO.toWeb().bibliotecaILL(bibIll, null);
		return biblioteca;
	}

	protected UtenteBibliotecaVO controllaUtenteBiblioteca(String codPolo, String codBib, BibliotecaILLVO bibliotecaILL) throws Exception {

		boolean importa = false;
		UtenteBibliotecaVO utente = null;
		UtenteBaseVO utenteBase = bibliotecaILL.getUtente();
		if (utenteBase != null) {
			//l'utente esiste, viene cercato prima in biblioteca, poi in polo.
			RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
			ricerca.setCodPolo(codPolo);
			ricerca.setCodBib(codBib);
			ricerca.setCodUte(utenteBase.getCodUtente());
			utente = getServizi().getDettaglioUtente(ticket, ricerca, null, Locale.getDefault());
			if (utente != null)
				return utente;	//iscritto a questa bib.

			//non trovato: si cerca nella bib che ha creato l'anagrafica
			ricerca.setCodBib(utenteBase.getCodBib());
			importa = true;
			utente = getServizi().getDettaglioUtente(ticket, ricerca, null, Locale.getDefault());

			// pulizia dei dati appartenenti alle altre biblioteche
			utente.setImportato(true);
			utente.setCodBibSer(codBib);
			utente.setCodPoloSer(codPolo);
			utente.setAutorizzazioni(new AutorizzazioniVO());
			utente.getProfessione().getMaterie().clear();
			utente.getProfessione().setIdOccupazione(null);
			utente.getProfessione().setIdSpecTitoloStudio(null);
			utente.getBibliopolo().clear();

			// l'id appartiene alla biblioteca da cui sto importando l'utente
			// all'atto della registrazione ne sarà creato uno nuovo
			utente.setIdUtenteBiblioteca(null);
			// Antonio giovedì 11 ottobre 2007
			utente.setNuovoUte(false);
		}

		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		UtentiDAO udao = new UtentiDAO();

		if (!importa) {
			//nuovo utente
			utente = new UtenteBibliotecaVO();
			BibliotecaVO bib = bibliotecaILL.getBiblioteca();

			// creazione di un utente lettore partendo dai dati di biblioteca.
			utente.setCodiceBiblioteca(codBib);
			utente.setCodPolo(codPolo);
			utente.setUteIns(firmaUtente);
			utente.setUteVar(firmaUtente);
			utente.setFlCanc("N");
			//currentForm.setTipoUtente("E");
			utente.getProfessione().setPersonaGiuridica('S');
			utente.getProfessione().setTipoPersona("5");

			utente.setCodBibSer(codBib);
			utente.setCodPoloSer(codPolo);

			AnagrafeVO anag = utente.getAnagrafe();
			anag.setCodFiscale("");
			//almaviva5_20110228 #4207
			//almaviva5_20110428 #4401
			BiblioPoloVO bibliopolo = utente.getBibliopolo();
			if (ValidazioneDati.isFilled(bib.getCd_ana_biblioteca()))
				bibliopolo.setCodiceAnagrafe(bib.getCd_ana_biblioteca());

			if (ValidazioneDati.isFilled(bib.getCod_fiscale()) )
				anag.setCodFiscale(bib.getCod_fiscale());
			else
				if (ValidazioneDati.isFilled(bib.getP_iva()) )
					anag.setCodFiscale(bib.getP_iva());

			utente.setNome("");
			utente.setCognome("");
			if (bib.getNom_biblioteca() != null)
				utente.setCognome(bib.getNom_biblioteca());

			anag.getResidenza().setProvincia("");
			if (bib.getProvincia() != null)
				anag.getResidenza().setProvincia(bib.getProvincia());

			anag.getResidenza().setCap("");
			if (bib.getCap() != null) {
				anag.getResidenza().setCap(bib.getCap());
			}
			anag.getResidenza().setVia("");
			if (bib.getIndirizzo() != null) {
				anag.getResidenza().setVia(bib.getIndirizzo());
			}
			anag.getResidenza().setCitta("");
			if (bib.getLocalita() != null) {
				anag.getResidenza().setCitta(bib.getLocalita());
			}
			anag.setNazionalita("");
			if (bib.getPaese() != null) {
				anag.getResidenza().setNazionalita(bib.getPaese());
			}
			anag.setPostaElettronica("");
			if (bib.getE_mail() != null) {
				anag.setPostaElettronica(bib.getE_mail());
			}
			utente.getProfessione().setTipoPersona("5");

			anag.getResidenza().setTelefono("");
			if (bib.getTelefono() != null) {
				anag.getResidenza().setTelefono(bib.getTelefono());
			}
			anag.getResidenza().setFax("");
			if (bib.getFax() != null) {
				anag.getResidenza().setFax(bib.getFax());
			}
			// impostazione del polo e della biblioteca
			bibliopolo.setCodBibXUteBib(codBib);
			bibliopolo.setCodPoloXUteBib(codPolo);
			bibliopolo.setPoloNote("importato come biblioteca ILL richiedente");

			//almaviva5_20101206 aggiorna chiave utente
			String nome = utente.getCognomeNome();
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi("", nome);
			utente.setChiaveUte(key.getKy_cles1_A());
		}

		//valorizzazione diritti da profilo impostato in conf. biblioteca
		ParametriBibliotecaVO paramBib = getServizi().getParametriBiblioteca(ticket, codPolo, codBib);
		if (paramBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);
		AutorizzazioneVO autorizzazioneILL = paramBib.getAutorizzazioneILL();
		if (autorizzazioneILL == null)
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_TIPO_AUTORIZZAZIONE_ASSENTE);

		utente.setAutorizzazioni(new AutorizzazioniVO(autorizzazioneILL));

		//inserimento utente
		boolean ok = false;
		if (!importa)
			ok = getServizi().insertUtente(ticket, utente);
		else
			ok = getServizi().importaUtente(ticket, utente);

		if (!ok)
			//TODO togliere errore generico
			throw new ApplicationException(SbnErrorTypes.SRV_GENERIC);

		//associa utente a biblioteca
		Tbl_utenti utenteByIsil = udao.getUtenteByIsil(bibliotecaILL.getIsil());
		associaUtenteBiblioteca(bibliotecaILL, utenteByIsil);

		//rilettura utente
		RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
		ricerca.setCodPolo(codPolo);
		ricerca.setCodBib(codBib);
		ricerca.setCodUte(utenteByIsil.getCod_utente() );
		return getServizi().getDettaglioUtente(ticket, ricerca, null, Locale.getDefault());

	}

	private void associaUtenteBiblioteca(BibliotecaILLVO bibliotecaILL, Tbl_utenti utente) throws Exception, DaoManagerException,
			DAOConcurrentException {
		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		bibliotecaILL.setUtente(ServiziConversioneVO.daHibernateAWebUtente(utente));
		bibliotecaILL.setUteVar(firmaUtente);
		Tbl_biblioteca_ill biblioteca_ill = ConversioneHibernateVO.toHibernate().bibliotecaILL(null, bibliotecaILL);
		ServiziIllDAO dao = new ServiziIllDAO();
		dao.aggiornaBiblioteca(biblioteca_ill);
	}

	protected DatiRichiestaILLVO aggiornaDatiRichiestaILL(DatiRichiestaILLVO dati_richiesta_ill) throws Exception {
		CommandResultVO result = getServizi().invoke(CommandInvokeVO.build(getTicket(),
						CommandType.SRV_ILL_AGGIORNA_DATI_RICHIESTA,
						dati_richiesta_ill));
		result.throwError();
		return (DatiRichiestaILLVO) result.getResult();
	}

	protected UtenteBibliotecaVO getUtenteBiblioteca(String codUtenteILL) throws Exception {
		RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
		ricerca.setCodPolo(getCodPolo());
		ricerca.setCodBib(getCodBib());
		ricerca.setCodUte(codUtenteILL);
		UtenteBibliotecaVO utente = getServizi().getDettaglioUtente(ticket, ricerca, null, Locale.getDefault());

		return utente;
	}

	protected MessaggioVO preparaMessaggio(StatoIterRichiesta stato, RuoloBiblioteca ruolo, RequesterNote requesterNotes) {
		List<String> notes = requesterNotes != null ? requesterNotes.getNote() : null;
		return preparaMessaggio(stato, ruolo, notes);
	}

	protected MessaggioVO preparaMessaggio(StatoIterRichiesta stato, RuoloBiblioteca ruolo, ResponderNote responderNotes) {
		List<String> notes = responderNotes != null ? responderNotes.getNote() : null;
		return preparaMessaggio(stato, ruolo, notes);
	}

	protected MessaggioVO preparaMessaggio(StatoIterRichiesta stato, RuoloBiblioteca ruolo, MostRecentServiceNote serviceNotes) {
		List<String> notes = serviceNotes != null ? serviceNotes.getNote() : null;
		return preparaMessaggio(stato, ruolo, notes);
	}

	private MessaggioVO preparaMessaggio(StatoIterRichiesta stato, RuoloBiblioteca ruolo, List<String> notes) {
		StringBuilder buf = new StringBuilder();
		if (isFilled(notes))
			for (String n : notes)
				buf.append(trimOrEmpty(n)).append('\u0020');

		return preparaMessaggio(stato, ruolo, buf.toString().trim());
	}

	protected MessaggioVO preparaMessaggio(StatoIterRichiesta stato, RuoloBiblioteca ruolo, String note) {
		MessaggioVO msg = new MessaggioVO();
		msg.setDataMessaggio(DaoManager.now());
		msg.setTipoInvio(ILLConfiguration2.getInstance().getTipoInvioMessaggio(ruolo, stato));
		msg.setStato(stato.getISOCode());

		if (!isFilled(note) && stato.withNotes())
			note = "n/a";

		msg.setNote(note);
		return msg;
	}

	public void setTarget(String isil) {
		this.targetIsil = ServiziUtil.formattaIsil(isil, Locale.getDefault().getCountry() );
	}

	public String getTarget() {
		return targetIsil;
	}

}
