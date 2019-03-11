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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_cartografiaCommonDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_luogoCommonDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Ve_cartografia_tit_c_luoCommonDao extends Tb_titoloCommonDao {




    protected boolean kycleslike = false;
    Tb_cartografiaCommonDao cartografia;
    Tb_luogoCommonDao luogo;
    //Tb_titoloCommonDao titolo;

	public Ve_cartografia_tit_c_luoCommonDao() {
        super();
	}


	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Ve_cartografia_tit_c_luoCommonDao.class;//this.getClass();
			Method[] metodi = cl.getDeclaredMethods();
			for(int index =0; index<metodi.length; index++){
				if(metodi[index].getName().startsWith("set")){
						metodi[index].invoke(this,new Object[] { opzioni});
					//log.debug(metodi[index].getName());
				}
			}
            this.cartografia = new Tb_cartografiaCommonDao(this.basicCriteria);
            this.luogo = new Tb_luogoCommonDao(this.basicCriteria);
            //this.titolo = new Tb_titoloCommonDao(this.basicCriteria);

            super.createCriteria(opzioni);
            this.cartografia.createCriteria(opzioni);
            this.luogo.createCriteria(opzioni);
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
		Ve_cartografia_tit_c_luoCommonDao aut = new Ve_cartografia_tit_c_luoCommonDao();
		aut.createCriteria(new HashMap());
		System.exit(0);
	}
}

