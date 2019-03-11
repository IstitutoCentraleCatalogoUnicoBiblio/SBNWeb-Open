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


public class AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO  extends SerializableVO {

	// = AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 8429387836024576085L;
	private int numPrimo;
	private int maxRighe;
	private String idLista;
	private String tipoOrdinam;
	private String tipoOutput;
	private boolean presenzaSequenzaLegame;

	private boolean ricercaPolo;
	private boolean ricercaIndice;


	public boolean isPresenzaSequenzaLegame() {
		return presenzaSequenzaLegame;
	}
	public void setPresenzaSequenzaLegame(boolean presenzaSequenzaLegame) {
		this.presenzaSequenzaLegame = presenzaSequenzaLegame;
	}
	public boolean isRicercaIndice() {
		return ricercaIndice;
	}
	public void setRicercaIndice(boolean ricercaIndice) {
		this.ricercaIndice = ricercaIndice;
	}
	public boolean isRicercaPolo() {
		return ricercaPolo;
	}
	public void setRicercaPolo(boolean ricercaPolo) {
		this.ricercaPolo = ricercaPolo;
	}
	public String getIdLista() {
		return idLista;
	}
	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}
	public int getMaxRighe() {
		return maxRighe;
	}
	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}
	public int getNumPrimo() {
		return numPrimo;
	}
	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}
	public String getTipoOrdinam() {
		return tipoOrdinam;
	}
	public void setTipoOrdinam(String tipoOrdinam) {
		this.tipoOrdinam = tipoOrdinam;
	}
	public String getTipoOutput() {
		return tipoOutput;
	}
	public void setTipoOutput(String tipoOutput) {
		this.tipoOutput = tipoOutput;
	}

}
