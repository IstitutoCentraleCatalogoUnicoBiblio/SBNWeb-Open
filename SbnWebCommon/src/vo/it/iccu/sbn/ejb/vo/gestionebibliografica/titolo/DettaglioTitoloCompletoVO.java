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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.BaseVO;


public class DettaglioTitoloCompletoVO  extends BaseVO {

	// = DettaglioTitoloCompletoVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 2362432026862980233L;
	DettaglioTitoloParteFissaVO detTitoloPFissaVO = new DettaglioTitoloParteFissaVO();
	DettaglioTitoloCartografiaVO detTitoloCarVO = new DettaglioTitoloCartografiaVO();
	DettaglioTitoloGraficaVO detTitoloGraVO = new DettaglioTitoloGraficaVO();
	DettaglioTitoloMusicaVO detTitoloMusVO = new DettaglioTitoloMusicaVO();

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	DettaglioTitoloAudiovisivoVO detTitoloAudVO = new DettaglioTitoloAudiovisivoVO();
	DettaglioTitoloElettronicoVO detTitoloEleVO = new DettaglioTitoloElettronicoVO();

	// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
	// viene esteso anche al Materiale Moderno e Antico
	DettaglioTitoloModAntVO detTitoloModAntVO = new DettaglioTitoloModAntVO();

	// Dati eventuali per legami LEGAME_DOCUMENTO_DOCUMENTO
	private String idPadre;
	private String nominativoPadre;

	private String campoSici;
	private String campoSequenza;
	private String campoNotaAlLegame;
	private String campoTipoLegame;
	private String campoTipoLegameConNature;
	private String campoSottoTipoLegame;
	private String tipoLegame;


