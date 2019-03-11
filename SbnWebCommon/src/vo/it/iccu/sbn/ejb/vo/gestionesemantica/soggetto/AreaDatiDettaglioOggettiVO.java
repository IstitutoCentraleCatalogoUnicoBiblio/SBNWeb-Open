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

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.DettaglioAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;

public class AreaDatiDettaglioOggettiVO extends SerializableVO {

	private static final long serialVersionUID = -298786739325022866L;

	DettaglioSoggettoVO dettaglioSoggettoGeneraleVO = new DettaglioSoggettoVO();
	DettaglioClasseVO dettaglioClasseGeneraleVO = new DettaglioClasseVO();
	DettaglioDescrittoreVO dettaglioDescrittoreGeneraleVO = new DettaglioDescrittoreVO();
	DettaglioAbstractVO dettaglioAbstractGeneraleVO = new DettaglioAbstractVO();
	DettaglioTermineThesauroVO dettaglioTermineThesauroVO = new DettaglioTermineThesauroVO();

	public DettaglioClasseVO getDettaglioClasseGeneraleVO() {
		return dettaglioClasseGeneraleVO;
	}

	public void setDettaglioClasseGeneraleVO(
			DettaglioClasseVO dettaglioClasseGeneraleVO) {
		this.dettaglioClasseGeneraleVO = dettaglioClasseGeneraleVO;
	}

	public DettaglioDescrittoreVO getDettaglioDescrittoreGeneraleVO() {
		return dettaglioDescrittoreGeneraleVO;
	}

	public void setDettaglioDescrittoreGeneraleVO(
			DettaglioDescrittoreVO dettaglioDescrittoreGeneraleVO) {
		this.dettaglioDescrittoreGeneraleVO = dettaglioDescrittoreGeneraleVO;
	}

	public DettaglioSoggettoVO getDettaglioSoggettoGeneraleVO() {
		return dettaglioSoggettoGeneraleVO;
	}

	public void setDettaglioSoggettoGeneraleVO(
			DettaglioSoggettoVO dettaglioSoggettoGeneraleVO) {
		this.dettaglioSoggettoGeneraleVO = dettaglioSoggettoGeneraleVO;
	}

	public DettaglioAbstractVO getDettaglioAbstractGeneraleVO() {
		return dettaglioAbstractGeneraleVO;
	}

	public void setDettaglioAbstractGeneraleVO(
			DettaglioAbstractVO dettaglioAbstractGeneraleVO) {
		this.dettaglioAbstractGeneraleVO = dettaglioAbstractGeneraleVO;
	}

	public DettaglioTermineThesauroVO getDettaglioTermineThesauroVO() {
		return dettaglioTermineThesauroVO;
	}

	public void setDettaglioTermineThesauroVO(
			DettaglioTermineThesauroVO dettaglioTermineThesauroVO) {
		this.dettaglioTermineThesauroVO = dettaglioTermineThesauroVO;
	}

}
