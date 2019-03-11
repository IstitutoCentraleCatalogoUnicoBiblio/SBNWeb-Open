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
import it.iccu.sbn.polo.orm.acquisizioni.Tra_messaggi;

import org.hibernate.proxy.HibernateProxyHelper;

public class Trp_messaggio_fascicolo extends Tb_base {

	private static final long serialVersionUID = 6511348160395268937L;
	private Tra_messaggi messaggio;
	private Tbp_fascicolo fascicolo;
	private Tba_ordini ordine;
	private int cd_msg;

	public Tbp_fascicolo getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(Tbp_fascicolo fascicolo) {
		this.fascicolo = fascicolo;
	}

	public Tba_ordini getOrdine() {
		return ordine;
	}

	public void setOrdine(Tba_ordini ordine) {
		this.ordine = ordine;
	}

	public int getCd_msg() {
		return cd_msg;
	}

	public void setCd_msg(int cd_msg) {
		this.cd_msg = cd_msg;
	}

	public Tra_messaggi getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(Tra_messaggi messaggio) {
		this.messaggio = messaggio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cd_msg;
		result = prime * result
				+ ((fascicolo == null) ? 0 : fascicolo.hashCode());
		result = prime * result
				+ ((messaggio == null) ? 0 : messaggio.hashCode());
		result = prime * result + ((ordine == null) ? 0 : ordine.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(HibernateProxyHelper.getClassWithoutInitializingProxy(obj) == Trp_messaggio_fascicolo.class))
			return false;
		Trp_messaggio_fascicolo other = (Trp_messaggio_fascicolo) obj;
		if (cd_msg != other.cd_msg)
			return false;
		if (fascicolo == null) {
			if (other.fascicolo != null)
				return false;
		} else if (!fascicolo.equals(other.fascicolo))
			return false;
		if (messaggio == null) {
			if (other.messaggio != null)
				return false;
		} else if (!messaggio.equals(other.messaggio))
			return false;
		if (ordine == null) {
			if (other.ordine != null)
				return false;
		} else if (!ordine.equals(other.ordine))
			return false;
		return true;
	}
}
