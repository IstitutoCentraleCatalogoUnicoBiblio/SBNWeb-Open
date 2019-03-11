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

import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class SezioniCollocazioniGestioneForm extends ActionForm {


	private static final long serialVersionUID = -5782996343423961285L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private SezioneCollocazioneVO recSezione = new SezioneCollocazioneVO();
	private List listaTipoSezione;
	private List listaTipoCollocazione;
	private List listaClassificazioni;
	private boolean disable;
	private boolean esamina;
	private boolean disableSez=false;
	private boolean disableInvColl=false;
	private boolean disableTipoSez=false;
	private boolean disableTipoColl=false;
	private boolean disableUltPrg=false;
	private boolean disableNote=false;
	private boolean disableDescr=false;
	private boolean disableInvPrev=false;
	private boolean disableSistClass=false;
	private boolean sessione;
	private boolean ultPrgAss;
	private boolean sistCla;
	private String prov;
	private boolean conferma;
	private boolean tastoFormati=false;
	private boolean tastoAggiorna=false;
	private boolean salva;
	private boolean date=false;
	private boolean hoPremutoFormati = false;
	private String ticket;

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
	public List getListaTipoCollocazione() {
		return listaTipoCollocazione;
	}
	public void setListaTipoCollocazione(List listaTipoCollocazione) {
		this.listaTipoCollocazione = listaTipoCollocazione;
	}
	public List getListaTipoSezione() {
		return listaTipoSezione;
	}
	public void setListaTipoSezione(List listaTipoSezione) {
		this.listaTipoSezione = listaTipoSezione;
	}
	public SezioneCollocazioneVO getRecSezione() {
		return recSezione;
	}
	public void setRecSezione(SezioneCollocazioneVO recSezione) {
		this.recSezione = recSezione;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isSistCla() {
		return sistCla;
	}
	public void setSistCla(boolean sistCla) {
		this.sistCla = sistCla;
	}
	public boolean isUltPrgAss() {
		return ultPrgAss;
	}
	public void setUltPrgAss(boolean ultPrgAss) {
		this.ultPrgAss = ultPrgAss;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isTastoFormati() {
		return tastoFormati;
	}
	public void setTastoFormati(boolean tastoFormati) {
		this.tastoFormati = tastoFormati;
	}
	public boolean isDisableInvColl() {
		return disableInvColl;
	}
	public void setDisableInvColl(boolean disableInvColl) {
		this.disableInvColl = disableInvColl;
	}
	public boolean isDisableSez() {
		return disableSez;
	}
	public void setDisableSez(boolean disableSez) {
		this.disableSez = disableSez;
	}
	public boolean isDisableTipoColl() {
		return disableTipoColl;
	}
	public void setDisableTipoColl(boolean disableTipoColl) {
		this.disableTipoColl = disableTipoColl;
	}
	public boolean isDisableTipoSez() {
		return disableTipoSez;
	}
	public void setDisableTipoSez(boolean disableTipoSez) {
		this.disableTipoSez = disableTipoSez;
	}
	public boolean isDisableUltPrg() {
		return disableUltPrg;
	}
	public void setDisableUltPrg(boolean disableUltPrg) {
		this.disableUltPrg = disableUltPrg;
	}
	public boolean isDisableDescr() {
		return disableDescr;
	}
	public void setDisableDescr(boolean disableDescr) {
		this.disableDescr = disableDescr;
	}
	public boolean isDisableNote() {
		return disableNote;
	}
	public void setDisableNote(boolean disableNote) {
		this.disableNote = disableNote;
	}
	public boolean isDisableInvPrev() {
		return disableInvPrev;
	}
	public void setDisableInvPrev(boolean disableInvPrev) {
		this.disableInvPrev = disableInvPrev;
	}
	public boolean isDisableSistClass() {
		return disableSistClass;
	}
	public void setDisableSistClass(boolean disableSistClass) {
		this.disableSistClass = disableSistClass;
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
	public boolean isTastoAggiorna() {
		return tastoAggiorna;
	}
	public void setTastoAggiorna(boolean tastoAggiorna) {
		this.tastoAggiorna = tastoAggiorna;
	}
	public boolean isEsamina() {
		return esamina;
	}
	public void setEsamina(boolean esamina) {
		this.esamina = esamina;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public boolean isHoPremutoFormati() {
		return hoPremutoFormati;
	}
	public void setHoPremutoFormati(boolean hoPremutoFormati) {
		this.hoPremutoFormati = hoPremutoFormati;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public List getListaClassificazioni() {
		return listaClassificazioni;
	}
	public void setListaClassificazioni(List listaClassificazioni) {
		this.listaClassificazioni = listaClassificazioni;
	}

}
