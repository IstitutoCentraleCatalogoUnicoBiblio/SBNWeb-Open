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

package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;


import java.util.List;

public class DettaglioSoggettoVO extends DettaglioSogDesBaseVO {

	private static final long serialVersionUID = 5964583624964139899L;

	private String cid;
	private String categoriaSoggetto = "1";
	private String campoStringaSoggetto;
	private int numTitoliPolo;
	private int numTitoliBiblio;

	private boolean has_num_tit_coll;
	private boolean has_num_tit_coll_bib;

	private String tipoLegame = "";

	private List<DatiCondivisioneSoggettoVO> datiCondivisione = null;

	public List<DatiCondivisioneSoggettoVO> getDatiCondivisione() {
		return datiCondivisione;
	}

	public void setDatiCondivisione(
			List<DatiCondivisioneSoggettoVO> datiCondivisione) {
		this.datiCondivisione = datiCondivisione;
	}

	public String getCampoStringaSoggetto() {
		return campoStringaSoggetto;
	}

	public void setCampoStringaSoggetto(String campoStringaSoggetto) {
		this.campoStringaSoggetto = campoStringaSoggetto;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getCategoriaSoggetto() {
		return categoriaSoggetto;
	}

	public void setCategoriaSoggetto(String categoriaSoggetto) {
		this.categoriaSoggetto = categoriaSoggetto;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public boolean isHas_num_tit_coll() {
		return has_num_tit_coll;
	}

	public void setHas_num_tit_coll(boolean has_num_tit_coll) {
		this.has_num_tit_coll = has_num_tit_coll;
	}

	public boolean isHas_num_tit_coll_bib() {
		return has_num_tit_coll_bib;
	}

	public void setHas_num_tit_coll_bib(boolean has_num_tit_coll_bib) {
		this.has_num_tit_coll_bib = has_num_tit_coll_bib;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("[cd_sogg: ");
		buf.append(campoSoggettario);
		buf.append(" edizione: ");
		buf.append(edizioneSoggettario);
		buf.append(" cid: ");
		buf.append(cid);
		buf.append(" testo: ");
		buf.append(getTesto());
		buf.append("]");

		return buf.toString();
	}

}
