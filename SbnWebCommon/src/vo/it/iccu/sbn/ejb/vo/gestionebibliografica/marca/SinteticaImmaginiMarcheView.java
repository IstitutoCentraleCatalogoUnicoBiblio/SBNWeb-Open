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

package it.iccu.sbn.ejb.vo.gestionebibliografica.marca;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class SinteticaImmaginiMarcheView extends SerializableVO {

	private static final long serialVersionUID = -1125111423542582389L;
	private String mid;
	private String nome;
	private String motto;
	private String citazione;
	private String[] keyImage;

	public SinteticaImmaginiMarcheView() {
		super();
	}

	public SinteticaImmaginiMarcheView(SinteticaMarcheView marca) {
		copyCommonProperties(this, marca);
	}

	public SinteticaImmaginiMarcheView(SinteticaMarcheView marca, String[] imgKeys) {
		this(marca);
		this.keyImage = imgKeys;
	}

	public String getCitazione() {
		return citazione;
	}

	public void setCitazione(String citazione) {
		this.citazione = citazione;
	}

	public String[] getKeyImage() {
		return keyImage;
	}

	public void setKeyImage(String[] keyImage) {
		this.keyImage = keyImage;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
