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

import java.sql.Timestamp;

public class SerieCollocazioneVO extends SerieBaseVO {

	private static final long serialVersionUID = -28879089166079339L;

	private int key_loc;
	private String cd_loc;
	private String spec_loc;
	private String ord_loc;
	private String ord_spec;
	private String codSez;
	private Timestamp tsInsPrimaColl;
	private String bid;
	private String consis;

	public int getKey_loc() {
		return key_loc;
	}

	public void setKey_loc(int key_loc) {
		this.key_loc = key_loc;
	}

	public String getCd_loc() {
		return cd_loc;
	}

	public void setCd_loc(String cd_loc) {
		this.cd_loc = trimAndSet(cd_loc);
	}

	public String getSpec_loc() {
		return spec_loc;
	}

	public void setSpec_loc(String spec_loc) {
		this.spec_loc = trimAndSet(spec_loc);
	}

	public String getDescrizione() {
		StringBuilder buf = new StringBuilder();
		buf.append(codSez);
		buf.append(" ");
		buf.append(cd_loc);
		buf.append(" ");
		buf.append(spec_loc);
		return buf.toString();
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = trimAndSet(codSez);
	}

	public Timestamp getTsInsPrimaColl() {
		return tsInsPrimaColl;
	}

	public void setTsInsPrimaColl(Timestamp tsPrimaColl) {
		this.tsInsPrimaColl = tsPrimaColl;
	}

	public int compareTo(SerieCollocazioneVO o) {
		int cmp = tsInsPrimaColl.compareTo(o.tsInsPrimaColl);
		cmp = (cmp == 0) ? (ord_loc + ord_spec).compareTo((o.ord_loc + o.ord_spec)) : cmp;
		cmp = (cmp == 0) ? super.compareTo(o) : cmp;
		return cmp;
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

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getConsis() {
		return ValidazioneDati.equals(consis, "$") ? "" : consis;
	}

	public void setConsis(String consis) {
		this.consis = trimAndSet(consis);
	}

}
