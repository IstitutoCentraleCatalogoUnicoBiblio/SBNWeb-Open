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
import it.finsiel.sbn.polo.orm.Tr_aut_aut;
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
public class Tr_aut_autResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao{

    private Tr_aut_aut tr_aut_aut;

	public Tr_aut_autResult(Tr_aut_aut tr_aut_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_aut_aut.leggiAllParametro());
        this.tr_aut_aut = tr_aut_aut;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  tp_legame = XXXtp_legame
				  AND vid_base = XXXvid_base
				  AND vid_coll = XXXvid_coll
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_aut> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_AUT_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXtp_legame, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXtp_legame));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXtp_legame);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_coll);
			List<Tr_aut_aut> result = this.basicCriteria
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
	<statement nome="selectPerKeys2" tipo="select" id="01">
			<fisso>
				WHERE

				  (
				  (vid_base = XXXvid_1 AND vid_coll = XXXvid_2)
				  OR
				  (vid_coll = XXXvid_1 AND vid_base = XXXvid_2)
				  )
				  AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXtp_legame"> AND tp_legame = XXXtp_legame </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_aut> selectPerKeys2(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_AUT_selectPerKeys2");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_1, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_1));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_2, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_2));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_1);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_2);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_aut",
                    this.basicCriteria, session);

			List<Tr_aut_aut> result = this.basicCriteria
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
	<statement nome="selectPerAutoreBaseOColl" tipo="select" id="Jenny_10">
			<fisso>
				WHERE
				  (
				   vid_base = XXXvid_base OR
				   vid_coll = XXXvid_base
				  )
				  AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXtp_legame"> AND tp_legame = XXXtp_legame </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_aut> selectPerAutoreBaseOColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_AUT_selectPerAutoreBaseOColl");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_aut",
                    this.basicCriteria, session);

			List<Tr_aut_aut> result = this.basicCriteria
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
			<opzionale dipende="XXXvid_base"> AND vid_base = XXXvid_base </opzionale>
			<opzionale dipende="XXXvid_coll"> AND vid_coll = XXXvid_coll </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_aut> selectLegami(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TR_AUT_AUT_selectLegami");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_aut",
                    this.basicCriteria, session);

			List<Tr_aut_aut> result = this.basicCriteria
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
	<statement nome="selectEsistenza" tipo="select" id="14_taymer">
			<fisso>
				WHERE
				vid_base = XXXvid_base
				AND vid_coll = XXXvid_coll
  			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_aut> selectEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_AUT_selectEsistenza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_autCommonDao.XXXvid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_aut",
                    this.basicCriteria, session);

			List<Tr_aut_aut> result = this.basicCriteria
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
	 * <statement nome="update" tipo="update" id="07">
			<fisso>
				UPDATE Tr_aut_aut
				 SET
				  ts_var = SYSTIMESTAMP ,
				  ute_var = XXXute_var ,
				  nota_aut_aut = XXXnota_aut_aut
				WHERE
				  tp_legame = XXXtp_legame
				  AND vid_base = XXXvid_base
				  AND vid_coll = XXXvid_coll
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>
	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("nota_aut_aut",this.getParametro().get(KeyParameter.XXXnota_aut_aut));


		buildUpdate.addWhere("tp_legame",this.getParametro().get(KeyParameter.XXXtp_legame),"=");
		buildUpdate.addWhere("vid_base",this.getParametro().get(KeyParameter.XXXvid_base),"=");
		buildUpdate.addWhere("vid_coll",this.getParametro().get(KeyParameter.XXXvid_coll),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}
	/*
     * <statement nome="insert" tipo="insert" id="06">
            <fisso>
                INSERT INTO Tr_aut_aut
                 (
                  vid_coll ,
                  vid_base ,
                  ts_ins ,
                  ute_var ,
                  ts_var ,
                  fl_canc ,
                  nota_aut_aut ,
                  ute_ins ,
                  tp_legame
                 )
                VALUES
                 (
                  XXXvid_coll ,
                  XXXvid_base ,
                  SYSTIMESTAMP ,
                  XXXute_var ,
                  SYSTIMESTAMP ,
                  XXXfl_canc ,
                  XXXnota_aut_aut ,
                  XXXute_ins ,
                  XXXtp_legame
                 )
            </fisso>
	 */
    public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_aut_aut.setTS_INS(now);
        tr_aut_aut.setTS_VAR(now);
        session.saveOrUpdate(this.tr_aut_aut);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="updateInsert" tipo="update" id="07b_Taymer">
			<fisso>
				UPDATE Tr_aut_aut
				 SET
				  ts_var = SYSTIMESTAMP ,
				  ute_var = XXXute_var ,
				  nota_aut_aut = XXXnota_aut_aut,
				  tp_legame = XXXtp_legame,
				  fl_canc = XXXfl_canc
				WHERE
				  vid_base = XXXvid_base
				  AND vid_coll = XXXvid_coll
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void updateInsert(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo updateInsert invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("nota_aut_aut",this.getParametro().get(KeyParameter.XXXnota_aut_aut));
		buildUpdate.addProperty("tp_legame",this.getParametro().get(KeyParameter.XXXtp_legame));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("vid_base",this.getParametro().get(KeyParameter.XXXvid_base),"=");
		buildUpdate.addWhere("vid_coll",this.getParametro().get(KeyParameter.XXXvid_coll),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 *
     * <statement nome="updateCancella" tipo="update" id="Jenny_09">
			<fisso>
				UPDATE Tr_aut_aut
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  ( (vid_base = XXXvid_1 AND vid_coll = XXXvid_2)
				  OR
				  (vid_coll = XXXvid_1 AND vid_base = XXXvid_2) )
  			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
//    public void updateCancell(Object opzioni) throws InfrastructureException {
//        // TODO Auto-generated method stub
//        log.debug("Tr_aut_aut metodo updateCancell invocato");
//        Session session = this.getSession();
//        this.beginTransaction();
//        Query query = session.createSQLQuery("UPDATE Tr_aut_aut " +
//                "SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP , fl_canc = :XXXfl_canc " +
//                "WHERE ( " +
//                "(vid_base = :XXXvid_1 AND vid_coll = :XXXvid_2) " +
//                "OR (vid_coll = :XXXvid_1_1 AND vid_base = :XXXvid_2_1) " +
//                ")");
//
//        query.setParameter("XXXute_var",this.getParametro().get(KeyParameter.XXXute_var));
//        query.setParameter("SYSTIMESTAMP",now());
//        query.setParameter("XXXfl_canc","S");
//
//        query.setParameter("XXXvid_1",this.getParametro().get(KeyParameter.XXXvid_1));
//        query.setParameter("XXXvid_2",this.getParametro().get(KeyParameter.XXXvid_2));
//        query.setParameter("XXXvid_1_1",this.getParametro().get(KeyParameter.XXXvid_1));
//        query.setParameter("XXXvid_2_1",this.getParametro().get(KeyParameter.XXXvid_2));
//
//        query.executeUpdate();
//        this.commitTransaction();
//        this.closeSession();
//
//    }

	public void updateCancell(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo updateCancell invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		Expression exp = null;

		exp = new Expression("0");
		exp.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_1),"=");
		exp.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_2),"=");
		buildUpdate.addExpressionOr(exp);
		exp = new Expression("1");
		exp.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_2),"=");
		exp.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_1),"=");
		buildUpdate.addExpressionOr(exp);

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
       // FUNZIONE AGGIUNTIVA PER LA RIABILITAZIONE DI UNA FORMA DI RINVIO GIA ESISTENTE
    public void UpdateAbilitaFormaRinvioEsistente(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo UpdateAbilitaFormaRinvioEsistente invocato");
        Session session = this.getSession();
        this.beginTransaction();
        //session.saveOrUpdate(this.tr_aut_aut);
        session.update(this.tr_aut_aut);
        this.commitTransaction();
        this.closeSession();

    }

    //

    /*
     *  <statement nome="updateDisabilitaDoppio" tipo="update" id="08_taymer">
            <fisso>
                UPDATE Tr_aut_aut
                 SET
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP ,
                  fl_canc = 'S'
                WHERE
                  (
                  (vid_base = XXXvid_1 AND vid_coll = XXXvid_2)
                  OR
                  (vid_coll = XXXvid_1 AND vid_base = XXXvid_2)
                  )
            </fisso>
            <opzionale dipende="XXXts_var"> AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var </opzionale>
    </statement>

     */
