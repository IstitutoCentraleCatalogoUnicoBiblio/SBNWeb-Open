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
package it.iccu.sbn.webservices.servizi.impl;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.domain.servizi.utenti.UtenteLettore;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO.TipoEvento;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.xml.binding.sbnwebws.EsitoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.UtenteType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.webservices.WSRESTBaseImpl;
import it.iccu.sbn.webservices.servizi.WSUtenteLettoreREST;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;



public class WSUtenteLettoreRESTImpl extends WSRESTBaseImpl implements WSUtenteLettoreREST {

	static class ISO8601TimestampAdapterFactory implements TypeAdapterFactory  {

		static class DateAdapter<T extends Date> extends TypeAdapter<Date> {

			private DateTimeFormatter formatter;

			@Override
			public T read(JsonReader reader) throws IOException {
				return null;
			}

			public DateAdapter() {
				formatter = ISODateTimeFormat.dateTime();
			}

			@Override
			public void write(JsonWriter writer, Date value) throws IOException {
				DateTime dateTime = LocalDateTime.fromDateFields(value).toDateTime();
				writer.value(dateTime.toString(formatter));
			}
		}

		private DateAdapter<Date> adapter = new DateAdapter<Date>();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public TypeAdapter create(Gson gson, TypeToken type) {
			return adapter;
		}

	}

	static class AccessoResponse implements Serializable {

		private static final long serialVersionUID = 2425446675781593229L;

		String id_tessera;
		String cod_utente;
		String cognome_nome;
		boolean autenticato;
		TipoEvento evento;

		@JsonAdapter(ISO8601TimestampAdapterFactory.class)
		Timestamp data_evento;

		public AccessoResponse(EventoAccessoVO ea) {
			id_tessera = ea.getIdTessera();
			evento = ea.getEvento();
			data_evento = ea.getDataEvento();
			autenticato = ea.isAutenticato();
			if (autenticato) {
				UtenteBaseVO utente = ea.getUtente();
				cod_utente = utente.getCodUtente();
				cognome_nome = utente.getCognomeNome();
			}
		}
	}

	static Reference<AmministrazionePolo> polo = new Reference<AmministrazionePolo>() {
		@Override
		protected AmministrazionePolo init() throws Exception {
			return DomainEJBFactory.getInstance().getPolo();
		}};

	static Reference<UtenteLettore> utente = new Reference<UtenteLettore>() {
		@Override
		protected UtenteLettore init() throws Exception {
			return DomainEJBFactory.getInstance().getUtenteLettore();
		}};

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}};

	private String codPolo;

	public WSUtenteLettoreRESTImpl() throws Exception {
		codPolo = polo.get().getInfoPolo().getCd_polo();
	}

	EventoAccessoVO accesso(HttpServletRequest request, HttpHeaders headers, String cd_bib, String idTessera,
			String password, String action, Boolean allowNonMembers) throws Exception {
		try {
			addClient(request);

			try {
				if (!isFilled(cd_bib))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codice biblioteca");

				if (!isFilled(idTessera))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "id tessera");

				TipoEvento evento = null;
				if (isFilled(action)) {
					try {
						evento = TipoEvento.valueOf(action.toUpperCase());
					} catch (Exception e) {
						throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "azione");
					}
				}

				boolean checkPassword = isFilled(password);

				String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
				String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getByName(request.getRemoteAddr()));

				//di default il controllo non blocca i non iscritti
				allowNonMembers = coalesce(allowNonMembers, Boolean.TRUE);
				//ma se è stata specificata un password allora l'utente deve essere registrato
				allowNonMembers = checkPassword ? Boolean.FALSE : allowNonMembers;

				if (!allowNonMembers) {
					boolean userExists = false;
					boolean userIsAuthorized = false;

					//solo gli utenti registrati possono accedere
					UtenteBaseVO utenteBase = servizi.get().getUtente(ticket, ServiziUtil.espandiCodUtente(idTessera));

					userExists = (utenteBase != null);
					if (userExists) {
						//utente trovato, check password
						userIsAuthorized = true;
						if (checkPassword) {
							PasswordEncrypter crypt = new PasswordEncrypter(password);
							userIsAuthorized = ValidazioneDati.equals(utenteBase.getPassword(), crypt.encrypt(password));
						}
					}

					if (!userExists || !userIsAuthorized) {
						//EventoAccessoVO dummyEventoAccesso = preparaEventoAccesso(codPolo, codBib, idTessera, null);
						//return dummyEventoAccesso;
						throw new ApplicationException(SbnErrorTypes.SRV_TIPO_ACCESSO_UTENTE_NON_VALIDO);
					}
				}

				EventoAccessoVO eventoAccesso = preparaEventoAccesso(codPolo, codBib, idTessera, evento);
				eventoAccesso = utente.get().aggiornaEventoAccesso(ticket, eventoAccesso);

				return eventoAccesso;

			} catch (SbnBaseException e) {
				throw e;

			} catch (Exception e) {
				throw new WebApplicationException(e);
			}

		} finally {
			removeClient();
		}
	}

	public Response accessoUtenteJSON(HttpServletRequest request, HttpHeaders headers, String cd_bib, String idTessera,
			String password, String action, Boolean allowNonMembers) throws Exception {
		try {
			EventoAccessoVO eventoAccesso = accesso(request, headers, cd_bib, idTessera, password, action, allowNonMembers);
			AccessoResponse response = new AccessoResponse(eventoAccesso);
			return Response.ok().entity(ClonePool.toJson(response, AccessoResponse.class)).build();

		} catch (SbnBaseException e) {
			return Response.status(Status.BAD_REQUEST).entity(super.formattaErrore(e)).build();
		}
	}

	public Response accessoUtenteXML(HttpServletRequest request, HttpHeaders headers, String cd_bib, String idTessera,
			String password, String action, Boolean allowNonMembers) throws Exception {
		try {
			EventoAccessoVO eventoAccesso = accesso(request, headers, cd_bib, idTessera, password, action, allowNonMembers);

			SbnwebType sbn = new SbnwebType();
			EsitoType esito = new EsitoType();
			esito.setReturnCode(0);
			esito.setMessage("OK");
			esito.setNumeroRighe(1);
			sbn.setEsito(esito);

			UtenteType utente = ConversioneHibernateVO.toXML().utente(eventoAccesso);
			sbn.setUtente(utente);

			return Response.ok(sbn).build();

		} catch (SbnBaseException e) {
			return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();
		}
	}

	EventoAccessoVO preparaEventoAccesso(String codPolo, String codBib, String id_tessera, TipoEvento evento)
			throws Exception {
		EventoAccessoVO ea = new EventoAccessoVO();
		ea.setCodPolo(codPolo);
		ea.setCodBib(codBib);
		ea.setEvento(evento);
		ea.setDataEvento(DaoManager.now());
		ea.setAutenticato(false);
		ea.setIdTessera(id_tessera);

		return ea;
	}

}
