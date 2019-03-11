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

import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.AreaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.GruppoVO;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

public interface GestioneDefault extends EJBObject {

    public List<GruppoVO> getDefaultUtente(int Utente, String idArea, Map attivita, String idBiblioteca, String idPolo) throws RemoteException;

    public Map<String, String> getTuttiDefaultUtente(String idBiblioteca, String idPolo, Map attivita) throws RemoteException;

    public boolean setDefaultUtente(int idUtente, List<GruppoVO> campi, String idBiblioteca, String idPolo) throws RemoteException;

    public List<AreaVO> getAreeUtente(Map attivita) throws RemoteException;

}
