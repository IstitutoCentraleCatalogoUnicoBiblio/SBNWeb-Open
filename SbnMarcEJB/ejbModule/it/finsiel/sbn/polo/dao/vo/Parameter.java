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
package it.finsiel.sbn.polo.dao.vo;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.BasicTableDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_autoreCommonDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class Parameter {

	public static int date = 0;
	public static int string = 1;
	public static int integer = 2;
	public static int collection = 3;

	private Object value = null;
	private String key = null;
	private int type = 1;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public boolean isValid()
	{
		if(this.value!= null)
			return true;
		else
			return false;
	}


	public void setValueCollection(Object value) {
		this.type = Parameter.collection;
		this.value = value;
	}
	public void setValueString(Object value) throws Exception {
		if(value instanceof String) {
			this.type = Parameter.string;
			this.value = value;
		}else
			throw new Exception();
	}
	public void setValueDate(Object value,String format) throws ParseException {
		if(value instanceof String) {
			this.type = Parameter.date;
			SimpleDateFormat sp = new SimpleDateFormat(format);
			this.value = sp.parse((String) value);
		}
	}
	public void setValueInteger(Object value) throws Exception {
		if(value instanceof Integer) {
			this.type = Parameter.integer;
			this.value = value;
		}else
			throw new Exception();

	}






	public void setValueString(HashMap opzione,String Key) throws Exception {
		if(opzione.containsKey(Key))
		{
			this.setValueString(opzione.get(Key));
		}
	}
	public void setValueDate(HashMap opzione,String Key,String format) throws ParseException {
		if(opzione.containsKey(Key))
		{
			this.setValueDate(opzione.get(Key),format);
		}
	}
	public void setValueString(HashMap opzione,String Key,String costante) throws Exception {
		if(opzione.containsKey(Key))
		{
			this.setValueString(costante);
		}
	}
	public void setValueCollection(HashMap opzione,String Key,int maxRow) {
		List<String> value = new ArrayList<String>();

		for (int index = 1; index <= maxRow; index++) {
			if (opzione.containsKey( Key + String.valueOf(index))) {
				value.add((String)opzione.get(Key	+ String.valueOf(index)));
			}
		}
		this.type = Parameter.collection;
		this.value = value;
	}











	public void setValueInteger(HashMap opzione,String Key,String costante) throws Exception {
//		this.type = Parameter.integer;
//		this.value = value;
		if(opzione.containsKey(Key))
		{
			this.setValueInteger(costante);
		}

	}



	public void setValueInteger(HashMap opzione,String Key) {
		this.type = Parameter.integer;
		this.value = opzione.get(Key);
	}




	public void setValueCollection(HashMap opzione,String Key,String costante) {
//		this.type = Parameter.collection;
//		this.value = value;
		if(opzione.containsKey(Key))
		{
			this.setValueCollection(costante);
		}
	}

    public static Criteria setOrdinamento(HashMap opzioni,String table,Criteria basicCriteria,Session session ) throws InfrastructureException {
        String TmpOrder = "";

        if((opzioni.containsKey("ORDER") && opzioni.get("ORDER") != null)){
            TmpOrder = table.toUpperCase() + "_" + opzioni.get("ORDER");
            Query order = session.getNamedQuery((TmpOrder).toString());

            if(BasicTableDao.isSessionPostgreSQL(session)){
            	//DEVO SOSTITUIRE NVL IN COALESCE
                String OrdPostGres = "";
                OrdPostGres = order.getQueryString().replaceAll("NVL"," COALESCE");
                basicCriteria.add(Restrictions.sqlRestriction(OrdPostGres));
                opzioni.remove(Tb_autoreCommonDao.ORDER);
            }
            if(BasicTableDao.isSessionOracle(session)){
                basicCriteria.add(Restrictions.sqlRestriction(order.getQueryString()));
                opzioni.remove(Tb_autoreCommonDao.ORDER);
            }

        }



//        Query order = session.getNamedQuery(("VE_CARTOGRAFIA_AUT_AUT_order_5").toString());
//        order.getQueryString();
//        basicCriteria.add(Restrictions.sqlRestriction(order.getQueryString()));

        return basicCriteria;
    }

}
