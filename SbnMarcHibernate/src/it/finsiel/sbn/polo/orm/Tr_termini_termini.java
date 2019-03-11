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

public class Tr_termini_termini extends OggettoServerSbnMarc implements Serializable {


	private static final long serialVersionUID = 6574088997550216255L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_termini_termini))
			return false;
		Tr_termini_termini tr_termini_termini = (Tr_termini_termini)aObj;
		if (getDID_BASE() == null)
			return false;
		if (!getDID_BASE().equals(tr_termini_termini.getDID_BASE()))
			return false;
		if (getDID_COLL() == null)
			return false;
		if (!getDID_COLL().equals(tr_termini_termini.getDID_COLL()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getDID_BASE() != null) {
			hashcode = hashcode + getDID_BASE().hashCode();
		}
		if (getDID_COLL() != null) {
			hashcode = hashcode + getDID_COLL().hashCode();
		}
		return hashcode;
	}

	private String DID_BASE;

	private String DID_COLL;

	private String NOTA_TERMINI_TERMINI;

	private String TIPO_COLL;

	public String getDID_BASE() {
		return DID_BASE;
	}

	public void setDID_BASE(String value) {
		this.DID_BASE = value;
		this.settaParametro(KeyParameter.XXXdid_base,value);
	}

	public String getDID_COLL() {
		return DID_COLL;
	}

	public void setDID_COLL(String value) {
		this.DID_COLL = value;
		this.settaParametro(KeyParameter.XXXdid_coll,value);
	}

	public String getNOTA_TERMINI_TERMINI() {
		return NOTA_TERMINI_TERMINI;
	}

	public void setNOTA_TERMINI_TERMINI(String value) {
		this.NOTA_TERMINI_TERMINI = value;
		this.settaParametro(KeyParameter.XXXtermini_termini,value);
	}

	public String getTIPO_COLL() {
		return TIPO_COLL;
	}

	public void setTIPO_COLL(String value) {
		this.TIPO_COLL = value;
		this.settaParametro(KeyParameter.XXXtipo_coll,value);
	}


	public String toString() {
		return String.valueOf(((getDID_BASE() == null) ? "" : String.valueOf(getDID_BASE())) + " " + ((getDID_COLL() == null) ? "" : String.valueOf(getDID_COLL())));
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
