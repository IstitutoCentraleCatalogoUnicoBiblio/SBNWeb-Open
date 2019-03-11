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
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_tit_c;

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
public class Vl_cartografia_tit_cResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao{

    public Vl_cartografia_tit_cResult(Vl_cartografia_tit_c vl_cartografia_tit_c) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_cartografia_tit_c.leggiAllParametro());
    }
     /*
    <statement nome="selectPerDocumento" tipo="select" id="2_taymer">
            <fisso>
                WHERE
                bid_coll = XXXbid_coll
            </fisso>

    <filter name="VL_CARTOGRAFIA_TIT_C_selectPerDocumento"
            condition="bid_coll = :XXXbid_coll "/>


	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_cartografia_tit_c> selectPerDocumento(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_cartografia_tit_c.class);
			Filter filter = session.enableFilter("VL_CARTOGRAFIA_TIT_C_selectPerDocumento");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao.XXXbid_coll,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao.XXXbid_coll));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao.XXXbid_coll);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_cartografia_tit_c",
                    this.basicCriteria, session);

			List<Vl_cartografia_tit_c> result = this.basicCriteria.list();
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
    <statement nome="countPerDocumento" tipo="count" id="3_taymer">
            <fisso>
            SELECT COUNT(*) FROM Vl_cartografia_tit_cCommonDao
                WHERE
                bid_coll = XXXbid_coll
            </fisso>

    <filter name="VL_CARTOGRAFIA_TIT_C_countPerDocumento"
            condition="bid_coll = :XXXbid_coll "/>

	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countPerDocumento(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_cartografia_tit_c.class);
            Filter filter = session.enableFilter("VL_CARTOGRAFIA_TIT_C_countPerDocumento");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao.XXXbid_coll,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao.XXXbid_coll));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_cartografia_tit_cCommonDao.XXXbid_coll);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_cartografia_tit_c", this.basicCriteria, session);


            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList()
                      .add( Projections.rowCount())).uniqueResult();

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
