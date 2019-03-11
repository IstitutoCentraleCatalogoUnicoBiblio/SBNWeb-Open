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

public class OffertaFornitoreIsbdVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -4289799653144955483L;
	private String chiaveTitoloIsbd;
	private String descrizioneIsbd;
	private boolean flag_canc=false;


	public OffertaFornitoreIsbdVO (){};
	public OffertaFornitoreIsbdVO (String chiaveIsbd, String desIsbd) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.chiaveTitoloIsbd=chiaveIsbd;
		this.descrizioneIsbd=desIsbd;

	}



	public String getChiaveTitoloIsbd() {
		return chiaveTitoloIsbd;
	}



	public void setChiaveTitoloIsbd(String chiaveTitoloIsbd) {
		this.chiaveTitoloIsbd = chiaveTitoloIsbd;
	}



	public String getDescrizioneIsbd() {
		return descrizioneIsbd;
	}



	public void setDescrizioneIsbd(String descrizioneIsbd) {
		this.descrizioneIsbd = descrizioneIsbd;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}




}
