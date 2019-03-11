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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.util.cloning.ClonePool;

import java.nio.ReadOnlyBufferException;

public class TB_CODICI extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 701210731929292666L;

	private boolean _locked = false;
	private boolean _saved = false;

	private String cd_flg1;
	private String cd_flg10;
	private String cd_flg11;
	private String cd_flg2;
	private String cd_flg3;
	private String cd_flg4;
	private String cd_flg5;
	private String cd_flg6;
	private String cd_flg7;
	private String cd_flg8;
	private String cd_flg9;
	private String cd_marc_21;
	private String cd_tabella;
	private String cd_unimarc;
	private String ds_cdsbn_ulteriore;
	private String ds_tabella;
	private java.util.Date dt_fine_validita;
	private Character tp_materiale;
	private String tp_tabella;


	public TB_CODICI() {
		return;
	}

	public TB_CODICI(TB_CODICI src) {
		ClonePool.copyCommonProperties(this, src);
	}

	public TB_CODICI(String cd_tabella, String ds_tabella) {
		this.cd_tabella = cd_tabella;
		setDs_tabella(ds_tabella);
	}

	public TB_CODICI(int cd_tabella, String ds_tabella) {
		this.cd_tabella = String.valueOf(cd_tabella);
		setDs_tabella(ds_tabella);
	}

	public TB_CODICI(String cd_tabella, String ds_tabella, String cd_unimarc) {
		this.cd_tabella = cd_tabella;
		this.cd_unimarc = cd_unimarc;
		setDs_tabella(ds_tabella);
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof TB_CODICI))
			return false;
		TB_CODICI tb_codici = (TB_CODICI) other;

		return this.getRepeatableId() == tb_codici.getRepeatableId();
	}

	/**
	 * Primo eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public String getCd_flg1() {
		return cd_flg1;
	}

	/**
	 * Decimo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public String getCd_flg10() {
		return cd_flg10;
	}

	/**
	 * Undicesimo eventuale attributo associato al codice identificante
	 * l'elemento di tabella
	 */
	public String getCd_flg11() {
		return cd_flg11;
	}

	/**
	 * Secondo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public String getCd_flg2() {
		return cd_flg2;
	}

	/**
	 * Terzo eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public String getCd_flg3() {
		return cd_flg3;
	}

	/**
	 * Quarto eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public String getCd_flg4() {
		return cd_flg4;
	}

	/**
	 * Quinto eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public String getCd_flg5() {
		return cd_flg5;
	}

	/**
	 * Sesto eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public String getCd_flg6() {
		return cd_flg6;
	}

	/**
	 * Settimo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public String getCd_flg7() {
		return cd_flg7;
	}

	/**
	 * Ottavo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public String getCd_flg8() {
		return cd_flg8;
	}

	/**
	 * Nono eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public String getCd_flg9() {
		return cd_flg9;
	}

	/**
	 * Export Marc 21
	 */
	public String getCd_marc_21() {
		return cd_marc_21;
	}

	public String getCd_tabella() {
		return cd_tabella;
	}

	public String getCd_tabellaTrim() {
		return trimOrEmpty(cd_tabella);
	}

	/**
	 * Export Unimarc
	 */
	public String getCd_unimarc() {
		return cd_unimarc;
	}

	public String getCd_unimarcTrim() {
		return trimOrEmpty(cd_unimarc);
	}

	/**
	 * Ulteriore decrizione associata al codice identificante l'elemento di
	 * tabella
	 */
	public String getDs_cdsbn_ulteriore() {
		return ds_cdsbn_ulteriore;
	}

	/**
	 * Decrizione associata al codice identificante l'elemento di tabella
	 */
	public String getDs_tabella() {
		return ds_tabella;
	}

	/**
	 * Data in cui codice a cessato di essere utilizzato
	 */
	public java.util.Date getDt_fine_validita() {
		return dt_fine_validita;
	}

	/**
	 * Tipo materiale
	 */
	public Character getTp_materiale() {
		return tp_materiale;
	}

	public String getTp_tabella() {
		return tp_tabella;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode += (isFilled(getTp_tabella()) ? getTp_tabella().hashCode() : 1);
		hashcode += (isFilled(getCd_tabella()) ? getCd_tabella().hashCode() : 2);
		return hashcode;
	}

	public boolean isSaved() {
		return _saved;
	}

	public TB_CODICI lock() {
		this._locked = true;
		return this;
	}

	public void onLoad() {
		_saved = true;
	}

	public void onSave() {
		_saved = true;
	}

	/**
	 * Primo eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public void setCd_flg1(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg1 = trimAndSet(value);
	}

	/**
	 * Decimo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public void setCd_flg10(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg10 = trimAndSet(value);
	}

	/**
	 * Undicesimo eventuale attributo associato al codice identificante
	 * l'elemento di tabella
	 */
	public void setCd_flg11(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg11 = trimAndSet(value);
	}

	/**
	 * Secondo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public void setCd_flg2(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg2 = trimAndSet(value);
	}

	/**
	 * Terzo eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public void setCd_flg3(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg3 = trimAndSet(value);
	}

	/**
	 * Quarto eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public void setCd_flg4(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg4 = trimAndSet(value);
	}

	/**
	 * Quinto eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public void setCd_flg5(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg5 = trimAndSet(value);
	}

	/**
	 * Sesto eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public void setCd_flg6(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg6 = trimAndSet(value);
	}

	/**
	 * Settimo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public void setCd_flg7(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg7 = trimAndSet(value);
	}

	/**
	 * Ottavo eventuale attributo associato al codice identificante l'elemento
	 * di tabella
	 */
	public void setCd_flg8(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg8 = trimAndSet(value);
	}

	/**
	 * Nono eventuale attributo associato al codice identificante l'elemento di
	 * tabella
	 */
	public void setCd_flg9(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_flg9 = trimAndSet(value);
	}

	/**
	 * Export Marc 21
	 */
	public void setCd_marc_21(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_marc_21 = value;
	}

	public void setCd_tabella(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_tabella = value;
	}

	/**
	 * Export Unimarc
	 */
	public void setCd_unimarc(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.cd_unimarc = value;
	}

	/**
	 * Ulteriore decrizione associata al codice identificante l'elemento di
	 * tabella
	 */
	public void setDs_cdsbn_ulteriore(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.ds_cdsbn_ulteriore = trimAndSet(value);
	}

	/**
	 * Decrizione associata al codice identificante l'elemento di tabella
	 */
	public void setDs_tabella(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.ds_tabella = trimAndSet(value);
	}

	/**
	 * Data in cui codice a cessato di essere utilizzato
	 */
	public void setDt_fine_validita(java.util.Date value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.dt_fine_validita = value;
	}

	/**
	 * Tipo materiale
	 */
	public void setTp_materiale(char value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		setTp_materiale(new Character(value));
	}

	/**
	 * Tipo materiale
	 */
	public void setTp_materiale(Character value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.tp_materiale = value;
	}

	public void setTp_tabella(String value) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.tp_tabella = value;
	}

	public String toString() {
		return String.valueOf(getTp_tabella() + " " + getCd_tabella());
	}

	@Override
	public int getRepeatableId() {
		return (trimOrBlank(tp_tabella) + trimOrBlank(cd_tabella)).hashCode();
	}

}
