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

public class AcquisizioneUriCopiaDigitaleVO extends	ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -5402054163788370385L;

	private String inputFile;
	private String tipoInput;
	private String tipoDigit;
	private String prefisso;
	private String suffisso;
	private String model;
	private boolean preparaFileIndice = true;
	private boolean eliminaSpaziUri;

	private String tecaDigitale;
	private String dispDaRemoto;

	private boolean aggiungiUri = true;


	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getTipoDigit() {
		return tipoDigit;
	}

	public void setTipoDigit(String tipoDigit) {
		this.tipoDigit = trimAndSet(tipoDigit);
	}

	public String getPrefisso() {
		return prefisso;
	}

	public void setPrefisso(String prefisso) {
		this.prefisso = trimAndSet(prefisso);
	}

	public String getSuffisso() {
		return suffisso;
	}

	public void setSuffisso(String suffisso) {
		this.suffisso = trimAndSet(suffisso);
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = trimAndSet(model);
	}

	public boolean isPreparaFileIndice() {
		return preparaFileIndice;
	}

	public void setPreparaFileIndice(boolean preparaFileIndice) {
		this.preparaFileIndice = preparaFileIndice;
	}

	public boolean isEliminaSpaziUri() {
		return eliminaSpaziUri;
	}

	public void setEliminaSpaziUri(boolean eliminaSpaziUri) {
		this.eliminaSpaziUri = eliminaSpaziUri;
	}

	public String getTecaDigitale() {
		return tecaDigitale;
	}

	public void setTecaDigitale(String tecaDigitale) {
		this.tecaDigitale = trimAndSet(tecaDigitale);
	}

	public String getDispDaRemoto() {
		return dispDaRemoto;
	}

	public void setDispDaRemoto(String dispDaRemoto) {
		this.dispDaRemoto = trimAndSet(dispDaRemoto);
	}

	public String getTipoInput() {
		return tipoInput;
	}

	public void setTipoInput(String tipoInput) {
		this.tipoInput = trimAndSet(tipoInput);
	}

	public boolean isAggiungiUri() {
		return aggiungiUri;
	}

	public void setAggiungiUri(boolean aggiungiUri) {
		this.aggiungiUri = aggiungiUri;
	}

}
