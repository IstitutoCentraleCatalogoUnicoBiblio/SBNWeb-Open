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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tbf_default implements Serializable {

	private static final long serialVersionUID = 2770051788985714193L;

	public Tbf_default() {
	}

	private int id_default;

	private String key;

	private String tipo;

	private Integer lunghezza;

	private String id_etichetta;

	private String codice_attivita;

	private String codice_tabella_validazione;

	private Integer seq_ordinamento;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppi_default tbf_gruppi_default;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_config_default tbf_config_default__id_config;

	private String bundle;

	private java.util.Set Tbf_bibliotecario_Default = new java.util.HashSet();

	private java.util.Set Tbf_biblioteca_default = new java.util.HashSet();

	private java.util.Set Tbf_polo_default = new java.util.HashSet();

	private void setId_default(int value) {
		this.id_default = value;
	}

	public int getId_default() {
		return id_default;
	}

	public int getORMID() {
		return getId_default();
	}

	public void setKey(String value) {
		this.key = value;
	}

	public String getKey() {
		return key;
	}

	public void setTipo(String value) {
		this.tipo = value;
	}

	public String getTipo() {
		return tipo;
	}

	public void setLunghezza(int value) {
		setLunghezza(new Integer(value));
	}

	public void setLunghezza(Integer value) {
		this.lunghezza = value;
	}

	public Integer getLunghezza() {
		return lunghezza;
	}

	/**
	 * Potrebbe non esssere utilizzata se al posto usiamo il campo key per prendere l'etichetta in lingua a un file di properties
	 */
	public void setId_etichetta(String value) {
		this.id_etichetta = value;
	}

	/**
	 * Potrebbe non esssere utilizzata se al posto usiamo il campo key per prendere l'etichetta in lingua a un file di properties
	 */
	public String getId_etichetta() {
		return id_etichetta;
	}

	public void setCodice_attivita(String value) {
		this.codice_attivita = value;
	}

	public String getCodice_attivita() {
		return codice_attivita;
	}

	/**
	 * Per prendere le liste (eg. lingua) dalle tabelle di validazione
	 */
	public void setCodice_tabella_validazione(String value) {
		this.codice_tabella_validazione = value;
	}

	/**
	 * Per prendere le liste (eg. lingua) dalle tabelle di validazione
	 */
	public String getCodice_tabella_validazione() {
		return codice_tabella_validazione;
	}

	/**
	 * Identifica il posizionamento nella maschera di prospettazione
	 */
	public void setSeq_ordinamento(int value) {
		setSeq_ordinamento(new Integer(value));
	}

	/**
	 * Identifica il posizionamento nella maschera di prospettazione
	 */
	public void setSeq_ordinamento(Integer value) {
		this.seq_ordinamento = value;
	}

	/**
	 * Identifica il posizionamento nella maschera di prospettazione
	 */
	public Integer getSeq_ordinamento() {
		return seq_ordinamento;
	}

	/**
	 * Indicazione del bundle per le properties
	 */
	public void setBundle(String value) {
		this.bundle = value;
	}

	/**
	 * Indicazione del bundle per le properties
	 */
	public String getBundle() {
		return bundle;
	}

	public void setTbf_gruppi_default(it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppi_default value) {
		this.tbf_gruppi_default = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppi_default getTbf_gruppi_default() {
		return tbf_gruppi_default;
	}

	public void setTbf_config_default__id_config(it.iccu.sbn.polo.orm.amministrazione.Tbf_config_default value) {
		this.tbf_config_default__id_config = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_config_default getTbf_config_default__id_config() {
		return tbf_config_default__id_config;
	}

	public void setTbf_bibliotecario_Default(java.util.Set value) {
		this.Tbf_bibliotecario_Default = value;
	}

	public java.util.Set getTbf_bibliotecario_Default() {
		return Tbf_bibliotecario_Default;
	}


	public void setTbf_biblioteca_default(java.util.Set value) {
		this.Tbf_biblioteca_default = value;
	}

	public java.util.Set getTbf_biblioteca_default() {
		return Tbf_biblioteca_default;
	}


	public void setTbf_polo_default(java.util.Set value) {
		this.Tbf_polo_default = value;
	}

	public java.util.Set getTbf_polo_default() {
		return Tbf_polo_default;
	}


	private String id_sezione;

	public String toString() {
		return String.valueOf(getId_default());
	}

}
