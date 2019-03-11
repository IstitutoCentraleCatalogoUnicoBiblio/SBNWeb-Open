/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: antoniospatera@libero.it
 * License Type: Evaluation
 */
package it.finsiel.sbn.polo.orm;

import it.finsiel.sbn.polo.orm.intf.DatiComuni;

/**
 * ORM-Persistable Class
 */
public class Tb_titset_1 extends OggettoServerSbnMarc implements DatiComuni {

	private static final long serialVersionUID = 5775960938379636164L;

	private String BID;

	private String S105_TP_TESTO_LETTERARIO;
	private String S140_TP_TESTO_LETTERARIO;

	private String S125_INDICATORE_TESTO;

	private String S181_TP_FORMA_CONTENUTO_1;
	private String S181_CD_TIPO_CONTENUTO_1;
	private String S181_CD_MOVIMENTO_1;
	private String S181_CD_DIMENSIONE_1;
	private String S181_CD_SENSORIALE_1_1;
	private String S181_CD_SENSORIALE_2_1;
	private String S181_CD_SENSORIALE_3_1;
	private String S181_TP_FORMA_CONTENUTO_2;
	private String S181_CD_TIPO_CONTENUTO_2;
	private String S181_CD_MOVIMENTO_2;
	private String S181_CD_DIMENSIONE_2;
	private String S181_CD_SENSORIALE_1_2;
	private String S181_CD_SENSORIALE_2_2;
	private String S181_CD_SENSORIALE_3_2;
	private String S182_TP_MEDIAZIONE_1;
	private String S182_TP_MEDIAZIONE_2;

	//almaviva5_20150923 sbnmarc v2.01
	private String S183_TP_SUPPORTO_1;
	private String S183_TP_SUPPORTO_2;

	private String S210_IND_PUBBLICATO;

	public void setBID(String value) {
		this.BID = value;
		this.settaParametro(KeyParameter.XXXbid, value);
	}

