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
package it.iccu.sbn.web.actionforms.servizi.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class RinnovoAutorizzazioniUtenteForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 7297739918782466038L;

	private boolean sessione = false;

	private String codBib;
	private String descrBib;
	private String dataSvecchiamento;
	private String codPolo;

	private String dataRinnovoAut;
	private String dataRinnovoOpz2;
	private String dataRinnovoOpz3;
	private String tipoRinnModalitaDiff="D"; // D=singolarmente (date distinte),T= selezione (data unica),U= tutti gli utenti (data unica),L= tutti gli utenti (data unica) utenti senza aut
	private List<StrutturaCombo> elencoAutSel; // codAut, dataRinn
	private String[] selectedAut=new String[0];
	private List<StrutturaTerna> listaAutorizzazioni=new ArrayList<StrutturaTerna>(); // codAut,descr, dataRinn
	private boolean risultatiPresenti=true;

	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;


	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getDataSvecchiamento() {
		return dataSvecchiamento;
	}
	public void setDataSvecchiamento(String dataSvecchiamento) {
		this.dataSvecchiamento = dataSvecchiamento;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getDataRinnovoAut() {
		return dataRinnovoAut;
	}
	public void setDataRinnovoAut(String dataRinnovoAut) {
		this.dataRinnovoAut = dataRinnovoAut;
	}
	public String[] getSelectedAut() {
		return selectedAut;
	}
	public void setSelectedAut(String[] selectedAut) {
		this.selectedAut = selectedAut;
	}
	public String getTipoRinnModalitaDiff() {
		return tipoRinnModalitaDiff;
	}
	public void setTipoRinnModalitaDiff(String tipoRinnModalitaDiff) {
		this.tipoRinnModalitaDiff = tipoRinnModalitaDiff;
	}
	public List getListaAutorizzazioni() {
		return listaAutorizzazioni;
	}
	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}
	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
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
	public List<StrutturaCombo> getElencoAutSel() {
		return elencoAutSel;
	}
	public void setElencoAutSel(List<StrutturaCombo> elencoAutSel) {
		this.elencoAutSel = elencoAutSel;
	}
	public void setListaAutorizzazioni(List<StrutturaTerna> listaAutorizzazioni) {
		this.listaAutorizzazioni = listaAutorizzazioni;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}
	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}
	public String getPressioneBottone() {
		return pressioneBottone;
	}
	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}
}





