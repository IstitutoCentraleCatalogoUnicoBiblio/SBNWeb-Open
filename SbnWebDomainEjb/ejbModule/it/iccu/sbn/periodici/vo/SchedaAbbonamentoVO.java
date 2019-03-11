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
package it.iccu.sbn.periodici.vo;


import java.io.Serializable;

public class SchedaAbbonamentoVO implements Serializable {

	private static final long serialVersionUID = 2324041400431549466L;
	//Abbonamento
	private String kbibl="";
	private String kordi="";
	private String kanno="";
	private String kforn="";
	private String livello="";

	public SchedaAbbonamentoVO (){};


	public String getKanno() {
		return kanno;
	}
	public void setKanno(String kanno) {
		this.kanno = kanno;
	}
	public String getKbibl() {
		return kbibl;
	}
	public void setKbibl(String kbibl) {
		this.kbibl = kbibl;
	}
	public String getKforn() {
		return kforn;
	}
	public void setKforn(String kforn) {
		this.kforn = kforn;
	}
	public String getKordi() {
		return kordi;
	}
	public void setKordi(String kordi) {
		this.kordi = kordi;
	}
	public String getLivello() {
		return livello;
	}
	public void setLivello(String livello) {
		this.livello = livello;
	}




}
