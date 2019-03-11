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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public class InterrogazioneTitoloCartografiaVO   extends SerializableVO {

	// = InterrogazioneTitoloCartografiaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 6758667436695270139L;
	private List listaMeridianiOrigine;
	private String meridianoOrigineSelez;


	// Modifica almaviva2 24.06.2011 - BUG MANTIS collaudo 4386 (adeguamento della mappa di interrogazione per parametri
	// di cartografia a quella di creazione (drop per tipologia di latitudine/longitudine invece dei RadioButton)
	// i campi precedenti vengono asteriscati
	private List listaLongitudine;
	private List listaLatitudine;
	private String longitTipo1;
	private String longitTipo2;
	private String latitTipo1;
	private String latitTipo2;




// Output dei 3 RadioButton (Vuoto/E/O)
//	private String longitudineRadio1;
	private String longitudineInput1;
	private String longInput1gg;
	private String longInput1pp;
	private String longInput1ss;

//	 Output dei 3 RadioButton (Vuoto/E/O)
//	private String longitudineRadio2;
	private String longitudineInput2;
	private String longInput2gg;
	private String longInput2pp;
	private String longInput2ss;

//	 Output dei 3 RadioButton (Vuoto/E/O)
//	private String latitudineRadio1;
	private String latitudineInput1;
	private String latiInput1gg;
	private String latiInput1pp;
	private String latiInput1ss;

//	 Output dei 3 RadioButton (Vuoto/E/O)
//	private String latitudineRadio2;
	private String latitudineInput2;
	private String latiInput2gg;
	private String latiInput2pp;
	private String latiInput2ss;


//	 Output dei 3 RadioButton (Lineare/Angolare/Altro)
	private String tipoScalaRadio;
	private String scalaH;
	private String scalaV;

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

//	public String getLatitudineRadio1() {
//		return latitudineRadio1;
//	}
//
//	public void setLatitudineRadio1(String latitudineRadio1) {
//		this.latitudineRadio1 = latitudineRadio1;
//	}
//
//	public String getLatitudineRadio2() {
//		return latitudineRadio2;
//	}
//
//	public void setLatitudineRadio2(String latitudineRadio2) {
//		this.latitudineRadio2 = latitudineRadio2;
//	}

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

//	public String getLongitudineRadio1() {
//		return longitudineRadio1;
//	}
//
//	public void setLongitudineRadio1(String longitudineRadio1) {
//		this.longitudineRadio1 = longitudineRadio1;
//	}
//
//	public String getLongitudineRadio2() {
//		return longitudineRadio2;
//	}
//
//	public void setLongitudineRadio2(String longitudineRadio2) {
//		this.longitudineRadio2 = longitudineRadio2;
//	}

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

	public String getScalaH() {
		return scalaH;
	}

	public void setScalaH(String scalaH) {
		this.scalaH = scalaH;
	}

	public String getScalaV() {
		return scalaV;
	}

	public void setScalaV(String scalaV) {
		this.scalaV = scalaV;
	}

	public String getLongInput1gg() {
		return longInput1gg;
	}

	public void setLongInput1gg(String longInput1gg) {
		this.longInput1gg = longInput1gg;
	}

	public String getLongInput1pp() {
		return longInput1pp;
	}

	public void setLongInput1pp(String longInput1pp) {
		this.longInput1pp = longInput1pp;
	}

	public String getLongInput1ss() {
		return longInput1ss;
	}

	public void setLongInput1ss(String longInput1ss) {
		this.longInput1ss = longInput1ss;
	}

	public String getLatiInput1gg() {
		return latiInput1gg;
	}

	public void setLatiInput1gg(String latiInput1gg) {
		this.latiInput1gg = latiInput1gg;
	}

	public String getLatiInput1pp() {
		return latiInput1pp;
	}

	public void setLatiInput1pp(String latiInput1pp) {
		this.latiInput1pp = latiInput1pp;
	}

	public String getLatiInput1ss() {
		return latiInput1ss;
	}

	public void setLatiInput1ss(String latiInput1ss) {
		this.latiInput1ss = latiInput1ss;
	}

	public String getLatiInput2gg() {
		return latiInput2gg;
	}

	public void setLatiInput2gg(String latiInput2gg) {
		this.latiInput2gg = latiInput2gg;
	}

	public String getLatiInput2pp() {
		return latiInput2pp;
	}

	public void setLatiInput2pp(String latiInput2pp) {
		this.latiInput2pp = latiInput2pp;
	}

	public String getLatiInput2ss() {
		return latiInput2ss;
	}

	public void setLatiInput2ss(String latiInput2ss) {
		this.latiInput2ss = latiInput2ss;
	}

	public String getLongInput2gg() {
		return longInput2gg;
	}

	public void setLongInput2gg(String longInput2gg) {
		this.longInput2gg = longInput2gg;
	}

	public String getLongInput2pp() {
		return longInput2pp;
	}

	public void setLongInput2pp(String longInput2pp) {
		this.longInput2pp = longInput2pp;
	}

	public String getLongInput2ss() {
		return longInput2ss;
	}

	public void setLongInput2ss(String longInput2ss) {
		this.longInput2ss = longInput2ss;
	}

	public String getLongitTipo1() {
		return longitTipo1;
	}

	public void setLongitTipo1(String longitTipo1) {
		this.longitTipo1 = longitTipo1;
	}

	public String getLongitTipo2() {
		return longitTipo2;
	}

	public void setLongitTipo2(String longitTipo2) {
		this.longitTipo2 = longitTipo2;
	}

	public String getLatitTipo1() {
		return latitTipo1;
	}

	public void setLatitTipo1(String latitTipo1) {
		this.latitTipo1 = latitTipo1;
	}

	public String getLatitTipo2() {
		return latitTipo2;
	}

	public void setLatitTipo2(String latitTipo2) {
		this.latitTipo2 = latitTipo2;
	}

	public List getListaLongitudine() {
		return listaLongitudine;
	}

	public void setListaLongitudine(List listaLongitudine) {
		this.listaLongitudine = listaLongitudine;
	}

	public List getListaLatitudine() {
		return listaLatitudine;
	}

	public void setListaLatitudine(List listaLatitudine) {
		this.listaLatitudine = listaLatitudine;
	}


}
