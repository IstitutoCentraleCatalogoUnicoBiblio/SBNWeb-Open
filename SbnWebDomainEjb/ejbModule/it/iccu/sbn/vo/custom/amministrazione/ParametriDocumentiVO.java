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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ParametriDocumentiVO extends SerializableVO {

	private static final long serialVersionUID = 2236125870571073243L;

	private String tipoMateriale;
	private int livelloAut;
	private String abilitaOggetto;
	private String abilitatoForzatura;
	private boolean sololocale;

	public ParametriDocumentiVO(String tipoMateriale, int livelloAut,
			String abilitaOggetto, String abilitatoForzatura, boolean sololocale) {
		this.tipoMateriale = tipoMateriale;
		this.livelloAut = livelloAut;
		this.abilitaOggetto = abilitaOggetto;
		this.abilitatoForzatura = abilitatoForzatura;
		//almaviva5_20140128 evolutive google3
		this.sololocale = sololocale;
	}

	public String getAbilitaOggetto() {
		return abilitaOggetto;
	}

	public void setAbilitaOggetto(String abilitaOggetto) {
		this.abilitaOggetto = abilitaOggetto;
	}

	public String getAbilitatoForzatura() {
		return abilitatoForzatura;
	}

	public void setAbilitatoForzatura(String abilitatoForzatura) {
		this.abilitatoForzatura = abilitatoForzatura;
	}

	public int getLivelloAut() {
		return livelloAut;
	}

	public void setLivelloAut(int livelloAut) {
		this.livelloAut = livelloAut;
	}

	public boolean isSololocale() {
		return sololocale;
	}

	public void setSololocale(boolean sololocale) {
		this.sololocale = sololocale;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

}
