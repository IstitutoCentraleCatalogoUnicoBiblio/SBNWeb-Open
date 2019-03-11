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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_luogoCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_luogo;
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
public class Tb_luogoResult extends Tb_luogoCommonDao{

    private Tb_luogo tb_luogo;

    public Tb_luogoResult(Tb_luogo tb_luogo) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_luogo.leggiAllParametro());
        this.tb_luogo = tb_luogo;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="Jenny_01">
			<fisso>
				WHERE
			  	 lid = XXXlid
				AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_luogo> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_selectPerKey");

			filter.setParameter(Tb_luogoCommonDao.XXXlid, opzioni
					.get(Tb_luogoCommonDao.XXXlid));

			myOpzioni.remove(Tb_luogoCommonDao.XXXlid);

			List<Tb_luogo> result = this.basicCriteria
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
	<statement nome="selectPerNomeLike" tipo="select" id="Jenny_02">
			<fisso>
				WHERE
				 UPPER(TRIM(Tb_luogoCommonDao.ky_norm_luogo)) like '%'||UPPER(TRIM(XXXky_norm_luogo ))||'%'
				AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXcd_paese ">AND cd_paese = XXXcd_paese</opzionale>
			<opzionale dipende="XXXlivello_aut_da">AND cd_livello &gt;= XXXlivello_aut_da</opzionale>
			<opzionale dipende="XXXlivello_aut_a">AND cd_livello &lt;=  XXXlivello_aut_a</opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>

	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_luogo> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_selectPerNomeLike");

			filter.setParameter(Tb_luogoCommonDao.XXXky_norm_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXky_norm_luogo));

			myOpzioni.remove(Tb_luogoCommonDao.XXXky_norm_luogo);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_luogo",
                    this.basicCriteria, session);

			List<Tb_luogo> result = this.basicCriteria
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
	<statement nome="countPerNomeLike" tipo="count" id="Jenny_03">
			<fisso>
			SELECT COUNT (*) FROM tb_luogo
				WHERE
				 UPPER(TRIM(Tb_luogoCommonDao.ky_norm_luogo)) like '%'||UPPER(TRIM(XXXky_norm_luogo ))||'%'
				AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXcd_paese ">AND cd_paese = XXXcd_paese</opzionale>
			<opzionale dipende="XXXlivello_aut_da">AND cd_livello &gt;= XXXlivello_aut_da</opzionale>
			<opzionale dipende="XXXlivello_aut_a">AND cd_livello &lt;=  XXXlivello_aut_a</opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_countPerNomeLike");

			filter.setParameter(Tb_luogoCommonDao.XXXky_norm_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXky_norm_luogo));

			myOpzioni.remove(Tb_luogoCommonDao.XXXky_norm_luogo);

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
	<statement nome="selectPerNome" tipo="select" id="Jenny_04">
			<fisso>
				WHERE
				 UPPER(TRIM(Tb_luogoCommonDao.ky_norm_luogo))=UPPER(TRIM(XXXky_norm_luogo ))
				AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXcd_paese ">AND cd_paese = XXXcd_paese</opzionale>
			<opzionale dipende="XXXlivello_aut_da">AND cd_livello &gt;= XXXlivello_aut_da</opzionale>
			<opzionale dipende="XXXlivello_aut_a">AND cd_livello &lt;=  XXXlivello_aut_a</opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>

	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_luogo> selectPerNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_selectPerNome");

			filter.setParameter(Tb_luogoCommonDao.XXXky_norm_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXky_norm_luogo));

			myOpzioni.remove(Tb_luogoCommonDao.XXXky_norm_luogo);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_luogo",
                    this.basicCriteria, session);

			List<Tb_luogo> result = this.basicCriteria
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
	<statement nome="countPerNome" tipo="count" id="Jenny_05">
			<fisso>
				SELECT COUNT (*) FROM 	tb_luogo
				WHERE
				 UPPER(TRIM(Tb_luogoCommonDao.ky_norm_luogo))=UPPER(TRIM(XXXky_norm_luogo ))
				AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXcd_paese ">AND cd_paese = XXXcd_paese</opzionale>
			<opzionale dipende="XXXlivello_aut_da">AND cd_livello &gt;= XXXlivello_aut_da</opzionale>
			<opzionale dipende="XXXlivello_aut_a">AND cd_livello &lt;=  XXXlivello_aut_a</opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_countPerNome");

			filter.setParameter(Tb_luogoCommonDao.XXXky_norm_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXky_norm_luogo));

			myOpzioni.remove(Tb_luogoCommonDao.XXXky_norm_luogo);

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
	<statement nome="selectLuoghiSimili" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				 UPPER(TRIM(Tb_luogoCommonDao.ky_norm_luogo))=UPPER(TRIM(XXXky_norm_luogo ))
				 AND UPPER(TRIM(Tb_luogoCommonDao.ds_luogo))=UPPER(TRIM(XXXds_luogo ))
				AND lid != XXXidDaModificare
				AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXlivello_aut_da">AND cd_livello &gt;= XXXlivello_aut_da</opzionale>
			<opzionale dipende="XXXlivello_aut_a">AND cd_livello &lt;=  XXXlivello_aut_a</opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>

	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_luogo> selectLuoghiSimili(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_selectLuoghiSimili");

			filter.setParameter(Tb_luogoCommonDao.XXXky_norm_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXky_norm_luogo));
			filter.setParameter(Tb_luogoCommonDao.XXXds_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXds_luogo));
			filter.setParameter(Tb_luogoCommonDao.XXXidDaModificare, opzioni
					.get(Tb_luogoCommonDao.XXXidDaModificare));

			myOpzioni.remove(Tb_luogoCommonDao.XXXky_norm_luogo);
			myOpzioni.remove(Tb_luogoCommonDao.XXXds_luogo);
			myOpzioni.remove(Tb_luogoCommonDao.XXXidDaModificare);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_luogo",
                    this.basicCriteria, session);

			List<Tb_luogo> result = this.basicCriteria
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
	<statement nome="selectSimili" tipo="select" id="Jenny_12">
			<fisso>
				WHERE
				 UPPER(TRIM(Tb_luogoCommonDao.ky_norm_luogo))=UPPER(TRIM(XXXky_norm_luogo ))
				 AND UPPER(TRIM(Tb_luogoCommonDao.ds_luogo))=UPPER(TRIM(XXXds_luogo))
				AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_luogo> selectSimili(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_selectSimili");

			filter.setParameter(Tb_luogoCommonDao.XXXky_norm_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXky_norm_luogo));
			filter.setParameter(Tb_luogoCommonDao.XXXds_luogo, opzioni
					.get(Tb_luogoCommonDao.XXXds_luogo));

			myOpzioni.remove(Tb_luogoCommonDao.XXXky_norm_luogo);
			myOpzioni.remove(Tb_luogoCommonDao.XXXds_luogo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_luogo",
                    this.basicCriteria, session);

			List<Tb_luogo> result = this.basicCriteria
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
        log.debug("Tb_luogo metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_luogo.setTS_INS(now);
        tb_luogo.setTS_VAR(now);
        session.save(this.tb_luogo);
        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="10">

			<fisso>
			UPDATE Tb_luogo
			 SET
			  cd_livello = XXXcd_livello ,
			  ky_norm_luogo = XXXky_norm_luogo ,
			  ky_luogo = XXXky_luogo ,
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  nota_luogo = XXXnota_luogo ,
			  tp_forma = XXXtp_forma ,
			  fl_canc = XXXfl_canc ,
			  cd_paese = XXXcd_paese ,
			  ute_ins = XXXute_ins ,
			  ds_luogo = XXXds_luogo
			WHERE
			  lid = XXXlid
			</fisso>
	</statement>

	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public boolean update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_luogo metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_luogo",Tb_luogo.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("cd_livello",params.get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("ky_norm_luogo",params.get(KeyParameter.XXXky_norm_luogo));
		buildUpdate.addProperty("ky_luogo",params.get(KeyParameter.XXXky_luogo));
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("nota_luogo",params.get(KeyParameter.XXXnota_luogo));
		buildUpdate.addProperty("tp_forma",params.get(KeyParameter.XXXtp_forma));
		buildUpdate.addProperty("fl_canc",params.get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("cd_paese",params.get(KeyParameter.XXXcd_paese));
		buildUpdate.addProperty("ute_ins",params.get(KeyParameter.XXXute_ins));
		buildUpdate.addProperty("ds_luogo",params.get(KeyParameter.XXXds_luogo));

		//almaviva5_20150923 sbnmarc v2.01
		buildUpdate.addProperty("nota_catalogatore", params.get(KeyParameter.XXXnota_catalogatore));

		buildUpdate.addWhere("lid",params.get(KeyParameter.XXXlid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
        return true;

	}

	/**
     * 	<statement nome="cancellaLuogo" tipo="update" id="Jenny_12">
			<fisso>
				UPDATE Tb_luogo
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  nota_luogo = XXXnota_luogo ,
				  fl_canc = XXXfl_canc
				WHERE
				  lid = XXXlid
				AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
				AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
	 * @throws InfrastructureException
     */
	public void cancellaLuogo(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_luogo metodo cancellaLuogo invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_luogo",Tb_luogo.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("nota_luogo",this.getParametro().get(KeyParameter.XXXnota_luogo));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("lid",this.getParametro().get(KeyParameter.XXXlid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	/**
     * 	<statement nome="updateVersione" tipo="update" id="13_taymer">
			<fisso>
				UPDATE Tb_luogo
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  lid = XXXlid
			</fisso>
	</statement>

     * @param opzioni
	 * @throws InfrastructureException
     */
   public void updateVersione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_luogo metodo updateVersione invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_luogo",Tb_luogo.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("lid",this.getParametro().get(KeyParameter.XXXlid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

   /**
    * <statement nome="selectPerEsistenza" tipo="select" id="Taymer_01Bis">
			<fisso>
				WHERE
			  	 lid = XXXlid
			</fisso>
	</statement>
    * @param opzioni
    * @return
 * @throws InfrastructureException
    */
   public List<Tb_luogo> selectPerEsistenza(HashMap opzioni) throws InfrastructureException {

		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_LUOGO_selectPerEsistenza");

			filter.setParameter(Tb_luogoCommonDao.XXXlid, opzioni
					.get(Tb_luogoCommonDao.XXXlid));

			myOpzioni.remove(Tb_luogoCommonDao.XXXlid);

			List<Tb_luogo> result = this.basicCriteria
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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_luogo.class;
	}
}