	public String getCampoSottoTipoLegame() {
		return campoSottoTipoLegame;
	}
	public void setCampoSottoTipoLegame(String campoSottoTipoLegame) {
		this.campoSottoTipoLegame = campoSottoTipoLegame;
	}
	public DettaglioTitoloCartografiaVO getDetTitoloCarVO() {
		return detTitoloCarVO;
	}
	public void setDetTitoloCarVO(DettaglioTitoloCartografiaVO detTitoloCarVO) {
		this.detTitoloCarVO = detTitoloCarVO;
	}
	public DettaglioTitoloGraficaVO getDetTitoloGraVO() {
		return detTitoloGraVO;
	}
	public void setDetTitoloGraVO(DettaglioTitoloGraficaVO detTitoloGraVO) {
		this.detTitoloGraVO = detTitoloGraVO;
	}
	public DettaglioTitoloMusicaVO getDetTitoloMusVO() {
		return detTitoloMusVO;
	}
	public void setDetTitoloMusVO(DettaglioTitoloMusicaVO detTitoloMusVO) {
		this.detTitoloMusVO = detTitoloMusVO;
	}
	public DettaglioTitoloParteFissaVO getDetTitoloPFissaVO() {
		return detTitoloPFissaVO;
	}
	public void setDetTitoloPFissaVO(DettaglioTitoloParteFissaVO detTitoloPFissaVO) {
		this.detTitoloPFissaVO = detTitoloPFissaVO;
	}
	public String getCampoNotaAlLegame() {
		return campoNotaAlLegame;
	}
	public void setCampoNotaAlLegame(String campoNotaAlLegame) {
		this.campoNotaAlLegame = campoNotaAlLegame;
	}
	public String getCampoSequenza() {
		return campoSequenza;
	}
	public void setCampoSequenza(String campoSequenza) {
		this.campoSequenza = campoSequenza;
	}
	public String getCampoSici() {
		return campoSici;
	}
	public void setCampoSici(String campoSici) {
		this.campoSici = campoSici;
	}
	public String getCampoTipoLegame() {
		return campoTipoLegame;
	}
	public void setCampoTipoLegame(String campoTipoLegame) {
		this.campoTipoLegame = campoTipoLegame;
	}
	public String getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}
	public String getNominativoPadre() {
		return nominativoPadre;
	}
	public void setNominativoPadre(String nominativoPadre) {
		this.nominativoPadre = nominativoPadre;
	}
	public String getTipoLegame() {
		return tipoLegame;
	}
	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (campoNotaAlLegame == null) {
			if (other.campoNotaAlLegame != null)
				return false;
		} else if (!campoNotaAlLegame.equals(other.campoNotaAlLegame))
			return false;
		if (campoSequenza == null) {
			if (other.campoSequenza != null)
				return false;
		} else if (!campoSequenza.equals(other.campoSequenza))
			return false;
		if (campoSici == null) {
			if (other.campoSici != null)
				return false;
		} else if (!campoSici.equals(other.campoSici))
			return false;
		if (campoSottoTipoLegame == null) {
			if (other.campoSottoTipoLegame != null)
				return false;
		} else if (!campoSottoTipoLegame.equals(other.campoSottoTipoLegame))
			return false;
		if (campoTipoLegame == null) {
			if (other.campoTipoLegame != null)
				return false;
		} else if (!campoTipoLegame.equals(other.campoTipoLegame))
			return false;
		if (detTitoloCarVO == null) {
			if (other.detTitoloCarVO != null)
				return false;
		} else if (!detTitoloCarVO.equals(other.detTitoloCarVO))
			return false;
		if (detTitoloGraVO == null) {
			if (other.detTitoloGraVO != null)
				return false;
		} else if (!detTitoloGraVO.equals(other.detTitoloGraVO))
			return false;
		if (detTitoloMusVO == null) {
			if (other.detTitoloMusVO != null)
				return false;
		} else if (!detTitoloMusVO.equals(other.detTitoloMusVO))
			return false;

		// Manutenzione Novembre 2015 segnalata da Carla Scognamiglio; non vengono effettuate modifiche
		// alle specificita audiovisive perchè manca il relativo controllo sulla presenza di differenze nelle
		// sole aree delle specificità!!!!
		if (detTitoloAudVO == null) {
			if (other.detTitoloAudVO != null)
				return false;
		} else if (!detTitoloAudVO.equals(other.detTitoloAudVO))
			return false;
		if (detTitoloEleVO == null) {
			if (other.detTitoloEleVO != null)
				return false;
		} else if (!detTitoloEleVO.equals(other.detTitoloEleVO))
			return false;

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if (detTitoloModAntVO == null) {
			if (other.detTitoloModAntVO != null)
				return false;
		} else if (!detTitoloModAntVO.equals(other.detTitoloModAntVO))
			return false;

		if (detTitoloPFissaVO == null) {
			if (other.detTitoloPFissaVO != null)
				return false;
		} else if (!detTitoloPFissaVO.equals(other.detTitoloPFissaVO))
			return false;
		if (idPadre == null) {
			if (other.idPadre != null)
				return false;
		} else if (!idPadre.equals(other.idPadre))
			return false;
		if (nominativoPadre == null) {
			if (other.nominativoPadre != null)
				return false;
		} else if (!nominativoPadre.equals(other.nominativoPadre))
			return false;
		if (tipoLegame == null) {
			if (other.tipoLegame != null)
				return false;
		} else if (!tipoLegame.equals(other.tipoLegame))
			return false;
		return true;
	}


	public boolean equalsTitBase(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (campoNotaAlLegame == null) {
			if (other.campoNotaAlLegame != null)
				return false;
		} else if (!campoNotaAlLegame.equals(other.campoNotaAlLegame))
			return false;
		if (campoSequenza == null) {
			if (other.campoSequenza != null)
				return false;
		} else if (!campoSequenza.equals(other.campoSequenza))
			return false;
		if (campoSici == null) {
			if (other.campoSici != null)
				return false;
		} else if (!campoSici.equals(other.campoSici))
			return false;
		if (campoSottoTipoLegame == null) {
			if (other.campoSottoTipoLegame != null)
				return false;
		} else if (!campoSottoTipoLegame.equals(other.campoSottoTipoLegame))
			return false;
		if (campoTipoLegame == null) {
			if (other.campoTipoLegame != null)
				return false;
		} else if (!campoTipoLegame.equals(other.campoTipoLegame))
			return false;
		if (detTitoloPFissaVO == null) {
			if (other.detTitoloPFissaVO != null)
				return false;
		} else if (!detTitoloPFissaVO.equals(other.detTitoloPFissaVO))
			return false;
		if (idPadre == null) {
			if (other.idPadre != null)
				return false;
		} else if (!idPadre.equals(other.idPadre))
			return false;
		if (nominativoPadre == null) {
			if (other.nominativoPadre != null)
				return false;
		} else if (!nominativoPadre.equals(other.nominativoPadre))
			return false;
		if (tipoLegame == null) {
			if (other.tipoLegame != null)
				return false;
		} else if (!tipoLegame.equals(other.tipoLegame))
			return false;
		return true;
	}


	public boolean equalsSpecCar(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (detTitoloCarVO == null) {
			if (other.detTitoloCarVO != null)
				return false;
		} else if (!detTitoloCarVO.equals(other.detTitoloCarVO))
			return false;
		return true;
	}


	public boolean equalsSpecGra(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (detTitoloGraVO == null) {
			if (other.detTitoloGraVO != null)
				return false;
		} else if (!detTitoloGraVO.equals(other.detTitoloGraVO))
			return false;
		return true;
	}


	public boolean equalsSpecMus(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (detTitoloMusVO == null) {
			if (other.detTitoloMusVO != null)
				return false;
		} else if (!detTitoloMusVO.equals(other.detTitoloMusVO))
			return false;
		return true;
	}

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	public boolean equalsSpecAud(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (detTitoloAudVO == null) {
			if (other.detTitoloAudVO != null)
				return false;
		} else if (!detTitoloAudVO.equals(other.detTitoloAudVO))
			return false;
		return true;
	}

	public boolean equalsSpecEle(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloCompletoVO other = (DettaglioTitoloCompletoVO) obj;
		if (detTitoloEleVO == null) {
			if (other.detTitoloEleVO != null)
				return false;
		} else if (!detTitoloEleVO.equals(other.detTitoloEleVO))
			return false;
		return true;
	}



	public String getCampoTipoLegameConNature() {
		return campoTipoLegameConNature;
	}
	public void setCampoTipoLegameConNature(String campoTipoLegameConNature) {
		this.campoTipoLegameConNature = campoTipoLegameConNature;
	}
	public DettaglioTitoloAudiovisivoVO getDetTitoloAudVO() {
		return detTitoloAudVO;
	}
	public void setDetTitoloAudVO(DettaglioTitoloAudiovisivoVO detTitoloAudVO) {
		this.detTitoloAudVO = detTitoloAudVO;
	}
	public DettaglioTitoloElettronicoVO getDetTitoloEleVO() {
		return detTitoloEleVO;
	}
	public void setDetTitoloEleVO(DettaglioTitoloElettronicoVO detTitoloEleVO) {
		this.detTitoloEleVO = detTitoloEleVO;
	}
	public DettaglioTitoloModAntVO getDetTitoloModAntVO() {
		return detTitoloModAntVO;
	}
	public void setDetTitoloModAntVO(DettaglioTitoloModAntVO detTitoloModAntVO) {
		this.detTitoloModAntVO = detTitoloModAntVO;
	}


}
