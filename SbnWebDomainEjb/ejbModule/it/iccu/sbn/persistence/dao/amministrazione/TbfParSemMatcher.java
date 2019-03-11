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
package it.iccu.sbn.persistence.dao.amministrazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class TbfParSemMatcher extends BaseMatcher<Tbf_par_sem> {

	private final Tbf_par_sem check;

	public TbfParSemMatcher(Tbf_par_sem check) {
		this.check = check;
	}

	public boolean matches(Object item) {
		if (!(item instanceof Tbf_par_sem) )
			return false;

		Tbf_par_sem p = (Tbf_par_sem) item;
		boolean eq = ValidazioneDati.equals(p.getTp_tabella_codici().trim(), check.getTp_tabella_codici().trim());
		eq = eq && ValidazioneDati.equals(p.getCd_tabella_codici().trim(), check.getCd_tabella_codici().trim());


		//log.debug(check + " <--> " + p + " == " + eq);

		return eq;
	}

	public void describeTo(Description description) {
		return;
	}

}
