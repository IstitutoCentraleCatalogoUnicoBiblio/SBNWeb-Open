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
package it.iccu.sbn.polo.orm.documentofisico.viste;

import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;

public class Vc_inventario_coll extends Tbc_inventario {

	private static final long serialVersionUID = 1L;

	private String cd_bib_sez;
	private String cd_sez;
	private String cd_loc;
	private String spec_loc;
	private String ord_loc;
	private String ord_spec;
	private String bid_coll;
	private int tot_inv;

	public String getCd_sez() {
		return cd_sez;
	}

	public void setCd_sez(String cd_sez) {
		this.cd_sez = cd_sez;
	}

	public String getCd_loc() {
		return cd_loc;
	}

	public void setCd_loc(String cd_loc) {
		this.cd_loc = cd_loc;
	}

	public String getSpec_loc() {
		return spec_loc;
	}

	public void setSpec_loc(String spec_loc) {
		this.spec_loc = spec_loc;
	}

	public String getOrd_loc() {
		return ord_loc;
	}

	public void setOrd_loc(String ord_loc) {
		this.ord_loc = ord_loc;
	}

	public String getOrd_spec() {
		return ord_spec;
	}

	public void setOrd_spec(String ord_spec) {
		this.ord_spec = ord_spec;
	}

	public String getBid_coll() {
		return bid_coll;
	}

	public void setBid_coll(String bid_coll) {
		this.bid_coll = bid_coll;
	}

	public int getTot_inv() {
		return tot_inv;
	}

	public void setTot_inv(int totInv) {
		tot_inv = totInv;
	}

	public String getCd_bib_sez() {
		return cd_bib_sez;
	}

	public void setCd_bib_sez(String cd_bib_sez) {
		this.cd_bib_sez = cd_bib_sez;
	}



}
