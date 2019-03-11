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
/**
 * ORM-Persistable Class
 */
public class Tb_abstract extends OggettoServerSbnMarc implements Serializable {


	private static final long serialVersionUID = 325728753847608808L;

	private String BID;

	private String DS_ABSTRACT;

	private String CD_LIVELLO;

	public void setBID(String value) {
		this.BID = value;
		this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}


	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
		this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getDS_ABSTRACT() {
		return DS_ABSTRACT;
	}

	public void setDS_ABSTRACT(String value) {
		DS_ABSTRACT = value;
		this.settaParametro(KeyParameter.XXXds_abstract,value);
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
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
