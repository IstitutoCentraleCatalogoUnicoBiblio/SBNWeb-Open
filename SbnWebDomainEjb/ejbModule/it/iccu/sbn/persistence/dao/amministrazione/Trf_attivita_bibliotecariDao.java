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
package it.iccu.sbn.persistence.dao.amministrazione;

import it.iccu.sbn.persistence.dao.common.DaoManager;

public class Trf_attivita_bibliotecariDao extends DaoManager {

	public Trf_attivita_bibliotecariDao() {
		super();
	}
/*
	public List<Trf_attivita_bibliotecari> select(int id_bibliotecario) throws DaoManagerException
	{
		Tbf_bibliotecarioDao bibliotecario = new Tbf_bibliotecarioDao();
		Tbf_bibliotecario tbf_bibliotecario = bibliotecario.select(id_bibliotecario);

		Trf_attivita_bibliotecari attivita_bibliotecari = new Trf_attivita_bibliotecari();
		attivita_bibliotecari.setId_bibliotecario(tbf_bibliotecario);
		Session session = this.getCurrentSession();
		Criteria attivita = session.createCriteria(Trf_attivita_bibliotecari.class);
		attivita.add(Example.create(attivita_bibliotecari));
		List<Trf_attivita_bibliotecari> results = (List<Trf_attivita_bibliotecari>)attivita.list();
		return results;
	}
	*/
}
