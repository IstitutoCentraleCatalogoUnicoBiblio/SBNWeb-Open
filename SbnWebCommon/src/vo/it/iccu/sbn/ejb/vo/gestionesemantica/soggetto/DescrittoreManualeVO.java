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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;


public class DescrittoreManualeVO extends CreaVariaDescrittoreVO {

	private static final long serialVersionUID = -555430766748327220L;
	private String dataInsDescr;
	private String dataVarDescr;

	public String getDataInsDescr() {
		return dataInsDescr;
	}

	public void setDataInsDescr(String dataInsDescr) {
		this.dataInsDescr = dataInsDescr;
	}

	public String getDataVarDescr() {
		return dataVarDescr;
	}

	public void setDataVarDescr(String dataVarDescr) {
		this.dataVarDescr = dataVarDescr;
	}




}
