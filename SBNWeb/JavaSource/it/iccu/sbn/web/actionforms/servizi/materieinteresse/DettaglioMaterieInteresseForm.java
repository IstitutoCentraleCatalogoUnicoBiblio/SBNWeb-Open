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
package it.iccu.sbn.web.actionforms.servizi.materieinteresse;

import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioMaterieInteresseForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -4483274718445755825L;
	private boolean sessione = false;
	private boolean conferma = false;
	private boolean nuovo    = false;
	private String richiesta = null;
	private List selectedMatInt;
	private int posizioneScorrimento;
	private int numMat;


	/**
	 * dettagli della materia visualizzati
	 */
	private MateriaVO anaMateria = new MateriaVO();

	/**
	 * Contiene i dati che sono salvati sul db. Nel caso di richiesta di aggiornamento
	 * é confrontata con anaMateria per stabilire se effettivamente ci sono modifiche da salvare o meno
	 */
	private MateriaVO materiaSalvata = new MateriaVO();


	public MateriaVO getAnaMateria() {
		return anaMateria;
	}
	public void setAnaMateria(MateriaVO anaMateria) {
		this.anaMateria = anaMateria;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public boolean isNuovo() {
		return nuovo;
	}
	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}
	public MateriaVO getMateriaSalvata() {
		return materiaSalvata;
	}
	public void setMateriaSalvata(MateriaVO materiaSalvata) {
		this.materiaSalvata = materiaSalvata;
	}
	public int getNumMat() {
		return numMat;
	}
	public void setNumMat(int numMat) {
		this.numMat = numMat;
	}
	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}
	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}
	public List getSelectedMatInt() {
		return selectedMatInt;
	}
	public void setSelectedMatInt(List selectedMatInt) {
		this.selectedMatInt = selectedMatInt;
	}

}
