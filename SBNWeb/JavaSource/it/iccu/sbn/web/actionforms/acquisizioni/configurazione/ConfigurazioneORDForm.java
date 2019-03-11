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

import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ConfigurazioneORDForm extends ActionForm implements AcquisizioniBaseFormIntf {


	private static final long serialVersionUID = -2358819654477133337L;
	private ConfigurazioneORDVO datiConfigORD;
	private String profAcqFornDes = "";
	private String fornitoreDes = "";
	private String gestBil; // VARIABILE LATO CLIENT PER CHECK
	private boolean enableBil; // VARIABILE LATO CLIENT PER CHECK
	private String gestSez; // VARIABILE LATO CLIENT PER CHECK
	private boolean enableSez; // VARIABILE LATO CLIENT PER CHECK
	private String gestProf; // VARIABILE LATO CLIENT PER CHECK
	private boolean enableProf; // VARIABILE LATO CLIENT PER CHECK
	private List listaTipoImpegno;
	private int radioForn;

	private String ordAperti; // VARIABILE LATO CLIENT PER CHECK
	private String ordChiusi; // VARIABILE LATO CLIENT PER CHECK
	private String ordAnnullati; // VARIABILE LATO CLIENT PER CHECK
	private List listaAllineamento;

	private ComboVO fornitore = new ComboVO();

	private boolean sessione;

	private boolean conferma = false;

	private boolean disabilitaTutto = false;

	private boolean caricamentoIniziale = false;

	private boolean submitDinamico = false;

	// private String[] fields = {"","", ""};

	// private List items = new ArrayList();
	private String[] fields = { "A" };

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	private String datoAgg;
	private FornitoreVO fornitoreVO;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// super.reset(mapping, request);
		// this.set("numAutomatica",false);
		// this.set("selectedDatiFineStampa",new String[0]);
		// this.set("selectedDatiIntest",new String[0]);
		// this.gestBil=null;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public ConfigurazioneORDVO getDatiConfigORD() {
		return datiConfigORD;
	}

	public void setDatiConfigORD(ConfigurazioneORDVO datiConfigORD) {
		this.datiConfigORD = datiConfigORD;
	}

	public String getDatoAgg() {
		return datoAgg;
	}

	public void setDatoAgg(String datoAgg) {
		this.datoAgg = datoAgg;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public boolean isEnableBil() {
		return enableBil;
	}

	public void setEnableBil(boolean enableBil) {
		this.enableBil = enableBil;
	}

	public boolean isEnableProf() {
		return enableProf;
	}

	public void setEnableProf(boolean enableProf) {
		this.enableProf = enableProf;
	}

	public boolean isEnableSez() {
		return enableSez;
	}

	public void setEnableSez(boolean enableSez) {
		this.enableSez = enableSez;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getFornitoreDes() {
		return fornitoreDes;
	}

	public void setFornitoreDes(String fornitoreDes) {
		this.fornitoreDes = fornitoreDes;
	}

	public String getProfAcqFornDes() {
		return profAcqFornDes;
	}

	public void setProfAcqFornDes(String profAcqFornDes) {
		this.profAcqFornDes = profAcqFornDes;
	}

	public List getListaTipoImpegno() {
		return listaTipoImpegno;
	}

	public void setListaTipoImpegno(List listaTipoImpegno) {
		this.listaTipoImpegno = listaTipoImpegno;
	}

	public int getRadioForn() {
		return radioForn;
	}

	public void setRadioForn(int radioForn) {
		this.radioForn = radioForn;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public String getGestBil() {
		return gestBil;
	}

	public void setGestBil(String gestBil) {
		this.gestBil = gestBil;
	}

	public String getGestProf() {
		return gestProf;
	}

	public void setGestProf(String gestProf) {
		this.gestProf = gestProf;
	}

	public String getGestSez() {
		return gestSez;
	}

	public void setGestSez(String gestSez) {
		this.gestSez = gestSez;
	}

	public List getListaAllineamento() {
		return listaAllineamento;
	}

	public void setListaAllineamento(List listaAllineamento) {
		this.listaAllineamento = listaAllineamento;
	}

	public String getOrdAnnullati() {
		return ordAnnullati;
	}

	public void setOrdAnnullati(String ordAnnullati) {
		this.ordAnnullati = ordAnnullati;
	}

	public String getOrdAperti() {
		return ordAperti;
	}

	public void setOrdAperti(String ordAperti) {
		this.ordAperti = ordAperti;
	}

	public String getOrdChiusi() {
		return ordChiusi;
	}

	public void setOrdChiusi(String ordChiusi) {
		this.ordChiusi = ordChiusi;
	}

	public boolean isSubmitDinamico() {
		return submitDinamico;
	}

	public void setSubmitDinamico(boolean submitDinamico) {
		this.submitDinamico = submitDinamico;
	}

	public String getFornitore() {
		return fornitore.getDescrizione();
	}

	public void setFornitore(String dsFornitore) {
		fornitore.setDescrizione(dsFornitore);
	}

	public String getCodFornitore() {
		return fornitore.getCodice();
	}

	public void setCodFornitore(String codFornitore) {
		fornitore.setCodice(codFornitore);
	}

	public FornitoreVO getFornitoreVO() {
		return fornitoreVO;
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		this.fornitoreVO = fornitoreVO;
	}

}
