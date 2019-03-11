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
package it.iccu.sbn.web.actionforms.acquisizioni.profiliacquisto;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaProfiloForm extends DynaValidatorForm  {


	private static final long serialVersionUID = -4741899531634405487L;
	private List listaLingue;
	private String lingue;
	private List listaPaesi;
	private String paesi;

	private int[] radioForn;
	private int numForn;

	private boolean caricamentoIniziale=false;

	private StrutturaProfiloVO datiProfilo;
	private List<StrutturaTerna> elencaFornitori;
	private boolean sessione;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;
	private List<ListaSuppProfiloVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;
	private boolean esaminaInibito=false;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// super.reset(mapping, request);
		// this.set("elencaRigheFatt",new StrutturaFatturaVO[0]);
		this.set("conferma",false);
		this.set("disabilitaTutto",false);
		// this.set("numRigheFatt",0);
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public StrutturaProfiloVO getDatiProfilo() {
		return datiProfilo;
	}

	public void setDatiProfilo(StrutturaProfiloVO datiProfilo) {
		this.datiProfilo = datiProfilo;
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

	public String getLingue() {
		return lingue;
	}

	public void setLingue(String lingue) {
		this.lingue = lingue;
	}

	public List<ListaSuppProfiloVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(List<ListaSuppProfiloVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
	}

	public List getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List listaLingue) {
		this.listaLingue = listaLingue;
	}

	public List getListaPaesi() {
		return listaPaesi;
	}

	public void setListaPaesi(List listaPaesi) {
		this.listaPaesi = listaPaesi;
	}

	public int getNumForn() {
		return numForn;
	}

	public void setNumForn(int numForn) {
		this.numForn = numForn;
	}

	public String getPaesi() {
		return paesi;
	}

	public void setPaesi(String paesi) {
		this.paesi = paesi;
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

	public List<StrutturaTerna> getElencaFornitori() {
		return elencaFornitori;
	}

	public void setElencaFornitori(List<StrutturaTerna> elencaFornitori) {
		this.elencaFornitori = elencaFornitori;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public int[] getRadioForn() {
		return radioForn;
	}

	public void setRadioForn(int[] radioForn) {
		this.radioForn = radioForn;
	}

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}




}
