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
package it.iccu.sbn.web.integration.actionforms.servizi.sale;

import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO.SlotSala;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.SlotVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.web.integration.actionforms.servizi.ServiziBaseForm;

import java.util.ArrayList;
import java.util.List;

public class GestionePrenotazionePostoForm extends ServiziBaseForm {


	private static final long serialVersionUID = 3836878988337198988L;

	public enum TipoGriglia {
		DETTAGLIO,
		MESE,
		SALA,
		GIORNO,
		PRENOTAZIONI_UTENTE;
	}

	public enum TipoOperazionePrenotazione {
		SALVA,
		RIFIUTA,
		SPOSTA;
	}

	private List<GiornoVO> giorni = new ArrayList<GiornoVO>();
	private String selectedDay;
	private Integer selectedSala;
	private String selectedFascia;
	private GiornoVO giorno;
	private SlotVO fascia;
	private List<MovimentoVO> movimenti = new ArrayList<MovimentoVO>();
	private String intestazione;
	private String tipoMateriale;
	private RicercaGrigliaCalendarioVO ricerca = new RicercaGrigliaCalendarioVO();

	private TipoGriglia tipo = TipoGriglia.MESE;

	private PrenotazionePostoVO prenotazione;
	private PrenotazionePostoVO prenotazioneOld;

	private int maxSlots;

	private ModalitaGestioneType modalita;

	private String descrCatMediazione;

	private TipoOperazionePrenotazione tipoOperazione;

	private List<PrenotazionePostoVO> prenotazioniUtente = new ArrayList<PrenotazionePostoVO>();
	private Integer selectedPren;

	private SlotSala sala;

	private Integer selectedUtente;

	public List<GiornoVO> getGiorni() {
		return giorni;
	}

	public void setGiorni(List<GiornoVO> giorni) {
		this.giorni = giorni;
	}

	public String getSelectedDay() {
		return selectedDay;
	}

	public void setSelectedDay(String dataSelezionata) {
		this.selectedDay = dataSelezionata;
	}

	public Integer getSelectedSala() {
		return selectedSala;
	}

	public void setSelectedSala(Integer selectedSala) {
		this.selectedSala = selectedSala;
	}

	public String getSelectedFascia() {
		return selectedFascia;
	}

	public void setSelectedFascia(String selectedFascia) {
		this.selectedFascia = selectedFascia;
	}

	public GiornoVO getGiorno() {
		return giorno;
	}

	public void setGiorno(GiornoVO giorno) {
		this.giorno = giorno;
	}

	public SlotVO getFascia() {
		return fascia;
	}

	public void setFascia(SlotVO fascia2) {
		this.fascia = fascia2;
	}

	public List<MovimentoVO> getMovimenti() {
		return movimenti;
	}

	public void setMovimenti(List<MovimentoVO> movimenti) {
		this.movimenti = movimenti;
	}

	public String getIntestazione() {
		return intestazione;
	}

	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}

	public TipoGriglia getTipo() {
		return tipo;
	}

	public void setTipo(TipoGriglia tipo) {
		this.tipo = tipo;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public RicercaGrigliaCalendarioVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RicercaGrigliaCalendarioVO ricerca) {
		this.ricerca = ricerca;
	}

	public PrenotazionePostoVO getPrenotazione() {
		return prenotazione;
	}

	public void setPrenotazione(PrenotazionePostoVO pp) {
		if (pp != null)
			pp = new PrenotazionePostoDecorator(pp);
		this.prenotazione = pp;
	}

	public PrenotazionePostoVO getPrenotazioneOld() {
		return prenotazioneOld;
	}

	public void setPrenotazioneOld(PrenotazionePostoVO prenotazioneOld) {
		this.prenotazioneOld = prenotazioneOld;
	}

	public int getMaxSlots() {
		return maxSlots;
	}

	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}

	public ModalitaGestioneType getModalita() {
		return modalita;
	}

	public void setModalita(ModalitaGestioneType modalita) {
		this.modalita = modalita;
	}

	public String getDescrCatMediazione() {
		return descrCatMediazione;
	}

	public void setDescrCatMediazione(String descrCatMediazione) {
		this.descrCatMediazione = descrCatMediazione;
	}

	public TipoOperazionePrenotazione getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazionePrenotazione tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public List<PrenotazionePostoVO> getPrenotazioniUtente() {
		return prenotazioniUtente;
	}

	public void setPrenotazioniUtente(List<PrenotazionePostoVO> prenotazioniUtente) {
		this.prenotazioniUtente = prenotazioniUtente;
	}

	public Integer getSelectedPren() {
		return selectedPren;
	}

	public void setSelectedPren(Integer selectedPren) {
		this.selectedPren = selectedPren;
	}

	public SlotSala getSala() {
		return sala;
	}

	public void setSala(SlotSala sala) {
		this.sala = sala;
	}

	public Integer getSelectedUtente() {
		return selectedUtente;
	}

	public void setSelectedUtente(Integer selectedUtente) {
		this.selectedUtente = selectedUtente;
	}

}
