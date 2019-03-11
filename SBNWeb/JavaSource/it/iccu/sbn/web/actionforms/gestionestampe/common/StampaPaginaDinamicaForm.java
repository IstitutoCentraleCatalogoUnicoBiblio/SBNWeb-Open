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
package it.iccu.sbn.web.actionforms.gestionestampe.common;


public class StampaPaginaDinamicaForm extends StampaMultiFontDinamicoForm{



	private static final long serialVersionUID = 2326316382016871244L;
	protected String unitaDiMisura;
	protected float margineSup;
	protected float margineSin;
	protected float margineDes;
	protected float margineInf;
	protected float larghezzaPagina;
	protected float altezzaPagina;


	public StampaPaginaDinamicaForm() throws Exception {
		super();
		inizializza();
	}

	protected void inizializza() throws Exception{
		super.inizializza();
		altezzaPagina=29.7f;
		larghezzaPagina=21f;

		unitaDiMisura="cm";
		margineSup=0.9f;
		margineSin=0.8f;
		margineDes=1.3f;
		margineInf=1.6f;
	}



	public float getAltezzaPagina() {
		return altezzaPagina;
	}
	public void setAltezzaPagina(float altezzaPagina) {
		this.altezzaPagina = altezzaPagina;
	}
	public float getLarghezzaPagina() {
		return larghezzaPagina;
	}
	public void setLarghezzaPagina(float larghezzaPagina) {
		this.larghezzaPagina = larghezzaPagina;
	}
	public float getMargineDes() {
		return margineDes;
	}
	public void setMargineDes(float margineDes) {
		this.margineDes = margineDes;
	}
	public float getMargineInf() {
		return margineInf;
	}
	public void setMargineInf(float margineInf) {
		this.margineInf = margineInf;
	}
	public float getMargineSin() {
		return margineSin;
	}
	public void setMargineSin(float margineSin) {
		this.margineSin = margineSin;
	}
	public float getMargineSup() {
		return margineSup;
	}
	public void setMargineSup(float margineSup) {
		this.margineSup = margineSup;
	}
	public String getUnitaDiMisura() {
		return unitaDiMisura;
	}
	public void setUnitaDiMisura(String unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
	}



}
