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
package it.iccu.sbn.polo.orm.acquisizioni;

import it.iccu.sbn.polo.orm.Tb_base;

import java.util.Date;

public class Tra_ordine_carrello_spedizione extends Tb_base {

	private static final long serialVersionUID = -8430945555923721671L;

	private int idOrdine;
	private Tba_ordini ordine;
	private Date dt_spedizione;
	private short prg_spedizione;
	private String cart_name;

	public Tba_ordini getOrdine() {
		return ordine;
	}

	public void setOrdine(Tba_ordini ordine) {
		this.ordine = ordine;
	}

	public Date getDt_spedizione() {
		return dt_spedizione;
	}

	public void setDt_spedizione(Date dt_spedizione) {
		this.dt_spedizione = dt_spedizione;
	}

	public short getPrg_spedizione() {
		return prg_spedizione;
	}

	public void setPrg_spedizione(short prg_spedizione) {
		this.prg_spedizione = prg_spedizione;
	}

	public String getCart_name() {
		return cart_name;
	}

	public void setCart_name(String cart_name) {
		this.cart_name = cart_name;
	}

	public int getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

}
