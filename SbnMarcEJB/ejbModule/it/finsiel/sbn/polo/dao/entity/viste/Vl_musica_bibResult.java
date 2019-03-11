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
import it.finsiel.sbn.polo.orm.viste.Vl_musica_bib;

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
public class Vl_musica_bibResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao{

    public Vl_musica_bibResult(Vl_musica_bib vl_musica_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_musica_bib.leggiAllParametro());
    }

    /*
    <statement nome="selectLocalizzazione" tipo="select" id="01_taymer">
            <fisso>
                WHERE
                cd_polo = XXXcd_polo
                AND cd_biblioteca = XXXcd_biblioteca
            </fisso>
    <filter name="VL_MUSICA_BIB_selectLocalizzazione"
            condition="cd_polo = :XXXcd_polo
            AND cd_biblioteca = :XXXcd_biblioteca "/>
     */
    public List<Vl_musica_bib> selectLocalizzazione(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_musica_bib.class);
            Filter filter = session.enableFilter("VL_MUSICA_BIB_selectLocalizzazione");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_polo));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_biblioteca,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_polo);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_biblioteca);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_musica_bib",
                    this.basicCriteria, session);

            List<Vl_musica_bib> result = this.basicCriteria.list();
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
    <statement nome="countLocalizzazione" tipo="count" id="02_taymer">
            <fisso>
                SELECT COUNT (*) FROM Vl_musica_bibCommonDao
                WHERE
                cd_polo = XXXcd_polo
                AND cd_biblioteca = XXXcd_biblioteca
            </fisso>
    <filter name="VL_MUSICA_BIB_countLocalizzazione"
            condition="cd_polo = :XXXcd_polo
            AND cd_biblioteca = :XXXcd_biblioteca "/>
     */



    public Integer countLocalizzazione(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_musica_bib.class);
            Filter filter = session.enableFilter("VL_MUSICA_BIB_countLocalizzazione");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_polo));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_biblioteca,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_polo);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_bibCommonDao.XXXcd_biblioteca);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_musica_bib",
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

}
