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
package it.iccu.sbn.ejb.vo.servizi.erogazione.ill;

public class DatiRichiestaILLRicercaVO extends DatiRichiestaILLVO {

	private static final long serialVersionUID = 5000106350734005439L;

	private String tipoOrdinamento;
	private int numeroElementiBlocco;

	private String codStatoRic;
	private String codStatoMov;

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public int getNumeroElementiBlocco() {
		return numeroElementiBlocco;
	}

	public void setNumeroElementiBlocco(int numeroElementiBlocco) {
		this.numeroElementiBlocco = numeroElementiBlocco;
	}

	public String getCodStatoRic() {
		return codStatoRic;
	}

	public void setCodStatoRic(String codStatoRic) {
		this.codStatoRic = codStatoRic;
	}

	public String getCodStatoMov() {
		return codStatoMov;
	}

	public void setCodStatoMov(String codStatoMov) {
		this.codStatoMov = codStatoMov;
	}

}
