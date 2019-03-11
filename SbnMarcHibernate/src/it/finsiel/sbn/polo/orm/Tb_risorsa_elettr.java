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
public class Tb_risorsa_elettr extends OggettoServerSbnMarc {

	private static final long serialVersionUID = -1938897474485023153L;

	private String BID;

	private String CD_LIVELLO;
	private String TP_RISORSA;
	private String CD_DESIGNAZIONE;
	private String CD_COLORE;
	private String CD_DIMENSIONE;
	private String CD_SUONO;
	private String CD_BIT_IMMAGINE;
	private String CD_FORMATO_FILE;
	private String CD_QUALITA;
	private String CD_ORIGINE;
	private String CD_COMPRESSIONE;
	private String CD_RIFORMATTAZIONE;

	public void setTP_RISORSA(String value) {
		this.TP_RISORSA = value;
		this.settaParametro(KeyParameter.XXXtp_risorsa, value);
	}

	public String getTP_RISORSA() {
		return TP_RISORSA;
	}

	public void setCD_DESIGNAZIONE(String value) {
		this.CD_DESIGNAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_designazione, value);
	}

	public String getCD_DESIGNAZIONE() {
		return CD_DESIGNAZIONE;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_colore, value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_DIMENSIONE(String value) {
		this.CD_DIMENSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_dimensione, value);
	}

	public String getCD_DIMENSIONE() {
		return CD_DIMENSIONE;
	}

	public void setCD_SUONO(String value) {
		this.CD_SUONO = value;
		this.settaParametro(KeyParameter.XXXcd_suono, value);
	}

	public String getCD_SUONO() {
		return CD_SUONO;
	}

	public void setCD_BIT_IMMAGINE(String value) {
		this.CD_BIT_IMMAGINE = value;
		this.settaParametro(KeyParameter.XXXcd_bit_immagine, value);
	}

	public String getCD_BIT_IMMAGINE() {
		return CD_BIT_IMMAGINE;
	}

	public void setCD_FORMATO_FILE(String value) {
		this.CD_FORMATO_FILE = value;
		this.settaParametro(KeyParameter.XXXcd_formato_file, value);
	}

	public String getCD_FORMATO_FILE() {
		return CD_FORMATO_FILE;
	}

	public void setCD_QUALITA(String value) {
		this.CD_QUALITA = value;
		this.settaParametro(KeyParameter.XXXcd_qualita, value);
	}

	public String getCD_QUALITA() {
		return CD_QUALITA;
	}

	public void setCD_ORIGINE(String value) {
		this.CD_ORIGINE = value;
		this.settaParametro(KeyParameter.XXXcd_origine, value);
	}

	public String getCD_ORIGINE() {
		return CD_ORIGINE;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
		this.settaParametro(KeyParameter.XXXcd_livello, value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
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

	public String getCD_COMPRESSIONE() {
		return CD_COMPRESSIONE;
	}

	public void setCD_COMPRESSIONE(String value) {
		CD_COMPRESSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_compressione, value);
	}

	public String getCD_RIFORMATTAZIONE() {
		return CD_RIFORMATTAZIONE;
	}

	public void setCD_RIFORMATTAZIONE(String value) {
		CD_RIFORMATTAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_riformattazione, value);
	}

}
