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
package it.iccu.sbn.web.integration.servizi.erogazione;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;

import java.sql.Timestamp;

public class ControlloDisponibilitaVO extends SerializableVO {

	private static final long serialVersionUID = -3341111860168966899L;

	private ControlloAttivitaServizioResult result;
	private MovimentoVO movimentoAttivo;
	private MovimentoVO movimento;
	private int prenotazioniPendenti;
	private Timestamp dataPrenotazione;

	private final boolean inoltroPrenotazione;
	private OperatoreType operatore;

	private boolean noPrenotazione;

	private String motivo;
	private boolean disponibile;

	public ControlloDisponibilitaVO(MovimentoVO movimento, boolean inoltroPrenotazione) {
		super();
		this.movimento = movimento;
		this.inoltroPrenotazione = inoltroPrenotazione;
		this.dataPrenotazione = movimento.getDataFinePrev();
		this.result = ControlloAttivitaServizioResult.OK;
	}

	public ControlloDisponibilitaVO(ControlloDisponibilitaVO cd) {
		super();
		this.result = cd.result;
		this.movimentoAttivo = (MovimentoVO) (cd.movimentoAttivo != null ? cd.movimentoAttivo.copy() : null);
		this.movimento = (MovimentoVO) (cd.movimento != null ? cd.movimento.copy() : null);
		this.prenotazioniPendenti = cd.prenotazioniPendenti;
		this.dataPrenotazione = cd.dataPrenotazione;
		this.inoltroPrenotazione = cd.inoltroPrenotazione;
		this.operatore = cd.operatore;
		this.noPrenotazione = cd.noPrenotazione;
		this.motivo = cd.motivo;
		this.disponibile = cd.disponibile;
	}

	public ControlloAttivitaServizioResult getResult() {
		return result;
	}

	public ControlloDisponibilitaVO setResult(ControlloAttivitaServizioResult result) {
		this.result = result;

		return this;
	}

	public MovimentoVO getMovimento() {
		return movimento;
	}

	public void setMovimento(MovimentoVO movimento) {
		this.movimento = movimento;
	}

	public Timestamp getDataPrenotazione() {
		return dataPrenotazione;
	}

	public void setDataPrenotazione(Timestamp dataPrenotazione) {
		this.dataPrenotazione = dataPrenotazione;
	}

	public int getPrenotazioniPendenti() {
		return prenotazioniPendenti;
	}

	public void setPrenotazioniPendenti(int prenotazioniPendenti) {
		this.prenotazioniPendenti = prenotazioniPendenti;
	}

	public MovimentoVO getMovimentoAttivo() {
		return movimentoAttivo;
	}

	public void setMovimentoAttivo(MovimentoVO movimentoAttivo) {
		this.movimentoAttivo = movimentoAttivo;
	}

	public boolean isInoltroPrenotazione() {
		return inoltroPrenotazione;
	}

	public OperatoreType getOperatore() {
		return operatore;
	}

	public void setOperatore(OperatoreType operatore) {
		this.operatore = operatore;
	}

	public boolean isNoPrenotazione() {
		return noPrenotazione;
	}

	public void setNoPrenotazione(boolean noPrenotazione) {
		this.noPrenotazione = noPrenotazione;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = trimAndSet(motivo);
	}

	public boolean isDisponibile() {
		return disponibile;
	}

	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}

}
