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
 * DESCRITTORI DI SOGGETTO (TPSDSO))
 */
/**
 * ORM-Persistable Class
 */
public class Tb_descrittore extends Tb_soggetto_base {

	private static final long serialVersionUID = -354420645244547470L;

	private String did;

	private String ds_descrittore;

	private String ky_norm_descritt;

	private String nota_descrittore;

	private Character cat_termine;

	private char tp_forma_des;

	private String cd_livello;

	private java.util.Set Tr_des_des = new java.util.HashSet();

	private java.util.Set Tr_des_des1 = new java.util.HashSet();

	private java.util.Set Tr_sog_des = new java.util.HashSet();

	public void setDid(String value) {
		this.did = value;
	}

	public String getDid() {
		return did;
	}

	public String getORMID() {
		return getDid();
	}

	/**
	 * descrizione
	 */
	public void setDs_descrittore(String value) {
		this.ds_descrittore = value;
	}

	/**
	 * descrizione
	 */
	public String getDs_descrittore() {
		return ds_descrittore;
	}

	/**
	 * chiave di ricerca
	 */
	public void setKy_norm_descritt(String value) {
		this.ky_norm_descritt = value;
	}

	/**
	 * chiave di ricerca
	 */
	public String getKy_norm_descritt() {
		return ky_norm_descritt;
	}

	/**
	 * note al descrittore
	 */
	public void setNota_descrittore(String value) {
		this.nota_descrittore = value;
	}

	/**
	 * note al descrittore
	 */
	public String getNota_descrittore() {
		return nota_descrittore;
	}

	public void setTp_forma_des(char value) {
		this.tp_forma_des = value;
	}

	public char getTp_forma_des() {
		return tp_forma_des;
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setTr_des_des(java.util.Set value) {
		this.Tr_des_des = value;
	}

	public java.util.Set getTr_des_des() {
		return Tr_des_des;
	}


	public void setTr_des_des1(java.util.Set value) {
		this.Tr_des_des1 = value;
	}

	public java.util.Set getTr_des_des1() {
		return Tr_des_des1;
	}


	public void setTr_sog_des(java.util.Set value) {
		this.Tr_sog_des = value;
	}

	public java.util.Set getTr_sog_des() {
		return Tr_sog_des;
	}


	public String toString() {
		return String.valueOf(getDid());
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

	public Character getCat_termine() {
		return cat_termine;
	}

	public void setCat_termine(Character cat_termine) {
		this.cat_termine = cat_termine;
	}


}
