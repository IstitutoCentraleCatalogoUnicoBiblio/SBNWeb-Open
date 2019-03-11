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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_autoreCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Category;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class Tb_autoreResult extends Tb_autoreCommonDao {
	static Category log = Category.getInstance(Tb_autoreResult.class);
    private Tb_autore tb_autore;

	public Tb_autoreResult(Tb_autore tb_autore) throws InfrastructureException {
		super();
        this.valorizzaParametro(tb_autore.leggiAllParametro());
        this.tb_autore = tb_autore;

	}

	/*
	 * <statement nome="selectPerKey" tipo="select" id="01_taymer"> <fisso>
	 * WHERE fl_canc !='S' AND vid = XXXvid </fisso> </statement>
	 */
	public List<Tb_autore> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerKey");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			//HBSbnInterceptor interceptor = new HBSbnInterceptor();
			Session session = this.getSession();

			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectPerKey");
			filter.setParameter(Tb_autoreCommonDao.XXXvid, opzioni.get(Tb_autoreCommonDao.XXXvid));

			myOpzioni.remove(Tb_autoreCommonDao.XXXvid);
			List<Tb_autore> result = this.basicCriteria.list();
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
	 * <statement nome="selectEsistenzaId" tipo="select" id="02_taymer"> <fisso>
	 * WHERE vid = XXXvid </fisso> </statement>
	 */
	public List<Tb_autore> selectEsistenzaId(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectEsistenzaId");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectEsistenzaId");
			filter.setParameter(Tb_autoreResult.XXXvid, opzioni.get(Tb_autoreResult.XXXvid));
			myOpzioni.remove(Tb_autoreCommonDao.XXXvid);
			List<Tb_autore> result = this.basicCriteria.list();
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
	 * <statement nome="selectPerIsadn" tipo="select" id="03_taymer"> <fisso>
	 * WHERE fl_canc !='S' AND isadn = XXXisadn </fisso> </statement>
	 */
	public List<Tb_autore> selectPerIsadn(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerIsadn");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectPerIsadn");
			filter.setParameter(Tb_autoreCommonDao.XXXisadn, opzioni.get(Tb_autoreCommonDao.XXXisadn));
			myOpzioni.remove(Tb_autoreCommonDao.XXXisadn);
			List<Tb_autore> result = this.basicCriteria.list();
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
	 * <statement nome="selectPerNomeLike" tipo="select_campi" id="04_taymer">
	 * <fisso> WHERE fl_canc !='S' AND ( (ky_cles1_a like XXXky_cles1_a || '%'
	 * </fisso> <opzionale dipende="XXXky_cles2_a"> AND ky_cles2_a like
	 * XXXky_cles2_a || '%' </opzionale> <opzionale dipende="XXXcles2_1"> ) OR
	 * (ky_cles1_a like XXXcles2_1 || '%' </opzionale> <opzionale
	 * dipende="XXXcles2_2"> AND ky_cles2_a like XXXcles2_2 || '%' </opzionale>
	 * <opzionale dipende="XXXky_cles1_a"> ) ) </opzionale> <opzionale
	 * dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut
	 * </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> AND ( tp_nome_aut = XXXtipoNome1</opzionale>
	 * <opzionale dipende="XXXtipoNome2"> OR tp_nome_aut = XXXtipoNome2
	 * </opzionale> <opzionale dipende="XXXtipoNome3"> OR tp_nome_aut =
	 * XXXtipoNome3 </opzionale> <opzionale dipende="XXXtipoNome4"> OR
	 * tp_nome_aut = XXXtipoNome4 </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> ) </opzionale> <opzionale dipende="XXXcd_paese">
	 * AND cd_paese = XXXcd_paese</opzionale> <opzionale
	 * dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da
	 * </opzionale> <opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;=
	 * XXXdataInizio_A </opzionale> <opzionale dipende="XXXdataFine_Da"> AND
	 * aa_morte &gt;= XXXdataFine_Da </opzionale> <opzionale
	 * dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
	 * <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd')
	 * &gt;= XXXdata_var_Da </opzionale> <opzionale dipende="XXXdata_var_A"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale> <opzionale
	 * dipende="XXXesporta_ts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;=
	 * XXXesporta_ts_var_da </opzionale> <opzionale
	 * dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins = ts_var AND
	 * to_char(ts_var,'yyyy-mm-dd') &gt;= XXXesporta_ts_var_e_ts_ins_da
	 * </opzionale> <opzionale dipende="XXXesporta_ts_var_a"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXesporta_ts_var_a </opzionale>
	 * <opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd')
	 * &gt;= XXXdata_ins_Da </opzionale> <opzionale dipende="XXXdata_ins_A"> AND
	 * to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerNomeLike");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectPerNomeLike");
			this.kycleslike=true;
            myOpzioni.remove(XXXky_auteur);
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria.list();
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
 *  <statement nome="countPerNomeLike" tipo="count" id="09">
            <fisso>
            SELECT COUNT (*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                (
                (ky_cles1_a like XXXky_cles1_a || '%'
            </fisso>
 *     <filter name="TB_AUTORE_countPerNomeLike" condition="fl_canc !='S' "/>

 */
    public Integer countPerNomeLike(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            session.enableFilter("TB_AUTORE_countPerNomeLike");

            this.kycleslike=true;
            myOpzioni.remove(XXXky_auteur);
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
	 * <statement nome="selectPerNome" tipo="select" id="05"> <fisso> WHERE
	 * fl_canc !='S' AND ( (ky_cles1_a = XXXky_cles1_a </fisso> <opzionale
	 * dipende="XXXky_cles2_a"> AND ky_cles2_a = XXXky_cles2_a </opzionale>
	 * <opzionale dipende="XXXky_cles2_anull"> AND ky_cles2_a is null
	 * </opzionale> <opzionale dipende="XXXky_cles1_a"> ) </opzionale>
	 * <opzionale dipende="XXXcles2_1"> OR (ky_cles1_a = XXXcles2_1 </opzionale>
	 * <opzionale dipende="XXXcles2_2"> AND ky_cles2_a = XXXcles2_2 </opzionale>
	 * <opzionale dipende="XXXcles2_2null"> AND ky_cles2_a is null </opzionale>
	 * <opzionale dipende="XXXcles2_1"> ) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a"> ) </opzionale> <opzionale
	 * dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut
	 * </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> AND ( tp_nome_aut = XXXtipoNome1</opzionale>
	 * <opzionale dipende="XXXtipoNome2"> OR tp_nome_aut = XXXtipoNome2
	 * </opzionale> <opzionale dipende="XXXtipoNome3"> OR tp_nome_aut =
	 * XXXtipoNome3 </opzionale> <opzionale dipende="XXXtipoNome4"> OR
	 * tp_nome_aut = XXXtipoNome4 </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> ) </opzionale> <opzionale dipende="XXXcd_paese">
	 * AND cd_paese = XXXcd_paese</opzionale> <opzionale
	 * dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da
	 * </opzionale> <opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;=
	 * XXXdataInizio_A </opzionale> <opzionale dipende="XXXdataFine_Da"> AND
	 * aa_morte &gt;= XXXdataFine_Da </opzionale> <opzionale
	 * dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
	 * <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd')
	 * &gt;= XXXdata_var_Da </opzionale> <opzionale dipende="XXXdata_var_A"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale> <opzionale
	 * dipende="XXXesporta_ts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;=
	 * XXXesporta_ts_var_da </opzionale> <opzionale
	 * dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins = ts_var AND
	 * to_char(ts_var,'yyyy-mm-dd') &gt;= XXXesporta_ts_var_e_ts_ins_da
	 * </opzionale> <opzionale dipende="XXXesporta_ts_var_a"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXesporta_ts_var_a </opzionale>
	 * <opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd')
	 * &gt;= XXXdata_ins_Da </opzionale> <opzionale dipende="XXXdata_ins_A"> AND
	 * to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectPerNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerNome");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectPerNome");
			this.kycleslike=false;

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
    <statement nome="countPerNome" tipo="count" id="10">
            <fisso>
            SELECT COUNT (*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                (
                (ky_cles1_a = XXXky_cles1_a
            </fisso>
        <filter name="TB_AUTORE_countPerNome" condition="fl_canc !='S' "/>


     */
        public Integer countPerNome(HashMap opzioni)
                throws InfrastructureException {
            try {
                HashMap myOpzioni = (HashMap) opzioni.clone();
                Session session = this.getSession();
                this.beginTransaction();
                this.basicCriteria = session.createCriteria(getTarget());
                Filter filter = session.enableFilter("TB_AUTORE_countPerNome");


                this.kycleslike=false;

                this.createCriteria(myOpzioni);

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


	/*
	 * <statement nome="selectSimile" tipo="select" id="06"> <fisso> WHERE
	 * fl_canc !='S' AND ky_cautun = XXXky_cautun AND ky_auteur = XXXky_auteur
	 * AND ky_cles1_a = XXXky_cles1_a AND tp_nome_aut IN ('A','B','C','D')
	 * </fisso> <opzionale dipende="XXXvid"> AND vid != XXXvid </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectSimile(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectSimile");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectSimile");
			filter.setParameter(Tb_autoreCommonDao.XXXky_cautun, opzioni
					.get(Tb_autoreCommonDao.XXXky_cautun));
			filter.setParameter(Tb_autoreCommonDao.XXXky_auteur, opzioni
					.get(Tb_autoreCommonDao.XXXky_auteur));

			myOpzioni.remove(Tb_autoreCommonDao.XXXky_cautun);
			myOpzioni.remove(Tb_autoreCommonDao.XXXky_auteur);
			this.kycleslike=false;

//            this.createCriteria(myOpzioni);
//            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
//                    "Tb_autore",
//                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
					.list();
			// NON SI SA PERCHE MA VA IN ERRORE
			// this.commitTransaction();
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
    <statement nome="countSimile" tipo="count" id="11">
            <fisso>
                SELECT COUNT (*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                  ky_cautun = XXXky_cautun
                  AND
                  ky_auteur = XXXky_auteur
                  </fisso>
    </statement>
    <filter name="TB_AUTORE_countSimile"
        condition="fl_canc !='S'
          AND ky_cautun = :XXXky_cautun
          AND ky_auteur = :XXXky_auteur "/>


           */
	public Integer countSimile(HashMap opzioni) throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_AUTORE_countSimile");
            filter.setParameter(Tb_autoreCommonDao.XXXky_cautun, opzioni.get(Tb_autoreCommonDao.XXXky_cautun));
            filter.setParameter(Tb_autoreCommonDao.XXXky_auteur, opzioni.get(Tb_autoreCommonDao.XXXky_auteur));

            myOpzioni.remove(Tb_autoreCommonDao.XXXky_cautun);
            myOpzioni.remove(Tb_autoreCommonDao.XXXky_auteur);

            this.kycleslike=false;

            this.createCriteria(myOpzioni);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(Projections.rowCount()))
                    .uniqueResult();

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
     * <statement nome="selectSimileEnti_1" tipo="select" id="06"> <fisso> WHERE
     * fl_canc !='S' AND ky_cautun = XXXky_cautun AND tp_nome_aut =
     * XXXtp_nome_aut AND ky_auteur = XXXky_auteur </fisso> <opzionale
     * dipende="XXXds_nome_cerca_simili">AND TRIM (UPPER (ds_nome_aut)) =
     * XXXds_nome_cerca_simili</opzionale> <opzionale
     * dipende="XXXtp_forma_conferma">AND tp_forma_aut = XXXtp_forma_conferma</opzionale>
     * <opzionale dipende="XXXky_el1">AND ky_el1_a || ky_el1_b = XXXky_el1</opzionale>
     * <opzionale dipende="XXXky_el1null">AND ky_el1_a is null AND ky_el1_b is
     * null</opzionale> <opzionale dipende="XXXky_el2">AND ky_el2_a || ky_el2_b =
     * XXXky_el2</opzionale> <opzionale dipende="XXXky_el2null">AND ky_el2_a is
     * null AND ky_el2_b is null</opzionale> <opzionale dipende="XXXky_el3">AND
     * ky_el3 = XXXky_el3</opzionale> <opzionale dipende="XXXky_el3null">AND
     * ky_el3 is null</opzionale> <opzionale dipende="XXXky_el4">AND ky_el4 =
     * XXXky_el4</opzionale> <opzionale dipende="XXXky_el4null">AND ky_el4 is
     * null</opzionale> <opzionale dipende="XXXky_el5">AND ky_el5 = XXXky_el5</opzionale>
     * <opzionale dipende="XXXky_el5null">AND ky_el5 is null</opzionale>
     * <opzionale dipende="XXXvid"> AND vid != XXXvid </opzionale> </statement>
     */
	public List<Tb_autore> selectSimileEnti_1(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectSimileEnti_1");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_AUTORE_selectSimileEnti_1");
			filter.setParameter(Tb_autoreCommonDao.XXXky_cautun, opzioni
					.get(Tb_autoreCommonDao.XXXky_cautun));
			filter.setParameter(Tb_autoreCommonDao.XXXky_auteur, opzioni
					.get(Tb_autoreCommonDao.XXXky_auteur));
			filter.setParameter(Tb_autoreCommonDao.XXXtp_nome_aut, opzioni
					.get(Tb_autoreCommonDao.XXXtp_nome_aut));

			myOpzioni.remove(Tb_autoreCommonDao.XXXky_cautun);
			myOpzioni.remove(Tb_autoreCommonDao.XXXky_auteur);
			myOpzioni.remove(Tb_autoreCommonDao.XXXtp_nome_aut);
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectSimileEnti_2" tipo="select" id="07_taymer">
	 * <fisso> WHERE fl_canc !='S' AND ky_cles1_a = XXXky_cles1_a AND
	 * tp_nome_aut IN ('E','R','G') </fisso> <opzionale dipende="XXXky_cles2_a">
	 * AND ky_cles2_a = XXXky_cles2_a </opzionale> <opzionale dipende="XXXvid">
	 * AND vid != XXXvid </opzionale> </statement>
	 */
	public List<Tb_autore> selectSimileEnti_2(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectSimileEnti_2");
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectSimileEnti_2");
			this.kycleslike=false;
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectSimileConferma" tipo="select" id="08_taymer">
	 * <fisso> WHERE fl_canc !='S' AND ky_cautun = XXXky_cautun AND ky_auteur =
	 * XXXky_auteur AND ky_cles1_a = XXXky_cles1_a AND tp_forma_aut =
	 * XXXtp_forma_aut AND TRIM ( UPPER (ds_nome_aut)) = TRIM (UPPER
	 * (XXXds_nome_aut)) </fisso> <opzionale dipende="XXXky_cles2_a"> AND
	 * ky_cles2_a = XXXky_cles2_a </opzionale> <opzionale
	 * dipende="XXXky_cles2_anull"> AND ky_cles2_a is null </opzionale>
	 * <opzionale dipende="XXXky_el1">AND ky_el1_a || ky_el1_b = XXXky_el1</opzionale>
	 * <opzionale dipende="XXXky_el1null">AND ky_el1_a is null AND ky_el1_b is
	 * null</opzionale> <opzionale dipende="XXXky_el2">AND ky_el2_a || ky_el2_b =
	 * XXXky_el2</opzionale> <opzionale dipende="XXXky_el2null">AND ky_el2_a is
	 * null AND ky_el2_b is null</opzionale> <opzionale dipende="XXXky_el3">AND
	 * ky_el3 = XXXky_el3</opzionale> <opzionale dipende="XXXky_el3null">AND
	 * ky_el3 is null</opzionale> <opzionale dipende="XXXky_el4">AND ky_el4 =
	 * XXXky_el4</opzionale> <opzionale dipende="XXXky_el4null">AND ky_el4 is
	 * null</opzionale> <opzionale dipende="XXXky_el5">AND ky_el5 = XXXky_el5</opzionale>
	 * <opzionale dipende="XXXky_el5null">AND ky_el5 is null</opzionale>
	 * <opzionale dipende="XXXvid"> AND vid != XXXvid </opzionale> </statement>
	 */
	public List<Tb_autore> selectSimileConferma(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectSimileConferma");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_AUTORE_selectSimileConferma");
			filter.setParameter(Tb_autoreCommonDao.XXXky_cautun, opzioni
					.get(Tb_autoreCommonDao.XXXky_cautun));
			filter.setParameter(Tb_autoreCommonDao.XXXky_auteur, opzioni
					.get(Tb_autoreCommonDao.XXXky_auteur));
			filter.setParameter(Tb_autoreCommonDao.XXXds_nome_aut, opzioni
					.get(Tb_autoreCommonDao.XXXds_nome_aut));

			myOpzioni.remove(Tb_autoreCommonDao.XXXky_cautun);
			myOpzioni.remove(Tb_autoreCommonDao.XXXky_auteur);
			myOpzioni.remove(Tb_autoreCommonDao.XXXds_nome_aut);
			this.kycleslike=false;

            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);


			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectPerDatavar" tipo="select" id="07"> <fisso> WHERE
	 * fl_canc !='S' AND to_char(ts_var,'yyyy-mm-dd') &gt; XXXdata_var_Da AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt; XXXdata_var_A </fisso> <opzionale
	 * dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut
	 * </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> AND ( tp_nome_aut = XXXtipoNome1</opzionale>
	 * <opzionale dipende="XXXtipoNome2"> OR tp_nome_aut = XXXtipoNome2
	 * </opzionale> <opzionale dipende="XXXtipoNome3"> OR tp_nome_aut =
	 * XXXtipoNome3 </opzionale> <opzionale dipende="XXXtipoNome4"> OR
	 * tp_nome_aut = XXXtipoNome4 </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> ) </opzionale> <opzionale dipende="XXXcd_paese">
	 * AND cd_paese = XXXcd_paese</opzionale> <opzionale
	 * dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da
	 * </opzionale> <opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;=
	 * XXXdataInizio_A </opzionale> <opzionale dipende="XXXdataFine_Da"> AND
	 * aa_morte &gt;= XXXdataFine_Da </opzionale> <opzionale
	 * dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectPerDatavar(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerDatavar");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectPerDatavar");
			filter.setParameter(Tb_autoreCommonDao.XXXdata_var_Da, myOpzioni
					.get(Tb_autoreCommonDao.XXXdata_var_Da));
			filter.setParameter(Tb_autoreCommonDao.XXXdata_var_A, myOpzioni
					.get(Tb_autoreCommonDao.XXXdata_var_A));

			myOpzioni.remove(Tb_autoreCommonDao.XXXdata_var_Da);
			myOpzioni.remove(Tb_autoreCommonDao.XXXdata_var_A);
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);


			List<Tb_autore> result = this.basicCriteria
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
     *  <statement nome="countPerDatavar" tipo="count" id="12">
            <fisso>
            SELECT COUNT(*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                   to_char(ts_var,'yyyy-mm-dd') &gt; XXXdata_var_Da
                  AND to_char(ts_var,'yyyy-mm-dd') &lt;  XXXdata_var_A
            </fisso>

    <filter name="TB_AUTORE_countPerDatavar"
        condition="fl_canc !='S'
          AND to_char(ts_var,'yyyy-mm-dd') &gt; :XXXdata_var_Da
          AND to_char(ts_var,'yyyy-mm-dd') &lt;  :XXXdata_var_A "/>

     */
    public Integer countPerDatavar(HashMap opzioni) throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_AUTORE_countPerDatavar");
            filter.setParameter(Tb_autoreCommonDao.XXXdata_var_Da, myOpzioni.get(Tb_autoreCommonDao.XXXdata_var_Da));
            filter.setParameter(Tb_autoreCommonDao.XXXdata_var_A, myOpzioni.get(Tb_autoreCommonDao.XXXdata_var_A));

            myOpzioni.remove(Tb_autoreCommonDao.XXXdata_var_Da);
            myOpzioni.remove(Tb_autoreCommonDao.XXXdata_var_A);

            this.kycleslike=false;

            this.createCriteria(myOpzioni);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(Projections.rowCount()))
                    .uniqueResult();

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
	 * <statement nome="selectPerParoleNome" tipo="select" id="08"> <fisso>
	 * WHERE fl_canc !='S' AND CONTAINS(ds_nome_aut, XXXparola1 ) &gt; 0
	 * </fisso> <opzionale dipende="XXXparola2"> AND CONTAINS(ds_nome_aut,
	 * XXXparola2 ) &gt; 0 </opzionale> <opzionale dipende="XXXparola3"> AND
	 * CONTAINS(ds_nome_aut, XXXparola3 ) &gt; 0 </opzionale> <opzionale
	 * dipende="XXXparola4"> AND CONTAINS(ds_nome_aut, XXXparola4 ) &gt; 0
	 * </opzionale> <opzionale dipende="XXXtp_forma_aut"> AND tp_forma_aut =
	 * XXXtp_forma_aut </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND
	 * cd_livello &gt;= XXXlivello_aut_da </opzionale> <opzionale
	 * dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a
	 * </opzionale> <opzionale dipende="XXXtipoNome1"> AND ( tp_nome_aut =
	 * XXXtipoNome1</opzionale> <opzionale dipende="XXXtipoNome2"> OR
	 * tp_nome_aut = XXXtipoNome2 </opzionale> <opzionale
	 * dipende="XXXtipoNome3"> OR tp_nome_aut = XXXtipoNome3 </opzionale>
	 * <opzionale dipende="XXXtipoNome4"> OR tp_nome_aut = XXXtipoNome4
	 * </opzionale> <opzionale dipende="XXXtipoNome1"> ) </opzionale> <opzionale
	 * dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese</opzionale> <opzionale
	 * dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da
	 * </opzionale> <opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;=
	 * XXXdataInizio_A </opzionale> <opzionale dipende="XXXdataFine_Da"> AND
	 * aa_morte &gt;= XXXdataFine_Da </opzionale> <opzionale
	 * dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
	 * <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd')
	 * &gt;= XXXdata_var_Da </opzionale> <opzionale dipende="XXXdata_var_A"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale> <opzionale
	 * dipende="XXXesporta_ts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;=
	 * XXXesporta_ts_var_da </opzionale> <opzionale
	 * dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins = ts_var AND
	 * to_char(ts_var,'yyyy-mm-dd') &gt;= XXXesporta_ts_var_e_ts_ins_da
	 * </opzionale> <opzionale dipende="XXXesporta_ts_var_a"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXesporta_ts_var_a </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerParoleNome");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_AUTORE_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_AUTORE_selectPerParoleNome");
				filter.setParameter(Tb_autoreCommonDao.XXXparola1, opzioni
						.get(Tb_autoreCommonDao.XXXparola1));
				myOpzioni.remove(Tb_autoreCommonDao.XXXparola1);
			}
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
    <statement nome="countPerParoleNome" tipo="count" id="13">
            <fisso>
                SELECT COUNT(*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                    CONTAINS(ds_nome_aut, XXXparola1 ) &gt; 0
            </fisso>

    <filter name="TB_AUTORE_countPerParoleNome" condition="fl_canc !='S' AND CONTAINS(ds_nome_aut, :XXXparola1 ) &gt; 0 "/>

     */
    public Integer countPerParoleNome(HashMap opzioni) throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if(isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_AUTORE_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_AUTORE_countPerParoleNome");
	            filter.setParameter(Tb_autoreCommonDao.XXXparola1, opzioni.get(Tb_autoreCommonDao.XXXparola1));
	            myOpzioni.remove(Tb_autoreCommonDao.XXXparola1);
			}
            this.kycleslike=false;
            this.createCriteria(myOpzioni);
            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(Projections.rowCount()))
                    .uniqueResult();

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
	 * <statement nome="selectPerCautun" tipo="select" id="23_taymer"> <fisso>
	 * WHERE fl_canc !='S' AND ky_cautun = XXXKy_cautun </fisso> <opzionale
	 * dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut
	 * </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> AND ( tp_nome_aut = XXXtipoNome1</opzionale>
	 * <opzionale dipende="XXXtipoNome2"> OR tp_nome_aut = XXXtipoNome2
	 * </opzionale> <opzionale dipende="XXXtipoNome3"> OR tp_nome_aut =
	 * XXXtipoNome3 </opzionale> <opzionale dipende="XXXtipoNome4"> OR
	 * tp_nome_aut = XXXtipoNome4 </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> ) </opzionale> <opzionale dipende="XXXcd_paese">
	 * AND cd_paese = XXXcd_paese</opzionale> <opzionale
	 * dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da
	 * </opzionale> <opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;=
	 * XXXdataInizio_A </opzionale> <opzionale dipende="XXXdataFine_Da"> AND
	 * aa_morte &gt;= XXXdataFine_Da </opzionale> <opzionale
	 * dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
	 * <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd')
	 * &gt;= XXXdata_var_Da </opzionale> <opzionale dipende="XXXdata_var_A"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;=
	 * XXXdata_ins_Da </opzionale> <opzionale dipende="XXXdata_ins_A"> AND
	 * to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectPerCautun(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerCautun");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectPerCautun");
			filter.setParameter(Tb_autoreCommonDao.XXXKy_cautun, opzioni
					.get(Tb_autoreCommonDao.XXXKy_cautun));
			myOpzioni.remove(Tb_autoreCommonDao.XXXKy_cautun);
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
    <statement nome="countPerCautun" tipo="count" id="24_taymer">
            <fisso>
            SELECT COUNT (*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                ky_cautun = XXXKy_cautun
            </fisso>

    <filter name="TB_AUTORE_countPerCautun" condition="fl_canc !='S' AND ky_cautun = :XXXKy_cautun "/>

     */
    public Integer countPerCautun(HashMap opzioni) throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_AUTORE_countPerCautun");
            filter.setParameter(Tb_autoreCommonDao.XXXKy_cautun, opzioni
                    .get(Tb_autoreCommonDao.XXXKy_cautun));
            myOpzioni.remove(Tb_autoreCommonDao.XXXKy_cautun);

            this.kycleslike=false;

            this.createCriteria(myOpzioni);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(Projections.rowCount()))
                    .uniqueResult();

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
	 * WHERE fl_canc !='S' AND ky_auteur = XXXKy_auteur </fisso> <opzionale
	 * dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut
	 * </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> AND ( tp_nome_aut = XXXtipoNome1</opzionale>
	 * <opzionale dipende="XXXtipoNome2"> OR tp_nome_aut = XXXtipoNome2
	 * </opzionale> <opzionale dipende="XXXtipoNome3"> OR tp_nome_aut =
	 * XXXtipoNome3 </opzionale> <opzionale dipende="XXXtipoNome4"> OR
	 * tp_nome_aut = XXXtipoNome4 </opzionale> <opzionale
	 * dipende="XXXtipoNome1"> ) </opzionale> <opzionale dipende="XXXcd_paese">
	 * AND cd_paese = XXXcd_paese</opzionale> <opzionale
	 * dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da
	 * </opzionale> <opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;=
	 * XXXdataInizio_A </opzionale> <opzionale dipende="XXXdataFine_Da"> AND
	 * aa_morte &gt;= XXXdataFine_Da </opzionale> <opzionale
	 * dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
	 * <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd')
	 * &gt;= XXXdata_var_Da </opzionale> <opzionale dipende="XXXdata_var_A"> AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;=
	 * XXXdata_ins_Da </opzionale> <opzionale dipende="XXXdata_ins_A"> AND
	 * to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectPerAuteur(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectPerAuteur");
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectPerAuteur");
			filter.setParameter(Tb_autoreCommonDao.XXXKy_auteur, opzioni
					.get(Tb_autoreCommonDao.XXXKy_auteur));
			myOpzioni.remove(Tb_autoreCommonDao.XXXKy_auteur);
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
    <statement nome="countPerAuteur" tipo="count" id="26_taymer">
            <fisso>
            SELECT COUNT (*) FROM tb_autore
                WHERE
                fl_canc !='S' AND
                ky_auteur = XXXKy_auteur
            </fisso>

    <filter name="TB_AUTORE_countPerAuteur" condition="fl_canc !='S' AND ky_auteur = :XXXKy_auteur "/>

     */
    public Integer countPerAuteur(HashMap opzioni) throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_AUTORE_countPerAuteur");
            filter.setParameter(Tb_autoreCommonDao.XXXKy_auteur, opzioni
                    .get(Tb_autoreCommonDao.XXXKy_auteur));
            myOpzioni.remove(Tb_autoreCommonDao.XXXKy_auteur);

            this.kycleslike = false;

            this.createCriteria(myOpzioni);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(Projections.rowCount()))
                    .uniqueResult();

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

	// <filter name="TB_AUTORE_countPerNomeLike" condition="fl_canc !='S' "/>
	// <filter name="TB_AUTORE_countPerNome" condition="fl_canc !='S' AND (
	// (ky_cles1_a = :XXXky_cles1_a "/>
	// <filter name="TB_AUTORE_countSimile" condition="fl_canc !='S' AND
	// ky_cautun = :XXXky_cautun AND ky_auteur = :XXXky_auteur "/>
	// <filter name="TB_AUTORE_countPerDatavar" condition="fl_canc !='S' AND
	// to_char(ts_var,'yyyy-mm-dd') &gt; :XXXdata_var_Da AND
	// to_char(ts_var,'yyyy-mm-dd') &lt; :XXXdata_var_A "/>
	// <filter name="TB_AUTORE_countPerParoleNome" condition="fl_canc !='S' AND
	// CONTAINS(ds_nome_aut, :XXXparola1 ) &gt; 0 "/>
	// <filter name="TB_AUTORE_countPerCautun" condition="fl_canc !='S' AND
	// ky_cautun = :XXXKy_cautun "/>
	// <filter name="TB_AUTORE_countPerAuteur" condition="fl_canc !='S' AND
	// ky_auteur = :XXXKy_auteur "/>
	// <filter name="TB_AUTORE_countPerDatavar" condition="fl_canc !='S' AND
	// to_char(ts_var,'yyyy-mm-dd') &gt; :XXXdata_var_da AND
	// to_char(ts_var,'yyyy-mm-dd') &lt; :XXXdata_var_a "/>







	// NON IMPLEMENTATE
	/*
	 * <statement nome="selectAutoriNoRaffinamento" tipo="select" id="17">
	 * <fisso> WHERE fl_canc != 'S'
	 *
	 * </fisso> <opzionale dipende="XXXcd_livello">AND XXXcd_livello &gt;=
	 * TO_NUMBER(cd_livello)</opzionale> <opzionale dipende="XXXute_ins">AND
	 * UPPER(ute_ins) LIKE UPPER(XXXute_ins)||'%'</opzionale> <opzionale
	 * dipende="XXXute_var">AND UPPER(ute_var) LIKE UPPER(XXXute_var)||'%'</opzionale>
	 * <opzionale dipende="XXXdata_inizio_inserimento"> AND
	 * to_char(ts_ins,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_inserimento
	 * </opzionale> <opzionale dipende="XXXdata_fine_inserimento"> AND
	 * to_char(ts_ins,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_inserimento
	 * </opzionale> <opzionale dipende="XXXdata_inizio_variazione"> AND
	 * to_char(ts_var,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_variazione
	 * </opzionale> <opzionale dipende="XXXdata_fine_variazione"> AND
	 * to_char(ts_var,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_variazione
	 * </opzionale> <opzionale dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a
	 * LIKE UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> </statement>
	 */
	public List<Tb_autore> selectAutoriNoRaffinamento(HashMap opzioni)
			throws InfrastructureException {
		try {
			log.debug("selectAutoriNoRaffinamento");
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectAutoriNoRaffinamento");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectAutoriTipoCD" tipo="select" id="18"> <fisso> WHERE
	 * fl_canc != 'S' AND (tp_nome_aut = 'C' OR tp_nome_aut = 'D') </fisso>
	 * <opzionale dipende="XXXcd_livello">AND XXXcd_livello &gt;=
	 * TO_NUMBER(cd_livello)</opzionale> <opzionale dipende="XXXute_ins">AND
	 * UPPER(ute_ins) LIKE UPPER(XXXute_ins)||'%'</opzionale> <opzionale
	 * dipende="XXXute_var">AND UPPER(ute_var) LIKE UPPER(XXXute_var)||'%'</opzionale>
	 * <opzionale dipende="XXXdata_inizio_inserimento"> AND
	 * to_char(ts_ins,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_inserimento
	 * </opzionale> <opzionale dipende="XXXdata_fine_inserimento"> AND
	 * to_char(ts_ins,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_inserimento
	 * </opzionale> <opzionale dipende="XXXdata_inizio_variazione"> AND
	 * to_char(ts_var,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_variazione
	 * </opzionale> <opzionale dipende="XXXdata_fine_variazione"> AND
	 * to_char(ts_var,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_variazione
	 * </opzionale> <opzionale dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a
	 * LIKE UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> </statement>
	 */
	public List<Tb_autore> selectAutoriTipoCD(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectAutoriTipoCD");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectAutoriTipo" tipo="select" id="19"> <fisso> WHERE
	 * fl_canc != 'S' </fisso> <opzionale dipende="XXXtp_nome_aut">AND
	 * tp_nome_aut = XXXtp_nome_aut </opzionale> <opzionale
	 * dipende="XXXcd_livello">AND XXXcd_livello &gt;= TO_NUMBER(cd_livello)</opzionale>
	 * <opzionale dipende="XXXute_ins">AND UPPER(ute_ins) LIKE
	 * UPPER(XXXute_ins)||'%'</opzionale> <opzionale dipende="XXXute_var">AND
	 * UPPER(ute_var) LIKE UPPER(XXXute_var)||'%'</opzionale> <opzionale
	 * dipende="XXXdata_inizio_inserimento"> AND
	 * to_char(ts_ins,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_inserimento
	 * </opzionale> <opzionale dipende="XXXdata_fine_inserimento"> AND
	 * to_char(ts_ins,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_inserimento
	 * </opzionale> <opzionale dipende="XXXdata_inizio_variazione"> AND
	 * to_char(ts_var,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_variazione
	 * </opzionale> <opzionale dipende="XXXdata_fine_variazione"> AND
	 * to_char(ts_var,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_variazione
	 * </opzionale> <opzionale dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a
	 * LIKE UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> </statement>
	 */
	public List<Tb_autore> selectAutoriTipo(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectAutoriTipo");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectVidCreatiPerPolo" tipo="count" id="31_william">
	 */
	public List<Tb_autore> selectVidCreatiPerPolo(HashMap opzioni)
			throws InfrastructureException {
		// TODO da implementare
		return null;
	}

	/*
	 * <statement nome="selectElencoAutoriVid" tipo="select" id="16_william">
	 * <fisso> WHERE fl_canc != 'S' and UPPER(vid) LIKE UPPER('%'||XXXvid||'%')
	 * </fisso> </statement>
	 */
	public List<Tb_autore> selectElencoAutoriVid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectElencoAutoriVid");
			filter.setParameter(Tb_autoreCommonDao.XXXvid, opzioni.get(Tb_autoreCommonDao.XXXvid));
			myOpzioni.remove(Tb_autoreCommonDao.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_autore",
                    this.basicCriteria, session);
			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectTipoForma" tipo="select_campi" id="33_william">
	 */
	public List<Tb_autore> selectTipoForma(HashMap opzioni)
			throws InfrastructureException {
		// TODO da implementare
		return null;
	}

	/*
	 * <statement nome="selectElencoAutori" tipo="select" id="14_william">
	 * <fisso> WHERE fl_canc != 'S' </fisso> <opzionale
	 * dipende="XXXautore_parentesi">and ( </opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_a">tp_nome_aut = 'A' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_b">tp_nome_aut = 'B' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_c">tp_nome_aut = 'C' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_d">tp_nome_aut = 'D' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_e">tp_nome_aut = 'E' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_r">tp_nome_aut = 'R' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_g">tp_nome_aut = 'G' or</opzionale> <opzionale
	 * dipende="XXXautore_parentesi"> tp_nome_aut =XXXautore_parentesi )
	 * </opzionale> <opzionale dipende="XXXtp_forma_aut"> AND tp_forma_aut=
	 * XXXtp_forma_aut </opzionale> <opzionale
	 * dipende="XXXdata_inizio_variazione"> AND to_char(ts_var,'dd/mm/yyyy')
	 * &gt;= XXXdata_inizio_variazione </opzionale> <opzionale
	 * dipende="XXXdata_fine_variazione"> AND to_char(ts_var,'dd/mm/yyyy') &lt;=
	 * XXXdata_fine_variazione </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a LIKE
	 * UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> </statement>
	 */
	public List<Tb_autore> selectElencoAutori(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectElencoAutori");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectElencoAutoriConLinkLogico" tipo="select"
	 * id="18_william"> <fisso> WHERE fl_canc != 'S' AND (vid_link is not null
	 * OR vid_link !=' ') </fisso> <opzionale dipende="XXXky_cles1_a_puntuale">
	 * AND ky_cles1_a LIKE UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'dd/mm/yyyy') &gt;=
	 * XXXdata_ins_Da </opzionale> <opzionale dipende="XXXdata_ins_A"> AND
	 * to_char(ts_ins,'dd/mm/yyyy') &lt;= XXXdata_ins_A </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectElencoAutoriConLinkLogico(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectElencoAutoriConLinkLogico");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectUte_forza_var" tipo="select_campi"
	 * id="19_william"> <fisso> SELECT distinct(ute_forza_var) FROM tb_autore
	 * where ute_forza_var is not null </fisso> </statement>
	 */
	public List<Tb_autore> selectUte_forza_var(HashMap opzioni)
			throws InfrastructureException {
		// TODO da implementare
		return null;
	}

	/*
	 * <statement nome="selectAutoreFiltri" tipo="select" id="23_william">
	 * <fisso> WHERE fl_canc != 'S' </fisso> <opzionale
	 * dipende="XXXautore_parentesi">and ( </opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_a">tp_nome_aut = 'A' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_b">tp_nome_aut = 'B' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_c">tp_nome_aut = 'C' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_d">tp_nome_aut = 'D' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_e">tp_nome_aut = 'E' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_r">tp_nome_aut = 'R' or</opzionale> <opzionale
	 * dipende="XXXtp_nome_aut_g">tp_nome_aut = 'G' or</opzionale> <opzionale
	 * dipende="XXXautore_parentesi"> tp_nome_aut =XXXautore_parentesi )
	 * </opzionale> <opzionale dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a
	 * LIKE UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> <opzionale
	 * dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut
	 * </opzionale> </statement>
	 */
	public List<Tb_autore> selectAutoreFiltri(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectAutoreFiltri");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);
			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectDoppi" tipo="select" id="24_marco"> <fisso> WHERE
	 * fl_canc !='S' AND vid != XXXvid AND UPPER(ky_auteur) =
	 * UPPER(XXXky_auteur) AND UPPER(ky_cautun) = UPPER(XXXky_cautun) </fisso>
	 * </statement>
	 */
	public List<Tb_autore> selectDoppi(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_AUTORE_selectDoppi");
			filter
					.setParameter(Tb_autoreCommonDao.XXXvid, opzioni
							.get(Tb_autoreCommonDao.XXXvid));
			filter.setParameter(Tb_autoreCommonDao.XXXky_auteur, opzioni
					.get(Tb_autoreCommonDao.XXXky_auteur));
			filter.setParameter(Tb_autoreCommonDao.XXXky_cautun, opzioni
					.get(Tb_autoreCommonDao.XXXky_cautun));
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectElencoAutoriConNota" tipo="select"
	 * id="25_william"> <fisso> WHERE fl_canc != 'S' AND nota_aut is not null
	 * AND nota_aut !=' ' </fisso> <opzionale dipende="XXXautore_parentesi">and (
	 * </opzionale> <opzionale dipende="XXXtp_nome_aut_a">tp_nome_aut = 'A' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_b">tp_nome_aut = 'B' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_c">tp_nome_aut = 'C' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_d">tp_nome_aut = 'D' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_e">tp_nome_aut = 'E' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_r">tp_nome_aut = 'R' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_g">tp_nome_aut = 'G' or</opzionale>
	 * <opzionale dipende="XXXautore_parentesi"> tp_nome_aut
	 * =XXXautore_parentesi ) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a LIKE
	 * UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> <opzionale
	 * dipende="XXXcd_livello">AND cd_livello = XXXcd_livello </opzionale>
	 * </statement>
	 */
	public List<Tb_autore> selectElencoAutoriConNota(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectElencoAutoriConNota");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectDirettaPerDatavar" tipo="select_campi"
	 * id="26_taymer"> <fisso> SELECT vid from Tb_autoreCommonDao WHERE fl_canc !='S' AND
	 * to_char(ts_var,'yyyy-mm-dd') &gt; XXXdata_var_da AND
	 * to_char(ts_var,'yyyy-mm-dd') &lt; XXXdata_var_a </fisso> </statement>
	 */
	public List<Tb_autore> selectDirettaPerDatavar(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_AUTORE_selectDirettaPerDatavar");
			filter.setParameter(Tb_autoreCommonDao.XXXdata_var_da, opzioni
					.get(Tb_autoreCommonDao.XXXdata_var_da));
			filter.setParameter(Tb_autoreCommonDao.XXXdata_var_a, opzioni
					.get(Tb_autoreCommonDao.XXXdata_var_a));

            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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
	 * <statement nome="selectAutoriPersonaleCollettivo" tipo="select"
	 * id="21_william"> <fisso> WHERE fl_canc !='S'
	 *
	 * </fisso> <opzionale dipende="XXXautore_parentesi">and ( </opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_a">tp_nome_aut = 'A' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_b">tp_nome_aut = 'B' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_c">tp_nome_aut = 'C' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_d">tp_nome_aut = 'D' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_e">tp_nome_aut = 'E' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_r">tp_nome_aut = 'R' or</opzionale>
	 * <opzionale dipende="XXXtp_nome_aut_g">tp_nome_aut = 'G' or</opzionale>
	 * <opzionale dipende="XXXautore_parentesi"> tp_nome_aut
	 * =XXXautore_parentesi ) </opzionale> <opzionale dipende="XXXute_var">AND
	 * UPPER(ute_var) LIKE UPPER(XXXute_var)||'%'</opzionale> <opzionale
	 * dipende="XXXdata_inizio_variazione"> AND to_char(ts_var,'dd/mm/yyyy')
	 * &gt;= XXXdata_inizio_variazione </opzionale> <opzionale
	 * dipende="XXXdata_fine_variazione"> AND to_char(ts_var,'dd/mm/yyyy') &lt;=
	 * XXXdata_fine_variazione </opzionale> <opzionale dipende="XXXaa_nascita">
	 * AND aa_nascita = XXXaa_nascita </opzionale> <opzionale
	 * dipende="XXXaa_morte"> AND aa_morte = XXXaa_morte </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_puntuale"> AND ky_cles1_a LIKE
	 * UPPER(XXXky_cles1_a_puntuale) </opzionale> <opzionale
	 * dipende="XXXky_cles1_a_inizio"> AND UPPER(ky_cles1_a) &gt;=
	 * UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt;
	 * UPPER(XXXky_cles1_a_fine) </opzionale> </statement>
	 */
	public List<Tb_autore> selectAutoriPersonaleCollettivo(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_AUTORE_selectAutoriPersonaleCollettivo");
            this.createCriteria(opzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_autore",
                    this.basicCriteria, session);

			List<Tb_autore> result = this.basicCriteria
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

	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
		// CAMPI AGGIUNTIVI SU ISTANZA POSTGRESS
		// DOVRANNO ESSERE GESTITI CHIEDERE A ROSSANA


	    log.debug("Tb_autoreResult metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
        tb_autore.setTS_VAR(now());
        session.update(opzioni);
        this.commitTransaction();
        this.closeSession();
	}

	public void insert(Object opzioni) throws InfrastructureException {
		// CAMPI AGGIUNTIVI SU ISTANZA POSTGRESS
		// DOVRANNO ESSERE GESTITI CHIEDERE A ROSSANA


		// TODO Auto-generated method stub
        log.debug("Tb_autoreResult metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_autore.setTS_INS(now);
        tb_autore.setTS_VAR(now);
        session.save(this.tb_autore);
        this.commitTransaction();
        this.closeSession();


	}

    /**
     * 	<statement nome="annullaIsadn" tipo="update" id="27_taymer">
		<fisso>
			UPDATE tb_autore
				SET ISADN = NULL
			WHERE
				vid = XXXvid
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void annullaIsadn(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_autoreResult metodo annullaIsadn invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_autore",Tb_autore.class);

		buildUpdate.addProperty("ISADN","NULL");

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="updateScambioForma" tipo="update" id="22">
	<!-- Setta tutto tranne vid, tp_forma_aut, ts_ins, ute_forza_ins e ute_ins.
	-->
			<fisso>
				UPDATE Tb_autore
				 SET
				  ute_var = XXXute_var ,
				  fl_speciale = XXXfl_speciale ,
				  ky_cles1_a = XXXky_cles1_a ,
				  ts_var = SYSTIMESTAMP ,
				  isadn = XXXisadn ,
				  ky_cautun = XXXky_cautun ,
				  ky_el5 = XXXky_el5 ,
				  ky_el4 = XXXky_el4 ,
				  ky_el3 = XXXky_el3 ,
				  ky_el2_b = XXXky_el2_b ,
				  ky_el2_a = XXXky_el2_a ,
				  ds_nome_aut = XXXds_nome_aut ,
				  ky_cles2_a = XXXky_cles2_a ,
				  aa_nascita = XXXaa_nascita ,
				  cd_agenzia = XXXcd_agenzia ,
				  vid_link = XXXvid_link ,
				  tp_nome_aut = XXXtp_nome_aut ,
				  ky_auteur = XXXky_auteur ,
				  aa_morte = XXXaa_morte ,
				  ky_el1_b = XXXky_el1_b ,
				  ky_el1_a = XXXky_el1_a ,
				  cd_paese = XXXcd_paese ,
				  cd_lingua = XXXcd_lingua ,
				  nota_cat_aut = XXXnota_cat_aut ,
				  nota_aut = XXXnota_aut ,
				  cd_norme_cat = XXXcd_norme_cat ,
				  ute_forza_var = XXXute_forza_var
				WHERE
				  vid = XXXvid
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

        log.debug("Tb_autoreResult metodo updateScambioForma invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_autore",Tb_autore.class);
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("fl_speciale",this.getParametro().get(KeyParameter.XXXfl_speciale));
		buildUpdate.addProperty("ky_cles1_a",this.getParametro().get(KeyParameter.XXXky_cles1_a));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("isadn",this.getParametro().get(KeyParameter.XXXisadn));
		buildUpdate.addProperty("ky_cautun",this.getParametro().get(KeyParameter.XXXky_cautun));
		buildUpdate.addProperty("ky_el5",this.getParametro().get(KeyParameter.XXXky_el5));
		buildUpdate.addProperty("ky_el4",this.getParametro().get(KeyParameter.XXXky_el4));
		buildUpdate.addProperty("ky_el3",this.getParametro().get(KeyParameter.XXXky_el3));
		buildUpdate.addProperty("ky_el2_b",this.getParametro().get(KeyParameter.XXXky_el2_b));
		buildUpdate.addProperty("ky_el2_a",this.getParametro().get(KeyParameter.XXXky_el2_a));
		buildUpdate.addProperty("ds_nome_aut",this.getParametro().get(KeyParameter.XXXds_nome_aut));
		buildUpdate.addProperty("ky_cles2_a",this.getParametro().get(KeyParameter.XXXky_cles2_a));
		buildUpdate.addProperty("aa_nascita",this.getParametro().get(KeyParameter.XXXaa_nascita));
		buildUpdate.addProperty("cd_agenzia",this.getParametro().get(KeyParameter.XXXcd_agenzia));
		buildUpdate.addProperty("vid_link",this.getParametro().get(KeyParameter.XXXvid_link));
		buildUpdate.addProperty("tp_nome_aut",this.getParametro().get(KeyParameter.XXXtp_nome_aut));
		buildUpdate.addProperty("ky_auteur",this.getParametro().get(KeyParameter.XXXky_auteur));
		buildUpdate.addProperty("aa_morte",this.getParametro().get(KeyParameter.XXXaa_morte));
		buildUpdate.addProperty("ky_el1_b",this.getParametro().get(KeyParameter.XXXky_el1_b));
		buildUpdate.addProperty("ky_el1_a",this.getParametro().get(KeyParameter.XXXky_el1_a));
		buildUpdate.addProperty("cd_paese",this.getParametro().get(KeyParameter.XXXcd_paese));
		buildUpdate.addProperty("cd_lingua",this.getParametro().get(KeyParameter.XXXcd_lingua));
		buildUpdate.addProperty("nota_cat_aut",this.getParametro().get(KeyParameter.XXXnota_cat_aut));
		buildUpdate.addProperty("nota_aut",this.getParametro().get(KeyParameter.XXXnota_aut));
		buildUpdate.addProperty("cd_norme_cat",this.getParametro().get(KeyParameter.XXXcd_norme_cat));
		buildUpdate.addProperty("ute_forza_var",this.getParametro().get(KeyParameter.XXXute_forza_var));

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="updateCancellaAutore" tipo="update" id="27_jenny">
		<fisso>
			UPDATE tb_autore
				SET
				  fl_canc = 'S' ,
				  ts_var = SYSTIMESTAMP ,
				  vid_link = XXXvid_link ,
				  ute_var = XXXute_var ,
				  ky_auteur = ' ' ,
				  ky_cautun = ' '
			WHERE
				vid = XXXvid
		</fisso>
		<opzionale dipende="XXXts_var"> AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var </opzionale>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaAutore(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_autoreResult metodo updateCancellaAutore invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_autore",Tb_autore.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("vid_link",this.getParametro().get(KeyParameter.XXXvid_link));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ky_auteur"," ");
		buildUpdate.addProperty("ky_cautun"," ");

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }
    public void updateAbilitaAutore(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_autoreResult metodo updateCancellaAutore invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_autore",Tb_autore.class);

		buildUpdate.addProperty("fl_canc"," ");
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("vid_link",this.getParametro().get(KeyParameter.XXXvid_link));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ky_auteur"," ");
		buildUpdate.addProperty("ky_cautun"," ");

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="updateVariazione" tipo="update" id="30_taymer">
		<fisso>
			UPDATE tb_autore
				SET
				  ts_var = SYSTIMESTAMP ,
				  ute_var = XXXute_var
			WHERE
				vid = XXXvid
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateVariazione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_autoreResult metodo updateVariazione invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_autore",Tb_autore.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    public void updateFlag(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_autoreResult metodo updateFlag invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_autore",Tb_autore.class);

		//buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		//buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
		int pippo =0;
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_autore.class;
	}

}
