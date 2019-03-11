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
public class Tb_termine_thesauro extends OggettoServerSbnMarc implements Serializable {


	private static final long serialVersionUID = -3578231656927280312L;

	private String DID;

	private String CD_THE;

	private String DS_TERMINE_THESAURO;

	private String NOTA_TERMINE_THESAURO;

	private String KY_TERMINE_THESAURO;

	private String TP_FORMA_THE;

	private String CD_LIVELLO;

	private String FL_CONDIVISO;

	public String getDID() {
		return DID;
	}


	public void setDID(String value) {
		this.DID = value;
		this.settaParametro(KeyParameter.XXXdid,value);

	}

	public String getCD_THE() {
		return CD_THE;
	}


	public void setCD_THE(String value) {
		this.CD_THE = value;
		this.settaParametro(KeyParameter.XXXcd_the,value);
	}

	public String getDS_TERMINE_THESAURO() {
		return DS_TERMINE_THESAURO;
	}


	public void setDS_TERMINE_THESAURO(String value) {
		this.DS_TERMINE_THESAURO = value;
		this.settaParametro(KeyParameter.XXXds_termine_thesauro,value);
	}


	public String getKY_TERMINE_THESAURO() {
		return KY_TERMINE_THESAURO;
	}


	public void setKY_TERMINE_THESAURO(String value) {
		this.KY_TERMINE_THESAURO = value;
		this.settaParametro(KeyParameter.XXXky_termine_thesauro,value);
	}


	public String getNOTA_TERMINE_THESAURO() {
		return NOTA_TERMINE_THESAURO;
	}


	public void setNOTA_TERMINE_THESAURO(String value) {
		this.NOTA_TERMINE_THESAURO = value;
		this.settaParametro(KeyParameter.XXXnota_termine_thesauro,value);
	}





	public void setTP_FORMA_THE(String value) {
		this.TP_FORMA_THE = value;
    this.settaParametro(KeyParameter.XXXtp_forma_the,value);
	}

	public String getTP_FORMA_THE() {
		return TP_FORMA_THE;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso,value);
	}

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public String toString() {
		return String.valueOf(((getDID() == null) ? "" : String.valueOf(getDID())));
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
