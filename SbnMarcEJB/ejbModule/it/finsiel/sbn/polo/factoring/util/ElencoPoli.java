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
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Category;

/**
 * Classe ElencoPoli
 * <p>
 * Legge i poli e le priorità per la creazione di legami tit-sog e tit-cla
 * </p>
 * @author
 * @author
 *
 * @version 30-giu-03
 */
public class ElencoPoli implements Elenco {
    /** La sortedSet (di tipo TreeSet) dovrebbe migliorare l'efficienza della ricerca.
    */
    private static Map<String, String> elenco = new TreeMap<String, String>();
    private static final ElencoPoli instance = new ElencoPoli();
    static Category log = Category.getInstance("iccu.box.ElencoPoli");


    private ElencoPoli()  {}

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
			log.debug("Initializing ElencoPoli");
			StopList stoplist = new StopList();
			elenco = new TreeMap<String, String>();
			List v = stoplist.leggiParoleS();
			Ts_stop_list sl;
			for (int i = 0; i < v.size(); i++) {
				sl = (Ts_stop_list) v.get(i);
				elenco.put(sl.getPAROLA().substring(0, 3), sl.getPAROLA().substring(3));
			}
		} finally {
			Decodificatore.instance.w.unlock();
		}
    }

    /**
     * Verifica se un polo è contenuto nella lista.
     */
    public boolean contiene(String polo) {
    	Decodificatore.instance.r.lock();
        try {
			if (elenco.containsKey(polo))
				return true;
			return false;
		} finally {
			Decodificatore.instance.r.unlock();
		}
    }

    public static int getPrioritaClasse(String polo) {
    	Decodificatore.instance.r.lock();
        try {
			if (elenco.containsKey(polo))
				return Integer.parseInt(elenco.get(polo).substring(2));
			else
				return -1;
		} finally {
			Decodificatore.instance.r.unlock();
		}
    }

    public static int getPrioritaSoggetto(String polo) {
    	Decodificatore.instance.r.lock();
        try {
			if (elenco.containsKey(polo))
				return Integer.parseInt(elenco.get(polo).substring(0, 2));
			else
				return -1;
		} finally {
			Decodificatore.instance.r.unlock();
		}
    }

	public boolean contiene(String parola, String lingua) {
		return contiene(parola);
	}

	public static ElencoPoli getInstance() {
		return instance;
	}

}
