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
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;

import java.util.Date;

public class Tbp_esemplare_fascicolo extends Tb_base {

	private static final long serialVersionUID = 8633419422751588487L;

	private int id_ese_fascicolo;

	private Tbp_fascicolo fascicolo;
	private Tbc_inventario inventario;
	private Tba_ordini ordine;
	private String cd_bib_abb;
	private Date data_arrivo;
	private Integer grp_fasc;
	private String note;
	private Character cd_no_disp;

	public Tbp_fascicolo getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(Tbp_fascicolo fascicolo) {
		this.fascicolo = fascicolo;
	}

	public Tbc_inventario getInventario() {
		return inventario;
	}

	public void setInventario(Tbc_inventario inventario) {
		this.inventario = inventario;
	}

	public Tba_ordini getOrdine() {
		return ordine;
	}

	public void setOrdine(Tba_ordini ordine) {
		this.ordine = ordine;
	}

	public String getCd_bib_abb() {
		return cd_bib_abb;
	}

	public void setCd_bib_abb(String cd_bib_abb) {
		this.cd_bib_abb = cd_bib_abb;
	}

	public Date getData_arrivo() {
		return data_arrivo;
	}

	public void setData_arrivo(Date data_arrivo) {
		this.data_arrivo = data_arrivo;
	}

	public Integer getGrp_fasc() {
		return grp_fasc;
	}

	public void setGrp_fasc(Integer grp_fasc) {
		this.grp_fasc = grp_fasc;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getId_ese_fascicolo() {
		return id_ese_fascicolo;
	}

	public void setId_ese_fascicolo(int id_ese_fascicolo) {
		this.id_ese_fascicolo = id_ese_fascicolo;
	}

	@Override
	public String toString() {
		return (fascicolo != null ? fascicolo + " " : "") + id_ese_fascicolo;
	}

	public Character getCd_no_disp() {
		return cd_no_disp;
	}

	public void setCd_no_disp(Character cd_no_disp) {
		this.cd_no_disp = cd_no_disp;
	}

}
