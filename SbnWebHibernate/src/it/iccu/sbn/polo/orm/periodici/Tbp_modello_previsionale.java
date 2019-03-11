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
package it.iccu.sbn.polo.orm.periodici;

import it.iccu.sbn.polo.orm.Tb_base;

public class Tbp_modello_previsionale extends Tb_base {


	private static final long serialVersionUID = 5794676595033431468L;
	private int id_modello;
	private String nome;
	private String descrizione;
	private String xml_model;

	public int getId_modello() {
		return id_modello;
	}

	public void setId_modello(int id_modello) {
		this.id_modello = id_modello;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getXml_model() {
		return xml_model;
	}

	public void setXml_model(String xml_model) {
		this.xml_model = xml_model;
	}

}
