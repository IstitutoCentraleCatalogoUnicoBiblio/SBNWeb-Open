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
package it.iccu.sbn.ejb.vo.gestionestampe;

import java.io.Serializable;

public class StampaSchedeVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1993141832203798509L;
	// Attributes
	private String scegliTAB;
	private String codiceBibl;
	private String chkStampaPiuInventari;
	private String status;
	private String natura;
	private String font;
	private String punti;


    // Constructors
    public StampaSchedeVO() {
    }


	public String getFont() {
		return font;
	}


	public void setFont(String font) {
		this.font = font;
	}


	public String getPunti() {
		return punti;
	}


	public void setPunti(String punti) {
		this.punti = punti;
	}


	public String getChkStampaPiuInventari() {
		return chkStampaPiuInventari;
	}


	public void setChkStampaPiuInventari(String chkStampaPiuInventari) {
		this.chkStampaPiuInventari = chkStampaPiuInventari;
	}


	public String getCodiceBibl() {
		return codiceBibl;
	}


	public void setCodiceBibl(String codiceBibl) {
		this.codiceBibl = codiceBibl;
	}


	public String getNatura() {
		return natura;
	}


	public void setNatura(String natura) {
		this.natura = natura;
	}


	public String getScegliTAB() {
		return scegliTAB;
	}


	public void setScegliTAB(String scegliTAB) {
		this.scegliTAB = scegliTAB;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}




}

