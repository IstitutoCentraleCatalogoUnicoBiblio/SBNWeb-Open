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
package it.iccu.sbn.web.actionforms.acquisizioni.ordini;

import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.sql.Timestamp;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class SpedizioneOrdineForm extends ActionForm {


	private static final long serialVersionUID = -6939333101591952172L;

	private final Timestamp creationTime = DaoManager.now();

	private boolean initialized;
	private boolean conferma;
	private OrdiniVO ordine;
	private String[] pulsanti;
	private List<TB_CODICI> listaStatoOrdine;
	private String tipoFormato;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public List<TB_CODICI> getListaStatoOrdine() {
		return listaStatoOrdine;
	}

	public void setListaStatoOrdine(List<TB_CODICI> listaStatoOrdine) {
		this.listaStatoOrdine = listaStatoOrdine;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public OrdiniVO getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdiniVO datiOrdine) {
		this.ordine = datiOrdine;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

}
