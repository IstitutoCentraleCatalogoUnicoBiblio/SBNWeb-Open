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
package it.iccu.sbn.ejb.vo.periodici.previsionale;

import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;

import java.util.Date;

public class RicercaKardexPrevisionaleVO extends RicercaKardexPeriodicoVO {

	private static final long serialVersionUID = 733876045731675363L;

	private ModelloPrevisionaleVO modello = new ModelloPrevisionaleVO();
	private Date data_conv_pub;
	private String annataFrom;
	private String annataTo;
	private int posizione_primo_fascicolo;

	private int durata;
	private DurataPrevisionaleType tipo;

	public ModelloPrevisionaleVO getModello() {
		return modello;
	}

	public void setModello(ModelloPrevisionaleVO modello) {
		this.modello = modello;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public DurataPrevisionaleType getTipo() {
		return tipo;
	}

	public void setTipo(DurataPrevisionaleType tipo) {
		this.tipo = tipo;
	}

	public boolean isPrevedeVolume() {
		return (modello != null && isFilled(modello.getNum_primo_volume()) );
	}

	public boolean isPrevedeNumFascicolo() {
		return (modello != null && isFilled(modello.getNum_primo_fascicolo()) );
	}

	public Date getData_conv_pub() {
		return data_conv_pub;
	}

	public void setData_conv_pub(Date data_conv_pub) {
		this.data_conv_pub = data_conv_pub;
	}

	public String getAnnataFrom() {
		return annataFrom;
	}

	public void setAnnataFrom(String annataFrom) {
		this.annataFrom = annataFrom;
	}

	public String getAnnataTo() {
		return annataTo;
	}

	public void setAnnataTo(String annataTo) {
		this.annataTo = annataTo;
	}

	public int getPosizione_primo_fascicolo() {
		return posizione_primo_fascicolo;
	}

	public void setPosizione_primo_fascicolo(int posizione_primo_fascicolo) {
		this.posizione_primo_fascicolo = posizione_primo_fascicolo;
	}

	public boolean isAnnataDoppia() {
		return isFilled(annataFrom) && isFilled(annataTo);
	}

}
