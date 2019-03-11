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
package it.iccu.sbn.ejb.vo.statistiche;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DettVarStatisticaVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 499091494781199730L;

	private String idStatistica;
	private String nomeVar;
	private String tipoVar;
	private String valore;
	private String etichettaNomeVar;
	private String obbligatorio;
	private List listaCodiciVO = new ArrayList();

	public DettVarStatisticaVO(){
	}

	public String getIdStatistica() {
		return idStatistica;
	}

	public void setIdStatistica(String idStatistica) {
		this.idStatistica = idStatistica;
	}

	public String getNomeVar() {
		return nomeVar;
	}

	public void setNomeVar(String nomeVar) {
		this.nomeVar = nomeVar;
	}

	public String getTipoVar() {
		return tipoVar;
	}

	public void setTipoVar(String tipoVar) {
		this.tipoVar = tipoVar;
	}

	public String getEtichettaNomeVar() {
		return etichettaNomeVar;
	}

	public void setEtichettaNomeVar(String etichettaNomeVar) {
		this.etichettaNomeVar = etichettaNomeVar;
	}


	public String getValore() {
		return valore;
	}


	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public List getListaCodiciVO() {
		return listaCodiciVO;
	}

	public void setListaCodiciVO(List listaCodiciVO) {
		this.listaCodiciVO = listaCodiciVO;
	}

	@Override
	public String toString() {
		return "DettVarStatisticaVO [etichettaNomeVar=" + etichettaNomeVar
				+ ", idStatistica=" + idStatistica + ", listaCodiciVO="
				+ listaCodiciVO + ", nomeVar=" + nomeVar + ", obbligatorio="
				+ obbligatorio + ", tipoVar=" + tipoVar + ", valore=" + valore
				+ "]";
	}


}
