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

public class Tr_sistemi_classi_biblioteche extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 2018708755204243194L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_sistemi_classi_biblioteche))
			return false;
		Tr_sistemi_classi_biblioteche tr_sistemi_classi_biblioteche = (Tr_sistemi_classi_biblioteche)aObj;
		if (getCD_POLO() == null)
			return false;
		if (getCD_POLO() != tr_sistemi_classi_biblioteche.getCD_POLO())
			return false;
		if (!getCD_BIBLIOTECA().equals(tr_sistemi_classi_biblioteche.getCD_BIBLIOTECA()))
			return false;
		if (getCD_SISTEMA() == null)
			return false;
		if (!getCD_SISTEMA().equals(tr_sistemi_classi_biblioteche.getCD_SISTEMA()))
			return false;
		if (getCD_EDIZIONE() == null)
			return false;
		if (!getCD_EDIZIONE().equals(tr_sistemi_classi_biblioteche.getCD_EDIZIONE()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCD_POLO() != null) {
			hashcode = hashcode + getCD_POLO().hashCode();
			hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		}
		if (getCD_SISTEMA() != null) {
			hashcode = hashcode + getCD_SISTEMA().hashCode();
			hashcode = hashcode + getCD_EDIZIONE().hashCode();
		}
		return hashcode;
	}


	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String CD_SISTEMA;

	private String CD_EDIZIONE;

	private String FLG_ATT;

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

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setCD_EDIZIONE(String value) {
		this.CD_EDIZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_edizione,value);
	}

	public String getCD_SISTEMA() {
		return CD_SISTEMA;
	}

	public void setCD_SISTEMA(String value) {
		this.CD_SISTEMA = value;
		this.settaParametro(KeyParameter.XXXcd_sistema,value);
	}

	public String getFLG_ATT() {
		return FLG_ATT;
	}

	public void setFLG_ATT(String value) {
		this.FLG_ATT = value;
		this.settaParametro(KeyParameter.XXXflg_att,value);
	}

	public String toString() {
		return String.valueOf(((getCD_POLO() == null) ? "" :
			   String.valueOf(getCD_POLO()) + " " +
			   String.valueOf(getCD_BIBLIOTECA())) + " " + ((getCD_SISTEMA() == null) ? "" :
				   String.valueOf(getCD_SISTEMA())) );
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
