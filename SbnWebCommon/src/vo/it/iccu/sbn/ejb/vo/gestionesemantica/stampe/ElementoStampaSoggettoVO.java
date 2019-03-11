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
package it.iccu.sbn.ejb.vo.gestionesemantica.stampe;

import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;

public class ElementoStampaSoggettoVO extends ElementoStampaSogDesVO {

	private static final long serialVersionUID = 8473158342084319302L;
	private String tipoSoggetto;
	private SubReportVO subReportDescrittori;
	private SubReportVO subReportTitoli;

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public void setSubReportDescrittori(SubReportVO subReportDescrittori) {
		this.subReportDescrittori = subReportDescrittori;
	}

	public void setSubReportTitoli(SubReportVO subReportTitoli) {
		this.subReportTitoli = subReportTitoli;
	}

	public SubReportVO getSubReportDescrittori() {
		return subReportDescrittori;
	}

	public SubReportVO getSubReportTitoli() {
		return subReportTitoli;
	}

}
