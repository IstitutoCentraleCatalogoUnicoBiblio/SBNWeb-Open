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
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpException;

public class FactorySbnHttpIndice extends FactorySbnHttp {

	private static final long serialVersionUID = 1075790360153227135L;

	private static final AtomicBoolean available = new AtomicBoolean(true);

	private static final Class<?>[] CONNECTION_ERRORS = new Class[] {
			HttpException.class,
			ConnectTimeoutException.class,
			SocketException.class,
			IOException.class };

	private static final SBNMarc buildNotAvailableXML() {
		SbnUserType user = new SbnUserType(); //utente fittizio
		user.setBiblioteca("      ");
		user.setUserId("      ");

		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		result.setEsito(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getEsito());
		result.setTestoEsito(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(user);

		sbnmarc.setSchemaVersion(SBNMarcUtil.getSchemaVersion());
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);

		return sbnmarc;
	}

	private static final SBNMarc buildTestXML() {
		SbnUserType user = new SbnUserType(); //utente fittizio
		user.setBiblioteca("      ");
		user.setUserId("      ");

		SBNMarc sbnmarc = new SBNMarc();
		CercaType cercaType = new CercaType();
		cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
		cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
		cercaType.setCercaSbnProfile(user);
		SbnRequestType sbnrequest = new SbnRequestType();
		sbnrequest.setCerca(cercaType);
		sbnmarc.setSbnUser(user);
		SbnMessageType sbnmessage = new SbnMessageType();
		sbnmessage.setSbnRequest(sbnrequest);
		sbnmarc.setSbnMessage(sbnmessage);
		sbnmarc.setSchemaVersion(SBNMarcUtil.getSchemaVersion());

		return sbnmarc;
	}

	public FactorySbnHttpIndice(String tipo, ServerHttp server) {
		super(tipo, server);
	}

	public static boolean isAvailable() {
		return available.get();
	}

	public static void setAvailable(boolean avail) {
		available.set(avail);
	}

	@Override
	protected SBNMarc execute(SBNMarc request) throws Exception {
		try {
			if (!available.get())
				return buildNotAvailableXML();

			return super.execute(request);
		}  catch (SbnMarcException e) {
			//almaviva5_20141217
			if (isConnectionError(e)) {
				//almaviva5_20150126 check disattivazione semaforo
				int timeout = CommonConfiguration.getPropertyAsInteger(Configuration.INDICE_CONNECTION_CHECKER_TIMEOUT, 0);
				if (timeout > 0) {
					available.set(false);
					log.warn("ATTENZIONE: Collegamento a INDICE non disponibile.");
					return buildNotAvailableXML();
				} else
					available.set(true);	//check disattivato
			}

			throw e;
		}
	}

	private boolean isConnectionError(SbnMarcException e) {
		Throwable cause = e.getCause();
		if (cause == null)
			return false;

		for (Class<?> clazz : CONNECTION_ERRORS)
			if (clazz.isInstance(cause))
				return true;

		return false;
	}

	public static void check() throws Exception {
		try {
			AmministrazionePolo polo = DomainEJBFactory.getInstance().getPolo();
			FactorySbnHttp factory = new FactorySbnHttp("INDICE", new ServerHttp(polo.getIndice(), polo.getCredentials()));
			factory.execute(buildTestXML());
			available.set(true);
			log.debug("Collegamento a INDICE ripristinato.");
		} catch (Exception e) {
			available.set(false);
		}
	}

}
