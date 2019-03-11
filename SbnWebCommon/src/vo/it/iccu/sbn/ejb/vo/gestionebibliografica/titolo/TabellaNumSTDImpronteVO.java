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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;



public class TabellaNumSTDImpronteVO extends SerializableVO {

	// = TabellaNumSTDImpronteVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -8951889460429895016L;
	private String  campoUno;
	private String  campoDue;
	private String  descCampoDue;
	private String  nota;


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TabellaNumSTDImpronteVO other = (TabellaNumSTDImpronteVO) obj;
		if (campoDue == null) {
			if (other.campoDue != null)
				return false;
		} else if (!campoDue.equals(other.campoDue))
			return false;
		if (campoUno == null) {
			if (other.campoUno != null)
				return false;
		} else if (!campoUno.equals(other.campoUno))
			return false;
		if (descCampoDue == null) {
			if (other.descCampoDue != null)
				return false;
		} else if (!descCampoDue.equals(other.descCampoDue))
			return false;
		if (nota == null) {
			if (other.nota != null)
				return false;
		} else if (!nota.equals(other.nota))
			return false;
		return true;
	}

	public TabellaNumSTDImpronteVO() {
		super();
		this.campoUno = "";
		this.campoDue = "";
		this.descCampoDue = "";
		this.nota = "";

	}

	public TabellaNumSTDImpronteVO(String campoUno, String campoDue, String  descCampoDue, String nota) {
		super();
		this.campoUno = campoUno;
		this.campoDue = campoDue;
		this.descCampoDue = descCampoDue;
		this.nota = nota;
	}

	public String getCampoDue() {
		return campoDue;
	}
	public void setCampoDue(String campoDue) {
		this.campoDue = campoDue;
	}
	public String getCampoUno() {
		return campoUno;
	}
	public void setCampoUno(String campoUno) {
		this.campoUno = campoUno;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public String getDescCampoDue() {
		return descCampoDue;
	}
	public void setDescCampoDue(String descCampoDue) {
		this.descCampoDue = descCampoDue;
	}

}
