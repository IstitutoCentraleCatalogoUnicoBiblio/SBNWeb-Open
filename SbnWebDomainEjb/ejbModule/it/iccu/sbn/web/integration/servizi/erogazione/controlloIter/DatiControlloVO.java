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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatiControlloVO extends SerializableVO {

	private static final long serialVersionUID = 5611975596410380225L;

	public enum OperatoreType {
		BIBLIOTECARIO,
		UTENTE,
		SIP2;
	}

	/**
	 * dati del movimento
	 */
	private MovimentoVO movimento;

	/**
	 * Dati del servizio della richiesta
	 */
	private ServizioBibliotecaVO servizio;

	/**
	 * Dati del tipo servizio
	 */
	private TipoServizioVO tipoServizio;

	/**
	 * Settato dalla classe che implementa il controllo per dire al chiamante
	 * quale controllo è stato eseguito
	 */
	private StatoIterRichiesta controlloEseguito;

	/**
	 * giorni di sospensione calcolati dal controllo RESTITUZIONE_DOCUMENTO
	 */
	private Long ggSospensione;

	/**
	 * giorni di ritardo calcolati dal controllo RESTITUZIONE_DOCUMENTO
	 */
	private Long ggRitardo;

	/**
	 * data fino alla quale all'utente sarà sospenso il servizio
	 */
	private Date dataSospensione;

	/**
	 * lista di codici relativi a messaggi che il controllo vuole mandare al
	 * chiamante. Si tratta di ulteriori messaggi, oltre a quello censito nella
	 * tabella dei controlli, che devono essere presenti nel file di properties
	 * relativo ai messaggi. Nel caso dei servizi il ServiziMessages.properties
	 */
	private List<String> codiciMsgSupplementari = new ArrayList<String>();

	private final String ticket;
	private Object checkData;
	private ControlloAttivitaServizioResult result = ControlloAttivitaServizioResult.OK;
	private boolean inoltroPrenotazione;
	private OperatoreType operatore;
	private String catMediazioneDigit;
	private String codNoDisp;
	private String dataRedisp;
	private ControlloAttivitaServizioResult previousResult = ControlloAttivitaServizioResult.OK;

	private MessaggioVO ultimoMessaggio;
	private MovimentoVO movimentoAggiornato;

	public DatiControlloVO(String ticket, MovimentoVO mov, OperatoreType operatore, boolean inoltroPrenotazione,
			ControlloAttivitaServizioResult previousResult) {
		super();
		this.movimento = mov;
		this.operatore = operatore;
		this.inoltroPrenotazione = inoltroPrenotazione;
		this.ggSospensione = 0L;
		this.ggRitardo = 0L;
		this.dataSospensione = null;
		this.ticket = ticket;
		this.previousResult = previousResult;

		this.movimentoAggiornato = mov;
	}

	public MovimentoVO getMovimento() {
		return movimento;
	}

	public DatiControlloVO setMovimento(MovimentoVO movimento) {
		this.movimento = movimento;
		return this;
	}

	public Long getGgSospensione() {
		return ggSospensione;
	}

	public void setGgSospensione(Long ggSospensione) {
		this.ggSospensione = ggSospensione;
	}

	public Long getGgRitardo() {
		return ggRitardo;
	}

	public void setGgRitardo(Long ggRitardo) {
		this.ggRitardo = ggRitardo;
	}

	public StatoIterRichiesta getControlloEseguito() {
		return controlloEseguito;
	}

	public DatiControlloVO setControlloEseguito(StatoIterRichiesta controlloEseguito) {
		this.controlloEseguito = controlloEseguito;
		return this;
	}

	public Date getDataSospensione() {
		return dataSospensione;
	}

	public void setDataSospensione(Date dataSospensione) {
		this.dataSospensione = dataSospensione;
	}

	public ServizioBibliotecaVO getServizio() {
		return servizio;
	}

	public void setServizio(ServizioBibliotecaVO servizio) {
		this.servizio = servizio;
	}

	public TipoServizioVO getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(TipoServizioVO tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public List<String> getCodiciMsgSupplementari() {
		return codiciMsgSupplementari;
	}

	public void setCodiciMsgSupplementari(List<String> codiciMsgSupplementari) {
		this.codiciMsgSupplementari = codiciMsgSupplementari;
	}

	public String getTicket() {
		return ticket;
	}

	public Object getCheckData() {
		return checkData;
	}

	public void setCheckData(Object checkData) {
		this.checkData = checkData;
	}

	public ControlloAttivitaServizioResult getResult() {
		return result;
	}

	public DatiControlloVO setResult(ControlloAttivitaServizioResult result) {
		this.result = result;
		return this;
	}

	public boolean isInoltroPrenotazione() {
		return inoltroPrenotazione;
	}

	public void setInoltroPrenotazione(boolean inoltroPrenotazione) {
		this.inoltroPrenotazione = inoltroPrenotazione;
	}

	public OperatoreType getOperatore() {
		return operatore;
	}

	public void setOperatore(OperatoreType operatore) {
		this.operatore = operatore;
	}

	public String getCatMediazioneDigit() {
		return catMediazioneDigit;
	}

	public void setCatMediazioneDigit(String catMediazioneDigit) {
		this.catMediazioneDigit = catMediazioneDigit;
	}

	public String getCodNoDisp() {
		return codNoDisp;
	}

	public void setCodNoDisp(String codNoDisp) {
		this.codNoDisp = trimAndSet(codNoDisp);
	}

	public String getDataRedisp() {
		return dataRedisp;
	}

	public void setDataRedisp(String dataRedisp) {
		this.dataRedisp = trimAndSet(dataRedisp);
	}

	public ControlloAttivitaServizioResult getPreviousResult() {
		return previousResult;
	}

	public void setPreviousResult(ControlloAttivitaServizioResult previousResult) {
		this.previousResult = previousResult;
	}

	public MessaggioVO getUltimoMessaggio() {
		return ultimoMessaggio;
	}

	public void setUltimoMessaggio(MessaggioVO ultimoMessaggio) {
		this.ultimoMessaggio = ultimoMessaggio;
	}

	public MovimentoVO getMovimentoAggiornato() {
		return movimentoAggiornato;
	}

	public void setMovimentoAggiornato(MovimentoVO movimentoAggiornato) {
		this.movimentoAggiornato = movimentoAggiornato;
	}

}
