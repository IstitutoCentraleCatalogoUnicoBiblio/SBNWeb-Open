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
package it.iccu.sbn.ejb.remote;

import it.iccu.sbn.ejb.model.tree.TreeElement;
import it.iccu.sbn.web.vo.DescrizioneFunzioneVO;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

public interface Menu extends EJBObject {
    TreeElement getRootMenu() throws RemoteException;

    boolean isUserEnabled(String codiceUtente, String codiceBiblio,
            String menuKey) throws RemoteException;

    public Map<String, List<DescrizioneFunzioneVO>> getFunzioni(String ticket) throws RemoteException;

}
