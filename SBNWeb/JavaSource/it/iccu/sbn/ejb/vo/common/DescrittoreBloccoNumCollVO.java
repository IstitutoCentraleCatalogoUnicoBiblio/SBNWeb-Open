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
package it.iccu.sbn.ejb.vo.common;

import java.io.Serializable;
import java.util.List;


public class DescrittoreBloccoNumCollVO extends DescrittoreBloccoVO implements Serializable {


	private static final long serialVersionUID = -405587186610965152L;
	private int countColl;
	private String ultLoc;
	private String ultOrdLoc;
	private String ultSpec;
	private String ultKeyLoc;

	public DescrittoreBloccoNumCollVO(DescrittoreBloccoVO descrBloccoVO, int countColl, String ultLoc,  String ultSpec, String ultOrdLoc, String ultKeyLoc) {
		super(descrBloccoVO);
		this.countColl = countColl;
		this.ultLoc = ultLoc;
		this.ultOrdLoc = ultOrdLoc;
		this.ultSpec = ultSpec;
		this.ultKeyLoc = ultKeyLoc;
	}

	public int getCountColl() {
		return countColl;
	}

	public void setCountColl(int countColl) {
		this.countColl = countColl;
	}

	public String getUltLoc() {
		return ultLoc;
	}

	public void setUltLoc(String ultLoc) {
		this.ultLoc = ultLoc;
	}

	public void setLista(List lista) {
		super.lista = lista;
	}

	public void setIdLista(String idLista) {
		super.idLista = idLista;
	}

	public String getUltKeyLoc() {
		return ultKeyLoc;
	}

	public void setUltKeyLoc(String ultKeyLoc) {
		this.ultKeyLoc = ultKeyLoc;
	}

	public String getUltSpec() {
		return ultSpec;
	}

	public void setUltSpec(String ultSpec) {
		this.ultSpec = ultSpec;
	}

	public String getUltOrdLoc() {
		return ultOrdLoc;
	}

	public void setUltOrdLoc(String ultOrdLoc) {
		this.ultOrdLoc = ultOrdLoc;
	}

}
