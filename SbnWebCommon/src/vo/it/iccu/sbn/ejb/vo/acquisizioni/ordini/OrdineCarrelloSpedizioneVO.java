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
package it.iccu.sbn.ejb.vo.acquisizioni.ordini;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.Date;

public class OrdineCarrelloSpedizioneVO extends BaseVO {

	private static final long serialVersionUID = -6007735423703380194L;

	private int idOrdine;
	private String cartName;
	protected Date dataSpedizione;
	protected short prgSpedizione;

	public OrdineCarrelloSpedizioneVO() {
		super();
	}

	public OrdineCarrelloSpedizioneVO(OrdineCarrelloSpedizioneVO ocs) {
		super(ocs);
		this.idOrdine = ocs.idOrdine;
		this.cartName = ocs.cartName;
		this.dataSpedizione = ocs.dataSpedizione != null ? new Date(ocs.dataSpedizione.getTime()) : null;
		this.prgSpedizione = ocs.prgSpedizione;
	}

	public String getCartName() {
		return cartName;
	}

	public void setCartName(String cartName) {
		this.cartName = trimAndSet(cartName);
	}

	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	public short getPrgSpedizione() {
		return prgSpedizione;
	}

	public void setPrgSpedizione(short prgSpedizione) {
		this.prgSpedizione = prgSpedizione;
	}

	public int getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

}
