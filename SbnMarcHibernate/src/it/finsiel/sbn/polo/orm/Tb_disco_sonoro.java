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

public class Tb_disco_sonoro extends OggettoServerSbnMarc {

	private static final long serialVersionUID = -7282985974774138729L;

	private String BID;

	private String CD_LIVELLO;
	private String CD_FORMA;
	private String CD_VELOCITA;
	private String TP_SUONO;
	private String CD_PISTA;
	private String CD_DIMENSIONE;
	private String CD_LARG_NASTRO;
	private String CD_CONFIGURAZIONE;

	private String CD_MATER_ACCOMP_1;
	private String CD_MATER_ACCOMP_2;
	private String CD_MATER_ACCOMP_3;
	private String CD_MATER_ACCOMP_4;
	private String CD_MATER_ACCOMP_5;
	private String CD_MATER_ACCOMP_6;

	private String CD_TECNICA_REGIS;
	private String CD_RIPRODUZIONE;
	private String TP_DISCO;
	private String TP_TAGLIO;

	private String TP_MATERIALE;
	private String DURATA;

	public void setCD_FORMA(String value) {
		this.CD_FORMA = value;
		this.settaParametro(KeyParameter.XXXcd_forma, value);
	}

	public String getCD_FORMA() {
		return CD_FORMA;
	}

	public void setCD_VELOCITA(String value) {
		this.CD_VELOCITA = value;
		this.settaParametro(KeyParameter.XXXcd_velocita, value);
	}

	public String getCD_VELOCITA() {
		return CD_VELOCITA;
	}

	public void setTP_SUONO(String value) {
		this.TP_SUONO = value;
		this.settaParametro(KeyParameter.XXXtp_suono, value);
	}

	public String getTP_SUONO() {
		return TP_SUONO;
	}

	public void setCD_PISTA(String value) {
		this.CD_PISTA = value;
		this.settaParametro(KeyParameter.XXXcd_pista, value);
	}

	public String getCD_PISTA() {
		return CD_PISTA;
	}

	public void setCD_DIMENSIONE(String value) {
		this.CD_DIMENSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_dimensione, value);
	}

	public String getCD_DIMENSIONE() {
		return CD_DIMENSIONE;
	}

	public void setCD_LARG_NASTRO(String value) {
		this.CD_LARG_NASTRO = value;
		this.settaParametro(KeyParameter.XXXcd_larg_nastro, value);
	}

	public String getCD_LARG_NASTRO() {
		return CD_LARG_NASTRO;
	}

	public void setCD_CONFIGURAZIONE(String value) {
		this.CD_CONFIGURAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_configurazione, value);
	}

	public String getCD_CONFIGURAZIONE() {
		return CD_CONFIGURAZIONE;
	}

	public void setCD_TECNICA_REGIS(String value) {
		this.CD_TECNICA_REGIS = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_regis, value);
	}

	public String getCD_TECNICA_REGIS() {
		return CD_TECNICA_REGIS;
	}

	public void setCD_RIPRODUZIONE(String value) {
		this.CD_RIPRODUZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_riproduzione, value);
	}

	public String getCD_RIPRODUZIONE() {
		return CD_RIPRODUZIONE;
	}

	public void setTP_DISCO(String value) {
		this.TP_DISCO = value;
		this.settaParametro(KeyParameter.XXXtp_disco, value);
	}

	public String getTP_DISCO() {
		return TP_DISCO;
	}

	public void setTP_TAGLIO(String value) {
		this.TP_TAGLIO = value;
		this.settaParametro(KeyParameter.XXXtp_taglio, value);
	}

	public String getTP_TAGLIO() {
		return TP_TAGLIO;
	}

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

	public String getCD_MATER_ACCOMP_1() {
		return CD_MATER_ACCOMP_1;
	}

	public void setCD_MATER_ACCOMP_1(String value) {
		CD_MATER_ACCOMP_1 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_1, value);
	}

	public String getCD_MATER_ACCOMP_2() {
		return CD_MATER_ACCOMP_2;
	}

	public void setCD_MATER_ACCOMP_2(String value) {
		CD_MATER_ACCOMP_2 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_2, value);
	}

	public String getCD_MATER_ACCOMP_3() {
		return CD_MATER_ACCOMP_3;
	}

	public void setCD_MATER_ACCOMP_3(String value) {
		CD_MATER_ACCOMP_3 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_3, value);
	}

	public String getCD_MATER_ACCOMP_4() {
		return CD_MATER_ACCOMP_4;
	}

	public void setCD_MATER_ACCOMP_4(String value) {
		CD_MATER_ACCOMP_4 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_4, value);
	}

	public String getCD_MATER_ACCOMP_5() {
		return CD_MATER_ACCOMP_5;
	}

	public void setCD_MATER_ACCOMP_5(String value) {
		CD_MATER_ACCOMP_5 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_5, value);
	}

	public String getCD_MATER_ACCOMP_6() {
		return CD_MATER_ACCOMP_6;
	}

	public void setCD_MATER_ACCOMP_6(String value) {
		CD_MATER_ACCOMP_6 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_6, value);
	}

	public String getTP_MATERIALE() {
		return TP_MATERIALE;
	}

	public void setTP_MATERIALE(String value) {
		TP_MATERIALE = value;
		this.settaParametro(KeyParameter.XXXtp_materiale, value);
	}

	public String getDURATA() {
		return DURATA;
	}

	public void setDURATA(String value) {
		DURATA = value;
		this.settaParametro(KeyParameter.XXXdurata, value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setCD_LIVELLO(String value) {
		CD_LIVELLO = value;
		this.settaParametro(KeyParameter.XXXcd_livello, value);
	}

}
