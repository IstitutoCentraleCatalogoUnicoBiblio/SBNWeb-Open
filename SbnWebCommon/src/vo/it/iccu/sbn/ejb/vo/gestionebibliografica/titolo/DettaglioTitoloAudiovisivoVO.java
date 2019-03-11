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
// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.BaseVO;

public class DettaglioTitoloAudiovisivoVO extends BaseVO {


	private static final long serialVersionUID = 8849046486733750801L;

	private String livAutSpec;

	private String tipoTestoRegSonora = "";

	private String tipoVideo = "";
	private String lunghezza = "";
	private String indicatoreColore = "";
	private String indicatoreSuono = "";
	private String supportoSuono = "";
	private String larghezzaDimensioni = "";
	private String formaPubblDistr = "";
	private String tecnicaVideoFilm = "";
	private String presentImmagMov = "";
	private String materAccompagn1 = "";
	private String materAccompagn2 = "";
	private String materAccompagn3 = "";
	private String materAccompagn4 = "";
	private String pubblicVideoreg = "";
	private String presentVideoreg = "";
	private String materialeEmulsBase = "";
	private String materialeSupportSec = "";
	private String standardTrasmiss = "";
	private String versioneAudiovid = "";
	private String elementiProd = "";
	private String specCatColoreFilm = "";
	private String emulsionePellic = "";
	private String composPellic = "";
	private String suonoImmagMovimento = "";
	private String tipoPellicStampa = "";

	private String formaPubblicazioneDisco = "";
	private String velocita = "";
	private String tipoSuono = "";
	private String larghezzaScanal = "";
	private String dimensioni = "";
	private String larghezzaNastro = "";
	private String configurazNastro = "";
	private String materTestAccompagn1 = "";
	private String materTestAccompagn2 = "";
	private String materTestAccompagn3 = "";
	private String materTestAccompagn4 = "";
	private String materTestAccompagn5 = "";
	private String materTestAccompagn6 = "";
	private String tecnicaRegistraz = "";
	private String specCarattRiprod = "";
	private String datiCodifRegistrazSonore = "";
	private String tipoDiMateriale = "";
	private String tipoDiTaglio = "";
	private String durataRegistraz = "";


