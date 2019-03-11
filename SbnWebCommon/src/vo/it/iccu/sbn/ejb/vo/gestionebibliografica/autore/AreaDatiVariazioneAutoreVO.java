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
package it.iccu.sbn.ejb.vo.gestionebibliografica.autore;

import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;

import java.util.ArrayList;
import java.util.List;


public class AreaDatiVariazioneAutoreVO  extends SerializableVO {

	// = AreaDatiVariazioneAutoreVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -7233905326920049684L;

	private boolean flagCondiviso;

	private boolean modifica;
	private boolean conferma;
	private boolean primoBloccoSimili;
	private boolean inserimentoPolo;
	private boolean inserimentoIndice;

	DettaglioAutoreGeneraleVO dettAutoreVO = new DettaglioAutoreGeneraleVO();

	private boolean variazione;

	private String bidTemporaneo;

	// Area dati per inserimento contestuale dei legami presenti in polo ad oggetti condivisi che vanno condivisi con l'Indice
	private boolean legameDaCondividere;
	private LegamiType legamiTypeDaCondividere;
	List <AreaDatiLegameTitoloVO> listaAreaDatiLegameTitoloVO = new ArrayList<AreaDatiLegameTitoloVO>();



	public String getBidTemporaneo() {
		return bidTemporaneo;
	}

	public void setBidTemporaneo(String bidTemporaneo) {
		this.bidTemporaneo = bidTemporaneo;
	}

	public boolean isVariazione() {
		return variazione;
	}

	public void setVariazione(boolean variazione) {
		this.variazione = variazione;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public DettaglioAutoreGeneraleVO getDettAutoreVO() {
		return dettAutoreVO;
	}

	public void setDettAutoreVO(DettaglioAutoreGeneraleVO dettAutoreVO) {
		this.dettAutoreVO = dettAutoreVO;
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

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public boolean isLegameDaCondividere() {
		return legameDaCondividere;
	}

	public void setLegameDaCondividere(boolean legameDaCondividere) {
		this.legameDaCondividere = legameDaCondividere;
	}

	public LegamiType getLegamiTypeDaCondividere() {
		return legamiTypeDaCondividere;
	}

	public void setLegamiTypeDaCondividere(LegamiType legamiTypeDaCondividere) {
		this.legamiTypeDaCondividere = legamiTypeDaCondividere;
	}

	public List<AreaDatiLegameTitoloVO> getListaAreaDatiLegameTitoloVO() {
		return listaAreaDatiLegameTitoloVO;
	}

	public void setListaAreaDatiLegameTitoloVO(
			List<AreaDatiLegameTitoloVO> listaAreaDatiLegameTitoloVO) {
		this.listaAreaDatiLegameTitoloVO = listaAreaDatiLegameTitoloVO;
	}

	public List addListaAreaDatiLegameTitoloVO(AreaDatiLegameTitoloVO elemento ) {
		listaAreaDatiLegameTitoloVO.add(elemento);
		return listaAreaDatiLegameTitoloVO;
	}



}
