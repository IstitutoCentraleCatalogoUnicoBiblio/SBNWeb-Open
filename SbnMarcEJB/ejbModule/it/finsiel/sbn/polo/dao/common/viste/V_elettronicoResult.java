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
import it.finsiel.sbn.polo.orm.viste.V_elettronico;

//almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
//Gestione nuovi campi specifici per etichetta 135 (filtri per la specificità in interrogazione; campi e liste nelle mappe di visualizzazione, variazione e inserimento)
public class V_elettronicoResult extends Tb_titoloResult {

	private V_elettronico v_elettronico;

	public V_elettronicoResult(V_elettronico ele) {
		this.v_elettronico = ele;
		valorizzaParametro(ele.leggiAllParametro());
	}

	public V_elettronico getV_elettronico() {
		return v_elettronico;
	}

	public void setV_elettronico(V_elettronico vElettronico) {
		v_elettronico = vElettronico;
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return V_elettronico.class;
	}

//	public Integer countPerNomeLike(HashMap opzioni)
//			throws InfrastructureException {
//		return super.countPerNomeLike(opzioni);
//	}



}
