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

public class DatiFornitoreVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -8455737888687489086L;
	private String codPolo;
	private String codBibl;
	private String codFornitore;
	private String tipoPagamento;
	private String codCliente;
	private String nomContatto;
	private String telContatto;
	private String faxContatto;
	private String valuta;
	private List<StrutturaProfiloVO> profiliAcq;
	private boolean flag_canc=false;


	public DatiFornitoreVO (){};
	public DatiFornitoreVO (String codP, String codB, String codForn, String tipoPagForn , String codCliForn, String numContForn, String telContForn, String faxContForn, String valForn) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.codPolo=codP;
		this.codBibl=codB;
		this.codFornitore=codForn;
		this.tipoPagamento=tipoPagForn;
		this.codCliente=codCliForn;
		this.nomContatto=numContForn;
		this.telContatto=telContForn;
		this.faxContatto=faxContForn;
		this.valuta=valForn;

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


	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public String getFaxContatto() {
		return faxContatto;
	}

	public void setFaxContatto(String faxContatto) {
		this.faxContatto = faxContatto;
	}

	public String getNomContatto() {
		return nomContatto;
	}

	public void setNomContatto(String numContatto) {
		this.nomContatto = numContatto;
	}

	public String getTelContatto() {
		return telContatto;
	}

	public void setTelContatto(String telContatto) {
		this.telContatto = telContatto;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}
	public List<StrutturaProfiloVO> getProfiliAcq() {
		return profiliAcq;
	}
	public void setProfiliAcq(List<StrutturaProfiloVO> profiliAcq) {
		this.profiliAcq = profiliAcq;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}





}
