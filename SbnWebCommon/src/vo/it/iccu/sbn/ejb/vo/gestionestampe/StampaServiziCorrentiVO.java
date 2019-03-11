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

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.Locale;

public class StampaServiziCorrentiVO extends StampaServiziDettagliVO{


	/**
	 *
	 */
	private static final long serialVersionUID = 6086262051992718664L;

	public StampaServiziCorrentiVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	//servizi correnti
	private String codServizio;
	private String progrMovimento;
	private String progrIter;
	private String codAttivita;
	private String statoRichiesta;



	private Locale locale = Locale.getDefault();
	private String numberFormatPrezzi = "###,###,###,##0.00";

	public String getCodServizio() {
		return codServizio;
	}
	public void setCodServizio(String codServizio) {
		this.codServizio = codServizio;
	}
	public String getProgrMovimento() {
		return progrMovimento;
	}
	public void setProgrMovimento(String progrMovimento) {
		this.progrMovimento = progrMovimento;
	}
	public String getProgrIter() {
		return progrIter;
	}
	public void setProgrIter(String progrIter) {
		this.progrIter = progrIter;
	}
	public String getCodAttivita() {
		return codAttivita;
	}
	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getNumberFormatPrezzi() {
		return numberFormatPrezzi;
	}
	public String getStatoRichiesta() {
		return statoRichiesta;
	}
	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}
}
