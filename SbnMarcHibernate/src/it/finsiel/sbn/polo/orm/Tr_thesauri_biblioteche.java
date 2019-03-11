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

public class Tr_thesauri_biblioteche extends OggettoServerSbnMarc implements Serializable {



	private static final long serialVersionUID = 1278927619879075768L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_thesauri_biblioteche))
			return false;
		Tr_thesauri_biblioteche tr_thesauri_biblioteche = (Tr_thesauri_biblioteche)aObj;
		if (getCD_POLO() == null)
			return false;
		if (getCD_POLO()!= tr_thesauri_biblioteche.getCD_POLO())
			return false;
		if (!getCD_BIBLIOTECA().equals(tr_thesauri_biblioteche.getCD_BIBLIOTECA()))
			return false;
		if (getCD_THE() != null && !getCD_THE().equals(tr_thesauri_biblioteche.getCD_THE()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCD_POLO() != null) {
			hashcode = hashcode + getCD_POLO().hashCode();
			hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		}
		hashcode = hashcode + getCD_THE().hashCode();
		return hashcode;
	}



	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String CD_THE;

	private String FL_ATT;

	private String FL_AUTO_LOC;

	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setCD_BIBLIOTECA(String value) {
		this.CD_BIBLIOTECA = value;
		this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCD_POLO() {
		return CD_POLO;
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
		this.settaParametro(KeyParameter.XXXcd_polo,value);
	}

	public String getCD_THE() {
		return CD_THE;
	}

	public void setCD_THE(String value) {
		this.CD_THE = value;
		this.settaParametro(KeyParameter.XXXcd_the,value);
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
		return String.valueOf(((getCD_POLO() == null) ? "" : String.valueOf(getCD_POLO()) + " " +
				String.valueOf(getCD_BIBLIOTECA())) + " " + getCD_THE());
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
