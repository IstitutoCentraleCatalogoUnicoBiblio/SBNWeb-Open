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
package it.iccu.sbn.web.actionforms.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioAutorizzazioniForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -8710179939356722920L;
	// Costanti per settare l'attributo "richiesta"
	public static String NUOVA_AUT = "Nuovo";
	public static String AGGIORNA_AUT = "Aggiorna";
	public static String CANCELLA_AUT = "Cancella";

	// salva la sessione
	private boolean sessione = false;
	// gestisce la pagina di conferma Si/No
	private boolean conferma = false;
	private String richiesta = null;
	private AutorizzazioneVO autAna = new AutorizzazioneVO("", "", "", "");
	private List elencoAutomaticoX;
	private RicercaAutorizzazioneVO autRicerca = new RicercaAutorizzazioneVO();
	private AutorizzazioneVO autAnaOLD = new AutorizzazioneVO("", "", "", "");
	private List selectedAutorizzazioni;
	private int posizioneScorrimento;
	private int numAutorizzazioni;

	public AutorizzazioneVO getAutAnaOLD() {
		return autAnaOLD;
	}

	public void setAutAnaOLD(AutorizzazioneVO autAnaOLD) {
		this.autAnaOLD = autAnaOLD;
	}

	public AutorizzazioneVO getAutAna() {
		return autAna;
	}

	public void setAutAna(AutorizzazioneVO autAna) {
		this.autAna = autAna;
	}

	public String getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}

	public List getElencoAutomaticoX() {
		return elencoAutomaticoX;
	}

	public void setElencoAutomaticoX(List elencoAutomaticoX) {
		this.elencoAutomaticoX = elencoAutomaticoX;
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

	public RicercaAutorizzazioneVO getAutRicerca() {
		return autRicerca;
	}

	public void setAutRicerca(RicercaAutorizzazioneVO autRicerca) {
		this.autRicerca = autRicerca;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public int getNumAutorizzazioni() {
		return numAutorizzazioni;
	}

	public void setNumAutorizzazioni(int numAutorizzazioni) {
		this.numAutorizzazioni = numAutorizzazioni;
	}

	public List getSelectedAutorizzazioni() {
		return selectedAutorizzazioni;
	}

	public void setSelectedAutorizzazioni(List selectedAutorizzazioni) {
		this.selectedAutorizzazioni = selectedAutorizzazioni;
	}
}
