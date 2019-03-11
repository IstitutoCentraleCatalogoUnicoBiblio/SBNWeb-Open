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

public class OffertaFornitoreNoteEdiVO extends SerializableVO{

	/**
	 *
	 */
	private static final long serialVersionUID = 8453578994565123142L;
	private String noteEdi;
	private String coordinateEdi;
	private String altriRiferimenti;
	private boolean flag_canc=false;


	public OffertaFornitoreNoteEdiVO (){};
	public OffertaFornitoreNoteEdiVO (String noteEd, String coordEdi, String altriRif) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.noteEdi=noteEd;
		this.coordinateEdi=coordEdi;
		this.altriRiferimenti=altriRif;

	}



	public String getAltriRiferimenti() {
		return altriRiferimenti;
	}



	public void setAltriRiferimenti(String altriRiferimenti) {
		this.altriRiferimenti = altriRiferimenti;
	}



	public String getCoordinateEdi() {
		return coordinateEdi;
	}



	public void setCoordinateEdi(String coordinateEdi) {
		this.coordinateEdi = coordinateEdi;
	}



	public String getNoteEdi() {
		return noteEdi;
	}



	public void setNoteEdi(String noteEdi) {
		this.noteEdi = noteEdi;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}



}
