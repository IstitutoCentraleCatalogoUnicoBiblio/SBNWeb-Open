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
package it.iccu.sbn.web.actionforms.servizi.configurazione;

import it.iccu.sbn.ejb.vo.common.AnagrafeUtenteProfessionaleVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class AttivitaBibliotecarioForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 6751033748711857979L;
	private boolean sessione;
	private TipoServizioVO tipoServizio = new TipoServizioVO();
	private String codiceAttivita;
	private String descrizioneAttivita;

	/**
	 * Mappa dei bibliotecari censiti.<br/>
	 * La chiave della mappa è composta da:<br/>
	 * cognome+"_"+nome+"_"+"id_utente_professionale"<br/>
	 * <br/>
	 *
	 * I valori della mappa sono istanze della classe
	 * AnagrafeUtenteProfessionaleVO<br/>
	 * che contiene gli attributi che compomngono la chiave della mappa stessa.
	 */
	private TreeMap<String, AnagrafeUtenteProfessionaleVO> bibliotecari = new TreeMap<String, AnagrafeUtenteProfessionaleVO>();
	/**
	 * Contiene le key degli elementi della mappa <b>bibliotecari</b>
	 * selezionati
	 *
	 */
	private String[] bibliotecariSelected;

	/**
	 * Mappa dei bibliotecari associati all'attività corrente La chiave della
	 * mappa è composta da:<br/>
	 * cognome+"_"+nome+"_"+"id_utente_professionale"<br/>
	 * <br/>
	 *
	 * I valori della mappa sono istanze della classe
	 * AnagrafeUtenteProfessionaleVO<br/>
	 * che contiene gli attributi che compomngono la chiave della mappa stessa.
	 */
	private TreeMap<String, AnagrafeUtenteProfessionaleVO> attivita_bibliotecari = new TreeMap<String, AnagrafeUtenteProfessionaleVO>();
	/**
	 * Contiene le key degli elementi della mappa <b>attivita_bibliotecari</b>
	 * selezionati
	 *
	 */
	private String[] attivita_bibliotecariSelected;

	/**
	 * Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di
	 * aggiornamento per stabilire se ci sono delle modifiche da salvare o meno,
	 * per evitare di effettuare un aggiornamento inutile con valori uguali a
	 * quelli precedentemente inseriti.
	 */
	private TreeMap<String, AnagrafeUtenteProfessionaleVO> ultimo_attivita_bibliotecari = new TreeMap<String, AnagrafeUtenteProfessionaleVO>();

	/**
	 * Contiene le chiavi dei bibliotecari che erano associati all'attività ma
	 * che si è deciso di cancellare
	 */
	private List<String> chiaviBibliotecariDaRimuovere = new ArrayList<String>();

	/**
	 * Contiene le chiavi dei nuovi bibliotecari associati
	 */
	private List<String> chiaviBibliotecariDaAggiungere = new ArrayList<String>();

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.bibliotecariSelected = new String[] {};
		this.attivita_bibliotecariSelected = new String[] {};
	}

	public String getDescrizioneAttivita() {
		return descrizioneAttivita;
	}

	public void setDescrizioneAttivita(String descrizioneAttivita) {
		this.descrizioneAttivita = descrizioneAttivita;
	}

	public TreeMap<String, AnagrafeUtenteProfessionaleVO> getBibliotecari() {
		return bibliotecari;
	}

	public void setBibliotecari(
			TreeMap<String, AnagrafeUtenteProfessionaleVO> bibliotecari) {
		this.bibliotecari = bibliotecari;
	}

	public String[] getBibliotecariSelected() {
		return bibliotecariSelected;
	}

	public void setBibliotecariSelected(String[] bibliotecariSelected) {
		this.bibliotecariSelected = bibliotecariSelected;
	}

	public TreeMap<String, AnagrafeUtenteProfessionaleVO> getAttivita_bibliotecari() {
		return attivita_bibliotecari;
	}

	public void setAttivita_bibliotecari(
			TreeMap<String, AnagrafeUtenteProfessionaleVO> attivita_bibliotecari) {
		this.attivita_bibliotecari = attivita_bibliotecari;
	}

	public String[] getAttivita_bibliotecariSelected() {
		return attivita_bibliotecariSelected;
	}

	public void setAttivita_bibliotecariSelected(
			String[] attivita_bibliotecariSelected) {
		this.attivita_bibliotecariSelected = attivita_bibliotecariSelected;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public TipoServizioVO getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(TipoServizioVO tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public String getCodiceAttivita() {
		return codiceAttivita;
	}

	public void setCodiceAttivita(String codiceAttivita) {
		this.codiceAttivita = codiceAttivita;
	}

	public TreeMap<String, AnagrafeUtenteProfessionaleVO> getUltimo_attivita_bibliotecari() {
		return ultimo_attivita_bibliotecari;
	}

	public void setUltimo_attivita_bibliotecari(
			TreeMap<String, AnagrafeUtenteProfessionaleVO> ultimo_attivita_bibliotecari) {
		this.ultimo_attivita_bibliotecari = ultimo_attivita_bibliotecari;
	}

	public List<String> getChiaviBibliotecariDaRimuovere() {
		return chiaviBibliotecariDaRimuovere;
	}

	public void setChiaviBibliotecariDaRimuovere(
			List<String> chiaviBibliotecariDaRimuovere) {
		this.chiaviBibliotecariDaRimuovere = chiaviBibliotecariDaRimuovere;
	}

	public List<String> getChiaviBibliotecariDaAggiungere() {
		return chiaviBibliotecariDaAggiungere;
	}

	public void setChiaviBibliotecariDaAggiungere(
			List<String> chiaviBibliotecariDaAggiungere) {
		this.chiaviBibliotecariDaAggiungere = chiaviBibliotecariDaAggiungere;
	}

}
