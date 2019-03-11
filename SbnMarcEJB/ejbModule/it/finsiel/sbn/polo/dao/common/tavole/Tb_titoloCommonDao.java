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
package it.finsiel.sbn.polo.dao.common.tavole;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.viste.Vl_biblioteca_titCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.factoring.transactionmaker.Factoring;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.CdBibType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class Tb_titoloCommonDao extends TableDao {


        /*
         public static final String XXXcd_paese = "XXXcd_paese";
        public static final String XXXcd_paese_std = "XXXcd_paese_std";
        public static final String XXXlivello_aut_da = "XXXlivello_aut_da";
        public static final String XXXlivello_aut_a = "XXXlivello_aut_a";
        public static final String XXXdata_var_Da = "XXXdata_var_Da";
        public static final String XXXdata_var_A = "XXXdata_var_A";
        public static final String XXXdataInizio_Da = "XXXdataInizio_Da";
        public static final String XXXdataInizio_A = "XXXdataInizio_A";
        public static final String XXXdata_ins_Da = "XXXdata_ins_Da";
        public static final String XXXdata_ins_A = "XXXdata_ins_A";
        public static final String XXXdataInizioDa = "XXXdataInizioDa";
        public static final String XXXdataInizioA = "XXXdataInizioA";
        public static final String XXXdataFine_Da = "XXXdataFine_Da";
        public static final String XXXdataFine_A = "XXXdataFine_A";
        public static final String XXXdataFineDa = "XXXdataFineDa";
        public static final String XXXdataFineA = "XXXdataFineA";
        public static final String XXXaa_morte = "XXXaa_morte";
        public static final String XXXaa_nascita = "XXXaa_nascita";
        public static final String XXXute_var = "XXXute_var";
        public static final String XXXute_ins = "XXXute_ins";
        public static final String XXXlivello = "XXXlivello";
        public static final String XXXlivello_auf = "XXXlivello_auf";
        public static final String XXXlivello_lav = "XXXlivello_lav";
        public static final String XXXlivello_sup = "XXXlivello_sup";
        public static final String XXXlivello_max = "XXXlivello_max";
        public static final String XXXlivello_med = "XXXlivello_med";
        public static final String XXXlivello_min = "XXXlivello_min";
        public static final String XXXlivello_rec = "XXXlivello_rec";
        public static final String XXXnon_musicale = "XXXnon_musicale";
        public static final String XXXky_editore = "XXXky_editore";


    */

       /*
            <opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
            <opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>
            <opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
            <opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
            <opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
            <opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
            <opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
            <opzionale dipende="XXXtp_materiale1"> ) </opzionale>
            <opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
            <opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
            <opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
            <opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
            <opzionale dipende="XXXcd_natura1"> ) </opzionale>
            <opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
            <opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
            <opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
            <opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
            <opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
            <opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
            <opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
            <opzionale dipende="XXXesporta_ts_var_da"> AND ts_var &gt;= XXXesporta_ts_var_da </opzionale>
            <opzionale dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins = ts_var AND ts_var &gt;= XXXesporta_ts_var_e_ts_ins_da </opzionale>
            <opzionale dipende="XXXesporta_ts_var_a"> AND ts_var  &lt;= XXXesporta_ts_var_a </opzionale>
            <opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
            <opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
            <opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
            <opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
            <!-- Date -->
            <!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
            <opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
            <!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
            <!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
            <opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
            <opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
            <opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
            <opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
            <opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
            <opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
            <!-- fine date-->
            DEFAULT <opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
            <opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
            <opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
            <opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
            <opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
            <opzionale dipende="XXXcd_genere_1"> ) </opzionale>
            <opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
            <opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
            <opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
            <opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
            <opzionale dipende="XXXstringaClet"> AND UPPER(ky_clet1_t) || UPPER(ky_clet2_t)  LIKE UPPER( XXXstringaClet)||'%' </opzionale>

            <opzionale dipende="XXXbid">AND bid != XXXbid</opzionale>
            <opzionale dipende="XXXnaturaSoC">AND cd_natura in ('S','C')</opzionale>
            <opzionale dipende="XXXnaturaMoW">AND cd_natura in ('M','W')</opzionale>
            <opzionale dipende="XXXnaturaBoA">AND cd_natura in ('B','A')</opzionale>
            <opzionale dipende="XXXcd_natura">AND cd_natura = XXXcd_natura</opzionale>
            <opzionale dipende="XXXaa_pubb_1_s">AND aa_pubb_1 = XXXaa_pubb_1_s</opzionale>
            <opzionale dipende="XXXute_var">AND  ute_var LIKE UPPER(XXXute_var)||'%'</opzionale>

// Function setCles
            <fisso>
                AND (
                ky_cles1_t = XXXcles1_0
                </fisso>
                <opzionale dipende="XXXcles2_0">AND ky_cles2_t = XXXcles2_0</opzionale>
                <opzionale dipende="XXXcles1_1">OR ky_cles1_t = XXXcles1_1</opzionale>
                <opzionale dipende="XXXcles2_1">AND ky_cles2_t = XXXcles2_1</opzionale>
                <opzionale dipende="XXXcles1_2">OR ky_cles1_t = XXXcles1_2</opzionale>
                <opzionale dipende="XXXcles2_2">AND ky_cles2_t = XXXcles2_2</opzionale>
                <opzionale dipende="XXXcles1_3">OR ky_cles1_t = XXXcles1_3</opzionale>
                <opzionale dipende="XXXcles2_3">AND ky_cles2_t = XXXcles2_3</opzionale>
                <opzionale dipende="XXXcles1_4">OR ky_cles1_t = XXXcles1_4</opzionale>
                <opzionale dipende="XXXcles2_4">AND ky_cles2_t = XXXcles2_4</opzionale>
                <successivo>
                <fisso>
                    )
            </fisso>

// Function setStringaLike
                AND
                    (
                    ( ky_cles1_t  LIKE XXXstringaLike1||'%'
                    AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
                    </fisso>
                    <opzionale dipende="XXXstringaLike2">
                         AND ky_cles2_t like XXXstringaLike2 || '%'
                    </opzionale>
                    <opzionale dipende="XXXstringaLike1">
                           ) OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%'
                    </opzionale>
                    <opzionale dipende="XXXstringaLike2">
                           AND ky_cles2_ct like XXXstringaLike2 || '%'
                    </opzionale>
                    <opzionale dipende="XXXstringaLike1">
                            )
                         )

// function setStringaEsatta
               AND(( ky_cles1_t = XXXstringaEsatta1 AND ky_cles2_t = XXXstringaEsatta2
               AND ky_clet2_t LIKE XXXclet2_ricerca||'%')
               OR ( ky_cles1_ct  = XXXstringaEsatta1 AND ky_cles2_ct = XXXstringaEsatta2 ))

     */



    protected boolean kycleslike = false;

    public Tb_titoloCommonDao() {
        super();
    }

    public Tb_titoloCommonDao(Criteria titoloCriteria) {
        super();
        this.basicCriteria = titoloCriteria;
    }

    /*
    <opzionale dipende="XXXute_var">AND  ute_var LIKE UPPER(XXXute_var)||'%'</opzionale>
     */
    public void setUteVar(HashMap opzioni) {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("UTE_VAR");
        try {
			param.setValueString(opzioni,Tb_titoloCommonDao.XXXute_var);
	        if ((value = this.setParameterLikeEnd(param)) != null) {
	            this.basicCriteria.add(value);
                opzioni.remove(XXXute_var);
	        }
		} catch (Exception e) {
		}
    }


    /*
     *
     *
     * <fisso>
        AND (
        ky_cles1_t = XXXcles1_0
        </fisso>
        <opzionale dipende="XXXcles2_0">AND ky_cles2_t = XXXcles2_0</opzionale>
        <opzionale dipende="XXXcles1_1">OR ky_cles1_t = XXXcles1_1</opzionale>
        <opzionale dipende="XXXcles2_1">AND ky_cles2_t = XXXcles2_1</opzionale>
        <opzionale dipende="XXXcles1_2">OR ky_cles1_t = XXXcles1_2</opzionale>
        <opzionale dipende="XXXcles2_2">AND ky_cles2_t = XXXcles2_2</opzionale>
        <opzionale dipende="XXXcles1_3">OR ky_cles1_t = XXXcles1_3</opzionale>
        <opzionale dipende="XXXcles2_3">AND ky_cles2_t = XXXcles2_3</opzionale>
        <opzionale dipende="XXXcles1_4">OR ky_cles1_t = XXXcles1_4</opzionale>
        <opzionale dipende="XXXcles2_4">AND ky_cles2_t = XXXcles2_4</opzionale>
        <successivo>
            <fisso>
            )
            </fisso>
     */
    private void setCles(HashMap opzioni) throws Exception {
        Criterion value;
        // <fisso>AND (ky_cles1_t = XXXcles1_0 )


        // Inizio modifica almaviva2 06.12.2010 BUG MANTIS 3182
        // La costruzione della select per ricerca simili è errata e viene asteriscata e sostituita dalla
        // nuova versione che inserisce correttamente parentesi AND e OR (Tb_titoloCommonDao - setCles);
        if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_0)){
            Parameter param0;
            param0 = new Parameter();
            param0.setKey("KY_CLES1_T");
            param0.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_0);


            Disjunction disjunction =  Restrictions.disjunction();;
            Conjunction conjunction =  Restrictions.conjunction();;
            conjunction.add(this.setParameterEq(param0));

            //<opzionale dipende="XXXcles2_0">AND ky_cles2_t = XXXcles2_0</opzionale>
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_0)){
                Parameter param;
                param = new Parameter();
                param.setKey("KY_CLES2_T");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_0);
                conjunction.add(this.setParameterEq(param));
            }
            disjunction.add(conjunction);

            //<opzionale dipende="XXXcles1_1">OR ky_cles1_t = XXXcles1_1</opzionale>
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_1)){
                Conjunction conjunction1 =  Restrictions.conjunction();;
                Parameter param;
                param = new Parameter();
                param.setKey("KY_CLES1_T");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_1);
                conjunction1.add(this.setParameterEq(param));

                //<opzionale dipende="XXXcles2_1">AND ky_cles2_t = XXXcles2_1</opzionale>
                if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_1)){
                    param = new Parameter();
                    param.setKey("KY_CLES2_T");
                    param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_1);
                    conjunction1.add(this.setParameterEq(param));
                }
                disjunction.add(conjunction1);
            }

            //<opzionale dipende="XXXcles1_2">OR ky_cles1_t = XXXcles1_2</opzionale>
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_2)){
                Conjunction conjunction2 =  Restrictions.conjunction();;
                Parameter param;
                param = new Parameter();
                param.setKey("KY_CLES1_T");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_2);
                conjunction2.add(this.setParameterEq(param));
                //<opzionale dipende="XXXcles2_2">AND ky_cles2_t = XXXcles2_2</opzionale>
                if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_2)){
                    param = new Parameter();
                    param.setKey("KY_CLES2_T");
                    param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_2);
                    conjunction2.add(this.setParameterEq(param));
                }
                disjunction.add(conjunction2);
            }

            //<opzionale dipende="XXXcles1_3">OR ky_cles1_t = XXXcles1_3</opzionale>
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_3)){
            	Conjunction conjunction3 =  Restrictions.conjunction();;
                Parameter param;
                param = new Parameter();
                param.setKey("KY_CLES1_T");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_3);
                conjunction3.add(this.setParameterEq(param));
                //<opzionale dipende="XXXcles2_3">AND ky_cles2_t = XXXcles2_3</opzionale>
                if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_3)){
                    param = new Parameter();
                    param.setKey("KY_CLES2_T");
                    param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_3);
                    conjunction3.add(this.setParameterEq(param));
                }
                disjunction.add(conjunction3);
            }

            //<opzionale dipende="XXXcles1_4">OR ky_cles1_t = XXXcles1_4</opzionale>
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_4)){
            	Conjunction conjunction4 =  Restrictions.conjunction();;
                Parameter param;
                param = new Parameter();
                param.setKey("KY_CLES1_T");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_4);
                conjunction4.add(this.setParameterEq(param));
                //<opzionale dipende="XXXcles2_4">AND ky_cles2_t = XXXcles2_4</opzionale>
                if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_4)){
                    param = new Parameter();
                    param.setKey("KY_CLES2_T");
                    param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_4);
                    conjunction4.add(this.setParameterEq(param));
                }
                disjunction.add(conjunction4);
            }

            this.basicCriteria.add(disjunction);
            opzioni.remove(XXXcles1_0);
            opzioni.remove(XXXcles2_0);
            opzioni.remove(XXXcles1_1);
            opzioni.remove(XXXcles2_1);
            opzioni.remove(XXXcles1_2);
            opzioni.remove(XXXcles2_2);
            opzioni.remove(XXXcles1_3);
            opzioni.remove(XXXcles2_3);
            opzioni.remove(XXXcles1_4);
            opzioni.remove(XXXcles2_4);

        }
