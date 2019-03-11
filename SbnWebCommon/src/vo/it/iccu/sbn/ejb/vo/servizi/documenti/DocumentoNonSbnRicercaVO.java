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
package it.iccu.sbn.ejb.vo.servizi.documenti;

import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

public class DocumentoNonSbnRicercaVO extends DocumentoNonSbnVO {

	private static final long serialVersionUID = 801324778007722512L;

	private String ordinamento;
	private String inventario;
	private String dataInizio;
	private String dataFine;
	private List<String> listaBib;

	private boolean ricercaOpac;

	private int elementiPerBlocco = Integer.parseInt(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String tipoServizio;

	private String tipoRecord1;
	private String tipoRecord2;
	private String tipoRecord3;

	private String annoDa;
	private String annoA;

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(int elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getDataFine() {
		return dataFine;
	}

	public List<String> getListaBib() {
		return listaBib;
	}

	public void setListaBib(List<String> listaBib) {
		this.listaBib = listaBib;
	}

	public boolean isRicercaOpac() {
		return ricercaOpac;
	}

	public void setRicercaOpac(boolean ricercaOpac) {
		this.ricercaOpac = ricercaOpac;
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public String getTipoRecord1() {
		return tipoRecord1;
	}

	public void setTipoRecord1(String tipoRecord1) {
		this.tipoRecord1 = tipoRecord1;
	}

	public String getTipoRecord2() {
		return tipoRecord2;
	}

	public void setTipoRecord2(String tipoRecord2) {
		this.tipoRecord2 = tipoRecord2;
	}

	public String getTipoRecord3() {
		return tipoRecord3;
	}

	public void setTipoRecord3(String tipoRecord3) {
		this.tipoRecord3 = tipoRecord3;
	}

	public String getAnnoDa() {
		return annoDa;
	}

	public void setAnnoDa(String annoDa) {
		this.annoDa = annoDa;
	}

	public String getAnnoA() {
		return annoA;
	}

	public void setAnnoA(String annoA) {
		this.annoA = annoA;
	}

}
