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

public class ImportaLegamiBidAltroIdVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -2161629353241062112L;

	public enum Institution {
		OCLC("OCoLC");

		private final String cd_istituzione;

		private Institution(String cd_istituzione) {
			this.cd_istituzione = cd_istituzione;

		}

		public String getCd_istituzione() {
			return cd_istituzione;
		}
	}

	private String nomeInputFile;
	private String inputFile;
	private Institution codIstituzione;

	public String getInputFile() {
		return inputFile;
	}

	public String getNomeInputFile() {
		return nomeInputFile;
	}

	public void setNomeInputFile(String nomeInputFile) {
		this.nomeInputFile = trimAndSet(nomeInputFile);
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public Institution getCodIstituzione() {
		return codIstituzione;
	}

	public void setCodIstituzione(Institution codIstituzione) {
		this.codIstituzione = codIstituzione;
	}

}
