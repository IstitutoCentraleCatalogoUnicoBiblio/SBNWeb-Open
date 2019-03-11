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
 * Classe DBException.java
 * <p>
 * Eccezione relativa all'accesso al DB.
 * </p>
 *
 * @author
 * @author
 *
 * @version 4-set-2002
 */
public class EccezioneDB extends EccezioneIccu {

	private static final long serialVersionUID = -1517128539053399980L;

	static Category log = Category.getInstance("it.finsiel.sbn.polo.exception.EccezioneDB");

	/** Costruttore codice errore e eccezione generante*/
	public EccezioneDB (int errorID, Exception eccezione) {
		super (errorID,eccezione);
	}

	/** Costruttore codice errore , messaggio e eccezione generante*/
	public EccezioneDB (int errorID, String message, Exception eccezione) {
		super (errorID, message, eccezione);
	}

	/** Costruttore codice errore */
	public EccezioneDB (int errorID) {
		super (errorID);
	}

	/**
	 * Constructor EccezioneDB.
	 * @param i
	 * @param string
	 */
	public EccezioneDB(int errorID, String string) {
        super (errorID,string);
	}


}
