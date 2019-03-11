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
import it.iccu.sbn.SbnMarcFactory.util.BuildSbnMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.util.AtomicCyclicCounter;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.ConfigChangeInterceptor;
import it.iccu.sbn.util.config.Configuration;

import java.util.Set;

import org.exolab.castor.xml.ValidationException;
import org.jboss.logging.Logger;

public abstract class AbstractFactorySbnImpl extends SerializableVO implements
		FactorySbn, ConfigChangeInterceptor {

	private static final long serialVersionUID = 9190605693366942010L;
	private static final AtomicCyclicCounter _tid_counter = new AtomicCyclicCounter();

	protected static Logger log = Logger.getLogger("sbnmarc");

	protected SBNMarc sbnRequest;
	protected SbnUserType _user;

	protected final String tipoServer;
	protected String httpId = "000000000";
	protected int limit;
	protected BuildSbnMarc build = new BuildSbnMarc();

	protected static final String getTransactionId() {
		int id = _tid_counter.getNextValue();
		return String.format("%09d", id);
	}

	public AbstractFactorySbnImpl(String tipoServer) {
		super();
		this.tipoServer = toUpperCase(tipoServer);
		CommonConfiguration.addInterceptor(this, Configuration.MAX_RESULT_ROWS);
		setLimit();
	}

	public void onConfigPropertyChange(String key) throws Exception {
		return;
	}

	public void onConfigReload(Set<String> changedProperties) throws Exception {
		setLimit();
	}

	private void setLimit() {
		try {
			this.limit = CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, 0);
		} catch (Exception e) {
			this.limit = 0;
		}
	}

	public void setMessage(SbnMessageType sbnmessage, SbnUserType user)	throws SbnMarcException {
		this._user = user;
		sbnRequest = build.creaIntestazione(this._user);
		sbnRequest.setSbnMessage(sbnmessage);
		this.httpId = getTransactionId();

		try {
			CercaType cercaType = sbnmessage.getSbnRequest().getCerca();
			//almaviva5_20111003 limit response
			if (cercaType != null && limit > 0) // è una ricerca?
				cercaType.setLimit(limit);

			sbnRequest.validate();

		} catch (ValidationException e) {
			log.error("", e);
			throw new SbnMarcException("httpId: " + httpId + " - Protocollo di " + this.tipoServer +
					": Impossibile validare il protocollo per l'invio", e);
		}
	}

	protected abstract SBNMarc execute(SBNMarc request) throws Exception;

	public final SBNMarc eseguiRichiestaServer() throws SbnMarcException {
		long start = System.currentTimeMillis();
		try {
			if (log.isDebugEnabled()) {
				log.debug("httpId: " + httpId + " - userid: " + String.format("%1$-6s", _user.getUserId()) + " - INIZIO TRANSAZIONE " + tipoServer);
				String richiestaAlServer = this.build.marshalSbnMarc(sbnRequest);
				log.info("httpId: " + httpId + " - XML RICHIESTA ("	+ tipoServer + "): " + richiestaAlServer);
			}

			SBNMarc response = execute(sbnRequest);

			if (log.isDebugEnabled()) {
				String rispostaDalServer = this.build.marshalSbnMarc(response);
				log.info("httpId: " + httpId + " - XML RISPOSTA  (" + tipoServer + "): " + rispostaDalServer);
			}

			if (response != null
					&& response.getSbnMessage() != null
					&& response.getSbnMessage().getSbnResponse() != null
					&& response.getSbnMessage().getSbnResponse().getSbnResult() != null) {

				// Inizio almaviva2 03.08.2010 - Modifica per nuova messaggistica riportata dal software di Indice
				// se il codice ritorno è 0000 non si deve mettere Protocollo di Polo perchè dovrebbe essere una informativa
				if (!response.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					response.getSbnMessage().getSbnResponse().getSbnResult().setTestoEsito("Protocollo di "	+ this.tipoServer + ": "
									+ response.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				}
				// Fine almaviva2 03.08.2010 - Modifica per nuova messaggistica riportata dal software di Indice

			}

			return response;

		} catch (SbnMarcException e) {
			throw e;
		} catch (Exception e) {
			throw new SbnMarcException("Impossibile validare il protocollo per l'invio", e);
		} finally {
			if (log.isDebugEnabled()) {
				double end = (double)(System.currentTimeMillis() - start) / 1000;
				log.debug("httpId: " + httpId + " - userid: " + String.format("%1$-6s", _user.getUserId())
						+ " - FINE   TRANSAZIONE " + tipoServer + " (in "
						+ String.format("%.3f", end) + " secondi)");
			}
		}

	}

}
