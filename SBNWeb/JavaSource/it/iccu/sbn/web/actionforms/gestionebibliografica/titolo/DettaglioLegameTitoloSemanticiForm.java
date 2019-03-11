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
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioClasseGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioDescrittoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioSoggettoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioTermineThesauroGeneraleVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

public class DettaglioLegameTitoloSemanticiForm extends AbstractBibliotecaForm {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -757569320733391396L;
	private DettaglioSoggettoGeneraleVO dettSogGenVO = new DettaglioSoggettoGeneraleVO();
	 private DettaglioClasseGeneraleVO dettClaGenVO = new DettaglioClasseGeneraleVO();
	 private DettaglioDescrittoreGeneraleVO dettDesGenVO = new DettaglioDescrittoreGeneraleVO();
	 private DettaglioTermineThesauroGeneraleVO dettTerThesGenVO = new DettaglioTermineThesauroGeneraleVO();

	 private String tipoLegame;

	public DettaglioTermineThesauroGeneraleVO getDettTerThesGenVO() {
		return dettTerThesGenVO;
	}

	public void setDettTerThesGenVO(
			DettaglioTermineThesauroGeneraleVO dettTerThesGenVO) {
		this.dettTerThesGenVO = dettTerThesGenVO;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public DettaglioSoggettoGeneraleVO getDettSogGenVO() {
		return dettSogGenVO;
	}

	public void setDettSogGenVO(DettaglioSoggettoGeneraleVO dettSogGenVO) {
		this.dettSogGenVO = dettSogGenVO;
	}

	public DettaglioClasseGeneraleVO getDettClaGenVO() {
		return dettClaGenVO;
	}

	public void setDettClaGenVO(DettaglioClasseGeneraleVO dettClaGenVO) {
		this.dettClaGenVO = dettClaGenVO;
	}

	public DettaglioDescrittoreGeneraleVO getDettDesGenVO() {
		return dettDesGenVO;
	}

	public void setDettDesGenVO(DettaglioDescrittoreGeneraleVO dettDesGenVO) {
		this.dettDesGenVO = dettDesGenVO;
	}




}
