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
package it.iccu.sbn.web.actionforms.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class CreaClasseForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -2776933851356388238L;
	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private DettaglioClasseVO dettClaGen = new DettaglioClasseVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private String notazioneTrascinaDa;
	private String testoTrascinaDa;
	private List titoliBiblio;
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private String bid;
	private String testoBid;
	private FolderType folder;
	private boolean enableTit = false;
	private boolean abilita = true;
	private boolean noEdizione = false;
	private boolean polo;
	private boolean indice;
	private List listaStatoControllo;
	private List listaSistemiClassificazione;
	private List<TB_CODICI> listaEdizioni;
	private String simbolo;
	private String livelloAutorita;
	private String descrizione;
	private String ulterioreTermine;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private String codStatoControllo;
	private int numNotiziePolo;
	private int numNotizieBiblio;
	private String action;
	private List ricerca;
	private boolean enableSalvaSog = true;
	private boolean enableOk = false;
	private boolean enableEdizione = false;
	private boolean enableStato = false;
	private boolean enableSimbolo = false;
	private boolean sessione = false;
	private RicercaClasseListaVO outputLista;
	private boolean costruito;

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
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

	public String getSogInserimento() {
		return sogInserimento;
	}

	public void setSogInserimento(String sogInserimento) {
		this.sogInserimento = sogInserimento;
	}

	public String getSogModifica() {
		return sogModifica;
	}

	public void setSogModifica(String sogModifica) {
		this.sogModifica = sogModifica;
	}

	public int getNumNotizieBiblio() {
		return numNotizieBiblio;
	}

	public void setNumNotizieBiblio(int numNotizieBiblio) {
		this.numNotizieBiblio = numNotizieBiblio;
	}

	public int getNumNotiziePolo() {
		return numNotiziePolo;
	}

	public void setNumNotiziePolo(int numNotiziePolo) {
		this.numNotiziePolo = numNotiziePolo;
	}

	public String getCodStatoControllo() {
		return codStatoControllo;
	}

	public void setCodStatoControllo(String codStatoControllo) {
		this.codStatoControllo = codStatoControllo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public boolean isEnableSalvaSog() {
		return enableSalvaSog;
	}

	public void setEnableSalvaSog(boolean enableSalvaSog) {
		this.enableSalvaSog = enableSalvaSog;
	}

	public boolean isIndice() {
		return indice;
	}

	public void setIndice(boolean indice) {
		this.indice = indice;
	}

	public boolean isPolo() {
		return polo;
	}

	public void setPolo(boolean polo) {
		this.polo = polo;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public List getListaSistemiClassificazione() {
		return listaSistemiClassificazione;
	}

	public void setListaSistemiClassificazione(
			List listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
	}

	public RicercaClasseListaVO getOutputLista() {
		return outputLista;
	}

	public void setOutputLista(RicercaClasseListaVO outputLista) {
		this.outputLista = outputLista;
	}

	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		this.ulterioreTermine = ulterioreTermine;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder2) {
		this.folder = folder2;
	}

	public String getTestoBid() {
		return testoBid;
	}

	public void setTestoBid(String testoBid) {
		this.testoBid = testoBid;
	}

	public boolean isEnableTit() {
		return enableTit;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public boolean isEnableEdizione() {
		return enableEdizione;
	}

	public void setEnableEdizione(boolean enableEdizione) {
		this.enableEdizione = enableEdizione;
	}

	public boolean isEnableSimbolo() {
		return enableSimbolo;
	}

	public void setEnableSimbolo(boolean enableSimbolo) {
		this.enableSimbolo = enableSimbolo;
	}

	public boolean isEnableStato() {
		return enableStato;
	}

	public void setEnableStato(boolean enableStato) {
		this.enableStato = enableStato;
	}

	public DettaglioClasseVO getDettClaGen() {
		return dettClaGen;
	}

	public void setDettClaGen(DettaglioClasseVO dettClaGen) {
		this.dettClaGen = dettClaGen;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public String getNotazioneTrascinaDa() {
		return notazioneTrascinaDa;
	}

	public void setNotazioneTrascinaDa(String notazioneTrascinaDa) {
		this.notazioneTrascinaDa = notazioneTrascinaDa;
	}

	public String getTestoTrascinaDa() {
		return testoTrascinaDa;
	}

	public void setTestoTrascinaDa(String testoTrascinaDa) {
		this.testoTrascinaDa = testoTrascinaDa;
	}

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public boolean isNoEdizione() {
		return noEdizione;
	}

	public void setNoEdizione(boolean noEdizione) {
		this.noEdizione = noEdizione;
	}

	public boolean isCostruito() {
		return costruito;
	}

	public void setCostruito(boolean costruito) {
		this.costruito = costruito;
	}

}
