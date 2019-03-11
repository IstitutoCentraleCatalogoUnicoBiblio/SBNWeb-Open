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

public class Tb_arte_tridimens extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 5782757082067002752L;

	private String BID;

	private String CD_DESIGNAZIONE;

	private String TP_MATERIALE_1;

	private String TP_MATERIALE_2;

	private String TP_MATERIALE_3;

	private String CD_COLORE;

	public void setCD_DESIGNAZIONE(String value) {
		this.CD_DESIGNAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_designazione,value);
  }

	public String getCD_DESIGNAZIONE() {
		return CD_DESIGNAZIONE;
	}

  public void setTP_MATERIALE_1(String value) {
		this.TP_MATERIALE_1 = value;
    this.settaParametro(KeyParameter.XXXtp_materiale_1,value);
	}

	public String getTP_MATERIALE_1() {
		return TP_MATERIALE_1;
	}

	public void setTP_MATERIALE_2(String value) {
		this.TP_MATERIALE_2 = value;
    this.settaParametro(KeyParameter.XXXtp_materiale_2,value);
	}

	public String getTP_MATERIALE_2() {
		return TP_MATERIALE_2;
	}

	public void setTP_MATERIALE_3(String value) {
		this.TP_MATERIALE_3 = value;
    this.settaParametro(KeyParameter.XXXtp_materiale_3,value);
	}

	public String getTP_MATERIALE_3() {
		return TP_MATERIALE_3;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
    this.settaParametro(KeyParameter.XXXcd_colore,value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
	}

}
