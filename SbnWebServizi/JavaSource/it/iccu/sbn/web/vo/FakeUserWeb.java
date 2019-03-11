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
package it.iccu.sbn.web.vo;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.integration.bd.Ticket;
import it.iccu.sbn.web2.util.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class FakeUserWeb extends SerializableVO implements UserVO {

	private static final long serialVersionUID = -1179067148454124175L;
	private String codPolo;
	private String ticket;
	private String codBib;
	private String userId;
	private boolean remoto = false;

	public FakeUserWeb(String cdPolo) {
		this.codPolo = cdPolo;
		this.codBib = Constants.UTENTE_WEB_FAKE_BIB;
		this.userId = Constants.UTENTE_WEB_TICKET;
		try {
			this.ticket = Ticket.getUtenteWebTicket(codPolo, codBib, InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public String getBiblioteca() {
		return null;
	}

	public String getCodBib() {
		return null;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public String getFirmaUtente() {
		return this.codPolo + this.codBib + this.userId;
	}

	public int getIdUtenteProfessionale() {
		return 0;
	}

	public String getNome_cognome() {
		return null;
	}

	public String getPassword() {
		return null;
	}

	public String getTicket() {
		return ticket;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isNewPassword() {
		return false;
	}

	public boolean isRoot() {
		return false;
	}

	public void setBiblioteca(String biblioteca) {

	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public void setCodPolo(String codPolo) {

	}

	public void setIdUtenteProfessionale(int idUtenteProfessionale) {

	}

	public void setNewPassword(boolean newPassword) {

	}

	public void setNome_cognome(String nomeCognome) {

	}

	public void setPassword(String password) {

	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setUserId(String userId) {

	}

	public boolean isRemoto() {
		return remoto;
	}

	public void setRemoto(boolean remoto) {
		this.remoto = remoto;
	}

}
