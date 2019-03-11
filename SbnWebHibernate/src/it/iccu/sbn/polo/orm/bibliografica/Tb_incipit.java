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
 * INCIPIT (SOLO MUSICA)
 */
/**
 * ORM-Persistable Class
 */
public class Tb_incipit implements Serializable {

	private static final long serialVersionUID = -41620918657503120L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_incipit))
			return false;
		Tb_incipit tb_incipit = (Tb_incipit)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tb_incipit.getB().getORMID()))
			return false;
		if (getNumero_mov() != null && !getNumero_mov().equals(tb_incipit.getNumero_mov()))
			return false;
		if (getNumero_p_mov() != null && !getNumero_p_mov().equals(tb_incipit.getNumero_p_mov()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		hashcode = hashcode + getNumero_mov().hashCode();
		hashcode = hashcode + getNumero_p_mov().hashCode();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String numero_mov;

	private String numero_p_mov;

	private String bid_letterario;

	private Character tp_indicatore;

	private String numero_comp;

	private String registro_mus;

	private String nome_personaggio;

	private String tempo_mus;

	private String cd_forma;

	private String cd_tonalita;

	private String chiave_mus;

	private String alterazione;

	private String misura;

	private String ds_contesto;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setNumero_mov(String value) {
		this.numero_mov = value;
	}

	public String getNumero_mov() {
		return numero_mov;
	}

	public void setNumero_p_mov(String value) {
		this.numero_p_mov = value;
	}

	public String getNumero_p_mov() {
		return numero_p_mov;
	}

	public void setBid_letterario(String value) {
		this.bid_letterario = value;
	}

	public String getBid_letterario() {
		return bid_letterario;
	}

	public void setTp_indicatore(char value) {
		setTp_indicatore(new Character(value));
	}

	public void setTp_indicatore(Character value) {
		this.tp_indicatore = value;
	}

	public Character getTp_indicatore() {
		return tp_indicatore;
	}

	public void setNumero_comp(String value) {
		this.numero_comp = value;
	}

	public String getNumero_comp() {
		return numero_comp;
	}

	public void setRegistro_mus(String value) {
		this.registro_mus = value;
	}

	public String getRegistro_mus() {
		return registro_mus;
	}

	public void setNome_personaggio(String value) {
		this.nome_personaggio = value;
	}

	public String getNome_personaggio() {
		return nome_personaggio;
	}

	public void setTempo_mus(String value) {
		this.tempo_mus = value;
	}

	public String getTempo_mus() {
		return tempo_mus;
	}

	public void setCd_forma(String value) {
		this.cd_forma = value;
	}

	public String getCd_forma() {
		return cd_forma;
	}

	public void setCd_tonalita(String value) {
		this.cd_tonalita = value;
	}

	public String getCd_tonalita() {
		return cd_tonalita;
	}

	public void setChiave_mus(String value) {
		this.chiave_mus = value;
	}

	public String getChiave_mus() {
		return chiave_mus;
	}

	public void setAlterazione(String value) {
		this.alterazione = value;
	}

	public String getAlterazione() {
		return alterazione;
	}

	public void setMisura(String value) {
		this.misura = value;
	}

	public String getMisura() {
		return misura;
	}

	public void setDs_contesto(String value) {
		this.ds_contesto = value;
	}

	public String getDs_contesto() {
		return ds_contesto;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + getNumero_mov() + " " + getNumero_p_mov());
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
