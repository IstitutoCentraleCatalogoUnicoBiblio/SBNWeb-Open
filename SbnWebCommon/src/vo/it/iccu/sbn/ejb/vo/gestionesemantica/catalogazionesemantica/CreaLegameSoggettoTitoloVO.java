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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.BaseVO;

public class CreaLegameSoggettoTitoloVO extends BaseVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -7996011097444015063L;
	private String notaLegame;
	private String cid;
	private String codSoggettario;
	private String livelloAutorita;
	private String testoSoggetto;
	private String notaSoggetto;

	public CreaLegameSoggettoTitoloVO() {
		super();
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

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	public String getNotaSoggetto() {
		return notaSoggetto;
	}

	public void setNotaSoggetto(String notaSoggetto) {
		this.notaSoggetto = notaSoggetto;
	}

	public String getTestoSoggetto() {
		return testoSoggetto;
	}

	public void setTestoSoggetto(String testoSoggetto) {
		this.testoSoggetto = testoSoggetto;
	}

}
