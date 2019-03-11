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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class AreePassaggioSifVO extends SerializableVO {

	// = AreePassaggioSifVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -6922765928316538607L;
	private int livelloRicerca;
	private int oggettoRicerca;
	private String oggettoDaRicercare;
	private String descOggettoDaRicercare;
	private String oggettoChiamante;
	private String codBiblioteca;
	private String naturaOggetto;
	private String tipMatOggetto;
	private boolean visualCall;

	private String consistenzaPolo;
	private String segnaturaPolo;
	private String uriPolo;

	private String codTipoDigitPolo;
	private String descTipoDigitPolo;



	public String getUriPolo() {
		return uriPolo;
	}

	public void setUriPolo(String uriPolo) {
		this.uriPolo = uriPolo;
	}

	public String getConsistenzaPolo() {
		return consistenzaPolo;
	}

	public void setConsistenzaPolo(String consistenzaPolo) {
		this.consistenzaPolo = consistenzaPolo;
	}

	public String getSegnaturaPolo() {
		return segnaturaPolo;
	}

	public void setSegnaturaPolo(String segnaturaPolo) {
		this.segnaturaPolo = segnaturaPolo;
	}

	public int getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(int livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public String getOggettoChiamante() {
		return oggettoChiamante;
	}

	public void setOggettoChiamante(String oggettoChiamante) {
		this.oggettoChiamante = oggettoChiamante;
	}

	public String getOggettoDaRicercare() {
		return oggettoDaRicercare;
	}

	public void setOggettoDaRicercare(String oggettoDaRicercare) {
		this.oggettoDaRicercare = oggettoDaRicercare;
	}

	public int getOggettoRicerca() {
		return oggettoRicerca;
	}

	public void setOggettoRicerca(int oggettoRicerca) {
		this.oggettoRicerca = oggettoRicerca;
	}

	public String getDescOggettoDaRicercare() {
		return descOggettoDaRicercare;
	}

	public void setDescOggettoDaRicercare(String descOggettoDaRicercare) {
		this.descOggettoDaRicercare = descOggettoDaRicercare;
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public boolean isVisualCall() {
		return visualCall;
	}

	public void setVisualCall(boolean visualCall) {
		this.visualCall = visualCall;
	}

	public String getNaturaOggetto() {
		return naturaOggetto;
	}

	public void setNaturaOggetto(String naturaOggetto) {
		this.naturaOggetto = naturaOggetto;
	}

	public String getTipMatOggetto() {
		return tipMatOggetto;
	}

	public void setTipMatOggetto(String tipMatOggetto) {
		this.tipMatOggetto = tipMatOggetto;
	}

	public String getCodTipoDigitPolo() {
		return codTipoDigitPolo;
	}

	public void setCodTipoDigitPolo(String codTipoDigitPolo) {
		this.codTipoDigitPolo = codTipoDigitPolo;
	}

	public String getDescTipoDigitPolo() {
		return descTipoDigitPolo;
	}

	public void setDescTipoDigitPolo(String descTipoDigitPolo) {
		this.descTipoDigitPolo = descTipoDigitPolo;
	}

}
