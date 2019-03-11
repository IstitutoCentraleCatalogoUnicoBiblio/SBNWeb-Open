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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class EsportaAuthorityVO extends ExportCommonVO {

	private static final long serialVersionUID = -750496764924347930L;

	private SbnAuthority authority;

	public SbnAuthority getAuthority() {
		return authority;
	}

	public void setAuthority(SbnAuthority authority) {
		this.authority = authority;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (!isFilled(inputFile))
			throw new ValidationException(SbnErrorTypes.UNI_FILE_ID_NON_PRESENTE);
	}



}
