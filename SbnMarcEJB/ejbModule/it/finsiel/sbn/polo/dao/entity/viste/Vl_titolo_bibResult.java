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
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_bib;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_titolo_bibResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_bibCommonDao{

    public Vl_titolo_bibResult(Vl_titolo_bib vl_titolo_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_titolo_bib.leggiAllParametro());
    }
     /*
    <statement nome="selectPerPolo" tipo="select" id="17_william">
        <fisso>
            WHERE fl_canc != 'S'
            and cd_polo = XXXcd_polo
        </fisso>

     <filter name="VL_TITOLO_BIB_selectPerPolo" condition="fl_canc != 'S' AND cd_polo = :XXXcd_polo"/>



	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_titolo_bib> selectPerPolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_TITOLO_BIB_selectPerPolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_bibCommonDao.XXXcd_polo));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_bibCommonDao.XXXcd_polo);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_bib",
                    this.basicCriteria, session);

			List<Vl_titolo_bib> result = this.basicCriteria.list();
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
    public void selectRidottoOpac() {
        // TODO Auto-generated method stub

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_titolo_bib.class;
	}


}
