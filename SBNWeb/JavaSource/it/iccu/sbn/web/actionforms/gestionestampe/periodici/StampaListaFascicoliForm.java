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
package it.iccu.sbn.web.actionforms.gestionestampe.periodici;

import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;

import java.util.ArrayList;
import java.util.List;

public class StampaListaFascicoliForm extends RicercaInventariCollocazioniForm {

	private static final long serialVersionUID = 4772844057242961220L;
	private String elemBlocco;
	private List listaStatoFascicolo;
	private String statoFascicolo;
	private List listaTipo;
	private String tipo;
	private String anno;
	private String codice;
	private String annoIniziale;
	private String annoFinale;
	private List listaPeriodicita;
	private String periodicita;
	private List listaStatoOrdine;
	private String statoOrdine;
	private List listaTipoOrdine;
	private String tipoOrdine;
	private List listaOrdinamento;
	private String ordinamento;
	private List listaStampaNote;
	private String stampaNote;
	//
	private String tipoModello;
	private List<ModelloStampaVO> elencoModelli = new ArrayList<ModelloStampaVO>();
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

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public List getListaTipo() {
		return listaTipo;
	}

	public void setListaTipo(List listaTipo) {
		this.listaTipo = listaTipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAnnoFinale() {
		return annoFinale;
	}

	public void setAnnoFinale(String annoFinale) {
		this.annoFinale = annoFinale;
	}

	public String getAnnoIniziale() {
		return annoIniziale;
	}

	public void setAnnoIniziale(String annoIniziale) {
		this.annoIniziale = annoIniziale;
	}

	public List getListaPeriodicita() {
		return listaPeriodicita;
	}

	public void setListaPeriodicita(List listaPeriodicita) {
		this.listaPeriodicita = listaPeriodicita;
	}

	public List getListaStatoOrdine() {
		return listaStatoOrdine;
	}

	public void setListaStatoOrdine(List listaStatoOrdine) {
		this.listaStatoOrdine = listaStatoOrdine;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public String getStatoOrdine() {
		return statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public List getListaOrdinamento() {
		return listaOrdinamento;
	}

	public void setListaOrdinamento(List listaOrdinamento) {
		this.listaOrdinamento = listaOrdinamento;
	}

	public List getListaStampaNote() {
		return listaStampaNote;
	}

	public void setListaStampaNote(List listaStampaNote) {
		this.listaStampaNote = listaStampaNote;
	}

	public String getStampaNote() {
		return stampaNote;
	}

	public void setStampaNote(String stampaNote) {
		this.stampaNote = stampaNote;
	}

	public List getListaStatoFascicolo() {
		return listaStatoFascicolo;
	}

	public void setListaStatoFascicolo(List listaStatoFascicolo) {
		this.listaStatoFascicolo = listaStatoFascicolo;
	}

	public String getStatoFascicolo() {
		return statoFascicolo;
	}

	public void setStatoFascicolo(String statoFascicolo) {
		this.statoFascicolo = statoFascicolo;
	}

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public List<ModelloStampaVO> getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ModelloStampaVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

}
