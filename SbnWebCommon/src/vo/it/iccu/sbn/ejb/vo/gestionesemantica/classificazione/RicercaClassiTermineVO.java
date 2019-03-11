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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

public class RicercaClassiTermineVO extends RicercaClassiVO {

	private static final long serialVersionUID = 6565883460640981630L;
	protected String codThesauro;
	protected String did;
	private String bid;
	private boolean escludiClassiLegateTitolo;

	private String idLista;
	private int numBlocco;

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public boolean isEscludiClassiLegateTitolo() {
		return escludiClassiLegateTitolo;
	}

	public void setEscludiClassiLegateTitolo(boolean escludiClassiLegateTitolo) {
		this.escludiClassiLegateTitolo = escludiClassiLegateTitolo;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int caricaBlocco) {
		this.numBlocco = caricaBlocco;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

}
