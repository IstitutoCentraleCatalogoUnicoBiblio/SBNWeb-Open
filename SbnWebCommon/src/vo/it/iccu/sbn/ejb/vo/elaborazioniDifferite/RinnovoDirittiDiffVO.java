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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;

import java.util.ArrayList;
import java.util.List;

public class RinnovoDirittiDiffVO extends
		ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -3019497100487321388L;

	private Object parametri;
	private String tipoOrdinamento;
	private Boolean modalitaDifferita = false;

	private String[] codUtente;
	private String dataRinnovoAut;
	private String dataRinnovoOpz2;
	private String dataRinnovoOpz3;

	private String tipoRinnModalitaDiff = ""; // D=singolarmente (date
												// distinte),T= selezione (data
												// unica),U= tutti gli utenti
												// (data unica), utenti senza
												// aut
	private List<StrutturaCombo> elencoAutSel; // codAut, dataRinn

	private List<String> errori = new ArrayList<String>();

	public Object getParametri() {
		return parametri;
	}

	public void setParametri(Object parametri) {
		this.parametri = parametri;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String[] getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String[] codUtente) {
		this.codUtente = codUtente;
	}

	public String getDataRinnovoAut() {
		return dataRinnovoAut;
	}

	public void setDataRinnovoAut(String dataRinnovoAut) {
		this.dataRinnovoAut = dataRinnovoAut;
	}

	public List<String> getErrori() {
		return errori;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
	}

	public List<StrutturaCombo> getElencoAutSel() {
		return elencoAutSel;
	}

	public void setElencoAutSel(List<StrutturaCombo> elencoAutSel) {
		this.elencoAutSel = elencoAutSel;
	}

	public Boolean getModalitaDifferita() {
		return modalitaDifferita;
	}

	public void setModalitaDifferita(Boolean modalitaDifferita) {
		this.modalitaDifferita = modalitaDifferita;
	}

	public String getTipoRinnModalitaDiff() {
		return tipoRinnModalitaDiff;
	}

	public void setTipoRinnModalitaDiff(String tipoRinnModalitaDiff) {
		this.tipoRinnModalitaDiff = tipoRinnModalitaDiff;
	}

	public String getDataRinnovoOpz2() {
		return dataRinnovoOpz2;
	}

	public void setDataRinnovoOpz2(String dataRinnovoOpz2) {
		this.dataRinnovoOpz2 = dataRinnovoOpz2;
	}

	public String getDataRinnovoOpz3() {
		return dataRinnovoOpz3;
	}

	public void setDataRinnovoOpz3(String dataRinnovoOpz3) {
		this.dataRinnovoOpz3 = dataRinnovoOpz3;
	}

}
