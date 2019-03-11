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
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
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
public class Tr_tit_marResult  extends it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao{

    private Tr_tit_mar tr_tit_mar;

	public Tr_tit_marResult(Tr_tit_mar tr_tit_mar) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_mar.leggiAllParametro());
        this.tr_tit_mar = tr_tit_mar;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND mid = XXXmid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_mar> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_MAR_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXmid);
			List<Tr_tit_mar> result = this.basicCriteria
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
	<statement nome="selectPerMarca" tipo="select" id="Jenny_06">
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
	public List<Tr_tit_mar> selectPerMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_MAR_selectPerMarca");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXmid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_mar",
                    this.basicCriteria, session);

			List<Tr_tit_mar> result = this.basicCriteria
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
	<statement nome="selectPerTitolo" tipo="select" id="Jenny_07">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_mar> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_MAR_selectPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_marCommonDao.XXXbid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_mar",
                    this.basicCriteria, session);

			List<Tr_tit_mar> result = this.basicCriteria
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
	/*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_tit_mar
                 (
                  fl_canc ,
                  ute_var ,
                  ute_ins ,
                  mid ,
                  bid ,
                  nota_tit_mar ,
                  ts_var ,
                  ts_ins
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXmid ,
                  XXXbid ,
                  XXXnota_tit_mar ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP
                 )
            </fisso>
    </statement>

	 */
	public void insert(Object opzioni) throws InfrastructureException
	{
        log.debug("Tr_tit_mar metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_tit_mar.setTS_INS(now);
        tr_tit_mar.setTS_VAR(now);
        session.saveOrUpdate(this.tr_tit_mar);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
			<fisso>
				UPDATE Tr_tit_mar
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  nota_tit_mar = XXXnota_tit_mar ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND mid = XXXmid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException
	{
        log.debug("Tr_tit_mar metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_mar",Tr_tit_mar.class);

		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_tit_mar",this.getParametro().get(KeyParameter.XXXnota_tit_mar));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}

    /**
     * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_tit_mar
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid AND mid = XXXmid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateDisabilita(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_mar metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_mar",Tr_tit_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateLegameMarca" tipo="update" id="Jenny_08">
			<fisso>
				UPDATE Tr_tit_mar
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  mid = XXXidArrivo
				WHERE
				  bid = XXXbid
				  AND mid = XXXidPartenza

			</fisso>
			<opzionale dipende="XXXts_var"> AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var </opzionale>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
   public void updateLegameMarca(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_mar metodo updateLegameMarca invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_mar",Tr_tit_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("mid",this.getParametro().get(KeyParameter.XXXidArrivo));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXidPartenza),"=");

		if(this.getParametro().containsKey(KeyParameter.XXXts_var))
			buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

   /**
    * 	<statement nome="updateCancellaPerBid" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tr_tit_mar
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
        log.debug("Tr_tit_mar metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_mar",Tr_tit_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancellaLegameTitMar" tipo="update" id="Jenny_10">
			<fisso>
				UPDATE Tr_tit_mar
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				AND mid = XXXmid
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaLegameTitMar(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_mar metodo updateCancellaLegameTitMar invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_mar",Tr_tit_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_tit_mar.class;
	}

}
