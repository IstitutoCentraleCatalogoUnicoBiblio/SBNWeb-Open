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

public class FormattazioneVOImmagini extends SerializableVO {

	private static final long serialVersionUID = -8880363963178197738L;

	private String nomeImmagine;
	private boolean presente;
	private boolean verticale;
	private String fieldName;
	private float dimensioneVerticale;
	private float dimensioneOrizzontale;
	private float x;
	private float y;

	public float getDimensioneOrizzontale() {
		return dimensioneOrizzontale;
	}

	public void setDimensioneOrizzontale(float dimensioneOrizzontale) {
		this.dimensioneOrizzontale = dimensioneOrizzontale;
	}

	public float getDimensioneVerticale() {
		return dimensioneVerticale;
	}

	public void setDimensioneVerticale(float dimensioneVerticale) {
		this.dimensioneVerticale = dimensioneVerticale;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isPresente() {
		return presente;
	}

	public void setPresente(boolean presente) {
		this.presente = presente;
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

	public String getNomeImmagine() {
		return nomeImmagine;
	}

	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}
}
