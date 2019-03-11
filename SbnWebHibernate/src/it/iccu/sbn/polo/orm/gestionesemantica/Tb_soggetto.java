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
package it.iccu.sbn.polo.orm.gestionesemantica;

/**
 * SOGGETTI (TPSSOG))
 */
/**
 * ORM-Persistable Class
 */
public class Tb_soggetto extends Tb_soggetto_base  {


	private static final long serialVersionUID = 9155989893538982587L;

	private String cid;

	private String ds_soggetto;

	private char fl_speciale;

	private String ky_cles1_s;

	private String ky_primo_descr;

	private char cat_sogg;

	private String cd_livello;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	private String ky_cles2_s;

	private java.util.Set Tr_tit_sog_bib = new java.util.HashSet();

	private java.util.Set Tr_sog_des = new java.util.HashSet();

	private java.util.Set Tr_sogp_sogi = new java.util.HashSet();

	public Tb_soggetto() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tb_soggetto))
			return false;
		Tb_soggetto tb_soggetto = (Tb_soggetto)aObj;
		if ((getCid() != null && !getCid().equals(tb_soggetto.getCid())) || (getCid() == null && tb_soggetto.getCid() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCid() == null ? 0 : getCid().hashCode());
		return hashcode;
	}

	public void setCid(String value) {
		this.cid = value;
	}

	public String getCid() {
		return cid;
	}

	public String getORMID() {
		return getCid();
	}

	/**
	 * descrizione
	 */
	public void setDs_soggetto(String value) {
		this.ds_soggetto = value;
	}

	/**
	 * descrizione
	 */
	public String getDs_soggetto() {
		return ds_soggetto;
	}

	/**
	 * indicatore di presenza di caratteri speciali nella stringa del soggetto
	 */
	public void setFl_speciale(char value) {
		this.fl_speciale = value;
	}

	/**
	 * indicatore di presenza di caratteri speciali nella stringa del soggetto
	 */
	public char getFl_speciale() {
		return fl_speciale;
	}

	/**
	 * chiave di ricerca
	 */
	public void setKy_cles1_s(String value) {
		this.ky_cles1_s = value;
	}

	/**
	 * chiave di ricerca
	 */
	public String getKy_cles1_s() {
		return ky_cles1_s;
	}

	/**
	 * chiave di ordinamento del primo elemento di soggetto (descrittore)
	 */
	public void setKy_primo_descr(String value) {
		this.ky_primo_descr = value;
	}

	/**
	 * chiave di ordinamento del primo elemento di soggetto (descrittore)
	 */
	public String getKy_primo_descr() {
		return ky_primo_descr;
	}

	/**
	 * codice di categoria soggetto
	 */
	public void setCat_sogg(char value) {
		this.cat_sogg = value;
	}

	/**
	 * codice di categoria soggetto
	 */
	public char getCat_sogg() {
		return cat_sogg;
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
	 * Indicatore di gestione condivisa con il sistema indice
	 */
	public void setFl_condiviso(char value) {
		this.fl_condiviso = value;
	}

	/**
	 * Indicatore di gestione condivisa con il sistema indice
	 */
	public char getFl_condiviso() {
		return fl_condiviso;
	}

	/**
	 * Utente che ha effettuato la condivisione con il sistema Indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Utente che ha effettuato la condivisione con il sistema Indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Timestamp di cndivisione con sistema indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Timestamp di cndivisione con sistema indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
	}

	public void setKy_cles2_s(String value) {
		this.ky_cles2_s = value;
	}

	public String getKy_cles2_s() {
		return ky_cles2_s;
	}

	public void setTidx_vector(int value) {
		setTidx_vector(new Integer(value));
	}

	public void setTr_tit_sog_bib(java.util.Set value) {
		this.Tr_tit_sog_bib = value;
	}

	public java.util.Set getTr_tit_sog_bib() {
		return Tr_tit_sog_bib;
	}


	public void setTr_sog_des(java.util.Set value) {
		this.Tr_sog_des = value;
	}

	public java.util.Set getTr_sog_des() {
		return Tr_sog_des;
	}


	public void setTr_sogp_sogi(java.util.Set value) {
		this.Tr_sogp_sogi = value;
	}

	public java.util.Set getTr_sogp_sogi() {
		return Tr_sogp_sogi;
	}


	public String toString() {
		return String.valueOf(getCid());
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
