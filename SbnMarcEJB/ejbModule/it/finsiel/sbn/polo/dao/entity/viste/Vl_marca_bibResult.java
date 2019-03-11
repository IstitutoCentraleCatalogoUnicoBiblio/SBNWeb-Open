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
import it.finsiel.sbn.polo.orm.viste.Vl_marca_bib;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_marca_bibResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao{

    public Vl_marca_bibResult(Vl_marca_bib vl_marca_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_marca_bib.leggiAllParametro());
    }

     /*
    <statement nome="selectPerPolo" tipo="select" id="01">
            <fisso>
                WHERE
                  mid = XXXmid
                  AND cd_polo = XXXcd_polo
            </fisso>

        <filter name="VL_MARCA_BIB_selectPerPolo"
                condition="mid = :XXXmid
                           AND cd_polo = :XXXcd_polo "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_marca_bib> selectPerPolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_marca_bib.class);
			Filter filter = session.enableFilter("VL_MARCA_BIB_selectPerPolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_polo));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_polo);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_marca_bib",
                    this.basicCriteria, session);

			List<Vl_marca_bib> result = this.basicCriteria.list();
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
    <statement nome="selectPerAnagrafe" tipo="select" id="01">
            <fisso>
                WHERE
                  mid = XXXmid
                  AND cd_ana_biblioteca= XXXcd_ana_biblioteca
            </fisso>

       <filter name="VL_MARCA_BIB_selectPerAnagrafe"
               condition="mid = :XXXmid
                          AND cd_ana_biblioteca= :XXXcd_ana_biblioteca "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_bib> selectPerAnagrafe(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_marca_bib.class);
            Filter filter = session.enableFilter("VL_MARCA_BIB_selectPerAnagrafe");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid));
           filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_ana_biblioteca,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_ana_biblioteca));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid);
           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_ana_biblioteca);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_bib",
                   this.basicCriteria, session);

            List<Vl_marca_bib> result = this.basicCriteria.list();
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
    <statement nome="selectPerAnagrafeLike" tipo="select" id="01">
            <fisso>
                WHERE
                  mid = XXXmid
                  AND upper(trim(cd_ana_biblioteca)) like upper(trim(XXXcd_ana_biblioteca))||'%'
            </fisso>
    </statement>

       <filter name="VL_MARCA_BIB_selectPerAnagrafeLike"
               condition="mid = :XXXmid
                         AND upper(trim(cd_ana_biblioteca)) like upper(trim(:XXXcd_ana_biblioteca))||'%' "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_bib> selectPerAnagrafeLike(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_marca_bib.class);
            Filter filter = session.enableFilter("VL_MARCA_BIB_selectPerAnagrafeLike");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid));
           filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_ana_biblioteca,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_ana_biblioteca));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid);
           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXcd_ana_biblioteca);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_bib",
                   this.basicCriteria, session);

            List<Vl_marca_bib> result = this.basicCriteria.list();
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
            </fisso>
    </statement>

       <filter name="VL_MARCA_BIB_selectPerMid"
               condition="mid = :XXXmid "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_marca_bib> selectPerMid(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_marca_bib.class);
            Filter filter = session.enableFilter("VL_MARCA_BIB_selectPerMid");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid));

           myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_marca_bibCommonDao.XXXmid);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_marca_bib",
                   this.basicCriteria, session);

            List<Vl_marca_bib> result = this.basicCriteria.list();
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
