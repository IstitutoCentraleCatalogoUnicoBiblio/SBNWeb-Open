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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.constant.ConstantDefault;

public abstract class RicercaBaseVO<T> extends SerializableVO {

	private static final long serialVersionUID = -5120369985225806425L;

	private String codPolo;
	private String codBib;
	private String ticket;

	private String ordinamento;
	private SerializableComparator<T> comparator;
	private int numeroElementiBlocco = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public int getNumeroElementiBlocco() {
		return numeroElementiBlocco;
	}

	public void setNumeroElementiBlocco(int numeroElementiBlocco) {
		this.numeroElementiBlocco = numeroElementiBlocco;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = trimAndSet(ordinamento);
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public SerializableComparator<T> getComparator() {
		return comparator;
	}

	public void setComparator(SerializableComparator<T> comparator) {
		this.comparator = comparator;
	}

}
