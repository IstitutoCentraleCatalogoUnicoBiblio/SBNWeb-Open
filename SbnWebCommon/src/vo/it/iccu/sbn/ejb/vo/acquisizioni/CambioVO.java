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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.sql.Timestamp;

public class CambioVO extends SerializableVO{
	/**
	 *
	 */
	private static final long serialVersionUID = 8137468854809465306L;
	private Integer progressivo=0;
	private int IDCambio;
	private String codPolo;
	private String codBibl;
	private String codValuta;
	private String desValuta;
	private double tassoCambio;
	private String tassoCambioStr;
	private String dataVariazione;
	private String tipoVariazione;
	private String ticket;
	private String utente;
	private boolean flag_canc=false;
	private Timestamp dataUpd;
	private boolean valRifer = false;



	public CambioVO (){};
	public CambioVO (String codP, String codB, String codVal, String desVal , double tasso, String dataVar, String tipoVar) throws Exception {
		this.codPolo = codP;
		this.codBibl = codB;
		this.codValuta = codVal;
		this.desValuta = desVal;
		this.tassoCambio = tasso;
		this.dataVariazione = dataVar;
		this.tipoVariazione = tipoVar;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodValuta() {
		return codValuta;
	}

	public void setCodValuta(String codValuta) {
		this.codValuta = codValuta;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getDesValuta() {
		return desValuta;
	}

	public void setDesValuta(String desValuta) {
		this.desValuta = desValuta;
	}

	public double getTassoCambio() {
		return tassoCambio;
	}

	public void setTassoCambio(double tassoCambio) {
		this.tassoCambio = tassoCambio;
	}
	public String getTipoVariazione() {
		return tipoVariazione;
	}
	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public Integer getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getTassoCambioStr() {
		return tassoCambioStr;
	}
	public void setTassoCambioStr(String tassoCambioStr) {
		this.tassoCambioStr = tassoCambioStr;
	}
	public int getIDCambio() {
		return IDCambio;
	}
	public void setIDCambio(int cambio) {
		IDCambio = cambio;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}
	public boolean isValRifer() {
		return valRifer;
	}
	public void setValRifer(boolean valRifer) {
		this.valRifer = valRifer;
	}



}
