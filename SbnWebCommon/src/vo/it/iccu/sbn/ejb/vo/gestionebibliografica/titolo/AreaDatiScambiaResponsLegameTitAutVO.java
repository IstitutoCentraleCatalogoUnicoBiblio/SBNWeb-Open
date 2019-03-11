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

import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiScambiaResponsLegameTitAutVO   extends SerializableVO {

	private static final long serialVersionUID = 1334810983414550213L;

	private String tipoOperazione;
	private boolean inserimentoPolo;
	private boolean inserimentoIndice;

	AreaDatiLegameTitoloVO areaDatiLegameTitolo12VO;
	AreaDatiLegameTitoloVO areaDatiLegameTitolo21VO;

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitolo12VO() {
		return areaDatiLegameTitolo12VO;
	}
	public void setAreaDatiLegameTitolo12VO(
			AreaDatiLegameTitoloVO areaDatiLegameTitolo12VO) {
		this.areaDatiLegameTitolo12VO = areaDatiLegameTitolo12VO;
	}
	public AreaDatiLegameTitoloVO getAreaDatiLegameTitolo21VO() {
		return areaDatiLegameTitolo21VO;
	}
	public void setAreaDatiLegameTitolo21VO(
			AreaDatiLegameTitoloVO areaDatiLegameTitolo21VO) {
		this.areaDatiLegameTitolo21VO = areaDatiLegameTitolo21VO;
	}
	public boolean isInserimentoIndice() {
		return inserimentoIndice;
	}
	public void setInserimentoIndice(boolean inserimentoIndice) {
		this.inserimentoIndice = inserimentoIndice;
	}
	public boolean isInserimentoPolo() {
		return inserimentoPolo;
	}
	public void setInserimentoPolo(boolean inserimentoPolo) {
		this.inserimentoPolo = inserimentoPolo;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

}
