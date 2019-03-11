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

import org.apache.log4j.Category;

/**
 * Classe ElencoFormeU
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 30-mag-04
 */
public class ElencoFormeU implements Elenco {
    /** La sortedSet (di tipo TreeSet) dovrebbe migliorare l'efficienza della ricerca.
    */
    private static SortedSet elenco = new TreeSet();
    private static final ElencoFormeU instance = new ElencoFormeU();
    static Category log = Category.getInstance("iccu.box.ElencoFormeU");


    private ElencoFormeU()  {}

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
			log.debug("Initializing ElencoFormeU");
			StopList stoplist = new StopList();
			elenco = new TreeSet<String>();
			List<Ts_stop_list> v = stoplist.leggiParoleU();
			Ts_stop_list sl;
			for (int i = 0; i < v.size(); i++) {
				sl = v.get(i);
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
    public boolean contiene(String forma) {
        if (forma == null)
            return false;
        Decodificatore.instance.r.lock();
        try {
			if (elenco.contains(forma.toUpperCase()))
				return true;
			return false;
		} finally {
			Decodificatore.instance.r.unlock();
		}
    }

	public boolean contiene(String parola, String lingua) {
		return contiene(parola);
	}

	public static ElencoFormeU getInstance() {
		return instance;
	}

}
