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
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_aut_nstd;

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
public class Ve_titolo_aut_nstdResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao{

    public Ve_titolo_aut_nstdResult(Ve_titolo_aut_nstd ve_titolo_aut_nstd) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_titolo_aut_nstd.leggiAllParametro());
    }



	/*
    <statement nome="selectPerNumero" tipo="select" id="02_taymer">
            <fisso>
            WHERE
                tp_numero_std = XXXtp_numero_std
        </fisso>

    <filter name="VE_TITOLO_AUT_NSTD_selectPerNumero"
            condition="tp_numero_std = :XXXtp_numero_std "/>

     *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_titolo_aut_nstd> selectPerNumero(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_titolo_aut_nstd.class);
			Filter filter = session.enableFilter("VE_TITOLO_AUT_NSTD_selectPerNumero");


			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao.XXXtp_numero_std,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao.XXXtp_numero_std));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao.XXXtp_numero_std);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_aut_nstd",
                    this.basicCriteria, session);

            List<Ve_titolo_aut_nstd> result = this.basicCriteria
					.list();
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
    <statement nome="countPerNumero" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM Ve_titolo_aut_nstdCommonDao
            WHERE
                tp_numero_std = XXXtp_numero_std
        </fisso>

    <filter name="VE_TITOLO_AUT_NSTD_countPerNumero"
            condition="tp_numero_std = :XXXtp_numero_std "/>

	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerNumero(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_titolo_aut_nstd.class);
            Filter filter = session.enableFilter("VE_TITOLO_AUT_NSTD_countPerNumero");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao.XXXtp_numero_std,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao.XXXtp_numero_std));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_aut_nstdCommonDao.XXXtp_numero_std);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_aut_nstd",
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
