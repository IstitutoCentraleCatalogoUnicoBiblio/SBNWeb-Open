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

import java.io.Serializable;

public class Tr_soggettari_biblioteche extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -1358803660051451361L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_soggettari_biblioteche))
			return false;
		Tr_soggettari_biblioteche tr_soggettari_biblioteche = (Tr_soggettari_biblioteche)aObj;
		if (getCD_SOGG() != null && !getCD_SOGG().equals(tr_soggettari_biblioteche.getCD_SOGG()))
			return false;
		if (getCD_POLO() == null)
			return false;
		if (getCD_POLO() != tr_soggettari_biblioteche.getCD_POLO())
			return false;
		if (getCD_BIBLIOTECA() == null)
			return false;
		if (!getCD_BIBLIOTECA().equals(tr_soggettari_biblioteche.getCD_BIBLIOTECA()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SOGG().hashCode();
		if (getCD_POLO() != null) {
			hashcode = hashcode + getCD_POLO().hashCode();
			hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		}
		return hashcode;
	}

	private String CD_SOGG;

	private String CD_BIBLIOTECA;

	private String CD_POLO;

	private String FL_ATT;

	private String FL_AUTO_LOC;

	public String getCD_POLO() {
		return CD_POLO;
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
		this.settaParametro(KeyParameter.XXXcd_polo,value);
	}


	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setCD_BIBLIOTECA(String value) {
		this.CD_BIBLIOTECA = value;
		this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCD_SOGG() {
		return CD_SOGG;
	}

	public void setCD_SOGG(String value) {
		this.CD_SOGG = value;
		this.settaParametro(KeyParameter.XXXcd_sogg,value);
	}

	public String getFL_ATT() {
		return FL_ATT;
	}

	public void setFL_ATT(String value) {
		this.FL_ATT = value;
		this.settaParametro(KeyParameter.XXXfl_att,value);
	}

	public String getFL_AUTO_LOC() {
		return FL_AUTO_LOC;
	}

	public void setFL_AUTO_LOC(String value) {
		this.FL_AUTO_LOC = value;
		this.settaParametro(KeyParameter.XXXfl_auto_loc,value);
	}

    public String toString() {
		return String.valueOf(getCD_SOGG() + " " + ((getCD_POLO() == null) ? "" :
				String.valueOf(getCD_POLO()) + " " + String.valueOf(getCD_BIBLIOTECA())));
	}


	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}


}

