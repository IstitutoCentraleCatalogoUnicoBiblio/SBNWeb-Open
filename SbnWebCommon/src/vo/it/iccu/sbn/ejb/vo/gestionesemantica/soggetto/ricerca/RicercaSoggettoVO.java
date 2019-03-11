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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca;

import it.iccu.sbn.ejb.vo.SerializableVO;


public abstract class RicercaSoggettoVO extends SerializableVO {

	private static final long serialVersionUID = -5027351186710008821L;
	private String testoSogg="";
	private String cid="";

	protected boolean utilizzati = false;

	abstract public int count();

	public RicercaSoggettoVO() {
		super();
	}

	public RicercaSoggettoVO(String testoSogg, String cid) {
		this.testoSogg = testoSogg;
		this.cid = cid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = toUpperCase(cid);
	}

	public String getTestoSogg() {
		return testoSogg;
	}

	public void setTestoSogg(String testoSogg) {
		this.testoSogg = testoSogg;
	}

	@Override
	public boolean isEmpty() {
		return (isNull(cid) && isNull(testoSogg));
	}

	public boolean isUtilizzati() {
		return utilizzati;
	}

	public void setUtilizzati(boolean utilizzati) {
		this.utilizzati = utilizzati;
	}

}
