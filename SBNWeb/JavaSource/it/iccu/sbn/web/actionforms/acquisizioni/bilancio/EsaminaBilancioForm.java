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
package it.iccu.sbn.web.actionforms.acquisizioni.bilancio;

import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaBilancioForm extends DynaValidatorForm {

	private static final long serialVersionUID = 874283048406938745L;
	private String codBibl;
	private String esercizio;
	private String capitolo;

	private double totBudget;

	private int[] radioImpegno;
	private int numImpegni;

	private String tipoImpegno;
	private List listaTipoImpegno;

	private BilancioVO bilancio;

	private List<BilancioDettVO> listaImpegni;

	private boolean sessione;

	private boolean conferma = false;
	private String pressioneBottone = null;

	private boolean disabilitaTutto=false;

	private List<ListaSuppBilancioVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;
	private boolean esaminaInibito=false;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		//super.reset(mapping, request);
		//  this.set("elencaImpegni",new BilancioDettVO[0]);
		//this.set("conferma",false);
		//this.set("disabilitaTutto",false);
		//this.set("numImpegni",0);

	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}


	public String getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}



	public List getListaTipoImpegno() {
		return listaTipoImpegno;
	}

	public void setListaTipoImpegno(List listaTipoImpegno) {
		this.listaTipoImpegno = listaTipoImpegno;
	}

	public int getNumImpegni() {
		return numImpegni;
	}

	public void setNumImpegni(int numImpegni) {
		this.numImpegni = numImpegni;
	}



	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public double getTotBudget() {
		return totBudget;
	}

	public void setTotBudget(double totBudget) {
		this.totBudget = totBudget;
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

	public boolean isEnableScorrimento() {
		return enableScorrimento;
	}

	public void setEnableScorrimento(boolean enableScorrimento) {
		this.enableScorrimento = enableScorrimento;
	}

	public List<ListaSuppBilancioVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(List<ListaSuppBilancioVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public String getPressioneBottone() {
		return pressioneBottone;
	}

	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public BilancioVO getBilancio() {
		return bilancio;
	}

	public void setBilancio(BilancioVO bilancio) {
		this.bilancio = bilancio;
	}

	public List<BilancioDettVO> getListaImpegni() {
		return listaImpegni;
	}

	public void setListaImpegni(List<BilancioDettVO> listaImpegni) {
		this.listaImpegni = listaImpegni;
	}

	public int[] getRadioImpegno() {
		return radioImpegno;
	}

	public void setRadioImpegno(int[] radioImpegno) {
		this.radioImpegno = radioImpegno;
	}

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}




}
