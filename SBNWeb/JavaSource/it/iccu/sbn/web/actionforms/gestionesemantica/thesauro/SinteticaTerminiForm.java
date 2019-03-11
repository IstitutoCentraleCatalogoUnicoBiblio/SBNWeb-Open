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
package it.iccu.sbn.web.actionforms.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaTitoliCollegatiVO;

public class SinteticaTerminiForm extends AbstractSinteticaThesauriForm {


	private static final long serialVersionUID = -8979879102102174885L;
	public static final String SUBMIT_TITOLI_COLLEGATI = "tit.coll.btn";
	public static final String SUBMIT_TERMINI_COLLEGATI = "did.coll.btn";

	AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private ThRicercaComuneVO ricercaComune = new ThRicercaComuneVO();
	private ThRicercaTitoliCollegatiVO ricercaThesauroDescrittore = new ThRicercaTitoliCollegatiVO();
	private FolderType folder;
	private boolean noTutti = false;
	private boolean initialized = false;
	private RicercaThesauroListaVO listaTermini;
	private ParametriThesauro parametri;
	private String didScelto;

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public ThRicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(ThRicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public ThRicercaTitoliCollegatiVO getRicercaThesauroDescrittore() {
		return ricercaThesauroDescrittore;
	}

	public void setRicercaThesauroDescrittore(
			ThRicercaTitoliCollegatiVO ricercaThesauroDescrittore) {
		this.ricercaThesauroDescrittore = ricercaThesauroDescrittore;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder) {
		this.folder = folder;
	}

	public boolean isNoTutti() {
		return noTutti;
	}

	public void setNoTutti(boolean noTutti) {
		this.noTutti = noTutti;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public RicercaThesauroListaVO getListaTermini() {
		return listaTermini;
	}

	public void setListaTermini(RicercaThesauroListaVO listaTermini) {
		this.listaTermini = listaTermini;
	}

	public String getDidScelto() {
		return didScelto;
	}

	public void setDidScelto(String didScelto) {
		this.didScelto = didScelto;
	}

	public String getSUBMIT_TITOLI_COLLEGATI() {
		return SUBMIT_TITOLI_COLLEGATI;
	}

	public String getSUBMIT_TERMINI_COLLEGATI() {
		return SUBMIT_TERMINI_COLLEGATI;
	}

}
