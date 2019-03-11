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

import it.iccu.sbn.ejb.utils.ValidazioneDati;

public class SerieEsemplareCollVO extends SerieBaseVO {

	private static final long serialVersionUID = -1663826118115043539L;

	private String codPolo;
	private String codBib;
	private int cd_doc;
	private String bid;
	private String cons_doc;

	public int getCd_doc() {
		return cd_doc;
	}

	public void setCd_doc(int cd_doc) {
		this.cd_doc = cd_doc;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public int compareTo(SerieEsemplareCollVO o) {

		int cmp = bid.compareTo(o.bid);
		cmp = (cmp == 0) ? cd_doc - o.cd_doc : cmp;
		cmp = (cmp == 0) ? super.compareTo(o) : cmp;
		return cmp;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getDescrizione() {
		return codBib + " " +  bid + " " + cd_doc;
	}

	public String getCons_doc() {
		return ValidazioneDati.equals(cons_doc, "$") ? "" : cons_doc;
	}

	public void setCons_doc(String cons_doc) {
		this.cons_doc = trimAndSet(cons_doc);
	}

}
