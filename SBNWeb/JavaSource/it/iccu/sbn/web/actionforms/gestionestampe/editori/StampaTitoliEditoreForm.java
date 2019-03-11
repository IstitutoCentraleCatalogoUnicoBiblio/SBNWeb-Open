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
package it.iccu.sbn.web.actionforms.gestionestampe.editori;

import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaTitoliEditoreForm extends ActionForm{//  DynaValidatorForm


	private static final long serialVersionUID = -8836067116481602274L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codEditore;
	private String descrEditore;
	private String isbn;

	//Ottobre 2013: gestione della stampa titoli per Editore anche per Paese
	private String paese;
	private boolean disablePaese;
	private List listaPaesi;

	private String regione;
	private boolean disableCodEditore;
	private boolean disableDescrEditore;
	private boolean disableIsbn;
	private boolean disableRegione;
	private boolean disableProvincia;
	private List listaRegioni;
	private String provincia;
	private List listaProvincie;
	private String dataPubbl1Da;
	private String dataPubbl2Da;
	private String dataPubbl1A;
	private String dataPubbl2A;
	private String tipoRecord;
	private List listaTipiRecord;
	private String lingua;
	private List listaLingue;
	private String natura;
	private List listaNature;
	private String dataIngressoDa;
	private String dataIngressoA;
	private String tipoAcq;
	private List listaTipoAcq;

	private String codiceTipoMateriale;
	private List listaTipoMateriale;

	private String sistema;
	private List listaClassificazioni;
	private String simbolo;
	private boolean sessione;
	private String checkTipoRicerca;
	private String checkTipoPosseduto;
	private String checkEditore;
	private List<ModelloStampaVO> elencoModelli = new ArrayList<ModelloStampaVO>();
	private List listaBiblioteche;
	private String ticket;
	private String tipoFormato;
	private String tipoModello;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );



	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
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
	public String getCodEditore() {
		return codEditore;
	}
	public void setCodEditore(String codEditore) {
		this.codEditore = codEditore;
	}
	public String getDescrEditore() {
		return descrEditore;
	}
	public void setDescrEditore(String descrEditore) {
		this.descrEditore = descrEditore;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getRegione() {
		return regione;
	}
	public void setRegione(String regione) {
		this.regione = regione;
	}
	public List getListaRegioni() {
		return listaRegioni;
	}
	public void setListaRegioni(List listaRegioni) {
		this.listaRegioni = listaRegioni;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public List getListaProvincie() {
		return listaProvincie;
	}
	public void setListaProvincie(List listaProvincie) {
		this.listaProvincie = listaProvincie;
	}
	public String getDataPubbl1Da() {
		return dataPubbl1Da;
	}
	public void setDataPubbl1Da(String dataPubbl1Da) {
		this.dataPubbl1Da = dataPubbl1Da;
	}
	public String getDataPubbl2Da() {
		return dataPubbl2Da;
	}
	public void setDataPubbl2Da(String dataPubbl2Da) {
		this.dataPubbl2Da = dataPubbl2Da;
	}
	public String getDataPubbl1A() {
		return dataPubbl1A;
	}
	public void setDataPubbl1A(String dataPubbl1A) {
		this.dataPubbl1A = dataPubbl1A;
	}
	public String getDataPubbl2A() {
		return dataPubbl2A;
	}
	public void setDataPubbl2A(String dataPubbl2A) {
		this.dataPubbl2A = dataPubbl2A;
	}
	public String getTipoRecord() {
		return tipoRecord;
	}
	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public List getListaLingue() {
		return listaLingue;
	}
	public void setListaLingue(List listaLingue) {
		this.listaLingue = listaLingue;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public String getDataIngressoDa() {
		return dataIngressoDa;
	}
	public void setDataIngressoDa(String dataIngressoDa) {
		this.dataIngressoDa = dataIngressoDa;
	}
	public String getDataIngressoA() {
		return dataIngressoA;
	}
	public void setDataIngressoA(String dataIngressoA) {
		this.dataIngressoA = dataIngressoA;
	}
	public String getTipoAcq() {
		return tipoAcq;
	}
	public void setTipoAcq(String tipoAcq) {
		this.tipoAcq = tipoAcq;
	}
	public List getListaTipoAcq() {
		return listaTipoAcq;
	}
	public void setListaTipoAcq(List listaTipoAcq) {
		this.listaTipoAcq = listaTipoAcq;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public List getListaClassificazioni() {
		return listaClassificazioni;
	}
	public void setListaClassificazioni(List listaClassificazioni) {
		this.listaClassificazioni = listaClassificazioni;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getCheckTipoRicerca() {
		return checkTipoRicerca;
	}
	public void setCheckTipoRicerca(String checkTipoRicerca) {
		this.checkTipoRicerca = checkTipoRicerca;
	}
	public String getCheckTipoPosseduto() {
		return checkTipoPosseduto;
	}
	public void setCheckTipoPosseduto(String checkTipoPosseduto) {
		this.checkTipoPosseduto = checkTipoPosseduto;
	}
	public String getCheckEditore() {
		return checkEditore;
	}
	public void setCheckEditore(String checkEditore) {
		this.checkEditore = checkEditore;
	}
	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public List getListaTipiRecord() {
		return listaTipiRecord;
	}
	public void setListaTipiRecord(List listaTipiRecord) {
		this.listaTipiRecord = listaTipiRecord;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int nRec) {
		this.nRec = nRec;
	}
	public List getListaNature() {
		return listaNature;
	}
	public void setListaNature(List listaNature) {
		this.listaNature = listaNature;
	}
	public boolean isDisableCodEditore() {
		return disableCodEditore;
	}
	public void setDisableCodEditore(boolean disableCodEditore) {
		this.disableCodEditore = disableCodEditore;
	}
	public boolean isDisableDescrEditore() {
		return disableDescrEditore;
	}
	public void setDisableDescrEditore(boolean disableDescrEditore) {
		this.disableDescrEditore = disableDescrEditore;
	}
	public boolean isDisableIsbn() {
		return disableIsbn;
	}
	public void setDisableIsbn(boolean disableIsbn) {
		this.disableIsbn = disableIsbn;
	}
	public boolean isDisableRegione() {
		return disableRegione;
	}
	public void setDisableRegione(boolean disableRegione) {
		this.disableRegione = disableRegione;
	}

	public boolean isDisablePaese() {
		return disablePaese;
	}
	public void setDisablePaese(boolean disablePaese) {
		this.disablePaese = disablePaese;
	}




	public boolean isDisableProvincia() {
		return disableProvincia;
	}
	public void setDisableProvincia(boolean disableProvincia) {
		this.disableProvincia = disableProvincia;
	}
	public List<ModelloStampaVO> getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List<ModelloStampaVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}
	public String getCodiceTipoMateriale() {
		return codiceTipoMateriale;
	}
	public void setCodiceTipoMateriale(String codiceTipoMateriale) {
		this.codiceTipoMateriale = codiceTipoMateriale;
	}
	public List getListaTipoMateriale() {
		return listaTipoMateriale;
	}
	public void setListaTipoMateriale(List listaTipoMateriale) {
		this.listaTipoMateriale = listaTipoMateriale;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public List getListaPaesi() {
		return listaPaesi;
	}
	public void setListaPaesi(List listaPaesi) {
		this.listaPaesi = listaPaesi;
	}
}
