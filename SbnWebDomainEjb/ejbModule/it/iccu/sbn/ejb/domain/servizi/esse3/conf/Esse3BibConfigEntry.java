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
package it.iccu.sbn.ejb.domain.servizi.esse3.conf;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.net.URL;

public class Esse3BibConfigEntry extends SerializableVO {

	private static final long serialVersionUID = -8909022942135831339L;

	private String id;
	private String apikey;
	private URL login_ws_url;
	private String cd_bib;

	// Getter Methods

	public String getId() {
		return id;
	}

	public String getApikey() {
		return apikey;
	}

	public URL getLogin_ws_url() {
		return login_ws_url;
	}

	public String getCd_bib() {
		return cd_bib;
	}

	// Setter Methods

	public void setId(String id) {
		this.id = id;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setLogin_ws_url(URL login_url) {
		this.login_ws_url = login_url;
	}

	public void setCd_bib(String cd_bib) {
		this.cd_bib = cd_bib;
	}
}