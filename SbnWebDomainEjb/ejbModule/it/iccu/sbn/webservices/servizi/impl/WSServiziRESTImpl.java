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

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DisponibilitaType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.EsitoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.UtenteType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.webservices.WSRESTBaseImpl;
import it.iccu.sbn.webservices.servizi.WSServiziILLREST;
import it.iccu.sbn.webservices.servizi.WSServiziREST;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;


public class WSServiziRESTImpl extends WSRESTBaseImpl implements WSServiziREST {

	private static Logger log = Logger.getLogger(WSServiziREST.class);

	private AmministrazionePolo amministrazionePolo;
	private Servizi servizi;

	private AmministrazionePolo getPolo() throws Exception {

		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;
	}

	private Servizi getServizi() {
		if (servizi == null) {
			try {
				servizi = DomainEJBFactory.getInstance().getServizi();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return servizi;
	}

	public Response getDisponibilitaInvDoc(HttpServletRequest request, String cd_bib, String idDoc)	throws Exception {

		try {
			addClient(request);

			if (!ValidazioneDati.isFilled(cd_bib))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "cd_bib");

			try {
				InventarioVO inv = InventarioRFIDParser.parse(idDoc);

				String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
				String codPolo = getPolo().getInfoPolo().getCd_polo();

				String bibInv = inv.getCodBib();
				if (!ValidazioneDati.isFilled(bibInv))
					inv.setCodBib(codBib);

				MovimentoVO mov = new MovimentoVO();
				mov.setCodPolo(codPolo);
				mov.setCodBibOperante(codBib);
				mov.setCodBibInv(inv.getCodBib());
				mov.setCodSerieInv(inv.getCodSerie());
				mov.setCodInvenInv(inv.getCodInvent() + "");
				mov.setDataInizioPrev(DaoManager.now());

				ControlloDisponibilitaVO cdvo = new ControlloDisponibilitaVO(mov, false);
				cdvo.setNoPrenotazione(true);

				String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());
				CommandInvokeVO cmd = CommandInvokeVO.build(ticket, CommandType.SRV_CONTROLLO_DISPONIBILITA_WS, cdvo, true);
				CommandResultVO response = getServizi().invoke(cmd);
				response.throwError();

				DisponibilitaType dtype = (DisponibilitaType) response.getResult();

				SbnwebType sbn = new SbnwebType();
				EsitoType esito = new EsitoType();
				esito.setReturnCode(0);
				esito.setNumeroRighe(1);
				esito.setMessage("OK");
				sbn.setEsito(esito);

				sbn.setDisponibilita(dtype);

				return Response.ok(sbn).build();

			} catch (SbnBaseException e) {
				return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

			} catch (Exception e) {
				throw new WebApplicationException(e);
			}

		} finally {
			removeClient();
		}
	}

	public Response redirect(HttpServletRequest request, HttpHeaders headers, String isil, String query)
			throws Exception {
		String path = CommonConfiguration.getProperty("ILL_DEBUG_REDIRECT_PATH", FileUtil.getTempFilesDir() );
		String url = CommonConfiguration.getProperty("ILL_DEBUG_REDIRECT_URL");
		url += isil;
		log.debug("redirect url: " + url);
		log.debug("body: " + query);

		long when = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			String fileName = path + File.separator + String.format("apdu_%d_%s_req.xml", when, isil);
			FileUtil.writeStringToFile(fileName, URLDecoder.decode(query, "UTF-8") );
		}

		String response = "";
		OutputStream out = null;
		InputStream in = null;

		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setDoOutput(true); // Triggers POST.

			for (String key : headers.getRequestHeaders().keySet() ) {
		        List<String> values = headers.getRequestHeader(key);
		        for (String value : values) {
		        	connection.setRequestProperty(key, value);
		        	log.debug(String.format("header: %s --> %s", key, value) );
				}
		    }

			out = connection.getOutputStream();
			out.write(query.getBytes("UTF-8"));

