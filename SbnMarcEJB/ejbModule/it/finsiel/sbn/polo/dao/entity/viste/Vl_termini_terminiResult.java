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
import it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.viste.Vl_termini_termini;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_termini_terminiResult extends Vl_termini_terminiCommonDao{

    public Vl_termini_terminiResult(Vl_termini_termini  vl_termini_termini) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_termini_termini.leggiAllParametro());
    }
     /*
    <statement nome="selectTermniniPerRinvii" tipo="select" id="jenny02">
            <fisso>
                WHERE did_1 = XXXdid_1
            </fisso>
    </statement>
    <filter name="VL_TERMINI_TERMINI_selectDescrittorePerRinvii"
            condition="did_1 = :XXXdid_1 "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_termini_termini> selectTerminiPerRinvii(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_termini_termini.class);
			Filter filter = session.enableFilter("VL_TERMINI_TERMINI_selectTerminiPerRinvii");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_termini_termini",
                    this.basicCriteria, session);

			List<Vl_termini_termini> result = this.basicCriteria.list();
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
    <statement nome="selectLegamiPerTipo" tipo="select" id="Jenny_03">
            <fisso>
                WHERE did_1 = XXXdid_1
                AND tp_legame = XXXtp_legame
            </fisso>
    </statement>

    <filter name="VL_TERMINI_TERMINI_selectLegamiPerTipo"
            condition="did_1 = :XXXdid_1
                       AND tp_legame = :XXXtp_legame "/>


	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
     */
    public List<Vl_termini_termini> selectLegamiPerTipo(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_termini_termini.class);
            Filter filter = session.enableFilter("VL_TERMINI_TERMINI_selectLegamiPerTipo");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXtipo_coll,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXtipo_coll));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXtipo_coll);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_termini_termini",
                    this.basicCriteria, session);

            List<Vl_termini_termini> result = this.basicCriteria.list();
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

	/**
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_termini_termini> cercaLegamiTerminiTermini(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_termini_termini.class);
			Filter filter = session	.enableFilter("VL_TERMINI_TERMINI_cercaLegamiTerminiTermini");
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_termini_terminiCommonDao.XXXdid_1);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_termini_termini",
                    this.basicCriteria, session);

			List<Vl_termini_termini> result = this.basicCriteria
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


}
