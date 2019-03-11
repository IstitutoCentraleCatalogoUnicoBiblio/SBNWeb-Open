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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;
import java.util.List;

public class ElementoSinteticaSoggettoVO extends SerializableVO {

	private static final long serialVersionUID = 7998107038266585514L;

	private int progr;
	private String cid;
	private String stato;
	private String indicatore;
	private String testo;
	private String cidIndice;
	private String condivisoLista;
	private String categoriaSoggetto;
	private String nota;
	private int numTitoliPolo;
	private int numTitoliBiblio;
	private String codiceSoggettario;
	private String edizioneSoggettario;
	private boolean condiviso;
	private List<DatiCondivisioneSoggettoVO> datiCondivisione = null;
	private String maxLivAutLegame;

	private short rank;


	public static final Comparator<ElementoSinteticaSoggettoVO> ORDINA_PER_PROGRESSIVO = new Comparator<ElementoSinteticaSoggettoVO>() {
		public int compare(ElementoSinteticaSoggettoVO o1, ElementoSinteticaSoggettoVO o2) {
			int p1 = o1.getProgr();
			int p2 = o2.getProgr();
			return p1-p2;
		}
	};

	public ElementoSinteticaSoggettoVO() {
		super();
	}

	public ElementoSinteticaSoggettoVO(int progr, String cid, String stato, String indicatore,
			String testo, String condivisoLista, String tipoSoggetto) throws Exception {

		this.progr = progr;
		this.cid = cid;
		this.stato = stato;
		this.indicatore = indicatore;
		this.testo = testo;
		this.condivisoLista = condivisoLista;
		this.categoriaSoggetto = tipoSoggetto;
	}

	public ElementoSinteticaSoggettoVO(DettaglioSoggettoVO dettaglio) {
		this.progr = 1;
		this.cid = dettaglio.getCid();
		this.stato = dettaglio.getLivAut();
		this.testo = dettaglio.getTesto();
		this.categoriaSoggetto = dettaglio.getCategoriaSoggetto();
		this.nota = dettaglio.getNote();

		this.codiceSoggettario = dettaglio.getCampoSoggettario();
		this.edizioneSoggettario = dettaglio.getEdizioneSoggettario();
	}

	public List<DatiCondivisioneSoggettoVO> getDatiCondivisione() {
		return datiCondivisione;
	}

	public void setDatiCondivisione(
			List<DatiCondivisioneSoggettoVO> datiCondivisione) {
		this.datiCondivisione = datiCondivisione;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getIndicatore() {
		return indicatore;
	}

	public void setIndicatore(String indicatore) {
		this.indicatore = indicatore;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getCid() {
		return cid;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
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

	public String getCondivisoLista() {
		return condivisoLista;
	}

	public void setCondivisoLista(String condivisoLista) {
		this.condivisoLista = condivisoLista;
	}

	public String getCidIndice() {
		return cidIndice;
	}

	public void setCidIndice(String cidIndice) {
		this.cidIndice = cidIndice;
	}

	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}

	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime
				* result
				+ ((codiceSoggettario == null) ? 0 : codiceSoggettario.toUpperCase()
						.hashCode());
		result = prime * result + ((testo == null) ? 0 : testo.toUpperCase().hashCode());
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
		final ElementoSinteticaSoggettoVO other = (ElementoSinteticaSoggettoVO) obj;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (codiceSoggettario == null) {
			if (other.codiceSoggettario != null)
				return false;
		} else if (!codiceSoggettario.equalsIgnoreCase(other.codiceSoggettario))
			return false;
		if (testo == null) {
			if (other.testo != null)
				return false;
		} else if (!testo.equalsIgnoreCase(other.testo))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("[cd_sogg: ");
		buf.append(codiceSoggettario);
		buf.append(" edizione: ");
		buf.append(edizioneSoggettario);
		buf.append(" cid: ");
		buf.append(cid);
		buf.append(" testo: ");
		buf.append(getTesto());
		buf.append("]");

		return buf.toString();
	}

	public String getMaxLivAutLegame() {
		return maxLivAutLegame;
	}

	public void setMaxLivAutLegame(String maxLivAutLegame) {
		this.maxLivAutLegame = maxLivAutLegame;
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

	public boolean isGestisceEdizione() {
		return isFilled(edizioneSoggettario);
	}

	public short getRank() {
		return rank;
	}

	public void setRank(short rank) {
		this.rank = rank;
	}

}
