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
package it.iccu.sbn.web2.navigation;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class NavigationBlocchiInfo extends SerializableVO {

	private static final long serialVersionUID = 4447201432975328763L;
	private int numBlocco;
	private int totBlocchi;
	private int numNotizie;

	private String livelloRicerca;
	private String numBloccoFormProperty;
	private String totBlocchiFormProperty;
	private String numNotizieFormProperty;

	private int elementiPerBlocco;


	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public NavigationBlocchiInfo(int numBlocco, int totBlocchi, int numNotizie, int elementiPerBlocco,
			String livelloRicerca, String numBloccoFormProperty, String totBlocchiFormProperty,
			String numNotizieFormProperty) {
		super();
		this.numBlocco = numBlocco;
		this.totBlocchi = totBlocchi;
		this.numNotizie = numNotizie;
		this.livelloRicerca = livelloRicerca;
		this.numBloccoFormProperty = numBloccoFormProperty;
		this.totBlocchiFormProperty = totBlocchiFormProperty;
		this.numNotizieFormProperty = numNotizieFormProperty;
		this.elementiPerBlocco = elementiPerBlocco;

	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public String getNumBloccoFormProperty() {
		return numBloccoFormProperty;
	}

	public void setNumBloccoFormProperty(String numBloccoFormProperty) {
		this.numBloccoFormProperty = numBloccoFormProperty;
	}

	public String getNumNotizieFormProperty() {
		return numNotizieFormProperty;
	}

	public void setNumNotizieFormProperty(String numNotizieFormProperty) {
		this.numNotizieFormProperty = numNotizieFormProperty;
	}

	public String getTotBlocchiFormProperty() {
		return totBlocchiFormProperty;
	}

	public void setTotBlocchiFormProperty(String totBlocchiFormProperty) {
		this.totBlocchiFormProperty = totBlocchiFormProperty;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(int elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}

}
