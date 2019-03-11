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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;


public class ProfessioniVO extends BaseVO {

	private static final long serialVersionUID = -4986377349031158543L;
	private String matricola;
	private Character personaGiuridica = 'N';
	private String corsoLaurea;
	private String referente;
	private String tipoPersona;
	private String ateneo;
	private String professione;
	private String titoloStudio;
	private String idOccupazione;
	private String idSpecTitoloStudio;
	private List<MateriaVO> materie = new ArrayList<MateriaVO>();

	public ProfessioniVO() {
		this.clear();
		this.materie.clear();
	}

	public void clear() {
		this.ateneo = "";
		this.matricola = "";
		this.personaGiuridica = 'N';
		this.corsoLaurea = "";
		this.referente = "";
		this.tipoPersona = "";
		this.ateneo = "";
		this.professione = "";
//		this.occupazione = "";
		this.titoloStudio = "";
//		this.specTitoloStudio = "";
	}

	public String getAteneo() {
		return ateneo;
	}

	public void setAteneo(String ateneo) {
		this.ateneo = trimAndSet(ateneo);
	}

	public String getCorsoLaurea() {
		return corsoLaurea;
	}

	public void setCorsoLaurea(String corsoLaurea) {
		this.corsoLaurea = trimAndSet(corsoLaurea);
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = trimAndSet(matricola);
	}

	public Character getPersonaGiuridica() {
		return personaGiuridica;
	}

	public void setPersonaGiuridica(Character personaGiuridica) {
		this.personaGiuridica = personaGiuridica;
	}

	public void setMaterie(List<MateriaVO> materie) {
		this.materie = materie;
	}

	public String getReferente() {
		return referente;
	}

	public void setReferente(String referente) {
		this.referente = referente;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public List<MateriaVO> getMaterie() {
		return materie;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProfessioniVO other = (ProfessioniVO) obj;
		if (ateneo == null) {
			if (other.ateneo != null)
				return false;
		} else if (!ateneo.equals(other.ateneo))
			return false;
		if (corsoLaurea == null) {
			if (other.corsoLaurea != null)
				return false;
		} else if (!corsoLaurea.equals(other.corsoLaurea))
			return false;
		if (materie == null) {
			if (other.materie != null)
				return false;
		} else if (!listEquals(this.materie, other.materie, MateriaVO.class))
			return false;
		if (matricola == null) {
			if (other.matricola != null)
				return false;
		} else if (!matricola.equals(other.matricola))
			return false;
		if (idOccupazione == null) {
			if (other.idOccupazione != null)
				return false;
		} else if (!idOccupazione.equals(other.idOccupazione))
			return false;
//		if (personaGiuridica != other.personaGiuridica)
//			return false;
		if (professione == null) {
			if (other.professione != null)
				return false;
		} else if (!professione.equals(other.professione))
			return false;
		if (referente == null) {
			if (other.referente != null)
				return false;
		} else if (!referente.equals(other.referente))
			return false;
		if (idSpecTitoloStudio == null) {
			if (other.idSpecTitoloStudio != null)
				return false;
		} else if (!idSpecTitoloStudio.equals(other.idSpecTitoloStudio))
			return false;
		if (tipoPersona == null) {
			if (other.tipoPersona != null)
				return false;
		} else if (!tipoPersona.equals(other.tipoPersona))
			return false;
		if (titoloStudio == null) {
			if (other.titoloStudio != null)
				return false;
		} else if (!titoloStudio.equals(other.titoloStudio))
			return false;
		return true;
	}

	public String getIdOccupazione() {
		return idOccupazione;
	}

	public void setIdOccupazione(String idOccupazione) {
		this.idOccupazione = idOccupazione;
	}

	public String getIdSpecTitoloStudio() {
		return idSpecTitoloStudio;
	}

	public void setIdSpecTitoloStudio(String idSpecTitoloStudio) {
		this.idSpecTitoloStudio = idSpecTitoloStudio;
	}

	public String getProfessione() {
		return professione;
	}

	public void setProfessione(String professione) {
		this.professione = professione;
	}

	public String getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(String titoloStudio) {
		this.titoloStudio = titoloStudio;
	}

	public boolean isEnte() {
		return ValidazioneDati.in(personaGiuridica, 'S', 's');
	}

	public boolean isPersonaFisica() {
		return !isEnte();
	}

}
