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
package it.iccu.sbn.webservices;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public abstract class WSRESTBaseImpl {

	private static Logger log = Logger.getLogger(WSRESTBaseImpl.class);

	private static Semaphore clients;
	static int max;

	static {
		try {
			max = CommonConfiguration.getPropertyAsInteger(Configuration.WS_MAX_CONCURRENT_CLIENTS, 10);
		} catch (Exception e) {
			max = 10;	//hard-coded
		}
		clients = new Semaphore(max);
	}

	protected static final void addClient(HttpServletRequest request) throws ApplicationException {
		try {
			clients.acquire();
			int cnt = max - clients.availablePermits();
			log.debug(String.format("client %d, richiesta da ip: %s", cnt, request.getRemoteAddr()));

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
		}
	}

	protected static final void removeClient() {
		clients.release();
	}

	protected final String formattaErrore(SbnBaseException e) {
		SbnErrorTypes error = e.getErrorCode();
		StringBuilder msg = new StringBuilder(512);
		msg.append(error.name());
		String[] labels = e.getLabels();
		if (ValidazioneDati.isFilled(labels)) {
			msg.append(" (");
			msg.append(ValidazioneDati.formatValueList(Arrays.asList(labels), ", "));
			msg.append(")");
		}
		return msg.toString();
	}

	public WSRESTBaseImpl() {
		super();
	}

}
