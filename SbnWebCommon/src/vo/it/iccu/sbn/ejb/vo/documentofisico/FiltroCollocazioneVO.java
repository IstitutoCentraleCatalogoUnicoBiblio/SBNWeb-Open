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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class FiltroCollocazioneVO extends SerializableVO {

	private static final long serialVersionUID = -1244342202731303630L;

	private String codPolo;
	private String codBib;
	private String codSezione;
	private boolean sezioneSpazio;

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

	public String getCodSezione() {
		return codSezione;
	}

	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}

	public boolean isSezioneSpazio() {
		return sezioneSpazio;
	}

	public void setSezioneSpazio(boolean sezioneSpazio) {
		this.sezioneSpazio = sezioneSpazio;
	}

	@Override
	public boolean isEmpty() {
		return length(codSezione) < 1 && !sezioneSpazio;
	}

}
