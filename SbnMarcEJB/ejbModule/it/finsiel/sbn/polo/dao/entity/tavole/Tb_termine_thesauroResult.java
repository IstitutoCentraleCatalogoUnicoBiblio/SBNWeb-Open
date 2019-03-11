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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_termine_thesauroCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
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
public class Tb_termine_thesauroResult extends Tb_termine_thesauroCommonDao{

    private Tb_termine_thesauro tb_termine_thesauro;

    public Tb_termine_thesauroResult(Tb_termine_thesauro tb_termine_thesauro) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_termine_thesauro.leggiAllParametro());
        this.tb_termine_thesauro = tb_termine_thesauro;
    }


    /**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
				WHERE
				  did = XXXdid
				AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_selectPerKey");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXdid, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXdid));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXdid);
			List<Tb_termine_thesauro> result = this.basicCriteria
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
	<statement nome="selectPerNomeLike" tipo="select" id="Jenny_05">
		<fisso>
				WHERE
					fl_canc !='S'
					AND ky_termine_thesauro LIKE XXXstringaLike ||'%'
			</fisso>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXcd_the"> AND cd_the = XXXcd_the </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_selectPerNomeLike");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXstringaLike, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXstringaLike);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "tb_termine_thesauro",
                    this.basicCriteria, session);

			List<Tb_termine_thesauro> result = this.basicCriteria
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
	<statement nome="countPerNomeLike" tipo="count" id="Jenny_06">
		<fisso>
				SELECT COUNT (*) FROM tb_termine_thesauro
				WHERE
					fl_canc !='S'
					AND ky_termine_thesauro LIKE XXXstringaLike ||'%'
			</fisso>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXcd_the"> AND cd_the = XXXcd_the </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_countPerNomeLike");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXstringaLike, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXstringaLike);

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
	<statement nome="selectPerParoleNome" tipo="select" id="Jenny_07">
		<fisso>
				WHERE
				fl_canc !='S' AND
				  	CONTAINS(ds_termine_thesauro, XXXparola1 ) > 0
		  	</fisso>
		<opzionale dipende="XXXparola2"> AND CONTAINS(ds_termine_thesauro, XXXparola2 ) > 0 </opzionale>
		<opzionale dipende="XXXparola3"> AND CONTAINS(ds_termine_thesauro, XXXparola3 ) > 0 </opzionale>
		<opzionale dipende="XXXparola4"> AND CONTAINS(ds_termine_thesauro, XXXparola4 ) > 0 </opzionale>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_the = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_TERMINE_THESAURO_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TTB_TERMINE_THESAURO_selectPerParoleNome");
				filter.setParameter(Tb_termine_thesauroCommonDao.XXXparola1, opzioni
						.get(Tb_termine_thesauroCommonDao.XXXparola1));
				myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXparola1);
			}
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "tb_termine_thesauro",
                    this.basicCriteria, session);

			List<Tb_termine_thesauro> result = this.basicCriteria
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
	<statement nome="countPerParoleNome" tipo="count" id="Jenny_08">
		<fisso>
				SELECT COUNT (*) FROM tb_termine_thesauro
				WHERE
				fl_canc !='S' AND
				  	CONTAINS(ds_termine_thesauro, XXXparola1 ) > 0
		  	</fisso>
		<opzionale dipende="XXXparola2"> AND CONTAINS(ds_termine_thesauro, XXXparola2 ) > 0 </opzionale>
		<opzionale dipende="XXXparola3"> AND CONTAINS(ds_termine_thesauro, XXXparola3 ) > 0 </opzionale>
		<opzionale dipende="XXXparola4"> AND CONTAINS(ds_termine_thesauro, XXXparola4 ) > 0 </opzionale>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXcd_the"> AND cd_the = XXXcd_the </opzionale>
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
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_TERMINE_THESAURO_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TTB_TERMINE_THESAURO_countPerParoleNome");
				filter.setParameter(Tb_termine_thesauroCommonDao.XXXparola1, opzioni
						.get(Tb_termine_thesauroCommonDao.XXXparola1));
				myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXparola1);
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
	<statement nome="selectPerNomeEsatto" tipo="select" id="Jenny_9">
		<fisso>
				WHERE
					fl_canc !='S'
					AND ky_termine_thesauro = XXXstringaEsatta
			</fisso>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXcd_the"> AND cd_the = XXXcd_the </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_selectPerNomeEsatto");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXstringaEsatta, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXstringaEsatta));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXstringaEsatta);

			this.createCriteria(myOpzioni);
			List<Tb_termine_thesauro> result = this.basicCriteria
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
	<statement nome="countPerNomeEsatto" tipo="count" id="Jenny_10">
		<fisso>
				SELECT COUNT (*) FROM tb_termine_thesauro
				WHERE
					fl_canc !='S'
					AND ky_termine_thesauro = XXXstringaEsatta
			</fisso>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXcd_the"> AND cd_the = XXXcd_the </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */

    public Integer countPerNomeEsatto(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_TERMINE_THESAURO_countPerNomeEsatto");

            filter.setParameter(Tb_termine_thesauroCommonDao.XXXstringaEsatta, opzioni
                    .get(Tb_termine_thesauroCommonDao.XXXstringaEsatta));

            myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXstringaEsatta);

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
	<statement nome="selectPerKyNormtermine" tipo="select" id="Jenny_12">
		<fisso>
				WHERE
				ky_termine_thesauro = XXXky_termine_thesauro
				AND cd_the =XXXcd_the
				AND fl_canc !='S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectPerKyNormTermine(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_selectPerKyNormTermine");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro));
			filter.setParameter(Tb_termine_thesauroCommonDao.XXXcd_the, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXcd_the));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro);
			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXcd_the);

			this.createCriteria(myOpzioni);
