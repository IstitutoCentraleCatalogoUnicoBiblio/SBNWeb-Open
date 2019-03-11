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
package it.iccu.sbn.polo.orm.statistiche;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxyHelper;
/**
 * ORM-Persistable Class
 */
public class Tbf_config_statistiche implements Serializable {

	private static final long serialVersionUID = -1392766060510181547L;

	public Tbf_config_statistiche() {
	}

	private int id_stat;

	private String id_area_sezione;

	private String parent;

	private int seq_ordinamento;

	private String codice_attivita;

	private String parametro_attivita;

	private Short codice_modulo;

	private String nome_statistica;

	private String tipo_query;

	private String query;

	private String colonne_output;

	private String fl_polo_biblio;

	private String fl_txt;

	private java.util.Set Tb_stat_parameter = new java.util.HashSet();
	//	private java.util.Set Tbf_default = new java.util.HashSet();

	public int getId_stat() {
		return id_stat;
	}

	public void setId_stat(int id_stat) {
		this.id_stat = id_stat;
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


	public String getNome_statistica() {
		return nome_statistica;
	}

	public void setNome_statistica(String nomeStatistica) {
		nome_statistica = nomeStatistica;
	}

	public String getTipo_query() {
		return tipo_query;
	}

	public void setTipo_query(String tipoQuery) {
		tipo_query = tipoQuery;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public java.util.Set getTb_stat_parameter() {
		return Tb_stat_parameter;
	}

	public void setTb_stat_parameter(java.util.Set tb_stat_parameter) {
		Tb_stat_parameter = tb_stat_parameter;
	}

	public String getColonne_output() {
		return colonne_output;
	}

	public void setColonne_output(String colonneOutput) {
		colonne_output = colonneOutput;
	}

	public String getFl_polo_biblio() {
		return fl_polo_biblio;
	}

	public void setFl_polo_biblio(String flPoloBiblio) {
		fl_polo_biblio = flPoloBiblio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_stat;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(HibernateProxyHelper.getClassWithoutInitializingProxy(obj) == Tbf_config_statistiche.class))
			return false;
		Tbf_config_statistiche other = (Tbf_config_statistiche) obj;
		if (id_stat != other.id_stat)
			return false;
		return true;
	}

	public String getFl_txt() {
		return fl_txt;
	}

	public void setFl_txt(String fl_txt) {
		this.fl_txt = fl_txt;
	}

}
