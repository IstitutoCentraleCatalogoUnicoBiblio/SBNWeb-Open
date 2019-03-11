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

import it.iccu.sbn.ejb.vo.BaseVO;

public class IndirizzoVO extends BaseVO {

	private static final long serialVersionUID = 2027475191472609268L;

	private String via;

	private String cap;

	private String citta;

	private String telefono;

	private String fax;

	private String provincia = "";

	public IndirizzoVO() {
		this.clearInd();
	}

	public void clearInd() {
		this.via = "";
		this.cap = "";
		this.citta = "";
		this.telefono = "";
		this.fax = "";
		this.provincia = "";
	}

	public boolean impostato() {
		return (isFilled(via)
				&& isFilled(cap)
				&& isFilled(citta)
				&& isFilled(provincia));
	}


	public boolean impostatoXResidenzaEstera() {
		return (isFilled(via)
				&& isFilled(citta));
	}


	public boolean almenoUnaProprietaImpostata() {
		return (isFilled(via)
				|| isFilled(cap)
				|| isFilled(citta)
				|| isFilled(provincia));
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = trimAndSet(cap);
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = trimAndSet(citta);
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = trimAndSet(fax);
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = trimAndSet(provincia);
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = trimAndSet(telefono);
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = trimAndSet(via);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IndirizzoVO other = (IndirizzoVO) obj;
		if (cap == null) {
			if (other.cap != null)
				return false;
		} else if (!cap.equals(other.cap))
			return false;
		if (citta == null) {
			if (other.citta != null)
				return false;
		} else if (!citta.equals(other.citta))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (provincia == null) {
			if (other.provincia != null)
				return false;
		} else if (!provincia.equals(other.provincia))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (via == null) {
			if (other.via != null)
				return false;
		} else if (!via.equals(other.via))
			return false;
		return true;
	}

}
