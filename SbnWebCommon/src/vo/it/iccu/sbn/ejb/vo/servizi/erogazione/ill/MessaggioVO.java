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
package it.iccu.sbn.ejb.vo.servizi.erogazione.ill;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.sql.Timestamp;

public class MessaggioVO extends BaseVO {

	private static final long serialVersionUID = -7091443923360150974L;
	private int id_messaggio;
	private int id_dati_richiesta_ill;
	private String note;
	private TipoInvio tipoInvio = TipoInvio.INVIATO;

	private String stato;
	private String condizione;
	private Timestamp dataMessaggio;

	private String requesterId;
	private String responderId;
	private RuoloBiblioteca ruolo = RuoloBiblioteca.RICHIEDENTE;

	public enum TipoInvio {
		INVIATO, RICEVUTO;

		public char getFl_tipo() {
			return name().charAt(0);
		}
	}

	public MessaggioVO(MessaggioVO msg) {
		super(msg);
		this.id_messaggio = msg.id_messaggio;
		this.id_dati_richiesta_ill = msg.id_dati_richiesta_ill;
		this.note = msg.note;
		this.tipoInvio = msg.tipoInvio;
		this.stato = msg.stato;
		this.condizione = msg.condizione;
		this.dataMessaggio = msg.dataMessaggio;
		this.requesterId = msg.requesterId;
		this.responderId = msg.responderId;
		this.ruolo = msg.ruolo;
	}

	public MessaggioVO() {
		super();
	}

	public MessaggioVO(DatiRichiestaILLVO datiILL) {
		super();
		this.requesterId = datiILL.getRequesterId();
		this.responderId = datiILL.getResponderId();
		this.ruolo = datiILL.getRuolo();
	}

	public int getId_messaggio() {
		return id_messaggio;
	}

	public void setId_messaggio(int id_messaggio) {
		this.id_messaggio = id_messaggio;
	}

	public int getId_dati_richiesta_ill() {
		return id_dati_richiesta_ill;
	}

	public void setId_dati_richiesta_ill(int id_dati_richiesta_ill) {
		this.id_dati_richiesta_ill = id_dati_richiesta_ill;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public TipoInvio getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(TipoInvio tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String cd_stato) {
		this.stato = trimAndSet(cd_stato);
	}

	public String getCondizione() {
		return condizione;
	}

	public void setCondizione(String condizione) {
		this.condizione = trimAndSet(condizione);
	}

	public Timestamp getDataMessaggio() {
		return dataMessaggio;
	}

	public void setDataMessaggio(Timestamp data_messaggio) {
		this.dataMessaggio = data_messaggio;
	}

	@Override
	public boolean isNuovo() {
		return id_messaggio == 0;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = trimAndSet(requesterId);
	}

	public String getResponderId() {
		return responderId;
	}

	public void setResponderId(String responderId) {
		this.responderId = trimAndSet(responderId);
	}

	public RuoloBiblioteca getRuolo() {
		return ruolo;
	}

	public void setRuolo(RuoloBiblioteca ruolo) {
		this.ruolo = ruolo;
	}

}
