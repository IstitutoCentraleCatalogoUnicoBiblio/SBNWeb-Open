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

import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


public abstract class AbstractControlloBase implements ControlloFaseIter {

	protected Logger logger = Logger.getLogger(this.getClass());
	protected ControlloFaseEJBDelegate delegate;

	public AbstractControlloBase() throws RemoteException, NamingException, CreateException {
		super();
		delegate = ControlloFaseEJBDelegate.getInstance();
	}

	public abstract Result check(FaseControlloIterVO controlloIter, DatiControlloVO dati) throws Exception;

}
