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
package it.iccu.sbn.ejb.vo.gestionestampe.etichette;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class FormattazioneVOCampoEtichetta extends SerializableVO {

	private static final long serialVersionUID = 22905965517618807L;

	private String nomeCampo;
	private boolean presente; // visualizza
	private boolean concatena; // concatena
	private float x; // x
	private float y; // y
	private boolean verticale; // verticale
	private String font; // font
	private String punti; // dimensione
	private boolean grassetto; // grassetto
	private boolean corsivo; // corsivo
	private String posizione; // posizione

	public boolean isConcatena() {
		return concatena;
	}

	public void setConcatena(boolean concatena) {
		this.concatena = concatena;
	}

	public boolean isCorsivo() {
		return corsivo;
	}

	public void setCorsivo(boolean corsivo) {
		this.corsivo = corsivo;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public boolean isGrassetto() {
		return grassetto;
	}

	public void setGrassetto(boolean grassetto) {
		this.grassetto = grassetto;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public String getPosizione() {
		return posizione;
	}

	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}

	public boolean isPresente() {
		return presente;
	}

	public void setPresente(boolean presente) {
		this.presente = presente;
	}

	public String getPunti() {
		return punti;
	}

	public void setPunti(String punti) {
		this.punti = punti;
	}

	public boolean isVerticale() {
		return verticale;
	}

	public void setVerticale(boolean verticale) {
		this.verticale = verticale;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
