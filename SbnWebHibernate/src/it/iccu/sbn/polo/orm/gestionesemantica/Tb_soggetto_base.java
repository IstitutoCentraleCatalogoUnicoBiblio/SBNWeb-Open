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
package it.iccu.sbn.polo.orm.gestionesemantica;

import it.iccu.sbn.polo.orm.Tb_base;

public class Tb_soggetto_base extends Tb_base {


	private static final long serialVersionUID = -8197461749169476055L;
	protected String cd_soggettario;
	protected Character cd_edizione;
	protected char fl_condiviso;

	public Tb_soggetto_base() {
		super();
	}

	/**
	 * codice del tipo di soggettario
	 */
	public void setCd_soggettario(String value) {
		this.cd_soggettario = value;
	}

	/**
	 * codice del tipo di soggettario
	 */
	public String getCd_soggettario() {
		return cd_soggettario;
	}

	public Character getCd_edizione() {
		return cd_edizione;
	}

	public void setCd_edizione(Character cd_edizione) {
		this.cd_edizione = cd_edizione;
	}

	/**
	 * indicatore di descrizione acquisita a livello di polo (cioe' non accettati a livello di indice); ammette i valori: 1 = si, 0 = no
	 */
	public void setFl_condiviso(char value) {
		this.fl_condiviso = value;
	}

	/**
	 * indicatore di descrizione acquisita a livello di polo (cioe' non accettati a livello di indice); ammette i valori: 1 = si, 0 = no
	 */
	public char getFl_condiviso() {
		return fl_condiviso;
	}

}
