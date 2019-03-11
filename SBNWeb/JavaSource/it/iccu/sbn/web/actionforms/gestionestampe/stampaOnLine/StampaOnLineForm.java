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
package it.iccu.sbn.web.actionforms.gestionestampe.stampaOnLine;

import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.OrdineStampaOnlineVO;
import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaOnLineForm extends ActionForm {// extends StampaForm

	private static final long serialVersionUID = -3937256675452029781L;
	private String tipoFormato;
	private String tipoModello;
	private List datiLista = new ArrayList();
	private StampaType tipoStampa;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private String tipoOrdinamento;
	private String etichetteVuoteIniziali;
	private String findForward;
	private String numCopie;
	private boolean initialized;
	private String codBibEtichetta;
	private String descrBibEtichetta;
	private String modalita;
	private boolean disableModBarCode;
	private boolean disableModConfig;
	private String prov;
	private String ritornareAOrdine;
	private ModelloStampaVO modello;

	private OrdineStampaOnlineVO ordine = new OrdineStampaOnlineVO();

	private String motivoPrelievo;

	public ModelloStampaVO getModello() {
		return modello;
	}

	public void setModello(ModelloStampaVO modello) {
		this.modello = modello;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getRitornareAOrdine() {
		return ritornareAOrdine;
	}

	public void setRitornareAOrdine(String ritornareAOrdine) {
		this.ritornareAOrdine = ritornareAOrdine;
	}

	public boolean isDisableModBarCode() {
		return disableModBarCode;
	}

	public void setDisableModBarCode(boolean disableModBarCode) {
		this.disableModBarCode = disableModBarCode;
	}

	public boolean isDisableModConfig() {
		return disableModConfig;
	}

	public void setDisableModConfig(boolean disableModConfig) {
		this.disableModConfig = disableModConfig;
	}

	public String getFindForward() {
		return findForward;
	}

	public void setFindForward(String findForward) {
		this.findForward = findForward;
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

	public List getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public List getDatiLista() {
		return datiLista;
	}

	public void setDatiLista(List datiLista) {
		this.datiLista = datiLista;
	}

	public StampaType getTipoStampa() {
		return tipoStampa;
	}

	public void setTipoStampa(StampaType tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getEtichetteVuoteIniziali() {
		return etichetteVuoteIniziali;
	}

	public void setEtichetteVuoteIniziali(String etichetteVuoteIniziali) {
		this.etichetteVuoteIniziali = etichetteVuoteIniziali;
	}

	public String getNumCopie() {
		return numCopie;
	}

	public void setNumCopie(String numCopie) {
		this.numCopie = numCopie;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;

	}

	public boolean isInitialized() {
		return initialized;
	}

	public String getDescrBibEtichetta() {
		return descrBibEtichetta;
	}

	public void setDescrBibEtichetta(String descrBibEtichetta) {
		this.descrBibEtichetta = descrBibEtichetta;
	}

	public String getModalita() {
		return modalita;
	}

	public void setModalita(String mod) {
		this.modalita = mod;
	}

	public String getCodBibEtichetta() {
		return codBibEtichetta;
	}

	public void setCodBibEtichetta(String codBibEtichetta) {
		this.codBibEtichetta = codBibEtichetta;
	}

	public OrdineStampaOnlineVO getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdineStampaOnlineVO ordine) {
		this.ordine = ordine;
	}

	public String getMotivoPrelievo() {
		return motivoPrelievo;
	}

	public void setMotivoPrelievo(String motivoPrelievo) {
		this.motivoPrelievo = motivoPrelievo;
	}

}
