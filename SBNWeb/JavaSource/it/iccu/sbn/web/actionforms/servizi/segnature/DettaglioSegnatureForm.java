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
package it.iccu.sbn.web.actionforms.servizi.segnature;

import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioSegnatureForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 4151704934453507978L;
	private boolean conferma = false;
	private String richiesta = null;
	private boolean sessione = false;
	private RangeSegnatureVO dettSegn = new RangeSegnatureVO(0,0,"","","","","","");
	private RangeSegnatureVO dettSegnSalvato = new RangeSegnatureVO(0,0,"","","","","","");
	private List lstFruizioni;
	private List lstIndisp;
	private List selectedSegnature;
	private int posizioneScorrimento;
	private int numSegn;



	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List getLstFruizioni() {
		return lstFruizioni;
	}
	public void setLstFruizioni(List lstFruizioni) {
		this.lstFruizioni = lstFruizioni;
	}
	public List getLstIndisp() {
		return lstIndisp;
	}
	public void setLstIndisp(List lstIndisp) {
		this.lstIndisp = lstIndisp;
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
	public RangeSegnatureVO getDettSegn() {
		return dettSegn;
	}
	public void setDettSegn(RangeSegnatureVO dettSegn) {
		this.dettSegn = dettSegn;
	}
	public RangeSegnatureVO getDettSegnSalvato() {
		return dettSegnSalvato;
	}
	public void setDettSegnSalvato(RangeSegnatureVO dettSegnSalvato) {
		this.dettSegnSalvato = dettSegnSalvato;
	}
	public int getNumSegn() {
		return numSegn;
	}
	public void setNumSegn(int numSegn) {
		this.numSegn = numSegn;
	}
	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}
	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}
	public List getSelectedSegnature() {
		return selectedSegnature;
	}
	public void setSelectedSegnature(List selectedSegnature) {
		this.selectedSegnature = selectedSegnature;
	}
}
