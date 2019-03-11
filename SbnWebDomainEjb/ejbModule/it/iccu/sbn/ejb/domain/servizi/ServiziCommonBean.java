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
package it.iccu.sbn.ejb.domain.servizi;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;



public abstract class ServiziCommonBean implements javax.ejb.SessionBean {

	private static final long serialVersionUID = -2006748793575948567L;

	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
		return;
	}

	public int getNumeroMovimentiAttivi(String codPolo, String codBibInv,
			String codSerieInv, int codInven) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			return richiesteDAO.getNumeroMovimentiAttivi(codPolo, codBibInv, codSerieInv, codInven);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

}
