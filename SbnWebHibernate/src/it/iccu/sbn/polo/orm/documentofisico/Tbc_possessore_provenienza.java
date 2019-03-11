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
 * POSSESSORI E PROVENIENZE DI INVENTARIO
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_possessore_provenienza implements Serializable {

	private static final long serialVersionUID = -1121779657332381943L;

	/**
	 *
	 */

	public Tbc_possessore_provenienza() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_possessore_provenienza))
			return false;
		Tbc_possessore_provenienza tbc_possessore_provenienza = (Tbc_possessore_provenienza)aObj;
		if ((getPid() != null && !getPid().equals(tbc_possessore_provenienza.getPid())) || (getPid() == null && tbc_possessore_provenienza.getPid() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getPid() == null ? 0 : getPid().hashCode());
		return hashcode;
	}

	private String pid;

	private char tp_forma_pp;

	private String ky_cautun;

	private String ky_auteur;

	private String ky_el1;

	private String ky_el2;

	private char tp_nome_pp;

	private String ky_el3;

	private String ky_el4;

	private String ky_el5;

	private String ky_cles1_a;

	private String ky_cles2_a;

	private String note;

	private Integer tot_inv;

	private String cd_livello;

	private Character fl_speciale;

	private String ds_nome_aut;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Trc_poss_prov_inventari = new java.util.HashSet();

	private java.util.Set Trc_possessori_possessori = new java.util.HashSet();

	private java.util.Set Trc_possessori_possessori1 = new java.util.HashSet();

	public void setPid(String value) {
		this.pid = value;
	}

	public String getPid() {
		return pid;
	}

	public String getORMID() {
		return getPid();
	}

	/**
	 * codice identificativo della tipologia del nome
	 */
	public void setTp_forma_pp(char value) {
		this.tp_forma_pp = value;
	}

	/**
	 * codice identificativo della tipologia del nome
	 */
	public char getTp_forma_pp() {
		return tp_forma_pp;
	}

	/**
	 * chiave cautun
	 */
	public void setKy_cautun(String value) {
		this.ky_cautun = value;
	}

	/**
	 * chiave cautun
	 */
	public String getKy_cautun() {
		return ky_cautun;
	}

	/**
	 * chiave auteur
	 */
	public void setKy_auteur(String value) {
		this.ky_auteur = value;
	}

	/**
	 * chiave auteur
	 */
	public String getKy_auteur() {
		return ky_auteur;
	}

	/**
	 * primo elemento di ordinamento
	 */
	public void setKy_el1(String value) {
		this.ky_el1 = value;
	}

	/**
	 * primo elemento di ordinamento
	 */
	public String getKy_el1() {
		return ky_el1;
	}

	/**
	 * secondo elemento di ordinamento
	 */
	public void setKy_el2(String value) {
		this.ky_el2 = value;
	}

	/**
	 * secondo elemento di ordinamento
	 */
	public String getKy_el2() {
		return ky_el2;
	}

	/**
	 * codice forma. indica se il nome del possessore e' in forma accettata o variante. ammette i valori: a=forma accettata, r=forma variante
	 */
	public void setTp_nome_pp(char value) {
		this.tp_nome_pp = value;
	}

	/**
	 * codice forma. indica se il nome del possessore e' in forma accettata o variante. ammette i valori: a=forma accettata, r=forma variante
	 */
	public char getTp_nome_pp() {
		return tp_nome_pp;
	}

	/**
	 * terzo elemento di ordinamento
	 */
	public void setKy_el3(String value) {
		this.ky_el3 = value;
	}

	/**
	 * terzo elemento di ordinamento
	 */
	public String getKy_el3() {
		return ky_el3;
	}

	/**
	 * quarto elemento di ordinamento
	 */
	public void setKy_el4(String value) {
		this.ky_el4 = value;
	}

	/**
	 * quarto elemento di ordinamento
	 */
	public String getKy_el4() {
		return ky_el4;
	}

	/**
	 * quinto elemento di ordinamento
	 */
	public void setKy_el5(String value) {
		this.ky_el5 = value;
	}

	/**
	 * quinto elemento di ordinamento
	 */
	public String getKy_el5() {
		return ky_el5;
	}

	/**
	 * note al possessore
	 */
	public void setKy_cles1_a(String value) {
		this.ky_cles1_a = value;
	}

	/**
	 * note al possessore
	 */
	public String getKy_cles1_a() {
		return ky_cles1_a;
	}

	/**
	 * prima parte della chiave di ricerca possessori ottenuta dalla trasformazione in caratteri di ordinamento della descrizione
	 */
	public void setKy_cles2_a(String value) {
		this.ky_cles2_a = value;
	}

	/**
	 * prima parte della chiave di ricerca possessori ottenuta dalla trasformazione in caratteri di ordinamento della descrizione
	 */
	public String getKy_cles2_a() {
		return ky_cles2_a;
	}

	/**
	 * seconda parte della chiave di ricerca possessori ottenuta dalla trasformazione in caratteri di ordinamento della descrizione
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * seconda parte della chiave di ricerca possessori ottenuta dalla trasformazione in caratteri di ordinamento della descrizione
	 */
	public String getNote() {
		return note;
	}

	/**
	 * numero totale degli inventari della base dati locale
	 */
	public void setTot_inv(int value) {
		setTot_inv(new Integer(value));
	}

	/**
	 * numero totale degli inventari della base dati locale
	 */
	public void setTot_inv(Integer value) {
		this.tot_inv = value;
	}

	/**
	 * numero totale degli inventari della base dati locale
	 */
	public Integer getTot_inv() {
		return tot_inv;
	}

	/**
	 * codice del livello di autorita'
	 */
	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	/**
	 * codice del livello di autorita'
	 */
	public String getCd_livello() {
		return cd_livello;
	}

	/**
	 * indicatore della presenza di caratteri speciali
	 */
	public void setFl_speciale(Character value) {
		this.fl_speciale = value;
	}

	/**
	 * indicatore della presenza di caratteri speciali
	 */
	public Character getFl_speciale() {
		return fl_speciale;
	}

	/**
	 * descrizione del possessore
	 */
	public void setDs_nome_aut(String value) {
		this.ds_nome_aut = value;
	}

	/**
	 * descrizione del possessore
	 */
	public String getDs_nome_aut() {
		return ds_nome_aut;
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

	public void setTrc_poss_prov_inventari(java.util.Set value) {
		this.Trc_poss_prov_inventari = value;
	}

	public java.util.Set getTrc_poss_prov_inventari() {
		return Trc_poss_prov_inventari;
	}


	public void setTrc_possessori_possessori(java.util.Set value) {
		this.Trc_possessori_possessori = value;
	}

	public java.util.Set getTrc_possessori_possessori() {
		return Trc_possessori_possessori;
	}


	public void setTrc_possessori_possessori1(java.util.Set value) {
		this.Trc_possessori_possessori1 = value;
	}

	public java.util.Set getTrc_possessori_possessori1() {
		return Trc_possessori_possessori1;
	}


	public String toString() {
		return String.valueOf(getPid());
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
