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
package it.iccu.sbn.ejb.domain.servizi.calendario;

import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface Calendario extends EJBObject {

	ModelloCalendarioVO aggiornaModelloCalendario(String ticket, ModelloCalendarioVO modello) throws SbnBaseException, RemoteException;

	ModelloCalendarioVO getCalendarioBiblioteca(String ticket, String codPolo, String codBib) throws SbnBaseException, RemoteException;

	ModelloCalendarioVO getCalendarioCategoriaMediazione(String ticket, String codPolo, String codBib,
			String cd_cat_mediazione) throws SbnBaseException, RemoteException;

	ModelloCalendarioVO getCalendarioSala(String ticket, SalaVO sala) throws SbnBaseException, RemoteException;

	CalendarioVO cancellaModelloCalendario(String ticket, ModelloCalendarioVO mc) throws SbnBaseException, RemoteException;

	List<GiornoVO> getGrigliaCalendario(String ticket, RicercaGrigliaCalendarioVO ricerca) throws SbnBaseException, RemoteException;

}
