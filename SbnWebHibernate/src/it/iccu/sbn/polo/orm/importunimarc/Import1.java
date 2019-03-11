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
package it.iccu.sbn.polo.orm.importunimarc;

import java.io.Serializable;

public class Import1 implements Serializable {

	private static final long serialVersionUID = 5777884421488641386L;

	private int id;
	private String id_input;
	private String leader;
	private String tag;
	private Character indicatore1;
	private Character indicatore2;
	private String id_link;
	private String dati;
	private Integer nr_richiesta;
	private Character stato_id_input;
	private Character stato_tag;
	private Integer id_batch;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getId_input() {
		return id_input;
	}

	public void setId_input(String id_input) {
		this.id_input = id_input;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Character getIndicatore1() {
		return indicatore1;
	}

	public void setIndicatore1(Character indicatore1) {
		this.indicatore1 = indicatore1;
	}

	public Character getIndicatore2() {
		return indicatore2;
	}

	public void setIndicatore2(Character indicatore2) {
		this.indicatore2 = indicatore2;
	}

	public String getId_link() {
		return id_link;
	}

	public void setId_link(String id_link) {
		this.id_link = id_link;
	}

	public String getDati() {
		return dati;
	}

	public void setDati(String dati) {
		this.dati = dati;
	}

	public Integer getNr_richiesta() {
		return nr_richiesta;
	}

	public void setNr_richiesta(Integer nr_richiesta) {
		this.nr_richiesta = nr_richiesta;
	}

	public Character getStato_id_input() {
		return stato_id_input;
	}

	public void setStato_id_input(Character stato_id_input) {
		this.stato_id_input = stato_id_input;
	}

	public Character getStato_tag() {
		return stato_tag;
	}

	public void setStato_tag(Character stato_tag) {
		this.stato_tag = stato_tag;
	}

	public Integer getId_batch() {
		return id_batch;
	}

	public void setId_batch(Integer id_batch) {
		this.id_batch = id_batch;
	}

}