			in = connection.getInputStream();
			response = FileUtil.streamToString(in);

			if (log.isDebugEnabled() && ValidazioneDati.isFilled(response) ) {
				String fileName = path + File.separator + String.format("apdu_%d_%s_res.xml", when, isil);
				FileUtil.writeStringToFile(fileName, URLDecoder.decode(response, "UTF-8") );
			}

		} catch (SocketException e) {
			log.warn("redirect non attivo, gestione locale della richiesta APDU...");
			WSServiziILLREST ws = new WSServiziILLRESTImpl();
			return ws.apdu(request, headers, isil, query);
		} finally {
			FileUtil.close(in);
			FileUtil.close(out);
		}

		return Response.ok(response).build();

	}

	public Response utente(HttpServletRequest request, HttpHeaders headers, String cd_bib, String username,
			String password) throws Exception {

		try {
			addClient(request);

			try {
				if (!ValidazioneDati.isFilled(cd_bib))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codice biblioteca");

				if (!ValidazioneDati.isFilled(username))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "userId");

				if (!ValidazioneDati.isFilled(password))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "password");

				String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
				String codPolo = getPolo().getInfoPolo().getCd_polo();

				String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());

				String codUtente = ServiziUtil.espandiCodUtente(username);
				UtenteBaseVO utente = getServizi().getUtente(ticket, codUtente);
				if (utente == null)
					//utente non trovato
					return Response.status(Status.NOT_FOUND).entity("utente non trovato: " + username).build();

				codUtente = utente.getCodUtente();
				RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
				ricerca.setCodPolo(codPolo);
				ricerca.setCodBib(codBib);
				ricerca.setCodUte(codUtente);
				//dettaglio utente
				UtenteBibliotecaVO utenteBib = getServizi().getDettaglioUtente(ticket, ricerca, null, Locale.getDefault());

				if (utenteBib == null)
					//utente non iscritto
					return Response.status(Status.NOT_FOUND).entity("utente non iscritto alla biblioteca: " + codBib).build();

				//check password
				codUtente = utenteBib.getCodiceUtente();
				PasswordEncrypter crypt = new PasswordEncrypter(password);
				String encrypted = crypt.encrypt(password);

				boolean authorized = ValidazioneDati.equals(utenteBib.getPassword(), encrypted);

				log.debug(String.format("tentativo login per utente: %s, authorized: %b", codUtente, authorized));

				if (!authorized)
					return Response.status(Status.UNAUTHORIZED).entity("password errata").build();

				//controllo su utente auto-registrato
				String codAut = utenteBib.getAutorizzazioni().getCodTipoAutor();
				log.debug(String.format("utente: %s, codice tipo autorizzazione: '%s'", codUtente, codAut));

				if (ValidazioneDati.isFilled(codAut)) {
					AutorizzazioneVO aut = getServizi().getTipoAutorizzazione(ticket, codPolo, codBib, codAut);

					boolean selfregistered = (aut != null && ValidazioneDati.equals(aut.getAutomaticoPer(), "*"));
					log.debug(String.format("utente: %s, selfregistered: %b", codUtente, selfregistered));
					if (selfregistered) {
						boolean escludiAutoregistrati = Boolean.parseBoolean(CommonConfiguration
								.getProperty(Configuration.SRV_ACCESSO_UTENTE_ESCLUDI_AUTOREGISTRATI, "true"));
						if (escludiAutoregistrati)
							return Response.status(Status.UNAUTHORIZED).entity("utente auto-registrato").build();
					}
				}

				return Response.ok().entity("ok").build();

			} catch (SbnBaseException e) {
				//return Response.status(Status.BAD_REQUEST).entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();
				return Response.status(Status.BAD_REQUEST).entity(super.formattaErrore(e)).build();

			} catch (Exception e) {
				throw new WebApplicationException(e);
			}

		} finally {
			removeClient();
		}
	}

	public Response utenteDettaglio(HttpServletRequest request, HttpHeaders headers, String cd_bib, String username,
			String password) throws Exception {

		try {
			addClient(request);

			try {
				boolean withBib = ValidazioneDati.isFilled(cd_bib);

				if (!ValidazioneDati.isFilled(username))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "userId");

				if (!ValidazioneDati.isFilled(password))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "password");

				String codPolo = getPolo().getInfoPolo().getCd_polo();
				String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, " XX", InetAddress.getLocalHost());

				//controllo esistenza utente
				String codUtente = ServiziUtil.espandiCodUtente(username);
				UtenteBaseVO utente = getServizi().getUtente(ticket, codUtente);
				if (utente == null)
					//utente non trovato
					return Response.status(Status.NOT_FOUND).entity(ConversioneHibernateVO.toXML()
							.formattaErrore(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND, "utente non trovato: " + username))
							.build();

				codUtente = utente.getCodUtente();	//sostituzione eventuale cod.fiscale con user-id

				//check password
				PasswordEncrypter crypt = new PasswordEncrypter(password);
				String encrypted = crypt.encrypt(password);

				boolean authorized = ValidazioneDati.equals(utente.getPassword(), encrypted);
				log.debug(String.format("tentativo login per utente: %s, authorized: %b", codUtente, authorized));

				if (!authorized)
					return Response.status(Status.UNAUTHORIZED).entity(ConversioneHibernateVO.toXML()
							.formattaErrore(SbnErrorTypes.USER_NOT_AUTHORIZED, "password errata"))
							.build();

				if (withBib) {
					//controllo iscrizione alla biblioteca
					String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
					RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
					ricerca.setCodPolo(codPolo);
					ricerca.setCodBib(codBib);
					ricerca.setCodUte(codUtente);
					//dettaglio utente
					UtenteBibliotecaVO utenteBib = getServizi().getDettaglioUtente(ticket, ricerca, null, Locale.getDefault());

					if (utenteBib == null)
						//utente non iscritto
						return Response.status(Status.NOT_FOUND).entity(ConversioneHibernateVO.toXML()
								.formattaErrore(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND,
										"utente " + codUtente + " non iscritto alla biblioteca: " + codBib)).build();

					//controllo su utente auto-registrato
					String codAut = utenteBib.getAutorizzazioni().getCodTipoAutor();
					log.debug(String.format("utente: %s, codice tipo autorizzazione: '%s'", codUtente, codAut));

					if (ValidazioneDati.isFilled(codAut)) {
						AutorizzazioneVO aut = getServizi().getTipoAutorizzazione(ticket, codPolo, codBib, codAut);

						boolean selfregistered = (aut != null && ValidazioneDati.equals(aut.getAutomaticoPer(), "*"));
						log.debug(String.format("utente: %s, selfregistered: %b", codUtente, selfregistered));
						if (selfregistered) {
							boolean escludiAutoregistrati = Boolean.parseBoolean(CommonConfiguration
									.getProperty(Configuration.SRV_ACCESSO_UTENTE_ESCLUDI_AUTOREGISTRATI, "true"));
							if (escludiAutoregistrati)
								return Response.status(Status.UNAUTHORIZED).entity(ConversioneHibernateVO.toXML()
										.formattaErrore(SbnErrorTypes.USER_NOT_AUTHORIZED, "utente auto-registrato")).build();
						}
					}

				}

				UtenteType ut = ConversioneHibernateVO.toXML().utente(utente);

				SbnwebType sbn = new SbnwebType();
				EsitoType esito = new EsitoType();
				esito.setReturnCode(0);
				esito.setNumeroRighe(1);
				esito.setMessage("OK");
				sbn.setEsito(esito);
				sbn.setUtente(ut);

				return Response.ok().entity(sbn).build();

			} catch (SbnBaseException e) {
				return Response.status(Status.BAD_REQUEST).entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

			} catch (Exception e) {
				throw new WebApplicationException(e);
			}

		} finally {
			removeClient();
		}
	}


}
