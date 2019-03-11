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
package it.iccu.sbn.vo.custom.profilo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.HashMap;
import java.util.Map;

public class AttivitaBibliotecario extends SerializableVO {

	private static final long serialVersionUID = -3511266029442882151L;
	private String codAttivita;
	private String attivita;
	private Map<String, AttivitaBibliotecario> sottoAttivita = new HashMap<String, AttivitaBibliotecario>();

	public AttivitaBibliotecario(String codAttivita, String attivita) {
		this.codAttivita = codAttivita;
		this.attivita = attivita;
	}

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

	public Map<String, AttivitaBibliotecario> getSottoAttivita() {
		return sottoAttivita;
	}

	public void addSottoAttivita(AttivitaBibliotecario sottoAttivita) {
		this.sottoAttivita.put(sottoAttivita.getCodAttivita(), sottoAttivita);
	}
}
