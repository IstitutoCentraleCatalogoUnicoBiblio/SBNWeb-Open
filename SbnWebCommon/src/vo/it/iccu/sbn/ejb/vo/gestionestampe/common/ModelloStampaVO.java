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
package it.iccu.sbn.ejb.vo.gestionestampe.common;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;

public class ModelloStampaVO extends BaseVO {

	public enum ModelloStampaType {
		CSV			('C'),
		JRXML		('J'),
		FILE		('F');

		private final char type;

		private ModelloStampaType(char type) {
			this.type = type;
		}

		public static final ModelloStampaType fromChar(char value) {
			for (ModelloStampaType mod : ModelloStampaType.values())
				if (mod.type == value)
					return mod;
			return null;
		}

	}

	private static final long serialVersionUID = -8513806297792188305L;
	private int idModello;
	private String codPolo;
	private String codBib;
	private String nomeModello;
	private ModelloStampaType tipoModello;
	private String descrizione;
	private String attivita;
	private String jrxml;
	private List<String> subReports = new ArrayList<String>();
	private String descrizioneBib;

	public ModelloStampaVO() {
		super();
	}

	public ModelloStampaVO(int idModello) {
		this.idModello = idModello;
	}

	public int getIdModello() {
		return idModello;
	}

	public void setIdModello(int idModello) {
		this.idModello = idModello;
	}

	public String getNomeModello() {
		return nomeModello;
	}

	public void setNomeModello(String nomeModello) {
		this.nomeModello = nomeModello;
	}

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String codAttivita) {
		this.attivita = trimAndSet(codAttivita);
	}

	public String getJrxml() {
		return jrxml;
	}

	public void setJrxml(String jrxml) {
		this.jrxml = trimAndSet(jrxml);
	}

	public List<String> getSubReports() {
		return subReports;
	}

	public void setSubReports(List<String> subReports) {
		this.subReports = subReports;
	}

	public boolean hasSubReports() {
		return isFilled(subReports);
	}

	public int compareTo(ModelloStampaVO mod) {

		if (mod == null)
			throw new NullPointerException();

		return this.idModello - mod.idModello;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public ModelloStampaType getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(ModelloStampaType tipoModello) {
		this.tipoModello = tipoModello;
	}

	public String getDescrizioneBib() {
		return descrizioneBib;
	}

	public void setDescrizioneBib(String descrizioneBib) {
		this.descrizioneBib = descrizioneBib;
	}

}
