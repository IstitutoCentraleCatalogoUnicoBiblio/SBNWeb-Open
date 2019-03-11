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
 * COLLEGAMENTI OGGETTI BIBLIOGRAFICI
 */
/**
 * ORM-Persistable Class
 */
public class Tr_tit_tit implements Serializable {

	private static final long serialVersionUID = 9080391199146836762L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_tit))
			return false;
		Tr_tit_tit tr_tit_tit = (Tr_tit_tit)aObj;
		if (getBid_base() == null)
			return false;
		if (!getBid_base().getORMID().equals(tr_tit_tit.getBid_base().getORMID()))
			return false;
		if (getBid_coll() == null)
			return false;
		if (!getBid_coll().getORMID().equals(tr_tit_tit.getBid_coll().getORMID()))
			return false;
		if (getTp_legame() != null && !getTp_legame().equals(tr_tit_tit.getTp_legame()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBid_base() != null) {
			hashcode = hashcode + getBid_base().getORMID().hashCode();
		}
		if (getBid_coll() != null) {
			hashcode = hashcode + getBid_coll().getORMID().hashCode();
		}
		hashcode = hashcode + getTp_legame().hashCode();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo bid_base;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo bid_coll;

	private String tp_legame;

	private Character tp_legame_musica;

	private char cd_natura_base;

	private char cd_natura_coll;

	private String sequenza;

	private String nota_tit_tit;

	private String sequenza_musica;

	private String sici;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char fl_condiviso;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	public void setTp_legame(String value) {
		this.tp_legame = value;
	}

	public String getTp_legame() {
		return tp_legame;
	}

	public void setTp_legame_musica(char value) {
		setTp_legame_musica(new Character(value));
	}

	public void setTp_legame_musica(Character value) {
		this.tp_legame_musica = value;
	}

	public Character getTp_legame_musica() {
		return tp_legame_musica;
	}

	public void setCd_natura_base(char value) {
		this.cd_natura_base = value;
	}

	public char getCd_natura_base() {
		return cd_natura_base;
	}

	public void setCd_natura_coll(char value) {
		this.cd_natura_coll = value;
	}

	public char getCd_natura_coll() {
		return cd_natura_coll;
	}

	public void setSequenza(String value) {
		this.sequenza = value;
	}

	public String getSequenza() {
		return sequenza;
	}

	public void setNota_tit_tit(String value) {
		this.nota_tit_tit = value;
	}

	public String getNota_tit_tit() {
		return nota_tit_tit;
	}

	public void setSequenza_musica(String value) {
		this.sequenza_musica = value;
	}

	public String getSequenza_musica() {
		return sequenza_musica;
	}

	public void setSici(String value) {
		this.sici = value;
	}

	public String getSici() {
		return sici;
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
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
	}

	public void setBid_base(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.bid_base = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getBid_base() {
		return bid_base;
	}

	public void setBid_coll(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.bid_coll = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getBid_coll() {
		return bid_coll;
	}

	public String toString() {
		return String.valueOf(((getBid_base() == null) ? "" : String.valueOf(getBid_base().getORMID())) + " " + ((getBid_coll() == null) ? "" : String.valueOf(getBid_coll().getORMID())) + " " + getTp_legame());
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