//
//        if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_0)){
//            Parameter param0;
//            param0 = new Parameter();
//            param0.setKey("KY_CLES1_T");
//            param0.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_0);
//
//
//            Disjunction disjunction =  Restrictions.disjunction();;
//            Conjunction conjunction =  Restrictions.conjunction();;
//            conjunction.add(this.setParameterEq(param0));
//
//            //<opzionale dipende="XXXcles2_0">AND ky_cles2_t = XXXcles2_0</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_0)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES2_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_0);
//                conjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles2_1">AND ky_cles2_t = XXXcles2_1</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_1)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES2_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_1);
//                conjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles2_2">AND ky_cles2_t = XXXcles2_2</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_2)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES2_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_2);
//                conjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles2_3">AND ky_cles2_t = XXXcles2_3</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_3)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES2_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_3);
//                conjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles2_4">AND ky_cles2_t = XXXcles2_4</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles2_4)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES2_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles2_4);
//                conjunction.add(this.setParameterEq(param));
//            }
//
//            //<opzionale dipende="XXXcles1_1">OR ky_cles1_t = XXXcles1_1</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_1)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES1_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_1);
//                disjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles1_2">OR ky_cles1_t = XXXcles1_2</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_2)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES1_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_2);
//                disjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles1_3">OR ky_cles1_t = XXXcles1_3</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_3)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES1_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_3);
//                disjunction.add(this.setParameterEq(param));
//            }
//            //<opzionale dipende="XXXcles1_4">OR ky_cles1_t = XXXcles1_4</opzionale>
//            if(opzioni.containsKey(Tb_titoloCommonDao.XXXcles1_4)){
//                Parameter param;
//                param = new Parameter();
//                param.setKey("KY_CLES1_T");
//                param.setValueString(opzioni,Tb_titoloCommonDao.XXXcles1_4);
//                disjunction.add(this.setParameterEq(param));
//            }
//
//            disjunction.add(conjunction);
//
//            this.basicCriteria.add(disjunction);
//            opzioni.remove(XXXcles1_0);
//            opzioni.remove(XXXcles2_0);
//            opzioni.remove(XXXcles1_1);
//            opzioni.remove(XXXcles2_1);
//            opzioni.remove(XXXcles1_2);
//            opzioni.remove(XXXcles2_2);
//            opzioni.remove(XXXcles1_3);
//            opzioni.remove(XXXcles2_3);
//            opzioni.remove(XXXcles1_4);
//            opzioni.remove(XXXcles2_4);
//
//        }

        // Fine modifica almaviva2 06.12.2010 BUG MANTIS 3182

    }
    /*
           AND
            (
            ( ky_cles1_t  LIKE XXXstringaLike1||'%'
            AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
            </fisso>
            <opzionale dipende="XXXstringaLike2">
                 AND ky_cles2_t like XXXstringaLike2 || '%'
            </opzionale>
            <opzionale dipende="XXXstringaLike1">
                   ) OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%'
            </opzionale>
            <opzionale dipende="XXXstringaLike2">
                   AND ky_cles2_ct like XXXstringaLike2 || '%'
            </opzionale>
            <opzionale dipende="XXXstringaLike1">
                    )
                 )

    TRADOTTO

     AND(( ky_cles1_t  LIKE XXXstringaLike1||'%'AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
         ) OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%'))
    AND(
    ( ky_cles1_t  LIKE XXXstringaLike1||'%'AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
     AND ky_cles2_t like XXXstringaLike2 || '%' )
     OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%' AND ky_cles2_ct like XXXstringaLike2 || '%')

     ANCHE IN FORMA

     AND (( ky_cles1_t  LIKE XXXstringaLike1||'%'AND ky_cles2_t like XXXstringaLike2 || '%'
          ) OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%' AND ky_cles2_ct like XXXstringaLike2 || '%' ))


     */
    private void setStringaLike(HashMap opzioni) throws Exception {
        Criterion value;
        if(kycleslike)
        	return;

        if(opzioni.containsKey(Tb_titoloCommonDao.XXXstringaLike1)){
            Disjunction disjunction =  Restrictions.disjunction();
            Conjunction conjunction =  Restrictions.conjunction();
            Parameter param;
            param = new Parameter();
            param.setKey("KY_CLES1_T");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaLike1);

            conjunction.add(this.setParameterLikeEnd(param));
            param = new Parameter();
            param.setKey("KY_CLET2_T");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXclet2_ricerca);
            conjunction.add(this.setParameterLikeEnd(param));
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXstringaLike2)){
                param = new Parameter();
                param.setKey("KY_CLES2_T");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaLike2);
                conjunction.add(this.setParameterLikeEnd(param));
            }
            disjunction.add(conjunction);
            conjunction =  Restrictions.conjunction();
            param = new Parameter();
            param.setKey("KY_CLES1_CT");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaLike1);
            conjunction.add(this.setParameterLikeEnd(param));
            if(opzioni.containsKey(Tb_titoloCommonDao.XXXstringaLike2)){
                param = new Parameter();
                param.setKey("KY_CLES2_CT");
                param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaLike2);
                conjunction.add(this.setParameterLikeEnd(param));
            }
            disjunction.add(conjunction);
            this.basicCriteria.add(disjunction);
            opzioni.remove(XXXstringaLike1);
            opzioni.remove(XXXstringaLike2);
            opzioni.remove(XXXclet2_ricerca);

        }
    }
    /*
           AND
                (
                    ( ky_cles1_t = XXXstringaEsatta1
                     AND ky_cles2_t = XXXstringaEsatta2
                    AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
                   )
                   OR
                   ( ky_cles1_ct  = XXXstringaEsatta1
                   AND ky_cles2_ct = XXXstringaEsatta2
                    )
                 )
       TRADOTTO
       AND(( ky_cles1_t = XXXstringaEsatta1 AND ky_cles2_t = XXXstringaEsatta2
       AND ky_clet2_t LIKE XXXclet2_ricerca||'%')
       OR ( ky_cles1_ct  = XXXstringaEsatta1 AND ky_cles2_ct = XXXstringaEsatta2 ))

       SI TROVA ANCHE SENZA XXXclet2_ricerca

       AND (( ky_cles1_t = XXXstringaEsatta1 AND ky_cles2_t = XXXstringaEsatta2)
       OR ( ky_cles1_ct  = XXXstringaEsatta1 AND ky_cles2_ct = XXXstringaEsatta2 ))




     */


    /*
               and ((this_.KY_CLES1_T='MAGIST' and this_.KY_CLES2_T='RATO'
               and this_.KY_CLET2_T like '%')
               or (this_.KY_CLES1_CT like 'MAGIST%' and this_.KY_CLES2_CT like 'RATO%'))

               AND ( ( ky_cles1_t = 'AEMILI' AND ky_cles2_t = 'A'
               AND ky_clet2_t LIKE ''||'%' ) OR
               ( ky_cles1_ct = 'AEMILI' AND ky_cles2_ct = 'A' ) )

     */
    private void setStringaEsatta(HashMap opzioni) throws Exception {
        if(kycleslike)
        	return;

        if(opzioni.containsKey(Tb_titoloCommonDao.XXXstringaEsatta1)){

            Disjunction disjunction =  Restrictions.disjunction();

            Conjunction conjunction =  Restrictions.conjunction();
    		Criterion value;
            Parameter param;
            param = new Parameter();
            param.setKey("KY_CLES1_T");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaEsatta1);

            conjunction.add(this.setParameterEq(param));
            param = new Parameter();
            param.setKey("KY_CLES2_T");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaEsatta2);
            conjunction.add(this.setParameterEq(param));
            param = new Parameter();
            param.setKey("KY_CLET2_T");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXclet2_ricerca);
            conjunction.add(this.setParameterLikeEnd(param));


            disjunction.add(conjunction);


            conjunction =  Restrictions.conjunction();
            param = new Parameter();
            param.setKey("KY_CLES1_CT");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaEsatta1);
            conjunction.add(this.setParameterEq(param));
            param = new Parameter();
            param.setKey("KY_CLES2_CT");
            param.setValueString(opzioni,Tb_titoloCommonDao.XXXstringaEsatta2);
            conjunction.add(this.setParameterEq(param));

            disjunction.add(conjunction);
            this.basicCriteria.add(disjunction);
            opzioni.remove(XXXstringaEsatta1);
            opzioni.remove(XXXstringaEsatta2);
            opzioni.remove(XXXclet2_ricerca);


        }
    }
    /*
     <opzionale dipende="XXXbid">AND bid != XXXbid</opzionale>
     */
    private void setBID(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("BID");
        param.setValueString(opzioni,Tb_titoloCommonDao.XXXbid);

        if((value = this.setParameterNotEq(param))!= null)
        {
            this.basicCriteria.add(value);
            opzioni.remove(XXXbid);
        }
    }
    /*
    <opzionale dipende="XXXnaturaSoC">AND cd_natura in ('S','C')</opzionale>
    */
    private void setNaturaSoC(HashMap opzioni) {
        Criterion value;
        List<String> naturaSoC = new ArrayList<String>();
        naturaSoC.add("S");
        naturaSoC.add("C");
        if(opzioni.containsKey("XXXnaturaSoC")){
            Parameter param;
            param = new Parameter();
            param.setKey("CD_NATURA");
            param.setValueCollection(naturaSoC);
            if((value = this.setParameterIn(param))!= null){
                this.basicCriteria.add(value);
                opzioni.remove(XXXnaturaSoC);
            }
        }
    }
    /*
    <opzionale dipende="XXXnaturaMoW">AND cd_natura in ('M','W')</opzionale>
    */
    private void setNaturaMoW(HashMap opzioni) {
        Criterion value;
        List<String> naturaMoW = new ArrayList<String>();
        naturaMoW.add("M");
        naturaMoW.add("W");
        if(opzioni.containsKey("XXXnaturaMoW")){
            Parameter param;
            param = new Parameter();
            param.setKey("CD_NATURA");
            param.setValueCollection(naturaMoW);
            if((value = this.setParameterIn(param))!= null){
                this.basicCriteria.add(value);
                opzioni.remove(XXXnaturaMoW);
       }
        }
    }
    /*
    <opzionale dipende="XXXnaturaBoA">AND cd_natura in ('B','A')</opzionale>
    */
    private void setNaturaBoA(HashMap opzioni) {
        Criterion value;
        List<String> naturaBoA = new ArrayList<String>();
        naturaBoA.add("B");
        naturaBoA.add("A");
        if(opzioni.containsKey("XXXnaturaBoA")){
            Parameter param;
            param = new Parameter();
            param.setKey("CD_NATURA");
            param.setValueCollection(naturaBoA);
            if((value = this.setParameterIn(param))!= null){
                this.basicCriteria.add(value);
                opzioni.remove(XXXnaturaBoA);
            }
        }
    }
    /*
    <opzionale dipende="XXXcd_natura">AND cd_natura = XXXcd_natura</opzionale>
    */
    private void setNatura(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("CD_NATURA");
        param.setValueString(opzioni,Tb_titoloCommonDao.XXXcd_natura);
        if((value = this.setParameterEq(param))!= null)
        {
            this.basicCriteria.add(value);
            opzioni.remove(XXXcd_natura);

        }
    }
    /*
    <opzionale dipende="XXXaa_pubb_1_s">AND aa_pubb_1 = XXXaa_pubb_1_s</opzionale>
    */
    private void setAAPub1s(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("AA_PUBB_1");
        param.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_1_s);
        if((value = this.setParameterEq(param))!= null)
        {
            this.basicCriteria.add(value);
            opzioni.remove(XXXaa_pubb_1_s);
        }
    }
    /*
    <opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
    <opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
    <opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
    <opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
    <opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
    <opzionale dipende="XXXtp_materiale1"> ) </opzionale>
     */
    public void setTipoMateriale(HashMap opzioni) {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("TP_MATERIALE");
        param.setValueCollection(opzioni,Tb_titoloCommonDao.XXXtp_materiale,5);

        if((value = this.setParameterIn(param))!= null)
        {
            this.basicCriteria.add(value);
        }
        opzioni.remove(XXXtp_materiale);
        opzioni.remove(XXXtp_materiale1);
        opzioni.remove(XXXtp_materiale2);
        opzioni.remove(XXXtp_materiale3);
        opzioni.remove(XXXtp_materiale4);
        opzioni.remove(XXXtp_materiale5);
    }
    /*
    <opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
    <opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
    <opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
    <opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
    <opzionale dipende="XXXcd_natura1"> ) </opzionale>
     */
    public void setCdNatura(HashMap opzioni) {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("CD_NATURA");
        param.setValueCollection(opzioni,Tb_titoloCommonDao.XXXcd_natura,4);
        if((value = this.setParameterIn(param))!= null)
        {
            this.basicCriteria.add(value);
        }
        opzioni.remove(XXXcd_natura);
        opzioni.remove(XXXcd_natura1);
        opzioni.remove(XXXcd_natura2);
        opzioni.remove(XXXcd_natura3);
        opzioni.remove(XXXcd_natura4);
    }
    /*
    <opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
    <opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
    <opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
    <opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
    <opzionale dipende="XXXtp_record_uni1"> ) </opzionale>

     */
    public void setTipoRecordUni(HashMap opzioni) {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("TP_RECORD_UNI");
        param.setValueCollection(opzioni,Tb_titoloCommonDao.XXXtp_record_uni,4);
        if((value = this.setParameterIn(param))!= null)
        {
            this.basicCriteria.add(value);
        }
        opzioni.remove(XXXtp_record_uni);
        opzioni.remove(XXXtp_record_uni1);
        opzioni.remove(XXXtp_record_uni2);
        opzioni.remove(XXXtp_record_uni3);
        opzioni.remove(XXXtp_record_uni4);
    }
    /*
    <opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
    <opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
     */
    public void setTsVar(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param0;
        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_titoloCommonDao.XXXts_var_da,"yyyy-mm-dd");

        Parameter param1;
        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_titoloCommonDao.XXXts_var_a,"yyyy-mm-dd");
        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(XXXts_var_da);
            opzioni.remove(XXXts_var_a);

        }
    }
    /*
    <opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
    <opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>

     */
    public void setCdLivello(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        param0 = new Parameter();
        param0.setKey("CD_LIVELLO");
        param0.setValueString(opzioni,Tb_titoloCommonDao.XXXcd_livello_da);

        Parameter param1;
        param1 = new Parameter();
        param1.setKey("CD_LIVELLO");
        param1.setValueString(opzioni,Tb_titoloCommonDao.XXXcd_livello_a);
        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(XXXcd_livello_da);
            opzioni.remove(XXXcd_livello_a);
        }
    }
    /*
    <opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
    <opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>

    <opzionale dipende="XXXts_ins_da"> AND ts_ins &gt;= to_timestamp(XXXts_ins_da,'yyyy-mm-dd') AND ts_var &gt;= to_timestamp(XXXts_ins_da,'yyyy-mm-dd')</opzionale>
    <opzionale dipende="XXXts_ins_a"> AND ts_ins  &lt;= to_timestamp(XXXts_ins_a,'yyyy-mm-dd') </opzionale>

     */
    public void setTsIns(HashMap opzioni) throws ParseException {
        Criterion value;
        if(opzioni.containsKey(Tb_titoloCommonDao.XXXts_ins_da))
        {
            Parameter param0;
            param0 = new Parameter();
            param0.setKey("TS_INS");
            param0.setValueDate(opzioni,Tb_titoloCommonDao.XXXts_ins_da,"yyyy-mm-dd");
            Parameter param1;
            param1 = new Parameter();
            param1.setKey("TS_VAR");
            param1.setValueDate(opzioni,Tb_titoloCommonDao.XXXts_ins_da,"yyyy-mm-dd");
            if((value=setParameterDaA(param0,param1))!= null){
                this.basicCriteria.add(value);
                opzioni.remove(XXXts_ins_da);
            }
        }

        if(opzioni.containsKey(Tb_titoloCommonDao.XXXts_ins_a))
        {
            Parameter param1;
            param1 = new Parameter();
            param1.setKey("TS_INS");
            param1.setValueDate(opzioni,Tb_titoloCommonDao.XXXts_ins_a,"yyyy-mm-dd");
            if((value=setParameterDaA(new Parameter(),param1))!= null){
                this.basicCriteria.add(value);
                opzioni.remove(XXXts_ins_a);
            }
        }
    }
    /*
    <opzionale dipende="XXXesporta_ts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXesporta_ts_var_da </opzionale>
    <opzionale dipende="XXXesporta_ts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXesporta_ts_var_a </opzionale>
     */
    public void setEsportaTsVar(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param0;
        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_titoloCommonDao.XXXesporta_ts_var_da,"yyyy-mm-dd");

        Parameter param1;
        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_titoloCommonDao.XXXesporta_ts_var_a,"yyyy-mm-dd");
        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(XXXesporta_ts_var_da);
            opzioni.remove(XXXesporta_ts_var_a);
        }
    }
    /*
    <opzionale dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins != ts_var AND ts_var &gt;= XXXesporta_ts_var_e_ts_ins_da </opzionale>
     */
    public void setEsportaTsVarETsInsDa(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("TS_VAR");
        param.setValueDate(opzioni,Tb_titoloCommonDao.XXXesporta_ts_var_e_ts_ins_da,"yyyy-mm-dd");

        if((value=setParameterGe(param))!= null){
            this.basicCriteria.add(value);
            this.basicCriteria.add(Restrictions.neProperty("TS_INS","TS_VAR"));
            opzioni.remove(XXXesporta_ts_var_e_ts_ins_da);
        }
    }

    /*
    <opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
     */
    public void setAAPub(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("TP_AA_PUBB");
        param.setValueString(opzioni,Tb_titoloCommonDao.XXXtp_aa_pubb);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXtp_aa_pubb);
        }
    }
    /*
    <opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
    <opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
     */
    public void setAAPub1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        param0 = new Parameter();
        param0.setKey("AA_PUBB_1");
        param0.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_1_da);

        Parameter param1;
        param1 = new Parameter();
        param1.setKey("AA_PUBB_1");
        param1.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_1_a);

        if((value=setParameterDaA(param0,param1))!= null){
            // this.basicCriteria.add(value);
            opzioni.remove(XXXaa_pubb_1_da);
            opzioni.remove(XXXaa_pubb_1_a);

            /**
             * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
             * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
             * o uguale al carattere punto '.';
             * Modifica della select di ricerca in Polo cosi' da cercare non solo i documenti in cui la data sia uguale
             * a quella indicata (esempio 1999) ma nahce quelle simili (199. e 19..)
             * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
             */

            String data1Jolly = ((String) param0.getValue()).substring(0,3) + ".";
            String data2Jolly = ((String) param0.getValue()).substring(0,2) + "..";
            Disjunction disjunction1 = Restrictions.disjunction();
            disjunction1.add(value);
			disjunction1.add(Restrictions.eq("AA_PUBB_1",data1Jolly));
			disjunction1.add(Restrictions.eq("AA_PUBB_1",data2Jolly));
			this.basicCriteria.add(disjunction1);
        }
    }
    /*
    <opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
    */
    public void setAAPub1Like(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("AA_PUBB_1");
        param.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_1_like);
        if((value=setParameterLikeEnd(param))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(XXXaa_pubb_1_like);
        }
    }
    /*
    <opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
    <opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
     */
    public void setAAPub2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        param0 = new Parameter();
        param0.setKey("AA_PUBB_2");
        param0.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_2_da);

        Parameter param1;
        param1 = new Parameter();
        param1.setKey("AA_PUBB_2");
        param1.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_2_a);
        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(XXXaa_pubb_2_da);
            opzioni.remove(XXXaa_pubb_2_a);
        }
    }
    /*
    <opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
    <opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
    */
    public void setAAPub2Like(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("AA_PUBB_2");
        param.setValueString(opzioni,Tb_titoloCommonDao.XXXaa_pubb_2_like);
        if((value=setParameterLikeEnd(param))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(XXXaa_pubb_2_like);
        }
    }
    /*
    <opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
    <opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
    <opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
    <opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
    <opzionale dipende="XXXcd_genere_1"> ) </opzionale>
     */
    private void setCdGenere(HashMap opzioni) {
        List<String> gen = new ArrayList<String>();
        gen.add("CD_GENERE_1");
        gen.add("CD_GENERE_2");
        gen.add("CD_GENERE_3");
        gen.add("CD_GENERE_4");
        Disjunction disjunction;
        if ((disjunction = setCollectionDisjunction(opzioni, "XXXcd_genere_",
                gen, 4)) != null) {
            this.basicCriteria.add(disjunction);
        }
        opzioni.remove(XXXcd_genere_1);
        opzioni.remove(XXXcd_genere_2);
        opzioni.remove(XXXcd_genere_3);
        opzioni.remove(XXXcd_genere_4);
    }
    /*
     AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1
     OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2
     OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3)
     */
    private void setCdLingua(HashMap opzioni) {
        Disjunction disjunction;
        List<String> ling = new ArrayList<String>();
        ling.add("CD_LINGUA_1");
        ling.add("CD_LINGUA_2");
        ling.add("CD_LINGUA_3");
        if((disjunction = setCollectionDisjunction(opzioni,"XXXcd_lingua_",ling,3) )!=null){
            this.basicCriteria.add(disjunction);
            opzioni.remove(XXXcd_lingua_1);
            opzioni.remove(XXXcd_lingua_2);
            opzioni.remove(XXXcd_lingua_3);
        }
    }
    /*
    <opzionale dipende="XXXstringaClet"> AND UPPER(ky_clet1_t) || UPPER(ky_clet2_t)  LIKE UPPER( XXXstringaClet)||'%' </opzionale>
    */
    private void setStringaClet(HashMap opzioni) {
        if(opzioni.containsKey("XXXstringaClet")){
            this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(ky_clet1_t) || UPPER(ky_clet2_t)  LIKE UPPER(?) ||'%'",opzioni.get("XXXstringaClet"), Hibernate.STRING));
            opzioni.remove(XXXstringaClet);
        }
    }


    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            log.debug("DEBUG: PARAMETRI PASSATI");
            Iterator iter = opzioni.keySet().iterator();
            while(iter.hasNext()) {
                Object key = iter.next();
                Object value = opzioni.get(key);
				if (value == null)
                    log.debug("Chiave=" + key.toString() + "  Valore=\"" + "null" + "\"");
                else
                    log.debug("Chiave=" + key.toString() + "  Valore=\"" + value.toString() + "\"");
            }

            Class cl = Tb_titoloCommonDao.class;//this.getClass();
            Method[] metodi = cl.getDeclaredMethods();
			for (int index = 0; index < metodi.length; index++)
                if(metodi[index].getName().startsWith("set"))
					metodi[index].invoke(this, new Object[] { opzioni } );

            super.createCriteria(opzioni);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            log.error("", e);
            throw new InfrastructureException(e);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            log.error("", e);
            throw new InfrastructureException(e);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            log.error("", e);
            throw new InfrastructureException(e);
        }
    }

    //almaviva5_20091007
    // se é stato impostato un filtro per localizzazione in biblioteca
    // é necessario intercettare la costruzione del criterio hibernate
    // prima dell'esecuzione e impostare una subquery sulla TR_TIT_BIB
	public void setFiltroLocBib(HashMap opzioni) {

		CdBibType[] locBib = (CdBibType[]) opzioni.get(XXXcercaTitLocBib);
		if (!ValidazioneDati.isFilled(locBib))
			return;

		log.debug("metodo setFiltroLocBib() invocato");
		DetachedCriteria dc = DetachedCriteria.forClass(Tr_tit_bib.class, "titbib");

		dc.setProjection(Property.forName("titbib.BID"));
		dc.add(Restrictions.ne("titbib.FL_CANC", 'S'));
		dc.add(Property.forName("titbib.BID").eqProperty(Property.forName("this.BID")) );
		dc.add(Restrictions.eq("titbib.CD_POLO", locBib[0].getCdPol()) );
		//almaviva5_20091127 solo possesso
		dc.add(Restrictions.eq("titbib.FL_POSSESSO", 'S') );

		List<String> listaBib = new ArrayList<String>();
		for (CdBibType bib : locBib)
			listaBib.add(bib.getCdBib());
		dc.add(Restrictions.in("titbib.CD_BIBLIOTECA", listaBib) );

		basicCriteria.add(Property.forName("this.BID").in(dc));
		opzioni.remove(XXXcercaTitLocBib);
	}


	//almaviva5_20120530 filtro globale su biblioteca per ricerca titolo e natura='R'
	public void setFiltroBibRaccoltaFattizia(HashMap opzioni) {
		SbnUserType user = Factoring.getCurrentUser();
		if (user == null)
			return;

		String bib = user.getBiblioteca();
		if (!ValidazioneDati.isFilled(bib))
			return;

		if (!(this instanceof Vl_biblioteca_titCommonDao)) {
			LogicalExpression and = Restrictions.and(Restrictions.eq("CD_NATURA", 'R'),
					Restrictions.sqlRestriction("substring(UTE_INS, 1, 6)='" + bib + "'") );
			basicCriteria.add(Restrictions.or(Restrictions.ne("CD_NATURA", 'R'), and));
		}
	}

	public boolean isKycleslike() {
		return kycleslike;
	}

	public void Kycleslike(boolean kycleslike) {
		this.kycleslike = kycleslike;
	}

	//almaviva5_20150224
    public void setFiltriDatiComuni(HashMap opzioni) throws Exception {

    	String tipoTestoA = (String) opzioni.get(XXXtestoLetterarioAntico);		//tipo testo antico
    	String tipoTestoM = (String) opzioni.get(XXXtestoLetterarioModerno);	//tipo testo moderno
    	boolean hasTipoTestoA = ValidazioneDati.isFilled(tipoTestoA);
		boolean hasTipoTestoM = ValidazioneDati.isFilled(tipoTestoM);
		if (hasTipoTestoA && hasTipoTestoM) {
    		this.basicCriteria.add(Restrictions.or(Restrictions.eq("S140_TP_TESTO_LETTERARIO", tipoTestoA), Restrictions.eq("S105_TP_TESTO_LETTERARIO", tipoTestoM)));
    	} else if (hasTipoTestoA) {
    		this.basicCriteria.add(Restrictions.eq("S140_TP_TESTO_LETTERARIO", tipoTestoA));
        } else if (hasTipoTestoM) {
    		this.basicCriteria.add(Restrictions.eq("S105_TP_TESTO_LETTERARIO", tipoTestoM));
        }
    	opzioni.remove(XXXtestoLetterarioAntico);
    	opzioni.remove(XXXtestoLetterarioModerno);

		String forma = (String) opzioni.get(XXXformaContenuto);
		String media = (String) opzioni.get(XXXtipoMediazione);
		boolean hasForma = ValidazioneDati.isFilled(forma);
		boolean hasMedia = ValidazioneDati.isFilled(media);
		if (hasForma && hasMedia) {
			LogicalExpression and1 = Restrictions.and(Restrictions.eq("S181_TP_FORMA_CONTENUTO_1", forma), Restrictions.eq("S182_TP_MEDIAZIONE_1", media));
			LogicalExpression and2 = Restrictions.and(Restrictions.eq("S181_TP_FORMA_CONTENUTO_2", forma), Restrictions.eq("S182_TP_MEDIAZIONE_2", media));
			this.basicCriteria.add(Restrictions.or(and1, and2));
		} else if (hasForma) {
			this.basicCriteria.add(Restrictions.or(Restrictions.eq("S181_TP_FORMA_CONTENUTO_1", forma), Restrictions.eq("S181_TP_FORMA_CONTENUTO_2", forma)));
		} else if (hasMedia) {
			this.basicCriteria.add(Restrictions.or(Restrictions.eq("S182_TP_MEDIAZIONE_1", media), Restrictions.eq("S182_TP_MEDIAZIONE_2", media)));
		}
		opzioni.remove(XXXformaContenuto);
		opzioni.remove(XXXtipoMediazione);

    }

	//almaviva5_20150225
    public void setFiltriAudiovisivo(HashMap opzioni) throws Exception {
        //tipo video
    	String tipoVideo = (String) opzioni.get(XXXtipoVideo);
    	if (ValidazioneDati.isFilled(tipoVideo)) {
    		this.basicCriteria.add(Restrictions.eq("TP_MATER_AUDIOVIS", tipoVideo));
      		opzioni.remove(XXXtipoVideo);
        }

        //forma pubb. distribuzione
    	String formaPubDistr = (String) opzioni.get(XXXformaPubblicazioneDistribuzione);
    	if (ValidazioneDati.isFilled(formaPubDistr)) {
    		this.basicCriteria.add(Restrictions.eq("CD_FORMA_VIDEO", formaPubDistr));
        	opzioni.remove(XXXformaPubblicazioneDistribuzione);
        }

        //forma video
    	String tecnicaVideo = (String) opzioni.get(XXXtecnicaVideoregistrazione);
    	if (ValidazioneDati.isFilled(tecnicaVideo)) {
    		this.basicCriteria.add(Restrictions.eq("CD_TECNICA", tecnicaVideo));
        	opzioni.remove(XXXtecnicaVideoregistrazione);
        }

        //forma pubblicazione
    	String formaPub = (String) opzioni.get(XXXformaPubblicazione);
    	if (ValidazioneDati.isFilled(formaPub)) {
    		this.basicCriteria.add(Restrictions.eq("CD_FORMA", formaPub));
        	opzioni.remove(XXXformaPubblicazione);
        }

        //velocita
    	String velocita = (String) opzioni.get(XXXvelocita);
    	if (ValidazioneDati.isFilled(velocita)) {
    		this.basicCriteria.add(Restrictions.eq("CD_VELOCITA", velocita));
        	opzioni.remove(XXXvelocita);
        }

    }


    /*
    Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
 				e opportunamente salvato sulla tb_titolo campo ky_editore
    <opzionale dipende="XXXky_editore">AND bid != XXXky_editore</opzionale>
    */
   private void setKY_EDITORE(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("KY_EDITORE");
       param.setValueString(opzioni,Tb_titoloCommonDao.XXXky_editore);

       //if((value = this.setParameterEq(param))!= null)
       if((value = this.setParameterLikeAnywhere(param))!= null)
       {
           this.basicCriteria.add(value);
           opzioni.remove(XXXky_editore);
       }
   }

	public void setFiltriElettronico(HashMap opzioni) throws Exception {
		// tipo risorsa
		String tipoRisorsa = (String) opzioni.get(XXXtp_risorsa);
		if (ValidazioneDati.isFilled(tipoRisorsa)) {
			this.basicCriteria.add(Restrictions.eq("TP_RISORSA", tipoRisorsa));
			opzioni.remove(XXXtp_risorsa);
		}

		//indicazione specifica materiale
		String materiale = (String) opzioni.get(XXXcd_designazione);
		if (ValidazioneDati.isFilled(materiale)) {
			this.basicCriteria.add(Restrictions.eq("CD_DESIGNAZIONE", materiale));
			opzioni.remove(XXXcd_designazione);
		}

	}

}
