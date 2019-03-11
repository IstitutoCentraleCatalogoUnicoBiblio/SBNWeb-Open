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
package it.iccu.sbn.ejb.vo.servizi.relazioni;

import it.iccu.sbn.ejb.vo.BaseVO;

public class RelazioniVO extends BaseVO {

	private static final long serialVersionUID = 367301675315433615L;
	private int progr;
	private int id;
	private String codPolo;
	private String codBib;
	private String codRelazione;
	private String tabellaRelazione;
	private int idTabellaRelazione;
	private String descIdTabellaRelazione;
	private String tabellaCodici;
	private String idTabellaCodici;
	private String descIdTabellaCodici;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCodRelazione() {
		return codRelazione;
	}

	public void setCodRelazione(String codRelazione) {
		this.codRelazione = codRelazione;
	}

	public String getTabellaRelazione() {
		return tabellaRelazione;
	}

	public void setTabellaRelazione(String tabellaRelazione) {
		this.tabellaRelazione = tabellaRelazione;
	}

	public int getIdTabellaRelazione() {
		return idTabellaRelazione;
	}

	public void setIdTabellaRelazione(int idTabellaRelazione) {
		this.idTabellaRelazione = idTabellaRelazione;
	}

	public String getDescIdTabellaRelazione() {
		return descIdTabellaRelazione;
	}

	public void setDescIdTabellaRelazione(String descIdTabellaRelazione) {
		this.descIdTabellaRelazione = descIdTabellaRelazione;
	}

	public String getTabellaCodici() {
		return tabellaCodici;
	}

	public void setTabellaCodici(String tabellaCodici) {
		this.tabellaCodici = tabellaCodici;
	}

	public String getIdTabellaCodici() {
		return idTabellaCodici;
	}

	public void setIdTabellaCodici(String idTabellaCodici) {
		this.idTabellaCodici = idTabellaCodici;
	}

	public String getDescIdTabellaCodici() {
		return descIdTabellaCodici;
	}

	public void setDescIdTabellaCodici(String descIdTabellaCodici) {
		this.descIdTabellaCodici = descIdTabellaCodici;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final RelazioniVO other = (RelazioniVO) obj;

		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (codRelazione == null) {
			if (other.codRelazione != null)
				return false;
		} else if (!codRelazione.equals(other.codRelazione))
			return false;
		if (tabellaRelazione == null) {
			if (other.tabellaRelazione != null)
				return false;
		} else if (!tabellaRelazione.equals(other.tabellaRelazione))
			return false;
		if (idTabellaRelazione != other.idTabellaRelazione)
			return false;
		if (tabellaCodici == null) {
			if (other.tabellaCodici != null)
				return false;
		} else if (!tabellaCodici.equals(other.tabellaCodici))
			return false;
		if (idTabellaCodici != other.idTabellaCodici)
			return false;

		return true;
	}

}
