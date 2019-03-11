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
package it.iccu.sbn.web.actionforms.acquisizioni.documenti;

import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EsaminaDocForm extends ActionForm {

	private static final long serialVersionUID = 6413096586446709453L;
	private String statoSuggerimento;
	private List listaStatoSuggerimento;
	private String lingue;
	private List listaLingue;
	private String paesi;
	private List listaPaesi;
	private DocumentoVO datiDocumento;

	private boolean caricamentoIniziale=false;

	private boolean sessione;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;
	private List<ListaSuppDocumentoVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;
	private boolean esaminaInibito=false;
	private String[] pulsanti;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
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


	public DocumentoVO getDatiDocumento() {
		return datiDocumento;
	}


	public void setDatiDocumento(DocumentoVO datiDocumento) {
		this.datiDocumento = datiDocumento;
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


	public List<ListaSuppDocumentoVO> getListaDaScorrere() {
		return listaDaScorrere;
	}


	public void setListaDaScorrere(List<ListaSuppDocumentoVO> listaDaScorrere) {
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


	public List getListaStatoSuggerimento() {
		return listaStatoSuggerimento;
	}


	public void setListaStatoSuggerimento(List listaStatoSuggerimento) {
		this.listaStatoSuggerimento = listaStatoSuggerimento;
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


	public String getStatoSuggerimento() {
		return statoSuggerimento;
	}


	public void setStatoSuggerimento(String statoSuggerimento) {
		this.statoSuggerimento = statoSuggerimento;
	}


	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}


	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}


	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}


	public String[] getPulsanti() {
		return pulsanti;
	}

}
