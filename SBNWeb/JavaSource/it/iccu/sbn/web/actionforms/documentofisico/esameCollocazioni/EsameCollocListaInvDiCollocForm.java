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
package it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class EsameCollocListaInvDiCollocForm extends ActionForm {



	private static final long serialVersionUID = 4481224421193683756L;
	private List listaInv = new ArrayList();
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSez;
	private int keyLoc;
	private String selectedInv;
	private boolean disable;
	private boolean noInv;
	private boolean sessione;
	private String provSceltaColl;
	private String ticket;

	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public List getListaInv() {
		return listaInv;
	}
	public void setListaInv(List listaInv) {
		this.listaInv = listaInv;
	}
	public boolean isNoInv() {
		return noInv;
	}
	public void setNoInv(boolean noInv) {
		this.noInv = noInv;
	}
	public String getSelectedInv() {
		return selectedInv;
	}
	public void setSelectedInv(String selectedInv) {
		this.selectedInv = selectedInv;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getProvSceltaColl() {
		return provSceltaColl;
	}
	public void setProvSceltaColl(String provSceltaColl) {
		this.provSceltaColl = provSceltaColl;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public int getKeyLoc() {
		return keyLoc;
	}
	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

}
