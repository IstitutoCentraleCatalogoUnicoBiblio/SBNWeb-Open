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

import org.apache.struts.action.ActionForm;

public class EsameCollocConsistenzaIndiceForm extends ActionForm {



	private static final long serialVersionUID = 2667706229910156681L;
	private String codBib;
	private String descrBib;
	private String bid;
	private String titoloBid;
	private String consistenzaEsemplare;
	private String consistenzaIndice;
	private boolean mutilo;
	private boolean sessione;
	private boolean disable;
	private String action;
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
	public boolean isMutilo() {
		return mutilo;
	}
	public void setMutilo(boolean mutilo) {
		this.mutilo = mutilo;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getTitoloBid() {
		return titoloBid;
	}
	public void setTitoloBid(String titoloBid) {
		this.titoloBid = titoloBid;
	}
	public String getConsistenzaIndice() {
		return consistenzaIndice;
	}
	public void setConsistenzaIndice(String consistenzaIndice) {
		this.consistenzaIndice = consistenzaIndice;
	}
	public String getConsistenzaEsemplare() {
		return consistenzaEsemplare;
	}
	public void setConsistenzaEsemplare(String consistenzaEsemplare) {
		this.consistenzaEsemplare = consistenzaEsemplare;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
