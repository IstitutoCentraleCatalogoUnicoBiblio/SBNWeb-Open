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
package it.iccu.sbn.web.actionforms.acquisizioni.sezioni;

import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RigheVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EsaminaSpesaForm extends ActionForm {

	private static final long serialVersionUID = -4691592926562435009L;

	private SezioneVO sezione;

	private int numBilanci=0;
	private int numImpegni=0;
	private int numTipologie;
	private String dataDa;
	private String dataA;
	private String esercizio;


	private List<BilancioVO> listaBilanci2;

	private List<StrutturaTerna> tipologie;

	private List<RigheVO> listaBilanci;
	private List<RigheVO> listaTipologie;


	private boolean sessione;
	private boolean gestBil=true;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public String getDataA() {
		return dataA;
	}

	public void setDataA(String dataA) {
		this.dataA = dataA;
	}

	public String getDataDa() {
		return dataDa;
	}

	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}

	public String getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}

	public List<RigheVO> getListaBilanci() {
		return listaBilanci;
	}

	public void setListaBilanci(List<RigheVO> listaBilanci) {
		this.listaBilanci = listaBilanci;
	}

	public List<BilancioVO> getListaBilanci2() {
		return listaBilanci2;
	}

	public void setListaBilanci2(List<BilancioVO> listaBilanci2) {
		this.listaBilanci2 = listaBilanci2;
	}


	public int getNumBilanci() {
		return numBilanci;
	}

	public void setNumBilanci(int numBilanci) {
		this.numBilanci = numBilanci;
	}

	public int getNumImpegni() {
		return numImpegni;
	}

	public void setNumImpegni(int numImpegni) {
		this.numImpegni = numImpegni;
	}

	public int getNumTipologie() {
		return numTipologie;
	}

	public void setNumTipologie(int numTipologie) {
		this.numTipologie = numTipologie;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public SezioneVO getSezione() {
		return sezione;
	}

	public void setSezione(SezioneVO sezione) {
		this.sezione = sezione;
	}

	public List<StrutturaTerna> getTipologie() {
		return tipologie;
	}

	public void setTipologie(List<StrutturaTerna> tipologie) {
		this.tipologie = tipologie;
	}

	public List<RigheVO> getListaTipologie() {
		return listaTipologie;
	}

	public void setListaTipologie(List<RigheVO> listaTipologie) {
		this.listaTipologie = listaTipologie;
	}

	public boolean isGestBil() {
		return gestBil;
	}

	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}






}
