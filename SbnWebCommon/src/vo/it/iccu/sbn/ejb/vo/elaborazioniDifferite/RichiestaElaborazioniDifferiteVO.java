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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.common.RicercaBaseTimeRangeVO;

import java.io.Serializable;

public class RichiestaElaborazioniDifferiteVO extends RicercaBaseTimeRangeVO<Serializable> {

	private static final long serialVersionUID = -3758492552874441087L;

	private String visibilita;
	private String procedura;
	private String idRichiesta;
	private String dataRichiesta;
	private String dataEsecuzioneProgrammata;
	private String biblioteca;
	private String richiedente;
	private String stato;


	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getDataEsecuzioneProgrammata() {
		return dataEsecuzioneProgrammata;
	}

	public void setDataEsecuzioneProgrammata(String dataEsecuzioneProgrammata) {
		this.dataEsecuzioneProgrammata = dataEsecuzioneProgrammata;
	}

	public String getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public String getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = trimAndSet(idRichiesta);
	}

	public String getProcedura() {
		return procedura;
	}

	public void setProcedura(String procedura) {
		this.procedura = procedura;
	}

	public String getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(String richiedente) {
		this.richiedente = trimAndSet(richiedente);
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(String visibilita) {
		this.visibilita = visibilita;
	}

}
