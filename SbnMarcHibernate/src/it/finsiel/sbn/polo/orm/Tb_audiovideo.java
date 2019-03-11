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

/**
 * ORM-Persistable Class
 */
public class Tb_audiovideo extends OggettoServerSbnMarc {

	private static final long serialVersionUID = 5940230110402280160L;

	private String BID;

	private String CD_LIVELLO;
	private String TP_MATER_AUDIOVIS;

	private Integer LUNGHEZZA;
	private String CD_COLORE;
	private String CD_SUONO;
	private String TP_MEDIA_SUONO;
	private String CD_DIMENSIONE;
	private String CD_FORMA_VIDEO;
	private String CD_TECNICA;
	private String TP_FORMATO_FILM;

	private String CD_MAT_ACCOMP_1;
	private String CD_MAT_ACCOMP_2;
	private String CD_MAT_ACCOMP_3;
	private String CD_MAT_ACCOMP_4;

	private String CD_FORMA_REGIST;
	private String TP_FORMATO_VIDEO;
	private String CD_MATERIALE_BASE;
	private String CD_SUPPORTO_SECOND;
	private String CD_BROADCAST;
	private String TP_GENERAZIONE;
	private String CD_ELEMENTI;
	private String CD_CATEG_COLORE;
	private String CD_POLARITA;
	private String CD_PELLICOLA;
	private String TP_SUONO;
	private String TP_STAMPA_FILM;
	private String CD_DETERIORE;
	private String CD_COMPLETO;

	private Integer DT_ISPEZIONE;

	public void setTP_MATER_AUDIOVIS(String value) {
		this.TP_MATER_AUDIOVIS = value;
		this.settaParametro(KeyParameter.XXXtp_mater_audiovis, value);
	}

	public String getTP_MATER_AUDIOVIS() {
		return TP_MATER_AUDIOVIS;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_colore, value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_SUONO(String value) {
		this.CD_SUONO = value;
		this.settaParametro(KeyParameter.XXXcd_suono, value);
	}

	public String getCD_SUONO() {
		return CD_SUONO;
	}

	public void setTP_MEDIA_SUONO(String value) {
		this.TP_MEDIA_SUONO = value;
		this.settaParametro(KeyParameter.XXXtp_media_suono, value);
	}

	public String getTP_MEDIA_SUONO() {
		return TP_MEDIA_SUONO;
	}

	public void setCD_DIMENSIONE(String value) {
		this.CD_DIMENSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_dimensione, value);
	}

	public String getCD_DIMENSIONE() {
		return CD_DIMENSIONE;
	}

	public void setCD_FORMA_VIDEO(String value) {
		this.CD_FORMA_VIDEO = value;
		this.settaParametro(KeyParameter.XXXcd_forma_video, value);
	}

	public String getCD_FORMA_VIDEO() {
		return CD_FORMA_VIDEO;
	}

