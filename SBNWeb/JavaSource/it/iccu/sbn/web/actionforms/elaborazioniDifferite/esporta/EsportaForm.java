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
package it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.vo.custom.esporta.EsportaDecorator;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import org.apache.struts.upload.FormFile;

//public class EsportaForm extends ActionForm {
public class EsportaForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = 7675017348373270037L;

	public enum TipoProspettazioneExport {
		DATI_CATALOGRAFICI,
		POSSEDUTO,
		BIBLIOTECHE,
		CLASSI;
	}

	private EsportaVO esporta = new EsportaDecorator();
	private TipoProspettazioneExport tipoProspettazione = TipoProspettazioneExport.DATI_CATALOGRAFICI;
	private int focusDa = 0;
	private boolean  tastoCancBib;

	private FormFile fileIdList;
	private String extractionTime;
	private boolean initialized;

	private List<TB_CODICI> listaCodScarico;

	// Inizio Modifica almaviva2 febbraio 2010 per utilizzare oggetto anche per stampa cataloghi.
	private String codAttivita;
	   // Opzioni di stampa
    private boolean intestTitoloAdAutore;
    private boolean titoloCollana;
    private boolean titoliAnalitici;
    private boolean datiCollocazione;
    private String tipoFormato;
	// Fine Modifica almaviva2 febbraio 2010 per utilizzare oggetto anche per stampa cataloghi.

	// Intervento del 15.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	// Eliminata combo con tutte le biblio ed inserito cartiglio come in stampe di Documento Fisico
    private String codicePolo;
    private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	// Intervento del 25.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	// Modifiche per attivazione stampa catalogo per Soggetti e Classi
    private List<ComboCodDescVO> listaSoggettari;
    private List<ComboCodDescVO> listaSistemiClassificazione;

	private List<?> listaLingue;
	private List<?> listaPaese;

	private List<?> listaTipoData;
	private List<?> listaTipoRecord;

	private List<ComboCodDescVO> listaTipoCatalogo;

	// //// # tab biblioteche
	private List<?> listaBiblioAteneo;
	// //// # tab classificazioni
	private List<?> listaClassSistema;

	// //// # tab posseduto
	private List<?> listaPossedutoSerie;

	private String elencoBiblio;

	public TipoProspettazioneExport getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(TipoProspettazioneExport tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public EsportaVO getEsporta() {
		return esporta;
	}

	public void setEsporta(EsportaVO esporta) {
		this.esporta = esporta;
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

	public List<TB_CODICI> getListaCodScarico() {
		return listaCodScarico;
	}

	public void setListaCodScarico(List<TB_CODICI> listaCodScarico) {
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

	public List<ComboCodDescVO> getListaTipoCatalogo() {
		return listaTipoCatalogo;
	}

	public void setListaTipoCatalogo(List<ComboCodDescVO> listaTipoCatalogo) {
		this.listaTipoCatalogo = listaTipoCatalogo;
	}

	public String getCodicePolo() {
		return codicePolo;
	}

	public void setCodicePolo(String codicePolo) {
		this.codicePolo = codicePolo;
	}

	public int getnRec() {
		return nRec;
	}

	public void setnRec(int nRec) {
		this.nRec = nRec;
	}

	public List<ComboCodDescVO> getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List<ComboCodDescVO> listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public List<ComboCodDescVO> getListaSistemiClassificazione() {
		return listaSistemiClassificazione;
	}

	public void setListaSistemiClassificazione(List<ComboCodDescVO> listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
	}

}
