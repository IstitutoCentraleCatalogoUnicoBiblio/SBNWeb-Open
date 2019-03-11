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

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;

import java.util.ArrayList;
import java.util.List;

public class SalaVO extends BaseVO {

	private static final long serialVersionUID = 3461857563412481158L;

	private int idSala;

	private String codPolo;
	private String codBib;
	private String codSala;
	private String descrizione;
	private short numeroPosti;
	private short postiOccupati;

	private ModelloCalendarioVO calendario;

	private List<PostoSalaVO> posti = new ArrayList<PostoSalaVO>();

	private List<GruppoPostiVO> gruppi = new ArrayList<GruppoPostiVO>();

	private short durataFascia = 30;
	private short maxFascePrenotazione = 3;
	private short maxUtentiPerPrenotazione = 1;

	private boolean prenotabileDaRemoto = true;

	public SalaVO() {
		super();
	}

	public SalaVO(SalaVO s) {
		super(s);
		this.idSala = s.idSala;
		this.codPolo = s.codPolo;
		this.codBib = s.codBib;
		this.codSala = s.codSala;
		this.descrizione = s.descrizione;
		this.numeroPosti = s.numeroPosti;
		this.postiOccupati = s.postiOccupati;
		this.calendario = s.calendario;
		this.posti = s.posti;
		this.gruppi = s.gruppi;
		this.durataFascia = s.durataFascia;
		this.maxFascePrenotazione = s.maxFascePrenotazione;
		this.maxUtentiPerPrenotazione = s.maxUtentiPerPrenotazione;
		this.prenotabileDaRemoto = s.prenotabileDaRemoto;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
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

	public String getCodSala() {
		return codSala;
	}

	public void setCodSala(String codSala) {
		this.codSala = trimOrEmpty(codSala).toUpperCase();
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public short getNumeroPosti() {
		return numeroPosti;
	}

	public void setNumeroPosti(short numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	public short getPostiOccupati() {
		return postiOccupati;
	}

	public void setPostiOccupati(short postiOccupati) {
		this.postiOccupati = postiOccupati;
	}

	public ModelloCalendarioVO getCalendario() {
		return calendario;
	}

	public void setCalendario(ModelloCalendarioVO calendario) {
		this.calendario = calendario;
	}

	@Override
	public int getRepeatableId() {
		return idSala;
	}

	public boolean isNuovo() {
		return idSala == 0;
	}

	public List<PostoSalaVO> getPosti() {
		return posti;
	}

	public void setPosti(List<PostoSalaVO> posti) {
		this.posti = posti;
	}

	public List<GruppoPostiVO> getGruppi() {
		return gruppi;
	}

	public void setGruppi(List<GruppoPostiVO> gruppi) {
		this.gruppi = gruppi;
	}

	public short getDurataFascia() {
		return durataFascia;
	}

	public void setDurataFascia(short durataFascia) {
		this.durataFascia = durataFascia;
	}

	public short getMaxFascePrenotazione() {
		return maxFascePrenotazione;
	}

	public void setMaxFascePrenotazione(short maxFascePrenotazione) {
		this.maxFascePrenotazione = maxFascePrenotazione;
	}

	public short getMaxUtentiPerPrenotazione() {
		return maxUtentiPerPrenotazione;
	}

	public void setMaxUtentiPerPrenotazione(short maxUtentePerPrenotazione) {
		this.maxUtentiPerPrenotazione = maxUtentePerPrenotazione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((calendario == null) ? 0 : calendario.hashCode());
		result = prime * result + ((codBib == null) ? 0 : codBib.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result + ((codSala == null) ? 0 : codSala.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + durataFascia;
		result = prime * result + ((gruppi == null) ? 0 : gruppi.hashCode());
		result = prime * result + idSala;
		result = prime * result + maxFascePrenotazione;
		result = prime * result + numeroPosti;
		result = prime * result + ((posti == null) ? 0 : posti.hashCode());
		result = prime * result + postiOccupati;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SalaVO other = (SalaVO) obj;
		if (calendario == null) {
			if (other.calendario != null) {
				return false;
			}
		} else if (!calendario.equals(other.calendario)) {
			return false;
		}
		if (codBib == null) {
			if (other.codBib != null) {
				return false;
			}
		} else if (!codBib.equals(other.codBib)) {
			return false;
		}
		if (codPolo == null) {
			if (other.codPolo != null) {
				return false;
			}
		} else if (!codPolo.equals(other.codPolo)) {
			return false;
		}
		if (codSala == null) {
			if (other.codSala != null) {
				return false;
			}
		} else if (!codSala.equals(other.codSala)) {
			return false;
		}
		if (descrizione == null) {
			if (other.descrizione != null) {
				return false;
			}
		} else if (!descrizione.equals(other.descrizione)) {
			return false;
		}
		if (durataFascia != other.durataFascia) {
			return false;
		}
		if (gruppi == null) {
			if (other.gruppi != null) {
				return false;
			}
		} else if (!gruppi.equals(other.gruppi)) {
			return false;
		}
		if (idSala != other.idSala) {
			return false;
		}
		if (maxFascePrenotazione != other.maxFascePrenotazione) {
			return false;
		}
		if (maxUtentiPerPrenotazione != other.maxUtentiPerPrenotazione) {
			return false;
		}
		if (numeroPosti != other.numeroPosti) {
			return false;
		}
		if (posti == null) {
			if (other.posti != null) {
				return false;
			}
		} else if (!posti.equals(other.posti)) {
			return false;
		}
		if (postiOccupati != other.postiOccupati) {
			return false;
		}
		return true;
	}

	public boolean isPrenotabileDaRemoto() {
		return prenotabileDaRemoto;
	}

	public void setPrenotabileDaRemoto(boolean prenotaDaRemoto) {
		this.prenotabileDaRemoto = prenotaDaRemoto;
	}

}
