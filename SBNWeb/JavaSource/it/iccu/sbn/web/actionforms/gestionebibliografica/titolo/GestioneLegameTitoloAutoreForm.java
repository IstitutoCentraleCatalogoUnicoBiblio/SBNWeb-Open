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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class GestioneLegameTitoloAutoreForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 3664825539764883171L;

	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();

	private List listaRelatorCode;
	private List listaTipoRespons;
	private List listaSpecStrumVoci;
	private String presenzaSpecStrumVoci;

	private String elencoBidArrivoLegamiAutore;

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public List getListaRelatorCode() {
		return listaRelatorCode;
	}

	public void setListaRelatorCode(List listaRelatorCode) {
		this.listaRelatorCode = listaRelatorCode;
	}

	public List getListaTipoRespons() {
		return listaTipoRespons;
	}

	public void setListaTipoRespons(List listaTipoRespons) {
		this.listaTipoRespons = listaTipoRespons;
	}

	public List getListaSpecStrumVoci() {
		return listaSpecStrumVoci;
	}

	public void setListaSpecStrumVoci(List listaSpecStrumVoci) {
		this.listaSpecStrumVoci = listaSpecStrumVoci;
	}

	public String getPresenzaSpecStrumVoci() {
		return presenzaSpecStrumVoci;
	}

	public void setPresenzaSpecStrumVoci(String presenzaSpecStrumVoci) {
		this.presenzaSpecStrumVoci = presenzaSpecStrumVoci;
	}

	public String getElencoBidArrivoLegamiAutore() {
		return elencoBidArrivoLegamiAutore;
	}

	public void setElencoBidArrivoLegamiAutore(String elencoBidArrivoLegamiAutore) {
		this.elencoBidArrivoLegamiAutore = elencoBidArrivoLegamiAutore;
	}


}
