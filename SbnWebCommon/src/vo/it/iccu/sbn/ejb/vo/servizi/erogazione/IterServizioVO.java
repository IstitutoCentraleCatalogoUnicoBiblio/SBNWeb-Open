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
package it.iccu.sbn.ejb.vo.servizi.erogazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;

public class IterServizioVO extends BaseVO {

	private static final long serialVersionUID = -8307837315979906096L;
	private String codBib;
	private String codTipoServ;
	private Short progrIter;
	private String flagStampa;
	private Short numPag;
	private String testo;
	private String flgAbil;
	private String flgRinn;
	private String statoIter;
	private String codAttivita;
	private String obbl;
	private String codStatoRich;
	private String codStatoMov;
	private String codStatCir;
	private String codStatoRicIll;
	private String descrizione;
	private int idIterServizio;

	private String codServizioILL;

	// Constructors
	/** default constructor */
	public IterServizioVO() {
		codBib = "";
		codTipoServ = "";
		progrIter = new Short((short) 0);
		numPag = new Short((short) 0);
		testo = "";
		flagStampa = "N";
		flgAbil = "N";
		flgRinn = "N";
		obbl = "S";
		statoIter = "";
		codAttivita = "";
		descrizione = "";
		codStatoRich = "";
		codStatoMov = "";
		codStatCir = "";
		codStatoRicIll = "";
	}

	// Property accessors

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizioneAttivita) {
		this.descrizione = descrizioneAttivita;
	}

	public String getFlagStampa() {
		return this.flagStampa;
	}

	public void setFlagStampa(String flagStampa) {
		this.flagStampa = flagStampa;
		if (this.numPag > 0)
			this.flagStampa = "S";
	}

	public Short getNumPag() {
		return this.numPag;
	}

	public void setNumPag(Short numPag) {
		this.numPag = numPag;
		if (this.numPag > 0)
			this.flagStampa = "S";
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getFlgAbil() {
		return this.flgAbil;
	}

	public void setFlgAbil(String flgAbil) {
		this.flgAbil = flgAbil;
	}

	public String getFlgRinn() {
		return this.flgRinn;
	}

	public void setFlgRinn(String flgRinn) {
		this.flgRinn = flgRinn;
	}

	public String getStatoIter() {
		return this.statoIter;
	}

	public void setStatoIter(String statoIter) {
		this.statoIter = statoIter;
	}

	public String getCodAttivita() {
		return this.codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = trimAndSet(codAttivita);
	}

	public String getObbl() {
		return this.obbl;
	}

	public void setObbl(String obbl) {
		this.obbl = obbl;
	}

	public String getCodStatoRich() {
		return this.codStatoRich;
	}

	public void setCodStatoRich(String codStatoRich) {
		this.codStatoRich = codStatoRich;
	}

	public String getCodStatoMov() {
		return this.codStatoMov;
	}

	public void setCodStatoMov(String codStatoMov) {
		this.codStatoMov = codStatoMov;
	}

	public String getCodStatCir() {
		return this.codStatCir;
	}

	public void setCodStatCir(String codStatCir) {
		this.codStatCir = codStatCir;
	}

	public String getCodStatoRicIll() {
		return this.codStatoRicIll;
	}

	public void setCodStatoRicIll(String codStatoRicIll) {
		this.codStatoRicIll = codStatoRicIll;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodTipoServ() {
		return codTipoServ;
	}

	public void setCodTipoServ(String codTipoServ) {
		this.codTipoServ = codTipoServ;
	}

	public Short getProgrIter() {
		return progrIter;
	}

	public void setProgrIter(Short progrIter) {
		this.progrIter = progrIter;
	}

	public String getChiave() {
		return this.codAttivita + "-" + this.progrIter;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IterServizioVO other = (IterServizioVO) obj;
		if (codAttivita == null) {
			if (other.codAttivita != null)
				return false;
		} else if (!codAttivita.equals(other.codAttivita))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (codStatCir == null) {
			if (other.codStatCir != null)
				return false;
		} else if (!codStatCir.equals(other.codStatCir))
			return false;
		if (codStatoMov == null) {
			if (other.codStatoMov != null)
				return false;
		} else if (!codStatoMov.equals(other.codStatoMov))
			return false;
		if (codStatoRicIll == null) {
			if (other.codStatoRicIll != null)
				return false;
		} else if (!codStatoRicIll.equals(other.codStatoRicIll))
			return false;
		if (codStatoRich == null) {
			if (other.codStatoRich != null)
				return false;
		} else if (!codStatoRich.equals(other.codStatoRich))
			return false;
		if (codTipoServ == null) {
			if (other.codTipoServ != null)
				return false;
		} else if (!codTipoServ.equals(other.codTipoServ))
			return false;
		if (flCanc == null) {
			if (other.flCanc != null)
				return false;
		} else if (!flCanc.equals(other.flCanc))
			return false;
		if (flagStampa == null) {
			if (other.flagStampa != null)
				return false;
		} else if (!flagStampa.equals(other.flagStampa))
			return false;
		if (flgAbil == null) {
			if (other.flgAbil != null)
				return false;
		} else if (!flgAbil.equals(other.flgAbil))
			return false;
		if (flgRinn == null) {
			if (other.flgRinn != null)
				return false;
		} else if (!flgRinn.equals(other.flgRinn))
			return false;
		if (numPag == null) {
			if (other.numPag != null)
				return false;
		} else if (!numPag.equals(other.numPag))
			return false;
		if (obbl == null) {
			if (other.obbl != null)
				return false;
		} else if (!obbl.equals(other.obbl))
			return false;
		if (progrIter == null) {
			if (other.progrIter != null)
				return false;
		} else if (!progrIter.equals(other.progrIter))
			return false;
		if (statoIter == null) {
			if (other.statoIter != null)
				return false;
		} else if (!statoIter.equals(other.statoIter))
			return false;
		if (testo == null) {
			if (other.testo != null)
				return false;
		} else if (!testo.equals(other.testo))
			return false;
		// if (tsIns == null) {
		// if (other.tsIns != null)
		// return false;
		// } else if (!tsIns.equals(other.tsIns))
		// return false;
		// if (tsVar == null) {
		// if (other.tsVar != null)
		// return false;
		// } else if (!tsVar.equals(other.tsVar))
		// return false;
		// if (uteIns == null) {
		// if (other.uteIns != null)
		// return false;
		// } else if (!uteIns.equals(other.uteIns))
		// return false;
		// if (uteVar == null) {
		// if (other.uteVar != null)
		// return false;
		// } else if (!uteVar.equals(other.uteVar))
		// return false;
		return true;
	}

	public void setIdIterServizio(int idIterServizio) {
		this.idIterServizio = idIterServizio;
	}

	public int getIdIterServizio() {
		return idIterServizio;
	}

	public boolean isRinnovabile() {
		return ValidazioneDati.in(flgRinn, "s", "S");
	}

	public String getCodServizioILL() {
		return codServizioILL;
	}

	public void setCodServizioILL(String codServizioILL) {
		this.codServizioILL = trimAndSet(codServizioILL);
	}

}
