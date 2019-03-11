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
import it.finsiel.sbn.polo.orm.Tr_rep_aut;
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
public class Tr_rep_autResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao{

    private Tr_rep_aut tr_rep_aut;

    public Tr_rep_autResult(Tr_rep_aut tr_rep_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_rep_aut.leggiAllParametro());
        this.tr_rep_aut = tr_rep_aut;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND vid = XXXvid
			  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_aut> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_AUT_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid);
			List<Tr_rep_aut> result = this.basicCriteria
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
	<statement nome="selectPerKeyCancellato" tipo="select" id="Jenny_10">
		<fisso>
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND vid = XXXvid
			  AND fl_canc = 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_aut> selectPerKeyCancellato(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_AUT_selectPerKeyCancellato");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_rep_aut",
                    this.basicCriteria, session);

			List<Tr_rep_aut> result = this.basicCriteria
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
	<statement nome="selectEsistenza" tipo="select" id="12">
		<fisso>
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND vid = XXXvid
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_aut> selectEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_AUT_selectEsistenza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXid_repertorio);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid);
			List<Tr_rep_aut> result = this.basicCriteria
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
	<statement nome="selectPerAutore" tipo="select" id="02">
			<fisso>
				WHERE
				  vid = XXXvid
				AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_rep_aut> selectPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_AUT_selectPerAutore");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_rep_aut",
                    this.basicCriteria, session);

			List<Tr_rep_aut> result = this.basicCriteria
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
	<statement nome="selectRepertorioPerAutore" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  vid = XXXvid
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
	public List<Tr_rep_aut> selectRepertorioPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_AUT_selectRepertorioPerAutore");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_rep_aut",
                    this.basicCriteria, session);

			List<Tr_rep_aut> result = this.basicCriteria
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
	<statement nome="countRepertorioPerAutore" tipo="count" id="Jenny_07">
			<fisso>
				SELECT COUNT (*) FROM tr_rep_aut
				WHERE
				  vid = XXXvid
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
	public Integer countRepertorioPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_AUT_countRepertorioPerAutore");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_rep_autCommonDao.XXXvid);

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
    /*
     *  <statement nome="insert" tipo="insert" id="03">
        <fisso>
            INSERT INTO Tr_rep_aut
             (
              note_rep_aut ,
              ts_ins ,
              ute_var ,
              id_repertorio ,
              ts_var ,
              vid ,
              fl_canc ,
              ute_ins ,
              fl_trovato
             )
            VALUES
             (
              XXXnote_rep_aut ,
              SYSTIMESTAMP ,
              XXXute_var ,
              XXXid_repertorio ,
              SYSTIMESTAMP ,
              XXXvid ,
              XXXfl_canc ,
              XXXute_ins ,
              XXXfl_trovato
             )
        </fisso>
    </statement>

     */
	public boolean insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_rep_aut metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_rep_aut.setTS_INS(now);
        tr_rep_aut.setTS_VAR(now);
        session.save(this.tr_rep_aut);
        this.commitTransaction();
        this.closeSession();

		return true;
	}

	/**
	 * 	<statement nome="update" tipo="update" id="04">
		<fisso>
			UPDATE Tr_rep_aut
			 SET
				note_rep_aut = XXXnote_rep_aut ,
				ute_var = XXXute_var ,
				ts_var = SYSTIMESTAMP ,
				fl_trovato = XXXfl_trovato,
				fl_canc = XXXfl_canc
			WHERE
			  id_repertorio = XXXid_repertorio
			  AND vid = XXXvid
		</fisso>
	</statement>

	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public boolean update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_rep_aut metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_aut",Tr_rep_aut.class);

		buildUpdate.addProperty("note_rep_aut",this.getParametro().get(KeyParameter.XXXnote_rep_aut));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_trovato",this.getParametro().get(KeyParameter.XXXfl_trovato));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

        return true;
	}

	/**
	 * 	<statement nome="cancellaLegamiRepertorio" tipo="update" id="11_taymer">
			<fisso>
				UPDATE Tr_rep_aut
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
        log.debug("Tr_rep_aut metodo cancellaLegamiRepertorio invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_aut",Tr_rep_aut.class);

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
     *  <statement nome="updateCancella" tipo="update" id="05_taymer">
            <fisso>
                UPDATE Tr_rep_aut
                 SET
                  fl_canc = XXXfl_canc ,
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP
                WHERE
                  id_repertorio = XXXid_repertorio
                  AND vid = XXXvid
                  <!--AND ts_var = XXXts_var-->
            </fisso>
    </statement>

     */
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_rep_aut metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_rep_aut",Tr_rep_aut.class);

        buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());


        buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
        buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_rep_aut.class;
	}
}