//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
//    public void updateDisabilitaDoppio(Object opzioni) throws InfrastructureException {
//        // TODO Auto-generated method stub
//        log.debug("Tr_aut_aut metodo updateDisabilitaDoppio invocato");
//        Session session = this.getSession();
//        this.beginTransaction();
//        // OPZIONALE
//        Query query = null;
//        if(this.getParametro().get(KeyParameter.XXXts_var) != null){
//            query = session.createSQLQuery("UPDATE Tr_aut_aut " +
//                    "SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP , fl_canc = :XXXfl_canc " +
//                    "WHERE ( " +
//                    "(vid_base = :XXXvid_1 AND vid_coll = :XXXvid_2) " +
//                    "OR (vid_coll = :XXXvid_1_1 AND vid_base = :XXXvid_2_1) " +
//                    ") " +
//                    "AND ts_var = :XXXts_var");
//            query.setParameter("XXXts_var",now());
//        }else{
//            query = session.createSQLQuery("UPDATE Tr_aut_aut " +
//                    "SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP , fl_canc = :XXXfl_canc " +
//                    "WHERE ( " +
//                    "(vid_base = :XXXvid_1 AND vid_coll = :XXXvid_2) " +
//                    "OR (vid_coll = :XXXvid_1_1 AND vid_base = :XXXvid_2_1) " +
//                    ")");
//        }
//        query.setParameter("XXXute_var",this.getParametro().get(KeyParameter.XXXute_var));
//        query.setParameter("SYSTIMESTAMP",now());
//        query.setParameter("XXXfl_canc","S");
//
//        query.setParameter("XXXvid_1",this.getParametro().get(KeyParameter.XXXvid_1));
//        query.setParameter("XXXvid_2",this.getParametro().get(KeyParameter.XXXvid_2));
//        query.setParameter("XXXvid_1_1",this.getParametro().get(KeyParameter.XXXvid_1));
//        query.setParameter("XXXvid_2_1",this.getParametro().get(KeyParameter.XXXvid_2));
//
//        query.executeUpdate();
//        this.commitTransaction();
//        this.closeSession();
//
//    }

    public void updateDisabilitaDoppio(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo updateModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();

        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("fl_canc","S");

        Expression exp = null;
        Expression exp1 = null;
        exp = new Expression("0");
        exp.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_1),"=");
        exp.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_2),"=");
        buildUpdate.addExpressionOr(exp);
        exp1 = new Expression("1");
        exp1.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_1),"=");
        exp1.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_2),"=");
        buildUpdate.addExpressionOr(exp1);


        if(this.getParametro().get(KeyParameter.XXXts_var) != null){
            buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");
        }
        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }


    /*
     *  <statement nome="updateModifica" tipo="update" id="08">
            <fisso>
                UPDATE Tr_aut_aut
                 SET
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP ,
                  nota_aut_aut = XXXnota_aut_aut
                WHERE
                  ( (vid_base = XXXvid_1 AND vid_coll = XXXvid_2)
                  OR
                  (vid_coll = XXXvid_1 AND vid_base = XXXvid_2) )
                  AND
                  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
            </fisso>
    </statement>
     */

