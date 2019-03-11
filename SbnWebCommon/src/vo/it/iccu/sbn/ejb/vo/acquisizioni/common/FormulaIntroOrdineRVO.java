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
package it.iccu.sbn.ejb.vo.acquisizioni.common;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

public class FormulaIntroOrdineRVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -3876845509427391503L;
	private int progr;
	private String lang;
	private String cd_tipo_lav;
	private String intro;

	public FormulaIntroOrdineRVO() {
		super();
	}

	public FormulaIntroOrdineRVO(String lang, String cd_tipo_lav, String intro) {
		this.lang = lang;
		this.cd_tipo_lav = cd_tipo_lav;
		this.intro = intro;

	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = trimAndSet(lang);
	}

	public String getCd_tipo_lav() {
		return cd_tipo_lav;
	}

	public void setCd_tipo_lav(String cd_tipo_lav) {
		this.cd_tipo_lav = trimAndSet(cd_tipo_lav);
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = trimAndSet(intro);
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cd_tipo_lav == null) ? 0 : cd_tipo_lav.hashCode());
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormulaIntroOrdineRVO other = (FormulaIntroOrdineRVO) obj;
		if (cd_tipo_lav == null) {
			if (other.cd_tipo_lav != null)
				return false;
		} else if (!cd_tipo_lav.equals(other.cd_tipo_lav))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		return true;
	}

	@Override
	public int getRepeatableId() {
		return progr;
	}

}
