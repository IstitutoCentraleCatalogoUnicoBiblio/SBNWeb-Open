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
package it.iccu.sbn.ejb.vo.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ElencoDirittiUtenteVO extends SerializableVO {

	private static final long serialVersionUID = 1610525205613126731L;

	private Integer idServizio;
	private String servizi;
	private String scadenza;
	private String sospesoDal;
	private String sospesoAl;

	public Integer getIdServizio() {
		return idServizio;
	}

	public void setIdServizio(Integer idServizio) {
		this.idServizio = idServizio;
	}


	public String getServizi() {
		return servizi;
	}

	public void setServizi(String servizi) {
		this.servizi = servizi;
	}

	public String getSospesoAl() {
		return sospesoAl;
	}

	public void setSospesoAl(String sospesoAl) {
		this.sospesoAl = sospesoAl;
	}

	public String getSospesoDal() {
		return sospesoDal;
	}

	public void setSospesoDal(String sospesoDal) {
		this.sospesoDal = sospesoDal;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}

	public String getScadenza() {
		return scadenza;
	}
}
