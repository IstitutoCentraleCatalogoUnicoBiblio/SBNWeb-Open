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
package it.iccu.sbn.ejb.vo.servizi.autorizzazioni;//

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;

import java.util.ArrayList;
import java.util.List;

public class AutorizzazioneVO extends BaseVO {

	private static final long serialVersionUID = 140853479267669924L;

	public static boolean NEW = true;
	public static boolean OLD = false;

	private boolean NuovaAut;
	private int idAutorizzazione;

	private int progressivo;
	private String codPolo;
	private String codBiblioteca;
	private String codAutorizzazione;
	private String desAutorizzazione;
	private String automaticoPer;
	private List<ElementoSinteticaServizioVO> elencoServizi = new ArrayList<ElementoSinteticaServizioVO>();

	private List<ComboCodDescVO> lstTipiOrdinamento;

	private String fl_svolg = "L";

	public AutorizzazioneVO() {
		super();
	}

	public AutorizzazioneVO(String codAutorizzazione,
			String desAutorizzazione) {
		this.codAutorizzazione = trimAndSet(codAutorizzazione);
		this.desAutorizzazione = trimAndSet(desAutorizzazione);
	}

	public AutorizzazioneVO(String codBiblioteca, String codAutorizzazione,
			String desAutorizzazione, String automaticoPer) {
		super();

		this.codBiblioteca = codBiblioteca;
		this.codAutorizzazione = trimAndSet(codAutorizzazione);
		this.desAutorizzazione = trimAndSet(desAutorizzazione);
		this.automaticoPer = trimAndSet(automaticoPer);
	}

	public void clearAut() {
		//this.codBiblioteca = "";
		this.codAutorizzazione = "";
		this.desAutorizzazione = "";
		this.automaticoPer = "";
	    this.elencoServizi.clear();
	}

	public String getAutomaticoPer() {
		return automaticoPer;
	}

	public void setAutomaticoPer(String automaticoPer) {
		this.automaticoPer = trimAndSet(automaticoPer);
	}

	public String getCodAutorizzazione() {
		return codAutorizzazione;
	}

	public void setCodAutorizzazione(String codAutorizzazione) {
		this.codAutorizzazione = trimAndSet(codAutorizzazione);
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public String getDesAutorizzazione() {
		return desAutorizzazione;
	}

	public void setDesAutorizzazione(String desAutorizzazione) {
		this.desAutorizzazione = trimAndSet(desAutorizzazione);
	}

	public List<ElementoSinteticaServizioVO> getElencoServizi() {
		return elencoServizi;
	}

	public boolean isNuovaAut() {
		return NuovaAut;
	}

	public void setNuovaAut(boolean nuovaAut) {
		NuovaAut = nuovaAut;
	}

	public void setListaServizi(List<ElementoSinteticaServizioVO> elencoServizi) {
		this.elencoServizi = elencoServizi;
	}

	public List<ElementoSinteticaServizioVO> getListaServizi() {
		return elencoServizi;
	}

	public ElementoSinteticaServizioVO getListaServizi(int index) {
		// automatically grow List size
		while (index >= this.elencoServizi.size()) {
			this.elencoServizi.add(new ElementoSinteticaServizioVO());
		}
		return this.elencoServizi.get(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AutorizzazioneVO other = (AutorizzazioneVO) obj;
		if (automaticoPer == null) {
			if (other.automaticoPer != null)
				return false;
		} else if (!automaticoPer.trim().equals(other.automaticoPer.trim()))
			return false;
		if (codAutorizzazione == null) {
			if (other.codAutorizzazione != null)
				return false;
		} else if (!codAutorizzazione.trim().toUpperCase().equals(other.codAutorizzazione.trim().toUpperCase()))
			return false;
		if (codBiblioteca == null) {
			if (other.codBiblioteca != null)
				return false;
		} else if (!codBiblioteca.equals(other.codBiblioteca))
			return false;
		if (desAutorizzazione == null) {
			if (other.desAutorizzazione != null)
				return false;
		} else if (!desAutorizzazione.equals(other.desAutorizzazione))
			return false;
		if (NuovaAut != other.NuovaAut)
			return false;

		if (elencoServizi == null) {
			if (other.elencoServizi != null)
				return false;
		} else if (!listEquals(this.elencoServizi, other.elencoServizi, ElementoSinteticaServizioVO.class))
			return false;
		return true;
	}

//	public int getElemPerBlocchi() {
//		return elemPerBlocchi;
//	}
//
//	public void setElemPerBlocchi(int elemPerBlocchi) {
//		this.elemPerBlocchi = elemPerBlocchi;
//	}

	public List<ComboCodDescVO> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List<ComboCodDescVO> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public int getIdAutorizzazione() {
		return idAutorizzazione;
	}

	public void setIdAutorizzazione(int idAutorizzazione) {
		this.idAutorizzazione = idAutorizzazione;
	}

	public String getFl_svolg() {
		return fl_svolg;
	}

	public void setFl_svolg(String string) {
		this.fl_svolg = string;
	}

	public boolean isILL() {
		return ValidazioneDati.equals(fl_svolg, "I");
	}

	public boolean isLocale() {
		return ValidazioneDati.equals(fl_svolg, "L");
	}

}
