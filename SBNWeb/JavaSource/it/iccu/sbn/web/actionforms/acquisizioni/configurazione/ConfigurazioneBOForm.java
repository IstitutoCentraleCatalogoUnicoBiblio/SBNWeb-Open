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
package it.iccu.sbn.web.actionforms.acquisizioni.configurazione;

import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class ConfigurazioneBOForm extends DynaValidatorForm {


	private static final long serialVersionUID = 55610886154153602L;
	private ConfigurazioneBOVO datiConfigBO = new ConfigurazioneBOVO();
	private String[] selectedDatiFineStampa;
	private String[] selectedDatiIntest;
	private String[] selectedFormulaIntroduttiva;
	private String[] selectedTestoOggetto;

	private boolean numAutomatica;
	private boolean sessione;
	private boolean enableIntest;
	private StrutturaTerna[] datiIntest;
	private StrutturaTerna[] datiFineStampa;
	private StrutturaQuater[] formulaIntroduttiva;
	private StrutturaQuater[] testoOggetto;

	private String numerazBuono; // check
	private String areaTit; // check
	private String areaEdi; // check
	private String areaNum; // check
	private String areaPub; // check
	private String logo; // check
	private String firmaDigit; // check

	private String imgLogo;
	private String prezzo; // check
	private String numProt; // check
	private String dataProt; // check
	private String indicazioneRinnovo; // check
	private String imgFirma;
	private String ristampa; // check
	private String tipoOrdine; // COMBO
	private String scegliLingua; // COMBO
	private String imgLogoPathCompleto;
	private String imgFirmaPathCompleto;

	private String numIntest;
	private String aggiungiIntest;
	private boolean checkIntest;
	private List listaRox = new ArrayList();
	private boolean conferma = false;

	private boolean disabilitaTutto = false;
	private transient FormFile fileIdList;
	private transient FormFile fileIdListFirma;

	private String[] fields = { "A" };;

	// almaviva5_20121115 evolutive google
	private List<TB_CODICI> listaTipoLavorazione;
	private Integer selectedFormulaIntroOrdineR;

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	private String datoAgg;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// super.reset(mapping, request);
		this.set("numAutomatica", false);
		// this.set("selectedDatiFineStampa",new String[0]);
		// this.set("selectedDatiIntest",new String[0]);
		// this.set("selectedFormulaIntroduttiva",new String[0]);
		// this.set("selectedTestoOggetto",new String[0]);

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public ConfigurazioneBOVO getDatiConfigBO() {
		return datiConfigBO;
	}

	public void setDatiConfigBO(ConfigurazioneBOVO datiConfigBO) {
		this.datiConfigBO = datiConfigBO;
	}

	public String[] getSelectedDatiFineStampa() {
		return selectedDatiFineStampa;
	}

	public void setSelectedDatiFineStampa(String[] selectedDatiFineStampa) {
		this.selectedDatiFineStampa = selectedDatiFineStampa;
	}

	public String[] getSelectedDatiIntest() {
		return selectedDatiIntest;
	}

	public void setSelectedDatiIntest(String[] selectedDatiIntest) {
		this.selectedDatiIntest = selectedDatiIntest;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getDatoAgg() {
		return datoAgg;
	}

	public void setDatoAgg(String datoAgg) {
		this.datoAgg = datoAgg;
	}

	public String getAggiungiIntest() {
		return aggiungiIntest;
	}

	public void setAggiungiIntest(String aggiungiIntest) {
		this.aggiungiIntest = aggiungiIntest;
	}

	public boolean isEnableIntest() {
		return enableIntest;
	}

	public void setEnableIntest(boolean enableIntest) {
		this.enableIntest = enableIntest;
	}

	public String getNumIntest() {
		return numIntest;
	}

	public void setNumIntest(String numIntest) {
		this.numIntest = numIntest;
	}

	public boolean isCheckIntest() {
		return checkIntest;
	}

	public void setCheckIntest(boolean checkIntest) {
		this.checkIntest = checkIntest;
	}

	public List getListaRox() {
		return listaRox;
	}

	public void setListaRox(List listaRox) {
		this.listaRox = listaRox;
	}

	public StrutturaTerna[] getDatiFineStampa() {
		return datiFineStampa;
	}

	public void setDatiFineStampa(StrutturaTerna[] datiFineStampa) {
		this.datiFineStampa = datiFineStampa;
	}

	public StrutturaTerna[] getDatiIntest() {
		return datiIntest;
	}

	public void setDatiIntest(StrutturaTerna[] datiIntest) {
		this.datiIntest = datiIntest;
	}

	public boolean isNumAutomatica() {
		return numAutomatica;
	}

	public void setNumAutomatica(boolean numAutomatica) {
		this.numAutomatica = numAutomatica;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public String getNumerazBuono() {
		return numerazBuono;
	}

	public void setNumerazBuono(String numerazBuono) {
		this.numerazBuono = numerazBuono;
	}

	public String getAreaEdi() {
		return areaEdi;
	}

	public void setAreaEdi(String areaEdi) {
		this.areaEdi = areaEdi;
	}

	public String getAreaNum() {
		return areaNum;
	}

	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	public String getAreaPub() {
		return areaPub;
	}

	public void setAreaPub(String areaPub) {
		this.areaPub = areaPub;
	}

	public String getAreaTit() {
		return areaTit;
	}

	public void setAreaTit(String areaTit) {
		this.areaTit = areaTit;
	}

	public String getImgLogo() {
		return imgLogo;
	}

	public void setImgLogo(String imgLogo) {
		this.imgLogo = imgLogo;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDataProt() {
		return dataProt;
	}

	public void setDataProt(String dataProt) {
		this.dataProt = dataProt;
	}

	public String getIndicazioneRinnovo() {
		return indicazioneRinnovo;
	}

	public void setIndicazioneRinnovo(String indicazioneRinnovo) {
		this.indicazioneRinnovo = indicazioneRinnovo;
	}

	public String getNumProt() {
		return numProt;
	}

	public void setNumProt(String numProt) {
		this.numProt = numProt;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public String getImgFirma() {
		return imgFirma;
	}

	public void setImgFirma(String imgFirma) {
		this.imgFirma = imgFirma;
	}

	public String getRistampa() {
		return ristampa;
	}

	public void setRistampa(String ristampa) {
		this.ristampa = ristampa;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public String getScegliLingua() {
		return scegliLingua;
	}

	public void setScegliLingua(String scegliLingua) {
		this.scegliLingua = scegliLingua;
	}

	public String[] getSelectedFormulaIntroduttiva() {
		return selectedFormulaIntroduttiva;
	}

	public void setSelectedFormulaIntroduttiva(
			String[] selectedFormulaIntroduttiva) {
		this.selectedFormulaIntroduttiva = selectedFormulaIntroduttiva;
	}

	public StrutturaQuater[] getFormulaIntroduttiva() {
		return formulaIntroduttiva;
	}

	public void setFormulaIntroduttiva(StrutturaQuater[] formulaIntroduttiva) {
		this.formulaIntroduttiva = formulaIntroduttiva;
	}

	public String[] getSelectedTestoOggetto() {
		return selectedTestoOggetto;
	}

	public void setSelectedTestoOggetto(String[] selectedTestoOggetto) {
		this.selectedTestoOggetto = selectedTestoOggetto;
	}

	public StrutturaQuater[] getTestoOggetto() {
		return testoOggetto;
	}

	public void setTestoOggetto(StrutturaQuater[] testoOggetto) {
		this.testoOggetto = testoOggetto;
	}

	public String getFirmaDigit() {
		return firmaDigit;
	}

	public void setFirmaDigit(String firmaDigit) {
		this.firmaDigit = firmaDigit;
	}

	public FormFile getFileIdList() {
		return fileIdList;
	}

	public void setFileIdList(FormFile fileIdList) {
		this.fileIdList = fileIdList;
	}

	public FormFile getFileIdListFirma() {
		return fileIdListFirma;
	}

	public void setFileIdListFirma(FormFile fileIdListFirma) {
		this.fileIdListFirma = fileIdListFirma;
	}

	public String getImgFirmaPathCompleto() {
		return imgFirmaPathCompleto;
	}

	public void setImgFirmaPathCompleto(String imgFirmaPathCompleto) {
		this.imgFirmaPathCompleto = imgFirmaPathCompleto;
	}

	public String getImgLogoPathCompleto() {
		return imgLogoPathCompleto;
	}

	public void setImgLogoPathCompleto(String imgLogoPathCompleto) {
		this.imgLogoPathCompleto = imgLogoPathCompleto;
	}

	public List<TB_CODICI> getListaTipoLavorazione() {
		return listaTipoLavorazione;
	}

	public void setListaTipoLavorazione(List<TB_CODICI> listaTipoLavorazione) {
		this.listaTipoLavorazione = listaTipoLavorazione;
	}

	/*
	 * public FormulaIntroOrdineRVO getItem(int index) {
	 *
	 * // automatically grow List size List<FormulaIntroOrdineRVO> items =
	 * this.datiConfigBO.getFormulaIntroOrdineR(); if (items == null) { items =
	 * new ArrayList<FormulaIntroOrdineRVO>();
	 * this.datiConfigBO.setFormulaIntroOrdineR(items); }
	 *
	 * while (index >= items.size()) items.add(new FormulaIntroOrdineRVO("ITA",
	 * null, null));
	 *
	 * return items.get(index); }
	 *
	 * public List<FormulaIntroOrdineRVO> getItem() {
	 *
	 * // automatically grow List size List<FormulaIntroOrdineRVO> items =
	 * this.datiConfigBO.getFormulaIntroOrdineR(); if (items == null) { items =
	 * new ArrayList<FormulaIntroOrdineRVO>();
	 * this.datiConfigBO.setFormulaIntroOrdineR(items); }
	 *
	 * return items; }
	 */
	public Integer getSelectedFormulaIntroOrdineR() {
		return selectedFormulaIntroOrdineR;
	}

	public void setSelectedFormulaIntroOrdineR(
			Integer selectedFormulaIntroOrdineR) {
		this.selectedFormulaIntroOrdineR = selectedFormulaIntroOrdineR;
	}
}
