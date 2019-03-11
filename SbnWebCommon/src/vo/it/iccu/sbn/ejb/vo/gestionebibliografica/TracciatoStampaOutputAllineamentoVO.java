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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class TracciatoStampaOutputAllineamentoVO extends SerializableVO {

	private static final long serialVersionUID = 947145824275550419L;

	private int progrAllineamento;
	private String dataAllineamento;

	private String oggettoAllineamento;
	private String tipoAllineamento;
	private String oggettoArrivo;

	private String esitoAllineamento;



	public TracciatoStampaOutputAllineamentoVO() {
		super();
	}

	public TracciatoStampaOutputAllineamentoVO(int progrAllineamento, String dataAllineamento, String oggettoAllineamento, String tipoAllineamento, String oggettoArrivo, String esitoAllineamento) {
		super();
		this.progrAllineamento = progrAllineamento;
		this.dataAllineamento = dataAllineamento;
		this.oggettoAllineamento = oggettoAllineamento;

		String appo = tipoAllineamento + "          ";
		this.tipoAllineamento = appo.substring(0,10);
		this.oggettoArrivo = oggettoArrivo;
		this.esitoAllineamento = esitoAllineamento;
	}

	public TracciatoStampaOutputAllineamentoVO(String messaggio) {
		super();
		this.progrAllineamento = 0;
		this.dataAllineamento = "";
		this.oggettoAllineamento = "";
		this.tipoAllineamento = "";
		this.oggettoArrivo = "";
		this.esitoAllineamento = messaggio;
	}


	public String getDataAllineamento() {
		return dataAllineamento;
	}

	public void setDataAllineamento(String dataAllineamento) {
		this.dataAllineamento = dataAllineamento;
	}

	public String getEsitoAllineamento() {
		return esitoAllineamento;
	}

	public void setEsitoAllineamento(String esitoAllineamento) {
		this.esitoAllineamento = esitoAllineamento;
	}

	public String getOggettoAllineamento() {
		return oggettoAllineamento;
	}

	public void setOggettoAllineamento(String oggettoAllineamento) {
		this.oggettoAllineamento = oggettoAllineamento;
	}

	public String getOggettoArrivo() {
		return oggettoArrivo;
	}

	public void setOggettoArrivo(String oggettoArrivo) {
		this.oggettoArrivo = oggettoArrivo;
	}

	public int getProgrAllineamento() {
		return progrAllineamento;
	}

	public void setProgrAllineamento(int progrAllineamento) {
		this.progrAllineamento = progrAllineamento;
	}

	public String getTipoAllineamento() {
		return tipoAllineamento;
	}

	public void setTipoAllineamento(String tipoAllineamento) {
		String appo = tipoAllineamento + "          ";
		this.tipoAllineamento = appo.substring(0,10);
	}

	public String getStringaToPrint() {

		// Intervento almaviva2 29.12.2010 BUG MANTIS 3963 -
		// inserimento/eliminazione  righe bianche fra una tipologia di allineamento
		// ed un altro (esempio fra allineamento BID e VID c'erano almeno 8 righe vuote);
		// (TracciatoStampaOutputAllineamentoVO - getStringaToPrint)

		if (this.oggettoAllineamento == null || this.oggettoAllineamento.equals("")) {
//			return this.esitoAllineamento + "<br>";
			return this.esitoAllineamento;
		} else if (this.oggettoArrivo == null || this.oggettoArrivo.equals("")) {
			return this.oggettoAllineamento + " " + this.tipoAllineamento + " " + "           "      + ": " + this.esitoAllineamento + "<br>";
		} else {
			return this.oggettoAllineamento + " " + this.tipoAllineamento + " " + this.oggettoArrivo + ": " + this.esitoAllineamento + "<br>";
		}

	}


}
