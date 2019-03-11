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

public class OffertaFornitoreAutoreVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 7421329611816297183L;
	private String chiaveAutore;
	private String autore;
	private String tipoAutore;
	private String responsabilitaAutore;
	private boolean flag_canc=false;


	public OffertaFornitoreAutoreVO (){};
	public OffertaFornitoreAutoreVO (String kAut, String aut, String tipoAut, String respAut) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.chiaveAutore=kAut;
		this.autore=aut;
		this.tipoAutore=tipoAut;
		this.responsabilitaAutore=respAut;

	}



	public String getAutore() {
		return autore;
	}



	public void setAutore(String autore) {
		this.autore = autore;
	}



	public String getChiaveAutore() {
		return chiaveAutore;
	}



	public void setChiaveAutore(String chiaveAutore) {
		this.chiaveAutore = chiaveAutore;
	}



	public String getResponsabilitaAutore() {
		return responsabilitaAutore;
	}



	public void setResponsabilitaAutore(String responsabilitaAutore) {
		this.responsabilitaAutore = responsabilitaAutore;
	}



	public String getTipoAutore() {
		return tipoAutore;
	}



	public void setTipoAutore(String tipoAutore) {
		this.tipoAutore = tipoAutore;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}




}
