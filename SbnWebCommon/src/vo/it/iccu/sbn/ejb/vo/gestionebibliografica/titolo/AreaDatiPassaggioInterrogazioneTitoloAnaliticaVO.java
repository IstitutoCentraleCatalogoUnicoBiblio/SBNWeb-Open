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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO  extends SerializableVO {

	// = AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 7723677515542461594L;
	private String bidRicerca;

	// Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
	// operante che nel caso di centro Sistema non coincidono; inserita impostazione iniziale nel Costruttore
	private String codiceBiblioSbn;

	private boolean ricercaPolo;
	private boolean ricercaIndice;

	private boolean inviaSoloTimeStampRadice;
	private boolean inviaSoloSbnMarc;


	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(String codBiblioSbn) {
		super();
		this.inviaSoloTimeStampRadice = false;
		this.inviaSoloSbnMarc = false;
		this.codiceBiblioSbn = codBiblioSbn;
	}


	public boolean isInviaSoloSbnMarc() {
		return inviaSoloSbnMarc;
	}


	public void setInviaSoloSbnMarc(boolean inviaSoloSbnMarc) {
		this.inviaSoloSbnMarc = inviaSoloSbnMarc;
	}


	public boolean isInviaSoloTimeStampRadice() {
		return inviaSoloTimeStampRadice;
	}

	public void setInviaSoloTimeStampRadice(boolean inviaSoloTimeStampRadice) {
		this.inviaSoloTimeStampRadice = inviaSoloTimeStampRadice;
	}

	public String getBidRicerca() {
		return bidRicerca;
	}
	public void setBidRicerca(String bidRicerca) {
		this.bidRicerca = bidRicerca;
	}
	public boolean isRicercaIndice() {
		return ricercaIndice;
	}
	public void setRicercaIndice(boolean ricercaIndice) {
		this.ricercaIndice = ricercaIndice;
	}
	public boolean isRicercaPolo() {
		return ricercaPolo;
	}
	public void setRicercaPolo(boolean ricercaPolo) {
		this.ricercaPolo = ricercaPolo;
	}


	public String getCodiceBiblioSbn() {
		return codiceBiblioSbn;
	}


	public void setCodiceBiblioSbn(String codiceBiblioSbn) {
		this.codiceBiblioSbn = codiceBiblioSbn;
	}
}
