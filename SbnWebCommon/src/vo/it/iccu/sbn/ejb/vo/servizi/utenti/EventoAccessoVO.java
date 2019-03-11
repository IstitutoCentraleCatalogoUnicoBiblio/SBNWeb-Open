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
package it.iccu.sbn.ejb.vo.servizi.utenti;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.sql.Timestamp;

public class EventoAccessoVO extends BaseVO {

	private static final long serialVersionUID = 8216456455202165140L;

	public enum TipoEvento {
		ENTRATA,
		USCITA;

		public char getFl_evento() {
			return name().charAt(0);
		}

		public static TipoEvento of(char fl_evento) {
			for (TipoEvento te : values())
				if (te.getFl_evento() == fl_evento)
					return te;

			return null;
		}
	}

	private int id_evento;
	private String codPolo;
	private String codBib;
	private UtenteBaseVO utente;
	private int posto;
	private String idTessera;
	private Timestamp dataEvento;
	private TipoEvento evento = TipoEvento.ENTRATA;

	private boolean autenticato;
	private boolean forzato;

	public EventoAccessoVO() {
		super();
	}

	public EventoAccessoVO(EventoAccessoVO ea) {
		super();
		this.id_evento = ea.id_evento;
		this.codPolo = ea.codPolo;
		this.codBib = ea.codBib;
		this.utente = ea.utente;
		this.posto = ea.posto;
		this.idTessera = ea.idTessera;
		this.dataEvento = ea.dataEvento;
		this.evento = ea.evento;
		this.autenticato = ea.autenticato;
		this.forzato = ea.forzato;
	}

	public int getId_evento() {
		return id_evento;
	}

	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public UtenteBaseVO getUtente() {
		return utente;
	}

	public void setUtente(UtenteBaseVO utente) {
		this.utente = utente;
	}

	public int getPosto() {
		return posto;
	}

	public void setPosto(int posto) {
		this.posto = posto;
	}

	public String getIdTessera() {
		return idTessera;
	}

	public void setIdTessera(String tessera) {
		this.idTessera = tessera;
	}

	public Timestamp getDataEvento() {
		return dataEvento;
	}

	public void setDataEvento(Timestamp ts_evento) {
		this.dataEvento = ts_evento;
	}

	public TipoEvento getEvento() {
		return evento;
	}

	public void setEvento(TipoEvento evento) {
		this.evento = evento;
	}

	public boolean isAutenticato() {
		return autenticato;
	}

	public void setAutenticato(boolean autenticato) {
		this.autenticato = autenticato;
	}

	public boolean isForzato() {
		return forzato;
	}

	public void setForzato(boolean forzato) {
		this.forzato = forzato;
	}

	public boolean isNuovo() {
		return id_evento == 0;
	}

}
