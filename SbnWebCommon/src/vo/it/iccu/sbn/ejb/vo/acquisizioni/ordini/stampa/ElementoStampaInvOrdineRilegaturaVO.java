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
package it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;

import java.util.Date;

public class ElementoStampaInvOrdineRilegaturaVO extends SerializableVO {

	private static final long serialVersionUID = 9170908297233853406L;

	private String bid;
	private String titolo;
	private String serie;
	private int numero;
	private String collocazione;
	private String note;
	private Date dataUscita;
	private Date dataRientroPresunta;
	private String dataRientro;
	private String codBibl;
	private String descrBib;
	private String chiaveOrdine;
	private String listaDatiIntestazione;
	private String testoOggetto;
	private FornitoreVO fornitore;
	private String testoIntroduttivo;
	private OrdineCarrelloSpedizioneVO carrello;

	//almaviva5_20140529 #4527
	private String noteInv;

	public ElementoStampaInvOrdineRilegaturaVO() {
		super();
	}

	public ElementoStampaInvOrdineRilegaturaVO(ElementoStampaInvOrdineRilegaturaVO o) {
		super();
		this.bid = o.bid;
		this.titolo = o.titolo;
		this.serie = o.serie;
		this.numero = o.numero;
		this.collocazione = o.collocazione;
		this.note = o.note;
		this.dataUscita = o.dataUscita;
		this.dataRientroPresunta = o.dataRientroPresunta;
		this.dataRientro = o.dataRientro;
		this.codBibl = o.codBibl;
		this.descrBib = o.descrBib;
		this.chiaveOrdine = o.chiaveOrdine;
		this.listaDatiIntestazione = o.listaDatiIntestazione;
		this.testoOggetto = o.testoOggetto;
		this.testoIntroduttivo = o.testoIntroduttivo;

		this.fornitore = (FornitoreVO) (o.fornitore != null ? o.fornitore.copy() : null);
		this.carrello = (OrdineCarrelloSpedizioneVO) (o.carrello != null ? o.carrello.copy() : null);

		//almaviva5_20140529 #4527
		this.noteInv = o.noteInv;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = trimAndSet(titolo);
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public Date getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(Date timestamp) {
		this.dataUscita = timestamp;
	}

	public Date getDataRientroPresunta() {
		return dataRientroPresunta;
	}

	public void setDataRientroPresunta(Date timestamp) {
		this.dataRientroPresunta = timestamp;
	}

	public String getDataRientro() {
		return dataRientro;
	}

	public void setDataRientro(String dataRientro) {
		this.dataRientro = dataRientro;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String denoBib) {
		this.descrBib = denoBib;
	}

	public String getChiaveOrdine() {
		return chiaveOrdine;
	}

	public void setChiaveOrdine(String chiaveOrdine) {
		this.chiaveOrdine = chiaveOrdine;
	}

	public String getListaDatiIntestazione() {
		return listaDatiIntestazione;
	}

	public void setListaDatiIntestazione(String listaDatiIntestazione) {
		this.listaDatiIntestazione = listaDatiIntestazione;
	}

	public String getTestoOggetto() {
		return testoOggetto;
	}

	public void setTestoOggetto(String testoOggetto) {
		this.testoOggetto = testoOggetto;
	}

	public FornitoreVO getFornitore() {
		return fornitore;
	}

	public void setFornitore(FornitoreVO fornitore) {
		this.fornitore = fornitore;
	}

	public String getTestoIntroduttivo() {
		return testoIntroduttivo;
	}

	public void setTestoIntroduttivo(String testoIntroduttivo) {
		this.testoIntroduttivo = testoIntroduttivo;
	}

	public OrdineCarrelloSpedizioneVO getCarrello() {
		return carrello;
	}

	public void setCarrello(OrdineCarrelloSpedizioneVO carrello) {
		this.carrello = carrello;
	}

	public String getNoteInv() {
		return noteInv;
	}

	public void setNoteInv(String noteInv) {
		this.noteInv = noteInv;
	}

	public boolean isGoogle() {
		return carrello != null
			&& isFilled(carrello.getCartName())
			&& isFilled(carrello.getPrgSpedizione());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bid == null) ? 0 : bid.hashCode());
		result = prime * result
				+ ((carrello == null) ? 0 : carrello.hashCode());
		result = prime * result
				+ ((chiaveOrdine == null) ? 0 : chiaveOrdine.hashCode());
		result = prime * result + ((codBibl == null) ? 0 : codBibl.hashCode());
		result = prime * result
				+ ((collocazione == null) ? 0 : collocazione.hashCode());
		result = prime * result
				+ ((dataRientro == null) ? 0 : dataRientro.hashCode());
		result = prime
				* result
				+ ((dataRientroPresunta == null) ? 0 : dataRientroPresunta
						.hashCode());
		result = prime * result
				+ ((dataUscita == null) ? 0 : dataUscita.hashCode());
		result = prime * result
				+ ((descrBib == null) ? 0 : descrBib.hashCode());
		result = prime * result
				+ ((fornitore == null) ? 0 : fornitore.hashCode());
		result = prime
				* result
				+ ((listaDatiIntestazione == null) ? 0 : listaDatiIntestazione
						.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + numero;
		result = prime * result + ((serie == null) ? 0 : serie.hashCode());
		result = prime
				* result
				+ ((testoIntroduttivo == null) ? 0 : testoIntroduttivo
						.hashCode());
		result = prime * result
				+ ((testoOggetto == null) ? 0 : testoOggetto.hashCode());
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementoStampaInvOrdineRilegaturaVO other = (ElementoStampaInvOrdineRilegaturaVO) obj;
		if (bid == null) {
			if (other.bid != null)
				return false;
		} else if (!bid.equals(other.bid))
			return false;
		if (carrello == null) {
			if (other.carrello != null)
				return false;
		} else if (!carrello.equals(other.carrello))
			return false;
		if (chiaveOrdine == null) {
			if (other.chiaveOrdine != null)
				return false;
		} else if (!chiaveOrdine.equals(other.chiaveOrdine))
			return false;
		if (codBibl == null) {
			if (other.codBibl != null)
				return false;
		} else if (!codBibl.equals(other.codBibl))
			return false;
		if (collocazione == null) {
			if (other.collocazione != null)
				return false;
		} else if (!collocazione.equals(other.collocazione))
			return false;
		if (dataRientro == null) {
			if (other.dataRientro != null)
				return false;
		} else if (!dataRientro.equals(other.dataRientro))
			return false;
		if (dataRientroPresunta == null) {
			if (other.dataRientroPresunta != null)
				return false;
		} else if (!dataRientroPresunta.equals(other.dataRientroPresunta))
			return false;
		if (dataUscita == null) {
			if (other.dataUscita != null)
				return false;
		} else if (!dataUscita.equals(other.dataUscita))
			return false;
		if (descrBib == null) {
			if (other.descrBib != null)
				return false;
		} else if (!descrBib.equals(other.descrBib))
			return false;
		if (fornitore == null) {
			if (other.fornitore != null)
				return false;
		} else if (!fornitore.equals(other.fornitore))
			return false;
		if (listaDatiIntestazione == null) {
			if (other.listaDatiIntestazione != null)
				return false;
		} else if (!listaDatiIntestazione.equals(other.listaDatiIntestazione))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (numero != other.numero)
			return false;
		if (serie == null) {
			if (other.serie != null)
				return false;
		} else if (!serie.equals(other.serie))
			return false;
		if (testoIntroduttivo == null) {
			if (other.testoIntroduttivo != null)
				return false;
		} else if (!testoIntroduttivo.equals(other.testoIntroduttivo))
			return false;
		if (testoOggetto == null) {
			if (other.testoOggetto != null)
				return false;
		} else if (!testoOggetto.equals(other.testoOggetto))
			return false;
		if (titolo == null) {
			if (other.titolo != null)
				return false;
		} else if (!titolo.equals(other.titolo))
			return false;
		return true;
	}

}
