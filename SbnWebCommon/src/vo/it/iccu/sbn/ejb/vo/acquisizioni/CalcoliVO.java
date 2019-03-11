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

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

public class CalcoliVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 415183351056038691L;

	private double ordinato;
	private String ordinatoStr;
	private double acquisito;
	private String acquisitoStr;
	private double fatturato;
	private String fatturatoStr;
	private double pagato;
	private String pagatoStr;
	private SezioneVO sezione;

	public CalcoliVO() {
		pagato = 0;
		acquisito = 0;
		ordinato = 0;
		fatturato = 0;
		pagatoStr = "0,00";
		acquisitoStr = "0,00";
		ordinatoStr = "0,00";
		fatturatoStr = "0,00";
	}

	public double getAcquisito() {
		return acquisito;
	}

	public void setAcquisito(double acquisito) {
		this.acquisito = acquisito;
	}

	public String getAcquisitoStr() {
		return acquisitoStr;
	}

	public void setAcquisitoStr(String acquisitoStr) {
		this.acquisitoStr = acquisitoStr;
	}

	public double getFatturato() {
		return fatturato;
	}

	public void setFatturato(double fatturato) {
		this.fatturato = fatturato;
	}

	public String getFatturatoStr() {
		return fatturatoStr;
	}

	public void setFatturatoStr(String fatturatoStr) {
		this.fatturatoStr = fatturatoStr;
	}

	public double getOrdinato() {
		return ordinato;
	}

	public void setOrdinato(double ordinato) {
		this.ordinato = ordinato;
	}

	public String getOrdinatoStr() {
		return ordinatoStr;
	}

	public void setOrdinatoStr(String ordinatoStr) {
		this.ordinatoStr = ordinatoStr;
	}

	public double getPagato() {
		return pagato;
	}

	public void setPagato(double pagato) {
		this.pagato = pagato;
	}

	public String getPagatoStr() {
		return pagatoStr;
	}

	public void setPagatoStr(String pagatoStr) {
		this.pagatoStr = pagatoStr;
	}

	public SezioneVO getSezione() {
		return sezione;
	}

	public void setSezione(SezioneVO sezione) {
		this.sezione = sezione;
	}

}
