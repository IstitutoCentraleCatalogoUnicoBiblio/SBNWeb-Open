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
package it.iccu.sbn.web.actionforms.acquisizioni.suggerimenti;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class EsaminaSugBiblForm extends SugBiblBaseForm {


	private static final long serialVersionUID = 3793367491662015900L;
	private String statoSuggerimento;
	private List listaStatoSuggerimento;

	private boolean caricamentoIniziale=false;

	private boolean sessione;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;
	private List<ListaSuppSuggerimentoVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;
	private boolean esaminaInibito=false;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public List getListaStatoSuggerimento() {
		return listaStatoSuggerimento;
	}

	public void setListaStatoSuggerimento(List listaStatoSuggerimento) {
		this.listaStatoSuggerimento = listaStatoSuggerimento;
	}

	public String getStatoSuggerimento() {
		return statoSuggerimento;
	}

	public void setStatoSuggerimento(String statoSuggerimento) {
		this.statoSuggerimento = statoSuggerimento;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
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

	public List<ListaSuppSuggerimentoVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(
			List<ListaSuppSuggerimentoVO> listaDaScorrere) {
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

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}

}
