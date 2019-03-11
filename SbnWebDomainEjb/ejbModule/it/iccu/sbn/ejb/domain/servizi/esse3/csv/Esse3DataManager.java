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
 * Manager di import dati da Esse3 ad SbnWeb
 * @version 1.0
 * @since 18/07/2018
 */
public interface Esse3DataManager {
	//Tipo di operazione che deve fare
	public enum Esse3OperationType {
		INSERT_FROM_CSV, UPDATE_FROM_CSV, UPDATE_FROM_MODEL ;
	}
	//manager
	 boolean manage(String cd_polo, String cd_biblioteca, Object data);
	//Lista di errori
	 List<String> getErrors();
	 //ritorna la lista di id inseriti
	 List<String> getUtentiInseriti();

	 //ritorna la lista di id aggiornati
	 List<String> getUtentiAggiornati();
}
