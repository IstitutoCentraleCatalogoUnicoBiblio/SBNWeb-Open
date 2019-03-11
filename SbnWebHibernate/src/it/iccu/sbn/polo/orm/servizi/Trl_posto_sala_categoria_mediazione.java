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
package it.iccu.sbn.polo.orm.servizi;

import it.iccu.sbn.polo.orm.Tb_base;

public class Trl_posto_sala_categoria_mediazione extends Tb_base {

	private static final long serialVersionUID = 7035779716696207084L;

	private Tbl_posti_sala id_posto_sala;
	private String cd_cat_mediazione;

	protected int id_posto_sala_id;

	public Tbl_posti_sala getId_posto_sala() {
		return id_posto_sala;
	}

	public void setId_posto_sala(Tbl_posti_sala id_posto_sala) {
		this.id_posto_sala = id_posto_sala;
		if (id_posto_sala != null)
			this.id_posto_sala_id = id_posto_sala.getId_posti_sala();
	}

	public String getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(String cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cd_cat_mediazione == null) ? 0 : cd_cat_mediazione.hashCode());
		result = prime * result + id_posto_sala_id;
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
		Trl_posto_sala_categoria_mediazione other = (Trl_posto_sala_categoria_mediazione) obj;
		if (cd_cat_mediazione == null) {
			if (other.cd_cat_mediazione != null) {
				return false;
			}
		} else if (!cd_cat_mediazione.equals(other.cd_cat_mediazione)) {
			return false;
		}
		if (id_posto_sala_id != other.id_posto_sala_id) {
			return false;
		}
		return true;
	}

}
