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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Criteria;

    public class Tb_abstractCommonDao extends TableDao {





    protected boolean kycleslike = false;

    public Tb_abstractCommonDao() {
        super();
    }
    public Tb_abstractCommonDao(Criteria titoloCriteria) {
        super();
        this.basicCriteria = titoloCriteria;
    }




    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            log.debug("DEBUG: PARAMETRI PASSATI " + "\n");
            Iterator  iter = opzioni.keySet().iterator();
            while(iter.hasNext()) {
                Object key = iter.next();
                Object value = opzioni.get(key);
                if(value==null)
                    log.debug("Chiave=" + key.toString() + "  Valore=\"" + "null" + "\"");
                else
                    log.debug("Chiave=" + key.toString() + "  Valore=\"" + value.toString() + "\"");
            }

            Class cl = Tb_abstractCommonDao.class;//this.getClass();
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
        Tb_abstractCommonDao aut = new Tb_abstractCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
	public boolean isKycleslike() {
		return kycleslike;
	}
	public void Kycleslike(boolean kycleslike) {
		this.kycleslike = kycleslike;
	}

}
