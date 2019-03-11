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
package it.finsiel.sbn.polo.dao.entity.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.viste.Ve_grafica_luo_aut;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Ve_grafica_luo_autResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao{

    public Ve_grafica_luo_autResult(Ve_grafica_luo_aut ve_grafica_luo_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_grafica_luo_aut.leggiAllParametro());
    }


	 /*
    <statement nome="selectPerLuogo" tipo="select" id="02_taymer">
            <fisso>
                WHERE
                lid = XXXlid
            </fisso>

    <filter name="VE_GRAFICA_LUO_AUT_selectPerLuogo"
            condition="lid = :XXXlid "/>


     * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_grafica_luo_aut> selectPerLuogo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_grafica_luo_aut.class);
			Filter filter = session.enableFilter("VE_GRAFICA_LUO_AUT_selectPerLuogo");


			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao.XXXlid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao.XXXlid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao.XXXlid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_grafica_luo_aut",
                    this.basicCriteria, session);

			List<Ve_grafica_luo_aut> result = this.basicCriteria.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}
	/*
    <statement nome="countPerLuogo" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM VE_grafica_LUO_AUT
                WHERE
                lid = XXXlid
            </fisso>
    <filter name="VE_GRAFICA_LUO_AUT_countPerLuogo"
            condition="lid = :XXXlid "/>

	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerLuogo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_grafica_luo_aut.class);
            Filter filter = session.enableFilter("VE_GRAFICA_LUO_AUT_countPerLuogo");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao.XXXlid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao.XXXlid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_grafica_luo_autCommonDao.XXXlid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_grafica_luo_aut",
                    this.basicCriteria, session);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.countDistinct("BID"))).uniqueResult();

			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

}
