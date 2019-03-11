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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.BaseVO;

public class DatiCondivisioneSoggettoVO extends BaseVO {

	private static final long serialVersionUID = 2740020378494289856L;

	public enum OrigineModificaSoggetto {
		NESSUNO,
		POLO,
		INDICE;
	}

	public enum OrigineSoggetto {
		INDICE,
		POLO;
	}

	public enum OrigineLegameTitoloSoggetto {
		NESSUNO,
		INDICE,
		POLO;
	}

	private String cidPolo;
	private String cidIndice;
	private String bid = " ";
	private OrigineModificaSoggetto origineModifica;
	private OrigineSoggetto origineSoggetto;
	private OrigineLegameTitoloSoggetto origineLegame;

	public DatiCondivisioneSoggettoVO() {
		super();
		origineModifica = OrigineModificaSoggetto.NESSUNO;
		origineSoggetto = OrigineSoggetto.INDICE;
		origineLegame   = OrigineLegameTitoloSoggetto.INDICE;
		setFlCanc("N");
	}

	public OrigineLegameTitoloSoggetto getOrigineLegame() {
		return origineLegame;
	}

	public void setOrigineLegame(OrigineLegameTitoloSoggetto origineLegame) {
		this.origineLegame = origineLegame;
	}

	public OrigineModificaSoggetto getOrigineModifica() {
		return origineModifica;
	}

	public void setOrigineModifica(OrigineModificaSoggetto origineModifica) {
		this.origineModifica = origineModifica;
	}

	public OrigineSoggetto getOrigineSoggetto() {
		return origineSoggetto;
	}

	public void setOrigineSoggetto(OrigineSoggetto origineSoggetto) {
		this.origineSoggetto = origineSoggetto;
	}

	public String getCidPolo() {
		return cidPolo;
	}

	public void setCidPolo(String cidPolo) {
		this.cidPolo = cidPolo;
	}

	public String getCidIndice() {
		return cidIndice;
	}

	public void setCidIndice(String cidIndice) {
		this.cidIndice = cidIndice;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	@Override
	public String toString() {
		return "[cid_polo: " + cidPolo + "; cid_indice: " + cidIndice + "; bid: " + bid + "]";
	}

}
