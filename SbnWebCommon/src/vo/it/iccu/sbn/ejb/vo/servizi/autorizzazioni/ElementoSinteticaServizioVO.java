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
package it.iccu.sbn.ejb.vo.servizi.autorizzazioni;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.Comparator;

public class ElementoSinteticaServizioVO extends BaseVO {

	public static final Comparator<ElementoSinteticaServizioVO> ORDINAMENTO_PER_TIPO_SRV_DIRITTO = new Comparator<ElementoSinteticaServizioVO>() {

		public int compare(ElementoSinteticaServizioVO o1, ElementoSinteticaServizioVO o2) {
			int cmp = ValidazioneDati.compare(o1.tipServizio, o2.tipServizio);
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(o1.codServizio, o2.codServizio);
			return cmp;
		}
	};

	private static final long serialVersionUID = -4239566091947504492L;

	private String codBiblioteca;
	private String codPolo;
	private int idServizio;
	private String tipServizio;
	private String codServizio;
	private String desServizio;
	private int stato = ElementoSinteticaServizioVO.NEW;
    private String cancella;

	private String descrizioneTipoServizio = "";
	private String codAut = "";

	public static int NEW = 1;
	public static int OLD = 2;
	public static int DEL = 3;
	public static int ELI = 4;

    public ElementoSinteticaServizioVO(String codBiblioteca, String tipServizio, String codServizio, String desServizio , String cancella, int stato) throws Exception {
    	super();

		if (codBiblioteca == null) {
			throw new Exception("Codice Biblioteca non valido");
	    }
		this.codBiblioteca = toUpperCase(codBiblioteca);
		if (tipServizio == null) {
			throw new Exception("Tipo Servizio non valido");
	    }
		this.tipServizio = toUpperCase(tipServizio);
		if (codServizio == null) {
			throw new Exception("Codice Servizio non valido");
	    }
		this.codServizio = toUpperCase(codServizio);
		if (desServizio == null) {
			throw new Exception("Descrizione Servizio non valida");
		}
		this.cancella = cancella;
		this.stato = stato;
    }

    public ElementoSinteticaServizioVO(){
    	super();
    }

    public String getCancella() {
		return cancella;
	}

    public void resetCancella() {
		this.cancella = "";
    }

	public void setCancella(String cancella) {
		this.cancella = cancella;
		if (ValidazioneDati.equals(this.cancella, "C") ) {
			if (this.stato == ElementoSinteticaServizioVO.OLD) {
				this.stato = ElementoSinteticaServizioVO.DEL;
			} else {
				this.stato = ElementoSinteticaServizioVO.ELI;
			}
		}
	}

	public String getComponi() {
		// diritto
		String descrComposta="";
		if (desServizio == null) desServizio = "";
		if (desServizio.trim().equals(""))
		{
			descrComposta=codServizio.trim() ;
		}
		else
		{
			descrComposta=codServizio.trim()+ "-" + desServizio.trim() ;
		}
		return descrComposta ;
	}

	public String getComponiTipoServizio() {
		// serrvizio
		String descrComposta="";
		if (descrizioneTipoServizio == null) descrizioneTipoServizio = "";
		if (descrizioneTipoServizio.trim().equals(""))
		{
			descrComposta=tipServizio.trim() ;
		}
		else
		{
			descrComposta=tipServizio.trim()+ "-" + descrizioneTipoServizio.trim() ;
		}
		return descrComposta ;
	}

	public String getCodServizio() {
		return codServizio;
	}

	public void setCodServizio(String codServizio) {
		this.codServizio = toUpperCase(codServizio);
	}

	public String getDesServizio() {
		return desServizio;
	}

	public void setDesServizio(String desServizio) {
		this.desServizio = desServizio;
	}

	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public String getTipServizio() {
		return tipServizio;
	}

	public void setTipServizio(String tipServizio) {
		this.tipServizio = tipServizio;
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = toUpperCase(codBiblioteca);
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = toUpperCase(codPolo);
	}

	public int getIdServizio() {
		return idServizio;
	}

	public void setIdServizio(int idServizio) {
		this.idServizio = idServizio;
	}

	public int getProgressivo() {
		return this.progr;
	}

	public void setProgressivo(int progressivo) {
		this.progr = progressivo;
	}

	public String getDescrizioneTipoServizio() {
		return descrizioneTipoServizio;
	}

	public void setDescrizioneTipoServizio(String descrizioneTipoServizio) {
		this.descrizioneTipoServizio = descrizioneTipoServizio;
	}

	public String getCodAut() {
		return codAut;
	}

	public void setCodAut(String codAut) {
		this.codAut = codAut;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementoSinteticaServizioVO other = (ElementoSinteticaServizioVO) obj;
		if (codServizio == null) {
			if (other.codServizio != null)
				return false;
		} else if (!codServizio.equals(other.codServizio))
			return false;
		if (idServizio != other.idServizio)
			return false;
		if (tipServizio == null) {
			if (other.tipServizio != null)
				return false;
		} else if (!tipServizio.equals(other.tipServizio))
			return false;
		return true;
	}

}
