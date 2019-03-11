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
package it.iccu.sbn.ejb.vo.gestionesemantica.stampe;

public class ParametriStampaTerminiThesauroVO extends ParametriStampaVO {

	private static final long serialVersionUID = -4344966757534814303L;
	private String codThesauro;
	private String descrizioneThesauro;
	private boolean soloTerminiBiblioteca;
	private boolean stampaTerminiCollegati;
	private boolean stampaNoteTerminiCollegati;
	private boolean stampaFormeRinvio;
	private boolean soloLegamiTitoloUtilizzati;
	private boolean soloLegamiTitoloInseritiDaBiblioteca;

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public boolean isSoloTerminiBiblioteca() {
		return soloTerminiBiblioteca;
	}

	public void setSoloTerminiBiblioteca(boolean soloTerminiBiblioteca) {
		this.soloTerminiBiblioteca = soloTerminiBiblioteca;
	}

	public boolean isStampaTerminiCollegati() {
		return stampaTerminiCollegati;
	}

	public void setStampaTerminiCollegati(boolean stampaTerminiCollegati) {
		this.stampaTerminiCollegati = stampaTerminiCollegati;
	}

	public String getDescrizioneThesauro() {
		return descrizioneThesauro;
	}

	public void setDescrizioneThesauro(String descrizioneThesauro) {
		this.descrizioneThesauro = descrizioneThesauro;
	}

	public boolean isStampaFormeRinvio() {
		return stampaFormeRinvio;
	}

	public void setStampaFormeRinvio(boolean stampaFormeRinvio) {
		this.stampaFormeRinvio = stampaFormeRinvio;
	}

	public boolean isSoloLegamiTitoloInseritiDaBiblioteca() {
		return soloLegamiTitoloInseritiDaBiblioteca;
	}

	public void setSoloLegamiTitoloInseritiDaBiblioteca(
			boolean soloLegamiTitoloInseritiDaBiblioteca) {
		this.soloLegamiTitoloInseritiDaBiblioteca = soloLegamiTitoloInseritiDaBiblioteca;
	}

	public boolean isStampaNoteTerminiCollegati() {
		return stampaNoteTerminiCollegati;
	}

	public void setStampaNoteTerminiCollegati(boolean stampaNoteTerminiCollegati) {
		this.stampaNoteTerminiCollegati = stampaNoteTerminiCollegati;
	}

	public boolean isSoloLegamiTitoloUtilizzati() {
		return soloLegamiTitoloUtilizzati;
	}

	public void setSoloLegamiTitoloUtilizzati(boolean soloLegamiTitoloUtilizzati) {
		this.soloLegamiTitoloUtilizzati = soloLegamiTitoloUtilizzati;
	}

}
