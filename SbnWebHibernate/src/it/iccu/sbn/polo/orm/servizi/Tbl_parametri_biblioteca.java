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
public class Tbl_parametri_biblioteca implements Serializable {

	private static final long serialVersionUID = 665151460735695955L;

	public Tbl_parametri_biblioteca() {
	}

	private int id_parametri_biblioteca;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private short num_lettere;

	private short num_gg_ritardo1;

	private short num_gg_ritardo2;

	private short num_gg_ritardo3;

	private short num_prenotazioni;

	private java.sql.Timestamp ts_ins;

	private java.sql.Timestamp ts_var;

	private String ute_ins;

	private String ute_var;

	private char fl_catfrui_nosbndoc;

	private String cd_catfrui_nosbndoc;

	private String cd_catriprod_nosbndoc;

	private Character cod_modalita_invio1_sollecito1;

	private Character cod_modalita_invio2_sollecito1;

	private Character cod_modalita_invio3_sollecito1;

	private Character cod_modalita_invio1_sollecito2;

	private Character cod_modalita_invio2_sollecito2;

	private Character cod_modalita_invio3_sollecito2;

	private Character cod_modalita_invio1_sollecito3;

	private Character cod_modalita_invio2_sollecito3;

	private Character cod_modalita_invio3_sollecito3;

	private short num_gg_validita_prelazione;

	private char ammessa_autoregistrazione_utente;

	private char ammesso_inserimento_utente;

	private char anche_da_remoto;

	private String cd_cat_med_digit;

	private String xml_modello_soll;

	private Character fl_tipo_rinnovo;

	private Short num_gg_rinnovo_richiesta;

	private Character fl_priorita_prenot;

	private String email_notifica;

	private Tbl_tipi_autorizzazioni id_autorizzazione_ill;

	private Character fl_att_servizi_ill;

	public char getFl_catfrui_nosbndoc() {
		return fl_catfrui_nosbndoc;
	}

	public void setFl_catfrui_nosbndoc(char fl_catfrui_nosbndoc) {
		this.fl_catfrui_nosbndoc = fl_catfrui_nosbndoc;
	}

	public String getCd_catfrui_nosbndoc() {
		return cd_catfrui_nosbndoc;
	}

	public void setCd_catfrui_nosbndoc(String cd_catfrui_nosbndoc) {
		this.cd_catfrui_nosbndoc = cd_catfrui_nosbndoc;
	}

	public String getCd_catriprod_nosbndoc() {
		return cd_catriprod_nosbndoc;
	}

	public void setCd_catriprod_nosbndoc(String cd_catriprod_nosbndoc) {
		this.cd_catriprod_nosbndoc = cd_catriprod_nosbndoc;
	}

	public void setNum_lettere(short value) {
		this.num_lettere = value;
	}

	public short getNum_lettere() {
		return num_lettere;
	}

	public void setNum_gg_ritardo1(short value) {
		this.num_gg_ritardo1 = value;
	}

	public short getNum_gg_ritardo1() {
		return num_gg_ritardo1;
	}

	public void setNum_gg_ritardo2(short value) {
		this.num_gg_ritardo2 = value;
	}

	public short getNum_gg_ritardo2() {
		return num_gg_ritardo2;
	}

	public void setNum_gg_ritardo3(short value) {
		this.num_gg_ritardo3 = value;
	}

	public short getNum_gg_ritardo3() {
		return num_gg_ritardo3;
	}

	public void setNum_prenotazioni(short value) {
		this.num_prenotazioni = value;
	}

