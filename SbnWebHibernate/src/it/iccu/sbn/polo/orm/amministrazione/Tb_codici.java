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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * Codici delle tabelle
 */
/**
 * ORM-Persistable Class
 */
public class Tb_codici implements Serializable {

	private static final long serialVersionUID = 8272832020257966212L;

	public Tb_codici(String string, String string2) {
		this.cd_tabella=string;
		this.ds_tabella=string2;
	}

	public Tb_codici(String string, String string2, String string3) {
		this.cd_tabella=string;
		this.ds_tabella=string2;
		this.cd_unimarc=string3;
	}

	public Tb_codici(String tp_tabella, String cd_tabella, String ds_tabella,
			String cd_unimarc) {
		this.tp_tabella = tp_tabella;
		this.cd_tabella = cd_tabella;
		this.ds_tabella = ds_tabella;
		this.cd_unimarc = cd_unimarc;
	}

	public Tb_codici() {
		return;
	}

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_codici))
			return false;
		Tb_codici Tb_codici = (Tb_codici)aObj;
		if (getTp_tabella() != null && !getTp_tabella().equals(Tb_codici.getTp_tabella()))
			return false;
		if (getCd_tabella() != null && !getCd_tabella().equals(Tb_codici.getCd_tabella()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getTp_tabella().hashCode();
		hashcode = hashcode + getCd_tabella().hashCode();
		return hashcode;
	}

	private String tp_tabella;

	private String cd_tabella;

	private String ds_tabella;

	private String cd_unimarc;

	private String cd_marc_21;

	private Character tp_materiale;

	private java.util.Date dt_fine_validita;

	private String ds_cdsbn_ulteriore;

	private String cd_flg1;

	private String cd_flg2;

	private String cd_flg3;

	private String cd_flg4;

	private String cd_flg5;

	private String cd_flg6;

	private String cd_flg7;

	private String cd_flg8;

	private String cd_flg9;

	private String cd_flg10;

	private String cd_flg11;

	public void setTp_tabella(String value) {
		this.tp_tabella = value;
	}

	public String getTp_tabella() {
		return tp_tabella;
	}

	public void setCd_tabella(String value) {
		this.cd_tabella = value;
	}

	public String getCd_tabella() {
		return cd_tabella;
	}

	/**
	 * Decrizione associata al codice identificante l'elemento di tabella
	 */
	public void setDs_tabella(String value) {
		this.ds_tabella = value;
	}

	/**
	 * Decrizione associata al codice identificante l'elemento di tabella
	 */
	public String getDs_tabella() {
		return ds_tabella;
	}

	/**
	 * Export Unimarc
	 */
	public void setCd_unimarc(String value) {
		this.cd_unimarc = value;
	}

	/**
	 * Export Unimarc
	 */
	public String getCd_unimarc() {
		return cd_unimarc;
	}

	/**
	 * Export Marc 21
	 */
	public void setCd_marc_21(String value) {
		this.cd_marc_21 = value;
	}

	/**
	 * Export Marc 21
	 */
	public String getCd_marc_21() {
		return cd_marc_21;
	}

	/**
	 * Tipo materiale
	 */
	public void setTp_materiale(char value) {
		setTp_materiale(new Character(value));
	}

	/**
	 * Tipo materiale
	 */
	public void setTp_materiale(Character value) {

		this.tp_materiale = value;
	}

	/**
	 * Tipo materiale
	 */
	public Character getTp_materiale() {
		return tp_materiale;
	}

	/**
	 * Data in cui codice a cessato di essere utilizzato
	 */
	public void setDt_fine_validita(java.util.Date value) {

		this.dt_fine_validita = value;
	}

	/**
	 * Data in cui codice a cessato di essere utilizzato
	 */
	public java.util.Date getDt_fine_validita() {
		return dt_fine_validita;
	}

	/**
	 * Ulteriore decrizione associata al codice identificante l'elemento di tabella
	 */
	public void setDs_cdsbn_ulteriore(String value) {
		this.ds_cdsbn_ulteriore = value;
	}

	/**
	 * Ulteriore decrizione associata al codice identificante l'elemento di tabella
	 */
	public String getDs_cdsbn_ulteriore() {
		return ds_cdsbn_ulteriore;
	}

	/**
	 * Primo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg1(String value) {
		this.cd_flg1 = value;
	}

	/**
	 * Primo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg1() {
		return cd_flg1;
	}

	/**
	 * Secondo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg2(String value) {
		this.cd_flg2 = value;
	}

	/**
	 * Secondo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg2() {
		return cd_flg2;
	}

	/**
	 * Terzo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg3(String value) {
		this.cd_flg3 = value;
	}

	/**
	 * Terzo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg3() {
		return cd_flg3;
	}

	/**
	 * Quarto eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg4(String value) {
		this.cd_flg4 = value;
	}

	/**
	 * Quarto eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg4() {
		return cd_flg4;
	}

	/**
	 * Quinto eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg5(String value) {
		this.cd_flg5 = value;
	}

	/**
	 * Quinto eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg5() {
		return cd_flg5;
	}

	/**
	 * Sesto eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg6(String value) {
		this.cd_flg6 = value;
	}

	/**
	 * Sesto eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg6() {
		return cd_flg6;
	}

	/**
	 * Settimo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg7(String value) {
		this.cd_flg7 = value;
	}

	/**
	 * Settimo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg7() {
		return cd_flg7;
	}

	/**
	 * Ottavo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg8(String value) {
		this.cd_flg8 = value;
	}

	/**
	 * Ottavo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg8() {
		return cd_flg8;
	}

	/**
	 * Nono eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg9(String value) {
		this.cd_flg9 = value;
	}

	/**
	 * Nono eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg9() {
		return cd_flg9;
	}

	/**
	 * Decimo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg10(String value) {
		this.cd_flg10 = value;
	}

	/**
	 * Decimo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg10() {
		return cd_flg10;
	}

	/**
	 * Undicesimo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public void setCd_flg11(String value) {
		this.cd_flg11 = value;
	}

	/**
	 * Undicesimo eventuale attributo associato al codice identificante l'elemento di tabella
	 */
	public String getCd_flg11() {
		return cd_flg11;
	}

	public String toString() {
		return String.valueOf(getTp_tabella() + " " + getCd_tabella());
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
