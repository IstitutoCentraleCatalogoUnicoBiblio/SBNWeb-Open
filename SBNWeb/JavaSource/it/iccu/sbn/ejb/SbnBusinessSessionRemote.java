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
package it.iccu.sbn.ejb;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;

import java.rmi.RemoteException;

public interface SbnBusinessSessionRemote {

	// gestione blocchi
	public abstract DescrittoreBloccoVO nextBlocco(String ticket,
			String idLista, int numBlocco) throws RemoteException,
			ValidationException;

	public abstract void clearBlocchiIdLista(String ticket, String idLista)
			throws RemoteException, ValidationException;

	public abstract void clearBlocchiAll(String ticket) throws RemoteException,
			ValidationException;

}
