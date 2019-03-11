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
package it.finsiel.sbn.polo.dao.entity.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_marcaCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;

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
public class Vl_autore_titResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao{

    public Vl_autore_titResult(Vl_autore_tit vl_autore_tit) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_autore_tit.leggiAllParametro());
    }

     /*
    <statement nome="selectAutorePerTitolo" tipo="select" id="01">
            <fisso>
                WHERE bid = XXXbid
            </fisso>
    <filter name="VL_AUTORE_TIT_selectAutorePerTitolo" condition="bid = :XXXbid "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_autore_tit> selectAutorePerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_AUTORE_TIT_selectAutorePerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit",
                    this.basicCriteria, session);

			List<Vl_autore_tit> result = this.basicCriteria.list();
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
    <statement nome="countAutorePerTitolo" tipo="count" id="01">
            <fisso>
            SELECT COUNT(*) FROM Vl_autore_tit
                WHERE bid = XXXbid
            </fisso>
     <filter name="VL_AUTORE_TIT_countAutorePerTitolo" condition="bid = :XXXbid "/>
	 */
    public Integer countAutorePerTitolo(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_TIT_countAutorePerTitolo");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit", this.basicCriteria, session);


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
    <statement nome="selectPerBid" tipo="select" id="10_william">
           <fisso>
               WHERE  fl_canc != 'S'
               and bid =XXXbid
           </fisso>
     <filter name="VL_AUTORE_TIT_selectPerBid" condition="fl_canc != 'S'
             and bid = :XXXbid "/>
     */
    public List<Vl_autore_tit> selectPerBid(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_TIT_selectPerBid");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit",
                    this.basicCriteria, session);

            List<Vl_autore_tit> result = this.basicCriteria.list();
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
     * ORIGINALE
     *  <statement nome="selectPerNomeLike" tipo="select_campi" id="12_taymer">
            <fisso>
            SELECT distinct
              cd_paese , cd_lingua , fl_canc , ute_var , fl_speciale , ky_cles1_a , ute_forza_ins ,
                to_char(ts_var,'yyyymmddhh24miss.FF') ts_var , ute_ins , isadn , ky_cautun , cd_norme_cat ,
                nota_cat_aut , ky_el5 , to_char(ts_ins,'yyyymmdd') ts_ins , ky_el4 , cd_livello , ky_el3 ,
                ky_el2_b , ky_el2_a , nota_aut , ds_nome_aut , ky_cles2_a , aa_nascita , tp_forma_aut , cd_agenzia ,
                vid_link , tp_nome_aut , ky_auteur , vid , aa_morte , ky_el1_b , ky_el1_a , ute_forza_var
                FROM Vl_autore_titCommonDao
                WHERE
                cd_relazione = XXXrelatorCode AND
                (
                (ky_cles1_a like XXXky_cles1_a || '%'
            </fisso>
      MODIFICATA
     <statement nome="selectPerNomeLike" tipo="select_campi" id="12_taymer">
            <fisso>
                WHERE
                cd_relazione = XXXrelatorCode
            </fisso>
     <filter name="VL_AUTORE_TIT_selectPerNomeLike" condition="cd_relazione = :XXXrelatorCode "/>
     */
    public List<Vl_autore_tit> selectPerNomeLike(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_TIT_selectPerNomeLike");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit",
                    this.basicCriteria, session);

            List<Vl_autore_tit> result = this.basicCriteria.list();
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
     * ORIGINALE
     *  <statement nome="selectPerNome" tipo="select_campi" id="13_taymer">
            <fisso>
            SELECT distinct
              cd_paese , cd_lingua , fl_canc , ute_var , fl_speciale , ky_cles1_a , ute_forza_ins ,
                to_char(ts_var,'yyyymmddhh24miss.FF') ts_var , ute_ins , isadn , ky_cautun , cd_norme_cat ,
                nota_cat_aut , ky_el5 , to_char(ts_ins,'yyyymmdd') ts_ins , ky_el4 , cd_livello , ky_el3 ,
                ky_el2_b , ky_el2_a , nota_aut , ds_nome_aut , ky_cles2_a , aa_nascita , tp_forma_aut , cd_agenzia ,
                vid_link , tp_nome_aut , ky_auteur , vid , aa_morte , ky_el1_b , ky_el1_a , ute_forza_var
                FROM Vl_autore_titCommonDao
                WHERE
                cd_relazione = XXXrelatorCode AND
                (
                (ky_cles1_a = XXXky_cles1_a
            </fisso>
     * MODIFICATA
    <statement nome="selectPerNome" tipo="select_campi" id="13_taymer">
            <fisso>
                cd_relazione = XXXrelatorCode AND
            </fisso>
     <filter name="VL_AUTORE_TIT_selectPerNome" condition="cd_relazione = :XXXrelatorCode "/>
     */
    public List<Vl_autore_tit> selectPerNome(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_TIT_selectPerNome");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit",
                    this.basicCriteria, session);

            List<Vl_autore_tit> result = this.basicCriteria.list();
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
     *ORIGINALE
     *  <statement nome="selectPerParoleNome" tipo="select_campi" id="14_taymer">
            <fisso>
            SELECT distinct
              cd_paese , cd_lingua , fl_canc , ute_var , fl_speciale , ky_cles1_a , ute_forza_ins ,
                to_char(ts_var,'yyyymmddhh24miss.FF') ts_var , ute_ins , isadn , ky_cautun , cd_norme_cat ,
                nota_cat_aut , ky_el5 , to_char(ts_ins,'yyyymmdd') ts_ins , ky_el4 , cd_livello , ky_el3 ,
                ky_el2_b , ky_el2_a , nota_aut , ds_nome_aut , ky_cles2_a , aa_nascita , tp_forma_aut , cd_agenzia ,
                vid_link , tp_nome_aut , ky_auteur , vid , aa_morte , ky_el1_b , ky_el1_a , ute_forza_var
                FROM Vl_autore_titCommonDao
                WHERE
                cd_relazione = XXXrelatorCode AND
                    CONTAINS(ds_nome_aut, XXXparola1 ) > 0
            </fisso>
     MODIFICATA
        <statement nome="selectPerParoleNome" tipo="select_campi" id="14_taymer">
            <fisso>
                WHERE
                cd_relazione = XXXrelatorCode AND
                    CONTAINS(ds_nome_aut, XXXparola1 ) > 0
            </fisso>
     <filter name="VL_AUTORE_TIT_selectPerParoleNome"
        condition="cd_relazione = :XXXrelatorCode AND
                   CONTAINS(ds_nome_aut, :XXXparola1 ) &gt; 0 "/>

     */
    public List<Vl_autore_tit> selectPerParoleNome(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("VL_AUTORE_TIT_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("VL_AUTORE_TIT_selectPerParoleNome");
	            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXparola1,
	                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXparola1));
				myOpzioni.remove(Tb_marcaCommonDao.XXXparola1);
			}


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit",
                    this.basicCriteria, session);

            List<Vl_autore_tit> result = this.basicCriteria.list();
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
     * ORIGINALE
    <statement nome="countPerNomeLike" tipo="count" id="15_taymer">
            <fisso>
                SELECT COUNT(distinct(vid)) FROM vl_autore_tit
                WHERE
                cd_relazione = XXXrelatorCode AND
                (
                (ky_cles1_a like XXXky_cles1_a || '%'
            </fisso>
     MODIFICATA
        <statement nome="countPerNomeLike" tipo="count" id="15_taymer">
            <fisso>
                SELECT COUNT(distinct(vid)) FROM vl_autore_tit
                WHERE
                cd_relazione = XXXrelatorCode
            </fisso>
     <filter name="VL_AUTORE_TIT_countPerNomeLike" condition="cd_relazione = :XXXrelatorCode "/>
     */
    public Integer countPerNomeLike(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_TIT_countPerNomeLike");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit", this.basicCriteria, session);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(
                            Projections.countDistinct("VID"))).uniqueResult();


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
     * ORIGINALE
    <statement nome="countPerNome" tipo="count" id="16_Taymer">
            <fisso>
                SELECT COUNT(distinct(vid)) FROM vl_autore_tit
                WHERE
                cd_relazione = XXXrelatorCode AND
                (
                (ky_cles1_a = XXXky_cles1_a
            </fisso>
      MODIFICATA
    <statement nome="countPerNome" tipo="count" id="16_Taymer">
            <fisso>
                SELECT COUNT(distinct(vid)) FROM vl_autore_tit
                WHERE
                cd_relazione = XXXrelatorCode
            </fisso>
     <filter name="VL_AUTORE_TIT_countPerNome" condition="cd_relazione = :XXXrelatorCode "/>
     */
    public Integer countPerNome(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_TIT_countPerNome");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit", this.basicCriteria, session);


            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(
                            Projections.countDistinct("VID"))).uniqueResult();

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
     * ORIGINALE
    <statement nome="countPerParoleNome" tipo="count" id="17_Taymer">
            <fisso>
                SELECT COUNT(distinct(vid)) FROM vl_autore_tit
                WHERE
                cd_relazione = XXXrelatorCode AND
                    CONTAINS(ds_nome_aut, XXXparola1 ) > 0
            </fisso>
     <filter name="VL_AUTORE_TIT_countPerParoleNome"
        condition="cd_relazione = :XXXrelatorCode AND
                   CONTAINS(ds_nome_aut, :XXXparola1 ) &gt; 0 "/>

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
				filter = session.enableFilter("VL_AUTORE_TIT_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("VL_AUTORE_TIT_countPerParoleNome");
	            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXparola1,
	                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXparola1));
	            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXparola1);
			}

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_titCommonDao.XXXrelatorCode);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_tit", this.basicCriteria, session);


            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(
                            Projections.countDistinct("VID"))).uniqueResult();

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
		return Vl_autore_tit.class;
	}

}
