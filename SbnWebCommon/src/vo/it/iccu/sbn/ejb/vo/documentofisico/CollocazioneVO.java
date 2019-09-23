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

public class CollocazioneVO extends SerializableVO {

	private static final long serialVersionUID = -4271443153909864110L;

	private String codSistema;
	private String codEdizione;
	private String classe;
	private String codPoloSez;
	private String codBibSez;
	private String codSez;
	private String codPoloDoc;
	private String codBibDoc;
	private String bidDoc;
	private int codDoc;
	private int keyColloc;
	private String bid;
	private String codPolo;
	private String codBib;
	private String codColloc;
	private String specColloc;
	private String consistenza;
	private Integer totInv;
	private String indice;
	private String ordLoc;
	private String ordSpec;
	private Integer totInvProv;
	private String dataIns;
	private String uteIns;
	private String dataAgg;
	private String uteAgg;
	private String cancDb2i;
	private FormatiSezioniVO recFS;
	private String msg;
	private String serieIncompleta;
	private String numMancanti;

	public CollocazioneVO() {
	}

	public CollocazioneVO(int keyColloc) throws Exception {
		this.keyColloc = keyColloc;
	}

	public CollocazioneVO(String codLoc, String codSpec, String ordLoc)
			throws Exception {
		this.codColloc = codLoc;
		this.specColloc = codSpec;
		this.ordLoc = ordLoc;
	}

	public CollocazioneVO(int keyColloc, String bid) throws Exception {
		this.keyColloc = keyColloc;
		this.bid = bid;
	}

	// costruttore per collocazioneDettaglio
	public CollocazioneVO(CollocazioneVO coll) throws Exception {
		this.dataIns = coll.dataIns;
		this.dataAgg = coll.dataAgg;
		this.keyColloc = coll.keyColloc;
		this.codColloc = coll.codColloc;
		this.specColloc = coll.specColloc;
		this.consistenza = coll.consistenza;
		this.totInv = coll.totInv;
		this.codBibDoc = coll.codBibDoc;
		this.bidDoc = coll.bidDoc;
		this.codDoc = coll.codDoc;
		this.bid = coll.bid;
		this.indice = coll.indice;
		this.codBib = coll.codBib;
		this.codPolo = coll.codPolo;
		this.codSez = coll.codSez;
		this.ordLoc = coll.ordLoc;
		this.ordSpec = coll.ordSpec;
		this.totInvProv = coll.totInvProv;
		this.cancDb2i = coll.cancDb2i;
	}

	public void setKeyColloc(int keyColloc) {
		this.keyColloc = keyColloc;
	}

	// costruttore CollocazioneReticolo
	public CollocazioneVO(String dataIns, String dataAgg, String codBib,
			String bid, String codColloc, String specColloc, int totInv,
			String consistenza, String ord1Colloc, String ord2Colloc,
			int keyColloc, String bidDoc, int codDoc, String codSez)
			throws Exception {
		this.dataIns = dataIns;
		this.dataAgg = dataAgg;
		this.keyColloc = keyColloc;
		this.codColloc = codColloc;
		this.specColloc = specColloc;
		this.consistenza = consistenza;
		this.totInv = totInv;
		this.bidDoc = bidDoc;
		this.codDoc = codDoc;
		this.bid = bid;
		this.codBib = codBib;
		this.codSez = codSez;
	}

	// tracciato liste
	public CollocazioneVO(String dataIns, String dataAgg, String codBib,
			String bid, String codColloc, String specColloc, int totInv,
			String consistenza, int keyColloc, String bidDoc, int codDoc,
			String codSez) throws Exception {
		this.dataIns = dataIns;
		this.dataAgg = dataAgg;
		this.codBib = codBib;
		this.bid = bid;
		this.codColloc = codColloc;
		this.specColloc = specColloc;
		this.totInv = totInv;
		this.consistenza = consistenza;
		this.keyColloc = keyColloc;
		this.bidDoc = bidDoc;
		this.codDoc = codDoc;
		this.codSez = codSez;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getBidDoc() {
		return bidDoc;
	}

	public void setBidDoc(String bidDoc) {
		this.bidDoc = bidDoc;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodBibDoc() {
		return codBibDoc;
	}

	public void setCodBibDoc(String codBibDoc) {
		this.codBibDoc = codBibDoc;
	}

	public String getCodColloc() {
		return codColloc;
	}

	public void setCodColloc(String codColloc) {
		this.codColloc = codColloc;
	}

	public Integer getCodDoc() {
		return codDoc;
	}

	public void setCodDoc(Integer codDoc) {
		if (codDoc != null)
			this.codDoc = codDoc.intValue();
		else
			this.codDoc = 0;
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = trimOrEmpty(consistenza);
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

	public int getKeyColloc() {
		return keyColloc;
	}

	public String getCancDb2i() {
		return cancDb2i;
	}

	public void setCancDb2i(String cancDb2i) {
		this.cancDb2i = cancDb2i;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public Integer getTotInv() {
		return totInv;
	}

	public void setTotInv(Integer totInv) {
		this.totInv = totInv;
	}

	public String getSpecColloc() {
		return specColloc;
	}

	public void setSpecColloc(String specColloc) {
		this.specColloc = specColloc;
	}

	public Integer getTotInvProv() {
		return totInvProv;
	}

	public void setTotInvProv(Integer totInvProv) {
		this.totInvProv = totInvProv;
	}

	public String getOrdLoc() {
		return ordLoc;
	}

	public void setOrdLoc(String ordLoc) {
		this.ordLoc = ordLoc;
	}

	public String getOrdSpec() {
		return ordSpec;
	}

	public void setOrdSpec(String ordSpec) {
		this.ordSpec = ordSpec;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getCodBibSez() {
		return codBibSez;
	}

	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	public String getCodEdizione() {
		return codEdizione;
	}

	public void setCodEdizione(String codEdizione) {
		this.codEdizione = codEdizione;
	}

	public String getCodPoloDoc() {
		return codPoloDoc;
	}

	public void setCodPoloDoc(String codPoloDoc) {
		this.codPoloDoc = codPoloDoc;
	}

	public String getCodPoloSez() {
		return codPoloSez;
	}

	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}

	public String getCodSistema() {
		return codSistema;
	}

	public void setCodSistema(String codSistema) {
		this.codSistema = codSistema;
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

	public void setCodDoc(int codDoc) {
		this.codDoc = codDoc;
	}

	public FormatiSezioniVO getRecFS() {
		return recFS;
	}

	public void setRecFS(FormatiSezioniVO recFS) {
		this.recFS = recFS;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSerieIncompleta() {
		return serieIncompleta;
	}

	public void setSerieIncompleta(String serieIncompleta) {
		this.serieIncompleta = serieIncompleta;
	}

	public String getNumMancanti() {
		return numMancanti;
	}

	public void setNumMancanti(String numMancanti) {
		this.numMancanti = numMancanti;
	}

}
