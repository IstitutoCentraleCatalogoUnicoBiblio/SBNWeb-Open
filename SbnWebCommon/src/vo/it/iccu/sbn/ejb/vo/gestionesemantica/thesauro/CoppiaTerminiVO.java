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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.SerializableVO;

public abstract class CoppiaTerminiVO extends SerializableVO {

	private static final long serialVersionUID = -4821220952777521854L;
	private DettaglioTermineThesauroVO did1 = new DettaglioTermineThesauroVO();
	private DettaglioTermineThesauroVO did2 = new DettaglioTermineThesauroVO();
	private boolean livelloPolo = true;

	public CoppiaTerminiVO() {
		super();
	}

	public DettaglioTermineThesauroVO getDid1() {
		return did1;
	}

	public void setDid1(DettaglioTermineThesauroVO did1) {
		this.did1 = did1;
	}

	public DettaglioTermineThesauroVO getDid2() {
		return did2;
	}

	public void setDid2(DettaglioTermineThesauroVO did2) {
		this.did2 = did2;
	}

	public boolean isLivelloPolo() {
		return livelloPolo;
	}

	public void setLivelloPolo(boolean livelloPolo) {
		this.livelloPolo = livelloPolo;
	}

}
