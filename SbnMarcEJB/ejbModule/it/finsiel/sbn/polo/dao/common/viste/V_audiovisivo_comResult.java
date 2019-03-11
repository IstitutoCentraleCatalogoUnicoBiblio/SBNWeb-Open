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

import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.V_audiovisivo_com;

public class V_audiovisivo_comResult extends V_audiovisivoResult {

	private V_audiovisivo_com v_audiovisivo_com;

	public V_audiovisivo_comResult(V_audiovisivo_com aud) {
		super();
		this.v_audiovisivo_com = aud;
		valorizzaParametro(aud.leggiAllParametro());
	}

	public V_audiovisivo_com getV_audiovisivo_com() {
		return v_audiovisivo_com;
	}

	public void setV_audiovisivo_com(V_audiovisivo_com aud) {
		v_audiovisivo_com = aud;
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return V_audiovisivo_com.class;
	}

}
