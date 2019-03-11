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
 * COLLOCAZIONE (LOC)
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_collocazione implements Serializable {

	private static final long serialVersionUID = -4475675890937787512L;

	public Tbc_collocazione() {
	}

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe cd_sistema;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione cd_sez;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo cd_biblioteca_doc;

	private int key_loc;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String cd_loc;

	private String spec_loc;

	private String consis;

	private int tot_inv;

	private String indice;

	private String ord_loc;

	private String ord_spec;

	private Integer tot_inv_prov;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbc_inventario = new java.util.HashSet();

	private void setKey_loc(int value) {
		this.key_loc = value;
	}

	public int getKey_loc() {
		return key_loc;
	}

	public int getORMID() {
		return getKey_loc();
	}

	/**
	 * collocazione
	 */
	public void setCd_loc(String value) {
		this.cd_loc = value;
	}

	/**
	 * collocazione
	 */
	public String getCd_loc() {
		return cd_loc;
	}

	/**
	 * specificazione della collocazione
	 */
	public void setSpec_loc(String value) {
		this.spec_loc = value;
	}

	/**
	 * specificazione della collocazione
	 */
	public String getSpec_loc() {
		return spec_loc;
	}

	/**
	 * consistenza della collocazione
	 */
	public void setConsis(String value) {
		this.consis = value;
	}

	/**
	 * consistenza della collocazione
	 */
	public String getConsis() {
		return consis;
	}

	/**
	 * numero totale degli inventari collocati
	 */
	public void setTot_inv(int value) {
		this.tot_inv = value;
	}

	/**
	 * numero totale degli inventari collocati
	 */
	public int getTot_inv() {
		return tot_inv;
	}

	/**
	 * simbolo di classificazione
	 */
	public void setIndice(String value) {
		this.indice = value;
	}

	/**
	 * simbolo di classificazione
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * chiave per ordinamento alfabetico calcolata dalla stringa di collocazione cd_loc
	 */
	public void setOrd_loc(String value) {
		this.ord_loc = value;
	}

	/**
	 * chiave per ordinamento alfabetico calcolata dalla stringa di collocazione cd_loc
	 */
	public String getOrd_loc() {
		return ord_loc;
	}

	/**
	 * chiave per ordinamento alfabetico calcolata dalla stringa di specificazione spec_loc
	 */
	public void setOrd_spec(String value) {
		this.ord_spec = value;
	}

	/**
	 * chiave per ordinamento alfabetico calcolata dalla stringa di specificazione spec_loc
	 */
	public String getOrd_spec() {
		return ord_spec;
	}

	/**
	 * numero di inventari spostati in una collocazione temporanea
	 */
	public void setTot_inv_prov(int value) {
		setTot_inv_prov(new Integer(value));
	}

	/**
	 * numero di inventari spostati in una collocazione temporanea
	 */
	public void setTot_inv_prov(Integer value) {
		this.tot_inv_prov = value;
	}

	/**
	 * numero di inventari spostati in una collocazione temporanea
	 */
	public Integer getTot_inv_prov() {
		return tot_inv_prov;
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

	public void setCd_biblioteca_doc(it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo value) {
		this.cd_biblioteca_doc = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo getCd_biblioteca_doc() {
		return cd_biblioteca_doc;
	}

	public void setCd_sez(it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione value) {
		this.cd_sez = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione getCd_sez() {
		return cd_sez;
	}

	public void setCd_sistema(it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe value) {
		this.cd_sistema = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe getCd_sistema() {
		return cd_sistema;
	}

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setTbc_inventario(java.util.Set value) {
		this.Tbc_inventario = value;
	}

	public java.util.Set getTbc_inventario() {
		return Tbc_inventario;
	}


	public String toString() {
		return String.valueOf(getKey_loc());
	}

}
