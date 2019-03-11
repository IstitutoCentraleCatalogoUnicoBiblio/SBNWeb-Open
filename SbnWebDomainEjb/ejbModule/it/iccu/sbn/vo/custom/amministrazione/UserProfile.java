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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.extension.cloning.Copyable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserProfile extends UniqueIdentifiableVO implements Copyable<UserProfile> {

	private static final long serialVersionUID = -3796557667067578480L;

	private boolean newPassword;
	private String userName;
	private String password;
	private String nome_cognome;
	private String polo;
	private String biblioteca;

	private String descrizioneBiblioteca;
	private String ticket;
	private int id_utente_professionale;
	private boolean remoto = false;
	private SbnProfileType profile;

	private List<String> _attivitaBib = new ArrayList<String>();

	private Map<String, Default> _default;
	private Map<String, Attivita> _attivita;
	private Map<String, ParametriAuthorityVO> _parametriAuthority;
	private Map<String, ParametriDocumentiVO> _parametriDocumenti;
	private Map<String, Attivita> _attivitaIndice;
	private Map<String, ParametriAuthorityVO> _parametriAuthorityIndice;
	private Map<String, ParametriDocumentiVO> _parametriDocumentiIndice;

	private Map<String, List<AttivitaAffiliata>> _attivitaAffiliate;


	private UserProfile(UserProfile u) {
		super();
		this.newPassword = u.newPassword;
		this.userName = u.userName;
		this.password = u.password;
		this.nome_cognome = u.nome_cognome;
		this.polo = u.polo;
		this.biblioteca = u.biblioteca;
		this.descrizioneBiblioteca = u.descrizioneBiblioteca;
		this.ticket = u.ticket;
		this.id_utente_professionale = u.id_utente_professionale;
		this._default = u._default;
		this._attivita = u._attivita;
		this._parametriAuthority = u._parametriAuthority;
		this._parametriDocumenti = u._parametriDocumenti;
		this._attivitaIndice = u._attivitaIndice;
		this._parametriAuthorityIndice = u._parametriAuthorityIndice;
		this._parametriDocumentiIndice = u._parametriDocumentiIndice;
		this._attivitaBib = u._attivitaBib;
		this.remoto = u.remoto;
		this.profile = u.profile;
		this._attivitaAffiliate = u._attivitaAffiliate;
	}

	public UserProfile() {
		super();
	}

	public String toString() {
		return "[utente: " + polo + biblioteca + userName + ", nome: "
				+ nome_cognome + ", ticket: " + ticket + "]";
	}

	public Map<String, Attivita> getAttivitaIndice() {
		return _attivitaIndice;
	}

	public void setAttivitaIndice(Map<String, Attivita> indice) {
		_attivitaIndice = indice;
	}

	public Map<String, ParametriAuthorityVO> getParametriAuthorityIndice() {
		return _parametriAuthorityIndice;
	}

	public void setParametriAuthorityIndice(Map<String, ParametriAuthorityVO> authorityIndice) {
		_parametriAuthorityIndice = authorityIndice;
	}

	public Map<String, ParametriDocumentiVO> getParametriDocumentiIndice() {
		return _parametriDocumentiIndice;
	}

	public void setParametriDocumentiIndice(Map<String, ParametriDocumentiVO> documentiIndice) {
		_parametriDocumentiIndice = documentiIndice;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getNome_cognome() {
		return nome_cognome;
	}

	public void setNome_cognome(String nome_cognome) {
		this.nome_cognome = nome_cognome;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = trimAndSet(userName);
	}

	public String getDescrizioneBiblioteca() {
		return descrizioneBiblioteca;
	}

	public void setDescrizioneBiblioteca(String descrizioneBiblioteca) {
		this.descrizioneBiblioteca = trimAndSet(descrizioneBiblioteca);
	}

	public boolean isNewPassword() {
		return newPassword;
	}

	public void setNewPassword(boolean newPassword) {
		this.newPassword = newPassword;
	}

	public Map<String, Default> getDefault() {
		return _default;
	}

	public void addDefault(String key, Default _default) {
		this._default.put(key, _default);
	}

	public void setDefault(Map<String, Default> _default) {
		this._default = _default;
	}

	public Map<String, Attivita> getAttivita() {
		return _attivita;
	}

	public void addAttivita(String key, Attivita _attivita) {
		this._attivita.put(key, _attivita);
	}

	public void setAttivita(Map<String, Attivita> _attivita) {
		this._attivita = _attivita;
	}

	public Map<String, ParametriAuthorityVO> getParametriAuthority() {
		return _parametriAuthority;
	}

	public void addParametriAuthority(String key,
			ParametriAuthorityVO _parametriAuthority) {
		this._parametriAuthority.put(key, _parametriAuthority);
	}

	public void setParametriAuthority(Map<String, ParametriAuthorityVO> _parametriAuthority) {
		this._parametriAuthority = _parametriAuthority;
	}

	public Map<String, ParametriDocumentiVO> getParametriDocumenti() {
		return _parametriDocumenti;
	}

	public void addParametriDocumenti(String key,
			ParametriDocumentiVO _parametriDocumenti) {
		this._parametriDocumenti.put(key, _parametriDocumenti);
	}

	public void setParametriDocumenti(Map<String, ParametriDocumentiVO> _parametriDocumenti) {
		this._parametriDocumenti = _parametriDocumenti;
	}

	public void setIdUtenteProfessionale(int id_utente_professionale) {
		this.id_utente_professionale = id_utente_professionale;
	}

	public int getIdUtenteProfessionale() {
		return id_utente_professionale;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setRemoto(boolean remoto) {
		this.remoto = remoto;
	}

	public boolean isRemoto() {
		return remoto;
	}

	public List<String> getAttivitaBib() {
		return _attivitaBib;
	}

	public void setAttivitaBib(List<String> attivitaBib) {
		_attivitaBib = attivitaBib;
	}

	public SbnProfileType getProfile() {
		return profile;
	}

	public void setProfile(SbnProfileType profile) {
		this.profile = profile;
	}

	public UserProfile copyThis() {
		return new UserProfile(this);
	}

	public Map<String, List<AttivitaAffiliata>> getAttivitaAffiliate() {
		return _attivitaAffiliate;
	}

	public void setAttivitaAffiliate(Map<String, List<AttivitaAffiliata>> _attivitaAffiliate) {
		this._attivitaAffiliate = _attivitaAffiliate;
	}

}
