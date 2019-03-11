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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriLegameVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioClasseGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioDescrittoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioSoggettoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioTermineThesauroGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;

public class AreaDatiDettaglioOggettiVO extends SerializableVO {

	private static final long serialVersionUID = -8844431252102306578L;

	DettaglioGenericoLegameVO dettaglioLegameVO = new DettaglioGenericoLegameVO();
	DettaglioTitoloCompletoVO dettaglioTitoloCompletoVO = new DettaglioTitoloCompletoVO();
	DettaglioAutoreGeneraleVO dettaglioAutoreGeneraleVO = new DettaglioAutoreGeneraleVO();
	DettaglioMarcaGeneraleVO dettaglioMarcaGeneraleVO = new DettaglioMarcaGeneraleVO();
	DettaglioSoggettoGeneraleVO dettaglioSoggettoGeneraleVO = new DettaglioSoggettoGeneraleVO();
	DettaglioClasseGeneraleVO dettaglioClasseGeneraleVO = new DettaglioClasseGeneraleVO();
	DettaglioTermineThesauroGeneraleVO dettaglioTermineThesauroGeneraleVO = new DettaglioTermineThesauroGeneraleVO();
	DettaglioDescrittoreGeneraleVO dettaglioDescrittoreGeneraleVO = new DettaglioDescrittoreGeneraleVO();
	DettaglioLuogoGeneraleVO dettaglioLuogoGeneraleVO = new DettaglioLuogoGeneraleVO();
	PossessoriDettaglioVO possessoreDettaglioVO = new PossessoriDettaglioVO();
	PossessoriLegameVO possessoreLegameVO = new PossessoriLegameVO();

	public DettaglioClasseGeneraleVO getDettaglioClasseGeneraleVO() {
		return dettaglioClasseGeneraleVO;
	}
	public void setDettaglioClasseGeneraleVO(
			DettaglioClasseGeneraleVO dettaglioClasseGeneraleVO) {
		this.dettaglioClasseGeneraleVO = dettaglioClasseGeneraleVO;
	}
	public DettaglioDescrittoreGeneraleVO getDettaglioDescrittoreGeneraleVO() {
		return dettaglioDescrittoreGeneraleVO;
	}
	public void setDettaglioDescrittoreGeneraleVO(
			DettaglioDescrittoreGeneraleVO dettaglioDescrittoreGeneraleVO) {
		this.dettaglioDescrittoreGeneraleVO = dettaglioDescrittoreGeneraleVO;
	}
	public DettaglioLuogoGeneraleVO getDettaglioLuogoGeneraleVO() {
		return dettaglioLuogoGeneraleVO;
	}
	public void setDettaglioLuogoGeneraleVO(
			DettaglioLuogoGeneraleVO dettaglioLuogoGeneraleVO) {
		this.dettaglioLuogoGeneraleVO = dettaglioLuogoGeneraleVO;
	}
	public DettaglioMarcaGeneraleVO getDettaglioMarcaGeneraleVO() {
		return dettaglioMarcaGeneraleVO;
	}
	public void setDettaglioMarcaGeneraleVO(
			DettaglioMarcaGeneraleVO dettaglioMarcaGeneraleVO) {
		this.dettaglioMarcaGeneraleVO = dettaglioMarcaGeneraleVO;
	}
	public DettaglioSoggettoGeneraleVO getDettaglioSoggettoGeneraleVO() {
		return dettaglioSoggettoGeneraleVO;
	}
	public void setDettaglioSoggettoGeneraleVO(
			DettaglioSoggettoGeneraleVO dettaglioSoggettoGeneraleVO) {
		this.dettaglioSoggettoGeneraleVO = dettaglioSoggettoGeneraleVO;
	}
	public DettaglioAutoreGeneraleVO getDettaglioAutoreGeneraleVO() {
		return dettaglioAutoreGeneraleVO;
	}
	public void setDettaglioAutoreGeneraleVO(
			DettaglioAutoreGeneraleVO dettaglioAutoreGeneraleVO) {
		this.dettaglioAutoreGeneraleVO = dettaglioAutoreGeneraleVO;
	}
	public DettaglioTermineThesauroGeneraleVO getDettaglioTermineThesauroGeneraleVO() {
		return dettaglioTermineThesauroGeneraleVO;
	}
	public void setDettaglioTermineThesauroGeneraleVO(
			DettaglioTermineThesauroGeneraleVO dettaglioTermineThesauroGeneraleVO) {
		this.dettaglioTermineThesauroGeneraleVO = dettaglioTermineThesauroGeneraleVO;
	}
	public DettaglioGenericoLegameVO getDettaglioLegameVO() {
		return dettaglioLegameVO;
	}
	public void setDettaglioLegameVO(DettaglioGenericoLegameVO dettaglioLegameVO) {
		this.dettaglioLegameVO = dettaglioLegameVO;
	}
	public DettaglioTitoloCompletoVO getDettaglioTitoloCompletoVO() {
		return dettaglioTitoloCompletoVO;
	}
	public void setDettaglioTitoloCompletoVO(
			DettaglioTitoloCompletoVO dettaglioTitoloCompletoVO) {
		this.dettaglioTitoloCompletoVO = dettaglioTitoloCompletoVO;
	}
	public PossessoriDettaglioVO getPossessoreDettaglioVO() {
		return possessoreDettaglioVO;
	}
	public void setPossessoreDettaglioVO(PossessoriDettaglioVO possessoreDettaglioVO) {
		this.possessoreDettaglioVO = possessoreDettaglioVO;
	}
	public PossessoriLegameVO getPossessoreLegameVO() {
		return possessoreLegameVO;
	}
	public void setPossessoreLegameVO(PossessoriLegameVO possessoreLegameVO) {
		this.possessoreLegameVO = possessoreLegameVO;
	}

}