	public short getNum_prenotazioni() {
		return num_prenotazioni;
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

	private void setId_parametri_biblioteca(int value) {
		this.id_parametri_biblioteca = value;
	}

	public int getId_parametri_biblioteca() {
		return id_parametri_biblioteca;
	}

	public int getORMID() {
		return getId_parametri_biblioteca();
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public String toString() {
		return String.valueOf(getId_parametri_biblioteca());
	}

	public Character getCod_modalita_invio1_sollecito1() {
		return cod_modalita_invio1_sollecito1;
	}

	public void setCod_modalita_invio1_sollecito1(
			Character cod_modalita_invio1_sollecito1) {
		this.cod_modalita_invio1_sollecito1 = cod_modalita_invio1_sollecito1;
	}

	public Character getCod_modalita_invio2_sollecito1() {
		return cod_modalita_invio2_sollecito1;
	}

	public void setCod_modalita_invio2_sollecito1(
			Character cod_modalita_invio2_sollecito1) {
		this.cod_modalita_invio2_sollecito1 = cod_modalita_invio2_sollecito1;
	}

	public Character getCod_modalita_invio3_sollecito1() {
		return cod_modalita_invio3_sollecito1;
	}

	public void setCod_modalita_invio3_sollecito1(
			Character cod_modalita_invio3_sollecito1) {
		this.cod_modalita_invio3_sollecito1 = cod_modalita_invio3_sollecito1;
	}

	public Character getCod_modalita_invio1_sollecito2() {
		return cod_modalita_invio1_sollecito2;
	}

	public void setCod_modalita_invio1_sollecito2(
			Character cod_modalita_invio1_sollecito2) {
		this.cod_modalita_invio1_sollecito2 = cod_modalita_invio1_sollecito2;
	}

	public Character getCod_modalita_invio2_sollecito2() {
		return cod_modalita_invio2_sollecito2;
	}

	public void setCod_modalita_invio2_sollecito2(
			Character cod_modalita_invio2_sollecito2) {
		this.cod_modalita_invio2_sollecito2 = cod_modalita_invio2_sollecito2;
	}

	public Character getCod_modalita_invio3_sollecito2() {
		return cod_modalita_invio3_sollecito2;
	}

	public void setCod_modalita_invio3_sollecito2(
			Character cod_modalita_invio3_sollecito2) {
		this.cod_modalita_invio3_sollecito2 = cod_modalita_invio3_sollecito2;
	}

	public Character getCod_modalita_invio1_sollecito3() {
		return cod_modalita_invio1_sollecito3;
	}

	public void setCod_modalita_invio1_sollecito3(
			Character cod_modalita_invio1_sollecito3) {
		this.cod_modalita_invio1_sollecito3 = cod_modalita_invio1_sollecito3;
	}

	public Character getCod_modalita_invio2_sollecito3() {
		return cod_modalita_invio2_sollecito3;
	}

	public void setCod_modalita_invio2_sollecito3(
			Character cod_modalita_invio2_sollecito3) {
		this.cod_modalita_invio2_sollecito3 = cod_modalita_invio2_sollecito3;
	}

	public Character getCod_modalita_invio3_sollecito3() {
		return cod_modalita_invio3_sollecito3;
	}

	public void setCod_modalita_invio3_sollecito3(
			Character cod_modalita_invio3_sollecito3) {
		this.cod_modalita_invio3_sollecito3 = cod_modalita_invio3_sollecito3;
	}

	public short getNum_gg_validita_prelazione() {
		return num_gg_validita_prelazione;
	}

	public void setNum_gg_validita_prelazione(short num_gg_validita_prelazione) {
		this.num_gg_validita_prelazione = num_gg_validita_prelazione;
	}

	public char getAmmessa_autoregistrazione_utente() {
		return ammessa_autoregistrazione_utente;
	}

	public void setAmmessa_autoregistrazione_utente(
			char ammessa_autoregistrazione_utente) {
		this.ammessa_autoregistrazione_utente = ammessa_autoregistrazione_utente;
	}

	public char getAmmesso_inserimento_utente() {
		return ammesso_inserimento_utente;
	}

	public void setAmmesso_inserimento_utente(char ammesso_inserimento_utente) {
		this.ammesso_inserimento_utente = ammesso_inserimento_utente;
	}

	public char getAnche_da_remoto() {
		return anche_da_remoto;
	}

	public void setAnche_da_remoto(char anche_da_remoto) {
		this.anche_da_remoto = anche_da_remoto;
	}

	public String getXml_modello_soll() {
		return xml_modello_soll;
	}

	public void setXml_modello_soll(String xml_modello_soll) {
		this.xml_modello_soll = xml_modello_soll;
	}

	public String getCd_cat_med_digit() {
		return cd_cat_med_digit;
	}

	public void setCd_cat_med_digit(String cd_cat_med_digit) {
		this.cd_cat_med_digit = cd_cat_med_digit;
	}

	public Character getFl_tipo_rinnovo() {
		return fl_tipo_rinnovo;
	}

	public void setFl_tipo_rinnovo(Character fl_tipo_rinnovo) {
		this.fl_tipo_rinnovo = fl_tipo_rinnovo;
	}

	public Short getNum_gg_rinnovo_richiesta() {
		return num_gg_rinnovo_richiesta;
	}

	public void setNum_gg_rinnovo_richiesta(Short num_gg_rinnovo_richiesta) {
		this.num_gg_rinnovo_richiesta = num_gg_rinnovo_richiesta;
	}

	public Character getFl_priorita_prenot() {
		return fl_priorita_prenot;
	}

	public void setFl_priorita_prenot(Character fl_priorita_prenot) {
		this.fl_priorita_prenot = fl_priorita_prenot;
	}

	public String getEmail_notifica() {
		return email_notifica;
	}

	public void setEmail_notifica(String email_notifica) {
		this.email_notifica = email_notifica;
	}

	public Tbl_tipi_autorizzazioni getId_autorizzazione_ill() {
		return id_autorizzazione_ill;
	}

	public void setId_autorizzazione_ill(Tbl_tipi_autorizzazioni id_autorizzazione_ill) {
		this.id_autorizzazione_ill = id_autorizzazione_ill;
	}

	public Character getFl_att_servizi_ill() {
		return fl_att_servizi_ill;
	}

	public void setFl_att_servizi_ill(Character fl_att_servizi_ill) {
		this.fl_att_servizi_ill = fl_att_servizi_ill;
	}

}
