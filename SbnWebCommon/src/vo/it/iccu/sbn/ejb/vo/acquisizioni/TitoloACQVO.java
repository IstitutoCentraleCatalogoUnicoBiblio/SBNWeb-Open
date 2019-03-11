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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.ArrayList;
import java.util.List;

public class TitoloACQVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -545304218247755749L;
	private String bid;
	private String isbd;
	private String natura;
	private String fl_canc;
	private String codPaese;
	private String numStandard;
	private List<StrutturaTerna> numStandardArr; // tipo, numero, denotipo
	private String[] arrCodLingua;
	private String indiceISBD;

	public TitoloACQVO(String bid, String isbd, String natura)
	throws Exception {
	this.bid = bid;
	this.bid = isbd;
	this.natura = natura;
	}

	public TitoloACQVO() {
		// TODO Auto-generated constructor stub
	}

	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getFl_canc() {
		return fl_canc;
	}
	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}
	public String getIsbd() {
		return isbd;
	}
	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String[] getArrCodLingua() {
		return arrCodLingua;
	}

	public void setArrCodLingua(String[] arrCodLingua) {
		this.arrCodLingua = arrCodLingua;
	}

	public String getCodPaese() {
		return codPaese;
	}

	public void setCodPaese(String codPaese) {
		this.codPaese = codPaese;
	}

	public String getNumStandard() {
		return numStandard;
	}

	public void setNumStandard(String numStandard) {
		this.numStandard = numStandard;
	}

	public String getIndiceISBD() {
		return indiceISBD;
	}

	public void setIndiceISBD(String indiceISBD) {
		this.indiceISBD = indiceISBD;
	}


	public List<StrutturaCombo> leggiStringa(String campoRecord) {
		// riceve la stringa memorizzata nel campo del record e ne crea un array

		String[] campoRicevente=campoRecord.split("\\|");

		String tipoNum="";
		String numero="";

		List<StrutturaCombo> arrayNumStd=new ArrayList<StrutturaCombo>();

		for (int s=0; s<campoRicevente.length; s++)
		{
			if(campoRicevente[s]!=null && campoRicevente[s].trim().length()>1 )
			{
				tipoNum=campoRicevente[s].substring(0,1);
				numero=campoRicevente[s].substring(1,campoRicevente[s].length());
				try
				{
					arrayNumStd.add(new StrutturaCombo(tipoNum,numero) );
			 	} catch (Exception e) {
					e.printStackTrace();
			 	}
			}
		}
		return arrayNumStd;
	}

	public List<StrutturaTerna> getNumStandardArr() {
		return numStandardArr;
	}

	public void setNumStandardArr(List<StrutturaTerna> numStandardArr) {
		this.numStandardArr = numStandardArr;
	}

}
