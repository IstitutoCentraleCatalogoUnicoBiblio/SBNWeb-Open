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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_termine_thesauroCommonDao;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_the_cla;

import java.util.HashMap;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Tr_the_claResult extends Tb_termine_thesauroCommonDao {

	private final Tr_the_cla tr_the_cla;

	public Tr_the_claResult(Tr_the_cla tr_the_cla) {
		super();
		this.tr_the_cla = tr_the_cla;
		this.valorizzaParametro(tr_the_cla.leggiAllParametro());
	}

	public List<Tr_the_cla> getLegameTermineClasse(HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			//this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("DID", myOpzioni.get(KeyParameter.XXXdid)));
			this.basicCriteria.add(Restrictions.eq("CD_SISTEMA", myOpzioni.get(KeyParameter.XXXcd_sistema)));
			this.basicCriteria.add(Restrictions.eq("CD_EDIZIONE", myOpzioni.get(KeyParameter.XXXcd_edizione)));
			this.basicCriteria.add(Restrictions.eq("CLASSE", myOpzioni.get(KeyParameter.XXXclasse)));

			myOpzioni.remove(KeyParameter.XXXdid);
			myOpzioni.remove(KeyParameter.XXXcd_sistema);
			myOpzioni.remove(KeyParameter.XXXcd_edizione);
			myOpzioni.remove(KeyParameter.XXXclasse);

			List<Tr_the_cla> result = this.basicCriteria.list();

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

	public void save(Tr_the_cla row) throws InfrastructureException {
        Session session = this.getSession();
        this.beginTransaction();
        session.saveOrUpdate(row);
        this.commitTransaction();
        this.closeSession();
	}

	public Tr_the_cla get() throws InfrastructureException {
        Session session = this.getSession();
        this.beginTransaction();
        this.basicCriteria = session.createCriteria(getTarget());
        this.basicCriteria.add(Restrictions.eq("CD_THE", tr_the_cla.getCD_THE()) );
        this.basicCriteria.add(Restrictions.eq("DID", tr_the_cla.getDID()) );
        this.basicCriteria.add(Restrictions.eq("CD_SISTEMA", tr_the_cla.getCD_SISTEMA() + tr_the_cla.getCD_EDIZIONE()));
        this.basicCriteria.add(Restrictions.eq("CD_EDIZIONE", tr_the_cla.getCD_EDIZIONE()) );
        this.basicCriteria.add(Restrictions.eq("CLASSE", tr_the_cla.getCLASSE()) );

        Tr_the_cla result = (Tr_the_cla) this.basicCriteria.uniqueResult();

        this.commitTransaction();
        this.closeSession();

        return result;
	}

	public Integer contaClassePerThesauro(HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("CD_SISTEMA", tr_the_cla.getCD_SISTEMA()));
		    this.basicCriteria.add(Restrictions.eq("CD_EDIZIONE", tr_the_cla.getCD_EDIZIONE()) );
		    this.basicCriteria.add(Restrictions.eq("CLASSE", tr_the_cla.getCLASSE()) );

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

	public List<Tr_the_cla> rankLegameTermineClasse1(HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.setLockMode(LockMode.UPGRADE);
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("DID", myOpzioni.get(KeyParameter.XXXdid)));

			String cdSistema = ValidazioneDati.trimOrEmpty((String) myOpzioni.get(KeyParameter.XXXcd_sistema));
			String cdEdizione = ValidazioneDati.trimOrEmpty((String) myOpzioni.get(KeyParameter.XXXcd_edizione));
			String classe = ValidazioneDati.trimOrEmpty((String) myOpzioni.get(KeyParameter.XXXclasse));
			StringBuilder clKey = new StringBuilder(32);
			clKey.append(cdSistema).append(cdEdizione).append(cdEdizione).append(classe);
			this.basicCriteria.add(Restrictions.ne("CL_KEY", clKey.toString()) );

			this.basicCriteria.add(Restrictions.ge("POSIZIONE", myOpzioni.get(KeyParameter.XXXposizione)));

			this.basicCriteria.addOrder(Order.asc("POSIZIONE"));

			myOpzioni.remove(KeyParameter.XXXdid);
			myOpzioni.remove(KeyParameter.XXXcd_sistema);
			myOpzioni.remove(cdEdizione);
			myOpzioni.remove(KeyParameter.XXXclasse);
			myOpzioni.remove(KeyParameter.XXXposizione);

			List<Tr_the_cla> result = this.basicCriteria.list();

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

	public List<Tr_the_cla> rankLegameTermineClasse2(HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.setLockMode(LockMode.UPGRADE);
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("DID", myOpzioni.get(KeyParameter.XXXdid)));

			String cdSistema = ValidazioneDati.trimOrEmpty((String) myOpzioni.get(KeyParameter.XXXcd_sistema));
			String cdEdizione = ValidazioneDati.trimOrEmpty((String) myOpzioni.get(KeyParameter.XXXcd_edizione));
			String classe = ValidazioneDati.trimOrEmpty((String) myOpzioni.get(KeyParameter.XXXclasse));
			StringBuilder clKey = new StringBuilder(32);
			clKey.append(cdSistema).append(cdEdizione).append(cdEdizione).append(classe);
			this.basicCriteria.add(Restrictions.ne("CL_KEY", clKey.toString()) );

			this.basicCriteria.add(Restrictions.eq("POSIZIONE", myOpzioni.get(KeyParameter.XXXposizione)));

			this.basicCriteria.addOrder(Order.asc("POSIZIONE"));

			myOpzioni.remove(KeyParameter.XXXdid);
			myOpzioni.remove(KeyParameter.XXXcd_sistema);
			myOpzioni.remove(cdEdizione);
			myOpzioni.remove(KeyParameter.XXXclasse);
			myOpzioni.remove(KeyParameter.XXXposizione);

			List<Tr_the_cla> result = this.basicCriteria.list();

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
		return Tr_the_cla.class;
	}


}
