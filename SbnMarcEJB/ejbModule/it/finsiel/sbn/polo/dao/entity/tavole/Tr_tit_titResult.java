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
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Test Eseguito con successo
 * @author Antonio
 *
 */
public class Tr_tit_titResult  extends it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao{

    private Tr_tit_tit tr_tit_tit;

	public Tr_tit_titResult(Tr_tit_tit tr_tit_tit) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_tit.leggiAllParametro());
        this.tr_tit_tit = tr_tit_tit;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  bid_base = XXXbid_base
			  AND bid_coll = XXXbid_coll
			  AND fl_canc != 'S'
		</fisso>
		<opzionale dipende="XXXtp_legame"> AND tp_legame = XXXtp_legame</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_tit> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);

			this.createCriteria(myOpzioni);

			List<Tr_tit_tit> result = this.basicCriteria
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


	 // Inizio modifica almaviva2 04.07.2012 - i legami a raccolta fattizia vanno spostati dal titolo fuso a quello di arrivo
	public List<Tr_tit_tit> selectPerBidBaseNaturaColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
//			Filter filter = session.enableFilter("TR_TIT_TIT_selectPerBidBaseEColl");
			Filter filter = session.enableFilter("TR_TIT_TIT_selectPerBidBaseNaturaColl");


			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXcd_natura_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXcd_natura_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXcd_natura_coll);
		    this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
		            "Tr_tit_tit",
		            this.basicCriteria, session);

			List<Tr_tit_tit> result = this.basicCriteria
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

	 // Fine modifica almaviva2 04.07.2012 - i legami a raccolta fattizia vanno spostati dal titolo fuso a quello di arrivo

	/**
	<statement nome="selectPerBidBaseEColl" tipo="select" id="10_taymer">
		<fisso>
			WHERE  fl_canc != 'S'
			  AND  bid_coll = XXXbid_coll
			  AND  bid_base = XXXbid_base
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_tit> selectPerBidBaseEColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_selectPerBidBaseEColl");


			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_tit",
                    this.basicCriteria, session);

			List<Tr_tit_tit> result = this.basicCriteria
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
	<statement nome="selectPerBidColl" tipo="select" id="11_taymer">
		<fisso>
			WHERE  fl_canc != 'S'
			  AND  bid_coll = XXXbid_coll

		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_tit> selectPerBidColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_selectPerBidColl");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_tit",
                    this.basicCriteria, session);

			List<Tr_tit_tit> result = this.basicCriteria
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


	// INIZIO bug mantis 4980 (sbnweb) - evolutva 15/05/2012
    // ai poli è consentito fondere W su M o viceversa solo se entrambi i titoli hanno lo stesso padre,
    // a interfaccia diretta è sempre consentito
	public List<Tr_tit_tit> selectBidPadre(HashMap opzioni)
		throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			/*
			fl_canc != 'S'
	               AND  bid_base = XXXbid_base
	               AND tp_legame = '01'
			 */
			basicCriteria.add(Restrictions.ne("fl_canc", "S"));
			basicCriteria.add(Restrictions.eq("tp_legame", "01"));
			basicCriteria.add(Restrictions.eq("bid_base", opzioni.get(XXXbid_base)));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);
		    this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
		            "Tr_tit_tit",
		            this.basicCriteria, session);

			List<Tr_tit_tit> result = this.basicCriteria
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

	// FINE bug mantis 4980 (sbnweb) - evolutva 15/05/2012

	/**
	<statement nome="countPerBidColl" tipo="count" id="12_carlo">
			<fisso>
			SELECT COUNT(*) FROM Tr_tit_titCommonDao
			WHERE  fl_canc != 'S'
			  AND  bid_coll = XXXbid_coll
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerBidColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_countPerBidColl");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);

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
	<statement nome="countPerBidBase" tipo="count" id="13_carlo">
			<fisso>
			SELECT COUNT(*) FROM Tr_tit_titCommonDao
			WHERE  fl_canc != 'S'
			  AND  bid_base = XXXbid_base
			  AND tp_legame = '01'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerBidBase(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_countPerBidBase");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);

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
	<statement nome="countPerLivelloColl" tipo="count" id="14_carlo">
			<fisso>
			SELECT COUNT(*) FROM Tr_tit_titCommonDao
			WHERE  fl_canc != 'S'
			  AND  bid_coll = XXXbid_coll
			  AND tp_legame = '01'
			  AND ( (cd_natura_base = 'C' and cd_natura_coll = 'C')
			        OR (cd_natura_base != 'C' and cd_natura_coll != 'C') )
			  AND ( (cd_natura_base = 'S' and cd_natura_coll = 'S')
			        OR (cd_natura_base != 'S' and cd_natura_coll != 'S') )
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerLivelloColl(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_countPerLivelloColl");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);

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
	<statement nome="countPerLivelloBase" tipo="count" id="15_carlo">
			<fisso>
			SELECT COUNT(*) FROM Tr_tit_titCommonDao
			WHERE  fl_canc != 'S'
			  AND  bid_base = XXXbid_base
			  AND tp_legame = '01'
			  AND ( (cd_natura_base = 'C' and cd_natura_coll = 'C')
			        OR (cd_natura_base != 'C' and cd_natura_coll != 'C') )
			  AND ( (cd_natura_base = 'S' and cd_natura_coll = 'S')
			        OR (cd_natura_base != 'S' and cd_natura_coll != 'S') )
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerLivelloBase(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_countPerLivelloBase");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);

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
	<statement nome="selectMusica" tipo="select" id="18_taymer">
		<fisso>
			WHERE  fl_canc != 'S'
			  AND  bid_coll = XXXbid_coll
			  AND tp_legame_musica is not null
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_tit> selectMusica(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_selectMusica");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_tit",
                    this.basicCriteria, session);

			List<Tr_tit_tit> result = this.basicCriteria
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
	<statement nome="selectCancellato" tipo="select" id="19_taymer">
		<fisso>
			WHERE fl_canc = 'S'
			  AND bid_coll = XXXbid_coll
			  AND bid_base = XXXbid_base
			  AND tp_legame = XXXtp_legame
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_tit> selectCancellato(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_TIT_selectCancellato");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXtp_legame, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXtp_legame));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXbid_coll);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_titCommonDao.XXXtp_legame);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_tit",
                    this.basicCriteria, session);

			List<Tr_tit_tit> result = this.basicCriteria
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
            INSERT INTO Tr_tit_tit
             (
              ute_var ,
              ts_ins ,
              cd_natura_coll ,
              cd_natura_base ,
              tp_legame_musica ,
              sequenza_musica ,
              sequenza ,
              nota_tit_tit ,
              ts_var ,
              sici ,
              fl_canc ,
              bid_coll ,
              ute_ins ,
              bid_base ,
              tp_legame
             )
            VALUES
             (
              XXXute_var ,
              SYSTIMESTAMP ,
              XXXcd_natura_coll ,
              XXXcd_natura_base ,
              XXXtp_legame_musica ,
              XXXsequenza_musica ,
              XXXsequenza ,
              XXXnota_tit_tit ,
              SYSTIMESTAMP ,
              XXXsici ,
              XXXfl_canc ,
              XXXbid_coll ,
              XXXute_ins ,
              XXXbid_base ,
              XXXtp_legame
             )
        </fisso>
    </statement>

	 */
	public void insert(Object opzioni) throws InfrastructureException
	{
        log.debug("Tr_tit_tit metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tr_tit_tit.setTS_INS(ts);
        tr_tit_tit.setTS_VAR(ts);
        session.saveOrUpdate(this.tr_tit_tit);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tr_tit_tit
			 SET
			  ute_var = XXXute_var ,
			  cd_natura_coll = XXXcd_natura_coll ,
			  cd_natura_base = XXXcd_natura_base ,
			  tp_legame_musica = XXXtp_legame_musica ,
			  sequenza_musica = XXXsequenza_musica ,
			  sequenza = XXXsequenza ,
			  nota_tit_tit = XXXnota_tit_tit ,
			  ts_var = SYSTIMESTAMP ,
			  sici = XXXsici ,
			  tp_legame = XXXtp_legame ,
			  fl_canc = XXXfl_canc
			WHERE
			  bid_base = XXXbid_base
			  AND bid_coll = XXXbid_coll
			  AND
			  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_tit metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_tit",Tr_tit_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("cd_natura_coll",this.getParametro().get(KeyParameter.XXXcd_natura_coll));
		buildUpdate.addProperty("cd_natura_base",this.getParametro().get(KeyParameter.XXXcd_natura_base));
		buildUpdate.addProperty("tp_legame_musica",this.getParametro().get(KeyParameter.XXXtp_legame_musica));
		buildUpdate.addProperty("sequenza_musica",this.getParametro().get(KeyParameter.XXXsequenza_musica));
		buildUpdate.addProperty("sequenza",this.getParametro().get(KeyParameter.XXXsequenza));
		buildUpdate.addProperty("nota_tit_tit",this.getParametro().get(KeyParameter.XXXnota_tit_tit));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("sici",this.getParametro().get(KeyParameter.XXXsici));
		buildUpdate.addProperty("tp_legame",this.getParametro().get(KeyParameter.XXXtp_legame));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));


		buildUpdate.addWhere("bid_base",this.getParametro().get(KeyParameter.XXXbid_base),"=");
		buildUpdate.addWhere("bid_coll",this.getParametro().get(KeyParameter.XXXbid_coll),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	/**
     * 	<statement nome="updateCancellaPerBidBase" tipo="update" id="09_taymer">
			<fisso>
				UPDATE Tr_tit_tit
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid_base = XXXbid_base
				  AND fl_canc != 's'
			</fisso>
	</statement>

     * @param opzioni
	 * @throws InfrastructureException
     */
    public void updateCancellaPerBidBase(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_tit metodo updateCancellaPerBidBase invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_tit",Tr_tit_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid_base",this.getParametro().get(KeyParameter.XXXbid_base),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateModificaNatura" tipo="update" id="15_taymer">
			<fisso>
				UPDATE Tr_tit_tit
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  cd_natura_coll = XXXcd_natura_coll ,
				  tp_legame = XXXtp_legame
				WHERE
				  bid_coll = XXXbid_coll
				  AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateModificaNatura(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_tit metodo updateModificaNatura invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_tit",Tr_tit_tit.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cd_natura_coll",this.getParametro().get(KeyParameter.XXXcd_natura_coll));
		buildUpdate.addProperty("tp_legame",this.getParametro().get(KeyParameter.XXXtp_legame));
		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));


		buildUpdate.addWhere("bid_coll",this.getParametro().get(KeyParameter.XXXbid_coll),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /*
     *  <statement nome="updateDisabilita" tipo="update" id="06_taymer">
            <fisso>
                UPDATE Tr_tit_tit
                 SET
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP ,
                  fl_canc = 'S'
                WHERE
                  (
                  (bid_base = XXXbid_base AND bid_coll = XXXbid_coll)
                  OR
                  (bid_coll = XXXbid_base AND bid_base = XXXbid_coll)
                  )
                  <!-- Da mettere quando ci sono i controlli nello sposta documenti.
                  AND
                  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var-->
            </fisso>
    </statement>
     */
    public void updateDisabilita(Object opzioni) throws InfrastructureException {
        log.debug("Tr_aut_aut metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();

        Query query = session.createSQLQuery("UPDATE Tr_tit_tit  " +
                "SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP , fl_canc = :XXXfl_canc " +
                "WHERE ( " +
                "(bid_base = :XXXbid_base AND bid_coll = :XXXbid_coll) " +
                "OR " +
                "(bid_coll = :XXXbid_base1 AND bid_base = :XXXbid_coll1))");

        //Query query = session.createSQLQuery("UPDATE Tr_tit_tit  SET ute_var = :XXXute_var , ts_var = :SYSTIMESTAMP , fl_canc = :XXXfl_canc WHERE ( (bid_base = :XXXbid_base AND bid_coll = :XXXbid_coll) OR (bid_coll = :XXXbid_base1 AND bid_base = :XXXbid_coll1))");
        query.setParameter("XXXute_var",this.getParametro().get(KeyParameter.XXXute_var));
        query.setParameter("SYSTIMESTAMP",now());
        query.setParameter("XXXfl_canc","S");

        query.setParameter("XXXbid_base",this.getParametro().get(KeyParameter.XXXbid_base));
        query.setParameter("XXXbid_coll",this.getParametro().get(KeyParameter.XXXbid_coll));
        query.setParameter("XXXbid_base1",this.getParametro().get(KeyParameter.XXXbid_coll));
        query.setParameter("XXXbid_coll1",this.getParametro().get(KeyParameter.XXXbid_base));
        query.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }
//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
//    public void updateDisabilita(Object opzioni) throws InfrastructureException {
//        // TODO Auto-generated method stub
//        log.debug("Tr_aut_aut metodo updateDisabilita invocato");
//        Session session = this.getSession();
//        this.beginTransaction();
//
//        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_tit",Tr_tit_tit.class);
//
//        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
//        buildUpdate.addProperty("ts_var",now());
//        buildUpdate.addProperty("fl_canc","S");
//        Expression exp = new Expression();
//        Expression exp1 = new Expression();
//        exp.addProperty("bid_base",this.getParametro().get(KeyParameter.XXXbid_base),"=");
//        exp.addProperty("bid_coll",this.getParametro().get(KeyParameter.XXXbid_coll),"=");
//        buildUpdate.addExpressionOr(exp);
//        exp1.addProperty("bid_coll",this.getParametro().get(KeyParameter.XXXbid_base),"=");
//        exp1.addProperty("bid_base",this.getParametro().get(KeyParameter.XXXbid_coll),"=");
//        buildUpdate.addExpressionOr(exp1);
//        buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");
//
//        int query = buildUpdate.executeUpdate();
//        this.commitTransaction();
//        this.closeSession();
//
//    }
    public void updateModificaMusica(Object opzioni) throws InfrastructureException{
        // TODO Auto-generated method stub
        // VIENE RICHIAMATA DA TITOLOTITOLO.JAVA
        /*
        public void aggiornaMusica(Tb_titolo titolo, String tp_legame) throws IllegalArgumentException, InvocationTargetException, Exception {
            Tr_tit_tit tt = new Tr_tit_tit();
            tt.setBID_COLL(titolo.getBID());
            tt.setUTE_VAR(titolo.getUTE_VAR());
            tt.setTP_LEGAME(tp_legame);
            tt.setCD_NATURA_COLL(titolo.getCD_NATURA());
            Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
            tabella.executeCustom("updateModificaMusica");

        }
        */
        // MA NON ESISTE DA NESSUNA PARTE FORSE CODICE MORTO
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_tit_tit.class;
	}
}
