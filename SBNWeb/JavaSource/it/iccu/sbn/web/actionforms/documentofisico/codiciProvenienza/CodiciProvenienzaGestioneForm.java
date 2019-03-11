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
package it.iccu.sbn.web.actionforms.documentofisico.codiciProvenienza;

import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;

import org.apache.struts.action.ActionForm;

public class CodiciProvenienzaGestioneForm extends ActionForm {


	private static final long serialVersionUID = 760037838254899721L;
	private ProvenienzaInventarioVO recProvInv = new ProvenienzaInventarioVO();
	private String codBib;
	private String codPolo;
	private String descrBib;
	private boolean disableBib;
	private boolean disableDescr;
	private boolean sessione;
	private boolean conferma;
	private boolean date;
	private String tastoOk;
	private String ticket;

	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public ProvenienzaInventarioVO getRecProvInv() {
		return recProvInv;
	}
	public void setRecProvInv(ProvenienzaInventarioVO recProvInv) {
		this.recProvInv = recProvInv;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTastoOk() {
		return tastoOk;
	}
	public void setTastoOk(String tastoOk) {
		this.tastoOk = tastoOk;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isDate() {
		return date;
	}
	public void setDate(boolean date) {
		this.date = date;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public boolean isDisableBib() {
		return disableBib;
	}
	public void setDisableBib(boolean disableBib) {
		this.disableBib = disableBib;
	}
	public boolean isDisableDescr() {
		return disableDescr;
	}
	public void setDisableDescr(boolean disableDescr) {
		this.disableDescr = disableDescr;
	}
}
