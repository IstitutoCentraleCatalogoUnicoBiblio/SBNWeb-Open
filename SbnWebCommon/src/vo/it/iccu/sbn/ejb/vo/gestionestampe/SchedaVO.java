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
package it.iccu.sbn.ejb.vo.gestionestampe;



public class SchedaVO extends EtichettaDettaglioVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 2713673451854313492L;
	/**
	 *
	 */

	private String bidInventario;
	private String bidCollocazione;
	private String bidEsemplare;

	public SchedaVO(){
		super();
	}

	public String getBidInventario() {
		return bidInventario;
	}

	public void setBidInventario(String bidInventario) {
		this.bidInventario = bidInventario;
	}

	public String getBidCollocazione() {
		return bidCollocazione;
	}

	public void setBidCollocazione(String bidCollocazione) {
		this.bidCollocazione = bidCollocazione;
	}

	public String getBidEsemplare() {
		return bidEsemplare;
	}

	public void setBidEsemplare(String bidEsemplare) {
		this.bidEsemplare = bidEsemplare;
	}


}
