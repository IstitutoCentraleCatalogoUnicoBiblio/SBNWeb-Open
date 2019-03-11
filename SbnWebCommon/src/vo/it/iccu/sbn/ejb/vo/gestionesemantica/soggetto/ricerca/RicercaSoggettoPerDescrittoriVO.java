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

import it.iccu.sbn.ejb.vo.SerializableVO;

public class RicercaSoggettoPerDescrittoriVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 5257892600652451389L;
	private int countSogg;
	private int countDid;

	private String didAccettato;
	private String desDidAccettato;
	private String didRinvio;
	private String desDidRinvio;

	public String getDesDidAccettato() {
		return desDidAccettato;
	}

	public void setDesDidAccettato(String desDidAccettato) {
		this.desDidAccettato = desDidAccettato;
	}

	public String getDesDidRinvio() {
		return desDidRinvio;
	}

	public void setDesDidRinvio(String desDidRinvio) {
		this.desDidRinvio = desDidRinvio;
	}

	public String getDidAccettato() {
		return didAccettato;
	}

	public void setDidAccettato(String didAccettato) {
		this.didAccettato = didAccettato;
	}

	public String getDidRinvio() {
		return didRinvio;
	}

	public void setDidRinvio(String didRinvio) {
		this.didRinvio = didRinvio;
	}

	public RicercaSoggettoPerDescrittoriVO() {
		super();
	}

	public RicercaSoggettoPerDescrittoriVO(String formaAccettata,
			String formaRinvio, int countSogg, int countDid) {
		this.desDidAccettato = formaAccettata;
		this.countDid = countDid;
		this.countSogg = countSogg;
		this.desDidRinvio = formaRinvio;
	}

	public int getCountDid() {
		return countDid;
	}

	public void setCountDid(int countDid) {
		this.countDid = countDid;
	}

	public int getCountSogg() {
		return countSogg;
	}

	public void setCountSogg(int countSogg) {
		this.countSogg = countSogg;
	}

}
