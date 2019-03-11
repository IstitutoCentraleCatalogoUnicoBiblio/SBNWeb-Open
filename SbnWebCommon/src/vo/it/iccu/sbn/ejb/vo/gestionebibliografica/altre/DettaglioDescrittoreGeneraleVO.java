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

package it.iccu.sbn.ejb.vo.gestionebibliografica.altre;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class DettaglioDescrittoreGeneraleVO extends SerializableVO {

	// = DettaglioDescrittoreGeneraleVO.class.hashCode();

// Dati relativi alla ricerca marca - Canali principali

	/**
	 *
	 */
	private static final long serialVersionUID = -2575388974623933185L;
	private String livAut;
	private String did;
	private String campoSoggettario;
	private String campoDescrittore;
	private String campoNotaDescrittore;
	private String TipoLegame;

	private String dataAgg;
	private String dataIns;

	public String getCampoDescrittore() {
		return campoDescrittore;
	}
	public void setCampoDescrittore(String campoDescrittore) {
		this.campoDescrittore = campoDescrittore;
	}
	public String getCampoNotaDescrittore() {
		return campoNotaDescrittore;
	}
	public void setCampoNotaDescrittore(String campoNotaDescrittore) {
		this.campoNotaDescrittore = campoNotaDescrittore;
	}
	public String getCampoSoggettario() {
		return campoSoggettario;
	}
	public void setCampoSoggettario(String campoSoggettario) {
		this.campoSoggettario = campoSoggettario;
	}
	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getLivAut() {
		return livAut;
	}
	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}
	public String getTipoLegame() {
		return TipoLegame;
	}
	public void setTipoLegame(String tipoLegame) {
		TipoLegame = tipoLegame;
	}

}
