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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class DettaglioTitoloGraficaVO  extends SerializableVO {

	// = DettaglioTitoloGraficaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -4969959100115734247L;

	private String livAutSpec;

	private String designMat;
	private String suppPrim;
	private String indicCol;
	private String indicTecnica;
	private String indicTec1;
	private String indicTec2;
	private String indicTec3;
	private String indicTecSta1;
	private String indicTecSta2;
	private String indicTecSta3;

	private String designFun;

	public String getDesignFun() {
		return designFun;
	}
	public void setDesignFun(String designFun) {
		this.designFun = designFun;
	}
	public String getDesignMat() {
		return designMat;
	}
	public void setDesignMat(String designMat) {
		this.designMat = designMat;
	}
	public String getIndicCol() {
		return indicCol;
	}
	public void setIndicCol(String indicCol) {
		this.indicCol = indicCol;
	}
	public String getSuppPrim() {
		return suppPrim;
	}
	public void setSuppPrim(String suppPrim) {
		this.suppPrim = suppPrim;
	}
	public String getIndicTec1() {
		return indicTec1;
	}
	public void setIndicTec1(String indicTec1) {
		this.indicTec1 = indicTec1;
	}
	public String getIndicTec2() {
		return indicTec2;
	}
	public void setIndicTec2(String indicTec2) {
		this.indicTec2 = indicTec2;
	}
	public String getIndicTec3() {
		return indicTec3;
	}
	public void setIndicTec3(String indicTec3) {
		this.indicTec3 = indicTec3;
	}
	public String getIndicTecnica() {
		return indicTecnica;
	}
	public void setIndicTecnica(String indicTecnica) {
		this.indicTecnica = indicTecnica;
	}
	public String getIndicTecSta1() {
		return indicTecSta1;
	}
	public void setIndicTecSta1(String indicTecSta1) {
		this.indicTecSta1 = indicTecSta1;
	}
	public String getIndicTecSta2() {
		return indicTecSta2;
	}
	public void setIndicTecSta2(String indicTecSta2) {
		this.indicTecSta2 = indicTecSta2;
	}
	public String getIndicTecSta3() {
		return indicTecSta3;
	}
	public void setIndicTecSta3(String indicTecSta3) {
		this.indicTecSta3 = indicTecSta3;
	}

	@Override
	public Object clone() {
		DettaglioTitoloGraficaVO inst = new DettaglioTitoloGraficaVO();
		inst.designMat = this.designMat == null ? null : new String(
			this.designMat);
		inst.suppPrim = this.suppPrim == null
			? null
			: new String(this.suppPrim);
		inst.indicCol = this.indicCol == null
			? null
			: new String(this.indicCol);
		inst.indicTecnica = this.indicTecnica == null ? null : new String(
			this.indicTecnica);
		inst.indicTec1 = this.indicTec1 == null ? null : new String(
			this.indicTec1);
		inst.indicTec2 = this.indicTec2 == null ? null : new String(
			this.indicTec2);
		inst.indicTec3 = this.indicTec3 == null ? null : new String(
			this.indicTec3);
		inst.indicTecSta1 = this.indicTecSta1 == null ? null : new String(
			this.indicTecSta1);
		inst.indicTecSta2 = this.indicTecSta2 == null ? null : new String(
			this.indicTecSta2);
		inst.indicTecSta3 = this.indicTecSta3 == null ? null : new String(
			this.indicTecSta3);
		inst.designFun = this.designFun == null ? null : new String(
			this.designFun);
		return inst;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloGraficaVO other = (DettaglioTitoloGraficaVO) obj;
		if (designFun == null) {
			if (other.designFun != null)
				return false;
		} else if (!designFun.equals(other.designFun))
			return false;
		if (designMat == null) {
			if (other.designMat != null)
				return false;
		} else if (!designMat.equals(other.designMat))
			return false;
		if (indicCol == null) {
			if (other.indicCol != null)
				return false;
		} else if (!indicCol.equals(other.indicCol))
			return false;
		if (indicTec1 == null) {
			if (other.indicTec1 != null)
				return false;
		} else if (!indicTec1.equals(other.indicTec1))
			return false;
		if (indicTec2 == null) {
			if (other.indicTec2 != null)
				return false;
		} else if (!indicTec2.equals(other.indicTec2))
			return false;
		if (indicTec3 == null) {
			if (other.indicTec3 != null)
				return false;
		} else if (!indicTec3.equals(other.indicTec3))
			return false;
		if (indicTecSta1 == null) {
			if (other.indicTecSta1 != null)
				return false;
		} else if (!indicTecSta1.equals(other.indicTecSta1))
			return false;
		if (indicTecSta2 == null) {
			if (other.indicTecSta2 != null)
				return false;
		} else if (!indicTecSta2.equals(other.indicTecSta2))
			return false;
		if (indicTecSta3 == null) {
			if (other.indicTecSta3 != null)
				return false;
		} else if (!indicTecSta3.equals(other.indicTecSta3))
			return false;
		if (indicTecnica == null) {
			if (other.indicTecnica != null)
				return false;
		} else if (!indicTecnica.equals(other.indicTecnica))
			return false;
		if (suppPrim == null) {
			if (other.suppPrim != null)
				return false;
		} else if (!suppPrim.equals(other.suppPrim))
			return false;
		return true;
	}
	public String getLivAutSpec() {
		return livAutSpec;
	}
	public void setLivAutSpec(String livAutSpec) {
		this.livAutSpec = livAutSpec;
	}



}
