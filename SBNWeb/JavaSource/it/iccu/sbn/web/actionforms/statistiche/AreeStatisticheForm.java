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
package it.iccu.sbn.web.actionforms.statistiche;

import it.iccu.sbn.ejb.vo.statistiche.AreaVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class AreeStatisticheForm extends ActionForm {


	private static final long serialVersionUID = -3811945251403810569L;
	private String codPolo;
	private String codBib;


	List<AreaVO> elencoAree = new ArrayList<AreaVO>();
	String selezione;
	public static final String SUBMIT_TESTO_PULSANTE = "profilo.button.pulsante";

	public String getSUBMIT_TESTO_PULSANTE() {
		return SUBMIT_TESTO_PULSANTE;
	}

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}


	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public List<AreaVO> getElencoAree() {
		return elencoAree;
	}

	public void setElencoAree(List<AreaVO> elencoAree) {
		this.elencoAree = elencoAree;
	}

}
