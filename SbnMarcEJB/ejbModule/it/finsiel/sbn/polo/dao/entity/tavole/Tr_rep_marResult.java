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
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
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
public class Tr_rep_marResult  extends it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao {

    private Tr_rep_mar tr_rep_mar;

	public Tr_rep_marResult(Tr_rep_mar tr_rep_mar) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_rep_mar.leggiAllParametro());
        this.tr_rep_mar = tr_rep_mar;
   }

	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND mid = XXXmid
			  AND progr_repertorio = XXXprogr_repertorio
		</fisso>
			<opzionale dipende="XXXfl_canc"> AND fl_canc = XXXfl_canc </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_mar> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_MAR_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXprogr_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXprogr_repertorio));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXprogr_repertorio);

			this.createCriteria(myOpzioni);

			List<Tr_rep_mar> result = this.basicCriteria
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
	<statement nome="selectMarcaPerRepertorio" tipo="select" id="Jenny_03">
			<fisso>
				WHERE
				  id_repertorio = XXXid_repertorio
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_mar> selectMarcaPerRepertorio(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_MAR_selectMarcaPerRepertorio");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_rep_mar",
                    this.basicCriteria, session);

			List<Tr_rep_mar> result = this.basicCriteria
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
	<statement nome="selectRepertorioPerMarca" tipo="select" id="Jenny_09">
			<fisso>
				WHERE
				  mid = XXXmid
				AND fl_canc != 'S'
			</fisso>
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
	public List<Tr_rep_mar> selectRepertorioPerMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_MAR_selectRepertorioPerMarca");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_rep_mar",
                    this.basicCriteria, session);

			List<Tr_rep_mar> result = this.basicCriteria
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
	<statement nome="countRepertorioPerMarca" tipo="count" id="Jenny_10">
			<fisso>
				SELECT COUNT (*) FROM tr_rep_mar
				WHERE
				  mid = XXXmid
				AND fl_canc != 'S'
			</fisso>
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
	public Integer countRepertorioPerMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_MAR_countRepertorioPerMarca");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid);

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
	<statement nome="contaCitazioniInRepertorio" tipo="count" id="Jenny_05">
			<fisso>
				SELECT COUNT (*) FROM tr_rep_mar
				WHERE
				  id_repertorio = XXXid_repertorio
				  AND mid = XXXmid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer contaCitazioniInRepertorio(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_MAR_contaCitazioniInRepertorio");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXmid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio);

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
	<statement nome="countRepertorioPerCitazione" tipo="count" id="Jenny_04">
			<fisso>
			SELECT COUNT (*) FROM tr_rep_mar
				WHERE
				  id_repertorio = XXXid_repertorio
				  AND progr_repertorio = XXXprogr_repertorio
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countRepertorioPerCitazione(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_MAR_countRepertorioPerCitazione");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXprogr_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXprogr_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXprogr_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_marCommonDao.XXXid_repertorio);

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


    /*
     *  <statement nome="insert" tipo="insert" id="07">
        <fisso>
            INSERT INTO Tr_rep_mar
             (
              nota_rep_mar ,
              ts_ins ,
              ute_var ,
              id_repertorio ,
              progr_repertorio ,
              ts_var ,
              mid ,
              fl_canc ,
              ute_ins
             )
            VALUES
             (
              XXXnota_rep_mar ,
              SYSTIMESTAMP ,
              XXXute_var ,
              XXXid_repertorio ,
              XXXprogr_repertorio ,
              SYSTIMESTAMP ,
              XXXmid ,
              XXXfl_canc ,
              XXXute_ins
             )
        </fisso>
    </statement>

	 */
	public boolean insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_rep_mar metodo insert invocato ");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_rep_mar.setTS_INS(now);
        tr_rep_mar.setTS_VAR(now);
        session.saveOrUpdate(this.tr_rep_mar);
        this.commitTransaction();
        this.closeSession();
		return true;
	}

	/**
	 * 	<statement nome="update" tipo="update" id="08">
		<fisso>
			UPDATE Tr_rep_mar
			 SET
			  nota_rep_mar = XXXnota_rep_mar ,
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_canc = XXXfl_canc ,
			  ute_ins = XXXute_ins
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND mid = XXXmid
			  AND progr_repertorio = XXXprogr_repertorio
		</fisso>
	</statement>

	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public boolean update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_rep_mar metodo update invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_mar",Tr_rep_mar.class);

		buildUpdate.addProperty("nota_rep_mar",this.getParametro().get(KeyParameter.XXXnota_rep_mar));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ute_ins",this.getParametro().get(KeyParameter.XXXute_ins));


		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		buildUpdate.addWhere("progr_repertorio",this.getParametro().get(KeyParameter.XXXprogr_repertorio),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
		return true;

	}

	/**
	 * 	<statement nome="spostaLegami" tipo="update" id="Jenny_11">
			<fisso>
				UPDATE Tr_rep_mar
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  mid = XXXidArrivo
				WHERE
				  mid = XXXidPartenza
  			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void spostaLegami(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_mar metodo spostaLegami invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_mar",Tr_rep_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("mid",this.getParametro().get(KeyParameter.XXXidArrivo));


		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXidPartenza),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="cancellaLegamiRepertorio" tipo="update" id="13_taymer">
			<fisso>
				UPDATE Tr_rep_mar
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  id_repertorio = XXXid_repertorio
				  AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void cancellaLegamiRepertorio(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_mar metodo cancellaLegamiRepertorio invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_mar",Tr_rep_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
   }
    /*
     *  <statement nome="updatePerCancella" tipo="update" id="Jenny_12">
        <fisso>
            UPDATE Tr_rep_mar
             SET
              ute_var = XXXute_var ,
              ts_var = SYSTIMESTAMP ,
              fl_canc = 'S'
            WHERE
              id_repertorio = XXXid_repertorio
              AND mid = XXXmid
              AND progr_repertorio = XXXprogr_repertorio
              AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
        </fisso>
    </statement>

     */
    public void updatePerCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_mar metodo updatePerCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_mar",Tr_rep_mar.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("fl_canc","S");


        buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
        buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
        buildUpdate.addWhere("progr_repertorio",this.getParametro().get(KeyParameter.XXXprogr_repertorio),"=");
        buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_rep_mar.class;
	}
}
