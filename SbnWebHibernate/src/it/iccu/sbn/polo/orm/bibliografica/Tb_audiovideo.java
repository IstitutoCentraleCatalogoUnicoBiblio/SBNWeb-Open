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
 * AUDIOVISIVI
 */
/**
 * ORM-Persistable Class
 */
public class Tb_audiovideo implements Serializable {

	private static final long serialVersionUID = -1313928803695701698L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private char tp_mater_audiovis;

	private String lunghezza;

	private char cd_colore;

	private char cd_suono;

	private Character tp_media_suono;

	private Character cd_dimensione;

	private Character cd_forma_video;

	private Character cd_tecnica;

	private Character tp_formato_film;

	private String cd_mat_accomp;

	private Character cd_forma_regist;

	private Character tp_formato_video;

	private Character cd_materiale_base;

	private Character cd_supporto_second;

	private Character cd_broadcast;

	private Character tp_generazione;

	private Character cd_elementi;

	private Character cd_categ_colore;

	private Character cd_polarita;

	private Character cd_pellicola;

	private Character tp_suono;

	private Character tp_stampa_film;

	private Character cd_deteriore;

	private Character cd_completo;

	private java.sql.Timestamp dt_ispezione;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_mater_audiovis(char value) {
		this.tp_mater_audiovis = value;
	}

	public char getTp_mater_audiovis() {
		return tp_mater_audiovis;
	}

	public void setLunghezza(String value) {
		this.lunghezza = value;
	}

	public String getLunghezza() {
		return lunghezza;
	}

	public void setCd_colore(char value) {
		this.cd_colore = value;
	}

	public char getCd_colore() {
		return cd_colore;
	}

	public void setCd_suono(char value) {
		this.cd_suono = value;
	}

	public char getCd_suono() {
		return cd_suono;
	}

	public void setTp_media_suono(char value) {
		setTp_media_suono(new Character(value));
	}

	public void setTp_media_suono(Character value) {
		this.tp_media_suono = value;
	}

	public Character getTp_media_suono() {
		return tp_media_suono;
	}

	public void setCd_dimensione(char value) {
		setCd_dimensione(new Character(value));
	}

	public void setCd_dimensione(Character value) {
		this.cd_dimensione = value;
	}

	public Character getCd_dimensione() {
		return cd_dimensione;
	}

	public void setCd_forma_video(char value) {
		setCd_forma_video(new Character(value));
	}

	public void setCd_forma_video(Character value) {
		this.cd_forma_video = value;
	}

	public Character getCd_forma_video() {
		return cd_forma_video;
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

	public void setTp_formato_film(char value) {
		setTp_formato_film(new Character(value));
	}

	public void setTp_formato_film(Character value) {
		this.tp_formato_film = value;
	}

	public Character getTp_formato_film() {
		return tp_formato_film;
	}

	public void setCd_mat_accomp(String value) {
		this.cd_mat_accomp = value;
	}

	public String getCd_mat_accomp() {
		return cd_mat_accomp;
	}

	public void setCd_forma_regist(char value) {
		setCd_forma_regist(new Character(value));
	}

	public void setCd_forma_regist(Character value) {
		this.cd_forma_regist = value;
	}

	public Character getCd_forma_regist() {
		return cd_forma_regist;
	}

	public void setTp_formato_video(char value) {
		setTp_formato_video(new Character(value));
	}

	public void setTp_formato_video(Character value) {
		this.tp_formato_video = value;
	}

	public Character getTp_formato_video() {
		return tp_formato_video;
	}

	public void setCd_materiale_base(char value) {
		setCd_materiale_base(new Character(value));
	}

	public void setCd_materiale_base(Character value) {
		this.cd_materiale_base = value;
	}

	public Character getCd_materiale_base() {
		return cd_materiale_base;
	}

	public void setCd_supporto_second(char value) {
		setCd_supporto_second(new Character(value));
	}

	public void setCd_supporto_second(Character value) {
		this.cd_supporto_second = value;
	}

	public Character getCd_supporto_second() {
		return cd_supporto_second;
	}

	public void setCd_broadcast(char value) {
		setCd_broadcast(new Character(value));
	}

	public void setCd_broadcast(Character value) {
		this.cd_broadcast = value;
	}

	public Character getCd_broadcast() {
		return cd_broadcast;
	}

	public void setTp_generazione(char value) {
		setTp_generazione(new Character(value));
	}

	public void setTp_generazione(Character value) {
		this.tp_generazione = value;
	}

	public Character getTp_generazione() {
		return tp_generazione;
	}

	public void setCd_elementi(char value) {
		setCd_elementi(new Character(value));
	}

	public void setCd_elementi(Character value) {
		this.cd_elementi = value;
	}

	public Character getCd_elementi() {
		return cd_elementi;
	}

	public void setCd_categ_colore(char value) {
		setCd_categ_colore(new Character(value));
	}

	public void setCd_categ_colore(Character value) {
		this.cd_categ_colore = value;
	}

	public Character getCd_categ_colore() {
		return cd_categ_colore;
	}

	public void setCd_polarita(char value) {
		setCd_polarita(new Character(value));
	}

	public void setCd_polarita(Character value) {
		this.cd_polarita = value;
	}

	public Character getCd_polarita() {
		return cd_polarita;
	}

	public void setCd_pellicola(char value) {
		setCd_pellicola(new Character(value));
	}

	public void setCd_pellicola(Character value) {
		this.cd_pellicola = value;
	}

	public Character getCd_pellicola() {
		return cd_pellicola;
	}

	public void setTp_suono(char value) {
		setTp_suono(new Character(value));
	}

	public void setTp_suono(Character value) {
		this.tp_suono = value;
	}

	public Character getTp_suono() {
		return tp_suono;
	}

	public void setTp_stampa_film(char value) {
		setTp_stampa_film(new Character(value));
	}

	public void setTp_stampa_film(Character value) {
		this.tp_stampa_film = value;
	}

	public Character getTp_stampa_film() {
		return tp_stampa_film;
	}

	public void setCd_deteriore(char value) {
		setCd_deteriore(new Character(value));
	}

	public void setCd_deteriore(Character value) {
		this.cd_deteriore = value;
	}

	public Character getCd_deteriore() {
		return cd_deteriore;
	}

	public void setCd_completo(char value) {
		setCd_completo(new Character(value));
	}

	public void setCd_completo(Character value) {
		this.cd_completo = value;
	}

	public Character getCd_completo() {
		return cd_completo;
	}

	public void setDt_ispezione(java.sql.Timestamp value) {
		this.dt_ispezione = value;
	}

	public java.sql.Timestamp getDt_ispezione() {
		return dt_ispezione;
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

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

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
