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
package it.finsiel.sbn.polo.exception;

import org.apache.log4j.Category;
/**
 * Classe EccezioneAutorizzazione.java
 * <p>
 * Eccezione relativa alle autorizzazioni: viene generata qualora
 * un utente richieda l'esecuzione di un'attività per cui non ha
 * l'autorizzazione.
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-ott-02
 */
public class EccezioneAbilitazioni extends EccezioneIccu {

	private static final long serialVersionUID = -1252877087652935907L;

	static Category log = Category.getInstance("it.finsiel.sbn.polo.exception.EccezioneAutorizzazione");

    /** Costruttore codice errore e eccezione generante*/
    public EccezioneAbilitazioni (int errorID, Exception eccezione) {
        super (errorID,eccezione);
    }

    /** Costruttore codice errore , messaggio e eccezione generante*/
    public EccezioneAbilitazioni (int errorID, String message, Exception eccezione) {
        super (errorID, message, eccezione);
    }

    /** Costruttore codice errore */
    public EccezioneAbilitazioni (int errorID) {
        super (errorID);
    }

    /** Costruttore codice errore , messaggio*/
    public EccezioneAbilitazioni (int errorID, String message) {
        super (errorID, message);
    }

}

