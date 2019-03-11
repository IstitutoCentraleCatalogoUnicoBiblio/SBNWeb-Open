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
import it.finsiel.sbn.polo.orm.viste.Vl_marca_tit;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_marca_titResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_marca_titCommonDao{

    public Vl_marca_titResult(Vl_marca_tit vl_marca_tit) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_marca_tit.leggiAllParametro());
    }

     /*
    <statement nome="selectMarcaPerTitolo" tipo="select" id="02">
            <fisso>
                WHERE
                bid = XXXbid
            </fisso>
    </statement>

    <filter name="VL_MARCA_TIT_selectMarcaPerTitolo"
            condition="bid = :XXXbid "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_marca_tit> selectMarcaPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_MARCA_TIT_selectMarcaPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_titCommonDao.XXXbid);

            //this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_marca_tit",
                    this.basicCriteria, session);

			List<Vl_marca_tit> result = this.basicCriteria.list();
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
		return Vl_marca_tit.class;
	}

}
