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
 * Classe EccezioneFactoring
 * <p>
 * Eccezione relativa alle azioni di Factoring
 * </p>
 *
 * @author
 * @author
 *
 * @version 25-set-2002
 */
public class EccezioneFactoring extends EccezioneIccu
{

	private static final long serialVersionUID = -4005458587690659006L;

	static Category log = Category.getInstance("it.finsiel.sbn.polo.exception.EccezioneFactoring");

	/** Costruttore codice errore e eccezione generante*/
	public EccezioneFactoring (int errorID, Exception eccezione) {
		super (errorID,eccezione);
	}

	/** Costruttore codice errore , messaggio e eccezione generante*/
	public EccezioneFactoring (int errorID, String message, Exception eccezione) {
		super (errorID, message, eccezione);
	}


	/** Costruttore codice errore */
	public EccezioneFactoring (int errorID) {
		super (errorID);
	}

    /** Costruttore codice errore , messaggio */
    public EccezioneFactoring (int errorID, String message) {
        super (errorID, message);
    }



}
