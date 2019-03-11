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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_titolo;

/**
 * Classe Ve_audiovisivo_aut_aut ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
 * AUTOMATICAMENTE. NESSUNA MODIFICA DEVE ESSERE APPORTATA MANUALMENTE, PERCHE'
 * SARA' PERSA IN FUTURO. OGNI AGGIUNTA MANUALE NON E' ATTUALMENTE POSSIBILE.
 *
 * <p>
 * Classe che contiene gli attributi estratti dalla tavola del DB
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 10/10/2014
 */
public class Ve_audiovisivo_aut_aut extends Tb_titolo {

	private static final long serialVersionUID = 4500388526388763162L;

	private String VID;
	private String FL_SUPERFLUO;
	private String TP_RESPONSABILITA;
	private String CD_RELAZIONE;
	private String NOTA_TIT_AUT;
	private String FL_INCERTO;
	private String CD_STRUMENTO_MUS;

	public void setTP_RESPONSABILITA(String value) {
		this.TP_RESPONSABILITA = value;
		this.settaParametro(KeyParameter.XXXtp_responsabilita, value);
	}

	public String getTP_RESPONSABILITA() {
		return TP_RESPONSABILITA;
	}

	public void setCD_RELAZIONE(String value) {
		this.CD_RELAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_relazione, value);
	}

	public String getCD_RELAZIONE() {
		return CD_RELAZIONE;
	}

	public void setNOTA_TIT_AUT(String value) {
		this.NOTA_TIT_AUT = value;
		this.settaParametro(KeyParameter.XXXnota_tit_aut, value);
	}

	public String getNOTA_TIT_AUT() {
		return NOTA_TIT_AUT;
	}

	public void setFL_INCERTO(String value) {
		this.FL_INCERTO = value;
		this.settaParametro(KeyParameter.XXXfl_incerto, value);
	}

	public String getFL_INCERTO() {
		return FL_INCERTO;
	}

	public void setFL_SUPERFLUO(String value) {
		this.FL_SUPERFLUO = value;
		this.settaParametro(KeyParameter.XXXfl_superfluo, value);
	}

	public String getFL_SUPERFLUO() {
		return FL_SUPERFLUO;
	}

	public void setCD_STRUMENTO_MUS(String value) {
		this.CD_STRUMENTO_MUS = value;
		this.settaParametro(KeyParameter.XXXcd_strumento_mus, value);
	}

	public String getCD_STRUMENTO_MUS() {
		return CD_STRUMENTO_MUS;
	}

	public void setVID(String value) {
		this.VID = value;
		this.settaParametro(KeyParameter.XXXvid, value);
	}

	public String getVID() {
		return VID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String
				.valueOf(getBID()))
				+ " "
				+ ((getVID() == null) ? "" : String.valueOf(getVID()))
				+ " "
				+ getTP_RESPONSABILITA() + " " + getCD_RELAZIONE());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved = true;
	}

	public void onLoad() {
		_saved = true;
	}

	public boolean isSaved() {
		return _saved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CD_RELAZIONE == null) ? 0 : CD_RELAZIONE.hashCode());
		result = prime * result + ((CD_STRUMENTO_MUS == null) ? 0 : CD_STRUMENTO_MUS.hashCode());
		result = prime * result + ((FL_INCERTO == null) ? 0 : FL_INCERTO.hashCode());
		result = prime * result + ((FL_SUPERFLUO == null) ? 0 : FL_SUPERFLUO.hashCode());
		result = prime * result + ((NOTA_TIT_AUT == null) ? 0 : NOTA_TIT_AUT.hashCode());
		result = prime * result + ((TP_RESPONSABILITA == null) ? 0 : TP_RESPONSABILITA.hashCode());
		result = prime * result + ((VID == null) ? 0 : VID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Ve_audiovisivo_aut_aut other = (Ve_audiovisivo_aut_aut) obj;
		if (CD_RELAZIONE == null) {
			if (other.CD_RELAZIONE != null) {
				return false;
			}
		} else if (!CD_RELAZIONE.equals(other.CD_RELAZIONE)) {
			return false;
		}
		if (CD_STRUMENTO_MUS == null) {
			if (other.CD_STRUMENTO_MUS != null) {
				return false;
			}
		} else if (!CD_STRUMENTO_MUS.equals(other.CD_STRUMENTO_MUS)) {
			return false;
		}
		if (FL_INCERTO == null) {
			if (other.FL_INCERTO != null) {
				return false;
			}
		} else if (!FL_INCERTO.equals(other.FL_INCERTO)) {
			return false;
		}
		if (FL_SUPERFLUO == null) {
			if (other.FL_SUPERFLUO != null) {
				return false;
			}
		} else if (!FL_SUPERFLUO.equals(other.FL_SUPERFLUO)) {
			return false;
		}
		if (NOTA_TIT_AUT == null) {
			if (other.NOTA_TIT_AUT != null) {
				return false;
			}
		} else if (!NOTA_TIT_AUT.equals(other.NOTA_TIT_AUT)) {
			return false;
		}
		if (TP_RESPONSABILITA == null) {
			if (other.TP_RESPONSABILITA != null) {
				return false;
			}
		} else if (!TP_RESPONSABILITA.equals(other.TP_RESPONSABILITA)) {
			return false;
		}
		if (VID == null) {
			if (other.VID != null) {
				return false;
			}
		} else if (!VID.equals(other.VID)) {
			return false;
		}
		return true;
	}

}
