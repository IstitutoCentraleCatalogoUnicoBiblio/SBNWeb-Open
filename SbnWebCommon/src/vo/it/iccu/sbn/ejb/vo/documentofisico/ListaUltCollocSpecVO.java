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
package it.iccu.sbn.ejb.vo.documentofisico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaUltCollocSpecVO  implements Serializable{


	private static final long serialVersionUID = 8671146192444300355L;
	private String 	codBib;
	private String 	descrBib;
	private String 	sezione;
	private String 	collocazione;
	private String 	specificazione;
	private List colloc = new ArrayList();
	private List specif = new ArrayList();

	public List getColloc() {
		return colloc;
	}

	public void setColloc(List colloc) {
		this.colloc = colloc;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSpecificazione() {
		return specificazione;
	}

	public void setSpecificazione(String specificazione) {
		this.specificazione = specificazione;
	}

	public List getSpecif() {
		return specif;
	}

	public void setSpecif(List specif) {
		this.specif = specif;
	}

}
