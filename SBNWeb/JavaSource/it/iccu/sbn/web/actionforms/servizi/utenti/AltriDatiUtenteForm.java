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
package it.iccu.sbn.web.actionforms.servizi.utenti;

import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class AltriDatiUtenteForm extends AbstractBibliotecaForm {
/**
	 * 
	 */
	private static final long serialVersionUID = -919170343142886732L;
	// salva la sessione
	private boolean sessione = false;
// anagrafe
	private UtenteBibliotecaVO uteAna = new UtenteBibliotecaVO();
	private List professioni;
	private List occupazioni;
	private List ateneo;
	private List tipoPersonalita;
	private List specTitoloStudio;
	private List titoloStudio;
	private String[] codMateria;
	private List allMaterie;

// bibliopolo

// documenti
	private List elencoDocumenti;
// fine
	public UtenteBibliotecaVO getUteAna() {
		return uteAna;
	}
	public void setUteAna(UtenteBibliotecaVO uteAna) {
		this.uteAna = uteAna;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List getOccupazioni() {
		return occupazioni;
	}
	public void setOccupazioni(List occupazioni) {
		this.occupazioni = occupazioni;
	}
//	public List getProfessioni() {
//		return professioni;
//	}
//	public void setProfessioni(List professioni) {
//		this.professioni = professioni;
//	}
	public List getSpecTitoloStudio() {
		return specTitoloStudio;
	}
	public void setSpecTitoloStudio(List specTitoloStudio) {
		this.specTitoloStudio = specTitoloStudio;
	}
	public List getTipoPersonalita() {
		return tipoPersonalita;
	}
	public void setTipoPersonalita(List tipoPersonalita) {
		this.tipoPersonalita = tipoPersonalita;
	}
//	public List getTitoloStudio() {
//		return titoloStudio;
//	}
//	public void setTitoloStudio(List titoloStudio) {
//		this.titoloStudio = titoloStudio;
//	}
	public List getAteneo() {
		return ateneo;
	}
	public void setAteneo(List ateneo) {
		this.ateneo = ateneo;
	}
	public String[] getCodMateria() {
		return codMateria;
	}
	public void setCodMateria(String[] codMateria) {
		this.codMateria = codMateria;
	}
	public List getElencoDocumenti() {
		return elencoDocumenti;
	}
	public void setElencoDocumenti(List elencoDocumenti) {
		this.elencoDocumenti = elencoDocumenti;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		List<MateriaVO> materie = this.getUteAna().getProfessione().getMaterie();
		//if (materie == null) return;
		if (materie == null || !this.sessione) return;
		// rersetto i selezionati tranne i caricati già fleggati
		for (MateriaVO m: materie) {
				m.setSelezionato("");
		}
	}
	public List getAllMaterie() {
		return allMaterie;
	}
	public void setAllMaterie(List allMaterie) {
		this.allMaterie = allMaterie;
	}
	public List getProfessioni() {
		return professioni;
	}
	public void setProfessioni(List professioni) {
		this.professioni = professioni;
	}
	public List getTitoloStudio() {
		return titoloStudio;
	}
	public void setTitoloStudio(List titoloStudio) {
		this.titoloStudio = titoloStudio;
	}
}
