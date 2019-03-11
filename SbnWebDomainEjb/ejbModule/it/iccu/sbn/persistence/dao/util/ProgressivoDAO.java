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
package it.iccu.sbn.persistence.dao.util;

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class ProgressivoDAO extends DaoManager {

	private static Logger log = Logger.getLogger(ProgressivoDAO.class);

	public int calcolaIdPrenotazioneBatch() throws DaoManagerException {
		Session session = this.getCurrentSession();
		SQLQuery query = (SQLQuery) session.getNamedQuery("calcolaIdPrenotazioneBatch");
		Long seq = (Long) query.uniqueResult();
		log.debug("id progressivo batch: " + seq.intValue());
		return seq.intValue();
	}

}
