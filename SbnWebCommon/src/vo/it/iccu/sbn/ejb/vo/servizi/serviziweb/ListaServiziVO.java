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

public class ListaServiziVO extends SerializableVO {

	private static final long serialVersionUID = -7170564053971473936L;

	private String servizio;
	private int idServizio;

	private String cod_mod_erog;
	private String descr_mod_erog;

	private String cod_supporto;
	private String descr_supporto;

	public int getIdServizio() {
		return idServizio;
	}

	public void setIdServizio(int idServizio) {
		this.idServizio = idServizio;
	}

	public String getCod_supporto() {
		return cod_supporto;
	}

	public void setCod_supporto(String cod_supporto) {
		this.cod_supporto = cod_supporto;
	}

	public String getDescr_supporto() {
		return descr_supporto;
	}

	public void setDescr_supporto(String descr_supporto) {
		this.descr_supporto = descr_supporto;
	}

	public String getCod_mod_erog() {
		return cod_mod_erog;
	}

	public void setCod_mod_erog(String cod_mod_erog) {
		this.cod_mod_erog = cod_mod_erog;
	}

	public String getDescr_mod_erog() {
		return descr_mod_erog;
	}

	public void setDescr_mod_erog(String descr_mod_erog) {
		this.descr_mod_erog = descr_mod_erog;
	}

	public String getServizio() {
		return servizio;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}

}
