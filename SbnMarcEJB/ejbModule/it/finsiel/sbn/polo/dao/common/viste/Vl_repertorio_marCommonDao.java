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
package it.finsiel.sbn.polo.dao.common.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_repertorioCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.criterion.Criterion;

public class Vl_repertorio_marCommonDao extends Tb_repertorioCommonDao{


	protected boolean kycleslike = false;

    public Vl_repertorio_marCommonDao() {
        super();
	}

    /*
     * <opzionale dipende="XXXmid"> AND mid != XXXmid </opzionale>
     */
   private void setMID(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("MID");
       param.setValueString(opzioni,Vl_repertorio_marCommonDao.XXXmid);

       if((value = this.setParameterNotEq(param))!= null){
           this.basicCriteria.add(value);
           opzioni.remove(Vl_repertorio_marCommonDao.XXXmid);
       }
   }



	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Vl_repertorio_marCommonDao.class;//this.getClass();
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
		Vl_repertorio_marCommonDao aut = new Vl_repertorio_marCommonDao();
		aut.createCriteria(new HashMap());
		System.exit(0);
	}
}

