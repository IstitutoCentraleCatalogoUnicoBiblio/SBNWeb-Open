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

public class TitoloCollocazioneVO_old implements Serializable {


	private static final long serialVersionUID = -5201085106923625712L;
	private String natura;
	private String bid;
	private String isbd;
	private String chiaveAutore;
	private String chiaveTitolo;

	public TitoloCollocazioneVO_old(String key, String text, String natura,
			String chiaveTitolo, String chiaveAutore) {
		this.bid = key;
		this.isbd = text;
		this.natura = natura;
		this.chiaveAutore = chiaveAutore;
		this.chiaveTitolo = chiaveTitolo;
	}

	public String getBid() {
		return bid;
	}

	public String getIsbd() {
		return isbd;
	}

	public String getNatura() {
		return natura;
	}

	public String getChiaveAutore() {
		return chiaveAutore;
	}

	public String getChiaveTitolo() {
		return chiaveTitolo;
	}

}
