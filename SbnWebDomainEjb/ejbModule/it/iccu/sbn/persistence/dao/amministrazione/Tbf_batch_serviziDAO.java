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
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_batch_servizi;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_coda_jms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Tbf_batch_serviziDAO extends DaoManager {

	public Tbf_batch_serviziDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Tbf_batch_servizi> selectAll() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_batch_servizi.class);
		criteria.add(Restrictions.ne("fl_canc", "S"));
		List<Tbf_batch_servizi> ret = criteria.list();
		return ret;
	}

	public List<Tbf_coda_jms> selectCodeJMS(boolean all) throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_coda_jms.class);
		if (!all)
			criteria.add(Restrictions.isNotEmpty("Tbf_batch_servizi"));
		return criteria.list();

	}

	public Tbf_batch_servizi selectBatchByCodAttivita(String codAttivita) throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_batch_servizi.class);
		criteria.add(Restrictions.eq("cd_attivita.id", codAttivita));
		criteria.add(Restrictions.ne("fl_canc", "S"));
		return (Tbf_batch_servizi) criteria.uniqueResult();
	}

	public Tbf_coda_jms selectCodeJMS(int idCoda) throws DaoManagerException {
		Session session = this.getCurrentSession();
		return (Tbf_coda_jms) session.get(Tbf_coda_jms.class, new Integer(idCoda) );
	}

	public List<String> selectCodeJMSOutput() throws DaoManagerException {

		List<String> output = new ArrayList<String>();
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_batch_servizi.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("nome_coda_output"));
		projList.add(Projections.groupProperty("nome_coda_output"));
		criteria.setProjection(projList);

		for (Object o : criteria.list()) {
			String queue = (String) ((Object[])o)[0];
			output.add(queue);
		}

		return output;
	}

}
