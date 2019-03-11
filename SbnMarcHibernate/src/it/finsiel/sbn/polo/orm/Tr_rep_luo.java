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
 * Classe Tr_rep_luo ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
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
 * @version 4/5/2015
 */
public class Tr_rep_luo extends OggettoServerSbnMarc {

	private static final long serialVersionUID = 3158057345357419200L;

	private String NOTA_REP_LUO;
	private String LID;
	private long ID_REPERTORIO;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_aut))
			return false;
		Tr_rep_aut tr_rep_aut = (Tr_rep_aut) aObj;
		if (getLID() == null)
			return false;
		if (!getLID().equals(tr_rep_aut.getVID()))
			return false;
		if (getID_REPERTORIO() != tr_rep_aut.getID_REPERTORIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getLID() != null) {
			hashcode = hashcode + getLID().hashCode();
		}
		hashcode = hashcode + (int) getID_REPERTORIO();
		return hashcode;
	}

	public void setNOTA_REP_LUO(String value) {
		this.NOTA_REP_LUO = value;
		this.settaParametro(KeyParameter.XXXnota_rep_luo, value);
	}

	public String getNOTA_REP_LUO() {
		return NOTA_REP_LUO;
	}

	public void setLID(String value) {
		this.LID = value;
		this.settaParametro(KeyParameter.XXXlid, value);
	}

	public String getLID() {
		return LID;
	}

	public void setID_REPERTORIO(long value) {
		this.ID_REPERTORIO = value;
		this.settaParametro(KeyParameter.XXXid_repertorio, value);
	}

	public long getID_REPERTORIO() {
		return ID_REPERTORIO;
	}

	public String toString() {
		return String.valueOf(((getLID() == null) ? "" : String
				.valueOf(getLID())) + " " + String.valueOf(getID_REPERTORIO()));
	}

	private boolean _saved = false;

	public void onSave() {
		_saved = true;
	}

	public void onLoad() {
		_saved = true;
	}

	public boolean isSaved() {
		return _saved;
	}

}
