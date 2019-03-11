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

package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.gestionesemantica.DettaglioOggettoSemanticaVO;

public class DettaglioTermineThesauroVO extends DettaglioOggettoSemanticaVO {

	private static final long serialVersionUID = 2555728769924251236L;
	private String did;
	private String TipoLegame;
	private String formaNome = "A";
	private String codThesauro;

	private int numTitoliPolo;
	private int numTitoliBiblio;

	public boolean isRinvio() {
		return (isFilled(formaNome) && formaNome.equalsIgnoreCase("R"));
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTipoLegame() {
		return TipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		TipoLegame = tipoLegame;
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public String toString() {
		return "[did: " + did + "]";
	}

}
