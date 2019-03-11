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
package it.iccu.sbn.ejb.vo.servizi.segnature;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;

import java.util.List;

public class RangeSegnatureVO extends BaseVO {

	private static final long serialVersionUID = 751132923546528586L;

	public final static int SEGNATURA_LEN = 80;

	private boolean newSegnatura;
	private int id;
	private String codPolo;
	private String codBiblioteca;
	private int codSegnatura;
	private String segnInizio;
	private String segnFine;
	private String segnDa;
	private String segnA;
	private String codFruizione;
	private String fruizione;
	private String codIndisp;
	private String indisponibile;

	// gestisce i blocchi e l'ordinamento liste
	private String tipoOrdinamento;
	private int elemPerBlocchi;
	private List<ComboCodDescVO> lstTipiOrdinamento;

	public RangeSegnatureVO(int id, int codSegnatura, String segnInizio,
			String segnFine, String segnDa, String segnA, String codFruizione,
			String codIndisp) {
		super();
		this.id = id;
		this.codSegnatura = codSegnatura;
		this.segnInizio = trimAndSet(segnInizio);
		this.segnFine = trimAndSet(segnFine);
		this.segnDa = segnDa;
		this.segnA = segnA;
		this.codFruizione = codFruizione;
		this.codIndisp = codIndisp;
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public String getCodFruizione() {
		return codFruizione;
	}

	public void setCodFruizione(String codFruizione) {
		this.codFruizione = codFruizione;
	}

	public String getCodIndisp() {
		return codIndisp;
	}

	public void setCodIndisp(String codIndisp) {
		this.codIndisp = codIndisp;
	}

	public boolean isNewSegnatura() {
		return newSegnatura;
	}

	public void setNewSegnatura(boolean newSegnatura) {
		this.newSegnatura = newSegnatura;
	}

	public String getSegnA() {
		return segnA;
	}

	public String getSegnATrim() {
		return trimOrEmpty(segnA);
	}

	public void setSegnA(String segnA) {
		this.segnA = segnA;
	}

	public String getSegnDa() {
		return segnDa;
	}

	public String getSegnDaTrim() {
		return trimOrEmpty(segnDa);
	}

	public void setSegnDa(String segnDa) {
		this.segnDa = segnDa;
	}

	public String getSegnFine() {
		return segnFine;
	}

	public String getSegnFineTrim() {
		return trimOrEmpty(segnFine);
	}

	public void setSegnFine(String segnFine) {
		this.segnFine = trimAndSet(segnFine);
	}

	public String getSegnInizio() {
		return segnInizio;
	}

	public String getSegnInizioTrim() {
		return trimOrEmpty(segnInizio);
	}

	public void setSegnInizio(String segnInizio) {
		this.segnInizio = trimAndSet(segnInizio);
	}

	public int getCodSegnatura() {
		return codSegnatura;
	}

	public void setCodSegnatura(int codSegnatura) {
		this.codSegnatura = codSegnatura;
	}

	public void clear() {
		this.id = 0;
		this.codPolo = "";
		this.codBiblioteca = "";
		this.codSegnatura = 0;
		this.segnInizio = "";
		this.segnFine = "";
		this.segnDa = "";
		this.segnA = "";
		this.codFruizione = "";
		this.codIndisp = "";
	}

	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}

	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
	}

	public List<ComboCodDescVO> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(
			List<ComboCodDescVO> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getDataOraIns() {
		return DateUtil.formattaDataOra(this.getTsIns().getTime());
	}

	public String getDataOraAgg() {
		return DateUtil.formattaDataOra(this.getTsVar().getTime());
	}

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

	public String getFruizione() {
		return fruizione;
	}

	public void setFruizione(String fruizione) {
		this.fruizione = fruizione;
	}

	public String getIndisponibile() {
		return indisponibile;
	}

	public void setIndisponibile(String indisponibile) {
		this.indisponibile = indisponibile;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final RangeSegnatureVO other = (RangeSegnatureVO) obj;

		if (codBiblioteca == null) {
			if (other.codBiblioteca != null)
				return false;
		} else if (!codBiblioteca.equals(other.codBiblioteca))
			return false;

		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;

		if (segnInizio == null) {
			if (other.segnInizio != null)
				return false;
		} else if (!segnInizio.equals(other.segnInizio))
			return false;

		if (segnFine == null) {
			if (other.segnFine != null)
				return false;
		} else if (!segnFine.equals(other.segnFine))
			return false;

		if (segnDa == null) {
			if (other.segnDa != null)
				return false;
		} else if (!segnDa.equals(other.segnDa))
			return false;

		if (segnA == null) {
			if (other.segnA != null)
				return false;
		} else if (!segnA.equals(other.segnA))
			return false;

		if (codFruizione == null) {
			if (other.codFruizione != null)
				return false;
		} else if (!codFruizione.equals(other.codFruizione))
			return false;

		if (codIndisp == null) {
			if (other.codIndisp != null)
				return false;
		} else if (!codIndisp.equals(other.codIndisp))
			return false;

		if (codSegnatura != other.codSegnatura)
			return false;

		return true;
	}
}
