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
package it.iccu.sbn.web.actionforms.gestionestampe.buoni;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;

import java.util.ArrayList;
import java.util.List;

public class StampaBuoniCaricoForm extends RicercaInventariCollocazioniForm {

	private static final long serialVersionUID = 7938869859880778284L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String ticket;
	private List listaTipiOrdinamento;
	private boolean ristampaDaInv;// hidden
	private boolean ristampaDaFattura;// hidden
	private boolean ristampaDaNumBuono;// hidden

	private String tipoOperazione;
	private String tipoOrdinamento;
	private String elemBlocco;
	private String tipoRicerca;
	private List listaBiblio;
	private boolean disable;
	private boolean disableRistampaNumBuono;
	private boolean sessione = false;
	// private String folder;
	private String buonoCarico;
	private String annoFattura;
	private String numFattura;
	private String progrFattura;
	private String tipoModello;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private String tipoFormato;
	private ModelloStampaVO modello;

	// almaviva5_20131118 #5100
	private String dataCarico;

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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
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

	public String getNumFattura() {
		return numFattura;
	}

	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getAnnoFattura() {
		return annoFattura;
	}

	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}

	public ModelloStampaVO getModello() {
		return modello;
	}

	public void setModello(ModelloStampaVO modello) {
		this.modello = modello;
	}

	public String getProgrFattura() {
		return progrFattura;
	}

	public void setProgrFattura(String progrFattura) {
		this.progrFattura = progrFattura;
	}

	public boolean isRistampaDaInv() {
		return ristampaDaInv;
	}

	public void setRistampaDaInv(boolean ristampaDaInv) {
		this.ristampaDaInv = ristampaDaInv;
	}

	public boolean isRistampaDaFattura() {
		return ristampaDaFattura;
	}

	public void setRistampaDaFattura(boolean ristampaDaFattura) {
		this.ristampaDaFattura = ristampaDaFattura;
	}

	public boolean isRistampaDaNumBuono() {
		return ristampaDaNumBuono;
	}

	public void setRistampaDaNumBuono(boolean ristampaDaNumBuono) {
		this.ristampaDaNumBuono = ristampaDaNumBuono;
	}

	public boolean isDisableRistampaNumBuono() {
		return disableRistampaNumBuono;
	}

	public void setDisableRistampaNumBuono(boolean disableRistampaNumBuono) {
		this.disableRistampaNumBuono = disableRistampaNumBuono;
	}

	public String getBuonoCarico() {
		return buonoCarico;
	}

	public void setBuonoCarico(String buonoCarico) {
		this.buonoCarico = buonoCarico;
	}

	public String getDataCarico() {
		return dataCarico;
	}

	public void setDataCarico(String dataCarico) {
		this.dataCarico = dataCarico;
	}
}
