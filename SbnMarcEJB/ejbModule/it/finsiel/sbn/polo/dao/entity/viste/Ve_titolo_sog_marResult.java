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
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_sog_mar;

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
public class Ve_titolo_sog_marResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao{

    public Ve_titolo_sog_marResult(Ve_titolo_sog_mar ve_titolo_sog_mar) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_titolo_sog_mar.leggiAllParametro());
    }
     /*
    <statement nome="selectPerSoggetto" tipo="select" id="02_taymer">
            <fisso>
                WHERE
                cid = XXXcid
            </fisso>
    <filter name="VE_TITOLO_SOG_MAR_selectPerSoggetto"
            condition="cid = :XXXcid "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_titolo_sog_mar> selectPerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_titolo_sog_mar.class);
			Filter filter = session.enableFilter("VE_TITOLO_SOG_MAR_selectPerSoggetto");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao.XXXcid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao.XXXcid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_sog_mar",
                    this.basicCriteria, session);

			List<Ve_titolo_sog_mar> result = this.basicCriteria.list();
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
    <statement nome="countPerSoggetto" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM Ve_titolo_sog_mar
                WHERE
                cid = XXXcid
            </fisso>
    <filter name="VE_TITOLO_SOG_MAR_countPerSoggetto"
            condition="cid = :XXXcid "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_titolo_sog_mar.class);
            Filter filter = session.enableFilter("VE_TITOLO_SOG_MAR_countPerSoggetto");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao.XXXcid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_sog_marCommonDao.XXXcid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_sog_mar",
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
