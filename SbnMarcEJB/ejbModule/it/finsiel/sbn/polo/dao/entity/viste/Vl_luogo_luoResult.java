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
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_luo;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_luogo_luoResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao{

    public Vl_luogo_luoResult(Vl_luogo_luo vl_luogo_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_luogo_luo.leggiAllParametro());
    }

     /*
    <statement nome="selectLuogoPerRinvii" tipo="select" id="jenny02">
            <fisso>
                WHERE lid_1 = XXXlid_1
            </fisso>
    </statement>

    <filter name="VL_LUOGO_LUO_selectLuogoPerRinvii"
            condition="lid_1 = :XXXlid_1 "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_luogo_luo> selectLuogoPerRinvii(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_luogo_luo.class);
			Filter filter = session.enableFilter("VL_LUOGO_LUO_selectLuogoPerRinvii");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_luogo_luo",
                    this.basicCriteria, session);

			List<Vl_luogo_luo> result = this.basicCriteria.list();
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
    <statement nome="selectLuogoPerAltriRinvii" tipo="select" id="jenny03">
            <fisso>
                WHERE lid_1 = XXXlid_1
                    AND lid != XXXlid
            </fisso>
    </statement>

    <filter name="VL_LUOGO_LUO_selectLuogoPerAltriRinvii"
            condition="lid_1 = :XXXlid_1
                       AND lid != :XXXlid "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_luogo_luo> selectLuogoPerAltriRinvii(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_luogo_luo.class);
            Filter filter = session.enableFilter("VL_LUOGO_LUO_selectLuogoPerAltriRinvii");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1));

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1);
           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid);

           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_luogo_luo",
                   this.basicCriteria, session);

            List<Vl_luogo_luo> result = this.basicCriteria.list();
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
    <statement nome="selectPerLid" tipo="select_campi" id="04_william">
        <fisso>
            SELECT ds_luogo FROM vl_luogo_luo
            WHERE fl_canc != 'S'
            and lid_1 = XXXlid_1
        </fisso>

    <filter name="VL_LUOGO_LUO_selectPerLid"
            condition="fl_canc != 'S'
                       AND lid_1 = :XXXlid_1 "/>


     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_luogo_luo> selectPerLid(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_luogo_luo.class);
            Filter filter = session.enableFilter("VL_LUOGO_LUO_selectPerLid");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_luogo_luoCommonDao.XXXlid_1);

           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_luogo_luo",
                   this.basicCriteria, session);

            List<Vl_luogo_luo> result = this.basicCriteria.list();
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
