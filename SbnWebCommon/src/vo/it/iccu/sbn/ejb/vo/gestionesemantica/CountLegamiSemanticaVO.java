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
package it.iccu.sbn.ejb.vo.gestionesemantica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class CountLegamiSemanticaVO extends SerializableVO {

	private static final long serialVersionUID = 160210295373085543L;
	private String bid;
	private int countLegamiSoggetto;
	private int countLegamiClasse;
	private int countLegameThesauro;
	private int countLegameAbstract;

	/**
	 * @param bid
	 * @param countLegamiSoggetto
	 * @param countLegamiClasse
	 * @param countLegameThesauro
	 * @param countLegameAbstract
	 */
	public CountLegamiSemanticaVO(String bid, int countLegamiSoggetto,
			int countLegamiClasse, int countLegameThesauro,
			int countLegameAbstract) {
		super();
		this.bid = bid;
		this.countLegamiSoggetto = countLegamiSoggetto;
		this.countLegamiClasse = countLegamiClasse;
		this.countLegameThesauro = countLegameThesauro;
		this.countLegameAbstract = countLegameAbstract;
	}

	public String getBid() {
		return bid;
	}

	public int getCountLegamiSoggetto() {
		return countLegamiSoggetto;
	}

	public int getCountLegamiClasse() {
		return countLegamiClasse;
	}

	public int getCountLegameThesauro() {
		return countLegameThesauro;
	}

	public int getCountLegameAbstract() {
		return countLegameAbstract;
	}

	@Override
	public String toString() {
		return bid + " " + countLegamiSoggetto + " " + countLegamiClasse + " "
				+ countLegameThesauro + " " + countLegameAbstract;
	}

	public boolean isLegato() {
		return (countLegamiSoggetto + countLegamiClasse +
				countLegameThesauro + countLegameAbstract) > 0;
	}

}
