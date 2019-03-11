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
package it.iccu.sbn.web.actionforms.elaborazioniDifferite.importa;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa.ImportaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;

import java.util.Collection;
import java.util.List;

import org.apache.struts.upload.FormFile;

public class ImportaForm extends RicercaInventariCollocazioniForm {

/*	public enum TipoProspettazioneExport {
		DATI_CATALOGRAFICI,
		POSSEDUTO,
		BIBLIOTECHE,
		CLASSI;
	}*/


	private static final long serialVersionUID = 4119947750504351408L;
	private ImportaVO importa = new ImportaVO();
//	private TipoProspettazioneExport tipoProspettazione = TipoProspettazioneExport.DATI_CATALOGRAFICI;
	private int focusDa = 0;
	private boolean  tastoCancBib;

	private FormFile fileIdList;
	private Collection<StrutturaCombo> listaEtichette;
	private String idRichiesta;
	private String idRichiestaVerificaBid;
	private String idRichiestaCaricamento;
	private String idRichiestaCancellazione;
	private String etichettaSelez;
	private String extractionTime;
	private boolean initialized;
	private String verificaBidPoloIndice;
	private String verificaTipoCaricamento;

	private List<ComboCodDescVO> listaCodScarico;

	// Inizio Modifica almaviva2 febbraio 2010 per utilizzare oggetto anche per stampa cataloghi.
	private String codAttivita;
	   // Opzioni di stampa
    private boolean intestTitoloAdAutore;
    private boolean titoloCollana;
    private boolean titoliAnalitici;
    private boolean datiCollocazione;
    private String tipoFormato;
	// Fine Modifica almaviva2 febbraio 2010 per utilizzare oggetto anche per stampa cataloghi.

	private List<?> listaLingue;
	private List<?> listaPaese;

	private List<?> listaTipoData;
	private List<?> listaTipoRecord;

	// //// # tab biblioteche
	private List<?> listaBiblioAteneo;
	// //// # tab classificazioni
	private List<?> listaClassSistema;

	// //// # tab posseduto
	private List<?> listaPossedutoSerie;

	private String elencoBiblio;

/*	public TipoProspettazioneExport getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(TipoProspettazioneExport tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}*/

	public ImportaVO getImporta() {
		return importa;
	}

	public void setImporta(ImportaVO importa) {
		this.importa = importa;
	}

	/**
	 * @return Returns the fileIdList.
	 */
	public FormFile getFileIdList() {
		return fileIdList;
	}

	/**
	 * @param fileIdList
	 *            The fileIdList to set.
	 */
	public void setFileIdList(FormFile fileIdList) {
		this.fileIdList = fileIdList;
	}

	public String getExtractionTime() {
		return extractionTime;
	}

	public void setExtractionTime(String extractionTime) {
		this.extractionTime = extractionTime;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public List<?> getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List<?> listaTipoData) {
		this.listaTipoData = listaTipoData;
	}

	public List<?> getListaBiblioAteneo() {
		return listaBiblioAteneo;
	}

	public void setListaBiblioAteneo(List<?> listaBiblioAteneo) {
		this.listaBiblioAteneo = listaBiblioAteneo;
	}

	public List<?> getListaClassSistema() {
		return listaClassSistema;
	}

	public void setListaClassSistema(List<?> listaClassSistema) {
		this.listaClassSistema = listaClassSistema;
	}

	public List<?> getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List<?> listaLingue) {
		this.listaLingue = listaLingue;
	}

	public List<?> getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List<?> listaPaese) {
		this.listaPaese = listaPaese;
	}

	public List<?> getListaTipoRecord() {
		return listaTipoRecord;
	}

	public void setListaTipoRecord(List<?> listaTipoRecord) {
		this.listaTipoRecord = listaTipoRecord;
	}

	public String getElencoBiblio() {
		return elencoBiblio;
	}

	public void setElencoBiblio(String elencoBiblio) {
		this.elencoBiblio = elencoBiblio;
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

	public boolean isIntestTitoloAdAutore() {
		return intestTitoloAdAutore;
	}

	public void setIntestTitoloAdAutore(boolean intestTitoloAdAutore) {
		this.intestTitoloAdAutore = intestTitoloAdAutore;
	}

	public boolean isTitoloCollana() {
		return titoloCollana;
	}

	public void setTitoloCollana(boolean titoloCollana) {
		this.titoloCollana = titoloCollana;
	}

	public boolean isTitoliAnalitici() {
		return titoliAnalitici;
	}

	public void setTitoliAnalitici(boolean titoliAnalitici) {
		this.titoliAnalitici = titoliAnalitici;
	}

	public boolean isDatiCollocazione() {
		return datiCollocazione;
	}

	public void setDatiCollocazione(boolean datiCollocazione) {
		this.datiCollocazione = datiCollocazione;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public List<?> getListaPossedutoSerie() {
		return listaPossedutoSerie;
	}

	public void setListaPossedutoSerie(List<?> listaPossedutoSerie) {
		this.listaPossedutoSerie = listaPossedutoSerie;
	}

	public List<ComboCodDescVO> getListaCodScarico() {
		return listaCodScarico;
	}

	public void setListaCodScarico(List<ComboCodDescVO> listaCodScarico) {
		this.listaCodScarico = listaCodScarico;
	}

	public int getFocusDa() {
		return focusDa;
	}

	public void setFocusDa(int focusDa) {
		this.focusDa = focusDa;
	}

	public boolean isUnimarc() {
		return ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040);
	}

	public boolean isTastoCancBib() {
		return tastoCancBib;
	}

	public void setTastoCancBib(boolean tastoCancBib) {
		this.tastoCancBib = tastoCancBib;
	}


	public String getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public String getIdRichiestaVerificaBid() {
		return idRichiestaVerificaBid;
	}
	public void setIdRichiestaVerificaBid(String idRichiestaVerificaBid) {
		this.idRichiestaVerificaBid = idRichiestaVerificaBid;
	}
	public Collection<StrutturaCombo> getListaEtichette() {
		return listaEtichette;
	}
	public void setListaEtichette(Collection<StrutturaCombo> listaEtichette) {
		this.listaEtichette = listaEtichette;
	}
	public String getEtichettaSelez() {
		return etichettaSelez;
	}
	public void setEtichettaSelez(String etichettaSelez) {
		this.etichettaSelez = etichettaSelez;
	}

	public boolean isVerificaBidPolo(){
		return ("P".equalsIgnoreCase(this.verificaBidPoloIndice)) ? true : false;
	}
	public String getVerificaBidPoloIndice() {
		return verificaBidPoloIndice;
	}
	public void setVerificaBidPoloIndice(String verificaBidPoloIndice) {
		this.verificaBidPoloIndice = verificaBidPoloIndice;
	}

	public String getVerificaTipoCaricamento() {
		return verificaTipoCaricamento;
	}

	public void setVerificaTipoCaricamento(String verificaTipoCaricamento) {
		this.verificaTipoCaricamento = verificaTipoCaricamento;
	}

	public String getIdRichiestaCaricamento() {
		return idRichiestaCaricamento;
	}

	public void setIdRichiestaCaricamento(String idRichiestaCaricamento) {
		this.idRichiestaCaricamento = idRichiestaCaricamento;
	}

	public String getIdRichiestaCancellazione() {
		return idRichiestaCancellazione;
	}

	public void setIdRichiestaCancellazione(String idRichiestaCancellazione) {
		this.idRichiestaCancellazione = idRichiestaCancellazione;
	}



}
