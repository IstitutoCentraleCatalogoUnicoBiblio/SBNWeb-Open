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
import it.finsiel.sbn.polo.dao.vo.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

    public class Tb_luogoCommonDao extends TableDao {


        /*
        Criteri di tipo univoco generalizzati
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


    */

/*
        DEFAULT <opzionale dipende="XXXcd_paese ">AND cd_paese = XXXcd_paese</opzionale>
        DEFAULT <opzionale dipende="XXXlivello_aut_da">AND cd_livello &gt;= XXXlivello_aut_da</opzionale>
        DEFAULT <opzionale dipende="XXXlivello_aut_a">AND cd_livello &lt;=  XXXlivello_aut_a</opzionale>
        DEFAULT <opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
        DEFAULT <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
        DEFAULT <opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
        DEFAULT <opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>

        <opzionale dipende="XXXcd_livello"> AND cd_livello = XXXcd_livello</opzionale>
        IN BIS <opzionale dipende="XXXky_norm_luogo_puntuale"> AND  ky_norm_luogo LIKE UPPER(XXXky_norm_luogo_puntuale) </opzionale>
        IN BIS <opzionale dipende="XXXky_norm_luogo_inizio">AND  UPPER(ky_norm_luogo) &gt;= UPPER(XXXky_norm_luogo_inizio) AND  UPPER(ky_norm_luogo) &lt;  UPPER(XXXky_norm_luogo_fine)</opzionale>



        // CAMPI DELLE VISTE ( sono tutti univoci non hanno bisogno di Overwrite
        <!-- inizio campi ereditati da Tb_luogoCommonDao -->
        <opzionale dipende="XXXStringaLikeLuogo"> AND UPPER(ky_norm_luogo) like '%'||UPPER(XXXStringaLikeLuogo)||'%' </opzionale>
        <opzionale dipende="XXXStringaEsattaLuogo"> AND UPPER(ky_norm_luogo)=UPPER(XXXStringaEsattaLuogo) </opzionale>
        <!-- fine campi ereditati da Tb_luogoCommonDao -->
*/




    protected boolean kycleslike = false;

    public Tb_luogoCommonDao() {
        super();
    }
    public Tb_luogoCommonDao(Criteria luogoCriteria) {
        super();
        this.basicCriteria = luogoCriteria;
    }





    /*
    <opzionale dipende="XXXStringaLikeLuogo"> AND UPPER(ky_norm_luogo) like '%'||UPPER(XXXStringaLikeLuogo)||'%' </opzionale>
     */
    public void setStringaLikeLuogo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_NORM_LUOGO");
        param.setValueString(opzioni,Tb_luogoCommonDao.XXXStringaLikeLuogo);

        if ((value = this.setParameterLikeAnywhereUpper(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_luogoCommonDao.XXXStringaLikeLuogo);
        }
    }

    /*
    <opzionale dipende="XXXStringaEsattaLuogo"> AND UPPER(ky_norm_luogo)=UPPER(XXXStringaEsattaLuogo) </opzionale>
     */
    public void setStringaEsattaLuogo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_NORM_LUOGO");
        param.setValueString(opzioni,Tb_luogoCommonDao.XXXStringaEsattaLuogo);

        if ((value = this.setParameterEqUpper(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_luogoCommonDao.XXXStringaEsattaLuogo);
        }
    }
    /*
    <opzionale dipende="XXXcd_livello"> AND cd_livello = XXXcd_livello</opzionale>
     */
    public void setCdLivello(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni,Tb_luogoCommonDao.XXXcd_livello);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
        }
    }

    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_luogoCommonDao.class;//this.getClass();
            Method[] metodi = cl.getDeclaredMethods();
            for(int index =0; index<metodi.length; index++){
                if(metodi[index].getName().startsWith("set")){
                        metodi[index].invoke(this,new Object[] { opzioni});
                    //log.debug(metodi[index].getName());
                }
            }
            super.createCriteria(opzioni);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InfrastructureException(e);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InfrastructureException(e);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InfrastructureException(e);
        }
    }
    public static void main(String[] args) throws Exception{
        Tb_luogoCommonDao aut = new Tb_luogoCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }

    }
