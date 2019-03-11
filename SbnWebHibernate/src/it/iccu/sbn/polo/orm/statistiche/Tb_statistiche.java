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
/**
 * Materie d'interesse
 */
/**
 * ORM-Persistable Class
 */
public class Tb_statistiche implements Serializable {

	private static final long serialVersionUID = 448540489014810375L;


	public Tb_statistiche() {
	}

	private int id_stat;

	private String titolo;

	private String tipo_conn;

	private String query_str;

	private String tipo_query;

	private java.util.Set Tb_stat_parameter = new java.util.HashSet();


//	private String[] oggetto;

	public int getId_stat() {
		return id_stat;
	}

	public void setId_stat(int id_stat) {
		this.id_stat = id_stat;
	}



	public String getTipo_conn() {
		return tipo_conn;
	}

	public void setTipo_conn(String tipo_conn) {
		this.tipo_conn = tipo_conn;
	}

	public String getTipo_query() {
		return tipo_query;
	}

	public void setTipo_query(String tipo_query) {
		this.tipo_query = tipo_query;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}



	public java.util.Set getTb_stat_parameter() {
		return Tb_stat_parameter;
	}

	public void setTb_stat_parameter(java.util.Set tb_stat_parameter) {
		Tb_stat_parameter = tb_stat_parameter;
	}

	public String getQuery_str() {
		return query_str;
	}

	public void setQuery_str(String query_str) {
		this.query_str = query_str;
	}

}
