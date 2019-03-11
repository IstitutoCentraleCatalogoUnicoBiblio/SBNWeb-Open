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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_disco_sonoroResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_disco_sonoro;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 */
public class Discosonoro extends Tb_disco_sonoro {

	private static final long serialVersionUID = 4401780625579195928L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.Discosonoro");


    /**
     * @throws InfrastructureException
     */
    public Tb_disco_sonoro cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_disco_sonoroResult tavola = new Tb_disco_sonoroResult(this);
        tavola.selectPerKey();
        List v = tavola.getElencoRisultati();
        if (v.size() > 0)
            return (Tb_disco_sonoro) v.get(0);
        else
            return null;
    }

    /**
     */
    public void inserisci(Tb_disco_sonoro Discosonoro) throws EccezioneDB, InfrastructureException {
    	Tb_disco_sonoroResult tavola = new Tb_disco_sonoroResult(Discosonoro);
        tavola.insert(Discosonoro);

    }
    public void cancellaPerBid(String bid,String ute_var,SbnDatavar timestamp) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        setTS_VAR(timestamp.getTimestamp());
        Tb_disco_sonoroResult tavola = new Tb_disco_sonoroResult(this);
        tavola.executeCustom("updateCancellaPerBid");
    }

    /**
   */
    public void update(Tb_disco_sonoro Discosonoro) throws EccezioneDB, InfrastructureException {
    	Tb_disco_sonoroResult tavola = new Tb_disco_sonoroResult(Discosonoro);
    	tavola.update(Discosonoro);
    }
}
