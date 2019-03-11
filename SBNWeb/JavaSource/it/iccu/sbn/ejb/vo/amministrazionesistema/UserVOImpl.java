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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.UserVO;

public class UserVOImpl extends SerializableVO implements UserVO {

	private static final long serialVersionUID = -3888288945944073617L;
	private boolean newPassword;
	private String userId;
	private String password;
	private String nome_cognome;
	private String codBib;
	private String codPolo;
	private String biblioteca;
	private String ticket;
	private int id_utente_professionale;
	private boolean remoto = false;

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getFirmaUtente()
	 */
	public String getFirmaUtente() {
		return this.codPolo + this.codBib + this.userId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getCodBib()
	 */
	public String getCodBib() {
		return codBib;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setCodBib(java.lang.String)
	 */
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getPassword()
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		this.password = trimAndSet(password);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getUserId()
	 */
	public String getUserId() {
		return userId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setUserId(java.lang.String)
	 */
	public void setUserId(String userId) {
		this.userId = trimAndSet(userId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getBiblioteca()
	 */
	public String getBiblioteca() {
		return biblioteca;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setBiblioteca(java.lang.String)
	 */
	public void setBiblioteca(String biblioteca) {
		this.biblioteca = trimAndSet(biblioteca);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getNome_cognome()
	 */
	public String getNome_cognome() {
		return nome_cognome;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setNome_cognome(java.lang.String)
	 */
	public void setNome_cognome(String nome_cognome) {
		this.nome_cognome = nome_cognome;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getCodPolo()
	 */
	public String getCodPolo() {
		return codPolo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setCodPolo(java.lang.String)
	 */
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#getTicket()
	 */
	public String getTicket() {
		return ticket;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setTicket(java.lang.String)
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#isNewPassword()
	 */
	public boolean isNewPassword() {
		return newPassword;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.ejb.vo.amministrazionesistema.UserVO#setNewPassword(boolean)
	 */
	public void setNewPassword(boolean newPassword) {
		this.newPassword = newPassword;
	}

	public int getIdUtenteProfessionale() {
		return this.id_utente_professionale;
	}

	public void setIdUtenteProfessionale(int id_utente_professionale) {
		this.id_utente_professionale = id_utente_professionale;
	}

	public boolean isRoot() {
		return ValidazioneDati.equalsIgnoreCase(userId, "ROOT");
	}

	public boolean isRemoto() {
		return remoto;
	}

	public void setRemoto(boolean remoto) {
		this.remoto = remoto;
	}

}
