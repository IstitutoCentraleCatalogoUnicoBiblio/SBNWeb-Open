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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class LocalizzazioneMassivaVO extends
		ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -7591963226085419607L;

	private String inputFile;
	private String tipoInput;

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getTipoInput() {
		return tipoInput;
	}

	public void setTipoInput(String tipoInput) {
		this.tipoInput = tipoInput;
	}

	private enum TipoInput {
		DB,
		FILE;
	}

	@Override
	public void validate() throws ValidationException {
		TipoInput type = TipoInput.valueOf(tipoInput);
		if (type == TipoInput.FILE && !isFilled(inputFile))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "File");

		super.validate();
	}

}
