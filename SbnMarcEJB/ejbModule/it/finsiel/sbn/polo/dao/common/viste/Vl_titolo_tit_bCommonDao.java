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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public class Vl_titolo_tit_bCommonDao extends Tb_titoloCommonDao {



    protected boolean kycleslike = false;
    //Tb_titoloCommonDao titolo;

	public Vl_titolo_tit_bCommonDao() {
        super();
	}

    /*
    AND
     (
     ( ky_cles1_t  LIKE XXXstringaLike1||'%'
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
//            param = new Parameter();
//            param.setKey("KY_CLET2_T");
//            param.setValueString(opzioni,Tb_titoloCommonDao.XXXclet2_ricerca);
//            conjunction.add(this.setParameterLikeEnd(param));
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
//            opzioni.remove(XXXclet2_ricerca);

        }
    }







	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Vl_titolo_tit_bCommonDao.class;//this.getClass();
			Method[] metodi = cl.getDeclaredMethods();
			for(int index =0; index<metodi.length; index++){
				if(metodi[index].getName().startsWith("set")){
						metodi[index].invoke(this,new Object[] { opzioni});
					//log.debug(metodi[index].getName());
				}
			}
            //this.titolo = new Tb_titoloCommonDao(this.basicCriteria);
            super.createCriteria(opzioni);
            //this.titolo.createCriteria(opzioni);

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
		Vl_titolo_tit_bCommonDao aut = new Vl_titolo_tit_bCommonDao();
		aut.createCriteria(new HashMap());
		System.exit(0);
	}
}