	public void setCD_TECNICA(String value) {
		this.CD_TECNICA = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica, value);
	}

	public String getCD_TECNICA() {
		return CD_TECNICA;
	}

	public void setTP_FORMATO_FILM(String value) {
		this.TP_FORMATO_FILM = value;
		this.settaParametro(KeyParameter.XXXtp_formato_film, value);
	}

	public String getTP_FORMATO_FILM() {
		return TP_FORMATO_FILM;
	}

	public void setCD_FORMA_REGIST(String value) {
		this.CD_FORMA_REGIST = value;
		this.settaParametro(KeyParameter.XXXcd_forma_regist, value);
	}

	public String getCD_FORMA_REGIST() {
		return CD_FORMA_REGIST;
	}

	public void setTP_FORMATO_VIDEO(String value) {
		this.TP_FORMATO_VIDEO = value;
		this.settaParametro(KeyParameter.XXXtp_formato_video, value);
	}

	public String getTP_FORMATO_VIDEO() {
		return TP_FORMATO_VIDEO;
	}

	public void setCD_MATERIALE_BASE(String value) {
		this.CD_MATERIALE_BASE = value;
		this.settaParametro(KeyParameter.XXXcd_materiale_base, value);
	}

	public String getCD_MATERIALE_BASE() {
		return CD_MATERIALE_BASE;
	}

	public void setCD_SUPPORTO_SECOND(String value) {
		this.CD_SUPPORTO_SECOND = value;
		this.settaParametro(KeyParameter.XXXcd_supporto_second, value);
	}

	public String getCD_SUPPORTO_SECOND() {
		return CD_SUPPORTO_SECOND;
	}

	public void setCD_BROADCAST(String value) {
		this.CD_BROADCAST = value;
		this.settaParametro(KeyParameter.XXXcd_broadcast, value);
	}

	public String getCD_BROADCAST() {
		return CD_BROADCAST;
	}

	public void setTP_GENERAZIONE(String value) {
		this.TP_GENERAZIONE = value;
		this.settaParametro(KeyParameter.XXXtp_generazione, value);
	}

	public String getTP_GENERAZIONE() {
		return TP_GENERAZIONE;
	}

	public void setCD_ELEMENTI(String value) {
		this.CD_ELEMENTI = value;
		this.settaParametro(KeyParameter.XXXcd_elementi, value);
	}

	public String getCD_ELEMENTI() {
		return CD_ELEMENTI;
	}

	public void setCD_CATEG_COLORE(String value) {
		this.CD_CATEG_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_categ_colore, value);
	}

	public String getCD_CATEG_COLORE() {
		return CD_CATEG_COLORE;
	}

	public void setCD_POLARITA(String value) {
		this.CD_POLARITA = value;
		this.settaParametro(KeyParameter.XXXcd_polarita, value);
	}

	public String getCD_POLARITA() {
		return CD_POLARITA;
	}

	public void setCD_PELLICOLA(String value) {
		this.CD_PELLICOLA = value;
		this.settaParametro(KeyParameter.XXXcd_pellicola, value);
	}

	public String getCD_PELLICOLA() {
		return CD_PELLICOLA;
	}

	public void setTP_SUONO(String value) {
		this.TP_SUONO = value;
		this.settaParametro(KeyParameter.XXXtp_suono, value);
	}

	public String getTP_SUONO() {
		return TP_SUONO;
	}

	public void setTP_STAMPA_FILM(String value) {
		this.TP_STAMPA_FILM = value;
		this.settaParametro(KeyParameter.XXXtp_stampa_film, value);
	}

	public String getTP_STAMPA_FILM() {
		return TP_STAMPA_FILM;
	}

	public void setCD_DETERIORE(String value) {
		this.CD_DETERIORE = value;
		this.settaParametro(KeyParameter.XXXcd_deteriore, value);
	}

	public String getCD_DETERIORE() {
		return CD_DETERIORE;
	}

	public void setCD_COMPLETO(String value) {
		this.CD_COMPLETO = value;
		this.settaParametro(KeyParameter.XXXcd_completo, value);
	}

	public String getCD_COMPLETO() {
		return CD_COMPLETO;
	}

	public void setBID(String value) {
		this.BID = value;
		this.settaParametro(KeyParameter.XXXbid, value);
	}

	public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setCD_LIVELLO(String value) {
		CD_LIVELLO = value;
		this.settaParametro(KeyParameter.XXXcd_livello, value);
	}

	public String getCD_MAT_ACCOMP_1() {
		return CD_MAT_ACCOMP_1;
	}

	public void setCD_MAT_ACCOMP_1(String value) {
		CD_MAT_ACCOMP_1 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_1, value);
	}

	public String getCD_MAT_ACCOMP_2() {
		return CD_MAT_ACCOMP_2;
	}

	public void setCD_MAT_ACCOMP_2(String value) {
		CD_MAT_ACCOMP_2 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_2, value);
	}

	public String getCD_MAT_ACCOMP_3() {
		return CD_MAT_ACCOMP_3;
	}

	public void setCD_MAT_ACCOMP_3(String value) {
		CD_MAT_ACCOMP_3 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_3, value);
	}

	public String getCD_MAT_ACCOMP_4() {
		return CD_MAT_ACCOMP_4;
	}

	public void setCD_MAT_ACCOMP_4(String value) {
		CD_MAT_ACCOMP_4 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_4, value);
	}

	public Integer getDT_ISPEZIONE() {
		return DT_ISPEZIONE;
	}

	public void setDT_ISPEZIONE(Integer value) {
		DT_ISPEZIONE = value;
		this.settaParametro(KeyParameter.XXXdt_ispezione, value);
	}

	public Integer getLUNGHEZZA() {
		return LUNGHEZZA;
	}

	public void setLUNGHEZZA(Integer value) {
		LUNGHEZZA = value;
		this.settaParametro(KeyParameter.XXXlunghezza, value);
	}

}
