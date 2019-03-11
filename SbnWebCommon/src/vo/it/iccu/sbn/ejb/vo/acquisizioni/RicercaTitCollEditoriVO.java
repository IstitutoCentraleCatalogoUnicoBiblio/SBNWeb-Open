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

import java.util.List;

public class RicercaTitCollEditoriVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -2947753583239475357L;
	/**
	 *
	 */
	private int elemXBlocchi;
	private String codFornitore;
	private List<FornitoreVO> selezioniChiamato;
	private String ordinamento;
	private String ticket;
	private boolean flag_canc=false;
	private String tipoRicerca="inizio";

	private String[] isbnSelez;
	private String[] editSelez;
	private String bidSelez;

	public RicercaTitCollEditoriVO () {
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public List<FornitoreVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}

	public void setSelezioniChiamato(List<FornitoreVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flagCanc) {
		flag_canc = flagCanc;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String[] getIsbnSelez() {
		return isbnSelez;
	}

	public void setIsbnSelez(String[] isbnSelez) {
		this.isbnSelez = isbnSelez;
	}

	public String[] getEditSelez() {
		return editSelez;
	}

	public void setEditSelez(String[] editSelez) {
		this.editSelez = editSelez;
	}

	public String getBidSelez() {
		return bidSelez;
	}

	public void setBidSelez(String bidSelez) {
		this.bidSelez = bidSelez;
	}

}
