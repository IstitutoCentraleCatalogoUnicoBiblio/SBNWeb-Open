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
package it.iccu.sbn.web.actionforms.gestionestampe.cataloghi;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaCataloghiOldVersionForm extends ActionForm {

	private static final long serialVersionUID = -6006005292089659472L;
	private String elemBlocco;
	private String materiale;
	private String catalogoPer;
	private List listaCatalogoPer;
	private String ordinatoPer;
	private List listaOrdinatoPer;
	private String dalTitolo;
	private String alTitolo;
	private String natura;
	private List listaNatura;
	private String status;
	private List listaStatus;
	private String lingua;
	private List listaLingua;
	private String paese;
	private List listaPaese;
	private String genere;
	private List listaGenere;
	private String tipoData;
	private List listaTipoData;
	private String dallaData;
	private String allaData;
	private String vid;
	private String dallAutore;
	private String allAutore;
	private String tipo;
	private List listaTipo;
	private String responsabilita;
	private List listaResponsabilita;
	private String dallaSezione;
	private String dallaCollocazione;
	private String specificazioneDallaCollocazione;
	private String allaCollocazione;
	private String specificazioneAllaCollocazione;
	private String serie;
	private List listaSerie;
	private String dalNumero;
	private String alNumero;
	private String soggettario;
	private List listaSoggettario;
	private String dalCid;
	private String alCid;
	private String dalSoggetto;
	private String alSoggetto;
	private String sistema;
	private List listaSistema;
	private String dalSimbolo;
	private String alSimbolo;
	private String codiceBibl;
	private String mid;
	private String repertorio;
	private List listaRepertorio;
	private String citazioneDa;
	private String citazioneA;
	private String dallaParola;
	private String allaParola;
	private String pid;
	private String dalPossessore;
	private String alPossessore;
	private String possessoreResponsabilita;
	private List listaPossessoreResponsabilita;
	private String stampaITitoli;
	private String chkDatiStampa;

	private boolean chkDatiStampa1;
	private boolean chkDatiStampa2;
	private boolean chkDatiStampa3;
	private boolean chkDatiStampa4;
	private boolean chkDatiStampa5;
	private String tipoFormato;

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}
	public String getCatalogoPer() {
		return catalogoPer;
	}
	public void setCatalogoPer(String catalogoPer) {
		this.catalogoPer = catalogoPer;
	}
	public List getListaCatalogoPer() {
		return listaCatalogoPer;
	}
	public void setListaCatalogoPer(List listaCatalogoPer) {
		this.listaCatalogoPer = listaCatalogoPer;
	}
	public String getOrdinatoPer() {
		return ordinatoPer;
	}
	public void setOrdinatoPer(String ordinatoPer) {
		this.ordinatoPer = ordinatoPer;
	}
	public List getListaOrdinatoPer() {
		return listaOrdinatoPer;
	}
	public void setListaOrdinatoPer(List listaOrdinatoPer) {
		this.listaOrdinatoPer = listaOrdinatoPer;
	}
	public String getDalTitolo() {
		return dalTitolo;
	}
	public void setDalTitolo(String dalTitolo) {
		this.dalTitolo = dalTitolo;
	}
	public String getAlTitolo() {
		return alTitolo;
	}
	public void setAlTitolo(String alTitolo) {
		this.alTitolo = alTitolo;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public List getListaNatura() {
		return listaNatura;
	}
	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}
	public List getListaStatus() {
		return listaStatus;
	}
	public void setListaStatus(List listaStatus) {
		this.listaStatus = listaStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public List getListaLingua() {
		return listaLingua;
	}
	public void setListaLingua(List listaLingua) {
		this.listaLingua = listaLingua;
	}
	public List getListaPaese() {
		return listaPaese;
	}
	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}
	public String getAllaData() {
		return allaData;
	}
	public void setAllaData(String allaData) {
		this.allaData = allaData;
	}
	public String getDallaData() {
		return dallaData;
	}
	public void setDallaData(String dallaData) {
		this.dallaData = dallaData;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getDallAutore() {
		return dallAutore;
	}
	public void setDallAutore(String dallAutore) {
		this.dallAutore = dallAutore;
	}
	public String getAllAutore() {
		return allAutore;
	}
	public void setAllAutore(String allAutore) {
		this.allAutore = allAutore;
	}
	public List getListaResponsabilita() {
		return listaResponsabilita;
	}
	public void setListaResponsabilita(List listaResponsabilita) {
		this.listaResponsabilita = listaResponsabilita;
	}
	public List getListaTipo() {
		return listaTipo;
	}
	public void setListaTipo(List listaTipo) {
		this.listaTipo = listaTipo;
	}
	public String getResponsabilita() {
		return responsabilita;
	}
	public void setResponsabilita(String responsabilita) {
		this.responsabilita = responsabilita;
	}
	public String getDallaSezione() {
		return dallaSezione;
	}
	public void setDallaSezione(String dallaSezione) {
		this.dallaSezione = dallaSezione;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getAllaCollocazione() {
		return allaCollocazione;
	}
	public void setAllaCollocazione(String allaCollocazione) {
		this.allaCollocazione = allaCollocazione;
	}
	public String getDallaCollocazione() {
		return dallaCollocazione;
	}
	public void setDallaCollocazione(String dallaCollocazione) {
		this.dallaCollocazione = dallaCollocazione;
	}
	public String getSpecificazioneAllaCollocazione() {
		return specificazioneAllaCollocazione;
	}
	public void setSpecificazioneAllaCollocazione(
			String specificazioneAllaCollocazione) {
		this.specificazioneAllaCollocazione = specificazioneAllaCollocazione;
	}
	public String getSpecificazioneDallaCollocazione() {
		return specificazioneDallaCollocazione;
	}
	public void setSpecificazioneDallaCollocazione(
			String specificazioneDallaCollocazione) {
		this.specificazioneDallaCollocazione = specificazioneDallaCollocazione;
	}
	public String getAlNumero() {
		return alNumero;
	}
	public void setAlNumero(String alNumero) {
		this.alNumero = alNumero;
	}
	public String getDalNumero() {
		return dalNumero;
	}
	public void setDalNumero(String dalNumero) {
		this.dalNumero = dalNumero;
	}
	public List getListaSerie() {
		return listaSerie;
	}
	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getAlCid() {
		return alCid;
	}
	public void setAlCid(String alCid) {
		this.alCid = alCid;
	}
	public String getDalCid() {
		return dalCid;
	}
	public void setDalCid(String dalCid) {
		this.dalCid = dalCid;
	}
	public List getListaSoggettario() {
		return listaSoggettario;
	}
	public void setListaSoggettario(List listaSoggettario) {
		this.listaSoggettario = listaSoggettario;
	}
	public String getSoggettario() {
		return soggettario;
	}
	public void setSoggettario(String soggettario) {
		this.soggettario = soggettario;
	}
	public String getAlSimbolo() {
		return alSimbolo;
	}
	public void setAlSimbolo(String alSimbolo) {
		this.alSimbolo = alSimbolo;
	}
	public String getDalSimbolo() {
		return dalSimbolo;
	}
	public void setDalSimbolo(String dalSimbolo) {
		this.dalSimbolo = dalSimbolo;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getAlSoggetto() {
		return alSoggetto;
	}
	public void setAlSoggetto(String alSoggetto) {
		this.alSoggetto = alSoggetto;
	}
	public String getDalSoggetto() {
		return dalSoggetto;
	}
	public void setDalSoggetto(String dalSoggetto) {
		this.dalSoggetto = dalSoggetto;
	}
	public String getGenere() {
		return genere;
	}
	public void setGenere(String genere) {
		this.genere = genere;
	}
	public List getListaGenere() {
		return listaGenere;
	}
	public void setListaGenere(List listaGenere) {
		this.listaGenere = listaGenere;
	}
	public List getListaTipoData() {
		return listaTipoData;
	}
	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}
	public String getTipoData() {
		return tipoData;
	}
	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}
	public String getCodiceBibl() {
		return codiceBibl;
	}
	public void setCodiceBibl(String codiceBibl) {
		this.codiceBibl = codiceBibl;
	}
	public List getListaSistema() {
		return listaSistema;
	}
	public void setListaSistema(List listaSistema) {
		this.listaSistema = listaSistema;
	}
	public String getMateriale() {
		return materiale;
	}
	public void setMateriale(String materiale) {
		this.materiale = materiale;
	}
	public String getAllaParola() {
		return allaParola;
	}
	public void setAllaParola(String allaParola) {
		this.allaParola = allaParola;
	}
	public String getCitazioneA() {
		return citazioneA;
	}
	public void setCitazioneA(String citazioneA) {
		this.citazioneA = citazioneA;
	}
	public String getCitazioneDa() {
		return citazioneDa;
	}
	public void setCitazioneDa(String citazioneDa) {
		this.citazioneDa = citazioneDa;
	}
	public String getDallaParola() {
		return dallaParola;
	}
	public void setDallaParola(String dallaParola) {
		this.dallaParola = dallaParola;
	}
	public List getListaRepertorio() {
		return listaRepertorio;
	}
	public void setListaRepertorio(List listaRepertorio) {
		this.listaRepertorio = listaRepertorio;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getRepertorio() {
		return repertorio;
	}
	public void setRepertorio(String repertorio) {
		this.repertorio = repertorio;
	}
	public String getAlPossessore() {
		return alPossessore;
	}
	public void setAlPossessore(String alPossessore) {
		this.alPossessore = alPossessore;
	}
	public String getDalPossessore() {
		return dalPossessore;
	}
	public void setDalPossessore(String dalPossessore) {
		this.dalPossessore = dalPossessore;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List getListaPossessoreResponsabilita() {
		return listaPossessoreResponsabilita;
	}
	public void setListaPossessoreResponsabilita(
			List listaPossessoreResponsabilita) {
		this.listaPossessoreResponsabilita = listaPossessoreResponsabilita;
	}
	public String getPossessoreResponsabilita() {
		return possessoreResponsabilita;
	}
	public void setPossessoreResponsabilita(String possessoreResponsabilita) {
		this.possessoreResponsabilita = possessoreResponsabilita;
	}
	public String getStampaITitoli() {
		return stampaITitoli;
	}
	public void setStampaITitoli(String stampaITitoli) {
		this.stampaITitoli = stampaITitoli;
	}
	public String getChkDatiStampa() {
		return chkDatiStampa;
	}
	public void setChkDatiStampa(String chkDatiStampa) {
		this.chkDatiStampa = chkDatiStampa;
	}
	public boolean isChkDatiStampa1() {
		return chkDatiStampa1;
	}
	public void setChkDatiStampa1(boolean chkDatiStampa1) {
		this.chkDatiStampa1 = chkDatiStampa1;
	}
	public boolean isChkDatiStampa2() {
		return chkDatiStampa2;
	}
	public void setChkDatiStampa2(boolean chkDatiStampa2) {
		this.chkDatiStampa2 = chkDatiStampa2;
	}
	public boolean isChkDatiStampa3() {
		return chkDatiStampa3;
	}
	public void setChkDatiStampa3(boolean chkDatiStampa3) {
		this.chkDatiStampa3 = chkDatiStampa3;
	}
	public boolean isChkDatiStampa4() {
		return chkDatiStampa4;
	}
	public void setChkDatiStampa4(boolean chkDatiStampa4) {
		this.chkDatiStampa4 = chkDatiStampa4;
	}
	public boolean isChkDatiStampa5() {
		return chkDatiStampa5;
	}
	public void setChkDatiStampa5(boolean chkDatiStampa5) {
		this.chkDatiStampa5 = chkDatiStampa5;
	}
}
