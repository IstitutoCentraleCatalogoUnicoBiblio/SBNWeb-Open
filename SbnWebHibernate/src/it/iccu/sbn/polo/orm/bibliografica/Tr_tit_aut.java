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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * AUTORI DI OGGETTI BIBLIOGRAFICI
 */
/**
 * ORM-Persistable Class
 */
public class Tr_tit_aut implements Serializable {

	private static final long serialVersionUID = -3631367143900312514L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_aut))
			return false;
		Tr_tit_aut tr_tit_aut = (Tr_tit_aut)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tr_tit_aut.getB().getORMID()))
			return false;
		if (getV() == null)
			return false;
		if (!getV().getORMID().equals(tr_tit_aut.getV().getORMID()))
			return false;
		if (getTp_responsabilita() != tr_tit_aut.getTp_responsabilita())
			return false;
		if (getCd_relazione() != null && !getCd_relazione().equals(tr_tit_aut.getCd_relazione()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		if (getV() != null) {
			hashcode = hashcode + getV().getORMID().hashCode();
		}
		hashcode = hashcode + getTp_responsabilita();
		hashcode = hashcode + getCd_relazione().hashCode();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_autore v;

	private char tp_responsabilita;

	private String cd_relazione;

	private String nota_tit_aut;

	private char fl_incerto;

	private char fl_superfluo;

	private String cd_strumento_mus;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char fl_condiviso;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	public void setTp_responsabilita(char value) {
		this.tp_responsabilita = value;
	}

	public char getTp_responsabilita() {
		return tp_responsabilita;
	}

	public void setCd_relazione(String value) {
		this.cd_relazione = value;
	}

	public String getCd_relazione() {
		return cd_relazione;
	}

	public void setNota_tit_aut(String value) {
		this.nota_tit_aut = value;
	}

	public String getNota_tit_aut() {
		return nota_tit_aut;
	}

	/**
	 * Indicatore di legame autore incerto, S=incerto, spazio=certo
	 */
	public void setFl_incerto(char value) {
		this.fl_incerto = value;
	}

	/**
	 * Indicatore di legame autore incerto, S=incerto, spazio=certo
	 */
	public char getFl_incerto() {
		return fl_incerto;
	}

	/**
	 * Indicatore di legame autore non obbligatorio, S=non obb, spazio=obbligatorio
	 */
	public void setFl_superfluo(char value) {
		this.fl_superfluo = value;
	}

	/**
	 * Indicatore di legame autore non obbligatorio, S=non obb, spazio=obbligatorio
	 */
	public char getFl_superfluo() {
		return fl_superfluo;
	}

	public void setCd_strumento_mus(String value) {
		this.cd_strumento_mus = value;
	}

	public String getCd_strumento_mus() {
		return cd_strumento_mus;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * Timestamp di inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * Timestamp di inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public String getUte_var() {
		return ute_var;
	}

	/**
	 * Timestamp di variazione
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * Timestamp di variazione
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public char getFl_canc() {
		return fl_canc;
	}

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public void setFl_condiviso(char value) {
		this.fl_condiviso = value;
	}

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public char getFl_condiviso() {
		return fl_condiviso;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
	}

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setV(it.iccu.sbn.polo.orm.bibliografica.Tb_autore value) {
		this.v = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_autore getV() {
		return v;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getV() == null) ? "" : String.valueOf(getV().getORMID())) + " " + getTp_responsabilita() + " " + getCd_relazione());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}


}
