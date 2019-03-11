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
package it.iccu.sbn.ejb.utils.isbd;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class IsbdVO extends SerializableVO {

	private static final long serialVersionUID = 7436112018967566490L;

	private static final String[] SEPARATORI_T200_$A = new String[]{" / ", " : " };

	private String t200_areaTitolo;
	private String t200_areaResponsabilita;
	private String t205_areaEdizione;
	private String t210_areaPubblicazione;
	private String t215_areaDescrizioneFisica;

	public IsbdVO() {
		super();
	}

	public IsbdVO(String isbd) {
		super();
		setT200_areaTitolo(isbd);
	}

	public String getT200_areaTitolo() {
		return t200_areaTitolo;
	}

	public String getT200$a_areaTitolo() {
		int idx = ValidazioneDati.firstIndexOf(t200_areaTitolo, SEPARATORI_T200_$A);
		return (idx < 0 ? t200_areaTitolo : t200_areaTitolo.substring(0, idx));
	}

	public void setT200_areaTitolo(String areaTitolo_200) {
		this.t200_areaTitolo = trimAndSet(areaTitolo_200);
	}

	public String getT200_areaResponsabilita() {
		return t200_areaResponsabilita;
	}

	public void setT200_areaResponsabilita(String areaResponsabilita_200) {
		this.t200_areaResponsabilita = trimAndSet(areaResponsabilita_200);
	}

	public String getT210_areaPubblicazione() {
		return t210_areaPubblicazione;
	}

	public void setT210_areaPubblicazione(String areaPubblicazione_210) {
		this.t210_areaPubblicazione = trimAndSet(areaPubblicazione_210);
	}

	public String getT215_areaDescrizioneFisica() {
		return t215_areaDescrizioneFisica;
	}

	public void setT215_areaDescrizioneFisica(String areaEdizione_215) {
		this.t215_areaDescrizioneFisica = trimAndSet(areaEdizione_215);
	}

	public String getT205_areaEdizione() {
		return t205_areaEdizione;
	}

	public void setT205_areaEdizione(String areaEdizione_205) {
		this.t205_areaEdizione = trimAndSet(areaEdizione_205);
	}

}
