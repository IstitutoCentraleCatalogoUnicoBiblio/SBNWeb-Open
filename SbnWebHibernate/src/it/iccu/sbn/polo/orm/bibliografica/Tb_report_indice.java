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
package it.iccu.sbn.polo.orm.bibliografica;

import it.iccu.sbn.polo.orm.Tb_base;
/**
 * NUMERI STANDARD
 */
/**
 * ORM-Persistable Class
 */
public class Tb_report_indice extends Tb_base {

	private static final long serialVersionUID = -23276354225582757L;
	private int id;
	private String nome_lista;

	private String data_prod_lista;

	private java.util.Set<Tb_report_indice_id_locali> Tb_report_indice_id_locali = new java.util.HashSet<Tb_report_indice_id_locali>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome_lista() {
		return nome_lista;
	}

	public void setNome_lista(String nomeLista) {
		nome_lista = nomeLista;
	}

	public String getData_prod_lista() {
		return data_prod_lista;
	}

	public void setData_prod_lista(String dataProdLista) {
		data_prod_lista = dataProdLista;
	}

	public java.util.Set<Tb_report_indice_id_locali> getTb_report_indice_id_locali() {
		return Tb_report_indice_id_locali;
	}

	public void setTb_report_indice_id_locali(
			java.util.Set<Tb_report_indice_id_locali> tb_report_indice_id_locali) {
		Tb_report_indice_id_locali = tb_report_indice_id_locali;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", "
				+ (nome_lista != null ? "nome_lista=" + nome_lista : "") + "]";
	}

}
