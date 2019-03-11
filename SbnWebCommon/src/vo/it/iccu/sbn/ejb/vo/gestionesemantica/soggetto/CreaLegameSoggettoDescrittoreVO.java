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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.BaseVO;


public class CreaLegameSoggettoDescrittoreVO extends BaseVO {

	private static final long serialVersionUID = -5863651706978795685L;
	private String codSoggettario;
	private String edizioneSoggettario;
	private String testoDescrittore;
	private String notaLegame;
	private String livelloAutorita;
	private String cid;
	private String categoriaSoggetto;
	private boolean condiviso;
	private String T005;
	private String did;
	private boolean polo;
	private boolean indice;

	public boolean isIndice() {
		return indice;
	}

	public void setIndice(boolean indice) {
		this.indice = indice;
	}

	public boolean isPolo() {
		return polo;
	}

	public void setPolo(boolean polo) {
		this.polo = polo;
	}

	public CreaLegameSoggettoDescrittoreVO() {
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getNotaLegame() {
		return notaLegame;
	}

	public void setNotaLegame(String notaLegame) {
		this.notaLegame = notaLegame;
	}

	public String getTestoDescrittore() {
		return testoDescrittore;
	}

	public void setTestoDescrittore(String testoDescrittore) {
		this.testoDescrittore = testoDescrittore;
	}

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}

	public String getCategoriaSoggetto() {
		return categoriaSoggetto;
	}

	public void setCategoriaSoggetto(String categoriaSoggetto) {
		this.categoriaSoggetto = categoriaSoggetto;
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

}
