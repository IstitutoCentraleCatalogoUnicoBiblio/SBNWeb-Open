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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_marcaCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tb_marcaResult extends Tb_marcaCommonDao{

    private Tb_marca tb_marca;

    public Tb_marcaResult(Tb_marca tb_marca) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_marca.leggiAllParametro());
        this.tb_marca = tb_marca;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="jenny_01">
			<fisso>
				WHERE
				  mid = XXXmid
				AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_marca> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MARCA_selectPerKey");

			filter.setParameter(Tb_marcaCommonDao.XXXmid, opzioni
					.get(Tb_marcaCommonDao.XXXmid));

			myOpzioni.remove(Tb_marcaCommonDao.XXXmid);
			List<Tb_marca> result = this.basicCriteria
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

	/**
	<statement nome="selectPerParoleNome" tipo="select" id="jenny_02">
			<fisso>
				WHERE
				  	CONTAINS(ds_marca, XXXparola1 ) &gt; 0
				AND fl_canc != 'S'
		  	</fisso>
			<opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXdata_ins_A </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_marca> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			//Filter filter = session.enableFilter("TB_MARCA_selectPerParoleNome");
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_MARCA_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_MARCA_countPerParoleNome");
				filter.setParameter(Tb_marcaCommonDao.XXXparola1, opzioni.get(Tb_marcaCommonDao.XXXparola1));
				myOpzioni.remove(Tb_marcaCommonDao.XXXparola1);
			}

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_marca",
                    this.basicCriteria, session);

			List<Tb_marca> result = this.basicCriteria
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

	/**
	<statement nome="selectPerMotto" tipo="select" id="jenny_04">
			<fisso>
				WHERE
				  	UPPER (DS_MOTTO) LIKE UPPER(XXXmotto)||'%'
				AND fl_canc != 'S'
		  	</fisso>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXdata_ins_A </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_marca> selectPerMotto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MARCA_selectPerMotto");

			filter.setParameter(Tb_marcaCommonDao.XXXmotto, opzioni
					.get(Tb_marcaCommonDao.XXXmotto));

			myOpzioni.remove(Tb_marcaCommonDao.XXXmotto);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_marca",
                    this.basicCriteria, session);

			List<Tb_marca> result = this.basicCriteria
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

	/**
	<statement nome="selectPerDatavar" tipo="select" id="jenny_05">
			<fisso>
				WHERE
				   ts_var &gt; to_date(XXXdata_var_Da , 'yyyy-mm-dd')
				  AND ts_var &lt;  to_date(XXXdata_var_A , 'yyyy-mm-dd')
				AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_marca> selectPerDatavar(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MARCA_selectPerDatavar");

			filter.setParameter(Tb_marcaCommonDao.XXXdata_var_Da, opzioni
					.get(Tb_marcaCommonDao.XXXdata_var_Da));
			filter.setParameter(Tb_marcaCommonDao.XXXdata_var_A, opzioni
					.get(Tb_marcaCommonDao.XXXdata_var_A));

			myOpzioni.remove(Tb_marcaCommonDao.XXXdata_var_Da);
			myOpzioni.remove(Tb_marcaCommonDao.XXXdata_var_A);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_marca",
                    this.basicCriteria, session);

			List<Tb_marca> result = this.basicCriteria
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


	/**
	<statement nome="countPerParoleNome" tipo="count" id="jenny_03">
			<fisso>
				SELECT COUNT(*) FROM tb_marca
				WHERE
				  	CONTAINS(ds_marca, XXXparola1 ) &gt; 0
				AND fl_canc != 'S'
		  	</fisso>
			<opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) &gt; 0 </opzionale>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXdata_ins_A </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_MARCA_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_MARCA_countPerParoleNome");
				filter.setParameter(Tb_marcaCommonDao.XXXparola1, opzioni
				.get(Tb_marcaCommonDao.XXXparola1));
				myOpzioni.remove(Tb_marcaCommonDao.XXXparola1);
			}
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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

	/**
	<statement nome="countPerMotto" tipo="count" id="jenny_04">
			<fisso>
				SELECT COUNT (*) FROM tb_marca
				WHERE
				  	UPPER (DS_MOTTO) LIKE UPPER(XXXmotto)||'%'
				AND fl_canc != 'S'
		  	</fisso>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXdata_ins_A </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerMotto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MARCA_countPerMotto");

			filter.setParameter(Tb_marcaCommonDao.XXXmotto, opzioni
					.get(Tb_marcaCommonDao.XXXmotto));

			myOpzioni.remove(Tb_marcaCommonDao.XXXmotto);
			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();

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

	/**
	<statement nome="countPerDatavar" tipo="count" id="jenny_06">
			<fisso>
			SELECT COUNT(*) FROM tb_marca
				WHERE
				   ts_var &gt; to_date(XXXdata_var_Da , 'yyyy-mm-dd')
				  AND ts_var &lt;  to_date(XXXdata_var_A , 'yyyy-mm-dd')
				 AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerDatavar(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MARCA_countPerDatavar");

			filter.setParameter(Tb_marcaCommonDao.XXXdata_var_Da, opzioni
					.get(Tb_marcaCommonDao.XXXdata_var_Da));
			filter.setParameter(Tb_marcaCommonDao.XXXdata_var_A, opzioni
					.get(Tb_marcaCommonDao.XXXdata_var_A));

			myOpzioni.remove(Tb_marcaCommonDao.XXXdata_var_Da);
			myOpzioni.remove(Tb_marcaCommonDao.XXXdata_var_A);
			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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
//        BasicTableDao hb = new BasicTableDao();
//        Session session1 = hb.getSession();
//        hb.beginTransaction();
//        List  results = null;
//        String qry = "set_curcfg";
//        SQLQuery query = (SQLQuery) session1.getNamedQuery(qry);
//        results = (List) query.list();

		// TODO Auto-generated method stub
        log.debug("Tb_marca metodo insert invocato");
		Session session = this.getSession();
	    this.beginTransaction();


        Timestamp now = now();
		tb_marca.setTS_INS(now);
        tb_marca.setTS_VAR(now);
        session.save(this.tb_marca);
        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="09">
			<fisso>
				UPDATE Tb_marca
				 SET
				  ds_motto = XXXds_motto ,
				  cd_livello = XXXcd_livello ,
				  ute_var = XXXute_var ,
				  nota_marca = XXXnota_marca ,
				  ts_var = SYSTIMESTAMP ,
				  ds_marca = XXXds_marca ,
				  fl_canc = XXXfl_canc ,
				  fl_speciale = XXXfl_speciale ,
				  ute_ins = XXXute_ins
				WHERE
				  mid = XXXmid
				AND fl_canc != 'S'
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_marca metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_marca",Tb_marca.class);

		buildUpdate.addProperty("ds_motto",this.getParametro().get(KeyParameter.XXXds_motto));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_marca",this.getParametro().get(KeyParameter.XXXnota_marca));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("ds_marca",this.getParametro().get(KeyParameter.XXXds_marca));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_speciale",this.getParametro().get(KeyParameter.XXXfl_speciale));
		buildUpdate.addProperty("ute_ins",this.getParametro().get(KeyParameter.XXXute_ins));

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));

		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		buildUpdate.addWhere("fl_canc","s","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}

	/**
	 * 	<statement nome="cancellaMarca" tipo="update" id="Jenny_11">
			<fisso>
				UPDATE Tb_marca
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  mid = XXXmid
				AND  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void cancellaMarca(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_marca metodo cancellaMarca invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_marca",Tb_marca.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 * 	<statement nome="updateVersione" tipo="update" id="12_taymer">
			<fisso>
				UPDATE Tb_marca
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  mid = XXXmid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateVersione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_marca metodo updateVersione invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_marca",Tb_marca.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_marca.class;
	}
}
