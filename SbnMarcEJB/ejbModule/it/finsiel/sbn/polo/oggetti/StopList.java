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

import it.finsiel.sbn.polo.dao.entity.tavole.Ts_stop_listResult;
import it.finsiel.sbn.polo.orm.Ts_stop_list;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe StopList
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 8-apr-03
 */
public class StopList extends Ts_stop_list {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6253018703740749420L;

	public StopList() {

    }

    public Ts_stop_list verificaNomeEnte(String nome) throws IllegalArgumentException, InvocationTargetException, Exception {
		setTP_STOP_LIST(nome);
		Ts_stop_listResult tavola = new Ts_stop_listResult(this);
		tavola.executeCustom("verificaNomeEnte");
        List v = tavola.getElencoRisultati();
		if (v.size()>0) return (Ts_stop_list)v.get(0);
		return null;
    }

    public List leggiArticoli() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ts_stop_listResult tavola = new Ts_stop_listResult(this);
		tavola.executeCustom("selectElencoArticoli");
        List v = tavola.getElencoRisultati();
		return v;
    }

    /** Filtra le parole nella chiave degli editori
     *
     * @param parola
     * @return true se la parola non è presente nella stop list.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List leggiParoleEeD() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_stop_listResult tavola = new Ts_stop_listResult(this);
        tavola.executeCustom("selectElencoParoleTipiEeD");
        List v = tavola.getElencoRisultati();
        return v;
    }

    public List leggiParoleF() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_stop_listResult tavola = new Ts_stop_listResult(this);
        tavola.executeCustom("selectElencoForme");
        List v = tavola.getElencoRisultati();
        return v;
    }

    public List leggiParoleU() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_stop_listResult tavola = new Ts_stop_listResult(this);
        tavola.executeCustom("selectElencoFormeU");
        List v = tavola.getElencoRisultati();
        return v;
    }

    public List leggiParoleP() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_stop_listResult tavola = new Ts_stop_listResult(this);
        tavola.executeCustom("selectElencoParole");
        List v = tavola.getElencoRisultati();
        return v;
    }

    public List leggiParoleV() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_stop_listResult tavola = new Ts_stop_listResult(this);
        tavola.executeCustom("selectElencoV");
        List v = tavola.getElencoRisultati();
        return v;
    }

    public List leggiParoleS() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_stop_listResult tavola = new Ts_stop_listResult(this);
        tavola.executeCustom("selectElencoS");
        List v = tavola.getElencoRisultati();
        return v;
    }

}
