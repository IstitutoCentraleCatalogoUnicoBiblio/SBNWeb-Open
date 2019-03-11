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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.V_audiovisivo;

import java.util.HashMap;

public class V_audiovisivoResult extends Tb_titoloResult {

	private V_audiovisivo v_audiovisivo;

	public V_audiovisivoResult(V_audiovisivo aud) {
		this.v_audiovisivo = aud;
		valorizzaParametro(aud.leggiAllParametro());
	}

	public V_audiovisivoResult() {
		super();
	}

	public V_audiovisivo getV_audiovisivo() {
		return v_audiovisivo;
	}

	public void setV_audiovisivo(V_audiovisivo vAudiovisivo) {
		v_audiovisivo = vAudiovisivo;
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return V_audiovisivo.class;
	}

	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		return super.countPerNomeLike(opzioni);
	}

}
