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
package it.iccu.sbn.web.actionforms.gestionestampe.semantica;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ActionPathVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class SelezioneStampaSemanticaForm extends ActionForm {


	private static final long serialVersionUID = 3378580392650029105L;

	public static final String SUBMIT_TESTO_PULSANTE = "stampa.semantica.batch.button";

	private List<ActionPathVO> listaStampe = new ArrayList<ActionPathVO>();
	private String selezione;

	private boolean initialized;

	public String getSUBMIT_TESTO_PULSANTE() {
		return SUBMIT_TESTO_PULSANTE;
	}

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public List<ActionPathVO> getListaStampe() {
		return listaStampe;
	}

	public void setListaStampe(List<ActionPathVO> listaStampe) {
		this.listaStampe = listaStampe;
	}

}
