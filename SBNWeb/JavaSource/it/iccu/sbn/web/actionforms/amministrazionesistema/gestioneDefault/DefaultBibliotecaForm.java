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
package it.iccu.sbn.web.actionforms.amministrazionesistema.gestioneDefault;

import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.AreaVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class DefaultBibliotecaForm extends ActionForm {


	private static final long serialVersionUID = -6764668617562462686L;
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

	public List<AreaVO> getelencoAree() {
		return elencoAree;
	}

	public void setelencoAree(List<AreaVO> elencoAree) {
		this.elencoAree = elencoAree;
	}

}
