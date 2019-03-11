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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.services.Codici;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class ControlloFaseEJBDelegate {

	private static ControlloFaseEJBDelegate instance;

	private Codici codici;
	private Servizi servizi;
	private Inventario inventario;

	private ControlloFaseEJBDelegate() throws NamingException, RemoteException,
			CreateException {

		DomainEJBFactory factory = DomainEJBFactory.getInstance();
		this.codici = factory.getCodici();
		this.servizi = factory.getServizi();
		this.inventario = factory.getInventario();
	}

	synchronized public static ControlloFaseEJBDelegate getInstance()
			throws RemoteException, NamingException, CreateException {
		if (instance == null) {
			instance = new ControlloFaseEJBDelegate();
		}
		return instance;
	}

	public Codici getGestioneCodici() {
		return this.codici;
	}

	public Servizi getGestioneServizi() {
		return this.servizi;
	}

	public Inventario getInventario() {
		return inventario;
	}
}
