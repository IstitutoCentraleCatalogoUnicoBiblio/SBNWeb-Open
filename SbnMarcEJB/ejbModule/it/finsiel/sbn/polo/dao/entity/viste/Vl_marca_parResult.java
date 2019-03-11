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
import it.finsiel.sbn.polo.orm.viste.Vl_marca_par;

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
public class Vl_marca_parResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao{

    public Vl_marca_parResult(Vl_marca_par vl_marca_par) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_marca_par.leggiAllParametro());
    }

     /*
     <statement nome="selectPerParola" tipo="select" id="jenny_02">
            <fisso>
                WHERE
                    parola = XXXparola
            </fisso>
     </statement>

     <filter name="VL_MARCA_PAR_selectPerParola"
             condition="parola = :XXXparola "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_marca_par> selectPerParola(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_marca_par.class);
			Filter filter = session.enableFilter("VL_MARCA_PAR_selectPerParola");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_marca_par",
                    this.basicCriteria, session);

			List<Vl_marca_par> result = this.basicCriteria.list();
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
    <statement nome="selectParolaMarca" tipo="select" id="jenny_03">
            <fisso>
                WHERE
                    parola = XXXparola
                    AND mid = XXXmid
            </fisso>
    </statement>

    <filter name="VL_MARCA_PAR_selectPerParolaMarca"
            condition="parola = :XXXparola
                       AND mid = :XXXmid "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_par> selectParolaMarca(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_marca_par.class);
            Filter filter = session.enableFilter("VL_MARCA_PAR_selectPerParolaMarca");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXmid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXmid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXmid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_par",
                   this.basicCriteria, session);

            List<Vl_marca_par> result = this.basicCriteria.list();
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
    <statement nome="selectPerParole" tipo="select" id="04_taymer">
            <fisso>
            WHERE fl_canc != 'S' AND
            parola = XXXparola
            </fisso>

    <filter name="VL_MARCA_PAR_selectPerParole"
            condition="fl_canc != 'S' AND
                       parola = :XXXparola "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_par> selectPerParole(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_marca_par.class);
            Filter filter = session.enableFilter("VL_MARCA_PAR_selectPerParole");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_par",
                   this.basicCriteria, session);

            List<Vl_marca_par> result = this.basicCriteria.list();
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
    <statement nome="countPerParole" tipo="count" id="05_taymer">
            <fisso>
            SELECT COUNT(*) FROM Vl_marca_par
            WHERE fl_canc != 'S' AND
            parola = XXXparola
            </fisso>
    <filter name="VL_MARCA_PAR_countPerParole"
            condition="fl_canc != 'S' AND
                       parola = :XXXparola "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countPerParole(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_marca_par.class);
            Filter filter = session.enableFilter("VL_MARCA_PAR_countPerParole");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola));


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_parCommonDao.XXXparola);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_marca_par", this.basicCriteria, session);


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
