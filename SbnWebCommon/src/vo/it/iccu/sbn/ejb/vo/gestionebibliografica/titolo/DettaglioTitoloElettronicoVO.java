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
//	almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.BaseVO;

public class DettaglioTitoloElettronicoVO extends BaseVO {

	private static final long serialVersionUID = 6335758602927092000L;

	private String livAutSpec;

	private String tipoRisorsaElettronica;
	private String indicazioneSpecificaMateriale;
	private String coloreElettronico;
	private String dimensioniElettronico;
	private String suonoElettronico;


	public DettaglioTitoloElettronicoVO() {
		super();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloElettronicoVO other = (DettaglioTitoloElettronicoVO) obj;

		if (tipoRisorsaElettronica == null) {
			if (other.tipoRisorsaElettronica != null)
				return false;
		} else if (!tipoRisorsaElettronica.equals(other.tipoRisorsaElettronica))
			return false;
		if (indicazioneSpecificaMateriale == null) {
			if (other.indicazioneSpecificaMateriale != null)
				return false;
		} else if (!indicazioneSpecificaMateriale.equals(other.indicazioneSpecificaMateriale))
			return false;
		if (coloreElettronico == null) {
			if (other.coloreElettronico != null)
				return false;
		} else if (!coloreElettronico.equals(other.coloreElettronico))
			return false;
		if (dimensioniElettronico == null) {
			if (other.dimensioniElettronico != null)
				return false;
		} else if (!dimensioniElettronico.equals(other.dimensioniElettronico))
			return false;
		if (suonoElettronico == null) {
			if (other.suonoElettronico != null)
				return false;
		} else if (!suonoElettronico.equals(other.suonoElettronico))
			return false;


		return true;
	}


	public String getLivAutSpec() {
		return livAutSpec;
	}


	public void setLivAutSpec(String livAutSpec) {
		this.livAutSpec = livAutSpec;
	}


	public String getTipoRisorsaElettronica() {
		return tipoRisorsaElettronica;
	}


	public void setTipoRisorsaElettronica(String tipoRisorsaElettronica) {
		this.tipoRisorsaElettronica = tipoRisorsaElettronica;
	}


	public String getIndicazioneSpecificaMateriale() {
		return indicazioneSpecificaMateriale;
	}


	public void setIndicazioneSpecificaMateriale(
			String indicazioneSpecificaMateriale) {
		this.indicazioneSpecificaMateriale = indicazioneSpecificaMateriale;
	}


	public String getColoreElettronico() {
		return coloreElettronico;
	}


	public void setColoreElettronico(String coloreElettronico) {
		this.coloreElettronico = coloreElettronico;
	}


	public String getDimensioniElettronico() {
		return dimensioniElettronico;
	}


	public void setDimensioniElettronico(String dimensioniElettronico) {
		this.dimensioniElettronico = dimensioniElettronico;
	}


	public String getSuonoElettronico() {
		return suonoElettronico;
	}


	public void setSuonoElettronico(String suonoElettronico) {
		this.suonoElettronico = suonoElettronico;
	}


}
