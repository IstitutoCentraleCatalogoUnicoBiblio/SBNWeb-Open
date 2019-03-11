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
package it.iccu.sbn.ejb.vo.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.common.RicercaBaseVO;

public class RicercaAutorizzazioneVO extends RicercaBaseVO<Object> {

	private static final long serialVersionUID = 5023222852515602721L;
	private String descrizione;
	private String codice;
	private boolean nuovaAut;

	private boolean soloILL = false;

	public boolean isNuovaAut() {
		return nuovaAut;
	}

	public void setNuovaAut(boolean nuovaAut) {
		this.nuovaAut = nuovaAut;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public boolean isSoloILL() {
		return soloILL;
	}

	public void setSoloILL(boolean soloILL) {
		this.soloILL = soloILL;
	}

}
