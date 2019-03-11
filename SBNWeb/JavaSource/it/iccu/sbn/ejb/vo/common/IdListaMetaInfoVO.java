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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;

public class IdListaMetaInfoVO extends SerializableVO {

	private static final long serialVersionUID = -7453088690856012444L;
	private String idLista;
	private int totRighe;
	private int totBlocchi;
	private int maxRighe;
	private DescrittoreBloccoVO ultimoBlocco;
	private Serializable queryInfo;


	public IdListaMetaInfoVO(String idLista, int totRighe, int totBlocchi,
			int maxRighe, DescrittoreBloccoVO ultimoBlocco, Serializable queryInfo) {
		super();
		this.idLista = idLista;
		this.totRighe = totRighe;
		this.totBlocchi = totBlocchi;
		this.maxRighe = maxRighe;
		this.ultimoBlocco = ultimoBlocco;
		this.queryInfo = queryInfo;
	}

	public IdListaMetaInfoVO(DescrittoreBloccoVO blocco) {
		this.idLista    = blocco.getIdLista();
		this.totRighe   = blocco.getTotRighe();
		this.totBlocchi = blocco.getTotBlocchi();
		this.maxRighe   = blocco.getMaxRighe();
		this.ultimoBlocco = blocco;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public int getTotRighe() {
		return totRighe;
	}
	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}
	public DescrittoreBloccoVO getUltimoBlocco() {
		return ultimoBlocco;
	}
	public void setUltimoBlocco(DescrittoreBloccoVO ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}
	public String getIdLista() {
		return idLista;
	}
	public int getMaxRighe() {
		return maxRighe;
	}

	public Serializable getQueryInfo() {
		// TODO Auto-generated method stub
		return this.queryInfo;
	}

	public void setQueryInfo(Serializable queryInfo) {
		this.queryInfo = queryInfo;
	}

}
