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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.BaseVO;

public class AnagrafeUtenteProfessionaleVO extends BaseVO {

	private static final long serialVersionUID = 3702678444715441994L;
	private int idUtenteProfessionale;
	private String cognome;
	private String nome;

	private String userId;

	public int getIdUtenteProfessionale() {
		return idUtenteProfessionale;
	}

	public void setIdUtenteProfessionale(int idUtenteProfessionale) {
		this.idUtenteProfessionale = idUtenteProfessionale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = trimAndSet(cognome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public String getCognomeNome() {
		StringBuilder buf = new StringBuilder(128);
		if (isFilled(cognome))
			buf.append(cognome);
		if (isFilled(nome))
			buf.append(" ").append(nome);
		if (isFilled(userId))
			buf.append(" (").append(userId).append(')');

		String output = buf.toString().trim();
		return output;
	}

	public String getNomeCognome() {
		StringBuilder buf = new StringBuilder(128);
		if (isFilled(nome))
			buf.append(nome);
		if (isFilled(cognome))
			buf.append(" ").append(cognome);
		if (isFilled(userId))
			buf.append(" (").append(userId).append(')');

		String output = buf.toString().trim();
		return output;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = trimAndSet(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AnagrafeUtenteProfessionaleVO other = (AnagrafeUtenteProfessionaleVO) obj;

		if (this.cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!this.cognome.equals(other.cognome))
			return false;

		if (this.nome == null) {
			if (other.nome != null)
				return false;
		} else if (!this.nome.equals(other.nome))
			return false;

		if (this.getFlCanc() == null) {
			if (other.getFlCanc() != null)
				return false;
		} else if (!this.getFlCanc().equals(other.getFlCanc()))
			return false;

		return true;
	}

}