	public DettaglioTitoloAudiovisivoVO() {
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
		final DettaglioTitoloAudiovisivoVO other = (DettaglioTitoloAudiovisivoVO) obj;
		if (tipoVideo == null) {
			if (other.tipoVideo != null)
				return false;
		} else if (!tipoVideo.equals(other.tipoVideo))
			return false;

		if (tipoTestoRegSonora == null) {
			if (other.tipoTestoRegSonora != null)
				return false;
		} else if (!tipoTestoRegSonora.equals(other.tipoTestoRegSonora))
			return false;

		if (lunghezza == null) {
			if (other.lunghezza != null)
				return false;
		} else if (!lunghezza.equals(other.lunghezza))
			return false;
		if (indicatoreColore == null) {
			if (other.indicatoreColore != null)
				return false;
		} else if (!indicatoreColore.equals(other.indicatoreColore))
			return false;
		if (indicatoreSuono == null) {
			if (other.indicatoreSuono != null)
				return false;
		} else if (!indicatoreSuono.equals(other.indicatoreSuono))
			return false;
		if (supportoSuono == null) {
			if (other.supportoSuono != null)
				return false;
		} else if (!supportoSuono.equals(other.supportoSuono))
			return false;
		if (larghezzaDimensioni == null) {
			if (other.larghezzaDimensioni != null)
				return false;
		} else if (!larghezzaDimensioni.equals(other.larghezzaDimensioni))
			return false;
		if (formaPubblDistr == null) {
			if (other.formaPubblDistr != null)
				return false;
		} else if (!formaPubblDistr.equals(other.formaPubblDistr))
			return false;
		if (tecnicaVideoFilm == null) {
			if (other.tecnicaVideoFilm != null)
				return false;
		} else if (!tecnicaVideoFilm.equals(other.tecnicaVideoFilm))
			return false;
		if (presentImmagMov == null) {
			if (other.presentImmagMov != null)
				return false;
		} else if (!presentImmagMov.equals(other.presentImmagMov))
			return false;
		if (materAccompagn1 == null) {
			if (other.materAccompagn1 != null)
				return false;
		} else if (!materAccompagn1.equals(other.materAccompagn1))
			return false;
		if (materAccompagn2 == null) {
			if (other.materAccompagn2 != null)
				return false;
		} else if (!materAccompagn2.equals(other.materAccompagn2))
			return false;
		if (materAccompagn3 == null) {
			if (other.materAccompagn3 != null)
				return false;
		} else if (!materAccompagn3.equals(other.materAccompagn3))
			return false;
		if (materAccompagn4 == null) {
			if (other.materAccompagn4 != null)
				return false;
		} else if (!materAccompagn4.equals(other.materAccompagn4))
			return false;
		if (pubblicVideoreg == null) {
			if (other.pubblicVideoreg != null)
				return false;
		} else if (!pubblicVideoreg.equals(other.pubblicVideoreg))
			return false;
		if (presentVideoreg == null) {
			if (other.presentVideoreg != null)
				return false;
		} else if (!presentVideoreg.equals(other.presentVideoreg))
			return false;
		if (materialeEmulsBase == null) {
			if (other.materialeEmulsBase != null)
				return false;
		} else if (!materialeEmulsBase.equals(other.materialeEmulsBase))
			return false;


		if (materialeSupportSec == null) {
			if (other.materialeSupportSec != null)
				return false;
		} else if (!materialeSupportSec.equals(other.materialeSupportSec))
			return false;
		if (standardTrasmiss == null) {
			if (other.standardTrasmiss != null)
				return false;
		} else if (!standardTrasmiss.equals(other.standardTrasmiss))
			return false;
		if (versioneAudiovid == null) {
			if (other.versioneAudiovid != null)
				return false;
		} else if (!versioneAudiovid.equals(other.versioneAudiovid))
			return false;
		if (elementiProd == null) {
			if (other.elementiProd != null)
				return false;
		} else if (!elementiProd.equals(other.elementiProd))
			return false;
		if (specCatColoreFilm == null) {
			if (other.specCatColoreFilm != null)
				return false;
		} else if (!specCatColoreFilm.equals(other.specCatColoreFilm))
			return false;
		if (emulsionePellic == null) {
			if (other.emulsionePellic != null)
				return false;
		} else if (!emulsionePellic.equals(other.emulsionePellic))
			return false;
		if (composPellic == null) {
			if (other.composPellic != null)
				return false;
		} else if (!composPellic.equals(other.composPellic))
			return false;
		if (suonoImmagMovimento == null) {
			if (other.suonoImmagMovimento != null)
				return false;
		} else if (!suonoImmagMovimento.equals(other.suonoImmagMovimento))
			return false;
		if (tipoPellicStampa == null) {
			if (other.tipoPellicStampa != null)
				return false;
		} else if (!tipoPellicStampa.equals(other.tipoPellicStampa))
			return false;

		if (formaPubblicazioneDisco == null) {
			if (other.formaPubblicazioneDisco != null)
				return false;
		} else if (!formaPubblicazioneDisco.equals(other.formaPubblicazioneDisco))
			return false;
		if (velocita == null) {
			if (other.velocita != null)
				return false;
		} else if (!velocita.equals(other.velocita))
			return false;
		if (tipoSuono == null) {
			if (other.tipoSuono != null)
				return false;
		} else if (!tipoSuono.equals(other.tipoSuono))
			return false;
		if (larghezzaScanal == null) {
			if (other.larghezzaScanal != null)
				return false;
		} else if (!larghezzaScanal.equals(other.larghezzaScanal))
			return false;
		if (dimensioni == null) {
			if (other.dimensioni != null)
				return false;
		} else if (!dimensioni.equals(other.dimensioni))
			return false;
		if (larghezzaNastro == null) {
			if (other.larghezzaNastro != null)
				return false;
		} else if (!larghezzaNastro.equals(other.larghezzaNastro))
			return false;
		if (configurazNastro == null) {
			if (other.configurazNastro != null)
				return false;
		} else if (!configurazNastro.equals(other.configurazNastro))
			return false;
		if (materTestAccompagn1 == null) {
			if (other.materTestAccompagn1 != null)
				return false;
		} else if (!materTestAccompagn1.equals(other.materTestAccompagn1))
			return false;
		if (materTestAccompagn2 == null) {
			if (other.materTestAccompagn2 != null)
				return false;
		} else if (!materTestAccompagn2.equals(other.materTestAccompagn2))
			return false;
		if (materTestAccompagn3 == null) {
			if (other.materTestAccompagn3 != null)
				return false;
		} else if (!materTestAccompagn3.equals(other.materTestAccompagn3))
			return false;
		if (materTestAccompagn4 == null) {
			if (other.materTestAccompagn4 != null)
				return false;
		} else if (!materTestAccompagn4.equals(other.materTestAccompagn4))
			return false;
		if (materTestAccompagn5 == null) {
			if (other.materTestAccompagn5 != null)
				return false;
		} else if (!materTestAccompagn5.equals(other.materTestAccompagn5))
			return false;
		if (materTestAccompagn6 == null) {
			if (other.materTestAccompagn6 != null)
				return false;
		} else if (!materTestAccompagn6.equals(other.materTestAccompagn6))
			return false;

		if (tecnicaRegistraz == null) {
			if (other.tecnicaRegistraz != null)
				return false;
		} else if (!tecnicaRegistraz.equals(other.tecnicaRegistraz))
			return false;
		if (specCarattRiprod == null) {
			if (other.specCarattRiprod != null)
				return false;
		} else if (!specCarattRiprod.equals(other.specCarattRiprod))
			return false;
		if (datiCodifRegistrazSonore == null) {
			if (other.datiCodifRegistrazSonore != null)
				return false;
		} else if (!datiCodifRegistrazSonore.equals(other.datiCodifRegistrazSonore))
			return false;
		if (tipoDiMateriale == null) {
			if (other.tipoDiMateriale != null)
				return false;
		} else if (!tipoDiMateriale.equals(other.tipoDiMateriale))
			return false;
		if (tipoDiTaglio == null) {
			if (other.tipoDiTaglio != null)
				return false;
		} else if (!tipoDiTaglio.equals(other.tipoDiTaglio))
			return false;
		if (durataRegistraz == null) {
			if (other.durataRegistraz != null)
				return false;
		} else if (!durataRegistraz.equals(other.durataRegistraz))
			return false;
		return true;
	}

