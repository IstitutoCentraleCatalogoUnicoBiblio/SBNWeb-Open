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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_marcaCommonDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;

public class Vl_marca_parCommonDao extends Tb_marcaCommonDao{



    protected boolean kycleslike = false;

	public Vl_marca_parCommonDao() {
        super();
	}
    /*
    <opzionale dipende="XXXparola1"> AND mid in  (select  mid from tb_parola  where parola = XXXparola1</opzionale>
    <opzionale dipende="XXXparola2"> AND mid in  (select  mid from tb_parola  where parola = XXXparola2 ) </opzionale>
    <opzionale dipende="XXXparola1"> ) </opzionale>
    */
    private void setParola(HashMap opzioni) {
        if(opzioni.containsKey("XXXparola1")){
            this.basicCriteria.add(Restrictions.sqlRestriction("mid in  (select  mid from tb_parola  where parola = ? )",opzioni.get("XXXparola1"  ), Hibernate.STRING));
        }
        if(opzioni.containsKey("XXXparola2")){
            this.basicCriteria.add(Restrictions.sqlRestriction("mid in  (select  mid from tb_parola  where parola = ? )",opzioni.get("XXXparola2"), Hibernate.STRING));
        }
//        if(opzioni.containsKey("XXXparola1")){
//            this.basicCriteria.add(Restrictions.sqlRestriction(")" ));
//        }
    }


	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Vl_marca_parCommonDao.class;//this.getClass();
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
		Vl_marca_parCommonDao aut = new Vl_marca_parCommonDao();
		aut.createCriteria(new HashMap());
		System.exit(0);
	}
}

