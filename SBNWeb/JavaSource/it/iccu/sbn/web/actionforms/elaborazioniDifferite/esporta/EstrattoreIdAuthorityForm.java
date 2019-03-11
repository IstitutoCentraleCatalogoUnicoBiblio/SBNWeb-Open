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
package it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaAuthorityVO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class EstrattoreIdAuthorityForm extends ActionForm {


	private static final long serialVersionUID = 1756213106164792779L;
	private boolean initialized;
	private EsportaAuthorityVO esporta = new EsportaAuthorityVO();
	private FormFile inputFile;
	private String extractionTime;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public EsportaAuthorityVO getEsporta() {
		return esporta;
	}

	public void setEsporta(EsportaAuthorityVO esporta) {
		this.esporta = esporta;
	}

	public FormFile getInputFile() {
		return inputFile;
	}

	public void setInputFile(FormFile inputFile) {
		this.inputFile = inputFile;
	}

	public String getExtractionTime() {
		return extractionTime;
	}

	public void setExtractionTime(String extractionTime) {
		this.extractionTime = extractionTime;
	}

}
