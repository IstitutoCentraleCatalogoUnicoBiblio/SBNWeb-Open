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
package it.iccu.sbn.web.actionforms.servizi.occupazioni;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioOccupazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -2399000654146573699L;
	private boolean sessione = false;
	private boolean conferma = false;
	private String richiesta = null;
	private OccupazioneVO dettaglio = new OccupazioneVO("","","","");
	private List<ComboCodDescVO> lstProfessioni;

	private List selectedOccup;
	private int posizioneScorrimento;
	private int numOcc;


	//Istanza contenente i dati presenti sul DB.
	private OccupazioneVO datiSalvati = new OccupazioneVO();



	public List<ComboCodDescVO> getLstProfessioni() {
		return lstProfessioni;
	}
	public void setLstProfessioni(List<ComboCodDescVO> lstProfessioni) {
		this.lstProfessioni = lstProfessioni;
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
	public OccupazioneVO getDettaglio() {
		return dettaglio;
	}
	public void setDettaglio(OccupazioneVO dettaglio) {
		this.dettaglio = dettaglio;
	}
	public OccupazioneVO getDatiSalvati() {
		return datiSalvati;
	}
	public void setDatiSalvati(OccupazioneVO datiSalvati) {
		this.datiSalvati = datiSalvati;
	}
	public int getNumOcc() {
		return numOcc;
	}
	public void setNumOcc(int numOcc) {
		this.numOcc = numOcc;
	}
	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}
	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}
	public List getSelectedOccup() {
		return selectedOccup;
	}
	public void setSelectedOccup(List selectedOccup) {
		this.selectedOccup = selectedOccup;
	}

}
