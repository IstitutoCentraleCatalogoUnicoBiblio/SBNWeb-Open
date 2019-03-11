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
import it.finsiel.sbn.polo.orm.viste.Vl_musica_cla;

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
public class Vl_musica_claResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao{

    public Vl_musica_claResult(Vl_musica_cla vl_musica_cla) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_musica_cla.leggiAllParametro());
    }
     /*
    <statement nome="selectPerClasse" tipo="select" id="02_taymer">
            <fisso>
                WHERE cd_sistema = XXXcd_sistema
                AND cd_edizione = XXXcd_edizione
                AND classe = XXXclasse
            </fisso>

    <filter name="VL_MUSICA_CLA_selectPerClasse"
            condition="cd_sistema = :XXXcd_sistema
                      AND cd_edizione = :XXXcd_edizione
                      AND classe = :XXXclasse "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_musica_cla> selectPerClasse(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_musica_cla.class);
			Filter filter = session.enableFilter("VL_MUSICA_CLA_selectPerClasse");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_sistema,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_sistema));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_edizione,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_edizione));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXclasse,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXclasse));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_sistema);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_edizione);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXclasse);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_musica_cla",
                    this.basicCriteria, session);

			List<Vl_musica_cla> result = this.basicCriteria.list();
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
    <statement nome="countPerClasse" tipo="count" id="03_taymer">
            <fisso>
                SELECT COUNT(*) FROM Vl_musica_cla
                WHERE cd_sistema = XXXcd_sistema
                AND cd_edizione = XXXcd_edizione
                AND classe = XXXclasse
            </fisso>
    <filter name="VL_MUSICA_CLA_countPerClasse"
            condition="cd_sistema = :XXXcd_sistema
                      AND cd_edizione = :XXXcd_edizione
                      AND classe = :XXXclasse "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countPerClasse(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_musica_cla.class);
            Filter filter = session.enableFilter("VL_MUSICA_CLA_countPerClasse");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_sistema,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_sistema));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_edizione,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_edizione));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXclasse,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXclasse));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_sistema);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXcd_edizione);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_musica_claCommonDao.XXXclasse);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_musica_cla", this.basicCriteria, session);


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
