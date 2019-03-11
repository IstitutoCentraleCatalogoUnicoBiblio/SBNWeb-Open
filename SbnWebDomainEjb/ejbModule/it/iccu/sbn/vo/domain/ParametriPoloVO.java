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
package it.iccu.sbn.vo.domain;

import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.vo.custom.Credentials;

public class ParametriPoloVO extends SerializableVO {

	private static final long serialVersionUID = -7015155950063621834L;

	private String URL_POLO;
	private String URL_INDICE;
	private Credentials credentials;
	private SbnUserType sbnusertype;

	public String getURL_POLO() {
		return URL_POLO;
	}

	public void setURL_POLO(String uRLPOLO) {
		URL_POLO = uRLPOLO;
	}

	public String getURL_INDICE() {
		return URL_INDICE;
	}

	public void setURL_INDICE(String uRLINDICE) {
		URL_INDICE = uRLINDICE;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public SbnUserType getSbnusertype() {
		return sbnusertype;
	}

	public void setSbnusertype(SbnUserType sbnusertype) {
		this.sbnusertype = sbnusertype;
	}

}
