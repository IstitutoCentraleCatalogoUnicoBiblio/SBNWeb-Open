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
package it.iccu.sbn.ejb.vo.gestionestampe;

import java.io.Serializable;

public class StampaUtentiVO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 4925225376045097883L;
	// Attributes
	private String cognNome;
	private String dataNasc;
	private String codFisc;
	private String tipoAut;
	private String materia;
	private String prof;
	private String nazCitt;
	private String titStudio;
	private String provRes;
	private String specTit;
	private String codAteneo;
	private String occup;
	private String fineAut;

    // Constructors
    public StampaUtentiVO() {
    }

	public String getCodAteneo() {
		return codAteneo;
	}

	public void setCodAteneo(String codAteneo) {
		this.codAteneo = codAteneo;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getCognNome() {
		return cognNome;
	}

	public void setCognNome(String cognNome) {
		this.cognNome = cognNome;
	}

	public String getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getFineAut() {
		return fineAut;
	}

	public void setFineAut(String fineAut) {
		this.fineAut = fineAut;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getNazCitt() {
		return nazCitt;
	}

	public void setNazCitt(String nazCitt) {
		this.nazCitt = nazCitt;
	}

	public String getOccup() {
		return occup;
	}

	public void setOccup(String occup) {
		this.occup = occup;
	}

	public String getProf() {
		return prof;
	}

	public void setProf(String prof) {
		this.prof = prof;
	}

	public String getProvRes() {
		return provRes;
	}

	public void setProvRes(String provRes) {
		this.provRes = provRes;
	}

	public String getSpecTit() {
		return specTit;
	}

	public void setSpecTit(String specTit) {
		this.specTit = specTit;
	}

	public String getTipoAut() {
		return tipoAut;
	}

	public void setTipoAut(String tipoAut) {
		this.tipoAut = tipoAut;
	}

	public String getTitStudio() {
		return titStudio;
	}

	public void setTitStudio(String titStudio) {
		this.titStudio = titStudio;
	}

}

