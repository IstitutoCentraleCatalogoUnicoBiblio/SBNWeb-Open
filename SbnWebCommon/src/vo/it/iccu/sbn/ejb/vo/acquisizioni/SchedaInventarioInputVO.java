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
package it.iccu.sbn.ejb.vo.acquisizioni;


import java.io.Serializable;

public class SchedaInventarioInputVO implements Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = -3099557964166855419L;


	//dati di input per creazione inventario
	private String codPolo = "";
	private String codBib = "";
	private String serie = "";
	private String bidInv = "";
	private String codProven = "";

	private String codBibO = "";//kbibl
	private String kanno = "";
	private String NroOrdineRinnovo = "";
	private String annoOrd="";
	private String codOrd="";

	private String idFornitore = "";
	private String codTipoOrd = "";
	private String dataIngresso = "";
	private String dataPrimaCollocazione = "";

	private String keyLoc = "";
	private String tipoMat = "";
	private String tipoCirc = "";
	private String precis = "";
	private String valore = "";
	private String prezzoBil = "";
	private String tipoPrezzo = "";
	private String consisDoc = "";
	private String sequenza = "";


	public SchedaInventarioInputVO(){};


	public String getNroOrdineRinnovo() {
		return NroOrdineRinnovo;
	}
	public void setNroOrdineRinnovo(String nroOrdineRinnovo) {
		NroOrdineRinnovo = nroOrdineRinnovo;
	}
	public String getIdFornitore() {
		return idFornitore;
	}
	public void setIdFornitore(String idFornitore) {
		this.idFornitore = idFornitore;
	}
	public String getKeyLoc() {
		return keyLoc;
	}
	public void setKeyLoc(String keyLoc) {
		this.keyLoc = keyLoc;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getKanno() {
		return kanno;
	}
	public void setKanno(String kanno) {
		this.kanno = kanno;
	}
	public String getTipoMat() {
		return tipoMat;
	}
	public void setTipoMat(String tipoMat) {
		this.tipoMat = tipoMat;
	}
	public String getTipoCirc() {
		return tipoCirc;
	}
	public void setTipoCirc(String tipoCirc) {
		this.tipoCirc = tipoCirc;
	}
	public String getPrecis() {
		return precis;
	}
	public void setPrecis(String precis) {
		this.precis = precis;
	}
	public String getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}
	public String getPrezzoBil() {
		return prezzoBil;
	}
	public void setPrezzoBil(String prezzoBil) {
		this.prezzoBil = prezzoBil;
	}
	public String getTipoPrezzo() {
		return tipoPrezzo;
	}
	public void setTipoPrezzo(String tipoPrezzo) {
		this.tipoPrezzo = tipoPrezzo;
	}
	public String getConsisDoc() {
		return consisDoc;
	}
	public void setConsisDoc(String consisDoc) {
		this.consisDoc = consisDoc;
	}


	public String getBidInv() {
		return bidInv;
	}


	public void setBidInv(String bidInv) {
		this.bidInv = bidInv;
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


	public String getCodProven() {
		return codProven;
	}


	public void setCodProven(String codProven) {
		this.codProven = codProven;
	}


	public String getCodBibO() {
		return codBibO;
	}


	public void setCodBibO(String codBibO) {
		this.codBibO = codBibO;
	}


	public String getCodTipoOrd() {
		return codTipoOrd;
	}


	public void setCodTipoOrd(String codTipoOrd) {
		this.codTipoOrd = codTipoOrd;
	}


	public String getDataIngresso() {
		return dataIngresso;
	}


	public void setDataIngresso(String dataIngresso) {
		this.dataIngresso = dataIngresso;
	}


	public String getDataPrimaCollocazione() {
		return dataPrimaCollocazione;
	}


	public void setDataPrimaCollocazione(String dataPrimaCollocazione) {
		this.dataPrimaCollocazione = dataPrimaCollocazione;
	}


	public String getAnnoOrd() {
		return annoOrd;
	}


	public void setAnnoOrd(String annoOrd) {
		this.annoOrd = annoOrd;
	}


	public String getCodOrd() {
		return codOrd;
	}


	public void setCodOrd(String codOrd) {
		this.codOrd = codOrd;
	}


	public String getSequenza() {
		return sequenza;
	}


	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}
}
