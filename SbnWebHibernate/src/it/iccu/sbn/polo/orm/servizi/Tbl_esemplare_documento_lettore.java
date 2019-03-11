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
package it.iccu.sbn.polo.orm.servizi;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tbl_esemplare_documento_lettore implements Serializable {

	private static final long serialVersionUID = -6259289996223331007L;

	public Tbl_esemplare_documento_lettore() {
	}

	private int id_esemplare;

	private it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori id_documenti_lettore;

	private short prg_esemplare;

	private char fonte;

	private String inventario;

	private String  precisazione;

	private String cod_no_disp;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbl_richiesta_servizio = new java.util.HashSet();

	private void setId_esemplare(int value) {
		this.id_esemplare = value;
	}

	public int getId_esemplare() {
		return id_esemplare;
	}

	public int getORMID() {
		return getId_esemplare();
	}

	public void setPrg_esemplare(short value) {
		this.prg_esemplare = value;
	}

	public short getPrg_esemplare() {
		return prg_esemplare;
	}

	public void setFonte(char value) {
		this.fonte = value;
	}

	public char getFonte() {
		return fonte;
	}

	public void setInventario(String value) {
		this.inventario = value;
	}

	public String getInventario() {
		return inventario;
	}

/*	public void setAnnata(java.util.Date value) {
		this.annata = value;
	}

	public java.util.Date getAnnata() {
		return annata;
	}
	*/
	public void setCod_no_disp(String value) {
		this.cod_no_disp = value;
	}

	public String getCod_no_disp() {
		return cod_no_disp;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setId_documenti_lettore(it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori value) {
		this.id_documenti_lettore = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori getId_documenti_lettore() {
		return id_documenti_lettore;
	}

	public void setTbl_richiesta_servizio(java.util.Set value) {
		this.Tbl_richiesta_servizio = value;
	}

	public java.util.Set getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}


	public String toString() {
		return String.valueOf(getId_esemplare());
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}





}
