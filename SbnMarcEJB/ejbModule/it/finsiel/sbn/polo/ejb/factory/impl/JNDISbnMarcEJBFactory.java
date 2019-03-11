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
package it.finsiel.sbn.polo.ejb.factory.impl;

import it.finsiel.gateway.local.SbnMarcLocalGateway;
import it.finsiel.sbn.polo.ejb.Profiler;
import it.finsiel.sbn.polo.ejb.factory.SbnMarcEJBFactoryIntf;

import javax.naming.Context;
import javax.naming.InitialContext;

public class JNDISbnMarcEJBFactory implements SbnMarcEJBFactoryIntf {

	private Profiler profiler;
	private SbnMarcLocalGateway gateway;

	public JNDISbnMarcEJBFactory() {
		try {
			Context ctx = new InitialContext();
			profiler = (Profiler) ctx.lookup(Profiler.JNDI_NAME);
			gateway = (SbnMarcLocalGateway) ctx.lookup(SbnMarcLocalGateway.JNDI_NAME);
		} catch (Exception e) {
		}
	}

	public Profiler getProfiler() throws Exception {
		return profiler;
	}

	public SbnMarcLocalGateway getGateway() throws Exception {
		return gateway;
	}
}
