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

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Category;

/**
 * Classe GestoreLegami
 * <p>
 * Gestisce i legami tra entità di tipo Accettato, ovvero delle
 * relazioni 'vedi anche'.
 * In fase di inserimento tutte le relazioni che sono ad uno stesso
 * livello devono essere inserite.
 * </p>
 *
 * @author
 * @author
 *
 * @version 15-nov-02
 */
public abstract class GestoreLegami {
    //elenco legami da inserire
    private Map<String, List<?>> legami;
    private Category log = Category.getInstance("iccu.box.utility.GestoreLegami");

    /**
     * Esegue l'elaborazione, prima verifica tutti i legami,
     * quindi inserisce quelli mancanti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void elabora(String id_1, String id_2) throws IllegalArgumentException, InvocationTargetException, Exception {
        //Stack di appoggio per gli elementi da controllare
    	Stack nuovi = new Stack();
        legami = new HashMap();
        String nuovoEl;
        String elemLegato;
        List legati;
        nuovi.push(id_1);
        nuovi.push(id_2);
        //loop fino a quando non svuoto lo stack
        while (!nuovi.isEmpty()) {
            nuovoEl = (String) nuovi.pop();
            legati = cercaLegami(nuovoEl);
            for (int i = 0; i < legati.size(); i++) {
                elemLegato = (String) legati.get(i);
                if (!(legami.containsKey(elemLegato)
                    || nuovi.contains(elemLegato)
                    || nuovoEl.equals(elemLegato))) {
                    //log.debug("nuovo elemento:" + elemLegato);
                    nuovi.push(elemLegato);
                }
            }
            legami.put(nuovoEl, legati);
        }
        inserisci();
        //return inserisci2();
    }

    /**
     * Inserisce tutti i legami che mancano nel DB
     * In alternativa si potrebbe restituire una hashtable contenente solamente
     * le coppie da inserire.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void inserisci() throws IllegalArgumentException, InvocationTargetException, Exception {
        String el_1, el_2;
        List vec_1, vec_2;
        Iterator<String> en = legami.keySet().iterator();
        List tutti = new ArrayList(legami.keySet());
        int size = tutti.size();
        while (en.hasNext()) {
            el_1 = en.next();
            vec_1 = legami.get(el_1);
            for (int i = 0; i < size; i++) {
                el_2 = (String) tutti.get(i);
                vec_2 = legami.get(el_2);
                if (!el_2.equals(el_1) && (vec_2 == null || !vec_2.contains(el_1)))
                    if (!vec_1.contains(el_2)) {
                        //log.debug("aggiungo legame:" + el_1 + " " + el_2);
                        inserisciLegame(el_1, el_2);
                        vec_1.add(el_2);
                    }
            }
        }
    }

    /**
     * Restituisce una hashtable contenente solamente
     * le coppie da inserire.
     */
    protected Map inserisci2() {
        String el_1, el_2;
        List vec_1, vec_2;
        Iterator<String> en = legami.keySet().iterator();
        List tutti = new ArrayList(legami.keySet());
        int size = tutti.size();
        Map mancanti = new HashMap();
        List mancano;
        while (en.hasNext()) {
            el_1 = en.next();
            vec_1 = legami.get(el_1);
            mancano = new ArrayList();
            for (int i = 0; i < size; i++) {
                el_2 = (String) tutti.get(i);
                vec_2 = legami.get(el_2);
                if (vec_2 == null || !vec_2.contains(el_1))
                    if (!vec_1.contains(el_2)) {
                        mancano.add(el_2);
                        vec_1.add(el_2);
                    }
            }
            mancanti.put(el_1, mancano);
        }
        return mancanti;
    }

    /**
     * Restituisce una hashtable contenente come chiavi le stringhe dell'identificatore,
     * come valore un elenco di stringhe con le chiavi legate.
     */
    public Map<String, List<?>> getLegami() {
        return legami;
    }

    /**
     * Deve restituire un elenco di Stringhe che identificano gli oggetti legati a quello
     * con id specificato.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected abstract List cercaLegami(String id) throws EccezioneDB, IllegalArgumentException, InvocationTargetException, Exception;

    /**
     * Deve inserire nel DB il legame specificato dalle due stringhe
     */
    protected abstract void inserisciLegame(String id_1, String id_2)  throws IllegalArgumentException, InvocationTargetException, Exception ;

	/**
	 * Recupera la posizione (rank) del legame con authority.
	 * @param leg i dati del legame
	 * @return la posizione del legame, 0 altrimenti.
	 */
	public static short getPosizioneLegame(LegameElementoAutType leg) {
		short rank = 0;
		if (leg != null) {
			rank = (short) leg.getRank();
		}
		return rank;
	}
}
