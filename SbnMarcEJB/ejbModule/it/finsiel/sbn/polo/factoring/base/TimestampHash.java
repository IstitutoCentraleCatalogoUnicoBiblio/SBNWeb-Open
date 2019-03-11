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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.factoring.util.SbnDatavar;

import java.util.HashMap;

/**
 * Classe TimestampHash.java
 * <p>
 * Classe per mantenere in memoria le informazioni relative all'ultima modifica
 * lette da db. Serve per garantire integrità dei dati durante la modifica.
 * </p>
 *
 * @author
 * @author
 *
 * @version 6-feb-2003
 */
public class TimestampHash extends HashMap<String, String> {

	private static final long serialVersionUID = 3421339024967351015L;

	public String getTimestamp(String nome_tabella, String id) {
		return get((nome_tabella + id).toUpperCase());
	}

	//bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
	public String getTimestamp (String nome_tabella,int id) {
		return get((nome_tabella+id).toUpperCase());
	}

	public void putTimestamp(String nome_tabella, String id, String timestamp) {
		put((nome_tabella + id).toUpperCase(), timestamp);
	}

	public void putTimestamp(String nome_tabella, String id, SbnDatavar sbnData) {
		//almaviva5_20100209 memorizzo anche i nanosecondi
		String dataComposta = sbnData.getT005Date() + SbnDatavar.NANOS_SEPARATORE + sbnData.getNanos();
		putTimestamp(nome_tabella, id, dataComposta);
	}

	//bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
	public void putTimestamp(String nome_tabella,int id, String timestamp) {
		put((nome_tabella+id).toUpperCase(),timestamp);
	}

	public void putTimestamp(String nome_tabella,int id, SbnDatavar timestamp) {
		putTimestamp(nome_tabella,id,timestamp.getDate());
	}

}
