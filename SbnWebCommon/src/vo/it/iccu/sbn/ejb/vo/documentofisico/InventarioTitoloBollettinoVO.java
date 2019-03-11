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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.sql.Timestamp;

public class InventarioTitoloBollettinoVO extends SerializableVO {

	private static final long serialVersionUID = -99084477364367717L;

	private String cd_bib;
	private String cd_serie;
	private int cd_inven;
	private String bid;
	private char cd_natura;
	private String isbd;
	private Timestamp ts_ins_prima_coll;
	private String cd_frui;

	public String getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(String cd_bib) {
		this.cd_bib = cd_bib;
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

	public char getCd_natura() {
		return cd_natura;
	}

	public void setCd_natura(char cd_natura) {
		this.cd_natura = cd_natura;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

	public Timestamp getTs_ins_prima_coll() {
		return ts_ins_prima_coll;
	}

	public void setTs_ins_prima_coll(Timestamp ts_ins_prima_coll) {
		this.ts_ins_prima_coll = ts_ins_prima_coll;
	}

	public String getCd_frui() {
		return cd_frui;
	}

	public void setCd_frui(String cd_frui) {
		this.cd_frui = cd_frui;
	}

}
