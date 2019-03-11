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

import java.util.List;

import org.apache.struts.upload.FormFile;

//public class EsportaForm extends ActionForm {
public class EstrattoreIdDocumentiForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = 4628847609549157474L;

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

	private List<ComboCodDescVO> listaCodScarico;

	private String codAttivita;

	private List<?> listaLingue;
	private List<?> listaPaese;

	private List<?> listaTipoData;
	private List<?> listaTipoRecord;

	private List<?> listaBiblioAteneo;
	private List<?> listaClassSistema;
	private List<?> listaPossedutoSerie;

	private List<?> listaCodTipoFruizione;
	private List<?> listaCodRiproducibilita;
	private List<?> listaCodNoDispo;

	private List<?> listaCodStatoConservazione;

	// Intervento L. almaviva2 06.07.2011 - Bug MANTIS 4510 (collaudo) vengono dichiarati i campi da utilizzare per
	// filtrare per data ts_var e data ts_ins (il filtro su data ins e data var ammette solo 4 chr, serve gg/mm/aaaa)
	private String dataInsFrom = "";
	private String dataInsTo = "";
	private String dataAggFrom = "";
	private String dataAggTo = "";


	private String elencoBiblio;

	// Intervento L. almaviva2 20.09.2011 - Interevnto interno vengono dichiarati i campi da utilizzare per
	// filtrare per ute_ins e ute_var (bibliotecario)
	private String tipoCodBibliotec;
	private String codBibliotecIns;
	private String nomeBibliotecIns;
	private String codBibliotecAgg;
	private String nomeBibliotecAgg;

	//almaviva5_20130904 evolutive google2
	private List<TB_CODICI> listaTipoDigit;


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

	public boolean isEstrattoreIdDoc() {
		return ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO);
	}

	public boolean isTastoCancBib() {
		return tastoCancBib;
	}

	public void setTastoCancBib(boolean tastoCancBib) {
		this.tastoCancBib = tastoCancBib;
	}

	public List<?> getListaCodTipoFruizione() {
		return listaCodTipoFruizione;
	}

	public void setListaCodTipoFruizione(List<?> listaCodTipoFruizione) {
		this.listaCodTipoFruizione = listaCodTipoFruizione;
	}

	public List<?> getListaCodRiproducibilita() {
		return listaCodRiproducibilita;
	}

	public void setListaCodRiproducibilita(List<?> listaCodRiproducibilita) {
		this.listaCodRiproducibilita = listaCodRiproducibilita;
	}

	public List<?> getListaCodNoDispo() {
		return listaCodNoDispo;
	}

	public void setListaCodNoDispo(List<?> listaCodNoDispo) {
		this.listaCodNoDispo = listaCodNoDispo;
	}

	public List<?> getListaCodStatoConservazione() {
		return listaCodStatoConservazione;
	}

	public void setListaCodStatoConservazione(
			List<?> listaCodStatoConservazione) {
		this.listaCodStatoConservazione = listaCodStatoConservazione;
	}

	public String getDataInsFrom() {
		return dataInsFrom;
	}

	public void setDataInsFrom(String dataInsFrom) {
		this.dataInsFrom = dataInsFrom;
	}

	public String getDataInsTo() {
		return dataInsTo;
	}

	public void setDataInsTo(String dataInsTo) {
		this.dataInsTo = dataInsTo;
	}

	public String getDataAggFrom() {
		return dataAggFrom;
	}

	public void setDataAggFrom(String dataAggFrom) {
		this.dataAggFrom = dataAggFrom;
	}

	public String getDataAggTo() {
		return dataAggTo;
	}

	public void setDataAggTo(String dataAggTo) {
		this.dataAggTo = dataAggTo;
	}

	public String getCodBibliotecIns() {
		return codBibliotecIns;
	}

	public void setCodBibliotecIns(String codBibliotecIns) {
		this.codBibliotecIns = codBibliotecIns;
	}

	public String getNomeBibliotecIns() {
		return nomeBibliotecIns;
	}

	public void setNomeBibliotecIns(String nomeBibliotecIns) {
		this.nomeBibliotecIns = nomeBibliotecIns;
	}

	public String getCodBibliotecAgg() {
		return codBibliotecAgg;
	}

	public void setCodBibliotecAgg(String codBibliotecAgg) {
		this.codBibliotecAgg = codBibliotecAgg;
	}

	public String getNomeBibliotecAgg() {
		return nomeBibliotecAgg;
	}

	public void setNomeBibliotecAgg(String nomeBibliotecAgg) {
		this.nomeBibliotecAgg = nomeBibliotecAgg;
	}

	public String getTipoCodBibliotec() {
		return tipoCodBibliotec;
	}

	public void setTipoCodBibliotec(String tipoCodBibliotec) {
		this.tipoCodBibliotec = tipoCodBibliotec;
	}

	public List<TB_CODICI> getListaTipoDigit() {
		return listaTipoDigit;
	}

	public void setListaTipoDigit(List<TB_CODICI> listaTipoDigit) {
		this.listaTipoDigit = listaTipoDigit;
	}

}
