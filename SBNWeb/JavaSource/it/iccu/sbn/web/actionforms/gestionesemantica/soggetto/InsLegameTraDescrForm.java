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
package it.iccu.sbn.web.actionforms.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class InsLegameTraDescrForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -7711149839282474719L;



	private RicercaComuneVO ricercaComune = new RicercaComuneVO();



	private boolean abilita = true;
	private boolean enable;
	private List<TB_CODICI> listaTipoLegame;
	private List listaSoggettari;
	private String cid;
	private String didPadre;
	private String primoDid;
	private String primoTesto;
	private String primaForma;
	private String primoLivelloAut;
	private String secondoDid;
	private String secondoTesto;
	private String secondaForma;
	private String secondoLivelloAut;
	private String note;
	private String codTipoLegame;
	private String codice;
	private String action;
	private String T005;

	private String dataInserimento;
	private String dataModifica;
	private String tipoSoggetto;
	private boolean treeDaLista = false;
	private boolean enableScegli = false;
	private boolean enableModifica = false;
	private boolean condiviso = false;

	private List ricerca;

	private boolean sessione = false;

	public DettaglioDescrittoreVO[] getDescrittori() {

		ParametriSoggetti parametri = getParametriSogg();

		DettaglioDescrittoreVO did1 = (DettaglioDescrittoreVO) parametri.get(SoggettiParamType.DETTAGLIO_ID_PARTENZA);
		DettaglioDescrittoreVO did2 = (DettaglioDescrittoreVO) parametri.get(SoggettiParamType.DETTAGLIO_ID_ARRIVO);
		return new DettaglioDescrittoreVO[] {did1, did2};
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getCodTipoLegame() {
		return codTipoLegame;
	}

	public void setCodTipoLegame(String codTipoLegame) {
		this.codTipoLegame = codTipoLegame;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public List<TB_CODICI> getListaTipoLegame() {
		return listaTipoLegame;
	}

	public void setListaTipoLegame(List<TB_CODICI> listaTipoLegame) {
		this.listaTipoLegame = listaTipoLegame;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPrimoDid() {
		return primoDid;
	}

	public void setPrimoDid(String primoDid) {
		this.primoDid = primoDid;
	}

	public String getPrimoTesto() {
		return primoTesto;
	}

	public void setPrimoTesto(String primoTesto) {
		this.primoTesto = primoTesto;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public String getSecondoDid() {
		return secondoDid;
	}

	public void setSecondoDid(String secondoDid) {
		this.secondoDid = secondoDid;
	}

	public String getSecondoTesto() {
		return secondoTesto;
	}

	public void setSecondoTesto(String secondoTesto) {
		this.secondoTesto = secondoTesto;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getPrimaForma() {
		return primaForma;
	}

	public void setPrimaForma(String primaForma) {
		this.primaForma = primaForma;
	}

	public String getPrimoLivelloAut() {
		return primoLivelloAut;
	}

	public void setPrimoLivelloAut(String primoLivelloAut) {
		this.primoLivelloAut = primoLivelloAut;
	}

	public String getSecondaForma() {
		return secondaForma;
	}

	public void setSecondaForma(String secondaForma) {
		this.secondaForma = secondaForma;
	}

	public String getSecondoLivelloAut() {
		return secondoLivelloAut;
	}

	public void setSecondoLivelloAut(String secondoLivelloAut) {
		this.secondoLivelloAut = secondoLivelloAut;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(String dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public boolean isEnableModifica() {
		return enableModifica;
	}

	public void setEnableModifica(boolean enableModifica) {
		this.enableModifica = enableModifica;
	}

	public boolean isEnableScegli() {
		return enableScegli;
	}

	public void setEnableScegli(boolean enableScegli) {
		this.enableScegli = enableScegli;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getDidPadre() {
		return didPadre;
	}

	public void setDidPadre(String didPadre) {
		this.didPadre = didPadre;
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}





}








