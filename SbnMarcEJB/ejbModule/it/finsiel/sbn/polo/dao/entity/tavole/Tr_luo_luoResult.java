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
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.util.BuilderUpdate;
import it.finsiel.sbn.util.Expression;

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
public class Tr_luo_luoResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao {

    private Tr_luo_luo tr_luo_luo;

	public Tr_luo_luoResult(Tr_luo_luo tr_luo_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_luo_luo.leggiAllParametro());
        this.tr_luo_luo = tr_luo_luo;
    }

	/**
	<statement nome="selectPerKey" tipo="select" id="jenny01">
			<fisso>
				WHERE
				  lid_base = XXXlid_base
				  AND lid_coll = XXXlid_coll
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_luo_luo> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_LUO_LUO_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll);
			List<Tr_luo_luo> result = this.basicCriteria
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
	<statement nome="verifica" tipo="select" id="01_taymer">
			<fisso>
				WHERE
				  lid_base = XXXlid_base
				  AND lid_coll = XXXlid_coll
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_luo_luo> verifica(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_LUO_LUO_verifica");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_luo_luo",
                    this.basicCriteria, session);

			List<Tr_luo_luo> result = this.basicCriteria
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
	<statement nome="selectPerLidBase" tipo="select" id="jenny05">
			<fisso>
				WHERE lid_base = XXXlid_base
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_luo_luo> selectPerLidBase(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_LUO_LUO_selectPerLidBase");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_luo_luo",
                    this.basicCriteria, session);

			List<Tr_luo_luo> result = this.basicCriteria
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
	<statement nome="selectPerLidColl" tipo="select" id="jenny06">
			<fisso>
				WHERE lid_coll = XXXlid_coll
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_luo_luo> selectPerLidColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_LUO_LUO_selectPerLidColl");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_luo_luo",
                    this.basicCriteria, session);

			List<Tr_luo_luo> result = this.basicCriteria
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
	<statement nome="cercaLegamiPerLid" tipo="select" id="Jenny_15">
			<fisso>
				WHERE
				  fl_canc != 'S'
				AND (lid_base = XXXlid_base
				OR lid_coll = XXXlid_coll)
  			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_luo_luo> cercaLegamiPerLid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_LUO_LUO_cercaLegamiPerLid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_luo_luoCommonDao.XXXlid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_luo_luo",
                    this.basicCriteria, session);

			List<Tr_luo_luo> result = this.basicCriteria
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
	<statement nome="selectLegami" tipo="select" id="Jenny_12">
			<fisso>
				WHERE
				  fl_canc != 'S'
  			</fisso>
			<opzionale dipende="XXXlid_base"> AND lid_base = XXXlid_base </opzionale>
			<opzionale dipende="XXXlid_coll"> AND lid_coll = XXXlid_coll </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_luo_luo> selectLegami(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TR_LUO_LUO_selectLegami");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_luo_luo",
                    this.basicCriteria, session);

			List<Tr_luo_luo> result = this.basicCriteria
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
     *  <statement nome="insert" tipo="insert" id="jenny03">
            <fisso>
                INSERT INTO Tr_luo_luo
                 (
                  fl_canc ,
                  ute_var ,
                  ute_ins ,
                  tp_legame ,
                  lid_coll ,
                  ts_var ,
                  ts_ins ,
                  lid_base
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXtp_legame ,
                  XXXlid_coll ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP ,
                  XXXlid_base
                 )
            </fisso>
    </statement>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_luo_luo metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_luo_luo.setTS_INS(now);
        tr_luo_luo.setTS_VAR(now);
        session.saveOrUpdate(this.tr_luo_luo);
        this.commitTransaction();
        this.closeSession();

	}


    /**
     * 	<statement nome="cancellaLegameLuogoLuogo" tipo="update" id="Jenny_16">
			<fisso>
				UPDATE Tr_luo_luo
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  fl_canc = 'S'
				WHERE
				  (lid_base = XXXlid_base
				   AND lid_coll = XXXlid_coll)
				OR
				  (lid_coll = XXXlid_base
				   AND lid_base = XXXlid_coll)
				AND fl_canc != 'S'
  			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
//    public void cancellaLegameLuogoLuogo(Object opzioni) throws InfrastructureException {
//        // TODO Auto-generated method stub
//        log.debug("Tr_luo_luo metodo cancellaLegameLuogoLuogo invocato ");
//        Session session = this.getSession();
//        this.beginTransaction();
//
//
//        Query query = session.createSQLQuery("UPDATE Tr_luo_luo " +
//                "SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP, fl_canc = :XXXfl_canc  " +
//                "WHERE " +
//                "(lid_base = :XXXlid_base AND lid_coll = :XXXlid_coll) " +
//                "OR " +
//                "(lid_coll = :XXXlid_base_1 AND lid_base = :XXXlid_coll_1) " +
//                "AND fl_canc != :XXXfl_canc_1 ");
//
//
//        query.setParameter("XXXute_var",this.getParametro().get(KeyParameter.XXXute_var));
//        query.setParameter("SYSTIMESTAMP",now());
//        query.setParameter("XXXfl_canc","S");
//
//        query.setParameter("XXXlid_base",this.getParametro().get(KeyParameter.XXXlid_base));
//        query.setParameter("XXXlid_coll",this.getParametro().get(KeyParameter.XXXlid_coll));
//        query.setParameter("XXXlid_base_1",this.getParametro().get(KeyParameter.XXXlid_coll));
//        query.setParameter("XXXlid_coll_1",this.getParametro().get(KeyParameter.XXXlid_base));
//        query.setParameter("XXXfl_canc_1","S");
//
//        query.executeUpdate();
//        this.commitTransaction();
//        this.closeSession();
//
//    }

    public void cancellaLegameLuogoLuogo(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_luo_luo metodo cancellaLegameLuogoLuogo invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_luo_luo",Tr_luo_luo.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		Expression exp = null;
		Expression exp1 = null;
		exp = new Expression("0");
		exp.addProperty("lid_base",this.getParametro().get(KeyParameter.XXXlid_base),"=");
		exp.addProperty("lid_coll",this.getParametro().get(KeyParameter.XXXlid_coll),"=");
		buildUpdate.addExpressionOr(exp);
		exp1 = new Expression("1");
		exp1.addProperty("lid_coll",this.getParametro().get(KeyParameter.XXXlid_base),"=");
		exp1.addProperty("lid_base",this.getParametro().get(KeyParameter.XXXlid_coll),"=");
		buildUpdate.addExpressionOr(exp1);

		buildUpdate.addWhere("fl_canc","S","!=");


		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateLegame" tipo="update" id="taymer_04">
			<fisso>
				UPDATE Tr_luo_luo
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  tp_legame = XXXtp_legame ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  lid_base = XXXlid_base
				  AND lid_coll = XXXlid_coll
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateLegame(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_luo_luo metodo updateLegame invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_luo_luo",Tr_luo_luo.class);

		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("tp_legame",this.getParametro().get(KeyParameter.XXXtp_legame));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("lid_base",this.getParametro().get(KeyParameter.XXXlid_base),"=");
		buildUpdate.addWhere("lid_coll",this.getParametro().get(KeyParameter.XXXlid_coll),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    // FUNZIONE AGGIUNTIVA PER LA RIABILITAZIONE DI UNA FORMA DI RINVIO GIA ESISTENTE
    public void UpdateAbilitaFormaRinvioEsistente(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_luo_luo metodo UpdateAbilitaFormaRinvioEsistente invocato");
        Session session = this.getSession();
        this.beginTransaction();
        session.saveOrUpdate(this.tr_luo_luo);
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_luo_luo.class;
	}
}
