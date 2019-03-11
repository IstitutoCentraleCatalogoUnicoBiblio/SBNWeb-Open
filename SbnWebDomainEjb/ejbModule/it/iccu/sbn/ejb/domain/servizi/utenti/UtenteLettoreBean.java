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
package it.iccu.sbn.ejb.domain.servizi.utenti;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO.TipoEvento;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.UtenteAccessoDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_accesso_utente;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConvertToHibernate;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.ConfigChangeInterceptor;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.util.servizi.UtenteAccessoUtil;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;

public class UtenteLettoreBean extends TicketChecker implements UtenteLettore, ConfigChangeInterceptor {

	private static final long serialVersionUID = 5311832756720380148L;

	//gli eventi successivi nell'intervallo specificato saranno fusi in un solo evento
	private static final int DEFAULT_EVENT_MERGE_THRESHOLD = 60000;	//1 minuto

	static Logger log = Logger.getLogger(UtenteLettore.class);

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}};

	private UtenteAccessoDAO dao = new UtenteAccessoDAO();

	int EVENT_MERGE_THRESHOLD = DEFAULT_EVENT_MERGE_THRESHOLD;

	private void setProperties() throws Exception {
		EVENT_MERGE_THRESHOLD = CommonConfiguration.getPropertyAsInteger(
				Configuration.SRV_EVENTO_ACCESSO_MERGE_THRESHOLD, DEFAULT_EVENT_MERGE_THRESHOLD);
	}

	public UtenteLettoreBean() {
		try {
			CommonConfiguration.addInterceptor(this, Configuration.SRV_EVENTO_ACCESSO_MERGE_THRESHOLD);
			setProperties();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void onConfigPropertyChange(String key) throws Exception {
		setProperties();
	}

	public void onConfigReload(Set<String> changedProperties) throws Exception {
		setProperties();
	}

	public EventoAccessoVO aggiornaEventoAccesso(String ticket, EventoAccessoVO evento) throws SbnBaseException {
		try {
			checkTicket(ticket);

			evento.validate();
			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			if (evento.isNuovo()) {
				evento.setTsIns(DaoManager.now());
				evento.setUteIns(firmaUtente);
				evento.setFlCanc("N");
			}
			evento.setUteVar(firmaUtente);

			//check utente registrato
			UtenteBaseVO utenteBase = servizi.get().getUtente(ticket, ServiziUtil.espandiCodUtente(evento.getIdTessera()));
			if (utenteBase != null) {
				evento.setAutenticato(true);
				evento.setUtente(utenteBase);
				//si sostituisce il codice tessera fornito in input con il cod_utente recuperato
				evento.setIdTessera(utenteBase.getCodUtente());
			} else {
				//utente non riconosciuto
				evento.setAutenticato(false);
			}

			TipoEvento tipo_nuovo_evento = evento.getEvento();
			boolean tipoEventoImpostato = (tipo_nuovo_evento != null);

			//1. ricerca ultimo evento utente
			Date limit = null;//LocalDateTime.now().minusDays(1).toDate();	//eventi delle ultime 24 ore
			Tbl_accesso_utente ultimo_evento = dao.getUltimoEventoUtente(evento.getCodPolo(), evento.getCodBib(), evento.getIdTessera(), limit);
			if (ultimo_evento != null) {
				TipoEvento tipo_ultimo_evento = TipoEvento.of(ultimo_evento.getFl_evento());
				if (tipoEventoImpostato && tipo_nuovo_evento == tipo_ultimo_evento) {
					Timestamp dataEventoFittizio = evento.getDataEvento();	//DateUtil.addMillis(evento.getDataEvento(), -50);
					Tbl_accesso_utente fittizio = creaEventoFittizio(ultimo_evento, firmaUtente, dataEventoFittizio);
					tipo_ultimo_evento = TipoEvento.of(fittizio.getFl_evento());

					log.warn(String.format("Utente: %s: inserito evento '%s' fittizio in data '%s'",
							evento.getIdTessera(), tipo_ultimo_evento,
							DateUtil.formattaDataOra(dataEventoFittizio)));
				}
/*
				if (checkFusioneEvento(evento, ultimo_evento.getTs_evento())) {
					//questo evento viene ignorato
					return ConvertToWeb.Utenti.eventoAccesso(ultimo_evento);
				}


				if (!DateUtil.isSameDay(evento.getDataEvento(), ultimo_evento.getTs_evento()) ) {
					//i due eventi non sono nella stessa giornata, se l'ultimo evento non è USCITA
					//viene inserito un evento di uscita fittizio a fine giornata
					if (tipo_ultimo_evento != TipoEvento.USCITA) {
						Tbl_accesso_utente fittizio = creaEventoUscitaFittizio(ultimo_evento, firmaUtente);
						log.warn(String.format("Utente: %s: inserito evento USCITA fittizio in data '%s'",
								evento.getIdTessera(), DateUtil.formattaDataOra(fittizio.getTs_evento())));
						tipo_ultimo_evento = TipoEvento.of(fittizio.getFl_evento());	//USCITA
					}
				}
*/
				// il nuovo evento è l'inverso
				tipo_nuovo_evento = UtenteAccessoUtil.checkEvento(tipo_ultimo_evento, tipo_nuovo_evento);

			} else {
				//primo evento per questo utente, default a ENTRATA
				tipo_nuovo_evento = ValidazioneDati.coalesce(tipo_nuovo_evento, TipoEvento.ENTRATA);
				if (tipo_nuovo_evento != TipoEvento.ENTRATA)
					throw new ValidationException(SbnErrorTypes.SRV_TIPO_ACCESSO_UTENTE_NON_VALIDO);
			}

			evento.setEvento(tipo_nuovo_evento);

			Tbl_accesso_utente accesso = ConvertToHibernate.Utenti.eventoAccesso(evento);
			accesso = dao.aggiornaEventoAccesso(accesso);

			return ConvertToWeb.Utenti.eventoAccesso(accesso);

		} catch (SbnBaseException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION, e);
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e);
		}
	}

	/*
	 * Crea un evento fittizio di USCITA con orario evento posto a fine giornata.
	 */

	Tbl_accesso_utente creaEventoUscitaFittizio(Tbl_accesso_utente evento, String firmaUtente) throws DaoManagerException, DAOConcurrentException {
		Tbl_accesso_utente evento_fittizio = new Tbl_accesso_utente();

		evento_fittizio.setBiblioteca(evento.getBiblioteca());
		evento_fittizio.setUtente(evento.getUtente());
		evento_fittizio.setPosto(evento.getPosto());
		evento_fittizio.setId_tessera(evento.getId_tessera());
		evento_fittizio.setFl_autenticato(evento.getFl_autenticato());

		evento_fittizio.setFl_forzatura('S');	//evento forzato dal sistema
		evento_fittizio.setFl_canc('N');
		evento_fittizio.setTs_ins(DaoManager.now());
		evento_fittizio.setUte_ins(firmaUtente);
		evento_fittizio.setUte_var(firmaUtente);

		evento_fittizio.setId(0);	//nuovo inserimento
		evento_fittizio.setFl_evento(TipoEvento.USCITA.getFl_evento());
		Timestamp ts_evento = DateUtil.withTimeAtEndOfDay(evento.getTs_evento());	//fine giornata
		evento_fittizio.setTs_evento(ts_evento);

		dao.aggiornaEventoAccesso(evento_fittizio);

		return evento_fittizio;
	}

	Tbl_accesso_utente creaEventoFittizio(Tbl_accesso_utente evento, String firmaUtente, Timestamp when) throws DaoManagerException, DAOConcurrentException {
		Tbl_accesso_utente evento_fittizio = new Tbl_accesso_utente();

		evento_fittizio.setBiblioteca(evento.getBiblioteca());
		evento_fittizio.setUtente(evento.getUtente());
		evento_fittizio.setPosto(evento.getPosto());
		evento_fittizio.setId_tessera(evento.getId_tessera());
		evento_fittizio.setFl_autenticato(evento.getFl_autenticato());

		evento_fittizio.setFl_forzatura('S');	//evento forzato dal sistema
		evento_fittizio.setFl_canc('N');
		evento_fittizio.setTs_ins(DaoManager.now());
		evento_fittizio.setUte_ins(firmaUtente);
		evento_fittizio.setUte_var(firmaUtente);

		evento_fittizio.setId(0);	//nuovo inserimento

		TipoEvento tipoEvento = UtenteAccessoUtil.getEventoInverso(TipoEvento.of(evento.getFl_evento()));

		evento_fittizio.setFl_evento(tipoEvento.getFl_evento());
		evento_fittizio.setTs_evento(when);

		dao.aggiornaEventoAccesso(evento_fittizio);

		return evento_fittizio;
	}

	boolean checkFusioneEvento(EventoAccessoVO evento, Timestamp ts_ultimo_evento) {

		DateTime dt_old = LocalDateTime.fromDateFields(ts_ultimo_evento).toDateTime();
		DateTime dt_new = LocalDateTime.fromDateFields(evento.getDataEvento()).toDateTime();
		long duration = new Interval(dt_old, dt_new).toDurationMillis();

		log.debug(String.format("checkFusioneEvento(): %s --> %s = %dms", dt_old, dt_new, duration));
		//il nuovo l'evento sarà fuso sul precedente se entro il limite o comunque nello stesso minuto
		if (duration < EVENT_MERGE_THRESHOLD && dt_old.getMinuteOfHour() == dt_new.getMinuteOfHour()) {
			//questo evento viene ignorato
			log.warn(String.format("Utente: %s: distanza da ultimo evento < %dms. Evento scartato",
					evento.getIdTessera(), EVENT_MERGE_THRESHOLD));
			return true;
		}

		return false;
	}

}
