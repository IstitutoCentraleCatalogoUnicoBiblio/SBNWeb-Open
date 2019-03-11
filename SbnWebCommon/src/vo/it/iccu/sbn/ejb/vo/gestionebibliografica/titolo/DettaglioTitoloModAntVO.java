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
//	SBNWeb - Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
// viene esteso anche al Materiale Moderno e Antico

package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;

public class DettaglioTitoloModAntVO extends BaseVO {

	// = DettaglioTitoloMusicaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -2943957145298790497L;


	private String genereRappr;
	private String annoIRappr;
	private String periodoIRappr;
	private String localitaIRappr;
	private String sedeIRappr;
	private String occasioneIRappr;
	private String noteIRappr;

	private String presenzaPersonaggi;
	private List<TabellaNumSTDImpronteVO> listaPersonaggi;

	// Lista degli interpreti nuovi da inserire in Polo prima di effettuare la variazione descrizione
	private List listaInterpreti;

	public DettaglioTitoloModAntVO() {
		super();
		// TODO Auto-generated constructor stub
		this.listaPersonaggi = new ArrayList<TabellaNumSTDImpronteVO>();

	}

	public List getListaPersonaggi() {
		return listaPersonaggi;
	}

	public void setListaPersonaggi(List listaPersonaggi) {
		this.listaPersonaggi = listaPersonaggi;
	}

	public String getPresenzaPersonaggi() {
		return presenzaPersonaggi;
	}

	public void setPresenzaPersonaggi(String presenzaPersonaggi) {
		this.presenzaPersonaggi = presenzaPersonaggi;
	}



	public String getGenereRappr() {
		return genereRappr;
	}

	public void setGenereRappr(String genereRappr) {
		this.genereRappr = genereRappr;
	}

	public String getAnnoIRappr() {
		return annoIRappr;
	}

	public void setAnnoIRappr(String annoIRappr) {
		this.annoIRappr = annoIRappr;
	}

	public String getLocalitaIRappr() {
		return localitaIRappr;
	}

	public void setLocalitaIRappr(String localitaIRappr) {
		this.localitaIRappr = localitaIRappr;
	}

	public String getNoteIRappr() {
		return noteIRappr;
	}

	public void setNoteIRappr(String noteIRappr) {
		this.noteIRappr = noteIRappr;
	}

	public String getOccasioneIRappr() {
		return occasioneIRappr;
	}

	public void setOccasioneIRappr(String occasioneIRappr) {
		this.occasioneIRappr = occasioneIRappr;
	}

	public String getPeriodoIRappr() {
		return periodoIRappr;
	}

	public void setPeriodoIRappr(String periodoIRappr) {
		this.periodoIRappr = periodoIRappr;
	}

	public String getSedeIRappr() {
		return sedeIRappr;
	}

	public void setSedeIRappr(String sedeIRappr) {
		this.sedeIRappr = sedeIRappr;
	}

	public List addListaPersonaggi(TabellaNumSTDImpronteVO tabPer) {
		listaPersonaggi.add(tabPer);
		return listaPersonaggi;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloModAntVO other = (DettaglioTitoloModAntVO) obj;
		if (annoIRappr == null) {
			if (other.annoIRappr != null)
				return false;
		} else if (!annoIRappr.equals(other.annoIRappr))
			return false;
		if (genereRappr == null) {
			if (other.genereRappr != null)
				return false;
		} else if (!genereRappr.equals(other.genereRappr))
			return false;
		if (localitaIRappr == null) {
			if (other.localitaIRappr != null)
				return false;
		} else if (!localitaIRappr.equals(other.localitaIRappr))
			return false;
		if (noteIRappr == null) {
			if (other.noteIRappr != null)
				return false;
		} else if (!noteIRappr.equals(other.noteIRappr))
			return false;
		if (occasioneIRappr == null) {
			if (other.occasioneIRappr != null)
				return false;
		} else if (!occasioneIRappr.equals(other.occasioneIRappr))
			return false;
		if (periodoIRappr == null) {
			if (other.periodoIRappr != null)
				return false;
		} else if (!periodoIRappr.equals(other.periodoIRappr))
			return false;

		if (presenzaPersonaggi == null) {
			if (other.presenzaPersonaggi != null)
				return false;
		} else if (!presenzaPersonaggi.equals(other.presenzaPersonaggi))
			return false;
		if (sedeIRappr == null) {
			if (other.sedeIRappr != null)
				return false;
		} else if (!sedeIRappr.equals(other.sedeIRappr))
			return false;


		if (!listEquals(this.listaPersonaggi, other.listaPersonaggi,
				TabellaNumSTDImpronteVO.class))
			return false;

		return true;
	}

	public List getListaInterpreti() {
		return listaInterpreti;
	}

	public void setListaInterpreti(List listaInterpreti) {
		this.listaInterpreti = listaInterpreti;
	}

}
