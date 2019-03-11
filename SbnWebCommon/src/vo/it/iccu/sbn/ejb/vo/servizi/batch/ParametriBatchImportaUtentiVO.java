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
package it.iccu.sbn.ejb.vo.servizi.batch;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.List;

public class ParametriBatchImportaUtentiVO extends ParametriRichiestaElaborazioneDifferitaVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String importFileName;
	private List listaUtentiDaFile;
	private String codErr;
	public static String dataDa;
	public static String nomeF;
	//LFV 18/07/2018
	private Boolean isEsse3 = false;

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public void setListaUtentiDaFile(List listaUtentiDaFile) {
		// TODO Auto-generated method stub

	}

	public List getListaUtenti() {
		return listaUtentiDaFile;
	}

	public void setListaUtenti(List listaUtentiDaFile) {
		this.listaUtentiDaFile = listaUtentiDaFile;
	}

	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	public void setDataAut(String dataDa) {
		this.dataDa = dataDa;
	}

	public void setNomeF(String nomeF) {
		this.nomeF = nomeF;
	}

	public Boolean isEsse3() {
		return isEsse3;
	}

	public void setIsEsse3(Boolean isEsse3) {
		this.isEsse3 = isEsse3;
	}

}
