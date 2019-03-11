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
package it.iccu.sbn.vo.custom.esporta;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoEstrazioneUnimarc;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoOutput;

public class EsportaDecorator extends EsportaVO {

	private static final long serialVersionUID = 8535698095033997542L;

	public String getTipoOutputJSP() {
		return tipoOutput.name();
	}

	public void setTipoOutputJSP(String tipoOutput) {
		this.tipoOutput = TipoOutput.valueOf(tipoOutput);
	}

	public String getTipoInputJSP() {
		return tipoInput.name();
	}

	public void setTipoInputJSP(String tipoOutput) {
		this.tipoInput = TipoOutput.valueOf(tipoOutput);
	}

	public String getTipoEstrazioneJSP() {
		if (tipoEstrazione == null)
			return "";
		return tipoEstrazione.name();
	}

	public void setTipoEstrazioneJSP(String tipoEstrazione) {
		this.tipoEstrazione = TipoEstrazioneUnimarc.valueOf(tipoEstrazione);
	}

}
