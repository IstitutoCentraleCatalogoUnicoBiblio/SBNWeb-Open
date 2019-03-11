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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class InserisciCambioForm extends ActionForm {

	private static final long serialVersionUID = -2514202644107187547L;
	private boolean conferma = false;
	private String valuta;
	List listaValuta;
	private String valutaInserita = "";
	private boolean sessione;
	private CambioVO datiCambio = new CambioVO();
	private String cambiaValuta = "0";
	private boolean valRifer = false;
	private String valRiferChk; // VARIABILE LATO CLIENT PER CHECK
	private boolean submitDinamico = false;

	private boolean forzaValutaRif;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.valRifer = false;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public CambioVO getDatiCambio() {
		return datiCambio;
	}

	public void setDatiCambio(CambioVO datiCambio) {
		this.datiCambio = datiCambio;
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

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getCambiaValuta() {
		return cambiaValuta;
	}

	public void setCambiaValuta(String cambiaValuta) {
		this.cambiaValuta = cambiaValuta;
	}

	public String getValutaInserita() {
		return valutaInserita;
	}

	public void setValutaInserita(String valutaInserita) {
		this.valutaInserita = valutaInserita;
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

	public boolean isForzaValutaRif() {
		return forzaValutaRif;
	}

	public void setForzaValutaRif(boolean forzaValutaRif) {
		this.forzaValutaRif = forzaValutaRif;
	}

}
