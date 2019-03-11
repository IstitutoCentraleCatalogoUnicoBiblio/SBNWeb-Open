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
package it.iccu.sbn.ejb.vo.gestionesemantica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public abstract class DettaglioOggettoSemanticaVO extends SerializableVO {

	private static final long serialVersionUID = -1781130852844518317L;
	private boolean condiviso;
	private String T005;
	private String livAut;
	private boolean livelloPolo;
	private String testo;
	private String note;

	private String dataAgg;
	private String dataIns;

	private short rank;

	public String getDataAgg() {
		return dataAgg;
	}

	public short getRank() {
		return rank;
	}

	public void setRank(short rank) {
		this.rank = rank;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isLivelloPolo() {
		return livelloPolo;
	}

	public void setLivelloPolo(boolean livelloPolo) {
		this.livelloPolo = livelloPolo;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}

	public DettaglioOggettoSemanticaVO() {
		super();
	}

}
