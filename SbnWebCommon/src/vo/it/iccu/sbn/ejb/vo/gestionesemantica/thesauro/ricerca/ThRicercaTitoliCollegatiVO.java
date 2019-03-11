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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;

public class ThRicercaTitoliCollegatiVO extends SerializableVO {

	private static final long serialVersionUID = -4243735579902589765L;

	private String termineThes0;
	private String termineThes1;
	private String termineThes2;
	private String termineThes3;
	private String termineThes4;


	public int count() {
		int count = 0;
		count += isFilled(termineThes0) ? 1 : 0;
		count += isFilled(termineThes1) ? 1 : 0;
		count += isFilled(termineThes2) ? 1 : 0;
		count += isFilled(termineThes3) ? 1 : 0;
		count += isFilled(termineThes4) ? 1 : 0;

		return count;
	}


	public String getTermine() {
		if (count() > 1) return null;

		if (isFilled(termineThes0) ) return termineThes0;
		if (isFilled(termineThes1) ) return termineThes1;
		if (isFilled(termineThes2) ) return termineThes2;
		if (isFilled(termineThes3) ) return termineThes3;
		if (isFilled(termineThes4) ) return termineThes4;
		return null;
	}

	public String[] getTermini() {

		List<String> tmp = new ArrayList<String>();
		if (isFilled(termineThes0) ) tmp.add(termineThes0);
		if (isFilled(termineThes1) ) tmp.add(termineThes1);
		if (isFilled(termineThes2) ) tmp.add(termineThes2);
		if (isFilled(termineThes3) ) tmp.add(termineThes3);
		if (isFilled(termineThes4) ) tmp.add(termineThes4);

		int size = tmp.size();
		if (size > 0)
			return tmp.toArray(new String[size]);

		return null;
	}

	public String getTermineThes0() {
		return termineThes0;
	}

	public void setTermineThes0(String termineThes) {
		this.termineThes0 = termineThes;
	}

	public String getTermineThes1() {
		return termineThes1;
	}

	public void setTermineThes1(String termineThes1) {
		this.termineThes1 = termineThes1;
	}

	public String getTermineThes2() {
		return termineThes2;
	}

	public void setTermineThes2(String termineThes2) {
		this.termineThes2 = termineThes2;
	}

	public String getTermineThes3() {
		return termineThes3;
	}

	public void setTermineThes3(String termineThes3) {
		this.termineThes3 = termineThes3;
	}

	public String getTermineThes4() {
		return termineThes4;
	}

	public void setTermineThes4(String termineThes4) {
		this.termineThes4 = termineThes4;
	}

	@Override
	public boolean isEmpty() {
		return (count() == 0);
	}

	public void validate() throws ValidationException {
		if (count() < 1) {
			throw new ValidationException("Nessun parametro impostato",
					ValidationException.validazione);
		}
	}

}
