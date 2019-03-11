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
package it.iccu.sbn.SbnMarcFactory.factory;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.utils.ValidazioneDati;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;


public class FactorySbnHttp extends AbstractFactorySbnImpl {

	private static final long serialVersionUID = 2564052775994671202L;

	private String rispostaDalServer;
	private String richiestaAlServer;
	private ServerHttp server;


	public FactorySbnHttp(String tipo, ServerHttp server) {
		super(tipo);
		this.server = server;
		this.server.setTipo(tipo);
	}

	private void sendHttp(SBNMarc sbnRichiesta) throws ValidationException,
			SbnMarcException, Exception {
		richiestaAlServer = this.build.marshalSbnMarc(sbnRichiesta);
	/*
		if (tipoServer == null)
			log.info("httpId: " + httpId + " - XML RICHIESTA: " + richiestaAlServer);
		else
			log.info("httpId: " + httpId + " - XML RICHIESTA (" + tipoServer + "): " + richiestaAlServer);
	*/
		this.server.setTransactionId(httpId);
		rispostaDalServer = this.server.esecuzioneRichiesta(richiestaAlServer);
	/*
		if (tipoServer == null)
			log.info("httpId: " + httpId + " - XML RISPOSTA:  " + rispostaDalServer);
		else
			log.info("httpId: " + httpId + " - XML RISPOSTA  (" + tipoServer + "): " + rispostaDalServer);
	*/
	}

	private String checkResponse() throws SbnMarcException {
		int codiceHttp = this.server.getHttpReturnCode();

		String httpErr = null;
		if ((codiceHttp != HttpStatus.SC_OK) && (codiceHttp > 0)) {
			httpErr = +codiceHttp + ": " + HttpStatus.getStatusText(codiceHttp);
			log.error("httpId: " + httpId + " - " +tipoServer + ": ERRORE " + httpErr);
			throw new SbnMarcException("httpId: " + httpId + " - " + tipoServer + ": ERRORE " + httpErr);

		} else if (rispostaDalServer == null) {
			httpErr = String.valueOf(codiceHttp);
			log.error("httpId: " + httpId + " - " + tipoServer + ":Risposta dal server non ricevuta a causa di timeout di rete" + httpErr);
			throw new SbnMarcException("httpId: " + httpId + " - " + tipoServer + ": Risposta dal server non ricevuta a causa di timeout di rete" + httpErr);
		}
		return httpErr;
	}

	private SBNMarc receiveHttp() throws SbnMarcException, ValidationException {
		SBNMarc sbnRisposta = null;
		String messaggioErroreHTTP = this.checkResponse();

		if (messaggioErroreHTTP == null) {
			try {
				sbnRisposta = this.build.unmarshalSbnMarc(rispostaDalServer);
			} catch (MarshalException e) {
				log.error(tipoServer + ": Errore formato XML:\n" + rispostaDalServer);
				String msg = ValidazioneDati.trimOrEmpty(e.getLocalizedMessage());
				int pos = StringUtils.ordinalIndexOf(msg, "\n", 3);
				msg = pos > 0 ? msg.substring(0, pos) : msg;
				throw new SbnMarcException(tipoServer + ": Errore formato XML: " + msg, e);
			}
		}
		return sbnRisposta;
	}

	@Override
	protected SBNMarc execute(SBNMarc request) throws Exception {

		SBNMarc sbnRisposta = null;

		try {
			this.sendHttp(this.sbnRequest != null ? this.sbnRequest : request);
		} catch (ValidationException e) {
			log.error("", e);
			throw new SbnMarcException("httpId: " + httpId + " - Protocollo di " + this.tipoServer + ": Impossibile validare il protocollo per l'invio", e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw e;
		} catch (Exception e) {
			log.error("", e);
			throw new SbnMarcException("httpId: " + httpId + " - Protocollo di " + this.tipoServer + ": Errore generico", e);
		}

		try {
			sbnRisposta = this.receiveHttp();
		} catch (ValidationException e) {
			log.error("", e);
			throw new SbnMarcException("httpId: " + httpId + " - Protocollo di " + this.tipoServer
					+ ": Impossibile validare il protocollo di ricezione",	e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw e;
		}

		return sbnRisposta;

	}

}
