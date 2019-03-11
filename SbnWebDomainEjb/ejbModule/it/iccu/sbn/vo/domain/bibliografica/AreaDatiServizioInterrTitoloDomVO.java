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
package it.iccu.sbn.vo.domain.bibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class AreaDatiServizioInterrTitoloDomVO extends SerializableVO {

	private static final long serialVersionUID = 5590726467158859934L;
	private String bid;
	private String timeStamp;
	private String tipoAuthority;
	private String tipoMateriale;
	private String natura;
	private String livAut;
	private String titoloResponsabilita;
	private String codPaese;
	private String[] arrCodLingua;

	private String codErr;
	private String testoProtocollo;

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
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getLivAut() {
		return livAut;
	}
	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public String getTipoAuthority() {
		return tipoAuthority;
	}
	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}
	public String getTipoMateriale() {
		return tipoMateriale;
	}
	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}
	public String getTitoloResponsabilita() {
		return titoloResponsabilita;
	}
	public void setTitoloResponsabilita(String titoloResponsabilita) {
		this.titoloResponsabilita = titoloResponsabilita;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String[] getArrCodLingua() {
		return arrCodLingua;
	}
	public void setArrCodLingua(String[] arrCodLingua) {
		this.arrCodLingua = arrCodLingua;
	}
	public String getCodPaese() {
		return codPaese;
	}
	public void setCodPaese(String codPaese) {
		this.codPaese = codPaese;
	}


}
