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

import it.finsiel.gateway.local.SbnMarcLocalGateway;
import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.jndi.JNDIUtil;

import javax.naming.InitialContext;

public class FactorySbnEJB extends AbstractFactorySbnImpl {

	private static final long serialVersionUID = 7396253703425793089L;

	private static volatile SbnMarcLocalGateway gateway;


	private SbnMarcLocalGateway getGateway() throws Exception {
		if (gateway == null) {
			InitialContext ctx = JNDIUtil.getContext();
			gateway = (SbnMarcLocalGateway) ctx.lookup(SbnMarcLocalGateway.JNDI_NAME);
		}

		return gateway;
	}


	public FactorySbnEJB(String tipoServer) {
		super(tipoServer);
	}

	@Override
	protected SBNMarc execute(SBNMarc request) throws Exception {
		try {
			return getGateway().execute(sbnRequest);

		} catch (Exception e) {
			gateway = null;
			throw e;
		}
	}


	@Override
	public void setMessage(SbnMessageType sbnmessage, SbnUserType user)
			throws SbnMarcException {

		try {
			super.setMessage(sbnmessage, user);
			CercaType cercaType = sbnmessage.getSbnRequest().getCerca();
			if (cercaType != null) {
				//almaviva5_20120123 fix per modifica (illecita) dell'istanza sbnmarc da parte del protocollo
				SbnMessageType clone = ClonePool.deepCopy(sbnmessage);
				sbnRequest.setSbnMessage(clone);
			}

		} catch (SbnMarcException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			throw new SbnMarcException("httpId: " + httpId + " - Protocollo di " + this.tipoServer +
					": Impossibile validare il protocollo per l'invio", e);
		}
	}

}