	public String getLivAutSpec() {
		return livAutSpec;
	}

	public void setLivAutSpec(String livAutSpec) {
		this.livAutSpec = livAutSpec;
	}

	public String getTipoVideo() {
		return tipoVideo;
	}

	public void setTipoVideo(String tipoVideo) {
		this.tipoVideo = tipoVideo;
	}

	public String getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(String lunghezza) {
		this.lunghezza = lunghezza;
	}

	public String getIndicatoreColore() {
		return indicatoreColore;
	}

	public void setIndicatoreColore(String indicatoreColore) {
		this.indicatoreColore = indicatoreColore;
	}

	public String getIndicatoreSuono() {
		return indicatoreSuono;
	}

	public void setIndicatoreSuono(String indicatoreSuono) {
		this.indicatoreSuono = indicatoreSuono;
	}

	public String getSupportoSuono() {
		return supportoSuono;
	}

	public void setSupportoSuono(String supportoSuono) {
		this.supportoSuono = supportoSuono;
	}

	public String getLarghezzaDimensioni() {
		return larghezzaDimensioni;
	}

	public void setLarghezzaDimensioni(String larghezzaDimensioni) {
		this.larghezzaDimensioni = larghezzaDimensioni;
	}

	public String getFormaPubblDistr() {
		return formaPubblDistr;
	}

