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
public class Tb_report_indice_id_locali extends Tb_base {

	private static final long serialVersionUID = 7355290042316617148L;
	private int id;
	private it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice id_lista;
	private String id_oggetto_locale;
	private Character risultato_confronto;
	private Character stato_lavorazione;
	private Character tipo_trattamento_selezionato;
	private String id_arrivo_fusione;

	private java.util.Set<Tb_report_indice_id_arrivo> Tb_report_indice_id_arrivo = new java.util.HashSet<Tb_report_indice_id_arrivo>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice getId_lista() {
		return id_lista;
	}

	public void setId_lista(
			it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice idLista) {
		id_lista = idLista;
	}

	public String getId_oggetto_locale() {
		return id_oggetto_locale;
	}

	public void setId_oggetto_locale(String idOggettoLocale) {
		id_oggetto_locale = idOggettoLocale;
	}

	public Character getRisultato_confronto() {
		return risultato_confronto;
	}

	public void setRisultato_confronto(Character risultatoConfronto) {
		risultato_confronto = risultatoConfronto;
	}

	public Character getStato_lavorazione() {
		return stato_lavorazione;
	}

	public void setStato_lavorazione(Character statoLavorazione) {
		stato_lavorazione = statoLavorazione;
	}

	public Character getTipo_trattamento_selezionato() {
		return tipo_trattamento_selezionato;
	}

	public void setTipo_trattamento_selezionato(Character tipoTrattamentoSelezionato) {
		tipo_trattamento_selezionato = tipoTrattamentoSelezionato;
	}

	public String getId_arrivo_fusione() {
		return id_arrivo_fusione;
	}

	public void setId_arrivo_fusione(String idArrivoFusione) {
		id_arrivo_fusione = idArrivoFusione;
	}

	public java.util.Set<Tb_report_indice_id_arrivo> getTb_report_indice_id_arrivo() {
		return Tb_report_indice_id_arrivo;
	}

	public void setTb_report_indice_id_arrivo(
			java.util.Set<Tb_report_indice_id_arrivo> tb_report_indice_id_arrivo) {
		Tb_report_indice_id_arrivo = tb_report_indice_id_arrivo;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", "
				+ (id_lista != null ? "id_lista=" + id_lista : "") + "]";
	}

}
