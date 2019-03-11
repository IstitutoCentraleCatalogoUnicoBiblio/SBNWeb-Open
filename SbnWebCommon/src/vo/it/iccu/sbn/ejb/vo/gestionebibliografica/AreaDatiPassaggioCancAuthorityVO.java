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

public class AreaDatiPassaggioCancAuthorityVO extends SerializableVO {

	private static final long serialVersionUID = -8361744687177745986L;

	// Valori ammessi "Cancella" (cancella sia in Indice che in Polo) "Scattura"
	// (cancella in Polo e delocalizza in Indice)
	private String tipoOperazione;

	private String tipoAut;
	private String tipoMat;
	private String bid;
	private boolean ricercaPolo;
	private boolean ricercaIndice;
	private String ticket;

	private String codErr;
	private String testoProtocollo;

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
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

	public String getTipoAut() {
		return tipoAut;
	}

	public void setTipoAut(String tipoAut) {
		this.tipoAut = tipoAut;
	}

	public String getTipoMat() {
		return tipoMat;
	}

	public void setTipoMat(String tipoMat) {
		this.tipoMat = tipoMat;
	}

	public boolean isRicercaIndice() {
		return ricercaIndice;
	}

	public void setRicercaIndice(boolean ricercaIndice) {
		this.ricercaIndice = ricercaIndice;
	}

	public boolean isRicercaPolo() {
		return ricercaPolo;
	}

	public void setRicercaPolo(boolean ricercaPolo) {
		this.ricercaPolo = ricercaPolo;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
