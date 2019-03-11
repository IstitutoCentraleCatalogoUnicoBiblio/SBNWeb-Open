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
package it.iccu.sbn.ejb.vo.servizi.configurazione;

import it.iccu.sbn.ejb.vo.BaseVO;

public class ServizioWebDatiRichiestiVO extends BaseVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -1458571823716595344L;

	private int idTipoServizio;
	private String codPolo;
	private String codBib;
	private String codiceTipoServizio;

	private String descrizione;
	private int campoRichiesta = 0;
	private boolean obbligatorio = false;
	private boolean utilizzato = false;

	private String ordinamento;

	private String note;

	private boolean obbligatorioTabCodici = false;

	public int getIdTipoServizio() {
		return idTipoServizio;
	}

	public void setIdTipoServizio(int idTipoServizio) {
		this.idTipoServizio = idTipoServizio;
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

	public String getCodiceTipoServizio() {
		return codiceTipoServizio;
	}

	public void setCodiceTipoServizio(String codiceTipoServizio) {
		this.codiceTipoServizio = codiceTipoServizio;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getCampoRichiesta() {
		return campoRichiesta;
	}

	public void setCampoRichiesta(int campoRichiesta) {
		this.campoRichiesta = campoRichiesta;
	}

	public boolean isObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(boolean obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public boolean isUtilizzato() {
		return utilizzato;
	}

	public void setUtilizzato(boolean utilizzato) {
		this.utilizzato = utilizzato;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isObbligatorioTabCodici() {
		return obbligatorioTabCodici;
	}

	public void setObbligatorioTabCodici(boolean obbligatorioTabCodici) {
		this.obbligatorioTabCodici = obbligatorioTabCodici;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + campoRichiesta;
		result = prime * result + ((codBib == null) ? 0 : codBib.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime
				* result
				+ ((codiceTipoServizio == null) ? 0 : codiceTipoServizio
						.hashCode());
		result = prime * result
				+ ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + idTipoServizio;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + (obbligatorio ? 1231 : 1237);
		result = prime * result + (obbligatorioTabCodici ? 1231 : 1237);
		result = prime * result
				+ ((ordinamento == null) ? 0 : ordinamento.hashCode());
		result = prime * result + (utilizzato ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServizioWebDatiRichiestiVO other = (ServizioWebDatiRichiestiVO) obj;
		if (campoRichiesta != other.campoRichiesta)
			return false;
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
		if (codiceTipoServizio == null) {
			if (other.codiceTipoServizio != null)
				return false;
		} else if (!codiceTipoServizio.equals(other.codiceTipoServizio))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idTipoServizio != other.idTipoServizio)
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (obbligatorio != other.obbligatorio)
			return false;
		if (obbligatorioTabCodici != other.obbligatorioTabCodici)
			return false;
		if (ordinamento == null) {
			if (other.ordinamento != null)
				return false;
		} else if (!ordinamento.equals(other.ordinamento))
			return false;
		if (utilizzato != other.utilizzato)
			return false;
		return true;
	}

}