	public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String
				.valueOf(getBID())));
	}

	public String getS105_TP_TESTO_LETTERARIO() {
		return S105_TP_TESTO_LETTERARIO;
	}

	public void setS105_TP_TESTO_LETTERARIO(String value) {
		S105_TP_TESTO_LETTERARIO = value;
		this.settaParametro(KeyParameter.XXXs105_tp_testo_letterario, value);
	}

	public String getS140_TP_TESTO_LETTERARIO() {
		return S140_TP_TESTO_LETTERARIO;
	}

	public void setS140_TP_TESTO_LETTERARIO(String value) {
		S140_TP_TESTO_LETTERARIO = value;
		this.settaParametro(KeyParameter.XXXs140_tp_testo_letterario, value);
	}

	public String getS181_TP_FORMA_CONTENUTO_1() {
		return S181_TP_FORMA_CONTENUTO_1;
	}

	public void setS181_TP_FORMA_CONTENUTO_1(String value) {
		S181_TP_FORMA_CONTENUTO_1 = value;
		this.settaParametro(KeyParameter.XXXs181_tp_forma_contenuto_1, value);
	}

	public String getS181_CD_TIPO_CONTENUTO_1() {
		return S181_CD_TIPO_CONTENUTO_1;
	}

	public void setS181_CD_TIPO_CONTENUTO_1(String value) {
		S181_CD_TIPO_CONTENUTO_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_tipo_contenuto_1, value);
	}

	public String getS181_CD_MOVIMENTO_1() {
		return S181_CD_MOVIMENTO_1;
	}

	public void setS181_CD_MOVIMENTO_1(String value) {
		S181_CD_MOVIMENTO_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_movimento_1, value);
	}

	public String getS181_CD_DIMENSIONE_1() {
		return S181_CD_DIMENSIONE_1;
	}

	public void setS181_CD_DIMENSIONE_1(String value) {
		S181_CD_DIMENSIONE_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_dimensione_1, value);
	}

	public String getS181_CD_SENSORIALE_1_1() {
		return S181_CD_SENSORIALE_1_1;
	}

	public void setS181_CD_SENSORIALE_1_1(String value) {
		S181_CD_SENSORIALE_1_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_1_1, value);
	}

	public String getS181_CD_SENSORIALE_2_1() {
		return S181_CD_SENSORIALE_2_1;
	}

	public void setS181_CD_SENSORIALE_2_1(String value) {
		S181_CD_SENSORIALE_2_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_2_1, value);
	}

	public String getS181_CD_SENSORIALE_3_1() {
		return S181_CD_SENSORIALE_3_1;
	}

	public void setS181_CD_SENSORIALE_3_1(String value) {
		S181_CD_SENSORIALE_3_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_3_1, value);
	}

	public String getS181_TP_FORMA_CONTENUTO_2() {
		return S181_TP_FORMA_CONTENUTO_2;
	}

	public void setS181_TP_FORMA_CONTENUTO_2(String value) {
		S181_TP_FORMA_CONTENUTO_2 = value;
		this.settaParametro(KeyParameter.XXXs181_tp_forma_contenuto_2, value);
	}

	public String getS181_CD_TIPO_CONTENUTO_2() {
		return S181_CD_TIPO_CONTENUTO_2;
	}

	public void setS181_CD_TIPO_CONTENUTO_2(String value) {
		S181_CD_TIPO_CONTENUTO_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_tipo_contenuto_2, value);
	}

	public String getS181_CD_MOVIMENTO_2() {
		return S181_CD_MOVIMENTO_2;
	}

	public void setS181_CD_MOVIMENTO_2(String value) {
		S181_CD_MOVIMENTO_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_movimento_2, value);
	}

	public String getS181_CD_DIMENSIONE_2() {
		return S181_CD_DIMENSIONE_2;
	}

	public void setS181_CD_DIMENSIONE_2(String value) {
		S181_CD_DIMENSIONE_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_dimensione_2, value);
	}

	public String getS181_CD_SENSORIALE_1_2() {
		return S181_CD_SENSORIALE_1_2;
	}

	public void setS181_CD_SENSORIALE_1_2(String value) {
		S181_CD_SENSORIALE_1_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_1_2, value);
	}

	public String getS181_CD_SENSORIALE_2_2() {
		return S181_CD_SENSORIALE_2_2;
	}

	public void setS181_CD_SENSORIALE_2_2(String value) {
		S181_CD_SENSORIALE_2_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_2_2, value);
	}

	public String getS181_CD_SENSORIALE_3_2() {
		return S181_CD_SENSORIALE_3_2;
	}

	public void setS181_CD_SENSORIALE_3_2(String value) {
		S181_CD_SENSORIALE_3_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_3_2, value);
	}

	public String getS182_TP_MEDIAZIONE_1() {
		return S182_TP_MEDIAZIONE_1;
	}

	public void setS182_TP_MEDIAZIONE_1(String value) {
		S182_TP_MEDIAZIONE_1 = value;
		this.settaParametro(KeyParameter.XXXs182_tp_mediazione_1, value);
	}

	public String getS182_TP_MEDIAZIONE_2() {
		return S182_TP_MEDIAZIONE_2;
	}

	public void setS182_TP_MEDIAZIONE_2(String value) {
		S182_TP_MEDIAZIONE_2 = value;
		this.settaParametro(KeyParameter.XXXs182_tp_mediazione_2, value);
	}

	public String getS125_INDICATORE_TESTO() {
		return S125_INDICATORE_TESTO;
	}

	public void setS125_INDICATORE_TESTO(String value) {
		S125_INDICATORE_TESTO = value;
		this.settaParametro(KeyParameter.XXXs125_indicatore_testo, value);
	}

	public String getS183_TP_SUPPORTO_1() {
		return S183_TP_SUPPORTO_1;
	}

	public void setS183_TP_SUPPORTO_1(String value) {
		S183_TP_SUPPORTO_1 = value;
		this.settaParametro(KeyParameter.XXXs183_tp_supporto_1, value);
	}

	public String getS183_TP_SUPPORTO_2() {
		return S183_TP_SUPPORTO_2;
	}

	public void setS183_TP_SUPPORTO_2(String value) {
		S183_TP_SUPPORTO_2 = value;
		this.settaParametro(KeyParameter.XXXs183_tp_supporto_2, value);
	}

	public String getS210_IND_PUBBLICATO() {
		return S210_IND_PUBBLICATO;
	}

	public void setS210_IND_PUBBLICATO(String value) {
		S210_IND_PUBBLICATO = value;
		this.settaParametro(KeyParameter.XXXs210_ind_pubblicato, value);
	}

}
