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
package it.iccu.sbn.ejb.vo.gestionebibliografica.marca;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiVariazioneMarcaVO  extends SerializableVO {

	// = AreaDatiVariazioneMarcaVO.class.hashCode();


	/**
	 *
	 */
	private static final long serialVersionUID = 260943593306168965L;

	private boolean flagCondiviso;

	private boolean modifica;
//	private boolean conferma;
	private boolean confermaCitazioneDuplicata;
	private boolean primoBloccoSimili;
	private boolean inserimentoPolo;
	private boolean inserimentoIndice;

	DettaglioMarcaGeneraleVO dettMarcaVO = new DettaglioMarcaGeneraleVO();

	private boolean variazione;

	public boolean isVariazione() {
		return variazione;
	}

	public void setVariazione(boolean variazione) {
		this.variazione = variazione;
	}

//	public boolean isConferma() {
//		return conferma;
//	}
//
//	public void setConferma(boolean conferma) {
//		this.conferma = conferma;
//	}

	public DettaglioMarcaGeneraleVO getDettMarcaVO() {
		return dettMarcaVO;
	}

	public void setDettMarcaVO(DettaglioMarcaGeneraleVO dettMarcaVO) {
		this.dettMarcaVO = dettMarcaVO;
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

	public boolean isModifica() {
		return modifica;
	}

	public void setModifica(boolean modifica) {
		this.modifica = modifica;
	}

	public boolean isPrimoBloccoSimili() {
		return primoBloccoSimili;
	}

	public void setPrimoBloccoSimili(boolean primoBloccoSimili) {
		this.primoBloccoSimili = primoBloccoSimili;
	}

	public boolean isConfermaCitazioneDuplicata() {
		return confermaCitazioneDuplicata;
	}

	public void setConfermaCitazioneDuplicata(boolean confermaCitazioneDuplicata) {
		this.confermaCitazioneDuplicata = confermaCitazioneDuplicata;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}


}
