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

public class ListaSuppCambioVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -8600650562818051777L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String codValuta;
	private String desValuta;
	private boolean esisteRiferimento=false; // controllo presenza di una valuta di riferimento
	private String chiamante;
	private String ordinamento;
	private List<CambioVO> selezioniChiamato;
	private boolean flag_canc=false;
	private boolean modalitaSif=true;


	public ListaSuppCambioVO (){};
	public ListaSuppCambioVO (String codP, String codB, String codVal, String desVal , String chiama) throws Exception {
		this.codPolo = codP;
		this.codBibl = codB;
		this.codValuta = codVal;
		this.desValuta = desVal;
		this.chiamante = chiama;
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



	public String getDesValuta() {
		return desValuta;
	}

	public void setDesValuta(String desValuta) {
		this.desValuta = desValuta;
	}

	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public List<CambioVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<CambioVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getElemXBlocchi() {
		return elemXBlocchi;
	}
	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}
	public boolean isEsisteRiferimento() {
		return esisteRiferimento;
	}
	public void setEsisteRiferimento(boolean esisteRiferimento) {
		this.esisteRiferimento = esisteRiferimento;
	}

}
