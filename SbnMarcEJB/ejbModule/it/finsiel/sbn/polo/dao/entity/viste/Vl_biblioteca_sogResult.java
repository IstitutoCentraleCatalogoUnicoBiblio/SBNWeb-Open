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
import it.finsiel.sbn.polo.dao.common.viste.Vl_biblioteca_sogCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.viste.Vl_biblioteca_sog;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

public class Vl_biblioteca_sogResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_biblioteca_sogCommonDao{

	public Vl_biblioteca_sogResult(Vl_biblioteca_sog vl_biblioteca_sog) throws InfrastructureException {
		super();
        this.valorizzaParametro(vl_biblioteca_sog.leggiAllParametro());

	}

    /*
    <statement nome="selectPerFiltraLocalizza" tipo="select" id="Carlo_02">
        <fisso>
                WHERE
                  cid = XXXcid
        </fisso>
        <opzionale dipende="XXXcd_polo"> AND cd_polo = XXXcd_polo</opzionale>
        <opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca IN (</opzionale>
        <opzionale dipende="XXXcdBiblio" ciclico="S" separatore=",">XXXcdBiblio</opzionale>
        <opzionale dipende="XXXcd_biblioteca">)</opzionale>
    </statement>

        <filter name="VL_BIBLIOTECA_SOG_selectPerFiltraLocalizza"
                condition="cid = :XXXcid "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_biblioteca_sog> selectPerFiltraLocalizza(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_biblioteca_sog.class);
            Filter filter = session.enableFilter("VL_BIBLIOTECA_SOG_selectPerFiltraLocalizza");

            filter.setParameter(Vl_biblioteca_sogCommonDao.XXXcid,opzioni.get(Vl_biblioteca_sogCommonDao.XXXcid));
           myOpzioni.remove(Vl_biblioteca_sogCommonDao.XXXcid);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_biblioteca_sog",
                   this.basicCriteria, session);

            List<Vl_biblioteca_sog> result = this.basicCriteria.list();
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
