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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;

public class ComboCodDescVO extends SerializableVO {

	private static final long serialVersionUID = 6516612697231835531L;

	private String codice = "";
	private String descrizione = "";

	/**
	 * Comparator that can be used for a case insensitive sort of
	 * <code>LabelValueBean</code> objects.
	 */
	public static final Comparator<ComboCodDescVO> ORDINA_PER_CODICE = new Comparator<ComboCodDescVO>() {
		public int compare(ComboCodDescVO o1, ComboCodDescVO o2) {
			String myProgr1 = o1.getDescrizione();
			String myProgr2 = o2.getDescrizione();
			return myProgr1.compareTo(myProgr2);
		}
	};

	public ComboCodDescVO() {
		super();
	}

	public ComboCodDescVO(String codice, String descrizione) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
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

	public String getCodiceDescrizione() {
		return (this.codice.equals("") ? "" : this.codice + "_"
				+ this.descrizione);
	}

	public String getDescrizioneCodiceACQ() {
		String desc = "";
		if (this.descrizione.equals("") == false) {
			desc = this.codice + "\t" + this.descrizione;
		}
		return desc;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrizioneCodice() {

		return (this.codice.equals("") == false ? this.codice + "\t"
				+ this.descrizione : "");
	}

	public String getDescrizioneParentesiCod() {
		String cod = isFilled(codice) ? '(' + codice + ')' : "";
		String des = isFilled(descrizione) ? descrizione : "";
		return des + (isFilled(cod) ? " " + cod : "");
	}

	public String toString() {
		return getCodiceDescrizione();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result
				+ ((descrizione == null) ? 0 : descrizione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComboCodDescVO other = (ComboCodDescVO) obj;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		return true;
	}
}
