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
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_the_luo;

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
public class Ve_titolo_the_luoResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao{

    public Ve_titolo_the_luoResult(Ve_titolo_the_luo ve_titolo_the_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_titolo_the_luo.leggiAllParametro());
    }
     /*
    <statement nome="selectPerThesauro" tipo="select" id="02_taymer">
            <fisso>
                WHERE
                did = XXXdid
            </fisso>
    <filter name="VE_TITOLO_THE_LUO_selectPerThesauro"
            condition="did = :XXXdid "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_titolo_the_luo> selectPerThesauro(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_titolo_the_luo.class);
			Filter filter = session.enableFilter("VE_TITOLO_THE_LUO_selectPerThesauro");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao.XXXdid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao.XXXdid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao.XXXdid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_the_luo",
                    this.basicCriteria, session);

			List<Ve_titolo_the_luo> result = this.basicCriteria.list();
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
    <statement nome="countPerThesauro" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM Ve_titolo_the_luo
                WHERE
                did = XXXdid
            </fisso>
    <filter name="VE_TITOLO_THE_LUO_countPerThesauro"
            condition="did = :XXXdid "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerThesauro(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_titolo_the_luo.class);
            Filter filter = session.enableFilter("VE_TITOLO_THE_LUO_countPerThesauro");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao.XXXdid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao.XXXdid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_the_luoCommonDao.XXXdid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_the_luo",
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
