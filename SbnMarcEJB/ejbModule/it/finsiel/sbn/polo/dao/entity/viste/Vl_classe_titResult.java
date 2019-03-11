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
import it.finsiel.sbn.polo.orm.viste.Vl_classe_tit;

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
public class Vl_classe_titResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao{

    public Vl_classe_titResult(Vl_classe_tit vl_classe_tit) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_classe_tit.leggiAllParametro());
    }
     /*
    <statement nome="selectClassePerTitolo" tipo="select" id="02">
            <fisso>
                WHERE
                bid = XXXbid
            </fisso>

    <filter name="VL_CLASSE_TIT_selectClassePerTitolo"
            condition="bid = :XXXbid "/>


	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_classe_tit> selectClassePerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_CLASSE_TIT_selectClassePerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_classe_tit",
                    this.basicCriteria, session);

			List<Vl_classe_tit> result = this.basicCriteria.list();
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
    <statement nome="selectClassePerTitolo" tipo="select" id="02">
            <fisso>
                WHERE
                bid = XXXbid
                sistema = sistema
                edizione = edizione
            </fisso>

    <filter name="VL_CLASSE_TIT_selectClassePerTitolo"
            condition="bid = :XXXbid
                       sistema = :XXXsistema
                       edizione = :XXXedizione
                       "/>


     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_classe_tit> selectClassePerTitoloSistemaEdizione(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_CLASSE_TIT_selectClassePerTitolo");



            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid));
          //almaviva5_20111114 #4694
//            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_sistema,
//                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_sistema));
//            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_edizione,
//                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_edizione));

            this.createCriteria(myOpzioni);

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_sistema);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_edizione);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_classe_tit",
                    this.basicCriteria, session);

            List<Vl_classe_tit> result = this.basicCriteria.list();
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
    public Integer countClassePerTitoloSistemaEdizione(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_CLASSE_TIT_selectClassePerTitolo");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid));
          //almaviva5_20111114 #4694
//            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_sistema,
//                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_sistema));
//            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_edizione,
//                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_edizione));

            this.createCriteria(myOpzioni);


            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_sistema);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXcd_edizione);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_classe_tit", this.basicCriteria, session);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(
                            Projections.countDistinct("BID"))).uniqueResult();

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


    public Integer countClassePerTitolo(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session
                    .enableFilter("VL_CLASSE_TIT_countClassePerTitolo");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid,
                            opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_classe_titCommonDao.XXXbid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_classe_tit", this.basicCriteria, session);

            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList().add(
                            Projections.countDistinct("BID"))).uniqueResult();

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
		return Vl_classe_tit.class;
	}

}
