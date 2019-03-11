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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titset_2Result;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_titset_2;

import java.util.List;

import org.apache.log4j.Category;

/**
 */
public class Titset2 extends Tb_titset_2 {

	private static final long serialVersionUID = 7560029317300825718L;

    private static Category log = Category.getInstance("iccu.serversbnmarc.Titset2");

    /**
     * @throws InfrastructureException
     */
    public Tb_titset_2 cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_titset_2Result tavola = new Tb_titset_2Result(this);
        tavola.selectPerKey();
        List v = tavola.getElencoRisultati();
        if (v.size() > 0)
            return (Tb_titset_2) v.get(0);
        else
            return null;
    }

    /**
     */
    public void inserisci(Tb_titset_2 titset2) throws EccezioneDB, InfrastructureException {
    	Tb_titset_2Result tavola = new Tb_titset_2Result(titset2);
        tavola.insert(titset2);

    }

    public void cancellaPerBid(String bid, String ute_var, SbnDatavar timestamp) throws Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        setTS_VAR(timestamp.getTimestamp());
        Tb_titset_2Result tavola = new Tb_titset_2Result(this);
        tavola.executeCustom("updateCancellaPerBid");
    }

    /**
   */
    public void update(Tb_titset_2 titset2) throws EccezioneDB, InfrastructureException {
    	Tb_titset_2Result tavola = new Tb_titset_2Result(titset2);
    	tavola.update(titset2);
    }

}
