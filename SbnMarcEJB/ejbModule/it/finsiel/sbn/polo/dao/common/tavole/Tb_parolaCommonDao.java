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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

    public class Tb_parolaCommonDao extends TableDao {


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

        <opzionale dipende="XXXparola2"> OR UPPER (parola) LIKE '%'||UPPER(XXXparola2)||'%' </opzionale>
        <opzionale dipende="XXXparola3"> OR UPPER (parola) LIKE '%'||UPPER(XXXparola3)||'%' </opzionale>
        <opzionale dipende="XXXparola4"> OR UPPER (parola) LIKE '%'||UPPER(XXXparola4)||'%' </opzionale>

*/
    protected boolean kycleslike = false;

    public Tb_parolaCommonDao() {
        super();
    }
    public Tb_parolaCommonDao(Criteria parolaCriteria) {
        super();
        this.basicCriteria = parolaCriteria;
    }




    /*

    <opzionale dipende="XXXparola1"> AND CONTAINS(ds_marca, XXXparola1 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) &gt; 0 </opzionale>


    <opzionale dipende="XXXparola2"> OR UPPER (parola) LIKE '%'||UPPER(XXXparola2)||'%' </opzionale>
    <opzionale dipende="XXXparola3"> OR UPPER (parola) LIKE '%'||UPPER(XXXparola3)||'%' </opzionale>
    <opzionale dipende="XXXparola4"> OR UPPER (parola) LIKE '%'||UPPER(XXXparola4)||'%' </opzionale>

    */
    public void setParola(HashMap opzioni) throws InfrastructureException {
    	// FORSE NON VENGONO USERE LE CONTEINS SULLA TABELLA TB_PAROLA IN QUANTO NON ESITONO INDICI
	        if(opzioni.containsKey(Tb_parolaCommonDao.XXXparola2)){
	            Disjunction disjunction = Restrictions.disjunction();
	            disjunction.add(Restrictions.sqlRestriction("UPPER (parola) LIKE '%'||UPPER(?)||'%'",opzioni.get(Tb_parolaCommonDao.XXXparola2), Hibernate.STRING));
	            this.basicCriteria.add(disjunction);
	        }
	        if(opzioni.containsKey(Tb_parolaCommonDao.XXXparola3)){
	            Disjunction disjunction = Restrictions.disjunction();
	            disjunction.add(Restrictions.sqlRestriction("UPPER (parola) LIKE '%'||UPPER(?)||'%'",opzioni.get(Tb_parolaCommonDao.XXXparola3), Hibernate.STRING));
	            this.basicCriteria.add(disjunction);
	        }
	        if(opzioni.containsKey(Tb_parolaCommonDao.XXXparola4)){
	            Disjunction disjunction = Restrictions.disjunction();
	            disjunction.add(Restrictions.sqlRestriction("UPPER (parola) LIKE '%'||UPPER(?)||'%'",opzioni.get(Tb_parolaCommonDao.XXXparola4), Hibernate.STRING));
	            this.basicCriteria.add(disjunction);
	        }
    }


    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_parolaCommonDao.class;//this.getClass();
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
        Tb_parolaCommonDao aut = new Tb_parolaCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }

    }
