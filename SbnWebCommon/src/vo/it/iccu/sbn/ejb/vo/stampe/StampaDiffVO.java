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
package it.iccu.sbn.ejb.vo.stampe;

public class StampaDiffVO extends StampaVo {

	private static final long serialVersionUID = 954288195649363767L;

	/**
	 * metodo di salvataggio da utilizzare
	 * - FILE
	 * - OUTPUT
	 * - DB
	 * - MAIL
	 * attualmenete salveremo solo su DB (forse)
	 */
	private String modalita;
	private String tipoRegistro;
	private boolean modelloPrelievo;
	private String dataPrelievo;
	private String motivoPrelievo;
	/**
	 * Contiene il VO che permette la ricerca
	 * tale property è generalizzata per TUTTE LE STAMPE
	 */
	private Object parametri;

	public String getModalita() {
		return modalita;
	}

	public void setModalita(String modalita) {
		this.modalita = modalita;
	}
	public Object getParametri() {
		return parametri;
	}

	public void setParametri(Object parametri) {
		this.parametri = parametri;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public boolean isModelloPrelievo() {
		return modelloPrelievo;
	}

	public void setModelloPrelievo(boolean modelloPrelievo) {
		this.modelloPrelievo = modelloPrelievo;
	}

	public String getDataPrelievo() {
		return dataPrelievo;
	}

	public void setDataPrelievo(String dataPrelievo) {
		this.dataPrelievo = dataPrelievo;
	}

	public String getMotivoPrelievo() {
		return motivoPrelievo;
	}

	public void setMotivoPrelievo(String motivoPrelievo) {
		this.motivoPrelievo = motivoPrelievo;
	}
}
