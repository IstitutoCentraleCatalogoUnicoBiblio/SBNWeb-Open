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
package it.iccu.sbn.ejb.vo.servizi.sale;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

import java.util.ArrayList;
import java.util.List;

public class GruppoPostiVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -1363127310324935748L;

	int gruppo;
	int posto_da;
	int posto_a;

	private List<String> categorieMediazione = new ArrayList<String>();

	public GruppoPostiVO(GruppoPostiVO gp) {
		super();
		this.gruppo = gp.gruppo;
		this.posto_da = gp.posto_da;
		this.posto_a = gp.posto_a;
		this.categorieMediazione = gp.categorieMediazione;
	}

	public GruppoPostiVO(int gruppo, int posto_da, int posto_a, List<String> categorie) {
		super();
		this.gruppo = gruppo;
		this.posto_da = posto_da;
		this.posto_a = posto_a;
		this.categorieMediazione = categorie;
	}

	public GruppoPostiVO() {
		super();
	}

	public int getGruppo() {
		return gruppo;
	}

	public void setGruppo(int gruppo) {
		this.gruppo = gruppo;
	}

	public int getPosto_da() {
		return posto_da;
	}

	public void setPosto_da(int posto_da) {
		this.posto_da = posto_da;
	}

	public int getPosto_a() {
		return posto_a;
	}

	public void setPosto_a(int posto_a) {
		this.posto_a = posto_a;
	}

	public List<String> getCategorieMediazione() {
		return categorieMediazione;
	}

	public void setCategorieMediazione(List<String> categorieMediazione) {
		this.categorieMediazione = categorieMediazione;
	}

	public boolean contains(PostoSalaVO posto) {
		short num_posto = posto.getNum_posto();
		return this.getPosto_da() <= num_posto && num_posto <= this.getPosto_a();
	}

	public int compareTo(GruppoPostiVO gp) {
		return this.gruppo - gp.gruppo;
	}
}
