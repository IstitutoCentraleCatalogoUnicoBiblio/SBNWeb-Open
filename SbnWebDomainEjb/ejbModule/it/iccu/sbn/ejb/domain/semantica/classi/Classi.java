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
package it.iccu.sbn.ejb.domain.semantica.classi;

import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.vo.custom.semantica.UserMessage;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface Classi extends EJBObject {

	public List<UserMessage> consumeMessages(String ticket) throws RemoteException;

	public CreaVariaClasseVO importaClasseDaIndice(CreaVariaClasseVO richiesta,	String ticket) throws DAOException, RemoteException;
}
