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
package it.iccu.sbn.ejb.domain.amministrazione;

import it.finsiel.gateway.local.SbnMarcLocalGateway;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.jndi.JNDIUtil;

import javax.naming.InitialContext;

public class AmministrazioneBaseBean extends TicketChecker {

	private static final long serialVersionUID = 327505380805643739L;

	private static SbnMarcLocalGateway gateway;

	public AmministrazioneBaseBean() {
		super();
	}

	protected SbnMarcLocalGateway getGateway() throws Exception {
		if (gateway == null) {
			InitialContext ctx = JNDIUtil.getContext();
			gateway = (SbnMarcLocalGateway) ctx.lookup(SbnMarcLocalGateway.JNDI_NAME);
		}

		return gateway;
	}

	protected void resetGateway() {
		gateway = null;
	}

}
