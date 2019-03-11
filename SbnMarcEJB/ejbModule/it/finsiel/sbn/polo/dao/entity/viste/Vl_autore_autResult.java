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
import it.finsiel.sbn.polo.orm.viste.Vl_autore_aut;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_autore_autResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao{

    public Vl_autore_autResult(Vl_autore_aut vl_autore_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_autore_aut.leggiAllParametro());
    }
     /*
     <statement nome="selectAutorePerRinvii" tipo="select" id="01">
        <fisso>
            WHERE vid_1 = XXXvid_1
        </fisso>
     <filter name="VL_AUTORE_AUT_selectAutorePerRinvii" condition="vid_1 = :XXXvid_1 "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_autore_aut> selectAutorePerRinvii(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_AUTORE_AUT_selectAutorePerRinvii");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXvid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXvid_1));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXvid_1);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_aut",
                    this.basicCriteria, session);

			List<Vl_autore_aut> result = this.basicCriteria.list();
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
    <statement nome="selectAutorePerLegame" tipo="select" id="06_taymer">
        <fisso>
            WHERE vid_1 = XXXvid_1
            AND tp_legame = XXXtp_legame
            and fl_canc !='S'
        </fisso>
     <filter name="VL_AUTORE_AUT_selectAutorePerLegame" condition="vid_1 = :XXXvid_1
             AND tp_legame = :XXXtp_legame
             AND fl_canc !='S' "/>


	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public List<Vl_autore_aut> selectAutorePerLegame(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session
                    .enableFilter("VL_AUTORE_AUT_selectAutorePerLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXvid_1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXvid_1));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXtp_legame,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXtp_legame));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXvid_1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_autCommonDao.XXXtp_legame);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_autore_aut", this.basicCriteria, session);


            List<Vl_autore_aut> result = this.basicCriteria
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

    @Override
    public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_autore_aut.class;
	}

}
