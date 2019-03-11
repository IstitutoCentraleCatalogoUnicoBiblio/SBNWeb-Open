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
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_bib;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_autore_bibResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao{

    public Vl_autore_bibResult(Vl_autore_bib vl_autore_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_autore_bib.leggiAllParametro());
    }

    /*
      <statement nome="selectPerPolo" tipo="select" id="01">
           <fisso>
               WHERE vid = XXXvid AND
               cd_polo = XXXcd_polo
           </fisso>
      <filter name="VL_AUTORE_BIB_selectPerPolo"
              condition="vid = :XXXvid
                   AND cd_polo = :XXXcd_polo "/>
     */
    public List<Vl_autore_bib> selectPerPolo(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_BIB_selectPerPolo");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_polo));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_polo);

            this.createCriteria(myOpzioni);

            List<Vl_autore_bib> result = this.basicCriteria.list();
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
                  vid = XXXvid
                  AND cd_ana_biblioteca= XXXcd_ana_biblioteca
            </fisso>
    </statement>
    <filter name="VL_AUTORE_BIB_selectPerAnagrafe"
            condition="vid = :XXXvid
                AND cd_ana_biblioteca= :XXXcd_ana_biblioteca "/>
     */
    public List<Vl_autore_bib> selectPerAnagrafe(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_AUT_selectAutorePerLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_ana_biblioteca,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_ana_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_ana_biblioteca);

            this.createCriteria(myOpzioni);

            List<Vl_autore_bib> result = this.basicCriteria.list();
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
                  vid = XXXvid
                  AND upper(trim(cd_ana_biblioteca)) like upper(trim(XXXcd_ana_biblioteca))||'%'
            </fisso>
    </statement>

    <filter name="VL_AUTORE_BIB_selectPerAnagrafeLike"
            condition="vid = :XXXvid
                  AND upper(trim(cd_ana_biblioteca)) like upper(trim(:XXXcd_ana_biblioteca))||'%' "/>
     */
    public List<Vl_autore_bib> selectPerAnagrafeLike(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session
                    .enableFilter("VL_AUTORE_BIB_selectPerAnagrafeLike");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_ana_biblioteca,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_ana_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXcd_ana_biblioteca);

            List<Vl_autore_bib> result = this.basicCriteria.list();
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
            </fisso>
    </statement>
    <filter name="VL_AUTORE_BIB_selectPerVid"
            condition="vid = :XXXvid "/>
     */
    public List<Vl_autore_bib> selectPerVid(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_AUTORE_BIB_selectPerVid");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_autore_bibCommonDao.XXXvid);

            List<Vl_autore_bib> result = this.basicCriteria.list();
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
		return Vl_autore_bib.class;
	}

}
