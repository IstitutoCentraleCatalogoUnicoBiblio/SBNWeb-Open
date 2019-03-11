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
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_aut;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_titolo_autResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_autCommonDao{

    public Vl_titolo_autResult(Vl_titolo_aut vl_titolo_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_titolo_aut.leggiAllParametro());
    }
     /*
     <statement nome="selectPerAutore" tipo="select_campi" id="02">
        <fisso>
        FROM Vl_titolo_autCommonDao
        WHERE
        vid = XXXvid
        </fisso>
     <filter name="VL_TITOLO_AUT_selectPerAutore" condition="vid = :XXXvid "/>


	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_titolo_aut> selectPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			//almaviva5_20150903 #5965
            this.basicCriteria.add(Restrictions.eq("VID", myOpzioni.get(KeyParameter.XXXvid)));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_autCommonDao.XXXvid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_aut",
                    this.basicCriteria, session);

			List<Vl_titolo_aut> result = this.basicCriteria.list();
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
    <statement nome="countPerAutore" tipo="count" id="03">
            <fisso>
                SELECT COUNT(distinct (bid)) FROM Vl_titolo_aut
                WHERE
                vid = XXXvid
            </fisso>

     <filter name="VL_TITOLO_AUT_countPerAutore" condition="vid = :XXXvid "/>

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
            this.basicCriteria = session.createCriteria(getTarget());

            //almaviva5_20150903 #5965
            this.basicCriteria.add(Restrictions.eq("VID", myOpzioni.get(KeyParameter.XXXvid)));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_autCommonDao.XXXvid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_aut",
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
		return Vl_titolo_aut.class;
	}


}
