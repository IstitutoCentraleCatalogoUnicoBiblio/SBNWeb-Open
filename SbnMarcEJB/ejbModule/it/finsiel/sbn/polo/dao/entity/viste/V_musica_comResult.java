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
package it.finsiel.sbn.polo.dao.entity.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.V_musica_com;

public class V_musica_comResult extends V_musicaResult {

    private V_musica_com v_musica_com;

	public V_musica_comResult(V_musica_com v_musica_com) throws InfrastructureException {
        super();
		this.v_musica_com = v_musica_com;
        this.valorizzaParametro(v_musica_com.leggiAllParametro());
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return V_musica_com.class;
	}

	public V_musica_com getV_musica_com() {
		return v_musica_com;
	}

	public void setV_musica_com(V_musica_com v_musica_com) {
		this.v_musica_com = v_musica_com;
	}


}
