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
package it.iccu.sbn.ejb.vo.servizi.utenti;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.extension.auth.utente.UtenteData;

import java.util.Comparator;
import java.util.Date;

public class UtenteBaseVO extends SerializableVO implements UtenteData {

	private static final long serialVersionUID = 4593777783350999606L;

	public static final Comparator<UtenteBaseVO> ORDINAMENTO_COGNOME_NOME = new Comparator<UtenteBaseVO>() {
		public int compare(UtenteBaseVO u1, UtenteBaseVO u2) {
			return u1.getOrdUtente().compareTo(u2.getOrdUtente());
		}
	};

	private String codPolo;
	private String codBib;
	private String codUtente;
	private String cognome;
	private String nome;
	private char sesso;
	private Date dataNascita;
	private String luogoNascita;
	private String codiceFiscale;

	private String mail1;
	private String mail2;

	private int idUtente;
	private String password;
	private String personaGiuridica;
	private String tipoEnte;

	private String ordUtente;

	private String tipoUtente;

	public UtenteBaseVO() {
		super();
	}

	public UtenteBaseVO(String codPolo, String codBib, String codUtente, String cognome, String nome, char sesso,
			Date dataNascita, String luogoNascita, String codiceFiscale) {
		super();
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.codUtente = trimAndSet(codUtente);
		this.cognome = trimAndSet(cognome);
		this.nome = trimAndSet(nome);
		this.sesso = sesso;
		this.dataNascita = dataNascita;
		this.luogoNascita = trimAndSet(luogoNascita);
		this.codiceFiscale = trimAndSet(codiceFiscale);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		final UtenteBaseVO other = (UtenteBaseVO) obj;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;

		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;

		if (codUtente == null) {
			if (other.codUtente != null)
				return false;
		} else if (!codUtente.equals(other.codUtente))
			return false;

		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;

		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;

		if (luogoNascita == null) {
			if (other.luogoNascita != null)
				return false;
		} else if (!luogoNascita.equals(other.luogoNascita))
			return false;

		if (codiceFiscale == null) {
			if (other.codiceFiscale != null)
				return false;
		} else if (!codiceFiscale.equals(other.codiceFiscale))
			return false;

		if (dataNascita == null) {
			if (other.dataNascita != null)
				return false;
		} else if (!dataNascita.equals(other.dataNascita))
			return false;

		return (this.sesso == other.sesso);
	}

	/* (non-Javadoc)
	 * @see it.iccu.sbn.ejb.vo.servizi.utenti.UtenteData#getCodPolo()
	 */
	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	/* (non-Javadoc)
	 * @see it.iccu.sbn.ejb.vo.servizi.utenti.UtenteData#getCodBib()
	 */
	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	/* (non-Javadoc)
	 * @see it.iccu.sbn.ejb.vo.servizi.utenti.UtenteData#getCodUtente()
	 */
	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = trimAndSet(codUtente);
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = trimAndSet(cognome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public char getSesso() {
		return sesso;
	}

	public void setSesso(char sesso) {
		this.sesso = sesso;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = trimAndSet(luogoNascita);
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = trimAndSet(codiceFiscale);
	}

	public String getCognomeNome() {
		return trimOrEmpty(this.cognome) + " " + trimOrEmpty(this.nome);
	}

	public String getMail1() {
		return mail1;
	}

	public void setMail1(String mail1) {
		this.mail1 = trimAndSet(mail1);
	}

	public String getMail2() {
		return mail2;
	}

	public void setMail2(String mail2) {
		this.mail2 = trimAndSet(mail2);
	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = trimAndSet(password);
	}

	public String getPersonaGiuridica() {
		return personaGiuridica;
	}

	public void setPersonaGiuridica(String personaGiuridica) {
		this.personaGiuridica = personaGiuridica;
	}

	public String getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(String tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	public String getOrdUtente() {
		return ordUtente;
	}

	public void setOrdUtente(String ordUtente) {
		this.ordUtente = trimOrEmpty(ordUtente);
	}

	public boolean isEnte() {
		return ValidazioneDati.in(this.personaGiuridica, "s", "S");
	}

	public String getEmail() {
		return isFilled(mail1) ? mail1 : mail2;
	}

	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
}
