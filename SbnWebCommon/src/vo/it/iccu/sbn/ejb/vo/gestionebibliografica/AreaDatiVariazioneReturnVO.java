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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;
import java.util.Map;

public class AreaDatiVariazioneReturnVO extends SerializableVO {

	private static final long serialVersionUID = 2392007922040549569L;

	private String codErr;
	private String testoProtocollo;
	private String testoProtocolloInformational;
	private String versioneIndice;
	private String versionePolo;

	private boolean primoBloccoSimili;
	private String bid;
	private String bidTemporaneo;

	// Area per la prospettazione della lista simili
	private String livelloTrovato;
	private String idLista;
	private int maxRighe;
	private int totRighe;
	private int numBlocco;
	private int numNotizie;
	private int totBlocchi;
	private int numPrimo;
	private List listaSintetica;

	private Map<String, SerializableVO> tabellaOggettiPerCattura;

	public boolean isPrimoBloccoSimili() {
		return primoBloccoSimili;
	}

	public void setPrimoBloccoSimili(boolean primoBloccoSimili) {
		this.primoBloccoSimili = primoBloccoSimili;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public String getLivelloTrovato() {
		return livelloTrovato;
	}

	public void setLivelloTrovato(String livelloTrovato) {
		this.livelloTrovato = livelloTrovato;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public Map<String, SerializableVO> getTabellaOggettiPerCattura() {
		return tabellaOggettiPerCattura;
	}

	public void setTabellaOggettiPerCattura(Map<String, SerializableVO> tabellaOggettiPerCattura) {
		this.tabellaOggettiPerCattura = tabellaOggettiPerCattura;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getVersioneIndice() {
		return versioneIndice;
	}

	public void setVersioneIndice(String versioneIndice) {
		this.versioneIndice = versioneIndice;
	}

	public String getVersionePolo() {
		return versionePolo;
	}

	public void setVersionePolo(String versionePolo) {
		this.versionePolo = versionePolo;
	}

	public String getBidTemporaneo() {
		return bidTemporaneo;
	}

	public void setBidTemporaneo(String bidTemporaneo) {
		this.bidTemporaneo = bidTemporaneo;
	}

	public String getTestoProtocolloInformational() {
		return testoProtocolloInformational;
	}

	public void setTestoProtocolloInformational(
			String testoProtocolloInformational) {
		this.testoProtocolloInformational = testoProtocolloInformational;
	}

}
