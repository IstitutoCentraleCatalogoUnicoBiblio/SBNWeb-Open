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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class CodiceVO extends SerializableVO implements Comparable<CodiceVO>{

	private static final long serialVersionUID = -8742217029346476019L;
	private String codice;
	private String descrizione;
	private String terzo;
	private String quarto;
	private int numero;
	private Character carattere;

	public Character getCarattere() {
		return carattere;
	}

	public void setCarattere(Character carattere) {
		this.carattere = carattere;
	}

	public CodiceVO() {
	}

	/*
	 * public CodiceVO(Object req, Object req1){ }
	 */
	public CodiceVO(String cod) {
		this.codice = cod;
	}

	public CodiceVO(String cod, String des) {
		this.codice = cod;
		this.descrizione = des;
	}

	public CodiceVO(Character cod, String des) {
		this.carattere = cod;
		this.descrizione = des;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodiceDescrizione() {
		return this.codice + " " + this.descrizione;
	}

	public String getDescrizioneCodice() {
		return this.codice + " - " + this.descrizione;
	}

	public String getDescrizioneCodice1() {
		if(codice.equals("")){
			return this.codice + " " + this.descrizione;
		}else{
				return this.codice + " - " + this.descrizione;
			}
		}

	public String getTerzo() {
		return terzo;
	}

	public void setTerzo(String terzo) {
		this.terzo = terzo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getQuarto() {
		return quarto;
	}

	public void setQuarto(String quarto) {
		this.quarto = quarto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carattere == null) ? 0 : carattere.hashCode());
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result
				+ ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + numero;
		result = prime * result + ((quarto == null) ? 0 : quarto.hashCode());
		result = prime * result + ((terzo == null) ? 0 : terzo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CodiceVO other = (CodiceVO) obj;
		if (carattere == null) {
			if (other.carattere != null) {
				return false;
			}
		} else if (!carattere.equals(other.carattere)) {
			return false;
		}
		if (codice == null) {
			if (other.codice != null) {
				return false;
			}
		} else if (!codice.equals(other.codice)) {
			return false;
		}
		if (descrizione == null) {
			if (other.descrizione != null) {
				return false;
			}
		} else if (!descrizione.equals(other.descrizione)) {
			return false;
		}
		if (numero != other.numero) {
			return false;
		}
		if (quarto == null) {
			if (other.quarto != null) {
				return false;
			}
		} else if (!quarto.equals(other.quarto)) {
			return false;
		}
		if (terzo == null) {
			if (other.terzo != null) {
				return false;
			}
		} else if (!terzo.equals(other.terzo)) {
			return false;
		}
		return true;
	}

	public int compareTo(CodiceVO o) {
		int ord = codice.compareTo(o.codice);
		if (ord != 0) {
			return ord;
		} else {
			int inv = Integer.parseInt(this.descrizione);
			int invO = Integer.parseInt(o.descrizione);
			return inv - invO;
		}
	}
}
