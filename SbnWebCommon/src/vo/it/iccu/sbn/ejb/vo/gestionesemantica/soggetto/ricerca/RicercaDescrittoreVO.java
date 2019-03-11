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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class RicercaDescrittoreVO extends SerializableVO {

	private static final long serialVersionUID = 1512209516396406425L;
	private String testoDescr = "";
	private String did = "";
	private String parole = "";
	private String parole1 = "";
	private String parole2 = "";
	private String parole3 = "";
	private String parole4 = "";
	private String parole5 = "";
	private String did1 = "";
	private String did2 = "";
	private String did3 = "";
	private String did4 = "";

	//almaviva5_20101221 #4068
	private boolean cercaDidCollegati = false;


	public String getDid1() {
		return did1;
	}

	public void setDid1(String did1) {
		this.did1 = did1;
	}

	public String getDid2() {
		return did2;
	}

	public void setDid2(String did2) {
		this.did2 = did2;
	}

	public String getDid3() {
		return did3;
	}

	public void setDid3(String did3) {
		this.did3 = did3;
	}

	public String getDid4() {
		return did4;
	}

	public void setDid4(String did4) {
		this.did4 = did4;
	}

	public RicercaDescrittoreVO() {
	}

	public RicercaDescrittoreVO(String testoDescr, String did, String parole,
			String parole1, String parole2, String parole3, String parole4,
			String parole5) {
		this.testoDescr = testoDescr;
		this.did = did.toUpperCase();
		this.parole = parole;
		this.parole1 = parole1;
		this.parole2 = parole2;
		this.parole3 = parole3;
		this.parole4 = parole4;
		this.parole5 = parole5;
	}

	public int countParole() {
		int count = 0;
		count += isFilled(parole) ? 1 : 0;
		count += isFilled(parole1) ? 1 : 0;
		count += isFilled(parole2) ? 1 : 0;
		count += isFilled(parole3) ? 1 : 0;
		count += isFilled(parole4) ? 1 : 0;
		count += isFilled(parole5) ? 1 : 0;

		return count;
	}

	public int didCount() {
		int count = 0;
		count += isFilled(did1) ? 1 : 0;
		count += isFilled(did2) ? 1 : 0;
		count += isFilled(did3) ? 1 : 0;
		count += isFilled(did4) ? 1 : 0;

		return count;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did.toUpperCase();
	}

	public String getParole() {
		return parole;
	}

	public void setParole(String parole) {
		this.parole = parole;
	}

	public String getParole1() {
		return parole1;
	}

	public void setParole1(String parole1) {
		this.parole1 = parole1;
	}

	public String getParole2() {
		return parole2;
	}

	public void setParole2(String parole2) {
		this.parole2 = parole2;
	}

	public String getParole3() {
		return parole3;
	}

	public void setParole3(String parole3) {
		this.parole3 = parole3;
	}

	public String getParole4() {
		return parole4;
	}

	public void setParole4(String parole4) {
		this.parole4 = parole4;
	}

	public String getParole5() {
		return parole5;
	}

	public void setParole5(String parole5) {
		this.parole5 = parole5;
	}

	public String getTestoDescr() {
		return testoDescr;
	}

	public void setTestoDescr(String testoDescr) {
		this.testoDescr = testoDescr;
	}

	public void validate() throws ValidationException {

		if (testTestoDid()) {
			if (testDidDescr() || testParoleDescr() || testDid())
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);

		} else if (testDidDescr()) {
			if (testParoleDescr() || testDid())
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);

		} else if (!testParoleDescr() && !testDid()) {
			throw new ValidationException("Validazione non corretta",
					ValidationException.validazione);
		}

	}


	@Override
	public boolean isEmpty() {
		return (!testParoleDescr() && !testDid() && isNull(did) && isNull(testoDescr) );
	}

	private boolean testParoleDescr() {
		return (countParole() > 0 );
	}

	private boolean testDidDescr() {
		return isFilled(did);
	}

	private boolean testTestoDid() {
		return isFilled(testoDescr);
	}

	private boolean testDid() {
		return (didCount() > 0);
	}

	public boolean isCercaDidCollegati() {
		return cercaDidCollegati;
	}

	public void setCercaDidCollegati(boolean cercaDidCollegati) {
		this.cercaDidCollegati = cercaDidCollegati;
	}

}
