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



public class StampaMultiFontDinamicoForm extends StampaFontDinamicoBaseForm {



	private static final long serialVersionUID = 8547081572245162473L;
	protected String[] font;
	protected String[] punti;

	//string per multibox
	protected String[] grassetto;

	//string per multibox
	protected String[] corsivo;

	public StampaMultiFontDinamicoForm() throws Exception{
		super();
	}

	protected void inizializza() throws Exception{
		super.inizializza();
	}
	public String[] getCorsivo() {
		return corsivo;
	}

	public void setCorsivo(String[] corsivo) {
		this.corsivo = corsivo;
	}

	public String[] getFont() {
		return font;
	}

	public void setFont(String[] font) {
		this.font = font;
	}

	public String[] getGrassetto() {
		return grassetto;
	}

	public void setGrassetto(String[] grassetto) {
		this.grassetto = grassetto;
	}

	public String[] getPunti() {
		return punti;
	}

	public void setPunti(String[] punti) {
		this.punti = punti;
	}



}
