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
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

    public class Tb_marcaCommonDao extends TableDao {


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
        <opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) &gt; 0 </opzionale>
        <opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) &gt; 0 </opzionale>
        <opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) &gt; 0 </opzionale>
        <opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) &gt; 0 </opzionale>
        DEFAULT <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
        DEFAULT <opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
        DEFAULT <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
        DEFAULT <opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXdata_var_A </opzionale>
        DEFAULT <opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
        DEFAULT <opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXdata_ins_A </opzionale>
        DEFAULT <opzionale dipende="XXXdata_inizio_variazione"> AND to_char(ts_var,'yyyymmddhh24miss.FF') &gt;XXXdata_inizio_variazione  </opzionale>
        DEFAULT <opzionale dipende="XXXdata_fine_variazione"> AND to_char(ts_var,'yyyymmddhh24miss.FF') &lt;  XXXdata_fine_variazione  </opzionale>
    </statement>

// CAMPI DELLE VISTE ( sono tutti univoci non hanno bisogno di Overwrite
<!-- inizio campi ereditati da Tb_marcaCommonDao -->
DEFAULT <opzionale dipende="XXXparola1"> AND CONTAINS(ds_marca, XXXparola1 ) > 0 </opzionale>
DEFAULT <opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) > 0 </opzionale>
DEFAULT <opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) > 0 </opzionale>
DEFAULT <opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) > 0 </opzionale>
DEFAULT <opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) > 0 </opzionale>
<opzionale dipende="XXXStringaEsattaMarca"> AND UPPER (DS_MARCA) = UPPER(XXXStringaEsattaMarca)</opzionale>
<opzionale dipende="XXXStringaLikeMarca"> AND UPPER (DS_MARCA) LIKE UPPER(XXXStringaLikeMarca)||'%'</opzionale>


<!-- fine campi ereditati da Tb_marcaCommonDao -->


*/
    protected boolean kycleslike = false;

    public Tb_marcaCommonDao() {
        super();
    }
    public Tb_marcaCommonDao(Criteria marcaCriteria) {
        super();
        this.basicCriteria = marcaCriteria;
    }


    /*
    <opzionale dipende="XXXStringaEsattaMarca"> AND UPPER (DS_MARCA) = UPPER(XXXStringaEsattaMarca)</opzionale>
   */
   private void setStringaEsattaMarca(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("DS_MARCA");
       param.setValueString(opzioni,Tb_marcaCommonDao.XXXStringaEsattaMarca);

       if ((value = this.setParameterEqUpper(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(Tb_marcaCommonDao.XXXStringaEsattaMarca);
       }
   }

   /*
   <opzionale dipende="XXXStringaLikeMarca"> AND UPPER (DS_MARCA) LIKE UPPER(XXXStringaLikeMarca)||'%'</opzionale>
  */
   private void setStringaLikeMarca(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("DS_MARCA");
      param.setValueString(opzioni,Tb_marcaCommonDao.XXXStringaLikeMarca);
      if ((value = this.setParameterLikeEndUpper(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_marcaCommonDao.XXXStringaLikeMarca);
      }
   }

    /*

    <opzionale dipende="XXXparola1"> AND CONTAINS(ds_marca, XXXparola1 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) &gt; 0 </opzionale>
    <opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) &gt; 0 </opzionale>

    */
   // ATTENZIONE PARAMETRIZZARE LA RICERCA con set_curcfg (adesso solo default)
   // perchè questo parametro puo variare in base alle ricerche (inserirlo in properties????)
   private void setParola(HashMap opzioni) throws InfrastructureException {

	   	if(isSessionPostgreSQL()){
	   		String appo = null;
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola1)){
		   		appo = "tidx_vector  @@ to_tsquery(";
		   		if (isPostgresVersion83())
		   			appo += "'pg_catalog.italian', ";
		   		else
		   			appo += "'default', ";
	        	appo = appo + " '" + opzioni.get(Tb_marcaCommonDao.XXXparola1);
	            opzioni.remove(Tb_marcaCommonDao.XXXparola1);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola2)){
	        	appo = appo + "&" + opzioni.get(Tb_marcaCommonDao.XXXparola2);
	            opzioni.remove(Tb_marcaCommonDao.XXXparola2);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola3)){
	        	appo = appo + "&" + opzioni.get(Tb_marcaCommonDao.XXXparola3);
	            opzioni.remove(Tb_marcaCommonDao.XXXparola3);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola4)){
	        	appo = appo + "&" + opzioni.get(Tb_marcaCommonDao.XXXparola4);
	            opzioni.remove(Tb_marcaCommonDao.XXXparola4);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola5)){
	        	appo = appo + "&" + opzioni.get(Tb_marcaCommonDao.XXXparola5);
	            opzioni.remove(Tb_marcaCommonDao.XXXparola5);
	        }
	        if(appo != null){
	        	appo = appo + "')";
	        	this.basicCriteria.add(Restrictions.sqlRestriction(appo));
	        }


   		}else{
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola1)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_MARCA, ? ) > 0",opzioni.get(Tb_marcaCommonDao.XXXparola1), Hibernate.STRING));
	            opzioni.remove(Tb_marcaCommonDao.XXXparola1);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola2)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_MARCA, ? ) > 0",opzioni.get(Tb_marcaCommonDao.XXXparola2), Hibernate.STRING));
	            opzioni.remove(Tb_marcaCommonDao.XXXparola2);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola3)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_MARCA, ? ) > 0",opzioni.get(Tb_marcaCommonDao.XXXparola3), Hibernate.STRING));
	            opzioni.remove(Tb_marcaCommonDao.XXXparola3);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola4)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_MARCA, ? ) > 0",opzioni.get(Tb_marcaCommonDao.XXXparola4), Hibernate.STRING));
	            opzioni.remove(Tb_marcaCommonDao.XXXparola4);
	        }
	        if(opzioni.containsKey(Tb_marcaCommonDao.XXXparola5)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_MARCA, ? ) > 0",opzioni.get(Tb_marcaCommonDao.XXXparola5), Hibernate.STRING));
	            opzioni.remove(Tb_marcaCommonDao.XXXparola5);
	        }
   		}
    }



    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_marcaCommonDao.class;//this.getClass();
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
        Tb_marcaCommonDao aut = new Tb_marcaCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }

}
