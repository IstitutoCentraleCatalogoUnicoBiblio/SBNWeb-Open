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
package it.iccu.sbn.web.actionforms.acquisizioni.cambi;

import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EsaminaCambioForm extends ActionForm {

	private static final long serialVersionUID = 4882347750235313548L;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private String valuta;
	List listaValuta;
	private boolean sessione;
	//private ListaSuppCambioVO datiCambio;
	private CambioVO datiCambio=new CambioVO() ;
	//private ListaSuppCambioVO datiRicerca;
	private List listaCambi;
	private boolean EnableScorrimento;
	private int posizioneScorrimento;
	private List<ListaSuppCambioVO> listaDaScorrere;
	private boolean disabilitaTutto=false;
	private boolean esaminaInibito=false;
	private boolean valRifer = false;
	private String  valRiferChk; // VARIABILE LATO CLIENT PER CHECK
	private boolean submitDinamico = false;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
/*		try {
			this.datiCambio.valida();
		} catch (ValidationException e) {
			e.printStackTrace();*/
/*			switch (e.getError()) {
			case 0:// ValidationException.erroreSoggetto:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.soggettoerr"));
				break;*/
		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo
		//}
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.valRifer=false;
	}


	public List getListaValuta() {
		return listaValuta;
	}

	public void setListaValuta(List listaValuta) {
		this.listaValuta = listaValuta;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}


	public List getListaCambi() {
		return listaCambi;
	}


	public void setListaCambi(List listaCambi) {
		this.listaCambi = listaCambi;
	}


	public boolean isEnableScorrimento() {
		return EnableScorrimento;
	}


	public void setEnableScorrimento(boolean enableScorrimento) {
		EnableScorrimento = enableScorrimento;
	}


	public CambioVO getDatiCambio() {
		return datiCambio;
	}


	public void setDatiCambio(CambioVO datiCambio) {
		this.datiCambio = datiCambio;
	}


	public List<ListaSuppCambioVO> getListaDaScorrere() {
		return listaDaScorrere;
	}


	public void setListaDaScorrere(List<ListaSuppCambioVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
	}


	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}


	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}


	public boolean isConferma() {
		return conferma;
	}


	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}


	public String getPressioneBottone() {
		return pressioneBottone;
	}


	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}


	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}


	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}


	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}


	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}


	public boolean isValRifer() {
		return valRifer;
	}


	public void setValRifer(boolean valRifer) {
		this.valRifer = valRifer;
	}

	public String getValRiferChk() {
		return valRiferChk;
	}

	public void setValRiferChk(String valRiferChk) {
		this.valRiferChk = valRiferChk;
	}

	public boolean isSubmitDinamico() {
		return submitDinamico;
	}

	public void setSubmitDinamico(boolean submitDinamico) {
		this.submitDinamico = submitDinamico;
	}









}
