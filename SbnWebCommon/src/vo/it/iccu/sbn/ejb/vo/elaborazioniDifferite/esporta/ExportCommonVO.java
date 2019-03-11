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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriTimestampRangeVO;

import java.util.List;

public abstract class ExportCommonVO extends ParametriTimestampRangeVO {

	private static final long serialVersionUID = 592752856075474486L;
	private boolean exportDB = true;
	private List<BibliotecaVO> listaBib;
	private String paese1;
	private String paese2;

	protected String inputFile;

	public boolean isExportDB() {
		return exportDB;
	}

	public void setExportDB(boolean exportDB) {
		this.exportDB = exportDB;
	}

	public List<BibliotecaVO> getListaBib() {
		return listaBib;
	}

	public void setListaBib(List<BibliotecaVO> listaBib) {
		this.listaBib = listaBib;
	}

	public String getPaese1() {
		return paese1;
	}

	public void setPaese1(String paese1) {
		this.paese1 = paese1;
	}

	public String getPaese2() {
		return paese2;
	}

	public void setPaese2(String paese2) {
		this.paese2 = paese2;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

}
