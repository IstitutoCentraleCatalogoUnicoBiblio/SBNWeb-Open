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

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;


public class DatiLegameDescrittoreVO extends SBNMarcCommonVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 6294226372764557164L;
	public static final String TIPO_LEGAME_BT  = "BT";
	public static final String TIPO_LEGAME_NT  = "NT";
	public static final String TIPO_LEGAME_UF  = "UF";
	public static final String TIPO_LEGAME_RT  = "RT";
	public static final String TIPO_LEGAME_USE = "USE";

	private String didPartenza;
	private String didPartenzaLivelloAut;
	private String didPartenzaFormaNome;
	private String didArrivoFormaNome;
	private String didArrivo;
	private String notaLegame;
	private String tipoLegame;

	private String codiceSoggettario;

	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}

	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}

	/**
	 * @return Returns the didArrivo.
	 */
	public String getDidArrivo() {
		return didArrivo;
	}

	/**
	 * @param didArrivo The didArrivo to set.
	 */
	public void setDidArrivo(String didArrivo) {
		this.didArrivo = didArrivo;
	}

	/**
	 * @return Returns the didPartenza.
	 */
	public String getDidPartenza() {
		return didPartenza;
	}

	/**
	 * @param didPartenza The didPartenza to set.
	 */
	public void setDidPartenza(String didPartenza) {
		this.didPartenza = didPartenza;
	}

	/**
	 * @return Returns the didPartenzaFormaNome.
	 */
	public String getDidPartenzaFormaNome() {
		return didPartenzaFormaNome;
	}

	/**
	 * @param didPartenzaFormaNome The didPartenzaFormaNome to set.
	 */
	public void setDidPartenzaFormaNome(String didPartenzaFormaNome) {
		this.didPartenzaFormaNome = didPartenzaFormaNome;
	}

	/**
	 * @return Returns the didPartenzaLivelloAut.
	 */
	public String getDidPartenzaLivelloAut() {
		return didPartenzaLivelloAut;
	}

	/**
	 * @param didPartenzaLivelloAut The didPartenzaLivelloAut to set.
	 */
	public void setDidPartenzaLivelloAut(String didPartenzaLivelloAut) {
		this.didPartenzaLivelloAut = didPartenzaLivelloAut;
	}

	/**
	 * @return Returns the notaLegame.
	 */
	public String getNotaLegame() {
		return notaLegame;
	}

	/**
	 * @param notaLegame The notaLegame to set.
	 */
	public void setNotaLegame(String notaLegame) {
		this.notaLegame = notaLegame;
	}

	/**
	 * @return Returns the tipoLegame.
	 */
	public String getTipoLegame() {
		return tipoLegame;
	}

	/**
	 * @param tipoLegame The tipoLegame to set.
	 */
	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getDidArrivoFormaNome() {
		return didArrivoFormaNome;
	}

	public void setDidArrivoFormaNome(String didArrivoFormaNome) {
		this.didArrivoFormaNome = didArrivoFormaNome;
	}



}