	public void setFormaPubblDistr(String formaPubblDistr) {
		this.formaPubblDistr = formaPubblDistr;
	}

	public String getTecnicaVideoFilm() {
		return tecnicaVideoFilm;
	}

	public void setTecnicaVideoFilm(String tecnicaVideoFilm) {
		this.tecnicaVideoFilm = tecnicaVideoFilm;
	}

	public String getPresentImmagMov() {
		return presentImmagMov;
	}

	public void setPresentImmagMov(String presentImmagMov) {
		this.presentImmagMov = presentImmagMov;
	}

	public String getPubblicVideoreg() {
		return pubblicVideoreg;
	}

	public void setPubblicVideoreg(String pubblicVideoreg) {
		this.pubblicVideoreg = pubblicVideoreg;
	}

	public String getPresentVideoreg() {
		return presentVideoreg;
	}

	public void setPresentVideoreg(String presentVideoreg) {
		this.presentVideoreg = presentVideoreg;
	}

	public String getMaterialeEmulsBase() {
		return materialeEmulsBase;
	}

	public void setMaterialeEmulsBase(String materialeEmulsBase) {
		this.materialeEmulsBase = materialeEmulsBase;
	}

	public String getMaterialeSupportSec() {
		return materialeSupportSec;
	}

	public void setMaterialeSupportSec(String materialeSupportSec) {
		this.materialeSupportSec = materialeSupportSec;
	}

	public String getStandardTrasmiss() {
		return standardTrasmiss;
	}

	public void setStandardTrasmiss(String standardTrasmiss) {
		this.standardTrasmiss = standardTrasmiss;
	}

	public String getVersioneAudiovid() {
		return versioneAudiovid;
	}

	public void setVersioneAudiovid(String versioneAudiovid) {
		this.versioneAudiovid = versioneAudiovid;
	}

	public String getElementiProd() {
		return elementiProd;
	}

	public void setElementiProd(String elementiProd) {
		this.elementiProd = elementiProd;
	}

	public String getSpecCatColoreFilm() {
		return specCatColoreFilm;
	}

	public void setSpecCatColoreFilm(String specCatColoreFilm) {
		this.specCatColoreFilm = specCatColoreFilm;
	}

	public String getEmulsionePellic() {
		return emulsionePellic;
	}

	public void setEmulsionePellic(String emulsionePellic) {
		this.emulsionePellic = emulsionePellic;
	}

	public String getComposPellic() {
		return composPellic;
	}

	public void setComposPellic(String composPellic) {
		this.composPellic = composPellic;
	}

	public String getSuonoImmagMovimento() {
		return suonoImmagMovimento;
	}

	public void setSuonoImmagMovimento(String suonoImmagMovimento) {
		this.suonoImmagMovimento = suonoImmagMovimento;
	}

	public String getTipoPellicStampa() {
		return tipoPellicStampa;
	}

	public void setTipoPellicStampa(String tipoPellicStampa) {
		this.tipoPellicStampa = tipoPellicStampa;
	}

	public String getMaterAccompagn1() {
		return materAccompagn1;
	}

	public void setMaterAccompagn1(String materAccompagn1) {
		this.materAccompagn1 = materAccompagn1;
	}

	public String getMaterAccompagn2() {
		return materAccompagn2;
	}

	public void setMaterAccompagn2(String materAccompagn2) {
		this.materAccompagn2 = materAccompagn2;
	}

	public String getMaterAccompagn3() {
		return materAccompagn3;
	}

	public void setMaterAccompagn3(String materAccompagn3) {
		this.materAccompagn3 = materAccompagn3;
	}

	public String getMaterAccompagn4() {
		return materAccompagn4;
	}

	public void setMaterAccompagn4(String materAccompagn4) {
		this.materAccompagn4 = materAccompagn4;
	}

	public String getFormaPubblicazioneDisco() {
		return formaPubblicazioneDisco;
	}

	public String getVelocita() {
		return velocita;
	}

