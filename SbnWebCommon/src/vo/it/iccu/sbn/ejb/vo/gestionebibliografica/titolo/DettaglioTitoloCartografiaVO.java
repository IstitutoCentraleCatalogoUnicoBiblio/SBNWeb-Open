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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class DettaglioTitoloCartografiaVO  extends SerializableVO {

	// = DettaglioTitoloCartografiaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 8198072161364450009L;

	private String livAutSpec;

	private String meridianoOrigine;
	private String longitTipo1;
	private String longitValEO1;
	private String longitTipo2;
	private String longitValEO2;
	private String latitTipo1;
	private String latitValNS1;
	private String latitTipo2;
	private String latitValNS2;

	private String scalaOriz;
	private String scalaVert;

	private String pubblicazioneGovernativa;
	private String indicatoreColore;
	private String supportoFisico;
	private String tecnicaCreazione;
	private String formaRiproduzione;
	private String formaPubblicazione;
	private String altitudine;
	private String indicatoreTipoScala;
	private String tipoScala;
	private String carattereImmagine;
	private String forma;
	private String piattaforma;
	private String categoriaSatellite;
	// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
	private String proiezioneCarte;


	public String getLatitTipo1() {
		return latitTipo1;
	}
	public void setLatitTipo1(String latitTipo1) {
		this.latitTipo1 = latitTipo1;
	}
	public String getLatitTipo2() {
		return latitTipo2;
	}
	public void setLatitTipo2(String latitTipo2) {
		this.latitTipo2 = latitTipo2;
	}
	public String getLongitTipo1() {
		return longitTipo1;
	}
	public void setLongitTipo1(String longitTipo1) {
		this.longitTipo1 = longitTipo1;
	}
	public String getLongitTipo2() {
		return longitTipo2;
	}
	public void setLongitTipo2(String longitTipo2) {
		this.longitTipo2 = longitTipo2;
	}
	public String getLatitValNS1() {
		return latitValNS1;
	}
	public void setLatitValNS1(String latitValNS1) {
		this.latitValNS1 = latitValNS1;
	}
	public String getLatitValNS2() {
		return latitValNS2;
	}
	public void setLatitValNS2(String latitValNS2) {
		this.latitValNS2 = latitValNS2;
	}
	public String getLongitValEO1() {
		return longitValEO1;
	}
	public void setLongitValEO1(String longitValEO1) {
		this.longitValEO1 = longitValEO1;
	}
	public String getLongitValEO2() {
		return longitValEO2;
	}
	public void setLongitValEO2(String longitValEO2) {
		this.longitValEO2 = longitValEO2;
	}
	public String getMeridianoOrigine() {
		return meridianoOrigine;
	}
	public void setMeridianoOrigine(String meridianoOrigine) {
		this.meridianoOrigine = meridianoOrigine;
	}
	public String getTipoScala() {
		return tipoScala;
	}
	public void setTipoScala(String tipoScala) {
		this.tipoScala = tipoScala;
	}
	public String getAltitudine() {
		return altitudine;
	}
	public void setAltitudine(String altitudine) {
		this.altitudine = altitudine;
	}
	public String getCarattereImmagine() {
		return carattereImmagine;
	}
	public void setCarattereImmagine(String carattereImmagine) {
		this.carattereImmagine = carattereImmagine;
	}
	public String getCategoriaSatellite() {
		return categoriaSatellite;
	}
	public void setCategoriaSatellite(String categoriaSatellite) {
		this.categoriaSatellite = categoriaSatellite;
	}


	public String getProiezioneCarte() {
		return proiezioneCarte;
	}
	public void setProiezioneCarte(String proiezioneCarte) {
		this.proiezioneCarte = proiezioneCarte;
	}
	public String getForma() {
		return forma;
	}
	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getFormaPubblicazione() {
		return formaPubblicazione;
	}
	public void setFormaPubblicazione(String formaPubblicazione) {
		this.formaPubblicazione = formaPubblicazione;
	}
	public String getFormaRiproduzione() {
		return formaRiproduzione;
	}
	public void setFormaRiproduzione(String formaRiproduzione) {
		this.formaRiproduzione = formaRiproduzione;
	}
	public String getIndicatoreColore() {
		return indicatoreColore;
	}
	public void setIndicatoreColore(String indicatoreColore) {
		this.indicatoreColore = indicatoreColore;
	}
	public String getIndicatoreTipoScala() {
		return indicatoreTipoScala;
	}
	public void setIndicatoreTipoScala(String indicatoreTipoScala) {
		this.indicatoreTipoScala = indicatoreTipoScala;
	}
	public String getPiattaforma() {
		return piattaforma;
	}
	public void setPiattaforma(String piattaforma) {
		this.piattaforma = piattaforma;
	}
	public String getPubblicazioneGovernativa() {
		return pubblicazioneGovernativa;
	}
	public void setPubblicazioneGovernativa(String pubblicazioneGovernativa) {
		this.pubblicazioneGovernativa = pubblicazioneGovernativa;
	}
	public String getSupportoFisico() {
		return supportoFisico;
	}
	public void setSupportoFisico(String supportoFisico) {
		this.supportoFisico = supportoFisico;
	}
	public String getTecnicaCreazione() {
		return tecnicaCreazione;
	}
	public void setTecnicaCreazione(String tecnicaCreazione) {
		this.tecnicaCreazione = tecnicaCreazione;
	}
	public String getScalaOriz() {
		return scalaOriz;
	}
	public void setScalaOriz(String scalaOriz) {
		this.scalaOriz = scalaOriz;
	}
	public String getScalaVert() {
		return scalaVert;
	}
	public void setScalaVert(String scalaVert) {
		this.scalaVert = scalaVert;
	}
	@Override
	public Object clone() {
		DettaglioTitoloCartografiaVO inst = new DettaglioTitoloCartografiaVO();
		inst.meridianoOrigine = this.meridianoOrigine == null
			? null
			: new String(this.meridianoOrigine);
		inst.longitTipo1 = this.longitTipo1 == null ? null : new String(
			this.longitTipo1);
		inst.longitValEO1 = this.longitValEO1 == null ? null : new String(
			this.longitValEO1);
		inst.longitTipo2 = this.longitTipo2 == null ? null : new String(
			this.longitTipo2);
		inst.longitValEO2 = this.longitValEO2 == null ? null : new String(
			this.longitValEO2);
		inst.latitTipo1 = this.latitTipo1 == null ? null : new String(
			this.latitTipo1);
		inst.latitValNS1 = this.latitValNS1 == null ? null : new String(
			this.latitValNS1);
		inst.latitTipo2 = this.latitTipo2 == null ? null : new String(
			this.latitTipo2);
		inst.latitValNS2 = this.latitValNS2 == null ? null : new String(
			this.latitValNS2);
		inst.tipoScala = this.tipoScala == null ? null : new String(
			this.tipoScala);
		inst.scalaOriz = this.scalaOriz == null ? null : new String(
			this.scalaOriz);
		inst.scalaVert = this.scalaVert == null ? null : new String(
			this.scalaVert);
		inst.pubblicazioneGovernativa = this.pubblicazioneGovernativa == null
			? null
			: new String(this.pubblicazioneGovernativa);
		inst.indicatoreColore = this.indicatoreColore == null
			? null
			: new String(this.indicatoreColore);
		inst.supportoFisico = this.supportoFisico == null ? null : new String(
			this.supportoFisico);
		inst.tecnicaCreazione = this.tecnicaCreazione == null
			? null
			: new String(this.tecnicaCreazione);
		inst.formaRiproduzione = this.formaRiproduzione == null
			? null
			: new String(this.formaRiproduzione);
		inst.formaPubblicazione = this.formaPubblicazione == null
			? null
			: new String(this.formaPubblicazione);
		inst.altitudine = this.altitudine == null ? null : new String(
			this.altitudine);
		inst.indicatoreTipoScala = this.indicatoreTipoScala == null
			? null
			: new String(this.indicatoreTipoScala);
		inst.carattereImmagine = this.carattereImmagine == null
			? null
			: new String(this.carattereImmagine);
		inst.forma = this.forma == null ? null : new String(this.forma);
		inst.piattaforma = this.piattaforma == null ? null : new String(
			this.piattaforma);
		inst.categoriaSatellite = this.categoriaSatellite == null
			? null
			: new String(this.categoriaSatellite);

		// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
		inst.proiezioneCarte = this.proiezioneCarte == null
		? null
		: new String(this.proiezioneCarte);
		return inst;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCartografiaVO other = (DettaglioTitoloCartografiaVO) obj;
		if (altitudine == null) {
			if (other.altitudine != null)
				return false;
		} else if (!altitudine.equals(other.altitudine))
			return false;
		if (carattereImmagine == null) {
			if (other.carattereImmagine != null)
				return false;
		} else if (!carattereImmagine.equals(other.carattereImmagine))
			return false;
		if (categoriaSatellite == null) {
			if (other.categoriaSatellite != null)
				return false;
		} else if (!categoriaSatellite.equals(other.categoriaSatellite))
			return false;
		// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
		if (proiezioneCarte == null) {
			if (other.proiezioneCarte != null)
				return false;
		} else if (!proiezioneCarte.equals(other.proiezioneCarte))
			return false;

		if (forma == null) {
			if (other.forma != null)
				return false;
		} else if (!forma.equals(other.forma))
			return false;
		if (formaPubblicazione == null) {
			if (other.formaPubblicazione != null)
				return false;
		} else if (!formaPubblicazione.equals(other.formaPubblicazione))
			return false;
		if (formaRiproduzione == null) {
			if (other.formaRiproduzione != null)
				return false;
		} else if (!formaRiproduzione.equals(other.formaRiproduzione))
			return false;
		if (indicatoreColore == null) {
			if (other.indicatoreColore != null)
				return false;
		} else if (!indicatoreColore.equals(other.indicatoreColore))
			return false;
		if (indicatoreTipoScala == null) {
			if (other.indicatoreTipoScala != null)
				return false;
		} else if (!indicatoreTipoScala.equals(other.indicatoreTipoScala))
			return false;
		if (latitTipo1 == null) {
			if (other.latitTipo1 != null)
				return false;
		} else if (!latitTipo1.equals(other.latitTipo1))
			return false;
		if (latitTipo2 == null) {
			if (other.latitTipo2 != null)
				return false;
		} else if (!latitTipo2.equals(other.latitTipo2))
			return false;
		if (latitValNS1 == null) {
			if (other.latitValNS1 != null)
				return false;
		} else if (!latitValNS1.equals(other.latitValNS1))
			return false;
		if (latitValNS2 == null) {
			if (other.latitValNS2 != null)
				return false;
		} else if (!latitValNS2.equals(other.latitValNS2))
			return false;
		if (longitTipo1 == null) {
			if (other.longitTipo1 != null)
				return false;
		} else if (!longitTipo1.equals(other.longitTipo1))
			return false;
		if (longitTipo2 == null) {
			if (other.longitTipo2 != null)
				return false;
		} else if (!longitTipo2.equals(other.longitTipo2))
			return false;
		if (longitValEO1 == null) {
			if (other.longitValEO1 != null)
				return false;
		} else if (!longitValEO1.equals(other.longitValEO1))
			return false;
		if (longitValEO2 == null) {
			if (other.longitValEO2 != null)
				return false;
		} else if (!longitValEO2.equals(other.longitValEO2))
			return false;
		if (meridianoOrigine == null) {
			if (other.meridianoOrigine != null)
				return false;
		} else if (!meridianoOrigine.equals(other.meridianoOrigine))
			return false;
		if (piattaforma == null) {
			if (other.piattaforma != null)
				return false;
		} else if (!piattaforma.equals(other.piattaforma))
			return false;
		if (pubblicazioneGovernativa == null) {
			if (other.pubblicazioneGovernativa != null)
				return false;
		} else if (!pubblicazioneGovernativa.equals(other.pubblicazioneGovernativa))
			return false;
		if (scalaOriz == null) {
			if (other.scalaOriz != null)
				return false;
		} else if (!scalaOriz.equals(other.scalaOriz))
			return false;
		if (scalaVert == null) {
			if (other.scalaVert != null)
				return false;
		} else if (!scalaVert.equals(other.scalaVert))
			return false;
		if (supportoFisico == null) {
			if (other.supportoFisico != null)
				return false;
		} else if (!supportoFisico.equals(other.supportoFisico))
			return false;
		if (tecnicaCreazione == null) {
			if (other.tecnicaCreazione != null)
				return false;
		} else if (!tecnicaCreazione.equals(other.tecnicaCreazione))
			return false;
		if (tipoScala == null) {
			if (other.tipoScala != null)
				return false;
		} else if (!tipoScala.equals(other.tipoScala))
			return false;
		return true;
	}
	public String getLivAutSpec() {
		return livAutSpec;
	}
	public void setLivAutSpec(String livAutSpec) {
		this.livAutSpec = livAutSpec;
	}


}
