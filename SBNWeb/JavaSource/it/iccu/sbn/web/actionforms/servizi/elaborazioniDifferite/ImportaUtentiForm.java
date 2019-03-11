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
package it.iccu.sbn.web.actionforms.servizi.elaborazioniDifferite;


import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchImportaUtentiVO;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ImportaUtentiForm extends ActionForm {


	private static final long serialVersionUID = 8260340448546188279L;
	//private String idFileUtenti;
	private ParametriBatchImportaUtentiVO importa = new ParametriBatchImportaUtentiVO();
	private String dataDa;
	private String nomeF;
	private String polo;
	private FormFile uploadImmagine;
	private List listaUtentiDaFile;

	//LFV 18/07/2018 specifica se il csv è di esse3
	private Boolean isEsse3 = false;

	/*
	 public void setIdFileUtenti(String idFileUtenti) {

		this.idFileUtenti = idFileUtenti;
	}
	*/

	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}

	public List getListaUtentiDaFile() {
		return listaUtentiDaFile;
	}

	public void setListaUtentiDaFile(List listaBidDaFile) {
		this.listaUtentiDaFile = listaBidDaFile;
	}

	public List addListaUtentiDaFile(String record) {
		listaUtentiDaFile.add(record);
		return listaUtentiDaFile;
	}

	public String getDataDa() {
		return dataDa;
	}

	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}

	public String getNomeFile() {
		return nomeF;
	}

	public void setNomeFile(String nomeF) {
		this.nomeF = nomeF;

	}

	public String getCodPolo() {
		return polo;
	}

	public Boolean getIsEsse3() {
		return isEsse3;
	}

	public void setIsEsse3(Boolean isEsse3) {
		this.isEsse3 = isEsse3;
	}

	public boolean isEsse3() {
		return isEsse3;
	}




	/*
	public ParametriBatchImportaUtentiVO getImporta() {
		return importa;
	}

	public void setImporta(ParametriBatchImportaUtentiVO importa) {
		this.importa = importa;
	}
*/

}
