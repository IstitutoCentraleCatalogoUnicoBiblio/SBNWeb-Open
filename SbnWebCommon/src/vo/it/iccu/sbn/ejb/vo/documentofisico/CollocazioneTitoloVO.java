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
package it.iccu.sbn.ejb.vo.documentofisico;

public class CollocazioneTitoloVO extends CollocazioneVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -6019697106139534769L;
	private int prg;
	private String natura;
	private String isbd;
	private String chiaveAutore;
	private String chiaveTitolo;
	private String vidAutore;

	public String getVidAutore() {
		return vidAutore;
	}

	public CollocazioneTitoloVO(){
	}

	public CollocazioneTitoloVO(int keyColloc, String bid, String isbd, String natura)
	throws Exception {

		super(keyColloc, bid);

		this.isbd = isbd;
		this.natura = natura;
		}

	public CollocazioneTitoloVO(int prg, String bid, String isbd, String natura,
			String chiaveTitolo, String chiaveAutore, String vidAutore) {
		super();
		super.setBid(bid);
		this.prg = prg;
		this.isbd = isbd;
		this.natura = natura;
		this.chiaveAutore = chiaveAutore;
		this.chiaveTitolo = chiaveTitolo;
		this.vidAutore = vidAutore;
	}

	//costruttore per listaCollocazioniDiEsemplare
	public CollocazioneTitoloVO(CollocazioneVO coll, String isbd)
	throws Exception {
		super(coll);
		this.isbd = isbd;
	}

	public String getChiaveAutore() {
		return chiaveAutore;
	}

	public void setChiaveAutore(String chiaveAutore) {
		this.chiaveAutore = chiaveAutore;
	}

	public String getChiaveTitolo() {
		return chiaveTitolo;
	}

	public void setChiaveTitolo(String chiaveTitolo) {
		this.chiaveTitolo = chiaveTitolo;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}
}
