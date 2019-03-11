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

import java.util.List;

public class StampaOrdiniCartografiaVO {

	private List listaMeridianiOrigine;
	private String meridianoOrigineSelez;

// Output dei 3 RadioButton (Vuoto/E/O)
	private String longitudineRadio1;
	private String longitudineInput1;
//	 Output dei 3 RadioButton (Vuoto/E/O)
	private String longitudineRadio2;
	private String longitudineInput2;

//	 Output dei 3 RadioButton (Vuoto/E/O)
	private String latitudineRadio1;
	private String latitudineInput1;
//	 Output dei 3 RadioButton (Vuoto/E/O)
	private String latitudineRadio2;
	private String latitudineInput2;

//	 Output dei 3 RadioButton (Lineare/Angolare/Altro)
	private String tipoScalaRadio;

	public String getLatitudineInput1() {
		return latitudineInput1;
	}

	public void setLatitudineInput1(String latitudineInput1) {
		this.latitudineInput1 = latitudineInput1;
	}

	public String getLatitudineInput2() {
		return latitudineInput2;
	}

	public void setLatitudineInput2(String latitudineInput2) {
		this.latitudineInput2 = latitudineInput2;
	}

	public String getLatitudineRadio1() {
		return latitudineRadio1;
	}

	public void setLatitudineRadio1(String latitudineRadio1) {
		this.latitudineRadio1 = latitudineRadio1;
	}

	public String getLatitudineRadio2() {
		return latitudineRadio2;
	}

	public void setLatitudineRadio2(String latitudineRadio2) {
		this.latitudineRadio2 = latitudineRadio2;
	}

	public List getListaMeridianiOrigine() {
		return listaMeridianiOrigine;
	}

	public void setListaMeridianiOrigine(List listaMeridianiOrigine) {
		this.listaMeridianiOrigine = listaMeridianiOrigine;
	}

	public String getLongitudineInput1() {
		return longitudineInput1;
	}

	public void setLongitudineInput1(String longitudineInput1) {
		this.longitudineInput1 = longitudineInput1;
	}

	public String getLongitudineInput2() {
		return longitudineInput2;
	}

	public void setLongitudineInput2(String longitudineInput2) {
		this.longitudineInput2 = longitudineInput2;
	}

	public String getLongitudineRadio1() {
		return longitudineRadio1;
	}

	public void setLongitudineRadio1(String longitudineRadio1) {
		this.longitudineRadio1 = longitudineRadio1;
	}

	public String getLongitudineRadio2() {
		return longitudineRadio2;
	}

	public void setLongitudineRadio2(String longitudineRadio2) {
		this.longitudineRadio2 = longitudineRadio2;
	}

	public String getMeridianoOrigineSelez() {
		return meridianoOrigineSelez;
	}

	public void setMeridianoOrigineSelez(String meridianoOrigineSelez) {
		this.meridianoOrigineSelez = meridianoOrigineSelez;
	}

	public String getTipoScalaRadio() {
		return tipoScalaRadio;
	}

	public void setTipoScalaRadio(String tipoScalaRadio) {
		this.tipoScalaRadio = tipoScalaRadio;
	}


}
