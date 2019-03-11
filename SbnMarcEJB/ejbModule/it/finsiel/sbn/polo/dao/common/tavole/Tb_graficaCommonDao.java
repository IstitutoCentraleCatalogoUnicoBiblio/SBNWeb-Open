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

public class Tb_graficaCommonDao extends TableDao{

    protected boolean kycleslike = false;
/*
 *
// CAMPI DELLE VISTE ( sono tutti univoci non hanno bisogno di Overwrite

<!-- campi grafici -->
<opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
<opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
<opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
<opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
<opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
<opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
<opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
<opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
<opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
<opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
<!-- fine campi grafici -->

 */

    public Tb_graficaCommonDao() {
        super();
    }
    public Tb_graficaCommonDao(Criteria graficaCriteria) {
        super();
        this.basicCriteria = graficaCriteria;
    }







    /*
    <opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
     */
    public void setTpMaterialeGra(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_MATERIALE_GRA");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXtpMaterialeGra);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXtpMaterialeGra);
        }
    }

    /*
    <opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
     */
    public void setCdSupporto(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_SUPPORTO");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdSupporto);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdSupporto);
        }
    }

    /*
    <opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
     */
    public void setCdColore(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_COLORE");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdColore);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdColore);
        }
    }

    /*
    <opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
    <opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
    <opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
     */
    public void setCdTecnicaDis1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TECNICA_DIS_1");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdTecnicaDis_1);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdTecnicaDis_1);
        }
    }

    /*
    <opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
     */
    public void setCdTecnicaDis2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TECNICA_DIS_2");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdTecnicaDis_2);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdTecnicaDis_2);
        }
    }
    /*
    <opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
     */
    public void setCdTecnicaDis3(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TECNICA_DIS_3");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdTecnicaDis_3);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdTecnicaDis_3);
        }
    }

    /*
    <opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
    <opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
    <opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
     */
    public void setCdTecnicaSta1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TECNICA_STA_1");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdTecnicaSta_1);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdTecnicaSta_1);
        }
    }

    /*
    <opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
     */
    public void setCdTecnicaSta2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TECNICA_STA_2");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdTecnicaSta_2);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdTecnicaSta_2);
        }
    }
    /*
    <opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
     */
    public void setCdTecnicaSta3(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TECNICA_STA_3");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdTecnicaSta_3);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdTecnicaSta_3);
        }
    }

    /*
    <opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
     */
    public void setCdDesignFunz(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_DESIGN_FUNZ");
        param.setValueString(opzioni,Tb_graficaCommonDao.XXXcdDesignFunz);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_graficaCommonDao.XXXcdDesignFunz);
        }
    }


    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_graficaCommonDao.class;//this.getClass();
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
        Tb_graficaCommonDao aut = new Tb_graficaCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}