	public String getTipoSuono() {
		return tipoSuono;
	}

	public String getLarghezzaScanal() {
		return larghezzaScanal;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public String getLarghezzaNastro() {
		return larghezzaNastro;
	}

	public String getConfigurazNastro() {
		return configurazNastro;
	}

	public String getTecnicaRegistraz() {
		return tecnicaRegistraz;
	}

	public String getSpecCarattRiprod() {
		return specCarattRiprod;
	}

	public String getDatiCodifRegistrazSonore() {
		return datiCodifRegistrazSonore;
	}

	public String getTipoDiMateriale() {
		return tipoDiMateriale;
	}

	public String getTipoDiTaglio() {
		return tipoDiTaglio;
	}

	public String getDurataRegistraz() {
		return durataRegistraz;
	}

	public String getMaterTestAccompagn1() {
		return materTestAccompagn1;
	}

	public String getMaterTestAccompagn2() {
		return materTestAccompagn2;
	}

	public String getMaterTestAccompagn3() {
		return materTestAccompagn3;
	}

	public String getMaterTestAccompagn4() {
		return materTestAccompagn4;
	}

	public String getMaterTestAccompagn5() {
		return materTestAccompagn5;
	}

	public String getMaterTestAccompagn6() {
		return materTestAccompagn6;
	}

	public void setFormaPubblicazioneDisco(String formaPubblicazioneDisco) {
		this.formaPubblicazioneDisco = formaPubblicazioneDisco;
	}

	public void setVelocita(String velocita) {
		this.velocita = velocita;
	}

	public void setTipoSuono(String tipoSuono) {
		this.tipoSuono = tipoSuono;
	}

	public void setLarghezzaScanal(String larghezzaScanal) {
		this.larghezzaScanal = larghezzaScanal;
	}

	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}

	public void setLarghezzaNastro(String larghezzaNastro) {
		this.larghezzaNastro = larghezzaNastro;
	}

	public void setConfigurazNastro(String configurazNastro) {
		this.configurazNastro = configurazNastro;
	}

	public void setMaterTestAccompagn1(String materTestAccompagn1) {
		this.materTestAccompagn1 = materTestAccompagn1;
	}

	public void setMaterTestAccompagn2(String materTestAccompagn2) {
		this.materTestAccompagn2 = materTestAccompagn2;
	}

	public void setMaterTestAccompagn3(String materTestAccompagn3) {
		this.materTestAccompagn3 = materTestAccompagn3;
	}

	public void setMaterTestAccompagn4(String materTestAccompagn4) {
		this.materTestAccompagn4 = materTestAccompagn4;
	}

	public void setMaterTestAccompagn5(String materTestAccompagn5) {
		this.materTestAccompagn5 = materTestAccompagn5;
	}

	public void setMaterTestAccompagn6(String materTestAccompagn6) {
		this.materTestAccompagn6 = materTestAccompagn6;
	}

	public void setTecnicaRegistraz(String tecnicaRegistraz) {
		this.tecnicaRegistraz = tecnicaRegistraz;
	}

	public void setSpecCarattRiprod(String specCarattRiprod) {
		this.specCarattRiprod = specCarattRiprod;
	}

	public void setDatiCodifRegistrazSonore(String datiCodifRegistrazSonore) {
		this.datiCodifRegistrazSonore = datiCodifRegistrazSonore;
	}

	public void setTipoDiMateriale(String tipoDiMateriale) {
		this.tipoDiMateriale = tipoDiMateriale;
	}

	public void setTipoDiTaglio(String tipoDiTaglio) {
		this.tipoDiTaglio = tipoDiTaglio;
	}

	public void setDurataRegistraz(String durataRegistraz) {
		this.durataRegistraz = durataRegistraz;
	}

	public String getTipoTestoRegSonora() {
		return tipoTestoRegSonora;
	}

	public void setTipoTestoRegSonora(String tipoTestoRegSonora) {
		this.tipoTestoRegSonora = tipoTestoRegSonora;
	}
}
