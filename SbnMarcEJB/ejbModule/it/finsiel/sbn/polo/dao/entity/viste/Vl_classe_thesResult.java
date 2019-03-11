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
package it.finsiel.sbn.polo.dao.entity.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_classeCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.viste.Vl_classe_the;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Vl_classe_thesResult extends Tb_classeCommonDao {

	public Vl_classe_thesResult(Vl_classe_the cla_the) {
		super();
		this.valorizzaParametro(cla_the.leggiAllParametro());
	}

	public Integer contaClassePerThesauro(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_classe_the.class);
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("DID", myOpzioni.get(KeyParameter.XXXdid)));
			myOpzioni.remove(KeyParameter.XXXdid);

			this.createCriteria(myOpzioni);
			ProjectionList prj = Projections.projectionList().add(Projections.rowCount());
			Integer result = (Integer) this.basicCriteria.setProjection(prj).uniqueResult();

			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}

	}

	public List<Vl_classe_the> selectClassePerThesauro(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_classe_the.class);
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("DID", myOpzioni.get(KeyParameter.XXXdid)));
			myOpzioni.remove(KeyParameter.XXXdid);

			this.basicCriteria = Parameter.setOrdinamento(myOpzioni, "Vl_classe_the", this.basicCriteria, session);

			List<Vl_classe_the> result = this.basicCriteria.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
}

}
