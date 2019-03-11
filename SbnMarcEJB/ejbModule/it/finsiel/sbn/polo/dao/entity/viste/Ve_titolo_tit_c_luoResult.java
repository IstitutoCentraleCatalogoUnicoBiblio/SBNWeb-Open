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
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_tit_c_luo;

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
public class Ve_titolo_tit_c_luoResult extends it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao{

    public Ve_titolo_tit_c_luoResult(Ve_titolo_tit_c_luo ve_titolo_tit_c_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(ve_titolo_tit_c_luo.leggiAllParametro());
    }
     /*
    <statement nome="selectPerTitolo" tipo="select" id="02_taymer">
            <fisso>
                WHERE
                bid = XXXbid
            </fisso>
    <filter name="VE_TITOLO_TIT_C_LUO_selectPerTitolo"
            condition="bid = :XXXbid "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ve_titolo_tit_c_luo> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VE_TITOLO_TIT_C_LUO_selectPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_tit_c_luo",
                    this.basicCriteria, session);

			List<Ve_titolo_tit_c_luo> result = this.basicCriteria.list();
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

	//  Inizio modifica almaviva2 28.09.2010 bug mantis 3749 inserita nuova select VE_TITOLO_TIT_C_LUO_selectPerTitolo_Coll
	//  corretta per bid_coll e non per bid -->

	public List<Ve_titolo_tit_c_luo> selectPerTitolo_Coll(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VE_TITOLO_TIT_C_LUO_selectPerTitolo_Coll");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid,
		            opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid));

		    myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid);

		    this.createCriteria(myOpzioni);
		    this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
		            "Ve_titolo_tit_c_luo",
		            this.basicCriteria, session);

			List<Ve_titolo_tit_c_luo> result = this.basicCriteria.list();
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
	// Fine modifica almaviva2 28.09.2010 bug mantis 3749

	/*
    <statement nome="countPerTitolo" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT (distinct (bid) ) FROM Ve_titolo_tit_c_luo
                WHERE
                bid = XXXbid
            </fisso>
    <filter name="VE_TITOLO_TIT_C_LUO_countPerTitolo"
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
			this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VE_TITOLO_TIT_C_LUO_countPerTitolo");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ve_titolo_tit_c_luo",
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


	//  Inizio modifica almaviva2 28.09.2010 bug mantis 3749 inserita nuova select VE_TITOLO_TIT_C_LUO_countPerTitolo_Coll
	//  corretta per bid_coll e non per bid -->
	public Integer countPerTitolo_Coll(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
		    Filter filter = session.enableFilter("VE_TITOLO_TIT_C_LUO_countPerTitolo_Coll");


		    filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid,
		            opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid));


		    myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Ve_titolo_tit_c_luoCommonDao.XXXbid);

		    this.createCriteria(myOpzioni);
		    this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
		            "Ve_titolo_tit_c_luo",
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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Ve_titolo_tit_c_luo.class;
	}

}
