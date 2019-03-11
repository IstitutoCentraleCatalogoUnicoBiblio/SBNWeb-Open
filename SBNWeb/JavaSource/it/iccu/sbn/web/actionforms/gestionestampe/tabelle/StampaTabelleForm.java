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
package it.iccu.sbn.web.actionforms.gestionestampe.tabelle;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaTabelleForm extends ActionForm {

	private static final long serialVersionUID = -7601657469142365591L;
	private String elemBlocco;
	private List listaContinuativo;
	private String continuativo;
	private String tipoOrdine;
	private List listaTipoOrdine;
	private String codiceBibl;
	private String biblAffil;
	private List listaBiblAffil;
	private String sezione;
	private String fornitore;
	private String natura;
	private List listaNatura;
	private String dataOrdineDa;
	private String dataOrdineA;
	private String dataOrdineAbbDa;
	private String dataOrdineAbbA;
	private String stato;
	private List listaStato;
	private String tipoInvio;
	private List listaTipoInvio;
	private String numero;
	private String esercizio;
	private String capitolo;
	private boolean sessione;
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
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getContinuativo() {
		return continuativo;
	}
	public void setContinuativo(String continuativo) {
		this.continuativo = continuativo;
	}
	public String getTipoOrdine() {
		return tipoOrdine;
	}
	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}
	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}
	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}
	public List getListaContinuativo() {
		return listaContinuativo;
	}
	public void setListaContinuativo(List listaContinuativo) {
		this.listaContinuativo = listaContinuativo;
	}
	public String getCodiceBibl() {
		return codiceBibl;
	}
	public void setCodiceBibl(String codiceBibl) {
		this.codiceBibl = codiceBibl;
	}
	public String getBiblAffil() {
		return biblAffil;
	}
	public void setBiblAffil(String biblAffil) {
		this.biblAffil = biblAffil;
	}
	public List getListaBiblAffil() {
		return listaBiblAffil;
	}
	public void setListaBiblAffil(List listaBiblAffil) {
		this.listaBiblAffil = listaBiblAffil;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getFornitore() {
		return fornitore;
	}
	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}
	public List getListaNatura() {
		return listaNatura;
	}
	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public String getDataOrdineA() {
		return dataOrdineA;
	}
	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}
	public String getDataOrdineDa() {
		return dataOrdineDa;
	}
	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}
	public String getDataOrdineAbbA() {
		return dataOrdineAbbA;
	}
	public void setDataOrdineAbbA(String dataOrdineAbbA) {
		this.dataOrdineAbbA = dataOrdineAbbA;
	}
	public String getDataOrdineAbbDa() {
		return dataOrdineAbbDa;
	}
	public void setDataOrdineAbbDa(String dataOrdineAbbDa) {
		this.dataOrdineAbbDa = dataOrdineAbbDa;
	}
	public List getListaStato() {
		return listaStato;
	}
	public void setListaStato(List listaStato) {
		this.listaStato = listaStato;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public List getListaTipoInvio() {
		return listaTipoInvio;
	}
	public void setListaTipoInvio(List listaTipoInvio) {
		this.listaTipoInvio = listaTipoInvio;
	}
	public String getTipoInvio() {
		return tipoInvio;
	}
	public void setTipoInvio(String tipoInvio) {
		this.tipoInvio = tipoInvio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}
	public String getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

}
