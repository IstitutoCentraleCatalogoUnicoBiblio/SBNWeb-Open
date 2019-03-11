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
package it.iccu.sbn.ejb.vo.servizi.occupazioni;

import it.iccu.sbn.ejb.vo.BaseVO;

public class OccupazioneVO extends BaseVO {

	private static final long serialVersionUID = 8332678510930805756L;
	private int progressivo;
	private int idOccupazioni;
	private boolean newOccupazione;
	private String codPolo;
	private String codBiblioteca;
	private String professione;
	private String codOccupazione;
	private String desOccupazione;
	//descrizione professione+"-"+descrizione occupazione
	private String comboDescrizione;

	public OccupazioneVO() {
		super();
	}


	public OccupazioneVO(String codBiblioteca, String professione, String codOccupazione, String desOccupazione) {
		super();
		this.codBiblioteca = codBiblioteca;
		this.professione = professione;
		this.codOccupazione = trimAndSet(codOccupazione);
		this.desOccupazione = trimAndSet(desOccupazione);
	}

	public void clearOccupazioni() {
		this.codBiblioteca = "";
		this.professione = "";
		this.codOccupazione = "";
		this.desOccupazione = "";
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public String getCodOccupazione() {
		return codOccupazione;
	}

	public void setCodOccupazione(String codOccupazione) {
		this.codOccupazione = trimAndSet(codOccupazione);
	}

	public String getProfessione() {
		return professione;
	}

	public void setProfessione(String professione) {
		this.professione = trimAndSet(professione);
	}

	public String getDesOccupazione() {
		return desOccupazione;
	}

	public void setDesOccupazione(String desOccupazione) {
		this.desOccupazione = trimAndSet(desOccupazione);
	}

	public boolean isNewOccupazione() {
		return newOccupazione;
	}

	public void setNewOccupazione(boolean newOccupazione) {
		this.newOccupazione = newOccupazione;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public int getIdOccupazioni() {
		return idOccupazioni;
	}

	public void setIdOccupazioni(int idOccupazioni) {
		this.idOccupazioni = idOccupazioni;
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

	public String getDescrizioneProfessione()	{
		int k = (this.comboDescrizione!=null ? this.comboDescrizione.indexOf("-") : -1);
		if (k!=-1) {
			return this.comboDescrizione.substring(0, k);
		} else return null;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final OccupazioneVO other = (OccupazioneVO)obj;

		if (codBiblioteca == null) {
			if (other.codBiblioteca != null) return false;
		} else if (!codBiblioteca.equals(other.codBiblioteca)) return false;

		if (professione == null) {
			if (other.professione != null) return false;
		} else if (!professione.equals(other.professione)) return false;

		if (codOccupazione == null) {
			if (other.codOccupazione != null) return false;
		} else if (!codOccupazione.trim().toUpperCase().equals(other.codOccupazione.trim().toUpperCase())) return false;

		if (desOccupazione == null) {
			if (other.desOccupazione != null) return false;
		} else if (!desOccupazione.equals(other.desOccupazione)) return false;

		return true;
	}

}


