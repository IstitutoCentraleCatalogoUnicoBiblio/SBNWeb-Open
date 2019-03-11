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

public class InputCalcoliVO  extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -3056250595754703982L;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private int idSezione;
	private int idBilancio;
	private String esercizio;
	private String capitolo;
	private String impegno;
	private String dataOrdineDa;
	private String dataOrdineA;
	private SezioneVO sezione;

	public InputCalcoliVO (){
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
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

	public String getDataOrdineA() {
		return dataOrdineA;
	}

	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}

	public String getDataOrdineDa() {
		return dataOrdineDa;
	}

	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}

	public String getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}

	public int getIdBilancio() {
		return idBilancio;
	}

	public void setIdBilancio(int idBilancio) {
		this.idBilancio = idBilancio;
	}

	public int getIdSezione() {
		return idSezione;
	}

	public void setIdSezione(int idSezione) {
		this.idSezione = idSezione;
	}

	public String getImpegno() {
		return impegno;
	}

	public void setImpegno(String impegno) {
		this.impegno = impegno;
	}

	public SezioneVO getSezione() {
		return sezione;
	}

	public void setSezione(SezioneVO sezione) {
		this.sezione = sezione;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}



}
