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
 * MATERIALE CARTOGRAFICO
 */
/**
 * ORM-Persistable Class
 */
public class Tb_cartografia implements Serializable {

	private static final long serialVersionUID = -7217727143354797849L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String cd_livello;

	private Character tp_pubb_gov;

	private Character cd_colore;

	private String cd_meridiano;

	private String cd_supporto_fisico;

	private Character cd_tecnica;

	private Character cd_forma_ripr;

	private Character cd_forma_pubb;

	private Character cd_altitudine;

	private Character cd_tiposcala;

	private Character tp_scala;

	private String scala_oriz;

	private String scala_vert;

	private String longitudine_ovest;

	private String longitudine_est;

	private String latitudine_nord;

	private String latitudine_sud;

	private Character tp_immagine;

	private Character cd_forma_cart;

	private Character cd_piattaforma;

	private Character cd_categ_satellite;

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

	public void setTp_pubb_gov(char value) {
		setTp_pubb_gov(new Character(value));
	}

	public void setTp_pubb_gov(Character value) {
		this.tp_pubb_gov = value;
	}

	public Character getTp_pubb_gov() {
		return tp_pubb_gov;
	}

	public void setCd_colore(char value) {
		setCd_colore(new Character(value));
	}

	public void setCd_colore(Character value) {
		this.cd_colore = value;
	}

	public Character getCd_colore() {
		return cd_colore;
	}

	public void setCd_meridiano(String value) {
		this.cd_meridiano = value;
	}

	public String getCd_meridiano() {
		return cd_meridiano;
	}

	public void setCd_supporto_fisico(String value) {
		this.cd_supporto_fisico = value;
	}

	public String getCd_supporto_fisico() {
		return cd_supporto_fisico;
	}

	public void setCd_tecnica(char value) {
		setCd_tecnica(new Character(value));
	}

	public void setCd_tecnica(Character value) {
		this.cd_tecnica = value;
	}

	public Character getCd_tecnica() {
		return cd_tecnica;
	}

	public void setCd_forma_ripr(char value) {
		setCd_forma_ripr(new Character(value));
	}

	public void setCd_forma_ripr(Character value) {
		this.cd_forma_ripr = value;
	}

	public Character getCd_forma_ripr() {
		return cd_forma_ripr;
	}

	public void setCd_forma_pubb(char value) {
		setCd_forma_pubb(new Character(value));
	}

	public void setCd_forma_pubb(Character value) {
		this.cd_forma_pubb = value;
	}

	public Character getCd_forma_pubb() {
		return cd_forma_pubb;
	}

	public void setCd_altitudine(char value) {
		setCd_altitudine(new Character(value));
	}

	public void setCd_altitudine(Character value) {
		this.cd_altitudine = value;
	}

	public Character getCd_altitudine() {
		return cd_altitudine;
	}

	public void setCd_tiposcala(char value) {
		setCd_tiposcala(new Character(value));
	}

	public void setCd_tiposcala(Character value) {
		this.cd_tiposcala = value;
	}

	public Character getCd_tiposcala() {
		return cd_tiposcala;
	}

	public void setTp_scala(char value) {
		setTp_scala(new Character(value));
	}

	public void setTp_scala(Character value) {
		this.tp_scala = value;
	}

	public Character getTp_scala() {
		return tp_scala;
	}

	public void setScala_oriz(String value) {
		this.scala_oriz = value;
	}

	public String getScala_oriz() {
		return scala_oriz;
	}

	public void setScala_vert(String value) {
		this.scala_vert = value;
	}

	public String getScala_vert() {
		return scala_vert;
	}

	public void setLongitudine_ovest(String value) {
		this.longitudine_ovest = value;
	}

	public String getLongitudine_ovest() {
		return longitudine_ovest;
	}

	public void setLongitudine_est(String value) {
		this.longitudine_est = value;
	}

	public String getLongitudine_est() {
		return longitudine_est;
	}

	public void setLatitudine_nord(String value) {
		this.latitudine_nord = value;
	}

	public String getLatitudine_nord() {
		return latitudine_nord;
	}

	public void setLatitudine_sud(String value) {
		this.latitudine_sud = value;
	}

	public String getLatitudine_sud() {
		return latitudine_sud;
	}

	public void setTp_immagine(char value) {
		setTp_immagine(new Character(value));
	}

	public void setTp_immagine(Character value) {
		this.tp_immagine = value;
	}

	public Character getTp_immagine() {
		return tp_immagine;
	}

	public void setCd_forma_cart(char value) {
		setCd_forma_cart(new Character(value));
	}

	public void setCd_forma_cart(Character value) {
		this.cd_forma_cart = value;
	}

	public Character getCd_forma_cart() {
		return cd_forma_cart;
	}

	public void setCd_piattaforma(char value) {
		setCd_piattaforma(new Character(value));
	}

	public void setCd_piattaforma(Character value) {
		this.cd_piattaforma = value;
	}

	public Character getCd_piattaforma() {
		return cd_piattaforma;
	}

	public void setCd_categ_satellite(char value) {
		setCd_categ_satellite(new Character(value));
	}

	public void setCd_categ_satellite(Character value) {
		this.cd_categ_satellite = value;
	}

	public Character getCd_categ_satellite() {
		return cd_categ_satellite;
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
