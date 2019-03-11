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
public class Tb_report_indice_id_arrivo extends Tb_base {

	private static final long serialVersionUID = -7940663306598580202L;

	private int id;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice_id_locali id_lista_ogg_loc;
	private String id_oggetto_locale;
	private String id_arrivo_fusione;
	private Character tipologia_uguaglianza;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getId_arrivo_fusione() {
		return id_arrivo_fusione;
	}

	public void setId_arrivo_fusione(String idArrivoFusione) {
		id_arrivo_fusione = idArrivoFusione;
	}

	public Character getTipologia_uguaglianza() {
		return tipologia_uguaglianza;
	}

	public void setTipologia_uguaglianza(Character tipologiaUguaglianza) {
		tipologia_uguaglianza = tipologiaUguaglianza;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice_id_locali getId_lista_ogg_loc() {
		return id_lista_ogg_loc;
	}

	public void setId_lista_ogg_loc(
			it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice_id_locali idListaOggLoc) {
		id_lista_ogg_loc = idListaOggLoc;
	}

	public String getId_oggetto_locale() {
		return id_oggetto_locale;
	}

	public void setId_oggetto_locale(String idOggettoLocale) {
		id_oggetto_locale = idOggettoLocale;
	}

	@Override
	public String toString() {
		return "[id="
				+ id
				+ ", "
				+ (id_lista_ogg_loc != null ? "id_lista_ogg_loc="
						+ id_lista_ogg_loc + ", " : "")
				+ (id_oggetto_locale != null ? "id_oggetto_locale="
						+ id_oggetto_locale + ", " : "")
				+ (id_arrivo_fusione != null ? "id_arrivo_fusione="
						+ id_arrivo_fusione : "") + "]";
	}

}
