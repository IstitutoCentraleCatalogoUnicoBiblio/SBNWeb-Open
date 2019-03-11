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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.polo.orm.documentofisico.Trc_formati_sezioni;

public class ForSezMiscVO extends SerializableVO{

	/**
	 *
	 */
	private static final long serialVersionUID = 1346207208029645095L;

	private int num1;
	private int num2;
	private Trc_formati_sezioni rec = new Trc_formati_sezioni();
	private String msg;
	private int serie;
	private int numMancanti;

	public int getNum1() {
		return num1;
	}
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	public int getNum2() {
		return num2;
	}
	public void setNum2(int num2) {
		this.num2 = num2;
	}
	public Trc_formati_sezioni getRec() {
		return rec;
	}
	public void setRec(Trc_formati_sezioni rec) {
		this.rec = rec;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getSerie() {
		return serie;
	}
	public void setSerie(int serie) {
		this.serie = serie;
	}
	public int getNumMancanti() {
		return numMancanti;
	}
	public void setNumMancanti(int numMancanti) {
		this.numMancanti = numMancanti;
	}

}
