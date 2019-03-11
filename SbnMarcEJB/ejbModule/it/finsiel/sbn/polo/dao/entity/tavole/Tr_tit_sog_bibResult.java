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
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Filter;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Tr_tit_sog_bibResult  extends TableDao{

    private Tr_tit_sog_bib tr_tit_sog;

	public Tr_tit_sog_bibResult(Tr_tit_sog_bib tr_tit_sog) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_sog.leggiAllParametro());
        this.tr_tit_sog = tr_tit_sog;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND cid = XXXcid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_sog_bib> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_SOG_BIB_selectPerKey");

			filter.setParameter(Tr_tit_sog_bibResult.XXXcid, opzioni
					.get(Tr_tit_sog_bibResult.XXXcid));
			filter.setParameter(Tr_tit_sog_bibResult.XXXbid, opzioni
					.get(Tr_tit_sog_bibResult.XXXbid));

			myOpzioni.remove(Tr_tit_sog_bibResult.XXXcid);
			myOpzioni.remove(Tr_tit_sog_bibResult.XXXbid);


			List<Tr_tit_sog_bib> result = new ArrayList<Tr_tit_sog_bib>(this.basicCriteria.list());
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
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND cid = XXXcid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public int selectPerKeyEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_SOG_BIB_selectPerKey");

			filter.setParameter(Tr_tit_sog_bibResult.XXXcid, opzioni
					.get(Tr_tit_sog_bibResult.XXXcid));
			filter.setParameter(Tr_tit_sog_bibResult.XXXbid, opzioni
					.get(Tr_tit_sog_bibResult.XXXbid));

			myOpzioni.remove(Tr_tit_sog_bibResult.XXXcid);
			myOpzioni.remove(Tr_tit_sog_bibResult.XXXbid);

			Number result = (Number) this.basicCriteria.setProjection(
				Projections.projectionList()
				   .add(Projections.rowCount())).uniqueResult();

			this.commitTransaction();
			this.closeSession();

			return result.intValue();

		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="selectTitoloPerSoggetto" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  cid = XXXcid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_sog_bib> selectTitoloPerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_SOG_BIB_selectTitoloPerSoggetto");

			filter.setParameter(Tr_tit_sog_bibResult.XXXcid, opzioni
					.get(Tr_tit_sog_bibResult.XXXcid));

			myOpzioni.remove(Tr_tit_sog_bibResult.XXXcid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_sog_bib",
                    this.basicCriteria, session);

			List<Tr_tit_sog_bib> result = new ArrayList<Tr_tit_sog_bib>(this.basicCriteria.list());
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
	public List<Tr_tit_sog_bib> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_SOG_BIB_selectPerTitolo");

			filter.setParameter(Tr_tit_sog_bibResult.XXXbid, opzioni
					.get(Tr_tit_sog_bibResult.XXXbid));

			myOpzioni.remove(Tr_tit_sog_bibResult.XXXbid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_sog_bib",
                    this.basicCriteria, session);

			List<Tr_tit_sog_bib> result = new ArrayList<Tr_tit_sog_bib>(this.basicCriteria.list());
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
	<statement nome="countFrequenzaPerBid" tipo="count" id="10_william">
		<fisso>
			SELECT COUNT(*)
			FROM   tr_tit_sog
			WHERE  fl_canc != 'S'
			and bid =XXXbid
		</fisso>
    </statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countFrequenzaPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_SOG_BIB_countFrequenzaPerBid");

			filter.setParameter(Tr_tit_sog_bibResult.XXXbid, opzioni
					.get(Tr_tit_sog_bibResult.XXXbid));

			myOpzioni.remove(Tr_tit_sog_bibResult.XXXbid);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.rowCount())).uniqueResult();

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
    <statement nome="countTitoloSoggetto" tipo="count" id="11_william">
			<fisso>
			SELECT COUNT(*) FROM tr_tit_sog
				WHERE fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countTitoloSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TR_TIT_SOG_BIB_countTitoloSoggetto");

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.rowCount())).uniqueResult();

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
				UPDATE Tr_tit_sog_bib
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND cid = XXXcid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Map opzioni) throws InfrastructureException {
        log.debug("Tr_tit_sog_bib metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_sog_bib", Tr_tit_sog_bib.class);

		Map<?, ?> params = this.getParametro();
		buildUpdate.addProperty("cd_sogg", params.get(KeyParameter.XXXcd_sogg));
		buildUpdate.addProperty("nota_tit_sog_bib",	params.get(KeyParameter.XXXnota_tit_sog_bib));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));

		// almaviva5_20120508 evolutive CFI
		buildUpdate.addProperty("posizione", params.get(KeyParameter.XXXposizione));

		buildUpdate.addWhere("bid", params.get(KeyParameter.XXXbid), "=");
		buildUpdate.addWhere("cid", params.get(KeyParameter.XXXcid), "=");
		buildUpdate.addWhere("ts_var", params.get(KeyParameter.XXXts_var), "=");

		buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_tit_sog_bib
                 (
                  fl_canc ,
                  ute_var ,
                  ute_ins ,
                  bid ,
                  ts_var ,
                  ts_ins ,
                  cid
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXbid ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP ,
                  XXXcid
                 )
            </fisso>
    </statement>

     */
	public void insert(Object opzioni) throws InfrastructureException {

        log.debug("Tr_tit_sog_bib metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tr_tit_sog.setTS_INS(ts);
        tr_tit_sog.setTS_VAR(ts);
        session.saveOrUpdate(this.tr_tit_sog);
        this.commitTransaction();
        this.closeSession();

	}

    /**
     * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_tit_sog_bib
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid AND cid = XXXcid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
   public void updateDisabilita(Object opzioni) throws InfrastructureException {

        log.debug("Tr_tit_sog_bib metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_sog_bib",Tr_tit_sog_bib.class);

		Map<?, ?> params = this.getParametro();
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",params.get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cid",params.get(KeyParameter.XXXcid),"=");
		//buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
   public void updateDisabilitaConBib(Object opzioni) throws InfrastructureException {

       log.debug("Tr_tit_sog_bib metodo updateDisabilita invocato");
       Session session = this.getSession();
       this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_sog_bib",Tr_tit_sog_bib.class);

		Map<?, ?> params = this.getParametro();
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",params.get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cid",params.get(KeyParameter.XXXcid),"=");
		buildUpdate.addWhere("cd_biblioteca",params.get(KeyParameter.XXXcd_biblioteca),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

   }

    /**
     * 	<statement nome="updateLegameSoggetto" tipo="update" id="Jenny_08">
			<fisso>
				UPDATE Tr_tit_sog_bib
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  cid = XXXidArrivo
				WHERE
				  bid = XXXbid
				  AND cid = XXXidPartenza
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
   public void updateLegameSoggetto(Object opzioni) throws InfrastructureException {

        log.debug("Tr_tit_sog_bib metodo updateLegameSoggetto invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_sog_bib",Tr_tit_sog_bib.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cid",params.get(KeyParameter.XXXidArrivo));
		//almaviva5_20140221
		buildUpdate.addProperty("CD_SOGG",params.get(KeyParameter.XXXcd_sogg));


		buildUpdate.addWhere("bid",params.get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cid",params.get(KeyParameter.XXXidPartenza),"=");
		// TOLTO PERCHE? genera errore
//		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancellaPerBid" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tr_tit_sog_bib
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

        log.debug("Tr_tit_sog_bib metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_sog_bib",Tr_tit_sog_bib.class);

		Map<?, ?> params = this.getParametro();
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",params.get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancellaLegameTitSog" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tr_tit_sog_bib
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				AND cid = XXXcid
				AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaLegameTitSog(Object opzioni) throws InfrastructureException {

        log.debug("Tr_tit_sog_bib metodo updateCancellaLegameTitSog invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_sog_bib",Tr_tit_sog_bib.class);

		Map<?, ?> params = this.getParametro();
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",params.get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cid",params.get(KeyParameter.XXXcid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	public void delete(Object opzioni) throws InfrastructureException {

        log.debug("Tr_tit_sog_bib metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_tit_sog.setTS_INS(now);
        tr_tit_sog.setTS_VAR(now);
        session.delete(this.tr_tit_sog);
        this.commitTransaction();
        this.closeSession();

	}


	public boolean verifica_fl_aut_loc(String polo, String bib, String cd_sogg)
			throws InfrastructureException {

		Session session = this.getSession();
		this.beginTransaction();
		Query query = session
				.createSQLQuery("SELECT * FROM tr_soggettari_biblioteche "
						+ " where cd_polo=:POLO and cd_biblioteca=:BIB AND cd_sogg=:CD_SOGG and fl_auto_loc='1'");

		query.setParameter("POLO", polo);
		query.setParameter("BIB", bib);
		query.setParameter("CD_SOGG", cd_sogg);
		query.getQueryString();
		int x = query.list().size();
		if (x > 0) {
			this.commitTransaction();
			this.closeSession();
			return true;
		} else {
			this.commitTransaction();
			this.closeSession();
			return false;
		}

	}

	public List<Tr_tit_sog_bib> rankLegameTitoloSoggettoUp(HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.setLockMode(LockMode.UPGRADE);
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("BID", myOpzioni.get(KeyParameter.XXXbid)));
			this.basicCriteria.add(Restrictions.ne("CID", myOpzioni.get(KeyParameter.XXXcid)));

			this.basicCriteria.add(Restrictions.ge("POSIZIONE", myOpzioni.get(KeyParameter.XXXposizione)));

			this.basicCriteria.addOrder(Order.asc("POSIZIONE"));

			myOpzioni.remove(KeyParameter.XXXcid);
			myOpzioni.remove(KeyParameter.XXXbid);
			myOpzioni.remove(KeyParameter.XXXposizione);

			List<Tr_tit_sog_bib> result = this.basicCriteria.list();

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

	public List<Tr_tit_sog_bib> rankLegameTitoloSoggettoDown(HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.setLockMode(LockMode.UPGRADE);
			this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
			this.basicCriteria.add(Restrictions.eq("BID", myOpzioni.get(KeyParameter.XXXbid)));
			this.basicCriteria.add(Restrictions.ne("CID", myOpzioni.get(KeyParameter.XXXcid)));
			this.basicCriteria.add(Restrictions.eq("POSIZIONE", myOpzioni.get(KeyParameter.XXXposizione)));

			this.basicCriteria.addOrder(Order.asc("POSIZIONE"));

			myOpzioni.remove(KeyParameter.XXXcid);
			myOpzioni.remove(KeyParameter.XXXbid);
			myOpzioni.remove(KeyParameter.XXXposizione);

			List<Tr_tit_sog_bib> result = this.basicCriteria.list();

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
		return Tr_tit_sog_bib.class;
	}

	public List<Tr_tit_sog_bib> selectPerKeyPerFusione(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			//almaviva5_20151201 #6046
			this.basicCriteria.add(Restrictions.eq("CID", opzioni.get(Tr_tit_sog_bibResult.XXXcid)) );
			this.basicCriteria.add(Restrictions.eq("BID", opzioni.get(Tr_tit_sog_bibResult.XXXbid)) );

			myOpzioni.remove(Tr_tit_sog_bibResult.XXXcid);
			myOpzioni.remove(Tr_tit_sog_bibResult.XXXbid);

			List<Tr_tit_sog_bib> result = new ArrayList<Tr_tit_sog_bib>(this.basicCriteria.list());
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

}
