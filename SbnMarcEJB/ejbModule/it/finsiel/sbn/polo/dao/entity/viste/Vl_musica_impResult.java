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
import it.finsiel.sbn.polo.orm.viste.Vl_musica_imp;

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
public class Vl_musica_impResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_musica_impCommonDao{

    public Vl_musica_impResult(Vl_musica_imp vl_musica_imp) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_musica_imp.leggiAllParametro());
    }
     /*
    <statement nome="selectPerImpronta" tipo="select" id="02_taymer">
            <fisso>
                WHERE
             fl_canc != 'S'
            </fisso>

    <filter name="VL_MUSICA_IMP_selectPerImpronta"
            condition="fl_canc != 'S' "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_musica_imp> selectPerImpronta(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_musica_imp.class);
			Filter filter = session.enableFilter("VL_MUSICA_IMP_selectPerImpronta");


            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_musica_imp",
                    this.basicCriteria, session);

			List<Vl_musica_imp> result = this.basicCriteria.list();
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
    <statement nome="countPerImpronta" tipo="count" id="03_taymer">
            <fisso>
            SELECT COUNT(*) FROM Vl_titolo_impCommonDao
                WHERE
                 fl_canc != 'S'
            </fisso>
    <filter name="VL_MUSICA_IMP_countPerImpronta"
            condition="fl_canc != 'S' "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countPerImpronta(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_musica_imp.class);
            Filter filter = session.enableFilter("VL_MUSICA_IMP_countPerImpronta");


            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_musica_imp", this.basicCriteria, session);


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
