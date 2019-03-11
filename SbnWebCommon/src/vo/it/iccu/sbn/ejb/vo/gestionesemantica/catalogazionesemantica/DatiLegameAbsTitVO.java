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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

public class DatiLegameAbsTitVO extends SBNMarcCommonVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -3873227408757568771L;
	private String bid;
	private String bidLivelloAut;
	private String bidNatura;
	private String bidTipoMateriale;
	private String descrAbs;

	public String getDescrAbs() {
		return descrAbs;
	}

	public void setDescrAbs(String descrAbs) {
		this.descrAbs = descrAbs;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getBidLivelloAut() {
		return bidLivelloAut;
	}

	public void setBidLivelloAut(String bidLivelloAut) {
		this.bidLivelloAut = bidLivelloAut;
	}

	public String getBidNatura() {
		return bidNatura;
	}

	public void setBidNatura(String bidNatura) {
		this.bidNatura = bidNatura;
	}

	public String getBidTipoMateriale() {
		return bidTipoMateriale;
	}

	public void setBidTipoMateriale(String bidTipoMateriale) {
		this.bidTipoMateriale = bidTipoMateriale;
	}

}
