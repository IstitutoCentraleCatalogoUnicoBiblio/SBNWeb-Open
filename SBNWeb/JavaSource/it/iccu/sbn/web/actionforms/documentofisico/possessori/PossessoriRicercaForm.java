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
package it.iccu.sbn.web.actionforms.documentofisico.possessori;

import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class PossessoriRicercaForm extends ActionForm {



	private static final long serialVersionUID = 9218683787190722861L;
	private PossessoriRicercaVO ricercaPoss = new PossessoriRicercaVO();
	private String tipoAutore;

	public String livRicerca;
	public boolean sessione;

	public String creaPoss = "SI";

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
//		this.ricercaPoss.setChkTipoNomeA(false);
//		this.ricercaPoss.setChkTipoNomeB(false);
//		this.ricercaPoss.setChkTipoNomeC(false);
//		this.ricercaPoss.setChkTipoNomeD(false);
//		this.ricercaPoss.setChkTipoNomeE(false);
//		this.ricercaPoss.setChkTipoNomeG(false);
//		this.ricercaPoss.setChkTipoNomeR(false);

	}

	public PossessoriRicercaVO getRicercaPoss() {
		return ricercaPoss;
	}

	public void setRicercaPoss(PossessoriRicercaVO ricercaPoss) {
		this.ricercaPoss = ricercaPoss;
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

	public String getCreaPoss() {
		return creaPoss;
	}

	public void setCreaPoss(String creaPoss) {
		this.creaPoss = creaPoss;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

}
