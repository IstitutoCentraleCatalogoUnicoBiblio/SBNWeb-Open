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
public class Tbf_config_default implements Serializable {

	private static final long serialVersionUID = -571376594925576034L;

	public Tbf_config_default() {
	}

	private int id_config;

	private String id_area_sezione;

	private String parent;

	private int seq_ordinamento;

	private String codice_attivita;

	private String parametro_attivita;

	private Short codice_modulo;

	private java.util.Set Tbf_default = new java.util.HashSet();

	private void setId_config(int value) {
		this.id_config = value;
	}

	public int getId_config() {
		return id_config;
	}

	public int getORMID() {
		return getId_config();
	}

	/**
	 * eg: 00_GestBib (00_ per definire un'area) e 01_GestBibInterrogazione (NN_ con nn diveso dad 00 per indicare una sezione. Una sezione ha un parent con codice area)
	 */
	public void setId_area_sezione(String value) {
		this.id_area_sezione = value;
	}

	/**
	 * eg: 00_GestBib (00_ per definire un'area) e 01_GestBibInterrogazione (NN_ con nn diveso dad 00 per indicare una sezione. Una sezione ha un parent con codice area)
	 */
	public String getId_area_sezione() {
		return id_area_sezione;
	}

	/**
	 * Id del codice are a cui la sezione appartiene. Eg  la sezioen 01_GestBibInterrogazione appartiene a 00_GestBib (id_area_sezione)
	 */
	public void setParent(String value) {
		this.parent = value;
	}

	/**
	 * Id del codice are a cui la sezione appartiene. Eg  la sezioen 01_GestBibInterrogazione appartiene a 00_GestBib (id_area_sezione)
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * Ordine di prospettazione
	 */
	public void setSeq_ordinamento(int value) {
		this.seq_ordinamento = value;
	}

	/**
	 * Ordine di prospettazione
	 */
	public int getSeq_ordinamento() {
		return seq_ordinamento;
	}

	/**
	 * Codice di attività di appartenenenza. Usato da solo o con il parametro per verificare abilitazione per uso del default
	 */
	public void setCodice_attivita(String value) {
		this.codice_attivita = value;
	}

	/**
	 * Codice di attività di appartenenenza. Usato da solo o con il parametro per verificare abilitazione per uso del default
	 */
	public String getCodice_attivita() {
		return codice_attivita;
	}

	/**
	 * parametro da usarsi in congiunzione con in codice attività per verificare se abilitati o meno ad usare il default
	 */
	public void setParametro_attivita(String value) {
		this.parametro_attivita = value;
	}

	/**
	 * parametro da usarsi in congiunzione con in codice attività per verificare se abilitati o meno ad usare il default
	 */
	public String getParametro_attivita() {
		return parametro_attivita;
	}

	/**
	 * Identificativo del modulo funzionale (eg. acquisizioni, servizi, ecc.) per abilitare o meno il default
	 */
	public void setCodice_modulo(short value) {
		setCodice_modulo(new Short(value));
	}

	/**
	 * Identificativo del modulo funzionale (eg. acquisizioni, servizi, ecc.) per abilitare o meno il default
	 */
	public void setCodice_modulo(Short value) {
		this.codice_modulo = value;
	}

	/**
	 * Identificativo del modulo funzionale (eg. acquisizioni, servizi, ecc.) per abilitare o meno il default
	 */
	public Short getCodice_modulo() {
		return codice_modulo;
	}

	public void setTbf_default(java.util.Set value) {
		this.Tbf_default = value;
	}

	public java.util.Set getTbf_default() {
		return Tbf_default;
	}


	public String toString() {
		return String.valueOf(getId_config());
	}

}
