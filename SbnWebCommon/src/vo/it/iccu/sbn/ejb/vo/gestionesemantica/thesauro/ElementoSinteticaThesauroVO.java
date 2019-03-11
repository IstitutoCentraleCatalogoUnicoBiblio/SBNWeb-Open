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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.Comparator;


public class ElementoSinteticaThesauroVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 5125802460585033553L;

	private int progr;
	private String codThesauro;
	private String did;
	private String termine;
	private String formaNome;
	private String livelloAutorita;
	private int numTitoliPolo;
	private int numTitoliBiblio;
	private int numTerminiCollegati;


	public static final Comparator<ElementoSinteticaThesauroVO> ORDINA_PER_PROGRESSIVO = new Comparator<ElementoSinteticaThesauroVO>() {
		public int compare(ElementoSinteticaThesauroVO o1,
				ElementoSinteticaThesauroVO o2) {
			int p1 = o1.getProgr();
			int p2 = o2.getProgr();
			return p1 - p2;
		}
	};

	public ElementoSinteticaThesauroVO() {
		super();
	}

	public ElementoSinteticaThesauroVO(int progr, String did, String termine) {
		this.progr = progr;
		this.did = did;
		this.termine = termine;
	}


	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTermine() {
		return termine;
	}

	public void setTermine(String termine) {
		this.termine = termine;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTerminiCollegati() {
		return numTerminiCollegati;
	}

	public void setNumTerminiCollegati(int numTerminiCollegati) {
		this.numTerminiCollegati = numTerminiCollegati;
	}

}
