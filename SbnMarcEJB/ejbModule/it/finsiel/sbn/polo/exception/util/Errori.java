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
package it.finsiel.sbn.polo.exception.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Errori.java
 * <p>
 * Classe contenente le associazioni tra un identificatore di errore e i
 * relativi campi: descrizione, nome e mail del referente.
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-set-2002
 */
public class Errori {
	private Map<Integer, Errore> errori = new HashMap<Integer, Errore>();

	/**
	 * Aggiunge o sovrascrive un errore
	 */
	public void settaErrore(Integer numeroErrore, Errore errore) {
		errori.put(numeroErrore, errore);
	}

	/**
	 * Aggiunge o sovrascrive un errore
	 */
	public void settaErrore(Integer numeroErrore, String descrizione,
			String nome_referente, String mail_referente) {
		Errore nuovo_errore = new Errore(numeroErrore, descrizione,
				nome_referente, mail_referente);
		errori.put(numeroErrore, nuovo_errore);
	}

	/**
	 * Legge un errore, se non esiste ritorna null
	 */
	public Errore leggiErrore(Integer numero) {
		return errori.get(numero);
	}

	public Enumeration<Integer> getIdErrori() {
    	return Collections.enumeration(errori.keySet());
    }

}
