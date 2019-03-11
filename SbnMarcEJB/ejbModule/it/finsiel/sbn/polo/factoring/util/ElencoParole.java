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
package it.finsiel.sbn.polo.factoring.util;

import it.finsiel.sbn.polo.oggetti.StopList;
import it.finsiel.sbn.polo.orm.Ts_stop_list;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * Classe ElencoForme
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 30-mag-03
 */
public class ElencoParole implements Elenco {
    /** La sortedSet (di tipo TreeSet) dovrebbe migliorare l'efficienza della ricerca.
    */
    private static SortedSet<String> elenco = new TreeSet<String>();
    private static final ElencoParole instance = new ElencoParole();
    static Logger log = Logger.getLogger("iccu.box.ElencoParole");

    private ElencoParole()  {
    	super();
    }

    /**
    * Inizializza la lista degli articoli, solo per le prove
    * @param nomeFile
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */
    public void init() throws Exception {
    	Decodificatore.instance.w.lock();
    	try {
			log.debug("Initializing ElencoParole");
			elenco.clear();
			StopList stoplist = new StopList();
			List v = stoplist.leggiParoleP();
			Ts_stop_list sl;
			for (int i = 0; i < v.size(); i++) {
				sl = (Ts_stop_list) v.get(i);
				elenco.add(sl.getPAROLA().toUpperCase());
			}
		} finally {
			Decodificatore.instance.w.unlock();
		}
    }

    /**
     * Verifica se un elemento di una lingua è contenuto nella
     * stop list.
     */
    public boolean contiene(String parola) {
        if (parola == null)
            return false;
        Decodificatore.instance.r.lock();
        try {
			if (elenco.contains(parola.toUpperCase()))
				return true;
			return false;
		} finally {
			Decodificatore.instance.r.unlock();
		}
    }

	public boolean contiene(String parola, String lingua) {
		return contiene(parola);
	}

	public static ElencoParole getInstance() {
		return instance;
	}

}
