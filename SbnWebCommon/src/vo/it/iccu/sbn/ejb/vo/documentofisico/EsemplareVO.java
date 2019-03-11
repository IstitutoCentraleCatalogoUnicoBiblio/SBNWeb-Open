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

public class EsemplareVO extends SerializableVO {

	private static final long serialVersionUID = 5201846750790123507L;

	private String codPolo;
	private String codBib;
	private String bid;
	private Integer codDoc;
	private String consDoc;
	private String dataIns;
	private String dataAgg;
	private String uteIns;
	private String uteAgg;
	private String cancDb2i;

	public EsemplareVO(){
	}

//	costruttore per EsemplareDettalio
	public EsemplareVO(EsemplareVO esempl)
	throws Exception {
		this.codPolo = esempl.codPolo;
		this.codBib = esempl.codBib;
		this.bid = esempl.bid;
		this.codDoc = esempl.codDoc;
		this.consDoc = esempl.consDoc;
		this.dataIns = esempl.dataIns;
		this.dataAgg = esempl.dataAgg;
		this.uteIns = esempl.uteIns;
		this.uteAgg = esempl.uteAgg;
		this.cancDb2i = esempl.cancDb2i;
	}
//	costruttore per EsemplareLista
	public EsemplareVO(String codPolo, String codBib, String bid, int codDoc, String consDoc)
	throws Exception {
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.bid = bid;
		this.codDoc = codDoc;
		this.consDoc = consDoc;
	}
//	costruttore per EsemplareOrdine
	public EsemplareVO(String codPolo, String codBib)
	throws Exception {
		this.codPolo = codPolo;
		this.codBib = codBib;
	}
	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCancDb2i() {
		return cancDb2i;
	}

	public void setCancDb2i(String cancDb2i) {
		this.cancDb2i = cancDb2i;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public Integer getCodDoc() {
		return codDoc;
	}

	public void setCodDoc(Integer codDoc) {
		this.codDoc = codDoc;
	}


	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getConsDoc() {
		return consDoc;
	}

	public void setConsDoc(String consDoc) {
		this.consDoc = consDoc;
	}

	public String getUteAgg() {
		return uteAgg;
	}

	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}

	public String getUteIns() {
		return uteIns;
	}

	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}

}
