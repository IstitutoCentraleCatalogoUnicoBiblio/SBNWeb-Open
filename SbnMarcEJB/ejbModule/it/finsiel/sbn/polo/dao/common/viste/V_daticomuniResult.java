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
package it.finsiel.sbn.polo.dao.common.viste;

import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.V_daticomuni;

public class V_daticomuniResult extends Tb_titoloResult {

	private V_daticomuni v_daticomuni;

	public V_daticomuniResult() {
		super();
	}

	public V_daticomuniResult(V_daticomuni com) {
		this.v_daticomuni = com;
		valorizzaParametro(com.leggiAllParametro());
	}

	public V_daticomuni getV_daticomuni() {
		return v_daticomuni;
	}

	public void setV_daticomuni(V_daticomuni vDaticomuni) {
		v_daticomuni = vDaticomuni;
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return V_daticomuni.class;
	}

}
