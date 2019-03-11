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
package it.iccu.sbn.polo.orm.bibliografica;

import it.iccu.sbn.polo.orm.Tb_base;

import java.math.BigInteger;

public class Tr_bid_altroid extends Tb_base {

	private static final long serialVersionUID = -5246502695635795556L;

	private int id;

	private Tb_titolo titolo;
	private String cd_istituzione;
	private BigInteger ist_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tb_titolo getTitolo() {
		return titolo;
	}

	public void setTitolo(Tb_titolo titolo) {
		this.titolo = titolo;
	}

	public String getCd_istituzione() {
		return cd_istituzione;
	}

	public void setCd_istituzione(String cd_istituzione) {
		this.cd_istituzione = cd_istituzione;
	}

	public BigInteger getIst_id() {
		return ist_id;
	}

	public void setIst_id(BigInteger ist_id) {
		this.ist_id = ist_id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tr_bid_altroid [");
		if (titolo != null)
			builder.append("titolo=").append(titolo).append(", ");
		if (cd_istituzione != null)
			builder.append("cd_istituzione=").append(cd_istituzione).append(", ");
		if (ist_id != null)
			builder.append("ist_id=").append(ist_id);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cd_istituzione == null) ? 0 : cd_istituzione.hashCode());
		result = prime * result + ((ist_id == null) ? 0 : ist_id.hashCode());
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tr_bid_altroid other = (Tr_bid_altroid) obj;
		if (cd_istituzione == null) {
			if (other.cd_istituzione != null) {
				return false;
			}
		} else if (!cd_istituzione.equals(other.cd_istituzione)) {
			return false;
		}
		if (ist_id == null) {
			if (other.ist_id != null) {
				return false;
			}
		} else if (!ist_id.equals(other.ist_id)) {
			return false;
		}
		if (titolo == null) {
			if (other.titolo != null) {
				return false;
			}
		} else if (!titolo.equals(other.titolo)) {
			return false;
		}
		return true;
	}



}
