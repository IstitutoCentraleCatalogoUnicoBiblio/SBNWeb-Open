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
package it.iccu.sbn.web.actionforms.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;

public class ListaClassiTermineForm extends ActionForm {


	private static final long serialVersionUID = 6988643274620931540L;
	private boolean initialized;
	private boolean conferma;
	private Integer selected;
	private int bloccoSelezionato;
	private Set<Integer> blocchiCaricati = new HashSet<Integer>();
	private OggettoRiferimentoVO oggettoRiferimento = new OggettoRiferimentoVO();
	private ParametriThesauro parametri = new ParametriThesauro();
	private ClassiCollegateTermineVO classi;
	private TreeElementViewSoggetti analitica;
	private DettaglioTermineThesauroVO dettaglio;

	private List<ComboCodDescVO> comboGestioneLegame = new ArrayList<ComboCodDescVO>();
	private String idFunzioneLegame;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public Set<Integer> getBlocchiCaricati() {
		return blocchiCaricati;
	}

	public void setBlocchiCaricati(Set<Integer> blocchiCaricati) {
		this.blocchiCaricati = blocchiCaricati;
	}

	public OggettoRiferimentoVO getOggettoRiferimento() {
		return oggettoRiferimento;
	}

	public void setOggettoRiferimento(OggettoRiferimentoVO oggettoRiferimento) {
		this.oggettoRiferimento = oggettoRiferimento;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public TreeElementViewSoggetti getAnalitica() {
		return analitica;
	}

	public void setAnalitica(TreeElementViewSoggetti analitica) {
		this.analitica = analitica;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public ClassiCollegateTermineVO getClassi() {
		return classi;
	}

	public void setClassi(ClassiCollegateTermineVO classi) {
		this.classi = classi;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getIdFunzioneLegame() {
		return idFunzioneLegame;
	}

	public void setIdFunzioneLegame(String idFunzioneLegame) {
		this.idFunzioneLegame = idFunzioneLegame;
	}

	public List<ComboCodDescVO> getComboGestioneLegame() {
		return comboGestioneLegame;
	}

	public void setComboGestioneLegame(List<ComboCodDescVO> comboGestioneLegame) {
		this.comboGestioneLegame = comboGestioneLegame;
	}

	public DettaglioTermineThesauroVO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(DettaglioTermineThesauroVO dettaglio) {
		this.dettaglio = dettaglio;
	}

}
