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
package it.iccu.sbn.web.actionforms.servizi.serviziweb;

import java.util.List;

import org.apache.struts.action.ActionForm;
// almaviva
public class ListaBiblioForm extends ActionForm {

	private static final long serialVersionUID = -3207708348755303248L;
	//
	private List biblio;
    private String cod_biblio;
    private String nome_biblio;
    private int riga;
	//
	public void setBiblio(List biblio) {
		this.biblio = biblio;
	}

	public List getBiblio() {
		return biblio;
	}
	//
	public void setCod_biblio(String cod_biblio) {
		this.cod_biblio = cod_biblio;
	}

	public String getCod_biblio() {
		return cod_biblio;
	}
	//
	public void setNome_biblio(String nome_biblio) {
		this.nome_biblio = nome_biblio;
	}

	public String getNome_biblio() {
		return nome_biblio;
	}
	//

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public int getRiga() {
		return riga;
	}
}
