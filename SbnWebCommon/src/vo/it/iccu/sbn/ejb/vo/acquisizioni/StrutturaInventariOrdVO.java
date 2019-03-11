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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.Comparator;

public class StrutturaInventariOrdVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -2296769616053994986L;

	public static final Comparator<StrutturaInventariOrdVO> ORDINAMENTO_POSIZIONE_VOLUME = new Comparator<StrutturaInventariOrdVO>() {

		public int compare(StrutturaInventariOrdVO o1, StrutturaInventariOrdVO o2) {
			int cmp = o1.posizione - o2.posizione;
			cmp = (cmp != 0) ? cmp : o1.volume - o2.volume;
			return cmp;
		}
	};

	private int IDOrd;
	private String codPolo;
	private String codBibl;
	private String denoBibl;
	private String serie;
	private String numero;
	private String bid;
	private String titolo;
	private String dataUscita;
	private String dataRientro;
	private String dataRientroPresunta;
	private String note;
	private String ticket;
	private Integer progressivo = 0;
	private String utente;
	private boolean flag_canc = false;
	private StrutturaCombo fornitore;
	private StrutturaTerna ordine; // tipo, anno, cod
	private String collocazione;

	private Short posizione;
	private Short volume;
	private String rfid;

	public StrutturaInventariOrdVO() {
		setSerie("");
		this.numero = "";
		this.titolo = "";
		this.dataUscita = "";
		this.dataRientro = "";
		this.note = "";
	};

	public StrutturaInventariOrdVO(String codPolo, String codBib) throws Exception {
		setSerie("");
		this.codPolo = codPolo;
		this.codBibl = codBib;
		this.numero = "";
		this.titolo = "";
		this.dataUscita = "";
		this.dataRientro = "";
		this.note = "";
	}

	public StrutturaInventariOrdVO(StrutturaInventariOrdVO sio) {
		super();
		this.IDOrd = sio.IDOrd;
		this.codPolo = sio.codPolo;
		this.codBibl = sio.codBibl;
		this.denoBibl = sio.denoBibl;
		this.serie = sio.serie;
		this.numero = sio.numero;
		this.bid = sio.bid;
		this.titolo = sio.titolo;
		this.dataUscita = sio.dataUscita;
		this.dataRientro = sio.dataRientro;
		this.dataRientroPresunta = sio.dataRientroPresunta;
		this.note = sio.note;
		this.ticket = sio.ticket;
		this.progressivo = sio.progressivo;
		this.utente = sio.utente;
		this.flag_canc = sio.flag_canc;
		this.fornitore = sio.fornitore.copy();
		this.ordine = sio.ordine.copy();
		this.collocazione = sio.collocazione;
		this.posizione = sio.posizione;
		this.volume = sio.volume;
		this.rfid = sio.rfid;
	}

	public String getChiave() {
		StringBuilder buf = new StringBuilder(32);
		buf.append(trimOrEmpty(codBibl));
		buf.append(" ");
		buf.append(ValidazioneDati.coalesce(serie, "") );
		buf.append(" ");
		buf.append(isFilled(numero) ? numero : "0");

		return buf.toString();
	}

	public String getDataRientro() {
		return dataRientro;
	}

	public void setDataRientro(String dataRientro) {
		this.dataRientro = dataRientro;
	}

	public String getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(String dataUscita) {
		this.dataUscita = dataUscita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public int getIDOrd() {
		return IDOrd;
	}

	public void setIDOrd(int ord) {
		IDOrd = ord;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = ValidazioneDati.fillRight(serie, ' ', 3);
	}

	public String getDataRientroPresunta() {
		return dataRientroPresunta;
	}

	public void setDataRientroPresunta(String dataRientroPresunta) {
		this.dataRientroPresunta = dataRientroPresunta;
	}

	public boolean isEsisteNota() {
		return isFilled(note);
	}

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public StrutturaTerna getOrdine() {
		return ordine;
	}

	public void setOrdine(StrutturaTerna ordine) {
		this.ordine = ordine;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getDenoBibl() {
		return denoBibl;
	}

	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}

	public Short getPosizione() {
		return posizione;
	}

	public void setPosizione(Short posizione) {
		this.posizione = posizione;
	}

	@Override
	public int getRepeatableId() {
		return super.getUniqueId();
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public Short getVolume() {
		return volume;
	}

	public void setVolume(Short volume) {
		this.volume = volume;
	}

	public int compareTo(StrutturaInventariOrdVO o) {
		return ORDINAMENTO_POSIZIONE_VOLUME.compare(this, o);
	}

}
