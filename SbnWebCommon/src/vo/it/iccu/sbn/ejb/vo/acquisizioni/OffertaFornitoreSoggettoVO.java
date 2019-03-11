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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class OffertaFornitoreSoggettoVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 8133523099262365657L;
	private String soggetto;
	private String chiaveSoggetto;
	private boolean flag_canc=false;


	public OffertaFornitoreSoggettoVO (){};
	public OffertaFornitoreSoggettoVO (String sogg, String kSogg) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.soggetto=sogg;
		this.chiaveSoggetto=kSogg;

	}


	public String getChiaveSoggetto() {
		return chiaveSoggetto;
	}



	public void setChiaveSoggetto(String chiaveSoggetto) {
		this.chiaveSoggetto = chiaveSoggetto;
	}



	public String getSoggetto() {
		return soggetto;
	}



	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}




}
