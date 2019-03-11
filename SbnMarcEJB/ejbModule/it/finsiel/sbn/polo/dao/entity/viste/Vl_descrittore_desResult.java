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
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_des;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_descrittore_desResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao{

    public Vl_descrittore_desResult(Vl_descrittore_des vl_descrittore_des) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_descrittore_des.leggiAllParametro());
    }
     /*
    <statement nome="selectDescrittorePerRinvii" tipo="select" id="jenny02">
            <fisso>
                WHERE did_1 = XXXdid_1
            </fisso>
    </statement>
    <filter name="VL_DESCRITTORE_DES_selectDescrittorePerRinvii"
            condition="did_1 = :XXXdid_1 "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_descrittore_des> selectDescrittorePerRinvii(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_descrittore_des.class);
			Filter filter = session.enableFilter("VL_DESCRITTORE_DES_selectDescrittorePerRinvii");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_descrittore_des",
                    this.basicCriteria, session);

			List<Vl_descrittore_des> result = this.basicCriteria.list();
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

    <filter name="VL_DESCRITTORE_DES_selectLegamiPerTipo"
            condition="did_1 = :XXXdid_1
                       AND tp_legame = :XXXtp_legame "/>


	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
     */
    public List<Vl_descrittore_des> selectLegamiPerTipo(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_descrittore_des.class);
            Filter filter = session.enableFilter("VL_DESCRITTORE_DES_selectLegamiPerTipo");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXtp_legame,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXtp_legame));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXtp_legame);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_descrittore_des",
                    this.basicCriteria, session);

            List<Vl_descrittore_des> result = this.basicCriteria.list();
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
	 *
	 *
	<statement nome="cercaLegamiDescrittoreDescrittore" tipo="select" id="Jenny_09">
			<fisso>
				WHERE
				  ( did_base = XXXdid_base
				  OR did_coll = XXXdid_coll)
				OR ( did_base = XXXdid_coll
				  AND did_coll = XXXdid_base)
			</fisso>
	</statement>

//
//SELECT * FROM VL_DESCRITTORE_DES WHERE ( did = 'CSWD000363'
//				    AND TP_FORMA_DES ='R')
//				OR ( did = 'CSWD000359'
//				  and did_1 = 'CSWD000363');


	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_descrittore_des> cercaLegamiDescrittoreDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_descrittore_des.class);
			Filter filter = session.enableFilter("VL_DESCRITTORE_DES_cercaLegamiDescrittoreDescrittore");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_desCommonDao.XXXdid_1);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_descrittore_des",
                    this.basicCriteria, session);

			List<Vl_descrittore_des> result = this.basicCriteria
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


	public List<Vl_descrittore_des> cercaDescrittorePerLegami(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_descrittore_des.class);
			Filter filter = session.enableFilter("VL_DESCRITTORE_DES_cercaDescrittorePerLegami");

			filter.setParameter(XXXdid_1, opzioni.get(XXXdid_1));

			myOpzioni.remove(XXXdid_1);
			this.basicCriteria = Parameter.setOrdinamento(myOpzioni, "Vl_descrittore_des", this.basicCriteria, session);

			List<Vl_descrittore_des> result = this.basicCriteria.list();
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
