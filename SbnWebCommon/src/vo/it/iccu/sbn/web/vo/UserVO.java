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

public interface UserVO {

	public abstract String getFirmaUtente();

	public abstract String getCodBib();

	public abstract void setCodBib(String codBib);

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract String getUserId();

	public abstract void setUserId(String userId);

	public abstract String getBiblioteca();

	public abstract void setBiblioteca(String biblioteca);

	public abstract String getNome_cognome();

	public abstract void setNome_cognome(String nome_cognome);

	public abstract String getCodPolo();

	public abstract void setCodPolo(String codPolo);

	public abstract String getTicket();

	public abstract void setTicket(String ticket);

	public abstract boolean isNewPassword();

	public abstract void setNewPassword(boolean newPassword);

	public abstract void setIdUtenteProfessionale(int id_utente_professionale);

	public abstract int getIdUtenteProfessionale();

	public abstract boolean isRoot();

	public abstract void setRemoto(boolean remoto);

	public abstract boolean isRemoto();

}
