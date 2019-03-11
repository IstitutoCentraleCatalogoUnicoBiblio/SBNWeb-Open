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
import it.finsiel.sbn.polo.orm.viste.Vl_marca_aut;

import java.util.ArrayList;
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
public class Vl_marca_autResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao{

    public Vl_marca_autResult(Vl_marca_aut vl_marca_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_marca_aut.leggiAllParametro());
    }

     /*
    <statement nome="selectPerKey" tipo="select" id="01">
            <fisso>
                WHERE
                fl_canc != 'S'
            </fisso>
    </statement>
    <filter name="VL_MARCA_AUT_selectPerKey"
            condition="fl_canc != 'S' "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_marca_aut> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_MARCA_AUT_selectPerKey");


            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_marca_aut",
                    this.basicCriteria, session);

			List<Vl_marca_aut> result = new ArrayList<Vl_marca_aut>(this.basicCriteria.list());
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
    <statement nome="selectPerMid" tipo="select" id="01">
            <fisso>
                WHERE
                    mid = XXXmid
                AND fl_canc != 'S'
            </fisso>
    </statement>

    <filter name="VL_MARCA_AUT_selectPerMid"
            condition="mid = :XXXmid
                       AND fl_canc != 'S' "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_aut> selectPerMid(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_MARCA_AUT_selectPerMid");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXmid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXmid));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXmid);


           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_aut",
                   this.basicCriteria, session);

            List<Vl_marca_aut> result = new ArrayList<Vl_marca_aut>(this.basicCriteria.list());
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
    <statement nome="selectPerVid" tipo="select" id="01">
            <fisso>
                WHERE
                    vid = XXXvid
                AND fl_canc != 'S'
            </fisso>
    </statement>

    <filter name="VL_MARCA_AUT_selectPerVid"
            condition="vid = :XXXvid
                       AND fl_canc != 'S' "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_aut> selectPerVid(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_MARCA_AUT_selectPerVid");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXvid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXvid));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXvid);


           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_aut",
                   this.basicCriteria, session);

            List<Vl_marca_aut> result = new ArrayList<Vl_marca_aut>(this.basicCriteria.list());
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
    <statement nome="countAutorePerLegameVid" tipo="count" id="02_william">
        <fisso>
            SELECT COUNT (*) FROM vl_marca_aut
            WHERE vid=XXXvid
            AND fl_canc != 'S'
        </fisso>
    </statement>
    <filter name="VL_MARCA_AUT_countAutorePerLegameVid"
            condition="vid=:XXXvid
                       AND fl_canc != 'S' "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countAutorePerLegameVid(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_MARCA_AUT_countAutorePerLegameVid");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXvid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_autCommonDao.XXXvid);


            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_marca_aut",
                    this.basicCriteria, session);


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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_marca_aut.class;
	}


}
