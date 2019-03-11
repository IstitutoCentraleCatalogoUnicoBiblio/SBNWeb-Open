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

public class Tb_musicaCommonDao extends TableDao {


/*
// CAMPI DELLE VISTE ( sono tutti univoci non hanno bisogno di Overwrite
    <!-- campi musicali -->
    <opzionale dipende="XXXtipoTesto"> AND tp_testo_letter = XXXtipoTesto </opzionale>
    <opzionale dipende="XXXorganicoSintetico"> AND ds_org_sint like XXXorganicoSintetico </opzionale>
    <opzionale dipende="XXXorganicoAnalitico"> AND ds_org_anal like XXXorganicoAnalitico </opzionale>
    <opzionale dipende="XXXtipoElaborazione"> AND tp_elaborazione = XXXtipoElaborazione </opzionale>
    <opzionale dipende="XXXpresentazione"> AND cd_presentazione = XXXpresentazione </opzionale>
    <!-- fine campi musicali -->
*/

    protected boolean kycleslike = false;


    public Tb_musicaCommonDao() {
        super();
    }

    public Tb_musicaCommonDao(Criteria musicaCriteria) {
        super();
        this.basicCriteria = musicaCriteria;
    }

    /*
     <opzionale dipende="XXXtipoTesto"> AND tp_testo_letter = XXXtipoTesto </opzionale>
    */
    private void setTipoTesto(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_TESTO_LETTER");
        param.setValueString(opzioni,Tb_musicaCommonDao.XXXtipoTesto);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_musicaCommonDao.XXXtipoTesto);
        }
    }
    /*
    <opzionale dipende="XXXorganicoSintetico"> AND ds_org_sint like XXXorganicoSintetico </opzionale>
   */
   private void setOrganicoSintetico(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("DS_ORG_SINT");
       param.setValueString(opzioni,Tb_musicaCommonDao.XXXorganicoSintetico);

       if ((value = this.setParameterLikeEnd(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(Tb_musicaCommonDao.XXXorganicoSintetico);
       }
   }

   /*
   <opzionale dipende="XXXorganicoAnalitico"> AND ds_org_anal like XXXorganicoAnalitico </opzionale>
  */
  private void setOrganicoAnalitico(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("DS_ORG_ANAL");
      param.setValueString(opzioni,Tb_musicaCommonDao.XXXorganicoAnalitico);

      if ((value = this.setParameterLikeEnd(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_musicaCommonDao.XXXorganicoAnalitico);
      }
  }

  /*
  <opzionale dipende="XXXtipoElaborazione"> AND tp_elaborazione = XXXtipoElaborazione </opzionale>
 */
  private void setTipoElaborazione(HashMap opzioni) throws Exception {
     Criterion value;
     Parameter param = new Parameter();
     param.setKey("TP_ELABORAZIONE");
     param.setValueString(opzioni,Tb_musicaCommonDao.XXXtipoElaborazione);

     if ((value = this.setParameterEq(param)) != null) {
         this.basicCriteria.add(value);
         opzioni.remove(Tb_musicaCommonDao.XXXtipoElaborazione);
     }
 }

  /*
  <opzionale dipende="XXXpresentazione"> AND cd_presentazione = XXXpresentazione </opzionale>
 */
  private void setPresentazione(HashMap opzioni) throws Exception {
     Criterion value;
     Parameter param = new Parameter();
     param.setKey("CD_PRESENTAZIONE");
     param.setValueString(opzioni,Tb_musicaCommonDao.XXXpresentazione);

     if ((value = this.setParameterEq(param)) != null) {
         this.basicCriteria.add(value);
         opzioni.remove(Tb_musicaCommonDao.XXXpresentazione);
     }
 }



    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_musicaCommonDao.class;//this.getClass();
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
        Tb_musicaCommonDao aut = new Tb_musicaCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}
