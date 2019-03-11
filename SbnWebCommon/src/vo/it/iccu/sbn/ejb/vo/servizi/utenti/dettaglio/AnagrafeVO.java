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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class AnagrafeVO extends SerializableVO {

	private static final long serialVersionUID = -4522712523897108326L;
	private String codFiscale;
	private String dataNascita;
	private String luogoNascita;
	private String nazionalita = "";
	private String postaElettronica;
	private String provenienza;
	private String sesso;
	private IndirizzoVO domicilio = new IndirizzoVO();
	private ResidenzaVO residenza = new ResidenzaVO();

	private String postaElettronica2;

	public AnagrafeVO() {
		this.clear();
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = trimAndSet(dataNascita);
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = trimAndSet(luogoNascita);
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = trimAndSet(nazionalita);
	}

	public String getPostaElettronica() {
		return postaElettronica;
	}

	public void setPostaElettronica(String postaElettronica) {
		this.postaElettronica = trimAndSet(postaElettronica);
	}

	public String getPostaElettronica2() {
		return postaElettronica2;
	}

	public void setPostaElettronica2(String postaElettronica2) {
		this.postaElettronica2 = trimAndSet(postaElettronica2);
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = trimAndSet(sesso);
	}

	public IndirizzoVO getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(IndirizzoVO domicilio) {
		this.domicilio = domicilio;
	}

	public ResidenzaVO getResidenza() {
		return residenza;
	}
	public void setResidenza(ResidenzaVO residenza) {
		this.residenza = residenza;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = trimOrEmpty(codFiscale).toUpperCase();
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = trimAndSet(provenienza);
	}

	public void clear() {
		this.sesso = "";
		this.luogoNascita = "";
		this.dataNascita = "";
		this.codFiscale = "";
		this.provenienza = "";
		this.postaElettronica = "";
		this.postaElettronica2 = "";
		this.nazionalita = "";
		this.residenza.clearRes();
		this.getDomicilio().clearInd();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AnagrafeVO other = (AnagrafeVO) obj;
		if (codFiscale == null) {
			if (other.codFiscale != null)
				return false;
		} else if (!codFiscale.trim().equals(other.codFiscale.trim()))
			return false;
		if (dataNascita == null) {
			if (other.dataNascita != null)
				return false;
		} else if (!dataNascita.equals(other.dataNascita))
			return false;
		if (domicilio == null) {
			if (other.domicilio != null)
				return false;
		} else if (!domicilio.equals(other.domicilio))
			return false;
		if (luogoNascita == null) {
			if (other.luogoNascita != null)
				return false;
		} else if (!luogoNascita.equals(other.luogoNascita))
			return false;
		if (nazionalita == null) {
			if (other.nazionalita != null)
				return false;
		} else if (!nazionalita.equals(other.nazionalita))
			return false;
		if (postaElettronica == null) {
			if (other.postaElettronica != null)
				return false;
		} else if (!postaElettronica.equals(other.postaElettronica))
			return false;
		if (postaElettronica2 == null) {
			if (other.postaElettronica2 != null)
				return false;
		} else if (!postaElettronica2.equals(other.postaElettronica2))
			return false;
		if (provenienza == null) {
			if (other.provenienza != null)
				return false;
		} else if (!provenienza.equals(other.provenienza))
			return false;
		if (residenza == null) {
			if (other.residenza != null)
				return false;
		} else if (!residenza.equals(other.residenza))
			return false;
		if (sesso == null) {
			if (other.sesso != null)
				return false;
		} else if (!sesso.equals(other.sesso))
			return false;
		return true;
	}

}
