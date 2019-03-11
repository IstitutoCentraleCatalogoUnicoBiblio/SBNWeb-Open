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
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
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
public class Tr_rep_titResult  extends it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao{

    private Tr_rep_tit tr_rep_tit;

	public Tr_rep_titResult(Tr_rep_tit tr_rep_tit) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_rep_tit.leggiAllParametro());
        this.tr_rep_tit = tr_rep_tit;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  bid = XXXbid
			  AND id_repertorio = XXXid_repertorio
			  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_tit> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_TIT_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXid_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXid_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid);
			List<Tr_rep_tit> result = this.basicCriteria
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
	<statement nome="selectRepertorioPerTitolo" tipo="select" id="Jenny_09">
			<fisso>
				WHERE
				  bid = XXXbid
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
	public List<Tr_rep_tit> selectRepertorioPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_TIT_selectRepertorioPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_rep_mar",
                    this.basicCriteria, session);

			List<Tr_rep_tit> result = this.basicCriteria
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
	<statement nome="countRepertorioPerTitolo" tipo="count" id="Jenny_10">
			<fisso>
				SELECT COUNT (*) FROM tr_rep_tit
				WHERE
				  bid = XXXbid
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
	public Integer countRepertorioPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_TIT_countRepertorioPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid);

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
	<statement nome="selectPerKeyCancellato" tipo="select" id="Jenny_10">
		<fisso>
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND bid = XXXbid
			  AND fl_canc = 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_tit> selectPerKeyCancellato(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_TIT_selectPerKeyCancellato");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXid_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXid_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_titCommonDao.XXXbid);
			List<Tr_rep_tit> result = this.basicCriteria
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
	 * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tr_rep_tit
			 SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_canc = XXXfl_canc ,
			  nota_rep_tit = XXXnota_rep_tit ,
			  fl_trovato = XXXfl_trovato
			WHERE
			  bid = XXXbid
			  AND id_repertorio = XXXid_repertorio
			  AND
			  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_tit metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_tit",Tr_rep_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("nota_rep_tit",this.getParametro().get(KeyParameter.XXXnota_rep_tit));
		buildUpdate.addProperty("fl_trovato",this.getParametro().get(KeyParameter.XXXfl_trovato));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}
	/*
     *  <statement nome="insert" tipo="insert" id="03">
        <fisso>
            INSERT INTO Tr_rep_tit
             (
              ts_ins ,
              ute_var ,
              bid ,
              id_repertorio ,
              ts_var ,
              fl_canc ,
              nota_rep_tit ,
              ute_ins ,
              fl_trovato
             )
            VALUES
             (
              SYSTIMESTAMP ,
              XXXute_var ,
              XXXbid ,
              XXXid_repertorio ,
              SYSTIMESTAMP ,
              XXXfl_canc ,
              XXXnota_rep_tit ,
              XXXute_ins ,
              XXXfl_trovato
             )
        </fisso>
    </statement>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_rep_tit metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_rep_tit.setTS_INS(now);
        tr_rep_tit.setTS_VAR(now);
        session.saveOrUpdate(this.tr_rep_tit);
        this.commitTransaction();
        this.closeSession();

	}

    /**
     * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_rep_tit
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid AND id_repertorio = XXXid_repertorio
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateDisabilita(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_tit metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_tit",Tr_rep_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancellaPerBid" tipo="update" id="11_taymer">
			<fisso>
				UPDATE Tr_rep_tit
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_tit metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_tit",Tr_rep_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="cancellaLegamiRepertorio" tipo="update" id="13_taymer">
			<fisso>
				UPDATE Tr_rep_tit
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
        log.debug("Tr_rep_tit metodo cancellaLegamiRepertorio invocato Da implementare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_tit",Tr_rep_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_rep_tit.class;
	}
}
