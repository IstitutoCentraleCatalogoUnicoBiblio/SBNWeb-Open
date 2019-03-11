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
package it.iccu.sbn.web.actions.gestionebibliografica.isbd.akros;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Classe ElencoArticoli
 * <p>
 * Contiene articoli della lingua italiana e inglese.
 * Eventualmente potrebbe leggerli da DB.
 * </p>
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer
 *
 * @version 2-dic-2003
 */
public class ElencoArticoli {

    /**
     * Hashtable di stoplist nelle varie lingue:
     * contiene coppie di tipo (lingua,sortedSet).
     * La sortedSet (di tipo TreeSet) dovrebbe migliorare l'efficienza della ricerca.
     */
    private static Hashtable elenchi = new Hashtable();
    static {
        SortedSet v = new TreeSet();
        String[] arr = { "GLI", "I", "IL", "L", "LA", "LE" , "LO", "UN", "UNA" };
        for (int i = 0; i<arr.length;i++)
            v.add(arr[i]);
        elenchi.put("ITA", v);
        v = new TreeSet();
        String[] arr1 = { "A","AN","THE" };
        for (int i = 0; i<arr1.length;i++)
            v.add(arr1[i]);
        elenchi.put("ENG", v);
    }

    /**
     * Verifica se un elemento di una lingua è contenuto nella
     * relativa stop list.
     */
    public static boolean contiene(String articolo, String lingua) {
        if (lingua == null)
            return contiene(articolo);
        SortedSet set = (SortedSet) elenchi.get(lingua.toUpperCase());
        if (set == null)
            return false;
        return set.contains(articolo.toUpperCase());
    }

    /**
     * Verifica se un elemento di una lingua è contenuto nella
     * relativa stop list.
     */
    public static boolean contiene(String articolo) {
        Enumeration en = elenchi.keys();
        SortedSet set;
        while (en.hasMoreElements()) {
            set = (SortedSet) elenchi.get(en.nextElement());
            if (set.contains(articolo.toUpperCase()))
                return true;
        }
        return false;
    }

}
