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
import it.finsiel.sbn.polo.orm.viste.Ve_musica_aut_luo;

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
public class Ve_musica_aut_luoResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao{

    public Ve_musica_aut_luoResult(Ve_musica_aut_luo ve_musica_aut_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_musica_aut_luo.leggiAllParametro());
    }



	/*


    <statement nome="selectPerAutore" tipo="select" id="02_taymer">
            <fisso>
                WHERE
                vid = XXXvid
            </fisso>
    <filter name="VE_MUSICA_AUT_LUO_selectPerAutore"
            condition="vid = :XXXvid "/>

     *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_musica_aut_luo> selectPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_musica_aut_luo.class);
			Filter filter = session.enableFilter("VE_MUSICA_AUT_LUO_selectPerAutore");


			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao.XXXvid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao.XXXvid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_musica_aut_luo",
                    this.basicCriteria, session);

			List<Ve_musica_aut_luo> result = this.basicCriteria
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
    <statement nome="countPerAutore" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM Ve_titolo_aut_luo
                WHERE
                vid = XXXvid
            </fisso>

    <filter name="VE_MUSICA_AUT_LUO_countPerAutore"
            condition="vid = :XXXvid "/>


	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_musica_aut_luo.class);
            Filter filter = session.enableFilter("VE_MUSICA_AUT_LUO_countPerAutore");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao.XXXvid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_aut_luoCommonDao.XXXvid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_musica_aut_luo",
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
