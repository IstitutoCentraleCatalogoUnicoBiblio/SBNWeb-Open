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
package it.iccu.sbn.ejb.vo.gestionebibliografica.altre;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiPassaggioInterrogazioneLuogoVO  extends SerializableVO {

	// = AreaDatiPassaggioInterrogazioneLuogoVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -8677768656080418602L;
	private int oggChiamante;
	private int tipoOggetto;
	private String oggDiRicerca;
	private boolean ricercaPolo;
	private boolean ricercaIndice;

	// Campi per la ricerca dei titoli collegati ad oggetti
	private String naturaTitBase;
	private String tipMatTitBase;
	private String codiceLegame;
	private String codiceSici;

	InterrogazioneLuogoGeneraleVO interrGener = new InterrogazioneLuogoGeneraleVO();


	public InterrogazioneLuogoGeneraleVO getInterrGener() {
		return interrGener;
	}

	public void setInterrGener(InterrogazioneLuogoGeneraleVO interrGener) {
		this.interrGener = interrGener;
	}

	public int getOggChiamante() {
		return oggChiamante;
	}

	public void setOggChiamante(int oggChiamante) {
		this.oggChiamante = oggChiamante;
	}

	public String getOggDiRicerca() {
		return oggDiRicerca;
	}

	public void setOggDiRicerca(String oggDiRicerca) {
		this.oggDiRicerca = oggDiRicerca;
	}

	public boolean isRicercaIndice() {
		return ricercaIndice;
	}

	public void setRicercaIndice(boolean ricercaIndice) {
		this.ricercaIndice = ricercaIndice;
	}

	public boolean isRicercaPolo() {
		return ricercaPolo;
	}

	public void setRicercaPolo(boolean ricercaPolo) {
		this.ricercaPolo = ricercaPolo;
	}

	public int getTipoOggetto() {
		return tipoOggetto;
	}

	public void setTipoOggetto(int tipoOggetto) {
		this.tipoOggetto = tipoOggetto;
	}

	public String getCodiceLegame() {
		return codiceLegame;
	}

	public void setCodiceLegame(String codiceLegame) {
		this.codiceLegame = codiceLegame;
	}

	public String getCodiceSici() {
		return codiceSici;
	}

	public void setCodiceSici(String codiceSici) {
		this.codiceSici = codiceSici;
	}

	public String getNaturaTitBase() {
		return naturaTitBase;
	}

	public void setNaturaTitBase(String naturaTitBase) {
		this.naturaTitBase = naturaTitBase;
	}

	public String getTipMatTitBase() {
		return tipMatTitBase;
	}

	public void setTipMatTitBase(String tipMatTitBase) {
		this.tipMatTitBase = tipMatTitBase;
	}

}
