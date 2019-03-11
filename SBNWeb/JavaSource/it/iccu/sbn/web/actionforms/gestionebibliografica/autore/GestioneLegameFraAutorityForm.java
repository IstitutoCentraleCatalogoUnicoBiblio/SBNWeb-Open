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

package it.iccu.sbn.web.actionforms.gestionebibliografica.autore;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class GestioneLegameFraAutorityForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -6227870385009185333L;
	private String creaRinvio;
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();

    private List listaLivAut;
	private String descLivAut;

	private List listaTipoNome;
	private String descTipoNome;

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public String getCreaRinvio() {
		return creaRinvio;
	}

	public void setCreaRinvio(String creaRinvio) {
		this.creaRinvio = creaRinvio;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public String getDescLivAut() {
		return descLivAut;
	}

	public void setDescLivAut(String descLivAut) {
		this.descLivAut = descLivAut;
	}

	public String getDescTipoNome() {
		return descTipoNome;
	}

	public void setDescTipoNome(String descTipoNome) {
		this.descTipoNome = descTipoNome;
	}

	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public List getListaTipoNome() {
		return listaTipoNome;
	}

	public void setListaTipoNome(List listaTipoNome) {
		this.listaTipoNome = listaTipoNome;
	}

}
