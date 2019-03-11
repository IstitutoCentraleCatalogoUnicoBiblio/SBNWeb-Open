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

import java.sql.Timestamp;
import java.util.Comparator;

public class BilancioDettVO  extends SerializableVO {

	private static final long serialVersionUID = -4849528680104247148L;

	public static final Comparator<BilancioDettVO> TIPO_IMPEGNO = new Comparator<BilancioDettVO>() {

		public int compare(BilancioDettVO o1, BilancioDettVO o2) {
			return o1.getImpegno().compareTo(o2.getImpegno());
		}
	};

	private String impegno;
	private double budget;
	private String budgetStr;
	private double impegnato;
	private double fatturato;
	private double acquisito;
	private double pagato;
	private double impFatt;
	private double dispCassa;
	private double dispCompetenza;
	private double dispCompetenzaAcq;
	private boolean flag_canc;
	private Timestamp dataUpd;



	public BilancioDettVO (){};
	public BilancioDettVO ( String imp, double bdg, double impegna, double fattur, double paga  ) throws Exception {
		this.impegno = imp;
		this.budget = bdg;
		this.impegnato = impegna;
		this.fatturato = fattur;
		this.pagato = paga;
		this.impFatt= impegna - fattur;
		this.dispCassa= bdg - paga;
		this.dispCompetenza= bdg - impegna;
		this.dispCompetenzaAcq= bdg - this.acquisito;
	}


	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public String getDettaglioSintetico() {
		//String sintesi=getImpegno().trim()+ " - " +  String.valueOf(getBudget()) ;
		String sintesi=getImpegno().trim()+ " - " +  getBudgetStr() ;
		return sintesi;
	}

	public String getSalvaImp(String kchiave ) throws Exception {
		String salvaImpStrut=kchiave+ "*" +this.getImpegno().trim() ;
		return salvaImpStrut;
	}

	public double getFatturato() {
		return fatturato;
	}

	public void setFatturato(double fatturato) {
		this.fatturato = fatturato;
	}

	public double getImpegnato() {
		return impegnato;
	}

	public void setImpegnato(double impegnato) {
		this.impegnato = impegnato;
	}

	public String getImpegno() {
		return impegno;
	}

	public void setImpegno(String impegno) {
		this.impegno = impegno;
	}

	public double getPagato() {
		return pagato;
	}

	public void setPagato(double pagato) {
		this.pagato = pagato;
	}

	public double getImpFatt() {
		return impFatt;
	}

	public void setImpFatt(double impFatt) {
		this.impFatt = impFatt;
	}

	public double getDispCassa() {
		return dispCassa;
	}

	public void setDispCassa(double dispCassa) {
		this.dispCassa = dispCassa;
	}

	public double getDispCompetenza() {
		return dispCompetenza;
	}

	public void setDispCompetenza(double dispCompetenza) {
		this.dispCompetenza = dispCompetenza;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getBudgetStr() {
		return budgetStr;
	}
	public void setBudgetStr(String budgetStr) {
		this.budgetStr = budgetStr;
	}
	public double getAcquisito() {
		return acquisito;
	}
	public void setAcquisito(double acquisito) {
		this.acquisito = acquisito;
	}
	public double getDispCompetenzaAcq() {
		return dispCompetenzaAcq;
	}
	public void setDispCompetenzaAcq(double dispCompetenzaAcq) {
		this.dispCompetenzaAcq = dispCompetenzaAcq;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}

}
