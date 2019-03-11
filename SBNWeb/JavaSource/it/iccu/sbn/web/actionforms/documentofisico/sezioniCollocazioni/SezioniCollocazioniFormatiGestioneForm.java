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
package it.iccu.sbn.web.actionforms.documentofisico.sezioniCollocazioni;

import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class SezioniCollocazioniFormatiGestioneForm extends ActionForm {


	private static final long serialVersionUID = 1938118934640563652L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSez;
	private String descrSez;
	private int numSerie;
	private int numAss;
	private SezioneCollocazioneVO sezione = new SezioneCollocazioneVO();
	private FormatiSezioniVO recFormatiSezioni = new FormatiSezioniVO();
	private String codFormato;
	private String descrFormato;
	private List listaCodiciFormati;
	private List listaFormatiSezioni;
	private String selectedFor;
	private boolean noFormati;
	private boolean disable;
	private boolean disableFormato = false;
	private boolean disableDescrFormato = false;
	private boolean disableDimDa = false;
	private boolean disableDimA = false;
	public boolean isDisableDimDa() {
		return disableDimDa;
	}
	public void setDisableDimDa(boolean disableDimDa) {
		this.disableDimDa = disableDimDa;
	}
	public boolean isDisableDimA() {
		return disableDimA;
	}
	public void setDisableDimA(boolean disableDimA) {
		this.disableDimA = disableDimA;
	}
	private boolean disableNPezzi = false;
	private boolean disableNSerie = false;
	private boolean disablePrgAss = false;
	private boolean disableNPezziMisc = false;
	private boolean disableNSerieNum1Misc = false;
	private boolean disableDalProgrMisc = false;
	private boolean disableNSerieNum2Misc = false;
	private boolean disableAlProgrMisc = false;
	private boolean sessione;
	private String prov;
	private boolean conferma;
	private boolean modifica;
	private boolean salva;
	private boolean date=false;
	private String ticket;
	private String dataIns;
	private String dataAgg;
	private String uteIns;
	private String uteAgg;

	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getUteIns() {
		return uteIns;
	}
	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}
	public String getUteAgg() {
		return uteAgg;
	}
	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isDate() {
		return date;
	}
	public void setDate(boolean date) {
		this.date = date;
	}
	public boolean isSalva() {
		return salva;
	}
	public void setSalva(boolean salva) {
		this.salva = salva;
	}
	public List getListaCodiciFormati() {
		return listaCodiciFormati;
	}
	public void setListaCodiciFormati(List listaCodiciFormati) {
		this.listaCodiciFormati = listaCodiciFormati;
	}
	public FormatiSezioniVO getRecFormatiSezioni() {
		return recFormatiSezioni;
	}
	public void setRecFormatiSezioni(FormatiSezioniVO recFormatiSezioni) {
		this.recFormatiSezioni = recFormatiSezioni;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public String getDescrSez() {
		return descrSez;
	}
	public void setDescrSez(String descrSez) {
		this.descrSez = descrSez;
	}
	public List getListaFormatiSezioni() {
		return listaFormatiSezioni;
	}
	public void setListaFormatiSezioni(List listaFormatiSezioni) {
		this.listaFormatiSezioni = listaFormatiSezioni;
	}
	public boolean isNoFormati() {
		return noFormati;
	}
	public void setNoFormati(boolean noFormati) {
		this.noFormati = noFormati;
	}
	public String getSelectedFor() {
		return selectedFor;
	}
	public void setSelectedFor(String selectedFor) {
		this.selectedFor = selectedFor;
	}
	public boolean isModifica() {
		return modifica;
	}
	public void setModifica(boolean modifica) {
		this.modifica = modifica;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodFormato() {
		return codFormato;
	}
	public void setCodFormato(String codFormato) {
		this.codFormato = codFormato;
	}
	public boolean isDisableFormato() {
		return disableFormato;
	}
	public void setDisableFormato(boolean disableFormato) {
		this.disableFormato = disableFormato;
	}
	public boolean isDisableNPezzi() {
		return disableNPezzi;
	}
	public void setDisableNPezzi(boolean disableNPezzi) {
		this.disableNPezzi = disableNPezzi;
	}
	public boolean isDisableNSerie() {
		return disableNSerie;
	}
	public void setDisableNSerie(boolean disableNSerie) {
		this.disableNSerie = disableNSerie;
	}
	public boolean isDisablePrgAss() {
		return disablePrgAss;
	}
	public void setDisablePrgAss(boolean disablePrgAss) {
		this.disablePrgAss = disablePrgAss;
	}
	public boolean isDisableDescrFormato() {
		return disableDescrFormato;
	}
	public void setDisableDescrFormato(boolean disableDescrFormato) {
		this.disableDescrFormato = disableDescrFormato;
	}
	public int getNumSerie() {
		return numSerie;
	}
	public void setNumSerie(int numSerie) {
		this.numSerie = numSerie;
	}
	public int getNumAss() {
		return numAss;
	}
	public void setNumAss(int numAss) {
		this.numAss = numAss;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getDescrFormato() {
		return descrFormato;
	}
	public void setDescrFormato(String descrFormato) {
		this.descrFormato = descrFormato;
	}
	public boolean isDisableNPezziMisc() {
		return disableNPezziMisc;
	}
	public void setDisableNPezziMisc(boolean disableNPezziMisc) {
		this.disableNPezziMisc = disableNPezziMisc;
	}
	public boolean isDisableDalProgrMisc() {
		return disableDalProgrMisc;
	}
	public void setDisableDalProgrMisc(boolean disableDalProgrMisc) {
		this.disableDalProgrMisc = disableDalProgrMisc;
	}
	public boolean isDisableAlProgrMisc() {
		return disableAlProgrMisc;
	}
	public void setDisableAlProgrMisc(boolean disableAlProgrMisc) {
		this.disableAlProgrMisc = disableAlProgrMisc;
	}
	public SezioneCollocazioneVO getSezione() {
		return sezione;
	}
	public void setSezione(SezioneCollocazioneVO sezione) {
		this.sezione = sezione;
	}
	public boolean isDisableNSerieNum1Misc() {
		return disableNSerieNum1Misc;
	}
	public void setDisableNSerieNum1Misc(boolean disableNSerieNum1Misc) {
		this.disableNSerieNum1Misc = disableNSerieNum1Misc;
	}
	public boolean isDisableNSerieNum2Misc() {
		return disableNSerieNum2Misc;
	}
	public void setDisableNSerieNum2Misc(boolean disableNSerieNum2Misc) {
		this.disableNSerieNum2Misc = disableNSerieNum2Misc;
	}


}