//            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
//                    "tb_termine_thesauro",
//                    this.basicCriteria, session);

			List<Tb_termine_thesauro> result = this.basicCriteria.list();
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
	<statement nome="selectPerEsistenza" tipo="select" id="13_taymer">
		<fisso>
				WHERE
				  did = XXXdid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectPerEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_selectPerEsistenza");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXdid, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXdid));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXdid);
			List<Tb_termine_thesauro> result = this.basicCriteria
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
        log.debug("Tb_termine_thesauro metodo insert invocato ");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_termine_thesauro.setTS_INS(now);
        tb_termine_thesauro.setTS_VAR(now);
        session.save(this.tb_termine_thesauro);
        this.commitTransaction();
        this.closeSession();

	}


	/**
	 * <statement nome="delete" tipo="delete" id="02">
		<fisso>
				DELETE FROM Tb_termine_thesauro
				WHERE
				  did = XXXdid
			</fisso>
	</statement>
	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void delete(Object opzioni) throws InfrastructureException {

        Session session = this.getSession();
        this.beginTransaction();
        session.delete(this.tb_termine_thesauro);
		this.commitTransaction();
		this.closeSession();

    }


	/**
	 * 	<statement nome="update" tipo="update" id="04">
		<fisso>
				UPDATE Tb_termine_thesauro
				 SET
				  nota_termine_thesauro = XXXnota_termine_thesauro ,
				  cd_livello = XXXcd_livello ,
				  ds_termine_thesauro = XXXds_termine_thesauro ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  cd_the = XXXcd_the ,
				  fl_canc = XXXfl_canc ,
				  ky_termine_thesauro = XXXky_termine_thesauro,
	  			  tp_forma_the = XXXtp_forma_the
				WHERE
				  did = XXXdid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 * @throws EccezioneSbnDiagnostico
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub

		log.debug("Tb_termine_thesauro metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_termine_thesauro",Tb_termine_thesauro.class);

		buildUpdate.addProperty("nota_termine_thesauro",this.getParametro().get(KeyParameter.XXXnota_termine_thesauro));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("ds_termine_thesauro",this.getParametro().get(KeyParameter.XXXds_termine_thesauro));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cd_the",this.getParametro().get(KeyParameter.XXXcd_the));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ky_termine_thesauro",this.getParametro().get(KeyParameter.XXXky_termine_thesauro));
		buildUpdate.addProperty("tp_forma_the",this.getParametro().get(KeyParameter.XXXtp_forma_the));

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));

		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}

	/**
	<statement nome="cancellaTerminThesauro" tipo="update" id="Jenny_11">
	<fisso>
			UPDATE Tb_termine_thesauro
			 SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_canc = XXXfl_canc
			WHERE
			  did = XXXdid
			AND fl_canc !='S'
		</fisso>
</statement>
	 * @throws InfrastructureException
*/
    public void cancellaTermineThesauro(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_termine_thesauro metodo cancellaTermineThesauro invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_termine_thesauro",Tb_termine_thesauro.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
	/**
	<statement nome="selectSimili" tipo="select" id="Jenny_15">
			<fisso>
				WHERE
				  fl_canc != 'S'
				  AND ky_termine_thesauro_s = XXXky_termine_thesauro
			</fisso>
			<opzionale dipende="XXXcid"> AND did != XXXdid </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro>  selectSimili(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
            // Modifico la modalità di passaggio dei dati per la preparazione dei filtri opzionali
            // in quanto alcuni parametri non necessitano ma essendo settati creano SQL non congruenti in più
            // personalizzo il filtro cid in quanto e settato a != inveche che ==
            HashMap prova = new HashMap();
            if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro))
                prova.put(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro,opzioni.get(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro));
            if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXdid))
                prova.put(Tb_termine_thesauroCommonDao.XXXdid,opzioni.get(Tb_termine_thesauroCommonDao.XXXdid));

			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_TERMINE_THESAURO_selectSimili");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro));

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXdid, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXdid));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXky_termine_thesauro);
			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXdid);

			this.createCriteria(myOpzioni);
			//this.createCriteria(prova);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_termine_thesauro",
                    this.basicCriteria, session);

			List<Tb_termine_thesauro> result = this.basicCriteria
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
	<statement nome="selectSimiliConferma" tipo="select" id="20_Taymer">
			<fisso>
				WHERE
				  fl_canc != 'S'
				  AND ky_cles1_s = XXXky_cles1_s
				  AND UPPER( TRIM(ds_soggetto) ) = XXXds_soggetto
			</fisso>
			<opzionale dipende="XXXky_cles2_s"> AND ky_cles2_s = XXXky_cles2_s </opzionale>
			<opzionale dipende="XXXky_cles2_sNULL"> AND ky_cles2_s is null </opzionale>
			<opzionale dipende="XXXcid"> AND cid != XXXcid </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_termine_thesauro> selectSimiliConferma(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
            // Modifico la modalità di passaggio dei dati per la preparazione dei filtri opzionali
            // in quanto alcuni parametri non necessitano ma essendo settati creano SQL non congruenti in più
            // personalizzo il filtro cid in quanto e settato a != inveche che ==

            HashMap prova = new HashMap();
            if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXky_cles2_s))
                prova.put(Tb_termine_thesauroCommonDao.XXXky_cles2_s,opzioni.get(Tb_termine_thesauroCommonDao.XXXky_cles2_s));
            if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXky_cles2_sNULL))
                prova.put(Tb_termine_thesauroCommonDao.XXXky_cles2_sNULL,opzioni.get(Tb_termine_thesauroCommonDao.XXXky_cles2_sNULL));
            if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXcid))
                prova.put(Tb_termine_thesauroCommonDao.XXXcid_Diverso,opzioni.get(Tb_termine_thesauroCommonDao.XXXcid));

			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("Tb_termine_thesauro_selectSimiliConferma");

			filter.setParameter(Tb_termine_thesauroCommonDao.XXXky_cles1_s, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXky_cles1_s));
			filter.setParameter(Tb_termine_thesauroCommonDao.XXXds_soggetto, opzioni
					.get(Tb_termine_thesauroCommonDao.XXXds_soggetto));

			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXky_cles1_s);
			myOpzioni.remove(Tb_termine_thesauroCommonDao.XXXds_soggetto);
			this.createCriteria(prova);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_termine_thesauro",
                    this.basicCriteria, session);

			List<Tb_termine_thesauro> result = this.basicCriteria
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
     * 	<statement nome="updateScambioForma" tipo="update" id="22">
	<!-- Setta tutto tranne vid, tp_forma_aut, ts_ins, ute_forza_ins e ute_ins.
	-->
			<fisso>
				UPDATE Tb_autore
				 SET
				  ute_var = XXXute_var , .........................
				WHERE
				  did = XXXdid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
		</fisso>
	</statement>
     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateScambioForma(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
		// CAMPI AGGIUNTIVI SU ISTANZA POSTGRESS
		// DOVRANNO ESSERE GESTITI CHIEDERE A ROSSANA

        log.debug("Tb_terminiThesautoResult metodo updateScambioForma invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_termine_thesauro",Tb_termine_thesauro.class);

		buildUpdate.addProperty("did",this.getParametro().get(KeyParameter.XXXdid));
		buildUpdate.addProperty("cd_the",this.getParametro().get(KeyParameter.XXXcd_the));
		buildUpdate.addProperty("ds_termine_thesauro",this.getParametro().get(KeyParameter.XXXds_termine_thesauro));
		//buildUpdate.addProperty("nota_termine_thesauro",this.getParametro().get(KeyParameter.XXXnota_termine_thesauro));
		buildUpdate.addProperty("ky_termine_thesauro",this.getParametro().get(KeyParameter.XXXky_termine_thesauro));
		buildUpdate.addProperty("ute_ins",this.getParametro().get(KeyParameter.XXXute_ins));
		buildUpdate.addProperty("ts_ins",this.getParametro().get(KeyParameter.XXXts_ins));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",this.getParametro().get(KeyParameter.XXXts_var));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("tp_forma_the",this.getParametro().get(KeyParameter.XXXtp_forma_the));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));

		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		//buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }
    public void updateScambioFormaSwitch(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
		// CAMPI AGGIUNTIVI SU ISTANZA POSTGRESS
		// DOVRANNO ESSERE GESTITI CHIEDERE A ROSSANA

        log.debug("Tb_terminiThesautoResult metodo updateScambioForma invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_termine_thesauro",Tb_termine_thesauro.class);

		buildUpdate.addProperty("cd_the","___");
		buildUpdate.addProperty("ds_termine_thesauro","X_X_X");
		buildUpdate.addProperty("ky_termine_thesauro","X_X_X");

		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		//buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }


	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_termine_thesauro.class;
	}

}
