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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_audiovideoResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_audiovideo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 */
public class Audiovisivo extends Tb_audiovideo {

	private static final long serialVersionUID = -7709748462514037805L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.Audiovisivo");


    /**
     * @throws InfrastructureException
     */

	public Audiovisivo() {
    }


    public Tb_audiovideo cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_audiovideoResult tavola = new Tb_audiovideoResult(this);
        tavola.selectPerKey();
        List v = tavola.getElencoRisultati();
        if (v.size() > 0)
            return (Tb_audiovideo) v.get(0);
        else
            return null;
    }

    /**
     */
    public void inserisci(Tb_audiovideo Audiovideo) throws EccezioneDB, InfrastructureException {
    	Tb_audiovideoResult tavola = new Tb_audiovideoResult(Audiovideo);
        tavola.insert(Audiovideo);

    }
    public void cancellaPerBid(String bid,String ute_var,SbnDatavar timestamp) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        setTS_VAR(timestamp.getTimestamp());
        Tb_audiovideoResult tavola = new Tb_audiovideoResult(this);
        tavola.executeCustom("updateCancellaPerBid");
    }

    /**
   */
    public void update(Tb_audiovideo Audiovideo) throws EccezioneDB, InfrastructureException {
    	Tb_audiovideoResult tavola = new Tb_audiovideoResult(Audiovideo);
    	tavola.update(Audiovideo);
    }
}
