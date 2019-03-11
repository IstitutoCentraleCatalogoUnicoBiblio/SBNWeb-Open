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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_abstractCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tb_abstractResult extends Tb_abstractCommonDao{
	private Tb_abstract tb_abstract= null;
    public Tb_abstractResult(Tb_abstract tb_abstract) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_abstract.leggiAllParametro());
        this.tb_abstract = tb_abstract;
    }
	public Tb_abstractResult() {
        // TODO Auto-generated constructor stub
    }

	/**
	<statement nome="selectPerKey" tipo="select" id="02">
			<fisso>
				WHERE
				fl_canc !='S' AND
				  bid = XXXbid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_abstract> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());

            Filter filter = session.enableFilter("TB_ABSTRACT_selectPerKey");

			filter.setParameter(Tb_abstractCommonDao.XXXbid, opzioni
					.get(Tb_abstractCommonDao.XXXbid));

			myOpzioni.remove(Tb_abstractCommonDao.XXXbid);
			List<Tb_abstract> result = this.basicCriteria
					.list();



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





  public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_titolo metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_abstract.setTS_INS(now);
        tb_abstract.setTS_VAR(now);
        session.saveOrUpdate(this.tb_abstract);
        this.commitTransaction();
        this.closeSession();
	}


  /**
   * 	<statement nome="update" tipo="update" id="43_taymer">
			<fisso>
				UPDATE Tb_titolo
				 SET
				 bid char(10) NOT NULL,
				 ds_abstract varchar(2160) NOT NULL,
				 cd_livello char(2) NOT NULL,
				 ute_ins char(12) NOT NULL,
				 ts_ins timestamp(6) NOT NULL,
				 ute_var char(12) NOT NULL,
				 ts_var timestamp(6) NOT NULL,
				 fl_canc char(1) NOT NULL,
				 WHERE
				  bid = XXXbid and fl_canc != 's'
			</fisso>
	</statement>

   * @param opzioni
 * @throws InfrastructureException
   */

  public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("tb_abstract metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_abstract",Tb_abstract.class);

		buildUpdate.addProperty("ds_abstract",this.getParametro().get(TableDao.XXXds_abstract));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(TableDao.XXXcd_livello));
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}

  public void cancellaAbstract(Object opzioni) throws InfrastructureException{
      log.debug("Tb_abstractResult metodo cancellaAbstract");
      Session session = this.getSession();
      this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_abstract",Tb_abstract.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
  }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_abstract.class;
	}

}
