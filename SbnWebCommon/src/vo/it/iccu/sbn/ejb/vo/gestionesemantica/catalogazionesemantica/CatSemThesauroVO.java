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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CatSemThesauroVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 3495781489020808826L;

	private String codThesauro;
	private String descThesauro;
	private CampiThesauroVO dettaglioThesauro;
	private List<ElementoSinteticaThesauroVO> listaTermini = new ArrayList<ElementoSinteticaThesauroVO>();
	private String[] didMultiSelez;
	private boolean initialized;
	private ParametriThesauro parametri;
	private HashSet<Integer> blocchiCaricati = new HashSet<Integer>();
	private String codSelezionato;

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public HashSet<Integer> getBlocchiCaricati() {
		return blocchiCaricati;
	}

	public void setBlocchiCaricati(HashSet<Integer> blocchiCaricati) {
		this.blocchiCaricati = blocchiCaricati;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String[] getDidMultiSelez() {
		return didMultiSelez;
	}

	public void setDidMultiSelez(String[] didMultiSelez) {
		this.didMultiSelez = didMultiSelez;
	}

	public List<ElementoSinteticaThesauroVO> getListaTermini() {
		return listaTermini;
	}

	public void setListaTermini(List<ElementoSinteticaThesauroVO> listaTermini) {
		this.listaTermini = listaTermini;
	}

	public CatSemThesauroVO() {
		super();
	}

	public CatSemThesauroVO(String codThesauro, String descThesauro,
			CampiThesauroVO dettaglioThesauro) {
		this.codThesauro = codThesauro;
		this.descThesauro = descThesauro;
		this.dettaglioThesauro = dettaglioThesauro;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public String getDescThesauro() {
		return descThesauro;
	}

	public void setDescThesauro(String descThesauro) {
		this.descThesauro = descThesauro;
	}

	public CampiThesauroVO getDettaglioThesauro() {
		return dettaglioThesauro;
	}

	public void setDettaglioThesauro(CampiThesauroVO dettaglioThesauro) {
		this.dettaglioThesauro = dettaglioThesauro;
	}

	public int getTotRighe() {
		int totRighe = super.getTotRighe();
		return totRighe != 0 ? totRighe : listaTermini.size();
	}



}
