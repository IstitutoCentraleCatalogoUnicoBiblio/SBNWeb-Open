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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class AttivitaProfilazioneVO extends SerializableVO {

	private static final long serialVersionUID = 1896727193508718022L;

	private String codiceAttivita;
	private String descrizioneAttivita;
	private String codiceFunzione;
	private String descrizioneFunzione;
	private boolean selezionato;

	public String getCodiceAttivita() {
		return codiceAttivita;
	}

	public void setCodiceAttivita(String codiceAttivita) {
		this.codiceAttivita = codiceAttivita;
	}

	public String getCodiceFunzione() {
		return codiceFunzione;
	}

	public void setCodiceFunzione(String codiceFunzione) {
		this.codiceFunzione = codiceFunzione;
	}

	public String getDescrizioneAttivita() {
		return descrizioneAttivita;
	}

	public void setDescrizioneAttivita(String descrizioneAttivita) {
		this.descrizioneAttivita = descrizioneAttivita;
	}

	public boolean isSelezionato() {
		return selezionato;
	}

	public void setSelezionato(boolean selezionato) {
		this.selezionato = selezionato;
	}

	public String getDescrizioneFunzione() {
		return descrizioneFunzione;
	}

	public void setDescrizioneFunzione(String descrizioneFunzione) {
		this.descrizioneFunzione = descrizioneFunzione;
	}

	public AttivitaProfilazioneVO(String codiceAttivita,
			String descrizioneAttivita, boolean selezionato) {
		this.codiceAttivita = codiceAttivita;
		this.descrizioneAttivita = descrizioneAttivita;
		this.selezionato = selezionato;
		this.codiceFunzione = "cdFn";
		this.descrizioneFunzione = "DescFn";
	}

	public AttivitaProfilazioneVO(String codiceAttivita,
			String descrizioneAttivita, boolean selezionato,
			String codiceFunzione, String descrizioneFunzione) {
		this.codiceAttivita = codiceAttivita;
		this.descrizioneAttivita = descrizioneAttivita;
		this.selezionato = selezionato;
		this.codiceFunzione = codiceFunzione;
		this.descrizioneFunzione = descrizioneFunzione;
	}

}
