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
import it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.criterion.Criterion;

public class Vl_biblioteca_titCommonDao extends Tb_titoloCommonDao {

// almaviva DA RIVEDERE NON ESISTE l'IMPLEMENTAZIONE DEL CICLICO si usa per l'export
// almaviva VERIFICARE ESISTENZA
//    <opzionale dipende="XXXcd_polo"> AND cd_polo = XXXcd_polo</opzionale>
//    <opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca IN (</opzionale>
//    <opzionale dipende="XXXcdBiblio" ciclico="S" separatore=",">XXXcdBiblio</opzionale>
//    <opzionale dipende="XXXcd_biblioteca">)</opzionale>

    protected boolean kycleslike = false;
    //Tb_titoloCommonDao titolo;

    public Vl_biblioteca_titCommonDao() {
        super();
    }

    /*
    <opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca = XXXcd_biblioteca</opzionale>
     */
    public void setCdBiblioteca(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("Cd_biblioteca");
        param.setValueString(opzioni,Tr_aut_bibCommonDao.XXXcd_biblioteca);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
        }
    }

    /*
    <opzionale dipende="XXXcd_polo"> AND cd_polo = XXXcd_polo</opzionale>
     */
    public void setCdPolo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("Cd_polo");
        param.setValueString(opzioni,Tr_aut_bibCommonDao.XXXcd_polo);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
        }
    }


    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Vl_biblioteca_titCommonDao.class;//this.getClass();
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
        Vl_biblioteca_titCommonDao aut = new Vl_biblioteca_titCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}


