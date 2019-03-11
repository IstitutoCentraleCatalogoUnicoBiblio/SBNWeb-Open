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
package it.finsiel.sbn.polo.dao.entity.tavole;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_classeCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_sistemi_classi_biblioteche;

import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Tr_sistemi_classi_bibliotecheResult extends Tb_classeCommonDao {

	private final Tr_sistemi_classi_biblioteche scb;

	public Tr_sistemi_classi_bibliotecheResult(Tr_sistemi_classi_biblioteche scb) {
		super();
		this.valorizzaParametro(scb.leggiAllParametro());
		this.scb = scb;
	}

	//almaviva5_20120702 #5032
	public Integer verificaEdizioneDeweyBiblioteca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.add(Restrictions.ne("FL_CANC", "S"));
			this.basicCriteria.add(Restrictions.eq("FLG_ATT", "1"));
			this.basicCriteria.add(Restrictions.eq("CD_POLO", myOpzioni.get(KeyParameter.XXXcd_polo)));
			this.basicCriteria.add(Restrictions.eq("CD_BIBLIOTECA", myOpzioni.get(KeyParameter.XXXcd_biblioteca)));
			this.basicCriteria.add(Restrictions.eq("CD_SISTEMA", myOpzioni.get(KeyParameter.XXXcd_sistema)));
			this.basicCriteria.add(Restrictions.eq("CD_EDIZIONE", myOpzioni.get(KeyParameter.XXXcd_edizione)));

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(Projections.rowCount())).uniqueResult();
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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_sistemi_classi_biblioteche.class;
	}

}
