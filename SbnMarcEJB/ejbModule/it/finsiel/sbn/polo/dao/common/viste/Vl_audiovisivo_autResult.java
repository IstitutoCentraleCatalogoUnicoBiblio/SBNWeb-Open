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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_autResult;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_audiovisivo_aut;

public class Vl_audiovisivo_autResult extends Vl_titolo_autResult {

	private Vl_audiovisivo_aut vl_audiovisivo_aut;

	public Vl_audiovisivo_autResult(Vl_audiovisivo_aut aud) throws InfrastructureException {
		super(aud);
		this.vl_audiovisivo_aut = aud;
		valorizzaParametro(aud.leggiAllParametro());
	}

	public Vl_audiovisivo_aut getVl_audiovisivo_aut() {
		return vl_audiovisivo_aut;
	}

	public void setVl_audiovisivo_aut(Vl_audiovisivo_aut vl_audiovisivo_aut) {
		this.vl_audiovisivo_aut = vl_audiovisivo_aut;
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_audiovisivo_aut.class;
	}

}
