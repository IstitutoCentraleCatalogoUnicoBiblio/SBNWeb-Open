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
 * MUSICA
 */
/**
 * ORM-Persistable Class
 */
public class Tb_musica implements Serializable {

	private static final long serialVersionUID = 7169969796556483387L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String cd_livello;

	private String ds_org_sint;

	private String ds_org_anal;

	private Character tp_elaborazione;

	private Character cd_stesura;

	private char fl_composito;

	private char fl_palinsesto;

	private String datazione;

	private String cd_presentazione;

	private Character cd_materia;

	private String ds_illustrazioni;

	private String notazione_musicale;

	private String ds_legatura;

	private String ds_conservazione;

	private Character tp_testo_letter;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setDs_org_sint(String value) {
		this.ds_org_sint = value;
	}

	public String getDs_org_sint() {
		return ds_org_sint;
	}

	public void setDs_org_anal(String value) {
		this.ds_org_anal = value;
	}

	public String getDs_org_anal() {
		return ds_org_anal;
	}

	public void setTp_elaborazione(char value) {
		setTp_elaborazione(new Character(value));
	}

	public void setTp_elaborazione(Character value) {
		this.tp_elaborazione = value;
	}

	public Character getTp_elaborazione() {
		return tp_elaborazione;
	}

	public void setCd_stesura(char value) {
		setCd_stesura(new Character(value));
	}

	public void setCd_stesura(Character value) {
		this.cd_stesura = value;
	}

	public Character getCd_stesura() {
		return cd_stesura;
	}

	public void setFl_composito(char value) {
		this.fl_composito = value;
	}

	public char getFl_composito() {
		return fl_composito;
	}

	public void setFl_palinsesto(char value) {
		this.fl_palinsesto = value;
	}

	public char getFl_palinsesto() {
		return fl_palinsesto;
	}

	public void setDatazione(String value) {
		this.datazione = value;
	}

	public String getDatazione() {
		return datazione;
	}

	public void setCd_presentazione(String value) {
		this.cd_presentazione = value;
	}

	public String getCd_presentazione() {
		return cd_presentazione;
	}

	public void setCd_materia(char value) {
		setCd_materia(new Character(value));
	}

	public void setCd_materia(Character value) {
		this.cd_materia = value;
	}

	public Character getCd_materia() {
		return cd_materia;
	}

	public void setDs_illustrazioni(String value) {
		this.ds_illustrazioni = value;
	}

	public String getDs_illustrazioni() {
		return ds_illustrazioni;
	}

	public void setNotazione_musicale(String value) {
		this.notazione_musicale = value;
	}

	public String getNotazione_musicale() {
		return notazione_musicale;
	}

	public void setDs_legatura(String value) {
		this.ds_legatura = value;
	}

	public String getDs_legatura() {
		return ds_legatura;
	}

	public void setDs_conservazione(String value) {
		this.ds_conservazione = value;
	}

	public String getDs_conservazione() {
		return ds_conservazione;
	}

	public void setTp_testo_letter(char value) {
		setTp_testo_letter(new Character(value));
	}

	public void setTp_testo_letter(Character value) {
		this.tp_testo_letter = value;
	}

	public Character getTp_testo_letter() {
		return tp_testo_letter;
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

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getORMID() {
		return getB();
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())));
	}

}
