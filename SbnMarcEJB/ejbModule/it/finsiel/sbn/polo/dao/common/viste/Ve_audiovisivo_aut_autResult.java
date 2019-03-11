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

import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;
import it.finsiel.sbn.polo.orm.viste.Ve_audiovisivo_aut_aut;

public class Ve_audiovisivo_aut_autResult extends Tb_titoloCommonDao {

	private Ve_audiovisivo_aut_aut ve_audiovisivo_aut_aut;

	public Ve_audiovisivo_aut_autResult(Ve_audiovisivo_aut_aut aud) {
		this.ve_audiovisivo_aut_aut = aud;
		valorizzaParametro(aud.leggiAllParametro());
	}

	public Ve_audiovisivo_aut_aut getVe_audiovisivo_aut_aut() {
		return ve_audiovisivo_aut_aut;
	}

	public void setVe_audiovisivo_aut_aut(Ve_audiovisivo_aut_aut ve_audiovisivo_aut_aut) {
		this.ve_audiovisivo_aut_aut = ve_audiovisivo_aut_aut;
	}

}
