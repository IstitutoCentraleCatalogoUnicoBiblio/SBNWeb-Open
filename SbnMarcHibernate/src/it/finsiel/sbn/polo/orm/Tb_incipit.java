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

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tb_incipit extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -7365085244247003159L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_incipit))
			return false;
		Tb_incipit tb_incipit = (Tb_incipit)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tb_incipit.getBID()))
			return false;
		if (getNUMERO_MOV() != null && !getNUMERO_MOV().equals(tb_incipit.getNUMERO_MOV()))
			return false;
		if (getNUMERO_P_MOV() != null && !getNUMERO_P_MOV().equals(tb_incipit.getNUMERO_P_MOV()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		hashcode = hashcode + getNUMERO_MOV().hashCode();
		hashcode = hashcode + getNUMERO_P_MOV().hashCode();
		return hashcode;
	}

	private String BID;

	private String NUMERO_MOV;

	private String BID_LETTERARIO;

	private String TP_INDICATORE;

	private String NUMERO_COMP;

	private String NUMERO_P_MOV;

	private String REGISTRO_MUS;

	private String NOME_PERSONAGGIO;

	private String TEMPO_MUS;

	private String CD_FORMA;

	private String CD_TONALITA;

	private String CHIAVE_MUS;

	private String ALTERAZIONE;

	private String MISURA;

	private String DS_CONTESTO;

	public void setNUMERO_MOV(String value) {
		this.NUMERO_MOV = value;
    this.settaParametro(KeyParameter.XXXnumero_mov,value);
	}

	public String getNUMERO_MOV() {
		return NUMERO_MOV;
	}

	public void setTP_INDICATORE(String value) {
		this.TP_INDICATORE = value;
    this.settaParametro(KeyParameter.XXXtp_indicatore,value);
	}

	public String getTP_INDICATORE() {
		return TP_INDICATORE;
	}

	public void setNUMERO_COMP(String value) {
		this.NUMERO_COMP = value;
    this.settaParametro(KeyParameter.XXXnumero_comp,value);
	}

	public String getNUMERO_COMP() {
		return NUMERO_COMP;
	}

	public void setNUMERO_P_MOV(String value) {
		this.NUMERO_P_MOV = value;
    this.settaParametro(KeyParameter.XXXnumero_p_mov,value);
	}

	public String getNUMERO_P_MOV() {
		return NUMERO_P_MOV;
	}

	public void setREGISTRO_MUS(String value) {
		this.REGISTRO_MUS = value;
    this.settaParametro(KeyParameter.XXXregistro_mus,value);
	}

	public String getREGISTRO_MUS() {
		return REGISTRO_MUS;
	}

	public void setNOME_PERSONAGGIO(String value) {
		this.NOME_PERSONAGGIO = value;
    this.settaParametro(KeyParameter.XXXnome_personaggio,value);
	}

	public String getNOME_PERSONAGGIO() {
		return NOME_PERSONAGGIO;
	}

	public void setTEMPO_MUS(String value) {
		this.TEMPO_MUS = value;
    this.settaParametro(KeyParameter.XXXtempo_mus,value);
	}

	public String getTEMPO_MUS() {
		return TEMPO_MUS;
	}

	public void setCD_FORMA(String value) {
		this.CD_FORMA = value;
    this.settaParametro(KeyParameter.XXXcd_forma,value);
	}

	public String getCD_FORMA() {
		return CD_FORMA;
	}

	public void setCD_TONALITA(String value) {
		this.CD_TONALITA = value;
    this.settaParametro(KeyParameter.XXXcd_tonalita,value);
	}

	public String getCD_TONALITA() {
		return CD_TONALITA;
	}

	public void setCHIAVE_MUS(String value) {
		this.CHIAVE_MUS = value;
    this.settaParametro(KeyParameter.XXXchiave_mus,value);
	}

	public String getCHIAVE_MUS() {
		return CHIAVE_MUS;
	}

	public void setALTERAZIONE(String value) {
		this.ALTERAZIONE = value;
    this.settaParametro(KeyParameter.XXXalterazione,value);
	}

	public String getALTERAZIONE() {
		return ALTERAZIONE;
	}

	public void setMISURA(String value) {
		this.MISURA = value;
    this.settaParametro(KeyParameter.XXXmisura,value);
	}

	public String getMISURA() {
		return MISURA;
	}

	public void setDS_CONTESTO(String value) {
		this.DS_CONTESTO = value;
    this.settaParametro(KeyParameter.XXXds_contesto,value);
	}

	public String getDS_CONTESTO() {
		return DS_CONTESTO;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setBID_LETTERARIO(String value) {
		this.BID_LETTERARIO = value;
    this.settaParametro(KeyParameter.XXXbid_letterario,value);
	}

 public String getBID_LETTERARIO() {
		return BID_LETTERARIO;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + getNUMERO_MOV() + " " + getNUMERO_P_MOV());
	}
}
