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
package it.iccu.sbn.ejb.vo.periodici.esame;

import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;

public class SerieInventarioVO extends SerieBaseVO {

	private static final long serialVersionUID = 1439462471157025295L;

	private String codBib;
	private String cd_serie;
	private int cd_inven;
	private String bid;

	public SerieInventarioVO(InventarioVO inv) {
		super();
		this.codBib = inv.getCodBib();
		this.cd_serie = inv.getCodSerie();
		this.cd_inven = inv.getCodInvent();
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCd_serie() {
		return cd_serie;
	}

	public void setCd_serie(String cd_serie) {
		this.cd_serie = cd_serie;
	}

	public int getCd_inven() {
		return cd_inven;
	}

	public void setCd_inven(int cd_inven) {
		this.cd_inven = cd_inven;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

}
