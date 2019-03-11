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
package it.iccu.sbn.ejb.vo.documentofisico;

import java.io.Serializable;

public class TitoloVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3287966026459697910L;
	private String bid;
	private String isbd;
	private String natura;
	private boolean flagCondiviso;
	private String tipoMateriale;
	private String fl_canc;
	private String terzaParte;

	public TitoloVO(String bid, String isbd, String natura, String terzaParte)
	throws Exception {
	this.bid = bid;
	this.bid = isbd;
	this.natura = natura;
	this.terzaParte = terzaParte;
	}

	public String getTerzaParte() {
		return terzaParte;
	}

	public void setTerzaParte(String terzaParte) {
		this.terzaParte = terzaParte;
	}

	public TitoloVO() {
		// TODO Auto-generated constructor stub
	}

	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getFl_canc() {
		return fl_canc;
	}
	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}
	public String getIsbd() {
		return isbd;
	}
	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}
}
