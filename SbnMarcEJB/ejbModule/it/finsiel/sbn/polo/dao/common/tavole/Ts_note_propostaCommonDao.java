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
import java.text.ParseException;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

public class Ts_note_propostaCommonDao extends TableDao {

	public Ts_note_propostaCommonDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ts_note_propostaCommonDao(Criteria basicCriteria) {
		super(basicCriteria);
		// TODO Auto-generated constructor stub
	}

	  /*
	<opzionale dipende="XXXid_oggetto"> AND id_oggetto = XXXid_oggetto </opzionale>
	  */
	 private void setId_oggetto(HashMap opzioni) throws Exception {
	     Criterion value;
	     Parameter param = new Parameter();
	     param.setKey("ID_OGGETTO");
	     param.setValueString(opzioni, TableDao.XXXid_oggetto);

	     if ((value = this.setParameterEq(param)) != null) {
	         this.basicCriteria.add(value);
	         opzioni.remove(TableDao.XXXid_oggetto);
	     }
	 }

  /*
  <opzionale dipende="XXXid_proposta_string"> AND id_proposta = XXXid_proposta_string </opzionale>
  */
 private void setId_proposta_string(HashMap opzioni) throws Exception {
     Criterion value;
     Parameter param = new Parameter();
     param.setKey("ID_PROPOSTA_STRING");
     param.setValueString(opzioni, TableDao.XXXid_proposta_string);

     if ((value = this.setParameterEq(param)) != null) {
         this.basicCriteria.add(value);
         opzioni.remove(TableDao.XXXid_proposta_string);
     }
 }
 /*
 <opzionale dipende="XXXute_mittente"> AND ute_mittente = XXXute_mittente </opzionale>
 */
private void setUte_mittente(HashMap opzioni) throws Exception {
    Criterion value;
    Parameter param = new Parameter();
    param.setKey("UTE_MITTENTE");
    param.setValueString(opzioni, TableDao.XXXute_mittente);

    if ((value = this.setParameterEq(param)) != null) {
        this.basicCriteria.add(value);
        opzioni.remove(TableDao.XXXute_mittente);
    }
}


/*
<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
<opzionale dipende="XXXdata_Da"> AND dt_inoltro &gt;= to_timestamp(XXXdata_Da ||'000000.0','yyyy-mm-ddHH24miss.FF')</opzionale>
<opzionale dipende="XXXdata_A"> AND dt_inoltro &lt;= to_timestamp(XXXdata_A ||'235959.9','yyyy-mm-ddHH24miss.FF')</opzionale>

 */
public void setData(HashMap opzioni) throws ParseException {
    Criterion value;
    Parameter param0;
    Parameter param1;
    param0 = new Parameter();
    param0.setKey("DT_INOLTRO");
    param0.setValueDate(opzioni, TableDao.XXXdata_Da,"yyyy-mm-dd");
    param1 = new Parameter();
    param1.setKey("DT_INOLTRO");
    param1.setValueDate(opzioni, TableDao.XXXdata_A,"yyyy-mm-dd");

    if((value=setParameterDaA(param0,param1))!= null){
        this.basicCriteria.add(value);
        opzioni.remove(TableDao.XXXdata_Da);
        opzioni.remove(TableDao.XXXdata_A);
    }
}

    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Ts_note_propostaCommonDao.class;//this.getClass();
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

}
