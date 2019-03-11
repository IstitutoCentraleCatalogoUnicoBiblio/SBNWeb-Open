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


public class Tb_termine_thesauroCommonDao extends TableDao {





    protected boolean kycleslike = false;

    public Tb_termine_thesauroCommonDao() {
        super();
    }
    public Tb_termine_thesauroCommonDao(Criteria termine_thesauroCriteria) {
        super();
        this.basicCriteria = termine_thesauroCriteria;
    }

    /*
    <opzionale dipende="XXXparola1"> AND CONTAINS(DS_TERMINE_THESAURO, XXXparola1 ) > 0 </opzionale>
    <opzionale dipende="XXXparola2"> AND CONTAINS(DS_TERMINE_THESAURO, XXXparola2 ) > 0 </opzionale>
    <opzionale dipende="XXXparola3"> AND CONTAINS(DS_TERMINE_THESAURO, XXXparola3 ) > 0 </opzionale>
    <opzionale dipende="XXXparola4"> AND CONTAINS(DS_TERMINE_THESAURO, XXXparola4 ) > 0 </opzionale>

    */
    public void setParola(HashMap opzioni) throws InfrastructureException {
	   	if(isSessionPostgreSQL()){
	   		String appo = null;
			if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola1)){
		   		appo = "tidx_vector  @@ to_tsquery(";
		   		if (isPostgresVersion83())
		   			appo += "'pg_catalog.italian', ";
		   		else
		   			appo += "'default', ";
	        	appo = appo + " '" + opzioni.get(Tb_termine_thesauroCommonDao.XXXparola1);
	            opzioni.remove(Tb_termine_thesauroCommonDao.XXXparola1);
	        }
	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola2)){
	        	appo = appo + "&" + opzioni.get(Tb_termine_thesauroCommonDao.XXXparola2);
	            opzioni.remove(Tb_termine_thesauroCommonDao.XXXparola2);
	        }
	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola3)){
	        	appo = appo + "&" + opzioni.get(Tb_termine_thesauroCommonDao.XXXparola3);
	            opzioni.remove(Tb_termine_thesauroCommonDao.XXXparola3);
	        }
	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola4)){
	        	appo = appo + "&" + opzioni.get(Tb_termine_thesauroCommonDao.XXXparola4);
	            opzioni.remove(Tb_termine_thesauroCommonDao.XXXparola4);
	        }
	        if(appo != null){
	        	appo = appo + "')";
	        	this.basicCriteria.add(Restrictions.sqlRestriction(appo));
	        }
	   	}else{

	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola1)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_TERMINE_THESAURO, ? ) > 0",opzioni.get(Tb_termine_thesauroCommonDao.XXXparola1), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola2)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_TERMINE_THESAURO, ? ) > 0",opzioni.get(Tb_termine_thesauroCommonDao.XXXparola2), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola3)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_TERMINE_THESAURO, ? ) > 0",opzioni.get(Tb_termine_thesauroCommonDao.XXXparola3), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(Tb_termine_thesauroCommonDao.XXXparola4)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_TERMINE_THESAURO, ? ) > 0",opzioni.get(Tb_termine_thesauroCommonDao.XXXparola4), Hibernate.STRING));
	        }
	   	}
    }









   /*
   <opzionale dipende="XXXcd_the"> AND cd_the = XXXcd_the </opzionale>
   */
  public void setCdThe(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("CD_THE");
//      param.setValueString(opzioni,Tb_descrittoreCommonDao.XXXds_soggetto);
      param.setValueString(opzioni,Tb_termine_thesauroCommonDao.XXXcd_the);

      if ((value = this.setParameterEq(param)) != null) {
          this.basicCriteria.add(value);
      }
  }

    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_termine_thesauroCommonDao.class;//this.getClass();
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
        Tb_termine_thesauroCommonDao aut = new Tb_termine_thesauroCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }

}














