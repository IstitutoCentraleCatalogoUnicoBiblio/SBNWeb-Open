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
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica.luogo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.InterrogazioneLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class InterrogazioneLuogoForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -8714143391234502384L;

	private InterrogazioneLuogoGeneraleVO interrGener = new InterrogazioneLuogoGeneraleVO();

	public String livRicerca;
	public String provenienza;
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();

	public String creaLuo = "SI";
	public String creaLuoLoc = "SI";


	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
//		this.interrGener.setRicLocale(false);
//		this.interrGener.setRicIndice(false);
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}

	public InterrogazioneLuogoGeneraleVO getInterrGener() {
		return interrGener;
	}

	public void setInterrGener(InterrogazioneLuogoGeneraleVO interrGener) {
		this.interrGener = interrGener;
	}

	public String getCreaLuo() {
		return creaLuo;
	}

	public void setCreaLuo(String creaLuo) {
		this.creaLuo = creaLuo;
	}

	public String getCreaLuoLoc() {
		return creaLuoLoc;
	}

	public void setCreaLuoLoc(String creaLuoLoc) {
		this.creaLuoLoc = creaLuoLoc;
	}

}
