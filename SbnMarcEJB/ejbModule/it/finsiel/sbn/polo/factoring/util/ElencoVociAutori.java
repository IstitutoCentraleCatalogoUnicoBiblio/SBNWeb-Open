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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

/**
 * Classe Elenco Voci degli autori
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 21-lug-03
 */
public class ElencoVociAutori implements Elenco {

    private static Map<String, Ts_stop_list> elenco = new HashMap<String, Ts_stop_list>();
    private static final ElencoVociAutori instance = new ElencoVociAutori();

    static Category log = Category.getInstance("iccu.box.ElencoVociAutori");


    private ElencoVociAutori()  {}

    /**
    * Inizializza la lista delle voci non corrette
    * @param nomeFile
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */
    public void init() throws Exception {
    	Decodificatore.instance.w.lock();
    	try {
			log.debug("Initializing ElencoVociAutori");
			StopList stoplist = new StopList();
			elenco = new HashMap<String, Ts_stop_list>();
			List v = stoplist.leggiParoleV();
			Ts_stop_list sl;
			for (int i = 0; i < v.size(); i++) {
				sl = (Ts_stop_list) v.get(i);
				elenco.put(sl.getPAROLA().toUpperCase(), sl);
			}
		} finally {
			Decodificatore.instance.w.unlock();
		}
    }

    /**
     * Verifica se il nome inizia con una stringa non corretta.
     */
    public static Ts_stop_list verificaEsistenzaVoce(String nome) {
    	Decodificatore.instance.r.lock();
        try {
			Iterator<String> voci = elenco.keySet().iterator();
			String parola;
			while (voci.hasNext()) {
				parola = voci.next();
				if (nome.toUpperCase().startsWith(parola))
					return elenco.get(parola);
			}
			return null;
		} finally {
			Decodificatore.instance.r.unlock();
		}
    }

	public boolean contiene(String parola, String lingua) {
		return false;
	}

	public boolean contiene(String parola) {
		return false;
	}

	public static ElencoVociAutori getInstance() {
		return instance;
	}

}
