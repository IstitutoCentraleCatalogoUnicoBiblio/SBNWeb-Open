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
package it.iccu.sbn.polo.orm.bibliografica;

import it.iccu.sbn.polo.orm.Tb_base;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

public class Tb_loc_indice extends Tb_base {

	private static final long serialVersionUID = -5809816664873339741L;

	private int id_loc;

	private Tb_titolo titolo;
	private Tbf_biblioteca_in_polo biblioteca;
	private char fl_stato;
	private short tp_loc;
	private String sbnmarc_xml;

	public int getId_loc() {
		return id_loc;
	}

	public void setId_loc(int id_loc) {
		this.id_loc = id_loc;
	}

	public Tb_titolo getTitolo() {
		return titolo;
	}

	public void setTitolo(Tb_titolo titolo) {
		this.titolo = titolo;
	}

	public char getFl_stato() {
		return fl_stato;
	}

	public void setFl_stato(char fl_stato) {
		this.fl_stato = fl_stato;
	}

	public String getSbnmarc_xml() {
		return sbnmarc_xml;
	}

	public void setSbnmarc_xml(String sbnmarc_xml) {
		this.sbnmarc_xml = sbnmarc_xml;
	}

	public short getTp_loc() {
		return tp_loc;
	}

	public void setTp_loc(short tp_loc) {
		this.tp_loc = tp_loc;
	}

	public Tbf_biblioteca_in_polo getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(Tbf_biblioteca_in_polo biblioteca) {
		this.biblioteca = biblioteca;
	}

}
