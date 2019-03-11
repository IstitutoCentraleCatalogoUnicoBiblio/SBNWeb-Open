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
package it.iccu.sbn.vo.custom.servizi.sale;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;

import java.util.ArrayList;
import java.util.List;

public class Mediazione extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 5766373098714291578L;

	String codPolo;
	String codBib;
	String cd_cat_mediazione;
	String descr;
	List<String> supporti = new ArrayList<String>();

	ModelloCalendarioVO calendario;

	boolean richiedeSupporto;

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(String cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public List<String> getSupporti() {
		return supporti;
	}

	public void setSupporti(List<String> supporti) {
		this.supporti = supporti;
	}

	public ModelloCalendarioVO getCalendario() {
		return calendario;
	}

	public void setCalendario(ModelloCalendarioVO calendario) {
		this.calendario = calendario;
	}

	public boolean isRichiedeSupporto() {
		return richiedeSupporto;
	}

	public void setRichiedeSupporto(boolean richiedeSupporto) {
		this.richiedeSupporto = richiedeSupporto;
	}

	@Override
	public int getRepeatableId() {
		return (codPolo + codBib + cd_cat_mediazione).hashCode();
	}


}
