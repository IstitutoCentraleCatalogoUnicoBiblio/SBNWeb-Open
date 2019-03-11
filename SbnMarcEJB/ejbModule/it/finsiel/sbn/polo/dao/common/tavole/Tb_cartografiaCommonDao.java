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

public class Tb_cartografiaCommonDao extends TableDao{


    protected boolean kycleslike = false;

/*
    // CAMPI DELLE VISTE ( sono tutti univoci non hanno bisogno di Overwrite

    <!-- campi cartografici: sono canali di ricerca, ma non da utilizzarsi in mutua esclusione -->
    <opzionale dipende="XXXcoordOvest"> AND  longitudine_ovest like XXXcoordOvest || '%'</opzionale>
    <opzionale dipende="XXXcoordEst"> AND  longitudine_est like XXXcoordEst || '%'</opzionale>
    <opzionale dipende="XXXcoordNord"> AND  latitudine_nord like XXXcoordNord || '%'</opzionale>
    <opzionale dipende="XXXcoordSud"> AND  latitudine_sud like XXXcoordSud || '%'</opzionale>
    <!-- filtri cartografici -->
    <opzionale dipende="XXXtipoScala"> AND  tp_scala = XXXtipoScala</opzionale>
    <opzionale dipende="XXXmeridiano"> AND  cd_meridiano = XXXmeridiano</opzionale>
    <opzionale dipende="XXXscalaOrizzontale"> AND  scala_oriz like XXXscalaOrizzontale || '%'</opzionale>
    <opzionale dipende="XXXscalaVerticale"> AND  scala_vert like XXXscalaVerticale || '%'</opzionale>
    <!-- fine campi cartografici -->
*/


    public Tb_cartografiaCommonDao() {
        super();
    }

    public Tb_cartografiaCommonDao(Criteria cartografiaCriteria) {
        super();
        this.basicCriteria = cartografiaCriteria;
    }

    /*
    <opzionale dipende="XXXcoordOvest"> AND  longitudine_ovest like XXXcoordOvest || '%'</opzionale>
    */
    private void setCoordOvest(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("LONGITUDINE_OVEST");
        param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXcoordOvest);

       if ((value = this.setParameterLikeAnywhere(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(Tb_cartografiaCommonDao.XXXcoordOvest);
       }
    }

     /*
     <opzionale dipende="XXXcoordEst"> AND  longitudine_est like XXXcoordEst || '%'</opzionale>
     */
    private void setCoordEst(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("LONGITUDINE_EST");
        param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXcoordEst);
        if ((value = this.setParameterLikeAnywhere(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_cartografiaCommonDao.XXXcoordEst);
        }
    }

    /*
    <opzionale dipende="XXXcoordNord"> AND  latitudine_nord like XXXcoordNord || '%'</opzionale>
    */
   private void setCoordNord(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("LATITUDINE_NORD");
       param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXcoordNord);
       if ((value = this.setParameterLikeAnywhere(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(Tb_cartografiaCommonDao.XXXcoordNord);
       }
   }
   /*
   <opzionale dipende="XXXcoordSud"> AND  latitudine_sud like XXXcoordSud || '%'</opzionale>
   */
  private void setCoordSud(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("LATITUDINE_SUD");
      param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXcoordSud);
      if ((value = this.setParameterLikeAnywhere(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_cartografiaCommonDao.XXXcoordSud);
      }
  }

  /*
  <opzionale dipende="XXXtipoScala"> AND  tp_scala = XXXtipoScala</opzionale>
  */
  private void setTipoScala(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("TP_SCALA");
      param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXtipoScala);
     if ((value = this.setParameterEq(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_cartografiaCommonDao.XXXtipoScala);
      }
  }

  /*
  <opzionale dipende="XXXmeridiano"> AND  cd_meridiano = XXXmeridiano</opzionale>
  */
  private void setMeridiano(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("CD_MERIDIANO");
      param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXmeridiano);
      if ((value = this.setParameterEq(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_cartografiaCommonDao.XXXmeridiano);
      }
  }

  /*
  <opzionale dipende="XXXscalaOrizzontale"> AND  scala_oriz like XXXscalaOrizzontale || '%'</opzionale>
  <opzionale dipende="XXXscalaVerticale"> AND  scala_vert like XXXscalaVerticale || '%'</opzionale>
  */
  private void setScalaOrizzontale(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("SCALA_ORIZ");
      param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXscalaOrizzontale);
      if ((value = this.setParameterLikeAnywhere(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_cartografiaCommonDao.XXXscalaOrizzontale);
      }
  }

  /*
  <opzionale dipende="XXXscalaVerticale"> AND  scala_vert like XXXscalaVerticale || '%'</opzionale>
  */
  private void setScalaVerticale(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("SCALA_VERT");
      param.setValueString(opzioni,Tb_cartografiaCommonDao.XXXscalaVerticale);
      if ((value = this.setParameterLikeAnywhere(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(Tb_cartografiaCommonDao.XXXscalaVerticale);
      }
  }

    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_cartografiaCommonDao.class;//this.getClass();
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
        Tb_cartografiaCommonDao aut = new Tb_cartografiaCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}


