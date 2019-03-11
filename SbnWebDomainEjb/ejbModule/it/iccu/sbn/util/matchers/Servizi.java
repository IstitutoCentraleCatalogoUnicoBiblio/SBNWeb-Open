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
package it.iccu.sbn.util.matchers;

import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import com.annimon.stream.function.Predicate;

public class Servizi {

	public static class ControlloAttivitaServizio extends BaseMatchers {

		public static final Predicate<it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio> codAttivitaEquals(
				final String codAttivita) {
			return new Predicate<it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio>() {
				public boolean test(it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio v) {
					return StatoIterRichiesta.of(v.getPassoIter().getCodAttivita()) == StatoIterRichiesta
							.of(codAttivita);
				}
			};
		}
	}

}
