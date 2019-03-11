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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_descrittoreCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
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
public class Tb_descrittoreResult extends Tb_descrittoreCommonDao{

    private Tb_descrittore tb_descrittore;

    public Tb_descrittoreResult(Tb_descrittore tb_descrittore) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_descrittore.leggiAllParametro());
        this.tb_descrittore = tb_descrittore;
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
	public List<Tb_descrittore> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_DESCRITTORE_selectPerKey");

			filter.setParameter(Tb_descrittoreCommonDao.XXXdid, opzioni
					.get(Tb_descrittoreCommonDao.XXXdid));

			myOpzioni.remove(Tb_descrittoreCommonDao.XXXdid);
			List<Tb_descrittore> result = this.basicCriteria
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
					AND ky_norm_descritt LIKE XXXstringaLike ||'%'
			</fisso>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_descrittore> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_DESCRITTORE_selectPerNomeLike");

			filter.setParameter(Tb_descrittoreCommonDao.XXXstringaLike, opzioni
					.get(Tb_descrittoreCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_descrittoreCommonDao.XXXstringaLike);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_descrittore",
                    this.basicCriteria, session);

			List<Tb_descrittore> result = this.basicCriteria
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
				SELECT COUNT (*) FROM tb_descrittore
				WHERE
					fl_canc !='S'
					AND ky_norm_descritt LIKE XXXstringaLike ||'%'
			</fisso>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
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
			Filter filter = session.enableFilter("TB_DESCRITTORE_countPerNomeLike");

			filter.setParameter(Tb_descrittoreCommonDao.XXXstringaLike, opzioni
					.get(Tb_descrittoreCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_descrittoreCommonDao.XXXstringaLike);

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
				  	CONTAINS(ds_descrittore, XXXparola1 ) > 0
		  	</fisso>
		<opzionale dipende="XXXparola2"> AND CONTAINS(ds_descrittore, XXXparola2 ) > 0 </opzionale>
		<opzionale dipende="XXXparola3"> AND CONTAINS(ds_descrittore, XXXparola3 ) > 0 </opzionale>
		<opzionale dipende="XXXparola4"> AND CONTAINS(ds_descrittore, XXXparola4 ) > 0 </opzionale>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_descrittore> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_DESCRITTORE_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_DESCRITTORE_selectPerParoleNome");
				filter.setParameter(Tb_descrittoreCommonDao.XXXparola1, opzioni
						.get(Tb_descrittoreCommonDao.XXXparola1));
				myOpzioni.remove(Tb_descrittoreCommonDao.XXXparola1);
			}
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_descrittore",
                    this.basicCriteria, session);

			List<Tb_descrittore> result = this.basicCriteria
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
				SELECT COUNT (*) FROM tb_descrittore
				WHERE
				fl_canc !='S' AND
				  	CONTAINS(ds_descrittore, XXXparola1 ) > 0
		  	</fisso>
		<opzionale dipende="XXXparola2"> AND CONTAINS(ds_descrittore, XXXparola2 ) > 0 </opzionale>
		<opzionale dipende="XXXparola3"> AND CONTAINS(ds_descrittore, XXXparola3 ) > 0 </opzionale>
		<opzionale dipende="XXXparola4"> AND CONTAINS(ds_descrittore, XXXparola4 ) > 0 </opzionale>
		<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
		<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
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
			if (isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_DESCRITTORE_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_DESCRITTORE_countPerParoleNome");
				filter.setParameter(Tb_descrittoreCommonDao.XXXparola1, opzioni
						.get(Tb_descrittoreCommonDao.XXXparola1));
				myOpzioni.remove(Tb_descrittoreCommonDao.XXXparola1);
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
					AND ky_norm_descritt = XXXstringaEsatta
			</fisso>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_descrittore> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_DESCRITTORE_selectPerNomeEsatto");

			filter.setParameter(Tb_descrittoreCommonDao.XXXstringaEsatta, opzioni
					.get(Tb_descrittoreCommonDao.XXXstringaEsatta));

			myOpzioni.remove(Tb_descrittoreCommonDao.XXXstringaEsatta);

			this.createCriteria(myOpzioni);
//            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
//                    "Tb_descrittore",
//                    this.basicCriteria, session);
//
			List<Tb_descrittore> result = this.basicCriteria
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
				SELECT COUNT (*) FROM tb_descrittore
				WHERE
					fl_canc !='S'
					AND ky_norm_descritt = XXXstringaEsatta
			</fisso>
		<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
		<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
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
            Filter filter = session.enableFilter("TB_DESCRITTORE_countPerNomeEsatto");

            filter.setParameter(Tb_descrittoreCommonDao.XXXstringaEsatta, opzioni
                    .get(Tb_descrittoreCommonDao.XXXstringaEsatta));

            myOpzioni.remove(Tb_descrittoreCommonDao.XXXstringaEsatta);

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
	<statement nome="selectPerKyNormDescrittore" tipo="select" id="Jenny_12">
		<fisso>
				WHERE
				  ky_norm_descritt = XXXky_norm_descritt
				AND cd_soggettario =XXXcd_soggettario
				AND fl_canc !='S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_descrittore> selectPerKyNormDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_DESCRITTORE_selectPerKyNormDescrittore");

			filter.setParameter(Tb_descrittoreCommonDao.XXXky_norm_descritt, opzioni
					.get(Tb_descrittoreCommonDao.XXXky_norm_descritt));
			filter.setParameter(Tb_descrittoreCommonDao.XXXcd_soggettario, opzioni
					.get(Tb_descrittoreCommonDao.XXXcd_soggettario));

			myOpzioni.remove(Tb_descrittoreCommonDao.XXXky_norm_descritt);
			myOpzioni.remove(Tb_descrittoreCommonDao.XXXcd_soggettario);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_descrittore",
                    this.basicCriteria, session);

			List<Tb_descrittore> result = this.basicCriteria
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
	public List<Tb_descrittore> selectPerEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_DESCRITTORE_selectPerEsistenza");

			filter.setParameter(Tb_descrittoreCommonDao.XXXdid, opzioni
					.get(Tb_descrittoreCommonDao.XXXdid));

			myOpzioni.remove(Tb_descrittoreCommonDao.XXXdid);
			List<Tb_descrittore> result = this.basicCriteria
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
        log.debug("Tb_descrittore metodo insert invocato ");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tb_descrittore.setTS_INS(ts);
        tb_descrittore.setTS_VAR(ts);
        session.save(this.tb_descrittore);
        this.commitTransaction();
        this.closeSession();

	}


	/**
	 * <statement nome="delete" tipo="delete" id="02">
		<fisso>
				DELETE FROM Tb_descrittore
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
        session.delete(this.tb_descrittore);
		this.commitTransaction();
		this.closeSession();

    }


	/**
	 * 	<statement nome="update" tipo="update" id="04">
		<fisso>
				UPDATE Tb_descrittore
				 SET
				  nota_descrittore = XXXnota_descrittore ,
				  cd_livello = XXXcd_livello ,
				  ds_descrittore = XXXds_descrittore ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  cd_soggettario = XXXcd_soggettario ,
				  fl_canc = XXXfl_canc ,
				  ky_norm_descritt = XXXky_norm_descritt,
	  			  tp_forma_des = XXXtp_forma_des
				WHERE
				  did = XXXdid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub

		log.debug("Tb_descrittore metodo update invocato");
		Session session = this.getSession();
		this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tb_descrittore", Tb_descrittore.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("nota_descrittore",	params.get(KeyParameter.XXXnota_descrittore));
		buildUpdate.addProperty("cd_livello", params.get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("ds_descrittore", params.get(KeyParameter.XXXds_descrittore));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("cd_soggettario", params.get(KeyParameter.XXXcd_soggettario));
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ky_norm_descritt", params.get(KeyParameter.XXXky_norm_descritt));
		buildUpdate.addProperty("tp_forma_des", params.get(KeyParameter.XXXtp_forma_des));
		buildUpdate.addProperty("fl_condiviso", params.get(KeyParameter.XXXfl_condiviso));

		// almaviva5_20120322 evolutive CFI
		buildUpdate.addProperty("cd_edizione", params.get(KeyParameter.XXXcd_edizione));
		buildUpdate.addProperty("cat_termine", params.get(KeyParameter.XXXcat_termine));

		buildUpdate.addWhere("did", params.get(KeyParameter.XXXdid), "=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}

	/**
	<statement nome="cancellaDescrittore" tipo="update" id="Jenny_11">
	<fisso>
			UPDATE Tb_descrittore
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
    public void cancellaDescrittore(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_descrittore metodo cancellaDescrittore invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_descrittore",Tb_descrittore.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }


	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_descrittore.class;
	}

}
