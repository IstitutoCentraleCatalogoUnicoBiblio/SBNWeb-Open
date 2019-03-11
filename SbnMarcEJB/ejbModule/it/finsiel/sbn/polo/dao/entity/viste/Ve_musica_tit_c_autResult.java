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
import it.finsiel.sbn.polo.orm.viste.Ve_musica_tit_c_aut;

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
public class Ve_musica_tit_c_autResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao{

    public Ve_musica_tit_c_autResult(Ve_musica_tit_c_aut ve_musica_tit_c_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_musica_tit_c_aut.leggiAllParametro());
    }
     /*
    <statement nome="selectPerTitolo" tipo="select" id="02_taymer">
            <fisso>
                WHERE
                bid = XXXbid
            </fisso>
    <filter name="VE_MUSICA_TIT_C_AUT_selectPerTitolo"
            condition="bid = :XXXbid "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_musica_tit_c_aut> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_musica_tit_c_aut.class);
			Filter filter = session.enableFilter("VE_GRAFICA_TIT_C_AUT_selectPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_grafica_tit_c_aut",
                    this.basicCriteria, session);

			List<Ve_musica_tit_c_aut> result = this.basicCriteria.list();
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
    <statement nome="countPerTitolo" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM Ve_musica_tit_c_aut
                WHERE
                bid = XXXbid
            </fisso>
    <filter name="VE_MUSICA_TIT_C_AUT_countPerTitolo"
            condition="bid = :XXXbid "/>

	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Ve_musica_tit_c_aut.class);
            Filter filter = session.enableFilter("VE_GRAFICA_TIT_C_AUT_countPerTitolo");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao.XXXbid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_musica_tit_c_autCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_grafica_tit_c_aut",
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
