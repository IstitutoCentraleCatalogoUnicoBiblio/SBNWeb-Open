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
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_titolo_tit_cResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao{

    public Vl_titolo_tit_cResult(Vl_titolo_tit_c vl_titolo_tit_c) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_titolo_tit_c.leggiAllParametro());
    }


     /*
    <statement nome="selectLegamiDiMonografie" tipo="select" id="11_taymer">
            <fisso>
                WHERE
                fl_canc != 'S'
                AND
                tp_legame = '01'
                AND
                bid_coll = XXXbid_coll
            </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_C_selectLegamiDiMonografie"
                condition="fl_canc != 'S'
                            AND
                            tp_legame = '01'
                            AND
                            bid_coll = :XXXbid_coll "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_titolo_tit_c> selectLegamiDiMonografie(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_TITOLO_TIT_C_selectLegamiDiMonografie");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_c",
                    this.basicCriteria, session);

			List<Vl_titolo_tit_c> result = this.basicCriteria.list();
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
    <statement nome="selectPerDocumento" tipo="select" id="12_taymer">
            <fisso>
                WHERE
                fl_canc != 'S'
                AND
                bid_coll = XXXbid_coll
            </fisso>
    <filter name="VL_TITOLO_TIT_C_selectPerDocumento"
            condition="fl_canc != 'S'
                        AND
                        bid_coll = :XXXbid_coll "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_c> selectPerDocumento(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_C_selectPerDocumento");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_titolo_tit_c",
                   this.basicCriteria, session);

            List<Vl_titolo_tit_c> result = this.basicCriteria.list();
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
    <statement nome="countPerDocumento" tipo="count" id="13_taymer">
            <fisso>
            SELECT COUNT(*) FROM Vl_titolo_tit_cCommonDao
                WHERE
                fl_canc != 'S'
                AND
                bid_coll = XXXbid_coll
            </fisso>
    <filter name="VL_TITOLO_TIT_C_countPerDocumento"
            condition="fl_canc != 'S'
                        AND
                        bid_coll = :XXXbid_coll "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerDocumento(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_C_countPerDocumento");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_c", this.basicCriteria, session);


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


    /*
    <statement nome="selectPerBidCollENaturaBase" tipo="select" id="15_taymer">
            <fisso>
                WHERE  bid_coll= XXXbid_coll
                AND cd_natura_base = XXXcd_natura_base
            </fisso>
    </statement>

    <filter name="VL_TITOLO_TIT_C_selectPerBidCollENaturaBase"
            condition="bid_coll= :XXXbid_coll
                        AND cd_natura_base = :XXXcd_natura_base "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_c> selectPerBidCollENaturaBase(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_C_selectPerBidCollENaturaBase");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXcd_natura_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXcd_natura_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXbid_coll);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_cCommonDao.XXXcd_natura_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_c",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_c> result = this.basicCriteria.list();
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
		return Vl_titolo_tit_c.class;
	}


	 /*

     *      <statement nome="selectRinviiV" prepared="true" tipo="select" id="11_taymer">
                  <fisso>
                        WHERE bid_coll = XXXbid_coll
                        AND  cd_natura_coll = 'V'
                        AND  fl_canc != 'S'
                  </fisso>
      </statement>

     */



    public List<Vl_titolo_tit_c> selectRinviiV(HashMap opzioni)

    throws InfrastructureException {

        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            basicCriteria.add(Restrictions.ne("FL_CANC",  'S'));
            basicCriteria.add(Restrictions.eq("BID_COLL", opzioni.get(KeyParameter.XXXbid_coll)));
            basicCriteria.add(Restrictions.eq("CD_NATURA_COLL", 'V'));

            myOpzioni.remove(KeyParameter.XXXbid_coll);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_c", this.basicCriteria, session);

            List<Vl_titolo_tit_c> result = new ArrayList<Vl_titolo_tit_c>(this.basicCriteria.list());
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
