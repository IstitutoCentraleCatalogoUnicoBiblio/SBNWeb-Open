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

public class EsameCollocRicercaVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 4827292852900508414L;
	private String codPolo;
	private String codBib;
	private String codPoloSez;
	private String codBibSez;
	private String codSez;
	private String codPoloDoc;
	private String codBibDoc;
	private String bidDoc;
	private int codDoc;
	private String aLoc;
	private String aSpec;
	private String codLoc;
	private String codSpec;
	private String tipoRicerca;
	private String ultLoc;
	private String ultOrdLoc;
	private String ultSpec;
	private int keyLoc;
	private int ultKeyLoc;
	private int nuovoLimiteRicerca;
	private String oldIdLista;
	private int ultimoBlocco;
	private String tipoOperazione;
	private String primoCodLoc;

	private String codSerie;
	private int codInvent;

	private int ElemPerBlocchi;

	//
	private boolean esattoColl;
	private boolean esattoSpec;
	//
	private String ordLst;
	//

	// almaviva5_20101116 gest. periodici
	private int annoAbb;

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public int getCodInvent() {
		return codInvent;
	}

	public void setCodInvent(int codInvent) {
		this.codInvent = codInvent;
	}

	public int getUltKeyLoc() {
		return ultKeyLoc;
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getCodSpec() {
		return codSpec;
	}

	public void setCodSpec(String codSpec) {
		this.codSpec = codSpec;
	}

	public String getUltLoc() {
		return ultLoc;
	}

	public void setUltLoc(String ultLoc) {
		this.ultLoc = ultLoc;
	}

	public String getUltSpec() {
		return ultSpec;
	}

	public void setUltSpec(String ultSpec) {
		this.ultSpec = ultSpec;
	}

	public void setUltKeyLoc(int ultKeyLoc) {
		this.ultKeyLoc = ultKeyLoc;
	}

	public String getOrdLst() {
		return ordLst;
	}

	public void setOrdLst(String ordLst) {
		this.ordLst = ordLst;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public int getKeyLoc() {
		return keyLoc;
	}

	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
	}

	public String getBidDoc() {
		return bidDoc;
	}

	public void setBidDoc(String bidDoc) {
		this.bidDoc = bidDoc;
	}

	public String getCodBibDoc() {
		return codBibDoc;
	}

	public void setCodBibDoc(String codBibDoc) {
		this.codBibDoc = codBibDoc;
	}

	public int getCodDoc() {
		return codDoc;
	}

	public void setCodDoc(int codDoc) {
		this.codDoc = codDoc;
	}

	public String getCodPoloDoc() {
		return codPoloDoc;
	}

	public void setCodPoloDoc(String codPoloDoc) {
		this.codPoloDoc = codPoloDoc;
	}

	public String getCodBibSez() {
		return codBibSez;
	}

	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	public String getCodPoloSez() {
		return codPoloSez;
	}

	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}

	public int getElemPerBlocchi() {
		return ElemPerBlocchi;
	}

	public void setElemPerBlocchi(int elemPerBlocchi) {
		ElemPerBlocchi = elemPerBlocchi;
	}

	public int getNuovoLimiteRicerca() {
		return nuovoLimiteRicerca;
	}

	public void setNuovoLimiteRicerca(int nuovoLimiteRicerca) {
		this.nuovoLimiteRicerca = nuovoLimiteRicerca;
	}

	public String getOldIdLista() {
		return oldIdLista;
	}

	public void setOldIdLista(String oldIdLista) {
		this.oldIdLista = oldIdLista;
	}

	public int getUltimoBlocco() {
		return ultimoBlocco;
	}

	public void setUltimoBlocco(int ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}

	public String getALoc() {
		return aLoc;
	}

	public void setALoc(String loc) {
		aLoc = loc;
	}

	public String getASpec() {
		return aSpec;
	}

	public void setASpec(String spec) {
		aSpec = spec;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public boolean isEsattoColl() {
		return esattoColl;
	}

	public void setEsattoColl(boolean esattoColl) {
		this.esattoColl = esattoColl;
	}

	public boolean isEsattoSpec() {
		return esattoSpec;
	}

	public void setEsattoSpec(boolean esattoSpec) {
		this.esattoSpec = esattoSpec;
	}

	public String getPrimoCodLoc() {
		return primoCodLoc;
	}

	public void setPrimoCodLoc(String primoCodLoc) {
		this.primoCodLoc = primoCodLoc;
	}

	public String getUltOrdLoc() {
		return ultOrdLoc;
	}

	public void setUltOrdLoc(String ultOrdLoc) {
		this.ultOrdLoc = ultOrdLoc;
	}

	public int getAnnoAbb() {
		return annoAbb;
	}

	public void setAnnoAbb(int annoAbb) {
		this.annoAbb = annoAbb;
	}

}
