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
package it.iccu.sbn.polo.orm.documentofisico;

import java.io.Serializable;
/**
 * DATI DI SERVIZIO RELATIVI ALL'INVENTARIO
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_default_inven_schede implements Serializable {

	private static final long serialVersionUID = 7180704993858289653L;

	public Tbc_default_inven_schede() {
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private int id_default_inven_scheda;

	private Integer n_copie_tit;

	private Integer n_copie_edi;

	private Integer n_copie_poss;

	private Integer n_copie_richiamo;

	private String cd_unimarc;

	private Character sch_autori;

	private Character fl_coll_aut;

	private Character fl_tipo_leg;

	private Character sch_topog;

	private Character fl_coll_top;

	private Character sch_soggetti;

	private Character fl_coll_sog;

	private Character sch_classi;

	private Character fl_coll_cla;

	private Character sch_titoli;

	private Character fl_coll_tit;

	private Character sch_edit;

	private Character fl_coll_edi;

	private Character sch_poss;

	private Character fl_coll_poss;

	private Character flg_coll_richiamo;

	private Character fl_non_inv;

	private String tipo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Integer n_serie;

	private Integer n_piste;

	private Integer n_copie;

	private Integer n_copie_aut;

	private Integer n_copie_top;

	private Integer n_copie_sog;

	private Integer n_copie_cla;

	private String formato_stampa;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe id_modello;

	private void setId_default_inven_scheda(int value) {
		this.id_default_inven_scheda = value;
	}

	public int getId_default_inven_scheda() {
		return id_default_inven_scheda;
	}

	public int getORMID() {
		return getId_default_inven_scheda();
	}

	/**
	 * codice di default del tipo di scarico unimarc utilizzato dalla biblioteca
	 */
	public void setCd_unimarc(String value) {
		this.cd_unimarc = value;
	}

	/**
	 * codice di default del tipo di scarico unimarc utilizzato dalla biblioteca
	 */
	public String getCd_unimarc() {
		return cd_unimarc;
	}

	public String getTipo() {
		return tipo;
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

	public Integer getN_copie_tit() {
		return n_copie_tit;
	}

	public void setN_copie_tit(Integer n_copie_tit) {
		this.n_copie_tit = n_copie_tit;
	}

	public Integer getN_copie_edi() {
		return n_copie_edi;
	}

	public void setN_copie_edi(Integer n_copie_edi) {
		this.n_copie_edi = n_copie_edi;
	}

	public Integer getN_copie_poss() {
		return n_copie_poss;
	}

	public void setN_copie_poss(Integer n_copie_poss) {
		this.n_copie_poss = n_copie_poss;
	}

	public Integer getN_copie_richiamo() {
		return n_copie_richiamo;
	}

	public void setN_copie_richiamo(Integer n_copie_richiamo) {
		this.n_copie_richiamo = n_copie_richiamo;
	}

	public Character getSch_autori() {
		return sch_autori;
	}

	public void setSch_autori(Character sch_autori) {
		this.sch_autori = sch_autori;
	}

	public Character getFl_coll_aut() {
		return fl_coll_aut;
	}

	public void setFl_coll_aut(Character fl_coll_aut) {
		this.fl_coll_aut = fl_coll_aut;
	}

	public Character getFl_tipo_leg() {
		return fl_tipo_leg;
	}

	public void setFl_tipo_leg(Character fl_tipo_leg) {
		this.fl_tipo_leg = fl_tipo_leg;
	}

	public Character getSch_topog() {
		return sch_topog;
	}

	public void setSch_topog(Character sch_topog) {
		this.sch_topog = sch_topog;
	}

	public Character getFl_coll_top() {
		return fl_coll_top;
	}

	public void setFl_coll_top(Character fl_coll_top) {
		this.fl_coll_top = fl_coll_top;
	}

	public Character getSch_soggetti() {
		return sch_soggetti;
	}

	public void setSch_soggetti(Character sch_soggetti) {
		this.sch_soggetti = sch_soggetti;
	}

	public Character getFl_coll_sog() {
		return fl_coll_sog;
	}

	public void setFl_coll_sog(Character fl_coll_sog) {
		this.fl_coll_sog = fl_coll_sog;
	}

	public Character getSch_classi() {
		return sch_classi;
	}

	public void setSch_classi(Character sch_classi) {
		this.sch_classi = sch_classi;
	}

	public Character getFl_coll_cla() {
		return fl_coll_cla;
	}

	public void setFl_coll_cla(Character fl_coll_cla) {
		this.fl_coll_cla = fl_coll_cla;
	}

	public Character getSch_titoli() {
		return sch_titoli;
	}

	public void setSch_titoli(Character sch_titoli) {
		this.sch_titoli = sch_titoli;
	}

	public Character getFl_coll_tit() {
		return fl_coll_tit;
	}

	public void setFl_coll_tit(Character fl_coll_tit) {
		this.fl_coll_tit = fl_coll_tit;
	}

	public Character getSch_edit() {
		return sch_edit;
	}

	public void setSch_edit(Character sch_edit) {
		this.sch_edit = sch_edit;
	}

	public Character getFl_coll_edi() {
		return fl_coll_edi;
	}

	public void setFl_coll_edi(Character fl_coll_edi) {
		this.fl_coll_edi = fl_coll_edi;
	}

	public Character getSch_poss() {
		return sch_poss;
	}

	public void setSch_poss(Character sch_poss) {
		this.sch_poss = sch_poss;
	}

	public Character getFl_coll_poss() {
		return fl_coll_poss;
	}

	public void setFl_coll_poss(Character fl_coll_poss) {
		this.fl_coll_poss = fl_coll_poss;
	}

	public Character getFlg_coll_richiamo() {
		return flg_coll_richiamo;
	}

	public void setFlg_coll_richiamo(Character flg_coll_richiamo) {
		this.flg_coll_richiamo = flg_coll_richiamo;
	}

	public Character getFl_non_inv() {
		return fl_non_inv;
	}

	public void setFl_non_inv(Character fl_non_inv) {
		this.fl_non_inv = fl_non_inv;
	}

	public Integer getN_serie() {
		return n_serie;
	}

	public void setN_serie(Integer n_serie) {
		this.n_serie = n_serie;
	}

	public Integer getN_piste() {
		return n_piste;
	}

	public void setN_piste(Integer n_piste) {
		this.n_piste = n_piste;
	}

	public Integer getN_copie() {
		return n_copie;
	}

	public void setN_copie(Integer n_copie) {
		this.n_copie = n_copie;
	}

	public Integer getN_copie_aut() {
		return n_copie_aut;
	}

	public void setN_copie_aut(Integer n_copie_aut) {
		this.n_copie_aut = n_copie_aut;
	}

	public Integer getN_copie_top() {
		return n_copie_top;
	}

	public void setN_copie_top(Integer n_copie_top) {
		this.n_copie_top = n_copie_top;
	}

	public Integer getN_copie_sog() {
		return n_copie_sog;
	}

	public void setN_copie_sog(Integer n_copie_sog) {
		this.n_copie_sog = n_copie_sog;
	}

	public Integer getN_copie_cla() {
		return n_copie_cla;
	}

	public void setN_copie_cla(Integer n_copie_cla) {
		this.n_copie_cla = n_copie_cla;
	}

	public String getFormato_stampa() {
		return formato_stampa;
	}

	public void setFormato_stampa(String formato_stampa) {
		this.formato_stampa = formato_stampa;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setId_modello(it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe value) {
		this.id_modello = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe getId_modello() {
		return id_modello;
	}

	public String toString() {
		return String.valueOf(getId_default_inven_scheda());
	}

}
