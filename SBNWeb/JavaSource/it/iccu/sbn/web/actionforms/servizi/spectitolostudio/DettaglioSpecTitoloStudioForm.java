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
package it.iccu.sbn.web.actionforms.servizi.spectitolostudio;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioSpecTitoloStudioForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -7230901301635834471L;
	private boolean conferma = false;
	private String richiesta = null;
	private boolean sessione = false;
	private List<ComboCodDescVO> listaTdS;
	private SpecTitoloStudioVO anaSpecialita = new SpecTitoloStudioVO();
	private List selectedSpecTit;
	private int posizioneScorrimento;
	private int numSpecTit;

	/**
	 * Istanza contenente i dati salvati sul DB
	 */
	private SpecTitoloStudioVO datiSalvati = new SpecTitoloStudioVO();





	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public SpecTitoloStudioVO getAnaSpecialita() {
		return anaSpecialita;
	}

	public void setAnaSpecialita(SpecTitoloStudioVO anaSpecialita) {
		this.anaSpecialita = anaSpecialita;
	}

	public List<ComboCodDescVO> getListaTdS() {
		return listaTdS;
	}

	public void setListaTdS(List<ComboCodDescVO> listaTdS) {
		this.listaTdS = listaTdS;
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

	public SpecTitoloStudioVO getDatiSalvati() {
		return datiSalvati;
	}

	public void setDatiSalvati(SpecTitoloStudioVO datiSalvati) {
		this.datiSalvati = datiSalvati;
	}

	public int getNumSpecTit() {
		return numSpecTit;
	}

	public void setNumSpecTit(int numSpecTit) {
		this.numSpecTit = numSpecTit;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public List getSelectedSpecTit() {
		return selectedSpecTit;
	}

	public void setSelectedSpecTit(List selectedSpecTit) {
		this.selectedSpecTit = selectedSpecTit;
	}

}
