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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class DettaglioGenericoLegameVO  extends SerializableVO {

	// = DettaglioGenericoLegameVO.class.hashCode();

    /**
	 *
	 */
	private static final long serialVersionUID = -2842463239272067491L;
	private String identificativo;
    private String descr;
    private String codiceRelaz;
    private String responsabilita;
    private String notaLegame;


	public String getCodiceRelaz() {
		return codiceRelaz;
	}
	public void setCodiceRelaz(String codiceRelaz) {
		this.codiceRelaz = codiceRelaz;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getIdentificativo() {
		return identificativo;
	}
	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}
	public String getNotaLegame() {
		return notaLegame;
	}
	public void setNotaLegame(String notaLegame) {
		this.notaLegame = notaLegame;
	}
	public String getResponsabilita() {
		return responsabilita;
	}
	public void setResponsabilita(String responsabilita) {
		this.responsabilita = responsabilita;
	}




}
