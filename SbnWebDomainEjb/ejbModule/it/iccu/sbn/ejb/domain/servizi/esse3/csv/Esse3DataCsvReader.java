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
package it.iccu.sbn.ejb.domain.servizi.esse3.csv;

import java.util.List;

/**
 * Interfaccia di conversione da CSV dei dati ESSE3 (x LUMSA)
 * @author Luca Ferraro Visardi
 * @version 1.0
 * @since 17/07/2018
 */
public interface Esse3DataCsvReader {
	/**
	 * Esegue la lettura e la conversione dei dati
	 * Ritorna se la conversione è andata a buon fine oppure no.<br>
	 * In caso di errore vedere il log
	 *
	 * @return boolean
	 */
	boolean read();
	/**
	 * Imposta un sparatore custom, default: '|' <br>
	 * Ricordati di impostarlo prima di eseguire read()
	 * @param String separator stringa separatrice
	 */
	void setSeparator(String separator);
	/**
	 * Ritorna la lista di utenti convertiti <br>
	 * @return List<?>
	 */
	List<?> getUtenti();

	enum Esse3DataInputType {
		WEBSERVICE, CSV_FILE_BATCH;
	}
}
