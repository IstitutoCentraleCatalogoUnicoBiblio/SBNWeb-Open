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
package it.iccu.sbn.web.actionforms.gestionebibliografica.autore;

import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.InterrogazioneAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class InterrogazioneAutoreForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -7917363474401631648L;

	private InterrogazioneAutoreGeneraleVO interrGener = new InterrogazioneAutoreGeneraleVO();

	private String tipoAutore;

	public String livRicerca;
	public String provenienza;

	public String creaAut = "SI";
	public String creaAutLoc = "SI";

	public String presenzaRicLocale = "";

	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();

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
		this.interrGener.setChkTipoNomeA(false);
		this.interrGener.setChkTipoNomeB(false);
		this.interrGener.setChkTipoNomeC(false);
		this.interrGener.setChkTipoNomeD(false);
		this.interrGener.setChkTipoNomeE(false);
		this.interrGener.setChkTipoNomeG(false);
		this.interrGener.setChkTipoNomeR(false);
	}

	public InterrogazioneAutoreGeneraleVO getInterrGener() {
		return interrGener;
	}

	public void setInterrGener(InterrogazioneAutoreGeneraleVO interrGener) {
		this.interrGener = interrGener;
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}

	public String getTipoAutore() {
		return tipoAutore;
	}

	public void setTipoAutore(String tipoAutore) {
		this.tipoAutore = tipoAutore;
	}


	public String getCreaAut() {
		return creaAut;
	}

	public void setCreaAut(String creaAut) {
		this.creaAut = creaAut;
	}
	public String getCreaAutLoc() {
		return creaAutLoc;
	}

	public void setCreaAutLoc(String creaAutLoc) {
		this.creaAutLoc = creaAutLoc;
	}

	public String getPresenzaRicLocale() {
		return presenzaRicLocale;
	}

	public void setPresenzaRicLocale(String presenzaRicLocale) {
		this.presenzaRicLocale = presenzaRicLocale;
	}


}
