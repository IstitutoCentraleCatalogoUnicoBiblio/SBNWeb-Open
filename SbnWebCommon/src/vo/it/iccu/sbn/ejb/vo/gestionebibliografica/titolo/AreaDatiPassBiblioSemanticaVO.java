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

public class AreaDatiPassBiblioSemanticaVO extends SerializableVO {

	private static final long serialVersionUID = -8370081162095288213L;
	private String bidPartenza;
	private String descPartenza;

	private TreeElementViewTitoli treeElement = new TreeElementViewTitoli();
	private boolean classeDewey;
	private boolean inserimentoPolo;
	private boolean inserimentoIndice;

	private int countLegamiSoggetti;
	private short rankLegame;

	private String poloSoggettazioneIndice;

	public String getBidPartenza() {
		return bidPartenza;
	}

	public void setBidPartenza(String bidPartenza) {
		this.bidPartenza = bidPartenza;
	}

	public boolean isClasseDewey() {
		return classeDewey;
	}

	public void setClasseDewey(boolean classeDewey) {
		this.classeDewey = classeDewey;
	}

	public String getDescPartenza() {
		return descPartenza;
	}

	public void setDescPartenza(String descPartenza) {
		this.descPartenza = descPartenza;
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

	public TreeElementViewTitoli getTreeElement() {
		return treeElement;
	}

	public void setTreeElement(TreeElementViewTitoli treeElement) {
		this.treeElement = treeElement;
	}

	public int getCountLegamiSoggetti() {
		return countLegamiSoggetti;
	}

	public void setCountLegamiSoggetti(int countLegamiSoggetti) {
		this.countLegamiSoggetti = countLegamiSoggetti;
	}

	public short getRankLegame() {
		return rankLegame;
	}

	public void setRankLegame(short rankLegame) {
		this.rankLegame = rankLegame;
	}

	public String getPoloSoggettazioneIndice() {
		return poloSoggettazioneIndice;
	}

	public void setPoloSoggettazioneIndice(String poloSoggettatore) {
		this.poloSoggettazioneIndice = poloSoggettatore;
	}

}
