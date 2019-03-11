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
package it.iccu.sbn.ejb.vo.servizi.sale;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PrenotazionePostoVO extends BaseVO {

	private static final long serialVersionUID = -7276532753056349886L;

	private int id_prenotazione;
	private String codPolo;
	private String codBib;
	private PostoSalaVO posto;
	private UtenteBaseVO utente;
	private List<MovimentoVO> movimenti = new ArrayList<MovimentoVO>();
	private StatoPrenotazionePosto stato;
	private Timestamp ts_inizio;
	private Timestamp ts_fine;

	private String catMediazione;

	private List<UtenteBaseVO> altriUtenti = new ArrayList<UtenteBaseVO>();

	public PrenotazionePostoVO() {
		super();
	}

	public PrenotazionePostoVO(PrenotazionePostoVO pp) {
		super(pp);
		this.id_prenotazione = pp.id_prenotazione;
		this.codPolo = pp.codPolo;
		this.codBib = pp.codBib;
		this.posto = pp.posto;
		this.utente = pp.utente;
		this.movimenti = pp.movimenti;
		this.stato = pp.stato;
		this.ts_inizio = pp.ts_inizio;
		this.ts_fine = pp.ts_fine;
		this.catMediazione = pp.catMediazione;
		this.altriUtenti = pp.altriUtenti;
	}

	public int getId_prenotazione() {
		return id_prenotazione;
	}

	public void setId_prenotazione(int id_prenotazione) {
		this.id_prenotazione = id_prenotazione;
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

	public PostoSalaVO getPosto() {
		return posto;
	}

	public void setPosto(PostoSalaVO posto) {
		this.posto = posto;
	}

	public UtenteBaseVO getUtente() {
		return utente;
	}

	public void setUtente(UtenteBaseVO utente) {
		this.utente = utente;
	}

	public List<MovimentoVO> getMovimenti() {
		return movimenti;
	}

	public void setMovimenti(List<MovimentoVO> movimenti) {
		this.movimenti = movimenti;
	}

	public StatoPrenotazionePosto getStato() {
		return stato;
	}

	public void setStato(StatoPrenotazionePosto stato) {
		this.stato = stato;
	}

	public Timestamp getTs_inizio() {
		return ts_inizio;
	}

	public void setTs_inizio(Timestamp ts_inizio) {
		this.ts_inizio = ts_inizio;
	}

	public Timestamp getTs_fine() {
		return ts_fine;
	}

	public void setTs_fine(Timestamp ts_fine) {
		this.ts_fine = ts_fine;
	}

	public String getCatMediazione() {
		return catMediazione;
	}

	public void setCatMediazione(String catMediazione) {
		this.catMediazione = catMediazione;
	}

	public List<UtenteBaseVO> getAltriUtenti() {
		return altriUtenti;
	}

	public void setAltriUtenti(List<UtenteBaseVO> altriUtenti) {
		this.altriUtenti = altriUtenti;
	}

	@Override
	public boolean isNuovo() {
		return id_prenotazione == 0;
	}

	public boolean isWithMovimento() {
		return isFilled(movimenti);
	}

}
