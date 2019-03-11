/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: almaviva
 * License Type: Purchased
 */
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;

/**
 * Tabella per l'invio e ricezione di mail da parte del polo
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_mail implements Serializable {

	private static final long serialVersionUID = -4744382131416097764L;

	public Tbf_mail() {
	}

	private int id;

	private String smtp;

	private String pop3;

	private String user_name;

	private String password;

	private String indirizzo;

	private String descrizione;

	private Character fl_forzatura;

	protected void setId(int value) {
		this.id = value;
	}

	public int getId() {
		return id;
	}

	public int getORMID() {
		return getId();
	}

	/**
	 * Server di invio mail
	 */
	public void setSmtp(String value) {
		this.smtp = value;
	}

	/**
	 * Server di invio mail
	 */
	public String getSmtp() {
		return smtp;
	}

	/**
	 * Server di ricezione mail
	 */
	public void setPop3(String value) {
		this.pop3 = value;
	}

	/**
	 * Server di ricezione mail
	 */
	public String getPop3() {
		return pop3;
	}

	/**
	 * Nome utente registrato con il server di posta
	 */
	public void setUser_name(String value) {
		this.user_name = value;
	}

	/**
	 * Nome utente registrato con il server di posta
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * password utente registrato con server di posta
	 */
	public void setPassword(String value) {
		this.password = value;
	}

	/**
	 * password utente registrato con server di posta
	 */
	public String getPassword() {
		return password;
	}

	public void setIndirizzo(String value) {
		this.indirizzo = value;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setDescrizione(String value) {
		this.descrizione = value;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String toString() {
		return String.valueOf(getId());
	}

	public Character getFl_forzatura() {
		return fl_forzatura;
	}

	public void setFl_forzatura(Character fl_forzatura) {
		this.fl_forzatura = fl_forzatura;
	}

}
