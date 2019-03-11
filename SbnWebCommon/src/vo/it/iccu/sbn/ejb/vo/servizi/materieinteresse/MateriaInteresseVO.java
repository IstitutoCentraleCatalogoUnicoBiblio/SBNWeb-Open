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
package it.iccu.sbn.ejb.vo.servizi.materieinteresse;

import it.iccu.sbn.ejb.vo.BaseVO;


public class MateriaInteresseVO extends BaseVO {

	private static final long serialVersionUID = -8467546439127734138L;

	private boolean newMateria;
	private int    idMateria;

	private String codPolo;
	private String codBib;
	private String codMateria;
	private String desMateria;



	public MateriaInteresseVO(String codicePolo, String codBiblioteca, String codMateria, String desMateria) {
		super();
		this.codPolo       = codicePolo;
		this.codBib        = codBiblioteca;
		this.codMateria    = trimAndSet(codMateria);
		this.desMateria    = trimAndSet(desMateria);
	}

	public MateriaInteresseVO() {
		super();
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codicePolo) {
		this.codPolo = codicePolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBiblioteca) {
		this.codBib = codBiblioteca;
	}

	public String getCodMateria() {
		return codMateria;
	}

	public void setCodMateria(String codMateria) {
		this.codMateria = trimAndSet(codMateria);
	}

	public String getDesMateria() {
		return desMateria;
	}

	public void setDesMateria(String desMateria) {
		this.desMateria = trimAndSet(desMateria);
	}

	public boolean isNewMateria() {
		return newMateria;
	}

	public void setNewMateria(boolean newMateria) {
		this.newMateria = newMateria;
	}

	public void clearMateria() {
		this.idMateria  = 0;
		this.newMateria = false;
		this.codMateria = "";
		this.codBib     = "";
		this.desMateria = "";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final MateriaInteresseVO other = (MateriaInteresseVO) obj;

		if (this.codBib == null) {
			if (other.codBib != null) return false;
		} else if (!codBib.equals(other.codBib))
			return false;

		if (this.codPolo == null) {
			if (other.codPolo != null) return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;

		if (this.codMateria == null) {
			if (other.codMateria != null) return false;
		} else if (!codMateria.equals(other.codMateria))
			return false;

		if (this.desMateria == null) {
			if (other.desMateria != null) return false;
		} else if (!desMateria.equals(other.desMateria))
			return false;


		return true;
	}

	public int getIdMateria() {
		return idMateria;
	}

	public void setIdMateria(int idMateria) {
		this.idMateria = idMateria;
	}
}


