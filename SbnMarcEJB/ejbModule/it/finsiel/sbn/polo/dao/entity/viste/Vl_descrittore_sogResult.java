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
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_sog;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_descrittore_sogResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao{

    public Vl_descrittore_sogResult(Vl_descrittore_sog vl_descrittore_sog) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_descrittore_sog.leggiAllParametro());
    }


     /*
    <statement nome="selectDescrittorePerSoggetto" tipo="select" id="Jenny_02">
        <fisso>
            WHERE
            cid = XXXcid
            AND fl_canc != 'S'
            ORDER BY ky_norm_descritt
        </fisso>

    <filter name="VL_DESCRITTORE_SOG_selectDescrittorePerSoggetto"
            condition="cid = :XXXcid
                       AND fl_canc != 'S' "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_descrittore_sog> selectDescrittorePerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_DESCRITTORE_SOG_selectDescrittorePerSoggetto");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXcid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXcid);

            // ORDINAMENTO LOCALE VIENE SETTATO QUI
            // CREATA AD HOC NEL FILE
            Query order = session.getNamedQuery(("VL_DESCRITTORE_SOG_KY_NORM_DESCRITT").toString());
            order.getQueryString();
            basicCriteria.add(Restrictions.sqlRestriction(order.getQueryString()));


			List<Vl_descrittore_sog> result = this.basicCriteria.list();
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
    <statement nome="selectLegameDescrittore" tipo="select" id="Jenny_03">
        <fisso>
            WHERE
            did = XXXdid
            AND fl_canc != 'S'
        </fisso>

       <filter name="VL_DESCRITTORE_SOG_selectLegameDescrittore"
               condition="did = :XXXdid
                          AND fl_canc != 'S' "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_descrittore_sog> selectLegameDescrittore(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_DESCRITTORE_SOG_selectLegameDescrittore");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXdid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXdid));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXdid);

           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_descrittore_sog",
                   this.basicCriteria, session);

            List<Vl_descrittore_sog> result = this.basicCriteria.list();
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
    <statement nome="countSoggettoPerDescrittore" tipo="count" id="Jenny_04">
            <fisso>
                SELECT COUNT (*) FROM Vl_descrittore_sog
                WHERE
                  did = XXXdid
                  AND fl_canc != 'S'
            </fisso>
    </statement>


    <filter name="VL_DESCRITTORE_SOG_countLegameDescrittore"
            condition="did = :XXXdid
                       AND fl_canc != 'S' "/>


	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countSoggettoPerDescrittore(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_DESCRITTORE_SOG_countLegameDescrittore");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXdid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXdid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXdid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_descrittore_sog",
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

	public List<Vl_descrittore_sog> selectDescrittoreAutomaticoPerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_DESCRITTORE_SOG_selectDescrittorePerSoggetto");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXcid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_descrittore_sogCommonDao.XXXcid);

            //non manuali
            basicCriteria.add(Restrictions.ne("FL_PRIMAVOCE", 'M'));
            basicCriteria.add(Restrictions.ne("FL_POSIZIONE", ResourceLoader.getPropertyInteger("POSIZIONE_DESCRITTORE_MANUALE")));

            // ORDINAMENTO LOCALE VIENE SETTATO QUI
            // CREATA AD HOC NEL FILE
            Query order = session.getNamedQuery(("VL_DESCRITTORE_SOG_KY_NORM_DESCRITT").toString());

            basicCriteria.add(Restrictions.sqlRestriction(order.getQueryString()));

			List<Vl_descrittore_sog> result = this.basicCriteria.list();
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
		return Vl_descrittore_sog.class;
	}

}
