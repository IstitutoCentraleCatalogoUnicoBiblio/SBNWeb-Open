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
package it.iccu.sbn.polo.orm.acquisizioni;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tra_ordine_inventari implements Serializable {

	private static final long serialVersionUID = -6632165848747284108L;

	public Tra_ordine_inventari() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tra_ordine_inventari))
			return false;
		Tra_ordine_inventari tra_ordine_inventari = (Tra_ordine_inventari)aObj;
		if (getId_ordine() == null && tra_ordine_inventari.getId_ordine() != null)
			return false;
		if (!getId_ordine().equals(tra_ordine_inventari.getId_ordine()))
			return false;
		if (getCd_polo() == null && tra_ordine_inventari.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tra_ordine_inventari.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_ordine() != null) {
			hashcode = hashcode + getId_ordine().getORMID();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_serie() == null ? 0 : getCd_polo().getCd_serie().hashCode());
			hashcode = hashcode + getCd_polo().getCd_inven();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini id_ordine;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario cd_polo;

	private java.sql.Timestamp data_uscita;

	private java.sql.Timestamp data_rientro;

	private String ota_fornitore;

	private String ute_ins;

	private String ute_var;

	private java.sql.Timestamp ts_ins;

	private java.sql.Timestamp ts_var;

	private java.sql.Timestamp data_rientro_presunta;

	private Short posizione;

	private Short volume;

	public void setData_uscita(java.sql.Timestamp value) {
		this.data_uscita = value;
	}

	public java.sql.Timestamp getData_uscita() {
		return data_uscita;
	}

	public void setData_rientro(java.sql.Timestamp value) {
		this.data_rientro = value;
	}

	public java.sql.Timestamp getData_rientro() {
		return data_rientro;
	}

	public void setOta_fornitore(String value) {
		this.ota_fornitore = value;
	}

	public String getOta_fornitore() {
		return ota_fornitore;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setCd_polo(it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario getCd_polo() {
		return cd_polo;
	}

	public void setId_ordine(it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini value) {
		this.id_ordine = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini getId_ordine() {
		return id_ordine;
	}

	public String toString() {
		return String.valueOf(((getId_ordine() == null) ? "" : String.valueOf(getId_ordine().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_serie()) + " " + String.valueOf(getCd_polo().getCd_inven())));
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

	public java.sql.Timestamp getData_rientro_presunta() {
		return data_rientro_presunta;
	}

	public void setData_rientro_presunta(java.sql.Timestamp data_rientro_presunta) {
		this.data_rientro_presunta = data_rientro_presunta;
	}

	public Short getPosizione() {
		return posizione;
	}

	public void setPosizione(Short posizione) {
		this.posizione = posizione;
	}

	public Short getVolume() {
		return volume;
	}

	public void setVolume(Short volume) {
		this.volume = volume;
	}


}
