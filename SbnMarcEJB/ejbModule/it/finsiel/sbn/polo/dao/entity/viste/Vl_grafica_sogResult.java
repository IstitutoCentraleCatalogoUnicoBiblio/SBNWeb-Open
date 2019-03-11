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
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_sog;

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
public class Vl_grafica_sogResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao{

    public Vl_grafica_sogResult(Vl_grafica_sog vl_grafica_sog) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_grafica_sog.leggiAllParametro());
    }
     /*
    <statement nome="selectPerSoggetto" tipo="select" id="02">
            <fisso>
                WHERE
                cid = XXXcid
            </fisso>
    <filter name="VL_GRAFICA_SOG_selectPerSoggetto"
            condition="cid = :XXXcid "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_grafica_sog> selectPerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_grafica_sog.class);
			Filter filter = session.enableFilter("VL_GRAFICA_SOG_selectPerSoggetto");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao.XXXcid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao.XXXcid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_grafica_sog",
                    this.basicCriteria, session);

			List<Vl_grafica_sog> result = this.basicCriteria.list();
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
    <statement nome="countPerSoggetto" tipo="count" id="03">
            <fisso>
                SELECT COUNT(*) FROM Vl_grafica_sog
                WHERE
                cid = XXXcid
            </fisso>
    <filter name="VL_GRAFICA_SOG_countPerSoggetto"
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
            this.basicCriteria = session.createCriteria(Vl_grafica_sog.class);
            Filter filter = session.enableFilter("VL_GRAFICA_SOG_countPerSoggetto");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao.XXXcid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_grafica_sogCommonDao.XXXcid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_grafica_sog", this.basicCriteria, session);


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
