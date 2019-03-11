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

import java.io.Serializable;

public class SchedaInventarioAcqVO extends SerializableVO {


	private static final long serialVersionUID = -8888107684210291153L;
	//Inventario
	private String kbibl="";
	private String kserie="";
	private String ninvent="";
	private String sezione="";
	private String colloc="";
	private String specific="";
	private String sequenza="";
	private String data="";
	private String tipoprov="";
	private String tipomat="";
	private String tipocirc="";
	private String precis="";
	private String valore="";

	public SchedaInventarioAcqVO (Serializable sbnmarcVO) {
		copyCommonProperties(this, sbnmarcVO);
	}

	public String getColloc() {
		return colloc;
	}
	public void setColloc(String colloc) {
		this.colloc = colloc;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getKbibl() {
		return kbibl;
	}
	public void setKbibl(String kbibl) {
		this.kbibl = kbibl;
	}
	public String getNinvent() {
		return ninvent;
	}
	public void setNinvent(String ninvent) {
		this.ninvent = ninvent;
	}
	public String getPrecis() {
		return precis;
	}
	public void setPrecis(String precis) {
		this.precis = precis;
	}
	public String getSequenza() {
		return sequenza;
	}
	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getSpecific() {
		return specific;
	}
	public void setSpecific(String specific) {
		this.specific = specific;
	}
	public String getTipocirc() {
		return tipocirc;
	}
	public void setTipocirc(String tipocirc) {
		this.tipocirc = tipocirc;
	}
	public String getTipomat() {
		return tipomat;
	}
	public void setTipomat(String tipomat) {
		this.tipomat = tipomat;
	}
	public String getTipoprov() {
		return tipoprov;
	}
	public void setTipoprov(String tipoprov) {
		this.tipoprov = tipoprov;
	}
	public String getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getKserie() {
		return kserie;
	}

	public void setKserie(String kserie) {
		this.kserie = kserie;
	}




}
