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

public class TipoServizioVO extends BaseVO {

	private static final long serialVersionUID = 6969859961128921436L;
	private int idTipoServizio;
	private String codPolo;
	private String codBib;
	private String codiceTipoServizio;
	private String descrizione;
	private int numMaxMov = 0;
	private int oreRidis = 0;
	private int ggRidis = 0;
	private boolean penalita = false;
	private boolean codaRichieste = false;
	// private int codaRichieste = 0;
	private boolean ins_richieste_utente = false;
	private boolean anche_da_remoto = false;

	private String codServizioILL;

	public TipoServizioVO() {
		super();
	}

	public TipoServizioVO(BaseVO base) {
		super(base);
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

	public int getNumMaxMov() {
		return numMaxMov;
	}

	public void setNumMaxMov(int numMaxMov) {
		this.numMaxMov = numMaxMov;
	}

	public int getOreRidis() {
		return oreRidis;
	}

	public void setOreRidis(int oreRidis) {
		this.oreRidis = oreRidis;
	}

	public boolean isPenalita() {
		return penalita;
	}

	public void setPenalita(boolean penalita) {
		this.penalita = penalita;
	}

	public boolean isCodaRichieste() {
		return codaRichieste;
	}

	public void setCodaRichieste(boolean codaRichieste) {
		this.codaRichieste = codaRichieste;
	}

	/*
	 * public int getCodaRichieste() { return codaRichieste; }
	 *
	 * public void setCodaRichieste(int codaRichieste) { this.codaRichieste =
	 * codaRichieste; }
	 */

	public boolean isIns_richieste_utente() {
		return ins_richieste_utente;
	}

	public void setIns_richieste_utente(boolean ins_richieste_utente) {
		this.ins_richieste_utente = ins_richieste_utente;
	}

	public boolean isAnche_da_remoto() {
		return anche_da_remoto;
	}

	public void setAnche_da_remoto(boolean anche_da_remoto) {
		this.anche_da_remoto = anche_da_remoto;
	}

	public String getCodServizioILL() {
		return codServizioILL;
	}

	public void setCodServizioILL(String codServizioILL) {
		this.codServizioILL = trimAndSet(codServizioILL);
	}

	public boolean isILL() {
		return isFilled(codServizioILL);
	}

	public boolean isValid() {
		return (this.codBib != null && this.codPolo != null && this.codiceTipoServizio != null);
	}

	@Override
	public boolean equals(Object obj) {
		final TipoServizioVO other = (TipoServizioVO) obj;
		if (!super.equals(obj))
			return false;
		else {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;

			if (codPolo == null) {
				if (other.codPolo != null)
					return false;
			} else if (!codPolo.equals(other.codPolo))
				return false;
			if (codBib == null) {
				if (other.codBib != null)
					return false;
			} else if (!codBib.equals(other.codBib))
				return false;
			if (codiceTipoServizio == null) {
				if (other.codiceTipoServizio != null)
					return false;
			} else if (!codiceTipoServizio.equals(other.codiceTipoServizio))
				return false;
			if (codServizioILL == null) {
				if (other.codServizioILL != null)
					return false;
			} else if (!codServizioILL.equals(other.codServizioILL))
				return false;

		}
		return ((this.penalita == other.penalita)
				&& (this.codaRichieste == other.codaRichieste)
				&& (this.numMaxMov == other.numMaxMov)
				&& (this.oreRidis == other.oreRidis)
				&& (this.ggRidis == other.ggRidis)
				&& (this.ins_richieste_utente == other.ins_richieste_utente) && (this.anche_da_remoto == other.anche_da_remoto));
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getGgRidis() {
		return ggRidis;
	}

	public void setGgRidis(int ggRidis) {
		this.ggRidis = ggRidis;
	}

	public int getIdTipoServizio() {
		return idTipoServizio;
	}

	public void setIdTipoServizio(int idTipoServizio) {
		this.idTipoServizio = idTipoServizio;
	}

	@Override
	public boolean isNuovo() {
		return (idTipoServizio == 0);
	}

}
