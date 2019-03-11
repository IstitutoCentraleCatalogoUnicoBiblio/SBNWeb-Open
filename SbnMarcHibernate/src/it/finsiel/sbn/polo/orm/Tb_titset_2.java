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
package it.finsiel.sbn.polo.orm;

/**
 * Classe Tb_titset_2 ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
 * AUTOMATICAMENTE. NESSUNA MODIFICA DEVE ESSERE APPORTATA MANUALMENTE, PERCHE'
 * SARA' PERSA IN FUTURO. OGNI AGGIUNTA MANUALE NON E' ATTUALMENTE POSSIBILE.
 *
 * <p>
 * Classe che contiene gli attributi estratti dalla tavola del DB
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 19/5/2016
 */
public class Tb_titset_2 extends OggettoServerSbnMarc {

	private static final long serialVersionUID = -1676127069222048815L;

	// Attributi
	private String BID;
	private String S231_FORMA_OPERA;
	private String S231_DATA_OPERA;
	private String S231_ALTRE_CARATTERISTICHE;

	public String getBID() {
		return BID;
	}

	public void setBID(String value) {
		BID = value;
		this.settaParametro(KeyParameter.XXXbid, value);
	}

	public String getS231_DATA_OPERA() {
		return S231_DATA_OPERA;
	}

	public void setS231_DATA_OPERA(String value) {
		S231_DATA_OPERA = value;
		this.settaParametro(KeyParameter.XXXs231_data_opera, value);
	}

	public String getS231_ALTRE_CARATTERISTICHE() {
		return S231_ALTRE_CARATTERISTICHE;
	}

	public void setS231_ALTRE_CARATTERISTICHE(String value) {
		S231_ALTRE_CARATTERISTICHE = value;
		this.settaParametro(KeyParameter.XXXs231_altre_caratteristiche, value);
	}

	public String getS231_FORMA_OPERA() {
		return S231_FORMA_OPERA;
	}

	public void setS231_FORMA_OPERA(String value) {
		S231_FORMA_OPERA = value;
		this.settaParametro(KeyParameter.XXXs231_forma_opera, value);
	}

}
