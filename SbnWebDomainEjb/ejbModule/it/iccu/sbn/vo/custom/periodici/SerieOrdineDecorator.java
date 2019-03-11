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
package it.iccu.sbn.vo.custom.periodici;

import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;

public class SerieOrdineDecorator extends SerieOrdineVO {

	private static final long serialVersionUID = -4223992194660442546L;

	private final OrdiniVO ordine;

	public SerieOrdineDecorator(SerieOrdineVO serie, OrdiniVO ordine) {
		this.ordine = ordine;
		copyCommonProperties(this, serie);
	}

	public OrdiniVO getOrdine() {
		return ordine;
	}

	//almaviva5_20110405 #4425
	public boolean isBilancioPresente() {
		return (ordine != null
					&& ordine.getBilancio() != null
					&& isFilled(ordine.getBilancio().getCodice1()));
	}

}
