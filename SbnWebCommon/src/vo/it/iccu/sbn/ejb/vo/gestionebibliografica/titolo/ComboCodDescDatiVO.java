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

public class ComboCodDescDatiVO extends SerializableVO {

	private static final long serialVersionUID = 6516612697231835531L;

	private String codice = "";
	private String descrizione = "";
	private String datiUno = "";
	private String datiDue = "";
	private String datiTre = "";

	/**
	 * Comparator that can be used for a case insensitive sort of
	 * <code>LabelValueBean</code> objects.
	 */
	public static final Comparator<ComboCodDescDatiVO> ORDINA_PER_CODICE = new Comparator<ComboCodDescDatiVO>() {
		public int compare(ComboCodDescDatiVO o1, ComboCodDescDatiVO o2) {
			String myProgr1 = o1.getDescrizione();
			String myProgr2 = o2.getDescrizione();
			return myProgr1.compareTo(myProgr2);
		}
	};

	public ComboCodDescDatiVO() {
		super();
	}

	public ComboCodDescDatiVO(String codice, String descrizione, String datiUno, String datiDue) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
		this.datiUno = datiUno;
		this.datiDue = datiDue;
		this.datiTre = "";
	}

	public ComboCodDescDatiVO(String codice, String descrizione, String datiUno, String datiDue, String datiTre) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
		this.datiUno = datiUno;
		this.datiDue = datiDue;
		this.datiTre = datiTre;
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
		String desc="";
		if (this.descrizione.equals("") == false)
		{
			desc=this.codice + "\t"	+ this.descrizione ;
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
		ComboCodDescDatiVO other = (ComboCodDescDatiVO) obj;
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

	public String getDatiUno() {
		return datiUno;
	}

	public void setDatiUno(String datiUno) {
		this.datiUno = datiUno;
	}

	public String getDatiDue() {
		return datiDue;
	}

	public void setDatiDue(String datiDue) {
		this.datiDue = datiDue;
	}

	public String getDatiTre() {
		return datiTre;
	}

	public void setDatiTre(String datiTre) {
		this.datiTre = datiTre;
	}


}
