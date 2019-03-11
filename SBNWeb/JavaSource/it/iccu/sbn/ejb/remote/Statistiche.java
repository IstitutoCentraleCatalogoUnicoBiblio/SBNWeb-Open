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

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.statistiche.AreaVO;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface Statistiche extends javax.ejb.EJBObject{

    public List<AreaVO> getAreeUtente(Map attivita) throws RemoteException;
	public DescrittoreBloccoVO getListaStatistiche(String area, String ticket, int elemBlocco) throws RemoteException;
	public List<DettVarStatisticaVO> getDettVarStatistica(int idConfig, String ticket) throws RemoteException;
	public List<CodiceVO> getListaCodDescrDaTabGenerica(String codPolo, String valori0/*codBib*/, String valori1/*tabella*/,
			 String  valori2/*codice*/,  String valori3/*descrizione*/, String valori4/*valore codBib*/, String ticket) throws RemoteException;
}
