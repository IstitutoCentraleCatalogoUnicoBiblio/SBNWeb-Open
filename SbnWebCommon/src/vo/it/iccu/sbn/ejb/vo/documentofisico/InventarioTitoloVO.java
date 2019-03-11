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

public class InventarioTitoloVO extends InventarioVO implements
		Comparable<InventarioTitoloVO> {

	private static final long serialVersionUID = 3349450403775598844L;

	private int prg;
	private String flEsempl;
	private String codSez;
	private String codLoc;
	private String specLoc;
	private String consistenza;
	private String fruizione;
	private String disponibilita;

	private String isbd;

	public InventarioTitoloVO(int codInvent) throws Exception {
		super(codInvent);
	}

	public InventarioTitoloVO(InventarioVO inventario) throws Exception {
		super(inventario);
	}

	public InventarioTitoloVO(String codBib, String codSerie, int codInvent,
			String codSit, String seqColl, String prec1, String bid,
			int keyLoc, String keyLocOld, String sezOld, String locOld,
			String specOld, String codSez, String codLoc, String specLoc,
			String consistenza, int prg, String flEsempl, String fruizione,
			String disponibilita) throws Exception {

		super(codBib, codSerie, codInvent, codSit, seqColl, prec1, bid, keyLoc,
				keyLocOld, sezOld, locOld, specOld);
		this.codSez = codSez;
		this.codLoc = codLoc;
		this.specLoc = specLoc;
		this.consistenza = consistenza;
		this.prg = prg;
		this.fruizione = fruizione;
		this.disponibilita = fruizione;
	}

	public InventarioTitoloVO() {
		super();
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getSpecLoc() {
		return specLoc;
	}

	public void setSpecLoc(String specLoc) {
		this.specLoc = specLoc;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public int compareTo(InventarioTitoloVO o) {
		return (this.prg - o.prg);
	}

	public String getFlEsempl() {
		return flEsempl;
	}

	public void setFlEsempl(String flEsempl) {
		this.flEsempl = flEsempl;
	}

	public String getFruizione() {
		return fruizione;
	}

	public void setFruizione(String fruizione) {
		this.fruizione = fruizione;
	}

	public String getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(String disponibilita) {
		this.disponibilita = disponibilita;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = trimAndSet(isbd);
	}
}
