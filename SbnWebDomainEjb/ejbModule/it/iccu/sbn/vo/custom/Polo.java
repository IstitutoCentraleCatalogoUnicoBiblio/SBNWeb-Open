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
package it.iccu.sbn.vo.custom;

import it.iccu.sbn.ejb.vo.SerializableVO;

/**
 * ORM-Persistable Class
 */
public class Polo extends SerializableVO {

	private static final long serialVersionUID = -1153880514881100009L;

	private String cd_polo;

	private String url_indice;

	private String username;

	private String password;

	private String ute_var;

	public void setCd_polo(String value) {
		this.cd_polo = value;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public void setUrl_indice(String value) {
		this.url_indice = value;
	}

	public String getUrl_indice() {
		return url_indice;
	}

	public void setUsername(String value) {
		this.username = value;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public String getPassword() {
		return password;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

}
