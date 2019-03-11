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

public class ParametriCollocazioneVO  implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -4731420837736663411L;
	private String tipoOperazione;
	private String livelloCollocazione;
	private String chiave;
	private String confermaCodLoc;

	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getConfermaCodLoc() {
		return confermaCodLoc;
	}
	public void setConfermaCodLoc(String confermaCodLoc) {
		this.confermaCodLoc = confermaCodLoc;
	}
	public String getLivelloCollocazione() {
		return livelloCollocazione;
	}
	public void setLivelloCollocazione(String livelloCollocazione) {
		this.livelloCollocazione = livelloCollocazione;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

}
