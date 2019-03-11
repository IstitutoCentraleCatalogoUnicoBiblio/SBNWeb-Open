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

import it.iccu.sbn.exception.ws.SbnWsExceptionMapper;
import it.iccu.sbn.webservices.documentofisico.impl.WSDocumentoFisicoRESTImpl;
import it.iccu.sbn.webservices.periodici.impl.WSPeriodiciRESTImpl;
import it.iccu.sbn.webservices.servizi.impl.WSServiziILLRESTImpl;
import it.iccu.sbn.webservices.servizi.impl.WSServiziRESTImpl;
import it.iccu.sbn.webservices.servizi.impl.WSUtenteLettoreRESTImpl;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class WSInvocationEntryPoint extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public WSInvocationEntryPoint() {
		this.singletons.add(new SbnWsExceptionMapper());

		this.classes.add(WSServiziRESTImpl.class);
		this.classes.add(WSUtenteLettoreRESTImpl.class);
		this.classes.add(WSPeriodiciRESTImpl.class);
		this.classes.add(WSDocumentoFisicoRESTImpl.class);
		//almaviva5_20140731 evolutive ill
		this.classes.add(WSServiziILLRESTImpl.class);
	}

	public Set<Class<?>> getClasses() {
		return this.classes;
	}

	public Set<Object> getSingletons() {
		return this.singletons;
	}

}
