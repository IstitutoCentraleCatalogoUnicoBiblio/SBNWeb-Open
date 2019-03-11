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
package it.iccu.sbn.web.actionforms.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.AreaBatchVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ElaborazioniDifferiteConfig;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;

import org.apache.struts.action.ActionForm;

public class InvioElaborazioniDifferiteForm extends ActionForm {


	private static final long serialVersionUID = -5622576265508266527L;
	public static final String SUBMIT_TESTO_PULSANTE = "elab.diff.selez.button";

	public enum TipoVisualizzazione {
		AREA,
		ATTIVITA;
	}

	private ElaborazioniDifferiteConfig config;
	private String selez;
	private boolean sessione;
	private TipoVisualizzazione tipoVisualizzazione = TipoVisualizzazione.AREA;
	private AreaBatchVO area = new AreaBatchVO();
	private TipoAttivita tipoAttivita = TipoAttivita.FUNZIONE;

	public String getSUBMIT_TESTO_PULSANTE() {
		return SUBMIT_TESTO_PULSANTE;
	}

	public ElaborazioniDifferiteConfig getConfig() {
		return config;
	}

	public void setConfig(ElaborazioniDifferiteConfig config) {
		this.config = config;
	}

	public String getSelez() {
		return selez;
	}

	public void setSelez(String selez) {
		this.selez = selez;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public TipoVisualizzazione getTipoVisualizzazione() {
		return tipoVisualizzazione;
	}

	public void setTipoVisualizzazione(TipoVisualizzazione tipoVisualizzazione) {
		this.tipoVisualizzazione = tipoVisualizzazione;
	}

	public AreaBatchVO getArea() {
		return area;
	}

	public void setArea(AreaBatchVO area) {
		this.area = area;
	}

	public TipoAttivita getTipoAttivita() {
		return tipoAttivita;
	}

	public void setTipoAttivita(TipoAttivita tipoAttivita) {
		this.tipoAttivita = tipoAttivita;
	}

}
