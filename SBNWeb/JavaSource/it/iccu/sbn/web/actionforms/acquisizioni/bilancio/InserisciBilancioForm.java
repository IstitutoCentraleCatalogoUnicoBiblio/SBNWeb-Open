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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;


public class InserisciBilancioForm extends DynaValidatorForm {


	private static final long serialVersionUID = 4452551200316148594L;
	private String codBibl;
	private String esercizio;
	private String capitolo;

	private double totBudget;

	private int[] radioImpegno;
	private int numImpegni;

	private String tipoImpegno;
	private List listaTipoImpegno;

	private BilancioVO bilancio= new BilancioVO();
	private List<BilancioDettVO> elencaImpegni;
	private boolean sessione;

	private boolean conferma = false;
	private String pressioneBottone = null;

	private boolean disabilitaTutto=false;

	private boolean visibilitaIndietroLS=false;


	public void reset(ActionMapping mapping, HttpServletRequest request) {
		//super.reset(mapping, request);
		//  this.set("elencaImpegni",new BilancioDettVO[0]);
		//this.set("visibilitaIndietroLS",false);
		this.set("conferma",false);
		this.set("disabilitaTutto",false);
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

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public BilancioVO getBilancio() {
		return bilancio;
	}

	public void setBilancio(BilancioVO bilancio) {
		this.bilancio = bilancio;
	}

	public List<BilancioDettVO> getElencaImpegni() {
		return elencaImpegni;
	}

	public void setElencaImpegni(List<BilancioDettVO> elencaImpegni) {
		this.elencaImpegni = elencaImpegni;
	}


	public int[] getRadioImpegno() {
		return radioImpegno;
	}


	public void setRadioImpegno(int[] radioImpegno) {
		this.radioImpegno = radioImpegno;
	}







}
