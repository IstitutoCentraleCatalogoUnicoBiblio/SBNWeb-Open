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
package it.iccu.sbn.web.actionforms.common;

import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriBatchSoggettoBaseVO;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class MigrazioneForm extends ActionForm {


	private static final long serialVersionUID = 7141889853344404443L;
	private String userName;
	private String password;

	private String codBib;
	private String codSez;
	private String startLoc;
	private String endLoc;

	private FormFile fileSoggetti;

	// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per
	// gestione lista bid locali da confrontare con oggetti di Indice
	public String presenzaLoadFile = "NO";
	private FormFile uploadImmagine;
	private List<String> listaBidDaFile;

	private String email;
	private String idBatch;

	private String idBatchAllinea;

	private ParametriBatchSoggettoBaseVO parametri = new ParametriBatchSoggettoBaseVO();

	private String isil;

	private String ill_xml;

	public String getPassword() {
		return password;
	}

	public void setPasswoOrd(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(String startLoc) {
		this.startLoc = startLoc;
	}

	public String getEndLoc() {
		return endLoc;
	}

	public void setEndLoc(String endLoc) {
		this.endLoc = endLoc;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ParametriBatchSoggettoBaseVO getParametri() {
		return parametri;
	}

	public void setParametri(ParametriBatchSoggettoBaseVO parametri) {
		this.parametri = parametri;
	}

	public FormFile getFileSoggetti() {
		return fileSoggetti;
	}

	public void setFileSoggetti(FormFile fileSoggetti) {
		this.fileSoggetti = fileSoggetti;
	}

	public String getPresenzaLoadFile() {
		return presenzaLoadFile;
	}

	public void setPresenzaLoadFile(String presenzaLoadFile) {
		this.presenzaLoadFile = presenzaLoadFile;
	}

	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}

	public List<String> getListaBidDaFile() {
		return listaBidDaFile;
	}

	public void setListaBidDaFile(List<String> listaBidDaFile) {
		this.listaBidDaFile = listaBidDaFile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdBatch() {
		return idBatch;
	}

	public void setIdBatch(String idBatch) {
		this.idBatch = idBatch;
	}

	public void setIdBatchAllinea(String idBatchAllinea) {
		this.idBatchAllinea = idBatchAllinea;
	}

	public String getIdBatchAllinea() {
		return idBatchAllinea;
	}

	public String getIsil() {
		return isil;
	}

	public void setIsil(String isil) {
		this.isil = isil;
	}

	public String getIll_xml() {
		return ill_xml;
	}

	public void setIll_xml(String ill_xml) {
		this.ill_xml = ill_xml;
	}
}