//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
    /// DOVREBBE ESSERE RISOLTA metto la vecchia chiamata
//    public void updateModifica(Object opzioni) throws InfrastructureException {
//        // TODO Auto-generated method stub
//        //log.debug("Tr_aut_aut metodo updateModifica invocato");
//        Session session = this.getSession();
//        this.beginTransaction();
//        Query query = session.createSQLQuery("UPDATE Tr_aut_aut " +
//                "SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP , nota_aut_aut = :XXXnota_aut_aut " +
//                "WHERE ( " +
//                "(vid_base = :XXXvid_1 AND vid_coll = :XXXvid_2) " +
//                "OR " +
//                "(vid_coll = :XXXvid_1_1 AND vid_base = :XXXvid_2_1) " +
//                ") " +
//                "AND ts_var = :XXXts_var");
//
//        query.setParameter("XXXute_var",this.getParametro().get(KeyParameter.XXXute_var));
//        query.setParameter("SYSTIMESTAMP",now());
//        query.setParameter("XXXnota_aut_aut",this.getParametro().get(KeyParameter.XXXnota_att_att));
//
//        query.setParameter("XXXvid_1",this.getParametro().get(KeyParameter.XXXvid_1));
//        query.setParameter("XXXvid_2",this.getParametro().get(KeyParameter.XXXvid_2));
//        query.setParameter("XXXvid_1_1",this.getParametro().get(KeyParameter.XXXvid_1));
//        query.setParameter("XXXvid_2_1",this.getParametro().get(KeyParameter.XXXvid_2));
//        query.setParameter("XXXts_var",now());
//        query.executeUpdate();
//        this.commitTransaction();
//        this.closeSession();
//
//    }

    public void updateModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo updateModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();

        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("nota_aut_aut",this.getParametro().get(KeyParameter.XXXnota_aut_aut));

        Expression exp = null;

        exp = new Expression("0");
        exp.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_1),"=");
        exp.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_2),"=");
        buildUpdate.addExpressionOr(exp);
        exp = new Expression("1");
        exp.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_1),"=");
        exp.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_2),"=");
        buildUpdate.addExpressionOr(exp);

        buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }
    /*
     *  <statement nome="spostaLegamiBase" tipo="update" id="Jenny_11">
            <fisso>
                UPDATE Tr_aut_aut
                 SET
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP,
                  vid_base = XXXvid_base_new
                WHERE
                  vid_base = XXXvid_base
                  AND vid_coll = XXXvid_coll
                  AND fl_canc != 'S'
            </fisso>
    </statement>
     */
    public void spostaLegamiBase(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo spostaLegamiBase invocato");
        Session session = this.getSession();
        this.beginTransaction();

        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("vid_base",this.getParametro().get(KeyParameter.XXXvid_base_new));

        buildUpdate.addWhere("vid_base",this.getParametro().get(KeyParameter.XXXvid_base),"=");
        buildUpdate.addWhere("vid_coll",this.getParametro().get(KeyParameter.XXXvid_coll),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }
    /*
     *  <statement nome="spostaLegamiColl" tipo="update" id="Jenny_11">
            <fisso>
                UPDATE Tr_aut_aut
                 SET
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP,
                  vid_coll = XXXvid_coll_new
                WHERE
                  vid_coll = XXXvid_coll
                  AND vid_base = XXXvid_base
                  AND fl_canc != 'S'
            </fisso>
    </statement>

     */
    public void spostaLegamiColl(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        log.debug("Tr_aut_aut metodo spostaLegamiColl invocato");
        Session session = this.getSession();
        this.beginTransaction();

        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_aut",Tr_aut_aut.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("vid_coll",this.getParametro().get(KeyParameter.XXXvid_coll_new));

        buildUpdate.addWhere("vid_coll",this.getParametro().get(KeyParameter.XXXvid_coll),"=");
        buildUpdate.addWhere("vid_base",this.getParametro().get(KeyParameter.XXXvid_base),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }
	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_aut_aut.class;
	}

}
