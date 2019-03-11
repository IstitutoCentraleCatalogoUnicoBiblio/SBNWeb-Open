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
package it.iccu.sbn.ejb.vo.servizi.spectitolostudio;

import it.iccu.sbn.ejb.vo.BaseVO;

public class SpecTitoloStudioVO extends BaseVO {

	private static final long serialVersionUID = 622952242639663724L;
	public static boolean NEW = true;
	public static boolean OLD = false;
	private int idTitoloStudio;
	private int progressivo;
	private boolean newSpecialita;
	private String codPolo;
	private String codBiblioteca;
	private String titoloStudio;
	private String codSpecialita;
	private String desSpecialita;

	// stringa composta da: descrizione titolo di studio + "-" + descrizione
	// specificità titolo di studio
	private String comboDescrizione;

	public SpecTitoloStudioVO() {
		super();
	}

	public SpecTitoloStudioVO(String codBiblioteca, String titoloStudio,
			String codSpecialita, String desSpecialita) {
		super();
		this.codBiblioteca = codBiblioteca;
		this.titoloStudio = titoloStudio;
		this.codSpecialita = codSpecialita;
		this.desSpecialita = desSpecialita;
	}

	public void clearTdS() {
		this.titoloStudio = "";
		this.codSpecialita = "";
		this.desSpecialita = "";
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public int getIdTitoloStudio() {
		return idTitoloStudio;
	}

	public void setIdTitoloStudio(int idTitoloStudio) {
		this.idTitoloStudio = idTitoloStudio;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getComboDescrizione() {
		return comboDescrizione;
	}

	public void setComboDescrizione(String comboDescrizione) {
		this.comboDescrizione = comboDescrizione;
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public String getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(String titoloStudio) {
		this.titoloStudio = titoloStudio;
	}

	public String getCodSpecialita() {
		return codSpecialita;
	}

	public void setCodSpecialita(String codSpecialita) {
		this.codSpecialita = codSpecialita;
	}

	public String getDesSpecialita() {
		return desSpecialita;
	}

	public void setDesSpecialita(String desSpecialita) {
		this.desSpecialita = desSpecialita;
	}

	public boolean isNewSpecialita() {
		return newSpecialita;
	}

	public void setNewSpecialita(boolean newSpecialita) {
		this.newSpecialita = newSpecialita;
	}

	public String getDescrizioneTitoloStudio() {
		int k = (this.comboDescrizione != null ? this.comboDescrizione
				.indexOf("-") : -1);
		if (k != -1) {
			return this.comboDescrizione.substring(0, k);
		} else
			return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SpecTitoloStudioVO other = (SpecTitoloStudioVO) obj;

		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;

		if (codBiblioteca == null) {
			if (other.codBiblioteca != null)
				return false;
		} else if (!codBiblioteca.equals(other.codBiblioteca))
			return false;

		if (titoloStudio == null) {
			if (other.titoloStudio != null)
				return false;
		} else if (!titoloStudio.equals(other.titoloStudio))
			return false;

		if (codSpecialita == null) {
			if (other.codSpecialita != null)
				return false;
		} else if (!codSpecialita.trim().toUpperCase().equals(other.codSpecialita.trim().toUpperCase()))
			return false;

		if (desSpecialita == null) {
			if (other.desSpecialita != null)
				return false;
		} else if (!desSpecialita.equals(other.desSpecialita))
			return false;

		return true;
	}
}
