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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.sql.Timestamp;

public class PoloVO extends SerializableVO {

	private static final long serialVersionUID = -7772043339696094325L;

	private String cd_polo;
	private String url_indice;
	private String username;
	private String password;
	private String denominazione;
	private String ute_var;
	private Timestamp ts_ins;
	private String ute_ins;
	private Timestamp ts_var;
	private String url_polo;
	private String username_polo;
	private String sbnMarcBuildTime = DateUtil.now();
	private String sbnWebBuildTime = DateUtil.now();

	public PoloVO(Tbf_polo polo) {
		copyCommonProperties(this, polo);
	}

	public void setCd_polo(String value) {
		this.cd_polo = value;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public String getORMID() {
		return getCd_polo();
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

	/**
	 * Denominazione del polo
	 */
	public void setDenominazione(String value) {
		this.denominazione = trimAndSet(value);
	}

	/**
	 * Denominazione del polo
	 */
	public String getDenominazione() {
		return denominazione;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setUrl_polo(String value) {
		this.url_polo = value;
	}

	public String getUrl_polo() {
		return url_polo;
	}

	public void setUsername_polo(String value) {
		this.username_polo = value;
	}

	public String getUsername_polo() {
		return username_polo;
	}

	public String toString() {
		return String.valueOf(getCd_polo());
	}

	private boolean _saved = false;

	private String sbnWebBuildNumber;

	public void onSave() {
		_saved = true;
	}

	public void onLoad() {
		_saved = true;
	}

	public boolean isSaved() {
		return _saved;
	}

	public String getSbnMarcBuildTime() {
		return sbnMarcBuildTime;
	}

	public void setSbnMarcBuildTime(String sbnMarcBuildTime) {
		this.sbnMarcBuildTime = sbnMarcBuildTime;
	}

	public String getSbnWebBuildTime() {
		return sbnWebBuildTime;
	}

	public void setSbnWebBuildTime(String sbnWebBuildTime) {
		this.sbnWebBuildTime = sbnWebBuildTime;
	}

	public void setSbnWebBuildNumber(String sbnWebBuildNumber) {
		this.sbnWebBuildNumber = sbnWebBuildNumber;
	}

	public String getSbnWebBuildNumber() {
		return sbnWebBuildNumber;
	}

}
