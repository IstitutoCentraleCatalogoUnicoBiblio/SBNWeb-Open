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

public class RicercaSoggettoDescrittoriVO extends RicercaSoggettoVO {

	private static final long serialVersionUID = -3867165845115381733L;

	private String descrittoriSogg0 = "";
	private String descrittoriSogg1 = "";
	private String descrittoriSogg2 = "";
	private String descrittoriSogg3 = "";

	private String did = "";
	private String did1 = "";
	private String did2 = "";
	private String did3 = "";
	private String did4 = "";

	public RicercaSoggettoDescrittoriVO() {
		super();
	}

	public RicercaSoggettoDescrittoriVO(String testoSogg, String cid,
			String descrittoriSogg, String descrittoriSogg1,
			String descrittoriSogg2, String descrittoriSogg3,
			boolean utilizzati) {
		super(testoSogg, cid);
		this.descrittoriSogg0 = descrittoriSogg;
		this.descrittoriSogg1 = descrittoriSogg1;
		this.descrittoriSogg2 = descrittoriSogg2;
		this.descrittoriSogg3 = descrittoriSogg3;
		this.utilizzati = utilizzati;
	}

	public String getDescrittoriSogg() {
		return descrittoriSogg0;
	}

	public void setDescrittoriSogg(String descrittoriSogg) {
		this.descrittoriSogg0 = descrittoriSogg;
	}

	public String getDescrittoriSogg1() {
		return descrittoriSogg1;
	}

	public void setDescrittoriSogg1(String descrittoriSogg1) {
		this.descrittoriSogg1 = descrittoriSogg1;
	}

	public String getDescrittoriSogg2() {
		return descrittoriSogg2;
	}

	public void setDescrittoriSogg2(String descrittoriSogg2) {
		this.descrittoriSogg2 = descrittoriSogg2;
	}

	public String getDescrittoriSogg3() {
		return descrittoriSogg3;
	}

	public void setDescrittoriSogg3(String descrittoriSogg3) {
		this.descrittoriSogg3 = descrittoriSogg3;
	}

	@Override
	public int count() {

		int count = 0;
		count += isFilled(descrittoriSogg0) ? 1 : 0;
		count += isFilled(descrittoriSogg1) ? 1 : 0;
		count += isFilled(descrittoriSogg2) ? 1 : 0;
		count += isFilled(descrittoriSogg3) ? 1 : 0;

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

	@Override
	public void validate() throws ValidationException {

		if (isFilled(did) ) {
			if (testTestoCid() || testCid() || testDescrittoreSogg() || testDid())
				throw new ValidationException("Validazione non corretta",
					ValidationException.errore);

		} else if (testTestoCid()) {
			if (testCid() || testDescrittoreSogg() || testDid())
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);

		} else if (testCid()) {
			if (testDescrittoreSogg() || testDid())
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);

		} else if (!testDescrittoreSogg() && !testDid()) {
			throw new ValidationException("Validazione non corretta",
					ValidationException.validazione);
		}

	}

	@Override
	public boolean isEmpty() {
		return (count() == 0 && didCount() == 0 && isNull(did) && super.isEmpty() );
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getUnDescrittore() {
		if (count() != 1)
			return null;

		if (isFilled(descrittoriSogg0) )
			return descrittoriSogg0;
		if (isFilled(descrittoriSogg1) )
			return descrittoriSogg1;
		if (isFilled(descrittoriSogg2) )
			return descrittoriSogg2;
		if (isFilled(descrittoriSogg3) )
			return descrittoriSogg3;

		return null;
	}

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

	private boolean testDescrittoreSogg() {
		return (count() > 0 );
	}

	private boolean testCid() {
		return (isFilled(this.getCid() ));
	}

	private boolean testTestoCid() {
		return (isFilled(this.getTestoSogg() ));
	}

	private boolean testDid() {
		return (didCount() > 0);
	}
}
